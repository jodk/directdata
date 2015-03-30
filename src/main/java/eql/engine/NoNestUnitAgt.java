package eql.engine;


import eql.model.Aggregate;

public class NoNestUnitAgt {

	private Integer index;
	private String optName;
	private String[][] columnNamePair;

	public NoNestUnitAgt(String[][] columnNamePair,Integer columnIndex, Integer agt) {
		this.index = columnIndex;
		this.columnNamePair = columnNamePair;
		if(agt!=null){
			this.optName = Aggregate.agt(agt);
		}
	}


	public String getUnit() {
		if(optName==null || "".equals(optName)){
			return columnNamePair[index][1] + " AS "+columnNamePair[index][0];
		}
		if(Aggregate.DCOUNT.toString().equals(optName)){
			return Aggregate.COUNT+"("+Aggregate.DISTINCT + " " + columnNamePair[index][1]+")" +" AS "+ Aggregate.COUNT+"_"+columnNamePair[index][0]+"" ;
		}
		return optName+"("+ columnNamePair[index][1]+")" + " AS "+ optName+"_"+columnNamePair[index][0]+"" ;
	}
}
