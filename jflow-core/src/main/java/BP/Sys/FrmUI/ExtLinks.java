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
	public Entity getNewEntity()
	{
		return new ExtLink();
	}

		///#endregion


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

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}