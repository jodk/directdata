package eql.db;

import java.util.List;

public interface DbService {

	/**
	 * 数据库中的table 名称列表
	 * 
	 * @return
	 */
	public List<String> tablesName();

	/**
	 * 根据table名称 获得table的信息
	 * 
	 * @param tableName
	 * @return
	 */
	public DbTable getDbTable(String tableName);

	/**
	 * 这个数据库下能看到的所有table的信息
	 * 
	 * @return
	 */
	public List<DbTable> getDbTables();

	/**
	 * 根据sql查询数据
	 * 
	 * @param sql
	 * @return
	 */
	public QueryData query(String sql) throws Exception;
	/**
	 * sql查询的记录数
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public long count(String sql) throws Exception;

	/**
	 * 查询所得到的列信息
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public DbColumn[] queryColumns(String sql) throws Exception;
	/**
	 * 测试链接是否成功
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean test(String url, String username, String password);
}
