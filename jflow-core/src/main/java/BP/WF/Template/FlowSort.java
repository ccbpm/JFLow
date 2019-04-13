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

	public final String getDomain()
	{
		return this.GetValStringByKey(FlowSortAttr.Domain);		 
	}
		
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
		
		 String sql = "UPDATE WF_GenerWorkFlow SET Domain='" + this.getDomain() + "' WHERE FK_FlowSort='" + this.getNo() + "'";
         DBAccess.RunSQL(sql);
         
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