package BP.WF.Data;

import BP.DA.*;
import BP.WF.*;
import BP.Port.*;
import BP.Sys.*;
import BP.En.*;
import BP.WF.*;
import java.util.*;

/** 
 我部门的流程
*/
public class MyDeptFlow extends Entity
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
		return MyDeptFlowAttr.WorkID;
	}
	/** 
	 备注
	*/
	public final String getFlowNote()
	{
		return this.GetValStrByKey(MyDeptFlowAttr.FlowNote);
	}
	public final void setFlowNote(String value)
	{
		SetValByKey(MyDeptFlowAttr.FlowNote, value);
	}
	/** 
	 工作流程编号
	*/
	public final String getFK_Flow()
	{
		return this.GetValStrByKey(MyDeptFlowAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)
	{
		SetValByKey(MyDeptFlowAttr.FK_Flow,value);
	}
	/** 
	 BillNo
	*/
	public final String getBillNo()
	{
		return this.GetValStrByKey(MyDeptFlowAttr.BillNo);
	}
	public final void setBillNo(String value)
	{
		SetValByKey(MyDeptFlowAttr.BillNo, value);
	}
	/** 
	 流程名称
	*/
	public final String getFlowName()
	{
		return this.GetValStrByKey(MyDeptFlowAttr.FlowName);
	}
	public final void setFlowName(String value)
	{
		SetValByKey(MyDeptFlowAttr.FlowName, value);
	}
	/** 
	 优先级
	*/
	public final int getPRI()
	{
		return this.GetValIntByKey(MyDeptFlowAttr.PRI);
	}
	public final void setPRI(int value)
	{
		SetValByKey(MyDeptFlowAttr.PRI, value);
	}
	/** 
	 待办人员数量
	*/
	public final int getTodoEmpsNum()
	{
		return this.GetValIntByKey(MyDeptFlowAttr.TodoEmpsNum);
	}
	public final void setTodoEmpsNum(int value)
	{
		SetValByKey(MyDeptFlowAttr.TodoEmpsNum, value);
	}
	/** 
	 待办人员列表
	*/
	public final String getTodoEmps()
	{
		return this.GetValStrByKey(MyDeptFlowAttr.TodoEmps);
	}
	public final void setTodoEmps(String value)
	{
		SetValByKey(MyDeptFlowAttr.TodoEmps, value);
	}
	/** 
	 参与人
	*/
	public final String getEmps()
	{
		return this.GetValStrByKey(MyDeptFlowAttr.Emps);
	}
	public final void setEmps(String value)
	{
		SetValByKey(MyDeptFlowAttr.Emps, value);
	}
	/** 
	 状态
	*/
	public final TaskSta getTaskSta()
	{
		return TaskSta.forValue(this.GetValIntByKey(MyDeptFlowAttr.TaskSta));
	}
	public final void setTaskSta(TaskSta value)
	{
		SetValByKey(MyDeptFlowAttr.TaskSta, value.getValue());
	}
	/** 
	 类别编号
	*/
	public final String getFK_FlowSort()
	{
		return this.GetValStrByKey(MyDeptFlowAttr.FK_FlowSort);
	}
	public final void setFK_FlowSort(String value)
	{
		SetValByKey(MyDeptFlowAttr.FK_FlowSort, value);
	}
	/** 
	 部门编号
	*/
	public final String getFK_Dept()
	{
		return this.GetValStrByKey(MyDeptFlowAttr.FK_Dept);
	}
	public final void setFK_Dept(String value)
	{
		SetValByKey(MyDeptFlowAttr.FK_Dept,value);
	}
	/** 
	 标题
	*/
	public final String getTitle()
	{
		return this.GetValStrByKey(MyDeptFlowAttr.Title);
	}
	public final void setTitle(String value)
	{
		SetValByKey(MyDeptFlowAttr.Title,value);
	}
	/** 
	 客户编号
	*/
	public final String getGuestNo()
	{
		return this.GetValStrByKey(MyDeptFlowAttr.GuestNo);
	}
	public final void setGuestNo(String value)
	{
		SetValByKey(MyDeptFlowAttr.GuestNo, value);
	}
	/** 
	 客户名称
	*/
	public final String getGuestName()
	{
		return this.GetValStrByKey(MyDeptFlowAttr.GuestName);
	}
	public final void setGuestName(String value)
	{
		SetValByKey(MyDeptFlowAttr.GuestName, value);
	}
	/** 
	 产生时间
	*/
	public final String getRDT()
	{
		return this.GetValStrByKey(MyDeptFlowAttr.RDT);
	}
	public final void setRDT(String value)
	{
		SetValByKey(MyDeptFlowAttr.RDT,value);
	}
	/** 
	 节点应完成时间
	*/
	public final String getSDTOfNode()
	{
		return this.GetValStrByKey(MyDeptFlowAttr.SDTOfNode);
	}
	public final void setSDTOfNode(String value)
	{
		SetValByKey(MyDeptFlowAttr.SDTOfNode, value);
	}
	/** 
	 流程应完成时间
	*/
	public final String getSDTOfFlow()
	{
		return this.GetValStrByKey(MyDeptFlowAttr.SDTOfFlow);
	}
	public final void setSDTOfFlow(String value)
	{
		SetValByKey(MyDeptFlowAttr.SDTOfFlow, value);
	}
	/** 
	 流程ID
	*/
	public final long getWorkID()
	{
		return this.GetValInt64ByKey(MyDeptFlowAttr.WorkID);
	}
	public final void setWorkID(long value)
	{
		SetValByKey(MyDeptFlowAttr.WorkID,value);
	}
	/** 
	 主线程ID
	*/
	public final long getFID()
	{
		return this.GetValInt64ByKey(MyDeptFlowAttr.FID);
	}
	public final void setFID(long value)
	{
		SetValByKey(MyDeptFlowAttr.FID, value);
	}
	/** 
	 父节点流程编号.
	*/
	public final long getPWorkID()
	{
		return this.GetValInt64ByKey(MyDeptFlowAttr.PWorkID);
	}
	public final void setPWorkID(long value)
	{
		SetValByKey(MyDeptFlowAttr.PWorkID, value);
	}
	/** 
	 父流程调用的节点
	*/
	public final int getPNodeID()
	{
		return this.GetValIntByKey(MyDeptFlowAttr.PNodeID);
	}
	public final void setPNodeID(int value)
	{
		SetValByKey(MyDeptFlowAttr.PNodeID, value);
	}
	/** 
	 PFlowNo
	*/
	public final String getPFlowNo()
	{
		return this.GetValStrByKey(MyDeptFlowAttr.PFlowNo);
	}
	public final void setPFlowNo(String value)
	{
		SetValByKey(MyDeptFlowAttr.PFlowNo, value);
	}
	/** 
	 吊起子流程的人员
	*/
	public final String getPEmp()
	{
		return this.GetValStrByKey(MyDeptFlowAttr.PEmp);
	}
	public final void setPEmp(String value)
	{
		SetValByKey(MyDeptFlowAttr.PEmp, value);
	}
	/** 
	 发起人
	*/
	public final String getStarter()
	{
		return this.GetValStrByKey(MyDeptFlowAttr.Starter);
	}
	public final void setStarter(String value)
	{
		SetValByKey(MyDeptFlowAttr.Starter, value);
	}
	/** 
	 发起人名称
	*/
	public final String getStarterName()
	{
		return this.GetValStrByKey(MyDeptFlowAttr.StarterName);
	}
	public final void setStarterName(String value)
	{
		this.SetValByKey(MyDeptFlowAttr.StarterName, value);
	}
	/** 
	 发起人部门名称
	*/
	public final String getDeptName()
	{
		return this.GetValStrByKey(MyDeptFlowAttr.DeptName);
	}
	public final void setDeptName(String value)
	{
		this.SetValByKey(MyDeptFlowAttr.DeptName, value);
	}
	/** 
	 当前节点名称
	*/
	public final String getNodeName()
	{
		return this.GetValStrByKey(MyDeptFlowAttr.NodeName);
	}
	public final void setNodeName(String value)
	{
		this.SetValByKey(MyDeptFlowAttr.NodeName, value);
	}
	/** 
	 当前工作到的节点
	*/
	public final int getFK_Node()
	{
		return this.GetValIntByKey(MyDeptFlowAttr.FK_Node);
	}
	public final void setFK_Node(int value)
	{
		SetValByKey(MyDeptFlowAttr.FK_Node, value);
	}
	/** 
	 工作流程状态
	*/
	public final WFState getWFState()
	{
		return WFState.forValue(this.GetValIntByKey(MyDeptFlowAttr.WFState));
	}
	public final void setWFState(WFState value)
	{
		if (value == WF.WFState.Complete)
		{
			SetValByKey(MyDeptFlowAttr.WFSta, getWFSta().Complete.getValue());
		}
		else if (value == WF.WFState.Delete)
		{
			SetValByKey(MyDeptFlowAttr.WFSta, getWFSta().Etc.getValue());
		}
		else
		{
			SetValByKey(MyDeptFlowAttr.WFSta, getWFSta().Runing.getValue());
		}

		SetValByKey(MyDeptFlowAttr.WFState, value.getValue());
	}
	/** 
	 状态(简单)
	*/
	public final WFSta getWFSta()
	{
		return WFSta.forValue(this.GetValIntByKey(MyDeptFlowAttr.WFSta));
	}
	public final void setWFSta(WFSta value)
	{
		SetValByKey(MyDeptFlowAttr.WFSta, value.getValue());
	}
	public final String getWFStateText()
	{
		BP.WF.WFState ws = WFState.forValue(this.getWFState());
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
	public final String getGUID()
	{
		return this.GetValStrByKey(MyDeptFlowAttr.GUID);
	}
	public final void setGUID(String value)
	{
		SetValByKey(MyDeptFlowAttr.GUID, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 参数属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
	/** 
	 产生的工作流程
	*/
	public MyDeptFlow()
	{
	}
	public MyDeptFlow(long workId)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(MyDeptFlowAttr.WorkID, workId);
		if (qo.DoQuery() == 0)
		{
			throw new RuntimeException("工作 MyDeptFlow [" + workId + "]不存在。");
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
		if (this._enMap != null)
		{
			return this._enMap;
		}

		Map map = new Map("WF_GenerWorkFlow", "我部门的流程");
		map.Java_SetEnType(EnType.View);

		map.AddTBString(MyDeptFlowAttr.Title, null, "标题", true, false, 0, 100, 150, true);
		map.AddDDLEntities(MyDeptFlowAttr.FK_Flow, null, "流程", new Flows(), false);
		map.AddTBString(MyDeptFlowAttr.BillNo, null, "单据编号", true, false, 0, 100, 50);

		map.AddTBString(MyDeptFlowAttr.StarterName, null, "发起人", true, false, 0, 30, 40);
		map.AddTBDateTime(MyDeptFlowAttr.RDT, "发起日期", true, true);

		map.AddTBString(MyDeptFlowAttr.NodeName, null, "当前节点", true, false, 0, 100, 80);
		map.AddTBString(MyDeptFlowAttr.TodoEmps, null, "当前处理人", true, false, 0, 100, 80);

		map.AddDDLSysEnum(MyDeptFlowAttr.WFSta, 0, "状态", true, false, MyDeptFlowAttr.WFSta);
		map.AddDDLSysEnum(MyFlowAttr.TSpan, 0, "时间段", true, false, MyFlowAttr.TSpan, "@0=本周@1=上周@2=两周以前@3=三周以前@4=更早");

		map.AddTBStringDoc(MyDeptFlowAttr.FlowNote, null, "备注", true, false,true);
		map.AddTBMyNum();

			//工作ID
		map.AddTBIntPK(MyDeptFlowAttr.WorkID, 0, "工作ID", true, true);

			//隐藏字段.
		map.AddTBInt(MyDeptFlowAttr.FID, 0, "FID", false, false);
		map.AddTBString(MyDeptFlowAttr.FK_Dept, null, "部门", false, false, 0, 30, 10);


		map.AddSearchAttr(MyDeptFlowAttr.FK_Flow);
		map.AddSearchAttr(MyDeptFlowAttr.WFSta);
		map.AddSearchAttr(MyDeptFlowAttr.TSpan);
		map.AddHidden(MyStartFlowAttr.FID, "=", "0");


			//增加隐藏的查询条件.
		AttrOfSearch search = new AttrOfSearch(MyDeptFlowAttr.FK_Dept, "部门", MyDeptFlowAttr.FK_Dept, "=", BP.Web.WebUser.FK_Dept, 0, true);

		map.AttrsOfSearch.Add(search);

		RefMethod rm = new RefMethod();
		rm.Title = "流程轨迹";
		rm.ClassMethodName = this.toString() + ".DoTrack";
		rm.Icon = "../../WF/Img/FileType/doc.gif";
		map.AddRefMethod(rm);

		this._enMap = map;
		return this._enMap;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 执行诊断
	public final String DoTrack()
	{
		return "../../WFRpt.htm?WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getFK_Node();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}