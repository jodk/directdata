package eql.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.jolbox.bonecp.BoneCPDataSource;
import org.apache.log4j.Logger;


public class DbPool {
	public static final Logger logger = Logger.getLogger(DbPool.class);
	private static final long keep = 1000 * 60 * 60 * 2;// 毫秒 保持时间
	private long timestamp;
	private DB db;
	private BoneCPDataSource bcpd;//为了兼容keylin

	public DbPool(DB db) {
		this.db = db;
		timestamp = System.currentTimeMillis();
		bcpd = new BoneCPDataSource();
		bcpd.setDriverClass(db.getDriverClass());
		bcpd.setJdbcUrl(db.getUrl());
		bcpd.setUsername(db.getUsername());
		bcpd.setPassword(db.getPassword());
		bcpd.setPoolName(db.hashCode()+"");
		bcpd.setAcquireIncrement(5);
		bcpd.setIdleConnectionTestPeriodInMinutes(1);
		bcpd.setIdleMaxAgeInMinutes(30);
		bcpd.setMaxConnectionsPerPartition(10);
		bcpd.setMinConnectionsPerPartition(5);
		bcpd.setPartitionCount(3);
		bcpd.setReleaseHelperThreads(1);
	}

	public Connection getConnection(){
		try {
			return bcpd.getConnection();
		} catch (SQLException e) {
			logger.error("get connection from " + db.getUrl() + " error: "
					+ e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public Connection getConnection(String username, String password) {
		try {
			return bcpd.getConnection(username, password);
		} catch (SQLException e) {
			logger.error("get connection from " + db.getUrl() + "  username:"
					+ username + "  password:" + password + " error: "
					+ e.getMessage());
			throw new RuntimeException(e);
		}
	} 
	public   void shutDown(){
		bcpd.close();
	}
	public boolean isExpire(){
		long now = System.currentTimeMillis();
		return (now - this.timestamp)>keep;
	}
}
