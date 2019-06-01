package BP.Sys.FrmUI;

import java.util.*;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

/** 
超连接s
*/
public class ExtHandWritings extends EntitiesMyPK
{
	/** 
	 超连接s
	*/
	public ExtHandWritings()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new ExtHandWriting();
	}
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<ExtHandWriting> ToJavaList()
	{
		return (List<ExtHandWriting>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<ExtHandWriting> Tolist()
	{
		ArrayList<ExtHandWriting> list = new ArrayList<ExtHandWriting>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((ExtHandWriting)this.get(i));
		}
		return list;
	}
}
