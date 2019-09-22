package BP.WF;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.Port.*;
import java.util.*;

/** 
 移交记录s 
*/
public class ShiftWorks extends Entities
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 移交记录s
	*/
	public ShiftWorks()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new ShiftWork();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<ShiftWork> ToJavaList()
	{
		return (List<ShiftWork>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<ShiftWork> Tolist()
	{
		ArrayList<ShiftWork> list = new ArrayList<ShiftWork>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((ShiftWork)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}