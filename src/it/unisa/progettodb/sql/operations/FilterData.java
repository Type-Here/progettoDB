package it.unisa.progettodb.sql.operations;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import it.unisa.progettodb.datacontrol.ContentPackage;
import it.unisa.progettodb.datacontrol.ContentWrap;
import it.unisa.progettodb.datacontrol.CustomDocFilter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.sql.JDBCType;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class FilterData {
    private final Component owner;
    private JPanel filterPanel;
    private ContentWrap contentWrap;
    private final ContentWrap original;
    private HashMap<String, List<JComponent>> userInputFields;
    private boolean isFiltered;
    private static boolean isChanged; //If any field changes its value reload original ContentWrap to re-filter data

    public FilterData(Component owner, ContentWrap contentWrap) {
        this.owner = owner;
        this.contentWrap = contentWrap;
        try {
            this.original = (ContentWrap) contentWrap.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        this.filterPanel = null;
        isChanged = false;
    }

    /**
     * Launch MainMethod createDialog with default value false
     * @return Filtered ContentWrap (Does Not update the Table)
     * @see FilterData#createDialog(boolean)
     */
    public ContentWrap createDialog(){
        return createDialog(false);
    }

    /**
     * Getter for isFiltered variable.
     * @return If filter is active returns true
     */
    public boolean isFiltered() {
        return isFiltered;
    }

    /**
     * Setter for isFiltered Value
     * @param filtered true if table is successfully filtered
     */
    public void setFiltered(boolean filtered) {
        isFiltered = filtered;
    }

    /**
     * Main Method: <br />
     * Launch a user panel to collect user input. <br />
     * Launch Filter Data using input collected. <br />
     * Return Filtered ContentWrap
     * @param isFilterActive boolean is true if table is already filtered, reuse old panel
     * @return Filtered ContentWrap (Does Not update the Table). If user cancel option or some error occurred it returns null
     */
    public ContentWrap createDialog(boolean isFilterActive){
        if(!isFilterActive || this.filterPanel == null) //check on null if method with true is called but new Filter Object is created instead
            this.userInputFields = createUserInputPanel(this.contentWrap.getMetaData());

        if( JOptionPane.showConfirmDialog(this.owner, this.filterPanel,
                        "Filtra Dati", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION ){
            filterData(userInputFields);
           // this.tableManager.setTable(this.contentWrap, this.tableManager.getTableName());
            return this.contentWrap;
        }
        return null;
    }

    /**
     * Effectively filter ContentWrap Data by user input constraints. <br />
     * If FilterData is reused it keeps track of precedent filter. <br />
     * If a filtered field is changed the original ContentWrap is cloned to filter new user input data. <br />
     * @param userInputFields list of jComponents containing JTextFields or DatePicker to choose from
     */
    private void filterData(HashMap<String, List<JComponent>> userInputFields) {
        if(isChanged){ //If any field changes its value reload original ContentWrap to re-filter data
            try {
                this.contentWrap = (ContentWrap) original.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
        //Get Values of Text and DatePicker fields set By User
        int i = -1;
        for (Map.Entry<String, List<JComponent>> e : userInputFields.entrySet()) {
            i++;
            String column = e.getKey();
            if (!column.equalsIgnoreCase(this.contentWrap.getMetaData().get(i).getColumnName()))
                throw new RuntimeException("FilterData has misaligned columns");

            /* If Text is set, get it and filter */
            if (e.getValue().get(0) instanceof JTextField t) {
                String userInput = t.getText();

                if (userInput == null || userInput.isEmpty()) continue;
                removeDataPerColumnText(i, userInput);

                /* If Date and at least 1 of the 2 DatePickerField is not empty Filter */
            } else if (e.getValue().get(0) instanceof DatePicker dFrom) {
                DatePicker dTo = (DatePicker) e.getValue().get(1);
                if (dFrom.getDate() == null && dTo.getDate() == null) continue;
                removeDataPerColumnDate(i, dFrom, dTo);
            }
        }
    }

    /**
     * Search into each row of data if #column = index has a date outside the range and removes that row. <br />
     * If a date is null no upper/lower limit is set.
     * @param index of the column to filter
     * @param dFrom Starting date
     * @param dTo Up to Date
     */
    private void removeDataPerColumnDate(int index, DatePicker dFrom, DatePicker dTo) {
        Iterator<Map.Entry<Integer, List<String>>> it = this.contentWrap.getRows().entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<Integer, List<String>> e = it.next();
            LocalDate dateRow = LocalDate.parse(e.getValue().get(index));
            if(dFrom.getDate() != null && dateRow.isBefore(dFrom.getDate())) it.remove();
            else if(dTo.getDate() != null && dateRow.isAfter(dTo.getDate())) it.remove();
        }
    }

    /**
     * Remove a row in data if index column does not 'contain' input substring (partial result are accepted)
     * @param index of the column to filter
     * @param userInput substring for compare
     */
    private void removeDataPerColumnText(int index, String userInput){
        this.contentWrap.getRows().entrySet().removeIf(e -> !e.getValue().get(index).toLowerCase().contains(userInput.toLowerCase()));
    }

    /**
     * Create a Panel for User to Insert a Search pattern for each column of the table. <br />
     * It sets the main panel internally and returns a hashmap containing only user interactive fields for each column. <br />
     * If column is a date shows 2 date picker (From - To). <br />
     * @param contentPackageList for metadata of each column
     * @return HashMap K:ColumnName - E:List of JComponent(s) to get Data after eventual User Confirm
     */
    private HashMap<String, List<JComponent>> createUserInputPanel(List<ContentPackage> contentPackageList){

        JPanel mainDialogPanel = new JPanel(new GridLayout(contentPackageList.size(), 2));
        mainDialogPanel.setFocusTraversalKeysEnabled(true);
        HashMap<String,List<JComponent>> fieldsPerColumn = new LinkedHashMap<>();

        for(ContentPackage content: contentPackageList) {
            List<JComponent> fields = new ArrayList<>();

            JDBCType typeSQL = content.getType();
            JPanel rowPanel = new JPanel();
            JLabel label = new JLabel(' ' + content.getColumnName() + ' ' + ':');
            rowPanel.add(label);

            //If Data is a Date
            if (content.getType().equals(JDBCType.DATE)) {
                JLabel fromLabel = new JLabel("Da");
                JLabel toLabel = new JLabel("A");
                DatePicker[] datePickers = getDatePickers();

                fields.add(datePickers[0]);
                fields.add(datePickers[1]);
                rowPanel.add(fromLabel);
                rowPanel.add(datePickers[0]);
                rowPanel.add(toLabel);
                rowPanel.add(datePickers[1]);

                /* THIS IS IMPLEMENTATION SPECIFIC - NO BOOLEAN IN OUR DB HAS TO BE SET BY USER */
            } else if (content.getType().equals(JDBCType.BIT) || content.getType().equals(JDBCType.BOOLEAN)) {
                continue;

            } else {
                JTextField textField = getjTextField(content, typeSQL);
                PlainDocument doc = (PlainDocument) textField.getDocument();
                doc.setDocumentFilter(new CustomDocFilter(content.getType(), content.getPrecision()));

                fields.add(textField);
                rowPanel.add(textField);
            }
            fieldsPerColumn.put(content.getColumnName(), fields);
            mainDialogPanel.add(rowPanel);
        }

        mainDialogPanel.setFocusTraversalKeysEnabled(true);
        this.filterPanel = mainDialogPanel;
        return fieldsPerColumn;
    }

    /**
     * Create a New JTextField (formatted) with InputVerifier based on content and typeSQL
     * @param content metadata for Precision (in ContentPackage)
     * @param typeSQL metadata for InputVerifier and Limit Input
     * @return a new JTextField (JFormattedTextField)
     */
    private static JTextField getjTextField(ContentPackage content, JDBCType typeSQL) {
        JTextField textField = new JFormattedTextField();

        //no fixed size to let partial search but in DocFilter max size is set
        textField.setInputVerifier(CustomDocFilter.getInputVerifierFixedSize(CustomDocFilter.NOFIXEDSIZE));

        textField.setPreferredSize(new Dimension(200, 20));

        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {isChanged = true;}
            @Override
            public void removeUpdate(DocumentEvent e) {isChanged = true;}
            @Override
            public void changedUpdate(DocumentEvent e) {isChanged = true;}
        });
        return textField;
    }

    /**
     * Set 2 DatePicker (From date - To date) and their listeners to set Veto Policy
     * @return DatePicker[] with 2 element: DatePicker-FromDate, DatePicker-ToDate
     */
    private static DatePicker[] getDatePickers() {
        DatePicker[] res = new DatePicker[2];

        DatePickerSettings datePickerSettingsFrom = new DatePickerSettings(Locale.ITALY);
        DatePickerSettings datePickerSettingsTo = new DatePickerSettings(Locale.ITALY);

        datePickerSettingsFrom.setFirstDayOfWeek(DayOfWeek.MONDAY);
        datePickerSettingsTo.setFirstDayOfWeek(DayOfWeek.MONDAY);

        DatePicker datePickerFrom = new DatePicker(datePickerSettingsFrom);
        DatePicker datePickerTo = new DatePicker(datePickerSettingsTo);

        //Listeners
        datePickerFrom.addDateChangeListener(e ->{
            LocalDate d = datePickerFrom.getDate();
            datePickerSettingsTo.setDateRangeLimits(d, null);

            /*Limit To Date based on chosen From*/
            LocalDate toDate = datePickerTo.getDate();
            if(toDate != null && toDate.isBefore(d)) datePickerTo.setDate(null);
            isChanged = true;
        });

        datePickerTo.addDateChangeListener(e ->{
            LocalDate d = datePickerTo.getDate();
            datePickerSettingsFrom.setDateRangeLimits(null, d);

            /*Limit From Date based on chosen TO*/
            LocalDate FromDate = datePickerFrom.getDate();
            if(FromDate != null && FromDate.isAfter(d)) datePickerFrom.setDate(null);
            isChanged = true;
        });

        res[0] = datePickerFrom;
        res[1] = datePickerTo;

        return res;
    }
}
