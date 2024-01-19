package it.unisa.progettodb.dialogs;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import it.unisa.progettodb.datacontrol.ContentPackage;
import it.unisa.progettodb.datacontrol.CustomDocFilter;

import javax.swing.*;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.sql.JDBCType;
import java.time.DayOfWeek;
import java.util.*;
import java.util.List;

/**
 * Basic Use of <b>Composition over Inheritance</b> for Swing Components.
 * An Object is only created in createUserInputPanel Static Method. <br />
 * A JPanel is instantiated as a variable of this class object inside createUserInputPanel.
 * This way we can obtain 2 major advantages. <br /><br />
 * 1. EDT automatically take cares of updated panel. <br />
 *    An extended class wouldn't work. It would need for each update inside the panel of SwingUtilities.invokeLater()
 *    and would have required extra method call or bad design code in other classes with bad cohesion and coupling. <br /><br />
 * 2. We can keep trace of all input JComponents (FieldTexts and DatePickers for now) with 'fields' HashMap.
 *    In this way it's possible to avoid cycling through panel.getComponents() to get data when user confirm data in a JOptionPane Dialog. <br />
 *    NB. This need a rewrite of some classes. At the moment only, MenuAction methods fully use this new implementation.
 * @see #createUserInputPanel(List)
 */

public class UserPanelDialog {
    private JPanel panel;
    private final HashMap<String, JComponent> fields;

    /**
     * Private Constructor so it is needed createUserInputPanel to Create it
     * @see it.unisa.progettodb.dialogs.UserPanelDialog#createUserInputPanel(List);
     */
    private UserPanelDialog() {
        this.fields = new LinkedHashMap<>();
        this.panel = null;
    }

    /**
     * Return User Input Dialog JPanel.
     * @return JPanel main
     */
    public JPanel getPanel() {
        return panel;
    }

    /**
     * Adds a Field in 'this' hashmap
     * @param name Name of Column for which input component is created for (Used as Key)
     * @param field JTextField or DatePicker to Get Data From
     */
    private void addField(String name, JComponent field){
        this.fields.put(name, field);
    }

    /**
     * Return an HashMap with ColumnName And UserInput extracted from JComponents
     * @return HashMap K:Name of Column - E: Object (Strings) with extracted data (May be null);
     */
    public HashMap<String, Object> getTexts(){
        HashMap<String, Object> res = new HashMap<>();
        for(Map.Entry<String,JComponent> e : this.fields.entrySet()){
               if(e.getValue() instanceof JTextField t){
                   res.put(e.getKey(), t.getText().trim());
               } else if(e.getValue() instanceof DatePicker d){
                   res.put(e.getKey(), d.getDate() == null ? null : d.getDate().toString()); //May be NULL
               }
        }
        return res;
    }


    /**
     * Create a GridLayout Panel. <br />
     * Static Method For Creating User Panels from List of Content Packages <br />
     * Set Up the MainDialogPanel. <br />
     * Each Row Has Label and UserInput Field; <br />
     * Each Row Will Fill Data for 1 Attribute of the new Instance. <br />
     * Use DocumentFilter and inputValidator to Check On Input User.
     * @param contentPackageList containing only MetaData (index, columnName, precision, isNullable, JDBCType)
     */
    public static UserPanelDialog createUserInputPanel(List<ContentPackage> contentPackageList){
        UserPanelDialog main = new UserPanelDialog();
        /* We assign here a new JPanel so EDT can automatically detect updates.
         * If the panel was created in constructor instead it wouldn't have worked as well (Tested)*/
        JPanel mainDialogPanel = new JPanel(new GridLayout(contentPackageList.size(), 2));
        main.panel = mainDialogPanel;

        /*For Each Column (Represented by a ContentPackage element) get its Input Panel (text fields of date pickers)*/
        for(ContentPackage content: contentPackageList) {

            JDBCType typeSQL = content.getType();
            JPanel rowPanel = new JPanel();
            JLabel label = new JLabel("Insert " + content.getColumnName() + ' '
                    + typeSQL + ':'
                    + (content.isNullable() ? ' ' : '*'));
            rowPanel.add(label);

            JFormattedTextField textField;
            DatePicker datePicker;

            //If Data is a Date
            if (content.getType().equals(JDBCType.DATE)) {
                DatePickerSettings datePickerSettings = new DatePickerSettings(Locale.ITALY);
                datePickerSettings.setFirstDayOfWeek(DayOfWeek.MONDAY);
                datePicker = new DatePicker(datePickerSettings);

                rowPanel.add(datePicker);
                main.addField(content.getColumnName(), datePicker);

                /* THIS IS IMPLEMENTATION SPECIFIC - NO BOOLEAN IN OUR DB HAS TO BE SET BY USER */
            } else if (content.getType().equals(JDBCType.BIT) || content.getType().equals(JDBCType.BOOLEAN)) {
                continue;

            } else {
                textField = new JFormattedTextField();

                //CHAR in MySQL has a Fixed size, so Precision is used to set TextSize
                if (typeSQL.equals(JDBCType.CHAR)) {
                    textField.setInputVerifier(CustomDocFilter.getInputVerifierFixedSize(content.getPrecision()));
                } else {
                    textField.setInputVerifier(CustomDocFilter.getInputVerifierFixedSize(CustomDocFilter.NOFIXEDSIZE));
                }

                PlainDocument doc = (PlainDocument) textField.getDocument();
                doc.setDocumentFilter(new CustomDocFilter(content.getType(), content.getPrecision()));

                textField.setPreferredSize(new Dimension(200, 20));

                rowPanel.add(textField);
                main.addField(content.getColumnName(), textField);
            }

            //mainDialog.add(rowPanel);
            mainDialogPanel.add(rowPanel);
        }

        mainDialogPanel.setFocusTraversalKeysEnabled(true);

        return main;
    }

}
