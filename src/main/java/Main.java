import com.mypersonalupdates.webserver.WebServer;

public class Main {
    public static void main(String[] args) {
        WebServer.getInstance().init(8080);
    }
}