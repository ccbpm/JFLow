package BP.En;

import BP.Sys.SystemConfig;
import BP.XML.XmlEn;
import BP.XML.XmlMenus;

/**
 * 取值s
 */
public class FrmPopVals extends XmlMenus
{
	// /#region 构造
	/**
	 * 取值s
	 */
	public FrmPopVals()
	{
	}
	
	// 重写基类属性或方法。
	/**
	 * 得到它的 Entity
	 */
	@Override
	public XmlEn getGetNewEntity()
	{
		return new FrmPopVal();
	}
	
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfDataUser() + "Xml/FrmPopVal.xml";
	}
	
	/**
	 * 物理表名
	 */
	@Override
	public String getTableName()
	{
		return "Item";
		
	}
	
	@Override
	public Entities getRefEns()
	{
		return null;
	}
}