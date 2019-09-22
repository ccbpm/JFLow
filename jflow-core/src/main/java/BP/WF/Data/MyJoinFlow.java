package BP.WF.Data;

import BP.DA.*;
import BP.WF.*;
import BP.Port.*;
import BP.Sys.*;
import BP.En.*;
import BP.WF.*;
import java.util.*;

/** 
 我参与的流程
*/
public class MyJoinFlow extends Entity
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
			//uac.LoadRightFromCCGPM(this);
			//return uac;
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
		if (value == WF.WFState.Complete)
		{
			SetValByKey(MyFlowAttr.WFSta, getWFSta().Complete.getValue());
		}
		else if (value == WF.WFState.Delete)
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
		return this.GetValStrByKey(MyFlowAttr.GUID);
	}
	public final void setGUID(String value)
	{
		SetValByKey(MyFlowAttr.GUID, value);
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
	public MyJoinFlow()
	{
	}
	public MyJoinFlow(long workId)
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
		if (this._enMap != null)
		{
			return this._enMap;
		}

		Map map = new Map("WF_GenerWorkFlow", "我审批的流程");

		map.Java_SetEnType(EnType.View);

		map.AddTBIntPK(MyFlowAttr.WorkID, 0, "WorkID", false, false);
		map.AddTBInt(MyFlowAttr.FID, 0, "FID", false, false);
		map.AddTBInt(MyFlowAttr.PWorkID, 0, "PWorkID", false, false);
		map.AddTBString(MyFlowAttr.Title, null, "流程标题", true, false, 0, 100, 150, true);
		map.AddDDLEntities(MyFlowAttr.FK_Flow, null, "流程名称", new Flows(), false);
		map.AddTBString(MyFlowAttr.BillNo, null, "单据编号", true, false, 0, 100, 50);
		map.AddTBString(MyFlowAttr.StarterName, null, "发起人", true, false, 0, 30, 40);


		map.AddTBDateTime(MyFlowAttr.RDT, "发起日期", true, true);
		map.AddDDLSysEnum(MyFlowAttr.WFSta, 0, "状态", true, false, MyFlowAttr.WFSta, "@0=运行中@1=已完成@2=其他");
		map.AddDDLSysEnum(MyFlowAttr.TSpan, 0, "时间段", true, false, MyFlowAttr.TSpan, "@0=本周@1=上周@2=两周以前@3=三周以前@4=更早");
		map.AddTBString(MyFlowAttr.NodeName, null, "当前节点", true, false, 0, 100, 100, true);
		map.AddTBString(MyStartFlowAttr.TodoEmps, null, "当前处理人", true, false, 0, 100, 100, true);

		map.AddTBString(MyFlowAttr.Emps, null, "参与人", false, false, 0, 4000, 10, true);
		map.AddTBStringDoc(MyFlowAttr.FlowNote, null, "备注", true, false, true);
		map.AddTBDateTime(GenerWorkFlowAttr.SDTOfNode, "节点应完成时间", true, true);

			//隐藏字段.
		map.AddTBInt(MyFlowAttr.FK_Node, 0, "FK_Node", false, false);


		map.AddTBMyNum();

		map.DTSearchKey = MyFlowAttr.RDT;
		map.DTSearchLable = "发起日期";
		map.DTSearchWay = DTSearchWay.ByDate;

		map.DTSearchKey = GenerWorkFlowAttr.SDTOfNode;
		map.DTSearchLable = "节点应完成时间";
		map.DTSearchWay = DTSearchWay.ByDate;


		 //   map.AddSearchAttr(MyFlowAttr.FK_Flow);
		map.AddSearchAttr(MyFlowAttr.WFSta);
		map.AddSearchAttr(MyFlowAttr.TSpan);
		map.AddHidden(MyStartFlowAttr.FID, "=", "0");
			//map.IsShowSearchKey = false;

			//增加隐藏的查询条件. 我参与的流程.
		AttrOfSearch search = new AttrOfSearch(MyFlowAttr.Emps, "人员", MyFlowAttr.Emps, " LIKE ", "%" + BP.Web.WebUser.No + "%", 0, true);
		map.AttrsOfSearch.Add(search);


		RefMethod rm = new RefMethod();
		rm.Title = "轨迹";
		rm.ClassMethodName = this.toString() + ".DoTrack";
		rm.RefMethodType = RefMethodType.LinkeWinOpen;
		rm.Icon = "../../WF/Img/Track.png";
		rm.IsForEns = true;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "表单";
		rm.ClassMethodName = this.toString() + ".DoOpenLastForm";
		rm.Icon = "../../WF/Img/Form.png";
		rm.RefMethodType = RefMethodType.LinkeWinOpen;
		rm.IsForEns = true;
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
		//PubClass.WinOpen(Glo.CCFlowAppPath + "WF/WFRpt.htm?WorkID=" + this.WorkID + "&FID=" + this.FID + "&FK_Flow=" + this.FK_Flow, 900, 800);
		return "/WF/WFRpt.htm?CurrTab=Truck&WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getFK_Node();
	}
	/** 
	 打开最后一个节点表单
	 
	 @return 
	*/
	public final String DoOpenLastForm()
	{

		Paras pss = new Paras();
		pss.SQL = "SELECT MYPK FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track WHERE ActionType=" + BP.Sys.SystemConfig.AppCenterDBVarStr + "ActionType AND WorkID=" + BP.Sys.SystemConfig.AppCenterDBVarStr + "WorkID ORDER BY RDT DESC";
		pss.Add("ActionType", BP.WF.ActionType.Forward.getValue());
		pss.Add("WorkID", this.getWorkID());
		DataTable dt = DBAccess.RunSQLReturnTable(pss);
		if (dt != null && dt.Rows.Count > 0)
		{
			String myPk = dt.Rows[0][0].toString();
			return "/WF/WFRpt.htm?CurrTab=Frm&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getFK_Node() + "&DoType=View&MyPK=" + myPk + "&PWorkID=" + this.getPWorkID();
		}

		Node nd = new Node(this.getFK_Node());
		nd.WorkID = this.getWorkID(); //为获取表单ID ( NodeFrmID )提供参数.

		return "/WF/CCForm/FrmGener.htm?WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&FK_MapData=" + nd.getNodeFrmID() + "&ReadOnly=1&IsEdit=0";
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

}