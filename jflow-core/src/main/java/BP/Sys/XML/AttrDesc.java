package BP.Sys.XML;

import BP.XML.XmlEn;
import BP.XML.XmlEns;

/**
 * AttrDesc 的摘要说明，属性的配置。
 */
public class AttrDesc extends XmlEn
{
	// 属性
	public final String getAttr()
	{
		return this.GetValStringByKey(AttrDescAttr.Attr);
	}
	
	public final String getFor()
	{
		return this.GetValStringByKey(AttrDescAttr.For);
	}
	
	public final String getDesc()
	{
		return this.GetValStringByKey(AttrDescAttr.Desc);
	}
	
	public final boolean getIsAcceptFile()
	{
		String s = this.GetValStringByKey(AttrDescAttr.IsAcceptFile);
		if (s == null || s.equals("") || s.equals("0"))
		{
			return false;
		}
		
		return true;
	}
	
	public final int getHeight()
	{
		String str = this.GetValStringByKey(AttrDescAttr.Height);
		if (str == null || str.equals(""))
		{
			return 200;
		}
		return Integer.parseInt(str);
	}
	
	// 构造
	public AttrDesc()
	{
	}
	
	/**
	 * 获取一个实例
	 */
	@Override
	public XmlEns getGetNewEntities()
	{
		return new AttrDescs();
	}
}