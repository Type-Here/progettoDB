package it.unisa.progettodb.exceptions;

public class DateInPastException extends ValidatorException {
    public DateInPastException(){
        super("Date cannot be in Past");
    }
    public DateInPastException(String message) {
        super(message);
    }
}
