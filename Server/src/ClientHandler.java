import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientHandler extends Thread{
    Socket clientConnection;
    public ClientHandler(Socket clientConnection)
    {
        this.clientConnection = clientConnection;
    }

    public void run(){
        OutputStreamWriter out = null;
        InputStreamReader in = null;
        try {
            in = new InputStreamReader(clientConnection.getInputStream());
            out = new OutputStreamWriter((clientConnection.getOutputStream()));
            out.write("Hello there, What do you want to do ?\n");
            out.write("1. Log in\n");
            out.write("2. Sign up\n");
            out.flush();
            System.out.println(in.read());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
