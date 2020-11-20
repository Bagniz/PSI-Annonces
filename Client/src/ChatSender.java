import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChatSender extends Thread {
    // Attributes
    private ObjectOutputStream writer;
    private final int clientId;
    private final String clientName;
    private final MyJFrame frame;
    private final AtomicBoolean isSender;

    // Constructor
    public ChatSender(Socket chatConnection, int clientId, String clientName, MyJFrame frame, AtomicBoolean isSender){
        try {
            this.writer = new ObjectOutputStream(chatConnection.getOutputStream());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        this.clientId = clientId;
        this.clientName = clientName;
        this.frame = frame;
        this.isSender = isSender;
    }

    @Override
    public void run() {
        if(!isSender.get()){
            try {
                writer.writeObject(new Message(clientId, clientName, "my info"));
                writer.flush();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        String clientMessage;
        frame.printToChat("Start chatting boy");
        do{
            clientMessage = frame.readFromChat();
            try {
                writer.writeObject(new Message(clientId, clientName, clientMessage));
                writer.flush();
            } catch (IOException exception) {
                exception.printStackTrace();
                break;
            }
        } while (!clientMessage.equals("END"));
        try {
            this.writer.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        isSender.set(false);
    }
}
