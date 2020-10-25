import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Client {

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void chooseAction(ServerDaemon serverDaemon)
    {
        Requests[] requestsTab = Requests.values();
        int i =1;
        for(Requests requests : requestsTab)
        {
            System.out.println(i+"- "+requests.getInformation());
            i++;
        }
        Scanner scanner = new Scanner(System.in);
        i = Integer.parseInt(scanner.nextLine());
        switch (requestsTab[i-1])
        {
            case ADDAD: {
                if (serverDaemon.addAd() <= 0) {
                    System.out.println("the ad could not be published");
                    break;
                }
                System.out.println("the ad is published");
                break;
            }
            default:{
                System.out.println("Your choice doesn't exist");
                break;
            }
        }
    }

    public static void main(String[] args){

        // Variables
        Socket serverConnectionSocket = null;
        File serverConfigFile;
        Scanner reader;

        // Open configuration file
        serverConfigFile = new File("serverConfig.txt");
        try {

            // Read configuration file
            reader = new Scanner(serverConfigFile);
            String[] config = reader.nextLine().split(":");
            reader.close();

            // Connect to server
            serverConnectionSocket = new Socket(config[1],Integer.parseInt(config[2]));
            System.out.println("Connected to server");
        } catch (IOException  exception) {
            System.out.println("Could not connect to server");
        }

        // Variables
        assert serverConnectionSocket != null;
        ServerDaemon serverDaemon = new ServerDaemon(serverConnectionSocket);

        // Authenticate to the server
        if(serverDaemon.authenticate()) {
            System.out.println("Hello there, here som command try them");
            chooseAction(serverDaemon);
        }
        else {
            System.out.println("Bye Bye");
        }
    }
}