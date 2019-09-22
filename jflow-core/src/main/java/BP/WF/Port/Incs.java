package BP.WF.Port;

import BP.DA.*;
import BP.En.*;
import BP.Web.*;
import BP.WF.*;
import java.util.*;

/** 
独立组织集合
*/
public class Incs extends EntitiesNoName
{
	/** 
	 得到一个新实体
	*/
	@Override
	public Entity getNewEntity()
	{
		return new Inc();
	}
	/** 
	 create ens
	*/
	public Incs()
	{
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Inc> ToJavaList()
	{
		return (List<Inc>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Inc> Tolist()
	{
		ArrayList<Inc> list = new ArrayList<Inc>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Inc)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}