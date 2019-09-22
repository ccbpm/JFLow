package BP.WF.XML;

import BP.DA.*;
import BP.En.*;
import BP.Sys.XML.*;
import BP.Sys.*;
import BP.WF.*;

/** 
 工作一户式
*/
public class OneWorkXml extends XmlEnNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性.
	public final String getName()
	{
		return this.GetValStringByKey(BP.Web.WebUser.SysLang);
	}
	public final String getURL()
	{
		if (BP.WF.Glo.Plant == Plant.CCFlow)
		{
			return this.GetValStringByKey("UrlCCFlow");
		}
		return this.GetValStringByKey("UrlJFlow");
	}

	public final String getIsDefault()
	{
		return this.GetValStringByKey("IsDefault");
	}


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 节点扩展信息
	*/
	public OneWorkXml()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new OneWorkXmls();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}