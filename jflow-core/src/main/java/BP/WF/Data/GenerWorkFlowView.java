package BP.WF.Data;

import BP.DA.*;
import BP.WF.*;
import BP.Port.*;
import BP.Sys.*;
import BP.En.*;
import BP.En.Map;
import BP.WF.Template.*;
import BP.WF.*;
import java.util.*;

/** 
 流程实例
*/
public class GenerWorkFlowView extends Entity
{

		///#region 基本属性
	/** 
	 主键
	*/
	@Override
	public String getPK()
	{
		return GenerWorkFlowViewAttr.WorkID;
	}
	/** 
	 备注
	*/
	public final String getFlowNote() throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.FlowNote);
	}
	public final void setFlowNote(String value) throws Exception
	{
		SetValByKey(GenerWorkFlowViewAttr.FlowNote, value);
	}
	/** 
	 工作流程编号
	*/
	public final String getFK_Flow() throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.FK_Flow);
	}
	public final void setFK_Flow(String value) throws Exception
	{
		SetValByKey(GenerWorkFlowViewAttr.FK_Flow, value);
	}
	/** 
	 BillNo
	*/
	public final String getBillNo() throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.BillNo);
	}
	public final void setBillNo(String value) throws Exception
	{
		SetValByKey(GenerWorkFlowViewAttr.BillNo, value);
	}
	/** 
	 流程名称
	*/
	public final String getFlowName() throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.FlowName);
	}
	public final void setFlowName(String value) throws Exception
	{
		SetValByKey(GenerWorkFlowViewAttr.FlowName, value);
	}
	/** 
	 优先级
	*/
	public final int getPRI() throws Exception
	{
		return this.GetValIntByKey(GenerWorkFlowViewAttr.PRI);
	}
	public final void setPRI(int value) throws Exception
	{
		SetValByKey(GenerWorkFlowViewAttr.PRI, value);
	}
	/** 
	 待办人员数量
	*/
	public final int getTodoEmpsNum() throws Exception
	{
		return this.GetValIntByKey(GenerWorkFlowViewAttr.TodoEmpsNum);
	}
	public final void setTodoEmpsNum(int value) throws Exception
	{
		SetValByKey(GenerWorkFlowViewAttr.TodoEmpsNum, value);
	}
	/** 
	 待办人员列表
	*/
	public final String getTodoEmps() throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.TodoEmps);
	}
	public final void setTodoEmps(String value) throws Exception
	{
		SetValByKey(GenerWorkFlowViewAttr.TodoEmps, value);
	}
	/** 
	 参与人
	*/
	public final String getEmps() throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.Emps);
	}
	public final void setEmps(String value) throws Exception
	{
		SetValByKey(GenerWorkFlowViewAttr.Emps, value);
	}
	/** 
	 状态
	*/
	public final TaskSta getTaskSta() throws Exception
	{
		return TaskSta.forValue(this.GetValIntByKey(GenerWorkFlowViewAttr.TaskSta));
	}
	public final void setTaskSta(TaskSta value) throws Exception
	{
		SetValByKey(GenerWorkFlowViewAttr.TaskSta, value.getValue());
	}
	/** 
	 类别编号
	*/
	public final String getFK_FlowSort() throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.FK_FlowSort);
	}
	public final void setFK_FlowSort(String value) throws Exception
	{
		SetValByKey(GenerWorkFlowViewAttr.FK_FlowSort, value);
	}
	/** 
	 部门编号
	*/
	public final String getFK_Dept() throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.FK_Dept);
	}
	public final void setFK_Dept(String value) throws Exception
	{
		SetValByKey(GenerWorkFlowViewAttr.FK_Dept, value);
	}
	/** 
	 标题
	*/
	public final String getTitle() throws Exception 
	{ 
		return this.GetValStrByKey(GenerWorkFlowViewAttr.Title);
	}
	public final void setTitle(String value) throws Exception
	{
		SetValByKey(GenerWorkFlowViewAttr.Title, value);
	}
	/** 
	 客户编号
	*/
	public final String getGuestNo() throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.GuestNo);
	}
	public final void setGuestNo(String value) throws Exception
	{
		SetValByKey(GenerWorkFlowViewAttr.GuestNo, value);
	}
	/** 
	 客户名称
	*/
	public final String getGuestName() throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.GuestName);
	}
	public final void setGuestName(String value) throws Exception
	{
		SetValByKey(GenerWorkFlowViewAttr.GuestName, value);
	}
	/** 
	 产生时间
	*/
	public final String getRDT() throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.RDT);
	}
	public final void setRDT(String value) throws Exception
	{
		SetValByKey(GenerWorkFlowViewAttr.RDT, value);
	}
	/** 
	 节点应完成时间
	*/
	public final String getSDTOfNode() throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.SDTOfNode);
	}
	public final void setSDTOfNode(String value) throws Exception
	{
		SetValByKey(GenerWorkFlowViewAttr.SDTOfNode, value);
	}
	/** 
	 流程应完成时间
	*/
	public final String getSDTOfFlow() throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.SDTOfFlow);
	}
	public final void setSDTOfFlow(String value) throws Exception
	{
		SetValByKey(GenerWorkFlowViewAttr.SDTOfFlow, value);
	}
	/** 
	 流程ID
	*/
	public final long getWorkID() throws Exception
	{
		return this.GetValInt64ByKey(GenerWorkFlowViewAttr.WorkID);
	}
	public final void setWorkID(long value) throws Exception
	{
		SetValByKey(GenerWorkFlowViewAttr.WorkID, value);
	}
	/** 
	 主线程ID
	*/
	public final long getFID() throws Exception
	{
		return this.GetValInt64ByKey(GenerWorkFlowViewAttr.FID);
	}
	public final void setFID(long value) throws Exception
	{ 
		SetValByKey(GenerWorkFlowViewAttr.FID, value);
	}
	/** 
	 父节点流程编号.
	*/
	public final long getPWorkID() throws Exception
	{
		return this.GetValInt64ByKey(GenerWorkFlowViewAttr.PWorkID);
	}
	public final void setPWorkID(long value) throws Exception
	{
		SetValByKey(GenerWorkFlowViewAttr.PWorkID, value);
	}
	/** 
	 父流程调用的节点
	*/
	public final int getPNodeID() throws Exception
	{
		return this.GetValIntByKey(GenerWorkFlowViewAttr.PNodeID);
	}
	public final void setPNodeID(int value) throws Exception
	{
		SetValByKey(GenerWorkFlowViewAttr.PNodeID, value);
	}
	/** 
	 PFlowNo
	*/
	public final String getPFlowNo() throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.PFlowNo);
	}
	public final void setPFlowNo(String value) throws Exception
	{
		SetValByKey(GenerWorkFlowViewAttr.PFlowNo, value);
	}
	/** 
	 吊起子流程的人员
	*/
	public final String getPEmp() throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.PEmp);
	}
	public final void setPEmp(String value) throws Exception
	{
		SetValByKey(GenerWorkFlowViewAttr.PEmp, value);
	}
	/** 
	 发起人
	*/
	public final String getStarter() throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.Starter);
	}
	public final void setStarter(String value) throws Exception
	{
		SetValByKey(GenerWorkFlowViewAttr.Starter, value);
	}
	/** 
	 发起人名称
	*/
	public final String getStarterName() throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.StarterName);
	}
	public final void setStarterName(String value) throws Exception
	{
		this.SetValByKey(GenerWorkFlowViewAttr.StarterName, value);
	}
	/** 
	 发起人部门名称
	*/
	public final String getDeptName() throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.DeptName);
	}
	public final void setDeptName(String value) throws Exception
	{
		this.SetValByKey(GenerWorkFlowViewAttr.DeptName, value);
	}
	/** 
	 当前节点名称
	*/
	public final String getNodeName() throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.NodeName);
	}
	public final void setNodeName(String value) throws Exception
	{
		this.SetValByKey(GenerWorkFlowViewAttr.NodeName, value);
	}
	/** 
	 当前工作到的节点
	*/
	public final int getFK_Node() throws Exception
	{
		return this.GetValIntByKey(GenerWorkFlowViewAttr.FK_Node);
	}
	public final void setFK_Node(int value) throws Exception
	{
		SetValByKey(GenerWorkFlowViewAttr.FK_Node, value);
	}
	/** 
	 工作流程状态
	*/
	public final WFState getWFState() throws Exception
	{
		return WFState.forValue(this.GetValIntByKey(GenerWorkFlowViewAttr.WFState));
	}
	public final void setWFState(WFState value) throws Exception
	{
		if (value == WFState.Complete)
		{
			SetValByKey(GenerWorkFlowViewAttr.WFSta, getWFSta().Complete.getValue());
		}
		else if (value == WFState.Delete)
		{
			SetValByKey(GenerWorkFlowViewAttr.WFSta, getWFSta().Etc.getValue());
		}
		else
		{
			SetValByKey(GenerWorkFlowViewAttr.WFSta, getWFSta().Runing.getValue());
		}

		SetValByKey(GenerWorkFlowViewAttr.WFState, value.getValue());
	}
	/** 
	 状态(简单)
	*/
	public final WFSta getWFSta() throws Exception
	{
		return WFSta.forValue(this.GetValIntByKey(GenerWorkFlowViewAttr.WFSta));
	}
	public final void setWFSta(WFSta value) throws Exception
	{
		SetValByKey(GenerWorkFlowViewAttr.WFSta, value.getValue());
	}
	public final String getWFStateText() throws Exception
	{
		BP.WF.WFState ws = (WFState) this.getWFState();
		switch (ws)
		{
			case Complete:
				return "已完成";
			case Runing:
				return "在运行";
			case HungUp:
				return "挂起";
			case Askfor:
				return "加签";
			case ReturnSta:
				return "退回";
			case Draft:
				return "草稿";
			default:
				return "未判断";
		}
	}
	/** 
	 GUID
	*/
	public final String getGUID() throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.GUID);
	}
	public final void setGUID(String value) throws Exception
	{
		SetValByKey(GenerWorkFlowViewAttr.GUID, value);
	}

		///#endregion


		///#region 参数属性.

	public final String getParas_ToNodes() throws Exception
	{
		return this.GetParaString("ToNodes");
	}

	public final void setParas_ToNodes(String value) throws Exception
	{
		this.SetPara("ToNodes", value);
	}
	/** 
	 加签信息
	*/

	public final String getParas_AskForReply() throws Exception
	{
		return this.GetParaString("AskForReply");
	}

	public final void setParas_AskForReply(String value) throws Exception
	{
		this.SetPara("AskForReply", value);
	}

		///#endregion 参数属性.


		///#region 构造函数
	/** 
	 访问权限
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.Readonly();
		return uac;
	}
	/** 
	 产生的工作流程
	*/
	public GenerWorkFlowView()
	{
	}
	/** 
	 产生的工作流程
	 
	 @param workId
	 * @throws Exception 
	*/
	public GenerWorkFlowView(long workId) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(GenerWorkFlowViewAttr.WorkID, workId);
		if (qo.DoQuery() == 0)
		{
			throw new RuntimeException("工作 GenerWorkFlowView [" + workId + "]不存在。");
		}
	}
	/** 
	 执行修复
	*/
	public final void DoRepair()
	{
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_GenerWorkFlow", "流程查询");

		map.AddTBIntPK(GenerWorkFlowViewAttr.WorkID, 0, "WorkID", true, true);
		map.AddTBString(GenerWorkFlowViewAttr.StarterName, null, "发起人", true, false, 0, 30, 10);
		map.AddTBString(GenerWorkFlowViewAttr.Title, null, "标题", true, false, 0, 100, 10, true);
		map.AddDDLSysEnum(GenerWorkFlowViewAttr.WFSta, 0, "流程状态", true, false, GenerWorkFlowViewAttr.WFSta, "@0=运行中@1=已完成@2=其他");

		map.AddDDLSysEnum(GenerWorkFlowViewAttr.WFState, 0, "流程状态", true, false, MyStartFlowAttr.WFState);
		map.AddTBString(GenerWorkFlowViewAttr.NodeName, null, "当前节点名称", true, false, 0, 100, 10);
		map.AddTBDateTime(GenerWorkFlowViewAttr.RDT, "记录日期", true, true);
		map.AddTBString(GenerWorkFlowViewAttr.BillNo, null, "单据编号", true, false, 0, 100, 10);
		map.AddTBStringDoc(GenerWorkFlowViewAttr.FlowNote, null, "备注", true, false, true);

		map.AddDDLEntities(GenerWorkFlowViewAttr.FK_FlowSort, null, "类别", new FlowSorts(), false);
		map.AddDDLEntities(GenerWorkFlowViewAttr.FK_Flow, null, "流程", new Flows(), false);
		map.AddDDLEntities(GenerWorkFlowViewAttr.FK_Dept, null, "部门", new BP.Port.Depts(), false);

		map.AddTBInt(GenerWorkFlowViewAttr.FID, 0, "FID", false, false);
		map.AddTBInt(GenerWorkFlowViewAttr.FK_Node, 0, "FK_Node", false, false);

		map.AddDDLEntities(GenerWorkFlowViewAttr.FK_NY, null, "月份", new GenerWorkFlowViewNYs(), false);

		map.AddTBMyNum();

			//map.AddSearchAttr(GenerWorkFlowViewAttr.FK_Dept);
		map.AddSearchAttr(GenerWorkFlowViewAttr.FK_Flow);
		map.AddSearchAttr(GenerWorkFlowViewAttr.WFSta);
		map.AddSearchAttr(GenerWorkFlowViewAttr.FK_NY);

			//把不等于 0 的去掉.
		map.AddHidden(GenerWorkFlowViewAttr.WFState, "!=", "0");


		RefMethod rm = new RefMethod();
		rm.Title = "轨迹";
		rm.ClassMethodName = this.toString() + ".DoTrack";
		rm.Icon = "../../WF/Img/Track.png";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "删除";
		rm.ClassMethodName = this.toString() + ".DoDelete";
		rm.Warning = "您确定要删除吗？";
		rm.Icon = "../../WF/Img/Btn/Delete.gif";
		rm.IsForEns = false;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Icon = "../../WF/Img/Btn/CC.gif";
		rm.Title = "移交";
		rm.ClassMethodName = this.toString() + ".DoFlowShift";
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Icon = "../../WF/Img/Btn/Back.png";
		rm.Title = "回滚";
		rm.ClassMethodName = this.toString() + ".Rollback";
			//rm.getHisAttrs().AddTBInt("NodeID", 0, "回滚到节点", true, false);
		   // rm.getHisAttrs().AddTBInt("NodeID", 0, "回滚到节点", true, false);
		rm.getHisAttrs().AddTBString("NodeID", null, "NodeID", true, false, 0, 100, 100);
		rm.getHisAttrs().AddTBString("EmpNo", null, "回滚到人员编号",true,false,0,100,100);
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Icon = "../../WF/Img/Btn/CC.gif";
		rm.Title = "跳转";
		rm.IsForEns = false;
		rm.ClassMethodName = this.toString() + ".DoFlowSkip";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Icon = "../../WF/Img/Btn/CC.gif";
		rm.Title = "修复该流程数据实例";
		rm.IsForEns = false;
		rm.ClassMethodName = this.toString() + ".RepairDataIt";
		rm.refMethodType = RefMethodType.Func;
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "调整";
		rm.getHisAttrs().AddTBString("wenben", null, "调整到人员", true, false, 0, 100, 100);
		rm.getHisAttrs().AddTBInt("shuzi", 0, "调整到节点", true, false);

		rm.ClassMethodName = this.toString() + ".DoTest";
		map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion


		///#region 执行功能.
	//,string isOK, int wfstate, string fk_emp
	public final String DoTest(String toEmpNo, int toNodeID) throws Exception
	{
	   return BP.WF.Dev2Interface.Flow_ReSend(this.getWorkID(), toNodeID, toEmpNo, "admin调整");
	}
	public final String RepairDataIt() throws Exception
	{
		String infos = "";

		Flow fl = new Flow(this.getFK_Flow());
		Node nd = new Node(Integer.parseInt(fl.getNo() + "01"));
		Work wk = nd.getHisWork();

		String trackTable = "ND" + Integer.parseInt(fl.getNo()) + "Track";
		String sql = "SELECT MyPK FROM " + trackTable + " WHERE WorkID=" + this.getWorkID() + " AND ACTIONTYPE=1 and NDFrom=" + nd.getNodeID();
		String mypk = DBAccess.RunSQLReturnString(sql);
		if (DataType.IsNullOrEmpty(mypk) == true)
		{
			return "err@没有找到track主键。";
		}

		wk.setOID(this.getWorkID());
		wk.RetrieveFromDBSources();

		String file = "c:\\temp\\" + this.getWorkID() + ".txt";
		try
		{
			BP.DA.DBAccess.GetFileFromDB(file, trackTable, "MyPK", mypk, "FrmDB");
		}
		catch (RuntimeException ex)
		{
			infos += "@ 错误:" + fl.getNo() + " - Rec" + wk.getRec() + " db=" + wk.getOID() + " - " + fl.getName();
		}

		String json = DataType.ReadTextFile(file);
		DataTable dtVal = BP.Tools.Json.ToDataTable(json);

		DataRow mydr = dtVal.Rows.get(0);

		Attrs attrs = wk.getEnMap().getAttrs();
		boolean isHave = false;
		for (Attr attr : attrs)
		{
			String jsonVal = mydr.getValue(attr.getKey()).toString();
			String enVal = wk.GetValStringByKey(attr.getKey());
			if (DataType.IsNullOrEmpty(enVal) == true)
			{
				wk.SetValByKey(attr.getKey(), jsonVal);
				isHave = true;
			}
		}

		if (isHave == true)
		{
			wk.DirectUpdate();
			return "不需要更新数据.";
		}
		infos += "@WorkID=" + wk.getOID() + " =" + wk.getRec() + "  dt=" + wk.getRDT() + "被修复.";

		return infos;
	}
	public final String RepairDataAll() throws Exception
	{
		String infos = "";

		Flows fls = new Flows();
		fls.RetrieveAll();

		for (Flow fl : fls.ToJavaList())
		{


			String sql = "SELECT OID FROM " + fl.getPTable() + " WHERE BillNo IS NULL AND OID=" + this.getWorkID();
			DataTable dt = DBAccess.RunSQLReturnTable(sql);

			Node nd = new Node(Integer.parseInt(fl.getNo() + "01"));
			Work wk = nd.getHisWork();

			String trackTable = "ND" + Integer.parseInt(fl.getNo()) + "Track";
			for (DataRow dr : dt.Rows)
			{
				long workid = Long.parseLong(dr.getValue("OID").toString());

				sql = "SELECT MyPK FROM " + trackTable + " WHERE WorkID=" + workid + " AND ACTIONTYPE=1 and NDFrom=" + nd.getNodeID();
				String mypk = DBAccess.RunSQLReturnString(sql);
				if (DataType.IsNullOrEmpty(mypk) == true)
				{
					continue;
				}

				wk.setOID(workid);
				wk.RetrieveFromDBSources();

				String file = "c:\\temp\\" + mypk + ".txt";
				try
				{
					BP.DA.DBAccess.GetFileFromDB(file, trackTable, "MyPK", mypk, "FrmDB");
				}
				catch (RuntimeException ex)
				{
					infos += "@ 错误:" + fl.getNo() + " - Rec" + wk.getRec() + " db=" + wk.getOID() + " - " + fl.getName();
				}

				String json = DataType.ReadTextFile(file);
				DataTable dtVal = BP.Tools.Json.ToDataTable(json);

				DataRow mydr = dtVal.Rows.get(0);

				Attrs attrs = wk.getEnMap().getAttrs();
				boolean isHave = false;
				for (Attr attr : attrs)
				{
					String jsonVal = mydr.getValue(attr.getKey()).toString();
					String enVal = wk.GetValStringByKey(attr.getKey());
					if (DataType.IsNullOrEmpty(enVal) == true)
					{
						wk.SetValByKey(attr.getKey(), jsonVal);
						isHave = true;
					}
				}

				if (isHave == true)
				{
					wk.DirectUpdate();
					continue;
				}
				infos += "@WorkID=" + wk.getOID() + " =" + wk.getRec() + "  dt=" + wk.getRDT() + "被修复.";
			}
		}
		return infos;
	}
	/** 
	 回滚
	 
	 @param nodeid 节点ID
	 @param note 回滚原因
	 @return 回滚的结果
	 * @throws Exception 
	*/
	public final String Rollback(String nodeid, String note) throws Exception
	{
		BP.WF.Template.FlowSheet fl = new FlowSheet(this.getFK_Flow());
		return fl.DoRebackFlowData(this.getWorkID(), Integer.parseInt(nodeid), note);
	}

	public final String DoTrack() throws Exception
	{
		return "../../WF/WFRpt.htm?WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow();
	}
	/** 
	 执行移交
	 
	 @param ToEmp
	 @param Note
	 @return 
	*/
	public final String DoShift(String ToEmp, String Note) throws Exception
	{
		if (BP.WF.Dev2Interface.Flow_IsCanViewTruck(this.getFK_Flow(), this.getWorkID()) == false)
		{
			return "您没有操作该流程数据的权限.";
		}

		try
		{
			BP.WF.Dev2Interface.Node_Shift(this.getWorkID(), ToEmp, Note);
			return "移交成功";
		}
		catch (RuntimeException ex)
		{
			return "移交失败@" + ex.getMessage();
		}
	}
	/** 
	 执行删除
	 
	 @return 
	*/
	public final String DoDelete() throws Exception
	{
		if (BP.WF.Dev2Interface.Flow_IsCanViewTruck(this.getFK_Flow(), this.getWorkID()) == false)
		{
			return "您没有操作该流程数据的权限.";
		}

		try
		{
			BP.WF.Dev2Interface.Flow_DoDeleteFlowByReal(this.getFK_Flow(), this.getWorkID(), true);
			return "删除成功";
		}
		catch (RuntimeException ex)
		{
			return "删除失败@" + ex.getMessage();
		}
	}
	/** 
	 移交
	 
	 @return 
	*/
	public final String DoFlowShift() throws Exception
	{
		return "../../WorkOpt/Forward.htm?WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getFK_Node();
	}
	/** 
	 回滚流程
	 
	 @return 
	*/
	public final String Rollback() throws Exception
	{

		return "../../WorkOpt/Rollback.htm?WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getFK_Node();
	}
	/** 
	 执行跳转
	 
	 @return 
	*/
	public final String DoFlowSkip() throws Exception
	{
		return "../../WorkOpt/FlowSkip.htm?WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getFK_Node();
	}

		///#endregion
}