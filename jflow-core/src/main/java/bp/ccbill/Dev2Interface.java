package bp.ccbill;

import bp.tools.DateUtils;
import bp.tools.StringHelper;
import bp.wf.*;
import bp.en.*;
import bp.da.*;
import bp.web.*;
import bp.sys.*;
import bp.*;
import bp.wf.template.SysFormTrees;

import java.util.*;
import java.time.*;
import java.util.Map;

/** 
 接口调用
*/
public class Dev2Interface
{
	/** 
	 增加日志
	 
	 param at
	 param frmID
	 param at
	 param msg
	 @return 
	*/

	public static void Dict_AddTrack(String frmID, String frmWorkID, String at, String msg, String paras, String flowNo, String flowName, int nodeID, long workIDOfFlow) throws Exception {
		Dict_AddTrack(frmID, frmWorkID, at, msg, paras, flowNo, flowName, nodeID, workIDOfFlow, "");
	}

	public static void Dict_AddTrack(String frmID, String frmWorkID, String at, String msg, String paras, String flowNo, String flowName, int nodeID) throws Exception {
		Dict_AddTrack(frmID, frmWorkID, at, msg, paras, flowNo, flowName, nodeID, 0, "");
	}

	public static void Dict_AddTrack(String frmID, String frmWorkID, String at, String msg, String paras, String flowNo, String flowName) throws Exception {
		Dict_AddTrack(frmID, frmWorkID, at, msg, paras, flowNo, flowName, 0, 0, "");
	}

	public static void Dict_AddTrack(String frmID, String frmWorkID, String at, String msg, String paras, String flowNo) throws Exception {
		Dict_AddTrack(frmID, frmWorkID, at, msg, paras, flowNo, null, 0, 0, "");
	}

	public static void Dict_AddTrack(String frmID, String frmWorkID, String at, String msg, String paras) throws Exception {
		Dict_AddTrack(frmID, frmWorkID, at, msg, paras, null, null, 0, 0, "");
	}

	public static void Dict_AddTrack(String frmID, String frmWorkID, String at, String msg) throws Exception {
		Dict_AddTrack(frmID, frmWorkID, at, msg, null, null, null, 0, 0, "");
	}

//ORIGINAL LINE: public static void Dict_AddTrack(string frmID, string frmWorkID, string at, string msg, string paras = null, string flowNo = null, string flowName = null, int nodeID = 0, Int64 workIDOfFlow = 0, string frmName = "")
	public static void Dict_AddTrack(String frmID, String frmWorkID, String at, String msg, String paras, String flowNo, String flowName, int nodeID, long workIDOfFlow, String frmName) throws Exception {
		bp.ccbill.Track tk = new bp.ccbill.Track();
		tk.setWorkID(frmWorkID);
		tk.setFrmID(frmID);
		tk.setFrmName(frmName);
		tk.setActionType(at);

		switch (at)
		{
			case FrmActionType.BBS:
				tk.setActionTypeText("评论");
				break;
			case FrmActionType.Create:
				tk.setActionTypeText("创建");
				break;
			case FrmActionType.DataVerReback:
				tk.setActionTypeText("数据版本");
				break;
			case FrmActionType.Save:
				tk.setActionTypeText("保存");
				break;
			case FrmActionType.StartFlow:
				tk.setActionTypeText("发起流程");
				break;
			default:
				tk.setActionTypeText("其他");
				break;
		}

		tk.setRec(WebUser.getNo());
		tk.setRecName(WebUser.getName());
		tk.setDeptNo(WebUser.getFK_Dept());
		tk.setDeptName(WebUser.getFK_DeptName());

		// 流程信息。
		tk.setWorkIDOfFlow(workIDOfFlow);
		tk.setNodeID(nodeID);
		if (flowName != null)
		{
			tk.setFlowName(flowName);
		}
		if (flowNo != null)
		{
			tk.setFlowNo(flowNo);
		}

		//tk.setMyPK(tk.FrmID + "_" + tk.WorkID + "_" + tk.Rec + "_" + (int)BP.CCBill.FrmActionType.BBS;
		tk.setMsg(msg);
		tk.setRDT(DataType.getCurrentDataTime());

		////流程信息.
		//tk.NodeID = nodeID;
		//tk.NodeName = nodeName;
		//tk.FlowNo = flowNo;
		//tk.FlowName = flowName;
		//tk.FID = fid;
		tk.Insert();
	}


	/** 
	 创建单据的WorkID
	 
	 param frmID
	 param userNo
	 param htParas
	 param pDictFrmID
	 @return 
	*/

	public static long CreateBlankBillID(String frmID, String userNo, java.util.Hashtable htParas, String pDictFrmID) throws Exception {
		return CreateBlankBillID(frmID, userNo, htParas, pDictFrmID, 0);
	}

	public static long CreateBlankBillID(String frmID, String userNo, java.util.Hashtable htParas) throws Exception {
		return CreateBlankBillID(frmID, userNo, htParas, null, 0);
	}

	public static long CreateBlankBillID(String frmID, String userNo) throws Exception {
		return CreateBlankBillID(frmID, userNo, null, null, 0);
	}

	public static long CreateBlankBillID(String frmID) throws Exception {
		return CreateBlankBillID(frmID, null, null, null, 0);
	}

//ORIGINAL LINE: public static Int64 CreateBlankBillID(string frmID, string userNo = null, Hashtable htParas = null, string pDictFrmID = null, Int64 pDictWorkID = 0)
	public static long CreateBlankBillID(String frmID, String userNo, Hashtable htParas, String pDictFrmID, long pDictWorkID) throws Exception {
		if (userNo == null)
		{
			userNo = WebUser.getNo();
		}

		GenerBill gb = new GenerBill();
		int i = gb.Retrieve(GenerBillAttr.FrmID, frmID, GenerBillAttr.Starter, userNo, GenerBillAttr.BillState, 0);
		if (i == 1)
		{
			GERpt rpt1 = new GERpt(frmID);
			rpt1.setOID(gb.getWorkID());
			int count = rpt1.RetrieveFromDBSources();

			if (htParas != null)
			{
				rpt1.Copy(htParas);
			}

			rpt1.SetValByKey("BillState", 0);
			rpt1.SetValByKey("Starter", gb.getStarter());
			rpt1.SetValByKey("StarterName", gb.getStarterName());
			rpt1.SetValByKey("FK_Dept", WebUser.getFK_Dept());
			rpt1.SetValByKey("RDT", gb.getRDT());
			rpt1.SetValByKey("Title", gb.getTitle());
			rpt1.SetValByKey("BillNo", gb.getBillNo());
			if (pDictFrmID != null)
			{
				rpt1.SetValByKey("PWorkID", pDictWorkID);
				rpt1.SetValByKey("PFrmID", pDictFrmID);
			}
			if (count == 0)
			{
				rpt1.InsertAsOID(gb.getWorkID());
			}
			else
			{
				rpt1.Update();
			}
			return gb.getWorkID();
		}


		FrmBill fb = new FrmBill(frmID);
		gb.setWorkID(DBAccess.GenerOID("WorkID"));
		gb.setBillState(BillState.None); //初始化状态.
		gb.setStarter(WebUser.getNo());
		gb.setStarterName(WebUser.getName());
		gb.setFrmName(fb.getName()); //单据名称.
		gb.setFrmID(fb.getNo()); //单据ID

		//if (DataType.IsNullOrEmpty(billNo) == false)
		//    gb.BillNo = billNo; //BillNo
		gb.setFK_Dept(WebUser.getFK_Dept());
		gb.setDeptName(WebUser.getFK_DeptName());
		gb.setFKFrmTree(fb.getFKFormTree()); //单据类别.
		gb.setRDT(DataType.getCurrentDataTime());
		gb.setNDStep(1);
		gb.setNDStepName("启动");

		//父字典信息.
		if (pDictFrmID != null)
		{
			gb.setPFrmID(pDictFrmID);
			gb.setPWorkID(pDictWorkID);
		}


		//创建rpt.
		GERpt rpt = new GERpt(frmID);

		//设置标题.
		if (fb.getEntityType() == EntityType.FrmBill)
		{
			gb.setTitle(Dev2Interface.GenerTitle(fb.getTitleRole(), rpt));
			//if (DataType.IsNullOrEmpty(billNo) == false)
			//    gb.BillNo = billNo;
			//else
			gb.setBillNo(bp.ccbill.Dev2Interface.GenerBillNo(fb.getBillNoFormat(), gb.getWorkID(), null, frmID));
		}

		if (fb.getEntityType() == EntityType.EntityTree || fb.getEntityType() == EntityType.FrmDict)
		{
			rpt.getEnMap().setCodeStruct(fb.getEnMap().getCodeStruct());
			//if (DataType.IsNullOrEmpty(billNo) == false)
			//    gb.BillNo = billNo;
			//else
			gb.setBillNo(rpt.GenerNewNoByKey("BillNo", null));
			// BP.CCBill.Dev2Interface.GenerBillNo(fb.BillNoFormat, gb.WorkID, null, frmID);
			gb.setTitle("");
		}

		gb.DirectInsert(); //执行插入.

		//如果.
		if (htParas != null)
		{
			rpt.Copy(htParas);
		}

		//更新基础的数据到表单表.
		// rpt = new BP.WF.GERpt(frmID);
		rpt.SetValByKey("BillState", gb.getBillState().getValue());
		rpt.SetValByKey("Starter", gb.getStarter());
		rpt.SetValByKey("StarterName", gb.getStarterName());
		rpt.SetValByKey("FK_Dept", WebUser.getFK_Dept());
		rpt.SetValByKey("RDT", gb.getRDT());
		rpt.SetValByKey("Title", gb.getTitle());
		rpt.SetValByKey("BillNo", gb.getBillNo());
		if (pDictFrmID != null)
		{
			rpt.SetValByKey("PWorkID", pDictWorkID);
			rpt.SetValByKey("PFrmID", pDictFrmID);
		}



		rpt.setOID(gb.getWorkID());
		rpt.InsertAsOID(gb.getWorkID());

		bp.ccbill.Dev2Interface.Dict_AddTrack(frmID, String.valueOf(rpt.getOID()), FrmActionType.Create, "创建记录");

		return gb.getWorkID();
	}
	/** 
	 创建一个实体ID
	 
	 param frmID 实体ID
	 param userNo 用户编号
	 param htParas 参数
	 @return 一个实例的workid
	*/
	public static long CreateBlankDictID(String frmID, String userNo, Hashtable htParas) throws Exception {
		if (userNo == null)
		{
			userNo = WebUser.getNo();
		}

		// 创建一个实体, 先检查一下是否有空白的数据.
		GERpt rpt = new GERpt(frmID);
		int i = rpt.Retrieve("Starter", userNo, "BillState", 0);
		if (i >= 1)
		{
			if (htParas != null)
			{
				rpt.Copy(htParas);
			}

			rpt.SetValByKey("RDT", DataType.getCurrentDate());
			rpt.Update();
			return rpt.getOID(); //如果有空白的数据，就返回给他.
		}


		//执行copy数据.
		if (htParas != null)
		{
			rpt.Copy(htParas);
		}

		FrmBill fb = new FrmBill(frmID);


		//更新基础的数据到表单表.
		rpt.SetValByKey("BillState", 0);
		rpt.SetValByKey("Starter", WebUser.getNo());
		rpt.SetValByKey("StarterName", WebUser.getName());
		rpt.SetValByKey("FK_Dept", WebUser.getFK_Dept());
		rpt.SetValByKey("RDT", DataType.getCurrentDate());

		//设置编号生成规则.
		rpt.getEnMap().setCodeStruct(fb.getBillNoFormat());

		//rpt.SetValByKey("Title", gb.Title);
		rpt.SetValByKey("BillNo", rpt.GenerNewNoByKey("BillNo", null));
		rpt.setOID(DBAccess.GenerOID("WorkID"));
		rpt.InsertAsOID(rpt.getOID());


		bp.ccbill.Dev2Interface.Dict_AddTrack(frmID, String.valueOf(rpt.getOID()), FrmActionType.Create, "创建记录");

		return rpt.getOID();
	}
	/** 
	 保存实体数据
	 
	 param frmID 表单ID
	 param workid 工作ID
	 param htParas 参数数据
	 @return 
	*/
	public static void SaveDictWork(String frmID, long workid, Hashtable htParas) throws Exception {
		// 创建一个实体, 先检查一下是否有空白的数据.
		GERpt rpt = new GERpt(frmID);
		rpt.setOID(workid);
		if (rpt.RetrieveFromDBSources() == 0)
		{
			if (htParas != null)
			{
				rpt.Copy(htParas);
			}

			//设置编号生成规则.
			FrmBill fb = new FrmBill(frmID);
			rpt.getEnMap().setCodeStruct(fb.getBillNoFormat());
			rpt.SetValByKey("BillNo", rpt.GenerNewNoByKey("BillNo", null));
			rpt.InsertAsOID(workid);
		}
		else
		{
			//执行copy数据.
			if (htParas != null)
			{
				rpt.Copy(htParas);
			}
		}

		//更新基础的数据到表单表.
		rpt.SetValByKey("BillState", 100);
		rpt.SetValByKey("Starter", WebUser.getNo());
		rpt.SetValByKey("StarterName", WebUser.getName());
		rpt.SetValByKey("FK_Dept", WebUser.getFK_Dept());
		rpt.SetValByKey("RDT", DataType.getCurrentDate());
		rpt.Update();

		bp.ccbill.Dev2Interface.Dict_AddTrack(frmID, String.valueOf(workid), FrmActionType.Save, "执行保存");

	}

	/** 
	 保存
	 
	 param frmID 表单ID
	 param workID 工作ID
	 @return 返回保存结果
	*/
	public static String SaveBillWork(String frmID, long workID) throws Exception {
		FrmBill fb = new FrmBill(frmID);

		GenerBill gb = new GenerBill();
		gb.setWorkID(workID);
		int i = gb.RetrieveFromDBSources();
		if (i == 0)
		{
			return "";
		}

		gb.setBillState(BillState.Editing);

		//创建rpt.
		GERpt rpt = new GERpt(gb.getFrmID(), workID);

		if (fb.getEntityType() == EntityType.EntityTree || fb.getEntityType() == EntityType.FrmDict)
		{

			gb.setTitle(rpt.getTitle());
			gb.Update();
			return "保存成功...";
		}

		//单据编号.
		if (DataType.IsNullOrEmpty(gb.getBillNo()) == true && !(fb.getEntityType() == EntityType.EntityTree || fb.getEntityType() == EntityType.FrmDict))
		{
			gb.setBillNo(bp.ccbill.Dev2Interface.GenerBillNo(fb.getBillNoFormat(), workID, null, fb.getPTable()));
			//更新单据里面的billNo字段.
			if (DBAccess.IsExitsTableCol(fb.getPTable(), "BillNo") == true)
			{
				DBAccess.RunSQL("UPDATE " + fb.getPTable() + " SET BillNo='" + gb.getBillNo() + "' WHERE OID=" + workID);
			}
		}

		//标题.
		if (DataType.IsNullOrEmpty(gb.getTitle()) == true && !(fb.getEntityType() == EntityType.EntityTree || fb.getEntityType() == EntityType.FrmDict))
		{
			gb.setTitle(Dev2Interface.GenerTitle(fb.getTitleRole(), rpt));
			//更新单据里面的 Title 字段.
			if (DBAccess.IsExitsTableCol(fb.getPTable(), "Title") == true)
			{
				DBAccess.RunSQL("UPDATE " + fb.getPTable() + " SET Title='" + gb.getTitle() + "' WHERE OID=" + workID);
			}
		}

		gb.Update();

		//把通用的字段更新到数据库.
		rpt.setTitle(gb.getTitle());
		rpt.setBillNo(gb.getBillNo());
		rpt.Update();

		bp.ccbill.Dev2Interface.Dict_AddTrack(frmID, String.valueOf(rpt.getOID()), FrmActionType.Save, "保存");

		return "保存成功...";
	}

	/** 
	 提交
	 
	 param frmID 表单ID
	 param workID 工作ID
	 @return 返回保存结果
	*/
	public static String SubmitWork(String frmID, long workID) throws Exception {
		FrmBill fb = new FrmBill(frmID);

		GenerBill gb = new GenerBill();
		gb.setWorkID(workID);
		int i = gb.RetrieveFromDBSources();
		if (i == 0)
		{
			return "";
		}

		//设置为归档状态.
		gb.setBillState(BillState.Over);

		//创建rpt.
		GERpt rpt = new GERpt(gb.getFrmID(), workID);

		if (fb.getEntityType() == EntityType.EntityTree || fb.getEntityType() == EntityType.FrmDict)
		{
			gb.setTitle(rpt.getTitle());
			gb.Update();
			return "提交成功...";
		}

		//单据编号.
		if (DataType.IsNullOrEmpty(gb.getBillNo()) == true && !(fb.getEntityType() == EntityType.EntityTree || fb.getEntityType() == EntityType.FrmDict))
		{
			gb.setBillNo(bp.ccbill.Dev2Interface.GenerBillNo(fb.getBillNoFormat(), workID, null, fb.getPTable()));
			//更新单据里面的billNo字段.
			if (DBAccess.IsExitsTableCol(fb.getPTable(), "BillNo") == true)
			{
				DBAccess.RunSQL("UPDATE " + fb.getPTable() + " SET BillNo='" + gb.getBillNo() + "' WHERE OID=" + workID);
			}
		}

		//标题.
		if (DataType.IsNullOrEmpty(gb.getTitle()) == true && !(fb.getEntityType() == EntityType.EntityTree || fb.getEntityType() == EntityType.FrmDict))
		{
			gb.setTitle(Dev2Interface.GenerTitle(fb.getTitleRole(), rpt));
			//更新单据里面的 Title 字段.
			if (DBAccess.IsExitsTableCol(fb.getPTable(), "Title") == true)
			{
				DBAccess.RunSQL("UPDATE " + fb.getPTable() + " SET Title='" + gb.getTitle() + "' WHERE OID=" + workID);
			}
		}

		gb.Update();

		//把通用的字段更新到数据库.
		rpt.setTitle(gb.getTitle());
		rpt.setBillNo(gb.getBillNo());
		rpt.Update();

		bp.ccbill.Dev2Interface.Dict_AddTrack(frmID, String.valueOf(workID), FrmActionType.Submit, "执行提交.");


		return "提交成功...";
	}
	/** 
	 保存
	 
	 param frmID 表单ID
	 param workID 工作ID
	 @return 返回保存结果
	*/
	public static String SaveAsDraft(String frmID, long workID) throws Exception {
		GenerBill gb = new GenerBill(workID);
		if (gb.getBillState() != BillState.None)
		{
			return "err@只有在None的模式下才能保存草稿。";
		}

		if (gb.getBillState() != BillState.Editing)
		{
			gb.setBillState(BillState.Editing);
			gb.Update();
		}
		return "保存成功...";
	}
	/** 
	 删除单据
	 
	 param frmID
	 param workID
	 @return 
	*/
	public static String MyBill_Delete(String frmID, long workID) throws Exception {
		FrmBill fb = new FrmBill(frmID);
		String sqls = "DELETE FROM Frm_GenerBill WHERE WorkID=" + workID;
		sqls += "@DELETE FROM " + fb.getPTable() + " WHERE OID=" + workID;
		DBAccess.RunSQLs(sqls);
		return "删除成功.";
	}

	public static String MyBill_DeleteBills(String frmID, String workIds) throws Exception {
		FrmBill fb = new FrmBill(frmID);
		String sqls = "DELETE FROM Frm_GenerBill WHERE WorkID in (" + workIds + ")";
		sqls += "@DELETE FROM " + fb.getPTable() + " WHERE OID in (" + workIds + ")";
		DBAccess.RunSQLs(sqls);
		return "删除成功.";
	}

	/** 
	 删除实体
	 
	 param frmID
	 param workID
	 @return 
	*/
	public static String MyDict_Delete(String frmID, long workID) throws Exception {
		FrmBill fb = new FrmBill(frmID);
		String sql = "@DELETE FROM " + fb.getPTable() + " WHERE OID=" + workID;
		DBAccess.RunSQLs(sql);
		return "删除成功.";
	}


	/** 
	 删除实体单据
	 
	 param frmID
	 param workIds
	 @return 
	*/
	public static String MyDict_DeleteDicts(String frmID, String workIds) throws Exception {
		FrmBill fb = new FrmBill(frmID);
		String sql = "DELETE FROM " + fb.getPTable() + " WHERE OID in (" + workIds + ")";
		DBAccess.RunSQLs(sql);
		return "删除成功.";
	}
	/** 
	 删除树形结构的实体表单
	 
	 param frmID
	 param billNo
	 @return 
	*/
	public static String MyEntityTree_Delete(String frmID, String billNo) throws Exception {
		FrmBill fb = new FrmBill(frmID);
		String sql = "DELETE FROM " + fb.getPTable() + " WHERE BillNo='" + billNo + "' OR ParentNo='" + billNo + "'";
		DBAccess.RunSQLs(sql);
		return "删除成功.";
	}

	/** 
	 复制单据数据
	 
	 param frmID
	 param workID
	 @return 
	*/
	public static String MyBill_Copy(String frmID, long workID) throws Exception {
		//获取单据的属性
		FrmBill fb = new FrmBill(frmID);

		GenerBill gb = new GenerBill();
		gb.setWorkID(DBAccess.GenerOID("WorkID"));
		gb.setBillState(BillState.Editing); //初始化状态.
		gb.setStarter(WebUser.getNo());
		gb.setStarterName(WebUser.getName());
		gb.setFrmName(fb.getName()); //单据名称.
		gb.setFrmID(fb.getNo()); //单据ID

		gb.setFKFrmTree(fb.getFKFormTree()); //单据类别.
		gb.setRDT(DataType.getCurrentDateTime());
		gb.setNDStep(1);
		gb.setNDStepName("启动");

		//创建rpt.
		GERpt rpt = new GERpt(frmID, workID);

		//设置标题.
		gb.setTitle(Dev2Interface.GenerTitle(fb.getTitleRole(), rpt));
		gb.setBillNo(bp.ccbill.Dev2Interface.GenerBillNo(fb.getBillNoFormat(), gb.getWorkID(), null, frmID));

		gb.DirectInsert(); //执行插入.

		//更新基础的数据到表单表.
		rpt.SetValByKey("BillState", gb.getBillState().getValue());
		rpt.SetValByKey("Starter", gb.getStarter());
		rpt.SetValByKey("StarterName", gb.getStarterName());
		rpt.SetValByKey("RDT", gb.getRDT());
		rpt.SetValByKey("Title", gb.getTitle());
		rpt.SetValByKey("BillNo", gb.getBillNo());
		rpt.setOID(gb.getWorkID());
		rpt.InsertAsOID(gb.getWorkID());

			///#region 复制其他数据.

		//复制明细。
		MapDtls dtls = new MapDtls(frmID);
		if (!dtls.isEmpty())
		{
			for (MapDtl dtl : dtls.ToJavaList())
			{
				if (dtl.getIsCopyNDData() == false)
				{
					continue;
				}

				//new 一个实例.
				GEDtl dtlData = new GEDtl(dtl.getNo());

				GEDtls dtlsFromData = new GEDtls(dtl.getNo());
				dtlsFromData.Retrieve(GEDtlAttr.RefPK, workID, null);
				for (GEDtl geDtlFromData : dtlsFromData.ToJavaList())
				{
					//是否启用多附件
					FrmAttachmentDBs dbs = null;
					if (dtl.getIsEnableAthM() == true)
					{
						//根据从表的OID 获取附件信息
						dbs = new FrmAttachmentDBs();
						dbs.Retrieve(FrmAttachmentDBAttr.RefPKVal, geDtlFromData.getOID(), null);
					}

					dtlData.Copy(geDtlFromData);
					dtlData.setRefPK(String.valueOf(rpt.getOID()));
					dtlData.InsertAsNew();
					if (dbs != null && !dbs.isEmpty())
					{
						//复制附件信息
						FrmAttachmentDB newDB = new FrmAttachmentDB();
						for (FrmAttachmentDB db : dbs.ToJavaList())
						{
							newDB.Copy(db);
							newDB.setRefPKVal(String.valueOf(dtlData.getOID()));
							newDB.setFID(dtlData.getOID());
							newDB.setMyPK(DBAccess.GenerGUID(0, null, null));
							newDB.Insert();
						}
					}

				}
			}

		}

		//获取附件组件、
		FrmAttachments athDecs = new FrmAttachments(frmID);
		//复制附件数据。
		if (!athDecs.isEmpty())
		{
			for (FrmAttachment athDec : athDecs.ToJavaList())
			{
				FrmAttachmentDBs aths = new FrmAttachmentDBs();
				aths.Retrieve(FrmAttachmentDBAttr.FK_FrmAttachment, athDec.getMyPK(), FrmAttachmentDBAttr.RefPKVal, workID, null);
				for (FrmAttachmentDB athDB : aths.ToJavaList())
				{
					FrmAttachmentDB athDB_N = new FrmAttachmentDB();
					athDB_N.Copy(athDB);
					athDB_N.setRefPKVal ( String.valueOf(rpt.getOID()));
					athDB_N.setMyPK(DBAccess.GenerGUID(0, null, null));
					athDB_N.Insert();
				}
			}
		}

			///#endregion 复制表单其他数据.

		bp.ccbill.Dev2Interface.Dict_AddTrack(frmID, String.valueOf(workID), "复制", "执行复制");


		return "复制成功.";
	}

	/** 
	 获得发起列表
	 
	 param empID
	 @return 
	*/
	public static DataSet DB_StartBills(String empID) throws Exception {
		//定义容器.
		DataSet ds = new DataSet();

		//单据类别.
		SysFormTrees ens = new SysFormTrees();
		ens.RetrieveAll();

		DataTable dtSort = ens.ToDataTableField("Sort");
		dtSort.TableName = "Sort";
		ds.Tables.add(dtSort);

		//查询出来单据运行模式的.
		FrmBills bills = new FrmBills();
		bills.RetrieveAll();

		//bills.Retrieve(FrmBillAttr.EntityType, 0); //实体类型.

		DataTable dtStart = bills.ToDataTableField("dt");
		dtStart.TableName = "Start";
		ds.Tables.add(dtStart);
		return ds;
	}
	/** 
	 获得待办列表
	 
	 param empID
	 @return 
	*/
	public static DataTable DB_Todolist(String empID)
	{
		return new DataTable();
	}
	/** 
	 草稿列表
	 
	 param frmID 单据ID
	 param empID 操作员
	 @return 
	*/
	public static DataTable DB_Draft(String frmID, String empID) throws Exception {
		if (DataType.IsNullOrEmpty(empID) == true)
		{
			empID = WebUser.getNo();
		}

		GenerBills bills = new GenerBills();
		bills.Retrieve(GenerBillAttr.FrmID, frmID, GenerBillAttr.Starter, empID, null);

		return bills.ToDataTableField("dt");
	}

	public static String GenerTitle(String titleRole, Entity wk)
	{
		if (DataType.IsNullOrEmpty(titleRole))
		{
			// 为了保持与ccflow4.5的兼容,从开始节点属性里获取.
			Attr myattr = wk.getEnMap().getAttrs().GetAttrByKey("Title");
			if (myattr == null)
			{
				myattr = wk.getEnMap().getAttrs().GetAttrByKey("Title");
			}

			if (myattr != null)
			{
				titleRole = myattr.getDefaultVal().toString();
			}

			if (DataType.IsNullOrEmpty(titleRole) || titleRole.contains("@") == false)
			{
				titleRole = "@WebUser.FK_DeptName-@WebUser.No,@WebUser.Name在@RDT发起.";
			}
		}

		if (titleRole.equals("@OutPara") || DataType.IsNullOrEmpty(titleRole) == true)
		{
			titleRole = "@WebUser.FK_DeptName-@WebUser.No,@WebUser.Name在@RDT发起.";
		}


		titleRole = titleRole.replace("@WebUser.No", WebUser.getNo());
		titleRole = titleRole.replace("@WebUser.Name", WebUser.getName());
		titleRole = titleRole.replace("@WebUser.FK_DeptNameOfFull", WebUser.getFK_DeptNameOfFull());
		titleRole = titleRole.replace("@WebUser.FK_DeptName", WebUser.getFK_DeptName());
		titleRole = titleRole.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
		titleRole = titleRole.replace("@RDT", DataType.getCurrentDateByFormart("yy年MM月dd日HH时mm分"));
		if (titleRole.contains("@"))
		{
			Attrs attrs = wk.getEnMap().getAttrs();

			// 优先考虑外键的替换,因为外键文本的字段的长度相对较长。
			for (Attr attr : attrs.ToJavaList())
			{
				if (titleRole.contains("@") == false)
				{
					break;
				}
				if (attr.getIsRefAttr() == false)
				{
					continue;
				}
				titleRole = titleRole.replace("@" + attr.getKey(), wk.GetValStrByKey(attr.getKey()));
			}

			//在考虑其它的字段替换.
			for (Attr attr : attrs.ToJavaList())
			{
				if (titleRole.contains("@") == false)
				{
					break;
				}

				if (attr.getIsRefAttr() == true)
				{
					continue;
				}
				titleRole = titleRole.replace("@" + attr.getKey(), wk.GetValStrByKey(attr.getKey()));
			}
		}
		titleRole = titleRole.replace('~', '-');
		titleRole = titleRole.replace("'", "”");

		// 为当前的工作设置title.
		wk.SetValByKey("Title", titleRole);
		return titleRole;
	}
	/** 
	 生成单据编号
	 
	 param billNo 单据编号规则
	 param workid 工作ID
	 param en 实体类
	 param frmID 表单ID
	 @return 生成的单据编号
	*/
	public static String GenerBillNo(String billNo, long workid, Entity en, String frmID) throws Exception {
		if (DataType.IsNullOrEmpty(billNo))
		{
			billNo = "3";
		}

		if (billNo.contains("@"))
		{
			billNo = bp.wf.Glo.DealExp(billNo, en, null);
		}

		/*如果，Bill 有规则 */
		billNo = billNo.replace("{YYYY}", DataType.getCurrentDateByFormart("yyyy"));
		billNo = billNo.replace("{yyyy}", DataType.getCurrentDateByFormart("yyyy"));

		billNo = billNo.replace("{yy}", DataType.getCurrentDateByFormart("yy"));
		billNo = billNo.replace("{YY}", DataType.getCurrentDateByFormart("yy"));

		billNo = billNo.replace("{MM}", DataType.getCurrentDateByFormart("MM"));
		billNo = billNo.replace("{mm}", DataType.getCurrentDateByFormart("MM"));

		billNo = billNo.replace("{DD}", DateUtils.format(new Date(), "dd"));
		billNo = billNo.replace("{dd}", DateUtils.format(new Date(), "dd"));
		billNo = billNo.replace("{HH}", DateUtils.format(new Date(),"HH"));
		billNo = billNo.replace("{hh}", DateUtils.format(new Date(),"HH"));

		billNo = billNo.replace("{LSH}", String.valueOf(workid));
		billNo = billNo.replace("{WorkID}", String.valueOf(workid));
		billNo = billNo.replace("{OID}", String.valueOf(workid));

		if (billNo.contains("@WebUser.DeptZi"))
		{
			String val = DBAccess.RunSQLReturnStringIsNull("SELECT Zi FROM Port_Dept WHERE No='" + WebUser.getFK_Dept() + "'", "");
			billNo = billNo.replace("@WebUser.DeptZi", val.toString());
		}

		String sql = "";
		int num = 0;
		String supposeBillNo = billNo; //假设单据号，长度与真实单据号一致
		ArrayList<java.util.Map.Entry<Integer, Integer>> loc = new java.util.ArrayList<java.util.Map.Entry<Integer, Integer>>(); //流水号位置，流水号位数
		String lsh; //流水号设置码
		int lshIdx = -1; //流水号设置码所在位置
		java.util.Map<Integer, Integer>  map = new HashMap<Integer, Integer>();
		for (int i = 2; i < 9; i++)
		{
			lsh = "{LSH" + i + "}";

			if (!supposeBillNo.contains(lsh))
			{
				continue;
			}

			while (supposeBillNo.contains(lsh))
			{
				//查找流水号所在位置
				lshIdx = supposeBillNo.indexOf(lsh);
				//将找到的流水号码替换成假设的流水号
				supposeBillNo = (lshIdx == 0 ? "" : supposeBillNo.substring(0, lshIdx)) + StringHelper.padLeft("", i, '_') + (lshIdx + 6 < supposeBillNo.length() ? supposeBillNo.substring(lshIdx + 6) : "");
				//保存当前流程号所处位置，及流程号长度，以便之后使用替换成正确的流水号
				map.put(lshIdx, i);
			}
		}
		Iterator<Map.Entry<Integer, Integer>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Integer, Integer> entry = iterator.next();
			loc.add(entry);
		}
		//数据库中查找符合的单据号集合,NOTE:此处需要注意，在LIKE中带有左广方括号时，要使用一对广播号将其转义
		sql = "SELECT BillNo FROM Frm_GenerBill WHERE BillNo LIKE '" + supposeBillNo.replace("[", "[[]") + "'" + " AND WorkID <> " + workid + " AND FrmID ='" + frmID + "' " + " ORDER BY BillNo DESC ";

		String maxBillNo = DBAccess.RunSQLReturnString(sql);
		int ilsh = 0;

		if (DataType.IsNullOrEmpty(maxBillNo))
		{
			//没有数据，则所有流水号都从1开始
			for (java.util.Map.Entry<Integer, Integer> kv : loc)
			{
				supposeBillNo = (kv.getKey() == 0 ? "" : supposeBillNo.substring(0, kv.getKey())) + StringHelper.padLeft("1", kv.getValue(), '0') + (kv.getKey() + kv.getValue() < supposeBillNo.length() ? supposeBillNo.substring(kv.getKey() + kv.getValue()) : "");
			}
		}
		else
		{
			//有数据，则从右向左开始判断流水号，当右侧的流水号达到最大值，则左侧的流水号自动加1
			HashMap<Integer, Integer> mlsh = new HashMap<Integer, Integer>();
			int plus1idx = -1;

			for (int i = loc.size() - 1; i >= 0; i--)
			{
				//获取单据号中当前位的流水码数
				ilsh = Integer.parseInt(StringHelper.substring(maxBillNo, loc.get(i).getKey(), loc.get(i).getValue()));

				if (plus1idx >= 0)
				{
					//如果当前码位被置为+1，则+1，同时将标识置为-1
					ilsh++;
					plus1idx = -1;
				}
				else
				{
					mlsh.put(loc.get(i).getKey(), i == loc.size() - 1 ? ilsh + 1 : ilsh);
					continue;
				}

				if (ilsh >= Integer.parseInt(StringHelper.padLeft("", loc.get(i).getValue(), '9')))
				{
					//右侧已经达到最大值
					if (i > 0)
					{
						//记录前位的码
						mlsh.put(loc.get(i).getKey(), 1);
					}
					else
					{
						supposeBillNo = "单据号超出范围";
						break;
					}

					//则将前一个流水码位，标记为+1
					plus1idx = i - 1;
				}
				else
				{
					mlsh.put(loc.get(i).getKey(), ilsh + 1);
				}
			}

			if (supposeBillNo.equals("单据号超出范围"))
			{
				return supposeBillNo;
			}

			//拼接单据号
			for (Map.Entry<Integer, Integer> kv : loc)
			{
				supposeBillNo = (kv.getKey() == 0 ? "" : supposeBillNo.substring(0, kv.getKey())) + StringHelper.padLeft(mlsh.get(kv.getKey()).toString(), kv.getValue(), '0') + (kv.getKey() + kv.getValue() < supposeBillNo.length() ? supposeBillNo.substring(kv.getKey() + kv.getValue()) : "");
			}
		}

		billNo = supposeBillNo;

		return billNo;
	}
}