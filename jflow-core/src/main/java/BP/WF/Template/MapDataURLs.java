package BP.WF.Template;

import BP.En.EntitiesNoName;
import BP.En.Entity;

/** 
 表单属性s
 
*/
public class MapDataURLs extends EntitiesNoName
{

		
	/** 
	 表单属性s
	 
	*/
	public MapDataURLs()
	{
	}
	/** 
	 得到它的 Entity
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MapDataURL();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<MapDataURL> ToJavaList()
	{
		return (java.util.List<MapDataURL>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<MapDataURL> Tolist()
	{
		java.util.ArrayList<MapDataURL> list = new java.util.ArrayList<MapDataURL>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapDataURL)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}