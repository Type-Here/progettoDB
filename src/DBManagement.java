import java.sql.*;

@SuppressWarnings("CallToPrintStackTrace")
public class DBManagement {
    private Connection connectDB;
    public DBManagement(String user, String pass) throws SQLException {
        this("localhost", 3306, user ,pass);
    }
    public DBManagement(String hostname, int port, String user, String pass) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connectDB = DriverManager.getConnection("jdbc:mysql://"+ hostname +":"+ port +"/ATI", user, pass);
            System.out.println("DataBase Connesso");

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public void closeConnection(){
        try {
            connectDB.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean isConnected(){
        try {
            return connectDB.isValid(5);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
