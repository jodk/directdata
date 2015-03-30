package eql.engine;

import eql.db.Vendor;

import java.util.HashMap;
import java.util.Map;


public class PaginationFactory {

	private static Map<String,Pagination> paginationMap = new HashMap<String,Pagination>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put(Vendor.MYSQL,new MysqlPagination());
			put(Vendor.ORACLE,new OraclePagination());
			put(Vendor.HIVE,new HivePagination());
			put(Vendor.HIVE2,new HivePagination());
			put(Vendor.SQLSERVER,new SqlserverPagination());
		}
	};
	public static Pagination getPagination(String vendor) throws Exception{
		Pagination pagination = paginationMap.get(vendor);
		if(pagination !=null){
			return pagination;
		}else{
			throw new Exception("don't support this vendor:"+vendor+"  to paging.");
		}
	}
}
