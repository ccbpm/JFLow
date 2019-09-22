package BP.Frm;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;
import BP.Port.*;
import BP.Sys.*;
import java.util.*;

/** 
 连接方法
*/
public class MethodLinks extends EntitiesMyPK
{
	/** 
	 连接方法
	*/
	public MethodLinks()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MethodLink();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MethodLink> ToJavaList()
	{
		return (List<MethodLink>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MethodLink> Tolist()
	{
		ArrayList<MethodLink> list = new ArrayList<MethodLink>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MethodLink)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}