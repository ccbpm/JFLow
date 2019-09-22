package BP.GPM;

import BP.DA.*;
import BP.En.*;
import BP.Web.*;
import BP.Sys.*;
import BP.Port.*;

/** 
 权限调用API
*/
public class Dev2Interface
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 菜单权限
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 菜单权限

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 登陆接口
	/** 
	 用户登陆,此方法是在开发者校验好用户名与密码后执行
	 
	 @param userNo 用户名
	 @param SID 安全ID,请参考流程设计器操作手册
	*/
	public static void Port_Login(String userNo, String sid)
	{
		if (BP.Sys.SystemConfig.OSDBSrc == OSDBSrc.Database)
		{
			String sql = "SELECT SID FROM Port_Emp WHERE No='" + userNo + "'";
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			if (dt.Rows.Count == 0)
			{
				throw new RuntimeException("用户不存在或者SID错误。");
			}

			if (!dt.Rows[0]["SID"].toString().equals(sid))
			{
				throw new RuntimeException("用户不存在或者SID错误。");
			}
		}

		BP.Port.Emp emp = new BP.Port.Emp(userNo);
		WebUser.SignInOfGener(emp);
		return;
	}
	/** 
	 用户登陆,此方法是在开发者校验好用户名与密码后执行
	 
	 @param userNo 用户名
	*/
	public static void Port_Login(String userNo)
	{
		BP.Port.Emp emp = new BP.Port.Emp(userNo);
		WebUser.SignInOfGener(emp);
		return;
	}
	/** 
	 注销当前登录
	*/
	public static void Port_SigOut()
	{
		WebUser.Exit();
	}
	/** 
	 获取未读的消息
	 用于消息提醒.
	 
	 @param userNo 用户ID
	*/
	public static String Port_SMSInfo(String userNo)
	{
		Paras ps = new Paras();
		ps.SQL = "SELECT MyPK, EmailTitle  FROM sys_sms WHERE SendToEmpID=" + SystemConfig.AppCenterDBVarStr + "SendToEmpID AND IsAlert=0";
		ps.Add("SendToEmpID", userNo);
		DataTable dt = DBAccess.RunSQLReturnTable(ps);
		String strs = "";
		for (DataRow dr : dt.Rows)
		{
			strs += "@" + dr.get(0) + "=" + dr.get(1).toString();
		}
		ps = new Paras();
		ps.SQL = "UPDATE  sys_sms SET IsAlert=1 WHERE  SendToEmpID=" + SystemConfig.AppCenterDBVarStr + "SendToEmpID AND IsAlert=0";
		ps.Add("SendToEmpID", userNo);
		DBAccess.RunSQL(ps);
		return strs;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 登陆接口

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region GPM接口
	/** 
	 获取一个操作人员对于一个系统的权限
	 
	 @param userNo 用户编号
	 @param app 系统编号
	 @return 结果集
	*/
	public static DataTable DB_Menus(String userNo, String app)
	{
		Paras ps = new Paras();
		ps.SQL = "SELECT * FROM GPM_EmpMenu WHERE FK_Emp=" + SystemConfig.AppCenterDBVarStr + "FK_Emp AND FK_App=" + SystemConfig.AppCenterDBVarStr + "FK_App ";
		ps.Add("FK_Emp", userNo);
		ps.Add("FK_App", app);
		return DBAccess.RunSQLReturnTable(ps);
	}
	/** 
	 获取一个操作人员对此应用可以访问的系统
	 
	 @param userNo 用户编号
	 @return 结果集
	*/
	public static DataTable DB_Apps(String userNo)
	{
		Paras ps = new Paras();
		ps.SQL = "SELECT * FROM GPM_EmpApp WHERE FK_Emp=" + SystemConfig.AppCenterDBVarStr + "FK_Emp ";
		ps.Add("FK_Emp", userNo);
		return DBAccess.RunSQLReturnTable(ps);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion GPM接口

}