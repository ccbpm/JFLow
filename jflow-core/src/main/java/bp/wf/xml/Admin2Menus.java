package bp.wf.xml;

import java.util.List;

import bp.difference.SystemConfig;
import bp.en.*;
import bp.sys.xml.XmlEn;
import bp.sys.xml.XmlEns;

/** 
 
*/
public class Admin2Menus extends XmlEns
{
	private static final long serialVersionUID = 1L;
		///构造
	/** 
	 考核率的数据元素
	*/
	public Admin2Menus()
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
		return new Admin2Menu();
	}
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfWebApp() + "DataUser/XML/Admin2Menu.xml";
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
	public final List<Admin2Menu> ToJavaList()
	{
		return (List<Admin2Menu>)(Object)this;
	}

}