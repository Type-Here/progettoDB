import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
        //System.out.printf("Hello and welcome!");
        MainGUI GUI = new MainGUI();

        JFrame mainFrame = new JFrame("ATI Management System");
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setPreferredSize(new Dimension(500, 400));
        mainFrame.setSize(500,400);
        mainFrame.add(GUI.getMainContainer());
        mainFrame.setVisible(true);

        mainFrame.addWindowListener(new WindowAdapter(){

            public void windowClosing(WindowEvent e){
               GUI.closeConnection();
            }

        });
    }
}