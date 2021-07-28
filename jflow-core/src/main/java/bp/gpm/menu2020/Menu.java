package bp.gpm.menu2020;

import bp.da.DBAccess;
import bp.da.DataType;
import bp.da.Depositary;
import bp.difference.SystemConfig;
import bp.en.EnType;
import bp.en.EntityNoName;
import bp.en.Map;
import bp.en.RefMethod;
import bp.en.UAC;
import bp.gpm.DeptAttr;
import bp.gpm.DeptMenuAttr;
import bp.gpm.DeptMenus;
import bp.gpm.EmpAttr;
import bp.gpm.EmpMenuAttr;
import bp.gpm.EmpMenus;
import bp.gpm.GroupMenuAttr;
import bp.gpm.GroupMenus;
import bp.gpm.Groups;
import bp.gpm.MenuCtrlWay;
import bp.gpm.MenuType;
import bp.gpm.StationMenuAttr;
import bp.gpm.StationMenus;
import bp.port.Depts;
import bp.port.Emps;
import bp.port.StationAttr;
import bp.sys.CCBPMRunModel;
import bp.web.WebUser;

/** 
菜单
*/
public class Menu extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 属性
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		if (WebUser.getIsAdmin() == true)
		{
			uac.IsDelete = true;
			uac.IsUpdate = true;
			uac.IsInsert = false;
			return uac;
		}
		else
		{
			uac.Readonly();
		}
		return uac;
	}
	/** 
	 系统编号
	 * @throws Exception 
	*/
	public final String getSystemNo() throws Exception
	{
		return this.GetValStringByKey(MenuAttr.SystemNo);
	}
	public final void setSystemNo(String value) throws Exception
	{
		this.SetValByKey(MenuAttr.SystemNo, value);
	}
	/** 
	 组织编号
	 * @throws Exception 
	*/
	public final String getOrgNo() throws Exception
	{
		return this.GetValStringByKey(MenuAttr.OrgNo);
	}
	public final void setOrgNo(String value) throws Exception
	{
		this.SetValByKey(MenuAttr.OrgNo, value);
	}
	/** 
	 标记
	 * @throws Exception 
	*/
	public final String getMark() throws Exception
	{
		return this.GetValStringByKey(MenuAttr.Mark);
	}
	public final void setMark(String value) throws Exception
	{
		this.SetValByKey(MenuAttr.Mark, value);
	}
	/** 
	 Tag 1
	 * @throws Exception 
	*/
	public final String getTag1() throws Exception
	{
		return this.GetValStringByKey(MenuAttr.Tag1);
	}
	public final void setTag1(String value) throws Exception
	{
		this.SetValByKey(MenuAttr.Tag1, value);
	}
	//public CtrlWay HisCtrlWay
	//{
	//    get
	//    {
	//        return (CtrlWay)this.GetValIntByKey(MenuAttr.CtrlWay);
	//    }
	//    set
	//    {
	//        this.SetValByKey(MenuAttr.CtrlWay, (int)value);
	//    }
	//}
	public final MenuType getMenuType()throws Exception {
		return MenuType.forValue(this.GetValIntByKey(MenuAttr.MenuType));
	}

	public final void setMenuType(MenuType value)throws Exception {
		this.SetValByKey(MenuAttr.MenuType, value.getValue());
	}
	public final MenuCtrlWay getMenuCtrlWay()throws Exception {
		return MenuCtrlWay.forValue(this.GetValIntByKey(MenuAttr.MenuCtrlWay));
	}

	public final void setMenuCtrlWay(MenuCtrlWay value) throws Exception{
		this.SetValByKey(MenuAttr.MenuCtrlWay, value.getValue());
	}
	/** 
	 是否启用
	 * @throws Exception 
	*/
	public final boolean getIsEnable() throws Exception
	{
		return this.GetValBooleanByKey(MenuAttr.IsEnable);
	}
	public final void setIsEnable(boolean value) throws Exception
	{
		this.SetValByKey(MenuAttr.IsEnable, value);
	}
	/** 
	 是否是ccSytem
	*/
	//public MenuType MenuType
	//{
	//    get
	//    {
	//        return (MenuType)this.GetValIntByKey(MenuAttr.MenuType);
	//    }
	//    set
	//    {
	//        this.SetValByKey(MenuAttr.MenuType, (int)value);
	//    }
	//}
	/** 
	 类别编号
	 * @throws Exception 
	*/
	public final String getModuleNo() throws Exception
	{
		return this.GetValStringByKey(MenuAttr.ModuleNo);
	}
	public final void setModuleNo(String value) throws Exception
	{
		this.SetValByKey(MenuAttr.ModuleNo, value);
	}
	/** 
	 模式
	 * @throws Exception 
	*/
	public final String getMenuModel() throws Exception
	{
		return this.GetValStringByKey(MenuAttr.MenuModel);
	}
	public final void setMenuModel(String value) throws Exception
	{
		this.SetValByKey(MenuAttr.MenuModel, value);
	}
	public final String getIcon() throws Exception
	{
		return this.GetValStringByKey(MenuAttr.Icon);
	}
	public final void setIcon(String value) throws Exception
	{
		this.SetValByKey(MenuAttr.Icon, value);
	}
	/** 
	 菜单工作类型 0=自定义菜单， 1=系统菜单，不可以删除.
	 * @throws Exception 
	*/
	public final int getWorkType() throws Exception
	{
		return this.GetValIntByKey(MenuAttr.WorkType);
	}
	public final void setWorkType(int value)throws Exception
	{
		this.SetValByKey(MenuAttr.WorkType, value);
	}
	public final String getUrlExt()throws Exception
	{
		return this.GetValStringByKey(MenuAttr.UrlExt);
	}
	public final void setUrlExt(String value)throws Exception
	{
		this.SetValByKey(MenuAttr.UrlExt, value);
	}
	public final String getMobileUrlExt()throws Exception
	{
		return this.GetValStringByKey(MenuAttr.MobileUrlExt);
	}
	public final void setMobileUrlExt(String value)throws Exception
	{
		this.SetValByKey(MenuAttr.MobileUrlExt, value);
	}
	public boolean IsCheck = false;
	/** 
	 标记
	*/
	public final int getIdx()throws Exception
	{
		return this.GetValIntByKey(MenuAttr.Idx);
	}
	public final void setIdx(int value)throws Exception
	{
		this.SetValByKey(MenuAttr.Idx, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
	/** 
	 业务处理.
	 
	 @return 
	 * @throws Exception 
	*/
	@Override
	protected boolean beforeInsert() throws Exception
	{
		if (DataType.IsNullOrEmpty(this.getNo()) == true)
		{
			this.setNo(DBAccess.GenerGUID());
		}
		this.setOrgNo(WebUser.getOrgNo());

		this.InitIcon();
		return super.beforeInsert();
	}
	/** 
	 初始化icon.
	 * @throws Exception 
	*/
	private void InitIcon() throws Exception
	{
		if (this.getMark().equals("StartFlow") == true)
		{
			this.setIcon("icon-paper-plane");
		}
		if (this.getMark().equals("Todolist") == true)
		{
			this.setIcon("icon-bell");
		}
		if (this.getMark().equals("Runing") == true)
		{
			this.setIcon("icon-clock");
		}
		if (this.getMark().equals("Group") == true)
		{
			this.setIcon("icon-chart");
		}
		if (this.getMark().equals("Search") == true)
		{
			this.setIcon("icon-grid");
		}

	}
	@Override
	protected boolean beforeDelete() throws Exception
	{
		if (this.getWorkType() == 1)
		{
			throw new RuntimeException("@删除失败,此项为系统菜单，不能删除只能隐藏。");
		}

		return super.beforeDelete();
	}
	/** 
	 EnMap
	 * @throws Exception 
*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("GPM_Menu"); // 类的基本属性.
		map.setDepositaryOfEntity(Depositary.None);
		map.setDepositaryOfMap(Depositary.Application);
		map.setEnDesc("菜单");
		map.setEnType(EnType.Sys);
		map.setCodeStruct("4");

		map.AddTBStringPK(MenuAttr.No, null, "编号", false, false, 1, 90, 50);
		map.AddTBString(MenuAttr.Name, null, "名称", true, false, 0, 300, 200, true);

		map.AddTBString(MenuAttr.MenuModel, null, "菜单模式", true, true, 0, 50, 50);
		map.AddTBString(MenuAttr.Mark, null, "标记", true, false, 0, 300, 200, false);
		map.AddTBString(MenuAttr.Tag1, null, "Tag1", true, false, 0, 300, 200, false);


		map.AddTBString(MenuAttr.FrmID, null, "FrmID", false, false, 0, 300, 200, false);
		map.AddTBString(MenuAttr.FlowNo, null, "FlowNo", false, false, 0, 300, 200, false);


			// @0=系统根目录@1=系统类别@2=系统.
		map.AddDDLSysEnum(MenuAttr.OpenWay, 1, "打开方式", true, true, MenuAttr.OpenWay, "@0=新窗口@1=本窗口@2=覆盖新窗口");

		map.AddTBString(MenuAttr.UrlExt, null, "PC端连接", true, false, 0, 500, 200, true);
		map.AddTBString(MenuAttr.MobileUrlExt, null, "移动端连接", true, false, 0, 500, 200, true);

		map.AddDDLSysEnum(MenuAttr.MenuCtrlWay, 0, "控制方式", true, true, MenuAttr.MenuCtrlWay, "@0=按照设置的控制@1=任何人都可以使用@2=Admin用户可以使用");
		map.AddBoolean(MenuAttr.IsEnable, true, "是否启用?", true, true);

		map.AddTBString(MenuAttr.Icon, null, "Icon", true, false, 0, 50, 50, true);


			//  map.AddTBString(MenuAttr.ModuleNo, null, "ModuleNo", false, false, 0, 50, 50);
		map.AddTBString(MenuAttr.SystemNo, null, "SystemNo", false, false, 0, 50, 50);

			//隶属模块，可以让用户编辑。
		map.AddDDLSQL(MenuAttr.ModuleNo, null, "隶属模块编号", "SELECT No,Name FROM GPM_Module WHERE SystemNo='@SystemNo'", true);

		map.AddDDLSysEnum(MenuAttr.ListModel, 0, "列表模式", true, true, MenuAttr.ListModel, "@0=编辑模式@1=视图模式");
		String msg = "提示";
		msg += "\t\n 1. 编辑模式就是可以批量的编辑方式打开数据, 可以批量的表格方式编辑数据.";
		msg += "\t\n 2. 视图模式就是查询的模式打开数据..";
		map.SetHelperAlert(MenuAttr.ListModel, msg);

		map.AddTBInt(MenuAttr.Idx, 0, "顺序号", true, false);

			// @0=自定义菜单. @1=系统菜单.  系统菜单不可以删除.
		map.AddTBInt(MenuAttr.WorkType, 0, "工作类型", false, false);


		if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			map.AddTBString(MenuAttr.OrgNo, null, "组织编号", true, false, 0, 50, 20);
		}

			//查询条件.
			//map.AddSearchAttr(MenuAttr.MenuType);
			//map.AddSearchAttr(MenuAttr.OpenWay);

			//map.AddDDLSysEnum(AppAttr.CtrlWay, 1, "控制方式", true, true, AppAttr.CtrlWay,
			//    "@0=游客@1=所有人员@2=按岗位@3=按部门@4=按人员@5=按SQL");
			// map.AddTBString(MenuAttr.CtrlObjs, null, "控制内容", false, false, 0, 4000, 20);
			//// 一对多的关系.
			//map.AttrsOfOneVSM.Add(new ByStations(), new Stations(), ByStationAttr.RefObj, ByStationAttr.FK_Station,
			//    StationAttr.Name, StationAttr.No, "可访问的岗位");
			//map.AttrsOfOneVSM.Add(new ByDepts(), new Depts(), ByStationAttr.RefObj, ByDeptAttr.FK_Dept,
			//    DeptAttr.Name, DeptAttr.No, "可访问的部门");
			//map.AttrsOfOneVSM.Add(new ByEmps(), new Emps(), ByStationAttr.RefObj, ByEmpAttr.FK_Emp,
			//    EmpAttr.Name, EmpAttr.No, "可访问的人员");

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本功能.
			//可以访问的权限组.
		map.getAttrsOfOneVSM().Add(new GroupMenus(), new Groups(), GroupMenuAttr.FK_Menu, GroupMenuAttr.FK_Group, EmpAttr.Name, EmpAttr.No, "绑定到权限组");

			//可以访问的权限组.
		map.getAttrsOfOneVSM().Add(new StationMenus(), new bp.port.Stations(), StationMenuAttr.FK_Menu, StationMenuAttr.FK_Station, EmpAttr.Name, EmpAttr.No, "绑定到岗位-列表模式");

			//可以访问的权限组.
		map.getAttrsOfOneVSM().AddGroupListModel(new StationMenus(), new bp.port.Stations(), StationMenuAttr.FK_Menu, StationMenuAttr.FK_Station, "绑定到岗位-分组模式", StationAttr.FK_StationType, "Name", EmpAttr.No);
			//可以访问的权限组.(岗位)
		map.getAttrsOfOneVSM().Add(new DeptMenus(), new Depts(), DeptMenuAttr.FK_Menu, DeptMenuAttr.FK_Dept, DeptAttr.Name, DeptAttr.No, "绑定到部门-列表模式");

		map.getAttrsOfOneVSM().AddBranches(new DeptMenus(), new Depts(), DeptMenuAttr.FK_Menu, DeptMenuAttr.FK_Dept, "部门(树)", EmpAttr.Name, EmpAttr.No);

			//节点绑定人员. 使用树杆与叶子的模式绑定.
		map.getAttrsOfOneVSM().AddBranchesAndLeaf(new EmpMenus(), new Emps(), EmpMenuAttr.FK_Menu, EmpMenuAttr.FK_Emp, "绑定人员-树结构", EmpAttr.FK_Dept, EmpAttr.Name, EmpAttr.No, "@WebUser.FK_Dept");

			//不带有参数的方法.
		RefMethod rm = new RefMethod();
		rm.Title = "增加(增删改查)功能权限";
		rm.Warning = "确定要增加吗？";
		rm.ClassMethodName = this.toString() + ".DoAddRight3";
		rm.IsForEns = true;
		rm.IsCanBatch = true; //是否可以批处理？
								  // map.AddRefMethod(rm);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 基本功能.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 创建菜单.
		rm = new RefMethod();
		rm.GroupName = "创建菜单";
		rm.Title = "创建单据";
		rm.Warning = "您确定要创建吗？";

		rm.getHisAttrs().AddTBString("No", null, "单据编号", true, false, 0, 100, 100);
		rm.getHisAttrs().AddTBString("Name", null, "单据名称", true, false, 0, 100, 400);
		rm.getHisAttrs().AddTBString("PTable", null, "存储表(为空则为编号相同)", true, false, 0, 100, 100);
		rm.getHisAttrs().AddDDLSysEnum("FrmType", 0, "单据模式", true, true, "BillFrmType", "@0=傻瓜表单@1=自由表单");
		rm.getHisAttrs().AddDDLSQL("Sys_FormTree", "", "选择表单树", "SELECT No,Name FROM Sys_FormTree WHERE ParentNo='1'");
		rm.ClassMethodName = this.toString() + ".DoAddCCBill";
			// map.AddRefMethod(rm);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 创建菜单.

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion
	 @Override
	 protected boolean beforeUpdateInsertAction() throws Exception
	 {
				if (DataType.IsNullOrEmpty(this.getModuleNo()) == true)
				{
					throw new RuntimeException("err@模块编号不能为空.");
				}

				//获取他的系统编号.
				Module md = new Module(this.getModuleNo());
				this.setSystemNo(md.getSystemNo());

				return super.beforeUpdateInsertAction();
	 }

	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 移动方法.
			/** 
			 向上移动
			 * @throws Exception 
			*/
			public final void DoUp() throws Exception
			{
				this.DoOrderUp(MenuAttr.ModuleNo, this.getModuleNo(), ModuleAttr.Idx);
			}
			/** 
			 向下移动
			 * @throws Exception 
			*/
			public final void DoDown() throws Exception
			{
				this.DoOrderDown(MenuAttr.ModuleNo, this.getModuleNo(), ModuleAttr.Idx);
			}
	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 移动方法.
}
