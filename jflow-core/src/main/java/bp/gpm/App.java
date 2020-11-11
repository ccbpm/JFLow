package bp.gpm;

import bp.web.WebUser;
import bp.da.*;
import bp.en.*;
import bp.en.Map;
/** 
 系统
*/
public class App extends EntityNoName
{

	private static final long serialVersionUID = 1L;
		///属性
	/** 
	 打开方式
	 * @throws Exception 
	*/
	public final String getOpenWay() throws Exception
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
	 路径
	*/
	public final String getWebPath()throws Exception
	{
		return this.GetValStringByKey("WebPath");
	}
	/** 
	 ICON
	*/
	public final String getICON()throws Exception
	{
		return this.getWebPath();
	}
	public final void setICON(String value) throws Exception
	{
		this.SetValByKey("ICON", value);
	}
	/** 
	 连接
	 * @throws Exception 
	*/
	public final String getUrl() throws Exception
	{
		String url = this.GetValStrByKey(AppAttr.UrlExt);
		if (DataType.IsNullOrEmpty(url))
		{
			return "";
		}

		if (this.getSSOType().equals("0")) //SID验证
		{
			String SID = DBAccess.RunSQLReturnStringIsNull("SELECT SID FROM Port_Emp WHERE No='" + WebUser.getNo() + "'", null);
			if (url.contains("?"))
			{
				url += "&UserNo=" + WebUser.getNo() + "&SID=" + SID;
			}
			else
			{
				url += "?UserNo=" + WebUser.getNo() + "&SID=" + SID;
			}
		}
		return url;
	}
	public final void setUrl(String value) throws Exception
	{
		this.SetValByKey(AppAttr.UrlExt, value);
	}
	/** 
	 跳转连接
	*/
	public final String getSubUrl()throws Exception
	{
		return this.GetValStrByKey(AppAttr.SubUrl);
	}
	public final void setSubUrl(String value) throws Exception
	{
		this.SetValByKey(AppAttr.UrlExt, value);
	}
	/** 
	 是否启用
	*/
	public final boolean getIsEnable()throws Exception
	{
		return this.GetValBooleanByKey(AppAttr.IsEnable);
	}
	public final void setIsEnable(boolean value) throws Exception
	{
		this.SetValByKey(AppAttr.IsEnable, value);
	}
	/** 
	 顺序
	*/
	public final int getIdx()throws Exception
	{
		return this.GetValIntByKey(AppAttr.Idx);
	}
	public final void setIdx(int value) throws Exception
	{
		this.SetValByKey(AppAttr.Idx, value);
	}
	/** 
	 用户控件ID
	*/
	public final String getUidControl()throws Exception
	{
		return this.GetValStrByKey(AppAttr.UidControl);
	}
	public final void setUidControl(String value) throws Exception
	{
		this.SetValByKey(AppAttr.UidControl, value);
	}
	/** 
	 密码控件ID
	*/
	public final String getPwdControl()throws Exception
	{
		return this.GetValStrByKey(AppAttr.PwdControl);
	}
	public final void setPwdControl(String value) throws Exception
	{
		this.SetValByKey(AppAttr.PwdControl, value);
	}
	/** 
	 提交方式
	*/
	public final String getActionType()throws Exception
	{
		return this.GetValStrByKey(AppAttr.ActionType);
	}
	public final void setActionType(String value) throws Exception
	{
		this.SetValByKey(AppAttr.ActionType, value);
	}
	/** 
	 登录方式@0=SID验证@1=连接@2=表单提交
	*/
	public final String getSSOType()throws Exception
	{
		return this.GetValStrByKey(AppAttr.SSOType);
	}
	public final void setSSOType(String value) throws Exception
	{
		this.SetValByKey(AppAttr.SSOType, value);
	}
	public final String getFK_AppSort()throws Exception
	{
		return this.GetValStringByKey(AppAttr.FK_AppSort);
	}
	public final void setFK_AppSort(String value) throws Exception
	{
		this.SetValByKey(AppAttr.FK_AppSort, value);
	}
	public final String getRefMenuNo()throws Exception
	{
		return this.GetValStringByKey(AppAttr.RefMenuNo);
	}
	public final void setRefMenuNo(String value) throws Exception
	{
		this.SetValByKey(AppAttr.RefMenuNo, value);
	}

		///


		///按钮权限控制
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForAppAdmin();
		return uac;
	}

		///


		///构造方法
	/** 
	 系统
	*/
	public App()
	{
	}
	/** 
	 系统
	 
	 @param no
	 * @throws Exception 
	*/
	public App(String no) throws Exception
	{
		this.setNo( no);
		this.Retrieve();
	}
	/** 
	 EnMap
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("GPM_App", "系统");

		map.setDepositaryOfEntity(Depositary.None);
		map.setDepositaryOfMap(Depositary.Application);

		map.AddTBStringPK(AppAttr.No, null, "编号", true, false, 2, 30, 100);
		map.AddDDLSysEnum(AppAttr.AppModel, 0, "应用类型", true, true, AppAttr.AppModel, "@0=BS系统@1=CS系统");
		map.AddTBString(AppAttr.Name, null, "名称", true, false, 0, 3900, 150, true);
		map.AddDDLEntities(AppAttr.FK_AppSort, null, "类别", new AppSorts(), false);
		map.AddBoolean(AppAttr.IsEnable, true, "启用?", true, true);

		map.AddTBString(AppAttr.UrlExt, null, "默认连接", true, false, 0, 3900, 100, true);
		map.AddTBString(AppAttr.SubUrl, null, "第二连接", true, false, 0, 3900, 100, true);

		map.AddTBString(AppAttr.UidControl, null, "用户名控件", true, false, 0, 100, 100);
		map.AddTBString(AppAttr.PwdControl, null, "密码控件", true, false, 0, 100, 100);
		map.AddDDLSysEnum(AppAttr.ActionType, 0, "提交类型", true, true, AppAttr.ActionType, "@0=GET@1=POST");
		map.AddDDLSysEnum(AppAttr.SSOType, 0, "登录方式", true, true, AppAttr.SSOType, "@0=SID验证@1=连接@2=表单提交@3=不传值");
		map.AddDDLSysEnum(AppAttr.OpenWay, 0, "打开方式", true, true, AppAttr.OpenWay, "@0=新窗口@1=本窗口@2=覆盖新窗口");

		map.AddTBString(AppAttr.RefMenuNo, null, "关联菜单编号", true, false, 0, 300, 100);
		map.AddTBString(AppAttr.AppRemark, null, "备注", true, false, 0, 500, 100, true);
		map.AddTBInt(AppAttr.Idx, 0, "显示顺序", true, false);
		map.AddMyFile("ICON");

			//增加查询条件.
		map.AddSearchAttr(AppAttr.FK_AppSort);

		RefMethod rm = new RefMethod();
		rm.Title = "编辑菜单";
		rm.ClassMethodName = this.toString() + ".DoMenu";
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "查看可访问该系统的人员";
		rm.ClassMethodName = this.toString() + ".DoWhoCanUseApp";

			//map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "刷新设置";
		rm.ClassMethodName = this.toString() + ".DoRef";
			//map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "第二连接";
			//rm.Title = "第二连接：登录方式为不传值、连接不设置用户名密码转为第二连接。";
		rm.ClassMethodName = this.toString() + ".About";
			// map.AddRefMethod(rm);
		this.set_enMap(map);
		return this.get_enMap();
	}

		///

	@Override
	protected boolean beforeDelete() throws Exception
	{
		Menu appMenu = new Menu(this.getRefMenuNo());
		if (appMenu != null && appMenu.getFlag().contains("Flow"))
		{
			throw new RuntimeException("@删除失败,此项为工作流菜单，不能删除。");
		}

		// 删除该系统.
		Menu menu = new Menu();
		menu.Delete(MenuAttr.FK_App, this.getNo());

		// 删除用户数据.
		EmpMenu em = new EmpMenu();
		em.Delete(MenuAttr.FK_App, this.getNo());

		//EmpApp ea = new EmpApp();
		//ea.Delete(MenuAttr.FK_App, this.getNo());

		return super.beforeDelete();
	}

	@Override
	protected boolean beforeUpdate() throws Exception
	{
		//检查表.
		CheckPTable();

		if (DataType.IsNullOrEmpty(this.getRefMenuNo()) == false)
		{
			//系统类别
			AppSort appSort = new AppSort(this.getFK_AppSort());

			Menu menu = new Menu(this.getRefMenuNo());
			menu.setName( this.getName());
			menu.setParentNo( appSort.getRefMenuNo());
			menu.Update();
		}

		return super.beforeUpdate();
	}

	public final void CheckPTable() throws Exception
	{
		AppSort sort = new AppSort();
		sort.CheckPhysicsTable();
		App app = new App();
		app.CheckPhysicsTable();
		Menu en = new Menu();
		en.CheckPhysicsTable();
	}

	@Override
	protected boolean beforeInsert() throws Exception
	{
		CheckPTable();

		if (DataType.IsNullOrEmpty(this.getName()) == true)
		{
			throw new RuntimeException("err@系统名称不能为空.");
		}

		AppSort sort = new AppSort();
		sort.setNo(this.getFK_AppSort());
		sort.Retrieve();

		//求系统类别的菜单.
		Menu menu = new Menu(sort.getRefMenuNo());

		// 创建子菜单. 系统的根目录. 
		Object tempVar = menu.DoCreateSubNode();
		Menu appMenu = tempVar instanceof Menu ? (Menu)tempVar : null;
		appMenu.setFK_App(this.getNo());

		appMenu.setName( this.getName());
		appMenu.setHisMenuType(MenuType.App);

		appMenu.Update();

		//设置相关的菜单编号.
		this.setRefMenuNo(appMenu.getNo());


			///为该系统创建几个空白菜单
		Object tempVar2 = appMenu.DoCreateSubNode();
		Menu dir = tempVar2 instanceof Menu ? (Menu)tempVar2 : null;
		dir.setFK_App(this.getNo());
		dir.setName("流程管理");
		dir.setMenuType(MenuType.Dir);
		dir.setMenuCtrlWay(MenuCtrlWay.Anyone);
		dir.Update();

		Object tempVar3 = dir.DoCreateSubNode();
		menu = tempVar3 instanceof Menu ? (Menu)tempVar3 : null;
		menu.setName("发起");
		menu.setFK_App(this.getNo());
		menu.setMenuType(MenuType.Menu);
		menu.setUrlExt("/WF/Start.htm");
		menu.setParentNo( dir.getNo());
		menu.setMenuCtrlWay(MenuCtrlWay.Anyone);
		menu.Update();

		Object tempVar4 = dir.DoCreateSubNode();
		menu = tempVar4 instanceof Menu ? (Menu)tempVar4 : null;
		menu.setName("待办");
		menu.setFK_App(this.getNo());
		menu.setMenuType(MenuType.Menu);
		menu.setUrlExt("/WF/Todolist.htm");
		menu.setParentNo( dir.getNo());
		menu.setMenuCtrlWay(MenuCtrlWay.Anyone);
		menu.Update();

		Object tempVar5 = dir.DoCreateSubNode();
		menu = tempVar5 instanceof Menu ? (Menu)tempVar5 : null;
		menu.setName("在途");
		menu.setFK_App(this.getNo());
		menu.setMenuType(MenuType.Menu);
		menu.setUrlExt("/WF/Runing.htm");
		menu.setMenuCtrlWay(MenuCtrlWay.Anyone);
		menu.setParentNo( dir.getNo());

		menu.Update();

			///


		Object tempVar6 = appMenu.DoCreateSubNode();
		Menu dir2 = tempVar6 instanceof Menu ? (Menu)tempVar6 : null;
		dir2.setFK_App(this.getNo());
		dir2.setName("系统管理");
		dir2.setMenuCtrlWay(MenuCtrlWay.AdminOnly);
		dir2.setMenuType(MenuType.Dir);
		dir2.Update();

		Object tempVar7 = dir2.DoCreateSubNode();
		menu = tempVar7 instanceof Menu ? (Menu)tempVar7 : null;
		menu.setName("部门");
		menu.setFK_App(this.getNo());
		menu.setMenuType(MenuType.Menu);
		menu.setUrlExt("/WF/Comm/Tree.htm?EnsName=bp.gpm.Depts");
		menu.setParentNo( dir2.getNo());
		menu.setMenuCtrlWay(MenuCtrlWay.AdminOnly);
		menu.Update();

		Object tempVar8 = dir2.DoCreateSubNode();
		menu = tempVar8 instanceof Menu ? (Menu)tempVar8 : null;
		menu.setName("人员");
		menu.setFK_App(this.getNo());
		menu.setMenuType(MenuType.Menu);
		menu.setUrlExt("/WF/Comm/Search.htm?EnsName=bp.gpm.Emps");
		menu.setParentNo( dir2.getNo());
		menu.setMenuCtrlWay(MenuCtrlWay.AdminOnly);
		menu.Update();

		Object tempVar9 = dir2.DoCreateSubNode();
		menu = tempVar9 instanceof Menu ? (Menu)tempVar9 : null;
		menu.setName("岗位");
		menu.setFK_App(this.getNo());
		menu.setMenuType(MenuType.Menu);
		menu.setUrlExt("/WF/Comm/Search.htm?EnsName=bp.port.Stations");
		menu.setParentNo( dir2.getNo());
		menu.setMenuCtrlWay(MenuCtrlWay.AdminOnly);
		menu.Update();

		return super.beforeInsert();
	}

	/** 
	 为BPM初始化菜单.
	 * @throws Exception 
	*/
	public static void InitBPMMenu() throws Exception
	{
		AppSort sort = new AppSort();
		sort.setNo("01");
		if (sort.RetrieveFromDBSources() == 0)
		{
			sort.setName("应用系统");
			sort.setRefMenuNo("2000");
			sort.Insert();
		}

		App app = new App();
		app.setNo("CCFlowBPM");
		app.setName("BPM系统");
		app.setFK_AppSort("01");
		app.Insert();
	}
	/** 
	 信息介绍
	 
	 @return 
	*/
	public final String About()
	{
		return null;
	}
	/** 
	 打开菜单
	 
	 @return 
	*/
	public final String DoMenu()throws Exception
	{
		return "../../../WF/Comm/Tree.htm?EnsName=bp.gpm.Menus&ParentNo=" + this.getRefMenuNo();
	}
	/** 
	 刷新数据.
	 * @throws Exception 
	*/
	public final void RefData() throws Exception
	{
		//删除数据.
		EmpMenus mymes = new EmpMenus();
		mymes.Delete(EmpMenuAttr.FK_App, this.getNo());

		//删除系统.
		EmpApps empApps = new EmpApps();
		empApps.Delete(EmpMenuAttr.FK_App, this.getNo());

		//查询出来菜单.
		Menus menus = new Menus();
		menus.Retrieve(EmpMenuAttr.FK_App, this.getNo());

		//查询出来人员.
		Emps emps = new Emps();
		emps.RetrieveAllFromDBSource();

		for (Emp emp : emps.ToJavaList())
		{

				///初始化系统访问权限.
			EmpApp me = new EmpApp();
			me.Copy(this);
			me.setFK_Emp(emp.getNo());
			me.setFK_App(this.getNo());
			me.setMyPK(this.getNo() + "_" + me.getFK_Emp());
			me.Insert();

				/// 初始化系统访问权限.
		}
	}
}