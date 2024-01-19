package it.unisa.progettodb.datacontrol;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import java.sql.JDBCType;

public class CustomDocFilter extends DocumentFilter {
    public static final int NOFIXEDSIZE = -1;
    private final JDBCType type;
    private final int limit;

    public CustomDocFilter(JDBCType type){
        this(type, -1);
    }

    public CustomDocFilter(JDBCType type, int limit) {
        super();
        this.type = type;
        this.limit = limit;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string,
                             AttributeSet attr) throws BadLocationException {

        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.insert(offset, string);

        if (test(sb.toString())) {
            super.insertString(fb, offset, string, attr);
        //} else { // warn the user and don't allow the insert
        }
    }

    private boolean test(String text) {
        if(text == null) return true;
        if(limit != -1 && text.length() > limit) return false;
        try {
            //Integer.parseInt(text);
            DataMapFormatter.objectFromData(this.type, text);

            /*THIS CHECK IS DATABASE SPECIFIC*/
            if(this.type == JDBCType.CHAR) return text.matches("[A-Z0-9-]+");
            return text.matches("[A-Za-z0-9òàèìù'.@_ -]+");

        } catch (IllegalArgumentException e){
            return false;
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text,
                        AttributeSet attrs) throws BadLocationException {

        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.replace(offset, offset + length, text);

        if (test(sb.toString())) {
            super.replace(fb, offset, length, text, attrs);
        //} else { // warn the user and don't allow the insert
        }

    }

    @Override
    public void remove(FilterBypass fb, int offset, int length)
            throws BadLocationException {

        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.delete(offset, offset + length);

        if(sb.isEmpty()){ //Aggiunto altrimenti non cancella ultimo carattere
            super.remove(fb, offset, length);
        } else if (test(sb.toString())) {
            super.remove(fb, offset, length);
        //} else { // warn the user and don't allow the insert
        }

    }


    public static InputVerifier getInputVerifierFixedSize(int size) {
        return new InputVerifier() {

            @Override
            public boolean verify(JComponent input) {
                JTextField field = (JTextField) input;
                String text = field.getText();
                return (size == -1 || text.length() == size) || text.isEmpty();
            }

            @Override
            public boolean shouldYieldFocus(JComponent source, JComponent target) {
                boolean valid = verify(source);
                if (!valid) {
                    if(source instanceof JTextField c ){
                        c.setText("");
                    }
                }
                return valid;
            }

        };
    }


}
