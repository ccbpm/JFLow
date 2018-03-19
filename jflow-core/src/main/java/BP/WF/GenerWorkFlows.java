package BP.WF;

import BP.DA.DBAccess;
import BP.DA.DataTable;
import BP.En.Entities;
import BP.En.Entity;

/** 
 流程实例s
 
*/
public class GenerWorkFlows extends Entities
{
	/** 
	 根据工作流程,工作人员 ID 查询出来他当前的能做的工作.
	 
	 @param flowNo 流程编号
	 @param empId 工作人员ID
	 @return 
	*/
	public static DataTable QuByFlowAndEmp(String flowNo, int empId)
	{
		String sql="SELECT a.WorkID FROM WF_GenerWorkFlow a, WF_GenerWorkerlist b WHERE a.WorkID=b.WorkID   AND b.FK_Node=a.FK_Node  AND b.FK_Emp='"+(new Integer(empId)).toString()+"' AND a.FK_Flow='"+flowNo+"'";
		return DBAccess.RunSQLReturnTable(sql);
	}


		///#region 方法
	/** 
	 得到它的 Entity 
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new GenerWorkFlow();
	}
	/** 
	 流程实例集合
	 
	*/
	public GenerWorkFlows()
	{
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<GenerWorkFlow> ToJavaList()
	{
		return (java.util.List<GenerWorkFlow>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<GenerWorkFlow> Tolist()
	{
		java.util.ArrayList<BP.WF.GenerWorkFlow> list = new java.util.ArrayList<BP.WF.GenerWorkFlow>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((BP.WF.GenerWorkFlow)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}