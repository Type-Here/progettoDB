package it.unisa.progettodb.datacontrol;

import java.sql.JDBCType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContentPackage {
    private final int index;
    private final String dataString;
    private final String columnName;
    private final JDBCType type;

    public ContentPackage(int index, String dataString, String columnName, JDBCType type) {
        this.index = index;
        this.dataString = dataString;
        this.columnName = columnName;
        this.type = type;
    }

    /* GETTERS */

    public int getIndex() {
        return index;
    }

    public String getDataString() {
        return dataString;
    }

    public String getColumnName() {
        return columnName;
    }

    public JDBCType getType() {
        return type;
    }


    public static HashMap<String, Object> returnDataForQuery(List<ContentPackage> contentPackageList){
        HashMap<String, Object> resultObjectMap = new HashMap<>();
        for(ContentPackage c: contentPackageList){
            Object o = DataMapFormatter.objectFromData(c.getType(), c.getDataString());
            resultObjectMap.put(c.getColumnName(), o);
        }
        return resultObjectMap;
    }


    public static HashMap<String, String> returnDataMapAsString(List<ContentPackage> contentPackageList){
        HashMap<String, String> resultStringMap = new HashMap<>();
        for(ContentPackage c: contentPackageList){
            resultStringMap.put(c.getColumnName(), c.getDataString());
        }
        return resultStringMap;
    }

    @Override
    public String toString() {
        return "ContentPackage{" +
                "index=" + index +
                ", dataString='" + dataString + '\'' +
                ", columnName='" + columnName + '\'' +
                ", type=" + type +
                '}' + '\n';
    }
}
