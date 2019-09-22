package BP.Sys.XML;

import BP.DA.*;
import BP.En.*;
import BP.Sys.XML.*;
import BP.Sys.*;

/** 
 EnumInfoXml 的摘要说明，属性的配置。
*/
public class EnumInfoXml extends XmlEn
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	public final String getName()
	{
		return this.GetValStringByKey(BP.Web.WebUser.getSysLang());
	}
	public final String getKey()
	{
		return this.GetValStringByKey("Key");
	}
	/** 
	 Vals
	*/
	public final String getVals()
	{
		String str = BP.Web.WebUser.getSysLang();
		str = "CH";
		return this.GetValStringByKey(str);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	public EnumInfoXml()
	{
	}
	public EnumInfoXml(String key)
	{
		this.RetrieveByPK("Key", key);
	}

	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new EnumInfoXmls();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}