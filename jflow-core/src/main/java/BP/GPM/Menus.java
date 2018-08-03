package BP.GPM;

import BP.DA.*;
import BP.En.*;

/** 
 菜单s
 
*/
public class Menus extends EntitiesTree
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 菜单s
	 
	*/
	public Menus()
	{
	}
	/** 
	 得到它的 Entity
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Menu();
	}
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<Menu> ToJavaList()
	{
		return (java.util.List<Menu>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<Menu> Tolist()
	{
		java.util.ArrayList<Menu> list = new java.util.ArrayList<Menu>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Menu)this.get(i));
		}
		return list;
	}
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}