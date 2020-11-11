package bp.wf.httphandler;

import bp.da.*;
import bp.difference.handler.WebContralBase;
import bp.sys.*;
import bp.web.*;
import bp.port.*;
import bp.en.*;
import bp.wf.*;
import bp.wf.Glo;
import bp.wf.template.*;
import bp.wf.*;

/** 
 页面功能实体
*/
public class WF_WorkOpt_Selecter extends WebContralBase
{
	/** 
	 构造函数
	*/
	public WF_WorkOpt_Selecter()
	{

	}

	public final String ByStation_ShowEmps() throws Exception
	{
		String staNo = this.GetRequestVal("StaNo");
		String sql = "";
		if (Glo.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			sql = "SELECT A.No, A.Name,A.FK_Dept FROM Port_Emp A, Port_DeptEmpStation B WHERE A.No=B.FK_Emp AND B.FK_Station='" + staNo + "'";
		}
		else
		{
			sql = "SELECT A.No, A.Name,A.FK_Dept FROM Port_Emp A, Port_DeptEmpStation B WHERE A.No=B.FK_Emp AND A.OrgNo='" + WebUser.getOrgNo() + "' AND B.FK_Station='" + staNo + "'";
		}

		DataTable db = DBAccess.RunSQLReturnTable(sql);
		return bp.tools.Json.ToJson(db);
		//return "方法未完成";
	}


		/// 界面 .
	public final String SelectEmpsByTeamStation_Init() throws Exception
	{
		String TeamNo = this.GetRequestVal("TeamNo");
		String sql = "";
		if (Glo.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			sql = "SELECT A.No, A.Name,A.FK_Dept FROM Port_Emp A, Port_TeamEmp B WHERE A.No=B.FK_Emp AND B.FK_Team='" + TeamNo + "'";
		}
		else
		{
			sql = "SELECT A.No, A.Name,A.FK_Dept FROM Port_Emp A, Port_TeamEmp B WHERE A.No=B.FK_Emp AND A.OrgNo='" + WebUser.getOrgNo() + "' AND B.FK_Team='" + TeamNo + "'";
		}

		DataTable db = DBAccess.RunSQLReturnTable(sql);
		return bp.tools.Json.ToJson(db);
	}

		/// 界面方法.
	public final String AddSelectEmp() throws Exception
	{
		//获得前台传来的参数
		String FK_Node = this.GetRequestVal("FK_Node");
		String WorkID = this.GetRequestVal("WorkID");
		String FK_Emp = this.GetRequestVal("FK_Emp");
		String EmpName = this.GetRequestVal("EmpName");
		String FK_Dept = this.GetRequestVal("FK_Dept");
		//得到部门名称
		Dept dept = new Dept(FK_Dept);
		String DeptName = dept.getName();
		SelectAccper selectAccper = new SelectAccper();
		selectAccper.setMyPK(FK_Node + "_" + WorkID + "_" + FK_Emp);
		if (selectAccper.RetrieveFromDBSources() == 0)
		{
			selectAccper.setFK_Node(Integer.parseInt(FK_Node));
			selectAccper.setWorkID(Long.parseLong(WorkID));
			selectAccper.setFK_Emp(FK_Emp);
			selectAccper.setEmpName(EmpName);
			selectAccper.setDeptName(DeptName);
			selectAccper.Insert();
			return "";
		}
		return "err@添加人员失败";
	}
	public final String DelSelectEmp() throws Exception
	{
		String MyPK = this.GetRequestVal("MyPK");
		SelectAccper selectAccper = new SelectAccper(MyPK);
		if (selectAccper.Delete() == 0)
		{
			return "err@删除失败";
		}
		return "删除成功";
	}
	/** 
	 关键字查询
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Selecter_SearchByKey() throws Exception
	{

		String key = this.GetRequestVal("Key"); //查询关键字.

		String ensOfM = this.GetRequestVal("EnsOfM"); //多的实体.
		Entities ensMen = ClassFactory.GetEns(ensOfM);
		QueryObject qo = new QueryObject(ensMen); //集合.
		qo.AddWhere("No", " LIKE ", "%" + key + "%");
		qo.addOr();
		qo.AddWhere("Name", " LIKE ", "%" + key + "%");
		qo.DoQuery();

		return ensMen.ToJson();
	}
}