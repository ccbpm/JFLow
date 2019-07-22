package BP.WF;

import java.io.IOException;


import BP.DA.DBAccess;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.En.Entity;
import BP.En.Map;
import BP.En.QueryObject;
import BP.En.RefMethod;
import BP.Sys.PubClass;
import BP.Tools.ContextHolderUtils;
import BP.WF.Template.FlowSortAttr;

/** 
 流程实例
 
*/
public class GenerWorkFlow extends Entity
{
	 
	/** 
	 是否是流程模版?
	 
*/
	public final boolean getParas_DBTemplate()
	{
		return this.GetParaBoolen("DBTemplate");
	}
	public final void setParas_DBTemplate(boolean value)
	{
		this.SetPara("DBTemplate", value);
	}
	/** 
	 模版名称
	 
	*/
	public final String getParas_DBTemplateName()
	{
		return this.GetParaString("DBTemplateName");
	}
	public final void setParas_DBTemplateName(String value)
	{
		this.SetPara("DBTemplateName", value);
	}
	/** 
	 主键
	*/
	@Override
	public String getPK()
	{
		return GenerWorkFlowAttr.WorkID;
	}
	/** 
	 备注
	*/
	public final String getFlowNote()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.FlowNote);
	}
	public final void setFlowNote(String value)
	{
		SetValByKey(GenerWorkFlowAttr.FlowNote, value);
	}
	/** 
	 工作流程编号
	*/
	public final String getFK_Flow()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)
	{
		SetValByKey(GenerWorkFlowAttr.FK_Flow,value);
	}
	/** 
	 BillNo
	*/
	public final String getBillNo()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.BillNo);
	}
	public final void setBillNo(String value)
	{
		SetValByKey(GenerWorkFlowAttr.BillNo, value);
	}
	/** 
	 最后的发送人
	*/
	public final String getSender()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.Sender);
	}
	public final void setSender(String value)
	{
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
	{
		SetValByKey(GenerWorkFlowAttr.SendDT, value);
	}
	/** 
	 流程名称
	*/
	public final String getFlowName()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.FlowName);
	}
	public final void setFlowName(String value)
	{
		SetValByKey(GenerWorkFlowAttr.FlowName, value);
	}
	/** 
	 优先级
	*/
	public final int getPRI()
	{
		return this.GetValIntByKey(GenerWorkFlowAttr.PRI);
	}
	public final void setPRI(int value)
	{
		SetValByKey(GenerWorkFlowAttr.PRI, value);
	}
	/** 
	 待办人员数量
	*/
	public final int getTodoEmpsNum()
	{
		return this.GetValIntByKey(GenerWorkFlowAttr.TodoEmpsNum);
	}
	public final void setTodoEmpsNum(int value)
	{
		SetValByKey(GenerWorkFlowAttr.TodoEmpsNum, value);
	}
	/** 
	 待办人员列表
	*/
	public final String getTodoEmps()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.TodoEmps);
	}
	public final void setTodoEmps(String value)
	{
		String val = this.GetValStrByKey(GenerWorkFlowAttr.TodoEmps);
        if (val.contains(value) == true)
            return;
		SetValByKey(GenerWorkFlowAttr.TodoEmps, value);
	}
	public final void setTodoEmpsV2(String value)
	{

		SetValByKey(GenerWorkFlowAttr.TodoEmps, value);
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
		SetValByKey(GenerWorkFlowAttr.Emps, value);
	}
	/** 
	 状态
	*/
	public final TaskSta getTaskSta()
	{
		return TaskSta.forValue(this.GetValIntByKey(GenerWorkFlowAttr.TaskSta));
	}
	public final void setTaskSta(TaskSta value)
	{
		SetValByKey(GenerWorkFlowAttr.TaskSta, value.getValue());
	}
	/** 
	 类别编号
	*/
	public final String getFK_FlowSort()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.FK_FlowSort);
	}
	public final void setFK_FlowSort(String value)
	{
		SetValByKey(GenerWorkFlowAttr.FK_FlowSort, value);
	}
	/** 
	 系统类别
	*/
	public final String getSysType()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.SysType);
	}
	public final void setSysType(String value)
	{
		SetValByKey(GenerWorkFlowAttr.SysType, value);
	}
	/** 
	 发起人部门
	*/
	public final String getFK_Dept()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.FK_Dept);
	}
	public final void setFK_Dept(String value)
	{
		SetValByKey(GenerWorkFlowAttr.FK_Dept,value);
	}
	/** 
	 标题
	*/
	public final String getTitle()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.Title);
	}
	public final void setTitle(String value)
	{
		SetValByKey(GenerWorkFlowAttr.Title,value);
	}
	/** 
	 客户编号
	*/
	public final String getGuestNo()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.GuestNo);
	}
	public final void setGuestNo(String value)
	{
		SetValByKey(GenerWorkFlowAttr.GuestNo, value);
	}
	/** 
	 客户名称
	*/
	public final String getGuestName()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.GuestName);
	}
	public final void setGuestName(String value)
	{
		SetValByKey(GenerWorkFlowAttr.GuestName, value);
	}
	/** 
	 年月
	*/
	public final String getFK_NY()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.FK_NY);
	}
	public final void setFK_NY(String value)
	{
		SetValByKey(GenerWorkFlowAttr.FK_NY, value);
	}
	/** 
	 产生时间
	*/
	public final String getRDT()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.RDT);
	}
	public final void setRDT(String value)
	{
		this.SetValByKey(GenerWorkFlowAttr.RDT, value);
		this.setFK_NY(value.substring(0, 7));
	}
	/** 
	 节点应完成时间
	*/
	public final String getSDTOfNode()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.SDTOfNode);
	}
	public final void setSDTOfNode(String value)
	{
		SetValByKey(GenerWorkFlowAttr.SDTOfNode, value);
	}
	/** 
	 流程应完成时间
	*/
	public final String getSDTOfFlow()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.SDTOfFlow);
	}
	public final void setSDTOfFlow(String value)
	{
		SetValByKey(GenerWorkFlowAttr.SDTOfFlow, value);
	}
	/** 
	 流程ID
	*/
	public final long getWorkID()
	{
		return this.GetValInt64ByKey(GenerWorkFlowAttr.WorkID);
	}
	public final void setWorkID(long value)
	{
		SetValByKey(GenerWorkFlowAttr.WorkID,value);
	}
	/** 
	 主线程ID
	*/
	public final long getFID()
	{
		return this.GetValInt64ByKey(GenerWorkFlowAttr.FID);
	}
	public final void setFID(long value)
	{
		SetValByKey(GenerWorkFlowAttr.FID, value);
	}
	/** 
	 父节点流程编号.
	*/
	public final long getPWorkID()
	{
		return this.GetValInt64ByKey(GenerWorkFlowAttr.PWorkID);
	}
	public final void setPWorkID(long value)
	{
		SetValByKey(GenerWorkFlowAttr.PWorkID, value);
	}
	public final long getPFID()
	{
		return this.GetValInt64ByKey(GenerWorkFlowAttr.PFID);
	}
	public final void setPFID(long value)
	{
		SetValByKey(GenerWorkFlowAttr.PFID, value);
	}
	/** 
	 父流程调用的节点
	*/
	public final int getPNodeID()
	{
		return this.GetValIntByKey(GenerWorkFlowAttr.PNodeID);
	}
	public final void setPNodeID(int value)
	{
		SetValByKey(GenerWorkFlowAttr.PNodeID, value);
	}
	/** 
	 PFlowNo
	*/
	public final String getPFlowNo()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.PFlowNo);
	}
	public final void setPFlowNo(String value)
	{
		SetValByKey(GenerWorkFlowAttr.PFlowNo, value);
	}
	/** 
	 吊起子流程的人员
	*/
	public final String getPEmp()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.PEmp);
	}
	public final void setPEmp(String value)
	{
		SetValByKey(GenerWorkFlowAttr.PEmp, value);
	}
	/** 
	 发起人
	*/
	public final String getStarter()
	{
		return this.GetValStrByKey(GenerWorkFlowAttr.Starter);
	}
	public final void setStarter(String value)
	{
		SetValByKey(GenerWorkFlowAttr.Starter, value);
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
	{
		SetValByKey(GenerWorkFlowAttr.FK_Node, value);
	}
	/** 
	 工作流程状态
	*/
	public final WFState getWFState()
	{
		return WFState.forValue(this.GetValIntByKey(GenerWorkFlowAttr.WFState));
	}
	public final void setWFState(WFState value)
	{
		if (value == BP.WF.WFState.Complete)
		{
			SetValByKey(GenerWorkFlowAttr.WFSta, getWFSta().Complete.getValue());
		}
		else if (value == BP.WF.WFState.Delete)
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
	public final WFSta getWFSta()
	{
		return WFSta.forValue(this.GetValIntByKey(GenerWorkFlowAttr.WFSta));
	}
	public final void setWFSta(WFSta value)
	{
		SetValByKey(GenerWorkFlowAttr.WFSta, value.getValue());
	}
	/** 
	 状态
	*/
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
			case Draft:
				return "草稿";
			case ReturnSta:
				return "退回";
			default:
				return "其他"+ws.toString();
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
	{
		SetValByKey(GenerWorkFlowAttr.GUID, value);
	}

		///#endregion


		
	/** 
	 它的子流程
	 * @throws Exception 
	 
	*/
	public final GenerWorkFlows getHisSubFlowGenerWorkFlows() throws Exception
	{
		GenerWorkFlows ens = new GenerWorkFlows();
		ens.Retrieve(GenerWorkFlowAttr.PWorkID, this.getWorkID());
		return ens;
	}

		///#endregion 扩展属性


		
	/** 
	 选择的表单(用于子流程列表里，打开草稿，记录当初选择的表单.)
	 
	*/
	public final String getParas_Frms()
	{
		return this.GetParaString("Frms");
	}
	public final void setParas_Frms(String value)
	{
		this.SetPara("Frms", value);
	}
	/** 
	 到达的节点
	 
	*/
	public final String getParas_ToNodes()
	{
		return this.GetParaString("ToNodes");
	}

	public final void setParas_ToNodes(String value)
	{
		this.SetPara("ToNodes", value);
	}
	/** 
	 关注&取消关注
	 * @throws Exception 
	 
	*/
	public final boolean getParas_Focus() throws Exception
	{
		return this.GetParaBoolen("F_"+BP.Web.WebUser.getNo(),false);
	}
	public final void setParas_Focus(boolean value) throws Exception
	{
		this.SetPara("F_" + BP.Web.WebUser.getNo(), value);
	}
	/** 
	 最后一个执行发送动作的ID.
	 
	*/
	public final String getParas_LastSendTruckID()
	{
		String str= this.GetParaString("LastTruckID");
		if (str.equals(""))
		{
			str = (new Long(this.getWorkID())).toString();
		}
		return str;
	}
	public final void setParas_LastSendTruckID(String value)
	{
		this.SetPara("LastTruckID", value);
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
	/** 
	 是否是退回并原路返回.
	 
	*/

	public final boolean getParas_IsTrackBack()
	{
		return this.GetParaBoolen("IsTrackBack");
	}
	public final void setParas_IsTrackBack(boolean value)
	{
		this.SetPara("IsTrackBack", value);
	}
	/** 
	 分组Mark
	*/
	public final String getParas_GroupMark()
	{
		return this.GetParaString(GenerWorkerListAttr.GroupMark);
	}
	public final void setParas_GroupMark(String value)
	{
		this.SetPara(GenerWorkerListAttr.GroupMark, value);
	}
	/** 
	 是否是自动运行
	 0=自动运行(默认,无需人工干涉). 1=手工运行(按照手工设置的模式运行,人工干涉模式).
	 用于自由流程中.
	*/
	public final TransferCustomType getTransferCustomType()
	{
		return TransferCustomType.forValue(this.GetParaInt("IsAutoRun"));
	}
	public final void setTransferCustomType(TransferCustomType value)
	{
		this.SetPara("IsAutoRun", value.getValue());
	}
	/** 
	 多人待办处理模式
	*/
	public final TodolistModel getTodolistModel()
	{
		return TodolistModel.forValue(this.GetParaInt("TodolistModel"));
	}
	public final void setTodolistModel(TodolistModel value)
	{
		this.SetPara("TodolistModel", value.getValue());
	}
	
	/** 
	 会签到达人员
	 
*/
	public final String getHuiQianSendToEmps()
	{
		return this.GetParaString("HuiQianSendToEmps");
	}
	public final void setHuiQianSendToEmps(String value)
	{
		this.SetPara("HuiQianSendToEmps", value);
	}
	/** 
	 会签到达节点: 101@102
	 
	*/
	public final String getHuiQianSendToNodeIDStr()
	{
		return this.GetParaString("HuiQianSendToNodeID");
	}
	
	/** 
	 会签主持人
	 
	*/
	public final String getHuiQianZhuChiRen()
	{
		return this.GetParaString("HuiQianZhuChiRen");
	}
	public final void setHuiQianZhuChiRen(String value)
	{
		this.SetPara("HuiQianZhuChiRen", value);
	}
	
	public void setHuiQianZhuChiRenName(String name) {
		
		this.SetPara("HuiQianZhuChiRenName", name);
		
	}
	public final String getHuiQianZhuChiRenName()
	{
		return this.GetParaString("HuiQianZhuChiRenName");
	}
	 
	
    public void setHuiQianSendToNodeIDStr(String name) {
		
		this.SetPara("HuiQianSendToNodeID", name);
		
	}
	   
	
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
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_GenerWorkFlow", "流程实例");

		map.AddTBIntPK(GenerWorkFlowAttr.WorkID, 0, "WorkID", true, true);
		map.AddTBInt(GenerWorkFlowAttr.FID, 0, "流程ID", true, true);

		map.AddTBString(GenerWorkFlowAttr.FK_FlowSort, null, "流程类别", true, false, 0, 10, 10);
		map.AddTBString(GenerWorkFlowAttr.SysType, null, "系统类别", true, false, 0, 10, 10);
		map.AddTBString(GenerWorkFlowAttr.FK_Flow, null, "流程", true, false, 0, 3, 10);
		map.AddTBString(GenerWorkFlowAttr.FlowName, null, "流程名称", true, false, 0, 100, 10);

		map.AddTBString(GenerWorkFlowAttr.Title, null, "标题", true, false, 0, 1000, 10);

		map.AddDDLSysEnum(GenerWorkFlowAttr.WFSta, 0, "状态", true, false, GenerWorkFlowAttr.WFSta, "@0=运行中@1=已完成@2=其他");
		map.AddDDLSysEnum(GenerWorkFlowAttr.WFState, 0, "流程状态", true, false, GenerWorkFlowAttr.WFState);

		map.AddTBString(GenerWorkFlowAttr.Starter, null, "发起人", true, false, 0, 200, 10);
		map.AddTBString(GenerWorkFlowAttr.StarterName, null, "发起人名称", true, false, 0, 200, 10);
		map.AddTBString(GenerWorkFlowAttr.Sender, null, "发送人", true, false, 0, 200, 10);


		map.AddTBDateTime(GenerWorkFlowAttr.RDT, "记录日期", true, true);
		map.AddTBDateTime(GenerWorkFlowAttr.SendDT, "记录日期", true, true);
		map.AddTBInt(GenerWorkFlowAttr.FK_Node, 0, "节点", true, false);
		map.AddTBString(GenerWorkFlowAttr.NodeName, null, "节点名称", true, false, 0, 100, 10);

		map.AddTBString(GenerWorkFlowAttr.FK_Dept, null, "部门", true, false, 0, 100, 10);
		map.AddTBString(GenerWorkFlowAttr.DeptName, null, "部门名称", true, false, 0, 100, 10);
		map.AddTBInt(GenerWorkFlowAttr.PRI, 1, "优先级", true, true);

		map.AddTBDateTime(GenerWorkFlowAttr.SDTOfNode, "节点应完成时间", true, true);
		map.AddTBDateTime(GenerWorkFlowAttr.SDTOfFlow, "流程应完成时间", true, true);

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
		map.AddTBString(GenerWorkFlowAttr.Emps, null, "参与人", true, false, 0, 4000, 10);
		map.AddTBString(GenerWorkFlowAttr.GUID, null, "GUID", false, false, 0, 36, 10);
		map.AddTBString(GenerWorkFlowAttr.FK_NY, null, "年月", false, false, 0, 7, 7);
		map.AddTBInt(GenerWorkFlowAttr.WeekNum, 0, "周次", true, true);
		map.AddTBInt(GenerWorkFlowAttr.TSpan, 0, "时间间隔", true, true);
		
		map.AddTBString(GenerWorkFlowAttr.Domain, null, "域/系统编号", true, false, 0, 100, 30);
        map.SetHelperAlert(GenerWorkFlowAttr.Domain, "用于区分不同系统的流程,比如:一个集团有多个子系统每个子系统都有自己的流程,就需要标记那些流程是那个子系统的.");

			//待办状态(0=待办中,1=预警中,2=逾期中,3=按期完成,4=逾期完成) 
		map.AddTBInt(GenerWorkFlowAttr.TodoSta, 0, "待办状态", true, true);

		map.AddTBMyNum();


		RefMethod rm = new RefMethod();
		rm.Title = "工作轨迹"; // "工作报告";
		rm.ClassMethodName = this.toString() + ".DoRpt";
		rm.Icon = Glo.getCCFlowAppPath() + "WF/Img/FileType/doc.gif";
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
	/** 
	 删除后,需要把工作者列表也要删除.
	 * @throws Exception 
	*/
	@Override
	protected void afterDelete() throws Exception
	{
		// . clear bad worker .  
		DBAccess.RunSQLReturnTable("DELETE FROM WF_GenerWorkerlist WHERE WorkID in  ( select WorkID from WF_GenerWorkerlist WHERE WorkID not in (select WorkID from WF_GenerWorkFlow) )");

		WorkFlow wf = new WorkFlow(new Flow(this.getFK_Flow()), this.getWorkID(), this.getFID());
		wf.DoDeleteWorkFlowByReal(true); // 删除下面的工作。
		super.afterDelete();
	}
	@Override
	protected boolean beforeInsert() throws Exception
	{
		if (this.getStarter().equals("Guest"))
		{
			this.setStarterName(BP.Web.GuestUser.getName());
			this.setGuestName(this.getStarterName());
			this.setGuestNo(BP.Web.GuestUser.getNo());
		}

		return super.beforeInsert();
	}
	public final String DoRpt() throws IOException
	{
		PubClass.WinOpen(ContextHolderUtils.getResponse(),"WFRpt.jsp?WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow());
		return null;
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
			//如果是开始工作节点，就删除它。
			WorkFlow wf = new WorkFlow(new Flow(this.getFK_Flow()), this.getWorkID(), this.getFID());
			wf.DoDeleteWorkFlowByReal(true);
			return "此流程是因为发起工作失败被系统删除。";
		}

		int FK_Node = Integer.parseInt(dt.Rows.get(0).getValue(0).toString());

		Node nd = new Node(FK_Node);
		if (nd.getIsStartNode())
		{
			//如果是开始工作节点，就删除它。
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


			///#region  查看一下当前的节点是否开始工作节点。
		Node nd = new Node(this.getFK_Node());
		if (nd.getIsStartNode())
		{
			// 判断是否是退回的节点 
			Work wk = nd.getHisWork();
			wk.setOID(this.getWorkID());
			wk.Retrieve();
		}

			///#endregion



			///#region  查看一下是否有当前的工作节点信息。
		boolean isHave=false;
		for (GenerWorkerList wl : wls.ToJavaList())
		{
			if (wl.getFK_Node()==this.getFK_Node())
			{
				isHave=true;
			}
		}

		if (isHave==false)
		{
			//  
			return "已经不存在当前的工作节点信息，造成此流程的原因可能是没有捕获的系统异常，建议删除此流程或者交给系统自动修复它。";
		}
		return "没有发现异常。";
	}
	/** 
	 确认与取消确认
	 * @throws Exception 
	 
	 */
	public final boolean getParas_Confirm() throws Exception
	{
		return this.GetParaBoolen("C_"+BP.Web.WebUser.getNo(),false);
	}
	public final void setParas_Confirm(boolean value) throws Exception
	{
		this.SetPara("C_" + BP.Web.WebUser.getNo(), value);
	}
	public final HuiQianTaskSta getHuiQianTaskSta()
	{
		return HuiQianTaskSta.forValue(this.GetParaInt(GenerWorkFlowAttr.HuiQianTaskSta));
	}
	public final void setHuiQianTaskSta(HuiQianTaskSta value)
	{
		this.SetPara(GenerWorkFlowAttr.HuiQianTaskSta, value.getValue());
	}
	
}