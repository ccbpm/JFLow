package bp.sys.xml;


public abstract class XmlEnNoName extends XmlEn
{
	public final String getNo()  throws Exception
	{
		return this.GetValStringByKey("No");
	}
	public final void setNo(String value) throws Exception
	{
		this.SetVal("No", value);
	}
	public String getName()  throws Exception
	{
		return this.GetValStringByKey("Name");
	}
	public final void setName(String value) throws Exception
	{
		this.SetVal("Name", value);
	}
	public XmlEnNoName()
	{
	}
	public XmlEnNoName(String no) throws Exception 
	{
	   int i = this.RetrieveByPK("No", no);
	   if (i == 0)
	   {
		   throw new RuntimeException("@没有查询到 No =" + no + " XML数据.");
	   }
	}
}