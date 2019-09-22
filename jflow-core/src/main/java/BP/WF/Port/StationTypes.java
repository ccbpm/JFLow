package BP.WF.Port;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import java.util.*;

/** 
 岗位类型
*/
public class StationTypes extends EntitiesNoName
{
	/** 
	 岗位类型s
	*/
	public StationTypes()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new StationType();
	}


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<StationType> ToJavaList()
	{
		return (List<StationType>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<StationType> Tolist()
	{
		ArrayList<StationType> list = new ArrayList<StationType>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((StationType)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

}