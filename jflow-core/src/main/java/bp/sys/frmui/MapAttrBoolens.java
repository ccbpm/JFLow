package bp.sys.frmui;

import bp.da.*;
import bp.en.*;
import bp.sys.*;
import bp.*;
import bp.sys.*;
import java.util.*;

/** 
 实体属性s
*/
public class MapAttrBoolens extends EntitiesMyPK
{

		///构造
	/** 
	 实体属性s
	*/
	public MapAttrBoolens()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MapAttrBoolen();
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<MapAttrBoolen> ToJavaList()
	{
		return (java.util.List<MapAttrBoolen>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MapAttrBoolen> Tolist()
	{
		ArrayList<MapAttrBoolen> list = new ArrayList<MapAttrBoolen>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapAttrBoolen)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}