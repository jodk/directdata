package eql.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MysqlDbService extends AbstractDbService {

	private final DB db;

	public MysqlDbService(DB db) {
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
		return Vendor.MYSQL;
	}

	public static void main(String[] args) throws Exception {
		DB db = new DB();
		db.setDriverClass(Vendor.MYSQL_DRIVER);
		db.setUrl("jdbc:mysql://192.168.1.89:3306/ums?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull");
		db.setUsername("root");
		db.setPassword("root");
		DbService serv = new MysqlDbService(db);
		List<String> l = serv.tablesName();
		QueryData d = serv.query("select *  from t_submchnt");
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
