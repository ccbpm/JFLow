package BP.Sys.XML;

import java.util.ArrayList;
import java.util.List;

import BP.En.Entities;
import BP.Sys.SystemConfig;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 
 
*/
public class RegularExpressions extends XmlEns
{
	
	public static ArrayList<RegularExpression> convertRegularExpressions(
			Object obj)
	{
		return (ArrayList<RegularExpression>) obj;
	}
	public List<RegularExpression> ToJavaList()
	{
		return (List<RegularExpression>)(Object)this;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 构造
	/**
	 * 考核率的数据元素
	 */
	public RegularExpressions()
	{
	}
	
	// 重写基类属性或方法。
	/**
	 * 得到它的 Entity
	 */
	@Override
	public XmlEn getGetNewEntity()
	{
		return new RegularExpression();
	}
	
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfData() + "/XML/RegularExpression.xml";
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