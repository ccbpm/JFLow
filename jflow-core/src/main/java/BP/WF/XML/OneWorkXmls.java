package BP.WF.XML;

import BP.DA.*;
import BP.En.*;
import BP.Sys.XML.*;
import BP.Sys.*;
import BP.WF.*;

/** 
 工作一户式s
*/
public class OneWorkXmls extends XmlEns
{

		///#region 构造
	/** 
	 工作一户式s
	*/
	public OneWorkXmls()
	{
	}

		///#endregion


		///#region 重写基类属性或方法。
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getNewEntity()
	{
		return new OneWorkXml();
	}
	/** 
	 文件
	*/
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfData() + "\\Xml\\WFAdmin.xml";
	}
	/** 
	 物理表名
	*/
	@Override
	public String getTableName()
	{
		return "OneWork";
	}
	@Override
	public Entities getRefEns()
	{
		return null;
	}

		///#endregion
}