package bp.wf.xml;

import bp.difference.SystemConfig;
import bp.en.*;
import bp.sys.xml.XmlEn;
import bp.sys.xml.XmlEns;


/** 
 事件s
*/
public class EventLists extends XmlEns
{
	private static final long serialVersionUID = 1L;
	///构造
	/** 
	 事件s
	*/
	public EventLists()
	{
	}

		///


		///重写基类属性或方法。
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new EventList();
	}
	/** 
	 存放路径
	*/
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfXML() + "EventList.xml";
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

		///
}