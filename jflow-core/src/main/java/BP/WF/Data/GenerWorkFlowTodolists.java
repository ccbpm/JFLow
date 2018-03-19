package BP.WF.Data;

import java.util.ArrayList;
import java.util.List;

import BP.DA.DBAccess;
import BP.DA.DataTable;
import BP.En.Entities;
import BP.En.Entity;

/** 
 流程统计s
*/
public class GenerWorkFlowTodolists extends Entities
{
	/** 
	 根据工作流程,工作人员 ID 查询出来他当前的能做的工作.
	 
	 @param flowNo 流程编号
	 @param empId 工作人员ID
	 @return 
	*/
	public static DataTable QuByFlowAndEmp(String flowNo, int empId)
	{
		String sql = "SELECT a.WorkID FROM WF_GenerWorkFlowTodolist a, WF_GenerWorkerlist b WHERE a.WorkID=b.WorkID   AND b.FK_Node=a.FK_Node  AND b.FK_Emp='" + String.valueOf(empId) + "' AND a.FK_Flow='" + flowNo + "'";
		return DBAccess.RunSQLReturnTable(sql);
	}


		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new GenerWorkFlowTodolist();
	}
	/** 
	 流程统计集合
	*/
	public GenerWorkFlowTodolists()
	{
	}
		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<GenerWorkFlowTodolist> ToJavaList()
	{
		return (List<GenerWorkFlowTodolist>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<GenerWorkFlowTodolist> Tolist()
	{
		ArrayList<GenerWorkFlowTodolist> list = new ArrayList<GenerWorkFlowTodolist>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((GenerWorkFlowTodolist)this.get(i));
		}
		return list;
	}
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}