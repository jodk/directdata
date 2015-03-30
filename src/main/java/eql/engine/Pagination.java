package eql.engine;

public interface Pagination {
	public final static String PAGING_TABLE = "pagination_table";
	public String getPaginationSql(String sql, int start, int size);
}
