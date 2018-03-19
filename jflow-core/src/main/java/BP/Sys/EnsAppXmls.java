package BP.Sys;

import java.util.ArrayList;

import BP.En.Entities;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/**
 * 属性集合
 */
public class EnsAppXmls extends XmlEns
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 构造
	/**
	 * 考核过错行为的数据元素
	 */
	public EnsAppXmls()
	{
	}
	
	public static ArrayList<EnsAppXml> convertEnsAppXmls(Object obj)
	{
		return (ArrayList<EnsAppXml>) obj;
	}
	
	// 重写基类属性或方法。
	/**
	 * 得到它的 Entity
	 */
	@Override
	public XmlEn getGetNewEntity()
	{
		return new EnsAppXml();
	}
	
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfXML() + "/Ens/EnsAppXml/GE.xml";
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