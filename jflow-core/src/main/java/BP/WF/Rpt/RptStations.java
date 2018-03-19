package BP.WF.Rpt;

import BP.En.Entities;
import BP.En.Entity;

/** 
 报表岗位 
 
*/
public class RptStations extends Entities
{

		
	/** 
	 报表与岗位集合
	 
	*/
	public RptStations()
	{
	}

		///#endregion


		///#region 方法
	/** 
	 得到它的 Entity 
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new RptStation();
	}


	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<RptStation> ToJavaList()
	{
		return (java.util.List<RptStation>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<RptStation> Tolist()
	{
		java.util.ArrayList<RptStation> list = new java.util.ArrayList<RptStation>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((RptStation)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}