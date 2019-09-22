package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.Sys.XML.*;

/** 
 多音字
*/
public class ChMulToneXml extends XmlEn
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	public final String getNo()
	{
		return this.GetValStringByKey("No");
	}
	public final String getName()
	{
		return this.GetValStringByKey("Name");
	}
	public final String getDesc()
	{
		return this.GetValStringByKey("No");
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 节点扩展信息
	*/
	public ChMulToneXml()
	{
	}
	/** 
	 获取一个实例s
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new ChMulToneXmls();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}