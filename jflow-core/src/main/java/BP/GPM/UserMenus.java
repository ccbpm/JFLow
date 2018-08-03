package BP.GPM;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;

/** 
 用户菜单s
 
*/
public class UserMenus extends EntitiesMM
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 用户s
	 
	*/
	public UserMenus()
	{
	}
	/** 
	 得到它的 Entity
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new UserMenu();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<UserMenu> ToJavaList()
	{
		return (java.util.List<UserMenu>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<UserMenu> Tolist()
	{
		java.util.ArrayList<UserMenu> list = new java.util.ArrayList<UserMenu>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((UserMenu)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}