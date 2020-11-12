import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args){
        ArrayList<ClientHandler> clients = new ArrayList<>();

        // Connect to database
        Database database = new Database();
        if(database.connectToDb())
            System.out.println(database.getDbVersion());
        else
            System.out.println("Error while connecting to database");

        // Create a connection cleaner
        ConnectionsCleaner connectionsCleaner = new ConnectionsCleaner(clients);
        connectionsCleaner.start();

        try {
            System.out.println("Starting the server");
            ServerSocket serverSocket = serverSocket = new ServerSocket(8080);
            System.out.println("Server started at address: " + serverSocket.getInetAddress());

            // Waiting for new clients
            while (true){
                Socket client = serverSocket.accept();
                clients.add(new ClientHandler(client, database));
                clients.get(clients.size() - 1).start();
            }
        } catch (IOException e) {
            System.out.println("Connection problem");
        }
    }
}
