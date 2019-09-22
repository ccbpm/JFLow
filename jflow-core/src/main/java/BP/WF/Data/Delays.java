package BP.WF.Data;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.Web.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 逾期流程s
*/
public class Delays extends EntitiesMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法属性
	/** 
	 逾期流程s
	*/
	public Delays()
	{
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	/** 
	 逾期流程
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Delay();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Delay> ToJavaList()
	{
		return (List<Delay>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Delay> Tolist()
	{
		ArrayList<Delay> list = new ArrayList<Delay>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Delay)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}