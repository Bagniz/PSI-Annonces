import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerDaemon {
    private final ObjectInputStream reader;
    private final ObjectOutputStream writer;
    private final Scanner scanner;
    private Request request;

    public ServerDaemon(Socket server) throws IOException {
        this.reader = new ObjectInputStream(server.getInputStream());
        this.writer = new ObjectOutputStream(server.getOutputStream());
        this.scanner = new Scanner(System.in);
    }

    public boolean authenticate(){
        System.out.println("Hello there, What do you want to do ?");
        System.out.println("1. Log In");
        System.out.println("2. Sign Up");

        System.out.print("Do => ");
        String choice;
        do {
            choice = scanner.nextLine();
        } while(!choice.equals("1") && !choice.equals("2"));
        if(choice.equals("1")){
            return this.crudClient(Command.LOGIN);
        }
        else{
            return this.crudClient(Command.SIGNUP);
        }
    }

    public boolean doAction() throws IOException {
        int index = 3;
        Command[] commands = Command.values();
        for(Command command: commands){
            if(!(command == Command.LOGIN) && !(command == Command.SIGNUP)) {
                System.out.println(index + " - " + command.getCommandInformation());
                index++;
            }
        }
        System.out.print("Do => ");
        index = Integer.parseInt(scanner.nextLine());
        switch (commands[index - 1]){
            case GETADS:{
                this.getAds(Command.GETADS);
                break;
            }

            case GETAD:{
                this.getAd();
                break;
            }

            case ADDAD:{
                this.crudAd(Command.ADDAD);
                break;
            }

            case UPDATEAD:{
                this.crudAd(Command.UPDATEAD);
                break;
            }

            case DELETEAD:{
                this.crudAd(Command.DELETEAD);
                break;
            }

            case GETRESERVEDADS:{
                this.getAds(Command.GETRESERVEDADS);
                break;
            }

            case RESERVEAD:{
                this.crudAd(Command.RESERVEAD);
                break;
            }

            case UNRESERVEAD:{
                this.crudAd(Command.UNRESERVEAD);
                break;
            }

            case GETCLIENTINFO:{
                this.getClientInfo();
                break;
            }

            case UPDATECLIENT:{
                this.crudClient(Command.UPDATECLIENT);
                break;
            }

            case DELETECLIENT:{
                if(this.crudClient(Command.DELETECLIENT)){
                    this.writer.close();
                    this.reader.close();
                    this.scanner.close();
                    return false;
                }
                break;
            }

            case LOGOUT:{
                this.writer.close();
                this.reader.close();
                this.scanner.close();
                System.out.println("Logged Out");
                System.out.println("Sad to see you go :(");
                return false;
            }

            default:{
                System.out.println("Your choice doesn't exist");
                break;
            }
        }
        return true;
    }

    private boolean crudClient(Command command){
        this.request = new Request();
        this.request.setCommand(command);
        Client client = new Client();
        Response response = new Response();

        if(command == Command.LOGIN){
            System.out.print("Please enter your email: ");
            client.setEmail(scanner.nextLine());
            System.out.print("Please enter you password: ");
            client.setPassword(scanner.nextLine());
        }
        else if(command == Command.SIGNUP || command == Command.UPDATECLIENT){
            if(command == Command.UPDATECLIENT){
                System.out.println("If you don't want to change any field just right next");
            }
            System.out.print("Please enter your first name: ");
            client.setFirstName(scanner.nextLine());
            System.out.print("Please enter your last name: ");
            client.setLastName(scanner.nextLine());
            System.out.print("Please enter your birthday (yyyy-MM-dd): ");
            client.setBirthdate(Date.valueOf(scanner.nextLine()));
            System.out.print("Please enter your email: ");
            client.setEmail(scanner.nextLine());
            if(command == Command.UPDATECLIENT){
                System.out.print("Please enter the old password you can't use next here: ");
                client.setPassword(scanner.nextLine());
                System.out.print("Please enter the new password if you want: ");
                request.setNewPassword(scanner.nextLine());
            }
            else{
                System.out.print("Please enter your password: ");
                client.setPassword(scanner.nextLine());
            }
            System.out.print("Please enter your address: ");
            client.setAddress(scanner.nextLine());
            System.out.print("Please enter your postal code: ");
            client.setPostalCode(Integer.parseInt(scanner.nextLine()));
            System.out.print("Please enter your city: ");
            client.setCity(scanner.nextLine());
            System.out.print("Please enter your phone number (+xxx xxx xxx xxxx): ");
            client.setPhoneNumber(scanner.nextLine());
        }
        else if(command == Command.DELETECLIENT){
            System.out.print("Please enter your password to delete your account: ");
            client.setPassword(scanner.nextLine());
        }

        this.request.setClient(client);
        try {
            this.writer.writeObject(this.request);
            response = (Response) this.reader.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(response.getMessage());
        return response.isSucceeded();
    }

    private void crudAd(Command command){
        this.request = new Request();
        this.request.setCommand(command);
        Response response;
        Ad ad = new Ad();

        if(command == Command.ADDAD || command == Command.UPDATEAD){
            if(command == Command.UPDATEAD){
                System.out.print("Please enter the id of the Ad: ");
                ad.setId(Integer.parseInt(scanner.nextLine()));
            }
            Scanner scanner = new Scanner(System.in);
            System.out.print("Please the title of the Ad: ");
            ad.setTitle(scanner.nextLine());
            System.out.print("Please enter the description: ");
            ad.setDescription(scanner.nextLine());
            System.out.print("Please enter the price: ");
            ad.setPrice(Float.parseFloat(scanner.nextLine()));
            System.out.print("Please enter the id of the category: ");
            ad.setCategory(new Category(Integer.parseInt(scanner.nextLine()), null));
        }
        else if(command == Command.DELETEAD || command == Command.RESERVEAD || command == Command.UNRESERVEAD){
            System.out.print("Please enter the id of the Ad: ");
            ad.setId(Integer.parseInt(scanner.nextLine()));
        }

        this.request.setAd(ad);
        try {
            this.writer.writeObject(this.request);
            response = (Response) this.reader.readObject();
            System.out.println(response.getMessage());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getAds(Command command){
        this.request = new Request();
        this.request.setCommand(command);
        Response response;
        ArrayList<Ad> ads = new ArrayList<>();

        if(command == Command.GETADS){
            System.out.print("Do you want to see your ads (yes|no): ");
            String choice = scanner.nextLine();
            this.request.setAreMine(choice.equals("yes"));

        }

        try {
            this.writer.writeObject(request);
            response = (Response) this.reader.readObject();
            if(response.getAds() == null)
                System.out.println(response.getMessage());
            else
                ads = response.getAds();
                for(Ad ad: ads){
                    System.out.println("Id: " + ad.getId());
                    System.out.println("Title: " + ad.getTitle());
                    System.out.println("Description: " + ad.getDescription());
                    System.out.println("Price: " + ad.getPrice());
                    System.out.println();
                }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getAd(){
        this.request = new Request();
        this.request.setCommand(Command.GETAD);
        Response response = new Response();
        Ad ad = new Ad();

        System.out.print("Please enter the id of the Ad: ");
        ad.setId(Integer.parseInt(scanner.nextLine()));

        this.request.setAd(ad);
        try {
            this.writer.writeObject(this.request);
            response = (Response) this.reader.readObject();
            ad = response.getAd();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if(ad != null){
            System.out.println("Ad Id: " + ad.getId());
            System.out.println("Title: " + ad.getTitle());
            System.out.println("Description: " + ad.getDescription());
            System.out.println("Price: " + ad.getPrice());
            System.out.println("Category: " + ad.getCategory().getName());
            System.out.println("Posted by: " + ad.getPostedBy().getId() + ", " + ad.getPostedBy().getFirstName() + " " + ad.getPostedBy().getLastName());
            System.out.println("Posted at: " + ad.getPostingDate());
            System.out.println("Reserved: " + ad.isReserved());
        }
        else{
            System.out.println(response.getMessage());
        }
    }

    private void getClientInfo(){
        this.request = new Request();
        this.request.setCommand(Command.GETCLIENTINFO);
        Response response;
        Client client = new Client();

        try {
            this.writer.writeObject(this.request);
            response = (Response) this.reader.readObject();
            client = response.getClient();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("First Name: " + client.getFirstName());
        System.out.println("Last Name: " + client.getLastName());
        System.out.println("Birthday: " + client.getBirthdate().toString());
        System.out.println("Email: " + client.getEmail());
        System.out.println("Address: " + client.getAddress());
        System.out.println("Postal Code: " + client.getPostalCode());
        System.out.println("City: " + client.getCity());
        System.out.println("Phone Number: " + client.getPhoneNumber());
    }
}
