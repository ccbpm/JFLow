package BP.WF.Entity;

import BP.DA.DBAccess;
import BP.DA.DataTable;
import BP.En.Entities;
import BP.En.Entity;

/**
 * 产生分合流程控制s
 */
public class GenerFHs extends Entities
{
	/**
	 * 根据工作流程,工作人员ID 查询出来他当前的能做的工作.
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param empId
	 *            工作人员ID
	 * @return
	 */
	public static DataTable QuByFlowAndEmp(String flowNo, int empId)
	{
		String sql = "SELECT a.FID FROM WF_GenerFH a, WF_GenerWorkerlist b WHERE a.FID=b.FID   AND b.FK_Node=a.FK_Node  AND b.FK_Emp='"
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
		return new GenerFH();
	}
	
	/**
	 * 产生工作流程集合
	 */
	public GenerFHs()
	{
	}
}