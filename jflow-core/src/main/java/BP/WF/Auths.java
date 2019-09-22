package BP.WF;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;
import BP.Port.*;
import BP.Sys.*;
import java.util.*;

/** 
 授权
*/
public class Auths extends EntitiesMyPK
{
	/** 
	 授权
	*/
	public Auths()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Auth();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Auth> ToJavaList()
	{
		return (List<Auth>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Auth> Tolist()
	{
		ArrayList<Auth> list = new ArrayList<Auth>();
		for (int i = 0; i < this.Count; i++)
		{
			list.add((Auth)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}