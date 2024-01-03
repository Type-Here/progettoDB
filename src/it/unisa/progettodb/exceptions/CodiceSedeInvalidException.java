package it.unisa.progettodb.exceptions;

public class CodiceSedeInvalidException extends ValidatorException {
    public CodiceSedeInvalidException() {
        super("Codice Sede in Formato Non Valido");
    }

    public CodiceSedeInvalidException(String message) {
        super(message);
    }
}
