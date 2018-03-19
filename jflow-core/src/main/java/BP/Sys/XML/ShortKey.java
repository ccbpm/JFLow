package BP.Sys.XML;

import BP.Web.WebUser;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/**
 * ShortKey 的摘要说明。 考核过错行为的数据元素 1，它是 ShortKey 的一个明细。 2，它表示一个数据元素。
 */
public class ShortKey extends XmlEn
{
	// 属性
	public final String getNo()
	{
		return this.GetValStringByKey(ShortKeyAttr.No);
	}
	
	/**
	 * 数据
	 */
	public final String getDFor()
	{
		return this.GetValStringByKey(ShortKeyAttr.DFor);
	}
	
	public final String getName()
	{
		return this.GetValStringByKey(WebUser.getSysLang());
	}
	
	/**
	 * URL
	 */
	public final String getURL()
	{
		return this.GetValStringByKey(ShortKeyAttr.URL);
	}
	
	/**
	 * 图片
	 */
	public final String getImg()
	{
		return this.GetValStringByKey(ShortKeyAttr.Img);
	}
	
	public final String getTarget()
	{
		return this.GetValStringByKey(ShortKeyAttr.Target);
	}
	
	// 构造
	public ShortKey()
	{
	}
	
	/**
	 * 获取一个实例
	 */
	@Override
	public XmlEns getGetNewEntities()
	{
		return new ShortKeys();
	}
}