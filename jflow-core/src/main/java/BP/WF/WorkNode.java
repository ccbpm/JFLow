package BP.WF;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.sun.star.bridge.oleautomation.Decimal;

import BP.En.*;
import BP.DA.*;
import BP.Port.*;
import BP.Web.*;
import BP.Sys.*;
import BP.Tools.DateUtils;
import BP.Tools.StringHelper;
import BP.WF.Template.*;
import BP.WF.Template.Bill;
import BP.WF.Template.BillFileType;
import BP.WF.Glo;
import BP.WF.Data.*;

/**
 * WF 的摘要说明. 工作流. 这里包含了两个方面 工作的信息． 流程的信息.
 * 
 */
public class WorkNode {

	/// #region 权限判断
	/**
	 * 判断一个人能不能对这个工作节点进行操作。
	 * 
	 * @param empId
	 * @return
	 * @throws Exception 
	 */
	private boolean IsCanOpenCurrentWorkNode(String empId) throws Exception {
		WFState stat = this.getHisGenerWorkFlow().getWFState();
		if (stat == WFState.Runing) {
			if (this.getHisNode().getIsStartNode()) {
				// 如果是开始工作节点，从工作岗位判断他有没有工作的权限。
				return WorkFlow.IsCanDoWorkCheckByEmpStation(this.getHisNode().getNodeID(), empId);
			} else {
				// 如果是初始化阶段,判断他的初始化节点
				GenerWorkerList wl = new GenerWorkerList();
				wl.setWorkID(this.getHisWork().getOID());
				wl.setFK_Emp(empId);

				Emp myEmp = new Emp(empId);
				wl.setFK_EmpText(myEmp.getName());

				wl.setFK_Node(this.getHisNode().getNodeID());
				wl.setFK_NodeText(this.getHisNode().getName());
				return wl.getIsExits();
			}
		} else {
			// 如果是初始化阶段
			return false;
		}
	}

	/**
	 * 子线程是否有分组标志.
	 */
	private boolean IsHaveSubThreadGroupMark = false;

	private String _execer = null;

	/**
	 * 实际执行人，执行工作发送时，有时候当前 WebUser.getNo() 并非实际的执行人。
	 * @throws Exception 
	 * 
	 */
	public final String getExecer() throws Exception {
		if (_execer == null || _execer.equals("")) {
			_execer = WebUser.getNo();
		}
		return _execer;
	}

	public final void setExecer(String value) {
		_execer = value;
	}

	private String _execerName = null;

	/**
	 * 实际执行人名称(请参考实际执行人)
	 * 
	 */
	public final String getExecerName() {
		if (_execerName == null || _execerName.equals("")) {
			_execerName = WebUser.getName();
		}
		return _execerName;
	}

	public final void setExecerName(String value) {
		_execerName = value;
	}

	private String _execerDeptName = null;

	/**
	 * 实际执行人名称(请参考实际执行人)
	 * 
	 */
	public final String getExecerDeptName() {
		if (_execerDeptName == null) {
			_execerDeptName = WebUser.getFK_DeptName();
		}
		return _execerDeptName;
	}

	public final void setExecerDeptName(String value) {
		_execerDeptName = value;
	}

	private String _execerDeptNo = null;

	/**
	 * 实际执行人名称(请参考实际执行人)
	 * @throws Exception 
	 * 
	 */
	public final String getExecerDeptNo() throws Exception {
		if (_execerDeptNo == null) {
			_execerDeptNo = WebUser.getFK_Dept();
		}
		return _execerDeptNo;
	}

	public final void setExecerDeptNo(String value) {
		_execerDeptNo = value;
	}

	/**
	 * 虚拟目录的路径
	 * 
	 */
	private String _VirPath = null;

	/**
	 * 虚拟目录的路径
	 * 
	 */
	public final String getVirPath() {
		if (_VirPath == null && BP.Sys.SystemConfig.getIsBSsystem()) {
			_VirPath = Glo.getCCFlowAppPath(); // BP.Sys.Glo.Request.ApplicationPath;
		}
		return _VirPath;
	}

	private String _AppType = null;

	/**
	 * 虚拟目录的路径
	 * 
	 */
	public final String getAppType() {
		if (BP.Sys.SystemConfig.getIsBSsystem() == false) {
			return "CCFlow";
		}

		if (_AppType == null && BP.Sys.SystemConfig.getIsBSsystem()) {
			if (BP.Web.WebUser.getIsWap()) {
				_AppType = "WF/WAP";
			} else {
				boolean b = BP.Sys.Glo.getRequest().getRequestURI().toLowerCase().contains("oneflow");
				if (b) {
					_AppType = "WF/OneFlow";
				} else {
					_AppType = "WF";
				}
			}
		}
		return _AppType;
	}

	private String nextStationName = "";
	public WorkNode town = null;
	private boolean IsFindWorker = false;

	public final boolean getIsSubFlowWorkNode() throws Exception {
		if (this.getHisWork().getFID() == 0) {
			return false;
		} else {
			return true;
		}
	}

	/// #region GenerWorkerList 相关方法.
	// 查询出每个节点表里的接收人集合（Emps）。
	public final String GenerEmps(Node nd) {
		String str = "";
		for (GenerWorkerList wl : this.HisWorkerLists.ToJavaList()) {
			str = wl.getFK_Emp() + ",";
		}
		return str;
	}

	/**
	 * 产生它的工作者
	 * 
	 * @param town
	 *            WorkNode
	 * @return 产生的工作人员
	 * @throws Exception 
	 */
	public final GenerWorkerLists Func_GenerWorkerLists(WorkNode town) throws Exception {
		this.town = town;
		DataTable dt = new DataTable();
		dt.Columns.Add("No", String.class);
		String sql;
		String FK_Emp;

		// 如果指定特定的人员处理。
		if (StringHelper.isNullOrEmpty(JumpToEmp) == false) {
			String[] emps = JumpToEmp.split("[,]", -1);
			for (String emp : emps) {
				if (StringHelper.isNullOrEmpty(emp)) {
					continue;
				}
				DataRow dr = dt.NewRow();
				dr.setValue(0, emp);
				dt.Rows.add(dr);
			}

			// 如果是抢办或者共享.

			// 如果执行了两次发送，那前一次的轨迹就需要被删除,这里是为了避免错误。
			ps = new Paras();
			ps.Add("WorkID", this.getHisWork().getOID());
			ps.Add("FK_Node", town.getHisNode().getNodeID());
			ps.SQL = "DELETE FROM WF_GenerWorkerlist WHERE WorkID=" + dbStr + "WorkID AND FK_Node =" + dbStr
					+ "FK_Node";
			DBAccess.RunSQL(ps);

			return InitWorkerLists(town, dt);
		}

		// 如果执行了两次发送，那前一次的轨迹就需要被删除,这里是为了避免错误,
		ps = new Paras();
		ps.Add("WorkID", this.getHisWork().getOID());
		ps.Add("FK_Node", town.getHisNode().getNodeID());
		ps.SQL = "DELETE FROM WF_GenerWorkerlist WHERE WorkID=" + dbStr + "WorkID AND FK_Node =" + dbStr + "FK_Node";
		DBAccess.RunSQL(ps);

		if (this.town.getHisNode().getHisDeliveryWay() == DeliveryWay.ByCCFlowBPM || 1 == 1) {
			// 如果设置了安ccbpm的BPM模式
			while (true) {
				FindWorker fw = new FindWorker();
				dt = fw.DoIt(this.getHisFlow(), this, town);
				if (dt == null) {
					throw new RuntimeException("@没有找到接收人.");
				}

				return InitWorkerLists(town, dt);
			}
		}

		throw new RuntimeException("@此部分代码已经移除了.");
	}

	/// #endregion GenerWorkerList 相关方法.

	/**
	 * 生成一个 word
	 * @throws Exception 
	 */
	public final void DoPrint() throws Exception {
		try {
			String tempFile = SystemConfig.getPathOfTemp() + "/" + this.getWorkID() + ".doc";
			Work wk = this.getHisNode().getHisWork();
			wk.setOID(this.getWorkID());
			wk.Retrieve();
			Glo.GenerWord(tempFile, wk);
			PubClass.OpenWordDocV2(tempFile, this.getHisNode().getName() + ".doc");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String dbStr = SystemConfig.getAppCenterDBVarStr();
	public Paras ps = new Paras();

	/**
	 * 递归删除两个节点之间的数据
	 * 
	 * @param nds
	 *            到达的节点集合
	 * @throws Exception 
	 */
	public final void DeleteToNodesData(Nodes nds) throws Exception {
		if (this.getHisFlow().getHisDataStoreModel() == DataStoreModel.SpecTable) {
			return;
		}
		// 开始遍历到达的节点集合
		for (Node nd : nds.ToJavaList()) {
			Work wk = nd.getHisWork();
			if (this.getHisFlow().getPTable().equals(wk.getEnMap().getPhysicsTable())) {
				continue;
			}

			wk.setOID(this.getWorkID());
			if (wk.Delete() == 0) {
				wk.setFID(this.getWorkID());
				if (this.getHisFlow().getPTable().equals(wk.getEnMap().getPhysicsTable())) {
					continue;
				}
				if (wk.Delete(WorkAttr.FID, this.getWorkID()) == 0) {
					continue;
				}
			}
			// 删除明细表信息。
			MapDtls dtls = new MapDtls("ND" + nd.getNodeID());
			for (MapDtl dtl : dtls.ToJavaList()) {
				ps = new Paras();
				ps.SQL = "DELETE FROM " + dtl.getPTable() + " WHERE RefPK=" + dbStr + "WorkID";
				ps.Add("WorkID", (new Long(this.getWorkID())).toString());
				BP.DA.DBAccess.RunSQL(ps);
			}

			// 删除表单附件信息。
			BP.DA.DBAccess.RunSQL(
					"DELETE FROM Sys_FrmAttachmentDB WHERE RefPKVal=" + dbStr + "WorkID AND FK_MapData=" + dbStr
							+ "FK_MapData ",
					"WorkID", (new Long(this.getWorkID())).toString(), "FK_MapData", "ND" + nd.getNodeID());
			// 删除签名信息。
			BP.DA.DBAccess.RunSQL(
					"DELETE FROM Sys_FrmEleDB WHERE RefPKVal=" + dbStr + "WorkID AND FK_MapData=" + dbStr
							+ "FK_MapData ",
					"WorkID", (new Long(this.getWorkID())).toString(), "FK_MapData", "ND" + nd.getNodeID());

			/// #endregion 删除当前节点数据。

			// 说明:已经删除该节点数据。
			DBAccess.RunSQL(
					"DELETE FROM WF_GenerWorkerList WHERE (WorkID=" + dbStr + "WorkID1 OR FID=" + dbStr
							+ "WorkID2 ) AND FK_Node=" + dbStr + "FK_Node",
					"WorkID1", this.getWorkID(), "WorkID2", this.getWorkID(), "FK_Node", nd.getNodeID());

			if (nd.getIsFL()) {
				// 如果是分流
				GenerWorkerLists wls = new GenerWorkerLists();
				QueryObject qo = new QueryObject(wls);
				qo.AddWhere(GenerWorkerListAttr.FID, this.getWorkID());
				qo.addAnd();

				String[] ndsStrs = nd.getHisToNDs().split("[@]", -1);
				String inStr = "";
				for (String s : ndsStrs) {
					if (s.equals("") || s == null) {
						continue;
					}
					inStr += "'" + s + "',";
				}
				inStr = inStr.substring(0, inStr.length() - 1);
				if (inStr.contains(",") == true) {
					qo.AddWhere(GenerWorkerListAttr.FK_Node, Integer.parseInt(inStr));
				} else {
					qo.AddWhereIn(GenerWorkerListAttr.FK_Node, "(" + inStr + ")");
				}

				qo.DoQuery();
				for (GenerWorkerList wl : wls.ToJavaList()) {
					Node subNd = new Node(wl.getFK_Node());
					Work subWK = subNd.GetWork(wl.getWorkID());
					subWK.Delete();

					// 删除分流下步骤的节点信息.
					DeleteToNodesData(subNd.getHisToNodes());
				}

				DBAccess.RunSQL("DELETE FROM WF_GenerWorkFlow WHERE FID=" + dbStr + "WorkID", "WorkID",
						this.getWorkID());
				DBAccess.RunSQL("DELETE FROM WF_GenerWorkerList WHERE FID=" + dbStr + "WorkID", "WorkID",
						this.getWorkID());
				 
			}
			DeleteToNodesData(nd.getHisToNodes());
		}
	}

	/// #region 根据工作岗位生成工作者
	private Node _ndFrom = null;

	private Node getndFrom() {
		if (_ndFrom == null) {
			_ndFrom = this.getHisNode();
		}
		return _ndFrom;
	}

	private void setndFrom(Node value) {
		_ndFrom = value;
	}

	/**
	 * 初始化工作人员
	 * 
	 * @param town
	 *            到达的wn
	 * @param dt
	 *            数据源
	 * @param fid
	 *            FID
	 * @return GenerWorkerLists
	 * @throws Exception 
	 */
	private GenerWorkerLists InitWorkerLists(WorkNode town, DataTable dt, long fid) throws Exception {
		if (dt.Rows.size() == 0) {
			throw new RuntimeException("接受人员列表为空"); // 接受人员列表为空
		}

		this.getHisGenerWorkFlow().setTodoEmpsNum(-1);

		/// #region 判断发送的类型，处理相关的FID.
		// 定义下一个节点的接受人的 FID 与 WorkID.
		long nextUsersWorkID = this.getWorkID();
		long nextUsersFID = this.getHisWork().getFID();

		// 是否是分流到子线程。
		boolean isFenLiuToSubThread = false;
		if (this.getHisNode().getIsFLHL() == true && town.getHisNode().getHisRunModel() == RunModel.SubThread) {
			isFenLiuToSubThread = true;
			nextUsersWorkID = 0;
			nextUsersFID = this.getHisWork().getOID();
		}

		// 子线程 到 合流点or 分合流点.
		boolean isSubThreadToFenLiu = false;
		if (this.getHisNode().getHisRunModel() == RunModel.SubThread && town.getHisNode().getIsFLHL() == true) {
			nextUsersWorkID = this.getHisWork().getFID();
			nextUsersFID = 0;
			isSubThreadToFenLiu = true;
		}

		// 子线程到子线程.
		boolean isSubthread2Subthread = false;
		if (this.getHisNode().getHisRunModel() == RunModel.SubThread
				&& town.getHisNode().getHisRunModel() == RunModel.SubThread) {
			nextUsersWorkID = this.getHisWork().getOID();
			nextUsersFID = this.getHisWork().getFID();
			isSubthread2Subthread = true;
		}

		/// #endregion 判断发送的类型，处理相关的FID.

		int toNodeId = town.getHisNode().getNodeID();
		this.HisWorkerLists = new GenerWorkerLists();
		this.HisWorkerLists.clear();

		/// #region 限期时间 town.HisNode.TSpan-1
		// 2008-01-22 之前的东西。
		// int i = town.HisNode.TSpan;
		// dtOfShould = DataType.AddDays(dtOfShould, i);
		// if (town.HisNode.WarningHour > 0)
		// dtOfWarning = DataType.AddDays(dtOfWarning, i -
		// town.HisNode.WarningHour);
		// edit at 2008-01-22 , 处理预警日期的问题。

		java.util.Date dtOfShould = new java.util.Date(0);
		if (this.getHisFlow().getHisTimelineRole() == TimelineRole.ByFlow) {
			// 如果整体流程是按流程设置计算。
			dtOfShould = DataType.ParseSysDateTime2DateTime(this.getHisGenerWorkFlow().getSDTOfFlow());
		} else {
			int day = 0;
			int hh = 0;

			// 增加天数. 考虑到了节假日.
			dtOfShould = Glo.AddDayHoursSpan(new Date(),
					this.town.getHisNode().getTimeLimit(),
					this.town.getHisNode().getTimeLimitHH(),
					this.town.getHisNode().getTimeLimitMM());
		}

		// 求警告日期.
		java.util.Date dtOfWarning = new java.util.Date();
		if (this.town.getHisNode().getWarningDay() == 0 ) {
			dtOfWarning = dtOfShould;
		} else {
			// 计算警告日期。
			// 增加小时数. 考虑到了节假日.
			dtOfWarning = Glo.AddDayHoursSpan(new Date(), this.town.getHisNode().getTimeLimit(),
					this.town.getHisNode().getTimeLimitHH(),this.town.getHisNode().getTimeLimitMM() );
		}

		switch (this.getHisNode().getHisNodeWorkType()) {
		case StartWorkFL:
		case WorkFHL:
		case WorkFL:
		case WorkHL:
			break;
		default:
			this.getHisGenerWorkFlow().setFK_Node(town.getHisNode().getNodeID());
			this.getHisGenerWorkFlow().setSDTOfNode(DateUtils.format(dtOfShould, DataType.getSysDataTimeFormat()));
			this.getHisGenerWorkFlow().setTodoEmpsNum(dt.Rows.size());
			break;
		}

		/// #endregion 限期时间 town.HisNode.TSpan-1

		/// #region 处理 人员列表 数据源。
		// 定义是否有分组mark. 如果有三列，就说明该集合中有分组 mark. 就是要处理一个人多个子线程的情况.
		if (dt.Columns.size() == 3 && town.getHisNode().getHisFormType() == NodeFormType.SheetAutoTree) {
			this.IsHaveSubThreadGroupMark = false;
		} else {
			this.IsHaveSubThreadGroupMark = true;
		}

		// 如果有4个列并且下一个节点是动态表单树节点.No,Name,BatchNo,FrmIDs 这样的四个列，就是子线程分组.
		if (dt.Columns.size() == 4 && town.getHisNode().getHisFormType() == NodeFormType.SheetAutoTree) {
			this.IsHaveSubThreadGroupMark = true;
		}

		if (dt.Columns.size() <= 2 && town.getHisNode().getHisFormType() == NodeFormType.SheetAutoTree) {
			throw new RuntimeException("@组织的数据源不正确，到达的节点" + town.getHisNode().getName() + ",表单类型是动态表单树，至少返回3列来标识表单ID.");
		}

		if (dt.Columns.size() <= 2) {
			this.IsHaveSubThreadGroupMark = false;
		}

		if (dt.Rows.size() == 1) {
			// 如果只有一个人员
			GenerWorkerList wl = new GenerWorkerList();
			if (isFenLiuToSubThread) {
				// 说明这是分流点向下发送
				// * 在这里产生临时的workid.
				//
				wl.setWorkID(DBAccess.GenerOIDByGUID());
			} else {
				wl.setWorkID(nextUsersWorkID);
			}

			wl.setFID(nextUsersFID);
			wl.setFK_Node(toNodeId);
			wl.setFK_NodeText(town.getHisNode().getName());

			wl.setFK_Emp(dt.Rows.get(0).getValue(0).toString());

			Emp emp = new Emp();
			emp.setNo(wl.getFK_Emp());
			if (emp.RetrieveFromDBSources() == 0) {
				throw new RuntimeException("@接收人规则设置错误，求出来的接收人[" + wl.getFK_Emp() + "]不存在或者被停用。");
			}

			wl.setFK_EmpText(emp.getName());
			wl.setFK_Dept(emp.getFK_Dept());
			wl.setWarningHour(0);
			wl.setSDT(DateUtils.format(dtOfShould, DataType.getSysDataTimeFormat()));

			wl.setDTOfWarning(DateUtils.format(dtOfShould, DataType.getSysDataTimeFormat()));
			wl.setRDT(DateUtils.format(new Date(), DataType.getSysDataTimeFormat()));
			wl.setFK_Flow(town.getHisNode().getFK_Flow());
			// wl.Sender = this.Execer;

			// and 2015-01-14 , 如果有三列，就约定为最后一列是分组标志， 有标志就把它放入标志里 .
			if (this.IsHaveSubThreadGroupMark == true) {
				wl.setGroupMark(dt.Rows.get(0).getValue(2).toString()); // 第3个列是分组标记.
				if (DotNetToJavaStringHelper.isNullOrEmpty(wl.getGroupMark())) {
					throw new RuntimeException("@｛" + wl.getFK_Emp() + "｝分组标记中没有值,会导致无法按照分组标记去生成子线程,请检查配置的信息是否正确.");
				}
			}

			if (this.IsHaveSubThreadGroupMark == true
					&& town.getHisNode().getHisFormType() == NodeFormType.SheetAutoTree) {
				// 是分组标记，并且是自动表单树.
				wl.setGroupMark(dt.Rows.get(0).getValue(2).toString()); // 第3个列是分组标记.
				wl.setFrmIDs(dt.Rows.get(0).getValue(3).toString()); // 第4个列是可以执行的表单IDs.
			}

			if (dt.Columns.size() == 3 && town.getHisNode().getHisFormType() == NodeFormType.SheetAutoTree) {
				// 是自动表单树,只有3个列，说明最后一列就是表单IDs.
				wl.setFrmIDs(dt.Rows.get(0).getValue(2).toString()); // 第3个列是可以执行的表单IDs.
			}

			// 设置发送人.
			if (BP.Web.WebUser.getNo().equals("Guest")) {
				wl.setSender(GuestUser.getNo() + "," + GuestUser.getName());
				wl.setGuestNo(GuestUser.getNo());
				wl.setGuestName(GuestUser.getName());
			} else {
				wl.setSender(WebUser.getNo() + "," + WebUser.getName());
			}

			// 判断下一个节点是否是外部用户处理人节点？
			if (town.getHisNode().getIsGuestNode()) {
				if (!this.getHisGenerWorkFlow().getGuestNo().equals("")) {
					wl.setGuestNo(this.getHisGenerWorkFlow().getGuestNo());
					wl.setGuestName(this.getHisGenerWorkFlow().getGuestName());
				} else {
					// 这种情况是，不是外部用户发起的流程。
					if (town.getHisNode().getHisDeliveryWay() == DeliveryWay.BySQL) {
						Object tempVar = town.getHisNode().getDeliveryParas();
						String mysql = (String) ((tempVar instanceof String) ? tempVar : null);
						DataTable mydt = BP.DA.DBAccess.RunSQLReturnTable(Glo.DealExp(mysql, this.rptGe, null));

						wl.setGuestNo(mydt.Rows.get(0).getValue(0).toString());
						wl.setGuestName(mydt.Rows.get(0).getValue(1).toString());

						this.getHisGenerWorkFlow().setGuestNo(wl.getGuestNo());
						this.getHisGenerWorkFlow().setGuestName(wl.getGuestName());
					} else if (town.getHisNode().getHisDeliveryWay() == DeliveryWay.ByPreviousNodeFormEmpsField) {
						wl.setGuestNo(this.getHisWork().GetValStrByKey(town.getHisNode().getDeliveryParas()));
						wl.setGuestName("外部用户");
						this.getHisGenerWorkFlow().setGuestNo(wl.getGuestNo());
						this.getHisGenerWorkFlow().setGuestName(wl.getGuestName());
					} else {
						throw new RuntimeException("@当前节点[" + this.town.getHisNode().getName()
								+ "]是中间节点，并且是外部用户处理节点，您需要正确的设置，这个外部用户接受人规则。");
					}
				}

				// wl.FK_Emp = wl.GuestNo;
				// wl.GuestName = wl.GuestName;
			}

			wl.DirectInsert();
			this.HisWorkerLists.AddEntity(wl);

			RememberMe rm = new RememberMe(); // this.GetHisRememberMe(town.HisNode);
			rm.setObjs("@" + wl.getFK_Emp() + "@");
			rm.setObjsExt(rm.getObjsExt() + BP.WF.Glo.DealUserInfoShowModel(wl.getFK_Emp(), wl.getFK_EmpText()));
			rm.setEmps("@" + wl.getFK_Emp() + "@");
			rm.setEmpsExt(BP.WF.Glo.DealUserInfoShowModel(wl.getFK_Emp(), wl.getFK_EmpText()));
			this.HisRememberMe = rm;
		} else {
			// 如果有多个人员，就要考虑接受人是否记忆属性的问题。
			RememberMe rm = this.GetHisRememberMe(town.getHisNode());

			/// #region 是否需要清空记忆属性.
			// 如果按照选择的人员处理，就设置它的记忆为空。2011-11-06 处理电厂需求 .
			if (this.town.getHisNode().getHisDeliveryWay() == DeliveryWay.BySelected
					|| this.town.getHisNode().getIsAllowRepeatEmps() == true
					|| town.getHisNode().getIsRememberMe() == false) // 允许接受人员重复
			{
				if (rm != null) {
					rm.setObjs("");
				}
			}

			if (this.getHisNode().getIsFL()) {
				if (rm != null) {
					rm.setObjs("");
				}
			}

			if (this.IsHaveSubThreadGroupMark == false && rm != null && !rm.getObjs().equals("")) {
				// 检查接受人列表是否发生了变化,如果变化了，就要把有效的接受人清空，让其重新生成.
				String emps = "@";
				for (DataRow dr : dt.Rows) {
					emps += dr.getValue(0).toString() + "@";
				}

				emps = ischongfu(rm.getEmps());// 增加去重复
				if (!rm.getEmps().equals(emps)) {
					// 列表发生了变化.
					rm.setEmps(emps);
					rm.setObjs(""); // 清空有效的接受人集合.
				}
			}

			/// #endregion 是否需要清空记忆属性.

			String myemps = "";
			Emp emp = null;
			for (DataRow dr : dt.Rows) {
				String fk_emp = dr.getValue(0).toString();
				if (this.IsHaveSubThreadGroupMark == true) {
					// 如果有分组 Mark ,就不处理重复人员的问题.
				} else {
					// 处理人员重复的，不然会导致generworkerlist的pk错误。
					if (myemps.indexOf("@" + dr.getValue(0).toString() + ",") != -1) {
						continue;
					}
					myemps += "@" + dr.getValue(0).toString() + ",";
				}

				GenerWorkerList wl = new GenerWorkerList();

				/// #region 根据记忆是否设置该操作员可用与否。
				if (rm != null) {
					if (rm.getObjs().equals("")) {
						// 如果是空的.
						wl.setIsEnable(true);
					} else {
						if (rm.getObjs().contains("@" + fk_emp + "@") == true) {
							wl.setIsEnable(true); // 如果包含，就说明他已经有了
						} else {
							wl.setIsEnable(false);
						}
					}
				} else {
					wl.setIsEnable(false);
				}

				/// #endregion 根据记忆是否设置该操作员可用与否。

				wl.setFK_Node(toNodeId);
				wl.setFK_NodeText(town.getHisNode().getName());
				wl.setFK_Emp(dr.getValue(0).toString());
				try {
					emp = new Emp(wl.getFK_Emp());
				} catch (RuntimeException ex) {
					throw new RuntimeException("@为人员分配工作时出现错误:" + wl.getFK_Emp() + ",没有执行成功,异常信息." + ex.getMessage());
				}

				wl.setFK_EmpText(emp.getName());
				wl.setFK_Dept(emp.getFK_Dept());
				wl.setWarningHour( 0);
				wl.setSDT(DateUtils.format(dtOfShould, DataType.getSysDataTimeFormat()));
				wl.setDTOfWarning(DateUtils.format(dtOfWarning, DataType.getSysDataTimeFormat()));
				wl.setRDT(DataType.getCurrentDataTime());

				wl.setFK_Flow(town.getHisNode().getFK_Flow());
				if (this.IsHaveSubThreadGroupMark == true) {
					// 设置分组信息.
					Object val = dr.get(2);
					if (val == null) {
						throw new RuntimeException("@分组标志不能为Null.");
					}

					if (StringHelper.isNullOrEmpty(val.toString())) {
						throw new RuntimeException("@分组标志不能为Null.");
					}

					wl.setGroupMark(val.toString());
					if (dt.Columns.size() == 4
							&& this.town.getHisNode().getHisFormType() == NodeFormType.SheetAutoTree) {
						wl.setFrmIDs(dr.get(3).toString());
						if (DotNetToJavaStringHelper.isNullOrEmpty(dr.get(3).toString())) {
							throw new RuntimeException("@组织的接受人数据源不正确,表单IDs不能为空.");
						}
					}
				} else {
					if (dt.Columns.size() == 3
							&& this.town.getHisNode().getHisFormType() == NodeFormType.SheetAutoTree) {
						// 如果只有三列, 并且是动态表单树.
						wl.setFrmIDs(dr.get(2).toString());
						if (DotNetToJavaStringHelper.isNullOrEmpty(dr.get(2).toString())) {
							throw new RuntimeException("@组织的接受人数据源不正确,表单IDs不能为空.");
						}
					}
				}

				wl.setFID(nextUsersFID);
				if (isFenLiuToSubThread) {
					// 说明这是分流点向下发送
					// * 在这里产生临时的workid.
					//
					wl.setWorkID(DBAccess.GenerOIDByGUID());
				} else {
					wl.setWorkID(nextUsersWorkID);
				}

				try {
					wl.DirectInsert();
					this.HisWorkerLists.AddEntity(wl);
				} catch (RuntimeException ex) {
					Log.DefaultLogWriteLineError("不应该出现的异常信息：" + ex.getMessage());
				}
			}

			// 执行对rm的更新。
			if (rm != null) {
				String empExts = "";
				String objs = "@"; // 有效的工作人员.
				String objsExt = "@"; // 有效的工作人员.
				for (GenerWorkerList wl : this.HisWorkerLists.ToJavaList()) {
					if (wl.getIsEnable() == false) {
						empExts += "<strike><font color=red>"
								+ BP.WF.Glo.DealUserInfoShowModel(wl.getFK_Emp(), wl.getFK_EmpText())
								+ "</font></strike>、";
					} else {
						empExts += BP.WF.Glo.DealUserInfoShowModel(wl.getFK_Emp(), wl.getFK_EmpText());
					}

					if (wl.getIsEnable() == true) {
						objs += wl.getFK_Emp() + "@";
						objsExt += BP.WF.Glo.DealUserInfoShowModel(wl.getFK_Emp(), wl.getFK_EmpText());
					}
				}
				rm.setEmpsExt(empExts);

				rm.setObjs(objs);
				rm.setObjsExt(objsExt);
				// rm.Save(); //保存.
				this.HisRememberMe = rm;
			}
		}

		if (this.HisWorkerLists.size() == 0) {
			throw new RuntimeException("@根据[" + this.town.getHisNode().getHisRunModel() + "]部门产生工作人员出现错误，流程["
					+ this.getHisWorkFlow().getHisFlow().getName() + "],中节点[" + town.getHisNode().getName()
					+ "]定义错误,没有找到接受此工作的工作人员.");
		}

		/// #endregion 处理 人员列表 数据源。

		/// #region 设置流程数量,其他的信息为任务池提供数据。
		String hisEmps = "";
		int num = 0;
		for (GenerWorkerList wl : this.HisWorkerLists.ToJavaList()) {
			if (wl.getIsPassInt() == 0 && wl.getIsEnable() == true) {
				num++;
				hisEmps += wl.getFK_Emp() + "," + wl.getFK_EmpText() + ";";
			}
		}

		if (num == 0) {
			throw new RuntimeException("@不应该产生的结果错误.");
		}

		this.getHisGenerWorkFlow().setTodoEmpsNum(num);
		this.getHisGenerWorkFlow().setTodoEmps(hisEmps);

		/// #endregion

		/// #region 求出日志类型，并加入变量中。
		ActionType at = ActionType.Forward;
		switch (town.getHisNode().getHisNodeWorkType()) {
		case StartWork:
		case StartWorkFL:
			at = ActionType.Start;
			break;
		case Work:
			if (this.getHisNode().getHisNodeWorkType() == NodeWorkType.WorkFL
					|| this.getHisNode().getHisNodeWorkType() == NodeWorkType.WorkFHL) {
				at = ActionType.ForwardFL;
			} else {
				at = ActionType.Forward;
			}
			break;
		case WorkHL:
			at = ActionType.ForwardHL;
			break;
		case SubThreadWork:
			at = ActionType.SubThreadForward;
			break;
		default:
			break;
		}

		
		//  #region 如果是子线城前进.
          if (at == ActionType.SubThreadForward)
          {
              for (GenerWorkerList wl : this.HisWorkerLists.ToJavaList())
              {
                  this.AddToTrack(at, wl, "子线程",this.town.getHisWork().getOID());
              }
              //写入到日志.
          }
          //#endregion 如果是子线城前进.
          
		// 把工作的当时信息存入数据库.
		//String json = this.getHisWork().ToJson();
          if (at != ActionType.SubThreadForward)
          {
		if (this.HisWorkerLists.size() == 1) {
			GenerWorkerList wl = (GenerWorkerList) ((this.HisWorkerLists.get(0) instanceof GenerWorkerList)
					? this.HisWorkerLists.get(0) : null);

			this.AddToTrack(at, wl.getFK_Emp(), wl.getFK_EmpText(), wl.getFK_Node(), wl.getFK_NodeText(), null,
					this.getndFrom());
		} else {
			String info = "共(" + this.HisWorkerLists.size() + ")人接收\t\n";

			String emps = "";
			for (GenerWorkerList wl : this.HisWorkerLists.ToJavaList()) {
				info += BP.WF.Glo.DealUserInfoShowModel(wl.getFK_DeptT(), wl.getFK_EmpText()) + "\t\n";

				emps += wl.getFK_Emp() + "," + wl.getFK_EmpText() + ";";
			}

			// 写入到日志.
			this.AddToTrack(at, this.getExecer(), "多人接受(见信息栏)", town.getHisNode().getNodeID(),
					town.getHisNode().getName(), info, this.getndFrom(), null, emps);
		}
        }
		/// #endregion

		/// #region 把数据加入变量中.
		String ids = "";
		String names = "";
		String idNames = "";
		if (this.HisWorkerLists.size() == 1) {
			GenerWorkerList gwl = (GenerWorkerList) this.HisWorkerLists.get(0);
			ids = gwl.getFK_Emp();
			names = gwl.getFK_EmpText();

			// 设置状态。
			this.getHisGenerWorkFlow().setTaskSta(TaskSta.None);
		} else {
			for (GenerWorkerList gwl : this.HisWorkerLists.ToJavaList()) {
				ids += gwl.getFK_Emp() + ",";
				names += gwl.getFK_EmpText() + ",";
			}

			// 设置状态, 如果该流程使用了启用共享任务池。
			if (town.getHisNode().getIsEnableTaskPool() && this.getHisNode().getHisRunModel() == RunModel.Ordinary) {
				this.getHisGenerWorkFlow().setTaskSta(TaskSta.Sharing);
			} else {
				this.getHisGenerWorkFlow().setTaskSta(TaskSta.None);
			}
		}

		this.addMsg(SendReturnMsgFlag.VarAcceptersID, ids, ids, SendReturnMsgType.SystemMsg);
		this.addMsg(SendReturnMsgFlag.VarAcceptersName, names, names, SendReturnMsgType.SystemMsg);
		this.addMsg(SendReturnMsgFlag.VarAcceptersNID, idNames, idNames, SendReturnMsgType.SystemMsg);

		/// #endregion

		return this.HisWorkerLists;
	}

	/**
	 * 初始化工作人员
	 * 
	 * @param town
	 *            到达的wn
	 * @param dt
	 *            数据源
	 * @param fid
	 *            FID
	 * @return GenerWorkerLists
	 * @throws Exception 
	 * @throws ParseException
	 */

	private GenerWorkerLists InitWorkerLists(WorkNode town, DataTable dt) throws Exception {
		return InitWorkerLists(town, dt, 0);
	}

	/// #region 判断一人多部门的情况
	/**
	 * HisDeptOfUse
	 * 
	 */
	private Dept _HisDeptOfUse = null;
	/**
	 * HisDeptOfUse
	 * 
	 */

	/// #endregion

	/// #region 条件
	private Conds _HisNodeCompleteConditions = null;

	/**
	 * 节点完成任务的条件 条件与条件之间是or 的关系, 就是说,如果任何一个条件满足,这个工作人员在这个节点上的任务就完成了.
	 * @throws Exception 
	 * 
	 */
	public final Conds getHisNodeCompleteConditions() throws Exception {
		if (this._HisNodeCompleteConditions == null) {
			_HisNodeCompleteConditions = new Conds(CondType.Node, this.getHisNode().getNodeID(), this.getWorkID(),
					this.rptGe);

			return _HisNodeCompleteConditions;
		}
		return _HisNodeCompleteConditions;
	}

	private Conds _HisFlowCompleteConditions = null;

	/**
	 * 他的完成任务的条件,此节点是完成任务的条件集合 条件与条件之间是or 的关系, 就是说,如果任何一个条件满足,这个任务就完成了.
	 * @throws Exception 
	 * 
	 */
	public final Conds getHisFlowCompleteConditions() throws Exception {
		if (this._HisFlowCompleteConditions == null) {
			_HisFlowCompleteConditions = new Conds(CondType.Flow, this.getHisNode().getNodeID(), this.getWorkID(),
					this.rptGe);
		}
		return _HisFlowCompleteConditions;
	}

	/// #endregion

	/// #region 关于质量考核
	///// <summary>
	///// 得到以前的已经完成的工作节点.
	///// </summary>
	///// <returns></returns>
	// public WorkNodes GetHadCompleteWorkNodes()
	// {
	// WorkNodes mywns = new WorkNodes();
	// WorkNodes wns = new WorkNodes(this.HisNode.HisFlow, this.HisWork.OID);
	// foreach (WorkNode wn in wns)
	// {
	// if (wn.IsComplete)
	// mywns.Add(wn);
	// }
	// return mywns;
	// }

	/// #endregion

	/// #region 流程公共方法
	private Flow _HisFlow = null;

	public final Flow getHisFlow() throws Exception {
		if (_HisFlow == null) {
			_HisFlow = this.getHisNode().getHisFlow();
		}
		return _HisFlow;
	}

	private Node JumpToNode = null;
	private String JumpToEmp = null;

	/// #region NodeSend 的附属功能.
	/**
	 * 获得下一个节点.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final Node NodeSend_GenerNextStepNode() throws Exception {
		// 如果要是跳转到的节点，自动跳转规则规则就会失效。
		if (this.JumpToNode != null) {
			return this.JumpToNode;
		}

		// 判断是否有延续流程.
		NodeYGFlows ygflows = new NodeYGFlows();
		ygflows.Retrieve(NodeYGFlowAttr.FK_Node, this._HisNode.getNodeID());
		if (ygflows.size() > 0 && 1==2) {
			for (NodeYGFlow item : ygflows.ToJavaList()) {
				boolean isPass = false;

				if (item.getExpType() == ConnDataFrom.Paras)
					isPass = BP.WF.Glo.CondExpPara(item.getCondExp(), this.rptGe.getRow());

				if (item.getExpType() == ConnDataFrom.SQL)
					isPass = BP.WF.Glo.CondExpSQL(item.getCondExp(), this.rptGe.getRow());

				if (isPass == true)
					return new Node(Integer.parseInt(item.getFK_Flow() + "01"));
			}
		}
		// 判断是否有延续流程.
		// 判断是否有用户选择的节点.
		if (this.getHisNode().getCondModel() == CondModel.ByUserSelected) {
			// 获取用户选择的节点.
			String nodes = this.getHisGenerWorkFlow().getParas_ToNodes();
			if (DotNetToJavaStringHelper.isNullOrEmpty(nodes)) {
				throw new RuntimeException("@用户没有选择发送到的节点.");
			}

			String[] mynodes = nodes.split("[,]", -1);
			for (String item : mynodes) {
				if (DotNetToJavaStringHelper.isNullOrEmpty(item)) {
					continue;
				}

				// 排除到达自身节点.
				if ((new Integer(this.getHisNode().getNodeID())).toString().equals(item)) {
					continue;
				}

				return new Node(Integer.parseInt(item));
			}

			// 设置他为空,以防止下一次发送出现错误.
			this.getHisGenerWorkFlow().setParas_ToNodes("");
		}

		Node nd = NodeSend_GenerNextStepNode_Ext1();

		// 写入到达信息.
		// this.addMsg(SendReturnMsgFlag.VarToNodeID, (new
		// Integer(nd.getNodeID())).toString(), (new
		// Integer(nd.getNodeID())).toString(), SendReturnMsgType.SystemMsg);
		// this.addMsg(SendReturnMsgFlag.VarToNodeName, nd.getName(),
		// nd.getName(), SendReturnMsgType.SystemMsg);
		return nd;
	}

	/**
	 * 知否执行了跳转.
	 * 
	 */
	public boolean IsSkip = false;

	/**
	 * 获取下一步骤的工作节点.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final Node NodeSend_GenerNextStepNode_Ext1() throws Exception {
		// 如果要是跳转到的节点，自动跳转规则规则就会失效。
		if (this.JumpToNode != null) {
			return this.JumpToNode;
		}

		Node mynd = this.getHisNode();
		Work mywork = this.getHisWork();
		int beforeSkipNodeID = 0; // added by liuxc,2015-7-13,标识自动跳转之前的节点ID
	
		this.setndFrom(this.getHisNode());
		while (true) {
			// 上一步的工作节点.
			int prvNodeID = mynd.getNodeID();
			if (mynd.getIsEndNode()) {
				// 如果是最后一个节点了,仍然找不到下一步节点...
				if (this.getHisGenerWorkFlow().getTransferCustomType() == TransferCustomType.ByCCBPMDefine) {
					this.setIsStopFlow(true);
				}
				return mynd;
			}

			// 获取它的下一步节点.
			Node nd = this.NodeSend_GenerNextStepNode_Ext(mynd);
			nd.WorkID=this.getWorkID(); // 未获得表单ID。
		
			mynd = nd;
			Work skipWork = null;
			if (!nd.getNodeFrmID().equals(mywork.NodeFrmID)) {
				// 跳过去的节点也要写入数据，不然会造成签名错误。
				skipWork = nd.getHisWork();

				if (!skipWork.getEnMap().getPhysicsTable().equals(this.rptGe.getEnMap().getPhysicsTable())) {
					skipWork.Copy(this.rptGe);
					skipWork.Copy(mywork);

					skipWork.setOID(this.getWorkID());
					skipWork.setRec(this.getExecer());
					skipWork.SetValByKey(WorkAttr.RDT, DataType.getCurrentDataTimess());
					skipWork.SetValByKey(WorkAttr.CDT, DataType.getCurrentDataTimess());
					skipWork.ResetDefaultVal();

					// 把里面的默认值也copy报表里面去.
					rptGe.Copy(skipWork);

					// 如果存在就修改
					if (skipWork.IsExit(skipWork.getPK(), this.getWorkID()) == true) {
						skipWork.DirectUpdate();
					} else {
						skipWork.InsertAsOID(this.getWorkID());
					}
				}
 
				/// #endregion 初始化发起的工作节点.

				IsSkip = true;
				mywork = skipWork;
			}

			// 如果没有设置跳转规则，就返回他们.
			if (nd.getAutoJumpRole0() == false && nd.getAutoJumpRole1() == false && nd.getAutoJumpRole2() == false
					&& nd.getHisWhenNoWorker() == false) {
				return nd;
			}

			DataTable dt = null;
			FindWorker fw = new FindWorker();
			WorkNode toWn = new WorkNode(this.getWorkID(), nd.getNodeID());
			if (skipWork == null) {
				skipWork = toWn.getHisWork();
			}

			dt = fw.DoIt(this.getHisFlow(), this, toWn); // 找到下一步骤的接受人.
			if (dt == null || dt.Rows.size() == 0) {
				if (nd.getHisWhenNoWorker() == true) {
					this.AddToTrack(ActionType.Skip, this.getExecer(), this.getExecerName(), nd.getNodeID(),
							nd.getName(), "自动跳转.(当没有找到处理人时)", getndFrom());
					setndFrom(nd);
					continue;
				} else {
					// 抛出异常.
					throw new RuntimeException("@没有找到节点[" + nd.getName() + "]的工作处理人.");
				}
			}

			if (dt.Rows.size() == 0) {
				throw new RuntimeException("@没有找到下一个节点(" + nd.getName() + ")的处理人");
			}

			if (nd.getAutoJumpRole0()) {
				// 处理人就是发起人
				boolean isHave = false;
				for (DataRow dr : dt.Rows) {
					// 如果出现了 处理人就是发起人的情况.
					if (dr.getValue(0).toString().equals(this.getHisGenerWorkFlow().getStarter())) {
						// #region 处理签名，让签名的人是发起人。
						Attrs attrs = skipWork.getEnMap().getAttrs();
						boolean isUpdate = false;
						for (Attr attr : attrs) {
							if (attr.getUIIsReadonly() && attr.getUIVisible() == true) {
								if ("@WebUser.No".equals(attr.getDefaultValOfReal())) {
									skipWork.SetValByKey(attr.getKey(), this.getHisGenerWorkFlow().getStarter());
									isUpdate = true;
								}
								if ("@WebUser.Name".equals(attr.getDefaultValOfReal())) {
									skipWork.SetValByKey(attr.getKey(), this.getHisGenerWorkFlow().getStarterName());
									isUpdate = true;
								}
								if ("@WebUser.FK_Dept".equals(attr.getDefaultValOfReal())) {
									skipWork.SetValByKey(attr.getKey(), this.getHisGenerWorkFlow().getFK_Dept());
									isUpdate = true;
								}
								if ("@WebUser.DeptName".equals(attr.getDefaultValOfReal())) {
									skipWork.SetValByKey(attr.getKey(), this.getHisGenerWorkFlow().getDeptName());
									isUpdate = true;
								}
							}
						}
						if (isUpdate) {
							skipWork.DirectUpdate();
						}
						// #endregion 处理签名，让签名的人是发起人。

						isHave = true;
						break;
					}
				}

				if (isHave == true) {
					// 如果发现了，当前人员包含处理人集合.
					this.AddToTrack(ActionType.Skip, this.getExecer(), this.getExecerName(), nd.getNodeID(),
							nd.getName(), "自动跳转,(处理人就是提交人)", getndFrom());
					this.getHisFlow().DoFlowEventEntity(EventListOfNode.SendWhen, nd,skipWork, null);
					setndFrom(nd);
					//发送成功后事件.
					this.getHisFlow().DoFlowEventEntity(EventListOfNode.SendSuccess, nd,skipWork, null,this.HisMsgObjs);
					continue;
				}

				// 如果没有跳转,判断是否,其他两个条件是否设置.
				if (nd.getAutoJumpRole1() == false && nd.getAutoJumpRole2() == false) {
					return nd;
				}
			}

			if (nd.getAutoJumpRole1()) {
				// 处理人已经出现过
				boolean isHave = false;
				for (DataRow dr : dt.Rows) {
					// 如果出现了处理人就是提交人的情况.
					String sql = "SELECT COUNT(*) FROM WF_GenerWorkerList WHERE FK_Emp='" + dr.getValue(0).toString()
							+ "' AND WorkID=" + this.getWorkID();
					if (DBAccess.RunSQLReturnValInt(sql) == 1) {
						// 这里不处理签名.
						isHave = true;
						break;
					}
				}

				if (isHave == true) {
					this.AddToTrack(ActionType.Skip, this.getExecer(), this.getExecerName(), nd.getNodeID(),
							nd.getName(), "自动跳转.(处理人已经出现过)", getndFrom());
					this.getHisFlow().DoFlowEventEntity(EventListOfNode.SendWhen, nd,skipWork, null);
					setndFrom(nd);
					// 执行发送.
					this.getHisFlow().DoFlowEventEntity(EventListOfNode.SendSuccess, nd,skipWork, null,this.HisMsgObjs);
					continue;
				}

				// 如果没有跳转,判断是否,其他两个条件是否设置.
				if (nd.getAutoJumpRole2() == false) {
					return nd;
				}
			}

			if (nd.getAutoJumpRole2()) {
				// 处理人与上一步相同
				boolean isHave = false;
				for (DataRow dr : dt.Rows) {
					String sql = "SELECT COUNT(*) FROM WF_GenerWorkerList WHERE FK_Emp='" + dr.getValue(0).toString() + "' AND WorkID="
							+ this.getWorkID() + " AND FK_Node="
							+ (beforeSkipNodeID > 0 ? beforeSkipNodeID : prvNodeID); // edited
																						// by
																						// liuxc,2015-7-13
					if (DBAccess.RunSQLReturnValInt(sql) == 1) {
						// 这里不处理签名.
						isHave = true;
						break;
					}
				}

				if (isHave == true) {
					// added by liuxc,2015-7-13,生成跳过的节点数据
					// 记录最开始相同处理人的节点ID，用来上面查找SQL判断
					if (beforeSkipNodeID == 0) {
						beforeSkipNodeID = prvNodeID;
					}

					Work wk = nd.getHisWork();
					wk.Copy(this.rptGe);
					wk.setRec(WebUser.getNo());
					wk.setOID(this.getWorkID());
					wk.ResetDefaultVal();

					// 执行表单填充，如果有，修改新昌方面同时修改本版本，added by liuxc,2015-10-16
					Object tempVar = nd.getMapData().getMapExts().GetEntityByKey(MapExtAttr.ExtType,
							MapExtXmlList.PageLoadFull);
					MapExt item = (MapExt) ((tempVar instanceof MapExt) ? tempVar : null);
					BP.WF.Glo.DealPageLoadFull(wk, item, nd.getMapData().getMapAttrs(), nd.getMapData().getMapDtls());

					wk.DirectSave();

					// added by liuxc,2015-10-16

					/// #region
					/// //此处时，跳转的节点如果有签章，则签章路径会计算错误，需要重新计算一下签章路径，暂时没找到好法子，将UCEn.ascx.cs中的计算签章的逻辑挪过来使用
					FrmImgs imgs = new FrmImgs();
					imgs.Retrieve(FrmImgAttr.FK_MapData, "ND" + nd.getNodeID(), FrmImgAttr.ImgAppType, 1,
							FrmImgAttr.IsEdit, 1);

					for (FrmImg img : imgs.ToJavaList()) {
						// 获取登录人岗位
						String stationNo = "";
						// 签章对应部门
						String fk_dept = WebUser.getFK_Dept();
						// 部门来源类别
						String sealType = "0";
						// 签章对应岗位
						String fk_station = img.getTag0();
						// 表单字段
						String sealField = "";
						String imgSrc = "";
						String sql = "";

						// 如果设置了部门与岗位的集合进行拆分
						if (!StringHelper.isNullOrEmpty(img.getTag0()) && img.getTag0().contains("^")
								&& img.getTag0().split("[^]", -1).length == 4) {
							fk_dept = img.getTag0().split("[^]", -1)[0];
							fk_station = img.getTag0().split("[^]", -1)[1];
							sealType = img.getTag0().split("[^]", -1)[2];
							sealField = img.getTag0().split("[^]", -1)[3];
							// 如果部门没有设定，就获取部门来源
							if (fk_dept.equals("all")) {
								// 发起人
								if (sealType.equals("1")) {
									sql = "SELECT FK_Dept FROM WF_GenerWorkFlow WHERE WorkID=" + wk.getOID();
									fk_dept = BP.DA.DBAccess.RunSQLReturnString(sql);
								}
								// 表单字段
								if (sealType.equals("2") && !DotNetToJavaStringHelper.isNullOrEmpty(sealField)) {
									// 判断字段是否存在
									for (MapAttr attr : nd.getMapData().getMapAttrs().ToJavaList()) {
										if (sealField.equals(attr.getKeyOfEn())) {
											fk_dept = wk.GetValStrByKey(sealField);
											break;
										}
									}
								}
							}
						}

						sql = String.format(
								" select FK_Station from Port_DeptStation where FK_Dept ='%1$s' and FK_Station in (select FK_Station from "
										+ BP.WF.Glo.getEmpStation() + " where FK_Emp='%2$s')",
								fk_dept, WebUser.getNo());
						dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
						for (DataRow dr : dt.Rows) {
							if (fk_station.contains(dr.get(0) + ",")) {
								stationNo = dr.get(0).toString();
								break;
							}
						}

						try {
							imgSrc = BP.WF.Glo.getCCFlowAppPath() + "DataUser/Seal/" + fk_dept + "_" + stationNo
									+ ".jpg";
							// 设置主键
							String myPK = DotNetToJavaStringHelper.isNullOrEmpty(img.getEnPK()) ? "seal"
									: img.getEnPK();
							myPK = myPK + "_" + wk.getOID() + "_" + img.getMyPK();

							FrmEleDB imgDb = new FrmEleDB();
							QueryObject queryInfo = new QueryObject(imgDb);
							queryInfo.AddWhere(FrmEleAttr.MyPK, myPK);
							queryInfo.DoQuery();
							// 判断是否存在
							if (imgDb != null && !DotNetToJavaStringHelper.isNullOrEmpty(imgDb.getFK_MapData())) {
								imgDb.setFK_MapData(StringHelper.isNullOrEmpty(img.getEnPK()) ? "seal" : img.getEnPK());
								imgDb.setEleID(String.valueOf(wk.getOID()));
								imgDb.setRefPKVal(img.getMyPK());
								imgDb.setTag1(imgSrc);
								imgDb.Update();
							} else {
								imgDb.setFK_MapData(StringHelper.isNullOrEmpty(img.getEnPK()) ? "seal" : img.getEnPK());
								imgDb.setEleID(String.valueOf(wk.getOID()));
								imgDb.setRefPKVal(img.getMyPK());
								imgDb.setTag1(imgSrc);
								imgDb.Insert();
							}
						} catch (RuntimeException ex) {
						}
					}

					/// #endregion

					this.AddToTrack(ActionType.Skip, this.getExecer(), this.getExecerName(), nd.getNodeID(),
							nd.getName(), "自动跳转.(处理人与上一步相同)", getndFrom());
					this.getHisFlow().DoFlowEventEntity(EventListOfNode.SendWhen, nd,wk, null);
					setndFrom(nd);
					// 执行发送.
					this.getHisFlow().DoFlowEventEntity(EventListOfNode.SendSuccess, nd,wk, null,this.HisMsgObjs);
					continue;
				}

				// 没有跳出转的条件，就返回本身.
				return nd;
			}

			mynd = nd;
			setndFrom(nd);
		} // 结束循环。
	}

	/**
	 * 处理OrderTeamup退回模式
	 * @throws Exception 
	 * 
	 */
	public final void DealReturnOrderTeamup() throws Exception {
		// 如果协作，顺序方式.
		GenerWorkerList gwl = new GenerWorkerList();
		gwl.setFK_Emp(WebUser.getNo());
		gwl.setFK_Node(this.getHisNode().getNodeID());
		gwl.setWorkID(this.getWorkID());
		if (gwl.RetrieveFromDBSources() == 0) {
			throw new RuntimeException("@没有找到自己期望的数据.");
		}
		gwl.setIsPass(true);
		gwl.Update();

		gwl.setFK_Emp(this.JumpToEmp);
		gwl.setFK_Node(this.JumpToNode.getNodeID());
		gwl.setWorkID(this.getWorkID());
		if (gwl.RetrieveFromDBSources() == 0) {
			throw new RuntimeException("@没有找到接受人期望的数据.");
		}

		gwl.setIsPass(false);
		gwl.Update();
		GenerWorkerLists ens = new GenerWorkerLists();
		ens.AddEntity(gwl);
		this.HisWorkerLists = ens;

		this.addMsg(SendReturnMsgFlag.VarAcceptersID, gwl.getFK_Emp(), gwl.getFK_Emp(), SendReturnMsgType.SystemMsg);
		this.addMsg(SendReturnMsgFlag.VarAcceptersName, gwl.getFK_EmpText(), gwl.getFK_EmpText(),
				SendReturnMsgType.SystemMsg);
		this.addMsg(SendReturnMsgFlag.OverCurr, "@当前工作已经发送给退回人(" + gwl.getFK_Emp() + "," + gwl.getFK_EmpText() + ").",
				null, SendReturnMsgType.Info);

		this.getHisGenerWorkFlow().setWFState(WFState.Runing);
		this.getHisGenerWorkFlow().setFK_Node(gwl.getFK_Node());
		this.getHisGenerWorkFlow().setNodeName(gwl.getFK_NodeText());

		this.getHisGenerWorkFlow().setTodoEmps(gwl.getFK_Emp());
		this.getHisGenerWorkFlow().setTodoEmpsNum(0);
		this.getHisGenerWorkFlow().setTaskSta(TaskSta.None);
		this.getHisGenerWorkFlow().Update();
	}

	/**
	 * 获取下一步骤的工作节点
	 * 
	 * @return
	 * @throws Exception 
	 */
	private Node NodeSend_GenerNextStepNode_Ext(Node currNode) throws Exception {
		// 检查当前的状态是是否是退回，.
		if (this.SendNodeWFState == WFState.ReturnSta) {
		}

		Nodes nds = currNode.getHisToNodes();
		if (nds.size() == 1) {
			Node toND = (Node) nds.get(0);
			return toND;
		}

		if (nds.size() == 0) {
			throw new RuntimeException("没有找到它的下了步节点.");
		}

		Conds dcsAll = new Conds();
		dcsAll.Retrieve(CondAttr.NodeID, currNode.getNodeID(), CondAttr.CondType, CondType.Dir.getValue(),
				CondAttr.PRI);
		if (dcsAll.size() == 0) {
			throw new RuntimeException("@没有为节点(" + currNode.getNodeID() + " , " + currNode.getName() + ")设置方向条件");
		}

		/// #region 获取能够通过的节点集合，如果没有设置方向条件就默认通过.
		Nodes myNodes = new Nodes();
		int toNodeId = 0;
		int numOfWay = 0;
		
		//this.rptGe.Retrieve();
		
		for (Node nd : nds.ToJavaList()) {
			Conds dcs = new Conds();
			for (Cond dc : dcsAll.ToJavaList()) {
				if (dc.getToNodeID() != nd.getNodeID()) {
					continue;
				}

				dc.setWorkID(this.getWorkID());
				dc.setFID(this.getHisWork().getFID());

				dc.en = this.rptGe;

				dcs.AddEntity(dc);
			}
 

			if (dcs.getIsPass()) // 如果通过了.
			{
				myNodes.AddEntity(nd);
			}
		}

		/// #endregion 获取能够通过的节点集合，如果没有设置方向条件就默认通过.

		// 如果没有找到.
		if (myNodes.size() == 0) {
			throw new RuntimeException("@当前用户(" + this.getExecerName() + "),定义节点的方向条件错误:从{" + currNode.getNodeID()
					+ currNode.getName() + "}节点到其它节点,定义的所有转向条件都不成立.");
		}

		// 如果找到1个.
		if (myNodes.size() == 1) {
			Node toND = (Node) ((myNodes.get(0) instanceof Node) ? myNodes.get(0) : null);
			return toND;
		}

		// 如果找到了多个.
		for (Cond dc : dcsAll.ToJavaList()) {
			for (Node myND : myNodes.ToJavaList()) {
				if (dc.getToNodeID() == myND.getNodeID()) {
					return myND;
				}
			}
		}
		throw new RuntimeException("@有两条以上的分支(含两条)未设置方向条件,程序停止.");
	}

	/**
	 * 获取下一步骤的节点集合
	 * 
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public final Nodes Func_GenerNextStepNodes() throws NumberFormatException, Exception {
		// 如果跳转节点已经有了变量.
		if (this.JumpToNode != null) {
			Nodes myNodesTo = new Nodes();
			myNodesTo.AddEntity(this.JumpToNode);
			return myNodesTo;
		}

		if (this.getHisNode().getCondModel() == CondModel.ByUserSelected) {
			// 获取用户选择的节点.
			String nodes = this.getHisGenerWorkFlow().getParas_ToNodes();
			if (DotNetToJavaStringHelper.isNullOrEmpty(nodes)) {
				throw new RuntimeException("@用户没有选择发送到的节点.");
			}

			Nodes nds = new Nodes();
			String[] mynodes = nodes.split("[,]", -1);
			for (String item : mynodes) {
				if (DotNetToJavaStringHelper.isNullOrEmpty(item)) {
					continue;
				}
				nds.AddEntity(new Node(Integer.parseInt(item)));
			}
			return nds;
		}

		// 延续子流程添加 ddl
		// 判断是否有延续流程.

		Nodes toNodes = this.getHisNode().getHisToNodes();

		// 如果只有一个转向节点, 就不用判断条件了,直接转向他.
		if (toNodes.size() == 1) {
			return toNodes;
		}
		Conds dcsAll = new Conds();
		dcsAll.Retrieve(CondAttr.NodeID, this.getHisNode().getNodeID(), CondAttr.PRI);

		/// #region 获取能够通过的节点集合，如果没有设置方向条件就默认通过.
		Nodes myNodes = new Nodes();
		int toNodeId = 0;
		int numOfWay = 0;

		for (Node nd : toNodes.ToJavaList()) {
			Conds dcs = new Conds();
			for (Cond dc : dcsAll.ToJavaList()) {
				if (dc.getToNodeID() != nd.getNodeID()) {
					continue;
				}

				dc.setWorkID(this.getHisWork().getOID());
				dc.en = this.rptGe;
				dcs.AddEntity(dc);
			}

			if (dcs.size() == 0) {
				myNodes.AddEntity(nd);
				continue;
			}

			if (dcs.getIsPass()) // 如果多个转向条件中有一个成立.
			{
				myNodes.AddEntity(nd);
				continue;
			}
		}

		/// #endregion 获取能够通过的节点集合，如果没有设置方向条件就默认通过.

		if (myNodes.size() == 0) {
			throw new RuntimeException(String.format("@定义节点的方向条件错误:没有给从%1$s节点到其它节点,定义转向条件或者您定义的所有转向条件都不成立.",
					this.getHisNode().getNodeID() + this.getHisNode().getName()));
		}
		return myNodes;
	}

	/**
	 * 检查一下流程完成条件.
	 * 
	 * @return
	 * @throws Exception 
	 */
	private void Func_CheckCompleteCondition() throws Exception {
		if (this.getHisNode().getHisRunModel() == RunModel.SubThread) {
			throw new RuntimeException("@流程设计错误：不允许在子线程上设置流程完成条件。");
		}

		this.setIsStopFlow(false);
		this.addMsg("CurrWorkOver", String.format("当前工作[%1$s]已经完成", this.getHisNode().getName()));

		/// #region 判断流程条件.
		try {
			if (this.getHisNode().getHisToNodes().size() == 0 && this.getHisNode().getIsStartNode()) {
				// 如果流程完成
				String overMsg = this.getHisWorkFlow().DoFlowOver(ActionType.FlowOver, "符合流程完成条件", this.getHisNode(),
						this.rptGe);

				if (this.getHisGenerWorkFlow().getTransferCustomType() == TransferCustomType.ByCCBPMDefine) {
					this.setIsStopFlow(true);
				}
				this.addMsg("OneNodeFlowOver", "@工作已经成功处理(一个流程的工作)。");
			}

			if (this.getHisNode().getIsCCFlow() && this.getHisFlowCompleteConditions().getIsPass()) {
				// 如果有流程完成条件，并且流程完成条件是通过的。
				String stopMsg = this.getHisFlowCompleteConditions().getConditionDesc();
				// 如果流程完成
				String overMsg = this.getHisWorkFlow().DoFlowOver(ActionType.FlowOver, "符合流程完成条件:" + stopMsg,
						this.getHisNode(), this.rptGe);
				this.setIsStopFlow(true);

				// string path = BP.Sys.Glo.Request.ApplicationPath;
				String mymsg = "@符合工作流程完成条件" + stopMsg + "" + overMsg;
				// string mymsgHtml = mymsg + "@查看<img src='" + VirPath +
				// "WF/Img/Btn/PrintWorkRpt.gif' ><a href='" + VirPath +
				// "WF/WFRpt.jsp?WorkID=" + this.HisWork.OID + "&FID=" +
				// this.HisWork.FID + "&FK_Flow=" + this.HisNode.FK_Flow + "'
				// target='_self' >工作轨迹</a>";
				this.addMsg(SendReturnMsgFlag.FlowOver, mymsg, mymsg, SendReturnMsgType.Info);
			}
		} catch (RuntimeException ex) {
			throw new RuntimeException(
					String.format("@判断流程{0}完成条件出现错误." + ex.getMessage(), this.getHisNode().getName()));
		}

		/// #endregion
	}

	/**
	 * 设置当前工作已经完成.
	 * 
	 * @return
	 * @throws Exception 
	 */
	private String Func_DoSetThisWorkOver() throws Exception {
		// 设置结束人.
		this.rptGe.SetValByKey(GERptAttr.FK_Dept, this.getHisGenerWorkFlow().getFK_Dept()); // 此值不能变化.
		this.rptGe.SetValByKey(GERptAttr.FlowEnder, this.getExecer());
		this.rptGe.SetValByKey(GERptAttr.FlowEnderRDT, DataType.getCurrentDataTime());
		if (this.town == null) {
			this.rptGe.SetValByKey(GERptAttr.FlowEndNode, this.getHisNode().getNodeID());
		} else {
			if (this.getHisNode().getHisRunModel() == RunModel.FL
					|| this.getHisNode().getHisRunModel() == RunModel.FHL) {
				this.rptGe.SetValByKey(GERptAttr.FlowEndNode, this.getHisNode().getNodeID());
			} else {
				this.rptGe.SetValByKey(GERptAttr.FlowEndNode, this.town.getHisNode().getNodeID());
			}
		}

		this.rptGe.SetValByKey(GERptAttr.FlowDaySpan,
				DataType.GetSpanDays(rptGe.getFlowStartRDT(), DataType.getCurrentDataTime()));

		// 如果两个物理表不想等.
		if (!this.getHisWork().getEnMap().getPhysicsTable().equals(this.rptGe.getEnMap().getPhysicsTable())) {
			// 更新状态。
			this.getHisWork().SetValByKey("CDT", DataType.getCurrentDataTime());
			this.getHisWork().setRec(this.getExecer());

			// 判断是不是MD5流程？
			if (this.getHisFlow().getIsMD5()) {
				this.getHisWork().SetValByKey("MD5", Glo.GenerMD5(this.getHisWork()));
			}

			if (this.getHisNode().getIsStartNode()) {
				this.getHisWork().SetValByKey(StartWorkAttr.Title, this.getHisGenerWorkFlow().getTitle());
			}

			this.getHisWork().DirectUpdate();
		}

		/// #region 2014-08-02 删除了其他人员的待办，增加了 IsPass=0 参数.
		if (this.town != null && this.town.getHisNode().getNodeID() == this.getHisNode().getNodeID()) {
			// 清除其他的工作者.
			// ps.SQL = "DELETE FROM WF_GenerWorkerlist WHERE IsPass=0 AND
			// FK_Node=" + dbStr + "FK_Node AND WorkID=" + dbStr + "WorkID AND
			// FK_Emp <> " + dbStr + "FK_Emp";
			// ps.clear();
			// ps.Add("FK_Node", this.HisNode.NodeID);
			// ps.Add("WorkID", this.WorkID);
			// ps.Add("FK_Emp", this.Execer);
			// DBAccess.RunSQL(ps);
		} else {
			// 清除其他的工作者.
			ps.SQL = "DELETE FROM WF_GenerWorkerlist WHERE IsPass=0 AND FK_Node=" + dbStr + "FK_Node AND WorkID="
					+ dbStr + "WorkID AND FK_Emp <> " + dbStr + "FK_Emp";
			ps.clear();
			ps.Add("FK_Node", this.getHisNode().getNodeID());
			ps.Add("WorkID", this.getWorkID());
			ps.Add("FK_Emp", this.getExecer());
			DBAccess.RunSQL(ps);
		}

		/// #endregion 2014-08-02 删除了其他人员的待办，增加了 IsPass=0 参数.

		if (this.town != null && this.town.getHisNode().getNodeID() == this.getHisNode().getNodeID()) {
			// 如果不是当前节点发给当前节点，就更新上一个节点全部完成。
			ps = new Paras();
			ps.SQL = "UPDATE WF_GenerWorkerList SET IsPass=1 WHERE FK_Node=" + dbStr + "FK_Node AND WorkID=" + dbStr
					+ "WorkID AND FK_Emp=" + dbStr + "FK_Emp ";
			ps.Add("FK_Node", this.getHisNode().getNodeID());
			ps.Add("WorkID", this.getWorkID());
			ps.Add("FK_Emp", this.getExecer());
			DBAccess.RunSQL(ps);
		} else {
			// 如果不是当前节点发给当前节点，就更新上一个节点全部完成。
			ps = new Paras();
			ps.SQL = "UPDATE WF_GenerWorkerList SET IsPass=1 WHERE FK_Node=" + dbStr + "FK_Node AND WorkID=" + dbStr
					+ "WorkID";
			ps.Add("FK_Node", this.getHisNode().getNodeID());
			ps.Add("WorkID", this.getWorkID());
			DBAccess.RunSQL(ps);
		}

		// 给generworkflow赋值。
		if (this.getIsStopFlow() == true) {
			this.getHisGenerWorkFlow().setWFState(WFState.Complete);
		} else {
			this.getHisGenerWorkFlow().setWFState(WFState.Runing);
		}

		// 流程应完成时间。
		if (this.getHisWork().getEnMap().getAttrs().Contains(WorkSysFieldAttr.SysSDTOfFlow)) {
			this.getHisGenerWorkFlow().setSDTOfFlow(this.getHisWork().GetValStrByKey(WorkSysFieldAttr.SysSDTOfFlow));
		}

		// 下一个节点应完成时间。
		if (this.getHisWork().getEnMap().getAttrs().Contains(WorkSysFieldAttr.SysSDTOfNode)) {
			this.getHisGenerWorkFlow().setSDTOfFlow(this.getHisWork().GetValStrByKey(WorkSysFieldAttr.SysSDTOfNode));
		}

		// 执行更新。
		if (this.getIsStopFlow() == false) {
			this.getHisGenerWorkFlow().Update();
		}

		return "@流程已经完成.";
	}

	/// #endregion 附属功能
	/**
	 * 普通节点到普通节点
	 * 
	 * @param toND
	 *            要到达的下一个节点
	 * @return 执行消息
	 * @throws Exception 
	 */
	private void NodeSend_11(Node toND) throws Exception {
		String sql = "";
		String errMsg = "";
		Work toWK = toND.getHisWork();
		toWK.setOID(this.getWorkID());
		toWK.setFID(this.getHisWork().getFID());

		// 如果执行了跳转.
		if (this.IsSkip == true) {
			toWK.RetrieveFromDBSources(); // 有可能是跳转.
		}

		/// #region 执行数据初始化
		// town.
		WorkNode town = new WorkNode(toWK, toND);

		errMsg = "初试化他们的工作人员 - 期间出现错误.";

		// 初试化他们的工作人员．
		current_gwls = this.Func_GenerWorkerLists(town);
		if (town.getHisNode().getTodolistModel() == TodolistModel.Order && current_gwls.size() > 1) {
			// 如果到达的节点是队列流程节点，就要设置他们的队列顺序.
			int idx = 0;
			for (GenerWorkerList gwl : current_gwls.ToJavaList()) {
				idx++;
				if (idx == 1) {
					continue;
				}
				gwl.setIsPassInt(idx + 100);
				gwl.Update();
			}
		}

		/// #region 保存目标节点数据.
		if (!this.getHisWork().getEnMap().getPhysicsTable().equals(toWK.getEnMap().getPhysicsTable())) {
			errMsg = "保存目标节点数据 - 期间出现错误.";

			// 为下一步骤初始化数据.
			GenerWorkerList gwl = (GenerWorkerList) ((current_gwls.get(0) instanceof GenerWorkerList)
					? current_gwls.get(0) : null);
			toWK.setRec(gwl.getFK_Emp());
			String emps = gwl.getFK_Emp();
			if (current_gwls.size() != 1) {
				for (GenerWorkerList item : current_gwls.ToJavaList()) {
					emps += item.getFK_Emp() + ",";
				}
			}
			toWK.setEmps(emps);

			try {
				if (this.IsSkip == true) {
					toWK.DirectUpdate(); // 如果执行了跳转.
				} else {
					toWK.DirectInsert();
				}
			} catch (RuntimeException ex) {
				Log.DefaultLogWriteLineInfo("@出现SQL异常有可能是没有修复表，或者重复发送. Ext=" + ex.getMessage());
				try {
					toWK.CheckPhysicsTable();
					toWK.DirectUpdate();
				} catch (RuntimeException ex1) {
					Log.DefaultLogWriteLineInfo("@保存工作错误：" + ex1.getMessage());
					throw new RuntimeException("@保存工作错误：" + toWK.getEnDesc() + ex1.getMessage());
				}
			}
		}

		/// #endregion 保存目标节点数据.

		// @加入消息集合里。
		this.SendMsgToThem(current_gwls);

		if (toND.getIsGuestNode() == true) {
			String htmlInfo = String.format("@发送给如下%1$s位处理人,%2$s.",
					(new Integer(this.HisRememberMe.getNumOfObjs())).toString(),
					this.getHisGenerWorkFlow().getGuestNo() + " " + this.getHisGenerWorkFlow().getGuestName());

			String textInfo = String.format("@发送给如下%1$s位处理人,%2$s.",
					(new Integer(this.HisRememberMe.getNumOfObjs())).toString(),
					this.getHisGenerWorkFlow().getGuestName());

			this.addMsg(SendReturnMsgFlag.ToEmps, textInfo, htmlInfo);
		} else {
			String htmlInfo = String.format("@发送给如下%1$s位处理人,%2$s.",
					(new Integer(this.HisRememberMe.getNumOfObjs())).toString(), this.HisRememberMe.getEmpsExt());

			String textInfo = String.format("@发送给如下%1$s位处理人,%2$s.",
					(new Integer(this.HisRememberMe.getNumOfObjs())).toString(), this.HisRememberMe.getObjsExt());

			this.addMsg(SendReturnMsgFlag.ToEmps, textInfo, htmlInfo);

		}

		/// #region 处理审核问题,更新审核组件插入的审核意见中的 到节点，到人员。
		try {
			Paras ps = new Paras();
			ps.SQL = "UPDATE ND" + Integer.parseInt(toND.getFK_Flow()) + "Track SET NDTo=" + dbStr + "NDTo,NDToT="
					+ dbStr + "NDToT,EmpTo=" + dbStr + "EmpTo,EmpToT=" + dbStr + "EmpToT WHERE NDFrom=" + dbStr
					+ "NDFrom AND EmpFrom=" + dbStr + "EmpFrom AND WorkID=" + dbStr + "WorkID AND ActionType="
					+ ActionType.WorkCheck.getValue();
			ps.Add(TrackAttr.NDTo, toND.getNodeID());
			ps.Add(TrackAttr.NDToT, toND.getName());
			ps.Add(TrackAttr.EmpTo, this.HisRememberMe.getEmpsExt());
			ps.Add(TrackAttr.EmpToT, this.HisRememberMe.getEmpsExt());
			ps.Add(TrackAttr.NDFrom, this.getHisNode().getNodeID());
			ps.Add(TrackAttr.EmpFrom, WebUser.getNo());
			ps.Add(TrackAttr.WorkID, this.getWorkID());
			BP.DA.DBAccess.RunSQL(ps);
		} catch (RuntimeException ex) {

			/// #region 如果更新失败，可能是由于数据字段大小引起。
			Flow flow = new Flow(toND.getFK_Flow());

			String updateLengthSql = String.format("  alter table %1$s alter column %2$s varchar(2000) ",
					"ND" + Integer.parseInt(toND.getFK_Flow()) + "Track", "EmpFromT");
			BP.DA.DBAccess.RunSQL(updateLengthSql);

			updateLengthSql = String.format("  alter table %1$s alter column %2$s varchar(2000) ",
					"ND" + Integer.parseInt(toND.getFK_Flow()) + "Track", "EmpFrom");
			BP.DA.DBAccess.RunSQL(updateLengthSql);

			updateLengthSql = String.format("  alter table %1$s alter column %2$s varchar(2000) ",
					"ND" + Integer.parseInt(toND.getFK_Flow()) + "Track", "EmpTo");
			BP.DA.DBAccess.RunSQL(updateLengthSql);
			updateLengthSql = String.format("  alter table %1$s alter column %2$s varchar(2000) ",
					"ND" + Integer.parseInt(toND.getFK_Flow()) + "Track", "EmpToT");
			BP.DA.DBAccess.RunSQL(updateLengthSql);

			Paras ps = new Paras();
			ps.SQL = "UPDATE ND" + Integer.parseInt(toND.getFK_Flow()) + "Track SET NDTo=" + dbStr + "NDTo,NDToT="
					+ dbStr + "NDToT,EmpTo=" + dbStr + "EmpTo,EmpToT=" + dbStr + "EmpToT WHERE NDFrom=" + dbStr
					+ "NDFrom AND EmpFrom=" + dbStr + "EmpFrom AND WorkID=" + dbStr + "WorkID AND ActionType="
					+ ActionType.WorkCheck.getValue();
			ps.Add(TrackAttr.NDTo, toND.getNodeID());
			ps.Add(TrackAttr.NDToT, toND.getName());
			ps.Add(TrackAttr.EmpTo, this.HisRememberMe.getEmpsExt());
			ps.Add(TrackAttr.EmpToT, this.HisRememberMe.getEmpsExt());
			ps.Add(TrackAttr.NDFrom, this.getHisNode().getNodeID());
			ps.Add(TrackAttr.EmpFrom, WebUser.getNo());
			ps.Add(TrackAttr.WorkID, this.getWorkID());
			BP.DA.DBAccess.RunSQL(ps);

			/// #endregion
		}

		/// #endregion 处理审核问题.

		// string htmlInfo = string.Format("@任务自动发送给{0}如下处理人{1}.",
		// this.nextStationName,this._RememberMe.EmpsExt);
		// string textInfo = string.Format("@任务自动发送给{0}如下处理人{1}.",
		// this.nextStationName,this._RememberMe.ObjsExt);

		if (1==2 && this.HisWorkerLists.size() >= 2 && this.getHisNode().getIsTask()) {
			if (WebUser.getIsWap()) {
				this.addMsg(SendReturnMsgFlag.AllotTask, null,
						"<a href=\"" + this.getVirPath() + "WF/WorkOpt/AllotTask.jsp?WorkID=" + this.getWorkID()
								+ "&NodeID=" + toND.getNodeID() + "&FK_Flow=" + toND.getFK_Flow() + "')\"><img src='"
								+ getVirPath() + "WF/Img/AllotTask.gif' border=0/>指定特定的处理人处理</a>。",
						SendReturnMsgType.Info);
			} else {
				this.addMsg(SendReturnMsgFlag.AllotTask, null,
						"<a href=\"javascript:WinOpen('" + getVirPath() + "WF/WorkOpt/AllotTask.jsp?WorkID="
								+ this.getWorkID() + "&NodeID=" + toND.getNodeID() + "&FK_Flow=" + toND.getFK_Flow()
								+ "')\"><img src='" + getVirPath() + "WF/Img/AllotTask.gif' border=0/>指定特定的处理人处理</a>。",
						SendReturnMsgType.Info);
			}
		}

		// if (WebUser.IsWap == false)
		// this.addMsg(SendReturnMsgFlag.ToEmpExt, null, "@<a
		// href=\"javascript:WinOpen('" + VirPath + "WF/Msg/SMS.jsp?WorkID=" +
		// this.WorkID + "&FK_Node=" + toND.NodeID + "');\" ><img src='" +
		// VirPath + "WF/Img/SMS.gif' border=0 />发手机短信提醒他(们)</a>",
		// SendReturnMsgType.Info);

		if ( 1==2 &&this.getHisNode().getHisFormType() != NodeFormType.SDKForm ) {
			if (this.getHisNode().getIsStartNode()) {
				if (WebUser.getIsWap()) {
					this.addMsg(SendReturnMsgFlag.ToEmpExt, null,
							"@<a href='" + getVirPath() + "WF/Wap/MyFlowInfo.jsp?DoType=UnSend&WorkID="
									+ this.getHisWork().getOID() + "&FK_Flow=" + toND.getFK_Flow() + "'><img src='"
									+ getVirPath() + "WF/Img/Action/UnSend.png' border=0/>撤销本次发送</a>， <a href='"
									+ getVirPath() + "WF/Wap/MyFlow.htm?FK_Flow=" + toND.getFK_Flow() + "&FK_Node="
									+ Integer.parseInt(toND.getFK_Flow()) + "01'><img src='" + getVirPath()
									+ "WF/Img/New.gif' border=0/>新建流程</a>。",
							SendReturnMsgType.Info);
				} else {
					this.addMsg(SendReturnMsgFlag.ToEmpExt, null,
							"@<a href='" + this.getVirPath() + this.getAppType()
									+ "/MyFlowInfo.jsp?DoType=UnSend&WorkID=" + this.getHisWork().getOID() + "&FK_Flow="
									+ toND.getFK_Flow() + "'><img src='" + getVirPath()
									+ "WF/Img/Action/UnSend.png' border=0/>撤销本次发送</a>， <a href='" + getVirPath()
									+ "WF/MyFlow.htm?FK_Flow=" + toND.getFK_Flow() + "&FK_Node="
									+ Integer.parseInt(toND.getFK_Flow()) + "01'><img src='" + getVirPath()
									+ "WF/Img/New.gif' border=0/>新建流程</a>。",
							SendReturnMsgType.Info);
				}
			} else {
				this.addMsg(SendReturnMsgFlag.ToEmpExt, null,
						"@<a href='" + this.getVirPath() + this.getAppType() + "/MyFlowInfo.jsp?DoType=UnSend&WorkID="
								+ this.getHisWork().getOID() + "&FK_Flow=" + toND.getFK_Flow() + "'><img src='"
								+ getVirPath() + "WF/Img/Action/UnSend.png' border=0/>撤销本次发送</a>。",
						SendReturnMsgType.Info);
			}
		}

		this.getHisGenerWorkFlow().setFK_Node(toND.getNodeID());
		this.getHisGenerWorkFlow().setNodeName(toND.getName());

		// ps = new Paras();
		// ps.SQL = "UPDATE WF_GenerWorkFlow SET WFState=" + (int)WFState.Runing
		// + ", FK_Node=" + dbStr + "FK_Node,NodeName=" + dbStr + "NodeName
		// WHERE WorkID=" + dbStr + "WorkID";
		// ps.Add("FK_Node", toND.NodeID);
		// ps.Add("NodeName", toND.Name);
		// ps.Add("WorkID", this.HisWork.OID);
		// DBAccess.RunSQL(ps);

		if (this.getHisNode().getHisFormType() == NodeFormType.SDKForm
				|| this.getHisNode().getHisFormType() == NodeFormType.SelfForm) {
		} else {
			//this.addMsg(SendReturnMsgFlag.WorkRpt, null,
			//		"@<img src='" + getVirPath() + "WF/Img/Btn/PrintWorkRpt.gif' ><a href='" + getVirPath()
			//				+ "WF/WFRpt.jsp?WorkID=" + this.getHisWork().getOID() + "&FID=" + this.getHisWork().getFID()
			//				+ "&FK_Flow=" + toND.getFK_Flow() + "' target='_self' >工作轨迹</a>。");
		}
		this.addMsg(SendReturnMsgFlag.WorkStartNode, "@下一步[" + toND.getName() + "]工作成功启动.",
				"@下一步<font color=blue>[" + toND.getName() + "]</font>工作成功启动.");

		/// #endregion

		/// #region 初始化发起的工作节点。

		
		if (this.getHisWork().getEnMap().getPhysicsTable().equals(  toWK.getEnMap().getPhysicsTable()) ) {
			// 这是数据合并模式, 就不执行copy
			this.CopyData(toWK, toND, true);
		} else {
			// 如果两个数据源不想等，就执行copy。
			this.CopyData(toWK, toND, false);
		}

		/// #endregion 初始化发起的工作节点.

		/// #region 判断是否是质量评价。
		if (toND.getIsEval()) {
			// 如果是质量评价流程
			toWK.SetValByKey(WorkSysFieldAttr.EvalEmpNo, this.getExecer());
			toWK.SetValByKey(WorkSysFieldAttr.EvalEmpName, this.getExecerName());
			toWK.SetValByKey(WorkSysFieldAttr.EvalCent, 0);
			toWK.SetValByKey(WorkSysFieldAttr.EvalNote, "");
		}

		/// #endregion

	}
 

	/**
	 * 处理分流点向下发送 to 异表单.
	 * 
	 * @return
	 * @throws Exception 
	 */
	private void NodeSend_24_UnSameSheet(Nodes toNDs) throws Exception {
		 

		// 分别启动每个节点的信息.
		String msg = "";

		// #region 查询出来当前流程节点数据，为子线程的节点copy数据所用。
		// 查询出来上一个节点的附件信息来。
		FrmAttachmentDBs athDBs = new FrmAttachmentDBs("ND" + this.getHisNode().getNodeID(),
				(new Long(this.getWorkID())).toString());
		// 查询出来上一个Ele信息来。
		FrmEleDBs eleDBs = new FrmEleDBs("ND" + this.getHisNode().getNodeID(), (new Long(this.getWorkID())).toString());

		// #endregion

		// 定义系统变量.
		String workIDs = "";
		String empIDs = "";
		String empNames = "";
		String toNodeIDs = "";

		for (Node nd : toNDs.ToJavaList()) {
			msg += "@" + nd.getName() + "工作已经启动，处理工作者：";

			// 产生一个工作信息。
			Work wk = nd.getHisWork();
			wk.Copy(this.getHisWork());
			wk.setFID(this.getHisWork().getOID());
			wk.setOID(BP.DA.DBAccess.GenerOID("WorkID"));
			wk.BeforeSave();
			wk.DirectInsert();

			// 获得它的工作者。
			WorkNode town = new WorkNode(wk, nd);
			current_gwls = this.Func_GenerWorkerLists(town);
			if (current_gwls.size() == 0) {
				msg += "@" + nd.getName() + "工作已经启动，处理工作者：";
				msg += "节点:" + nd.getName() + "，没有找到可处理的人员，此线程节点无法启动。";
				wk.Delete();
				continue;
			}

			/// #region 执行数据copy.
			if (athDBs.size() > 0) {
				// 插入之前先删除.
				FrmAttachmentDB mydb = new FrmAttachmentDB();
				mydb.Delete(FrmAttachmentDBAttr.FK_MapData, "ND" + nd.getNodeID(), FrmAttachmentDBAttr.RefPKVal,
						wk.getOID());

				// 说明当前节点有附件数据
				int idx = 0;
				for (FrmAttachmentDB athDB : athDBs.ToJavaList()) {
					idx++;
					FrmAttachmentDB athDB_N = new FrmAttachmentDB();
					athDB_N.Copy(athDB);
					athDB_N.setFK_MapData("ND" + nd.getNodeID());
					athDB_N.setMyPK(BP.DA.DBAccess.GenerGUID());
					athDB_N.setFK_FrmAttachment(athDB_N.getFK_FrmAttachment()
							.replace("ND" + this.getHisNode().getNodeID(), "ND" + nd.getNodeID()));
					athDB_N.setRefPKVal(String.valueOf(wk.getOID()));
					athDB_N.Insert();
				}
			}

			if (eleDBs.size() > 0) {
				// 说明当前节点有附件数据
				int idx = 0;
				for (FrmEleDB eleDB : eleDBs.ToJavaList()) {
					idx++;
					FrmEleDB eleDB_N = new FrmEleDB();
					eleDB_N.Copy(eleDB);
					eleDB_N.setFK_MapData("ND" + nd.getNodeID());
					eleDB_N.Insert();
				}
			}

			/// #endregion 执行数据copy.

			for (GenerWorkerList wl : current_gwls.ToJavaList()) {
				msg += wl.getFK_Emp() + "，" + wl.getFK_EmpText() + "、";
				// 产生工作的信息。
				GenerWorkFlow gwf = new GenerWorkFlow();
				gwf.setWorkID(wk.getOID());
				if (gwf.getIsExits() == false) {
					gwf.setFID(this.getWorkID());

					/// #warning 需要修改成标题生成规则。
					/// #warning 让子流程的Titlte与父流程的一样.

					gwf.setTitle(this.getHisGenerWorkFlow().getTitle()); // WorkNode.GenerTitle(this.rptGe);
					gwf.setWFState(WFState.Runing);
					gwf.setRDT(DataType.getCurrentDataTime());
					gwf.setStarter(this.getExecer());
					gwf.setStarterName(this.getExecerName());
					gwf.setFK_Flow(nd.getFK_Flow());
					gwf.setFlowName(nd.getFlowName());
					gwf.setFK_FlowSort(this.getHisNode().getHisFlow().getFK_FlowSort());
					gwf.setSysType(this.getHisNode().getHisFlow().getSysType());

					gwf.setFK_Node(nd.getNodeID());
					gwf.setNodeName(nd.getName());
					gwf.setFK_Dept(wl.getFK_Dept());
					gwf.setDeptName(wl.getFK_DeptT());
					gwf.setTodoEmps(wl.getFK_Emp() + "," + wl.getFK_EmpText());
					gwf.DirectInsert();
				}

				ps = new Paras();
				ps.SQL = "UPDATE WF_GenerWorkerlist SET WorkID=" + dbStr + "WorkID1,FID=" + dbStr + "FID WHERE FK_Emp="
						+ dbStr + "FK_Emp AND WorkID=" + dbStr + "WorkID2 AND FK_Node=" + dbStr + "FK_Node ";
				ps.Add("WorkID1", wk.getOID());
				ps.Add("FID", this.getWorkID());

				ps.Add("FK_Emp", wl.getFK_Emp());
				ps.Add("WorkID2", wl.getWorkID());
				ps.Add("FK_Node", wl.getFK_Node());
				DBAccess.RunSQL(ps);

				// 设置当前的workid.
				wl.setWorkID(wk.getOID());

				// 记录变量.
				workIDs += (new Long(wk.getOID())).toString() + ",";
				empIDs += wl.getFK_Emp() + ",";
				empNames += wl.getFK_EmpText() + ",";
				toNodeIDs += gwf.getFK_Node() + ",";

				// 更新工作信息.
				wk.setRec(wl.getFK_Emp());
				wk.setEmps("@" + wl.getFK_Emp());
				// wk.RDT = DataType.getCurrentDataTimess();
				wk.DirectUpdate();
			}
		}

		// 加入分流异表单，提示信息。
		this.addMsg("FenLiuUnSameSheet", msg);

		// 加入变量。
		this.addMsg(SendReturnMsgFlag.VarTreadWorkIDs, workIDs, workIDs, SendReturnMsgType.SystemMsg);
		this.addMsg(SendReturnMsgFlag.VarAcceptersID, empIDs, empIDs, SendReturnMsgType.SystemMsg);
		this.addMsg(SendReturnMsgFlag.VarAcceptersName, empNames, empNames, SendReturnMsgType.SystemMsg);
		this.addMsg(SendReturnMsgFlag.VarToNodeIDs, toNodeIDs, toNodeIDs, SendReturnMsgType.SystemMsg);
		
		
		  //写入日志.
        if (this.getHisNode().getIsStartNode()==true)
            this.AddToTrack(ActionType.Start, empIDs, empNames, this.getHisNode().getNodeID(), this.getHisNode().getName(), msg);
        else
            this.AddToTrack(ActionType.Forward, empIDs, empNames, this.getHisNode().getNodeID(), this.getHisNode().getName(), msg);
	}

	/**
	 * 产生分流点
	 * 
	 * @param toWN
	 * @return
	 */
	private GenerWorkerLists NodeSend_24_SameSheet_GenerWorkerList(WorkNode toWN) {
		return null;
	}

	/**
	 * 当前产生的接受人员列表.
	 * 
	 */
	private GenerWorkerLists current_gwls = null;

	/**
	 * 处理分流点向下发送 to 同表单.
	 * 
	 * @param toNode
	 *            到达的分流节点
	 * @throws Exception 
	 */
	private void NodeSend_24_SameSheet(Node toNode) throws Exception {
		if (this.getHisGenerWorkFlow().getTitle().equals("未生成")) {
			this.getHisGenerWorkFlow()
					.setTitle(BP.WF.WorkFlowBuessRole.GenerTitle(this.getHisFlow(), this.getHisWork()));
		}

		/// #region 删除到达节点的子线程如果有，防止退回信息垃圾数据问题,如果退回处理了这个部分就不需要处理了.
		ps = new Paras();
		ps.SQL = "DELETE FROM WF_GenerWorkerlist WHERE FID=" + dbStr + "FID  AND FK_Node=" + dbStr + "FK_Node";
		ps.Add("FID", this.getHisWork().getOID());
		ps.Add("FK_Node", toNode.getNodeID());

		/// #endregion 删除到达节点的子线程如果有，防止退回信息垃圾数据问题，如果退回处理了这个部分就不需要处理了.
  

		/// #endregion GenerFH

		/// #region 产生下一步骤的工作人员
		// 发起.
		Work wk = toNode.getHisWork();
		wk.Copy(this.rptGe);
		wk.Copy(this.getHisWork()); // 复制过来主表基础信息。
		wk.setFID(this.getHisWork().getOID()); // 把该工作FID设置成干流程上的工作ID.

		// 到达的节点.
		town = new WorkNode(wk, toNode);

		// 产生下一步骤要执行的人员.
		current_gwls = this.Func_GenerWorkerLists(town);

		// 给当前工作人员增加已经处理的历史步骤. add 2015-01-14,这样做的目的就是，可以让分流节点的发送人员看到每个子线程的在途工作.
		current_gwls.Delete(GenerWorkerListAttr.FK_Node, this.getHisNode().getNodeID(), GenerWorkerListAttr.FID,
				this.getWorkID()); // 首先清除.

		// 清除以前的数据，比如两次发送。
		if (this.getHisFlow().getHisDataStoreModel() == DataStoreModel.ByCCFlow) {
			wk.Delete(WorkAttr.FID, this.getHisWork().getOID());
		}

		// 判断分流的次数.是不是历史记录里面有分流。
		boolean IsHaveFH = false;
		if (this.getHisNode().getIsStartNode() == false) {
			ps = new Paras();
			ps.SQL = "SELECT COUNT(WorkID) FROM WF_GenerWorkerlist WHERE FID=" + dbStr + "OID";
			ps.Add("OID", this.getHisWork().getOID());
			if (DBAccess.RunSQLReturnValInt(ps) != 0) {
				IsHaveFH = true;
			}
		}

		/// #endregion 产生下一步骤的工作人员

		/// #region 复制数据.
		// 获得当前流程数节点数据.
		FrmAttachmentDBs athDBs = new FrmAttachmentDBs("ND" + this.getHisNode().getNodeID(),
				(new Long(this.getWorkID())).toString());

		MapDtls dtlsFrom = new MapDtls("ND" + this.getHisNode().getNodeID());
		if (dtlsFrom.size() > 1) {
			for (MapDtl d : dtlsFrom.ToJavaList()) {
				d.HisGEDtls_temp = null;
			}
		}
		MapDtls dtlsTo = null;
		if (dtlsFrom.size() >= 1) {
			dtlsTo = new MapDtls("ND" + toNode.getNodeID());
		}

		/**
		 * 定义系统变量.
		 */
		String workIDs = "";

		DataTable dtWork = null;
		if (toNode.getHisDeliveryWay() == DeliveryWay.BySQLAsSubThreadEmpsAndData) {
			// 如果是按照查询ＳＱＬ，确定明细表的接收人与子线程的数据。
			String sql = toNode.getDeliveryParas();
			sql = Glo.DealExp(sql, this.getHisWork(), null);
			dtWork = BP.DA.DBAccess.RunSQLReturnTable(sql);
		}
		if (toNode.getHisDeliveryWay() == DeliveryWay.ByDtlAsSubThreadEmps) {
			// 如果是按照明细表，确定明细表的接收人与子线程的数据。
			for (MapDtl dtl : dtlsFrom.ToJavaList()) {
				// 加上顺序，防止变化，人员编号变化，处理明细表中接受人重复的问题。
				String sql = "SELECT * FROM " + dtl.getPTable() + " WHERE RefPK=" + this.getWorkID() + " ORDER BY OID";
				dtWork = BP.DA.DBAccess.RunSQLReturnTable(sql);
				if (dtWork.Columns.contains("UserNo")) {
					break;
				} else {
					dtWork = null;
				}
			}
		}

		String groupMark = "";
		int idx = -1;
		for (GenerWorkerList wl : current_gwls.ToJavaList()) {
			if (this.IsHaveSubThreadGroupMark == true) {
				// 如果启用了批次处理,子线程的问题..
				if (groupMark.contains("@" + wl.getFK_Emp() + "," + wl.getGroupMark()) == false) {
					groupMark += "@" + wl.getFK_Emp() + "," + wl.getGroupMark();
				} else {
					wl.Delete(); // 删除该条垃圾数据.
					continue;
				}
			}

			idx++;
			Work mywk = toNode.getHisWork();

			/// #region 复制数据.
			mywk.Copy(this.rptGe);
			// mywk.Copy(this.HisWork); // 复制过来信息。
			if (dtWork != null) {
				// 用IDX处理是为了解决，人员重复出现在数据源并且还能根据索引对应的上。
				DataRow dr = dtWork.Rows.get(idx);
				if (dtWork.Columns.contains("UserNo") && dr.getValue("UserNo").toString().equals(wl.getFK_Emp())) {
					mywk.Copy(dr);
				}

				if (dtWork.Columns.contains("No") && dr.getValue("No").toString().equals(wl.getFK_Emp())) {
					mywk.Copy(dr);
				}
			}

			/// #endregion 复制数据.

			boolean isHaveEmp = false;

			// 是否有分流的明细表?
			boolean isHaveFLDtl = false;
			for (MapDtl dtl : dtlsFrom.ToJavaList()) {
				if (dtl.getIsFLDtl() == true) {
					isHaveFLDtl = true;
					break;
				}
			}

			// 是否是分组工作流程, 定义变量是为了，不让其在重复插入work数据。
			boolean isGroupMarkWorklist = false;

			if (IsHaveFH) {
				// 如果曾经走过分流合流，就找到同一个人员同一个FID下的OID ，做这当前线程的ID。
				ps = new Paras();
				ps.SQL = "SELECT WorkID,FK_Node FROM WF_GenerWorkerlist WHERE FK_Node!=" + dbStr + "FK_Node AND FID="
						+ dbStr + "FID AND FK_Emp=" + dbStr + "FK_Emp ORDER BY RDT DESC";
				ps.Add("FK_Node", toNode.getNodeID());
				ps.Add("FID", this.getWorkID());
				ps.Add("FK_Emp", wl.getFK_Emp());
				DataTable dt = DBAccess.RunSQLReturnTable(ps);
				if (dt.Rows.size() == 0) {
					// 没有发现，就说明以前分流节点中没有这个人的分流信息.
					mywk.setOID(DBAccess.GenerOID("WorkID"));
				} else {
					int workid_old = Integer.parseInt(dt.getValue(0, 0).toString());
					int fk_Node_nearly = Integer.parseInt(dt.getValue(0, 1).toString());
					Node nd_nearly = new Node(fk_Node_nearly);
					Work nd_nearly_work = nd_nearly.getHisWork();
					nd_nearly_work.setOID(workid_old);
					if (nd_nearly_work.RetrieveFromDBSources() != 0) {
						mywk.Copy(nd_nearly_work);
						mywk.setOID(workid_old);
					} else {
						mywk.setOID(DBAccess.GenerOID("WorkID"));
					}

					// 明细表数据汇总表，要复制到子线程的主表上去.
					for (MapDtl dtl : dtlsFrom.ToJavaList()) {
						if (dtl.getIsHLDtl() == false) {
							continue;
						}

						String sql = "SELECT * FROM " + dtl.getPTable() + " WHERE Rec='" + wl.getFK_Emp()
								+ "' AND RefPK='" + this.getWorkID() + "'";
						DataTable myDT = DBAccess.RunSQLReturnTable(sql);
						if (myDT.Rows.size() == 1) {
							Attrs attrs = mywk.getEnMap().getAttrs();
							for (Attr attr : attrs) {
								String key = attr.getKey();
								if (key.equals(GEDtlAttr.FID) || key.equals(GEDtlAttr.OID) || key.equals(GEDtlAttr.Rec)
										|| key.equals(GEDtlAttr.RefPK)) {
									continue;
								}

								if (myDT.Columns.contains(attr.getField()) == true) {
									mywk.SetValByKey(attr.getKey(), myDT.Rows.get(0).getValue(attr.getField()));
								}
							}
						}
					}
					isHaveEmp = true;
				}
			} else {
				// 为子线程产生WorkID.
				// edit by zhoupeng 2015.12.24 平安夜.
				// 处理国机的需求，判断是否有分组的情况，如果有就要找到分组的workid
				// * 让其同一个分组，只能生成一个workid。
				// *
				if (this.IsHaveSubThreadGroupMark == true) {
					// 查询该GroupMark 是否已经注册到流程引擎主表里了.
					String sql = "SELECT WorkID FROM WF_GenerWorkFlow WHERE AtPara LIKE '%GroupMark="
							+ wl.getGroupMark() + "%' AND FID=" + this.getWorkID();
					DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
					if (dt.Rows.size() == 0) {
						mywk.setOID(DBAccess.GenerOID("WorkID")); // BP.DA.DBAccess.GenerOID();
					} else {
						mywk.setOID(Long.parseLong(dt.Rows.get(0).getValue(0).toString())); // 使用该分组下的，已经注册过的分组的WorkID，而非产生一个新的WorkID。
						isGroupMarkWorklist = true; // 是分组数据，让其work 就不要在重复插入了.
					}
				} else {
					mywk.setOID(DBAccess.GenerOID("WorkID")); // BP.DA.DBAccess.GenerOID();
				}
			}

			// 非分组工作人员.
			if (isGroupMarkWorklist == false) {
				if (this.getHisWork().getFID() == 0) {
					mywk.setFID(this.getHisWork().getOID());
				}

				mywk.setRec(wl.getFK_Emp());
				mywk.setEmps(wl.getFK_Emp());
				mywk.BeforeSave();

				// 判断是不是MD5流程？
				if (this.getHisFlow().getIsMD5()) {
					mywk.SetValByKey("MD5", Glo.GenerMD5(mywk));
				}
			}

			/// #region 处理烟台（安检模式的流程）需求,
			/// 需要合流节点有一个明细表，根据明细表启动子线程任务,并要求把明细表的一行数据copy到下一个子线程的主表上去。
			if (isHaveFLDtl == true) {
				// 如果设置了当前节点是分流节点，并且有明细表是分流明细表，我们就要从这里copy数据。
				for (MapDtl dtl : this.getHisWork().getHisMapDtls().ToJavaList()) {
					if (dtl.getIsFLDtl() == false) {
						continue; // 如果不是分流数据。
					}

					// 获取明细数据。
					GEDtls gedtls = null;
					if (dtl.HisGEDtls_temp == null) {
						gedtls = new GEDtls(dtl.getNo());
						QueryObject qo = null;
						qo = new QueryObject(gedtls);
						switch (dtl.getDtlOpenType()) {
						case ForEmp:
							qo.AddWhere(GEDtlAttr.RefPK, this.getWorkID());
							break;
						case ForWorkID:
							qo.AddWhere(GEDtlAttr.RefPK, this.getWorkID());
							break;
						case ForFID:
							qo.AddWhere(GEDtlAttr.FID, this.getWorkID());
							break;
						}
						qo.DoQuery();
						dtl.HisGEDtls_temp = gedtls;
					}
					gedtls = dtl.HisGEDtls_temp;

					// 找到数据，并copy到里面去.
					for (GEDtl gedtl : gedtls.ToJavaList()) {
						String worker = gedtl.GetValStringByKey(dtl.getSubThreadWorker());
						if (worker.contains(wl.getFK_Emp()) == false) {
							continue;
						}

						if (DotNetToJavaStringHelper.isNullOrEmpty(dtl.getSubThreadGroupMark()) == false) {
							String groupMarkDtl = gedtl.GetValStringByKey(dtl.getSubThreadGroupMark());
							if (!wl.getGroupMark().equals(groupMarkDtl)) {
								continue;
							}
						}

						// 开始执行数据copy,
						mywk.Copy(gedtl);
						// wk.DirectUpdate();
						break;
					}
				}
			}

			/// #endregion 处理烟台需求,
			/// 需要合流节点有一个明细表，根据明细表启动子线程任务,并要求把明细表的一行数据copy到下一个子线程的主表上去。

			// 非分组工作人员.
			if (isGroupMarkWorklist == false) {
				// 保存主表数据.
				mywk.InsertAsOID(mywk.getOID());
				// 给系统变量赋值，放在发送后返回对象里.
				workIDs += mywk.getOID() + ",";

				/// #region 复制附件信息
				if (athDBs.size() > 0) {
					// 说明当前节点有附件数据
					athDBs.Delete(FrmAttachmentDBAttr.FK_MapData, "ND" + toNode.getNodeID(),
							FrmAttachmentDBAttr.RefPKVal, mywk.getOID());

					for (FrmAttachmentDB athDB : athDBs.ToJavaList()) {
						FrmAttachmentDB athDB_N = new FrmAttachmentDB();
						athDB_N.Copy(athDB);
						athDB_N.setFK_MapData("ND" + toNode.getNodeID());
						athDB_N.setRefPKVal((new Long(mywk.getOID())).toString());
						athDB_N.setFK_FrmAttachment(athDB_N.getFK_FrmAttachment()
								.replace("ND" + this.getHisNode().getNodeID(), "ND" + toNode.getNodeID()));

						if (athDB_N.getHisAttachmentUploadType().equals(AttachmentUploadType.Single)) {
							// 注意如果是单附件主键的命名规则不能变化，否则会导致与前台约定获取数据错误。
							athDB_N.setMyPK(athDB_N.getFK_FrmAttachment() + "_" + mywk.getOID());
							try {
								athDB_N.DirectInsert();
							} catch (java.lang.Exception e) {
								athDB_N.setMyPK(BP.DA.DBAccess.GenerGUID());
								athDB_N.Insert();
							}
						} else {
							try {
								// 多附件就是: FK_MapData+序列号的方式, 替换主键让其可以保存,不会重复.
								athDB_N.setMyPK(athDB_N.getUploadGUID() + "_" + athDB_N.getFK_MapData() + "_"
										+ athDB_N.getRefPKVal());
								athDB_N.DirectInsert();
							} catch (java.lang.Exception e2) {
								athDB_N.setMyPK(BP.DA.DBAccess.GenerGUID());
								athDB_N.Insert();
							}
						}
					}
				}

				/// #endregion 复制附件信息

				/// #region 复制签名信息
				if (this.getHisNode().getMapData().getFrmImgs().size() > 0) {
					for (FrmImg img : this.getHisNode().getMapData().getFrmImgs().ToJavaList()) {
						// 排除图片
						if (img.getHisImgAppType() == ImgAppType.Img) {
							continue;
						}
						// 获取数据
						FrmEleDBs eleDBs = new FrmEleDBs(img.getMyPK(), (new Long(this.getWorkID())).toString());
						if (eleDBs.size() > 0) {
							eleDBs.Delete(FrmEleDBAttr.FK_MapData, img.getMyPK()
									.replace("ND" + this.getHisNode().getNodeID(), "ND" + toNode.getNodeID()),
									FrmEleDBAttr.EleID, this.getWorkID());

							// 说明当前节点有附件数据
							for (FrmEleDB eleDB : eleDBs.ToJavaList()) {
								FrmEleDB eleDB_N = new FrmEleDB();
								eleDB_N.Copy(eleDB);
								eleDB_N.setFK_MapData(img.getEnPK().replace("ND" + this.getHisNode().getNodeID(),
										"ND" + toNode.getNodeID()));
								eleDB_N.setRefPKVal(img.getEnPK().replace("ND" + this.getHisNode().getNodeID(),
										"ND" + toNode.getNodeID()));
								eleDB_N.setEleID(String.valueOf(mywk.getOID()));
								eleDB_N.GenerPKVal();
								eleDB_N.Save();
							}
						}
					}
				}

				/// #endregion 复制附件信息

				/// #region 复制图片上传附件。
				if (this.getHisNode().getMapData().getFrmImgAths().size() > 0) {
					FrmImgAthDBs frmImgAthDBs = new FrmImgAthDBs("ND" + this.getHisNode().getNodeID(),
							(new Long(this.getWorkID())).toString());
					if (frmImgAthDBs.size() > 0) {
						frmImgAthDBs.Delete(FrmAttachmentDBAttr.FK_MapData, "ND" + toNode.getNodeID(),
								FrmAttachmentDBAttr.RefPKVal, mywk.getOID());

						// 说明当前节点有附件数据
						for (FrmImgAthDB imgAthDB : frmImgAthDBs.ToJavaList()) {
							FrmImgAthDB imgAthDB_N = new FrmImgAthDB();
							imgAthDB_N.Copy(imgAthDB);
							imgAthDB_N.setFK_MapData("ND" + toNode.getNodeID());
							imgAthDB_N.setRefPKVal(String.valueOf(mywk.getOID()));
							imgAthDB_N.setFK_FrmImgAth(imgAthDB_N.getFK_FrmImgAth()
									.replace("ND" + this.getHisNode().getNodeID(), "ND" + toNode.getNodeID()));
							imgAthDB_N.Save();
						}
					}
				}

				/// #endregion 复制图片上传附件。

				/// #region 复制从表信息.
				if (dtlsFrom.size() > 0 && dtlsTo.size() > 0) {
					int i = -1;
					for (MapDtl dtl : dtlsFrom.ToJavaList()) {
						i++;
						if (dtlsTo.size() <= i) {
							continue;
						}

						MapDtl toDtl = (MapDtl) dtlsTo.get(i);
						if (toDtl.getIsCopyNDData() == false) {
							continue;
						}

						if (toDtl.getPTable() == dtl.getPTable()) {
							continue;
						}

						// 获取明细数据。
						GEDtls gedtls = null;
						if (dtl.HisGEDtls_temp == null) {
							gedtls = new GEDtls(dtl.getNo());
							QueryObject qo = null;
							qo = new QueryObject(gedtls);
							switch (dtl.getDtlOpenType()) {
							case ForEmp:
								qo.AddWhere(GEDtlAttr.RefPK, this.getWorkID());
								break;
							case ForWorkID:
								qo.AddWhere(GEDtlAttr.RefPK, this.getWorkID());
								break;
							case ForFID:
								qo.AddWhere(GEDtlAttr.FID, this.getWorkID());
								break;
							}
							qo.DoQuery();
							dtl.HisGEDtls_temp = gedtls;
						}
						gedtls = dtl.HisGEDtls_temp;

						int unPass = 0;
						DBAccess.RunSQL("DELETE FROM " + toDtl.getPTable() + " WHERE RefPK=" + dbStr + "RefPK", "RefPK",
								mywk.getOID());
						for (GEDtl gedtl : gedtls.ToJavaList()) {
							if (gedtl.getRefPK().equals(String.valueOf(mywk.getOID()))) {
								GEDtl dtCopy = new GEDtl(toDtl.getNo());
								dtCopy.Copy(gedtl);
								dtCopy.FK_MapDtl = toDtl.getNo();
								dtCopy.setRefPK(String.valueOf(mywk.getOID()));
								dtCopy.setOID(0);
								dtCopy.Insert();

								/// #region 复制从表单条 - 附件信息 - M2M- M2MM
								if (toDtl.getIsEnableAthM()) {
									/* 如果启用了多附件,就复制这条明细数据的附件信息。 */
									athDBs = new FrmAttachmentDBs(dtl.getNo(), String.valueOf(gedtl.getOID()));
									if (athDBs.size() > 0) {
										i = 0;
										for (FrmAttachmentDB athDB : athDBs.ToJavaList()) {
											i++;
											FrmAttachmentDB athDB_N = new FrmAttachmentDB();
											athDB_N.Copy(athDB);
											athDB_N.setFK_MapData(toDtl.getNo());
											athDB_N.setMyPK(
													toDtl.getNo() + "_" + dtCopy.getOID() + "_" + String.valueOf(i));
											athDB_N.setFK_FrmAttachment(athDB_N.getFK_FrmAttachment().replace(
													"ND" + this.getHisNode().getNodeID(), "ND" + toNode.getNodeID()));
											athDB_N.setRefPKVal(String.valueOf(dtCopy.getOID()));
											athDB_N.DirectInsert();
										}
									}
								}
								 
								/// #endregion 复制从表单条 - 附件信息
							}
						}
					}
				}

				/// #endregion 复制附件信息

			}

			/// #region (循环最后处理)产生工作的信息
			// 产生工作的信息。
			GenerWorkFlow gwf = new GenerWorkFlow();
			gwf.setWorkID(mywk.getOID());
			if (gwf.RetrieveFromDBSources() == 0) {
				gwf.setFID(this.getWorkID());
				gwf.setFK_Node(toNode.getNodeID());

				if (this.getHisNode().getIsStartNode()) {
					gwf.setTitle(BP.WF.WorkFlowBuessRole.GenerTitle(this.getHisFlow(), this.getHisWork()) + "("
							+ wl.getFK_EmpText() + ")");
				} else {
					gwf.setTitle(this.getHisGenerWorkFlow().getTitle() + "(" + wl.getFK_EmpText() + ")");
				}

				gwf.setWFState(WFState.Runing);
				gwf.setRDT(DataType.getCurrentDataTime());
				gwf.setStarter(this.getExecer());
				gwf.setStarterName(this.getExecerName());
				gwf.setFK_Flow(toNode.getFK_Flow());
				gwf.setFlowName(toNode.getFlowName());

				gwf.setFID(this.getWorkID());
				gwf.setFK_FlowSort(toNode.getHisFlow().getFK_FlowSort());
				gwf.setNodeName(toNode.getName());
				gwf.setFK_Dept(wl.getFK_Dept());
				gwf.setDeptName(wl.getFK_DeptT());
				gwf.setTodoEmps(wl.getFK_Emp() + "," + wl.getFK_EmpText());
				if (!wl.getGroupMark().equals("")) {
					gwf.setParas_GroupMark(wl.getGroupMark());
				}

				gwf.setSender(BP.WF.Glo.DealUserInfoShowModel(WebUser.getNo(), WebUser.getName()));

				gwf.DirectInsert();
			} else {
				if (!wl.getGroupMark().equals("")) {
					gwf.setParas_GroupMark(wl.getGroupMark());
				}

				gwf.setSender(BP.WF.Glo.DealUserInfoShowModel(WebUser.getNo(), WebUser.getName()));
				gwf.setFK_Node(toNode.getNodeID());
				gwf.setNodeName(toNode.getName());
				gwf.Update();
			}

			// 插入当前分流节点的处理人员,让其可以在在途里看到工作.
			// 非分组工作人员.
			if (isGroupMarkWorklist == false) {
				GenerWorkerList flGwl = new GenerWorkerList();
				flGwl.Copy(wl);

				// flGwl.WorkID = this.WorkID;

				flGwl.setFK_Emp(WebUser.getNo());
				flGwl.setFK_EmpText(WebUser.getName());
				flGwl.setFK_Node(this.getHisNode().getNodeID());
				flGwl.setSender(WebUser.getNo() + "," + WebUser.getName());
				// flGwl.GroupMark = "";
				flGwl.setIsPassInt(-2); // -2; //标志该节点是干流程人员处理的节点.
				// wl.FID = 0; //如果是干流，
				flGwl.Insert();
			}

			// 把临时的workid 更新到.
			ps = new Paras();
			ps.SQL = "UPDATE WF_GenerWorkerlist SET WorkID=" + dbStr + "WorkID1 WHERE WorkID=" + dbStr + "WorkID2";
			ps.Add("WorkID1", mywk.getOID());
			ps.Add("WorkID2", wl.getWorkID()); // 临时的ID,更新最新的workid.
			int num = DBAccess.RunSQL(ps);
			if (num == 0) {
				throw new RuntimeException("@不应该更新不到它。");
			}

			// 设置当前的workid. 临时的id有变化.
			wl.setWorkID(mywk.getOID());

			/// #endregion 产生工作的信息.
		}

		/// #endregion 复制数据.

		/// #region 处理消息提示

		this.addMsg("FenLiuInfo", "@分流节点:" + toNode.getName() + "已经发起,@发送给如下(" + this.HisRememberMe.getNumOfObjs()
				+ ")个处理人" + this.HisRememberMe.getEmpsExt() + ".");

		// 把子线程的 WorkIDs 加入系统变量.
		this.addMsg(SendReturnMsgFlag.VarTreadWorkIDs, workIDs, workIDs, SendReturnMsgType.SystemMsg);

		// 如果是开始节点，就可以允许选择接受人。
		if (1==2 && this.getHisNode().getIsStartNode()) {
			if (current_gwls.size() >= 2 && this.getHisNode().getIsTask()) {
				this.addMsg("AllotTask",
						"@<img src='" + getVirPath() + "WF/Img/AllotTask.gif' border=0 /><a href=\"" + getVirPath()
								+ "WF/WorkOpt/AllotTask.jsp?WorkID=" + this.getWorkID() + "&FID=" + this.getWorkID()
								+ "&NodeID=" + toNode.getNodeID() + "\" >修改接受对象</a>.");
			}
		}

		if ( 1==2 && this.getHisNode().getIsStartNode()) {
			if (WebUser.getIsWap()) {
				this.addMsg("UnDoNew",
						"@<a href='" + getVirPath() + "WF/Wap/MyFlowInfo.jsp?DoType=UnSend&WorkID=" + this.getWorkID()
								+ "&FK_Flow=" + toNode.getFK_Flow() + "'><img src='./Img/Action/UnSend.png' border=0/>撤销本次发送</a>， <a href='" + getVirPath()
								+ "WF/Wap/MyFlow.htm?FK_Flow=" + toNode.getFK_Flow() + "&FK_Node="
								+ Integer.parseInt(toNode.getFK_Flow()) + "01' ><img src='" + getVirPath()
								+ "WF/Img/New.gif' border=0/>新建流程</a>。");
			} else {
				this.addMsg("UnDoNew",
						"@<a href='" + this.getVirPath() + this.getAppType() + "/MyFlowInfo.jsp?DoType=UnSend&WorkID="
								+ this.getWorkID() + "&FK_Flow=" + toNode.getFK_Flow() + "'><img src='" + getVirPath()
								+ "WF/Img/Action/UnSend.png' border=0/>撤销本次发送</a>， <a href='" + this.getVirPath()
								+ this.getAppType() + "/MyFlow.htm?FK_Flow=" + toNode.getFK_Flow() + "&FK_Node="
								+ Integer.parseInt(toNode.getFK_Flow()) + "01' ><img src='" + getVirPath()
								+ "WF/Img/New.gif' border=0/>新建流程</a>。");
			}
		} else {
			this.addMsg("UnDo",
					"@<a href='" + this.getVirPath() + this.getAppType() + "/MyFlowInfo.jsp?DoType=UnSend&WorkID="
							+ this.getWorkID() + "&FK_Flow=" + toNode.getFK_Flow() + "'><img src='" + getVirPath()
							+ "WF/Img/Action/UnSend.png' border=0/>撤销本次发送</a>。");
		}

		this.addMsg("Rpt", "@<a href='" + getVirPath() + "WF/WFRpt.jsp?WorkID=" + this.getWorkID() + "&FID="
				+ wk.getFID() + "&FK_Flow=" + this.getHisNode().getFK_Flow() + "'target='_self' >工作轨迹</a>");

		/// #endregion 处理消息提示
	}

	/**
	 * 合流点到普通点发送 1. 首先要检查完成率. 2, 按普通节点向普通节点发送.
	 * 
	 * @return
	 * @throws Exception 
	 */
	private void NodeSend_31(Node nd) throws Exception {
		// 检查完成率.

		// 与1-1一样的逻辑处理.
		this.NodeSend_11(nd);
	}

	/**
	 * 子线程向下发送
	 * 
	 * @return
	 */
	private String NodeSend_4x() {
		return null;
	}

	/**
	 * 子线程向合流点
	 * 
	 * @return
	 * @throws Exception 
	 */
	private void NodeSend_53_SameSheet_To_HeLiu(Node nd) throws Exception {
		
		
		 Work heLiuWK = nd.getHisWork();
         heLiuWK.setOID(this.getHisWork().getFID());
         if (heLiuWK.RetrieveFromDBSources() == 0) //查询出来数据.
             heLiuWK.DirectInsert();

         heLiuWK.Copy(this.getHisWork()); // 执行copy.

         heLiuWK.setOID(this.getHisWork().getFID());
         heLiuWK.setFID( 0);

         this.town = new WorkNode(heLiuWK, nd);



         // 先查询一下是否有人员，在合流节点上，如果没有就让其初始化人员. 
         current_gwls = new GenerWorkerLists();
         current_gwls.Retrieve(GenerWorkerListAttr.WorkID, this.getHisWork().getFID(),GenerWorkerListAttr.FK_Node, nd.getNodeID() );
         if (current_gwls.size() == 0)
             current_gwls = this.Func_GenerWorkerLists(this.town);// 初试化他们的工作人员．
         
 

         String FK_Emp = "";
         String toEmpsStr = "";
         String emps = "";
         for (GenerWorkerList wl : current_gwls.ToJavaList())
         {
             toEmpsStr += BP.WF.Glo.DealUserInfoShowModel(wl.getFK_Emp(), wl.getFK_EmpText());
             if (current_gwls.size() == 1)
                 emps = toEmpsStr;
             else
                 emps += "@" + toEmpsStr;
         }
 

         //#region 复制主表数据. edit 2014-11-20 向合流点汇总数据.
         //复制当前节点表单数据.
         heLiuWK.setFID( 0);
         heLiuWK.setRec( FK_Emp);
         heLiuWK.setEmps(emps);
         heLiuWK.setOID( this.getHisWork().getFID());
         heLiuWK.DirectUpdate(); //在更新一次.

         /* 把数据复制到rpt数据表里. */
         this.rptGe.setOID(this.getHisWork().getFID());
         this.rptGe.RetrieveFromDBSources();
         this.rptGe.Copy(this.getHisWork());
         this.rptGe.DirectUpdate();
         //#endregion 复制主表数据.

         // 产生合流汇总明细表数据.
       //  this.GenerHieLiuHuiZhongDtlData_2013(nd);

         //#endregion 处理合流节点表单数据

         /* 合流点需要等待各个分流点全部处理完后才能看到它。*/
         String info = "";
//#warning 对于多个分合流点可能会有问题。
         ps = new Paras();
         ps.SQL = "SELECT COUNT(distinct WorkID) AS Num FROM WF_GenerWorkerList WHERE  FID=" + dbStr + "FID AND FK_Node IN (" + this.SpanSubTheadNodes(nd) + ")";
         ps.Add("FID", this.getHisWork().getFID());

         java.math.BigDecimal numAll1 = new BigDecimal(DBAccess.RunSQLReturnValInt(ps));
			java.math.BigDecimal passRate1 = numAll1.divide(numAll1, 2).multiply(new BigDecimal(100));
			if (nd.getPassRate().compareTo(passRate1) <= 0) {
				
		 
             ps = new Paras();
             ps.SQL = "UPDATE WF_GenerWorkerList SET IsPass=0,FID=0 WHERE FK_Node=" + dbStr + "FK_Node AND WorkID=" + dbStr + "WorkID";
             ps.Add("FK_Node", nd.getNodeID());
             ps.Add("WorkID", this.getHisWork().getFID());
             DBAccess.RunSQL(ps);

             ps = new Paras();
             ps.SQL = "UPDATE WF_GenerWorkFlow SET FK_Node=" + dbStr + "FK_Node,NodeName=" + dbStr + "NodeName WHERE WorkID=" + dbStr + "WorkID";
             ps.Add("FK_Node", nd.getNodeID());
             ps.Add("NodeName", nd.getName());
             ps.Add("WorkID", this.getHisWork().getFID());
             DBAccess.RunSQL(ps);

             info = "@下一步合流点(" + nd.getName() + ")已经启动。";
         }
         else
         {
//#warning 为了不让其显示在途的工作需要， =3 不是正常的处理模式。
             ps = new Paras();
             ps.SQL = "UPDATE WF_GenerWorkerList SET IsPass=3,FID=0 WHERE FK_Node=" + dbStr + "FK_Node AND WorkID=" + dbStr + "WorkID";
             ps.Add("FK_Node", nd.getNodeID());
             ps.Add("WorkID", this.getHisWork().getOID());
             DBAccess.RunSQL(ps);
         }

         this.getHisGenerWorkFlow().setFK_Node(nd.getNodeID());
         this.getHisGenerWorkFlow().setNodeName(nd.getName());
  
         this.addMsg(SendReturnMsgFlag.VarAcceptersID, emps, SendReturnMsgType.SystemMsg);
         this.addMsg("HeLiuInfo", "@下一步的工作处理人[" + emps + "]" + info, SendReturnMsgType.Info);
           
	}

	private String NodeSend_55(Node toNode) {
		return null;
	}

	/**
	 * 节点向下运动
	 * @throws Exception 
	 * 
	 */
	private void NodeSend_Send_5_5() throws Exception {
		// 执行设置当前人员的完成时间. for: anhua 2013-12-18.
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkerlist SET CDT=" + dbstr + "CDT WHERE WorkID=" + dbstr + "WorkID AND FK_Node="
				+ dbstr + "FK_Node AND FK_Emp=" + dbstr + "FK_Emp";
		ps.Add(GenerWorkerListAttr.CDT, DataType.getCurrentDataTimess());
		ps.Add(GenerWorkerListAttr.WorkID, this.getWorkID());
		ps.Add(GenerWorkerListAttr.FK_Node, this.getHisNode().getNodeID());
		ps.Add(GenerWorkerListAttr.FK_Emp, this.getExecer());
		BP.DA.DBAccess.RunSQL(ps);

		/// #region 检查当前的状态是是否是退回,如果是退回的状态，就给他赋值.
		// 检查当前的状态是是否是退回，.
		if (this.SendNodeWFState == WFState.ReturnSta) {
			// 检查该退回是否是原路返回?
			ps = new Paras();
			ps.SQL = "SELECT ReturnNode,Returner,IsBackTracking FROM WF_ReturnWork WHERE WorkID=" + dbStr
					+ "WorkID AND IsBackTracking=1 ORDER BY RDT DESC";
			ps.Add(ReturnWorkAttr.WorkID, this.getWorkID());
			DataTable dt = DBAccess.RunSQLReturnTable(ps);
			if (dt.Rows.size() != 0) {
				// 有可能查询出来多个，因为按时间排序了，只取出最后一次退回的，看看是否有退回并原路返回的信息。

				// 确认这次退回，是退回并原路返回 , 在这里初始化它的工作人员, 与将要发送的节点.
				this.JumpToNode = new Node(Integer.parseInt(dt.Rows.get(0).getValue("ReturnNode").toString()));
				this.JumpToEmp = dt.Rows.get(0).getValue("Returner").toString();
				this.IsSkip = true; // 如果不设置为true, 将会删除目标数据.
				// this.NodeSend_11(this.JumpToNode);
			}
		}

		/// #endregion.

		switch (this.getHisNode().getHisRunModel()) {
		case Ordinary: // 1： 普通节点向下发送的
			Node toND = this.NodeSend_GenerNextStepNode();
			if (this.getIsStopFlow()) {
				return;
			}
			// 写入到达信息.
			this.addMsg(SendReturnMsgFlag.VarToNodeID, toND.getNodeID() + "", toND.getNodeID() + "",
					SendReturnMsgType.SystemMsg);
			this.addMsg(SendReturnMsgFlag.VarToNodeName, toND.getName(), toND.getName(), SendReturnMsgType.SystemMsg);
			switch (toND.getHisRunModel()) {
			case Ordinary: // 1-1 普通节to普通节点
				this.NodeSend_11(toND);
				break;
			case FL: // 1-2 普通节to分流点
				this.NodeSend_11(toND);
				break;
			case HL: // 1-3 普通节to合流点
				this.NodeSend_11(toND);
				// throw new Exception("@流程设计错误:请检查流程获取详细信息, 普通节点下面不能连接合流节点(" +
				// toND.Name + ").");
				break;
			case FHL: // 1-4 普通节点to分合流点
				this.NodeSend_11(toND);
				break;
			// throw new Exception("@流程设计错误:请检查流程获取详细信息, 普通节点下面不能连接分合流节点(" +
			// toND.Name + ").");
			case SubThread: // 1-5 普通节to子线程点
				throw new RuntimeException("@流程设计错误:请检查流程获取详细信息, 普通节点下面不能连接子线程节点(" + toND.getName() + ").");
			default:
				throw new RuntimeException("@没有判断的节点类型(" + toND.getName() + ")");
			}
			break;
		case FL: // 2: 分流节点向下发送的
			Nodes toNDs = this.Func_GenerNextStepNodes();
			if (toNDs.size() == 1) {
				Node toND2 = (Node) ((toNDs.get(0) instanceof Node) ? toNDs.get(0) : null);
				// 加入系统变量.
				this.addMsg(SendReturnMsgFlag.VarToNodeID, (new Integer(toND2.getNodeID())).toString(),
						(new Integer(toND2.getNodeID())).toString(), SendReturnMsgType.SystemMsg);
				this.addMsg(SendReturnMsgFlag.VarToNodeName, toND2.getName(), toND2.getName(),
						SendReturnMsgType.SystemMsg);

				switch (toND2.getHisRunModel()) {
				case Ordinary: // 2.1 分流点to普通节点
					this.NodeSend_11(toND2); // 按普通节点到普通节点处理.
					break;
				case FL: // 2.2 分流点to分流点
					// throw new Exception("@流程设计错误:请检查流程获取详细信息, 分流点(" +
					// this.HisNode.Name + ")下面不能连接分流节点(" + toND2.Name + ").");
				case HL: // 2.3 分流点to合流点,分合流点.
				case FHL:
					this.NodeSend_11(toND2); // 按普通节点到普通节点处理.
					break;
				// throw new Exception("@流程设计错误:请检查流程获取详细信息, 分流点(" +
				// this.HisNode.Name + ")下面不能连接合流节点(" + toND2.Name + ").");
				case SubThread: // 2.4 分流点to子线程点
					if (toND2.getHisSubThreadType() == SubThreadType.SameSheet) {
						NodeSend_24_SameSheet(toND2);
					} else {
						NodeSend_24_UnSameSheet(toNDs); // 可能是只发送1个异表单
					}
					break;
				default:
					throw new RuntimeException("@没有判断的节点类型(" + toND2.getName() + ")");
				}
			} else {
				// 如果有多个节点，检查一下它们必定是子线程节点否则，就是设计错误。
				boolean isHaveSameSheet = false;
				boolean isHaveUnSameSheet = false;
				for (Node nd : toNDs.ToJavaList()) {
					switch (nd.getHisRunModel()) {
					case Ordinary:
						NodeSend_11(nd); // 按普通节点到普通节点处理.
						break;
					case FL:
					case FHL:
					case HL:
						NodeSend_11(nd); // 按普通节点到普通节点处理.
						break;
					// throw new Exception("@流程设计错误:请检查流程获取详细信息, 分流点(" +
					// this.HisNode.Name + ")下面不能连接分流节点(" + nd.Name + ").");
					// case RunModel.FHL:
					// throw new Exception("@流程设计错误:请检查流程获取详细信息, 分流点(" +
					// this.HisNode.Name + ")下面不能连接分合流节点(" + nd.Name + ").");
					// case RunModel.HL:
					// throw new Exception("@流程设计错误:请检查流程获取详细信息, 分流点(" +
					// this.HisNode.Name + ")下面不能连接合流节点(" + nd.Name + ").");
					default:
						break;
					}
					if (nd.getHisSubThreadType() == SubThreadType.SameSheet) {
						isHaveSameSheet = true;
					}

					if (nd.getHisSubThreadType() == SubThreadType.UnSameSheet) {
						isHaveUnSameSheet = true;
					}
				}

				if (isHaveUnSameSheet && isHaveSameSheet) {
					throw new RuntimeException("@不支持流程模式: 分流节点同时启动了同表单的子线程与异表单的子线程.");
				}

				if (isHaveSameSheet == true) {
					throw new RuntimeException("@不支持流程模式: 分流节点同时启动了多个同表单的子线程.");
				}

				// 启动多个异表单子线程节点.
				this.NodeSend_24_UnSameSheet(toNDs);
			}
			break;
		case HL: // 3: 合流节点向下发送
			Node toND3 = this.NodeSend_GenerNextStepNode();
			if (this.getIsStopFlow()) {
				return;
			}

			// 加入系统变量.
			this.addMsg(SendReturnMsgFlag.VarToNodeID, (new Integer(toND3.getNodeID())).toString(),
					(new Integer(toND3.getNodeID())).toString(), SendReturnMsgType.SystemMsg);
			this.addMsg(SendReturnMsgFlag.VarToNodeName, toND3.getName(), toND3.getName(), SendReturnMsgType.SystemMsg);

			switch (toND3.getHisRunModel()) {
			case Ordinary: // 3.1 普通工作节点
				this.NodeSend_31(toND3); // 让它与普通点点普通点一样的逻辑.
				break;
			case FL: // 3.2 分流点
				this.NodeSend_31(toND3); // 让它与普通点点普通点一样的逻辑.
				break;
			// throw new Exception("@流程设计错误:请检查流程获取详细信息, 合流点(" +
			// this.HisNode.Name + ")下面不能连接分流节点(" + toND3.Name + ").");
			case HL: // 3.3 合流点
			case FHL:
				this.NodeSend_31(toND3); // 让它与普通点点普通点一样的逻辑.
				break;
			// throw new Exception("@流程设计错误:请检查流程获取详细信息, 合流点(" +
			// this.HisNode.Name + ")下面不能连接合流节点(" + toND3.Name + ").");
			case SubThread: // 3.4 子线程
				throw new RuntimeException("@流程设计错误:请检查流程获取详细信息, 合流点(" + this.getHisNode().getName() + ")下面不能连接子线程节点("
						+ toND3.getName() + ").");
			default:
				throw new RuntimeException("@没有判断的节点类型(" + toND3.getName() + ")");
			}
			break;
		case FHL: // 4: 分流节点向下发送的
			Node toND4 = this.NodeSend_GenerNextStepNode();
			if (this.getIsStopFlow()) {
				return;
			}

			// 加入系统变量.
			this.addMsg(SendReturnMsgFlag.VarToNodeID, (new Integer(toND4.getNodeID())).toString(),
					(new Integer(toND4.getNodeID())).toString(), SendReturnMsgType.SystemMsg);
			this.addMsg(SendReturnMsgFlag.VarToNodeName, toND4.getName(), toND4.getName(), SendReturnMsgType.SystemMsg);

			switch (toND4.getHisRunModel()) {
			case Ordinary: // 4.1 普通工作节点
				this.NodeSend_11(toND4); // 让它与普通点点普通点一样的逻辑.
				break;
			case FL: // 4.2 分流点
				throw new RuntimeException("@流程设计错误:请检查流程获取详细信息, 合流点(" + this.getHisNode().getName() + ")下面不能连接分流节点("
						+ toND4.getName() + ").");
			case HL: // 4.3 合流点
			case FHL:
				this.NodeSend_11(toND4); // 让它与普通点点普通点一样的逻辑.
				break;
			// throw new Exception("@流程设计错误:请检查流程获取详细信息, 合流点(" +
			// this.HisNode.Name + ")下面不能连接合流节点(" + toND4.Name + ").");
			case SubThread: // 4.5 子线程
				if (toND4.getHisSubThreadType() == SubThreadType.SameSheet) {
					NodeSend_24_SameSheet(toND4);
				} else {
					Nodes toNDs4 = this.Func_GenerNextStepNodes();
					NodeSend_24_UnSameSheet(toNDs4); // 可能是只发送1个异表单
				}
				break;
			// throw new Exception("@流程设计错误:请检查流程获取详细信息, 合流点(" +
			// this.HisNode.Name + ")下面不能连接子线程节点(" + toND4.Name + ").");
			default:
				throw new RuntimeException("@没有判断的节点类型(" + toND4.getName() + ")");
			}
			break;
		// throw new Exception("@没有判断的类型:" + this.HisNode.HisNodeWorkTypeT);
		case SubThread: // 5: 子线程节点向下发送的
			Node toND5 = this.NodeSend_GenerNextStepNode();
			if (this.getIsStopFlow()) {
				return;
			}

			// 加入系统变量.
			this.addMsg(SendReturnMsgFlag.VarToNodeID, (new Integer(toND5.getNodeID())).toString(),
					(new Integer(toND5.getNodeID())).toString(), SendReturnMsgType.SystemMsg);
			this.addMsg(SendReturnMsgFlag.VarToNodeName, toND5.getName(), toND5.getName(), SendReturnMsgType.SystemMsg);

			switch (toND5.getHisRunModel()) {
			case Ordinary: // 5.1 普通工作节点
				throw new RuntimeException("@流程设计错误:请检查流程获取详细信息, 子线程点(" + this.getHisNode().getName() + ")下面不能连接普通节点("
						+ toND5.getName() + ").");
			case FL: // 5.2 分流点
				throw new RuntimeException("@流程设计错误:请检查流程获取详细信息, 子线程点(" + this.getHisNode().getName() + ")下面不能连接分流节点("
						+ toND5.getName() + ").");
			case HL: // 5.3 合流点
			case FHL: // 5.4 分合流点
				if (this.getHisNode().getHisSubThreadType() == SubThreadType.SameSheet) {
					this.NodeSend_53_SameSheet_To_HeLiu(toND5);
				} else {
					this.NodeSend_53_UnSameSheet_To_HeLiu(toND5);
				}

				// 把合流点设置未读.
				ps = new Paras();
				ps.SQL = "UPDATE WF_GenerWorkerList SET IsRead=0 WHERE WorkID=" + SystemConfig.getAppCenterDBVarStr()
						+ "WorkID AND  FK_Node=" + SystemConfig.getAppCenterDBVarStr() + "FK_Node";
				ps.Add("WorkID", this.getHisWork().getFID());
				ps.Add("FK_Node", toND5.getNodeID());
				BP.DA.DBAccess.RunSQL(ps);
				break;
			case SubThread: // 5.5 子线程
				if (toND5.getHisSubThreadType() == this.getHisNode().getHisSubThreadType()) {

					/// #region 删除到达节点的子线程如果有，防止退回信息垃圾数据问题,如果退回处理了这个部分就不需要处理了.
					ps = new Paras();
					ps.SQL = "DELETE FROM WF_GenerWorkerlist WHERE FID=" + dbStr + "FID  AND FK_Node=" + dbStr
							+ "FK_Node";
					ps.Add("FID", this.getHisWork().getFID());
					ps.Add("FK_Node", toND5.getNodeID());

					/// #endregion
					/// 删除到达节点的子线程如果有，防止退回信息垃圾数据问题，如果退回处理了这个部分就不需要处理了.

					this.NodeSend_11(toND5); // 与普通节点一样.
				} else {
					throw new RuntimeException("@流程设计模式错误：连续两个子线程的子线程模式不一样，从节点(" + this.getHisNode().getName() + ")到节点("
							+ toND5.getName() + ")");
				}
				break;
			default:
				throw new RuntimeException("@没有判断的节点类型(" + toND5.getName() + ")");
			}
			break;
		default:
			throw new RuntimeException("@没有判断的类型:" + this.getHisNode().getHisRunModelT());
		}
	}

	/// #region 执行数据copy.
	public final void CopyData(Work toWK, Node toND, boolean isSamePTable) throws Exception {
		String errMsg = "如果两个数据源不想等，就执行 copy - 期间出现错误.";
		
		if (isSamePTable == true) {
			return;
		}

		/// #region 主表数据copy.
		if (isSamePTable == false) {
			toWK.SetValByKey("OID", this.getHisWork().getOID()); // 设定它的ID.
			if (this.getHisNode().getIsStartNode() == false) {
				toWK.Copy(this.rptGe);
			}

			toWK.Copy(this.getHisWork()); // 执行 copy 上一个节点的数据。
			toWK.setRec(this.getExecer());

			// 要考虑FID的问题.
			if (this.getHisNode().getHisRunModel() == RunModel.SubThread
					&& toND.getHisRunModel() == RunModel.SubThread) {
				toWK.setFID(this.getHisWork().getFID());
			}

			try {
				// 判断是不是MD5流程？
				if (this.getHisFlow().getIsMD5()) {
					toWK.SetValByKey("MD5", Glo.GenerMD5(toWK));
				}

				if (toWK.getIsExits()) {
					toWK.Update();
				} else {
					toWK.Insert();
				}
			} catch (RuntimeException ex) {
				toWK.CheckPhysicsTable();
				try {
					toWK.Copy(this.getHisWork()); // 执行 copy 上一个节点的数据。
					toWK.setRec(this.getExecer());
					toWK.SaveAsOID(toWK.getOID());
				} catch (RuntimeException ex11) {
					if (toWK.Update() == 0) {
						throw new RuntimeException(ex.getMessage() + " == " + ex11.getMessage());
					}
				}
			}
		}

		/// #endregion 主表数据copy.

		/// #region 复制附件。
		if (this.getHisNode().getMapData().getFrmAttachments().size() > 0) {
			// 删除上一个节点可能有的数据，有可能是发送退回来的产生的垃圾数据.
			Paras ps = new Paras();
			ps.SQL = "DELETE FROM Sys_FrmAttachmentDB WHERE FK_MapData=" + dbStr + "FK_MapData AND RefPKVal=" + dbStr
					+ "RefPKVal";
			ps.Add(FrmAttachmentDBAttr.FK_MapData, "ND" + toND.getNodeID());
			ps.Add(FrmAttachmentDBAttr.RefPKVal, this.getWorkID());
			DBAccess.RunSQL(ps);

			FrmAttachmentDBs athDBs = new FrmAttachmentDBs("ND" + this.getHisNode().getNodeID(),
					(new Long(this.getWorkID())).toString());

			int idx = 0;
			if (athDBs.size() > 0) {
				// 说明当前节点有附件数据
				for (FrmAttachmentDB athDB : athDBs.ToJavaList()) {
					FrmAttachmentDB athDB_N = new FrmAttachmentDB();
					athDB_N.Copy(athDB);
					athDB_N.setFK_MapData("ND" + toND.getNodeID());
					athDB_N.setRefPKVal((new Long(this.getHisWork().getOID())).toString());
					athDB_N.setFK_FrmAttachment(athDB_N.getFK_FrmAttachment()
							.replace("ND" + this.getHisNode().getNodeID(), "ND" + toND.getNodeID()));

					if (athDB_N.getHisAttachmentUploadType() == AttachmentUploadType.Single) {
						// 如果是单附件.
						athDB_N.setMyPK(athDB_N.getFK_FrmAttachment() + "_" + this.getHisWork().getOID());
						// if (athDB_N.IsExits == true)
						// continue; /*说明上一个节点或者子线程已经copy过了,
						// 但是还有子线程向合流点传递数据的可能，所以不能用break.*/
						try {
							athDB_N.Insert();
						} catch (java.lang.Exception e) {
							athDB_N.setMyPK(BP.DA.DBAccess.GenerGUID());
							athDB_N.Insert();
						}
					} else {
						//// 判断这个guid 的上传文件是否被其他的线程copy过去了？
						// if (athDB_N.IsExit(FrmAttachmentDBAttr.UploadGUID,
						//// athDB_N.UploadGUID,
						// FrmAttachmentDBAttr.FK_MapData, athDB_N.FK_MapData)
						//// == true)
						// continue; /*如果是就不要copy了.*/

						athDB_N.setMyPK(athDB_N.getUploadGUID() + "_" + athDB_N.getFK_MapData() + "_" + toWK.getOID());
						try {
							athDB_N.Insert();
						} catch (java.lang.Exception e2) {
							athDB_N.setMyPK(BP.DA.DBAccess.GenerGUID());
							athDB_N.Insert();
						}
					}
				}
			}
		}

		/// #endregion 复制附件。

		/// #region 复制图片上传附件。
		if (this.getHisNode().getMapData().getFrmImgAths().size() > 0) {
			FrmImgAthDBs athDBs = new FrmImgAthDBs("ND" + this.getHisNode().getNodeID(),
					(new Long(this.getWorkID())).toString());
			int idx = 0;
			if (athDBs.size() > 0) {
				athDBs.Delete(FrmAttachmentDBAttr.FK_MapData, "ND" + toND.getNodeID(), FrmAttachmentDBAttr.RefPKVal,
						this.getWorkID());

				// 说明当前节点有附件数据
				for (FrmImgAthDB athDB : athDBs.ToJavaList()) {
					idx++;
					FrmImgAthDB athDB_N = new FrmImgAthDB();
					athDB_N.Copy(athDB);
					athDB_N.setFK_MapData("ND" + toND.getNodeID());
					athDB_N.setRefPKVal((new Long(this.getWorkID())).toString());
					athDB_N.setMyPK(this.getWorkID() + "_" + idx + "_" + athDB_N.getFK_MapData());
					athDB_N.setFK_FrmImgAth(athDB_N.getFK_FrmImgAth().replace("ND" + this.getHisNode().getNodeID(),
							"ND" + toND.getNodeID()));
					athDB_N.Save();
				}
			}
		}

		/// #endregion 复制图片上传附件。

		/// #region 复制Ele
		if (this.getHisNode().getMapData().getFrmImgs().size() > 0) {
			for (FrmImg img : this.getHisNode().getMapData().getFrmImgs().ToJavaList()) {
				// 排除图片
				if (img.getHisImgAppType() == ImgAppType.Img) {
					continue;
				}
				// 获取数据
				FrmEleDBs eleDBs = new FrmEleDBs(img.getEnPK(), (new Long(this.getWorkID())).toString());
				if (eleDBs.size() > 0) {
					eleDBs.Delete(FrmEleDBAttr.FK_MapData,
							img.getEnPK().replace("ND" + this.getHisNode().getNodeID(), "ND" + toND.getNodeID()),
							FrmEleDBAttr.EleID, this.getWorkID());

					// 说明当前节点有附件数据
					for (FrmEleDB eleDB : eleDBs.ToJavaList()) {
						FrmEleDB eleDB_N = new FrmEleDB();
						eleDB_N.Copy(eleDB);
						eleDB_N.setFK_MapData(
								img.getEnPK().replace("ND" + this.getHisNode().getNodeID(), "ND" + toND.getNodeID()));
						eleDB_N.setRefPKVal(
								img.getEnPK().replace("ND" + this.getHisNode().getNodeID(), "ND" + toND.getNodeID()));
						eleDB_N.GenerPKVal();
						eleDB_N.Save();
					}
				}
			}
		}

		/// #endregion 复制Ele

		 

		/// #endregion

		/// #region 复制明细数据
		// int deBugDtlCount=
		MapDtls dtls = this.getHisNode().getMapData().getMapDtls();
		String recDtlLog = "@记录测试明细表Copy过程,从节点ID:" + this.getHisNode().getNodeID() + " WorkID:" + this.getWorkID()
				+ ", 到节点ID=" + toND.getNodeID();
		if (dtls.size() > 0) {
			MapDtls toDtls = toND.getMapData().getMapDtls();
			recDtlLog += "@到节点明细表数量是:" + dtls.size() + "个";

			MapDtls startDtls = null;
			boolean isEnablePass = false; // 是否有明细表的审批.
			for (MapDtl dtl : dtls.ToJavaList()) {
				if (dtl.getIsEnablePass()) {
					isEnablePass = true;
				}
			}

			if (isEnablePass) // 如果有就建立它开始节点表数据
			{
				startDtls = new BP.Sys.MapDtls("ND" + Integer.parseInt(toND.getFK_Flow()) + "01");
			}

			recDtlLog += "@进入循环开始执行逐个明细表copy.";
			int i = -1;

			for (MapDtl dtl : dtls.ToJavaList()) {
				recDtlLog += "@进入循环开始执行明细表(" + dtl.getNo() + ")copy.";

				// 如果当前的明细表，不需要copy.
				if (dtl.getIsCopyNDData() == false) {
					continue;
				}

				// i++;
				// if (toDtls.Count <= i)
				// continue;
				// MapDtl toDtl = (MapDtl)toDtls[i];

				i++;
				// if (toDtls.Count <= i)
				// continue;
				MapDtl toDtl = null;
				for (MapDtl todtl : toDtls.ToJavaList()) {
					if (todtl.getPTable() == dtl.getPTable()) {
						continue;
					}

					String toDtlName = "";
					String dtlName = "";
					try {
						toDtlName = todtl.getHisGEDtl().FK_MapDtl.substring(
								todtl.getHisGEDtl().FK_MapDtl.indexOf("Dtl"), todtl.getHisGEDtl().FK_MapDtl.length());
						dtlName = dtl.getHisGEDtl().FK_MapDtl.substring(dtl.getHisGEDtl().FK_MapDtl.indexOf("Dtl"),
								dtl.getHisGEDtl().FK_MapDtl.length());
					} catch (java.lang.Exception e4) {
						continue;
					}

					if (toDtlName.equals(dtlName)) {
						toDtl = todtl;
						break;
					}
				}

				if (dtl.getIsEnablePass() == true) {
					// 如果启用了是否明细表的审核通过机制,就允许copy节点数据。
					toDtl.setIsCopyNDData(true);
				}

				if (toDtl == null || toDtl.getIsCopyNDData() == false) {
					continue;
				}

				if (dtl.getPTable() == toDtl.getPTable()) {
					continue;
				}

				// 获取明细数据。
				GEDtls gedtls = new GEDtls(dtl.getNo());
				QueryObject qo = null;
				qo = new QueryObject(gedtls);
				switch (dtl.getDtlOpenType()) {
				case ForEmp:
					qo.AddWhere(GEDtlAttr.RefPK, this.getWorkID());
					break;
				case ForWorkID:
					qo.AddWhere(GEDtlAttr.RefPK, this.getWorkID());
					break;
				case ForFID:
					qo.AddWhere(GEDtlAttr.FID, this.getWorkID());
					break;
				}
				qo.DoQuery();

				recDtlLog += "@查询出来从明细表:" + dtl.getNo() + ",明细数据:" + gedtls.size() + "条.";

				int unPass = 0;
				// 是否启用审核机制。
				isEnablePass = dtl.getIsEnablePass();
				if (isEnablePass && this.getHisNode().getIsStartNode() == false) {
					isEnablePass = true;
				} else {
					isEnablePass = false;
				}

				if (isEnablePass == true) {
					// 判断当前节点该明细表上是否有，isPass 审核字段，如果没有抛出异常信息。
					if (gedtls.size() != 0) {
						GEDtl dtl1 = (GEDtl) ((gedtls.get(0) instanceof GEDtl) ? gedtls.get(0) : null);
						if (dtl1.getEnMap().getAttrs().Contains("IsPass") == false) {
							isEnablePass = false;
						}
					}
				}

				recDtlLog += "@删除到达明细表:" + dtl.getNo() + ",数据, 并开始遍历明细表,执行一行行的copy.";

				if (BP.DA.DBAccess.IsExitsObject(toDtl.getPTable())) {
					DBAccess.RunSQL("DELETE FROM " + toDtl.getPTable() + " WHERE RefPK=" + dbStr + "RefPK", "RefPK",
							(new Long(this.getWorkID())).toString());
				}

				// copy数量.
				int deBugNumCopy = 0;
				for (GEDtl gedtl : gedtls.ToJavaList()) {
					if (isEnablePass) {
						if (gedtl.GetValBooleanByKey("IsPass") == false) {
							// 没有审核通过的就 continue 它们，仅复制已经审批通过的.
							continue;
						}
					}

					BP.Sys.GEDtl dtCopy = new GEDtl(toDtl.getNo());
					dtCopy.Copy(gedtl);
					dtCopy.FK_MapDtl = toDtl.getNo();
					dtCopy.setRefPK((new Long(this.getWorkID())).toString());
					dtCopy.InsertAsOID(dtCopy.getOID());
					dtCopy.setRefPKInt64(this.getWorkID());
					deBugNumCopy++;

					/// #region 复制明细表单条 - 附件信息
					if (toDtl.getIsEnableAthM()) {
						// 如果启用了多附件,就复制这条明细数据的附件信息。
						FrmAttachmentDBs athDBs = new FrmAttachmentDBs(dtl.getNo(), String.valueOf(gedtl.getOID()));
						if (athDBs.size() > 0) {
							i = 0;
							for (FrmAttachmentDB athDB : athDBs.ToJavaList()) {
								i++;
								FrmAttachmentDB athDB_N = new FrmAttachmentDB();
								athDB_N.Copy(athDB);
								athDB_N.setFK_MapData(toDtl.getNo());
								athDB_N.setMyPK(
										athDB.getMyPK() + "_" + dtCopy.getOID() + "_" + (new Integer(i)).toString());
								athDB_N.setFK_FrmAttachment(athDB_N.getFK_FrmAttachment()
										.replace("ND" + this.getHisNode().getNodeID(), "ND" + toND.getNodeID()));
								athDB_N.setRefPKVal(String.valueOf(dtCopy.getOID()));
								try {
									athDB_N.DirectInsert();
								} catch (java.lang.Exception e5) {
									athDB_N.DirectUpdate();
								}

							}
						}
					}
				 
					/// #endregion 复制明细表单条 - 附件信息

				}

				/// #warning 记录日志.
				if (gedtls.size() != deBugNumCopy) {
					recDtlLog += "@从明细表:" + dtl.getNo() + ",明细数据:" + gedtls.size() + "条.";
					// 记录日志.
					Log.DefaultLogWriteLineInfo(recDtlLog);
					throw new RuntimeException("@系统出现错误，请将如下信息反馈给管理员,谢谢。: 技术信息:" + recDtlLog);
				}

				/// #region 如果启用了审核机制
				if (isEnablePass) {
					// 如果启用了审核通过机制，就把未审核的数据copy到第一个节点上去
					// * 1, 找到对应的明细点.
					// * 2, 把未审核通过的数据复制到开始明细表里.
					//
					String fk_mapdata = "ND" + Integer.parseInt(toND.getFK_Flow()) + "01";
					MapData md = new MapData(fk_mapdata);
					String startUser = "SELECT Rec FROM " + md.getPTable() + " WHERE OID=" + this.getWorkID();
					startUser = DBAccess.RunSQLReturnString(startUser);

					MapDtl startDtl = (MapDtl) startDtls.get(i);
					for (GEDtl gedtl : gedtls.ToJavaList()) {
						if (gedtl.GetValBooleanByKey("IsPass")) {
							continue; // 排除审核通过的
						}

						BP.Sys.GEDtl dtCopy = new GEDtl(startDtl.getNo());
						dtCopy.Copy(gedtl);
						dtCopy.setOID(0);
						dtCopy.FK_MapDtl = startDtl.getNo();
						dtCopy.setRefPK(String.valueOf(gedtl.getOID())); // this.WorkID.ToString();
						dtCopy.SetValByKey("BatchID", this.getWorkID());
						dtCopy.SetValByKey("IsPass", 0);
						dtCopy.SetValByKey("Rec", startUser);
						dtCopy.SetValByKey("Checker", this.getExecerName());
						dtCopy.setRefPKInt64(this.getWorkID());
						dtCopy.SaveAsOID((int) gedtl.getOID());
					}
					DBAccess.RunSQL("UPDATE " + startDtl.getPTable() + " SET Rec='" + startUser + "',Checker='"
							+ this.getExecer() + "' WHERE BatchID=" + this.getWorkID() + " AND Rec='" + this.getExecer()
							+ "'");
				}

				/// #endregion 如果启用了审核机制
			}
		}

		/// #endregion 复制明细数据
	}

	/// #endregion

	/// #region 返回对象处理.
	private SendReturnObjs HisMsgObjs = null;

	public final void addMsg(String flag, String msg) {
		addMsg(flag, msg, null, SendReturnMsgType.Info);
	}

	public final void addMsg(String flag, String msg, SendReturnMsgType msgType) {
		addMsg(flag, msg, null, msgType);
	}

	public final void addMsg(String flag, String msg, String msgofHtml, SendReturnMsgType msgType) {
		if (HisMsgObjs == null) {
			HisMsgObjs = new SendReturnObjs();
		}
		this.HisMsgObjs.AddMsg(flag, msg, msgofHtml, msgType);
	}

	public final void addMsg(String flag, String msg, String msgofHtml) {
		addMsg(flag, msg, msgofHtml, SendReturnMsgType.Info);
	}

	/// #endregion 返回对象处理.

	/// #region 方法
	/**
	 * 发送失败是撤消数据。
	 * @throws Exception 
	 * 
	 */
	public final void DealEvalUn() throws Exception {
		// 数据发送。
		BP.WF.Data.Eval eval = new Eval();
		if (this.getHisNode().getIsFLHL() == false) {
			eval.setMyPK(this.getWorkID() + "_" + this.getHisNode().getNodeID());
			eval.Delete();
		}

		// 分合流的情况，它是明细表产生的质量评价。
		MapDtls dtls = this.getHisNode().getMapData().getMapDtls();
		for (MapDtl dtl : dtls.ToJavaList()) {
			if (dtl.getIsHLDtl() == false) {
				continue;
			}

			// 获取明细数据。
			GEDtls gedtls = new GEDtls(dtl.getNo());
			QueryObject qo = null;
			qo = new QueryObject(gedtls);
			switch (dtl.getDtlOpenType()) {
			case ForEmp:
				qo.AddWhere(GEDtlAttr.RefPK, this.getWorkID());
				break;
			case ForWorkID:
				qo.AddWhere(GEDtlAttr.RefPK, this.getWorkID());
				break;
			case ForFID:
				qo.AddWhere(GEDtlAttr.FID, this.getWorkID());
				break;
			}
			qo.DoQuery();

			for (GEDtl gedtl : gedtls.ToJavaList()) {
				eval = new Eval();
				eval.setMyPK(gedtl.getOID() + "_" + gedtl.getRec());
				eval.Delete();
			}
		}
	}

	/**
	 * 处理质量考核
	 * @throws Exception 
	 * 
	 */
	public final void DealEval() throws Exception {
		
		if (1==1)
	   	return ;
		
		if (this.getHisNode().getIsEval() == false) {
			return;
		}

		BP.WF.Data.Eval eval = new Eval();
		eval.CheckPhysicsTable();

		if (this.getHisNode().getIsFLHL() == false) {
			eval.setMyPK(this.getWorkID() + "_" + this.getHisNode().getNodeID());
			eval.Delete();

			eval.setTitle(this.getHisGenerWorkFlow().getTitle());

			eval.setWorkID(this.getWorkID());
			eval.setFK_Node(this.getHisNode().getNodeID());
			eval.setNodeName(this.getHisNode().getName());

			eval.setFK_Flow(this.getHisNode().getFK_Flow());
			eval.setFlowName(this.getHisNode().getFlowName());

			eval.setFK_Dept(this.getExecerDeptNo());
			eval.setDeptName(this.getExecerDeptName());

			eval.setRec(this.getExecer());
			eval.setRecName(this.getExecerName());

			eval.setRDT(DataType.getCurrentDataTime());
			eval.setFK_NY(DataType.getCurrentYearMonth());

			eval.setEvalEmpNo(this.getHisWork().GetValStringByKey(WorkSysFieldAttr.EvalEmpNo));
			eval.setEvalEmpName(this.getHisWork().GetValStringByKey(WorkSysFieldAttr.EvalEmpName));
			eval.setEvalCent(this.getHisWork().GetValStringByKey(WorkSysFieldAttr.EvalCent));
			eval.setEvalNote(this.getHisWork().GetValStringByKey(WorkSysFieldAttr.EvalNote));

			eval.Insert();
			return;
		}

		// 分合流的情况，它是明细表产生的质量评价。
		MapDtls dtls = this.getHisNode().getMapData().getMapDtls();
		for (MapDtl dtl : dtls.ToJavaList()) {
			if (dtl.getIsHLDtl() == false) {
				continue;
			}

			// 获取明细数据。
			GEDtls gedtls = new GEDtls(dtl.getNo());
			QueryObject qo = null;
			qo = new QueryObject(gedtls);
			switch (dtl.getDtlOpenType()) {
			case ForEmp:
				qo.AddWhere(GEDtlAttr.RefPK, this.getWorkID());
				break;
			case ForWorkID:
				qo.AddWhere(GEDtlAttr.RefPK, this.getWorkID());
				break;
			case ForFID:
				qo.AddWhere(GEDtlAttr.FID, this.getWorkID());
				break;
			}
			qo.DoQuery();

			for (GEDtl gedtl : gedtls.ToJavaList()) {
				eval = new Eval();
				eval.setMyPK(gedtl.getOID() + "_" + gedtl.getRec());
				eval.Delete();

				eval.setTitle(this.getHisGenerWorkFlow().getTitle());

				eval.setWorkID(this.getWorkID());
				eval.setFK_Node(this.getHisNode().getNodeID());
				eval.setNodeName(this.getHisNode().getName());

				eval.setFK_Flow(this.getHisNode().getFK_Flow());
				eval.setFlowName(this.getHisNode().getFlowName());

				eval.setFK_Dept(this.getExecerDeptNo());
				eval.setDeptName(this.getExecerDeptName());

				eval.setRec(this.getExecer());
				eval.setRecName(this.getExecerName());

				eval.setRDT(DataType.getCurrentDataTime());
				eval.setFK_NY(DataType.getCurrentYearMonth());

				eval.setEvalEmpNo(gedtl.GetValStringByKey(WorkSysFieldAttr.EvalEmpNo));
				eval.setEvalEmpName(gedtl.GetValStringByKey(WorkSysFieldAttr.EvalEmpName));
				eval.setEvalCent(gedtl.GetValStringByKey(WorkSysFieldAttr.EvalCent));
				eval.setEvalNote(gedtl.GetValStringByKey(WorkSysFieldAttr.EvalNote));
				eval.Insert();
			}
		}
	}

	private void CallSubFlow() throws Exception {
		// 获取配置信息.
		String[] paras = this.getHisNode().getSubFlowStartParas().split("[@]", -1);
		for (String item : paras) {
			if (DotNetToJavaStringHelper.isNullOrEmpty(item)) {
				continue;
			}

			String[] keyvals = item.split("[;]", -1);

			String FlowNo = ""; // 流程编号
			String EmpField = ""; // 人员字段.
			String DtlTable = ""; // 明细表.
			for (String keyval : keyvals) {
				if (DotNetToJavaStringHelper.isNullOrEmpty(keyval)) {
					continue;
				}

				String[] strs = keyval.split("[=]", -1);
				// C# TO JAVA CONVERTER NOTE: The following 'switch' operated on
				// a string member and was converted to Java 'if-else' logic:
				// switch (strs[0])
				// ORIGINAL LINE: case "FlowNo":
				if (strs[0].equals("FlowNo")) {
					FlowNo = strs[1];
				}
				// ORIGINAL LINE: case "EmpField":
				else if (strs[0].equals("EmpField")) {
					EmpField = strs[1];
				}
				// ORIGINAL LINE: case "DtlTable":
				else if (strs[0].equals("DtlTable")) {
					DtlTable = strs[1];
				} else {
					throw new RuntimeException("@流程设计错误,获取流程属性配置的发起参数时，未指明的标记: " + strs[0]);
				}

				if (this.getHisNode().getSubFlowStartWay() == SubFlowStartWay.BySheetField) {
					String emps = this.getHisWork().GetValStringByKey(EmpField) + ",";
					String[] empStrs = emps.split("[,]", -1);

					String currUser = this.getExecer();
					Emps empEns = new Emps();
					String msgInfo = "";
					for (String emp : empStrs) {
						if (DotNetToJavaStringHelper.isNullOrEmpty(emp)) {
							continue;
						}

						// 以当前人员的身份登录.
						Emp empEn = new Emp(emp);
						BP.Web.WebUser.SignInOfGener(empEn);

						// 把数据复制给它.
						Flow fl = new Flow(FlowNo);
						Work sw = fl.NewWork();

						long workID = sw.getOID();
						sw.Copy(this.getHisWork());
						sw.setOID(workID);
						sw.Update();

						WorkNode wn = new WorkNode(sw, new Node(Integer.parseInt(FlowNo + "01")));
						wn.NodeSend(null, this.getExecer());
						msgInfo += BP.WF.Dev2Interface.Node_StartWork(FlowNo, null, null, 0, emp, this.getWorkID(),
								FlowNo);
					}
				}

			}
		}

		// BP.WF.Dev2Interface.Flow_NewStartWork(
		DataTable dt;

	}

	/// #endregion

	/**
	 * 工作流发送业务处理
	 * @throws Exception 
	 * 
	 */
	public final SendReturnObjs NodeSend() throws Exception {
		return NodeSend(null, null);
	}

	/**
	 * 1变N,用于分流节点，向子线程copy数据。
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final void CheckFrm1ToN() throws Exception {
		// 只有分流，合流才能执行1ToN.
		if (this.getHisNode().getHisRunModel() == RunModel.Ordinary || this.getHisNode().getHisRunModel() == RunModel.HL
				|| this.getHisNode().getHisRunModel() == RunModel.SubThread) {
			return;
		}

		// 初始化变量.
		if (frmNDs == null) {
			frmNDs = new FrmNodes(this.getHisNode().getFK_Flow(), this.getHisNode().getNodeID());
		}

		for (FrmNode fn : frmNDs.ToJavaList()) {
			if (fn.getIs1ToN() == false) {
				continue;
			}

			/// #region 获得实体主键.
			// 处理主键.
			long pk = 0; // this.WorkID;
			switch (fn.getWhoIsPK()) {
			case FID:
				pk = this.getHisWork().getFID();
				break;
			case OID:
				pk = this.getHisWork().getOID();
				break;
			case PWorkID:
				pk = this.rptGe.getPWorkID();
				break;
			default:
				throw new RuntimeException("@未判断的类型.");
			}

			if (pk == 0) {
				throw new RuntimeException("@未能获取表单主键.");
			}

			/// #endregion 获得实体主键.

			// 初始化这个实体.
			GEEntity geEn = new GEEntity(fn.getFK_Frm(), pk);

			// 首先删除垃圾数据.
			geEn.Delete("FID", this.getWorkID());

			// 循环子线程，然后插入数据.
			for (GenerWorkerList item : current_gwls.ToJavaList()) {
				geEn.setPKVal(item.getWorkID()); // 子线程的WorkID作为.
				geEn.SetValByKey("FID", this.getWorkID());

				/// #region 处理默认变量.
				// foreach (Attr attr in geEn.getEnMap().getAttrs())
				// {
				// if (attr.DefaultValOfReal == "@RDT")
				// {
				// geEn.SetValByKey(attr.Key,
				// BP.DA.DataType.getCurrentDataTime());
				// continue;
				// }

				// if (attr.DefaultValOfReal == "WebUser.No")
				// {
				// geEn.SetValByKey(attr.Key, item.FK_Emp);
				// continue;
				// }

				// if (attr.DefaultValOfReal == "@WebUser.Name")
				// {
				// geEn.SetValByKey(attr.Key, item.FK_EmpText);
				// continue;
				// }

				// if (attr.DefaultValOfReal == "@WebUser.FK_Dept")
				// {
				// Emp emp = new Emp(item.FK_Emp);
				// geEn.SetValByKey(attr.Key, emp.FK_Dept);
				// continue;
				// }

				// if (attr.DefaultValOfReal == "@WebUser.FK_DeptName")
				// {
				// Emp emp = new Emp(item.FK_Emp);
				// geEn.SetValByKey(attr.Key, emp.FK_DeptText);
				// continue;
				// }
				// }

				/// #endregion 处理默认变量.

				geEn.DirectInsert();
			}
		}
	}

	/**
	 * 当前节点的-表单绑定.
	 * 
	 */
	private BP.WF.Template.FrmNodes frmNDs = null;

	/**
	 * 汇总子线程的表单到合流节点上去
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final void CheckFrmHuiZongToDtl() throws Exception {
		// 只有分流，合流才能执行1ToN.
		if (this.getHisNode().getHisRunModel() != RunModel.SubThread) {
			return;
		}

		// 初始化变量.
		if (frmNDs == null) {
			frmNDs = new FrmNodes(this.getHisNode().getFK_Flow(), this.getHisNode().getNodeID());
		}

		for (FrmNode fn : frmNDs.ToJavaList()) {
			// 如果该表单不需要汇总，就不处理他.
			if (fn.getHuiZong().equals("0") || fn.getHuiZong().equals("")) {
				continue;
			}

			/// #region 获得实体主键.
			// 处理主键.
			long pk = 0; // this.WorkID;
			switch (fn.getWhoIsPK()) {
			case FID:
				pk = this.getHisWork().getFID();
				break;
			case OID:
				pk = this.getHisWork().getOID();
				break;
			case PWorkID:
				pk = this.rptGe.getPWorkID();
				break;
			default:
				throw new RuntimeException("@未判断的类型.");
			}

			if (pk == 0) {
				throw new RuntimeException("@未能获取表单主键.");
			}

			/// #endregion 获得实体主键.

			// 初始化这个实体,获得这个实体的数据.
			GEEntity rpt = new GEEntity(fn.getFK_Frm(), pk);
			//
			String[] strs = fn.getHuiZong().trim().split("[@]", -1);

			// 实例化这个数据.
			MapDtl dtl = new MapDtl(strs[1].toString());

			// 把数据汇总到指定的表里.
			GEDtl dtlEn = dtl.getHisGEDtl();
			dtlEn.setOID((int) this.getWorkID());
			int i = dtlEn.RetrieveFromDBSources();
			dtlEn.Copy(rpt);

			dtlEn.setOID((int) this.getWorkID());
			dtlEn.setRDT(BP.DA.DataType.getCurrentDataTime());
			dtlEn.setRec(BP.Web.WebUser.getNo());

			dtlEn.setRefPK((new Long(this.getHisWork().getFID())).toString());
			dtlEn.setFID(0);

			if (i == 0) {
				dtlEn.SaveAsOID((int) this.getWorkID());
			} else {
				dtlEn.Update();
			}

		}
	}

	/**
	 * 检查独立表单上必须填写的项目.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final boolean CheckFrmIsNotNull() throws Exception {
		// if (this.HisNode.HisFormType != NodeFormType.SheetTree)
		// return true;

		// 增加节点表单的必填项判断.
		String err = "";
		if (this.getHisNode().getHisFormType() == NodeFormType.FreeForm) {
			MapAttrs attrs = this.getHisNode().getMapData().getMapAttrs();
			Row row = this.getHisWork().getRow();
			for (MapAttr attr : attrs.ToJavaList()) {
				if (attr.getUIIsInput() == false) {
					continue;
				}

				String str = (String) ((row.get(attr.getKeyOfEn()) instanceof String) ? row.get(attr.getKeyOfEn())
						: null);
				// 如果是检查不能为空
				if (str == null || DotNetToJavaStringHelper.isNullOrEmpty(str) == true || str.trim().equals("")) {
					err += "@字段{" + attr.getKeyOfEn() + " ; " + attr.getName() + "}，不能为空。";
				}
			}

			/// #region 检查附件个数的完整性. - 该部分代码稳定后，移动到独立表单的检查上去。
			for (FrmAttachment ath : this.getHisWork().getHisFrmAttachments().ToJavaList()) {
				if (ath.getUploadFileNumCheck() == UploadFileNumCheck.None) {
					continue;
				}

				if (ath.getUploadFileNumCheck() == UploadFileNumCheck.NotEmpty) {
					Paras ps = new Paras();
					ps.SQL = "SELECT COUNT(MyPK) as Num FROM Sys_FrmAttachmentDB WHERE FK_MapData="
							+ SystemConfig.getAppCenterDBVarStr() + "FK_MapData AND FK_FrmAttachment="
							+ SystemConfig.getAppCenterDBVarStr() + "FK_FrmAttachment AND RefPKVal="
							+ SystemConfig.getAppCenterDBVarStr() + "RefPKVal";
					ps.Add("FK_MapData", "ND" + this.getHisNode().getNodeID());
					ps.Add("FK_FrmAttachment", ath.getMyPK());
					ps.Add("RefPKVal", this.getWorkID());
					if (DBAccess.RunSQLReturnValInt(ps) == 0) {
						err += "@您没有上传附件:" + ath.getName();
					}
				}

				if (ath.getUploadFileNumCheck() == UploadFileNumCheck.EverySortNoteEmpty) {
					Paras ps = new Paras();
					ps.SQL = "SELECT COUNT(MyPK) as Num, MyNote FROM Sys_FrmAttachmentDB WHERE FK_MapData="
							+ SystemConfig.getAppCenterDBVarStr() + "FK_MapData AND FK_FrmAttachment="
							+ SystemConfig.getAppCenterDBVarStr() + "FK_FrmAttachment AND RefPKVal="
							+ SystemConfig.getAppCenterDBVarStr() + "RefPKVal Group BY MyNote";
					ps.Add("FK_MapData", "ND" + this.getHisNode().getNodeID());
					ps.Add("FK_FrmAttachment", ath.getMyPK());
					ps.Add("RefPKVal", this.getWorkID());

					DataTable dt = DBAccess.RunSQLReturnTable(ps);
					if (dt.Rows.size() == 0) {
						err += "@您没有上传附件:" + ath.getName() + "，请为每个类别下上传附件。";
					}

					String sort = ath.getSort().replace(";", ",");
					String[] strs = sort.split("[,]", -1);
					for (String str : strs) {
						boolean isHave = false;
						for (DataRow dr : dt.Rows) {
							if (dr.getValue("MyNote").toString().equals(str)) {
								isHave = true;
								break;
							}
						}
						if (isHave == false) {
							err += "@你需要上传(" + str + ")";
						}
					}
				}
			}

			/// #endregion 检查附件个数的完整性.

			/// #region 检查图片附件的必填，added by liuxc,2016-11-1
			for (FrmImgAth imgAth : this.getHisNode().getMapData().getFrmImgAths().ToJavaList()) {
				if (!imgAth.getIsRequired()) {
					continue;
				}

				Paras ps = new Paras();
				ps.SQL = "SELECT COUNT(MyPK) as Num FROM Sys_FrmImgAthDB WHERE FK_MapData="
						+ SystemConfig.getAppCenterDBVarStr() + "FK_MapData AND FK_FrmImgAth="
						+ SystemConfig.getAppCenterDBVarStr() + "FK_FrmImgAth AND RefPKVal="
						+ SystemConfig.getAppCenterDBVarStr() + "RefPKVal";
				ps.Add("FK_MapData", "ND" + this.getHisNode().getNodeID());
				ps.Add("FK_FrmImgAth", imgAth.getMyPK());
				ps.Add("RefPKVal", this.getWorkID());
				if (DBAccess.RunSQLReturnValInt(ps) == 0) {
					err += "@您没有上传图片附件:" + imgAth.getCtrlID();
				}
			}

			/// #endregion 检查图片附件的必填，added by liuxc,2016-11-1

			if (!err.equals("")) {
				throw new RuntimeException("在提交前检查到如下必输字段填写不完整:" + err);
			}
		}

		// 查询出来所有的设置。
		FrmFields ffs = new FrmFields();

		QueryObject qo = new QueryObject(ffs);
		qo.AddWhere(FrmFieldAttr.FK_Node, this.getHisNode().getNodeID());
		qo.addAnd();
		qo.addLeftBracket();
		qo.AddWhere(FrmFieldAttr.IsNotNull, 1);
		qo.addOr();
		qo.AddWhere(FrmFieldAttr.IsWriteToFlowTable, 1);
		qo.addRightBracket();
		qo.DoQuery();

		if (ffs.size() == 0) {
			return true;
		}

		BP.WF.Template.FrmNodes frmNDs = new FrmNodes(this.getHisNode().getFK_Flow(), this.getHisNode().getNodeID());
		err = "";
		for (FrmNode item : frmNDs.ToJavaList()) {
			MapData md = new MapData(item.getFK_Frm());

			// 可能是url.
			if (md.getHisFrmType() == FrmType.Url) {
				continue;
			}

			// 如果使用默认方案,就return出去.
			if (item.getFrmSlnInt() == 0) {
				continue;
			}

			// 检查是否有？
			boolean isHave = false;
			for (FrmField myff : ffs.ToJavaList()) {
				if (!myff.getFK_MapData().equals(item.getFK_Frm())) {
					continue;
				}
				isHave = true;
				break;
			}
			if (isHave == false) {
				continue;
			}

			// 处理主键.
			long pk = 0; // this.WorkID;

			switch (item.getWhoIsPK()) {
			case FID:
				pk = this.getHisWork().getFID();
				break;
			case OID:
				pk = this.getHisWork().getOID();
				break;
			case PWorkID:
				pk = this.rptGe.getPWorkID();
				break;
			default:
				throw new RuntimeException("@未判断的类型.");
			}

			if (pk == 0) {
				throw new RuntimeException("@未能获取表单主键.");
			}

			// 获取表单值
			ps = new Paras();
			ps.SQL = "SELECT * FROM " + md.getPTable() + " WHERE OID=" + ps.getDBStr() + "OID";
			ps.Add(WorkAttr.OID, pk);
			DataTable dt = DBAccess.RunSQLReturnTable(ps);
			if (dt.Rows.size() == 0) {
				err += "@表单{" + md.getName() + "}没有输入数据。";
				continue;
			}

			// 检查数据是否完整.
			for (FrmField ff : ffs.ToJavaList()) {
				if (!ff.getFK_MapData().equals(item.getFK_Frm())) {
					continue;
				}

				// 获得数据.
				String val = "";
				val = dt.Rows.get(0).getValue(ff.getKeyOfEn()).toString();

				if (ff.getIsNotNull() == true) {
					// 如果是检查不能为空
					if (DotNetToJavaStringHelper.isNullOrEmpty(val) == true || val.trim().equals("")) {
						err += "@表单{" + md.getName() + "}字段{" + ff.getKeyOfEn() + " ; " + ff.getName() + "}，不能为空。";
					}
				}

				// 判断是否需要写入流程数据表.
				if (ff.getIsWriteToFlowTable() == true) {
					this.getHisWork().SetValByKey(ff.getKeyOfEn(), val);
					// this.rptGe.SetValByKey(ff.KeyOfEn, val);
				}
			}
		}
		if (!err.equals("")) {
			throw new RuntimeException("@在提交前检查到如下必输字段填写不完整:" + err);
		}

		return true;
	}

	/**
	 * copy表单树的数据
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final Work CopySheetTree() throws Exception {
		if (this.getHisNode().getHisFormType() != NodeFormType.SheetTree) {
			return null;
		}

		// 查询出来所有的设置。
		FrmFields ffs = new FrmFields();

		QueryObject qo = new QueryObject(ffs);
		qo.AddWhere(FrmFieldAttr.FK_Node, this.getHisNode().getNodeID());
		qo.DoQuery();

		BP.WF.Template.FrmNodes frmNDs = new FrmNodes(this.getHisNode().getFK_Flow(), this.getHisNode().getNodeID());
		String err = "";
		for (FrmNode item : frmNDs.ToJavaList()) {
			MapData md = new MapData(item.getFK_Frm());

			// 可能是url.
			if (md.getHisFrmType() == FrmType.Url) {
				continue;
			}

			// 检查是否有？
			boolean isHave = false;
			for (FrmField myff : ffs.ToJavaList()) {
				if (!myff.getFK_MapData().equals(item.getFK_Frm())) {
					continue;
				}
				isHave = true;
				break;
			}
			if (isHave == false) {
				continue;
			}

			// 处理主键.
			long pk = 0; // this.WorkID;

			switch (item.getWhoIsPK()) {
			case FID:
				pk = this.getHisWork().getFID();
				break;
			case OID:
				pk = this.getHisWork().getOID();
				break;
			case PWorkID:
				pk = this.rptGe.getPWorkID();
				break;
			default:
				throw new RuntimeException("@未判断的类型.");
			}

			if (pk == 0) {
				throw new RuntimeException("@未能获取表单主键.");
			}

			// 获取表单值
			ps = new Paras();
			ps.SQL = "SELECT * FROM " + md.getPTable() + " WHERE OID=" + ps.getDBStr() + "OID";
			ps.Add(WorkAttr.OID, pk);
			DataTable dt = DBAccess.RunSQLReturnTable(ps);

			// 检查数据是否完整.
			for (FrmField ff : ffs.ToJavaList()) {
				if (!ff.getFK_MapData().equals(item.getFK_Frm())) {
					continue;
				}

				// 获得数据.
				String val = "";
				val = dt.Rows.get(0).getValue(ff.getKeyOfEn()).toString();

				// 判断是否需要写入流程数据表.
				// if (ff.IsWriteToFlowTable == true)
				// {
				this.getHisWork().SetValByKey(ff.getKeyOfEn(), val);
				// this.rptGe.SetValByKey(ff.KeyOfEn, val);
				// }
			}
		}

		return this.getHisWork();
	}

	/**
	 * 执行抄送
	 * 
	 */
	public final void DoCC() {
	}

	/*
	 * zhoupeng 2018.4.25重构.
	 * */
	public final boolean DealTeamUpNode() throws Exception {
		
		  
		GenerWorkerLists gwls = new GenerWorkerLists();
        gwls.Retrieve(GenerWorkerListAttr.WorkID, this.getWorkID(),
            GenerWorkerListAttr.FK_Node, this.getHisNode().getNodeID());

        if (gwls.size() == 1)
            return false; /*让其向下执行,因为只有一个人,就没有顺序的问题.*/

        //查看是否我是最后一个？
        int num = 0;
        String todoEmps = ""; //记录没有处理的人.
        for (GenerWorkerList item : gwls.ToJavaList())
        {
            if (item.getIsPassInt() == 0 || item.getIsPassInt() == 90)
            {
                if (item.getFK_Emp().equals(  WebUser.getNo()) ==false)
                    todoEmps += BP.WF.Glo.DealUserInfoShowModel(item.getFK_Emp(), item.getFK_EmpText()) + " ";
                num++;
            }
        }

        if (num == 1)
        {
            if (this.getHisGenerWorkFlow().getHuiQianTaskSta() == HuiQianTaskSta.None)
            {
                this.getHisGenerWorkFlow().setSender( BP.WF.Glo.DealUserInfoShowModel(BP.Web.WebUser.getNo(), BP.Web.WebUser.getName()));
                this.getHisGenerWorkFlow().setTodoEmpsNum(  1);
                this.getHisGenerWorkFlow().setTodoEmps( WebUser.getName() + ";");
            }
            else
            {
                String huiqianNo = this.getHisGenerWorkFlow().getHuiQianZhuChiRen();
                String huiqianName = this.getHisGenerWorkFlow().getHuiQianZhuChiRenName();

                this.getHisGenerWorkFlow().setSender(BP.WF.Glo.DealUserInfoShowModel(huiqianNo, huiqianName));
                this.getHisGenerWorkFlow().setTodoEmpsNum(  1);
                this.getHisGenerWorkFlow().setTodoEmps( WebUser.getName() + ";");


            }
            return false; /*只有一个待办,说明自己就是最后的一个人.*/
        }

        //把当前的待办设置已办，并且提示未处理的人。
        for (GenerWorkerList gwl : gwls.ToJavaList())
        {
            if (gwl.getFK_Emp().equals(WebUser.getNo())==false)
                continue;

            //设置当前不可以用.
            gwl.setIsPassInt(1);
            gwl.Update();

            // 检查完成条件。
            if (this.getHisNode().getIsEndNode() == false)
                this.CheckCompleteCondition();

            //写入日志.
            if (this.getHisGenerWorkFlow().getHuiQianTaskSta() != HuiQianTaskSta.None)
                this.AddToTrack(ActionType.TeampUp, gwl.getFK_Emp(), todoEmps, this.getHisNode().getNodeID(), this.getHisNode().getName(), "会签");
            else
                this.AddToTrack(ActionType.TeampUp, gwl.getFK_Emp(), todoEmps, this.getHisNode().getNodeID(), this.getHisNode().getName(), "协作发送");

            //替换人员信息.
            String emps = this.getHisGenerWorkFlow().getTodoEmps();
            emps = emps.replace(WebUser.getNo() + "," + WebUser.getName() + ";", "");
            this.getHisGenerWorkFlow().setTodoEmps( emps);

            //处理会签问题
            this.addMsg(SendReturnMsgFlag.OverCurr, "@会签工作已完成@当前工作未处理的会签人有: " + todoEmps + " .", null, SendReturnMsgType.Info);
            return true;
        }

        throw new Exception("@不应该运行到这里，DealTeamUpNode。");
	}
	/**
	 * 如果是协作.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final boolean DealTeamUpNode_bak() throws Exception {
		
		GenerWorkerLists gwls = new GenerWorkerLists();
		gwls.Retrieve(GenerWorkerListAttr.WorkID, this.getWorkID(), GenerWorkerListAttr.FK_Node,
				this.getHisNode().getNodeID(), GenerWorkerListAttr.IsPass);

		if (gwls.size() == 1) {
			return false; // 让其向下执行,因为只有一个人。就没有顺序的问题.
		}

		// 查看是否我是最后一个？
		int num = 0;
		String todoEmps = ""; // 记录没有处理的人.
		for (GenerWorkerList item : gwls.ToJavaList()) {
			if (item.getIsPassInt() == 0 || item.getIsPassInt() == 90) {
				if (!item.getFK_Emp().equals(WebUser.getNo())) {
					todoEmps += BP.WF.Glo.DealUserInfoShowModel(item.getFK_Emp(), item.getFK_EmpText()) + " ";
				}
				num++;
			}
		}

		if (num == 1) {
			this.getHisGenerWorkFlow()
					.setSender(BP.WF.Glo.DealUserInfoShowModel(BP.Web.WebUser.getNo(), BP.Web.WebUser.getName()));
			return false; // 只有一个待办,说明自己就是最后的一个人.
		}

		// 把当前的待办设置已办，并且提示未处理的人。
		for (GenerWorkerList gwl : gwls.ToJavaList()) {

			if (!gwl.getFK_Emp().equals(WebUser.getNo())) {
				continue;
			}

			// 设置当前不可以用.
			gwl.setIsPassInt(1);
			gwl.Update();

			// 检查完成条件。
			if (this.getHisNode().getIsEndNode() == false) {
				this.CheckCompleteCondition();
			}
			// 写入日志.
			this.AddToTrack(ActionType.TeampUp, gwl.getFK_Emp(), todoEmps, this.getHisNode().getNodeID(),
					this.getHisNode().getName(), "协作发送");

			// cut 当前的人员.
			String emps = this.getHisGenerWorkFlow().getTodoEmps();
			emps = emps.replace(WebUser.getName() + ";", "");
			this.getHisGenerWorkFlow().setTodoEmps(emps);
			this.getHisGenerWorkFlow().DirectUpdate();

			// 会签处理问题 @于庆海
			if (num == 2) {
				String msg = this.DealAlertZhuChiRen();
				this.addMsg(SendReturnMsgFlag.OverCurr, msg, null, SendReturnMsgType.Info);
			} else {
				this.addMsg(SendReturnMsgFlag.OverCurr, "当前工作未处理的人有: " + todoEmps + " .", null, SendReturnMsgType.Info);
				// this.addMsg(SendReturnMsgFlag.OverCurr, "当前工作未处理的人有: " +
				// todoEmps1 + " .", null, SendReturnMsgType.Info);
			}

			return true;
		}

		throw new RuntimeException("@不应该运行到这里，DealTeamUpNode。");
	}

	/**
	 * 通知主持人
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String DealAlertZhuChiRen() throws Exception {
		
		// 有两个待办，就说明当前人员是最后一个会签人，就要把主持人的状态设置为0
		// 获得主持人信息.
		String sql = "SELECT FK_Emp FROM WF_GenerWorkerList WHERE IsPass=90 AND WorkID=" + this.getWorkID();
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
			return "@您已经会签完毕.";

		// 从会签列表里移动到待办.
		BP.DA.DBAccess
				.RunSQL("UPDATE WF_GenerWorkerList SET IsPass=0 WHERE WorkID='" + this.getWorkID() + "' AND IsPass=90");

		// 发消息.
		String fk_emp = dt.Rows.get(0).getValue("FK_Emp").toString();
		BP.WF.Dev2Interface.Port_SendMsg(fk_emp, "工作会签完毕", this.getHisGenerWorkFlow().getTitle() + " 工作会签完毕,请到待办查看.",
				"HuiQian" + this.getWorkID() + "_" + WebUser.getNo(), "HuiQian", getHisGenerWorkFlow().getFK_Flow(),
				this.getHisGenerWorkFlow().getFK_Node(), this.getWorkID(), 0);

		// 删除回签人的信息在generworkerlist里面，不然就会显示到他的下一级的退回列表里.
		sql = "DELETE FROM WF_GenerWorkerList WHERE WorkID=" + this.getWorkID() + " AND FK_Node="
				+ this.getHisNode().getNodeID() + " AND FK_Emp!='" + fk_emp + "'";
		DBAccess.RunSQL(sql);

		// 设置为未读.
		BP.WF.Dev2Interface.Node_SetWorkUnRead(this.getHisGenerWorkFlow().getWorkID());

		return "您是最后一个会签该工作的处理人，已经提醒主持人处理当前工作。";
	}

	/**
	 * 如果是协作
	 * @throws Exception 
	 * 
	 */
	public final boolean TeamupGroupLeader() throws Exception {
		GenerWorkerLists gwls = new GenerWorkerLists();
		gwls.Retrieve(GenerWorkerListAttr.WorkID, this.getWorkID(), GenerWorkerListAttr.FK_Node,
				this.getHisNode().getNodeID(), GenerWorkerListAttr.IsPass);

		if (gwls.size() == 1) {
			return false; // 让其向下执行,因为只有一个人。就没有顺序的问题.
		}

		// 判断自己是否是组长？如果是组长，就让返回false, 让其运动到最后一个节点，因为组长同意了，就全部同意了。
		String sql = "SELECT COUNT(No) AS num FROM Port_Dept WHERE No='" + WebUser.getFK_Dept() + "' AND Leader='"
				+ WebUser.getNo() + "'";
		if (BP.DA.DBAccess.RunSQLReturnValInt(sql, 0) == 1)
			return false;

		// 查看是否我是最后一个？
		int num = 0;
		String todoEmps = ""; // 记录没有处理的人.
		for (GenerWorkerList item : gwls.ToJavaList()) {
			if (item.getIsPassInt() == 0 || item.getIsPassInt() == 99) {
				if (!item.getFK_Emp().equals(WebUser.getNo())) {
					todoEmps += BP.WF.Glo.DealUserInfoShowModel(item.getFK_Emp(), item.getFK_EmpText()) + " ";
				}
				num++;
			}
		}

		if (num == 1) {
			this.getHisGenerWorkFlow()
					.setSender(BP.WF.Glo.DealUserInfoShowModel(BP.Web.WebUser.getNo(), BP.Web.WebUser.getName()));
			return false; // 只有一个待办,说明自己就是最后的一个人.
		}

		// 把当前的待办设置已办，并且提示未处理的人。
		for (GenerWorkerList gwl : gwls.ToJavaList()) {

			if (!gwl.getFK_Emp().equals(WebUser.getNo())) {
				continue;
			}

			// 设置当前不可以用.
			gwl.setIsPassInt(1);
			gwl.Update();

			// 检查完成条件。
			if (this.getHisNode().getIsEndNode() == false) {
				this.CheckCompleteCondition();
			}

			// 写入日志.
			this.AddToTrack(ActionType.TeampUp, gwl.getFK_Emp(), todoEmps, this.getHisNode().getNodeID(),
					this.getHisNode().getName(), "协作发送");

			// 处理会签问题， @于庆海翻译,增加如下代码.
			if (num == 2) {
				String msg = this.DealAlertZhuChiRen();
				this.addMsg(SendReturnMsgFlag.OverCurr, msg, null, SendReturnMsgType.Info);
			} else {
				this.addMsg(SendReturnMsgFlag.OverCurr, "当前工作未处理的人有: " + todoEmps + " .", null, SendReturnMsgType.Info);
			}

			// this.addMsg(SendReturnMsgFlag.OverCurr, "当前工作未处理的人有: " + todoEmps
			// + " .", null, SendReturnMsgType.Info);
			return true;
		}

		throw new RuntimeException("@不应该运行到这里。");
	}

	/**
	 * 处理队列节点
	 * 
	 * @return 是否可以向下发送?
	 * @throws Exception 
	 */
	public final boolean DealOradeNode() throws Exception {
		GenerWorkerLists gwls = new GenerWorkerLists();
		gwls.Retrieve(GenerWorkerListAttr.WorkID, this.getWorkID(), GenerWorkerListAttr.FK_Node,
				this.getHisNode().getNodeID(), GenerWorkerListAttr.IsPass);

		if (gwls.size() == 1) {
			return false; // 让其向下执行,因为只有一个人。就没有顺序的问题.
		}

		int idx = -100;
		for (GenerWorkerList gwl : gwls.ToJavaList()) {
			idx++;
			if (!gwl.getFK_Emp().equals(WebUser.getNo())) {
				continue;
			}

			// 设置当前不可以用.
			gwl.setIsPassInt(idx);
			gwl.Update();
		}

		for (GenerWorkerList gwl : gwls.ToJavaList()) {
			if (gwl.getIsPassInt() > 10) {
				// 就开始发到这个人身上.
				gwl.setIsPassInt(0);
				gwl.Update();

				// 检查完成条件。
				if (this.getHisNode().getIsEndNode() == false) {
					this.CheckCompleteCondition();
				}
				// 写入日志.
				this.AddToTrack(ActionType.Order, gwl.getFK_Emp(), gwl.getFK_EmpText(), this.getHisNode().getNodeID(),
						this.getHisNode().getName(), "队列发送");

				this.addMsg(SendReturnMsgFlag.VarAcceptersID, gwl.getFK_Emp(), gwl.getFK_Emp(),
						SendReturnMsgType.SystemMsg);
				this.addMsg(SendReturnMsgFlag.VarAcceptersName, gwl.getFK_EmpText(), gwl.getFK_EmpText(),
						SendReturnMsgType.SystemMsg);
				this.addMsg(SendReturnMsgFlag.OverCurr,
						"当前工作已经发送给(" + gwl.getFK_Emp() + "," + gwl.getFK_EmpText() + ").", null,
						SendReturnMsgType.Info);

				// 执行更新.
				if (this.getHisGenerWorkFlow().getEmps().contains("@" + WebUser.getNo() + "@") == false) {
					this.getHisGenerWorkFlow().setEmps(this.getHisGenerWorkFlow().getEmps() + WebUser.getNo() + "@");
				}

				this.rptGe.setFlowEmps(this.getHisGenerWorkFlow().getEmps());
				this.rptGe.setWFState(WFState.Runing);

				this.rptGe.Update(GERptAttr.FlowEmps, this.rptGe.getFlowEmps(), GERptAttr.WFState,
						WFState.Runing.getValue());

				this.getHisGenerWorkFlow().setWFState(WFState.Runing);
				this.getHisGenerWorkFlow().Update();
				return true;
			}
		}

		// 如果是最后一个，就要他向下发送。
		return false;
	}

	// / <summary>
	// / 如果是协作
	// / </summary>
	public boolean DealTeamupGroupLeader() throws Exception {
		GenerWorkerLists gwls = new GenerWorkerLists();
		gwls.Retrieve(GenerWorkerListAttr.WorkID, this.getWorkID(), GenerWorkerListAttr.FK_Node,
				this.getHisNode().getNodeID(), GenerWorkerListAttr.IsPass);

		if (gwls.size() == 1)
			return false; /* 让其向下执行,因为只有一个人,就没有顺序的问题. */

		// #region 判断自己是否是组长？如果是组长，就让返回false, 让其运动到最后一个节点，因为组长同意了，就全部同意了。
		if (this.getHisNode().getTeamLeaderConfirmRole() == TeamLeaderConfirmRole.ByDeptFieldLeader) {
			String sql = "SELECT COUNT(No) AS num FROM Port_Dept WHERE Leader='" + WebUser.getNo() + "'";
			if (BP.DA.DBAccess.RunSQLReturnValInt(sql, 0) == 1)
				return false;
		}

		if (this.getHisNode().getTeamLeaderConfirmRole() == TeamLeaderConfirmRole.BySQL) {
			String sql = this.getHisNode().getTeamLeaderConfirmDoc();
			sql = Glo.DealExp(sql, this.getHisWork(), null);
			sql = sql.replaceAll("~", "'");
			sql = sql.replaceAll("@WorkID", String.valueOf(this.getWorkID()));
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);

			String userNo = WebUser.getNo();
			for (DataRow dr : dt.Rows) {
				String str = dr.getValue("No").toString();
				if (str == userNo)
					return false;
			}
		}

		if (this.getHisNode().getTeamLeaderConfirmRole() == TeamLeaderConfirmRole.HuiQianLeader) {
			if (this.getHisGenerWorkFlow().getTodoEmps().contains(BP.Web.WebUser.getNo() + ",") == true) {
				/* 当前人是组长，检查是否可以可以发送,检查自己是否是最后一个人 ？ */
				String todoEmps = ""; // 记录没有处理的人.
				int num = 0;
				for (GenerWorkerList item : gwls.ToJavaList()) {
					if (item.getIsPassInt() == 0 || item.getIsPassInt() == 90) {
						if (item.getFK_Emp() != WebUser.getNo())
							todoEmps += BP.WF.Glo.DealUserInfoShowModel(item.getFK_Emp(), item.getFK_EmpText()) + " ";
						num++;
					}
				}

				if (num == 1) {
					this.getHisGenerWorkFlow().setSender(
							BP.WF.Glo.DealUserInfoShowModel(BP.Web.WebUser.getNo(), BP.Web.WebUser.getName()));
					this.getHisGenerWorkFlow().setHuiQianTaskSta(HuiQianTaskSta.None);
					return false; /* 只有一个待办,说明自己就是最后的一个人. */
				}

				this.addMsg(SendReturnMsgFlag.CondInfo, "@当前工作未处理的会签人有: " + todoEmps + ",您不能执行发送.", null,
						SendReturnMsgType.Info);
				return true;
			}
		}
		// #endregion

		// 查看是否我是最后一个？
		int mynum = 0;
		String todoEmps1 = ""; // 记录没有处理的人.
		for (GenerWorkerList item : gwls.ToJavaList()) {
			if (item.getIsPassInt() == 0 || item.getIsPassInt() == 90) {
				if (item.getFK_Emp() != WebUser.getNo())
					todoEmps1 += BP.WF.Glo.DealUserInfoShowModel(item.getFK_Emp(), item.getFK_EmpText()) + " ";
				mynum++;
			}
		}

		if (mynum == 1) {
			this.getHisGenerWorkFlow()
					.setSender(BP.WF.Glo.DealUserInfoShowModel(BP.Web.WebUser.getNo(), BP.Web.WebUser.getName()));
			return false; /* 只有一个待办,说明自己就是最后的一个人. */
		}

		// 把当前的待办设置已办，并且提示未处理的人。
		for (GenerWorkerList gwl : gwls.ToJavaList()) {
			if (!gwl.getFK_Emp().equals(WebUser.getNo()))
				continue;

			// 设置当前已经完成.
			gwl.setIsPassInt(1);
			gwl.Update();

			// 检查完成条件。
			if (this.getHisNode().getIsEndNode() == false)
				this.CheckCompleteCondition();

			// 执行时效考核.
			if (this.rptGe == null)
				Glo.InitCH(this.getHisFlow(), this.getHisNode(), this.getWorkID(), this.rptGe.getFID(),
						this.rptGe.getTitle(), gwl);
			else
				Glo.InitCH(this.getHisFlow(), this.getHisNode(), this.getWorkID(), 0,
						this.getHisGenerWorkFlow().getTitle(), gwl);

			this.AddToTrack(ActionType.TeampUp, gwl.getFK_Emp(), todoEmps1, this.getHisNode().getNodeID(),
					this.getHisNode().getName(), "协作发送");

			// cut 当前的人员.
			String emps = this.getHisGenerWorkFlow().getTodoEmps();
			emps = emps.replaceAll(WebUser.getName() + ";", "");
			this.getHisGenerWorkFlow().setTodoEmps(emps);
			this.getHisGenerWorkFlow().DirectUpdate();

			// 处理会签问题，
			if (mynum == 2) {
				String msg = this.DealAlertZhuChiRen();
				this.addMsg(SendReturnMsgFlag.OverCurr, msg, null, SendReturnMsgType.Info);
			} else {
				this.addMsg(SendReturnMsgFlag.OverCurr, "@会签工作已完成@当前工作未处理的会签人有:" + todoEmps1 + " .", null,
						SendReturnMsgType.Info);
			}
			return true;
		}
		throw new RuntimeException("@不应该运行到这里。");
	}

	/**
	 * 检查阻塞模式
	 * 
	 * @return 返回是否可以向下发送.
	 * @throws Exception 
	 */
	private boolean CheckBlockModel() throws Exception {
		if (this.getHisNode().getBlockModel() == BlockModel.None) {
			return true;
		}

		if (this.getHisNode().getBlockModel() == BlockModel.CurrNodeAll) {
			// 如果设置检查是否子流程结束.
			GenerWorkFlows gwls = new GenerWorkFlows();
			if (this.getHisNode().getHisRunModel() == RunModel.SubThread) {
				// 如果是子线程,仅仅检查自己子线程上发起的workid.
				QueryObject qo = new QueryObject(gwls);
				qo.AddWhere(GenerWorkFlowAttr.PWorkID, this.getWorkID());
				qo.addAnd();
				qo.AddWhere(GenerWorkFlowAttr.PNodeID, this.getHisNode().getNodeID());
				qo.addAnd();
				qo.AddWhere(GenerWorkFlowAttr.PFlowNo, this.getHisFlow().getNo());
				qo.addAnd();
				qo.AddWhere(GenerWorkFlowAttr.WFSta, WFSta.Runing.getValue());
				qo.DoQuery();
				if (gwls.size() == 0) {
					return true;
				}
			} else {
				// 检查，以前的子线程是否发起过流程 与以前的分子线程节点是否发起过子流程。
				QueryObject qo = new QueryObject(gwls);

				qo.addLeftBracket();
				qo.AddWhere(GenerWorkFlowAttr.PFID, this.getWorkID());
				qo.addOr();
				qo.AddWhere(GenerWorkFlowAttr.PWorkID, this.getWorkID());
				qo.addRightBracket();

				qo.addAnd();

				qo.addLeftBracket();
				qo.AddWhere(GenerWorkFlowAttr.PNodeID, this.getHisNode().getNodeID());
				qo.addAnd();
				qo.AddWhere(GenerWorkFlowAttr.PFlowNo, this.getHisFlow().getNo());
				qo.addAnd();
				qo.AddWhere(GenerWorkFlowAttr.WFSta, WFSta.Runing.getValue());
				qo.addRightBracket();

				qo.DoQuery();
				if (gwls.size() == 0) {
					return true;
				}
			}

			String err = "";
			err += "@如下子流程没有完成，你不能向下发送。@---------------------------------";
			for (GenerWorkFlow gwf : gwls.ToJavaList()) {
				err += "@流程ID=" + gwf.getWorkID() + ",标题:" + gwf.getTitle() + ",当前执行人:" + gwf.getTodoEmps() + ",运行到节点:"
						+ gwf.getNodeName();
			}

			if (DotNetToJavaStringHelper.isNullOrEmpty(err) == true) {
				return true;
			}

			err = Glo.DealExp(this.getHisNode().getBlockAlert(), this.rptGe, null) + err;
			throw new RuntimeException(err);
		}

		if (this.getHisNode().getBlockModel() == BlockModel.SpecSubFlow) {
			// 如果按照特定的格式判断阻塞
			String exp = this.getHisNode().getBlockExp();
			if (exp.contains("@") == false) {
				throw new RuntimeException("@设置错误，该节点的阻塞配置格式错误，请参考帮助来解决。");
			}

			String[] strs = exp.split("[@]", -1);
			String err = "";
			for (String str : strs) {
				if (DotNetToJavaStringHelper.isNullOrEmpty(str) == true) {
					continue;
				}

				if (str.contains("=") == false) {
					throw new RuntimeException("@阻塞设置的格式不正确。" + str);
				}

				String[] nodeFlow = str.split("[=]", -1);
				int nodeid = Integer.parseInt(nodeFlow[0]); // 启动子流程的节点.
				String subFlowNo = nodeFlow[1];

				GenerWorkFlows gwls = new GenerWorkFlows();

				if (this.getHisNode().getHisRunModel() == RunModel.SubThread) {
					// 如果是子线程，就不需要管，主干节点的问题。
					QueryObject qo = new QueryObject(gwls);
					qo.AddWhere(GenerWorkFlowAttr.PWorkID, this.getWorkID());
					qo.addAnd();
					qo.AddWhere(GenerWorkFlowAttr.PNodeID, nodeid);
					qo.addAnd();
					qo.AddWhere(GenerWorkFlowAttr.PFlowNo, this.getHisFlow().getNo());
					qo.addAnd();
					qo.AddWhere(GenerWorkFlowAttr.FK_Flow, subFlowNo);
					qo.addAnd();
					qo.AddWhere(GenerWorkFlowAttr.WFSta, WFSta.Runing.getValue());

					qo.DoQuery();
					if (gwls.size() == 0) {
						continue;
					}
				} else {
					// 非子线程，就需要考虑，从该节点上，发起的子线程的 ，主干节点的问题。
					QueryObject qo = new QueryObject(gwls);

					qo.addLeftBracket();
					qo.AddWhere(GenerWorkFlowAttr.PFID, this.getWorkID());
					qo.addOr();
					qo.AddWhere(GenerWorkFlowAttr.PWorkID, this.getWorkID());
					qo.addRightBracket();

					qo.addAnd();

					qo.addLeftBracket();
					qo.AddWhere(GenerWorkFlowAttr.PNodeID, nodeid);
					qo.addAnd();
					qo.AddWhere(GenerWorkFlowAttr.PFlowNo, this.getHisFlow().getNo());
					qo.addAnd();
					qo.AddWhere(GenerWorkFlowAttr.WFSta, WFSta.Runing.getValue());
					qo.addAnd();
					qo.AddWhere(GenerWorkFlowAttr.FK_Flow, subFlowNo);
					qo.addRightBracket();

					qo.DoQuery();
					if (gwls.size() == 0) {
						continue;
					}
				}

				err += "@如下子流程没有完成，你不能向下发送。@---------------------------------";
				for (GenerWorkFlow gwf : gwls.ToJavaList()) {
					err += "@子流程ID=" + gwf.getWorkID() + ",子流程名称" + gwf.getFlowName() + ",标题:" + gwf.getTitle()
							+ ",当前执行人:" + gwf.getTodoEmps() + ",运行到节点:" + gwf.getNodeName();
				}
			}

			if (DotNetToJavaStringHelper.isNullOrEmpty(err) == true) {
				return true;
			}

			err = Glo.DealExp(this.getHisNode().getBlockAlert(), this.rptGe, null) + err;
			throw new RuntimeException(err);
		}

		if (this.getHisNode().getBlockModel() == BlockModel.BySQL) {
			// 按 sql 判断阻塞
			java.math.BigDecimal d = DBAccess.RunSQLReturnValDecimal(
					Glo.DealExp(this.getHisNode().getBlockExp(), this.rptGe, null), new BigDecimal(0), 1);
			if (d.compareTo(new BigDecimal(0)) >= 0) {
				throw new RuntimeException("@" + Glo.DealExp(this.getHisNode().getBlockAlert(), this.rptGe, null));
			}
			return true;
		}

		if (this.getHisNode().getBlockModel() == BlockModel.ByExp) {
			// 按表达式阻塞. 格式为: @ABC=123
			// this.MsgOfCond = "@以表单值判断方向，值 " + en.EnDesc + "." + this.AttrKey
			// + " (" + en.GetValStringByKey(this.AttrKey) + ") 操作符:(" +
			// this.FK_Operator + ") 判断值:(" + this.OperatorValue.ToString() +
			// ")";
			String exp = this.getHisNode().getBlockExp();
			String[] strs = exp.trim().split("[ ]", -1);

			String key = strs[0].trim();
			String oper = strs[1].trim();
			String val = strs[2].trim();
			val = val.replace("'", "");
			val = val.replace("%", "");
			val = val.replace("~", "");

			BP.En.Row row = this.rptGe.getRow();

			String valPara = null;
			if (row.containsKey(key) == false) {
				try {
					// 如果不包含指定的关键的key, 就到公共变量里去找.
					if (Glo.getSendHTOfTemp().containsKey(key) == false) {
						throw new RuntimeException("@判断条件时错误,请确认参数是否拼写错误,没有找到对应的表达式:" + exp + " Key=(" + key
								+ ") oper=(" + oper + ")Val=(" + val + ")");
					}
					valPara = Glo.getSendHTOfTemp().get(key).toString().trim();
				} catch (java.lang.Exception e) {
					// 有可能是常量.
					valPara = key;
				}
			} else {
				valPara = row.get(key).toString().trim();
			}

			/// #region 开始执行判断.
			if (oper.equals("=")) {
				if (val.equals(valPara)) {
					return true;
				} else {
					throw new RuntimeException("@" + Glo.DealExp(this.getHisNode().getBlockAlert(), this.rptGe, null));
				}

			}

			if (oper.toUpperCase().equals("LIKE")) {
				if (valPara.contains(val)) {
					return true;
				} else {
					throw new RuntimeException("@" + Glo.DealExp(this.getHisNode().getBlockAlert(), this.rptGe, null));
				}

			}

			if (oper.equals(">")) {
				if (Float.parseFloat(valPara) > Float.parseFloat(val)) {
					return true;
				} else {
					throw new RuntimeException("@" + Glo.DealExp(this.getHisNode().getBlockAlert(), this.rptGe, null));
				}

			}
			if (oper.equals(">=")) {
				if (Float.parseFloat(valPara) >= Float.parseFloat(val)) {
					return true;
				} else {
					throw new RuntimeException("@" + Glo.DealExp(this.getHisNode().getBlockAlert(), this.rptGe, null));
				}

			}
			if (oper.equals("<")) {
				if (Float.parseFloat(valPara) < Float.parseFloat(val)) {
					return true;
				} else {
					throw new RuntimeException("@" + Glo.DealExp(this.getHisNode().getBlockAlert(), this.rptGe, null));
				}

			}
			if (oper.equals("<=")) {
				if (Float.parseFloat(valPara) <= Float.parseFloat(val)) {
					return true;
				} else {
					throw new RuntimeException("@" + Glo.DealExp(this.getHisNode().getBlockAlert(), this.rptGe, null));
				}

			}

			if (oper.equals("!=")) {
				if (Float.parseFloat(valPara) != Float.parseFloat(val)) {
					return true;
				} else {
					throw new RuntimeException("@" + Glo.DealExp(this.getHisNode().getBlockAlert(), this.rptGe, null));
				}

			}
			throw new RuntimeException("@参数格式错误:" + exp + " Key=" + key + " oper=" + oper + " Val=" + val);

			/// #endregion 开始执行判断.
		}

		throw new RuntimeException("@该阻塞模式没有实现...");
	}

	/// <summary>
	/// 发送到延续子流程.
	/// </summary>
	/// <param name="node"></param>
	/// <param name="toEmps"></param>
	/// <returns></returns>
	private SendReturnObjs NodeSendToYGFlow(Node node, String toEmpIDs) throws Exception{		
            String sql = "";
            if (StringUtils.isBlank(toEmpIDs)) {
                toEmpIDs = "";

                DataTable dt = null;

            
                //按照岗位与部门的交集.
                if (node.getHisDeliveryWay() == DeliveryWay.ByDeptAndStation)
                {
                    
                        sql = "SELECT pdes.FK_Emp AS No"
                              + " FROM   Port_DeptEmpStation pdes"
                              + " INNER JOIN WF_NodeDept wnd ON wnd.FK_Dept = pdes.FK_Dept"
                              + " AND wnd.FK_Node = " + node.getNodeID()
                              + " INNER JOIN WF_NodeStation wns ON  wns.FK_Station = pdes.FK_Station"
                              + " AND wns.FK_Node =" + node.getNodeID()
                              + " ORDER BY pdes.FK_Emp";

                        dt = DBAccess.RunSQLReturnTable(sql);
                    

                    if (dt.Rows.size() == 0)
						try {
							throw new Exception("@节点访问规则(" + node.getHisDeliveryWay().toString() + ")错误:节点(" + node.getNodeID() + "," + node.getNodeID() + "), 按照岗位与部门的交集确定接受人的范围错误，没有找到人员:SQL=" + sql);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                }

               //仅按岗位计算
                if (node.getHisDeliveryWay() == DeliveryWay.ByStationOnly)
                {
                    sql = "SELECT A.FK_Emp No FROM " + BP.WF.Glo.getEmpStation() + " A, WF_NodeStation B WHERE A.FK_Station=B.FK_Station AND B.FK_Node=" + dbStr + "FK_Node ORDER BY A.FK_Emp";
                    ps = new Paras();
                    ps.Add("FK_Node", node.getNodeID());
                    ps.SQL = sql;
                    dt = DBAccess.RunSQLReturnTable(ps);
                    if (dt.Rows.size() == 0)
						try {
							throw new Exception("@节点访问规则错误:节点(" + node.getNodeID() + "," + node.getName() + "), 仅按岗位计算，没有找到人员:SQL=" + ps.getSQLNoPara());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                }
                //按绑定的人计算
                if (node.getHisDeliveryWay() == DeliveryWay.ByBindEmp)
                {
                    ps = new Paras();
                    ps.Add("FK_Node", node.getNodeID());
                    ps.SQL = "SELECT FK_Emp No FROM WF_NodeEmp WHERE FK_Node=" + dbStr + "FK_Node ORDER BY FK_Emp";
                    dt = DBAccess.RunSQLReturnTable(ps);
                    if (dt.Rows.size() == 0)
						try {
							throw new Exception("@流程设计错误:下一个节点(" + town._HisNode.getName() + ")没有绑定工作人员 . ");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                }
                
                
                //仅按照部门计算.
                if (node.getHisDeliveryWay() == DeliveryWay.ByDept)
        		{
        			ps = new Paras();
        			ps.Add("FK_Node", node.getNodeID());
        			ps.Add("WorkID", this._WorkID);
        			ps.SQL = "SELECT FK_Emp as No FROM WF_SelectAccper WHERE FK_Node=" + dbStr + "FK_Node AND WorkID=" + dbStr + "WorkID AND AccType=0 ORDER BY IDX";
        			dt = DBAccess.RunSQLReturnTable(ps);
        			if (dt.Rows.size() == 0)
        			{
        				  
        				ps = new Paras();
        				ps.SQL = "SELECT  A.No, A.Name  FROM Port_Emp A, WF_NodeDept B WHERE A.FK_Dept=B.FK_Dept AND B.FK_Node=" + dbStr + "FK_Node";
        				ps.Add("FK_Node", node.getNodeID());
        				dt = DBAccess.RunSQLReturnTable(ps);
        			}        				 
        		}
                
                
                //按照人员选择
                if (node.getHisDeliveryWay() == DeliveryWay.BySelected || dt==null ){
                    sql = "SELECT FK_Emp No, EmpName Name FROM WF_SelectAccper WHERE FK_Node=" + node.getNodeID() + " AND WorkID=" + this.getWorkID() + " AND AccType=0";
                    dt = DBAccess.RunSQLReturnTable(sql);
                    if (dt.Rows.size() == 0){
                        try {
							throw new Exception("@没有为延续子流程设置接受人.");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }
                    
                }

                
                //if (dt ==null)                 
                	//return "err@流程设计错误,延续流程仅仅可以设置按照.";
                
                
                //组装到达的人员.
                for (DataRow dr : dt.Rows)
                {
                    toEmpIDs += dr.getValue("No").toString();
                }
            

            if (toEmpIDs == "")
				try {
					throw new Exception("@延续子流程目前仅仅支持选择接受人方式");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        long workid = BP.WF.Dev2Interface.Node_CreateBlankWork(node.getFK_Flow(), null, null,
            toEmpIDs, null, this.getWorkID(), 0, this.getHisNode().getFK_Flow(), this.getHisNode().getNodeID(), BP.Web.WebUser.getNo(), 0, null);

        //复制当前信息.
        Work wk = node.getHisWork();
        wk.setOID(workid);
        wk.RetrieveFromDBSources();
        wk.Copy(this.getHisWork());
        wk.Update();

        //为接受人显示待办.
        BP.WF.Dev2Interface.Node_SetDraft2Todolist(node.getFK_Flow()
        		, workid);

        //设置变量.
       // this.addMsg(SendReturnMsgFlag.VarToNodeID, node.getNodeID().toString(), workid.toString(), SendReturnMsgType.SystemMsg);
        this.addMsg(SendReturnMsgFlag.VarAcceptersID, toEmpIDs, toEmpIDs, SendReturnMsgType.SystemMsg);

        //设置消息.
        this.addMsg("Msg1", "子流程(" + node.getFlowName() + ")已经启动,发送给{" + toEmpIDs + "}处理人.");
        //this.addMsg(SendReturnMsgFlag.MsgOfText, "需要等待子流程完成后，该流程才能向下运动。");
        this.addMsg("Msg2", "当前您的待办已经消失,需要等待子流程完成后您的待办才能显示,您可以从在途里查看工作进度.");


        //设置当前工作操作员不可见.
        sql = "UPDATE WF_GenerWorkerList SET IsPass=80 WHERE WorkID=" + this.getWorkID() + " AND IsPass=0";
        BP.DA.DBAccess.RunSQL(sql);

        return HisMsgObjs;
    }
	/**
	 * 工作流发送业务处理. 升级日期:2012-11-11. 升级原因:代码逻辑性不清晰,有遗漏的处理模式. 修改人:zhoupeng.
	 * 修改地点:厦门. ----------------------------------- 说明
	 * ----------------------------- 1，方法体分为三大部分: 发送前检查\5*5算法\发送后的业务处理. 2,
	 * 详细请参考代码体上的说明. 3, 发送后可以直接获取它的
	 * 
	 * @param jumpToNode
	 *            要跳转的节点,可以为空.
	 * @param jumpToEmp
	 *            要跳转的人,可以为空.
	 * @return 返回执行结果
	 * @throws Exception 
	 */
	public final SendReturnObjs NodeSend(Node jumpToNode, String jumpToEmp) throws Exception {
		
		DBAccess.DoTransactionCommit();
		if (this.getHisNode().getIsGuestNode()) {
			if (!this.getExecer().equals("Guest")) {
				throw new RuntimeException(
						"@当前节点（" + this.getHisNode().getName() + "）是客户执行节点，所以当前登录人员应当是Guest,现在是:" + this.getExecer());
			}
		}

		/// #region 安全性检查.
		//// 第1: 检查是否可以处理当前的工作.
		// if (this.HisNode.IsStartNode == false
		// && BP.WF.Dev2Interface.Flow_IsCanDoCurrentWork(this.HisNode.FK_Flow,
		//// this.HisNode.NodeID,
		// this.WorkID, this.Execer) == false)
		// throw new Exception("@当前工作您已经处理完成，或者您(" + this.Execer + " " +
		//// this.ExecerName + ")没有处理当前工作的权限。");

		// 第1.2: 调用发起前的事件接口,处理用户定义的业务逻辑.
		Work wk=this.getHisWork();
		wk.setOID(this.getWorkID());
		
		String sendWhen = this.getHisFlow().DoFlowEventEntity(EventListOfNode.SendWhen, this.getHisNode(),
				wk, null);

		// 返回格式. @Info=xxxx@ToNodeID=xxxx@ToEmps=xxxx
		if (sendWhen != null && sendWhen.indexOf("@") >= 0) {
			AtPara ap = new AtPara(sendWhen);
			int nodeid = ap.GetValIntByKey("ToNodeID", 0);
			if (nodeid != 0)
				jumpToNode = new Node(nodeid);

			// 监测是否有停止流程的标志？
			this.setIsStopFlow(ap.GetValBoolenByKey("IsStopFlow", false));

			String toEmps = ap.GetValStrByKey("ToEmps");
			if (StringHelper.isNullOrEmpty(toEmps) == true)
				jumpToEmp = toEmps;

			// 处理str信息.
			sendWhen = sendWhen.replace("@Info=", "");
			sendWhen = sendWhen.replace("@IsStopFlow=1", "");
			sendWhen = sendWhen.replace("@ToNodeID=" + nodeid, "");
			sendWhen = sendWhen.replace("@ToEmps=" + toEmps, "");
		}
		if (sendWhen != null) {
			// 说明有事件要执行,把执行后的数据查询到实体里
			this.getHisWork().RetrieveFromDBSources();
			this.getHisWork().ResetDefaultVal();
			this.getHisWork().setRec(this.getExecer());
			this.getHisWork().setRecText(this.getExecerName());
			if (DotNetToJavaStringHelper.isNullOrEmpty(sendWhen) == false) {
				// sendWhen = System.Web.HttpUtility.UrlDecode(sendWhen);
				if (sendWhen.startsWith("false") || sendWhen.startsWith("False") || sendWhen.startsWith("error")
						|| sendWhen.startsWith("Error")) {
					this.addMsg(SendReturnMsgFlag.SendWhen, sendWhen);
					sendWhen = sendWhen.replace("false", "");
					sendWhen = sendWhen.replace("False", "");

					throw new RuntimeException("@执行发送前事件失败:" + sendWhen);
				}
			}

			// 把发送sendWhen 消息提示给用户.
			this.addMsg("SendWhen", sendWhen, sendWhen, SendReturnMsgType.Info);
		}

		// #endregion 安全性检查.

		// 加入系统变量.
		this.addMsg(SendReturnMsgFlag.VarCurrNodeID, (new Integer(this.getHisNode().getNodeID())).toString(),
				(new Integer(this.getHisNode().getNodeID())).toString(), SendReturnMsgType.SystemMsg);
		this.addMsg(SendReturnMsgFlag.VarCurrNodeName, this.getHisNode().getName(), this.getHisNode().getName(),
				SendReturnMsgType.SystemMsg);
		this.addMsg(SendReturnMsgFlag.VarWorkID, (new Long(this.getWorkID())).toString(),
				(new Long(this.getWorkID())).toString(), SendReturnMsgType.SystemMsg);

		if (this.getIsStopFlow() == true) {
			// 在检查完后，反馈来的标志流程已经停止了。

			// 查询出来当前节点的工作报表.
			this.rptGe = this.getHisFlow().getHisGERpt();
			this.rptGe.SetValByKey("OID", this.getWorkID());
			this.rptGe.RetrieveFromDBSources();

			this.Func_DoSetThisWorkOver();
			this.rptGe.setWFState(WFState.Complete);
			this.rptGe.Update();
			this.getHisGenerWorkFlow().Update(); // added by
													// liuxc,2016-10=24,最后节点更新Sender字段
			// 执行考核
			Glo.InitCH(this.getHisFlow(), this.getHisNode(), this.getWorkID(), 0, this.getHisGenerWorkFlow().getTitle(),
					null);
			return this.HisMsgObjs;
		}

		// 设置跳转节点，如果有可以为null.
		this.JumpToNode = jumpToNode;
		this.JumpToEmp = jumpToEmp;

		String sql = null;
		java.util.Date dt = new java.util.Date();
		this.getHisWork().setRec(this.getExecer());
		this.setWorkID(this.getHisWork().getOID());

		/// #region 第一步: 检查当前操作员是否可以发送: 共分如下 3 个步骤.
		// 第1.2.1: 如果是开始节点，就要检查发起流程限制条件.
		if (this.getHisNode().getIsStartNode()) {
			if (Glo.CheckIsCanStartFlow_SendStartFlow(this.getHisFlow(), this.getHisWork()) == false) {
				throw new RuntimeException(
						"@违反了流程发起限制条件:" + Glo.DealExp(this.getHisFlow().getStartLimitAlert(), this.getHisWork(), null));
			}
		}

		// 第1.3: 判断当前流程状态.
		if (this.getHisNode().getIsStartNode() == false && this.getHisGenerWorkFlow().getWFState() == WFState.Askfor) {
			// 如果是加签状态, 就判断加签后，是否要返回给执行加签人.
			GenerWorkerLists gwls = new GenerWorkerLists();
			gwls.Retrieve(GenerWorkerListAttr.FK_Node, this.getHisNode().getNodeID(), GenerWorkerListAttr.WorkID,
					this.getWorkID());

			boolean isDeal = false;
			AskforHelpSta askForSta = AskforHelpSta.AfterDealSend;
			for (GenerWorkerList item : gwls.ToJavaList()) {
				if (item.getIsPassInt() == AskforHelpSta.AfterDealSend.getValue()) {
					// 如果是加签后，直接发送就不处理了。
					isDeal = true;
					askForSta = AskforHelpSta.AfterDealSend;

					// 更新workerlist, 设置所有人员的状态为已经处理的状态,让它走到下一步骤去.
					DBAccess.RunSQL("UPDATE WF_GenerWorkerList SET IsPass=1 WHERE FK_Node="
							+ this.getHisNode().getNodeID() + " AND WorkID=" + this.getWorkID());

					// 写入日志.
					this.AddToTrack(ActionType.ForwardAskfor, item.getFK_Emp(), item.getFK_EmpText(),
							this.getHisNode().getNodeID(), this.getHisNode().getName(), "加签后向下发送，直接发送给下一步处理人。");
				}

				if (item.getIsPassInt() == AskforHelpSta.AfterDealSendByWorker.getValue()) {
					// 如果是加签后，在由我直接发送。
					item.setIsPassInt(0);

					isDeal = true;
					askForSta = AskforHelpSta.AfterDealSendByWorker;

					// 更新workerlist, 设置所有人员的状态为已经处理的状态.
					DBAccess.RunSQL("UPDATE WF_GenerWorkerList SET IsPass=1 WHERE FK_Node="
							+ this.getHisNode().getNodeID() + " AND WorkID=" + this.getWorkID());

					// 把发起加签人员的状态更新过来，让他可见待办工作.
					item.setIsPassInt(0);
					item.Update();

					// 更新流程状态.
					this.getHisGenerWorkFlow().setWFState(WFState.AskForReplay);
					this.getHisGenerWorkFlow().Update();

					// 让加签人，设置成工作未读。
					BP.WF.Dev2Interface.Node_SetWorkUnRead(this.getWorkID(), item.getFK_Emp());

					// 从临时变量里获取回复加签意见.
					String replyInfo = this.getHisGenerWorkFlow().getParas_AskForReply();

					//// 写入日志.
					// this.AddToTrack(ActionType.ForwardAskfor, item.FK_Emp,
					//// item.FK_EmpText,
					// this.HisNode.NodeID, this.HisNode.Name,
					// "加签后向下发送，并转向加签人发起人（" + item.FK_Emp + "，" +
					//// item.FK_EmpText + "）。<br>意见:" + replyInfo);

					// 写入日志.
					this.AddToTrack(ActionType.ForwardAskfor, item.getFK_Emp(), item.getFK_EmpText(),
							this.getHisNode().getNodeID(), this.getHisNode().getName(), "回复意见:" + replyInfo);

					// 加入系统变量。
					this.addMsg(SendReturnMsgFlag.VarToNodeID, (new Integer(this.getHisNode().getNodeID())).toString(),
							SendReturnMsgType.SystemMsg);
					this.addMsg(SendReturnMsgFlag.VarToNodeName, this.getHisNode().getName(),
							SendReturnMsgType.SystemMsg);
					this.addMsg(SendReturnMsgFlag.VarAcceptersID, item.getFK_Emp(), SendReturnMsgType.SystemMsg);
					this.addMsg(SendReturnMsgFlag.VarAcceptersName, item.getFK_EmpText(), SendReturnMsgType.SystemMsg);

					// 加入提示信息.
					this.addMsg(SendReturnMsgFlag.SendSuccessMsg,
							"已经转给，加签的发起人(" + item.getFK_Emp() + "," + item.getFK_EmpText() + ")",
							SendReturnMsgType.Info);

					// 删除当前操作员临时增加的工作列表记录, 如果不删除就会导致第二次加签失败.
					GenerWorkerList gwl = new GenerWorkerList();
					gwl.Delete(GenerWorkerListAttr.FK_Node, this.getHisNode().getNodeID(), GenerWorkerListAttr.WorkID,
							this.getWorkID(), GenerWorkerListAttr.FK_Emp, this.getExecer());

					// 执行时效考核.
					if (this.rptGe == null) {
						Glo.InitCH(this.getHisFlow(), this.getHisNode(), this.getWorkID(), this.rptGe.getFID(),
								this.rptGe.getTitle(), null);
					} else {
						Glo.InitCH(this.getHisFlow(), this.getHisNode(), this.getWorkID(), 0,
								this.getHisGenerWorkFlow().getTitle(), null);
					}

					// 返回发送对象.
					return this.HisMsgObjs;
				}
			}

			if (isDeal == false) {
				throw new RuntimeException("@流程引擎错误，不应该找不到加签的状态.");
			}
		}

		// 第3: 如果是是合流点，有子线程未完成的情况.
		if (this.getHisNode().getIsHL() || this.getHisNode().getHisRunModel() == RunModel.FHL) {
			// 如果是合流点 检查当前是否是合流点如果是，则检查分流上的子线程是否完成。
			// 检查是否有子线程没有结束
			Paras ps = new Paras();
			ps.SQL = "SELECT WorkID,FK_Emp,FK_EmpText,FK_NodeText FROM WF_GenerWorkerList WHERE FID=" + ps.getDBStr()
					+ "FID AND IsPass=0 AND IsEnable=1";
			ps.Add(WorkAttr.FID, this.getWorkID());

			DataTable dtWL = DBAccess.RunSQLReturnTable(ps);
			String infoErr = "";
			if (dtWL.Rows.size() != 0) {
				if (this.getHisNode().getThreadKillRole() == ThreadKillRole.None
						|| this.getHisNode().getThreadKillRole() == ThreadKillRole.ByHand) {
					infoErr += "@您不能向下发送，有如下子线程没有完成。";
					for (DataRow dr : dtWL.Rows) {
						infoErr += "@操作员编号:" + dr.getValue("FK_Emp") + "," + dr.getValue("FK_EmpText") + ",停留节点:"
								+ dr.getValue("FK_NodeText");
					}

					if (this.getHisNode().getThreadKillRole() == ThreadKillRole.ByHand) {
						infoErr += "@请通知它们处理完成,或者强制删除子流程您才能向下发送.";
					} else {
						infoErr += "@请通知它们处理完成,您才能向下发送.";
					}

					// 抛出异常阻止它向下运动。
					throw new RuntimeException(infoErr);
				}

				if (this.getHisNode().getThreadKillRole() == ThreadKillRole.ByAuto) {
					// 删除每个子线程，然后向下运动。
					for (DataRow dr : dtWL.Rows) {
						BP.WF.Dev2Interface.Flow_DeleteSubThread(this.getHisFlow().getNo(),
								Long.parseLong(dr.get(0).toString()), "合流点发送时自动删除");
					}
				}
			}
		}

		/// #endregion 第一步: 检查当前操作员是否可以发送

		// 查询出来当前节点的工作报表.
		this.rptGe = this.getHisFlow().getHisGERpt();
		this.rptGe.SetValByKey("OID", this.getWorkID());
		this.rptGe.RetrieveFromDBSources();

		// 检查是否有子流程存在, 如果有就让子流程发送下去. and 2015-01-23. for gaoling.
		this.CheckBlockModel();

		// 检查FormTree必填项目,如果有一些项目没有填写就抛出异常.
		this.CheckFrmIsNotNull();

		// 处理自动运行 - 预先设置未来的运行节点.
		this.DealAutoRunEnable();

		// 把数据更新到数据库里.	 
		if (this.getHisNode().getHisFormType()== NodeFormType.FoolTruck)
			this.getHisWork().setSQLCash(null);
		
		this.getHisWork().DirectUpdate();
		 
		
		if (this.getHisWork().getEnMap().getPhysicsTable() != this.rptGe.getEnMap().getPhysicsTable()) {
			// 有可能外部参数传递过来导致，rpt表数据没有发生变化。
			this.rptGe.Copy(this.getHisWork());
			// 首先执行保存，不然会影响条件的判断 by dgq 2016-1-14
			this.rptGe.Update();
		}

		// 如果是队列节点, 就判断当前的队列人员是否走完。
		if (this.getTodolistModel() == TodolistModel.Order) {
			if (this.DealOradeNode() == true) {
				// if (this._transferCustom != null)
				// _transferCustom.Delete();

				// 执行时效考核.
				Glo.InitCH(this.getHisFlow(), this.getHisNode(), this.getWorkID(), this.rptGe.getFID(),
						this.rptGe.getTitle(), null);
				return this.HisMsgObjs;
			}
		}

		// 如果是协作模式节点, 就判断当前的队列人员是否走完.
		if (this.getTodolistModel() == TodolistModel.Teamup) {
			
			
			  // ,增加了此部分.
            String todoEmps = this.getHisGenerWorkFlow().getTodoEmps();
            todoEmps = todoEmps.replace(WebUser.getNo() + "," + WebUser.getName() + ";", "");
            this.getHisGenerWorkFlow().setTodoEmps( todoEmps);
            this.getHisGenerWorkFlow().Update(GenerWorkFlowAttr.TodoEmps, todoEmps);

            
			// 如果是协作.
			if (this.DealTeamUpNode() == true) {
				// if (this._transferCustom != null)
				// _transferCustom.Delete();

				// 执行时效考核.
				Glo.InitCH(this.getHisFlow(), this.getHisNode(), this.getWorkID(), this.rptGe.getFID(),
						this.rptGe.getTitle(), null);
				return this.HisMsgObjs;
			}
		}

		// 如果是协作组长模式节点, 就判断当前的队列人员是否走完.
		if (this.getTodolistModel() == TodolistModel.TeamupGroupLeader) {
			// 如果是协作组长模式.
			// if (this.DealTeamUpNode() == true)
			// {
			// //if (this._transferCustom != null)
			// // _transferCustom.Delete();
			//
			// //执行时效考核.
			// Glo.InitCH(this.getHisFlow(), this.getHisNode(),
			// this.getWorkID(), this.rptGe.getFID(),
			// this.rptGe.getTitle(),null);
			// return this.HisMsgObjs;
			// }

			/* 如果是协作组长模式. */
			if (this.DealTeamupGroupLeader() == true) {
				// 执行时效考核.
				// Glo.InitCH(this.HisFlow, this.HisNode, this.WorkID,
				// this.rptGe.FID, this.rptGe.Title);
				return this.HisMsgObjs;
			}
		}

		// 启动事务, 这里没有实现.
		DBAccess.DoTransactionBegin();
		try {
			if (this.getHisNode().getIsStartNode()) {
				InitStartWorkDataV2(); // 初始化开始节点数据, 如果当前节点是开始节点.
			}

			// 处理发送人，把发送人的信息放入wf_generworkflow 2015-01-14.
			// 原来放入WF_GenerWorkerList.
			oldSender = this.getHisGenerWorkFlow().getSender(); // 旧发送人,在回滚的时候把该发送人赋值给他.
			this.getHisGenerWorkFlow().setSender(BP.WF.Glo.DealUserInfoShowModel(WebUser.getNo(), WebUser.getName()));

			/// #region 处理退回的情况.
			if (this.getHisGenerWorkFlow().getWFState() == WFState.ReturnSta) {
				// 检查该退回是否是原路返回 ?
				Paras ps = new Paras();
				ps.SQL = "SELECT ReturnNode,Returner,IsBackTracking FROM WF_ReturnWork WHERE WorkID=" + dbStr
						+ "WorkID AND IsBackTracking=1 ORDER BY RDT DESC";
				ps.Add(ReturnWorkAttr.WorkID, this.getWorkID());
				DataTable mydt = DBAccess.RunSQLReturnTable(ps);
				if (mydt.Rows.size() != 0) {
					// 有可能查询出来多个，因为按时间排序了，只取出最后一次退回的，看看是否有退回并原路返回的信息。

					// 确认这次退回，是退回并原路返回 , 在这里初始化它的工作人员, 与将要发送的节点.
					this.JumpToNode = new Node(Integer.parseInt(mydt.Rows.get(0).getValue("ReturnNode").toString()));
					this.JumpToEmp = mydt.Rows.get(0).getValue("Returner").toString();

					if (this.getHisGenerWorkFlow().getTransferCustomType() == TransferCustomType.ByCCBPMDefine) {
						// 如果当前是退回, 并且当前的运行模式是按照流程图运行.
						if (this.JumpToNode.getTodolistModel() == TodolistModel.Order
								|| this.JumpToNode.getTodolistModel() == TodolistModel.TeamupGroupLeader
								|| this.JumpToNode.getTodolistModel() == TodolistModel.Teamup) {
							// 如果是多人处理节点.
							this.DealReturnOrderTeamup();

							// 执行时效考核.
							Glo.InitCH(this.getHisFlow(), this.getHisNode(), this.getWorkID(), this.rptGe.getFID(),
									this.rptGe.getTitle(), null);
							return this.HisMsgObjs;
						}
					}

					if (this.getHisGenerWorkFlow().getTransferCustomType() == TransferCustomType.ByWorkerSet) {
						// 如果当前是退回. 并且当前的运行模式按照自由流程设置方式运行。
						if (this.getHisGenerWorkFlow().getTodolistModel() == TodolistModel.Order
								|| this.JumpToNode.getTodolistModel() == TodolistModel.TeamupGroupLeader
								|| this.getHisGenerWorkFlow().getTodolistModel() == TodolistModel.Teamup) {
							// 如果是多人处理节点.
							this.DealReturnOrderTeamup();

							// 执行时效考核.
							Glo.InitCH(this.getHisFlow(), this.getHisNode(), this.getWorkID(), this.rptGe.getFID(),
									this.rptGe.getTitle(), null);
							return this.HisMsgObjs;
						}
					}
				}
			}

			/// #endregion 处理退回的情况.

			// 做了不可能性的判断.
			if (this.getHisGenerWorkFlow().getFK_Node() != this.getHisNode().getNodeID()) {
				throw new RuntimeException("@流程出现的错误,工作ID=" + this.getWorkID() + ",当前活动点("
						+ this.getHisGenerWorkFlow().getFK_Node() + "" + this.getHisGenerWorkFlow().getNodeName()
						+ ")与发送点(" + this.getHisNode().getNodeID() + this.getHisNode().getName() + ")不一致");
			}

			// 检查完成条件。
			if (jumpToNode != null && this.getHisNode().getIsEndNode()) {
				// 是跳转的情况，并且是最后的节点，就不检查流程完成条件。
			} else {
				// 检查流程完成条件.
				this.CheckCompleteCondition();
			}

			// 处理自由流程. add by stone. 2014-11-23.
			if (jumpToNode == null
					&& this.getHisGenerWorkFlow().getTransferCustomType() == TransferCustomType.ByWorkerSet) {
				// 如果没有指定要跳转到的节点，并且当前处理手工干预的运行状态.
				_transferCustom = TransferCustom.GetNextTransferCustom(this.getWorkID(), this.getHisNode().getNodeID());
				if (_transferCustom == null) {
					// 表示执行到这里结束流程.
					this.setIsStopFlow(true);

					this.getHisGenerWorkFlow().setWFState(WFState.Complete);
					this.rptGe.setWFState(WFState.Complete);
					String msg1 = this.getHisWorkFlow().DoFlowOver(ActionType.FlowOver, "流程已经按照设置的步骤成功结束。",
							this.getHisNode(), this.rptGe);
					this.addMsg(SendReturnMsgFlag.End, msg1);
				} else {
					this.JumpToNode = new Node(_transferCustom.getFK_Node());
					this.JumpToEmp = _transferCustom.getWorker();
					this.getHisGenerWorkFlow().setTodolistModel(_transferCustom.getTodolistModel());
				}
			}

			// 处理质量考核，在发送前。
			this.DealEval();

			// 加入系统变量.
			if (this.getIsStopFlow()) {
				this.addMsg(SendReturnMsgFlag.IsStopFlow, "1", "流程已经结束", SendReturnMsgType.Info);
			} else {
				this.addMsg(SendReturnMsgFlag.IsStopFlow, "0", "流程未结束", SendReturnMsgType.SystemMsg);
			}

			//String mymsgHtml = "@查看<img src='" + getVirPath() + "WF/Img/Btn/PrintWorkRpt.gif' ><a href='" + getVirPath()
			//		+ "WF/WFRpt.jsp?WorkID=" + this.getHisWork().getOID() + "&FID=" + this.getHisWork().getFID()
			//		+ "&NodeID=" + this.getHisNode().getNodeID()
			//		+ "&FK_Flow=" + this.getHisNode().getFK_Flow() + "' target='_self' >工作轨迹</a>";
			//this.addMsg(SendReturnMsgFlag.MsgOfText, mymsgHtml);

			if (this.getIsStopFlow() == true) {
				// 在检查完后，反馈来的标志流程已经停止了。
				// 这里应该去掉，不然事件就不起作用. @于庆海翻译.
				// this.Func_DoSetThisWorkOver();
				// this.rptGe.setWFState(WFState.Complete);
				// this.rptGe.Update();

				// this.getHisGenerWorkFlow().Update(); //added by
				// liuxc,2016-10=24,最后节点更新Sender字段
				this.Func_DoSetThisWorkOver();
				this.rptGe.setWFState(WFState.Complete);
				this.rptGe.Update();
				this.getHisGenerWorkFlow().Update(); // added by
														// liuxc,2016-10=24,最后节点更新Sender字段
				/**
				 * 执行考核
				 */
				Glo.InitCH(this.getHisFlow(), this.getHisNode(), this.getWorkID(), 0,
						this.getHisGenerWorkFlow().getTitle(), null);
				
			    // 执行发送.
                String sendSuccess = this.getHisFlow().DoFlowEventEntity(EventListOfNode.SendSuccess, this.getHisNode(),
                        this.rptGe, null, this.HisMsgObjs);

				return HisMsgObjs;
			}

			// @增加发送到子流程的判断.
			if (jumpToNode != null && this.getHisNode().getFK_Flow().equals(jumpToNode.getFK_Flow()) == false) {
				/* 判断是否是延续子流程. */
				return NodeSendToYGFlow(jumpToNode, jumpToEmp);
			}
			
			//设置会签模式为空.
			this.getHisGenerWorkFlow().setHuiQianTaskSta( HuiQianTaskSta.None);

			/// #region 第二步: 进入核心的流程运转计算区域. 5*5 的方式处理不同的发送情况.
			// 执行节点向下发送的25种情况的判断.
			this.NodeSend_Send_5_5();

			// 通过 55 之后要判断是否要结束流程，如果结束流程就执行相关的更新。
			if (this.getIsStopFlow()) {
				this.rptGe.setWFState(WFState.Complete);
				this.Func_DoSetThisWorkOver();

				this.getHisGenerWorkFlow().Update(); // added by
														// liuxc,2016-10=24,最后节点更新Sender字段
			} else {
				// 如果是退回状态，就把是否原路返回的轨迹去掉.
				if (this.getHisGenerWorkFlow().getWFState() == WFState.ReturnSta) {
					BP.DA.DBAccess.RunSQL("UPDATE WF_ReturnWork SET IsBackTracking=0 WHERE WorkID=" + this.getWorkID());
				}

				this.Func_DoSetThisWorkOver();
				if (town != null && town.getHisNode().getHisBatchRole() == BatchRole.Group) {
					this.getHisGenerWorkFlow().setWFState(WFState.Batch);
					this.getHisGenerWorkFlow().Update();
				}
			}

			// 计算从发送到现在的天数.
			// this.rptGe.setFlowDaySpan(DataType.GeTimeLimits(this.getHisGenerWorkFlow().getRDT()));
			this.rptGe.Update();

			/// #region 第三步: 处理发送之后的业务逻辑.
			// 把当前节点表单数据copy的流程数据表里.
			this.DoCopyCurrentWorkDataToRpt();

			// 处理合理节点的1变N的问题.
			this.CheckFrm1ToN();

			// 处理子线程的独立表单向合流节点的独立表单明细表的数据汇总.
			this.CheckFrmHuiZongToDtl();

			/// #endregion 第三步: 处理发送之后的业务逻辑.

			/// #region 处理子流程
			if (this.getHisNode().getIsStartNode() && this.getHisNode().getSubFlowStartWay() != SubFlowStartWay.None) {
				CallSubFlow();
			}

			/// #endregion 处理子流程

			/// #region 生成单据
			if (this.getHisNode().getHisPrintDocEnable() == PrintDocEnable.PrintRTF
					&& this.getHisNode().getBillTemplates().size() > 0) {

				BillTemplates reffunc = this.getHisNode().getBillTemplates();

				/// #region 生成单据信息
				long workid = this.getHisWork().getOID();
				int nodeId = this.getHisNode().getNodeID();
				String flowNo = this.getHisNode().getFK_Flow();

				/// #endregion

				java.util.Date dtNow = new java.util.Date();
				Flow fl = this.getHisNode().getHisFlow();
				String year = String.valueOf(dt.getYear());
				String billInfo = "";
				for (BillTemplate func : reffunc.ToJavaList()) {
					if (func.getHisBillFileType() != BillFileType.RuiLang) {
						String file = year + "_" + this.getExecerDeptNo() + "_" + func.getNo() + "_" + workid + ".doc";
						BP.Pub.RTFEngine rtf = new BP.Pub.RTFEngine();

						Works works;
						String[] paths;
						String path;
						try {

							/// #region 把数据放入 单据引擎。
							rtf.getHisEns().clear(); // 主表数据。
							rtf.getEnsDataDtls().clear(); // 明细表数据.

							// 找到主表数据.
							rtf.HisGEEntity = new GEEntity(this.rptGe.getClassID());
							rtf.HisGEEntity.setRow(rptGe.getRow());

							// 把每个节点上的工作加入到报表引擎。
							rtf.AddEn(this.getHisWork());
							rtf.ensStrs += ".ND" + this.getHisNode().getNodeID();

							// 把当前work的Dtl 数据放进去了。
							java.util.ArrayList<Entities> al = this.getHisWork().GetDtlsDatasOfList();
							for (Entities ens : al) {
								rtf.AddDtlEns(ens);
							}

							/// #endregion 把数据放入 单据引擎。

							/// #region 生成单据

							paths = file.split("[_]", -1);
							path = paths[0] + "/" + paths[1] + "/" + paths[2] + "/";
							String billUrl = getVirPath() + "DataUser/Bill/" + path + file;
							if (func.getHisBillFileType() == BillFileType.PDF) {
								billUrl = billUrl.replace(".doc", ".pdf");
								billInfo += "<img src='" + getVirPath() + "WF/Img/FileType/PDF.gif' /><a href='"
										+ billUrl + "' target=_blank >" + func.getName() + "</a>";
							} else {
								billInfo += "<img src='" + getVirPath() + "WF/Img/FileType/doc.gif' /><a href='"
										+ billUrl + "' target=_blank >" + func.getName() + "</a>";
							}

							path = BP.WF.Glo.getFlowFileBill() + year + "\\" + this.getExecerDeptNo() + "\\"
									+ func.getNo() + "\\";
							// path = AppDomain.CurrentDomain.BaseDirectory +
							// path;
							if ((new java.io.File(path)).isDirectory() == false) {
								(new java.io.File(path)).mkdirs();
							}

							rtf.MakeDoc(func.getTempFilePath() + ".rtf", path, file, func.getReplaceVal(), false);
							/// #region 转化成pdf.
							if (func.getHisBillFileType() == BillFileType.PDF) {
								String rtfPath = path + file;
								String pdfPath = rtfPath.replace(".doc", ".pdf");
								try {
									Glo.Rtf2PDF(rtfPath, pdfPath);
								} catch (RuntimeException ex) {
									this.addMsg("RptError", "产生报表数据错误:" + ex.getMessage());
								}
							}
							/// #region 保存单据
							Bill bill = new Bill();
							bill.setMyPK(this.getHisWork().getFID() + "_" + this.getHisWork().getOID() + "_"
									+ this.getHisNode().getNodeID() + "_" + func.getNo());
							bill.setFID(this.getHisWork().getFID());
							bill.setWorkID(this.getHisWork().getOID());
							bill.setFK_Node(this.getHisNode().getNodeID());
							bill.setFK_Dept(this.getExecerDeptNo());
							bill.setFK_Emp(this.getExecer());
							bill.setUrl(billUrl);
							bill.setRDT(DataType.getCurrentDataTime());
							bill.setFullPath(path + file);
							bill.setFK_NY(DataType.getCurrentYearMonth());
							bill.setFK_Flow(this.getHisNode().getFK_Flow());
							bill.setFK_BillType(func.getFK_BillType());
							bill.setFK_Flow(this.getHisNode().getFK_Flow());
							bill.setEmps(this.rptGe.GetValStrByKey("Emps"));
							bill.setFK_Starter(this.rptGe.GetValStrByKey("Rec"));
							bill.setStartDT(this.rptGe.GetValStrByKey("RDT"));
							bill.setTitle(this.rptGe.GetValStrByKey("Title"));
							bill.setFK_Dept(this.rptGe.GetValStrByKey("FK_Dept"));
							try {
								bill.Save();
							} catch (java.lang.Exception e) {
								bill.Update();
							}

							/// #endregion
						} catch (RuntimeException ex) {
							BP.WF.DTS.InitBillDir dir = new BP.WF.DTS.InitBillDir();
							dir.Do();
							path = BP.WF.Glo.getFlowFileBill() + year + "\\" + this.getExecerDeptNo() + "\\"
									+ func.getNo() + "\\";
							String msgErr = "@" + String.format("生成单据失败，请让管理员检查目录设置") + "["
									+ BP.WF.Glo.getFlowFileBill() + "]。@Err：" + ex.getMessage() + " @File=" + file
									+ " @Path:" + path;
							billInfo += "@<font color=red>" + msgErr
									+ "</font>@<hr>@系统已经做了可能性的修复，请您在发送一次，如果问题仍然存在请联系管理员。";
							throw new RuntimeException(msgErr + "@其它信息:" + ex.getMessage());
						}
					}

				} // end 生成循环单据。

				if (!billInfo.equals("")) {
					billInfo = "@" + billInfo;
				}
				this.addMsg(SendReturnMsgFlag.BillInfo, billInfo);
			}
			// 执行抄送.
			if (this.getHisNode().getIsEndNode() == false) {
				// 执行自动抄送.
				String ccMsg1 = WorkFlowBuessRole.DoCCAuto(this.getHisNode(), this.rptGe, this.getWorkID(),
						this.getHisWork().getFID());
				// 按照指定的字段抄送.
				String ccMsg2 = WorkFlowBuessRole.DoCCByEmps(this.getHisNode(), this.rptGe, this.getWorkID(),
						this.getHisWork().getFID());
				String ccMsg = ccMsg1 + ccMsg2;

				if (StringHelper.isNullOrEmpty(ccMsg) == false) {
					this.addMsg("CC", "@自动抄送给:" + ccMsg);
					this.AddToTrack(ActionType.CC, WebUser.getNo(), WebUser.getName(), this.getHisNode().getNodeID(),
							this.getHisNode().getName(), ccMsg1 + ccMsg2, this.getHisNode());
				}
			}

			DBAccess.DoTransactionCommit(); // 提交事务.
			/// #endregion 处理主要业务逻辑.

			/// #region 执行启动子流程.
			String msg = "";
			if (this.getHisNode().getSFActiveFlows().length() > 2) {
				String[] fls = this.getHisNode().getSFActiveFlows().split("[,]", -1);
				for (String flowNo : fls) {
					if (DotNetToJavaStringHelper.isNullOrEmpty(flowNo) == true) {
						continue;
					}

					// 检查是否有发起限制子流程的条件.
					Flow fl = new Flow(flowNo);

					/// #region 检查流程启动条件.
					boolean isPass = false;
					// 检查是否有
					Conds cds = new Conds();
					cds.Retrieve(CondAttr.FK_Node, this.getHisNode().getNodeID(), CondAttr.NodeID,
							Integer.parseInt(flowNo));
					for (Cond cd : cds.ToJavaList()) {
						cd.setWorkID(this.getHisWork().getOID());
						cd.setNodeID(this.getHisNode().getNodeID());
					}

					if (cds.size() == 0) {
						isPass = true;
					} else {
						isPass = cds.getIsPass();
					}

					if (isPass == false) {
						msg += "@不符合子流程[" + fl.getName() + "]启动条件。";
						continue;
					}

					/// #endregion 检查流程启动条件.

					// 启动子流程.
					SendReturnObjs sendObjs = BP.WF.Dev2Interface.Node_StartWork(fl.getNo(), this.getHisWork().getRow(),
							null, 0, null, this.getWorkID(), this.getHisFlow().getNo());
					msg += "@子流程[" + fl.getName() + "]已经启动，已经发送给：" + sendObjs.getVarAcceptersName() + "，发送到："
							+ sendObjs.getVarToNodeName() + "。";
				}
				if (!msg.equals("")) {
					this.addMsg(SendReturnMsgFlag.MsgOfText, msg);
				}

			}

			/// #endregion 执行启动子流程.

			/// #region 处理流程数据与业务表的数据同步.
			if (this.getHisFlow().getDTSWay() != FlowDTSWay.None) {
				this.getHisFlow().DoBTableDTS(this.rptGe, this.getHisNode(), this.getIsStopFlow());
			}

			/// #endregion 处理流程数据与业务表的数据同步.

			/// #region 处理发送成功后的消息提示
			if (this.getHisNode().getHisTurnToDeal() == TurnToDeal.SpecMsg) {
				String htmlInfo = "";
				String textInfo = "";

				/// #region 判断当前处理人员，可否处理下一步工作.
				if (this.town != null && this.HisRememberMe != null
						&& this.HisRememberMe.getEmps().contains("@" + WebUser.getNo() + "@") == true) {
					String url = Glo.getHostURL() + "/WF/MyFlow.htm?FK_Flow=" + this.getHisFlow().getNo() + "&WorkID="
							+ this.getWorkID() + "&FK_Node=" + town.getHisNode().getNodeID() + "&FID="
							+ this.rptGe.getFID();
					// htmlInfo = "@<a href='" + url + "'
					// >下一步工作您仍然可以处理，点击这里现在处理。</a>.";
					textInfo = "@下一步工作您仍然可以处理。";
					this.addMsg(SendReturnMsgFlag.MsgOfText, textInfo);
				}

				/// #endregion 判断当前处理人员，可否处理下一步工作.

				String msgOfSend = this.getHisNode().getTurnToDealDoc();
				if (msgOfSend.contains("@")) {
					Attrs attrs = this.getHisWork().getEnMap().getAttrs();
					for (Attr attr : attrs) {
						if (msgOfSend.contains("@") == false) {
							continue;
						}
						msgOfSend = msgOfSend.replace("@" + attr.getKey(),
								this.getHisWork().GetValStrByKey(attr.getKey()));
					}
				}

				if (msgOfSend.contains("@") == true) {
					// 说明有一些变量在系统运行里面.
					Object tempVar = msgOfSend;
					String msgOfSendText = (String) ((tempVar instanceof String) ? tempVar : null);
					for (SendReturnObj item : this.HisMsgObjs) {
						if (DotNetToJavaStringHelper.isNullOrEmpty(item.MsgFlag)) {
							continue;
						}

						if (msgOfSend.contains("@") == false) {
							continue;
						}

						msgOfSendText = msgOfSendText.replace("@" + item.MsgFlag, item.MsgOfText);

						if (item.MsgOfHtml != null) {
							msgOfSend = msgOfSend.replace("@" + item.MsgFlag, item.MsgOfHtml);
						} else {
							msgOfSend = msgOfSend.replace("@" + item.MsgFlag, item.MsgOfText);
						}
					}

					this.HisMsgObjs.OutMessageHtml = msgOfSend + htmlInfo;
					this.HisMsgObjs.OutMessageText = msgOfSendText + textInfo;
				} else {
					this.HisMsgObjs.OutMessageHtml = msgOfSend;
					this.HisMsgObjs.OutMessageText = msgOfSend;
				}

				// return msgOfSend;
			}

			/// #endregion 处理发送成功后事件.

			/// #region 如果需要跳转.
			if (town != null) {
				if (this.town.getHisNode().getHisRunModel() == RunModel.SubThread
						&& this.town.getHisNode().getHisRunModel() == RunModel.SubThread) {
					this.addMsg(SendReturnMsgFlag.VarToNodeID, (new Integer(town.getHisNode().getNodeID())).toString(),
							(new Integer(town.getHisNode().getNodeID())).toString(), SendReturnMsgType.SystemMsg);
					this.addMsg(SendReturnMsgFlag.VarToNodeName, town.getHisNode().getName(),
							town.getHisNode().getName(), SendReturnMsgType.SystemMsg);
				}
				// if (town.HisNode.HisDeliveryWay ==
				// DeliveryWay.ByPreviousOperSkip)
				// {
				// town.NodeSend();
				// this.HisMsgObjs = town.HisMsgObjs;
				// }
			}
			/// #region 删除已经发送的消息，那些消息已经成为了垃圾信息.
			if (Glo.getIsEnableSysMessage() == true) {
				Paras ps = new Paras();
				ps.SQL = "DELETE FROM Sys_SMS WHERE MsgFlag=" + SystemConfig.getAppCenterDBVarStr() + "MsgFlag";
				ps.Add("MsgFlag", "WKAlt" + this.getHisNode().getNodeID() + "_" + this.getWorkID());
				BP.DA.DBAccess.RunSQL(ps);
			}

			if (this.getHisNode().getIsStartNode()) {
				if (this.rptGe.getPWorkID() != 0 && this.getHisGenerWorkFlow().getPWorkID() == 0) {
					BP.WF.Dev2Interface.SetParentInfo(this.getHisFlow().getNo(), this.getWorkID(),
							this.rptGe.getPFlowNo(), this.rptGe.getPWorkID(), this.rptGe.getPNodeID(),
							this.rptGe.getPEmp());

					// 写入track, 调用了父流程.
					Node pND = new Node(rptGe.getPNodeID());
					long fid = 0;
					if (pND.getHisNodeWorkType() == NodeWorkType.SubThreadWork) {
						GenerWorkFlow gwf = new GenerWorkFlow(this.rptGe.getPWorkID());
						fid = gwf.getFID();
					}

					String paras = "@CFlowNo=" + this.getHisFlow().getNo() + "@CWorkID=" + this.getWorkID();

					Glo.AddToTrack(ActionType.StartChildenFlow, rptGe.getPFlowNo(), rptGe.getPWorkID(), fid,
							pND.getNodeID(), pND.getName(), WebUser.getNo(), WebUser.getName(), pND.getNodeID(),
							pND.getName(), WebUser.getNo(), WebUser.getName(),
							"<a href='/WF/WFRpt.jsp?FK_Flow=" + this.getHisFlow().getNo() + "&WorkID="
									+ this.getWorkID() + "' target=_blank >打开子流程</a>",
							paras);
				} else if (SystemConfig.getIsBSsystem() == true) {
					// 如果是BS系统
					String pflowNo = BP.Sys.Glo.getRequest().getParameter("PFlowNo");
					if (StringHelper.isNullOrEmpty(pflowNo) == false) {
						String pWorkID = BP.Sys.Glo.getRequest().getParameter("PWorkID");
						String pNodeID = BP.Sys.Glo.getRequest().getParameter("PNodeID");
						String pEmp = BP.Sys.Glo.getRequest().getParameter("PEmp");

						// 设置成父流程关系.
						BP.WF.Dev2Interface.SetParentInfo(this.getHisFlow().getNo(), this.getWorkID(), pflowNo,
								Long.parseLong(pWorkID), Integer.parseInt(pNodeID), pEmp);

						// 写入track, 调用了父流程.
						Node pND = new Node(pNodeID);
						long fid = 0;
						if (pND.getHisNodeWorkType() == NodeWorkType.SubThreadWork) {
							GenerWorkFlow gwf = new GenerWorkFlow(Long.parseLong(pWorkID));
							fid = gwf.getFID();
						}
						String paras = "@CFlowNo=" + this.getHisFlow().getNo() + "@CWorkID=" + this.getWorkID();
						Glo.AddToTrack(ActionType.StartChildenFlow, pflowNo, Long.parseLong(pWorkID),
								Long.parseLong((new Long(fid)).toString()), pND.getNodeID(), pND.getName(),
								WebUser.getNo(), WebUser.getName(), pND.getNodeID(), pND.getName(), WebUser.getNo(),
								WebUser.getName(), "<a href='/WF/WFRpt.jsp?FK_Flow=" + this.getHisFlow().getNo()
										+ "&WorkID=" + this.getWorkID() + "' target=_blank >打开子流程</a>",
								paras);
					}
				}
			}
			// 执行时效考核.
			Glo.InitCH(this.getHisFlow(), this.getHisNode(), this.getWorkID(), this.rptGe.getFID(),
					this.rptGe.getTitle(), null);

			/// #region 触发下一个节点的自动发送, 处理国机的需求.
			if (this.HisMsgObjs.getVarToNodeID() != WFState.Blank.getValue() && this.town != null
					&& this.town.getHisNode().getWhoExeIt() != 0) {
				String currUser = BP.Web.WebUser.getNo();
				String[] emps = this.HisMsgObjs.getVarAcceptersID().split("[,]", -1);
				for (String emp : emps) {
					if (DotNetToJavaStringHelper.isNullOrEmpty(emp)) {
						continue;
					}

					try {
						// 让这个人登录.
						BP.Port.Emp empEn = new Emp(emp);
						BP.WF.Dev2Interface.Port_Login(emp);
						if (this.getHisNode().getHisRunModel() == RunModel.SubThread
								&& this.town.getHisNode().getHisRunModel() != RunModel.SubThread) {
							// 如果当前的节点是子线程，并且发送到的节点非子线程。
							// * 就是子线程发送到非子线程的情况。
							//
							this.HisMsgObjs = BP.WF.Dev2Interface.Node_SendWork(this.getHisNode().getFK_Flow(),
									this.getHisWork().getFID());
						} else {
							this.HisMsgObjs = BP.WF.Dev2Interface.Node_SendWork(this.getHisNode().getFK_Flow(),
									this.getHisWork().getOID());
						}
					} catch (java.lang.Exception e2) {
						// 可能是正常的阻挡发送，操作不必提示。
						// this.HisMsgObjs.AddMsg("Auto"
					}
					BP.WF.Dev2Interface.Port_Login(currUser);
					// 使用一个人处理就可以了.
					break;
				}
			}
			/// #region 计算未来处理人.
			if (this.town == null) {
				FullSA fsa1 = new FullSA(this);
			} else {
				FullSA fsa = new FullSA(this.town);
			}
			/// #region 判断当前处理人员，可否处理下一步工作.
			if (this.town != null && this.HisRememberMe != null
					&& this.HisRememberMe.getEmps().contains("@" + WebUser.getNo() + "@") == true) {
				String url = Glo.getHostURL() + "/WF/MyFlow.htm?FK_Flow=" + this.getHisFlow().getNo() + "&WorkID="
						+ this.getWorkID() + "&FK_Node=" + town.getHisNode().getNodeID() + "&FID="
						+ this.rptGe.getFID();
				// String htmlInfo = "@<a href='" + url + "'
				// >下一步工作您仍然可以处理，点击这里现在处理。</a>.";
				String textInfo = "@下一步工作您仍然可以处理。";

				this.addMsg(SendReturnMsgFlag.MsgOfText, textInfo);
			}
			
			try {
				// 调起发送成功后的事件，把参数传入进去。
				if (this.SendHTOfTemp != null) {
					for (Object key : this.SendHTOfTemp.keySet()) {
						if (rptGe.getRow().containsKey(key) == true) {
							this.rptGe.getRow().put(key.toString(), this.SendHTOfTemp.get(key).toString());
						} else {
							this.rptGe.getRow().put(key.toString(), this.SendHTOfTemp.get(key).toString());
						}
					}
				}
 
			} catch (RuntimeException ex) {
				this.addMsg(SendReturnMsgFlag.SendSuccessMsgErr, ex.getMessage());
			}

			/// #endregion 处理发送成功后事件.
			
			//处理事件.
            this.Deal_Event();
            
			// 返回这个对象.
			return this.HisMsgObjs;
		} catch (RuntimeException ex) {
			this.WhenTranscactionRollbackError(ex);
			DBAccess.DoTransactionRollback();
			throw new RuntimeException("Message:" + ex.getMessage());
		}
	}
	
    /// <summary>
    /// 处理事件
    /// </summary>
    private void Deal_Event() throws Exception
    {
        ///#region 处理节点到达事件..
        //执行发送到达事件.
        if (this.town != null)
        {
        	//this.town
            String toMsg = this.getHisFlow().DoFlowEventEntity(EventListOfNode.WorkArrive,
                this.town.getHisNode(), this.rptGe, null, this.HisMsgObjs);
            
            if (toMsg!=null)
            	this.addMsg("WorkArrive", toMsg);
            	
        }
        ///#endregion 处理节点到达事件.

        ///#region 处理发送成功后事件.
        try
        {
            //调起发送成功后的事件，把参数传入进去。
            if (this.SendHTOfTemp != null)
            {
            	Enumeration<String> e = this.SendHTOfTemp.keys();
            	while(e.hasMoreElements()){
            		String key = e.nextElement();
                    if (rptGe.getRow().containsKey(key) == true)
                        this.rptGe.SetValByKey(key,this.SendHTOfTemp.get(key).toString());
                    else
                        this.rptGe.getRow().put(key, this.SendHTOfTemp.get(key).toString());
                }
            }

            //执行发送.
            String sendSuccess = this.getHisFlow().DoFlowEventEntity(EventListOfNode.SendSuccess,
                this.getHisNode(), this.rptGe, null, this.HisMsgObjs);

            //string SendSuccess = this.HisNode.MapData.FrmEvents.DoEventNode(EventListOfNode.SendSuccess, this.HisWork);
            if (sendSuccess != null)
                this.addMsg(SendReturnMsgFlag.SendSuccessMsg, sendSuccess);
        }
        catch (Exception ex)
        {
            this.addMsg(SendReturnMsgFlag.SendSuccessMsgErr, ex.getMessage());
        }
        ///#endregion 处理发送成功后事件.
    }
    
	/**
	 * 执行业务表数据同步.
	 */
	private void DTSBTable() {

	}

	/**
	 * 手工的回滚提交失败信息，补偿没有事务的缺陷。
	 * 
	 * @param ex
	 * @throws Exception 
	 */
	private void WhenTranscactionRollbackError(RuntimeException ex) throws Exception {
		// 在提交错误的情况下，回滚数据。
		/// #region 如果是分流点下同表单发送失败再次发送就出现错误
		if (this.town != null && this.town.getHisNode().getHisNodeWorkType() == NodeWorkType.SubThreadWork
				&& this.town.getHisNode().getHisSubThreadType() == SubThreadType.SameSheet) {
			// 如果是子线程
			DBAccess.RunSQL("DELETE FROM WF_GenerWorkerList WHERE FID=" + this.getWorkID() + " AND FK_Node="
					+ this.town.getHisNode().getNodeID());
			// 删除子线程数据.
            if (BP.DA.DBAccess.IsExitsObject(this.town.getHisWork().getEnMap().getPhysicsTable()) == true)
      			DBAccess.RunSQL("DELETE FROM " + this.town.getHisWork().getEnMap().getPhysicsTable() + " WHERE FID="
					+ this.getWorkID());
		}

		/// #endregion 如果是分流点下同表单发送失败再次发送就出现错误
		try {
			
			// 删除发生的日志.
			DBAccess.RunSQL("DELETE FROM ND" + Integer.parseInt(this.getHisFlow().getNo()) + "Track WHERE WorkID="
					+ this.getWorkID() + " AND NDFrom=" + this.getHisNode().getNodeID() + " AND ActionType="
					+ ActionType.Forward.getValue());

			// 删除考核信息。
			this.DealEvalUn();

			// 把工作的状态设置回来。
			if (this.getHisNode().getIsStartNode()) {
				ps = new Paras();
				ps.SQL = "UPDATE " + this.getHisFlow().getPTable() + " SET WFState=" + WFState.Runing.getValue()
						+ " WHERE OID=" + dbStr + "OID ";
				ps.Add(GERptAttr.OID, this.getWorkID());
				DBAccess.RunSQL(ps);
				// this.HisWork.Update(GERptAttr.WFState, (int)WFState.Runing);
			}

			// 把流程的状态设置回来。
			GenerWorkFlow gwf = new GenerWorkFlow();
			gwf.setWorkID(this.getWorkID());
			if (gwf.RetrieveFromDBSources() == 0) {
				return;
			}

			if (gwf.getWFState().ordinal() != 0 || gwf.getFK_Node() != this.getHisNode().getNodeID()) {
				// 如果这两项其中有一项有变化。
				gwf.setFK_Node(this.getHisNode().getNodeID());
				gwf.setNodeName(this.getHisNode().getName());
				gwf.setWFState(WFState.Runing);
				this.getHisGenerWorkFlow().setSender(BP.WF.Glo.DealUserInfoShowModel(oldSender, oldSender));
				gwf.Update();
			}

			// 执行数据.
			ps = new Paras();
			ps.SQL = "UPDATE WF_GenerWorkerlist SET IsPass=0 WHERE FK_Emp=" + dbStr + "FK_Emp AND WorkID=" + dbStr
					+ "WorkID AND FK_Node=" + dbStr + "FK_Node ";
			ps.AddFK_Emp();
			ps.Add("WorkID", this.getWorkID());
			ps.Add("FK_Node", this.getHisNode().getNodeID());
			DBAccess.RunSQL(ps);

			Node startND = this.getHisNode().getHisFlow().getHisStartNode();
			Work tempVar = startND.getHisWork();
			StartWork wk = (StartWork) ((tempVar instanceof StartWork) ? tempVar : null);
			switch (startND.getHisNodeWorkType()) {
			case StartWorkFL:
			case WorkFL:
				break;
			default:
				//
				// 要考虑删除WFState 节点字段的问题。
				//
				//// 把开始节点的装态设置回来。
				// DBAccess.RunSQL("UPDATE " + wk.getEnMap().getPhysicsTable() +
				// " SET WFState=0 WHERE OID="+this.WorkID+" OR OID="+this);
				// wk.OID = this.WorkID;
				// int i =wk.RetrieveFromDBSources();
				// if (wk.WFState == WFState.Complete)
				// {
				// wk.Update("WFState", (int)WFState.Runing);
				// }
				break;
			}
			Nodes nds = this.getHisNode().getHisToNodes();
			for (Node nd : nds.ToJavaList()) {
				if (nd.getNodeID() == this.getHisNode().getNodeID()) {
					continue;
				}

				Work mwk = nd.getHisWork();
				if (this.getHisFlow().getPTable().equals(mwk.getEnMap().getPhysicsTable())
						|| mwk.getEnMap().getPhysicsTable() == this.getHisWork().getEnMap().getPhysicsTable()) {
					continue;
				}

				mwk.setOID(this.getWorkID());
				try {
					mwk.DirectDelete();
				} catch (java.lang.Exception e) {
					mwk.CheckPhysicsTable();
					mwk.DirectDelete();
				}
			}
			this.getHisFlow().DoFlowEventEntity(EventListOfNode.SendError, this.getHisNode(), this.rptGe, null);

		} catch (RuntimeException ex1) {
			if (this.town != null && this.town.getHisWork() != null) {
				this.town.getHisWork().CheckPhysicsTable();
			}

			if (this.rptGe != null) {
				this.rptGe.CheckPhysicsTable();
			}
			throw new RuntimeException(
					ex.getMessage() + "@回滚发送失败数据出现错误：" + ex1.getMessage() + "@有可能系统已经自动修复错误，请您在重新执行一次。");
		}
	}

	/// #region 用户到的变量
	public GenerWorkerLists HisWorkerLists = null;
	private GenerWorkFlow _HisGenerWorkFlow;

	public final GenerWorkFlow getHisGenerWorkFlow() throws Exception {
		if (_HisGenerWorkFlow == null) {
			_HisGenerWorkFlow = new GenerWorkFlow(this.getWorkID());
			SendNodeWFState = _HisGenerWorkFlow.getWFState(); // 设置发送前的节点状态。
		}
		return _HisGenerWorkFlow;
	}

	public final void setHisGenerWorkFlow(GenerWorkFlow value) {
		_HisGenerWorkFlow = value;
	}

	private long _WorkID = 0;

	/**
	 * 工作ID.
	 */
	public final long getWorkID() {
		return _WorkID;
	}

	public final void setWorkID(long value) {
		_WorkID = value;
	}

	/**
	 * 原来的发送人.
	 */
	private String oldSender = null;

	public GERpt rptGe = null;

	private void InitStartWorkDataV2() throws Exception {
		// 如果是开始流程判断是不是被吊起的流程，如果是就要向父流程写日志。
		if (SystemConfig.getIsBSsystem()) {
			String fk_nodeFrom = BP.Sys.Glo.getRequest().getParameter("FromNode");
			if (DotNetToJavaStringHelper.isNullOrEmpty(fk_nodeFrom) == false) {
				Node ndFrom = new Node(Integer.parseInt(fk_nodeFrom));
				String PWorkID = BP.Sys.Glo.getRequest().getParameter("PWorkID");
				if (DotNetToJavaStringHelper.isNullOrEmpty(PWorkID)) {
					PWorkID = BP.Sys.Glo.getRequest().getParameter("PWorkID");
				}

				String pTitle = DBAccess.RunSQLReturnStringIsNull(
						"SELECT Title FROM  ND" + Integer.parseInt(ndFrom.getFK_Flow()) + "01 WHERE OID=" + PWorkID,
						"");

				//// 记录当前流程被调起。
				// this.AddToTrack(ActionType.StartSubFlow, WebUser.getNo(),
				// WebUser.Name, ndFrom.NodeID, ndFrom.FlowName + "\t\n" +
				//// ndFrom.FlowName, "被父流程(" + ndFrom.FlowName + ":" + pTitle +
				//// ")调起.");

				// 记录父流程被调起。
				BP.WF.Dev2Interface.WriteTrack(this.getHisFlow().getNo(), this.getHisNode().getNodeID(),
						this.getWorkID(), 0, "被{" + ndFrom.getFlowName() + "}发起,发起人:" + this.getExecerName(),
						ActionType.CallChildenFlow, "@PWorkID=" + PWorkID + "@PFlowNo=" + ndFrom.getHisFlow().getNo(),
						"发起子流程:" + this.getHisFlow().getName(), null);
			}
		}
		// 产生开始工作流程记录.
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(this.getHisWork().getOID());
		int srcNum = gwf.RetrieveFromDBSources();
		if (srcNum == 0) {
			gwf.setWFState(WFState.Runing);
		} else {
			if (gwf.getWFState() == WFState.Blank) {
				gwf.setWFState(WFState.Runing);
			}

			SendNodeWFState = gwf.getWFState(); // 设置发送前的节点状态。
		}
		/// #region 设置流程标题.
		if (this.title == null) {
			if (this.getHisFlow().getTitleRole().equals("@OutPara")
					|| DotNetToJavaStringHelper.isNullOrEmpty(this.getHisFlow().getTitleRole()) == true) {
				// 如果是外部参数,
				gwf.setTitle(DBAccess.RunSQLReturnString(
						"SELECT Title FROM " + this.getHisFlow().getPTable() + " WHERE OID=" + this.getWorkID()));
				if (DotNetToJavaStringHelper.isNullOrEmpty(gwf.getTitle())) {
					gwf.setTitle(this.getExecer() + "," + this.getExecerName() + "在:" + DataType.getCurrentDataTimeCN()
							+ "发起.");
				}
				// throw new
				// Exception("您设置的流程标题生成规则为外部传来的参数，但是您岋料在创建空白工作时，流程标题值为空。");
			} else {
				gwf.setTitle(BP.WF.WorkFlowBuessRole.GenerTitle(this.getHisFlow(), this.getHisWork()));
			}
		} else {
			gwf.setTitle(this.title);
		}

		// 流程标题.
		this.rptGe.setTitle(gwf.getTitle());

		/// #endregion 设置流程标题.

		if (DotNetToJavaStringHelper.isNullOrEmpty(rptGe.getBillNo())) {
			// 处理单据编号.
			Object tempVar = this.getHisFlow().getBillNoFormat();
			String billNo = (String) ((tempVar instanceof String) ? tempVar : null);
			if (DotNetToJavaStringHelper.isNullOrEmpty(billNo) == false) {
				billNo = BP.WF.WorkFlowBuessRole.GenerBillNo(billNo, this.getWorkID(), this.rptGe,
						this.getHisFlow().getPTable());
				gwf.setBillNo(billNo);
				this.rptGe.setBillNo(billNo);
			}
		} else {
			gwf.setBillNo(rptGe.getBillNo());
		}

		this.getHisWork().SetValByKey("Title", gwf.getTitle());
		gwf.setRDT(this.getHisWork().getRDT());
		gwf.setStarter(this.getExecer());
		gwf.setStarterName(this.getExecerName());
		gwf.setFK_Flow(this.getHisNode().getFK_Flow());
		gwf.setFlowName(this.getHisNode().getFlowName());
		gwf.setFK_FlowSort(this.getHisNode().getHisFlow().getFK_FlowSort());
		gwf.setSysType(this.getHisNode().getHisFlow().getSysType());
		gwf.setFK_Node(this.getHisNode().getNodeID());
		gwf.setNodeName(this.getHisNode().getName());
		gwf.setFK_Dept(this.getHisWork().getRecOfEmp().getFK_Dept());
		gwf.setDeptName(this.getHisWork().getRecOfEmp().getFK_DeptText());

		if (this.getHisFlow().getHisTimelineRole() == TimelineRole.ByFlow) {
			try {
				gwf.setSDTOfFlow(this.getHisWork().GetValStrByKey(WorkSysFieldAttr.SysSDTOfFlow));
			} catch (RuntimeException ex) {
				Log.DefaultLogWriteLineError("可能是流程设计错误,获取开始节点{" + gwf.getTitle()
						+ "}的整体流程应完成时间有错误,是否包含SysSDTOfFlow字段? 异常信息:" + ex.getMessage());
				// 获取开始节点的整体流程应完成时间有错误,是否包含SysSDTOfFlow字段? .
				if (this.getHisWork().getEnMap().getAttrs().Contains(WorkSysFieldAttr.SysSDTOfFlow) == false) {
					throw new RuntimeException(
							"流程设计错误，您设置的流程时效属性是｛按开始节点表单SysSDTOfFlow字段计算｝，但是开始节点表单不包含字段 SysSDTOfFlow , 系统错误信息:"
									+ ex.getMessage());
				}
				throw new RuntimeException("初始化开始节点数据错误:" + ex.getMessage());
			}
		}

		// 加入两个参数. 2013-02-17
		if (gwf.getPWorkID() != 0) {
			this.rptGe.setPWorkID(gwf.getPWorkID());
			this.rptGe.setPFlowNo(gwf.getPFlowNo());
			this.rptGe.setPNodeID(gwf.getPNodeID());
			this.rptGe.setPEmp(gwf.getPEmp());
		} else {
			try {
				gwf.setPWorkID(this.rptGe.getPWorkID());
			} catch (RuntimeException e) {
				gwf.setPWorkID(0);
			}

			try {
				gwf.setPFlowNo(this.rptGe.getPFlowNo());
			} catch (RuntimeException e) {
				gwf.setPFlowNo("");
			}
			try {
				gwf.setPNodeID(this.rptGe.getPNodeID());
			} catch (RuntimeException e) {
				gwf.setPNodeID(0);
			}
			try {
				gwf.setPEmp(this.rptGe.getPEmp());
			} catch (RuntimeException e) {
				gwf.setPEmp(WebUser.getNo());
			}
		}

		// 生成FlowNote
		Object tempVar2 = this.getHisFlow().getFlowNoteExp();
		String note = (String) ((tempVar2 instanceof String) ? tempVar2 : null);
		if (DotNetToJavaStringHelper.isNullOrEmpty(note) == false) {
			note = BP.WF.Glo.DealExp(note, this.rptGe, null);
		}
		this.rptGe.setFlowNote(note);
		gwf.setFlowNote(note);

		if (srcNum == 0) {
			gwf.DirectInsert();
		} else {
			gwf.DirectUpdate();
		}

		StartWork sw = (StartWork) this.getHisWork();
		this.setHisGenerWorkFlow(gwf);

		// #region 产生开始工作者,能够执行他们的人员.
		GenerWorkerList wl = new GenerWorkerList();
		wl.setWorkID(this.getHisWork().getOID());
		wl.setFK_Node(this.getHisNode().getNodeID());
		wl.setFK_Emp(this.getExecer());
		wl.Delete();

		wl.setFK_NodeText(this.getHisNode().getName());
		wl.setFK_EmpText(this.getExecerName());
		wl.setFK_Flow(this.getHisNode().getFK_Flow());
		wl.setFK_Dept(this.getExecerDeptNo());
		wl.setWarningHour(0);
		wl.setSDT(DataType.getCurrentDataTime());
		wl.setDTOfWarning(DataType.getCurrentData());
		wl.setRDT(DataType.getCurrentDataTime());

		try {
			wl.Save();
		} catch (java.lang.Exception e) {
			wl.CheckPhysicsTable();
			wl.Update();
		}
	}

	/**
	 * 执行将当前工作节点的数据copy到Rpt里面去.
	 * @throws Exception 
	 */
	public final void DoCopyCurrentWorkDataToRpt() throws Exception {
		// 如果两个表一致就返回..
		// 把当前的工作人员增加里面去.
		String str = rptGe.GetValStrByKey(GERptAttr.FlowEmps);
		if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserIDOnly) {
			if (str.contains("@" + this.getExecer()) == false) {
				rptGe.SetValByKey(GERptAttr.FlowEmps, str + "@" + this.getExecer());
			}
		}

		if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserNameOnly) {
			if (str.contains("@" + WebUser.getName()) == false) {
				rptGe.SetValByKey(GERptAttr.FlowEmps, str + "@" + this.getExecerName());
			}
		}

		if (Glo.getUserInfoShowModel() == UserInfoShowModel.UserIDUserName) {
			if (str.contains("@" + this.getExecer() + "," + this.getExecerName()) == false) {
				rptGe.SetValByKey(GERptAttr.FlowEmps, str + "@" + this.getExecer() + "," + this.getExecerName());
			}
		}
		rptGe.setFlowEnder(this.getExecer());
		rptGe.setFlowEnderRDT(DataType.getCurrentDataTime());

		// 设置当前的流程所有的用时.
		rptGe.setFlowDaySpan(DataType.GetSpanDays(this.rptGe.GetValStringByKey(GERptAttr.FlowStartRDT),
				DataType.getCurrentDataTime()));

		if (this.getHisNode().getIsEndNode() || this.getIsStopFlow()) {
			rptGe.setWFState(WFState.Complete);
		} else {
			rptGe.setWFState(WFState.Runing);
		}

		if (this.getHisFlow().getPTable().equals(this.getHisWork().getEnMap().getPhysicsTable())) {
			rptGe.DirectUpdate();
		} else {
			// 将当前的属性复制到rpt表里面去.
			DoCopyRptWork(this.getHisWork());
			rptGe.DirectUpdate();
		}
	}

	/**
	 * 执行数据copy.
	 * 
	 * @param fromWK
	 */
	public final void DoCopyRptWork(Work fromWK) {
		for (Attr attr : fromWK.getEnMap().getAttrs()) {
			String key = attr.getKey();
			if (key.equals(GERptAttr.FK_NY) || key.equals(GERptAttr.FK_Dept) || key.equals(GERptAttr.FlowDaySpan)
					|| key.equals(GERptAttr.FlowEmps) || key.equals(GERptAttr.FlowEnder)
					|| key.equals(GERptAttr.FlowEnderRDT) || key.equals(GERptAttr.FlowEndNode)
					|| key.equals(GERptAttr.FlowStarter) || key.equals(GERptAttr.Title)
					|| key.equals(GERptAttr.WFSta)) {
				continue;
			}

			Object obj = fromWK.GetValByKey(attr.getKey());
			if (obj == null) {
				continue;
			}
			this.rptGe.SetValByKey(attr.getKey(), obj);
		}
		if (this.getHisNode().getIsStartNode()) {
			this.rptGe.SetValByKey("Title", fromWK.GetValByKey("Title"));
		}
	}
	
	/// <summary>
    /// 增加日志
    /// </summary>
    /// <param name="at"></param>
    /// <param name="gwl"></param>
    /// <param name="msg"></param>
    public void AddToTrack(ActionType at, GenerWorkerList gwl, String msg, long subTreadWorkID) throws Exception
    {
        Track t = new Track();

        if (this.getHisGenerWorkFlow().getFID() == 0)
        {
            t.setWorkID(subTreadWorkID);
            t.setFID( this.getHisWork().getOID());
        }
        else
        {
            t.setWorkID( this.getHisWork().getOID());
            t.setFID( this.getHisGenerWorkFlow().getFID());
        }

        t.setRDT( DataType.getCurrentDataTimess());
        t.setHisActionType( at);

        t.setNDFrom( this.getndFrom().getNodeID());
        t.setNDFromT(this.getndFrom().getName());

        t.setEmpFrom(this.getExecer());
        t.setEmpFromT(this.getExecerName());
        t.setFK_Flow( this.getHisNode().getFK_Flow());

   //     t.Tag = tag + "@SendNode=" + this.HisNode.NodeID;

        t.setNDTo( gwl.getFK_Node());
        t.setNDToT( gwl.getFK_NodeText());

        t.setEmpTo ( gwl.getFK_Emp());
        t.setEmpToT ( gwl.getFK_EmpText());
        t.setMsg ( msg);
        //t.FrmDB = frmDBJson; //表单数据Json.

        /*
        switch (at)
        {
            case ActionType.Forward:
            case ActionType.ForwardAskfor:
            case ActionType.Start:
            case ActionType.UnSend:
            case ActionType.ForwardFL:
            case ActionType.ForwardHL:
            case ActionType.TeampUp:
                //判断是否有焦点字段，如果有就把它记录到日志里。
                if (this.HisNode.FocusField.Length > 1)
                {
                    string exp = this.HisNode.FocusField;
                    if (this.rptGe != null)
                        exp = Glo.DealExp(exp, this.rptGe, null);
                    else
                        exp = Glo.DealExp(exp, this.HisWork, null);

                    t.Msg += exp;
                    if (t.Msg.Contains("@"))
                        Log.DebugWriteError("@在节点(" + this.HisNode.NodeID + " ， " + this.HisNode.Name + ")焦点字段被删除了,表达式为:" + this.HisNode.FocusField + " 替换的结果为:" + t.Msg);
                }
                break;
            default:
                break;
        }


        if (at == ActionType.SubThreadForward
            || at == ActionType.StartChildenFlow
            || at == ActionType.Start
            || at == ActionType.Forward
            || at == ActionType.SubThreadForward
            || at == ActionType.ForwardHL
            || at == ActionType.FlowOver)
        {
            if (this.HisNode.IsFL)
                at = ActionType.ForwardFL;
            t.FrmDB = this.HisWork.ToJson();
        }*/

       
            // t.MyPK = t.WorkID + "_" + t.FID + "_"  + t.NDFrom + "_" + t.NDTo +"_"+t.EmpFrom+"_"+t.EmpTo+"_"+ DateTime.Now.ToString("yyMMddHHmmss");
        t.Insert();
       /*

        if (at == ActionType.SubThreadForward
          || at == ActionType.StartChildenFlow
          || at == ActionType.Start
          || at == ActionType.Forward
          || at == ActionType.SubThreadForward
          || at == ActionType.ForwardHL
          || at == ActionType.FlowOver)
        {
            this.HisGenerWorkFlow.Paras_LastSendTruckID = t.MyPK;
        }*/
    }

	/**
	 * 增加日志
	 * 
	 * @param at
	 *            类型
	 * @param toEmp
	 *            到人员
	 * @param toEmpName
	 *            到人员名称
	 * @param toNDid
	 *            到节点
	 * @param toNDName
	 *            到节点名称
	 * @param msg
	 *            消息
	 * @throws Exception 
	 */
	public final void AddToTrack(ActionType at, String toEmp, String toEmpName, int toNDid, String toNDName,
			String msg) throws Exception {
		AddToTrack(at, toEmp, toEmpName, toNDid, toNDName, msg, this.getHisNode());
	}

	/**
	 * 增加日志
	 * 
	 * @param at
	 *            类型
	 * @param toEmp
	 *            到人员
	 * @param toEmpName
	 *            到人员名称
	 * @param toNDid
	 *            到节点
	 * @param toNDName
	 *            到节点名称
	 * @param msg
	 *            消息
	 * @throws Exception 
	 */
	public final void AddToTrack(ActionType at, String toEmp, String toEmpName, int toNDid, String toNDName, String msg,
			Node ndFrom, String frmDBJson, String tag) throws Exception {
		Track t = new Track();
		t.setWorkID(this.getHisWork().getOID());
		t.setFID(this.getHisWork().getFID());
		t.setRDT(DataType.getCurrentDataTimess());
		t.setHisActionType(at);

		t.setNDFrom(ndFrom.getNodeID());
		t.setNDFromT(ndFrom.getName());

		t.setEmpFrom(this.getExecer());
		t.setEmpFromT(this.getExecerName());
		t.FK_Flow = this.getHisNode().getFK_Flow();
		t.setTag(tag);

		if (toNDid == 0) {
			toNDid = this.getHisNode().getNodeID();
			toNDName = this.getHisNode().getName();
		}

		t.setNDTo(toNDid);
		t.setNDToT(toNDName);

		t.setEmpTo(toEmp);
		t.setEmpToT(toEmpName);
		t.setMsg(msg);
		t.FrmDB = frmDBJson; // 表单数据Json.

		switch (at) {
		case Forward:
		case ForwardAskfor:
		case Start:
		case UnSend:
		case ForwardFL:
		case ForwardHL:
			// 判断是否有焦点字段，如果有就把它记录到日志里。
			if (this.getHisNode().getFocusField().length() > 1) {
				String exp = this.getHisNode().getFocusField();
				if (this.rptGe != null) {
					exp = Glo.DealExp(exp, this.rptGe, null);
				} else {
					exp = Glo.DealExp(exp, this.getHisWork(), null);
				}

				t.setMsg(t.getMsg() + exp);
				if (t.getMsg().contains("@")) {
					Log.DebugWriteError("@在节点(" + this.getHisNode().getNodeID() + " ， " + this.getHisNode().getName()
							+ ")焦点字段被删除了,表达式为:" + this.getHisNode().getFocusField() + " 替换的结果为:" + t.getMsg());
				}
			}
			break;
		default:
			break;
		}

		if (at == ActionType.SubThreadForward || at == ActionType.StartChildenFlow || at == ActionType.Start
				|| at == ActionType.Forward || at == ActionType.SubThreadForward || at == ActionType.ForwardHL
				|| at == ActionType.FlowOver) {
			if (this.getHisNode().getIsFL()) {
				at = ActionType.ForwardFL;
			}
			t.FrmDB = this.getHisWork().ToJson();
		}

		try {
			// t.MyPK = t.WorkID + "_" + t.FID + "_" + t.NDFrom + "_" + t.NDTo
			// +"_"+t.EmpFrom+"_"+t.EmpTo+"_"+
			// DateTime.Now.ToString("yyMMddHHmmss");
			t.Insert();
		} catch (java.lang.Exception e) {
			t.CheckPhysicsTable();
		}

		if (at == ActionType.SubThreadForward || at == ActionType.StartChildenFlow || at == ActionType.Start
				|| at == ActionType.Forward || at == ActionType.SubThreadForward || at == ActionType.ForwardHL
				|| at == ActionType.FlowOver) {
			this.getHisGenerWorkFlow().setParas_LastSendTruckID(t.getMyPK());
		}

	}

	/**
	 * 向他们发送消息
	 * 
	 * @param gwls
	 *            接受人
	 * @throws Exception 
	 */
	public final void SendMsgToThem(GenerWorkerLists gwls) throws Exception {
		if (BP.WF.Glo.getIsEnableSysMessage() == false) {
			return;
		}

		// 求到达人员的IDs
		String toEmps = "";
		for (GenerWorkerList gwl : gwls.ToJavaList()) {
			toEmps += gwl.getFK_Emp() + ",";
		}

		// 处理工作到达事件.
		PushMsgs pms = this.town.getHisNode().getHisPushMsgs();
		for (PushMsg pm : pms.ToJavaList()) {
			if (!pm.getFK_Event().equals(EventListOfNode.WorkArrive)) {
				continue;
			}

			String msg = pm.DoSendMessage(this.town.getHisNode(), this.town.getHisWork(), null, null, null, toEmps);
			this.addMsg("alert" + pm.getMyPK(), msg, msg, SendReturnMsgType.Info);
			// this.addMsg(SendReturnMsgFlag.SendSuccessMsg, "已经转给，加签的发起人(" +
			// item.FK_Emp + "," + item.FK_EmpText + ")",
			// SendReturnMsgType.Info);
		}
		/*
		 * //#region 判断是否可以发送. boolean isSendEmail = false; boolean isSendSMS =
		 * false; FrmEvent fEvent = this.getHisNode().getSendSuccess_FrmEvent();
		 * switch (fEvent.getMsgCtrl()) { case BySet: //是否启动消息? isSendEmail =
		 * fEvent.getMailEnable(); isSendSMS = fEvent.getSMSEnable();
		 * 
		 * if (isSendEmail == false && isSendSMS == false) { return; } break;
		 * case ByFrmIsSendMsg: try { //从表单字段里取参数. if
		 * (this.getHisWork().getRow().containsKey("IsSendEmail") == true) {
		 * isSendEmail = this.getHisWork().GetValBooleanByKey("IsSendEmail"); }
		 * if (this.getHisWork().getRow().containsKey("IsSendSMS") == true) {
		 * isSendSMS = this.getHisWork().GetValBooleanByKey("IsSendSMS"); }
		 * 
		 * if (isSendEmail == false && isSendSMS == false) { return; } } catch
		 * (java.lang.Exception e) { if
		 * (this.getHisWork().getRow().containsKey("IsSendEmail") == false ||
		 * this.getHisWork().getRow().containsKey("IsSendSMS") == false) { throw
		 * new RuntimeException("没有在ccform里接收到IsSendEmail， IsSendSMS 参数."); } }
		 * break; case BySDK: try { if
		 * (this.getHisWork().GetValBooleanByKey("IsSendMsg") == false) {
		 * return; } } catch (java.lang.Exception e2) { if
		 * (this.getHisWork().getRow().containsKey("IsSendMsg") == false) {
		 * throw new RuntimeException("没有接收到IsSendMsg参数."); } } break; default:
		 * break; } // 取出模版文件. String hostUrl = Glo.getHostURL();
		 * 
		 * //邮件标题. String mailTitle = fEvent.getMailTitle();
		 * 
		 * //邮件内容. String mailDoc = fEvent.getMailDoc(); String mailEnd =
		 * "<a href='{0}'>用计算机打开工作</a>,地址:{0}.";
		 * 
		 * //短消息内容. String msgTemp = fEvent.getSMSDoc(); for (GenerWorkerList wl
		 * : gwls.ToJavaList()) { if (wl.getIsEnable() == false) { continue; }
		 * 
		 * // 邮件标题. String title = ""; if
		 * (DotNetToJavaStringHelper.isNullOrEmpty(mailTitle)) { title =
		 * String.format("流程:%1$s.工作:%2$s,发送人:%3$s,标题:%4$s,需您处理.",
		 * this.getHisNode().getFlowName(), wl.getFK_NodeText(),
		 * this.getExecerName(), this.rptGe.getTitle()); } else { title =
		 * Glo.DealExp(mailTitle, this.getHisWork(), null); }
		 * 
		 * //邮件内容. String sid = wl.getFK_Emp() + "_" + wl.getWorkID() + "_" +
		 * wl.getFK_Node() + "_" + wl.getRDT(); String url = hostUrl +
		 * "WF/Do.jsp?DoType=OF&SID=" + sid; url = url.replace("//", "/"); url =
		 * url.replace("//", "/"); mailDoc = Glo.DealExp(mailDoc,
		 * this.getHisWork(), null); mailDoc += "\t\n " + String.format(mailEnd,
		 * url); mailDoc = mailDoc.replace("{Url}", url);
		 * 
		 * // 短信信息. if (DotNetToJavaStringHelper.isNullOrEmpty(msgTemp) == true)
		 * { msgTemp = "新工作:" + this.rptGe.getTitle() +
		 * "发送人:"+Glo.DealUserInfoShowModel(WebUser.getNo(), WebUser.getName())+
		 * ",流程:" + this.getHisFlow().getName(); } else { msgTemp =
		 * Glo.DealExp(msgTemp, this.getHisWork(), null); }
		 * 
		 * BP.WF.Dev2Interface.Port_SendMsg(wl.getFK_Emp(), title, mailDoc,
		 * "WKAlt" + wl.getFK_Node() + "_" + wl.getWorkID(),
		 * BP.WF.SMSMsgType.SendSuccess, wl.getFK_Flow(), wl.getFK_Node(),
		 * wl.getWorkID(), wl.getFID()); }
		 */
	}

	/**
	 * 增加日志
	 * 
	 * @param at
	 *            类型
	 * @param toEmp
	 *            到人员
	 * @param toEmpName
	 *            到人员名称
	 * @param toNDid
	 *            到节点
	 * @param toNDName
	 *            到节点名称
	 * @param msg
	 *            消息
	 * @throws Exception 
	 */
	public final void AddToTrack(ActionType at, String toEmp, String toEmpName, int toNDid, String toNDName, String msg,
			Node ndFrom) throws Exception {
		Track t = new Track();
		t.setWorkID(this.getHisWork().getOID());
		t.setFID(this.getHisWork().getFID());
		t.setRDT(DataType.getCurrentDataTime());
		t.setHisActionType(at);

		t.setNDFrom(ndFrom.getNodeID());
		t.setNDFromT(ndFrom.getName());

		t.setEmpFrom(this.getExecer());
		t.setEmpFromT(this.getExecerName());
		t.FK_Flow = this.getHisNode().getFK_Flow();

		if (toNDid == 0) {
			toNDid = this.getHisNode().getNodeID();
			toNDName = this.getHisNode().getName();
		}

		t.setNDTo(toNDid);
		t.setNDToT(toNDName);

		t.setEmpTo(toEmp);
		t.setEmpToT(toEmpName);
		t.setMsg(msg);

		switch (at) {
		case Forward:
		case Start:
		case UnSend:
		case ForwardFL:
		case ForwardHL:
			// 判断是否有焦点字段，如果有就把它记录到日志里。
			if (this.getHisNode().getFocusField().length() > 1) {
				String exp = this.getHisNode().getFocusField();
				if (this.rptGe != null) {
					exp = Glo.DealExp(exp, this.rptGe, null);
				} else {
					exp = Glo.DealExp(exp, this.getHisWork(), null);
				}

				t.setMsg(t.getMsg() + exp);
				if (t.getMsg().contains("@")) {
					Log.DebugWriteError("@在节点(" + this.getHisNode().getNodeID() + " ， " + this.getHisNode().getName()
							+ ")焦点字段被删除了,表达式为:" + this.getHisNode().getFocusField() + " 替换的结果为:" + t.getMsg());
				}
			}
			break;
		default:
			break;
		}

		if (at == ActionType.Forward) {
			if (this.getHisNode().getIsFL()) {
				at = ActionType.ForwardFL;
			}
		}

		try {
			// t.MyPK(t.WorkID + "_" + t.FID + "_" + t.NDFrom + "_" + t.NDTo
			// +"_"+t.EmpFrom+"_"+t.EmpTo+"_"+
			// DateTime.Now.ToString("yyMMddHHmmss"));
			t.Insert();
		} catch (java.lang.Exception e) {
			t.CheckPhysicsTable();
		}
	}

	/**
	 * 发送前的流程状态。
	 */
	private WFState SendNodeWFState = WFState.Blank;
	/**
	 * 合流节点是否全部完成？
	 * 
	 */
	private boolean IsOverMGECheckStand = false;
	private boolean _IsStopFlow = false;

	private boolean getIsStopFlow() {
		return _IsStopFlow;
	}

	private void setIsStopFlow(boolean value) throws Exception {
		_IsStopFlow = value;
		if (_IsStopFlow == true) {
			this.rptGe.setWFState(WFState.Complete);
			this.rptGe.Update("WFState", WFState.Complete.getValue());
		}
	}

	/**
	 * 检查
	 * @throws Exception 
	 */
	private void CheckCompleteCondition_IntCompleteEmps() throws Exception {
		String sql = "SELECT FK_Emp,FK_EmpText FROM WF_GenerWorkerlist WHERE WorkID=" + this.getWorkID()
				+ " AND IsPass=1";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		String emps = "@";
		String flowEmps = "@";
		for (DataRow dr : dt.Rows) {
			if (emps.contains("@" + dr.getValue(0).toString() + "@")) {
				continue;
			}

			emps = emps + dr.getValue(0).toString() + "@";
			flowEmps = flowEmps + dr.getValue(1) + "," + dr.getValue(0).toString() + "@";
		}
		// 追加当前操作人
		if (emps.contains("@" + WebUser.getNo() + "@") == false) {
			emps = emps + WebUser.getNo() + "@";
			flowEmps = flowEmps + WebUser.getName() + "," + WebUser.getNo() + "@";
		}
		// 给他们赋值.
		this.rptGe.setFlowEmps(flowEmps);
		this.getHisGenerWorkFlow().setEmps(emps);
	}

	/**
	 * 处理自动运行 - 预先设置未来的运行节点.
	 * @throws Exception 
	 */
	private void DealAutoRunEnable() throws Exception {
		// 检查当前节点是否是自动运行的.
		if (this.getHisNode().getAutoRunEnable() == false) {
			return;
		}

		// 如果是自动运行就要设置自动运行参数.
		Object tempVar = this.getHisNode().getAutoRunParas();
		String exp = (String) ((tempVar instanceof String) ? tempVar : null);
		if (exp == null || exp.equals("")) {
			throw new RuntimeException("@您设置当前是自动运行，但是没有在该节点上设置参数。");
		}

		exp = exp.replace("@OID", (new Long(this.getWorkID())).toString());
		exp = exp.replace("@WorkID", (new Long(this.getWorkID())).toString());

		exp = exp.replace("@NodeID", (new Integer(this.getHisNode().getNodeID())).toString());
		exp = exp.replace("@FK_Node", (new Integer(this.getHisNode().getNodeID())).toString());

		exp = exp.replace("WebUser.No", BP.Web.WebUser.getNo());
		exp = exp.replace("@WebUser.Name", BP.Web.WebUser.getName());
		exp = exp.replace("@WebUser.FK_Dept", BP.Web.WebUser.getFK_Dept());
		exp = exp.replace("@WebUser.FK_DeptName", BP.Web.WebUser.getFK_DeptName());

		if (exp.contains("@") == true) {
			exp = Glo.DealExp(exp, this.getHisWork(), null);
		}

		if (exp.contains("@") == true) {
			throw new RuntimeException("@您配置的表达式没有没被完全的解析下来:" + exp);
		}

		// 没有查询到就不设置了.
		String strs = DBAccess.RunSQLReturnStringIsNull(exp, null);
		if (strs == null) {
			return;
		}

		// 把约定的参数写入到引擎
		Dev2Interface.Flow_SetFlowTransferCustom(this.getHisFlow().getNo(), this.getWorkID(),
				BP.WF.TransferCustomType.ByWorkerSet, strs);
		// 重新执行查询.
		this.getHisGenerWorkFlow().RetrieveFromDBSources();
	}

	/**
	 * 检查流程、节点的完成条件
	 * 
	 * @return
	 * @throws Exception 
	 */
	private void CheckCompleteCondition() throws Exception {
		// 执行初始化人员.
		this.CheckCompleteCondition_IntCompleteEmps();

		// 如果结束流程，就增加如下信息 @于庆海翻译.
		this.getHisGenerWorkFlow().setSender(BP.Web.WebUser.getNo());
		this.getHisGenerWorkFlow().setSendDT(DataType.getCurrentDataTime());

		this.rptGe.setFlowEnder(BP.Web.WebUser.getNo());
		this.rptGe.setFlowEnderRDT(DataType.getCurrentDataTime());

		this.setIsStopFlow(false);
		if (this.getHisNode().getIsEndNode()) {

			if (this.getHisGenerWorkFlow().getTransferCustomType() == TransferCustomType.ByCCBPMDefine) {
				this.setIsStopFlow(true);
				this.getHisGenerWorkFlow().setWFState(WFState.Complete);
				this.rptGe.setWFState(WFState.Complete);

				String msg = this.getHisWorkFlow().DoFlowOver(ActionType.FlowOver, "流程已经走到最后一个节点，流程成功结束。",
						this.getHisNode(), this.rptGe);
				this.addMsg(SendReturnMsgFlag.End, msg);
			}

			return;
		}
		this.addMsg(SendReturnMsgFlag.OverCurr, String.format("当前工作[%1$s]已经完成", this.getHisNode().getName()));
		/// #region 判断流程条件.
		try {
			
			if (this.getHisNode().getHisToNodes().size() == 0 && this.getHisNode().getIsStartNode()) {

				// 如果流程完成
				this.getHisWorkFlow().DoFlowOver(ActionType.FlowOver, "符合流程完成条件", this.getHisNode(), this.rptGe);
				this.setIsStopFlow(true);
				this.addMsg(SendReturnMsgFlag.OneNodeSheetver, "工作已经成功处理(一个流程的工作)。",
						"工作已经成功处理(一个流程的工作)。 @查看<img src='" + getVirPath()
								+ "WF/Img/Btn/PrintWorkRpt.gif' ><a href='WFRpt.jsp?WorkID="
								+ this.getHisWork().getOID() + "&FID=" + this.getHisWork().getFID() + "&FK_Flow="
								+ this.getHisNode().getFK_Flow() + "' target='_self' >工作轨迹</a>",
						SendReturnMsgType.Info);
				return;
			}

			if (this.getHisNode().getIsCCFlow() && this.getHisFlowCompleteConditions().getIsPass()) {

				String stopMsg = this.getHisFlowCompleteConditions().getConditionDesc();
				// 如果流程完成
				String overMsg = this.getHisWorkFlow().DoFlowOver(ActionType.FlowOver, "符合流程完成条件:" + stopMsg,
						this.getHisNode(), this.rptGe);
				this.setIsStopFlow(true);

				// string path = BP.Sys.Glo.Request.ApplicationPath;
				this.addMsg(SendReturnMsgFlag.MacthFlowOver, "@符合工作流程完成条件" + stopMsg + "" + overMsg,
						"@符合工作流程完成条件" + stopMsg + "" + overMsg + " @查看<img src='" + getVirPath()
								+ "WF/Img/Btn/PrintWorkRpt.gif' ><a href='WFRpt.jsp?WorkID="
								+ this.getHisWork().getOID() + "&FID=" + this.getHisWork().getFID() + "&FK_Flow="
								+ this.getHisNode().getFK_Flow() + "' target='_self' >工作轨迹</a>",
						SendReturnMsgType.Info);
				return;
			}
		} catch (RuntimeException ex) {
			throw new RuntimeException(
					String.format("@判断流程{0}完成条件出现错误." + ex.getMessage(), this.getHisNode().getName()));
		}
	}

	/**
	 * 生成为什么发送给他们
	 * 
	 * @param fNodeID
	 * @param toNodeID
	 * @return
	 */
	public final String GenerWhySendToThem(int fNodeID, int toNodeID) {
		return "";
		// return "@<a href='WhySendToThem.jsp?NodeID=" + fNodeID + "&ToNodeID="
		// + toNodeID + "&WorkID=" + this.WorkID + "' target=_blank >" +
		// this.ToE("WN20", "为什么要发送给他们？") + "</a>";
	}

	/**
	 * 工作流程ID
	 */
	public static long FID = 0;

	/**
	 * 没有FID
	 * 
	 * @param nd
	 * @return
	 */
	private String StartNextWorkNodeHeLiu_WithOutFID(Node nd) {
		throw new RuntimeException("未完成:StartNextWorkNodeHeLiu_WithOutFID");
	}

	/**
	 * 异表单子线程向合流点运动
	 * 
	 * @param nd
	 * @throws Exception 
	 */
	private void NodeSend_53_UnSameSheet_To_HeLiu(Node nd) throws Exception {


		 Work heLiuWK = nd.getHisWork();
         heLiuWK.setOID(this.getHisWork().getFID());
         if (heLiuWK.RetrieveFromDBSources() == 0) //查询出来数据.
             heLiuWK.DirectInsert();

         heLiuWK.Copy(this.getHisWork()); // 执行copy.

         heLiuWK.setOID(this.getHisWork().getFID());
         heLiuWK.setFID( 0);

         this.town = new WorkNode(heLiuWK, nd);

         //合流节点上的工作处理者。
         GenerWorkerLists gwls = new GenerWorkerLists(this.getHisWork().getFID(), nd.getNodeID());
         current_gwls = gwls;

         if (gwls.size() == 0)
         {
             // 说明第一次到达河流节点。
             current_gwls = this.Func_GenerWorkerLists(this.town);
             gwls = current_gwls;

             GenerWorkFlow gwf = new GenerWorkFlow(this.getHisWork().getFID());
             gwf.setFK_Node(nd.getNodeID());
             gwf.setNodeName(nd.getName());
             gwf.setTodoEmpsNum( gwls.size()) ;

             String todoEmps = "";
             for (GenerWorkerList  item : gwls.ToJavaList())
                 todoEmps += item.getFK_Emp() + "," + item.getFK_EmpText() + ";";

             gwf.setTodoEmps(todoEmps);
             gwf.setWFState( WFState.Runing);
             gwf.Update();
         }

         String FK_Emp = "";
         String toEmpsStr = "";
         String emps = "";
         for (GenerWorkerList wl : gwls.ToJavaList())
         {
             toEmpsStr += BP.WF.Glo.DealUserInfoShowModel(wl.getFK_Emp(), wl.getFK_EmpText());
             if (gwls.size() == 1)
                 emps = toEmpsStr;
             else
                 emps += "@" + toEmpsStr;
         }
 

         //#region 复制主表数据. edit 2014-11-20 向合流点汇总数据.
         //复制当前节点表单数据.
         heLiuWK.setFID( 0);
         heLiuWK.setRec( FK_Emp);
         heLiuWK.setEmps(emps);
         heLiuWK.setOID( this.getHisWork().getFID());
         heLiuWK.DirectUpdate(); //在更新一次.

         /* 把数据复制到rpt数据表里. */
         this.rptGe.setOID(this.getHisWork().getFID());
         this.rptGe.RetrieveFromDBSources();
         this.rptGe.Copy(this.getHisWork());
         this.rptGe.DirectUpdate();
         //#endregion 复制主表数据.

         // 产生合流汇总明细表数据.
       //  this.GenerHieLiuHuiZhongDtlData_2013(nd);

         //#endregion 处理合流节点表单数据

         /* 合流点需要等待各个分流点全部处理完后才能看到它。*/
         String info = "";
         String sql1 = "";
//#warning 对于多个分合流点可能会有问题。
         ps = new Paras();
         ps.SQL = "SELECT COUNT(distinct WorkID) AS Num FROM WF_GenerWorkerList WHERE  FID=" + dbStr + "FID AND FK_Node IN (" + this.SpanSubTheadNodes(nd) + ")";
         ps.Add("FID", this.getHisWork().getFID());

         java.math.BigDecimal numAll1 = new BigDecimal(DBAccess.RunSQLReturnValInt(ps));
			java.math.BigDecimal passRate1 = numAll1.divide(numAll1, 2).multiply(new BigDecimal(100));
			if (nd.getPassRate().compareTo(passRate1) <= 0) {
				
		 
             ps = new Paras();
             ps.SQL = "UPDATE WF_GenerWorkerList SET IsPass=0,FID=0 WHERE FK_Node=" + dbStr + "FK_Node AND WorkID=" + dbStr + "WorkID";
             ps.Add("FK_Node", nd.getNodeID());
             ps.Add("WorkID", this.getHisWork().getFID());
             DBAccess.RunSQL(ps);

             ps = new Paras();
             ps.SQL = "UPDATE WF_GenerWorkFlow SET FK_Node=" + dbStr + "FK_Node,NodeName=" + dbStr + "NodeName WHERE WorkID=" + dbStr + "WorkID";
             ps.Add("FK_Node", nd.getNodeID());
             ps.Add("NodeName", nd.getName());
             ps.Add("WorkID", this.getHisWork().getFID());
             DBAccess.RunSQL(ps);

             info = "@下一步合流点(" + nd.getName() + ")已经启动。";
         }
         else
         {
//#warning 为了不让其显示在途的工作需要， =3 不是正常的处理模式。
             ps = new Paras();
             ps.SQL = "UPDATE WF_GenerWorkerList SET IsPass=3,FID=0 WHERE FK_Node=" + dbStr + "FK_Node AND WorkID=" + dbStr + "WorkID";
             ps.Add("FK_Node", nd.getNodeID());
             ps.Add("WorkID", this.getHisWork().getOID());
             DBAccess.RunSQL(ps);
         }

         this.getHisGenerWorkFlow().setFK_Node(nd.getNodeID());
         this.getHisGenerWorkFlow().setNodeName(nd.getName());
  
         this.addMsg(SendReturnMsgFlag.VarAcceptersID, emps, SendReturnMsgType.SystemMsg);
         this.addMsg("HeLiuInfo", "@下一步的工作处理人[" + emps + "]" + info, SendReturnMsgType.Info);
		
	}

	/**
	 * 产生合流汇总数据 把子线程的子表主表数据放到合流点的从表上去
	 * 
	 * @param nd
	 * @throws Exception 
	 */
	private void GenerHieLiuHuiZhongDtlData_2013(Node ndOfHeLiu) throws Exception {

		/// #region 汇总明细表.
		MapDtls mydtls = ndOfHeLiu.getHisWork().getHisMapDtls();
		for (MapDtl dtl : mydtls.ToJavaList()) {
			if (dtl.getIsHLDtl() == false) {
				continue;
			}

			GEDtl geDtl = dtl.getHisGEDtl();
			geDtl.Copy(this.getHisWork());
			geDtl.setRefPK((new Long(this.getHisWork().getFID())).toString()); // RefPK
																				// 就是当前子线程的FID.
			geDtl.setRec(this.getExecer());
			geDtl.setRDT(DataType.getCurrentDataTime());

			/// #region 判断是否是质量评价
			if (ndOfHeLiu.getIsEval()) {
				// 如果是质量评价流程
				geDtl.SetValByKey(WorkSysFieldAttr.EvalEmpNo, this.getExecer());
				geDtl.SetValByKey(WorkSysFieldAttr.EvalEmpName, this.getExecerName());
				geDtl.SetValByKey(WorkSysFieldAttr.EvalCent, 0);
				geDtl.SetValByKey(WorkSysFieldAttr.EvalNote, "");
			}

			/// #endregion

			/// #region 执行插入数据.
			try {
				geDtl.InsertAsOID(this.getHisWork().getOID());
			} catch (java.lang.Exception e) {
				geDtl.Update();
			}

			/// #endregion 执行插入数据.

			/// #region 还要处理附件的 copy 汇总. 如果子线程上有附件组件.
			if (dtl.getIsEnableAthM() == true) {
				// 如果启用了多附件。
				// 取出来所有的上个节点的数据集合.
				FrmAttachments athSLs = this.getHisWork().getHisFrmAttachments();
				if (athSLs.size() == 0) {
					break; // 子线程上没有附件组件.
				}

				// 求子线程的汇总附件集合 (处理如果子线程上有多个附件，其中一部分附件需要汇总另外一部分不需要汇总的模式)
				String strs = "";
				for (FrmAttachment item : athSLs.ToJavaList()) {
					if (item.getIsToHeLiuHZ() == true) {
						strs += "," + item.getMyPK() + ",";
					}
				}

				// 如果没有找到，并且附件集合只有1个，就设置他为子线程的汇总附件，可能是设计人员忘记了设计.
				if (strs.equals("") && athSLs.size() == 1) {
					FrmAttachment athT = (FrmAttachment) ((athSLs.get(0) instanceof FrmAttachment) ? athSLs.get(0)
							: null);
					athT.setIsToHeLiuHZ(true);
					athT.Update();
					strs = "," + athT.getMyPK() + ",";
				}

				// 没有找到要执行的附件.
				if (strs.equals("")) {
					break;
				}

				// 取出来所有的上个节点的数据集合.
				FrmAttachmentDBs athDBs = new FrmAttachmentDBs();
				athDBs.Retrieve(FrmAttachmentDBAttr.FK_MapData, this.getHisWork().NodeFrmID,
						FrmAttachmentDBAttr.RefPKVal, this.getHisWork().getOID());

				if (athDBs.size() == 0) {
					break; // 子线程没有上传附件.
				}

				// 说明当前节点有附件数据
				for (FrmAttachmentDB athDB : athDBs.ToJavaList()) {
					if (strs.contains("," + athDB.getFK_FrmAttachment() + ",") == false) {
						continue;
					}

					FrmAttachmentDB athDB_N = new FrmAttachmentDB();
					athDB_N.Copy(athDB);
					athDB_N.setFK_MapData(dtl.getNo());
					athDB_N.setRefPKVal(String.valueOf(geDtl.getOID()));
					athDB_N.setFK_FrmAttachment(dtl.getNo() + "_AthM");
					athDB_N.setUploadGUID("");
					athDB_N.setFID(this.getHisWork().getFID());

					// 生成新的GUID.
					athDB_N.setMyPK(BP.DA.DBAccess.GenerGUID());
					athDB_N.Insert();
				}

			}

			/// #endregion 还要处理附件的copy 汇总.
			break;
		}

		/// #endregion 汇总明细表.

		/// #region 复制附件。
		FrmAttachments aths = ndOfHeLiu.getHisWork().getHisFrmAttachments(); // new
																				// FrmAttachments("ND"
																				// +
																				// this.HisNode.NodeID);
		if (aths.size() == 0) {
			return;
		}
		for (FrmAttachment ath : aths.ToJavaList()) {
			if (ath.getIsHeLiuHuiZong() == false) {
				continue; // 如果是合流的汇总的多附件数据。
			}

			// 取出来所有的上个节点的数据集合.
			FrmAttachments athSLs = this.getHisWork().getHisFrmAttachments();
			if (athSLs.size() == 0) {
				break; // 子线程上没有附件组件.
			}

			// 求子线程的汇总附件集合 (处理如果子线程上有多个附件，其中一部分附件需要汇总另外一部分不需要汇总的模式)
			String strs = "";
			for (FrmAttachment item : athSLs.ToJavaList()) {
				if (item.getIsToHeLiuHZ() == true) {
					strs += "," + item.getMyPK() + ",";
				}
			}

			// 如果没有找到，并且附件集合只有1个，就设置他为子线程的汇总附件，可能是设计人员忘记了设计.
			if (strs.equals("") && athSLs.size() == 1) {
				FrmAttachment athT = (FrmAttachment) ((athSLs.get(0) instanceof FrmAttachment) ? athSLs.get(0) : null);
				athT.setIsToHeLiuHZ(true);
				athT.Update();
				strs = "," + athT.getMyPK() + ",";
			}

			// 没有找到要执行的附件.
			if (strs.equals("")) {
				break;
			}

			// 取出来所有的上个节点的数据集合.
			FrmAttachmentDBs athDBs = new FrmAttachmentDBs();
			athDBs.Retrieve(FrmAttachmentDBAttr.FK_MapData, this.getHisWork().NodeFrmID, FrmAttachmentDBAttr.RefPKVal,
					this.getHisWork().getOID());

			if (athDBs.size() == 0) {
				break; // 子线程没有上传附件.
			}

			// 说明当前节点有附件数据
			for (FrmAttachmentDB athDB : athDBs.ToJavaList()) {
				if (strs.contains("," + athDB.getFK_FrmAttachment() + ",") == false) {
					continue;
				}

				FrmAttachmentDB athDB_N = new FrmAttachmentDB();
				athDB_N.Copy(athDB);
				athDB_N.setFK_MapData("ND" + ndOfHeLiu.getNodeID());
				athDB_N.setRefPKVal((new Long(this.getHisWork().getFID())).toString());
				athDB_N.setFK_FrmAttachment(ath.getMyPK());
				athDB_N.setUploadGUID("");

				// 生成新的GUID.
				athDB_N.setMyPK(BP.DA.DBAccess.GenerGUID());
				athDB_N.Insert();
			}
			break;
		}

		/// #endregion 复制附件。

		/// #region 复制Ele。
		FrmEleDBs eleDBs = new FrmEleDBs("ND" + this.getHisNode().getNodeID(), (new Long(this.getWorkID())).toString());
		if (eleDBs.size() > 0) {
			// 说明当前节点有附件数据
			int idx = 0;
			for (FrmEleDB eleDB : eleDBs.ToJavaList()) {
				idx++;
				FrmEleDB eleDB_N = new FrmEleDB();
				eleDB_N.Copy(eleDB);
				eleDB_N.setFK_MapData("ND" + ndOfHeLiu.getNodeID());
				eleDB_N.setMyPK(
						eleDB_N.getMyPK().replace("ND" + this.getHisNode().getNodeID(), "ND" + ndOfHeLiu.getNodeID()));
				eleDB_N.setRefPKVal((new Long(this.getHisWork().getFID())).toString());
				eleDB_N.Save();
			}
		}

		/// #endregion 复制Ele。

	}

	/**
	 * 子线程节点
	 * 
	 */
	private String _SpanSubTheadNodes = null;

	/**
	 * 获取分流与合流之间的子线程节点集合.
	 * 
	 * @param toNode
	 * @return
	 * @throws Exception 
	 */
	private String SpanSubTheadNodes(Node toHLNode) throws Exception {
		_SpanSubTheadNodes = "";
		SpanSubTheadNodes_DiGui(toHLNode.getFromNodes());
		if (_SpanSubTheadNodes.equals("")) {
			throw new RuntimeException("获取分合流之间的子线程节点集合为空，请检查流程设计，在分合流之间的节点必须设置为子线程节点。");
		}
		_SpanSubTheadNodes = _SpanSubTheadNodes.substring(1);
		return _SpanSubTheadNodes;

	}

	private void SpanSubTheadNodes_DiGui(Nodes subNDs) throws Exception {
		for (Node nd : subNDs.ToJavaList()) {
			if (nd.getHisNodeWorkType() == NodeWorkType.SubThreadWork) {
				// 判断是否已经包含，不然可能死循环
				if (_SpanSubTheadNodes.contains("," + nd.getNodeID())) {
					continue;
				}

				_SpanSubTheadNodes += "," + nd.getNodeID();
				SpanSubTheadNodes_DiGui(nd.getFromNodes());
			}
		}
	}

	/// #endregion

	/**
	 * 工作
	 * 
	 */
	private Work _HisWork = null;

	/**
	 * 工作
	 * 
	 */
	public final Work getHisWork() {
		return this._HisWork;
	}

	/**
	 * 节点
	 * 
	 */
	private Node _HisNode = null;

	/**
	 * 节点
	 * 
	 */
	public final Node getHisNode() {
		return this._HisNode;
	}

	private RememberMe HisRememberMe = null;

	public final RememberMe GetHisRememberMe(Node nd) throws Exception {
		if (HisRememberMe == null || HisRememberMe.getFK_Node() != nd.getNodeID()) {
			HisRememberMe = new RememberMe();
			HisRememberMe.setFK_Emp(this.getExecer());
			HisRememberMe.setFK_Node(nd.getNodeID());
			HisRememberMe.RetrieveFromDBSources();
		}
		return this.HisRememberMe;
	}

	private WorkFlow _HisWorkFlow = null;

	/**
	 * 工作流程
	 * @throws Exception 
	 * 
	 */
	public final WorkFlow getHisWorkFlow() throws Exception {
		if (_HisWorkFlow == null) {
			_HisWorkFlow = new WorkFlow(this.getHisNode().getHisFlow(), this.getHisWork().getOID(),
					this.getHisWork().getFID());
		}
		return _HisWorkFlow;
	}

	/**
	 * 当前节点的工作是不是完成。
	 * @throws Exception 
	 * 
	 */
	public final boolean getIsComplete() throws Exception {
		if (this.getHisGenerWorkFlow().getWFState() == WFState.Complete) {
			return true;
		} else {
			return false;
		}
	}

	public TransferCustom _transferCustom = null;

	public final TodolistModel getTodolistModel() throws Exception {
		// 如果当前的节点是按照ccbpm定义的方式运行的，就返回当前节点的多人待办模式，否则就返回自定义的模式。
		if (this.getHisGenerWorkFlow().getTransferCustomType() == TransferCustomType.ByCCBPMDefine) {
			return this.getHisNode().getTodolistModel();
		}
		return this.getHisGenerWorkFlow().getTodolistModel();
	}

	/// #endregion

	/**
	 * 建立一个工作节点事例.
	 * 
	 * @param workId
	 *            工作ID
	 * @param nodeId
	 *            节点ID
	 * @throws Exception 
	 */
	public WorkNode(long workId, int nodeId) throws Exception {
		this.setWorkID(workId);
		Node nd = new Node(nodeId);
		Work wk = nd.getHisWork();
		wk.setOID(workId);
		int i = wk.RetrieveFromDBSources();
		if (i == 0) {
			this.rptGe = nd.getHisFlow().getHisGERpt();
			if (wk.getFID() != 0) {
				this.rptGe.setOID(wk.getFID());
			} else {
				this.rptGe.setOID(this.getWorkID());
			}

			this.rptGe.RetrieveFromDBSources();
			wk.setRow(rptGe.getRow());
		}
		this._HisWork = wk;
		this._HisNode = nd;
	}

	public java.util.Hashtable SendHTOfTemp = null;
	public String title = null;

	/**
	 * 建立一个工作节点事例
	 * 
	 * @param wk
	 *            工作
	 * @param nd
	 *            节点
	 */
	public WorkNode(Work wk, Node nd) {
		this.setWorkID(wk.getOID());
		this._HisWork = wk;
		this._HisNode = nd;
	}

	/// #endregion

	/// #region 运算属性
	private void Repair() {
	}

	public final WorkNode GetPreviousWorkNode_FHL(long workid) throws Exception {
		Nodes nds = this.getHisNode().getFromNodes();
		for (Node nd : nds.ToJavaList()) {
			if (nd.getHisRunModel() == RunModel.SubThread) {
				Work wk = nd.getHisWork();
				wk.setOID(workid);
				if (wk.RetrieveFromDBSources() != 0) {
					WorkNode wn = new WorkNode(wk, nd);
					return wn;
				}
			}
		}
		return null;
	}

	public final WorkNodes GetPreviousWorkNodes_FHL() throws Exception {
		// 如果没有找到转向他的节点,就返回,当前的工作.
		if (this.getHisNode().getIsStartNode()) {
			throw new RuntimeException("@此节点是开始节点,没有上一步工作"); // 此节点是开始节点,没有上一步工作.
		}

		if (this.getHisNode().getHisNodeWorkType() == NodeWorkType.WorkHL
				|| this.getHisNode().getHisNodeWorkType() == NodeWorkType.WorkFHL) {
		} else {
			throw new RuntimeException("@当前工作节 - 非是分合流节点。");
		}

		WorkNodes wns = new WorkNodes();
		Nodes nds = this.getHisNode().getFromNodes();
		for (Node nd : nds.ToJavaList()) {
			Works wks = (Works) nd.getHisWorks();
			wks.Retrieve(WorkAttr.FID, this.getHisWork().getOID());

			if (wks.size() == 0) {
				continue;
			}

			for (Work wk : wks.ToJavaList()) {
				WorkNode wn = new WorkNode(wk, nd);
				wns.Add(wn);
			}
		}
		return wns;
	}

	/**
	 * 得当他的上一步工作 1, 从当前的找到他的上一步工作的节点集合. 如果没有找到转向他的节点,就返回,当前的工作.
	 * 
	 * @return 得当他的上一步工作
	 * @throws Exception 
	 */
	public final WorkNode GetPreviousWorkNode() throws Exception {
		// 如果没有找到转向他的节点,就返回,当前的工作.
		if (this.getHisNode().getIsStartNode()) {
			throw new RuntimeException("@" + String.format("此节点是开始节点,没有上一步工作")); // 此节点是开始节点,没有上一步工作.
		}

		String sql = "";

		// 根据当前节点获取上一个节点，不用管那个人发送的
		sql = "SELECT NDFrom FROM ND" + Integer.parseInt(this.getHisNode().getFK_Flow()) + "Track WHERE WorkID="
				+ this.getWorkID() + " AND NDTo='" + this.getHisNode().getNodeID()
				+ "' AND ActionType=1 ORDER BY RDT DESC";
		int nodeid = DBAccess.RunSQLReturnValInt(sql, 0);
		if (nodeid == 0) {
			switch (this.getHisNode().getHisRunModel()) {
			case HL:
			case FHL:
				sql = "SELECT NDFrom FROM ND" + Integer.parseInt(this.getHisNode().getFK_Flow()) + "Track WHERE WorkID="
						+ this.getWorkID() + " ORDER BY RDT DESC";
				break;
			case SubThread:
				sql = "SELECT NDFrom FROM ND" + Integer.parseInt(this.getHisNode().getFK_Flow()) + "Track WHERE WorkID="
						+ this.getWorkID() + " AND NDTo=" + this.getHisNode().getNodeID() + " " + " AND ActionType="
						+ ActionType.SubThreadForward.getValue() + " ORDER BY RDT DESC";
				if (DBAccess.RunSQLReturnCOUNT(sql) == 0) {
					sql = "SELECT NDFrom FROM ND" + Integer.parseInt(this.getHisNode().getFK_Flow())
							+ "Track WHERE WorkID=" + this.getHisWork().getFID() + " AND NDTo="
							+ this.getHisNode().getNodeID() + " " + " AND ActionType="
							+ ActionType.SubThreadForward.getValue() + " ORDER BY RDT DESC";
				}

				break;
			default:
				break;
			}
			nodeid = DBAccess.RunSQLReturnValInt(sql, 0);
		}
		if (nodeid == 0) {
			throw new RuntimeException("@错误，没有找到上一步节点。" + sql);
		}

		Node nd = new Node(nodeid);
		Work wk = nd.getHisWork();
		wk.setOID(this.getWorkID());
		wk.RetrieveFromDBSources();

		WorkNode wn = new WorkNode(wk, nd);
		return wn;

		// WorkNodes wns = new WorkNodes();
		// Nodes nds = this.HisNode.FromNodes;
		// foreach (Node nd in nds)
		// {
		// switch (this.HisNode.HisNodeWorkType)
		// {
		// case NodeWorkType.WorkHL: /* 如果是合流 */
		// if (this.IsSubFlowWorkNode == false)
		// {
		// /* 如果不是线程 */
		// Node pnd = nd.HisPriFLNode;
		// if (pnd == null)
		// throw new Exception("@没有取道它的上一步骤的分流节点，请确认设计是否错误？");

		// Work wk1 = (Work)pnd.HisWorks.GetNewEntity;
		// wk1.OID = this.HisWork.OID;
		// if (wk1.RetrieveFromDBSources() == 0)
		// continue;
		// WorkNode wn11 = new WorkNode(wk1, pnd);
		// return wn11;
		// break;
		// }
		// break;
		// default:
		// break;
		// }

		// Work wk = (Work)nd.HisWorks.GetNewEntity;
		// wk.OID = this.HisWork.OID;
		// if (wk.RetrieveFromDBSources() == 0)
		// continue;

		// string table = "ND" + int.Parse(this.HisNode.FK_Flow) + "Track";
		// string actionSQL = "SELECT EmpFrom,EmpFromT,RDT FROM " + table + "
		// WHERE WorkID=" + this.WorkID + " AND NDFrom=" + nd.NodeID + " AND
		// ActionType=" + (int)ActionType.Forward;
		// DataTable dt = DBAccess.RunSQLReturnTable(actionSQL);
		// if (dt.Rows.Count == 0)
		// continue;

		// wk.Rec = dt.Rows[0]["EmpFrom"].ToString();
		// wk.RecText = dt.Rows[0]["EmpFromT"].ToString();
		// wk.SetValByKey("RDT", dt.Rows[0]["RDT"].ToString());

		// WorkNode wn = new WorkNode(wk, nd);
		// wns.Add(wn);
		// }
		// switch (wns.Count)
		// {
		// case 0:
		// throw new
		// Exception("没有找到他的上一步工作，系统错误，请通知管理员来处理，请上让上一步处理人撤消发送、或者用本区县管理员用户登陆=》待办工作=》流程查询=》在关键字中输入Workid其它条件选择全部，查询到该流程删除它。
		// @WorkID=" + this.WorkID);
		// case 1:
		// return (WorkNode)wns[0];
		// default:
		// break;
		// }
		// Node nd1 = wns[0].HisNode;
		// Node nd2 = wns[1].HisNode;
		// if (nd1.FromNodes.Contains(NodeAttr.NodeID, nd2.NodeID))
		// {
		// return wns[0];
		// }
		// else
		// {
		// return wns[1];
		// }
	}

	/// #endregion
	public static String ischongfu(String sts) {
		String[] string = sts.split("@");
		List<String> list = new ArrayList<String>();
		String st = "";
		for (int i = 0; i < string.length; i++) {
			if (!list.contains(string[i])) {
				list.add(string[i]);
				st += string[i] + "@";
			}
		}
		return st;
	}
}