package BP.WF.XML;

import BP.En.Entities;
import BP.Sys.SystemConfig;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 数据源类型s
*/
public class DBSrcs extends XmlEns
{

		
	/** 
	 考核率的数据元素
	*/
	public DBSrcs()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new DBSrc();
	}
	/** 
	 XML文件位置.
	*/
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfWebApp() + "DataUser/XML/DBSrc.xml";
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