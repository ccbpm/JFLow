package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 映射基础s
*/
public class MapDataTemplates extends EntitiesNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 映射基础s
	*/
	public MapDataTemplates()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MapDataTemplate();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MapDataTemplate> ToJavaList()
	{
		return (List<MapDataTemplate>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MapDataTemplate> Tolist()
	{
		ArrayList<MapDataTemplate> list = new ArrayList<MapDataTemplate>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapDataTemplate)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}