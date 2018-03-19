package BP.WF.Data;

import BP.DA.DBAccess;
import BP.DA.DataTable;
import BP.En.Entities;
import BP.En.Entity;

/** 
 流程实例s
*/
public class GenerWorkFlowViews extends Entities
{
	/** 
	 根据工作流程,工作人员 ID 查询出来他当前的能做的工作.
	 @param flowNo 流程编号
	 @param empId 工作人员ID
	 @return 
	*/
	public static DataTable QuByFlowAndEmp(String flowNo, int empId)
	{
		String sql="SELECT a.WorkID FROM WF_GenerWorkFlowView a, WF_GenerWorkerlist b WHERE a.WorkID=b.WorkID   AND b.FK_Node=a.FK_Node  AND b.FK_Emp='"+(new Integer(empId)).toString()+"' AND a.FK_Flow='"+flowNo+"'";
		return DBAccess.RunSQLReturnTable(sql);
	}

	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new GenerWorkFlowView();
	}
	/** 
	 流程实例集合
	*/
	public GenerWorkFlowViews()
	{
	}
	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<GenerWorkFlowView> ToJavaList()
	{
		return (java.util.List<GenerWorkFlowView>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<GenerWorkFlowView> Tolist()
	{
		java.util.ArrayList<GenerWorkFlowView> list = new java.util.ArrayList<GenerWorkFlowView>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((GenerWorkFlowView)this.get(i));
		}
		return list;
	}
}