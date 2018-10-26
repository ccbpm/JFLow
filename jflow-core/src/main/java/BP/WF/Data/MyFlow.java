package BP.WF.Data;

import java.io.IOException;

import BP.DA.Log;
import BP.En.AttrOfSearch;
import BP.En.EnType;
import BP.En.Entity;
import BP.En.Map;
import BP.En.QueryObject;
import BP.En.RefMethod;
import BP.En.UAC;
import BP.Sys.PubClass;
import BP.Sys.SystemConfig;
import BP.WF.Flows;
import BP.WF.Glo;
import BP.WF.TaskSta;
import BP.WF.WFSta;
import BP.WF.WFState;
import BP.Tools.ContextHolderUtils;


/** 
 我参与的流程
 
*/
public class MyFlow extends Entity
{

		
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
		return MyFlowAttr.WorkID;
	}
	/** 
	 备注
	 
	*/
	public final String getFlowNote()
	{
		return this.GetValStrByKey(MyFlowAttr.FlowNote);
	}
	public final void setFlowNote(String value)
	{
		SetValByKey(MyFlowAttr.FlowNote, value);
	}
	/** 
	 工作流程编号
	 
	*/
	public final String getFK_Flow()
	{
		return this.GetValStrByKey(MyFlowAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)
	{
		SetValByKey(MyFlowAttr.FK_Flow,value);
	}
	/** 
	 BillNo
	 
	*/
	public final String getBillNo()
	{
		return this.GetValStrByKey(MyFlowAttr.BillNo);
	}
	public final void setBillNo(String value)
	{
		SetValByKey(MyFlowAttr.BillNo, value);
	}
	/** 
	 流程名称
	 
	*/
	public final String getFlowName()
	{
		return this.GetValStrByKey(MyFlowAttr.FlowName);
	}
	public final void setFlowName(String value)
	{
		SetValByKey(MyFlowAttr.FlowName, value);
	}
	/** 
	 优先级
	 
	*/
	public final int getPRI()
	{
		return this.GetValIntByKey(MyFlowAttr.PRI);
	}
	public final void setPRI(int value)
	{
		SetValByKey(MyFlowAttr.PRI, value);
	}
	/** 
	 待办人员数量
	 
	*/
	public final int getTodoEmpsNum()
	{
		return this.GetValIntByKey(MyFlowAttr.TodoEmpsNum);
	}
	public final void setTodoEmpsNum(int value)
	{
		SetValByKey(MyFlowAttr.TodoEmpsNum, value);
	}
	/** 
	 待办人员列表
	 
	*/
	public final String getTodoEmps()
	{
		return this.GetValStrByKey(MyFlowAttr.TodoEmps);
	}
	public final void setTodoEmps(String value)
	{
		SetValByKey(MyFlowAttr.TodoEmps, value);
	}
	/** 
	 参与人
	 
	*/
	public final String getEmps()
	{
		return this.GetValStrByKey(MyFlowAttr.Emps);
	}
	public final void setEmps(String value)
	{
		SetValByKey(MyFlowAttr.Emps, value);
	}
	/** 
	 状态
	 
	*/
	public final TaskSta getTaskSta()
	{
		return TaskSta.forValue(this.GetValIntByKey(MyFlowAttr.TaskSta));
	}
	public final void setTaskSta(TaskSta value)
	{
		SetValByKey(MyFlowAttr.TaskSta, value.getValue());
	}
	/** 
	 类别编号
	 
	*/
	public final String getFK_FlowSort()
	{
		return this.GetValStrByKey(MyFlowAttr.FK_FlowSort);
	}
	public final void setFK_FlowSort(String value)
	{
		SetValByKey(MyFlowAttr.FK_FlowSort, value);
	}
	/** 
	 部门编号
	 
	*/
	public final String getFK_Dept()
	{
		return this.GetValStrByKey(MyFlowAttr.FK_Dept);
	}
	public final void setFK_Dept(String value)
	{
		SetValByKey(MyFlowAttr.FK_Dept,value);
	}
	/** 
	 标题
	 
	*/
	public final String getTitle()
	{
		return this.GetValStrByKey(MyFlowAttr.Title);
	}
	public final void setTitle(String value)
	{
		SetValByKey(MyFlowAttr.Title,value);
	}
	/** 
	 客户编号
	 
	*/
	public final String getGuestNo()
	{
		return this.GetValStrByKey(MyFlowAttr.GuestNo);
	}
	public final void setGuestNo(String value)
	{
		SetValByKey(MyFlowAttr.GuestNo, value);
	}
	/** 
	 客户名称
	 
	*/
	public final String getGuestName()
	{
		return this.GetValStrByKey(MyFlowAttr.GuestName);
	}
	public final void setGuestName(String value)
	{
		SetValByKey(MyFlowAttr.GuestName, value);
	}
	/** 
	 产生时间
	 
	*/
	public final String getRDT()
	{
		return this.GetValStrByKey(MyFlowAttr.RDT);
	}
	public final void setRDT(String value)
	{
		SetValByKey(MyFlowAttr.RDT,value);
	}
	/** 
	 节点应完成时间
	 
	*/
	public final String getSDTOfNode()
	{
		return this.GetValStrByKey(MyFlowAttr.SDTOfNode);
	}
	public final void setSDTOfNode(String value)
	{
		SetValByKey(MyFlowAttr.SDTOfNode, value);
	}
	/** 
	 流程应完成时间
	 
	*/
	public final String getSDTOfFlow()
	{
		return this.GetValStrByKey(MyFlowAttr.SDTOfFlow);
	}
	public final void setSDTOfFlow(String value)
	{
		SetValByKey(MyFlowAttr.SDTOfFlow, value);
	}
	/** 
	 流程ID
	 
	*/
	public final long getWorkID()
	{
		return this.GetValInt64ByKey(MyFlowAttr.WorkID);
	}
	public final void setWorkID(long value)
	{
		SetValByKey(MyFlowAttr.WorkID,value);
	}
	/** 
	 主线程ID
	 
	*/
	public final long getFID()
	{
		return this.GetValInt64ByKey(MyFlowAttr.FID);
	}
	public final void setFID(long value)
	{
		SetValByKey(MyFlowAttr.FID, value);
	}
	/** 
	 父节点流程编号.
	 
	*/
	public final long getPWorkID()
	{
		return this.GetValInt64ByKey(MyFlowAttr.PWorkID);
	}
	public final void setPWorkID(long value)
	{
		SetValByKey(MyFlowAttr.PWorkID, value);
	}
	/** 
	 父流程调用的节点
	 
	*/
	public final int getPNodeID()
	{
		return this.GetValIntByKey(MyFlowAttr.PNodeID);
	}
	public final void setPNodeID(int value)
	{
		SetValByKey(MyFlowAttr.PNodeID, value);
	}
	/** 
	 PFlowNo
	 
	*/
	public final String getPFlowNo()
	{
		return this.GetValStrByKey(MyFlowAttr.PFlowNo);
	}
	public final void setPFlowNo(String value)
	{
		SetValByKey(MyFlowAttr.PFlowNo, value);
	}
	/** 
	 吊起子流程的人员
	 
	*/
	public final String getPEmp()
	{
		return this.GetValStrByKey(MyFlowAttr.PEmp);
	}
	public final void setPEmp(String value)
	{
		SetValByKey(MyFlowAttr.PEmp, value);
	}
	/** 
	 发起人
	 
	*/
	public final String getStarter()
	{
		return this.GetValStrByKey(MyFlowAttr.Starter);
	}
	public final void setStarter(String value)
	{
		SetValByKey(MyFlowAttr.Starter, value);
	}
	/** 
	 发起人名称
	 
	*/
	public final String getStarterName()
	{
		return this.GetValStrByKey(MyFlowAttr.StarterName);
	}
	public final void setStarterName(String value)
	{
		this.SetValByKey(MyFlowAttr.StarterName, value);
	}
	/** 
	 发起人部门名称
	 
	*/
	public final String getDeptName()
	{
		return this.GetValStrByKey(MyFlowAttr.DeptName);
	}
	public final void setDeptName(String value)
	{
		this.SetValByKey(MyFlowAttr.DeptName, value);
	}
	/** 
	 当前节点名称
	 
	*/
	public final String getNodeName()
	{
		return this.GetValStrByKey(MyFlowAttr.NodeName);
	}
	public final void setNodeName(String value)
	{
		this.SetValByKey(MyFlowAttr.NodeName, value);
	}
	/** 
	 当前工作到的节点
	 
	*/
	public final int getFK_Node()
	{
		return this.GetValIntByKey(MyFlowAttr.FK_Node);
	}
	public final void setFK_Node(int value)
	{
		SetValByKey(MyFlowAttr.FK_Node, value);
	}
	/** 
	 工作流程状态
	 
	*/
	public final WFState getWFState()
	{
		return WFState.forValue(this.GetValIntByKey(MyFlowAttr.WFState));
	}
	public final void setWFState(WFState value)
	{
		if (value == WFState.Complete)
		{
			SetValByKey(MyFlowAttr.WFSta, getWFSta().Complete.getValue());
		}
		else if (value == WFState.Delete)
		{
			SetValByKey(MyFlowAttr.WFSta, getWFSta().Etc.getValue());
		}
		else
		{
			SetValByKey(MyFlowAttr.WFSta, getWFSta().Runing.getValue());
		}

		SetValByKey(MyFlowAttr.WFState, value.getValue());
	}
	/** 
	 状态(简单)
	*/
	public final WFSta getWFSta()
	{
		return WFSta.forValue(this.GetValIntByKey(MyFlowAttr.WFSta));
	}
	public final void setWFSta(WFSta value)
	{
		SetValByKey(MyFlowAttr.WFSta, value.getValue());
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
		return this.GetValStrByKey(MyFlowAttr.GUID);
	}
	public final void setGUID(String value)
	{
		SetValByKey(MyFlowAttr.GUID, value);
	}

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
	/** 
	 产生的工作流程
	*/
	public MyFlow()
	{
	}
	public MyFlow(long workId) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(MyFlowAttr.WorkID, workId);
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

		map.AddTBIntPK(MyFlowAttr.WorkID, 0, "WorkID", false, false);
		map.AddTBInt(MyFlowAttr.FID, 0, "FID", false, false);

		map.AddTBString(MyFlowAttr.Title, null, "流程标题", true, false, 0, 100, 10, true);
		map.AddDDLEntities(MyFlowAttr.FK_Flow, null, "流程名称", new Flows(), false);

			//map.AddDDLEntities(MyFlowAttr.FK_Dept, null, "发起人部门", new BP.Port.Depts(), false);
			//map.AddTBString(MyFlowAttr.Starter, null, "发起人编号", true, false, 0, 30, 10);
			//map.AddTBString(MyFlowAttr.StarterName, null, "发起人名称", true, false, 0, 30, 10);
			//map.AddTBString(MyFlowAttr.BillNo, null, "单据编号", true, false, 0, 100, 10);

		map.AddTBDateTime(MyFlowAttr.RDT, "发起日期", true, true);
		map.AddDDLSysEnum(MyFlowAttr.WFSta, 0, "状态", true, false, MyFlowAttr.WFSta, "@0=运行中@1=已完成@2=其他");
		map.AddDDLSysEnum(MyFlowAttr.WFState, 0, "流程状态", true, false, MyFlowAttr.WFState);
		map.AddDDLSysEnum(MyFlowAttr.TSpan, 0, "时间段", true, false, MyFlowAttr.TSpan, "@0=本周@1=上周@2=两周以前@3=三周以前@4=更早");
		map.AddTBString(MyFlowAttr.NodeName, null, "当前节点", true, false, 0, 100, 10, true);
		map.AddTBString(MyStartFlowAttr.TodoEmps, null, "当前处理人", true, false, 0, 100, 10, true);

		map.AddTBString(MyFlowAttr.Emps, null, "参与人", true, false, 0, 4000, 10, true);
		map.AddTBStringDoc(MyFlowAttr.FlowNote, null, "备注", true, false, true);


		map.AddTBMyNum();

		map.AddSearchAttr(MyFlowAttr.FK_Flow);
		map.AddSearchAttr(MyFlowAttr.WFSta);
		map.AddSearchAttr(MyFlowAttr.TSpan);


			//增加隐藏的查询条件.
		AttrOfSearch search = new AttrOfSearch(MyFlowAttr.Emps, "人员", MyFlowAttr.Emps, " LIKE ", "%@WebUser.No%", 0, true);
		
		map.getAttrsOfSearch().Add(search);

		RefMethod rm = new RefMethod();
		rm.Title = "流程轨迹";
		rm.ClassMethodName = this.toString() + ".DoTrack";
		rm.Icon = Glo.getCCFlowAppPath() + "WF/Img/FileType/doc.gif";
		map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}
	public final String DoTrack()
	{
		try {
			PubClass.WinOpen(ContextHolderUtils.getResponse(),SystemConfig.getCCFlowWebPath() + "WF/WFRpt.jsp?WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow(), 900, 800);
		} catch (IOException e) {
			Log.DebugWriteError("MyFlow DoTrack() " + e);
		}
		return null;
	}
}