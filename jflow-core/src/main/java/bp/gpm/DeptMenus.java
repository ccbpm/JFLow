package bp.gpm;

import java.util.ArrayList;
import java.util.List;

import bp.en.EntitiesMM;
import bp.en.Entity;

/** 
部门菜单s
*/
public class DeptMenus extends EntitiesMM
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 构造
	/** 
	 部门s
	*/
	public DeptMenus()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new DeptMenu();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<DeptMenu> ToJavaList()
	{
		return (List<DeptMenu>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<DeptMenu> Tolist()
	{
		ArrayList<DeptMenu> list = new ArrayList<DeptMenu>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((DeptMenu)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}

