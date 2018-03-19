package BP.Sys;

import BP.En.Entities;
import BP.Sys.SystemConfig;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 
 
*/
public class InputCheckXmls extends XmlEns
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 构造
	/**
	 * 考核率的数据元素
	 */
	public InputCheckXmls()
	{
	}
	
	// 重写基类属性或方法。
	/**
	 * 得到它的 Entity
	 */
	@Override
	public XmlEn getGetNewEntity()
	{
		return new InputCheckXml();
	}
	
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfXML() + "MapExt.xml";
	}
	
	/**
	 * 物理表名
	 */
	@Override
	public String getTableName()
	{
		return "InputCheck";
	}
	
	@Override
	public Entities getRefEns()
	{
		return null; // new BP.ZF1.AdminTools();
	}
}