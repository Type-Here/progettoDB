package it.unisa.progettodb.logs;

import it.unisa.progettodb.*;
import it.unisa.progettodb.sql.DBManagement;

public class LoggerManager {
    private TextAreaManager textAreaManager;

    public LoggerManager() {
        this.textAreaManager = null;
    }

    public LoggerManager(TextAreaManager textAreaManager) {
        this.textAreaManager = textAreaManager;
    }

    public void setTextAreaManager(TextAreaManager textAreaManager) {
        this.textAreaManager = textAreaManager;
    }

    public void log(DBManagement.ActionEnum action){
        if(textAreaManager == null) return;

        if(action.equals(DBManagement.ActionEnum.Connected)){
            textAreaManager.appendLnText("=============");
            textAreaManager.appendLnText("Connected To Database");
            textAreaManager.appendLnText("-------------");

        } else {
            textAreaManager.appendLnText(action.name() + "Performed; ");
        }
    }

    public void log(String data, DBManagement.ActionEnum action){
        if(textAreaManager == null) return;
        textAreaManager.appendLnText(action.name() + "Performed: " + data);
    }


}
