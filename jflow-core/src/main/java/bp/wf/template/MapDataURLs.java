package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.wf.*;
import bp.sys.*;
import bp.wf.*;
import java.util.*;

/** 
 表单属性s
*/
public class MapDataURLs extends EntitiesNoName
{

		///构造
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

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MapDataURL> ToJavaList()
	{
		return (List<MapDataURL>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MapDataURL> Tolist()
	{
		ArrayList<MapDataURL> list = new ArrayList<MapDataURL>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapDataURL)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}