package BP.WF.XML;

import BP.XML.XmlEnNoName;
import BP.XML.XmlEns;

/** 
 数据源类型
*/
public class DBSrc extends XmlEnNoName
{

		
	/** 
	 数据源类型
	*/
	public final String getSrcType()
	{
		return this.GetValStringByKey(DBSrcAttr.SrcType);
	}
	/** 
	 数据源类型URL
	*/
	public final String getUrl()
	{
		return this.GetValStringByKey(DBSrcAttr.Url);
	}
	/** 
	 数据源类型
	*/
	public DBSrc()
	{
	}
	/** 
	 数据源类型s
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new DBSrcs();
	}
}