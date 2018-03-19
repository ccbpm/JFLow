package BP.WF.Data;

import BP.En.Entities;
import BP.En.Entity;

/** 
 我参与的流程s
*/
public class MyFlows extends Entities
{
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MyFlow();
	}
	/** 
	 我参与的流程集合
	*/
	public MyFlows()
	{
	}

	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<MyFlow> ToJavaList()
	{
		return (java.util.List<MyFlow>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<MyFlow> Tolist()
	{
		java.util.ArrayList<MyFlow> list = new java.util.ArrayList<MyFlow>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MyFlow)this.get(i));
		}
		return list;
	}
}