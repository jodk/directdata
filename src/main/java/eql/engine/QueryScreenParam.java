package eql.engine;


import eql.db.DB;

public class QueryScreenParam {

    private DB db;
    private String sql;
    private Screen screen;

    public QueryScreenParam(DB db, String sql, Screen screen) {
        this.db = db;
        this.sql = sql;
        this.screen = screen;
    }

    public QueryScreenParam() {

    }

    public DB getDb() {
        return db;
    }

    public void setDb(DB db) {
        this.db = db;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public static void main(String[] args) {
    }
}
