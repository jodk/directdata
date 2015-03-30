package eql.model;

public class Groupby extends Index{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer opt;
	private Integer order;
	private Object[] vls;
	public Integer getOpt() {
		return opt;
	}
	public void setOpt(Integer opt) {
		this.opt = opt;
	}
	public Object[] getVls() {
		return vls;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public void setVls(Object[] vls) {
		this.vls = vls;
	}
}
