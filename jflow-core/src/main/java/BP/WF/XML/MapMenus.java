package BP.WF.XML;

import BP.En.Entities;
import BP.Sys.SystemConfig;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 映射菜单s
*/
public class MapMenus extends XmlEns
{

		
	/** 
	 映射菜单s
	*/
	public MapMenus()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new MapMenu();
	}
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfData() + "XML/XmlDB.xml";

	}
	/** 
	 物理表名
	*/
	@Override
	public String getTableName()
	{
		return "MapMenu";
	}
	@Override
	public Entities getRefEns()
	{
		return null; //new BP.ZF1.AdminTools();
	}
}