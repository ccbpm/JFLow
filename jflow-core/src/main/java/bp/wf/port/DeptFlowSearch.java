package bp.wf.port;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.web.WebUser;
import bp.wf.*;
import java.util.*;

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
		if (WebUser.getNo().equals("admin"))
		{
			uac.IsView = true;
			uac.IsDelete = true;
			uac.IsInsert = true;
			uac.IsUpdate = true;
			uac.IsAdjunct = true;
		}
		return uac;
	}


		///基本属性
	/** 
	 工作人员ID
	 * @throws Exception 
	*/
	public final String getFK_Emp() throws Exception
	{
		return this.GetValStringByKey(DeptFlowSearchAttr.FK_Emp);
	}
	public final void setFK_Emp(String value) throws Exception
	{
		SetValByKey(DeptFlowSearchAttr.FK_Emp, value);
	}
	/** 
	部门
	*/
	public final String getFK_Dept()throws Exception
	{
		return this.GetValStringByKey(DeptFlowSearchAttr.FK_Dept);
	}
	public final void setFK_Dept(String value) throws Exception
	{
		SetValByKey(DeptFlowSearchAttr.FK_Dept, value);
	}
	/** 
	 流程编号
	*/
	public final String getFK_Flow()throws Exception
	{
		return this.GetValStringByKey(DeptFlowSearchAttr.FK_Flow);
	}
	public final void setFK_Flow(String value) throws Exception
	{
		this.SetValByKey(DeptFlowSearchAttr.FK_Flow, value);
	}

		///


		///构造函数
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
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_DeptFlowSearch", "流程部门数据查询权限");
		map.AddMyPK();
		map.AddTBString(DeptFlowSearchAttr.FK_Emp, null, "操作员", true, true, 1, 50, 11);
		map.AddTBString(DeptFlowSearchAttr.FK_Flow, null, "流程编号", true, true, 1, 4, 11);
		map.AddTBString(DeptFlowSearchAttr.FK_Dept, null, "部门编号", true, true, 1, 100, 11);
		this.set_enMap(map);
		return this.get_enMap();
	}

		///

}