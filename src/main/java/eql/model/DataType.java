package eql.model;

public enum DataType {
    NUMERIC("NUMERIC"),//数值类型
    DATE("DATE"),//时间类型
    STRING("STRING"),//字符类型
    ;
    private String name;

    private DataType(String name) {
        this.name = name;
    }

    public int code() {
        return this.ordinal() + 1;
    }

    public static String dt(int code) {
        int ordi = code - 1;
        String name = null;
        switch (ordi) {
            case 0:
                name = NUMERIC.toString();
                break;
            case 1:
                name = DATE.toString();
                break;
            default:
                ;
        }
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
