package eql.engine;


import eql.model.Aggregate;

public class UnitAgt {

    private String name;
    private String optName;

    public UnitAgt(String name, Integer agt) {
        this.name = name;
        if (agt != null) {
            this.optName = Aggregate.agt(agt);
        }
    }


    public String getUnit() {
        if (optName == null || "".equals(optName)) {
            return name;
        }
        if (Aggregate.DCOUNT.toString().equals(optName)) {
            return Aggregate.COUNT + "(" + Aggregate.DISTINCT + " " + name + ")";
        }
        return optName + "(" + name + ")";
    }
}
