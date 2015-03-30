package eql.model;

import java.io.Serializable;

public class Limit implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Integer start;
    private Integer size;

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
