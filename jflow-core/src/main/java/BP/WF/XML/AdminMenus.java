package BP.WF.XML;

import BP.En.Entities;
import BP.Sys.SystemConfig;
import BP.WF.Port.Dept;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 
*/
public class AdminMenus extends XmlEns
{
		
	/** 
	 考核率的数据元素
	*/
	public AdminMenus()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new AdminMenu();
	}
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfWebApp() + "/DataUser/XML/AdminMenu.xml";
	}
	/** 
	 物理表名
	*/
	@Override
	public String getTableName()
	{
		return "Item";
	}
	@Override
	public Entities getRefEns()
	{
		return null; //new BP.ZF1.AdminAdminMenus();
	}
	
	public final java.util.List<AdminMenu> ToJavaList()
	{
		return (java.util.List<AdminMenu>)(Object)this;
	}
}