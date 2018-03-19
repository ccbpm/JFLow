package BP.CN;

import BP.DA.DBUrl;
import BP.DA.DBUrlType;
import BP.En.Entity;
import BP.En.Map;
import BP.En.UAC;

public class EmpWork extends Entity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public final String getADT()
	{
		return this.GetValStrByKey(EmpWorkAttr.ADT);
	}
	
	public final String getAtPara()
	{
		return this.GetValStrByKey(EmpWorkAttr.AtPara);
	}
	
	public final String getBillNo()
	{
		return this.GetValStrByKey(EmpWorkAttr.BillNo);
	}
	
	public final String getDeptName()
	{
		return this.GetValStrByKey(EmpWorkAttr.DeptName);
	}
	
	public final String getFID()
	{
		return this.GetValStrByKey(EmpWorkAttr.FID);
	}
	
	public final String getFK_Dept()
	{
		return this.GetValStrByKey(EmpWorkAttr.FK_Dept);
	}
	
	public final String getFK_Emp()
	{
		return this.GetValStrByKey(EmpWorkAttr.FK_Emp);
	}
	
	public final String getFK_Flow()
	{
		return this.GetValStrByKey(EmpWorkAttr.FK_Flow);
	}
	
	public final String getFK_FlowSort()
	{
		return this.GetValStrByKey(EmpWorkAttr.FK_FlowSort);
	}
	
	public final String getFK_Node()
	{
		return this.GetValStrByKey(EmpWorkAttr.FK_Node);
	}
	
	public final String getFlowName()
	{
		return this.GetValStrByKey(EmpWorkAttr.FlowName);
	}
	
	public final String getFlowNote()
	{
		return this.GetValStrByKey(EmpWorkAttr.FlowNote);
	}
	
	public final String getGuestName()
	{
		return this.GetValStrByKey(EmpWorkAttr.GuestName);
	}
	
	public final String getGuestNo()
	{
		return this.GetValStrByKey(EmpWorkAttr.GuestNo);
	}
	
	public final String getIsRead()
	{
		return this.GetValStrByKey(EmpWorkAttr.IsRead);
	}
	
	public final String getListType()
	{
		return this.GetValStrByKey(EmpWorkAttr.ListType);
	}
	
	public final String getNodeName()
	{
		return this.GetValStrByKey(EmpWorkAttr.NodeName);
	}
	
	public final String getPFlowNo()
	{
		return this.GetValStrByKey(EmpWorkAttr.PFlowNo);
	}
	
	public final String getPressTimes()
	{
		return this.GetValStrByKey(EmpWorkAttr.PressTimes);
	}
	
	public final String getPRI()
	{
		return this.GetValStrByKey(EmpWorkAttr.PRI);
	}
	
	public final String getPWorkID()
	{
		return this.GetValStrByKey(EmpWorkAttr.PWorkID);
	}
	
	public final String getRDT()
	{
		return this.GetValStrByKey(EmpWorkAttr.RDT);
	}
	
	public final String getSDT()
	{
		return this.GetValStrByKey(EmpWorkAttr.SDT);
	}
	
	public final String getSDTOfNode()
	{
		return this.GetValStrByKey(EmpWorkAttr.SDTOfNode);
	}
	
	public final String getSender()
	{
		return this.GetValStrByKey(EmpWorkAttr.Sender);
	}
	
	public final String getStarter()
	{
		return this.GetValStrByKey(EmpWorkAttr.Starter);
	}
	
	public final String getStarterName()
	{
		return this.GetValStrByKey(EmpWorkAttr.StarterName);
	}
	
	public final String getTaskSta()
	{
		return this.GetValStrByKey(EmpWorkAttr.TaskSta);
	}
	
	public final String getTitle()
	{
		return this.GetValStrByKey(EmpWorkAttr.Title);
	}
	
	public final String getTodoEmps()
	{
		return this.GetValStrByKey(EmpWorkAttr.TodoEmps);
	}
	
	public final String getTodoEmpsNum()
	{
		return this.GetValStrByKey(EmpWorkAttr.TodoEmpsNum);
	}
	
	public final String getWFState()
	{
		return this.GetValStrByKey(EmpWorkAttr.WFState);
	}
	
	public final String getWorkID()
	{
		return this.GetValStrByKey(EmpWorkAttr.WorkID);
	}
	
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.Readonly();
		return uac;
	}
	
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map();
		
		// 基本属性
		map.setEnDBUrl(new DBUrl(DBUrlType.AppCenterDSN));
		map.setPhysicsTable("WF_EmpWorks");
		this.set_enMap(map);
		map.AddTBStringPK(EmpWorkAttr.WorkID, null, "编号", true, false, 0, 50,
				50);
		map.AddTBString(EmpWorkAttr.Title, null, "标题", true, false, 0, 50, 200);
		map.AddTBString(EmpWorkAttr.DeptName, null, "部门名称", true, false, 0, 50,
				200);
		map.AddTBString(EmpWorkAttr.StarterName, null, "发起人", true, false, 0,
				50, 200);
		map.AddTBString(EmpWorkAttr.TodoEmps, null, "人员编号", true, false, 0, 50,
				200);
		map.AddTBString(EmpWorkAttr.FlowName, null, "流程名称", true, false, 0, 50,
				200);
		map.AddTBString(EmpWorkAttr.SDTOfNode, null, "发起时间", true, false, 0,
				50, 200);
		
		return this.get_enMap();
	}
	
}
