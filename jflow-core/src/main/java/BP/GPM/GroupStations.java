package BP.GPM;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;

/** 
 权限组岗位s
 
*/
public class GroupStations extends EntitiesMM
{
		///#region 构造
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

		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<GroupStation> ToJavaList()
	{
		return (java.util.List<GroupStation>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<GroupStation> Tolist()
	{
		java.util.ArrayList<GroupStation> list = new java.util.ArrayList<GroupStation>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((GroupStation)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}