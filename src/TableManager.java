import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TableManager {
    private String tableName;
    private final List<String> tableColumnList;
    private JTable table;
    private final DBManagement managerDB;
    private DefaultTableModel model;

    public TableManager(JTable table, DBManagement connectDB) {
        this.tableName = null;
        this.tableColumnList = new ArrayList<>();
        this.table = table;
        this.managerDB = connectDB;
    }

    public TableManager(String tableName, List<String> tableColumn, JTable table, DBManagement connectDB) {
        this.tableName = tableName;
        this.tableColumnList = tableColumn;
        this.table = table;
        this.managerDB = connectDB;
    }

    public void setTable(String tableName) throws SQLException {
        this.tableName = tableName;
        try (ResultSet rSet = managerDB.executeSelect(new String[]{"*"}, this.tableName)) {
            setTableColumnList(rSet);
            formatTable();
            setTableRows(rSet);
        }
    }

    public List<String> getTableColumnNames() throws SQLException{
        List<String> tables;
        tables = managerDB.getTablesName();
        return tables;
    }

    /* PRIVATE METHODS */


    private void formatTable(){
        model = new DefaultTableModel();
        tableColumnList.forEach( n -> {
            model.addColumn(n);
        });
        this.table.setModel(model);
        model.fireTableDataChanged();
    }


    private void setTableColumnList(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int count = metaData.getColumnCount(); //number of column
        String[] columnName = new String[count];

        this.tableColumnList.clear();
        for (int i = 0; i < count; i++)
        {
            columnName[i] = metaData.getColumnLabel(i + 1);
            this.tableColumnList.add(columnName[i]);
        }
    }

    private void setTableRows(ResultSet rSet) throws SQLException {
        while(rSet.next()){
            List<String> row = new ArrayList<>();
            for(int i = 1; i <= tableColumnList.size(); i++){ //Indice riga ResultSet Parte da 1
                row.add(rSet.getString(i));
            }
            model.addRow(row.toArray());
        }
    }
}
