package BP.WF.Data;

import BP.En.*;
import BP.WF.Template.*;
import BP.Sys.*;
import BP.WF.*;

/** 
 报表基类
*/
public abstract class NDXRptBase extends BP.En.EntityOID
{

		///#region 属性
	/** 
	 工作ID
	 * @throws Exception 
	*/
	public final long getOID() throws Exception
	{
		return this.GetValInt64ByKey(NDXRptBaseAttr.OID);
	}
	public final void setOID(long value) throws Exception
	{
		this.SetValByKey(NDXRptBaseAttr.OID, value);
	}
	/** 
	 流程时间跨度
	 * @throws Exception 
	*/
	public final float getFlowDaySpan() throws Exception
	{
		return this.GetValFloatByKey(NDXRptBaseAttr.FlowDaySpan);
	}
	public final void setFlowDaySpan(float value) throws Exception
	{
		this.SetValByKey(NDXRptBaseAttr.FlowDaySpan, value);
	}
	/** 
	 数量
	*/
	public final int getMyNum()
	{
		return 1;
	}
	/** 
	 主流程ID
	 * @throws Exception 
	*/
	public final long getFID() throws Exception
	{
		return this.GetValInt64ByKey(NDXRptBaseAttr.FID);
	}
	public final void setFID(long value) throws Exception
	{
		this.SetValByKey(NDXRptBaseAttr.FID, value);
	}
	/** 
	 流程参与人员
	 * @throws Exception 
	*/
	public final String getFlowEmps() throws Exception
	{
		return this.GetValStringByKey(NDXRptBaseAttr.FlowEmps);
	}
	public final void setFlowEmps(String value) throws Exception
	{
		this.SetValByKey(NDXRptBaseAttr.FlowEmps, value);
	}
	/** 
	 客户编号
	 * @throws Exception 
	*/
	public final String getGuestNo() throws Exception
	{
		return this.GetValStringByKey(NDXRptBaseAttr.GuestNo);
	}
	public final void setGuestNo(String value) throws Exception
	{
		this.SetValByKey(NDXRptBaseAttr.GuestNo, value);
	}
	/** 
	 客户名称
	 * @throws Exception 
	*/
	public final String getGuestName() throws Exception
	{
		return this.GetValStringByKey(NDXRptBaseAttr.GuestName);
	}
	public final void setGuestName(String value) throws Exception
	{
		this.SetValByKey(NDXRptBaseAttr.GuestName, value);
	}
	/** 
	 单据编号
	 * @throws Exception 
	*/
	public final String getBillNo() throws Exception
	{
		return this.GetValStringByKey(NDXRptBaseAttr.BillNo);
	}
	public final void setBillNo(String value) throws Exception
	{
		this.SetValByKey(NDXRptBaseAttr.BillNo, value);
	}
	/** 
	 流程发起人
	 * @throws Exception 
	*/
	public final String getFlowStarter() throws Exception
	{
		return this.GetValStringByKey(NDXRptBaseAttr.FlowStarter);
	}
	public final void setFlowStarter(String value) throws Exception
	{
		this.SetValByKey(NDXRptBaseAttr.FlowStarter, value);
	}
	/** 
	 流程发起时间
	 * @throws Exception 
	*/
	public final String getFlowStartRDT() throws Exception
	{
		return this.GetValStringByKey(NDXRptBaseAttr.FlowStartRDT);
	}
	public final void setFlowStartRDT(String value) throws Exception
	{
		this.SetValByKey(NDXRptBaseAttr.FlowStartRDT, value);
	}
	/** 
	 流程结束者
	 * @throws Exception 
	*/
	public final String getFlowEnder() throws Exception
	{
		return this.GetValStringByKey(NDXRptBaseAttr.FlowEnder);
	}
	public final void setFlowEnder(String value) throws Exception
	{
		this.SetValByKey(NDXRptBaseAttr.FlowEnder, value);
	}
	/** 
	 流程处理时间
	 * @throws Exception 
	*/
	public final String getFlowEnderRDT() throws Exception
	{
		return this.GetValStringByKey(NDXRptBaseAttr.FlowEnderRDT);
	}
	public final void setFlowEnderRDT(String value) throws Exception
	{
		this.SetValByKey(NDXRptBaseAttr.FlowEnderRDT, value);
	}
	/** 
	 停留节点
	 * @throws Exception 
	*/
	public final String getFlowEndNodeText() throws Exception
	{
		Node nd = new Node(this.getFlowEndNode());
		return nd.getName();
	}
	/** 
	 节点节点ID
	 * @throws Exception 
	*/
	public final int getFlowEndNode() throws Exception
	{
		return this.GetValIntByKey(NDXRptBaseAttr.FlowEndNode);
	}
	public final void setFlowEndNode(int value) throws Exception
	{
		this.SetValByKey(NDXRptBaseAttr.FlowEndNode, value);
	}
	/** 
	 流程标题
	 * @throws Exception 
	*/
	public final String getTitle() throws Exception
	{
		return this.GetValStringByKey(NDXRptBaseAttr.Title);
	}
	public final void setTitle(String value) throws Exception
	{
		this.SetValByKey(NDXRptBaseAttr.Title, value);
	}
	/** 
	 隶属年月
	 * @throws Exception 
	*/
	public final String getFK_NY() throws Exception
	{
		return this.GetValStringByKey(NDXRptBaseAttr.FK_NY);
	}
	public final void setFK_NY(String value) throws Exception
	{
		this.SetValByKey(NDXRptBaseAttr.FK_NY, value);
	}
	/** 
	 发起人部门
	 * @throws Exception 
	*/
	public final String getFK_Dept() throws Exception
	{
		return this.GetValStringByKey(NDXRptBaseAttr.FK_Dept);
	}
	public final void setFK_Dept(String value) throws Exception
	{
		this.SetValByKey(NDXRptBaseAttr.FK_Dept, value);
	}
	/** 
	 流程状态
	 * @throws Exception 
	*/
	public final WFState getWFState() throws Exception
	{
		return WFState.forValue(this.GetValIntByKey(NDXRptBaseAttr.WFState));
	}
	public final void setWFState(WFState value) throws Exception
	{
		this.SetValByKey(NDXRptBaseAttr.WFState, value.getValue());
	}
	/** 
	 状态名称
	 * @throws Exception 
	*/
	public final String getWFStateText() throws Exception
	{
		switch (this.getWFState())
		{
			case Complete:
				return "已完成";
			case Delete:
				return "已删除";
			default:
				return "运行中";
		}
	}
	/** 
	 父流程WorkID
	 * @throws Exception 
	*/
	public final long getPWorkID() throws Exception
	{
		return this.GetValInt64ByKey(NDXRptBaseAttr.PWorkID);
	}
	public final void setPWorkID(long value) throws Exception
	{
		this.SetValByKey(NDXRptBaseAttr.PWorkID, value);
	}
	/** 
	 父流程流程编号
	 * @throws Exception 
	*/
	public final String getPFlowNo() throws Exception
	{
		return this.GetValStringByKey(NDXRptBaseAttr.PFlowNo);
	}
	public final void setPFlowNo(String value) throws Exception
	{
		this.SetValByKey(NDXRptBaseAttr.PFlowNo, value);
	}
	/** 
	 PNodeID
	 * @throws Exception 
	*/
	public final String getPNodeID() throws Exception
	{
		return this.GetValStringByKey(NDXRptBaseAttr.PNodeID);
	}
	public final void setPNodeID(String value) throws Exception
	{
		this.SetValByKey(NDXRptBaseAttr.PNodeID, value);
	}
	public final String getPEmp() throws Exception
	{
		return this.GetValStringByKey(NDXRptBaseAttr.PEmp);
	}
	public final void setPEmp(String value) throws Exception
	{
		this.SetValByKey(NDXRptBaseAttr.PEmp, value);
	}

		///#endregion attrs


		///#region 构造
	/** 
	 构造
	*/
	protected NDXRptBase()
	{
	}
	/** 
	 根据OID构造实体
	 
	 @param 工作ID workid
	 * @throws Exception 
	*/
	protected NDXRptBase(int workid) throws Exception
	{
		super(workid);
	}

		///#endregion 构造
}