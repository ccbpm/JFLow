package BP.WF;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.WF.*;
import BP.Port.*;
import BP.WF.Data.*;
import java.util.*;

/** 
 附件未读日志
*/
public class AthUnReadLog extends EntityMyPK
{

		///#region 基本属性
	/** 
	 工作ID
	 * @throws Exception 
	*/
	public final long getWorkID() throws Exception
	{
		return this.GetValInt64ByKey(AthUnReadLogAttr.WorkID);
	}
	public final void setWorkID(long value) throws Exception
	{
		SetValByKey(AthUnReadLogAttr.WorkID, value);
	}
	/** 
	 操作人
	 * @throws Exception 
	*/
	public final String getFK_Emp() throws Exception
	{
		return this.GetValStringByKey(AthUnReadLogAttr.FK_Emp);
	}
	public final void setFK_Emp(String value) throws Exception
	{
		SetValByKey(AthUnReadLogAttr.FK_Emp, value);
	}
	/** 
	 删除人员
	 * @throws Exception 
	*/
	public final String getFK_EmpDept() throws Exception
	{
		return this.GetValStringByKey(AthUnReadLogAttr.FK_EmpDept);
	}
	public final void setFK_EmpDept(String value) throws Exception
	{
		SetValByKey(AthUnReadLogAttr.FK_EmpDept, value);
	}
	public final String getFK_EmpDeptName() throws Exception
	{
		return this.GetValStringByKey(AthUnReadLogAttr.FK_EmpDeptName);
	}
	public final void setFK_EmpDeptName(String value) throws Exception
	{
		SetValByKey(AthUnReadLogAttr.FK_EmpDeptName, value);
	}
	public final String getBeiZhu() throws Exception
	{
		return this.GetValStringByKey(AthUnReadLogAttr.BeiZhu);
	}
	public final void setBeiZhu(String value) throws Exception
	{
		SetValByKey(AthUnReadLogAttr.BeiZhu, value);
	}
	public final String getBeiZhuHtml() throws Exception
	{
		return this.GetValHtmlStringByKey(AthUnReadLogAttr.BeiZhu);
	}
	/** 
	 记录日期
	 * @throws Exception 
	*/
	public final String getSendDT() throws Exception
	{
		return this.GetValStringByKey(AthUnReadLogAttr.SendDT);
	}
	public final void setSendDT(String value) throws Exception
	{
		SetValByKey(AthUnReadLogAttr.SendDT, value);
	}
	/** 
	 流程编号
	 * @throws Exception 
	*/
	public final String getFK_Flow() throws Exception
	{
		return this.GetValStringByKey(AthUnReadLogAttr.FK_Flow);
	}
	public final void setFK_Flow(String value) throws Exception
	{
		SetValByKey(AthUnReadLogAttr.FK_Flow, value);
	}
	/** 
	 流程类别
	 * @throws Exception 
	*/
	public final String getFlowName() throws Exception
	{
		return this.GetValStringByKey(AthUnReadLogAttr.FlowName);
	}
	public final void setFlowName(String value) throws Exception
	{
		SetValByKey(AthUnReadLogAttr.FlowName, value);
	}
	public final int getFK_Node() throws Exception
	{
		return this.GetValIntByKey(AthUnReadLogAttr.FK_Node);
	}
	public final void setFK_Node(int value) throws Exception
	{
		SetValByKey(AthUnReadLogAttr.FK_Node, value);
	}
	/** 
	 节点名称
	 * @throws Exception 
	*/
	public final String getNodeName() throws Exception
	{
		return this.GetValStringByKey(AthUnReadLogAttr.NodeName);
	}
	public final void setNodeName(String value) throws Exception
	{
		SetValByKey(AthUnReadLogAttr.NodeName, value);
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
	 附件未读日志
	*/
	public AthUnReadLog()
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

		Map map = new Map("WF_AthUnReadLog", "附件未读日志");

			// 流程基础数据。
		map.AddMyPK(false);
		map.AddDDLEntities(GenerWorkFlowAttr.FK_Dept, null, "部门", new BP.WF.Port.Depts(), false);
		map.AddTBString(GenerWorkFlowAttr.Title, null, "标题", true, true, 0, 100, 100);
		map.AddTBInt(GenerWorkFlowAttr.WorkID, 0, "WorkID", false, false);
		map.AddTBString(GERptAttr.FlowStarter, null, "发起人", true, true, 0, 100, 100);
		map.AddTBDateTime(GERptAttr.FlowStartRDT, null, "发起时间", true, true);
		   // map.AddDDLEntities(GenerWorkFlowAttr.FK_NY, null, "年月", new BP.Pub.NYs(), false);
		map.AddDDLEntities(GenerWorkFlowAttr.FK_Flow, null, "流程", new Flows(), false);


		map.AddTBInt(AthUnReadLogAttr.FK_Node, 0, "节点ID", true, true);
		map.AddTBString(AthUnReadLogAttr.NodeName, null, "节点名称", true, true, 0, 20, 10);

			//删除信息.
		map.AddTBString(AthUnReadLogAttr.FK_Emp, null, "人员", true, true, 0, 20, 10);
		map.AddTBString(AthUnReadLogAttr.FK_EmpDept, null, "人员部门", true, true, 0, 20, 10);
		map.AddTBString(AthUnReadLogAttr.FK_EmpDeptName, null, "人员名称", true, true, 0, 200, 10);
		map.AddTBString(AthUnReadLogAttr.BeiZhu, "", "内容", true, true, 0, 4000, 10);
		map.AddTBDateTime(AthUnReadLogAttr.SendDT, null, "日期", true, true);

			//查询.
		map.AddSearchAttr(GenerWorkFlowAttr.FK_Dept);
		  //  map.AddSearchAttr(GenerWorkFlowAttr.FK_NY);
		map.AddSearchAttr(GenerWorkFlowAttr.FK_Flow);

		   // map.AddHidden(FlowDataAttr.FlowEmps, " LIKE ", "'%@@WebUser.No%'");

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}