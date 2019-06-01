package BP.WF.Template;

import java.util.*;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

/** 
超连接s
*/
public class ExtJobSchedules extends EntitiesMyPK
{
	/** 
	 超连接s
	*/
	public ExtJobSchedules()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new ExtJobSchedule();
	}
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<ExtJobSchedule> ToJavaList()
	{
		return (List<ExtJobSchedule>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<ExtJobSchedule> Tolist()
	{
		ArrayList<ExtJobSchedule> list = new ArrayList<ExtJobSchedule>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((ExtJobSchedule)this.get(i));
		}
		return list;
	}
}
