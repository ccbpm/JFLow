package BP.Sys;

import BP.En.Entities;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/**
 * 属性集合
 */
public class EnsAppGroupXmls extends XmlEns
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 构造
	/**
	 * 考核过错行为的数据元素
	 */
	public EnsAppGroupXmls()
	{
	}
	
	// 重写基类属性或方法。
	/**
	 * 得到它的 Entity
	 */
	@Override
	public XmlEn getGetNewEntity()
	{
		return new EnsAppGroupXml();
	}
	
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfXML() + "/Ens/EnsAppXml/";
	}
	
	/**
	 * 物理表名
	 */
	@Override
	public String getTableName()
	{
		return "Group";
	}
	
	@Override
	public Entities getRefEns()
	{
		return null;
	}
}