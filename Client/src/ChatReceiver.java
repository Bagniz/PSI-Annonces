import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChatReceiver extends Thread {
    // Attributes
    private ServerSocket serverSocket;
    private final int id;
    private final String name;
    private final MyJFrame frame;
    private final AtomicBoolean isSender;

    // Constructor
    public ChatReceiver(MyJFrame frame, int id, String name, AtomicBoolean isSender){
        try {
            this.serverSocket = new ServerSocket(6060 + id);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        this.frame = frame;
        this.id = id;
        this.name = name;
        this.isSender = isSender;
    }

    @Override
    public void run() {
        Message message = null;
        do {
            Socket messengerSocket;
            ObjectInputStream reader;
            try {
                messengerSocket = serverSocket.accept();
                reader = new ObjectInputStream(messengerSocket.getInputStream());
            } catch (IOException exception) {
                exception.printStackTrace();
                break;
            }
            ChatSender chatSender = null;
            try {
                message = (Message)(reader.readObject());
            } catch (IOException | ClassNotFoundException ignore) {
            }
            if(!isSender.get()){
                assert message != null;
                Socket socket = null;
                try {
                    socket = new Socket(messengerSocket.getLocalAddress(), 6060 + message.getClientId());
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                assert socket != null;
                chatSender = new ChatSender(socket, this.id, this.name, this.frame, isSender);
                chatSender.start();
            }
            do{
                try {
                    message = (Message)(reader.readObject());
                    frame.printToChat("[" + message.getClientId() + "|" + message.getClientName() + "] > " + message.getClientMessage());
                } catch (IOException | ClassNotFoundException exception) {
                    exception.printStackTrace();
                    break;
                }
            }while(!message.getClientMessage().equals("END"));
            if(chatSender != null)
                chatSender.stop();
        } while (true);
    }
}
