package it.unisa.progettodb.exceptions;

public class CodiceMerceInvalidException extends ValidatorException {
    public CodiceMerceInvalidException() {
        super("Invalid Codice Merce");
    }

    public CodiceMerceInvalidException(String message) {
        super(message);
    }
}
