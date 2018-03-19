package BP.WF.XML;

import BP.En.Entities;
import BP.Sys.SystemConfig;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 抄送菜单s
*/
public class CCMenus extends XmlEns
{
	/** 
	 考核率的数据元素
	*/
	public CCMenus()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new CCMenu();
	}
	/** 
	 XML文件位置.
	*/
	@Override
	public String getFile()
	{
		return SystemConfig.getCCFlowAppPath() + "WF/Data/Xml/SysDataType.xml";
	}
	/** 
	 物理表名
	*/
	@Override
	public String getTableName()
	{
		return "CCMenu";
	}
	@Override
	public Entities getRefEns()
	{
		return null;
	}

}