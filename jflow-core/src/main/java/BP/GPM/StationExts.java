package BP.GPM;

import BP.DA.*;
import BP.En.*;

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

		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<StationExt> ToJavaList()
	{
		return (java.util.List<StationExt>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<StationExt> Tolist()
	{
		java.util.ArrayList<StationExt> list = new java.util.ArrayList<StationExt>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((StationExt)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}