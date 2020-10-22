import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Responses {
    private final Socket serverConnectionSocket;
    private BufferedReader reader;

    public Responses(Socket serverConnectionSocket){
        this.serverConnectionSocket = serverConnectionSocket;
        try {
            this.reader = new BufferedReader(new InputStreamReader(this.serverConnectionSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen(){
        String line;
        try {
            this.reader = new BufferedReader(new InputStreamReader(this.serverConnectionSocket.getInputStream()));
            while((line = this.reader.readLine()) != null){
                if(line.length() == 0)
                    break;
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
