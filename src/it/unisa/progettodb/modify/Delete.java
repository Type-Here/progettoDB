package it.unisa.progettodb.modify;
import it.unisa.progettodb.sql.DBManagement;
import it.unisa.progettodb.datacontrol.ContentPackage;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class Delete implements DataManipulation {
    private final DBManagement managerDB;
    private final String workingTable;
    private final Component owner;
    private final List<ContentPackage> contentPackageList;

    public Delete(Component owner, DBManagement managerDB, String workingTable, List<ContentPackage> contentPackageList) {
        this.managerDB = managerDB;
        this.workingTable = workingTable;
        this.owner = owner;
        this.contentPackageList = contentPackageList;
    }

    /**
     * Main Control For Delete:
     * - Show Dialog For Confirmation Delete
     * - Retrieve Primary Keys for Table.
     * - Send Delete Query to managerDB.
     */
    @Override
    public boolean createDialog()  {
        if(checkDialog(ContentPackage.returnDataMapAsString(this.contentPackageList)) == JOptionPane.OK_OPTION){
            try {
                HashMap<String,Integer> primaryKeys = managerDB.retrievePrimaryKeys(this.workingTable);

                List<ContentPackage> filteredKeys = new ArrayList<>();
                //primaryKeys.forEach((key, value) -> System.out.println("Primary Key is" + key + "-Seq=" + value)); //DEBUG

                /* Get Only Primary Key attributes and their values from All Data */
                for(Map.Entry<String, Integer> e: primaryKeys.entrySet()){
                    ContentPackage c = this.contentPackageList.stream()
                            .filter(content -> e.getKey().equalsIgnoreCase(content.getColumnName()))
                            .findFirst().orElseThrow();
                    filteredKeys.add(c);
                }

                System.out.println("Filtered: " + filteredKeys);

                /*MAIN QUERY EXECUTION - DELETE*/
                managerDB.executeDelete(ContentPackage.returnDataForQuery(filteredKeys), this.workingTable);

                return true;

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this.owner, "Errore SQL in Eliminazione Dato: \n" + e.getMessage(),
                        "Error Delete", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException(e);

            } catch (NoSuchElementException e){ //Thrown by Stream filter
                JOptionPane.showMessageDialog(this.owner, "Errore Selezione Dati: \nImpossibile Individuare Chiavi Primarie da Elemento",
                        "Error Selection", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException("Missing Data From Selected Row. \nImpossible to Select Element by primary key");
            }
        }
        return false;
    }

    /**
     * Dialog to verify data that will be Deleted before actually deleting it.
     * @param data hashmap in string, string format to print (K:column name, E:data)
     * @return JOptionPane.OK_OPTION value if user confirm, CANCEL otherwise
     */
    private int checkDialog(HashMap<String, String> data){
        JScrollPane scrollPane = new JScrollPane( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(300, 200));

        JTextArea textArea = new JTextArea();
        textArea.setPreferredSize(new Dimension(250, 150));
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
        textArea.setText("Dati Selezionati per Eliminazione: \n\n");

        for(Map.Entry<String, String> e : data.entrySet()){
            textArea.append(' ' + e.getKey() + ": " + e.getValue() + " \n");
        }
        textArea.append("\nConfermi? \n");
        scrollPane.setViewportView(textArea);

        return JOptionPane.showConfirmDialog(this.owner, scrollPane, "Conferma Eliminazione",
                JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
    }


    /* THIS CHECK IS DATABASE SPECIFIC */

    /**
     * Check If a specific table accepts delete query. (i.e. is a View don't delete there or business rules)
     * @param tableName table to check
     * @return true if data is deletable, false if not
     */
    public static boolean isDeletable(String tableName){
        if(tableName.equalsIgnoreCase("dipendente") ||
                tableName.equalsIgnoreCase("categoria")) return true; //For Testing Purposes!!!!

        return tableName.equalsIgnoreCase("consegna") || tableName.equalsIgnoreCase("dipendentiview");
    }
}
