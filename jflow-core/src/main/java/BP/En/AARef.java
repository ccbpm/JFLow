package BP.En;

/**
 * 属性属性关联
 */
public class AARef
{
	/**
	 * 目录属性
	 */
	public String CataAttr = null;
	/**
	 * 关联key
	 */
	public String RefKey = null;
	/**
	 * 子属性
	 */
	public String SubAttr = null;
	
	/**
	 * 属性属性关联
	 * 
	 * @param CataAttr
	 *            属性
	 * @param RefKey
	 * @param SubAttr
	 */
	public AARef(String cataAttr, String subAttr, String refKey)
	{
		this.CataAttr = cataAttr;
		this.SubAttr = subAttr;
		this.RefKey = refKey;
	}
}