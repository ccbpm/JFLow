package BP.WF;

import BP.DA.*;
import BP.En.*;

/** 
 产生分合流程控制s
 
*/
public class GenerFHs extends Entities
{
	/** 
	 根据工作流程,工作人员ID 查询出来他当前的能做的工作.
	 
	 @param flowNo 流程编号
	 @param empId 工作人员ID
	 @return 
	*/
	public static DataTable QuByFlowAndEmp(String flowNo, int empId)
	{
		String sql="SELECT a.FID FROM WF_GenerFH a, WF_GenerWorkerlist b WHERE a.FID=b.FID   AND b.FK_Node=a.FK_Node  AND b.FK_Emp='"+(new Integer(empId)).toString()+"' AND a.FK_Flow='"+flowNo+"'";
		return DBAccess.RunSQLReturnTable(sql);
	}


		///#region 方法
	/** 
	 得到它的 Entity 
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new GenerFH();
	}
	/** 
	 产生工作流程集合
	 
	*/
	public GenerFHs()
	{
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<GenerFH> ToJavaList()
	{
		return (java.util.List<GenerFH>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<GenerFH> Tolist()
	{
		java.util.ArrayList<GenerFH> list = new java.util.ArrayList<GenerFH>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((GenerFH)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}