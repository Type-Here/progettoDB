package it.unisa.progettodb.modify;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import it.unisa.progettodb.DBManagement;
import it.unisa.progettodb.exceptions.NullTableException;
import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.sql.JDBCType;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.DayOfWeek;
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

    @Override
    public void createDialog(){
        List<JDBCType> dataType;
        ResultSetMetaData metaData;
        /*TODO*/
        try {
            metaData = managerDB.fetchMetaData(this.workingTable);
            dataType = managerDB.fetchDataType(this.workingTable);
            setPanel(metaData, dataType);
            JOptionPane.showConfirmDialog(this.owner, mainDialogPanel, "Insert Data in Table", this.optionType, this.messageType);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NullTableException e){
            JOptionPane.showMessageDialog(this.owner, e.getMessage() + "\nSeleziona Tabella e Riprova",
                                        "Attenzione", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setPanel(ResultSetMetaData metaData, List<JDBCType> dataType) throws SQLException {
        /*TODO*/
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

            } else {

                textField = new JFormattedTextField();
                textField.setPreferredSize(new Dimension(200, 20));

                rowPanel.add(textField);
            }

            mainDialogPanel.add(rowPanel);
            mainDialogPanel.setFocusTraversalKeysEnabled(true);
            ++i;
        }
    }


    private boolean isNumber(JDBCType type){
        return (type.equals(JDBCType.BIGINT) || type.equals(JDBCType.DOUBLE)
                || type.equals(JDBCType.FLOAT) || type.equals(JDBCType.INTEGER) || type.equals(JDBCType.DECIMAL));
    }
}
