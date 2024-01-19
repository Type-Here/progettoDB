package it.unisa.progettodb.modify;

import com.github.lgooddatepicker.components.DatePicker;
import it.unisa.progettodb.datacontrol.ContentPackage;
import it.unisa.progettodb.dialogs.UserPanelDialog;
import it.unisa.progettodb.exceptions.ValidatorException;

import javax.swing.*;
import java.awt.*;
import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public interface  DataManipulation {
    enum TablesEnum{
        Consegna, DipendentiView, Trasportatore, Dirigente
    }


    /**
     * NB: Return true if Table Needs Reload, False if Not.
     * A False Return Value doesn't always indicate an error (I.E. Operation Cancelled by User)
     */
    boolean createDialog();


    /* THIS METHOD IS DATABASE SPECIFIC */
    /**
     * Removes From List of ContentPackage Each Attribute That User Should NOT Insert OR Modify
     * @param contentPackageList list of contentPackage to filter (1 ContentPackage is 1 Attribute)
     * @param tableName table name where attributes belong to. Needed because some attributes to remove are table specific
     */
    static void removeNonUserModifyAbleData(List<ContentPackage> contentPackageList, String tableName){
        if( tableName.equalsIgnoreCase(TablesEnum.DipendentiView.name()) ) throw new RuntimeException("View Should Not Arrive Here");

        Iterator<ContentPackage> it = contentPackageList.listIterator();
        while(it.hasNext()){
            ContentPackage c = it.next();
            if(tableName.equalsIgnoreCase(TablesEnum.Trasportatore.name())){
                if(c.getColumnName().equalsIgnoreCase("NumeroConsegne")) it.remove();

            } else if(tableName.equalsIgnoreCase(TablesEnum.Dirigente.name())){
                if(c.getColumnName().equalsIgnoreCase("bonus")) it.remove();
            }
            if(c.getType().equals(JDBCType.BIT) || c.getType().equals(JDBCType.BOOLEAN)) it.remove();
        }
    }


    @Deprecated
    static List<ContentPackage> retrieveDataFromPanel(List<ContentPackage> emptyData, JPanel mainDialogPanel) throws ValidatorException {
        Component[] components = mainDialogPanel.getComponents();

        List<String> newData = new ArrayList<>();
        List<ContentPackage> contentPackageList = new ArrayList<>();

        /* Get Data Inserted in Previous Dialog and Add it in a List*/
        for(Component component : components){
            if (component instanceof JPanel internal){
                Component[] internalComp = internal.getComponents();

                for(Component comp : internalComp){

                    if(comp instanceof JFormattedTextField f){
                        newData.add(f.getText());
                    } else if(comp instanceof DatePicker d){
                        newData.add(d.getDate().toString());
                    }
                }
            } else {
                throw new RuntimeException("Something getting info gone wrong");
            }
        }

        if(newData.isEmpty()) throw new ValidatorException("Error Data is Empty");

        /* WHY CREATE A NEW CONTENTPACKAGE? TO NOT EXPOSE setDataString: DATA is Final. */
        System.out.println("Empty: " + emptyData);
        System.out.println("NewData: " + newData);
        /* Populate contentPackageList With Data Inserted by User */
        int i = 0;
        for(ContentPackage c : emptyData){
            String data = newData.get(i++);

            /* FUNDAMENTAL CHECK ON EMPTY STRINGS */
            if(!c.isNullable()){
                if(data == null || data.isEmpty()) throw new ValidatorException("Attributo " + c.getColumnName() + " non può essere vuoto!");
            }
            contentPackageList.add(
                    new ContentPackage(c.getIndex(), data, c.getColumnName(), c.getType())
            );
        }
        return contentPackageList;
    }

    /**
     * Retrieve Data from UserDialogPanel Object after User Input <br />
     * @param metaData MetaData to get info about Column
     * @param userPanel A UserDialogPanel Object to get info from
     * @return List&lt;ContentPackage&gt; with all Data from Panel
     * @throws ValidatorException if Not Nullable Data is Null or Empty
     */
    static List<ContentPackage> retrieveDataFromPanel(List<ContentPackage> metaData, UserPanelDialog userPanel) throws ValidatorException {

        List<String> newData = new ArrayList<>();
        List<ContentPackage> contentPackageList = new ArrayList<>();

        /* Get Data Inserted in Previous Dialog and Add it in a List*/
        for(Map.Entry<String, JComponent> e : userPanel.getFields().entrySet()){
            JComponent comp = e.getValue();
            if(comp instanceof JFormattedTextField f){
                newData.add(f.getText().trim());
            } else if(comp instanceof DatePicker d){
                newData.add(d.getDate().toString());
            }
        }

        if(newData.isEmpty()) throw new ValidatorException("Error Data is Empty");

        /* WHY CREATE A NEW CONTENTPACKAGE? TO NOT EXPOSE setDataString: DATA is Final. */
        /* Populate contentPackageList With Data Inserted by User */
        int i = 0;
        for(ContentPackage c : metaData){
            String data = newData.get(i++);

            /* FUNDAMENTAL CHECK ON EMPTY STRINGS */
            if(!c.isNullable()){
                if(data == null || data.isEmpty()) throw new ValidatorException("Attributo " + c.getColumnName() + " non può essere vuoto!");
            }
            contentPackageList.add(
                    new ContentPackage(c.getIndex(), data, c.getColumnName(), c.getType())
            );
        }
        return contentPackageList;
    }


}
