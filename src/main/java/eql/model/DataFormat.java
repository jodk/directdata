package eql.model;

import eql.db.DbColumn;
import eql.db.DbTypeName;
import eql.db.QueryData;
import eql.util.DateUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataFormat {


    private static void fmt(QueryData qd, Map<Integer, Format> fmtMap) {
        DbColumn[] dbcs = qd.getDbColumns();
        if (dbcs == null) {
            return;
        }
        List<Integer> positionList = new ArrayList<Integer>();
        List<Integer> fmtTypeList = new ArrayList<Integer>();
        List<String> valueList = new ArrayList<String>();
        for (int i = 0; i < dbcs.length; i++) {
            DbColumn dbc = dbcs[i];
            int index = dbc.getIndex();
            if (fmtMap.containsKey(index)) {
                Format fmt = fmtMap.get(index);
                positionList.add(i);
                fmtTypeList.add(fmt.getFmt());
                valueList.add(fmt.getVls()[0].toString());
            }
        }
        Object[] datas = qd.getDatas();
        for (int i = 0; i < datas.length; i++) {
            Object[] row = (Object[]) datas[i];
            for (int j = 0; j < positionList.size(); j++) {
                int colIndex = positionList.get(j);
                Object obj = row[colIndex];
                try {
                    if (fmtTypeList.get(j) == Formatting.DATEFMT.code()) {
                        row[colIndex] = DateUtils.format((Date) obj, valueList.get(j));
                    } else if (fmtTypeList.get(j) == Formatting.SIGN.code()) {
                        row[colIndex] = degree(obj, Integer.parseInt(valueList.get(j)));
                    }
                } catch (Exception e) {
                    //转换出错，原样输出
                }

            }
        }
    }

    public static void fmt(QueryData qd, List<Format> fmtList) {
        try {
            fmt(qd, getFormat(qd.getDbColumns(), fmtList));
        } catch (Exception e) {
            //出现错误则不格式化
        }
    }

    private static Map<Integer, Format> getFormat(DbColumn[] dbcs, List<Format> fmtList) {
        Map<Integer, Format> fmtMap = new HashMap<Integer, Format>();
        if (dbcs == null) {
            return fmtMap;
        }
        for (DbColumn dbc : dbcs) {
            if (DbTypeName.isDate(dbc.getDbType())) {
                Format fmt = new Format();
                fmt.setFmt(Formatting.DATEFMT.code());
                fmt.setIdx(dbc.getIndex());
                fmt.setVls(new Object[]{"yyyy-MM-dd"});
                fmtMap.put(dbc.getIndex(), fmt);
            } else if (DbTypeName.isTime(dbc.getDbType())) {
                Format fmt = new Format();
                fmt.setFmt(Formatting.DATEFMT.code());
                fmt.setIdx(dbc.getIndex());
                fmt.setVls(new Object[]{"HH:mm:ss"});
                fmtMap.put(dbc.getIndex(), fmt);
            } else if (DbTypeName.isTimestamp(dbc.getDbType())) {
                Format fmt = new Format();
                fmt.setFmt(Formatting.DATEFMT.code());
                fmt.setIdx(dbc.getIndex());
                fmt.setVls(new Object[]{"yyyy-MM-dd HH:mm:ss"});
                fmtMap.put(dbc.getIndex(), fmt);
            }
        }
        if (fmtList != null) {
            for (Format fmt : fmtList) {
                fmtMap.put(fmt.getIdx(), fmt);
            }
        }
        return fmtMap;
    }

    public static Object degree(Object obj, Integer dot) {
        if (obj != null && dot != null) {
            double d = Double.parseDouble(obj.toString());
            BigDecimal bg = new BigDecimal(d);
            obj = bg.setScale(dot, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return obj;
    }
}
