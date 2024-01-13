package it.unisa.progettodb;

import it.unisa.progettodb.datacontrol.ContentPackage;
import it.unisa.progettodb.datacontrol.ContentWrap;
import it.unisa.progettodb.dialogs.UserPanelDialog;
import it.unisa.progettodb.sql.DBManagement;
import it.unisa.progettodb.sql.Operations;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.sql.JDBCType;
import java.sql.SQLException;
import java.util.ArrayList;

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

    /**
     * Action for JMenuItem vehicleMaxDeliveries
     */
    void openOnlyTruckCarriersDialog(){
        try {
            ContentWrap result = Operations.getOnlyTruckCarriers(this.managerDB);
            createTableDialog(result, "cte_truck_driver", 600, 400, "Solo Camionisti");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this.owner, "Error: \n" + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    /**
     * Action for JMenuItem wareInAllCenter
     */
    public void openWareInAllCenterDialog() {
        try {
            ContentWrap result = Operations.getWareInAllCenter(this.managerDB);
            createTableDialog(result, "cte_ware_center", 600, 400, "TipoMerce consegnata in tutti i Centri");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this.owner, "Error: \n" + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    /**
     * Action for JMenuItem centerWithAllWares
     */
    public void openCenterWithAllWaresDialog() {
        try {
            ContentWrap result = Operations.getCenterWithAllWares(this.managerDB);
            createTableDialog(result, "cte_center_ware", 600, 400, "Centri che hanno ricevuto Tutti i Tipi Merce - Optional");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this.owner, "Error: \n" + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }


    /* ================================ INTERACTIVE ===================================== */
    /**
     * Action for JMenuItem workersPerOffice
     */
    public void setWorkersPerOfficeDialog() {
        ContentPackage sede = new ContentPackage(1, null, "città", JDBCType.VARCHAR);
        List<ContentPackage> list = new ArrayList<>();
        list.add(sede);
        UserPanelDialog main = (UserPanelDialog) UserPanelDialog.createUserInputPanel(list);
        if( JOptionPane.showConfirmDialog(this.owner, main, "Seleziona Città",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION ){
            try {
                ContentWrap result = Operations.getWorkersPerOffice(this.managerDB, main.getTexts());
                createTableDialog(result, "cte_worker_office", 600, 400, "Lavoratori In Sede " + main.getTexts().get("città"));

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this.owner, "Error: \n" + ex.getMessage());
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Action for JMenuItem fuelConsumption
     */
    public void setFuelConsumptionDialog() {
        ContentPackage anno = new ContentPackage(1, null, "anno", JDBCType.INTEGER);
        List<ContentPackage> list = new ArrayList<>();
        list.add(anno);
        UserPanelDialog main = (UserPanelDialog) UserPanelDialog.createUserInputPanel(list);
        if( JOptionPane.showConfirmDialog(this.owner, main, "Seleziona Anno",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION ){
            try {
                ContentWrap result = Operations.getFuelConsumption(this.managerDB, main.getTexts());
                createTableDialog(result, "cte_fuel", 600, 400, "Consumo Carburante Anno " + main.getTexts().get("anno"));

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this.owner, "Error: \n" + ex.getMessage());
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Action for JMenuItem deliveriesOp13
     */
    public void setDeliveries13Dialog() {
        List<ContentPackage> list = new ArrayList<>();
        list.add(new ContentPackage(1, null, "codice", JDBCType.CHAR));
        list.add(new ContentPackage(2, null, "data", JDBCType.DATE));
        UserPanelDialog main = UserPanelDialog.createUserInputPanel(list);
        if( JOptionPane.showConfirmDialog(this.owner, main.getPanel(), "Seleziona Codice Merce e Data",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION ){
            try {
                ContentWrap result = Operations.getDeliveriesOp13(this.managerDB, main.getTexts());
                createTableDialog(result, "cte_op13", 600, 400, "Consegne per Tipo e Data ");

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this.owner, "Error: \n" + ex.getMessage());
                throw new RuntimeException(ex);
            }
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
