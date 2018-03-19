package BP.Sys.FrmUI;

import java.util.*;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

/** 
超连接s
*/
public class FrmLinks extends EntitiesMyPK
{
	/** 
	 超连接s
	*/
	public FrmLinks()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FrmLink();
	}
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmLink> ToJavaList()
	{
		return (List<FrmLink>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmLink> Tolist()
	{
		ArrayList<FrmLink> list = new ArrayList<FrmLink>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmLink)this.get(i));
		}
		return list;
	}
}
