package bp.ta;

import bp.en.EntitiesMyPK;
import bp.en.Entity;

import java.util.*;

/** 
 轨迹s
*/
public class Tracks extends EntitiesMyPK
{
		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getNewEntity()
	{
		return new Track();
	}
	/** 
	 轨迹
	*/
	public Tracks()
	{
	}
		///#endregion

		///#region 为了适应自动翻译成java的需要,把实体转换成List.
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
	public final ArrayList<Track> Tolist()
	{
		ArrayList<Track> list = new ArrayList<Track>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Track)this.get(i));
		}
		return list;
	}
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
