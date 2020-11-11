package bp.sys;

import bp.da.*;
import bp.en.*;
import bp.*;
import java.util.*;

/** 
 通用OID实体s
*/
public class GEEntityMyPKs extends Entities
{

		///重载基类方法
	@Override
	public String toString()
	{
		//if (this.FK_MapData == null)
		//    throw new Exception("@没有能 FK_MapData 给值。");
		return this.FK_MapData;
	}
	/** 
	 主键
	*/
	public String FK_MapData = null;

		///


		///方法
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		if (this.FK_MapData == null)
		{
			return new GEEntityMyPK();
		}
		return new GEEntityMyPK(this.FK_MapData);
	}
	/** 
	 通用OID实体ID
	*/
	public GEEntityMyPKs()
	{
	}
	/** 
	 通用OID实体ID
	 
	 @param fk_mapdtl
	*/
	public GEEntityMyPKs(String fk_mapdata)
	{
		this.FK_MapData=fk_mapdata;
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<GEEntityMyPK> ToJavaList()
	{
		return (java.util.List<GEEntityMyPK>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<GEEntityMyPK> Tolist()
	{
		ArrayList<GEEntityMyPK> list = new ArrayList<GEEntityMyPK>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((GEEntityMyPK)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}