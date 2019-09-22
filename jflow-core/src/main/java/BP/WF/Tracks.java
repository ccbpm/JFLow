package BP.WF;

import BP.DA.*;
import BP.En.*;
import BP.Sys.*;
import BP.WF.Template.*;
import java.util.*;
import java.time.*;

/** 
 轨迹集合s
*/
public class Tracks extends BP.En.Entities
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法.
	/** 
	 轨迹集合
	*/
	public Tracks()
	{
	}
	@Override
	public Entity getGetNewEntity()
	{
		return new Track();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 构造方法.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Track> ToJavaList()
	{
		return (List<Track>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Track> Tolist()
	{
		ArrayList<Track> list = new ArrayList<Track>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Track)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

}