package bp.ccbill;

import bp.da.*;
import bp.en.*;
import bp.sys.*;
import bp.wf.template.*;
import bp.wf.*;
import bp.ccbill.template.*;
import java.util.*;

/** 
 轨迹集合s
*/
public class Tracks extends EntitiesMyPK
{

		///构造方法.
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

		/// 构造方法.


		///为了适应自动翻译成java的需要,把实体转换成List.
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

		/// 为了适应自动翻译成java的需要,把实体转换成List.

}