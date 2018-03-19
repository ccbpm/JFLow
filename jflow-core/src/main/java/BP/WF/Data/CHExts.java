package BP.WF.Data;

import BP.En.Entities;
import BP.En.Entity;

/** 
 时效考核s
 
*/
public class CHExts extends Entities
{
	/** 
	 时效考核s
	*/
	public CHExts()
	{
	}
	/** 
	 时效考核
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new CHExt();
	}
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<CHExt> ToJavaList()
	{
		return (java.util.List<CHExt>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<CHExt> Tolist()
	{
		java.util.ArrayList<CHExt> list = new java.util.ArrayList<CHExt>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((CHExt)this.get(i));
		}
		return list;
	}
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}