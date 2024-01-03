package it.unisa.progettodb.exceptions;

public class MatricolaInvalidException extends ValidatorException {
    public MatricolaInvalidException() {
        super("Matricola Non Valida o Formato Non Valido");
    }

    public MatricolaInvalidException(String message) {
        super(message);
    }
}
