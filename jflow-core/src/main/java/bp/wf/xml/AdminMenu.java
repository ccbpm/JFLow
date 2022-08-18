package bp.wf.xml;

import bp.da.*;
import bp.en.*;
import bp.sys.xml.*;
import bp.sys.*;
import bp.*;
import bp.wf.*;

/** 
 管理员
*/
public  class AdminMenu extends XmlEn
{

		///#region 属性
	/** 
	 菜单编号
	*/
	public final String getNo() throws Exception
	{
		return this.GetValStringByKey("No");
	}
	public final void setNo(String value)throws Exception
	{this.SetVal("No", value);
	}
	/** 
	 分组编号
	*/
	public final String getGroupNo() throws Exception
	{
		return this.GetValStringByKey("GroupNo");
	}
	public final void setGroupNo(String value)throws Exception
	{this.SetVal("GroupNo", value);
	}
	/** 
	 名称
	*/
	public final String getName() throws Exception
	{
		return this.GetValStringByKey("Name");
	}
	public final void setName(String value)throws Exception
	{this.SetVal("Name", value);
	}
	/** 
	 应用范围
	*/
	public final String getFor() throws Exception
	{
		return this.GetValStringByKey("For");
	}
	public final void setFor(String value)throws Exception
	{this.SetVal("For", value);
	}
	/** 
	 Url菜单
	*/
	public final String getUrl() throws Exception
	{
		return this.GetValStringByKey("Url");
	}
	public final void setUrl(String value)throws Exception
	{this.SetVal("Url", value);
	}

		///#endregion


		///#region 构造
	/** 
	 节点扩展信息
	*/
	public AdminMenu()  {
	}

	@Override
	public XmlEns getGetNewEntities() throws Exception {
		return null;
	}

	/**
	 获取一个实例
	*/

	public XmlEns getNewEntities()  {
		return new AdminMenus();
	}

		///#endregion

	/** 
	 是否可以使用？
	 
	 param no 操作员编号
	 @return 
	*/
	public final boolean IsCanUse(String no)
	{
		return true;
	}
}