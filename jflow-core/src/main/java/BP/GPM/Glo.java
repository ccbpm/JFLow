package BP.GPM;

import BP.Sys.*;
import BP.DA.*;
import BP.En.*;
import BP.*;
import java.util.*;

public class Glo
{
	/** 
	 运行模式
	*/
	public static OSModel getOSModel()
	{
		return getOSModel().OneMore;
			//OSModel os = (OSModel)BP.Sys.SystemConfig.GetValByKeyInt("OSModel", 1);
			//return os;  
	}
	/** 
	 钉钉是否启用
	*/
	public static boolean getIsEnable_DingDing()
	{
			//如果两个参数都不为空说明启用
		String corpid = BP.Sys.SystemConfig.Ding_CorpID;
		String corpsecret = BP.Sys.SystemConfig.Ding_CorpSecret;
		if (DataType.IsNullOrEmpty(corpid) || DataType.IsNullOrEmpty(corpsecret))
		{
			return false;
		}
		return true;
	}
	/** 
	 微信是否启用
	*/
	public static boolean getIsEnable_WeiXin()
	{
			//如果两个参数都不为空说明启用
		String corpid = BP.Sys.SystemConfig.WX_CorpID;
		String corpsecret = BP.Sys.SystemConfig.WX_AppSecret;
		if (DataType.IsNullOrEmpty(corpid) || DataType.IsNullOrEmpty(corpsecret))
		{
			return false;
		}

		return true;
	}
	/** 
	 安装包
	*/
	public static void DoInstallDataBase()
	{
		ArrayList al = null;
		String info = "BP.En.Entity";
		al = BP.En.ClassFactory.GetObjects(info);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 1, 修复表
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

			if (en.toString().contains("BP.Port."))
			{
				continue;
			}

			if (en.toString().contains("BP.GPM.") == false)
			{
				continue;
			}

			//if (en.ToString().Contains("BP.GPM."))
			//    continue;
			//if (en.ToString().Contains("BP.Demo."))
			//    continue;

			String table = null;
			try
			{
				table = en.EnMap.PhysicsTable;
				if (table == null)
				{
					continue;
				}
				if (en.EnMap.PhysicsTable.toLowerCase().contains("demo_") == true)
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

			en.PKVal = "123";
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 修复

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 2, 注册枚举类型 sql
		// 2, 注册枚举类型。
		BP.Sys.XML.EnumInfoXmls xmls = new BP.Sys.XML.EnumInfoXmls();
		xmls.RetrieveAll();
		for (BP.Sys.XML.EnumInfoXml xml : xmls)
		{
			BP.Sys.SysEnums ses = new BP.Sys.SysEnums();
			ses.RegIt(xml.Key, xml.Vals);
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 注册枚举类型

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 3, 执行基本的 sql
		String sqlscript = SystemConfig.PathOfWebApp + "\\GPM\\SQLScript\\Port_Inc_CH_BPM.sql";
		BP.DA.DBAccess.RunSQLScript(sqlscript);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 修复

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 5, 初始化数据。
		if (BP.DA.DBAccess.IsExitsObject("GPM_AppSort") == true)
		{
			sqlscript = SystemConfig.PathOfWebApp + "\\GPM\\SQLScript\\InitPublicData.sql";
			BP.DA.DBAccess.RunSQLScript(sqlscript);
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 初始化数据

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 6, 创建视图。
		sqlscript = SystemConfig.PathOfWebApp + "\\GPM\\SQLScript\\MSSQL_GPM_VIEW.sql";

		//MySQL 语法有所区别
		if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.MySQL)
		{
			sqlscript = SystemConfig.PathOfWebApp + "\\GPM\\SQLScript\\MySQL_GPM_VIEW.sql";
		}

		//Oracle 语法有所区别
		if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			sqlscript = SystemConfig.PathOfWebApp + "\\GPM\\SQLScript\\Oracle_GPM_VIEW.sql";
		}

		BP.DA.DBAccess.RunSQLScriptGo(sqlscript);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 创建视图


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
	public static boolean IsCanDoIt(String ctrlObj, BP.GPM.CtrlWay cw, String empID)
	{
		int n = 0;
		String sql = "";
		switch (cw)
		{
			case AnyOne:
				return true;
			case ByStation:
				sql = "SELECT count(*) FROM GPM_ByStation WHERE RefObj='" + ctrlObj + "'  AND FK_Station IN (select FK_Station from  Port_DeptEmpStation WHERE FK_Emp='" + empID + "')";
				n = BP.DA.DBAccess.RunSQLReturnValInt(sql, 0);
				break;
			case ByDept:
				sql = "SELECT count(*) FROM GPM_ByDept WHERE RefObj='" + ctrlObj + "'  AND FK_Dept IN (SELECT FK_Dept FROM Port_Emp WHERE No='" + empID + "')";
				n = BP.DA.DBAccess.RunSQLReturnValInt(sql, 0);
				break;
			case ByEmp:
				sql = "SELECT count(*) FROM GPM_ByEmp WHERE RefObj='" + ctrlObj + "'  AND  FK_Emp='" + empID + "'";
				n = BP.DA.DBAccess.RunSQLReturnValInt(sql, 0);
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