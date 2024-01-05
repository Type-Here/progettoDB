package it.unisa.progettodb;

import it.unisa.progettodb.datacontrol.ContentPackage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TableManager {
    private String tableName;
    private final List<String> tableColumnList;
    private final List<JDBCType> typeList;
    private final JTable table;
    private final DBManagement managerDB;
    private DefaultTableModel model;



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
     * Main Function That Reprint Table.
     * Select * From tableName
     * Updates Column Names in List, Reformat the table columns and add data rows
     * @param tableName Name of SQL Table to Draw
     * @throws SQLException if Select Data Failed
     */
    public void setTable(String tableName) throws SQLException {
        this.tableName = tableName;
        try (ResultSet rSet = managerDB.executeSelect(new String[]{"*"}, this.tableName)) {
            setTableColumnList(rSet);
            formatTable();
            setTableRows(rSet);
        }
    }

    /**
     * Reload The Current Working Table When Prompted
     * @throws SQLException if Select data failed
     */
    public void reloadTable() throws SQLException {
        this.setTable(this.tableName);
    }

    /**
     * Gets a List of Strings with the Names of all Tables in DataBase.
     * @return list of string with table names
     * @throws SQLException if failed to retrieve info from db
     */
    public List<String> getSchemasNames() throws SQLException{
        List<String> tables;
        tables = managerDB.getTablesName();
        return tables;
    }

    public List<ContentPackage> getRowContentPacakgeList(int row){
        List<ContentPackage> res = new ArrayList<>();
        for(int i = 0; i < tableColumnList.size(); i++){
            String data = this.table.getModel().getValueAt(row, i).toString();

            res.add(new ContentPackage(i +1, data, tableColumnList.get(i), this.typeList.get(i)) );
        }
        return res;
    }

    /* PRIVATE METHODS */

    /**
     * Create a new model (DefaultTableModel) and sets column names
     */
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
     */
    private void setTableColumnList(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();

        int count = metaData.getColumnCount(); //number of column
        String[] columnName = new String[count];

        /* GetColumnLabel returns String with name of column of index i. It Starts From 1!*/
        this.tableColumnList.clear();
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
     */
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
}
