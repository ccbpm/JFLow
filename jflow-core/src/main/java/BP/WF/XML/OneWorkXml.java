package BP.WF.XML;

import BP.WF.Plant;
import BP.XML.XmlEnNoName;
import BP.XML.XmlEns;

/** 
 工作一户式
*/
public class OneWorkXml extends XmlEnNoName
{

	public final String getName()
	{
		return this.GetValStringByKey(BP.Web.WebUser.getSysLang());
	}
	public final String getURL()
	{
		if (BP.WF.Glo.Plant == Plant.CCFlow)
            return this.GetValStringByKey("UrlCCFlow");
		return this.GetValStringByKey("UrlJFlow");
	}
	/** 
	 节点扩展信息
	*/
	public OneWorkXml()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new OneWorkXmls();
	}
}