package bp.sys;

import bp.en.*;
import bp.en.Map;
import bp.*;
import java.util.*;

/** 
 excel表单实体s
*/
public class GEEntityExcelFrms extends EntitiesOID
{

		///#region 重载基类方法
	@Override
	public String toString()  {
		return this.FK_MapData;
	}
	/** 
	 主键
	*/
	public String FK_MapData = null;

		///#endregion


		///#region 方法
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
			//if (this.FK_MapData == null)
			//    throw new Exception("@没有能 FK_MapData 给值。");

		if (this.FK_MapData == null)
		{
			return new GEEntity();
		}
		return new GEEntity(this.FK_MapData);
	}
	/** 
	 通用实体ID
	*/
	public GEEntityExcelFrms()
	{
	}
	/** 
	 通用实体ID
	 
	 param fk_mapdata
	*/
	public GEEntityExcelFrms(String fk_mapdata)
	{
		this.FK_MapData=fk_mapdata;
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<GEEntityExcelFrm> ToJavaList()throws Exception
	{
		return (java.util.List<GEEntityExcelFrm>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<GEEntityExcelFrm> Tolist()throws Exception
	{
		ArrayList<GEEntityExcelFrm> list = new ArrayList<GEEntityExcelFrm>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((GEEntityExcelFrm)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}