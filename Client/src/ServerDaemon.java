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

    public int addAd()
    {
        int response = 0;
        String addAdRequest = Requests.ADDAD.getStringValue();
        Client.clearScreen();
        Scanner scanner = new Scanner(System.in);
        Console console = System.console();
        System.out.print("Please the title of the Ad:");
        addAdRequest += "|" + scanner.nextLine();
        System.out.print("Please enter the description:");
        addAdRequest += "|" + scanner.nextLine();
        System.out.print("Please enter the price:");
        addAdRequest += "|" + scanner.nextLine();
        System.out.print("Please enter the id of the categorie:");
        addAdRequest += "|" + scanner.nextLine();
        addAdRequest += "|" + clientId;

        this.writer.write(addAdRequest + "\n");
        writer.flush();

        try {
            response = Integer.parseInt(reader.readLine());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return response;
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
