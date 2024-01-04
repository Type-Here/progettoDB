package it.unisa.progettodb.datacontrol;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataMapFormatter {
    private static HashMap<String,Object> formatData(ResultSetMetaData metaData, List<String> newData,
                                                    HashMap<Integer, JDBCType> dataIndexType) throws SQLException {

        HashMap<String,Object> dataFormatted = new HashMap<>();

        for(Map.Entry<Integer, JDBCType> e : dataIndexType.entrySet()){
            dataFormatted.put( metaData.getColumnName(e.getKey()),
                                objectFromData(e.getValue(), newData.get(e.getKey() - 1)) );
        }

        return dataFormatted;
    }

    public static Object objectFromData(JDBCType type, String data){
        if(type == JDBCType.BOOLEAN || type == JDBCType.TINYINT){
            return Boolean.parseBoolean(data);

        } else if(type.equals(JDBCType.DOUBLE) || type.equals(JDBCType.FLOAT) || type.equals(JDBCType.DECIMAL)){
            return Double.parseDouble(data);

        } else if(type.equals(JDBCType.INTEGER)){
            return Integer.parseInt(data);

        } else if(type.equals(JDBCType.BIGINT)) {
            return Long.parseLong(data);

        } else if(type.equals(JDBCType.DATE)){
            return Date.valueOf(data);

        } else if (type.equals(JDBCType.VARCHAR) || type.equals(JDBCType.CHAR)) {
            return data;

        } else if (type.equals(JDBCType.TIME)){
            return Time.valueOf(data);

        } else if(type.equals(JDBCType.TIMESTAMP)){
            return Timestamp.valueOf(data);
        }

        throw new IllegalArgumentException("Unable To Parse Data in objectFromData");
    }
}
