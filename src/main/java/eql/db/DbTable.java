package eql.db;

import java.util.ArrayList;
import java.util.List;

public class DbTable {

	private String tableName;
	private List<DbColumn> columnList = new ArrayList<DbColumn>();

	public List<DbColumn> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<DbColumn> columnList) {
		this.columnList = columnList;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}
