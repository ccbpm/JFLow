package BP.GPM;

import BP.Sys.*;
import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 系统s
*/
public class Apps extends EntitiesNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 系统s
	*/
	public Apps()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new App();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<App> ToJavaList()
	{
		return (List<App>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<App> Tolist()
	{
		ArrayList<App> list = new ArrayList<App>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((App)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}