package BP.Sys;

import BP.Web.WebUser;
import BP.XML.XmlEnNoName;
import BP.XML.XmlEns;

public class InputCheckXml extends XmlEnNoName
{
	// 属性
	public final String getName()
	{
		return this.GetValStringByKey(WebUser.getSysLang());
	}
	
	// 构造
	/**
	 * 节点扩展信息
	 */
	public InputCheckXml()
	{
	}
	
	public InputCheckXml(String no)
	{
		
	}
	
	/**
	 * 获取一个实例
	 */
	@Override
	public XmlEns getGetNewEntities()
	{
		return new InputCheckXmls();
	}
}