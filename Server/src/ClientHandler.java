import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientHandler extends Thread{
    Socket clientConnection;
    Database database;
    OutputStreamWriter writer;
    BufferedReader reader;

    public ClientHandler(Socket clientConnection, Database database)
    {
        this.clientConnection = clientConnection;
        this.database = database;
        try {
            this.writer = new OutputStreamWriter((clientConnection.getOutputStream()));
            this.reader = new BufferedReader(new InputStreamReader(clientConnection.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        try {
            this.writer.write("Hello there, What do you want to do ?\n");
            this.writer.write("1. Log in\n");
            this.writer.write("2. Sign up\n");
            this.writer.write("\r\n");
            this.writer.flush();
            this.listen();
        } catch (IOException e) {
            System.out.println("Client disconnected");
        }
    }

    private void listen(){
        try {
            String line = this.reader.readLine();
            switch (line){
                case "SIGNUP":{
                    String firstName = this.reader.readLine();
                    String lastName = this.reader.readLine();
                    String birthday = this.reader.readLine();
                    String email = this.reader.readLine();
                    String password = this.reader.readLine();
                    String address = this.reader.readLine();
                    int postalCode = Integer.parseInt(this.reader.readLine());
                    String city = this.reader.readLine();
                    String phoneNumber = this.reader.readLine();
                    int id = database.signUp(firstName, lastName, birthday, email, password, address, postalCode, city, phoneNumber);
                    this.writer.write(id + "\n");
                    this.writer.write("\r\n");
                    break;
                }
                case "LOGIN":{
                    String email = this.reader.readLine();
                    String password = this.reader.readLine();
                    int id = database.logIn(email, password);
                    this.writer.write(id + "\n");
                    this.writer.write("\r\n");
                    break;
                }
                default:{
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
