package eql.engine;


import eql.db.DB;
import eql.db.DbColumn;
import eql.db.QueryData;

import java.util.List;


public interface QueryScreen {

    /**
     * 在原来数据集合中筛选符合的数据
     *
     * @param param
     * @return
     */
    public QueryData screen(QueryScreenParam param);

    /**
     * 在原来数据集合中筛选符合的数据,多数据源时
     *
     * @param paramList
     * @return
     */
    public QueryData screen(List<QueryScreenParam> paramList);

    /**
     * sql查询的所有列
     *
     * @param db
     * @param sql
     * @return
     */
    public DbColumn[] columns(DB db, String sql);

    /**
     * 数值类型的列
     *
     * @param columns
     * @return
     */
    public List<DbColumn> numericColumns(DbColumn[] columns);

    /**
     * 非数值类型的列
     *
     * @param columns
     * @return
     */
    public List<DbColumn> notNumericColumns(DbColumn[] columns);

    public QueryExpression getQueryExpression();
}
