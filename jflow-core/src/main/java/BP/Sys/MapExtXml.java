package BP.Sys;

import BP.XML.XmlEnNoName;
import BP.XML.XmlEns;

public class MapExtXml extends XmlEnNoName
{

		
	public final String getName()
	{
		return this.GetValStringByKey(BP.Web.WebUser.getSysLang());
	}
	public final String getURL()
	{
		return this.GetValStringByKey("URL");
	}

		///#endregion


		
	/** 
	 节点扩展信息
	 
	*/
	public MapExtXml()
	{
	}
	/** 
	 获取一个实例
	 
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new MapExtXmls();
	}

		///#endregion
}