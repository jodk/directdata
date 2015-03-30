package eql.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DbFactory {

	private static final Map<String, DbPool> poolMap = new HashMap<String, DbPool>();

	public synchronized static Connection getConnection(DB db) throws SQLException {
		DbPool pool = get(db);
		if (pool != null) {
			return pool.getConnection(db.getUsername(), db.getPassword());
		} else {
			pool = getAPool(db);
			put(db, pool);
			return pool.getConnection();
		}
	}

	public static void close(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static DbService getDbService(DB db) throws Exception {
		if (Vendor.MYSQL.equals(db.getVendor())) {
			return new MysqlDbService(db);
		} else if (Vendor.ORACLE.equals(db.getVendor())) {
			return new OracleDbService(db);
		} else if (Vendor.HIVE.equals(db.getVendor()) || Vendor.HIVE2.equals(db.getVendor())) {
			return new HiveDbService(db);
		} else if (Vendor.SQLSERVER.equals(db.getVendor())) {
			return new SqlServerDbService(db);
		} else if (Vendor.KYLIN.equals(db.getVendor())) {
			return new KylinDbService(db);
		} else {
			throw new Exception("this db's type is not supported.");
		}
	}

	private static DbPool getAPool(DB db) {
		return new DbPool(db);
	}

	private static DbPool get(DB db) {
		return poolMap.get(db.getKey());
	}

	private static void put(DB db, DbPool pool) {
		synchronized (poolMap) {// 将过期的去掉
			String[] keyset = poolMap.keySet().toArray(new String[0]);
			if (keyset != null) {
				for (String key : keyset) {
					DbPool p = poolMap.get(key);
					if (p.isExpire()) {
						p.shutDown();
						poolMap.remove(p);
					}
				}
			}
		}
		poolMap.put(db.getKey(), pool);
	}
}
