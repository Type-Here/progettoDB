package it.unisa.progettodb.tables;

import it.unisa.progettodb.exceptions.CodiceMerceInvalidException;
import it.unisa.progettodb.exceptions.CodiceSedeInvalidException;
import it.unisa.progettodb.exceptions.MatricolaInvalidException;
import it.unisa.progettodb.exceptions.TargaInvalidException;

public class Validator {
    public static boolean validateMatricola(String matricola) throws MatricolaInvalidException {
        if(matricola.matches("[A-Z]{2}[0-9]{4}")) throw new MatricolaInvalidException();
        return true;
    }

    public static boolean validateCodiceMerce(String codiceMerce) throws CodiceMerceInvalidException {
       if(!codiceMerce.matches("[AMT]-[A-Z]{2}-[0-9]{3}")) throw new CodiceMerceInvalidException();
       try{
           ATI.categorieEnum.valueOf(codiceMerce.substring(2,4));
       } catch (IllegalArgumentException e){
           throw new CodiceMerceInvalidException();
       }
       return true;
    }

    public static boolean validateCodiceSede(String codiceSede) throws CodiceSedeInvalidException {
        if(!codiceSede.matches("SEDE[0-9]{3}")) throw new CodiceSedeInvalidException();
        return true;
    }

    public static boolean validateTargaMezzo(String targa) throws TargaInvalidException {
        if(!targa.matches("[A-Z]{2}[0-9]{3,6}[A-Z]{0,2}")) throw new TargaInvalidException();
        return true;
    }

}
