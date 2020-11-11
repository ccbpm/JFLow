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
public class Admin2Menu extends XmlEn
{

		///属性
	/** 
	 菜单编号
	*/
	public final String getNo()
	{
		return this.GetValStringByKey("No");
	}
	public final void setNo(String value) throws Exception
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
	public final void setGroupNo(String value) throws Exception
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
	public final void setName(String value) throws Exception
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
	public final void setFor(String value) throws Exception
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
	public final void setUrl(String value) throws Exception
	{
		this.SetVal("Url", value);
	}

		///


		///构造
	/** 
	 节点扩展信息
	*/
	public Admin2Menu()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new Admin2Menus();
	}

		///
}