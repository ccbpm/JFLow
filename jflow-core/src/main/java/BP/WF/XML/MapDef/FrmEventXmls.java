package BP.WF.XML.MapDef;

import BP.En.Entities;
import BP.Sys.SystemConfig;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/**
 * 表单事件
 */
public class FrmEventXmls extends XmlEns
{
	// 构造
	/**
	 * 考核率的数据元素
	 */
	public FrmEventXmls()
	{
	}
	
	// 重写基类属性或方法。
	/**
	 * 得到它的 Entity
	 */
	@Override
	public XmlEn getGetNewEntity()
	{
		return new FrmEventXml();
	}
	
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfData() + "XML/XmlDB.xml";
	}
	
	/**
	 * 物理表名
	 */
	@Override
	public String getTableName()
	{
		return "FrmEvent";
	}
	
	@Override
	public Entities getRefEns()
	{
		return null; // new BP.ZF1.AdminTools();
	}
}