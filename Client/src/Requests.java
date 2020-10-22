import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Scanner;
import java.io.Console;

public class Requests {
    private Socket serverConnectionSocket;
    private PrintWriter writer;

    public Requests(Socket serverConnectionSocket){
        this.serverConnectionSocket = serverConnectionSocket;
        try {
            this.writer = new PrintWriter(new OutputStreamWriter(this.serverConnectionSocket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getServerConnectionSocket() {
        return serverConnectionSocket;
    }

    public void setServerConnectionSocket(Socket serverConnectionSocket) {
        this.serverConnectionSocket = serverConnectionSocket;
    }

    public void logIn(){
        String email;
        String password;

        Scanner scanner = new Scanner(System.in);
        Console console = System.console();

        System.out.print("Please enter your email: ");
        email = scanner.nextLine();
        password = Arrays.toString(console.readPassword("%s", "Please enter your password: "));

        this.writer.write("LOGIN\n");
        this.writer.write(email + "\n");
        this.writer.write(password + "\n");
        this.writer.write("\r\n");
        writer.flush();
    }

    public void signUp(){
        String firstName;
        String lastName;
        String email;
        String password;
        String birthday;
        String address;
        String city;
        String phoneNumber;
        int postalCode;

        Scanner scanner = new Scanner(System.in);
        Console console = System.console();
        System.out.print("Please enter your first name: ");
        firstName = scanner.nextLine();
        System.out.print("Please enter your last name: ");
        lastName = scanner.nextLine();
        System.out.print("Please enter your birthday (yyyy-MM-dd): ");
        birthday = scanner.nextLine();
        System.out.print("Please enter your email: ");
        email = scanner.nextLine();
        password = Arrays.toString(console.readPassword("%s", "Please enter your password: "));
        System.out.print("Please enter your address: ");
        address = scanner.nextLine();
        System.out.print("Please enter your postal code: ");
        postalCode = scanner.nextInt();
        System.out.print("Please enter your city: ");
        city = scanner.nextLine();
        System.out.print("Please enter your phone number (+xxx xxx xxx xxxx): ");
        phoneNumber = scanner.nextLine();

        writer.write("SIGNUP\n");
        writer.write(firstName + "\n");
        writer.write(lastName + "\n");
        writer.write(birthday + "\n");
        writer.write(email + "\n");
        writer.write(password + "\n");
        writer.write(address + "\n");
        writer.write(postalCode + "\n");
        writer.write(city + "\n");
        writer.write(phoneNumber + "\n");
        this.writer.write("\r\n");
        writer.flush();
    }
}
