package BP.WF.XML;


import BP.Difference.SystemConfig;
import BP.En.*;
import BP.Sys.XML.*;

import java.util.List;

public class AdminMenuGroups extends XmlEns
{
	/**
	 考核率的数据元素
	*/
	public AdminMenuGroups()
	{
	}

	/**
	 转化成 java list,C#不能调用.
	 @return List
	 */
	public final List<AdminMenuGroup> ToJavaList()
	{
		return (List<AdminMenuGroup>)(Object)this;
	}


	/*
	*得到它的 Entity
	*/
	@Override
	public XmlEn getNewEntity()
	{
		return new AdminMenuGroup();
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
		return "Group";
	}
	@Override
	public Entities getRefEns()
	{
		return null; //new BP.ZF1.AdminAdminMenus();
	}
}