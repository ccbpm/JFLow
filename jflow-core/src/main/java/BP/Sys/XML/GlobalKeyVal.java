package BP.Sys.XML;

import BP.XML.XmlEn;
import BP.XML.XmlEns;

/**
 * GlobalKeyVal 的摘要说明。 考核过错行为的数据元素 1，它是 GlobalKeyVal 的一个明细。 2，它表示一个数据元素。
 */
public class GlobalKeyVal extends XmlEn
{
	// 属性
	public final String getKey()
	{
		return this.GetValStringByKey(GlobalKeyValAttr.Key);
	}
	
	public final String getVal()
	{
		return this.GetValStringByKey(GlobalKeyValAttr.Val);
	}
	
	// 构造
	public GlobalKeyVal()
	{
	}
	
	/**
	 * 获取一个实例
	 */
	@Override
	public XmlEns getGetNewEntities()
	{
		return new GlobalKeyVals();
	}
}