package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
  流程类别
*/
public class FlowSort extends EntityTree
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性.
	/** 
	 组织编号
	*/
	public final String getOrgNo()
	{
		return this.GetValStrByKey(FlowSortAttr.OrgNo);
	}
	public final void setOrgNo(String value)
	{
		this.SetValByKey(FlowSortAttr.OrgNo, value);
	}
	public final String getDomain()
	{
		return this.GetValStrByKey(FlowSortAttr.Domain);
	}
	public final void setDomain(String value)
	{
		this.SetValByKey(FlowSortAttr.Domain, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
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
	public FlowSort(String _No)
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 流程类别Map
	*/
	@Override
	public Map getEnMap()
	{
		if (this._enMap != null)
		{
			return this._enMap;
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

		this._enMap = map;
		return this._enMap;
	}

	@Override
	protected boolean beforeUpdateInsertAction()
	{
		String sql = "UPDATE WF_GenerWorkFlow SET Domain='" + this.getDomain() + "' WHERE FK_FlowSort='" + this.No + "'";
		DBAccess.RunSQL(sql);

		//@sly
		sql = "UPDATE WF_Emp SET StartFlows='' ";
		DBAccess.RunSQL(sql);

		return super.beforeUpdateInsertAction();
	}
}