import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    // Clear the console screen
    public static void clearScreen() {
        System.out.println();
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void main(String[] args){
        Socket serverConnectionSocket;
        File serverConfigFile;
        Scanner reader;

        // Open configuration file
        serverConfigFile = new File("../config/serverConfig.txt");
        if(!serverConfigFile.exists()){
            System.out.println("Configuration file does not exist");
        }
        else{
            while(true){
                try {
                    // Read configuration file
                    reader = new Scanner(serverConfigFile);
                    String[] config = reader.nextLine().split(":");
                    reader.close();

                    // Connect to server
                    serverConnectionSocket = new Socket(config[1], Integer.parseInt(config[2]));
                    System.out.println("Connected to server");
                    break;
                } catch (IOException  ignore) {}
            }
            MyJFrame jFrame = new MyJFrame();
            jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            jFrame.pack();
            jFrame.setVisible(true);
            // Create a daemon to connected to the server
            ServerDaemon serverDaemon = new ServerDaemon(serverConnectionSocket,jFrame);
            serverDaemon.start();
        }
    }
}