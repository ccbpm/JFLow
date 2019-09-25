package BP.WF;

import BP.En.*;
import BP.En.Map;
import BP.WF.Data.*;

/** 
 流程删除日志
*/
public class WorkFlowDeleteLog extends EntityOID
{

		///#region 基本属性
	  
	/** 
	 操作人
	 * @throws Exception 
	*/
	public final String getOper() throws Exception
	{
		return this.GetValStringByKey(WorkFlowDeleteLogAttr.Oper);
	}
	public final void setOper(String value) throws Exception
	{
		SetValByKey(WorkFlowDeleteLogAttr.Oper, value);
	}
	/** 
	 删除人员
	*/
	public final String getOperDept() throws Exception
	{
		return this.GetValStringByKey(WorkFlowDeleteLogAttr.OperDept);
	}
	public final void setOperDept(String value) throws Exception
	{
		SetValByKey(WorkFlowDeleteLogAttr.OperDept, value);
	}
	public final String getOperDeptName() throws Exception
	{
		return this.GetValStringByKey(WorkFlowDeleteLogAttr.OperDeptName);
	}
	public final void setOperDeptName(String value) throws Exception
	{
		SetValByKey(WorkFlowDeleteLogAttr.OperDeptName, value);
	}
	public final String getDeleteNote() throws Exception
	{
		return this.GetValStringByKey(WorkFlowDeleteLogAttr.DeleteNote);
	}
	public final void setDeleteNote(String value) throws Exception
	{
		SetValByKey(WorkFlowDeleteLogAttr.DeleteNote, value);
	}
	public final String getDeleteNoteHtml() throws Exception
	{
		return this.GetValHtmlStringByKey(WorkFlowDeleteLogAttr.DeleteNote);
	}
	/** 
	 记录日期
	*/
	public final String getDeleteDT() throws Exception
	{
		return this.GetValStringByKey(WorkFlowDeleteLogAttr.DeleteDT);
	}
	public final void setDeleteDT(String value) throws Exception
	{
		SetValByKey(WorkFlowDeleteLogAttr.DeleteDT, value);
	}
	/** 
	 流程编号
	*/
	public final String getFK_Flow() throws Exception
	{
		return this.GetValStringByKey(WorkFlowDeleteLogAttr.FK_Flow);
	}
	public final void setFK_Flow(String value) throws Exception
	{
		SetValByKey(WorkFlowDeleteLogAttr.FK_Flow, value);
	}
	/** 
	 流程类别
	*/
	public final String getFK_FlowSort() throws Exception
	{
		return this.GetValStringByKey(WorkFlowDeleteLogAttr.FK_FlowSort);
	}
	public final void setFK_FlowSort(String value) throws Exception
	{
		SetValByKey(WorkFlowDeleteLogAttr.FK_FlowSort, value);
	}

		///#endregion


		///#region 构造函数
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.Readonly();
		return uac;
	}
	/** 
	 流程删除日志
	*/
	public WorkFlowDeleteLog()
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

		Map map = new Map("WF_WorkFlowDeleteLog", "流程删除日志");

			// 流程基础数据。
		map.AddTBIntPKOID();
		map.AddTBInt(GenerWorkFlowAttr.FID, 0, "FID", false, false);
		map.AddDDLEntities(GenerWorkFlowAttr.FK_Dept, null, "部门", new BP.WF.Port.Depts(), false);
		map.AddTBString(GenerWorkFlowAttr.Title, null, "标题", true, true, 0, 100, 100);
		map.AddTBString(GERptAttr.FlowStarter, null, "发起人", true, true, 0, 100, 100);
		map.AddTBDateTime(GERptAttr.FlowStartRDT, null, "发起时间", true, true);
			//map.AddDDLEntities(GenerWorkFlowAttr.FK_NY, null, "年月", new BP.Pub.NYs(), false);
		map.AddDDLEntities(GenerWorkFlowAttr.FK_Flow, null, "流程", new Flows(), false);
		map.AddTBDateTime(GERptAttr.FlowEnderRDT, null, "最后处理时间", true, true);
		map.AddTBInt(GERptAttr.FlowEndNode, 0, "停留节点", true, true);
		map.AddTBFloat(GERptAttr.FlowDaySpan, 0, "跨度(天)", true, true);
		map.AddTBString(GERptAttr.FlowEmps, null, "参与人", false, false, 0, 100, 100);

			//删除信息.
		map.AddTBString(WorkFlowDeleteLogAttr.Oper, null, "删除人员", true, true, 0, 20, 10);
		map.AddTBString(WorkFlowDeleteLogAttr.OperDept, null, "删除人员部门", true, true, 0, 20, 10);
		map.AddTBString(WorkFlowDeleteLogAttr.OperDeptName, null, "删除人员名称", true, true, 0, 200, 10);
		map.AddTBString(WorkFlowDeleteLogAttr.DeleteNote, "", "删除原因", true, true, 0, 4000, 10);
		map.AddTBDateTime(WorkFlowDeleteLogAttr.DeleteDT, null, "删除日期", true, true);

			//查询.
		map.AddSearchAttr(GenerWorkFlowAttr.FK_Dept);
		map.AddSearchAttr(GenerWorkFlowAttr.FK_Flow);


		this.set_enMap(map);
		return this.get_enMap();
	}

}