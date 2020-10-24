import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void main(String[] args){
        // Variables
        Socket serverConnectionSocket = null;
        File serverConfigFile;
        Scanner reader;

        // Open configuration file
        serverConfigFile = new File("../config/serverConfig.txt");
        try {
            // Read configuration file
            reader = new Scanner(serverConfigFile);
            String[] config = reader.nextLine().split(":");
            reader.close();

            // Connect to server
            serverConnectionSocket = new Socket(config[1],Integer.parseInt(config[2]));
            System.out.println("Connected to server");
        } catch (IOException exception) {
            System.out.println("Could not connect to server");
        }

        // Variables
        assert serverConnectionSocket != null;
        ServerDaemon serverDaemon = new ServerDaemon(serverConnectionSocket);

        // Authenticate to the server
        if(serverDaemon.authenticate()) {
            System.out.println("Hello There");
        }
        else {
            System.out.println("Bye Bye");
        }
    }
}