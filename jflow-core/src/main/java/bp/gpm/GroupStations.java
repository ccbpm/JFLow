package bp.gpm;

import bp.da.*;
import bp.web.*;
import bp.en.*;
import java.util.*;

/** 
 权限组岗位s
*/
public class GroupStations extends EntitiesMM
{

		///构造
	/** 
	 权限组s
	*/
	public GroupStations()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new GroupStation();
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<GroupStation> ToJavaList()
	{
		return (List<GroupStation>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<GroupStation> Tolist()
	{
		ArrayList<GroupStation> list = new ArrayList<GroupStation>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((GroupStation)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}