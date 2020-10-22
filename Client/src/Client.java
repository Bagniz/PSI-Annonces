import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args){
        File serverConfigFile = new File("serverConfig.txt");
        Socket serverConnectionSocket = null;
        Scanner reader = null;
        try {
            reader = new Scanner(serverConfigFile);
            String[] config = reader.nextLine().split(":");
            reader.close();
            serverConnectionSocket = new Socket(config[1],Integer.parseInt(config[2]));
            System.out.println("Connected to server");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        Responses responses = new Responses(serverConnectionSocket);
        Requests requests = new Requests(serverConnectionSocket);
        responses.listen();
        reader = new Scanner(System.in);
        while(true){
            System.out.print("Choose one: ");
            String choice = reader.nextLine();
            if(choice.equals("1")){
                requests.logIn();
                break;
            }else if(choice.equals("2")){
                requests.signUp();
                break;
            }
        }
        responses.listen();
    }
}