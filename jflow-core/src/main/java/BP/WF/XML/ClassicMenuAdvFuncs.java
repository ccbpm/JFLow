package BP.WF.XML;

import java.util.List;

import BP.En.Entities;
import BP.Sys.SystemConfig;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 经典模式左侧菜单-高级功能s
 
*/
public class ClassicMenuAdvFuncs extends XmlEns
{

		
	/** 
	 考核率的数据元素
	*/
	public ClassicMenuAdvFuncs()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new ClassicMenuAdvFunc();
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
		return "AdvFunc";
	}
	@Override
	public Entities getRefEns()
	{
		return null; //new BP.ZF1.AdminTools();
	}
	
	public List<ClassicMenuAdvFunc> ToJavaList()
	{
		return (List<ClassicMenuAdvFunc>)(Object)this;
	}
}