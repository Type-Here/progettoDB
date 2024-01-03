package it.unisa.progettodb.exceptions;

public class TargaInvalidException extends ValidatorException {
    public TargaInvalidException() {
        super("Targa in Formato Non Valido.");
    }

    public TargaInvalidException(String message) {
        super(message);
    }
}
