package BP.WF.Template;

import BP.En.EntitiesNoName;
import BP.En.Entity;

/** 
 傻瓜表单属性s
 
*/
public class MapFrmFools extends EntitiesNoName
{

		
	/** 
	 傻瓜表单属性s
	 
	*/
	public MapFrmFools()
	{
	}
	/** 
	 得到它的 Entity
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MapFrmFool();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<MapFrmFool> ToJavaList()
	{
		return (java.util.List<MapFrmFool>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<MapFrmFool> Tolist()
	{
		java.util.ArrayList<MapFrmFool> list = new java.util.ArrayList<MapFrmFool>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapFrmFool)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}