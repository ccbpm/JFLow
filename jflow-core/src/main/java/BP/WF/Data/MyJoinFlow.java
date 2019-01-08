package BP.WF.Data;

import BP.DA.DBAccess;
import BP.DA.DataTable;
import BP.DA.Paras;
import BP.En.AttrOfSearch;
import BP.En.EnType;
import BP.En.Entity;
import BP.En.Map;
import BP.En.QueryObject;
import BP.En.RefMethod;
import BP.En.RefMethodType;
import BP.En.UAC;
import BP.WF.ActionType;
import BP.WF.Flows;
import BP.WF.Node;
import BP.WF.TaskSta;
import BP.WF.WFSta;
import BP.WF.WFState;

public class MyJoinFlow extends Entity
{

	///#region 基本属性
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.Readonly();
		return uac;
	}
	/** 
	 主键
	 
	*/
	@Override
	public String getPK()
	{
		return MyJoinFlowAttr.WorkID;
	}
	/** 
	 备注
	 
	*/
	public final String getFlowNote()
	{
		return this.GetValStrByKey(MyJoinFlowAttr.FlowNote);
	}
	public final void setFlowNote(String value)
	{
		SetValByKey(MyJoinFlowAttr.FlowNote, value);
	}
	/** 
	 工作流程编号
	 
	*/
	public final String getFK_Flow()
	{
		return this.GetValStrByKey(MyJoinFlowAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)
	{
		SetValByKey(MyJoinFlowAttr.FK_Flow,value);
	}
	/** 
	 BillNo
	 
	*/
	public final String getBillNo()
	{
		return this.GetValStrByKey(MyJoinFlowAttr.BillNo);
	}
	public final void setBillNo(String value)
	{
		SetValByKey(MyJoinFlowAttr.BillNo, value);
	}
	/** 
	 流程名称
	 
	*/
	public final String getFlowName()
	{
		return this.GetValStrByKey(MyJoinFlowAttr.FlowName);
	}
	public final void setFlowName(String value)
	{
		SetValByKey(MyJoinFlowAttr.FlowName, value);
	}
	/** 
	 优先级
	 
	*/
	public final int getPRI()
	{
		return this.GetValIntByKey(MyJoinFlowAttr.PRI);
	}
	public final void setPRI(int value)
	{
		SetValByKey(MyJoinFlowAttr.PRI, value);
	}
	/** 
	 待办人员数量
	 
	*/
	public final int getTodoEmpsNum()
	{
		return this.GetValIntByKey(MyJoinFlowAttr.TodoEmpsNum);
	}
	public final void setTodoEmpsNum(int value)
	{
		SetValByKey(MyJoinFlowAttr.TodoEmpsNum, value);
	}
	/** 
	 待办人员列表
	 
	*/
	public final String getTodoEmps()
	{
		return this.GetValStrByKey(MyJoinFlowAttr.TodoEmps);
	}
	public final void setTodoEmps(String value)
	{
		SetValByKey(MyJoinFlowAttr.TodoEmps, value);
	}
	/** 
	 参与人
	 
	*/
	public final String getEmps()
	{
		return this.GetValStrByKey(MyJoinFlowAttr.Emps);
	}
	public final void setEmps(String value)
	{
		SetValByKey(MyJoinFlowAttr.Emps, value);
	}
	/** 
	 状态
	 
	*/
	public final TaskSta getTaskSta()
	{
		return TaskSta.forValue(this.GetValIntByKey(MyJoinFlowAttr.TaskSta));
	}
	public final void setTaskSta(TaskSta value)
	{
		SetValByKey(MyJoinFlowAttr.TaskSta,value);
	}
	/** 
	 类别编号
	 
	*/
	public final String getFK_FlowSort()
	{
		return this.GetValStrByKey(MyJoinFlowAttr.FK_FlowSort);
	}
	public final void setFK_FlowSort(String value)
	{
		SetValByKey(MyJoinFlowAttr.FK_FlowSort, value);
	}
	/** 
	 部门编号
	 
	*/
	public final String getFK_Dept()
	{
		return this.GetValStrByKey(MyJoinFlowAttr.FK_Dept);
	}
	public final void setFK_Dept(String value)
	{
		SetValByKey(MyJoinFlowAttr.FK_Dept,value);
	}
	/** 
	 标题
	 
	*/
	public final String getTitle()
	{
		return this.GetValStrByKey(MyJoinFlowAttr.Title);
	}
	public final void setTitle(String value)
	{
		SetValByKey(MyJoinFlowAttr.Title,value);
	}
	/** 
	 客户编号
	 
	*/
	public final String getGuestNo()
	{
		return this.GetValStrByKey(MyJoinFlowAttr.GuestNo);
	}
	public final void setGuestNo(String value)
	{
		SetValByKey(MyJoinFlowAttr.GuestNo, value);
	}
	/** 
	 客户名称
	 
	*/
	public final String getGuestName()
	{
		return this.GetValStrByKey(MyJoinFlowAttr.GuestName);
	}
	public final void setGuestName(String value)
	{
		SetValByKey(MyJoinFlowAttr.GuestName, value);
	}
	/** 
	 产生时间
	 
	*/
	public final String getRDT()
	{
		return this.GetValStrByKey(MyJoinFlowAttr.RDT);
	}
	public final void setRDT(String value)
	{
		SetValByKey(MyJoinFlowAttr.RDT,value);
	}
	/** 
	 节点应完成时间
	 
	*/
	public final String getSDTOfNode()
	{
		return this.GetValStrByKey(MyJoinFlowAttr.SDTOfNode);
	}
	public final void setSDTOfNode(String value)
	{
		SetValByKey(MyJoinFlowAttr.SDTOfNode, value);
	}
	/** 
	 流程应完成时间
	 
	*/
	public final String getSDTOfFlow()
	{
		return this.GetValStrByKey(MyJoinFlowAttr.SDTOfFlow);
	}
	public final void setSDTOfFlow(String value)
	{
		SetValByKey(MyJoinFlowAttr.SDTOfFlow, value);
	}
	/** 
	 流程ID
	 
	*/
	public final long getWorkID()
	{
		return this.GetValInt64ByKey(MyJoinFlowAttr.WorkID);
	}
	public final void setWorkID(long value)
	{
		SetValByKey(MyJoinFlowAttr.WorkID,value);
	}
	/** 
	 主线程ID
	 
	*/
	public final long getFID()
	{
		return this.GetValInt64ByKey(MyJoinFlowAttr.FID);
	}
	public final void setFID(long value)
	{
		SetValByKey(MyJoinFlowAttr.FID, value);
	}
	/** 
	 父节点流程编号.
	 
	*/
	public final long getPWorkID()
	{
		return this.GetValInt64ByKey(MyJoinFlowAttr.PWorkID);
	}
	public final void setPWorkID(long value)
	{
		SetValByKey(MyJoinFlowAttr.PWorkID, value);
	}
	/** 
	 父流程调用的节点
	 
	*/
	public final int getPNodeID()
	{
		return this.GetValIntByKey(MyJoinFlowAttr.PNodeID);
	}
	public final void setPNodeID(int value)
	{
		SetValByKey(MyJoinFlowAttr.PNodeID, value);
	}
	/** 
	 PFlowNo
	 
	*/
	public final String getPFlowNo()
	{
		return this.GetValStrByKey(MyJoinFlowAttr.PFlowNo);
	}
	public final void setPFlowNo(String value)
	{
		SetValByKey(MyJoinFlowAttr.PFlowNo, value);
	}
	/** 
	 吊起子流程的人员
	 
	*/
	public final String getPEmp()
	{
		return this.GetValStrByKey(MyJoinFlowAttr.PEmp);
	}
	public final void setPEmp(String value)
	{
		SetValByKey(MyJoinFlowAttr.PEmp, value);
	}
	/** 
	 发起人
	 
	*/
	public final String getStarter()
	{
		return this.GetValStrByKey(MyJoinFlowAttr.Starter);
	}
	public final void setStarter(String value)
	{
		SetValByKey(MyJoinFlowAttr.Starter, value);
	}
	/** 
	 发起人名称
	 
	*/
	public final String getStarterName()
	{
		return this.GetValStrByKey(MyJoinFlowAttr.StarterName);
	}
	public final void setStarterName(String value)
	{
		this.SetValByKey(MyJoinFlowAttr.StarterName, value);
	}
	/** 
	 发起人部门名称
	 
	*/
	public final String getDeptName()
	{
		return this.GetValStrByKey(MyJoinFlowAttr.DeptName);
	}
	public final void setDeptName(String value)
	{
		this.SetValByKey(MyJoinFlowAttr.DeptName, value);
	}
	/** 
	 当前节点名称
	 
	*/
	public final String getNodeName()
	{
		return this.GetValStrByKey(MyJoinFlowAttr.NodeName);
	}
	public final void setNodeName(String value)
	{
		this.SetValByKey(MyJoinFlowAttr.NodeName, value);
	}
	/** 
	 当前工作到的节点
	 
	*/
	public final int getFK_Node()
	{
		return this.GetValIntByKey(MyJoinFlowAttr.FK_Node);
	}
	public final void setFK_Node(int value)
	{
		SetValByKey(MyJoinFlowAttr.FK_Node, value);
	}
	/** 
	 工作流程状态
	 
	*/
	public final WFState getWFState()
	{
		return WFState.forValue(this.GetValIntByKey(MyJoinFlowAttr.WFState));
	}
	public final void setWFState(WFState value)
	{
		if (value == BP.WF.WFState.Complete)
		{
			SetValByKey(MyJoinFlowAttr.WFSta, WFSta.Complete);
		}
		else if (value == WFState.Delete)
		{
			SetValByKey(MyJoinFlowAttr.WFSta, WFSta.Etc);
		}
		else
		{
			SetValByKey(MyJoinFlowAttr.WFSta, WFSta.Runing);
		}

		SetValByKey(MyJoinFlowAttr.WFState, (int)value.getValue()
				);
	}
	/** 
	 状态(简单)
	 
	*/
	public final WFSta getWFSta()
	{
		return WFSta.forValue(this.GetValIntByKey(MyJoinFlowAttr.WFSta));
	}
	public final void setWFSta(WFSta value)
	{
		SetValByKey(MyJoinFlowAttr.WFSta, (int)value.getValue());
	}
	public final String getWFStateText()
	{
		BP.WF.WFState ws = (WFState)this.getWFState();
		switch(ws)
		{
			case Complete:
				return "已完成";
			case Runing:
				return "在运行";
			case HungUp:
				return "挂起";
			case Askfor:
				return "加签";
			default:
				return "未判断";
		}
	}
	/** 
	 GUID
	 
	*/
	public final String getGUID()
	{
		return this.GetValStrByKey(MyJoinFlowAttr.GUID);
	}
	public final void setGUID(String value)
	{
		SetValByKey(MyJoinFlowAttr.GUID, value);
	}

	///#endregion


	///#region 参数属性.

	public final String getParas_ToNodes()
	{
		return this.GetParaString("ToNodes");
	}

	public final void setParas_ToNodes(String value)
	{
		this.SetPara("ToNodes", value);
	}
	/** 
	 加签信息
	 
	*/

	public final String getParas_AskForReply()
	{
		return this.GetParaString("AskForReply");
	}

	public final void setParas_AskForReply(String value)
	{
		this.SetPara("AskForReply", value);
	}

	///#endregion 参数属性.


	///#region 构造函数
	/** 
	 产生的工作流程
	 
	*/
	public MyJoinFlow()
	{
	}
	public MyJoinFlow(long workId) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(MyJoinFlowAttr.WorkID, workId);
		if (qo.DoQuery() == 0)
		{
			throw new RuntimeException("工作 MyFlow [" + workId + "]不存在。");
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

		Map map = new Map("WF_GenerWorkFlow", "我参与的流程");

		map.Java_SetEnType(EnType.View);

		map.AddTBIntPK(MyJoinFlowAttr.WorkID, 0, "WorkID", false, false);
		map.AddTBInt(MyJoinFlowAttr.FID, 0, "FID", false, false);
		map.AddTBInt(MyJoinFlowAttr.PWorkID, 0, "PWorkID", false, false);
		map.AddTBString(MyJoinFlowAttr.Title, null, "流程标题", true, false, 0, 100, 150, true);
		map.AddDDLEntities(MyJoinFlowAttr.FK_Flow, null, "流程名称", new Flows(), false);
		map.AddTBString(MyJoinFlowAttr.BillNo, null, "单据编号", true, false, 0, 100, 50);
		map.AddTBString(MyJoinFlowAttr.StarterName, null, "发起人", true, false, 0, 30, 40);

			//map.AddDDLEntities(MyJoinFlowAttr.FK_Dept, null, "发起人部门", new BP.Port.Depts(), false);
			//map.AddTBString(MyJoinFlowAttr.Starter, null, "发起人编号", true, false, 0, 30, 10);
			//map.AddTBString(MyJoinFlowAttr.StarterName, null, "发起人名称", true, false, 0, 30, 10);
			//map.AddTBString(MyJoinFlowAttr.BillNo, null, "单据编号", true, false, 0, 100, 10);

		map.AddTBDateTime(MyJoinFlowAttr.RDT, "发起日期", true, true);
		map.AddDDLSysEnum(MyJoinFlowAttr.WFSta, 0, "状态", true, false, MyJoinFlowAttr.WFSta, "@0=运行中@1=已完成@2=其他");
		map.AddDDLSysEnum(MyJoinFlowAttr.TSpan, 0, "时间段", true, false, MyJoinFlowAttr.TSpan, "@0=本周@1=上周@2=两周以前@3=三周以前@4=更早");
		map.AddTBString(MyJoinFlowAttr.NodeName, null, "当前节点", true, false, 0, 100, 100, true);
		map.AddTBString(MyStartFlowAttr.TodoEmps, null, "当前处理人", true, false, 0, 100, 100, true);

		map.AddTBString(MyJoinFlowAttr.Emps, null, "参与人", false, false, 0, 4000, 10, true);
		map.AddTBStringDoc(MyJoinFlowAttr.FlowNote, null, "备注", true, false, true);

			//隐藏字段.
		map.AddTBInt(MyJoinFlowAttr.FK_Node, 0, "FK_Node", false, false);

		map.AddTBMyNum();

		map.AddSearchAttr(MyJoinFlowAttr.FK_Flow);
		map.AddSearchAttr(MyJoinFlowAttr.WFSta);
		map.AddSearchAttr(MyJoinFlowAttr.TSpan);
		map.AddHidden(MyStartFlowAttr.FID, "=", "0");

			//增加隐藏的查询条件.
		AttrOfSearch search = new AttrOfSearch(MyJoinFlowAttr.Emps, "人员", MyJoinFlowAttr.Emps, " LIKE ", "%@WebUser.No%", 0, true);
		map.getAttrsOfSearch().Add(search);

		RefMethod rm = new RefMethod();
		rm.Title = "流程轨迹";
		rm.ClassMethodName = this.toString() + ".DoTrack";
		rm.Icon = "../../WF/Img/FileType/doc.gif";
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "打开表单";
		rm.ClassMethodName = this.toString() + ".DoOpenLastForm";
		rm.Icon = "../../WF/Img/FileType/doc.gif";
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}

	///#region 执行诊断
	public final String DoTrack()
	{
		//PubClass.WinOpen(Glo.CCFlowAppPath + "WF/WFRpt.htm?WorkID=" + this.WorkID + "&FID=" + this.FID + "&FK_Flow=" + this.FK_Flow, 900, 800);
		return "../WFRpt.htm?WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow()+"&FK_Node="+this.getFK_Node();
	}
	/** 
	 打开最后一个节点表单
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoOpenLastForm() throws Exception
	{
		Paras pss = new Paras();
		pss.SQL = "SELECT MYPK FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track WHERE ActionType=" + BP.Sys.SystemConfig.getAppCenterDBVarStr() + "ActionType AND WorkID=" + BP.Sys.SystemConfig.getAppCenterDBVarStr() + "WorkID ORDER BY RDT DESC";
		pss.Add("ActionType", ActionType.Forward.getValue());
		pss.Add("WorkID", this.getWorkID());
		DataTable dt = DBAccess.RunSQLReturnTable(pss);
		if (dt != null && dt.Rows.size() > 0)
		{
			String myPk = dt.Rows.get(0).getValue(0).toString();
			return "../WFRpt.htm?WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getFK_Node() + "&DoType=View&MyPK=" + myPk + "&PWorkID=" + this.getPWorkID();
		}

		BP.WF.Node nd = new Node(this.getFK_Node());
		nd.WorkID = this.getWorkID(); //为求当前表单ID获得参数，而赋值.

		return "../CCForm/FrmFreeReadonly.htm?WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&FK_MapData=" + nd.getNodeFrmID() + "&ReadOnly=1&IsEdit=0";
	}

	///#endregion
}