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
import java.util.List;
import java.util.Locale;

public class UserPanelDialog {
    /**
     * Static Method For Creating User Panels from List of Content Packages <br />
     * Set Up the MainDialogPanel. <br />
     * Each Row Has Label and UserInput Field; <br />
     * Each Row Will Fill Data for 1 Attribute of the new Instance. <br />
     * Use DocumentFilter and inputValidator to Check On Input User.
     * @param contentPackageList containing only MetaData (index, columnName, precision, isNullable, JDBCType)
     */
    public static JPanel createUserInputPanel(List<ContentPackage> contentPackageList){
        JPanel mainDialogPanel = new JPanel(new GridLayout(contentPackageList.size(), 2));

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

            }

            mainDialogPanel.add(rowPanel);
        }

        mainDialogPanel.setFocusTraversalKeysEnabled(true);

        return mainDialogPanel;
    }

}
