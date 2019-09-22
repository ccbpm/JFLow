package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 对比状态存储s
*/
public class Contrasts extends Entities
{
	/** 
	 对比状态存储s
	*/
	public Contrasts()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Contrast();
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Contrast> ToJavaList()
	{
		return (List<Contrast>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Contrast> Tolist()
	{
		ArrayList<Contrast> list = new ArrayList<Contrast>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Contrast)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}