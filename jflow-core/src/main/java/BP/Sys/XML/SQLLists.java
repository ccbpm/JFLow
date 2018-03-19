package BP.Sys.XML;

import BP.En.Entities;
import BP.Sys.SystemConfig;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/**
 * 属性集合
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class SQLLists extends XmlEns{
	
	/** 
	 考核过错行为的数据元素
	*/
	public SQLLists()
	{
	}

	//#region 重写基类属性或方法。
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new SQLList();
	}
	
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfXML() + "SQLList.xml";
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
