package lidselecter;

import java.sql.*;

public class Sql_connect {

    private static          Connection  conn = null;
    private static final    String      host                = "meru.hhs.nl";
    private static final    String      dbName              = "14096811";
    private static final    String      username            = "14096811";
    private static final    String      password            = "aiPapuuyaW";
    private static final    String      connectionString    = "jdbc:mysql://" + host + "/" + dbName + "?user=" + username + "&password=" + password;
    
    //returns the connection
    public static Connection getConnection() {
        return conn;
    }

    //checks if there is a connection
    private static boolean isConnected() {
        if (conn == null) {
            return false;
        }
        try {
            return !conn.isClosed();
        } catch (Exception e) {
            System.out.println("Connection not definable");
            System.out.println(e);
            return false;
        }
    }

    //connects to the database using the information declared above
    public static void doConnect() {
        try {
            if (isConnected()) {
                closeConnection();
            }
                conn = DriverManager.getConnection(connectionString);
        } catch (Exception e) {
            System.out.println("Error while connecting");
            System.out.println(e);
        }
    }

    //closes the conection if alive  
    private static void closeConnection() {
        try {
            if (isConnected()) {
                conn.close();
            }
        } catch (Exception e) {
            System.out.println("Error while closing");
            System.out.println(e);
        }
    }
}
