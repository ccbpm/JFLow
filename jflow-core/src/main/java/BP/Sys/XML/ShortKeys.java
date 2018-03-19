package BP.Sys.XML;

import BP.En.Entities;
import BP.Sys.SystemConfig;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 
 
*/
public class ShortKeys extends XmlEns
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 构造
	/**
	 * 考核过错行为的数据元素
	 */
	public ShortKeys()
	{
	}
	
	// 重写基类属性或方法。
	/**
	 * 得到它的 Entity
	 */
	@Override
	public XmlEn getGetNewEntity()
	{
		return new ShortKey();
	}
	
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfXML() + "/Menu.xml";
	}
	
	/**
	 * 物理表名
	 */
	@Override
	public String getTableName()
	{
		return "ShortKey";
	}
	
	@Override
	public Entities getRefEns()
	{
		return null;
	}
}