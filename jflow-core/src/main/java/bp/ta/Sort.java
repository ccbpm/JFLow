package bp.ta;
import bp.en.EntityTree;
import bp.en.UAC;
import bp.en.Map;
import bp.sys.CCBPMRunModel;

/** 
  流程类别
*/
public class Sort extends EntityTree
{
		///#region 属性.
	/** 
	 组织编号
	*/
	public final String getOrgNo()
	{
		return this.GetValStrByKey(SortAttr.OrgNo);
	}
	public final void setOrgNo(String value)
	{
		this.SetValByKey(SortAttr.OrgNo, value);
	}
	public final String getDomain()
	{
		return this.GetValStrByKey(SortAttr.Domain);
	}
	public final void setDomain(String value)
	{
		this.SetValByKey(SortAttr.Domain, value);
	}
		///#endregion 属性.

		///#region 构造方法
	/** 
	 流程类别
	*/
	public Sort()
	{
	}
	/** 
	 流程类别
	 
	 @param _No
	*/
	public Sort(String _No) throws Exception {
		super(_No);
	}
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.IsDelete = false;
		uac.IsInsert = false;
		uac.IsUpdate = true;
		return uac;
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

		Map map = new Map("TA_Sort", "流程类别");

		map.AddTBStringPK(SortAttr.No, null, "编号", false, false, 1, 100, 20);
		map.AddTBString(SortAttr.ParentNo, null, "父节点No", false, false, 0, 100, 30);

		map.AddTBString(SortAttr.Name, null, "名称", true, false, 0, 200, 30, true);
		map.AddTBString(SortAttr.ShortName, null, "简称", true, false, 0, 200, 30, true);
		map.AddTBString(SortAttr.OrgNo, "0", "组织编号(0为系统组织)", false, false, 0, 150, 30);
		map.SetHelperAlert(SortAttr.OrgNo, "用于区分不同组织的的流程,比如:一个集团有多个子公司,每个子公司都有自己的业务流程.");

		map.AddTBString(SortAttr.Domain, null, "域/系统编号", true, false, 0, 100, 30);
		map.SetHelperAlert(SortAttr.Domain, "用于区分不同系统的流程,比如:一个集团有多个子系统每个子系统都有自己的流程,就需要标记那些流程是那个子系统的.");
		map.AddTBInt(SortAttr.Idx, 0, "目录显示顺序(发起列表)", true, false);

		this.set_enMap(map);
		return this.get_enMap();
	}

	/** 
	 创建的时候，给他增加一个OrgNo。
	 
	 @return 
	*/
	@Override
	protected boolean beforeInsert() throws Exception {
		if (bp.difference.SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			this.setOrgNo(bp.web.WebUser.getOrgNo());
		}

		return super.beforeInsert();
	}
	@Override
	protected boolean beforeUpdate() throws Exception {
		return super.beforeUpdate();
	}
	/** 
	 删除之前的逻辑
	 
	 @return 
	*/
	@Override
	protected boolean beforeDelete() throws Exception {
		////检查是否有流程？
		//Paras ps = new Paras();
		//ps.SQL = "SELECT COUNT(*) FROM TA_Template WHERE Sort=" + BP.Difference.SystemConfig.AppCenterDBVarStr + "fk_Sort";
		//ps.Add("SortNo", this.No);
		////string sql = "SELECT COUNT(*) FROM WF_Flow WHERE FK_Sort='" + fk_Sort + "'";
		//if (DBAccess.RunSQLReturnValInt(ps) != 0)
		//    throw new Exception("err@该目录下有流程，您不能删除。");

		////检查是否有子目录？
		//ps = new Paras();
		//ps.SQL = "SELECT COUNT(*) FROM WF_Sort WHERE ParentNo=" + BP.Difference.SystemConfig.AppCenterDBVarStr + "ParentNo";
		//ps.Add("ParentNo", this.No);
		////sql = "SELECT COUNT(*) FROM WF_Sort WHERE ParentNo='" + fk_Sort + "'";
		//if (DBAccess.RunSQLReturnValInt(ps) != 0)
		//    throw new Exception("err@该目录下有子目录，您不能删除...");

		return super.beforeDelete();
	}
}
