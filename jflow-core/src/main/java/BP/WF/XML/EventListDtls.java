package BP.WF.XML;

import java.util.List;

import BP.En.Entities;
import BP.Sys.SystemConfig;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 从表事件s
*/
public class EventListDtls extends XmlEns
{

		
	/** 
	 从表事件s
	*/
	public EventListDtls()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new EventListDtl();
	}
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
		return "ItemDtl";
	}
	@Override
	public Entities getRefEns()
	{
		return null;
	}
	public List<EventListDtl> ToJavaList()
	{
		return (List<EventListDtl>)(Object)this;
	}
}