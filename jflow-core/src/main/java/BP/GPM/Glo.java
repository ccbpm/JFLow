package BP.GPM;

import BP.Sys.*;
import BP.DA.*;
import BP.En.*;

public class Glo
{
	/** 
	 运行模式
	 
	*/
	public static OSModel getOSModel()
	{
		OSModel os = OSModel.forValue(SystemConfig.GetValByKeyInt("OSModel", 1));
		return os;
	}
	/** 
	 钉钉是否启用
	 
	*/
	public static boolean getIsEnable_DingDing()
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
	public static boolean getIsEnable_WeiXin()
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
	public static void DoInstallDataBase(String lang, String yunXingHuanjing) throws Exception
	{
		java.util.ArrayList al = null;
		String info = "BP.En.Entity";
		al = BP.En.ClassFactory.GetObjects(info);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 1, 修复表
		for (Object obj : al)
		{
			Entity en = null;
			en = (Entity)((obj instanceof Entity) ? obj : null);
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
			//if (en.ToString().Contains("BP.GPM."))
			//    continue;
			//if (en.ToString().Contains("BP.Demo."))
			//    continue;

			String table = null;
			try
			{
				table = en.getEnMap().getPhysicsTable();
				if (table == null)
				{
					continue;
				}
			}
			catch (java.lang.Exception e)
			{
				continue;
			}


			if (table.equals("WF_EmpWorks") || table.equals("WF_GenerEmpWorkDtls") || table.equals("WF_GenerEmpWorks"))
			{
					continue;
			}

			else if (table.equals("Sys_Enum"))
			{
					en.CheckPhysicsTable();
			}
			else
			{
					en.CheckPhysicsTable();
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

		///#endregion 修复

		///#region 2, 注册枚举类型 sql
		// 2, 注册枚举类型。
		BP.Sys.XML.EnumInfoXmls xmls = new BP.Sys.XML.EnumInfoXmls();
		xmls.RetrieveAll();
		for (BP.Sys.XML.EnumInfoXml xml : xmls.ToJavaList())
		{
			BP.Sys.SysEnums ses = new BP.Sys.SysEnums();
			ses.RegIt(xml.getKey(), xml.getVals());
		}

		///#endregion 注册枚举类型

		///#region 3, 执行基本的 sql
		String sqlscript = SystemConfig.getPathOfWebApp() + "\\GPM\\SQLScript\\Port_Inc_CH_BPM.sql";
		//孙战平将RunSQLScript改为RunSQLScriptGo
		BP.DA.DBAccess.RunSQLScript(sqlscript);
		///#endregion 修复


		///#region 5, 初始化数据。

		sqlscript = SystemConfig.getPathOfWebApp() + "\\GPM\\SQLScript\\InitPublicData.sql";
		BP.DA.DBAccess.RunSQLScript(sqlscript);



		sqlscript = SystemConfig.getPathOfWebApp() + "\\GPM\\SQLScript\\MSSQL_GPM_VIEW.sql";

		//MySQL 语法有所区别
		if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
		{
			sqlscript = SystemConfig.getPathOfWebApp() + "\\GPM\\SQLScript\\MySQL_GPM_VIEW.sql";
		}
		//Oracle 语法有所区别
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			sqlscript = SystemConfig.getPathOfWebApp() + "\\GPM\\SQLScript\\Oracle_GPM_VIEW.sql";
		}
		BP.DA.DBAccess.RunSQLScriptGo(sqlscript);

			///#region 7, 初始化系统访问权限
		//查询出来系统
		Apps apps = new Apps();
		apps.RetrieveAll();

		//查询出来人员.
		Emps emps = new Emps();
		emps.RetrieveAllFromDBSource();
		//查询出来菜单
		Menus menus = new Menus();
		menus.RetrieveAllFromDBSource();

		//删除数据.
		BP.DA.DBAccess.RunSQL("DELETE FROM GPM_EmpApp");


		for (Emp emp : emps.ToJavaList())
		{
				///#region 初始化系统访问权限.
			for (App app : apps.ToJavaList())
			{
				EmpApp me = new EmpApp();
				me.Copy(app);
				me.setFK_Emp(emp.getNo());
				me.setFK_App(app.getNo());
				me.setName(app.getName());
				me.setMyPK(app.getNo() + "_" + me.getFK_Emp());
				me.Insert();
			}
				///#region 初始化人员菜单权限
			for (Menu menu : menus.ToJavaList())
			{
				EmpMenu em = new EmpMenu();
				em.Copy(menu);
				em.setFK_Emp(emp.getNo());
				em.setFK_App(menu.getFK_App());
				em.setFK_Menu(menu.getNo());
				//em.setMyPK(menu.getNo() + "_" + emp.getNo());
				em.Insert();
			}
				///#endregion
		}
		//处理全路径
		Depts depts = new Depts();
		depts.RetrieveAll();
		for (Dept dept : depts.ToJavaList())
		{
			dept.GenerNameOfPath();
		}
			///#endregion
	}
	/** 
	 安装CCIM
	 
	 @param lang
	 @param yunXingHuanjing
	 @param isDemo
	*/
	public static void DoInstallCCIM(String lang, String dbTypes)
	{
	  //  string sqlscript = SystemConfig.PathOfWebApp + "\\GPM\\SQLScript\\CCIM_"+BP.Sys.SystemConfig.AppCenterDBType+".sql";
	   // BP.DA.DBAccess.RunSQLScriptGo(sqlscript);
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