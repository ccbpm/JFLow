package BP.Sys.XML;

import java.util.ArrayList;
import java.util.List;

import BP.En.Entities;
import BP.Sys.SystemConfig;
import BP.Sys.FrmLab;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
*/
public class Searchs extends XmlEns
{
	
	public static ArrayList<Search> convertSearchs(Object obj)
	{
		return (ArrayList<Search>) obj;
	}
	public List<Search> ToJavaList()
	{
		return (List<Search>)(Object)this;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 构造
	/**
	 * 考核过错行为的数据元素
	 */
	public Searchs()
	{
	}
	
	// 重写基类属性或方法。
	/**
	 * 得到它的 Entity
	 */
	@Override
	public XmlEn getGetNewEntity()
	{
		return new Search();
	}
	
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfXML() + "/Ens/Search.xml";
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