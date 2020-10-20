import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Ecoute extends Thread{
    String adresseServeur = "";
    int portServeur = 0;
    public Ecoute(String adresseServeur, int portServeur)
    {
        this.adresseServeur = adresseServeur;
        this.portServeur = portServeur;
    }

    @Override
    public void run() {
        PrintWriter out = null;
        BufferedReader networkIn = null;
        int x =0;
        try {
            Socket theSocket = new Socket(adresseServeur, portServeur);
            System.out.println("Client: Connecté au serveur d'echo "+ theSocket);
            networkIn = new BufferedReader( new InputStreamReader(theSocket.getInputStream()));
            out = new PrintWriter(theSocket.getOutputStream());
            while (true) {
                String theLine = networkIn.readLine();
                if(Integer.parseInt(theLine) != x) {
                    System.out.println(theLine);
                    x = Integer.parseInt(theLine);
                }
            }
        }
        catch (IOException ex) {
            System.err.println(ex);

        } finally {
            try {
                if (networkIn != null) networkIn.close();
                if (out != null) out.close();
            } catch (IOException ex) {}
        }
    }

}
