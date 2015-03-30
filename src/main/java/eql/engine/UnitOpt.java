package eql.engine;


import eql.model.DataType;
import eql.model.Operator;
import eql.model.RelativeTime;

public class UnitOpt {

    private boolean isRelative = false;
    private String name;
    private String optName;
    private String dtName;
    private Object[] values;

    public UnitOpt(String name, int opt, Integer dt, Object[] values) {
        this.name = name;
        this.optName = Operator.opt(opt);
        if (dt != null) {
            this.dtName = DataType.dt(dt);
        }
        this.values = values;
        if (Operator.LAST.code() == opt) {
            this.isRelative = true;
        }
    }

    public boolean isRelative() {
        return isRelative;
    }

    public void setRelative(boolean isRelative) {
        this.isRelative = isRelative;
    }

    private boolean isDateType() {
        if (this.dtName != null) {
            return DataType.DATE.toString().equals(this.dtName);
        }
        return false;
    }

    public String getUnit() {
        try {
            if (Operator.IN.toString().equals(optName) || Operator.NN.toString().equals(optName)) {
                String vs = "''";
                if (values != null) {
                    for (Object obj : values) {
                        vs = vs + ",'" + obj + "'";
                    }
                }
                return name + " " + optName + " (" + vs + ")";
            } else if (Operator.LAST.toString().equals(optName)) {
                if (values != null) {
                    if (values.length > 1) {
                        return name + " between '" + RelativeTime.getLastStr(values[0].toString(), false) + "' and '" + RelativeTime.getLastStr(values[1].toString(), true) + "'";
                    } else {
                        return name + ">='" + RelativeTime.getLastStr(values[0].toString(), false) + "'";
                    }
                }
                return "";
            } else if (Operator.BEWTEEN.toString().equals(optName)) {
                if (isDateType()) {
                    return name + ">='" + values[0] + "' and " + name + "<='" + values[1] + "'";
                } else {
                    return name + ">=" + values[0] + " and " + name + "<=" + values[1];
                }
            }
            {
                if (isDateType()) {
                    return name + optName + "'" + values[0] + "'";
                } else {
                    return name + optName + values[0];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }
}
