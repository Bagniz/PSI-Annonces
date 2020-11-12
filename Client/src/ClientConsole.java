import java.io.IOException;
import java.net.Socket;

public class ClientConsole {
    public static void main(String[] args) throws IOException {
        Socket serverConnectionSocket;

        while (true){
            try {
                serverConnectionSocket = new Socket("localhost", 8080);
                System.out.println("Connected to server");
                break;
            } catch (IOException ignore) {}
        }

        ServerDaemon serverDaemon = null;
        try {
            serverDaemon = new ServerDaemon(serverConnectionSocket);
        } catch (IOException e) {
            System.out.println("There is no connection to server");
        }

        if(serverDaemon != null && serverDaemon.authenticate()){
            System.out.println("Hello there, here are some commands to try");
            while(true){
                if(!serverDaemon.doAction())
                    break;
            }
        }
        else{
            System.out.println("Bye Bye");
        }
    }
}
