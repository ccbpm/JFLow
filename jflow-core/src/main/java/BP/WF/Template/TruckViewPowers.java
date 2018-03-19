package BP.WF.Template;

import BP.En.EntitiesNoName;
import BP.En.Entity;

/** 
 流程轨迹权限s
 
*/
public class TruckViewPowers extends EntitiesNoName
{
	/** 
	 流程轨迹权限s
	 
	*/
	public TruckViewPowers()
	{
	}
	/** 
	 得到它的 Entity
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new TruckViewPower();
	}


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<TruckViewPower> ToJavaList()
	{
		return (java.util.List<TruckViewPower>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<TruckViewPower> Tolist()
	{
		java.util.ArrayList<TruckViewPower> list = new java.util.ArrayList<TruckViewPower>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((TruckViewPower)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}