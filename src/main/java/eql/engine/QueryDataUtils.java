package eql.engine;

import eql.db.DbColumn;
import eql.db.DbTypeName;
import eql.db.QueryData;
import eql.model.Filter;
import eql.model.Operator;
import eql.model.RelativeTime;
import eql.model.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class QueryDataUtils {

    /**
     * 将两个表格按照表格中相同的维度进行合并为一个大表格
     *
     * @param qdList
     * @param groupbyLength
     * @return
     */
    public static QueryData mergeColumn(List<QueryData> qdList,
                                        int groupbyLength) {
        QueryData result = new QueryData();
        List<DbColumn> dbColumns = new ArrayList<DbColumn>();
        List<List<Object>> dataList = new ArrayList<List<Object>>();
        Map<Object, Object> titleMap = new HashMap<Object, Object>();
        for (int i = 0; i < qdList.size(); i++) {
            QueryData p = qdList.get(i);
            DbColumn[] titleRow = p.getDbColumns();
            for (int j = 0; j < titleRow.length; j++) {
                DbColumn column = titleRow[j];
                String title = column.getColumnName();
                if (titleMap.containsKey(title)) {

                } else {
                    titleMap.put(title, title);
                    dbColumns.add(column);
                    // 数据填充
                    Object[] datas = p.getDatas();
                    int size = datas.length;
                    for (int k = 0; k < size; k++) {
                        Object[] nowrow = (Object[]) datas[k];
                        getCompareList(dataList, nowrow, j, groupbyLength,
                                dbColumns.size());
                    }
                }
            }
        }
        result.setDbColumns(dbColumns.toArray(new DbColumn[0]));
        List<Object[]> rowList = new ArrayList<Object[]>();
        for (List<Object> listObj : dataList) {
            Object[] row = listObj.toArray(new Object[0]);
            rowList.add(row);
        }
        result.setDatas(rowList.toArray(new Object[0]));
        return result;
    }

    public static void changeColumnsIndex(DbColumn[] dbs,
                                          List<Integer> selectedColumnIndex) {
        for (int i = 0; i < dbs.length; i++) {
            DbColumn dbc = dbs[i];
            dbc.setIndex(selectedColumnIndex.get(i));
        }
    }

    /**
     * 以维度列做为key，对维度相同的行进行合并，合并行的指标列进行相加
     *
     * @param qd
     * @param groupfilters
     * @param groupLength
     * @return
     */
    public static QueryData mergeRow(QueryData qd, Filter[] groupfilters,
                                     int groupLength) {
        if (groupfilters == null) {
            return qd;
        }
        QueryData result = new QueryData();
        Object[] datas = qd.getDatas();
        DbColumn[] dbcs = qd.getDbColumns();
        if (dbcs == null || dbcs.length == 0) {
            return qd;
        }
        result.setDbColumns(dbcs);
        /**
         * 构造表格列索引为key，这个列上存在的filter为VALUE的map
         */
        Map<Integer, List<Filter>> indexFilterMap = new HashMap<Integer, List<Filter>>();
        for (Filter filter : groupfilters) {
            int colIndex = idx2Index(filter.getIdx(), dbcs);
            if (!indexFilterMap.containsKey(colIndex)) {
                List<Filter> filterList = new ArrayList<Filter>();
                filterList.add(filter);
                indexFilterMap.put(colIndex, filterList);
            } else {
                List<Filter> filterList = indexFilterMap.get(colIndex);
                filterList.add(filter);
            }
        }
        /**
         * 替换行中分组后的名称 例如：城市 pv 北京 2313 上海 452345 广州 25245
         * filter是对城市分组，将北京，上海，广州做为‘北上广’一组，则： 城市 pv 北上广 2313 北上广 452345 北上广
         * 25245
         */
        for (int i = 0; i < datas.length; i++) {
            Object[] row = (Object[]) datas[i];
            for (Map.Entry<Integer, List<Filter>> entry : indexFilterMap
                    .entrySet()) {
                Object data = row[entry.getKey()];
                row[entry.getKey()] = groupData(data, entry.getValue());
            }
        }
        /**
         * 合并维度相同的行 综上： 城市 pv 北上广 479903 注：（479903=2313+452345+25245）
         */
        Map<String, Object[]> uniMap = new LinkedHashMap<String, Object[]>();
        for (int i = 0; i < datas.length; i++) {
            Object[] row = (Object[]) datas[i];
            String key = getGroupKey(row, groupLength);
            if (uniMap.containsKey(key)) {
                Object[] existRow = uniMap.get(key);
                fillGroupValue(existRow, row, groupLength);
            } else {
                uniMap.put(key, row);
            }
        }
        List<Object[]> dataList = new ArrayList<Object[]>();
        for (Map.Entry<String, Object[]> entry : uniMap.entrySet()) {
            dataList.add(entry.getValue());
        }
        result.setDatas(dataList.toArray(new Object[0]));
        return result;
    }

    private static String getGroupKey(Object[] row, int length) {
        String key = "";
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                key = row[i].toString();
            } else {
                key = key + "_" + row[i].toString();
            }
        }
        return key;
    }

    private static void fillGroupValue(Object[] existRow, Object[] row,
                                       int length) {
        for (int i = length; i < existRow.length; i++) {
            Object existObj = existRow[i];
            Object obj = row[i];
            if (existObj == null && obj != null) {
                existRow[i] = obj;
            } else if (existObj != null && obj != null) {
                Double existDouble = Double.parseDouble(existObj.toString());
                Double objDouble = Double.parseDouble(obj.toString());
                existRow[i] = existDouble + objDouble;
            }
        }
    }

    /**
     * 对数据分组，只对非数值的进行 in操作分组 对数值的进行between操作
     *
     * @param data
     * @param filterList
     * @return
     */
    private static Object groupData(Object data, List<Filter> filterList) {
        Object result = data;
        if (filterList != null) {
            for (int i = 0; i < filterList.size(); i++) {
                Filter filter = filterList.get(i);
                Integer opt = filter.getOpt();
                Object[] values = filter.getVls();
                // 只有in 和between两种形式
                if (opt != null && opt == Operator.IN.code()) {
                    for (Object obj : values) {
                        if (obj.toString().equals(data.toString())) {
                            return filter.getGpn();
                        }
                    }
                } else if (opt != null && opt == Operator.BEWTEEN.code()) {// 只符合数值类型的，时间的不适用
                    if (isBewteen(data, values[0], values[1])) {
                        return filter.getGpn();
                    }
                } else if (opt != null && opt == Operator.LAST.code()) {
                    // TODO
                } else if (opt != null && opt == Operator.DG.code()) {
                    //时间类型的分组处理
                    return RelativeTime.groupDate((Date) data, filter.getGpn());
                }
            }
        }
        return result;
    }

    private static boolean isBewteen(Object data, Object start, Object end) {
        if (data == null || start == null || end == null) {
            return false;
        }
        double datad = -1;
        double startd = -1;
        double endd = -1;
        if (data instanceof Date) {
            datad = ((Date) data).getTime();
            startd = ((Date) start).getTime();
            endd = ((Date) end).getTime();
        } else {
            datad = Double.parseDouble(data.toString());
            startd = Double.parseDouble(start.toString());
            endd = Double.parseDouble(end.toString());
        }
        return (datad >= startd && datad <= endd);
    }

    /**
     * 与getPosition方法一样
     *
     * @param idx
     * @param dbcs
     * @return
     */
    private static Integer idx2Index(int idx, DbColumn[] dbcs) {
        for (int i = 0; i < dbcs.length; i++) {
            DbColumn dbc = dbcs[i];
            if (idx == dbc.getIndex()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 填充当前列的数据 在datalist中查找该行是否可以加入到原有行中（以当前维度做为key）
     * 如果没有则在datalist中新添加一行，并保证列相互对应
     *
     * @param dataList    最终结果保存
     * @param nowRow
     * @param nowrowIndex
     * @param indexLength
     * @param columnSize
     * @return
     */
    private static List<Object> getCompareList(List<List<Object>> dataList,
                                               Object[] nowRow, int nowrowIndex, int indexLength, int columnSize) {
        List<Object> list = null;
        if (dataList.size() == 0) {
            list = new ArrayList<Object>();
            list.add(nowRow[nowrowIndex]);
            dataList.add(list);
            return list;
        } else {
            String key = "";
            int keyLen = (dataList.size() < indexLength ? dataList.size()
                    : indexLength);
            for (int i = 0; i < keyLen; i++) {
                String nowr = nowRow[i] == null ? "" : nowRow[i].toString();
                if (i == 0) {
                    key = nowr;
                } else {
                    key = key + "_" + nowr;
                }
            }
            int rowIndex = -1;
            List<List<Object>> dataListClone = dataList;
            for (int j = 0; j < dataListClone.size(); j++) {
                List<Object> objList = dataListClone.get(j);
                int size = objList.size();
                String key1 = "";
                for (int i = 0; i < indexLength && i < size; i++) {
                    String nowr1 = objList.get(i) == null ? "" : objList.get(i)
                            .toString();
                    if (i == 0) {
                        key1 = nowr1;
                    } else {
                        key1 = key1 + "_" + nowr1;
                    }
                }
                if (key.equals(key1)) {
                    rowIndex = j;
                    break;
                }
            }
            if (rowIndex != -1) {
                for (int m = 0; m < dataList.size(); m++) {
                    List<Object> mlist = dataList.get(m);
                    if (rowIndex == m) {
                        if (mlist.size() < columnSize - 1) {
                            for (int n = mlist.size(); n < columnSize - 1; n++) {
                                mlist.add(null);
                            }
                        }
                        mlist.add(columnSize - 1, nowRow[nowrowIndex]);
                    } else {
                        if (mlist.size() < columnSize) {
                            for (int n = mlist.size(); n < columnSize; n++) {
                                mlist.add(n, null);
                            }
                        }
                    }
                }
            } else if (rowIndex == -1) {
                list = new ArrayList<Object>();
                for (int i = 0; i < indexLength; i++) {
                    list.add(nowRow[i]);
                }
                for (int i = indexLength; i < columnSize; i++) {
                    if (i == columnSize - 1) {
                        list.add(nowRow[nowrowIndex]);
                    } else {
                        list.add(null);
                    }
                }
                dataList.add(list);
            }
        }
        return list;
    }

    public static void changeName(DbColumn[] dbColumns, String groupName,
                                  int groupLength) {
        String suffix = "";
        if (groupName != null && !"".equals(groupName)) {
            suffix = groupName;
        }
        if (!"".equals(suffix)) {
            suffix = "(" + suffix + ")";
        }

        for (int i = groupLength; i < dbColumns.length; i++) {
            DbColumn column = dbColumns[i];
            column.setColumnName(column.getColumnName() + suffix);
        }
    }

    public static String ary2Str(int[] objAry) {
        String result = null;
        for (int obj : objAry) {
            if (result == null) {
                result = obj + "";
            } else {
                result = result + "," + obj;
            }
        }
        return result;
    }

    /**
     * 目前只有对时间的排序
     *
     * @param qd
     * @param sorts
     */
    public static void sortQueryData(QueryData qd, Sort[] sorts) {

        if (sorts != null && sorts.length > 0) {
            if (qd == null || qd.getDbColumns() == null) {
                return;
            }
            DbColumn[] dbcs = qd.getDbColumns();
            for (Sort sort : sorts) {
                final int position = getPosition(dbcs, sort.getIdx());
                if (position == -1) {
                    continue;
                }
                DbColumn dbc = dbcs[position];
                if (!DbTypeName.isDate(dbc.getDbType())) {
                    continue;//只对时间排序
                }
                final int flag = sort.getSort() == Sort.ASC ? 1 : -1;
                Arrays.sort(qd.getDatas(), new Comparator<Object>() {
                    public int compare(Object row1, Object row2) {
                        Object obj1 = ((Object[]) row1)[position];
                        Object obj2 = ((Object[]) row2)[position];
                        return ((Date) obj1).compareTo((Date) obj2) * flag;
                    }
                });
            }
        }
    }

    /**
     * 计算index在列数组中的位置索引
     *
     * @param dbcs
     * @param index
     * @return
     */
    public static int getPosition(DbColumn[] dbcs, int index) {
        int position = -1;
        if (dbcs == null || dbcs.length == 0) {
            return position;
        }
        for (int i = 0; i < dbcs.length; i++) {
            if (index == dbcs[i].getIndex()) {
                position = i;
                break;
            }
        }
        return position;
    }

    public static void main(String[] args) {
        int[] test = new int[]{2, 3, 4};
        List<String> list = new ArrayList<String>();
        list.add("zhangdekun");
        list.add("hello");
        list.add(2, "add");
        list.add(2, "add2");
        System.out.println(list.size());
        System.out.println(list.size());
        System.out.println(ary2Str(test));
    }
}
