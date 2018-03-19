package BP.Sys.XML;

import BP.XML.XmlEn;
import BP.XML.XmlEns;

/**
 * 配置文件信息
 */
public class WebConfigDesc extends XmlEn
{
	// 属性
	private String _No = "";
	
	public final String getNo()
	{
		if (_No.equals(""))
		{
			return this.GetValStringByKey(WebConfigDescAttr.No);
		} else
		{
			return _No;
		}
	}
	
	public final void setNo(String value)
	{
		_No = value;
	}
	
	public final String getName()
	{
		return this.GetValStringByKey(WebConfigDescAttr.Name);
	}
	
	public final boolean getIsEnable()
	{
		if (this.GetValStringByKey(WebConfigDescAttr.IsEnable).equals("0"))
		{
			return false;
		}
		return true;
	}
	
	public final boolean getIsShow()
	{
		if (this.GetValStringByKey(WebConfigDescAttr.IsShow).equals("0"))
		{
			return false;
		}
		return true;
	}
	
	/**
	 * 描述
	 */
	public final String getNote()
	{
		return this.GetValStringByKey(WebConfigDescAttr.Note);
	}
	
	public final String getVals()
	{
		return this.GetValStringByKey(WebConfigDescAttr.Vals);
	}
	
	/**
	 * 类型
	 */
	public final String getDBType()
	{
		return this.GetValStringByKey(WebConfigDescAttr.DBType);
	}
	
	// 构造
	public WebConfigDesc()
	{
	}
	
	/**
	 * 获取一个实例
	 */
	@Override
	public XmlEns getGetNewEntities()
	{
		return new WebConfigDescs();
	}
}