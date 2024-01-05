package it.unisa.progettodb.modify;

import it.unisa.progettodb.datacontrol.ContentPackage;

import java.sql.JDBCType;
import java.util.Iterator;
import java.util.List;

public interface  DataManipulation {
    enum TablesEnum{
        DipendentiView, Trasportatore, Dirigente
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
}
