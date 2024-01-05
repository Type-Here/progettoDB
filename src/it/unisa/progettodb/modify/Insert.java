package it.unisa.progettodb.modify;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import it.unisa.progettodb.DBManagement;
import it.unisa.progettodb.datacontrol.ContentChecker;
import it.unisa.progettodb.datacontrol.ContentPackage;
import it.unisa.progettodb.exceptions.InvalidTableSelectException;
import it.unisa.progettodb.exceptions.NullTableException;
import it.unisa.progettodb.exceptions.ValidatorException;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.sql.JDBCType;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.util.*;
import java.util.List;

public class Insert extends JOptionPane implements DataManipulation{
    private final DBManagement managerDB;
    private final String workingTable;
    private final Component owner;
    private JPanel mainDialogPanel;
    private List<ContentPackage> contentPackageList;

    public Insert(Component owner, DBManagement managerDB, String workingTable) {
        super(null, JOptionPane.INFORMATION_MESSAGE, JOptionPane.OK_CANCEL_OPTION );
        this.managerDB = managerDB;
        this.workingTable = workingTable;
        this.owner = owner;
        createDialog();
    }

    /**
     * MAIN CONTROL METHOD:
     * Create Dialog for Insertion in Table.
     * Check if table is not null or is Not Possible to Insert (ex a view).
     * Fetch Data with managerDB.fetchMetaData and .fetchDataType
     * Then Call launchCheckThenDialog to confirm data.
     * Then if all ok call sendDataToInsert() to execute query by managerDB.
     */
    @Override
    public void createDialog(){
        List<JDBCType> dataType;
        ResultSetMetaData metaData;

        try {
            if(this.workingTable == null) throw new NullTableException();
            if(!isInsertionAble(this.workingTable)) throw new InvalidTableSelectException();

            /*Fetch MetaData*/
            metaData = managerDB.fetchMetaData(this.workingTable);
            dataType = managerDB.fetchDataType(this.workingTable);

            /*Open Main Dialog for USer Input*/
            HashMap<Integer, JDBCType> insertDataIndexType = setPanel(metaData, dataType);
            int result = JOptionPane.showConfirmDialog(this.owner, mainDialogPanel, "Insert Data in Table", this.optionType, this.messageType);

            if(result == JOptionPane.OK_OPTION) { //Else User Pressed Cancel so Return

                /*Validate Data then Open Confirmation Dialog*/
                contentPackageList = formatAndValidateData(metaData, insertDataIndexType);

                //If Data is Valid Show Message Confirm Box. So SUer Can Confirm
                if(finalCheckDialog(ContentPackage.returnDataMapAsString(contentPackageList)) == JOptionPane.OK_OPTION){
                    /*Final Content, if not null (operation annulled by user, then send data to managerDB to Insert */
                    if (contentPackageList != null) sendDataToInsert();
                }


            }
        } catch (SQLException e) {
            throw new RuntimeException(e);

        } catch (NullTableException | InvalidTableSelectException e){
            JOptionPane.showMessageDialog(this.owner, e.getMessage() + "\nSeleziona (altra) Tabella e Riprova",
                                        "Attenzione", JOptionPane.ERROR_MESSAGE);

        } catch (ValidatorException e) {
            JOptionPane.showMessageDialog(this.mainDialogPanel, "Data Not Valid: \n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Set Up the MainDialogPanel with each row a filed to insert data
     * @param metaData Contains metadata of Table to set Precision, Column Name ecc.
     * @param dataType List of JBDC Type, 1 for each column containing type (int, double...) of each column
     * @throws SQLException if getMetaData fails
     */
    private  HashMap<Integer, JDBCType> setPanel(ResultSetMetaData metaData, List<JDBCType> dataType) throws SQLException {

        //Save Index of MetaData Column that will be visible in Dialog Pane.
        HashMap<Integer, JDBCType> insertDataIndexType = new HashMap<>();

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

            //CHAR in MySQL has a Fixed size, so Precision is used to set TextSize viw MaskFormatter
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

                //If Data is a Date
            } else if(t.equals(JDBCType.DATE)) {
                DatePickerSettings datePickerSettings = new DatePickerSettings(Locale.ITALY);
                datePickerSettings.setFirstDayOfWeek(DayOfWeek.MONDAY);
                datePicker = new DatePicker(datePickerSettings);

                rowPanel.add(datePicker);

            } else if (isNumber(t)) {

                textField = new JFormattedTextField();
                textField.setPreferredSize(new Dimension(200, 20));

                rowPanel.add(textField);

                /* THIS IS IMPLEMENTATION SPECIFIC - NO BOOLEAN IN OUR DB HAS TO BE SET BY USER */
            } else if(t.equals(JDBCType.BIT) || t.equals(JDBCType.BOOLEAN)) {
                continue;

            } else { //Es VARCHAR

                textField = new JFormattedTextField();
                textField.setPreferredSize(new Dimension(200, 20));

                rowPanel.add(textField);
            }

            insertDataIndexType.put(i, t);

            mainDialogPanel.add(rowPanel);
            mainDialogPanel.setFocusTraversalKeysEnabled(true);
            ++i;
        }

        return insertDataIndexType;
    }

    /**
     * After MainPanelDialog.
     * Before is performed a validation for some of more sensible data (IDs like Targa, Matricola ecc)
     *
     * @param metaData            metaData of the table
     * @param insertDataIndexType HashMap list of indexes of the column used in main dialog (not all columns needs insertion) - JDBCType OF Column
     * @return List of ContentPackage each containing Index, JDBCType, User Inserted Data (String), Column Name.
     */
    private List<ContentPackage> formatAndValidateData(ResultSetMetaData metaData, HashMap<Integer, JDBCType> insertDataIndexType) throws SQLException, ValidatorException {
        Component[] c = mainDialogPanel.getComponents();

        List<String> newData = new ArrayList<>();
        List<ContentPackage> contentPackageList = new ArrayList<>();

        /* Get Data Inserted in Previous Dialog and Add it in a List*/
        for(Component component : c){
            if (component instanceof JPanel internal){
                Component[] internalComp = internal.getComponents();

                for(Component comp : internalComp){

                    if(comp instanceof JFormattedTextField f){
                        newData.add(f.getText());
                    } else if(comp instanceof DatePicker d){
                        newData.add(d.getDate().toString());
                    }
                }
            } else {
                throw new RuntimeException("Something getting info gone wrong");
            }
        }

        if(newData.isEmpty()) throw new RuntimeException("Error Data is Empty");

        /*FORMAT DATA in Content Package*/

        /* Format
         * - From HashMap - Key: Index of column -  Value: JDBC Type
         * - From medaData for ColumnName
         * - From newData list containing data inserted by user in string format
         * Saved in ContentPackage Object
         * Stored in List contentePackageList */
        for(Map.Entry<Integer,JDBCType> e : insertDataIndexType.entrySet()){
            int index = e.getKey();
            ContentPackage content = new ContentPackage(index, newData.get(index - 1),
                                            metaData.getColumnName(index), e.getValue() );
            contentPackageList.add(content);
        }

        System.out.println(contentPackageList);

        /*DATA CHECK*/

        /*Automatic Validation of Data*/
        if(ContentChecker.checker(ContentPackage.returnDataMapAsString(contentPackageList), this.workingTable))
            return contentPackageList;
        throw new RuntimeException("Something Went Wrong");
    }

    /**
     * New Dialog to verify data inserted before actually sending it.
     * Set Second Panel before Finalizing Insert Query
     * Let User Control if Data is Correct
     * @param data hashmap in string, string format to print (K:column name, E:data)
     * @return JOptionPane.OK_OPTION value if user confirm, CANCEL otherwise
     */
    private int finalCheckDialog(HashMap<String, String> data){
        JScrollPane scrollPane = new JScrollPane( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(300, 200));

        JTextArea textArea = new JTextArea();
        textArea.setPreferredSize(new Dimension(250, 150));
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
        textArea.setText("Dati Inseriti: \n\n");

        for(Map.Entry<String, String> e : data.entrySet()){
            textArea.append(' ' + e.getKey() + ": " + e.getValue() + " \n");
        }
        textArea.append("\nConfermi? \n");
        scrollPane.setViewportView(textArea);

        return JOptionPane.showConfirmDialog(this.mainDialogPanel, scrollPane, "Conferma Dati",
                                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    }


    /**
     * Last Method To Be Called.
     * Send Data To Insertion. Dialog if Fails.
     */
    private void sendDataToInsert() {

        try {
            managerDB.executeInsert(ContentPackage.returnDataForQuery(this.contentPackageList), this.workingTable);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this.mainDialogPanel, "Error: \n" + e.getMessage(), "Errore SQL", JOptionPane.ERROR_MESSAGE);
        }
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


    /**
     * Check if Table can receive an insertion. (Ex You can't insert into a view)
     * @param tableName Table to Check
     * @return true if insertable, false if not
     * @throws SQLException if check table is a view fails
     */
    public boolean isInsertionAble(String tableName) throws SQLException {
        return (! managerDB.isAView(tableName) );

        //return ( tableName.equalsIgnoreCase("dipendentiView") || tableName.toLowerCase().contains("view") );
    }

}
