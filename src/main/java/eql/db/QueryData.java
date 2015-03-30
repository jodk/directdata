package eql.db;

import java.io.Serializable;


public class QueryData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DbColumn[] dbColumns;
	private Object[] datas;
	public QueryData(){
		this.dbColumns = new DbColumn[0];
		this.datas =new Object[0];
	}
	public DbColumn[] getDbColumns() {
		return dbColumns;
	}
	public void setDbColumns(DbColumn[] dbColumns) {
		this.dbColumns = dbColumns;
	}
	public Object[] getDatas() {
		return datas;
	}
	public void setDatas(Object[] datas) {
		this.datas = datas;
	}
}
