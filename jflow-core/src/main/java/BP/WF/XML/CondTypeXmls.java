package BP.WF.XML;

import BP.En.Entities;
import BP.Sys.SystemConfig;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 方向条件类型s
*/
public class CondTypeXmls extends XmlEns
{

		
	/** 
	 方向条件类型s
	*/
	public CondTypeXmls()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new CondTypeXml();
	}
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfData() + "XML/WFAdmin.xml";
	}
	/** 
	 物理表名
	*/
	@Override
	public String getTableName()
	{
		return "CondType";
	}
	@Override
	public Entities getRefEns()
	{
		return null;
	}
}