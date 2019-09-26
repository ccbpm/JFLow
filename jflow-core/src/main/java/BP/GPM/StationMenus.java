package BP.GPM;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;
import java.util.*;

/** 
 岗位菜单s
*/
public class StationMenus extends EntitiesMM
{

		///#region 构造
	/** 
	 岗位s
	*/
	public StationMenus()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
	{
		return new StationMenu();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<StationMenu> ToJavaList()
	{
		return (List<StationMenu>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<StationMenu> Tolist()
	{
		ArrayList<StationMenu> list = new ArrayList<StationMenu>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((StationMenu)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}