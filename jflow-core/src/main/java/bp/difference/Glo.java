package bp.difference;

import bp.da.AtPara;

public class Glo {

    public static String DealSQLStringEnumFormat(String cfgString) {
        //把这个string,转化SQL. @tuanyuan=团员@dangyuan=党员
        AtPara ap = new AtPara(cfgString);
        String sql = "";
        for(String item : ap.getHisHT().keySet())
        {
            sql += " SELECT '" + item + "' as No, '" + ap.GetValStrByKey(item) + "' as Name FROM Port_Emp WHERE No = 'admin' UNION ";
        }
        sql = sql.substring(0, sql.length() - 6);
        return sql;
    }

    /**
     * 获取的是本机IP
     *
     * @return
     * @throws Exception
     */
    public static String getGetIP() {
        return bp.sys.base.Glo.getRequest().getLocalAddr();
    }

    public static String getRemotIP() {
        return bp.sys.base.Glo.getRequest().getRemoteAddr();
    }
}
