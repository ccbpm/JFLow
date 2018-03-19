package BP.WF.XML;

import java.util.List;

import BP.En.Entities;
import BP.Sys.SystemConfig;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 工作明细选项s
 
*/
public class WorkOptDtlXmls extends XmlEns
{

		
	/** 
	 工作明细选项s
	*/
	public WorkOptDtlXmls()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new WorkOptDtlXml();
	}
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfWebApp() + "WF/Style/Tools.xml";
	}
	/** 
	 物理表名
	*/
	@Override
	public String getTableName()
	{
		return "WorkOptDtl";
	}
	@Override
	public Entities getRefEns()
	{
		return null;
	}
	public List<WorkOptDtlXml> ToJavaList()
	{
		return (List<WorkOptDtlXml>)(Object)this;
	}
}