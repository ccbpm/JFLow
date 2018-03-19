package BP.WF.XML;

import BP.XML.XmlEnNoName;
import BP.XML.XmlEns;

/** 
 方向条件类型
*/
public class CondTypeXml extends XmlEnNoName
{
	public final String getName()
	{
		return this.GetValStringByKey(BP.Web.WebUser.getSysLang());
	}
		
	/** 
	 方向条件类型
	*/
	public CondTypeXml()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new CondTypeXmls();
	}
}