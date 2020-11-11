package bp.wf.xml;
import bp.sys.xml.XmlEn;
import bp.sys.xml.XmlEns;
import bp.web.WebUser;

/** 
 管理员
*/
public class Admin2MenuGroup extends XmlEn
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

		///

	/** 
	 是否可以使用？
	 
	 @param no 操作员编号
	 @return 
	*/
	public final boolean IsCanUse(String no) throws Exception
	{
		if (WebUser.getNo().equals("admin") == true && this.getFor().equals("admin"))
		{
			return true;
		}

		if (WebUser.getNo().equals("admin") == true)
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