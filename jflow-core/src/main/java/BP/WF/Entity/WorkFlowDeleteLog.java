package BP.WF.Entity;

import BP.En.EnType;
import BP.En.EntityOID;
import BP.En.Map;
import BP.Port.Depts;
import BP.WF.Flows;
import BP.WF.Data.FlowDataAttr;

/**
 * 流程删除日志
 */
public class WorkFlowDeleteLog extends EntityOID
{
	private Map _enMap;
	
	// 基本属性
	/**
	 * 工作ID
	 */
	public final long getOID()
	{
		return this.GetValInt64ByKey(WorkFlowDeleteLogAttr.OID);
	}
	
	public final void setOID(long value)
	{
		SetValByKey(WorkFlowDeleteLogAttr.OID, value);
	}
	
	/**
	 * 操作人
	 */
	public final String getOper()
	{
		return this.GetValStringByKey(WorkFlowDeleteLogAttr.Oper);
	}
	
	public final void setOper(String value)
	{
		SetValByKey(WorkFlowDeleteLogAttr.Oper, value);
	}
	
	/**
	 * 删除人员
	 */
	public final String getOperDept()
	{
		return this.GetValStringByKey(WorkFlowDeleteLogAttr.OperDept);
	}
	
	public final void setOperDept(String value)
	{
		SetValByKey(WorkFlowDeleteLogAttr.OperDept, value);
	}
	
	public final String getOperDeptName()
	{
		return this.GetValStringByKey(WorkFlowDeleteLogAttr.OperDeptName);
	}
	
	public final void setOperDeptName(String value)
	{
		SetValByKey(WorkFlowDeleteLogAttr.OperDeptName, value);
	}
	
	public final String getDeleteNote()
	{
		return this.GetValStringByKey(WorkFlowDeleteLogAttr.DeleteNote);
	}
	
	public final void setDeleteNote(String value)
	{
		SetValByKey(WorkFlowDeleteLogAttr.DeleteNote, value);
	}
	
	public final String getDeleteNoteHtml()
	{
		return this.GetValHtmlStringByKey(WorkFlowDeleteLogAttr.DeleteNote);
	}
	
	/**
	 * 记录日期
	 */
	public final String getDeleteDT()
	{
		return this.GetValStringByKey(WorkFlowDeleteLogAttr.DeleteDT);
	}
	
	public final void setDeleteDT(String value)
	{
		SetValByKey(WorkFlowDeleteLogAttr.DeleteDT, value);
	}
	
	/**
	 * 流程编号
	 */
	public final String getFK_Flow()
	{
		return this.GetValStringByKey(WorkFlowDeleteLogAttr.FK_Flow);
	}
	
	public final void setFK_Flow(String value)
	{
		SetValByKey(WorkFlowDeleteLogAttr.FK_Flow, value);
	}
	
	/**
	 * 流程类别
	 */
	public final String getFK_FlowSort()
	{
		return this.GetValStringByKey(WorkFlowDeleteLogAttr.FK_FlowSort);
	}
	
	public final void setFK_FlowSort(String value)
	{
		SetValByKey(WorkFlowDeleteLogAttr.FK_FlowSort, value);
	}
	
	// 构造函数
	/**
	 * 流程删除日志
	 */
	public WorkFlowDeleteLog()
	{
	}
	
	/**
	 * 重写基类方法
	 */
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		
		Map map = new Map("WF_WorkFlowDeleteLog");
		map.setEnDesc("流程删除日志");
		map.setEnType(EnType.App);
		
		// 流程基础数据。
		map.AddTBIntPKOID(FlowDataAttr.OID, "WorkID");
		map.AddTBInt(FlowDataAttr.FID, 0, "FID", false, false);
		map.AddDDLEntities(FlowDataAttr.FK_Dept, null, "部门", new Depts(), false);
		map.AddTBString(FlowDataAttr.Title, null, "标题", true, true, 0, 100, 100);
		map.AddTBString(FlowDataAttr.FlowStarter, null, "发起人", true, true, 0,
				100, 100);
		map.AddTBDateTime(FlowDataAttr.FlowStartRDT, null, "发起日期", true, true);
		map.AddDDLEntities(FlowDataAttr.FK_NY, null, "年月", new BP.Pub.NYs(),
				false);
		map.AddDDLEntities(FlowDataAttr.FK_Flow, null, "流程", new Flows(), false);
		map.AddTBDateTime(FlowDataAttr.FlowEnderRDT, null, "结束日期", true, true);
		map.AddTBInt(FlowDataAttr.FlowEndNode, 0, "结束节点", true, true);
		map.AddTBInt(FlowDataAttr.FlowDaySpan, 0, "跨度(天)", true, true);
		map.AddTBInt(FlowDataAttr.MyNum, 1, "个数", true, true);
		map.AddTBString(FlowDataAttr.FlowEmps, null, "参与人", false, false, 0,
				100, 100);
		
		// 删除信息.
		map.AddTBString(WorkFlowDeleteLogAttr.Oper, null, "删除人员", true, true,
				0, 20, 10);
		map.AddTBString(WorkFlowDeleteLogAttr.OperDept, null, "删除人员部门", true,
				true, 0, 20, 10);
		map.AddTBString(WorkFlowDeleteLogAttr.OperDeptName, null, "删除人员名称",
				true, true, 0, 200, 10);
		map.AddTBString(WorkFlowDeleteLogAttr.DeleteNote, "", "删除原因", true,
				true, 0, 4000, 10);
		map.AddTBDateTime(WorkFlowDeleteLogAttr.DeleteDT, null, "删除日期", true,
				true);
		
		// 查询.
		map.AddSearchAttr(FlowDataAttr.FK_Dept);
		map.AddSearchAttr(FlowDataAttr.FK_NY);
		map.AddSearchAttr(FlowDataAttr.FK_Flow);
		
		// map.AddHidden(FlowDataAttr.FlowEmps, " LIKE ", "'%@WebUser.No%'");
		
		this.set_enMap(map);
		return this.get_enMap();
	}
}