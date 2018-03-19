package BP.WF.XML;

import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 工具按钮
*/
public class ToolBar extends XmlEn
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
		 String url=this.GetValStringByKey("Url");
		 if (url.equals(""))
		 {
			 url = "javascript:" + this.GetValStringByKey("OnClick");
		 }
		 return url;
	}
	/** 
	 节点扩展信息
	*/
	public ToolBar()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new ToolBars();
	}
}