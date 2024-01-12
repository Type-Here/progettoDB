package it.unisa.progettodb.sql;

import it.unisa.progettodb.datacontrol.ContentPackage;
import it.unisa.progettodb.datacontrol.ContentWrap;
import it.unisa.progettodb.sql.DBManagement;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/* =========== THIS CLASS IS HIGHLY DATABASE SPECIFIC =========== */

@SuppressWarnings("SpellCheckingInspection")
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

    /**
     * Op.17 <br />
     * Conta Consegne per CD aventi un numero di consegne maggiore di 2000. B
     */
    public static ContentWrap getCountDeliveriesGreter2K(DBManagement managerDB) throws SQLException {
        String query = "SELECT citta, zona, count(*) as NumConsegne " +
        "FROM consegna " +
        "GROUP BY citta, zona " +
        "HAVING NumConsegne > 2000;";
        return managerDB.execute(query);
    }

    /**
     * Op.18 <br />
     * Mezzo il cui numero di consegne è il più alto. B
     */
    public static ContentWrap getVehicleMaxDeliveries(DBManagement managerDB) throws SQLException {
        String query = """
                WITH ContaConsegne AS( SELECT targa, COUNT(*) AS NumConsegne FROM consegna GROUP BY targa) \s
                SELECT *\s
                FROM ContaConsegne JOIN mezzo USING(targa)\s
                WHERE numconsegne = (SELECT MAX(numconsegne) as max FROM ContaConsegne);""";
        return managerDB.execute(query);
    }

    /**
     * Op.19 <br />
     * Mezzo il cui numero di consegne è il più alto. B
     */
    public static ContentWrap getOnlyTruckCarriers(DBManagement managerDB) throws SQLException {
        String query = """
                SELECT *\s
                FROM trasportatore\s
                WHERE matricola IN (SELECT matricola FROM consegna JOIN mezzo USING(targa) WHERE tipologia='Autotreno' OR tipologia='Motrice')\s
                AND matricola NOT IN (SELECT matricola FROM consegna JOIN mezzo USING(targa) WHERE tipologia='Treno');""";
        return managerDB.execute(query);
    }


    /**
     * Op.20a <br />
     * Selezione del Tipo di Merce (Nome, Codice) che è stata consegnata in tutti i Centri di Distribuzione; B
     * Divisione
     */
    public static ContentWrap getWareInAllCenter(DBManagement managerDB) throws SQLException {
        String query = """
                SELECT Nome, Codice FROM TipoMerce M\s
                WHERE NOT EXISTS (SELECT * FROM CentroDistribuzione D\s
                                  WHERE NOT EXISTS (SELECT * FROM Consegna C\s
                                                    WHERE M.Codice = C.CodiceMerce\s
                                                    AND D.Zona = C.Zona\s
                                                    AND D.Citta = C.Citta) );""";
        return managerDB.execute(query);
    }

    /**
     * Op.20b <br />
     * I centri distribuzione che hanno ricevuto tutti i tipi di merci; B
     * Divisione
     */
    public static ContentWrap getCenterWithAllWares(DBManagement managerDB) throws SQLException {
        String query = """
                SELECT * FROM CentroDistribuzione D\s
                WHERE NOT EXISTS (SELECT * FROM TipoMerce M\s
                                  WHERE NOT EXISTS (SELECT * FROM Consegna C\s
                                                    WHERE M.Codice = C.CodiceMerce\s
                                                    AND D.Zona = C.Zona\s
                                                    AND D.Citta = C.Citta) );""";
        return managerDB.execute(query);
    }

}
