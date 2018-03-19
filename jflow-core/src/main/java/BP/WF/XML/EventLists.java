package BP.WF.XML;

import java.util.List;

import BP.En.Entities;
import BP.Sys.SystemConfig;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 事件s
*/
public class EventLists extends XmlEns
{

		
	/** 
	 事件s
	*/
	public EventLists()
	{
	}
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
	public List<EventList>ToJavaList()
	{
		return (List<EventList>)(Object)this;
	}
}