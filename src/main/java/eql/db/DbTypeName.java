package eql.db;

import java.lang.reflect.Field;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class DbTypeName {

    private final static Map<Integer, String> jdbcTypeNameMap = new HashMap<Integer, String>();
    private final static Map<String, Integer> jdbcTypeCodeMap = new HashMap<String, Integer>();
    private final static Map<String, String> numericNameMap = new HashMap<String, String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {
            put("NUMERIC", "NUMERIC");
            put("INTEGER", "INTEGER");
            put("DECIMAL", "DECIMAL");
            put("SMALLINT", "SMALLINT");
            put("FLOAT", "FLOAT");
            put("DOUBLE", "DOUBLE");
            put("BIT", "BIT");
            put("TINYINT", "TINYINT");
            put("BIGINT", "BIGINT");
        }
    };// 数值类型
    private final static Map<String, String> nonumericNameMap = new HashMap<String, String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {
            put("NULL", "NULL");
            put("CHAR", "CHAR");
            put("DATALINK", "DATALINK");
            put("REAL", "REAL");
            put("VARCHAR", "VARCHAR");
            put("LONGNVARCHAR", "LONGNVARCHAR");
            put("NCHAR", "NCHAR");
            put("BOOLEAN", "BOOLEAN");
            put("NVARCHAR", "NVARCHAR");
            put("ROWID", "ROWID");
            put("TIMESTAMP", "TIMESTAMP");
            put("TIME", "TIME");
            put("OTHER", "OTHER");
            put("LONGVARBINARY", "LONGVARBINARY");
            put("VARBINARY", "VARBINARY");
            put("DATE", "DATE");
            put("BINARY", "BINARY");
            put("LONGVARCHAR", "LONGVARCHAR");
            put("STRUCT", "STRUCT");
            put("ARRAY", "ARRAY");
            put("JAVA_OBJECT", "JAVA_OBJECT");
            put("DISTINCT", "DISTINCT");
            put("REF", "REF");
            put("BLOB", "BLOB");
            put("CLOB", "CLOB");
            put("NCLOB", "NCLOB");
            put("SQLXML", "SQLXML");
        }
    };// 非数值类型

    static {
        Field[] fields = Types.class.getFields();
        if (fields != null) {
            for (Field field : fields) {
                try {
                    Integer value = (Integer) field.get(null);
                    String name = field.getName();
                    jdbcTypeNameMap.put(value, name);
                    jdbcTypeCodeMap.put(name, value);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                }
            }
        }
    }

    public static String getTypeName(Integer dataType) {
        return jdbcTypeNameMap.get(dataType);
    }

    public static Integer getTypeCode(String typeName) {
        if (typeName == null) {
            return null;
        }
        return jdbcTypeCodeMap.get(typeName.toUpperCase());
    }

    /**
     * @param sqlType java.sql.Types
     * @return
     */
    public static boolean isNumericType(String sqlType) {
        return numericNameMap.containsKey(sqlType);
    }

    public static boolean isTime(String sqlType) {
        return "TIME".equals(sqlType);
    }

    public static boolean isDate(String sqlType) {
        return "DATE".equals(sqlType);
    }

    public static boolean isTimestamp(String sqlType) {
        return "TIMESTAMP".equals(sqlType);
    }

    public static void main(String[] args) {
        for (Map.Entry<Integer, String> en : jdbcTypeNameMap.entrySet()) {
            System.out.println("key:" + en.getKey() + "       value:"
                    + en.getValue());
        }
    }
}
