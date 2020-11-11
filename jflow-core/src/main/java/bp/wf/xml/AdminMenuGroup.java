package bp.wf.xml;

import bp.da.*;
import bp.en.*;
import bp.sys.*;
import bp.sys.xml.XmlEn;
import bp.sys.xml.XmlEns;
import bp.sys.*;
import bp.wf.*;

/** 
 管理员
*/
public class AdminMenuGroup extends XmlEn
{

		///属性
	public final String getNo()
	{
		return this.GetValStringByKey("No");
	}
	public final void setNo(String value) throws Exception
	{
		this.SetVal("No", value);
	}
	public final String getParentNo()
	{
		return this.GetValStringByKey("ParentNo");
	}
	public final void setParentNo(String value) throws Exception
	{
		this.SetVal("ParentNo", value);
	}
	public final String getName()
	{
		return this.GetValStringByKey("Name");
	}
	public final void setName(String value) throws Exception
	{
		this.SetVal("Name", value);
	}
	public final String getFor()
	{
		return this.GetValStringByKey("For");
	}
	public final void setFor(String value) throws Exception
	{
		this.SetVal("For", value);
	}


		///


		///构造
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

		///

	/** 
	 是否可以使用？
	 
	 @param no 操作员编号
	 @return 
	*/
	public final boolean IsCanUse(String no) throws Exception
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