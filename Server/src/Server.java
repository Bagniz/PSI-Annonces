import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args) {
        ArrayList<ClientHandler> clients = new ArrayList<>();
        try {
            System.out.println("Starting the server");
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("Server started at address: " + serverSocket.getInetAddress());

            Database database = new Database();
            if(database.connectToDB())
                System.out.println(database.getDbVersion());
            else
                System.out.println("Error while connecting to database");

            while (true)
            {
                Socket clientConnection = serverSocket.accept();
                clients.add(new ClientHandler(clientConnection, database));
                clients.get(clients.size() - 1).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
