package it.unisa.progettodb;

import it.unisa.progettodb.datacontrol.ContentPackage;
import it.unisa.progettodb.datacontrol.ContentWrap;
import it.unisa.progettodb.sql.DBManagement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TableManager {
    private String tableName;
    private final List<String> tableColumnList;
    private final List<JDBCType> typeList;
    private final JTable table;
    private final DBManagement managerDB;
    private DefaultTableModel model;

    private ContentWrap wrapData;


    public TableManager(JTable table, DBManagement connectDB) {
        this(null, new ArrayList<>(), table, connectDB);
    }

    public TableManager(String tableName, List<String> tableColumn, JTable table, DBManagement connectDB) {
        this.tableName = tableName;
        this.tableColumnList = tableColumn;
        this.table = table;
        this.managerDB = connectDB;
        this.typeList = new ArrayList<>();
    }


    /**
     * Reload The Current Working Table When Prompted
     * @throws SQLException if Select data failed
     */
    public void reloadTable() throws SQLException {
        this.setTable(this.tableName);
    }

    /**
     * Get Data from a Row by index in form of List of ContentPackage (metadata + data).
     * @param row index of the row in table.
     * @return a List of ContentPackage containing all data of a single row.
     */
    public List<ContentPackage> getRowContentPackageList(int row){
        List<ContentPackage> res = new ArrayList<>();
        for(int i = 0; i < tableColumnList.size(); i++){
            String data = this.table.getModel().getValueAt(row, i).toString();

            res.add(new ContentPackage(i +1, data, tableColumnList.get(i), this.typeList.get(i)) );
        }
        return res;
    }

    /**
     * Return Current Working Table in TableManager
     * @return Table Name String
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Return Current Data in form of ContentWrap
     * @return ContentWrap containing Data and MetaData
     */
    public ContentWrap getWrapData() {
        return wrapData;
    }

    /**
     * OverLoaded Method. Sets a New Model Table with all data from table. <br />
     * A select will automatically queried. <br />
     * It receives a ContentWrap containing all data, so it doesn't directly interact with the DataBase but only with the manager instance
     * @see it.unisa.progettodb.TableManager#setTable(ContentWrap, String)
     * @param tableName String with the name of the Table in DB
     * @throws SQLException if Select fails
     */
    public void setTable(String tableName) throws SQLException {
        this.setTable(this.managerDB.executeSelect(new String[]{"*"}, tableName), tableName);
    }


    /**
     * OverLoaded Method. Sets a New Model Table based on ContentWrap Data passed as Parameter.
     * @param data ContentWrap where all data is loaded
     * @param tableName String with the name of the Table in DB
     */
    public void setTable(ContentWrap data, String tableName){
        this.tableName = tableName;
        this.wrapData = data;
        DefaultTableModel model = newModel();
        this.tableColumnList.clear();
        this.typeList.clear();

        for(ContentPackage c: data.getMetaData()){
            model.addColumn(c.getColumnName());
            this.tableColumnList.add(c.getColumnName());
            this.typeList.add(c.getType());
        }

        for(Map.Entry<Integer, List<String>> e: data.getRows().entrySet()){
            List<String> row = e.getValue();
            model.addRow(row.toArray());
        }
        this.table.setModel(model);
    }



    /*-------------------- PRIVATE METHODS ----------------------*/

    /**
     * Create a new DefaultTableModel with Custom Listener
     * @return new DefaultTableModel
     */
    private DefaultTableModel newModel() {

        DefaultTableModel newModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        /*new TableModelListener(){public void tableChanged(TableModelEvent e)}*/
        newModel.addTableModelListener(e -> {
                /*TODO*/

        });

        return newModel;
    }




    /*======================================= DEPRECATED =========================================*/
    /**
     * Main Function That Reprint Table.
     * Select * From tableName
     * Updates Column Names in List, Reformat the table columns and add data rows
     * @deprecated New setTable doesn't use directly the resultSet. It's more secure and simplifies the job of the table management. <br />
     * @see it.unisa.progettodb.TableManager#setTable(String)
     * @param tableName Name of SQL Table to Draw
     * @throws SQLException if Select Data Failed
     */
    @Deprecated()
    private void setTableOLD(String tableName) throws SQLException {
        this.tableName = tableName;
        try (ResultSet rSet = managerDB.executeSelectRSet(new String[]{"*"}, this.tableName)) {
            setTableColumnList(rSet);
            formatTable();
            setTableRows(rSet);
        }
    }


    /**
     * Create a new model (DefaultTableModel) and sets column names
     * @deprecated used only in deprecated setTableOLD
     * @see it.unisa.progettodb.TableManager#setTableRows(ResultSet)
     */
    @Deprecated
    private void formatTable(){
        model = newModel(); //Vedi giù
        tableColumnList.forEach( n -> model.addColumn(n));
        this.table.setModel(model);
        model.fireTableDataChanged();
    }

    /**
     * Updates this.tableColumnList with the Names of each column in specified table.
     * A resultSet has to be taken before this method call.
     * @param resultSet to get metadata from
     * @throws SQLException if getMetaData fails
     * @deprecated used only in deprecated setTableOLD
     * @see it.unisa.progettodb.TableManager#setTableRows(ResultSet)
     */
    @Deprecated
    private void setTableColumnList(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();

        int count = metaData.getColumnCount(); //number of column
        String[] columnName = new String[count];

        /* GetColumnLabel returns String with name of column of index i. It Starts From 1!*/
        this.tableColumnList.clear();
        this.typeList.clear();
        for (int i = 0; i < count; i++)
        {
            columnName[i] = metaData.getColumnLabel(i + 1);
            this.tableColumnList.add(columnName[i]);
            this.typeList.add(JDBCType.valueOf(metaData.getColumnType(i + 1)));
        }
    }


    /**
     * Populate Rows of Table. ResultSet is needed before calling this method.
     * @param rSet ResultSet to Parse
     * @throws SQLException if parsing fails
     * @deprecated used only in deprecated setTableOLD
     * @see it.unisa.progettodb.TableManager#setTableRows(ResultSet)
     */
    @Deprecated
    private void setTableRows(ResultSet rSet) throws SQLException {
        while(rSet.next()){
            List<String> row = new ArrayList<>();
            for(int i = 1; i <= tableColumnList.size(); i++){ //Indice riga ResultSet Parte da 1
                int type = rSet.getMetaData().getColumnType(i);
                if( type == Types.BIT || type == Types.BOOLEAN ){
                    row.add(rSet.getBoolean(i) ? "Si" : "No");
                } else {
                    row.add(rSet.getString(i));
                }
            }
            model.addRow(row.toArray());
        }
    }

}
