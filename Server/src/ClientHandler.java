import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientHandler extends Thread{
    // Attributes
    private final Socket clientConnection;
    private final Database database;
    private OutputStreamWriter writer;
    private BufferedReader reader;
    private int clientId;

    // Constructor
    public ClientHandler(Socket clientConnection, Database database)
    {
        // Attributes
        this.clientConnection = clientConnection;
        this.database = database;
        try {
            this.writer = new OutputStreamWriter((clientConnection.getOutputStream()));
            this.reader = new BufferedReader(new InputStreamReader(clientConnection.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getClientConnection(){
        return this.clientConnection;
    }

    public void run(){
        // Authenticating new clients
        this.authenticateClient();
        this.requestListener();
    }

    private void requestListener() {
        String request;
        while (true) {
            try {
                if (((request = this.reader.readLine()) != null)) {
                    String [] requestTab = request.split("\\|");
                    switch (requestTab[0]){
                        case "GETAD":{
                            String ad = database.getAnAd(Integer.parseInt(requestTab[1]));
                            if(ad.equals("null"))
                                this.writer.write("Ad of id: " + requestTab[1] + " does not exist\n");
                            else
                                this.writer.write(ad + "\n");
                            this.writer.flush();
                            break;
                        }

                        case "ADDAD":{
                            int id = database.addAd(requestTab[1],requestTab[2],Float.parseFloat(requestTab[3]),Integer.parseInt(requestTab[4]), clientId);
                            System.out.println(id);
                            this.writer.write(id + "\n");
                            this.writer.flush();
                            break;
                        }

                        case "UPDATEAD":{
                            if(database.updateAd(Integer.parseInt(requestTab[1]), requestTab[2], requestTab[3], Float.parseFloat(requestTab[4]), Integer.parseInt(requestTab[5]), clientId))
                                this.writer.write("success\n");
                            else
                                this.writer.write("error\n");
                            this.writer.flush();
                            break;
                        }

                        case "DELETEAD":{
                            if(database.deleteAd(Integer.parseInt(requestTab[1]), clientId))
                                this.writer.write("success\n");
                            else
                                this.writer.write("error\n");
                            this.writer.flush();
                            break;
                        }

                        default:{
                            int response = -1;
                            this.writer.write(response + "\n");
                            this.writer.flush();
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
                    this.clientId = id;
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
    }
}
