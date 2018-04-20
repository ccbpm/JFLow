package BP.WF.Port;

import BP.En.EntitiesNoName;
import BP.En.Entity;

/** 
 操作员s 
*/
public class WFEmps extends EntitiesNoName
{

		
	/** 
	 操作员s
	*/
	public WFEmps()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new WFEmp();
	}

	@Override
	public int RetrieveAll() throws Exception
	{
		return super.RetrieveAll("FK_Dept","Idx");
	}
	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<WFEmp> ToJavaList()
	{
		return (java.util.List<WFEmp>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<WFEmp> Tolist()
	{
		java.util.ArrayList<WFEmp> list = new java.util.ArrayList<WFEmp>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((WFEmp)this.get(i));
		}
		return list;
	}

}