package BP.WF.XML;

import java.util.List;

import BP.En.Entities;
import BP.Sys.SystemConfig;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 经典模式左侧菜单s
 
*/
public class ClassicMenus extends XmlEns
{

	/** 
	 考核率的数据元素
	*/
	public ClassicMenus()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new ClassicMenu();
	}
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfDataUser() + "XML/Menu.xml";
	}
	/** 
	 物理表名
	*/
	@Override
	public String getTableName()
	{
		return "ClassicMenu";
	}
	@Override
	public Entities getRefEns()
	{
		return null; //new BP.ZF1.AdminTools();
	}
	public List<ClassicMenu> ToJavaList()
	{
		return (List<ClassicMenu>)(Object)this;
	}
}