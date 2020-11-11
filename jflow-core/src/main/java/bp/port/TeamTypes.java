package bp.port;

import bp.en.*;
import java.util.*;

/** 
 用户组类型
*/
public class TeamTypes extends EntitiesNoName
{
	private static final long serialVersionUID = 1L;
	/** 
	 用户组类型s
	*/
	public TeamTypes()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new TeamType();
	}


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<TeamType> ToJavaList()
	{
		return (java.util.List<TeamType>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<TeamType> Tolist()
	{
		ArrayList<TeamType> list = new ArrayList<TeamType>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((TeamType)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}