import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Client {
    public static void main(String[] args) throws InterruptedException {
        TimeUnit.SECONDS.sleep(20);
        File serverConfigFile = new File("serverConfig.txt");
        try {
            Scanner reader = new Scanner(serverConfigFile);
            String[] config = reader.nextLine().split(":");
            reader.close();
            Ecoute t = new Ecoute(config[1],Integer.parseInt(config[2]));
            t.start();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}