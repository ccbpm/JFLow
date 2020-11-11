package bp.ccbill.template;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.sys.*;
import bp.wf.template.*;
import bp.wf.*;
import bp.port.*;
import bp.ccbill.*;
import java.util.*;

/** 
 控制模型
*/
public class CtrlModel extends EntityMyPK
{

		///基本属性.
	/** 
	 表单ID
	 * @throws Exception 
	*/
	public final String getFrmID() throws Exception
	{
		return this.GetValStringByKey(CtrlModelAttr.FrmID);
	}
	public final void setFrmID(String value) throws Exception
	{
		SetValByKey(CtrlModelAttr.FrmID, value);
	}

	/** 
	 控制权限
	 * @throws Exception 
	*/
	public final String getCtrlObj() throws Exception
	{
		return this.GetValStringByKey(CtrlModelAttr.CtrlObj);
	}
	public final void setCtrlObj(String value) throws Exception
	{
		SetValByKey(CtrlModelAttr.CtrlObj, value);
	}

	public final String getIDOfUsers() throws Exception
	{
		return this.GetValStringByKey(CtrlModelAttr.IDOfUsers);
	}
	public final void setIDOfUsers(String value) throws Exception
	{
		SetValByKey(CtrlModelAttr.IDOfUsers, value);
	}


	public final String getIDOfStations() throws Exception
	{
		return this.GetValStringByKey(CtrlModelAttr.IDOfStations);
	}
	public final void setIDOfStations(String value) throws Exception
	{
		SetValByKey(CtrlModelAttr.IDOfStations, value);
	}


	public final String getIDOfDepts() throws Exception
	{
		return this.GetValStringByKey(CtrlModelAttr.IDOfDepts);
	}
	public final void setIDOfDepts(String value) throws Exception
	{
		SetValByKey(CtrlModelAttr.IDOfDepts, value);
	}

	public final boolean getIsEnableAll() throws Exception
	{
		return this.GetValBooleanByKey(CtrlModelAttr.IsEnableAll);
	}
	public final void setIsEnableAll(boolean value) throws Exception
	{
		SetValByKey(CtrlModelAttr.IsEnableAll, value);
	}

	public final boolean getIsEnableStation() throws Exception
	{
		return this.GetValBooleanByKey(CtrlModelAttr.IsEnableStation);
	}
	public final void setIsEnableStation(boolean value) throws Exception
	{
		SetValByKey(CtrlModelAttr.IsEnableStation, value);
	}

	public final boolean getIsEnableDept() throws Exception
	{
		return this.GetValBooleanByKey(CtrlModelAttr.IsEnableDept);
	}
	public final void setIsEnableDept(boolean value) throws Exception
	{
		SetValByKey(CtrlModelAttr.IsEnableDept, value);
	}

	public final boolean getIsEnableUser() throws Exception
	{
		return this.GetValBooleanByKey(CtrlModelAttr.IsEnableUser);
	}
	public final void setIsEnableUser(boolean value) throws Exception
	{
		SetValByKey(CtrlModelAttr.IsEnableUser, value);
	}

		/// 基本属性.


		///字段属性.

		/// attrs


		///构造.
	public String RptName = null;
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Frm_CtrlModel", "控制模型表");


			///字段
		map.AddMyPK(); //增加一个自动增长的列.


		map.AddTBString(CtrlModelAttr.FrmID, null, "表单ID", true, false, 0, 300, 100);
			//BtnNew,BtnSave,BtnSubmit,BtnDelete,BtnSearch
		map.AddTBString(CtrlModelAttr.CtrlObj, null, "控制权限", true, false, 0, 20, 100);
		map.AddTBInt(CtrlModelAttr.IsEnableAll, 0, "任何人都可以", true, false);
		map.AddTBInt(CtrlModelAttr.IsEnableStation, 0, "按照岗位计算", true, false);
		map.AddTBInt(CtrlModelAttr.IsEnableDept, 0, "按照绑定的部门计算", true, false);
		map.AddTBInt(CtrlModelAttr.IsEnableUser, 0, "按照绑定的人员计算", true, false);
		map.AddTBInt(CtrlModelAttr.IsEnableMyDept, 1, "按照本部门计算", false, false);
		map.AddTBString(CtrlModelAttr.IDOfUsers, null, "绑定的人员ID", true, false, 0, 1000, 300);
		map.AddTBString(CtrlModelAttr.IDOfStations, null, "绑定的岗位ID", true, false, 0, 1000, 300);
		map.AddTBString(CtrlModelAttr.IDOfDepts, null, "绑定的部门ID", true, false, 0, 1000, 300);

			/// 字段

			//权限设置绑定岗位. 使用树杆与叶子的模式绑定.
		map.getAttrsOfOneVSM().AddBranchesAndLeaf(new bp.ccbill.template.CtrlModelDtls(), new bp.port.Emps(), CtrlModelDtlAttr.FrmID, CtrlModelDtlAttr.IDs, "权限按照绑定的岗位", "FK_StationType", EmpAttr.Name, EmpAttr.No);

			//权限设置绑定人员. 使用树杆与叶子的模式绑定.
		map.getAttrsOfOneVSM().AddBranchesAndLeaf(new bp.ccbill.template.CtrlModelDtls(), new bp.port.Emps(), CtrlModelDtlAttr.FrmID, CtrlModelDtlAttr.IDs, "权限按照绑定的人员", EmpAttr.FK_Dept, EmpAttr.Name, EmpAttr.No, "@WebUser.FK_Dept");

		this.set_enMap(map);
		return this.get_enMap();
	}
	/** 
	 控制模型
	*/
	public CtrlModel()
	{
	}
	/** 
	 增加授权人
	 
	 @return 
	 * @throws Exception 
	*/
	@Override
	protected boolean beforeInsert() throws Exception
	{
		this.setMyPK(this.getFrmID() + "_" + getCtrlObj());
		return super.beforeInsert();
	}


		/// 构造.
}