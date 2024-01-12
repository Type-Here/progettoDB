package it.unisa.progettodb;

import it.unisa.progettodb.datacontrol.ContentWrap;
import it.unisa.progettodb.sql.DBManagement;
import it.unisa.progettodb.sql.Operations;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class MenuAction {

    private final DBManagement managerDB;
    private final JComponent owner;

    public MenuAction(DBManagement managerDB, JComponent owner) {
        this.managerDB = managerDB;
        this.owner = owner;
    }


    /* -------- Set all Action Methods Package-Private ----------- */

    /**
     * Action for JMenuItem sumStipendi
     */
    void openSumStipendiDialog(){
        try {
            ContentWrap result = Operations.getSumSalaries(this.managerDB);
            createTableDialog(result, "cte_stipendi", 300, 80, "Somma Stipendi Mensili");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this.owner, "Error: \n" + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    /**
     * Action for JMenuItem sumWeight
     */
    void openSumWeightDialog(){
        try {
            ContentWrap result = Operations.getSumOfWeight(this.managerDB);
            createTableDialog(result, "cte_weights", 600, 400, "Peso Totale per CD e TipoMerce");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this.owner, "Error: \n" + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    /**
     * Action for JMenuItem countDeliveries
     */
    void openCountDeliveriesDialog(){
        try {
            ContentWrap result = Operations.getCountDeliveriesGreter2K(this.managerDB);
            createTableDialog(result, "cte_count_deliveries", 600, 400, "CD con Consegne Maggiori di 2000");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this.owner, "Error: \n" + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }


    /**
     * Action for JMenuItem vehicleMaxDeliveries
     */
    void openVehicleMaxDeliveriesDialog(){
        try {
            ContentWrap result = Operations.getVehicleMaxDeliveries(this.managerDB);
            createTableDialog(result, "cte_weights", 600, 200, "Mezzo con Maggiori Consegne");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this.owner, "Error: \n" + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }


    /* ============================== DIALOG CREATOR =================================== */


    /**
     * Create a Dialog Panel containing a new temporary Table with all Data in a ContentWrap.
     * @param result ContentWrap containing all Data and MetaData to Display
     * @param tempTableName name of the Temporary Table
     * @param width Preferred Width for Main JPanel
     * @param height  Preferred Height for Main JPanel
     */
    void createTableDialog(ContentWrap result, String tempTableName, int width, int height, String title) {
        JTable detailsTable = new JTable();
        detailsTable.getTableHeader().setReorderingAllowed(false);
        detailsTable.setAutoCreateRowSorter(true);
        TableManager temp = new TableManager(detailsTable, null);
        temp.setTable(result, tempTableName);
        JPanel panel = new JPanel(new GridLayout(1, 1));
        panel.setPreferredSize(new Dimension(width, height));

        panel.add(new JScrollPane(detailsTable));
        JOptionPane.showMessageDialog(this.owner, panel, title, JOptionPane.PLAIN_MESSAGE);
    }


}
