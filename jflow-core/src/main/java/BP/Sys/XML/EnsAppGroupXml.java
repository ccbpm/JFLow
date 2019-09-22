package BP.Sys.XML;

import BP.DA.*;
import BP.En.*;
import BP.Sys.XML.*;
import BP.Sys.*;

/** 
 EnsAppGroupXml 的摘要说明，属性的配置。
*/
public class EnsAppGroupXml extends XmlEnNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	/** 
	 类名
	*/
	public final String getEnsName()
	{
		return this.GetValStringByKey(EnsAppGroupXmlEnsName.EnsName);
	}
	/** 
	 数据类型
	*/
	public final String getGroupName()
	{
		return this.GetValStringByKey(EnsAppGroupXmlEnsName.GroupName);
	}
	/** 
	 描述
	*/
	public final String getGroupKey()
	{
		return this.GetValStringByKey(EnsAppGroupXmlEnsName.GroupKey);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	public EnsAppGroupXml()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new EnsAppGroupXmls();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}