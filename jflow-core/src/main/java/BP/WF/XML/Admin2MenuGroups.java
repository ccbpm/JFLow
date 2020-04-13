package BP.WF.XML;

import BP.Difference.SystemConfig;
import BP.En.*;
import BP.Sys.XML.*;

import java.util.List;

/** 
 
*/
public class Admin2MenuGroups extends XmlEns
{
	/**
	 考核率的数据元素
	*/
	public Admin2MenuGroups()
	{
	}

	/**
	 转化成 java list,C#不能调用.
	 @return List
	 */
	public final List<Admin2MenuGroup> ToJavaList()
	{
		return (List<Admin2MenuGroup>)(Object)this;
	}
		///#region 重写基类属性或方法。
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getNewEntity()
	{
		return new Admin2MenuGroup();
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
		return "Group";
	}
	@Override
	public Entities getRefEns()
	{
		return null;
	}

}