package eql.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OracleDbService extends AbstractDbService {

	private final DB db;

	public OracleDbService(DB db) {
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
		return Vendor.ORACLE;
	}
	public static void main(String[] args) {
		DB db = new DB();
		db.setDriverClass(DB.ORACLE_DRIVER);
		db.setUrl("jdbc:oracle:thin:@//10.100.1.200:1521/workdb");
		db.setUsername("tjwork");
		db.setPassword("tjwork");
//		db.setUrl("jdbc:oracle:thin:@d196.mzhen.cn:1521:orcl");
//		db.setUsername("zdk");
//		db.setPassword("123456");
		DbService serv = new OracleDbService(db);
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
	}
}
