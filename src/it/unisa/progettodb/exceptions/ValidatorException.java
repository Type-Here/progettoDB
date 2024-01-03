package it.unisa.progettodb.exceptions;

public class ValidatorException extends Exception{
    public ValidatorException() {
        super("Generic Validator Exception");
    }

    public ValidatorException(String message) {
        super(message);
    }
}
