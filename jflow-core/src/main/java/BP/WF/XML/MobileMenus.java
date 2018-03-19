package BP.WF.XML;

import BP.En.Entities;
import BP.Sys.SystemConfig;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 经典模式左侧菜单s
 
*/
public class MobileMenus extends XmlEns
{
	/** 
	 考核率的数据元素
	*/
	public MobileMenus()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new MobileMenu();
	}
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfDataUser() + "XML/CCOAMobileMenu.xml";
	}
	/** 
	 物理表名
	*/
	@Override
	public String getTableName()
	{
		return "MobileMenu";
	}
	@Override
	public Entities getRefEns()
	{
		return null; //new BP.ZF1.AdminTools();
	}
}