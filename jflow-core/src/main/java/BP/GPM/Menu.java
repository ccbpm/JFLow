package BP.GPM;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 菜单
*/
public class Menu extends EntityTree
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		if (BP.Web.WebUser.No.equals("admin"))
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
				return this.No;
			default:
				return "";
		}
	}
	/** 
	 是否是ccSytem
	*/
	public final MenuType getMenuType()
	{
		return MenuType.forValue(this.GetValIntByKey(MenuAttr.MenuType));
	}
	public final void setMenuType(MenuType value)
	{
		this.SetValByKey(MenuAttr.MenuType, value.getValue());
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
			if (this.getHisMenuType() == GPM.MenuType.Dir)
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
	public final String getUrlExt()
	{
		return this.GetValStringByKey(MenuAttr.UrlExt);
	}
	public final void setUrlExt(String value)
	{
		this.SetValByKey(MenuAttr.UrlExt, value);
	}
	public boolean IsCheck = false;
	/** 
	 标记
	*/
	public final String getTag1()
	{
		return this.GetValStringByKey(MenuAttr.Tag1);
	}
	public final void setTag1(String value)
	{
		this.SetValByKey(MenuAttr.Tag1, value);
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
	*/
	public Menu(String no)
	{
		this.No = no;
		this.Retrieve();
	}

	@Override
	protected boolean beforeDelete()
	{
		if (this.getFlag().contains("FlowSort") || this.getFlag().contains("Flow"))
		{
			throw new RuntimeException("@删除失败,此项为工作流菜单，不能删除。");
		}

		return super.beforeDelete();
	}
	@Override
	protected void afterDelete()
	{
		//删除他的子项目.
		Menus ens = new Menus();
		ens.Retrieve(MenuAttr.ParentNo, this.No);
		for (Menu item : ens)
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
		if (this._enMap != null)
		{
			return this._enMap;
		}
		Map map = new Map("GPM_Menu"); // 类的基本属性.
		map.DepositaryOfEntity = Depositary.None;
		map.DepositaryOfMap = Depositary.Application;
		map.EnDesc = "系统菜单";
		map.EnType = EnType.Sys;
		map.CodeStruct = "4";

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 与树有关的必备属性.
		map.AddTBStringPK(MenuAttr.No, null, "功能编号", true, true, 1, 90, 50);
		map.AddDDLEntities(MenuAttr.ParentNo, null, DataType.AppString, "父节点", new Menus(), "No", "Name", false);
		map.AddTBString(MenuAttr.Name, null, "名称", true, false, 0, 300, 200, true);
		map.AddTBInt(MenuAttr.Idx, 0, "顺序号", true, false);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 与树有关的必备属性.

			// 类的字段属性. 
		map.AddDDLSysEnum(MenuAttr.MenuType, 0, "菜单类型", true, true, MenuAttr.MenuType, "@0=系统根目录@1=系统类别@2=系统@3=目录@4=功能/界面@5=功能控制点");

			// @0=系统根目录@1=系统类别@2=系统.
		map.AddDDLEntities(MenuAttr.FK_App, null, "系统", new Apps(), false);
		map.AddDDLSysEnum(MenuAttr.OpenWay, 1, "打开方式", true, true, MenuAttr.OpenWay, "@0=新窗口@1=本窗口@2=覆盖新窗口");

		map.AddTBString(MenuAttr.Url, null, "连接", false, false, 0, 3900, 200, true);
		map.AddTBString(MenuAttr.UrlExt, null, "连接", true, false, 0, 3900, 200, true);

		map.AddBoolean(MenuAttr.IsEnable, true, "是否启用?", true, true);
		map.AddTBString(MenuAttr.Icon, null, "Icon", true, false, 0, 500, 50, true);
		map.AddDDLSysEnum(MenuAttr.MenuCtrlWay, 0, "控制方式", true, true, MenuAttr.MenuCtrlWay, "@0=按照设置的控制@1=任何人都可以使用@2=Admin用户可以使用");

		map.AddTBString(MenuAttr.Flag, null, "标记", true, false, 0, 500, 20, false);
		map.AddTBString(MenuAttr.Tag1, null, "Tag1", true, false, 0, 500, 20, true);
		map.AddTBString(MenuAttr.Tag2, null, "Tag2", true, false, 0, 500, 20, true);
		map.AddTBString(MenuAttr.Tag3, null, "Tag3", true, false, 0, 500, 20, true);

		map.AddTBInt(MenuAttr.Idx, 0, "顺序号", true, false);

			//map.AddTBString(EntityNoMyFileAttr.WebPath, "/WF/Img/FileType/IE.gif", "图标", true, false, 0, 200, 20, true);

		map.AddMyFile("图标"); //附件.

		map.AddSearchAttr(MenuAttr.FK_App);
		map.AddSearchAttr(MenuAttr.MenuType);
		map.AddSearchAttr(MenuAttr.OpenWay);


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
		map.AttrsOfOneVSM.Add(new GroupMenus(), new Groups(), GroupMenuAttr.FK_Menu, GroupMenuAttr.FK_Group, EmpAttr.Name, EmpAttr.No, "绑定到权限组");


			//可以访问的权限组.
		map.AttrsOfOneVSM.Add(new StationMenus(), new BP.GPM.Stations(), StationMenuAttr.FK_Menu, StationMenuAttr.FK_Station, EmpAttr.Name, EmpAttr.No, "绑定到岗位-列表模式");

			//可以访问的权限组.
		map.AttrsOfOneVSM.AddGroupListModel(new StationMenus(), new BP.GPM.Stations(), StationMenuAttr.FK_Menu, StationMenuAttr.FK_Station, "绑定到岗位-分组模式", StationAttr.FK_StationType, "Name", EmpAttr.No);

			//节点绑定人员. 使用树杆与叶子的模式绑定.
		map.AttrsOfOneVSM.AddBranchesAndLeaf(new EmpMenus(), new BP.Port.Emps(), EmpMenuAttr.FK_Menu, EmpMenuAttr.FK_Emp, "绑定人员-树结构", EmpAttr.FK_Dept, EmpAttr.Name, EmpAttr.No, "@WebUser.FK_Dept");

			//不带有参数的方法.
		RefMethod rm = new RefMethod();
		rm.Title = "增加(增删改查)功能权限";
		rm.Warning = "确定要增加吗？";
		rm.ClassMethodName = this.toString() + ".DoAddRight3";
		rm.IsForEns = true;
		rm.IsCanBatch = true; //是否可以批处理？
		map.AddRefMethod(rm);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 基本功能.


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 创建菜单.
		rm = new RefMethod();
		rm.GroupName = "创建菜单(对目录有效)";
		rm.Title = "创建单据";
		rm.Warning = "您确定要创建吗？";

		rm.HisAttrs.AddTBString("No", null, "单据编号", true, false, 0, 100, 100);
		rm.HisAttrs.AddTBString("Name", null, "单据名称", true, false, 0, 100, 400);
		rm.HisAttrs.AddTBString("PTable", null, "存储表(为空则为编号相同)", true, false, 0, 100, 100);
		rm.HisAttrs.AddDDLSysEnum("FrmType", 0, "单据模式", true, true, "BillFrmType", "@0=傻瓜表单@1=自由表单");
		rm.HisAttrs.AddDDLSQL("Sys_FormTree", "", "选择表单树", "SELECT No,Name FROM Sys_FormTree WHERE ParentNo='1'");

		rm.ClassMethodName = this.toString() + ".DoAddCCBill";
		map.AddRefMethod(rm);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 创建菜单.


		this._enMap = map;
		return this._enMap;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 增加单据
	 
	 @param no 编号
	 @param name 名称
	 @param ptable 物理表
	 @param frmType 表单类型
	 @return 
	*/
	public final String DoAddCCBill(String no, String name, String ptable, int frmType, String formTree)
	{
		if (this.getMenuType() != GPM.MenuType.Dir)
		{
			return "err@菜单树的节点必须为目录才能创建.";
		}

		try
		{
			//创建表单.
			if (frmType == 0)
			{
				BP.Sys.CCFormAPI.CreateFrm(no, name, formTree, Sys.FrmType.FoolForm);
			}
			else
			{
				BP.Sys.CCFormAPI.CreateFrm(no, name, formTree, Sys.FrmType.FreeFrm);
			}

			//更改单据属性.
			BP.Frm.FrmBill fb = new BP.Frm.FrmBill(no);
			fb.No = no;
			fb.Name = name;
			fb.PTable = ptable;
			fb.Update();

			//执行绑定.
			fb.DoBindMenu(this.No,name);

			return "<a href='../Comm/En.htm?EnName=BP.Frm.FrmBill&No=" + no + "' target=_blank>打开单据属性</a>.";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 增加增删改查功能权限
	 
	 @return 
	*/
	public final String DoAddRight3()
	{
		if (this.getUrl().contains("Search.htm") == false && this.getUrl().contains("SearchBS.htm") == false)
		{
			return "该功能非Search组件，所以您不能增加功能权限.";
		}

		Object tempVar = this.DoCreateSubNode();
		Menu en = tempVar instanceof Menu ? (Menu)tempVar : null;
		en.Name = "增加权限";
		en.setMenuType(GPM.MenuType.Function); //功能权限.
		en.setUrl(this.getUrl());
		en.setTag1("Insert");
		en.Update();

		Object tempVar2 = this.DoCreateSubNode();
		en = tempVar2 instanceof Menu ? (Menu)tempVar2 : null;
		en.Name = "修改权限";
		en.setMenuType(GPM.MenuType.Function); //功能权限.
		en.setUrl(this.getUrl());
		en.setTag1("Update");
		en.Update();

		Object tempVar3 = this.DoCreateSubNode();
		en = tempVar3 instanceof Menu ? (Menu)tempVar3 : null;
		en.Name = "删除权限";
		en.setMenuType(GPM.MenuType.Function); //功能权限.
		en.setUrl(this.getUrl());
		en.setTag1("Delete");
		en.Update();

		return "增加成功,请刷新节点.";
	}
	/** 
	 路径
	*/
	public final String getWebPath()
	{
		return this.GetValStrByKey(EntityNoMyFileAttr.WebPath);
	}
	public final void setWebPath(String value)
	{
		this.SetValByKey(EntityNoMyFileAttr.WebPath, value);
	}
	/** 
	 更新
	 
	 @return 
	*/
	@Override
	protected boolean beforeUpdateInsertAction()
	{
		this.setWebPath(this.getWebPath().replace("//", "/"));

		this.setUrl(this.getUrlExt());
		return super.beforeUpdateInsertAction();
	}
	/** 
	 创建下级节点.
	 
	 @return 
	*/
	public final String DoMyCreateSubNode()
	{
		Entity en = this.DoCreateSubNode();
		en.SetValByKey(MenuAttr.FK_App, this.GetValByKey(MenuAttr.FK_App));
		en.Update();

		return en.ToJson();
	}
	/** 
	 创建同级节点.
	 
	 @return 
	*/
	public final String DoMyCreateSameLevelNode()
	{
		Entity en = this.DoCreateSameLevelNode();
		en.SetValByKey(MenuAttr.FK_App, this.GetValByKey(MenuAttr.FK_App));
		en.Update();
		return en.ToJson();
	}
}