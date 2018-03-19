package BP.WF.XML;

import BP.En.Entities;
import BP.Sys.SystemConfig;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 流程监控菜单s
*/
public class WatchDogXmls extends XmlEns
{

		
	/** 
	 流程监控菜单s
	*/
	public WatchDogXmls()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new WatchDogXml();
	}
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfWebApp() + "WF/Admin/Sys/Sys.xml";
	}
	/** 
	 表
	*/
	@Override
	public String getTableName()
	{
		return "WatchDog";
	}
	@Override
	public Entities getRefEns()
	{
		return null;
	}
}