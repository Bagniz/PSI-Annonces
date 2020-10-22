import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Ecoute extends Thread{
    String adresseServeur;
    int portServeur;
    public Ecoute(String adresseServeur, int portServeur)
    {
        this.adresseServeur = adresseServeur;
        this.portServeur = portServeur;
    }

    @Override
    public void run() {
        PrintWriter out = null;
        BufferedReader reader = null;
        int x =0;
        try {
            Socket theSocket = new Socket(adresseServeur, portServeur);
            System.out.println("Client: Connecté au serveur d'echo "+ theSocket);
            reader = new BufferedReader( new InputStreamReader(theSocket.getInputStream()));
            out = new PrintWriter(theSocket.getOutputStream());
            while (true) {
                String theLine = reader.readLine();
                if(theLine == null)
                    break;
                System.out.println(theLine);
            }
            Scanner scanner = new Scanner(System.in);
            String scan = scanner.next();
            out.write(scan + "\n");
            out.flush();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException ignored) {}
        }
    }

}
