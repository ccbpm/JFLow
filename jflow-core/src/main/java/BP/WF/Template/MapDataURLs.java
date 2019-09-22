package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 表单属性s
*/
public class MapDataURLs extends EntitiesNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 表单属性s
	*/
	public MapDataURLs()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MapDataURL();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MapDataURL> ToJavaList()
	{
		return (List<MapDataURL>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MapDataURL> Tolist()
	{
		ArrayList<MapDataURL> list = new ArrayList<MapDataURL>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapDataURL)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}