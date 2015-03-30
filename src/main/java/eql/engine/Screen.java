package eql.engine;

import eql.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Screen implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Groupby[] groupbys;
    private Get[] gets;
    private Filter[] filters;
    private Filter[] multifilters;
    private Filter[] groupfilters;
    private Sort[] sort;
    private Limit limit;
    private List<Object> relative2ab = new ArrayList<Object>();

    public Groupby[] getGroupbys() {
        return groupbys;
    }

    public void setGroupbys(Groupby[] groupbys) {
        this.groupbys = groupbys;
    }

    public Get[] getGets() {
        return gets;
    }

    public void setGets(Get[] gets) {
        this.gets = gets;
    }

    public Filter[] getFilters() {
        return filters;
    }

    public void setFilters(Filter[] filters) {
        if (filters != null) {
            this.filters = filters;
            fillRelative2ab(filters);
        }
    }

    private void fillRelative2ab(Filter[] filters) {
        if (filters == null) {
            return;
        }
        for (Filter filter : filters) {
            UnitOpt opt = new UnitOpt("", filter.getOpt(), filter.getDt(), filter.getVls());
            if (opt.isRelative()) {
                relative2ab.add(opt.getUnit());
            }
        }
    }

    public Filter[] getMultifilters() {
        return multifilters;
    }

    public void setMultifilters(Filter[] multifilters) {
        this.multifilters = multifilters;
        fillRelative2ab(multifilters);
    }

    public Filter[] getGroupfilters() {
        return groupfilters;
    }

    public void setGroupfilters(Filter[] groupfilters) {
        this.groupfilters = groupfilters;
        fillRelative2ab(groupfilters);
    }

    public Sort[] getSort() {
        return sort;
    }

    public void setSort(Sort[] sort) {
        this.sort = sort;
    }

    public Limit getLimit() {
        return limit;
    }

    public void setLimit(Limit limit) {
        this.limit = limit;
    }
}
