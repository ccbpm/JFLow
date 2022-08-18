package bp.sys;

import bp.ccbill.FrmBill;
import bp.da.*;
import bp.en.*;
import bp.pub.*;
import bp.*;
import java.util.*;
import java.io.*;
import java.time.*;
import java.math.*;

/** 
 实体属性s
*/
public class MapAttrs extends EntitiesMyPK
{

		///#region 构造
	/** 
	 实体属性s
	*/
	public MapAttrs()  {
	}
	/** 
	 实体属性s
	*/
	public MapAttrs(String frmID) throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(MapAttrAttr.FK_MapData, frmID);
		qo.addOrderBy(MapAttrAttr.Idx);
		qo.DoQuery();
	}
	public final int SearchMapAttrsYesVisable(String fk_map) throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(MapAttrAttr.FK_MapData, fk_map);
		qo.addAnd();
		qo.AddWhere(MapAttrAttr.UIVisible, 1);
		qo.addOrderBy(MapAttrAttr.Idx);
		// qo.addOrderBy(MapAttrAttr.Idx);
		return qo.DoQuery();
	}
	/// <summary>
	/// 查询日期+日期时间类型的
	/// 代码移动.
	/// </summary>
	/// <param name="fk_map"></param>
	/// <returns></returns>
	public String SearchMapAttrsDateAndDateTime(String fk_map) throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(MapAttrAttr.FK_MapData, fk_map);
		qo.addAnd();
		qo.AddWhere(MapAttrAttr.MyDataType, " IN ", "(6,7)");
		qo.addOrderBy(MapAttrAttr.Idx);
		qo.DoQuery();
		return this.ToJson();
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity() {
		return new MapAttr();
	}
	public final int getWithOfCtl() throws Exception {
		int i = 0;
		for (MapAttr item : this.ToJavaList())
		{
			if (item.getUIVisible() == false)
			{
				continue;
			}

			i += item.getUIWidthInt();
		}
		return i;
	}

		///#endregion



		///#region 业务方法.


		///#endregion



		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<MapAttr> ToJavaList() {
		return (List<MapAttr>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MapAttr> Tolist() {
		ArrayList<MapAttr> list = new ArrayList<MapAttr>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapAttr)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}