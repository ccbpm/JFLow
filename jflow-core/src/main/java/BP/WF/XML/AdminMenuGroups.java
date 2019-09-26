package BP.WF.XML;

import BP.DA.*;
import BP.En.*;
import BP.Sys.XML.*;
import BP.Sys.*;
import BP.WF.*;

/** 
 
*/
public class AdminMenuGroups extends XmlEns
{

		///#region 构造
	/** 
	 考核率的数据元素
	*/
	public AdminMenuGroups()
	{
	}

		///#endregion


		///#region 重写基类属性或方法。
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getNewEntity()
	{
		return new AdminMenuGroup();
	}
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfWebApp() + "\\DataUser\\XML\\AdminMenu.xml";
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

		///#endregion

}