import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ConnectionsCleaner extends Thread{
    ArrayList<ClientHandler> clients = new ArrayList<>();

    public ConnectionsCleaner(ArrayList<ClientHandler> clients){
        this.clients = clients;
    }

    public void run() {
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
