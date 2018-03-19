package BP.WF.XML;

import BP.En.Entities;
import BP.Sys.SystemConfig;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 
 
*/
public class SDKs extends XmlEns
{

		
	/** 
	 考核率的数据元素
	*/
	public SDKs()
	{
	}
	/** 
	 得到它的 Entity 
	 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new SDK();
	}
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfData() + "XML/SDK.xml";
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
		return null; //new BP.ZF1.AdminTools();
	}

}