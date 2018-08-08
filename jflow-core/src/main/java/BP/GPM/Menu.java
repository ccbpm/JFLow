package BP.GPM;

import BP.DA.*;
import BP.En.*;
import BP.Web.WebUser;

/** 
 菜单
 
*/
public class Menu extends EntityTree
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		if (WebUser.getNo().equals("admin"))
		{
			uac.IsDelete = true;
			uac.IsUpdate = true;
			uac.IsInsert = true;
			return uac;
		}
		else
		{
			uac.Readonly();
		}
		return uac;
	}
	public final String getCtrlObjs()
	{
		return this.GetValStringByKey(MenuAttr.CtrlObjs);
	}
	public final void setCtrlObjs(String value)
	{
		this.SetValByKey(MenuAttr.CtrlObjs, value);
	}

	/** 
	 功能
	 
	*/
	public final MenuType getHisMenuType()
	{
		return MenuType.forValue(this.GetValIntByKey(MenuAttr.MenuType));
	}
	public final void setHisMenuType(MenuType value)
	{
		this.SetValByKey(MenuAttr.MenuType, value.getValue());
	}
	/** 
	 是否启用
	 
	*/
	public final boolean getIsEnable()
	{
		return this.GetValBooleanByKey(MenuAttr.IsEnable);
	}
	public final void setIsEnable(boolean value)
	{
		this.SetValByKey(MenuAttr.IsEnable, value);
	}
	/** 
	 打开方式
	 
	*/
	public final String getOpenWay()
	{
		int openWay = 0;

		switch (openWay)
		{
			case 0:
				return "_blank";
			case 1:
				return this.getNo();
			default:
				return "";
		}
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
	public final String getFK_App()
	{
		return this.GetValStringByKey(MenuAttr.FK_App);
	}
	public final void setFK_App(String value)
	{
		this.SetValByKey(MenuAttr.FK_App, value);
	}

	public final String getFlag()
	{
		return this.GetValStringByKey(MenuAttr.Flag);
	}
	public final void setFlag(String value)
	{
		this.SetValByKey(MenuAttr.Flag, value);
	}

	public final String getImg()
	{
		String s = this.GetValStringByKey("WebPath");
		if (DataType.IsNullOrEmpty(s))
		{
			if (this.getHisMenuType() == MenuType.Dir)
			{
				return "/Images/Btn/View.gif";
			}
			else
			{
				return "/Images/Btn/Go.gif";
			}
		}
		else
		{
			return s;
		}
	}
	public final void setImg(String value)
	{
		this.SetValByKey("WebPath", value);
	}
	public final String getUrl()
	{
		return this.GetValStringByKey(MenuAttr.Url);
	}
	public final void setUrl(String value)
	{
		this.SetValByKey(MenuAttr.Url, value);
	}
	public boolean IsCheck = false;

		///#region 构造方法
	/** 
	 菜单
	 
	*/
	public Menu()
	{
	}
	/** 
	 菜单
	 
	 @param mypk
	 * @throws Exception 
	*/
	public Menu(String no) throws Exception
	{
		this.setNo(no);
		this.Retrieve();
	}
	@Override
	protected boolean beforeInsert() throws Exception
	{
		return super.beforeInsert();
	}
	@Override
	protected boolean beforeDelete() throws Exception
	{
		if (this.getFlag().contains("FlowSort") || this.getFlag().contains("Flow"))
		{
			throw new RuntimeException("@删除失败,此项为工作流菜单，不能删除。");
		}

		return super.beforeDelete();
	}
	@Override
	protected void afterDelete() throws Exception
	{
		//删除他的子项目.
		Menus ens = new Menus();
		ens.Retrieve(MenuAttr.ParentNo, this.getNo());
		for (Menu item : ens.ToJavaList())
		{
			item.Delete();
		}

		super.afterDelete();
	}
	/** 
	 EnMap
	 
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("GPM_Menu"); // 类的基本属性.
		map.setDepositaryOfEntity(Depositary.None);
		map.setDepositaryOfMap(Depositary.Application);
		map.setEnDesc("系统");
		map.setEnType(EnType.Sys);
		map.setCodeStruct("4");
		///#region 与树有关的必备属性.
		map.AddTBStringPK(MenuAttr.No, null, "功能编号", true, true, 4, 4, 4);
		map.AddTBString(MenuAttr.Name, null, "名称", true, false, 0, 300, 400);

		map.AddDDLEntities(MenuAttr.ParentNo, null, DataType.AppString, "父节点", new Menus(), "No", "Name", true);
			//map.AddTBString(MenuAttr.ParentNo, null, "父节点编号", true, true, 0, 10, 10);
		map.AddTBInt(MenuAttr.Idx, 0, "顺序号", true, false);
			///#endregion 与树有关的必备属性.

			// 类的字段属性.
		map.AddDDLSysEnum(MenuAttr.MenuType, 0, "菜单类型", true, true, MenuAttr.MenuType, "@3=目录@4=功能@5=功能控制点");

			// @0=系统根目录@1=系统类别@2=系统.
		map.AddDDLEntities(MenuAttr.FK_App, null, "系统", new Apps(), true);
		   // map.AddTBString(MenuAttr.FK_App, null, "系统", true, false, 0, 3900, 20, true);
		map.AddTBString(MenuAttr.Url, null, "连接", true, false, 0, 3900, 20, true);
		map.AddBoolean(MenuAttr.IsEnable, true, "是否启用?",true,true);
		map.AddDDLSysEnum(MenuAttr.OpenWay, 0, "打开方式", true, true, MenuAttr.OpenWay, "@0=新窗口@1=本窗口@2=覆盖新窗口");
		map.AddTBString(MenuAttr.Flag, null, "标记", true, false, 0, 500, 20, true);

		map.AddTBString(MenuAttr.Tag1, null, "Tag1", true, false, 0, 500, 20, true);
		map.AddTBString(MenuAttr.Tag2, null, "Tag2", true, false, 0, 500, 20, true);
		map.AddTBString(MenuAttr.Tag3, null, "Tag3", true, false, 0, 500, 20, true);
		map.AddTBString(EntityNoMyFileAttr.WebPath, "/WF/Img/FileType/IE.gif", "图标", true, false, 0, 200, 20, true);
		map.AddMyFile("图标"); //附件.

		map.AddSearchAttr(MenuAttr.FK_App);
		map.AddSearchAttr(MenuAttr.MenuType);
		map.AddSearchAttr(MenuAttr.OpenWay);


			//可以访问的权限组.
		map.getAttrsOfOneVSM().Add(new GroupMenus(), new Groups(), GroupMenuAttr.FK_Menu, GroupMenuAttr.FK_Group, EmpAttr.Name, EmpAttr.No, "权限组");


			//节点绑定人员. 使用树杆与叶子的模式绑定.
		map.getAttrsOfOneVSM().AddBranchesAndLeaf(new EmpMenus(), new BP.Port.Emps(), EmpMenuAttr.FK_Menu, EmpMenuAttr.FK_Emp, "绑定人员", EmpAttr.FK_Dept, EmpAttr.Name, EmpAttr.No, "@WebUser.FK_Dept");


		this.set_enMap(map);
		return this.get_enMap();
	}


	public final String getWebPath()
	{
		return this.GetValStrByKey(EntityNoMyFileAttr.WebPath);
	}
	public final void setWebPath(String value)
	{
		this.SetValByKey(EntityNoMyFileAttr.WebPath, value);
	}
	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		this.setWebPath(this.getWebPath().replace("//", "/"));
		return super.beforeUpdateInsertAction();
	}
}