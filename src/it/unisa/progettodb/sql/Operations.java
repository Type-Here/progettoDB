package it.unisa.progettodb.sql;

import it.unisa.progettodb.datacontrol.ContentPackage;
import it.unisa.progettodb.datacontrol.ContentWrap;
import it.unisa.progettodb.sql.DBManagement;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/* THIS CLASS IS HIGHLY DATABASE SPECIFIC */
@SuppressWarnings("typo")
public class Operations {

    /* =================================== INTERACTIVE ================================== */

    /**
     * Op.14 <br />
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


    /* ======================================== BATCH ========================================== */

    /**
     * Op.15 <br />
     * Sum of All Employees <br />
     * select sum(stipendioMensile) as SommaStipendi
     * from dipendentiView;
     */
    public static ContentWrap getSumSalaries(DBManagement managerDB) throws SQLException {
        String tableName = "dipendentiView";
        return managerDB.executeSelect(new String[]{"ROUND(SUM(stipendioMensile),2) as SommaStipendi"},tableName);
        //return managerDB.execute(new String[]{"SUM(stipendioMensile) as SommaStipendi"},tableName);
    }


    /**
     * Op.16 <br />
     * Somma Peso di Tipo Merce trasportata per CD e per Nome Merce. B
     */
    public static ContentWrap getSumOfWeight(DBManagement managerDB) throws SQLException {
        String query = "SELECT citta, zona, m.nome, ROUND(SUM(m.peso),2) as SommaCarico " +
                "FROM consegna c JOIN tipomerce m ON c.codicemerce=m.codice " +
                "GROUP BY citta, zona, m.nome;";
        return managerDB.execute(query);
    }
}
