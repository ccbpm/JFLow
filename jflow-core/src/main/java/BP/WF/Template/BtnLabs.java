package BP.WF.Template;

import BP.En.Entities;
import BP.En.Entity;

/** 
 节点按钮权限s
 
*/
public class BtnLabs extends Entities
{
	/** 
	 节点按钮权限s
	*/
	public BtnLabs()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new BtnLab();
	}
	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<BtnLab> ToJavaList()
	{
		return (java.util.List<BtnLab>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<BtnLab> Tolist()
	{
		java.util.ArrayList<BtnLab> list = new java.util.ArrayList<BtnLab>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((BtnLab)this.get(i));
		}
		return list;
	}
}