package BP.Sys.XML;

import BP.DA.*;
import BP.En.*;
import BP.Sys.*;
import BP.Sys.XML.*;
import BP.Sys.*;

/** 
 
*/
public class RegularExpressions extends XmlEns
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 正则表达模版
	*/
	public RegularExpressions()
	{
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 重写基类属性或方法。
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getNewEntity()
	{
		return new RegularExpression();
	}
	/** 
	 文件路径
	*/
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfData() + "\\XML\\RegularExpression.xml";
	}
	/** 
	 物理表名
	*/
	@Override
	public String getTableName()
	{
		return "Item";
	}
	@Override
	public Entities getRefEns()
	{
		return null;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

}