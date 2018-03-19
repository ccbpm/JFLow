package BP.WF;

import BP.En.Entities;
import BP.En.Entity;

/** 
 记忆我
*/
public class RememberMes extends Entities
{
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new RememberMe();
	}
	/** 
	 RememberMe
	*/
	public RememberMes()
	{
	}
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<RememberMe> ToJavaList()
	{
		return (java.util.List<RememberMe>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<RememberMe> Tolist()
	{
		java.util.ArrayList<RememberMe> list = new java.util.ArrayList<RememberMe>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((RememberMe)this.get(i));
		}
		return list;
	}
}