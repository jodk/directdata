package eql.engine;


import eql.model.DataType;
import eql.model.Operator;
import eql.model.RelativeTime;

public class NoNestUnitOpt {

	private boolean isRelative = false;
	private Integer index;
	private String optName;
	private String dtName ;
	private Object[] values;
	private String[][] columnNamePair;

	public NoNestUnitOpt(String[][] columnNamePair,Integer columnIndex, int opt, Integer dt,Object[] values) {
		this.index = columnIndex;
		this.columnNamePair = columnNamePair;
		this.optName = Operator.opt(opt);
		if(dt !=null){
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

	private boolean isDateType(){
		if(this.dtName !=null){
			return DataType.DATE.toString().equals(this.dtName);
		}
		return false;
	}
	public String getUnit() {
		try{
			if (Operator.IN.toString().equals(optName) || Operator.NN.toString().equals(optName)) {
				String vs = "''";
				if (values != null) {
					for (Object obj : values) {
						vs = vs + ",'" + obj + "'";
					}
				}
				return this.columnNamePair[index][1] + " " + optName + " (" + vs + ")";
			} else if (Operator.LAST.toString().equals(optName)) {
				if(values !=null){
					if(values.length>1){
						return this.columnNamePair[index][1] + " between '"+ RelativeTime.getLastStr(values[0].toString(), false) + "' and '"+RelativeTime.getLastStr(values[1].toString(),true)+"'";
					}else{
						return this.columnNamePair[index][1]+">='"+RelativeTime.getLastStr(values[0].toString(),false)+"'";
					}
				}
				return "";
			} else if(Operator.BEWTEEN.toString().equals(optName)){
				if(isDateType()){
					return this.columnNamePair[index][1]+">='" + values[0] + "' and "+this.columnNamePair[index][1] +"<='"+values[1]+"'";
				}else{
					return this.columnNamePair[index][1]+">=" + values[0] + " and "+this.columnNamePair[index][1] +"<="+values[1];
				}
			}{
				if(isDateType()){
					return this.columnNamePair[index][1] + optName +"'"+ values[0]+"'";
				}else{
					return this.columnNamePair[index][1] + optName + values[0];
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	
	}
}
