package BP.GPM;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;
import BP.En.Map;

import java.util.*;

/** 
 管理员与系统权限
*/
public class EmpApp extends EntityMyPK
{

		///#region 属性
	public final String getFK_Emp() throws Exception
	{
		return this.GetValStringByKey(EmpAppAttr.FK_Emp);
	}
	public final void setFK_Emp(String value) throws Exception
	{
		this.SetValByKey(EmpAppAttr.FK_Emp, value);
	}
	public final String getFK_App() throws Exception
	{
		return this.GetValStringByKey(EmpAppAttr.FK_App);
	}
	public final void setFK_App(String value) throws Exception
	{
		this.SetValByKey(EmpAppAttr.FK_App, value);
	}
	public final String getName() throws Exception
	{
		return this.GetValStringByKey(AppAttr.Name);
	}
	public final void setName(String value) throws Exception
	{
		this.SetValByKey(AppAttr.Name, value);
	}
	public final int getIdx() throws Exception
	{
		return this.GetValIntByKey(AppAttr.Idx);
	}
	public final void setIdx(int value) throws Exception
	{
		this.SetValByKey(AppAttr.Idx, value);
	}
	/** 
	 图片
	 * @throws Exception 
	*/
	public final String getImg() throws Exception
	{
		String s = this.GetValStringByKey("WebPath");
		if (DataType.IsNullOrEmpty(s))
		{
			return "../../DataUser/BP.GPM.STem/laptop.png";
		}
		else
		{
			return s;
		}
	}
	/** 
	 超链接
	 * @throws Exception 
	*/
	public final String getUrlExt() throws Exception
	{
		return this.GetValStringByKey(AppAttr.UrlExt);
	}
	public final void setUrl(String value) throws Exception
	{
		this.SetValByKey(AppAttr.UrlExt, value);
	}

		///#endregion


		///#region 构造方法
	/** 
	 管理员与系统权限
	*/
	public EmpApp()
	{
	}
	/** 
	 管理员与系统权限
	 
	 @param mypk
	 * @throws Exception 
	*/
	public EmpApp(String no) throws Exception
	{
		this.Retrieve();
	}
	/** 
	 管理员与系统权限
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("GPM_EmpApp");
		map.setDepositaryOfEntity(Depositary.None);
		map.setDepositaryOfMap(Depositary.Application);
		map.setEnDesc("管理员与系统权限");
		map.setEnType(EnType.App);

		map.AddMyPK();

		map.AddTBString(EmpAppAttr.FK_Emp, null, "操作员", true, false, 0, 50, 20);
		map.AddTBString(EmpAppAttr.FK_App, null, "系统", true, false, 0, 50, 20);

		map.AddTBString(AppAttr.Name, null, "系统-名称", true, false, 0, 3900, 20);

		map.AddTBString(AppAttr.UrlExt, null, "连接", true, false, 0, 3900, 20, true);

		map.AddMyFile("图标");

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}