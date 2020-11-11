package bp.sys.frmui;

import bp.da.*;
import bp.en.*;
import bp.*;
import bp.sys.*;
import java.util.*;

/** 
 超连接s
*/
public class ExtLinks extends EntitiesMyPK
{

		///构造
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

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<ExtLink> ToJavaList()
	{
		return (java.util.List<ExtLink>)(Object)this;
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

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}