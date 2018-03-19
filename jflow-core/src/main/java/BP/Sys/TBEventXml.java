package BP.Sys;

import BP.XML.XmlEn;
import BP.XML.XmlEns;

/**
 * 文本框事件
 */
public class TBEventXml extends XmlEn
{
	// 属性
	/**
	 * 事件名称
	 */
	public final String getEventName()
	{
		return this.GetValStringByKey(TBEventXmlList.EventName);
	}
	
	/**
	 * 功能
	 */
	public final String getFunc()
	{
		return this.GetValStringByKey(TBEventXmlList.Func);
	}
	
	/**
	 * 数据为
	 */
	public final String getDFor()
	{
		return this.GetValStringByKey(TBEventXmlList.DFor);
	}
	
	// 构造
	/**
	 * 文本框事件
	 */
	public TBEventXml()
	{
	}
	
	public TBEventXml(String no)
	{
	}
	
	/**
	 * 获取一个实例
	 */
	@Override
	public XmlEns getGetNewEntities()
	{
		return new TBEventXmls();
	}
}