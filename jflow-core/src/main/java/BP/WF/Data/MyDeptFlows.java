package BP.WF.Data;

import BP.En.Entities;
import BP.En.Entity;
/** 
 我部门的流程s
*/
public class MyDeptFlows extends Entities
{

	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MyDeptFlow();
	}
	/** 
	 我部门的流程集合
	 
	*/
	public MyDeptFlows()
	{
	}
	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<MyDeptFlow> ToJavaList()
	{
		return (java.util.List<MyDeptFlow>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<MyDeptFlow> Tolist()
	{
		java.util.ArrayList<MyDeptFlow> list = new java.util.ArrayList<MyDeptFlow>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MyDeptFlow)this.get(i));
		}
		return list;
	}
}