package bp.wf.xml;

import bp.da.*;
import bp.en.*;
import bp.sys.*;
import bp.sys.xml.XmlEn;
import bp.sys.xml.XmlEns;
import bp.web.WebUser;
import bp.wf.*;

/** 
 事件
*/
public class EventList extends XmlEn
{

		///属性
	public final String getNo()
	{
		return this.GetValStringByKey("No");
	}
	public final String getName()
	{
		return this.GetValStringByKey(WebUser.getSysLang());
	}
	/** 
	 扩展名称
	*/
	public final String getNameHtml()
	{
		if (this.getIsHaveMsg())
		{
			return "<img src='../Img/Message24.png' border=0 width='17px'/>" + this.GetValStringByKey(WebUser.getSysLang());
		}
		else
		{
			return this.GetValStringByKey(WebUser.getSysLang());
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

		///


		///构造
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

		///
}