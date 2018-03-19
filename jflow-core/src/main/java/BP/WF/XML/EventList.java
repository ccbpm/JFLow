package BP.WF.XML;

import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 事件
*/
public class EventList extends XmlEn
{

		
	public final String getNo()
	{
		return this.GetValStringByKey("No");
	}
	public final String getName()
	{
		return this.GetValStringByKey(BP.Web.WebUser.getSysLang());
	}
	/** 
	 扩展名称
	 
	*/
	public final String getNameHtml()
	{
		if (this.getIsHaveMsg())
		{
			return "<img src='../Img/Message24.png' border=0 width='17px'/>" + this.GetValStringByKey(BP.Web.WebUser.getSysLang());
		}
		else
		{
			return this.GetValStringByKey(BP.Web.WebUser.getSysLang());
		}
	}
	/** 
	 输入描述
	 
	*/
	public final String getEventDesc()
	{
		return this.GetValStringByKey("EventDesc");
	}
	/** 
	 事件类型
	*/
	public final String getEventType()
	{
		return this.GetValStringByKey("EventType");
	}
	/** 
	 是否有消息
	*/
	public final boolean getIsHaveMsg()
	{
		return this.GetValBoolByKey("IsHaveMsg");
	}

	/** 
	 事件
	*/
	public EventList()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new EventLists();
	}
}