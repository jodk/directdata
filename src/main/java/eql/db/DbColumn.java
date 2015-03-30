package eql.db;

import java.io.Serializable;

public class DbColumn implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final String HIDEN_PREFIX = "HIDE_";// 以这个为前缀的列不会显示，也不会参与数据查询,辅助作用
    private Integer index;
    private String columnName;
    private String dbType;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDbType() {
        return dbType;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }
}
