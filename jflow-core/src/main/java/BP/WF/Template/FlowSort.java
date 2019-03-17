package BP.WF.Template;

import BP.DA.DBAccess;
import BP.En.EntityTree;
import BP.En.Map;
import BP.Sys.OSModel;
import BP.Sys.SystemConfig;
import BP.WF.Flow;
import BP.WF.Flows;

/** 
  流程类别
 
*/
public class FlowSort extends EntityTree
{

		
	/** 
	 流程类别
	 
	*/
	public FlowSort()
	{
	}
	/** 
	 流程类别
	 
	 @param _No
	 * @throws Exception 
	*/
	public FlowSort(String _No) throws Exception
	{
		super(_No);
	}

		///#endregion

	/** 
	 流程类别Map
	 
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		  Map map = new Map("WF_FlowSort", "流程类别");


          map.AddTBStringPK(FlowSortAttr.No, null, "编号", true, true, 1, 100, 20);
          map.AddTBString(FlowSortAttr.ParentNo, null, "父节点No", true, true, 0, 100, 30);
          map.AddTBString(FlowSortAttr.Name, null, "名称", true, false, 0, 200, 30,true);
          map.AddTBString(FlowSortAttr.OrgNo, "0", "组织编号(0为系统组织)", true, false, 0, 150, 30);
          map.SetHelperAlert(FlowSortAttr.OrgNo, "用于区分不同组织的的流程,比如:一个集团有多个子公司,每个子公司都有自己的业务流程.");

          map.AddTBString(FlowSortAttr.Domain, null, "域/系统编号", true, false, 0, 100, 30);
          map.SetHelperAlert(FlowSortAttr.Domain, "用于区分不同系统的流程,比如:一个集团有多个子系统每个子系统都有自己的流程,就需要标记那些流程是那个子系统的.");
          map.AddTBInt(FlowSortAttr.Idx, 0, "Idx", false, false);


		this.set_enMap(map);
		return this.get_enMap();
	}


		///#region 重写方法
	public final void WritToGPM()
	{
		return;

		/*if (BP.WF.Glo.getOSModel() == BP.Sys.OSModel.OneOne)
		{
			return;
		}

		String pMenuNo = "";


			///#region 检查系统是否存在，并返回系统菜单编号
		String sql = "SELECT * FROM GPM_App where No='" + SystemConfig.getSysNo() + "'";

		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt != null && dt.Rows.size() == 0)
		{
			//系统类别
			sql = "SELECT No FROM GPM_Menu WHERE ParentNo=0";
			String sysRootNo = DBAccess.RunSQLReturnStringIsNull(sql, "0");
			// 取得该功能菜单的主键编号.
			pMenuNo = DBAccess.GenerOID("BP.GPM.Menu")+"";
			String url = BP.WF.Glo.getHostURL();
			//如果没有系统，就插入该系统菜单.
			sql = "INSERT INTO GPM_Menu(No,Name,ParentNo,IsDir,MenuType,FK_App,IsEnable,Flag)";
			sql += " VALUES('{0}','{1}','{2}',1,2,'{3}',1,'{4}')";
			sql = String.format(sql, pMenuNo, SystemConfig.getSysName(), sysRootNo, SystemConfig.getSysNo(), "FlowSort" + SystemConfig.getSysNo());
			DBAccess.RunSQL(sql);
			//如果没有系统，就插入该系统.
			sql = "INSERT INTO GPM_App(No,Name,AppModel,FK_AppSort,Url,RefMenuNo,MyFileName)";
			sql += " VALUES('{0}','{1}',0,'01','{2}','{3}','admin.gif')";

			sql = String.format(sql, SystemConfig.getSysNo(), SystemConfig.getSysName(), url, pMenuNo);
			sql = sql.replace("//", "/");
			DBAccess.RunSQL(sql);
		}
		else
		{
			pMenuNo = dt.Rows.get(0).getValue("RefMenuNo").toString();
		}

			///#endregion

		try
		{
			sql = "SELECT * FROM GPM_Menu WHERE Flag='FlowSort" + this.getNo() + "'";
			dt = DBAccess.RunSQLReturnTable(sql);
			if (dt.Rows.size() >= 1)
			{
				DBAccess.RunSQL("DELETE FROM GPM_Menu WHERE Flag='FlowSort" + this.getNo() + "' AND FK_App='" + SystemConfig.getSysNo() + "' ");
			}
		}
		catch (java.lang.Exception e)
		{
		}

		// 组织数据。
		// 获取他的ParentNo
		String parentNo = ""; //this.ParentNo
		if (!StringHelper.isNullOrEmpty(this.getParentNo()))
		{
			sql = "SELECT * FROM GPM_Menu WHERE Flag='FlowSort" + this.getParentNo() + "'";
			dt = DBAccess.RunSQLReturnTable(sql);
			if (dt.Rows.size() >= 1)
			{
				pMenuNo = dt.Rows.get(0).getValue("No").toString();
			}
		}

		String menuNo = DBAccess.GenerOID("BP.GPM.Menu")+"";
		// 执行插入.
		sql = "INSERT INTO GPM_Menu(No,Name,ParentNo,IsDir,MenuType,FK_App,IsEnable,Flag)";
		sql += " VALUES('{0}','{1}','{2}',{3},{4},'{5}',{6},'{7}')";
		sql = String.format(sql, menuNo, this.getName(), pMenuNo, 1, 3, SystemConfig.getSysNo(), 1, "FlowSort" + this.getNo());
		DBAccess.RunSQL(sql);*/
	}

	@Override
	protected boolean beforeInsert() throws Exception
	{
		this.WritToGPM();
		return super.beforeInsert();
	}

	@Override
	protected boolean beforeDelete() throws Exception
	{
		try
		{
			//删除权限管理
			if (BP.WF.Glo.getOSModel() == OSModel.OneMore)
			{
				DBAccess.RunSQL("DELETE FROM GPM_Menu WHERE Flag='FlowSort" + this.getNo() + "' AND FK_App='" + SystemConfig.getSysNo() + "'");
			}
		}
		catch (java.lang.Exception e)
		{
		}
		return super.beforeDelete();
	}

	@Override
	protected boolean beforeUpdate() throws Exception
	{
		//修改权限管理
		if (BP.WF.Glo.getOSModel() == OSModel.OneMore)
		{
		  //  DBAccess.RunSQL("UPDATE  GPM_Menu SET Name='" + this.Name + "' WHERE Flag='FlowSort" + this.getNo() + "' AND FK_App='" + SystemConfig.getSysNo() + "'");
		}
		return super.beforeUpdate();
	}

		///#endregion 重写方法


		///#region 扩展方法
	/** 
	 子文件夹
	 
	*/
	public final FlowSorts getHisSubFlowSorts()
	{
		try
		{
			FlowSorts flowSorts = new FlowSorts();
			flowSorts.RetrieveByAttr(FlowSortAttr.ParentNo, this.getNo());
			return flowSorts;
		}
		catch (java.lang.Exception e)
		{
		}
		return null;
	}

	/** 
	 类别下所包含流程
	 
	*/
	public final Flows getHisFlows()
	{
		try
		{
			Flows flows = new Flows();
			flows.RetrieveByAttr(BP.WF.Template.FlowAttr.FK_FlowSort, this.getNo());
			return flows;
		}
		catch (java.lang.Exception e)
		{
		}
		return null;
	}
	/** 
	 强制删除该流程类别下子项，递归
	 
	 @return 
	 * @throws Exception 
	*/
	public final boolean DeleteFlowSortSubNode_Force() throws Exception
	{
		try
		{
			//删除流程
			for (Flow flow : this.getHisFlows().ToJavaList())
			{
				flow.DoDelete();
			}
			//删除类别
			for (FlowSort flowSort : this.getHisSubFlowSorts().ToJavaListFs())
			{
				Delete_FlowSort_SubNodes(flowSort);
			}

			return true;
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException(ex.getMessage());
		}
	}

	/** 
	 删除类别下所有项，子流程类别、流程
	 
	 @param FK_FlowSort 流程类别编号
	 @return true false
	 * @throws Exception 
	*/
	private boolean Delete_FlowSort_SubNodes(FlowSort sub_FlowSort) throws Exception
	{
		try
		{
			//删除流程
			for (Flow flow : sub_FlowSort.getHisFlows().ToJavaList())
			{
				flow.DoDelete();
			}
			//删除类别
			for (FlowSort flowSort : sub_FlowSort.getHisSubFlowSorts().ToJavaListFs())
			{
				Delete_FlowSort_SubNodes(flowSort);
			}

			sub_FlowSort.Delete();
			return true;
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException(ex.getMessage());
		}
	}

		///#endregion
	
	/** 组织编号
	 
	*/
	public final String getOrgNo()
	{
		return this.GetValStrByKey(FlowSortAttr.OrgNo);
	}
	public final void setOrgNo(String value)
	{
		this.SetValByKey(FlowSortAttr.OrgNo, value);
	}
}