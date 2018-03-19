package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.Sys.XML.*;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 分组内容
 
*/
public class FieldGroupXml extends XmlEn
{
	//#region 属性
	public final String getNo()
	{
		return this.GetValStringByKey("No");
	}
	public final String getName()
	{
		return this.GetValStringByKey(BP.Web.WebUser.getSysLang());
	}
	public final String getDesc()
	{
		return this.GetValStringByKey(BP.Web.WebUser.getSysLang()+"Desc");
	}

		///#endregion


		
	/** 
	 节点扩展信息
	 
	*/
	public FieldGroupXml()
	{
	}
	/** 
	 获取一个实例s
	 
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new FieldGroupXmls();
	}

		///#endregion
}