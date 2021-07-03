package bp.wf.xml;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.en.*;
import bp.sys.*;
import bp.sys.xml.XmlEn;
import bp.sys.xml.XmlEns;
import bp.sys.*;
import bp.wf.*;

/** 
 从表事件s
*/
public class EventListDtls extends XmlEns
{

		///构造
	/** 
	 从表事件s
	*/
	public EventListDtls()
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

		///
}