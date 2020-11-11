package bp.sys.xml;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.en.*;
import bp.sys.*;
import bp.sys.*;
import bp.*;
import bp.sys.*;

/** 
 
*/
public class RegularExpressions extends XmlEns
{

		///构造
	/** 
	 正则表达模版
	*/
	public RegularExpressions()
	{
	}

		///


		///重写基类属性或方法。
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new RegularExpression();
	}
	/** 
	 文件路径
	*/
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfData() + "/XML/RegularExpression.xml";
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

		///

}