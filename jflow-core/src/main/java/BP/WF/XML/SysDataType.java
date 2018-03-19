package BP.WF.XML;

import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 数据类型
*/
public class SysDataType extends XmlEn
{

		
	public final String getNo()
	{
		return this.GetValStringByKey("No");
	}
	public final String getName()
	{
		return this.GetValStringByKey(BP.Web.WebUser.getSysLang());
	}
	/** 
	 图片
	 
	*/
	public final String getDesc()
	{
		return this.GetValStringByKey("Desc");
	}
	/** 
	 节点扩展信息
	*/
	public SysDataType()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new SysDataTypes();
	}
}