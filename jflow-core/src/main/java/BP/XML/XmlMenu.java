package BP.XML;

public abstract class XmlMenu extends XmlEnNoName
{
	/**
	 * 功能编号
	 */
	public final String getImg()
	{
		return this.GetValStringByKey("Img");
	}
	
	public final String getName()
	{
		return this.GetValStringByKey("Name");
	}
	
	public final String getUrl()
	{
		return this.GetValStringByKey("Url");
	}
	
	public final String getTarget()
	{
		return this.GetValStringByKey("Target");
	}
	
	public XmlMenu()
	{
	}
	
	public XmlMenu(String no)
	{
		this.RetrieveByPK("No", no);
	}
}