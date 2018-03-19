package BP.Sys.XML;

import BP.En.Entities;
import BP.Sys.SystemConfig;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/**
 * 配置文件信息
 */
public class WebConfigDescs extends XmlEns
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 构造
	/**
	 * 配置文件信息
	 */
	public WebConfigDescs()
	{
	}
	
	// 重写基类属性或方法。
	/**
	 * 得到它的 Entity
	 */
	@Override
	public XmlEn getGetNewEntity()
	{
		return new WebConfigDesc();
	}
	
	/**
	 * 文件
	 */
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfXML() + "/WebConfigDesc.xml";
	}
	
	/**
	 * 物理表名
	 */
	@Override
	public String getTableName()
	{
		return "Item";
	}
	
	@Override
	public Entities getRefEns()
	{
		return null;
	}
}