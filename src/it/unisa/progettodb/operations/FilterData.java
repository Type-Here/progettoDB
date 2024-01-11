package it.unisa.progettodb.operations;

import it.unisa.progettodb.TableManager;
import it.unisa.progettodb.datacontrol.ContentWrap;

import javax.swing.table.TableCellRenderer;

public class FilterData {
    private final TableManager tableManager;
    private ContentWrap contentWrap;

    public FilterData(TableManager tableManager) {
        this.tableManager = tableManager;
    }

    public void createDialog(){

    }
}
