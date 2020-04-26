package BP.GPM;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 用户组类型
*/
public class GroupTypes extends EntitiesNoName
{
	/** 
	 用户组类型s
	*/
	public GroupTypes()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getNewEntity()
	{
		return new GroupType();
	}

		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<GroupType> ToJavaList()
	{
		return (List<GroupType>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<GroupType> Tolist()
	{
		ArrayList<GroupType> list = new ArrayList<GroupType>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((GroupType)this.get(i));
		}
		return list;
	}
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}