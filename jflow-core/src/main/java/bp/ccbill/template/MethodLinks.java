package bp.ccbill.template;

import bp.da.*;
import bp.web.*;
import bp.en.*;
import bp.port.*;
import bp.sys.*;
import bp.ccbill.*;
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

		///为了适应自动翻译成java的需要,把实体转换成List.
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

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}