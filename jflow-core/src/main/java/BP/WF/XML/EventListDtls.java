package BP.WF.XML;

import BP.DA.*;
import BP.En.*;
import BP.Sys.*;
import BP.Sys.XML.*;
import BP.WF.*;

/** 
 从表事件s
*/
public class EventListDtls extends XmlEns
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 从表事件s
	*/
	public EventListDtls()
	{
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 重写基类属性或方法。
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
		return SystemConfig.getPathOfXML() + "\\EventList.xml";
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}