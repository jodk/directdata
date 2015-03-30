package eql.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class KylinDbService extends AbstractDbService {

    private final DB db;

    public KylinDbService(DB db) {
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
        return Vendor.KYLIN;
    }

    public static void main(String[] args) throws Exception {
        DB db = new DB();
        db.setDriverClass(Vendor.KYLIN_DRIVER);
        db.setUrl("jdbc:kylin://10.100.5.124:7070/default");
        db.setUsername("ADMIN");
        db.setPassword("KYLIN");
        DbService serv = new KylinDbService(db);
        List<String> l = serv.tablesName();
        QueryData d = serv.query("select * from TRANS_INFO");
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
