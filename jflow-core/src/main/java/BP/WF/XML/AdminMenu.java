package BP.WF.XML;

import BP.DA.*;
import BP.En.*;
import BP.Sys.XML.*;
import BP.Sys.*;
import BP.WF.*;

/** 
 管理员
*/
public class AdminMenu extends XmlEn
{
	/**
	 菜单编号
	*/
	public final String getNo()
	{
		return this.GetValStringByKey("No");
	}
	public final void setNo(String value)
	{
		this.SetVal("No", value);
	}
	/** 
	 分组编号
	*/
	public final String getGroupNo()
	{
		return this.GetValStringByKey("GroupNo");
	}
	public final void setGroupNo(String value)
	{
		this.SetVal("GroupNo", value);
	}
	/** 
	 名称
	*/
	public final String getName()
	{
		return this.GetValStringByKey("Name");
	}
	public final void setName(String value)
	{
		this.SetVal("Name", value);
	}
	/** 
	 应用范围
	*/
	public final String getFor()
	{
		return this.GetValStringByKey("For");
	}
	public final void setFor(String value)
	{
		this.SetVal("For", value);
	}
	/** 
	 Url菜单
	*/
	public final String getUrl()
	{
		return this.GetValStringByKey("Url");
	}
	public final void setUrl(String value)
	{
		this.SetVal("Url", value);
	}

	///#region 构造
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

	/** 
	 是否可以使用？
	 
	 @param no 操作员编号
	 @return 
	*/
	public final boolean IsCanUse(String no)
	{
		return true;
	}
}