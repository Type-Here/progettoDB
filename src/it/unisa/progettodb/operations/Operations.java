package it.unisa.progettodb.operations;

import it.unisa.progettodb.datacontrol.ContentPackage;
import it.unisa.progettodb.datacontrol.ContentWrap;
import it.unisa.progettodb.sql.DBManagement;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/* THIS CLASS IS HIGHLY DATABASE SPECIFIC */

public class Operations {

    /**
     * select *
     * from ( consegna c join tipomerce m on c.CodiceMerce = m.Codice ) join mezzo using(targa) <br />
     * where CodiceMerce = ? AND targa = ? AND DataConsegna = ?;
     * @param managerDB manager that will execute Query
     * @param rowData data from a table row to search detail from
     */
    public static ContentWrap getDeliveryDetails(DBManagement managerDB, List<ContentPackage> rowData) throws SQLException {
        //HashMap<String, Integer> primary = managerDB.retrievePrimaryKeys("consegna");
        String tableJoin = "( consegna c join tipomerce m on c.CodiceMerce = m.Codice ) join mezzo using(targa) ";
        return managerDB.executeSelect(new String[]{"*"}, tableJoin, ContentPackage.returnDataForQuery(rowData));
    }
}
