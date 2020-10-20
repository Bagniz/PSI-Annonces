import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {

    static class Connect extends Thread{

        Socket s;
        AtomicInteger nbrClient;
        public Connect(Socket s,AtomicInteger nbrClient)
        {
            this.s = s;
            this.nbrClient = nbrClient;
        }

        public void run(){
            OutputStreamWriter out = null;
            try {

                out = new OutputStreamWriter((s.getOutputStream()));
                while (true) {

                    out.write( nbrClient.get() +"\n");
                    out.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
                nbrClient.addAndGet(-1);
            }
            try {
                Thread.sleep(10);
            } catch (Exception e) {
            }
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println("Server Started");
            TimeUnit.SECONDS.sleep(10);
            ServerSocket server = new ServerSocket(8080);
            System.out.println("Serveur marche "+server.getInetAddress());
            AtomicInteger nbrClient =new AtomicInteger(0);
            Class.forName("org.postgresql.Driver");
            File myObj = new File("configDB.txt");
            Scanner myReader = null;
            try {
                myReader = new Scanner(myObj);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String data = myReader.nextLine();
            String[] config = data.split(":");
            myReader.close();
            String url = "jdbc:postgresql://"+config[1]+":"+config[2]+"/"+config[3];
            System.out.println(url);
            Properties props = new Properties();
            props.setProperty("user","postgres");
            props.setProperty("password","postgres");
            props.setProperty("ssl","false");
            Connection conn = DriverManager.getConnection(url, props);
            if (conn != null)
            {
                System.out.println("connecté a la bases");
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT VERSION()") ;

                if (rs.next()) {
                    System.out.println(rs.getString(1));
                }
            }

            Connect t;
            while (true)
            {
                Socket connection = server.accept();
                nbrClient.addAndGet(1);
                t = new Connect(connection,nbrClient);
                t.start();
            }
        } catch (IOException | SQLException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
