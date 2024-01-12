package it.unisa.progettodb.datacontrol;

import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * Wrapper For Containing Multiple Rows of Data
 */
public class ContentWrap implements Cloneable{
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


    /**
     * Method to parse ResultData to new ContentWrap Package (Standard Used in this Program).
     * @param metaData List of ContentPackage. dataString is null but contains all needed metadata for each Column (JDBCType, ColumnName, Index,
     *                 Precision, isNullable).
     * @param dataSet ResultSet from a Query to parse data from
     * @return HashMap with K:Integer=Num Row of insertion from DB. V:LIst of Strings containing All Row Data
     * @throws SQLException if retrieving data from ResultSet fails
     */
    public static ContentWrap getContentWrap(List<ContentPackage> metaData, ResultSet dataSet) throws SQLException {
       HashMap<Integer, List<String>> rows = new LinkedHashMap<>();
        int rowLine = 1;
        while(dataSet.next()){
            List<String> row = new ArrayList<>();
            for(int i = 1; i <= metaData.size(); i++){
                if(metaData.get(i - 1).getType().equals(JDBCType.BIT) || metaData.get(i - 1).getType().equals(JDBCType.BOOLEAN)){
                    row.add(dataSet.getString(i).equals("1") ? "Si" : "No");   //For Better View in Table
                } else {
                    row.add(dataSet.getString(i));
                }
            }
        rows.put(rowLine++, row);
        }
        return new ContentWrap(metaData, rows);
    }

    /**
     * Retrieve Automatically all MetaData for each Row. <br />
     * Method to parse ResultData to new ContentWrap Package (Standard Used in this Program).
     * @param dataSet ResultSet from a Query to parse Data and MetaData from
     * @return HashMap with K:Integer=Num Row of insertion from DB. V:LIst of Strings containing All Row Data
     * @throws SQLException if retrieving data from ResultSet fails
     * @see it.unisa.progettodb.datacontrol.ContentWrap#getContentWrap(List, ResultSet)
     */
    public static ContentWrap getContentWrap(ResultSet dataSet) throws SQLException {
        List<ContentPackage> metaData = new ArrayList<>();
        ResultSetMetaData rSetMeta = dataSet.getMetaData();
        System.out.println("Meta: " + rSetMeta.getColumnCount());
        try(dataSet){
            for(int i = 1; i <= rSetMeta.getColumnCount(); i++){
                ContentPackage c = new ContentPackage(i, null, rSetMeta.getColumnName(i),
                        JDBCType.valueOf(rSetMeta.getColumnType(i)));
                c.setPrecision(rSetMeta.getPrecision(i));
                if(rSetMeta.isNullable(i) == ResultSetMetaData.columnNullable) c.setNullable(true);
                metaData.add(c);
            }
            return getContentWrap(metaData, dataSet);
        }
    }

    /**
     * Overrides Object.clone(). <br />
     * Shallow Copy of MetaData (list of ContentPackages). <br />
     * Deep copy of HashMap of Data to survive filtering in FilterData.
     * @return a ContentWrap clone
     * @throws CloneNotSupportedException if not cloneable
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        /*Used to keep original data from filter so no data is actually modified only removed from hashmap when filtering*/
        //Shallow Copy of MetaData
        //Deep Copy of Data
        ContentWrap or = (ContentWrap) super.clone();
        HashMap<Integer, List<String>> original = or.getRows();
        {
            HashMap<Integer, List<String>> copy = new HashMap<>();
            for (Map.Entry<Integer, List<String>> entry : original.entrySet())
            {
                copy.put(entry.getKey(),
                        new ArrayList<>(entry.getValue()));
            }
            return new ContentWrap(or.getMetaData(), copy);
        }
    }

}
