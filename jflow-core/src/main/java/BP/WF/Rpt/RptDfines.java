package BP.WF.Rpt;

import BP.En.EntitiesNoName;
import BP.En.Entity;

/** 
报表定义s

*/
public class RptDfines extends EntitiesNoName
{

		///#region 构造
	/** 
	 报表定义s
	 
	*/
	public RptDfines()
	{
	}
	/** 
	 得到它的 Entity
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new RptDfine();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<RptDfine> ToJavaList()
	{
		return (java.util.List<RptDfine>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<RptDfine> Tolist()
	{
		java.util.ArrayList<RptDfine> list = new java.util.ArrayList<RptDfine>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((RptDfine)this.get(i));
		}
		return list;
	}
}