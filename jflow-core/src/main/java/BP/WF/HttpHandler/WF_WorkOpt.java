package BP.WF.HttpHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;

import BP.DA.AtPara;
import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.DA.Log;
import BP.DA.Paras;
import BP.En.Attr;
import BP.En.Attrs;
import BP.En.QueryObject;
import BP.En.UIContralType;
import BP.Port.Emp;
import BP.Port.Emps;
import BP.Sys.FrmAttachment;
import BP.Sys.FrmAttachmentDB;
import BP.Sys.FrmAttachmentDBAttr;
import BP.Sys.FrmAttachmentDBs;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
import BP.WF.ActionType;
import BP.WF.CancelRole;
import BP.WF.Dev2Interface;
import BP.WF.DotNetToJavaStringHelper;
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
import BP.WF.Track;
import BP.WF.Tracks;
import BP.WF.Work;
import BP.WF.WorkCheck;
import BP.WF.HttpHandler.Base.WebContralBase;
import BP.WF.Template.BtnLab;
import BP.WF.Template.CCSta;
import BP.WF.Template.FWCOrderModel;
import BP.WF.Template.FWCType;
import BP.WF.Template.FrmWorkCheck;
import BP.WF.Template.FrmWorkCheckSta;
import BP.WF.Template.FrmWorkChecks;
import BP.WF.Template.NodeAttr;
import BP.WF.Template.SelectAccperAttr;
import BP.WF.Template.SelectAccpers;
import BP.WF.Template.Selector;
import BP.WF.Template.SelectorModel;
import BP.Web.WebUser;

public class WF_WorkOpt extends WebContralBase {

	/**
	 * 初始化函数
	 * 
	 * @param mycontext
	 */
	public WF_WorkOpt(HttpContext mycontext) {
		this.context = mycontext;
	}

	public WF_WorkOpt() {
	}

	public final String SelectEmps_Init() {
		String fk_dept = this.getFK_Dept();
		
		if (DataType.IsNullOrEmpty(fk_dept) == true 
				|| fk_dept.equals("undefined")==true ) {
			fk_dept = BP.Web.WebUser.getFK_Dept();
		}

		DataSet ds = new DataSet();

		String sql = "SELECT No,Name,ParentNo FROM Port_Dept WHERE No='" + fk_dept + "' OR ParentNo='" + fk_dept + "' ";
		DataTable dtDept = BP.DA.DBAccess.RunSQLReturnTable(sql);
		
		if (dtDept.Rows.size()==0)
		{
			 fk_dept=BP.Web.WebUser.getFK_Dept();
			 
			  sql = "SELECT No,Name,ParentNo FROM Port_Dept WHERE No='" + fk_dept + "' OR ParentNo='" + fk_dept + "' ";
			  dtDept = BP.DA.DBAccess.RunSQLReturnTable(sql);
			
			//return "err@部门编号错误:"+fk_dept;
		}
		
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
			dtEmps.Columns.get(0).ColumnName = "No";
			dtEmps.Columns.get(1).ColumnName = "Name";
			dtEmps.Columns.get(2).ColumnName = "FK_Dept";
		}

		// 转化为 json
		return BP.Tools.Json.DataSetToJson(ds, false);
	}

	/**
	 * 初始化审核组件数据.
	 * 
	 * @return
	 */
	public final String WorkCheck_Init() {
		FrmWorkCheck wcDesc = new FrmWorkCheck(this.getFK_Node());
		FrmWorkCheck frmWorkCheck = null;
		FrmAttachmentDBs athDBs = null;
		Nodes nds = new Nodes(this.getFK_Flow());
		FrmWorkChecks fwcs = new FrmWorkChecks();
		Node nd = null;
		WorkCheck wc = null;
		Tracks tks = null;
		Track tkDoc = null;
		String nodes = "";
		boolean isCanDo = false;
		boolean isExitTb_doc = true;
		DataSet ds = new DataSet();
		DataRow row = null;

		 //是不是只读?
        Boolean isReadonly = false;
        if (this.GetRequestVal("IsReadonly")!=null 
        		&& this.GetRequestVal("IsReadonly").equals("1") )
            isReadonly = true;
        

		java.util.HashMap<Integer, DataTable> nodeEmps = new java.util.HashMap<Integer, DataTable>(); // 节点id，接收人列表
		FrmWorkCheck fwc = null;
		DataTable dt = null;
		int idx = 0;
		int noneEmpIdx = 0;

		fwcs.Retrieve(NodeAttr.FK_Flow, this.getFK_Flow(), NodeAttr.Step);
		ds.Tables.add(wcDesc.ToDataTableField("wcDesc"));

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
		ds.Tables.add(tkDt);

		DataTable athDt = new DataTable("Aths");
		athDt.Columns.Add("NodeID", Integer.class);
		athDt.Columns.Add("MyPK", String.class);
		athDt.Columns.Add("Href", String.class);
		athDt.Columns.Add("FileName", String.class);
		athDt.Columns.Add("FileExts", String.class);
		athDt.Columns.Add("CanDelete", Boolean.class);
		ds.Tables.add(athDt);

		if (this.getFID() != 0) {
			wc = new WorkCheck(this.getFK_Flow(), this.getFK_Node(), this.getFID(), 0);
		} else {
			wc = new WorkCheck(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), this.getFID());
		}

		isCanDo = BP.WF.Dev2Interface.Flow_IsCanDoCurrentWork(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(),
				BP.Web.WebUser.getNo());
		// 历史审核信息显示
		if (wcDesc.getFWCListEnable()) {
			tks = wc.getHisWorkChecks();

			// 已走过节点
			int empIdx = 0;
			int lastNodeId = 0;
			for (BP.WF.Track tk : tks.ToJavaList()) {
				if (tk.getHisActionType() == ActionType.FlowBBS) {
					continue;
				}

				if (lastNodeId == 0) {
					lastNodeId = tk.getNDFrom();
				}

				if (lastNodeId != tk.getNDFrom()) {
					idx++;
					lastNodeId = tk.getNDFrom();
				}

				tk.getRow().SetValByKey("T_NodeIndex", idx);

				Object tempVar = nds.GetEntityByKey(tk.getNDFrom());
				nd = (Node) ((tempVar instanceof Node) ? tempVar : null);

				Object tempVar2 = fwcs.GetEntityByKey(tk.getNDFrom());
				fwc = (FrmWorkCheck) ((tempVar2 instanceof FrmWorkCheck) ? tempVar2 : null);
				// 求出主键
				long pkVal = this.getWorkID();
				if (nd.getHisRunModel() == RunModel.SubThread) {
					pkVal = this.getFID();
				}

				// 排序，结合人员表Idx进行排序
				if (fwc.getFWCOrderModel() == FWCOrderModel.SqlAccepter) {
					tk.getRow().SetValByKey("T_CheckIndex", DBAccess.RunSQLReturnValInt(
							String.format("SELECT Idx FROM Port_Emp WHERE No='%1$s'", tk.getEmpFrom()), 0));
					noneEmpIdx++;
				} else {
					tk.getRow().SetValByKey("T_CheckIndex", noneEmpIdx++);
				}

				switch (tk.getHisActionType()) {
				case WorkCheck:
				case StartChildenFlow:
					if (nodes.contains(tk.getNDFrom() + ",") == false) {
						nodes += tk.getNDFrom() + ",";
					}
					break;
				default:
					continue;
				}
			}

			for (Track tk : tks.ToJavaList()) {
				if (nodes.contains(tk.getNDFrom() + ",")) {
					if (tk.getHisActionType() != ActionType.WorkCheck
							&& tk.getHisActionType() != ActionType.StartChildenFlow) {
						continue;
					}

					row = tkDt.NewRow();
					row.setValue("NodeID", tk.getNDFrom());
					Object tempVar3 = nds.GetEntityByKey(tk.getNDFrom());
					row.setValue("NodeName", ((Node) ((tempVar3 instanceof Node) ? tempVar3 : null)).getFWCNodeName());
					row.setValue("IsDoc", false);
					row.setValue("ParentNode", 0);
					row.setValue("RDT", StringUtils.isEmpty((tk.getRDT())) ? ""
							: tk.getNDFrom() == tk.getNDTo() && StringUtils.isEmpty((tk.getMsg())) ? "" : tk.getRDT());
					row.setValue("T_NodeIndex", tk.getRow().GetValByKey("T_NodeIndex"));
					row.setValue("T_CheckIndex", tk.getRow().GetValByKey("T_CheckIndex"));

					if (isReadonly==false && tk.getEmpFrom() == WebUser.getNo()
							&& this.getFK_Node() == tk.getNDFrom() && isExitTb_doc
							&& (wcDesc.getHisFrmWorkCheckType() == FWCType.Check
									|| ((wcDesc.getHisFrmWorkCheckType() == FWCType.DailyLog
											|| wcDesc.getHisFrmWorkCheckType() == FWCType.WeekLog)
											&& dateFormatter(new java.util.Date(java.util.Date.parse(tk.getRDT())),
													"yyyy-MM-dd")
															.equals(dateFormatter(new java.util.Date(), "yyyy-MM-dd")))
									|| (wcDesc.getHisFrmWorkCheckType() == FWCType.MonthLog
											&& dateFormatter(new java.util.Date(java.util.Date.parse(tk.getRDT())),
													"yyyy-MM")
															.equals(dateFormatter(new java.util.Date(), "yyyy-MM"))))) {
						boolean isLast = true;
						for (Track tk1 : tks.ToJavaList()) {
							if (tk1.getHisActionType() == tk.getHisActionType() && tk1.getNDFrom() == tk.getNDFrom()
									&& tk1.getRDT().compareTo(tk.getRDT()) > 0) {
								isLast = false;
								break;
							}
						}

						if (isLast) {
							isExitTb_doc = false;
							row.setValue("IsDoc", true);
							row.setValue("Msg",
									((Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(),
											this.getFK_Node())) != null)
													? Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(),
															this.getFK_Node())
													: "");
							;
							tkDoc = tk;

							// 增加默认审核意见
							if (StringUtils.isEmpty((row.getValue("Msg")).toString()) && wcDesc.getFWCIsFullInfo()) {
								row.setValue("Msg", wcDesc.getFWCDefInfo());
							}
						} else {
							row.setValue("Msg", tk.getMsgHtml());
						}
					} else {
						row.setValue("Msg", tk.getMsgHtml());
					}

					row.setValue("EmpFrom", tk.getEmpFrom());
					row.setValue("EmpFromT", tk.getEmpFromT());

					tkDt.Rows.add(row);

					/// #region //审核组件附件数据
					athDBs = new FrmAttachmentDBs();
					QueryObject obj_Ath = new QueryObject(athDBs);
					obj_Ath.AddWhere(FrmAttachmentDBAttr.FK_FrmAttachment, tk.getNDFrom() + "_FrmWorkCheck");
					obj_Ath.addAnd();
					obj_Ath.AddWhere(FrmAttachmentDBAttr.RefPKVal, this.getWorkID());
					obj_Ath.addOrderBy(FrmAttachmentDBAttr.RDT);
					obj_Ath.DoQuery();

					for (FrmAttachmentDB athDB : athDBs.ToJavaList()) {
						row = athDt.NewRow();

						row.setValue("NodeID", tk.getNDFrom());
						row.setValue("MyPK", athDB.getMyPK());
						row.setValue("Href", GetFileAction(athDB));
						row.setValue("FileName", athDB.getFileName());
						row.setValue("FileExts", athDB.getFileExts());
						row.setValue("CanDelete", (this.getFK_Node() + "").equals(athDB.getFK_MapData())
								&& athDB.getRec() == WebUser.getNo() && isReadonly==false);

						athDt.Rows.add(row);
					}

					/// #endregion

					/// #region //子流程的审核组件数据
					if (tk.getFID() != 0 && tk.getHisActionType() == ActionType.StartChildenFlow
							&& tkDt.Select("ParentNode=" + tk.getNDFrom()).length == 0) {
						String[] paras = tk.getTag().split("[@]", -1);
						String[] p1 = paras[1].split("[=]", -1);
						String fk_flow = p1[1]; // 子流程编号

						String[] p2 = paras[2].split("[=]", -1);
						String workId = p2[1]; // 子流程ID.
						int biaoji = 0;

						WorkCheck subwc = new WorkCheck(fk_flow, Integer.parseInt(fk_flow + "01"),
								Long.parseLong(workId), 0);

						Tracks subtks = subwc.getHisWorkChecks();
						// 取出来子流程的所有的节点。
						Nodes subNds = new Nodes(fk_flow);
						for (Node item : subNds.ToJavaList()) // 主要按顺序显示
						{
							for (Track mysubtk : subtks.ToJavaList()) {
								if (item.getNodeID() != mysubtk.getNDFrom()) {
									continue;
								}

								// 输出该子流程的审核信息，应该考虑子流程的子流程信息, 就不考虑那样复杂了.
								if (mysubtk.getHisActionType() == ActionType.WorkCheck) {
									// 发起多个子流程时，发起人只显示一次
									if (mysubtk.getNDFrom() == Integer.parseInt(fk_flow + "01") && biaoji == 1) {
										continue;
									}

									row = tkDt.NewRow();
									row.setValue("NodeID", mysubtk.getNDFrom());
									row.setValue("NodeName", String.format("(子流程)%1$s", mysubtk.getNDFromT()));
									row.setValue("Msg", mysubtk.getMsgHtml());
									row.setValue("EmpFrom", mysubtk.getEmpFrom());
									row.setValue("EmpFromT", mysubtk.getEmpFromT());
									row.setValue("RDT", (mysubtk.getRDT() != null) ? mysubtk.getRDT() : "");
									row.setValue("IsDoc", false);
									row.setValue("ParentNode", tk.getNDFrom());
									row.setValue("T_NodeIndex", idx++);
									row.setValue("T_CheckIndex", noneEmpIdx++);
									tkDt.Rows.add(row);

									if (mysubtk.getNDFrom() == Integer.parseInt(fk_flow + "01")) {
										biaoji = 1;
									}
								}
							}
						}
					}

					/// #endregion

					// todo:抄送暂未处理，不明逻辑
					continue;
				}

				// 判断是否显示所有步骤
				if (wcDesc.getFWCIsShowAllStep() == false) {
					continue;
				}

				// todo:抄送暂未处理，不明逻辑
			}

			if (tkDoc != null) {
				// 判断可编辑审核信息是否处于最后一条，不处于最后一条，则将其移到最后一条
				DataRow rdoc = tkDt.Select("IsDoc=True")[0];
				if (tkDt.Rows.indexOf(rdoc) != tkDt.Rows.size() - 1) {
					//// tkDt.Rows.add(rdoc.getItemArray())["RDT"] = "";
					rdoc.setValue("RDT", "");
					tkDt.Rows.add(rdoc);

					rdoc.setValue("IsDoc", false);
					rdoc.setValue("RDT", tkDoc.getRDT());
					rdoc.setValue("Msg", tkDoc.getMsgHtml());
				} else {
					// 判断刚退回时，退回接收人一打开，审核信息复制一条
					Track lastTrack = (Track) ((tks.get(tks.size() - 1) instanceof Track) ? tks.get(tks.size() - 1)
							: null);
					if ((lastTrack.getHisActionType() == ActionType.Return
							|| lastTrack.getHisActionType() == ActionType.Forward)
							&& lastTrack.getNDTo() == tkDoc.getNDFrom()) {
						//// tkDt.Rows.Add(rdoc.ItemArray)["RDT"] = "";
						rdoc.setValue("RDT", "");
						tkDt.Rows.add(rdoc);

						rdoc.setValue("IsDoc", false);
						rdoc.setValue("RDT", tkDoc.getRDT());
						rdoc.setValue("Msg", tkDoc.getMsgHtml());
					}
				}
			}
		}

		// 审核意见填写
		if (isExitTb_doc && wcDesc.getHisFrmWorkCheckSta() == FrmWorkCheckSta.Enable && isCanDo
				&& isReadonly==false) {
			DataRow[] rows = null;
			Object tempVar4 = nds.GetEntityByKey(this.getFK_Node());
			nd = (Node) ((tempVar4 instanceof Node) ? tempVar4 : null);
			if (wcDesc.getFWCOrderModel() == FWCOrderModel.SqlAccepter) {
				rows = tkDt.Select("NodeID=" + this.getFK_Node() + " AND Msg='' AND EmpFrom='" + WebUser.getNo() + "'");

				if (rows.length == 0) {
					rows = tkDt.Select("NodeID=" + this.getFK_Node() + " AND EmpFrom='" + WebUser.getNo() + "'");// ,
																													// "RDT
																													// DESC"
				}

				if (rows.length > 0) {
					row = rows[0];
					row.setValue("IsDoc", true);
					row.setValue("Msg",
							((Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(),
									this.getFK_Node())) != null)
											? Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(),
													this.getFK_Node())
											: "");

					if (StringUtils.isEmpty(row.getValue("Msg")) || StringUtils
							.isEmpty(((row.getValue("Msg")) instanceof String) ? row.getValue("Msg") : null)) {
						row.setValue("RDT", "");
					}

					// 增加默认审核意见
					if (StringUtils.isEmpty((row.getValue("Msg").toString())) && wcDesc.getFWCIsFullInfo()) {
						row.setValue("Msg", wcDesc.getFWCDefInfo());
					}
				} else {
					row = tkDt.NewRow();
					row.setValue("NodeID", this.getFK_Node());
					row.setValue("NodeName", nd.getFWCNodeName());
					row.setValue("IsDoc", true);
					row.setValue("ParentNode", 0);
					row.setValue("RDT", "");
					row.setValue("Msg",
							((Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(),
									this.getFK_Node())) != null)
											? Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(),
													this.getFK_Node())
											: "");
					row.setValue("EmpFrom", WebUser.getNo());
					row.setValue("EmpFromT", WebUser.getName());
					row.setValue("T_NodeIndex", ++idx);
					row.setValue("T_CheckIndex", ++noneEmpIdx);

					// 增加默认审核意见
					if (StringUtils.isEmpty(row.getValue("Msg").toString()) && wcDesc.getFWCIsFullInfo()) {
						row.setValue("Msg", wcDesc.getFWCDefInfo());
					}

					tkDt.Rows.add(row);
				}
			} else {
				row = tkDt.NewRow();
				row.setValue("NodeID", this.getFK_Node());
				row.setValue("NodeName", nd.getFWCNodeName());
				row.setValue("IsDoc", true);
				row.setValue("ParentNode", 0);
				row.setValue("RDT", "");
				row.setValue("Msg",
						((Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(), this.getFK_Node())) != null)
								? Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(), this.getFK_Node())
								: "");
				row.setValue("EmpFrom", WebUser.getNo());
				row.setValue("EmpFromT", WebUser.getName());
				row.setValue("T_NodeIndex", ++idx);
				row.setValue("T_CheckIndex", ++noneEmpIdx);

				// 增加默认审核意见
				if (StringUtils.isEmpty(row.getValue("Msg").toString()) && wcDesc.getFWCIsFullInfo()) {
					row.setValue("Msg", wcDesc.getFWCDefInfo());
				}

				tkDt.Rows.add(row);
			}
		}

		/// #region 显示有审核组件，但还未审核的节点
		if (tks == null) {
			tks = wc.getHisWorkChecks();
		}

		for (FrmWorkCheck item : fwcs.ToJavaList()) {
			if (item.getFWCIsShowTruck() == false) {
				continue;
			}

			// 是否已审核
			boolean isHave = false;
			for (BP.WF.Track tk : tks.ToJavaList()) {
				//@于庆海翻译.
				if (tk.getNDFrom() == item.getNodeID() || tk.getNDTo() == item.getNodeID()) {
					isHave = true; //已经有了
					break;
				}
			}

			if (isHave == true) {
				continue;
			}

			row = tkDt.NewRow();
			row.setValue("NodeID", item.getNodeID());
			Object tempVar5 = nds.GetEntityByKey(item.getNodeID());
			row.setValue("NodeName", ((Node) ((tempVar5 instanceof Node) ? tempVar5 : null)).getFWCNodeName());
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

		/// #endregion 增加空白.

		//// DataView dv = tkDt.DefaultView;
		//// dv.Sort = "T_NodeIndex ASC,T_CheckIndex ASC";
		//// DataTable sortedTKs = dv.ToTable("Tracks");

		//// ds.Tables.remove("Tracks");
		//// ds.Tables.add(sortedTKs);

		return BP.Tools.Json.ToJson(ds);
	}

	/**
	 * 获取审核组件中刚上传的附件列表信息
	 * 
	 * @return
	 */
	public final String WorkCheck_GetNewUploadedAths() {
		DataRow row = null;
		String athNames = GetRequestVal("Names");
		String attachPK = GetRequestVal("AttachPK");

		DataTable athDt = new DataTable("Aths");
		athDt.Columns.Add("NodeID", Integer.class);
		athDt.Columns.Add("MyPK", String.class);
		athDt.Columns.Add("Href", String.class);
		athDt.Columns.Add("FileName", String.class);
		athDt.Columns.Add("FileExts", String.class);
		athDt.Columns.Add("CanDelete", String.class);

		FrmAttachmentDBs athDBs = new FrmAttachmentDBs();
		QueryObject obj_Ath = new QueryObject(athDBs);
		obj_Ath.AddWhere(FrmAttachmentDBAttr.FK_FrmAttachment, this.getFK_Node() + "_FrmWorkCheck");
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
			row.setValue("Href", GetFileAction(athDB));
			row.setValue("FileName", athDB.getFileName());
			row.setValue("FileExts", athDB.getFileExts());
			row.setValue("CanDelete", athDB.getRec() == WebUser.getNo());

			athDt.Rows.add(row);
		}

		return BP.Tools.Json.ToJson(athDt);
	}

	/**
	 * 获取附件链接
	 * 
	 * @param athDB
	 * @return
	 */
	private String GetFileAction(FrmAttachmentDB athDB) {
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
	 */
	public final String WorkCheck_Save() {
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
		if (DotNetToJavaStringHelper.isNullOrEmpty(wcDesc.getFWCFields()) == false) {
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
		} else {

			/// #region 根据类型写入数据 qin
			if (wcDesc.getHisFrmWorkCheckType() == FWCType.Check) // 审核组件
			{
				// 判断是否审核组件中"协作模式下操作员显示顺序"设置为"按照接受人员列表先后顺序(官职大小)"，删除原有的空审核信息
				if (wcDesc.getFWCOrderModel() == FWCOrderModel.SqlAccepter) {
					sql = "DELETE FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track WHERE WorkID = "
							+ this.getWorkID() + " AND ActionType = " + ActionType.WorkCheck.getValue()
							+ " AND NDFrom = " + this.getFK_Node() + " AND NDTo = " + this.getFK_Node()
							+ " AND EmpFrom = '" + WebUser.getNo() + "' AND (Msg='' OR Msg IS NULL)";
					DBAccess.RunSQL(sql);
				}

				Dev2Interface.WriteTrackWorkCheck(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), this.getFID(),
						msg, wcDesc.getFWCOpLabel());
			}
			if (wcDesc.getHisFrmWorkCheckType() == FWCType.DailyLog) // 日志组件
			{
				Dev2Interface.WriteTrackDailyLog(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), this.getFID(),
						msg, wcDesc.getFWCOpLabel());
			}
			if (wcDesc.getHisFrmWorkCheckType() == FWCType.WeekLog) // 周报
			{
				Dev2Interface.WriteTrackWeekLog(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), this.getFID(),
						msg, wcDesc.getFWCOpLabel());
			}
			if (wcDesc.getHisFrmWorkCheckType() == FWCType.MonthLog) // 月报
			{
				Dev2Interface.WriteTrackMonthLog(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), this.getFID(),
						msg, wcDesc.getFWCOpLabel());
			}

			/// #endregion
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

	public String CC_Init() {
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

	public String CC_SelectDepts() {
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

	public String CC_Send() {

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

	public String DealSubThreadReturnToHL_Init() {

		// 如果工作节点退回了
		BP.WF.ReturnWorks rws = new BP.WF.ReturnWorks();
		rws.Retrieve(BP.WF.ReturnWorkAttr.ReturnToNode, this.getFK_Node(), BP.WF.ReturnWorkAttr.WorkID,
				this.getWorkID(), BP.WF.ReturnWorkAttr.RDT);

		String msgInfo = "";
		if (rws.size() != 0) {
			for (BP.WF.ReturnWork rw : rws.ToJavaList()) {
				msgInfo += "<fieldset width='100%' ><legend>&nbsp; 来自节点:" + rw.getReturnNodeName() + " 退回人:"
						+ rw.getReturnerName() + "  " + rw.getRDT() + "&nbsp;<a href='./../../DataUser/ReturnLog/"
						+ this.getFK_Flow() + "/" + rw.getMyPK() + ".htm' target=_blank>工作日志</a></legend>";
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

	public String DealSubThreadReturnToHL_Done() {
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
	
	
	 public final String DeleteFlowInstance_Init()
		{
			try {
				if (BP.WF.Dev2Interface.Flow_IsCanDeleteFlowInstance(this.getFK_Flow(), this.getWorkID(), BP.Web.WebUser.getNo()) == false)
				{
					return "err@您没有删除该流程的权限";
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//获取节点中配置的流程删除规则
			if (this.getFK_Node() != 0)
			{
				String sql = "SELECT wn.DelEnable FROM WF_Node wn WHERE wn.NodeID = " + this.getFK_Node();
				return DBAccess.RunSQLReturnValInt(sql) + "";
			}

			return "";
		}

	public String DeleteFlowInstance_DoDelete() {

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

	public String ViewWorkNodeFrm() {
		// 获得节点表单数据.
		Node nd = new Node(this.getFK_Node());

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
		DataSet myds = BP.Sys.CCFormAPI.GenerHisDataSet(nd.getNodeFrmID(), false);
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

	public String AskForRe() {
		/// 签信息.
		String note = this.GetRequestVal("Note"); // 原因.
		return BP.WF.Dev2Interface.Node_AskforReply(this.getWorkID(), note);
	}

	public String Askfor() {
		// 执行加签
		long workID = Integer.parseInt(this.GetRequestVal("WorkID")); // 工作ID
		String toEmp = this.GetRequestVal("ToEmp"); // 让谁加签?
		String note = this.GetRequestVal("Note"); // 原因.
		String model = this.GetRequestVal("Model"); // 模式.

		BP.WF.AskforHelpSta sta = BP.WF.AskforHelpSta.AfterDealSend;
		if (model.equals("0")) {
			sta = BP.WF.AskforHelpSta.AfterDealSend;
		}

		return BP.WF.Dev2Interface.Node_Askfor(workID, sta, toEmp, note);
	}

	public String SelectEmps() {
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

		return BP.Tools.Json.DataSetToJson(ds, false);
	}

	public String Accepter_Init() {

		// 当前节点ID.
		Node nd = new Node(this.getFK_Node());
		int toNodeID = this.GetRequestValInt("ToNode");
		if (toNodeID == 0) {
			Nodes nds = nd.getHisToNodes();
			if (nds.size() == 1) {
				toNodeID = nds.get(0).GetValIntByKey("NodeID");
			} else {
				return "err@参数错误,必须传递来到达的节点ID ToNode .";
			}
		}

		Work wk = nd.getHisWork();
		wk.setOID(this.getWorkID());
		wk.Retrieve();

		Selector select = new Selector(toNodeID);
		if (select.getSelectorModel() == SelectorModel.GenerUserSelecter) {
			return "url@AccepterOfGener.htm?WorkID=" + this.getWorkID() + "&FK_Node=" + this.getFK_Node() + "&FK_Flow="
					+ nd.getFK_Flow() + "&ToNode=" + toNodeID;
		}

		// 获得 部门与人员.
		DataSet ds = select.GenerDataSet(toNodeID, wk);

		/// #region 计算上一次选择的结果, 并把结果返回过去.
		String sql = "";
		DataTable dt = new DataTable();
		dt.Columns.Add("No", String.class);
		dt.TableName = "Selected";
		if (select.getIsAutoLoadEmps()) {
			if (SystemConfig.getAppCenterDBType() == DBType.MSSQL) {
				sql = "SELECT  top 1 Tag FROM ND" + Integer.parseInt(nd.getFK_Flow()) + "Track A WHERE A.NDFrom="
						+ this.getFK_Node() + " AND A.NDTo=" + toNodeID + " AND ActionType=1 ORDER BY WorkID DESC";
			} else if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
				sql = "SELECT  Tag FROM ND" + Integer.parseInt(nd.getFK_Flow()) + "Track A WHERE A.NDFrom="
						+ this.getFK_Node() + " AND A.NDTo=" + toNodeID
						+ " AND ActionType=1 AND ROWNUM =1  ORDER BY WorkID DESC ";
			} else if (SystemConfig.getAppCenterDBType() == DBType.MySQL) {
				sql = "SELECT  Tag FROM ND" + Integer.parseInt(nd.getFK_Flow()) + "Track A WHERE A.NDFrom="
						+ this.getFK_Node() + " AND A.NDTo=" + toNodeID
						+ " AND ActionType=1 AND  limit 1,1  ORDER BY WorkID  DESC";
			}

			String tag = DBAccess.RunSQLReturnStringIsNull(sql, "");

			String[] strs = tag.split("[;]", -1);
			for (String str : strs) {
				if (DotNetToJavaStringHelper.isNullOrEmpty(str) == true) {
					continue;
				}

				String[] emp = str.split("[,]", -1);
				if (emp.length != 2) {
					continue;
				}

				DataRow dr = dt.NewRow();
				dr.setValue(0, emp[0]);
				dt.Rows.add(dr);
			}
		}

		// 增加一个table.
		ds.Tables.add(dt);
		/// #endregion 计算上一次选择的结果, 并把结果返回过去.

		// 返回json.
		return BP.Tools.Json.DataSetToJson(ds, false);
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

	public String Accepter_Send() {
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

	public String Return_Init() {
		
		DataTable dt = BP.WF.Dev2Interface.DB_GenerWillReturnNodes(this.getFK_Node(), this.getWorkID(), this.getFID());

     	String str= BP.Tools.Json.ToJson(dt);
     //	String str2= BP.Tools.Json.ToJson(dt);		
			return str;
		
	}

	public String DoReturnWork() {

		try {

			int toNodeID = Integer.parseInt(this.GetRequestVal("ReturnToNode").split("@")[0]);

			// int toNodeID =
			// Integer.parseInt(this.GetRequestVal("ReturnToNode").split('@')[0]);
			String reMesage = this.GetRequestVal("ReturnInfo");

			boolean isBackBoolen = false;
			String isBack = this.GetRequestVal("IsBack");
			if (isBack.equals("1")) {
				isBackBoolen = true;
			}

			return BP.WF.Dev2Interface.Node_ReturnWork(this.getFK_Flow(), this.getWorkID(), this.getFID(),
					this.getFK_Node(), toNodeID, reMesage, isBackBoolen);
		} catch (Exception ex) {
			return "err@" + ex.getMessage();
		}
	}

	public String Shift() {
		String msg = this.GetRequestVal("Message");
		String toEmp = this.GetRequestVal("ToEmp");
		return BP.WF.Dev2Interface.Node_Shift(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), this.getFID(),
				toEmp, msg);
	}

	public String Allot() {
		String msg = this.GetRequestVal("Message");
		String toEmp = this.GetRequestVal("ToEmp");
		return BP.WF.Dev2Interface.Node_Allot(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), this.getFID(),
				toEmp, msg);
	}

	public String UnShift() {

		return BP.WF.Dev2Interface.Node_ShiftUn(this.getFK_Flow(), this.getWorkID());
	}

	public String Press() {

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
		return "err@没有此方法";
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
	 */
	public final String HuiQian_Init() {
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
	 */
	public final String HuiQian_Delete() {
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

		//如果已经没有会签待办了,就设置当前人员状态为0.  @于庆海翻译 增加这部分.
		String sql = "SELECT COUNT(WorkID) FROM WF_GenerWorkerList WHERE FK_Node=" + this.getFK_Node() + " AND WorkID='" + this.getWorkID() + "' AND IsPass=0";
		if (DBAccess.RunSQLReturnValInt(sql) == 0)
		{
			gwf.setHuiQianTaskSta(HuiQianTaskSta.HuiQianOver); //设置为会签状态.
			gwf.Update();

			DBAccess.RunSQL("UPDATE WF_GenerWorkerList SET IsPass=0 WHERE FK_Node=" + this.getFK_Node() + " AND WorkID=" + this.getWorkID() + " AND FK_Emp='" + WebUser.getNo() + "'");
		}
		
		
		//从待办里移除.
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
		if (gwf.getTodoEmps().contains(WebUser.getNo() + ",") == false) {
			return "err@您不是会签主持人，您不能执行该操作。";
		}

		GenerWorkerList gwlOfMe = new GenerWorkerList();
		int num = gwlOfMe.Retrieve(GenerWorkerListAttr.FK_Emp, WebUser.getNo(), GenerWorkerListAttr.WorkID,
				this.getWorkID(), GenerWorkerListAttr.FK_Node, this.getFK_Node());

		if (num == 0) {
			return "err@没有查询到当前人员的工作列表数据.";
		}

		// 是否有拼音字段？
		boolean isPinYin = DBAccess.IsExitsTableCol("Port_Emp", "PinYin");
		String sql = "";

		// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		/// #region 求人员集合.
		Emps emps = new Emps();
		String toEmpStrs = this.GetRequestVal("AddEmps");
		toEmpStrs = toEmpStrs.replace(",", ";");
		String[] toEmps = toEmpStrs.split("[;]", -1);
		String infos = "";
		for (String empStr : toEmps) {
			if (DotNetToJavaStringHelper.isNullOrEmpty(empStr) == true) {
				continue;
			}

			if (isPinYin == true) {
				sql = "SELECT No,Name FROM Port_Emp WHERE No='" + empStr + "' OR NAME ='" + empStr
						+ "'  OR PinYin LIKE '%," + empStr + ",%'";
			} else {
				sql = "SELECT No,Name FROM Port_Emp WHERE No='" + empStr + "' OR NAME ='" + empStr + "'";
			}

			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			if (dt.Rows.size() > 12 || dt.Rows.size() == 0) {
				continue;
			}

			for (DataRow dr : dt.Rows) {
				String empNo = dr.getValue(0).toString();
				String empName = dr.getValue(1).toString();

				// 查查是否存在队列里？
				num = gwlOfMe.Retrieve(GenerWorkerListAttr.FK_Emp, empNo, GenerWorkerListAttr.WorkID, this.getWorkID(),
						GenerWorkerListAttr.FK_Node, this.getFK_Node());

				if (num == 1) {
					infos += "\t\n@人员[" + empStr + "]已经在队列里.";
					continue;
				}

				// 增加到队列里面.
				emps.AddEntity(new Emp(empNo));
			}
		}

		if (!infos.equals("") && emps.size() != 0) {
			return "info@" + infos;
		}

		if (emps.size() == 0) {
			return "info@您没有选择人员, 执行信息:" + infos;
		}
		// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		/// #endregion 求人员集合.

		GenerWorkerLists gwls = new GenerWorkerLists();

		// 查询出来其他列的数据.
		gwlOfMe.Retrieve(GenerWorkerListAttr.FK_Emp, WebUser.getNo(), GenerWorkerListAttr.WorkID, this.getWorkID(),
				GenerWorkerListAttr.FK_Node, this.getFK_Node());

		// 遍历人员集合.
		for (Emp item : emps.ToJavaList()) {
			
			gwlOfMe.setFK_Emp(item.getNo());
			gwlOfMe.setFK_EmpText(item.getName());
			gwlOfMe.setIsPassInt(0);
			gwlOfMe.setFK_Dept(item.getFK_Dept());
			gwlOfMe.setFK_DeptT(item.getFK_DeptText()); // 部门名称.
			gwlOfMe.setIsRead(false);

			// gwlOfMe.FK_DeptT = item.FK_DeptText;

			gwlOfMe.Insert(); // 插入作为待办.
			infos += "\t\n@" + item.getNo() + "  " + item.getName();

			gwlOfMe.Retrieve();

			gwls.AddEntity(gwlOfMe);
		}

		// 赋值部门名称。
		DataTable mydt = gwls.ToDataTableField();
		mydt.Columns.Add("FK_DeptT", String.class);
		for (DataRow dr : mydt.Rows) {
			String fk_emp = dr.getValue("FK_Emp").toString();
			for (GenerWorkerList item : gwls.ToJavaList()) {
				if (fk_emp.equals(item.getFK_Emp())) {
					dr.setValue("FK_DeptT", item.getFK_DeptT());
				}
			}
		}

		return BP.Tools.Json.ToJson(mydt);
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

		return BP.Tools.Json.ToJson(dt);
	}

	
	
	  /** 
	 保存并关闭
	 
	 @return 
*/
	public final String HuiQian_SaveAndClose()
	{
		//生成变量.
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());

		//求会签人.
		GenerWorkerLists gwfs = new GenerWorkerLists();
		gwfs.Retrieve(GenerWorkerListAttr.WorkID, gwf.getWorkID(), GenerWorkerListAttr.FK_Node, gwf.getFK_Node(), GenerWorkerListAttr.IsPass, 0);

		if (gwfs.size() == 1 && gwf.getHuiQianTaskSta()== HuiQianTaskSta.HuiQianOver)
		{
			//只有一个人的情况下, 并且是会签完毕状态，就执行 
			return "当前工作已经到您的待办理了,会签工作已经完成.";
		}
		
		//说明没有会签人,就直接关闭.
        if (gwfs.size() == 1)
            return "您没有设置会签人，当前是待办状态。";
        

		gwf.setHuiQianTaskSta(HuiQianTaskSta.HuiQianing); //设置为会签状态.
		gwf.Update();

		String empsOfHuiQian = "会签人:";
		for (GenerWorkerList item : gwfs.ToJavaList())
		{
			empsOfHuiQian += item.getFK_Emp() + "," + item.getFK_EmpText() + ";";
		}

		//设置当前操作人员的状态.
		String sql = "UPDATE WF_GenerWorkerList SET IsPass=90 WHERE WorkID=" + this.getWorkID() + " AND FK_Node=" + this.getFK_Node() + " AND FK_Emp='" + WebUser.getNo() + "'";
		DBAccess.RunSQL(sql);

		  //删除以前执行的会签点,比如:该人多次执行会签，仅保留最后一个会签时间点.  @于庆海.
		sql = "DELETE FROM ND" + Integer.parseInt(gwf.getFK_Flow()) + "Track WHERE WorkID=" + this.WorkID + " AND ActionType=" + ActionType.HuiQian.getValue() + " AND NDFrom=" + this.getFK_Node();
		DBAccess.RunSQL(sql);

		//执行会签,写入日志.
		BP.WF.Dev2Interface.WriteTrackInfo(gwf.getFK_Flow(), gwf.getFK_Node(), gwf.getNodeName(), gwf.getWorkID(), gwf.getFID(), empsOfHuiQian, "执行会签");

		String str = "保存成功.\t\n该工作已经移动到会签列表中了,等到所有的人会签完毕后,就可以出现在待办列表里.";
		str += "\t\n如果您要增加或者移除会签人请到会签列表找到该记录,执行操作.";
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
			String trackTable = "ND" + Integer.parseInt( this.getFK_Flow()) + "Track";
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
	public String AccepterOfGener_Delete() {
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
	public String AccepterOfGener_Send() {
		try {
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
		} catch (Exception ex) {
			return "err@" + ex.getMessage();
		}
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

		return BP.Tools.Json.ToJson(dt);
	}

	public final String DBTemplate_DeleteDBTemplate() {
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
	 
 		gwf.setParas_DBTemplate(false);


		gwf.Update();

		return "设置成功";
	}

	public final String DBTemplate_Init() {
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

	public String DBTemplate_SaveAsDBTemplate() {
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		gwf.setParas_DBTemplate(true);
		try {
			gwf.setParas_DBTemplateName(URLDecoder.decode(this.GetRequestVal("Title"),"UTF-8"));
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
	
	/** 打包下载
	 
	 @return 
	 */
	public final String Packup_Init()
	{
		try
		{
			int nodeID = this.getFK_Node();
			if (this.getFK_Node() == 0)
			{
				GenerWorkFlow gwf = new GenerWorkFlow(this.WorkID);
				nodeID = gwf.getFK_Node();
			}

			Node nd = new Node(nodeID);
			Work wk = nd.getHisWork();
			return "err@暂未实现";////MakeForm2Html.MakeHtmlDocument(wk.NodeFrmID, this.WorkID, this.getFK_Flow(), null);
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
}
