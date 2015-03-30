package eql.engine;

import eql.db.DB;
import eql.db.DbColumn;
import eql.db.QueryData;

import java.util.List;


public class QueryScreenImplProxy implements QueryScreen {

	private final QueryScreen source;

	public QueryScreenImplProxy(QueryScreen qs) {
		this.source = qs;
	}

	@Override
	public QueryData screen(QueryScreenParam param) {
		QueryData qd = source.screen(param);
		return qd;
	}

	@Override
	public List<DbColumn> numericColumns(DbColumn[] columns) {
		List<DbColumn> ncList = source.numericColumns(columns);
		return ncList;
	}

	@Override
	public List<DbColumn> notNumericColumns(DbColumn[] columns) {
		List<DbColumn> nncList = source.notNumericColumns(columns);
		return nncList;
	}

	@Override
	public DbColumn[] columns(DB db, String sql) {
		DbColumn[] dbcs = source.columns(db, sql);
		return dbcs;
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
