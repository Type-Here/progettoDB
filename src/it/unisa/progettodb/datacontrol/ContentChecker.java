package it.unisa.progettodb.datacontrol;
import it.unisa.progettodb.exceptions.*;

import java.time.LocalDate;
import java.util.*;

/**
 * THIS CHECKER IS HIGHLY DATABASE SPECIFIC
 */
public class ContentChecker {
    private static final String[] tablesList = new String[]{
        "consegna", "dipendente", "tipomerce", "categoria",
        "centrodistribuzione", "dirigente", "impiegato", "mezzo",
        "registropresenze", "sede", "trasportabilitamezzo", "trasportatore"
    };

    /**
     * Main Called Method Return True if Check is Passed. Throw a ValidatorException if not (containing some explanation)
     * @param data HashMap containing key:name of column value:value to check for that specific column
     * @param tableName Table Where Data Will Be Inserted / Modified
     * @return true if check passed.
     * @throws ValidatorException if check fails
     */
    public static boolean checker(HashMap<String, String> data, String tableName) throws ValidatorException {
        if( Arrays.stream(tablesList).noneMatch( t -> t.equalsIgnoreCase(tableName)) )
            throw new ValidatorException("Table Name not valid in checker called method");

        if (tableName.equalsIgnoreCase("consegna")) {
            return insertionCheckerConsegna(data);
        }
        return insertionCheckerGeneric(data);
    }

    /**
     * Validate Data This is Generic For Every Other Element in our Database
     * @param data HashMap containing key:name of column value:value to check for that specific column
     * @return true if check passed.
     * @throws ValidatorException if check fails
     */
    private static boolean insertionCheckerGeneric(HashMap<String, String> data) throws ValidatorException {
        for(Map.Entry<String, String> e : data.entrySet()){

            //Data Minima Lavorativa 15 Anni
            if( e.getKey().equalsIgnoreCase("dataconsegna") ){
                if( LocalDate.parse(e.getValue()).isAfter(LocalDate.now().minusYears(15)) ) throw new DateInPastException();

            } else if( e.getKey().equalsIgnoreCase("matricola") ){
                Validator.validateMatricola(e.getValue());

            } else if( e.getKey().equalsIgnoreCase("codicemerce") ){
                Validator.validateCodiceMerce(e.getValue());

            } else if( e.getKey().equalsIgnoreCase("targa") ){
                Validator.validateTargaMezzo(e.getValue());

            } else if( e.getKey().equalsIgnoreCase("codicesede") ){
                Validator.validateCodiceSede(e.getValue());
            }
        }
        return true;
    }

    /**
     * Validate Data This is Generic For Every Other Element in our Database
     * @param consegna HashMap containing key:name of column value:value to check for that specific column in ' consegna' table
     * @return true if check passed.
     * @throws ValidatorException if check fails
     */
    private static boolean insertionCheckerConsegna(HashMap<String, String> consegna) throws ValidatorException {

        for(Map.Entry<String, String> e : consegna.entrySet()){

            if( e.getKey().equalsIgnoreCase("dataconsegna") ){
                if( LocalDate.parse(e.getValue()).isBefore(LocalDate.now()) ) throw new DateInPastException();

            } else if( e.getKey().equalsIgnoreCase("matricola") ){
                Validator.validateMatricola(e.getValue());

            } else if( e.getKey().equalsIgnoreCase("codicemerce") ){
                Validator.validateCodiceMerce(e.getValue());

            } else if( e.getKey().equalsIgnoreCase("targa") ){
                Validator.validateTargaMezzo(e.getValue());
            }
        }
        return true;
    }
}
