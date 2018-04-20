package BP.WF.Port;

import BP.En.EntityMyPK;
import BP.En.Map;
import BP.En.UAC;

/** 
 流程部门数据查询权限 的摘要说明。
 
*/
public class DeptFlowSearch extends EntityMyPK
{
	/** 
	 UI界面上的访问控制
	 * @throws Exception 
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		if (BP.Web.WebUser.getNo().equals("admin"))
		{
			uac.IsView = true;
			uac.IsDelete = true;
			uac.IsInsert = true;
			uac.IsUpdate = true;
			uac.IsAdjunct = true;
		}
		return uac;
	}


		
	/** 
	 工作人员ID
	*/
	public final String getFK_Emp()
	{
		return this.GetValStringByKey(DeptFlowSearchAttr.FK_Emp);
	}
	public final void setFK_Emp(String value)
	{
		SetValByKey(DeptFlowSearchAttr.FK_Emp, value);
	}
	/** 
	部门
	*/
	public final String getFK_Dept()
	{
		return this.GetValStringByKey(DeptFlowSearchAttr.FK_Dept);
	}
	public final void setFK_Dept(String value)
	{
		SetValByKey(DeptFlowSearchAttr.FK_Dept, value);
	}
	/** 
	 流程编号
	*/
	public final String getFK_Flow()
	{
		return this.GetValStringByKey(DeptFlowSearchAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)
	{
		this.SetValByKey(DeptFlowSearchAttr.FK_Flow, value);
	}
	/** 
	 流程部门数据查询权限
	*/
	public DeptFlowSearch()
	{
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_DeptFlowSearch", "流程部门数据查询权限");
		map.AddMyPK();
		map.AddTBString(DeptFlowSearchAttr.FK_Emp, null, "操作员", true, true, 1, 50, 11);
		map.AddTBString(DeptFlowSearchAttr.FK_Flow, null, "流程编号", true, true, 1, 50, 11);
		map.AddTBString(DeptFlowSearchAttr.FK_Dept, null, "部门编号", true, true, 1, 100, 11);
		this.set_enMap(map);
		return this.get_enMap();
	}
}