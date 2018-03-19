package BP.WF.XML;

import BP.En.Entities;
import BP.Sys.SystemConfig;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 工具按钮s
*/
public class ToolBars extends XmlEns
{
	/** 
	 考核率的数据元素
	*/
	public ToolBars()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new ToolBar();
	}
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfWebApp() + "DataUser/XML/BarOfTop.xml";
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
		return null; //new BP.ZF1.AdminTools();
	}
}