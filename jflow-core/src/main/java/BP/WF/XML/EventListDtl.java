package BP.WF.XML;

import BP.DA.*;
import BP.En.*;
import BP.Sys.*;
import BP.Sys.XML.*;
import BP.WF.*;
import BP.Web.WebUser;

/** 
 从表事件
*/
public class EventListDtl extends XmlEn
{

		///#region 属性
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
		return this.GetValStringByKey(WebUser.getSysLang());
	}
	/** 
	 描述
	*/
	public final String getEventDesc()
	{
		return this.GetValStringByKey("EventDesc");
	}

		///#endregion


		///#region 构造
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

		///#endregion
}