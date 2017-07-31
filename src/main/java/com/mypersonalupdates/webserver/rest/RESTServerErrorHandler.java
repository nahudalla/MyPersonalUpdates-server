package com.mypersonalupdates.webserver.rest;

import com.mypersonalupdates.webserver.Response;
import com.mypersonalupdates.exceptions.SealedException;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.util.BufferUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.Writer;
import java.nio.ByteBuffer;

/**
 * Esta clase se encarga de generar un mensaje de error
 * en formato JSON. Los errores son internos del servidor
 * y no tienen relación con el procesamiento de una
 * petición por el sistema.
 */
@Provider
public final class RESTServerErrorHandler extends ErrorHandler {
    @Override
    protected void generateAcceptableResponse(Request baseRequest, HttpServletRequest request, HttpServletResponse response, int code, String message, String mimeType) throws IOException {
        switch (mimeType) {
            case "text/*":
            case "application/json":
            case "*/*":
                baseRequest.setHandled(true);
                Writer writer = this.getAcceptableWriter(baseRequest,request,response);
                if (writer!=null)
                {
                    response.setContentType(MimeTypes.Type.APPLICATION_JSON.asString());
                    this.handleJSONErrorPage(writer, code);
                }
        }

        if(!baseRequest.isHandled())
            super.generateAcceptableResponse(baseRequest, request, response, code, message, mimeType);
    }

    private void handleJSONErrorPage(Writer writer, int code) throws IOException {
        try {
            writer.write(
                    new Response()
                            .setIncludeStatusInBody(false)
                            .setMessage(HttpStatus.getMessage(code))
                            .toString()
            );
        } catch (SealedException e) {
            throw new AssertionError(e);
        }
        writer.flush();
        writer.close();
    }

    @Override
    public ByteBuffer badMessageError(int status, String reason, HttpFields fields) {
        if (reason==null)
            reason=HttpStatus.getMessage(status);
        fields.put(HttpHeader.CONTENT_TYPE,MimeTypes.Type.APPLICATION_JSON.asString());
        try {
            return BufferUtil.toBuffer(
                    new Response()
                            .setIncludeStatusInBody(false)
                            .setMessage(reason)
                            .toString()
            );
        } catch (SealedException e) {
            throw new AssertionError(e);
        }
    }
}
