package it.unisa.progettodb.operations;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import it.unisa.progettodb.TableManager;
import it.unisa.progettodb.datacontrol.ContentPackage;
import it.unisa.progettodb.datacontrol.ContentWrap;
import it.unisa.progettodb.datacontrol.CustomDocFilter;

import javax.swing.*;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.sql.JDBCType;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class FilterData {
    private final TableManager tableManager;
    private final Component owner;
    private JPanel filterPanel;
    private final ContentWrap contentWrap;

    public FilterData(Component owner, TableManager tableManager) {
        this.tableManager = tableManager;
        this.owner = owner;
        this.contentWrap = tableManager.getWrapData();
    }

    public boolean createDialog(){
        HashMap<String, List<JComponent>> userInputFields = createUserInputPanel(this.contentWrap.getMetaData());
        if( JOptionPane.showConfirmDialog(this.owner, this.filterPanel,
                        "Filtra Dati", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION ){
            filterData(userInputFields);
            this.tableManager.setTable(this.contentWrap, this.tableManager.getTableName());
            return true;
        }
        return false;
    }

    private void filterData(HashMap<String, List<JComponent>> userInputFields) {
        int i = -1;
        for (Map.Entry<String, List<JComponent>> e : userInputFields.entrySet()) {
            i++;
            String column = e.getKey();
            if (!column.equalsIgnoreCase(this.contentWrap.getMetaData().get(i).getColumnName()))
                throw new RuntimeException("FilterData has misaligned columns ");

            /* If Text get it and filter if not empty */
            if (e.getValue().get(0) instanceof JTextField t) {
                String userInput = t.getText();
                System.out.println("Input: " + userInput);
                if (userInput == null || userInput.isEmpty()) continue;
                removeDataPerColumnText(i, userInput);

                /* If Date and 1 of 2 DatePickerField is not empty Filter */
            } else if (e.getValue().get(0) instanceof DatePicker dFrom) {
                DatePicker dTo = (DatePicker) e.getValue().get(1);
                if (dFrom.getDate() == null && dTo.getDate() == null) continue;
                removeDataPerColumnDate(i, dFrom, dTo);
            }
        }
    }

    private void removeDataPerColumnDate(int index, DatePicker dFrom, DatePicker dTo) {
        Iterator<Map.Entry<Integer, List<String>>> it = this.contentWrap.getRows().entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<Integer, List<String>> e = it.next();
            LocalDate dateRow = LocalDate.parse(e.getValue().get(index));
            if(dFrom.getDate() != null && dateRow.isBefore(dFrom.getDate())) it.remove();
            else if(dTo.getDate() != null && dateRow.isAfter(dTo.getDate())) it.remove();
        }
    }


    private void removeDataPerColumnText(int index, String userInput){
        this.contentWrap.getRows().entrySet().removeIf(e -> !e.getValue().get(index).toLowerCase().contains(userInput.toLowerCase()));
    }

    /**
     * Create a Panel for User to Insert a Search pattern for each column of the table. <br />
     * If column is a date shows 2 date picker (From - To). <br />
     * @param contentPackageList for metadata of each column
     * @return HashMap K:ColumnName - E:List of JComponent(s) to get Data after eventual User Confirm
     */
    private HashMap<String, List<JComponent>> createUserInputPanel(List<ContentPackage> contentPackageList){
        JPanel mainDialogPanel = new JPanel(new GridLayout(contentPackageList.size(), 2));
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
        });

        datePickerTo.addDateChangeListener(e ->{
            LocalDate d = datePickerTo.getDate();
            datePickerSettingsFrom.setDateRangeLimits(null, d);

            /*Limit From Date based on chosen TO*/
            LocalDate FromDate = datePickerFrom.getDate();
            if(FromDate != null && FromDate.isAfter(d)) datePickerFrom.setDate(null);
        });

        res[0] = datePickerFrom;
        res[1] = datePickerTo;

        return res;
    }
}
