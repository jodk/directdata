package eql.db;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;



public class DB implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	public static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
	public static final String HIVE_DRIVER = "org.apache.hadoop.hive.jdbc.HiveDriver";
	public static final String HIVE2_DRIVER = "org.apache.hive.jdbc.HiveDriver";
	public static final String SQLSERVER_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	public static final String KYLIN_DRIVER = "com.kylinolap.jdbc.Driver";
	
	private String driverClass;
	private String url;
	private String username;
	/**
	 * TODO : 密码应该需要加密，保证安全性
	 */
	private String password;
	

	public String getDriverClass() {
		if(driverClass ==null && url !=null){//默认查找一个驱动
			if(url.indexOf(Vendor.MYSQL) !=-1){
				driverClass = MYSQL_DRIVER;
			}else if(url.indexOf(Vendor.ORACLE) !=-1){
				driverClass = ORACLE_DRIVER;
			}else if(url.indexOf(Vendor.HIVE2) !=-1){
				driverClass = HIVE2_DRIVER;
			}else if(url.indexOf(Vendor.HIVE) !=-1){
				driverClass = HIVE_DRIVER;
			}else if(url.indexOf(Vendor.SQLSERVER) !=-1){
				driverClass = SQLSERVER_DRIVER;
			}else if(url.indexOf(Vendor.KYLIN) !=-1){
				driverClass = KYLIN_DRIVER;
			}
		}
		return driverClass;
	}

	public String getVendor() {
		this.driverClass = getDriverClass();
		if (driverClass != null) {
			if (driverClass.equals(MYSQL_DRIVER)) {
				return Vendor.MYSQL;
			}else if(driverClass.equals(ORACLE_DRIVER)){
					return Vendor.ORACLE;
			}else if(driverClass.equals(HIVE_DRIVER)){
				return Vendor.HIVE;
			}else if(driverClass.equals(HIVE2_DRIVER)){
				return Vendor.HIVE2;
			}else if(driverClass.equals(SQLSERVER_DRIVER)){
				return Vendor.SQLSERVER;
			}else if(driverClass.equals(KYLIN_DRIVER)){
				return Vendor.KYLIN;
			}
		}
		return null;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		if(StringUtils.isEmpty(this.password)){
			this.password = null;
		}
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getKey() {
		return driverClass + "," + url;
	}
	public static void main(String[] args) {
	}
}
