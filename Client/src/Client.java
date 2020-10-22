import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Client {

    public static void main(String[] args) throws InterruptedException {
        TimeUnit.SECONDS.sleep(20);
        File myObj = new File("configClient.txt");
        Scanner myReader = null;
        try {
            myReader = new Scanner(myObj);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String data = myReader.nextLine();
        String[] config = data.split(":");
        myReader.close();

        Ecoute t = new Ecoute(config[1],Integer.parseInt(config[2]));
        t.start();
    }
}

