package it.unisa.progettodb.exceptions;

public class InvalidTableSelectException extends ValidatorException {
    public InvalidTableSelectException() {
        super("This Operation is Not Valid on this Table. Maybe it's a View");
    }

    public InvalidTableSelectException(String message) {
        super(message);
    }
}
