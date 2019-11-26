package BP.Difference.Handler;

import java.util.Hashtable;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import BP.DA.DBAccess;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.DA.Paras;
import BP.Difference.SystemConfig;
import BP.Port.Emp;
import BP.Sys.GEDtl;
import BP.Sys.GEDtlAttr;
import BP.Sys.GEDtls;
import BP.Sys.MapDtl;
import BP.WF.Flow;
import BP.WF.GenerWorkFlowAttr;
import BP.WF.GenerWorkFlows;
import BP.WF.Node;
import BP.WF.StartFlowParaNameList;
import BP.WF.WFState;
import BP.WF.Work;
import BP.WF.Data.GERptAttr;
import BP.Web.WebUser;

public class PortalInterface {

	/**
	 * 发送消息
	 * 
	 * @param sender
	 * @param sendToEmpNo
	 * @param title
	 * @param url
	 * @param msgType
	 * @return
	 * @throws Exception
	 */
	public boolean SendToWebServices(String mypk, String sender, String sendToEmpNo, String tel, String title,
			String msgInfo, String OpenUrl, String msgType) throws Exception {
		return HttpRequest(mypk, sender, sendToEmpNo, tel, title, msgInfo, OpenUrl, msgType, "SendToWebServices");

	}

	/**
	 * 站内消息
	 * 
	 * @param sender
	 * @param sendToEmpNo
	 * @param title
	 * @param url
	 * @param msgType
	 * @return
	 * @throws Exception
	 */
	public boolean SendToCCMSG(String mypk, String sender, String sendToEmpNo, String tel, String title, String msgInfo,
			String OpenUrl, String msgType) throws Exception {
		return HttpRequest(mypk, sender, sendToEmpNo, tel, title, msgInfo, OpenUrl, msgType, "SendToCCMSG");

	}

	/**
	 * 发送到钉钉
	 * 
	 * @param sender
	 * @param sendToEmpNo
	 * @param title
	 * @param msgInfo
	 * @param OpenUrl
	 * @param msgType
	 * @return
	 * @throws Exception
	 */
	public boolean SendToDingDing(String mypk, String sender, String sendToEmpNo, String tel, String title,
			String msgInfo, String OpenUrl, String msgType) throws Exception {
		return HttpRequest(mypk, sender, sendToEmpNo, tel, title, msgInfo, OpenUrl, msgType, "SendToDingDing");
	}

	/**
	 * 发送到微信
	 * 
	 * @param sender
	 * @param sendToEmpNo
	 * @param title
	 * @param msgInfo
	 * @param OpenUrl
	 * @param msgType
	 * @return
	 * @throws Exception
	 */
	public boolean SendToWeiXin(String mypk, String sender, String sendToEmpNo, String tel, String title,
			String msgInfo, String OpenUrl, String msgType) throws Exception {
		return HttpRequest(mypk, sender, sendToEmpNo, tel, title, msgInfo, OpenUrl, msgType, "SendToWeiXin");
	}

	/**
	 * 发送到即时通
	 * 
	 * @param sender
	 * @param sendToEmpNo
	 * @param title
	 * @param msgInfo
	 * @param OpenUrl
	 * @param msgType
	 * @return
	 * @throws Exception
	 */
	public boolean SendToCCIM(String mypk, String sender, String sendToEmpNo, String tel, String title, String msgInfo,
			String OpenUrl, String msgType) throws Exception {
		return HttpRequest(mypk, sender, sendToEmpNo, tel, title, msgInfo, OpenUrl, msgType, "SendToCCIM");
	}

	/**
	 * 人員登陸
	 * 
	 * @param userNo
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public boolean CheckUserNoPassWord(String userNo, String password) throws Exception {
		return HttpRequestPost(userNo, password, "CheckUserNoPassWord");
	}

	/**
	 * 创建WorkID
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param ht
	 *            表单参数，可以为null。
	 * @param workDtls
	 *            明细表参数，可以为null。
	 * @param nextWorker
	 *            操作员，如果为null就是当前人员。
	 * @param title
	 *            创建工作时的标题，如果为null，就按设置的规则生成。
	 * @return 为开始节点创建工作后产生的WorkID.
	 * @throws Exception
	 */
	public static long Node_CreateBlankWork(String flowNo, Hashtable ht, DataSet workDtls, String guestNo, String title)
			throws Exception {
		return Node_CreateBlankWork(flowNo, ht, workDtls, guestNo, title, 0, null, 0, null);
	}

	/**
	 * 创建WorkID
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param ht
	 *            表单参数，可以为null。
	 * @param workDtls
	 *            明细表参数，可以为null。
	 * @param starter
	 *            流程的发起人
	 * @param title
	 *            创建工作时的标题，如果为null，就按设置的规则生成。
	 * @param parentWorkID
	 *            父流程的WorkID,如果没有父流程就传入为0.
	 * @param parentFlowNo
	 *            父流程的流程编号,如果没有父流程就传入为null.
	 * @return 为开始节点创建工作后产生的WorkID.
	 * @throws Exception
	 */
	public static long Node_CreateBlankWork(String flowNo, Hashtable ht, DataSet workDtls, String guestNo, String title,
			long parentWorkID, String parentFlowNo, int parentNodeID, String parentEmp) throws Exception {
		String dbstr = SystemConfig.getAppCenterDBVarStr();

		Flow fl = new Flow(flowNo);
		Node nd = new Node(fl.getStartNodeID());

		// 把一些其他的参数也增加里面去,传递给ccflow.
		Hashtable htPara = new Hashtable();
		if (parentWorkID != 0) {
			htPara.put(StartFlowParaNameList.PWorkID, parentWorkID);
		}
		if (parentFlowNo != null) {
			htPara.put(StartFlowParaNameList.PFlowNo, parentFlowNo);
		}
		if (parentNodeID != 0) {
			htPara.put(StartFlowParaNameList.PNodeID, parentNodeID);
		}
		if (parentEmp != null) {
			htPara.put(StartFlowParaNameList.PEmp, parentEmp);
		}

		Emp empStarter = new Emp(WebUser.getNo());
		Work wk = fl.NewWork(empStarter, htPara);
		long workID = wk.getOID();

		/// #region 给各个属性-赋值
		if (ht != null) {
			for (Object str : ht.keySet()) {
				wk.SetValByKey(str.toString(), ht.get(str));
			}
		}
		wk.setOID(workID);
		if (workDtls != null) {
			// 保存从表
			for (DataTable dt : workDtls.Tables) {
				for (MapDtl dtl : wk.getHisMapDtls().ToJavaList()) {
					if (!dt.TableName.equals(dtl.getNo())) {
						continue;
					}
					// 获取dtls
					GEDtls daDtls = new GEDtls(dtl.getNo());
					daDtls.Delete(GEDtlAttr.RefPK, wk.getOID()); // 清除现有的数据.

					GEDtl daDtl = daDtls.getNewEntity() instanceof GEDtl ? (GEDtl) daDtls.getNewEntity() : null;
					daDtl.setRefPK(String.valueOf(wk.getOID()));

					// 为从表复制数据.
					for (DataRow dr : dt.Rows) {
						daDtl.ResetDefaultVal();
						daDtl.setRefPK(String.valueOf(wk.getOID()));

						// 明细列.
						for (DataColumn dc : dt.Columns) {
							// 设置属性.
							daDtl.SetValByKey(dc.ColumnName, dr.getValue(dc.ColumnName));
						}
						daDtl.InsertAsOID(DBAccess.GenerOID("Dtl")); // 插入数据.
					}
				}
			}
		}

		/// #endregion 赋值

		Paras ps = new Paras();
		// 执行对报表的数据表WFState状态的更新,让它为runing的状态.
		if (DataType.IsNullOrEmpty(title) == false) {
			ps = new Paras();
			ps.SQL = "UPDATE " + fl.getPTable() + " SET WFState=" + dbstr + "WFState,Title=" + dbstr
					+ "Title WHERE OID=" + dbstr + "OID";
			ps.Add(GERptAttr.WFState, WFState.Blank.getValue());
			ps.Add(GERptAttr.Title, title);
			ps.Add(GERptAttr.OID, wk.getOID());
			DBAccess.RunSQL(ps);
		} else {
			ps = new Paras();
			ps.SQL = "UPDATE " + fl.getPTable() + " SET WFState=" + dbstr + "WFState,FK_Dept=" + dbstr
					+ "FK_Dept,Title=" + dbstr + "Title WHERE OID=" + dbstr + "OID";
			ps.Add(GERptAttr.WFState, WFState.Blank.getValue());
			ps.Add(GERptAttr.FK_Dept, empStarter.getFK_Dept());
			ps.Add(GERptAttr.Title, BP.WF.WorkFlowBuessRole.GenerTitle(fl, wk));
			ps.Add(GERptAttr.OID, wk.getOID());
			DBAccess.RunSQL(ps);
		}

		// 删除有可能产生的垃圾数据,比如上一次没有发送成功，导致数据没有清除.
		ps = new Paras();
		ps.SQL = "DELETE FROM WF_GenerWorkFlow  WHERE WorkID=" + dbstr + "WorkID1 OR FID=" + dbstr + "WorkID2";
		ps.Add("WorkID1", wk.getOID());
		ps.Add("WorkID2", wk.getOID());
		DBAccess.RunSQL(ps);

		ps = new Paras();
		ps.SQL = "DELETE FROM WF_GenerWorkerList  WHERE WorkID=" + dbstr + "WorkID1 OR FID=" + dbstr + "WorkID2";
		ps.Add("WorkID1", wk.getOID());
		ps.Add("WorkID2", wk.getOID());
		DBAccess.RunSQL(ps);

		// 设置流程信息
		if (parentWorkID != 0) {
			BP.WF.Dev2Interface.SetParentInfo(flowNo, workID, parentWorkID);
		}

		return wk.getOID();
	}

	/// #region 门户。
	/**
	 * 登陆
	 * 
	 * @param guestNo
	 *            客户编号
	 * @param guestName
	 *            客户名称
	 * @throws Exception
	 */
	public static void Port_Login(String guestNo, String guestName) throws Exception {
		// 登陆.
		BP.Web.GuestUser.SignInOfGener(guestNo, guestName, "CH", true);
	}

	/**
	 * 登陆
	 * 
	 * @param guestNo
	 *            客户编号
	 * @param guestName
	 *            客户名称
	 * @param deptNo
	 *            客户的部门编号
	 * @param deptName
	 *            客户的部门名称
	 * @throws Exception
	 */
	public static void Port_Login(String guestNo, String guestName, String deptNo, String deptName) throws Exception {
		// 登陆.
		BP.Web.GuestUser.SignInOfGener(guestNo, guestName, deptNo, deptName, "CH", true);
	}

	/**
	 * 退出登陆.
	 */
	public static void Port_LoginOunt() {
		// 登陆.
		BP.Web.GuestUser.Exit();
	}

	/**
	 * 获取Guest的待办
	 * 
	 * @param fk_flow
	 *            流程编号,流程编号为空表示所有的流程.
	 * @param guestNo
	 *            客户编号
	 * @return 结果集合
	 */
	public static DataTable DB_GenerEmpWorksOfDataTable(String fk_flow, String guestNo) {

		Paras ps = new Paras();
		String dbstr = SystemConfig.getAppCenterDBVarStr();
		String sql;

		/* 不是授权状态 */
		if (DataType.IsNullOrEmpty(fk_flow)) {
			ps.SQL = "SELECT * FROM WF_EmpWorks WHERE GuestNo=" + dbstr
					+ "GuestNo AND FK_Emp='Guest' ORDER BY FK_Flow,ADT DESC ";
			ps.Add("GuestNo", guestNo);
		} else {
			ps.SQL = "SELECT * FROM WF_EmpWorks WHERE GuestNo=" + dbstr + "GuestNo AND FK_Emp='Guest' AND FK_Flow="
					+ dbstr + "FK_Flow ORDER BY  ADT DESC ";
			ps.Add("FK_Flow", fk_flow);
			ps.Add("GuestNo", guestNo);
		}
		return BP.DA.DBAccess.RunSQLReturnTable(ps);
	}

	/**
	 * 获取未完成的流程(也称为在途流程:我参与的但是此流程未完成)
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @return 返回从数据视图WF_GenerWorkflow查询出来的数据.
	 * @throws Exception
	 */
	public static DataTable DB_GenerRuning(String fk_flow, String guestNo) throws Exception {

		String sql;
		int state = WFState.Runing.getValue();

		if (DataType.IsNullOrEmpty(fk_flow)) {
			sql = "SELECT a.WorkID FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND B.FK_Emp='"
					+ WebUser.getNo() + "' AND B.IsEnable=1 AND B.IsPass=1 AND A.GuestNo='" + guestNo + "' ";
		} else {
			sql = "SELECT a.WorkID FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.FK_Flow='" + fk_flow
					+ "'  AND A.WorkID=B.WorkID AND B.FK_Emp='" + WebUser.getNo()
					+ "' AND B.IsEnable=1 AND B.IsPass=1  AND A.GuestNo='" + guestNo + "'";
		}

		GenerWorkFlows gwfs = new GenerWorkFlows();
		gwfs.RetrieveInSQL(GenerWorkFlowAttr.WorkID, "(" + sql + ")");
		return gwfs.ToDataTableField();
	}

	/**
	 * 设置用户信息
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param workID
	 *            工作ID
	 * @param guestNo
	 *            客户编号
	 * @param guestName
	 *            客户名称
	 * @throws Exception
	 */
	public static void SetGuestInfo(String flowNo, long workID, String guestNo, String guestName) throws Exception {
		String dbstr = SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkFlow SET GuestNo=" + dbstr + "GuestNo, GuestName=" + dbstr
				+ "GuestName WHERE WorkID=" + dbstr + "WorkID";
		ps.Add("GuestNo", guestNo);
		ps.Add("GuestName", guestName);
		ps.Add("WorkID", workID);
		BP.DA.DBAccess.RunSQL(ps);

		Flow fl = new Flow(flowNo);
		ps = new Paras();
		ps.SQL = "UPDATE " + fl.getPTable() + " SET GuestNo=" + dbstr + "GuestNo, GuestName=" + dbstr
				+ "GuestName WHERE OID=" + dbstr + "OID";
		ps.Add("GuestNo", guestNo);
		ps.Add("GuestName", guestName);
		ps.Add("OID", workID);
		BP.DA.DBAccess.RunSQL(ps);
	}

	/**
	 * 设置当前用户的待办
	 * 
	 * @param workID
	 *            工作ID
	 * @param guestNo
	 *            客户编号
	 * @param guestName
	 *            客户名称
	 */
	public static void SetGuestToDoList(long workID, String guestNo, String guestName) {
		if (guestNo.equals("")) {
			throw new RuntimeException("@设置外部用户待办信息失败:参数guestNo不能为空.");
		}
		if (workID == 0) {
			throw new RuntimeException("@设置外部用户待办信息失败:参数workID不能为0.");
		}

		String dbstr = SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkerList SET GuestNo=" + dbstr + "GuestNo, GuestName=" + dbstr
				+ "GuestName WHERE WorkID=" + dbstr + "WorkID AND IsPass=0";
		ps.Add("GuestNo", guestNo);
		ps.Add("GuestName", guestName);
		ps.Add("WorkID", workID);
		int i = BP.DA.DBAccess.RunSQL(ps);
		if (i == 0) {
			throw new RuntimeException("@设置外部用户待办信息失败:参数workID不能为空.");
		}

		ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkFlow SET GuestNo=" + dbstr + "GuestNo, GuestName=" + dbstr
				+ "GuestName WHERE WorkID=" + dbstr + "WorkID ";
		ps.Add("GuestNo", guestNo);
		ps.Add("GuestName", guestName);
		ps.Add("WorkID", workID);
		i = BP.DA.DBAccess.RunSQL(ps);
		if (i == 0) {
			throw new RuntimeException("@WF_GenerWorkFlow - 设置外部用户待办信息失败:参数WorkID不能为空.");
		}
	}

	private boolean HttpRequestPost(String userNo, String password, String method) throws Exception {
		String webPath = SystemConfig.getAppSettings().get("HostURL") + "/services/PortalInterfaceWS";
		Service service = new Service();
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(webPath);
		call.setOperationName(new QName("http://WebServiceImp", method));// WSDL里面描述的接口名称
		call.addParameter("userNo", org.apache.axis.encoding.XMLType.XSD_LONG, javax.xml.rpc.ParameterMode.IN);// 接口的参数
		call.addParameter("password", org.apache.axis.encoding.XMLType.XSD_DATE, javax.xml.rpc.ParameterMode.IN);// 接口的参数

		call.setReturnType(org.apache.axis.encoding.XMLType.XSD_BOOLEAN);// 设置返回类型

		boolean result = (boolean) call.invoke(new Object[] { userNo, password });
		// 给方法传递参数，并且调用方法
		System.out.println("result is " + result);
		return result;
	}

	/**
	 * 调用Jflow-web中WebService接口
	 * 
	 * @param sender
	 * @param sendToEmpNo
	 * @param title
	 * @param msgInfo
	 * @param OpenUrl
	 * @param msgType
	 * @param method
	 * @return
	 */
	private boolean HttpRequest(String mypk, String sender, String sendToEmpNo, String tel, String title,
			String msgInfo, String OpenUrl, String msgType, String method) throws Exception {
		String webPath = SystemConfig.getAppSettings().get("HostURL") + "/services/PortalInterfaceWS";
		Service service = new Service();
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(webPath);
		call.setOperationName(new QName("http://WebServiceImp", method));// WSDL里面描述的接口名称
		call.addParameter("mypk", org.apache.axis.encoding.XMLType.XSD_LONG, javax.xml.rpc.ParameterMode.IN);// 接口的参数
		call.addParameter("sender", org.apache.axis.encoding.XMLType.XSD_LONG, javax.xml.rpc.ParameterMode.IN);// 接口的参数
		call.addParameter("sendToEmpNo", org.apache.axis.encoding.XMLType.XSD_DATE, javax.xml.rpc.ParameterMode.IN);// 接口的参数
		call.addParameter("tel", org.apache.axis.encoding.XMLType.XSD_LONG, javax.xml.rpc.ParameterMode.IN);// 接口的参数
		call.addParameter("title", org.apache.axis.encoding.XMLType.XSD_DATE, javax.xml.rpc.ParameterMode.IN);// 接口的参数
		call.addParameter("msgInfo", org.apache.axis.encoding.XMLType.XSD_DATE, javax.xml.rpc.ParameterMode.IN);// 接口的参数
		call.addParameter("OpenUrl", org.apache.axis.encoding.XMLType.XSD_DATE, javax.xml.rpc.ParameterMode.IN);// 接口的参数

		call.addParameter("msgType", org.apache.axis.encoding.XMLType.XSD_DATE, javax.xml.rpc.ParameterMode.IN);// 接口的参数

		call.setReturnType(org.apache.axis.encoding.XMLType.XSD_BOOLEAN);// 设置返回类型

		boolean result = (boolean) call
				.invoke(new Object[] { mypk, sender, sendToEmpNo, tel, title, msgInfo, OpenUrl, msgType });
		// 给方法传递参数，并且调用方法
		System.out.println("result is " + result);
		return result;
	}

	public DataTable GenerEmpsBySpecDeptAndStats(String empDept, String stas) throws Exception {
		try {
			String sql = "SELECT a.No,a.Name,a.FK_Dept FROM Port_Emp A, Port_DeptEmpStation B WHERE A.No=B.FK_Emp AND B.FK_Station IN ("
					+ stas + ") AND A.FK_Dept='" + empDept + "'";
			return BP.DA.DBAccess.RunSQLReturnTable(sql);
		} catch (Exception ex) {
			throw new Exception("@ 指定部门与一个岗位集合，获得他们的人员:" + ex.getMessage()); // 连接错误，直接抛出异常.
		}
	}

}
