package BP.DA;

import java.sql.Connection;

/**
 * DBProcedure 的摘要说明。
 */
public class DBProcedure
{
	// 不带有参数的 Para .
	/**
	 * 运行存储过程,没有Para。 返回影响的行
	 */
	
	public static Object RunSPReturnObj(String spName, Connection conn)
	{
		// OracleCommand cmd = new OracleCommand(spName, conn);
		// cmd.CommandType = CommandType.StoredProcedure;
		// if (conn.State == System.Data.ConnectionState.Closed)
		// {
		// conn.Open();
		// }
		//
		// return cmd.ExecuteScalar();
		return null;
	}
	
	public static int RunSPReturnInt(String spName, Connection conn)
	{
		// Object obj = DBProcedure.RunSPReturnObj(spName, conn);
		// if (obj == null || obj == DBNull.getValue())
		// {
		// throw new RuntimeException("@SpName 错误：" + spName + ",返回 null 值。");
		// }
		// return Integer.parseInt(obj.toString());
		return 0;
	}
	
	public static float RunSPReturnFloat(String spName, Connection conn)
	{
		return Float.parseFloat((new Float(DBProcedure.RunSPReturnFloat(spName,
				conn))).toString());
	}
	
	// 带有参数的 DBProcedure
	/**
	 * 运行存储过程,有Para。返回影响的行。
	 * 
	 * @param spName
	 * @param conn
	 * @param paras
	 */
	public static int RunSP(String spName, Paras paras, Connection conn)
	{
		// if (conn.getState() != ConnectionState.Open)
		// {
		// conn.Open();
		// }
		//
		// IfxCommand cmd = new IfxCommand(spName, conn);
		// cmd.setCommandType(CommandType.StoredProcedure);
		//
		// // 加入参数
		// for (Para para : paras)
		// {
		// IfxParameter myParameter = new IfxParameter(para.ParaName, para.val);
		// myParameter.setSize(para.Size);
		// cmd.getParameters().Add(myParameter);
		// }
		//
		// int i = cmd.ExecuteNonQuery();
		// conn.Close();
		// return i;
		return 0;
	}
	
	public static int RunSP(String spName, Paras paras)
	{
		// switch (DBAccess.getAppCenterDBType())
		// {
		// case MSSQL:
		// Connection conn = new Connection(SystemConfig.getAppCenterDSN());
		// if (conn.State != ConnectionState.Open)
		// {
		// conn.Open();
		// }
		// return DBProcedure.RunSP(spName, paras, conn);
		// break;
		// case Informix:
		// Connection conn1 = new Connection(SystemConfig.getAppCenterDSN());
		// if (conn1.getState() != ConnectionState.Open)
		// {
		// conn1.Open();
		// }
		// return DBProcedure.RunSP(spName, paras, conn1);
		// break;
		// default:
		// throw new RuntimeException("尚未处理。");
		// break;
		// }
		return 0;
	}
	
	// 运行存储过程返回 DataTable 不带有参数
	// public static DataTable RunSPReturnDataTable(string spName )
	// {
	// if (DBAccess.AppCenterDBType==DBType.MSSQL)
	// return DBProcedure.RunSPReturnDataTable(spName, new
	// Paras(),(Connection)DBAccess.GetAppCenterDBConn );
	// else
	// return DBProcedure.RunSPReturnDataTable(spName,new
	// Paras(),(Connection)DBAccess.GetAppCenterDBConn );
	// }
	/**
	 * 运行存储过程返回Table
	 * 
	 * @param spName
	 *            存储过程名称
	 * @return 执行后的Table
	 */
	public static DataTable RunSPReturnDataTable(String spName, Connection conn)
	{
		Paras ens = new Paras();
		return DBProcedure.RunSPReturnDataTable(spName, ens, conn);
	}
	
	/**
	 * 运行存储过程返回Table
	 * 
	 * @param spName
	 *            存储过程名称
	 * @param paras
	 *            参说集合
	 * @return 执行后的Table
	 */
	public static DataTable RunSPReturnDataTable(String spName, Paras paras)
	{
		// if (DBAccess.getAppCenterDBType()==DBType.MSSQL)
		// {
		// return DBProcedure.RunSPReturnDataTable(spName, paras,
		// (Connection)DBAccess.getGetAppCenterDBConn());
		// }
		// else
		// {
		// return DBProcedure.RunSPReturnDataTable(spName, paras,
		// (Connection)DBAccess.getGetAppCenterDBConn());
		// }
		return null;
	}
	
	public static DataTable RunSPReturnDataTable(String spName, Paras paras,
			Connection conn)
	{
		
		// OracleCommand salesCMD = new OracleCommand(spName, conn);
		// salesCMD.CommandType = CommandType.StoredProcedure;
		//
		// /** 加上他们的para
		// */
		// for (Para para : paras)
		// {
		// OracleParameter myParm =
		// salesCMD.Parameters.AddWithValue(para.ParaName, para.DAType);
		// myParm.setValue(para.val);
		// }
		//
		// //selectCMD.CommandTimeout =60;
		// OracleDataAdapter sda = new OracleDataAdapter(salesCMD);
		// //SqlDataAdapter sda = new SqlDataAdapter(salesCMD);
		// if (conn.State == System.Data.ConnectionState.Closed)
		// {
		// conn.Open();
		// }
		// DataTable dt = new DataTable();
		// sda.Fill(dt);
		// sda.dispose();
		// return dt;
		return null;
	}
	/**
	 * 运行存储过程返回Table。
	 * 
	 * @param spName
	 * @param paras
	 * @param conn
	 * @return
	 */
}