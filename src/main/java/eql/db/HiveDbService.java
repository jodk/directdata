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
		super(db.getVendor(),db.getDriverClass());
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
	public static void main(String[] args) {
		DB db = new DB();
		db.setDriverClass(Vendor.HIVE2_DRIVER);
//		db.setUrl("jdbc:hive2://k1222.mzhen.cn:10000");
		db.setUrl("jdbc:hive2://d114.mzhen.cn:10000/test");
		db.setUsername("hive");
		db.setPassword("hive");
//		db.setPassword("tjwork");
//		db.setUrl("jdbc:oracle:thin:@d196.mzhen.cn:1521:orcl");
//		db.setUsername("zdk");
//		db.setPassword("123456");
		DbService serv = new HiveDbService(db);
		List<String> l = serv.tablesName();
		if(l !=null){
			for(String s : l){
				System.out.println(s);
				DbTable t = serv.getDbTable(s);
				if(t !=null){
					for(DbColumn c : t.getColumnList()){
						System.out.println("type:"+c.getDbType()+"   name:"+c.getColumnName());
					}
				}
			}
		}
//		test();
	}
	public static void test(){
		try {
			Class.forName(Vendor.HIVE2_DRIVER);
			Connection con = DriverManager.getConnection("jdbc:hive2://d114.mzhen.cn:10000/test", "hive","hive");
//			System.out.println(con.getSchema());
			DatabaseMetaData meta = con.getMetaData();
			String[] DB_TABLE_TYPES = { "TABLE" };
			ResultSet 	rs = meta.getTables(null,null, null, DB_TABLE_TYPES);
			while (rs.next()) {
				String ca = rs.getString(1);
				String sh = rs.getString(2);
				String tableName = rs.getString("TABLE_NAME");
				if (tableName != null) {
					System.out.println(tableName);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
