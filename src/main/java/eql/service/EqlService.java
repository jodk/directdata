package eql.service;

import eql.db.DbColumn;
import eql.db.QueryData;
import eql.engine.*;
import eql.model.*;

import java.util.List;



/**
 *@author zhangdekun
 *@version 2015-3-20 上午11:33:31
 **/
public class EqlService {
	/**
	 * 
	 * @param info
	 * @param param
	 * @return
	 */
		public static QueryData apply(DataTableInfo info , CParam param){
			
			QueryData datas = null;
			QueryScreen qscreen = QueryScreenFactory.getQueryScreen(info.getDb().getVendor(), false);
			QueryScreen screenProxy = new QueryScreenImplProxy(qscreen);
			
			Screen screen = new Screen();
			if(param.getFilter() !=null){
				screen.setFilters(param.getFilter().toArray(new Filter[0]));
			}
			if(param.getGet() !=null){
				screen.setGets(param.getGet().toArray(new Get[0]));
			}
			if(param.getGroupby() !=null){
				screen.setGroupbys(param.getGroupby().toArray(new Groupby[0]));
			}
			if(param.getLimit() !=null){
				screen.setLimit(param.getLimit());
			}
			if(param.getMultifilter() !=null){
				screen.setMultifilters(param.getMultifilter().toArray(new Filter[0]));
			}
			if(param.getSort() !=null){
				screen.setSort(param.getSort().toArray(new Sort[0]));
			}
			if(param.getGroupfilter() !=null){
				screen.setGroupfilters(param.getGroupfilter().toArray(new Filter[0]));
			}
			QueryScreenParam sp = new QueryScreenParam(info.getDb(),info.getSql(),screen);
			datas = screenProxy.screen(sp);
			/**
			 * 格式化
			 */
			DbColumn[] dbcs = datas.getDbColumns();
			if(dbcs !=null){
				List<Format> formats = param.getFormat();
				DataFormat.fmt(datas, formats);
			}
			
			return datas;
		}
}
