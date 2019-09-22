package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 任务
*/
public class Tasks extends Entities
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Task();
	}
	/** 
	 任务
	*/
	public Tasks()
	{
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Task> ToJavaList()
	{
		return (List<Task>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Task> Tolist()
	{
		ArrayList<Task> list = new ArrayList<Task>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Task)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}