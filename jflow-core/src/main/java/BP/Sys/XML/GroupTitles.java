package BP.Sys.XML;

import BP.En.Entities;
import BP.Sys.SystemConfig;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 
 
*/
public class GroupTitles extends XmlEns
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 构造
	/**
	 * 考核过错行为的数据元素
	 */
	public GroupTitles()
	{
	}
	
	// 重写基类属性或方法。
	/**
	 * 得到它的 Entity
	 */
	@Override
	public XmlEn getGetNewEntity()
	{
		return new GroupTitle();
	}
	
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfXML() + "/Ens/GroupTitle.xml";
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