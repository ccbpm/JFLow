package bp.port;

import bp.da.DBAccess;
import bp.da.DataRow;
import bp.da.DataTable;

public class Glo
{
	/**
	 根据部门编号s，获得该部门下的人员编号s

	 @param depts 部门编号s
	 @return 人员编号,格式为:zhangsan,lisi,wangwu
	 */
	public static String GenerEmpNosByDeptNos(String depts)
	{
		if (bp.da.DataType.IsNullOrEmpty(depts) == true)
		{
			return "";
		}

		String sql = "SELECT No FROM Port_Emp WHERE FK_Dept IN (" + GenerWhereInSQL(depts) + ")";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		String strs = "";
		for (DataRow dr : dt.Rows)
		{
			strs += dr.get(0) + ",";
		}
		return strs;
	}
	/**
	 根据岗位编号s，获得该岗位下的人员编号s

	 @param stationNos 岗位编号s
	 @return 人员编号,格式为:zhangsan,lisi,wangwu
	 */
	public static String GenerEmpNosByStationNos(String stationNos)
	{
		if (bp.da.DataType.IsNullOrEmpty(stationNos) == true)
		{
			return "";
		}

		String sql = "SELECT FK_Emp FROM Port_DeptEmpStation WHERE FK_Station IN (" + GenerWhereInSQL(stationNos) + ")";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		String strs = "";
		for (DataRow dr : dt.Rows)
		{
			strs += dr.get(0) + ",";
		}
		return strs;
	}
	/**
	 格式化SQL

	 @param ids 001,002,003,
	 @return '001','002','003'
	 */
	public static String GenerWhereInSQL(String ids)
	{
		if (bp.da.DataType.IsNullOrEmpty(ids) == true)
		{
			return "";
		}
		if (ids.substring(0, 1).equals(",") == true)
		{
			ids = ids.substring(0);
		}

		String str = "";
		String[] strs = ids.split("[,]", -1);
		for (String s : strs)
		{
			str += ",'" + s + "'";
		}
		return str.substring(1);
	}
}