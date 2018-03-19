package BP.WF.XML;

import BP.En.Entities;
import BP.Sys.SystemConfig;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 公文左边谓词s
*/
public class GovWordLefts extends XmlEns
{

		
	/** 
	 考核率的数据元素
	*/
	public GovWordLefts()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new GovWordLeft();
	}
	/** 
	 XML文件位置.
	*/
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfWebApp() + "WF/Data/XML/XmlDB.xml";
	}
	/** 
	 物理表名
	*/
	@Override
	public String getTableName()
	{
		return "GovWordLeft";
	}
	@Override
	public Entities getRefEns()
	{
		return null;
	}
}