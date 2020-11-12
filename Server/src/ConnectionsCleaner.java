import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ConnectionsCleaner extends Thread{
    ArrayList<ClientHandler> clientSessions;

    public ConnectionsCleaner(ArrayList<ClientHandler> clientSessions){
        this.clientSessions = clientSessions;
    }

    @Override
    public void run() {
        while(true){
            clientSessions.removeIf(clientSession -> !clientSession.getClientConnection().isConnected());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
