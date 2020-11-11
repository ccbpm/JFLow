package bp.sys;

import bp.da.*;
import bp.en.*;
import bp.*;
import java.util.*;
import java.math.*;

/** 
 通用从表s
*/
public class GEDtls extends EntitiesOID
{

		///重载基类方法
	/** 
	 节点ID
	*/
	public String FK_MapDtl = null;

		///


		///方法
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		if (this.FK_MapDtl == null)
		{
			return new GEDtl();
		}
		return new GEDtl(this.FK_MapDtl);
	}
	/** 
	 通用从表ID
	*/
	public GEDtls()
	{
	}
	/** 
	 通用从表ID
	 
	 @param fk_mapdtl
	*/
	public GEDtls(String fk_mapdtl)
	{
		this.FK_MapDtl = fk_mapdtl;
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<GEDtl> ToJavaList()
	{
		return (java.util.List<GEDtl>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<GEDtl> Tolist()
	{
		ArrayList<GEDtl> list = new ArrayList<GEDtl>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((GEDtl)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}