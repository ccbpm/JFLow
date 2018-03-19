package BP.WF.Rpt;

import BP.En.Entities;
import BP.En.Entity;

/** 
 报表人员 
 
*/
public class RptEmps extends Entities
{

		
	/** 
	 报表与人员集合
	 
	*/
	public RptEmps()
	{
	}

		///#endregion


		///#region 方法
	/** 
	 得到它的 Entity 
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new RptEmp();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<RptEmp> ToJavaList()
	{
		return (java.util.List<RptEmp>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<RptEmp> Tolist()
	{
		java.util.ArrayList<RptEmp> list = new java.util.ArrayList<RptEmp>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((RptEmp)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}