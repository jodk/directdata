package eql.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import static eql.db.Constant.*;


public abstract class AbstractDbService implements DbService {

	public static final Logger logger = Logger.getLogger(AbstractDbService.class);
    public AbstractDbService(String vendor,String driver){
        this.register(vendor,driver);
    }
	@Override
	public List<String> tablesName() {
		ResultSet rs = null;
		Statement stmt = null;
		Connection con = null;
		List<String> tableNameList = new ArrayList<String>();
		try {
			con = this.getConnection();
			DatabaseMetaData meta = con.getMetaData();
			if (meta == null) {
				return tableNameList;
			}
			/**
			 * 通用获得tables，待以后使用metadata实现 //TODO
			 */
			if(isOracle()){
				stmt = con.createStatement();
				rs = stmt.executeQuery(ORACLE_TABLES);
			}else if(isSqlServer()){
				stmt = con.createStatement();
				rs = stmt.executeQuery(SQLSERVER_TABLES);
			}else if(isMysql()){//适合mysql
				rs = meta.getTables(null, null, null, DB_TABLE_TYPES);
//				stmt = con.createStatement();
//				rs = stmt.executeQuery(SHOW_TABLES);
			}else{
				rs = meta.getTables(null, null, null, DB_TABLE_TYPES);
			}
			while (rs.next()) {
				String tableName = rs.getString(COLUMN_NAME_TABLE_NAME);
				if (tableName != null) {
					tableNameList.add(tableName.trim());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("get table name error : "+e.getMessage());
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return tableNameList;
	}

	@Override
	public DbTable getDbTable(String tableName) {
		ResultSet rs = null;
		Connection con = null;
		DbTable table = null;
		try {
			if (tableName == null || tableName.length() == 0) {
				return null;
			} else if (isOracle()) {
				tableName = tableName.toUpperCase();// oracle需要大写
			}

			con = this.getConnection();
			DatabaseMetaData meta = con.getMetaData();
			if (meta == null) {
				return null;
			}
			rs = meta.getColumns(null, null, tableName, null);
			if (rs != null) {
				table = new DbTable();
				table.setTableName(tableName);
			}
			int columnIndex = 0;
			while (rs.next()) {
				String columnType = rs.getString(COLUMN_NAME_TYPE_NAME);
				String columnName = rs.getString(COLUMN_NAME_COLUMN_NAME);
				if (columnName != null) {
					DbColumn dbcolumn = new DbColumn();
					dbcolumn.setIndex(columnIndex);
					dbcolumn.setColumnName(columnName);
					dbcolumn.setDbType(columnType);
					table.getColumnList().add(dbcolumn);
					columnIndex++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("get table info error "+e.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
		return table;
	}

	@Override
	public List<DbTable> getDbTables() {
		List<DbTable> tables = new ArrayList<DbTable>();
		List<String> names = tablesName();
		if (names != null && names.size() > 0) {
			for (String name : names) {
				DbTable table = getDbTable(name);
				if (table != null) {
					tables.add(table);
				}
			}
		}
		return tables;
	}
	private String getSqlForResultSetMetadata(String sql){
		boolean canLimit =false;
		if(canLimit){
			return "";//TODO 不可使用limit做为限制的数据库
		}else if(isSqlServer()){
			return "SELECT TOP 0 * FROM ("+sql+") forMetadataTable";
		}else{
			return  "SELECT * FROM ("+sql+") forMetadataTable limit 0";
		}
	}
	private String countSQL(String sql){
		int fromIndex = sql.toLowerCase().indexOf("from");
		String countSql = "select count(*) " +  sql.substring(fromIndex) ;
		return countSql;
	}
	@Override
	public DbColumn[] queryColumns(String sql) throws Exception {
		ResultSet rs = null;
		Statement stmt = null;
		Connection con = null;
		ResultSetMetaData rsmd = null;
		DbColumn[] dbColumns = null;
		try {
			con = this.getConnection();
			stmt = con.createStatement();
			String metadataSql = getSqlForResultSetMetadata(sql);
			rs = stmt.executeQuery(metadataSql);
			rsmd = rs.getMetaData();
			if (rsmd == null|| rs==null ) {
				return null;
			}
			int columnCount = rsmd.getColumnCount();
			dbColumns= new DbColumn[columnCount];
			for(int i=1;i<=columnCount;i++){
				DbColumn dbc = new DbColumn();
				dbc.setIndex(i-1);
				dbc.setColumnName(wraperColumnName(rsmd.getColumnLabel(i)));
				dbc.setDbType(DbTypeName.getTypeName(rsmd.getColumnType(i)));
				dbColumns[i-1] = dbc;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("get columns meta error :"+e.getMessage());
		}finally{
			if(rs !=null){
				try {
					rs.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			if(stmt !=null){
				stmt.close();
			}
			if(con !=null){
				try {
					con.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
		return dbColumns;
	}

	@Override
	public long count(String sql) throws Exception {
		ResultSet rs = null;
		Statement stmt = null;
		Connection con = null;
		long count = 0;
		try {
			sql = countSQL(sql);
			con = this.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				count = rs.getLong(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(rs !=null){
				try {
					rs.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			if(stmt !=null){
				stmt.close();
			}
			if(con !=null){
				try {
					con.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
		return count;
	}

	@Override
	public QueryData query(String sql) throws Exception{
		ResultSet rs = null;
		Statement stmt = null;
		Connection con = null;
		ResultSetMetaData rsmd = null;
		QueryData qd = null;
		try {
			con = this.getConnection();
			con.setAutoCommit(false);
			stmt = con.createStatement();
			stmt.setFetchSize(1000);//防止数据过多导致内存泄漏
			rs = stmt.executeQuery(sql);
			rsmd = rs.getMetaData();
			if (rsmd == null|| rs==null ) {
				return null;
			}
			qd = new QueryData();
			int columnCount = rsmd.getColumnCount();
			int count = 0;
			DbColumn[] dbColumns =null;
			List<Object> datas = new ArrayList<Object>();
			Map<Integer,Integer> hiddenColumn = new HashMap<Integer,Integer>();
			while(rs.next()){
				if(count ==0){
					List<DbColumn> dbColumnList = new ArrayList<DbColumn>();
					for(int i=1;i<=columnCount;i++){
						DbColumn dbc = new DbColumn();
						dbc.setIndex(i-1);
						String lable = rsmd.getColumnLabel(i);
						dbc.setColumnName(wraperColumnName(lable));
						dbc.setDbType(DbTypeName.getTypeName(rsmd.getColumnType(i)));
						if(lable.toUpperCase().startsWith(DbColumn.HIDEN_PREFIX.toUpperCase())){
							hiddenColumn.put(i, i);
						}else{
							dbColumnList.add(dbc);
						}
					}
					dbColumns = dbColumnList.toArray(new DbColumn[0]);
				}
				Object[] data = new Object[columnCount-hiddenColumn.size()];
				int dataIndex =0;
				for(int i=1;i<=columnCount;i++){
					if(!hiddenColumn.containsKey(i)){
						data[dataIndex] = rs.getObject(i);
						dataIndex++;
					}
				}
				datas.add(data);
				count ++;
			}
			qd.setDatas(datas.toArray(new Object[0]));
			qd.setDbColumns(dbColumns);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("query datas error : "+e.getMessage());
		}finally{
			if(rs !=null){
				try {
					rs.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			if(stmt !=null){
				stmt.close();
			}
			if(con !=null){
				try {
					con.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
		return qd;
	}

	@Override
	public boolean test(String url, String username, String password) {
		Connection con = getConnection();
		if(con ==null){
			return false;
		}else{
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return true;
		}
	}

    @Override
    public void register(String vendor, String driver) {
        DbService.dbServiceMap.put(vendor,this);
        Vendor.vendorMap.put(vendor,driver);
    }

    private boolean isMysql() {
		return Vendor.MYSQL.equals(getVendor());
	}

	private boolean isOracle() {
		return Vendor.ORACLE.equals(getVendor());
	}

	private boolean isSqlServer(){
		return Vendor.SQLSERVER.equals(getVendor());
	}
	private  String wraperColumnName(String columnName){
		if(columnName !=null){
			int dotIndex = columnName.lastIndexOf(".");
			if(dotIndex !=-1){
				columnName = columnName.substring(dotIndex+1);
			}
		}
		return columnName;
	}
	public abstract Connection getConnection();

	public abstract String getVendor();
}
