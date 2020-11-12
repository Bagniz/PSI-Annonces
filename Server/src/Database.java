import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

public class Database {
    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;

    public boolean connectToDb(){
        try {
            Class.forName("org.postgresql.Driver");

            File dbConfigFile = new File("../config/dbConfig.txt");
            Scanner reader = new Scanner(dbConfigFile);
            String[] dbConfig = reader.nextLine().split(":");
            reader.close();

            String dbUrl = "jdbc:postgresql://"+ dbConfig[1] + ":" + dbConfig[2] + "/" + dbConfig[3];

            Properties dbProperties = new Properties();
            dbProperties.setProperty("user","postgres");
            dbProperties.setProperty("password","postgres");
            dbProperties.setProperty("ssl","false");

            while (this.connection == null){
                try {
                    this.connection = DriverManager.getConnection(dbUrl, dbProperties);
                } catch (SQLException ignore){}
            }
        } catch (ClassNotFoundException | FileNotFoundException exception) {
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
                    version = this.resultSet.getString("version");
            } catch (SQLException exception) {
                System.out.println("Could not get database version");
            }
        }
        return version;
    }

    public boolean isEmailUnique(String email){
        try{
            this.statement = this.connection.prepareStatement("SELECT * FROM clients WHERE email=?");
            this.statement.setString(1, email);
            this.resultSet = this.statement.executeQuery();
            if(this.resultSet.next())
                return false;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return true;
    }

    public Response SignUp(String firstName, String lastName, Date birthdate, String email, String password, String address, int postal_code, String city, String phone_number){
        Response response = new Response();
        if(!isEmailUnique(email)){
            response.setMessage("An account already exists with this email");
        }
        else{
            String signUpQuery = "INSERT INTO clients (first_name, last_name, birthdate, email, password, address, postal_code, city, phone_number, is_active) VALUES (?,?,?,?,?,?,?,?,?,true)";
            try {
                this.statement = this.connection.prepareStatement(signUpQuery, Statement.RETURN_GENERATED_KEYS);
                this.statement.setString(1, firstName);
                this.statement.setString(2, lastName);
                this.statement.setDate(3, birthdate);
                this.statement.setString(4, email);
                this.statement.setString(5, password);
                this.statement.setString(6, address);
                this.statement.setInt(7, postal_code);
                this.statement.setString(8, city);
                this.statement.setString(9, phone_number);
                int affectedRows = this.statement.executeUpdate();
                if(affectedRows > 0){
                    response.setSucceeded(true);
                    response.setMessage("Account created");
                    this.resultSet = this.statement.getGeneratedKeys();
                    this.resultSet.next();
                    response.setClient(this.getClient(resultSet.getInt(1)));
                }
                else{
                    response.setSucceeded(false);
                    response.setMessage("Sign up error, please check your info and try again");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.setSucceeded(false);
                response.setMessage("Operation error, try again");
            }
        }
        return response;
    }

    public Response logIn(String email, String password){
        Response response = new Response();
        try{
            this.statement = this.connection.prepareStatement("SELECT * FROM clients WHERE email=? AND password=crypt(?,'md5')");
            this.statement.setString(1, email);
            this.statement.setString(2, password);
            this.resultSet = this.statement.executeQuery();
            if(this.resultSet.next()){
                response.setSucceeded(true);
                response.setMessage("Logged In");
                response.setClient(this.getClient(this.resultSet.getInt("id")));
            }
            else{
                response.setSucceeded(false);
                response.setMessage("Invalid email / password");
            }
        } catch (SQLException exception){
            exception.printStackTrace();
            response.setSucceeded(false);
            response.setMessage("Operation error, try again");
        }
        return response;
    }

    public Client getClient(int clientId){
        Client client = null;
        try{
            this.statement = this.connection.prepareStatement("SELECT * FROM clients WHERE id=?");
            this.statement.setInt(1, clientId);
            this.resultSet = this.statement.executeQuery();
            if(this.resultSet.next()){
                client = new Client(clientId,
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getDate("birthdate"),
                        resultSet.getString("email"),
                        resultSet.getString("address"),
                        resultSet.getInt("postal_code"),
                        resultSet.getString("city"),
                        resultSet.getString("phone_number"),
                        resultSet.getBoolean("is_active"));
            }
        } catch (SQLException exception){
            exception.printStackTrace();
        }
        return client;
    }

    public Category getCategory(int categoryId){
        Category category = null;
        try{
            this.statement = this.connection.prepareStatement("SELECT * FROM categories WHERE id=?");
            this.statement.setInt(1, categoryId);
            this.resultSet = this.statement.executeQuery();
            if(this.resultSet.next()){
                category = new Category(categoryId, resultSet.getString("name"));
            }
        } catch (SQLException exception){
            exception.printStackTrace();
        }
        return category;
    }

    public Response getAds(boolean mine, int clientId){
        Response response = new Response();
        ArrayList<Ad> ads = new ArrayList<>();
        String getAdsQuery = "SELECT * FROM ads WHERE";
        if(mine)
            getAdsQuery += " posted_by = ?";
        else
            getAdsQuery += " posted_by != ? AND is_reserved=false";
        try {
            this.statement = this.connection.prepareStatement(getAdsQuery);
            this.statement.setInt(1, clientId);
            this.resultSet = this.statement.executeQuery();
            ResultSet resultSet = this.resultSet;
            fillAdsArray(response, ads, resultSet);
        } catch (SQLException exception) {
            exception.printStackTrace();
            response.setMessage("Operation error, try again");
        }
        return response;
    }

    public Ad getAd(int adId){
        Ad ad = null;
        try{
            this.statement = this.connection.prepareStatement("SELECT * FROM ads WHERE id=?");
            this.statement.setInt(1, adId);
            this.resultSet = this.statement.executeQuery();
            ResultSet resultSet = this.resultSet;
            if(this.resultSet.next()){
                ad = new Ad(adId,
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getFloat("price"),
                        this.getCategory(resultSet.getInt("id_cat")),
                        this.getClient(resultSet.getInt("posted_by")),
                        resultSet.getDate("posting_date"),
                        resultSet.getBoolean("is_reserved"));
            }
        } catch (SQLException exception){
            exception.printStackTrace();
        }
        return ad;
    }

    public Response addAd(String title, String description, float price, int idCat, int postedBy) {
        Response response = new Response();
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
                this.resultSet.next();
                response.setMessage("Added an ad with id: " + this.resultSet.getInt(1) + " and title: " + title);
            }
            else{
                response.setMessage("Could not add the ad, please check info and try again");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            response.setMessage("Operation error, try again");
        }
        return response;
    }

    public Response updateAd(int idAdd, String title, String description, float price, int idCat, int postedBy) {
        Response response = new Response();
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
                response.setMessage("Ad with id: " + idAdd + "is now updated");
            else{
                response.setMessage("Could not update the ad, please check info and try again");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            response.setMessage("Operation error, try again");
        }
        return response;
    }

    public Response deleteAd(int idAdd, int postedBy) {
        Response response = new Response();
        String deleteAdQuery = "DELETE FROM ads WHERE id=? AND posted_by=?";
        try {
            this.statement = this.connection.prepareStatement(deleteAdQuery);
            this.statement.setInt(1, idAdd);
            this.statement.setInt(2, postedBy);
            int affectedRows = this.statement.executeUpdate();
            if(affectedRows > 0)
                response.setMessage("Ad with id: " + idAdd + "is now deleted");
            else{
                response.setMessage("Could not delete the ad, please check info and try again");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            response.setMessage("Operation error, try again");
        }
        return response;
    }

    public Response getReservedAds(int clientId) {
        Response response = new Response();
        ArrayList<Ad> ads = new ArrayList<>();
        String getAdsQuery = "SELECT * FROM ads WHERE is_reserved = true AND id IN (SELECT id_ad FROM reservations WHERE id_client = ?)";
        try {
            this.statement = this.connection.prepareStatement(getAdsQuery);
            this.statement.setInt(1, clientId);
            this.resultSet = this.statement.executeQuery();
            fillAdsArray(response, ads, resultSet);
        } catch (SQLException exception) {
            exception.printStackTrace();
            response.setMessage("Operation error, try again");
        }
        return response;
    }

    private void fillAdsArray(Response response, ArrayList<Ad> ads, ResultSet resultSet) throws SQLException {
        if(this.resultSet.next()){
            Ad ad;
            do {
                ad = new Ad(resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getFloat("price"),
                        this.getCategory(resultSet.getInt("id_cat")),
                        this.getClient(resultSet.getInt("posted_by")),
                        resultSet.getDate("posting_date"),
                        resultSet.getBoolean("is_reserved"));
                ads.add(ad);
            } while (resultSet.next());
            response.setAds(ads);
        }
        else{
            response.setMessage("There are no ads");
        }
    }

    public Response reserveAd(int idAd, int idClient) {
        Response response = new Response();
        String reserveAdQuery = "INSERT INTO reservations (id_ad,id_client) VALUES (?,?); UPDATE ads SET is_reserved = true WHERE id = ?";
        try{
            this.statement = this.connection.prepareStatement(reserveAdQuery);
            this.statement.setInt(1, idAd);
            this.statement.setInt(2, idClient);
            this.statement.setInt(3, idAd);
            int affectedRows = this.statement.executeUpdate();
            if(affectedRows > 0)
                response.setMessage("Ad with id: " + idAd + "is now reserved");
            else
                response.setMessage("Could not reserve ad with id: " + idAd);
        } catch (SQLException exception){
            exception.printStackTrace();
            response.setMessage("Operation error, try again");
        }
        return response;
    }

    public Response unReserveAd(int idAd, int client) {
        Response response = new Response();
        String reserveAdQuery = "UPDATE ads SET is_reserved = false WHERE id = ?;DELETE FROM reservations WHERE id_ad = ? AND (SELECT posted_by FROM ads WHERE id = ?) = ? OR id_client = ?";
        try{
            this.statement = this.connection.prepareStatement(reserveAdQuery);
            this.statement.setInt(1, idAd);
            this.statement.setInt(2, idAd);
            this.statement.setInt(3, client);
            this.statement.setInt(4, client);
            this.statement.setInt(5, idAd);
            int affectedRows = this.statement.executeUpdate();
            System.out.println(affectedRows);
            if(affectedRows > 0)
                response.setMessage("Ad with id: " + idAd + "is now unreserved");
            else
                response.setMessage("Could not unreserve ad with id: " + idAd);
        } catch (SQLException exception){
            exception.printStackTrace();
            response.setMessage("Operation error, try again");
        }
        return response;
    }

    public Response updateClient(String firstName, String lastName, Date birthdate, String email, String oldPassword, String newPassword, String address, int postalCode, String city, String phoneNumber, int idClient) {
        Response response = new Response();
        String updateClientQuery = "UPDATE clients SET first_name = ?, last_name = ?,  birthdate = ?, email = ?, password = ?, address = ?, postal_code = ?, city = ?, phone_number = ? WHERE id = ? AND password = crypt(?, 'md5')";
        try {
            this.statement = this.connection.prepareStatement(updateClientQuery);
            this.statement.setString(1, firstName);
            this.statement.setString(2, lastName);
            this.statement.setDate(3, birthdate);
            this.statement.setString(4, email);
            if(newPassword.equals("next")){
                this.statement.setString(5, oldPassword);
            }
            else{
                this.statement.setString(5, newPassword);
            }
            this.statement.setString(6, address);
            this.statement.setInt(7, postalCode);
            this.statement.setString(8, city);
            this.statement.setString(9, phoneNumber);
            this.statement.setInt(10, idClient);
            this.statement.setString(11, oldPassword);

            int affectedRows = this.statement.executeUpdate();
            if(affectedRows > 0){
                response.setSucceeded(true);
                response.setMessage("Account updated");
            }
            else{
                response.setSucceeded(false);
                response.setMessage("Could not update your account, please check info and try again");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            response.setSucceeded(false);
            response.setMessage("Operation error, try again");
        }
        return response;
    }

    public Response deleteClient(int id_client, String password) {
        Response response = new Response();
        String deleteClientQuery = "DELETE FROM clients WHERE id = ? AND password = crypt(?, 'md5')";
        try {
            this.statement = this.connection.prepareStatement(deleteClientQuery);
            this.statement.setInt(1, id_client);
            this.statement.setString(2, password);
            int affectedRows = this.statement.executeUpdate();
            if(affectedRows > 0) {
                response.setSucceeded(true);
                response.setMessage("Your account is now deleted");
            }
            else{
                response.setSucceeded(false);
                response.setMessage("Could not delete account, please try again");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            response.setSucceeded(false);
            response.setMessage("Operation error, try again");
        }
        return response;
    }
}
