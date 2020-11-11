package bp.wf.xml;

import bp.da.*;
import bp.en.*;
import bp.sys.*;
import bp.sys.xml.XmlEn;
import bp.sys.xml.XmlEns;
import bp.web.WebUser;
import bp.sys.*;
import bp.wf.*;

/** 
 从表事件
*/
public class EventListDtl extends XmlEn
{

		///属性
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

		///


		///构造
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

		///
}