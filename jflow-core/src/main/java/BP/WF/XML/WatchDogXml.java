package BP.WF.XML;

import BP.XML.XmlEnNoName;
import BP.XML.XmlEns;

/** 
 流程监控菜单
*/
public class WatchDogXml extends XmlEnNoName
{
	public final String getName()
	{
		return this.GetValStringByKey(BP.Web.WebUser.getSysLang());
	}


		
	/** 
	 流程监控菜单
	*/
	public WatchDogXml()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new WatchDogXmls();
	}
}