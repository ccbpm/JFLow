package BP.Sys.XML;

import BP.DA.*;
import BP.En.*;
import BP.Sys.*;
import BP.Sys.XML.*;
import BP.Sys.*;

/** 
  RegularExpression 正则表达模版
*/
public class RegularExpression extends XmlEn
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
		return this.GetValStringByKey("Name");
	}
	public final String getNote()
	{
		return this.GetValStringByKey("Note");
	}
	public final String getForCtrl()
	{
		return this.GetValStringByKey("ForCtrl");
	}
	public final String getExp()
	{
		return this.GetValStringByKey("Exp");
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 节点扩展信息
	*/
	public RegularExpression()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new RegularExpressions();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}