package BP.WF.XML;

import BP.XML.XmlEnNoName;
import BP.XML.XmlEns;

/** 
 皮肤
*/
public class Skin extends XmlEnNoName
{
	public final String getName()
	{
		return this.GetValStringByKey(BP.Web.WebUser.getSysLang());
	}
	public final String getCSS()
	{
		return this.GetValStringByKey("CSS");
	}


		
	/** 
	 节点扩展信息
	*/
	public Skin()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new Skins();
	}
}