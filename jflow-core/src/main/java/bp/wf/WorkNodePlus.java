package bp.wf;

import bp.en.*;
import bp.da.*;
import bp.difference.SystemConfig;
import bp.pub.RTFEngine;
import bp.tools.HttpClientUtil;
import bp.web.*;
import bp.sys.*;
import bp.wf.template.*;
import bp.wf.data.*;
import net.sf.json.JSONObject;

import java.util.*;
import java.io.*;

/**
 * WorkNode的附加类: 2020年06月09号 1， 因为worknode的类方法太多，为了能够更好的减轻代码逻辑. 2. 一部分方法要移动到这里来.
 */
public class WorkNodePlus {

	/**
	 * 发送草稿实例 2020.10.27 fro 铁路局.
	 * 
	 * @throws Exception
	 */
	public static void SendSendDraftSubFlow(WorkNode wn) throws Exception {

		// 如果不允许发送草稿子流程，就让其返回.
		if (wn.getHisNode().getIsSendDraftSubFlow() == false) {
			return;
		}

		// 查询出来该流程实例下的所有草稿子流程.
		GenerWorkFlows gwfs = new GenerWorkFlows();
		gwfs.Retrieve(GenerWorkFlowAttr.PWorkID, wn.getWorkID(), GenerWorkFlowAttr.WFState, 1);

		// 子流程配置信息.
		SubFlowHandGuide sf = null;

		// 开始发送子流程.
		for (GenerWorkFlow gwfSubFlow : gwfs.ToJavaList()) {
			// 获得配置信息.
			if (sf == null || sf.getFK_Flow() != gwfSubFlow.getFK_Flow()) {
				String pkval = wn.getHisGenerWorkFlow().getFK_Flow() + "_" + gwfSubFlow.getFK_Flow() + "_0";
				sf = new SubFlowHandGuide();
				sf.setMyPK(pkval);
				sf.RetrieveFromDBSources();
			}

			// 把草稿移交给当前人员. - 更新控制表.
			gwfSubFlow.setStarter(WebUser.getNo());
			gwfSubFlow.setStarterName(WebUser.getName());
			gwfSubFlow.Update();
			// 把草稿移交给当前人员. - 更新工作人员列表.
			DBAccess.RunSQL("UPDATE WF_GenerWorkerList SET FK_Emp='" + WebUser.getNo() + "',FK_EmpText='"
					+ WebUser.getName() + "' WHERE WorkID=" + gwfSubFlow.getWorkID());
			// 更新track表.
			//DBAccess.RunSQL(
			//		"UPDATE ND" + Integer.parseInt(gwfSubFlow.getFK_Flow()) + "Track SET FK_Emp='" + WebUser.getNo()
			//				+ "',FK_EmpText='" + WebUser.getName() + "' WHERE WorkID=" + gwfSubFlow.getWorkID());

			// 启动子流程. 并把两个字段，写入子流程.
			bp.wf.Dev2Interface.Node_SendWork(gwfSubFlow.getFK_Flow(), gwfSubFlow.getWorkID(), null, null);
		}
	}

	/**
	 * 生成单据
	 * 
	 * @param wn
	 * @throws Exception
	 */
	public static void GenerRtfBillTemplate(WorkNode wn) throws Exception {
		BillTemplates reffunc = wn.getHisNode().getBillTemplates();

		/// 生成单据信息
		long workid = wn.getHisWork().getOID();
		int nodeId = wn.getHisNode().getNodeID();
		String flowNo = wn.getHisNode().getFK_Flow();

		///

		Flow fl = wn.getHisNode().getHisFlow();
		String year = DataType.getCurrentYear();
		String billInfo = "";
		for (BillTemplate func : reffunc.ToJavaList()) {
			if (func.getTemplateFileModel() != TemplateFileModel.RTF) {
				continue;
			}

			String file = year + "_" + wn.getExecerDeptNo() + "_" + func.getNo() + "_" + workid + ".doc";
			RTFEngine rtf = new RTFEngine();

			Works works;
			String[] paths;
			String path;
			try {

				/// 把数据放入 单据引擎。
				rtf.getHisEns().clear(); // 主表数据。
				rtf.getEnsDataDtls().clear(); // 明细表数据.

				// 找到主表数据.
				rtf.HisGEEntity = new GEEntity(wn.rptGe.getClassID());
				rtf.HisGEEntity.setRow(wn.rptGe.getRow());

				// 把每个节点上的工作加入到报表引擎。
				rtf.AddEn(wn.getHisWork());
				rtf.ensStrs += ".ND" + wn.getHisNode().getNodeID();

				// 把当前work的Dtl 数据放进去了。
				ArrayList<Entities> al = wn.getHisWork().GetDtlsDatasOfList();

				for (Entities ens : al) {
					rtf.AddDtlEns(ens);
				}

				/// 把数据放入 单据引擎。

				/// 生成单据

				paths = file.split("[_]", -1);
				path = paths[0] + "/" + paths[1] + "/" + paths[2] + "/";
				String billUrl = wn.getVirPath() + "DataUser/Bill/" + path + file;
				if (func.getHisBillFileType() == BillFileType.PDF) {
					billUrl = billUrl.replace(".doc", ".pdf");
					billInfo += "<img src='./Img/FileType/PDF.gif' /><a href='" + billUrl + "' target=_blank >"
							+ func.getName() + "</a>";
				} else {
					billInfo += "<img src='./Img/FileType/doc.gif' /><a href='" + billUrl + "' target=_blank >"
							+ func.getName() + "</a>";
				}

				path = bp.wf.Glo.getFlowFileBill() + year + "/" + wn.getExecerDeptNo() + "/" + func.getNo() + "/";
				// path = AppDomain.CurrentDomain.BaseDirectory + path;
				if ((new File(path)).isDirectory() == false) {
					(new File(path)).mkdirs();
				}

				rtf.MakeDoc(func.getTempFilePath() + ".rtf", path, file, false);

				///

				/// 转化成pdf.
				if (func.getHisBillFileType() == BillFileType.PDF) {
					String rtfPath = path + file;
					String pdfPath = rtfPath.replace(".doc", ".pdf");
					try {
						Glo.Rtf2PDF(rtfPath, pdfPath);
					} catch (RuntimeException ex) {
						wn.addMsg("RptError",
								bp.wf.Glo.multilingual("生成报表数据错误:{0}.", "WorkNode", "rpt_error", ex.getMessage()));

					}
				}

				///

				/// 保存单据
				Bill bill = new Bill();
				bill.setMyPK(wn.getHisWork().getFID() + "_" + wn.getHisWork().getOID() + "_"
						+ wn.getHisNode().getNodeID() + "_" + func.getNo());
				bill.setFID(wn.getHisWork().getFID());
				bill.setWorkID(wn.getHisWork().getOID());
				bill.setFK_Node(wn.getHisNode().getNodeID());
				bill.setFK_Dept(wn.getExecerDeptNo());
				bill.setFK_Emp(wn.getExecer());
				bill.setUrl(billUrl);
				bill.setRDT(DataType.getCurrentDataTime());
				bill.setFullPath(path + file);
				bill.setFK_NY(DataType.getCurrentYearMonth());
				bill.setFK_Flow(wn.getHisNode().getFK_Flow());
				// bill.FK_BillType = func.FK_BillType;
				bill.setFK_Flow(wn.getHisNode().getFK_Flow());
				bill.setEmps(wn.rptGe.GetValStrByKey("Emps"));
				bill.setFK_Starter(wn.rptGe.GetValStrByKey("Rec"));
				bill.setStartDT(wn.rptGe.GetValStrByKey("RDT"));
				bill.setTitle(wn.rptGe.GetValStrByKey("Title"));
				bill.setFK_Dept(wn.rptGe.GetValStrByKey("FK_Dept"));
				try {
					bill.Save();
				} catch (java.lang.Exception e) {
					bill.Update();
				}

				///
			} catch (RuntimeException ex) {
				bp.wf.dts.InitBillDir dir = new bp.wf.dts.InitBillDir();
				dir.Do();
				path = bp.wf.Glo.getFlowFileBill() + year + "/" + wn.getExecerDeptNo() + "/" + func.getNo() + "/";

				String[] para1 = new String[4];
				para1[0] = bp.wf.Glo.getFlowFileBill();
				para1[1] = ex.getMessage();
				para1[2] = file;
				para1[3] = path;
				String msgErr1 = bp.wf.Glo.multilingual("@生成单据失败,请让管理员检查目录设置:[{0}].@Err:{1},@File={2},@Path:{3}.",
						"WorkNode", "wf_eng_error_2", para1);
				String msgErr2 = bp.wf.Glo.multilingual("@系统已经做了可能性的修复，请您再发送一次，如果问题仍然存在请联系管理员。", "WorkNode",
						"wf_eng_error_3");
				String msgErr3 = bp.wf.Glo.multilingual("@其它信息:{0}.", "WorkNode", "other_info", ex.getMessage());

				billInfo += "@<font color=red>" + msgErr1 + "</font>@<hr>" + msgErr2;
				throw new RuntimeException(msgErr1 + msgErr3);
			}
		} // end 生成循环单据。

		if (!billInfo.equals("")) {
			billInfo = "@" + billInfo;
		}
		wn.addMsg(SendReturnMsgFlag.BillInfo, billInfo);
	}

	/**
	 * 当要发送是检查流程是否可以允许发起.
	 * 
	 * @param flow
	 *            流程
	 * @param wk
	 *            开始节点工作
	 * @return
	 * @throws Exception
	 */
	public static boolean CheckIsCanStartFlow_SendStartFlow(Flow flow, Work wk) throws Exception {
		StartLimitRole role = flow.getStartLimitRole();
		if (role == StartLimitRole.None) {
			return true;
		}

		String sql = "";
		String ptable = flow.getPTable();

		if (role == StartLimitRole.ColNotExit) {
			/* 指定的列名集合不存在，才可以发起流程。 */

			// 求出原来的值.
			String[] strs = flow.getStartLimitPara().split("[,]", -1);
			String val = "";
			for (String str : strs) {
				if (DataType.IsNullOrEmpty(str) == true) {
					continue;
				}
				try {
					val += wk.GetValStringByKey(str);
				} catch (RuntimeException ex) {
					throw new RuntimeException(
							"@流程设计错误,您配置的检查参数(" + flow.getStartLimitPara() + "),中的列(" + str + ")已经不存在表单里.");
				}
			}

			// 找出已经发起的全部流程.
			sql = "SELECT " + flow.getStartLimitPara() + " FROM " + ptable
					+ " WHERE  WFState NOT IN(0,1) AND FlowStarter='" + WebUser.getNo() + "'";
			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			for (DataRow dr : dt.Rows) {
				String v = dr.getValue(0).toString() + dr.getValue(1).toString() + dr.getValue(2).toString();
				if (val.equals(v)) {
					return false;
				}
			}
			return true;
		}

		// 配置的sql,执行后,返回结果是 0 .
		if (role == StartLimitRole.ResultIsZero) {
			sql = bp.wf.Glo.DealExp(flow.getStartLimitPara(), wk, null);
			if (DBAccess.RunSQLReturnValInt(sql, 0) == 0) {
				return true;
			} else {
				return false;
			}
		}

		// 配置的sql,执行后,返回结果是 <> 0 .
		if (role == StartLimitRole.ResultIsNotZero) {
			sql = bp.wf.Glo.DealExp(flow.getStartLimitPara(), wk, null);
			if (DBAccess.RunSQLReturnValInt(sql, 0) != 0) {
				return true;
			} else {
				return false;
			}
		}

		// 为子流程的时候，该子流程只能被调用一次.
		if (role == StartLimitRole.OnlyOneSubFlow) {
			sql = "SELECT PWorkID FROM WF_GenerWorkFlow WHERE WorkID=" + wk.getOID();
			String pWorkidStr = DBAccess.RunSQLReturnStringIsNull(sql, "0");
			if (pWorkidStr.equals("0")) {
				return true;
			}

			sql = "SELECT Starter, RDT FROM WF_GenerWorkFlow WHERE PWorkID=" + pWorkidStr + " AND FK_Flow='"
					+ flow.getNo() + "'";
			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			if (dt.Rows.size() == 0 || dt.Rows.size() == 1) {
				return true;
			}

			// string title = dt.Rows.get(0).getValue("Title"].ToString();
			String starter = dt.Rows.get(0).getValue("Starter").toString();
			String rdt = dt.Rows.get(0).getValue("RDT").toString();

			throw new RuntimeException(
					flow.getStartLimitAlert() + "@该子流程已经被[" + starter + "], 在[" + rdt + "]发起，系统只允许发起一次。");
		}

		return true;
	}

	/**
	 * 开始执行数据同步,在流程运动的过程中， 数据需要同步到不同的表里去.
	 * 
	 * @param fl
	 *            流程
	 * @param gwf
	 *            实体
	 * @param rpt
	 *            实体
	 * @throws Exception
	 */
	public static void DTSData(Flow fl, GenerWorkFlow gwf, GERpt rpt, Node currNode, boolean isStopFlow)
			throws Exception {
		// 判断同步类型.
		if (fl.getDTSWay() == DataDTSWay.None) {
			return;
		}

		boolean isActiveSave = false;
		// 判断是否符合流程数据同步条件.
		switch (fl.getDTSTime()) {
		case AllNodeSend:
			isActiveSave = true;
			break;
		case SpecNodeSend:
			if (fl.getDTSSpecNodes().contains(String.valueOf(currNode.getNodeID())) == true) {
				isActiveSave = true;
			}
			break;
		case WhenFlowOver:
			if (isStopFlow) {
				isActiveSave = true;
			}
			break;
		default:
			break;
		}
		if (isActiveSave == false) {
			return;
		}

		/// qinfaliang, 编写同步的业务逻辑,执行错误就抛出异常.

		if(fl.getDTSWay()==DataDTSWay.Syn) {
			String[] dtsArray = fl.getDTSFields().split("[@]", -1);

			String[] lcArr = dtsArray[0].split("[,]", -1); // 取出对应的主表字段
			String[] ywArr = dtsArray[1].split("[,]", -1); // 取出对应的业务表字段

			String sql = "SELECT " + dtsArray[0] + " FROM " + fl.getPTable().toUpperCase() + " WHERE OID=" + rpt.getOID();
			DataTable lcDt = DBAccess.RunSQLReturnTable(sql);
			if (lcDt.Rows.size() == 0) {
				throw new RuntimeException("没有找到业务表数据.");
			}

			bp.sys.SFDBSrc src = new bp.sys.SFDBSrc(fl.getDTSDBSrc());
			sql = "SELECT " + dtsArray[1] + " FROM " + fl.getDTSBTable().toUpperCase();

			DataTable ywDt = src.RunSQLReturnTable(sql);

			String values = "";
			String upVal = "";

			for (int i = 0; i < lcArr.length; i++) {
				switch (src.getDBSrcType()) {
					case Localhost:
						switch (SystemConfig.getAppCenterDBType()) {
							case MSSQL:
								break;
							case Oracle:
								if (ywDt.Columns.get(ywArr[i]).DataType == Date.class) {
									if (!DataType.IsNullOrEmpty(lcDt.Rows.get(0).getValue(lcArr[i].toString()).toString())) {
										values += "to_date('" + lcDt.Rows.get(0).getValue(lcArr[i].toString()) + "','YYYY-MM-DD'),";
									} else {
										values += "'',";
									}
									continue;
								}
								values += "'" + lcDt.Rows.get(0).getValue(lcArr[i].toString()) + "',";
								continue;
							case MySQL:
								break;
							case Informix:
								break;
							default:
								throw new RuntimeException("没有涉及到的连接测试类型...");
						}
						break;
					case SQLServer:
						break;
					case MySQL:
						break;
					case Oracle:
						if (ywDt.Columns.get(ywArr[i]).DataType == Date.class) {
							if (!DataType.IsNullOrEmpty(lcDt.Rows.get(0).getValue(lcArr[i].toString()).toString())) {
								values += "to_date('" + lcDt.Rows.get(0).getValue(lcArr[i].toString()) + "','YYYY-MM-DD'),";
							} else {
								values += "'',";
							}
							continue;
						}
						values += "'" + lcDt.Rows.get(0).getValue(lcArr[i].toString()) + "',";
						continue;
					default:
						throw new RuntimeException("暂时不支您所使用的数据库类型!");
				}
				values += "'" + lcDt.Rows.get(0).getValue(lcArr[i].toString()) + "',";
				// 获取除主键之外的其他值
				if (i > 0) {
					upVal = upVal + ywArr[i] + "='" + lcDt.Rows.get(0).getValue(lcArr[i].toString()) + "',";
				}
			}

			values = values.substring(0, values.length() - 1);
			upVal = upVal.substring(0, upVal.length() - 1);

			// 查询对应的业务表中是否存在这条记录
			sql = "SELECT * FROM " + fl.getDTSBTable().toUpperCase() + " WHERE " + fl.getDTSBTablePK() + "='"
					+ lcDt.Rows.get(0).getValue(fl.getDTSBTablePK()) + "'";
			DataTable dt = src.RunSQLReturnTable(sql);
			// 如果存在，执行更新，如果不存在，执行插入
			if (dt.Rows.size() > 0) {
				sql = "UPDATE " + fl.getDTSBTable().toUpperCase() + " SET " + upVal + " WHERE " + fl.getDTSBTablePK() + "='"
						+ lcDt.Rows.get(0).getValue(fl.getDTSBTablePK()) + "'";
			} else {
				sql = "INSERT INTO " + fl.getDTSBTable().toUpperCase() + "(" + dtsArray[1] + ") VALUES(" + values + ")";
			}

			try {
				src.RunSQL(sql);
			} catch (RuntimeException ex) {
				throw new RuntimeException(ex.getMessage());
			}

			/// qinfaliang, 编写同步的业务逻辑,执行错误就抛出异常.
			return;
		}
		if(fl.getDTSWay() == DataDTSWay.WebAPI) {
			//推送的数据
			String info = "{";
			//推送的主表数据
			String mainTable = "";
			mainTable += "\"mainTable\":";
			mainTable += "{";
			MapAttrs attrs = new MapAttrs(currNode.getNodeFrmID());
			for (MapAttr attr : attrs.ToJavaList()) {
				if (attr.getKeyOfEn().equals("Title") || attr.getKeyOfEn().equals("BillNo"))
					continue;
				if (attr.getKeyOfEn().equals("AtPara") || attr.getKeyOfEn().equals("BillState"))
					continue;
				if (attr.getKeyOfEn().equals("RDT") || attr.getKeyOfEn().equals("OrgNo"))
					continue;
				if (attr.getKeyOfEn().equals("FK_Dept") || attr.getKeyOfEn().equals("FID"))
					continue;
				if (attr.getKeyOfEn().equals("Starter") || attr.getKeyOfEn().equals("StarterName"))
					continue;
				if (attr.getKeyOfEn().equals("OID") || attr.getKeyOfEn().equals("Rec"))
					continue;
				mainTable += "\"" + attr.getKeyOfEn() + "\":\"" + rpt.GetValStrByKey(attr.getKeyOfEn()) + "\",";
			}
			mainTable += "\"oid\":\"" + gwf.getWorkID() + "\"";
//			if (!DataType.IsNullOrEmpty(mainTable))
//				mainTable = mainTable.substring(0, mainTable.length() - 1);
			mainTable += "}";

			//推送的从表数据
			String dtls = "[";
			String dtlData = "";

			MapDtls mapDtls = new MapDtls();
			mapDtls.Retrieve(MapDtlAttr.FK_MapData, currNode.getNodeFrmID());
			for (MapDtl dtl : mapDtls.ToJavaList()) {
				dtlData += "{";
				dtlData += "\"dtlNo\":\"" + dtl.getNo() + "\",";
				//多个从表的数据
				String dtlList = "[";
				//每一行数据
				String dtlOne = "";
				//每一行的字段数据
				String dtlKeys = "";
				//从表附件
				String dtlAths = "[";
				String dtlAth = "";

				MapAttrs dtlAttrs = new MapAttrs(dtl.getNo());
				GEDtls geDtls = new GEDtls(dtl.getNo());
				geDtls.Retrieve(GEDtlAttr.RefPK, gwf.getWorkID());
				for (GEDtl geDtl : geDtls.ToJavaList()) {
					dtlKeys = "{";
					for (MapAttr attr : dtlAttrs.ToJavaList()) {
						if (attr.getKeyOfEn().equals("OID") || attr.getKeyOfEn().equals("RefPK"))
							continue;
						if (attr.getKeyOfEn().equals("FID") || attr.getKeyOfEn().equals("RDT"))
							continue;
						if (attr.getKeyOfEn().equals("Rec") || attr.getKeyOfEn().equals("AthNum"))
							continue;
						dtlKeys += "\"" + attr.getKeyOfEn() + "\":\"" + geDtl.GetValByKey(attr.getKeyOfEn()) + "\",";
					}
					if (!DataType.IsNullOrEmpty(dtlKeys))
						dtlKeys = dtlKeys.substring(0, dtlKeys.length() - 1);
					dtlKeys += "}";

					FrmAttachmentDBs attachmentDBs = new FrmAttachmentDBs();
					attachmentDBs.Retrieve(FrmAttachmentDBAttr.FK_MapData, dtl.getNo(), FrmAttachmentDBAttr.RefPKVal, geDtl.getOID());
					for (FrmAttachmentDB frmAttachmentDB : attachmentDBs.ToJavaList()) {
						dtlAth += "{";
						dtlAth += "\"fileFullName\":\"" + frmAttachmentDB.getFileFullName() + "\",";
						dtlAth += "\"fileName\":\"" + frmAttachmentDB.getFileName() + "\",";
						dtlAth += "\"sort\":\"" + frmAttachmentDB.getSort() + "\",";
						dtlAth += "\"fileExts\":\"" + frmAttachmentDB.getFileExts() + "\",";
						dtlAth += "\"rdt\":\"" + frmAttachmentDB.getRDT() + "\",";
						dtlAth += "\"rec\":\"" + frmAttachmentDB.getRec() + "\",";
						dtlAth += "\"myPK\":\"" + frmAttachmentDB.getMyPK() + "\",";
						dtlAth += "\"recName\":\"" + frmAttachmentDB.getRecName() + "\",";
						dtlAth += "\"fk_dept\":\"" + frmAttachmentDB.getFK_Dept() + "\",";
						dtlAth += "\"fk_deptName\":\"" + frmAttachmentDB.getFK_DeptName() + "\"";
						dtlAth += "},";
					}
					if (!DataType.IsNullOrEmpty(dtlAth))
						dtlAth = dtlAth.substring(0, dtlAth.length() - 1);
					dtlAth += "]";
					dtlOne += "{";
					dtlOne += "\"dtlData\":" + dtlKeys + ",";
					dtlOne += "\"dtlAths\":[" + dtlAth + "";
					dtlOne += "},";
				}
				if (!DataType.IsNullOrEmpty(dtlOne))
					dtlOne = dtlOne.substring(0, dtlOne.length() - 1);
				dtlList += dtlOne;
				dtlList += "]";

				dtlData += "\"dtl\":" + dtlList + "";
				dtlData += "},";
			}
			if (!DataType.IsNullOrEmpty(dtlData))
				dtlData = dtlData.substring(0, dtlData.length() - 1);
			dtls += dtlData;
			dtls += "]";

			//附件数据
			String aths = "[";
			String ath = "";

			FrmAttachments attachments = new FrmAttachments();
			attachments.Retrieve(FrmAttachmentAttr.FK_MapData, currNode.getNodeFrmID(), FrmAttachmentAttr.FK_Node, 0);
			for (FrmAttachment attachment : attachments.ToJavaList()) {
				FrmAttachmentDBs dbs = new FrmAttachmentDBs();
				dbs.Retrieve(FrmAttachmentDBAttr.FK_FrmAttachment, attachment.getMyPK(), FrmAttachmentDBAttr.FK_MapData, currNode.getNodeFrmID(), FrmAttachmentDBAttr.RefPKVal, gwf.getWorkID());
				if (dbs.ToJavaList().size() <= 0)
					continue;
				ath += "{";
				ath += "\"attachmentid\":\"" + attachment.getMyPK() + "\",";

				String athdb = "";
				for (FrmAttachmentDB db : dbs.ToJavaList()) {
					athdb += "{";
					athdb += "\"fileFullName\":\"" + db.getFileFullName() + "\",";
					athdb += "\"fileName\":\"" + db.getFileName() + "\",";
					athdb += "\"sort\":\"" + db.getSort() + "\",";
					athdb += "\"fileExts\":\"" + db.getFileExts() + "\",";
					athdb += "\"rdt\":\"" + db.getRDT() + "\",";
					athdb += "\"myPK\":\"" + db.getMyPK() + "\",";
					athdb += "\"refPKVal\":\"" + db.getRefPKVal() + "\",";
					athdb += "\"rec\":\"" + db.getRec() + "\",";
					athdb += "\"recName\":\"" + db.getRecName() + "\",";
					athdb += "\"fk_dept\":\"" + db.getFK_Dept() + "\",";
					athdb += "\"fk_deptName\":\"" + db.getFK_DeptName() + "\",";
					athdb += "},";
				}
				if (!DataType.IsNullOrEmpty(athdb))
					athdb = athdb.substring(0, athdb.length() - 1);
				ath += "\"athdb\":" + athdb + "";
				ath += "},";
			}
			if (!DataType.IsNullOrEmpty(ath))
				ath = ath.substring(0, ath.length() - 1);
			aths += ath;
			aths += "]";

			info += mainTable;
			info += ",\"dtls\":" + dtls;
			info += ",\"aths\":" + aths;
			info += "}";

			String apiUrl = fl.getDTWebAPI();
			java.util.Map<String, String> headerMap = new Hashtable<String, String>();
			headerMap.put("Content-Type", "application/json");
			headerMap.put("accessToken", "");
			//执行POST
			String postData = HttpClientUtil.doPost(apiUrl, info.toString(), headerMap);
			JSONObject j = JSONObject.fromObject(postData);
			if (!j.get("code").toString().equals("200"))
				bp.da.Log.DefaultLogWriteLine(LogType.Info, "同步失败:" + postData.toString());

			return;
		}
	}

	/**
	 * 处理协作模式下的删除规则
	 * 
	 * @param nd
	 *            节点
	 * @param gwf
	 * @throws Exception
	 */
	public static void GenerWorkerListDelRole(Node nd, GenerWorkFlow gwf) throws Exception {
		if (nd.getGenerWorkerListDelRole() == 0) {
			return;
		}

		// 按照部门删除,同部门下的人员.
		if (nd.getGenerWorkerListDelRole() == 1) {
			// 定义本部门的人员.
			String sqlUnion = "";
			sqlUnion += " SELECT No FROM Port_Emp WHERE FK_Dept='" + WebUser.getFK_Dept() + "' ";
			sqlUnion += " UNION ";
			sqlUnion += " SELECT FK_Dept FROM Port_DeptEmp WHERE FK_Dept='" + WebUser.getFK_Dept() + "''";

			// 获得要删除的人员.
			String sql = " SELECT FK_Emp FROM WF_GenerWorkerlist WHERE ";
			sql += " WorkID=" + gwf.getWorkID() + " AND FK_Node=" + gwf.getFK_Node() + " AND IsPass=0 ";
			sql += " AND FK_Emp IN (" + sqlUnion + ")";

			// 获得要删除的数据.
			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			for (DataRow dr : dt.Rows) {
				String empNo = dr.getValue(0).toString();
				sql = "UPDATE WF_GenerWorkerlist SET IsPass=1 WHERE WorkID=" + gwf.getWorkID() + " AND FK_Node="
						+ gwf.getFK_Node() + " AND FK_Emp='" + empNo + "'";
				DBAccess.RunSQL(sql);
			}
		}

		// 按照岗位删除.
		if (nd.getGenerWorkerListDelRole() == 2) {
			NodeStations nss = new NodeStations();
			nss.Retrieve(NodeStationAttr.FK_Node, gwf.getFK_Node());
			if (nss.size() == 0) {
				throw new RuntimeException("err@流程配置错误，您设置了待办按照岗位删除的规则,但是在当前节点上，您没有设置岗位。");
			}
			// 定义岗位人员
			String station = "SELECT FK_Station FROM Port_DeptEmpStation WHERE FK_Emp='" + WebUser.getNo() + "'";
			station = DBAccess.RunSQLReturnVal(station).toString();
			String stationEmp = "SELECT FK_Emp FROM Port_DeptEmpStation where FK_Station = " + station + "";
			// 获得要删除的人员.
			String sql = " SELECT FK_Emp FROM WF_GenerWorkerlist WHERE ";
			sql += " WorkID=" + gwf.getWorkID() + " AND FK_Node=" + gwf.getFK_Node() + " AND IsPass=0 ";
			sql += " AND FK_Emp IN (" + stationEmp + ")";
			// 获得要删除的数据.

			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			for (int i = 0; i < dt.Rows.size(); i++) {
				String empNo = dt.Rows.get(i).getValue(0).toString();
				if (empNo.equals(WebUser.getNo())) {
					continue;
				}
				sql = "UPDATE WF_GenerWorkerlist SET IsPass=1 WHERE WorkID=" + gwf.getWorkID() + " AND FK_Node="
						+ gwf.getFK_Node() + " AND FK_Emp='" + empNo + "'";
				DBAccess.RunSQL(sql);
			}
		}
	}

	/**
	 * 处理发送返回，断头路节点.
	 * 
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	public static WorkNode IsSendBackNode(WorkNode wn) throws NumberFormatException, Exception {
		if (wn.getHisNode().getIsSendBackNode() == false) {
			return wn; // 如果不是断头路节点，就让其返回.
		}
		if (wn.getHisGenerWorkFlow().getWFState() == WFState.ReturnSta) {
			// 是退回状态且原路返回的情况

			String sql = "SELECT ReturnNode, Returner, ReturnerName, IsBackTracking ";
			sql += " FROM WF_ReturnWork  ";
			sql += " WHERE WorkID=" + wn.getWorkID() + " ORDER BY RDT DESC";

			DataTable mydt = DBAccess.RunSQLReturnTable(sql);
			if (mydt.Rows.size() != 0 && mydt.Rows.get(0).getValue(3).toString().equals("1") == true) {
				wn.JumpToNode = new Node(Integer.parseInt(mydt.Rows.get(0).getValue("ReturnNode").toString()));
				wn.JumpToEmp = mydt.Rows.get(0).getValue("Returner").toString();
				return wn;
			}

		}
		if (wn.getHisNode().getHisToNDNum() != 0) {
			throw new RuntimeException("err@流程配置错误:当前节点是发送自动返回节点，但是当前节点不能有到达的节点.");
		}

		if (wn.getHisNode().getHisRunModel() != RunModel.Ordinary) {
			throw new RuntimeException(
					"err@流程配置错误:只能是线性节点才能设置[发送并返回]属性,当前节点是[" + wn.getHisNode().getHisRunModel().toString() + "]");
		}

		// 判断是否是最后一个人？
		boolean isLastOne = false;
		GenerWorkerLists gwls = new GenerWorkerLists();
		gwls.Retrieve(GenerWorkerListAttr.WorkID, wn.getWorkID(), GenerWorkerListAttr.FK_Node,
				wn.getHisNode().getNodeID(), GenerWorkerListAttr.IsPass, 0);
		if (gwls.size() == 1) {
			isLastOne = true; // 如果只有一个，本人就是lastOne.
		}

		// WorkNode wn= this.GetPreviousWorkNode();
		// this.JumpToEmp = wn.HisWork.Rec; //对于绑定的表单有问题.
		// this.JumpToNode = wn.HisNode;

		if (isLastOne == true || wn.getHisNode().getTodolistModel() == TodolistModel.QiangBan) {
			String ptable = "ND" + Integer.parseInt(wn.getHisFlow().getNo()) + "Track";

			String mysql = "";
			if (wn.getHisNode().getHisRunModel() == RunModel.SubThread)
				mysql = "SELECT NDFrom,EmpFrom FROM " + ptable + " WHERE (WorkID =" + wn.getWorkID() + " AND FID="
						+ wn.getHisGenerWorkFlow().getFID() + ") AND ActionType!= " + ActionType.UnSend.getValue()
						+ " AND NDTo = " + wn.getHisNode().getNodeID()
						+ " AND(NDTo != NDFrom) AND NDFrom In(Select Node From WF_Direction Where ToNode="
						+ wn.getHisNode().getNodeID() + " AND FK_Flow='" + wn.getHisFlow().getNo() + "')";
			else
				mysql = "SELECT NDFrom,EmpFrom FROM " + ptable + " WHERE WorkID =" + wn.getWorkID()
						+ " AND ActionType!= " + ActionType.UnSend.getValue() + " AND NDTo = "
						+ wn.getHisNode().getNodeID()
						+ " AND(NDTo != NDFrom) AND NDFrom In(Select Node From WF_Direction Where ToNode="
						+ wn.getHisNode().getNodeID() + " AND FK_Flow='" + wn.getHisFlow().getNo() + "')";

			// DataTable mydt = DBAccess.RunSQLReturnTable("SELECT
			// FK_Node,FK_Emp FROM WF_GenerWorkerList WHERE WorkID=" +
			// this.WorkID + " AND FK_Node!=" + this.HisNode.NodeID + " ORDER BY
			// RDT DESC ");
			DataTable mydt = DBAccess.RunSQLReturnTable(mysql);
			if (mydt.Rows.size() == 0) {
				throw new RuntimeException("系统错误，没有找到上一个节点.");
			}

			wn.JumpToEmp = mydt.Rows.get(0).getValue(1).toString();
			int priNodeID = Integer.parseInt(mydt.Rows.get(0).getValue(0).toString());
			wn.JumpToNode = new Node(priNodeID);

			// 清除选择，防止在自动发送到该节点上来.
			wn.getHisGenerWorkFlow().setParasToNodes("");
			wn.getHisGenerWorkFlow().DirectUpdate();

			// 清除上次发送的选择,不然下次还会自动发送到当前的节点上来.
			mysql = "DELETE FROM WF_SelectAccper WHERE FK_Node=" + wn.JumpToNode.getNodeID() + " AND WorkID="
					+ wn.getWorkID();
			DBAccess.RunSQL(mysql);
		}
		return wn;
	}

	/**
	 * 处理 askfor 状态
	 * 
	 * @param wn
	 * @throws Exception
	 */
	public static SendReturnObjs DealAskForState(WorkNode wn) throws Exception {
		/* 如果是加签状态, 就判断加签后，是否要返回给执行加签人. */
		GenerWorkerLists gwls = new GenerWorkerLists();
		gwls.Retrieve(GenerWorkerListAttr.FK_Node, wn.getHisNode().getNodeID(), GenerWorkerListAttr.WorkID,
				wn.getWorkID());

		boolean isDeal = false;
		AskforHelpSta askForSta = AskforHelpSta.AfterDealSend;
		for (GenerWorkerList item : gwls.ToJavaList()) {
			if (item.getIsPassInt() == AskforHelpSta.AfterDealSend.getValue()) {
				/* 如果是加签后，直接发送就不处理了。 */
				isDeal = true;
				askForSta = AskforHelpSta.AfterDealSend;

				// 更新workerlist, 设置所有人员的状态为已经处理的状态,让它走到下一步骤去.
				DBAccess.RunSQL("UPDATE WF_GenerWorkerList SET IsPass=1 WHERE FK_Node=" + wn.getHisNode().getNodeID()
						+ " AND WorkID=" + wn.getWorkID());

				// 写入日志.
				wn.AddToTrack(ActionType.ForwardAskfor, item.getFK_Emp(), item.getFK_EmpText(),
						wn.getHisNode().getNodeID(), wn.getHisNode().getName(),
						bp.wf.Glo.multilingual("加签后向下发送，直接发送给下一步处理人。", "WorkNode", "send_to_next"));

			}

			if (item.getIsPassInt() == AskforHelpSta.AfterDealSendByWorker.getValue()) {
				/* 如果是加签后，在由我直接发送。 */
				item.setIsPassInt(0);

				isDeal = true;
				askForSta = AskforHelpSta.AfterDealSendByWorker;

				// 更新workerlist, 设置所有人员的状态为已经处理的状态.
				DBAccess.RunSQL("UPDATE WF_GenerWorkerList SET IsPass=1 WHERE FK_Node=" + wn.getHisNode().getNodeID()
						+ " AND WorkID=" + wn.getWorkID());

				// 把发起加签人员的状态更新过来，让他可见待办工作.
				item.setIsPassInt(0);
				item.Update();

				// 更新流程状态.
				wn.getHisGenerWorkFlow().setWFState(WFState.AskForReplay);
				wn.getHisGenerWorkFlow().Update();

				// 让加签人，设置成工作未读。
				bp.wf.Dev2Interface.Node_SetWorkUnRead(wn.getWorkID(), item.getFK_Emp());

				// 从临时变量里获取回复加签意见.
				String replyInfo = wn.getHisGenerWorkFlow().getParasAskForReply();

				//// 写入日志.
				// this.AddToTrack(ActionType.ForwardAskfor, item.FK_Emp,
				//// item.FK_EmpText,
				// this.HisNode.NodeID, this.HisNode.Name,
				// "加签后向下发送，并转向加签人发起人（" + item.FK_Emp + "，" + item.FK_EmpText +
				//// "）。<br>意见:" + replyInfo);

				// 写入日志.
				wn.AddToTrack(ActionType.ForwardAskfor, item.getFK_Emp(), item.getFK_EmpText(),
						wn.getHisNode().getNodeID(), wn.getHisNode().getName(),
						bp.wf.Glo.multilingual("回复意见:{0}.", "WorkNode", "reply_comments", replyInfo));

				// 加入系统变量。
				wn.addMsg(SendReturnMsgFlag.VarToNodeID, String.valueOf(wn.getHisNode().getNodeID()),
						SendReturnMsgType.SystemMsg);
				wn.addMsg(SendReturnMsgFlag.VarToNodeName, wn.getHisNode().getName(), SendReturnMsgType.SystemMsg);
				wn.addMsg(SendReturnMsgFlag.VarAcceptersID, item.getFK_Emp(), SendReturnMsgType.SystemMsg);
				wn.addMsg(SendReturnMsgFlag.VarAcceptersName, item.getFK_EmpText(), SendReturnMsgType.SystemMsg);

				// 加入提示信息.
				wn.addMsg(
						SendReturnMsgFlag.SendSuccessMsg, bp.wf.Glo.multilingual("已经转给加签的发起人({0},{1})", "WorkNode",
								"send_to_the_operator", item.getFK_Emp().toString(), item.getFK_EmpText()),
						SendReturnMsgType.Info);

				// 删除当前操作员临时增加的工作列表记录, 如果不删除就会导致第二次加签失败.
				GenerWorkerList gwl = new GenerWorkerList();
				gwl.Delete(GenerWorkerListAttr.FK_Node, wn.getHisNode().getNodeID(), GenerWorkerListAttr.WorkID,
						wn.getWorkID(), GenerWorkerListAttr.FK_Emp, wn.getExecer());

				// 调用发送成功事件.
				String sendSuccess = ExecEvent.DoNode(EventListNode.SendSuccess, wn);
				wn.HisMsgObjs.AddMsg("info21", sendSuccess, sendSuccess, SendReturnMsgType.Info);

				// 执行时效考核.
				Glo.InitCH(wn.getHisFlow(), wn.getHisNode(), wn.getWorkID(), 0, wn.getHisGenerWorkFlow().getTitle());

				// 返回发送对象.
				return wn.HisMsgObjs;
			}
		}

		if (isDeal == false) {
			throw new RuntimeException(bp.wf.Glo.multilingual("@流程引擎错误，不应该找不到加签的状态.", "WorkNode", "wf_eng_error_1"));
		}

		return null;
	}

	/**
	 * 执行分河流状态
	 * 
	 * @param wn
	 * @throws Exception
	 */
	public static void DealHeLiuState(WorkNode wn) throws Exception {
		/* 如果是合流点 检查当前是否是合流点如果是，则检查分流上的子线程是否完成。 */
		/* 检查是否有子线程没有结束 */
		Paras ps = new Paras();
		ps.SQL = "SELECT WorkID,FK_Emp,FK_EmpText,FK_NodeText FROM WF_GenerWorkerList WHERE FID=" + ps.getDBStr()
				+ "FID AND IsPass=0 AND IsEnable=1";
		ps.Add(WorkAttr.FID, wn.getWorkID());

		DataTable dtWL = DBAccess.RunSQLReturnTable(ps);
		String infoErr = "";
		if (dtWL.Rows.size() != 0) {
			if (wn.getHisNode().getThreadKillRole() == ThreadKillRole.None
					|| wn.getHisNode().getThreadKillRole() == ThreadKillRole.ByHand) {
				infoErr += bp.wf.Glo.multilingual("@您不能向下发送，有如下子线程没有完成。", "WorkNode", "cannot_send_to_next_1");

				for (DataRow dr : dtWL.Rows) {
					String op = bp.wf.Glo.multilingual("@操作员编号:{0},{1}", "WorkNode", "current_operator",
							dr.getValue("FK_Emp").toString(), dr.getValue("FK_EmpText").toString());
					String nd = bp.wf.Glo.multilingual("停留节点:{0}.", "WorkNode", "current_node",
							dr.getValue("FK_NodeText").toString());
					// infoErr += "@操作员编号:" + dr["FK_Emp"] + "," +
					// dr["FK_EmpText"] + ",停留节点:" + dr["FK_NodeText"];
					infoErr += op + ";" + nd;
				}

				if (wn.getHisNode().getThreadKillRole() == ThreadKillRole.ByHand) {
					infoErr += bp.wf.Glo.multilingual("@请通知他们处理完成,或者强制删除子流程您才能向下发送.", "WorkNode",
							"cannot_send_to_next_2");
				}

				else {
					infoErr += bp.wf.Glo.multilingual("@请通知他们处理完成,您才能向下发送.", "WorkNode", "cannot_send_to_next_3");
				}

				// 抛出异常阻止它向下运动。
				throw new RuntimeException(infoErr);
			}

			if (wn.getHisNode().getThreadKillRole() == ThreadKillRole.ByAuto) {
				// 删除每个子线程，然后向下运动。
				for (DataRow dr : dtWL.Rows) {
					bp.wf.Dev2Interface.Flow_DeleteSubThread(Long.parseLong(dr.getValue(0).toString()),
							bp.wf.Glo.multilingual("合流点发送时自动删除", "WorkNode", "auto_delete"));
				}
			}
		}
	}


	public static SendReturnObjs SubFlowEvent(WorkNode wn) throws Exception
	{
		GenerWorkFlow gwf = wn.getHisGenerWorkFlow();
		//不是子流程
		if (gwf.getPWorkID() == 0)
			return wn.HisMsgObjs;


		//子流程运行到该节点时主流程自动运行到下一个节点
		if(gwf.getWFState()!=WFState.Complete && wn.getHisNode().getIsToParentNextNode() == true)
		{
			GenerWorkFlow pgwf = new GenerWorkFlow(gwf.getPWorkID());
			if (pgwf.getFK_Node() == gwf.getPNodeID())
			{
				SendReturnObjs returnObjs =bp.wf.Dev2Interface.Node_SendWork(gwf.getPFlowNo(), gwf.getPWorkID());
				String sendSuccess = "父流程自动运行到下一个节点，" + returnObjs.ToMsgOfHtml();
				wn.HisMsgObjs.AddMsg("info", sendSuccess, sendSuccess, SendReturnMsgType.Info);
			}
			return wn.HisMsgObjs;
		}

		//判断是不是子流程结束后显示父流程待办
		if(gwf.getWFState() == WFState.Complete)
		{
			long slWorkID = gwf.GetParaInt("SLWorkID");
			String slFlowNo = gwf.GetParaString("SLFlowNo");
			 int slNodeID = gwf.GetParaInt("SLNodeID");

			SubFlows subFlows = new SubFlows();
			if (slWorkID == 0)
				subFlows.Retrieve(SubFlowAttr.FK_Node, gwf.getPNodeID(), SubFlowAttr.SubFlowNo, wn.getHisFlow().getNo());
			else
				subFlows.Retrieve(SubFlowAttr.FK_Node, slNodeID, SubFlowAttr.SubFlowNo, wn.getHisFlow().getNo());

			if(subFlows.size()==0)
				return wn.HisMsgObjs;

			SubFlow subFlow =(SubFlow) subFlows.get(0);
			if (subFlow.getBackCopyRole() != 0 && slWorkID==0)
			{

				Node pNd = new Node(subFlow.getFK_Node());
				Work pwork = pNd.getHisWork();
				pwork.setOID(gwf.getPWorkID());
				pwork.RetrieveFromDBSources();
				GERpt prpt = new bp.wf.data.GERpt("ND" + Integer.parseInt(subFlow.getFK_Flow()) + "Rpt");
				prpt.setOID(gwf.getPWorkID());
				prpt.RetrieveFromDBSources();
				//判断是否启用了数据字段反填规则
				if (subFlow.getBackCopyRole() == 1 || subFlow.getBackCopyRole() == 3)
				{
					//子流程数据拷贝到父流程中
					pwork.Copy(wn.getHisWork());
					prpt.Copy(wn.getHisWork());
				}
				//子流程数据拷贝到父流程中
				if ((subFlow.getBackCopyRole() == 2 || subFlow.getBackCopyRole() == 3)
						&& DataType.IsNullOrEmpty(subFlow.getParentFlowCopyFields()) == false)
				{
					Work wk = wn.getHisWork();
					Attrs attrs = wk.getEnMap().getAttrs();
					//获取子流程的签批字段
					String keyOfEns = "";
					String keyVals = ""; //签批字段存储的值
					for(Attr attr :attrs)
					{
						if (attr.getUIContralType() == UIContralType.SignCheck)
						{
							keyOfEns += attr.getField()+ ",";
							continue;
						}

					}

					//父流程把子流程不同字段进行匹配赋值
					AtPara ap = new AtPara(subFlow.getParentFlowCopyFields());
					for(String str : ap.getHisHT().keySet())
					{
						Object val = ap.GetValStrByKey(str);
						if (DataType.IsNullOrEmpty(val.toString()) == true)
							continue;
						pwork.SetValByKey(val.toString(), wk.GetValByKey(str));
						prpt.SetValByKey(val.toString(), wk.GetValByKey(str));
						if (keyOfEns.contains(str + ",") == true)
							keyVals += wk.GetValByKey(str);
					}
					if (DataType.IsNullOrEmpty(keyVals) == false)
					{
						String trackPTable = "ND" + Integer.parseInt(wn.getHisFlow().getNo()) + "Track";
						//把子流程的签批字段对应的审核信息拷贝到父流程中
						keyVals = keyVals.substring(1);
						String sql = "SELECT * FROM " + trackPTable + " WHERE ActionType=22 AND WorkID=" + wn.getWorkID() + " AND NDFrom IN(" + keyVals + ")";
						DataTable dt = DBAccess.RunSQLReturnTable(sql);
						Tracks tracks = new Tracks();
						bp.en.QueryObject.InitEntitiesByDataTable(tracks, dt, null);
						for(Track t : tracks.ToJavaList())
						{

							t.setWorkID(pwork.getOID());
							t.setFID(pwork.getFID());
							t.FK_Flow = subFlow.getFK_Flow();
							t.setHisActionType(ActionType.WorkCheck);
							t.setMyPK(String.valueOf(DBAccess.GenerOIDByGUID()));
							t.Insert();
						}
					}

				}
				pwork.Update();
				prpt.Update();

			}


			//子流程运行结束后父流程是否自动往下运行一步
			String msg = bp.wf.Dev2Interface.FlowOverAutoSendParentOrSameLevelFlow(wn.getHisGenerWorkFlow(), wn.getHisFlow(),subFlow);
			if (DataType.IsNullOrEmpty(msg) == false)
			{
				wn.HisMsgObjs.AddMsg("info", msg, msg, SendReturnMsgType.Info);
				return wn.HisMsgObjs;
			}

			String mypk = "";
			long PWorkID = 0;
			boolean isSameLeavl = false;
			if (gwf.GetParaInt("SLNodeID") != 0)
			{
				mypk = gwf.GetParaInt("SLNodeID") + "_" + wn.getHisFlow().getNo() + "_0";
				PWorkID = gwf.GetValInt64ByKey("SLWorkID");
				isSameLeavl = true;
			}
			else
			{
				mypk = gwf.getPNodeID()+ "_" + wn.getHisFlow().getNo() + "_0";
				PWorkID = gwf.getPWorkID();
			}

			SubFlowHandGuide subflow = new SubFlowHandGuide(mypk);
			if (subflow.getSubFlowHidTodolist() == true)
			{
				GenerWorkFlow pgwf = new GenerWorkFlow(PWorkID);
				String mysql = "SELECT COUNT(WorkID) as Num FROM WF_GenerWorkFlow WHERE PWorkID=" + gwf.getPWorkID() + " AND FK_Flow='" + wn.getHisFlow().getNo() + "' AND WFState !=3 ";
				if (DBAccess.RunSQLReturnValInt(mysql, 0) == 0)
					DBAccess.RunSQL("UPDATE WF_GenerWorkerlist SET IsPass=0 Where WorkID=" + pgwf.getWorkID()+" AND FK_Node="+pgwf.getFK_Node());
			}
		}

		return wn.HisMsgObjs;

	}
}