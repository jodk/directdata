package eql.engine;

public class MysqlPagination implements Pagination {

    @Override
    public String getPaginationSql(String sql, int start, int size) {
        return sql + " limit " + start + "," + size;
    }

}
