package BP.WF.XML;

import BP.XML.XmlEnNoName;
import BP.XML.XmlEns;

/** 
 流程一户式
*/
public class RptXml extends XmlEnNoName
{
	public final String getName()
	{
		return this.GetValStringByKey(BP.Web.WebUser.getSysLang());
	}
	public final String getURL()
	{
		return this.GetValStringByKey("URL");
	}
	public final String getICON()
	{
		return this.GetValStringByKey("ICON");
	}
	/** 
	 节点扩展信息
	*/
	public RptXml()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new RptXmls();
	}
}