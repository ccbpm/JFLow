package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Sys.*;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Template.*;
import LitJson.*;
import BP.WF.XML.*;
import BP.WF.*;
import java.util.*;
import java.time.*;

/** 
 初始化函数
*/
public class WF_Admin_CCBPMDesigner extends DirectoryPageBase
{

	public final String GetFlowTreeTable2019()
	{
		String sql = "SELECT * FROM (SELECT 'F'+No as No,'F'+ParentNo ParentNo, Name, IDX, 1 IsParent,'FLOWTYPE' TTYPE,-1 DTYPE FROM WF_FlowSort" + "\r\n" +
"                           union " + "\r\n" +
"                           SELECT NO, 'F'+FK_FlowSort as ParentNo,(NO + '.' + NAME) as Name,IDX,0 IsParent,'FLOW' TTYPE, 0 as DTYPE FROM WF_Flow) A  ORDER BY IDX";

		if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.Oracle || BP.Sys.SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			sql = "SELECT * FROM (SELECT 'F'||No as No,'F'||ParentNo as ParentNo,Name, IDX, 1 IsParent,'FLOWTYPE' TTYPE,-1 DTYPE FROM WF_FlowSort" + "\r\n" +
"                        union " + "\r\n" +
"                        SELECT NO, 'F'||FK_FlowSort as ParentNo,NO||'.'||NAME as Name,IDX,0 IsParent,'FLOW' TTYPE,0 as DTYPE FROM WF_Flow) A  ORDER BY IDX";
		}


		if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.MySQL)
		{
			sql = "SELECT * FROM (SELECT CONCAT('F', No) No, CONCAT('F', ParentNo) ParentNo, Name, IDX, 1 IsParent,'FLOWTYPE' TTYPE,-1 DTYPE FROM WF_FlowSort" + "\r\n" +
"                           union " + "\r\n" +
"                           SELECT NO, CONCAT('F', FK_FlowSort) ParentNo, CONCAT(NO, '.', NAME) Name,IDX,0 IsParent,'FLOW' TTYPE,0 as DTYPE FROM WF_Flow) A  ORDER BY IDX";
		}

		DataTable dt = DBAccess.RunSQLReturnTable(sql);


		if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dt.Columns.get("no").ColumnName = "No";
			dt.Columns.get("name").ColumnName = "Name";
			dt.Columns.get("parentno").ColumnName = "ParentNo";
			dt.Columns.get("idx").ColumnName = "IDX";

			dt.Columns.get("ttype").ColumnName = "TTYPE";
			dt.Columns.get("dtype").ColumnName = "DTYPE";
		}

		if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			dt.Columns.get("NO").ColumnName = "No";
			dt.Columns.get("NAME").ColumnName = "Name";
			dt.Columns.get("PARENTNO").ColumnName = "ParentNo";
			dt.Columns.get("IDX").ColumnName = "IDX";

			dt.Columns.get("TTYPE").ColumnName = "TTYPE";
			dt.Columns.get("DTYPE").ColumnName = "DTYPE";
		}

		//判断是否为空，如果为空，则创建一个流程根结点，added by liuxc,2016-01-24
		if (dt.Rows.size() == 0)
		{
			FlowSort fs = new FlowSort();
			fs.No = "99";
			fs.setParentNo("0");
			fs.Name = "流程树";
			fs.Insert();

			dt.Rows.add("F99", "F0", "流程树", 0, 1, "FLOWTYPE", -1);
		}
		else
		{
			DataRow[] drs = dt.Select("NAME='流程树'");
			if (drs.length > 0 && !Equals(drs[0].get("PARENTNO"), "F0"))
			{
				drs[0].set("ParentNo", "F0");
			}
		}


		if (!WebUser.getNo().equals("admin"))
		{
			BP.WF.Port.AdminEmp aemp = new Port.AdminEmp();
			aemp.No = WebUser.getNo();
			if (aemp.RetrieveFromDBSources() == 0)
			{
				return "err@登录帐号错误.";
			}

			if (aemp.getIsAdmin() == false)
			{
				return "err@非管理员用户.";
			}

			DataRow rootRow = dt.Select("ParentNo='F0'")[0];
			DataRow newRootRow = dt.Select("No='F" + aemp.getRootOfFlow() + "'")[0];

			newRootRow.set("ParentNo", "F0");
			DataTable newDt = dt.Clone();
			newDt.Rows.add(newRootRow.ItemArray);
			GenerChildRows(dt, newDt, newRootRow);
			dt = newDt;
		}

		return BP.Tools.Json.ToJson(dt);
	}
	/** 
	 选择器
	 
	 @return 
	*/
	public final String SelectEmps_Init()
	{
		String fk_flowsort = this.GetRequestVal("FK_FlowSort").substring(1);

		if (DataType.IsNullOrEmpty(fk_flowsort) == true || fk_flowsort.equals("undefined") == true)
		{
			fk_flowsort = "99";
		}

		DataSet ds = new DataSet();

		String sql = "";
			 sql = "SELECT 'F' + No as No,Name, 'F' + ParentNo as ParentNo FROM WF_FlowSort WHERE No='" + fk_flowsort + "' OR ParentNo='" + fk_flowsort + "' ORDER BY Idx";

		DataTable dtFlowSorts = BP.DA.DBAccess.RunSQLReturnTable(sql);
		//if (dtFlowSort.Rows.size() == 0)
		//{
		//    fk_dept = WebUser.getFK_Dept();
		//    sql = "SELECT No,Name,ParentNo FROM Port_Dept WHERE No='" + fk_dept + "' OR ParentNo='" + fk_dept + "' ORDER BY Idx ";
		//    dtDept = BP.DA.DBAccess.RunSQLReturnTable(sql);
		//}

		dtFlowSorts.TableName = "FlowSorts";
		ds.Tables.add(dtFlowSorts);

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dtFlowSorts.Columns[0].ColumnName = "No";
			dtFlowSorts.Columns[1].ColumnName = "Name";
			dtFlowSorts.Columns[2].ColumnName = "ParentNo";
		}



		//sql = "SELECT No,Name, FK_Dept FROM Port_Emp WHERE FK_Dept='" + fk_dept + "' ";
		sql = "SELECT  No,(NO + '.' + NAME) as Name, 'F' + FK_FlowSort as ParentNo, Idx FROM WF_Flow where FK_FlowSort='" + fk_flowsort + "' ";
		sql += " ORDER BY Idx ";


		DataTable dtFlows = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dtFlows.TableName = "Flows";
		ds.Tables.add(dtFlows);
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dtFlows.Columns[0].ColumnName = "No";
			dtFlows.Columns[1].ColumnName = "Name";
			dtFlows.Columns[2].ColumnName = "FK_FlowSort";
		}

		//转化为 json 
		return BP.Tools.Json.DataSetToJson(ds, false);
	}

	/** 
	 按照管理员登录.
	 
	 @param userNo 管理员编号
	 @return 登录信息
	*/
	public final String AdminerChang_LoginAs()
	{
		String orgNo = this.GetRequestVal("OrgNo");

		BP.WF.Port.AdminEmp ae = new Port.AdminEmp();
		ae.No = WebUser.getNo() + "@" + orgNo;
		if (ae.RetrieveFromDBSources() == 0)
		{
			return "err@您不是该组织的管理员.";
		}

		BP.WF.Port.AdminEmp ae1 = new Port.AdminEmp();
		ae1.No = WebUser.getNo();
		ae1.RetrieveFromDBSources();

		if (ae1.getRootOfDept().equals(orgNo) == true)
		{
			return "info@当前已经是该组织的管理员了，您不用切换.";
		}

		ae1.Copy(ae);
		ae1.No = WebUser.getNo();
		ae1.Update();

		//AdminEmp ad = new AdminEmp();
		//ad.No = userNo;
		//if (ad.RetrieveFromDBSources() == 0)
		//    return "err@用户名错误.";
		return "info@登录成功, 如果系统不能自动刷新，请手工刷新。";
	}

	public final String Flows_Init()
	{
		DataTable dt = new DataTable();

		dt.Columns.Add("FlowNo");
		dt.Columns.Add("FlowName");

		dt.Columns.Add("NumOfRuning"); //运行中的.
		dt.Columns.Add("NumOfOK"); //已经完成的.
		dt.Columns.Add("NumOfEtc"); //其他.

		Flows fls = new Flows();
		fls.RetrieveAll();

		for (Flow fl : fls.ToJavaList())
		{
			DataRow dr = dt.NewRow();
			dr.set("FlowNo", fl.No);
			dr.set("FlowName", fl.Name);
			dr.set("NumOfRuning", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM  WF_GenerWorkFlow WHERE FK_Flow='" + fl.getNo() + "' AND WFState in (2,5)", 0));
			dr.set("NumOfOK", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM  WF_GenerWorkFlow WHERE FK_Flow='" + fl.getNo() + "' AND WFState = 3 ", 0));
			dr.set("NumOfEtc", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM  WF_GenerWorkFlow WHERE FK_Flow='" + fl.getNo() + "' AND WFState in (4,5,6,7,8) ", 0));

			dt.Rows.add(dr);
		}

		return BP.Tools.Json.ToJson(dt);
	}

	/** 
	 构造函数
	*/
	public WF_Admin_CCBPMDesigner()
	{
	}
	/** 
	 执行流程设计图的保存.
	 
	 @return 
	*/
	public final String Designer_Save()
	{
		String sql = "";
		try
		{
			{
				StringBuilder sBuilder = new StringBuilder();

				//保存节点位置. @101,2,30@102,3,1
				String[] nodes = this.GetRequestVal("Nodes").split("[@]", -1);
				for (String item : nodes)
				{
					if (item.equals("") || item == null)
					{
						continue;
					}
					String[] strs = item.split("[,]", -1);
					sBuilder.append("UPDATE WF_Node SET X=" + strs[1] + ",Y=" + strs[2] + " WHERE NodeID=" + strs[0] + ";");
				}

				DBAccess.RunSQL(sBuilder.toString());

				//保存方向.
				sBuilder = new StringBuilder();
				String[] dirs = this.GetRequestVal("Dirs").split("[@]", -1);
				for (String item : dirs)
				{
					if (item.equals("") || item == null)
					{
						continue;
					}
					String[] strs = item.split("[,]", -1);
					sBuilder.append("DELETE FROM WF_Direction where MyPK='" + strs[0] + "';");
					sBuilder.append("INSERT INTO WF_Direction(MyPK,FK_Flow,Node,ToNode,IsCanBack) values('" + strs[0] + "','" + strs[1] + "','" + strs[2] + "','" + strs[3] + "'," + "0);");
				}

				DBAccess.RunSQL(sBuilder.toString());

				//保存label位置.
				sBuilder = new StringBuilder();
				String[] labs = this.GetRequestVal("Labs").split("[@]", -1);
				for (String item : labs)
				{
					if (item.equals("") || item == null)
					{
						continue;
					}
					String[] strs = item.split("[,]", -1);
					sBuilder.append("UPDATE WF_LabNote SET X=" + strs[1] + ",Y=" + strs[2] + " WHERE MyPK='" + strs[0] + "';");
				}

				DBAccess.RunSQL(sBuilder.toString());

				return "保存成功.";
			}
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}

	/** 
	 下载流程模版
	 
	 @return 
	*/
	public final String ExpFlowTemplete()
	{
		Flow flow = new Flow(this.getFK_Flow());
		String fileXml = flow.GenerFlowXmlTemplete();
		String docs = DataType.ReadTextFile(fileXml);
		return docs;
	}
	public final String DownFormTemplete()
	{
		DataSet ds = BP.Sys.CCFormAPI.GenerHisDataSet_AllEleInfo(this.getFK_MapData());
		String file = BP.Sys.SystemConfig.PathOfTemp + this.getFK_MapData() + ".xml";
		ds.WriteXml(file);
		String docs = DataType.ReadTextFile(file);
		return docs;

		//DataTable dt = new DataTable();
		//dt.Columns.Add("FileName");
		//dt.Columns.Add("FileType");
		//dt.Columns.Add("FlieContent");
		//DataRow dr = dt.NewRow();
		//dr["FileName"] = md.Name+".xml";
		//dr["FileType"] = "xml";
		//dr["FlieContent"] = docs;
		//dt.Rows.add(dr);
		//return BP.Tools.Json.ToJson(dt);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 执行父类的重写方法.
	/** 
	 默认执行的方法
	 
	 @return 
	*/
	@Override
	protected String DoDefaultMethod()
	{

		return "err@没有判断的标记:" + this.getDoType();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 执行父类的重写方法.

	/** 
	 使管理员登录使管理员登录    /// 
	 @return 
	*/
	public final String LetLogin()
	{
		LetAdminLogin(this.GetRequestVal("UserNo"), true);
		return "登录成功.";
	}
	/** 
	 获得枚举列表的JSON.
	 
	 @return 
	*/
	public final String Logout()
	{
		BP.WF.Dev2Interface.Port_SigOut();
		return "您已经安全退出,欢迎使用ccbpm.";
	}

	/** 
	 根据部门、岗位获取人员列表
	 
	 @return 
	*/
	public final String GetEmpsByStationTable()
	{
		String deptid = this.GetRequestVal("DeptNo");
		String stid = this.GetRequestVal("StationNo");

		if (DataType.IsNullOrEmpty(deptid) || DataType.IsNullOrEmpty(stid))
		{
			return "[]";
		}

		DataTable dt = new DataTable();
		dt.Columns.Add("NO", String.class);
		dt.Columns.Add("PARENTNO", String.class);
		dt.Columns.Add("NAME", String.class);
		dt.Columns.Add("TTYPE", String.class);


		BP.GPM.Emp emp = null;
		BP.GPM.Emps emps = new BP.GPM.Emps();
		emps.RetrieveAll();

		BP.GPM.DeptEmpStations dess = new BP.GPM.DeptEmpStations();
		dess.Retrieve(BP.GPM.DeptEmpStationAttr.FK_Dept, deptid, BP.GPM.DeptEmpStationAttr.FK_Station, stid);

		for (BP.GPM.DeptEmpStation des : dess)
		{
			Object tempVar = emps.GetEntityByKey(des.FK_Emp);
			emp = tempVar instanceof BP.GPM.Emp ? (BP.GPM.Emp)tempVar : null;

			dt.Rows.add(emp.No, deptid + "|" + stid, emp.Name, "EMP");
		}

		return BP.Tools.Json.ToJson(dt);
	}

	public final String GetStructureTreeRootTable()
	{
		DataTable dt = new DataTable();
		dt.Columns.Add("NO", String.class);
		dt.Columns.Add("PARENTNO", String.class);
		dt.Columns.Add("NAME", String.class);
		dt.Columns.Add("TTYPE", String.class);

		String parentrootid = this.GetRequestVal("parentrootid"); // context.Request.QueryString["parentrootid"];
		String newRootId = "";

		if (!WebUser.getNo().equals("admin"))
		{
			BP.WF.Port.AdminEmp aemp = new Port.AdminEmp();
			aemp.No = WebUser.getNo();

			if (aemp.RetrieveFromDBSources() != 0 && aemp.getUserType() == 1 && !DataType.IsNullOrEmpty(aemp.getRootOfDept()))
			{
				newRootId = aemp.getRootOfDept();
			}
		}

		if (BP.WF.Glo.getOSModel() == OSModel.OneOne)
		{
			BP.WF.Port.Dept dept = new BP.WF.Port.Dept();

			if (!DataType.IsNullOrEmpty(newRootId))
			{
				if (dept.Retrieve(BP.WF.Port.DeptAttr.No, newRootId) == 0)
				{
					dept.No = "-1";
					dept.Name = "无部门";
					dept.setParentNo("");
				}
			}
			else
			{
				if (dept.Retrieve(BP.WF.Port.DeptAttr.ParentNo, parentrootid) == 0)
				{
					dept.No = "-1";
					dept.Name = "无部门";
					dept.setParentNo("");
				}
			}

			dt.Rows.add(dept.No, dept.getParentNo(), dept.Name, "DEPT");
		}
		else
		{
			BP.GPM.Dept dept = new BP.GPM.Dept();

			if (!DataType.IsNullOrEmpty(newRootId))
			{
				if (dept.Retrieve(BP.GPM.DeptAttr.No, newRootId) == 0)
				{
					dept.No = "-1";
					dept.Name = "无部门";
					dept.ParentNo = "";
				}
			}
			else
			{
				if (dept.Retrieve(BP.GPM.DeptAttr.ParentNo, parentrootid) == 0)
				{
					dept.No = "-1";
					dept.Name = "无部门";
					dept.ParentNo = "";
				}
			}

			dt.Rows.add(dept.No, dept.ParentNo, dept.Name, "DEPT");
		}

		return BP.Tools.Json.ToJson(dt);
	}

	/** 
	 获取指定部门下一级子部门及岗位列表
	 
	 @return 
	*/
	public final String GetSubDeptsTable()
	{
		DataTable dt = new DataTable();
		dt.Columns.Add("NO", String.class);
		dt.Columns.Add("PARENTNO", String.class);
		dt.Columns.Add("NAME", String.class);
		dt.Columns.Add("TTYPE", String.class);

		String rootid = this.GetRequestVal("rootid"); // context.Request.QueryString["rootid"];


		BP.GPM.Depts depts = new BP.GPM.Depts();
		depts.Retrieve(BP.GPM.DeptAttr.ParentNo, rootid);
		BP.GPM.Stations sts = new BP.GPM.Stations();
		sts.RetrieveAll();
		BP.GPM.DeptStations dss = new BP.GPM.DeptStations();
		dss.Retrieve(BP.GPM.DeptStationAttr.FK_Dept, rootid);
		BP.GPM.DeptEmps des = new BP.GPM.DeptEmps();
		des.Retrieve(BP.GPM.DeptEmpAttr.FK_Dept, rootid);
		BP.GPM.DeptEmpStations dess = new BP.GPM.DeptEmpStations();
		dess.Retrieve(BP.GPM.DeptEmpStationAttr.FK_Dept, rootid);
		BP.GPM.Station stt = null;
		BP.GPM.Emp emp = null;
		ArrayList<String> inemps = new ArrayList<String>();

		for (BP.GPM.Dept dept : depts.ToJavaList())
		{
			//增加部门
			dt.Rows.add(dept.No, dept.ParentNo, dept.Name, "DEPT");
		}

		//增加部门岗位
		for (BP.GPM.DeptStation ds : dss)
		{
			Object tempVar = sts.GetEntityByKey(ds.FK_Station);
			stt = tempVar instanceof BP.GPM.Station ? (BP.GPM.Station)tempVar : null;

			if (stt == null)
			{
				continue;
			}

			dt.Rows.add(ds.FK_Station, rootid, stt.Name, "STATION");
		}

		//增加没有岗位的人员
		for (BP.GPM.DeptEmp de : des)
		{
			if (dess.GetEntityByKey(BP.GPM.DeptEmpStationAttr.FK_Emp, de.FK_Emp) == null)
			{
				if (inemps.contains(de.FK_Emp))
				{
					continue;
				}

				inemps.add(de.FK_Emp);
			}
		}

		for (String inemp : inemps)
		{
			emp = new BP.GPM.Emp(inemp);
			dt.Rows.add(emp.No, rootid, emp.Name, "EMP");
		}

		return BP.Tools.Json.ToJson(dt);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 主页.
	/** 
	 初始化登录界面.
	 
	 @return 
	*/
	public final String Default_Init()
	{
		try
		{
			//如果登录信息丢失了,就让其重新登录一次.
			if (DataType.IsNullOrEmpty(WebUser.getNo()OfRel) == true)
			{
				String userNo = this.GetRequestVal("UserNo");
				String sid = this.GetRequestVal("SID");
				BP.WF.Dev2Interface.Port_LoginBySID(userNo, sid);
			}

			if (WebUser.getIsAdmin() == false)
			{
				return "url@Login.htm?DoType=Logout&Err=NoAdminUsers";
			}

			//如果没有流程表，就执行安装.
			if (BP.DA.DBAccess.IsExitsObject("WF_Flow") == false)
			{
				return "url@../DBInstall.htm";
			}

			Hashtable ht = new Hashtable();
			if (BP.WF.Glo.getOSModel() == OSModel.OneOne)
			{
				ht.put("OSModel", "0");
			}
			else
			{
				ht.put("OSModel", "1");
			}

			//把系统信息加入里面去.
			ht.put("SysNo", SystemConfig.SysNo);
			ht.put("SysName", SystemConfig.SysName);

			ht.put("CustomerNo", SystemConfig.getCustomerNo());
			ht.put("CustomerName", SystemConfig.CustomerName);

			//集成的平台.
			ht.put("RunOnPlant", SystemConfig.RunOnPlant);

			try
			{
				// 执行升级
				String str = BP.WF.Glo.UpdataCCFlowVer();
				if (str == null)
				{
					str = "";
				}
				ht.put("Msg", str);
			}
			catch (RuntimeException ex)
			{
				return "err@" + ex.getMessage();
			}

			//生成Json.
			return BP.Tools.Json.ToJsonEntityModel(ht);
		}
		catch (RuntimeException ex)
		{
			return "err@初始化界面期间出现如下错误:" + ex.getMessage();
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 登录窗口.
	public final String Login_InitInfo()
	{
		Hashtable ht = new Hashtable();
		ht.put("SysNo", SystemConfig.SysNo);
		ht.put("SysName", SystemConfig.SysName);

		return BP.Tools.Json.ToJson(ht);
	}
	/** 
	 初始化登录界面.
	 
	 @return 
	*/
	public final String Login_Init()
	{

		//检查数据库连接.
		try
		{
			DBAccess.TestIsConnection();
		}
		catch (RuntimeException ex)
		{
			return "err@异常信息:" + ex.getMessage();
		}


		//检查是否缺少Port_Emp 表，如果没有就是没有安装.
		if (DBAccess.IsExitsObject("Port_Emp") == false && DBAccess.IsExitsObject("WF_Flow") == false)
		{
			return "url@../DBInstall.htm";
		}

		////让admin登录
		//if (DataType.IsNullOrEmpty(WebUser.getNo()) || WebUser.getIsAdmin() == false)
		//    return "url@Login.htm?DoType=Logout";

		//如果没有流程表，就执行安装.
		if (BP.DA.DBAccess.IsExitsObject("WF_Flow") == false)
		{
			return "url@../DBInstall.htm";
		}

		//是否需要自动登录
		String userNo = this.GetRequestVal("UserNo");
		String sid = this.GetRequestVal("SID");

		if (!DataType.IsNullOrEmpty(sid) && !DataType.IsNullOrEmpty(userNo))
		{
			/*  */
			try
			{
				String str = BP.WF.Glo.UpdataCCFlowVer();

				BP.WF.Dev2Interface.Port_LoginBySID(userNo, sid);
				if (this.getFK_Flow() == null)
				{
					return "url@Default.htm?UserNo=" + userNo + "&Key=" + LocalDateTime.now().ToBinary();
				}
				else
				{
					return "url@Designer.htm?UserNo=" + userNo + "&FK_Flow=" + this.getFK_Flow() + "&Key=" + LocalDateTime.now().ToBinary();
				}
			}
			catch (RuntimeException ex)
			{
				return "err@登录失败" + ex.getMessage();
			}
		}

		try
		{
			// 执行升级
			String str = BP.WF.Glo.UpdataCCFlowVer();
			if (str == null)
			{
				str = "准备完毕,欢迎登录,当前小版本号为:" + BP.WF.Glo.Ver;
			}

			return str;
			//Hashtable ht = new Hashtable();
			//ht.Add("Msg", str);
			//ht.Add("Title", SystemConfig.SysName);
			//return BP.Tools.Json.ToJson(ht);

		}
		catch (RuntimeException ex)
		{
			String msg = "err@升级失败(ccbpm有自动修复功能,您可以刷新一下系统会自动创建字段,刷新多次扔解决不了问题,请反馈给我们.www.ccflow.org)";
			msg += "@系统信息:" + ex.getMessage();
			return msg;
		}
	}
	//流程设计器登陆前台，转向规则，判断是否为天业BPM
	public final String Login_Redirect()
	{
		if (SystemConfig.getCustomerNo().equals("TianYe"))
		{
			return "url@../../../BPM/pages/login.html";
		}

		return "url@../../AppClassic/Login.htm?DoType=Logout";
	}
	/** 
	 提交
	 
	 @return 
	*/
	public final String Login_Submit()
	{
		String[] para = new String[0];
		BP.Port.Emp emp = new BP.Port.Emp();
		emp.No = this.GetRequestVal("TB_No").trim();
		if (emp.RetrieveFromDBSources() == 0)
		{
			return "err@用户名或密码错误.";
		}
		//return BP.WF.Glo.lang("invalid_username_or_pwd", para);

		if (!emp.No.equals("admin"))
		{
			//检查是否是管理员？
			BP.WF.Port.AdminEmp adminEmp = new Port.AdminEmp();
			adminEmp.No = emp.No;
			if (adminEmp.RetrieveFromDBSources() == 0)
			{
				return "err@您非管理员用户，不能登录.";
			}
			//return BP.WF.Glo.lang("no_permission_login_1", para);

			if (adminEmp.getIsAdmin() == false)
			{
				return "err@您非管理员用户或已被禁用,不能登录,请联系管理员初始化账户.";
			}
			//return BP.WF.Glo.lang("no_permission_login_2", para);

			if (DataType.IsNullOrEmpty(adminEmp.getRootOfFlow()) == true)
			{
				return "err@二级管理员用户没有设置流程树的权限..";
			}
			//return BP.WF.Glo.lang("secondary_user_no_permission_wf_tree", para);
		}

		String pass = this.GetRequestVal("TB_PW").trim();
		if (emp.CheckPass(pass) == false)
		{
			return "err@用户名或密码错误.";
		}
		//return BP.WF.Glo.lang("invalid_username_or_pwd", para);

		//让其登录.
		BP.WF.Dev2Interface.Port_Login(emp.No);
		return "url@Default.htm?SID=" + emp.SID + "&UserNo=" + emp.No;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 登录窗口.



//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 流程相关 Flow
	/** 
	 获取流程所有元素
	 
	 @return json data
	*/
	public final String Flow_AllElements_ResponseJson()
	{
		BP.WF.Flow flow = new BP.WF.Flow();
		flow.No = this.getFK_Flow();
		flow.RetrieveFromDBSources();

		DataSet ds = new DataSet();
		DataTable dtNodes = DBAccess.RunSQLReturnTable("SELECT NODEID,NAME,X,Y,RUNMODEL FROM WF_NODE WHERE FK_FLOW='" + this.getFK_Flow() + "'");
		dtNodes.TableName = "Nodes";
		ds.Tables.add(dtNodes);

		DataTable dtDirection = DBAccess.RunSQLReturnTable("SELECT NODE,TONODE FROM WF_DIRECTION WHERE FK_FLOW='" + this.getFK_Flow() + "'");
		dtDirection.TableName = "Direction";
		ds.Tables.add(dtDirection);

		DataTable dtLabNote = DBAccess.RunSQLReturnTable("SELECT MYPK,NAME,X,Y FROM WF_LABNOTE WHERE FK_FLOW='" + this.getFK_Flow() + "'");
		dtLabNote.TableName = "LabNote";
		ds.Tables.add(dtLabNote);


		// return BP.Tools.Json.DataSetToJson(ds, false);
		return BP.Tools.Json.ToJson(ds);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion end Flow

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 节点相关 Nodes
	/** 
	 创建流程节点并返回编号
	 
	 @return 
	*/
	public final String CreateNode()
	{
		try
		{
			String FK_Flow = this.GetValFromFrmByKey("FK_Flow");
			String figureName = this.GetValFromFrmByKey("FigureName");
			String x = this.GetValFromFrmByKey("x");
			String y = this.GetValFromFrmByKey("y");
			int iX = 20;
			int iY = 20;
			if (!DataType.IsNullOrEmpty(x))
			{
				iX = (int)Double.parseDouble(x);
			}
			if (!DataType.IsNullOrEmpty(y))
			{
				iY = (int)Double.parseDouble(y);
			}

			int nodeId = BP.WF.Template.TemplateGlo.NewNode(FK_Flow, iX, iY);

			BP.WF.Node node = new BP.WF.Node(nodeId);
			node.setHisRunModel(Node_GetRunModelByFigureName(figureName));
			node.Update();

			Hashtable ht = new Hashtable();
			ht.put("NodeID", node.getNodeID());
			ht.put("Name", node.getName());

			return BP.Tools.Json.ToJsonEntityModel(ht);
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 gen
	 
	 @param figureName
	 @return 
	*/
	public final BP.WF.RunModel Node_GetRunModelByFigureName(String figureName)
	{
		BP.WF.RunModel runModel = BP.WF.RunModel.Ordinary;
		switch (figureName)
		{
			case "NodeOrdinary":
				runModel = BP.WF.RunModel.Ordinary;
				break;
			case "NodeFL":
				runModel = BP.WF.RunModel.FL;
				break;
			case "NodeHL":
				runModel = BP.WF.RunModel.HL;
				break;
			case "NodeFHL":
				runModel = BP.WF.RunModel.FHL;
				break;
			case "NodeSubThread":
				runModel = BP.WF.RunModel.SubThread;
				break;
			default:
				runModel = BP.WF.RunModel.Ordinary;
				break;
		}
		return runModel;
	}
	/** 
	 根据节点编号删除流程节点
	 
	 @return 执行结果
	*/
	public final String DeleteNode()
	{
		try
		{
			BP.WF.Node node = new BP.WF.Node();
			node.setNodeID(this.getFK_Node());
			if (node.RetrieveFromDBSources() == 0)
			{
				return "err@删除失败,没有删除到数据，估计该节点已经别删除了.";
			}

			if (node.getIsStartNode() == true)
			{
				return "err@开始节点不允许被删除。";
			}

			node.Delete();
			return "删除成功.";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 修改节点名称
	 
	 @return 
	*/
	public final String Node_EditNodeName()
	{
		String FK_Node = this.GetValFromFrmByKey("NodeID");
		//string NodeName = System.Web.HttpContext.Current.Server.UrlDecode(this.GetValFromFrmByKey("NodeName"));
		String NodeName = HttpContextHelper.UrlDecode(this.GetValFromFrmByKey("NodeName"));

		BP.WF.Node node = new BP.WF.Node();
		node.setNodeID(Integer.parseInt(FK_Node));
		int iResult = node.RetrieveFromDBSources();
		if (iResult > 0)
		{
			node.setName(NodeName);
			node.Update();
			return "@修改成功.";
		}

		return "err@修改节点失败，请确认该节点是否存在？";
	}
	/** 
	 修改节点运行模式
	 
	 @return 
	*/
	public final String Node_ChangeRunModel()
	{
		String runModel = GetValFromFrmByKey("RunModel");
		BP.WF.Node node = new BP.WF.Node(this.getFK_Node());
		//节点运行模式
		switch (runModel)
		{
			case "NodeOrdinary":
				node.setHisRunModel(BP.WF.RunModel.Ordinary);
				break;
			case "NodeFL":
				node.setHisRunModel(BP.WF.RunModel.FL);
				break;
			case "NodeHL":
				node.setHisRunModel(BP.WF.RunModel.HL);
				break;
			case "NodeFHL":
				node.setHisRunModel(BP.WF.RunModel.FHL);
				break;
			case "NodeSubThread":
				node.setHisRunModel(BP.WF.RunModel.SubThread);
				break;
		}
		node.Update();

		return "设置成功.";
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion end Node

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region CCBPMDesigner
	/** 
	 获取用户信息
	 
	 @return 
	*/
	public final String GetWebUserInfo()
	{
		if (WebUser.getNo() == null)
		{
			return "err@当前用户没有登录，请登录后再试。";
		}

		Hashtable ht = new Hashtable();

		BP.Port.Emp emp = new BP.Port.Emp(WebUser.getNo());

		ht.put("No", emp.No);
		ht.put("Name", emp.Name);
		ht.put("FK_Dept", emp.FK_Dept);
		ht.put("SID", emp.SID);


		if (WebUser.getNo().equals("admin"))
		{
			ht.put("IsAdmin", "1");
			ht.put("RootOfDept", "0");
			ht.put("RootOfFlow", "F0");
			ht.put("RootOfForm", "");
		}
		else
		{
			BP.WF.Port.AdminEmp aemp = new Port.AdminEmp();
			aemp.No = WebUser.getNo();

			if (aemp.RetrieveFromDBSources() == 0)
			{
				ht.put("RootOfDept", "-9999");
				ht.put("RootOfFlow", "-9999");
				ht.put("RootOfForm", "-9999");
			}
			else
			{
				ht.put("RootOfDept", aemp.getRootOfDept());
				ht.put("RootOfFlow", "F" + aemp.getRootOfFlow());
				ht.put("RootOfForm", aemp.getRootOfForm());
			}
		}

		return BP.Tools.Json.ToJsonEntityModel(ht);
	}

	private StringBuilder sbJson = new StringBuilder();
	/** 
	 获取流程树数据
	 
	 @return 返回结果Json,流程树
	*/
	public final String GetFlowTreeTable()
	{
		String sql = "SELECT * FROM (SELECT 'F'+No as NO,'F'+ParentNo PARENTNO, NAME, IDX, 1 ISPARENT,'FLOWTYPE' TTYPE, -1 DTYPE FROM WF_FlowSort" + "\r\n" +
"                           union " + "\r\n" +
"                           SELECT NO, 'F'+FK_FlowSort as PARENTNO,(NO + '.' + NAME) as NAME,IDX,0 ISPARENT,'FLOW' TTYPE, 0 as DTYPE FROM WF_Flow) A  ORDER BY DTYPE, IDX ";

		if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.Oracle || BP.Sys.SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			sql = "SELECT * FROM (SELECT 'F'||No as NO,'F'||ParentNo as PARENTNO,NAME, IDX, 1 ISPARENT,'FLOWTYPE' TTYPE,-1 DTYPE FROM WF_FlowSort" + "\r\n" +
"                        union " + "\r\n" +
"                        SELECT NO, 'F'||FK_FlowSort as PARENTNO,NO||'.'||NAME as NAME,IDX,0 ISPARENT,'FLOW' TTYPE,0 as DTYPE FROM WF_Flow) A  ORDER BY DTYPE, IDX";
		}


		if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.MySQL)
		{
			sql = "SELECT * FROM (SELECT CONCAT('F', No) NO, CONCAT('F', ParentNo) PARENTNO, NAME, IDX, 1 ISPARENT,'FLOWTYPE' TTYPE,-1 DTYPE FROM WF_FlowSort" + "\r\n" +
"                           union " + "\r\n" +
"                           SELECT NO, CONCAT('F', FK_FlowSort) PARENTNO, CONCAT(NO, '.', NAME) NAME,IDX,0 ISPARENT,'FLOW' TTYPE, 0 as DTYPE FROM WF_Flow) A  ORDER BY DTYPE, IDX";
		}

		DataTable dt = DBAccess.RunSQLReturnTable(sql);


		if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dt.Columns.get("no").ColumnName = "NO";
			dt.Columns.get("name").ColumnName = "NAME";
			dt.Columns.get("parentno").ColumnName = "PARENTNO";
			dt.Columns.get("idx").ColumnName = "IDX";
			dt.Columns.get("isparent").ColumnName = "ISPARENT";
			dt.Columns.get("ttype").ColumnName = "TTYPE";
			dt.Columns.get("dtype").ColumnName = "DTYPE";
		}

		//判断是否为空，如果为空，则创建一个流程根结点，added by liuxc,2016-01-24
		if (dt.Rows.size() == 0)
		{
			FlowSort fs = new FlowSort();
			fs.No = "99";
			fs.setParentNo("0");
			fs.Name = "流程树";
			fs.Insert();

			dt.Rows.add("F99", "F0", "流程树", 0, 1, "FLOWTYPE", -1);
		}
		else
		{
			DataRow[] drs = dt.Select("NAME='流程树'");
			if (drs.length > 0 && !Equals(drs[0].get("PARENTNO"), "F0"))
			{
				drs[0].set("PARENTNO", "F0");
			}
		}


		if (!WebUser.getNo().equals("admin"))
		{
			BP.WF.Port.AdminEmp aemp = new Port.AdminEmp();
			aemp.No = WebUser.getNo();
			if (aemp.RetrieveFromDBSources() == 0)
			{
				return "err@登录帐号错误.";
			}

			if (aemp.getIsAdmin() == false)
			{
				return "err@非管理员用户.";
			}

			DataRow rootRow = dt.Select("PARENTNO='F0'")[0];
			DataRow newRootRow = dt.Select("NO='F" + aemp.getRootOfFlow() + "'")[0];

			newRootRow.set("PARENTNO", "F0");
			DataTable newDt = dt.Clone();
			newDt.Rows.add(newRootRow.ItemArray);
			GenerChildRows(dt, newDt, newRootRow);
			dt = newDt;
		}

		String str = BP.Tools.Json.ToJson(dt);
		return str;
	}

	public final void GenerChildRows(DataTable dt, DataTable newDt, DataRow parentRow)
	{
		DataRow[] rows = dt.Select("ParentNo='" + parentRow.get("NO") + "'");
		for (DataRow r : rows)
		{
			newDt.Rows.add(r.ItemArray);
			GenerChildRows(dt, newDt, r);
		}
	}

	public final String GetBindingFormsTable()
	{
		String fk_flow = GetValFromFrmByKey("fk_flow");
		if (DataType.IsNullOrEmpty(fk_flow))
		{
			return "[]";
		}

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT wfn.FK_Frm NO," + "\r\n");
		sql.append("       smd.NAME," + "\r\n");
		sql.append("       NULL PARENTNO," + "\r\n");
		sql.append("       'FORM' TTYPE," + "\r\n");
		sql.append("       -1 DTYPE," + "\r\n");
		sql.append("       0 ISPARENT" + "\r\n");
		sql.append("FROM   WF_FrmNode wfn" + "\r\n");
		sql.append("       INNER JOIN Sys_MapData smd" + "\r\n");
		sql.append("            ON  smd.No = wfn.FK_Frm" + "\r\n");
		sql.append("WHERE  wfn.FK_Flow = '{0}'" + "\r\n");
		sql.append("       AND wfn.FK_Node = (" + "\r\n");
		sql.append("               SELECT wn.NodeID" + "\r\n");
		sql.append("               FROM   WF_Node wn" + "\r\n");
		sql.append("               WHERE  wn.FK_Flow = '{0}' AND wn.NodePosType = 0" + "\r\n");
//C# TO JAVA CONVERTER TODO TASK: The following line could not be converted:
		sql.AppendLine("           )");

		DataTable dt = DBAccess.RunSQLReturnTable(String.format(sql.toString(), fk_flow));
		return BP.Tools.Json.ToJson(dt);
	}

	public final String GetFormTreeTable()
	{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 检查数据是否符合规范.
		String rootNo = DBAccess.RunSQLReturnStringIsNull("SELECT No FROM Sys_FormTree WHERE ParentNo='' OR ParentNo IS NULL", null);
		if (DataType.IsNullOrEmpty(rootNo) == false)
		{
			//删除垃圾数据.
			DBAccess.RunSQL(String.format("DELETE FROM Sys_FormTree WHERE No='%1$s'", rootNo));
		}
		//检查根目录是否符合规范.
		FrmTree ft = new FrmTree();
		ft.No = "1";
		if (ft.RetrieveFromDBSources() == 0)
		{
			ft.setName("表单库";
			ft.setParentNo("0");
			ft.Insert();
		}
		if (ft.ParentNo.equals("0") == false)
		{
			ft.setParentNo("0");
			ft.Update();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 检查数据是否符合规范.

		//组织数据源.
		String sqls = "";

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			sqls = "SELECT No \"No\", ParentNo \"ParentNo\",Name \"Name\", Idx \"Idx\", 1 \"IsParent\", 'FORMTYPE' \"TType\" FROM Sys_FormTree ORDER BY Idx ASC ; ";
			sqls += "SELECT No \"No\", FK_FormTree as \"ParentNo\", Name \"Name\",Idx \"Idx\", 0 \"IsParent\", 'FORM' \"TType\" FROM Sys_MapData  WHERE AppType=0 AND FK_FormTree IN (SELECT No FROM Sys_FormTree) ORDER BY Idx ASC";
		}
		else
		{
			sqls = "SELECT No,ParentNo,Name, Idx, 1 IsParent, 'FORMTYPE' TType FROM Sys_FormTree ORDER BY Idx ASC ; ";
			sqls += "SELECT No, FK_FormTree as ParentNo,Name,Idx,0 IsParent, 'FORM' TType FROM Sys_MapData  WHERE AppType=0 AND FK_FormTree IN (SELECT No FROM Sys_FormTree) ORDER BY Idx ASC";
		}

		DataSet ds = DBAccess.RunSQLReturnDataSet(sqls);



		//获得表单数据.
		DataTable dtSort = ds.Tables[0]; //类别表.
		DataTable dtForm = ds.Tables[1].Clone(); //表单表,这个是最终返回的数据.

		if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dtForm.Columns.get("no").ColumnName = "No";
			dtForm.Columns.get("name").ColumnName = "Name";
			dtForm.Columns.get("parentno").ColumnName = "ParentNo";
			dtForm.Columns.get("idx").ColumnName = "Idx";
			dtForm.Columns.get("isparent").ColumnName = "IsParent";
			dtForm.Columns.get("ttype").ColumnName = "TType";
		}

		//增加顶级目录.
		DataRow[] rowsOfSort = dtSort.Select("ParentNo='0'");
		DataRow drFormRoot = dtForm.NewRow();
		drFormRoot.set(0, rowsOfSort[0].get("No"));
		drFormRoot.set(1, "0");
		drFormRoot.set(2, rowsOfSort[0].get("Name"));
		drFormRoot.set(3, rowsOfSort[0].get("Idx"));
		drFormRoot.set(4, rowsOfSort[0].get("IsParent"));
		drFormRoot.set(5, rowsOfSort[0].get("TType"));
		dtForm.Rows.add(drFormRoot); //增加顶级类别..

		//把类别数据组装到form数据里.
		for (DataRow dr : dtSort.Rows)
		{
			DataRow drForm = dtForm.NewRow();
			drForm.set(0, dr.get("No"));
			drForm.set(1, dr.get("ParentNo"));
			drForm.set(2, dr.get("Name"));
			drForm.set(3, dr.get("Idx"));
			drForm.set(4, dr.get("IsParent"));
			drForm.set(5, dr.get("TType"));
			dtForm.Rows.add(drForm); //类别.
		}

		for (DataRow row : ds.Tables[1].Rows)
		{
			dtForm.Rows.add(row.ItemArray);
		}

		if (WebUser.getNo().equals("admin") == false)
		{
			BP.WF.Port.AdminEmp aemp = new Port.AdminEmp();
			aemp.No = WebUser.getNo();
			aemp.RetrieveFromDBSources();

			if (aemp.getUserType() != 1)
			{
				return "err@您[" + WebUser.getNo() + "]已经不是二级管理员了.";
			}
			if (aemp.getRootOfForm().equals(""))
			{
				return "err@没有给二级管理员[" + WebUser.getNo() + "]设置表单树的权限...";
			}

			DataRow[] rootRows = dtForm.Select("No='" + aemp.getRootOfForm() + "'");
			DataRow newRootRow = rootRows[0];

			newRootRow.set("ParentNo", "0");
			DataTable newDt = dtForm.Clone();
			newDt.Rows.add(newRootRow.ItemArray);

			GenerChildRows(dtForm, newDt, newRootRow);
			dtForm = newDt;
		}

		String str = BP.Tools.Json.ToJson(dtForm);
		return str;
	}


	public final String GetStructureTreeTable()
	{
		DataTable dt = new DataTable();
		dt.Columns.Add("NO", String.class);
		dt.Columns.Add("PARENTNO", String.class);
		dt.Columns.Add("NAME", String.class);
		dt.Columns.Add("TTYPE", String.class);


		BP.GPM.Depts depts = new BP.GPM.Depts();
		depts.RetrieveAll();
		BP.GPM.Stations sts = new BP.GPM.Stations();
		sts.RetrieveAll();
		BP.GPM.Emps emps = new BP.GPM.Emps();
		emps.RetrieveAll(BP.WF.Port.EmpAttr.Name);
		BP.GPM.DeptStations dss = new BP.GPM.DeptStations();
		dss.RetrieveAll();
		BP.GPM.DeptEmpStations dess = new BP.GPM.DeptEmpStations();
		dess.RetrieveAll();
		BP.GPM.Station stt = null;
		BP.GPM.Emp empt = null;

		for (BP.GPM.Dept dept : depts.ToJavaList())
		{
			//增加部门
			dt.Rows.add(dept.No, dept.ParentNo, dept.Name, "DEPT");

			//增加部门岗位
			dss.Retrieve(BP.GPM.DeptStationAttr.FK_Dept, dept.No);
			for (BP.GPM.DeptStation ds : dss)
			{
				Object tempVar = sts.GetEntityByKey(ds.FK_Station);
				stt = tempVar instanceof BP.GPM.Station ? (BP.GPM.Station)tempVar : null;

				if (stt == null)
				{
					continue;
				}

				dt.Rows.add(dept.No + "|" + ds.FK_Station, dept.No, stt.Name, "STATION");

				//增加部门岗位人员
				dess.Retrieve(BP.GPM.DeptEmpStationAttr.FK_Dept, dept.No, BP.GPM.DeptEmpStationAttr.FK_Station, ds.FK_Station);

				for (BP.GPM.DeptEmpStation des : dess)
				{
					Object tempVar2 = emps.GetEntityByKey(des.FK_Emp);
					empt = tempVar2 instanceof BP.GPM.Emp ? (BP.GPM.Emp)tempVar2 : null;

					if (empt == null)
					{
						continue;
					}

					dt.Rows.add(dept.No + "|" + ds.FK_Station + "|" + des.FK_Emp, dept.No + "|" + ds.FK_Station, empt.Name, "EMP");
				}
			}
		}

		return BP.Tools.Json.ToJson(dt);
	}

	/** 
	 获取设计器 - 系统维护菜单数据
	 系统维护管理员菜单 需要翻译
	 
	 @return 
	*/
	public final String GetTreeJson_AdminMenu()
	{
		//查询全部.
		AdminMenuGroups groups = new AdminMenuGroups();
		groups.RetrieveAll();

		AdminMenus menus = new AdminMenus();
		menus.RetrieveAll();

		// 定义容器.
		AdminMenus newMenus = new AdminMenus();

		for (AdminMenuGroup menu : groups)
		{
			//是否可以使用？
			if (menu.IsCanUse(WebUser.getNo()) == false)
			{
				continue;
			}

			AdminMenu newMenu = new AdminMenu();
			newMenu.setNo(menu.getNo());
			newMenu.setName(menu.getName());
			newMenu.setGroupNo("0");
			newMenu.setFor(menu.getFor());
			newMenu.setUrl("");
			newMenus.Add(newMenu);
		}

		for (AdminMenu menu : menus)
		{
			//是否可以使用？
			if (menu.IsCanUse(WebUser.getNo()) == false)
			{
				continue;
			}

			newMenus.Add(menu);
		}
		//添加默认，无权限
		if (newMenus.size() == 0)
		{
			AdminMenu menu = new AdminMenu();
			menu.setNo("1");
			menu.setGroupNo("0");
			menu.setName("无权限");
			menu.setUrl("");
			newMenus.Add(menu);
		}
		DataTable dt = newMenus.ToDataTable();
		return BP.Tools.Json.ToJson(newMenus.ToDataTable());
	}

	/** 
	 根据DataTable生成Json树结构
	*/

	public final String GetTreeJsonByTable(DataTable tabel, Object pId, String rela, String idCol, String txtCol, String IsParent, String sChecked)
	{
		return GetTreeJsonByTable(tabel, pId, rela, idCol, txtCol, IsParent, sChecked, null);
	}

	public final String GetTreeJsonByTable(DataTable tabel, Object pId, String rela, String idCol, String txtCol, String IsParent)
	{
		return GetTreeJsonByTable(tabel, pId, rela, idCol, txtCol, IsParent, "", null);
	}

	public final String GetTreeJsonByTable(DataTable tabel, Object pId, String rela, String idCol, String txtCol)
	{
		return GetTreeJsonByTable(tabel, pId, rela, idCol, txtCol, "IsParent", "", null);
	}

	public final String GetTreeJsonByTable(DataTable tabel, Object pId, String rela, String idCol)
	{
		return GetTreeJsonByTable(tabel, pId, rela, idCol, "Name", "IsParent", "", null);
	}

	public final String GetTreeJsonByTable(DataTable tabel, Object pId, String rela)
	{
		return GetTreeJsonByTable(tabel, pId, rela, "No", "Name", "IsParent", "", null);
	}

	public final String GetTreeJsonByTable(DataTable tabel, Object pId)
	{
		return GetTreeJsonByTable(tabel, pId, "ParentNo", "No", "Name", "IsParent", "", null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public string GetTreeJsonByTable(DataTable tabel, object pId, string rela = "ParentNo", string idCol = "No", string txtCol = "Name", string IsParent = "IsParent", string sChecked = "", string[] attrFields = null)
	public final String GetTreeJsonByTable(DataTable tabel, Object pId, String rela, String idCol, String txtCol, String IsParent, String sChecked, String[] attrFields)
	{
		String treeJson = "";

		if (tabel.Rows.size() > 0)
		{
			sbJson.append("[");
			String filer = "";
			if (pId.toString().equals(""))
			{
				filer = String.format("%1$s is null or %1$s='-1' or %1$s='0' or %1$s='F0'", rela);
			}
			else
			{
				filer = String.format("%1$s='%2$s'", rela, pId);
			}

			DataRow[] rows = tabel.Select(filer, idCol);
			if (rows.length > 0)
			{
				for (int i = 0; i < rows.length; i++)
				{
					DataRow row = rows[i];

					String jNo = row.get(idCol) instanceof String ? (String)row.get(idCol) : null;
					String jText = row.get(txtCol) instanceof String ? (String)row.get(txtCol) : null;
					if (jText.length() > 25)
					{
						jText = jText.substring(0, 25) + "<img src='../Scripts/easyUI/themes/icons/add2.png' onclick='moreText(" + jNo + ")'/>";
					}

					String jIsParent = row.get(IsParent).toString();
					String jState = "1".equals(jIsParent) ? "open" : "closed";
					jState = "open".equals(jState) && i == 0 ? "open" : "closed";

					DataRow[] rowChild = tabel.Select(String.format("%1$s='%2$s'", rela, jNo));
					String tmp = "{\"id\":\"" + jNo + "\",\"text\":\"" + jText;

					//增加自定义attributes列，added by liuxc,2015-10-6
					String attrs = "";
					if (attrFields != null && attrFields.length > 0)
					{
						for (String field : attrFields)
						{
							if (!tabel.Columns.Contains(field))
							{
								continue;
							}
							if (DataType.IsNullOrEmpty(row.get(field).toString()))
							{
								attrs += ",\"" + field + "\":\"\"";
								continue;
							}
							attrs += ",\"" + field + "\":" + (tabel.Columns[field].DataType == String.class ? String.format("\"%1$s\"", row.get(field)) : row.get(field));
						}
					}

					if ("0".equals(pId.toString()) || row.get(rela).toString().equals("F0"))
					{
						tmp += "\",\"attributes\":{\"IsParent\":\"" + jIsParent + "\",\"IsRoot\":\"1\"" + attrs + "}";
					}
					else
					{
						tmp += "\",\"attributes\":{\"IsParent\":\"" + jIsParent + "\"" + attrs + "}";
					}

					if (rowChild.length > 0)
					{
						tmp += ",\"checked\":" + String.valueOf(sChecked.contains("," + jNo + ",")).toLowerCase() + ",\"state\":\"" + jState + "\"";
					}
					else
					{
						tmp += ",\"checked\":" + String.valueOf(sChecked.contains("," + jNo + ",")).toLowerCase();
					}

					sbJson.append(tmp);
					if (rowChild.length > 0)
					{
						sbJson.append(",\"children\":");
						GetTreeJsonByTable(tabel, jNo, rela, idCol, txtCol, IsParent, sChecked, attrFields);
					}

					sbJson.append("},");
				}
				sbJson = sbJson.deleteCharAt(sbJson.length() - 1);
			}
			sbJson.append("]");
			treeJson = sbJson.toString();
		}
		return treeJson;
	}
	public final String NewFlow()
	{
		try
		{
			String[] ps = this.GetRequestVal("paras").split("[,]", -1);
			if (ps.length != 6)
			{
				throw new RuntimeException("@创建流程参数错误");
			}

			String fk_floSort = ps[0]; //类别编号.
			fk_floSort = fk_floSort.replace("F", ""); //传入的编号多出F符号，需要替换掉

			String flowName = ps[1]; // 流程名称.
			DataStoreModel dataSaveModel = DataStoreModel.forValue(Integer.parseInt(ps[2])); //数据保存方式。
			String pTable = ps[3]; // 物理表名.
			String flowMark = ps[4]; // 流程标记.
			String flowVer = ps[5]; // 流程版本.

			String flowNo = BP.WF.Template.TemplateGlo.NewFlow(fk_floSort, flowName, dataSaveModel, pTable, flowMark, flowVer);

			//清空WF_Emp 的StartFlows
			DBAccess.RunSQL("UPDATE  WF_Emp Set StartFlows =''");
			return flowNo;
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 上移流程
	 
	 @return 
	*/
	public final String MoveUpFlow()
	{
		Flow flow = new Flow(this.getFK_Flow());
		flow.DoUp();
		return flow.No;
	}
	/** 
	 下移流程
	 
	 @return 
	*/
	public final String MoveDownFlow()
	{
		Flow flow = new Flow(this.getFK_Flow());
		flow.DoDown();
		return flow.No;
	}
	/** 
	 删除流程类别.
	 
	 @return 
	*/
	public final String DelFlowSort()
	{
		String fk_flowSort = this.GetRequestVal("FK_FlowSort").replace("F", "");

		FlowSort fs = new FlowSort();
		fs.No = fk_flowSort;

		//检查是否有流程？
		Paras ps = new Paras();
		ps.SQL = "SELECT COUNT(*) FROM WF_Flow WHERE FK_FlowSort=" + SystemConfig.getAppCenterDBVarStr() + "fk_flowSort";
		ps.Add("fk_flowSort", fk_flowSort);
		//string sql = "SELECT COUNT(*) FROM WF_Flow WHERE FK_FlowSort='" + fk_flowSort + "'";
		if (DBAccess.RunSQLReturnValInt(ps) != 0)
		{
			return "err@该目录下有流程，您不能删除。";
		}

		//检查是否有子目录？
		ps = new Paras();
		ps.SQL = "SELECT COUNT(*) FROM WF_FlowSort WHERE ParentNo=" + SystemConfig.getAppCenterDBVarStr() + "ParentNo";
		ps.Add("ParentNo", fk_flowSort);
		//sql = "SELECT COUNT(*) FROM WF_FlowSort WHERE ParentNo='" + fk_flowSort + "'";
		if (DBAccess.RunSQLReturnValInt(ps) != 0)
		{
			return "err@该目录下有子目录，您不能删除。";
		}

		fs.Delete();

		return "删除成功.";
	}
	/** 
	 新建同级流程类别 对照需要翻译
	 
	 @return 
	*/
	public final String NewSameLevelFlowSort()
	{
		FlowSort fs = null;
		fs = new FlowSort(this.getNo().replace("F", "")); //传入的编号多出F符号，需要替换掉.

		String orgNo = fs.getOrgNo(); //记录原来的组织结构编号. 对照需要翻译

		String sameNodeNo = fs.DoCreateSameLevelNode().No;
		fs = new FlowSort(sameNodeNo);
		fs.Name = this.getName();
		fs.setOrgNo(orgNo); // 组织结构编号. 对照需要翻译
		fs.Update();
		return "F" + fs.No;
	}
	/** 
	 新建下级类别. 
	 
	 @return 
	*/
	public final String NewSubFlowSort()
	{
		FlowSort fsSub = new FlowSort(this.getNo().replace("F", "")); //传入的编号多出F符号，需要替换掉.
		String orgNo = fsSub.getOrgNo(); //记录原来的组织结构编号. 对照需要翻译

		String subNodeNo = fsSub.DoCreateSubNode().No;
		FlowSort subFlowSort = new FlowSort(subNodeNo);
		subFlowSort.Name = this.getName();
		subFlowSort.setOrgNo(orgNo); // 组织结构编号. 对照需要翻译.
		subFlowSort.Update();
		return "F" + subFlowSort.No;
	}
	/** 
	 上移流程类别
	 
	 @return 
	*/
	public final String MoveUpFlowSort()
	{
		String fk_flowSort = this.GetRequestVal("FK_FlowSort").replace("F", "");
		FlowSort fsSub = new FlowSort(fk_flowSort); //传入的编号多出F符号，需要替换掉
		fsSub.DoUp();
		return "F" + fsSub.No;
	}
	/** 
	 下移流程类别
	 
	 @return 
	*/
	public final String MoveDownFlowSort()
	{
		String fk_flowSort = this.GetRequestVal("FK_FlowSort").replace("F", "");
		FlowSort fsSub = new FlowSort(fk_flowSort); //传入的编号多出F符号，需要替换掉
		fsSub.DoDown();
		return "F" + fsSub.No;
	}

	/** 
	 表单树 - 编辑表单类别
	 
	 @return 
	*/
	public final String CCForm_EditCCFormSort()
	{
		SysFormTree formTree = new SysFormTree(this.getNo());
		formTree.Name = this.getName();
		formTree.Update();
		return this.getNo();
	}
	/** 
	 表单树 - 删除表单类别
	 
	 @return 
	*/
	public final String CCForm_DelFormSort()
	{
		SysFormTree formTree = new SysFormTree(this.getNo());

		//检查是否有子类别？
		Paras ps = new Paras();
		ps.SQL = "SELECT COUNT(*) FROM Sys_FormTree WHERE ParentNo=" + SystemConfig.getAppCenterDBVarStr() + "ParentNo";
		ps.Add("ParentNo", this.getNo());
		//string sql = "SELECT COUNT(*) FROM Sys_FormTree WHERE ParentNo=' " + this.getNo()+ " '";
		if (DBAccess.RunSQLReturnValInt(ps) != 0)
		{
			return "err@该目录下有子类别，您不能删除。";
		}

		//检查是否有表单？
		ps = new Paras();
		ps.SQL = "SELECT COUNT(*) FROM Sys_MapData WHERE FK_FormTree=" + SystemConfig.getAppCenterDBVarStr() + "FK_FormTree";
		ps.Add("FK_FormTree", this.getNo());
		//sql = "SELECT COUNT(*) FROM Sys_MapData WHERE FK_FormTree=' " + this.getNo()+ " '";
		if (DBAccess.RunSQLReturnValInt(ps) != 0)
		{
			return "err@该目录下有表单，您不能删除。";
		}

		formTree.Delete();
		return "删除成功";
	}
	/** 
	 表单树-上移表单类别
	 
	 @return 
	*/
	public final String CCForm_MoveUpCCFormSort()
	{
		SysFormTree formTree = new SysFormTree(this.getNo());
		formTree.DoUp();
		return formTree.No;
	}
	/** 
	 表单树-下移表单类别
	 
	 @return 
	*/
	public final String CCForm_MoveDownCCFormSort()
	{
		SysFormTree formTree = new SysFormTree(this.getNo());
		formTree.DoDown();
		return formTree.No;
	}

	/** 
	 表单树-上移表单
	 
	 @return 
	*/
	public final String CCForm_MoveUpCCFormTree()
	{
		MapData mapData = new MapData(this.getFK_MapData());
		mapData.DoUp();
		return mapData.No;
	}
	/** 
	 表单树-下移表单
	 
	 @return 
	*/
	public final String CCForm_MoveDownCCFormTree()
	{
		MapData mapData = new MapData(this.getFK_MapData());
		mapData.DoOrderDown();
		return mapData.No;
	}

	/** 
	 表单树 - 删除表单
	 
	 @return 
	*/
	public final String CCForm_DeleteCCFormMapData()
	{
		try
		{
			MapData mapData = new MapData(this.getFK_MapData());
			mapData.Delete();
			return "删除成功.";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}

	public final String EditFlowSort()
	{
		FlowSort fs = new FlowSort(); //传入的编号多出F符号，需要替换掉
		fs.No = tangible.StringHelper.trimStart(this.getNo(), 'F');
		fs.RetrieveFromDBSources();
		fs.Name = this.getName();
		fs.Update();
		return fs.No;
	}

	/** 
	 让admin 登陆
	 
	 @param lang 当前的语言
	 @return 成功则为空，有异常时返回异常信息
	*/
	public final String LetAdminLogin(String empNo, boolean islogin)
	{
		try
		{
			if (islogin)
			{
				BP.Port.Emp emp = new BP.Port.Emp(empNo);
				WebUser.SignInOfGener(emp);
			}
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
		return "@登录成功.";
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

}