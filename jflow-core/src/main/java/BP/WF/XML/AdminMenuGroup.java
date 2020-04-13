package BP.WF.XML;

import BP.DA.DataType;
import BP.Sys.XML.*;


/** 
 管理员
*/
public class AdminMenuGroup extends XmlEn
{
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


	///#region 构造
	/** 
	 节点扩展信息
	*/
	public AdminMenuGroup()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new AdminMenuGroups();
	}

	/** 
	 是否可以使用？
	 
	 @param no 操作员编号
	 @return 
	*/
	public final boolean IsCanUse(String no)
	{
		if (DataType.IsNullOrEmpty(this.getFor()))
		{
			return true;
		}

		if (this.getFor().equals(no))
		{
			return true;
		}

		if (this.getFor().equals("SecondAdmin"))
		{
			return true;
		}

		return false;
	}
}