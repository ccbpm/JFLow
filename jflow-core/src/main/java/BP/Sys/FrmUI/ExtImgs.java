package BP.Sys.FrmUI;

import java.util.*;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

/** 
超连接s
*/
public class ExtImgs extends EntitiesMyPK
{
	/** 
	 超连接s
	*/
	public ExtImgs()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new ExtImg();
	}
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<ExtImg> ToJavaList()
	{
		return (List<ExtImg>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<ExtImg> Tolist()
	{
		ArrayList<ExtImg> list = new ArrayList<ExtImg>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((ExtImg)this.get(i));
		}
		return list;
	}
}
