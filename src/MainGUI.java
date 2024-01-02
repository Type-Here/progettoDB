import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class MainGUI {
    private JPanel mainContainer;
    private JPanel topPanel;
    private JTable tableView;
    private JComboBox<Object> tabelleComboBox;
    private JButton cercaTabButton;
    private JTextArea textAreaLog;

    private int attempts;
    DBManagement managerDB;
    TableManager tableManager;
    TextAreaManager textAreaManager;
    LoggerManager loggerManager;

    public MainGUI(){
        attempts = 1;
        loggerManager = new LoggerManager();
        startDialog();


        if(managerDB.isConnected()){
            JOptionPane.showMessageDialog(this.getMainContainer(), "Connected");
        } else throw new RuntimeException("Unable to Connect");


        textAreaManager = new TextAreaManager(textAreaLog);
        tableManager = new TableManager(tableView, managerDB);
        loggerManager.setTextAreaManager(textAreaManager);

        setTopPanel();
        cercaTabButtonAction();

        try {
            tableManager.setTable("dipendente");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void startDialog(){
        startDialog("Insert Credentials");
    }


    public void startDialog(String title){
        JPanel dialogPanel = new JPanel(new GridLayout(2,1));
        Dimension StandardDim = new Dimension(200, 20);

        JLabel userLabel = new JLabel("User");
        userLabel.setPreferredSize(new Dimension(100, 20));
        JTextField user = new JTextField();
        user.setEditable(true);
        user.setPreferredSize(StandardDim);

        JLabel passLabel = new JLabel("Password");
        passLabel.setPreferredSize(new Dimension(100, 20));
        JPasswordField pass = new JPasswordField();
        pass.setPreferredSize(StandardDim);

        JPanel row0 = new JPanel();
        JPanel row1 = new JPanel();

        row0.add(userLabel);
        //row0.add(new JSeparator(SwingConstants.HORIZONTAL));
        row0.add(user);

        row1.add(passLabel);
        //row1.add(new JSeparator(SwingConstants.HORIZONTAL));
        row1.add(pass);

        dialogPanel.add(row0);
        dialogPanel.add(row1);

        int option = JOptionPane.showConfirmDialog(null, dialogPanel, title, JOptionPane.OK_CANCEL_OPTION);
        if(option == JOptionPane.OK_OPTION){
            //System.out.println(user.getText()); //Only DEBUG PURPOSES
            //System.out.println( new String(pass.getPassword()) ); //Only DEBUG PURPOSES

            try {
                this.managerDB = new DBManagement( user.getText(),  new String(pass.getPassword()), loggerManager );

            } catch (SQLException e) {
                if(this.attempts < 3){
                    this.attempts++;
                    startDialog("Credentials - Attempt " + attempts + " (Max: 3)");
                } else {
                    JOptionPane.showMessageDialog(dialogPanel, "Max Attempts Exceeded", "Warning", JOptionPane.ERROR_MESSAGE);
                    System.err.println(e.getMessage());
                    System.exit(1);
                }
            }
        } else {
            System.exit(0);
        }
    }

    public JPanel getMainContainer() {
        return this.mainContainer;
    }


    public void setTopPanel(){
        List<String> tables = null;
        try {
            tables = tableManager.getTableColumnNames();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tables.forEach(n -> tabelleComboBox.addItem(makeObj(n)));
    }

    public void closeConnection(){
        managerDB.closeConnection();
    }

    private Object makeObj(final String item)  {
        return new Object() { public String toString() { return item; } };
    }

    private void cercaTabButtonAction(){
        cercaTabButton.addActionListener( e -> {
            Object selected = tabelleComboBox.getSelectedItem();
            if(selected != null) {
                String tab = selected.toString();
                try {
                    tableManager.setTable(tab);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}

