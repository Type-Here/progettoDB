package it.unisa.progettodb.exceptions;

public class NullTableException extends IllegalArgumentException {
    public NullTableException() {
        super("Table is Not Selected or Invalidated");
    }

    public NullTableException(String s) {
        super(s);
    }
}
