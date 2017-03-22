import com.mypersonalupdates.webserver.WebServer;

public class Main {
    public static void main(String[] args) {
        WebServer webServer = WebServer.getInstance();

        webServer.init(8080);
    }
}