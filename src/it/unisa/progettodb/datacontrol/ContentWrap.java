package it.unisa.progettodb.datacontrol;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Wrapper For Containing Multiple Rows of Data
 */
public class ContentWrap {
    private final List<ContentPackage> metaData;
    private final HashMap<Integer,List<String>> rows;

    @SuppressWarnings("unused")
    public ContentWrap(List<ContentPackage> metaData) {
        this(metaData, null);
    }

    public ContentWrap(List<ContentPackage> metaData, HashMap<Integer, List<String>> rows) {
        this.metaData = metaData;
        this.rows = rows;
    }

    /* GETTERS */

    public List<ContentPackage> getMetaData() {
        return metaData;
    }

    public HashMap<Integer, List<String>> getRows() {
        return rows;
    }

    public static ContentWrap getContentWrap(List<ContentPackage> metaData, ResultSet dataSet) throws SQLException {
       HashMap<Integer, List<String>> rows = new LinkedHashMap<>();
        int rowLine = 1;
        while(dataSet.next()){
            List<String> row = new ArrayList<>();
            for(int i = 1; i <= metaData.size(); i++){
                row.add(dataSet.getString(i));
            }
        rows.put(rowLine++, row);
        }
        return new ContentWrap(metaData, rows);
    }
}
