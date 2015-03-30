package eql.model;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;


public class CParam {

	private String sid;
	private List<Groupby> groupby;
	private List<Get> get;
	private List<Filter> filter;
	private List<Filter> multifilter;
	private List<Filter> groupfilter;
	private List<Sort> sort;
	private Limit limit;
	private List<Format> format;
	public List<Groupby> getGroupby() {
		return groupby;
	}
	public void setGroupby(List<Groupby> groupby) {
		this.groupby = groupby;
	}
	public List<Get> getGet() {
		return get;
	}
	public void setGet(List<Get> get) {
		this.get = get;
	}
	public List<Filter> getFilter() {
		return filter;
	}
	public void setFilter(List<Filter> filter) {
		this.filter = filter;
	}
	public List<Filter> getMultifilter() {
		return multifilter;
	}
	public void setMultifilter(List<Filter> multifilter) {
		this.multifilter = multifilter;
	}
	public List<Sort> getSort() {
		return sort;
	}
	public void setSort(List<Sort> sort) {
		this.sort = sort;
	}
	public Limit getLimit() {
		return limit;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public void setLimit(Limit limit) {
		this.limit = limit;
	}
	public List<Format> getFormat() {
		return format;
	}
	public List<Filter> getGroupfilter() {
		return groupfilter;
	}
	public void setGroupfilter(List<Filter> groupfilter) {
		this.groupfilter = groupfilter;
	}
	public void setFormat(List<Format> format) {
		this.format = format;
	}
	public static void main(String[] args) {
		CParam p = new CParam();
		Filter filter = new Filter();
		filter.setIdx(1);
		filter.setOpt(Operator.IN.code());
		filter.setVls(new Object[]{"zhangdekun"});
		List<Filter> lf = new ArrayList<Filter>();
		lf.add(filter);
		p.setSid("1");
		p.setFilter(lf);
		Object obj = JSON.toJSON(p);
		System.out.println(obj);
	}
}
