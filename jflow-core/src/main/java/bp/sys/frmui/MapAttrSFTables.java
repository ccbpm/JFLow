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
public class MapAttrSFTables extends EntitiesMyPK
{

		///#region 构造
	/** 
	 实体属性s
	*/
	public MapAttrSFTables() throws Exception {
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity() {
		return new MapAttrSFTable();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<MapAttrSFTable> ToJavaList() {
		return (java.util.List<MapAttrSFTable>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MapAttrSFTable> Tolist()  {
		ArrayList<MapAttrSFTable> list = new ArrayList<MapAttrSFTable>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapAttrSFTable)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}