package BP.WF.Template;

import BP.En.EntitiesNoName;
import BP.En.Entity;

/** 
 自由表单属性s
 
*/
public class MapFrmFrees extends EntitiesNoName
{

		
	/** 
	 自由表单属性s
	 
	*/
	public MapFrmFrees()
	{
	}
	/** 
	 得到它的 Entity
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MapFrmFree();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<MapFrmFree> ToJavaList()
	{
		return (java.util.List<MapFrmFree>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<MapFrmFree> Tolist()
	{
		java.util.ArrayList<MapFrmFree> list = new java.util.ArrayList<MapFrmFree>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapFrmFree)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}