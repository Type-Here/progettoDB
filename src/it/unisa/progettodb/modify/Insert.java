package it.unisa.progettodb.modify;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import it.unisa.progettodb.DBManagement;
import it.unisa.progettodb.exceptions.InvalidTableSelectException;
import it.unisa.progettodb.exceptions.NullTableException;
import it.unisa.progettodb.exceptions.ValidatorException;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Insert extends JOptionPane implements DataManipulation{
    private final DBManagement managerDB;
    private final String workingTable;
    private final Component owner;
    private JPanel mainDialogPanel;
    public Insert(Component owner, DBManagement managerDB, String workingTable) {
        super(null, JOptionPane.INFORMATION_MESSAGE, JOptionPane.OK_CANCEL_OPTION );
        this.managerDB = managerDB;
        this.workingTable = workingTable;
        this.owner = owner;
        createDialog();
    }

    /**
     * Create Dialog for Insertion in Table.
     * Check if table is not null or is Not Possible to Insert (ex a view).
     * Fetch Data with managerDB.fetchMetaData and .fetchDataType
     */
    @Override
    public void createDialog(){
        List<JDBCType> dataType;
        ResultSetMetaData metaData;

        try {
            if(this.workingTable == null) throw new NullTableException();
            if(!isInsertionAble(this.workingTable)) throw new InvalidTableSelectException();

            metaData = managerDB.fetchMetaData(this.workingTable);
            dataType = managerDB.fetchDataType(this.workingTable);

            List<Integer> insertDataIndex = setPanel(metaData, dataType);
            int result = JOptionPane.showConfirmDialog(this.owner, mainDialogPanel, "Insert Data in Table", this.optionType, this.messageType);
            if(result == JOptionPane.OK_OPTION){
                launchCheckDialog(metaData, insertDataIndex);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NullTableException | InvalidTableSelectException e){
            JOptionPane.showMessageDialog(this.owner, e.getMessage() + "\nSeleziona (altra) Tabella e Riprova",
                                        "Attenzione", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Set Up the MainDialogPanel with each row a filed to insert data
     * @param metaData Contains metadata of Table to set Precision, Column Name ecc.
     * @param dataType List of JBDC Type, 1 for each column containing type (int, double...) of each column
     * @throws SQLException if getMetaData fails
     */
    private List<Integer> setPanel(ResultSetMetaData metaData, List<JDBCType> dataType) throws SQLException {

        //Save Index of MetaData Column that will be visible in Dialog Pane.
        List<Integer> insertDataIndex = new ArrayList<>();

        mainDialogPanel = new JPanel(new GridLayout(dataType.size(), 2));
        int i = 1;

        for(JDBCType t: dataType){
            JPanel rowPanel = new JPanel();
            JLabel label = new JLabel("Insert " + metaData.getColumnName(i) + ' '
                                + t.getName().toLowerCase() + ':'
                                + (metaData.isNullable(i) == ResultSetMetaData.columnNullable ? ' ' : '*') );
            rowPanel.add(label);

            JFormattedTextField textField;
            DatePicker datePicker;

            if(t.equals(JDBCType.CHAR)) {
                StringBuilder format = new StringBuilder();
                format.append("*".repeat(Math.max(0, metaData.getPrecision(i))));

                MaskFormatter formatter; //with however many characters you need
                try {
                    formatter = new MaskFormatter(format.toString());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                formatter.setValidCharacters("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-"); // whatever characters you would use

                textField = new JFormattedTextField(formatter);
                textField.setPreferredSize(new Dimension(200, 20));
                rowPanel.add(textField);

            } else if(t.equals(JDBCType.DATE)) {
                DatePickerSettings datePickerSettings = new DatePickerSettings(Locale.ITALY);
                datePickerSettings.setFirstDayOfWeek(DayOfWeek.MONDAY);
                datePicker = new DatePicker(datePickerSettings);

                rowPanel.add(datePicker);

            } else if (isNumber(t)) {
                StringBuilder format = new StringBuilder();
                format.append("*".repeat(Math.max(0, metaData.getPrecision(i))));

                MaskFormatter formatter; //with however many characters you need
                try {
                    formatter = new MaskFormatter(format.toString());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                formatter.setValidCharacters("0123456789."); // whatever characters you would use

                textField = new JFormattedTextField(formatter);
                textField.setPreferredSize(new Dimension(200, 20));

                rowPanel.add(textField);
            } else if(t.equals(JDBCType.BIT) || t.equals(JDBCType.BOOLEAN)) {
                continue;

            } else {

                textField = new JFormattedTextField();
                textField.setPreferredSize(new Dimension(200, 20));

                rowPanel.add(textField);
            }
            insertDataIndex.add(i);
            mainDialogPanel.add(rowPanel);
            mainDialogPanel.setFocusTraversalKeysEnabled(true);
            ++i;
        }
        return insertDataIndex;
    }


    private void launchCheckDialog(ResultSetMetaData metaData, List<Integer> insertDataIndex) {
        Component[] c = mainDialogPanel.getComponents();
        HashMap<String, String> values = new HashMap<>();

        /*TODO*/

        for(Component comp : c){
            if(comp instanceof JFormattedTextField f){
                f.getText();
            } else if(comp instanceof DatePicker d){
                d.getText();
            }
        }
        JOptionPane.showMessageDialog(mainDialogPanel, "Sono qui");
    }

    /**
     * Check if Data Type is a numerical value (int. double, float ...)
     * @param type JDBC Type to check
     * @return true if datatype is numerical, false otherwise
     */
    private boolean isNumber(JDBCType type){
        return (type.equals(JDBCType.BIGINT) || type.equals(JDBCType.DOUBLE)
                || type.equals(JDBCType.FLOAT) || type.equals(JDBCType.INTEGER) || type.equals(JDBCType.DECIMAL));
    }

    private boolean isInsertionAble(String tableName) throws SQLException {
        return (! managerDB.isAView(tableName) );

        //return ( tableName.equalsIgnoreCase("dipendentiView") || tableName.toLowerCase().contains("view") );
    }
}
