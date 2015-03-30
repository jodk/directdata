package eql.model;

public enum Formatting {

    SIGN("SIGN"),//保留几位有效数字
    DATEFMT("DATAFMT"),//时间格式化
    ;
    private String name;

    private Formatting(String name) {
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
                name = SIGN.toString();
                break;
            case 1:
                name = DATEFMT.toString();
                break;
            default:
                ;
        }
        return name;
    }

    public static void main(String[] args) {
    }
}
