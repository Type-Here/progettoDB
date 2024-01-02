import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
        MainGUI GUI = new MainGUI();

        JFrame mainFrame = new JFrame("ATI Management System");
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setMinimumSize(new Dimension(500, 400));
        mainFrame.setSize(900,800);
        mainFrame.add(GUI.getMainContainer());
        mainFrame.setJMenuBar(GUI.getJMenuBar());
        mainFrame.setVisible(true);

        mainFrame.addWindowListener(new WindowAdapter(){

            public void windowClosing(WindowEvent e){
               GUI.closeConnection();
               System.out.println("Connection Closed.");
            }

        });
    }
}