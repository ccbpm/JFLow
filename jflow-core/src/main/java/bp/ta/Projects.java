package bp.ta;

import bp.en.EntitiesNoName;
import bp.en.Entity;

import java.util.*;

/** 
 項目s
*/
public class Projects extends EntitiesNoName
{
		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getNewEntity()
	{
		return new Project();
	}
	/** 
	 項目
	*/
	public Projects()
	{
	}
//C# TO JAVA CONVERTER TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<Project> ToJavaList()
	{
		return (java.util.List<Project>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Project> Tolist()
	{
		ArrayList<Project> list = new ArrayList<Project>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Project)this.get(i));
		}
		return list;
	}
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
