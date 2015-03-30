package eql.model;

import eql.db.DB;

import java.io.Serializable;


public class DataTableInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Integer dataId;
    private String dataTableName;
    private DB db;
    private String sql;

    public Integer getDataId() {
        return dataId;
    }

    public String getDataTableName() {
        return dataTableName;
    }

    public void setDataTableName(String dataTableName) {
        this.dataTableName = dataTableName;
    }

    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    public DB getDb() {
        return db;
    }

    public void setDb(DB db) {
        this.db = db;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
