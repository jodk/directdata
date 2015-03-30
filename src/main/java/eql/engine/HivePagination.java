package eql.engine;


public class HivePagination implements Pagination {

	/**
	 * hive 不支持分页，这里的分页只能做到top n
	 */
	@Override
	public String getPaginationSql(String sql,int start,int size) {
		int end = start+size;
		return sql+" limit "+end;
	}

}
