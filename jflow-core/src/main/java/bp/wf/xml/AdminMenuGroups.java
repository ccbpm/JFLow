package bp.wf.xml;

import java.util.List;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.en.*;
import bp.sys.*;
import bp.sys.xml.XmlEn;
import bp.sys.xml.XmlEns;
import bp.sys.*;
import bp.wf.*;

/** 
 
*/
public class AdminMenuGroups extends XmlEns
{

		///构造
	/** 
	 考核率的数据元素
	*/
	public AdminMenuGroups()
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
		return new AdminMenuGroup();
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
		return "Group";
	}
	@Override
	public Entities getRefEns()
	{
		return null; //new BP.ZF1.AdminAdminMenus();
	}

	public final List<AdminMenuGroup> ToJavaList()
	{
		return (List<AdminMenuGroup>)(Object)this;
	}

}