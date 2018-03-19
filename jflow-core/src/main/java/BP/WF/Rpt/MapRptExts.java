package BP.WF.Rpt;

import BP.En.EntitiesNoName;
import BP.En.Entity;

/** 
 报表设计s
 
*/
public class MapRptExts extends EntitiesNoName
{

		
	/** 
	 报表设计s
	 
	*/
	public MapRptExts()
	{
	}
	/** 
	 得到它的 Entity
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MapRptExt();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<MapRptExt> ToJavaList()
	{
		return (java.util.List<MapRptExt>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<MapRptExt> Tolist()
	{
		java.util.ArrayList<MapRptExt> list = new java.util.ArrayList<MapRptExt>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapRptExt)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}