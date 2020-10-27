import java.io.*;
import java.net.Socket;
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

    public boolean authenticate(){
        // Variables
        Scanner reader;
        String choice;

        this.readMessages();

        // Get the users input
        reader = new Scanner(System.in);
        System.out.print("Do: ");
        do {
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

    private int logIn(){
        String logInRequest = "LOGIN";
        int response = 0;

        Client.clearScreen();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please enter your email: ");
        logInRequest += "|" + scanner.nextLine();
        System.out.print("Please enter your password: ");
        logInRequest += "|" + scanner.nextLine();
        System.out.println(logInRequest);

        this.writer.write(logInRequest + "\n");
        writer.flush();

        try {
            response = Integer.parseInt(reader.readLine());
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return response;
    }

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

    public boolean chooseAction(ServerDaemon serverDaemon) {
        Requests[] requestsTab = Requests.values();
        int i =1;
        Client.clearScreen();
        for(Requests requests : requestsTab)
        {
            System.out.println(i + " - " + requests.getInformation());
            i++;
        }
        Scanner scanner = new Scanner(System.in);
        i = Integer.parseInt(scanner.nextLine());
        switch (requestsTab[i-1])
        {
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

            case LOGOUT:{
                System.out.println("Logged Out");
                return false;
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
            String test = reader.readLine();
            System.out.println(test);
            String[] response = test.split("\\|");

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
        System.out.print("Please enter the id of the categorie:");
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
        System.out.print("Please enter the id of the categorie:");
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
        writer.flush();

        try {
            if(reader.readLine().equals("success"))
                return true;
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return false;
    }
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

    public void readMessages(){
        // Variables
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
