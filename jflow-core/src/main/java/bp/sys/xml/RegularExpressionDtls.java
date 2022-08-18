package bp.sys.xml;

import bp.en.*;

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
	public XmlEn getGetNewEntity()
	{
		return new RegularExpressionDtl();
	}
	@Override
	public String getFile()throws Exception
	{
		return bp.difference.SystemConfig.getPathOfData() + "XML/RegularExpression.xml";
	}
	/** 
	 物理表名
	*/
	@Override
	public String getTableName()  {
		return "Dtl";
	}
	@Override
	public Entities getRefEns()  {
		return null;
	}

		///#endregion

}