package BP.GPM;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;
import java.util.*;

/** 
 人员菜单功能s
*/
public class EmpMenus extends EntitiesMM
{

		///#region 构造
	/** 
	 菜单s
	*/
	public EmpMenus()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
	{
		return new EmpMenu();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<EmpMenu> ToJavaList()
	{
		return (List<EmpMenu>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<EmpMenu> Tolist()
	{
		ArrayList<EmpMenu> list = new ArrayList<EmpMenu>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((EmpMenu)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}