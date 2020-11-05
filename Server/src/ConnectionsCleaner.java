import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ConnectionsCleaner extends Thread{

    // Attribute
    ArrayList<ClientHandler> clients;

    // Constructor
    public ConnectionsCleaner(ArrayList<ClientHandler> clients){
        this.clients = clients;
    }

    public void run() {
        // Check and remove none connected clients every second
        while(true){
            clients.removeIf(client -> !client.getClientConnection().isConnected());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
