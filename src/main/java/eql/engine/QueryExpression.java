package eql.engine;

public class QueryExpression {

    private final String groupby = "group by";
    private final String having = "having";
    private final String orderby = "order by";
    private final String comma = ",";
    private final String and = "and";
    private final String sourceSQL;
    public final int GET = 1;
    public final int AND = 6;
    public final int MULTI_AND = 7;
    public final int GROUPBY = 9;
    public final int HAVING = 11;
    public final int ORDERBY = 13;
    private String[] targetSQL = null;

    private Pagination page;
    private Integer start = 0;// default
    private Integer size = 30;// default

    public QueryExpression(String sourceSql) {
        this.sourceSQL = sourceSql;
        targetSQL = new String[]{"select", "*", "", "from",
                "(" + this.sourceSQL + ") innerTable", "where 1=1", "", "", "", // group
                // by
                "", "", "", "", ""};
    }

    public String getSourceSQL() {
        return sourceSQL;
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
}
