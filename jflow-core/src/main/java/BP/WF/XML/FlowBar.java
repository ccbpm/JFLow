package BP.WF.XML;

import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 流程按钮
*/
public class FlowBar extends XmlEn
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
	public final String getImg()
	{
		return this.GetValStringByKey("Img");
	}
	public final String getTitle()
	{
		return this.GetValStringByKey("Title");
	}
	public final String getUrl()
	{
		return this.GetValStringByKey("Url");
	}
		
	/** 
	 节点扩展信息
	*/
	public FlowBar()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new FlowBars();
	}
}