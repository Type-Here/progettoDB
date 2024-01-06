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
import java.util.ArrayList;
import java.util.List;

public class Update extends JOptionPane implements DataManipulation{

    private final DBManagement managerDB;
    private final String workingTable;
    private final Component owner;
    private JPanel mainDialogPanel;
    private List<ContentPackage> contentPackageList;

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
     * - Then if all ok call sendDataToInsert() to execute query by managerDB. <br />
     * @return true if data in table are modified, false if not.
     * This is not an error check because will return false even if user cancel operation!
     */
    @Override
    public boolean createDialog() {
        try {
            if (this.workingTable == null) throw new NullTableException();
            if (!isUpdatable(this.workingTable)) throw new InvalidTableSelectException();
            /*Removes Data That User Cannot Modify*/
            DataManipulation.removeNonUserModifyAbleData(contentPackageList, this.workingTable);
            /*Create JPanel*/
            setPanel(contentPackageList);
            /* Show First Dialog */
            int result = JOptionPane.showConfirmDialog(this.owner, mainDialogPanel, "Insert Data in Table",
                                                        this.optionType, this.messageType);
            if(result == JOptionPane.OK_OPTION){
                System.out.println("OK");
            }else return false;

        /*} catch (SQLException e) {
            throw new RuntimeException(e);*/

        } catch (NullTableException | InvalidTableSelectException e){
            JOptionPane.showMessageDialog(this.owner, e.getMessage() + "\nSeleziona (altra) Tabella e Riprova",
                    "Attenzione", JOptionPane.ERROR_MESSAGE);

        /*} catch (ValidatorException e) {
            JOptionPane.showMessageDialog(this.mainDialogPanel, "Data Not Valid: \n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);*/
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
    private void setPanel(List<ContentPackage> contentPackageList) {
        this.mainDialogPanel = UserPanelDialog.createUserInputPanel(contentPackageList);

        Component[] c = mainDialogPanel.getComponents();
        int i = 0;
        /* Set Existing Data In Input Fields */
        for(Component component : c){
            if (component instanceof JPanel internal){
                Component[] internalComp = internal.getComponents();

                for(Component comp : internalComp){

                    if(comp instanceof JFormattedTextField f){
                        f.setText(contentPackageList.get(i++).getDataString());
                    } else if(comp instanceof DatePicker d){
                        d.setDate(LocalDate.parse(contentPackageList.get(i++).getDataString()));
                    }
                }
            } else {
                throw new RuntimeException("Something Wrong While Obtaining Input Field");
            }
        }
    }



    /* THIS CHECK IS DATABSE SPECIFIC */

    /**
     * Check if Current Working Table Data can be Updated
     * @return true if table is updatable, false if not
     */
    public boolean isUpdatable(String tableName){
        return !tableName.equalsIgnoreCase(TablesEnum.DipendentiView.name());
    }

}
