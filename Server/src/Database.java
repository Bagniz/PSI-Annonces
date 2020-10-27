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

    // Connect to the db_psi database
    public boolean connectToDB(){
        try {
            Class.forName("org.postgresql.Driver");
            File dbConfigFile = new File("../config/dbConfig.txt");
            Scanner reader = new Scanner(dbConfigFile);
            String[] dbConfig = reader.nextLine().split(":");
            reader.close();
            String dbUrl = "jdbc:postgresql://localhost:" + dbConfig[2] + "/" + dbConfig[3];
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
            System.out.println("Connected to the database");
        } catch (ClassNotFoundException | FileNotFoundException exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    // Get database version
    public String getDbVersion(){
        String version = "null";
        if (this.connection != null)
        {
            try {
                this.statement = this.connection.prepareStatement("SELECT VERSION()");
                this.resultSet = this.statement.executeQuery();
                if(this.resultSet.next())
                    version = this.resultSet.getString("version");
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        return version;
    }

    // Check if a client already exists
    public boolean doesClientExist(String email){
        String clientExistenceQuery = "SELECT * FROM clients WHERE email=?";
        try {
            this.statement = this.connection.prepareStatement(clientExistenceQuery);
            this.statement.setString(1, email);
            this.resultSet = this.statement.executeQuery();
            if(this.resultSet.next())
                    return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    // Create a new client
    public int signUp(String first_name, String last_name, String birthdate, String email, String password, String address, int postal_code, String city, String phone_number){
        if(this.doesClientExist(email))
            return -1;
        else{
            int idClient = 0;
            String signUpQuery = "INSERT INTO clients (first_name, last_name, birthdate, email, password, address, postal_code, city, phone_number, is_active) VALUES (?,?,?,?,?,?,?,?,?,true)";
            try {
                this.statement = this.connection.prepareStatement(signUpQuery, Statement.RETURN_GENERATED_KEYS);
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
                        idClient = resultSet.getInt(1);
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            return idClient;
        }
    }

    // Verify the existence of a client
    public int logIn(String email,String password){
        int idClient = 0;
        String logInQuery = "SELECT id FROM clients WHERE email = ? AND password = crypt(?, 'md5')";
        try {
            this.statement = this.connection.prepareStatement(logInQuery);
            this.statement.setString(1, email);
            this.statement.setString(2, password);
            this.resultSet = this.statement.executeQuery();
            if(this.resultSet.next())
                idClient = this.resultSet.getInt("id");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return idClient;
    }

    // Get an ad
    public String getAnAd(int idAd){
        StringBuilder ad = new StringBuilder("null");
        String getAnAddQuery = "SELECT * FROM ads WHERE id = ?";
        try {
            this.statement = this.connection.prepareStatement(getAnAddQuery);
            this.statement.setInt(1, idAd);
            this.resultSet = this.statement.executeQuery();
            ResultSet temp = this.resultSet;
            if(this.resultSet.next()){
                ad = new StringBuilder(temp.getString("id"));
                ad.append("|").append(temp.getString("title"));
                ad.append("|").append(temp.getString("description"));
                ad.append("|").append(temp.getString("price"));
                ad.append("|").append(this.getCategory(temp.getInt("id_cat")));
                ad.append("|").append(temp.getString("posted_by"));
                ad.append("|").append(temp.getString("posting_date"));
                ad.append("|").append(temp.getString("is_reserved"));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return ad.toString();
    }

    private String getCategory(int id_cat) {
        String category = "null";
        String getCategoryQuery = "SELECT name FROM categories WHERE id = ?";
        try {
            this.statement = this.connection.prepareStatement(getCategoryQuery);
            this.statement.setInt(1, id_cat);
            this.resultSet = this.statement.executeQuery();
            if(this.resultSet.next()){
                category = this.resultSet.getString("name");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return category;
    }

    // Add an ad
    public int addAd(String title, String description, float price, int idCat, int postedBy)
    {
        int idAd = 0;
        String addAdQuery = "INSERT INTO ads (title,description,price,id_cat,posted_by) VALUES (?,?,?,?,?)";
        try {
            this.statement = this.connection.prepareStatement(addAdQuery, Statement.RETURN_GENERATED_KEYS);
            this.statement.setString(1, title);
            this.statement.setString(2, description);
            this.statement.setFloat(3, price);
            this.statement.setInt(4, idCat);
            this.statement.setInt(5, postedBy);
            int affectedRows = this.statement.executeUpdate();
            if(affectedRows > 0){
                this.resultSet = this.statement.getGeneratedKeys();
                if(this.resultSet.next())
                    idAd = resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return idAd;
    }

    // Update an existing ad
    public boolean updateAd(int idAdd, String title, String description, float price, int idCat, int postedBy){
        String updateAddQuery = "UPDATE ads SET title=?, description=?,  price=?, id_cat=? WHERE id=? AND posted_by=?";
        try {
            this.statement = this.connection.prepareStatement(updateAddQuery);
            this.statement.setString(1, title);
            this.statement.setString(2, description);
            this.statement.setFloat(3, price);
            this.statement.setInt(4, idCat);
            this.statement.setInt(5, idAdd);
            this.statement.setInt(6, postedBy);
            int affectedRows = this.statement.executeUpdate();
            if(affectedRows > 0)
                return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public String[] getClientInformation(int id_client)
    {
        String[] client = new String[9];
        String getClientQuery = "SELECT * FROM clients WHERE id = ?";
        try {
            this.statement = this.connection.prepareStatement(getClientQuery);
            this.statement.setInt(1, id_client);
            this.resultSet = this.statement.executeQuery();
            ResultSet temp = this.resultSet;
            if(this.resultSet.next()){
                client[0] = temp.getString("first_name");
                client[1] = temp.getString("last_name");
                client[2] = temp.getString("birthdate");
                client[3] = temp.getString("email");
                client[4] = temp.getString("password");
                client[5] = temp.getString("address");
                client[6] = temp.getString("postal_code");
                client[7] = temp.getString("city");
                client[8] = temp.getString("phone_number");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return client;
    }

    public boolean updateClient(String first_name, String last_name, String birthdate, String email, String oldPassword, String newPassword, String address, int postal_code, String city, String phone_number, int id_client)
    {
        String updateClientQuery = "UPDATE clients SET first_name = ?, last_name = ?,  birthdate = ?, email = ?, password = ?, address = ?, postal_code = ?, city = ?, phone_number = ? WHERE id = ? AND password = crypt(?, 'md5')";
        try {
            this.statement = this.connection.prepareStatement(updateClientQuery);
            this.statement.setString(1, first_name);
            this.statement.setString(2, last_name);
            this.statement.setDate(3, Date.valueOf(birthdate));
            this.statement.setString(4, email);
            if(newPassword.equals("next")){
                this.statement.setString(5, oldPassword);
            }
            else{
                this.statement.setString(5, newPassword);
            }
            this.statement.setString(6, address);
            this.statement.setInt(7, postal_code);
            this.statement.setString(8, city);
            this.statement.setString(9, phone_number);
            this.statement.setInt(10, id_client);
            this.statement.setString(11, oldPassword);

            int affectedRows = this.statement.executeUpdate();
            if(affectedRows > 0)
                return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    // Delete an existing ad
    public boolean deleteAd(int idAdd, int postedBy){
        String deleteAddQuery = "DELETE FROM ads WHERE id=? AND posted_by=?";
        try {
            this.statement = this.connection.prepareStatement(deleteAddQuery);
            this.statement.setInt(1, idAdd);
            this.statement.setInt(2, postedBy);
            int affectedRows = this.statement.executeUpdate();
            if(affectedRows > 0)
                return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }
    public boolean deleteClient(String password,int id_client)
    {
        String deleteClientQuery = "DELETE FROM clients WHERE id = ? AND password = crypt(?, 'md5')";
        try {
            this.statement = this.connection.prepareStatement(deleteClientQuery);
            this.statement.setInt(1, id_client);
            this.statement.setString(2, password);
            int affectedRows = this.statement.executeUpdate();
            if(affectedRows > 0)
                return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }
}
