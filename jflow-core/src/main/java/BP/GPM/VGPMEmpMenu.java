package BP.GPM;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.Sys.OSModel;

/** 
 操作员 的摘要说明。
 
*/
public class VGPMEmpMenu extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	public final String getCtrlObjs()
	{
		return this.GetValStringByKey(MenuAttr.CtrlObjs);
	}
	public final void setCtrlObjs(String value)
	{
		this.SetValByKey(MenuAttr.CtrlObjs, value);
	}
	   
	/** 
	 是否是ccSytem
	 
	*/
	public final int getMenuType()
	{
		return this.GetValIntByKey(MenuAttr.MenuType);
	}
	public final void setMenuType(int value)
	{
		this.SetValByKey(MenuAttr.MenuType, value);
	}
	public final int getIdx()
	{
		return this.GetValIntByKey(MenuAttr.Idx);
	}
	public final void setIdx(int value)
	{
		this.SetValByKey(MenuAttr.Idx, value);
	}

	public final String getFK_App()
	{
		return this.GetValStringByKey(MenuAttr.FK_App);
	}
	public final void setFK_App(String value)
	{
		this.SetValByKey(MenuAttr.FK_App, value);
	}
 
	public final String getUrl()
	{
		return this.GetValStringByKey(MenuAttr.Url);
	}
	public final void setUrl(String value)
	{
		this.SetValByKey(MenuAttr.Url, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 人员菜单功能
	 
	*/
	public VGPMEmpMenu()
	{
	}
	 
	/** 
	 人员菜单功能
	 
	*/
	@Override
	public Map getEnMap()
	{
		
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("GPM_EmpMenu");
		map.setDepositaryOfEntity(Depositary.None);
		map.setDepositaryOfMap(Depositary.Application);
		map.setEnDesc("人员菜单功能");
		map.setEnType( EnType.View);

		map.AddMyPK();

		map.AddTBString(EmpMenuAttr.FK_Emp, null, "操作员", true, false, 0, 30, 20);
		map.AddTBString(EmpMenuAttr.FK_Menu, null, "菜单功能", true, false, 0, 30, 20);

		map.AddTBString(MenuAttr.Name, null, "菜单功能-名称", true, false, 0, 3900, 20);
		map.AddTBString(MenuAttr.ParentNo, null, "ParentNo", true, false, 1, 30, 20);
		map.AddTBString(AppAttr.Url, null, "连接", true, false, 0, 3900, 20, true);
		map.AddDDLSysEnum(MenuAttr.MenuType, 0, "菜单类型", true, true, MenuAttr.MenuType, "@3=目录@4=功能@5=功能控制点");
		map.AddTBString(MenuAttr.FK_App, null, "系统", true, false, 0, 30, 20);
		map.AddMyFile("图标");

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}
