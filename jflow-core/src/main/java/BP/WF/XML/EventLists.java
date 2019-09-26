package BP.WF.XML;

import BP.DA.*;
import BP.En.*;
import BP.Sys.XML.*;
import BP.Sys.*;
import BP.WF.*;

/** 
 事件s
*/
public class EventLists extends XmlEns
{

		///#region 构造
	/** 
	 事件s
	*/
	public EventLists()
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
		return new EventList();
	}
	/** 
	 存放路径
	*/
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfXML() + "\\EventList.xml";
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

		///#endregion
}