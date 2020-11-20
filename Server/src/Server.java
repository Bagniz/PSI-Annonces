import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args) {
        ArrayList<ClientHandler> clients = new ArrayList<>();
        try {
            // Connecting to database
            Database database = new Database();
            if(database.connectToDB()) {
                System.out.println("Connected to database");
                System.out.println("Database version: " + database.getDbVersion());
            }
            else
                System.out.println("Could not connect to database");

            // Create a connection cleaner
            ConnectionsCleaner connectionsCleaner = new ConnectionsCleaner(clients);
            connectionsCleaner.start();

            // Starting server
            System.out.println("Starting the server");
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("Server started at address: " + serverSocket.getLocalSocketAddress());

            while (true) {
                // Wait for new clients
                Socket clientConnection = serverSocket.accept();
                clients.add(new ClientHandler(clientConnection, database, clients));
                clients.get(clients.size() - 1).start();
            }
        } catch (IOException e) {
            System.out.println("Could not start the server");
        }
    }
}
