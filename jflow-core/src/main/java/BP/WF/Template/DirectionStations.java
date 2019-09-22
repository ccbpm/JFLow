package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 方向与工作岗位对应
*/
public class DirectionStations extends EntitiesMM
{
	/** 
	 方向与工作岗位对应
	*/
	public DirectionStations()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new DirectionStation();
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<DirectionStation> ToJavaList()
	{
		return (List<DirectionStation>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<DirectionStation> Tolist()
	{
		ArrayList<DirectionStation> list = new ArrayList<DirectionStation>();
		for (int i = 0; i < this.Count; i++)
		{
			list.add((DirectionStation)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}