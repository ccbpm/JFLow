package BP.WF.XML;

import BP.DA.*;
import BP.En.*;
import BP.Sys.XML.*;
import BP.Sys.*;
import BP.Sys.Plant;
import BP.WF.*;

/** 
 工作一户式
*/
public class OneWorkXml extends XmlEnNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性.
 
	public final String getURL()
	{
		 
			return this.GetValStringByKey("Url"+BP.WF.Glo.Plant);
		 
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