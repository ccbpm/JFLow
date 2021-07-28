package bp.gpm.menu2020;

import bp.da.DBAccess;
import bp.da.DataType;
import bp.da.Depositary;
import bp.difference.SystemConfig;
import bp.en.EnType;
import bp.en.EntityNoName;
import bp.en.Map;
import bp.en.UAC;
import bp.sys.CCBPMRunModel;
import bp.web.WebUser;

/** 
模块
*/
public class Module extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 属性
	/** 
	 组织编号
	 * @throws Exception 
	*/
	public final String getOrgNo() throws Exception
	{
		return this.GetValStrByKey(ModuleAttr.OrgNo);
	}
	public final void setOrgNo(String value) throws Exception
	{
		this.SetValByKey(ModuleAttr.OrgNo, value);
	}
	/** 
	 系统编号
	 * @throws Exception 
	*/
	public final String getSystemNo() throws Exception
	{
		return this.GetValStrByKey(ModuleAttr.SystemNo);
	}
	public final void setSystemNo(String value) throws Exception
	{
		this.SetValByKey(ModuleAttr.SystemNo, value);
	}
	public final String getIcon() throws Exception
	{
		return this.GetValStrByKey(ModuleAttr.Icon);
	}
	public final void setIcon(String value) throws Exception
	{
		this.SetValByKey(ModuleAttr.Icon, value);
	}
	public final int getIdx() throws Exception
	{
		return this.GetValIntByKey(ModuleAttr.Idx);
	}
	public final void setIdx(int value) throws Exception
	{
		this.SetValByKey(ModuleAttr.Idx, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 按钮权限控制
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 构造方法
	/** 
	 模块
	*/
	public Module()
	{
	}
	/** 
	 模块
	 
	 @param mypk
	 * @throws Exception 
	*/
	public Module(String no) throws Exception
	{
		this.setNo(no);
		this.Retrieve();
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
		Map map = new Map("GPM_Module");
		map.setDepositaryOfEntity(Depositary.None);
		map.setEnDesc("模块");
		map.setEnType(EnType.App);
		map.setIsAutoGenerNo(false);

		map.AddTBStringPK(ModuleAttr.No, null, "编号", true, true, 1, 200, 20);
		map.AddTBString(ModuleAttr.Name, null, "名称", true, false, 0, 300, 20);
			//map.AddTBString(ModuleAttr.SystemNo, null, "系统", false, false, 0, 50, 20);
			//map.AddTBString(ModuleAttr.SystemNo, null, "系统编号", false, false, 0, 30, 20);
		map.AddDDLEntities(ModuleAttr.SystemNo, null, "隶属系统", new MySystems(), true);
			//map.AddTBString(ModuleAttr.Remark, null, "标记", true, false, 0, 300, 20);
			//map.AddBoolean(MenuAttr.IsEnable, true, "是否启用?", true, true);
		map.AddTBString(MenuAttr.Icon, null, "Icon", true, false, 0, 500, 50, true);

			//map.AddDDLSysEnum(MenuAttr.MenuCtrlWay, 0, "控制方式", true, true, MenuAttr.MenuCtrlWay,
			//  "@0=按照设置的控制@1=任何人都可以使用@2=Admin用户可以使用");
		map.AddTBInt(ModuleAttr.Idx, 0, "显示顺序", true, false);

		if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			map.AddTBString(ModuleAttr.OrgNo, null, "组织编号", true, false, 0, 50, 20);
		}

			// @0=自定义菜单. @1=系统菜单.
		map.AddTBInt(MenuAttr.WorkType, 0, "工作类型", false, false);

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 移动方法.
	/** 
	 向上移动
	 * @throws Exception 
	*/
	public final void DoUp() throws Exception
	{
		this.DoOrderUp(ModuleAttr.SystemNo, this.getSystemNo(), ModuleAttr.Idx);
	}
	/** 
	 向下移动
	 * @throws Exception 
	*/
	public final void DoDown() throws Exception
	{
		this.DoOrderDown(ModuleAttr.SystemNo, this.getSystemNo(), ModuleAttr.Idx);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion 移动方法.

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
		return super.beforeInsert();
	}
	@Override
	protected boolean beforeDelete() throws Exception
	{
		Menus ens = new Menus();
		ens.Retrieve(MenuAttr.ModuleNo, this.getNo());
		if (ens.size() != 0)
		{
			throw new RuntimeException("err@该模块下有菜单，您不能删除。");
		}

		return super.beforeDelete();
	}

}

