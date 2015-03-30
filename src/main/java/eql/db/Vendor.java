package eql.db;

import java.util.HashMap;
import java.util.Map;

public class Vendor {
	public static final String MYSQL = "mysql";
	public static final String ORACLE = "oracle";
	public static final String SQLSERVER = "sqlserver";
	public static final String HIVE = "hive";
	public static final String HIVE2 = "hive2";
	public static final String KYLIN="kylin";

    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    public static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
    public static final String HIVE_DRIVER = "org.apache.hadoop.hive.jdbc.HiveDriver";
    public static final String HIVE2_DRIVER = "org.apache.hive.jdbc.HiveDriver";
    public static final String SQLSERVER_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static final String KYLIN_DRIVER = "com.kylinolap.jdbc.Driver";
    public static final Map<String,String> vendorMap = new HashMap<String,String>(){
        {
            put(MYSQL,MYSQL_DRIVER);
            put(ORACLE,ORACLE_DRIVER);
            put(SQLSERVER,SQLSERVER_DRIVER);
            put(HIVE,HIVE_DRIVER);
            put(HIVE2,HIVE2_DRIVER);
            put(KYLIN,KYLIN_DRIVER);
        }
    };
}
