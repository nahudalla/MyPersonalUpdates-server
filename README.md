# MyPersonalUpdates-server

## Dependencias

Para compilar este proyecto adecuadamente, es necesaria la librería
Gson de Google, versión 2.8.2 o superior. Actualmente (Julio de 2017)
esta versión sólo está disponible como SNAPSHOT de GitHub y es
necesario compilado e instalación con Maven manual.

El resto de dependencias que se incluyen en el archivo pom.xml se
descargan automáticamente con Maven 2.

## Configuración

Comentario extraído del archivo [Config.java](src/main/java/com/mypersonalupdates/Config.java).

```java
/*

En el directorio "src/main/resources" se encuentra el archivo
"default.conf" que contiene la configuración por defecto del
servidor.

****************************************************************
*                       ¡¡¡ATENCIÓN!!!                         *
*                                                              *
* EL ARCHIVO "default.conf" NO INCLUYE VALORES POR DEFECTO DE  *
* ALGUNOS DE LOS PARÁMETROS DE CONFIGURACIÓN Y ES NECESARIO    *
* ESPECIFICARLOS EN EL ARCHIVO "config.conf" POR SEPARADO PARA *
* QUE EL SERVIDOR FUNCIONE CORRECTAMENTE.                      *
*                                                              *
* Los parámetros de configuración sin valor por defecto son:   *
* - Parámetros de conexión a la base de datos                  *
* - Identificador y clave de acceso a la API de Twitter        *
* - Clave de firma para los tokens de acceso JWT               *
*                                                              *
* Los valores que se deben incluir en el archivo "config.conf" *
* se encuentran identificados por comentarios en el archivo    *
* "default.conf".                                              *
*                                                              *
****************************************************************

¡¡IMPORTANTE!!

El archivo de configuración "config.conf" NO SE INCLUYE en el
código fuente por motivos de seguridad, ya que contiene claves
de acceso a APIs de servicios externos e información sobre la
conexión a la base de datos, entre otras cosas.

Tanto el archivo "default.conf" como el archivo "config.conf"
DEBEN ESTAR PRESENTES AL MOMENTO DE COMPILAR EL SERVIDOR
EN EL DIRECTORIO "src/main/resources". De lo contrario, la
ejecución del programa final ya sea empaquetado o no, fallará.

*/
```