package BP.WF.Rpt;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.Port.*;
import BP.WF.*;
import BP.Web.WebUser;

import java.util.*;

/** 
 RptDept 的摘要说明。
*/
public class RptDept extends Entity
{
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


		///#region 基本属性
	/** 
	 报表ID
	 * @throws Exception 
	*/
	public final String getFK_Rpt() throws Exception
	{
		return this.GetValStringByKey(RptDeptAttr.FK_Rpt);
	}
	public final void setFK_Rpt(String value) throws Exception
	{
		SetValByKey(RptDeptAttr.FK_Rpt,value);
	}
	public final String getFK_DeptT() throws Exception
	{
		return this.GetValRefTextByKey(RptDeptAttr.FK_Dept);
	}
	/** 
	部门
	 * @throws Exception 
	*/
	public final String getFK_Dept() throws Exception
	{
		return this.GetValStringByKey(RptDeptAttr.FK_Dept);
	}
	public final void setFK_Dept(String value) throws Exception
	{
		SetValByKey(RptDeptAttr.FK_Dept,value);
	}

		///#endregion


		///#region 扩展属性


		///#endregion


		///#region 构造函数
	/** 
	 报表岗位
	*/
	public RptDept()
	{
	}
	/** 
	 报表部门对应
	 
	 @param _empoid 报表ID
	 @param wsNo 部门编号 	
	 * @throws Exception 
	*/
	public RptDept(String _empoid, String wsNo) throws Exception
	{
		this.setFK_Rpt(_empoid);
		this.setFK_Dept(wsNo);
		if (this.Retrieve() == 0)
		{
			this.Insert();
		}
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

		Map map = new Map("Sys_RptDept", "报表部门对应信息");
		map.setEnType(EnType.Dot2Dot);

		map.AddTBStringPK(RptDeptAttr.FK_Rpt, null, "报表", false, false, 1, 15, 1);
		map.AddDDLEntitiesPK(RptDeptAttr.FK_Dept,null,"部门",new Depts(),true);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}