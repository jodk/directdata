package eql.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class HiveDbService extends AbstractDbService {

    private final DB db;

    public HiveDbService(DB db) {
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
        return db.getVendor();
    }


}
