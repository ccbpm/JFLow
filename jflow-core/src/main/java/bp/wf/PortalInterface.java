package bp.wf;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.port.*;
import bp.web.*;
import bp.sys.*;
import bp.wf.data.*;
import java.util.*;

/** 
 门户接口
*/
public class PortalInterface
{
	/** 
	 创建WorkID
	 
	 @param flowNo 流程编号
	 @param ht 表单参数，可以为null。
	 @param workDtls 明细表参数，可以为null。
	 @param nextWorker 操作员，如果为null就是当前人员。
	 @param title 创建工作时的标题，如果为null，就按设置的规则生成。
	 @return 为开始节点创建工作后产生的WorkID.
	 * @throws Exception 
	*/
	public static long Node_CreateBlankWork(String flowNo, Hashtable ht, DataSet workDtls, String guestNo, String title) throws Exception
	{
		return Node_CreateBlankWork(flowNo, ht, workDtls, guestNo, title, 0, null, 0, null);
	}
	/** 
	 创建WorkID
	 
	 @param flowNo 流程编号
	 @param ht 表单参数，可以为null。
	 @param workDtls 明细表参数，可以为null。
	 @param starter 流程的发起人
	 @param title 创建工作时的标题，如果为null，就按设置的规则生成。
	 @param parentWorkID 父流程的WorkID,如果没有父流程就传入为0.
	 @param parentFlowNo 父流程的流程编号,如果没有父流程就传入为null.
	 @return 为开始节点创建工作后产生的WorkID.
	 * @throws Exception 
	*/
	public static long Node_CreateBlankWork(String flowNo, Hashtable ht, DataSet workDtls, String guestNo, String title, long parentWorkID, String parentFlowNo, int parentNodeID, String parentEmp) throws Exception
	{
		//if (WebUser.getNo() != "Guest")
		//    throw new Exception("@必须是Guest登陆才能发起.");



		String dbstr = SystemConfig.getAppCenterDBVarStr();

		Flow fl = new Flow(flowNo);
		Node nd = new Node(fl.getStartNodeID());


		//把一些其他的参数也增加里面去,传递给ccflow.
		Hashtable htPara = new Hashtable();
		if (parentWorkID != 0)
		{
			htPara.put(StartFlowParaNameList.PWorkID, parentWorkID);
		}
		if (parentFlowNo != null)
		{
			htPara.put(StartFlowParaNameList.PFlowNo, parentFlowNo);
		}
		if (parentNodeID != 0)
		{
			htPara.put(StartFlowParaNameList.PNodeID, parentNodeID);
		}
		if (parentEmp != null)
		{
			htPara.put(StartFlowParaNameList.PEmp, parentEmp);
		}



		Emp empStarter = new Emp(WebUser.getNo());
		Work wk = fl.NewWork(empStarter, htPara);
		long workID = wk.getOID();


			///给各个属性-赋值
		if (ht != null)
		{
			for (Object str : ht.keySet())
			{
				if(str !=null)
					continue;
				wk.SetValByKey(str.toString(), ht.get(str));
			}
		}
		wk.setOID(workID);
		if (workDtls != null)
		{
			//保存从表
			for (DataTable dt : workDtls.Tables)
			{
				for (MapDtl dtl : wk.getHisMapDtls().ToJavaList())
				{
					if (!dt.TableName.equals(dtl.getNo()))
					{
						continue;
					}
					//获取dtls
					GEDtls daDtls = new GEDtls(dtl.getNo());
					daDtls.Delete(GEDtlAttr.RefPK, wk.getOID()); // 清除现有的数据.

					GEDtl daDtl = daDtls.getGetNewEntity() instanceof GEDtl ? (GEDtl)daDtls.getGetNewEntity() : null;
					daDtl.setRefPK(String.valueOf(wk.getOID()));

					// 为从表复制数据.
					for (DataRow dr : dt.Rows)
					{
						daDtl.ResetDefaultVal();
						daDtl.setRefPK(String.valueOf(wk.getOID()));

						//明细列.
						for (DataColumn dc : dt.Columns)
						{
							//设置属性.
							daDtl.SetValByKey(dc.ColumnName, dr.getValue(dc.ColumnName));
						}
						daDtl.InsertAsOID(DBAccess.GenerOID("Dtl")); //插入数据.
					}
				}
			}
		}

			/// 赋值

		Paras ps = new Paras();
		// 执行对报表的数据表WFState状态的更新,让它为runing的状态.
		if (DataType.IsNullOrEmpty(title) == false)
		{
			ps = new Paras();
			ps.SQL="UPDATE " + fl.getPTable() + " SET WFState=" + dbstr + "WFState,Title=" + dbstr + "Title WHERE OID=" + dbstr + "OID";
			ps.Add(GERptAttr.WFState, WFState.Blank.getValue());
			ps.Add(GERptAttr.Title, title);
			ps.Add(GERptAttr.OID, wk.getOID());
			DBAccess.RunSQL(ps);
		}
		else
		{
			ps = new Paras();
			ps.SQL="UPDATE " + fl.getPTable() + " SET WFState=" + dbstr + "WFState,FK_Dept=" + dbstr + "FK_Dept,Title=" + dbstr + "Title WHERE OID=" + dbstr + "OID";
			ps.Add(GERptAttr.WFState, WFState.Blank.getValue());
			ps.Add(GERptAttr.FK_Dept, empStarter.getFK_Dept());
			ps.Add(GERptAttr.Title, bp.wf.WorkFlowBuessRole.GenerTitle(fl, wk));
			ps.Add(GERptAttr.OID, wk.getOID());
			DBAccess.RunSQL(ps);
		}

		// 删除有可能产生的垃圾数据,比如上一次没有发送成功，导致数据没有清除.
		ps = new Paras();
		ps.SQL="DELETE FROM WF_GenerWorkFlow  WHERE WorkID=" + dbstr + "WorkID1 OR FID=" + dbstr + "WorkID2";
		ps.Add("WorkID1", wk.getOID());
		ps.Add("WorkID2", wk.getOID());
		DBAccess.RunSQL(ps);

		ps = new Paras();
		ps.SQL="DELETE FROM WF_GenerWorkerList  WHERE WorkID=" + dbstr + "WorkID1 OR FID=" + dbstr + "WorkID2";
		ps.Add("WorkID1", wk.getOID());
		ps.Add("WorkID2", wk.getOID());
		DBAccess.RunSQL(ps);

		// 设置流程信息
		if (parentWorkID != 0)
		{
			bp.wf.Dev2Interface.SetParentInfo(flowNo, workID, parentWorkID);
		}

		return wk.getOID();
	}


		///门户。
	/** 
	 登陆
	 
	 @param guestNo 客户编号
	 @param guestName 客户名称
	 * @throws Exception 
	*/
	public static void Port_Login(String guestNo, String guestName) throws Exception
	{
		//登陆.
		GuestUser.SignInOfGener(guestNo, guestName, "CH", true);
	}
	/** 
	 登陆
	 
	 @param guestNo 客户编号
	 @param guestName 客户名称
	 @param deptNo 客户的部门编号
	 @param deptName 客户的部门名称
	 * @throws Exception 
	*/
	public static void Port_Login(String guestNo, String guestName, String deptNo, String deptName) throws Exception
	{
		//登陆.
		GuestUser.SignInOfGener(guestNo, guestName, deptNo,deptName,"CH", true);
	}
	/** 
	 退出登陆.
	*/
	public static void Port_LoginOunt()
	{
		//登陆.
		GuestUser.Exit();
	}

		/// 门户。



		///获取Guest的待办
	/** 
	 获取Guest的待办
	 
	 @param fk_flow 流程编号,流程编号为空表示所有的流程.
	 @param guestNo 客户编号
	 @return 结果集合
	*/
	public static DataTable DB_GenerEmpWorksOfDataTable(String fk_flow, String guestNo)
	{


		Paras ps = new Paras();
		String dbstr = SystemConfig.getAppCenterDBVarStr();
		String sql;

		/*不是授权状态*/
		if (DataType.IsNullOrEmpty(fk_flow))
		{
			ps.SQL="SELECT * FROM WF_EmpWorks WHERE GuestNo=" + dbstr + "GuestNo AND FK_Emp='Guest' ORDER BY FK_Flow,ADT DESC ";
			ps.Add("GuestNo", guestNo);
		}
		else
		{
			ps.SQL="SELECT * FROM WF_EmpWorks WHERE GuestNo=" + dbstr + "GuestNo AND FK_Emp='Guest' AND FK_Flow=" + dbstr + "FK_Flow ORDER BY  ADT DESC ";
			ps.Add("FK_Flow", fk_flow);
			ps.Add("GuestNo", guestNo);
		}
		return DBAccess.RunSQLReturnTable(ps);
	}
	/** 
	 获取未完成的流程(也称为在途流程:我参与的但是此流程未完成)
	 
	 @param fk_flow 流程编号
	 @return 返回从数据视图WF_GenerWorkflow查询出来的数据.
	 * @throws Exception 
	*/
	public static DataTable DB_GenerRuning(String fk_flow, String guestNo) throws Exception
	{

		String sql;
		int state = WFState.Runing.getValue();

		if (DataType.IsNullOrEmpty(fk_flow))
		{
			sql = "SELECT a.WorkID FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND B.FK_Emp='" + WebUser.getNo() + "' AND B.IsEnable=1 AND B.IsPass=1 AND A.GuestNo='" + guestNo + "' ";
		}
		else
		{
			sql = "SELECT a.WorkID FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.FK_Flow='" + fk_flow + "'  AND A.WorkID=B.WorkID AND B.FK_Emp='" + WebUser.getNo() + "' AND B.IsEnable=1 AND B.IsPass=1  AND A.GuestNo='" + guestNo + "'";
		}

		GenerWorkFlows gwfs = new GenerWorkFlows();
		gwfs.RetrieveInSQL(GenerWorkFlowAttr.WorkID, "(" + sql + ")");
		return gwfs.ToDataTableField();
	}

		///


		///功能
	/** 
	 设置用户信息
	 
	 @param flowNo 流程编号
	 @param workID 工作ID
	 @param guestNo 客户编号
	 @param guestName 客户名称
	 * @throws Exception 
	*/
	public static void SetGuestInfo(String flowNo, long workID, String guestNo, String guestName) throws Exception
	{
		String dbstr = SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL="UPDATE WF_GenerWorkFlow SET GuestNo=" + dbstr + "GuestNo, GuestName=" + dbstr + "GuestName WHERE WorkID=" + dbstr + "WorkID";
		ps.Add("GuestNo", guestNo);
		ps.Add("GuestName", guestName);
		ps.Add("WorkID", workID);
		DBAccess.RunSQL(ps);

		Flow fl = new Flow(flowNo);
		ps = new Paras();
		ps.SQL="UPDATE " + fl.getPTable() + " SET GuestNo=" + dbstr + "GuestNo, GuestName=" + dbstr + "GuestName WHERE OID=" + dbstr + "OID";
		ps.Add("GuestNo", guestNo);
		ps.Add("GuestName", guestName);
		ps.Add("OID", workID);
		DBAccess.RunSQL(ps);
	}
	/** 
	 设置当前用户的待办
	 
	 @param workID 工作ID
	 @param guestNo 客户编号
	 @param guestName 客户名称
	*/
	public static void SetGuestToDoList(long workID, String guestNo, String guestName)
	{
		if (guestNo.equals(""))
		{
			throw new RuntimeException("@设置外部用户待办信息失败:参数guestNo不能为空.");
		}
		if (workID == 0)
		{
			throw new RuntimeException("@设置外部用户待办信息失败:参数workID不能为0.");
		}

		String dbstr = SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL="UPDATE WF_GenerWorkerList SET GuestNo=" + dbstr + "GuestNo, GuestName=" + dbstr + "GuestName WHERE WorkID=" + dbstr + "WorkID AND IsPass=0";
		ps.Add("GuestNo", guestNo);
		ps.Add("GuestName", guestName);
		ps.Add("WorkID", workID);
		int i = DBAccess.RunSQL(ps);
		if (i == 0)
		{
			throw new RuntimeException("@设置外部用户待办信息失败:参数workID不能为空.");
		}

		ps = new Paras();
		ps.SQL="UPDATE WF_GenerWorkFlow SET GuestNo=" + dbstr + "GuestNo, GuestName=" + dbstr + "GuestName WHERE WorkID=" + dbstr + "WorkID ";
		ps.Add("GuestNo", guestNo);
		ps.Add("GuestName", guestName);
		ps.Add("WorkID", workID);
		i = DBAccess.RunSQL(ps);
		if (i == 0)
		{
			throw new RuntimeException("@WF_GenerWorkFlow - 设置外部用户待办信息失败:参数WorkID不能为空.");
		}
	}

		///



}