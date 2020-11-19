import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class ServerDaemon extends Thread{

    // Attributes
    private Scanner scanner;
    private BufferedReader reader;
    private PrintWriter writer;
    private int clientId;
    private MyJFrame frame;

    @Override
    public void run() {
        // Authenticate to the server
        if(this.authenticate()) {
            frame.printConsole("Hello there, what do you want to do (Number): ");
            while(true){
                if(!this.chooseAction())
                    break;
            }
        }
        else {
            frame.printConsole("Sad to see you go :(");
        }
    }

    // Constructor
    public ServerDaemon(Socket server,MyJFrame jFrame){
        try {
            this.reader = new BufferedReader(new InputStreamReader(server.getInputStream()));
            this.writer = new PrintWriter(new OutputStreamWriter(server.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.frame = jFrame;
        
    }

    // Authenticate a client
    public boolean authenticate(){
        // Variables
        Scanner reader;
        String choice;

        this.readMessages();

        // Get the users input
        do {
            frame.printConsole("Please enter a valid operation (1/2): ");
            choice = frame.readConsole();
        } while (!choice.equals("1") && !choice.equals("2"));

        // LogIn or SignUp
        if(choice.equals("1")){
            if((clientId = this.logIn()) <= 0) {
                frame.printConsole("LogIn failed");
                return false;
            }
        }
        else {
            if((clientId = this.signUp()) <= 0) {
                frame.printConsole("SignUp failed");
                return false;
            }
            else {
                frame.printConsole("SignUp succeeded");
            }
        }

        return true;
    }

    // Log in an existing client
    private int logIn(){
        String logInRequest = "LOGIN";
        int response = 0;

        Client.clearScreen();
        

        frame.printConsole("Please enter your email: ");
        logInRequest += "|" + frame.readConsole();
        frame.printConsole("Please enter your password: ");
        logInRequest += "|" + frame.readConsole();

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
        
        frame.printConsole("Please enter your first name: ");
        signUpRequest += "|" + frame.readConsole();
        frame.printConsole("Please enter your last name: ");
        signUpRequest += "|" + frame.readConsole();
        frame.printConsole("Please enter your birthday (yyyy-MM-dd): ");
        signUpRequest += "|" + frame.readConsole();
        frame.printConsole("Please enter your email: ");
        signUpRequest += "|" + frame.readConsole();
        frame.printConsole("Please enter your password: ");
        signUpRequest += "|" + frame.readConsole();
        frame.printConsole("Please enter your address: ");
        signUpRequest += "|" + frame.readConsole();
        frame.printConsole("Please enter your postal code: ");
        signUpRequest += "|" + frame.readConsole();
        frame.printConsole("Please enter your city: ");
        signUpRequest += "|" + frame.readConsole();
        frame.printConsole("Please enter your phone number (+xxx xxx xxx xxxx): ");
        signUpRequest += "|" + frame.readConsole();

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
            frame.printConsole(operationIndex + " - " + requests.getInformation());
            operationIndex++;
        }

        do{
            frame.printConsole("Please enter a valid operation: ");
            
            try{
                operationIndex = Integer.parseInt(frame.readConsole());
            } catch (NumberFormatException e){
                operationIndex = -1;
            }
        } while ((operationIndex <= 0) || (operationIndex > requestsTab.length));

        switch (requestsTab[operationIndex - 1])
        {
            case GETADS:{
                String ads = this.getAds();
                frame.printConsole(Objects.requireNonNullElse(ads, "Operation failed"));
                break;
            }

            case GETAD:{
                String ad = this.getAd();
                if(ad.equals("null"))
                    frame.printConsole("There is no ad that matches the given id");
                else
                    frame.printConsole(ad);
                break;
            }

            case ADDAD:{
                if (this.addAd() <= 0)
                    frame.printConsole("the ad could not be published");
                else
                    frame.printConsole("the ad is published");
                break;
            }

            case UPDATEAD:{
                if(this.updateAd())
                    frame.printConsole("Ad updated");
                else
                    frame.printConsole("Ad update failed");
                break;
            }

            case DELETEAD:{
                if(this.deleteAd())
                    frame.printConsole("Ad deleted");
                else
                    frame.printConsole("Ad deletion failed");
                break;
            }

            case GETRESERVEDADS:{
                String ads = this.getReservedAds();
                frame.printConsole(Objects.requireNonNullElse(ads, "Operation failed"));
                break;
            }

            case RESERVEAD:{
                if(this.resUnresAd(true))
                    frame.printConsole("The ad has been reserved");
                else
                    frame.printConsole("Reservation operation failed");
                break;
            }

            case UNRESERVEAD:{
                if(this.resUnresAd(false))
                    frame.printConsole("The ad has been unreserved");
                else
                    frame.printConsole("Unresevation operation failed");
                break;
            }

            case LOGOUT:{
                frame.printConsole("Logged Out");
                frame.printConsole("Sad to see you go :(");
                return false;
            }

            case GETCLIENTINFO:{
                String clientInfo = this.getClientInfo();
                if(clientInfo.equals("null")){
                    frame.printConsole("Operation failed");
                }
                else{
                    frame.printConsole(clientInfo);
                }
                break;
            }

            case UPDATECLIENT:{
                if(this.updateClient())
                    frame.printConsole("Client updated");
                else
                    frame.printConsole("Client update failed");
                break;
            }

            case DELETECLIENT:{
                if(this.deleteClient()){
                    frame.printConsole("Client account is deleted");
                    return false;
                }
                else
                    frame.printConsole("Client account deletion failed");
                break;
            }

            case CHAT:{
                if(!chat()){
                    frame.printConsole("Erreur while connection to the owner");
                }
                break;
            }

            default:{
                frame.printConsole("Your choice doesn't exist");
                break;
            }
        }
        return true;
    }

    // Chat
    private boolean chat(){
        String chatClientRequest = Requests.CHAT.getStringValue();
        Client.clearScreen();
        
        frame.printConsole("Please enter the id of the ad: ");
        chatClientRequest += "|" + frame.readConsole();
        this.writer.write(chatClientRequest + "\n");
        this.writer.flush();

        try {
            String[] response = reader.readLine().split("\\|");
            if(response.length > 1){
                Socket socket = new Socket(response[0], Integer.parseInt(response[1]));
                ChatReceiver chatReceiver = new ChatReceiver(socket);
                ChatSender chatSender;
                String getClient = Requests.GETCLIENTINFO.getStringValue() + "|" + clientId;
                this.writer.write(getClient + "\n");
                this.writer.flush();
                response = reader.readLine().split("\\|");
                if(response.length > 1){
                    chatSender = new ChatSender(socket, clientId, response[1]);
                    chatReceiver.start();
                    chatSender.start();
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return true;
    }

    // Get existing adds
    private String getAds() {
        String getAdsRequest = Requests.GETADS.getStringValue();
        Client.clearScreen();
        
        frame.printConsole("Do you want to see your ads (yes|no): ");
        if(frame.readConsole().equals("yes"))
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
                for(int i = 0; i < response.length; i += 4){
                    adsBuilder.append("Id: ").append(response[i]).append("\n");
                    adsBuilder.append("Title: ").append(response[i+1]).append("\n");
                    adsBuilder.append("Description: ").append(response[i+2]).append("\n");
                    adsBuilder.append("Price: ").append(response[i+3]).append("\n");
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
        
        frame.printConsole("Please enter the id of the Ad: ");
        getAdRequest += "|" + frame.readConsole();

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
    public int addAd() {
        int response = 0;
        String addAdRequest = Requests.ADDAD.getStringValue();
        Client.clearScreen();
        
        frame.printConsole("Please the title of the Ad:");
        addAdRequest += "|" + frame.readConsole();
        frame.printConsole("Please enter the description:");
        addAdRequest += "|" + frame.readConsole();
        frame.printConsole("Please enter the price:");
        addAdRequest += "|" + frame.readConsole();
        frame.printConsole("Please enter the id of the category:");
        addAdRequest += "|" + frame.readConsole();

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
        
        frame.printConsole("Please enter the id of the Ad");
        updateAdRequest += "|" + frame.readConsole();
        frame.printConsole("Please the title of the Ad:");
        updateAdRequest += "|" + frame.readConsole();
        frame.printConsole("Please enter the description:");
        updateAdRequest += "|" + frame.readConsole();
        frame.printConsole("Please enter the price:");
        updateAdRequest += "|" + frame.readConsole();
        frame.printConsole("Please enter the id of the category:");
        updateAdRequest += "|" + frame.readConsole();

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
        
        frame.printConsole("Please enter the id of the Ad");
        deleteAdRequest += "|" + frame.readConsole();

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
                for(int i = 0; i < response.length; i += 4){
                    adsBuilder.append("Id: ").append(response[i]).append("\n");
                    adsBuilder.append("Title: ").append(response[i+1]).append("\n");
                    adsBuilder.append("Description: ").append(response[i+2]).append("\n");
                    adsBuilder.append("Price: ").append(response[i+3]).append("\n");
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
        
        frame.printConsole("Please enter the id of the Ad");
        resUnresAdRequest += "|" + frame.readConsole();

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
    public boolean updateClient() {
        String updateClient = Requests.UPDATECLIENT.getStringValue();
        
        frame.printConsole("If you don't want to change any field just right next");
        frame.printConsole("Please enter your new first name: ");
        updateClient += "|" + frame.readConsole();
        frame.printConsole("Please enter your last name: ");
        updateClient += "|" + frame.readConsole();
        frame.printConsole("Please enter your birthday (yyyy-MM-dd): ");
        updateClient += "|" + frame.readConsole();
        frame.printConsole("Please enter your email: ");
        updateClient += "|" + frame.readConsole();
        frame.printConsole("Please enter the old password you can't use next here: ");
        updateClient += "|" + frame.readConsole();
        frame.printConsole("Please enter the new password if you want: ");
        updateClient += "|" + frame.readConsole();
        frame.printConsole("Please enter your address: ");
        updateClient += "|" + frame.readConsole();
        frame.printConsole("Please enter your postal code: ");
        updateClient += "|" + frame.readConsole();
        frame.printConsole("Please enter your city: ");
        updateClient += "|" + frame.readConsole();
        frame.printConsole("Please enter your phone number (+xxx xxx xxx xxxx): ");
        updateClient += "|" + frame.readConsole();
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
        
        frame.printConsole("Please enter your password to delete your account");
        deleteClientRequest += "|" + frame.readConsole();
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
                frame.printConsole(message);
            }
        } catch (IOException exception){
            exception.printStackTrace();
        }
    }
}
