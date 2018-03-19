package BP.Sys.XML;

import BP.XML.XmlEn;
import BP.XML.XmlEns;

/**
 * Search 的摘要说明。 考核过错行为的数据元素 1，它是 Search 的一个明细。 2，它表示一个数据元素。
 */
public class Search extends XmlEn
{
	
	// 属性
	public final String getAttr()
	{
		return this.GetValStringByKey(SearchAttr.Attr);
	}
	
	public final String getFor()
	{
		return this.GetValStringByKey(SearchAttr.For);
	}
	
	public final String getURL()
	{
		return this.GetValStringByKey(SearchAttr.URL);
	}
	
	// 构造
	public Search()
	{
	}
	
	/**
	 * 获取一个实例
	 */
	@Override
	public XmlEns getGetNewEntities()
	{
		return new Searchs();
	}
}