package it.unisa.progettodb.modify;

public interface  DataManipulation {
    enum StateChangeEnum{ TableChanged }
    enum TablesEnum{
        Consegna, DipendentiView, Other;

        public static String getTable(String tableName){
            if(tableName.equalsIgnoreCase("consegna")) return Consegna.name();
            if(tableName.equalsIgnoreCase("dipendentiview")) return DipendentiView.name();
            return Other.name();
        }
    }
    /**
     * NB: Return true if Table Needs Reload, False if Not.
     * A False Return Value doesn't always indicate an error (I.E. Operation Cancelled by User)
     */
    boolean createDialog();
}
