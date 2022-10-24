package bp.wf.admin;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.port.*;
import bp.sys.*;
import java.util.*;

/** 
  表单目录
*/
public class FrmSort extends EntityNoName
{

		///#region 属性.
	/** 
	 组织编号
	*/
	public final String getOrgNo()
	{
		return this.GetValStrByKey(FrmSortAttr.OrgNo);
	}
	public final void setOrgNo(String value)
	{
		this.SetValByKey(FrmSortAttr.OrgNo, value);
	}
	public final String getDomain()
	{
		return this.GetValStrByKey(FrmSortAttr.Domain);
	}
	public final void setDomain(String value)
	{
		this.SetValByKey(FrmSortAttr.Domain, value);
	}
	public final String getParentNo()
	{
		return this.GetValStrByKey(FrmSortAttr.ParentNo);
	}
	public final void setParentNo(String value)
	{
		this.SetValByKey(FrmSortAttr.ParentNo, value);
	}

		///#endregion 属性.


		///#region 构造方法
	/** 
	 表单目录
	*/
	public FrmSort()
	{
	}
	/** 
	 表单目录
	 
	 @param _No
	*/
	public FrmSort(String _No)
	{
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
	 表单目录Map
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_FormTree", "表单目录");

		map.AddTBStringPK(FrmSortAttr.No, null, "编号", false, false, 1, 100, 20);
		map.AddTBString(FrmSortAttr.ParentNo, null, "父节点No", false, false, 0, 100, 30);

		map.AddTBString(FrmSortAttr.Name, null, "名称", true, false, 0, 200, 30, true);
		map.AddTBString(FrmSortAttr.ShortName, null, "简称", true, false, 0, 200, 30, true);

		map.AddTBString(FrmSortAttr.OrgNo, "0", "组织编号(0为系统组织)", false, false, 0, 150, 30);
		map.SetHelperAlert(FrmSortAttr.OrgNo, "用于区分不同组织的的流程,比如:一个集团有多个子公司,每个子公司都有自己的业务流程.");

		map.AddTBString(FrmSortAttr.Domain, null, "域/系统编号", true, false, 0, 100, 30);
		map.SetHelperAlert(FrmSortAttr.Domain, "用于区分不同系统的流程,比如:一个集团有多个子系统每个子系统都有自己的流程,就需要标记那些流程是那个子系统的.");
		map.AddTBInt(FrmSortAttr.Idx, 0, "Idx", false, false);

		this.set_enMap(map);
		return this.get_enMap();
	}

	/** 
	 创建的时候，给他增加一个OrgNo。
	 
	 @return 
	*/
	@Override
	protected boolean beforeInsert() throws Exception {
		if (bp.wf.Glo.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			this.setOrgNo(bp.web.WebUser.getOrgNo());
		}

		return super.beforeInsert();
	}
	/** 
	 删除之前的逻辑
	 
	 @return 
	*/
	@Override
	protected boolean beforeDelete() throws Exception {
		String sql = "SELECT COUNT(*) as Num FROM Sys_MapData WHERE FK_FormTree='" + this.getNo() + "'";
		int num = DBAccess.RunSQLReturnValInt(sql);
		if (num != 0)
		{
			throw new RuntimeException("err@您不能删除该目录，下面有表单。");
		}

		return super.beforeDelete();
	}
}