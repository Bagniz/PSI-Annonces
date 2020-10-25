import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Database {
    // Attributes
    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;

    // Constructor
    public Database(){
        this.connection = null;
        this.statement = null;
        this.resultSet = null;
    }

    public boolean connectToDB(){
        try {
            Class.forName("org.postgresql.Driver");
            File dbConfigFile = new File("dbConfig.txt");
            Scanner reader = new Scanner(dbConfigFile);
            String[] dbConfig = reader.nextLine().split(":");
            reader.close();
            String dbUrl = "jdbc:postgresql://10.188.41.217:" + dbConfig[2] + "/" + dbConfig[3];
            System.out.println(dbUrl);
            Properties dbProperties = new Properties();
            dbProperties.setProperty("user","postgres");
            dbProperties.setProperty("password","postgres");
            dbProperties.setProperty("ssl","false");
            while (this.connection == null){
                try {
                    this.connection = DriverManager.getConnection(dbUrl, dbProperties);
                } catch (SQLException ignore){}
            }
            System.out.println("connecté a la bases");
        } catch (ClassNotFoundException | FileNotFoundException exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    public String getDbVersion(){
        String version = "null";
        if (this.connection != null)
        {
            try {
                this.statement = this.connection.prepareStatement("SELECT VERSION()");
                this.resultSet = this.statement.executeQuery();
                if(this.resultSet.next())
                    version = this.resultSet.getString(1);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        return version;
    }

    public int signUp(String first_name,String last_name,String birthdate,String email,String password,String address,int postal_code,String city,String phone_number){
        int id = 0;
        String query = "INSERT INTO clients (first_name, last_name, birthdate, email, password, address, postal_code, city, phone_number, is_active) VALUES (?,?,?,?,?,?,?,?,?,true)";
        try {
            this.statement = this.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            this.statement.setString(1, first_name);
            this.statement.setString(2, last_name);
            this.statement.setDate(3, Date.valueOf(birthdate));
            this.statement.setString(4, email);
            this.statement.setString(5, password);
            this.statement.setString(6, address);
            this.statement.setInt(7, postal_code);
            this.statement.setString(8, city);
            this.statement.setString(9, phone_number);
            int affectedRows = this.statement.executeUpdate();
            if(affectedRows > 0){
                this.resultSet = this.statement.getGeneratedKeys();
                if(this.resultSet.next())
                    id = resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return id;
    }

    public int logIn(String email,String password){
        int id = 0;
        String query = "SELECT id FROM clients WHERE email = ? AND password = crypt(?, 'md5')";
        try {
            this.statement = this.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            this.statement.setString(1, email);
            this.statement.setString(2, password);
            this.resultSet = this.statement.executeQuery();
            if(this.resultSet.next())
                id = this.resultSet.getInt(1);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return id;
    }

    public int addAd(String title,String description,float price,int idCat,int postedBy)
    {
        int id = 0;
        String query = "INSERT INTO ads (title,description,price,id_cat,posted_by,posting_date) VALUES (?,?,?,?,?,CURRENT_TIMESTAMP)";
        try {
            this.statement = this.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            this.statement.setString(1, title);
            this.statement.setString(2, description);
            this.statement.setFloat(3, price);
            this.statement.setInt(4, idCat);
            this.statement.setInt(5, postedBy);
            int affectedRows = this.statement.executeUpdate();
            if(affectedRows > 0){
                this.resultSet = this.statement.getGeneratedKeys();
                if(this.resultSet.next())
                    id = resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return id;
    }
}
