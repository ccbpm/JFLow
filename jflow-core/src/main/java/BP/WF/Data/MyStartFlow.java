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
import BP.WF.Glo;
import BP.WF.Node;
import BP.WF.TaskSta;
import BP.WF.WFSta;
import BP.WF.WFState;

/** 
 我发起的流程
 
*/
public class MyStartFlow extends Entity
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
		return MyStartFlowAttr.WorkID;
	}
	/** 
	 备注
	*/
	public final String getFlowNote()
	{
		return this.GetValStrByKey(MyStartFlowAttr.FlowNote);
	}
	public final void setFlowNote(String value)
	{
		SetValByKey(MyStartFlowAttr.FlowNote, value);
	}
	/** 
	 工作流程编号
	*/
	public final String getFK_Flow()
	{
		return this.GetValStrByKey(MyStartFlowAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)
	{
		SetValByKey(MyStartFlowAttr.FK_Flow,value);
	}
	/** 
	 BillNo
	*/
	public final String getBillNo()
	{
		return this.GetValStrByKey(MyStartFlowAttr.BillNo);
	}
	public final void setBillNo(String value)
	{
		SetValByKey(MyStartFlowAttr.BillNo, value);
	}
	/** 
	 流程名称
	*/
	public final String getFlowName()
	{
		return this.GetValStrByKey(MyStartFlowAttr.FlowName);
	}
	public final void setFlowName(String value)
	{
		SetValByKey(MyStartFlowAttr.FlowName, value);
	}
	/** 
	 优先级
	*/
	public final int getPRI()
	{
		return this.GetValIntByKey(MyStartFlowAttr.PRI);
	}
	public final void setPRI(int value)
	{
		SetValByKey(MyStartFlowAttr.PRI, value);
	}
	/** 
	 待办人员数量
	*/
	public final int getTodoEmpsNum()
	{
		return this.GetValIntByKey(MyStartFlowAttr.TodoEmpsNum);
	}
	public final void setTodoEmpsNum(int value)
	{
		SetValByKey(MyStartFlowAttr.TodoEmpsNum, value);
	}
	/** 
	 待办人员列表
	*/
	public final String getTodoEmps()
	{
		return this.GetValStrByKey(MyStartFlowAttr.TodoEmps);
	}
	public final void setTodoEmps(String value)
	{
		SetValByKey(MyStartFlowAttr.TodoEmps, value);
	}
	/** 
	 参与人
	*/
	public final String getEmps()
	{
		return this.GetValStrByKey(MyStartFlowAttr.Emps);
	}
	public final void setEmps(String value)
	{
		SetValByKey(MyStartFlowAttr.Emps, value);
	}
	/** 
	 状态
	*/
	public final TaskSta getTaskSta()
	{
		return TaskSta.forValue(this.GetValIntByKey(MyStartFlowAttr.TaskSta));
	}
	public final void setTaskSta(TaskSta value)
	{
		SetValByKey(MyStartFlowAttr.TaskSta, value.getValue());
	}
	/** 
	 类别编号
	*/
	public final String getFK_FlowSort()
	{
		return this.GetValStrByKey(MyStartFlowAttr.FK_FlowSort);
	}
	public final void setFK_FlowSort(String value)
	{
		SetValByKey(MyStartFlowAttr.FK_FlowSort, value);
	}
	/** 
	 部门编号
	*/
	public final String getFK_Dept()
	{
		return this.GetValStrByKey(MyStartFlowAttr.FK_Dept);
	}
	public final void setFK_Dept(String value)
	{
		SetValByKey(MyStartFlowAttr.FK_Dept,value);
	}
	/** 
	 标题
	*/
	public final String getTitle()
	{
		return this.GetValStrByKey(MyStartFlowAttr.Title);
	}
	public final void setTitle(String value)
	{
		SetValByKey(MyStartFlowAttr.Title,value);
	}
	/** 
	 客户编号
	*/
	public final String getGuestNo()
	{
		return this.GetValStrByKey(MyStartFlowAttr.GuestNo);
	}
	public final void setGuestNo(String value)
	{
		SetValByKey(MyStartFlowAttr.GuestNo, value);
	}
	/** 
	 客户名称
	*/
	public final String getGuestName()
	{
		return this.GetValStrByKey(MyStartFlowAttr.GuestName);
	}
	public final void setGuestName(String value)
	{
		SetValByKey(MyStartFlowAttr.GuestName, value);
	}
	/** 
	 产生时间
	*/
	public final String getRDT()
	{
		return this.GetValStrByKey(MyStartFlowAttr.RDT);
	}
	public final void setRDT(String value)
	{
		SetValByKey(MyStartFlowAttr.RDT,value);
	}
	/** 
	 节点应完成时间
	*/
	public final String getSDTOfNode()
	{
		return this.GetValStrByKey(MyStartFlowAttr.SDTOfNode);
	}
	public final void setSDTOfNode(String value)
	{
		SetValByKey(MyStartFlowAttr.SDTOfNode, value);
	}
	/** 
	 流程应完成时间
	*/
	public final String getSDTOfFlow()
	{
		return this.GetValStrByKey(MyStartFlowAttr.SDTOfFlow);
	}
	public final void setSDTOfFlow(String value)
	{
		SetValByKey(MyStartFlowAttr.SDTOfFlow, value);
	}
	/** 
	 流程ID
	*/
	public final long getWorkID()
	{
		return this.GetValInt64ByKey(MyStartFlowAttr.WorkID);
	}
	public final void setWorkID(long value)
	{
		SetValByKey(MyStartFlowAttr.WorkID,value);
	}
	/** 
	 主线程ID
	*/
	public final long getFID()
	{
		return this.GetValInt64ByKey(MyStartFlowAttr.FID);
	}
	public final void setFID(long value)
	{
		SetValByKey(MyStartFlowAttr.FID, value);
	}
	/** 
	 父节点流程编号.
	 
	*/
	public final long getPWorkID()
	{
		return this.GetValInt64ByKey(MyStartFlowAttr.PWorkID);
	}
	public final void setPWorkID(long value)
	{
		SetValByKey(MyStartFlowAttr.PWorkID, value);
	}
	/** 
	 父流程调用的节点
	 
	*/
	public final int getPNodeID()
	{
		return this.GetValIntByKey(MyStartFlowAttr.PNodeID);
	}
	public final void setPNodeID(int value)
	{
		SetValByKey(MyStartFlowAttr.PNodeID, value);
	}
	/** 
	 PFlowNo
	*/
	public final String getPFlowNo()
	{
		return this.GetValStrByKey(MyStartFlowAttr.PFlowNo);
	}
	public final void setPFlowNo(String value)
	{
		SetValByKey(MyStartFlowAttr.PFlowNo, value);
	}
	/** 
	 吊起子流程的人员
	*/
	public final String getPEmp()
	{
		return this.GetValStrByKey(MyStartFlowAttr.PEmp);
	}
	public final void setPEmp(String value)
	{
		SetValByKey(MyStartFlowAttr.PEmp, value);
	}
	/** 
	 发起人
	*/
	public final String getStarter()
	{
		return this.GetValStrByKey(MyStartFlowAttr.Starter);
	}
	public final void setStarter(String value)
	{
		SetValByKey(MyStartFlowAttr.Starter, value);
	}
	/** 
	 发起人名称
	*/
	public final String getStarterName()
	{
		return this.GetValStrByKey(MyStartFlowAttr.StarterName);
	}
	public final void setStarterName(String value)
	{
		this.SetValByKey(MyStartFlowAttr.StarterName, value);
	}
	/** 
	 发起人部门名称
	*/
	public final String getDeptName()
	{
		return this.GetValStrByKey(MyStartFlowAttr.DeptName);
	}
	public final void setDeptName(String value)
	{
		this.SetValByKey(MyStartFlowAttr.DeptName, value);
	}
	/** 
	 当前节点名称
	*/
	public final String getNodeName()
	{
		return this.GetValStrByKey(MyStartFlowAttr.NodeName);
	}
	public final void setNodeName(String value)
	{
		this.SetValByKey(MyStartFlowAttr.NodeName, value);
	}
	/** 
	 当前工作到的节点
	*/
	public final int getFK_Node()
	{
		return this.GetValIntByKey(MyStartFlowAttr.FK_Node);
	}
	public final void setFK_Node(int value)
	{
		SetValByKey(MyStartFlowAttr.FK_Node, value);
	}
	/** 
	 工作流程状态
	*/
	public final WFState getWFState()
	{
		return WFState.forValue(this.GetValIntByKey(MyStartFlowAttr.WFState));
	}
	public final void setWFState(WFState value)
	{
		if (value == WFState.Complete)
		{
			SetValByKey(MyStartFlowAttr.WFSta, getWFSta().Complete.getValue());
		}
		else if (value == WFState.Delete)
		{
			SetValByKey(MyStartFlowAttr.WFSta, getWFSta().Etc.getValue());
		}
		else
		{
			SetValByKey(MyStartFlowAttr.WFSta, getWFSta().Runing.getValue());
		}

		SetValByKey(MyStartFlowAttr.WFState, value.getValue());
	}
	/** 
	 状态(简单)
	*/
	public final WFSta getWFSta()
	{
		return WFSta.forValue(this.GetValIntByKey(MyStartFlowAttr.WFSta));
	}
	public final void setWFSta(WFSta value)
	{
		SetValByKey(MyStartFlowAttr.WFSta, value.getValue());
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
		return this.GetValStrByKey(MyStartFlowAttr.GUID);
	}
	public final void setGUID(String value)
	{
		SetValByKey(MyStartFlowAttr.GUID, value);
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
	public MyStartFlow()
	{
	}
	public MyStartFlow(long workId) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(MyStartFlowAttr.WorkID, workId);
		if (qo.DoQuery() == 0)
		{
			throw new RuntimeException("工作 MyStartFlow [" + workId + "]不存在。");
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
	 * @throws Exception 
	*/
	@Override
	public Map getEnMap(){
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_GenerWorkFlow", "我发起的流程");

		map.Java_SetEnType(EnType.View);
		
		map.AddTBIntPK(MyStartFlowAttr.WorkID, 0, "WorkID", true, false);
		map.AddTBString(MyStartFlowAttr.Title, null, "标题", true, false, 0, 100, 10, true);
		map.AddDDLEntities(MyStartFlowAttr.FK_Flow, null, "流程", new Flows(), false);
		map.AddTBString(MyStartFlowAttr.BillNo, null, "单据编号", true, false, 0, 100, 10);
		 map.AddTBInt(MyStartFlowAttr.FK_Node, 0, "节点编号", false, false);

         map.AddDDLSysEnum(MyStartFlowAttr.WFSta, 0, "状态", true, true, MyStartFlowAttr.WFSta, "@0=运行中@1=已完成@2=其他");
         map.AddTBString(MyStartFlowAttr.Starter, null, "发起人", false, false, 0, 100, 100);
         map.AddTBDateTime(MyStartFlowAttr.RDT, "发起日期", true, true);

         map.AddTBString(MyStartFlowAttr.NodeName, null, "停留节点", true, true, 0, 100, 100, false);
         map.AddTBString(MyStartFlowAttr.TodoEmps, null, "当前处理人", true, false, 0, 100, 100, false);
         map.AddTBStringDoc(MyFlowAttr.FlowNote, null, "备注", true, false, true);


         map.AddTBString(MyFlowAttr.Emps, null, "参与人", false, false, 0, 4000, 100, true);
         map.AddDDLSysEnum(MyFlowAttr.TSpan, 0, "时间段", true, false, MyFlowAttr.TSpan, "@0=本周@1=上周@2=两周以前@3=三周以前@4=更早");

         map.AddTBMyNum();

         //隐藏字段.
         map.AddTBInt(MyStartFlowAttr.WFState, 0, "状态", false, false);
         map.AddTBInt(MyStartFlowAttr.FID, 0, "FID", false, false);
         map.AddTBInt(MyFlowAttr.PWorkID, 0, "PWorkID", false, false);
		
		map.AddTBInt(MyStartFlowAttr.FID, 0, "FID", false, false);


		map.AddSearchAttr(MyStartFlowAttr.FK_Flow);
		map.AddSearchAttr(MyStartFlowAttr.WFSta);
		map.AddSearchAttr(MyStartFlowAttr.TSpan);


		
		try {
			AttrOfSearch  search = new AttrOfSearch(MyStartFlowAttr.Starter, "发起人", MyStartFlowAttr.Starter, "=", BP.Web.WebUser.getNo(), 0, true);
			map.getAttrsOfSearch().Add(search);

			search = new AttrOfSearch(MyStartFlowAttr.WFState, "流程状态", MyStartFlowAttr.WFState, "not in", "('0')", 0, true);
			map.getAttrsOfSearch().Add(search);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		RefMethod rm = new RefMethod();
		rm.Title = "流程轨迹";
		rm.ClassMethodName = this.toString() + ".DoTrack";
		rm.refMethodType= RefMethodType.LinkeWinOpen;
		rm.Icon = Glo.getCCFlowAppPath() + "WF/Img/Track.png";
		map.AddRefMethod(rm);
		
		 rm = new RefMethod();
         rm.Title = "表单";
         rm.ClassMethodName = this.toString() + ".DoOpenLastForm";
         rm.Icon = Glo.getCCFlowAppPath() + "WF/Img/Form.png";
         rm.refMethodType = RefMethodType.LinkeWinOpen;
         rm.IsForEns = true;
         map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}
		///#region 执行诊断
	public final String DoTrack()
	{ 
		return Glo.getCCFlowAppPath() + "/WF/WFRpt.htm?WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow()+"&FK_Node="+this.getFK_Node();
	}
	
	 /// <summary>
    /// 打开最后一个节点表单
    /// </summary>
    /// <returns></returns>
    public String DoOpenLastForm() throws Exception{
        Paras pss = new Paras();
        pss.SQL = "SELECT MYPK FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track WHERE ActionType=" + BP.Sys.SystemConfig.getAppCenterDBVarStr() + "ActionType AND WorkID=" + BP.Sys.SystemConfig.getAppCenterDBVarStr() + "WorkID ORDER BY RDT DESC";
        pss.Add("ActionType",ActionType.Forward.getValue());
        pss.Add("WorkID", this.getWorkID());
        DataTable dt = DBAccess.RunSQLReturnTable(pss);
        if (dt != null && dt.Rows.size() > 0)
        {
            String myPk = dt.Rows.get(0).getValue(0).toString();
            return Glo.getCCFlowAppPath() + "WF/WFRpt.htm?WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getFK_Node() + "&DoType=View&MyPK=" + myPk + "&PWorkID=" + this.getPWorkID();
        }

        Node nd = new Node(this.getFK_Node());
        nd.WorkID = this.getWorkID(); //为求当前表单ID获得参数，而赋值.
        return Glo.getCCFlowAppPath() + "WF/CCForm/FrmGener.htm?WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&FK_MapData=" + nd.getNodeFrmID() + "&ReadOnly=1&IsEdit=0";
    }
    ///#endregion
}