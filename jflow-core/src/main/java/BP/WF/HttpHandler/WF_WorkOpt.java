package BP.WF.HttpHandler;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.protocol.HttpContext;
import BP.DA.AtPara;
import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.DA.Log;
import BP.DA.Paras;
import BP.En.Attr;
import BP.En.Attrs;
import BP.En.Entities;
import BP.En.QueryObject;
import BP.En.UIContralType;
import BP.Port.Emp;
import BP.Sys.FrmAttachment;
import BP.Sys.FrmAttachmentDB;
import BP.Sys.FrmAttachmentDBAttr;
import BP.Sys.FrmAttachmentDBs;
import BP.Sys.FrmAttachments;
import BP.Sys.FrmRB;
import BP.Sys.FrmRBAttr;
import BP.Sys.FrmRBs;
import BP.Sys.FrmType;
import BP.Sys.GEDtls;
import BP.Sys.GEEntity;
import BP.Sys.GroupFields;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.MapDatas;
import BP.Sys.MapDtl;
import BP.Sys.MapDtls;
import BP.Sys.SysEnum;
import BP.Sys.SysEnums;
import BP.Sys.SystemConfig;
import BP.WF.ActionType;
import BP.WF.CancelRole;
import BP.WF.DeliveryWay;
import BP.WF.Dev2Interface;
import BP.WF.Flow;
import BP.WF.FullSA;
import BP.WF.GenerWorkFlow;
import BP.WF.GenerWorkerList;
import BP.WF.GenerWorkerListAttr;
import BP.WF.GenerWorkerLists;
import BP.WF.HuiQianTaskSta;
import BP.WF.Node;
import BP.WF.NodeFormType;
import BP.WF.Nodes;
import BP.WF.RunModel;
import BP.WF.SendReturnObjs;
import BP.WF.TodolistModel;
import BP.WF.Track;
import BP.WF.TrackAttr;
import BP.WF.Tracks;
import BP.WF.TransferCustom;
import BP.WF.TransferCustomType;
import BP.WF.TransferCustoms;
import BP.WF.WFState;
import BP.WF.Work;
import BP.WF.WorkCheck;
import BP.WF.WorkNode;
import BP.WF.Data.Bill;
import BP.WF.HttpHandler.Base.WebContralBase;
import BP.WF.Rpt.MakeForm2Html;
import BP.WF.Template.BillFileType;
import BP.WF.Template.BillOpenModel;
import BP.WF.Template.BillTemplate;
import BP.WF.Template.BillTemplateAttr;
import BP.WF.Template.BillTemplates;
import BP.WF.Template.BtnLab;
import BP.WF.Template.CCSta;
import BP.WF.Template.FWCOrderModel;
import BP.WF.Template.FWCType;
import BP.WF.Template.FlowExt;
import BP.WF.Template.FrmNode;
import BP.WF.Template.FrmWorkCheck;
import BP.WF.Template.FrmWorkCheckSta;
import BP.WF.Template.FrmWorkChecks;
import BP.WF.Template.NodeAttr;
import BP.WF.Template.SelectAccper;
import BP.WF.Template.SelectAccperAttr;
import BP.WF.Template.SelectAccpers;
import BP.WF.Template.Selector;
import BP.WF.Template.SelectorModel;
import BP.WF.Template.TemplateFileModel;
import BP.WF.Template.WhoIsPK;
import BP.Web.WebUser;

public class WF_WorkOpt extends WebContralBase {
	
    

	/**
	 * 构造函数
	 */
	public WF_WorkOpt() {

	}

	/**
	 * 初始化函数
	 * 
	 * @param mycontext
	 */
	public WF_WorkOpt(HttpContext mycontext) {
		this.context = mycontext;
	}

	public final String SelectEmps_Init() throws Exception {
		String fk_dept = this.getFK_Dept();

		if (DataType.IsNullOrEmpty(fk_dept) == true || fk_dept.equals("undefined") == true) {
			fk_dept = BP.Web.WebUser.getFK_Dept();
		}

		DataSet ds = new DataSet();

		String sql = "SELECT No,Name,ParentNo FROM Port_Dept WHERE No='" + fk_dept + "' OR ParentNo='" + fk_dept + "' ";
		DataTable dtDept = BP.DA.DBAccess.RunSQLReturnTable(sql);

		if (dtDept.Rows.size() == 0) {
			fk_dept = BP.Web.WebUser.getFK_Dept();

			sql = "SELECT No,Name,ParentNo FROM Port_Dept WHERE No='" + fk_dept + "' OR ParentNo='" + fk_dept + "' ";
			dtDept = BP.DA.DBAccess.RunSQLReturnTable(sql);

			// return "err@部门编号错误:"+fk_dept;
		}

		dtDept.TableName = "Depts";
		ds.Tables.add(dtDept);

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
			dtDept.Columns.get(0).ColumnName = "No";
			dtDept.Columns.get(1).ColumnName = "Name";
			dtDept.Columns.get(2).ColumnName = "ParentNo";
		}

		// sql = "SELECT No,Name, FK_Dept FROM Port_Emp WHERE FK_Dept='" +
		// fk_dept + "' ";
		sql = "SELECT A.No,A.Name, '" + fk_dept
				+ "' as FK_Dept FROM Port_Emp A LEFT JOIN Port_DeptEmp B  ON A.No=B.FK_Emp WHERE A.FK_Dept='" + fk_dept
				+ "' OR B.FK_Dept='" + fk_dept + "'";
		
        sql += " ORDER BY A.Idx ";


		DataTable dtEmps = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dtEmps.TableName = "Emps";
		ds.Tables.add(dtEmps);
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
			dtEmps.Columns.get(0).ColumnName = "No";
			dtEmps.Columns.get(1).ColumnName = "Name";
			dtEmps.Columns.get(2).ColumnName = "FK_Dept";
		}

		// 转化为 json
		return BP.Tools.Json.ToJson(ds);
	}

	public final String WorkCheck_Init() throws Exception {
		if (WebUser.getNo() == null)
			return "err@登录信息丢失,请重新登录.";

		// #region 定义变量.
		FrmWorkCheck wcDesc = new FrmWorkCheck(this.getFK_Node());
		FrmWorkCheck frmWorkCheck = null;
		FrmAttachmentDBs athDBs = null;
		Nodes nds = new Nodes(this.getFK_Flow());
		FrmWorkChecks fwcs = new FrmWorkChecks();
		Node nd = null;
		WorkCheck wc = null;
		Tracks tks = null;
		Track tkDoc = null;
		String nodes = ""; // 可以审核的节点.
		Boolean isCanDo = false;
		Boolean isExitTb_doc = true;
		DataSet ds = new DataSet();
		DataRow row = null;

		// 是不是只读?
		Boolean isReadonly = false;
		if (this.GetRequestVal("IsReadonly") != null && this.GetRequestVal("IsReadonly").equals("1"))
			isReadonly = true;

		DataTable nodeEmps = new DataTable();
		// Dictionary<int, DataTable> nodeEmps = new Dictionary<int,
		// DataTable>(); //节点id，接收人列表
		FrmWorkCheck fwc = null;
		DataTable dt = null;
		int idx = 0;
		int noneEmpIdx = 0;

		fwcs.Retrieve(NodeAttr.FK_Flow, this.getFK_Flow(), NodeAttr.Step);
		ds.Tables.add(wcDesc.ToDataTableField("wcDesc")); // 当前的节点审核组件定义，放入ds.

		DataTable tkDt = new DataTable("Tracks");
		tkDt.Columns.Add("NodeID", Integer.class);
		tkDt.Columns.Add("NodeName", String.class);
		tkDt.Columns.Add("Msg", String.class);
		tkDt.Columns.Add("EmpFrom", String.class);
		tkDt.Columns.Add("EmpFromT", String.class);
		tkDt.Columns.Add("RDT", String.class);
		tkDt.Columns.Add("IsDoc", Boolean.class);
		tkDt.Columns.Add("ParentNode", Integer.class);
		tkDt.Columns.Add("T_NodeIndex", Integer.class); // 节点排列顺序，用于后面的排序
		tkDt.Columns.Add("T_CheckIndex", Integer.class); // 审核人显示顺序，用于后面的排序
		tkDt.Columns.Add("ActionType", Integer.class);

		// 流程附件.
		DataTable athDt = new DataTable("Aths");
		athDt.Columns.Add("NodeID", Integer.class);
		athDt.Columns.Add("MyPK", String.class);
		athDt.Columns.Add("FK_FrmAttachment", String.class);
		athDt.Columns.Add("FK_MapData", String.class);
		athDt.Columns.Add("FileName", String.class);
		athDt.Columns.Add("FileExts", String.class);
		athDt.Columns.Add("CanDelete", Boolean.class);
		//当前节点的流程数据
		FrmAttachmentDBs frmathdbs = new FrmAttachmentDBs();
		frmathdbs.Retrieve(FrmAttachmentDBAttr.FK_FrmAttachment, "ND" + this.getFK_Node() + "_FrmWorkCheck", FrmAttachmentDBAttr.RefPKVal, this.getWorkID(),FrmAttachmentDBAttr.Rec,WebUser.getNo(), FrmAttachmentDBAttr.RDT);
		for(FrmAttachmentDB athDB : frmathdbs.ToJavaList())
		{
			row = athDt.NewRow();
			row.setValue("NodeID",this.getFK_Node());
			row.setValue("MyPK",athDB.getMyPK());
			row.setValue("FK_FrmAttachment",athDB.getFK_FrmAttachment());
			row.setValue("FK_MapData",athDB.getFK_MapData());
			row.setValue("FileName",athDB.getFileName());
			row.setValue("FileExts",athDB.getFileExts());
			row.setValue("CanDelete",  athDB.getRec().equals(WebUser.getNo()));
			athDt.Rows.add(row);
		}
		ds.Tables.add(athDt);

		if (this.getFID() != 0)
			wc = new WorkCheck(this.getFK_Flow(), this.getFK_Node(), this.getFID(), 0);
		else
			wc = new WorkCheck(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), this.getFID());

		// 是否只读？
		if (isReadonly == true)
			isCanDo = false;
		else
			isCanDo = BP.WF.Dev2Interface.Flow_IsCanDoCurrentWork(this.getWorkID(), WebUser.getNo());

		// 如果是查看状态, 为了屏蔽掉正在审批的节点, 在查看审批意见中.
		Boolean isShowCurrNodeInfo = true;
		GenerWorkFlow gwf = new GenerWorkFlow();
		if (this.getWorkID() != 0) {
			gwf.setWorkID(this.getWorkID());
			gwf.Retrieve();
		}

		if (isCanDo == false && isReadonly == true) {
			if (gwf.getWFState() == WFState.Runing && gwf.getFK_Node() == this.getFK_Node())
				isShowCurrNodeInfo = false;
		}

		/*
		 * 获得当前节点已经审核通过的人员. 比如：多人处理规则中的已经审核同意的人员，会签人员,组合成成一个字符串。 格式为:
		 * ,zhangsan,lisi, 用于处理在审核列表中屏蔽临时的保存的审核信息.
		 */
		String checkerPassed = ",";
		if (gwf.getWFState() != WFState.Complete) {
			String sql = "SELECT FK_Emp FROM WF_Generworkerlist where workid=" + this.getWorkID()
					+ " AND IsPass=1 AND FK_Node=" + this.getFK_Node();
			DataTable checkerPassedDt = DBAccess.RunSQLReturnTable(sql);
			for (DataRow dr : checkerPassedDt.Rows) {
				checkerPassed += dr.getValue("FK_Emp") + ",";
			}
		}

		// #endregion 定义变量.

		// #region 判断是否显示 - 历史审核信息显示
		Boolean isDoc = false;
		if (wcDesc.getFWCListEnable() == true) {
			tks = wc.getHisWorkChecks();

			// 已走过节点
			int empIdx = 0;
			int lastNodeId = 0;
			for (BP.WF.Track tk : tks.ToJavaList()) {
				if (tk.getHisActionType() == ActionType.FlowBBS)
					continue;

				if (lastNodeId == 0)
					lastNodeId = tk.getNDFrom();

				if (lastNodeId != tk.getNDFrom()) {
					idx++;
					lastNodeId = tk.getNDFrom();
				}

				// wanning 这个地方没有翻译.
				tk.getRow().put("T_NodeIndex", idx);

				nd = (Node) nds.GetEntityByKey(tk.getNDFrom());
				if (nd == null)
					continue;

				fwc = (FrmWorkCheck) fwcs.GetEntityByKey(tk.getNDFrom());
				// 求出主键
				long pkVal = this.getWorkID();
				if (nd.getHisRunModel() == RunModel.SubThread)
					pkVal = this.getFID();

				// 排序，结合人员表Idx进行排序
				if (fwc.getFWCOrderModel() == FWCOrderModel.SqlAccepter) {

					tk.getRow().put("T_CheckIndex", DBAccess.RunSQLReturnValInt(
							String.format("SELECT Idx FROM Port_Emp WHERE No='{0}'",
									tk.getEmpFrom()), 0));

					noneEmpIdx++;
				} else {
					tk.getRow().put("T_CheckIndex",noneEmpIdx++) ;
				}

				if (tk.getHisActionType() == ActionType.WorkCheck
						|| tk.getHisActionType() == ActionType.StartChildenFlow) {
					if (nodes.contains(tk.getNDFrom() + ",") == false)
						nodes += tk.getNDFrom() + ",";
				} else if(tk.getHisActionType() == ActionType.Return
						|| tk.getHisActionType() == ActionType.ReturnAndBackWay) {
					if (wcDesc.getFWCIsShowReturnMsg() == true && tk.getNDTo() == this.getFK_Node())
					{
						if (nodes.contains(tk.getNDFrom() + ",") == false)
							nodes += tk.getNDFrom() + ",";
					}
					continue;
				}
				continue;


			}

			for (Track tk : tks.ToJavaList()) {
				if (nodes.contains(tk.getNDFrom() + ",") == false)
					continue;
				if (tk.getHisActionType() != ActionType.WorkCheck
						&& tk.getHisActionType() != ActionType.StartChildenFlow
						&& tk.getHisActionType() != ActionType.Return)
					continue;
				//退回
				if(tk.getHisActionType() == ActionType.Return){
					//1.不显示退回意见 2.显示退回意见但是不是退回给本节点的意见
					if(wcDesc.getFWCIsShowReturnMsg() == false ||(wcDesc.getFWCIsShowReturnMsg() == true &&tk.getNDTo() != this.getFK_Node() ))
						continue;
				}

				if (tk.getHisActionType() != ActionType.WorkCheck
						&& tk.getHisActionType() != ActionType.StartChildenFlow
						&& tk.getHisActionType()!=ActionType.Return
						&& tk.getHisActionType()!=ActionType.ReturnAndBackWay)
					continue;

				// 如果是当前的节点. 当前人员可以处理, 已经审批通过的人员.
				if (tk.getNDFrom() == this.getFK_Node() && isCanDo == true && tk.getEmpFrom() != WebUser.getNo()
						&& checkerPassed.contains("," + tk.getEmpFrom() + ",") == false )
					continue;

				if (tk.getNDFrom() == this.getFK_Node() && gwf.getHuiQianTaskSta() != HuiQianTaskSta.None) {
					// 判断会签, 去掉正在审批的节点.
					if (tk.getNDFrom() == this.getFK_Node() && isShowCurrNodeInfo == false)
						continue;
				}

				// 如果是多人处理，就让其显示已经审核过的意见.
				if (tk.getNDFrom() == this.getFK_Node() && checkerPassed.indexOf("," + tk.getEmpFrom() + ",") < 0 && gwf.getWFState() != WFState.Complete) {
					continue;
					// 如果当前人，没有审核完成,就不显示.
					// 判断会签, 去掉正在审批的节点.
					// if (tk.NDFrom == this.FK_Node)
					// continue;
				}

				row = tkDt.NewRow();
				row.setValue("NodeID", tk.getNDFrom());



				row.setValue("NodeName", tk.getNDFromT());
				isDoc = false;
				// zhoupeng 增加了判断，在会签的时候最后会签人发送前不能填写意见.
				if (tk.getNDFrom() == this.getFK_Node() && tk.getEmpFrom() == BP.Web.WebUser.getNo() && isCanDo
						&& isDoc == false)
					isDoc = true;

				row.setValue("IsDoc", isDoc);
				row.setValue("ParentNode", 0);

				row.setValue("RDT", tk.getRDT());

				row.setValue("T_NodeIndex", 0);
				row.setValue("T_CheckIndex", 0);

				if (isReadonly == false && tk.getEmpFrom() == WebUser.getNo() && this.getFK_Node() == tk.getNDFrom()
						&& isExitTb_doc) {
					Boolean isLast = true;
					for (Track tk1 : tks.ToJavaList()) {
						if (tk1.getHisActionType() == tk.getHisActionType() && tk1.getNDFrom() == tk.getNDFrom()
								&& tk1.getRDT().compareTo(tk.getRDT()) > 0) {
							isLast = false;
							break;
						}
					}

					if (isLast && isDoc == false && gwf.getWFState() != WFState.Complete) {
						isExitTb_doc = false;
						row.setValue("IsDoc", true);
						isDoc = true;

						row.setValue("Msg", Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(),
								this.getFK_Node(), wcDesc.getFWCDefInfo()));

						tkDoc = tk;

					} else {
						row.setValue("Msg", tk.getMsgHtml());
					}
				} else {
					row.setValue("Msg", tk.getMsgHtml());
				}

				row.setValue("EmpFrom", tk.getEmpFrom());
				row.setValue("EmpFromT", tk.getEmpFromT());
				row.setValue("ActionType",tk.getHisActionType().getValue());

				tkDt.Rows.add(row);

				// #region //审核组件附件数据
				athDBs = new FrmAttachmentDBs();
				QueryObject obj_Ath = new QueryObject(athDBs);
				obj_Ath.AddWhere(FrmAttachmentDBAttr.FK_FrmAttachment,"ND"+ tk.getNDFrom() + "_FrmWorkCheck");
				obj_Ath.addAnd();
				obj_Ath.AddWhere(FrmAttachmentDBAttr.RefPKVal, this.getWorkID());
				obj_Ath.addAnd();
				obj_Ath.AddWhere(FrmAttachmentDBAttr.Rec, tk.getEmpFrom());
				obj_Ath.addOrderBy(FrmAttachmentDBAttr.RDT);
				obj_Ath.DoQuery();

				for (FrmAttachmentDB athDB : athDBs.ToJavaList()) {
					row = athDt.NewRow();

					row.setValue("NodeID",tk.getNDFrom());
					row.setValue("MyPK",athDB.getMyPK());
					row.setValue("FK_FrmAttachment",athDB.getFK_FrmAttachment());
					row.setValue("FK_MapData",athDB.getFK_MapData());
					row.setValue("FileName",athDB.getFileName());
					row.setValue("FileExts",athDB.getFileExts());
					row.setValue("CanDelete", athDB.getFK_MapData() ==
							String.valueOf(this.getFK_Node()) && athDB.getRec().equals(WebUser.getNo()) &&
							isReadonly == false);
					athDt.Rows.add(row);


				}
				// #endregion

				// #region //子流程的审核组件数据
				if (tk.getFID() != 0 && tk.getHisActionType() == ActionType.StartChildenFlow
						&& tkDt.Select("ParentNode=" + tk.getNDFrom()).length == 0) {
					String[] paras = tk.getTag().split("@");
					String[] p1 = paras[1].split("=");
					String fk_flow = p1[1]; // 子流程编号

					String[] p2 = paras[2].split("=");
					String workId = p2[1]; // 子流程ID.
					int biaoji = 0;

					WorkCheck subwc = new WorkCheck(fk_flow, Integer.parseInt(fk_flow + "01"), Long.parseLong(workId),
							0);

					Tracks subtks = subwc.getHisWorkChecks();
					// 取出来子流程的所有的节点。
					Nodes subNds = new Nodes(fk_flow);
					for (Node item : subNds.ToJavaList()) // 主要按顺序显示
					{
						for (Track mysubtk : subtks.ToJavaList()) {
							if (item.getNodeID() != mysubtk.getNDFrom())
								continue;

							/* 输出该子流程的审核信息，应该考虑子流程的子流程信息, 就不考虑那样复杂了. */
							if (mysubtk.getHisActionType() == ActionType.WorkCheck) {
								// 发起多个子流程时，发起人只显示一次
								if (mysubtk.getNDFrom() == Integer.parseInt(fk_flow + "01") && biaoji == 1)
									continue;

								row = tkDt.NewRow();
								row.setValue("NodeID", mysubtk.getNDFrom());
								row.setValue("NodeName", String.format("(子流程){0}", mysubtk.getNDFromT()));
								row.setValue("Msg", mysubtk.getMsgHtml());
								row.setValue("EmpFrom", mysubtk.getEmpFrom());
								row.setValue("EmpFromT", mysubtk.getEmpFromT());
								row.setValue("RDT", mysubtk.getRDT());
								row.setValue("IsDoc", false);
								row.setValue("ParentNode", tk.getNDFrom());
								row.setValue("T_NodeIndex", idx++);
								row.setValue("T_CheckIndex", noneEmpIdx++);
								row.setValue("ActionType",mysubtk.getHisActionType().getValue());
								tkDt.Rows.add(row);

								if (mysubtk.getNDFrom() == Integer.parseInt(fk_flow + "01")) {
									biaoji = 1;
								}
							}
						}
					}
				}
				// #endregion

			}

		}
		// #endregion 判断是否显示 - 历史审核信息显示

		// #region 审核意见默认填写

		// 首先判断当前是否有此意见? 如果是退回的该信息已经存在了.
		Boolean isHaveMyInfo = false;
		for (DataRow dr : tkDt.Rows) {
			String fk_node = dr.getValue("NodeID").toString();
			String empFrom = dr.getValue("EmpFrom").toString();
			if (Integer.parseInt(fk_node) == this.getFK_Node() && empFrom == WebUser.getNo())
				isHaveMyInfo = true;
		}

		// 增加默认的审核意见.
		if (isExitTb_doc && wcDesc.getHisFrmWorkCheckSta() == FrmWorkCheckSta.Enable && isCanDo && isReadonly == false
				&& isHaveMyInfo == false) {
			DataRow[] rows = null;
			nd = (Node) nds.GetEntityByKey(this.getFK_Node());
			if (wcDesc.getFWCOrderModel() == FWCOrderModel.SqlAccepter) {
				rows = tkDt.Select("NodeID=" + this.getFK_Node() + " AND Msg='' AND EmpFrom='" + WebUser.getNo() + "'");

				if (rows.length == 0)
					rows = tkDt.Select("NodeID=" + this.getFK_Node() + " AND EmpFrom='" + WebUser.getNo() + "'");

				if (rows.length > 0) {
					row = rows[0];
					row.setValue("IsDoc", true);

					String mymsg = Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(), this.getFK_Node());
					if (mymsg == null)
						mymsg = "";
					//ccflow没有这段
					row.setValue("Msg", mymsg);
					if (mymsg == "")
						row.setValue("RDT", "");

					// 增加默认审核意见
					if (DataType.IsNullOrEmpty(mymsg) && wcDesc.getFWCIsFullInfo())
						row.setValue("Msg", wcDesc.getFWCDefInfo());
				} else {
					row = tkDt.NewRow();
					row.setValue("NodeID", this.getFK_Node());
					row.setValue("NodeName", nd.getFWCNodeName());
					row.setValue("IsDoc", true);
					row.setValue("ParentNode", "0");
					row.setValue("RDT", "");

					row.setValue("Msg",
							Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(), this.getFK_Node()));

					row.setValue("EmpFrom", WebUser.getNo());
					row.setValue("EmpFromT", WebUser.getName());
					row.setValue("T_NodeIndex", ++idx);
					row.setValue("T_CheckIndex", ++noneEmpIdx);
					row.setValue("ActionType",ActionType.Forward.getValue());
					tkDt.Rows.add(row);
				}
			} else {
				row = tkDt.NewRow();
				row.setValue("NodeID", this.getFK_Node());
				row.setValue("NodeName", nd.getFWCNodeName());
				row.setValue("IsDoc", true);
				row.setValue("ParentNode", 0);
				row.setValue("RDT", "");
				row.setValue("Msg", Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(), this.getFK_Node()));
				row.setValue("EmpFrom", WebUser.getNo());
				row.setValue("EmpFromT", WebUser.getName());
				row.setValue("T_NodeIndex", ++idx);
				row.setValue("T_CheckIndex", ++noneEmpIdx);
				row.setValue("ActionType",ActionType.Forward.getValue());
				tkDt.Rows.add(row);
			}
		}
		// 显示有审核组件，但还未审核的节点.  包括退回后的.
		if (tks == null)
			tks = wc.getHisWorkChecks();

		for (FrmWorkCheck item : fwcs.ToJavaList())
		{
			if (item.getFWCIsShowTruck() == false)
				continue;  //不需要显示历史记录.

			//是否已审核.
			Boolean isHave = false;
			for (BP.WF.Track tk : tks.ToJavaList())
			{
				//翻译.
				if (tk.getNDFrom() == this.getFK_Node() && tk.getHisActionType() == ActionType.WorkCheck)
				{
					isHave = true; //已经有了
					break;
				}
			}

			if (isHave == true)
				continue;

			row = tkDt.NewRow();

			row.setValue("NodeID", item.getNodeID());
			Node mynd = (Node)nds.GetEntityByKey(item.getNodeID());
			row.setValue("NodeName", mynd.getFWCNodeName());
			row.setValue("IsDoc", false);
			row.setValue("ParentNode", 0);
			row.setValue("RDT", "");
			row.setValue("Msg", "&nbsp;");
			row.setValue("EmpFrom", "");
			row.setValue("EmpFromT", "");
			row.setValue("T_NodeIndex", ++idx);
			row.setValue("T_CheckIndex", ++noneEmpIdx);

			tkDt.Rows.add(row);
		}
		// 增加空白.

		ds.Tables.add(tkDt);



		//如果有 SignType 列就获得签名信息.
		if (SystemConfig.getCustomerNo() == "TianYe")
		{
			String tTable = "ND" + Integer.valueOf(this.getFK_Flow()) + "Track";
			String sql = "SELECT distinct a.No, a.SignType, a.EleID FROM Port_Emp a, " + tTable + " b WHERE (A.No='" + WebUser.getNo() + "') OR B.ActionType=22 AND a.No=b.EmpFrom AND B.WorkID=" + this.getWorkID();

			DataTable dtTrack = DBAccess.RunSQLReturnTable(sql);
			dtTrack.TableName = "SignType";

			dtTrack.Columns.get("NO").ColumnName = "No";
			dtTrack.Columns.get("SIGNTYPE").ColumnName = "SignType";
			dtTrack.Columns.get("ELEID").ColumnName = "EleID";

			ds.Tables.add(dtTrack);
		}

		String str = BP.Tools.Json.ToJson(ds);
		return str;
	}

	/**
	 * 新版审核组件
	 * @return
	 * @throws Exception
	 */
	public final String WorkCheck_Init2019() throws Exception {
		if (WebUser.getNo() == null)
			return "err@登录信息丢失,请重新登录.";

		// #region 定义变量.
		FrmWorkCheck wcDesc = new FrmWorkCheck(this.getFK_Node());
		FrmWorkCheck frmWorkCheck = null;
		FrmAttachmentDBs athDBs = null;
		Nodes nds = new Nodes(this.getFK_Flow());
		FrmWorkChecks fwcs = new FrmWorkChecks();
		Node nd = null;
		WorkCheck wc = null;
		Tracks tks = null;
		Track tkDoc = null;
		String nodes = ""; // 可以审核的节点.
		Boolean isCanDo = false;
		Boolean isExitTb_doc = true;
		DataSet ds = new DataSet();
		DataRow row = null;

		// 是不是只读?
		Boolean isReadonly = false;
		if (this.GetRequestVal("IsReadonly") != null && this.GetRequestVal("IsReadonly").equals("1"))
			isReadonly = true;

		DataTable nodeEmps = new DataTable();
		FrmWorkCheck fwc = null;
		DataTable dt = null;
		int idx = 0;
		int noneEmpIdx = 0;

		fwcs.Retrieve(NodeAttr.FK_Flow, this.getFK_Flow(), NodeAttr.Step);
		ds.Tables.add(wcDesc.ToDataTableField("wcDesc")); // 当前的节点审核组件定义，放入ds.

		DataTable tkDt = new DataTable("Tracks");
		tkDt.Columns.Add("NodeID", Integer.class);
		tkDt.Columns.Add("NodeName", String.class);
		tkDt.Columns.Add("Msg", String.class);
		tkDt.Columns.Add("EmpFrom", String.class);
		tkDt.Columns.Add("EmpFromT", String.class);
		tkDt.Columns.Add("RDT", String.class);
		tkDt.Columns.Add("IsDoc", Boolean.class);
		tkDt.Columns.Add("ParentNode", Integer.class);
		tkDt.Columns.Add("T_NodeIndex", Integer.class); // 节点排列顺序，用于后面的排序
		tkDt.Columns.Add("T_CheckIndex", Integer.class); // 审核人显示顺序，用于后面的排序
		tkDt.Columns.Add("ActionType", Integer.class);
		
		// 流程附件.
		DataTable athDt = new DataTable("Aths");
		athDt.Columns.Add("NodeID", Integer.class);
		athDt.Columns.Add("MyPK", String.class);
		athDt.Columns.Add("FK_FrmAttachment", String.class);
		athDt.Columns.Add("FK_MapData", String.class);
		athDt.Columns.Add("FileName", String.class);
		athDt.Columns.Add("FileExts", String.class);
		athDt.Columns.Add("CanDelete", Boolean.class);
		 //当前节点的流程数据
        FrmAttachmentDBs frmathdbs = new FrmAttachmentDBs();
        frmathdbs.Retrieve(FrmAttachmentDBAttr.FK_FrmAttachment, "ND" + this.getFK_Node() + "_FrmWorkCheck", FrmAttachmentDBAttr.RefPKVal, this.getWorkID(),FrmAttachmentDBAttr.Rec,WebUser.getNo(), FrmAttachmentDBAttr.RDT);
        for(FrmAttachmentDB athDB : frmathdbs.ToJavaList())
        {	
        	 row = athDt.NewRow();
        	 row.setValue("NodeID",this.getFK_Node());
			 row.setValue("MyPK",athDB.getMyPK());
			 row.setValue("FK_FrmAttachment",athDB.getFK_FrmAttachment());
			 row.setValue("FK_MapData",athDB.getFK_MapData());
			 row.setValue("FileName",athDB.getFileName());
			 row.setValue("FileExts",athDB.getFileExts());
			 row.setValue("CanDelete",  athDB.getRec().equals(WebUser.getNo()));
			 athDt.Rows.add(row);
        }
		ds.Tables.add(athDt);

		if (this.getFID() != 0)
			wc = new WorkCheck(this.getFK_Flow(), this.getFK_Node(), this.getFID(), 0);
		else
			wc = new WorkCheck(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), this.getFID());

		// 是否只读？
		if (isReadonly == true)
			isCanDo = false;
		else
			isCanDo = BP.WF.Dev2Interface.Flow_IsCanDoCurrentWork(this.getWorkID(), WebUser.getNo());

		// 如果是查看状态, 为了屏蔽掉正在审批的节点, 在查看审批意见中.
		Boolean isShowCurrNodeInfo = true;
		GenerWorkFlow gwf = new GenerWorkFlow();
		if (this.getWorkID() != 0) {
			gwf.setWorkID(this.getWorkID());
			gwf.Retrieve();
		}

		if (isCanDo == false && isReadonly == true) {
			if (gwf.getWFState() == WFState.Runing && gwf.getFK_Node() == this.getFK_Node())
				isShowCurrNodeInfo = false;
		}

		/*
		 * 获得当前节点已经审核通过的人员. 比如：多人处理规则中的已经审核同意的人员，会签人员,组合成成一个字符串。 格式为:
		 * ,zhangsan,lisi, 用于处理在审核列表中屏蔽临时的保存的审核信息.
		 */
		String checkerPassed = ",";
		if (gwf.getWFState() != WFState.Complete) {
			String sql = "SELECT FK_Emp FROM WF_Generworkerlist where workid=" + this.getWorkID()
					+ " AND IsPass=1 AND FK_Node=" + this.getFK_Node();
			DataTable checkerPassedDt = DBAccess.RunSQLReturnTable(sql);
			for (DataRow dr : checkerPassedDt.Rows) {
				checkerPassed += dr.getValue("FK_Emp") + ",";
			}
		}

		// #endregion 定义变量.

		// #region 判断是否显示 - 历史审核信息显示
		Boolean isDoc = false;
		if (wcDesc.getFWCListEnable() == true) {
			tks = wc.getHisWorkChecks();

			// 已走过节点
			int empIdx = 0;
			int lastNodeId = 0;
			for (BP.WF.Track tk : tks.ToJavaList()) {
				if (tk.getHisActionType() == ActionType.FlowBBS)
					continue;

				if (lastNodeId == 0)
					lastNodeId = tk.getNDFrom();

				if (lastNodeId != tk.getNDFrom()) {
					idx++;
					lastNodeId = tk.getNDFrom();
				}

				// wanning 这个地方没有翻译.
				tk.getRow().put("T_NodeIndex", idx);

				nd = (Node) nds.GetEntityByKey(tk.getNDFrom());
				if (nd == null)
                    continue;
				
				fwc = (FrmWorkCheck) fwcs.GetEntityByKey(tk.getNDFrom());
				// 求出主键
				long pkVal = this.getWorkID();
				if (nd.getHisRunModel() == RunModel.SubThread)
					pkVal = this.getFID();

				// 排序，结合人员表Idx进行排序
				if (fwc.getFWCOrderModel() == FWCOrderModel.SqlAccepter) {
					
					 tk.getRow().put("T_CheckIndex", DBAccess.RunSQLReturnValInt(
					 String.format("SELECT Idx FROM Port_Emp WHERE No='{0}'",
					 tk.getEmpFrom()), 0));
					 
					noneEmpIdx++;
				} else {
					tk.getRow().put("T_CheckIndex",noneEmpIdx++) ;
				}

				if (tk.getHisActionType() == ActionType.WorkCheck
						|| tk.getHisActionType() == ActionType.StartChildenFlow) {
					if (nodes.contains(tk.getNDFrom() + ",") == false)
						nodes += tk.getNDFrom() + ",";
				} else if(tk.getHisActionType() == ActionType.Return 
						|| tk.getHisActionType() == ActionType.ReturnAndBackWay) {
					if (wcDesc.getFWCIsShowReturnMsg() == true && tk.getNDTo()== this.getFK_Node())
                    {
                        if (nodes.contains(tk.getNDFrom() + ",") == false)
                            nodes += tk.getNDFrom() + ",";
                    }
					continue;
				}
				continue;
				

			}

			for (Track tk : tks.ToJavaList()) {
				if (nodes.contains(tk.getNDFrom() + ",") == false)
					continue;
				//退回
				if(tk.getHisActionType() == ActionType.Return){
					//1.不显示退回意见 2.显示退回意见但是不是退回给本节点的意见
					if(wcDesc.getFWCIsShowReturnMsg() == false ||(wcDesc.getFWCIsShowReturnMsg() == true &&tk.getNDTo() != this.getFK_Node() ))
						continue;
				}

				// 如果是当前的节点. 当前人员可以处理, 已经审批通过的人员.
				if (tk.getNDFrom() == this.getFK_Node() && isCanDo == true && tk.getEmpFrom().equals(WebUser.getNo())==false
						&& checkerPassed.contains("," + tk.getEmpFrom() + ",") == false )
					continue;

				if (tk.getNDFrom() == this.getFK_Node() && gwf.getHuiQianTaskSta() != HuiQianTaskSta.None) {
					// 判断会签, 去掉正在审批的节点.
					if (tk.getNDFrom() == this.getFK_Node() && isShowCurrNodeInfo == false)
						continue;
				}

				//历史审核信息现在存放在流程前进的节点中
				switch (tk.getHisActionType()) {
					case Forward:
					case ForwardAskfor:
					case Start:
					case UnSend:
					case ForwardFL:
					case ForwardHL:
					case TeampUp:
					case Return:
					case StartChildenFlow:
					case FlowOver:
						row = tkDt.NewRow();
						row.setValue("NodeID", tk.getNDFrom());
						row.setValue("NodeName", tk.getNDFromT());
						isDoc = false;
						// zhoupeng 增加了判断，在会签的时候最后会签人发送前不能填写意见.
						if (tk.getNDFrom() == this.getFK_Node() && tk.getEmpFrom() == BP.Web.WebUser.getNo() && isCanDo
								&& isDoc == false)
							isDoc = true;

						row.setValue("IsDoc", isDoc);
						row.setValue("ParentNode", 0);

						row.setValue("RDT", tk.getRDT());

						row.setValue("T_NodeIndex", 0);
						row.setValue("T_CheckIndex", 0);

						/*if (isReadonly == false && tk.getEmpFrom() == WebUser.getNo() && this.getFK_Node() == tk.getNDFrom()
								&& isExitTb_doc) {
							Boolean isLast = true;
							for (Track tk1 : tks.ToJavaList()) {
								if (tk1.getHisActionType() == tk.getHisActionType() && tk1.getNDFrom() == tk.getNDFrom()
										&& tk1.getRDT().compareTo(tk.getRDT()) > 0) {
									isLast = false;
									break;
								}
							}

							if (isLast && isDoc == false && gwf.getWFState() != WFState.Complete) {
								isExitTb_doc = false;
								row.setValue("IsDoc", true);
								isDoc = true;

								row.setValue("Msg", Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(),
										this.getFK_Node(), wcDesc.getFWCDefInfo()));

								tkDoc = tk;

							} else {
								row.setValue("Msg", tk.getMsgHtml());
							}
						} else {
							row.setValue("Msg", tk.getMsgHtml());
						}*/

						if(gwf.getWFState() == WFState.Complete)
						{
							row.setValue("Msg", Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(),
									this.getFK_Node(), wcDesc.getFWCDefInfo()));
						}
						else
						{
							row.setValue("Msg", tk.getMsgHtml());
						}
						row.setValue("EmpFrom", tk.getEmpFrom());
						row.setValue("EmpFromT", tk.getEmpFromT());
						row.setValue("ActionType", tk.getHisActionType().getValue());

						tkDt.Rows.add(row);

						// #region //审核组件附件数据
						athDBs = new FrmAttachmentDBs();
						QueryObject obj_Ath = new QueryObject(athDBs);
						obj_Ath.AddWhere(FrmAttachmentDBAttr.FK_FrmAttachment, "ND" + tk.getNDFrom() + "_FrmWorkCheck");
						obj_Ath.addAnd();
						obj_Ath.AddWhere(FrmAttachmentDBAttr.RefPKVal, this.getWorkID());
						obj_Ath.addAnd();
						obj_Ath.AddWhere(FrmAttachmentDBAttr.Rec, tk.getEmpFrom());
						obj_Ath.addOrderBy(FrmAttachmentDBAttr.RDT);
						obj_Ath.DoQuery();

						for (FrmAttachmentDB athDB : athDBs.ToJavaList()) {
							row = athDt.NewRow();

							row.setValue("NodeID", tk.getNDFrom());
							row.setValue("MyPK", athDB.getMyPK());
							row.setValue("FK_FrmAttachment", athDB.getFK_FrmAttachment());
							row.setValue("FK_MapData", athDB.getFK_MapData());
							row.setValue("FileName", athDB.getFileName());
							row.setValue("FileExts", athDB.getFileExts());
							row.setValue("CanDelete", athDB.getFK_MapData() ==
									String.valueOf(this.getFK_Node()) && athDB.getRec().equals(WebUser.getNo()) &&
									isReadonly == false);
							athDt.Rows.add(row);


						}
						// #endregion

						// #region //子流程的审核组件数据
						if (tk.getFID() != 0 && tk.getHisActionType() == ActionType.StartChildenFlow
								&& tkDt.Select("ParentNode=" + tk.getNDFrom()).length == 0) {
							String[] paras = tk.getTag().split("@");
							String[] p1 = paras[1].split("=");
							String fk_flow = p1[1]; // 子流程编号

							String[] p2 = paras[2].split("=");
							String workId = p2[1]; // 子流程ID.
							int biaoji = 0;

							WorkCheck subwc = new WorkCheck(fk_flow, Integer.parseInt(fk_flow + "01"), Long.parseLong(workId),
									0);

							Tracks subtks = subwc.getHisWorkChecks();
							// 取出来子流程的所有的节点。
							Nodes subNds = new Nodes(fk_flow);
							for (Node item : subNds.ToJavaList()) // 主要按顺序显示
							{
								for (Track mysubtk : subtks.ToJavaList()) {
									if (item.getNodeID() != mysubtk.getNDFrom())
										continue;

									/* 输出该子流程的审核信息，应该考虑子流程的子流程信息, 就不考虑那样复杂了. */
									if (mysubtk.getHisActionType() == ActionType.WorkCheck) {
										// 发起多个子流程时，发起人只显示一次
										if (mysubtk.getNDFrom() == Integer.parseInt(fk_flow + "01") && biaoji == 1)
											continue;

										row = tkDt.NewRow();
										row.setValue("NodeID", mysubtk.getNDFrom());
										row.setValue("NodeName", String.format("(子流程){0}", mysubtk.getNDFromT()));
										row.setValue("Msg", mysubtk.getMsgHtml());
										row.setValue("EmpFrom", mysubtk.getEmpFrom());
										row.setValue("EmpFromT", mysubtk.getEmpFromT());
										row.setValue("RDT", mysubtk.getRDT());
										row.setValue("IsDoc", false);
										row.setValue("ParentNode", tk.getNDFrom());
										row.setValue("T_NodeIndex", idx++);
										row.setValue("T_CheckIndex", noneEmpIdx++);
										row.setValue("ActionType", mysubtk.getHisActionType().getValue());
										tkDt.Rows.add(row);

										if (mysubtk.getNDFrom() == Integer.parseInt(fk_flow + "01")) {
											biaoji = 1;
										}
									}
								}
							}
						}
						// #endregion
						break;
					default:
						break;
				}

			}

		}
		// #endregion 判断是否显示 - 历史审核信息显示

		// #region 审核意见默认填写

		// 首先判断当前是否有此意见? 如果是退回的该信息已经存在了.
		Boolean isHaveMyInfo = false;
		for (DataRow dr : tkDt.Rows) {
			String fk_node = dr.getValue("NodeID").toString();
			String empFrom = dr.getValue("EmpFrom").toString();
			if (Integer.parseInt(fk_node) == this.getFK_Node() && empFrom == WebUser.getNo())
				isHaveMyInfo = true;
		}

		// 增加默认的审核意见.
		if (isExitTb_doc && wcDesc.getHisFrmWorkCheckSta() == FrmWorkCheckSta.Enable && isCanDo && isReadonly == false
				&& isHaveMyInfo == false) {
			DataRow[] rows = null;
			nd = (Node) nds.GetEntityByKey(this.getFK_Node());
			if (wcDesc.getFWCOrderModel() == FWCOrderModel.SqlAccepter) {
				rows = tkDt.Select("NodeID=" + this.getFK_Node() + " AND Msg='' AND EmpFrom='" + WebUser.getNo() + "'");

				if (rows.length == 0)
					rows = tkDt.Select("NodeID=" + this.getFK_Node() + " AND EmpFrom='" + WebUser.getNo() + "'");

				if (rows.length > 0) {
					row = rows[0];
					row.setValue("IsDoc", true);

					String mymsg = Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(), this.getFK_Node());
					if (mymsg == null)
						mymsg = "";
					//ccflow没有这段
					row.setValue("Msg", mymsg);
					if (mymsg == "")
						row.setValue("RDT", "");

					// 增加默认审核意见
					if (DataType.IsNullOrEmpty(mymsg) && wcDesc.getFWCIsFullInfo())
						row.setValue("Msg", wcDesc.getFWCDefInfo());
				} else {
					row = tkDt.NewRow();
					row.setValue("NodeID", this.getFK_Node());
					row.setValue("NodeName", nd.getFWCNodeName());
					row.setValue("IsDoc", true);
					row.setValue("ParentNode", "0");
					row.setValue("RDT", "");

					row.setValue("Msg",
							Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(), this.getFK_Node()));

					row.setValue("EmpFrom", WebUser.getNo());
					row.setValue("EmpFromT", WebUser.getName());
					row.setValue("T_NodeIndex", ++idx);
					row.setValue("T_CheckIndex", ++noneEmpIdx);
					row.setValue("ActionType",ActionType.Forward.getValue());
					tkDt.Rows.add(row);
				}
			} else {
				row = tkDt.NewRow();
				row.setValue("NodeID", this.getFK_Node());
				row.setValue("NodeName", nd.getFWCNodeName());
				row.setValue("IsDoc", true);
				row.setValue("ParentNode", 0);
				row.setValue("RDT", "");
				row.setValue("Msg", Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(), this.getFK_Node()));
				row.setValue("EmpFrom", WebUser.getNo());
				row.setValue("EmpFromT", WebUser.getName());
				row.setValue("T_NodeIndex", ++idx);
				row.setValue("T_CheckIndex", ++noneEmpIdx);
				row.setValue("ActionType",ActionType.Forward.getValue());
				tkDt.Rows.add(row);
			}
		}
		 // 显示有审核组件，但还未审核的节点.  包括退回后的.
         if (tks == null)
             tks = wc.getHisWorkChecks();

         for (FrmWorkCheck item : fwcs.ToJavaList())
         {
             if (item.getFWCIsShowTruck() == false)
                 continue;  //不需要显示历史记录.

             //是否已审核.
             Boolean isHave = false;
             for (BP.WF.Track tk : tks.ToJavaList())
             {
                 //翻译.
                 if (tk.getNDFrom() == this.getFK_Node() && tk.getHisActionType() == ActionType.WorkCheck)
                 {
                     isHave = true; //已经有了
                     break;
                 }
             }

             if (isHave == true)
                 continue;

             row = tkDt.NewRow();
             
             row.setValue("NodeID", item.getNodeID());
             Node mynd = (Node)nds.GetEntityByKey(item.getNodeID());
             row.setValue("NodeName", mynd.getFWCNodeName());
             row.setValue("IsDoc", false);
             row.setValue("ParentNode", 0);
             row.setValue("RDT", "");
             row.setValue("Msg", "&nbsp;");
             row.setValue("EmpFrom", "");
             row.setValue("EmpFromT", "");
             row.setValue("T_NodeIndex", ++idx);
             row.setValue("T_CheckIndex", ++noneEmpIdx);

             tkDt.Rows.add(row);
         }
        // 增加空白.

         ds.Tables.add(tkDt);

         //如果有 SignType 列就获得签名信息.
         if (SystemConfig.getCustomerNo() == "TianYe")
         {
             String tTable = "ND" + Integer.valueOf(this.getFK_Flow()) + "Track";
             String sql = "SELECT distinct a.No, a.SignType, a.EleID FROM Port_Emp a, " + tTable + " b WHERE (A.No='" + WebUser.getNo() + "') OR B.ActionType=22 AND a.No=b.EmpFrom AND B.WorkID=" + this.getWorkID();

             DataTable dtTrack = DBAccess.RunSQLReturnTable(sql);
             dtTrack.TableName = "SignType";

             dtTrack.Columns.get("NO").ColumnName = "No";
             dtTrack.Columns.get("SIGNTYPE").ColumnName = "SignType";
             dtTrack.Columns.get("ELEID").ColumnName = "EleID";

             ds.Tables.add(dtTrack);
         }

         String str = BP.Tools.Json.ToJson(ds);
         return str;

	}

	/**
	 * 获取审核组件中刚上传的附件列表信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String WorkCheck_GetNewUploadedAths() throws Exception {
		DataRow row = null;
		String athNames = GetRequestVal("Names");
		String attachPK = GetRequestVal("AttachPK");

		DataTable athDt = new DataTable("Aths");
		athDt.Columns.Add("NodeID", Integer.class);
		athDt.Columns.Add("MyPK", String.class);
		athDt.Columns.Add("FK_FrmAttachment", String.class);
		athDt.Columns.Add("FK_MapData", String.class);
		athDt.Columns.Add("FileName", String.class);
		athDt.Columns.Add("FileExts", String.class);
		athDt.Columns.Add("CanDelete", String.class);

		FrmAttachmentDBs athDBs = new FrmAttachmentDBs();
		QueryObject obj_Ath = new QueryObject(athDBs);
		obj_Ath.AddWhere(FrmAttachmentDBAttr.FK_FrmAttachment, "ND"+this.getFK_Node() + "_FrmWorkCheck");
		obj_Ath.addAnd();
		obj_Ath.AddWhere(FrmAttachmentDBAttr.RefPKVal, this.getWorkID());
		obj_Ath.addOrderBy(FrmAttachmentDBAttr.RDT);
		obj_Ath.DoQuery();

		for (FrmAttachmentDB athDB : athDBs.ToJavaList()) {
			if (athNames.toLowerCase().indexOf("|" + athDB.getFileName().toLowerCase() + "|") == -1) {
				continue;
			}

			row = athDt.NewRow();

			row.setValue("NodeID", this.getFK_Node());
			row.setValue("MyPK", athDB.getMyPK());
			row.setValue("FK_FrmAttachment", athDB.getFK_FrmAttachment());
			row.setValue("FK_MapData", athDB.getFK_MapData());
			row.setValue("FileName", athDB.getFileName());
			row.setValue("FileExts", athDB.getFileExts());
			row.setValue("CanDelete", athDB.getRec().equals(WebUser.getNo()));

			athDt.Rows.add(row);
		}

		return BP.Tools.Json.ToJson(athDt);
	}

	/**
	 * 获取附件链接
	 * 
	 * @param athDB
	 * @return
	 * @throws Exception
	 */
	private String GetFileAction(FrmAttachmentDB athDB) throws Exception {

		if (athDB == null || athDB.getFileExts().equals("")) {
			return "#";
		}

		FrmAttachment athDesc = new FrmAttachment(athDB.getFK_FrmAttachment());
		// C# TO JAVA CONVERTER NOTE: The following 'switch' operated on a
		// string member and was converted to Java 'if-else' logic:
		// switch (athDB.FileExts)
		// ORIGINAL LINE: case "doc":
		if (athDB.getFileExts().equals("doc") || athDB.getFileExts().equals("docx") || athDB.getFileExts().equals("xls")
				|| athDB.getFileExts().equals("xlsx")) {
			return "javascript:AthOpenOfiice('" + athDB.getFK_FrmAttachment() + "','" + this.getWorkID() + "','"
					+ athDB.getMyPK() + "','" + athDB.getFK_MapData() + "','" + athDB.getFK_FrmAttachment() + "','"
					+ this.getFK_Node() + "')";
		}
		// ORIGINAL LINE: case "txt":
		else if (athDB.getFileExts().equals("txt") || athDB.getFileExts().equals("jpg")
				|| athDB.getFileExts().equals("jpeg") || athDB.getFileExts().equals("gif")
				|| athDB.getFileExts().equals("png") || athDB.getFileExts().equals("bmp")
				|| athDB.getFileExts().equals("ceb")) {
			return "javascript:AthOpenView('" + athDB.getRefPKVal() + "','" + athDB.getMyPK() + "','"
					+ athDB.getFK_FrmAttachment() + "','" + athDB.getFileExts() + "','" + this.getFK_Flow() + "','"
					+ athDB.getFK_MapData() + "','" + this.getWorkID() + "','false')";
		}
		// ORIGINAL LINE: case "pdf":
		else if (athDB.getFileExts().equals("pdf")) {
			return athDesc.getSaveTo() + this.getWorkID() + "/" + athDB.getMyPK() + "." + athDB.getFileName();
		}

		return "javascript:AthDown('" + athDB.getFK_FrmAttachment() + "','" + this.getWorkID() + "','" + athDB.getMyPK()
				+ "','" + athDB.getFK_MapData() + "','" + this.getFK_Flow() + "','" + athDB.getFK_FrmAttachment()
				+ "')";
	}

	/**
	 * 审核信息保存.
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String WorkCheck_Save() throws Exception {

		// 审核信息.
		String msg = "";
		String dotype = this.GetRequestVal("ShowType");
		String doc = GetRequestVal("Doc");
		boolean isCC = GetRequestVal("IsCC").equals("1");
		// 查看时取消保存
		if (dotype != null && dotype.equals("View")) {
			return "";
		}

		// 内容为空，取消保存，20170727取消此处限制
		// if (string.IsNullOrEmpty(doc.Trim()))
		// return "";

		String val = "";
		FrmWorkCheck wcDesc = new FrmWorkCheck(this.getFK_Node());
		if (DataType.IsNullOrEmpty(wcDesc.getFWCFields()) == false) {
			// 循环属性获取值
			Attrs fwcAttrs = new Attrs(wcDesc.getFWCFields());
			for (Attr attr : fwcAttrs) {
				if (attr.getUIContralType() == UIContralType.TB) {
					val = GetRequestVal("TB_" + attr.getKey());

					msg += attr.getKey() + "=" + val + ";";
				} else if (attr.getUIContralType() == UIContralType.CheckBok) {
					val = GetRequestVal("CB_" + attr.getKey());

					msg += attr.getKey() + "=" + Integer.parseInt(val) + ";";
				} else if (attr.getUIContralType() == UIContralType.DDL) {
					val = GetRequestVal("DDL_" + attr.getKey());

					msg += attr.getKey() + "=" + val + ";";
				}
			}
		} else {
			// 加入审核信息.
			msg = doc;
		}

		// 处理人大的需求，需要把审核意见写入到FlowNote里面去.
		String sql = "UPDATE WF_GenerWorkFlow SET FlowNote='" + msg + "' WHERE WorkID=" + this.getWorkID();
		DBAccess.RunSQL(sql);

		// 判断是否是抄送?
		if (isCC) {
			// 写入审核信息，有可能是update数据。
			Dev2Interface.WriteTrackWorkCheck(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), this.getFID(),
					msg, wcDesc.getFWCOpLabel());

			// 设置抄送状态 - 已经审核完毕.
			Dev2Interface.Node_CC_SetSta(this.getFK_Node(), this.getWorkID(), WebUser.getNo(), CCSta.CheckOver);
			return "";
		}

		/// #region 根据类型写入数据 qin
		if (wcDesc.getHisFrmWorkCheckType() == FWCType.Check) // 审核组件
		{
			// 判断是否审核组件中"协作模式下操作员显示顺序"设置为"按照接受人员列表先后顺序(官职大小)"，删除原有的空审核信息
			if (wcDesc.getFWCOrderModel() == FWCOrderModel.SqlAccepter) {
				sql = "DELETE FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track WHERE WorkID = "
						+ this.getWorkID() + " AND ActionType = " + ActionType.WorkCheck.getValue() + " AND NDFrom = "
						+ this.getFK_Node() + " AND NDTo = " + this.getFK_Node() + " AND EmpFrom = '" + WebUser.getNo()
						+ "' AND (Msg='' OR Msg IS NULL)";
				DBAccess.RunSQL(sql);
			}

			Dev2Interface.WriteTrackWorkCheck(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), this.getFID(),
					msg, wcDesc.getFWCOpLabel());
		}

		if (wcDesc.getHisFrmWorkCheckType() == FWCType.DailyLog) // 日志组件
		{
			Dev2Interface.WriteTrackDailyLog(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), this.getFID(), msg,
					wcDesc.getFWCOpLabel());
		}

		if (wcDesc.getHisFrmWorkCheckType() == FWCType.WeekLog) // 周报
		{
			Dev2Interface.WriteTrackWeekLog(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), this.getFID(), msg,
					wcDesc.getFWCOpLabel());
		}

		if (wcDesc.getHisFrmWorkCheckType() == FWCType.MonthLog) // 月报
		{
			Dev2Interface.WriteTrackMonthLog(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), this.getFID(), msg,
					wcDesc.getFWCOpLabel());
		}

		sql = "SELECT MyPK,RDT FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track WHERE NDFrom = "
				+ this.getFK_Node() + " AND ActionType = " + ActionType.WorkCheck.getValue() + " AND EmpFrom = '"
				+ WebUser.getNo() + "'";
		DataTable dt = DBAccess.RunSQLReturnTable(sql, 1, 1, "MyPK", "RDT", "DESC");

		return dt.Rows.size() > 0 ? dt.Rows.get(0).getValue("RDT").toString() : "";
	}

	public String CC_SelectGroups() {
		String sql = "SELECT NO,NAME FROM GPM_Group ORDER BY IDX";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		return BP.Tools.Json.ToJson(dt);
	}

	public String CC_Init() throws Exception {
		// 抄送初始化.
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		java.util.Hashtable ht = new java.util.Hashtable();
		ht.put("Title", gwf.getTitle());

		// 计算出来曾经抄送过的人.
		String sql = "SELECT CCToName FROM WF_CCList WHERE FK_Node=" + this.getFK_Node() + " AND WorkID="
				+ this.getWorkID();

		DataTable mydt = DBAccess.RunSQLReturnTable(sql);
		String toAllEmps = "";
		for (DataRow dr : mydt.Rows) {
			toAllEmps += dr.getValue(0).toString() + ",";
		}

		ht.put("CCTo", toAllEmps);

		// 根据他判断是否显示权限组。
		if (BP.DA.DBAccess.IsExitsObject("GPM_Group") == true) {
			ht.put("IsGroup", "1");
		} else {
			ht.put("IsGroup", "0");
		}

		// 返回流程标题.
		return BP.Tools.Json.ToJsonEntityModel(ht);
	}

	public String CC_SelectDepts() throws Exception {
		// 选择部门呈现信息.
		BP.Port.Depts depts = new BP.Port.Depts();
		depts.RetrieveAll();
		return depts.ToJson();
	}

	public String CC_SelectStations() {
		// CC_SelectStations
		// 岗位类型.
		String sql = "SELECT NO,NAME FROM Port_StationType ORDER BY NO";
		DataSet ds = new DataSet();
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Port_StationType";
		ds.Tables.add(dt);

		// 岗位.
		String sqlStas = "SELECT NO,NAME,FK_STATIONTYPE FROM Port_Station ORDER BY FK_STATIONTYPE,NO";
		DataTable dtSta = BP.DA.DBAccess.RunSQLReturnTable(sqlStas);
		dtSta.TableName = "Port_Station";
		ds.Tables.add(dtSta);
		return BP.Tools.Json.ToJson(ds);
	}

	public String CC_Send() throws Exception {

		// 人员信息. 格式 zhangsan,张三;lisi,李四;
		String emps = this.GetRequestVal("Emps");

		// 岗位信息. 格式: 001,002,003,
		String stations = this.GetRequestVal("Stations");
		stations = stations.replace(";", ",");

		// 权限组. 格式: 001,002,003,
		String groups = this.GetRequestVal("Groups");
		if (groups == null) {
			groups = "";
		}
		groups = groups.replace(";", ",");

		// 部门信息. 格式: 001,002,003,
		String depts = this.GetRequestVal("Depts");
		// 标题.
		String title = this.GetRequestVal("TB_Title");
		// 内容.
		String doc = this.GetRequestVal("TB_Doc");

		// 调用抄送接口执行抄送.
		String ccRec = BP.WF.Dev2Interface.Node_CC_WriteTo_CClist(this.getFK_Node(), this.getWorkID(), title, doc, emps,
				depts, stations, groups);

		if (ccRec.equals("")) {
			return "没有抄送到任何人。";
		} else {
			return "本次抄送给如下人员：" + ccRec;
		}
	}

	public String DealSubThreadReturnToHL_Init() throws Exception {

		// 如果工作节点退回了
		BP.WF.ReturnWorks rws = new BP.WF.ReturnWorks();
		rws.Retrieve(BP.WF.ReturnWorkAttr.ReturnToNode, this.getFK_Node(), BP.WF.ReturnWorkAttr.WorkID,
				this.getWorkID(), BP.WF.ReturnWorkAttr.RDT);

		String msgInfo = "";
		if (rws.size() != 0) {
			for (BP.WF.ReturnWork rw : rws.ToJavaList()) {
				msgInfo += "<fieldset width='100%' ><legend>&nbsp; 来自节点:" + rw.getReturnNodeName() + " 退回人:"
						+ rw.getReturnerName() + "  " + rw.getRDT() + "</legend>";
				msgInfo += rw.getBeiZhuHtml();
				msgInfo += "</fieldset>";
			}
		}

		// 把节点信息也传入过去，用于判断不同的按钮显示.
		BP.WF.Template.BtnLab btn = new BtnLab(this.getFK_Node());
		BP.WF.Node nd = new Node(this.getFK_Node());

		java.util.Hashtable ht = new java.util.Hashtable();
		// 消息.
		ht.put("MsgInfo", msgInfo);

		// 是否可以移交？
		if (btn.getShiftEnable()) {
			ht.put("ShiftEnable", "1");
		} else {
			ht.put("ShiftEnable", "0");
		}

		// 是否可以撤销？
		if (nd.getHisCancelRole() == CancelRole.None) {
			ht.put("CancelRole", "0");
		} else {
			ht.put("CancelRole", "1");
		}

		// 是否可以删除子线程? 在分流节点上.
		if (btn.getThreadIsCanDel()) {
			ht.put("ThreadIsCanDel", "1");
		} else {
			ht.put("ThreadIsCanDel", "0");
		}

		// 是否可以移交子线程? 在分流节点上.
		if (btn.getThreadIsCanShift()) {
			ht.put("ThreadIsCanShift", "1");
		} else {
			ht.put("ThreadIsCanShift", "0");
		}

		return BP.Tools.Json.ToJsonEntityModel(ht);
	}

	public String DealSubThreadReturnToHL_Done() throws Exception {
		String msg = "";
		// 操作类型.
		String actionType = this.GetRequestVal("ActionType");
		String note = this.GetRequestVal("Note");

		if (actionType.equals("Return")) {
			// 如果是退回.
			BP.WF.ReturnWork rw = new BP.WF.ReturnWork();
			rw.Retrieve(BP.WF.ReturnWorkAttr.ReturnToNode, this.getFK_Node(), BP.WF.ReturnWorkAttr.WorkID,
					this.getWorkID());
			String info = BP.WF.Dev2Interface.Node_ReturnWork(this.getFK_Flow(), this.getWorkID(), this.getFID(),
					this.getFK_Node(), rw.getReturnNode(), note, false);
			return info;
		}

		if (actionType.equals("Shift")) {
			// 如果是移交操作.
			String toEmps = this.GetRequestVal("ShiftToEmp");
			return BP.WF.Dev2Interface.Node_Shift(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), this.getFID(),
					toEmps, note);
		}

		if (actionType.equals("Kill")) {
			msg = BP.WF.Dev2Interface.Flow_DeleteSubThread(this.getFK_Flow(), this.getWorkID(), "手工删除");
			// 提示信息.
			if (msg.equals("") || msg == null) {
				msg = "该工作删除成功...";
			}
			return msg;
		}

		if (actionType.equals("UnSend")) {
			return BP.WF.Dev2Interface.Flow_DoUnSend(this.getFK_Flow(), this.getFID(), this.getFK_Node());
		}

		return "err@没有判断的类型" + actionType;
	}

	public final String DeleteFlowInstance_Init() {
		try {
			if (BP.WF.Dev2Interface.Flow_IsCanDeleteFlowInstance(this.getFK_Flow(), this.getWorkID(),
					BP.Web.WebUser.getNo()) == false) {
				return "err@您没有删除该流程的权限";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 获取节点中配置的流程删除规则
		if (this.getFK_Node() != 0) {
			String sql = "SELECT wn.DelEnable FROM WF_Node wn WHERE wn.NodeID = " + this.getFK_Node();
			return DBAccess.RunSQLReturnValInt(sql) + "";
		}

		return "";
	}

	public String DeleteFlowInstance_DoDelete() throws Exception {

		try {
			if (BP.WF.Dev2Interface.Flow_IsCanDeleteFlowInstance(this.getFK_Flow(), this.getWorkID(),
					BP.Web.WebUser.getNo()) == false) {
				return "您没有删除该流程的权限.";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String deleteWay = this.GetRequestVal("RB_DeleteWay");
		String doc = this.GetRequestVal("TB_Doc");

		// 是否要删除子流程？ 这里注意变量的获取方式，你可以自己定义.
		String isDeleteSubFlow = this.GetRequestVal("CB_IsDeleteSubFlow");

		boolean isDelSubFlow = false;
		if (isDeleteSubFlow.equals("1")) {
			isDelSubFlow = true;
		}

		// 按照标记删除.
		if (deleteWay.equals("0")) {
			BP.WF.Dev2Interface.Flow_DoDeleteFlowByFlag(this.getFK_Flow(), this.getWorkID(), doc, isDelSubFlow);
		}

		// 彻底删除.
		if (deleteWay.equals("1")) {
			BP.WF.Dev2Interface.Flow_DoDeleteFlowByReal(this.getFK_Flow(), this.getWorkID(), isDelSubFlow);
		}

		// 彻底并放入到删除轨迹里.
		if (deleteWay.equals("2")) {
			BP.WF.Dev2Interface.Flow_DoDeleteFlowByWriteLog(this.getFK_Flow(), this.getWorkID(), doc, isDelSubFlow);
		}

		return "流程删除成功.";
	}

	public String ViewWorkNodeFrm() throws Exception {
		// 获得节点表单数据.
		Node nd = new Node(this.getFK_Node());
		nd.WorkID = this.getWorkID(); // 为求当前表单ID获得参数，而赋值.

		java.util.Hashtable ht = new java.util.Hashtable();
		ht.put("FormType", nd.getFormType().toString());
		ht.put("Url", nd.getFormUrl() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&FK_Node="
				+ this.getFK_Node());

		if (nd.getFormType() == NodeFormType.SDKForm) {
			return BP.Tools.Json.ToJsonEntityModel(ht);
		}

		if (nd.getFormType() == NodeFormType.SelfForm) {
			return BP.Tools.Json.ToJsonEntityModel(ht);
		}

		// 表单模版.
		DataSet myds = BP.Sys.CCFormAPI.GenerHisDataSet(nd.getNodeFrmID(), null);
		String json = BP.WF.Dev2Interface.CCFrom_GetFrmDBJson(this.getFK_Flow(), this.getMyPK());
		DataTable mainTable = BP.Tools.Json.ToDataTableOneRow(json);
		mainTable.TableName = "MainTable";
		myds.Tables.add(mainTable);

		// MapExts exts = new MapExts(nd.HisWork.ToString());
		// DataTable dtMapExt = exts.ToDataTableDescField();
		// dtMapExt.TableName = "Sys_MapExt";
		// myds.Tables.add(dtMapExt);

		return BP.Tools.Json.ToJson(myds);
	}

	public String AskForRe() throws Exception {
		/// 签信息.
		String note = this.GetRequestVal("Note"); // 原因.
		return BP.WF.Dev2Interface.Node_AskforReply(this.getWorkID(), note);
	}

	public String Askfor() throws Exception {
		// 执行加签
		long workID = Integer.parseInt(this.GetRequestVal("WorkID")); // 工作ID
		String toEmp = this.GetRequestVal("ToEmp"); // 让谁加签?
		String note = this.GetRequestVal("Note"); // 原因.
		String model = this.GetRequestVal("Model"); // 模式.

		BP.WF.AskforHelpSta sta = BP.WF.AskforHelpSta.AfterDealSend;
		if (model.equals("1")) {
			sta = BP.WF.AskforHelpSta.AfterDealSendByWorker;
		}
		return BP.WF.Dev2Interface.Node_Askfor(workID, sta, toEmp, note);
	}

	public String SelectEmps() throws Exception {
		// 人员选择器
		String fk_dept = this.GetRequestVal("FK_Dept");
		if (fk_dept == null || StringUtils.isEmpty(fk_dept)) {
			fk_dept = BP.Web.WebUser.getFK_Dept();
		}

		DataSet ds = new DataSet();

		String sql = "SELECT No,Name,ParentNo FROM Port_Dept WHERE No='" + fk_dept + "' OR ParentNo='" + fk_dept + "' ";
		DataTable dtDept = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dtDept.TableName = "Depts";
		ds.Tables.add(dtDept);
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
			dtDept.Columns.get(0).ColumnName = "No";
			dtDept.Columns.get(1).ColumnName = "Name";
			dtDept.Columns.get(2).ColumnName = "ParentNo";
		}

		sql = "SELECT No,Name,FK_Dept FROM Port_Emp WHERE FK_Dept='" + fk_dept + "'";
		DataTable dtEmps = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dtEmps.TableName = "Emps";
		ds.Tables.add(dtEmps);
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
			dtDept.Columns.get(0).ColumnName = "No";
			dtDept.Columns.get(1).ColumnName = "Name";
			dtDept.Columns.get(2).ColumnName = "ParentNo";
		}

		return BP.Tools.Json.ToJson(ds);
	}
	
	public DataTable GetNextWorks(long WorkID,String FK_Node,String FK_Flow,int toNodeID) throws Exception {
		/* 如果是协作模式, 就要检查当前是否主持人, 当前是否是会签模式. */
		GenerWorkFlow gwf = new GenerWorkFlow(WorkID);
		if (gwf.getFK_Node() != Integer.parseInt(FK_Node))
			throw new Exception("err@当前流程已经运动到[" + gwf.getNodeName() + "]上,当前处理人员为[" + gwf.getTodoEmps() + "]");

		// 当前节点ID.
		Node nd = new Node(FK_Node);

		// 判断当前是否是协作模式.
		if (nd.getTodolistModel() == TodolistModel.Teamup && nd.getIsStartNode() == false) {
			if (gwf.getTodoEmps().contains(WebUser.getNo() + ",")) {
				/* 说明我是主持人之一, 我就可以选择接受人,发送到下一个节点上去. */
			} else {
				/* 不是主持人就执行发送，返回发送结果. */
				SendReturnObjs objs = BP.WF.Dev2Interface.Node_SendWork(FK_Flow, WorkID);
				throw new Exception("info@" + objs.ToMsgOfHtml());
			}
		}

		if (toNodeID == 0) {
			Nodes nds = nd.getHisToNodes();
			if (nds.size() == 1)
				toNodeID = nds.get(0).GetValIntByKey("NodeID");
			else
				throw new Exception("err@参数错误,必须传递来到达的节点ID ToNode .");
		}

		Work wk = nd.getHisWork();
		wk.setOID(this.getWorkID());
		wk.Retrieve();

		Selector select = new Selector(toNodeID);
		if (select.getSelectorModel() == SelectorModel.GenerUserSelecter)
			throw new Exception("url@AccepterOfGener.htm?WorkID=" + WorkID + "&FK_Node=" + FK_Node + "&FK_Flow="
					+ nd.getFK_Flow() + "&ToNode=" + toNodeID);

		if (select.getSelectorModel() == SelectorModel.AccepterOfDeptStationEmp)
			throw new Exception("url@AccepterOfDeptStationEmp.htm?WorkID=" + WorkID + "&FK_Node=" + FK_Node
					+ "&FK_Flow=" + nd.getFK_Flow() + "&ToNode=" + toNodeID);

		// 获得 部门与人员.
		DataSet ds = select.GenerDataSet(toNodeID, wk);

		// #region 计算上一次选择的结果, 并把结果返回过去.
		String sql = "";
		DataTable dt = new DataTable();
		dt.Columns.Add("No", String.class);
		dt.TableName = "Selected";
		if (select.getIsAutoLoadEmps() == true) {
			if (SystemConfig.getAppCenterDBType() == DBType.MSSQL)
				sql = "SELECT  top 1 Tag,EmpTo FROM ND" + Integer.parseInt(nd.getFK_Flow()) + "Track A WHERE A.NDFrom="
						+ this.getFK_Node() + " AND A.NDTo=" + toNodeID + " AND ActionType=1 ORDER BY WorkID DESC";
			else if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
				sql = "SELECT * FROM (SELECT  Tag,EmpTo,WorkID FROM ND" + Integer.parseInt(nd.getFK_Flow())
						+ "Track A WHERE A.NDFrom=" + this.getFK_Node() + " AND A.NDTo=" + toNodeID
						+ " AND ActionType=1 ORDER BY WorkID DESC ) WHERE ROWNUM =1";
			else if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
				sql = "SELECT  Tag,EmpTo FROM ND" + Integer.parseInt(nd.getFK_Flow()) + "Track A WHERE A.NDFrom="
						+ this.getFK_Node() + " AND A.NDTo=" + toNodeID
						+ " AND ActionType=1 ORDER BY WorkID  DESC limit 1,1 ";

			DataTable mydt = DBAccess.RunSQLReturnTable(sql);
			String emps = "";
			if (mydt.Rows.size() != 0) {
				emps = mydt.Rows.get(0).getValue("Tag").toString();
				if (DataType.IsNullOrEmpty(emps)) {
					emps = mydt.Rows.get(0).getValue("EmpTo").toString();
					emps = emps + "," + emps;
				}
			}

			String[] strs = emps.split(";");
			for (String str : strs) {
				if (DataType.IsNullOrEmpty(str) == true)
					continue;

				String[] emp = str.split(",");
				if (emp.length != 2)
					continue;

				DataRow dr = dt.NewRow();
				dr.setValue(0, emp[0]);
				dt.Rows.add(dr);
			}
		}

		// 增加一个table.
		ds.Tables.add(dt);
		// #endregion 计算上一次选择的结果, 并把结果返回过去.

		// 返回json.
		return ds.GetTableByName("Emps");
	}
	
	public String Accepter_Init() throws Exception {
		/* 如果是协作模式, 就要检查当前是否主持人, 当前是否是会签模式. */
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		if (gwf.getFK_Node() != this.getFK_Node())
			return "err@当前流程已经运动到[" + gwf.getNodeName() + "]上,当前处理人员为[" + gwf.getTodoEmps() + "]";

		// 当前节点ID.
		Node nd = new Node(this.getFK_Node());

		// 判断当前是否是协作模式.
		if (nd.getTodolistModel() == TodolistModel.Teamup && nd.getIsStartNode() == false) {


			 String mysql = "SELECT COUNT(WORKID) AS Num FROM WF_GenerWorkerlist WHERE WorkID=" + this.getWorkID() + " AND FK_Node=" + this.getFK_Node() + " AND IsPass=0";
             int num = DBAccess.RunSQLReturnValInt(mysql);
             if (num != 1)
             {
                 /* 如果不是最后一位，返回发送结果. */
                 SendReturnObjs objs = BP.WF.Dev2Interface.Node_SendWork(this.getFK_Flow(), this.getWorkID());
                 return "info@" + objs.ToMsgOfHtml();
             }
			
		}

		int toNodeID = this.GetRequestValInt("ToNode");
		if (toNodeID == 0) {
			Nodes nds = nd.getHisToNodes();
			if (nds.size() == 1)
				toNodeID = nds.get(0).GetValIntByKey("NodeID");
			else
				return "err@参数错误,必须传递来到达的节点ID ToNode .";
		}

		Work wk = nd.getHisWork();
		wk.setOID(this.getWorkID());
		wk.Retrieve();

		Selector select = new Selector(toNodeID);
		if (select.getSelectorModel() == SelectorModel.GenerUserSelecter)
			return "url@AccepterOfGener.htm?WorkID=" + this.getWorkID() + "&FK_Node=" + this.getFK_Node() + "&FK_Flow="
					+ nd.getFK_Flow() + "&ToNode=" + toNodeID;

		if (select.getSelectorModel() == SelectorModel.AccepterOfDeptStationEmp)
			return "url@AccepterOfDeptStationEmp.htm?WorkID=" + this.getWorkID() + "&FK_Node=" + this.getFK_Node()
					+ "&FK_Flow=" + nd.getFK_Flow() + "&ToNode=" + toNodeID;

		// 获得 部门与人员.
		DataSet ds = select.GenerDataSet(toNodeID, wk);

		// #region 计算上一次选择的结果, 并把结果返回过去.
		String sql = "";
		DataTable dt = new DataTable();
		dt.Columns.Add("No", String.class);
		dt.TableName = "Selected";
		if (select.getIsAutoLoadEmps() == true) {
			if (SystemConfig.getAppCenterDBType() == DBType.MSSQL)
				sql = "SELECT  top 1 Tag,EmpTo FROM ND" + Integer.parseInt(nd.getFK_Flow()) + "Track A WHERE A.NDFrom="
						+ this.getFK_Node() + " AND A.NDTo=" + toNodeID + " AND ActionType=1 ORDER BY WorkID DESC";
			else if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
				sql = "SELECT * FROM (SELECT  Tag,EmpTo,WorkID FROM ND" + Integer.parseInt(nd.getFK_Flow())
						+ "Track A WHERE A.NDFrom=" + this.getFK_Node() + " AND A.NDTo=" + toNodeID
						+ " AND ActionType=1 ORDER BY WorkID DESC ) WHERE ROWNUM =1";
			else if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
				sql = "SELECT  Tag,EmpTo FROM ND" + Integer.parseInt(nd.getFK_Flow()) + "Track A WHERE A.NDFrom="
						+ this.getFK_Node() + " AND A.NDTo=" + toNodeID
						+ " AND ActionType=1 ORDER BY WorkID  DESC limit 1,1 ";

			DataTable mydt = DBAccess.RunSQLReturnTable(sql);
			String emps = "";
			if (mydt.Rows.size() != 0) {
				emps = mydt.Rows.get(0).getValue("Tag").toString();
				if (DataType.IsNullOrEmpty(emps)) {
					emps = mydt.Rows.get(0).getValue("EmpTo").toString();
					emps = emps + "," + emps;
				}
			}

			String[] strs = emps.split(";");
			for (String str : strs) {
				if (DataType.IsNullOrEmpty(str) == true)
					continue;

				String[] emp = str.split(",");
				if (emp.length != 2)
					continue;

				DataRow dr = dt.NewRow();
				dr.setValue(0, emp[0]);
				dt.Rows.add(dr);
			}
		}

		// 增加一个table.
		ds.Tables.add(dt);
		// #endregion 计算上一次选择的结果, 并把结果返回过去.

		// 返回json.
		return BP.Tools.Json.ToJson(ds);
	}

	public String AccepterSave12() throws Exception {
		try {
			// 求到达的节点.
			int toNodeID = 0;
			if (!this.GetRequestVal("ToNode").equals("0")) {
				toNodeID = Integer.parseInt(this.GetRequestVal("ToNode"));
			}

			if (toNodeID == 0) { // 没有就获得第一个节点.
				Node nd = new Node(this.getFK_Node());
				Nodes nds = nd.getHisToNodes();
				toNodeID = nds.get(0).GetValIntByKey("NodeID");
			}

			// 求发送到的人员.
			// string selectEmps = this.GetValFromFrmByKey("SelectEmps");
			String selectEmps = this.GetRequestVal("SelectEmps");
			selectEmps = selectEmps.replace(";", ",");

			// 保存接受人.
			BP.WF.Dev2Interface.Node_AddNextStepAccepters(this.getWorkID(), toNodeID, selectEmps, true);
			return "SaveOK@" + selectEmps;
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}

	public String Accepter_Send() throws Exception {
		try {
			// 求到达的节点.
			int toNodeID = 0;
			if (!this.GetRequestVal("ToNode").equals("0")) {
				toNodeID = Integer.parseInt(this.GetRequestVal("ToNode"));
			}

			if (toNodeID == 0) { // 没有就获得第一个节点.
				Node nd = new Node(this.getFK_Node());
				Nodes nds = nd.getHisToNodes();
				toNodeID = nds.get(0).GetValIntByKey("NodeID");
			}

			// 求发送到的人员.
			// string selectEmps = this.GetValFromFrmByKey("SelectEmps");
			String selectEmps = this.GetRequestVal("SelectEmps");
			selectEmps = selectEmps.replace(";", ",");

			// 执行发送.
			SendReturnObjs objs = BP.WF.Dev2Interface.Node_SendWork(this.getFK_Flow(), this.getWorkID(), toNodeID,
					selectEmps);
			return objs.ToMsgOfHtml();
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}

	/**
	 * 回滚页面初始化数据
	 * @return
	 */
	 public String Rollback_Init()
     {
         String andsql = " ";
         andsql += "  ActionType=" + ActionType.Start.getValue();
         andsql += " OR ActionType=" + ActionType.TeampUp.getValue();
         andsql += " OR ActionType=" + ActionType.Forward.getValue();
         andsql += " OR ActionType=" + ActionType.HuiQian.getValue();

         String sql = "SELECT RDT,NDFrom, NDFromT,EmpFrom,EmpFromT  FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track WHERE WorkID=" + this.getWorkID() + " AND("+andsql +") Order By RDT DESC";
         DataTable dt = DBAccess.RunSQLReturnTable(sql);

         if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
         {
             dt.Columns.get(0).ColumnName = "RDT";
             dt.Columns.get(1).ColumnName = "NDFrom";
             dt.Columns.get(2).ColumnName = "NDFromT";
             dt.Columns.get(3).ColumnName = "EmpFrom";
             dt.Columns.get(4).ColumnName = "EmpFromT";
         }


         return BP.Tools.Json.ToJson(dt);
     }

     /**
      * 执行回滚操作
      * @return
     * @throws Exception 
      */
     public String Rollback_Done() throws Exception
     {
         FlowExt flow = new FlowExt(this.getFK_Flow());
         return flow.DoRebackFlowData(this.getWorkID(), this.getFK_Node(), this.GetRequestVal("Msg"));
     }
     
	public String Return_Init() {

		try {

			DataTable dt = BP.WF.Dev2Interface.DB_GenerWillReturnNodes(this.getFK_Node(), this.getWorkID(),
					this.getFID());
			//如果只有一个退回节点，就需要判断是否启用了单节点退回规则.
            if (dt.Rows.size() == 1)
            {
               Node nd = new Node(this.getFK_Node());
               if (nd.getReturnOneNodeRole() != 0)
               {
                   /* 如果:启用了单节点退回规则.
                    */
                   String returnMsg = "";
                   if (nd.getReturnOneNodeRole() == 1 && DataType.IsNullOrEmpty(nd.getReturnField()) == false)
                   {
                       /*从表单字段里取意见.*/
                       Flow fl = new Flow(nd.getFK_Flow());
                       String sql = "SELECT " + nd.getReturnField() + " FROM " + fl.getPTable() + " WHERE OID=" + this.getWorkID();
                       returnMsg = DBAccess.RunSQLReturnStringIsNull(sql, "未填写意见");
                   }

                   if (nd.getReturnOneNodeRole() == 2)
                   {
                       /*从审核组件里取意见.*/
                       String sql = "SELECT Msg FROM ND" + Integer.parseInt(nd.getFK_Flow()) + "Track WHERE WorkID=" + this.getWorkID() + " AND NDFrom=" + this.getFK_Node() + " AND EmpFrom='" + WebUser.getNo() + "' AND ActionType=" + ActionType.WorkCheck.getValue();
                       returnMsg = DBAccess.RunSQLReturnStringIsNull(sql, "未填写意见");
                   }

                   int toNodeID = Integer.parseInt(dt.getValue(0, 0).toString());

                   String info = BP.WF.Dev2Interface.Node_ReturnWork(this.getFK_Flow(), this.getWorkID(), 0, this.getFK_Node(), toNodeID, returnMsg, false);
                   return "info@" + info;
               }
            }

			if (dt.Rows.size() == 0)
				return "err@没有获取到应该退回到的节点.";

			String str = BP.Tools.Json.ToJson(dt);

			return str;
		} catch (Exception ex) {
			return "err@" + ex.getMessage();
		}

	}

	public String DoReturnWork() {

		try {

			String[] vals = this.GetRequestVal("ReturnToNode").split("@");

			int toNodeID = Integer.parseInt(vals[0]);

            String toEmp = vals[1];
            
			String reMesage = this.GetRequestVal("ReturnInfo");

			boolean isBackBoolen = false;
			String isBack = this.GetRequestVal("IsBack");
			if (isBack.equals("1")) {
				isBackBoolen = true;
			}

			return BP.WF.Dev2Interface.Node_ReturnWork(this.getFK_Flow(), this.getWorkID(), this.getFID(),
					this.getFK_Node(), toNodeID,toEmp, reMesage, isBackBoolen);
		} catch (Exception ex) {
			return "err@" + ex.getMessage();
		}
	}

	public String Shift() throws Exception {
		String msg = this.GetRequestVal("Message");
		String toEmp = this.GetRequestVal("ToEmp");
		return BP.WF.Dev2Interface.Node_Shift(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), this.getFID(),
				toEmp, msg);
	}

	public String Allot() throws Exception {
		String msg = this.GetRequestVal("Message");
		String toEmp = this.GetRequestVal("ToEmp");
		return BP.WF.Dev2Interface.Node_Allot(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), this.getFID(),
				toEmp, msg);
	}

	public String UnShift() throws Exception {

		return BP.WF.Dev2Interface.Node_ShiftUn(this.getFK_Flow(), this.getWorkID());
	}

	public String Press() throws Exception {

		String msg = this.GetRequestVal("Msg");

		// 调用API.
		return BP.WF.Dev2Interface.Flow_DoPress(this.getWorkID(), msg, true);
	}

	public String FlowBBSList() {
		Paras ps = new Paras();
		ps.SQL = "SELECT * FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track WHERE ActionType="
				+ BP.Sys.SystemConfig.getAppCenterDBVarStr() + "ActionType";
		ps.Add("ActionType", BP.WF.ActionType.FlowBBS.getValue());

		// 转化成json
		return BP.Tools.Json.ToJson(BP.DA.DBAccess.RunSQLReturnTable(ps));
	}

	/**
	 * 默认执行的方法
	 */
	@Override
	public String DoDefaultMethod() {
		return "err@没有此方法getDoType:" + this.getDoType() + " this.toString" + this.toString();
	}

	/**
	 * 获取传入参数
	 * 
	 * @param param
	 *            参数名
	 * @return
	 */
	public final String getUTF8ToString(String param) {
		try {
			return java.net.URLDecoder.decode(this.getRequest().getParameter(param), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	public String dateFormatter(Date date, String fm) {
		SimpleDateFormat formatter = new SimpleDateFormat(fm);
		return formatter.format(date);

	}

	/**
	 * 会签
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String HuiQian_Init() throws Exception {
		// 要找到主持人.
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());

		// 查询出来集合.
		GenerWorkerLists ens = new GenerWorkerLists(this.getWorkID(), this.getFK_Node());
		for (GenerWorkerList item : ens.ToJavaList()) {

			if (gwf.getTodoEmps().contains(item.getFK_Emp() + ",") == true) {

				item.setFK_EmpText("<img src='../Img/zhuichiren.png' border=0 />" + item.getFK_EmpText());
				item.setFK_EmpText(item.getFK_EmpText());
				item.setIsPassInt(100);
				continue;
			}

			// 标记为自己.
			if (item.getFK_Emp().equals(BP.Web.WebUser.getNo())) {
				item.setFK_EmpText("" + item.getFK_EmpText());
				item.setIsPassInt(99);
			}
		}
		// 赋值部门名称。
		DataTable mydt = ens.ToDataTableField();
		mydt.Columns.Add("FK_DeptT", String.class);
		for (DataRow dr : mydt.Rows) {
			String fk_emp = dr.getValue("FK_Emp").toString();
			for (GenerWorkerList item : ens.ToJavaList()) {
				if (fk_emp.equals(item.getFK_Emp())) {
					dr.setValue("FK_DeptT", item.getFK_DeptT().toString());
				}
			}
		}
		return BP.Tools.Json.ToJson(mydt);
	}

	/**
	 * 移除
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String HuiQian_Delete() throws Exception {
		String emp = this.GetRequestVal("FK_Emp");
		if (this.getFK_Emp() == WebUser.getNo()) {
			return "err@您不能移除您自己";
		}

		// 要找到主持人.
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		if (gwf.getTodoEmps().contains(BP.Web.WebUser.getNo() + ",") == false) {
			return "err@您不是主持人，您不能删除。";
		}

		// 删除该数据.
		GenerWorkerList gwlOfMe = new GenerWorkerList();
		gwlOfMe.Delete(GenerWorkerListAttr.FK_Emp, this.getFK_Emp(), GenerWorkerListAttr.WorkID, this.getWorkID(),
				GenerWorkerListAttr.FK_Node, this.getFK_Node());

		// 如果已经没有会签待办了,就设置当前人员状态为0. @于庆海翻译 增加这部分.
		String sql = "SELECT COUNT(WorkID) FROM WF_GenerWorkerList WHERE FK_Node=" + this.getFK_Node() + " AND WorkID='"
				+ this.getWorkID() + "' AND IsPass=0";
		if (DBAccess.RunSQLReturnValInt(sql) == 0) {
			gwf.setHuiQianTaskSta(HuiQianTaskSta.HuiQianOver); // 设置为会签状态.
			gwf.Update();

			DBAccess.RunSQL("UPDATE WF_GenerWorkerList SET IsPass=0 WHERE FK_Node=" + this.getFK_Node() + " AND WorkID="
					+ this.getWorkID() + " AND FK_Emp='" + WebUser.getNo() + "'");
		}

		// 从待办里移除.
		BP.Port.Emp myemp = new BP.Port.Emp(this.getFK_Emp());
		String str = gwf.getTodoEmps();
		str = str.replace(";" + myemp.getName() + ";", "");
		gwf.setTodoEmps(str);
		gwf.Update();

		return HuiQian_Init();
	}

	/**
	 * 增加审核人员
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String HuiQian_AddEmps() throws Exception {

		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		if (gwf.getTodoEmps().contains(WebUser.getNo() + ",") == false)
			return "err@您不是会签主持人，您不能执行该操作。";

		GenerWorkerList gwlOfMe = new GenerWorkerList();
		int num = gwlOfMe.Retrieve(GenerWorkerListAttr.FK_Emp, WebUser.getNo(), GenerWorkerListAttr.WorkID,
				this.getWorkID(), GenerWorkerListAttr.FK_Node, this.getFK_Node());

		Node nd = new Node(this.getFK_Node());
		if (num == 0)
			return "err@您没有权限执行会签.";

		String fk_emp = this.GetRequestVal("AddEmps");

		Emp emp = new Emp(fk_emp);

		// 查查是否存在队列里？
		num = gwlOfMe.Retrieve(GenerWorkerListAttr.FK_Emp, emp.getNo(), GenerWorkerListAttr.WorkID, this.getWorkID(),
				GenerWorkerListAttr.FK_Node, this.getFK_Node());
		if (num == 1) {
			return "err@人员[" + emp.getNo() + "," + emp.getName() + "]已经在队列里.";
		}

		// 查询出来其他列的数据.
		gwlOfMe.Retrieve(GenerWorkerListAttr.FK_Emp, WebUser.getNo(), GenerWorkerListAttr.WorkID, this.getWorkID(),
				GenerWorkerListAttr.FK_Node, this.getFK_Node());

		gwlOfMe.setFK_Emp(emp.getNo());
		gwlOfMe.setFK_EmpText(emp.getName());
		gwlOfMe.setIsPassInt(-1); // 设置不可以用.
		gwlOfMe.setFK_Dept(emp.getFK_Dept());
		gwlOfMe.setFK_DeptT(emp.getFK_DeptText()); // 部门名称.
		gwlOfMe.setIsRead(false);

		gwlOfMe.setSender(WebUser.getName()); // 发送人为当前人.
		gwlOfMe.setIsHuiQian(true);
		gwlOfMe.Insert(); // 插入作为待办.

		// 发送消息.
		BP.WF.Dev2Interface.Port_SendMsg(emp.getNo(), "bpm会签邀请",
				"HuiQian" + gwf.getWorkID() + "_" + gwf.getFK_Node() + "_" + emp.getNo(),
				WebUser.getName() + "邀请您对工作｛" + gwf.getTitle() + "｝进行会签,请您在{" + gwlOfMe.getSDT() + "}前完成.", "HuiQian",
				gwf.getFK_Flow(), gwf.getFK_Node(), gwf.getWorkID(), gwf.getFID());

		if (gwf.getTodoEmps().contains(emp.getName() + ";") == false) {
			gwf.setTodoEmps(gwf.getTodoEmps() + emp.getName() + ";");
		}

		gwf.Update();

		return "增加成功.";

		// 把会签init的数据返回.
		// return HuiQian_Init();
	}

	// 查询select集合
	public final String HuiQian_SelectEmps() throws Exception {
		String sql = "";
		String emp = this.GetRequestVal("TB_Emps");
		boolean isPinYin = DBAccess.IsExitsTableCol("Port_Emp", "PinYin");
		if (isPinYin == true) {
			if (SystemConfig.getAppCenterDBType() == DBType.MSSQL) {
				sql = "SELECT TOP 12 a.No,a.Name +'/'+b.name as Name FROM Port_Emp a,Port_Dept b  WHERE  (a.fk_dept=b.no) and (a.No like '%"
						+ emp + "%' OR a.NAME  LIKE '%" + emp + "%'  OR a.PinYin LIKE '%," + emp.toLowerCase() + ",%')";
			}
			if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
				sql = "SELECT a.No,a.Name || '/' || b.name as Name FROM Port_Emp a,Port_Dept b  WHERE  (a.fk_dept=b.no) and (a.No like '%"
						+ emp + "%' OR a.NAME  LIKE '%" + emp + "%'  OR a.PinYin LIKE '%," + emp.toLowerCase()
						+ ",%') and rownum<=12";
			}
			if (SystemConfig.getAppCenterDBType() == DBType.MySQL) {
				sql = "SELECT a.No,CONCAT(a.Name,'/',b.name) as Name FROM Port_Emp a,Port_Dept b  WHERE  (a.fk_dept=b.no) and (a.No like '%"
						+ emp + "%' OR a.NAME  LIKE '%" + emp + "%'  OR a.PinYin LIKE '%," + emp.toLowerCase()
						+ ",%') LIMIT 12";
			}
		} else {
			if (SystemConfig.getAppCenterDBType() == DBType.MSSQL) {
				sql = "SELECT TOP 12 a.No,a.Name +'/'+b.name as Name FROM Port_Emp a,Port_Dept b  WHERE  (a.fk_dept=b.no) and (a.No like '%"
						+ emp + "%' OR a.NAME  LIKE '%" + emp + "%')";
			}
			if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
				sql = "SELECT a.No,a.Name || '/' || b.name as Name FROM Port_Emp a,Port_Dept b  WHERE  (a.fk_dept=b.no) and (a.No like '%"
						+ emp + "%' OR a.NAME  LIKE '%" + emp + "%') and rownum<=12";
			}
			if (SystemConfig.getAppCenterDBType() == DBType.MySQL) {
				sql = "SELECT a.No,CONCAT(a.Name,'/',b.name) as Name FROM Port_Emp a,Port_Dept b  WHERE  (a.fk_dept=b.no) and (a.No like '%"
						+ emp + "%' OR a.NAME  LIKE '%" + emp + "%') LIMIT 12";
			}
		}

		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
			dt.Columns.get("No").ColumnName = "No";
			dt.Columns.get("NAME").ColumnName = "Name";
		}

		return BP.Tools.Json.ToJson(dt);
	}

	/*
	 * 保存并关闭
	 * 
	 * zhoupeng 2018.4.25重构.
	 */
	public final String HuiQian_SaveAndClose() throws Exception {
		// 生成变量.
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());

		if (gwf.getHuiQianTaskSta() == HuiQianTaskSta.HuiQianOver) {
			/* 只有一个人的情况下, 并且是会签完毕状态，就执行 */
			return "info@当前工作已经到您的待办理了,会签工作已经完成.";
		}

		if (gwf.getHuiQianTaskSta() == HuiQianTaskSta.None) {
			String mysql = "SELECT COUNT(WorkID) FROM WF_GenerWorkerList WHERE FK_Node=" + this.getFK_Node()
					+ " AND WorkID=" + this.getWorkID() + " AND (IsPass=0 OR IsPass=-1) AND FK_Emp!='"
					+ BP.Web.WebUser.getNo() + "'";
			if (DBAccess.RunSQLReturnValInt(mysql, 0) == 0)
				return "info@您没有设置会签人，请在文本框输入会签人，或者选择会签人。";
		}

		// 判断当前节点的会签类型.
		Node nd = new Node(gwf.getFK_Node());

		// 设置当前接单是会签的状态.
		gwf.setHuiQianTaskSta(HuiQianTaskSta.HuiQianing); // 设置为会签状态.
		gwf.setHuiQianZhuChiRen(WebUser.getNo());
		gwf.setHuiQianZhuChiRenName(WebUser.getName());

		// 改变了节点就把会签状态去掉.
		gwf.setHuiQianSendToNodeIDStr("");
		gwf.Update();

		// 求会签人.
		GenerWorkerLists gwfs = new GenerWorkerLists();
		gwfs.Retrieve(GenerWorkerListAttr.WorkID, gwf.getWorkID(), GenerWorkerListAttr.FK_Node, gwf.getFK_Node(),
				GenerWorkerListAttr.IsPass, 0);

		String empsOfHuiQian = "会签人:";
		for (GenerWorkerList item : gwfs.ToJavaList())
			empsOfHuiQian += item.getFK_Emp() + "," + item.getFK_EmpText() + ";";

		// 设置当前操作人员的状态.
		String sql = "UPDATE WF_GenerWorkerList SET IsPass=90 WHERE WorkID=" + this.getWorkID() + " AND FK_Node="
				+ this.getFK_Node() + " AND FK_Emp='" + WebUser.getNo() + "'";
		DBAccess.RunSQL(sql);

		// 恢复他的状态.
		sql = "UPDATE WF_GenerWorkerList SET IsPass=0 WHERE WorkID=" + this.getWorkID() + " AND FK_Node="
				+ this.getFK_Node() + " AND IsPass=-1";
		DBAccess.RunSQL(sql);

		// 删除以前执行的会签点,比如:该人多次执行会签，仅保留最后一个会签时间点.
		sql = "DELETE FROM ND" + Integer.parseInt(gwf.getFK_Flow()) + "Track WHERE WorkID=" + this.getWorkID()
				+ " AND ActionType=" + ActionType.HuiQian.getValue() + " AND NDFrom=" + this.getFK_Node();
		DBAccess.RunSQL(sql);

		// 执行会签,写入日志.
		// BP.WF.Dev2Interface.WriteTrack(gwf.getFK_Flow(), gwf.getFK_Node(),
		// workid, fid, msg, at, tag, cFlowInfo, optionMsg, empNoTo, empNameTo);
		// BP.WF.Dev2Interface.WriteTrack(gwf.getFK_Flow(),
		// gwf.getFK_Node(), gwf.getNodeName(), gwf.getWorkID(),
		// gwf.getFID(), empsOfHuiQian, ActionType.HuiQian, "执行会签", null);

		BP.WF.Dev2Interface.WriteTrack(gwf.getFK_Flow(), gwf.getFK_Node(), gwf.getWorkID(), gwf.getFID(), "执行会签",
				ActionType.HuiQian, "执行会签", "执行会签", "执行会签", WebUser.getNo(), WebUser.getName());

		String str = "";
		if (nd.getTodolistModel() == TodolistModel.TeamupGroupLeader) {
			/* 如果是组长模式. */
			str = "close@保存成功.\t\n该工作已经移动到会签列表中了,等到所有的人会签完毕后,就可以出现在待办列表里.";
			str += "\t\n如果您要增加或者移除会签人请到会签列表找到该记录,执行操作.";

			// 删除自己的意见，以防止其他人员看到.
			BP.WF.Dev2Interface.DeleteCheckInfo(gwf.getFK_Flow(), this.getWorkID(), gwf.getFK_Node());
			return str;
		}

		if (nd.getTodolistModel() == TodolistModel.Teamup) {
			int toNodeID = this.GetRequestValInt("ToNode");
			if (toNodeID == 0)
				return "Send@[" + nd.getName() + "]会签成功执行.";

			Node toND = new Node(toNodeID);
			// 如果到达的节点是按照接受人来选择,就转向接受人选择器.
			if (toND.getHisDeliveryWay() == DeliveryWay.BySelected)
				return "url@Accepter.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID="
						+ this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&ToNode=" + toNodeID;
			else
				return "Send@执行发送操作";
		}

		return str;
	}

	/// 通用人员选择器Init
	public String AccepterOfGener_Init() throws Exception {
		/* 获得上一次发送的人员列表. */
		int toNodeID = this.GetRequestValInt("ToNode");

		// 查询出来,已经选择的人员.
		SelectAccpers sas = new SelectAccpers();
		int i = sas.Retrieve(SelectAccperAttr.FK_Node, toNodeID, SelectAccperAttr.WorkID, this.getWorkID());
		if (i == 0) {
			// 获得最近的一个workid.
			String trackTable = "ND" + Integer.parseInt(this.getFK_Flow()) + "Track";
			String sql = "";
			if (SystemConfig.getAppCenterDBType() == DBType.MSSQL) {
				sql = "SELECT TOP 1 Tag,EmpTo FROM " + trackTable + " WHERE NDTo=" + toNodeID
						+ " AND (ActionType=0 OR ActionType=1) AND EmpFrom='" + WebUser.getNo()
						+ "' ORDER BY WorkID desc  ";
			} else if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
				sql = "SELECT * FROM (SELECT  Tag,EmpTo,WorkID FROM " + trackTable + " A WHERE A.NDFrom="
						+ this.getFK_Node() + " AND A.NDTo=" + toNodeID
						+ " AND (ActionType=1 OR ActionType=0) AND EmpFrom='" + WebUser.getNo()
						+ "' ORDER BY WorkID DESC ) WHERE ROWNUM =1";
			} else if (SystemConfig.getAppCenterDBType() == DBType.MySQL) {
				sql = "SELECT  Tag,EmpTo FROM " + trackTable + " A WHERE A.NDFrom=" + this.getFK_Node() + " AND A.NDTo="
						+ toNodeID + " AND (ActionType=1 or ActionType=0 ) ORDER BY WorkID  DESC limit 1";
			}

			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			if (dt.Rows.size() != 0) {
				String emps = dt.Rows.get(0).getValue("Tag").toString();
				if (emps == null || "".equals(emps))
					emps = dt.Rows.get(0).getValue("EmpTo").toString();

				if (emps.contains(",") == false)
					BP.WF.Dev2Interface.Node_AddNextStepAccepters(this.getWorkID(), toNodeID, emps, false);

			}
			if (dt.Rows.size() != 0)
				sas.Retrieve(SelectAccperAttr.FK_Node, toNodeID, SelectAccperAttr.WorkID, this.getWorkID());
		}

		return sas.ToJson();
	}

	/// <summary>
	/// 增加接收人.
	/// </summary>
	/// <returns></returns>
	public String AccepterOfGener_AddEmps() {
		try {
			// 到达的节点ID.
			int toNodeID = this.GetRequestValInt("ToNode");
			String emps = this.GetRequestVal("AddEmps");

			// 增加到里面去.s
			BP.WF.Dev2Interface.Node_AddNextStepAccepters(this.getWorkID(), toNodeID, emps, false);

			// 查询出来,已经选择的人员.
			SelectAccpers sas = new SelectAccpers();
			sas.Retrieve(SelectAccperAttr.FK_Node, toNodeID, SelectAccperAttr.WorkID, this.getWorkID());

			return sas.ToJson();
		} catch (Exception ex) {
			if (ex.getMessage().contains("INSERT") == true)
				return "err@人员名称重复,导致部分人员插入失败.";

			return "err@" + ex.getMessage();
		}
	}

	/// <summary>
	/// 删除.
	/// </summary>
	/// <returns></returns>
	public String AccepterOfGener_Delete() throws Exception {
		// 删除指定的人员.
		BP.DA.DBAccess.RunSQL("DELETE FROM WF_SelectAccper WHERE WorkID=" + this.getWorkID() + " AND FK_Emp='"
				+ this.getFK_Emp() + "'");

		int toNodeID = this.GetRequestValInt("ToNode");

		// 查询出来,已经选择的人员.
		SelectAccpers sas = new SelectAccpers();
		sas.Retrieve(SelectAccperAttr.FK_Node, toNodeID, SelectAccperAttr.WorkID, this.getWorkID());
		return sas.ToJson();
	}

	/// <summary>
	/// 执行发送.
	/// </summary>
	/// <returns></returns>
	public String AccepterOfGener_Send() throws Exception {

		int toNodeID = this.GetRequestValInt("ToNode");
		/* 仅仅设置一个,检查压入的人员个数. */
		String sql = "SELECT count(WorkID) as Num FROM WF_SelectAccper WHERE FK_Node=" + toNodeID + " AND WorkID="
				+ this.getWorkID() + " AND AccType=0";
		int num = DBAccess.RunSQLReturnValInt(sql, 0);
		if (num == 0)
			return "err@请设置选择的人员.";
		Selector sr = new Selector(toNodeID);
		if (sr.getIsSimpleSelector() == true) {
			if (num != 1)
				return "err@您只能选择一个接受人,请移除其他的接受人然后执行发送.";
		}

		SendReturnObjs objs = BP.WF.Dev2Interface.Node_SendWork(this.getFK_Flow() + "", this.getWorkID(), toNodeID,
				null);
		String strs = objs.ToMsgOfHtml();
		strs = strs.replace("@", "<br>@");
		return strs;

	}

	// 查询select集合
	public final String AccepterOfGener_SelectEmps() throws Exception {
		String sql = "";
		String emp = this.GetRequestVal("TB_Emps");
		boolean isPinYin = DBAccess.IsExitsTableCol("Port_Emp", "PinYin");
		if (isPinYin == true) {
			if (SystemConfig.getAppCenterDBType() == DBType.MSSQL) {
				sql = "SELECT TOP 12 a.No,a.Name +'/'+b.name as Name FROM Port_Emp a,Port_Dept b  WHERE  (a.fk_dept=b.no) and (a.No like '%"
						+ emp + "%' OR a.NAME  LIKE '%" + emp + "%'  OR a.PinYin LIKE '%," + emp.toLowerCase() + ",%')";
			}
			if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
				sql = "SELECT a.No,a.Name || '/' || b.name as Name FROM Port_Emp a,Port_Dept b  WHERE  (a.fk_dept=b.no) and (a.No like '%"
						+ emp + "%' OR a.NAME  LIKE '%" + emp + "%'  OR a.PinYin LIKE '%," + emp.toLowerCase()
						+ ",%') and rownum<=12";
			}
			if (SystemConfig.getAppCenterDBType() == DBType.MySQL) {
				sql = "SELECT a.No, CONCAT(a.Name,'/',b.name) as Name FROM Port_Emp a,Port_Dept b  WHERE  (a.fk_dept=b.no) and (a.No like '%"
						+ emp + "%' OR a.NAME  LIKE '%" + emp + "%'  OR a.PinYin LIKE '%," + emp.toLowerCase()
						+ ",%') LIMIT 12";
			}
		} else {
			if (SystemConfig.getAppCenterDBType() == DBType.MSSQL) {
				sql = "SELECT TOP 12 a.No,a.Name +'/'+b.name as Name FROM Port_Emp a,Port_Dept b  WHERE  (a.fk_dept=b.no) and (a.No like '%"
						+ emp + "%' OR a.NAME  LIKE '%" + emp + "%')";
			}
			if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
				sql = "SELECT a.No, a.Name || '/' || b.name as Name FROM Port_Emp a,Port_Dept b  WHERE  (a.fk_dept=b.no) and (a.No like '%"
						+ emp + "%' OR a.NAME  LIKE '%" + emp + "%') and rownum<=12";
			}
			if (SystemConfig.getAppCenterDBType() == DBType.MySQL) {
				sql = "SELECT a.No,CONCAT(a.Name,'/',b.name) as Name FROM Port_Emp a,Port_Dept b  WHERE  (a.fk_dept=b.no) and (a.No like '%"
						+ emp + "%' OR a.NAME  LIKE '%" + emp + "%') LIMIT 12";
			}
		}

		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
			dt.Columns.get("NO").ColumnName = "No";
			dt.Columns.get("NAME").ColumnName = "Name";
		}

		return BP.Tools.Json.ToJson(dt);
	}

	public final String DBTemplate_DeleteDBTemplate() throws Exception {
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());

		gwf.setParas_DBTemplate(false);

		gwf.Update();

		return "设置成功";
	}

	public final String DBTemplate_Init() throws Exception {
		DataSet ds = new DataSet();

		// 获取模版.
		String sql = "SELECT WorkID,Title,AtPara FROM WF_GenerWorkFlow WHERE FK_Flow='" + this.getFK_Flow()
				+ "' AND WFState=3 AND Starter='" + WebUser.getNo() + "' AND ATPARA LIKE '%@DBTemplate=1%'";
		DataTable dtTemplate = DBAccess.RunSQLReturnTable(sql);
		dtTemplate.TableName = "DBTemplate";
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {

			dtTemplate.Columns.get(0).ColumnName = "WorkID";
			dtTemplate.Columns.get(1).ColumnName = "Title";

		}

		// 把模版名称替换 title.
		for (DataRow dr : dtTemplate.Rows) {
			String str = dr.getValue(2).toString();
			BP.DA.AtPara ap = new AtPara(str);
			dr.setValue("Title", ap.GetValStrByKey("DBTemplateName"));
		}

		ds.Tables.add(dtTemplate);

		// 获取历史发起数据.
		if (SystemConfig.getAppCenterDBType() == DBType.MSSQL) {
			sql = "SELECT TOP 30 WorkID,Title FROM WF_GenerWorkFlow WHERE FK_Flow='" + this.getFK_Flow()
					+ "' AND WFState=3 AND Starter='" + WebUser.getNo()
					+ "' AND ATPARA NOT LIKE '%@DBTemplate=1%' ORDER BY RDT ";
		}

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
			sql = "SELECT WorkID,Title FROM WF_GenerWorkFlow WHERE FK_Flow='" + this.getFK_Flow()
					+ "' AND WFState=3 AND Starter='" + WebUser.getNo()
					+ "' AND ATPARA NOT LIKE '%@DBTemplate=1%' AND rownum<=30 ORDER BY RDT ";
		}

		if (SystemConfig.getAppCenterDBType() == DBType.MySQL) {
			sql = "SELECT WorkID,Title FROM WF_GenerWorkFlow WHERE FK_Flow='" + this.getFK_Flow()
					+ "' AND WFState=3 AND Starter='" + WebUser.getNo()
					+ "' AND ATPARA NOT LIKE '%@DBTemplate=1%' ORDER BY RDT LIMIT 30 ";
		}

		DataTable dtHistroy = DBAccess.RunSQLReturnTable(sql);
		dtHistroy.TableName = "History";
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {

			dtHistroy.Columns.get(0).ColumnName = "WorkID";
			dtHistroy.Columns.get(1).ColumnName = "Title";

		}
		ds.Tables.add(dtHistroy);

		// 转化为 json.
		return BP.Tools.Json.ToJson(ds);
	}

	public String DBTemplate_SaveAsDBTemplate() throws Exception {
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		gwf.setParas_DBTemplate(true);
		try {
			gwf.setParas_DBTemplateName(URLDecoder.decode(this.GetRequestVal("Title"), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			Log.DebugWriteError("WF_WorkOpt DBTemplate_SaveAsDBTemplate 参数decode报错");
		}
		gwf.Update();
		return "设置成功";
	}

	public final String DBTemplate_StartFlowAsWorkID() {
		return "设置成功";
	}

	/// #endregion 流程数据模版.

	public String Accepter_Save() throws Exception {
		try {
			// 求到达的节点.
			int toNodeID = 0;
			if (!this.GetRequestVal("ToNode").equals("0")) {
				toNodeID = Integer.parseInt(this.GetRequestVal("ToNode"));
			}

			if (toNodeID == 0) { // 没有就获得第一个节点.
				Node nd = new Node(this.getFK_Node());
				Nodes nds = nd.getHisToNodes();
				toNodeID = nds.get(0).GetValIntByKey("NodeID");
			}

			// 求发送到的人员.
			// string selectEmps = this.GetValFromFrmByKey("SelectEmps");
			String selectEmps = this.GetRequestVal("SelectEmps");
			selectEmps = selectEmps.replace(";", ",");

			// 保存接受人.
			BP.WF.Dev2Interface.Node_AddNextStepAccepters(this.getWorkID(), toNodeID, selectEmps, true);
			return "SaveOK@" + selectEmps;
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}

	/**
	 * 打包下载
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String Packup_Init() throws Exception {
		try {
			int nodeID = this.getFK_Node();
			if (this.getFK_Node() == 0) {
				GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
				nodeID = gwf.getFK_Node();
			}

			Node nd = new Node(nodeID);
			//树形表单方案单独打印
			if((nd.getHisFormType() == NodeFormType.SheetTree && nd.getHisPrintPDFModle() == 1) == false)
				return MakeForm2Html.MakeCCFormToPDF(nd,this.getWorkID(), this.getFK_Flow(),null,false,this.GetRequestVal("BasePath"));
			else{
				//获取该节点绑定的表单
				// 所有表单集合.
				MapDatas mds = new MapDatas();
				mds.RetrieveInSQL("SELECT FK_Frm FROM WF_FrmNode WHERE FK_Node=" + this.getFK_Node());
				return "info@"+BP.Tools.Json.ToJson(mds.ToDataTableField());
			}
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}
	
	/**
	 * 独立表单PDF打印
	 * @return
	 * @throws Exception 
	 */
	public String Packup_FromInit() throws Exception{
		try {
			int nodeID = this.getFK_Node();
			if (this.getFK_Node() == 0) {
				GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
				nodeID = gwf.getFK_Node();
			}
			Node nd = new Node(nodeID);
			return MakeForm2Html.MakeFormToPDF(this.GetRequestVal("FrmID"),this.GetRequestVal("FrmName"),nd,this.getWorkID(), this.getFK_Flow(),null,false,this.GetRequestVal("BasePath"));
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}
	
	/// <summary>
    /// 初始化
    /// </summary>
    /// <returns></returns>
    public String PrintDoc_Init() throws Exception
    {
    	 String sourceType = this.GetRequestVal("SourceType");
         String FK_MapData = "";
         Node nd =null;
         if (this.getFK_Node() != 0 && this.getFK_Node() != 9999)
         {
             nd = new Node(this.getFK_Node());

             if (nd.getHisFormType() == NodeFormType.SheetTree)
             {
                 //获取该节点绑定的表单
                 // 所有表单集合.
                 MapDatas mds = new MapDatas();
                 mds.RetrieveInSQL("SELECT FK_Frm FROM WF_FrmNode WHERE FK_Node=" + this.getFK_Node() + " AND FrmEnableRole !=5");
                 return "info@" + BP.Tools.Json.ToJson(mds.ToDataTableField());
             }

             FK_MapData = "ND" + this.getFK_Node();

             if (nd.getHisFormType() == NodeFormType.RefOneFrmTree)
                 FK_MapData = nd.getNodeFrmID();

             if (nd.getHisFormType() == NodeFormType.SDKForm || nd.getHisFormType() == NodeFormType.SelfForm)
             {
                 return "err@SDK表单、嵌入式表单暂时不支持打印功能";
             }
         }
         if (DataType.IsNullOrEmpty(sourceType) == false && sourceType.equals("Bill"))
             FK_MapData = this.GetRequestVal("FrmID");

         BillTemplates templetes = new BillTemplates();
         String billNo = this.GetRequestVal("FK_Bill");
         if (billNo == null)
             templetes.Retrieve(BillTemplateAttr.FK_MapData, FK_MapData);
         else
             templetes.Retrieve(BillTemplateAttr.FK_MapData, this.getFK_MapData(), BillTemplateAttr.No, billNo);

         if (templetes.size() == 0)
             return "err@当前节点上没有绑定单据模板。";

         if (templetes.size() == 1)
         {
             BillTemplate templete = (BillTemplate) templetes.get(0);

             //单据的打印
             if (DataType.IsNullOrEmpty(sourceType) == false && sourceType.equals("Bill"))
                 return PrintDoc_FormDoneIt(null, this.getWorkID(), this.getFID(), FK_MapData, templete);
             
             if (nd!=null && nd.getHisFormType() == NodeFormType.RefOneFrmTree)
            	 return PrintDoc_FormDoneIt(null, this.getWorkID(), this.getFID(), FK_MapData, templete);

             return PrintDoc_DoneIt(templete.getNo());
         }
         return templetes.ToJson();
    }
    /// <summary>
    /// 执行打印
    /// </summary>
    /// <returns></returns>
    public String PrintDoc_Done() throws Exception
    {
        String billTemplateNo = this.GetRequestVal("FK_Bill");
        return PrintDoc_DoneIt(billTemplateNo);
    }
    
    
    /// <summary>
    /// 打印pdf.
    /// </summary>
    /// <param name="func"></param>
    /// <returns></returns>
    public String PrintDoc_DoneIt(String billTemplateNo) throws Exception
    {
        if (billTemplateNo == null)
            billTemplateNo = this.GetRequestVal("FK_Bill");

        BillTemplate func = new BillTemplate(billTemplateNo);

        //如果不是 BillTemplateExcel 打印
        if (func.getTemplateFileModel() == TemplateFileModel.VSTOForExcel)
            return "url@httpccword://-fromccflow,App=BillTemplateExcel,TemplateNo=" + func.getNo() + ",WorkID=" + this.getWorkID() + ",FK_Flow=" + this.getFK_Flow() + ",FK_Node=" + this.getFK_Node() + ",UserNo=" + WebUser.getNo() + ",SID=" + WebUser.getSID() ;

        //如果不是 BillTemplateWord 打印
        if (func.getTemplateFileModel() == TemplateFileModel.VSTOForWord)
            return "url@httpccword://-fromccflow,App=BillTemplateWord,TemplateNo=" + func.getNo() + ",WorkID=" + this.getWorkID() + ",FK_Flow=" + this.getFK_Flow() + ",FK_Node=" + this.getFK_Node() + ",UserNo=" + WebUser.getNo() + ",SID=" + WebUser.getSID();

        String billInfo = "";
        Node nd = new Node(this.getFK_Node());
        Work wk = nd.getHisWork();
        wk.setOID(this.getWorkID());
        wk.RetrieveFromDBSources();

        String file = DataType.getCurrentYear() + "_" + WebUser.getFK_Dept() + "_" + func.getNo() + "_" + this.getWorkID() + ".doc";
        BP.Pub.RTFEngine rtf = new BP.Pub.RTFEngine();

        String[] paths;
        String path;
        long newWorkID = 0;
        try
        {
            //#region 单据变量.
            Bill bill = new Bill();
            bill.setMyPK( wk.getFID() + "_" + wk.getOID() + "_" + nd.getNodeID() + "_" + func.getNo());
            //#endregion

            //#region 生成单据
            rtf.getHisEns().clear();
            rtf.getEnsDataDtls().clear();
            if (func.getNodeID() != 0)
            {
                //把流程主表数据放入里面去.
                GEEntity ndxxRpt = new GEEntity("ND" + Integer.parseInt(nd.getFK_Flow()) + "Rpt");
                try
                {
                    ndxxRpt.setPKVal(this.getWorkID());
                    ndxxRpt.Retrieve();

                    newWorkID = this.getWorkID();
                }
                catch (Exception ex)
                {
                    if (this.getFID() > 0)
                    {
                        ndxxRpt.setPKVal(this.getFID());
                        ndxxRpt.Retrieve();

                        newWorkID = this.getFID();

                        wk = null;
                        wk = nd.getHisWork();
                        wk.setOID(this.getFID());
                        wk.RetrieveFromDBSources();
                    }
                    else
                    {
                        BP.WF.DTS.InitBillDir dir = new BP.WF.DTS.InitBillDir();
                        dir.Do();
                        path = BP.WF.Glo.getFlowFileBill() + DataType.getCurrentYear() + "\\" + WebUser.getFK_Dept() + "\\" + func.getNo() + "\\";
                        String msgErr = "@" + String.format("生成单据失败，请让管理员检查目录设置") + "[" + BP.WF.Glo.getFlowFileBill() + "]。@Err：" + ex.getMessage() + " @File=" + file + " @Path:" + path;
                        billInfo += "@<font color=red>" + msgErr + "</font>";
                        throw new Exception(msgErr + "@其它信息:" + ex.getMessage());
                    }
                }
                ndxxRpt.Copy(wk);

                //把数据赋值给wk. 有可能用户还没有执行流程检查，字段没有同步到 NDxxxRpt.
                if (ndxxRpt.getRow().size()> wk.getRow().size())
                    wk.setRow(ndxxRpt.getRow());

                rtf.HisGEEntity = wk;

                //加入他的明细表.
                List<Entities> al = wk.GetDtlsDatasOfList();
                for (Entities ens : al)
                    rtf.AddDtlEns(ens);

                //增加多附件数据
                FrmAttachments aths = wk.getHisFrmAttachments();
                for(FrmAttachment athDesc : aths.ToJavaList())
                {
                    FrmAttachmentDBs athDBs = new FrmAttachmentDBs();
                    if (athDBs.Retrieve(FrmAttachmentDBAttr.FK_FrmAttachment, athDesc.getMyPK(), FrmAttachmentDBAttr.RefPKVal, newWorkID, "RDT") == 0)
                        continue;

                    rtf.getEnsDataAths().put(athDesc.getNoOfObj(), athDBs);
                }

                //把审核日志表加入里面去.
                Paras ps = new BP.DA.Paras();
                ps.SQL = "SELECT * FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track WHERE ActionType=" + SystemConfig.getAppCenterDBVarStr() + "ActionType AND WorkID=" + SystemConfig.getAppCenterDBVarStr() + "WorkID";
                ps.Add(TrackAttr.ActionType, ActionType.WorkCheck.getValue());
                ps.Add(TrackAttr.WorkID, newWorkID);

                rtf.dtTrack=BP.DA.DBAccess.RunSQLReturnTable(ps);
            }

            paths = file.split("_");
            path = paths[0] + "/" + paths[1] + "/" + paths[2] + "/";

            String billUrl = "url@" + BP.WF.Glo.getCCFlowAppPath() + "DataUser/Bill/" + path + file;

            if (func.getHisBillFileType() == BillFileType.PDF)
                billUrl = billUrl.replace(".doc", ".pdf");

            path = BP.WF.Glo.getFlowFileBill() + DataType.getCurrentYear() + "\\" + WebUser.getFK_Dept() + "\\" + func.getNo() + "\\";
            File filepath = new File(path);
            if (filepath.exists() == false)
            	filepath.mkdirs();

            String tempFile = func.getTempFilePath();
            if (tempFile.contains(".rtf") == false)
                tempFile = tempFile + ".rtf";

            //用于扫描打印.
            String qrUrl = SystemConfig.getAppSettings().get("HostURL").toString() + "/WF/WorkOpt/PrintDocQRGuide.htm?MyPK=" + bill.getMyPK();
            rtf.MakeDoc(tempFile,path, file, qrUrl,false);
            //#endregion

            //#region 转化成pdf.
            if (func.getHisBillFileType() == BillFileType.PDF)
            {
                String rtfPath = path + file;
                String pdfPath = rtfPath.replace(".doc", ".pdf");
                try
                {
                    BP.WF.Glo.Rtf2PDF(rtfPath, pdfPath);
                }
                catch (Exception ex)
                {
                    return "err@" + ex.getMessage();
                }
            }
            //#endregion

            //#region 保存单据.

            bill.setFID(wk.getFID());
            bill.setWorkID(wk.getOID());
            bill.setFK_Node(wk.getNodeID());
            bill.setFK_Dept(WebUser.getFK_Dept());
            bill.setFK_Emp(WebUser.getNo());
            bill.setUrl(billUrl);
            bill.setRDT(DataType.getCurrentDataTime());
            bill.setFullPath( path + file);
            bill.setFK_NY(DataType.getCurrentYearMonth());
            bill.setFK_Flow(nd.getFK_Flow());
            bill.setEmps(rtf.HisGEEntity.GetValStrByKey("Emps"));
            bill.setFK_Starter(rtf.HisGEEntity.GetValStrByKey("Rec"));
            bill.setStartDT(rtf.HisGEEntity.GetValStrByKey("RDT"));
            bill.setTitle(rtf.HisGEEntity.GetValStrByKey("Title"));
            bill.setFK_Dept(rtf.HisGEEntity.GetValStrByKey("FK_Dept"));

            try
            {
                bill.Save();
            }catch(Exception e){
                bill.Update();
            }
            //#endregion

            //在线WebOffice打开
            if (func.getBillOpenModel() == BillOpenModel.WebOffice)
                return "url@../WebOffice/PrintOffice.htm?MyPK=" + bill.getMyPK();
            if(func.getTemplateFileModel() ==TemplateFileModel.RTF)
            	return billUrl.replace("url@", "url@rtf@");
            if(func.getTemplateFileModel() ==TemplateFileModel.RTF)
            	return billUrl.replace("url@", "url@word@");
            return billUrl;
        }
        catch (Exception ex)
        {
            BP.WF.DTS.InitBillDir dir = new BP.WF.DTS.InitBillDir();
            dir.Do();
            path = BP.WF.Glo.getFlowFileBill() + DataType.getCurrentYear() + "\\" + WebUser.getFK_Dept() + "\\" + func.getNo() + "\\";
            String msgErr = "@" + String.format("生成单据失败，请让管理员检查目录设置") + "[" + BP.WF.Glo.getFlowFileBill() + "]。@Err：" + ex.getMessage() + " @File=" + file + " @Path:" + path;
            return "err@<font color=red>" + msgErr + "</font>" + ex.getMessage();
        }
    }
    
    /**
     * 表单打印单据
     * @param nd
     * @param workID
     * @param fid
     * @param formID
     * @param func
     * @return
     * @throws Exception 
     */
    public String PrintDoc_FormDoneIt(Node nd, long workID, long fid, String formID, BillTemplate func) throws Exception
    {
        long pkval = workID;
        Work wk = null;
        String billInfo = "";
        if (nd != null)
        {
            FrmNode fn = new FrmNode();
            fn = new FrmNode(nd.getFK_Flow(), nd.getNodeID(), formID);
            //先判断解决方案
            if (fn != null && fn.getWhoIsPK() != WhoIsPK.OID)
            {
                if (fn.getWhoIsPK() == WhoIsPK.PWorkID)
                    pkval = this.getPWorkID();
                if (fn.getWhoIsPK() == WhoIsPK.FID)
                    pkval = fid;
            }
            wk = nd.getHisWork();
            wk.setOID(this.getWorkID());
            wk.RetrieveFromDBSources();
        }

        MapData mapData = new MapData(formID);

        String file = DataType.getCurrentYear() + "_" + WebUser.getFK_Dept() + "_" + func.getNo() + "_" + this.getWorkID() + ".doc";
        BP.Pub.RTFEngine rtf = new BP.Pub.RTFEngine();

        String[] paths;
        String path;
        long newWorkID = 0;
        try
        {
            //单据变量.
            Bill bill = new Bill();
            if (nd != null)
                bill.setMyPK(wk.getFID() + "_" + wk.getOID() + "_" + nd.getNodeID() + "_" + func.getNo());
            else
                bill.setMyPK(fid + "_" + workID + "_0_" + func.getNo());
           
            //生成单据
            rtf.getHisEns().clear();
            rtf.getEnsDataDtls().clear();
            if (DataType.IsNullOrEmpty(func.getFK_MapData()) == false)
            {
                //把流程主表数据放入里面去.
                GEEntity ndxxRpt = new GEEntity(formID);
                try
                {
                    ndxxRpt.setPKVal(pkval);
                    ndxxRpt.Retrieve();

                    newWorkID = pkval;
                }
                catch (Exception ex)
                {
                    if (this.getFID() > 0)
                    {
                        ndxxRpt.setPKVal(this.getFID());
                        ndxxRpt.Retrieve();

                        newWorkID=this.getFID();

                        wk = null;
                        wk = nd.getHisWork();
                        wk.setOID(this.getWorkID());
                        wk.RetrieveFromDBSources();
                    }
                    else
                    {
                        BP.WF.DTS.InitBillDir dir = new BP.WF.DTS.InitBillDir();
                        dir.Do();
                        path = BP.WF.Glo.getFlowFileBill() + DataType.getCurrentYear() + "\\" + WebUser.getFK_Dept() + "\\" + func.getNo() + "\\";
                        String msgErr = "@" + String.format("生成单据失败，请让管理员检查目录设置") + "[" + BP.WF.Glo.getFlowFileBill() + "]。@Err：" + ex.getMessage() + " @File=" + file + " @Path:" + path;
                        billInfo += "@<font color=red>" + msgErr + "</font>";
                       
                        throw new Exception(msgErr + "@其它信息:" + ex.getMessage());
                    }
                }
                //ndxxRpt.Copy(wk);

                //把数据赋值给wk. 有可能用户还没有执行流程检查，字段没有同步到 NDxxxRpt.
                //if (ndxxRpt.Row.Count > wk.Row.Count)
                //   wk.Row = ndxxRpt.Row;

                rtf.HisGEEntity = ndxxRpt;

                //加入他的明细表.
                List<Entities> al = mapData.GetDtlsDatasOfList(String.valueOf(pkval));
                if (al.size() == 0)
                {
                    MapDtls mapdtls = mapData.getMapDtls();
                    for(MapDtl dtl : mapdtls.ToJavaList())
                    {
                        GEDtls dtls1 = new GEDtls(dtl.getNo());
                        mapData.getEnMap().AddDtl(dtls1, "RefPK");

                    }
                    al = mapData.GetDtlsDatasOfList(String.valueOf(pkval));
                }
                for(Entities ens : al)
                    rtf.AddDtlEns(ens);

                //增加多附件数据
                FrmAttachments aths = mapData.getFrmAttachments();
                for(FrmAttachment athDesc : aths.ToJavaList())
                {
                    FrmAttachmentDBs athDBs = new FrmAttachmentDBs();
                    if (athDBs.Retrieve(FrmAttachmentDBAttr.FK_FrmAttachment, athDesc.getMyPK(), FrmAttachmentDBAttr.RefPKVal, newWorkID, "RDT") == 0)
                        continue;

                    rtf.getEnsDataAths().put(athDesc.getNoOfObj(), athDBs);
                }

                if (nd != null)
                {
                    //把审核日志表加入里面去.
                    Paras ps = new BP.DA.Paras();
                    ps.SQL = "SELECT * FROM ND" + Integer.parseInt(nd.getFK_Flow()) + "Track WHERE ActionType=" + SystemConfig.getAppCenterDBVarStr() + "ActionType AND WorkID=" + SystemConfig.getAppCenterDBVarStr() + "WorkID";
                    ps.Add(TrackAttr.ActionType, ActionType.WorkCheck.getValue());
                    ps.Add(TrackAttr.WorkID, newWorkID);

                    rtf.dtTrack = BP.DA.DBAccess.RunSQLReturnTable(ps);
                }
            }

            paths = file.split("_");
            path = paths[0] + "/" + paths[1] + "/" + paths[2] + "/";

            String fileModelT = "rtf";
            if (func.getTemplateFileModel().getValue() == 1)
                fileModelT = "word";
            
            String billUrl = "url@" + fileModelT + "@" + BP.WF.Glo.getCCFlowAppPath() + "DataUser/Bill/" + path + file;
            
            if (func.getHisBillFileType() == BillFileType.PDF)
                billUrl = billUrl.replace(".doc", ".pdf");

            path = BP.WF.Glo.getFlowFileBill() + DataType.getCurrentYear() + "\\" + WebUser.getFK_Dept() + "\\" + func.getNo() + "\\";
            File filepath = new File(path);
            if (filepath.exists() == false)
            	filepath.mkdirs();

            String tempFile = func.getTempFilePath();
            if (tempFile.contains(".rtf") == false)
                tempFile = tempFile + ".rtf";

            //用于扫描打印.
            String qrUrl = SystemConfig.getAppSettings().get("HostURL").toString() + "/WF/WorkOpt/PrintDocQRGuide.htm?MyPK=" + bill.getMyPK();
            rtf.MakeDoc(tempFile,path, file, qrUrl,false);
            
            //转化成pdf.
            if (func.getHisBillFileType() == BillFileType.PDF)
            {
                String rtfPath = path + file;
                String pdfPath = rtfPath.replace(".doc", ".pdf");
                try
                {
                    BP.WF.Glo.Rtf2PDF(rtfPath, pdfPath);
                }
                catch (Exception ex)
                {
                    return "err@" + ex.getMessage();
                }
            }
           

            //保存单据.
            if (nd != null)
            {
                bill.setFID(wk.getFID());
                bill.setWorkID(wk.getOID());
                bill.setFK_Node(wk.getNodeID());
                bill.setFK_Flow(nd.getFK_Flow());
            }
            else
            {
                bill.setFID(fid);
                bill.setWorkID(workID);
                bill.setFK_Node(0);
                bill.setFK_Flow("0");
            }
            bill.setFK_Dept(WebUser.getFK_Dept());
            bill.setFK_Emp(WebUser.getNo());
            bill.setUrl(billUrl);
            bill.setRDT(DataType.getCurrentDataTime());
            bill.setFullPath( path + file);
            bill.setFK_NY(DataType.getCurrentYearMonth());
           
            bill.setEmps(rtf.HisGEEntity.GetValStrByKey("Emps"));
            bill.setFK_Starter(rtf.HisGEEntity.GetValStrByKey("Rec"));
            bill.setStartDT(rtf.HisGEEntity.GetValStrByKey("RDT"));
            bill.setTitle(rtf.HisGEEntity.GetValStrByKey("Title"));
            bill.setFK_Dept(rtf.HisGEEntity.GetValStrByKey("FK_Dept"));


            try
            {
                bill.Save();
            }
            catch(Exception e)
            {
                bill.Update();
            }
           

            //在线WebOffice打开
            if (func.getBillOpenModel() == BillOpenModel.WebOffice)
            {
                return "err@【/WF/WebOffice/PrintOffice.aspx】该文件没有重构好,您可以找到旧版本解决，或者自己开发。";
            }
            return billUrl;
        }
        catch (Exception ex)
        {
        	 BP.WF.DTS.InitBillDir dir = new BP.WF.DTS.InitBillDir();
             dir.Do();
             path = BP.WF.Glo.getFlowFileBill() + DataType.getCurrentYear() + "\\" + WebUser.getFK_Dept() + "\\" + func.getNo() + "\\";
             String msgErr = "@" + String.format("生成单据失败，请让管理员检查目录设置") + "[" + BP.WF.Glo.getFlowFileBill() + "]。@Err：" + ex.getMessage() + " @File=" + file + " @Path:" + path;
             return "err@<font color=red>" + msgErr + "</font>" + ex.getMessage();
        }
    }

	// #region 单选按钮事件
	/// <summary>
	/// 返回信息。
	/// </summary>
	/// <returns></returns>
	public String RadioBtns_Init() throws Exception {
		DataSet ds = new DataSet();

		// 放入表单字段.
		MapAttrs attrs = new MapAttrs(this.getFK_MapData());
		ds.Tables.add(attrs.ToDataTableField("Sys_MapAttr"));

		// 属性.
		MapAttr attr = new MapAttr();
		attr.setMyPK(this.getFK_MapData() + "_" + this.getKeyOfEn());
		attr.Retrieve();

		// 把分组加入里面.
		GroupFields gfs = new GroupFields(this.getFK_MapData());
		ds.Tables.add(gfs.ToDataTableField("Sys_GroupFields"));

		// 字段值.
		FrmRBs rbs = new FrmRBs();
		int num = rbs.Retrieve(FrmRBAttr.FK_MapData, this.getFK_MapData(), FrmRBAttr.KeyOfEn, this.getKeyOfEn());
		if (num == 0) {
			/*
			 * 初始枚举值变化.
			 */
			SysEnums ses = new SysEnums(attr.getUIBindKey());
			for (SysEnum se : ses.ToJavaList()) {
				FrmRB rb = new FrmRB();
				// rb.getFK_MapData() =
				rb.setFK_MapData(this.getFK_MapData());
				rb.setKeyOfEn(this.getKeyOfEn());
				rb.setIntKey(se.getIntKey());
				rb.setLab(se.getLab());
				rb.setEnumKey(attr.getUIBindKey());
				rb.Insert(); // 插入数据.
			}

			rbs.Retrieve(FrmRBAttr.FK_MapData, this.getFK_MapData(), FrmRBAttr.KeyOfEn, this.getKeyOfEn());
		}

		// 加入单选按钮.
		ds.Tables.add(rbs.ToDataTableField("Sys_FrmRB"));
		return BP.Tools.Json.ToJson(ds);
	}

	/// <summary>
	/// 执行保存
	/// </summary>
	/// <returns></returns>
	public String RadioBtns_Save() throws Exception {
		String json = this.GetRequestVal("data");

		DataTable dt = BP.Tools.Json.ToDataTable(json);

		for (DataRow dr : dt.Rows) {
			FrmRB rb = new FrmRB();
			rb.setMyPK(dr.getValue("MyPK").toString());
			rb.Retrieve();

			rb.setScript(dr.getValue("Script").toString());
			rb.setFieldsCfg(dr.getValue("FieldsCfg").toString()); // 格式为
																	// @字段名1=1@字段名2=0
			rb.setTip(dr.getValue("Tip").toString()); // 提示信息
			rb.Update();
		}

		return "保存成功.";
	}
	
	
	  /// <summary>
    /// 扫描二维码获得文件.
    /// </summary>
    /// <returns></returns>
    public String PrintDocQRGuide_Init()
    {
        try
        {
            int nodeID = this.getFK_Node();
            if (this.getFK_Node() == 0)
            {
                GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
                nodeID = gwf.getFK_Node();
            }

            Node nd = new Node(nodeID);
            return MakeForm2Html.MakeCCFormToPDF(nd,this.getWorkID(), this.getFK_Flow(),null,false,this.GetRequestVal("BasePath"));
        }
        catch (Exception ex)
        {
            return "err@" + ex.getMessage();
        }
    }
    /**
     * 流程流转自定义的设置初始化
     * @return
     * @throws Exception 
     */
   public String TransferCustom_Init() throws Exception{
	   DataSet ds = new DataSet();

       GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
       if (gwf.getTransferCustomType() != TransferCustomType.ByWorkerSet)
       {
           gwf.setTransferCustomType(TransferCustomType.ByWorkerSet);
           gwf.Update();
       }

       ds.Tables.add(gwf.ToDataTableField("WF_GenerWorkFlow"));

       //当前运行到的节点
       Node currNode = new Node(gwf.getFK_Node());

       //所有的节点s.
       Nodes nds = new Nodes(this.getFK_Flow());

       //工作人员列表.已经走完的节点与人员.
       GenerWorkerLists gwls = new GenerWorkerLists(this.getWorkID());
	   GenerWorkerList gwln = (GenerWorkerList) gwls.GetEntityByKey(GenerWorkerListAttr.FK_Node, this.getFK_Node());
	   if (gwln == null){
		   gwln = new GenerWorkerList();
		   gwln.setFK_Node(currNode.getNodeID());
		   gwln.setFK_NodeText(currNode.getName());
		   gwln.setFK_Emp(WebUser.getNo());
		   gwln.setFK_EmpText(WebUser.getName());
		   gwls.AddEntity(gwln);
	   }
       ds.Tables.add(gwls.ToDataTableField("WF_GenerWorkerList"));

       //设置的手工运行的流转信息.
       TransferCustoms tcs = new TransferCustoms(this.getWorkID());
       if (tcs.size() == 0)
       {
           // begin执行计算未来处理人.

           Work wk = currNode.getHisWork();
           wk.setOID(this.getWorkID());
           wk.Retrieve();
           WorkNode wn = new WorkNode(wk, currNode);
           wn.getHisFlow().setIsFullSA(true);
           //执行计算未来处理人.
           FullSA fsa = new FullSA(wn);
           // end 执行计算未来处理人.

           for(Node nd : nds.ToJavaList())
           {
           		if(nd.getNodeID() == this.getFK_Node())
           			continue;
        	   GenerWorkerList gwl = (GenerWorkerList) gwls.GetEntityByKey(GenerWorkerListAttr.FK_Node, nd.getNodeID());
               if (gwl == null)
               {
                   /*说明没有 */
                   TransferCustom tc = new TransferCustom();
                   tc.setWorkID(this.getWorkID());
                   tc.setFK_Node(nd.getNodeID());
                   tc.setNodeName(nd.getName());

                   //begin计算出来当前节点的工作人员.
                   SelectAccpers sas = new SelectAccpers();
                   sas.Retrieve(SelectAccperAttr.WorkID, this.getWorkID(), SelectAccperAttr.FK_Node, nd.getNodeID());

                   String workerID = "";
                   String workerName = "";
                   for(SelectAccper sa : sas.ToJavaList())
                   {
                       workerID += sa.getFK_Emp() + ",";
                       workerName += sa.getEmpName() + ",";
                   }
                   //end 计算出来当前节点的工作人员.

                   tc.setWorker(workerID);
                   tc.setWorkerName(workerName);
                   tc.setIdx(nd.getStep());
                   tc.setIsEnable(true);
                   tc.Insert();
               }
           }
           tcs = new TransferCustoms(this.getWorkID());
       }

       ds.Tables.add(tcs.ToDataTableField("WF_TransferCustoms"));

       return BP.Tools.Json.ToJson(ds);
   }
   
   /**
    * 节点备注初始化
    * @return
    */
   public String Note_Init()
   {
       Paras ps = new Paras();
       ps.SQL = "SELECT * FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track WHERE ActionType=" + BP.Sys.SystemConfig.getAppCenterDBVarStr() + "ActionType AND WorkID=" + BP.Sys.SystemConfig.getAppCenterDBVarStr() + "WorkID";
       ps.Add("ActionType", BP.WF.ActionType.FlowBBS.getValue());
       ps.Add("WorkID", this.getWorkID());

       //转化成json
       return BP.Tools.Json.ToJson(BP.DA.DBAccess.RunSQLReturnTable(ps));
   }

   /// <summary>
   /// 保存备注.
   /// </summary>
   /// <returns></returns>
   public String Note_Save() throws Exception
   {
       String msg = this.GetRequestVal("Msg");
       //增加track
       Node nd = new Node(this.getFK_Node());
       BP.WF.Glo.AddToTrack(ActionType.FlowBBS, this.getFK_Flow(), this.getWorkID(), this.getFID(), nd.getNodeID(), nd.getName(),  WebUser.getNo(), WebUser.getName(),nd.getNodeID(), nd.getName(), WebUser.getNo(), WebUser.getName(), msg, null);
      
       //发送消息
       String empsStrs = DBAccess.RunSQLReturnStringIsNull("SELECT Emps FROM WF_GenerWorkFlow WHERE WorkID=" + this.getWorkID(), "");
       String[] emps = empsStrs.split("@");
       //标题
       GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
       String title = "流程名称为"+gwf.getFlowName()+"标题为"+gwf.getTitle()+"在节点增加备注说明"+msg;
      
       for(String emp : emps)
       {
           if (DataType.IsNullOrEmpty(emp))
               continue;
           //获得当前人的邮件.
           BP.WF.Port.WFEmp empEn = new BP.WF.Port.WFEmp(emp);

           BP.WF.Dev2Interface.Port_SendMsg(empEn.getNo(), title,msg, null,"NoteMessage",this.getFK_Flow(),this.getFK_Node(),this.getWorkID(),this.getFID());

       }
       return "保存成功";
   }

   //节点备注的设置
}
