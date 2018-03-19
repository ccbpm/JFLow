package BP.WF.XML;

import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 从表事件
*/
public class EventListDtl extends XmlEn
{

		
	/** 
	 编号
	*/
	public final String getNo()
	{
		return this.GetValStringByKey("No");
	}
	/** 
	 名称
	*/
	public final String getName()
	{
		return this.GetValStringByKey(BP.Web.WebUser.getSysLang());
	}
	/** 
	 描述
	*/
	public final String getEventDesc()
	{
		return this.GetValStringByKey("EventDesc");
	}
	/** 
	 从表事件
	*/
	public EventListDtl()
	{
	}
	/** 
	 获取一个实例
	 
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new EventListDtls();
	}
}