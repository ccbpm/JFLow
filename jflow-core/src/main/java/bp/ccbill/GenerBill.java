package bp.ccbill;

import bp.da.*; 
import bp.en.*;
import bp.en.Map;
import bp.wf.*;
import bp.wf.template.*;
import bp.sys.*;
import bp.web.WebUser;
import bp.ccbill.template.*;
import java.util.*;

/** 
 单据控制表
*/
public class GenerBill extends Entity
{

		///属性
	/** 
	 主键
	*/
	@Override
	public String getPK()
	{
		return GenerBillAttr.WorkID;
	}
	/** 
	 备注
	 * @throws Exception 
	*/
	public final String getFlowNote() throws Exception
	{
		return this.GetValStrByKey(GenerBillAttr.FlowNote);
	}
	public final void setFlowNote(String value) throws Exception
	{
		SetValByKey(GenerBillAttr.FlowNote, value);
	}

	/** 
	 BillNo
	 * @throws Exception 
	*/
	public final String getBillNo() throws Exception
	{
		return this.GetValStrByKey(GenerBillAttr.BillNo);
	}
	public final void setBillNo(String value) throws Exception
	{
		SetValByKey(GenerBillAttr.BillNo, value);
	}
	/** 
	 单据ID
	 * @throws Exception 
	*/
	public final String getFrmID() throws Exception
	{
		return this.GetValStrByKey(GenerBillAttr.FrmID);
	}
	public final void setFrmID(String value) throws Exception
	{
		SetValByKey(GenerBillAttr.FrmID, value);
	}
	/** 
	 单据单据
	 * @throws Exception 
	*/
	public final String getFrmName() throws Exception
	{
		return this.GetValStrByKey(GenerBillAttr.FrmName);
	}
	public final void setFrmName(String value) throws Exception
	{
		SetValByKey(GenerBillAttr.FrmName, value);
	}
	/** 
	 优先级
	 * @throws Exception 
	*/
	public final int getPRI() throws Exception
	{
		return this.GetValIntByKey(GenerBillAttr.PRI);
	}
	public final void setPRI(int value) throws Exception
	{
		SetValByKey(GenerBillAttr.PRI, value);
	}
	/** 
	 待办人员数量
	 * @throws Exception 
	*/
	public final int getTodoEmpsNum() throws Exception
	{
		return this.GetValIntByKey(GenerBillAttr.TodoEmpsNum);
	}
	public final void setTodoEmpsNum(int value) throws Exception
	{
		SetValByKey(GenerBillAttr.TodoEmpsNum, value);
	}
	/** 
	 待办人员列表
	 * @throws Exception 
	*/
	public final String getTodoEmps() throws Exception
	{
		return this.GetValStrByKey(GenerBillAttr.TodoEmps);
	}
	public final void setTodoEmps(String value) throws Exception
	{
		SetValByKey(GenerBillAttr.TodoEmps, value);
	}
	/** 
	 参与人
	 * @throws Exception 
	*/
	public final String getEmps() throws Exception
	{
		return this.GetValStrByKey(GenerBillAttr.Emps);
	}
	public final void setEmps(String value) throws Exception
	{
		SetValByKey(GenerBillAttr.Emps, value);
	}
	/** 
	 状态
	 * @throws Exception 
	*/
	public final TaskSta getTaskSta() throws Exception
	{
		return TaskSta.forValue(this.GetValIntByKey(GenerBillAttr.TaskSta));
	}
	public final void setTaskSta(TaskSta value) throws Exception
	{
		SetValByKey(GenerBillAttr.TaskSta, value.getValue());
	}
	/** 
	 类别编号
	 * @throws Exception 
	*/
	public final String getFK_FrmTree() throws Exception
	{
		return this.GetValStrByKey(GenerBillAttr.FK_FrmTree);
	}
	public final void setFK_FrmTree(String value) throws Exception
	{
		SetValByKey(GenerBillAttr.FK_FrmTree, value);
	}
	/** 
	 部门编号
	 * @throws Exception 
	*/
	public final String getFK_Dept() throws Exception
	{
		return this.GetValStrByKey(GenerBillAttr.FK_Dept);
	}
	public final void setFK_Dept(String value) throws Exception
	{
		SetValByKey(GenerBillAttr.FK_Dept, value);
	}
	/** 
	 标题
	 * @throws Exception 
	*/
	public final String getTitle() throws Exception
	{
		return this.GetValStrByKey(GenerBillAttr.Title);
	}
	public final void setTitle(String value) throws Exception
	{
		SetValByKey(GenerBillAttr.Title, value);
	}
	/** 
	 客户编号
	 * @throws Exception 
	*/
	public final String getGuestNo() throws Exception
	{
		return this.GetValStrByKey(GenerBillAttr.GuestNo);
	}
	public final void setGuestNo(String value) throws Exception
	{
		SetValByKey(GenerBillAttr.GuestNo, value);
	}
	/** 
	 客户名称
	 * @throws Exception 
	*/
	public final String getGuestName() throws Exception
	{
		return this.GetValStrByKey(GenerBillAttr.GuestName);
	}
	public final void setGuestName(String value) throws Exception
	{
		SetValByKey(GenerBillAttr.GuestName, value);
	}
	/** 
	 产生时间
	 * @throws Exception 
	*/
	public final String getRDT() throws Exception
	{
		return this.GetValStrByKey(GenerBillAttr.RDT);
	}
	public final void setRDT(String value) throws Exception
	{
		SetValByKey(GenerBillAttr.RDT, value);
	}
	/** 
	 节点应完成时间
	 * @throws Exception 
	*/
	public final String getSDTOfNode() throws Exception
	{
		return this.GetValStrByKey(GenerBillAttr.SDTOfNode);
	}
	public final void setSDTOfNode(String value) throws Exception
	{
		SetValByKey(GenerBillAttr.SDTOfNode, value);
	}
	/** 
	 单据应完成时间
	 * @throws Exception 
	*/
	public final String getSDTOfFlow() throws Exception
	{
		return this.GetValStrByKey(GenerBillAttr.SDTOfFlow);
	}
	public final void setSDTOfFlow(String value) throws Exception
	{
		SetValByKey(GenerBillAttr.SDTOfFlow, value);
	}
	/** 
	 单据ID
	 * @throws Exception 
	*/
	public final long getWorkID() throws Exception
	{
		return this.GetValInt64ByKey(GenerBillAttr.WorkID);
	}
	public final void setWorkID(long value)  throws Exception
	{
		SetValByKey(GenerBillAttr.WorkID, value);
	}
	/** 
	 主线程ID
	 * @throws Exception 
	*/
	public final long getFID() throws Exception
	{
		return this.GetValInt64ByKey(GenerBillAttr.FID);
	}
	public final void setFID(long value) throws Exception
	{
		SetValByKey(GenerBillAttr.FID, value);
	}
	/** 
	 父节点单据编号.
	 * @throws Exception 
	*/
	public final long getPWorkID() throws Exception
	{
		return this.GetValInt64ByKey(GenerBillAttr.PWorkID);
	}
	public final void setPWorkID(long value) throws Exception
	{
		SetValByKey(GenerBillAttr.PWorkID, value);
	}
	/** 
	 父单据调用的节点
	 * @throws Exception 
	*/
	public final int getPNodeID() throws Exception
	{
		return this.GetValIntByKey(GenerBillAttr.PNodeID);
	}
	public final void setPNodeID(int value) throws Exception
	{
		SetValByKey(GenerBillAttr.PNodeID, value);
	}
	/** 
	 PFrmID
	 * @throws Exception 
	*/
	public final String getPFrmID() throws Exception
	{
		return this.GetValStrByKey(GenerBillAttr.PFrmID);
	}
	public final void setPFrmID(String value) throws Exception
	{
		SetValByKey(GenerBillAttr.PFrmID, value);
	}
	/** 
	 吊起子单据的人员
	 * @throws Exception 
	*/
	public final String getPEmp() throws Exception
	{
		return this.GetValStrByKey(GenerBillAttr.PEmp);
	}
	public final void setPEmp(String value) throws Exception
	{
		SetValByKey(GenerBillAttr.PEmp, value);
	}
	/** 
	 发起人
	 * @throws Exception 
	*/
	public final String getStarter() throws Exception
	{
		return this.GetValStrByKey(GenerBillAttr.Starter);
	}
	public final void setStarter(String value) throws Exception
	{
		SetValByKey(GenerBillAttr.Starter, value);
	}
	/** 
	 发起人名称
	 * @throws Exception 
	*/
	public final String getStarterName() throws Exception
	{
		return this.GetValStrByKey(GenerBillAttr.StarterName);
	}
	public final void setStarterName(String value) throws Exception
	{
		this.SetValByKey(GenerBillAttr.StarterName, value);
	}
	/** 
	 发起人部门名称
	 * @throws Exception 
	*/
	public final String getDeptName()  throws Exception
	{
		return this.GetValStrByKey(GenerBillAttr.DeptName);
	}
	public final void setDeptName(String value) throws Exception
	{
		this.SetValByKey(GenerBillAttr.DeptName, value);
	}
	/** 
	 当前节点名称
	 * @throws Exception 
	*/
	public final String getNDStepName() throws Exception
	{
		return this.GetValStrByKey(GenerBillAttr.NDStepName);
	}
	public final void setNDStepName(String value) throws Exception
	{
		this.SetValByKey(GenerBillAttr.NDStepName, value);
	}
	/** 
	 当前工作到的节点
	 * @throws Exception 
	*/
	public final int getNDStep() throws Exception
	{
		return this.GetValIntByKey(GenerBillAttr.NDStep);
	}
	public final void setNDStep(int value) throws Exception
	{
		SetValByKey(GenerBillAttr.NDStep, value);
	}
	/** 
	 工作单据状态
	 * @throws Exception 
	*/
	public final BillState getBillState() throws Exception
	{
		return BillState.forValue(this.GetValIntByKey(GenerBillAttr.BillState));
	}
	public final void setBillState(BillState value) throws Exception
	{
		SetValByKey(GenerBillAttr.BillState, value.getValue());
	}
	/** 
	 单据状态
	 * @throws Exception 
	*/
	public final String getBillStateText() throws Exception
	{
		return this.GetValRefTextByKey(GenerBillAttr.BillState);
	}
	/** 
	 GUID
	 * @throws Exception 
	*/
	public final String getGUID() throws Exception
	{
		return this.GetValStrByKey(GenerBillAttr.GUID);
	}
	public final void setGUID(String value) throws Exception
	{
		SetValByKey(GenerBillAttr.GUID, value);
	}

		///


		///权限控制.
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		if (WebUser.getNo().equals("admin") == true)
		{
			uac.IsDelete = false;
			uac.IsUpdate = true;
			return uac;
		}
		uac.Readonly();
		return uac;
	}

		/// 权限控制.


		///构造方法
	/** 
	 单据控制表
	*/
	public GenerBill()
	{
	}
	/** 
	 单据控制表
	 
	 @param workid
	 * @throws Exception 
	*/
	public GenerBill(long workid) throws Exception
	{
		this.setWorkID(workid);
		this.Retrieve();
	}
	/** 
	 EnMap
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Frm_GenerBill", "单据控制表");


		map.AddTBIntPK(GenerBillAttr.WorkID, 0, "WorkID", true, true);

		map.AddTBString(GenerBillAttr.FK_FrmTree, null, "单据类别", true, false, 0, 10, 10);
		map.AddTBString(GenerBillAttr.FrmID, null, "单据ID", true, false, 0, 100, 10);
		map.AddTBString(GenerBillAttr.FrmName, null, "单据名称", true, false, 0, 200, 10);

		map.AddTBString(GenerBillAttr.BillNo, null, "单据编号", true, false, 0, 100, 10);
		map.AddTBString(GenerBillAttr.Title, null, "标题", true, false, 0, 1000, 10);
		map.AddDDLSysEnum(GenerBillAttr.BillSta, 0, "状态(简)", true, false, GenerBillAttr.BillSta, "@0=运行中@1=已完成@2=其他");
		map.AddDDLSysEnum(GenerBillAttr.BillState, 0, "单据状态", true, false, GenerBillAttr.BillState, "@0=空白@1=草稿@2=编辑中@100=归档");

		map.AddTBString(GenerBillAttr.Starter, null, "创建人", true, false, 0, 200, 10);
		map.AddTBString(GenerBillAttr.StarterName, null, "创建人名称", true, false, 0, 200, 10);
		map.AddTBString(GenerBillAttr.Sender, null, "发送人", true, false, 0, 200, 10);

		map.AddTBDateTime(GenerBillAttr.RDT, "记录日期", true, true);
		map.AddTBDateTime(GenerBillAttr.SendDT, "单据活动时间", true, true);
		map.AddTBInt(GenerBillAttr.NDStep, 0, "步骤", true, false);
		map.AddTBString(GenerBillAttr.NDStepName, null, "步骤名称", true, false, 0, 100, 10);

		map.AddTBString(GenerBillAttr.FK_Dept, null, "部门", true, false, 0, 100, 10);
		map.AddTBString(GenerBillAttr.DeptName, null, "部门名称", true, false, 0, 100, 10);
		map.AddTBInt(GenerBillAttr.PRI, 1, "优先级", true, true);

		map.AddTBDateTime(GenerBillAttr.SDTOfNode, "节点应完成时间", true, true);
		map.AddTBDateTime(GenerBillAttr.SDTOfFlow, "单据应完成时间", true, true);

			//父子单据信息.
		map.AddTBString(GenerBillAttr.PFrmID, null, "父单据编号", true, false, 0, 3, 10);
		map.AddTBInt(GenerBillAttr.PWorkID, 0, "父单据ID", true, true);
		map.AddDDLSysEnum(GenerBillAttr.TSpan, 0, "时间段", true, false, GenerBillAttr.TSpan, "@0=本周@1=上周@2=上上周@3=更早");

			//参数.
		map.AddTBString(GenerBillAttr.AtPara, null, "参数(单据运行设置临时存储的参数)", true, false, 0, 2000, 10);
		map.AddTBString(GenerBillAttr.Emps, null, "参与人", true, false, 0, 4000, 10);
		map.AddTBString(GenerBillAttr.GUID, null, "GUID", false, false, 0, 36, 10);
		map.AddTBString(GenerBillAttr.FK_NY, null, "年月", false, false, 0, 7, 7);


		this.set_enMap(map);
		return this.get_enMap();
	}

		///


		///方法操作.

		/// 方法操作.
}