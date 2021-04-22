package bp.ccbill;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.difference.handler.WebContralBase;
import bp.sys.*;
import bp.tools.StringHelper;
import bp.web.*;
import bp.ccbill.template.*;
import java.util.*;

/** 
 页面功能实体
*/
public class WF_CCBill_API extends WebContralBase
{

		///构造方法.
	/** 
	 构造函数
	*/
	public WF_CCBill_API()
	{
	}

		/// 构造方法.


		///常用参数.
	/** 
	 目录树编号
	*/
	public final String getTreeNo()
	{
		return this.GetRequestVal("TreeNo");
	}

		/// 常用参数.



		///前台的操作 api
	/** 
	 获得可以操作的单据列表
	 
	 @return 
	 * @throws Exception 
	*/
	public final String CCFrom_GenerFrmListOfCanOption() throws Exception
	{
		String sql = "";
		String userNo = GetRequestVal("UserNo");
		if (DataType.IsNullOrEmpty(userNo) == true)
		{
			userNo = WebUser.getNo();
		}

		String powerSQL = "";

		if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
		{
			powerSQL = "SELECT FrmID," + "(CASE WHEN IsEnableAll=1 THEN true " + "ELSE(CASE WHEN IsEnableUser=1 AND INSTR(IDOfUsers,'," + userNo + ",')>0 THEN true " + "ELSE(CASE WHEN IsEnableStation=1 AND (SELECT COUNT(*) From Port_DeptEmpStation D,Port_Emp E WHERE D.FK_Emp = E.No AND E.No='" + userNo + "' AND INSTR(IDOfStations,D.FK_Station))>0 THEN true " + "ELSE(CASE WHEN IsEnableDept=1 AND (SELECT COUNT(*) From Port_DeptEmp D,Port_Emp E WHERE D.FK_Emp = E.No AND E.No='" + userNo + "' AND INSTR(IDOfDepts,D.FK_Dept))>0 THEN true " + "ELSE false END)" + "END)" + "END)" + "END) AS IsView   FROM Frm_CtrlModel WHERE CtrlObj='BtnSearch'";

			sql = "SELECT No,Name,EntityType,FrmType,PTable FROM Sys_MapData M, (" + powerSQL + ") AS B WHERE M.No=B.FrmID AND (M.EntityType=1 OR M.EntityType=2) AND B.IsView=1 ORDER BY M.IDX ";

		}
		else
		{
			powerSQL = "SELECT FrmID," + "(CASE WHEN IsEnableAll=1 THEN true " + "ELSE(CASE WHEN IsEnableUser=1 AND 1=1 THEN true " + "ELSE(CASE WHEN IsEnableStation=1 AND (SELECT COUNT(*) From Port_DeptEmpStation D,Port_Emp E WHERE D.FK_Emp = E.No AND E.No='" + userNo + "' AND  1=1 THEN true " + "ELSE(CASE WHEN IsEnableDept=1 AND (SELECT COUNT(*) From Port_DeptEmp D,Port_Emp E WHERE D.FK_Emp = E.No AND E.No='" + userNo + "' AND  1=1 THEN true " + "ELSE false END)" + "END)" + "END)" + "END) AS IsView   FROM Frm_CtrlModel WHERE CtrlObj='BtnSearch'";

			sql = "SELECT No,Name,EntityType,FrmType,PTable FROM Sys_MapData M  WHERE  (M.EntityType=1 OR M.EntityType=2)   ORDER BY M.IDX ";

		}

		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle 
				|| SystemConfig.getAppCenterDBType() == DBType.KingBase)
		{
			dt.Columns.get(0).setColumnName("No");
			dt.Columns.get(1).setColumnName("Name");
			dt.Columns.get(2).setColumnName("EntityType");
			dt.Columns.get(3).setColumnName("FrmType");
			dt.Columns.get(4).setColumnName("PTable");
		}

		return bp.tools.Json.ToJson(dt);
	}
	/** 
	 根据单据编号创建或者更新实体信息.
	 
	 @return 返回url：打开该实体的url.
	 * @throws Exception 
	*/
	public final String CCFrom_NewFrmBillAsSpecBillNo() throws Exception
	{

		String billNo = this.GetRequestVal("BillNo");
		String title = this.GetRequestVal("Title");
		String paras = this.GetRequestVal("Paras");

		if (DataType.IsNullOrEmpty(paras) == true)
		{
			paras = "";
		}
		AtPara ap = new AtPara(paras);

		GEEntity en = new GEEntity(this.getFrmID());
		int i = en.Retrieve("BillNo", billNo);
		if (i == 0)
		{
			long workid = bp.ccbill.Dev2Interface.CreateBlankBillID(this.getFrmID(), WebUser.getNo(), ap.getHisHT(), billNo);
			en = new GEEntity(this.getFrmID(), workid);
			if (DataType.IsNullOrEmpty(paras) == false)
			{
				en.Copy(ap.getHisHT());
				en.Update();
			}
			if (DataType.IsNullOrEmpty(title) == false)
			{
				en.SetValByKey("Title", title);
				en.Update();
			}
			return "url@../../WF/CCBill/MyBill.htm?FrmID=" + this.getFrmID() + "&OID=" + workid;
		}
		else
		{
			if (DataType.IsNullOrEmpty(paras) == false)
			{
				en.Copy(ap.getHisHT());
				en.Update();
			}

			if (DataType.IsNullOrEmpty(title) == false && en.GetValStrByKey("Title").equals(title) == false)
			{
				en.SetValByKey("Title", title);
				en.Update();
			}
		}
		return "url@../../WF/CCBill/MyBill.htm?FrmID=" + this.getFrmID() + "&OID=" + en.getOID();
	}
	/** 
	 获得指定的目录下可以操作的单据列表
	 
	 @return 
	*/
	public final String CCFrom_GenerFrmListOfCanOptionBySpecTreeNo()
	{

		String treeNo = this.GetRequestVal("TreeNo");
		return null;
	}
	/** 
	 获得一个表单的操作权限
	 
	 @return 
	 * @throws Exception 
	*/
	public final String CCFrom_FrmPower() throws Exception
	{
		Hashtable ht = new Hashtable();
		String frmID = this.getFrmID();
		CtrlModels ctrlMs = new CtrlModels();

		ctrlMs.Retrieve(CtrlModelAttr.FrmID, frmID);
		String userNo = GetRequestVal("UserNo");
		if (DataType.IsNullOrEmpty(userNo) == true)
		{
			userNo = WebUser.getNo();
		}
		for (CtrlModel ctrlM : ctrlMs.ToJavaList())
		{
			int isTrue = 0;
			if (ctrlM.getIsEnableAll() == true)
			{
				isTrue = 1;
			}
			else
			{
				//根据设置的权限来判断
				if (ctrlM.getIsEnableStation() == true)
				{
					String stations = ctrlM.getIDOfStations();
					stations = StringHelper.trim(stations, ',');
					stations = stations.replace(",", "','");
					stations = "'" + stations + "'";
					String sql = "SELECT * From Port_DeptEmpStation DES,Port_Emp E WHERE  E.setNo(DES.FK_Emp AND E.No='" + userNo + "' AND DES.FK_Station IN(" + stations + ")";
					if (DBAccess.RunSQLReturnCOUNT(sql) > 1)
					{
						isTrue = 1;
					}
				}

				if (ctrlM.getIsEnableUser() == true && isTrue == 0)
				{
					String emps = ctrlM.getIDOfUsers();
					if (emps.contains("," + userNo + ",") == true)
					{
						isTrue = 1;
					}
				}

				if (ctrlM.getIsEnableDept() == true && isTrue == 0)
				{
					String depts = ctrlM.getIDOfDepts();
					depts = StringHelper.trim(depts, ',');
					depts = depts.replace(",", "','");
					depts = "'" + depts + "'";
					String sql = "SELECT * From Port_DeptEmp D,Port_Emp E WHERE  E.setNo(D.FK_Emp AND E.No='" + userNo + "' AND D.FK_Dept IN(" + depts + ")";
					if (DBAccess.RunSQLReturnCOUNT(sql) > 1)
					{
						isTrue = 1;
					}
				}

			}

			if (ctrlM.getCtrlObj().equals("BtnNew") == true)
			{
				ht.put("IsInsert", isTrue);
			}
			if (ctrlM.getCtrlObj().equals("BtnSave") == true)
			{
				ht.put("IsSave", isTrue);
			}
			if (ctrlM.getCtrlObj().equals("BtnSubmit") == true)
			{
				ht.put("IsSubmit", isTrue);
			}
			if (ctrlM.getCtrlObj().equals("BtnSearch") == true)
			{
				ht.put("IsView", isTrue);
			}
			if (ctrlM.getCtrlObj().equals("BtnDelete") == true)
			{
				ht.put("IsDelete", isTrue);
			}
		}

		return bp.tools.Json.ToJson(ht);
	}

	/** 
	 获取菜单列表
	 
	 @return 
	 * @throws Exception 
	*/
	public final String CCForm_Power_ViewList() throws Exception
	{
		String userNo = GetRequestVal("UserNo");
		if (DataType.IsNullOrEmpty(userNo) == true)
		{
			userNo = WebUser.getNo();
		}
		String sql = "SELECT FrmID," + "(CASE WHEN IsEnableAll=1 THEN true " + "ELSE(CASE WHEN IsEnableUser=1 AND INSTR(IDOfUsers,'," + userNo + ",')>0 THEN true " + "ELSE(CASE WHEN IsEnableStation=1 AND (SELECT COUNT(*) From Port_DeptEmpStation D,Port_Emp E WHERE D.FK_Emp = E.No AND E.No='" + userNo + "' AND INSTR(IDOfStations,D.FK_Station))>0 THEN true " + "ELSE(CASE WHEN IsEnableDept=1 AND (SELECT COUNT(*) From Port_DeptEmp D,Port_Emp E WHERE D.FK_Emp = E.No AND E.No='" + userNo + "' AND INSTR(IDOfDepts,D.FK_Dept))>0 THEN true " + "ELSE false END)" + "END)" + "END)" + "END) AS IsView   FROM Frm_CtrlModel WHERE CtrlObj='BtnSearch'";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "FrmView";
		return bp.tools.Json.ToJson(dt);
	}
	/** 
	 删除实体根据BillNo
	 
	 @return 
	 * @throws Exception 
	*/
	public final String CCFrom_DeleteFrmEntityByBillNo() throws Exception
	{

		GEEntity en = new GEEntity(this.getFrmID());
		int i = en.Retrieve("BillNo", this.GetRequestVal("BillNo"));
		if (i == 0)
		{
			return "err@单据编号为" + this.GetRequestVal("BillNo") + "的数据不存在.";
		}

		en.Delete();
		return "删除成功";
	}
	/** 
	 删除实体根据 OID
	 
	 @return 
	 * @throws Exception 
	*/
	public final String CCFrom_DeleteFrmEntityByOID() throws Exception
	{
		GEEntity en = new GEEntity(this.getFrmID(), this.getOID());
		en.Delete();
		return "删除成功";
	}

		/// 前台的操作 api.



		///后台操作api.
	/** 
	 获得所有的单据、表单 @lizhen 转移代码.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String CCBillAdmin_Admin_GenerAllBills() throws Exception
	{
		String sql = "";
		sql = "SELECT No,Name,EntityType,FrmType,PTable FROM Sys_MapData WHERE (EntityType=1 OR EntityType=2) ORDER BY IDX ";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle 
				|| SystemConfig.getAppCenterDBType() == DBType.KingBase)
		{
			dt.Columns.get(0).setColumnName("No");
			dt.Columns.get(1).setColumnName("Name");
			dt.Columns.get(2).setColumnName("EntityType");
			dt.Columns.get(3).setColumnName("FrmType");
			dt.Columns.get(4).setColumnName("PTable");
		}

		return bp.tools.Json.ToJson(dt);
	}

		///

}