package BP.Pub;

import BP.DA.*;
import BP.En.*;
import BP.Web.Controls.*;
import BP.Web.*;
import BP.Sys.*;

/** 
 报表基类
*/
public abstract class Rpt2Base
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 报表基类
	*/
	public Rpt2Base()
	{
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 构造方法

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 要求子类强制重写的属性.
	/** 
	 显示的标题.
	*/
	public abstract String getTitle();
	/** 
	 默认选择的属性.
	*/
	public abstract int getAttrDefSelected();
	/** 
	 分组显示属性, 多个属性用@符号隔开.
	*/
	public abstract Rpt2Attrs getAttrsOfGroup();
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 要求子类重写的属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 提供给操作者的方法.
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 提供给操作者的方法
}