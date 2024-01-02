import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class StartDialog extends JComponent{
    private int attempts;
    private final LoggerManager loggerManager;

    public StartDialog(LoggerManager loggerManager) {
        this.attempts = 1;
        this.loggerManager = loggerManager;
    }

    public DBManagement startDialog(){
        return startDialog("Insert Credentials");
    }


    public DBManagement startDialog(String title) {
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

            try {
                return new DBManagement(user.getText(), new String(pass.getPassword()), loggerManager);
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
        return null;
    }
}
