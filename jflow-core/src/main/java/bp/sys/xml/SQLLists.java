package bp.sys.xml;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.en.*;
import bp.sys.*;
import bp.*;
import bp.sys.*;

/** 
 属性集合
*/
public class SQLLists extends XmlEns
{

		///构造
	/** 
	 考核过错行为的数据元素
	*/
	public SQLLists()
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

		///
}