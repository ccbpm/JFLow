package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.wf.*;
import bp.sys.*;
import bp.wf.*;
import java.util.*;

/** 
 自由表单属性s
*/
public class MapFrmFrees extends EntitiesNoName
{

		///构造
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

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
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

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}