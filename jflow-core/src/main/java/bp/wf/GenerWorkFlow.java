package bp.wf;

import bp.da.*;
import bp.sys.*;
import bp.en.*;


/** 
 流程实例
*/
public class GenerWorkFlow extends Entity
{

		///#region 基本属性
	/** 
	 主键
	*/
	@Override
	public String getPK()  {
		return GenerWorkFlowAttr.WorkID;
	}
	public final String getOrgNo()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.OrgNo);
	}
	public final void setOrgNo(String value)
	{SetValByKey(GenerWorkFlowAttr.OrgNo, value);
	}
	/** 
	 所在的域
	*/
	public final String getDomain()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.Domain);
	}
	public final void setDomain(String value)
	{SetValByKey(GenerWorkFlowAttr.Domain, value);
	}
	/** 
	 备注
	*/
	public final String getFlowNote()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.FlowNote);
	}
	public final void setFlowNote(String value)
	{SetValByKey(GenerWorkFlowAttr.FlowNote, value);
	}
	public final String getBuessFields()  {
		return this.GetParaString("BuessFields");
	}
	public final void setBuessFields(String value)
	{this.SetPara("BuessFields", value);
	}

	/** 
	 工作流程编号
	*/
	public final String getFK_Flow()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)
	{SetValByKey(GenerWorkFlowAttr.FK_Flow, value);
	}
	/** 
	 BillNo
	*/
	public final String getBillNo()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.BillNo);
	}
	public final void setBillNo(String value)
	{SetValByKey(GenerWorkFlowAttr.BillNo, value);
	}
	/** 
	 最后的发送人
	*/
	public final String getSender()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.Sender);
	}
	public final void setSender(String value)
	{//检查数据正确性.
		if (DataType.IsNullOrEmpty(value) == true)
		{
			throw new RuntimeException("err@设置的人员不能为空.");
		}

		if (value.contains(";") == false)
		{
			value = value + ";";
		}

			//检查数据正确性.
		if (value.contains(",") == false || value.contains(";") == false)
		{
			throw new RuntimeException("err@设置的Sender人员格式不正确，请联系管理员,格式为:No,Name; 您设置的值为:" + value);
		}

			//发送人.
		this.SetValByKey(GenerWorkFlowAttr.Sender, value);

			//当前日期.
		this.SetValByKey(GenerWorkFlowAttr.SendDT, DataType.getCurrentDataTime());
	}
	/** 
	 发送日期
	*/
	public final String getSendDT()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.SendDT);
	}
	public final void setSendDT(String value)
	{SetValByKey(GenerWorkFlowAttr.SendDT, value);
	}

	/** 
	 流程名称
	*/
	public final String getFlowName()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.FlowName);
	}
	public final void setFlowName(String value)
	{SetValByKey(GenerWorkFlowAttr.FlowName, value);
	}
	/** 
	 优先级
	*/
	public final int getPRI()
	{
		return this.GetValIntByKey(GenerWorkFlowAttr.PRI);
	}
	public final void setPRI(int value)
	{SetValByKey(GenerWorkFlowAttr.PRI, value);
	}
	/** 
	 待办人员数量
	*/
	public final int getTodoEmpsNum()
	{
		return this.GetValIntByKey(GenerWorkFlowAttr.TodoEmpsNum);
	}
	public final void setTodoEmpsNum(int value)
	{SetValByKey(GenerWorkFlowAttr.TodoEmpsNum, value);
	}
	/** 
	 待办人员列表
	*/
	public final String getTodoEmps()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.TodoEmps);
	}
	public final void setTodoEmps(String value)
	{String str = value;
		str = str.replace(" ", "");
			//TodoEmps在会签完去掉人员此判断去不掉，暂时注释掉
			//string val = this.GetValStrByKey(GenerWorkFlowAttr.TodoEmps);
			//if (val.contains(str) == true)
			//    return;

		SetValByKey(GenerWorkFlowAttr.TodoEmps, str);
	}

	/** 
	 参与人
	*/
	public final String getEmps()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.Emps);
	}
	public final void setEmps(String value)
	 {
		this.SetValByKey(GenerWorkFlowAttr.Emps, value);
	}
	/** 
	 会签状态
	*/
	public final HuiQianTaskSta getHuiQianTaskSta()  {
			//如果有方向信息，并且方向不包含到达的节点.
		if (this.getHuiQianSendToNodeIDStr().length() > 3 && this.getHuiQianSendToNodeIDStr().contains(this.getFK_Node() + ",") == false)
		{
			return HuiQianTaskSta.None;
		}

		return HuiQianTaskSta.forValue(this.GetParaInt(GenerWorkFlowAttr.HuiQianTaskSta, 0));
	}
	public final void setHuiQianTaskSta(HuiQianTaskSta value)
	{SetPara(GenerWorkFlowAttr.HuiQianTaskSta, value.getValue());
	}
	/** 
	 共享任务池状态
	*/
	public final TaskSta getTaskSta()  {
		return TaskSta.forValue(this.GetValIntByKey(GenerWorkFlowAttr.TaskSta));
	}
	public final void setTaskSta(TaskSta value)
	{SetValByKey(GenerWorkFlowAttr.TaskSta, value.getValue());
	}
	/** 
	 类别编号
	*/
	public final String getFK_FlowSort()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.FK_FlowSort);
	}
	public final void setFK_FlowSort(String value)
	{SetValByKey(GenerWorkFlowAttr.FK_FlowSort, value);
	}
	/** 
	 系统类别
	*/
	public final String getSysType()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.SysType);
	}
	public final void setSysType(String value)
	{SetValByKey(GenerWorkFlowAttr.SysType, value);
	}
	/** 
	 发起人部门
	*/
	public final String getFK_Dept()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.FK_Dept);
	}
	public final void setFK_Dept(String value)
	{SetValByKey(GenerWorkFlowAttr.FK_Dept, value);
	}
	/** 
	 标题
	*/
	public final String getTitle()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.Title);
	}
	public final void setTitle(String value)
	{SetValByKey(GenerWorkFlowAttr.Title, value);
	}
	/** 
	 客户编号
	*/
	public final String getGuestNo()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.GuestNo);
	}
	public final void setGuestNo(String value)
	{SetValByKey(GenerWorkFlowAttr.GuestNo, value);
	}
	/** 
	 客户名称
	*/
	public final String getGuestName()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.GuestName);
	}
	public final void setGuestName(String value)
	{SetValByKey(GenerWorkFlowAttr.GuestName, value);
	}
	/** 
	 年月
	*/
	public final String getFkNy()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.FK_NY);
	}
	public final void setFk_Ny(String value)
	{SetValByKey(GenerWorkFlowAttr.FK_NY, value);
	}
	/** 
	 实际开始时间
	*/
	public final String getRDT()  {
			//string rdt = this.GetParaString("");
		return this.GetValStrByKey(GenerWorkFlowAttr.RDT);
	}
	public final void setRDT(String value)
	 {
		this.SetValByKey(GenerWorkFlowAttr.RDT, value);
		this.setFk_Ny(value.substring(0, 7));
	}
	public final String getHungupTime()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.HungupTime);
	}
	public final void setHungupTime(String value)
	 {
		this.SetValByKey(GenerWorkFlowAttr.HungupTime, value);
	}
	/** 
	 计划开始时间
	 SDTOfFlow 就是计划完成日期.
	*/
	public final String getRDTOfSetting()  {
		String str = this.GetParaString("RDTOfSetting");
		if (DataType.IsNullOrEmpty(str) == true)
		{
			return this.getRDT();
		}
		return str;
	}
	public final void setRDTOfSetting(String value)
	{this.SetPara("RDTOfSetting", value);
	}
	/** 
	 节点应完成时间
	*/
	public final String getSDTOfNode()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.SDTOfNode);
	}
	public final void setSDTOfNode(String value)
	{SetValByKey(GenerWorkFlowAttr.SDTOfNode, value);
	}
	/** 
	 流程应完成时间
	 RDTOfSetting 是计划开始日期，如果为空就是发起日期.
	*/
	public final String getSDTOfFlow()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.SDTOfFlow);
	}
	public final void setSDTOfFlow(String value)
	{SetValByKey(GenerWorkFlowAttr.SDTOfFlow, value);
	}
	/** 
	 流程预警时间时间
	*/
	public final String getSDTOfFlowWarning()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.SDTOfFlowWarning);
	}
	public final void setSDTOfFlowWarning(String value)
	{SetValByKey(GenerWorkFlowAttr.SDTOfFlowWarning, value);
	}
	/** 
	 流程ID
	*/
	public final long getWorkID()
	{
		return this.GetValInt64ByKey(GenerWorkFlowAttr.WorkID);
	}
	public final void setWorkID(long value)
	{SetValByKey(GenerWorkFlowAttr.WorkID, value);
	}
	/** 
	 主线程ID
	*/
	public final long getFID()
	{
		return this.GetValInt64ByKey(GenerWorkFlowAttr.FID);
	}
	public final void setFID(long value)
	{SetValByKey(GenerWorkFlowAttr.FID, value);
	}
	/** 
	 父节点流程编号.
	*/
	public final long getPWorkID()
	{
		return this.GetValInt64ByKey(GenerWorkFlowAttr.PWorkID);
	}
	public final void setPWorkID(long value)
	{SetValByKey(GenerWorkFlowAttr.PWorkID, value);
	}
	public final long getPFID()
	{
		return this.GetValInt64ByKey(GenerWorkFlowAttr.PFID);
	}
	public final void setPFID(long value)
	{SetValByKey(GenerWorkFlowAttr.PFID, value);
	}
	/** 
	 父流程调用的节点
	*/
	public final int getPNodeID()
	{
		return this.GetValIntByKey(GenerWorkFlowAttr.PNodeID);
	}
	public final void setPNodeID(int value)
	{SetValByKey(GenerWorkFlowAttr.PNodeID, value);
	}
	/** 
	 PFlowNo
	*/
	public final String getPFlowNo()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.PFlowNo);
	}
	public final void setPFlowNo(String value)
	{SetValByKey(GenerWorkFlowAttr.PFlowNo, value);
	}
	/** 
	 项目编号
	*/
	public final String getPrjNo()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.PrjNo);
	}
	public final void setPrjNo(String value)
	{SetValByKey(GenerWorkFlowAttr.PrjNo, value);
	}
	/** 
	 项目名称
	*/
	public final String getPrjName()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.PrjName);
	}
	public final void setPrjName(String value)
	{SetValByKey(GenerWorkFlowAttr.PrjName, value);
	}
	/** 
	 吊起子流程的人员
	*/
	public final String getPEmp()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.PEmp);
	}
	public final void setPEmp(String value)
	{SetValByKey(GenerWorkFlowAttr.PEmp, value);
	}
	/** 
	 发起人
	*/
	public final String getStarter()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.Starter);
	}
	public final void setStarter(String value)
	{SetValByKey(GenerWorkFlowAttr.Starter, value);
	}
	/** 
	 发起人名称
	*/
	public final String getStarterName()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.StarterName);
	}
	public final void setStarterName(String value)
	 {
		this.SetValByKey(GenerWorkFlowAttr.StarterName, value);
	}
	/** 
	 发起人部门名称
	*/
	public final String getDeptName()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.DeptName);
	}
	public final void setDeptName(String value)
	 {
		this.SetValByKey(GenerWorkFlowAttr.DeptName, value);
	}
	/** 
	 当前节点名称
	*/
	public final String getNodeName()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.NodeName);
	}
	public final void setNodeName(String value)
	 {
		this.SetValByKey(GenerWorkFlowAttr.NodeName, value);
	}
	/** 
	 当前工作到的节点
	*/
	public final int getFK_Node()
	{
		return this.GetValIntByKey(GenerWorkFlowAttr.FK_Node);
	}
	public final void setFK_Node(int value)
	{SetValByKey(GenerWorkFlowAttr.FK_Node, value);
	}
	/** 
	 工作流程状态
	*/
	public final WFState getWFState()  {
		return WFState.forValue(this.GetValIntByKey(GenerWorkFlowAttr.WFState));
	}
	public final void setWFState(WFState value)
	{if (value == bp.wf.WFState.Complete)
		{
			SetValByKey(GenerWorkFlowAttr.WFSta, WFSta.Complete.getValue());
		}
		else if (value == bp.wf.WFState.Delete || value == bp.wf.WFState.Blank)
		{
			SetValByKey(GenerWorkFlowAttr.WFSta, WFSta.Etc.getValue());
		}
		else
		{
			SetValByKey(GenerWorkFlowAttr.WFSta, WFSta.Runing.getValue());
		}

		SetValByKey(GenerWorkFlowAttr.WFState, value.getValue());
	}
	/** 
	 状态(简单)
	*/
	public final WFSta getWFSta()  {
		return WFSta.forValue(this.GetValIntByKey(GenerWorkFlowAttr.WFSta));
	}
	/** 
	 是否可以批处理？
	*/
	public final boolean isCanBatch()  {
		return this.GetParaBoolen("IsCanBatch");
	}
	public final void setCanBatch(boolean value)
	{this.SetPara("IsCanBatch", value);
	}
	/** 
	 状态
	*/
	public final String getWFStateText()  {
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
			case Draft:
				return "草稿";
			case ReturnSta:
				return "退回";
			default:
				return "其他" + ws.toString();
		}
	}
	/** 
	 GUID
	*/
	public final String getGUID()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.GUID);
	}
	public final void setGUID(String value)
	{SetValByKey(GenerWorkFlowAttr.GUID, value);
	}

		///#endregion


		///#region 扩展属性
	/** 
	 它的子流程
	*/
	public final GenerWorkFlows getHisSubFlowGenerWorkFlows() throws Exception {
		GenerWorkFlows ens = new GenerWorkFlows();
		ens.Retrieve(GenerWorkFlowAttr.PWorkID, this.getWorkID(), null);
		return ens;
	}
	/** 
	 0=待办中,1=预警中,2=逾期中,3=按期完成,4=逾期完成
	*/
	public final int getTodoSta()
	{
		return this.GetValIntByKey(GenerWorkFlowAttr.TodoSta);
	}

		///#endregion 扩展属性


		///#region 参数属性.
	/** 
	 是否是流程模版?
	*/
	public final boolean getParasDBTemplate()  {
		return this.GetParaBoolen("DBTemplate");
	}
	public final void setParasDBTemplate(boolean value)
	{this.SetPara("DBTemplate", value);
	}
	/** 
	 模版名称
	*/
	public final String getParasDBTemplateName()  {
		return this.GetParaString("DBTemplateName");
	}
	public final void setParasDBTemplateName(String value)
	{this.SetPara("DBTemplateName", value);
	}
	/** 
	 选择的表单(用于子流程列表里，打开草稿，记录当初选择的表单.)
	*/
	public final String getParasFrms()  {
		return this.GetParaString("Frms");
	}
	public final void setParasFrms(String value)
	{this.SetPara("Frms", value);
	}
	/** 
	 到达的节点
	*/
	public final String getParasToNodes()  {
		return this.GetParaString("ToNodes");
	}
	public final void setParasToNodes(String value)
	{this.SetPara("ToNodes", value);
	}
	/** 
	 关注&取消关注
	*/
	public final boolean getParasFocus()  {
		return this.GetParaBoolen("F_" + bp.web.WebUser.getNo(), false);
	}
	public final void setParasFocus(boolean value)
	{this.SetPara("F_" + bp.web.WebUser.getNo(), value);
	}
	/** 
	 确认与取消确认
	*/
	public final boolean getParasConfirm()  {
		return this.GetParaBoolen("C_" + bp.web.WebUser.getNo(), false);
	}
	public final void setParasConfirm(boolean value)
	{this.SetPara("C_" + bp.web.WebUser.getNo(), value);
	}
	/** 
	 最后一个执行发送动作的ID.
	*/
	public final String getParasLastSendTruckID()  {
		String str = this.GetParaString("LastTruckID");
		if (str.equals(""))
		{
			str = String.valueOf(this.getWorkID());
		}
		return str;
	}
	public final void setParasLastSendTruckID(String value)
	{this.SetPara("LastTruckID", value);
	}

	/** 
	 加签信息
	*/
	public final String getParasAskForReply()  {
		return this.GetParaString("AskForReply");
	}
	public final void setParasAskForReply(String value)
	{this.SetPara("AskForReply", value);
	}
	/** 
	 是否是退回并原路返回.
	*/

	public final boolean getParasIsTrackBack()  {
		return this.GetParaBoolen("IsTrackBack");
	}
	public final void setParasIsTrackBack(boolean value)
	{this.SetPara("IsTrackBack", value);
	}
	/** 
	 分组Mark
	*/
	public final String getParasGroupMark()  {
		return this.GetParaString(GenerWorkerListAttr.GroupMark);
	}
	public final void setParasGroupMark(String value)
	{this.SetPara(GenerWorkerListAttr.GroupMark, value);
	}
	/** 
	 是否是自动运行
	 0=自动运行(默认,无需人工干涉). 1=手工运行(按照手工设置的模式运行,人工干涉模式).
	 用于自由流程中.
	*/
	public final TransferCustomType getTransferCustomType()  {
		return TransferCustomType.forValue(this.GetParaInt("IsAutoRun", 0));
	}
	public final void setTransferCustomType(TransferCustomType value)
	{this.SetPara("IsAutoRun", value.getValue());
	}
	/** 
	 多人待办处理模式
	*/
	public final TodolistModel getTodolistModel()  {
		return TodolistModel.forValue(this.GetParaInt("TodolistModel", 0));
	}
	public final void setTodolistModel(TodolistModel value)
	{this.SetPara("TodolistModel", value.getValue());
	}
	/** 
	 会签到达人员
	*/
	public final String getHuiQianSendToEmps()  {
		return this.GetParaString("HuiQianSendToEmps");
	}
	public final void setHuiQianSendToEmps(String value)
	{this.SetPara("HuiQianSendToEmps", value);
	}
	/** 
	 会签到达节点: 101@102
	*/
	public final String getHuiQianSendToNodeIDStr() {
		return this.GetParaString("HuiQianSendToNodeID");
	}
	public final void setHuiQianSendToNodeIDStr(String value)
	{this.SetPara("HuiQianSendToNodeID", value);
	}
	/** 
	 会签主持人
	*/
	public final String getHuiQianZhuChiRen()  {
		return this.GetParaString("HuiQianZhuChiRen");
	}
	public final void setHuiQianZhuChiRen(String value)
	{this.SetPara("HuiQianZhuChiRen", value);
	}
	/** 
	 会签主持人名称
	*/
	public final String getHuiQianZhuChiRenName()  {
		return this.GetParaString("HuiQianZhuChiRenName");
	}
	public final void setHuiQianZhuChiRenName(String value)
	{this.SetPara("HuiQianZhuChiRenName", value);
	}

	public final int getScripNodeID()  {
		return this.GetParaInt("ScripNodeID", 0);
	}
	public final void setScripNodeID(int value)
	{this.SetPara("ScripNodeID", value);
	}
	public final void setScripMsg(String value)
	{this.SetPara("ScripMsg", value);
	}


		///#endregion 参数属性.


		///#region 构造函数
	/** 
	 产生的工作流程
	*/
	public GenerWorkFlow()  {
	}
	/** 
	 按照WorkID查询.
	 
	 param workId
	*/
	public GenerWorkFlow(long workId) throws Exception {
		//this.WorkID = workId
		//this.Retrieve();

		QueryObject qo = new QueryObject(this);
		qo.AddWhere(GenerWorkFlowAttr.WorkID, workId);
		if (qo.DoQuery() == 0)
		{
			throw new RuntimeException("工作 GenerWorkFlow [" + workId + "]不存在。");
		}
	}
	/** 
	 按照GUID查询.
	 
	 param guid
	*/
	public GenerWorkFlow(String guid) throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(GenerWorkFlowAttr.GUID, guid);
		if (qo.DoQuery() == 0)
		{
			throw new RuntimeException("工作 GenerWorkFlow [" + guid + "]不存在。");
		}
	}
	/** 
	 重写基类方法
	*/
	@Override
	public bp.en.Map getEnMap()  {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_GenerWorkFlow", "流程实例");
		map.setDepositaryOfMap(Depositary.Application);

		map.AddTBIntPK(GenerWorkFlowAttr.WorkID, 0, "WorkID", true, true); //主键.
		map.AddTBInt(GenerWorkFlowAttr.FID, 0, "流程ID", true, true);

		map.AddTBString(GenerWorkFlowAttr.FK_FlowSort, null, "流程类别", true, false, 0, 10, 10);

			//等于流程类别的Domain字段值.
		map.AddTBString(GenerWorkFlowAttr.SysType, null, "系统类别", true, false, 0, 10, 10);

		map.AddTBString(GenerWorkFlowAttr.FK_Flow, null, "流程", true, false, 0, 5, 10);
		map.AddTBString(GenerWorkFlowAttr.FlowName, null, "流程名称", true, false, 0, 100, 10);
		map.AddTBString(GenerWorkFlowAttr.Title, null, "标题", true, false, 0, 300, 10);

			//两个状态，在不同的情况下使用. WFState状态 可以查询到SELECT  * FROM sys_enum WHERE EnumKey='WFState'
			// WFState 的状态  @0=空白@1=草稿@2=运行中@3=已经完成@4=挂起@5=退回.
		map.AddTBInt(GenerWorkFlowAttr.WFSta, 0, "状态", true, false);
		map.AddTBInt(GenerWorkFlowAttr.WFState, 0, "状态", true, false);


			//  map.AddDDLSysEnum(GenerWorkFlowAttr.WFSta, 0, "状态", true, false, GenerWorkFlowAttr.WFSta, "@0=运行中@1=已完成@2=其他");
			//  map.AddDDLSysEnum(GenerWorkFlowAttr.WFState, 0, "流程状态", true, false, GenerWorkFlowAttr.WFState);

		map.AddTBString(GenerWorkFlowAttr.Starter, null, "发起人", true, false, 0, 200, 10);
		map.AddTBString(GenerWorkFlowAttr.StarterName, null, "发起人名称", true, false, 0, 200, 10);
		map.AddTBString(GenerWorkFlowAttr.Sender, null, "发送人", true, false, 0, 200, 10);

		map.AddTBDateTime(GenerWorkFlowAttr.RDT, "记录日期", true, true);
		map.AddTBString(GenerWorkFlowAttr.HungupTime, null, "挂起日期", true, false, 0, 50, 10);
		map.AddTBDateTime(GenerWorkFlowAttr.SendDT, "流程活动时间", true, true);
		map.AddTBInt(GenerWorkFlowAttr.FK_Node, 0, "节点", true, false);
		map.AddTBString(GenerWorkFlowAttr.NodeName, null, "节点名称", true, false, 0, 100, 10);

		map.AddTBString(GenerWorkFlowAttr.FK_Dept, null, "部门", true, false, 0, 100, 10);
		map.AddTBString(GenerWorkFlowAttr.DeptName, null, "部门名称", true, false, 0, 100, 10);
		map.AddTBInt(GenerWorkFlowAttr.PRI, 1, "优先级", true, true);

		map.AddTBDateTime(GenerWorkFlowAttr.SDTOfNode, "节点应完成时间", true, true);
		map.AddTBDateTime(GenerWorkFlowAttr.SDTOfFlow, null, "流程应完成时间", true, true);
		map.AddTBDateTime(GenerWorkFlowAttr.SDTOfFlowWarning, null, "流程预警时间", true, true);

			//父子流程信息.
		map.AddTBString(GenerWorkFlowAttr.PFlowNo, null, "父流程编号", true, false, 0, 100, 10);
		map.AddTBInt(GenerWorkFlowAttr.PWorkID, 0, "父流程ID", true, true);
		map.AddTBInt(GenerWorkFlowAttr.PNodeID, 0, "父流程调用节点", true, true);
		map.AddTBInt(GenerWorkFlowAttr.PFID, 0, "父流程调用的PFID", true, true);
		map.AddTBString(GenerWorkFlowAttr.PEmp, null, "子流程的调用人", true, false, 0, 100, 10);

			//客户流程信息.
		map.AddTBString(GenerWorkFlowAttr.GuestNo, null, "客户编号", true, false, 0, 100, 10);
		map.AddTBString(GenerWorkFlowAttr.GuestName, null, "客户名称", true, false, 0, 100, 10);

		map.AddTBString(GenerWorkFlowAttr.BillNo, null, "单据编号", true, false, 0, 100, 10);
		map.AddTBString(GenerWorkFlowAttr.FlowNote, null, "备注", true, false, 0, 4000, 10);

			//任务池相关。
		map.AddTBString(GenerWorkFlowAttr.TodoEmps, null, "待办人员", true, false, 0, 4000, 10);
		map.AddTBInt(GenerWorkFlowAttr.TodoEmpsNum, 0, "待办人员数量", true, true);
		map.AddTBInt(GenerWorkFlowAttr.TaskSta, 0, "共享状态", true, true);

			//参数.
		map.AddTBString(GenerWorkFlowAttr.AtPara, null, "参数(流程运行设置临时存储的参数)", true, false, 0, 2000, 10);

		map.AddTBString(GenerWorkFlowAttr.Emps, null, "参与人(格式:@zhangshan,张三@lishi,李四)", true, false, 0, 4000, 10);
		map.AddTBString(GenerWorkFlowAttr.GUID, null, "GUID", false, false, 0, 36, 10);
		map.AddTBString(GenerWorkFlowAttr.FK_NY, null, "年月", false, false, 0, 7, 7);
		map.AddTBInt(GenerWorkFlowAttr.WeekNum, 0, "周次", true, true);
		map.AddTBInt(GenerWorkFlowAttr.TSpan, 0, "时间间隔", true, true);

			//待办状态(0=待办中,1=预警中,2=逾期中,3=按期完成,4=逾期完成) 
		map.AddTBInt(GenerWorkFlowAttr.TodoSta, 0, "待办状态", true, true);

		map.AddTBString(GenerWorkFlowAttr.Domain, null, "域/系统编号", true, false, 0, 100, 30);
			//map.SetHelperAlert(GenerWorkFlowAttr.Domain, "用于区分不同系统的流程,比如:一个集团有多个子系统每个子系统都有自己的流程,就需要标记那些流程是那个子系统的.");

		map.AddTBString(GenerWorkFlowAttr.PrjNo, null, "PrjNo", true, false, 0, 100, 10);
		map.AddTBString(GenerWorkFlowAttr.PrjName, null, "PrjNo", true, false, 0, 100, 10);

			//隶属组织.
		map.AddTBString(GenerWorkFlowAttr.OrgNo, null, "OrgNo", true, false, 0, 50, 10);

		RefMethod rm = new RefMethod();
		rm.Title = "工作轨迹"; // "工作报告";
		rm.ClassMethodName = this.toString() + ".DoRpt";
		rm.Icon = "../../WF/Img/FileType/doc.gif";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "流程自检"; // "流程自检";
		rm.ClassMethodName = this.toString() + ".DoSelfTestInfo";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "流程自检并修复";
		rm.ClassMethodName = this.toString() + ".DoRepare";
		rm.Warning = "您确定要执行此功能吗？ \t\n 1)如果是断流程，并且停留在第一个节点上，系统为执行删除它。\t\n 2)如果是非地第一个节点，系统会返回到上次发起的位置。";
		map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion


		///#region 重载基类方法
	/** 
	 删除后,需要把工作者列表也要删除.
	*/
	@Override
	protected void afterDelete() throws Exception {
		switch (bp.difference.SystemConfig.getAppCenterDBType( ))
		{
			case MSSQL:
			case Oracle:
			case KingBaseR3:
			case KingBaseR6:
				DBAccess.RunSQL("DELETE FROM WF_GenerWorkerlist WHERE WorkID in  ( select WorkID from WF_GenerWorkerlist WHERE WorkID not in (select WorkID from WF_GenerWorkFlow) )");
				break;
			case MySQL:
				DBAccess.RunSQL("DELETE A FROM WF_GenerWorkerlist A, WF_GenerWorkerlist B WHERE A.WorkID = B.WorkID And B.WorkID Not IN(select WorkID from WF_GenerWorkFlow)");
				break;
			case PostgreSQL:
			case UX:
				DBAccess.RunSQL("DELETE FROM WF_GenerWorkerlist A USING WF_GenerWorkerlist B WHERE A.WorkID = B.WorkID And B.WorkID Not IN(select WorkID from WF_GenerWorkFlow)");
				break;
			default:
				break;

		}

		WorkFlow wf = new WorkFlow(new Flow(this.getFK_Flow()), this.getWorkID(), this.getFID());
		wf.DoDeleteWorkFlowByReal(true); // 删除下面的工作。
		super.afterDelete();
	}

	@Override
	protected boolean beforeInsert() throws Exception {
		if (this.getStarter().equals("Guest"))
		{
			this.setStarterName(bp.web.GuestUser.getName());
			this.setGuestName(this.getStarterName());
			this.setGuestNo(bp.web.GuestUser.getNo());
		}

		//加入组织no.
		if (Glo.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			this.setOrgNo(bp.web.WebUser.getOrgNo());
		}

		//生成GUID.
		this.setGUID(DBAccess.GenerGUID(0, null, null));

		return super.beforeInsert();
	}

		///#endregion


		///#region 执行诊断
	/** 
	 生成父子流程的甘特图
	 
	 @return 
	*/
	public final String GenerGantt() throws Exception {
		return Glo.GenerGanttDataOfSubFlows(this.getWorkID());
	}
	/** 
	 终止流程
	 
	 param msg 终止的信息
	 @return 终止结果
	*/
	public final String DoFix(String msg) throws Exception {
		return Dev2Interface.Flow_DoFix(this.getWorkID(), true, msg);
	}

	public final String DoRpt() throws Exception {
		return "WFRpt.htm?WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow();
	}
	/** 
	 增加子线程
	 
	 param empStrs 要增加的人员多个用都好分开.
	 @return 
	*/
	public final String DoSubFlowAddEmps(String empStrs,int toNodeID) throws Exception {
		//获得当前的干流程的gwf.
		long workID = this.getFID();
		if (workID == 0)
		{
			workID = this.getWorkID();
		}
		return Dev2Interface.Node_FHL_AddSubThread(workID, empStrs, toNodeID);
	}
	/** 
	 执行修复
	 
	 @return 
	*/
	public final String DoRepare() throws Exception {
		if (this.DoSelfTestInfo().equals("没有发现异常。"))
		{
			return "没有发现异常。";
		}

		String sql = "SELECT FK_Node FROM WF_GenerWorkerlist WHERE WORKID='" + this.getWorkID() + "' ORDER BY FK_Node desc";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			/*如果是开始工作节点，就删除它。*/
			WorkFlow wf = new WorkFlow(new Flow(this.getFK_Flow()), this.getWorkID(), this.getFID());
			wf.DoDeleteWorkFlowByReal(true);
			return "此流程是因为发起工作失败被系统删除。";
		}

		int FK_Node = Integer.parseInt(dt.Rows.get(0).getValue(0).toString());

		Node nd = new Node(FK_Node);
		if (nd.isStartNode())
		{
			/*如果是开始工作节点，就删除它。*/
			WorkFlow wf = new WorkFlow(new Flow(this.getFK_Flow()), this.getWorkID(), this.getFID());
			wf.DoDeleteWorkFlowByReal(true);
			return "此流程是因为发起工作失败被系统删除。";
		}

		this.setFK_Node(nd.getNodeID());
		this.setNodeName(nd.getName());
		this.Update();

		String str = "";
		GenerWorkerLists wls = new GenerWorkerLists();
		wls.Retrieve(GenerWorkerListAttr.FK_Node, FK_Node, GenerWorkerListAttr.WorkID, this.getWorkID(), null);
		for (GenerWorkerList wl : wls.ToJavaList())
		{
			str += wl.getFK_Emp() + wl.getFK_EmpText() + ",";
		}

		return "此流程是因为[" + nd.getName() + "]工作发送失败被回滚到当前位置，请转告[" + str + "]流程修复成功。";
	}
	public final String DoSelfTestInfo() throws Exception {
		GenerWorkerLists wls = new GenerWorkerLists(this.getWorkID(), this.getFK_Flow());


			///#region  查看一下当前的节点是否开始工作节点。
		Node nd = new Node(this.getFK_Node());
		if (nd.isStartNode())
		{
			/* 判断是否是退回的节点 */
			Work wk = nd.getHisWork();
			wk.setOID(this.getWorkID());
			wk.Retrieve();
		}

			///#endregion



			///#region  查看一下是否有当前的工作节点信息。
		boolean isHave = false;
		for (GenerWorkerList wl : wls.ToJavaList())
		{
			if (wl.getFK_Node() == this.getFK_Node())
			{
				isHave = true;
			}
		}

		if (isHave == false)
		{
			/*  */
			return "已经不存在当前的工作节点信息，造成此流程的原因可能是没有捕获的系统异常，建议删除此流程或者交给系统自动修复它。";
		}

			///#endregion

		return "没有发现异常。";
	}

		///#endregion
}