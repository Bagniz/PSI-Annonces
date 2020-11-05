import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class ServerDaemon {

    // Attributes
    private BufferedReader reader;
    private PrintWriter writer;
    private int clientId;

    // Constructor
    public ServerDaemon(Socket server){
        try {
            this.reader = new BufferedReader(new InputStreamReader(server.getInputStream()));
            this.writer = new PrintWriter(new OutputStreamWriter(server.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Authenticate a client
    public boolean authenticate(){
        // Variables
        Scanner reader;
        String choice;

        this.readMessages();

        // Get the users input
        reader = new Scanner(System.in);
        do {
            System.out.print("Please enter a valid operation (1/2): ");
            choice = reader.nextLine();
        } while (!choice.equals("1") && !choice.equals("2"));

        // LogIn or SignUp
        if(choice.equals("1")){
            if((clientId = this.logIn()) <= 0) {
                System.out.println("LogIn failed");
                return false;
            }
        }
        else {
            if((clientId = this.signUp()) <= 0) {
                System.out.println("SignUp failed");
                return false;
            }
            else {
                System.out.println("SignUp succeeded");
            }
        }

        return true;
    }

    // Log in an existing client
    private int logIn(){
        String logInRequest = "LOGIN";
        int response = 0;

        Client.clearScreen();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please enter your email: ");
        logInRequest += "|" + scanner.nextLine();
        System.out.print("Please enter your password: ");
        logInRequest += "|" + scanner.nextLine();

        this.writer.write(logInRequest + "\n");
        writer.flush();

        try {
            response = Integer.parseInt(reader.readLine());
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return response;
    }

    // Sign up a new client
    private int signUp(){
        String signUpRequest = "SIGNUP";
        int response = 0;

        Client.clearScreen();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter your first name: ");
        signUpRequest += "|" + scanner.nextLine();
        System.out.print("Please enter your last name: ");
        signUpRequest += "|" + scanner.nextLine();
        System.out.print("Please enter your birthday (yyyy-MM-dd): ");
        signUpRequest += "|" + scanner.nextLine();
        System.out.print("Please enter your email: ");
        signUpRequest += "|" + scanner.nextLine();
        System.out.print("Please enter your password: ");
        signUpRequest += "|" + scanner.nextLine();
        System.out.print("Please enter your address: ");
        signUpRequest += "|" + scanner.nextLine();
        System.out.print("Please enter your postal code: ");
        signUpRequest += "|" + scanner.nextLine();
        System.out.print("Please enter your city: ");
        signUpRequest += "|" + scanner.nextLine();
        System.out.print("Please enter your phone number (+xxx xxx xxx xxxx): ");
        signUpRequest += "|" + scanner.nextLine();

        this.writer.write(signUpRequest + "\n");
        writer.flush();

        try {
            response = Integer.parseInt(reader.readLine());
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return response;
    }

    // Choose the operation to execute based on client choice
    public boolean chooseAction() {
        Requests[] requestsTab = Requests.values();
        int operationIndex = 1;
        Client.clearScreen();
        for(Requests requests : requestsTab) {
            System.out.println(operationIndex + " - " + requests.getInformation());
            operationIndex++;
        }

        do{
            System.out.print("Please enter a valid operation: ");
            Scanner scanner = new Scanner(System.in);
            try{
                operationIndex = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e){
                operationIndex = -1;
            }
        } while ((operationIndex <= 0) || (operationIndex > requestsTab.length));

        switch (requestsTab[operationIndex - 1])
        {
            case GETADS:{
                String ads = this.getAds();
                System.out.println(Objects.requireNonNullElse(ads, "Operation failed"));
                break;
            }

            case GETAD:{
                String ad = this.getAd();
                if(ad.equals("null"))
                    System.out.println("There is no ad that matches the given id");
                else
                    System.out.println(ad);
                break;
            }

            case ADDAD:{
                if (this.addAd() <= 0)
                    System.out.println("the ad could not be published");
                else
                    System.out.println("the ad is published");
                break;
            }

            case UPDATEAD:{
                if(this.updateAd())
                    System.out.println("Ad updated");
                else
                    System.out.println("Ad update failed");
                break;
            }

            case DELETEAD:{
                if(this.deleteAd())
                    System.out.println("Ad deleted");
                else
                    System.out.println("Ad deletion failed");
                break;
            }

            case GETRESERVEDADS:{
                String ads = this.getReservedAds();
                System.out.println(Objects.requireNonNullElse(ads, "Operation failed"));
                break;
            }

            case RESERVEAD:{
                if(this.resUnresAd(true))
                    System.out.println("The ad has been reserved");
                else
                    System.out.println("Reservation operation failed");
                break;
            }

            case UNRESERVEAD:{
                if(this.resUnresAd(false))
                    System.out.println("The ad has been unreserved");
                else
                    System.out.println("Unresevation operation failed");
                break;
            }

            case LOGOUT:{
                System.out.println("Logged Out");
                System.out.println("Sad to see you go :(");
                return false;
            }

            case GETCLIENTINFO:{
                String clientInfo = this.getClientInfo();
                if(clientInfo.equals("null")){
                    System.out.println("Operation failed");
                }
                else{
                    System.out.println(clientInfo);
                }
                break;
            }

            case UPDATECLIENT:{
                if(this.updateClient())
                    System.out.println("Client updated");
                else
                    System.out.println("Client update failed");
                break;
            }

            case DELETECLIENT:{
                if(this.deleteClient()){
                    System.out.println("Client account is deleted");
                    return false;
                }
                else
                    System.out.println("Client account deletion failed");
                break;
            }

            default:{
                System.out.println("Your choice doesn't exist");
                break;
            }
        }
        return true;
    }

    // Get existing adds
    private String getAds() {
        String getAdsRequest = Requests.GETADS.getStringValue();
        Client.clearScreen();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Do you want to see your ads (yes|no): ");
        if(scanner.nextLine().equals("yes"))
            getAdsRequest += "|" + true;
        else
            getAdsRequest += "|" + false;

        this.writer.write(getAdsRequest + "\n");
        this.writer.flush();

        String ads = null;
        try {
            String[] response = reader.readLine().split("\\|");
            StringBuilder adsBuilder = new StringBuilder();
            if(response.length > 1){
                for(int i = 0; i < response.length; i += 3){
                    adsBuilder.append("Id: ").append(response[i]).append("\n");
                    adsBuilder.append("Title: ").append(response[i+1]).append("\n");
                    adsBuilder.append("Description: ").append(response[i+2]).append("\n");
                }
                ads = adsBuilder.toString();
            }
            else{
                ads = response[0];
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return ads;
    }

    // Get an existing ad
    public String getAd(){
        String getAdRequest = Requests.GETAD.getStringValue();
        Client.clearScreen();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter the id of the Ad: ");
        getAdRequest += "|" + scanner.nextLine();

        this.writer.write(getAdRequest + "\n");
        writer.flush();

        String ad = "null";
        try {
            String[] response = reader.readLine().split("\\|");

            if(response.length > 1){
                ad = "Id: " + response[0] + "\n";
                ad += "Title: " + response[1] + "\n";
                ad += "Description: " + response[2] + "\n";
                ad += "Price: " + response[3] + "\n";
                ad += "Category: " + response[4] + "\n";
                ad += "Posted by: " + response[5] + "\n";
                ad += "Posted in: " + response[6] + "\n";
                ad += "Reserved: " + response[7] + "\n";
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return ad;
    }

    // Add a new ad
    public int addAd()
    {
        int response = 0;
        String addAdRequest = Requests.ADDAD.getStringValue();
        Client.clearScreen();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please the title of the Ad:");
        addAdRequest += "|" + scanner.nextLine();
        System.out.print("Please enter the description:");
        addAdRequest += "|" + scanner.nextLine();
        System.out.print("Please enter the price:");
        addAdRequest += "|" + scanner.nextLine();
        System.out.print("Please enter the id of the category:");
        addAdRequest += "|" + scanner.nextLine();

        this.writer.write(addAdRequest + "\n");
        writer.flush();

        try {
            response = Integer.parseInt(reader.readLine());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return response;
    }

    // Update an existing update
    public boolean updateAd(){
        String updateAdRequest = Requests.UPDATEAD.getStringValue();
        Client.clearScreen();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the id of the Ad");
        updateAdRequest += "|" + scanner.nextLine();
        System.out.print("Please the title of the Ad:");
        updateAdRequest += "|" + scanner.nextLine();
        System.out.print("Please enter the description:");
        updateAdRequest += "|" + scanner.nextLine();
        System.out.print("Please enter the price:");
        updateAdRequest += "|" + scanner.nextLine();
        System.out.print("Please enter the id of the category:");
        updateAdRequest += "|" + scanner.nextLine();

        this.writer.write(updateAdRequest + "\n");
        writer.flush();

        try {
            if(reader.readLine().equals("success"))
                return true;
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    // Delete an existing add
    public boolean deleteAd(){
        String deleteAdRequest = Requests.DELETEAD.getStringValue();
        Client.clearScreen();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the id of the Ad");
        deleteAdRequest += "|" + scanner.nextLine();

        this.writer.write(deleteAdRequest + "\n");
        writer.flush();

        try {
            if(reader.readLine().equals("success"))
                return true;
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    // Get all reserved adds
    public String getReservedAds(){
        String getReservedAdsRequest = Requests.GETRESERVEDADS.getStringValue();
        Client.clearScreen();
        this.writer.write(getReservedAdsRequest + "\n");
        this.writer.flush();

        String ads = null;
        try {
            String[] response = reader.readLine().split("\\|");
            StringBuilder adsBuilder = new StringBuilder();
            if(response.length > 1){
                for(int i = 0; i < response.length; i += 3){
                    adsBuilder.append("Id: ").append(response[i]).append("\n");
                    adsBuilder.append("Title: ").append(response[i+1]).append("\n");
                    adsBuilder.append("Description: ").append(response[i+2]).append("\n");
                }
                ads = adsBuilder.toString();
            }
            else{
                ads = response[0];
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return ads;
    }

    // Reserve or un reserve an ad
    public boolean resUnresAd(boolean reserve){
        String resUnresAdRequest;
        if(reserve)
            resUnresAdRequest = Requests.RESERVEAD.getStringValue();
        else
            resUnresAdRequest = Requests.UNRESERVEAD.getStringValue();
        Client.clearScreen();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the id of the Ad");
        resUnresAdRequest += "|" + scanner.nextLine();

        this.writer.write(resUnresAdRequest + "\n");
        writer.flush();

        try {
            if(reader.readLine().equals("success"))
                return true;
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    // Get an existing clients info
    public String getClientInfo(){
        String clientInfo = "null";
        String infoClientRequest = Requests.GETCLIENTINFO.getStringValue();
        this.writer.write(infoClientRequest + "\n");
        this.writer.flush();
        try {
            String[] response = this.reader.readLine().split("\\|");
            if(response.length > 1){
                clientInfo = "First Name: " + response[0] + "\n";
                clientInfo += "Last Name: " + response[1] + "\n";
                clientInfo += "Birthday: " + response[2] + "\n";
                clientInfo += "Email: " + response[3] + "\n";
                clientInfo += "Address: " + response[4] + "\n";
                clientInfo += "Postal Code: " + response[5] + "\n";
                clientInfo += "City: " + response[6] + "\n";
                clientInfo += "Phone Number: " + response[7] + "\n";
            }
            else if(response.length == 1){
                clientInfo = response[0];
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return clientInfo;
    }

    // Update an existing client
    public boolean updateClient()
    {
        String updateClient = Requests.UPDATECLIENT.getStringValue();
        Scanner scanner = new Scanner(System.in);
        System.out.println("If you don't want to change any field just right next");
        System.out.print("Please enter your new first name: ");
        updateClient += "|" + scanner.nextLine();
        System.out.print("Please enter your last name: ");
        updateClient += "|" + scanner.nextLine();
        System.out.print("Please enter your birthday (yyyy-MM-dd): ");
        updateClient += "|" + scanner.nextLine();
        System.out.print("Please enter your email: ");
        updateClient += "|" + scanner.nextLine();
        System.out.print("Please enter the old password you can't use next here: ");
        updateClient += "|" + scanner.nextLine();
        System.out.print("Please enter the new password if you want: ");
        updateClient += "|" + scanner.nextLine();
        System.out.print("Please enter your address: ");
        updateClient += "|" + scanner.nextLine();
        System.out.print("Please enter your postal code: ");
        updateClient += "|" + scanner.nextLine();
        System.out.print("Please enter your city: ");
        updateClient += "|" + scanner.nextLine();
        System.out.print("Please enter your phone number (+xxx xxx xxx xxxx): ");
        updateClient += "|" + scanner.nextLine();
        updateClient += "|" + clientId;

        this.writer.write(updateClient + "\n");
        this.writer.flush();

        try {
            if(reader.readLine().equals("success"))
                return true;
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    // Delete an existing client
    public boolean deleteClient(){
        String deleteClientRequest = Requests.DELETECLIENT.getStringValue();
        Client.clearScreen();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your password to delete your account");
        deleteClientRequest += "|" + scanner.nextLine();
        deleteClientRequest += "|" + clientId;

        this.writer.write(deleteClientRequest + "\n");
        writer.flush();

        try {
            if(reader.readLine().equals("success"))
                return true;
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    // Read messages from the server
    public void readMessages(){

        String message;

        // Print messages
        try{
            while((message = reader.readLine()) != null){
                if(message.length() == 0)
                    break;
                System.out.println(message);
            }
        } catch (IOException exception){
            exception.printStackTrace();
        }
    }
}
