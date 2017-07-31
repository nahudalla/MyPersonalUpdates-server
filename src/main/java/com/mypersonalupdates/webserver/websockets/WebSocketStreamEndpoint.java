package com.mypersonalupdates.webserver.websockets;

import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.realtime.RealTimeStreamsManager;
import com.mypersonalupdates.realtime.UpdatesConsumer;
import com.mypersonalupdates.webserver.Server;
import com.mypersonalupdates.webserver.handlers.Handler;
import com.mypersonalupdates.webserver.handlers.RequestProcessingException;
import com.mypersonalupdates.webserver.handlers.websocket.WebSocketConnectionHandler;
import com.mypersonalupdates.webserver.responses.builders.UpdateResponseBuilder;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * Esta clase representa el endpoint (lugar donde se
 * reciben las conexiones) del servicio de streaming
 * en tiempo real.
 */
@ClientEndpoint
@ServerEndpoint(value = "/stream/{categoryName}/{authToken}")
public final class WebSocketStreamEndpoint {
    private static final Handler<String> connectionHandler =
            new Handler.Builder<>(new WebSocketResponseProcessor())
                    .build(new WebSocketConnectionHandler<>());

    private Long subscriptionID = null;

    private final WebSocketConnectionHandledCallback callback = (session, user, category) -> {
        this.clearSubscription();

        final RemoteEndpoint.Async remoteEndpoint = session.getAsyncRemote();

        final UpdatesConsumer consumer = update -> {
            String JSONUpdate = null;
            try {
                JSONUpdate = Server.JSON.toJson(
                        new UpdateResponseBuilder(update)
                                .includeProviderID()
                                .includeTimestamp()
                                .includeAttributes()
                                .build()
                );
            } catch (DBException e) {
                e.printStackTrace();
                // TODO: Log
            }
            if (JSONUpdate != null)
                remoteEndpoint.sendText(JSONUpdate);
        };

        boolean error;
        try {
            this.subscriptionID = RealTimeStreamsManager.getInstance().subscribe(category, consumer);
            error = this.subscriptionID == null;
        } catch (Exception e) {
            error = true;
        }

        if (error) {
            try {
                session.getBasicRemote().sendText(Handler.INTERNAL_ERROR_RESPONSE.toString());
                session.close();
            } catch (IOException e1) {
                e1.printStackTrace();
                // TODO: Log
            }
        }
    };

    private void clearSubscription() {
        if (this.subscriptionID == null) return;
        RealTimeStreamsManager.getInstance().unsubscribe(this.subscriptionID);
        this.subscriptionID = null;
    }

    @OnOpen
    public void onWebSocketConnect(
            Session sess,
            @PathParam("categoryName") String categoryName,
            @PathParam("authToken") String authToken
    ) {
        try {
            connectionHandler.process(WebSocketRequest.create(
                    sess, authToken, categoryName, callback
            ));
        } catch (RequestProcessingException e) {
            try {
                sess.getBasicRemote().sendText(e.getResponse().toString());
                sess.close();
            } catch (IOException e1) {
                e1.printStackTrace();
                // TODO: Log
            }
        }
    }

    @OnClose
    public void onWebSocketClose(Session session, CloseReason reason) {
        this.clearSubscription();
    }

    @OnError
    public void onWebSocketError(Session session, Throwable cause) {
        this.clearSubscription();
        cause.printStackTrace(System.err);
        // TODO: log
    }
}
