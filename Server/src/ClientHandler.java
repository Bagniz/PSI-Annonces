import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread{

    // Attributes
    private final ArrayList<ClientHandler> clients;
    private final Socket clientConnection;
    private final Database database;
    private OutputStreamWriter writer;
    private BufferedReader reader;
    private Requests requestCode;
    private int clientId;

    // Constructor
    public ClientHandler(Socket clientConnection, Database database, ArrayList<ClientHandler> clients)
    {
        this.clientConnection = clientConnection;
        this.database = database;
        this.requestCode = null;
        try {
            this.writer = new OutputStreamWriter((clientConnection.getOutputStream()));
            this.reader = new BufferedReader(new InputStreamReader(clientConnection.getInputStream()));
        } catch (IOException e) {
            System.out.println("Could not get client input/output stream");
        }
        this.clients = clients;
    }

    public Socket getClientConnection(){
        return this.clientConnection;
    }

    public void run(){
        // Client authentication menu
        clientAuthenticationMenu();

        // Authenticating new clients
        this.authenticateClient();

        // Listen to clients requests
        this.requestListener();
    }

    // Send SignUp and LogIn menu
    private void clientAuthenticationMenu(){
        try {
            this.writer.write("Hello there, What do you want to do ?\n");
            this.writer.write("1. Log in\n");
            this.writer.write("2. Sign up\n");
            this.writer.write("\r\n");
            this.writer.flush();
        } catch (IOException exception) {
            System.out.println("Could not send client authentication menu");
        }
    }

    // Authenticating new clients (Log In/Sign Up)
    private void authenticateClient(){
        String[] request;
        int idClient;

        try {
            // Wait for clients request and execute the right operation
            request = this.reader.readLine().split("\\|");
            this.requestCode = Requests.valueOf(request[0]);
            switch (this.requestCode){
                case SIGNUP:{
                    idClient = database.signUp(request[1], request[2], request[3], request[4], request[5], request[6], Integer.parseInt(request[7]), request[8], request[9]);
                    this.clientId = idClient;

                    this.writer.write(idClient + "\n");
                    this.writer.flush();
                    break;
                }
                case LOGIN:{
                    idClient = database.logIn(request[1], request[2]);
                    this.clientId = idClient;

                    this.writer.write(idClient + "\n");
                    this.writer.flush();
                    break;
                }
                default:{
                    idClient = -1;

                    this.writer.write(idClient + "\n");
                    this.writer.flush();
                    break;
                }
            }
        } catch (Exception exception) {
            System.out.println("Could not send authentication response to client");
            idClient = -1;
            try {
                this.writer.write(idClient + "\n");
                this.writer.flush();
            } catch (IOException ignore) {}
        }


    }

    // Listen to an authenticated clients requests
    private void requestListener() {
        String request;

        while (true) {
            try {
                if ((request = this.reader.readLine()) != null) {
                    String [] requestTab = request.split("\\|");
                    this.requestCode = Requests.valueOf(requestTab[0]);
                    switch (this.requestCode){
                        case GETADS:{
                            String ads = database.getAds(Boolean.parseBoolean(requestTab[1]), clientId);
                            if(ads.equals("null"))
                                this.writer.write("There are no ads\n");
                            else
                                this.writer.write(ads + "\n");
                            this.writer.flush();
                            break;
                        }

                        case GETAD:{
                            String ad = database.getAnAd(Integer.parseInt(requestTab[1]));
                            if(ad.equals("null"))
                                this.writer.write("Ad of id: " + requestTab[1] + " does not exist\n");
                            else
                                this.writer.write(ad + "\n");
                            this.writer.flush();
                            break;
                        }

                        case ADDAD:{
                            int id = database.addAd(requestTab[1],requestTab[2],Float.parseFloat(requestTab[3]),Integer.parseInt(requestTab[4]), clientId);
                            System.out.println(id);
                            this.writer.write(id + "\n");
                            this.writer.flush();
                            break;
                        }

                        case UPDATEAD:{
                            if(database.updateAd(Integer.parseInt(requestTab[1]), requestTab[2], requestTab[3], Float.parseFloat(requestTab[4]), Integer.parseInt(requestTab[5]), clientId))
                                this.writer.write("success\n");
                            else
                                this.writer.write("error\n");
                            this.writer.flush();
                            break;
                        }

                        case DELETEAD:{
                            if(database.deleteAd(Integer.parseInt(requestTab[1]), clientId))
                                this.writer.write("success\n");
                            else
                                this.writer.write("error\n");
                            this.writer.flush();
                            break;
                        }

                        case GETRESERVEDADS:{
                            String ads = database.getReservedAds(clientId);
                            if(ads.equals("null"))
                                this.writer.write("There are no ads\n");
                            else
                                this.writer.write(ads + "\n");
                            this.writer.flush();
                            break;
                        }

                        case RESERVEAD:{
                            if(database.reserveAd(Integer.parseInt(requestTab[1]), clientId))
                                this.writer.write("success\n");
                            else
                                this.writer.write("error\n");
                            this.writer.flush();
                            break;
                        }

                        case UNRESERVEAD:{
                            if(database.unReserveAd(Integer.parseInt(requestTab[1]), clientId))
                                this.writer.write("success\n");
                            else
                                this.writer.write("error\n");
                            this.writer.flush();
                            break;
                        }

                        case GETCLIENTINFO:{
                            String[] clientInfo = this.database.getClientInformation(clientId);
                            if(clientInfo[0] == null){
                                this.writer.write("Account does not exist");
                            }
                            else{
                                StringBuilder clientInfoResponse = new StringBuilder(clientInfo[0]);
                                for(int i = 1;i < 9; i++){
                                    if(i == 4)
                                        continue;
                                    clientInfoResponse.append("|").append(clientInfo[i]);
                                }
                                clientInfoResponse.append("\n");

                                this.writer.write(clientInfoResponse.toString());
                            }
                            this.writer.flush();
                            break;
                        }

                        case UPDATECLIENT:{
                            String[] clientInformationTab = database.getClientInformation(Integer.parseInt(requestTab[11]));
                            int k=1;
                            for (int i =0;i<9;i++)
                            {
                                if(i==5)
                                {
                                    k++;
                                }
                                if (requestTab[i+k].equals("next"))
                                {
                                    requestTab[i+k] = clientInformationTab[i];
                                }
                            }
                            if(database.updateClient(requestTab[1],requestTab[2],requestTab[3],requestTab[4],requestTab[5],requestTab[6],requestTab[7],Integer.parseInt(requestTab[8]),requestTab[9],requestTab[10],Integer.parseInt(requestTab[11])))
                            {
                                this.writer.write("success\n");
                            }
                            else {
                                this.writer.write("error\n");
                            }
                            this.writer.flush();
                            break;
                        }

                        case DELETECLIENT:{
                            if(database.deleteClient(requestTab[1],Integer.parseInt(requestTab[2])))
                                this.writer.write("success\n");
                            else
                                this.writer.write("error\n");
                            this.writer.flush();
                            break;
                        }

                        case CHAT:{
                            String ad;
                            if((ad = database.getAnAd(Integer.parseInt(requestTab[1]))).equals("null"))
                                this.writer.write("error\n");
                            else{
                                int id = Integer.parseInt(ad.split("\\|")[5]);
                                for(ClientHandler clientHandler: clients){
                                    if(id == clientHandler.getClientId()){
                                        this.writer.write( clientHandler.getClientConnection().getLocalAddress().getHostAddress() + "|" + clientHandler.getClientConnection().getPort() + "|" + id + "\n");
                                    }
                                }
                            }
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
                System.out.println("Could not send response to client");
            }
        }
    }

    public int getClientId() {
        return clientId;
    }
}
