package BP.WF.HttpHandler;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.http.protocol.HttpContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import BP.DA.AtPara;
import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.Sys.AthSaveWay;
import BP.Sys.FrmAttachment;
import BP.Sys.FrmAttachmentDB;
import BP.Sys.FrmAttachmentDBAttr;
import BP.Sys.FrmEventList;
import BP.Sys.FrmEventListDtl;
import BP.Sys.FrmEvents;
import BP.Sys.FrmSubFlowAttr;
import BP.Sys.FrmType;
import BP.Sys.FrmWorkCheckAttr;
import BP.Sys.GEDtl;
import BP.Sys.GEDtlAttr;
import BP.Sys.GEDtls;
import BP.Sys.GEEntity;
import BP.Sys.M2M;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.MapDtl;
import BP.Sys.MapDtls;
import BP.Sys.MapExt;
import BP.Sys.MapExtAttr;
import BP.Sys.MapExtXmlList;
import BP.Sys.MapExts;
import BP.Sys.PopValWorkModel;
import BP.Tools.FileAccess;
import BP.Tools.StringHelper;
import BP.WF.DotNetToJavaStringHelper;
import BP.WF.Node;
import BP.WF.NodeFormType;
import BP.WF.HttpHandler.Base.WebContralBase;
import BP.WF.Template.FTCAttr;
import BP.WF.Template.FrmNode;
import BP.WF.Template.FrmNodeAttr;
import BP.WF.Template.FrmNodeComponent;
import BP.WF.Template.FrmThreadAttr;
import BP.WF.Template.FrmTrackAttr;
import BP.WF.XML.EventListDtlList;
import BP.Web.WebUser;
import cn.jflow.common.util.ContextHolderUtils;

public class WF_CCForm extends WebContralBase {
	/**
	 * 初始化函数
	 * 
	 * @param mycontext
	 */
	public WF_CCForm(HttpContext mycontext) {
		this.context = mycontext;
	}

	public WF_CCForm() {

	}

	public String HandlerMapExt() throws UnsupportedEncodingException {

		String fk_mapExt = getRequest().getParameter("FK_MapExt");
		if (DotNetToJavaStringHelper.isNullOrEmpty(getRequest().getParameter("Key"))) {
			return "";
		}

		String oid = getRequest().getParameter("OID");
		String kvs = getRequest().getParameter("KVs");

		BP.Sys.MapExt me = new BP.Sys.MapExt(fk_mapExt);
		DataTable dt = null;
		String sql = "";
		String key = getRequest().getParameter("Key");
		key = URLDecoder.decode(key, "GB2312");
		key = key.trim();

		// key = "周";
		if (me.getExtType().equals(BP.Sys.MapExtXmlList.ActiveDDL)) { // 动态填充ddl。
			sql = this.DealSQL(me.getDocOfSQLDeal(), key);
			if (sql.contains("@") == true) {
				Enumeration keys = this.getRequest().getAttributeNames();
				while (keys.hasMoreElements()) {
					sql = sql.replace("@" + keys.nextElement().toString(),
							this.getRequest().getParameter(keys.nextElement().toString()));
				}

			}

			dt = BP.DA.DBAccess.RunSQLReturnTable(sql);

			return JSONTODT(dt);
		}

		if (me.getExtType().equals(BP.Sys.MapExtXmlList.AutoFullDLL)
				|| me.getExtType().equals(BP.Sys.MapExtXmlList.TBFullCtrl)
				|| me.getExtType().equals(BP.Sys.MapExtXmlList.DDLFullCtrl)) { // 填充下拉框
			// ORIGINAL LINE: case "ReqCtrl":
			if(getRequest().getParameter("DoTypeExt")!=null && StringUtils.isEmpty(getRequest().getParameter("DoTypeExt")) && getRequest().getParameter("DoTypeExt").equals("null")){
				if (getRequest().getParameter("DoTypeExt").equals("ReqCtrl")) {
					// 获取填充 ctrl 值的信息.
					sql = this.DealSQL(me.getDocOfSQLDeal(), key);
					HttpSession session = getRequest().getSession();
					session.setAttribute("DtlKey", key);
					dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
	
					return JSONTODT(dt);
				}
				// ORIGINAL LINE: case "ReqM2MFullList":
	
				if (getRequest().getParameter("DoTypeExt").equals("ReqM2MFullList")) {
					// 获取填充的M2m集合.
					DataTable dtM2M = new DataTable("Head");
					dtM2M.Columns.Add("Dtl", String.class);
					String[] strsM2M = me.getTag2().split("[$]", -1);
					for (String str : strsM2M) {
						if (str.equals("") || str == null) {
							continue;
						}
	
						String[] ss = str.split("[:]", -1);
						String noOfObj = ss[0];
						String mysql = ss[1];
						mysql = DealSQL(mysql, key);
	
						DataTable dtFull = DBAccess.RunSQLReturnTable(mysql);
						M2M m2mData = new M2M();
						m2mData.setFK_MapData(me.getFK_MapData());
						m2mData.setEnOID(Integer.parseInt(oid));
						m2mData.setM2MNo(noOfObj);
						String mystr = ",";
						String mystrT = "";
						for (DataRow dr : dtFull.Rows) {
							String myno = dr.getValue("No").toString();
							String myname = dr.getValue("Name").toString();
							mystr += myno + ",";
							mystrT += "@" + myno + "," + myname;
						}
						m2mData.setVals(mystr);
						m2mData.setValsName(mystrT);
						m2mData.InitMyPK();
						m2mData.setNumSelected(dtFull.Rows.size());
						m2mData.Save();
	
						DataRow mydr = dtM2M.NewRow();
						mydr.setValue(0, ss[0]);
						dtM2M.Rows.add(mydr);
					}
					return JSONTODT(dtM2M);
	
				}
				// ORIGINAL LINE: case "ReqDtlFullList":
	
				if (getRequest().getParameter("DoTypeExt").equals("ReqDtlFullList")) {
					// 获取填充的从表集合.
					DataTable dtDtl = new DataTable("Head");
					dtDtl.Columns.Add("Dtl", String.class);
					String[] strsDtl = me.getTag1().split("[$]", -1);
					for (String str : strsDtl) {
						if (DotNetToJavaStringHelper.isNullOrEmpty(str)) {
							continue;
						}
	
						String[] ss = str.split("[:]", -1);
						String fk_dtl = ss[0];
						String dtlKey = (String) ((getRequest().getSession().getAttribute("DtlKey") instanceof String)
								? getRequest().getSession().getAttribute("DtlKey") : null);
						if (dtlKey == null) {
							dtlKey = key;
						}
						String mysql = DealSQL(ss[1], dtlKey);
	
						GEDtls dtls = new GEDtls(fk_dtl);
						MapDtl dtl = new MapDtl(fk_dtl);
	
						DataTable dtDtlFull = DBAccess.RunSQLReturnTable(mysql);
						BP.DA.DBAccess.RunSQL("DELETE FROM " + dtl.getPTable() + " WHERE RefPK=" + oid);
						for (DataRow dr : dtDtlFull.Rows) {
							BP.Sys.GEDtl mydtl = new GEDtl(fk_dtl);
							// mydtl.OID = dtls.Count + 1;
							dtls.AddEntity(mydtl);
							for (DataColumn dc : dtDtlFull.Columns) {
								mydtl.SetValByKey(dc.ColumnName, dr.getValue(dc.ColumnName).toString());
							}
							mydtl.setRefPKInt(Integer.parseInt(oid));
							if (mydtl.getOID() > 100) {
								mydtl.InsertAsOID(mydtl.getOID());
							} else {
								mydtl.setOID(0);
								mydtl.Insert();
							}
	
						}
						DataRow drRe = dtDtl.NewRow();
						drRe.setValue(0, fk_dtl);
						dtDtl.Rows.add(drRe);
					}
					return JSONTODT(dtDtl);
				}
				// ORIGINAL LINE: case "ReqDDLFullList":
	
				if (getRequest().getParameter("DoTypeExt").equals("ReqDDLFullList")) {
					// 获取要个性化填充的下拉框.
					DataTable dt1 = new DataTable("Head");
					dt1.Columns.Add("DDL", String.class);
					// dt1.Columns.Add("SQL", typeof(string));
					String[] strs = me.getTag().split("[$]", -1);
					for (String str : strs) {
						if (str.equals("") || str == null) {
							continue;
						}
	
						String[] ss = str.split("[:]", -1);
						DataRow dr = dt1.NewRow();
						dr.setValue(0, ss[0]);
						// dr[1] = ss[1];
						dt1.Rows.add(dr);
					}
					return JSONTODT(dt);
				}
				// ORIGINAL LINE: case "ReqDDLFullListDB":
	
				if (getRequest().getParameter("DoTypeExt").equals("ReqDDLFullListDB")) {
					// 获取要个性化填充的下拉框的值. 根据已经传递过来的 ddl id.
					String myDDL = getRequest().getParameter("MyDDL");
					sql = me.getDocOfSQLDeal();
					String[] strs1 = me.getTag().split("[$]", -1);
					for (String str : strs1) {
						if (str.equals("") || str == null) {
							continue;
						}
	
						String[] ss = str.split("[:]", -1);
						if (myDDL.equals(ss[0]) && ss.length == 2) {
							sql = ss[1];
							sql = this.DealSQL(sql, key);
							break;
						}
					}
					dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
					return JSONTODT(dt);
				} 
			}else{
				sql = this.DealSQL(me.getDocOfSQLDeal(), key);
				dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
				return JSONTODT(dt);
			}
				return "";
		}

		return "err@没有解析的标记" + me.getExtType();

	}

	public String Frm_Init() {

		MapData md = new MapData(this.getEnsName());

		// /#region 判断是否是返回的URL.
		if (md.getHisFrmType() == FrmType.Url) {
			String no = this.GetRequestVal("NO");
			String urlParas = "OID=" + this.getRefOID() + "&NO=" + no + "&WorkID=" + this.getWorkID() + "&FK_Node="
					+ this.getFK_Node() + "&UserNo=" + WebUser.getNo() + "&SID=" + this.getSID();

			String url = "";
			// 如果是URL.
			if (md.getUrl().contains("?") == true) {
				url = md.getUrl() + "&" + urlParas;
			} else {
				url = md.getUrl() + "?" + urlParas;
			}

			return "url@" + url;
		}

		if (md.getHisFrmType() == FrmType.VSTOForExcel && this.GetRequestVal("IsFreeFrm") == null) {
			String url = "FrmVSTO.jsp?1=1&" + this.getRequestParas();
			return "url@" + url;
		}

		if (md.getHisFrmType() == FrmType.WordFrm) {
			String no = this.GetRequestVal("NO");
			String urlParas = "OID=" + this.getRefOID() + "&NO=" + no + "&WorkID=" + this.getWorkID() + "&FK_Node="
					+ this.getFK_Node() + "&UserNo=" + WebUser.getNo() + "&SID=" + this.getSID() + "&FK_MapData="
					+ this.getFK_MapData() + "&OIDPKVal=" + this.getOID() + "&FID=" + this.getFID() + "&FK_Flow="
					+ this.getFK_Flow();
			// 如果是URL.
			String requestParas = this.getRequestParas();
			String[] parasArrary = this.getRequestParas().split("[&]", -1);
			for (String str : parasArrary) {
				if (DotNetToJavaStringHelper.isNullOrEmpty(str) || str.contains("=") == false) {
					continue;
				}
				String[] kvs = str.split("[=]", -1);
				if (urlParas.contains(kvs[0])) {
					continue;
				}
				urlParas += "&" + kvs[0] + "=" + kvs[1];
			}
			if (md.getUrl().contains("?") == true) {
				return "url@FrmWord.jsp?1=2" + "&" + urlParas;
			} else {
				return "url@FrmWord.jsp" + "?" + urlParas;
			}
		}

		if (md.getHisFrmType() == FrmType.ExcelFrm) {
			return "url@FrmExcel.jsp?1=2" + this.getRequestParas();
		}

		// /#endregion 判断是否是返回的URL.
		return "url@FrmGener.htm?1=2" + this.getRequestParas();

	}

	public final String DtlFrm_Init() {
		long pk = this.getRefOID();
		if (pk == 0) {
			pk = Integer.parseInt(this.getOID());
		}
		if (pk == 0) {
			pk = this.getWorkID();
		}

		if (pk != 0) {
			return FrmGener_Init();
		}

		GEEntity en = new GEEntity(this.getEnsName());
		if (BP.Sys.SystemConfig.getIsBSsystem() == true) {

			// 处理传递过来的参数。
			for (String k : BP.Sys.Glo.getQueryStringKeys()) {
				en.SetValByKey(k, this.GetRequestVal(k));
			}
		}

		// 设置主键.
		en.setOID(DBAccess.GenerOID(this.getEnsName()));
		en.SetValByKey("RefPK", this.getRefPKVal());
		en.Insert();

		return "url@DtlFrm.htm?EnsName=" + this.getEnsName() + "&RefPKVal=" + this.getRefPKVal() + "&OID="
				+ en.getOID();
	}

	public final String DtlFrm_Delete() {
		try {
			GEEntity en = new GEEntity(this.getEnsName());
			en.setOID(Long.parseLong(this.getOID()));
			en.Delete();

			return "删除成功.";
		} catch (RuntimeException ex) {
			return "err@删除错误:" + ex.getMessage();
		}
	}

	public String FrmGener_Init() {

		long pk = this.getRefOID();
		if (pk == 0) {
			if(!StringUtils.isEmpty(this.getOID()))
				pk = Long.parseLong(this.getOID());
		}
		if (pk == 0) {
			pk = this.getWorkID();
		}

		// 清空缓存。
		if (pk == 0) {
			BP.DA.Cash.ClearCash();
		}

		MapData md = new MapData(this.getEnsName());
		DataSet ds = BP.Sys.CCFormAPI.GenerHisDataSet_2017(md.getNo());

		// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		// /#region 把主表数据放入.
		String atParas = "";
		// 主表实体.
		GEEntity en = new GEEntity(this.getEnsName());

		// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		// /#region 求出 who is pk 值.

		if (this.getFK_Node() != 0 && DotNetToJavaStringHelper.isNullOrEmpty(this.getFK_Flow()) == false) {
			// 说明是流程调用它， 就要判断谁是表单的PK.
			FrmNode fn = new FrmNode(this.getFK_Flow(), this.getFK_Node(), this.getFK_MapData());
			switch (fn.getWhoIsPK()) {
			case FID:
				pk = this.getFID();
				if (pk == 0) {
					throw new RuntimeException("@没有接收到参数FID");
				}
				break;
			case PWorkID: // 父流程ID
				pk = this.getPWorkID();
				if (pk == 0) {
					throw new RuntimeException("@没有接收到参数PWorkID");
				}
				break;
			case OID:
			default:
				break;
			}
		}
		// /#endregion 求who is PK.

		en.setOID(pk);

		if (en.getOID() != 0) {
			if (en.RetrieveFromDBSources() == 0)
				en.Insert();
		}

		if (en.getOID() == 0) {
			en.CheckPhysicsTable();
			en.ResetDefaultVal();
		}

		// 把参数放入到 En 的 Row 里面。
		if (DotNetToJavaStringHelper.isNullOrEmpty(atParas) == false) {
			AtPara ap = new AtPara(atParas);
			for (String key : ap.getHisHT().keySet()) {
				if (en.getRow().containsKey(key) == true) // 有就该变.
				{
					en.getRow().SetValByKey(key, ap.GetValStrByKey(key));
				} else {
					en.getRow().put(key, ap.GetValStrByKey(key)); // 增加他.
				}
			}
		}

		if (BP.Sys.SystemConfig.getIsBSsystem() == true) {

			String strs = "OID,DoType,E,";
			// 处理传递过来的参数。
			Enumeration<?> keys = this.getRequest().getParameterNames();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement().toString();

				en.SetValByKey(key, this.getRequest().getParameter(key));
			}
		}

		// 执行表单事件..
		String msg = md.getFrmEvents().DoEventNode(FrmEventList.FrmLoadBefore, en);
		if (DotNetToJavaStringHelper.isNullOrEmpty(msg) == false) {
			throw new RuntimeException("err@错误:" + msg);
		}

		String json = en.ToJson();

		// 重设默认值.
		en.ResetDefaultVal();

		json = en.ToJson();

		// 执行装载填充.
		MapExt me = new MapExt();
		me.setMyPK(this.getEnsName() + "_" + MapExtXmlList.PageLoadFull);
		if (me.RetrieveFromDBSources() == 1) {
			// 执行通用的装载方法.
			MapAttrs attrs = new MapAttrs(this.getEnsName());
			MapDtls dtls = new MapDtls(this.getEnsName());
			Object tempVar = BP.WF.Glo.DealPageLoadFull(en, me, attrs, dtls);
			en = (GEEntity) ((tempVar instanceof GEEntity) ? tempVar : null);
		}

		json = en.ToJson();

		// 增加主表数据.
		DataTable mainTable = en.ToDataTableField("MainTable");

		json = en.ToJson();

		ds.Tables.add(mainTable);
		// /#endregion 把主表数据放入.
		
		///#region 把外键表加入DataSet
		DataTable dtMapAttr = null;
		for(int i=0;i<ds.Tables.size();i++){
			if(ds.Tables.get(i).TableName.equals("Sys_MapAttr")){
				dtMapAttr = ds.Tables.get(i);
			}
		}

		MapExts mes = md.getMapExts();

		for (DataRow dr : dtMapAttr.Rows)
		{
			String lgType = dr.getValue("LGType").toString();
			if (!lgType.equals("2"))
			{
				continue;
			}

			String UIIsEnable = dr.getValue("UIVisible").toString();
			if (UIIsEnable.equals("0"))
			{
				continue;
			}

			String uiBindKey = dr.getValue("UIBindKey").toString();
			if (DotNetToJavaStringHelper.isNullOrEmpty(uiBindKey) == true)
			{
				String myPK = dr.getValue("MyPK").toString();
				//如果是空的
				//   throw new Exception("@属性字段数据不完整，流程:" + fl.No + fl.Name + ",节点:" + nd.NodeID + nd.Name + ",属性:" + myPK + ",的UIBindKey IsNull ");
			}

			// 检查是否有下拉框自动填充。
			String keyOfEn = dr.getValue("KeyOfEn").toString();
			String fk_mapData = dr.getValue("FK_MapData").toString();

			///#region 处理下拉框数据范围. for 小杨.
			Object tempVar = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL, MapExtAttr.AttrOfOper, keyOfEn);
			me = (MapExt)((tempVar instanceof MapExt) ? tempVar : null);
			if (me != null)
			{
				Object tempVar2 = me.getDoc();
				String fullSQL = (String)((tempVar2 instanceof String) ? tempVar2 : null);
				fullSQL = fullSQL.replace("~", ",");
				fullSQL = BP.WF.Glo.DealExp(fullSQL, en, null);
				DataTable dt = DBAccess.RunSQLReturnTable(fullSQL);
				dt.TableName = keyOfEn; //可能存在隐患，如果多个字段，绑定同一个表，就存在这样的问题.
				ds.Tables.add(dt);
				continue;
			}
			///#endregion 处理下拉框数据范围.

			// 判断是否存在.
			if (ds.Tables.contains(uiBindKey) == true)
			{
				continue;
			}

			ds.Tables.add(BP.Sys.PubClass.GetDataTableByUIBineKey(uiBindKey));
		}

		// #region 加入组件的状态信息, 在解析表单的时候使用.
		if (this.getFK_Node() != 0 && this.getFK_Node() != 999999) {
			Node nd = new Node(this.getFK_Node());
			BP.WF.Template.FrmNodeComponent fnc = new FrmNodeComponent(nd.getNodeID());
			if (nd.getNodeFrmID().equals("ND" + nd.getNodeID()) == false
					&& nd.getHisFormType() != NodeFormType.RefOneFrmTree ) {
				/* 说明这是引用到了其他节点的表单，就需要把一些位置元素修改掉. */
				int refNodeID = Integer.parseInt(nd.getNodeFrmID().replace("ND", ""));

				BP.WF.Template.FrmNodeComponent refFnc = new FrmNodeComponent(refNodeID);

				fnc.SetValByKey(FrmWorkCheckAttr.FWC_H, refFnc.GetValFloatByKey(FrmWorkCheckAttr.FWC_H));
				fnc.SetValByKey(FrmWorkCheckAttr.FWC_W, refFnc.GetValFloatByKey(FrmWorkCheckAttr.FWC_W));
				fnc.SetValByKey(FrmWorkCheckAttr.FWC_X, refFnc.GetValFloatByKey(FrmWorkCheckAttr.FWC_X));
				fnc.SetValByKey(FrmWorkCheckAttr.FWC_Y, refFnc.GetValFloatByKey(FrmWorkCheckAttr.FWC_Y));

				fnc.SetValByKey(FrmSubFlowAttr.SF_H, refFnc.GetValFloatByKey(FrmSubFlowAttr.SF_H));
				fnc.SetValByKey(FrmSubFlowAttr.SF_W, refFnc.GetValFloatByKey(FrmSubFlowAttr.SF_W));
				fnc.SetValByKey(FrmSubFlowAttr.SF_X, refFnc.GetValFloatByKey(FrmSubFlowAttr.SF_X));
				fnc.SetValByKey(FrmSubFlowAttr.SF_Y, refFnc.GetValFloatByKey(FrmSubFlowAttr.SF_Y));

				fnc.SetValByKey(FrmThreadAttr.FrmThread_H, refFnc.GetValFloatByKey(FrmThreadAttr.FrmThread_H));
				fnc.SetValByKey(FrmThreadAttr.FrmThread_W, refFnc.GetValFloatByKey(FrmThreadAttr.FrmThread_W));
				fnc.SetValByKey(FrmThreadAttr.FrmThread_X, refFnc.GetValFloatByKey(FrmThreadAttr.FrmThread_X));
				fnc.SetValByKey(FrmThreadAttr.FrmThread_Y, refFnc.GetValFloatByKey(FrmThreadAttr.FrmThread_Y));

				fnc.SetValByKey(FrmTrackAttr.FrmTrack_H, refFnc.GetValFloatByKey(FrmTrackAttr.FrmTrack_H));
				fnc.SetValByKey(FrmTrackAttr.FrmTrack_W, refFnc.GetValFloatByKey(FrmTrackAttr.FrmTrack_W));
				fnc.SetValByKey(FrmTrackAttr.FrmTrack_X, refFnc.GetValFloatByKey(FrmTrackAttr.FrmTrack_X));
				fnc.SetValByKey(FrmTrackAttr.FrmTrack_Y, refFnc.GetValFloatByKey(FrmTrackAttr.FrmTrack_Y));

				fnc.SetValByKey(FTCAttr.FTC_H, refFnc.GetValFloatByKey(FTCAttr.FTC_H));
				fnc.SetValByKey(FTCAttr.FTC_W, refFnc.GetValFloatByKey(FTCAttr.FTC_W));
				fnc.SetValByKey(FTCAttr.FTC_X, refFnc.GetValFloatByKey(FTCAttr.FTC_X));
				fnc.SetValByKey(FTCAttr.FTC_Y, refFnc.GetValFloatByKey(FTCAttr.FTC_Y));
			}

			ds.Tables.add(fnc.ToDataTableField("WF_FrmNodeComponent"));

			DataTable dtNode = nd.ToDataTableField("WF_Node");
			dtNode.TableName = "WF_Node";

			ds.Tables.add(dtNode);
		}

		// #endregion 加入组件的状态信息, 在解析表单的时候使用.

		json = BP.Tools.Json.ToJson(ds);

		return json;

	}

	public String FrmGener_Save() {
		// 保存主表数据.
		GEEntity en = new GEEntity(this.getEnsName());
		en.setOID(this.getRefOID());
		int i = en.RetrieveFromDBSources();

		en.ResetDefaultVal();

		Object tempVar = BP.Sys.PubClass.CopyFromRequest(en, this.getRequest());
		en = (GEEntity) ((tempVar instanceof GEEntity) ? tempVar : null);

		en.setOID(this.getRefOID());
		if (i == 0) {
			en.Insert();
		} else {
			en.Update();
		}

		return "保存成功.";
	}

	public final String FrmFreeReadonly_Init() {
		try {
			MapData md = new MapData(this.getEnsName());
			DataSet ds = BP.Sys.CCFormAPI.GenerHisDataSet_2017(md.getNo());

			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			/// #region 把主表数据放入.
			String atParas = "";
			// 主表实体.
			GEEntity en = new GEEntity(this.getEnsName());

			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			/// #region 求出 who is pk 值.
			long pk = this.getRefOID();
			if (pk == 0) {
				pk = Long.parseLong(this.getOID());
			}

			if (pk == 0) {
				pk = this.getWorkID();
			}

			if (this.getFK_Node() != 0 && DotNetToJavaStringHelper.isNullOrEmpty(this.getFK_Flow()) == false) {
				// 说明是流程调用它， 就要判断谁是表单的PK.
				FrmNode fn = new FrmNode(this.getFK_Flow(), this.getFK_Node(), this.getFK_MapData());

				switch (fn.getWhoIsPK()) {
				case FID:
					pk = this.getFID();
					if (pk == 0) {
						throw new RuntimeException("@没有接收到参数FID");
					}
					break;
				case PWorkID: // 父流程ID
					pk = this.getPWorkID();
					if (pk == 0) {
						throw new RuntimeException("@没有接收到参数PWorkID");
					}
					break;
				case OID:
				default:
					break;
				}
			}

			/// #endregion 求who is PK.

			en.setOID(pk);
			en.RetrieveFromDBSources();

			// 增加主表数据.
			DataTable mainTable = en.ToDataTableField(md.getNo());
			mainTable.TableName = "MainTable";

			ds.Tables.add(mainTable);

			/// #endregion 把主表数据放入.

			return BP.Tools.Json.ToJson(ds);
		} catch (RuntimeException ex) {
			GEEntity myen = new GEEntity(this.getEnsName());
			myen.CheckPhysicsTable();

			// BP.Sys.CCFormAPI.RepareCCForm(this.getEnsName());
			return "err@装载表单期间出现如下错误,ccform有自动诊断修复功能请在刷新一次，如果仍然存在请联系管理员. @" + ex.getMessage();
		}
	}

	/**
	 * 初始化从表数据
	 * 
	 * @return 返回结果数据
	 */
	public final String Dtl_Init() {

		/// #region 检查是否是测试.
		if (this.GetRequestVal("IsTest") != null) {
			BP.Sys.GEDtl dtl = new GEDtl(this.getEnsName());
			dtl.CheckPhysicsTable();

			// MapDtl mdtl = new MapDtl(this.getEnsName());
			// BP.Sys.CCFormAPI.RepareCCForm();
		}

		/// #endregion

		/// #region 组织参数.
		MapDtl mdtl = new MapDtl(this.getEnsName());
		mdtl.setNo(this.getEnsName());
		mdtl.RetrieveFromDBSources();

		if (this.getFK_Node() != 0 && !mdtl.getFK_MapData().equals("Temp")
				&& this.getEnsName().contains("ND" + this.getFK_Node()) == false && this.getFK_Node()!=999999 ) {
			Node nd = new BP.WF.Node(this.getFK_Node());
			// 如果
			// * 1,传来节点ID, 不等于0.
			// * 2,不是节点表单. 就要判断是否是独立表单，如果是就要处理权限方案。

			BP.WF.Template.FrmNode fn = new BP.WF.Template.FrmNode(nd.getFK_Flow(), nd.getNodeID(),
					mdtl.getFK_MapData());
			int i = fn.Retrieve(FrmNodeAttr.FK_Frm, mdtl.getFK_MapData(), FrmNodeAttr.FK_Node, this.getFK_Node());
			if (i != 0 && fn.getFrmSln() > 1) {

				mdtl.setNo(this.getEnsName() + "_" + this.getFK_Node());
				mdtl.RetrieveFromDBSources();
			}

			if (i != 0 && fn.getFrmSln() == 1) {
				mdtl.setIsInsert(false);
				mdtl.setIsDelete(false);
				mdtl.setIsUpdate(false);
			}

		}

		if (this.GetRequestVal("IsReadonly").equals("1")) {
			mdtl.setIsInsert(false);
			mdtl.setIsDelete(false);
			mdtl.setIsUpdate(false);
		}

		String strs = this.getRequestParas();
		strs = strs.replace("?", "@");
		strs = strs.replace("&", "@");

		/// #endregion 组织参数.

		// 获得他的描述,与数据.
		DataSet ds = BP.WF.CCFormAPI.GenerDBForCCFormDtl(mdtl.getFK_MapData(), mdtl,
				Integer.parseInt(this.getRefPKVal()), strs);

		return BP.Tools.Json.ToJson(ds);
	}

	/**
	 * 执行从表的保存.
	 * 
	 * @return
	 */
	public final String Dtl_Save() {
		MapDtl mdtl = new MapDtl(this.getEnsName());
		GEDtls dtls = new GEDtls(this.getEnsName());
		FrmEvents fes = new FrmEvents(this.getEnsName()); // 获得事件.
		GEEntity mainEn = null;

		// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		/// #region 从表保存前处理事件.
		if (fes.size() > 0) {
			mainEn = mdtl.GenerGEMainEntity(this.getRefPKVal());
			String msg = fes.DoEventNode(EventListDtlList.DtlSaveBefore, mainEn);
			if (DataType.IsNullOrEmpty(msg) == false)
				return "err@" + msg;

		}
		// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		/// #endregion 从表保存前处理事件.

		// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		/// #region 保存的业务逻辑.

		// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		/// #endregion 保存的业务逻辑.

		return "保存成功";
	}

	/**
	 * 保存单行数据
	 * 
	 * @return
	 */
	public final String Dtl_SaveRow() {
		try {
			// 从表.
			MapDtl mdtl = new MapDtl(this.getFK_MapDtl());

			// 从表实体.
			GEDtl dtl = new GEDtl(this.getFK_MapDtl());
			int oid = this.getRefOID();
			if (oid != 0) {
				dtl.setOID(oid);
				dtl.RetrieveFromDBSources();
			}

			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			/// #region 给实体循环赋值/并保存.
			BP.En.Attrs attrs = dtl.getEnMap().getAttrs();
			for (BP.En.Attr attr : attrs.ToJavaList()) {
				dtl.SetValByKey(attr.getKey(), this.GetRequestVal(attr.getKey()));
			}

			// 关联主赋值.
			dtl.setRefPK(this.getRefPKVal());

			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			/// #region 从表保存前处理事件.
			// 获得主表事件.
			FrmEvents fes = new FrmEvents(this.getFK_MapData()); // 获得事件.
			GEEntity mainEn = null;
			if (fes.size() > 0) {
				mainEn = mdtl.GenerGEMainEntity(this.getRefPKVal());
				String msg = fes.DoEventNode(EventListDtlList.DtlSaveBefore, mainEn);
				if (DataType.IsNullOrEmpty(msg) == false)
					return "err@" + msg;
			}

			if (mdtl.getFEBD().length() != 0) {
				String str = mdtl.getFEBD();
				BP.Sys.FormEventBaseDtl febd = BP.Sys.Glo.GetFormDtlEventBaseByEnName(mdtl.getNo());

				febd.HisEn = mdtl.GenerGEMainEntity(this.getRefPKVal());
				febd.HisEnDtl = dtl;

				febd.DoIt(FrmEventListDtl.RowSaveBefore, febd.HisEn, dtl, null);
			}

			if (dtl.getOID() == 0) {
				// dtl.OID = DBAccess.GenerOID();
				dtl.Insert();
			} else {
				dtl.Update();
			}
			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			/// #endregion 给实体循环赋值/并保存.

			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			/// #region 从表保存后处理事件。
			if (fes.size() > 0) {
				String msg = fes.DoEventNode(EventListDtlList.DtlSaveEnd, mainEn);
				if (DataType.IsNullOrEmpty(msg) == false)
					return "err@" + msg;
			}

			if (mdtl.getFEBD().length() != 0) {
				String str = mdtl.getFEBD();
				BP.Sys.FormEventBaseDtl febd = BP.Sys.Glo.GetFormDtlEventBaseByEnName(mdtl.getNo());

				febd.HisEn = mdtl.GenerGEMainEntity(this.getRefPKVal());
				febd.HisEnDtl = dtl;

				febd.DoIt(FrmEventListDtl.RowSaveAfter, febd.HisEn, dtl, null);
			}
			// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			/// #endregion 处理事件.

			// 返回当前数据存储信息.
			return dtl.ToJson();
		} catch (Exception e) {
			return "err@" + e.getMessage();
		}

	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	public final String Dtl_DeleteRow() {
		GEDtl dtl = new GEDtl(this.getFK_MapDtl());
		dtl.setOID(this.getRefOID());
		dtl.Delete();
		return "删除成功";
	}

	/**
	 * 重新获取单个ddl数据
	 * 
	 * @return
	 */
	public final String Dtl_ReloadDdl() {
		String Doc = this.GetRequestVal("Doc");
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(Doc);
		dt.TableName = "ReloadDdl";
		return BP.Tools.Json.ToJson(dt);
	}

	public String InitPopValTree() {

		String mypk = getRequest().getParameter("FK_MapExt");

		MapExt me = new MapExt();
		me.setMyPK(mypk);
		me.Retrieve();

		// 获得配置信息.
		java.util.Hashtable ht = me.PopValToHashtable();
		DataTable dtcfg = BP.Sys.PubClass.HashtableToDataTable(ht);

		String parentNo = getRequest().getParameter("ParentNo");
		if (parentNo == null) {
			parentNo = me.getPopValTreeParentNo();
		}

		DataSet resultDs = new DataSet();
		String sqlObjs = me.getPopValTreeSQL();
		sqlObjs = sqlObjs.replace("@WebUser.getNo()", BP.Web.WebUser.getNo());
		sqlObjs = sqlObjs.replace("@WebUser.Name", BP.Web.WebUser.getName());
		sqlObjs = sqlObjs.replace("@WebUser.getFK_Dept()", BP.Web.WebUser.getFK_Dept());
		sqlObjs = sqlObjs.replace("@ParentNo", parentNo);
		sqlObjs = this.DealExpByFromVals(sqlObjs);

		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sqlObjs);
		dt.TableName = "DTObjs";
		resultDs.Tables.add(dt);

		// doubleTree
		if (me.getPopValWorkModel() == PopValWorkModel.TreeDouble.getValue()
				&& !parentNo.equals(me.getPopValTreeParentNo())) {
			sqlObjs = me.getPopValDoubleTreeEntitySQL();
			sqlObjs = sqlObjs.replace("@WebUser.getNo()", BP.Web.WebUser.getNo());
			sqlObjs = sqlObjs.replace("@WebUser.Name", BP.Web.WebUser.getName());
			sqlObjs = sqlObjs.replace("@WebUser.getFK_Dept()", BP.Web.WebUser.getFK_Dept());
			sqlObjs = sqlObjs.replace("@ParentNo", parentNo);
			sqlObjs = this.DealExpByFromVals(sqlObjs);

			DataTable entityDt = BP.DA.DBAccess.RunSQLReturnTable(sqlObjs);
			entityDt.TableName = "DTEntitys";
			resultDs.Tables.add(entityDt);
		}

		return BP.Tools.Json.ToJson(resultDs);
	}

	public String InitPopVal() {

		MapExt me = new MapExt();
		me.setMyPK(this.getFK_MapExt());
		me.Retrieve();

		// 数据对象，将要返回的.
		DataSet ds = new DataSet();

		// 获得配置信息.
		java.util.Hashtable ht = me.PopValToHashtable();
		DataTable dtcfg = BP.Sys.PubClass.HashtableToDataTable(ht);

		// 增加到数据源.
		ds.Tables.add(dtcfg);

		if (me.getPopValWorkModel() == PopValWorkModel.SelfUrl.getValue()) {
			return "@SelfUrl" + me.getPopValUrl();
		}

		if (me.getPopValWorkModel() == PopValWorkModel.TableOnly.getValue()) {
			String sqlObjs = me.getPopValEntitySQL();
			sqlObjs = sqlObjs.replace("@WebUser.getNo()", BP.Web.WebUser.getNo());
			sqlObjs = sqlObjs.replace("@WebUser.Name", BP.Web.WebUser.getName());
			sqlObjs = sqlObjs.replace("@WebUser.getFK_Dept()", BP.Web.WebUser.getFK_Dept());

			sqlObjs = this.DealExpByFromVals(sqlObjs);

			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sqlObjs);
			dt.TableName = "DTObjs";
			ds.Tables.add(dt);
			return BP.Tools.Json.ToJson(ds);
		}

		if (me.getPopValWorkModel() == PopValWorkModel.TablePage.getValue()) {
			// 分页的
			// key
			String key = getRequest().getParameter("Key");
			if (DotNetToJavaStringHelper.isNullOrEmpty(key) == true) {
				key = "";
			}

			// 取出来查询条件.
			String[] conds = me.getPopValSearchCond().split("[$]", -1);

			String countSQL = me.getPopValTablePageSQLCount();

			// 固定参数.
			countSQL = countSQL.replace("@WebUser.getNo()", BP.Web.WebUser.getNo());
			countSQL = countSQL.replace("@WebUser.Name", BP.Web.WebUser.getName());
			countSQL = countSQL.replace("@WebUser.getFK_Dept()", BP.Web.WebUser.getFK_Dept());
			countSQL = countSQL.replace("@Key", key);
			countSQL = this.DealExpByFromVals(countSQL);

			// 替换其他参数.
			for (String cond : conds) {
				if (cond == null || cond.equals("")) {
					continue;
				}

				// 参数.
				String para = cond.substring(5, cond.indexOf("#"));
				String val = this.getRequest().getParameter(para);
				if (DotNetToJavaStringHelper.isNullOrEmpty(val)) {
					if (cond.contains("ListSQL") == true || cond.contains("EnumKey") == true) {
						val = "all";
					} else {
						val = "";
					}
				}

				if (val.equals("all")) {
					countSQL = countSQL.replace(para + "=@" + para, "1=1");
					countSQL = countSQL.replace(para + "='@" + para + "'", "1=1");

					// 找到para 前面表的别名 如 t.1=1 把t. 去掉
					int startIndex = 0;
					while (startIndex != -1 && startIndex < countSQL.length()) {
						int index = countSQL.indexOf("1=1", startIndex + 1);
						if (index > 0 && countSQL.substring(startIndex, index).trim().endsWith(".")) {
							int lastBlankIndex = countSQL.substring(startIndex, index).lastIndexOf(" ");

							countSQL = countSQL.substring(0, lastBlankIndex + startIndex + 1)
									+ countSQL.substring(lastBlankIndex + startIndex + 1 + index - lastBlankIndex - 1);

							startIndex = (startIndex + lastBlankIndex) + 3;
						} else {
							startIndex = index;
						}
					}
				} else {
					// 要执行两次替换有可能是，有引号.
					countSQL = countSQL.replace("@" + para, val);
				}
			}

			String count = BP.DA.DBAccess.RunSQLReturnValInt(countSQL, 0) + "";

			// pageSize
			String pageSize = getRequest().getParameter("pageSize");
			if (DotNetToJavaStringHelper.isNullOrEmpty(pageSize)) {
				pageSize = "10";
			}

			// pageIndex
			String pageIndex = getRequest().getParameter("pageIndex");
			if (DotNetToJavaStringHelper.isNullOrEmpty(pageIndex)) {
				pageIndex = "1";
			}

			String sqlObjs = me.getPopValTablePageSQL();
			sqlObjs = sqlObjs.replace("@WebUser.getNo()", BP.Web.WebUser.getNo());
			sqlObjs = sqlObjs.replace("@WebUser.Name", BP.Web.WebUser.getName());
			sqlObjs = sqlObjs.replace("@WebUser.getFK_Dept()", BP.Web.WebUser.getFK_Dept());
			sqlObjs = sqlObjs.replace("@Key", key);

			// 三个固定参数.
			sqlObjs = sqlObjs.replace("@PageCount",
					((Integer.parseInt(pageIndex) - 1) * Integer.parseInt(pageSize)) + "");
			sqlObjs = sqlObjs.replace("@PageSize", pageSize);
			sqlObjs = sqlObjs.replace("@PageIndex", pageIndex);
			sqlObjs = this.DealExpByFromVals(sqlObjs);

			// 替换其他参数.
			for (String cond : conds) {
				if (cond == null || cond.equals("")) {
					continue;
				}

				// 参数.
				String para = cond.substring(5, cond.indexOf("#"));
				String val = this.getRequest().getParameter(para);
				if (DotNetToJavaStringHelper.isNullOrEmpty(val)) {
					if (cond.contains("ListSQL") == true || cond.contains("EnumKey") == true) {
						val = "all";
					} else {
						val = "";
					}
				}
				if (val.equals("all")) {
					sqlObjs = sqlObjs.replace(para + "=@" + para, "1=1");
					sqlObjs = sqlObjs.replace(para + "='@" + para + "'", "1=1");

					int startIndex = 0;
					while (startIndex != -1 && startIndex < sqlObjs.length()) {
						int index = sqlObjs.indexOf("1=1", startIndex + 1);
						if (index > 0 && sqlObjs.substring(startIndex, index).trim().endsWith(".")) {
							int lastBlankIndex = sqlObjs.substring(startIndex, index).lastIndexOf(" ");

							sqlObjs = sqlObjs.substring(0, lastBlankIndex + startIndex + 1)
									+ sqlObjs.substring(lastBlankIndex + startIndex + 1 + index - lastBlankIndex - 1);

							startIndex = (startIndex + lastBlankIndex) + 3;
						} else {
							startIndex = index;
						}
					}
				} else {
					// 要执行两次替换有可能是，有引号.
					sqlObjs = sqlObjs.replace("@" + para, val);
				}
			}

			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sqlObjs);
			dt.TableName = "DTObjs";
			ds.Tables.add(dt);

			DataTable dtCount = new DataTable("DTCout");
			dtCount.TableName = "DTCout";
			dtCount.Columns.Add("Count", Integer.class);
			dtCount.Rows.AddRow(new String[] { count });
			ds.Tables.add(dtCount);

			// 处理查询条件.
			// $Para=Dept#Label=所在班级#ListSQL=Select No,Name FROM Port_Dept WHERE
			// No='@WebUser.getNo()'
			// $Para=XB#Label=性别#EnumKey=XB
			// $Para=DTFrom#Label=注册日期从#DefVal=@Now-30
			// $Para=DTTo#Label=到#DefVal=@Now

			for (String cond : conds) {
				if (DotNetToJavaStringHelper.isNullOrEmpty(cond) == true) {
					continue;
				}

				String sql = null;
				if (cond.contains("#ListSQL=") == true) {
					sql = cond.substring(cond.indexOf("ListSQL") + 8);
					sql = sql.replace("@WebUser.getNo()", WebUser.getNo());
					sql = sql.replace("@WebUser.Name", WebUser.getName());
					sql = sql.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());
					sql = this.DealExpByFromVals(sql);
				}

				if (cond.contains("#EnumKey=") == true) {
					String enumKey = cond.substring(cond.indexOf("EnumKey") + 8);
					sql = "SELECT IntKey AS No, Lab as Name FROM Sys_Enum WHERE EnumKey='" + enumKey + "'";
				}

				// 处理日期的默认值
				// DefVal=@Now-30
				// if (cond.Contains("@Now"))
				// {
				// int nowIndex = cond.IndexOf(cond);
				// if (cond.Trim().Length - nowIndex > 5)
				// {
				// char optStr = cond.Trim()[nowIndex + 5];
				// int day = 0;
				// if (int.TryParse(cond.Trim().Substring(nowIndex + 6), out
				// day)) {
				// cond = cond.Substring(0, nowIndex) + DateTime.Now.AddDays(-1
				// * day).ToString("yyyy-MM-dd HH:mm");
				// }
				// }
				// }

				if (sql == null) {
					continue;
				}

				// 参数.
				String para = cond.substring(5, cond.indexOf("#"));
				if (ds.Tables.contains(para) == true) {
					throw new RuntimeException("@配置的查询,参数名有冲突不能命名为:" + para);
				}

				// 查询出来数据，就把他放入到dataset里面.
				DataTable dtPara = BP.DA.DBAccess.RunSQLReturnTable(sql);
				dtPara.TableName = para;
				ds.Tables.add(dtPara); // 加入到参数集合.
			}

			return BP.Tools.Json.ToJson(ds);
		}

		if (me.getPopValWorkModel() == PopValWorkModel.Group.getValue()) {
			//
			// * 分组的.
			//

			String sqlObjs = me.getPopValGroupSQL();
			if (sqlObjs.length() > 10) {
				sqlObjs = sqlObjs.replace("@WebUser.getNo()", BP.Web.WebUser.getNo());
				sqlObjs = sqlObjs.replace("@WebUser.Name", BP.Web.WebUser.getName());
				sqlObjs = sqlObjs.replace("@WebUser.getFK_Dept()", BP.Web.WebUser.getFK_Dept());
				sqlObjs = this.DealExpByFromVals(sqlObjs);

				DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sqlObjs);
				dt.TableName = "DTGroup";
				ds.Tables.add(dt);
			}

			sqlObjs = me.getPopValEntitySQL();
			if (sqlObjs.length() > 10) {
				sqlObjs = sqlObjs.replace("@WebUser.getNo()", BP.Web.WebUser.getNo());
				sqlObjs = sqlObjs.replace("@WebUser.Name", BP.Web.WebUser.getName());
				sqlObjs = sqlObjs.replace("@WebUser.getFK_Dept()", BP.Web.WebUser.getFK_Dept());
				sqlObjs = this.DealExpByFromVals(sqlObjs);

				DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sqlObjs);
				dt.TableName = "DTEntity";
				ds.Tables.add(dt);
			}
			return BP.Tools.Json.ToJson(ds);
		}

		// 返回数据.
		return BP.Tools.Json.ToJson(ds);
	}

	public String SingleAttach() {

		String attachPk = this.getRequest().getParameter("attachPk");
		String workid = this.getRequest().getParameter("workid");
		String fk_node = this.getRequest().getParameter("fk_node");
		String ensName = this.getRequest().getParameter("ensName");
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) this.getRequest();
		CommonsMultipartFile item = (CommonsMultipartFile) multipartRequest.getFile("file");
		FrmAttachment frmAth = new FrmAttachment();
		frmAth.setMyPK(attachPk);
		frmAth.RetrieveFromDBSources();

		String athDBPK = attachPk + "_" + workid;

		BP.WF.Node currND = new BP.WF.Node(fk_node);
		BP.WF.Work currWK = currND.getHisWork();
		currWK.setOID(Long.parseLong(workid));
		currWK.Retrieve();
		// 处理保存路径.
		String saveTo = frmAth.getSaveTo();

		if (saveTo.contains("*") || saveTo.contains("@")) {
			// 如果路径里有变量.
			saveTo = saveTo.replace("*", "@");
			saveTo = BP.WF.Glo.DealExp(saveTo, currWK, null);
		}

		// 替换关键的字串.
		saveTo = saveTo.replace("\\\\", "\\");// 增加
		try {
			// saveTo = response.Server.MapPath("~/" + saveTo);
			if (saveTo.indexOf(":") == -1) {
				saveTo = ContextHolderUtils.getRequest().getSession().getServletContext().getRealPath(saveTo);
			}
		} catch (java.lang.Exception e) {
			return "false";
			// saveTo = saveTo;
		}

		/*
		 * if (System.IO.Directory.Exists(saveTo) == false) {
		 * System.IO.Directory.CreateDirectory(saveTo); }
		 * 
		 * saveTo = saveTo + "\\" + athDBPK + "." +
		 * response.Request.Files[0].FileName.substring(context.Request.Files[0]
		 * .FileName.lastIndexOf('.') + 1);
		 * response.Request.Files[0].SaveAs(saveTo);
		 */

		try {
			File fileInfo = new File(saveTo);

			if (!fileInfo.exists()) {
				fileInfo.mkdirs();
			}
		} catch (RuntimeException ex) {
			throw new RuntimeException("@创建路径出现错误，可能是没有权限或者路径配置有问题:"
					+ ContextHolderUtils.getRequest().getSession().getServletContext().getRealPath("~/" + saveTo)
					+ "===" + saveTo + "@技术问题:" + ex.getMessage());
		}

		FrmAttachmentDB dbUpload = new FrmAttachmentDB();
		dbUpload.setMyPK(athDBPK);
		dbUpload.setFK_FrmAttachment(attachPk);
		dbUpload.setRefPKVal(workid);

		dbUpload.setFK_MapData(ensName);

		String ext = FileAccess.getExtensionName(item.getOriginalFilename());

		dbUpload.setFileExts(ext);

		/// #region 处理文件路径，如果是保存到数据库，就存储pk.
		if (frmAth.getAthSaveWay() == AthSaveWay.IISServer) {
			// 文件方式保存
			dbUpload.setFileFullName(saveTo);
		}

		if (frmAth.getAthSaveWay() == AthSaveWay.DB) {
			// 保存到数据库
			dbUpload.setFileFullName(dbUpload.getMyPK());
		}
		/// #endregion 处理文件路径，如果是保存到数据库，就存储pk.

		dbUpload.setFileName(item.getOriginalFilename());
		dbUpload.setFileSize(item.getSize());
		dbUpload.setRec(WebUser.getNo());
		dbUpload.setRecName(WebUser.getName());
		dbUpload.setRDT(DataType.getCurrentDataTimess());

		dbUpload.setNodeID(fk_node);
		dbUpload.Save();

		if (frmAth.getAthSaveWay() == AthSaveWay.DB) {
			// 执行文件保存.
			try {
				BP.DA.DBAccess.SaveFileToDB(saveTo, dbUpload.getEnMap().getPhysicsTable(), "MyPK", dbUpload.getMyPK(),
						"FDB");
			} catch (Exception e) {
				e.printStackTrace();
				return "false";
			}
		}
		return "true";
	}

	// / <summary>
	// / 初始化树的接口
	// / </summary>
	// / <param name="context"></param>
	// / <returns></returns>
	public String PopVal_InitTree() {
		String mypk = this.GetRequestVal("FK_MapExt");

		MapExt me = new MapExt();
		me.setMyPK(mypk);
		me.Retrieve();

		// 获得配置信息.
		Hashtable ht = me.PopValToHashtable();
		DataTable dtcfg = BP.Sys.PubClass.HashtableToDataTable(ht);

		String parentNo = this.GetRequestVal("ParentNo");
		if (parentNo == null)
			parentNo = me.getPopValTreeParentNo();

		DataSet resultDs = new DataSet();
		String sqlObjs = me.getPopValTreeSQL();
		sqlObjs = sqlObjs.replace("@WebUser.No", BP.Web.WebUser.getNo());
		sqlObjs = sqlObjs.replace("@WebUser.Name", BP.Web.WebUser.getName());
		sqlObjs = sqlObjs.replace("@WebUser.FK_Dept", BP.Web.WebUser.getFK_Dept());
		sqlObjs = sqlObjs.replace("@ParentNo", parentNo);
		sqlObjs = this.DealExpByFromVals(sqlObjs);

		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sqlObjs);
		dt.TableName = "DTObjs";
		resultDs.Tables.add(dt);

		// doubleTree
		if (me.getPopValWorkModel() == PopValWorkModel.TreeDouble.getValue()
				&& parentNo != me.getPopValTreeParentNo()) {
			sqlObjs = me.getPopValDoubleTreeEntitySQL();
			sqlObjs = sqlObjs.replace("@WebUser.No", BP.Web.WebUser.getNo());
			sqlObjs = sqlObjs.replace("@WebUser.Name", BP.Web.WebUser.getName());
			sqlObjs = sqlObjs.replace("@WebUser.FK_Dept", BP.Web.WebUser.getFK_Dept());
			sqlObjs = sqlObjs.replace("@ParentNo", parentNo);
			sqlObjs = this.DealExpByFromVals(sqlObjs);

			DataTable entityDt = BP.DA.DBAccess.RunSQLReturnTable(sqlObjs);
			entityDt.TableName = "DTEntitys";
			resultDs.Tables.add(entityDt);
		}
		return BP.Tools.Json.ToJson(resultDs);
	}

	// / <summary>
	// / 处理DataTable中的列名，将不规范的No,Name,ParentNo列纠正
	// / </summary>
	// / <param name="dt"></param>
	public void DoCheckTableColumnNameCase(DataTable dt) {
		for (DataColumn col : dt.Columns) {
			String columnName = col.ColumnName.toLowerCase();
			if (columnName.equals("no")) {
				col.ColumnName = "No";
			} else if (columnName.equals("name")) {
				col.ColumnName = "Name";
			} else if (columnName.equals("parentno")) {
				col.ColumnName = "ParentNo";
			}
		}
	}

	// / <summary>
	// / 初始化PopVal的值 除了分页表格模式之外的其他数据值
	// / </summary>
	// / <returns></returns>
	public String PopVal_Init() throws Exception {
		MapExt me = new MapExt();
		me.setMyPK(this.getFK_MapExt());
		me.Retrieve();

		// 数据对象，将要返回的.
		DataSet ds = new DataSet();

		// 获得配置信息.
		Hashtable ht = me.PopValToHashtable();
		DataTable dtcfg = BP.Sys.PubClass.HashtableToDataTable(ht);

		// 增加到数据源.
		ds.Tables.add(dtcfg);

		if (me.getPopValWorkModel() == PopValWorkModel.SelfUrl.getValue())
			return "@SelfUrl" + me.getPopValUrl();

		if (me.getPopValWorkModel() == PopValWorkModel.TableOnly.getValue()) {
			String sqlObjs = me.getPopValEntitySQL();
			sqlObjs = sqlObjs.replace("@WebUser.No", BP.Web.WebUser.getNo());
			sqlObjs = sqlObjs.replace("@WebUser.Name", BP.Web.WebUser.getName());
			sqlObjs = sqlObjs.replace("@WebUser.FK_Dept", BP.Web.WebUser.getFK_Dept());

			sqlObjs = this.DealExpByFromVals(sqlObjs);

			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sqlObjs);
			dt.TableName = "DTObjs";
			DoCheckTableColumnNameCase(dt);
			ds.Tables.add(dt);
			return BP.Tools.Json.ToJson(ds);
		}

		if (me.getPopValWorkModel() == PopValWorkModel.Group.getValue()) {
			/*
			 * 分组的.
			 */
			String sqlObjs = me.getPopValGroupSQL();
			if (sqlObjs.length() > 10) {
				sqlObjs = sqlObjs.replace("@WebUser.No", BP.Web.WebUser.getNo());
				sqlObjs = sqlObjs.replace("@WebUser.Name", BP.Web.WebUser.getName());
				sqlObjs = sqlObjs.replace("@WebUser.FK_Dept", BP.Web.WebUser.getFK_Dept());
				sqlObjs = this.DealExpByFromVals(sqlObjs);

				DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sqlObjs);
				dt.TableName = "DTGroup";
				DoCheckTableColumnNameCase(dt);
				ds.Tables.add(dt);
			}

			sqlObjs = me.getPopValEntitySQL();
			if (sqlObjs.length() > 10) {
				sqlObjs = sqlObjs.replace("@WebUser.No", BP.Web.WebUser.getNo());
				sqlObjs = sqlObjs.replace("@WebUser.Name", BP.Web.WebUser.getName());
				sqlObjs = sqlObjs.replace("@WebUser.FK_Dept", BP.Web.WebUser.getFK_Dept());
				sqlObjs = this.DealExpByFromVals(sqlObjs);

				DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sqlObjs);
				dt.TableName = "DTEntity";
				DoCheckTableColumnNameCase(dt);
				ds.Tables.add(dt);
			}
			return BP.Tools.Json.ToJson(ds);
		}

		if (me.getPopValWorkModel() == PopValWorkModel.TablePage.getValue()) {
			/* 分页的 */
			// key
			String key = this.GetRequestVal("Key");
			if (DataType.IsNullOrEmpty(key) == true)
				key = "";

			// 取出来查询条件.
			String[] conds = me.getPopValSearchCond().split("\\$");

			// pageSize
			String pageSize = this.GetRequestVal("pageSize");
			if (DataType.IsNullOrEmpty(pageSize))
				pageSize = "10";

			// pageIndex
			String pageIndex = this.GetRequestVal("pageIndex");
			if (DataType.IsNullOrEmpty(pageIndex))
				pageIndex = "1";

			String sqlObjs = me.getPopValTablePageSQL();
			sqlObjs = sqlObjs.replace("@WebUser.No", BP.Web.WebUser.getNo());
			sqlObjs = sqlObjs.replace("@WebUser.Name", BP.Web.WebUser.getName());
			sqlObjs = sqlObjs.replace("@WebUser.FK_Dept", BP.Web.WebUser.getFK_Dept());
			sqlObjs = sqlObjs.replace("@Key", key);

			// 三个固定参数.
			sqlObjs = sqlObjs.replace("@PageCount",
					String.valueOf((Integer.parseInt(pageIndex) - 1) * Integer.parseInt(pageSize)));
			sqlObjs = sqlObjs.replace("@PageSize", pageSize);
			sqlObjs = sqlObjs.replace("@PageIndex", pageIndex);
			sqlObjs = this.DealExpByFromVals(sqlObjs);

			// 替换其他参数.
			for (String cond : conds) {
				if (cond == null || "".equals(cond))
					continue;

				// 参数.
				String para = cond.substring(5, cond.indexOf("#"));
				String val = this.GetRequestVal(para); // context.Request.QueryString[para];
														// //
														// ////////////////////
				if (DataType.IsNullOrEmpty(val)) {
					if (cond.contains("ListSQL") == true || cond.contains("EnumKey") == true)
						val = "all";
					else
						val = "";
				}
				if ("all".equals(val)) {
					sqlObjs = sqlObjs.replace(para + "=@" + para, "1=1");
					sqlObjs = sqlObjs.replace(para + "='@" + para + "'", "1=1");

					int startIndex = 0;
					while (startIndex != -1 && startIndex < sqlObjs.length()) {
						int index = sqlObjs.indexOf("1=1", startIndex + 1);
						if (index > 0 && sqlObjs.substring(startIndex, index).trim().endsWith(".")) { // substring未测试(由.net直译而来,
																										// 需要修改)
							int lastBlankIndex = sqlObjs.substring(startIndex, index).lastIndexOf(" "); // substring未测试(由.net直译而来,
																										// 需要修改)
							sqlObjs = StringHelper.remove(sqlObjs, lastBlankIndex + startIndex + 1,
									index - lastBlankIndex - 1);
							startIndex = (startIndex + lastBlankIndex) + 3;
						} else {
							startIndex = index;
						}
					}
				} else {
					// 要执行两次替换有可能是，有引号.
					sqlObjs = sqlObjs.replace("@" + para, val);
				}
			}

			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sqlObjs);
			dt.TableName = "DTObjs";
			DoCheckTableColumnNameCase(dt);
			ds.Tables.add(dt);

			for (String cond : conds) {
				if (DataType.IsNullOrEmpty(cond) == true)
					continue;

				String sql = null;
				if (cond.contains("#ListSQL=") == true) {
					sql = cond.substring(cond.indexOf("ListSQL") + 8);
					sql = sql.replace("@WebUser.No", WebUser.getNo());
					sql = sql.replace("@WebUser.Name", WebUser.getName());
					sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
					sql = this.DealExpByFromVals(sql);
				}

				if (cond.contains("#EnumKey=") == true) {
					String enumKey = cond.substring(cond.indexOf("EnumKey") + 8);
					sql = "SELECT IntKey AS No, Lab as Name FROM Sys_Enum WHERE EnumKey='" + enumKey + "'";
				}

				if (sql == null)
					continue;
				// 参数.
				String para = cond.substring(5, cond.indexOf("#"));
				if (ds.Tables.contains(para) == true)
					throw new Exception("@配置的查询,参数名有冲突不能命名为:" + para);

				// 查询出来数据，就把他放入到dataset里面.
				DataTable dtPara = BP.DA.DBAccess.RunSQLReturnTable(sql);
				dtPara.TableName = para;
				DoCheckTableColumnNameCase(dt);
				ds.Tables.add(dtPara); // 加入到参数集合.
			}
			return BP.Tools.Json.ToJson(ds);
		}
		// 返回数据.
		return BP.Tools.Json.ToJson(ds);
	}

	public String MoreAttach() {

		String attachPk = this.getRequest().getParameter("attachPk");
		String workid = this.getRequest().getParameter("workid");
		String fk_node = this.getRequest().getParameter("fk_node");
		String ensNamestring = this.getRequest().getParameter("ensNamestring");
		String fk_flow = this.getRequest().getParameter("fk_flow");
		String pkVal = this.getRequest().getParameter("pkVal");
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) this.getRequest();
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		// 多附件描述.
		FrmAttachment athDesc = new FrmAttachment(attachPk);

		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile mf = entity.getValue();
			String fileName = mf.getOriginalFilename();
			String savePath = athDesc.getSaveTo();
			if (savePath.contains("@") == true || savePath.contains("*") == true) {
				// 如果有变量
				savePath = savePath.replace("*", "@");
				GEEntity en = new GEEntity(athDesc.getFK_MapData());
				en.setPKVal(pkVal);
				en.Retrieve();
				savePath = BP.WF.Glo.DealExp(savePath, en, null);

				if (savePath.contains("@") && fk_node != null) {
					// 如果包含 @
					BP.WF.Flow flow = new BP.WF.Flow(fk_flow);
					BP.WF.Data.GERpt myen = flow.getHisGERpt();
					myen.setOID(Long.parseLong(workid));
					myen.RetrieveFromDBSources();
					savePath = BP.WF.Glo.DealExp(savePath, myen, null);
				}
				if (savePath.contains("@") == true) {
					throw new RuntimeException("@路径配置错误,变量没有被正确的替换下来." + savePath);
				}
			} else {
				savePath = athDesc.getSaveTo() + "/" + pkVal;
			}

			// 替换关键的字串.
			savePath = savePath.replace("\\\\", "\\");
			try {
				// savePath = context.Server.MapPath("~/" + savePath);
				if (savePath.indexOf(":") == -1) {
					savePath = ContextHolderUtils.getRequest().getSession().getServletContext().getRealPath(savePath);
				}
			} catch (RuntimeException e) {
				return "false";
			}

			try {
				File fileInfo = new File(savePath);

				if (!fileInfo.exists()) {
					fileInfo.mkdirs();
				}
			} catch (RuntimeException ex) {
				throw new RuntimeException("@创建路径出现错误，可能是没有权限或者路径配置有问题:"
						+ ContextHolderUtils.getRequest().getSession().getServletContext().getRealPath("~/" + savePath)
						+ "===" + savePath + "@技术问题:" + ex.getMessage());
			}

			// String exts =
			// System.IO.Path.GetExtension(context.Request.Files[i].FileName).toLowerCase().replace(".",
			// "");
			String exts = FileAccess.getExtensionName(fileName);

			String guid = BP.DA.DBAccess.GenerGUID();
			// String fileName = context.Request.Files[i].FileName.substring(0,
			// context.Request.Files[i].FileName.lastIndexOf('.'));
			String ext = mf.getContentType();
			String realSaveTo = savePath + "/" + guid + "." + fileName + ext;
			String saveTo = realSaveTo;
			// context.Request.Files[i].SaveAs(realSaveTo);

			File info = new File(realSaveTo);

			FrmAttachmentDB dbUpload = new FrmAttachmentDB();
			dbUpload.setMyPK(guid); // athDesc.FK_MapData + oid.ToString();
			dbUpload.setNodeID(fk_node.toString());
			dbUpload.setFK_FrmAttachment(attachPk);
			dbUpload.setFK_MapData(athDesc.getFK_MapData());
			dbUpload.setFK_FrmAttachment(attachPk);
			dbUpload.setFileExts(exts);

			/// #region 处理文件路径，如果是保存到数据库，就存储pk.
			if (athDesc.getAthSaveWay() == AthSaveWay.IISServer) {
				// 文件方式保存
				dbUpload.setFileFullName(saveTo);
			}

			if (athDesc.getAthSaveWay() == AthSaveWay.DB) {
				// 保存到数据库
				dbUpload.setFileFullName(dbUpload.getMyPK());
			}
			/// #endregion 处理文件路径，如果是保存到数据库，就存储pk.

			dbUpload.setFileName(fileName);
			dbUpload.setFileSize((float) info.length());
			dbUpload.setRDT(DataType.getCurrentDataTimess());
			dbUpload.setRec(WebUser.getNo());
			dbUpload.setRecName(BP.Web.WebUser.getName());
			dbUpload.setRefPKVal(pkVal);
			// if (athDesc.IsNote)
			// dbUpload.MyNote = this.Pub1.GetTextBoxByID("TB_Note").Text;

			// if (athDesc.Sort.Contains(","))
			// dbUpload.Sort =
			// this.Pub1.GetDDLByID("ddl").SelectedItemStringVal;

			dbUpload.setUploadGUID(guid);
			dbUpload.Insert();

			if (athDesc.getAthSaveWay() == AthSaveWay.DB) {
				// 执行文件保存.
				try {
					BP.DA.DBAccess.SaveFileToDB(saveTo, dbUpload.getEnMap().getPhysicsTable(), "MyPK",
							dbUpload.getMyPK(), "FDB");
				} catch (Exception e) {
					e.printStackTrace();
					return "false";
				}
			}
		}
		return "true";
	}

	public String DelWorkCheckAttach() {

		String MyPK = this.getRequest().getParameter("MyPK");
		FrmAttachmentDB athDB = new FrmAttachmentDB();
		athDB.RetrieveByAttr(FrmAttachmentDBAttr.MyPK, MyPK);
		// 删除文件
		if (athDB.getFileFullName() != null) {
			/*
			 * if (File.Exists(athDB.getFileFullName()) == true) {
			 * File.Delete(athDB.getFileFullName()); }
			 */
			try {
				File fileInfo = new File(athDB.getFileFullName());
				if (fileInfo.exists()) {
					fileInfo.delete();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		int i = athDB.Delete(FrmAttachmentDBAttr.MyPK, MyPK);
		if (i > 0) {
			return "true";
		}
		return "false";
	}

	/**
	 * 默认执行的方法
	 */
	public final String DoDefaultMethod() {
		return "err@没有此方法 " + this.toString() + " - " + this.getDoType();
	}

	// /////////////////////////////////////////////////
	// /#region HanderMapExt
	private String DealSQL(String sql, String key) {

		sql = sql.replace("@Key", key);
		sql = sql.replace("@key", key);
		sql = sql.replace("@Val", key);
		sql = sql.replace("@val", key);

		sql = sql.replace("@WebUser.getNo()", WebUser.getNo());
		sql = sql.replace("@WebUser.Name", WebUser.getName());
		sql = sql.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());

		String oid = this.GetRequestVal("OID");
		if (oid != null) {
			sql = sql.replace("@OID", oid);
		}

		String kvs = this.GetRequestVal("KVs");

		if (DotNetToJavaStringHelper.isNullOrEmpty(kvs) == false && sql.contains("@") == true) {
			String[] strs = kvs.split("[~]", -1);
			for (String s : strs) {
				if (DotNetToJavaStringHelper.isNullOrEmpty(s) || s.contains("=") == false) {
					continue;
				}

				String[] mykv = s.split("[=]", -1);
				sql = sql.replace("@" + mykv[0], mykv[1]);

				if (sql.contains("@") == false) {
					break;
				}
			}
		}

		if (sql.contains("@") == true) {
			Enumeration keys = this.getRequest().getAttributeNames();
			while (keys.hasMoreElements()) {
				sql = sql.replace("@" + keys.nextElement().toString(),
						this.getRequest().getParameter(keys.nextElement().toString()));
			}
		}

		dealSQL = sql;
		return sql;
	}

	private String dealSQL = "";

	public final String JSONTODT(DataTable dt) {
		if ((BP.Sys.SystemConfig.getAppCenterDBType() == DBType.Informix
				|| BP.Sys.SystemConfig.getAppCenterDBType() == DBType.Oracle) && dealSQL != null) {
			// 如果数据库不区分大小写, 就要按用户输入的sql进行二次处理。
			String mysql = dealSQL.trim();
			mysql = mysql.substring(6, mysql.toLowerCase().indexOf("from"));
			mysql = mysql.replace(",", " ");
			String[] strs = mysql.split("[ ]", -1);
			for (String s : strs) {
				if (DotNetToJavaStringHelper.isNullOrEmpty(s)) {
					continue;
				}
				for (DataColumn dc : dt.Columns) {
					if (dc.ColumnName.toLowerCase().equals(s.toLowerCase())) {
						dc.ColumnName = s;
						break;
					}
				}
			}
		} else {
			for (DataColumn dc : dt.Columns) {
				if (dc.ColumnName.toLowerCase().equals("no")) {
					dc.ColumnName = "No";
					continue;
				}
				if (dc.ColumnName.toLowerCase().equals("name")) {
					dc.ColumnName = "Name";
					continue;
				}
			}
		}

		StringBuilder JsonString = new StringBuilder();
		if (dt != null && dt.Rows.size() > 0) {
			JsonString.append("{ ");
			JsonString.append("\"Head\":[ ");
			for (int i = 0; i < dt.Rows.size(); i++) {
				JsonString.append("{ ");
				for (int j = 0; j < dt.Columns.size(); j++) {
					if (j < dt.Columns.size() - 1) {
						JsonString.append("\"" + dt.Columns.get(j).ColumnName.toString() + "\":\""
								+ dt.Rows.get(i).getValue(j).toString() + "\",");
					} else if (j == dt.Columns.size() - 1) {
						JsonString.append("\"" + dt.Columns.get(j).ColumnName.toString() + "\":\""
								+ dt.Rows.get(i).getValue(j).toString() + "\"");
					}
				}
				//
				// end Of String
				if (i == dt.Rows.size() - 1) {
					JsonString.append("} ");
				} else {
					JsonString.append("}, ");
				}
			}
			JsonString.append("]}");
			return JsonString.toString();
		} else {
			return null;
		}
	}

	// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	// /#endregion dtl.htm 从表.
	/**
	 * 处理SQL的表达式.
	 * 
	 * @param exp
	 *            表达式
	 * @return 从from里面替换的表达式.
	 */
	public final String DealExpByFromVals(String exp) {
		Enumeration keys = this.getRequest().getParameterNames();
		while (keys.hasMoreElements()) {
			if (exp.contains("@") == false) {
				return exp;
			}
			String strKey = keys.nextElement().toString();
			String str = strKey.replace("TB_", "").replace("CB_", "").replace("DDL_", "").replace("RB_", "");

			exp = exp.replace("@" + str, this.getRequest().getParameter(strKey));
		}
		return exp;
	}

	public final long getPWorkID() {
		return this.GetRequestValInt("PWorkID");
	}

	// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	/// #region 从表的选项.
	/**
	 * 初始化数据
	 * 
	 * @return
	 */
	public final String DtlOpt_Init() {
		MapDtl dtl = new MapDtl(this.getFK_MapDtl());
		if (StringUtils.isEmpty(dtl.getImpSQLInit())) {
			return "err@从表加载语句为空，请设置从表加载的sql语句。";
		} else {
			DataTable dt = DBAccess.RunSQLReturnTable(dtl.getImpSQLInit());

			return BP.Tools.Json.ToJson(dt);
		}
	}

	/**
	 * 增加
	 * 
	 * @return
	 */
	public final String DtlOpt_Add() {
		MapDtl dtl = new MapDtl(this.getFK_MapDtl());
		String pks = this.GetRequestVal("PKs");

		String[] strs = pks.split("[,]", -1);
		int i = 0;
		for (String str : strs) {
			if (str.equals("CheckAll") || str == null || str.equals("")) {
				continue;
			}

			GEDtl gedtl = new BP.Sys.GEDtl(this.getFK_MapDtl());
			String sql = dtl.getImpSQLFullOneRow();
			sql = sql.replace("@Key", str);

			DataTable dt = DBAccess.RunSQLReturnTable(sql);

			if (dt.Rows.size() == 0) {
				return "err@导入数据失败:" + sql;
			}

			gedtl.Copy(dt.Rows.get(0));
			gedtl.setRefPK(this.GetRequestVal("RefPKVal"));
			gedtl.InsertAsNew();
			i++;
		}

		return "成功的导入了[" + i + "]行数据...";
	}

	/**
	 * 执行查询.
	 * 
	 * @return
	 */
	public final String DtlOpt_Search() {
		MapDtl dtl = new MapDtl(this.getFK_MapDtl());

		String sql = dtl.getImpSQLSearch();
		sql = sql.replace("@Key", this.GetRequestVal("Key"));
		sql = sql.replace("@WebUser.No", WebUser.getNo());
		sql = sql.replace("@WebUser.Name", WebUser.getName());
		sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());

		DataSet ds = new DataSet();
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		return BP.Tools.Json.ToJson(dt);
	}
	// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	/// #endregion 从表的选项.

	/**
	 * 下载
	 * 
	 * @return
	 */
	public final String AttachmentUpload_Down() {
		FrmAttachmentDB downDB = new FrmAttachmentDB();
		downDB.setMyPK(this.getMyPK());
		downDB.Retrieve();

		FrmAttachment dbAtt = new FrmAttachment();
		dbAtt.setMyPK(downDB.getFK_FrmAttachment());
		dbAtt.Retrieve();
		String docs = DataType.ReadTextFile(downDB.getFileFullName());

         DataTable dt = new DataTable();
         dt.Columns.Add("FileName");
         dt.Columns.Add("FileType");
         dt.Columns.Add("FlieContent");
         DataRow dr = dt.NewRow();
         
         
		if (dbAtt.getAthSaveWay() == AthSaveWay.IISServer) {
			dr.put("FileName",downDB.getFileName());;
	         dr.put("FileType",downDB.getFileExts());
	         dr.put("FlieContent",docs);
		}

		if (dbAtt.getAthSaveWay() == AthSaveWay.FTPServer) {
			if(this.GetRequestVal("Model").equals("2") == true){
				 String fileName = downDB.GenerTempFile(dbAtt.getAthSaveWay());
			}
			String fileName = "";//// downDB.MakeFullFileFromFtp(); 暂时未翻译FTP
			// PubClass.DownloadFile(downDB.MakeFullFileFromFtp(),
			// downDB.FileName);
			return "url@" + fileName;
		}

		if (dbAtt.getAthSaveWay() == AthSaveWay.DB) {
			//// PubClass.DownloadHttpFile(downDB.getFileFullName(),
			//// downDB.getFileName());暂时未翻译
		}
		dt.Rows.add(dr);
        return BP.Tools.Json.ToJson(dt);
		
	}

	/** 初始化
	 
	 @return 
*/
	public final String DtlCard_Init()
	{
		DataSet ds = new DataSet();

		MapDtl md = new MapDtl(this.getEnsName());

		//主表数据.
		DataTable dt = md.ToDataTableField("Main");
		ds.Tables.add(dt);

		//主表字段.
		MapAttrs attrs = md.getMapAttrs();
		ds.Tables.add(attrs.ToDataTableField("MapAttrs"));

		//从表.
		MapDtls dtls = md.getMapDtls();
		ds.Tables.add(dtls.ToDataTableField("MapDtls"));

		//从表的从表.
		for (MapDtl dtl : dtls.ToJavaList())
		{
			MapAttrs subAttrs = new MapAttrs(dtl.getNo());
			ds.Tables.add(subAttrs.ToDataTableField(dtl.getNo()));
		}

		//从表的数据.
		GEDtls enDtls = new GEDtls(this.getEnsName());
		enDtls.Retrieve(GEDtlAttr.RefPK, this.getRefPKVal());
		ds.Tables.add(enDtls.ToDataTableField("DTDtls"));

		return BP.Tools.Json.ToJson(ds);

	}
	/** 
	 获得从表的从表数据
	 
	 @return 
	*/
	public final String DtlCard_Init_Dtl()
	{
		DataSet ds = new DataSet();

		MapDtl md = new MapDtl(this.getEnsName());

		//主表数据.
		DataTable dt = md.ToDataTableField("Main");
		ds.Tables.add(dt);

		//主表字段.
		MapAttrs attrs = md.getMapAttrs();
		ds.Tables.add(attrs.ToDataTableField("MapAttrs"));

		GEDtls enDtls = new GEDtls(this.getEnsName());
		enDtls.Retrieve(GEDtlAttr.RefPK, this.getRefPKVal());
		ds.Tables.add(enDtls.ToDataTableField("DTDtls"));

		return BP.Tools.Json.ToJson(ds);
	}
}
