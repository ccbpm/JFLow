package BP.Sys.FrmUI;

import BP.DA.*;
import BP.En.*;
import BP.Sys.*;
import BP.Sys.*;
import java.util.*;

/** 
 实体属性s
*/
public class MapAttrDTs extends EntitiesMyPK
{

		///#region 构造
	/** 
	 实体属性s
	*/
	public MapAttrDTs()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
	{
		return new MapAttrDT();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MapAttrDT> ToJavaList()
	{
		return (List<MapAttrDT>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MapAttrDT> Tolist()
	{
		ArrayList<MapAttrDT> list = new ArrayList<MapAttrDT>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapAttrDT)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}