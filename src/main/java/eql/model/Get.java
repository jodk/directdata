package eql.model;

public class Get extends Index {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer agt;
	private Integer order;
	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Integer getAgt() {
		return agt;
	}

	public void setAgt(Integer agt) {
		this.agt = agt;
	}

}
