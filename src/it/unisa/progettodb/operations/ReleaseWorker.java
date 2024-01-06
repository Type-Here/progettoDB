package it.unisa.progettodb.operations;

import it.unisa.progettodb.datacontrol.ContentPackage;
import it.unisa.progettodb.datacontrol.ContentWrap;
import it.unisa.progettodb.datacontrol.CustomDocFilter;
import it.unisa.progettodb.exceptions.ValidatorException;
import it.unisa.progettodb.modify.DataManipulation;
import it.unisa.progettodb.sql.DBManagement;

import javax.swing.*;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.sql.JDBCType;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/* THIS IS HIGHLY DATABASE SPECIFIC */

public class ReleaseWorker extends JOptionPane implements DataManipulation {
    private static final int IDLENGTH = 6;
    private final DBManagement managerDB;
    private static final String tableName = "Dipendente";
    private final Component owner;
    private boolean isWorkerOld;
    private final ImageIcon icon;
    private boolean isWorkerNew;

    public ReleaseWorker(Component owner, DBManagement managerDB) {
        super(null, JOptionPane.INFORMATION_MESSAGE, JOptionPane.OK_CANCEL_OPTION );
        this.managerDB = managerDB;
        this.owner= owner;
        this.icon = new ImageIcon("ForseUtili/liberate_i_cani.png");
    }

    /**
     * MAIN CONTROL METHOD: <br />
     * Create Dialog for Updating Data. <br />
     * Find Worker by 'Matricola' Field <br />
     * - Get Worker Data and prompts showDataDialog <br />
     * - If User modifies CheckBox with Working Status prompt for final check.
     * - Then if all ok call sendDataToUpdate() to execute query by managerDB. <br />
     * @return true if data in table are modified, false if not.
     * This is not an error check because will return false even if user cancel operation!
     */
    public boolean createDialog(){
        JPanel mainDialog = new JPanel(new GridLayout(2,1));
        JLabel idLabel = new JLabel("Matricola: *");
        JTextField idTextField = new JTextField();
        idTextField.setPreferredSize(new Dimension(200,20));

        idTextField.setInputVerifier(CustomDocFilter.getInputVerifierFixedSize(IDLENGTH));
        PlainDocument doc = (PlainDocument) idTextField.getDocument();
        doc.setDocumentFilter(new CustomDocFilter(JDBCType.CHAR, IDLENGTH));

        JPanel firstRow = new JPanel();
        firstRow.add(idLabel);
        firstRow.add(idTextField);

        mainDialog.add(firstRow);
        this.setMessage(mainDialog);
        try {
            if (JOptionPane.showConfirmDialog(this.owner, mainDialog,
                    "Seleziona Matricola", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                String id = idTextField.getText().trim();

                if (id.isEmpty()) throw new ValidatorException("Matricola Vuota o Non Valida");
                HashMap<String, Object> condition = new HashMap<>();
                condition.put("Matricola", id);
                ContentWrap data = this.managerDB.executeSelect(new String[]{"*"}, ReleaseWorker.tableName, condition);

                if(this.showDataDialog(data) == JOptionPane.OK_OPTION && isWorkerOld != isWorkerNew){
                    if(this.finalDialog() == JOptionPane.OK_OPTION){
                        HashMap<String, Object> dataMap = new HashMap<>();
                        dataMap.put("AttualeDipendente", isWorkerNew);

                        HashMap<String, Object> primKey = new HashMap<>();
                        primKey.put("Matricola", id);

                        this.managerDB.executeUpdate(dataMap, ReleaseWorker.tableName, primKey);
                        return true;
                    }
                }
            }

        } catch (ValidatorException | SQLException e){
            JOptionPane.showMessageDialog(this.owner, "Error:\n" + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException();
        }
        return false;
    }

    /**
     * Show Data of Worker Queried by "Matricola"
     * @param data ContentWrap containing metadata and all rows (should be only 1 in this case)
     * @return JOptionPane.OK_OPTION Value if user confirm choice otherwise CANCEL_VALUE
     */
    private int showDataDialog(ContentWrap data) {
        AtomicBoolean isWorker = new AtomicBoolean(false);
        StringBuilder build = new StringBuilder("<html>Dati Dipendente: \n");
        data.getRows().forEach((key, value) -> {
            for(int i = 0; i < data.getMetaData().size(); i++){
                ContentPackage c = data.getMetaData().get(i);

                if(c.getType().equals(JDBCType.BIT) || c.getType().equals(JDBCType.BOOLEAN)){
                       isWorker.set(value.get(i).equals("1") || value.get(i).equalsIgnoreCase("true"));
                    build.append(" ").append(c.getColumnName()).append(": ");
                    build.append(isWorker.get() ? "Si" : "No").append("    <br />");
                    continue;
                }
                build.append(" ").append(c.getColumnName()).append(": ");
                build.append(value.get(i)).append("    <br />");
            }
        });
        build.append("</html>");
        this.isWorkerOld = isWorker.get();

        JLabel dataPrint = new JLabel(build.toString());
        JPanel dataDialogPanel = new JPanel(new GridLayout(2,1));

        JPanel row1 = new JPanel();
        row1.add(dataPrint);
        JPanel row2 = new JPanel();

        JCheckBox checkBoxSi = new JCheckBox("Si");
        checkBoxSi.setSelected(isWorker.get());
        JCheckBox checkBoxNo = new JCheckBox("No");
        checkBoxNo.setSelected(!isWorker.get());

        row2.add(checkBoxSi);
        row2.add(checkBoxNo);

        checkBoxSi.addActionListener(e ->{
            if(checkBoxSi.isSelected()){
                checkBoxNo.setSelected(false);
                this.isWorkerNew = true;
            }
        });

        checkBoxNo.addActionListener(e ->{
            if(checkBoxNo.isSelected()){
                checkBoxSi.setSelected(false);
                this.isWorkerNew = false;
            }
        });


        dataDialogPanel.add(row1);
        dataDialogPanel.add(row2);

        return JOptionPane.showConfirmDialog(this.owner, dataDialogPanel,
                "Attenzione", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,
                this.icon);
    }

    /**
     * Last Prompt to Confirm Choice
     * @return JOptionPane.OK_OPTION Value if user confirm choice otherwise CANCEL_VALUE
     */
    private int finalDialog() {
        return JOptionPane.showConfirmDialog(this.owner,
                "Confermare? \n Prima: " + isWorkerOld + "\n Nuovo Valore: " + isWorkerNew,
                "Attenzione", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
    }

}
