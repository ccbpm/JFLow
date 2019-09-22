package BP.GPM;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;
import java.util.*;

/** 
 人员菜单功能
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
	public final String getFK_Emp()
	{
		return this.GetValStringByKey(VGPMEmpMenuAttr.FK_Emp);
	}
	public final void setFK_Emp(String value)
	{
		this.SetValByKey(VGPMEmpMenuAttr.FK_Emp, value);
	}
	public final String getFK_Menu()
	{
		return this.GetValStringByKey(VGPMEmpMenuAttr.FK_Menu);
	}
	public final void setFK_Menu(String value)
	{
		this.SetValByKey(VGPMEmpMenuAttr.FK_Menu, value);
	}
	public final String getName()
	{
		return this.GetValStringByKey(MenuAttr.Name);
	}
	public final void setName(String value)
	{
		this.SetValByKey(MenuAttr.Name, value);
	}

	/** 
	 功能
	*/
	public final MenuType getHisMenuType()
	{
		return getMenuType().forValue(this.GetValIntByKey(MenuAttr.MenuType));
	}
	public final void setHisMenuType(MenuType value)
	{
		this.SetValByKey(MenuAttr.MenuType, value.getValue());
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
	public final String getImg()
	{
		String s = this.GetValStringByKey("WebPath");
		if (DataType.IsNullOrEmpty(s))
		{
			if (this.getHisMenuType() == GPM.MenuType.Dir)
			{
				return "../../Images/Btn/View.gif";
			}
			else
			{
				return "../../Images/Btn/Go.gif";
			}
		}
		else
		{
			return s;
		}
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
	 
	 @param mypk
	*/
	public VGPMEmpMenu(String no)
	{
		this.Retrieve();
	}
	/** 
	 人员菜单功能
	*/
	@Override
	public Map getEnMap()
	{
		if (this._enMap != null)
		{
			return this._enMap;
		}
		Map map = new Map("V_GPM_EmpMenu");
		map.DepositaryOfEntity = Depositary.None;
		map.DepositaryOfMap = Depositary.Application;
		map.EnDesc = "人员菜单对应";
		map.EnType = EnType.View;

		map.AddMyPK();

		map.AddTBString(VGPMEmpMenuAttr.FK_Emp, null, "操作员", true, false, 0, 30, 20);
		map.AddTBString(VGPMEmpMenuAttr.FK_Menu, null, "菜单功能", true, false, 0, 30, 20);

		map.AddTBString(MenuAttr.Name, null, "菜单功能-名称", true, false, 0, 3900, 20);
		map.AddTBString(MenuAttr.ParentNo, null, "ParentNo", true, false, 1, 30, 20);
		map.AddTBString(AppAttr.Url, null, "连接", true, false, 0, 3900, 20, true);
		map.AddDDLSysEnum(MenuAttr.MenuType, 0, "菜单类型", true, true, MenuAttr.MenuType, "@3=目录@4=功能@5=功能控制点");
		map.AddTBString(MenuAttr.FK_App, null, "系统", true, false, 0, 30, 20);
		map.AddMyFile("图标");

		this._enMap = map;
		return this._enMap;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}