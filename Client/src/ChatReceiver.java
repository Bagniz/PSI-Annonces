import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ChatReceiver extends Thread {
    // Attributes
    private ObjectInputStream reader;

    // Constructor
    public ChatReceiver(Socket chatConnection){
        try {
            this.reader = new ObjectInputStream(chatConnection.getInputStream());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void run() {
        do{
            try {
                Message message = (Message)(reader.readObject());
                System.out.println("[" + message.getClientId() + "|" + message.getClientName() + "] > " + message.getClientMessage());
            } catch (IOException | ClassNotFoundException exception) {
                exception.printStackTrace();
                break;
            }
        }while(true);
    }
}
