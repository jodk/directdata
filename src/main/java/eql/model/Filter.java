package eql.model;


public class Filter extends Index {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Integer opt;
    private Object[] vls;
    private String gpn;

    public Filter() {

    }

    public Filter(Integer opt, Object[] vls, int index) {
        this.opt = opt;
        this.vls = vls;
        super.setIdx(index);
    }

    public Integer getOpt() {
        return opt;
    }

    public void setOpt(Integer opt) {
        this.opt = opt;
    }

    public Object[] getVls() {
        return vls;
    }

    public String getGpn() {
        if (opt != null && opt == Operator.DG.code()) {

        }
        return gpn;
    }

    public void setGpn(String gpn) {
        this.gpn = gpn;
    }

    public void setVls(Object[] vls) {
        this.vls = vls;
    }

    public void appendValues(Object[] temp) {
        if (this.vls != null) {
            if (temp != null) {
                Object[] all = new Object[this.vls.length + temp.length];
                int count = 0;
                for (Object obj : this.vls) {
                    all[count++] = obj;
                }
                for (Object obj : temp) {
                    all[count++] = obj;
                }
                this.vls = all;
            }
        } else {
            this.vls = temp;
        }
    }
}
