package BP.WF.XML;

import BP.XML.XmlEnNoName;
import BP.XML.XmlEns;

/** 
 工作明细选项
*/
public class WorkOptDtlXml extends XmlEnNoName
{
	/** 
	 名称
	*/
	public final String getName()
	{
		return this.GetValStringByKey(BP.Web.WebUser.getSysLang());
	}
	/** 
	 超链接
	*/
	public final String getURL()
	{
		return this.GetValStringByKey("URL");
	}
		
	/** 
	 节点扩展信息
	*/
	public WorkOptDtlXml()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new WorkOptDtlXmls();
	}
	
}