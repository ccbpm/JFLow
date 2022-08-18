package bp.wf.rpt;

import bp.en.*;

import java.util.*;

/** 
 报表岗位 
*/
public class RptStations extends Entities
{

		///#region 构造
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

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<RptStation> ToJavaList()throws Exception
	{
		return (java.util.List<RptStation>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<RptStation> Tolist()throws Exception
	{
		ArrayList<RptStation> list = new ArrayList<RptStation>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((RptStation)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}