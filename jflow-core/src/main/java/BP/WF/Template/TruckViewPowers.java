package BP.WF.Template;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import BP.WF.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 流程轨迹权限s
*/
public class TruckViewPowers extends EntitiesNoName
{
	/** 
	 流程轨迹权限s
	*/
	public TruckViewPowers()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new TruckViewPower();
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<TruckViewPower> ToJavaList()
	{
		return (List<TruckViewPower>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<TruckViewPower> Tolist()
	{
		ArrayList<TruckViewPower> list = new ArrayList<TruckViewPower>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((TruckViewPower)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}