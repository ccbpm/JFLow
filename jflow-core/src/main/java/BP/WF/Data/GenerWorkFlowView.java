package BP.WF.Data;

import BP.DA.*;
import BP.WF.*;
import BP.Port.*;
import BP.Sys.*;
import BP.En.*;
import BP.WF.Template.*;
import BP.WF.*;
import java.util.*;

/** 
 流程实例
*/
public class GenerWorkFlowView extends Entity
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
	public final String getFlowNote()
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.FlowNote);
	}
	public final void setFlowNote(String value)
	{
		SetValByKey(GenerWorkFlowViewAttr.FlowNote, value);
	}
	/** 
	 工作流程编号
	*/
	public final String getFK_Flow()
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)
	{
		SetValByKey(GenerWorkFlowViewAttr.FK_Flow, value);
	}
	/** 
	 BillNo
	*/
	public final String getBillNo()
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.BillNo);
	}
	public final void setBillNo(String value)
	{
		SetValByKey(GenerWorkFlowViewAttr.BillNo, value);
	}
	/** 
	 流程名称
	*/
	public final String getFlowName()
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.FlowName);
	}
	public final void setFlowName(String value)
	{
		SetValByKey(GenerWorkFlowViewAttr.FlowName, value);
	}
	/** 
	 优先级
	*/
	public final int getPRI()
	{
		return this.GetValIntByKey(GenerWorkFlowViewAttr.PRI);
	}
	public final void setPRI(int value)
	{
		SetValByKey(GenerWorkFlowViewAttr.PRI, value);
	}
	/** 
	 待办人员数量
	*/
	public final int getTodoEmpsNum()
	{
		return this.GetValIntByKey(GenerWorkFlowViewAttr.TodoEmpsNum);
	}
	public final void setTodoEmpsNum(int value)
	{
		SetValByKey(GenerWorkFlowViewAttr.TodoEmpsNum, value);
	}
	/** 
	 待办人员列表
	*/
	public final String getTodoEmps()
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.TodoEmps);
	}
	public final void setTodoEmps(String value)
	{
		SetValByKey(GenerWorkFlowViewAttr.TodoEmps, value);
	}
	/** 
	 参与人
	*/
	public final String getEmps()
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.Emps);
	}
	public final void setEmps(String value)
	{
		SetValByKey(GenerWorkFlowViewAttr.Emps, value);
	}
	/** 
	 状态
	*/
	public final TaskSta getTaskSta()
	{
		return TaskSta.forValue(this.GetValIntByKey(GenerWorkFlowViewAttr.TaskSta));
	}
	public final void setTaskSta(TaskSta value)
	{
		SetValByKey(GenerWorkFlowViewAttr.TaskSta, value.getValue());
	}
	/** 
	 类别编号
	*/
	public final String getFK_FlowSort()
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.FK_FlowSort);
	}
	public final void setFK_FlowSort(String value)
	{
		SetValByKey(GenerWorkFlowViewAttr.FK_FlowSort, value);
	}
	/** 
	 部门编号
	*/
	public final String getFK_Dept()
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.FK_Dept);
	}
	public final void setFK_Dept(String value)
	{
		SetValByKey(GenerWorkFlowViewAttr.FK_Dept, value);
	}
	/** 
	 标题
	*/
	public final String getTitle()
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.Title);
	}
	public final void setTitle(String value)
	{
		SetValByKey(GenerWorkFlowViewAttr.Title, value);
	}
	/** 
	 客户编号
	*/
	public final String getGuestNo()
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.GuestNo);
	}
	public final void setGuestNo(String value)
	{
		SetValByKey(GenerWorkFlowViewAttr.GuestNo, value);
	}
	/** 
	 客户名称
	*/
	public final String getGuestName()
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.GuestName);
	}
	public final void setGuestName(String value)
	{
		SetValByKey(GenerWorkFlowViewAttr.GuestName, value);
	}
	/** 
	 产生时间
	*/
	public final String getRDT()
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.RDT);
	}
	public final void setRDT(String value)
	{
		SetValByKey(GenerWorkFlowViewAttr.RDT, value);
	}
	/** 
	 节点应完成时间
	*/
	public final String getSDTOfNode()
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.SDTOfNode);
	}
	public final void setSDTOfNode(String value)
	{
		SetValByKey(GenerWorkFlowViewAttr.SDTOfNode, value);
	}
	/** 
	 流程应完成时间
	*/
	public final String getSDTOfFlow()
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.SDTOfFlow);
	}
	public final void setSDTOfFlow(String value)
	{
		SetValByKey(GenerWorkFlowViewAttr.SDTOfFlow, value);
	}
	/** 
	 流程ID
	*/
	public final long getWorkID()
	{
		return this.GetValInt64ByKey(GenerWorkFlowViewAttr.WorkID);
	}
	public final void setWorkID(long value)
	{
		SetValByKey(GenerWorkFlowViewAttr.WorkID, value);
	}
	/** 
	 主线程ID
	*/
	public final long getFID()
	{
		return this.GetValInt64ByKey(GenerWorkFlowViewAttr.FID);
	}
	public final void setFID(long value)
	{
		SetValByKey(GenerWorkFlowViewAttr.FID, value);
	}
	/** 
	 父节点流程编号.
	*/
	public final long getPWorkID()
	{
		return this.GetValInt64ByKey(GenerWorkFlowViewAttr.PWorkID);
	}
	public final void setPWorkID(long value)
	{
		SetValByKey(GenerWorkFlowViewAttr.PWorkID, value);
	}
	/** 
	 父流程调用的节点
	*/
	public final int getPNodeID()
	{
		return this.GetValIntByKey(GenerWorkFlowViewAttr.PNodeID);
	}
	public final void setPNodeID(int value)
	{
		SetValByKey(GenerWorkFlowViewAttr.PNodeID, value);
	}
	/** 
	 PFlowNo
	*/
	public final String getPFlowNo()
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.PFlowNo);
	}
	public final void setPFlowNo(String value)
	{
		SetValByKey(GenerWorkFlowViewAttr.PFlowNo, value);
	}
	/** 
	 吊起子流程的人员
	*/
	public final String getPEmp()
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.PEmp);
	}
	public final void setPEmp(String value)
	{
		SetValByKey(GenerWorkFlowViewAttr.PEmp, value);
	}
	/** 
	 发起人
	*/
	public final String getStarter()
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.Starter);
	}
	public final void setStarter(String value)
	{
		SetValByKey(GenerWorkFlowViewAttr.Starter, value);
	}
	/** 
	 发起人名称
	*/
	public final String getStarterName()
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.StarterName);
	}
	public final void setStarterName(String value)
	{
		this.SetValByKey(GenerWorkFlowViewAttr.StarterName, value);
	}
	/** 
	 发起人部门名称
	*/
	public final String getDeptName()
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.DeptName);
	}
	public final void setDeptName(String value)
	{
		this.SetValByKey(GenerWorkFlowViewAttr.DeptName, value);
	}
	/** 
	 当前节点名称
	*/
	public final String getNodeName()
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.NodeName);
	}
	public final void setNodeName(String value)
	{
		this.SetValByKey(GenerWorkFlowViewAttr.NodeName, value);
	}
	/** 
	 当前工作到的节点
	*/
	public final int getFK_Node()
	{
		return this.GetValIntByKey(GenerWorkFlowViewAttr.FK_Node);
	}
	public final void setFK_Node(int value)
	{
		SetValByKey(GenerWorkFlowViewAttr.FK_Node, value);
	}
	/** 
	 工作流程状态
	*/
	public final WFState getWFState()
	{
		return WFState.forValue(this.GetValIntByKey(GenerWorkFlowViewAttr.WFState));
	}
	public final void setWFState(WFState value)
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
	public final WFSta getWFSta()
	{
		return WFSta.forValue(this.GetValIntByKey(GenerWorkFlowViewAttr.WFSta));
	}
	public final void setWFSta(WFSta value)
	{
		SetValByKey(GenerWorkFlowViewAttr.WFSta, value.getValue());
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
	public final String getGUID()
	{
		return this.GetValStrByKey(GenerWorkFlowViewAttr.GUID);
	}
	public final void setGUID(String value)
	{
		SetValByKey(GenerWorkFlowViewAttr.GUID, value);
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
	*/
	public GenerWorkFlowView(long workId)
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
		rm.RefMethodType = RefMethodType.LinkeWinOpen;
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
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Icon = "../../WF/Img/Btn/CC.gif";
		rm.Title = "修复该流程数据实例";
		rm.IsForEns = false;
		rm.ClassMethodName = this.toString() + ".RepairDataIt";
		rm.RefMethodType = RefMethodType.Func;
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 执行功能.
	//,string isOK, int wfstate, string fk_emp
	public final String DoTest(String toEmpNo, int toNodeID)
	{
	   return BP.WF.Dev2Interface.Flow_ReSend(this.getWorkID(), toNodeID, toEmpNo, "admin调整");
	}
	public final String RepairDataIt()
	{
		String infos = "";

		Flow fl = new Flow(this.getFK_Flow());
		Node nd = new Node(Integer.parseInt(fl.No + "01"));
		Work wk = nd.getHisWork();

		String trackTable = "ND" + Integer.parseInt(fl.No) + "Track";
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
			infos += "@ 错误:" + fl.getNo() + " - Rec" + wk.getRec() + " db=" + wk.getOID() + " - " + fl.Name;
		}

		String json = DataType.ReadTextFile(file);
		DataTable dtVal = BP.Tools.Json.ToDataTable(json);

		DataRow mydr = dtVal.Rows[0];

		Attrs attrs = wk.EnMap.Attrs;
		boolean isHave = false;
		for (Attr attr : attrs)
		{
			String jsonVal = mydr.get(attr.Key).toString();
			String enVal = wk.GetValStringByKey(attr.Key);
			if (DataType.IsNullOrEmpty(enVal) == true)
			{
				wk.SetValByKey(attr.Key, jsonVal);
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
	public final String RepairDataAll()
	{
		String infos = "";

		Flows fls = new Flows();
		fls.RetrieveAll();

		for (Flow fl : fls.ToJavaList())
		{


			String sql = "SELECT OID FROM " + fl.getPTable() + " WHERE BillNo IS NULL AND OID=" + this.getWorkID();
			DataTable dt = DBAccess.RunSQLReturnTable(sql);

			Node nd = new Node(Integer.parseInt(fl.No + "01"));
			Work wk = nd.getHisWork();

			String trackTable = "ND" + Integer.parseInt(fl.No) + "Track";
			for (DataRow dr : dt.Rows)
			{
				long workid = Long.parseLong(dr.get("OID").toString());

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
					infos += "@ 错误:" + fl.getNo() + " - Rec" + wk.getRec() + " db=" + wk.getOID() + " - " + fl.Name;
				}

				String json = DataType.ReadTextFile(file);
				DataTable dtVal = BP.Tools.Json.ToDataTable(json);

				DataRow mydr = dtVal.Rows[0];

				Attrs attrs = wk.EnMap.Attrs;
				boolean isHave = false;
				for (Attr attr : attrs)
				{
					String jsonVal = mydr.get(attr.Key).toString();
					String enVal = wk.GetValStringByKey(attr.Key);
					if (DataType.IsNullOrEmpty(enVal) == true)
					{
						wk.SetValByKey(attr.Key, jsonVal);
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
	*/
	public final String Rollback(String nodeid, String note)
	{
		BP.WF.Template.FlowSheet fl = new Template.FlowSheet(this.getFK_Flow());
		return fl.DoRebackFlowData(this.getWorkID(), Integer.parseInt(nodeid), note);
	}

	public final String DoTrack()
	{
		return "../../WF/WFRpt.htm?WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow();
	}
	/** 
	 执行移交
	 
	 @param ToEmp
	 @param Note
	 @return 
	*/
	public final String DoShift(String ToEmp, String Note)
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
	public final String DoDelete()
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
	public final String DoFlowShift()
	{
		return "../../WorkOpt/Forward.htm?WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getFK_Node();
	}
	/** 
	 回滚流程
	 
	 @return 
	*/
	public final String Rollback()
	{

		return "../../WorkOpt/Rollback.htm?WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getFK_Node();
	}
	/** 
	 执行跳转
	 
	 @return 
	*/
	public final String DoFlowSkip()
	{
		return "../../WorkOpt/FlowSkip.htm?WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getFK_Node();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}