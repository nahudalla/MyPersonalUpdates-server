package com.mypersonalupdates.webserver.websockets;

import com.mypersonalupdates.users.Category;
import com.mypersonalupdates.users.User;

import javax.websocket.Session;

/**
 * Esta interfaz representa un callback, es decir,
 * un objeto al que se le debe avisar cuando una
 * conexión al servicio de streaming en tiempo real
 * fue procesada.
 */
public interface WebSocketConnectionHandledCallback {
    void done(Session session, User user, Category category);
}
