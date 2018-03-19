package BP.WF.Template;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

/** 
 挂起
 
*/
public class HungUps extends EntitiesMyPK
{

		///#region 方法
	/** 
	 得到它的 Entity 
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new HungUp();
	}
	/** 
	 挂起
	 
	*/
	public HungUps()
	{
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<HungUp> ToJavaList()
	{
		return (java.util.List<HungUp>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<HungUp> Tolist()
	{
		java.util.ArrayList<HungUp> list = new java.util.ArrayList<HungUp>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((HungUp)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}