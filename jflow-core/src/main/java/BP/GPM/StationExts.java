package BP.GPM;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 岗位s
*/
public class StationExts extends EntitiesNoName
{
	/** 
	 岗位
	*/
	public StationExts()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new BP.GPM.StationExt();
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<StationExt> ToJavaList()
	{
		return (List<StationExt>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<StationExt> Tolist()
	{
		ArrayList<StationExt> list = new ArrayList<StationExt>();
		for (int i = 0; i < this.Count; i++)
		{
			list.add((StationExt)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}