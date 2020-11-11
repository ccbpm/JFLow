package bp.port;

import bp.en.*;
import java.util.*;

/** 
 用户组s
*/
public class Teams extends EntitiesNoName
{
	private static final long serialVersionUID = 1L;
	///构造
	/** 
	 用户组s
	*/
	public Teams()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Team();
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<Team> ToJavaList()
	{
		return (java.util.List<Team>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Team> Tolist()
	{
		ArrayList<Team> list = new ArrayList<Team>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Team)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}