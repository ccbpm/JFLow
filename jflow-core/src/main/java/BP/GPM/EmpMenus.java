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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
	public Entity getGetNewEntity()
	{
		return new EmpMenu();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<EmpMenu> ToJavaList()
	{
		return (List<EmpMenu>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<EmpMenu> Tolist()
	{
		ArrayList<EmpMenu> list = new ArrayList<EmpMenu>();
		for (int i = 0; i < this.Count; i++)
		{
			list.add((EmpMenu)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}