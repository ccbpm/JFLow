package BP.En;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于OID Name 属性的实体继承
 */
public abstract class EntitiesOIDName extends EntitiesOID
{
	public static ArrayList<EntityOIDName> convertEntitiesOIDName(Object obj)
	{
		return (ArrayList<EntityOIDName>) obj;
	}
	public List<EntityOIDName> ToJavaList()
	{
		return (List<EntityOIDName>)(Object)this;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 构造
	/**
	 * 构造
	 */
	public EntitiesOIDName()
	{
	}
}