package BP.WF.Template;

import BP.En.Entities;
import BP.En.Entity;

/** 
 抄送s
 
*/
public class CCs extends Entities
{

		///#region 方法
	/** 
	 得到它的 Entity 
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new CC();
	}
	/** 
	 抄送
	 
	*/
	public CCs()
	{
	}

		///#endregion



		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<CC> ToJavaList()
	{
		return (java.util.List<CC>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<CC> Tolist()
	{
		java.util.ArrayList<CC> list = new java.util.ArrayList<CC>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((CC)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}