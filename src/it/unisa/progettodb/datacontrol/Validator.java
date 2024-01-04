package it.unisa.progettodb.datacontrol;

import it.unisa.progettodb.exceptions.CodiceMerceInvalidException;
import it.unisa.progettodb.exceptions.CodiceSedeInvalidException;
import it.unisa.progettodb.exceptions.MatricolaInvalidException;
import it.unisa.progettodb.exceptions.TargaInvalidException;

public class Validator {
    public static void validateMatricola(String matricola) throws MatricolaInvalidException {
        if(matricola.matches("[A-Z]{2}[0-9]{4}")) throw new MatricolaInvalidException();
    }

    public static void validateCodiceMerce(String codiceMerce) throws CodiceMerceInvalidException {
       if(!codiceMerce.matches("[AMT]-[A-Z]{2}-[0-9]{3}")) throw new CodiceMerceInvalidException();
       try{
           ATI.categorieEnum.valueOf(codiceMerce.substring(2,4));
       } catch (IllegalArgumentException e){
           throw new CodiceMerceInvalidException();
       }
    }

    public static void validateCodiceSede(String codiceSede) throws CodiceSedeInvalidException {
        if(!codiceSede.matches("SEDE[0-9]{3}")) throw new CodiceSedeInvalidException();
    }

    public static void validateTargaMezzo(String targa) throws TargaInvalidException {
        if(!targa.matches("[A-Z]{2}[0-9]{3,6}[A-Z]{0,2}")) throw new TargaInvalidException();
    }

}
