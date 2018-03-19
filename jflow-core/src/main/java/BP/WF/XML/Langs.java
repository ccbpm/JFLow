package BP.WF.XML;

import BP.En.Entities;
import BP.Sys.SystemConfig;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 语言s
*/
public class Langs extends XmlEns
{

		
	/** 
	 考核率的数据元素
	*/
	public Langs()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new Lang();
	}
	/** 
	 XML文件位置.
	*/
	@Override
	public String getFile()
	{
		return SystemConfig.getCCFlowAppPath() + "WF/Style/Tools.xml";
	}
	/** 
	 物理表名
	*/
	@Override
	public String getTableName()
	{
		return "Lang";
	}
	@Override
	public Entities getRefEns()
	{
		return null;
	}
}