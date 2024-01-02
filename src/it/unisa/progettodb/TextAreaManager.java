package it.unisa.progettodb;

import javax.swing.*;

public class TextAreaManager {
    private final JTextArea textArea;

    public TextAreaManager(JTextArea textArea) {
        this.textArea = textArea;
    }

    public void setTextArea(String text){
        this.textArea.setText(text);
    }

    public void cleanTextArea(){
        this.textArea.setText("");
    }

    public void appendLnText(String text){
        this.textArea.append(text + "\n");
    }

}
