package eql.model;

import eql.util.DateUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class RelativeTime {

    private static final String Y = "y";// year
    private static final String Q = "q";// quarter
    private static final String M = "m";// month
    private static final String W = "w";// week
    private static final String D = "d";// day
    private static final String H = "h";// hour

    private static final DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:s");

    public static Date getLastDate(String rel, boolean isEnd) {
        Calendar c = Calendar.getInstance();
        int[] ps = getParam(rel);
        c.add(ps[0], ps[1] * -1);
        return extendTime(c.getTime(), rel, isEnd);
    }

    private static Date extendTime(Date date, String rel, boolean isEnd) {
        String type = getType(rel);
        if (Y.equals(type)) {
            return isEnd ? DateUtils.yearEnd(date) : DateUtils.yearStart(date);
        } else if (Q.equals(type)) {
            return isEnd ? DateUtils.seasonEnd(date) : DateUtils.seasonStart(date);
        } else if (M.equals(type)) {
            return isEnd ? DateUtils.monthEnd(date) : DateUtils.monthStart(date);
        } else if (W.equals(type)) {
            return isEnd ? DateUtils.weekEnd(date) : DateUtils.weekStart(date);
        } else if (D.equals(type)) {
            return isEnd ? DateUtils.dayEnd(date) : DateUtils.dayStart(date);
        } else {
            return date;
        }
    }

    private static String getType(String rel) {
        if (rel.endsWith(Y)) {
            return Y;
        } else if (rel.endsWith(Q)) {
            return Q;
        } else if (rel.endsWith(M)) {
            return M;
        } else if (rel.endsWith(W)) {
            return W;
        } else if (rel.endsWith(H)) {
            return H;
        } else {
            return D;
        }
    }

    public static String getLastStr(String rel, boolean isEnd) {
        Date date = getLastDate(rel, isEnd);
        return fmt.format(date);
    }

    public static int[] getParam(String rel) {//TODO 如果不正确，在这里修改
        int[] ps = new int[3];
        int type = -1;
        int mount = -1;
        if (rel.endsWith(Y)) {
            type = Calendar.YEAR;
            String ymount = rel.replace(Y, "");
            mount = Integer.parseInt(ymount);
        } else if (rel.endsWith(Q)) {
            type = Calendar.MONTH;
            String qmount = rel.replace(Q, "");
            int quarter = Integer.parseInt(qmount);
            mount = quarter * 3;
        } else if (rel.endsWith(M)) {
            type = Calendar.MONTH;
            String mmount = rel.replace(M, "");
            mount = Integer.parseInt(mmount);
        } else if (rel.endsWith(W)) {
            type = Calendar.WEEK_OF_YEAR;
            String wmount = rel.replace(W, "");
            mount = Integer.parseInt(wmount);
        } else if (rel.endsWith(D)) {
            type = Calendar.DAY_OF_YEAR;
            String dmount = rel.replace(D, "");
            mount = Integer.parseInt(dmount);
        } else if (rel.endsWith(H)) {
            type = Calendar.HOUR_OF_DAY;
            String hmount = rel.replace(H, "");
            mount = Integer.parseInt(hmount);
        }
        ps[0] = type;
        ps[1] = mount;
        return ps;
    }

    /**
     * 按照年， 月， 季度，周，进行分组
     *
     * @param date
     * @param type
     * @return
     */
    public static Object groupDate(Date date, String type) {
        if (type != null) {
            int year = DateUtils.getByField(date, Calendar.YEAR);
            int month = DateUtils.getByField(date, Calendar.MONTH) + 1;
            if (Y.equals(type)) {
                return year;
            } else if (Q.equals(type)) {
                int season = DateUtils.getSeasonByMonth(month);
                return year + "-" + season + "Q";
            } else if (M.equals(type)) {
                return year + "-" + month;
            } else if (W.equals(type)) {
                int week = DateUtils.getByField(date, Calendar.WEEK_OF_YEAR);
                return week + "W";
            }
        }
        return date;
    }

    public static void main(String[] args) {
        String s = getLastStr("2q", false);
        System.out.println(s);
    }
}
