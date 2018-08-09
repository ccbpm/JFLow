package BP.GPM;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;

/** 
 人员菜单功能
 
*/
public class EmpMenu extends EntityMM
{
		///#region 属性

	public final void setFK_Emp(String value)
	{
		this.SetValByKey(EmpMenuAttr.FK_Emp, value);
	}
	public final String getFK_Menu()
	{
		return this.GetValStringByKey(EmpMenuAttr.FK_Menu);
	}
	public final void setFK_Menu(String value)
	{
		this.SetValByKey(EmpMenuAttr.FK_Menu, value);
	}


	public final String getFK_App()
	{
		return this.GetValStringByKey(MenuAttr.FK_App);
	}
	public final void setFK_App(String value)
	{
		this.SetValByKey(MenuAttr.FK_App, value);
	}
	
	public final String getIsChecked(){
		return this.GetValStringByKey(EmpMenuAttr.IsChecked);
	}
	
	public final void setIsChecked(String value){
		this.SetValByKey(EmpMenuAttr.IsChecked, value);
	}
		///#region 构造方法
	/** 
	 人员菜单功能
	 
	*/
	public EmpMenu()
	{
	}
	/** 
	 人员菜单功能
	 
	 @param mypk
	 * @throws Exception 
	*/
	public EmpMenu(String no) throws Exception
	{
		this.Retrieve();
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
		map.setEnType( EnType.App);


		map.AddTBStringPK(EmpMenuAttr.FK_Emp, null, "操作员", true, false, 0, 30, 20);
		map.AddTBStringPK(EmpMenuAttr.FK_Menu, null, "菜单功能", true, false, 0, 50, 20);
		map.AddBoolean(EmpMenuAttr.IsChecked, true, "是否选中", true, true);
		

		this.set_enMap(map);
		return this.get_enMap();
	}
		///#endregion
}