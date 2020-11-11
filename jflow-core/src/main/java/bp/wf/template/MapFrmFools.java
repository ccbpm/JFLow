package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.wf.*;
import bp.sys.*;
import bp.wf.*;
import java.util.*;

/** 
 傻瓜表单属性s
*/
public class MapFrmFools extends EntitiesNoName
{

		///构造
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

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MapFrmFool> ToJavaList()
	{
		return (List<MapFrmFool>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MapFrmFool> Tolist()
	{
		ArrayList<MapFrmFool> list = new ArrayList<MapFrmFool>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapFrmFool)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}