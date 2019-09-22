package BP.WF.Port;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import java.util.*;

/** 
 流程部门数据查询权限 的摘要说明。
*/
public class DeptFlowSearch extends EntityMyPK
{
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		if (BP.Web.WebUser.No.equals("admin"))
		{
			uac.IsView = true;
			uac.IsDelete = true;
			uac.IsInsert = true;
			uac.IsUpdate = true;
			uac.IsAdjunct = true;
		}
		return uac;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
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
		if (this._enMap != null)
		{
			return this._enMap;
		}

		Map map = new Map("WF_DeptFlowSearch", "流程部门数据查询权限");
		map.AddMyPK();
		map.AddTBString(DeptFlowSearchAttr.FK_Emp, null, "操作员", true, true, 1, 50, 11);
		map.AddTBString(DeptFlowSearchAttr.FK_Flow, null, "流程编号", true, true, 1, 50, 11);
		map.AddTBString(DeptFlowSearchAttr.FK_Dept, null, "部门编号", true, true, 1, 100, 11);
		this._enMap = map;
		return this._enMap;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

}