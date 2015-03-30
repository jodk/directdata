package eql.model;

public class Sort extends Index{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer sort;

	private Integer agt ;
	public static final int DESC = 0;
	public static final  int ASC = 1;
	public Integer getSort() {
		return sort;
	}

	public String getBy(int sort){
		return sort==DESC?"desc":"asc";
	}

	public Integer getAgt() {
		return agt;
	}

	public void setAgt(Integer agt) {
		this.agt = agt;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
}
