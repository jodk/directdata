package eql.model;

public enum Operator {
	EQ("="), NE("<>"), LT("<"), GT(">"), LE("<="), GE(">="), IN("in"), NN(
			"not in"), LAST("last"), BEWTEEN("BEWTEEN"),DG("DATE GROUP");
	private String code;

	private Operator(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return this.code;
	}

	public int code() {
		return this.ordinal() + 1;
	}

	public static String opt(int code) {
		int ordi = code - 1;
		String name = "";
		switch (ordi) {
		case 0:
			name = EQ.toString();
			break;
		case 1:
			name = NE.toString();
			break;
		case 2:
			name = LT.toString();
			break;
		case 3:
			name = GT.toString();
			break;
		case 4:
			name = LE.toString();
			break;
		case 5:
			name = GE.toString();
			break;
		case 6:
			name = IN.toString();
			break;
		case 7:
			name = NN.toString();
			break;
		case 8:
			name = LAST.toString();
			break;
		case 9:
			name = BEWTEEN.toString();
			break;
		case 10:
			name = DG.toString();//时间分组
			break;
		default:
			;
		}
		return name;
	}

	public static void main(String[] args) {
		System.out.println(EQ.code() + ":" + EQ);
		System.out.println(NE.code() + ":" + NE);
		System.out.println(LT.code() + ":" + LT);
		System.out.println(GT.code() + ":" + GT);
		System.out.println(LE.code() + ":" + LE);
		System.out.println(GE.code() + ":" + GE);
		System.out.println(IN.code() + ":" + IN);
		System.out.println(NN.code() + ":" + NN);
		System.out.println(LAST.code() + ":" + LAST);
		System.out.println(IN.toString().equals("in"));
		System.out.println("in".equals(IN));
	}
}
