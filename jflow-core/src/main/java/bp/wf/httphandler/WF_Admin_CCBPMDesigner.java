package bp.wf.httphandler;

import bp.da.*;
import bp.difference.handler.WebContralBase;
import bp.web.*;
import bp.wf.Glo;
import bp.wf.template.*;
import bp.wf.template.sflow.SubFlows;
import bp.wf.xml.*;
import bp.wf.port.admin2group.*;
import bp.difference.*;
import bp.*;
import bp.wf.*;

import java.net.URLDecoder;
import java.util.*;
import java.time.*;

/** 
 初始化函数 
*/
public class WF_Admin_CCBPMDesigner extends WebContralBase
{
	/** 
	 选择器
	 
	 @return 
	*/
	public final String SelectFlows_Init() throws Exception {
		String fk_flowsort = this.GetRequestVal("FK_FlowSort").substring(1);

		if (DataType.IsNullOrEmpty(fk_flowsort) == true || fk_flowsort.equals("undefined") == true)
		{
			fk_flowsort = "99";
		}
		DataSet ds = new DataSet();

		String sql = "";
		if (DBAccess.getAppCenterDBType() == DBType.MySQL)
		{
			sql = "SELECT CONCAT('F' , No) as No,Name, CONCAT('F' ,ParentNo) as ParentNo FROM WF_FlowSort WHERE No='" + fk_flowsort + "' OR ParentNo='" + fk_flowsort + "' ORDER BY Idx";
		}
		else
		{
			sql = "SELECT 'F' + No as No,Name, 'F' + ParentNo as ParentNo FROM WF_FlowSort WHERE No='" + fk_flowsort + "' OR ParentNo='" + fk_flowsort + "' ORDER BY Idx";
		}

		DataTable dtFlowSorts = DBAccess.RunSQLReturnTable(sql);
		//if (dtFlowSort.Rows.size() == 0)
		//{
		//    fk_dept = bp.web.WebUser.getFK_Dept();
		//    sql = "SELECT No,Name,ParentNo FROM Port_Dept WHERE No='" + fk_dept + "' OR ParentNo='" + fk_dept + "' ORDER BY Idx ";
		//    dtDept = DBAccess.RunSQLReturnTable(sql);
		//}

		dtFlowSorts.TableName = "FlowSorts";
		ds.Tables.add(dtFlowSorts);

		if (SystemConfig.AppCenterDBFieldCaseModel() != FieldCaseModel.None)
		{
			dtFlowSorts.Columns.get(0).setColumnName("No");
			dtFlowSorts.Columns.get(1).setColumnName("Name");
			dtFlowSorts.Columns.get(2).setColumnName("ParentNo");
		}

		//sql = "SELECT No,Name, FK_Dept FROM Port_Emp WHERE FK_Dept='" + fk_dept + "' ";

		if (DBAccess.getAppCenterDBType() == DBType.MySQL)
		{

			sql = "SELECT  No,CONCAT(NO ,'.',NAME) as Name, CONCAT('F',FK_FlowSort) as ParentNo, Idx FROM WF_Flow where FK_FlowSort='" + fk_flowsort + "' ";
		}
		else
		{
			sql = "SELECT  No,(NO + '.' + NAME) as Name, 'F' + FK_FlowSort as ParentNo, Idx FROM WF_Flow where FK_FlowSort='" + fk_flowsort + "' ";
		}

		sql += " ORDER BY Idx ";

		DataTable dtFlows = DBAccess.RunSQLReturnTable(sql);
		dtFlows.TableName = "Flows";
		ds.Tables.add(dtFlows);
		if (SystemConfig.AppCenterDBFieldCaseModel() != FieldCaseModel.None)
		{
			dtFlows.Columns.get(0).setColumnName("No");
			dtFlows.Columns.get(1).setColumnName("Name");
			dtFlows.Columns.get(2).setColumnName("FK_FlowSort");
		}

		//转化为 json 
		return bp.tools.Json.ToJson(ds);
	}

	/** 
	 按照管理员登录.
	 
	 param userNo 管理员编号
	 @return 登录信息
	*/
	public final String AdminerChang_LoginAs() throws Exception {
		String orgNo = this.GetRequestVal("OrgNo");
		WebUser.setOrgNo(this.getOrgNo());
		return "info@登录成功, 如果系统不能自动刷新，请手工刷新。";
	}

	public final String Flows_Init() throws Exception {
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
		return bp.tools.Json.ToJson(dt);
	}

	/** 
	 构造函数
	*/
	public WF_Admin_CCBPMDesigner() throws Exception {
	}
	/** 
	 执行流程设计图的保存.
	 
	 @return 
	*/
	public final String Designer_Save() throws Exception {

		if (WebUser.getIsAdmin() == false)
		{
			return "err@当前您【" + WebUser.getNo() + "," + WebUser.getName() + "】不是管理员,请重新登录.造成这种原因是您在测试容器没有正常退回造成的.";
		}

		String sql = "";
		try
		{
			Flow flow = new Flow(this.getFK_Flow());

			StringBuilder sBuilder = new StringBuilder();

			//保存方向.
			sBuilder = new StringBuilder();
			String[] dirs = this.GetRequestVal("Dirs").split("[@]", -1);

			Direction mydir = new Direction();
			for (String item : dirs)
			{
				if (item.equals("") || item == null)
				{
					continue;
				}
				String[] strs = item.split("[,]", -1);
				mydir.setMyPK(strs[0]);
				if (mydir.getIsExits() == true)
				{
					continue;
				}

				sBuilder.append("DELETE FROM WF_Direction WHERE MyPK='" + strs[0] + "';");

				sBuilder.append("INSERT INTO WF_Direction (MyPK,FK_Flow,Node,ToNode) VALUES ('" + strs[0] + "','" + strs[1] + "','" + strs[2] + "','" + strs[3] + "');");
			}
			DBAccess.RunSQLs(sBuilder.toString());

			//保存label位置.
			sBuilder = new StringBuilder();
			String[] labs = this.GetRequestVal("Labs").split("[@]", -1);
			for (String item : labs)
			{
				if (DataType.IsNullOrEmpty(item) == true)
				{
					continue;
				}
				String[] strs = item.split("[,]", -1);

				sBuilder.append("UPDATE WF_LabNote SET X=" + strs[1] + ",Y=" + strs[2] + " WHERE MyPK='" + strs[0] + "';");
			}

			String sqls = sBuilder.toString();
			DBAccess.RunSQLs(sqls);

			//更新节点 HisToNDs，不然就需要检查一遍.
			Nodes nds = new Nodes();
			nds.Retrieve(NodeAttr.FK_Flow, this.getFK_Flow(), null);

			//获得方向集合处理toNodes
			Directions mydirs = new Directions(this.getFK_Flow());

			String mystrs = "";
			for (Node item : nds.ToJavaList())
			{
				String strs = "";
				for (Direction dir : mydirs.ToJavaList())
				{
					if (dir.getNode() != item.getNodeID())
					{
						continue;
					}

					strs += "@" + dir.getToNode();
				}

				int nodePosType = 0;
				if (item.isStartNode() == true)
					nodePosType = 0;
				else if (DataType.IsNullOrEmpty(strs) == true)
					nodePosType = 2;
				else
					nodePosType = 1;

				DBAccess.RunSQL("UPDATE WF_Node SET HisToNDs='" + strs + "',NodePosType=" + nodePosType + "  WHERE NodeID=" + item.getNodeID());
				//DBAccess.RunSQL("UPDATE WF_Node SET HisToNDs='" + strs + "' WHERE NodeID=" + item.getNodeID());
			}

			//获取所有子流程
			String subs = this.GetRequestVal("SubFlows");
			int subFlowShowType = flow.GetValIntByKey(FlowAttr.SubFlowShowType);
			if(DataType.IsNullOrEmpty(subs)==false && subFlowShowType==0){
				String[]  subFlows = subs.split("[@]", -1);
				for(String item :subFlows){
					if (DataType.IsNullOrEmpty(item) == true)
						continue;
					String[] strs = item.split("[,]", -1);
					sBuilder.append("UPDATE WF_NodeSubFlow SET X=" + strs[1] + ",Y=" + strs[2] + " WHERE MyPK=" + strs[0] + ";");
				}
			}
			//保存节点位置. @101,2,30@102,3,1
			String[] nodes = this.GetRequestVal("Nodes").split("[@]", -1);
			for (String item : nodes)
			{
				if (DataType.IsNullOrEmpty(item) == true)
					continue;

				String[] strs = item.split("[,]", -1);
				String nodeID = strs[0]; //获得nodeID.
				if( subFlowShowType==1 && subs.indexOf(nodeID)!=-1){
					String sub = subs.substring(subs.indexOf("@\""+nodeID)+1);
					if(sub.contains("@")==true)
						sub = sub.substring(0,sub.indexOf("@"));
					String[] subInfo = sub.split("[,]", -1);
					sBuilder.append("UPDATE WF_Node SET X=" + strs[1] + ",Y=" + strs[2] + ",Name='" + strs[3] + "',SubFlowX="+subInfo[1]+", SubFlowY="+subInfo[2]+" WHERE NodeID=" + strs[0] + ";");
				}else{
					sBuilder.append("UPDATE WF_Node SET X=" + strs[1] + ",Y=" + strs[2] + ",Name='" + strs[3] + "' WHERE NodeID=" + strs[0] + ";");
				}
			}

			DBAccess.RunSQLs(sBuilder.toString());

			//DBAccess.RunSQL("update WF_Direction set ToNodeName=WF_Node.Name from WF_Node where WF_Direction.ToNode=WF_Node.NodeID AND WF_Direction.FK_FlOW='" + this.getFK_Flow()+"'");
			 //更新节点名称.
			switch (SystemConfig.getAppCenterDBType())
			{
				case MSSQL:
				case KingBaseR3:
				case KingBaseR6:
					sql = " UPDATE WF_Direction SET ToNodeName = WF_Node.Name FROM WF_Node  ";
					sql += " WHERE WF_Direction.ToNode = WF_Node.NodeID AND WF_Direction.FK_Flow='" + this.getFK_Flow() + "'";
					break;
				default:
					sql = "UPDATE WF_Direction A, WF_Node B SET A.ToNodeName=B.Name WHERE A.ToNode=B.NodeID AND A.FK_Flow='" + this.getFK_Flow() + "' ";
					break;
			}
			DBAccess.RunSQL(sql);
             //更新节点名称.
			//清楚缓存.
			Cash.ClearCash();
			// Node nd = new Node(102);
			// throw new Exception(nd.getName());

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
	*/
	public final String ExpFlowTemplete() throws Exception {
		Flow flow = new Flow(this.getFK_Flow());
		String fileXml = flow.GenerFlowXmlTemplete();
		String docs = DataType.ReadTextFile(fileXml);
		return docs;
	}
	/** 
	 返回临时文件.
	 
	 @return 
	*/
	public final String DownFormTemplete() throws Exception {
		DataSet ds = bp.sys.CCFormAPI.GenerHisDataSet_AllEleInfo(this.getFK_MapData());

		String file = SystemConfig.getPathOfTemp() + this.getFK_MapData() + ".xml";
		ds.WriteXml(file,XmlWriteMode.IgnoreSchema,ds);
		String docs = DataType.ReadTextFile(file);
		return docs;
	}

	/** 
	 使管理员登录使管理员登录    /// 
	 @return 
	*/
	public final String LetLogin() throws Exception {
		LetAdminLogin(this.GetRequestVal("UserNo"), true);
		return "登录成功.";
	}
	/** 
	 获得枚举列表的JSON.
	 
	 @return 
	*/
	public final String Logout() throws Exception {
		Dev2Interface.Port_SigOut();
		return "您已经安全退出,欢迎使用ccbpm.";
	}

	/** 
	 根据部门、岗位获取人员列表
	 
	 @return 
	*/
	public final String GetEmpsByStationTable() throws Exception {
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


		bp.port.Emp emp = null;
		bp.port.Emps emps = new bp.port.Emps();
		emps.RetrieveAll();

		bp.port.DeptEmpStations dess = new bp.port.DeptEmpStations();
		dess.Retrieve(bp.port.DeptEmpStationAttr.FK_Dept, deptid, bp.port.DeptEmpStationAttr.FK_Station, stid, null);

		for (bp.port.DeptEmpStation des : dess.ToJavaList())
		{
			Object tempVar = emps.GetEntityByKey(des.getFK_Emp());
			emp = tempVar instanceof bp.port.Emp ? (bp.port.Emp)tempVar : null;

			dt.Rows.AddDatas(emp.getNo(), deptid + "|" + stid, emp.getName(), "EMP");
		}

		return bp.tools.Json.ToJson(dt);
	}

	public final String GetStructureTreeRootTable() throws Exception {
		DataTable dt = new DataTable();
		dt.Columns.Add("NO", String.class);
		dt.Columns.Add("PARENTNO", String.class);
		dt.Columns.Add("NAME", String.class);
		dt.Columns.Add("TTYPE", String.class);

		String parentrootid = this.GetRequestVal("parentrootid"); // context.Request.QueryString["parentrootid"];
		String newRootId = "";

		if (!WebUser.getNo().equals("admin"))
		{
			newRootId = WebUser.getOrgNo();
		}


		bp.port.Dept dept = new bp.port.Dept();

		if (!DataType.IsNullOrEmpty(newRootId))
		{
			if (dept.Retrieve(bp.port.DeptAttr.No, newRootId) == 0)
			{
				dept.setNo("-1");
				dept.setName("无部门");
				dept.setParentNo("");
			}
		}
		else
		{
			if (dept.Retrieve(bp.port.DeptAttr.ParentNo, parentrootid) == 0)
			{
				dept.setNo("-1");
				dept.setName("无部门");
				dept.setParentNo("");
			}
		}

		dt.Rows.AddDatas(dept.getNo(), dept.getParentNo(), dept.getName(), "DEPT");


		return bp.tools.Json.ToJson(dt);
	}

		///#region 主页.
	/** 
	 初始化登录界面.
	 
	 @return 
	*/
	public final String Default_Init() throws Exception {
		try
		{
			//如果登录信息丢失了,就让其重新登录一次.
			if (DataType.IsNullOrEmpty(WebUser.getNoOfRel()) == true)
			{
				String userNo = this.GetRequestVal("UserNo");
				String sid = this.GetRequestVal("Token");
				Dev2Interface.Port_LoginByToken(sid);
			}

			if (WebUser.getIsAdmin() == false)
			{
				return "url@Login.htm?DoType=Logout&Err=NoAdminUsers";
			}

			//如果没有流程表，就执行安装.
			if (DBAccess.IsExitsObject("WF_Flow") == false)
			{
				return "url@../DBInstall.htm";
			}

			Hashtable ht = new Hashtable();

			ht.put("OSModel", "1");

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
				String str = Glo.UpdataCCFlowVer();
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
			return bp.tools.Json.ToJsonEntityModel(ht);
		}
		catch (RuntimeException ex)
		{
			return "err@初始化界面期间出现如下错误:" + ex.getMessage();
		}
	}

		///#endregion


		///#region 登录窗口.
	public final String Login_InitInfo() throws Exception {
		Hashtable ht = new Hashtable();
		ht.put("SysNo", SystemConfig.getSysNo());
		ht.put("SysName", SystemConfig.getSysName());

		return bp.tools.Json.ToJson(ht);
	}
	/** 
	 初始化登录界面.
	 
	 @return 
	*/
	public final String Login_Init() throws Exception {
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
		//if (DataType.IsNullOrEmpty(bp.web.WebUser.getNo()) || bp.web.WebUser.getIsAdmin() == false)
		//    return "url@Login.htm?DoType=Logout";

		//如果没有流程表，就执行安装.
		if (DBAccess.IsExitsObject("WF_Flow") == false)
		{
			return "url@../DBInstall.htm";
		}

		//是否需要自动登录。 这里都把cookeis的数据获取来了.
		String userNo = this.GetRequestVal("UserNo");
		String sid = this.GetRequestVal("Token");

		if (DataType.IsNullOrEmpty(sid) == false && DataType.IsNullOrEmpty(userNo) == false)
		{
			/*  如果都有值，就需要他登录。 */
			try
			{
				String str = Glo.UpdataCCFlowVer();
				Dev2Interface.Port_LoginByToken(sid);
				if (this.getFK_Flow() == null)
				{
					return "url@Default.htm?UserNo=" + userNo + "&OrgNo=" + WebUser.getOrgNo() + "&Key=" + new Date() + "&Token=" + sid;
				}
				else
				{
					return "url@Designer.htm?UserNo=" + userNo + "&OrgNo=" + WebUser.getOrgNo() + "&FK_Flow=" + this.getFK_Flow() + "&Key=" + new Date() + "&Token=" + sid;
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
			String str = Glo.UpdataCCFlowVer();
			if (str == null)
			{
				str = "准备完毕,欢迎登录,当前小版本号为:" + Glo.Ver;
			}
			return str;
		}
		catch (RuntimeException ex)
		{
			String msg = "err@升级失败(ccbpm有自动修复功能,您可以刷新一下系统会自动创建字段,刷新多次扔解决不了问题,请反馈给我们)";
			msg += "@系统信息:" + ex.getMessage();
			return msg;
		}
	}
	//流程设计器登陆前台，转向规则，判断是否为天业BPM
	public final String Login_Redirect() throws Exception {
		if (SystemConfig.getCustomerNo().equals("TianYe"))
		{
			return "url@../../../BPM/pages/login.html";
		}

		return "url@../../AppClassic/Login.htm?DoType=Logout";
	}
	/** 
	初始化当前登录人的下的所有组织
	 
	 @return 
	*/
	public final String SelectOneOrg_Init() throws Exception {
		Orgs orgs = new Orgs();
		//            orgs.Retrieve("Adminer", WebUser.getNo());
		orgs.RetrieveInSQL("SELECT OrgNo FROM Port_OrgAdminer WHERE FK_Emp='" + WebUser.getNo() + "'");
		return orgs.ToJson("dt");
	}
	/** 
	选择一个组织
	 
	 @return 
	*/
	public final String SelectOneOrg_Selected() throws Exception {
		WebUser.setOrgNo(this.getOrgNo());

		//找到管理员所在的部门.
		String sql = "SELECT a.No FROM Port_Dept A,Port_DeptEmp B WHERE A.No=B.FK_Dept AND B.FK_Emp='" + WebUser.getNo() + "'  AND A.OrgNo='" + this.getOrgNo() + "'";
		String deptNo = DBAccess.RunSQLReturnStringIsNull(sql, this.getOrgNo());

		WebUser.setFK_Dept(deptNo);

		//执行更新到用户表信息.
		WebUser.UpdateSIDAndOrgNoSQL();

		return "url@Default.htm?Token=" + WebUser.getToken() + "&UserNo=" + WebUser.getNo() + "&OrgNo=" + WebUser.getOrgNo();
		// return "登录成功.";
	}

		///#endregion 登录窗口.



		///#region 流程相关 Flow
	/** 
	 获取流程所有元素
	 
	 @return json data
	*/
	public final String Flow_AllElements_ResponseJson() throws Exception {
		Flow flow = new Flow();
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


		// return BP.Tools.Json.DataSetToJson(ds, false);
		return bp.tools.Json.ToJson(ds);
	}

		///#endregion end Flow


		///#region 节点相关 Nodes
	/** 
	 gen
	 
	 param figureName
	 @return 
	*/
	public final RunModel Node_GetRunModelByFigureName(String figureName)
	{
		RunModel runModel = RunModel.Ordinary;
		switch (figureName)
		{
			case "NodeOrdinary":
				runModel = RunModel.Ordinary;
				break;
			case "NodeFL":
				runModel = RunModel.FL;
				break;
			case "NodeHL":
				runModel = RunModel.HL;
				break;
			case "NodeFHL":
				runModel = RunModel.FHL;
				break;
			case "NodeSubThread":
				runModel = RunModel.SubThread;
				break;
			default:
				runModel = RunModel.Ordinary;
				break;
		}
		return runModel;
	}
	/** 
	 根据节点编号删除流程节点
	 
	 @return 执行结果
	*/
	public final String DeleteNode() throws Exception {
		try
		{
			Node node = new Node();
			node.setNodeID(this.getFK_Node());
			if (node.RetrieveFromDBSources() == 0)
			{
				return "err@删除失败,没有删除到数据，估计该节点已经别删除了.";
			}

			if (node.isStartNode() == true)
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
	public final String Node_EditNodeName() throws Exception {
		String FK_Node = this.GetValFromFrmByKey("NodeID");
		//string NodeName = System.Web.HttpContext.Current.Server.UrlDecode(this.GetValFromFrmByKey("NodeName"));
		String NodeName = URLDecoder.decode(this.GetValFromFrmByKey("NodeName"), "UTF-8");

		Node node = new Node();
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
	public final String Node_ChangeRunModel() throws Exception {
		String runModel = GetValFromFrmByKey("RunModel");
		Node node = new Node(this.getFK_Node());
		//节点运行模式
		switch (runModel)
		{
			case "NodeOrdinary":
				node.setHisRunModel(RunModel.Ordinary);
				break;
			case "NodeFL":
				node.setHisRunModel(RunModel.FL);
				break;
			case "NodeHL":
				node.setHisRunModel(RunModel.HL);
				break;
			case "NodeFHL":
				node.setHisRunModel(RunModel.FHL);
				break;
			case "NodeSubThread":
				node.setHisRunModel(RunModel.SubThread);
				break;
		}
		node.Update();

		return "设置成功.";
	}

		///#endregion end Node


		///#region CCBPMDesigner

	private StringBuilder sbJson = new StringBuilder();
	public final void GenerChildRows(DataTable dt, DataTable newDt, DataRow parentRow)
	{
		DataRow[] rows = dt.Select("ParentNo='" + parentRow.getValue("NO") + "'");
		for (DataRow r : rows)
		{
			newDt.Rows.AddDatas(r.ItemArray);
			GenerChildRows(dt, newDt, r);
		}
	}

	public final String GetBindingFormsTable() throws Exception {
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
		sql.append("            ON  smd.getNo()=wfn.FK_Frm" + "\r\n");
		sql.append("WHERE  wfn.FK_Flow = '%1$s'" + "\r\n");
		sql.append("       AND wfn.FK_Node = (" + "\r\n");
		sql.append("               SELECT wn.NodeID" + "\r\n");
		sql.append("               FROM   WF_Node wn" + "\r\n");
		sql.append("               WHERE  wn.FK_Flow = '%1$s' AND wn.NodePosType = 0" + "\r\n");
		sql.append("           )" + "\r\n");

		DataTable dt = DBAccess.RunSQLReturnTable(String.format(sql.toString(), fk_flow));
		return bp.tools.Json.ToJson(dt);
	}
	/** 
	 获取设计器 - 系统维护菜单数据
	 系统维护管理员菜单 需要翻译
	 
	 @return 
	*/
	public final String GetTreeJson_AdminMenu() throws Exception {
		String treeJson = "";
		//查询全部.
		AdminMenus groups = new AdminMenus();
		groups.RetrieveAll();

		return bp.tools.Json.ToJson(groups.ToDataTable());
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

			//List<DataRow> rows = tabel.Select(filer, idCol);
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
	 上移流程类别
	 
	 @return 
	*/
	public final String MoveUpFlowSort() throws Exception {
		String fk_flowSort = this.GetRequestVal("FK_FlowSort").replace("F", "");
		FlowSort fsSub = new FlowSort(fk_flowSort); //传入的编号多出F符号，需要替换掉
		fsSub.DoUp();
		return "F" + fsSub.getNo();
	}
	/** 
	 下移流程类别
	 
	 @return 
	*/
	public final String MoveDownFlowSort() throws Exception {
		String fk_flowSort = this.GetRequestVal("FK_FlowSort").replace("F", "");
		FlowSort fsSub = new FlowSort(fk_flowSort); //传入的编号多出F符号，需要替换掉
		fsSub.DoDown();
		return "F" + fsSub.getNo();
	}
	/** 
	 上移流程
	 
	 @return 
	*/
	public final String MoveUpFlow() throws Exception {
		Flow flow = new Flow(this.getFK_Flow());
		flow.DoUp();
		return flow.getNo();
	}
	/** 
	 下移流程
	 
	 @return 
	*/
	public final String MoveDownFlow() throws Exception {
		Flow flow = new Flow(this.getFK_Flow());
		flow.DoDown();
		return flow.getNo();
	}
	/** 
	 删除流程类别.
	 
	 @return 
	*/
	public final String DelFlowSort() throws Exception {
		String fk_flowSort = this.GetRequestVal("FK_FlowSort").replace("F", "");

		FlowSort fs = new FlowSort();
		fs.setNo(fk_flowSort);

		//检查是否有流程？
		Paras ps = new Paras();
		ps.SQL = "SELECT COUNT(*) FROM WF_Flow WHERE FK_FlowSort=" + SystemConfig.getAppCenterDBVarStr() + "fk_flowSort";
		ps.Add("fk_flowSort", fk_flowSort, false);
		//string sql = "SELECT COUNT(*) FROM WF_Flow WHERE FK_FlowSort='" + fk_flowSort + "'";
		if (DBAccess.RunSQLReturnValInt(ps) != 0)
		{
			return "err@该目录下有流程，您不能删除。";
		}

		//检查是否有子目录？
		ps = new Paras();
		ps.SQL = "SELECT COUNT(*) FROM WF_FlowSort WHERE ParentNo=" + SystemConfig.getAppCenterDBVarStr() + "ParentNo";
		ps.Add("ParentNo", fk_flowSort, false);
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
	public final String NewSameLevelFlowSort() throws Exception {
		FlowSort fs = null;
		fs = new FlowSort(this.getNo().replace("F", "")); //传入的编号多出F符号，需要替换掉.

		String orgNo = fs.getOrgNo(); //记录原来的组织结构编号. 对照需要翻译

		String sameNodeNo = fs.DoCreateSameLevelNode(null).getNo();
		fs = new FlowSort(sameNodeNo);
		fs.setName(this.getName());
		fs.setOrgNo(orgNo); // 组织结构编号. 对照需要翻译
		fs.Update();
		return "F" + fs.getNo();
	}
	/** 
	 新建下级类别. 
	 
	 @return 
	*/
	public final String NewSubFlowSort() throws Exception {
		FlowSort fsSub = new FlowSort(this.getNo().replace("F", "")); //传入的编号多出F符号，需要替换掉.
		String orgNo = fsSub.getOrgNo(); //记录原来的组织结构编号. 对照需要翻译

		String subNodeNo = fsSub.DoCreateSubNode(null).getNo();
		FlowSort subFlowSort = new FlowSort(subNodeNo);
		subFlowSort.setName(this.getName());
		subFlowSort.setOrgNo(orgNo); // 组织结构编号. 对照需要翻译.
		subFlowSort.Update();
		return "F" + subFlowSort.getNo();
	}
	/** 
	 表单树 - 删除表单类别
	 
	 @return 
	*/
	public final String CCForm_DelFormSort() throws Exception {
		SysFormTree formTree = new SysFormTree(this.getNo());

		//检查是否有子类别？
		Paras ps = new Paras();
		ps.SQL = "SELECT COUNT(*) FROM Sys_FormTree WHERE ParentNo=" + SystemConfig.getAppCenterDBVarStr() + "ParentNo";
		ps.Add("ParentNo", this.getNo(), false);
		//string sql = "SELECT COUNT(*) FROM Sys_FormTree WHERE ParentNo='" + this.No + "'";
		if (DBAccess.RunSQLReturnValInt(ps) != 0)
		{
			return "err@该目录下有子类别，您不能删除。";
		}

		//检查是否有表单？
		ps = new Paras();
		ps.SQL = "SELECT COUNT(*) FROM Sys_MapData WHERE FK_FormTree=" + SystemConfig.getAppCenterDBVarStr() + "FK_FormTree";
		ps.Add("FK_FormTree", this.getNo(), false);
		//sql = "SELECT COUNT(*) FROM Sys_MapData WHERE FK_FormTree='" + this.No + "'";
		if (DBAccess.RunSQLReturnValInt(ps) != 0)
		{
			return "err@该目录下有表单，您不能删除。";
		}

		formTree.Delete();
		return "删除成功";
	}
	/** 
	 让admin登录
	 
	 @return 
	*/
	public final String LetAdminLoginByToken() throws Exception {
		try
		{
			String userNo = this.GetRequestVal("UserNo");
			String sid = this.GetRequestVal("Token");

			Dev2Interface.Port_LoginByToken(sid);

			return "info@登录成功";
		}
		catch (RuntimeException ex)
		{
			return "err@登录失败:" + ex.getMessage();
		}
	}
	/** 
	 让admin 登陆
	 
	 param lang 当前的语言
	 @return 成功则为空，有异常时返回异常信息
	*/
	public final String LetAdminLogin(String empNo, boolean islogin)throws Exception
	{
		try
		{
			if (islogin)
			{
				bp.port.Emp emp = new bp.port.Emp(empNo);
				WebUser.SignInOfGener(emp, "CH", false, false, null, null);
			}
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
		return "@登录成功.";
	}

		///#endregion
	/**
	 NodeStationGroup_init
	 @return
	 */
	public final String AdminerChange() throws Exception {

		String mysql = "SELECT ";
		mysql += "No as \"No\", ";
		mysql += "Name as \"Name\", ";
		mysql += "UseSta as \"UseSta\", ";
		mysql += "RootOfDept as \"RootOfDept\" ";
		mysql += " FROM  WF_Emp WHERE No LIKE '" + this.GetRequestVal("UserNo") + "@%' ";
		DataTable dt = DBAccess.RunSQLReturnTable(mysql);
		return bp.tools.Json.ToJson(dt);
	}
}