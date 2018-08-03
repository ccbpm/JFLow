package BP.GPM;

import BP.DA.*;
import BP.En.*;

/** 
 系统类别
 
*/
public class AppSort extends EntityNoName
{
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
	 * @throws Exception 
	*/
	public AppSort(String no) throws Exception
	{
		this.setNo(no);
		this.Retrieve();
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
		Map map = new Map("GPM_AppSort");
		map.setDepositaryOfEntity(Depositary.None);
		map.setEnDesc("系统类别");
		map.setEnType(EnType.App);
		map.setIsAutoGenerNo(true);

		map.AddTBStringPK(AppSortAttr.No, null, "编号", true, true, 2, 2, 20);
		map.AddTBString(AppSortAttr.Name, null, "名称", true, false, 0, 300, 20);
		map.AddTBInt(AppSortAttr.Idx, 0, "显示顺序", true, false);
		map.AddTBString(AppSortAttr.RefMenuNo, null, "关联的菜单编号", false, false, 0, 300, 20);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	@Override
	protected boolean beforeDelete() throws Exception
	{
		Apps pps = new Apps();
		pps.Retrieve(AppAttr.FK_AppSort, this.getNo());
		if (pps.size() != 0)
		{
			throw new RuntimeException("err@该类别下有系统，您不能删除，请把该系统类别下的系统移除或者删除，您才能删除该类别。");
		}

		Menu root = new Menu();
		root.setNo(this.getRefMenuNo());
		if (root.RetrieveFromDBSources() > 0)
		{
			root.Delete();
		}
		return super.beforeDelete();
	}

	@Override
	protected boolean beforeUpdate() throws Exception
	{
		Menu root = new Menu();
		root.setNo(this.getRefMenuNo());
		if (root.RetrieveFromDBSources() > 0)
		{
			root.setName(this.getName());
			root.Update();
		}
		return super.beforeUpdate();
	}

	@Override
	protected boolean beforeInsert() throws Exception
	{
		super.beforeInsert();

		// 求root.
		Menu root = new Menu();
		root.setNo("1000");
		if (root.RetrieveFromDBSources() == 0)
		{
			//如果没有root.
			root.setParentNo("0");
			root.setName(BP.Sys.SystemConfig.getSysName());
			root.setFK_App(BP.Sys.SystemConfig.getSysNo());
			root.setHisMenuType(MenuType.Root);
			root.setIdx(0);
			root.Insert();
		}

		// 创建系统类别做为二级菜单.
		Object tempVar = root.DoCreateSubNode();
		Menu sort1 = (Menu)((tempVar instanceof Menu) ? tempVar : null);
		sort1.setName(this.getName());
		sort1.setHisMenuType(MenuType.AppSort);
		sort1.setFK_App("AppSort");
		sort1.Update();

		this.setRefMenuNo(sort1.getNo());
		return true;
	}
}