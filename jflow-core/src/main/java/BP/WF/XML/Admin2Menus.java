package BP.WF.XML;

import BP.Difference.SystemConfig;
import BP.En.*;
import BP.Sys.XML.*;

import java.util.List;


/** 
 
*/
public class Admin2Menus extends XmlEns
{
	/**
	 考核率的数据元素
	*/
	public Admin2Menus()
	{
	}

	/**
	 转化成 java list,C#不能调用.
	 @return List
	 */
	public final List<Admin2Menu> ToJavaList()
	{
		return (List<Admin2Menu>)(Object)this;
	}
	/**
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getNewEntity()
	{
		return new Admin2Menu();
	}
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfWebApp() + "/DataUser/XML/Admin2Menu.xml";
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
		return null;
	}

}