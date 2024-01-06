package it.unisa.progettodb.modify;

import it.unisa.progettodb.sql.DBManagement;
import it.unisa.progettodb.datacontrol.ContentPackage;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Update extends JOptionPane implements DataManipulation{

    private final DBManagement managerDB;
    private final String workingTable;
    private final Component owner;
    private JPanel mainDialogPanel;
    private List<ContentPackage> contentPackageList;

    public Update(Component owner, DBManagement managerDB, String workingTable) {
        super(null, JOptionPane.INFORMATION_MESSAGE, JOptionPane.OK_CANCEL_OPTION );
        this.managerDB = managerDB;
        this.workingTable = workingTable;
        this.owner = owner;
    }

    @Override
    public boolean createDialog() {
        return false;
    }



    /* THIS CHECK IS DATABSE SPECIFIC */

    /**
     * Check if Current Working Table Data can be Updated
     * @return true if table if updatable, false if not
     */
    public boolean isUpdatable(){
        return !this.workingTable.equalsIgnoreCase("dipendentiview");
    }

}
