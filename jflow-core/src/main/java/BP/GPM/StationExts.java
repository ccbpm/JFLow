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
	public Entity getNewEntity()
	{
		return new BP.GPM.StationExt();
	}


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<StationExt> ToJavaList()
	{
		return (List<StationExt>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<StationExt> Tolist()
	{
		ArrayList<StationExt> list = new ArrayList<StationExt>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((StationExt)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}