package it.unisa.progettodb;

import it.unisa.progettodb.logs.LoggerManager;
import it.unisa.progettodb.modify.Insert;

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
    /* Custom Objects */
    private final JMenuBar menuBar;
    private DBManagement managerDB;
    private final TableManager tableManager;
    private final TextAreaManager textAreaManager;
    private final LoggerManager loggerManager;
    private String currentTable;

    public MainGUI(){
        loggerManager = new LoggerManager();

        /* Prompt for Connection To Database (user/pass) */
        StartDialog dialog = new StartDialog(this.loggerManager);
        this.managerDB = dialog.startDialog();
        if(this.managerDB == null) throw new RuntimeException("Unable to Set managerDB");


        if(managerDB.isConnected()){
            JOptionPane.showMessageDialog(this.getMainContainer(), "Connected");
        } else throw new RuntimeException("Unable to Connect");

        menuBar = addMainMenu();

        textAreaManager = new TextAreaManager(textAreaLog);
        tableManager = new TableManager(tableView, managerDB);
        loggerManager.setTextAreaManager(textAreaManager);

        /*Sets tabelleComboBox with Table Names to be Selected for View*/
        setTopPanel();

        /*Button Listener*/
        cercaTabButtonAction();

    }

    /* ------ PUBLIC METHODS ------ */

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
     * Sets JPanel topPanel.
     * Used to Updated JComboBox after each change of Table View.
     */
    public void setTopPanel(){
        List<String> tables;
        try {
            tables = tableManager.getSchemasNames();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tables.forEach(n -> tabelleComboBox.addItem(makeObj(n)));
    }

    /**
     * Close connection to Database
     */
    public void closeConnection(){
        managerDB.closeConnection();
    }

    /* ------ PRIVATE METHODS ------ */

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
                    tableManager.setTable(tab);
                    this.currentTable = tab;
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
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
            if(this.managerDB == null) throw new RuntimeException("Unable To Reconnect");
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

            Insert insertPane = new Insert(this.getMainContainer(), this.managerDB, currentTable);
        });
        /*TODO*/


        /* Operazioni Menu */

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
        menuBar.add(menuOperazioni);
        menuBar.add(menuAbout);

        return menuBar;
    }


}

