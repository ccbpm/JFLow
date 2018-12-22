package BP.GPM;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;

/** 
 权限组菜单s
 
*/
public class GroupMenus extends EntitiesMM
{
		///#region 构造
	/** 
	 权限组s
	 
	*/
	public GroupMenus()
	{
	}
	/** 
	 得到它的 Entity
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new GroupMenu();
	}
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<GroupMenu> ToJavaList()
	{
		return (java.util.List<GroupMenu>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<GroupMenu> Tolist()
	{
		java.util.ArrayList<GroupMenu> list = new java.util.ArrayList<GroupMenu>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((GroupMenu)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}