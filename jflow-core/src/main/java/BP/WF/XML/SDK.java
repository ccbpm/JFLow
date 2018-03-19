package BP.WF.XML;

import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 sdk
*/
public class SDK extends XmlEn
{

		
	public final String getNo()
	{
		return this.GetValStringByKey("DoWhat");
	}
	public final String getName()
	{
		return this.GetValStringByKey(BP.Web.WebUser.getSysLang());
	}
	public final String getUrl()
	{
		return this.GetValStringByKey("Url");
	}
	/** 
	 节点扩展信息
	*/
	public SDK()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new SDKs();
	}
}