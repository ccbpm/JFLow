package BP.Sys.XML;

import BP.DA.*;
import BP.En.*;
import BP.Sys.XML.*;
import BP.Sys.*;

/** 
 
*/
public class ActiveAttrs extends XmlEns
{

		///#region 构造
	/** 
	 考核过错行为的数据元素
	*/
	public ActiveAttrs()
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
		return new ActiveAttr();
	}
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfXML() + "\\Ens\\ActiveAttr.xml";
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

		///#endregion

}