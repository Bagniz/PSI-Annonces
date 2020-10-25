import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientHandler extends Thread{
    // Attributes
    private final Database database;
    private OutputStreamWriter writer;
    private BufferedReader reader;
    private int clientId;

    // Constructor
    public ClientHandler(Socket clientConnection, Database database)
    {
        // Attributes
        this.database = database;
        try {
            this.writer = new OutputStreamWriter((clientConnection.getOutputStream()));
            this.reader = new BufferedReader(new InputStreamReader(clientConnection.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        // Authenticating new clients
        this.authenticateClient();
    }

    private void authenticateClient(){
        // Variables
        int id;

        try {
            // Send SignUp and LogIn menu
            this.writer.write("Hello there, What do you want to do ?\n");
            this.writer.write("1. Log in\n");
            this.writer.write("2. Sign up\n");
            this.writer.write("\r\n");
            this.writer.flush();

            // Wait for clients response and call the right method
            String[] request = this.reader.readLine().split("\\|");
            switch (request[0]){
                case "SIGNUP":{
                    id = database.signUp(request[1], request[2], request[3], request[4], request[5], request[6], Integer.parseInt(request[7]), request[8], request[9]);
                    this.writer.write(id + "\n");
                    this.writer.flush();
                    break;
                }
                case "LOGIN":{
                    id = database.logIn(request[1], request[2]);
                    this.clientId = id;
                    this.writer.write(id + "\n");
                    this.writer.flush();
                    break;
                }
                default:{
                    id = -1;
                    this.writer.write(id + "\n");
                    this.writer.flush();
                    break;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            try {
                this.writer.write(-1 + "\n");
                this.writer.flush();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}
