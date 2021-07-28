package bp.gpm;

import bp.da.Depositary;
import bp.en.EnType;
import bp.en.EntityMM;
import bp.en.Map;
import bp.port.Stations;

/** 
部门菜单
*/
public class DeptMenu extends EntityMM
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 属性
	/** 
	 菜单
	 * @throws Exception 
	*/
	public final String getFKMenu() throws Exception
	{
		return this.GetValStringByKey(DeptMenuAttr.FK_Menu);
	}
	public final void setFKMenu(String value) throws Exception
	{
		this.SetValByKey(DeptMenuAttr.FK_Menu, value);
	}
	/** 
	 部门
	 * @throws Exception 
	*/
	public final String getFKDept() throws Exception
	{
		return this.GetValStringByKey(DeptMenuAttr.FK_Dept);
	}
	public final void setFKDept(String value) throws Exception
	{
		this.SetValByKey(DeptMenuAttr.FK_Dept, value);
	}
	/** 
	 是否选中
	 * @throws Exception 
	*/
	public final String getIsChecked() throws Exception
	{
		return this.GetValStringByKey(DeptMenuAttr.IsChecked);
	}
	public final void setIsChecked(String value) throws Exception
	{
		this.SetValByKey(DeptMenuAttr.IsChecked, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 构造方法
	/** 
	 部门菜单
	*/
	public DeptMenu()
	{
	}
	/** 
	 部门菜单
	 
	 @param mypk
	 * @throws Exception 
	*/
	public DeptMenu(String no) throws Exception
	{
		this.Retrieve();
	}
	/** 
	 部门菜单
	 * @throws Exception 
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("GPM_DeptMenu");
		map.setDepositaryOfEntity(Depositary.None);
		map.setDepositaryOfMap(Depositary.Application);
		map.setEnDesc("部门菜单");
		map.setEnType(EnType.Sys);

			//map.AddTBStringPK(DeptMenuAttr.FK_Station, null, "部门", false, false, 0, 50, 20);
		map.AddDDLEntitiesPK(DeptMenuAttr.FK_Dept, null, " 部门", new Stations(), true);
		map.AddTBStringPK(DeptMenuAttr.FK_Menu, null, "菜单", false, false, 0, 50, 20);
		map.AddBoolean(DeptMenuAttr.IsChecked, true, "是否选中", true, true);
		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion
}
