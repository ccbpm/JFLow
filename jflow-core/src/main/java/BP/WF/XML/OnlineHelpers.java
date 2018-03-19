package BP.WF.XML;

import BP.En.Entities;
import BP.Sys.SystemConfig;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 在线帮助s
*/
public class OnlineHelpers extends XmlEns
{
	/** 
	 在线帮助s
	*/
	public OnlineHelpers()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new OnlineHelper();
	}
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfWebApp() + "WF/OnlineHepler/";
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