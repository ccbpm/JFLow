package BP.GPM;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;
import BP.En.Map;

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
	 * @throws Exception 
	*/
	public final String getFK_Emp() throws Exception
	{
		return this.GetValStringByKey(EmpMenuAttr.FK_Emp);
	}
	public final void setFK_Emp(String value) throws Exception
	{
		this.SetValByKey(EmpMenuAttr.FK_Emp, value);
	}
	/** 
	 菜单
	 * @throws Exception 
	*/
	public final String getFK_Menu() throws Exception
	{
		return this.GetValStringByKey(EmpMenuAttr.FK_Menu);
	}
	public final void setFK_Menu(String value) throws Exception
	{
		this.SetValByKey(EmpMenuAttr.FK_Menu, value);
	}
	/** 
	 系统
	 * @throws Exception 
	*/
	public final String getFK_App() throws Exception
	{
		return this.GetValStringByKey(MenuAttr.FK_App);
	}
	public final void setFK_App(String value) throws Exception
	{
		this.SetValByKey(MenuAttr.FK_App, value);
	}
	/** 
	 是否选中
	 * @throws Exception 
	*/
	public final boolean getIsChecked() throws Exception
	{
		return this.GetValBooleanByKey(EmpMenuAttr.IsChecked);
	}
	public final void setIsChecked(boolean value) throws Exception
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
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("GPM_EmpMenu");
		map.setDepositaryOfEntity(Depositary.None);
		map.setDepositaryOfMap(Depositary.Application);
		map.setEnDesc("人员菜单对应");
		map.setEnType(EnType.App);

			// map.AddTBStringPK(EmpMenuAttr.FK_Emp, null, "操作员", true, false, 0, 3900, 20);
		map.AddTBStringPK(EmpMenuAttr.FK_Menu, null, "菜单", false, false, 0, 50, 20);
		map.AddDDLEntitiesPK(EmpMenuAttr.FK_Emp, null, "菜单功能", new BP.Port.Emps(), true);

		map.AddBoolean(EmpMenuAttr.IsChecked, true, "是否选中", true, true);

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}