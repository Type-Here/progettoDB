package it.unisa.progettodb;

import it.unisa.progettodb.exceptions.NullTableException;
import it.unisa.progettodb.logs.LoggerManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("CallToPrintStackTrace")
public class DBManagement {
    private Connection connectDB;
    private final String DBName;
    private Statement stmt;
    private ResultSet rSet;
    private LoggerManager loggerManager;

    public enum ActionEnum {Select, Insert, Update, GetSchemas, Connected, FetchDataType}

    public DBManagement(String user, String pass) throws SQLException {
        this("localhost", 3306, user ,pass);
    }

    public DBManagement(String user, String pass, LoggerManager loggerManager) throws SQLException {
        this("localhost", 3306, user ,pass);
        this.loggerManager = loggerManager;
    }

    public DBManagement(String hostname, int port, String user, String pass) throws SQLException {
        this.DBName = "ATI";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://"+ hostname +":"+ port + "/" + DBName;
            System.out.println(url);
            connectDB = DriverManager.getConnection(url, user, pass);
            System.out.println("DataBase Connesso");
            stmt = connectDB.createStatement();

            this.sendToLog(ActionEnum.Connected);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Close Connection to DataBase
     */
    public void closeConnection(){
        try {
            connectDB.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Check if DataBase is correctly connected
     * @return true if connected, false otherwise
     */
    public boolean isConnected(){
        try {
            return connectDB.isValid(5);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get Names of Schemas in DataBase (Table Names)
     * @return list of string containing the names of
     * @throws SQLException if fails to get info
     */
    public List<String> getTablesName() throws SQLException {
        //DatabaseMetaData md = connectDB.getMetaData();
        List<String> tabNames = new ArrayList<>();
        /*ResultSet rs = md.getTables(null, "ATI", "%", null);*/
        String query = " select table_name from information_schema.tables WHERE table_schema = '"+ DBName + '\'';
        /*Statement stmt = connectDB.createStatement();*/
        rSet = stmt.executeQuery(query);

        while (rSet.next()) {
            tabNames.add( rSet.getString(1) );
        }

        this.sendToLog(ActionEnum.GetSchemas);
        return tabNames;
    }

    /**
     * Send Data to logs.LoggerManager if exists
     * @param action action to log
     */
    private void sendToLog(ActionEnum action) {
        if(this.loggerManager != null) loggerManager.log(action);
    }

    /**
     * Send Data to logs.LoggerManager if exists
     * @param info String to add detail (used also to print query)
     * @param action action to log
     */
    private void sendToLog(String info, ActionEnum action) {
        if(this.loggerManager != null) loggerManager.log(info, action);
    }

    /**
     * Execute a SELECT row0, row1 [, ...] FROM TableName
     * @param row column to SELECT (es. *, name, surname ecc..)
     * @param tableName table to select FROM
     * @return ResultSet with result Data
     * @throws SQLException if SELECT fails
     */
    public ResultSet executeSelect(String[] row, String tableName) throws SQLException {
        StringBuilder build = new StringBuilder();
        Iterator<String> rowIt = Arrays.stream(row).iterator();

        build.append("SELECT ");
        while(rowIt.hasNext()) {
            build.append(rowIt.next());
            if(rowIt.hasNext()) build.append(", ");
        }
        build.append(" FROM ").append(tableName);
        System.out.println("Query: '" + build + '\'');

        rSet = stmt.executeQuery(build.toString());

        this.sendToLog(build.toString(), ActionEnum.Select);
        return rSet;
    }

    public List<JDBCType> fetchDataType(String tableName) throws SQLException {
        if(tableName == null) throw new NullTableException();

        List<JDBCType> dataType = new ArrayList<>();
        try( ResultSet rSet = this.executeSelect(new String[]{"*"}, tableName) ){
            for (int i = 1; i <= rSet.getMetaData().getColumnCount(); i++) {
                int type = rSet.getMetaData().getColumnType(i);
                dataType.add(JDBCType.valueOf(type));
            }
        }

        this.sendToLog("On " + tableName, ActionEnum.FetchDataType);
        return dataType;
    }


}
