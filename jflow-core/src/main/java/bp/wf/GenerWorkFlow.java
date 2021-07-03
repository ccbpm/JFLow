package bp.wf;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.sys.*;
import bp.web.GuestUser;
import bp.web.WebUser;
import bp.en.*;
import bp.en.Map;

/** 
 流程实例
*/
public class GenerWorkFlow extends Entity
{

		///基本属性
	/** 
	 主键
	*/
	@Override
	public String getPK()
	{
		return GenerWorkFlowAttr.WorkID;
	}
	public final String getOrgNo() throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.OrgNo);
	}
	public final void setOrgNo(String value) throws Exception
	{
		SetValByKey(GenerWorkFlowAttr.OrgNo, value);
	}
	/** 
	 所在的域
	*/
	public final String getDomain() throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.Domain);
	}
	public final void setDomain(String value) throws Exception
	{
		SetValByKey(GenerWorkFlowAttr.Domain, value);
	}
	/** 
	 备注
	*/
	public final String getFlowNote() throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.FlowNote);
	}
	public final void setFlowNote(String value) throws Exception
	{
		SetValByKey(GenerWorkFlowAttr.FlowNote, value);
	}
	public final String getBuessFields() throws Exception
	{
		return this.GetParaString("BuessFields");
	}
	public final void setBuessFields(String value) throws Exception
	{
		this.SetPara("BuessFields", value);
	}

	/** 
	 工作流程编号
	*/
	public final String getFK_Flow() throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.FK_Flow);
	}
	public final void setFK_Flow(String value) throws Exception
	{
		SetValByKey(GenerWorkFlowAttr.FK_Flow,value);
	}
	/** 
	 BillNo
	*/
	public final String getBillNo() throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.BillNo);
	}
	public final void setBillNo(String value) throws Exception
	{
		SetValByKey(GenerWorkFlowAttr.BillNo, value);
	}
	/** 
	 最后的发送人
	*/
	public final String getSender() throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.Sender);
	}
	public final void setSender(String value) throws Exception
	{
			//发送人.
		this.SetValByKey(GenerWorkFlowAttr.Sender, value);

			//当前日期.
		this.SetValByKey(GenerWorkFlowAttr.SendDT, DataType.getCurrentDataTime());
	}
	/** 
	 发送日期
	*/
	public final String getSendDT() throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.SendDT);
	}
	public final void setSendDT(String value) throws Exception
	{
		SetValByKey(GenerWorkFlowAttr.SendDT, value);
	}

	/** 
	 流程名称
	*/
	public final String getFlowName() throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.FlowName);
	}
	public final void setFlowName(String value) throws Exception
	{
		SetValByKey(GenerWorkFlowAttr.FlowName, value);
	}
	/** 
	 优先级
	*/
	public final int getPRI() throws Exception
	{
		return this.GetValIntByKey(GenerWorkFlowAttr.PRI);
	}
	public final void setPRI(int value) throws Exception
	{
		SetValByKey(GenerWorkFlowAttr.PRI, value);
	}
	/** 
	 待办人员数量
	*/
	public final int getTodoEmpsNum() throws Exception
	{
		return this.GetValIntByKey(GenerWorkFlowAttr.TodoEmpsNum);
	}
	public final void setTodoEmpsNum(int value) throws Exception
	{
		SetValByKey(GenerWorkFlowAttr.TodoEmpsNum, value);
	}
	/** 
	 待办人员列表
	*/
	public final String getTodoEmps() throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.TodoEmps);
	}
	public final void setTodoEmps(String value) throws Exception
	{
		String str = value;
		str = str.replace(" ", "");
			//TodoEmps在会签完去掉人员此判断去不掉，暂时注释掉
			//string val = this.GetValStrByKey(GenerWorkFlowAttr.TodoEmps);
			//if (val.Contains(str) == true)
			//    return;

		SetValByKey(GenerWorkFlowAttr.TodoEmps, str);
	}

	/** 
	 参与人
	*/
	public final String getEmps() throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.Emps);
	}
	public final void setEmps(String value) throws Exception
	{
		this.SetValByKey(GenerWorkFlowAttr.Emps, value);
	}
	/** 
	 会签状态
	 * @throws Exception 
	*/
	public final HuiQianTaskSta getHuiQianTaskSta() throws Exception
	{
			//如果有方向信息，并且方向不包含到达的节点.
		if (this.getHuiQianSendToNodeIDStr().length() > 3 && this.getHuiQianSendToNodeIDStr().contains(this.getFK_Node() + ",") == false)
		{
			return HuiQianTaskSta.None;
		}

		return HuiQianTaskSta.forValue(this.GetParaInt(GenerWorkFlowAttr.HuiQianTaskSta, 0));
	}
	public final void setHuiQianTaskSta(HuiQianTaskSta value)throws Exception
	{
		SetPara(GenerWorkFlowAttr.HuiQianTaskSta, value.getValue());
	}
	/** 
	 共享任务池状态
	*/
	public final TaskSta getTaskSta()throws Exception
	{
		return TaskSta.forValue(this.GetValIntByKey(GenerWorkFlowAttr.TaskSta));
	}
	public final void setTaskSta(TaskSta value)throws Exception
	{
		SetValByKey(GenerWorkFlowAttr.TaskSta, value.getValue());
	}
	/** 
	 类别编号
	*/
	public final String getFK_FlowSort()throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.FK_FlowSort);
	}
	public final void setFK_FlowSort(String value)throws Exception
	{
		SetValByKey(GenerWorkFlowAttr.FK_FlowSort, value);
	}
	/** 
	 系统类别
	*/
	public final String getSysType()throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.SysType);
	}
	public final void setSysType(String value)throws Exception
	{
		SetValByKey(GenerWorkFlowAttr.SysType, value);
	}
	/** 
	 发起人部门
	*/
	public final String getFK_Dept()throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.FK_Dept);
	}
	public final void setFK_Dept(String value)throws Exception
	{
		SetValByKey(GenerWorkFlowAttr.FK_Dept,value);
	}
	/** 
	 标题
	*/
	public final String getTitle()throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.Title);
	}
	public final void setTitle(String value)throws Exception
	{
		SetValByKey(GenerWorkFlowAttr.Title,value);
	}
	/** 
	 客户编号
	*/
	public final String getGuestNo()throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.GuestNo);
	}
	public final void setGuestNo(String value)throws Exception
	{
		SetValByKey(GenerWorkFlowAttr.GuestNo, value);
	}
	/** 
	 客户名称
	*/
	public final String getGuestName()throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.GuestName);
	}
	public final void setGuestName(String value)throws Exception
	{
		SetValByKey(GenerWorkFlowAttr.GuestName, value);
	}
	/** 
	 年月
	*/
	public final String getFK_NY()throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.FK_NY);
	}
	public final void setFK_NY(String value)throws Exception
	{
		SetValByKey(GenerWorkFlowAttr.FK_NY, value);
	}
	/** 
	 实际开始时间
	*/
	public final String getRDT()throws Exception
	{
			//string rdt = this.GetParaString("");
		return this.GetValStrByKey(GenerWorkFlowAttr.RDT);
	}
	public final void setRDT(String value)throws Exception
	{
		this.SetValByKey(GenerWorkFlowAttr.RDT, value);
		this.setFK_NY(value.substring(0, 7));
	}
	/** 
	 计划开始时间
	 SDTOfFlow 就是计划完成日期.
	*/
	public final String getRDTOfSetting()throws Exception
	{
		String str = this.GetParaString("RDTOfSetting");
		if (DataType.IsNullOrEmpty(str) == true)
		{
			return this.getRDT();
		}
		return str;
	}
	public final void setRDTOfSetting(String value)throws Exception
	{
		this.SetPara("RDTOfSetting", value);
	}
	/** 
	 节点应完成时间
	*/
	public final String getSDTOfNode()throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.SDTOfNode);
	}
	public final void setSDTOfNode(String value)throws Exception
	{
		SetValByKey(GenerWorkFlowAttr.SDTOfNode, value);
	}
	/** 
	 流程应完成时间
	 RDTOfSetting 是计划开始日期，如果为空就是发起日期.
	*/
	public final String getSDTOfFlow()throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.SDTOfFlow);
	}
	public final void setSDTOfFlow(String value)throws Exception
	{
		SetValByKey(GenerWorkFlowAttr.SDTOfFlow, value);
	}
	/** 
	 流程预警时间时间
	*/
	public final String getSDTOfFlowWarning()throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.SDTOfFlowWarning);
	}
	public final void setSDTOfFlowWarning(String value)throws Exception
	{
		SetValByKey(GenerWorkFlowAttr.SDTOfFlowWarning, value);
	}
	/** 
	 流程ID
	*/
	public final long getWorkID()throws Exception
	{
		return this.GetValInt64ByKey(GenerWorkFlowAttr.WorkID);
	}
	public final void setWorkID(long value)throws Exception
	{
		SetValByKey(GenerWorkFlowAttr.WorkID,value);
	}
	/** 
	 主线程ID
	*/
	public final long getFID()throws Exception
	{
		return this.GetValInt64ByKey(GenerWorkFlowAttr.FID);
	}
	public final void setFID(long value)throws Exception
	{
		SetValByKey(GenerWorkFlowAttr.FID, value);
	}
	/** 
	 父节点流程编号.
	*/
	public final long getPWorkID()throws Exception
	{
		return this.GetValInt64ByKey(GenerWorkFlowAttr.PWorkID);
	}
	public final void setPWorkID(long value)throws Exception
	{
		SetValByKey(GenerWorkFlowAttr.PWorkID, value);
	}
	public final long getPFID()throws Exception
	{
		return this.GetValInt64ByKey(GenerWorkFlowAttr.PFID);
	}
	public final void setPFID(long value)throws Exception
	{
		SetValByKey(GenerWorkFlowAttr.PFID, value);
	}
	/** 
	 父流程调用的节点
	*/
	public final int getPNodeID()throws Exception
	{
		return this.GetValIntByKey(GenerWorkFlowAttr.PNodeID);
	}
	public final void setPNodeID(int value)throws Exception
	{
		SetValByKey(GenerWorkFlowAttr.PNodeID, value);
	}
	/** 
	 PFlowNo
	*/
	public final String getPFlowNo()throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.PFlowNo);
	}
	public final void setPFlowNo(String value)throws Exception
	{
		SetValByKey(GenerWorkFlowAttr.PFlowNo, value);
	}
	/** 
	 项目编号
	*/
	public final String getPrjNo()throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.PrjNo);
	}
	public final void setPrjNo(String value)throws Exception
	{
		SetValByKey(GenerWorkFlowAttr.PrjNo, value);
	}
	/** 
	 项目名称
	*/
	public final String getPrjName()throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.PrjName);
	}
	public final void setPrjName(String value)throws Exception
	{
		SetValByKey(GenerWorkFlowAttr.PrjName, value);
	}
	/** 
	 吊起子流程的人员
	*/
	public final String getPEmp()throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.PEmp);
	}
	public final void setPEmp(String value)throws Exception
	{
		SetValByKey(GenerWorkFlowAttr.PEmp, value);
	}
	/** 
	 发起人
	*/
	public final String getStarter()throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.Starter);
	}
	public final void setStarter(String value)throws Exception
	{
		SetValByKey(GenerWorkFlowAttr.Starter, value);
	}
	/** 
	 发起人名称
	*/
	public final String getStarterName()throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.StarterName);
	}
	public final void setStarterName(String value)throws Exception
	{
		this.SetValByKey(GenerWorkFlowAttr.StarterName, value);
	}
	/** 
	 发起人部门名称
	*/
	public final String getDeptName()throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.DeptName);
	}
	public final void setDeptName(String value)throws Exception
	{
		this.SetValByKey(GenerWorkFlowAttr.DeptName, value);
	}
	/** 
	 当前节点名称
	*/
	public final String getNodeName()throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.NodeName);
	}
	public final void setNodeName(String value)throws Exception
	{
		this.SetValByKey(GenerWorkFlowAttr.NodeName, value);
	}
	/** 
	 当前工作到的节点
	*/
	public final int getFK_Node()throws Exception
	{
		return this.GetValIntByKey(GenerWorkFlowAttr.FK_Node);
	}
	public final void setFK_Node(int value)throws Exception
	{
		SetValByKey(GenerWorkFlowAttr.FK_Node, value);
	}
	/** 
	 工作流程状态
	*/
	public final WFState getWFState()throws Exception
	{
		return WFState.forValue(this.GetValIntByKey(GenerWorkFlowAttr.WFState));
	}
	public final void setWFState(WFState value)throws Exception
	{
		if (value == WFState.Complete)
		{
			SetValByKey(GenerWorkFlowAttr.WFSta, getWFSta().Complete.getValue());
		}
		else if (value == WFState.Delete || value == WFState.Blank)
		{
			SetValByKey(GenerWorkFlowAttr.WFSta, getWFSta().Etc.getValue());
		}
		else
		{
			SetValByKey(GenerWorkFlowAttr.WFSta, getWFSta().Runing.getValue());
		}

		SetValByKey(GenerWorkFlowAttr.WFState, value.getValue());
	}
	/** 
	 状态(简单)
	*/
	public final WFSta getWFSta()throws Exception
	{
		return WFSta.forValue(this.GetValIntByKey(GenerWorkFlowAttr.WFSta));
	}
	/** 
	 是否可以批处理？
	*/
	public final boolean getIsCanBatch()throws Exception
	{
		return this.GetParaBoolen("IsCanBatch");
	}
	public final void setIsCanBatch(boolean value) throws Exception
	{
		this.SetPara("IsCanBatch", value);
	}
	/** 
	 状态
	 * @throws Exception 
	*/
	public final String getWFStateText() throws Exception
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
	 * @throws Exception 
	*/
	public final String getGUID() throws Exception
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.GUID);
	}
	public final void setGUID(String value) throws Exception
	{
		SetValByKey(GenerWorkFlowAttr.GUID, value);
	}

		///


		///扩展属性
	/** 
	 它的子流程
	*/
	public final GenerWorkFlows getHisSubFlowGenerWorkFlows() throws Exception
	{
		GenerWorkFlows ens = new GenerWorkFlows();
		ens.Retrieve(GenerWorkFlowAttr.PWorkID, this.getWorkID());
		return ens;
	}
	/** 
	 0=待办中,1=预警中,2=逾期中,3=按期完成,4=逾期完成
	*/
	public final int getTodoSta() throws Exception
	{
		return this.GetValIntByKey(GenerWorkFlowAttr.TodoSta);
	}

		/// 扩展属性


		///参数属性.
	/** 
	 是否是流程模版?
	*/
	public final boolean getParasDBTemplate() throws Exception
	{
		return this.GetParaBoolen("DBTemplate");
	}
	public final void setParasDBTemplate(boolean value) throws Exception
	{
		this.SetPara("DBTemplate", value);
	}
	/** 
	 模版名称
	*/
	public final String getParasDBTemplateName() throws Exception
	{
		return this.GetParaString("DBTemplateName");
	}
	public final void setParasDBTemplateName(String value) throws Exception
	{
		this.SetPara("DBTemplateName", value);
	}
	/** 
	 选择的表单(用于子流程列表里，打开草稿，记录当初选择的表单.)
	*/
	public final String getParasFrms() throws Exception
	{
		return this.GetParaString("Frms");
	}
	public final void setParasFrms(String value) throws Exception
	{
		this.SetPara("Frms", value);
	}
	/** 
	 到达的节点
	*/
	public final String getParasToNodes() throws Exception
	{
		return this.GetParaString("ToNodes");
	}

	public final void setParasToNodes(String value) throws Exception
	{
		this.SetPara("ToNodes", value);
	}
	/** 
	 关注&取消关注
	*/
	public final boolean getParasFocus() throws Exception
	{
		return this.GetParaBoolen("F_" + WebUser.getNo(),false);
	}
	public final void setParasFocus(boolean value) throws Exception 
	{
		this.SetPara("F_" + WebUser.getNo(), value);
	}
	/** 
	 确认与取消确认
	*/
	public final boolean getParasConfirm()throws Exception
	{
		return this.GetParaBoolen("C_" + WebUser.getNo(),false);
	}
	public final void setParasConfirm(boolean value) throws Exception
	{
		this.SetPara("C_" + WebUser.getNo(), value);
	}
	/** 
	 最后一个执行发送动作的ID.
	*/
	public final String getParasLastSendTruckID()throws Exception
	{
		String str = this.GetParaString("LastTruckID");
		if (str.equals(""))
		{
			str = String.valueOf(this.getWorkID());
		}
		return str;
	}
	public final void setParasLastSendTruckID(String value)throws Exception
	{
		this.SetPara("LastTruckID", value);
	}

	/** 
	 加签信息
	*/
	public final String getParasAskForReply()throws Exception
	{
		return this.GetParaString("AskForReply");
	}
	public final void setParasAskForReply(String value)throws Exception
	{
		this.SetPara("AskForReply", value);
	}
	/** 
	 是否是退回并原路返回.
	*/

	public final boolean getParasIsTrackBack()throws Exception
	{
		return this.GetParaBoolen("IsTrackBack");
	}
	public final void setParasIsTrackBack(boolean value) throws Exception
	{
		this.SetPara("IsTrackBack", value);
	}
	/** 
	 分组Mark
	*/
	public final String getParasGroupMark()throws Exception
	{
		return this.GetParaString(GenerWorkerListAttr.GroupMark);
	}
	public final void setParasGroupMark(String value)throws Exception
	{
		this.SetPara(GenerWorkerListAttr.GroupMark, value);
	}
	/** 
	 是否是自动运行
	 0=自动运行(默认,无需人工干涉). 1=手工运行(按照手工设置的模式运行,人工干涉模式).
	 用于自由流程中.
	*/
	public final TransferCustomType getTransferCustomType()throws Exception
	{
		return TransferCustomType.forValue(this.GetParaInt("IsAutoRun"));
	}
	public final void setTransferCustomType(TransferCustomType value)throws Exception
	{
		this.SetPara("IsAutoRun", value.getValue());
	}
	/** 
	 多人待办处理模式
	*/
	public final TodolistModel getTodolistModel()throws Exception
	{
		return TodolistModel.forValue(this.GetParaInt("TodolistModel"));
	}
	public final void setTodolistModel(TodolistModel value)throws Exception
	{
		this.SetPara("TodolistModel", value.getValue());
	}
	/** 
	 会签到达人员
	*/
	public final String getHuiQianSendToEmps()throws Exception
	{
		return this.GetParaString("HuiQianSendToEmps");
	}
	public final void setHuiQianSendToEmps(String value)throws Exception
	{
		this.SetPara("HuiQianSendToEmps", value);
	}
	/** 
	 会签到达节点: 101@102
	*/
	public final String getHuiQianSendToNodeIDStr()throws Exception
	{
		return this.GetParaString("HuiQianSendToNodeID");
	}
	public final void setHuiQianSendToNodeIDStr(String value)throws Exception
	{
		this.SetPara("HuiQianSendToNodeID", value);
	}
	/** 
	 会签主持人
	 * @throws Exception 
	*/
	public final String getHuiQianZhuChiRen() throws Exception
	{
		return this.GetParaString("HuiQianZhuChiRen");
	}
	public final void setHuiQianZhuChiRen(String value) throws Exception
	{
		this.SetPara("HuiQianZhuChiRen", value);
	}
	/** 
	 会签主持人名称
	*/
	public final String getHuiQianZhuChiRenName() throws Exception
	{
		return this.GetParaString("HuiQianZhuChiRenName");
	}
	public final void setHuiQianZhuChiRenName(String value) throws Exception
	{
		this.SetPara("HuiQianZhuChiRenName", value);
	}

		/// 参数属性.


		///构造函数
	/** 
	 产生的工作流程
	*/
	public GenerWorkFlow()
	{
	}
	public GenerWorkFlow(long workId) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(GenerWorkFlowAttr.WorkID, workId);
		if (qo.DoQuery() == 0)
		{
			throw new RuntimeException("工作 GenerWorkFlow [" + workId + "]不存在。");
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
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_GenerWorkFlow", "流程实例");

		map.AddTBIntPK(GenerWorkFlowAttr.WorkID, 0, "WorkID", true, true); //主键.
		map.AddTBInt(GenerWorkFlowAttr.FID, 0, "流程ID", true, true);

		map.AddTBString(GenerWorkFlowAttr.FK_FlowSort, null, "流程类别", true, false, 0, 10, 10);

			//等于流程类别的Domain字段值.
		map.AddTBString(GenerWorkFlowAttr.SysType, null, "系统类别", true, false, 0, 10, 10);

		map.AddTBString(GenerWorkFlowAttr.FK_Flow, null, "流程", true, false, 0, 5, 10);
		map.AddTBString(GenerWorkFlowAttr.FlowName, null, "流程名称", true, false, 0, 100, 10);
		map.AddTBString(GenerWorkFlowAttr.Title, null, "标题", true, false, 0, 1000, 10);

			//两个状态，在不同的情况下使用. WFState状态 可以查询到SELECT  * FROM sys_enum WHERE EnumKey='WFState'
			// WFState 的状态  @0=空白@1=草稿@2=运行中@3=已经完成@4=挂起@5=退回.
		map.AddDDLSysEnum(GenerWorkFlowAttr.WFSta, 0, "状态", true, false, GenerWorkFlowAttr.WFSta, "@0=运行中@1=已完成@2=其他");
		map.AddDDLSysEnum(GenerWorkFlowAttr.WFState, 0, "流程状态", true, false, GenerWorkFlowAttr.WFState);


		map.AddTBString(GenerWorkFlowAttr.Starter, null, "发起人", true, false, 0, 200, 10);
		map.AddTBString(GenerWorkFlowAttr.StarterName, null, "发起人名称", true, false, 0, 200, 10);
		map.AddTBString(GenerWorkFlowAttr.Sender, null, "发送人", true, false, 0, 200, 10);


		map.AddTBDateTime(GenerWorkFlowAttr.RDT, "记录日期", true, true);
		map.AddTBDateTime(GenerWorkFlowAttr.SendDT, "流程活动时间", true, true);
		map.AddTBInt(GenerWorkFlowAttr.FK_Node, 0, "节点", true, false);
		map.AddTBString(GenerWorkFlowAttr.NodeName, null, "节点名称", true, false, 0, 100, 10);

		map.AddTBString(GenerWorkFlowAttr.FK_Dept, null, "部门", true, false, 0, 100, 10);
		map.AddTBString(GenerWorkFlowAttr.DeptName, null, "部门名称", true, false, 0, 100, 10);
		map.AddTBInt(GenerWorkFlowAttr.PRI, 1, "优先级", true, true);

		map.AddTBDateTime(GenerWorkFlowAttr.SDTOfNode, "节点应完成时间", true, true);
		map.AddTBDateTime(GenerWorkFlowAttr.SDTOfFlow,null, "流程应完成时间", true, true);
		map.AddTBDateTime(GenerWorkFlowAttr.SDTOfFlowWarning, null,"流程预警时间", true, true);

			//父子流程信息.
		map.AddTBString(GenerWorkFlowAttr.PFlowNo, null, "父流程编号", true, false, 0, 3, 10);
		map.AddTBInt(GenerWorkFlowAttr.PWorkID, 0, "父流程ID", true, true);
		map.AddTBInt(GenerWorkFlowAttr.PNodeID, 0, "父流程调用节点", true, true);
		map.AddTBInt(GenerWorkFlowAttr.PFID, 0, "父流程调用的PFID", true, true);
		map.AddTBString(GenerWorkFlowAttr.PEmp, null, "子流程的调用人", true, false, 0,32, 10);

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

		///


		///重载基类方法
	/** 
	 删除后,需要把工作者列表也要删除.
	 * @throws Exception 
	*/
	@Override
	protected void afterDelete() throws Exception
	{
		switch (SystemConfig.getAppCenterDBType())
		{
			case MSSQL:
			case Oracle:
			case KingBaseR3:
			case KingBaseR6:
				DBAccess.RunSQL("DELETE FROM WF_GenerWorkerlist WHERE WorkID in  ( select WorkID from WF_GenerWorkerlist WHERE WorkID not in (select WorkID from WF_GenerWorkFlow) )");
				break;
			case MySQL:
				DBAccess.RunSQL("DELETE A FROM WF_GenerWorkerlist A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID And B.WorkID Not IN(select WorkID from WF_GenerWorkFlow)");
				break;
			case PostgreSQL:
				DBAccess.RunSQL("DELETE FROM WF_GenerWorkerlist A USING WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID And B.WorkID Not IN(select WorkID from WF_GenerWorkFlow)");
				break;
			default:
				break;

		}

		WorkFlow wf = new WorkFlow(new Flow(this.getFK_Flow()), this.getWorkID(), this.getFID());
		wf.DoDeleteWorkFlowByReal(true); // 删除下面的工作。
		super.afterDelete();
	}

	@Override
	protected boolean beforeInsert() throws Exception
	{
		if (this.getStarter().equals("Guest"))
		{
			this.setStarterName(GuestUser.getName());
			this.setGuestName(this.getStarterName());
			this.setGuestNo(GuestUser.getNo());
		}

		//加入组织no.
		if (Glo.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			this.setOrgNo(WebUser.getOrgNo());
		}

		return super.beforeInsert();
	}

		///


		///执行诊断
	/** 
	 生成父子流程的甘特图
	 
	 @return 
	 * @throws Exception 
	*/
	public final String GenerGantt() throws Exception
	{
		return bp.wf.Glo.GenerGanttDataOfSubFlows(this.getWorkID());
	}
	/** 
	 终止流程
	 
	 @param msg 终止的信息
	 @return 终止结果
	 * @throws Exception 
	*/
	public final String DoFix(String msg) throws Exception
	{
	   return bp.wf.Dev2Interface.Flow_DoFix(this.getWorkID(), true, msg);
	}

	public final String DoRpt() throws Exception
	{
		return "WFRpt.htm?WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow();
	}
	/** 
	 执行修复
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoRepare() throws Exception
	{
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
		if (nd.getIsStartNode())
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
		wls.Retrieve(GenerWorkerListAttr.FK_Node, FK_Node, GenerWorkerListAttr.WorkID, this.getWorkID());
		for (GenerWorkerList wl : wls.ToJavaList())
		{
			str += wl.getFK_Emp() + wl.getFK_EmpText() + ",";
		}

		return "此流程是因为[" + nd.getName() + "]工作发送失败被回滚到当前位置，请转告[" + str + "]流程修复成功。";
	}
	public final String DoSelfTestInfo() throws Exception
	{
		GenerWorkerLists wls = new GenerWorkerLists(this.getWorkID(), this.getFK_Flow());


			/// 查看一下当前的节点是否开始工作节点。
		Node nd = new Node(this.getFK_Node());
		if (nd.getIsStartNode())
		{
			/* 判断是否是退回的节点 */
			Work wk = nd.getHisWork();
			wk.setOID(this.getWorkID());
			wk.Retrieve();
		}

			///



			/// 查看一下是否有当前的工作节点信息。
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

			///

		return "没有发现异常。";
	}

		///
}