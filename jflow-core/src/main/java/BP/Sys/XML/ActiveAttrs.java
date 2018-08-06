package BP.Sys.XML;

import BP.En.Entities;
import BP.GPM.Dept;
import BP.Sys.SystemConfig;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 
 
*/
public class ActiveAttrs extends XmlEns
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 构造
	/**
	 * 考核过错行为的数据元素
	 */
	public ActiveAttrs()
	{
	}
	
	// 重写基类属性或方法。
	/**
	 * 得到它的 Entity
	 */
	@Override
	public XmlEn getGetNewEntity()
	{
		return new ActiveAttr();
	}
	
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfXML() + "Ens/ActiveAttr.xml";
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
	
	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<ActiveAttr> ToJavaList()
	{
		return (java.util.List<ActiveAttr>)(Object)this;
	}
}