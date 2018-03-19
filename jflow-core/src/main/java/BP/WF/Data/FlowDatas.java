package BP.WF.Data;

import BP.En.Entity;

/** 
 报表集合
*/
public class FlowDatas extends BP.En.EntitiesOID
{
	/** 
	 报表集合
	*/
	public FlowDatas()
	{
	}

	@Override
	public Entity getGetNewEntity()
	{
		return new FlowData();
	}

		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<FlowData> ToJavaList()
	{
		return (java.util.List<FlowData>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<FlowData> Tolist()
	{
		java.util.ArrayList<FlowData> list = new java.util.ArrayList<FlowData>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FlowData)this.get(i));
		}
		return list;
	}
}