package eql.engine;


import eql.db.DbColumn;

public class SqlserverPagination implements Pagination {

	private final String rowNumAlias = DbColumn.HIDEN_PREFIX + "RN";// 不在查询结果中出现
	private final String tempcolumn = DbColumn.HIDEN_PREFIX + "col";// 不在查询结果中出现

	@Override
	public String getPaginationSql(String sql, int start, int size) {
		int end = start + size;
		String limitSql = "select * from (select row_number() over(order by "+tempcolumn+") "
				+ rowNumAlias
				+ ",* from (select top "
				+ end
				+ "  "+tempcolumn+"=0, * from ("
				+ sql
				+ ") innerTable) outTable) pageTable where "
				+ rowNumAlias
				+ ">" + start;
		return limitSql;
	}

}
