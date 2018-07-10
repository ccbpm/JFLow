package BP.WF.XML;

import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 管理员
*/
public class AdminMenu extends XmlEn
{

		
	public final String getNo()
	{
		return this.GetValStringByKey("No");
	}
	public final String getName()
	{
		return this.GetValStringByKey(BP.Web.WebUser.getSysLang());
	}
	public final String getGroupNo()
	{
		return this.GetValStringByKey("GroupNo");
	}
	public final void setGroupNo(String value)
	{
		this.SetVal("GroupNo", value);
	}
	public final String getUrl()
	{
		return this.GetValStringByKey("Url");
	}
	public final String getFor()
	{
		return this.GetValStringByKey("For");
	}
	public final void setFor(String value)
	{
		this.SetVal("For", value);
	}
	public final void setNo(String value)
	{
		this.SetVal("No", value);
	}
	public final String getParentNo()
	{
		return this.GetValStringByKey("ParentNo");
	}
	public final void setParentNo(String value)
	{
		this.SetVal("ParentNo", value);
	}
	public final void setName(String value)
	{
		this.SetVal("Name", value);
	}
	public final void setUrl(String value)
	{
		this.SetVal("Url", value);
	}
	/** 是否可以使用？
	 
	 @param no 操作员编号
	 @return 
*/
	public final boolean IsCanUse(String no)
	{
		if (this.getFor().equals(""))
		{
			return true;
		}

		if (no.equals(this.getFor()))
		{
			return true;
		}

		if (this.getFor().equals("SecondAdmin"))
		{
			return true;
		}

		return false;
	}
	/** 
	 节点扩展信息
	*/
	public AdminMenu()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new AdminMenus();
	}
}