package BP.WF.XML;

import BP.DA.*;
import BP.En.*;
import BP.Sys.XML.*;
import BP.Sys.*;
import BP.WF.*;

/** 
 管理员
*/
public class Admin2MenuGroup extends XmlEn
{
		///#region 属性
	public final String getNo()
	{
		return this.GetValStringByKey("No");
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
	public final String getName()
	{
		return this.GetValStringByKey("Name");
	}
	public final void setName(String value)
	{
		this.SetVal("Name", value);
	}
	public final String getFor()
	{
		return this.GetValStringByKey("For");
	}
	public final void setFor(String value)
	{
		this.SetVal("For", value);
	}


	/**
	 节点扩展信息
	*/
	public Admin2MenuGroup()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new Admin2MenuGroups();
	}

	/** 
	 是否可以使用？
	 
	 @param no 操作员编号
	 @return 
	*/
	public final boolean IsCanUse(String no) throws Exception
	{
		if (BP.Web.WebUser.getNo().equals("admin") == true && this.getFor().equals("admin"))
		{
			return true;
		}

		if (BP.Web.WebUser.getNo().equals("admin") == true)
		{
			return false;
		}

		if (this.getFor().equals("admin2"))
		{
			return true;
		}
		return false;

	}
}