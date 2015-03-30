package eql.model;

public enum Aggregate {

	SUM("SUM"), COUNT("COUNT"), DCOUNT("DISTINCT COUNT"), AVG("AVG"), DISTINCT(
			"DISTINCT"),MAX("MAX"),MIN("MIN");
	private String name;

	private Aggregate(String name) {
		this.name = name;
	}

	public int code() {
		return this.ordinal() + 1;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public static String agt(int code) {
		int ordi = code - 1;
		String name = null;
		switch (ordi) {
		case 0:
			name = SUM.toString();
			break;
		case 1:
			name = COUNT.toString();
			break;
		case 2:
			name = DCOUNT.toString();
			break;
		case 3:
			name = AVG.toString();
			break;
		case 4:
			name = DISTINCT.toString();
			break;
		default:
			;
		}
		return name;
	}

	public static void main(String[] args) {
		System.out.println(SUM.code() + ":" + SUM);
		System.out.println(COUNT.code() + ":" + COUNT);
		System.out.println(DCOUNT.code() + ":" + DCOUNT);
		System.out.println(AVG.code() + ":" + AVG);
	}
}
