import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread{
    private final Socket clientConnection;
    private final Database database;
    private final ObjectOutputStream writer;
    private final ObjectInputStream reader;
    private Request request;
    private Client client;

    public ClientHandler(Socket clientConnection, Database database) throws IOException {
        this.clientConnection = clientConnection;
        this.database = database;
        this.writer = new ObjectOutputStream(this.clientConnection.getOutputStream());
        this.reader = new ObjectInputStream(this.clientConnection.getInputStream());
    }

    public Socket getClientConnection(){
        return this.clientConnection;
    }

    @Override
    public void run() {
        try {
            this.authenticateClient();
            this.handleClient();
        } catch (Exception e) {
            e.printStackTrace();
            Response response = new Response();
            response.setMessage("Exception was raised");
            try {
                this.writer.writeObject(response);
            } catch (IOException ioException) {
                System.out.println("Client disconnected");
            }
        }
    }

    private void authenticateClient() throws IOException, ClassNotFoundException {
        request = (Request) this.reader.readObject();
        switch (request.getCommand()){
            case SIGNUP:{
                Response response = database.SignUp(request.getClient().getFirstName(),
                        request.getClient().getLastName(),
                        request.getClient().getBirthdate(),
                        request.getClient().getEmail(),
                        request.getClient().getPassword(),
                        request.getClient().getAddress(),
                        request.getClient().getPostalCode(),
                        request.getClient().getCity(),
                        request.getClient().getPhoneNumber());
                this.client = response.getClient();
                this.writer.writeObject(response);
                break;
            }

            case LOGIN:{
                Response response = database.logIn(request.getClient().getEmail(), request.getClient().getPassword());
                this.client = response.getClient();
                this.writer.writeObject(response);
                break;
            }

            default:{
                Response response = new Response();
                response.setMessage("Wrong command");
                this.writer.writeObject(response);
                break;
            }
        }
    }

    private void handleClient() throws IOException, ClassNotFoundException {
        while(true){
            request = (Request) this.reader.readObject();
            switch (request.getCommand()){
                case GETADS:{
                    Response response = database.getAds(request.isAreMine(), this.client.getId());
                    this.writer.writeObject(response);
                    break;
                }

                case GETAD:{
                    Ad ad = database.getAd(request.getAd().getId());
                    Response response = new Response();
                    if(ad == null)
                        response.setMessage("Ad with id (" + request.getAd().getId() + ") does not exist");
                    else
                        response.setAd(ad);
                    this.writer.writeObject(response);
                    break;
                }

                case ADDAD:{
                    Response response = database.addAd(request.getAd().getTitle(),
                            request.getAd().getDescription(),
                            request.getAd().getPrice(),
                            request.getAd().getCategory().getId(),
                            this.client.getId());
                    this.writer.writeObject(response);
                    break;
                }

                case UPDATEAD:{
                    Response response = database.updateAd(request.getAd().getId(),
                            request.getAd().getTitle(),
                            request.getAd().getDescription(),
                            request.getAd().getPrice(),
                            request.getAd().getCategory().getId(),
                            this.client.getId());
                    this.writer.writeObject(response);
                    break;
                }

                case DELETEAD:{
                    Response response = database.deleteAd(request.getAd().getId(), this.client.getId());
                    this.writer.writeObject(response);
                    break;
                }

                case GETRESERVEDADS:{
                    Response response = database.getReservedAds(this.client.getId());
                    this.writer.writeObject(response);
                    break;
                }

                case RESERVEAD:{
                    Response response = database.reserveAd(request.getAd().getId(), this.client.getId());
                    this.writer.writeObject(response);
                    break;
                }

                case UNRESERVEAD:{
                    Response response = database.unReserveAd(request.getAd().getId(), this.client.getId());
                    this.writer.writeObject(response);
                    break;
                }

                case GETCLIENTINFO:{
                    Response response = new Response();
                    response.setClient(this.client);
                    this.writer.writeObject(response);
                    break;
                }

                case UPDATECLIENT:{
                    Response response = database.updateClient(request.getClient().getFirstName(),
                            request.getClient().getLastName(),
                            request.getClient().getBirthdate(),
                            request.getClient().getEmail(),
                            request.getClient().getPassword(),
                            request.getNewPassword(),
                            request.getClient().getAddress(),
                            request.getClient().getPostalCode(),
                            request.getClient().getCity(),
                            request.getClient().getPhoneNumber(),
                            this.client.getId());
                    this.writer.writeObject(response);
                    break;
                }

                case DELETECLIENT:{
                    Response response = database.deleteClient(this.client.getId(), request.getClient().getPassword());
                    this.writer.writeObject(response);
                    break;
                }

                default:{
                    Response response = new Response();
                    response.setMessage("Wrong command");
                    this.writer.writeObject(response);
                    break;
                }
            }
        }
    }
}
