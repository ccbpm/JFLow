package BP.WF.XML;

import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 映射菜单
*/
public class MapMenu extends XmlEn
{

		
	public final String getNo()
	{
		return this.GetValStringByKey("No");
	}
	public final String getName()
	{
		return this.GetValStringByKey(BP.Web.WebUser.getSysLang());
	}
	public final String getJS()
	{
		return this.GetValStringByKey("JS");
	}
	/** 
	 图片
	*/
	public final String getImg()
	{
		return this.GetValStringByKey("Img");
	}
	/** 
	 说明
	*/
	public final String getNote()
	{
		return this.GetValStringByKey("Note");
	}
	/** 
	 节点扩展信息
	*/
	public MapMenu()
	{
	}
	/** 
	 获取一个实例s
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new MapMenus();
	}
}