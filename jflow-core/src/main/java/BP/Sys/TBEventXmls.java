package BP.Sys;

import BP.En.Entities;
import BP.Sys.SystemConfig;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/**
 * 文本框事件s
 */
public class TBEventXmls extends XmlEns
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 构造
	/**
	 * 文本框事件s
	 */
	public TBEventXmls()
	{
	}
	
	/**
	 * 文本框事件s
	 * 
	 * @param dFor
	 */
	public TBEventXmls(String dFor)
	{
		this.Retrieve(TBEventXmlList.DFor, dFor);
	}
	
	// 重写基类属性或方法。
	/**
	 * 得到它的 Entity
	 */
	@Override
	public XmlEn getGetNewEntity()
	{
		return new TBEventXml();
	}
	
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfXML() + "MapExt.xml";
	}
	
	/**
	 * 物理表名
	 */
	@Override
	public String getTableName()
	{
		return "TBEvent";
	}
	
	@Override
	public Entities getRefEns()
	{
		return null; // new BP.ZF1.AdminTools();
	}
}