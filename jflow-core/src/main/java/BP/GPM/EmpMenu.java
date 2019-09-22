package BP.GPM;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;
import java.util.*;

/** 
 人员菜单功能
*/
public class EmpMenu extends EntityMM
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	/** 
	 人员
	*/
	public final String getFK_Emp()
	{
		return this.GetValStringByKey(EmpMenuAttr.FK_Emp);
	}
	public final void setFK_Emp(String value)
	{
		this.SetValByKey(EmpMenuAttr.FK_Emp, value);
	}
	/** 
	 菜单
	*/
	public final String getFK_Menu()
	{
		return this.GetValStringByKey(EmpMenuAttr.FK_Menu);
	}
	public final void setFK_Menu(String value)
	{
		this.SetValByKey(EmpMenuAttr.FK_Menu, value);
	}
	/** 
	 系统
	*/
	public final String getFK_App()
	{
		return this.GetValStringByKey(MenuAttr.FK_App);
	}
	public final void setFK_App(String value)
	{
		this.SetValByKey(MenuAttr.FK_App, value);
	}
	/** 
	 是否选中
	*/
	public final boolean getIsChecked()
	{
		return this.GetValBooleanByKey(EmpMenuAttr.IsChecked);
	}
	public final void setIsChecked(boolean value)
	{
		this.SetValByKey(EmpMenuAttr.IsChecked, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 人员菜单功能
	*/
	public EmpMenu()
	{
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
		Map map = new Map("GPM_EmpMenu");
		map.DepositaryOfEntity = Depositary.None;
		map.DepositaryOfMap = Depositary.Application;
		map.EnDesc = "人员菜单对应";
		map.EnType = EnType.App;

			// map.AddTBStringPK(EmpMenuAttr.FK_Emp, null, "操作员", true, false, 0, 3900, 20);
		map.AddTBStringPK(EmpMenuAttr.FK_Menu, null, "菜单", false, false, 0, 50, 20);
		map.AddDDLEntitiesPK(EmpMenuAttr.FK_Emp, null, "菜单功能", new BP.Port.Emps(), true);

		map.AddBoolean(EmpMenuAttr.IsChecked, true, "是否选中", true, true);

		this._enMap = map;
		return this._enMap;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}