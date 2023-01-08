package bp.wf.data;

import bp.difference.SystemConfig;
import bp.sys.CCBPMRunModel;
import bp.web.WebUser;
import bp.wf.*;
import bp.en.*;
import bp.en.Map;

/** 
 我部门的待办
*/
public class MyDeptTodolist extends Entity
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
		return MyDeptTodolistAttr.WorkID;
	}
	/** 
	 工作流程编号
	*/
	public final String getFK_Flow()  throws Exception
	{
		return this.GetValStrByKey(MyDeptTodolistAttr.FK_Flow);
	}
	public final void setFK_Flow(String value) throws Exception
	{
		SetValByKey(MyDeptTodolistAttr.FK_Flow, value);
	}
	/** 
	 BillNo
	*/
	public final String getBillNo()  throws Exception
	{
		return this.GetValStrByKey(MyDeptTodolistAttr.BillNo);
	}
	public final void setBillNo(String value) throws Exception
	{
		SetValByKey(MyDeptTodolistAttr.BillNo, value);
	}
	/** 
	 流程名称
	*/
	public final String getFlowName()  throws Exception
	{
		return this.GetValStrByKey(MyDeptTodolistAttr.FlowName);
	}
	public final void setFlowName(String value) throws Exception
	{
		SetValByKey(MyDeptTodolistAttr.FlowName, value);
	}
	/** 
	 优先级
	*/
	public final int getPRI()  throws Exception
	{
		return this.GetValIntByKey(MyDeptTodolistAttr.PRI);
	}
	public final void setPRI(int value) throws Exception
	{
		SetValByKey(MyDeptTodolistAttr.PRI, value);
	}
	/** 
	 待办人员数量
	*/
	public final int getTodoEmpsNum()  throws Exception
	{
		return this.GetValIntByKey(MyDeptTodolistAttr.TodoEmpsNum);
	}
	public final void setTodoEmpsNum(int value) throws Exception
	{
		SetValByKey(MyDeptTodolistAttr.TodoEmpsNum, value);
	}
	/** 
	 待办人员列表
	*/
	public final String getTodoEmps()  throws Exception
	{
		return this.GetValStrByKey(MyDeptTodolistAttr.TodoEmps);
	}
	public final void setTodoEmps(String value) throws Exception
	{
		SetValByKey(MyDeptTodolistAttr.TodoEmps, value);
	}
	/** 
	 参与人
	*/
	public final String getEmps()  throws Exception
	{
		return this.GetValStrByKey(MyDeptTodolistAttr.Emps);
	}
	public final void setEmps(String value) throws Exception
	{
		SetValByKey(MyDeptTodolistAttr.Emps, value);
	}
	/** 
	 状态
	*/
	public final TaskSta getTaskSta()throws Exception
	{
		return TaskSta.forValue(this.GetValIntByKey(MyDeptTodolistAttr.TaskSta));
	}
	public final void setTaskSta(TaskSta value)throws Exception
	{
		SetValByKey(MyDeptTodolistAttr.TaskSta, value.getValue());
	}
	/** 
	 类别编号
	*/
	public final String getFK_Emp()  throws Exception
	{
		return this.GetValStrByKey(MyDeptTodolistAttr.FK_Emp);
	}
	public final void setFK_Emp(String value) throws Exception
	{
		SetValByKey(MyDeptTodolistAttr.FK_Emp, value);
	}
	/** 
	 部门编号
	*/
	public final String getFK_Dept()  throws Exception
	{
		return this.GetValStrByKey(MyDeptTodolistAttr.FK_Dept);
	}
	public final void setFK_Dept(String value) throws Exception
	{
		SetValByKey(MyDeptTodolistAttr.FK_Dept, value);
	}
	/** 
	 标题
	*/
	public final String getTitle()  throws Exception
	{
		return this.GetValStrByKey(MyDeptTodolistAttr.Title);
	}
	public final void setTitle(String value) throws Exception
	{
		SetValByKey(MyDeptTodolistAttr.Title, value);
	}
	/** 
	 客户编号
	*/
	public final String getGuestNo()  throws Exception
	{
		return this.GetValStrByKey(MyDeptTodolistAttr.GuestNo);
	}
	public final void setGuestNo(String value) throws Exception
	{
		SetValByKey(MyDeptTodolistAttr.GuestNo, value);
	}
	/** 
	 客户名称
	*/
	public final String getGuestName()  throws Exception
	{
		return this.GetValStrByKey(MyDeptTodolistAttr.GuestName);
	}
	public final void setGuestName(String value) throws Exception
	{
		SetValByKey(MyDeptTodolistAttr.GuestName, value);
	}
	/** 
	 产生时间
	*/
	public final String getRDT()  throws Exception
	{
		return this.GetValStrByKey(MyDeptTodolistAttr.RDT);
	}
	public final void setRDT(String value) throws Exception
	{
		SetValByKey(MyDeptTodolistAttr.RDT, value);
	}
	/** 
	 节点应完成时间
	*/
	public final String getSDTOfNode()  throws Exception
	{
		return this.GetValStrByKey(MyDeptTodolistAttr.SDTOfNode);
	}
	public final void setSDTOfNode(String value) throws Exception
	{
		SetValByKey(MyDeptTodolistAttr.SDTOfNode, value);
	}
	/** 
	 流程应完成时间
	*/
	public final String getSDTOfFlow()  throws Exception
	{
		return this.GetValStrByKey(MyDeptTodolistAttr.SDTOfFlow);
	}
	public final void setSDTOfFlow(String value) throws Exception
	{
		SetValByKey(MyDeptTodolistAttr.SDTOfFlow, value);
	}
	/** 
	 流程ID
	*/
	public final long getWorkID()  throws Exception
	{
		return this.GetValInt64ByKey(MyDeptTodolistAttr.WorkID);
	}
	public final void setWorkID(long value) throws Exception
	{
		SetValByKey(MyDeptTodolistAttr.WorkID, value);
	}
	/** 
	 主线程ID
	*/
	public final long getFID()  throws Exception
	{
		return this.GetValInt64ByKey(MyDeptTodolistAttr.FID);
	}
	public final void setFID(long value) throws Exception
	{
		SetValByKey(MyDeptTodolistAttr.FID, value);
	}
	/** 
	 父节点流程编号.
	*/
	public final long getPWorkID()  throws Exception
	{
		return this.GetValInt64ByKey(MyDeptTodolistAttr.PWorkID);
	}
	public final void setPWorkID(long value) throws Exception
	{
		SetValByKey(MyDeptTodolistAttr.PWorkID, value);
	}
	/** 
	 父流程调用的节点
	*/
	public final int getPNodeID()  throws Exception
	{
		return this.GetValIntByKey(MyDeptTodolistAttr.PNodeID);
	}
	public final void setPNodeID(int value) throws Exception
	{
		SetValByKey(MyDeptTodolistAttr.PNodeID, value);
	}
	/** 
	 PFlowNo
	*/
	public final String getPFlowNo()  throws Exception
	{
		return this.GetValStrByKey(MyDeptTodolistAttr.PFlowNo);
	}
	public final void setPFlowNo(String value) throws Exception
	{
		SetValByKey(MyDeptTodolistAttr.PFlowNo, value);
	}
	/** 
	 吊起子流程的人员
	*/
	public final String getPEmp()  throws Exception
	{
		return this.GetValStrByKey(MyDeptTodolistAttr.PEmp);
	}
	public final void setPEmp(String value) throws Exception
	{
		SetValByKey(MyDeptTodolistAttr.PEmp, value);
	}
	/** 
	 发起人
	*/
	public final String getStarter()  throws Exception
	{
		return this.GetValStrByKey(MyDeptTodolistAttr.Starter);
	}
	public final void setStarter(String value) throws Exception
	{
		SetValByKey(MyDeptTodolistAttr.Starter, value);
	}
	/** 
	 发起人名称
	*/
	public final String getStarterName()  throws Exception
	{
		return this.GetValStrByKey(MyDeptTodolistAttr.StarterName);
	}
	public final void setStarterName(String value) throws Exception
	{
		this.SetValByKey(MyDeptTodolistAttr.StarterName, value);
	}
	/** 
	 发起人部门名称
	*/
	public final String getDeptName()  throws Exception
	{
		return this.GetValStrByKey(MyDeptTodolistAttr.DeptName);
	}
	public final void setDeptName(String value) throws Exception
	{
		this.SetValByKey(MyDeptTodolistAttr.DeptName, value);
	}
	/** 
	 当前节点名称
	*/
	public final String getNodeName()  throws Exception
	{
		return this.GetValStrByKey(MyDeptTodolistAttr.NodeName);
	}
	public final void setNodeName(String value) throws Exception
	{
		this.SetValByKey(MyDeptTodolistAttr.NodeName, value);
	}
	/** 
	 当前工作到的节点
	*/
	public final int getFK_Node()  throws Exception
	{
		return this.GetValIntByKey(MyDeptTodolistAttr.FK_Node);
	}
	public final void setFK_Node(int value) throws Exception
	{
		SetValByKey(MyDeptTodolistAttr.FK_Node, value);
	}
	/** 
	 工作流程状态
	*/
	public final WFState getWFState()throws Exception
	{
		return WFState.forValue(this.GetValIntByKey(MyDeptTodolistAttr.WFState));
	}
	public final void setWFState(WFState value) throws Exception {
		if (value == WFState.Complete)
		{
			SetValByKey(MyDeptTodolistAttr.WFSta, WFSta.Complete.getValue());
		}
		else if (value == WFState.Delete)
		{
			SetValByKey(MyDeptTodolistAttr.WFSta, WFSta.Etc.getValue());
		}
		else
		{
			SetValByKey(MyDeptTodolistAttr.WFSta, WFSta.Runing.getValue());
		}

		SetValByKey(MyDeptTodolistAttr.WFState, value.getValue());
	}
	/** 
	 状态(简单)
	*/
	public final WFSta getWFSta()throws Exception
	{
		return WFSta.forValue(this.GetValIntByKey(MyDeptTodolistAttr.WFSta));
	}
	public final void setWFSta(WFSta value)throws Exception
	{
		SetValByKey(MyDeptTodolistAttr.WFSta, value.getValue());
	}
	public final String getWFStateText()throws Exception
	{
		WFState ws = this.getWFState();
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
			default:
				return "未判断";
		}
	}
	/** 
	 GUID
	*/
	public final String getGUID()  throws Exception
	{
		return this.GetValStrByKey(MyDeptTodolistAttr.GUID);
	}
	public final void setGUID(String value) throws Exception
	{
		SetValByKey(MyDeptTodolistAttr.GUID, value);
	}

		///#endregion


		///#region 参数属性.

		///#endregion 参数属性.


		///#region 构造函数
	/** 
	 产生的工作流程
	*/
	public MyDeptTodolist()
	{
	}
	public MyDeptTodolist(long workId) throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(MyDeptTodolistAttr.WorkID, workId);
		if (qo.DoQuery() == 0)
		{
			throw new RuntimeException("工作 MyDeptTodolist [" + workId + "]不存在。");
		}
	}
	/** 
	 执行修复
	*/
	public final void DoRepair()throws Exception
	{
	}
	/** 
	 重写基类方法
	*/
	@Override
	public bp.en.Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_EmpWorks", "我部门的待办");
		map.setEnType(EnType.View);

		map.AddTBInt(MyDeptTodolistAttr.FID, 0, "FID", false, false);
		map.AddTBString(MyDeptTodolistAttr.Title, null, "流程标题", true, false, 0, 300, 10, true);
		map.AddDDLEntities(MyDeptTodolistAttr.FK_Flow, null, "流程", new Flows(), false);
		map.AddTBString(MyDeptTodolistAttr.RDT, null, "发起时间", true, false, 0, 100, 10);


		map.AddTBString(MyDeptTodolistAttr.StarterName, null, "发起人名称", true, false, 0, 30, 10);

		map.AddTBString(MyDeptTodolistAttr.NodeName, null, "停留节点", true, false, 0, 100, 10);
			//map.AddTBString(MyDeptTodolistAttr.TodoEmps, null, "当前处理人", true, false, 0, 100, 10);

		map.AddTBStringDoc(MyDeptTodolistAttr.FlowNote, null, "备注", true, false,true);
		  //  MyDeptTodolistAttr.WorkerDept
			////作为隐藏字段.
		map.AddTBString(MyDeptTodolistAttr.OrgNo, null, "组织编号",false, false, 0, 100, 10);

		map.AddDDLEntities(MyDeptTodolistAttr.FK_Emp, null, "当前处理人", new bp.wf.data.MyDeptEmps(), false);
		map.AddTBIntPK(MyDeptTodolistAttr.WorkID, 0, "工作ID", true, true);

			//查询条件.
		map.AddSearchAttr(MyDeptTodolistAttr.FK_Flow, 130);
		map.AddSearchAttr(MyDeptTodolistAttr.FK_Emp, 130);

			////增加隐藏的查询条件.
		if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			AttrOfSearch search = new AttrOfSearch(MyDeptTodolistAttr.OrgNo, "组织",
					MyDeptTodolistAttr.OrgNo, "=",WebUser.getOrgNo(), 0, true);
			map.getAttrsOfSearch().Add(search);
		}

		RefMethod rm = new RefMethod();
		rm.Title = "轨迹";
		rm.ClassMethodName = this + ".DoTrack";
		rm.Icon = "../../WF/Img/FileType/doc.gif";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Icon = "../../WF/Img/Btn/CC.gif";
		rm.Title = "移交";
		rm.ClassMethodName = this + ".DoShift";
		rm.getHisAttrs().AddDDLEntities("ToEmp", null, "移交给:", new Flows(), true);
		rm.getHisAttrs().AddTBString("Note", null, "移交原因", true, false, 0, 300, 100);
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Icon = "../../WF/Img/Btn/Back.png";
		rm.Title = "回滚";
		rm.IsForEns = false;
		rm.ClassMethodName = this + ".DoComeBack";
		rm.getHisAttrs().AddTBInt("NodeID", 0, "回滚到节点", true, false);
		rm.getHisAttrs().AddTBString("Note", null, "回滚原因", true, false, 0, 300, 100);
		map.AddRefMethod(rm);


		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion


		///#region 执行功能.
	public final String DoTrack()throws Exception
	{
		return "../../WFRpt.htm?WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow() + "&FK_Node=";
	}
	/** 
	 执行移交
	 
	 param ToEmp
	 param Note
	 return
	*/
	public final String DoShift(String ToEmp, String Note) throws Exception {
		if (Dev2Interface.Flow_IsCanViewTruck(this.getFK_Flow(), this.getWorkID()) == false)
		{
			return "您没有操作该流程数据的权限.";
		}

		try
		{
			Dev2Interface.Node_Shift(this.getWorkID(), ToEmp, Note);
			return "移交成功";
		}
		catch (RuntimeException ex)
		{
			return "移交失败@" + ex.getMessage();
		}
	}
	/** 
	 执行删除
	 
	 return
	*/
	public final String DoDelete()throws Exception
	{
		if (Dev2Interface.Flow_IsCanViewTruck(this.getFK_Flow(), this.getWorkID()) == false)
		{
			return "您没有操作该流程数据的权限.";
		}

		try
		{
			Dev2Interface.Flow_DoDeleteFlowByReal(this.getWorkID(), true);
			return "删除成功";
		}
		catch (RuntimeException ex)
		{
			return "删除失败@" + ex.getMessage();
		}
	}
	public final String DoSkip()throws Exception
	{
		return "../../Admin/FlowDB/FlowSkip.htm?WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getFK_Node();
	}
	/** 
	 回滚
	 
	 param nodeid 节点ID
	 param note 回滚原因
	 @return 回滚的结果
	*/
	public final String DoComeBack(int nodeid, String note) throws Exception {
		bp.wf.template.FlowSheet fl = new bp.wf.template.FlowSheet(this.getFK_Flow());
		return fl.DoRebackFlowData(this.getWorkID(), nodeid, note);
	}

		///#endregion
}