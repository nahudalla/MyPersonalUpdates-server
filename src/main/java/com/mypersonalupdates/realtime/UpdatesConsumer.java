package com.mypersonalupdates.realtime;

import com.mypersonalupdates.Update;

/**
 * Esta interfaz representa a un consumidor de
 * actualizaciones en tiempo real.
 */
public interface UpdatesConsumer {
    void handleUpdate(Update update);
}
