package BP.Frm;

import BP.En.*;
import BP.DA.*;
import BP.Web.*;
import BP.Sys.*;
import BP.Tools.DateUtils;
import BP.Tools.StringHelper;

import java.util.*;
import java.util.Map.Entry;

/** 
 接口调用
*/
public class Dev2Interface
{
	/** 
	 创建工作ID
	 @param frmID 表单ID
	 @param userNo 用户编号
	 @param htParas 参数
	 @return 一个新的WorkID
	 * @throws Exception 
	*/
	public static long CreateBlankBillID(String frmID, String userNo, Hashtable htParas) throws Exception{
		return CreateBlankBillID(frmID, userNo,htParas,null);
	}

	/**
	 * 创建工作ID
	 * @param frmID
	 * @param userNo
	 * @param htParas
	 * @param billNo
	 * @return
	 * @throws Exception
	 */
	public static long CreateBlankBillID(String frmID, String userNo, Hashtable htParas,String billNo) throws Exception
	{
		GenerBill gb = new GenerBill();

		   if (DataType.IsNullOrEmpty(billNo) == true)
           {
               int i = gb.Retrieve(GenerBillAttr.FrmID, frmID, GenerBillAttr.Starter, userNo, GenerBillAttr.BillState, 0);
               if (i == 1)
                   return gb.getWorkID();
           }else
           {
               int i = gb.Retrieve(GenerBillAttr.FrmID, frmID, GenerBillAttr.BillNo, billNo);
               if (i == 1)
                   return gb.getWorkID();
           }

		FrmBill fb = new FrmBill(frmID);

		gb.setWorkID(BP.DA.DBAccess.GenerOID("WorkID"));
		gb.setBillState(BillState.None); //初始化状态.
		gb.setStarter(WebUser.getNo());
		gb.setStarterName(WebUser.getName());
		gb.setFrmName(fb.getName()); //单据名称.
		gb.setFrmID(fb.getNo()); //单据ID
		
		  if (DataType.IsNullOrEmpty(billNo) == false)
              gb.setBillNo(billNo) ; //BillNo

		gb.setFK_FrmTree(fb.getFK_FormTree()); //单据类别.
		gb.setRDT(BP.DA.DataType.getCurrentDataTime());
		gb.setNDStep(1);
		gb.setNDStepName("启动");

		//创建rpt.
		BP.WF.Data.GERpt rpt = new BP.WF.Data.GERpt(frmID);

		//设置标题.
		if (fb.getEntityType() == EntityType.FrmBill)
		{
			gb.setTitle(Dev2Interface.GenerTitle(fb.getTitleRole(), rpt));
			if(DataType.IsNullOrEmpty(billNo) == false)
				gb.setBillNo(billNo);
			else
				gb.setBillNo(BP.Frm.Dev2Interface.GenerBillNo(fb.getBillNoFormat(), gb.getWorkID(), null, frmID));
		}

		if (fb.getEntityType() == EntityType.EntityTree || fb.getEntityType() == EntityType.FrmDict)
		{
			rpt.getEnMap().setCodeStruct(fb.getEnMap().getCodeStruct());
			if(DataType.IsNullOrEmpty(billNo) == false)
				gb.setBillNo(billNo);
			else
				gb.setBillNo(rpt.GenerNewNoByKey("BillNo"));
			gb.setTitle("");
		}

		gb.DirectInsert(); //执行插入.

		//更新基础的数据到表单表.
		// rpt = new BP.WF.Data.GERpt(frmID);
		rpt.SetValByKey("BillState", gb.getBillState().getValue());
		rpt.SetValByKey("Starter", gb.getStarter());
		rpt.SetValByKey("StarterName", gb.getStarterName());
		rpt.SetValByKey("RDT", gb.getRDT());
		rpt.SetValByKey("Title", gb.getTitle());
		rpt.SetValByKey("BillNo", gb.getBillNo());
		rpt.setOID(gb.getWorkID());
		rpt.InsertAsOID(gb.getWorkID());

		return gb.getWorkID();
	}
	public static long CreateBlankDictID(String frmID, String userNo, Hashtable htParas) throws Exception{
		return CreateBlankDictID(frmID,userNo,htParas,null);
	}
	public static long CreateBlankDictID(String frmID, String userNo, Hashtable htParas,String billNo) throws Exception
	{

		FrmBill fb = new FrmBill(frmID);

		//创建rpt.
		BP.WF.Data.GERpt rpt = new BP.WF.Data.GERpt(frmID);

		int i = rpt.Retrieve("Starter", WebUser.getNo(), "BillState", 0);
		if (i >= 1)
		{
			rpt.SetValByKey("RDT", DataType.getCurrentDate());
			rpt.Update();
			return rpt.getOID();
		}

		//更新基础的数据到表单表.
		rpt.SetValByKey("BillState", 0);
		rpt.SetValByKey("Starter", WebUser.getNo());
		rpt.SetValByKey("StarterName", WebUser.getName());
		rpt.SetValByKey("RDT", DataType.getCurrentDate());

		rpt.getEnMap().setCodeStruct(fb.getEnMap().getCodeStruct());
		if(DataType.IsNullOrEmpty(billNo) == false)
			rpt.SetValByKey("BillNo", billNo);
		else
			rpt.SetValByKey("BillNo", rpt.GenerNewNoByKey("BillNo"));
		rpt.setOID(DBAccess.GenerOID(frmID));
		rpt.InsertAsOID(rpt.getOID());
		return rpt.getOID();
	}

	/** 
	 保存
	 
	 @param frmID 表单ID
	 @param workID 工作ID
	 @return 返回保存结果
	 * @throws Exception 
	*/
	public static String SaveWork(String frmID, long workID) throws Exception
	{
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
		BP.WF.Data.GERpt rpt = new BP.WF.Data.GERpt(gb.getFrmID(), workID);

		if (fb.getEntityType() == EntityType.EntityTree || fb.getEntityType() == EntityType.FrmDict)
		{

			gb.setTitle(rpt.getTitle());
			gb.Update();
			return "保存成功...";
		}

		//单据编号.
		if (DataType.IsNullOrEmpty(gb.getBillNo()) == true && !(fb.getEntityType() == EntityType.EntityTree || fb.getEntityType() == EntityType.FrmDict))
		{
			gb.setBillNo(BP.Frm.Dev2Interface.GenerBillNo(fb.getBillNoFormat(), workID, null, fb.getPTable()));
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

		return "保存成功...";
	}
	/**
	 提交

	 @param frmID 表单ID
	 @param workID 工作ID
	 @return 返回保存结果
	  * @throws Exception
	 */
	public static String SubmitWork(String frmID, long workID) throws Exception
	{
		FrmBill fb = new FrmBill(frmID);

		GenerBill gb = new GenerBill();
		gb.setWorkID(workID);
		int i = gb.RetrieveFromDBSources();
		if (i == 0)
		{
			return "";
		}
		gb.setBillState(BillState.Over);

		//创建rpt.
		BP.WF.Data.GERpt rpt = new BP.WF.Data.GERpt(gb.getFrmID(), workID);

		if (fb.getEntityType() == EntityType.EntityTree || fb.getEntityType() == EntityType.FrmDict)
		{

			gb.setTitle(rpt.getTitle());
			gb.Update();
			return "提交成功...";
		}

		//单据编号.
		if (DataType.IsNullOrEmpty(gb.getBillNo()) == true && !(fb.getEntityType() == EntityType.EntityTree || fb.getEntityType() == EntityType.FrmDict))
		{
			gb.setBillNo(BP.Frm.Dev2Interface.GenerBillNo(fb.getBillNoFormat(), workID, null, fb.getPTable()));
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

		return "提交成功...";
	}
	/** 
	 保存
	 
	 @param frmID 表单ID
	 @param workID 工作ID
	 @return 返回保存结果
	 * @throws Exception 
	*/
	public static String SaveAsDraft(String frmID, long workID) throws Exception
	{
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
	 
	 @param frmID
	 @param workID
	 @return 
	 * @throws Exception 
	*/
	public static String MyBill_Delete(String frmID, long workID) throws Exception
	{
		FrmBill fb = new FrmBill(frmID);
		String sqls = "DELETE FROM Frm_GenerBill WHERE WorkID=" + workID;
		sqls += "@DELETE FROM " + fb.getPTable() + " WHERE OID=" + workID;
		DBAccess.RunSQLs(sqls);
		return "删除成功.";
	}


	/** 
	 删除实体
	 
	 @param frmID
	 @param workID
	 @return 
	 * @throws Exception 
	*/
	public static String MyDict_Delete(String frmID, long workID) throws Exception
	{
		FrmBill fb = new FrmBill(frmID);
		String sql = "@DELETE FROM " + fb.getPTable() + " WHERE OID=" + workID;
		DBAccess.RunSQLs(sql);
		return "删除成功.";
	}


	/** 
	 删除实体单据
	 
	 @param frmID
	 @param workIds
	 @return 
	 * @throws Exception 
	*/
	public static String MyBill_DeleteDicts(String frmID, String workIds) throws Exception
	{
		FrmBill fb = new FrmBill(frmID);
		String sql = "DELETE FROM " + fb.getPTable() + " WHERE OID in (" + workIds + ")";
		DBAccess.RunSQLs(sql);
		return "删除成功.";
	}
	/** 
	 删除树形结构的实体表单
	 
	 @param frmID
	 @param billNo
	 @return 
	 * @throws Exception 
	*/
	public static String MyEntityTree_Delete(String frmID, String billNo) throws Exception
	{
		FrmBill fb = new FrmBill(frmID);
		String sql = "DELETE FROM " + fb.getPTable() + " WHERE BillNo='" + billNo + "' OR ParentNo='" + billNo + "'";
		DBAccess.RunSQLs(sql);
		return "删除成功.";
	}

	/** 
	 复制单据数据
	 
	 @param frmID
	 @param workID
	 @return 
	 * @throws Exception 
	*/
	public static String MyBill_Copy(String frmID, long workID) throws Exception
	{
		//获取单据的属性
		FrmBill fb = new FrmBill(frmID);

		GenerBill gb = new GenerBill();
		gb.setWorkID(BP.DA.DBAccess.GenerOID("WorkID"));
		gb.setBillState(BillState.None); //初始化状态.
		gb.setStarter(WebUser.getNo());
		gb.setStarterName(WebUser.getName());
		gb.setFrmName(fb.getName()); //单据名称.
		gb.setFrmID(fb.getNo()); //单据ID

		gb.setFK_FrmTree(fb.getFK_FormTree()); //单据类别.
		gb.setRDT(BP.DA.DataType.getCurrentDataTime());
		gb.setNDStep(1);
		gb.setNDStepName("启动");

		//创建rpt.
		BP.WF.Data.GERpt rpt = new BP.WF.Data.GERpt(frmID, workID);

		//设置标题.
		gb.setTitle(Dev2Interface.GenerTitle(fb.getTitleRole(), rpt));
		gb.setBillNo(BP.Frm.Dev2Interface.GenerBillNo(fb.getBillNoFormat(), gb.getWorkID(), null, frmID));

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
		if (dtls.size() > 0)
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
				dtlsFromData.Retrieve(GEDtlAttr.RefPK, workID);
				for (GEDtl geDtlFromData : dtlsFromData.ToJavaList())
				{
					//是否启用多附件
					FrmAttachmentDBs dbs = null;
					if (dtl.getIsEnableAthM() == true)
					{
						//根据从表的OID 获取附件信息
						dbs = new FrmAttachmentDBs();
						dbs.Retrieve(FrmAttachmentDBAttr.RefPKVal, geDtlFromData.getOID());
					}

					dtlData.Copy(geDtlFromData);
					dtlData.setRefPK( String.valueOf(rpt.getOID()));
					dtlData.InsertAsNew();
					if (dbs != null && dbs.size() != 0)
					{
						//复制附件信息
						FrmAttachmentDB newDB = new FrmAttachmentDB();
						for (FrmAttachmentDB db : dbs.ToJavaList())
						{
							newDB.Copy(db);
							newDB.setRefPKVal( String.valueOf(dtlData.getOID()));
							newDB.setFID( dtlData.getOID());
							newDB.setMyPK( BP.DA.DBAccess.GenerGUID());
							newDB.Insert();
						}
					}

				}
			}

		}

		//获取附件组件、
		FrmAttachments athDecs = new FrmAttachments(frmID);
		//复制附件数据。
		if (athDecs.size() > 0)
		{
			for (FrmAttachment athDec : athDecs.ToJavaList())
			{
				FrmAttachmentDBs aths = new FrmAttachmentDBs();
				aths.Retrieve(FrmAttachmentDBAttr.FK_FrmAttachment, athDec.getMyPK(),FrmAttachmentDBAttr.RefPKVal, workID);
				for (FrmAttachmentDB athDB : aths.ToJavaList())
				{
					FrmAttachmentDB athDB_N = new FrmAttachmentDB();
					athDB_N.Copy(athDB);
					athDB_N.setRefPKVal( String.valueOf(rpt.getOID()));
					athDB_N.setMyPK( BP.DA.DBAccess.GenerGUID());
					athDB_N.Insert();
				}
			}
		}
			///#endregion 复制表单其他数据.

		return "复制成功.";
	}

	/** 
	 获得发起列表
	 
	 @param empID
	 @return 
	 * @throws Exception 
	*/
	public static DataSet DB_StartBills(String empID) throws Exception
	{
		//定义容器.
		DataSet ds = new DataSet();

		//单据类别.
		BP.Sys.FrmTrees ens = new BP.Sys.FrmTrees();
		ens.RetrieveAll();

		DataTable dtSort = ens.ToDataTableField("Sort");
		dtSort.TableName = "Sort";
		ds.Tables.add(dtSort);

		//查询出来单据运行模式的.
		FrmBills bills = new FrmBills();
		bills.RetrieveAll();

		DataTable dtStart = bills.ToDataTableField();
		dtStart.TableName = "Start";
		ds.Tables.add(dtStart);
		return ds;
	}
	/** 
	 获得待办列表
	 
	 @param empID
	 @return 
	*/
	public static DataTable DB_Todolist(String empID)
	{
		return new DataTable();
	}
	/** 
	 草稿列表
	 
	 @param frmID 单据ID
	 @param empID 操作员
	 @return 
	 * @throws Exception 
	*/
	public static DataTable DB_Draft(String frmID, String empID) throws Exception
	{
		if (DataType.IsNullOrEmpty(empID) == true)
		{
			empID = WebUser.getNo();
		}

		GenerBills bills = new GenerBills();
		bills.Retrieve(GenerBillAttr.FrmID, frmID, GenerBillAttr.Starter, empID);

		return bills.ToDataTableField();
	}

	public static String GenerTitle(String titleRole, Entity wk) throws Exception
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
		titleRole = titleRole.replace("@RDT", DateUtils.format(new Date(),"yy年MM月dd日HH时mm分"));
		if (titleRole.contains("@"))
		{
			Attrs attrs = wk.getEnMap().getAttrs();

			// 优先考虑外键的替换,因为外键文本的字段的长度相对较长。
			for (Attr attr : attrs)
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
			for (Attr attr : attrs)
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
	 
	 @param billNo 单据编号规则
	 @param workid 工作ID
	 @param en 实体类
	 @param frmID 表单ID
	 @return 生成的单据编号
	 * @throws Exception 
	*/
	public static String GenerBillNo(String billNo, long workid, Entity en, String frmID) throws Exception
	{
		if (DataType.IsNullOrEmpty(billNo))
		{
			billNo = "3";
		}

		if (billNo.contains("@"))
		{
			billNo = BP.WF.Glo.DealExp(billNo, en, null);
		}

		/*如果，Bill 有规则 */  
		billNo = billNo.replace("{YYYY}", DateUtils.format(new Date(),"yyyy"));
		billNo = billNo.replace("{yyyy}", DateUtils.format(new Date(),"yyyy"));

		billNo = billNo.replace("{yy}", DateUtils.format(new Date(),"yy"));
		billNo = billNo.replace("{YY}", DateUtils.format(new Date(),"yy"));

		billNo = billNo.replace("{MM}", DateUtils.format(new Date(),"MM"));
		billNo = billNo.replace("{mm}", DateUtils.format(new Date(),"MM"));

		billNo = billNo.replace("{DD}",DateUtils.format(new Date(), "dd"));
		billNo = billNo.replace("{dd}",DateUtils.format(new Date(), "dd"));
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
		
		Iterator<Entry<Integer, Integer>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Integer, Integer> entry = iterator.next();
			loc.add(entry);
		}


		//数据库中查找符合的单据号集合,NOTE:此处需要注意，在LIKE中带有左广方括号时，要使用一对广播号将其转义
		sql = "SELECT BillNo FROM Frm_GenerBill WHERE BillNo LIKE '" + supposeBillNo.replace("[", "[[]") + "'"
			+ " AND WorkID <> " + workid + " AND FrmID ='" + frmID + "' "
			+ " ORDER BY BillNo DESC ";

		String maxBillNo = DBAccess.RunSQLReturnString(sql);
		int ilsh = 0;

		if (DataType.IsNullOrEmpty(maxBillNo))
		{
			//没有数据，则所有流水号都从1开始
			for (java.util.Map.Entry<Integer, Integer> kv : loc)
			{
				supposeBillNo = (kv.getKey() == 0 ? "" : supposeBillNo.substring(0, kv.getKey())) + StringHelper.padLeft("1", kv.getValue(), '0') + (kv.getKey() + kv.getValue() + 1 < supposeBillNo.length() ? supposeBillNo.substring(kv.getKey() + kv.getValue()) : "");
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
			for (java.util.Map.Entry<Integer, Integer> kv : loc)
			{
				supposeBillNo = (kv.getKey() == 0 ? "" : supposeBillNo.substring(0, kv.getKey())) + StringHelper.padLeft(mlsh.get(kv.getKey()).toString(), kv.getValue(), '0') + (kv.getKey() + kv.getValue() + 1 < supposeBillNo.length() ? supposeBillNo.substring(kv.getKey() + kv.getValue()) : "");
			}
		}

		billNo = supposeBillNo;

		return billNo;
	}
}