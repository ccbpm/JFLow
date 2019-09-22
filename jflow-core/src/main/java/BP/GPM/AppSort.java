package BP.GPM;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 系统类别
*/
public class AppSort extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	/** 
	 RefMenuNo
	*/
	public final String getRefMenuNo()
	{
		return this.GetValStrByKey(AppSortAttr.RefMenuNo);
	}
	public final void setRefMenuNo(String value)
	{
		this.SetValByKey(AppSortAttr.RefMenuNo, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 按钮权限控制
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenForAppAdmin();
		return uac;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 系统类别
	*/
	public AppSort()
	{
	}
	/** 
	 系统类别
	 
	 @param mypk
	*/
	public AppSort(String no)
	{
		this.No = no;
		this.Retrieve();
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
		Map map = new Map("GPM_AppSort");
		map.DepositaryOfEntity = Depositary.None;
		map.EnDesc = "系统类别";
		map.EnType = EnType.App;
		map.IsAutoGenerNo = true;


		map.AddTBStringPK(AppSortAttr.No, null, "编号", true, true, 2, 2, 20);
		map.AddTBString(AppSortAttr.Name, null, "名称", true, false, 0, 300, 20);
		map.AddTBInt(AppSortAttr.Idx, 0, "显示顺序", true, false);
		map.AddTBString(AppSortAttr.RefMenuNo, null, "关联的菜单编号", false, false, 0, 300, 20);

		this._enMap = map;
		return this._enMap;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	@Override
	protected boolean beforeDelete()
	{
		Apps pps = new Apps();
		pps.Retrieve(AppAttr.FK_AppSort, this.No);
		if (pps.Count != 0)
		{
			throw new RuntimeException("err@该类别下有系统，您不能删除，请把该系统类别下的系统移除或者删除，您才能删除该类别。");
		}

		Menu root = new Menu();
		root.No = this.getRefMenuNo();
		if (root.RetrieveFromDBSources() > 0)
		{
			root.Delete();
		}
		return super.beforeDelete();
	}

	@Override
	protected boolean beforeUpdate()
	{
		Menu root = new Menu();
		root.No = this.getRefMenuNo();
		if (root.RetrieveFromDBSources() > 0)
		{
			root.Name = this.Name;
			root.Update();
		}
		return super.beforeUpdate();
	}

	@Override
	protected boolean beforeInsert()
	{
		super.beforeInsert();

		// 求root.
		Menu root = new Menu();
		root.No = "1000";
		if (root.RetrieveFromDBSources() == 0)
		{
			/*如果没有root.*/
			root.ParentNo = "0";
			root.Name = BP.Sys.SystemConfig.SysName;
			root.setFK_App(BP.Sys.SystemConfig.SysNo);
			root.setHisMenuType(MenuType.Root);
			root.Idx = 0;
			root.Insert();
		}

		// 创建系统类别做为二级菜单.
		Object tempVar = root.DoCreateSubNode();
		Menu sort1 = tempVar instanceof Menu ? (Menu)tempVar : null;
		sort1.Name = this.Name;
		sort1.setHisMenuType(MenuType.AppSort);
		sort1.setFK_App("AppSort");
		sort1.Update();

		this.setRefMenuNo(sort1.No);
		return true;
	}
}