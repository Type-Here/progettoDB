package it.unisa.progettodb.modify;

import com.github.lgooddatepicker.components.DatePicker;
import it.unisa.progettodb.dialogs.UserPanelDialog;
import it.unisa.progettodb.exceptions.InvalidTableSelectException;
import it.unisa.progettodb.exceptions.NullTableException;
import it.unisa.progettodb.exceptions.ValidatorException;
import it.unisa.progettodb.sql.DBManagement;
import it.unisa.progettodb.datacontrol.ContentPackage;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class Update extends JOptionPane implements DataManipulation{

    private final DBManagement managerDB;
    private final String workingTable;
    private final Component owner;
    private JPanel mainDialogPanel;
    private final List<ContentPackage> contentPackageList; //Old Data

    public Update(Component owner, DBManagement managerDB, String workingTable, List<ContentPackage> contentPackageList) {
        super(null, JOptionPane.INFORMATION_MESSAGE, JOptionPane.OK_CANCEL_OPTION );
        this.managerDB = managerDB;
        this.workingTable = workingTable;
        this.owner = owner;
        this.contentPackageList = contentPackageList;
    }

    /**
     * MAIN CONTROL METHOD: <br />
     * Create Dialog for Updating Data.
     * Check if table is not null or is Not Possible to Update (i.e. a view). <br />
     * - Get List of Content Package With makeEmptyContentPackage for managerDB. <br />
     * - Then Call launchCheckThenDialog to confirm data. <br />
     * - Then if all ok call sendDataToUpdate() to execute query by managerDB. <br />
     * @return true if data in table are modified, false if not.
     * This is not an error check because will return false even if user cancel operation!
     */
    @Override
    public boolean createDialog() {
        try {
            /* Checks Working Table */
            if (this.workingTable == null) throw new NullTableException();
            if (!isUpdatable(this.workingTable)) throw new InvalidTableSelectException();

            /*Saves Primary Key Values In Another List*/
            HashMap<String,Integer> primaryKeyName = managerDB.retrievePrimaryKeys(this.workingTable);

            List<ContentPackage> primaryKeyData = new ArrayList<>();
            //primaryKeyName.forEach((key, value) -> System.out.println("Primary Key is" + key + "-Seq=" + value)); //DEBUG

            /* Get Only Primary Key attributes and their values from All Data */
            for(Map.Entry<String, Integer> e: primaryKeyName.entrySet()){
                ContentPackage c = this.contentPackageList.stream()
                        .filter(content -> e.getKey().equalsIgnoreCase(content.getColumnName()))
                        .findFirst().orElseThrow();
                primaryKeyData.add(c);
            }

            /* DATABASE SPECIFIC */
            /* Removes Other Data That User Cannot Modify (derived attribute, ...) */
            DataManipulation.removeNonUserModifyAbleData(contentPackageList, this.workingTable);

            //NB: Primary Key Values Won't Be Remove They Will Grayed-Out for Better View
            /*Create JPanel*/
            setPanel(contentPackageList, primaryKeyName);
            /* Show First Dialog */
            int result = JOptionPane.showConfirmDialog(this.owner, mainDialogPanel, "Insert Data in Table",
                                                        this.optionType, this.messageType);
            if(result == JOptionPane.OK_OPTION){

                List<ContentPackage> emptyData = managerDB.makeEmptyContentPackage(this.workingTable);
                DataManipulation.removeNonUserModifyAbleData(emptyData, this.workingTable);

                List<ContentPackage> updateData = validateData(emptyData, this.contentPackageList);
                int res = finalCheckDialog(ContentPackage.returnDataMapAsString(this.contentPackageList),
                                            ContentPackage.returnDataMapAsString(updateData));
                if(res == JOptionPane.OK_OPTION){

                    /*MAIN ACTION: Execute Update */
                    this.managerDB.executeUpdate(ContentPackage.returnDataForQuery(updateData),
                                                this.workingTable,
                                                ContentPackage.returnDataForQuery(primaryKeyData));
                    return true;
                }
            }
            return false;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this.owner, "Errore SQL: \n" + e.getMessage() + '\n',
                    "Errore", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);

        } catch (NullTableException | InvalidTableSelectException e){
            JOptionPane.showMessageDialog(this.owner, e.getMessage() + "\nSeleziona (altra) Tabella e Riprova",
                    "Attenzione", JOptionPane.ERROR_MESSAGE);

        } catch (ValidatorException e) {
            JOptionPane.showMessageDialog(this.mainDialogPanel, "Data Not Valid: \n" + e.getMessage(),
                    "Warning", JOptionPane.ERROR_MESSAGE);
        }

        return false;
    }

    /**
     * This Method uses UserPanelDialog.createUserInputPanel to create a JDialog. <br />
     * Then uses Table Data to Visualize Existing Values (Old Values). <br />
     * Set Up the MainDialogPanel with each row a filed to insert data. <br />
     * Use DocumentFilter and inputValidator to Check On Input User.
     *
     * @param contentPackageList containing only MetaData (index, columnName, precision, isNullable, JDBCType)
     */
    private void setPanel(List<ContentPackage> contentPackageList, HashMap<String, Integer> primaryKeys) {
        this.mainDialogPanel = UserPanelDialog.createUserInputPanel(contentPackageList);

        Component[] c = mainDialogPanel.getComponents();
        int i = 0;
        /* Set Existing Data In Input Fields */
        for(Component component : c){
            if (component instanceof JPanel internal){
                Component[] internalComp = internal.getComponents();

                for(Component comp : internalComp){
                    if(comp instanceof JFormattedTextField || comp instanceof DatePicker) {
                        ContentPackage cp = contentPackageList.get(i++);
                        if (primaryKeys.keySet().stream().anyMatch(key -> cp.getColumnName().equalsIgnoreCase(key))) {
                            comp.setEnabled(false);
                            ((JComponent) comp).setToolTipText("Disable Update On Key");
                        }
                        if (comp instanceof JFormattedTextField f) {
                            f.setText(cp.getDataString());
                        } else {
                            DatePicker d = (DatePicker) comp;
                            d.setDate(LocalDate.parse(cp.getDataString()));
                        }
                    }
                }
            } else {
                throw new RuntimeException("Something Wrong While Obtaining Input Field");
            }
        }
    }


    /**
     * Validate Data Entered by User. <br />
     * Calls retrieveAndValidate method that: <br/>
     * - Retrieve data from MainPanel
     * - Check if data Modified Data is well Formatted
     * Then this method filter only updated data. If new List is empty throw an error.
     * Else return the updated ContentPackage list
     * @param emptyData containing metadata for each column
     * @param oldData containing OldData
     * @return A List of ContentPackage with only updated values
     * @throws ValidatorException if data validation fails or no data is actually modified
     */
    private List<ContentPackage> validateData(List<ContentPackage> emptyData, List<ContentPackage> oldData) throws ValidatorException {

        /* Retrieve Data from Panel. It also checks for Null or Empty Strings if Field is not Nullable. */
        List<ContentPackage> newData = DataManipulation.retrieveAndValidate(emptyData, this.mainDialogPanel);

        /* Remove Data if Equal To OldData to Retain only Updated */
        newData.removeIf(c -> oldData.stream().anyMatch(old -> {
            if (old.getColumnName().equalsIgnoreCase(c.getColumnName())) {
                return c.getDataString().equals(old.getDataString());
            }
            return false;
        }));

        /*If All newData equal oldData, newData isEmpty and en exception is thrown */
        if(newData.isEmpty()) throw new ValidatorException("No Data Where Modified");
        return newData;
    }

    /**
     * Dialog to verify data updated by User before actually Updating. <br />
     * - Set Second Panel before Finalizing Insert Query. <br />
     * - Let User Control if Data is Correct.
     * @param oldData All Old Data: hashmap in string, string format to print (K:column name, E:data)
     * @param updatedData Only Updated Data: hashmap in string, string format to print (K:column name, E:data)
     * @return JOptionPane.OK_OPTION value if user confirm, CANCEL otherwise
     */
    private int finalCheckDialog(HashMap<String, String> oldData, HashMap<String, String>updatedData){
        JScrollPane scrollPane = new JScrollPane( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(300, 200));

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
        textArea.setText("Dati Modificati: \n\n");

        for(Map.Entry<String, String> e : oldData.entrySet()){
            if(updatedData.keySet().stream().anyMatch(newK -> e.getKey().equals(newK))){
                textArea.append(' ' + e.getKey() + ": \n" + "  Old: " + e.getValue() + '\n');
                textArea.append("  New: " + updatedData.get(e.getKey()) + '\n');
            } else {
                textArea.append(" Not Modified: " + e.getKey() + ": " + e.getValue() + " \n");
            }
            textArea.append("\n");
        }
        textArea.append("\nConfermi? \n");
        scrollPane.setViewportView(textArea);
        JPanel window = new JPanel();
        window.add(scrollPane);

        return JOptionPane.showConfirmDialog(this.mainDialogPanel, window, "Conferma Dati",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    }



    /* THIS CHECK IS DATABASE SPECIFIC */

    /**
     * Check if Current Working Table Data can be Updated
     * @return true if table is updatable, false if not
     */
    public boolean isUpdatable(String tableName){
        return !tableName.equalsIgnoreCase(TablesEnum.DipendentiView.name());
    }

}
