package bp.gpm.menu2020;

import bp.da.DBAccess;
import bp.da.DataType;
import bp.da.Depositary;
import bp.difference.SystemConfig;
import bp.en.EntityNoName;
import bp.en.Map;
import bp.en.UAC;
import bp.sys.CCBPMRunModel;
import bp.web.WebUser;

/** 
系统
*/
public class MySystem extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
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
	 * @throws Exception 
	*/
	public final String getWebPath() throws Exception
	{
		return this.GetValStringByKey("WebPath");
	}
	/** 
	 是否启用
	 * @throws Exception 
	*/
	public final boolean getIsEnable() throws Exception
	{
		return this.GetValBooleanByKey(MySystemAttr.IsEnable);
	}
	public final void setIsEnable(boolean value) throws Exception
	{
		this.SetValByKey(MySystemAttr.IsEnable, value);
	}
	/** 
	 顺序
	 * @throws Exception 
	*/
	public final int getIdx() throws Exception
	{
		return this.GetValIntByKey(MySystemAttr.Idx);
	}
	public final void setIdx(int value) throws Exception
	{
		this.SetValByKey(MySystemAttr.Idx, value);
	}
	/** 
	 Icon
	 * @throws Exception 
	*/
	public final String getIcon() throws Exception
	{
		return this.GetValStrByKey(MySystemAttr.Icon);
	}
	public final void setIcon(String value) throws Exception
	{
		this.SetValByKey(MySystemAttr.Icon, value);
	}
	/** 
	 密码控件ID
	 * @throws Exception 
	*/
	public final String getPwdControl() throws Exception
	{
		return this.GetValStrByKey(MySystemAttr.PwdControl);
	}
	public final void setPwdControl(String value) throws Exception
	{
		this.SetValByKey(MySystemAttr.PwdControl, value);
	}
	/** 
	 提交方式
	 * @throws Exception 
	*/
	public final String getActionType() throws Exception
	{
		return this.GetValStrByKey(MySystemAttr.ActionType);
	}
	public final void setActionType(String value) throws Exception
	{
		this.SetValByKey(MySystemAttr.ActionType, value);
	}
	/** 
	 登录方式@0=SID验证@1=连接@2=表单提交
	 * @throws Exception 
	*/
	public final String getSSOType() throws Exception
	{
		return this.GetValStrByKey(MySystemAttr.SSOType);
	}
	public final void setSSOType(String value) throws Exception
	{
		this.SetValByKey(MySystemAttr.SSOType, value);
	}
	public final String getOrgNo() throws Exception
	{
		return this.GetValStringByKey(MySystemAttr.OrgNo);
	}
	public final void setOrgNo(String value) throws Exception
	{
		this.SetValByKey(MySystemAttr.OrgNo, value);
	}
	public final String getRefMenuNo() throws Exception
	{
		return this.GetValStringByKey(MySystemAttr.RefMenuNo);
	}
	public final void setRefMenuNo(String value) throws Exception
	{
		this.SetValByKey(MySystemAttr.RefMenuNo, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 按钮权限控制
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenAll();
		return uac;
	}
		///#endregion
		///#region 构造方法
	/** 
	 系统
	*/
	public MySystem()
	{
	}
	/** 
	 系统
	 
	 @param mypk
	 * @throws Exception 
	*/
	public MySystem(String no) throws Exception
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
		Map map = new Map("GPM_System", "系统");
		map.setDepositaryOfEntity(Depositary.None);

		map.AddTBStringPK(MySystemAttr.No, null, "编号", true, false, 2, 100, 100);
		map.AddTBString(MySystemAttr.Name, null, "名称", true, false, 0, 300, 150, true);
		map.AddBoolean(MySystemAttr.IsEnable, true, "启用?", true, true);
		map.AddTBString(MySystemAttr.Icon, null, "图标", true, false, 0, 50, 150, true);
		map.AddTBInt(MySystemAttr.Idx, 0, "显示顺序", true, false);

		if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			map.AddTBString(MenuAttr.OrgNo, null, "组织编号", true, false, 0, 50, 20);
		}

			//RefMethod rm = new RefMethod();
			//rm.Title = "编辑菜单";
			//rm.ClassMethodName = this.ToString() + ".DoMenu";
			//rm.RefMethodType = RefMethodType.LinkeWinOpen;
			//map.AddRefMethod(rm);

			//rm = new RefMethod();
			//rm.Title = "查看可访问该系统的人员";
			//rm.ClassMethodName = this.ToString() + ".DoWhoCanUseMySystem";

			//rm = new RefMethod();
			//rm.Title = "刷新设置";
			//rm.ClassMethodName = this.ToString() + ".DoRef";

			//rm = new RefMethod();
			//rm.Title = "第二连接";
			////rm.Title = "第二连接：登录方式为不传值、连接不设置用户名密码转为第二连接。";
			//rm.ClassMethodName = this.ToString() + ".About";
			//// map.AddRefMethod(rm);
		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

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
		Modules ens = new Modules();
		ens.Retrieve(ModuleAttr.SystemNo, this.getNo());
		if (ens.size() != 0)
		{
			throw new RuntimeException("err@该系统下有子模块，您不能删除。");
		}

		//看看这个类别下是否有表单，如果有就删除掉.
		String sql = "SELECT COUNT(No) AS No FROM Sys_MapData WHERE FK_FormTree='" + this.getNo() + "'";
		if (DBAccess.RunSQLReturnValInt(sql) == 0)
		{
			DBAccess.RunSQL("DELETE FROM Sys_FormTree WHERE No='" + this.getNo() + "' ");
		}

		//看看这个类别下是否有流程，如果有就删除掉.
		  sql = "SELECT COUNT(No) AS No FROM WF_Flow WHERE FK_FlowSort='" + this.getNo() + "'";
		if (DBAccess.RunSQLReturnValInt(sql) == 0)
		{
			DBAccess.RunSQL("DELETE FROM WF_FlowSort WHERE No='" + this.getNo() + "' ");
		}

		return super.beforeDelete();
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 移动方法.
	/** 
	 向上移动
	 * @throws Exception 
	*/
	public final void DoUp() throws Exception
	{
		if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			this.DoOrderUp(MySystemAttr.OrgNo, this.getOrgNo(), MySystemAttr.Idx);
		}
		else
		{
			this.DoOrderUp(MySystemAttr.Idx);
		}
	}
	/** 
	 向下移动
	 * @throws Exception 
	*/
	public final void DoDown() throws Exception
	{
		if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			this.DoOrderDown(MySystemAttr.OrgNo, this.getOrgNo(), MySystemAttr.Idx);
		}
		else
		{
			this.DoOrderDown(MySystemAttr.Idx);
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion 移动方法.

}

