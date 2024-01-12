package it.unisa.progettodb;

import it.unisa.progettodb.datacontrol.ContentWrap;
import it.unisa.progettodb.dialogs.StartDialog;
import it.unisa.progettodb.logs.LoggerManager;
import it.unisa.progettodb.modify.Delete;
import it.unisa.progettodb.modify.Insert;
import it.unisa.progettodb.modify.Update;
import it.unisa.progettodb.operations.FilterData;
import it.unisa.progettodb.operations.Operations;
import it.unisa.progettodb.operations.ReleaseWorker;
import it.unisa.progettodb.sql.DBManagement;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;

import java.sql.SQLException;
import java.util.List;

public class MainGUI {
    /* JComponents */
    private JPanel mainContainer;
    private JPanel topPanel;
    private JTable tableView;
    private JComboBox<Object> tabelleComboBox;
    private JButton cercaTabButton;
    private JTextArea textAreaLog;
    private JButton modificaButton;
    private JButton eliminaButton;
    private JButton dettagliButton;
    /* Custom Objects */
    private final JMenuBar menuBar;
    private DBManagement managerDB;
    private final TableManager tableManager;
    private final TextAreaManager textAreaManager;
    private final LoggerManager loggerManager;
    private String currentTable;
    private FilterData filter;

    public MainGUI(){
        loggerManager = new LoggerManager();

        /* Prompt for Connection To Database (user/pass) */
        StartDialog dialog = new StartDialog(this.loggerManager);
        this.managerDB = dialog.startDialog();
        if(this.managerDB == null) throw new RuntimeException("Unable to Set managerDB");


        if(! managerDB.isConnected()) throw new RuntimeException("Unable to Connect");
           // JOptionPane.showMessageDialog(this.getMainContainer(), "Connected");

        menuBar = addMainMenu();

        textAreaManager = new TextAreaManager(textAreaLog);
        tableManager = new TableManager(tableView, managerDB);
        loggerManager.setTextAreaManager(textAreaManager);

        /*Sets tabelleComboBox with Table Names to be Selected for View*/
        setTopPanel();

        /*Set Appropriate Listeners to JTable and Settings*/
        this.setTableSettingsAndListeners();

        /*Open Button Listener*/
        cercaTabButtonAction();

        /*Modify Data Listeners*/
        setModifyAndDeleteButtonAction();

        /*Set Dettagli Button Listener*/
        setDettagliButtonListener();

        /*Set Combo Box Listener*/
        setComboBoxListener();
    }


    /* ---------------------------- PUBLIC METHODS --------------------- */

    /**
     * Getter for the Main JPanel. Used for adding it to JFrame
     * @return JPanel mainContainer
     */
    public JPanel getMainContainer() {
        return this.mainContainer;
    }

    /**
     * Getter for the Main Menu Bar. Used for adding it to JFrame
     * @return JMenuBar menuBar (main menu)
     */
    public JMenuBar getJMenuBar(){
        return this.menuBar;
    }


    /**
     * Close connection to Database
     */
    public void closeConnection(){
        managerDB.closeConnection();
    }

    /* ----------------------- PRIVATE METHODS ------------------------- */

    /* -------- LISTENERS --------- */

    /**
     * Set Table Listeners adn Settings.
     * Focus Listener with KeyboardFocusManager to Remove Selection and Disable buttons when focus is lost.
     * Table Property Change to Disable Buttons on Table Reload.
     * Table Selection Listener to Enable / Disable Buttons Appropriately.
     */
    private void setTableSettingsAndListeners(){
        //Disable user reordering columns with drag and drop
        this.tableView.getTableHeader().setReorderingAllowed(false);
        this.tableView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //Enable User to Select Only 1 Row

        /*KeyboardFocusManager is one of main class to manage focus in Java*/
        /*When table nor modify or delete button are on focus deselect row */
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener("focusOwner", evt -> {
            //System.out.println(evt.getNewValue());
            /* Property Change Event */
            Object c = evt.getNewValue();
            if(c == null) return;
            if( !( c.equals(tableView) || c.equals(modificaButton) || c.equals(eliminaButton) || c.equals(dettagliButton) ) ){
                tableView.clearSelection();
                tableView.getSelectionModel().clearSelection();
            }

        });

        /* Disable Button When Table is Reloaded or Changed */
        this.tableView.addPropertyChangeListener(e->{
            if(this.currentTable != null){
                this.menuBar.getMenu(2).getItem(0).setEnabled(true); //Enable Select -> Filter if a Table is Selected
                if(!this.currentTable.equalsIgnoreCase("consegna")){
                    this.dettagliButton.setVisible(false);
                }
            } else {
                this.dettagliButton.setVisible(false);
                this.menuBar.getMenu(2).getItem(0).setEnabled(false); //Disable Select -> Filter if No Table is Selected
            }

            //In every Case Do:
            this.modificaButton.setEnabled(false);
            this.eliminaButton.setEnabled(false);
            this.dettagliButton.setEnabled(false);


        });


        /*THIS CALL A DATABASE SPECIFIC CONTROL ON WHICH TABLE USER CAN DELETE*/
        /*When Selected Row in Table Enable Modify Button and Delete Button (only on appropriate tables) */
        this.tableView.getSelectionModel().addListSelectionListener( ev -> {
            if(this.tableView.getSelectedRow() >= 0){
                this.modificaButton.setEnabled(true);
                if(this.currentTable.equalsIgnoreCase("consegna")) this.dettagliButton.setEnabled(true);
                if(Delete.isDeletable(this.currentTable)) this.eliminaButton.setEnabled(true);
            } else {
                this.dettagliButton.setEnabled(false);
                this.modificaButton.setEnabled(false);
                this.eliminaButton.setEnabled(false);
            }
        });
    }

    /**
     * Used in JComboBox to Avoid Error if Repeated String
     * Overrides toString method to reprint original string
     * @param item string to be transformed in Object
     * @return Object to add in JComboBox
     */
    private Object makeObj(final String item)  {
        return new Object() { public String toString() { return item; } };
    }

    /**
     * Adds Listener to cercaTabButton
     */
    private void cercaTabButtonAction(){
        cercaTabButton.addActionListener( e -> {
            Object selected = tabelleComboBox.getSelectedItem();
            if(selected != null) {
                String tab = selected.toString();
                try {
                    this.currentTable = tab;
                    this.tableManager.setTable(this.currentTable);
                    this.cercaTabButton.setText("Reload");
                    //If New Table is 'Consegna' set dettagli button true
                    this.dettagliButton.setVisible(this.currentTable.equalsIgnoreCase("consegna"));
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this.getMainContainer(), "Error: \n" + ex.getMessage());
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    /**
     * This Method Sets Listeners for: <br />
     * - Modify Button -> Update Data <br />
     * - Delete Button -> Delete Data <br />
     * in topPanel. <br />
     * Both Launch a JDialog if a Row in Open Table is Selected and the respective button is pressed. <br />
     * If no row is selected a warning dialog is shown instead.
     */
    private void setModifyAndDeleteButtonAction() {

        /* - UPDATE Button Listener - */
        this.modificaButton.addActionListener(e ->{
            int selectedRow = this.tableView.getSelectedRow();

            /*A Check Before Altering Table*/
            if(!this.currentTable.equals(tableManager.getTableName())) throw new RuntimeException("Data is Not in SYNC!");

            if( selectedRow >= 0){

                Update updatePane = new Update(this.getMainContainer(), this.managerDB,
                        this.currentTable, tableManager.getRowContentPackageList(selectedRow));
                if(updatePane.createDialog()) {
                    try {
                        this.tableManager.reloadTable();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this.getMainContainer(), "No Row Selected in Table.",
                        "No Row Selected", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        /* - DELETE Button Listener - */
        this.eliminaButton.addActionListener(e ->{
            int selectedRow = this.tableView.getSelectedRow();

            /*A Check Before Altering Table*/
            if(!this.currentTable.equals(tableManager.getTableName())) throw new RuntimeException("Data is Not in SYNC!");

            /*If A Row is Selected Do:*/
            if( selectedRow >= 0){

                Delete deletePane = new Delete(this.getMainContainer(), this.managerDB,
                        this.currentTable, tableManager.getRowContentPackageList(selectedRow));
                if(deletePane.createDialog()) {
                    try {
                        this.tableManager.reloadTable();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this.getMainContainer(), "No Row Selected in Table.",
                        "No Row Selected", JOptionPane.INFORMATION_MESSAGE);
            }
        });

    }

    /**
     * This Method Sets Listener for: <br />
     * - Dettagli Button <br />
     * in topPanel. <br />
     * This Launch a JDialog if a Row in "Consegna" Table is Selected and the respective button is pressed. <br />
     * It shows details for that specific delivery. <br />
     * If no row is selected, this button should be disabled. It is not visible if Current Table is NOT "Consegna".
     */
    private void setDettagliButtonListener(){
        this.dettagliButton.addActionListener(e ->{
            System.out.println("Dettagli Click");
            int selectedRow = this.tableView.getSelectedRow();

            /*A Check Before Altering Table*/
            if(!this.currentTable.equalsIgnoreCase("consegna")) throw new RuntimeException("Data is Not in SYNC!");

            if( selectedRow >= 0){
                try {
                    ContentWrap result = Operations.getDeliveryDetails(this.managerDB, this.tableManager.getRowContentPackageList(selectedRow));
                    JTable detailsTable = new JTable();
                    TableManager temp = new TableManager(detailsTable, null);
                    temp.setTable(result, "cte");
                    JPanel panel = new JPanel(new GridLayout(1,1));
                    panel.setPreferredSize(new Dimension(1000, 50));

                    panel.add(new JScrollPane(detailsTable));
                    JOptionPane.showMessageDialog(this.getMainContainer(), panel);

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this.getMainContainer(), "Error: \n" + ex.getMessage());
                    throw new RuntimeException(ex);
                }
            } else {
                JOptionPane.showMessageDialog(this.getMainContainer(), "No Row Selected in Table.",
                        "No Row Selected", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }


    /**
     * Set Listener for JComboBox tabelleComboBox:
     * If table is already selected set text of research button to 'Reload'
     * If selected table in ComboBox is different set button to 'Open'
     */
    private void setComboBoxListener() {
        tabelleComboBox.addActionListener(e -> {
            Object o = tabelleComboBox.getSelectedItem();

            if(o != null && o.toString().equals(this.currentTable)){
                cercaTabButton.setText("Reload");
            } else {
                cercaTabButton.setText("Open");
            }
        });
    }


    /* ---------- GRAPHIC COMPONENTS ------------ */

    /**
     * Sets JPanel topPanel.
     * Used to Updated JComboBox after each change of Table View.
     */
    private void setTopPanel(){
        List<String> tables;
        try {
            //tables = tableManager.getSchemasNames();
            tables = this.managerDB.getTablesName();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tables.forEach(n -> tabelleComboBox.addItem(makeObj(n)));
    }

    /**
     * Create Main Menu
     * @return JMenuBar
     */
    private JMenuBar addMainMenu(){
        JMenuBar menuBar = new JMenuBar();
        menuBar.setPreferredSize(new Dimension(50,20));
        JMenu menuFile = new JMenu("File");
        JMenu menuOperazioni = new JMenu("Operazioni");
        JMenu menuSelezione = new JMenu("Selezione");
        JMenu menuModifica = new JMenu("Modifica");
        JMenu menuAbout = new JMenu("About");
        menuBar.setFocusTraversalKeysEnabled(true);

        /* File Menu */
        JMenuItem riconnetti = new JMenuItem("Riconnetti");
        JMenuItem esci = new JMenuItem("Esci");

        menuFile.add(riconnetti);
        menuFile.add(esci);

        //Riconnetti
        riconnetti.addActionListener(e ->{
            this.managerDB = new StartDialog(this.loggerManager).startDialog("Riconnetti al BataBase ATI");
            if(this.managerDB == null) {
                JOptionPane.showMessageDialog(this.mainContainer, "Unable To Reconnect",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        //Esci
        esci.addActionListener(e -> {
            this.closeConnection();
            System.exit(0);
        });


        /* Modifica Menu */
        JMenuItem inserisciModifica = new JMenuItem("Inserisci Riga...");
        JMenuItem updateModifica = new JMenuItem("Modifica Riga...");
        JMenuItem deleteModifica = new JMenuItem("Elimina Riga...");

        menuModifica.add(inserisciModifica);
        menuModifica.add(updateModifica);
        menuModifica.add(deleteModifica);

        //Inserisci (Modifica)
        inserisciModifica.addActionListener(e ->{
            Insert insertPane = new Insert(this.getMainContainer(), this.managerDB, this.currentTable);
            if(insertPane.createDialog()) {
                try {
                    this.tableManager.reloadTable();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        //Elimina (Modifica)
        deleteModifica.addActionListener(e ->{

        });
        /*TODO*/

        /* Selezione Menu */
        JMenuItem filtraDati = new JMenuItem("Filtra...");
        menuSelezione.add(filtraDati);
        filtraDati.setEnabled(false); //On creation no table is selected so disable item
        filtraDati.addActionListener(e->{
            if(this.filter == null)
                this.filter = new FilterData(this.mainContainer, this.tableManager.getWrapData());
            ContentWrap filteredWrap = this.filter.createDialog(this.filter.isFiltered()); //NB return a ContentWrap not a boolean unlike createDialog in some other classes
            if(filteredWrap != null) this.tableManager.setTable(filteredWrap,this.currentTable); //null is not always an error, also canceled op by user
            this.filter.setFiltered(true);
        });


        /* Operazioni Menu */
        JMenuItem rimuoviDipendente = new JMenuItem("Liberate i Cani");
        menuOperazioni.add(rimuoviDipendente);


        rimuoviDipendente.addActionListener(e ->{
            ReleaseWorker releaseWorker = new ReleaseWorker(this.getMainContainer(), this.managerDB);
            if(releaseWorker.createDialog()){
                try {
                    this.tableManager.reloadTable();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        /*TODO*/

        /* About Menu */
        String aboutMessage = "From ATI S.P.A. \nCreated By: type-here and gianni \nVersion 0.1";
        menuAbout.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                JOptionPane.showMessageDialog(getMainContainer(), aboutMessage, "About", JOptionPane.INFORMATION_MESSAGE);
            }
            @Override
            public void menuDeselected(MenuEvent e) { }
            @Override
            public void menuCanceled(MenuEvent e) { }
        });

        menuBar.add(menuFile);
        menuBar.add(menuModifica);
        menuBar.add(menuSelezione);
        menuBar.add(menuOperazioni);
        menuBar.add(menuAbout);

        return menuBar;
    }


}

