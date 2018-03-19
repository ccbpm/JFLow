package BP.WF;

import BP.En.Entity;

/** 
 轨迹集合
 
*/
public class Tracks extends BP.En.Entities
{
	/** 
	 轨迹集合
	*/
	public Tracks()
	{
	}
	@Override
	public Entity getGetNewEntity()
	{
		return new Track();
	}
	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<Track> ToJavaList()
	{
		return (java.util.List<Track>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<Track> Tolist()
	{
		java.util.ArrayList<Track> list = new java.util.ArrayList<Track>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Track)this.get(i));
		}
		return list;
	}

}