package it.unisa.progettodb.datacontrol;

import java.sql.JDBCType;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * IMPORTANT: ContentPackage Contains Only Data Of a Single Attribute, NOT the Entire Entity Data!
 */
public class ContentPackage {
    private final int index;
    private final String dataString;
    private final String columnName;
    private final JDBCType type;
    private boolean isNullable;
    private int precision;

    public ContentPackage(int index, String dataString, String columnName, JDBCType type) {
        this.index = index;
        this.dataString = dataString;
        this.columnName = columnName;
        this.type = type;
        this.isNullable = false;
        this.precision = -1; //NO PRECISION SET
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

    public boolean isNullable() {
        return isNullable;
    }

    public int getPrecision() {
        return precision;
    }

    /* SETTERS*/

    public void setNullable(boolean nullable) {
        this.isNullable = nullable;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }


    /* ============== STATIC METHODS TO WRAP DATA ========= */

    /**
     * FORMAT FOR QUERY <br />
     * Static Method <br />
     * Format Data For PreparedStatement Query in DBManagement Object. <br />
     * Each DataString is transformed to its Object (es. if int -> parsed to integer) Based on its JBDCType. <br />
     * A HashMap&lt;String,Object&gt; is Created.
     * @param contentPackageList List of ContentPackages to Transform
     * @return HashMap&lt;String,Object&gt; <br /> K: Column Name, E:Object of Data
     */
    public static HashMap<String, Object> returnDataForQuery(List<ContentPackage> contentPackageList){
        HashMap<String, Object> resultObjectMap = new LinkedHashMap<>();
        for(ContentPackage c: contentPackageList){
            Object o = DataMapFormatter.objectFromData(c.getType(), c.getDataString());
            resultObjectMap.put(c.getColumnName(), o);
        }
        return resultObjectMap;
    }

    /**
     * FORMAT FOR PRINT
     * Static Method
     * Format Data For View (String - String)
     * A HashMap&lt;String,Object&gt; is Created With K: Column Name - E: Data in String Format.
     * @param contentPackageList List of ContentPackages to Transform
     * @return HashMap&lt;String,Object&gt; K: Column Name, E:Data in String Format
     */
    public static HashMap<String, String> returnDataMapAsString(List<ContentPackage> contentPackageList){
        HashMap<String, String> resultStringMap = new LinkedHashMap<>();
        for(ContentPackage c: contentPackageList){
            resultStringMap.put(c.getColumnName(), c.getDataString());
        }
        return resultStringMap;
    }

    /**
     * To String Overrides Object method
     * @return String of Content Package
     */
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
