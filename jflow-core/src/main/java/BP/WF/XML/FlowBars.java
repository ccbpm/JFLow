package BP.WF.XML;

import BP.En.Entities;
import BP.Sys.SystemConfig;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 流程按钮s
*/
public class FlowBars extends XmlEns
{

		
	/** 
	 考核率的数据元素
	*/
	public FlowBars()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new FlowBar();
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
		return "FlowBar";
	}
	@Override
	public Entities getRefEns()
	{
		return null; //new BP.ZF1.AdminTools();
	}
}