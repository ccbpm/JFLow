package BP.Sys;

import java.util.ArrayList;
import java.util.List;

import BP.En.EntitiesOID;
import BP.En.Entity;

/**
 * 通用从表s
 */
public class GEDtls extends EntitiesOID
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static ArrayList<GEDtl> convertGEDtls(Object obj)
	{
		return (ArrayList<GEDtl>) obj;
	}
	
	public List<GEDtl> ToJavaList()
	{
		return (List<GEDtl>)(Object)this;
	}
	/**
	 * 节点ID
	 */
	public String FK_MapDtl = null;
	
	// 方法
	/**
	 * 得到它的 Entity
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
	 * 通用从表ID
	 */
	public GEDtls()
	{
	}
	
	/**
	 * 通用从表ID
	 * 
	 * @param fk_mapdtl
	 */
	public GEDtls(String fk_mapdtl)
	{
		this.FK_MapDtl = fk_mapdtl;
	}
}