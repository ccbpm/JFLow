package BP.Sys.FrmUI;

import java.util.*;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

/** 
超连接s
*/
public class ExtLinks extends EntitiesMyPK
{
	/** 
	 超连接s
	*/
	public ExtLinks()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new ExtLink();
	}
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<ExtLink> ToJavaList()
	{
		return (List<ExtLink>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<ExtLink> Tolist()
	{
		ArrayList<ExtLink> list = new ArrayList<ExtLink>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((ExtLink)this.get(i));
		}
		return list;
	}
}
