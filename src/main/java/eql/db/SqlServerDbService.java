package eql.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SqlServerDbService extends AbstractDbService {

    private final DB db;

    public SqlServerDbService(DB db) {
        super(db.getVendor(), db.getDriverClass());
        this.db = db;
    }

    @Override
    public Connection getConnection() {
        try {
            return DbFactory.getConnection(db);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getVendor() {
        return Vendor.SQLSERVER;
    }

    public static void main(String[] args) {
        DB db = new DB();
        db.setDriverClass(Vendor.SQLSERVER_DRIVER);
        db.setUrl("jdbc:sqlserver://192.168.1.154:1434;DatabaseName=MB_MrColaDemo");
        db.setUsername("sa");
        db.setPassword("Mingboard1234");
//		db.setUrl("jdbc:oracle:thin:@d196.mzhen.cn:1521:orcl");
//		db.setUsername("zdk");
//		db.setPassword("123456");
        DbService serv = new SqlServerDbService(db);
        List<String> l = serv.tablesName();
        if (l != null) {
            for (String s : l) {
                System.out.println(s);
                DbTable t = serv.getDbTable(s);
                if (t != null) {
                    for (DbColumn c : t.getColumnList()) {
                        System.out.println("type:" + c.getDbType() + "   name:" + c.getColumnName());
                    }
                }
            }
        }
    }
}
