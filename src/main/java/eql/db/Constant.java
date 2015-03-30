package eql.db;

/**
 * Created by zhangdekun on 15-3-30-下午4:35.
 */
public final class Constant {
    public  static final String[] DB_TABLE_TYPES = { "TABLE" };
    public static final String COLUMN_NAME_TABLE_NAME = "TABLE_NAME";
    public static final String SHOW_TABLES = "show tables";
    public static final String SQLSERVER_TABLES = "select name from sysobjects where xtype='U'";
    public static final String COLUMN_NAME_COLUMN_NAME = "COLUMN_NAME";
    public static final String COLUMN_NAME_TYPE_NAME = "TYPE_NAME";
    public static final String ORACLE_TABLES = "select object_name as TABLE_NAME from user_objects where object_type='TABLE'";
}
