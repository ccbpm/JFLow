package BP.Sys.XML;

import java.util.ArrayList;
import java.util.List;

import BP.En.Entities;
import BP.Sys.SystemConfig;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/**
 * 属性集合
 */
public class EnumInfoXmls extends XmlEns
{
	public static ArrayList<EnumInfoXml> convertEnumInfoXmls(Object obj)
	{
		return (ArrayList<EnumInfoXml>) obj;
	}
	public List<EnumInfoXml> ToJavaList()
	{
		return (List<EnumInfoXml>)(Object)this;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 构造
	/**
	 * 考核过错行为的数据元素
	 */
	public EnumInfoXmls()
	{
	}
	
	// 重写基类属性或方法。
	/**
	 * 得到它的 Entity
	 */
	@Override
	public XmlEn getGetNewEntity()
	{
		return new EnumInfoXml();
	}
	
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfXML() + "/Enum/";
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