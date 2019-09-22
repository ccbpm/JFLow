package BP.WF;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.Web.*;
import BP.Sys.*;
import java.util.*;

/** 
 单据s
*/
public class Bills extends EntitiesMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法属性
	/** 
	 单据s
	*/
	public Bills()
	{
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	/** 
	 单据
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Bill();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Bill> ToJavaList()
	{
		return (List<Bill>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Bill> Tolist()
	{
		ArrayList<Bill> list = new ArrayList<Bill>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Bill)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}