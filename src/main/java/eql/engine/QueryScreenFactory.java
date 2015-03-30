package eql.engine;


import eql.db.Vendor;

public class QueryScreenFactory {

    /**
     * 获得相应的 QUEYR SCREEN
     *
     * @param vendor
     * @param nest
     * @return
     */
    public static QueryScreen getQueryScreen(String vendor, boolean nest) {
        if (Vendor.MYSQL.equals(vendor)) {
            return getDefaultQueryScreen(nest);
        } else if (Vendor.ORACLE.equals(vendor)) {
            return getDefaultQueryScreen(nest);
        } else {// TODO
            return getDefaultQueryScreen(nest);
        }
    }

    public static QueryScreen getDefaultQueryScreen(boolean nest) {
        if (!nest) {
            return new NoNestQueryScreenDefaultImpl();
        } else {
            return new QueryScreenDefaultImpl();
        }

    }
}
