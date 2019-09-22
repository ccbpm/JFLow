package BP.Sys.XML;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import BP.Sys.*;
import java.util.*;
import java.io.*;
import java.math.*;

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
	public final String getName()
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