package it.unisa.progettodb.modify;
import it.unisa.progettodb.DBManagement;
import it.unisa.progettodb.exceptions.NullTableException;

import javax.swing.*;
import java.awt.*;
import java.sql.JDBCType;
import java.sql.SQLException;
import java.util.List;

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
        List<JDBCType> dataType = null;
        /*TODO*/
        try {
            dataType = managerDB.fetchDataType(this.workingTable);
            setPanel(dataType);
            JOptionPane.showMessageDialog(this.owner, mainDialogPanel, "Insert Data in Table", this.messageType);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NullTableException e){
            JOptionPane.showMessageDialog(this.owner, e.getMessage() + "\nSeleziona Tabella e Riprova",
                                        "Attenzione", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setPanel(List<JDBCType> dataType) {
        /*TODO*/
        mainDialogPanel = new JPanel(new GridLayout(dataType.size(), 2));
        for(JDBCType t: dataType){
            JPanel rowPanel = new JPanel();
            JLabel label = new JLabel("Insert " + t.getName() + ":");
            JTextField textField = new JTextField();
            textField.setPreferredSize(new Dimension(200, 20));
            rowPanel.add(label);
            rowPanel.add(textField);
            mainDialogPanel.add(rowPanel);
        }
    }
}
