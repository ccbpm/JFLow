package BP.WF.Template;

import java.util.ArrayList;
import java.util.List;

import BP.En.EntitiesNoName;
import BP.En.Entity;

public class MapFoolForms extends EntitiesNoName{
	//#region 构造
	/** 
	 表单属性s
	*/
	public MapFoolForms()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MapFoolForm();
	}
	//#endregion

	//#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final List<MapFoolForm> ToJavaList()
	{
		return (List<MapFoolForm>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MapFoolForm> Tolist()
	{
		ArrayList<MapFoolForm> list = new ArrayList<MapFoolForm>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapFoolForm)this.get(i));
		}
		return list;
	}
	//#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
