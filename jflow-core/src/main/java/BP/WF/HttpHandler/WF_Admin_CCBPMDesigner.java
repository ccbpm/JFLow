package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Difference.SystemConfig;
import BP.Difference.Handler.WebContralBase;
import BP.Sys.*;
import BP.Tools.StringHelper;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Template.*;
import BP.WF.XML.*;
import BP.WF.*;
import java.util.*;
import java.net.URLDecoder;
import java.time.*;

import static java.awt.SystemColor.menu;

/**
 初始化函数
 */
public class WF_Admin_CCBPMDesigner extends WebContralBase
{

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

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL
				|| SystemConfig.getAppCenterDBType() == DBType.DM)
		{
			dtFlowSorts.Columns.get(0).setColumnName("No");
			dtFlowSorts.Columns.get(1).setColumnName("Name");
			dtFlowSorts.Columns.get(2).setColumnName("ParentNo");
		}



		//sql = "SELECT No,Name, FK_Dept FROM Port_Emp WHERE FK_Dept='" + fk_dept + "' ";
		sql = "SELECT  No,(NO + '.' + NAME) as Name, 'F' + FK_FlowSort as ParentNo, Idx FROM WF_Flow where FK_FlowSort='" + fk_flowsort + "' ";
		sql += " ORDER BY Idx ";


		DataTable dtFlows = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dtFlows.TableName = "Flows";
		ds.Tables.add(dtFlows);
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL
				|| SystemConfig.getAppCenterDBType() == DBType.DM)
		{
			dtFlows.Columns.get(0).setColumnName("No");
			dtFlows.Columns.get(1).setColumnName("Name");
			dtFlows.Columns.get(2).setColumnName("FK_FlowSort");
		}

		//转化为 json
		return BP.Tools.Json.ToJson(ds);
	}

	/**
	 按照管理员登录.

	 @param
	 @return 登录信息
	  * @throws Exception
	 */
	public final String AdminerChang_LoginAs() throws Exception
	{
		String orgNo = this.GetRequestVal("OrgNo");

		//AdminEmp ad = new AdminEmp();
		//ad.No = userNo;
		//if (ad.RetrieveFromDBSources() == 0)
		//    return "err@用户名错误.";
		return "info@登录成功, 如果系统不能自动刷新，请手工刷新。";
	}

	public final String Flows_Init() throws Exception
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
			dr.setValue("FlowNo", fl.getNo());
			dr.setValue("FlowName", fl.getName());
			dr.setValue("NumOfRuning", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM  WF_GenerWorkFlow WHERE FK_Flow='" + fl.getNo() + "' AND WFState in (2,5)", 0));
			dr.setValue("NumOfOK", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM  WF_GenerWorkFlow WHERE FK_Flow='" + fl.getNo() + "' AND WFState = 3 ", 0));
			dr.setValue("NumOfEtc", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM  WF_GenerWorkFlow WHERE FK_Flow='" + fl.getNo() + "' AND WFState in (4,5,6,7,8) ", 0));

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

			//保存节点位置. @101,2,30@102,3,1
			String[] nodes = this.GetRequestVal("Nodes").split("[@]", -1);
			for (String item : nodes)
			{
				if (item == null || item.equals(""))
				{
					continue;
				}
				String[] strs = item.split("[,]", -1);
				DBAccess.RunSQL("UPDATE WF_Node SET X=" + strs[1] + ",Y=" + strs[2] + " WHERE NodeID=" + strs[0] );
			}


			//保存方向.

			String[] dirs = this.GetRequestVal("Dirs").split("[@]", -1);
			for (String item : dirs)
			{
				if (item == null || item.equals(""))
				{
					continue;
				}
				String[] strs = item.split("[,]", -1);
				DBAccess.RunSQL("DELETE FROM WF_Direction where MyPK='" + strs[0] + "'");
				DBAccess.RunSQL("INSERT INTO WF_Direction(MyPK,FK_Flow,Node,ToNode,IsCanBack) values('" + strs[0] + "','" + strs[1] + "','" + strs[2] + "','" + strs[3] + "'," + "0)");
			}


			//保存label位置.

			String[] labs = this.GetRequestVal("Labs").split("[@]", -1);
			for (String item : labs)
			{
				if (item == null || item.equals(""))
				{
					continue;
				}
				String[] strs = item.split("[,]", -1);
				DBAccess.RunSQL("UPDATE WF_LabNote SET X=" + strs[1] + ",Y=" + strs[2] + " WHERE MyPK='" + strs[0] + "' ");
			}


			return "保存成功.";

		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}

	/**
	 下载流程模版

	 @return
	  * @throws Exception
	 */
	public final String ExpFlowTemplete() throws Exception
	{
		Flow flow = new Flow(this.getFK_Flow());
		String fileXml = flow.GenerFlowXmlTemplete();
		String docs = DataType.ReadTextFile(fileXml);
		return docs;
	}
	public final String DownFormTemplete() throws Exception
	{
		DataSet ds = BP.Sys.CCFormAPI.GenerHisDataSet_AllEleInfo(this.getFK_MapData());
		String file = SystemConfig.getPathOfTemp() + this.getFK_MapData() + ".xml";
		ds.WriteXml(file,XmlWriteMode.IgnoreSchema,ds);
		String docs = DataType.ReadTextFile(file);
		return docs;

	}


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

	///#endregion 执行父类的重写方法.

	/**
	 使管理员登录使管理员登录    ///
	 @return
	  * @throws Exception
	 */
	public final String LetLogin() throws Exception
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
	  * @throws Exception
	 */
	public final String GetEmpsByStationTable() throws Exception
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

		for (BP.GPM.DeptEmpStation des : dess.ToJavaList())
		{
			Object tempVar = emps.GetEntityByKey(des.getFK_Emp());
			emp = tempVar instanceof BP.GPM.Emp ? (BP.GPM.Emp)tempVar : null;

			dt.Rows.AddDatas(emp.getNo(), deptid + "|" + stid, emp.getName(), "EMP");
		}

		return BP.Tools.Json.ToJson(dt);
	}

	public final String GetStructureTreeRootTable() throws Exception
	{
		DataTable dt = new DataTable();
		dt.Columns.Add("NO", String.class);
		dt.Columns.Add("PARENTNO", String.class);
		dt.Columns.Add("NAME", String.class);
		dt.Columns.Add("TTYPE", String.class);

		String parentrootid = this.GetRequestVal("parentrootid"); // context.Request.QueryString["parentrootid"];
		String newRootId = "";

		if (BP.WF.Glo.getOSModel() == OSModel.OneOne)
		{
			BP.WF.Port.Dept dept = new BP.WF.Port.Dept();

			if (!DataType.IsNullOrEmpty(newRootId))
			{
				if (dept.Retrieve(BP.WF.Port.DeptAttr.No, newRootId) == 0)
				{
					dept.setNo("-1");
					dept.setName("无部门");
					dept.setParentNo("");
				}
			}
			else
			{
				if (dept.Retrieve(BP.WF.Port.DeptAttr.ParentNo, parentrootid) == 0)
				{
					dept.setNo("-1");
					dept.setName("无部门");
					dept.setParentNo("");
				}
			}

			dt.Rows.AddDatas(dept.getNo(), dept.getParentNo(), dept.getName(), "DEPT");
		}
		else
		{
			BP.GPM.Dept dept = new BP.GPM.Dept();

			if (!DataType.IsNullOrEmpty(newRootId))
			{
				if (dept.Retrieve(BP.GPM.DeptAttr.No, newRootId) == 0)
				{
					dept.setNo("-1");
					dept.setName("无部门");
					dept.setParentNo("");
				}
			}
			else
			{
				if (dept.Retrieve(BP.GPM.DeptAttr.ParentNo, parentrootid) == 0)
				{
					dept.setNo("-1");
					dept.setName("无部门");
					dept.setParentNo("");
				}
			}

			dt.Rows.AddDatas(dept.getNo(), dept.getParentNo(), dept.getName(), "DEPT");
		}

		return BP.Tools.Json.ToJson(dt);
	}

	/**
	 获取指定部门下一级子部门及岗位列表

	 @return
	  * @throws Exception
	 */
	public final String GetSubDeptsTable() throws Exception
	{
		DataTable dt = new DataTable();
		dt.Columns.Add("NO", String.class);
		dt.Columns.Add("PARENTNO", String.class);
		dt.Columns.Add("NAME", String.class);
		dt.Columns.Add("TTYPE", String.class);

		String rootid = this.GetRequestVal("rootid"); // context.Request.QueryString["rootid"];


		BP.GPM.Depts depts = new BP.GPM.Depts();
		depts.Retrieve(BP.GPM.DeptAttr.ParentNo, rootid);
		BP.Port.Stations sts = new BP.Port.Stations();
		sts.RetrieveAll();
		BP.GPM.DeptStations dss = new BP.GPM.DeptStations();
		dss.Retrieve(BP.GPM.DeptStationAttr.FK_Dept, rootid);
		BP.GPM.DeptEmps des = new BP.GPM.DeptEmps();
		des.Retrieve(BP.GPM.DeptEmpAttr.FK_Dept, rootid);
		BP.GPM.DeptEmpStations dess = new BP.GPM.DeptEmpStations();
		dess.Retrieve(BP.GPM.DeptEmpStationAttr.FK_Dept, rootid);
		BP.Port.Station stt = null;
		BP.GPM.Emp emp = null;
		ArrayList<String> inemps = new ArrayList<String>();

		for (BP.GPM.Dept dept : depts.ToJavaList())
		{
			//增加部门
			dt.Rows.AddDatas(dept.getNo(), dept.getParentNo(), dept.getName(), "DEPT");
		}

		//增加部门岗位
		for (BP.GPM.DeptStation ds : dss.ToJavaList())
		{
			Object tempVar = sts.GetEntityByKey(ds.getFK_Station());
			stt = tempVar instanceof BP.Port.Station ? (BP.Port.Station)tempVar : null;

			if (stt == null)
			{
				continue;
			}

			dt.Rows.AddDatas(ds.getFK_Station(), rootid, stt.getName(), "STATION");
		}

		//增加没有岗位的人员
		for (BP.GPM.DeptEmp de : des.ToJavaList())
		{
			if (dess.GetEntityByKey(BP.GPM.DeptEmpStationAttr.FK_Emp, de.getFK_Emp()) == null)
			{
				if (inemps.contains(de.getFK_Emp()))
				{
					continue;
				}

				inemps.add(de.getFK_Emp());
			}
		}

		for (String inemp : inemps)
		{
			emp = new BP.GPM.Emp(inemp);
			dt.Rows.AddDatas(emp.getNo(), rootid, emp.getName(), "EMP");
		}

		return BP.Tools.Json.ToJson(dt);
	}


	///#region 主页.
	/**
	 初始化登录界面.

	 @return
	  * @throws Exception
	 */
	public final String Default_Init() throws Exception
	{
		try
		{
			//如果登录信息丢失了,就让其重新登录一次.
			if (DataType.IsNullOrEmpty(WebUser.getNoOfRel()) == true)
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
			ht.put("SysNo", SystemConfig.getSysNo());
			ht.put("SysName", SystemConfig.getSysName());

			ht.put("CustomerNo", SystemConfig.getCustomerNo());
			ht.put("CustomerName", SystemConfig.getCustomerName());

			//集成的平台.
			ht.put("RunOnPlant", SystemConfig.getRunOnPlant());

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

	///#endregion


	///#region 登录窗口.
	public final String Login_InitInfo()
	{
		Hashtable ht = new Hashtable();
		ht.put("SysNo", SystemConfig.getSysNo());
		ht.put("SysName", SystemConfig.getSysName());

		return BP.Tools.Json.ToJson(ht);
	}
	/**
	 初始化登录界面.

	 @return
	  * @throws Exception
	 */
	public final String Login_Init() throws Exception
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
					return "url@Default.htm?UserNo=" + userNo + "&Key=" + new Date().getTime();
				}
				else
				{
					return "url@Designer.htm?UserNo=" + userNo + "&FK_Flow=" + this.getFK_Flow() + "&Key=" + new Date().getTime();
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
	  * @throws Exception
	 */
	public final String Login_Submit() throws Exception
	{
		String[] para = new String[0];
		BP.Port.Emp emp = new BP.Port.Emp();
		emp.setNo(this.GetRequestVal("TB_No").trim());
		if (emp.RetrieveFromDBSources() == 0)
		{
			return "err@用户名或密码错误.";
		}
		//return BP.WF.Glo.lang("invalid_username_or_pwd", para);

		String pass = this.GetRequestVal("TB_PW").trim();
		if (emp.CheckPass(pass) == false)
		{
			return "err@用户名或密码错误.";
		}
		//return BP.WF.Glo.lang("invalid_username_or_pwd", para);

		//让其登录.
		BP.WF.Dev2Interface.Port_Login(emp.getNo());
		return "url@Default.htm?SID=" + emp.getSID() + "&UserNo=" + emp.getNo();
	}

	///#endregion 登录窗口.




	///#region 流程相关 Flow
	/**
	 获取流程所有元素

	 @return json data
	  * @throws Exception
	 */
	public final String Flow_AllElements_ResponseJson() throws Exception
	{
		BP.WF.Flow flow = new BP.WF.Flow();
		flow.setNo(this.getFK_Flow());
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


		// return BP.Tools.Json.ToJson(ds)
		return BP.Tools.Json.ToJson(ds);
	}

	///#endregion end Flow


	///#region 节点相关 Nodes
	/**
	 创建流程节点并返回编号

	 @return
	  * @throws Exception
	 */
	public final String CreateNode() throws Exception
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
	  * @throws Exception
	 */
	public final String DeleteNode() throws Exception
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
	  * @throws Exception
	 * @throws NumberFormatException
	 */
	public final String Node_EditNodeName() throws NumberFormatException, Exception
	{
		String FK_Node = this.GetValFromFrmByKey("NodeID");
		//string NodeName = System.Web.HttpContext.Current.Server.UrlDecode(this.GetValFromFrmByKey("NodeName"));
//		String NodeName = HttpContextHelper.UrlDecode(this.GetValFromFrmByKey("NodeName"));
		String NodeName = URLDecoder.decode(this.GetValFromFrmByKey("NodeName"), "UTF-8");
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
	  * @throws Exception
	 */
	public final String Node_ChangeRunModel() throws Exception
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

	///#endregion end Node


	///#region CCBPMDesigner
	/**
	 获取用户信息

	 @return
	  * @throws Exception
	 */
	public final String GetWebUserInfo() throws Exception
	{
		if (WebUser.getNo() == null)
		{
			return "err@当前用户没有登录，请登录后再试。";
		}

		Hashtable ht = new Hashtable();

		BP.Port.Emp emp = new BP.Port.Emp(WebUser.getNo());

		ht.put("No", emp.getNo());
		ht.put("Name", emp.getName());
		ht.put("FK_Dept", emp.getFK_Dept());
		ht.put("SID", emp.getSID());


		if (WebUser.getNo().equals("admin"))
		{
			ht.put("IsAdmin", "1");
			ht.put("RootOfDept", "0");
			ht.put("RootOfFlow", "F0");
			ht.put("RootOfForm", "");
		}

		return BP.Tools.Json.ToJsonEntityModel(ht);
	}

	private StringBuilder sbJson = new StringBuilder();
	/**
	 获取流程树数据

	 @return 返回结果Json,流程树
	  * @throws Exception
	 */
	public final String GetFlowTreeTable() throws Exception
	{
		String sql = "SELECT * FROM (SELECT 'F'+No as NO,'F'+ParentNo PARENTNO, NAME, IDX, 1 ISPARENT,'FLOWTYPE' TTYPE, -1 DTYPE FROM WF_FlowSort" + "\r\n" +
				"                           union " + "\r\n" +
				"                           SELECT NO, 'F'+FK_FlowSort as PARENTNO,(NO + '.' + NAME) as NAME,IDX,0 ISPARENT,'FLOW' TTYPE, 0 as DTYPE FROM WF_Flow) A  ORDER BY DTYPE, IDX ";

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL || SystemConfig.getAppCenterDBType() == DBType.DM)
		{
			sql = "SELECT * FROM (SELECT 'F'||No as NO,'F'||ParentNo as PARENTNO,NAME, IDX, 1 ISPARENT,'FLOWTYPE' TTYPE,-1 DTYPE FROM WF_FlowSort" + "\r\n" +
					"                        union " + "\r\n" +
					"                        SELECT NO, 'F'||FK_FlowSort as PARENTNO,NO||'.'||NAME as NAME,IDX,0 ISPARENT,'FLOW' TTYPE,0 as DTYPE FROM WF_Flow) A  ORDER BY DTYPE, IDX";
		}


		if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
		{
			sql = "SELECT * FROM (SELECT CONCAT('F', No) NO, CONCAT('F', ParentNo) PARENTNO, NAME, IDX, 1 ISPARENT,'FLOWTYPE' TTYPE,-1 DTYPE FROM WF_FlowSort" + "\r\n" +
					"                           union " + "\r\n" +
					"                           SELECT NO, CONCAT('F', FK_FlowSort) PARENTNO, CONCAT(NO, '.', NAME) NAME,IDX,0 ISPARENT,'FLOW' TTYPE, 0 as DTYPE FROM WF_Flow) A  ORDER BY DTYPE, IDX";
		}

		DataTable dt = DBAccess.RunSQLReturnTable(sql);


		if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
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
			fs.setNo("99");
			fs.setParentNo("0");
			fs.setName("流程树");
			fs.Insert();

			dt.Rows.AddDatas("F99", "F0", "流程树", 0, 1, "FLOWTYPE", -1);
		}
		else
		{
			List<DataRow> drs = dt.select("NAME='流程树'");
			if (drs.size() > 0 && (!"F0".equals(drs.get(0).getValue("PARENTNO"))))
			{
				drs.get(0).setValue("PARENTNO", "F0");
			}
		}

		String str = BP.Tools.Json.ToJson(dt);
		return str;
	}

	public final void GenerChildRows(DataTable dt, DataTable newDt, DataRow parentRow,String coloumns)
	{
		List<DataRow> rows = dt.select("ParentNo='" + parentRow.getValue(coloumns) + "'");;
		for(DataRow r : rows)
		{
			newDt.Rows.AddRow(r);

			GenerChildRows(dt, newDt, r,coloumns);
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
		sql.append("           )");

		DataTable dt = DBAccess.RunSQLReturnTable(String.format(sql.toString(), fk_flow));
		return BP.Tools.Json.ToJson(dt);
	}

	public final String GetFormTreeTable() throws Exception
	{

		///#region 检查数据是否符合规范.
		String rootNo = DBAccess.RunSQLReturnStringIsNull("SELECT No FROM Sys_FormTree WHERE ParentNo='' OR ParentNo IS NULL", null);
		if (DataType.IsNullOrEmpty(rootNo) == false)
		{
			//删除垃圾数据.
			DBAccess.RunSQL(String.format("DELETE FROM Sys_FormTree WHERE No='%1$s'", rootNo));
		}
		//检查根目录是否符合规范.
		FrmTree ft = new FrmTree();
		ft.setNo("1");
		if (ft.RetrieveFromDBSources() == 0)
		{
			ft.setName("表单库");
			ft.setParentNo("0");
			ft.Insert();
		}
		if (ft.getParentNo().equals("0") == false)
		{
			ft.setParentNo("0");
			ft.Update();
		}

		///#endregion 检查数据是否符合规范.

		//组织数据源.
		String sqls = "";

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle
				|| SystemConfig.getAppCenterDBType() == DBType.DM)
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
		DataTable dtSort = ds.Tables.get(0); //类别表.
		DataTable dtForm = ds.Tables.get(1).clone(); //表单表,这个是最终返回的数据.

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
		drFormRoot.setValue(0, rowsOfSort[0].getValue("No"));
		drFormRoot.setValue(1, "0");
		drFormRoot.setValue(2, rowsOfSort[0].getValue("Name"));
		drFormRoot.setValue(3, rowsOfSort[0].getValue("Idx"));
		drFormRoot.setValue(4, rowsOfSort[0].getValue("IsParent"));
		drFormRoot.setValue(5, rowsOfSort[0].getValue("TType"));
		dtForm.Rows.add(drFormRoot); //增加顶级类别..

		//把类别数据组装到form数据里.
		for (DataRow dr : dtSort.Rows)
		{
			DataRow drForm = dtForm.NewRow();
			drForm.setValue(0, dr.getValue("No"));
			drForm.setValue(1, dr.getValue("ParentNo"));
			drForm.setValue(2, dr.getValue("Name"));
			drForm.setValue(3, dr.getValue("Idx"));
			drForm.setValue(4, dr.getValue("IsParent"));
			drForm.setValue(5, dr.getValue("TType"));
			dtForm.Rows.add(drForm); //类别.
		}

		for (DataRow row : ds.Tables.get(1).Rows)
		{
			dtForm.Rows.add(row);
		}

		String str = BP.Tools.Json.ToJson(dtForm);
		return str;
	}


	public final String GetStructureTreeTable() throws Exception
	{
		DataTable dt = new DataTable();
		dt.Columns.Add("NO", String.class);
		dt.Columns.Add("PARENTNO", String.class);
		dt.Columns.Add("NAME", String.class);
		dt.Columns.Add("TTYPE", String.class);


		BP.GPM.Depts depts = new BP.GPM.Depts();
		depts.RetrieveAll();
		BP.Port.Stations sts = new BP.Port.Stations();
		sts.RetrieveAll();
		BP.GPM.Emps emps = new BP.GPM.Emps();
		emps.RetrieveAll(BP.WF.Port.EmpAttr.Name);
		BP.GPM.DeptStations dss = new BP.GPM.DeptStations();
		dss.RetrieveAll();
		BP.GPM.DeptEmpStations dess = new BP.GPM.DeptEmpStations();
		dess.RetrieveAll();
		BP.Port.Station stt = null;
		BP.GPM.Emp empt = null;

		for (BP.GPM.Dept dept : depts.ToJavaList())
		{
			//增加部门
			dt.Rows.AddDatas(dept.getNo(), dept.getParentNo(), dept.getName(), "DEPT");

			//增加部门岗位
			dss.Retrieve(BP.GPM.DeptStationAttr.FK_Dept, dept.getNo());
			for (BP.GPM.DeptStation ds : dss.ToJavaList())
			{
				Object tempVar = sts.GetEntityByKey(ds.getFK_Station());
				stt = tempVar instanceof BP.Port.Station ? (BP.Port.Station)tempVar : null;

				if (stt == null)
				{
					continue;
				}

				dt.Rows.AddDatas(dept.getNo() + "|" + ds.getFK_Station(), dept.getNo(), stt.getName(), "STATION");

				//增加部门岗位人员
				dess.Retrieve(BP.GPM.DeptEmpStationAttr.FK_Dept, dept.getNo(), BP.GPM.DeptEmpStationAttr.FK_Station, ds.getFK_Station());

				for (BP.GPM.DeptEmpStation des : dess.ToJavaList())
				{
					Object tempVar2 = emps.GetEntityByKey(des.getFK_Emp());
					empt = tempVar2 instanceof BP.GPM.Emp ? (BP.GPM.Emp)tempVar2 : null;

					if (empt == null)
					{
						continue;
					}

					dt.Rows.AddDatas(dept.getNo() + "|" + ds.getFK_Station() + "|" + des.getFK_Emp(), dept.getNo() + "|" + ds.getFK_Station(), empt.getName(), "EMP");
				}
			}
		}

		return BP.Tools.Json.ToJson(dt);
	}

	/**
	 获取设计器 - 系统维护菜单数据
	 系统维护管理员菜单 需要翻译

	 @return
	  * @throws Exception
	 */
	public final String GetTreeJson_AdminMenu() throws Exception
	{
		if (BP.WF.Glo.getCCBPMRunModel() == BP.Sys.CCBPMRunModel.GroupInc)
		{
			//查询全部.
			Admin2MenuGroups groups = new Admin2MenuGroups();
			groups.RetrieveAll();

			Admin2Menus menus = new Admin2Menus();
			menus.RetrieveAll();

			// 定义容器.
			Admin2Menus newMenus = new Admin2Menus();

			for(Admin2MenuGroup menu : groups.ToJavaList())
			{

				//是否可以使用？
				if (menu.IsCanUse(WebUser.getNo()) == false)
					continue;
				Admin2Menu newMenu = new Admin2Menu();
				newMenu.setNo(menu.getNo());
				newMenu.setName(menu.getName());
				newMenu.setGroupNo("0");
				newMenu.setFor(menu.getFor());
				newMenu.setUrl("");
				newMenus.Add(newMenu);
			}

			for(Admin2Menu menu : menus.ToJavaList())
			{
				newMenus.Add(menu);
			}
			//添加默认，无权限
			if (newMenus.size() == 0)
			{
				Admin2Menu menu = new Admin2Menu();
				menu.setNo("1");
				menu.setGroupNo("0");
				menu.setName("无权限");
				menu.setUrl("");
				newMenus.Add(menu);
			}
			DataTable dt = newMenus.ToDataTable();
			return BP.Tools.Json.ToJson(newMenus.ToDataTable());
		}else{
			//查询全部.
			AdminMenuGroups groups = new AdminMenuGroups();
			groups.RetrieveAll();

			AdminMenus menus = new AdminMenus();
			menus.RetrieveAll();

			// 定义容器.
			AdminMenus newMenus = new AdminMenus();

			for (AdminMenuGroup menu : groups.ToJavaList())
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

			for (AdminMenu menu : menus.ToJavaList())
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

			List<DataRow> rows = tabel.select(filer);
			if (rows.size() > 0)
			{
				for (int i = 0; i < rows.size(); i++)
				{
					DataRow row = rows.get(i);

					String jNo = row.getValue(idCol) instanceof String ? (String)row.getValue(idCol) : null;
					String jText = row.getValue(txtCol) instanceof String ? (String)row.getValue(txtCol) : null;
					if (jText.length() > 25)
					{
						jText = jText.substring(0, 25) + "<img src='../Scripts/easyUI/themes/icons/add2.png' onclick='moreText(" + jNo + ")'/>";
					}

					String jIsParent = row.getValue(IsParent).toString();
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
							if (!tabel.Columns.contains(field))
							{
								continue;
							}
							if (DataType.IsNullOrEmpty(row.getValue(field).toString()))
							{
								attrs += ",\"" + field + "\":\"\"";
								continue;
							}
							attrs += ",\"" + field + "\":" + (tabel.Columns.get(field).DataType == String.class ? String.format("\"%1$s\"", row.getValue(field)) : row.getValue(field));
						}
					}

					if ("0".equals(pId.toString()) || row.getValue(rela).toString().equals("F0"))
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

	/**
	 *  创建一个新流程模版2019版本.
	 * @return
	 */
	public String Defualt_NewFlow()
	{
		try
		{
			int runModel = this.GetRequestValInt("RunModel");
			String FlowName = this.GetRequestVal("FlowName");
			String FlowSort = this.GetRequestVal("FlowSort").trim();
			FlowSort = FlowSort.trim();

			int DataStoreModel = this.GetRequestValInt("DataStoreModel");
			String PTable = this.GetRequestVal("PTable");
			String FlowMark = this.GetRequestVal("FlowMark");
			int flowFrmType = this.GetRequestValInt("FlowFrmType");
			String FrmUrl = this.GetRequestVal("FrmUrl");
			String FlowVersion = this.GetRequestVal("FlowVersion");

			String flowNo = BP.WF.Template.TemplateGlo.NewFlow(FlowSort, FlowName,
					BP.WF.Template.DataStoreModel.SpecTable, PTable, FlowMark, FlowVersion);

			Flow fl = new Flow(flowNo);

			//对极简版特殊处理.
			//如果是简洁版.
			if (runModel == 1)
			{
				fl.setFlowFrmType(BP.WF.FlowFrmType.forValue(flowFrmType));
				fl.Update(); //更新表单类型.

				//预制权限数据.
				int nodeID = Integer.parseInt(fl.getNo() + "01");
				FrmNode fn = new FrmNode();
				fn.setFK_Frm("ND" + nodeID);
				fn.setIsEnableFWC(FrmWorkCheckSta.Disable);
				fn.setFK_Node(nodeID);
				fn.setFK_Flow(flowNo);
				fn.setFrmSln(FrmSln.Default);
				fn.Insert();

				nodeID = Integer.parseInt(fl.getNo() + "02");
				fn = new FrmNode();
				fn.setFK_Frm("ND" + nodeID);
				fn.setIsEnableFWC(FrmWorkCheckSta.Disable);
				fn.setFK_Node(nodeID);
				fn.setFK_Flow(flowNo);
				fn.setFrmSln(FrmSln.Default);
				fn.Insert();


			}
			//对极简版特殊处理.


			//清空WF_Emp 的StartFlows ,让其重新计算.
			DBAccess.RunSQL("UPDATE  WF_Emp Set StartFlows =''");
			return flowNo;
		}
		catch (Exception ex)
		{
			return "err@" + ex.getMessage();
		}
	}

	/**
	 上移流程

	 @return
	  * @throws Exception
	 */
	public final String MoveUpFlow() throws Exception
	{
		Flow flow = new Flow(this.getFK_Flow());
		flow.DoUp();
		return flow.getNo();
	}
	/**
	 下移流程

	 @return
	  * @throws Exception
	 */
	public final String MoveDownFlow() throws Exception
	{
		Flow flow = new Flow(this.getFK_Flow());
		flow.DoDown();
		return flow.getNo();
	}
	/**
	 删除流程类别.

	 @return
	  * @throws Exception
	 */
	public final String DelFlowSort() throws Exception
	{
		String fk_flowSort = this.GetRequestVal("FK_FlowSort").replace("F", "");

		FlowSort fs = new FlowSort();
		fs.setNo(fk_flowSort);

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
	  * @throws Exception
	 */
	public final String NewSameLevelFlowSort() throws Exception
	{
		FlowSort fs = null;
		fs = new FlowSort(this.getNo().replace("F", "")); //传入的编号多出F符号，需要替换掉.

		String orgNo = fs.getOrgNo(); //记录原来的组织结构编号. 对照需要翻译

		String sameNodeNo = fs.DoCreateSameLevelNode().getNo();
		fs = new FlowSort(sameNodeNo);
		fs.setName(this.getName());
		fs.setOrgNo(orgNo); // 组织结构编号. 对照需要翻译
		fs.Update();
		return "F" + fs.getNo();
	}
	/**
	 新建下级类别.

	 @return
	  * @throws Exception
	 */
	public final String NewSubFlowSort() throws Exception
	{
		FlowSort fsSub = new FlowSort(this.getNo().replace("F", "")); //传入的编号多出F符号，需要替换掉.
		String orgNo = fsSub.getOrgNo(); //记录原来的组织结构编号. 对照需要翻译

		String subNodeNo = fsSub.DoCreateSubNode().getNo();
		FlowSort subFlowSort = new FlowSort(subNodeNo);
		subFlowSort.setName(this.getName());
		subFlowSort.setOrgNo(orgNo); // 组织结构编号. 对照需要翻译.
		subFlowSort.Update();
		return "F" + subFlowSort.getNo();
	}
	/**
	 上移流程类别

	 @return
	  * @throws Exception
	 */
	public final String MoveUpFlowSort() throws Exception
	{
		String fk_flowSort = this.GetRequestVal("FK_FlowSort").replace("F", "");
		FlowSort fsSub = new FlowSort(fk_flowSort); //传入的编号多出F符号，需要替换掉
		fsSub.DoUp();
		return "F" + fsSub.getNo();
	}
	/**
	 下移流程类别

	 @return
	  * @throws Exception
	 */
	public final String MoveDownFlowSort() throws Exception
	{
		String fk_flowSort = this.GetRequestVal("FK_FlowSort").replace("F", "");
		FlowSort fsSub = new FlowSort(fk_flowSort); //传入的编号多出F符号，需要替换掉
		fsSub.DoDown();
		return "F" + fsSub.getNo();
	}

	/**
	 表单树 - 编辑表单类别

	 @return
	  * @throws Exception
	 */
	public final String CCForm_EditCCFormSort() throws Exception
	{
		SysFormTree formTree = new SysFormTree(this.getNo());
		formTree.setName(this.getName());
		formTree.Update();
		return this.getNo();
	}
	/**
	 表单树 - 删除表单类别

	 @return
	  * @throws Exception
	 */
	public final String CCForm_DelFormSort() throws Exception
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
	  * @throws Exception
	 */
	public final String CCForm_MoveUpCCFormSort() throws Exception
	{
		SysFormTree formTree = new SysFormTree(this.getNo());
		formTree.DoUp();
		return formTree.getNo();
	}
	/**
	 表单树-下移表单类别

	 @return
	  * @throws Exception
	 */
	public final String CCForm_MoveDownCCFormSort() throws Exception
	{
		SysFormTree formTree = new SysFormTree(this.getNo());
		formTree.DoDown();
		return formTree.getNo();
	}

	/**
	 表单树-上移表单

	 @return
	  * @throws Exception
	 */
	public final String CCForm_MoveUpCCFormTree() throws Exception
	{
		MapData mapData = new MapData(this.getFK_MapData());
		mapData.DoUp();
		return mapData.getNo();
	}
	/**
	 表单树-下移表单

	 @return
	  * @throws Exception
	 */
	public final String CCForm_MoveDownCCFormTree() throws Exception
	{
		MapData mapData = new MapData(this.getFK_MapData());
		mapData.DoOrderDown();
		return mapData.getNo();
	}

	/**
	 表单树 - 删除表单

	 @return
	  * @throws Exception
	 */
	public final String CCForm_DeleteCCFormMapData() throws Exception
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

	public final String EditFlowSort() throws Exception
	{
		FlowSort fs = new FlowSort(); //传入的编号多出F符号，需要替换掉
		fs.setNo(StringHelper.trimStart(this.getNo(), 'F'));
		fs.RetrieveFromDBSources();
		fs.setName(this.getName());
		fs.Update();
		return fs.getNo();

	}

	/**
	 让admin 登陆

	 @param empNo 当前的语言
	 @return 成功则为空，有异常时返回异常信息
	  * @throws Exception
	 */
	public final String LetAdminLogin(String empNo, boolean islogin) throws Exception
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

	///#endregion

}