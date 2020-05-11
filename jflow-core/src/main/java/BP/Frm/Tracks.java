package BP.Frm;

import BP.En.*;

import java.util.*;

/**
 轨迹集合s
 */
public class Tracks extends BP.En.EntitiesMyPK
{
	///#region 构造方法.
	/**
	 轨迹集合
	 */
	public Tracks()
	{
	}
	@Override
	public Entity getNewEntity()
	{
		return new Track();
	}
	/**
	 转化成 java list,C#不能调用.

	 @return List
	 */
	public final List<Track> ToJavaList()
	{
		return (List<Track>)(Object)this;
	}
	/**
	 转化成list

	 @return List
	 */
	public final ArrayList<Track> Tolist()
	{
		ArrayList<Track> list = new ArrayList<Track>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Track)this.get(i));
		}
		return list;
	}

}