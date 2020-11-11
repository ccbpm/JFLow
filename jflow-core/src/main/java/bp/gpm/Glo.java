package bp.gpm;

import bp.sys.*;
import bp.sys.xml.EnumInfoXml;
import bp.sys.xml.EnumInfoXmls;
import bp.da.*;
import bp.difference.SystemConfig;
import bp.en.*;
import java.util.*;

public class Glo
{
	/** 
	 是否允许用户注册.
	*/
	public static boolean getIsEnableRegUser()
	{
		Object str = SystemConfig.getAppSettings().get("IsEnableRegUser");
		if (DataType.IsNullOrEmpty(str) == true)
		{
			return false;
		}

		if (str.toString().equals("1") == true)
		{
			return true;
		}

		return false;
	}
	/** 
	 运行模式
	*/
	public static OSModel getOSModel()
	{
		return getOSModel().OneMore;

	}
	/** 
	 钉钉是否启用
	*/
	public static boolean getIsEnableDingDing()
	{
			//如果两个参数都不为空说明启用
		String corpid = SystemConfig.getDing_CorpID();
		String corpsecret = SystemConfig.getDing_CorpSecret();
		if (DataType.IsNullOrEmpty(corpid) || DataType.IsNullOrEmpty(corpsecret))
		{
			return false;
		}
		return true;
	}
	/** 
	 微信是否启用
	*/
	public static boolean getIsEnableWeiXin()
	{
			//如果两个参数都不为空说明启用
		String corpid = SystemConfig.getWX_CorpID();
		String corpsecret = SystemConfig.getWX_AppSecret();
		if (DataType.IsNullOrEmpty(corpid) || DataType.IsNullOrEmpty(corpsecret))
		{
			return false;
		}

		return true;
	}
	/** 
	 安装包
	 * @throws Exception 
	*/
	public static void DoInstallDataBase() throws Exception
	{
		ArrayList al = null;
		String info = "bp.en.Entity";
		al =  bp.en.ClassFactory.GetObjects(info);


			///1, 修复表
		for (Object obj : al)
		{
			Entity en = null;
			en = obj instanceof Entity ? (Entity)obj : null;
			if (en == null)
			{
				continue;
			}

			if (en.toString() == null)
			{
				continue;
			}

			if (en.toString().contains("bp.port."))
			{
				continue;
			}

			if (en.toString().contains("bp.gpm.") == false)
			{
				continue;
			}

			String table = null;
			try
			{
				table = en.getEnMap().getPhysicsTable();
				if (table == null)
				{
					continue;
				}
				if (table.toLowerCase().contains("demo_") == true)
				{
					continue;
				}
			}
			catch (java.lang.Exception e)
			{
				continue;
			}

			switch (table)
			{
				case "WF_EmpWorks":
				case "WF_GenerEmpWorkDtls":
				case "WF_GenerEmpWorks":
					continue;
				case "Sys_Enum":
					en.CheckPhysicsTable();
					break;
				default:
					en.CheckPhysicsTable();
					break;
			}

			en.setPKVal("123");
			try
			{
				en.RetrieveFromDBSources();
			}
			catch (RuntimeException ex)
			{
				Log.DebugWriteWarning(ex.getMessage());
				en.CheckPhysicsTable();
			}
		}

			/// 修复


			///2, 注册枚举类型 sql
		// 2, 注册枚举类型。
		EnumInfoXmls xmls = new EnumInfoXmls();
		xmls.RetrieveAll();
		for (EnumInfoXml xml : xmls.ToJavaList())
		{
			SysEnums ses = new SysEnums();
			ses.RegIt(xml.getKey(), xml.getVals());
		}

			/// 注册枚举类型


			///3, 执行基本的 sql
		String sqlscript = SystemConfig.getPathOfWebApp() + "/GPM/SQLScript/Port_Inc_CH_BPM.sql";
		DBAccess.RunSQLScript(sqlscript);

			/// 修复


			///5, 初始化数据。
		if (DBAccess.IsExitsObject("GPM_AppSort") == true)
		{
			sqlscript = SystemConfig.getPathOfWebApp() + "/GPM/SQLScript/InitPublicData.sql";
			DBAccess.RunSQLScript(sqlscript);
		}

			/// 初始化数据


			///6, 创建视图。
		sqlscript = SystemConfig.getPathOfWebApp() + "/GPM/SQLScript/MSSQL_GPM_VIEW.sql";

		//MySQL 语法有所区别
		if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
		{
			sqlscript = SystemConfig.getPathOfWebApp() + "/GPM/SQLScript/MySQL_GPM_VIEW.sql";
		}

		//Oracle 语法有所区别
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			sqlscript = SystemConfig.getPathOfWebApp() + "/GPM/SQLScript/Oracle_GPM_VIEW.sql";
		}

		DBAccess.RunSQLScriptGo(sqlscript);

			/// 创建视图


		//处理全路径
		Depts depts = new Depts();
		depts.RetrieveAll();
		for (Dept dept : depts.ToJavaList())
		{
			dept.GenerNameOfPath();
		}

	}
	/** 
	 是否可以执行判断
	 
	 @param obj 判断对象
	 @param cw 方式
	 @return 是否可以执行
	*/
	public static boolean IsCanDoIt(String ctrlObj, bp.gpm.CtrlWay cw, String empID)
	{
		int n = 0;
		String sql = "";
		switch (cw)
		{
			case AnyOne:
				return true;
			case ByStation:
				sql = "SELECT count(*) FROM GPM_ByStation WHERE RefObj='" + ctrlObj + "'  AND FK_Station IN (select FK_Station from  Port_DeptEmpStation WHERE FK_Emp='" + empID + "')";
				n = DBAccess.RunSQLReturnValInt(sql, 0);
				break;
			case ByDept:
				sql = "SELECT count(*) FROM GPM_ByDept WHERE RefObj='" + ctrlObj + "'  AND FK_Dept IN (SELECT FK_Dept FROM Port_Emp WHERE No='" + empID + "')";
				n = DBAccess.RunSQLReturnValInt(sql, 0);
				break;
			case ByEmp:
				sql = "SELECT count(*) FROM GPM_ByEmp WHERE RefObj='" + ctrlObj + "'  AND  FK_Emp='" + empID + "'";
				n = DBAccess.RunSQLReturnValInt(sql, 0);
				break;
			default:
				break;
		}

		if (n == 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
}