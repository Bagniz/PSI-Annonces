import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Database {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public Database(){
        this.connection = null;
        this.statement = null;
        this.resultSet = null;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public boolean connectToDB(){
        try {
            Class.forName("org.postgresql.Driver");
            File dbConfigFile = new File("dbConfig.txt");
            Scanner reader = new Scanner(dbConfigFile);
            String[] dbConfig = reader.nextLine().split(":");
            reader.close();
            String dbUrl = "jdbc:postgresql://" + dbConfig[1] + ":" + dbConfig[2] + "/" + dbConfig[3];
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
                this.statement = this.connection.createStatement();
                this.resultSet = this.statement.executeQuery("SELECT VERSION()");
                if(this.resultSet.next())
                    version = this.resultSet.getString(1);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        return version;
    }
}
