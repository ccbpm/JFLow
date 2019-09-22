package BP.Sys.XML;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import BP.Sys.*;

public abstract class XmlMenu extends XmlEnNoName
{
	/** 
	 功能编号
	*/
	public final String getImg()
	{
		return this.GetValStringByKey("Img");
	}
	/** 
	 名称.
	*/
	public final String getName()
	{
		return this.GetValStringByKey("Name");
	}
	/** 
	 URL
	*/
	public final String getUrl()
	{
		return this.GetValStringByKey("Url");
	}
	public final String getTarget()
	{
		return this.GetValStringByKey("Target");
	}
	/** 
	 菜单
	*/
	public XmlMenu()
	{
	}
	/** 
	 菜单
	 
	 @param no
	*/
	public XmlMenu(String no)
	{
		this.RetrieveByPK("No", no);
	}
}