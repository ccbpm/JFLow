package bp.sys.xml;

import bp.en.*;


/** 
 属性集合
*/
public class EnsAppGroupXmls extends XmlEns
{

		///#region 构造
	/** 
	 考核过错行为的数据元素
	*/
	public EnsAppGroupXmls()throws Exception
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
		return new EnsAppGroupXml();
	}
	@Override
	public String getFile()throws Exception
	{
		return bp.difference.SystemConfig.getPathOfXML() + "Ens/EnsAppXml/";
	}
	/** 
	 物理表名
	*/
	@Override
	public String getTableName()  {
		return "Group";
	}
	@Override
	public Entities getRefEns()  {
		return null;
	}

		///#endregion
}