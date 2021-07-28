package bp.wf.template;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.en.*;
import bp.en.Map;
import bp.port.*;
import bp.sys.*;
import bp.web.WebUser;
import bp.wf.*;
import bp.wf.Glo;

import java.util.*;

/** 
  流程类别
*/
public class FlowSort extends EntityTree
{

		///属性.
	/** 
	 组织编号
	 * @throws Exception 
	*/
	public final String getOrgNo() throws Exception
	{
		return this.GetValStrByKey(FlowSortAttr.OrgNo);
	}
	public final void setOrgNo(String value) throws Exception
	{
		this.SetValByKey(FlowSortAttr.OrgNo, value);
	}
	public final String getDomain()throws Exception
	{
		return this.GetValStrByKey(FlowSortAttr.Domain);
	}
	public final void setDomain(String value) throws Exception
	{
		this.SetValByKey(FlowSortAttr.Domain, value);
	}

		/// 属性.


		///构造方法
	/** 
	 流程类别
	*/
	public FlowSort()
	{
	}
	/** 
	 流程类别
	 
	 @param _No
	*/
	public FlowSort(String _No)throws Exception
	{
		super(_No);
	}
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.IsDelete = false;
		uac.IsInsert = false;
		uac.IsUpdate = true;
		return uac;
	}

		///

	/** 
	 流程类别Map
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_FlowSort", "流程类别");

		map.AddTBStringPK(FlowSortAttr.No, null, "编号", true, true, 1, 100, 20);
		map.AddTBString(FlowSortAttr.ParentNo, null, "父节点No", true, true, 0, 100, 30);
		map.AddTBString(FlowSortAttr.Name, null, "名称", true, false, 0, 200, 30, true);
		map.AddTBString(FlowSortAttr.OrgNo, "0", "组织编号(0为系统组织)", true, false, 0, 150, 30);
		map.SetHelperAlert(FlowSortAttr.OrgNo, "用于区分不同组织的的流程,比如:一个集团有多个子公司,每个子公司都有自己的业务流程.");

		map.AddTBString(FlowSortAttr.Domain, null, "域/系统编号", true, false, 0, 100, 30);
		map.SetHelperAlert(FlowSortAttr.Domain, "用于区分不同系统的流程,比如:一个集团有多个子系统每个子系统都有自己的流程,就需要标记那些流程是那个子系统的.");
		map.AddTBInt(FlowSortAttr.Idx, 0, "Idx", false, false);

		this.set_enMap(map);
		return this.get_enMap();
	}
	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	 ///#region 创建节点.
	/** 
	 创建同级目录.
	 
	 @param name
	 @return 
	 * @throws Exception 
	*/
	public final String DoCreateSameLevelNodeMy(String name) throws Exception
	{
		EntityTree en = this.DoCreateSameLevelNode(name);
		en.setName(name);
		en.Update();
		return en.getNo();
	}
	/** 
	 创建下级目录.
	 
	 @param name
	 @return 
	 * @throws Exception 
	*/
	public final String DoCreateSubNodeMy(String name) throws Exception
	{
		EntityTree en = this.DoCreateSubNode(name);
		en.setName(name);
		en.Update();
		return en.getNo();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion 创建节点.
	  /** 
		 删除之前的逻辑  @hongyan
		 
		 @return 
	 * @throws Exception 
  */
	@Override
	protected boolean beforeDelete() throws Exception
	{
		//检查是否有流程？
		Paras ps = new Paras();
		ps.SQL = "SELECT COUNT(*) FROM WF_Flow WHERE FK_FlowSort=" + SystemConfig.getAppCenterDBVarStr() + "fk_flowSort";
		ps.Add("fk_flowSort", this.getNo());
		//string sql = "SELECT COUNT(*) FROM WF_Flow WHERE FK_FlowSort='" + fk_flowSort + "'";
		if (DBAccess.RunSQLReturnValInt(ps) != 0)
		{
			throw new RuntimeException("err@该目录下有流程，您不能删除。");
		}

		//检查是否有子目录？
		ps = new Paras();
		ps.SQL = "SELECT COUNT(*) FROM WF_FlowSort WHERE ParentNo=" + SystemConfig.getAppCenterDBVarStr()  + "ParentNo";
		ps.Add("ParentNo", this.getNo());
		//sql = "SELECT COUNT(*) FROM WF_FlowSort WHERE ParentNo='" + fk_flowSort + "'";
		if (DBAccess.RunSQLReturnValInt(ps) != 0)
		{
			throw new RuntimeException("err@该目录下有子目录，您不能删除...");
		}

		return super.beforeDelete();
	}

	/** 
	 创建的时候，给他增加一个OrgNo。
	 
	 @return 
	 * @throws Exception 
	*/
	@Override
	protected boolean beforeInsert() throws Exception
	{
		if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			this.setOrgNo(WebUser.getOrgNo());
		}

		return super.beforeInsert();
	}
	/** 
	 
	 
	 @return 
	 * @throws Exception 
	*/
	@Override
	protected boolean beforeUpdate() throws Exception
	{
		//更新流程引擎控制表.
		String sql = "UPDATE WF_GenerWorkFlow SET Domain='" + this.getDomain() + "' WHERE FK_FlowSort='" + this.getNo()+ "'";
		DBAccess.RunSQL(sql);

	
		if (Glo.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			sql = "UPDATE WF_Emp SET StartFlows='' ";
		}
		else
		{
			sql = "UPDATE WF_Emp SET StartFlows='' ";
		}

		DBAccess.RunSQL(sql);
		return super.beforeUpdate();
	}

}