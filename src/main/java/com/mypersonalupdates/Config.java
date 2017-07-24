package com.mypersonalupdates;

import com.typesafe.config.ConfigFactory;

public class Config {
    private static final String FILENAME = "config.conf";

    private static com.typesafe.config.Config config = ConfigFactory.load(FILENAME);
    public static com.typesafe.config.Config get() {
        return config;
    }

/*

El archivo de configuración "config.conf" no se incluye en el
código de fuente por motivos de seguridad, ya que contiene
claves de acceso a APIs de servicios externos entre otras cosas.

El archivo "config.conf" debe estar presente al momento de compilar
en el directorio "src/main/resources".

FORMATO DEL ARCHIVO DE CONFIGURACIÓN "config.conf":

# Se puede copiar y pegar desde esta linea

# NO DIVULGAR ESTE ARCHIVO, CONTIENE INFORMACIÓN DE
# CARACTER SENSIBLE

# Conexion a la base de datos
db {
    url = ""
    user = ""
    password = ""
}

providers.twitter {
    # Claves de acceso a API de Twitter
    consumerKey = ""
    consumerSecret = ""

    databaseProviderID = 1

    DBFollowingsAttrIDs = []
    DBTrackTermsAttrIDs = []

    UpdateAttributesIDs {
        text =
    }

    TweetSearchLanguages = ["es"]
    EnableStallWarnings = false
    msgQueueSize = 100000
    maxMsgProcessingThreadsPerUser = 2
}

# Se puede copiar y pegar hasta esta linea

*/

    private Config() {}
}
