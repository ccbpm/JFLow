package BP.Sys.XML;

import BP.DA.*;
import BP.Difference.SystemConfig;
import BP.Sys.*;
import BP.En.*;
import BP.Sys.XML.*;
import BP.Sys.*;

/** 
 
*/
public class RegularExpressionDtls extends XmlEns
{

		///#region 构造
	/** 
	 考核率的数据元素
	*/
	public RegularExpressionDtls()
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
		return new RegularExpressionDtl();
	}
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfData() + "XML/RegularExpression.xml";
	}
	/** 
	 物理表名
	*/
	@Override
	public String getTableName()
	{
		return "Dtl";
	}
	@Override
	public Entities getRefEns()
	{
		return null;
	}

		///#endregion

}