package BP.GPM;

import BP.DA.*;
import BP.En.*;

/** 
 管理员与系统权限
 
*/
public class EmpApp extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	public final String getFK_Emp()
	{
		return this.GetValStringByKey(EmpAppAttr.FK_Emp);
	}
	public final void setFK_Emp(String value)
	{
		this.SetValByKey(EmpAppAttr.FK_Emp, value);
	}
	public final String getFK_App()
	{
		return this.GetValStringByKey(EmpAppAttr.FK_App);
	}
	public final void setFK_App(String value)
	{
		this.SetValByKey(EmpAppAttr.FK_App, value);
	}
	public final String getName()
	{
		return this.GetValStringByKey(AppAttr.Name);
	}
	public final void setName(String value)
	{
		this.SetValByKey(AppAttr.Name, value);
	}
	public final int getIdx()
	{
		return this.GetValIntByKey(AppAttr.Idx);
	}
	public final void setIdx(int value)
	{
		this.SetValByKey(AppAttr.Idx, value);
	}
	/** 
	 图片
	 
	*/
	public final String getImg()
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
	 
	*/
	public final String getUrl()
	{
		return this.GetValStringByKey(AppAttr.Url);
	}
	public final void setUrl(String value)
	{
		this.SetValByKey(AppAttr.Url, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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

		map.AddTBString(AppAttr.Url, null, "连接", true, false, 0, 3900, 20, true);

		map.AddMyFile("图标");

		this.set_enMap(map);
		return this.get_enMap();
	}


}