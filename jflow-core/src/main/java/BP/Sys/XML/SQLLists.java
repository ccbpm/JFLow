package BP.Sys.XML;

import BP.DA.*;
import BP.Difference.SystemConfig;
import BP.En.*;
import BP.Sys.XML.*;
import BP.Sys.*;

/** 
 属性集合
*/
public class SQLLists extends XmlEns
{

		///#region 构造
	/** 
	 考核过错行为的数据元素
	*/
	public SQLLists()
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

		///#endregion
}