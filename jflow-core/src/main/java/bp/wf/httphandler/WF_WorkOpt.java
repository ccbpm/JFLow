package bp.wf.httphandler;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.difference.handler.CommonFileUtils;
import bp.difference.handler.WebContralBase;
import bp.sys.*;
import bp.tools.DateUtils;
import bp.tools.Json;
import bp.web.*;
import bp.port.*;
import bp.pub.RTFEngine;
import bp.en.*;
import bp.wf.*;
import bp.wf.Glo;
import bp.wf.data.*;
import bp.wf.port.WFEmp;
import bp.wf.template.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import java.io.*;
import java.net.URLDecoder;
import java.time.*;

/**
 * 椤甸潰鍔熻兘瀹炰綋g
 */
public class WF_WorkOpt extends WebContralBase {
	/**
	 * 杩囩▼鎵ц.
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String ccbpmServices() throws Exception {
		bp.wf.dts.ccbpmServices en = new bp.wf.dts.ccbpmServices();
		en.Do();
		return "鎵ц鎴愬姛锛岃妫�鏌�:\\DataUser\\Log\\涓嬮潰鐨勬墽琛屼俊鎭�� ";
	}

	/**
	 * 鍒犻櫎瀛愮嚎绋�
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String ThreadDtl_DelSubFlow() throws Exception {
		bp.wf.Dev2Interface.Flow_DeleteSubThread(this.getWorkID(), "鎵嬪伐鍒犻櫎");
		return "鍒犻櫎鎴愬姛";
	}
	public final String UsefulExpresFlow_Init() throws Exception
	{
		//AttrKey =WorkCheck, FlowBBS, WorkReturn
		String attrKey = this.GetRequestVal("AttrKey");

		FastInputs ens = new FastInputs();
		ens.Retrieve(FastInputAttr.CfgKey, "Flow", FastInputAttr.EnsName,
				"Flow", FastInputAttr.AttrKey, attrKey, FastInputAttr.FK_Emp, WebUser.getNo());

		if (ens.size() > 0)
			return ens.ToJson();
	

		if (attrKey.equals("Comment"))
		{
			FastInput en = new FastInput();
			en.setMyPK(DBAccess.GenerGUID());
			en.setEnsName("Flow");
			en.setCfgKey("Flow");
			en.setAttrKey(attrKey);
			en.setVals("已阅");
			en.setFK_Emp(WebUser.getNo());
			en.Insert();
		}
		if (attrKey.equals("CYY"))
		{
			FastInput en = new FastInput();
			en.setMyPK(DBAccess.GenerGUID());
			en.setEnsName("Flow");
			en.setCfgKey("Flow");
			en.setAttrKey(attrKey);
			en.setVals( "同意");
			en.setFK_Emp(WebUser.getNo());
			en.Insert();

			en = new FastInput();
			en.setMyPK( DBAccess.GenerGUID());
			en.setEnsName("Flow");
			en.setCfgKey("Flow");
			en.setAttrKey(attrKey);
			en.setVals("不同意");
			en.setFK_Emp(WebUser.getNo());
			en.Insert();

			en = new FastInput();
			en.setMyPK(DBAccess.GenerGUID());
			en.setEnsName("Flow");
			en.setCfgKey("Flow");
			en.setAttrKey(attrKey);
			en.setVals("同意，请领导批示");
			en.setFK_Emp(WebUser.getNo());
			en.Insert();

			en = new FastInput();
			en.setMyPK(DBAccess.GenerGUID());
			en.setEnsName("Flow");
			en.setCfgKey("Flow");
			en.setAttrKey( attrKey);

			en.setVals( "同意办理");
			en.setFK_Emp( WebUser.getNo());
			en.Insert();

			en = new FastInput();
			en.setMyPK( DBAccess.GenerGUID());
			en.setEnsName("Flow");
			en.setCfgKey("Flow");
			en.setAttrKey(attrKey);

			en.setVals("情况属实报领导批准");
			en.setFK_Emp(WebUser.getNo());
			en.Insert();
		}

		ens.Retrieve(FastInputAttr.CfgKey, "Flow", FastInputAttr.EnsName, "Flow", FastInputAttr.AttrKey, attrKey, 
				FastInputAttr.FK_Emp, WebUser.getNo());
		return ens.ToJson();
	}

	/// 鎵撳嵃 rtf
	/**
	 * 鍒濆鍖�
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String PrintDoc_Init() throws Exception {
		String sourceType = this.GetRequestVal("SourceType");
		String FK_MapData = "";
		Node nd = null;
		if (this.getFK_Node() != 0 && this.getFK_Node() != 9999) {
			nd = new Node(this.getFK_Node());

			if (nd.getHisFormType() == NodeFormType.SheetTree) {
				// 鑾峰彇璇ヨ妭鐐圭粦瀹氱殑琛ㄥ崟
				// 鎵�鏈夎〃鍗曢泦鍚�.
				MapDatas mds = new MapDatas();
				mds.RetrieveInSQL(
						"SELECT FK_Frm FROM WF_FrmNode WHERE FK_Node=" + this.getFK_Node() + " AND FrmEnableRole !=5");
				return "info@" + bp.tools.Json.ToJson(mds.ToDataTableField());
			}

			FK_MapData = "ND" + this.getFK_Node();

			if (nd.getHisFormType() == NodeFormType.RefOneFrmTree) {
				FK_MapData = nd.getNodeFrmID();
			}

			if (nd.getHisFormType() == NodeFormType.SDKForm || nd.getHisFormType() == NodeFormType.SelfForm) {
				return "err@SDK琛ㄥ崟銆佸祵鍏ュ紡琛ㄥ崟鏆傛椂涓嶆敮鎸佹墦鍗板姛鑳�";
			}
		}
		if (DataType.IsNullOrEmpty(sourceType) == false && sourceType.equals("Bill")) {
			FK_MapData = this.GetRequestVal("FrmID");
		}

		BillTemplates templetes = new BillTemplates();
		String billNo = this.GetRequestVal("FK_Bill");
		if (billNo == null) {
			templetes.Retrieve(BillTemplateAttr.FK_MapData, FK_MapData);
		} else {
			templetes.Retrieve(BillTemplateAttr.FK_MapData, this.getFK_MapData(), BillTemplateAttr.No, billNo);
		}

		if (templetes.size() == 0) {
			return "err@褰撳墠鑺傜偣涓婃病鏈夌粦瀹氬崟鎹ā鏉裤��";
		}

		if (templetes.size() == 1) {
			BillTemplate templete = templetes.get(0) instanceof BillTemplate ? (BillTemplate) templetes.get(0) : null;

			// 鍗曟嵁鐨勬墦鍗�
			if (DataType.IsNullOrEmpty(sourceType) == false && sourceType.equals("Bill")) {
				return PrintDoc_FormDoneIt(null, this.getWorkID(), this.getFID(), FK_MapData, templete);
			}

			if (nd != null && nd.getHisFormType() == NodeFormType.RefOneFrmTree) {
				return PrintDoc_FormDoneIt(null, this.getWorkID(), this.getFID(), FK_MapData, templete);
			}

			return PrintDoc_DoneIt(templete.getNo());
		}
		return templetes.ToJson();
	}

	/**
	 * 鎵ц鎵撳嵃
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String PrintDoc_Done() throws Exception {

		String billTemplateNo = this.GetRequestVal("FK_Bill");
		return PrintDoc_DoneIt(billTemplateNo);
	}

	/**
	 * 鎵撳嵃pdf.
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */

	public final String PrintDoc_DoneIt() throws Exception {
		return PrintDoc_DoneIt(null);
	}

	public final String PrintDoc_DoneIt(String billTemplateNo) throws Exception {
		Node nd = new Node(this.getFK_Node());

		if (billTemplateNo == null) {
			billTemplateNo = this.GetRequestVal("FK_Bill");
		}

		BillTemplate func = new BillTemplate(billTemplateNo);

		// 濡傛灉涓嶆槸 BillTemplateExcel 鎵撳嵃.
		if (func.getTemplateFileModel() == TemplateFileModel.VSTOForExcel) {
			return "url@httpccword://-fromccflow,App=BillTemplateExcel,TemplateNo=" + func.getNo() + ",WorkID="
					+ this.getWorkID() + ",FK_Flow=" + this.getFK_Flow() + ",FK_Node=" + this.getFK_Node() + ",UserNo="
					+ WebUser.getNo() + ",SID=" + WebUser.getSID();
		}

		// 濡傛灉涓嶆槸 BillTemplateWord 鎵撳嵃
		if (func.getTemplateFileModel() == TemplateFileModel.VSTOForWord) {
			return "url@httpccword://-fromccflow,App=BillTemplateWord,TemplateNo=" + func.getNo() + ",WorkID="
					+ this.getWorkID() + ",FK_Flow=" + this.getFK_Flow() + ",FK_Node=" + this.getFK_Node() + ",UserNo="
					+ WebUser.getNo() + ",SID=" + WebUser.getSID();
		}

		String billInfo = "";

		String ccformId = this.GetRequestVal("CCFormID");
		if (DataType.IsNullOrEmpty(ccformId) == false) {
			return PrintDoc_FormDoneIt(nd, this.getWorkID(), this.getFID(), ccformId, func);
		}

		Work wk = nd.getHisWork();
		wk.setOID(this.getWorkID());
		wk.RetrieveFromDBSources();

		String file = DateUtils.getYear(new Date()) + "_" + WebUser.getFK_Dept() + "_" + func.getNo() + "_"
				+ getWorkID() + ".doc";
		RTFEngine rtf = new RTFEngine();

		String[] paths;
		String path;
		long newWorkID = 0;
		try {

			/// 鍗曟嵁鍙橀噺.
			Bill bill = new Bill();
			bill.setMyPK(wk.getFID() + "_" + wk.getOID() + "_" + nd.getNodeID() + "_" + func.getNo());

			///

			/// 鐢熸垚鍗曟嵁
			rtf.getHisEns().clear();
			rtf.getEnsDataDtls().clear();
			if (func.getNodeID() != 0) {
				// 鎶婃祦绋嬩富琛ㄦ暟鎹斁鍏ラ噷闈㈠幓.
				GEEntity ndxxRpt = new GEEntity("ND" + Integer.parseInt(nd.getFK_Flow()) + "Rpt");
				try {
					ndxxRpt.setPKVal(this.getWorkID());
					ndxxRpt.Retrieve();

					newWorkID = this.getWorkID();
				} catch (RuntimeException ex) {
					if (getFID() > 0) {
						ndxxRpt.setPKVal(this.getFID());
						ndxxRpt.Retrieve();

						newWorkID = this.getFID();

						wk = null;
						wk = nd.getHisWork();
						wk.setOID(this.getWorkID());
						wk.RetrieveFromDBSources();
					} else {
						bp.wf.dts.InitBillDir dir = new bp.wf.dts.InitBillDir();
						dir.Do();
						path = bp.wf.Glo.getFlowFileBill() + DateUtils.getYear(new Date()) + "\\" + WebUser.getFK_Dept()
								+ "\\" + func.getNo() + "\\";
						String msgErr = "@" + String.format("鐢熸垚鍗曟嵁澶辫触锛岃璁╃鐞嗗憳妫�鏌ョ洰褰曡缃�") + "[" + bp.wf.Glo.getFlowFileBill()
								+ "]銆侤Err锛�" + ex.getMessage() + " @File=" + file + " @Path:" + path;
						billInfo += "@<font color=red>" + msgErr + "</font>";
						throw new RuntimeException(msgErr + "@鍏跺畠淇℃伅:" + ex.getMessage());
					}
				}
				ndxxRpt.Copy(wk);

				// 鎶婃暟鎹祴鍊肩粰wk. 鏈夊彲鑳界敤鎴疯繕娌℃湁鎵ц娴佺▼妫�鏌ワ紝瀛楁娌℃湁鍚屾鍒� NDxxxRpt.
				if (ndxxRpt.getRow().size() > wk.getRow().size()) {
					wk.setRow(ndxxRpt.getRow());
				}

				rtf.HisGEEntity = wk;

				// 鍔犲叆浠栫殑鏄庣粏琛�.
				ArrayList<Entities> al = wk.GetDtlsDatasOfList();
				for (Entities ens : al) {
					rtf.AddDtlEns(ens);
				}

				// 澧炲姞澶氶檮浠舵暟鎹�
				FrmAttachments aths = wk.getHisFrmAttachments();
				for (FrmAttachment athDesc : aths.ToJavaList()) {
					FrmAttachmentDBs athDBs = new FrmAttachmentDBs();
					if (athDBs.Retrieve(FrmAttachmentDBAttr.FK_FrmAttachment, athDesc.getMyPK(),
							FrmAttachmentDBAttr.RefPKVal, newWorkID, "RDT") == 0) {
						continue;
					}

					rtf.getEnsDataAths().put(athDesc.getNoOfObj(), athDBs);
				}

				// 鎶婂鏍告棩蹇楄〃鍔犲叆閲岄潰鍘�.
				Paras ps = new bp.da.Paras();
				ps.SQL = "SELECT * FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track WHERE ActionType="
						+ SystemConfig.getAppCenterDBVarStr() + "ActionType AND WorkID="
						+ SystemConfig.getAppCenterDBVarStr() + "WorkID";
				ps.Add(TrackAttr.ActionType, ActionType.WorkCheck.getValue());
				ps.Add(TrackAttr.WorkID, newWorkID);

				rtf.dtTrack = DBAccess.RunSQLReturnTable(ps);
			}

			paths = file.split("[_]", -1);
			path = paths[0] + "/" + paths[1] + "/" + paths[2] + "/";
			String fileModelT = "rtf";
			if (func.getTemplateFileModel().getValue() == 1) {
				fileModelT = "word";
			}
			String billUrl = "url@" + fileModelT + "@" + bp.wf.Glo.getCCFlowAppPath() + "DataUser/Bill/" + path + file;

			if (func.getHisBillFileType() == BillFileType.PDF) {
				billUrl = billUrl.replace(".doc", ".pdf");
			}

			path = bp.wf.Glo.getFlowFileBill() + DateUtils.getYear(new Date()) + "\\" + WebUser.getFK_Dept() + "\\"
					+ func.getNo() + "\\";
			// path = Server.MapPath(path);
			if ((new File(path)).isDirectory() == false) {
				(new File(path)).mkdirs();
			}

			String tempFile = func.getTempFilePath();
			if (tempFile.contains(".rtf") == false) {
				tempFile = tempFile + ".rtf";
			}

			// 鐢ㄤ簬鎵弿鎵撳嵃.
			String qrUrl = SystemConfig.getHostURL() + "WF/WorkOpt/PrintDocQRGuide.htm?MyPK=" + bill.getMyPK();
			rtf.MakeDoc(tempFile, path, file, false);

			///

			/// 杞寲鎴恜df.
			if (func.getHisBillFileType() == BillFileType.PDF) {
				String rtfPath = path + file;
				String pdfPath = rtfPath.replace(".doc", ".pdf");
				try {
					bp.wf.Glo.Rtf2PDF(rtfPath, pdfPath);
				} catch (RuntimeException ex) {
					return "err@" + ex.getMessage();
				}
			}

			///

			/// 淇濆瓨鍗曟嵁.

			bill.setFID(wk.getFID());
			bill.setWorkID(wk.getOID());
			bill.setFK_Node(wk.getNodeID());
			bill.setFK_Dept(WebUser.getFK_Dept());
			bill.setFK_Emp(WebUser.getNo());
			bill.setUrl(billUrl);
			bill.setRDT(DataType.getCurrentDataTime());
			bill.setFullPath(path + file);
			bill.setFK_NY(DataType.getCurrentYearMonth());
			bill.setFK_Flow(nd.getFK_Flow());
			// bill.FK_BillType = func.FK_BillType;
			bill.setEmps(rtf.HisGEEntity.GetValStrByKey("Emps"));
			bill.setFK_Starter(rtf.HisGEEntity.GetValStrByKey("Rec"));
			bill.setStartDT(rtf.HisGEEntity.GetValStrByKey("RDT"));
			bill.setTitle(rtf.HisGEEntity.GetValStrByKey("Title"));
			bill.setFK_Dept(rtf.HisGEEntity.GetValStrByKey("FK_Dept"));

			try {
				bill.Save();
			} catch (java.lang.Exception e) {
				bill.Update();
			}

			///

			// 鍦ㄧ嚎WebOffice鎵撳紑
			if (func.getBillOpenModel() == BillOpenModel.WebOffice) {
				return "err@銆�/WF/WebOffice/PrintOffice.htm銆戣鏂囦欢娌℃湁閲嶆瀯濂�,鎮ㄥ彲浠ユ壘鍒版棫鐗堟湰瑙ｅ喅锛屾垨鑰呰嚜宸卞紑鍙戙��";
			}
			return billUrl;
		} catch (RuntimeException ex) {
			bp.wf.dts.InitBillDir dir = new bp.wf.dts.InitBillDir();
			dir.Do();
			path = bp.wf.Glo.getFlowFileBill() + DateUtils.getYear(new Date()) + "\\" + WebUser.getFK_Dept() + "\\"
					+ func.getNo() + "\\";
			String msgErr = "@" + String.format("鐢熸垚鍗曟嵁澶辫触锛岃璁╃鐞嗗憳妫�鏌ョ洰褰曡缃�") + "[" + bp.wf.Glo.getFlowFileBill() + "]銆侤Err锛�"
					+ ex.getMessage() + " @File=" + file + " @Path:" + path;
			return "err@<font color=red>" + msgErr + "</font>" + ex.getMessage();
		}
	}

	///

	public final String PrintDoc_FormDoneIt(Node nd, long workID, long fid, String formID, BillTemplate func)
			throws Exception {
		long pkval = workID;
		Work wk = null;
		String billInfo = "";
		if (nd != null) {
			bp.wf.template.FrmNode fn = new FrmNode();
			fn = new FrmNode(nd.getNodeID(), formID);
			// 鍏堝垽鏂В鍐虫柟妗�
			if (fn != null && fn.getWhoIsPK() != WhoIsPK.OID) {
				if (fn.getWhoIsPK() == WhoIsPK.PWorkID) {
					pkval = this.getPWorkID();
				}
				if (fn.getWhoIsPK() == WhoIsPK.FID) {
					pkval = fid;
				}
			}
			wk = nd.getHisWork();
			wk.setOID(this.getWorkID());
			wk.RetrieveFromDBSources();
		}

		MapData mapData = new MapData(formID);

		String file = DateUtils.getYear(new Date()) + "_" + WebUser.getFK_Dept() + "_" + func.getNo() + "_"
				+ getWorkID() + ".doc";
		RTFEngine rtf = new RTFEngine();

		String[] paths;
		String path;
		long newWorkID = 0;
		try {

			/// 鍗曟嵁鍙橀噺.
			Bill bill = new Bill();
			if (nd != null) {
				bill.setMyPK(wk.getFID() + "_" + wk.getOID() + "_" + nd.getNodeID() + "_" + func.getNo());
			} else {
				bill.setMyPK(fid + "_" + workID + "_0_" + func.getNo());
			}

			///

			/// 鐢熸垚鍗曟嵁
			rtf.getHisEns().clear();
			rtf.getEnsDataDtls().clear();
			if (DataType.IsNullOrEmpty(func.getFK_MapData()) == false) {
				// 鎶婃祦绋嬩富琛ㄦ暟鎹斁鍏ラ噷闈㈠幓.
				GEEntity ndxxRpt = new GEEntity(formID);
				try {
					ndxxRpt.setPKVal(pkval);
					ndxxRpt.Retrieve();

					newWorkID = pkval;
				} catch (RuntimeException ex) {
					if (getFID() > 0) {
						ndxxRpt.setPKVal(this.getFID());
						ndxxRpt.Retrieve();

						newWorkID = this.getFID();

						wk = null;
						wk = nd.getHisWork();
						wk.setOID(this.getWorkID());
						wk.RetrieveFromDBSources();
					} else {
						bp.wf.dts.InitBillDir dir = new bp.wf.dts.InitBillDir();
						dir.Do();
						path = bp.wf.Glo.getFlowFileBill() + DateUtils.getYear(new Date()) + "\\" + WebUser.getFK_Dept()
								+ "\\" + func.getNo() + "\\";
						String msgErr = "@" + String.format("鐢熸垚鍗曟嵁澶辫触锛岃璁╃鐞嗗憳妫�鏌ョ洰褰曡缃�") + "[" + bp.wf.Glo.getFlowFileBill()
								+ "]銆侤Err锛�" + ex.getMessage() + " @File=" + file + " @Path:" + path;
						billInfo += "@<font color=red>" + msgErr + "</font>";
						throw new RuntimeException(msgErr + "@鍏跺畠淇℃伅:" + ex.getMessage());
					}
				}
				// ndxxRpt.Copy(wk);

				// 鎶婃暟鎹祴鍊肩粰wk. 鏈夊彲鑳界敤鎴疯繕娌℃湁鎵ц娴佺▼妫�鏌ワ紝瀛楁娌℃湁鍚屾鍒� NDxxxRpt.
				// if (ndxxRpt.Row.size() > wk.Row.size())
				// wk.Row = ndxxRpt.Row;

				rtf.HisGEEntity = ndxxRpt;

				// 鍔犲叆浠栫殑鏄庣粏琛�.
				ArrayList<Entities> al = mapData.GetDtlsDatasOfList(String.valueOf(pkval));
				if (al.isEmpty()) {
					MapDtls mapdtls = mapData.getMapDtls();
					for (MapDtl dtl : mapdtls.ToJavaList()) {
						GEDtls dtls1 = new GEDtls(dtl.getNo());
						mapData.getEnMap().AddDtl(dtls1, "RefPK");
					}
					al = mapData.GetDtlsDatasOfList(String.valueOf(pkval));
				}
				for (Entities ens : al) {
					rtf.AddDtlEns(ens);
				}

				// 澧炲姞澶氶檮浠舵暟鎹�
				FrmAttachments aths = mapData.getFrmAttachments();
				for (FrmAttachment athDesc : aths.ToJavaList()) {
					FrmAttachmentDBs athDBs = new FrmAttachmentDBs();
					if (athDBs.Retrieve(FrmAttachmentDBAttr.FK_FrmAttachment, athDesc.getMyPK(),
							FrmAttachmentDBAttr.RefPKVal, newWorkID, "RDT") == 0) {
						continue;
					}

					rtf.getEnsDataAths().put(athDesc.getNoOfObj(), athDBs);
				}

				if (nd != null) {
					// 鎶婂鏍告棩蹇楄〃鍔犲叆閲岄潰鍘�.
					Paras ps = new bp.da.Paras();
					ps.SQL = "SELECT * FROM ND" + Integer.parseInt(nd.getFK_Flow()) + "Track WHERE ActionType="
							+ SystemConfig.getAppCenterDBVarStr() + "ActionType AND WorkID="
							+ SystemConfig.getAppCenterDBVarStr() + "WorkID";
					ps.Add(TrackAttr.ActionType, ActionType.WorkCheck.getValue());
					ps.Add(TrackAttr.WorkID, newWorkID);

					rtf.dtTrack = DBAccess.RunSQLReturnTable(ps);
				}
			}

			paths = file.split("[_]", -1);
			path = paths[0] + "/" + paths[1] + "/" + paths[2] + "/";
			String fileModelT = "rtf";
			if (func.getTemplateFileModel().getValue() == 1) {
				fileModelT = "word";
			}

			String billUrl = "url@" + fileModelT + "@" + bp.wf.Glo.getCCFlowAppPath() + "DataUser/Bill/" + path + file;

			if (func.getHisBillFileType() == BillFileType.PDF) {
				billUrl = billUrl.replace(".doc", ".pdf");
			}

			path = bp.wf.Glo.getFlowFileBill() + DateUtils.getYear(new Date()) + "\\" + WebUser.getFK_Dept() + "\\"
					+ func.getNo() + "\\";
			// path = Server.MapPath(path);
			if ((new File(path)).isDirectory() == false) {
				(new File(path)).mkdirs();
			}

			String tempFile = func.getTempFilePath();
			if (tempFile.contains(".rtf") == false) {
				tempFile = tempFile + ".rtf";
			}

			// 鐢ㄤ簬鎵弿鎵撳嵃.
			String qrUrl = SystemConfig.getHostURL() + "WF/WorkOpt/PrintDocQRGuide.htm?MyPK=" + bill.getMyPK();
			rtf.MakeDoc(tempFile, path, file, false);

			///

			/// 杞寲鎴恜df.
			if (func.getHisBillFileType() == BillFileType.PDF) {
				String rtfPath = path + file;
				String pdfPath = rtfPath.replace(".doc", ".pdf");
				try {
					bp.wf.Glo.Rtf2PDF(rtfPath, pdfPath);
				} catch (RuntimeException ex) {
					return "err@" + ex.getMessage();
				}
			}

			///

			/// 淇濆瓨鍗曟嵁.
			if (nd != null) {
				bill.setFID(wk.getFID());
				bill.setWorkID(wk.getOID());
				bill.setFK_Node(wk.getNodeID());
				bill.setFK_Flow(nd.getFK_Flow());
			} else {
				bill.setFID(fid);
				bill.setWorkID(workID);
				bill.setFK_Node(0);
				bill.setFK_Flow("0");
			}
			bill.setFK_Dept(WebUser.getFK_Dept());
			bill.setFK_Emp(WebUser.getNo());
			bill.setUrl(billUrl);
			bill.setRDT(DataType.getCurrentDataTime());
			bill.setFullPath(path + file);
			bill.setFK_NY(DataType.getCurrentYearMonth());
			bill.setEmps(rtf.HisGEEntity.GetValStrByKey("Emps"));
			bill.setFK_Starter(rtf.HisGEEntity.GetValStrByKey("Rec"));
			bill.setStartDT(rtf.HisGEEntity.GetValStrByKey("RDT"));
			bill.setTitle(rtf.HisGEEntity.GetValStrByKey("Title"));
			bill.setFK_Dept(rtf.HisGEEntity.GetValStrByKey("FK_Dept"));

			try {
				bill.Save();
			} catch (java.lang.Exception e) {
				bill.Update();
			}

			///

			// 鍦ㄧ嚎WebOffice鎵撳紑
			if (func.getBillOpenModel() == BillOpenModel.WebOffice) {
				return "err@銆�/WF/WebOffice/PrintOffice.htm銆戣鏂囦欢娌℃湁閲嶆瀯濂�,鎮ㄥ彲浠ユ壘鍒版棫鐗堟湰瑙ｅ喅锛屾垨鑰呰嚜宸卞紑鍙戙��";
			}
			return billUrl;
		} catch (RuntimeException ex) {
			bp.wf.dts.InitBillDir dir = new bp.wf.dts.InitBillDir();
			dir.Do();
			path = bp.wf.Glo.getFlowFileBill() + DateUtils.getYear(new Date()) + "\\" + WebUser.getFK_Dept() + "\\"
					+ func.getNo() + "\\";
			String msgErr = "@" + String.format("鐢熸垚鍗曟嵁澶辫触锛岃璁╃鐞嗗憳妫�鏌ョ洰褰曡缃�") + "[" + bp.wf.Glo.getFlowFileBill() + "]銆侤Err锛�"
					+ ex.getMessage() + " @File=" + file + " @Path:" + path;
			return "err@<font color=red>" + msgErr + "</font>" + ex.getMessage();
		}
	}

	/**
	 * 鏋勯�犲嚱鏁�
	 */
	public WF_WorkOpt() {
	}

	/**
	 * 鎵撳寘涓嬭浇
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String Packup_Init() throws Exception {
		try {
			String sourceType = this.GetRequestVal("SourceType");
			// 鎵撳嵃鍗曟嵁瀹炰綋銆佸崟鎹〃鍗�
			if (DataType.IsNullOrEmpty(sourceType) == false && sourceType.equals("Bill")) {
				return MakeForm2Html.MakeBillToPDF(this.GetRequestVal("FrmID"), this.getWorkID(),
						this.GetRequestVal("BasePath"), false, this.GetRequestVal("html"));
			}
			int nodeID = this.getFK_Node();
			if (this.getFK_Node() == 0) {
				GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
				nodeID = gwf.getFK_Node();
			}

			Node nd = new Node(nodeID);
			// 鏍戝舰琛ㄥ崟鏂规鍗曠嫭鎵撳嵃
			if ((nd.getHisFormType() == NodeFormType.SheetTree && nd.getHisPrintPDFModle() == 1)) {
				// 鑾峰彇璇ヨ妭鐐圭粦瀹氱殑琛ㄥ崟
				// 鎵�鏈夎〃鍗曢泦鍚�.
				MapDatas mds = new MapDatas();
				mds.RetrieveInSQL(
						"SELECT FK_Frm FROM WF_FrmNode WHERE FK_Node=" + this.getFK_Node() + " AND FrmEnableRole != 5");
				return "info@" + bp.tools.Json.ToJson(mds.ToDataTableField());
			}

			return bp.wf.MakeForm2Html.MakeCCFormToPDF(nd, this.getWorkID(), this.getFK_Flow(), null, false,
					this.GetRequestVal("BasePath"), this.GetRequestVal("html"));

		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}

	/**
	 * 鐙珛琛ㄥ崟PDF鎵撳嵃
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String Packup_FromInit() throws Exception {
		try {
			int nodeID = this.getFK_Node();
			if (this.getFK_Node() == 0) {
				GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
				nodeID = gwf.getFK_Node();
			}
			Node nd = new Node(nodeID);
			return MakeForm2Html.MakeFormToPDF(this.GetRequestVal("FrmID"), this.GetRequestVal("FrmName"), nd,
					this.getWorkID(), this.getFK_Flow(), null, false, this.GetRequestVal("BasePath"));
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}

	/**
	 * 鎵弿浜岀淮鐮佽幏寰楁枃浠�.
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String PrintDocQRGuide_Init() throws Exception {
		try {
			int nodeID = this.getFK_Node();
			if (this.getFK_Node() == 0) {
				GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
				nodeID = gwf.getFK_Node();
			}

			Node nd = new Node(nodeID);
			Work wk = nd.getHisWork();
			return bp.wf.MakeForm2Html.MakeCCFormToPDF(nd, this.getWorkID(), this.getFK_Flow(), null, false,
					this.GetRequestVal("BasePath"));
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}

	/**
	 * 閫夋嫨琛ㄥ崟,鍙戣捣鍓嶇疆瀵艰埅.
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String StartGuideFrms_Init() throws Exception {
		bp.wf.template.FrmNodes fns = new bp.wf.template.FrmNodes();

		QueryObject qo = new QueryObject(fns);
		qo.AddWhere(FrmNodeAttr.FK_Node, Integer.parseInt(this.getFK_Flow() + "01"));
		qo.addAnd();
		qo.AddWhere(FrmNodeAttr.FrmEnableRole, bp.wf.template.FrmEnableRole.WhenHaveFrmPara.getValue());
		qo.addOrderBy(FrmNodeAttr.Idx);
		qo.DoQuery();

		for (bp.wf.template.FrmNode item : fns.ToJavaList()) {
			item.setGuanJianZiDuan(item.getHisFrm().getName());
		}
		return fns.ToJson();
	}

	/// 鍏枃澶勭悊.
	/**
	 * 鐩存帴涓嬭浇
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String DocWord_OpenByHttp() throws Exception {
		String DocName = this.GetRequestVal("DocName"); // 鑾峰彇涓婁紶鐨勫叕鏂囨ā鏉垮悕绉發z
		// 鐢熸垚鏂囦欢.
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		Flow fl = new Flow(this.getFK_Flow());

		String file = SystemConfig.getPathOfTemp() + "/" + DocName;
		DBAccess.GetFileFromDB(file, fl.getPTable(), "OID", String.valueOf(this.getWorkID()), "DocWordFile");
		return "../../DataUser/Temp/" + DocName;

	}

	/**
	 * 閲嶇疆鍏枃鏂囦欢.
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String DocWord_ResetFile() throws Exception {
		Flow fl = new Flow(this.getFK_Flow());
		String sql = "UPDATE " + fl.getPTable() + " SET DocWordFile=NULL WHERE OID=" + this.getWorkID();
		DBAccess.RunSQL(sql);
		return "閲嶆柊鐢熸垚妯＄増鎴愬姛.";
	}

	/**
	 * 鐢熸垚鏂囦欢妯＄増
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String DocWord_Init() throws Exception {
		BtnLab lab = new BtnLab(this.getFK_Node());
		if (lab.getOfficeBtnEnableInt() == 0) {
			return "err@褰撳墠鑺傜偣娌℃湁鍚敤鍏枃.";
		}

		// 棣栧厛鍒ゆ柇鏄惁鐢熸垚鍏枃鏂囦欢锛� todo.
		Flow fl = new Flow(this.getFK_Flow());
		byte[] val = DBAccess.GetByteFromDB(fl.getPTable(), "OID", String.valueOf(this.getWorkID()),
				FixFieldNames.DocWordFile);
		if (val != null) {
			return "info@OfficeBtnEnable=" + String.valueOf(lab.getOfficeBtnEnableInt()) + ";璇蜂笅杞芥枃浠�"; // 濡傛灉宸茬粡鏈夎繖涓ā鐗堜簡.
		}

		DocTemplate en = new DocTemplate();
		// 姹傚嚭瑕佺敓鎴愮殑妯＄増.
		DocTemplates ens = new DocTemplates();
		ens.Retrieve(DocTemplateAttr.FK_Node, this.getFK_Node());
		if (ens.size() > 1) {
			return "url@DocWordSelectDocTemp.htm";
		}

		// 濡傛灉娌℃湁妯＄増灏辩粰浠栦竴涓粯璁ょ殑妯＄増.
		if (ens.size() == 0) {
			en.setFilePath(SystemConfig.getPathOfDataUser() + "\\DocTemplete\\Default.docx");
		}

		if (ens.size() == 1) {
			en = ens.get(0) instanceof DocTemplate ? (DocTemplate) ens.get(0) : null;
		}

		DBAccess.SaveBytesToDB(en.getFileBytes(), fl.getPTable(), "OID", String.valueOf(this.getWorkID()),
				FixFieldNames.DocWordFile);
		return "info@OfficeBtnEnable=" + String.valueOf(lab.getOfficeBtnEnableInt()) + ";璇蜂笅杞芥枃浠�"; // 濡傛灉宸茬粡鏈夎繖涓ā鐗堜簡.
	}

	/**
	 * 涓婁紶
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String DocWord_Upload() throws Exception {
		String path = SystemConfig.getPathOfTemp() + DBAccess.GenerGUID() + ".docx";
		HttpServletRequest request = getRequest();
		String contentType = request.getContentType();
		if (contentType != null && contentType.indexOf("multipart/form-data") != -1) {
			if (new File(path).exists() == false)
				new File(path).mkdirs();
			
			try {
				CommonFileUtils.upload(request, "file", new File(path));
			} catch (Exception e) {
				e.printStackTrace();
				return "err@鎵ц澶辫触";
			}
		}
		
		Flow fl = new Flow(this.getFK_Flow());
		DBAccess.SaveFileToDB(path, fl.getPTable(), "OID", String.valueOf(this.getWorkID()), FixFieldNames.DocWordFile);

		return "涓婁紶鎴愬姛.";
	}

	/**
	 * 閫夋嫨涓�涓ā鐗�
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String DocWordSelectDocTemp_Imp() throws Exception {
		Node node = new Node(this.getFK_Node());
		if (node.getIsStartNode() == false) {
			return "err@涓嶆槸寮�濮嬭妭鐐逛笉鍙互鎵ц妯℃澘瀵煎叆.";
		}

		DocTemplate docTemplate = new DocTemplate(this.getNo());
		if ((new File(docTemplate.getFilePath())).isFile() == false) {
			return "err@閫夋嫨鐨勬ā鐗堟枃浠朵笉瀛樺湪,璇疯仈绯荤鐞嗗憳.";
		}

		byte[] bytes = bp.da.DataType.ConvertFileToByte(docTemplate.getFilePath());
		Flow fl = new Flow(this.getFK_Flow());
		DBAccess.SaveBytesToDB(bytes, fl.getPTable(), "OID", String.valueOf(this.getWorkID()),
				FixFieldNames.DocWordFile);
		return "妯℃澘瀵煎叆鎴愬姛.";
	}

	///

	/// 閫氱敤浜哄憳閫夋嫨鍣�.
	/**
	 * 閫氱敤浜哄憳閫夋嫨鍣↖nit
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String AccepterOfGener_Init() throws Exception {
		/* 鑾峰緱涓婁竴娆″彂閫佺殑浜哄憳鍒楄〃. */
		int toNodeID = this.GetRequestValInt("ToNode");

		// 鏌ヨ鍑烘潵,宸茬粡閫夋嫨鐨勪汉鍛�.
		SelectAccpers sas = new SelectAccpers();
		int i = sas.Retrieve(SelectAccperAttr.FK_Node, toNodeID, SelectAccperAttr.WorkID, this.getWorkID(),
				SelectAccperAttr.Idx);

		if (i == 0&&SystemConfig.getCustomerNo().equals("GJTLJ")==false) {
			// 鑾峰緱鏈�杩戠殑涓�涓獁orkid.
			String trackTable = "ND" + Integer.parseInt(this.getFK_Flow()) + "Track";
			Paras ps = new Paras();
			if (SystemConfig.getAppCenterDBType() == DBType.MSSQL) {
				ps.SQL = "SELECT TOP 1 Tag,EmpTo FROM " + trackTable + " WHERE NDTo="
						+ SystemConfig.getAppCenterDBVarStr() + "NDTo AND (ActionType=0 OR ActionType=1) AND EmpFrom="
						+ SystemConfig.getAppCenterDBVarStr() + "EmpFrom ORDER BY WorkID desc  ";
				ps.Add("NDTo", toNodeID);
				ps.Add("EmpFrom", WebUser.getNo());
			} else if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
				ps.SQL = "SELECT * FROM (SELECT  Tag,EmpTo,WorkID FROM " + trackTable + " A WHERE A.EmpFrom="
						+ SystemConfig.getAppCenterDBVarStr() + "EmpFrom AND A.NDFrom="
						+ SystemConfig.getAppCenterDBVarStr() + "NDFrom AND A.NDTo="
						+ SystemConfig.getAppCenterDBVarStr() + "NDTo AND (ActionType=0 OR ActionType=1) AND EmpFrom="
						+ SystemConfig.getAppCenterDBVarStr() + "EmpFrom ORDER BY WorkID DESC ) WHERE ROWNUM =1";
				ps.Add("EmpFrom", WebUser.getNo());
				ps.Add("NDFrom", this.getFK_Node());
				ps.Add("NDTo", toNodeID);
			} else if (SystemConfig.getAppCenterDBType() == DBType.MySQL) {
				ps.SQL = "SELECT Tag,EmpTo FROM " + trackTable + " A WHERE A.NDFrom="
						+ SystemConfig.getAppCenterDBVarStr() + "NDFrom AND A.NDTo="
						+ SystemConfig.getAppCenterDBVarStr() + "NDTo AND (ActionType=0 OR ActionType=1) AND EmpFrom="
						+ SystemConfig.getAppCenterDBVarStr() + "EmpFrom ORDER BY WorkID  DESC limit 1,1 ";
				ps.Add("NDFrom", this.getFK_Node());
				ps.Add("NDTo", toNodeID);
				ps.Add("EmpFrom", WebUser.getNo());
			} else if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL) {
				ps.SQL = "SELECT Tag,EmpTo FROM " + trackTable
						+ " A WHERE A.NDFrom=:NDFrom AND A.NDTo=:NDTo AND (ActionType=0 OR ActionType=1) AND EmpFrom=:EmpFrom ORDER BY WorkID  DESC limit 1 ";
				ps.Add("NDFrom", this.getFK_Node());
				ps.Add("NDTo", toNodeID);
				ps.Add("EmpFrom", WebUser.getNo());
			}

			DataTable dt = DBAccess.RunSQLReturnTable(ps);
			if (dt.Rows.size() != 0) {
				String tag = dt.Rows.get(0).getValue("Tag").toString();
				String emps = dt.Rows.get(0).getValue("EmpTo").toString();
				if (tag != null && tag.contains("EmpsAccepter=") == true) {
					String[] strs = tag.split("[@]", -1);
					for (String str : strs) {
						if (str == null || str.equals("") || str.contains("EmpsAccepter=") == false) {
							continue;
						}
						String[] mystr = str.split("[=]", -1);
						if (mystr.length == 2) {
							emps = mystr[1];
						}
					}
				}

				if (emps.contains(",") == true) {
					if (emps.contains("'") == true) {
						String[] strs = emps.split("[;]", -1);
						for (String str : strs) {
							String[] emp = str.split("[,]", -1);
							String empNo = emp[0];
							bp.wf.Dev2Interface.Node_AddNextStepAccepters(this.getWorkID(), toNodeID, empNo, false);
						}
					}
				} else {
					bp.wf.Dev2Interface.Node_AddNextStepAccepters(this.getWorkID(), toNodeID, emps, false);
				}
			}

			if (dt.Rows.size() != 0) {
				sas.Retrieve(SelectAccperAttr.FK_Node, toNodeID, SelectAccperAttr.WorkID, this.getWorkID());
			}
		}
		// 鍒ゆ柇浜哄憳鏄惁宸茬粡鍒犻櫎
		if (sas.size() != 0) {
			for (int k = sas.size() - 1; k >= 0; k--) {
				SelectAccper sa = sas.get(k) instanceof SelectAccper ? (SelectAccper) sas.get(k) : null;
				Emp emp = new Emp();
				int j = emp.Retrieve(EmpAttr.No, sa.getFK_Emp());
				if (j == 0 || emp.getFK_Dept().equals("")) {
					sas.RemoveEn(sa);
					sa.Delete();
				}

			}
		}
		return sas.ToJson();
	}

	/**
	 * 澧炲姞鎺ユ敹浜�.
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String AccepterOfGener_AddEmps() throws Exception {
		try {
			// 鍒拌揪鐨勮妭鐐笽D.
			int toNodeID = this.GetRequestValInt("ToNode");
			String emps = this.GetRequestVal("AddEmps");

			// 澧炲姞鍒伴噷闈㈠幓.
			bp.wf.Dev2Interface.Node_AddNextStepAccepters(this.getWorkID(), toNodeID, emps, false);

			// 鏌ヨ鍑烘潵,宸茬粡閫夋嫨鐨勪汉鍛�.
			SelectAccpers sas = new SelectAccpers();
			sas.Retrieve(SelectAccperAttr.FK_Node, toNodeID, SelectAccperAttr.WorkID, this.getWorkID(),
					SelectAccperAttr.Idx);

			return sas.ToJson();
		} catch (RuntimeException ex) {
			if (ex.getMessage().contains("INSERT") == true) {
				return "err@浜哄憳鍚嶇О閲嶅,瀵艰嚧閮ㄥ垎浜哄憳鎻掑叆澶辫触.";
			}

			return "err@" + ex.getMessage();
		}
	}

	/**
	 * 鎵ц鍙戦��.
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String AccepterOfGener_Send() throws Exception {
		try {
			int toNodeID = this.GetRequestValInt("ToNode");
			Node nd = new Node(toNodeID);
			if (nd.getHisDeliveryWay() == DeliveryWay.BySelected) {
				/* 浠呬粎璁剧疆涓�涓�,妫�鏌ュ帇鍏ョ殑浜哄憳涓暟. */
				Paras ps = new Paras();
				ps.SQL = "SELECT count(WorkID) as Num FROM WF_SelectAccper WHERE FK_Node="
						+ SystemConfig.getAppCenterDBVarStr() + "FK_Node AND WorkID="
						+ SystemConfig.getAppCenterDBVarStr() + "WorkID AND AccType=0";
				ps.Add("FK_Node", toNodeID);
				ps.Add("WorkID", this.getWorkID());
				int num = DBAccess.RunSQLReturnValInt(ps, 0);
				if (num == 0) {
					return "err@璇锋寚瀹氫笅涓�姝ュ伐浣滅殑澶勭悊浜�.";
				}
				Selector sr = new Selector(toNodeID);
				if (sr.getIsSimpleSelector() == true) {
					if (num != 1) {
						return "err@鎮ㄥ彧鑳介�夋嫨涓�涓帴鍙椾汉,璇风Щ闄ゅ叾浠栫殑鎺ュ彈浜虹劧鍚庢墽琛屽彂閫�.";
					}
				}
			}

			SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(this.getFK_Flow(), this.getWorkID(), toNodeID,
					null);
			String strs = objs.ToMsgOfHtml();
			strs = strs.replace("@", "<br>@");

			/// 澶勭悊鍙戦�佸悗杞悜.
			// 褰撳墠鑺傜偣.
			Node currNode = new Node(this.getFK_Node());
			/* 澶勭悊杞悜闂. */
			switch (currNode.getHisTurnToDeal()) {
			case SpecUrl:
				String myurl = currNode.getTurnToDealDoc();
				if (myurl.contains("?") == false) {
					myurl += "?1=1";
				}
				Attrs myattrs = currNode.getHisWork().getEnMap().getAttrs();
				Work hisWK = currNode.getHisWork();
				for (Attr attr : myattrs) {
					if (myurl.contains("@") == false) {
						break;
					}
					myurl = myurl.replace("@" + attr.getKey(), hisWK.GetValStrByKey(attr.getKey()));
				}
				myurl = myurl.replace("@WebUser.No", WebUser.getNo());
				myurl = myurl.replace("@WebUser.Name", WebUser.getName());
				myurl = myurl.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());

				if (myurl.contains("@")) {
					bp.wf.Dev2Interface.Port_SendMsg("admin",
							currNode.getName() + "鍦�" + currNode.getName() + "鑺傜偣澶勶紝鍑虹幇閿欒",
							"娴佺▼璁捐閿欒锛屽湪鑺傜偣杞悜url涓弬鏁版病鏈夎鏇挎崲涓嬫潵銆俇rl:" + myurl, "Err" + currNode.getNo() + "_" + this.getWorkID(),
							SMSMsgType.Err, this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), this.getFID());
					throw new RuntimeException("娴佺▼璁捐閿欒锛屽湪鑺傜偣杞悜url涓弬鏁版病鏈夎鏇挎崲涓嬫潵銆俇rl:" + myurl);
				}

				if (myurl.contains("PWorkID") == false) {
					myurl += "&PWorkID=" + this.getWorkID();
				}

				myurl += "&FromFlow=" + this.getFK_Flow() + "&FromNode=" + this.getFK_Node() + "&UserNo="
						+ WebUser.getNo() + "&SID=" + WebUser.getSID();
				return "TurnUrl@" + myurl;
			case TurnToByCond:

				return strs;
			default:
				strs = strs.replace("@WebUser.No", WebUser.getNo());
				strs = strs.replace("@WebUser.Name", WebUser.getName());
				strs = strs.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
				return strs;
			}

		} catch (RuntimeException ex) {
			AthUnReadLog athUnReadLog = new AthUnReadLog();
			athUnReadLog.CheckPhysicsTable();
			return "err@" + ex.getMessage();
		}
	}

	///

	// 鏌ヨselect闆嗗悎
	public final String AccepterOfGener_SelectEmps() throws Exception {
		String sql = "";
		String emp = this.GetRequestVal("TB_Emps");

		/// 淇濋殰鏌ヨ璇彞鐨勫畨鍏�.
		emp = emp.toLowerCase();
		emp = emp.replace("'", "");
		emp = emp.replace("&", "&amp");
		emp = emp.replace("<", "&lt");
		emp = emp.replace(">", "&gt");
		emp = emp.replace("delete", "");
		emp = emp.replace("update", "");
		emp = emp.replace("insert", "");

		/// 淇濋殰鏌ヨ璇彞鐨勫畨鍏�.

		boolean isPinYin = DBAccess.IsExitsTableCol("Port_Emp", "PinYin");
		if (isPinYin == true && SystemConfig.getCustomerNo().equals("GJTLJ") == false) {
			// 鏍囪瘑缁撴潫锛屼笉瑕乴ike鍚嶅瓧浜�.
			if (emp.contains("/")) {
				if (SystemConfig.getCustomerNo().equals("TianYe")) // 鍙敼浜唎racle鐨�
				{
					// string endSql = "";
					// if (WebUser.getFK_Dept().IndexOf("18099") == 0)
					// endSql = " AND B.No LIKE '18099%' ";
					// else
					// endSql = " AND B.No NOT LIKE '18099%' ";

					Object obj = SystemConfig.getAppSettings().get("SpecFlowNosForAccpter");
					String specFlowNos = obj == null ? null : obj.toString();
					if (DataType.IsNullOrEmpty(specFlowNos)==true)
						specFlowNos = ",001,";


					String specEmpNos = "";
					if (specFlowNos.contains(String.valueOf(this.getFK_Node()) + ",") == false) {
						specEmpNos = " AND a.No!='00000001' ";
					}

					sql = "SELECT a.No,a.Name || '/' || b.FullName as Name FROM Port_Emp a, Port_Dept b WHERE  (a.fk_dept=b.No) and (a.No like '%"
							+ emp + "%' OR a.NAME  LIKE '%" + emp + "%'  OR a.PinYin LIKE '%," + emp.toLowerCase()
							+ "%') AND rownum<=12 " + specEmpNos;
				} else {
					if (SystemConfig.getAppCenterDBType() == DBType.MSSQL) {
						sql = "SELECT TOP 12 a.No,a.Name +'/'+b.name as Name FROM Port_Emp a,Port_Dept b  WHERE  (a.fk_dept=b.No) and (a.No like '%"
								+ emp + "%' OR a.NAME  LIKE '%" + emp + "%'  OR a.PinYin LIKE '%," + emp.toLowerCase()
								+ "%') ";
					}
					if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
						sql = "SELECT a.No,a.Name || '/' || b.name as Name FROM Port_Emp a,Port_Dept b  WHERE  (a.fk_dept=b.No) and (a.No like '%"
								+ emp + "%' OR a.NAME  LIKE '%" + emp + "%'  OR a.PinYin LIKE '%," + emp.toLowerCase()
								+ "%') AND rownum<=12 ";
					}
					if (SystemConfig.getAppCenterDBType() == DBType.MySQL
							|| SystemConfig.getAppCenterDBType() == DBType.PostgreSQL) {
						sql = "SELECT a.No,CONCAT(a.Name,'/',b.Name) as Name FROM Port_Emp a,Port_Dept b  WHERE  (a.fk_dept=b.No) and (a.No like '%"
								+ emp + "%' OR a.NAME  LIKE '%" + emp + "%'  OR a.PinYin LIKE '%," + emp.toLowerCase()
								+ "%') LIMIT 12";
					}
				}
			} else {
				if (SystemConfig.getCustomerNo().equals("TianYe")) // 鍙敼浜唎racle鐨�
				{
					// string endSql = "";
					// if (WebUser.getFK_Dept().IndexOf("18099") == 0)
					// endSql = " AND B.No LIKE '18099%' ";
					// else
					// endSql = " AND B.No NOT LIKE '18099%' ";

					Object obj = SystemConfig.getAppSettings().get("SpecFlowNosForAccpter");
					String specFlowNos = obj == null ? null : obj.toString();

					if (DataType.IsNullOrEmpty(specFlowNos)==true) {
						specFlowNos = ",001,";
					}

					String specEmpNos = "";
					if (specFlowNos.contains(String.valueOf(this.getFK_Node()) + ",") == false) {
						specEmpNos = " AND a.No!='00000001' ";
					}

					Selector sa = new Selector(this.getFK_Node());
					// 鍚敤鎼滅储鑼冨洿闄愬畾.
					if (sa.getIsEnableStaRange() == true || sa.getIsEnableDeptRange() == true) {
						sql = "SELECT a.No,a.Name || '/' || b.FullName as Name FROM Port_Emp a, Port_Dept b, WF_NodeDept c WHERE  C.FK_Node='"
								+ GetRequestVal("ToNode")
								+ "' AND C.FK_Dept=b.No AND (a.fk_dept=b.No) AND (  a.PinYin LIKE '%,"
								+ emp.toLowerCase() + "%') AND rownum<=12   " + specEmpNos;
					} else {
						sql = "SELECT a.No,a.Name || '/' || b.FullName as Name FROM Port_Emp a,Port_Dept b  WHERE  (a.fk_dept=b.No) and (  a.PinYin LIKE '%,"
								+ emp.toLowerCase() + "%') AND rownum<=12   " + specEmpNos;
					}
				} else {
					if (SystemConfig.getAppCenterDBType() == DBType.MSSQL) {
						sql = "SELECT TOP 12 a.No,a.Name +'/'+b.name as Name FROM Port_Emp a,Port_Dept b  WHERE  (a.fk_dept=b.No) and ( a.PinYin LIKE '%,"
								+ emp.toLowerCase() + "%')";
					}
					if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
						sql = "SELECT a.No,a.Name || '/' || b.name as Name FROM Port_Emp a,Port_Dept b  WHERE  (a.fk_dept=b.No) and (  a.PinYin LIKE '%,"
								+ emp.toLowerCase() + "%') AND rownum<=12 ";
					}
					if (SystemConfig.getAppCenterDBType() == DBType.MySQL
							|| SystemConfig.getAppCenterDBType() == DBType.PostgreSQL) {
						sql = "SELECT a.No,CONCAT(a.Name,'/',b.Name) as Name FROM Port_Emp a,Port_Dept b  WHERE  (a.fk_dept=b.No) and (  a.PinYin LIKE '%,"
								+ emp.toLowerCase() + "%' ) LIMIT 12";
					}
				}
			}
		} else {
			if (SystemConfig.getAppCenterDBType() == DBType.MSSQL) {
				sql = "SELECT TOP 12 a.No,a.Name +'/'+b.name as Name FROM Port_Emp a,Port_Dept b  WHERE  (a.fk_dept=b.No) and (a.No like '%"
						+ emp + "%' OR a.NAME  LIKE '%" + emp + "%')";
			}
			if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
				sql = "SELECT a.No,a.Name || '/' || b.name as Name FROM Port_Emp a,Port_Dept b  WHERE  (a.fk_dept=b.No) and (a.No like '%"
						+ emp + "%' OR a.NAME  LIKE '%" + emp + "%') and rownum<=12 ";
			}
			if (SystemConfig.getAppCenterDBType() == DBType.MySQL
					|| SystemConfig.getAppCenterDBType() == DBType.PostgreSQL) {
				sql = "SELECT a.No,CONCAT(a.Name,'/',b.Name) as Name FROM Port_Emp a,Port_Dept b  WHERE  (a.fk_dept=b.No) and (a.No like '%"
						+ emp + "%' OR a.NAME  LIKE '%" + emp + "%') LIMIT 12";
			}
		}

		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		// bp.da.Log.DebugWriteError(sql);

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle
				|| SystemConfig.getAppCenterDBType() == DBType.PostgreSQL) {
			dt.Columns.get(0).setColumnName("No");
			dt.Columns.get(1).setColumnName("Name");
		}

		return bp.tools.Json.ToJson(dt);
	}

	/// 浼氱.
	/**
	 * 浼氱
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String HuiQian_Init() throws Exception {
		// 瑕佹壘鍒颁富鎸佷汉.
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		if (gwf.getHuiQianTaskSta() == HuiQianTaskSta.HuiQianOver) {
			return "err@浼氱宸ヤ綔宸茬粡瀹屾垚锛屾偍涓嶈兘鍦ㄦ墽琛屼細绛俱��";
		}

		String huiQianType = this.GetRequestVal("HuiQianType");
		// 鏌ヨ鍑烘潵闆嗗悎.
		GenerWorkerLists ens = new GenerWorkerLists(this.getWorkID(), this.getFK_Node());
		BtnLab btnLab = new BtnLab(this.getFK_Node());
		if (btnLab.getHuiQianRole() != HuiQianRole.TeamupGroupLeader
				|| (btnLab.getHuiQianRole() == HuiQianRole.TeamupGroupLeader
						&& btnLab.getHuiQianLeaderRole() != HuiQianLeaderRole.OnlyOne)) {
			for (GenerWorkerList item : ens.ToJavaList()) {

				if ((gwf.getHuiQianZhuChiRen().contains(item.getFK_Emp() + ",") == true
						|| (DataType.IsNullOrEmpty(gwf.getHuiQianZhuChiRen()) == true
								&& gwf.GetParaString("AddLeader").contains(item.getFK_Emp() + ",") == false
								&& gwf.getTodoEmps().contains(item.getFK_Emp() + ",") == true))
						&& !item.getFK_Emp().equals(WebUser.getNo()) && item.getIsHuiQian() == false) {
					item.setFK_EmpText("<img src='../Img/zhuichiren.png' border=0 />" + item.getFK_EmpText());
					item.setFK_EmpText(item.getFK_EmpText());
					if (item.getIsPass() == true) {
						item.setIsPassInt(1001);
					} else {
						item.setIsPassInt(100);
					}
					continue;
				}

				// 鏍囪涓鸿嚜宸�.
				if (item.getFK_Emp().equals(WebUser.getNo())) {
					item.setFK_EmpText("" + item.getFK_EmpText());
					if (item.getIsPass() == true) {
						item.setIsPassInt(9901);
					} else {
						item.setIsPassInt(99);
					}
				}
			}
		}

		// 璧嬪�奸儴闂ㄥ悕绉般��
		DataTable mydt = ens.ToDataTableField("WF_GenerWorkList");
		mydt.Columns.Add("FK_DeptT", String.class);
		for (DataRow dr : mydt.Rows) {
			String fk_emp = dr.getValue("FK_Emp").toString();
			for (GenerWorkerList item : ens.ToJavaList()) {
				if (item.getFK_Emp().equals(fk_emp)) {
					dr.setValue("FK_DeptT", item.getFK_DeptT());
				}
			}
		}

		// 鑾峰彇褰撳墠浜哄憳鐨勬祦绋嬪鐞嗕俊鎭�
		GenerWorkerList gwlOfMe = new GenerWorkerList();
		gwlOfMe.Retrieve(GenerWorkerListAttr.FK_Emp, WebUser.getNo(), GenerWorkerListAttr.WorkID, this.getWorkID(),
				GenerWorkerListAttr.FK_Node, this.getFK_Node());

		DataSet ds = new DataSet();
		ds.Tables.add(mydt);
		ds.Tables.add(gwlOfMe.ToDataTableField("My_GenerWorkList"));

		return bp.tools.Json.ToJson(ds);
	}

	/**
	 * 绉婚櫎
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String HuiQian_Delete() throws Exception {
		String emp = this.GetRequestVal("FK_Emp");
		if (this.getFK_Emp().equals(WebUser.getNo())) {
			return "err@鎮ㄤ笉鑳界Щ闄ゆ偍鑷繁";
		}

		// 瑕佹壘鍒颁富鎸佷汉.
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		String addLeader = gwf.GetParaString("AddLeader");
		if (gwf.getTodoEmps().contains(WebUser.getNo() + ",") == false
				&& addLeader.contains(WebUser.getNo() + ",") == false) {
			return "err@鎮ㄤ笉鏄富鎸佷汉锛屾偍涓嶈兘鍒犻櫎銆�";
		}

		// 鍒犻櫎璇ユ暟鎹�.
		GenerWorkerList gwlOfMe = new GenerWorkerList();
		gwlOfMe.Delete(GenerWorkerListAttr.FK_Emp, this.getFK_Emp(), GenerWorkerListAttr.WorkID, this.getWorkID(),
				GenerWorkerListAttr.FK_Node, this.getFK_Node());

		// 濡傛灉宸茬粡娌℃湁浼氱寰呭姙浜�,灏辫缃綋鍓嶄汉鍛樼姸鎬佷负0. 澧炲姞杩欓儴鍒�.
		Paras ps = new Paras();
		ps.SQL = "SELECT COUNT(WorkID) FROM WF_GenerWorkerList WHERE FK_Node=" + SystemConfig.getAppCenterDBVarStr()
				+ "FK_Node AND WorkID=" + SystemConfig.getAppCenterDBVarStr() + "WorkID AND IsPass=0 ";
		ps.Add("FK_Node", this.getFK_Node());
		ps.Add("WorkID", this.getWorkID());
		if (DBAccess.RunSQLReturnValInt(ps) == 0) {
			gwf.setHuiQianTaskSta(HuiQianTaskSta.None); // 璁剧疆涓� None .
														// 涓嶈兘璁剧疆浼氱瀹屾垚,涓嶇劧鍏朵粬鐨勫氨娌℃湁鍔炴硶澶勭悊浜�.
			gwf.Update();
			ps = new Paras();
			ps.SQL = "UPDATE WF_GenerWorkerList SET IsPass=0 WHERE FK_Node=" + SystemConfig.getAppCenterDBVarStr()
					+ "FK_Node AND WorkID=" + SystemConfig.getAppCenterDBVarStr() + "WorkID AND FK_Emp="
					+ SystemConfig.getAppCenterDBVarStr() + "FK_Emp";
			ps.Add("FK_Node", this.getFK_Node());
			ps.Add("WorkID", this.getWorkID());
			ps.Add("FK_Emp", WebUser.getNo());
			DBAccess.RunSQL(ps);
		}

		// 浠庡緟鍔為噷绉婚櫎.
		bp.port.Emp myemp = new bp.port.Emp(this.getFK_Emp());
		String str = gwf.getTodoEmps();
		str = str.replace(myemp.getNo() + "," + myemp.getName() + ";", "");
		str = str.replace(myemp.getName() + ";", "");

		addLeader = addLeader.replace(this.getFK_Emp() + ",", "");
		gwf.SetPara("AddLeader", addLeader);
		gwf.setTodoEmps(str);
		gwf.Update();

		return HuiQian_Init();
	}

	/**
	 * 澧炲姞瀹℃牳浜哄憳
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String HuiQian_AddEmps() throws Exception {
		String huiQianType = this.GetRequestVal("HuiQianType");
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		String addLeader = gwf.GetParaString("AddLeader");
		if (gwf.getTodoEmps().contains(WebUser.getNo() + ",") == false) {
			// 鍒ゆ柇鏄笉鏄浜屼細绛句富鎸佷汉
			if (addLeader.contains(WebUser.getNo() + ",") == false) {
				return "err@鎮ㄤ笉鏄細绛句富鎸佷汉锛屾偍涓嶈兘鎵ц璇ユ搷浣溿��";
			}
		}

		GenerWorkerList gwlOfMe = new GenerWorkerList();
		int num = gwlOfMe.Retrieve(GenerWorkerListAttr.FK_Emp, WebUser.getNo(), GenerWorkerListAttr.WorkID,
				this.getWorkID(), GenerWorkerListAttr.FK_Node, this.getFK_Node());

		Node nd = new Node(this.getFK_Node());
		if (num == 0) {
			return "err@娌℃湁鏌ヨ鍒板綋鍓嶄汉鍛樼殑宸ヤ綔鍒楄〃鏁版嵁.";
		}

		String empStrs = this.GetRequestVal("AddEmps");
		if (DataType.IsNullOrEmpty(empStrs) == true) {
			return "err@鎮ㄦ病鏈夐�夋嫨浜哄憳.";
		}

		String err = "";

		String[] emps = empStrs.split("[,]", -1);
		for (String empStr : emps) {
			if (DataType.IsNullOrEmpty(empStr) == true) {
				continue;
			}

			Emp emp = new Emp(empStr);

			// 鏌ユ煡鏄惁瀛樺湪闃熷垪閲岋紵
			num = gwlOfMe.Retrieve(GenerWorkerListAttr.FK_Emp, emp.getNo(), GenerWorkerListAttr.WorkID,
					this.getWorkID(), GenerWorkerListAttr.FK_Node, this.getFK_Node());

			if (num == 1) {
				err += " 浜哄憳[" + emp.getNo() + "," + emp.getName() + "]宸茬粡鍦ㄩ槦鍒楅噷.";
				continue;
			}

			// 澧炲姞缁勯暱
			if (DataType.IsNullOrEmpty(huiQianType) == false && huiQianType.equals("AddLeader")) {
				addLeader += emp.getNo() + ",";
			}

			// 鏌ヨ鍑烘潵鍏朵粬鍒楃殑鏁版嵁.
			gwlOfMe.Retrieve(GenerWorkerListAttr.FK_Emp, WebUser.getNo(), GenerWorkerListAttr.WorkID, this.getWorkID(),
					GenerWorkerListAttr.FK_Node, this.getFK_Node());
			gwlOfMe.SetPara("HuiQianType", "");
			gwlOfMe.setFK_Emp(emp.getNo());
			gwlOfMe.setFK_EmpText(emp.getName());
			gwlOfMe.setIsPassInt(-1); // 璁剧疆涓嶅彲浠ョ敤.
			gwlOfMe.setFK_Dept(emp.getFK_Dept());
			gwlOfMe.setFK_DeptT(emp.getFK_DeptText()); // 閮ㄩ棬鍚嶇О.
			gwlOfMe.setIsRead(false);
			gwlOfMe.SetPara("HuiQianZhuChiRen", WebUser.getNo());
			// 琛ㄦ槑鍚庡鍔犵殑缁勯暱
			if (DataType.IsNullOrEmpty(huiQianType) == false && huiQianType.equals("AddLeader")) {
				gwlOfMe.SetPara("HuiQianType", huiQianType);
			}

			/// 璁＄畻浼氱鏃堕棿.
			if (nd.getHisCHWay() == CHWay.None) {
				gwlOfMe.setSDT("鏃�");
			} else {
				Date dtOfShould = bp.wf.Glo.AddDayHoursSpan(new Date(), nd.getTimeLimit(), nd.getTimeLimitHH(),
						nd.getTimeLimitMM(), nd.getTWay());
				// 搴斿畬鎴愭棩鏈�.
				gwlOfMe.setSDT(DateUtils.format(dtOfShould, DataType.getSysDataTimeFormat() + ":ss"));
			}

			// 姹傝鍛婃棩鏈�.
			Date dtOfWarning = new Date();
			if (nd.getWarningDay() == 0) {
				// dtOfWarning = "鏃�";
			} else {
				// 璁＄畻璀﹀憡鏃ユ湡銆�
				// 澧炲姞灏忔椂鏁�. 鑰冭檻鍒颁簡鑺傚亣鏃�.
				dtOfWarning = bp.wf.Glo.AddDayHoursSpan(new Date(), nd.getWarningDay(), 0, 0, nd.getTWay());
			}
			gwlOfMe.setDTOfWarning(DateUtils.format(dtOfWarning, DataType.getSysDataTimeFormat()));

			/// #endregion 璁＄畻浼氱鏃堕棿.

			gwlOfMe.setSender(WebUser.getName()); // 鍙戦�佷汉涓哄綋鍓嶄汉.
			gwlOfMe.setIsHuiQian(true);
			gwlOfMe.Insert(); // 鎻掑叆浣滀负寰呭姙.

		}

		gwf.SetPara("AddLeader", addLeader);
		gwf.Update();
		if (err.equals("") == true) {
			return "澧炲姞鎴愬姛.";
		}

		return "err@" + err;
	}

	///

	/// 涓庝細绛剧浉鍏崇殑.
	// 鏌ヨselect闆嗗悎
	public final String HuiQian_SelectEmps() throws Exception {
		return AccepterOfGener_SelectEmps();
	}

	/**
	 * 澧炲姞涓绘寔浜�
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String HuiQian_AddLeader() throws Exception {
		// 鐢熸垚鍙橀噺.
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());

		if (gwf.getHuiQianTaskSta() == HuiQianTaskSta.HuiQianOver) {
			/* 鍙湁涓�涓汉鐨勬儏鍐典笅, 骞朵笖鏄細绛惧畬姣曠姸鎬侊紝灏辨墽琛� */
			return "info@褰撳墠宸ヤ綔宸茬粡鍒版偍鐨勫緟鍔炵悊浜�,浼氱宸ヤ綔宸茬粡瀹屾垚.";
		}
		String leaders = gwf.GetParaString("AddLeader");

		// 鑾峰彇鍔犵鐨勪汉
		GenerWorkerLists gwfs = new GenerWorkerLists();
		gwfs.Retrieve(GenerWorkerListAttr.WorkID, gwf.getWorkID(), GenerWorkerListAttr.FK_Node, gwf.getFK_Node(),
				GenerWorkerListAttr.IsPass, -1);
		String empsLeader = "鏂板涓绘寔浜�:";

		for (GenerWorkerList item : gwfs.ToJavaList()) {
			if (leaders.contains(item.getFK_Emp() + ",")) {
				empsLeader += item.getFK_Emp() + "," + item.getFK_EmpText() + ";";
				// 鍙戦�佹秷鎭�
				bp.wf.Dev2Interface.Port_SendMsg(item.getFK_Emp(), "bpm浼氱閭�璇�",
						"HuiQian" + gwf.getWorkID() + "_" + gwf.getFK_Node() + "_" + item.getFK_Emp(),
						WebUser.getName() + "閭�璇锋偍浣滀负宸ヤ綔锝�" + gwf.getTitle() + "锝濈殑涓绘寔浜�,璇锋偍鍦▄" + item.getSDT() + "}鍓嶅畬鎴�.",
						"HuiQian", gwf.getFK_Flow(), gwf.getFK_Node(), gwf.getWorkID(), gwf.getFID());
			}

		}
		if (DataType.IsNullOrEmpty(empsLeader) == true) {
			return "娌℃湁澧炲姞鏂扮殑涓绘寔浜�";
		}
		leaders = "('" + leaders.substring(0, leaders.length() - 1).replace(",", "','") + "')";
		// 鎭㈠浠栫殑鐘舵��.
		String sql = "UPDATE WF_GenerWorkerList SET IsPass=0 WHERE WorkID=" + this.getWorkID() + " AND FK_Node="
				+ this.getFK_Node() + " AND IsPass=-1 AND FK_Emp In" + leaders;
		DBAccess.RunSQL(sql);

		gwf.setTodoEmps(gwf.getTodoEmps() + empsLeader);
		gwf.setHuiQianTaskSta(HuiQianTaskSta.HuiQianing);
		Node nd = new Node(gwf.getFK_Node());
		if (nd.getHuiQianLeaderRole() == HuiQianLeaderRole.OnlyOne
				&& nd.getTodolistModel() == TodolistModel.TeamupGroupLeader) {

			gwf.setHuiQianZhuChiRen(WebUser.getNo());
			gwf.setHuiQianZhuChiRenName(WebUser.getName());
		} else {
			// 澶氫汉鐨勭粍闀挎ā寮忔垨鑰呭崗浣滄ā寮�
			if (DataType.IsNullOrEmpty(gwf.getHuiQianZhuChiRen()) == true) {
				gwf.setHuiQianZhuChiRen(gwf.getTodoEmps());
			}
		}

		gwf.Update();
		return "涓绘寔浜哄鍔犳垚鍔�";

	}

	/**
	 * 淇濆瓨骞跺叧闂�
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String HuiQian_SaveAndClose() throws Exception {
		// 鐢熸垚鍙橀噺.
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());

		if (gwf.getHuiQianTaskSta() == HuiQianTaskSta.HuiQianOver) {
			/* 鍙湁涓�涓汉鐨勬儏鍐典笅, 骞朵笖鏄細绛惧畬姣曠姸鎬侊紝灏辨墽琛� */
			return "info@褰撳墠宸ヤ綔宸茬粡鍒版偍鐨勫緟鍔炵悊浜�,浼氱宸ヤ綔宸茬粡瀹屾垚.";
		}

		if (gwf.getHuiQianTaskSta() == HuiQianTaskSta.None) {
			Paras ps = new Paras();
			ps.SQL = "SELECT COUNT(WorkID) FROM WF_GenerWorkerList WHERE FK_Node=" + SystemConfig.getAppCenterDBVarStr()
					+ "FK_Node AND WorkID=" + SystemConfig.getAppCenterDBVarStr()
					+ "WorkID AND (IsPass=0 OR IsPass=-1) AND FK_Emp!=" + SystemConfig.getAppCenterDBVarStr()
					+ "FK_Emp";
			ps.Add("FK_Node", this.getFK_Node());
			ps.Add("WorkID", this.getWorkID());
			ps.Add("FK_Emp", WebUser.getNo());
			if (DBAccess.RunSQLReturnValInt(ps, 0) == 0) {
				return "close@鎮ㄦ病鏈夎缃細绛句汉锛岃鍦ㄦ枃鏈杈撳叆浼氱浜猴紝鎴栬�呴�夋嫨浼氱浜恒��";
			}
		}

		// 鍒ゆ柇褰撳墠鑺傜偣鐨勪細绛剧被鍨�.
		Node nd = new Node(gwf.getFK_Node());

		// 璁剧疆褰撳墠鎺ュ崟鏄細绛剧殑鐘舵��.
		gwf.setHuiQianTaskSta(HuiQianTaskSta.HuiQianing); // 璁剧疆涓轰細绛剧姸鎬�.
		if (nd.getHuiQianLeaderRole() == HuiQianLeaderRole.OnlyOne
				&& nd.getTodolistModel() == TodolistModel.TeamupGroupLeader) {

			gwf.setHuiQianZhuChiRen(WebUser.getNo());
			gwf.setHuiQianZhuChiRenName(WebUser.getName());

		} else {
			// 澶氫汉鐨勭粍闀挎ā寮忔垨鑰呭崗浣滄ā寮�
			if (DataType.IsNullOrEmpty(gwf.getHuiQianZhuChiRen()) == true) {
				gwf.setHuiQianZhuChiRen(gwf.getTodoEmps());
			}
		}

		// 姹備細绛句汉.
		GenerWorkerLists gwfs = new GenerWorkerLists();
		gwfs.Retrieve(GenerWorkerListAttr.WorkID, gwf.getWorkID(), GenerWorkerListAttr.FK_Node, gwf.getFK_Node(),
				GenerWorkerListAttr.IsPass, -1);

		String empsOfHuiQian = "浼氱浜�:";
		for (GenerWorkerList item : gwfs.ToJavaList()) {
			empsOfHuiQian += item.getFK_Emp() + "," + item.getFK_EmpText() + ";";

			// 鍙戦�佹秷鎭�
			bp.wf.Dev2Interface.Port_SendMsg(item.getFK_Emp(), "bpm浼氱閭�璇�",
					"HuiQian" + gwf.getWorkID() + "_" + gwf.getFK_Node() + "_" + item.getFK_Emp(),
					WebUser.getName() + "閭�璇锋偍瀵瑰伐浣滐經" + gwf.getTitle() + "锝濊繘琛屼細绛�,璇锋偍鍦▄" + item.getSDT() + "}鍓嶅畬鎴�.", "HuiQian",
					gwf.getFK_Flow(), gwf.getFK_Node(), gwf.getWorkID(), gwf.getFID());
		}

		// 鏀瑰彉浜嗚妭鐐瑰氨鎶婁細绛剧姸鎬佸幓鎺�.
		gwf.setHuiQianSendToNodeIDStr("");
		gwf.setTodoEmps(gwf.getTodoEmps() + empsOfHuiQian);

		gwf.Update();

		String sql = "";

		// 鏄惁鍚敤浼氱寰呭姙鍒楄〃, 濡傛灉鍚敤浜嗭紝涓绘寔浜轰細绛惧悗灏辫浆鍒颁簡HuiQianList.htm閲岄潰浜�.
		if (bp.wf.Glo.getIsEnableHuiQianList() == true) {
			// 璁剧疆褰撳墠鎿嶄綔浜哄憳鐨勭姸鎬�.
			sql = "UPDATE WF_GenerWorkerList SET IsPass=90 WHERE WorkID=" + this.getWorkID() + " AND FK_Node="
					+ this.getFK_Node() + " AND FK_Emp='" + WebUser.getNo() + "'";
			DBAccess.RunSQL(sql);
		}

		// 鎭㈠浠栫殑鐘舵��.
		sql = "UPDATE WF_GenerWorkerList SET IsPass=0 WHERE WorkID=" + this.getWorkID() + " AND FK_Node="
				+ this.getFK_Node() + " AND IsPass=-1";
		DBAccess.RunSQL(sql);

		// 鎵ц浼氱,鍐欏叆鏃ュ織.
		bp.wf.Dev2Interface.WriteTrack(gwf.getFK_Flow(), gwf.getFK_Node(), gwf.getNodeName(), gwf.getWorkID(),
				gwf.getFID(), empsOfHuiQian, ActionType.HuiQian, "鎵ц浼氱", null);

		String str = "";
		if (nd.getTodolistModel() == TodolistModel.TeamupGroupLeader) {
			/* 濡傛灉鏄粍闀挎ā寮�. */
			str = "close@淇濆瓨鎴愬姛.\t\n璇ュ伐浣滃凡缁忕Щ鍔ㄥ埌浼氱鍒楄〃涓簡,绛夊埌鎵�鏈夌殑浜轰細绛惧畬姣曞悗,灏卞彲浠ュ嚭鐜板湪寰呭姙鍒楄〃閲�.";
			str += "\t\n濡傛灉鎮ㄨ澧炲姞鎴栬�呯Щ闄や細绛句汉璇峰埌浼氱鍒楄〃鎵惧埌璇ヨ褰�,鎵ц鎿嶄綔.";

			// 鍒犻櫎鑷繁鐨勬剰瑙侊紝浠ラ槻姝㈠叾浠栦汉鍛樼湅鍒�.
			bp.wf.Dev2Interface.DeleteCheckInfo(gwf.getFK_Flow(), this.getWorkID(), gwf.getFK_Node());
			return str;
		}

		if (nd.getTodolistModel() == TodolistModel.Teamup) {
			int toNodeID = this.GetRequestValInt("ToNode");
			if (toNodeID == 0) {
				return "Send@[" + nd.getName() + "]浼氱鎴愬姛鎵ц.";
			}

			Node toND = new Node(toNodeID);
			// 濡傛灉鍒拌揪鐨勮妭鐐规槸鎸夌収鎺ュ彈浜烘潵閫夋嫨,灏辫浆鍚戞帴鍙椾汉閫夋嫨鍣�.
			if (toND.getHisDeliveryWay() == DeliveryWay.BySelected) {
				return "url@Accepter.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID="
						+ this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&ToNode=" + toNodeID;
			} else {
				return "Send@鎵ц鍙戦�佹搷浣�";
			}
		}

		return str;
	}

	///

	/// 瀹℃牳缁勪欢.
	/**
	 * 鏍￠獙瀵嗙爜
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String WorkCheck_CheckPass() throws Exception {
		String sPass = this.GetRequestVal("SPass");
		WFEmp emp = new WFEmp(WebUser.getNo());
		if (emp.getSPass().equals(sPass)) {
			return "绛惧悕鎴愬姛";
		}
		return "err@瀵嗙爜閿欒";
	}

	/**
	 * 淇敼瀵嗙爜
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String WorkCheck_ChangePass() throws Exception {
		String sPass = this.GetRequestVal("SPass");
		String sPass1 = this.GetRequestVal("SPass1");
		String sPass2 = this.GetRequestVal("SPass2");

		WFEmp emp = new WFEmp(WebUser.getNo());
		if (emp.getSPass().equals(sPass)) {
			return "鏃у瘑鐮侀敊璇�";
		}

		if (sPass1.equals(sPass2) == false) {
			return "err@涓ゆ杈撳叆鐨勫瘑鐮佷笉涓�鑷�";
		}
		emp.setSPass(sPass2);
		emp.Update();
		return "瀵嗙爜淇敼鎴愬姛";
	}

	/**
	 * 鍒濆鍖栧鏍哥粍浠舵暟鎹�.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String WorkCheck_Init() throws Exception {
		if (WebUser.getNo() == null) {
			return "err@鐧诲綍淇℃伅涓㈠け,璇烽噸鏂扮櫥褰�.";
		}

		// 琛ㄥ崟搴撳鏍哥粍浠舵祦绋嬬紪鍙蜂负null鐨勫紓甯稿鐞�
		if (DataType.IsNullOrEmpty(this.getFK_Flow())) {
			return null;
		}
        String trackTable = "ND" + Integer.parseInt(this.getFK_Flow()) + "Track";
		/// 瀹氫箟鍙橀噺.
		NodeWorkCheck wcDesc = new NodeWorkCheck(this.getFK_Node());
		NodeWorkCheck frmWorkCheck = null;
		FrmAttachmentDBs athDBs = null;
		Nodes nds = new Nodes(this.getFK_Flow());
		NodeWorkChecks fwcs = new NodeWorkChecks();
		Node nd = null;
		WorkCheck wc = null;
		Tracks tks = null;
		Track tkDoc = null;
		String nodes = ""; // 鍙互瀹℃牳鐨勮妭鐐�.
		boolean isCanDo = false;
		boolean isExitTb_doc = true;
		DataSet ds = new DataSet();
		DataRow row = null;

		// 鏄笉鏄彧璇�?
		boolean isReadonly = false;
		if (this.GetRequestVal("IsReadonly") != null && this.GetRequestVal("IsReadonly").equals("1")) {
			isReadonly = true;
		}
		DataTable nodeEmps = new DataTable();
		NodeWorkCheck fwc = null;
		DataTable dt = null;
		int idx = 0;
		int noneEmpIdx = 0;

		fwcs.Retrieve(NodeAttr.FK_Flow, this.getFK_Flow(), NodeAttr.Step);
		ds.Tables.add(wcDesc.ToDataTableField("WF_FrmWorkCheck")); // 褰撳墠鐨勮妭鐐瑰鏍哥粍浠跺畾涔夛紝鏀惧叆ds.

		DataTable tkDt = new DataTable("Tracks");
		tkDt.Columns.Add("NodeID", Integer.class);
		tkDt.Columns.Add("NodeName", String.class);
		tkDt.Columns.Add("Msg", String.class);
		tkDt.Columns.Add("EmpFrom", String.class);
		tkDt.Columns.Add("EmpFromT", String.class);
		tkDt.Columns.Add("DeptName", String.class);
		tkDt.Columns.Add("RDT", String.class);
		tkDt.Columns.Add("IsDoc", Boolean.class);
		tkDt.Columns.Add("ParentNode", Integer.class);
		// tkDt.Columns.Add("T_NodeIndex", typeof(int)); //鑺傜偣鎺掑垪椤哄簭锛岀敤浜庡悗闈㈢殑鎺掑簭
		// tkDt.Columns.Add("T_CheckIndex", typeof(int)); //瀹℃牳浜烘樉绀洪『搴忥紝鐢ㄤ簬鍚庨潰鐨勬帓搴�
		tkDt.Columns.Add("ActionType", Integer.class);
		tkDt.Columns.Add("Tag", String.class);
		tkDt.Columns.Add("FWCView", String.class);
        tkDt.Columns.Add("WritImg", String.class);

		// 娴佺▼闄勪欢.
		DataTable athDt = new DataTable("Aths");
		athDt.Columns.Add("NodeID", Integer.class);
		athDt.Columns.Add("MyPK", String.class);
		athDt.Columns.Add("FK_FrmAttachment", String.class);
		athDt.Columns.Add("FK_MapData", String.class);
		athDt.Columns.Add("FileName", String.class);
		athDt.Columns.Add("FileExts", String.class);
		athDt.Columns.Add("CanDelete", Boolean.class);
		// 褰撳墠鑺傜偣鐨勬祦绋嬫暟鎹�
		FrmAttachmentDBs frmathdbs = new FrmAttachmentDBs();
		frmathdbs.Retrieve(FrmAttachmentDBAttr.FK_FrmAttachment, "ND" + this.getFK_Node() + "_FrmWorkCheck",
				FrmAttachmentDBAttr.RefPKVal, String.valueOf(this.getWorkID()), FrmAttachmentDBAttr.Rec,
				WebUser.getNo(), FrmAttachmentDBAttr.RDT);

		for (FrmAttachmentDB athDB : frmathdbs.ToJavaList()) {
			row = athDt.NewRow();
			row.setValue("NodeID", this.getFK_Node());
			row.setValue("MyPK", athDB.getMyPK());
			row.setValue("FK_FrmAttachment", athDB.getFK_FrmAttachment());
			row.setValue("FK_MapData", athDB.getFK_MapData());
			row.setValue("FileName", athDB.getFileName());
			row.setValue("FileExts", athDB.getFileExts());
			row.setValue("CanDelete", athDB.getRec().equals(WebUser.getNo()) && isReadonly == false);
			athDt.Rows.add(row);
		}
		ds.Tables.add(athDt);

		if (this.getFID() != 0) {
			wc = new WorkCheck(this.getFK_Flow(), this.getFK_Node(), this.getFID(), 0);
		} else {
			wc = new WorkCheck(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), this.getFID());
		}

		// 鏄惁鍙锛�
		if (isReadonly == true) {
			isCanDo = false;
		} else {
			isCanDo = bp.wf.Dev2Interface.Flow_IsCanDoCurrentWork(this.getWorkID(), WebUser.getNo());
		}

		// 濡傛灉鏄煡鐪嬬姸鎬�, 涓轰簡灞忚斀鎺夋鍦ㄥ鎵圭殑鑺傜偣, 鍦ㄦ煡鐪嬪鎵规剰瑙佷腑.
		boolean isShowCurrNodeInfo = true;
		GenerWorkFlow gwf = new GenerWorkFlow();
		if (this.getWorkID() != 0) {
			gwf.setWorkID(this.getWorkID());
			gwf.Retrieve();
		}

		if (isCanDo == false && isReadonly == true) {
			if (gwf.getWFState() == WFState.Runing && gwf.getFK_Node() == this.getFK_Node()) {
				isShowCurrNodeInfo = false;
			}
		}

		/*
		 * 鑾峰緱褰撳墠鑺傜偣宸茬粡瀹℃牳閫氳繃鐨勪汉鍛�. 姣斿锛氬浜哄鐞嗚鍒欎腑鐨勫凡缁忓鏍稿悓鎰忕殑浜哄憳锛屼細绛句汉鍛�,缁勫悎鎴愭垚涓�涓瓧绗︿覆銆� 鏍煎紡涓�:
		 * ,zhangsan,lisi, 鐢ㄤ簬澶勭悊鍦ㄥ鏍稿垪琛ㄤ腑灞忚斀涓存椂鐨勪繚瀛樼殑瀹℃牳淇℃伅. 12 涓鸿姃鏋滃鍔犱竴涓潪姝ｅ父瀹屾垚鐘舵��.
		 */
		String checkerPassed = ",";
		if (gwf.getWFState() != WFState.Complete && gwf.getWFState().getValue() != 12) {
			Paras ps = new Paras();
			ps.SQL = "SELECT FK_Emp FROM WF_Generworkerlist WHERE WorkID=" + SystemConfig.getAppCenterDBVarStr()
					+ "WorkID AND IsPass=1 AND FK_Node=" + SystemConfig.getAppCenterDBVarStr()
					+ "FK_Node Order By RDT,CDT";
			ps.Add("WorkID", this.getWorkID());
			ps.Add("FK_Node", this.getFK_Node());
			DataTable checkerPassedDt = DBAccess.RunSQLReturnTable(ps);
			for (DataRow dr : checkerPassedDt.Rows) {
				checkerPassed += dr.getValue("FK_Emp") + ",";
			}
		}

		/// 瀹氫箟鍙橀噺.

		/// 鍒ゆ柇鏄惁鏄剧ず - 鍘嗗彶瀹℃牳淇℃伅鏄剧ず
		boolean isDoc = false;
		if (wcDesc.getFWCListEnable() == true) {
			tks = wc.getHisWorkChecks();

			// 宸茶蛋杩囪妭鐐�
			int empIdx = 0;
			int lastNodeId = 0;
			for (bp.wf.Track tk : tks.ToJavaList()) {
				if (tk.getHisActionType() == ActionType.FlowBBS) {
					continue;
				}

				if(wcDesc.getFWCMsgShow()==1 && tk.getEmpFrom().equals(WebUser.getNo())==false)
					continue;

				if (lastNodeId == 0) {
					lastNodeId = tk.getNDFrom();
				}

				if (lastNodeId != tk.getNDFrom()) {
					idx++;
					lastNodeId = tk.getNDFrom();
				}

				// tk.Row.Add("T_NodeIndex", idx);

				Object tempVar = nds.GetEntityByKey(tk.getNDFrom());
				nd = tempVar instanceof Node ? (Node) tempVar : null;
				if (nd == null) {
					continue;
				}

				Object tempVar2 = fwcs.GetEntityByKey(tk.getNDFrom());
				fwc = tempVar2 instanceof NodeWorkCheck ? (NodeWorkCheck) tempVar2 : null;
				// 姹傚嚭涓婚敭
				long pkVal = this.getWorkID();
				if (nd.getHisRunModel() == RunModel.SubThread) {
					pkVal = this.getFID();
				}

				// 鎺掑簭锛岀粨鍚堜汉鍛樿〃Idx杩涜鎺掑簭
				/*
				 * if (fwc.getFWCOrderModel() == FWCOrderModel.SqlAccepter) {
				 * tk.Row["T_CheckIndex"] =
				 * DBAccess.RunSQLReturnValInt(String.format(
				 * "SELECT Idx FROM Port_Emp WHERE No='%1$s'", tk.getEmpFrom()),
				 * 0); noneEmpIdx++; } else { tk.Row["T_CheckIndex"] =
				 * noneEmpIdx++; }
				 */
				switch (tk.getHisActionType()) {
				case WorkCheck:
				case StartChildenFlow:
					if (nodes.contains(tk.getNDFrom() + ",") == false) {
						nodes += tk.getNDFrom() + ",";
					}
					break;
				case Return:
					if (wcDesc.getFWCIsShowReturnMsg() == true && tk.getNDTo() == this.getFK_Node()) {
						if (nodes.contains(tk.getNDFrom() + ",") == false) {
							nodes += tk.getNDFrom() + ",";
						}
					}
					break;
				default:
					continue;
				}
			}

			for (Track tk : tks.ToJavaList()) {
				if (nodes.contains(tk.getNDFrom() + ",") == false) {
					continue;
				}

				if (tk.getHisActionType() != ActionType.WorkCheck
						&& tk.getHisActionType() != ActionType.StartChildenFlow
						&& tk.getHisActionType() != ActionType.Return) {
					continue;
				}

				// 閫�鍥�
				if (tk.getHisActionType() == ActionType.Return) {
					// 1.涓嶆樉绀洪��鍥炴剰瑙� 2.鏄剧ず閫�鍥炴剰瑙佷絾鏄笉鏄��鍥炵粰鏈妭鐐圭殑鎰忚
					if (wcDesc.getFWCIsShowReturnMsg() == false
							|| (wcDesc.getFWCIsShowReturnMsg() == true && tk.getNDTo() != this.getFK_Node())) {
						continue;
					}
				}

				// 濡傛灉鏄綋鍓嶇殑鑺傜偣. 褰撳墠浜哄憳鍙互澶勭悊, 宸茬粡瀹℃壒閫氳繃鐨勪汉鍛�.
				if (tk.getNDFrom() == this.getFK_Node() && isCanDo == true
						&& tk.getEmpFrom().equals(WebUser.getNo()) == false
						&& checkerPassed.contains("," + tk.getEmpFrom() + ",") == false) {
					continue;
				}

				if (tk.getNDFrom() == this.getFK_Node() && gwf.getHuiQianTaskSta() != HuiQianTaskSta.None) {
					// 鍒ゆ柇浼氱, 鍘绘帀姝ｅ湪瀹℃壒鐨勮妭鐐�.
					if (tk.getNDFrom() == this.getFK_Node() && isShowCurrNodeInfo == false) {
						continue;
					}
				}

				//濡傛灉鏄��鍥炵姸鎬侊紝灏辨樉绀轰箣鍓嶅鏍哥殑淇℃伅

				// 濡傛灉鏄浜哄鐞嗭紝灏辫鍏舵樉绀哄凡缁忓鏍歌繃鐨勬剰瑙�.
				if (tk.getNDFrom() == this.getFK_Node() && checkerPassed.indexOf("," + tk.getEmpFrom() + ",") < 0
						&& (gwf.getWFState() != WFState.Complete &&gwf.getWFState() != WFState.ReturnSta  && gwf.getWFState().getValue() != 12)) {
					continue;

				}

				row = tkDt.NewRow();
				row.setValue("NodeID", tk.getNDFrom());

				row.setValue("NodeName", tk.getNDFromT());
				Object tempVar3 = fwcs.GetEntityByKey(tk.getNDFrom());
				fwc = tempVar3 instanceof NodeWorkCheck ? (NodeWorkCheck) tempVar3 : null;

				// zhoupeng 澧炲姞浜嗗垽鏂紝鍦ㄤ細绛剧殑鏃跺�欐渶鍚庝細绛句汉鍙戦�佸墠涓嶈兘濉啓鎰忚.
				if (tk.getNDFrom() == this.getFK_Node() && tk.getEmpFrom().equals(WebUser.getNo())  && isCanDo
						&& isDoc == false) {
					isDoc = true;
					row.setValue("IsDoc", true);
				} else {
					row.setValue("IsDoc", false);
				}

				row.setValue("ParentNode", 0);
				row.setValue("RDT", DataType.IsNullOrEmpty(tk.getRDT()) ? ""
						: tk.getNDFrom() == tk.getNDTo() && DataType.IsNullOrEmpty(tk.getMsg()) ? "" : tk.getRDT());
				// row["T_NodeIndex"] = tk.Row["T_NodeIndex"];
				// row["T_CheckIndex"] = tk.Row["T_CheckIndex"];

				if (isReadonly == false && tk.getEmpFrom() == WebUser.getNo() && this.getFK_Node() == tk.getNDFrom()
						&& isExitTb_doc
						&& (wcDesc.getHisFrmWorkCheckType() == FWCType.Check
								|| ((wcDesc.getHisFrmWorkCheckType() == FWCType.DailyLog
										|| wcDesc.getHisFrmWorkCheckType() == FWCType.WeekLog)
										&& DateUtils.format(DateUtils.parse(tk.getRDT()), "yyyy-MM-dd")
												.equals(DataType.getCurrentDateByFormart("yyyy-MM-dd")))
								|| (wcDesc.getHisFrmWorkCheckType() == FWCType.MonthLog
										&& DateUtils.format(DateUtils.parse(tk.getRDT()), "yyyy-MM-dd")
												.equals(DataType.getCurrentDateByFormart("yyyy-MM"))))) {
					boolean isLast = true;
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
				// 鑾峰彇閮ㄩ棬
				String DeptName = "";
				String[] Arrays = tk.getNodeData().split("[@]", -1);
				for (String i : Arrays) {
					if (i.contains("DeptName=")) {
						DeptName = i.split("[=]", -1)[1];
					}
				}
				row.setValue("DeptName", DeptName);
				row.setValue("ActionType", tk.getHisActionType().getValue());
				row.setValue("Tag", tk.getTag());
				row.setValue("FWCView", fwc.getFWCView());
                if (wcDesc.getSigantureEnabel() != 2)
                    row.setValue("WritImg","");
                else
                    row.setValue("WritImg",DBAccess.GetBigTextFromDB(trackTable, "MyPK", tk.getMyPK(), "WriteDB"));

                tkDt.Rows.add(row);

				///// 瀹℃牳缁勪欢闄勪欢鏁版嵁
				athDBs = new FrmAttachmentDBs();
				QueryObject obj_Ath = new QueryObject(athDBs);
				obj_Ath.AddWhere(FrmAttachmentDBAttr.FK_FrmAttachment, "ND" + tk.getNDFrom() + "_FrmWorkCheck");
				obj_Ath.addAnd();
				obj_Ath.AddWhere(FrmAttachmentDBAttr.RefPKVal, String.valueOf(this.getWorkID()));
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
					row.setValue("CanDelete", String.valueOf(this.getFK_Node()).equals(athDB.getFK_MapData())
							&& athDB.getRec().equals(WebUser.getNo()) && isReadonly == false);
					athDt.Rows.add(row);
				}

				///

				///// 瀛愭祦绋嬬殑瀹℃牳缁勪欢鏁版嵁
				if (tk.getFID() != 0 && tk.getHisActionType() == ActionType.StartChildenFlow
						&& tkDt.Select("ParentNode=" + tk.getNDFrom()).length == 0) {
					String[] paras = tk.getTag().split("[@]", -1);
					String[] p1 = paras[1].split("[=]", -1);
					String fk_flow = p1[1]; // 瀛愭祦绋嬬紪鍙�

					String[] p2 = paras[2].split("[=]", -1);
					String workId = p2[1]; // 瀛愭祦绋婭D.
					int biaoji = 0;
                    String subtrackTable = "ND" + Integer.parseInt(fk_flow) + "Track";
					WorkCheck subwc = new WorkCheck(fk_flow, Integer.parseInt(fk_flow + "01"), Long.parseLong(workId),
							0);

					Tracks subtks = subwc.getHisWorkChecks();
					// 鍙栧嚭鏉ュ瓙娴佺▼鐨勬墍鏈夌殑鑺傜偣銆�
					Nodes subNds = new Nodes(fk_flow);
					for (Node item : subNds.ToJavaList()) // 涓昏鎸夐『搴忔樉绀�
					{
						for (Track mysubtk : subtks.ToJavaList()) {
							if (item.getNodeID() != mysubtk.getNDFrom()) {
								continue;
							}

							/* 杈撳嚭璇ュ瓙娴佺▼鐨勫鏍镐俊鎭紝搴旇鑰冭檻瀛愭祦绋嬬殑瀛愭祦绋嬩俊鎭�, 灏变笉鑰冭檻閭ｆ牱澶嶆潅浜�. */
							if (mysubtk.getHisActionType() == ActionType.WorkCheck) {
								// 鍙戣捣澶氫釜瀛愭祦绋嬫椂锛屽彂璧蜂汉鍙樉绀轰竴娆�
								if (mysubtk.getNDFrom() == Integer.parseInt(fk_flow + "01") && biaoji == 1) {
									continue;
								}
								NodeWorkCheck subFrmCheck = new NodeWorkCheck("ND" + mysubtk.getNDFrom());
								row = tkDt.NewRow();
								row.setValue("NodeID", mysubtk.getNDFrom());
								row.setValue("NodeName", String.format("(瀛愭祦绋�)%1$s", mysubtk.getNDFromT()));
								row.setValue("Msg", mysubtk.getMsgHtml());
								row.setValue("EmpFrom", mysubtk.getEmpFrom());
								row.setValue("EmpFromT", mysubtk.getEmpFromT());
								// 鑾峰彇閮ㄩ棬
								DeptName = "";
								Arrays = tk.getNodeData().split("[@]", -1);
								for (String i : Arrays) {
									if (i.contains("DeptName=")) {
										DeptName = i.split("[=]", -1)[1];
									}
								}
								row.setValue("DeptName", DeptName);
								row.setValue("RDT", mysubtk.getRDT());
								row.setValue("IsDoc", false);
								row.setValue("ParentNode", tk.getNDFrom());
								// row["T_NodeIndex"] = idx++;
								row.setValue("T_CheckIndex", noneEmpIdx++);
								row.setValue("ActionType", mysubtk.getHisActionType().getValue());
								row.setValue("Tag", mysubtk.getTag());
								row.setValue("FWCView", subFrmCheck.getFWCView());
                                if (wcDesc.getSigantureEnabel() != 2)
                                    row.setValue("WritImg","");
                                else
                                    row.setValue("WritImg",DBAccess.GetBigTextFromDB(subtrackTable, "MyPK", mysubtk.getMyPK(), "WriteDB"));
                               tkDt.Rows.add(row);

								if (mysubtk.getNDFrom() == Integer.parseInt(fk_flow + "01")) {
									biaoji = 1;
								}
							}
						}
					}
				}

			}

		}

		/// 鍒ゆ柇鏄惁鏄剧ず - 鍘嗗彶瀹℃牳淇℃伅鏄剧ず

		/// 瀹℃牳鎰忚榛樿濉啓

		// 棣栧厛鍒ゆ柇褰撳墠鏄惁鏈夋鎰忚? 濡傛灉鏄��鍥炵殑璇ヤ俊鎭凡缁忓瓨鍦ㄤ簡.
		boolean isHaveMyInfo = false;
		for (DataRow dr : tkDt.Rows) {
			String fk_node = dr.getValue("NodeID").toString();
			String empFrom = dr.getValue("EmpFrom").toString();
			if (Integer.parseInt(fk_node) == this.getFK_Node() && empFrom.equals(WebUser.getNo())) {
				isHaveMyInfo = true;
			}
		}

		// 澧炲姞榛樿鐨勫鏍告剰瑙�.
		if (isExitTb_doc && wcDesc.getHisFrmWorkCheckSta() == FrmWorkCheckSta.Enable && isCanDo && isReadonly == false
				&& isHaveMyInfo == false) {
			DataRow[] rows = null;
			Object tempVar4 = nds.GetEntityByKey(this.getFK_Node());
			nd = tempVar4 instanceof Node ? (Node) tempVar4 : null;
			if (wcDesc.getFWCOrderModel() == FWCOrderModel.SqlAccepter) {
				rows = tkDt.Select("NodeID=" + this.getFK_Node() + " AND Msg='' AND EmpFrom='" + WebUser.getNo() + "'");

				if (rows.length == 0) {
					rows = tkDt.Select("NodeID=" + this.getFK_Node() + " AND EmpFrom='" + WebUser.getNo() + "'");
				}

				if (rows.length > 0) {
					row = rows[0];
					row.setValue("IsDoc", true);

					row.setValue("Msg", Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(), this.getFK_Node(),
							wcDesc.getFWCDefInfo()));
					if (row.getValue("Msg").toString().equals("")) {
						row.setValue("RDT", "");
					}

				} else {
					row = tkDt.NewRow();
					row.setValue("NodeID", this.getFK_Node());
					row.setValue("NodeName", nd.getFWCNodeName());
					row.setValue("IsDoc", true);
					row.setValue("ParentNode", 0);
					row.setValue("RDT", "");

					row.setValue("Msg", Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(), this.getFK_Node(),
							wcDesc.getFWCDefInfo()));
					row.setValue("EmpFrom", WebUser.getNo());
					row.setValue("EmpFromT", WebUser.getName());
					row.setValue("DeptName", WebUser.getFK_DeptName());
					// row["T_NodeIndex"] = ++idx;
					row.setValue("T_CheckIndex", ++noneEmpIdx);
					row.setValue("ActionType", ActionType.Forward.getValue());
					row.setValue("Tag", Dev2Interface.GetCheckTag(this.getFK_Flow(), this.getWorkID(), this.getFK_Node(),
							WebUser.getNo()));
					 if (wcDesc.getSigantureEnabel() != 2)
		                    row.setValue("WritImg","");
		                else
		                {
		                    String sql = "Select MyPK From " + trackTable + "  WHERE ActionType=" + ActionType.WorkCheck.getValue() + " AND  NDFrom=" + this.getFK_Node() + " AND  NDTo=" + this.getFK_Node() + " AND WorkID=" + this.getWorkID() + " AND EmpFrom = '" + WebUser.getNo() + "'";

		                    String writeDb =DBAccess.GetBigTextFromDB(trackTable, "MyPK", DBAccess.RunSQLReturnVal(sql) == null ? null : DBAccess.RunSQLReturnVal(sql).toString(), "WriteDB");
		                	if(DataType.IsNullOrEmpty(writeDb)){
		                		/*濡傛灉娌℃湁姝ゅ垪锛屽氨鑷姩鍒涘缓姝ゅ垪.*/
		        				if (DBAccess.IsExitsTableCol("ND" + Integer.parseInt(this.getFK_Flow()) + "Track", "WriteDB") == false)
		        				{
		        					String sqlWriteDB = "ALTER TABLE ND" + Integer.parseInt(this.getFK_Flow()) + "Track ADD  WriteDB BLOB ";
		        					DBAccess.RunSQL(sqlWriteDB);
		        				}
		                		String sql2="SELECT	WriteDB,Msg FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track WHERE WorkID = " + this.getWorkID() + " AND ActionType = " + ActionType.Forward.getValue() +" AND NDFrom = " + this.getFK_Node() + " AND EmpFrom = '" + WebUser.getNo() + "' ORDER BY	rdt DESC";
		        				DataTable dtw = DBAccess.RunSQLReturnTable(sql2);

		        				if(dtw.Rows.size() > 0){
		        					writeDb = dtw.Rows.get(0).getValue("WriteDb")==null?"":dtw.Rows.get(0).getValue("WriteDb").toString();
		        					row.setValue("Msg",dtw.Rows.get(0).getValue("Msg")==null?"":dtw.Rows.get(0).getValue("Msg").toString().substring(dtw.Rows.get(0).getValue("Msg").toString().indexOf("@")+1));
		        				}
		        			}
		                	row.setValue("WritImg",writeDb);
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

				row.setValue("Msg", Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(), this.getFK_Node(),
						wcDesc.getFWCDefInfo()));
				row.setValue("EmpFrom", WebUser.getNo());
				row.setValue("EmpFromT", WebUser.getName());
				row.setValue("DeptName", WebUser.getFK_DeptName());
				// row["T_NodeIndex"] = ++idx; zsy灞忚斀2020.6.17
				// row["T_CheckIndex"] = ++noneEmpIdx; zsy灞忚斀2020.6.17
				row.setValue("ActionType", ActionType.Forward.getValue());
				row.setValue("Tag", Dev2Interface.GetCheckTag(this.getFK_Flow(), this.getWorkID(), this.getFK_Node(),
						WebUser.getNo()));
                if (wcDesc.getSigantureEnabel() != 2)
                    row.setValue("WritImg","");
                else
                {
                    String sql = "Select MyPK From " + trackTable + "  WHERE ActionType=" + ActionType.WorkCheck.getValue() + " AND  NDFrom=" + this.getFK_Node() + " AND  NDTo=" + this.getFK_Node() + " AND WorkID=" + this.getWorkID() + " AND EmpFrom = '" + WebUser.getNo() + "'";
                    String writeDb =DBAccess.GetBigTextFromDB(trackTable, "MyPK", DBAccess.RunSQLReturnVal(sql) == null ? null : DBAccess.RunSQLReturnVal(sql).toString(), "WriteDB");
                	if(DataType.IsNullOrEmpty(writeDb)){
                		/*濡傛灉娌℃湁姝ゅ垪锛屽氨鑷姩鍒涘缓姝ゅ垪.*/
        				if (DBAccess.IsExitsTableCol("ND" + Integer.parseInt(this.getFK_Flow()) + "Track", "WriteDB") == false)
        				{
        					String sqlWriteDB = "ALTER TABLE ND" + Integer.parseInt(this.getFK_Flow()) + "Track ADD  WriteDB BLOB ";
        					DBAccess.RunSQL(sqlWriteDB);
        				}
                		String sql2="SELECT	WriteDB,Msg FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track WHERE WorkID = " + this.getWorkID() + " AND ActionType = " + ActionType.Forward.getValue() +" AND NDFrom = " + this.getFK_Node() + " AND EmpFrom = '" + WebUser.getNo() + "' ORDER BY	rdt DESC";
        				DataTable dtw = DBAccess.RunSQLReturnTable(sql2);

        				if(dtw.Rows.size() > 0){
        					writeDb = dtw.Rows.get(0).getValue("WriteDb")==null?"":dtw.Rows.get(0).getValue("WriteDb").toString();
        					row.setValue("Msg",dtw.Rows.get(0).getValue("Msg")==null?"":dtw.Rows.get(0).getValue("Msg").toString().substring(dtw.Rows.get(0).getValue("Msg").toString().indexOf("@")+1));
        				}
        			}
                	row.setValue("WritImg",writeDb);
                }
				tkDt.Rows.add(row);
			}
		}

		///

		/// 鏄剧ず鏈夊鏍哥粍浠讹紝浣嗚繕鏈鏍哥殑鑺傜偣. 鍖呮嫭閫�鍥炲悗鐨�.
		if (tks == null) {
			tks = wc.getHisWorkChecks();
		}

		for (NodeWorkCheck item : fwcs.ToJavaList()) {
			if (item.getFWCIsShowTruck() == false) {
				continue; // 涓嶉渶瑕佹樉绀哄巻鍙茶褰�.
			}

			// 鏄惁宸插鏍�.
			boolean isHave = false;
			for (bp.wf.Track tk : tks.ToJavaList()) {
				// 缈昏瘧.
				if (tk.getNDFrom() == this.getFK_Node() && tk.getHisActionType() == ActionType.WorkCheck) {
					isHave = true; // 宸茬粡鏈変簡
					break;
				}
			}

			if (isHave == true) {
				continue;
			}

			row = tkDt.NewRow();
			row.setValue("NodeID", item.getNodeID());

			Node mynd = (Node) nds.GetEntityByKey(item.getNodeID());
			row.setValue("NodeName", mynd.getFWCNodeName());
			row.setValue("IsDoc", false);
			row.setValue("ParentNode", 0);
			row.setValue("RDT", "");
			row.setValue("Msg", "&nbsp;");
			row.setValue("EmpFrom", "");
			row.setValue("EmpFromT", "");
			row.setValue("DeptName", "");
			// row["T_NodeIndex"] = ++idx;
			row.setValue("T_CheckIndex", ++noneEmpIdx);

			tkDt.Rows.add(row);
		}

		/// 澧炲姞绌虹櫧.

		ds.Tables.add(tkDt);

		// 濡傛灉鏈� SignType 鍒楀氨鑾峰緱绛惧悕淇℃伅.
		if (SystemConfig.getCustomerNo().equals("TianYe")) {
			String tTable = "ND" + Integer.parseInt(getFK_Flow()) + "Track";
			String sql = "SELECT distinct a.No, a.SignType, a.EleID FROM Port_Emp a, " + tTable + " b WHERE (A.No='"
					+ WebUser.getNo() + "') OR B.ActionType=22 AND a.No=b.EmpFrom AND B.WorkID=" + this.getWorkID();

			DataTable dtTrack = DBAccess.RunSQLReturnTable(sql);
			dtTrack.TableName = "SignType";

			dtTrack.Columns.get("NO").setColumnName("No");
			dtTrack.Columns.get("SIGNTYPE").setColumnName("SignType");
			dtTrack.Columns.get("ELEID").setColumnName("EleID");

			ds.Tables.add(dtTrack);
		}

		String str = bp.tools.Json.ToJson(ds);

		return str;
	}

	/** 
	 鍒濆鍖栧鏍哥粍浠舵暟鎹�.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String WorkCheck_Init2019() throws Exception
	{
		if (WebUser.getNo() == null)
		{
			return "err@鐧诲綍淇℃伅涓㈠け,璇烽噸鏂扮櫥褰�.";
		}
		// 琛ㄥ崟搴撳鏍哥粍浠舵祦绋嬬紪鍙蜂负null鐨勫紓甯稿鐞�
		if (DataType.IsNullOrEmpty(this.getFK_Flow())) {
			return null;
		}
        String trackTable = "ND" + Integer.parseInt(this.getFK_Flow()) + "Track";
	    //瀹氫箟鍙橀噺.
		NodeWorkCheck wcDesc = new NodeWorkCheck(this.getFK_Node()); // 褰撳墠鑺傜偣鐨勫鏍哥粍浠�
		NodeWorkCheck frmWorkCheck = null;
		FrmAttachmentDBs athDBs = null; //闄勪欢鏁版嵁
		Nodes nds = new Nodes(this.getFK_Flow()); //璇ユ祦绋嬬殑鎵�鏈夎妭鐐�
		NodeWorkChecks fwcs = new NodeWorkChecks();
		Node nd = null;
		WorkCheck wc = null;
		Tracks tks = null;
		Track tkDoc = null;
		String nodes = ""; //宸茬粡瀹℃牳杩囩殑鑺傜偣.
		boolean isCanDo = false; //鏄惁鍙互瀹℃壒
		boolean isExitTb_doc = true;
		DataSet ds = new DataSet();
		DataRow row = null;

		//鏄笉鏄彧璇�?
		boolean isReadonly = false;
		if (this.GetRequestVal("IsReadonly") != null && this.GetRequestVal("IsReadonly").equals("1"))
		{
			isReadonly = true;
		}

		DataTable nodeEmps = new DataTable();
		NodeWorkCheck fwc = null;
		DataTable dt = null;
		int idx = 0;
		int noneEmpIdx = 0;

		fwcs.Retrieve(NodeAttr.FK_Flow, this.getFK_Flow(), NodeAttr.Step);
		ds.Tables.add(wcDesc.ToDataTableField("WF_FrmWorkCheck")); //褰撳墠鐨勮妭鐐瑰鏍哥粍浠跺畾涔夛紝鏀惧叆ds.

		DataTable tkDt = new DataTable("Tracks");
		tkDt.Columns.Add("NodeID", Integer.class);
		tkDt.Columns.Add("NodeName", String.class);
		tkDt.Columns.Add("Msg", String.class);
		tkDt.Columns.Add("EmpFrom", String.class);
		tkDt.Columns.Add("EmpFromT", String.class);
		tkDt.Columns.Add("DeptName", String.class);
		tkDt.Columns.Add("RDT", String.class);
		tkDt.Columns.Add("IsDoc", Boolean.class);
		tkDt.Columns.Add("ParentNode", Integer.class);
		//tkDt.Columns.Add("T_NodeIndex", typeof(int));    //鑺傜偣鎺掑垪椤哄簭锛岀敤浜庡悗闈㈢殑鎺掑簭
		//tkDt.Columns.Add("T_CheckIndex", typeof(int));    //瀹℃牳浜烘樉绀洪『搴忥紝鐢ㄤ簬鍚庨潰鐨勬帓搴�
		tkDt.Columns.Add("ActionType", Integer.class);
		tkDt.Columns.Add("Tag", String.class);
		tkDt.Columns.Add("FWCView", String.class);
        tkDt.Columns.Add("WritImg", String.class);

		//娴佺▼闄勪欢.
		DataTable athDt = new DataTable("Aths");
		athDt.Columns.Add("NodeID", Integer.class);
		athDt.Columns.Add("MyPK", String.class);
		athDt.Columns.Add("FK_FrmAttachment", String.class);
		athDt.Columns.Add("FK_MapData", String.class);
		athDt.Columns.Add("FileName", String.class);
		athDt.Columns.Add("FileExts", String.class);
		athDt.Columns.Add("CanDelete", Boolean.class);
		//褰撳墠鑺傜偣鐨勬祦绋嬫暟鎹�
		FrmAttachmentDBs frmathdbs = new FrmAttachmentDBs();
		frmathdbs.Retrieve(FrmAttachmentDBAttr.FK_FrmAttachment, "ND" + this.getFK_Node() + "_FrmWorkCheck", FrmAttachmentDBAttr.RefPKVal, String.valueOf(this.getWorkID()), "Rec", WebUser.getNo(), FrmAttachmentDBAttr.RDT);

		for (FrmAttachmentDB athDB : frmathdbs.ToJavaList())
		{
			row = athDt.NewRow();
			row.setValue("NodeID", this.getFK_Node());
			row.setValue("MyPK", athDB.getMyPK());
			row.setValue("FK_FrmAttachment", athDB.getFK_FrmAttachment());
			row.setValue("FK_MapData", athDB.getFK_MapData());
			row.setValue("FileName", athDB.getFileName());
			row.setValue("FileExts", athDB.getFileExts());
			row.setValue("CanDelete", athDB.getRec().equals(WebUser.getNo()) && isReadonly == false);
			athDt.Rows.add(row);
		}
		ds.Tables.add(athDt);

		if (this.getFID() != 0)
		{
			wc = new WorkCheck(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), this.getFID());
		}
		else
		{
			wc = new WorkCheck(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), 0);
		}

		//鏄惁鍙锛�
		if (isReadonly == true)
		{
			isCanDo = false;
		}
		else
		{
			isCanDo = bp.wf.Dev2Interface.Flow_IsCanDoCurrentWork(this.getWorkID(), WebUser.getNo());
		}

		//濡傛灉鏄煡鐪嬬姸鎬�, 涓轰簡灞忚斀鎺夋鍦ㄥ鎵圭殑鑺傜偣, 鍦ㄦ煡鐪嬪鎵规剰瑙佷腑.
		boolean isShowCurrNodeInfo = true;
		GenerWorkFlow gwf = new GenerWorkFlow();
		if (this.getWorkID() != 0)
		{
			gwf.setWorkID(this.getWorkID());
			gwf.Retrieve();
		}

		if (isCanDo == false && isReadonly == true)
		{
			if (gwf.getWFState() == WFState.Runing && gwf.getFK_Node() == this.getFK_Node())
			{
				isShowCurrNodeInfo = false;
			}
		}

		/*
		 * 鑾峰緱褰撳墠鑺傜偣宸茬粡瀹℃牳閫氳繃鐨勪汉鍛�.
		 * 姣斿锛氬浜哄鐞嗚鍒欎腑鐨勫凡缁忓鏍稿悓鎰忕殑浜哄憳锛屼細绛句汉鍛�,缁勫悎鎴愭垚涓�涓瓧绗︿覆銆�
		 * 鏍煎紡涓�: ,zhangsan,lisi,
		 * 鐢ㄤ簬澶勭悊鍦ㄥ鏍稿垪琛ㄤ腑灞忚斀涓存椂鐨勪繚瀛樼殑瀹℃牳淇℃伅.
		 * 12 涓鸿姃鏋滃鍔犱竴涓潪姝ｅ父瀹屾垚鐘舵��.
		 * */
		String checkerPassed = ",";
		if (gwf.getWFState() != WFState.Complete && gwf.getWFState().getValue() != 12)
		{
			/*Paras ps = new Paras();
			ps.SQL="SELECT FK_Emp FROM WF_Generworkerlist WHERE WorkID=" + SystemConfig.getAppCenterDBVarStr() + "WorkID AND IsPass=1 AND FK_Node=" + SystemConfig.getAppCenterDBVarStr() + "FK_Node  Order By RDT,CDT";
			ps.Add("WorkID", this.getWorkID());
			ps.Add("FK_Node", this.getFK_Node());*/
			String sql = "SELECT EmpFrom as FK_Emp  FROM ND"+Integer.parseInt(this.getFK_Flow()) +"Track WHERE WorkID =" + this.getWorkID()+"  AND NDFrom = "+this.getFK_Node();

			DataTable checkerPassedDt = DBAccess.RunSQLReturnTable(sql);
			for (DataRow dr : checkerPassedDt.Rows)
			{
				checkerPassed += dr.getValue("FK_Emp") + ",";
			}
		}


			/// 瀹氫箟鍙橀噺.


			///鍒ゆ柇鏄惁鏄剧ず - 鍘嗗彶瀹℃牳淇℃伅鏄剧ず
		boolean isDoc = false;
		if (wcDesc.getFWCListEnable() == true)
		{
			tks = wc.getHisWorkChecks();

			for(Track tk : tks.ToJavaList())
			{
				if (tk.getHisActionType() == ActionType.FlowBBS)
					continue;
				if(wcDesc.getFWCMsgShow()==1 && tk.getEmpFrom().equals(WebUser.getNo())==false)
					continue;

				nd = (Node)nds.GetEntityByKey(tk.getNDFrom());
				if (nd == null)
					continue;

				//fwc = (NodeWorkCheck)fwcs.GetEntityByKey(tk.getNDFrom());
				//姹傚嚭涓婚敭
				long pkVal = this.getWorkID();
				if (nd.getHisRunModel() == RunModel.SubThread)
					pkVal = this.getFID();


				switch (tk.getHisActionType())
				{
					case WorkCheck:
					case Forward:
					case StartChildenFlow:
						//case ActionType.ForwardHL:
						if (nodes.contains(tk.getNDFrom() + ",") == false)
							nodes += tk.getNDFrom() + ",";
						break;
					case Return:
						if (wcDesc.getFWCIsShowReturnMsg() == true && tk.getNDTo() == this.getFK_Node())
						{
							if (nodes.contains(tk.getNDFrom() + ",") == false)
								nodes += tk.getNDFrom() + ",";
						}
						break;
					default:
						continue;
				}
			}
			for (Track tk : tks.ToJavaList())
			{
				if (tk.getHisActionType() == ActionType.ForwardHL)
				{
					String sss = "";
				}

				if (nodes.contains(tk.getNDFrom() + ",") == false)
				    continue;



				//閫�鍥�
				if (tk.getHisActionType() == ActionType.Return)
				{
					//1.涓嶆樉绀洪��鍥炴剰瑙� 2.鏄剧ず閫�鍥炴剰瑙佷絾鏄笉鏄��鍥炵粰鏈妭鐐圭殑鎰忚
					if (wcDesc.getFWCIsShowReturnMsg() == false || (wcDesc.getFWCIsShowReturnMsg() == true && tk.getNDTo() != this.getFK_Node()))
					{
						continue;
					}
				}

				//濡傛灉鏄綋鍓嶇殑鑺傜偣. 褰撳墠浜哄憳鍙互澶勭悊, 宸茬粡瀹℃壒閫氳繃鐨勪汉鍛�.
				if (tk.getNDFrom() == this.getFK_Node() && isCanDo == true && tk.getEmpFrom().equals(WebUser.getNo())==false && checkerPassed.contains("," + tk.getEmpFrom() + ",") == false)
				{
					continue;
				}

				if (tk.getNDFrom() == this.getFK_Node() && gwf.getHuiQianTaskSta() != HuiQianTaskSta.None)
				{
					//鍒ゆ柇浼氱, 鍘绘帀姝ｅ湪瀹℃壒鐨勮妭鐐�.
					if (tk.getNDFrom() == this.getFK_Node() && isShowCurrNodeInfo == false)
					{
						continue;
					}
				}
				//濡傛灉鏄浜哄鐞嗭紝灏辫鍏舵樉绀哄凡缁忓鏍歌繃鐨勬剰瑙�.
				//if (tk.getNDFrom() == this.FK_Node&& checkerPassed.IndexOf("," + tk.getEmpFrom() + ",") < 0 && (gwf.WFState != WFState.Complete && (int)gwf.WFState != 12))
				//    continue;

				Object tempVar = fwcs.GetEntityByKey(tk.getNDFrom());
				fwc = tempVar instanceof NodeWorkCheck ? (NodeWorkCheck)tempVar : null;
				if (fwc.getHisFrmWorkCheckSta() != FrmWorkCheckSta.Enable)
                        continue;

				//鍘嗗彶瀹℃牳淇℃伅鐜板湪瀛樻斁鍦ㄦ祦绋嬪墠杩涚殑鑺傜偣涓�
				switch (tk.getHisActionType())
				{
					case Forward:
					case ForwardAskfor:
					case Start:
					//case UnSend:
				   // case ActionType.ForwardFL:
					case ForwardHL:
					case SubThreadForward:
					case TeampUp:
					case Return:
					case StartChildenFlow:
					case FlowOver:
						row = tkDt.NewRow();
						row.setValue("NodeID", tk.getNDFrom());
						row.setValue("NodeName", tk.getNDFromT());

						// zhoupeng 澧炲姞浜嗗垽鏂紝鍦ㄤ細绛剧殑鏃跺�欐渶鍚庝細绛句汉鍙戦�佸墠涓嶈兘濉啓鎰忚.
						if (tk.getNDFrom() == this.getFK_Node() && tk.getEmpFrom().equals(WebUser.getNo())==true && isCanDo && isDoc == false)
						{
							//@yuan 淇敼娴嬭瘯
							isDoc = true;

						}

						row.setValue("IsDoc", false);

						row.setValue("ParentNode", 0);
						row.setValue("RDT", DataType.IsNullOrEmpty(tk.getRDT()) ? "" : tk.getNDFrom() == tk.getNDTo() && DataType.IsNullOrEmpty(tk.getMsg()) ? "" : tk.getRDT());
						//row["T_NodeIndex"] = tk.Row["T_NodeIndex"];
						//row["T_CheckIndex"] = tk.Row["T_CheckIndex"];

						row.setValue("Msg", tk.getMsgHtml());


						row.setValue("EmpFrom", tk.getEmpFrom());
						row.setValue("EmpFromT", tk.getEmpFromT());
						//鑾峰彇閮ㄩ棬
						String DeptName = "";
						String[] Arrays = tk.getNodeData().split("[@]", -1);
						for (String i : Arrays)
						{
							if (i.contains("DeptName="))
							{
								DeptName = i.split("[=]", -1)[1];
							}
						}
						row.setValue("DeptName", DeptName);
						row.setValue("ActionType", tk.getHisActionType().getValue());
						row.setValue("Tag", tk.getTag());
						row.setValue("FWCView", fwc.getFWCView());
                        if (wcDesc.getSigantureEnabel() != 2)
                            row.setValue("WritImg","");
                        else
                            row.setValue("WritImg",DBAccess.GetBigTextFromDB(trackTable, "MyPK", tk.getMyPK(), "WriteDB"));
						tkDt.Rows.add(row);


							///瀹℃牳缁勪欢闄勪欢鏁版嵁

						//athDBs = new FrmAttachmentDBs();
						//QueryObject obj_Ath = new QueryObject(athDBs);
						//obj_Ath.AddWhere(FrmAttachmentDBAttr.FK_FrmAttachment, "ND" + tk.getNDFrom() + "_FrmWorkCheck");
						//obj_Ath.addAnd();
						//obj_Ath.AddWhere(FrmAttachmentDBAttr.RefPKVal, this.WorkID.ToString());
						//obj_Ath.addAnd();
						//obj_Ath.AddWhere(FrmAttachmentDBAttr.Rec, tk.getEmpFrom());
						//obj_Ath.addOrderBy(FrmAttachmentDBAttr.RDT);
						//obj_Ath.DoQuery();

						//foreach (FrmAttachmentDB athDB in athDBs)
						//{
						//    row = athDt.NewRow();
						//    row["NodeID"] = tk.getNDFrom();
						//    row["MyPK"] = athDB.MyPK;
						//    row["FK_FrmAttachment"] = athDB.FK_FrmAttachment;
						//    row["FK_MapData"] = athDB.FK_MapData;
						//    row["FileName"] = athDB.FileName;
						//    row["FileExts"] = athDB.FileExts;
						//    row["CanDelete"] = athDB.FK_MapData == this.FK_Node.ToString() && athDB.Rec == WebUser.getNo() && isReadonly == false;
						//    athDt.Rows.add(row);
						//}

							///


							/////瀛愭祦绋嬬殑瀹℃牳缁勪欢鏁版嵁
						if (tk.getFID() != 0 && tk.getHisActionType() == ActionType.StartChildenFlow && tkDt.Select("ParentNode=" + tk.getNDFrom()).length == 0)
						{
							String[] paras = tk.getTag().split("[@]", -1);
							String[] p1 = paras[1].split("[=]", -1);
							String fk_flow = p1[1]; //瀛愭祦绋嬬紪鍙�

							String[] p2 = paras[2].split("[=]", -1);
							String workId = p2[1]; //瀛愭祦绋婭D.
							int biaoji = 0;

							WorkCheck subwc = new WorkCheck(fk_flow, Integer.parseInt(fk_flow + "01"), Long.parseLong(workId), 0);
                            String subtrackTable = "ND" + Integer.parseInt(fk_flow) + "Track";
							Tracks subtks = subwc.getHisWorkChecks();
							//鍙栧嚭鏉ュ瓙娴佺▼鐨勬墍鏈夌殑鑺傜偣銆�
							Nodes subNds = new Nodes(fk_flow);
							for (Node item : subNds.ToJavaList()) //涓昏鎸夐『搴忔樉绀�
							{
								for (Track mysubtk : subtks.ToJavaList())
								{
									if (item.getNodeID() != mysubtk.getNDFrom())
									{
										continue;
									}

									/*杈撳嚭璇ュ瓙娴佺▼鐨勫鏍镐俊鎭紝搴旇鑰冭檻瀛愭祦绋嬬殑瀛愭祦绋嬩俊鎭�, 灏变笉鑰冭檻閭ｆ牱澶嶆潅浜�.*/
									if (mysubtk.getHisActionType() == ActionType.WorkCheck)
									{
										// 鍙戣捣澶氫釜瀛愭祦绋嬫椂锛屽彂璧蜂汉鍙樉绀轰竴娆�
										if (mysubtk.getNDFrom() == Integer.parseInt(fk_flow + "01") && biaoji == 1)
										{
											continue;
										}

										row = tkDt.NewRow();
										row.setValue("NodeID", mysubtk.getNDFrom());
										row.setValue("NodeName", String.format("(瀛愭祦绋�)%1$s", mysubtk.getNDFromT()));
										row.setValue("Msg", mysubtk.getMsgHtml());
										row.setValue("EmpFrom", mysubtk.getEmpFrom());
										row.setValue("EmpFromT", mysubtk.getEmpFromT());

										//鑾峰彇閮ㄩ棬
										DeptName = "";
										Arrays = mysubtk.getNodeData().split("[@]", -1);
										for (String i : Arrays)
										{
											if (i.contains("DeptName="))
											{
												DeptName = i.split("[=]", -1)[1];
											}
										}
										row.setValue("DeptName", DeptName);
										row.setValue("RDT", mysubtk.getRDT());
										row.setValue("IsDoc", false);
										row.setValue("ParentNode", tk.getNDFrom());
									   // row["T_NodeIndex"] = idx++;
									   // row["T_CheckIndex"] = noneEmpIdx++;
										row.setValue("ActionType", mysubtk.getHisActionType().getValue());
										row.setValue("Tag", mysubtk.getTag());
                                        if (wcDesc.getSigantureEnabel() != 2)
                                            row.setValue("WritImg","");
                                        else
                                            row.setValue("WritImg",DBAccess.GetBigTextFromDB(subtrackTable, "MyPK", mysubtk.getMyPK(), "WriteDB"));
										tkDt.Rows.add(row);

										if (mysubtk.getNDFrom() == Integer.parseInt(fk_flow + "01"))
										{
											biaoji = 1;
										}
									}
								}
							}
						}

							///
						break;
					default:
						break;
				}

			}

		}

			/// 鍒ゆ柇鏄惁鏄剧ず - 鍘嗗彶瀹℃牳淇℃伅鏄剧ず


			///瀹℃牳鎰忚榛樿濉啓

		//棣栧厛鍒ゆ柇褰撳墠鏄惁鏈夋鎰忚? 濡傛灉鏄��鍥炵殑璇ヤ俊鎭凡缁忓瓨鍦ㄤ簡.
		boolean isHaveMyInfo = false;
		//foreach (DataRow dr in tkDt.Rows)
		//{
		//    string fk_node = dr["NodeID"].ToString();
		//    string empFrom = dr["EmpFrom"].ToString();
		//    if (int.Parse(fk_node) == this.FK_Node && empFrom == WebUser.getNo())
		//        isHaveMyInfo = true;
		//}

		// 澧炲姞榛樿鐨勫鏍告剰瑙�.
		if (isExitTb_doc && wcDesc.getHisFrmWorkCheckSta() == FrmWorkCheckSta.Enable && isCanDo && isReadonly == false && isHaveMyInfo == false)
		{
			DataRow[] rows = null;
			Object tempVar2 = nds.GetEntityByKey(this.getFK_Node());
			nd = tempVar2 instanceof Node ? (Node)tempVar2 : null;
			if (wcDesc.getFWCOrderModel() == FWCOrderModel.SqlAccepter)
			{
				rows = tkDt.Select("NodeID=" + this.getFK_Node() + " AND Msg='' AND EmpFrom='" + WebUser.getNo() + "'");

				if (rows.length == 0)
				{
					rows = tkDt.Select("NodeID=" + this.getFK_Node() + " AND EmpFrom='" + WebUser.getNo() + "'");
				}

				if (rows.length > 0)
				{
					row = rows[0];
					row.setValue("IsDoc", true);

					row.setValue("Msg", Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(), this.getFK_Node(), wcDesc.getFWCDefInfo()));
					if (row.getValue("Msg").toString().equals(""))
					{
						row.setValue("RDT", "");
					}

				}
				else
				{
					row = tkDt.NewRow();
					row.setValue("NodeID", this.getFK_Node());
					row.setValue("NodeName", nd.getFWCNodeName());
					row.setValue("IsDoc", true);
					row.setValue("ParentNode", 0);
					row.setValue("RDT", "");

					row.setValue("Msg", Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(), this.getFK_Node(), wcDesc.getFWCDefInfo()));
					row.setValue("EmpFrom", WebUser.getNo());
					row.setValue("EmpFromT", WebUser.getName());
					row.setValue("DeptName", WebUser.getFK_DeptName());
					//row["T_NodeIndex"] = ++idx;
					//row["T_CheckIndex"] = ++noneEmpIdx;
					row.setValue("ActionType", ActionType.Forward.getValue());
					row.setValue("Tag", Dev2Interface.GetCheckTag(this.getFK_Flow(), this.getWorkID(), this.getFK_Node(), WebUser.getNo()));
                    if (wcDesc.getSigantureEnabel() != 2)
                        row.setValue("WritImg","");
                    else
                    {
                        String sql = "Select MyPK From " + trackTable + "  WHERE ActionType=" + ActionType.WorkCheck.getValue() + " AND  NDFrom=" + this.getFK_Node() + " AND  NDTo=" + this.getFK_Node() + " AND WorkID=" + this.getWorkID() + " AND EmpFrom = '" + WebUser.getNo() + "'";
                        row.setValue("WritImg",DBAccess.GetBigTextFromDB(trackTable, "MyPK", DBAccess.RunSQLReturnVal(sql) == null ? null : DBAccess.RunSQLReturnVal(sql).toString(), "WriteDB"));
                    }
					tkDt.Rows.add(row);
				}
			}
			else
			{
				row = tkDt.NewRow();
				row.setValue("NodeID", this.getFK_Node());
				row.setValue("NodeName", nd.getFWCNodeName());
				row.setValue("IsDoc", true);
				row.setValue("ParentNode", 0);
				row.setValue("RDT", "");

				row.setValue("Msg", Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(), this.getFK_Node(), wcDesc.getFWCDefInfo()));
				row.setValue("EmpFrom", WebUser.getNo());
				row.setValue("EmpFromT", WebUser.getName());
				row.setValue("DeptName", WebUser.getFK_DeptName());
				//row["T_NodeIndex"] = ++idx;
				//row["T_CheckIndex"] = ++noneEmpIdx;
				row.setValue("ActionType", ActionType.Forward.getValue());
				row.setValue("Tag", Dev2Interface.GetCheckTag(this.getFK_Flow(), this.getWorkID(), this.getFK_Node(), WebUser.getNo()));
                if (wcDesc.getSigantureEnabel() != 2)
                    row.setValue("WritImg","");
                else
                {
                    String sql = "Select MyPK From " + trackTable + "  WHERE ActionType=" + ActionType.WorkCheck.getValue() + " AND  NDFrom=" + this.getFK_Node() + " AND  NDTo=" + this.getFK_Node() + " AND WorkID=" + this.getWorkID()+ " AND EmpFrom = '" + WebUser.getNo() + "'";
                    row.setValue("WritImg",DBAccess.GetBigTextFromDB(trackTable, "MyPK", DBAccess.RunSQLReturnVal(sql) == null ? null : DBAccess.RunSQLReturnVal(sql).toString(), "WriteDB"));
                }
				tkDt.Rows.add(row);
			}
		}

			///


			///鏄剧ず鏈夊鏍哥粍浠讹紝浣嗚繕鏈鏍哥殑鑺傜偣. 鍖呮嫭閫�鍥炲悗鐨�.
		if (tks == null)
		{
			tks = wc.getHisWorkChecks();
		}

		for (NodeWorkCheck item : fwcs.ToJavaList())
		{
			if (item.getFWCIsShowTruck() == false)
			{
				continue; //涓嶉渶瑕佹樉绀哄巻鍙茶褰�.
			}

			//鏄惁宸插鏍�.
			boolean isHave = false;
			for (bp.wf.Track tk : tks.ToJavaList())
			{
				//缈昏瘧.
				if (tk.getNDFrom() == this.getFK_Node() && tk.getHisActionType() == ActionType.WorkCheck)
				{
					isHave = true; //宸茬粡鏈変簡
					break;
				}
			}

			if (isHave == true)
			{
				continue;
			}

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
			row.setValue("DeptName", "");
			//row["T_NodeIndex"] = ++idx;
			//row["T_CheckIndex"] = ++noneEmpIdx;

			tkDt.Rows.add(row);
		}

			/// 澧炲姞绌虹櫧.

		ds.Tables.add(tkDt);


		//濡傛灉鏈� SignType 鍒楀氨鑾峰緱绛惧悕淇℃伅.
        if (SystemConfig.getCustomerNo().equals("TianYe")) {
            String tTable = "ND" + Integer.parseInt(getFK_Flow()) + "Track";
            String sql = "SELECT distinct a.No, a.SignType, a.EleID FROM Port_Emp a, " + tTable + " b WHERE (A.No='"
                    + WebUser.getNo() + "') OR B.ActionType=22 AND a.No=b.EmpFrom AND B.WorkID=" + this.getWorkID();

            DataTable dtTrack = DBAccess.RunSQLReturnTable(sql);
            dtTrack.TableName = "SignType";

            dtTrack.Columns.get("NO").setColumnName("No");
            dtTrack.Columns.get("SIGNTYPE").setColumnName("SignType");
            dtTrack.Columns.get("ELEID").setColumnName("EleID");

            ds.Tables.add(dtTrack);
        }

		String str = bp.tools.Json.ToJson(ds);
		//鐢ㄤ簬jflow鏁版嵁杈撳嚭鏍煎紡瀵规瘮.
		//  DataType.WriteFile("c:\\WorkCheck_Init_ccflow.txt", str);
		return str;
	}

	/**
	 * 鑾峰彇瀹℃牳缁勪欢涓垰涓婁紶鐨勯檮浠跺垪琛ㄤ俊鎭�
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
		obj_Ath.AddWhere(FrmAttachmentDBAttr.FK_FrmAttachment, "ND" + this.getFK_Node() + "_FrmWorkCheck");
		obj_Ath.addAnd();
		obj_Ath.AddWhere(FrmAttachmentDBAttr.RefPKVal, String.valueOf(this.getWorkID()));
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
			row.setValue("CanDelete", athDB.getRec().equals(WebUser.getNo()) ? 1 : 0);

			athDt.Rows.add(row);
		}

		return bp.tools.Json.ToJson(athDt);
	}

	/**
	 * 鑾峰彇闄勪欢閾炬帴
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
		switch (athDB.getFileExts()) {
		case "doc":
		case "docx":
		case "xls":
		case "xlsx":
			return "javascript:AthOpenOfiice('" + athDB.getFK_FrmAttachment() + "','" + this.getWorkID() + "','" + athDB.getMyPK()
					+ "','" + athDB.getFK_MapData() + "','" + athDB.getFK_FrmAttachment() + "','" + this.getFK_Node() + "')";
		case "txt":
		case "jpg":
		case "jpeg":
		case "gif":
		case "png":
		case "bmp":
		case "ceb":
			return "javascript:AthOpenView('" + athDB.getRefPKVal() + "','" + athDB.getMyPK() + "','" + athDB.getFK_FrmAttachment() + "','" + athDB.getFileExts() + "','" + this.getFK_Flow() + "','" + athDB.getFK_MapData() + "','" + this.getWorkID() + "','false')";

		case "pdf":
			return athDesc.getSaveTo() + this.getWorkID() + "/" + athDB.getMyPK() + "." + athDB.getFileName();
		}

		return "javascript:AthDown('" + athDB.getFK_FrmAttachment() + "','" + this.getWorkID() + "','" + athDB.getMyPK() + "','" + athDB.getFK_MapData() + "','" + this.getFK_Flow() + "','" + athDB.getFK_FrmAttachment() + "')";

	}

	/** 
	 瀹℃牳淇℃伅淇濆瓨.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String WorkCheck_Save() throws Exception
	{
		//璁捐鐨勬椂鍊�,workid=0,涓嶈鍏跺瓨鍌�.
		if (this.getWorkID() == 0)
		{
			return "";
		}

		// 瀹℃牳淇℃伅.
		String msg = "";
		String writeImg = GetRequestVal("WriteImg");
		if (DataType.IsNullOrEmpty(writeImg) == false)
			writeImg = writeImg.replace('~', '+');
		String dotype = GetRequestVal("ShowType");
		String doc = GetRequestVal("Doc");
		boolean isCC = GetRequestVal("IsCC").equals("1");
		String fwcView = null;
		if (DataType.IsNullOrEmpty(GetRequestVal("FWCView")) == false)
		{
			fwcView = "@FWCView=" + GetRequestValInt("FWCView");
		}
		//鏌ョ湅鏃跺彇娑堜繚瀛�
		if (dotype != null && dotype.equals("View"))
		{
			return "";
		}

		//鍐呭涓虹┖锛屽彇娑堜繚瀛橈紝20170727鍙栨秷姝ゅ闄愬埗
		//if (DataType.IsNullOrEmpty(doc.Trim()))
		//    return "";

		String val = "";
		NodeWorkCheck wcDesc = new NodeWorkCheck(this.getFK_Node());
		if (DataType.IsNullOrEmpty(wcDesc.getFWCFields()) == false)
		{
			//寰幆灞炴�ц幏鍙栧��
			Attrs fwcAttrs = new Attrs(wcDesc.getFWCFields());
			for (Attr attr : fwcAttrs)
			{
				if (attr.getUIContralType()== UIContralType.TB)
				{
					val = GetRequestVal("TB_" + attr.getKey());

					msg += attr.getKey() + "=" + val + ";";
				}
				else if (attr.getUIContralType()== UIContralType.CheckBok)
				{
					val = GetRequestVal("CB_" + attr.getKey());

					msg += attr.getKey() + "=" + Integer.parseInt(val) + ";";
				}
				else if (attr.getUIContralType()== UIContralType.DDL)
				{
					val = GetRequestVal("DDL_" + attr.getKey());

					msg += attr.getKey() + "=" + val + ";";
				}
			}
		}
		else
		{
			// 鍔犲叆瀹℃牳淇℃伅.
			msg = doc;
		}

		//鍦ㄥ鏍镐汉鎵撳紑鍚庯紝鐢宠浜烘挙閿�锛屽氨涓嶄笉鑳借鍏朵繚瀛�.
		String sql = "SELECT FK_Node FROM WF_GenerWorkFlow WHERE WorkID=" + this.getWorkID();
		if (DBAccess.RunSQLReturnValInt(sql) != this.getFK_Node())
		{
			return "err@褰撳墠宸ヤ綔宸茬粡琚挙閿�鎴栬�呭凡缁忕Щ鍔ㄥ埌涓嬩竴涓妭鐐规偍涓嶈兘鍦ㄦ墽琛屽鏍�.";
		}

		// 澶勭悊浜哄ぇ鐨勯渶姹傦紝闇�瑕佹妸瀹℃牳鎰忚鍐欏叆鍒癋lowNote閲岄潰鍘�.
		sql = "UPDATE WF_GenerWorkFlow SET FlowNote='" + msg + "' WHERE WorkID=" + this.getWorkID();
		DBAccess.RunSQL(sql);

		// 鍒ゆ柇鏄惁鏄妱閫�?
		if (isCC)
		{
			// 鍐欏叆瀹℃牳淇℃伅锛屾湁鍙兘鏄痷pdate鏁版嵁銆�
			Dev2Interface.WriteTrackWorkCheck(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), this.getFID(), msg, wcDesc.getFWCOpLabel(),wcDesc.getSigantureEnabel() == 2 ? writeImg : "");

			//璁剧疆鎶勯�佺姸鎬� - 宸茬粡瀹℃牳瀹屾瘯.
			Dev2Interface.Node_CC_SetSta(this.getFK_Node(), this.getWorkID(), WebUser.getNo(), CCSta.CheckOver);
			return "";
		}


			///鏍规嵁绫诲瀷鍐欏叆鏁版嵁  qin
		if (wcDesc.getHisFrmWorkCheckType() == FWCType.Check) //瀹℃牳缁勪欢
		{
			//鍒ゆ柇鏄惁瀹℃牳缁勪欢涓�"鍗忎綔妯″紡涓嬫搷浣滃憳鏄剧ず椤哄簭"璁剧疆涓�"鎸夌収鎺ュ彈浜哄憳鍒楄〃鍏堝悗椤哄簭(瀹樿亴澶у皬)"锛屽垹闄ゅ師鏈夌殑绌哄鏍镐俊鎭�
			if (wcDesc.getFWCOrderModel() == FWCOrderModel.SqlAccepter)
			{
				sql = "DELETE FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track WHERE WorkID = " + this.getWorkID() + " AND ActionType = " + ActionType.WorkCheck.getValue() + " AND NDFrom = " + this.getFK_Node() + " AND NDTo = " + this.getFK_Node() + " AND EmpFrom = '" + WebUser.getNo() + "'";
				DBAccess.RunSQL(sql);
			}
			if(DataType.IsNullOrEmpty(writeImg)){
				/*濡傛灉娌℃湁姝ゅ垪锛屽氨鑷姩鍒涘缓姝ゅ垪.*/
				if (DBAccess.IsExitsTableCol("ND" + Integer.parseInt(this.getFK_Flow()) + "Track", "WriteDB") == false)
				{
					String sqlWriteDB = "ALTER TABLE ND" + Integer.parseInt(this.getFK_Flow()) + "Track ADD  WriteDB BLOB ";
					DBAccess.RunSQL(sqlWriteDB);
				}
				sql="SELECT	WriteDB,Msg FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track WHERE WorkID = " + this.getWorkID() + " AND ActionType = " + ActionType.Forward.getValue() +" AND NDFrom = " + this.getFK_Node() + " AND EmpFrom = '" + WebUser.getNo() + "' ORDER BY	rdt DESC";
				DataTable dt = DBAccess.RunSQLReturnTable(sql);

				if(dt.Rows.size() > 0){
					writeImg = dt.Rows.get(0).getValue("WriteDb")==null?"":dt.Rows.get(0).getValue("WriteDb").toString();
					msg = dt.Rows.get(0).getValue("Msg")==null?"":dt.Rows.get(0).getValue("Msg").toString().substring(dt.Rows.get(0).getValue("Msg").toString().indexOf("@")+1);
				}
			}
			Dev2Interface.WriteTrackWorkCheck(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), this.getFID(), msg, wcDesc.getFWCOpLabel(),wcDesc.getSigantureEnabel() == 2 ? writeImg : "", fwcView);
		}

		if (wcDesc.getHisFrmWorkCheckType() == FWCType.DailyLog) //鏃ュ織缁勪欢
		{
			Dev2Interface.WriteTrackDailyLog(this.getFK_Flow(), this.getFK_Node(), wcDesc.getName(), this.getWorkID(), this.getFID(), msg, wcDesc.getFWCOpLabel());
		}
		if (wcDesc.getHisFrmWorkCheckType() == FWCType.WeekLog) //鍛ㄦ姤
		{
			Dev2Interface.WriteTrackWeekLog(this.getFK_Flow(), this.getFK_Node(), wcDesc.getName(), this.getWorkID(), this.getFID(), msg, wcDesc.getFWCOpLabel());
		}
		if (wcDesc.getHisFrmWorkCheckType() == FWCType.MonthLog) //鏈堟姤
		{
			Dev2Interface.WriteTrackMonthLog(this.getFK_Flow(), this.getFK_Node(), wcDesc.getName(), this.getWorkID(), this.getFID(), msg, wcDesc.getFWCOpLabel());
		}


		sql = "SELECT MyPK,RDT FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track WHERE NDFrom = " + this.getFK_Node() + " AND ActionType = " + ActionType.WorkCheck.getValue() + " AND EmpFrom = \'" + WebUser.getNo() + "\'";
		DataTable dt = DBAccess.RunSQLReturnTable(sql, 1, 1, "MyPK", "RDT", "DESC");

		return dt.Rows.size() > 0 ? dt.Rows.get(0).getValue("RDT").toString() : "";
	}

	///

	/// 宸ヤ綔鍒嗛厤.
	/**
	 * 鍒嗛厤宸ヤ綔
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String AllotTask_Init() throws Exception {
		GenerWorkerLists wls = new GenerWorkerLists(this.getWorkID(), this.getFK_Node(), true);
		return wls.ToJson();
	}

	/**
	 * 鍒嗛厤宸ヤ綔
	 * 
	 * @return
	 */
	public final String AllotTask_Save() {
		return "";
	}

	///

	/// 鎵ц璺宠浆.
	/** 
	 杩斿洖鍙互璺宠浆鐨勮妭鐐�.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String FlowSkip_Init() throws Exception
	{
		Node nd = new Node(this.getFK_Node());
		bp.wf.template.BtnLab lab = new BtnLab(this.getFK_Node());

		String sql = "SELECT NodeID,Name FROM WF_Node WHERE FK_Flow='" + this.getFK_Flow() + "'";
		switch (lab.getJumpWayEnum())
		{
			case Previous:
				sql = "SELECT NodeID,Name FROM WF_Node WHERE NodeID IN (SELECT FK_Node FROM WF_GenerWorkerlist WHERE WorkID=" + this.getWorkID() + " )";
				break;
			case Next:
				sql = "SELECT NodeID,Name FROM WF_Node WHERE NodeID NOT IN (SELECT FK_Node FROM WF_GenerWorkerlist WHERE WorkID=" + this.getWorkID() + " ) AND FK_Flow='" + this.getFK_Flow() + "'";
				break;
			case AnyNode:
				sql = "SELECT NodeID,Name FROM WF_Node WHERE FK_Flow='" + this.getFK_Flow() + "' ORDER BY STEP";
				break;
			case JumpSpecifiedNodes:
				sql = nd.getJumpToNodes();
				sql = sql.replace("@WebUser.No", WebUser.getNo());
				sql = sql.replace("@WebUser.Name", WebUser.getName());
				sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
				if (sql.contains("@"))
				{
					Work wk = nd.getHisWork();
					wk.setOID(this.getWorkID());
					wk.RetrieveFromDBSources();
					for (Attr attr : wk.getEnMap().getAttrs())
					{
						if (sql.contains("@") == false)
						{
							break;
						}
						sql = sql.replace("@" + attr.getKey(), wk.GetValStrByKey(attr.getKey()));
					}
				}
				break;
			case CanNotJump:
				return "err@姝よ妭鐐逛笉鍏佽璺宠浆.";
			default:
				return "err@鏈垽鏂�";
		}

		sql = sql.replace("~", "'");
		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		//濡傛灉鏄痮racle,灏辫浆鎴愬皬鍐�.
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dt.Columns.get("NODEID").setColumnName("NodeID");
			dt.Columns.get("NAME").setColumnName("Name");
		}
		return bp.tools.Json.ToJson(dt);
	}

	/**
	 * 鎵ц璺宠浆
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String FlowSkip_Do() throws Exception {
		try {
			Node ndJump = new Node(this.GetRequestValInt("GoNode"));
			bp.wf.WorkNode wn = new bp.wf.WorkNode(this.getWorkID(), this.getFK_Node());
			String msg = wn.NodeSend(ndJump, null).ToMsgOfHtml();
			return msg;
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}

	/// 鎵ц璺宠浆.

	/// 鎵ц鐖剁被鐨勯噸鍐欐柟娉�.
	/**
	 * 榛樿鎵ц鐨勬柟娉�
	 * 
	 * @return
	 */
	@Override
	protected String DoDefaultMethod() {
		switch (this.getDoType()) {

		case "DtlFieldUp": // 瀛楁涓婄Щ
			return "鎵ц鎴愬姛.";
		default:
			break;
		}

		// 鎵句笉涓嶅埌鏍囪灏辨姏鍑哄紓甯�.
		throw new RuntimeException("@鏍囪[" + this.getDoType() + "]锛屾病鏈夋壘鍒�.");
	}

	/// 鎵ц鐖剁被鐨勯噸鍐欐柟娉�.

	/// 鎶勯�丄dv.
	/**
	 * 閫夋嫨鏉冮檺缁�
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String CCAdv_SelectGroups() throws Exception {
		String sql = "SELECT NO,NAME FROM GPM_Group ORDER BY IDX";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		return bp.tools.Json.ToJson(dt);
	}

	/** 
	 鎶勯�佸垵濮嬪寲.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String CCAdv_Init() throws Exception
	{
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		Hashtable ht = new Hashtable();
		ht.put("Title", gwf.getTitle());

		//璁＄畻鍑烘潵鏇剧粡鎶勯�佽繃鐨勪汉.
		Paras ps = new Paras();
		ps.SQL="SELECT CCToName FROM WF_CCList WHERE FK_Node=" + SystemConfig.getAppCenterDBVarStr() + "FK_Node AND WorkID=" + SystemConfig.getAppCenterDBVarStr() + "WorkID";
		ps.Add("FK_Node", this.getFK_Node());
		ps.Add("WorkID", this.getWorkID());
		DataTable mydt = DBAccess.RunSQLReturnTable(ps);
		String toAllEmps = "";
		for (DataRow dr : mydt.Rows)
		{
			toAllEmps += dr.getValue(0).toString() + ",";
		}

		ht.put("CCTo", toAllEmps);

		// 鏍规嵁浠栧垽鏂槸鍚︽樉绀烘潈闄愮粍銆�
		if (DBAccess.IsExitsObject("GPM_Group") == true)
		{
			ht.put("IsGroup", "1");
		}
		else
		{
			ht.put("IsGroup", "0");
		}

		//杩斿洖娴佺▼鏍囬.
		return bp.tools.Json.ToJsonEntityModel(ht);
	}

	/**
	 * 閫夋嫨閮ㄩ棬鍛堢幇淇℃伅.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String CCAdv_SelectDepts() throws Exception {
		bp.port.Depts depts = new bp.port.Depts();
		depts.RetrieveAll();
		return depts.ToJson();
	}

	/**
	 * 閫夋嫨閮ㄩ棬鍛堢幇淇℃伅.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String CCAdv_SelectStations() throws Exception {
		// 宀椾綅绫诲瀷.
		String sql = "SELECT NO,NAME FROM Port_StationType ORDER BY NO";
		DataSet ds = new DataSet();
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Port_StationType";
		ds.Tables.add(dt);

		// 宀椾綅.
		String sqlStas = "SELECT NO,NAME,FK_STATIONTYPE FROM Port_Station ORDER BY FK_STATIONTYPE,NO";
		DataTable dtSta = DBAccess.RunSQLReturnTable(sqlStas);
		dtSta.TableName = "Port_Station";
		ds.Tables.add(dtSta);
		return bp.tools.Json.ToJson(ds);
	}

	/**
	 * 鎶勯�佸彂閫�.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String CCAdv_Send() throws Exception {
		// 浜哄憳淇℃伅. 鏍煎紡 zhangsan,寮犱笁;lisi,鏉庡洓;
		String emps = this.GetRequestVal("Emps");

		// 宀椾綅淇℃伅. 鏍煎紡: 001,002,003,
		String stations = this.GetRequestVal("Stations");
		stations = stations.replace(";", ",");

		// 鏉冮檺缁�. 鏍煎紡: 001,002,003,
		String groups = this.GetRequestVal("Groups");
		if (groups == null) {
			groups = "";
		}
		groups = groups.replace(";", ",");

		// 閮ㄩ棬淇℃伅. 鏍煎紡: 001,002,003,
		String depts = this.GetRequestVal("Depts");
		// 鏍囬.
		String title = this.GetRequestVal("TB_Title");
		// 鍐呭.
		String doc = this.GetRequestVal("TB_Doc");

		// 璋冪敤鎶勯�佹帴鍙ｆ墽琛屾妱閫�.
		String ccRec = bp.wf.Dev2Interface.Node_CC_WriteTo_CClist(this.getFK_Node(), this.getWorkID(), title, doc, emps,
				depts, stations, groups);

		if (ccRec.equals("")) {
			return "娌℃湁鎶勯�佸埌浠讳綍浜恒��";
		} else {
			return "鏈鎶勯�佺粰濡備笅浜哄憳锛�" + ccRec;
		}
		// return "鎵ц鎶勯�佹垚鍔�.emps=(" + emps + ") depts=(" + depts + ") stas=(" +
		// stations + ") 鏍囬:" + title + " ,鎶勯�佸唴瀹�:" + doc;
	}

	/// 鎶勯�丄dv.

	/// 鎶勯�佹櫘閫氱殑鎶勯��.
	public final String CC_AddEmps() throws Exception {
		String toEmpStrs = this.GetRequestVal("AddEmps");
		toEmpStrs = toEmpStrs.replace(",", ";");
		String[] toEmps = toEmpStrs.split("[;]", -1);
		String infos = "";
		for (String empStr : toEmps) {
			if (DataType.IsNullOrEmpty(empStr) == true) {
				continue;
			}

			bp.gpm.Emp emp = new bp.gpm.Emp(empStr);

			CCList cc = new CCList();
			cc.setFK_Flow(this.getFK_Flow());
			cc.setFK_Node(this.getFK_Node());
			cc.setWorkID(this.getWorkID());
			cc.setRec(emp.getNo());

			cc.Insert();
		}

		return "";
	}

	/**
	 * 鎵ц鍙戦��.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String CC_Send2020() throws Exception {
		// 浜哄憳淇℃伅. 鏍煎紡 zhangsan,寮犱笁;lisi,鏉庡洓;
		String emps = this.GetRequestVal("Emps");

		// 璋冪敤鎶勯�佹帴鍙ｆ墽琛屾妱閫�.
		String ccRec = bp.wf.Dev2Interface.Node_CCTo(this.getWorkID(), emps);
		return ccRec;
	}

	/**
	 * 鎶勯�佸彂閫�.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String CC_Send() throws Exception {
		// 浜哄憳淇℃伅. 鏍煎紡 zhangsan,寮犱笁;lisi,鏉庡洓;
		String emps = this.GetRequestVal("Emps");

		// 宀椾綅淇℃伅. 鏍煎紡: 001,002,003,
		String stations = this.GetRequestVal("Stations");
		if(DataType.IsNullOrEmpty(stations) == false)
			stations = stations.replace(";", ",");

		// 鏉冮檺缁�. 鏍煎紡: 001,002,003,
		String groups = this.GetRequestVal("Groups");
		if (groups == null) {
			groups = "";
		}
		groups = groups.replace(";", ",");

		// 閮ㄩ棬淇℃伅. 鏍煎紡: 001,002,003,
		String depts = this.GetRequestVal("Depts");
		// 鏍囬.
		String title = this.GetRequestVal("TB_Title");
		// 鍐呭.
		String doc = this.GetRequestVal("TB_Doc");

		// 璋冪敤鎶勯�佹帴鍙ｆ墽琛屾妱閫�.
		String ccRec = bp.wf.Dev2Interface.Node_CC_WriteTo_CClist(this.getFK_Node(), this.getWorkID(), title, doc, emps,
				depts, stations, groups);

		if (ccRec.equals("")) {
			return "娌℃湁鎶勯�佸埌浠讳綍浜恒��";
		} else {
			return "鏈鎶勯�佺粰濡備笅浜哄憳锛�" + ccRec;
		}
		// return "鎵ц鎶勯�佹垚鍔�.emps=(" + emps + ") depts=(" + depts + ") stas=(" +
		// stations + ") 鏍囬:" + title + " ,鎶勯�佸唴瀹�:" + doc;
	}

	/// 鎶勯�佹櫘閫氱殑鎶勯��.

	/// 閫�鍥炲埌鍒嗘祦鑺傜偣澶勭悊鍣�.
	/**
	 * 鍒濆鍖�.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String DealSubThreadReturnToHL_Init() throws Exception {
		/* 濡傛灉宸ヤ綔鑺傜偣閫�鍥炰簡 */
		bp.wf.ReturnWorks rws = new bp.wf.ReturnWorks();
		rws.Retrieve(bp.wf.ReturnWorkAttr.ReturnToNode, this.getFK_Node(), bp.wf.ReturnWorkAttr.WorkID,
				this.getWorkID(), bp.wf.ReturnWorkAttr.RDT);

		String msgInfo = "";
		if (rws.size() != 0) {
			for (bp.wf.ReturnWork rw : rws.ToJavaList()) {
				msgInfo += "<fieldset width='100%' ><legend>&nbsp; 鏉ヨ嚜鑺傜偣:" + rw.getReturnNodeName() + " 閫�鍥炰汉:"
						+ rw.getReturnerName() + "  " + rw.getRDT() + "&nbsp;<a href='./../../DataUser/ReturnLog/"
						+ this.getFK_Flow() + "/" + rw.getMyPK() + ".htm' target=_blank>宸ヤ綔鏃ュ織</a></legend>";
				msgInfo += rw.getBeiZhuHtml();
				msgInfo += "</fieldset>";
			}
		}

		// 鎶婅妭鐐逛俊鎭篃浼犲叆杩囧幓锛岀敤浜庡垽鏂笉鍚岀殑鎸夐挳鏄剧ず.
		bp.wf.template.BtnLab btn = new BtnLab(this.getFK_Node());
		bp.wf.Node nd = new Node(this.getFK_Node());

		Hashtable ht = new Hashtable();
		// 娑堟伅.
		ht.put("MsgInfo", msgInfo);

		// 鏄惁鍙互绉讳氦锛�
		if (btn.getShiftEnable()) {
			ht.put("ShiftEnable", "1");
		} else {
			ht.put("ShiftEnable", "0");
		}

		// 鏄惁鍙互鎾ら攢锛�
		if (nd.getHisCancelRole() == CancelRole.None) {
			ht.put("CancelRole", "0");
		} else {
			ht.put("CancelRole", "1");
		}

		// 鏄惁鍙互鍒犻櫎瀛愮嚎绋�? 鍦ㄥ垎娴佽妭鐐逛笂.
		if (btn.getThreadIsCanDel()) {
			ht.put("ThreadIsCanDel", "1");
		} else {
			ht.put("ThreadIsCanDel", "0");
		}

		// 鏄惁鍙互绉讳氦瀛愮嚎绋�? 鍦ㄥ垎娴佽妭鐐逛笂.
		if (btn.getThreadIsCanShift()) {
			ht.put("ThreadIsCanShift", "1");
		} else {
			ht.put("ThreadIsCanShift", "0");
		}

		return bp.tools.Json.ToJsonEntityModel(ht);
	}

	/**
	 * 淇濆瓨
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String DealSubThreadReturnToHL_Done() throws Exception {
		// 鎿嶄綔绫诲瀷.
		String actionType = this.GetRequestVal("ActionType");
		String note = this.GetRequestVal("Note");

		if (actionType.equals("Return")) {
			/* 濡傛灉鏄��鍥�. */
			bp.wf.ReturnWork rw = new bp.wf.ReturnWork();
			rw.Retrieve(bp.wf.ReturnWorkAttr.ReturnToNode, this.getFK_Node(), bp.wf.ReturnWorkAttr.WorkID,
					this.getWorkID());
			String info = bp.wf.Dev2Interface.Node_ReturnWork(this.getFK_Flow(), this.getWorkID(), this.getFID(),
					this.getFK_Node(), rw.getReturnNode(), note, false);
			return info;
		}

		if (actionType.equals("Shift")) {
			/* 濡傛灉鏄Щ浜ゆ搷浣�. */
			String toEmps = this.GetRequestVal("ShiftToEmp");
			return bp.wf.Dev2Interface.Node_Shift(this.getWorkID(), toEmps, note);
		}

		if (actionType.equals("Kill")) {
			String msg = bp.wf.Dev2Interface.Flow_DeleteSubThread(this.getWorkID(), "鎵嬪伐鍒犻櫎");
			// 鎻愮ず淇℃伅.
			if (DataType.IsNullOrEmpty(msg)==true)
				msg = "璇ュ伐浣滃垹闄ゆ垚鍔�...";

			return msg;
		}

		if (actionType.equals("UnSend")) {
			return bp.wf.Dev2Interface.Flow_DoUnSend(this.getFK_Flow(), this.getFID(), this.getFK_Node());
		}

		return "err@娌℃湁鍒ゆ柇鐨勭被鍨�" + actionType;
	}

	/// 閫�鍥炲埌鍒嗘祦鑺傜偣澶勭悊鍣�.

	public final String DeleteFlowInstance_Init() throws Exception {
		if (bp.wf.Dev2Interface.Flow_IsCanDeleteFlowInstance(this.getFK_Flow(), this.getWorkID(),
				WebUser.getNo()) == false) {
			return "err@鎮ㄦ病鏈夊垹闄よ娴佺▼鐨勬潈闄�";
		}
		// 鑾峰彇鑺傜偣涓厤缃殑娴佺▼鍒犻櫎瑙勫垯
		if (this.getFK_Node() != 0) {
			Paras ps = new Paras();
			ps.SQL = "SELECT wn.DelEnable FROM WF_Node wn WHERE wn.NodeID = " + SystemConfig.getAppCenterDBVarStr()
					+ "NodeID";
			ps.Add("NodeID", this.getFK_Node());
			return DBAccess.RunSQLReturnValInt(ps) + "";
		}

		return "鍒犻櫎鎴愬姛.";
	}

	public final String DeleteFlowInstance_DoDelete() throws Exception {
		if (bp.wf.Dev2Interface.Flow_IsCanDeleteFlowInstance(this.getFK_Flow(), this.getWorkID(),
				WebUser.getNo()) == false) {
			return "err@鎮ㄦ病鏈夊垹闄よ娴佺▼鐨勬潈闄�.";
		}

		String deleteWay = this.GetRequestVal("RB_DeleteWay");
		String doc = this.GetRequestVal("TB_Doc");

		// 鏄惁瑕佸垹闄ゅ瓙娴佺▼锛� 杩欓噷娉ㄦ剰鍙橀噺鐨勮幏鍙栨柟寮忥紝浣犲彲浠ヨ嚜宸卞畾涔�.
		String isDeleteSubFlow = this.GetRequestVal("CB_IsDeleteSubFlow");

		boolean isDelSubFlow = false;
		if (isDeleteSubFlow.equals("1")) {
			isDelSubFlow = true;
		}

		// 鎸夌収鏍囪鍒犻櫎.
		if (deleteWay.equals("1")) {
			bp.wf.Dev2Interface.Flow_DoDeleteFlowByFlag(this.getFK_Flow(), this.getWorkID(), doc, isDelSubFlow);
		}

		// 褰诲簳鍒犻櫎.
		if (deleteWay.equals("3")) {
			bp.wf.Dev2Interface.Flow_DoDeleteFlowByReal(this.getWorkID(), isDelSubFlow);
		}

		// 褰诲簳骞舵斁鍏ュ埌鍒犻櫎杞ㄨ抗閲�.
		if (deleteWay.equals("2")) {
			bp.wf.Dev2Interface.Flow_DoDeleteFlowByWriteLog(this.getFK_Flow(), this.getWorkID(), doc, isDelSubFlow);
		}

		return "娴佺▼鍒犻櫎鎴愬姛.";
	}
	/**
	 * 鑾峰緱鑺傜偣琛ㄥ崟鏁版嵁.
	 * 
	 * @return
	 */
	// public string ViewWorkNodeFrm()
	// {
	// Node nd = new Node(this.FK_Node);
	// nd.WorkID=this.WorkID; //涓鸿幏鍙栬〃鍗旾D ( NodeFrmID )鎻愪緵鍙傛暟.

	// Hashtable ht = new Hashtable();
	// ht.Add("FormType", nd.FormType.ToString());
	// ht.Add("Url", nd.FormUrl + "&WorkID=" + this.WorkID + "&FK_Flow=" +
	// this.FK_Flow + "&FK_Node=" + this.FK_Node);

	// if (nd.FormType == NodeFormType.SDKForm)
	// return bp.tools.Json.ToJsonEntityModel(ht);

	// if (nd.FormType == NodeFormType.SelfForm)
	// return bp.tools.Json.ToJsonEntityModel(ht);

	// //琛ㄥ崟妯＄増.
	// DataSet myds = bp.sys.CCFormAPI.GenerHisDataSet(nd.NodeFrmID);
	// string json = BP.WF.Dev2Interface.CCFrom_GetFrmDBJson(this.FK_Flow,
	// this.MyPK);
	// DataTable mainTable = bp.tools.Json.ToDataTableOneRow(json);
	// mainTable.TableName = "MainTable";
	// myds.Tables.add(mainTable);

	// //MapExts exts = new MapExts(nd.HisWork.ToString());
	// //DataTable dtMapExt = exts.ToDataTableDescField();
	// //dtMapExt.TableName = "Sys_MapExt";
	// //myds.Tables.add(dtMapExt);

	// return bp.tools.Json.ToJson(myds);
	// }

	/**
	 * 鍥炲鍔犵淇℃伅.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String AskForRe() throws Exception {
		String note = this.GetRequestVal("Note"); // 鍘熷洜.
		return bp.wf.Dev2Interface.Node_AskforReply(this.getWorkID(), note);
	}

	/**
	 * 鎵ц鍔犵
	 * 
	 * @return 鎵ц淇℃伅
	 * @throws Exception 
	 */
	public final String Askfor() throws Exception {
		long workID = Integer.parseInt(this.GetRequestVal("WorkID")); // 宸ヤ綔ID
		String toEmp = this.GetRequestVal("ToEmp"); // 璁╄皝鍔犵?
		String note = this.GetRequestVal("Note"); // 鍘熷洜.
		String model = this.GetRequestVal("Model"); // 妯″紡.

		bp.wf.AskforHelpSta sta = bp.wf.AskforHelpSta.AfterDealSend;
		if (model.equals("1")) {
			sta = bp.wf.AskforHelpSta.AfterDealSendByWorker;
		}

		return bp.wf.Dev2Interface.Node_Askfor(workID, sta, toEmp, note);
	}

	/** 
	 浜哄憳閫夋嫨鍣�
	 
	 @return 
	 * @throws Exception 
	*/
	public final String SelectEmps_Init() throws Exception
	{
		String fk_dept = this.getFK_Dept();
		if (DataType.IsNullOrEmpty(fk_dept) == true || fk_dept.equals("undefined") == true)
		{
			fk_dept = WebUser.getFK_Dept();
		}

		DataSet ds = new DataSet();

		String sql = "";
		sql = "SELECT No,Name,ParentNo FROM Port_Dept WHERE No='" + fk_dept + "' OR ParentNo='" + fk_dept + "' ORDER BY Idx";



		DataTable dtDept = DBAccess.RunSQLReturnTable(sql);
		if (dtDept.Rows.size() == 0)
		{
			fk_dept = WebUser.getFK_Dept();
			sql = "SELECT No,Name,ParentNo FROM Port_Dept WHERE No='" + fk_dept + "' OR ParentNo='" + fk_dept + "' ORDER BY Idx ";
			dtDept = DBAccess.RunSQLReturnTable(sql);
		}

		dtDept.TableName = "Depts";
		ds.Tables.add(dtDept);

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dtDept.Columns.get(0).setColumnName("No");
			dtDept.Columns.get(1).setColumnName("Name");
			dtDept.Columns.get(2).setColumnName("ParentNo");
		}

		if (SystemConfig.getCustomerNo().equals("TianYe"))
		{
			Object obj = SystemConfig.getAppSettings().get("SpecFlowNosForAccpter");
			String specFlowNos = obj == null?null:obj.toString();
			if (specFlowNos.equals("") || specFlowNos == null)
			{
				specFlowNos = ",,";
			}

			String specEmpNos = "";
			if (specFlowNos.contains("," + String.valueOf(this.getFK_Node()) + ",") == false)
			{
				specEmpNos = " AND No!='00000001' ";
			}
			sql = "SELECT No,Name,FK_Dept FROM Port_Emp WHERE FK_Dept='" + fk_dept + "' " + specEmpNos + "  ORDER BY Idx ";
		}
		else
		{
			sql = "SELECT distinct A.No,A.Name, '" + fk_dept + "' as FK_Dept, a.Idx FROM Port_Emp A LEFT JOIN Port_DeptEmp B  ON A.No=B.FK_Emp WHERE A.FK_Dept='" + fk_dept + "' OR B.FK_Dept='" + fk_dept + "' ";
			sql += " ORDER BY A.Idx ";
		}

		DataTable dtEmps = DBAccess.RunSQLReturnTable(sql);
		dtEmps.TableName = "Emps";
		ds.Tables.add(dtEmps);
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dtEmps.Columns.get(0).setColumnName("No");
			dtEmps.Columns.get(1).setColumnName("Name");
			dtEmps.Columns.get(2).setColumnName("FK_Dept");
		}

		//杞寲涓� json 
		return bp.tools.Json.ToJson(ds);
	}

	/// 閫夋嫨鎺ュ彈浜�.
	/** 
	 鍒濆鍖栨帴鍙椾汉.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Accepter_Init() throws Exception
	{
		/*濡傛灉鏄崗浣滄ā寮�, 灏辫妫�鏌ュ綋鍓嶆槸鍚︿富鎸佷汉, 褰撳墠鏄惁鏄細绛炬ā寮�. */
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		if (gwf.getFK_Node() != this.getFK_Node())
		{
			return "err@褰撳墠娴佺▼宸茬粡杩愬姩鍒癧" + gwf.getNodeName() + "]涓�,褰撳墠澶勭悊浜哄憳涓篬" + gwf.getTodoEmps() + "]";
		}

		//褰撳墠鑺傜偣ID.
		Node nd = new Node(this.getFK_Node());

		//鍒ゆ柇褰撳墠鏄惁鏄崗浣滄ā寮�.
		if (nd.getTodolistModel() == TodolistModel.Teamup && nd.getIsStartNode() == false)
		{
			if (gwf.getTodoEmps().contains(WebUser.getNo() + ","))
			{
				/* 璇存槑鎴戞槸涓绘寔浜轰箣涓�, 鎴戝氨鍙互閫夋嫨鎺ュ彈浜�,鍙戦�佸埌涓嬩竴涓妭鐐逛笂鍘�. */
			}
			else
			{
				//  string err= "err@娴佺▼閰嶇疆閫昏緫閿欒锛屽綋鍓嶈妭鐐规槸鍗忎綔妯″紡锛屽綋鍓嶈妭鐐圭殑鏂瑰悜鏉′欢涓嶅厑璁竅鍙戦�佹寜閽梺涓嬫媺妗嗛�夋嫨(榛樿妯″紡)].";
				//  err += "锛屽鏋滈渶瑕佹墜宸ラ�夋嫨锛岃浣跨敤[鑺傜偣灞炴�-[璁剧疆鏂瑰悜鏉′欢]-[鎸夌収鐢ㄦ埛鎵ц鍙戦�佸悗鎵嬪伐閫夋嫨璁＄畻]妯″紡璁＄畻.";
				//  return err;

				/* 涓嶆槸涓绘寔浜哄氨鎵ц鍙戦�侊紝杩斿洖鍙戦�佺粨鏋�. */

				//鍒ゆ柇鏄惁鏈変笉鍙戦�佹爣璁帮紵
				if (this.GetRequestValBoolen("IsSend") == true)
				{
					SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(this.getFK_Flow(), this.getWorkID());
					return "info@" + objs.ToMsgOfHtml();
				}
			}
		}

		int toNodeID = this.GetRequestValInt("ToNode");
		if (toNodeID == 0)
		{
			Nodes nds = nd.getHisToNodes();
			if (nds.size() == 1)
			{
				toNodeID = nds.get(0).GetValIntByKey("NodeID");
			}
			else
			{
				return "err@鍙傛暟閿欒,蹇呴』浼犻�掓潵鍒拌揪鐨勮妭鐐笽D ToNode .";
			}
		}

		Work wk = nd.getHisWork();
		wk.setOID(this.getWorkID());
		wk.Retrieve();

		Selector select = new Selector(toNodeID);

		if (select.getSelectorModel() == SelectorModel.GenerUserSelecter)
		{
			return "url@AccepterOfGener.htm?WorkID=" + this.getWorkID() + "&FK_Node=" + this.getFK_Node() + "&FK_Flow=" + nd.getFK_Flow() + "&ToNode=" + toNodeID + "&PWorkID=" + gwf.getPWorkID();
		}

		if (select.getSelectorModel() == SelectorModel.AccepterOfDeptStationEmp)
		{
			return "url@AccepterOfDeptStationEmp.htm?WorkID=" + this.getWorkID() + "&FK_Node=" + this.getFK_Node() + "&FK_Flow=" + nd.getFK_Flow() + "&ToNode=" + toNodeID + "&PWorkID=" + gwf.getPWorkID();
		}

		if (select.getSelectorModel() == SelectorModel.Url)
		{
			return "BySelfUrl@" + select.getSelectorP1() + "?WorkID=" + this.getWorkID() + "&FK_Node=" + this.getFK_Node() + "&FK_Flow=" + nd.getFK_Flow() + "&ToNode=" + toNodeID + "&PWorkID=" + gwf.getPWorkID();
		}

		//鑾峰緱 閮ㄩ棬涓庝汉鍛�.
		DataSet ds = select.GenerDataSet(toNodeID, wk);

		if (SystemConfig.getCustomerNo().equals("TianYe")) //澶╀笟闆嗗洟锛屽幓鎺�00000001钁ｄ簨闀�
		{
		}

		//澧炲姞鍒ゆ柇.
		if (ds.GetTableByName("Emps").Rows.size() == 0)
		{
			return "err@閰嶇疆鎺ュ彈浜鸿寖鍥翠负绌�,璇疯仈绯荤鐞嗗憳.";
		}

		////鍙湁涓�涓汉锛屽氨璁╁叾鍙戦�佷笅鍘�.
		//if (ds.GetTableByName("Emps"].Rows.size() == 1)
		//{
		//    string emp = ds.GetTableByName("Emps"].Rows.get(0).getValue(0].ToString();
		//    SendReturnObjs objs= BP.WF.Dev2Interface.Node_SendWork(this.FK_Flow, this.WorkID, toNodeID, emp);
		//    return  "info@"+objs.ToMsgOfText();
		//}


			///璁＄畻涓婁竴娆￠�夋嫨鐨勭粨鏋�, 骞舵妸缁撴灉杩斿洖杩囧幓.
		String sql = "";
		DataTable dt = new DataTable();
		dt.Columns.Add("No", String.class);
		dt.TableName = "Selected";
		if (select.getIsAutoLoadEmps() == true)
		{
			//鑾峰彇褰撳墠鑺傜偣鐨凷electAccper鐨勫�� @sly
			SelectAccpers selectAccpers = new SelectAccpers();
			selectAccpers.Retrieve(SelectAccperAttr.WorkID, this.getWorkID(), SelectAccperAttr.FK_Node, toNodeID);
			if (selectAccpers.size() != 0)
			{
				for (SelectAccper sa : selectAccpers.ToJavaList())
				{
					DataRow dr = dt.NewRow();
					dr.setValue(0, sa.getFK_Emp());
					dt.Rows.add(dr);
				}
			}
			else
			{
				if (SystemConfig.getAppCenterDBType() == DBType.MSSQL)
				{
					sql = "SELECT  top 1 Tag,EmpTo FROM ND" + Integer.parseInt(nd.getFK_Flow()) + "Track A WHERE A.NDFrom=" + this.getFK_Node() + " AND A.NDTo=" + toNodeID + " AND ActionType=1 ORDER BY WorkID DESC";
				}
				else if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
				{
					sql = "SELECT * FROM (SELECT  Tag,EmpTo,WorkID FROM ND" + Integer.parseInt(nd.getFK_Flow()) + "Track A WHERE A.NDFrom=" + this.getFK_Node() + " AND A.NDTo=" + toNodeID + " AND ActionType=1 ORDER BY WorkID DESC ) WHERE ROWNUM =1";
				}
				else if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
				{
					sql = "SELECT  Tag,EmpTo FROM ND" + Integer.parseInt(nd.getFK_Flow()) + "Track A WHERE A.NDFrom=" + this.getFK_Node() + " AND A.NDTo=" + toNodeID + " AND ActionType=1 ORDER BY WorkID  DESC limit 1,1 ";
				}
				else if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
				{
					sql = "SELECT  Tag,EmpTo FROM ND" + Integer.parseInt(nd.getFK_Flow()) + "Track A WHERE A.NDFrom=" + this.getFK_Node() + " AND A.NDTo=" + toNodeID + " AND ActionType=1 ORDER BY WorkID  DESC limit 1 ";
				}

				DataTable mydt = DBAccess.RunSQLReturnTable(sql);
				String emps = "";
				if (mydt.Rows.size() != 0)
				{
					emps = mydt.Rows.get(0).getValue("Tag").toString();
					if (emps.equals("") || emps == null)
					{
						emps = mydt.Rows.get(0).getValue("EmpTo").toString();
						emps = emps + "," + emps;
					}
				}

				String[] strs = emps.split("[;]", -1);
				for (String str : strs)
				{
					if (DataType.IsNullOrEmpty(str) == true)
					{
						continue;
					}

					String[] emp = str.split("[,]", -1);
					if (emp.length != 2)
					{
						continue;
					}

					DataRow dr = dt.NewRow();
					dr.setValue(0, emp[0]);
					dt.Rows.add(dr);
				}
			}



		}

		//澧炲姞涓�涓猼able.
		ds.Tables.add(dt);

			/// 璁＄畻涓婁竴娆￠�夋嫨鐨勭粨鏋�, 骞舵妸缁撴灉杩斿洖杩囧幓.


		//杩斿洖json.
		return bp.tools.Json.ToJson(ds);
	}

	public final String AccepterOfDept_Init() throws Exception
	{
		//褰撳墠鑺傜偣ID.
		Node nd = new Node(this.getFK_Node());

		//鍒ゆ柇鏄惁鏈変笉鍙戦�佹爣璁帮紵
		if (this.GetRequestValBoolen("IsSend") == true)
		{
			SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(this.getFK_Flow(), this.getWorkID());
			return "info@" + objs.ToMsgOfHtml();
		}

		int toNodeID = this.GetRequestValInt("ToNode");
		if (toNodeID == 0)
		{
			Nodes nds = nd.getHisToNodes();
			if (nds.size() == 1)
			{
				toNodeID = nds.get(0).GetValIntByKey("NodeID");
			}
			else
			{
				return "err@鍙傛暟閿欒,蹇呴』浼犻�掓潵鍒拌揪鐨勮妭鐐笽D ToNode .";
			}
		}

		Work wk = nd.getHisWork();
		wk.setOID(this.getWorkID());
		wk.Retrieve();

		DataSet ds = new DataSet();
		Node toNode = new Node(toNodeID);
		//鑾峰彇閮ㄩ棬淇℃伅
		String sql = toNode.getDeliveryParas();
		if(DataType.IsNullOrEmpty(sql) == true)
			throw new RuntimeException("err@璇疯缃煡璇㈤儴闂ㄧ殑SQL");
		sql = sql.replace("@WebUser.FK_Dept",WebUser.getFK_Dept());
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Depts";
		ds.Tables.add(dt);



		Selector select = new Selector(toNodeID);

		ds.Tables.add(select.ToDataTableField("Selector"));

		///璁＄畻涓婁竴娆￠�夋嫨鐨勭粨鏋�, 骞舵妸缁撴灉杩斿洖杩囧幓.
		sql = "";
		dt = new DataTable();
		dt.Columns.Add("No", String.class);
		dt.TableName = "Selected";
		if (select.getIsAutoLoadEmps() == true)
		{
			//鑾峰彇褰撳墠鑺傜偣鐨凷electAccper鐨勫�� @sly
			SelectAccpers selectAccpers = new SelectAccpers();
			selectAccpers.Retrieve(SelectAccperAttr.WorkID, this.getWorkID(), SelectAccperAttr.FK_Node, toNodeID);
			if (selectAccpers.size() != 0)
			{
				for (SelectAccper sa : selectAccpers.ToJavaList())
				{
					DataRow dr = dt.NewRow();
					dr.setValue(0, sa.getFK_Emp());
					dt.Rows.add(dr);
				}
			}
			else
			{
				if (SystemConfig.getAppCenterDBType() == DBType.MSSQL)
				{
					sql = "SELECT  top 1 Tag,EmpTo FROM ND" + Integer.parseInt(nd.getFK_Flow()) + "Track A WHERE A.NDFrom=" + this.getFK_Node() + " AND A.NDTo=" + toNodeID + " AND ActionType=1 ORDER BY WorkID DESC";
				}
				else if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
				{
					sql = "SELECT * FROM (SELECT  Tag,EmpTo,WorkID FROM ND" + Integer.parseInt(nd.getFK_Flow()) + "Track A WHERE A.NDFrom=" + this.getFK_Node() + " AND A.NDTo=" + toNodeID + " AND ActionType=1 ORDER BY WorkID DESC ) WHERE ROWNUM =1";
				}
				else if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
				{
					sql = "SELECT  Tag,EmpTo FROM ND" + Integer.parseInt(nd.getFK_Flow()) + "Track A WHERE A.NDFrom=" + this.getFK_Node() + " AND A.NDTo=" + toNodeID + " AND ActionType=1 ORDER BY WorkID  DESC limit 1,1 ";
				}
				else if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
				{
					sql = "SELECT  Tag,EmpTo FROM ND" + Integer.parseInt(nd.getFK_Flow()) + "Track A WHERE A.NDFrom=" + this.getFK_Node() + " AND A.NDTo=" + toNodeID + " AND ActionType=1 ORDER BY WorkID  DESC limit 1 ";
				}

				DataTable mydt = DBAccess.RunSQLReturnTable(sql);
				String emps = "";
				if (mydt.Rows.size() != 0)
				{
					emps = mydt.Rows.get(0).getValue("Tag").toString();
					if (emps.equals("") || emps == null)
					{
						emps = mydt.Rows.get(0).getValue("EmpTo").toString();
						emps = emps + "," + emps;
					}
				}

				String[] strs = emps.split("[;]", -1);
				for (String str : strs)
				{
					if (DataType.IsNullOrEmpty(str) == true)
					{
						continue;
					}

					String[] emp = str.split("[,]", -1);
					if (emp.length != 2)
					{
						continue;
					}

					DataRow dr = dt.NewRow();
					dr.setValue(0, emp[0]);
					dt.Rows.add(dr);
				}
			}

		}

		//澧炲姞涓�涓猼able.
		ds.Tables.add(dt);

		//杩斿洖json.
		return bp.tools.Json.ToJson(ds);
	}

	/**
	 * 淇濆瓨.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String Accepter_Save() throws Exception {
		try {
			// 姹傚埌杈剧殑鑺傜偣.
			int toNodeID = 0;
			if (!this.GetRequestVal("ToNode").equals("0")) {
				toNodeID = Integer.parseInt(this.GetRequestVal("ToNode"));
			}

			if (toNodeID == 0) { // 娌℃湁灏辫幏寰楃涓�涓妭鐐�.
				Node nd = new Node(this.getFK_Node());
				Nodes nds = nd.getHisToNodes();
				toNodeID = nds.get(0).GetValIntByKey("NodeID");
			}

			// 姹傚彂閫佸埌鐨勪汉鍛�.
			// string selectEmps = this.GetValFromFrmByKey("SelectEmps");
			String selectEmps = this.GetRequestVal("SelectEmps");
			selectEmps = selectEmps.replace(";", ",");

			// 淇濆瓨鎺ュ彈浜�.
			bp.wf.Dev2Interface.Node_AddNextStepAccepters(this.getWorkID(), toNodeID, selectEmps);
			return "SaveOK@" + selectEmps;
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}

	/**
	 * 鎵ц淇濆瓨骞跺彂閫�.
	 * 
	 * @return 杩斿洖鍙戦�佺殑缁撴灉.
	 * @throws Exception 
	 */
	public final String Accepter_Send() throws Exception {
		try {
			// 姹傚埌杈剧殑鑺傜偣.
			int toNodeID = 0;
			if (!this.GetRequestVal("ToNode").equals("0")) {
				toNodeID = Integer.parseInt(this.GetRequestVal("ToNode"));
			}

			if (toNodeID == 0) { // 娌℃湁灏辫幏寰楃涓�涓妭鐐�.
				Node nd = new Node(this.getFK_Node());
				Nodes nds = nd.getHisToNodes();
				toNodeID = nds.get(0).GetValIntByKey("NodeID");
			}

			// 姹傚彂閫佸埌鐨勪汉鍛�.
			// string selectEmps = this.GetValFromFrmByKey("SelectEmps");
			String selectEmps = this.GetRequestVal("SelectEmps");
			selectEmps = selectEmps.replace(";", ",");

			// 鎵ц鍙戦��.
			SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(this.getFK_Flow(), this.getWorkID(), toNodeID,
					selectEmps);
			return objs.ToMsgOfHtml();
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}

	/**
	 * 鎵ц淇濆瓨骞跺彂閫�.
	 *
	 * @return 杩斿洖鍙戦�佺殑缁撴灉.
	 * @throws Exception
	 */
	public final String AccepterOfDept_Send() throws Exception {
		try {
			// 姹傚埌杈剧殑鑺傜偣.
			int toNodeID = 0;
			if (!this.GetRequestVal("ToNode").equals("0")) {
				toNodeID = Integer.parseInt(this.GetRequestVal("ToNode"));
			}

			if (toNodeID == 0) { // 娌℃湁灏辫幏寰楃涓�涓妭鐐�.
				Node nd = new Node(this.getFK_Node());
				Nodes nds = nd.getHisToNodes();
				toNodeID = nds.get(0).GetValIntByKey("NodeID");
			}
			Selector nd = new Selector(toNodeID);
			//鑾峰彇璁剧疆鐨勪汉鍛樺瓧娈�
			String empField = nd.getSelectorP1();
			// 姹傚彂閫佸埌鐨勪汉鍛�.
			String selectDepts = this.GetRequestVal("SelectDepts");
			selectDepts = selectDepts.replace(";", ",");
			selectDepts = "'"+selectDepts.replace(",","','")+"'";
			//鏍规嵁閮ㄩ棬鑾峰彇閫夋嫨鐨勪汉鍛�
			String sql="SELECT "+empField +" FROM Port_Dept WHERE No IN ("+selectDepts+")";
			DataTable  dt=DBAccess.RunSQLReturnTable(sql);
			if(dt.Rows.size()==0)
				throw new RuntimeException("err@閰嶇疆鐨勯儴闂ㄦ病鏈夎幏鍙栫浉鍏崇殑鎺ュ彈浜哄憳");
			String selectEmps="";
			for(DataRow dr : dt.Rows){
				selectEmps+=dr.getValue(0)+",";
			}
			// 鎵ц鍙戦��.
			SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(this.getFK_Flow(), this.getWorkID(), toNodeID,
					selectEmps);
			return objs.ToMsgOfHtml();
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}

	///

	/// 鍥炴粴.
	/** 
	 鍥炴粴鎿嶄綔.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Rollback_Init() throws Exception
	{
		String andsql = " ";
		andsql += "  ActionType=" + ActionType.Start.getValue();
		andsql += " OR ActionType=" + ActionType.TeampUp.getValue();
		andsql += " OR ActionType=" + ActionType.Forward.getValue();
		andsql += " OR ActionType=" + ActionType.HuiQian.getValue();

		String sql = "SELECT RDT,NDFrom, NDFromT,EmpFrom,EmpFromT  FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track WHERE WorkID=" + this.getWorkID() + " AND(" + andsql + ") Order By RDT DESC";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			dt.Columns.get(0).setColumnName("RDT");
			dt.Columns.get(1).setColumnName("NDFrom");
			dt.Columns.get(2).setColumnName("NDFromT");
			dt.Columns.get(3).setColumnName("EmpFrom");
			dt.Columns.get(4).setColumnName("EmpFromT");
		}


		return bp.tools.Json.ToJson(dt);
	}

	/**
	 * 鎵ц鍥炴粴鎿嶄綔
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String Rollback_Done() throws Exception {
		FlowExt flow = new FlowExt(this.getFK_Flow());
		return flow.DoRebackFlowData(this.getWorkID(), this.getFK_Node(), this.GetRequestVal("Msg"));
	}

	/// 鍥炴粴.

	/// 宸ヤ綔閫�鍥�.
	/** 
	 鑾峰緱鍙互閫�鍥炵殑鑺傜偣.
	 
	 @return 閫�鍥炰俊鎭�
	 * @throws Exception 
	*/
	public final String Return_Init() throws Exception
	{
		try
		{
			DataTable dt = bp.wf.Dev2Interface.DB_GenerWillReturnNodes(this.getFK_Node(), this.getWorkID(), this.getFID());

			//澶囨敞:鍚姩瀛愭祦绋嬬殑鎴栬�呭钩绾у瓙娴佺▼鐨勮妭鐐逛篃鍙互閫�鍥烇紝閫�鍥炲悗鏄惁缁撴潫瀛愭祦绋嬮渶瑕佸湪FEE浜嬩欢涓鐞� - yln淇敼
			//鏍规嵁WorkID鏌ヨ鏄惁鏈夊惎鍔ㄧ殑瀛愭祦绋� 
			// GenerWorkFlows gwfs = new GenerWorkFlows();
			//int count = gwfs.Retrieve(GenerWorkFlowAttr.PWorkID, this.WorkID);
			//if (count != 0)
			//    return "info@璇ユ祦绋嬪凡缁忓惎鍔ㄥ瓙娴佺▼锛屼笉鑳芥墽琛岄��鍥�";

			//璇ユ祦绋嬩负瀛愭祦绋嬶紝鍚姩浜嗗钩绾у瓙娴佺▼
			//GenerWorkFlow gwf = new GenerWorkFlow(this.WorkID);
			//if (gwf.PWorkID != 0) 
			//{
			//    //瀛樺湪骞崇骇瀛愭祦绋�
			//     gwfs = new GenerWorkFlows();
			//     count = gwfs.Retrieve(GenerWorkFlowAttr.PWorkID, gwf.PWorkID);
			//     SubFlows subFlows = new SubFlows(); 
			//     int subFlowCount = subFlows.Retrieve(SubFlowYanXuAttr.FK_Node, this.FK_Node, SubFlowYanXuAttr.SubFlowModel,1);
			//    if (subFlowCount != 0)//鍚湁骞崇骇瀛愭祦绋�
			//    {
			//        foreach(SubFlow subFlow in subFlows)
			//        {
			//            //鏍规嵁FlowNo鑾峰彇鏈夋病鏈夊彂璧锋祦绋�
			//            var subGwf = gwfs.GetEntityByKey(GenerWorkFlowAttr.FK_Flow,subFlow.SubFlowNo);
			//            if(subGwf!=null)
			//                return "info@璇ユ祦绋嬪凡缁忓惎鍔ㄥ钩绾у瓙娴佺▼锛屼笉鑳芥墽琛岄��鍥�";
			//        }
			//    } 
			//}


			//濡傛灉鍙湁涓�涓��鍥炶妭鐐癸紝灏遍渶瑕佸垽鏂槸鍚﹀惎鐢ㄤ簡鍗曡妭鐐归��鍥炶鍒�.

			if (dt.Rows.size() == 1)
			{
				Node nd = new Node(this.getFK_Node());
				if (nd.getReturnOneNodeRole() != 0)
				{
					/* 濡傛灉:鍚敤浜嗗崟鑺傜偣閫�鍥炶鍒�.
					 */
					String returnMsg = "";
					if (nd.getReturnOneNodeRole() == 1 && DataType.IsNullOrEmpty(nd.getReturnField()) == false)
					{
						/*浠庤〃鍗曞瓧娈甸噷鍙栨剰瑙�.*/
						Flow fl = new Flow(nd.getFK_Flow());
						String sql = "SELECT " + nd.getReturnField() + " FROM " + fl.getPTable() + " WHERE OID=" + this.getWorkID();
						returnMsg = DBAccess.RunSQLReturnStringIsNull(sql, "鏈～鍐欐剰瑙�");
					}

					if (nd.getReturnOneNodeRole() == 2)
					{
						/*浠庡鏍哥粍浠堕噷鍙栨剰瑙�.*/
						String sql = "SELECT Msg FROM ND" + Integer.parseInt(nd.getFK_Flow()) + "Track WHERE WorkID=" + this.getWorkID() + " AND NDFrom=" + this.getFK_Node() + " AND EmpFrom='" + WebUser.getNo() + "' AND ActionType=" + ActionType.WorkCheck.getValue();
						returnMsg = DBAccess.RunSQLReturnStringIsNull(sql, "鏈～鍐欐剰瑙�");
					}

					int toNodeID = Integer.parseInt(dt.Rows.get(0).getValue(0).toString());

					String info = bp.wf.Dev2Interface.Node_ReturnWork(this.getFK_Flow(), this.getWorkID(), 0, this.getFK_Node(), toNodeID, returnMsg, false);
					return "info@" + info;
				}
			}
			return bp.tools.Json.ToJson(dt);
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}

	/**
	 * 鎵ц閫�鍥�,杩斿洖閫�鍥炰俊鎭�.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String DoReturnWork() throws Exception {
		String[] vals = this.GetRequestVal("ReturnToNode").split("[@]", -1);
		int toNodeID = Integer.parseInt(vals[0]);

		String toEmp = vals[1];
		String reMesage = this.GetRequestVal("ReturnInfo");

		boolean isBackBoolen = false;
		String isBack = this.GetRequestVal("IsBack");
		if (isBack.equals("1")) {
			isBackBoolen = true;
		}

		String pageData = this.GetRequestVal("PageData");

		return bp.wf.Dev2Interface.Node_ReturnWork(this.getFK_Flow(), this.getWorkID(), this.getFID(),
				this.getFK_Node(), toNodeID, toEmp, reMesage, isBackBoolen, pageData);
	}

	///

	/**
	 * 鎵ц绉讳氦.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String Shift_Save() throws Exception {
		String msg = this.GetRequestVal("Message");
		String toEmp = this.GetRequestVal("ToEmp");
		return bp.wf.Dev2Interface.Node_Shift(this.getWorkID(), toEmp, msg);
	}

	/**
	 * 鎾ら攢绉讳氦
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String UnShift() throws Exception {
		return bp.wf.Dev2Interface.Node_ShiftUn(this.getWorkID());
	}

	/**
	 * 鎵ц鍌姙
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String Press() throws Exception {
		String msg = this.GetRequestVal("Msg");
		// 璋冪敤API.
		return bp.wf.Dev2Interface.Flow_DoPress(this.getWorkID(), msg, true);
	}

	/// 娴佺▼鏁版嵁妯＄増. for 娴欏晢閾惰 by zhoupeng.
	/** 
	 娴佺▼鏁版嵁妯＄増
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DBTemplate_Init() throws Exception
	{
		DataSet ds = new DataSet();

		//鑾峰彇妯＄増.
		Paras ps = new Paras();
		ps.SQL="SELECT WorkID,Title,AtPara FROM WF_GenerWorkFlow WHERE FK_Flow=" + SystemConfig.getAppCenterDBVarStr() + "FK_Flow AND WFState=3 AND Starter=" + SystemConfig.getAppCenterDBVarStr() + "Starter AND ATPARA LIKE '%@DBTemplate=1%'";
		ps.Add("FK_Flow", this.getFK_Flow());
		ps.Add("Starter", WebUser.getNo());
		DataTable dtTemplate = DBAccess.RunSQLReturnTable(ps);
		dtTemplate.TableName = "DBTemplate";
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dtTemplate.Columns.get(0).setColumnName("WorkID");
			dtTemplate.Columns.get(1).setColumnName("Title");
		}

		//鎶婃ā鐗堝悕绉版浛鎹� title. 
		for (DataRow dr : dtTemplate.Rows)
		{
			String str = dr.getValue(2).toString();
			bp.da.AtPara ap = new AtPara(str);
			dr.setValue("Title", ap.GetValStrByKey("DBTemplateName"));
		}

		ds.Tables.add(dtTemplate);

		// 鑾峰彇鍘嗗彶鍙戣捣鏁版嵁.
		ps = new Paras();
		if (SystemConfig.getAppCenterDBType() == DBType.MSSQL)
		{
			ps.SQL="SELECT TOP 30 WorkID,Title FROM WF_GenerWorkFlow WHERE FK_Flow=" + SystemConfig.getAppCenterDBVarStr() + "FK_Flow AND WFState=3 AND Starter=" + SystemConfig.getAppCenterDBVarStr() + "Starter AND ATPARA NOT LIKE '%@DBTemplate=1%' ORDER BY RDT ";
			ps.Add("FK_Flow", this.getFK_Flow());
			ps.Add("Starter", WebUser.getNo());
		}
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			ps.SQL="SELECT WorkID,Title FROM WF_GenerWorkFlow WHERE FK_Flow=" + SystemConfig.getAppCenterDBVarStr() + "FK_Flow AND WFState=3 AND Starter=" + SystemConfig.getAppCenterDBVarStr() + "Starter AND ATPARA NOT LIKE '%@DBTemplate=1%' AND rownum<=30 ORDER BY RDT ";
			ps.Add("FK_Flow", this.getFK_Flow());
			ps.Add("Starter", WebUser.getNo());
		}
		if (SystemConfig.getAppCenterDBType() == DBType.MySQL || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			ps.SQL="SELECT WorkID,Title FROM WF_GenerWorkFlow WHERE FK_Flow=" + SystemConfig.getAppCenterDBVarStr() + "FK_Flow AND WFState=3 AND Starter=" + SystemConfig.getAppCenterDBVarStr() + "Starter AND ATPARA NOT LIKE '%@DBTemplate=1%' ORDER BY RDT LIMIT 30";
			ps.Add("FK_Flow", this.getFK_Flow());
			ps.Add("Starter", WebUser.getNo());
		}
		DataTable dtHistroy = DBAccess.RunSQLReturnTable(ps);
		dtHistroy.TableName = "History";
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dtHistroy.Columns.get(0).setColumnName("WorkID");
			dtHistroy.Columns.get(1).setColumnName("Title");
		}
		ds.Tables.add(dtHistroy);

		//杞寲涓� json.
		return bp.tools.Json.ToJson(ds);
	}

	public final String DBTemplate_SaveAsDBTemplate() throws UnsupportedEncodingException, Exception {
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		gwf.setParasDBTemplate(true);
		gwf.setParasDBTemplateName(URLDecoder.decode(this.GetRequestVal("Title"), "UTF-8")); // this.GetRequestVal("Title");
		gwf.Update();
		return "璁剧疆鎴愬姛";
	}

	public final String DBTemplate_DeleteDBTemplate() throws Exception {
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		gwf.setParasDBTemplate(false);
		gwf.Update();

		return "璁剧疆鎴愬姛";
	}

	public final String DBTemplate_StartFlowAsWorkID() {
		return "璁剧疆鎴愬姛";
	}

	/// 娴佺▼鏁版嵁妯＄増.

	/// tonodes
	/**
	 * 鍒濆鍖�.
	 * 
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public final String ToNodes_Init() throws NumberFormatException, Exception {
		// 鑾峰彇鍒颁笅涓�涓妭鐐圭殑鑺傜偣Nodes

		// 鑾峰緱褰撳墠鑺傜偣鍒拌揪鐨勮妭鐐�.
		Nodes nds = new Nodes();
		String toNodes = this.GetRequestVal("ToNodes");
		if (DataType.IsNullOrEmpty(toNodes) == false) {
			/* 瑙ｅ喅璺宠浆闂. */
			String[] mytoNodes = toNodes.split("[,]", -1);
			for (String str : mytoNodes) {
				if (DataType.IsNullOrEmpty(str) == true) {
					continue;
				}
				nds.AddEntity(new Node(Integer.parseInt(str)));
			}
		} else {
			nds = bp.wf.Dev2Interface.WorkOpt_GetToNodes(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(),
					this.getFID());
		}

		// 鑾峰緱涓婃榛樿閫夋嫨鐨勮妭鐐�
		int lastSelectNodeID = bp.wf.Dev2Interface.WorkOpt_ToNodes_GetLasterSelectNodeID(this.getFK_Flow(),
				this.getFK_Node());
		if (lastSelectNodeID == 0 && nds.size() != 0) {
			lastSelectNodeID = Integer.parseInt(nds.get(0).getPKVal().toString());
		}

		DataSet ds = new DataSet();
		ds.Tables.add(nds.ToDataTableField("Nodes"));
		DataTable dt = new DataTable("SelectNode");
		dt.Columns.Add("NodeID");
		DataRow dr = dt.NewRow();
		dr.setValue("NodeID", lastSelectNodeID);
		dt.Rows.add(dr);
		ds.Tables.add(dt);
		return bp.tools.Json.ToJson(ds);
	}

	/**
	 * 鍙戦��
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String ToNodes_Send() throws Exception {
		String toNodes = this.GetRequestVal("ToNodes");
		// 鎵ц鍙戦��.
		String msg = "";
		Node nd = new Node(this.getFK_Node());
		Work wk = nd.getHisWork();
		wk.setOID(this.getWorkID());
		wk.Retrieve();

		try {
			String toNodeStr = Integer.parseInt(getFK_Flow()) + "01";
			// 濡傛灉涓哄紑濮嬭妭鐐�
			if (toNodes.equals(toNodeStr)) {
				// 鎶婂弬鏁版洿鏂板埌鏁版嵁搴撻噷闈�.
				GenerWorkFlow gwf = new GenerWorkFlow();
				gwf.setWorkID(this.getWorkID());
				gwf.RetrieveFromDBSources();
				gwf.setParasToNodes(toNodes);
				gwf.Save();

				WorkNode firstwn = new WorkNode(wk, nd);

				Node toNode = new Node(toNodeStr);
				msg = firstwn.NodeSend(toNode, gwf.getStarter()).ToMsgOfHtml();
			} else {
				msg = bp.wf.Dev2Interface.WorkOpt_SendToNodes(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(),
						this.getFID(), toNodes).ToMsgOfHtml();
			}
		} catch (RuntimeException ex) {

			return ex.getMessage();
		}

		GenerWorkFlow gwfw = new GenerWorkFlow();
		gwfw.setWorkID(this.getWorkID());
		gwfw.RetrieveFromDBSources();
		if (nd.getIsRememberMe() == true) {
			gwfw.setParasToNodes(toNodes);
		} else {
			gwfw.setParasToNodes("");
		}
		gwfw.Save();

		// 褰撳墠鑺傜偣.
		Node currNode = new Node(this.getFK_Node());
		Flow currFlow = new Flow(this.getFK_Flow());

		/// 澶勭悊鍙戦�佸悗杞悜.
		try {
			/* 澶勭悊杞悜闂. */
			switch (currNode.getHisTurnToDeal()) {
			case SpecUrl:
				String myurl = currNode.getTurnToDealDoc();
				if (myurl.contains("?") == false) {
					myurl += "?1=1";
				}
				Attrs myattrs = currNode.getHisWork().getEnMap().getAttrs();
				Work hisWK = currNode.getHisWork();
				for (Attr attr : myattrs) {
					if (myurl.contains("@") == false) {
						break;
					}
					myurl = myurl.replace("@" + attr.getKey(), hisWK.GetValStrByKey(attr.getKey()));
				}
				myurl = myurl.replace("@WebUser.No", WebUser.getNo());
				myurl = myurl.replace("@WebUser.Name", WebUser.getName());
				myurl = myurl.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());

				if (myurl.contains("@")) {
					bp.wf.Dev2Interface.Port_SendMsg("admin", currFlow.getName() + "鍦�" + currNode.getName() + "鑺傜偣澶勶紝鍑虹幇閿欒",
							"娴佺▼璁捐閿欒锛屽湪鑺傜偣杞悜url涓弬鏁版病鏈夎鏇挎崲涓嬫潵銆俇rl:" + myurl, "Err" + currNode.getNo() + "_" + this.getWorkID(),
							SMSMsgType.Err, this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), this.getFID());
					throw new RuntimeException("娴佺▼璁捐閿欒锛屽湪鑺傜偣杞悜url涓弬鏁版病鏈夎鏇挎崲涓嬫潵銆俇rl:" + myurl);
				}

				if (myurl.contains("PWorkID") == false) {
					myurl += "&PWorkID=" + this.getWorkID();
				}

				myurl += "&FromFlow=" + this.getFK_Flow() + "&FromNode=" + this.getFK_Node() + "&UserNo="
						+ WebUser.getNo() + "&SID=" + WebUser.getSID();
				return "TurnUrl@" + myurl;
			case TurnToByCond:

				return msg;
			default:
				msg = msg.replace("@WebUser.No", WebUser.getNo());
				msg = msg.replace("@WebUser.Name", WebUser.getName());
				msg = msg.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
				return msg;
			}

			///

		} catch (RuntimeException ex) {
			if (ex.getMessage().contains("璇烽�夋嫨涓嬩竴姝ラ宸ヤ綔") == true || ex.getMessage().contains("鐢ㄦ埛娌℃湁閫夋嫨鍙戦�佸埌鐨勮妭鐐�") == true) {
				if (currNode.getCondModel() == DirCondModel.ByLineCond) {
					return "url@./WorkOpt/ToNodes.htm?FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getFK_Node()
							+ "&WorkID=" + this.getWorkID() + "&FID=" + this.getFID();
				}

				return "err@涓嬩竴涓妭鐐圭殑鎺ユ敹浜鸿鍒欐槸锛屽綋鍓嶈妭鐐归�夋嫨鏉ラ�夋嫨锛屽湪褰撳墠鑺傜偣灞炴�ч噷鎮ㄦ病鏈夊惎鍔ㄦ帴鍙椾汉鎸夐挳锛岀郴缁熻嚜鍔ㄥ府鍔╂偍鍚姩浜嗭紝璇峰叧闂獥鍙ｉ噸鏂版墦寮�銆�" + ex.getMessage();
			}

			GenerWorkFlow HisGenerWorkFlow = new GenerWorkFlow(this.getWorkID());
			// 闃叉鍙戦�佸け璐ヤ涪澶辨帴鍙椾汉锛屽鑷翠笉鑳藉嚭鐜颁笅鎷夋柟鍚戦�夋嫨妗�. @鏉�.
			if (HisGenerWorkFlow != null) {
				// 濡傛灉鏄細绛剧姸鎬�.
				if (HisGenerWorkFlow.getHuiQianTaskSta() == HuiQianTaskSta.HuiQianing) {
					// 濡傛灉鏄富鎸佷汉.
					if (HisGenerWorkFlow.getHuiQianZhuChiRen().equals(WebUser.getNo())) {
						String empStrSepc = WebUser.getNo() + "," + WebUser.getName() + ";";
						if (HisGenerWorkFlow.getTodoEmps().contains(empStrSepc) == false) {
							HisGenerWorkFlow.setTodoEmps(HisGenerWorkFlow.getTodoEmps() + empStrSepc);
							HisGenerWorkFlow.Update();
						}
					} else {
						// 闈炰富鎸佷汉.
						String empStrSepc = WebUser.getNo() + "," + WebUser.getName() + ";";
						if (HisGenerWorkFlow.getTodoEmps().contains(empStrSepc) == false) {
							HisGenerWorkFlow.setTodoEmps(HisGenerWorkFlow.getTodoEmps() + empStrSepc);
							HisGenerWorkFlow.Update();
						}
					}
				}

				if (HisGenerWorkFlow.getHuiQianTaskSta() != HuiQianTaskSta.HuiQianing) {
					if (HisGenerWorkFlow.getTodoEmps().contains(WebUser.getNo() + ",") == false) {
						HisGenerWorkFlow.setTodoEmps(
								HisGenerWorkFlow.getTodoEmps() + WebUser.getNo() + "," + WebUser.getName() + ";");
						HisGenerWorkFlow.Update();
					}
				}
			}
			return ex.getMessage();

		}
	}

	/// tonodes

	/// 鑷畾涔�.
	/**
	 * 鍒濆鍖�
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String TransferCustom_Init() throws Exception {
		DataSet ds = new DataSet();

		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		if (gwf.getTransferCustomType() != TransferCustomType.ByWorkerSet) {
			gwf.setTransferCustomType(TransferCustomType.ByWorkerSet);
			gwf.Update();
		}

		ds.Tables.add(gwf.ToDataTableField("WF_GenerWorkFlow"));

		// 褰撳墠杩愯鍒扮殑鑺傜偣
		Node currNode = new Node(gwf.getFK_Node());

		// 鎵�鏈夌殑鑺傜偣s.
		Nodes nds = new Nodes(this.getFK_Flow());
		// ds.Tables.add(nds.ToDataTableField("WF_Node"));

		// 宸ヤ綔浜哄憳鍒楄〃.宸茬粡璧板畬鐨勮妭鐐逛笌浜哄憳.
		GenerWorkerLists gwls = new GenerWorkerLists(this.getWorkID());
		GenerWorkerList gwln = (GenerWorkerList) gwls.GetEntityByKey(GenerWorkerListAttr.FK_Node, this.getFK_Node());
		if (gwln == null) {
			gwln = new GenerWorkerList();
			gwln.setFK_Node(currNode.getNodeID());
			gwln.setFK_NodeText(currNode.getName());
			gwln.setFK_Emp(WebUser.getNo());
			gwln.setFK_EmpText(WebUser.getName());
			gwls.AddEntity(gwln);
		}
		ds.Tables.add(gwls.ToDataTableField("WF_GenerWorkerList"));

		// 璁剧疆鐨勬墜宸ヨ繍琛岀殑娴佽浆淇℃伅.
		TransferCustoms tcs = new TransferCustoms(this.getWorkID());
		if (tcs.size() == 0) {

			/// 鎵ц璁＄畻鏈潵澶勭悊浜�.

			Work wk = currNode.getHisWork();
			wk.setOID(this.getWorkID());
			wk.Retrieve();
			WorkNode wn = new WorkNode(wk, currNode);
			wn.getHisFlow().setIsFullSA(true);

			// 鎵ц璁＄畻鏈潵澶勭悊浜�.
			FullSA fsa = new FullSA(wn);

			/// 鎵ц璁＄畻鏈潵澶勭悊浜�.

			for (Node nd : nds.ToJavaList()) {
				if (nd.getNodeID() == this.getFK_Node()) {
					continue;
				}
				if (nd.GetParaBoolen(NodeAttr.IsYouLiTai) == false) {
					continue;
				}
			
				GenerWorkFlow gwl = (GenerWorkFlow) gwls.GetEntityByKey(GenerWorkerListAttr.FK_Node, nd.getNodeID());
				if (gwl == null) {

					/* 璇存槑娌℃湁 */
					TransferCustom tc = new TransferCustom();
					tc.setWorkID(this.getWorkID());
					tc.setFK_Node(nd.getNodeID());
					tc.setNodeName(nd.getName());

					/// 璁＄畻鍑烘潵褰撳墠鑺傜偣鐨勫伐浣滀汉鍛�.
					SelectAccpers sas = new SelectAccpers();
					sas.Retrieve(SelectAccperAttr.WorkID, this.getWorkID(), SelectAccperAttr.FK_Node, nd.getNodeID());

					String workerID = "";
					String workerName = "";
					for (SelectAccper sa : sas.ToJavaList()) {
						workerID += sa.getFK_Emp() + ",";
						workerName += sa.getEmpName() + ",";
					}

					/// 璁＄畻鍑烘潵褰撳墠鑺傜偣鐨勫伐浣滀汉鍛�.

					tc.setWorker(workerID);
					tc.setWorkerName(workerName);
					tc.setIdx(nd.getStep());
					tc.setIsEnable(true);
					if (nd.getHisCHWay() == CHWay.ByTime && nd.GetParaInt("CHWayOfTimeRole") == 2) {
						tc.setPlanDT( DateUtils.format(DateUtils.addDay(new Date(), 1),DataType.getSysDataTimeFormat()));
					}
					tc.Insert();
				}
			}
			tcs = new TransferCustoms(this.getWorkID());
		}

		ds.Tables.add(tcs.ToDataTableField("WF_TransferCustoms"));

		return bp.tools.Json.ToJson(ds);
	}

	/// 鑷畾涔�.

	/// 鏃堕檺鍒濆鍖栨暟鎹�
	public final String CH_Init() throws Exception
	{
		DataSet ds = new DataSet();

		//鑾峰彇澶勭悊淇℃伅鐨勫垪琛�
		GenerWorkerLists gwls = new GenerWorkerLists();
		gwls.Retrieve(GenerWorkerListAttr.FK_Flow, this.getFK_Flow(), GenerWorkerListAttr.WorkID, this.getWorkID(), GenerWorkerListAttr.RDT);
		DataTable dt = gwls.ToDataTableField("WF_GenerWorkerList");
		ds.Tables.add(dt);

		//鑾峰彇娴佺▼淇℃伅
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		ds.Tables.add(gwf.ToDataTableField("WF_GenerWorkFlow"));

		Flow flow = new Flow(this.getFK_Flow());
		ds.Tables.add(flow.ToDataTableField("WF_Flow"));

		//鑾峰彇娴佺▼娴佽浆鑷畾涔夌殑鏁版嵁
		String sql = "SELECT FK_Node AS NodeID,NodeName AS Name From WF_TransferCustom WHERE WorkID=" + getWorkID() + " AND IsEnable=1 Order By Idx";
		DataTable dtYL = DBAccess.RunSQLReturnTable(sql);

		//鍒犻櫎涓嶅惎鐢ㄧ殑娓哥鎬佽妭鐐规椂闄愯缃�
		sql = "DELETE FROM WF_CHNode WHERE WorkID=" + getWorkID() + " AND FK_Node IN(SELECT FK_Node FROM WF_TransferCustom WHERE WorkID=" + getWorkID() + " AND IsEnable=0 )";
		DBAccess.RunSQL(sql);

		//鑺傜偣鏃堕檺琛�
		CHNodes chNodes = new CHNodes(this.getWorkID());




			///鑾峰彇娴佺▼鑺傜偣淇℃伅鐨勫垪琛�
		Nodes nds = new Nodes(this.getFK_Flow());
		//濡傛灉鏄父绂绘�佺殑鑺傜偣鏈夊彲鑳借皟鏁撮『搴�
		dt = new DataTable();
		dt.TableName = "WF_Node";
		dt.Columns.Add("NodeID");
		dt.Columns.Add("Name");
		dt.Columns.Add("SDTOfNode"); //鑺傜偣搴斿畬鎴愭椂闂�
		dt.Columns.Add("PlantStartDt"); //鑺傜偣璁″垝寮�濮嬫椂闂�
		dt.Columns.Add("GS"); //宸ユ椂

		DataRow dr;
		boolean isFirstY = true;
		//涓婁竴涓妭鐐圭殑鏃堕棿
		String beforeSDTOfNode = "";
		//鍏堟帓搴忚繍琛岃繃鐨勮妭鐐�
		CHNode chNode = null;
		for (GenerWorkerList gwl : gwls.ToJavaList())
		{
			Object tempVar = chNodes.GetEntityByKey(CHNodeAttr.FK_Node, gwl.getFK_Node());
			chNode = tempVar instanceof CHNode ? (CHNode)tempVar : null;
			if (chNode != null)
			{
				continue;
			}
			chNode = new CHNode();
			chNode.setWorkID(this.getWorkID());
			chNode.setFK_Node(gwl.getFK_Node());
			chNode.setNodeName(gwl.getFK_NodeText());
			chNode.setStartDT(gwl.getRDT());
			chNode.setEndDT(gwl.getCDT());
			chNode.setFK_Emp(gwl.getFK_Emp());
			chNode.setFK_EmpT(gwl.getFK_EmpText());
			chNode.SetPara("RDT", gwl.getRDT());
			chNode.SetPara("CDT", gwl.getCDT());
			chNode.SetPara("IsPass", gwl.getIsPass());
			chNodes.AddEntity(chNode);
			beforeSDTOfNode = gwl.getCDT();
		}
		for (Node node : nds.ToJavaList())
		{
			Object tempVar2 = gwls.GetEntityByKey(GenerWorkerListAttr.FK_Node, node.getNodeID());
			GenerWorkerList gwl = tempVar2 instanceof GenerWorkerList ? (GenerWorkerList)tempVar2 : null;
			if (gwl != null)
			{
				continue;
			}

			//宸茬粡璁惧畾
			Object tempVar3 = chNodes.GetEntityByKey(CHNodeAttr.FK_Node, node.getNodeID());
			chNode = tempVar3 instanceof CHNode ? (CHNode)tempVar3 : null;
			if (chNode != null)
			{
				continue;
			}

			String sdtOfNode = "";
			String plantStartDt = "";
			if (node.GetParaBoolen("IsYouLiTai") == true)
			{
				if (isFirstY == true)
				{
					for (DataRow drYL : dtYL.Rows)
					{
						Object tempVar4 = chNodes.GetEntityByKey(CHNodeAttr.FK_Node, Integer.parseInt(drYL.get("NodeID").toString()));
						chNode = tempVar4 instanceof CHNode ? (CHNode)tempVar4 : null;
						if (chNode != null)
						{
							continue;
						}
						chNode = new CHNode();
						chNode.setWorkID(this.getWorkID());
						chNode.setFK_Node(Integer.parseInt(drYL.get("NodeID").toString()));
						chNode.setNodeName(drYL.get("Name").toString());
						//璁″垝寮�濮嬫椂闂�
						plantStartDt = beforeSDTOfNode;
						chNode.setStartDT(plantStartDt);
						//璁″垝瀹屾垚鏃堕棿
						sdtOfNode = getSDTOfNode(node, beforeSDTOfNode, gwf);
						chNode.setEndDT(sdtOfNode);
						//宸ユ椂
						int gty = 0;
						if (DataType.IsNullOrEmpty(plantStartDt) == false && DataType.IsNullOrEmpty(sdtOfNode) == false)
						{
							gty = DataType.GetSpanDays(plantStartDt, sdtOfNode);
						}

						chNode.setGT(gty);

						beforeSDTOfNode = sdtOfNode;
						chNodes.AddEntity(chNode);
					}
					isFirstY = false;
				}
				continue;
			}
			chNode = new CHNode();
			chNode.setWorkID(this.getWorkID());
			chNode.setFK_Node(node.getNodeID());
			chNode.setNodeName(node.getName());

			//璁″垝寮�濮嬫椂闂�
			plantStartDt = beforeSDTOfNode;
			chNode.setStartDT(plantStartDt);

			//璁″垝瀹屾垚鏃堕棿
			sdtOfNode = getSDTOfNode(node, beforeSDTOfNode, gwf);
			chNode.setEndDT(sdtOfNode);

			//璁＄畻鍒濆鍊煎伐澶�
			int gs = 0;
			if (DataType.IsNullOrEmpty(plantStartDt) == false && DataType.IsNullOrEmpty(sdtOfNode) == false)
			{
				gs = DataType.GetSpanDays(plantStartDt, sdtOfNode);
			}
			chNode.setGT(gs);
			beforeSDTOfNode = sdtOfNode;
			chNodes.AddEntity(chNode);

		}

			/// 娴佺▼鑺傜偣淇℃伅

		ds.Tables.add(chNodes.ToDataTableField("WF_CHNode"));
		//鑾峰彇褰撳墠鑺傜偣淇℃伅
		Node nd = new Node(this.getFK_Node());
		ds.Tables.add(nd.ToDataTableField("WF_CurrNode"));



			///鑾峰彇鍓╀綑澶╂暟
		Part part = new Part();
		part.setMyPK(nd.getFK_Flow() + "_0_DeadLineRole");
		int count = part.RetrieveFromDBSources();
		int day = 0; //鍚亣鏈熺殑澶╂暟
		Date dateT = new Date();
		if (count > 0)
		{
			//鍒ゆ柇鏄惁鍖呭惈鍋囨湡
			if (Integer.parseInt(part.getTag4()) == 0)
			{
				String holidays = GloVar.getHolidays();
				while (true)
				{
					if (dateT.compareTo(DataType.ParseSysDate2DateTime(gwf.getSDTOfFlow())) >= 0)
					{
						break;
					}

					if (holidays.contains( DateUtils.format(dateT,"MM-dd")))
					{
						dateT = DateUtils.addDay(dateT, 1);
						day++;
						continue;
					}
					dateT = DateUtils.addDay(dateT, 1);
				}

			}

		}
		String spanTime = GetSpanTime(new Date(), DataType.ParseSysDate2DateTime(gwf.getSDTOfFlow()), day);
		dt = new DataTable();
		dt.TableName = "SpanTime";
		dt.Columns.Add("SpanTime");
		dr = dt.NewRow();
		dr.setValue("SpanTime", spanTime);
		dt.Rows.add(dr);
		ds.Tables.add(dt);

			/// 鑾峰彇鍓╀綑澶╂暟

		return bp.tools.Json.ToJson(ds);
	}

	private String getSDTOfNode(Node node, String beforeSDTOfNode, GenerWorkFlow gwf) throws Exception {
		Date SDTOfNode = new Date();
		if (beforeSDTOfNode.equals("")) {
			beforeSDTOfNode = gwf.getSDTOfNode();
		}
		// 鎸夊ぉ銆佸皬鏃惰�冩牳
		if (node.GetParaInt("CHWayOfTimeRole") == 0) {
			// 澧炲姞澶╂暟. 鑰冭檻鍒颁簡鑺傚亣鏃�.
			int timeLimit = node.getTimeLimit();
			SDTOfNode = Glo.AddDayHoursSpan(DateUtils.parse(beforeSDTOfNode), node.getTimeLimit(), node.getTimeLimitHH(), node.getTimeLimitMM(), node.getTWay());
		}
		// 鎸夌収鑺傜偣瀛楁璁剧疆
		if (node.GetParaInt("CHWayOfTimeRole") == 1) {
			// 鑾峰彇璁剧疆鐨勫瓧娈点��
			String keyOfEn = node.GetParaString("CHWayOfTimeRoleField");
			if (DataType.IsNullOrEmpty(keyOfEn) == true) {
				node.setHisCHWay(CHWay.None);
			} else {
				SDTOfNode = DataType.ParseSysDateTime2DateTime(node.getHisWork().GetValByKey(keyOfEn).toString());
			}

		}
		return DateUtils.format(SDTOfNode,DataType.getSysDataTimeFormat());
	}

	/// 鏃堕檺鍒濆鍖栨暟鎹�

	/// 鑺傜偣鏃堕檺閲嶆柊璁剧疆
	public final String CH_Save() throws Exception {
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		// 鑾峰彇娴佺▼搴斿畬鎴愭椂闂�
		String sdtOfFow = this.GetRequestVal("GWF");
		if (DataType.IsNullOrEmpty(sdtOfFow) == false && !gwf.getSDTOfFlow().equals(sdtOfFow)) {
			gwf.setSDTOfFlow(sdtOfFow);
		}

		// 鑾峰彇鑺傜偣鐨勬椂闄愯缃�
		Nodes nds = new Nodes(this.getFK_Flow());
		CHNode chNode = null;
		for (Node nd : nds.ToJavaList()) {
			chNode = new CHNode();
			String startDT = this.GetRequestVal("StartDT_" + nd.getNodeID());
			String endDT = this.GetRequestVal("EndDT_" + nd.getNodeID());
			int gt = this.GetRequestValInt("GT_" + nd.getNodeID());
			float scale = this.GetRequestValFloat("Scale_" + nd.getNodeID());
			float chanzhi = this.GetRequestValFloat("ChanZhi_" + nd.getNodeID());
			float totalScale = this.GetRequestValFloat("TotalScale_" + nd.getNodeID());

			chNode.setWorkID(this.getWorkID());
			chNode.setFK_Node(nd.getNodeID());
			chNode.setNodeName(nd.getName());
			if (DataType.IsNullOrEmpty(startDT) == false) {
				chNode.setStartDT(startDT);
			}
			if (DataType.IsNullOrEmpty(endDT) == false) {
				chNode.setEndDT(endDT);
			}

			chNode.setGT(gt);
			chNode.setScale(scale);
			chNode.setChanZhi(chanzhi);
			chNode.setTotalScale(totalScale);
			chNode.Save();
		}
		gwf.Update();
		return "淇濆瓨鎴愬姛";
	}

	/// 鑺傜偣鏃堕檺閲嶆柊璁剧疆

	/// 鑺傜偣澶囨敞鐨勮缃�
	public final String Note_Init() throws Exception {
		Paras ps = new Paras();
		ps.SQL = "SELECT * FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track WHERE ActionType="
				+ SystemConfig.getAppCenterDBVarStr() + "ActionType AND WorkID=" + SystemConfig.getAppCenterDBVarStr()
				+ "WorkID";
		ps.Add("ActionType", bp.wf.ActionType.FlowBBS.getValue());
		ps.Add("WorkID", this.getWorkID());

		// 杞寲鎴恓son
		return bp.tools.Json.ToJson(DBAccess.RunSQLReturnTable(ps));
	}

	/**
	 * 淇濆瓨澶囨敞.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String Note_Save() throws Exception {
		String msg = this.GetRequestVal("Msg");
		// 闇�瑕佸垹闄rack琛ㄤ腑鐨勬暟鎹槸鍚﹀瓨鍦ㄥ娉�
		String sql = "DELETE From ND" + Integer.parseInt(this.getFK_Flow()) + "Track WHERE WorkID=" + getWorkID()
				+ " AND NDFrom=" + this.getFK_Node() + " AND EmpFrom='" + WebUser.getNo() + "' And ActionType="
				+ ActionType.FlowBBS.getValue();
		DBAccess.RunSQL(sql);
		// 澧炲姞track
		Node nd = new Node(this.getFK_Node());
		Glo.AddToTrack(ActionType.FlowBBS, this.getFK_Flow(), this.getWorkID(), this.getFID(), nd.getNodeID(),
				nd.getName(), WebUser.getNo(), WebUser.getName(), nd.getNodeID(), nd.getName(), WebUser.getNo(),
				WebUser.getName(), msg, null);

		// 鍙戦�佹秷鎭�
		String empsStrs = DBAccess
				.RunSQLReturnStringIsNull("SELECT Emps FROM WF_GenerWorkFlow WHERE WorkID=" + this.getWorkID(), "");
		String[] emps = empsStrs.split("[@]", -1);
		// 鏍囬
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		String title = "娴佺▼鍚嶇О涓�" + gwf.getFlowName() + "鏍囬涓�" + gwf.getTitle() + "鍦ㄨ妭鐐瑰鍔犲娉ㄨ鏄�" + msg;

		for (String emp : emps) {
			if (DataType.IsNullOrEmpty(emp)) {
				continue;
			}
			// 鑾峰緱褰撳墠浜虹殑閭欢.
			bp.wf.port.WFEmp empEn = new bp.wf.port.WFEmp(emp);

			bp.wf.Dev2Interface.Port_SendMsg(empEn.getNo(), title, msg, null, "NoteMessage", this.getFK_Flow(),
					this.getFK_Node(), this.getWorkID(), this.getFID());

		}
		return "淇濆瓨鎴愬姛";
	}

	/// 鑺傜偣澶囨敞鐨勮缃�

	private static String GetSpanTime(Date t1, Date t2, int day)
	{
		Date span = new Date(t2.getTime() - t1.getTime());

		int days = DateUtils.getDay(span);

		int hours = DateUtils.getHour(span);

		int minutes = DateUtils.getMinute(span);

		if (days == 0 && hours == 0 && minutes == 0)
		{
			minutes = DateUtils.getSecond(span) > 0 ? 1 : 0;
		}

		String spanStr = "";

		if (days > 0)
		{
			spanStr += (days - day) + "澶�";
		}

		if (hours > 0)
		{
			spanStr += hours + "鏃�";
		}

		if (minutes > 0)
		{
			spanStr += minutes + "鍒�";
		}

		if (spanStr.length() == 0)
		{
			spanStr = "0鍒�";
		}

		return spanStr;
	}


    public String FastInput_Init()throws Exception
    {
        String groupKey = this.GetRequestVal("GroupKey");
        FastInputs ens = new FastInputs();
        ens.Retrieve(FastInputAttr.ContrastKey, groupKey, FastInputAttr.FK_Emp, WebUser.getNo());
        if (ens.size() > 0)
            return ens.ToJson();

        if (groupKey.equals("Comment"))
        {
            FastInput en = new FastInput();
            en.setMyPK(DBAccess.GenerGUID());
            en.setContrastKey(groupKey);
            en.setVals("宸查槄");
            en.setFK_Emp(WebUser.getNo());
            en.Insert();
        }
        if (groupKey.equals("CYY"))
        {
            FastInput en = new FastInput();
            en.setMyPK(DBAccess.GenerGUID());
            en.setContrastKey(groupKey);
            en.setVals("鍚屾剰");
            en.setFK_Emp(WebUser.getNo());
            en.Insert();

            en = new FastInput();
            en.setMyPK(DBAccess.GenerGUID());
            en.setContrastKey(groupKey);
            en.setVals("涓嶅悓鎰�");
            en.setFK_Emp(WebUser.getNo());
            en.Insert();

            en = new FastInput();
            en.setMyPK(DBAccess.GenerGUID());
            en.setContrastKey(groupKey);
            en.setVals("鍚屾剰锛岃棰嗗鎵圭ず");
            en.setFK_Emp(WebUser.getNo());
            en.Insert();

            en = new FastInput();
            en.setMyPK(DBAccess.GenerGUID());
            en.setContrastKey(groupKey);
            en.setVals("鍚屾剰鍔炵悊");
            en.setFK_Emp(WebUser.getNo());
            en.Insert();

            en = new FastInput();
            en.setMyPK(DBAccess.GenerGUID());
            en.setContrastKey(groupKey);
            en.setVals("鎯呭喌灞炲疄鎶ラ瀵兼壒鍑�");
            en.setFK_Emp(WebUser.getNo());
            en.Insert();
        }
        ens = new FastInputs();
        ens.Retrieve(FastInputAttr.ContrastKey, groupKey);
        return ens.ToJson();
    }
    
     
    public String SubFlowGuid_Save() throws Exception
    {
        //鑾峰緱閫夋嫨鐨勫疄浣撲俊鎭�. 鏍煎紡涓�: 001@杩愯緭鍙�,002@娉曞埗鍙�
    	String selectNos = GetRequestVal("SelectNos");
        if (DataType.IsNullOrEmpty(selectNos) == true)
            return "err@娌℃湁閫夋嫨闇�瑕佸惎鍔ㄥ瓙娴佺▼鐨勪俊鎭�";

        String isStartSameLevelFlow = this.GetRequestVal("IsStartSameLevelFlow");
        String subFlowMyPK = GetRequestVal("SubFlowMyPK");

        //鍓嶇疆瀵艰埅鐨勫瓙娴佺▼鐨勯厤缃�.
        SubFlowHandGuide subFlow = new SubFlowHandGuide(subFlowMyPK);
         

        // #region 姹傚嚭鏉ュ瓙娴佺▼鐨勪笟鍔¤〃
      		String pTableOfSubFlow = "";
      		//.
      		//姹傚嚭鏉ュ紑濮嬭妭鐐�.
      		Node nd = new Node(Integer.parseInt(this.getFK_Flow() + "01"));
      		String sql = "SELECT PTable FROM Sys_MapData WHERE No='" + nd.getNodeFrmID() + "'";
      		pTableOfSubFlow = DBAccess.RunSQLReturnString(sql);
      //C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
      		///#endregion 姹傚嚭鏉ュ瓙娴佺▼鐨勪笟鍔¤〃

      		//璁板綍瀛樺湪鐨勮褰�
      		String filedNo = "";

      		//閫夋嫨鐨勭紪鍙�. selectNos鏍煎紡涓� 001@寮�鍙戝徃,002@杩愯緭鍙�,
      		String[] strs = selectNos.split("[,]", -1);
      		GenerWorkFlow gwf = null;
      		for (String str : strs)
      		{
      			if (DataType.IsNullOrEmpty(str) == true)
      			{
      				continue;
      			}

      			// str鐨勬牸寮忎负:002@杩愯緭鍙�
      			String[] enNoName = str.split("[@]", -1);
      			if (enNoName.length < 2)
      			{
      				return "err@" + enNoName[0] + "涓嶅瓨鍦ㄥ悕绉�";
      			}

      			//鑾峰緱瀹炰綋鐨勫悕瀛�,缂栧彿.
      			String enNo = enNoName[0];
      			String enName = enNoName[1];

      			filedNo = "'" + enNo + "',";

      			//鍒ゆ柇璇ュ疄浣� 鐨勫瓙娴佺▼鏄惁鍙戣捣?, 濡傛灉鍙戣捣灏变笉鍦ㄩ噸澶嶅彂璧�.
      			sql = "SELECT WorkID FROM WF_GenerWorkFlow WHERE AtPara LIKE '%@SubFlowGuideEnNoFiled=" + enNo + "%' AND PWorkID=" + this.getPWorkID();
      			int val = DBAccess.RunSQLReturnValInt(sql, 0);
      			if (val != 0)
      			{
      				continue;
      			}

      			//鍒涘缓瀛愭祦绋媔d.
      			long workID = Dev2Interface.Node_CreateBlankWork(this.getFK_Flow(), null, null, WebUser.getNo(), null,
      					this.getPWorkID(), this.getPFID(), this.getPFlowNo(), this.getPNodeID(), null, 0, null, null, isStartSameLevelFlow);

      			//淇敼GenerWorkFlow涓鸿崏绋�
      			gwf = new GenerWorkFlow(workID);
      			gwf.setWFState(WFState.Draft);
      			gwf.setTitle(strs[1]);
      			gwf.SetPara(SubFlowHandGuideAttr.SubFlowGuideEnNameFiled, enName);
      			gwf.SetPara(SubFlowHandGuideAttr.SubFlowGuideEnNoFiled, enNo); //淇濆瓨鍒板弬鏁板瓧娈甸噷.
      			gwf.Update();

      			if (isStartSameLevelFlow != null && isStartSameLevelFlow.equals("1") == true)
      			{
      				String slFlowNo = GetRequestVal("SLFlowNo");
      				int slNode = GetRequestValInt("SLNodeID");
      				long slWorkID = GetRequestValInt("SLWorkID");
      				gwf.SetPara("SLFlowNo", slFlowNo);
      				gwf.SetPara("SLNodeID", slNode);
      				gwf.SetPara("SLWorkID", slWorkID);
      				gwf.SetPara("SLEmp",  WebUser.getNo());
      				gwf.Update();
      			}

      			//鎵ц鏇存柊. 瀹炰綋瀛楁.
      			sql = "UPDATE " + pTableOfSubFlow + " SET " + subFlow.getSubFlowGuideEnNoFiled() + "='" + enNo + "'," + subFlow.getSubFlowGuideEnNameFiled() + "='" + enName + "'";
      			sql += "  WHERE OID=" + workID;
      			DBAccess.RunSQL(sql);
      		}
      		
    		//鏌ヨ鍑烘潵鎵�鏈夌殑鑽夌.
    		GenerWorkFlows gwfs = new GenerWorkFlows();
    		gwfs.Retrieve(GenerWorkFlowAttr.PWorkID, this.getPWorkID(), GenerWorkFlowAttr.FK_Flow, this.getFK_Flow(), GenerWorkFlowAttr.WFState, WFState.Draft.getValue());

    		return bp.tools.Json.ToJson(gwfs.ToDataTableField());

    }
    
    public String SubFlowGuid_DeleteSubFlowDraf() throws NumberFormatException, Exception
    {
    	 Node nd = new Node(Integer.parseInt(this.getFK_Flow() + "01"));
 		String sql = "SELECT PTable FROM Sys_MapData WHERE No='" + nd.getNodeFrmID() + "'";
 		String pTableOfSubFlow = DBAccess.RunSQLReturnString(sql);

 		DBAccess.RunSQL("DELETE FROM WF_GenerWorkFlow WHERE WorkID=" + this.getWorkID());
 		DBAccess.RunSQL("DELETE FROM WF_GenerWorkerlist WHERE WorkID=" + this.getWorkID());
 		DBAccess.RunSQL("DELETE FROM " + pTableOfSubFlow + " WHERE OID=" + this.getWorkID());
 		return "鍒犻櫎鎴愬姛";

    }
    


}