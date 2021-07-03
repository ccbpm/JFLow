package bp.wf.xml;

import java.util.List;

import bp.difference.SystemConfig;
import bp.en.*;
import bp.sys.xml.XmlEn;
import bp.sys.xml.XmlEns;


/** 
 
*/
public class AdminMenus extends XmlEns
{
	private static final long serialVersionUID = 1L;
		///构造
	/** 
	 考核率的数据元素
	*/
	public AdminMenus()
	{
	}

		///


		///重写基类属性或方法。
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
		return SystemConfig.getPathOfWebApp() + "DataUser/XML/AdminMenu.xml";
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

	public final List<AdminMenu> ToJavaList()
	{
		return (List<AdminMenu>)(Object)this;
	}

}