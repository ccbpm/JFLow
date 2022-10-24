package bp.sys;

import bp.en.*;
import java.util.*;

/** 
 通用OID实体s
*/
public class GEEntitys extends EntitiesOID
{

		///#region 重载基类方法
	@Override
	public String toString()  {
		//if (this.FK_MapData == null)
		//    throw new Exception("@没有能 FK_MapData 给值。");
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
	 通用OID实体ID
	*/
	public GEEntitys()
	{
	}
	/** 
	 通用OID实体ID
	 
	 param fk_mapdata
	*/
	public GEEntitys(String fk_mapdata)
	{
		this.FK_MapData=fk_mapdata;
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<GEEntity> ToJavaList()
	{
		return (java.util.List<GEEntity>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<GEEntity> Tolist()
	{
		ArrayList<GEEntity> list = new ArrayList<GEEntity>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((GEEntity)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}