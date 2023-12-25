import javax.print.attribute.standard.JobOriginatingUserName;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Arrays;

public class MainGUI {
    private JTextArea textArea1;
    private JPanel panel1;
    private int attempts;
    DBManagement managerDB;

    public MainGUI(){
        attempts = 1;
        startDialog();

        if(managerDB.isConnected()){
            //DOTHINGS
        } else throw new RuntimeException("Unable to Connect");

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
            //System.out.println(pass.getText());
            try {
                this.managerDB = new DBManagement( user.getText(), Arrays.toString(pass.getPassword()) );

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
}
