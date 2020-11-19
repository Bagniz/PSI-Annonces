import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ChatSender extends Thread {
    // Attributes
    private ObjectOutputStream writer;
    private final int clientId;
    private final String clientName;

    // Constructor
    public ChatSender(Socket chatConnection, int clientId, String clientName){
        try {
            this.writer = new ObjectOutputStream(chatConnection.getOutputStream());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        this.clientId = clientId;
        this.clientName = clientName;
    }

    @Override
    public void run() {
        Scanner reader = new Scanner(System.in);
        do{
            String clientMessage = reader.nextLine();
            try {
                writer.writeObject(new Message(clientId, clientName, clientMessage));
                writer.flush();
            } catch (IOException exception) {
                exception.printStackTrace();
                break;
            }
        } while (true);
    }
}
