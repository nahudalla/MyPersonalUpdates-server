package com.mypersonalupdates.webserver.json;

import com.google.gson.JsonElement;

/**
 * Esta interfaz representa a un objeto que puede
 * ser convertido a formato JSON.
 */
public interface JSONSerializable {
    JsonElement toJSON();
}
