package eql.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import eql.db.*;
import eql.model.*;
import org.apache.commons.lang.StringUtils;

public class QueryScreenDefaultImpl implements QueryScreen {
    @Override
    public QueryData screen(QueryScreenParam params) {
        try {
            QueryData result = null;
            DB db = params.getDb();
            String sourceSQL = params.getSql();
            DbService dbs = DbFactory.getDbService(db);
            DbColumn[] dbColumns = dbs.queryColumns(sourceSQL);
            QueryExpression expression = new QueryExpression(sourceSQL);
//			NoNestQueryExpression expression = new NoNestQueryExpression(sourceSQL,dbColumns);
//			String[][] columnNamePair = expression.getColumnNamesPair();
            Screen screen = params.getScreen();
            /**
             * selected 部分的排序功能还没有处理 TODO
             */
            List<Integer> selectedColumnIdxList = new ArrayList<Integer>();
            if (screen != null) {
                Groupby[] groupbys = screen.getGroupbys();
                //group by
                if (groupbys != null) {
                    for (Groupby gb : groupbys) {
                        int index = gb.getIdx();
                        String colName = dbColumns[index].getColumnName();
                        expression.fillSelected(colName);
//						NoNestUnitAgt noAgt = new NoNestUnitAgt(columnNamePair,index,null);
//						expression.fillSelected(noAgt.getUnit());
                        selectedColumnIdxList.add(index);
                        expression.fillGroup(colName);
                        Integer opt = gb.getOpt();
                        if (opt != null) {
                            UnitOpt u = new UnitOpt(colName, opt, gb.getDt(), gb.getVls());
//								NoNestUnitOpt u = new NoNestUnitOpt(columnNamePair,index,opt,gb.getDt(),gb.getVls());
                            expression.fillHaving(u.getUnit());
                        }
                    }
                }
                // GET
                Get[] gets = screen.getGets();
                if (gets != null) {
                    for (Get get : gets) {
                        int index = get.getIdx();
                        String colName = dbColumns[index].getColumnName();
                        Integer agt = get.getAgt();
                        UnitAgt u = new UnitAgt(colName, agt);
//						NoNestUnitAgt u = new NoNestUnitAgt(columnNamePair,index,agt);
                        expression.fillSelected(u.getUnit());
                        selectedColumnIdxList.add(index);
                    }
                }
                // where
                Filter[] filters = screen.getFilters();
                if (filters != null) {
                    for (Filter filter : filters) {
                        int index = filter.getIdx();
                        int opt = filter.getOpt();
                        String colName = dbColumns[index].getColumnName();
                        UnitOpt u = new UnitOpt(colName, opt, filter.getDt(), filter.getVls());
//						NoNestUnitOpt u = new NoNestUnitOpt(columnNamePair,index,opt,filter.getDt(),filter.getVls());
                        expression.fillAnd(u.getUnit());
                    }
                }
                // order by
                Sort[] orderbys = screen.getSort();
                if (orderbys != null) {
                    for (Sort orderby : orderbys) {
                        int index = orderby.getIdx();
                        String colName = dbColumns[index].getColumnName();
                        Integer sort = orderby.getSort();
                        Integer agt = orderby.getAgt();
                        if (agt != null) {
                            String agtName = Aggregate.agt(agt);
                            if (agt == Aggregate.DCOUNT.code()) {
//								colName = "DISTINCT  "+columnNamePair[index][1];
                                colName = "DISTINCT  " + colName;
                                agtName = Aggregate.agt(Aggregate.COUNT.code());
                            }
                            colName = agtName + "(" + colName + ")";
                        }
                        expression.fillOrderby(colName + "  " + orderby.getBy(sort));
                    }
                }
                //
                Limit limt = screen.getLimit();
                if (limt != null) {
                    expression.fillLimit(PaginationFactory.getPagination(db.getVendor()), limt.getStart(), limt.getSize());
                }

                Filter[] multifilters = screen.getMultifilters();
                /**
                 * groupbys.lenght + 1  : 要包含新加的按照
                 */
                if (multifilters != null) {//
                    List<QueryData> queryDataList = new ArrayList<QueryData>();
                    //对filter中group相同的整合在一起
                    Map<String, List<Filter>> groupMap = new HashMap<String, List<Filter>>();
                    for (Filter filter : multifilters) {
                        String groupName = filter.getGpn();
                        if (StringUtils.isEmpty(groupName)) {
                            String colName = dbColumns[filter.getIdx()].getColumnName();
                            groupName = colName;
                        }
                        if (groupMap.containsKey(groupName)) {
                            List<Filter> filterList = groupMap.get(groupName);
                            filterList.add(filter);
                        } else {
                            List<Filter> filterList = new ArrayList<Filter>();
                            filterList.add(filter);
                            groupMap.put(groupName, filterList);
                        }
                    }
                    for (Map.Entry<String, List<Filter>> entry : groupMap.entrySet()) {
                        expression.clearMultiAnd();
                        String groupName = entry.getKey();
                        List<Filter> filterList = entry.getValue();
                        for (Filter filter : filterList) {
                            int index = filter.getIdx();
                            int opt = filter.getOpt();
                            String colName = dbColumns[index].getColumnName();
                            UnitOpt u = new UnitOpt(colName, opt, filter.getDt(), filter.getVls());
//							NoNestUnitOpt u = new NoNestUnitOpt(columnNamePair,index,opt,filter.getDt(),filter.getVls());
                            expression.fillMultiAnd(u.getUnit());
                        }
                        System.out.println("build sql : " + expression.getSQL());
                        QueryData qd = dbs.query(expression.getSQL());
                        DbColumn[] dbcs = qd.getDbColumns();
                        if (dbcs != null && dbcs.length > 0) {
                            QueryDataUtils.changeColumnsIndex(dbcs, selectedColumnIdxList);
                            QueryDataUtils.changeName(dbcs, groupName, groupbys.length);
                            queryDataList.add(qd);
                        }
                    }
//					
                    QueryData qd = QueryDataUtils.mergeColumn(queryDataList, groupbys.length);
                    //合并后，如果维度列有排序功能，则再进行排序
                    QueryDataUtils.sortQueryData(qd, orderbys);
                    result = qd;
                } else {
                    System.out.println("build sql : " + expression.getSQL());
                    QueryData qd = dbs.query(expression.getSQL());
                    DbColumn[] dbcs = qd.getDbColumns();
                    if (dbcs != null && dbcs.length > 0) {
                        QueryDataUtils.changeColumnsIndex(dbcs, selectedColumnIdxList);
                    }
                    result = qd;
                }
                Filter[] groupfilters = screen.getGroupfilters();
                if (groupfilters != null) {
                    result = QueryDataUtils.mergeRow(result, groupfilters, groupbys.length);
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public List<DbColumn> numericColumns(DbColumn[] columns) {
        List<DbColumn> columnList = new ArrayList<DbColumn>();
        if (columns == null) {
            return columnList;
        }
        for (DbColumn col : columns) {
            if (DbTypeName.isNumericType(col.getDbType())) {
                columnList.add(col);
            }
        }
        return columnList;
    }

    @Override
    public List<DbColumn> notNumericColumns(DbColumn[] columns) {
        List<DbColumn> columnList = new ArrayList<DbColumn>();
        if (columns == null) {
            return columnList;
        }
        for (DbColumn col : columns) {
            if (!DbTypeName.isNumericType(col.getDbType())) {
                columnList.add(col);
            }
        }
        return columnList;
    }

    @Override
    public DbColumn[] columns(DB db, String sql) {
        try {
            DbService dbs = DbFactory.getDbService(db);
            return dbs.queryColumns(sql);
        } catch (Exception e) {
            //TODO
        }
        return null;
    }

    @Override
    public QueryData screen(List<QueryScreenParam> paramList) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public QueryExpression getQueryExpression() {
        // TODO Auto-generated method stub
        return null;
    }

}
