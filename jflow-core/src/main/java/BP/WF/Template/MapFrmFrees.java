package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 自由表单属性s
*/
public class MapFrmFrees extends EntitiesNoName
{

		///#region 构造
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
	public Entity getNewEntity()
	{
		return new MapFrmFree();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MapFrmFree> ToJavaList()
	{
		return (List<MapFrmFree>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MapFrmFree> Tolist()
	{
		ArrayList<MapFrmFree> list = new ArrayList<MapFrmFree>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapFrmFree)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}