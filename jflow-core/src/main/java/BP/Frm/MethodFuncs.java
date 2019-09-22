package BP.Frm;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;
import BP.Port.*;
import BP.Sys.*;
import java.util.*;

/** 
 功能执行
*/
public class MethodFuncs extends EntitiesMyPK
{
	/** 
	 功能执行
	*/
	public MethodFuncs()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getNewEntity()
	{
		return new MethodFunc();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MethodFunc> ToJavaList()
	{
		return (List<MethodFunc>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MethodFunc> Tolist()
	{
		ArrayList<MethodFunc> list = new ArrayList<MethodFunc>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MethodFunc)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}