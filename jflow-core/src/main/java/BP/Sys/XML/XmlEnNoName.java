package BP.Sys.XML;

public abstract class XmlEnNoName extends XmlEn
{
	public final String getNo()
	{
		return this.GetValStringByKey("No");
	}
	
	public final void setNo(String value)
	{
		this.SetVal("No", value);
	}
	
	public String getName()
	{
		return this.GetValStringByKey("Name");
	}
	
	public final void setName(String value)
	{
		this.SetVal("Name", value);
	}
	
	public XmlEnNoName()
	{
	}
	
	public XmlEnNoName(String no)
	{
		int i = this.RetrieveByPK("No", no);
		if (i == 0)
		{
			throw new RuntimeException("@没有查询到 No =" + no + " XML数据.");
		}
	}
}