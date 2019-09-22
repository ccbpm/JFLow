package BP.WF;

import BP.DA.*;
import BP.En.*;
import BP.Web.*;
import BP.Sys.*;
import BP.WF.Port.*;
import java.util.*;

/** 
 消息s
*/
public class SMSs extends Entities
{
	/** 
	 获得实体
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new SMS();
	}
	public SMSs()
	{
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<SMS> ToJavaList()
	{
		return (List<SMS>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<SMS> Tolist()
	{
		ArrayList<SMS> list = new ArrayList<SMS>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((SMS)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}