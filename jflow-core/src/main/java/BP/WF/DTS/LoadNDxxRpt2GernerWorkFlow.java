package BP.WF.DTS;

import BP.DA.*;
import BP.Web.Controls.*;
import BP.Port.*;
import BP.En.*;
import BP.Sys.*;
import BP.WF.Data.*;
import BP.WF.*;

/** 
 装载已经完成的流程数据到WF_GenerWorkflow
*/
public class LoadNDxxRpt2GernerWorkFlow extends Method
{
	/** 
	 装载已经完成的流程数据到WF_GenerWorkflow
	*/
	public LoadNDxxRpt2GernerWorkFlow()
	{
		this.Title = "装载已经完成的流程数据到WF_GenerWorkflow（升级扩展流程数据完成模式下的旧数据查询不到的问题）";
		this.Help = "升级扩展流程数据完成模式下的旧数据查询不到的问题。";
		this.GroupName = "流程维护";

	}
	/** 
	 设置执行变量
	 
	 @return 
	*/
	@Override
	public void Init()
	{

	}
	/** 
	 当前的操纵员是否可以执行这个方法
	*/
	@Override
	public boolean getIsCanDo()
	{
		if (WebUser.getNo().equals("admin"))
		{
			return true;
		}
		return false;
	}
	/** 
	 执行
	 
	 @return 返回执行结果
	*/
	@Override
	public Object Do()
	{
		BP.WF.Flows ens = new Flows();
		for (BP.WF.Flow en : ens)
		{
			String sql = "SELECT * FROM " + en.getPTable() + " WHERE OID NOT IN (SELECT WorkID FROM WF_GenerWorkFlow WHERE FK_Flow='" + en.No + "')";
			DataTable dt = DBAccess.RunSQLReturnTable(sql);

			for (DataRow dr : dt.Rows)
			{
				GenerWorkFlow gwf = new GenerWorkFlow();
				gwf.setWorkID(Long.parseLong(dr.get(NDXRptBaseAttr.OID).toString()));
				gwf.setFID(Long.parseLong(dr.get(NDXRptBaseAttr.FID).toString()));

				gwf.setFK_FlowSort(en.getFK_FlowSort());
				gwf.setSysType(en.getSysType());

				gwf.setFK_Flow(en.No);
				gwf.setFlowName(en.Name);
				gwf.setTitle(dr.get(NDXRptBaseAttr.Title).toString());
				gwf.setWFState(WFState.forValue(Integer.parseInt(dr.get(NDXRptBaseAttr.WFState).toString())));
			//    gwf.WFSta = WFSta.Complete;

				gwf.setStarter(dr.get(NDXRptBaseAttr.FlowStarter).toString());
				gwf.setStarterName(dr.get(NDXRptBaseAttr.FlowStarter).toString());
				gwf.setRDT(dr.get(NDXRptBaseAttr.FlowStartRDT).toString());
				gwf.setFK_Node(Integer.parseInt(dr.get(NDXRptBaseAttr.FlowEndNode).toString()));
				gwf.setFK_Dept(dr.get(NDXRptBaseAttr.FK_Dept).toString());

				Port.Dept dept = null;
				try
				{
					dept = new Port.Dept(gwf.getFK_Dept());
					gwf.setDeptName(dept.Name);
				}
				catch (java.lang.Exception e)
				{
					gwf.setDeptName(gwf.getFK_Dept());
				}

				try
				{
					gwf.setPRI(Integer.parseInt(dr.get(NDXRptBaseAttr.PRI).toString()));
				}
				catch (java.lang.Exception e2)
				{

				}

				//  gwf.SDTOfNode = dr[NDXRptBaseAttr.FK_Dept].ToString();
				// gwf.SDTOfFlow = dr[NDXRptBaseAttr.FK_Dept].ToString();

				gwf.setPFlowNo(dr.get(NDXRptBaseAttr.PFlowNo).toString());
				gwf.setPWorkID(Long.parseLong(dr.get(NDXRptBaseAttr.PWorkID).toString()));
				gwf.setPNodeID(Integer.parseInt(dr.get(NDXRptBaseAttr.PNodeID).toString()));
				gwf.setPEmp(dr.get(NDXRptBaseAttr.PEmp).toString());

				//gwf.CFlowNo = dr[NDXRptBaseAttr.CFlowNo].ToString();
				//gwf.CWorkID = Int64.Parse(dr[NDXRptBaseAttr.CWorkID].ToString());

				gwf.setGuestNo(dr.get(NDXRptBaseAttr.GuestNo).toString());
				gwf.setGuestName(dr.get(NDXRptBaseAttr.GuestName).toString());
				gwf.setBillNo(dr.get(NDXRptBaseAttr.BillNo).toString());
				//gwf.FlowNote = dr[NDXRptBaseAttr.flowno].ToString();

				gwf.SetValByKey("Emps", dr.get(NDXRptBaseAttr.FlowEmps).toString());
				//   gwf.AtPara = dr[NDXRptBaseAttr.FK_Dept].ToString();
				//  gwf.GUID = dr[NDXRptBaseAttr.gu].ToString();
				gwf.Insert();
			}

		}
		return "执行成功...";
	}
}