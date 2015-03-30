package eql.engine;


import eql.db.DbColumn;

public class OraclePagination implements Pagination {

    private final String rowNumAlias = DbColumn.HIDEN_PREFIX + "RN";//不在查询结果中出现

    @Override
    public String getPaginationSql(String sql, int start, int size) {
        int end = start + size;
        String limitSql = "select * from (" +
                "select " + Pagination.PAGING_TABLE + ".*,ROWNUM  " + rowNumAlias + " from (" + sql + ") " + Pagination.PAGING_TABLE + " where ROWNUM<=" + end + "" +
                ") where " + rowNumAlias + " >=" + (start + 1);
        return limitSql;
    }

}
