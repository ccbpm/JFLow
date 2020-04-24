package BP.Port;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import java.util.*;

/** 
 用户组s
*/
public class Teams extends EntitiesNoName
{
		///#region 构造
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
	public Entity getNewEntity()
	{
		return new Team();
	}
		///#endregion

		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Team> ToJavaList()
	{
		return (List<Team>)(Object)this;
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
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}