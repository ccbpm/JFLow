package BP.Sys.FrmUI;

import BP.DA.*;
import BP.En.*;
import BP.Sys.*;
import java.util.*;

/** 
 超连接s
*/
public class ExtLinks extends EntitiesMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 超连接s
	*/
	public ExtLinks()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new ExtLink();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<ExtLink> ToJavaList()
	{
		return (List<ExtLink>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<ExtLink> Tolist()
	{
		ArrayList<ExtLink> list = new ArrayList<ExtLink>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((ExtLink)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}