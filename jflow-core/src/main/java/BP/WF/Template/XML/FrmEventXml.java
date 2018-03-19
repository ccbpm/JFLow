package BP.WF.Template.XML;

import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 表单事件
 
*/
public class FrmEventXml extends XmlEn
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

		///#endregion


		
	/** 
	 表单事件
	 
	*/
	public FrmEventXml()
	{
	}
	/** 
	 获取一个实例
	 
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new FrmEventXmls();
	}

		///#endregion
}