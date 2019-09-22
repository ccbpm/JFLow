package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 序列号s
*/
public class Serials extends Entities
{
	/** 
	 序列号s
	*/
	public Serials()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
	{
		return new Serial();
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Serial> ToJavaList()
	{
		return (List<Serial>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Serial> Tolist()
	{
		ArrayList<Serial> list = new ArrayList<Serial>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Serial)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}