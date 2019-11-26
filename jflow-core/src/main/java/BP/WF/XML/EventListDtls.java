package BP.WF.XML;

import BP.DA.*;
import BP.Difference.SystemConfig;
import BP.En.*;
import BP.Sys.*;
import BP.Sys.XML.*;
import BP.WF.*;

/** 
 从表事件s
*/
public class EventListDtls extends XmlEns
{

		///#region 构造
	/** 
	 从表事件s
	*/
	public EventListDtls()
	{
	}

		///#endregion


		///#region 重写基类属性或方法。
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getNewEntity()
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

		///#endregion
}