package BP.GPM;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;
import BP.En.Map;

import java.util.*;

/** 
 人员菜单功能
*/
public class VGPMEmpMenu extends EntityMyPK
{

		///#region 属性
	public final String getCtrlObjs() throws Exception
	{
		return this.GetValStringByKey(MenuAttr.CtrlObjs);
	}
	public final void setCtrlObjs(String value) throws Exception
	{
		this.SetValByKey(MenuAttr.CtrlObjs, value);
	}
	public final String getFK_Emp() throws Exception
	{
		return this.GetValStringByKey(VGPMEmpMenuAttr.FK_Emp);
	}
	public final void setFK_Emp(String value) throws Exception
	{
		this.SetValByKey(VGPMEmpMenuAttr.FK_Emp, value);
	}
	public final String getFK_Menu() throws Exception
	{
		return this.GetValStringByKey(VGPMEmpMenuAttr.FK_Menu);
	}
	public final void setFK_Menu(String value) throws Exception
	{
		this.SetValByKey(VGPMEmpMenuAttr.FK_Menu, value);
	}
	public final String getName() throws Exception
	{
		return this.GetValStringByKey(MenuAttr.Name);
	}
	public final void setName(String value) throws Exception
	{
		this.SetValByKey(MenuAttr.Name, value);
	}

	/** 
	 功能
	 * @throws Exception 
	*/
	public final MenuType getHisMenuType() throws Exception
	{
		return MenuType.forValue(this.GetValIntByKey(MenuAttr.MenuType));
	}
	public final void setHisMenuType(MenuType value) throws Exception
	{
		this.SetValByKey(MenuAttr.MenuType, value.getValue());
	}
	/** 
	 是否是ccSytem
	 * @throws Exception 
	*/
	public final int getMenuType() throws Exception
	{
		return this.GetValIntByKey(MenuAttr.MenuType);
	}
	public final void setMenuType(int value) throws Exception
	{
		this.SetValByKey(MenuAttr.MenuType, value);
	}
	public final int getIdx() throws Exception
	{
		return this.GetValIntByKey(MenuAttr.Idx);
	}
	public final void setIdx(int value) throws Exception
	{
		this.SetValByKey(MenuAttr.Idx, value);
	}

	public final String getFK_App() throws Exception
	{
		return this.GetValStringByKey(MenuAttr.FK_App);
	}
	public final void setFK_App(String value) throws Exception
	{
		this.SetValByKey(MenuAttr.FK_App, value);
	}
	public final String getImg() throws Exception
	{
		String s = this.GetValStringByKey("WebPath");
		if (DataType.IsNullOrEmpty(s))
		{
			if (this.getHisMenuType() == MenuType.Dir)
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
	public final String getUrl() throws Exception
	{
		return this.GetValStringByKey(MenuAttr.Url);
	}
	public final void setUrl(String value) throws Exception
	{
		this.SetValByKey(MenuAttr.Url, value);
	}

		///#endregion


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
	 * @throws Exception 
	*/
	public VGPMEmpMenu(String no) throws Exception
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
		Map map = new Map("V_GPM_EmpMenu");
		map.setDepositaryOfEntity(Depositary.None);
		map.setDepositaryOfMap(Depositary.Application);
		map.setEnDesc("人员菜单对应");
		map.setEnType(EnType.View);

		map.AddMyPK();

		map.AddTBString(VGPMEmpMenuAttr.FK_Emp, null, "操作员", true, false, 0, 30, 20);
		map.AddTBString(VGPMEmpMenuAttr.FK_Menu, null, "菜单功能", true, false, 0, 30, 20);

		map.AddTBString(MenuAttr.Name, null, "菜单功能-名称", true, false, 0, 3900, 20);
		map.AddTBString(MenuAttr.ParentNo, null, "ParentNo", true, false, 1, 30, 20);
		map.AddTBString(AppAttr.Url, null, "连接", true, false, 0, 3900, 20, true);
		map.AddDDLSysEnum(MenuAttr.MenuType, 0, "菜单类型", true, true, MenuAttr.MenuType, "@3=目录@4=功能@5=功能控制点");
		map.AddTBString(MenuAttr.FK_App, null, "系统", true, false, 0, 30, 20);
		map.AddMyFile("图标");

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}