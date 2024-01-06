package it.unisa.progettodb.sql;
import it.unisa.progettodb.datacontrol.ContentPackage;
import it.unisa.progettodb.exceptions.NullTableException;
import it.unisa.progettodb.logs.LoggerManager;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;
import java.util.*;

@SuppressWarnings("CallToPrintStackTrace")
public class DBManagement {
    private Connection connectDB;
    private final String DBName;
    private Statement stmt;
    private ResultSet rSet;
    private LoggerManager loggerManager;

    public enum ActionEnum {Select, Insert, Delete, Update, GetSchemas, Connected, FetchDataType, FetchMetaData}

    /* ===== CONSTRUCTORS ===== */

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

    /* ===============  BASIC CONNECTION CONTROLS ================== */

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

    /* ========================= METADATA FETCH ========================== */

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
     * Fetch Data Type of tableName Table (DataType = Int, Double, ecc of JDBCType)
     * @param tableName table to Fetch data from
     * @return List of JDBCType for each column
     * @throws SQLException if select and getMetaData fail
     */
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


    /**
     * Fetch ResultSetMetaData of a Table which name is tableName
     * @param tableName table to Fetch data from
     * @return a ResultSetMetaData containing all metadata from Table.
     * @throws SQLException if select and getMetaData fail
     */
    public ResultSetMetaData fetchMetaData(String tableName) throws SQLException {
        if(tableName == null) throw new NullTableException();

        ResultSetMetaData metaData;
        CachedRowSet resultCached;

        try( ResultSet rSet = this.executeSelect(new String[]{"*"}, tableName) ){
            resultCached = RowSetProvider.newFactory().createCachedRowSet();
            resultCached.populate(rSet);

            metaData = resultCached.getMetaData();

            resultCached.close();
        }

        this.sendToLog("On " + tableName, ActionEnum.FetchMetaData);
        return metaData;
    }


    /**
     * Retrieve Attributes that compose Primary key for a specific table
     * @param tableName Table Name of which retrieve PK from
     * @return HashMap of Primary Keys; K:ColumnName, E:KeySeq (See: {@link java.sql.DatabaseMetaData#getPrimaryKeys(String, String, String)} )
     * @throws SQLException if metadata getPrimaryKeys fails
     */
    public HashMap<String, Integer> retrievePrimaryKeys(String tableName) throws SQLException {
        HashMap<String, Integer> resultMap = new LinkedHashMap<>();
        //Retrieving meta data
        DatabaseMetaData metaData = connectDB.getMetaData();

        //Retrieving the columns in the table
        ResultSet rs = metaData.getPrimaryKeys("ATI", null, tableName);
        //Printing the column name and size
        while (rs.next()){
            /*System.out.println("Table name: "+rs.getString("TABLE_NAME"));
            System.out.println("Column name: "+rs.getString("COLUMN_NAME"));
            System.out.println("Catalog name: "+rs.getString("TABLE_CAT"));
            System.out.println("Primary key sequence: "+rs.getString("KEY_SEQ"));
            System.out.println("Primary key name: "+rs.getString("PK_NAME"));
            System.out.println(" ");*/
            resultMap.put(rs.getString("COLUMN_NAME"), rs.getInt("KEY_SEQ"));
        }

        return resultMap;
    }

    /**
     * Check if Table Name is a View
     * @param tableName to check
     * @return true if table is a view, false if not
     * @throws SQLException if getMetaData fails
     */
    public boolean isAView(String tableName) throws SQLException {
        DatabaseMetaData dbm = connectDB.getMetaData();
        try(ResultSet table = dbm.getTables(null, null, tableName, new String[]{"VIEW"}) ){
            return table.first();
        }
    }


    /* ========================= METADATA FORMAT FOR USAGE ======================== */

    /**
     * Retrieve MetaData of Table with tableName and Create a List of ContentPackage with only MetaData. <br />
     * 1 ContentPackage = 1 Attribute for the Specified Table. <br />
     * MetaData Contains: Index, ColumnName, Precision, isNullable, JDBCType <br />
     * Data String is Null.
     * @param tableName name of the Table to Fetch Data From
     * @return List of ContentPackage with MetaData
     * @throws SQLException if fetchMetaData fails
     */
    public List<ContentPackage> makeEmptyContentPackage(String tableName) throws SQLException {
        ResultSetMetaData metaData = fetchMetaData(tableName);

        List<ContentPackage> list = new ArrayList<>();
        for(int i = 1; i <= metaData.getColumnCount(); i++){
            ContentPackage c = new ContentPackage(i, null, metaData.getColumnName(i),
                    JDBCType.valueOf(metaData.getColumnType(i)));
            c.setPrecision(metaData.getPrecision(i));
            if(metaData.isNullable(i) == ResultSetMetaData.columnNullable) c.setNullable(true);
            list.add(c);
        }
        return list;
    }


    /* ================================= MAIN QUERY EXECUTION ============================== */

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

    /**
     * Execute INSERT in Table.
     * Use a Prepared Statement For Sending Data.
     * @param dataMap HashMap: K: String with Column Name, E: Data in Object Format see {@link it.unisa.progettodb.datacontrol.ContentPackage#returnDataForQuery(List)}
     * @param tableName Table Name Where Insertion will be performed (String)
     * @throws SQLException if Insert Fails
     */
    public void executeInsert(HashMap<String, Object> dataMap, String tableName) throws SQLException {
        StringBuilder buildIns = new StringBuilder();
        StringBuilder buildVal = new StringBuilder();
        buildIns.append("INSERT INTO ").append(tableName).append(" (");
        buildVal.append(" VALUES (");
        // Statement could be also created here but prone to SQL Injection.
        // So This Add Controlled Data (Column Names and Number of Values)
        int i = 0;
        for(Map.Entry<String, Object> e : dataMap.entrySet()){
            if(i++ == 0) {
                buildIns.append(e.getKey());
                buildVal.append("?");
            } else {
                buildIns.append(",").append(e.getKey());
                buildVal.append(",?");
            }
        }
        buildIns.append(") ").append(buildVal).append(");");

        PreparedStatement pStmt = connectDB.prepareStatement(buildIns.toString());

        /* Fill PreparedStatement with Data Using Statement method */
        i = 1;
        for(Map.Entry<String, Object> e : dataMap.entrySet()){
            if(e.getValue() == null) throw new RuntimeException("Valore Null in INSERT DB Management");
            if(e.getValue() instanceof String s){
                if(s.isEmpty()) throw new RuntimeException("Stringa Vuota in INSERT DBManagement");
            }
            pStmt.setObject(i++, e.getValue());
        }

        /*Execute Insert Query*/
        pStmt.executeUpdate();

        //Logs
        sendToLog(pStmt.toString(), ActionEnum.Insert);
    }

    /**
     * Execute DELETE from Table.
     * Use a Prepared Statement For Sending Data.
     * DataMap Contains Primary Keys Only!
     * @param dataMap HashMap: K: String with Column Name, E: Data in Object Format see {@link it.unisa.progettodb.datacontrol.ContentPackage#returnDataForQuery(List)}
     * @param tableName Table Name Where Deletion will be performed (String)
     * @throws SQLException if Insert Fails
     */
    public void executeDelete(HashMap<String, Object> dataMap, String tableName) throws SQLException {
        StringBuilder buildIns = new StringBuilder();
        buildIns.append("DELETE FROM ").append(tableName).append(" WHERE ");

        // Statement could be also created here but prone to SQL Injection.
        // So This Add Controlled Data (Column Names and Number of Values)
        int i = 0;
        for(Map.Entry<String, Object> e : dataMap.entrySet()){
            if(i++ == 0) {
                buildIns.append(e.getKey()).append("=?");
            } else {
                buildIns.append(" AND ").append(e.getKey()).append("=?");
            }
        }
        buildIns.append(" ;");

        PreparedStatement pStmt = connectDB.prepareStatement(buildIns.toString());

        /* Fill PreparedStatement with Data Using Statement method */
        i = 1;
        for(Map.Entry<String, Object> e : dataMap.entrySet()){
            if(e.getValue() == null) throw new RuntimeException("Valore Null in DELETE DBManagement");
            if(e.getValue() instanceof String s){
                if(s.isEmpty()) throw new RuntimeException("Stringa Vuota in DELETE DBManagement");
            }
            pStmt.setObject(i++, e.getValue());
        }

        /*Execute Delete Query*/
        pStmt.executeUpdate();
        //Logs
        sendToLog(pStmt.toString(), ActionEnum.Delete);
    }

    public void executeUpdate(HashMap<String, Object> dataMap, String tableName,  HashMap<String,Object> primaryKeyValues) throws SQLException {
        StringBuilder buildIns = new StringBuilder();
        buildIns.append("UPDATE ").append(tableName).append(" SET ");

        // Statement could be also created here but prone to SQL Injection.
        // So This Add Controlled Data (Column Names and Number of Values)
        int i = 0;
        for(Map.Entry<String, Object> e : dataMap.entrySet()){
            if(i++ == 0) {
                buildIns.append(e.getKey()).append("=?");
            } else {
                buildIns.append(" AND ").append(e.getKey()).append("=?");
            }
        }

        buildIns.append(" WHERE ");

        for(Map.Entry<String, Object> e : primaryKeyValues.entrySet()){
            if(i++ == 0) {
                buildIns.append(e.getKey()).append("=?");
            } else {
                buildIns.append(" AND ").append(e.getKey()).append("=?");
            }
        }

        buildIns.append(" ;");

        PreparedStatement pStmt = connectDB.prepareStatement(buildIns.toString());

        /* Fill PreparedStatement with Data Using Statement method */
        i = 1;
        for(Map.Entry<String, Object> e : dataMap.entrySet()){
            if(e.getValue() == null) throw new RuntimeException("Valore Null in UPDATE DBManagement");
            if(e.getValue() instanceof String s){
                if(s.isEmpty()) throw new RuntimeException("Stringa Vuota in UPDATE DBManagement");
            }
            pStmt.setObject(i++, e.getValue());
        }

        i = 1;
        for(Map.Entry<String, Object> e : primaryKeyValues.entrySet()){
            if(e.getValue() == null) throw new RuntimeException("Valore Null in UPDATE DBManagement");
            if(e.getValue() instanceof String s){
                if(s.isEmpty()) throw new RuntimeException("Stringa Vuota in UPDATE DBManagement");
            }
            pStmt.setObject(i++, e.getValue());
        }

        /*Execute UPDATE Query*/
        /*pStmt.executeUpdate();*/

        //Logs
        sendToLog(pStmt.toString(), ActionEnum.Delete);
    }


    /* ================== LOGS METOHDS ==================== */

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

}
