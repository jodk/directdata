package eql.engine;

import java.util.ArrayList;
import java.util.List;


import eql.db.DbColumn;
import org.apache.commons.lang.StringUtils;

public class NoNestQueryExpression {

	private final String groupby = "group by";
	private final String having = "having";
	private final String orderby = "order by";
	private final String comma = ",";
	private final String and = "and";
	private final String sourceSQL;
	private final DbColumn[] dbcs;// 数据集合查出来的列
	public final int GET = 1;
	public final int AND = 4;
	public final int MULTI_AND = 5;
	public final int GROUPBY = 9;
	public final int HAVING = 9;
	public final int ORDERBY = 11;
	private String[] targetSQL = null;
	private String[][] columnNamesPair;// 查出来的列名和原始列名的对应关系

	private String selectedString;// 源sql中的选择列部分 , 必定存在
	private int whereIndex = -1;// 源sql中的最外层where的索引,-1代表不存在where

	private Pagination page;
	private Integer start = 0;// default
	private Integer size = 30;// default

	public NoNestQueryExpression(String sourceSql, DbColumn[] dbcs) {
		this.sourceSQL = sourceSql;
		this.dbcs = dbcs;
		initColumnNamesPair();
	}

	public String getSourceSQL() {
		return sourceSQL;
	}

	public String[][] getColumnNamesPair() {
		return columnNamesPair;
	}

	private void changeUnit(int unitIndex, String unit) {
		if (unitIndex == GROUPBY) {
			this.targetSQL[unitIndex - 1] = this.groupby;
		} else if (unitIndex == HAVING) {
			this.targetSQL[unitIndex - 1] = this.having;
		} else if (unitIndex == ORDERBY) {
			this.targetSQL[unitIndex - 1] = this.orderby;
		}
		this.targetSQL[unitIndex] = unit;
	}

	private String getUnit(int unitIndex) {
		return this.targetSQL[unitIndex];
	}

	public void fillSelected(String unit) {
		String origUnit = getUnit(GET);
		if (origUnit.equals("*")) {
			changeUnit(GET, unit);
		} else {
			changeUnit(GET, origUnit + comma + unit);
		}
	}

	public void fillLimit(Pagination page, Integer start, Integer size) {
		this.page = page;
		if (start != null) {
			this.start = start;
		}
		if (size != null) {
			this.size = size;
		}
	}

	public void clearLimit() {
		this.page = null;
	}

	public void fillAnd(String unit) {
		String origUnit = getUnit(AND);
		if (origUnit.equals("")) {
			changeUnit(AND, and + " " + unit);
		} else {
			changeUnit(AND, origUnit + " " + and + " " + unit);
		}
	}

	public void fillMultiAnd(String unit) {
		String origUnit = getUnit(MULTI_AND);
		if (origUnit.equals("")) {
			changeUnit(MULTI_AND, and + " " + unit);
		} else {
			changeUnit(MULTI_AND, origUnit + " " + and + " " + unit);
		}
	}

	public void clearMultiAnd() {
		changeUnit(MULTI_AND, "");
	}

	public void fillGroup(String unit) {
		String origUnit = getUnit(GROUPBY);
		if (origUnit.equals("")) {
			changeUnit(GROUPBY, unit);
		} else {
			changeUnit(GROUPBY, origUnit + comma + unit);
		}
	}

	public void fillHaving(String unit) {
		String origUnit = getUnit(HAVING);
		if (origUnit.equals("")) {
			changeUnit(HAVING, unit);
		} else {
			changeUnit(HAVING, origUnit + " " + and + " " + unit);
		}
	}

	public void fillOrderby(String unit) {
		String origUnit = getUnit(ORDERBY);
		if (origUnit.equals("")) {
			changeUnit(ORDERBY, unit);
		} else {
			changeUnit(ORDERBY, origUnit + comma + unit);
		}
	}

	public String getSQL() {
		StringBuffer sb = new StringBuffer();
		for (String unit : this.targetSQL) {
			sb.append(unit + " ");
		}
		if (page != null) {
			return page.getPaginationSql(sb.toString(), start, size);
		} else {
			return sb.toString();
		}
	}

	private void initColumnNamesPair() {
		String lowerCaseSourceSql = this.sourceSQL.toLowerCase();
		int selectIndex = lowerCaseSourceSql.indexOf("select") + 6;
		int fromIndex = lowerCaseSourceSql.indexOf("from");

		this.selectedString = this.sourceSQL.substring(selectIndex, fromIndex);
		this.whereIndex = getOuterWhere(lowerCaseSourceSql);
		String[] selectedAry = this.selectedString.split(",");
		if (this.selectedString.contains("*")) {// 所有列
			selectedAry = new String[this.dbcs.length];
			for (int i = 0; i < this.dbcs.length; i++) {
				selectedAry[i] = this.dbcs[i].getColumnName();
			}
		}
		if (this.dbcs == null || selectedAry == null || this.dbcs.length != selectedAry.length) {
			// 发生错误
			return;
		}
		this.columnNamesPair = new String[this.dbcs.length][2];
		for (int i = 0; i < this.dbcs.length; i++) {
			DbColumn dbc = this.dbcs[i];
			this.columnNamesPair[i][0] = dbc.getColumnName();
			String oneField = selectedAry[i].trim();
			// TODO 现在通过空格来划分开字段 和 alias，需要sql语句严格，不能有冗余的空格行为
			// 以后解决各种情况
			this.columnNamesPair[i][1] = oneField.split(" ")[0];
		}
		this.targetSQL = new String[] { "SELECT", "*", "FROM", this.sourceSQL.substring(fromIndex + 4) + (this.whereIndex == -1 ? " where 1=1" : " "), ""/**
		 * 
		 * and
		 */
		, ""/** multi and */
		, "", ""/** group by */
		, "", ""/** having */
		, "", "" /** order by */
		};
	}

	public static void main(String[] args) {
		DbColumn dbc1 = new DbColumn();
		dbc1.setColumnName("AA");
		DbColumn dbc2 = new DbColumn();
		dbc2.setColumnName("CC");
		DbColumn[] ary = new DbColumn[2];
		ary[0] = dbc1;
		ary[1] = dbc2;
		String sql = "select *  from (select a,b,c from T where a in(xx,aa)) v,B where a in (select * from c where a=2)";
		NoNestQueryExpression ne = new NoNestQueryExpression(sql, ary);
		System.out.println(ne.getSQL());
	}

	/**
	 * 把包含在（）内的字符全部mark为*包括
	 * 
	 * @param sql
	 * @return
	 */
	private static int getOuterWhere(String sql) {
		int whereIndex = -1;
		int level = 0;
		int length = sql.length();
		int start = 0;
		int end = length;
		List<Integer[]> marks = new ArrayList<Integer[]>();
		String[] sqlAry = new String[length];
		boolean needmark = false;
		for (int i = 0; i < length; i++) {
			Character c = sql.charAt(i);
			sqlAry[i] = c.toString();
			if (c == '(') {
				if (!needmark) {// 括号的开始，多个（时取第一个
					start = i;
				}
				level++;
				needmark = true;
			} else if (c == ')') {
				level--;
				end = i;
			}
			if (level == 0 && needmark) {
				needmark = false;
				marks.add(new Integer[] { start, end });
			}
		}
		for (Integer[] is : marks) {
			int s = is[0];
			int e = is[1];
			for (int j = s; j <= e; j++) {
				sqlAry[j] = "*";
			}
		}
		String markSql = StringUtils.join(sqlAry);
		whereIndex = markSql.indexOf("where");
		return whereIndex;
	}
}
