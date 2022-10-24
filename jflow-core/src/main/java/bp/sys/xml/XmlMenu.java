package bp.sys.xml;


public abstract class XmlMenu extends XmlEnNoName
{
	/** 
	 功能编号
	*/
	public final String getImg()  throws Exception
	{
		return this.GetValStringByKey("Img");
	}

	/** 
	 URL
	*/
	public final String getUrl()  throws Exception
	{
		return this.GetValStringByKey("Url");
	}
	public final String getTarget()  throws Exception
	{
		return this.GetValStringByKey("Target");
	}
	/** 
	 菜单
	*/
	public XmlMenu()throws Exception
	{
	}
	/** 
	 菜单
	 
	 param no
	*/
	public XmlMenu(String no) throws Exception 
	{
		this.RetrieveByPK("No", no);
	}
}