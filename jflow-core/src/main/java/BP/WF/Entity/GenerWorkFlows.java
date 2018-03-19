package BP.WF.Entity;

import java.util.ArrayList;
import java.util.List;

import BP.DA.DBAccess;
import BP.DA.DataTable;
import BP.En.Entities;
import BP.En.Entity;
import BP.Sys.FrmRpt;

/**
 * 流程实例s
 */
public class GenerWorkFlows extends Entities
{
	public static ArrayList<GenerWorkFlow> convertGenerWorkFlows(Object obj)
	{
		return (ArrayList<GenerWorkFlow>) obj;
	}
	public List<GenerWorkFlow> ToJavaList()
	{
		return (List<GenerWorkFlow>)(Object)this;
	}
	/**
	 * 根据工作流程,工作人员 ID 查询出来他当前的能做的工作.
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param empId
	 *            工作人员ID
	 * @return
	 */
	public static DataTable QuByFlowAndEmp(String flowNo, int empId)
	{
		String sql = "SELECT a.WorkID FROM WF_GenerWorkFlow a, WF_GenerWorkerlist b WHERE a.WorkID=b.WorkID   AND b.FK_Node=a.FK_Node  AND b.FK_Emp='"
				+ (new Integer(empId)).toString()
				+ "' AND a.FK_Flow='"
				+ flowNo + "'";
		return DBAccess.RunSQLReturnTable(sql);
	}
	
	//  方法
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new GenerWorkFlow();
	}
	
	/**
	 * 流程实例集合
	 */
	public GenerWorkFlows()
	{
	}
}