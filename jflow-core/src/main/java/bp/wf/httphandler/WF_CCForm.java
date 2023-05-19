package bp.wf.httphandler;

import bp.ccbill.DBList;
import bp.da.*;
import bp.difference.ContextHolderUtils;
import bp.tools.OSSUploadFileUtils;
import bp.difference.SystemConfig;
import bp.difference.handler.CommonFileUtils;
import bp.difference.handler.CommonUtils;
import bp.difference.handler.WebContralBase;
import bp.pub.PubClass;
import bp.sys.*;
import bp.sys.CCFormAPI;
import bp.sys.base.FormEventBaseDtl;
import bp.tools.*;
import bp.web.*;
import bp.wf.Glo;
import bp.wf.template.*;
import bp.en.*;
import bp.wf.*;

import java.awt.image.BufferedImage;
import java.net.URLDecoder;
import java.nio.channels.FileChannel;
import java.util.*;
import java.io.*;

import bp.wf.template.frm.MapFrmWps;
import bp.wf.template.sflow.FrmSubFlowAttr;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

/** 
 表单功能界面
*/
public class WF_CCForm extends  WebContralBase
{

		///#region 多附件.
	/** 
	 获得数据
	 
	 @return 
	*/
	public final String Ath_Init() throws Exception {
		try {
			DataSet ds = new DataSet();

			FrmAttachment athDesc = this.GenerAthDesc();

			// 查询出来数据实体.
			String pkVal = this.GetRequestVal("RefOID");
			if (DataType.IsNullOrEmpty(pkVal) == true)
				pkVal = this.GetRequestVal("OID");
			if (DataType.IsNullOrEmpty(pkVal) == true)
				pkVal = String.valueOf(this.getWorkID());

			bp.sys.FrmAttachmentDBs dbs = bp.wf.Glo.GenerFrmAttachmentDBs(athDesc, pkVal, this.getFK_FrmAttachment(),
					this.getWorkID(), this.getFID(), this.getPWorkID());

			/// 如果图片显示.(先不考虑.)
			if (athDesc.getFileShowWay() == FileShowWay.Pict) {
				/* 如果是图片轮播，就在这里根据数据输出轮播的html代码. */
				if (dbs.size() == 0 && athDesc.getIsUpload() == true) {
					/* 没有数据并且，可以上传,就转到上传的界面上去. */
					return "url@AthImg.htm?1=1" + this.getRequestParas();
				}
			}

			// 处理权限问题, 有可能当前节点是可以上传或者删除，但是当前节点上不能让此人执行工作。
			boolean isDel = athDesc.getHisDeleteWay() == AthDeleteWay.None ? false : true;
			boolean isUpdate = athDesc.getIsUpload();
			athDesc.setIsUpload(isUpdate);

			String sort = athDesc.getSort().trim();
			if (sort.contains("SELECT") == true || sort.contains("select") == true) {
				String sql = bp.wf.Glo.DealExp(sort, null, null);
				DataTable dt = DBAccess.RunSQLReturnTable(sql);
				String strs = "";
				for (DataRow dr : dt.Rows) {
					strs += dr.getValue(0) + ",";
				}
				athDesc.setSort(strs);
			}

			// 增加附件描述.
			ds.Tables.add(athDesc.ToDataTableField("AthDesc"));

			// 增加附件.
			ds.Tables.add(dbs.ToDataTableField("DBAths"));

			// 返回.
			return bp.tools.Json.ToJson(ds);
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}
		///#endregion 多附件.


		///#region HanderMapExt
	/** 
	 扩展处理.
	 
	 @return 
	*/
	public final String HandlerMapExt() throws Exception {
		String fk_mapExt = this.GetRequestVal("FK_MapExt").toString();
		if (DataType.IsNullOrEmpty(this.GetRequestVal("Key"))) {
			return "";
		}

		String oid = this.GetRequestVal("OID");

		String kvs = this.GetRequestVal("KVs");

		MapExt me = new MapExt(fk_mapExt);
		DataTable dt = null;
		String sql = "";
		String key = this.GetRequestVal("Key");
		key = URLDecoder.decode(key, "UTF-8");
		key = key.trim();
		key = key.replace("'", ""); // 去掉单引号.
		String dbsrc = me.getFK_DBSrc();
		SFDBSrc sfdb = null;
		if (DataType.IsNullOrEmpty(dbsrc) == false && dbsrc.equals("local") == false)
			sfdb = new SFDBSrc(dbsrc);
		// key = "周";
		switch (me.getExtType()) {
			case MapExtXmlList.ActiveDDL: // 动态填充ddl.
				sql = this.DealSQL(me.getDocOfSQLDeal(), key);
				if (sql.contains("@") == true) {
					Enumeration keys = this.getRequest().getAttributeNames();
					while (keys.hasMoreElements()) {
						sql = sql.replace("@" + keys.nextElement().toString(),
								this.getRequest().getParameter(keys.nextElement().toString()));
					}
				}
				if (sfdb != null)
					dt = sfdb.RunSQLReturnTable(sql);
				else
					dt = DBAccess.RunSQLReturnTable(sql);
				return JSONTODT(dt);
			case MapExtXmlList.AutoFullDLL: // 填充下拉框
			case MapExtXmlList.TBFullCtrl: // 自动完成。
			case MapExtXmlList.DDLFullCtrl: // 级连ddl.
			case MapExtXmlList.FullData: // 填充其他控件.
				switch (this.GetRequestVal("DoTypeExt")) {
					case "ReqCtrl":
						// 获取填充 ctrl 值的信息.
						sql = this.DealSQL(me.getDocOfSQLDeal(), key);
						WebUser.SetSessionByKey("DtlKey", key);
						if (sfdb != null)
							dt = sfdb.RunSQLReturnTable(sql);
						else
							dt = DBAccess.RunSQLReturnTable(sql);

						return JSONTODT(dt);
					case "ReqDtlFullList":
						/* 获取填充的从表集合. */
						DataTable dtDtl = new DataTable("Head");
						dtDtl.Columns.Add("Dtl", String.class);
						String[] strsDtl = me.getTag1().split("[$]", -1);
						for (String str : strsDtl) {
							if (DataType.IsNullOrEmpty(str)) {
								continue;
							}

							String[] ss = str.split("[:]", -1);
							String fk_dtl = ss[0];
							if (ss[1] == null || ss[1].equals("")) {
								continue;
							}
							Object tempVar = WebUser.GetSessionByKey("DtlKey", null);
							String dtlKey = tempVar instanceof String ? (String) tempVar : null;
							if (dtlKey == null) {
								dtlKey = key;
							}
							String mysql = DealSQL(ss[1], dtlKey);

							GEDtls dtls = new GEDtls(fk_dtl);
							MapDtl dtl = new MapDtl(fk_dtl);

							DataTable dtDtlFull = null;

							try
							{
								if (sfdb != null)
									dtDtlFull = sfdb.RunSQLReturnTable(mysql);
								else
									dtDtlFull = DBAccess.RunSQLReturnTable(mysql);
							}
							catch (Exception ex)
							{
								throw new Exception("err@执行填充从表出现错误,[" + dtl.getNo() + " - " + dtl.getName() + "]设置的SQL" + mysql);
							}
							DBAccess.RunSQL("DELETE FROM " + dtl.getPTable() + " WHERE RefPK=" + oid);
							for (DataRow dr : dtDtlFull.Rows) {
								GEDtl mydtl = new GEDtl(fk_dtl);
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
					case "ReqDDLFullList":
						/* 获取要个性化填充的下拉框. */
						DataTable dt1 = new DataTable("Head");
						dt1.Columns.Add("DDL", String.class);
						if (DataType.IsNullOrEmpty(me.getTag()) == false) {
							String[] strs = me.getTag().split("[$]", -1);
							for (String str : strs) {
								if (str == null || str.equals("")) {
									continue;
								}

								String[] ss = str.split("[:]", -1);
								DataRow dr = dt1.NewRow();
								dr.setValue(0, ss[0]);
								// dr[1] = ss[1];
								dt1.Rows.add(dr);
							}
							return JSONTODT(dt1);
						}
						return "";
					case "ReqDDLFullListDB":
						/* 获取要个性化填充的下拉框的值. 根据已经传递过来的 ddl id. */
						String myDDL = this.GetRequestVal("MyDDL");
						sql = me.getDocOfSQLDeal();
						String[] strs1 = me.getTag().split("[$]", -1);
						for (String str : strs1) {
							if (str == null || str.equals("")) {
								continue;
							}

							String[] ss = str.split("[:]", -1);
							if (myDDL.equals(ss[0]) && ss.length == 2) {
								sql = ss[1];
								sql = this.DealSQL(sql, key);
								break;
							}
						}
						if (sfdb != null)
							dt = sfdb.RunSQLReturnTable(sql);
						else
							dt = DBAccess.RunSQLReturnTable(sql);
						return JSONTODT(dt);
					default:
						key = key.replace("'", "");

						sql = this.DealSQL(me.getDocOfSQLDeal(), key);

						if (sfdb != null)
							dt = sfdb.RunSQLReturnTable(sql);
						else
							dt = DBAccess.RunSQLReturnTable(sql);

						return JSONTODT(dt);
				}
			default:
				return "err@没有解析的标记" + me.getExtType();
		}
	}
	/** 
	 处理sql.
	 
	 param sql 要执行的sql
	 param key 关键字值
	 @return 执行sql返回的json
	*/
	private String DealSQL(String sql, String key)
	{
		sql = sql.replace("@Key", key);
		sql = sql.replace("@key", key);
		sql = sql.replace("@Val", key);
		sql = sql.replace("@val", key);
		sql = sql.replace("@val", key);

		sql = sql.replace("\n", "");
		sql = sql.replace("\n", "");
		sql = sql.replace("\n", "");
		sql = sql.replace("\n", "");
		sql = sql.replace("\n", "");


		sql = sql.replace("@WebUser.No", WebUser.getNo());
		sql = sql.replace("@WebUser.Name", WebUser.getName());
		sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());

		String oid = this.GetRequestVal("OID");
		if (oid != null)
		{
			sql = sql.replace("@OID", oid);
		}

		String kvs = this.GetRequestVal("KVs");

		if (DataType.IsNullOrEmpty(kvs) == false && sql.contains("@") == true)
		{
			String[] strs = kvs.split("[~]", -1);
			for (String s : strs)
			{
				if (DataType.IsNullOrEmpty(s) || s.contains("=") == false)
				{
					continue;
				}

				String[] mykv = s.split("[=]", -1);
				sql = sql.replace("@" + mykv[0], mykv[1]);

				if (sql.contains("@") == false)
				{
					break;
				}
			}
		}

		if (sql.contains("@") == true)
		{
			for (String mykey : CommonUtils.getRequest().getParameterMap().keySet())
			{
				sql = sql.replace("@" + mykey, this.GetRequestVal(mykey));
			}
		}

		dealSQL = sql;
		return sql;
	}
	private String dealSQL = "";



	public final String JSONTODT(DataTable dt) {
		if ((SystemConfig.getAppCenterDBType() == DBType.Informix
				|| SystemConfig.getAppCenterDBType() == DBType.Oracle
				|| SystemConfig.getAppCenterDBType() == DBType.KingBaseR3
				|| SystemConfig.getAppCenterDBType() == DBType.KingBaseR6)
				&& dealSQL != null) {
			/* 如果数据库不区分大小写, 就要按用户输入的sql进行二次处理。 */
			String mysql = dealSQL.trim();
			if (mysql.equals("")) {
				return "";
			}
			mysql = mysql.substring(6, mysql.toLowerCase().indexOf("from"));
			mysql = mysql.replace(",", "|");
			String[] strs = mysql.split("[|]", -1);
			String[] pstr = null;
			String ns = null;

			for (String s : strs) {
				if (DataType.IsNullOrEmpty(s)) {
					continue;
				}
				// 处理ORACLE中获取字段使用别名的情况，使用别名的字段，取别名
				ns = s.trim();
				pstr = ns.split("[ ]", -1);
				;
				if (pstr.length > 1) {
					ns = pstr[pstr.length - 1].replace("\"", "");
				}

				for (DataColumn dc : dt.Columns) {
					if (dc.ColumnName.toLowerCase().equals(ns.toLowerCase())) {
						dc.ColumnName = ns;
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
		JsonString.append("{ ");
		JsonString.append("\"Head\":[ ");

		if (dt != null && dt.Rows.size() > 0) {
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
				/* end Of String */
				if (i == dt.Rows.size() - 1) {
					JsonString.append("} ");
				} else {
					JsonString.append("}, ");
				}
			}
		}

		JsonString.append("]}");

		return JsonString.toString();
	}

		///#endregion HanderMapExt


		///#region 执行父类的重写方法.

	/** 
	 构造函数
	*/
	public WF_CCForm()  {
	}
	/** 
	 默认执行的方法
	 
	 @return 
	*/
	@Override
	protected String DoDefaultMethod() throws Exception {
		//找不不到标记就抛出异常.
		throw new RuntimeException("@标记[" + this.getDoType() + "]，没有找到.@原始URL:" + ContextHolderUtils.getRequest().getRequestURI());
	}
	///#endregion 执行父类的重写方法.
	/**
	 执行数据初始化
	 
	 @return 
	*/
	public final String Frm_Init() throws Exception {
		boolean IsMobile = GetRequestValBoolen("IsMobile");
		if (this.GetRequestVal("IsTest") != null) {
			MapData mymd = new MapData(this.getEnsName());
			mymd.RepairMap();
			SystemConfig.DoClearCash();
		}

		MapData md = new MapData(this.getEnsName());

		/// 判断是否是返回的URL.
		if (md.getHisFrmType() == FrmType.Url) {
			String no = this.GetRequestVal("NO");
			String urlParas = "OID=" + this.getRefOID()+"&FK_MapData="+md.getNo() + "&NO=" + no + "&WorkID=" + this.getWorkID() + "&FK_Node="
					+ this.getFK_Node() + "&UserNo=" + WebUser.getNo() + "&SID=" + this.getSID()+"&FID="+this.getFID()+"&PWorkID="+this.getPWorkID();

			String url = "";
			/* 如果是URL. */
			if (md.getUrlExt().contains("?") == true) {
				url = md.getUrlExt() + "&" + urlParas;
			} else {
				url = md.getUrlExt() + "?" + urlParas;
			}
			return "url@" + url;
		}

		if (md.getHisFrmType() == FrmType.Entity) {
			String no = this.GetRequestVal("NO");
			String urlParas = "OID=" + this.getRefOID() +"&FK_MapData="+md.getNo() + "&NO=" + no + "&WorkID=" + this.getWorkID() + "&FK_Node="
					+ this.getFK_Node() + "&UserNo=" + WebUser.getNo() + "&SID=" + this.getSID();

			Entities ens = ClassFactory.GetEns(md.getPTable());

			Entity en = ens.getGetNewEntity();

			if (en.getIsOIDEntity() == true) {
				EntityOID enOID = (EntityOID) en;
				if (enOID == null) {
					return "err@系统错误，无法将" + md.getPTable() + "转化成bp.en.EntityOID";
				}

				enOID.SetValByKey("OID", this.getWorkID());

				if (en.RetrieveFromDBSources() == 0) {
					while (getRequest().getParameterNames().hasMoreElements()) {
						String key = (String) bp.sys.Glo.getRequest().getParameterNames().nextElement();
						String val = bp.sys.Glo.getRequest().getParameter(key);
						enOID.SetValByKey(key, val);
					}
					enOID.SetValByKey("OID", this.getWorkID());

					enOID.InsertAsOID(this.getWorkID());
				}
			}
			return "url@../Comm/En.htm?EnName=" + md.getPTable() + "&PKVal=" + this.getWorkID();
		}

		if (md.getHisFrmType() == FrmType.VSTOForExcel && this.GetRequestVal("IsFreeFrm") == null) {
			String url = "FrmVSTO.htm?1=1" + this.getRequestParasOfAll();
			url = url.replace("&&", "&");
			return "url@" + url;
		}

		if (md.getHisFrmType() == FrmType.WordFrm || md.getHisFrmType() == FrmType.WPSFrm) {
			String no = this.GetRequestVal("NO");
			String urlParas = "OID=" + this.getRefOID() + "&NO=" + no + "&WorkID=" + this.getWorkID() + "&FK_Node="
					+ this.getFK_Node() + "&UserNo=" + WebUser.getNo() + "&SID=" + this.getSID() + "&FK_MapData="
					+ this.getFK_MapData() + "&OIDPKVal=" + this.getOID() + "&FID=" + this.getFID() + "&FK_Flow="
					+ this.getFK_Flow();
			/* 如果是URL. */
			String requestParas = this.getRequestParasOfAll();
			String[] parasArrary = this.getRequestParasOfAll().split("[&]", -1);
			for (String str : parasArrary) {
				if (DataType.IsNullOrEmpty(str) || str	.contains("=") == false) {
					continue;
				}
				String[] kvs = str.split("[=]", -1);
				if (urlParas.contains(kvs[0])) {
					continue;
				}
				urlParas += "&" + kvs[0] + "=" + kvs[1];
			}
			String frm = "FrmWord";
			if (md.getHisFrmType()  == FrmType.WPSFrm)
				frm = "WpsFrm";
			if (md.getUrlExt().contains("?") == true)
				return "url@" + frm + ".htm?1=2" + "&" + urlParas;
			else
				return "url@" + frm + ".htm" + "?" + urlParas;

		}

		if (md.getHisFrmType() == FrmType.ExcelFrm) {
			return "url@FrmExcel.htm?1=2" + this.getRequestParasOfAll();
		}

		/// 判断是否是返回的URL.

		// 处理参数.
		String paras = this.getRequestParasOfAll();
		paras = paras.replace("&DoType=Frm_Init", "");

		/// 流程的独立运行的表单.
		if (this.getFK_Node() != 0 && this.getFK_Node() != 999999) {
			bp.wf.template.FrmNode fn = new FrmNode();
			fn = new FrmNode(this.getFK_Node(), this.getFK_MapData());
			if(fn.getFrmSln()==FrmSln.Readonly)
				paras=paras+"&IsReadonly=1";
			if (fn.getWhoIsPK() != WhoIsPK.OID) {
				// 太爷孙关系
				if (fn.getWhoIsPK() == WhoIsPK.P3WorkID) {
					// 根据PWorkID 获取P3WorkID
					String sql = "Select PWorkID From WF_GenerWorkFlow Where WorkID=(Select PWorkID From WF_GenerWorkFlow Where WorkID="
							+ this.getPWorkID() + ")";
					String p3workID = DBAccess.RunSQLReturnString(sql);
					if (DataType.IsNullOrEmpty(p3workID) == true || p3workID.equals("0")) {
						throw new RuntimeException("err@不存在太爷孙流程关系，请联系管理员检查流程设计是否正确");
					}

					long workID = Long.parseLong(p3workID);
					paras = paras.replace("&OID=" + this.getWorkID(), "&OID=" + workID);
					paras = paras.replace("&WorkID=" + this.getWorkID(), "&WorkID=" + workID);
					paras = paras.replace("&PKVal=" + this.getWorkID(), "&PKVal=" + workID);
				}

				if (fn.getWhoIsPK() == WhoIsPK.P2WorkID) {
					// 根据PWorkID 获取PPWorkID
					GenerWorkFlow gwf = new GenerWorkFlow(this.getPWorkID());
					if (gwf != null && gwf.getPWorkID() != 0) {
						paras = paras.replace("&OID=" + this.getWorkID(), "&OID=" + gwf.getPWorkID());
						paras = paras.replace("&WorkID=" + this.getWorkID(), "&WorkID=" + gwf.getPWorkID());
						paras = paras.replace("&PKVal=" + this.getWorkID(), "&PKVal=" + gwf.getPWorkID());
					} else {
						throw new RuntimeException("err@不存在爷孙流程关系，请联系管理员检查流程设计是否正确");
					}
				}

				if (fn.getWhoIsPK() == WhoIsPK.PWorkID) {
					paras = paras.replace("&OID=" + this.getWorkID(), "&OID=" + this.getPWorkID());
					paras = paras.replace("&WorkID=" + this.getWorkID(), "&WorkID=" + this.getPWorkID());
					paras = paras.replace("&PKVal=" + this.getWorkID(), "&PKVal=" + this.getPWorkID());
				}

				if (fn.getWhoIsPK() == WhoIsPK.FID) {
					paras = paras.replace("&OID=" + this.getWorkID(), "&OID=" + this.getFID());
					paras = paras.replace("&WorkID=" + this.getWorkID(), "&WorkID=" + this.getFID());
					paras = paras.replace("&PKVal=" + this.getWorkID(), "&PKVal=" + this.getFID());
				}

				if ((this.GetRequestVal("ShowFrmType") != null
						&& this.GetRequestVal("ShowFrmType").equals("FrmFool") == true)
						|| md.getHisFrmType() == FrmType.FreeFrm || md.getHisFrmType() == FrmType.FoolForm) {
					if (IsMobile == true) {
						return "url@../FrmView.htm?1=2" + paras;
					}

					if (this.GetRequestValBoolen("Readonly") || this.GetRequestValBoolen("IsEdit")==false) {
						return "url@FrmGener.htm?1=2" + paras;
					} else {
						return "url@FrmGener.htm?1=2" + paras;
					}
				}

				if (md.getHisFrmType() == FrmType.VSTOForExcel || md.getHisFrmType() == FrmType.ExcelFrm) {
					if (this.GetRequestValBoolen("Readonly") || this.GetRequestValBoolen("IsEdit")==false) {
						return "url@FrmVSTO.htm?1=2" + paras;
					} else {
						return "url@FrmVSTO.htm?1=2" + paras;
					}
				}

				if (IsMobile == true) {
					return "url@../FrmView.htm?1=2" + paras;
				}

				if (this.GetRequestValBoolen("Readonly") || this.GetRequestValBoolen("IsEdit")==false) {
					return "url@FrmGener.htm?1=2" + paras;
				} else {
					return "url@FrmGener.htm?1=2" + paras;
				}
			}
		}

		/// 非流程的独立运行的表单.

		/// 非流程的独立运行的表单.

		if (md.getHisFrmType() == FrmType.FreeFrm) {
			if (IsMobile == true) {
				return "url@../FrmView.htm?1=2" + paras;
			}
			if (this.GetRequestValBoolen("Readonly") || this.GetRequestValBoolen("IsEdit")==false) {
				return "url@FrmGener.htm?1=2" + paras;
			} else {
				return "url@FrmGener.htm?1=2" + paras;
			}
		}

		if (md.getHisFrmType() == FrmType.VSTOForExcel || md.getHisFrmType() == FrmType.ExcelFrm) {
			if (this.GetRequestValBoolen("Readonly") || this.GetRequestValBoolen("IsEdit")==false) {
				return "url@FrmVSTO.htm?1=2" + paras;
			} else {
				return "url@FrmVSTO.htm?1=2" + paras;
			}
		}

		if (IsMobile == true) {
			return "url@../FrmView.htm?1=2" + paras;
		}

		if (md.getHisFrmType() == FrmType.ChapterFrm)
		{
			if (this.GetRequestValBoolen("Readonly") || this.GetRequestValBoolen("IsEdit")==false)
				return "url@ChapterFrmView.htm?1=2" + paras;
			else
				return "url@ChapterFrm.htm?1=2" + paras;
		}
		return "url@FrmGener.htm?1=2" + paras;


		/// 非流程的独立运行的表单.

	}

	/** 
	 附件图片
	 
	 @return 
	*/
	public final String FrmImgAthDB_Init() throws Exception {
		String ImgAthPK = this.GetRequestVal("ImgAth");

		FrmImgAthDBs imgAthDBs = new FrmImgAthDBs();
		QueryObject obj = new QueryObject(imgAthDBs);
		obj.AddWhere(FrmImgAthDBAttr.FK_MapData, this.getFK_MapData());
		obj.addAnd();
		obj.AddWhere(FrmImgAthDBAttr.FK_FrmImgAth, ImgAthPK);
		obj.addAnd();
		obj.AddWhere(FrmImgAthDBAttr.RefPKVal, this.getRefPKVal());
		obj.DoQuery();
		DataTable dt = imgAthDBs.ToDataTableField("dt");
		dt.TableName = "FrmImgAthDB";
		return Json.ToJson(dt);
	}
	/** 
	 上传编辑图片
	 
	 @return 
	*/
	public final String FrmImgAthDB_Upload() throws Exception {
		String CtrlID = this.GetRequestVal("CtrlID");
		int zoomW = this.GetRequestValInt("zoomW");
		int zoomH = this.GetRequestValInt("zoomH");
		String myName = "";
		String fk_mapData = this.getFK_MapData();
		if (fk_mapData.contains("ND") == true)
			myName = CtrlID + "_" + this.getRefPKVal();
		else
			myName = fk_mapData + "_" + CtrlID + "_" + this.getRefPKVal();

		// 生成新路径，解决返回相同src后图片不切换问题
		String webPath = "/DataUser/ImgAth/Data/" + myName + ".png";
		String saveToPath = SystemConfig.getPathOfDataUser() + "ImgAth/Data/";
		saveToPath = saveToPath.replace("\\", "/");
		String fileUPloadPath = SystemConfig.getPathOfDataUser() + "ImgAth/Upload/";
		fileUPloadPath = fileUPloadPath.replace("\\", "/");

		// 如果路径不存在就创建,否则拷贝文件报异常
		File sPath = new File(saveToPath);
		File uPath = new File(fileUPloadPath);
		if (!sPath.isDirectory()) {
			sPath.mkdirs();
		}
		if (!uPath.isDirectory()) {
			uPath.mkdirs();
		}

		saveToPath = saveToPath + myName + ".png";
		fileUPloadPath = fileUPloadPath + myName + ".png";
		File sFile = new File(saveToPath);
		File uFile = new File(fileUPloadPath);

		HttpServletRequest request = getRequest();
		String contentType = request.getContentType();
		if (contentType != null && contentType.indexOf("multipart/form-data") != -1) {

			String fileName = CommonFileUtils.getOriginalFilename(request, "file");
			String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
			try {
				CommonFileUtils.upload(request, "file", sFile);
			} catch (Exception e) {
				e.printStackTrace();
				return "err@上传图片保存失败";
			}
		}
		// 复制一份到uFile
		try {
			copyFileUsingFileChannels(sFile, uFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 获取文件大小;
		long fileSize = uFile.length();
		if (fileSize > 0) {
			return "{\"SourceImage\":\"" + webPath + "\"}";
		}
		return "{\"err\":\"没有选择文件\"}";
	}
	public static void copyFileUsingFileChannels(File source, File dest) throws IOException {
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		try {
			inputChannel = new FileInputStream(source).getChannel();
			outputChannel = new FileOutputStream(dest).getChannel();
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());

		} finally {
			inputChannel.close();
			outputChannel.close();
		}
	}
	public final String ImgUpload_Del() throws Exception {
		//执行删除.
		String delPK = this.GetRequestVal("DelPKVal");

		FrmImgAthDB delDB = new FrmImgAthDB();
		delDB.setMyPK(delPK == null ? this.getMyPK() : delPK);
		delDB.RetrieveFromDBSources();
		delDB.Delete(); //删除上传的文件.

		String saveToPath = SystemConfig.getPathOfWebApp() + (Glo.getCCFlowAppPath() + "DataUser/ImgAth/Data");

		File fileInfo = new File(saveToPath + "/" + delDB.getFileName());
		fileInfo.delete();
		return "删除成功.";
	}
	/** 
	 剪切图片
	 
	 @return 
	*/
	public final String FrmImgAthDB_Cut() throws Exception {
		String CtrlID = this.GetRequestVal("CtrlID");

		int zoomW = this.GetRequestValInt("zoomW");
		int zoomH = this.GetRequestValInt("zoomH");
		int x = this.GetRequestValInt("cX");
		int y = this.GetRequestValInt("cY");
		int w = this.GetRequestValInt("cW");
		int h = this.GetRequestValInt("cH");

		String newName = "";
		String fk_mapData = this.getFK_MapData();
		String fileFullName = "";
		if (fk_mapData.contains("ND") == true)
		{
			newName = CtrlID + "_" + this.getRefPKVal();
		}
		else
		{
			newName = fk_mapData + "_" + CtrlID + "_" + this.getRefPKVal();
		}
		//string newName = ImgAthPK + "_" + this.MyPK + "_" + DateTime.Now.ToString("yyyyMMddHHmmss");
		String webPath = Glo.getCCFlowAppPath() + "DataUser/ImgAth/Data/" + newName + ".png";
		String savePath = SystemConfig.getCCFlowAppPath() + "DataUser/ImgAth/Data/" + newName + ".png";
		//获取上传的大图片
		//string strImgPath = this.context.Server.MapPath(bp.difference.SystemConfig.CCFlowWebPath + "DataUser/ImgAth/Upload/" + newName + ".png");
		String strImgPath = SystemConfig.getPathOfWebApp() + SystemConfig.getCCFlowWebPath() + "DataUser/ImgAth/Upload/" + newName + ".png";
//		if ((new File(strImgPath)).isFile() == true)
//		{
//			//剪切图
//			boolean bSuc = Crop(strImgPath, savePath, w, h, x, y);
//			//imgAthDB.FileFullName = webPath;
//			//imgAthDB.Update();
//			return webPath;
//		}
		return webPath;
	}

	/** 
	 剪裁图像
	 
	 param Img
	 param Width
	 param Height
	 param X
	 param Y
	 @return 
	*/
//	private boolean Crop(String Img, String savePath, int Width, int Height, int X, int Y)
//	{
//		try
//		{
//			try (var OriginalImage = new System.Drawing.Bitmap(Img))
//			{
//				try (var bmp = new System.Drawing.Bitmap(Width, Height, OriginalImage.PixelFormat))
//				{
//					bmp.SetResolution(OriginalImage.HorizontalResolution, OriginalImage.VerticalResolution);
//					try (System.Drawing.Graphics Graphic = System.Drawing.Graphics.FromImage(bmp))
//					{
//						Graphic.SmoothingMode = System.Drawing.Drawing2D.SmoothingMode.AntiAlias;
//						Graphic.InterpolationMode = System.Drawing.Drawing2D.InterpolationMode.HighQualityBicubic;
//						Graphic.PixelOffsetMode = System.Drawing.Drawing2D.PixelOffsetMode.HighQuality;
//						Graphic.DrawImage(OriginalImage, new System.Drawing.Rectangle(0, 0, Width, Height), X, Y, Width, Height, System.Drawing.GraphicsUnit.Pixel);
//						//var ms = new MemoryStream();
//						bmp.Save(savePath);
//						//return ms.GetBuffer();
//						return true;
//					}
//				}
//			}
//		}
//		catch (RuntimeException Ex)
//		{
//			throw (Ex);
//		}
//		return false;
//	}

		///#endregion frm.htm 主表.


		///#region DtlFrm
	public final String DtlFrm_Init() throws Exception {
		long pk = this.getRefOID();
		if (pk == 0)
		{
			pk = this.getOID();
		}
		if (pk == 0)
		{
			pk = this.getWorkID();
		}

		if (pk != 0)
		{
			return FrmGener_Init();
		}

		MapDtl dtl = new MapDtl(this.getEnsName());

		GEEntity en = new GEEntity(this.getEnsName());
		if (SystemConfig.getIsBSsystem() == true)
		{
			// 处理传递过来的参数。
			for (String k : CommonUtils.getRequest().getParameterMap().keySet())
			{
				en.SetValByKey(k, bp.sys.Glo.getRequest().getParameter(k));
			}
		}



		//设置主键.
		en.setOID(DBAccess.GenerOID(this.getEnsName()));

			///#region 处理权限方案。
		if (this.getFK_Node() != 0 && this.getFK_Node() != 999999)
		{
			Node nd = new Node(this.getFK_Node());
			if (nd.getHisFormType() == NodeFormType.SheetTree || nd.getHisFormType() == NodeFormType.RefOneFrmTree)
			{
				FrmNode fn = new FrmNode(nd.getNodeID(), this.getFK_MapData());
				if (fn.getFrmSln() == FrmSln.Self)
				{
					String no = this.getEnsName() + "_" + nd.getNodeID();
					MapDtl mdtlSln = new MapDtl();
					mdtlSln.setNo(no);
					int result = mdtlSln.RetrieveFromDBSources();
					if (result != 0)
					{
						dtl = mdtlSln;
					}
				}
			}
		}

			///#endregion 处理权限方案。
		//给从表赋值.
		switch (dtl.getDtlOpenType())
		{
			case ForEmp: // 按人员来控制.

				en.SetValByKey("RefPK", this.getRefPKVal());
				en.SetValByKey("FID", this.getRefPKVal());
				break;
			case ForWorkID: // 按工作ID来控制
				en.SetValByKey("RefPK", this.getRefPKVal());
				en.SetValByKey("FID", this.getRefPKVal());
				break;
			case ForFID: // 按流程ID来控制.
				en.SetValByKey("RefPK", this.getRefPKVal());
				en.SetValByKey("FID", this.getFID());
				break;
		}
		en.SetValByKey("RefPK", this.getRefPKVal());

		en.Insert();

		return "url@DtlFrm.htm?EnsName=" + this.getEnsName() + "&RefPKVal=" + this.getRefPKVal() + "&FrmType=" + dtl.getHisEditModel().getValue() + "&OID=" + en.getOID()+"&IsNew=1";
	}

	public final String DtlFrm_Delete() throws Exception {
		try
		{
			GEEntity en = new GEEntity(this.getEnsName());
			en.setOID(this.getOID());
			en.Delete();

			//如果可以上传附件这删除相应的附件信息
			FrmAttachmentDBs dbs = new FrmAttachmentDBs();
			dbs.Delete(FrmAttachmentDBAttr.FK_MapData, this.getEnsName(), FrmAttachmentDBAttr.RefPKVal, this.getRefOID(), FrmAttachmentDBAttr.NodeID, this.getFK_Node());


			return "删除成功.";
		}
		catch (RuntimeException ex)
		{
			return "err@删除错误:" + ex.getMessage();
		}
	}


		///#endregion DtlFrm


		///#region frmFree
	/** 
	 实体类的初始化
	 
	 @return 
	*/
	public final String FrmGener_Init_ForBPClass() throws Exception {
		try
		{

			MapData md = new MapData(this.getEnsName());
			DataSet ds = CCFormAPI.GenerHisDataSet(md.getNo(), null, null);


				///#region 把主表数据放入.
			String atParas = "";
			Entities ens = ClassFactory.GetEns(this.getEnsName());
			Entity en = ens.getGetNewEntity();
			en.setPKVal(this.getPKVal());

			if (en.RetrieveFromDBSources() == 0)
			{
				en.Insert();
			}

			//把参数放入到 En 的 Row 里面。
			if (DataType.IsNullOrEmpty(atParas) == false)
			{
				AtPara ap = new AtPara(atParas);
				for (String key : ap.getHisHT().keySet())
				{
					if (en.getRow().containsKey(key) == true) //有就该变.
					{
						en.getRow().SetValByKey(key, ap.GetValStrByKey(key));
					}
					else
					{
						en.getRow().put(key, ap.GetValStrByKey(key)); //增加他.
					}
				}
			}

			if (SystemConfig.getIsBSsystem() == true)
			{
				// 处理传递过来的参数。
				for (String k : CommonUtils.getRequest().getParameterMap().keySet())
				{
					en.SetValByKey(k, bp.sys.Glo.getRequest().getParameter(k));
				}
			}

			// 执行表单事件. FrmLoadBefore .
			String msg = ExecEvent.DoFrm(md, EventListFrm.FrmLoadBefore, en);
			if (DataType.IsNullOrEmpty(msg) == false)
			{
				return "err@错误:" + msg;
			}


			//重设默认值.
			en.ResetDefaultVal(null, null, 0);

			//执行装载填充.
			MapExt me = new MapExt();
			if (me.Retrieve(MapExtAttr.ExtType, MapExtXmlList.PageLoadFull, MapExtAttr.FK_MapData, this.getEnsName()) == 1)
			{
				//执行通用的装载方法.
				MapAttrs attrs = new MapAttrs(this.getEnsName());
				MapDtls dtls = new MapDtls(this.getEnsName());
				Entity tempVar = Glo.DealPageLoadFull(en, me, attrs, dtls);
				en = tempVar instanceof GEEntity ? (GEEntity)tempVar : null;
			}

			//执行事件
			ExecEvent.DoFrm(md, EventListFrm.SaveBefore, en, null);


			//增加主表数据.
			DataTable mainTable = en.ToDataTableField(md.getNo());
			mainTable.TableName = "MainTable";

			ds.Tables.add(mainTable);

				///#endregion 把主表数据放入.


				///#region 把外键表加入DataSet
			DataTable dtMapAttr = ds.GetTableByName("Sys_MapAttr");

			MapExts mes = md.getMapExts();
			DataTable ddlTable = new DataTable();
			ddlTable.Columns.Add("No");
			for (DataRow dr : dtMapAttr.Rows)
			{
				String lgType = dr.getValue("LGType").toString();
				if (lgType.equals("2") == false)
				{
					continue;
				}

				String UIIsEnable = dr.getValue("UIVisible").toString();
				if (UIIsEnable.equals("0"))
				{
					continue;
				}

				//string lgType = dr[MapAttrAttr.LGType].ToString();
				//if (lgType == "0")
				//    continue

				String uiBindKey = dr.getValue("UIBindKey").toString();
				if (DataType.IsNullOrEmpty(uiBindKey) == true)
				{
					String myPK = dr.getValue("MyPK").toString();
					/*如果是空的*/
					//   throw new Exception("@属性字段数据不完整，流程:" + fl.No + fl.Name + ",节点:" + nd.NodeID + nd.Name + ",属性:" + myPK + ",的UIBindKey IsNull ");
				}

				// 检查是否有下拉框自动填充。
				String keyOfEn = dr.getValue("KeyOfEn").toString();
				String fk_mapData = dr.getValue("FK_MapData").toString();


					///#region 处理下拉框数据范围. for 小杨.
				Object tempVar2 = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL, MapExtAttr.AttrOfOper, keyOfEn);
				me = tempVar2 instanceof MapExt ? (MapExt)tempVar2 : null;
				if (me != null)
				{
					Object tempVar3 = me.getDoc();
					String fullSQL = tempVar3 instanceof String ? (String)tempVar3 : null;
					fullSQL = fullSQL.replace("~", ",");
					fullSQL = Glo.DealExp(fullSQL, en, null);
					DataTable dt = DBAccess.RunSQLReturnTable(fullSQL);
					dt.TableName = keyOfEn; //可能存在隐患，如果多个字段，绑定同一个表，就存在这样的问题.
					ds.Tables.add(dt);
					continue;
				}

					///#endregion 处理下拉框数据范围.

				// 判断是否存在.
				if (ds.contains(uiBindKey) == true)
				{
					continue;
				}

				DataTable dataTable = bp.pub.PubClass.GetDataTableByUIBineKey(uiBindKey, null);
				if (dataTable != null)
				{
					ds.Tables.add(dataTable);
				}
				else
				{
					DataRow ddldr = ddlTable.NewRow();
					ddldr.setValue("No", uiBindKey);
					ddlTable.Rows.add(ddldr);
				}
			}
			ddlTable.TableName = "UIBindKey";
			ds.Tables.add(ddlTable);

				///#endregion End把外键表加入DataSet

			return bp.tools.Json.ToJson(ds);
		}
		catch (RuntimeException ex)
		{
			GEEntity myen = new GEEntity(this.getEnsName());
			myen.CheckPhysicsTable();

			CCFormAPI.RepareCCForm(this.getEnsName());
			return "err@装载表单期间出现如下错误 FrmGener_Init_ForBPClass ,ccform有自动诊断修复功能请在刷新一次，如果仍然存在请联系管理员. @" + ex.getMessage();
		}
	}

	public final String FrmGener_Init_ForDBList() throws Exception {
		try
		{
			DBList dblist = new DBList(this.getEnsName());
			MapData md = new MapData(this.getEnsName());
			DataSet ds = CCFormAPI.GenerHisDataSet(dblist.getNo(), null, null);


				///#region 把主表数据放入.
			String atParas = "";
			GEEntity en = new GEEntity(this.getEnsName());

			String pk = this.GetRequestVal("OID");
			if (DataType.IsNullOrEmpty(pk) == true)
			{
				pk = this.GetRequestVal("WorkID");
			}

			en.SetValByKey("OID",pk);


			String expEn = dblist.getExpEn();
			expEn = expEn.replace("@Key", pk);
			if (expEn.contains("@") == true)
			{
				expEn = expEn.replace("@WebUser.No", WebUser.getNo());
				expEn = expEn.replace("@WebUser.Name", WebUser.getName());
				expEn = expEn.replace("@WebUser.FK_DeptName", WebUser.getFK_DeptName());
				expEn = expEn.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
			}
			if (dblist.getDBType() == 0)
			{
				expEn = expEn.replace("~", "'");
				if (DataType.IsNullOrEmpty(dblist.getDBSrc()) == true)
				{
					dblist.setDBSrc("local");
				}

				SFDBSrc sf = new SFDBSrc(dblist.getDBSrc());
				DataTable dt = sf.RunSQLReturnTable(expEn);
				if (dt.Rows.size() == 1)
				{
					//把DataTable中的内容转换成Entity的值
					for (MapAttr attr : md.getMapAttrs().ToJavaList())
					{
						if (dt.Columns.contains(attr.getKeyOfEn()) == true)
						{
							en.getRow().SetValByKey(attr.getKeyOfEn(), dt.Rows.get(0).getValue(attr.getKeyOfEn()));
						}
					}
				}
			}

			if (dblist.getDBType() == 1)
			{
				if (expEn.contains("http") == false)
				{
					/*如果没有绝对路径 */
					if (SystemConfig.getIsBSsystem())
					{
						/*在cs模式下自动获取*/
						String host = WebContralBase.getRequest().getRemoteHost(); //BP.Sys.Base.Glo.Request.Url.Host;
						if (expEn.contains("@AppPath"))
						{
							expEn = expEn.replace("@AppPath", "http://" + host + CommonUtils.getRequest().getRequestURI()); //BP.Sys.Base.Glo.Request.ApplicationPath
						}
						else
						{
							expEn = "http://" + bp.sys.Glo.getRequest().getRemoteHost() + expEn;
						}
					}

					if (SystemConfig.getIsBSsystem() == false)
					{
						/*在cs模式下它的baseurl 从web.config中获取.*/
						String cfgBaseUrl = SystemConfig.getAppSettings().get("HostURL").toString();
						if (DataType.IsNullOrEmpty(cfgBaseUrl))
						{
							String err = "调用url失败:没有在web.config中配置BaseUrl,导致url事件不能被执行.";
							Log.DebugWriteError(err);
							throw new RuntimeException(err);
						}
						expEn = cfgBaseUrl + expEn;
					}
				}
				//System.Text.Encoding encode = System.Text.Encoding.GetEncoding("UTF-8");
				String json = DataType.ReadURLContext(expEn, 8000);
				if (DataType.IsNullOrEmpty(json) == false)
				{
					DataTable dt = Json.ToDataTable(json);
					if (dt.Rows.size() == 1)
					{
						//把DataTable中的内容转换成Entity的值
						for (MapAttr attr : md.getMapAttrs().ToJavaList())
						{
							if (dt.Columns.contains(attr.getKeyOfEn()) == true)
							{
								en.getRow().SetValByKey(attr.getKeyOfEn(), dt.Rows.get(0).getValue(attr.getKeyOfEn()));
							}
						}
					}
				}

			}


			//把参数放入到 En 的 Row 里面。
			if (DataType.IsNullOrEmpty(atParas) == false)
			{
				AtPara ap = new AtPara(atParas);
				for (String key : ap.getHisHT().keySet())
				{
					if (en.getRow().containsKey(key) == true) //有就该变.
					{
						en.getRow().SetValByKey(key, ap.GetValStrByKey(key));
					}
					else
					{
						en.getRow().put(key, ap.GetValStrByKey(key)); //增加他.
					}
				}
			}

			if (SystemConfig.getIsBSsystem() == true)
			{
				// 处理传递过来的参数。
				for (String k : CommonUtils.getRequest().getParameterMap().keySet())
				{
					en.SetValByKey(k, bp.sys.Glo.getRequest().getParameter(k));
				}
			}

			// 执行表单事件. FrmLoadBefore .
			en.SetPara("FrmType","DBList");
			String msg = ExecEvent.DoFrm(md, EventListFrm.FrmLoadBefore, en);
			if (DataType.IsNullOrEmpty(msg) == false)
				return "err@错误:" + msg;


			//增加主表数据.
			DataTable mainTable = en.ToDataTableField(md.getNo());
			mainTable.TableName = "MainTable";

			ds.Tables.add(mainTable);

				///#endregion 把主表数据放入.


				///#region 把外键表加入DataSet
			DataTable dtMapAttr = ds.GetTableByName("Sys_MapAttr");

			MapExts mes = md.getMapExts();
			DataTable ddlTable = new DataTable();
			ddlTable.Columns.Add("No");
			for (DataRow dr : dtMapAttr.Rows)
			{
				String lgType = dr.getValue("LGType").toString();
				if (lgType.equals("2") == false)
				{
					continue;
				}

				String UIIsEnable = dr.getValue("UIVisible").toString();
				if (UIIsEnable.equals("0"))
				{
					continue;
				}


				String uiBindKey = dr.getValue("UIBindKey").toString();
				if (DataType.IsNullOrEmpty(uiBindKey) == true)
				{
					String myPK = dr.getValue("MyPK").toString();

				}

				// 检查是否有下拉框自动填充。
				String keyOfEn = dr.getValue("KeyOfEn").toString();
				String fk_mapData = dr.getValue("FK_MapData").toString();


					///#region 处理下拉框数据范围. for 小杨.
				Object tempVar = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL, MapExtAttr.AttrOfOper, keyOfEn);
				MapExt me = tempVar instanceof MapExt ? (MapExt)tempVar : null;
				if (me != null)
				{
					Object tempVar2 = me.getDoc();
					String fullSQL = tempVar2 instanceof String ? (String)tempVar2 : null;
					fullSQL = fullSQL.replace("~", ",");
					fullSQL = Glo.DealExp(fullSQL, en, null);
					DataTable dt = DBAccess.RunSQLReturnTable(fullSQL);
					dt.TableName = keyOfEn; //可能存在隐患，如果多个字段，绑定同一个表，就存在这样的问题.
					ds.Tables.add(dt);
					continue;
				}

					///#endregion 处理下拉框数据范围.

				// 判断是否存在.
				if (ds.contains(uiBindKey) == true)
				{
					continue;
				}

				DataTable dataTable = bp.pub.PubClass.GetDataTableByUIBineKey(uiBindKey, null);
				if (dataTable != null)
				{
					ds.Tables.add(dataTable);
				}
				else
				{
					DataRow ddldr = ddlTable.NewRow();
					ddldr.setValue("No", uiBindKey);
					ddlTable.Rows.add(ddldr);
				}
			}
			ddlTable.TableName = "UIBindKey";
			ds.Tables.add(ddlTable);

				///#endregion End把外键表加入DataSet

			return Json.ToJson(ds);
		}
		catch (RuntimeException ex)
		{
			GEEntity myen = new GEEntity(this.getEnsName());
			myen.CheckPhysicsTable();

			CCFormAPI.RepareCCForm(this.getEnsName());
			return "err@装载表单期间出现如下错误 FrmGener_Init_ForDBList ,ccform有自动诊断修复功能请在刷新一次，如果仍然存在请联系管理员. @" + ex.getMessage();
		}
	}
	/** 
	 表单打开
	 
	 @return 
	*/
	public final String WpsFrm_Init() throws Exception {
		long workID = this.getWorkID();

		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		if (gwf.getPWorkID() != 0)
		{
			workID = gwf.getPWorkID();
		}
		/**获得表单wps.
		*/
		MapFrmWps md = new MapFrmWps(this.getFrmID());

		//首先从数据表里获取二进制表单实例.
		String file = SystemConfig.getPathOfTemp() + "\\" + workID + "." + this.getFrmID() + ".docx";

		String templateFilePath = SystemConfig.getPathOfCyclostyleFile() + md.getNo() + "." + md.getMyFileExt();

		//生成文件.
		byte[] val = DBAccess.GetFileFromDB(file, md.getPTable(), "OID", String.valueOf(workID), "DBFile");
		if (val == null)
		{
			FileAccess.Copy(templateFilePath,file);
		}

		return "/DataUser/Temp/" + workID + "." + this.getFrmID() + ".docx";
	}
	/** 
	 保存文件
	 
	 @return 
	*/
	public final String WpsFrm_SaveFile() throws Exception {
		// string fileName = "c:\\xxxx\temp.px";
		long workID = this.getWorkID();

		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		if (gwf.getPWorkID() != 0)
		{
			workID = gwf.getPWorkID();
		}

		String fileName = SystemConfig.getPathOfTemp() + "\\" + workID + "." + this.getFrmID() + ".docx";

		MapData md = new MapData(this.getFrmID());
		HttpServletRequest request = getRequest();


		File file =null;
		try {
			file = File.createTempFile(SystemConfig.getPathOfTemp() + "\\" + workID + "." + this.getFrmID(), ".docx");
		} catch (IOException e1) {
			file = new File(System.getProperty("java.io.tmpdir"), fileName);
		}
		//CommonFileUtils.upload(request, "File_Upload", file);
		/*HttpPostedFile file = HttpContextHelper.RequestFiles().get(0); //context.Request.Files;
		file.SaveAs(fileName);*/
		CommonFileUtils.upload(request, "file", file);
		//保存文件.
		DBAccess.SaveFileToDB(fileName, md.getPTable(), "OID", String.valueOf(workID), "DBFile");

		return "上传成功.";
	}

	/** 
	 执行数据初始化
	 
	 @return 
	*/
	public final String FrmGener_Init() throws Exception {
		if (this.getFK_MapData() != null && this.getFK_MapData().toUpperCase().contains("BP.") == true) {
			return FrmGener_Init_ForBPClass();
		}
		MapData md = new MapData(this.getEnsName());
		if (md.getEntityType() == EntityType.DBList)
			return FrmGener_Init_ForDBList();

		/// 定义流程信息的所用的 配置entity.
		// 节点与表单的权限控制.
		FrmNode fn = null;

		// 是否启用装载填充？
		boolean isLoadData = true;
		// 定义节点变量.
		Node nd = null;
		if (this.getFK_Node() != 0 && this.getFK_Node() != 999999) {
			nd = new Node(this.getFK_Node());
			nd.WorkID = this.getWorkID(); // 为获取表单ID ( NodeFrmID )提供参数.
			// if (nd.HisFormType== NodeFormType.FoolTruck)

			fn = new FrmNode(this.getFK_Node(), this.getFK_MapData());
			isLoadData = fn.isEnableLoadData();
		}

		/// 定义流程信息的所用的 配置entity.

		try {

			/// 特殊判断 适应累加表单.
			String fromWhere = this.GetRequestVal("FromWorkOpt");
			if (fromWhere != null && fromWhere.equals("1") && this.getFK_Node() != 0 && this.getFK_Node() != 999999) {
				// 如果是累加表单.
				if (nd.getHisFormType() == NodeFormType.FoolTruck) {
					DataSet myds = bp.wf.CCFlowAPI.GenerWorkNode(this.getFK_Flow(), nd, this.getWorkID(), this.getFID(),
							WebUser.getNo(), this.getWorkID(), this.GetRequestVal("FromWorkOpt"), false);

					return bp.tools.Json.ToJson(myds);
				}
			}

			/// 特殊判断.适应累加表单.

			DataSet ds = bp.sys.CCFormAPI.GenerHisDataSet(md.getNo());

			// 主表实体.
			GEEntity en = new GEEntity(this.getEnsName());

			long pk = this.getRefOID();
			if (pk == 0) {
				pk = this.getOID();
			}
			if (pk == 0) {
				pk = this.getWorkID();
			}

			/// 根据who is pk 获取数据.
			en.setOID(pk);
			if (en.getOID() == 0) {
				en.ResetDefaultVal();
			} else {
				if (en.RetrieveFromDBSources() == 0) {
					en.Insert();
				}
			}

			// 如果有框架
			if (ds.GetTableByName("Sys_MapFrame").Rows.size() > 0) {
				// 把流程信息表发送过去.
				GenerWorkFlow gwf = new GenerWorkFlow();
				gwf.setWorkID(pk);
				gwf.RetrieveFromDBSources();
				ds.Tables.add(gwf.ToDataTableField("WF_GenerWorkFlow"));
			}

			/// 根据who is pk 获取数据.

			/// 附加参数数据.
			if (SystemConfig.getIsBSsystem() == true) {
				// 处理传递过来的参数。
				Enumeration enu = ContextHolderUtils.getRequest().getParameterNames();
				while (enu.hasMoreElements()) {
					String key = (String) enu.nextElement();
					en.SetValByKey(key, ContextHolderUtils.getRequest().getParameter(key));
				}
			}

			// 执行表单事件. FrmLoadBefore .
			String msg = ExecEvent.DoFrm(md, EventListFrm.FrmLoadBefore, en);
			if (DataType.IsNullOrEmpty(msg) == false) {
				return "err@错误:" + msg;
			}

			// 重设默认值.
			if (this.GetRequestValBoolen("IsReadonly") == false) {
				en.ResetDefaultVal();
			}

			/// 附加参数数据.

			/// 执行装载填充.与相关的事件.
			MapExts mes = md.getMapExts();

			Object tempVar = mes.GetEntityByKey("ExtType", MapExtXmlList.PageLoadFull);
			MapExt me = tempVar instanceof MapExt ? (MapExt) tempVar : null;
			if (isLoadData == true && md.isPageLoadFull() && me != null && GetRequestValInt("IsTest") != 1) {
				// 执行通用的装载方法.
				MapAttrs attrs = new MapAttrs(this.getEnsName());
				MapDtls dtls = new MapDtls(this.getEnsName());
				// 判断是否自定义权限.
				boolean IsSelf = false;
				// 单据或者是单据实体表单
				if (nd == null) {
					Entity tempVar2 = bp.wf.Glo.DealPageLoadFull(en, me, attrs, dtls, IsSelf, 0, this.getWorkID());
					en = tempVar2 instanceof GEEntity ? (GEEntity) tempVar2 : null;
				} else {
					if ((nd.getHisFormType() == NodeFormType.SheetTree
							|| nd.getHisFormType() == NodeFormType.RefOneFrmTree) && (fn.getFrmSln() == FrmSln.Self)) {
						IsSelf = true;
					}
					Entity tempVar3 = bp.wf.Glo.DealPageLoadFull(en, me, attrs, dtls, IsSelf, nd.getNodeID(),
							this.getWorkID());
					en = tempVar3 instanceof GEEntity ? (GEEntity) tempVar3 : null;
				}
			}

			// 执行事件, 不应该加.
			if (1 == 2) {
				ExecEvent.DoFrm(md, EventListFrm.SaveBefore, en, null);
			}

			/// 执行装载填充.与相关的事件.

			/// 把外键表加入 DataSet.
			DataTable dtMapAttr = ds.GetTableByName("Sys_MapAttr");

			DataTable ddlTable = new DataTable();
			ddlTable.Columns.Add("No");
			for (DataRow dr : dtMapAttr.Rows) {
				String lgType = dr.getValue("LGType").toString();
				String uiBindKey = dr.getValue("UIBindKey").toString();

				String uiVisible = dr.getValue("UIVisible").toString();
				if (uiVisible.equals("0") == true) {
					continue;
				}

				if (DataType.IsNullOrEmpty(uiBindKey) == true) {
					continue; // 为空就continue.
				}

				if (lgType.equals("1") == true) {
					continue; // 枚举值就continue;
				}

				String uiIsEnable = dr.getValue("UIIsEnable").toString();
				if (uiIsEnable.equals("0") == true && lgType.equals("1") == true) {
					continue; // 如果是外键，并且是不可以编辑的状态.
				}

				if (uiIsEnable.equals("0") == true && lgType.equals("0") == true) {
					continue; // 如果是外部数据源，并且是不可以编辑的状态.
				}

				// 检查是否有下拉框自动填充。
				String keyOfEn = dr.getValue("KeyOfEn").toString();
				String fk_mapData = dr.getValue("FK_MapData").toString();

				/// 处理下拉框数据范围. for 小杨.
				Object tempVar4 = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL,
						MapExtAttr.AttrOfOper, keyOfEn);
				me = tempVar4 instanceof MapExt ? (MapExt) tempVar4 : null;
				if (me != null) {
					Object tempVar5 = me.getDoc();
					String fullSQL = tempVar5 instanceof String ? (String) tempVar5 : null;
					fullSQL = fullSQL.replace("~", ",");
					fullSQL = bp.wf.Glo.DealExp(fullSQL, en, null);
					DataTable dt = DBAccess.RunSQLReturnTable(fullSQL);
					if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.UpperCase) {
						if (dt.Columns.contains("NO") == true) {
							dt.Columns.get("NO").setColumnName("No");
						}
						if (dt.Columns.contains("NAME") == true) {
							dt.Columns.get("NAME").setColumnName("Name");
						}
						if (dt.Columns.contains("PARENTNO") == true) {
							dt.Columns.get("PARENTNO").setColumnName("ParentNo");
						}
					}

					if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.Lowercase) {
						if (dt.Columns.contains("no") == true) {
							dt.Columns.get("no").setColumnName("No");
						}
						if (dt.Columns.contains("name") == true) {
							dt.Columns.get("name").setColumnName("Name");
						}
						if (dt.Columns.contains("parentno") == true) {
							dt.Columns.get("parentno").setColumnName("ParentNo");
						}
					}

					dt.TableName = keyOfEn; // 可能存在隐患，如果多个字段，绑定同一个表，就存在这样的问题.
					ds.Tables.add(dt);
					continue;
				}

				/// 处理下拉框数据范围.

				// 判断是否存在.
				if (ds.GetTableByName(uiBindKey)!=null) {
					continue;
				}

				DataTable dataTable = PubClass.GetDataTableByUIBineKey(uiBindKey);

				if (dataTable != null) {
					ds.Tables.add(dataTable);
				} else {
					DataRow ddldr = ddlTable.NewRow();
					ddldr.setValue("No", uiBindKey);
					ddlTable.Rows.add(ddldr);
				}
			}
			ddlTable.TableName = "UIBindKey";
			ds.Tables.add(ddlTable);

			/// End把外键表加入DataSet

			/// 加入组件的状态信息, 在解析表单的时候使用.
			if (nd != null && fn.isEnableFWC() != FrmWorkCheckSta.Disable) {
				bp.wf.template.FrmNodeComponent fnc = new FrmNodeComponent(nd.getNodeID());
				if (!nd.getNodeFrmID().equals("ND" + nd.getNodeID())) {
					/* 说明这是引用到了其他节点的表单，就需要把一些位置元素修改掉. */
					int refNodeID = 0;
					if (nd.getNodeFrmID().indexOf("ND") == -1) {
						refNodeID = nd.getNodeID();
					} else {
						refNodeID = Integer.parseInt(nd.getNodeFrmID().replace("ND", ""));
					}

					bp.wf.template.FrmNodeComponent refFnc = new FrmNodeComponent(refNodeID);

					fnc.SetValByKey(NodeWorkCheckAttr.FWC_H, refFnc.GetValFloatByKey(NodeWorkCheckAttr.FWC_H));

					fnc.SetValByKey(FrmSubFlowAttr.SF_H, refFnc.GetValFloatByKey(FrmSubFlowAttr.SF_H));
					fnc.SetValByKey(FrmTrackAttr.FrmTrack_H, refFnc.GetValFloatByKey(FrmTrackAttr.FrmTrack_H));

					fnc.SetValByKey(FTCAttr.FTC_H, refFnc.GetValFloatByKey(FTCAttr.FTC_H));

					if (md.getHisFrmType() == FrmType.FoolForm) {
						// 判断是否是傻瓜表单，如果是，就要判断该傻瓜表单是否有审核组件groupfield ,没有的话就增加上.
						DataTable gf = ds.GetTableByName("Sys_GroupField");
						boolean isHave = false;
						for (DataRow dr : gf.Rows) {
							String cType = dr.getValue("CtrlType") instanceof String ? (String) dr.getValue("CtrlType")
									: null;
							if (cType == null) {
								continue;
							}

							if (cType.equals("FWC") == true) {
								isHave = true;
							}
						}

						if (isHave == false) {
							DataRow dr = gf.NewRow();

							nd.WorkID = this.getWorkID(); // 为获取表单ID提供参数.
							dr.setValue(GroupFieldAttr.OID, 100);
							dr.setValue(GroupFieldAttr.FrmID, nd.getNodeFrmID());
							dr.setValue(GroupFieldAttr.CtrlType, "FWC");
							dr.setValue(GroupFieldAttr.CtrlID, "FWCND" + nd.getNodeID());
							dr.setValue(GroupFieldAttr.Idx, 100);
							dr.setValue(GroupFieldAttr.Lab, "审核信息");
							gf.Rows.add(dr);

							ds.Tables.remove("Sys_GroupField");
							ds.Tables.add(gf);

							// 执行更新,就自动生成那个丢失的字段分组.
							refFnc.Update(); // 这里生成到了NDxxx表单上去了。

						}
					}
				}

				/// 没有审核组件分组就增加上审核组件分组.
				if (fn.isEnableFWC() != FrmWorkCheckSta.Disable) {
					// 如果启用了审核组件，并且 节点表单与当前一致。
					if (md.getHisFrmType() == FrmType.FoolForm) {
						// 判断是否是傻瓜表单，如果是，就要判断该傻瓜表单是否有审核组件groupfield ,没有的话就增加上.
						DataTable gf = ds.GetTableByName("Sys_GroupField");
						boolean isHave = false;
						for (DataRow dr : gf.Rows) {
							String cType = dr.getValue("CtrlType") instanceof String ? (String) dr.getValue("CtrlType")
									: null;
							if (cType == null) {
								continue;
							}

							if (cType.equals("FWC") == true) {
								isHave = true;
							}
						}

						if (isHave == false) {
							DataRow dr = gf.NewRow();

							nd.WorkID = this.getWorkID(); // 为获取表单ID提供参数.
							dr.setValue(GroupFieldAttr.OID, 100);
							dr.setValue(GroupFieldAttr.FrmID, nd.getNodeFrmID());
							dr.setValue(GroupFieldAttr.CtrlType, "FWC");
							dr.setValue(GroupFieldAttr.CtrlID, "FWCND" + nd.getNodeID());
							dr.setValue(GroupFieldAttr.Idx, 100);
							dr.setValue(GroupFieldAttr.Lab, "审核信息");
							gf.Rows.add(dr);

							ds.Tables.remove("Sys_GroupField");
							ds.Tables.add(gf);

							// 更新,为了让其表单上自动增加审核分组.
							bp.wf.template.FrmNodeComponent refFnc = new FrmNodeComponent(nd.getNodeID());
							NodeWorkCheck fwc = new NodeWorkCheck(nd.getNodeID());
							if (fn.getFrmSln() == FrmSln.Self || fn.getFrmSln() == FrmSln.Default) {
								fwc.setHisFrmWorkCheckSta(FrmWorkCheckSta.Enable);
							} else {
								fwc.setHisFrmWorkCheckSta(FrmWorkCheckSta.Readonly);
							}

							refFnc.Update();
						}
					}
				}

				/// 没有审核组件分组就增加上审核组件分组.
				ds.Tables.add(fnc.ToDataTableField("WF_FrmNodeComponent"));
			}
			if (nd != null) {
				ds.Tables.add(nd.ToDataTableField("WF_Node"));
			}

			if (nd != null) {
				ds.Tables.add(fn.ToDataTableField("WF_FrmNode"));
			}

			/// 加入组件的状态信息, 在解析表单的时候使用.

			/// 处理权限方案
			if (nd != null && nd.getFormType() == NodeFormType.SheetTree) {

				/// 只读方案.
				if (fn.getFrmSln() == FrmSln.Readonly) {
					for (DataRow dr : dtMapAttr.Rows) {
						dr.setValue(MapAttrAttr.UIIsEnable, 0);
					}

					// 改变他的属性. 不知道是否应该这样写？
					ds.Tables.remove("Sys_MapAttr");
					ds.Tables.add(dtMapAttr);
				}

				/// 只读方案.

				/// 自定义方案.
				if (fn.getFrmSln() == FrmSln.Self) {
					// 查询出来自定义的数据.
					FrmFields ffs = new FrmFields();
					ffs.Retrieve(FrmFieldAttr.FK_Node, nd.getNodeID(), FrmFieldAttr.FK_MapData, md.getNo());

					// 遍历属性集合.
					for (DataRow dr : dtMapAttr.Rows) {
						String keyOfEn = dr.getValue(MapAttrAttr.KeyOfEn).toString();
						for (FrmField ff : ffs.ToJavaList()) {
							if (ff.getKeyOfEn().equals(keyOfEn) == false) {
								continue;
							}

							dr.setValue(MapAttrAttr.UIIsEnable, ff.getUIIsEnable()); // 是否只读?
							dr.setValue(MapAttrAttr.UIVisible, ff.getUIVisible()); // 是否可见?
							dr.setValue(MapAttrAttr.UIIsInput, ff.isNotNull()); // 是否必填?

							dr.setValue(MapAttrAttr.DefVal, ff.getDefVal()); // 默认值.

							Attr attr = new Attr();
							attr.setMyDataType(DataType.AppString);
							attr.setDefaultValOfReal(ff.getDefVal());
							attr.setKey(ff.getKeyOfEn());

							if (dr.getValue(MapAttrAttr.UIIsEnable).toString().equals("0")) {
								attr.setUIIsReadonly(true);
							} else {
								attr.setUIIsReadonly(false);
							}

							// 处理默认值.
							if (DataType.IsNullOrEmpty(ff.getDefVal()) == true) {
								continue;
							}

							// 数据类型.
							attr.setMyDataType(Integer.parseInt(dr.getValue(MapAttrAttr.MyDataType).toString()));
							String v = ff.getDefVal();

							// 设置默认值.
							String myval = en.GetValStrByKey(ff.getKeyOfEn());

							// 设置默认值.
							switch (ff.getDefVal()) {
								case "@WebUser.No":
									if (attr.getUIIsReadonly() == true) {
										en.SetValByKey(attr.getKey(), WebUser.getNo());
									} else {
										if (DataType.IsNullOrEmpty(myval) || myval.equals(v)) {
											en.SetValByKey(attr.getKey(), WebUser.getNo());
										}
									}
									continue;
								case "@WebUser.Name":
									if (attr.getUIIsReadonly() == true) {
										en.SetValByKey(attr.getKey(), WebUser.getName());
									} else {
										if (DataType.IsNullOrEmpty(myval) || myval.equals(v)) {
											en.SetValByKey(attr.getKey(), WebUser.getName());
										}
									}
									continue;
								case "@WebUser.FK_Dept":
									if (attr.getUIIsReadonly() == true) {
										en.SetValByKey(attr.getKey(), WebUser.getFK_Dept());
									} else {
										if (DataType.IsNullOrEmpty(myval) || myval.equals(v)) {
											en.SetValByKey(attr.getKey(), WebUser.getFK_Dept());
										}
									}
									continue;
								case "@WebUser.FK_DeptName":
									if (attr.getUIIsReadonly() == true) {
										en.SetValByKey(attr.getKey(), WebUser.getFK_DeptName());
									} else {
										if (DataType.IsNullOrEmpty(myval) || myval.equals(v)) {
											en.SetValByKey(attr.getKey(), WebUser.getFK_DeptName());
										}
									}
									continue;
								case "@WebUser.FK_DeptNameOfFull":
								case "@WebUser.FK_DeptFullName":
									if (attr.getUIIsReadonly() == true) {
										en.SetValByKey(attr.getKey(), WebUser.getFK_DeptNameOfFull());
									} else {
										if (DataType.IsNullOrEmpty(myval) || myval.equals(v)) {
											en.SetValByKey(attr.getKey(), WebUser.getFK_DeptNameOfFull());
										}
									}
									continue;
								case "@RDT":
									if (attr.getUIIsReadonly() == true) {
										if (attr.getMyDataType() == DataType.AppDate || myval.equals(v)) {
											en.SetValByKey(attr.getKey(), DataType.getCurrentDate());
										}

										if (attr.getMyDataType() == DataType.AppDateTime || myval.equals(v)) {
											en.SetValByKey(attr.getKey(), DataType.getCurrentDataTime());
										}
									} else {
										if (DataType.IsNullOrEmpty(myval) || myval.equals(v)) {
											if (attr.getMyDataType() == DataType.AppDate) {
												en.SetValByKey(attr.getKey(), DataType.getCurrentDate());
											} else {
												en.SetValByKey(attr.getKey(), DataType.getCurrentDataTime());
											}
										}
									}
									continue;
								case "@yyyy年MM月dd日":
								case "@yyyy年MM月dd日HH时mm分":
								case "@yy年MM月dd日":
								case "@yy年MM月dd日HH时mm分":
									if (attr.getUIIsReadonly() == true) {
										en.SetValByKey(attr.getKey(), DataType.getCurrentDateByFormart(v.replace("@", "")));
									} else {
										if (DataType.IsNullOrEmpty(myval) || myval.equals(v)) {
											en.SetValByKey(attr.getKey(),
													DataType.getCurrentDateByFormart(v.replace("@", "")));
										}
									}
									continue;
								default:
									continue;
							}
						}
					}

					// 改变他的属性. 不知道是否应该这样写？
					ds.Tables.remove("Sys_MapAttr");
					ds.Tables.add(dtMapAttr);

					// 处理radiobutton的模式的控件.
				}

				/// 自定义方案.

			}

			/// 处理权限方案s

			/// 加入主表的数据.
			// 增加主表数据.
			DataTable mainTable = en.ToDataTableField("MainTable");
			ds.Tables.add(mainTable);

			/// 加入主表的数据.

			String json = bp.tools.Json.ToJson(ds);

			// bp.da.DataType.WriteFile("c:\\aaa.txt", json);
			return json;
		} catch (RuntimeException ex) {
			GEEntity myen = new GEEntity(this.getEnsName());
			myen.CheckPhysicsTable();

			bp.sys.CCFormAPI.RepareCCForm(this.getEnsName());
			return "err@装载表单期间出现如下错误,ccform有自动诊断修复功能请在刷新一次，如果仍然存在请联系管理员. @" + ex.getMessage();
		}
	}
	public final String FrmFreeReadonly_Init() throws Exception {
		try
		{
			MapData md = new MapData(this.getEnsName());
			DataSet ds = CCFormAPI.GenerHisDataSet(md.getNo(), null, null);


				///#region 把主表数据放入.
			String atParas = "";
			//主表实体.
			GEEntity en = new GEEntity(this.getEnsName());


				///#region 求出 who is pk 值.
			long pk = this.getRefOID();
			if (pk == 0)
			{
				pk = this.getOID();
			}
			if (pk == 0)
			{
				pk = this.getWorkID();
			}

			if (this.getFK_Node() != 0 && DataType.IsNullOrEmpty(this.getFK_Flow()) == false)
			{
				/*说明是流程调用它， 就要判断谁是表单的PK.*/
				FrmNode fn = new FrmNode(this.getFK_Node(), this.getFK_MapData());
				switch (fn.getWhoIsPK())
				{
					case FID:
						pk = this.getFID();
						if (pk == 0)
						{
							throw new RuntimeException("@没有接收到参数FID");
						}
						break;
					case PWorkID: //父流程ID
						pk = this.getPWorkID();
						if (pk == 0)
						{
							throw new RuntimeException("@没有接收到参数PWorkID");
						}
						break;
					case OID:
					default:
						break;
				}
			}

				///#endregion  求who is PK.

			en.setOID(pk);
			en.RetrieveFromDBSources();

			//增加主表数据.
			DataTable mainTable = en.ToDataTableField(md.getNo());
			mainTable.TableName = "MainTable";

			ds.Tables.add(mainTable);

				///#endregion 把主表数据放入.

			return Json.ToJson(ds);
		}
		catch (RuntimeException ex)
		{
			GEEntity myen = new GEEntity(this.getEnsName());
			myen.CheckPhysicsTable();

			CCFormAPI.RepareCCForm(this.getEnsName());
			return "err@装载表单期间出现如下错误FrmFreeReadonly_Init,ccform有自动诊断修复功能请在刷新一次，如果仍然存在请联系管理员. @" + ex.getMessage();
		}
	}
	/** 
	 执行保存
	 
	 @return 
	*/
	public final String FrmGener_Save() throws Exception {
		try
		{
			//保存主表数据.
			GEEntity en = new GEEntity(this.getEnsName());


				///#region 求出 who is pk 值.
			long pk = this.getRefOID();
			if (pk == 0)
			{
				pk = this.getOID();
			}
			if (pk == 0)
			{
				pk = this.getWorkID();
			}

			if (this.getFK_Node() != 0 && DataType.IsNullOrEmpty(this.getFK_Flow()) == false)
			{
				/*说明是流程调用它， 就要判断谁是表单的PK.*/
				FrmNode fn = new FrmNode(this.getFK_Node(), this.getFK_MapData());
				switch (fn.getWhoIsPK())
				{
					case FID:
						pk = this.getFID();
						if (pk == 0)
						{
							throw new RuntimeException("@没有接收到参数FID");
						}
						break;
					case PWorkID: //父流程ID
						pk = this.getPWorkID();
						if (pk == 0)
						{
							throw new RuntimeException("@没有接收到参数PWorkID");
						}
						break;
					case OID:
					default:
						break;
				}

				if (fn.getFrmSln() == FrmSln.Readonly)
				{
					/*如果是不可以编辑*/
					return "";
				}
			}

				///#endregion  求who is PK.

			en.setOID(pk);
			int i = en.RetrieveFromDBSources();
			en.ResetDefaultVal(null, null, 0);

			try
			{
				Hashtable ht = bp.pub.PubClass.GetMainTableHT();
				for (Object item : ht.keySet())
				{
					en.SetValByKey(item.toString(), ht.get(item));
				}
			}
			catch (RuntimeException ex)
			{
				return "err@方法：MyBill_SaveIt错误，在执行  GetMainTableHT 期间" + ex.getMessage();
			}

			en.setOID(pk);

			// 处理表单保存前事件.
			MapData md = new MapData(this.getEnsName());


				///#region 调用事件.
			//是不是从表的保存.
			if (this.GetRequestValInt("IsForDtl") == 1)
			{

					///#region 从表保存前处理事件.
				//获得主表事件.
				FrmEvents fes = new FrmEvents(this.getEnsName()); //获得事件.
				GEEntity mainEn = null;
				if (!fes.isEmpty())
				{
					String msg = fes.DoEventNode(EventListFrm.DtlRowSaveBefore, en);
					if (DataType.IsNullOrEmpty(msg) == false)
					{
						return "err@" + msg;
					}
				}

				MapDtl mdtl = new MapDtl(this.getEnsName());
				if (mdtl.getFEBD().length() != 0)
				{
					String str = mdtl.getFEBD();
					bp.sys.base.FormEventBaseDtl febd = bp.sys.base.Glo.GetFormDtlEventBaseByEnName(mdtl.getNo());

					if (febd != null)
					{
						febd.HisEn = mdtl.GenerGEMainEntity(this.getRefPKVal());
						febd.HisEnDtl = en;

						febd.DoIt(EventListFrm.DtlRowSaveBefore, febd.HisEn, en, null);
					}
				}

					///#endregion 从表保存前处理事件.
			}
			else
			{
				ExecEvent.DoFrm(md, EventListFrm.SaveBefore, en);
			}

				///#endregion 调用事件
			if (i == 0)
			{
				en.Insert();
			}
			else
			{
				en.Update();
			}


				///#region 调用事件.
			if (this.GetRequestValInt("IsForDtl") == 1)
			{

					///#region 从表保存前处理事件.
				//获得主表事件.
				FrmEvents fes = new FrmEvents(this.getEnsName()); //获得事件.
				GEEntity mainEn = null;
				if (!fes.isEmpty())
				{
					String msg = fes.DoEventNode(EventListFrm.DtlRowSaveBefore, en);
					if (DataType.IsNullOrEmpty(msg) == false)
					{
						return "err@" + msg;
					}
				}

				MapDtl mdtl = new MapDtl(this.getEnsName());

				if (mdtl.getFEBD().length() != 0)
				{
					String str = mdtl.getFEBD();
					bp.sys.base.FormEventBaseDtl febd = bp.sys.base.Glo.GetFormDtlEventBaseByEnName(mdtl.getNo());

					if (febd != null)
					{
						febd.HisEn = mdtl.GenerGEMainEntity(this.getRefPKVal());
						febd.HisEnDtl = en;

						febd.DoIt(EventListFrm.DtlRowSaveAfter, febd.HisEn, en, null);
					}
				}

					///#endregion 从表保存前处理事件.
			}
			else
			{
				ExecEvent.DoFrm(md, EventListFrm.SaveAfter, en);
			}

				///#endregion 调用事件

			return "保存成功.";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}

		///#endregion


		///#region dtl.htm 从表.
	/** 
	 初始化从表数据
	 
	 @return 返回结果数据
	*/
	public final String Dtl_Init() throws Exception {
		DataSet ds = Dtl_Init_Dataset();
		return Json.ToJson(ds);
	}
	private DataSet Dtl_Init_Dataset() throws Exception {

			///#region 组织参数.
		MapDtl mdtl = new MapDtl(this.getEnsName());
		mdtl.setNo(this.getEnsName());

		String dtlRefPKVal = this.getRefPKVal(); //从表的RefPK


			///#region 如果是测试，就创建表.
		if (this.getFK_Node() == 999999 || this.GetRequestVal("IsTest") != null)
		{
			GEDtl dtl = new GEDtl(mdtl.getNo());
			dtl.CheckPhysicsTable();
			dtlRefPKVal = "0";
		}

			///#endregion 如果是测试，就创建表.

		String frmID = mdtl.getFK_MapData();

		if (this.getFK_Node() != 0 && this.getFK_Node() != 999999)
		{
			frmID = frmID.replace("_" + this.getFK_Node(), "");
		}

		if (this.getFK_Node() != 0 && mdtl.getFK_MapData().equals("Temp") == false && this.getFK_Node() != 999999)
		{
			Node nd = new Node(this.getFK_Node());

			Flow flow = new Flow(nd.getFK_Flow());

			FrmNode fn = new FrmNode(nd.getNodeID(), frmID);

			if (flow.getFlowDevModel() == FlowDevModel.JiJian || nd.getHisFormType() == NodeFormType.SheetTree || nd.getHisFormType() == NodeFormType.RefOneFrmTree || nd.getHisFormType() == NodeFormType.FoolTruck)
			{
				/*如果
				 * 1,传来节点ID, 不等于0.
				 * 2,不是节点表单.  就要判断是否是独立表单，如果是就要处理权限方案。*/
				if (fn.getFrmSln() == FrmSln.Readonly)
				{
					mdtl.setIsInsert(false);
					mdtl.setIsDelete(false);
					mdtl.setIsUpdate(false);
					mdtl.setReadonly(true);
				}

				/**自定义权限.
				*/
				if (fn.getFrmSln() == FrmSln.Self)
				{
					mdtl.setNo(this.getEnsName() + "_" + this.getFK_Node());
					if (mdtl.RetrieveFromDBSources() == 0)
					{
						mdtl.setNo(this.getEnsName());
						/*如果设置了自定义方案，但是没有定义，从表属性，就需要去默认值. */
					}
				}

			}
			dtlRefPKVal = Dev2Interface.GetDtlRefPKVal(this.getWorkID(), this.getPWorkID(), this.getFID(), this.getFK_Node(), frmID, mdtl);
			if (dtlRefPKVal.equals("0") == true || DataType.IsNullOrEmpty(dtlRefPKVal))
			{
				dtlRefPKVal = this.getRefPKVal();
			}

		}

		if (this.GetRequestValInt("IsReadonly") == 1)
		{
			mdtl.setIsInsert(false);
			mdtl.setIsDelete(false);
			mdtl.setIsUpdate(false);
		}


		String strs = this.getRequestParas();
		strs = strs.replace("?", "@");
		strs = strs.replace("&", "@");

			///#endregion 组织参数.

		//获得他的描述,与数据.
		DataSet ds = bp.wf.CCFormAPI.GenerDBForCCFormDtl(frmID, mdtl,
				Integer.parseInt(this.getRefPKVal()), strs,
				dtlRefPKVal,this.getFID());
		return ds;
	}
	/** 
	 执行从表的保存.
	 
	 @return 
	*/
	public final String Dtl_Save() throws Exception {
		MapDtl mdtl = new MapDtl(this.getEnsName());
		GEDtls dtls = new GEDtls(this.getEnsName());
		FrmEvents fes = new FrmEvents(this.getEnsName()); //获得事件.
		GEEntity mainEn = null;


			///#region 从表保存前处理事件.
		if (!fes.isEmpty())
		{
			mainEn = mdtl.GenerGEMainEntity(this.getRefPKVal());
			String msg = fes.DoEventNode(EventListFrm.DtlRowSaveBefore, mainEn);
			if (msg != null)
			{
				throw new RuntimeException(msg);
			}
		}

			///#endregion 从表保存前处理事件.


			///#region 保存的业务逻辑.


			///#endregion 保存的业务逻辑.

		return "保存成功";
	}

	public String Dtl_ChangePopAndAthIdx()
	{
		int oldRowIdx = GetRequestValInt("oldRowIdx");
		int newRowIdx = GetRequestValInt("newRowIdx");
		//dtl生成oid后，将pop弹出的FrmEleDB表中的数据用oid替换掉
		String refval = this.getWorkID() + "_" + oldRowIdx;
		String newRefVal = this.getWorkID() + "_" + newRowIdx;
		//处理从表行数据插入成功后，更新FrmEleDB中数据
		String dbstr = SystemConfig.getAppCenterDBVarStr();
		Paras paras = new Paras();
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL || SystemConfig.getAppCenterDBType() == DBType.UX || SystemConfig.getAppCenterDBType() == DBType.KingBaseR3 || SystemConfig.getAppCenterDBType() == DBType.KingBaseR6)
			paras.SQL = "UPDATE Sys_FrmEleDB SET RefPKVal=" + dbstr + "RefPKVal,MyPK=EleID||'_'||'" + newRefVal + "'||'_'||Tag1  WHERE RefPKVal=" + dbstr + "OldRefPKVal";
		else if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
			paras.SQL = "UPDATE Sys_FrmEleDB SET RefPKVal=" + dbstr + "RefPKVal,MyPK=CONCAT(EleID,'_','" + newRefVal + "','_',Tag1)  WHERE RefPKVal=" + dbstr + "OldRefPKVal";
		else
			paras.SQL = "UPDATE Sys_FrmEleDB SET RefPKVal=" + dbstr + "RefPKVal,MyPK= EleID+'_'+'" + newRefVal + "'+'_'+Tag1  WHERE RefPKVal=" + dbstr + "OldRefPKVal";
		paras.Add("RefPKVal", newRefVal);
		paras.Add("OldRefPKVal", refval);
		DBAccess.RunSQL(paras);
		//处理从表行数据插入成功后，更新FrmAttachmentDB中数据
		paras.SQL = "UPDATE Sys_FrmAttachmentDB SET RefPKVal=" + dbstr + "RefPKVal  WHERE RefPKVal=" + dbstr + "OldRefPKVal AND FK_MapData="+dbstr+"FK_MapData";
		paras.Add("FK_MapData", this.getFK_MapData());
		DBAccess.RunSQL(paras);
		return "执行成功";
	}

	/// <summary>
	/// 从表移动
	/// </summary>
	/// <returns></returns>
	public String DtlList_Move()
	{
		int newIdx= this.GetRequestValInt("newIdx");
		int oldIdx = this.GetRequestValInt("oldIdx");
		long newOID = this.GetRequestValInt("newOID");
		long oldOID = this.GetRequestValInt("oldOID");
		//处理从表行数据插入成功后，更新FrmAttachmentDB中数据
		Paras paras = new Paras();
		String dbstr = SystemConfig.getAppCenterDBVarStr();
		paras.Add("FK_MapData", this.getFK_MapData());
		String athSQL ="UPDATE Sys_FrmAttachmentDB SET RefPKVal=" + dbstr + "RefPKVal  WHERE RefPKVal=" + dbstr + "OldRefPKVal AND FK_MapData=" + dbstr + "FK_MapData";
		String eleSQL = "UPDATE Sys_FrmEleDB SET RefPKVal=" + dbstr + "RefPKVal  WHERE RefPKVal=" + dbstr + "OldRefPKVal AND FK_MapData=" + dbstr + "FK_MapData";

		if (newOID == 0 && oldOID == 0)
		{
			//先改变oldID的附件信息
                /*paras.Add("RefPKVal", this.RefPKVal + "_" + oldIdx + "_"+ oldIdx);
                paras.Add("OldRefPKVal", this.RefPKVal + "_" + oldIdx);
                paras.SQL = athSQL;
                DBAccess.RunSQL(paras);
                paras.SQL = eleSQL;
                DBAccess.RunSQL(paras);*/

			paras.Add("RefPKVal", this.getRefPKVal() + "_" + newIdx+"_"+newIdx);
			paras.Add("OldRefPKVal", this.getRefPKVal() + "_" + oldIdx);
			paras.SQL = athSQL;
			DBAccess.RunSQL(paras);
			paras.SQL = eleSQL;
			DBAccess.RunSQL(paras);
			paras.Add("RefPKVal", this.getRefPKVal() + "_" + oldIdx+"_"+oldIdx);
			paras.Add("OldRefPKVal", this.getRefPKVal() + "_" + newIdx);
			paras.SQL = athSQL;
			DBAccess.RunSQL(paras);
			paras.SQL = eleSQL;
			DBAccess.RunSQL(paras);
			return "从表拖拽完成";
		}
		if(newOID == 0)
		{
			paras.Add("RefPKVal", this.getRefPKVal() + "_" + newIdx+"_"+newIdx);
			paras.Add("OldRefPKVal", this.getRefPKVal() + "_" + oldIdx);
			paras.SQL = athSQL;
			DBAccess.RunSQL(paras);
			paras.SQL = eleSQL;
			DBAccess.RunSQL(paras);

		}
		if (oldOID == 0)
		{
			paras.Add("RefPKVal", this.getRefPKVal() + "_" + oldIdx+"_"+oldIdx);
			paras.Add("OldRefPKVal", this.getRefPKVal() + "_" + newIdx);
		}
		paras.SQL = athSQL;
		DBAccess.RunSQL(paras);
		paras.SQL = eleSQL;
		DBAccess.RunSQL(paras);
		return "从表拖拽完成";
	}

	public String DtlList_MoveAfter()
	{
		Paras paras = new Paras();
		paras.Add("FK_MapData", this.getFK_MapData());
		int newIdx = this.GetRequestValInt("newIdx");
		String dbstr = SystemConfig.getAppCenterDBVarStr();
		String athSQL = "";
		String eleSQL = "";
		switch (SystemConfig.getAppCenterDBType())
		{
			case MSSQL:
				athSQL = "UPDATE Sys_FrmAttachmentDB SET RefPKVal=SUBSTRING(RefPKVal,0,CHARINDEX('_', RefPKVal, CHARINDEX('_', RefPKVal) + 1))  WHERE RefPKVal LIKE " + dbstr + "RefPKVal+'[_]%[_]%' AND FK_MapData=" + dbstr + "FK_MapData";
				eleSQL = "UPDATE Sys_FrmEleDB SET RefPKVal=SUBSTRING(RefPKVal,0,CHARINDEX('_', RefPKVal, CHARINDEX('_', RefPKVal) + 1))  WHERE RefPKVal LIKE "+dbstr+ "RefPKVal+'[_]%[_]%' AND FK_MapData=" + dbstr + "FK_MapData";
				break;
			case Oracle:
			case KingBaseR3:
			case KingBaseR6:
				athSQL = "UPDATE Sys_FrmAttachmentDB SET RefPKVal=SUBSTRING(RefPKVal,0,INSERT(RefPKVal,'_',1,2))  WHERE RefPKVal LIKE " + dbstr + "RefPKVal||'\\_%\\_%'  ESCAPE '\\' AND FK_MapData=" + dbstr + "FK_MapData";
				eleSQL = "UPDATE Sys_FrmEleDB SET RefPKVal=SUBSTRING(RefPKVal,0,INSERT(RefPKVal,'_',1,2))  WHERE RefPKVal LIKE " + dbstr + "RefPKVal||'\\_%\\_%'  ESCAPE '\\' AND FK_MapData=" + dbstr + "FK_MapData";
				break;
			case MySQL:
				athSQL = "UPDATE Sys_FrmAttachmentDB SET RefPKVal=SUBSTRING(RefPKVal,1,LOCATE('_', RefPKVal, LOCATE('_', RefPKVal)+1)-1)  WHERE RefPKVal LIKE CONCAT(" + dbstr + "RefPKVal,'\\\\_%\\\\_%') AND FK_MapData=" + dbstr + "FK_MapData";
				eleSQL = "UPDATE Sys_FrmEleDB SET RefPKVal=SUBSTRING(RefPKVal,1,LOCATE('_', RefPKVal, LOCATE('_', RefPKVal)+1)-1)  WHERE RefPKVal LIKE CONCAT(" + dbstr + "RefPKVal,'\\\\_%\\\\_%') AND FK_MapData=" + dbstr + "FK_MapData";
				break;
			case PostgreSQL:
			case HGDB:
				athSQL = "UPDATE Sys_FrmAttachmentDB SET RefPKVal=SUBSTRING(RefPKVal,1,CHAR_LENGTH(RefPKVal)-POSITION('_' in REVERSE(RefPKVal)))  WHERE RefPKVal LIKE CONCAT(" + dbstr + "RefPKVal,'!_%!_%')  ESCAPE '!' AND FK_MapData=" + dbstr + "FK_MapData";
				eleSQL = "UPDATE Sys_FrmEleDB SET RefPKVal=SUBSTRING(RefPKVal,1,CHAR_LENGTH(RefPKVal)-POSITION('_' in REVERSE(RefPKVal)))  WHERE RefPKVal LIKE CONCAT(" + dbstr + "RefPKVal,'!_%!_%')  ESCAPE '!' AND FK_MapData=" + dbstr + "FK_MapData";
				break;
			default:
				return "err@" + SystemConfig.getAppCenterDBType() + "还未解析";
		}
		paras.Add("RefPKVal", this.getRefPKVal());
		paras.SQL = athSQL;
		DBAccess.RunSQL(paras);
		paras.SQL = eleSQL;
		DBAccess.RunSQL(paras);
		return "";
	}

	/** 
	 导出excel与附件信息,并且压缩一个压缩包.
	 
	 @return 返回下载路径
	*/
	public final String Dtl_ExpZipFiles() throws Exception {
		DataSet ds = Dtl_Init_Dataset();

		return "err@尚未wancheng.";
	}
	/** 
	 保存单行数据
	 
	 @return 
	*/
	public final String Dtl_SaveRow() throws Exception {
		//从表.
		String fk_mapDtl = this.getFK_MapDtl();
		String RowIndex = this.GetRequestVal("RowIndex");
		MapDtl mdtl = new MapDtl(fk_mapDtl);
		String dtlRefPKVal = this.getRefPKVal();

		String isRead = mdtl.getRow().GetValByKey(MapDtlAttr.IsReadonly).toString();
		if (DataType.IsNullOrEmpty(isRead) == false && "1".equals(isRead) == true)
		{
			return "";
		}
			///#region 处理权限方案。
		if (this.getFK_Node() != 0 && this.getFK_Node() != 999999 && mdtl.getNo().contains("ND") == false)
		{
			Node nd = new Node(this.getFK_Node());
			if (nd.getHisFormType() == NodeFormType.SheetTree || nd.getHisFormType() == NodeFormType.RefOneFrmTree)
			{
				FrmNode fn = new FrmNode(nd.getNodeID(), this.getFK_MapData());
				if (fn.getFrmSln() == FrmSln.Self)
				{
					String no = fk_mapDtl + "_" + nd.getNodeID();
					MapDtl mdtlSln = new MapDtl();
					mdtlSln.setNo(no);
					int result = mdtlSln.RetrieveFromDBSources();
					if (result != 0)
					{
						mdtl = mdtlSln;
						fk_mapDtl = no;
					}
				}
			}

			dtlRefPKVal = Dev2Interface.GetDtlRefPKVal(this.getWorkID(), this.getPWorkID(), this.getFID(), this.getFK_Node(), this.getFK_MapData(), mdtl);
			if (dtlRefPKVal.equals("0") == true)
			{
				dtlRefPKVal = this.getRefPKVal();
			}
		}

			///#endregion 处理权限方案。

		if (mdtl.isReadonly() == true)
		{
			return "err@不允许保存.IsReadonly=1," + mdtl.getName() + " ID:" + mdtl.getNo();
		}

		if (mdtl.getIsInsert() == false && mdtl.getIsUpdate() == false)
		{
			return "err@不允许保存. IsInsert=false,IsUpdate=false " + mdtl.getName() + " ID:" + mdtl.getNo();
		}

		//从表实体.
		GEDtl dtl = new GEDtl(fk_mapDtl);
		int oid = this.getRefOID();
		if (oid != 0)
		{
			dtl.setOID(oid);
			dtl.RetrieveFromDBSources();
		}
		else
		{
			//关联主赋值.
			dtl.setRefPK(dtlRefPKVal);
			if (this.getFID() == 0)
			{
				dtl.setFID(Long.parseLong(dtl.getRefPK()));
			}
			else
			{
				dtl.setFID(this.getFID());
			}

			//如果是新建，并且里面是ForFID的模式.
			if (mdtl.getDtlOpenType() == DtlOpenType.ForFID)
			{
				dtl.setFID(Long.parseLong(dtlRefPKVal));
			}
		}


			///#region 给实体循环赋值/并保存.
		Attrs attrs = dtl.getEnMap().getAttrs();
		for (Attr attr : attrs.ToJavaList())
		{
			if (attr.getKey().equals("FID") == true || attr.getKey().equals("RefPK") == true)
			{
				continue;
			}
			String val = this.GetRequestVal(attr.getKey());
			if(DataType.IsNullOrEmpty(val)==true)
				dtl.SetValByKey(attr.getKey(),"");
			else
				dtl.SetValByKey(attr.getKey(),URLDecoder.decode(val,"UTF-8"));
		}

		dtl.SetValByKey("Idx", RowIndex);
		///#region 从表保存前处理事件.
		//获得主表事件.
		FrmEvents fes = new FrmEvents(fk_mapDtl); //获得事件.
		GEEntity mainEn = null;
		if (!fes.isEmpty())
		{
			mainEn = mdtl.GenerGEMainEntity(this.getRefPKVal());
			String msg = fes.DoEventNode(EventListFrm.DtlRowSaveBefore, mainEn);
			if (DataType.IsNullOrEmpty(msg) == false)
			{
				return "err@" + msg;
			}
		}

		if (mdtl.getFEBD().length() != 0)
		{
			String str = mdtl.getFEBD();
			bp.sys.base.FormEventBaseDtl febd = bp.sys.base.Glo.GetFormDtlEventBaseByEnName(mdtl.getNo());

			febd.HisEn = mdtl.GenerGEMainEntity(this.getRefPKVal());
			febd.HisEnDtl = dtl;

			febd.DoIt(EventListFrm.DtlRowSaveBefore, febd.HisEn, dtl, null);
		}

			///#endregion 从表保存前处理事件.

		//一直找不到refpk  值为null .
		if (DataType.IsNullOrEmpty(dtl.getRefPK()) == true)
		{
			dtl.setRefPK(this.getRefPKVal());
		}

		if (dtl.getOID() == 0)
		{
			try
			{
				dtl.Insert();
			}
			catch (RuntimeException ex)
			{
				return "err@在保存行:" + RowIndex + " 错误: " + ex.getMessage();
			}

			//dtl生成oid后，将pop弹出的FrmEleDB表中的数据用oid替换掉
			String refval = this.getRefPKVal() + "_" + RowIndex;
			//处理从表行数据插入成功后，更新FrmEleDB中数据
			String dbstr = SystemConfig.getAppCenterDBVarStr();
			Paras paras = new Paras();
			DBType dbType = SystemConfig.getAppCenterDBType();
			if ( dbType== DBType.Oracle || dbType == DBType.PostgreSQL || dbType == DBType.HGDB || dbType == DBType.UX ||dbType== DBType.KingBaseR3 || dbType == DBType.KingBaseR6)
				paras.SQL = "UPDATE Sys_FrmEleDB SET RefPKVal=" + dbstr + "RefPKVal,MyPK=EleID||'_'||"+ dtl.getOID()+ "||'_'||Tag1  WHERE RefPKVal=" + dbstr + "OldRefPKVal AND FK_MapData=" + dbstr + "FK_MapData";
			else if (dbType == DBType.MySQL)
				paras.SQL = "UPDATE Sys_FrmEleDB SET RefPKVal=" + dbstr + "RefPKVal,MyPK=CONCAT(EleID,'_'," + dtl.getOID() + ",'_',Tag1)  WHERE RefPKVal=" + dbstr + "OldRefPKVal AND FK_MapData=" + dbstr + "FK_MapData";
			else
				paras.SQL = "UPDATE Sys_FrmEleDB SET RefPKVal=" + dbstr + "RefPKVal,MyPK= EleID+'_'+'" + dtl.getOID() + "'+'_'+Tag1  WHERE RefPKVal=" + dbstr + "OldRefPKVal AND FK_MapData=" + dbstr + "FK_MapData";
			paras.Add("RefPKVal", String.valueOf(dtl.getOID()));
			paras.Add("OldRefPKVal", refval);
			paras.Add("FK_MapData", fk_mapDtl);
			DBAccess.RunSQL(paras);
			//处理从表行数据插入成功后，更新FrmAttachmentDB中数据
			paras.SQL = "UPDATE Sys_FrmAttachmentDB SET RefPKVal=" + dbstr + "RefPKVal  WHERE RefPKVal=" + dbstr + "OldRefPKVal AND FK_MapData=" + dbstr + "FK_MapData";
			DBAccess.RunSQL(paras);
		}
		else
		{
			dtl.Update();
		}

			///#endregion 给实体循环赋值/并保存.


			///#region 从表保存后处理事件。
		//页面定义的事件.
		if (!fes.isEmpty())
		{
			String msg = fes.DoEventNode(EventListFrm.DtlRowSaveAfter, mainEn);
			if (DataType.IsNullOrEmpty(msg) == false)
			{
				return "err@" + msg;
			}
		}
		//事件实体类.
		if (mdtl.getFEBD().length() != 0)
		{
			String str = mdtl.getFEBD();
			FormEventBaseDtl febd = bp.sys.Glo.GetFormDtlEventBaseByEnName(mdtl.getNo());

			febd.HisEn = mdtl.GenerGEMainEntity(this.getRefPKVal());
			febd.HisEnDtl = dtl;
			febd.DoIt(EventListFrm.DtlRowSaveAfter, febd.HisEn, dtl, null);
		}
			///#endregion 处理事件.

		//返回当前数据存储信息.
		return dtl.ToJson(true);
	}
	/** 
	 删除
	 
	 @return 
	*/
	public final String Dtl_DeleteRow() throws Exception {
		GEDtl dtl = new GEDtl(this.getFK_MapDtl());
		dtl.setOID(this.getRefOID());


			///#region 从表 删除 前处理事件.
		//获得主表事件.
		FrmEvents fes = new FrmEvents(this.getFK_MapDtl()); //获得事件.
		GEEntity mainEn = null;
		if (!fes.isEmpty())
		{
			String msg = fes.DoEventNode(EventListFrm.DtlRowDelBefore, dtl);
			if (DataType.IsNullOrEmpty(msg) == false)
			{
				return "err@" + msg;
			}
		}

		MapDtl mdtl = new MapDtl(this.getFK_MapDtl());
		if (mdtl.getFEBD().length() != 0)
		{
			String str = mdtl.getFEBD();
			bp.sys.base.FormEventBaseDtl febd = bp.sys.base.Glo.GetFormDtlEventBaseByEnName(mdtl.getNo());
			if (febd != null)
			{
				febd.HisEn = mdtl.GenerGEMainEntity(this.getRefPKVal());
				febd.HisEnDtl = dtl;
				febd.DoIt(EventListFrm.DtlRowDelBefore, febd.HisEn, dtl, null);
			}
		}

			///#endregion 从表 删除 前处理事件.

		//执行删除.
		dtl.Delete();


			///#region 从表 删除 后处理事件.
		//获得主表事件.
		fes = new FrmEvents(this.getFK_MapDtl()); //获得事件.
		if (!fes.isEmpty())
		{
			String msg = fes.DoEventNode(EventListFrm.DtlRowDelAfter, dtl);
			if (DataType.IsNullOrEmpty(msg) == false)
			{
				return "err@" + msg;
			}
		}

		if (mdtl.getFEBD().length() != 0)
		{
			String str = mdtl.getFEBD();
			bp.sys.base.FormEventBaseDtl febd = bp.sys.base.Glo.GetFormDtlEventBaseByEnName(mdtl.getNo());
			if (febd != null)
			{
				febd.HisEn = mdtl.GenerGEMainEntity(this.getRefPKVal());
				febd.HisEnDtl = dtl;

				febd.DoIt(EventListFrm.DtlRowDelAfter, febd.HisEn, dtl, null);
			}
		}


			///#endregion 从表 删除 后处理事件.

		//如果有pop，删除相关存储
		FrmEleDBs FrmEleDBs = new FrmEleDBs();
		QueryObject qo = new QueryObject(FrmEleDBs);
		qo.AddWhere(FrmEleDBAttr.FK_MapData, this.getFK_MapDtl());
		qo.addAnd();
		qo.AddWhere(FrmEleDBAttr.RefPKVal, String.valueOf(dtl.getOID()));
		qo.DoQuery();
		if (FrmEleDBs != null && !FrmEleDBs.isEmpty())
		{
			for (FrmEleDB FrmEleDB : FrmEleDBs.ToJavaList())
			{
				FrmEleDB.Delete();
			}
		}
		//如果可以上传附件这删除相应的附件信息
		FrmAttachmentDBs dbs = new FrmAttachmentDBs();
		dbs.Delete(FrmAttachmentDBAttr.FK_MapData, this.getFK_MapDtl(), FrmAttachmentDBAttr.RefPKVal, this.getRefOID());
		return "删除成功";
	}
	/** 
	 重新获取单个ddl数据
	 
	 @return 
	*/
	public final String Dtl_ReloadDdl() throws Exception {
		String Doc = this.GetRequestVal("Doc");
		DataTable dt = DBAccess.RunSQLReturnTable(Doc);
		dt.TableName = "ReloadDdl";
		return Json.ToJson(dt);
	}

		///#endregion dtl.htm 从表.


		///#region dtl.Card
	/** 
	 初始化
	 
	 @return 
	*/
	public final String DtlCard_Init() throws Exception {
		DataSet ds = new DataSet();

		MapDtl md = new MapDtl(this.getEnsName());
		if (this.getFK_Node() != 0 && !md.getFK_MapData().equals("Temp") && this.getEnsName().contains("ND" + this.getFK_Node()) == false && this.getFK_Node() != 999999)
		{
			Node nd = new Node(this.getFK_Node());

			if (nd.getHisFormType() == NodeFormType.SheetTree)
			{
				/*如果
				 * 1,传来节点ID, 不等于0.
				 * 2,不是节点表单.  就要判断是否是独立表单，如果是就要处理权限方案。*/
				FrmNode fn = new FrmNode(nd.getNodeID(), this.getFK_MapData());
				/**自定义权限.
				*/
				if (fn.getFrmSln() == FrmSln.Self)
				{
					md.setNo(this.getEnsName() + "_" + this.getFK_Node());
					if (md.RetrieveFromDBSources() == 0)
					{
						md = new MapDtl(this.getEnsName());
					}
				}
			}
		}

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
		//GEDtls enDtls = new GEDtls(this.EnsName);

			///#region  把从表的数据放入.
		GEDtls enDtls = new GEDtls(md.getNo());
		QueryObject qo = null;
		try
		{
			qo = new QueryObject(enDtls);
			switch (md.getDtlOpenType())
			{
				case ForEmp: // 按人员来控制.
					qo.AddWhere(GEDtlAttr.RefPK, this.getRefPKVal());
					qo.addAnd();
					qo.AddWhere(GEDtlAttr.Rec, WebUser.getNo());
					break;
				case ForWorkID: // 按工作ID来控制
					qo.AddWhere(GEDtlAttr.RefPK, this.getRefPKVal());
					break;
				case ForFID: // 按流程ID来控制.
					if (this.getFID() == 0)
					{
						qo.AddWhere(GEDtlAttr.FID, this.getRefPKVal());
					}
					else
					{
						qo.AddWhere(GEDtlAttr.FID, this.getFID());
					}
					break;
			}
		}
		catch (RuntimeException ex)
		{
			dtls.getGetNewEntity().CheckPhysicsTable();
			throw ex;
		}

		//条件过滤.
		if (DataType.IsNullOrEmpty(md.getFilterSQLExp()) == false)
		{
			String[] strs = md.getFilterSQLExp().split("[=]", -1);
			qo.addAnd();
			qo.AddWhere(strs[0], strs[1]);
		}

		//排序.
		if (DataType.IsNullOrEmpty(md.getOrderBySQLExp()) == false)
		{
			qo.addOrderBy(md.getOrderBySQLExp());
		}
		else
		{
			//增加排序.
			qo.addOrderBy(GEDtlAttr.OID);
		}


		//从表
		DataTable dtDtl = qo.DoQueryToTable();
		dtDtl.TableName = "DTDtls";
		ds.Tables.add(dtDtl);

			///#endregion
		//enDtls.Retrieve(GEDtlAttr.RefPK, this.RefPKVal);
		//ds.Tables.add(enDtls.ToDataTableField("DTDtls"));

		return Json.ToJson(ds);

	}
	/** 
	 获得从表的从表数据
	 
	 @return 
	*/
	public final String DtlCard_Init_Dtl() throws Exception {
		DataSet ds = new DataSet();

		MapDtl md = new MapDtl(this.getEnsName());

		//主表数据.
		DataTable dt = md.ToDataTableField("Main");
		ds.Tables.add(dt);

		//主表字段.
		MapAttrs attrs = md.getMapAttrs();
		ds.Tables.add(attrs.ToDataTableField("MapAttrs"));

		GEDtls enDtls = new GEDtls(this.getEnsName());
		enDtls.Retrieve(GEDtlAttr.RefPK, this.getRefPKVal(), null);
		ds.Tables.add(enDtls.ToDataTableField("DTDtls"));

		return Json.ToJson(ds);
	}

		///#endregion dtl.Card


		///#region 保存手写签名图片
	/** 
	 保存手写签名图片
	 
	 @return 返回保存结果
	*/
	public final String HandWriting_Save() throws Exception {
		try {
			String basePath = SystemConfig.getPathOfDataUser() + "HandWritingImg";
			String ny = DataType.getCurrentDateByFormart("yyyy_MM");
			String tempPath = basePath + "/" + ny + "/" + this.getFK_Node() + "/";
			String tempName = this.getWorkID() + "_" + this.getKeyOfEn() + "_"
					+ DataType.getCurrentDateByFormart("HHmmss") + ".png";
			File file = new File(tempPath);
			if (file.exists() == false)
				file.mkdirs();
			// 删除改目录下WORKID的文件
			String[] files = file.list();
			for (String f : files) {
				if (f.contains(this.getWorkID() + "_" + this.getKeyOfEn()) == true)
					new File(f).delete();
			}

			String pic_Path = tempPath + tempName;

			String imgData = this.GetValFromFrmByKey("imageData");
			Base64.Decoder decoder = Base64.getDecoder();
			byte[] imgByte = decoder.decode(imgData);
			InputStream in = new ByteArrayInputStream(imgByte);
			byte[] b = new byte[1024];
			int nRead = 0;

			OutputStream o = new FileOutputStream(pic_Path);

			while ((nRead = in.read(b)) != -1) {
				o.write(b, 0, nRead);
			}

			o.flush();
			o.close();

			in.close();

			return "info@" + pic_Path.replace("\\", "/");
		} catch (Exception e) {
			return "err@" + e.getMessage();
		}
	}
	/** 
	 图片转二进制流
	 
	 @return 
	*/
	public final String ImageDatabytes() throws Exception {
		String FilePath = SystemConfig.getPathOfDataUser() + GetRequestVal("src");
		File file = new File(FilePath);
		if (file.exists()==false)
			return "";
		BufferedImage bi;
		Base64.Encoder encoder = Base64.getEncoder();
		//BASE64Encoder encoder = new sun.misc.BASE64Encoder();
		try {
			bi = ImageIO.read(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bi, "jpg", baos);  //经测试转换的图片是格式这里就什么格式，否则会失真
			byte[] bytes = baos.toByteArray();

			return encoder.encodeToString(bytes).trim();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


		///#endregion



	/** 
	 处理SQL的表达式.
	 
	 param exp 表达式
	 @return 从from里面替换的表达式.
	*/
	public final String DealExpByFromVals(String exp)
	{
		for (String strKey : CommonUtils.getRequest().getParameterMap().keySet())
		{
			if (exp.contains("@") == false)
			{
				return exp;
			}
			String str = strKey.replace("TB_", "").replace("CB_", "").replace("DDL_", "").replace("RB_", "");

			exp = exp.replace("@" + str, this.GetRequestVal(strKey));
		}
		return exp;
	}
	/** 
	 初始化树的接口
	 
	 param context
	 @return 
	*/
	public final String PopVal_InitTree() throws Exception {
		String mypk = this.GetRequestVal("FK_MapExt");

		MapExt me = new MapExt();
		me.setMyPK(mypk);
		me.Retrieve();

		// 获得配置信息.
		Hashtable ht = me.PopValToHashtable();
		DataTable dtcfg = PubClass.HashtableToDataTable(ht);

		String parentNo = this.GetRequestVal("ParentNo");
		if (parentNo == null) {
			parentNo = me.getPopValTreeParentNo();
		}

		DataSet resultDs = new DataSet();
		String sqlObjs = me.getPopValTreeSQL();
		sqlObjs = sqlObjs.replace("@WebUser.No", WebUser.getNo());
		sqlObjs = sqlObjs.replace("@WebUser.Name", WebUser.getName());
		sqlObjs = sqlObjs.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
		sqlObjs = sqlObjs.replace("@ParentNo", parentNo);
		sqlObjs = this.DealExpByFromVals(sqlObjs);

		DataTable dt = DBAccess.RunSQLReturnTable(sqlObjs);
		dt.TableName = "DTObjs";

		// 判断是否是oracle.
		if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.UpperCase) {
			dt.Columns.get("NO").setColumnName("No");
			dt.Columns.get("NAME").setColumnName("Name");
			dt.Columns.get("PARENTNO").setColumnName("ParentNo");
		}
		if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.Lowercase) {
			dt.Columns.get("no").setColumnName("No");
			dt.Columns.get("name").setColumnName("Name");
			dt.Columns.get("parentno").setColumnName("ParentNo");
		}
		resultDs.Tables.add(dt);

		// doubleTree
		if (me.getPopValWorkModel() == PopValWorkModel.TreeDouble && !parentNo.equals(me.getPopValTreeParentNo())) {
			sqlObjs = me.getPopValDoubleTreeEntitySQL();
			sqlObjs = sqlObjs.replace("@WebUser.No", WebUser.getNo());
			sqlObjs = sqlObjs.replace("@WebUser.Name", WebUser.getName());
			sqlObjs = sqlObjs.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
			sqlObjs = sqlObjs.replace("@ParentNo", parentNo);
			sqlObjs = this.DealExpByFromVals(sqlObjs);

			DataTable entityDt = DBAccess.RunSQLReturnTable(sqlObjs);
			entityDt.TableName = "DTEntitys";
			resultDs.Tables.add(entityDt);

			// 判断是否是oracle.
			if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.UpperCase) {
				entityDt.Columns.get("NO").setColumnName("No");
				entityDt.Columns.get("NAME").setColumnName("Name");

			}
			if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.Lowercase) {
				entityDt.Columns.get("no").setColumnName("No");
				entityDt.Columns.get("name").setColumnName("Name");

			}
		}

		return bp.tools.Json.ToJson(resultDs);
	}

	/** 
	 处理DataTable中的列名，将不规范的No,Name,ParentNo列纠正
	 
	 param dt
	*/
	public final void DoCheckTableColumnNameCase(DataTable dt)
	{
		for (DataColumn col : dt.Columns)
		{
			switch (col.ColumnName.toLowerCase())
			{
				case "no":
					col.ColumnName = "No";
					break;
				case "name":
					col.ColumnName = "Name";
					break;
				case "parentno":
					col.ColumnName = "ParentNo";
					break;
				default:
					break;
			}
		}
	}

	/** 
	 初始化PopVal的值   除了分页表格模式之外的其他数据值
	 
	 @return 
	*/
	public final String PopVal_Init() throws Exception {
		MapExt me = new MapExt();
		me.setMyPK(this.getFK_MapExt());
		me.Retrieve();

		//数据对象，将要返回的.
		DataSet ds = new DataSet();

		//获得配置信息.
		Hashtable ht = me.PopValToHashtable();
		DataTable dtcfg = bp.pub.PubClass.HashtableToDataTable(ht);

		//增加到数据源.
		ds.Tables.add(dtcfg);

		if (me.getPopValWorkModel() == PopValWorkModel.SelfUrl)
		{
			return "@SelfUrl" + me.getPopValUrl();
		}

		if (me.getPopValWorkModel() == PopValWorkModel.TableOnly)
		{
			String sqlObjs = me.getPopValEntitySQL();
			sqlObjs = sqlObjs.replace("@WebUser.No", WebUser.getNo());
			sqlObjs = sqlObjs.replace("@WebUser.Name", WebUser.getName());
			sqlObjs = sqlObjs.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());

			sqlObjs = this.DealExpByFromVals(sqlObjs);


			DataTable dt = DBAccess.RunSQLReturnTable(sqlObjs);
			dt.TableName = "DTObjs";
			DoCheckTableColumnNameCase(dt);
			ds.Tables.add(dt);
			return Json.ToJson(ds);
		}

		if (me.getPopValWorkModel() == PopValWorkModel.Group)
		{
			/*
			 *  分组的.
			 */

			String sqlObjs = me.getPopValGroupSQL();
			if (sqlObjs.length() > 10)
			{
				sqlObjs = sqlObjs.replace("@WebUser.No", WebUser.getNo());
				sqlObjs = sqlObjs.replace("@WebUser.Name", WebUser.getName());
				sqlObjs = sqlObjs.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
				sqlObjs = this.DealExpByFromVals(sqlObjs);


				DataTable dt = DBAccess.RunSQLReturnTable(sqlObjs);
				dt.TableName = "DTGroup";
				DoCheckTableColumnNameCase(dt);
				ds.Tables.add(dt);
			}

			sqlObjs = me.getPopValEntitySQL();
			if (sqlObjs.length() > 10)
			{
				sqlObjs = sqlObjs.replace("@WebUser.No", WebUser.getNo());
				sqlObjs = sqlObjs.replace("@WebUser.Name", WebUser.getName());
				sqlObjs = sqlObjs.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
				sqlObjs = this.DealExpByFromVals(sqlObjs);


				DataTable dt = DBAccess.RunSQLReturnTable(sqlObjs);
				dt.TableName = "DTEntity";
				DoCheckTableColumnNameCase(dt);
				ds.Tables.add(dt);
			}
			return Json.ToJson(ds);

		}

		if (me.getPopValWorkModel() == PopValWorkModel.TablePage)
		{
			/* 分页的 */
			//key
			String key = this.GetRequestVal("Key");
			if (DataType.IsNullOrEmpty(key) == true)
			{
				key = "";
			}

			//取出来查询条件.
			String[] conds = me.getPopValSearchCond().split("[$]", -1);

			//pageSize
			String pageSize = this.GetRequestVal("pageSize");
			if (DataType.IsNullOrEmpty(pageSize))
			{
				pageSize = "10";
			}

			//pageIndex
			String pageIndex = this.GetRequestVal("pageIndex");
			if (DataType.IsNullOrEmpty(pageIndex))
			{
				pageIndex = "1";
			}

			String sqlObjs = me.getPopValTablePageSQL();
			sqlObjs = sqlObjs.replace("@WebUser.No", WebUser.getNo());
			sqlObjs = sqlObjs.replace("@WebUser.Name", WebUser.getName());
			sqlObjs = sqlObjs.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
			sqlObjs = sqlObjs.replace("@Key", key);

			//三个固定参数.
			sqlObjs = sqlObjs.replace("@PageCount",
					(String.valueOf((Integer.parseInt(pageIndex) - 1) * Integer.parseInt(pageSize))));
			sqlObjs = sqlObjs.replace("@PageSize", pageSize);
			sqlObjs = sqlObjs.replace("@PageIndex", pageIndex);
			sqlObjs = this.DealExpByFromVals(sqlObjs);

			//替换其他参数.
			for (String cond : conds)
			{
				if (cond == null || cond.equals(""))
				{
					continue;
				}

				//参数.
				String para = cond.substring(5, cond.indexOf("#"));
				String val = this.GetRequestVal(para);
				if (DataType.IsNullOrEmpty(val))
				{
					if (cond.contains("ListSQL") == true || cond.contains("EnumKey") == true)
					{
						val = "all";
					}
					else
					{
						val = "";
					}
				}
				if (val.equals("all"))
				{
					sqlObjs = sqlObjs.replace(para + "=@" + para, "1=1");
					sqlObjs = sqlObjs.replace(para + "='@" + para + "'", "1=1");

					int startIndex = 0;
					while (startIndex != -1 && startIndex < sqlObjs.length())
					{
						int index = sqlObjs.indexOf("1=1", startIndex + 1);
						if (index > 0 && sqlObjs.substring(startIndex, index).trim().endsWith("."))
						{
							int lastBlankIndex = sqlObjs.substring(startIndex, index).lastIndexOf(" ");


							sqlObjs = StringHelper.remove(sqlObjs, lastBlankIndex + startIndex + 1, index - lastBlankIndex - 1);

							startIndex = (startIndex + lastBlankIndex) + 3;
						}
						else
						{
							startIndex = index;
						}
					}
				}
				else
				{
					//要执行两次替换有可能是，有引号.
					sqlObjs = sqlObjs.replace("@" + para, val);
				}
			}


			DataTable dt = DBAccess.RunSQLReturnTable(sqlObjs);
			dt.TableName = "DTObjs";
			DoCheckTableColumnNameCase(dt);
			ds.Tables.add(dt);

			//处理查询条件.
			//$Para=Dept#Label=所在班级#ListSQL=Select No,Name FROM Port_Dept WHERE No='@WebUser.getNo()'
			//$Para=XB#Label=性别#EnumKey=XB
			//$Para=DTFrom#Label=注册日期从#DefVal=@Now-30
			//$Para=DTTo#Label=到#DefVal=@Now


			for (String cond : conds)
			{
				if (DataType.IsNullOrEmpty(cond) == true)
				{
					continue;
				}

				String sql = null;
				if (cond.contains("#ListSQL=") == true)
				{
					sql = cond.substring(cond.indexOf("ListSQL") + 8);
					sql = sql.replace("@WebUser.No", WebUser.getNo());
					sql = sql.replace("@WebUser.Name", WebUser.getName());
					sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
					sql = this.DealExpByFromVals(sql);
				}

				if (cond.contains("#EnumKey=") == true)
				{
					String enumKey = cond.substring(cond.indexOf("EnumKey") + 8);
					sql = "SELECT IntKey AS No, Lab as Name FROM "+bp.sys.base.Glo.SysEnum()+" WHERE EnumKey='" + enumKey + "'";
				}

				//处理日期的默认值
				//DefVal=@Now-30
				//if (cond.contains("@Now"))
				//{
				//    int nowIndex = cond.IndexOf(cond);
				//    if (cond.Trim().Length - nowIndex > 5)
				//    {
				//        char optStr = cond.Trim()[nowIndex + 5];
				//        int day = 0;
				//        if (int.TryParse(cond.Trim().Substring(nowIndex + 6), out day)) {
				//            cond = cond.Substring(0, nowIndex) + DateTime.Now.AddDays(-1 * day).ToString("yyyy-MM-dd HH:mm");
				//        }
				//    }
				//}

				if (sql == null)
				{
					continue;
				}

				//参数.
				String para = cond.substring(5, cond.indexOf("#"));
				if (ds.contains(para) == true)
				{
					throw new RuntimeException("@配置的查询,参数名有冲突不能命名为:" + para);
				}

				//查询出来数据，就把他放入到dataset里面.
				DataTable dtPara = DBAccess.RunSQLReturnTable(sql);
				dtPara.TableName = para;
				DoCheckTableColumnNameCase(dt);
				ds.Tables.add(dtPara); //加入到参数集合.
			}


			return Json.ToJson(ds);
		}

		//返回数据.
		return Json.ToJson(ds);
	}

	/** 
	 初始化PopVal 分页表格模式的Count  杨玉慧
	 
	 @return 
	*/
	public final String PopVal_InitTablePageCount() throws Exception {
		MapExt me = new MapExt();
		me.setMyPK(this.getFK_MapExt());
		me.Retrieve();

		// 数据对象，将要返回的.
		DataSet ds = new DataSet();

		// 获得配置信息.
		Hashtable ht = me.PopValToHashtable();
		DataTable dtcfg = PubClass.HashtableToDataTable(ht);

		// 增加到数据源.
		ds.Tables.add(dtcfg);

		if (me.getPopValWorkModel() == PopValWorkModel.SelfUrl) {
			return "@SelfUrl" + me.getPopValUrl();
		}
		if (me.getPopValWorkModel() == PopValWorkModel.TablePage) {
			/* 分页的 */
			// key
			String key = this.GetRequestVal("Key");
			if (DataType.IsNullOrEmpty(key) == true) {
				key = "";
			}

			// 取出来查询条件.
			String[] conds = me.getPopValSearchCond().split("[$]", -1);

			String countSQL = me.getPopValTablePageSQLCount();

			// 固定参数.
			countSQL = countSQL.replace("@WebUser.No", WebUser.getNo());
			countSQL = countSQL.replace("@WebUser.Name", WebUser.getName());
			countSQL = countSQL.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
			countSQL = countSQL.replace("@Key", key);
			countSQL = this.DealExpByFromVals(countSQL);

			// 替换其他参数.
			for (String cond : conds) {
				if (cond == null || cond.equals("")) {
					continue;
				}

				// 参数.
				String para = cond.substring(5, cond.indexOf("#"));
				String val = this.GetRequestVal(para);
				if (DataType.IsNullOrEmpty(val)) {
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

							countSQL = StringHelper.remove(countSQL, lastBlankIndex + startIndex + 1,
									index - lastBlankIndex - 1);

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

			String count = String.valueOf(DBAccess.RunSQLReturnValInt(countSQL, 0));
			DataTable dtCount = new DataTable("DTCout");
			dtCount.TableName = "DTCout";
			dtCount.Columns.Add("Count", Integer.class);
			dtCount.Rows.AddDatas(new String[] { count });
			ds.Tables.add(dtCount);

			// 处理查询条件.
			// $Para=Dept#Label=所在班级#ListSQL=Select No,Name FROM Port_Dept WHERE
			// No='@WebUser.No'
			// $Para=XB#Label=性别#EnumKey=XB
			// $Para=DTFrom#Label=注册日期从#DefVal=@Now-30
			// $Para=DTTo#Label=到#DefVal=@Now

			for (String cond : conds) {
				if (DataType.IsNullOrEmpty(cond) == true) {
					continue;
				}

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
					sql = "SELECT IntKey AS No, Lab as Name FROM "+bp.sys.base.Glo.SysEnum()+" WHERE EnumKey='" + enumKey + "'";
				}

				// 处理日期的默认值
				// DefVal=@Now-30
				// if (cond.contains("@Now"))
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
				if (ds.GetTableByName(para)!=null) {
					throw new RuntimeException("@配置的查询,参数名有冲突不能命名为:" + para);
				}

				// 查询出来数据，就把他放入到dataset里面.
				DataTable dtPara = DBAccess.RunSQLReturnTable(sql);
				dtPara.TableName = para;
				ds.Tables.add(dtPara); // 加入到参数集合.
			}

			return bp.tools.Json.ToJson(ds);
		}
		// 返回数据.
		return bp.tools.Json.ToJson(ds);
	}

	/** 
	 /// 
	 初始化PopVal分页表格的List  杨玉慧
	 
	 @return 
	*/
	public final String PopVal_InitTablePageList() throws Exception {
		MapExt me = new MapExt();
		me.setMyPK(this.getFK_MapExt());
		me.Retrieve();

		//数据对象，将要返回的.
		DataSet ds = new DataSet();

		//获得配置信息.
		Hashtable ht = me.PopValToHashtable();
		DataTable dtcfg = bp.pub.PubClass.HashtableToDataTable(ht);

		//增加到数据源.
		ds.Tables.add(dtcfg);

		if (me.getPopValWorkModel() == PopValWorkModel.SelfUrl)
		{
			return "@SelfUrl" + me.getPopValUrl();
		}
		if (me.getPopValWorkModel() == PopValWorkModel.TablePage)
		{
			/* 分页的 */
			//key
			String key = this.GetRequestVal("Key");
			if (DataType.IsNullOrEmpty(key) == true)
			{
				key = "";
			}

			//取出来查询条件.
			String[] conds = me.getPopValSearchCond().split("[$]", -1);

			//pageSize
			String pageSize = this.GetRequestVal("pageSize");
			if (DataType.IsNullOrEmpty(pageSize))
			{
				pageSize = "10";
			}

			//pageIndex
			String pageIndex = this.GetRequestVal("pageIndex");
			if (DataType.IsNullOrEmpty(pageIndex))
			{
				pageIndex = "1";
			}

			String sqlObjs = me.getPopValTablePageSQL();
			sqlObjs = sqlObjs.replace("@WebUser.No", WebUser.getNo());
			sqlObjs = sqlObjs.replace("@WebUser.Name", WebUser.getName());
			sqlObjs = sqlObjs.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
			sqlObjs = sqlObjs.replace("@Key", key);

			//三个固定参数.
			sqlObjs = sqlObjs.replace("@PageCount",
					(String.valueOf((Integer.parseInt(pageIndex) - 1) * Integer.parseInt(pageSize))));
			sqlObjs = sqlObjs.replace("@PageSize", pageSize);
			sqlObjs = sqlObjs.replace("@PageIndex", pageIndex);
			sqlObjs = this.DealExpByFromVals(sqlObjs);

			//替换其他参数.
			for (String cond : conds)
			{
				if (cond == null || cond.equals(""))
				{
					continue;
				}

				//参数.
				String para = cond.substring(5, cond.indexOf("#"));
				String val = this.GetRequestVal(para);
				if (DataType.IsNullOrEmpty(val))
				{
					if (cond.contains("ListSQL") == true || cond.contains("EnumKey") == true)
					{
						val = "all";
					}
					else
					{
						val = "";
					}
				}
				if (val.equals("all"))
				{
					sqlObjs = sqlObjs.replace(para + "=@" + para, "1=1");
					sqlObjs = sqlObjs.replace(para + "='@" + para + "'", "1=1");

					int startIndex = 0;
					while (startIndex != -1 && startIndex < sqlObjs.length())
					{
						int index = sqlObjs.indexOf("1=1", startIndex + 1);
						if (index > 0 && sqlObjs.substring(startIndex, index).trim().endsWith("."))
						{
							int lastBlankIndex = sqlObjs.substring(startIndex, index).lastIndexOf(" ");


							sqlObjs = StringHelper.remove(sqlObjs, lastBlankIndex + startIndex + 1, index - lastBlankIndex - 1);

							startIndex = (startIndex + lastBlankIndex) + 3;
						}
						else
						{
							startIndex = index;
						}
					}
				}
				else
				{
					//要执行两次替换有可能是，有引号.
					sqlObjs = sqlObjs.replace("@" + para, val);
				}
			}


			DataTable dt = DBAccess.RunSQLReturnTable(sqlObjs);
			dt.TableName = "DTObjs";
			ds.Tables.add(dt);

			//处理查询条件.
			//$Para=Dept#Label=所在班级#ListSQL=Select No,Name FROM Port_Dept WHERE No='@WebUser.getNo()'
			//$Para=XB#Label=性别#EnumKey=XB
			//$Para=DTFrom#Label=注册日期从#DefVal=@Now-30
			//$Para=DTTo#Label=到#DefVal=@Now


			for (String cond : conds)
			{
				if (DataType.IsNullOrEmpty(cond) == true)
				{
					continue;
				}

				String sql = null;
				if (cond.contains("#ListSQL=") == true)
				{
					sql = cond.substring(cond.indexOf("ListSQL") + 8);
					sql = sql.replace("@WebUser.No", WebUser.getNo());
					sql = sql.replace("@WebUser.Name", WebUser.getName());
					sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
					sql = this.DealExpByFromVals(sql);
				}

				if (cond.contains("#EnumKey=") == true)
				{
					String enumKey = cond.substring(cond.indexOf("EnumKey") + 8);
					sql = "SELECT IntKey AS No, Lab as Name FROM "+bp.sys.base.Glo.SysEnum()+" WHERE EnumKey='" + enumKey + "'";
				}

				//处理日期的默认值
				//DefVal=@Now-30
				//if (cond.contains("@Now"))
				//{
				//    int nowIndex = cond.IndexOf(cond);
				//    if (cond.Trim().Length - nowIndex > 5)
				//    {
				//        char optStr = cond.Trim()[nowIndex + 5];
				//        int day = 0;
				//        if (int.TryParse(cond.Trim().Substring(nowIndex + 6), out day)) {
				//            cond = cond.Substring(0, nowIndex) + DateTime.Now.AddDays(-1 * day).ToString("yyyy-MM-dd HH:mm");
				//        }
				//    }
				//}

				if (sql == null)
				{
					continue;
				}

				//参数.
				String para = cond.substring(5, cond.indexOf("#"));
				if (ds.contains(para) == true)
				{
					throw new RuntimeException("@配置的查询,参数名有冲突不能命名为:" + para);
				}

				//查询出来数据，就把他放入到dataset里面.
				DataTable dtPara = DBAccess.RunSQLReturnTable(sql);
				dtPara.TableName = para;
				ds.Tables.add(dtPara); //加入到参数集合.
			}


			return Json.ToJson(ds);
		}
		//返回数据.
		return Json.ToJson(ds);
	}

	//单附件上传方法
//	private void SingleAttach(String attachPk, long workid, long fid, int fk_node, String ensName) throws Exception {
//		FrmAttachment frmAth = new FrmAttachment();
//		frmAth.setMyPK(attachPk);
//		frmAth.RetrieveFromDBSources();
//
//		String athDBPK = attachPk + "_" + workid;
//
//		Node currND = new Node(fk_node);
//		Work currWK = currND.getHisWork();
//		currWK.setOID(workid);
//		currWK.Retrieve();
//		//处理保存路径.
//		String saveTo = frmAth.getSaveTo();
//
//		if (saveTo.contains("*") || saveTo.contains("@"))
//		{
//			/*如果路径里有变量.*/
//			saveTo = saveTo.replace("*", "@");
//			saveTo = Glo.DealExp(saveTo, currWK, null);
//		}
//
//		try
//		{
//			saveTo = SystemConfig.getPathOfWebApp() + saveTo; //context.Server.MapPath("~/" + saveTo);
//		}
//		catch (java.lang.Exception e)
//		{
//			//saveTo = saveTo;
//		}
//
//		if ((new File(saveTo)).isDirectory() == false)
//		{
//			(new File(saveTo)).mkdirs();
//		}
//
//
//		saveTo = saveTo + "/" + athDBPK + "." + HttpContextHelper.RequestFiles(0).FileName.substring(HttpContextHelper.RequestFiles(0).FileName.lastIndexOf('.') + 1);
//		//context.Request.Files[0].SaveAs(saveTo);
//		HttpContextHelper.UploadFile(HttpContextHelper.RequestFiles(0), saveTo);
//
//		File info = new File(saveTo);
//
//		FrmAttachmentDB dbUpload = new FrmAttachmentDB();
//		dbUpload.setMyPK(athDBPK);
//		dbUpload.setFK_MapData(frmAth.getFK_MapData());
//		dbUpload.FK_FrmAttachment = attachPk;
//		dbUpload.RefPKVal = String.valueOf(this.getWorkID());
//		dbUpload.FID = fid;
//		dbUpload.setFK_MapData(ensName);
//
//		dbUpload.FileExts = info.Extension;
//
//
//			///#region 处理文件路径，如果是保存到数据库，就存储pk.
//		if (frmAth.AthSaveWay == AthSaveWay.IISServer)
//		{
//			//文件方式保存
//			dbUpload.FileFullName = saveTo;
//		}
//
//		if (frmAth.AthSaveWay == AthSaveWay.DB)
//		{
//			//保存到数据库
//			dbUpload.FileFullName = dbUpload.MyPK;
//		}
//
//			///#endregion 处理文件路径，如果是保存到数据库，就存储pk.
//
//
//		dbUpload.FileName = HttpContextHelper.RequestFiles(0).FileName;
//		dbUpload.FileSize = (float)info.length();
//		dbUpload.Rec = WebUser.getNo();
//		dbUpload.RecName = WebUser.getName();
//		dbUpload.FK_Dept = WebUser.getFK_Dept();
//		dbUpload.FK_DeptName = WebUser.getFK_DeptName();
//		dbUpload.RDT = DataType.getCurrentDateTime();
//
//		dbUpload.NodeID = fk_node;
//		dbUpload.Save();
//
//		if (frmAth.AthSaveWay == AthSaveWay.DB)
//		{
//			//执行文件保存.
//			DBAccess.SaveFileToDB(saveTo, dbUpload.getEnMap().getPhysicsTable(), "MyPK", dbUpload.MyPK, "FDB");
//		}
//	}

//	//多附件上传方法
//	public final String MoreAttach() throws Exception {
//		String empNo = this.GetRequestVal("UserNo");
//		if (DataType.IsNullOrEmpty(empNo) == false)
//		{
//			Dev2Interface.Port_Login(empNo);
//		}
//
//		String uploadFileM = ""; //上传附件数据的MyPK,用逗号分开
//		String pkVal = this.GetRequestVal("PKVal");
//		String attachPk = this.GetRequestVal("AttachPK");
//		String paras = this.GetRequestVal("parasData");
//		String sort = this.GetRequestVal("Sort");
//
//		//获取sort
//		if (DataType.IsNullOrEmpty(sort))
//		{
//			if (paras != null && paras.length() > 0)
//			{
//				for (String para : paras.split("[@]", -1))
//				{
//					if (para.indexOf("Sort") != -1)
//					{
//						sort = para.split("[=]", -1)[1];
//					}
//				}
//			}
//		}
//		// 多附件描述.
//		FrmAttachment athDesc = new FrmAttachment(attachPk);
//		MapData mapData = new MapData(athDesc.getFK_MapData());
//		String msg = "";
//		//求出来实体记录，方便执行事件.
//		GEEntity en = new GEEntity(athDesc.getFK_MapData());
//		en.PKVal = pkVal;
//		if (en.RetrieveFromDBSources() == 0)
//		{
//			en.PKVal = this.getFID();
//			if (en.RetrieveFromDBSources() == 0)
//			{
//				en.PKVal = this.getPWorkID();
//				en.RetrieveFromDBSources();
//			}
//		}
//
//		//求主键. 如果该表单挂接到流程上.
//		if (this.getFK_Node() != 0 && athDesc.NoOfObj.contains("AthMDtl") == false)
//		{
//			//判断表单方案。
//			FrmNode fn = new FrmNode(this.getFK_Node(), this.getFK_MapData());
//			if (fn.getFrmSln() == FrmSln.Self)
//			{
//				FrmAttachment myathDesc = new FrmAttachment();
//				myathDesc.setMyPK(attachPk + "_" + this.getFK_Node());
//				if (myathDesc.RetrieveFromDBSources() != 0)
//				{
//					athDesc.HisCtrlWay = myathDesc.HisCtrlWay;
//				}
//			}
//			pkVal = Dev2Interface.GetAthRefPKVal(this.getWorkID(), this.getPWorkID(), this.getFID(), this.getFK_Node(), this.getFK_MapData(), athDesc);
//		}
//
//
//
//
//		//获取上传文件是否需要加密
//		boolean fileEncrypt = SystemConfig.IsEnableAthEncrypt;
//
//		for (int i = 0; i < HttpContextHelper.RequestFilesCount; i++)
//		{
//			//HttpPostedFile file = context.Request.Files[i];
//			var file = HttpContextHelper.RequestFiles(i);
//
//			String fileName = (new File(file.FileName)).getName();
//
//
//				///#region 文件上传的iis服务器上 or db数据库里.
//			if (athDesc.getAthSaveWay() == AthSaveWay.IISServer)
//			{
//				String savePath = athDesc.SaveTo;
//				if (savePath.contains("@") == true || savePath.contains("*") == true)
//				{
//					/*如果有变量*/
//					savePath = savePath.replace("*", "@");
//
//					if (savePath.contains("@") && this.getFK_Node() != 0)
//					{
//						/*如果包含 @ */
//						Flow flow = new Flow(this.getFK_Flow());
//						GERpt myen = flow.getHisGERpt();
//						myen.setOID(this.getWorkID());
//						myen.RetrieveFromDBSources();
//						savePath = Glo.DealExp(savePath, myen, null);
//					}
//					if (savePath.contains("@") == true)
//					{
//						throw new RuntimeException("@路径配置错误,变量没有被正确的替换下来." + savePath);
//					}
//				}
//				else
//				{
//					savePath = athDesc.SaveTo + "/" + pkVal;
//				}
//
//				//替换关键的字串.
//				savePath = savePath.replace("\\\\", "/");
//				try
//				{
//					if (savePath.contains(SystemConfig.getPathOfWebApp()) == false)
//					{
//						savePath = SystemConfig.getPathOfWebApp() + savePath;
//					}
//				}
//				catch (RuntimeException ex)
//				{
//					savePath = SystemConfig.getPathOfDataUser() + "UploadFile/" + mapData.getNo() + "/";
//					//return "err@获取路径错误" + ex.Message + ",配置的路径是:" + savePath + ",您需要在附件属性上修改该附件的存储路径.";
//				}
//
//				try
//				{
//					if ((new File(savePath)).isDirectory() == false)
//					{
//						(new File(savePath)).mkdirs();
//					}
//				}
//				catch (RuntimeException ex)
//				{
//					throw new RuntimeException("err@创建路径出现错误，可能是没有权限或者路径配置有问题:" + savePath + "@异常信息:" + ex.getMessage());
//				}
//
//				String exts = System.IO.Path.GetExtension(file.FileName).toLowerCase().replace(".", "");
//				if (DataType.IsNullOrEmpty(exts))
//				{
//					return "err@上传的文件" + file.FileName + "没有扩展名";
//				}
//
//				String guid = DBAccess.GenerGUID(0, null, null);
//
//
//
//				String realSaveTo = savePath + "/" + guid + "." + fileName;
//
//				realSaveTo = realSaveTo.replace("~", "-");
//				realSaveTo = realSaveTo.replace("'", "-");
//				realSaveTo = realSaveTo.replace("*", "-");
//
//				if (fileEncrypt == true)
//				{
//
//					String strtmp = realSaveTo + ".tmp";
//					//file.SaveAs(strtmp);//先明文保存到本地(加个后缀名.tmp)
//					HttpContextHelper.UploadFile(file, strtmp);
//					AesEncodeUtil.encryptFile(strtmp, strtmp.replace(".tmp", "")); //加密
//					(new File(strtmp)).delete(); //删除临时文件
//				}
//				else
//				{
//					//文件保存的路径
//					//file.SaveAs(realSaveTo);
//
//
//					//if (athDesc.FileType == 1 || (exts.ToUpper().Equals("JPG") || exts.ToUpper().Equals("PNG")
//					//    || exts.ToUpper().Equals("JPEG") || exts.ToUpper().Equals("GIF")))
//					//{
//					//    string orgPath = realSaveTo.Replace("." + exts, "") + "Org." + exts;
//					//    HttpContextHelper.UploadFile(file, orgPath);
//					//    new Luban(orgPath).Compress(realSaveTo);
//					//}
//					//else
//					//{
//						HttpContextHelper.UploadFile(file, realSaveTo);
//					//}
//
//				}
//
//				//执行附件上传前事件，added by liuxc,2017-7-15
//				msg = ExecEvent.DoFrm(mapData, EventListFrm.AthUploadeBefore, en, "@FK_FrmAttachment=" + athDesc.MyPK + "@FileFullName=" + realSaveTo);
//				if (!DataType.IsNullOrEmpty(msg))
//				{
//					bp.sys.base.Glo.WriteLineError("@AthUploadeBefore事件返回信息，文件：" + file.FileName + "，" + msg);
//
//					try
//					{
//						(new File(realSaveTo)).delete();
//					}
//					catch (java.lang.Exception e)
//					{
//					}
//				}
//
//				File info = new File(realSaveTo);
//				FrmAttachmentDB dbUpload = new FrmAttachmentDB();
//				dbUpload.setMyPK(guid); // athDesc.getFK_MapData() + oid.ToString();
//				dbUpload.NodeID = this.getFK_Node();
//				dbUpload.Sort = sort;
//				dbUpload.setFK_MapData(athDesc.getFK_MapData());
//				dbUpload.FK_FrmAttachment = attachPk;
//				dbUpload.FileExts = info.Extension;
//				dbUpload.FID = this.getFID();
//				if (fileEncrypt == true)
//				{
//					dbUpload.SetPara("IsEncrypt", 1);
//				}
//
//				if (athDesc.IsExpCol == true)
//				{
//					if (paras != null && paras.length() > 0)
//					{
//						for (String para : paras.split("[@]", -1))
//						{
//							dbUpload.SetPara(para.split("[=]", -1)[0], para.split("[=]", -1)[1]);
//						}
//					}
//				}
//
//
//					///#region 处理文件路径，如果是保存到数据库，就存储pk.
//				if (athDesc.getAthSaveWay() == AthSaveWay.IISServer)
//				{
//					//文件方式保存
//					dbUpload.FileFullName = realSaveTo;
//				}
//
//				if (athDesc.getAthSaveWay() == AthSaveWay.FTPServer)
//				{
//					//保存到数据库
//					dbUpload.FileFullName = dbUpload.MyPK;
//				}
//
//					///#endregion 处理文件路径，如果是保存到数据库，就存储pk.
//
//				dbUpload.FileName = fileName;
//				dbUpload.FileSize = (float)info.length();
//				dbUpload.RDT = DataType.getCurrentDateTimess();
//				dbUpload.Rec = WebUser.getNo();
//				dbUpload.RecName = WebUser.getName();
//				dbUpload.FK_Dept = WebUser.getFK_Dept();
//				dbUpload.FK_DeptName = WebUser.getFK_DeptName();
//				dbUpload.RefPKVal = pkVal;
//				dbUpload.FID = this.getFID();
//
//				dbUpload.UploadGUID = guid;
//				dbUpload.Insert();
//				uploadFileM += dbUpload.MyPK + ",";
//
//
//				if (athDesc.getAthSaveWay() == AthSaveWay.DB)
//				{
//					//执行文件保存.
//					DBAccess.SaveFileToDB(realSaveTo, dbUpload.getEnMap().getPhysicsTable(), "MyPK", dbUpload.MyPK, "FDB");
//				}
//
//				//执行附件上传后事件，added by liuxc,2017-7-15
//				msg = ExecEvent.DoFrm(mapData, EventListFrm.AthUploadeAfter, en, "@FK_FrmAttachment=" + dbUpload.FK_FrmAttachment + "@FK_FrmAttachmentDB=" + dbUpload.MyPK + "@FileFullName=" + dbUpload.FileFullName);
//				if (!DataType.IsNullOrEmpty(msg))
//				{
//					bp.sys.base.Glo.WriteLineError("@AthUploadeAfter事件返回信息，文件：" + dbUpload.FileName + "，" + msg);
//				}
//			}
//
//				///#endregion 文件上传的iis服务器上 or db数据库里.
//
//
//				///#region 保存到数据库 / FTP服务器上.
//			if (athDesc.getAthSaveWay() == AthSaveWay.DB || athDesc.getAthSaveWay() == AthSaveWay.FTPServer)
//			{
//				String guid = DBAccess.GenerGUID(0, null, null);
//
//				//把文件临时保存到一个位置.
//				String temp = SystemConfig.getPathOfTemp() + "" + guid + ".tmp";
//
//				if (fileEncrypt == true)
//				{
//
//					String strtmp = SystemConfig.getPathOfTemp() + "" + guid + "_Desc" + ".tmp";
//					HttpContextHelper.UploadFile(file, strtmp);
//					AesEncodeUtil.encryptFile(strtmp, temp); //加密
//					(new File(strtmp)).delete(); //删除临时文件
//				}
//				else
//				{
//					//文件保存的路径
//					HttpContextHelper.UploadFile(file, temp);
//				}
//
//				//执行附件上传前事件，added by liuxc,2017-7-15
//				msg = ExecEvent.DoFrm(mapData, EventListFrm.AthUploadeBefore, en, "@FK_FrmAttachment=" + athDesc.MyPK + "@FileFullName=" + temp);
//				if (DataType.IsNullOrEmpty(msg) == false)
//				{
//					bp.sys.base.Glo.WriteLineError("@AthUploadeBefore事件返回信息，文件：" + file.FileName + "，" + msg);
//
//					try
//					{
//						(new File(temp)).delete();
//					}
//					catch (java.lang.Exception e2)
//					{
//					}
//
//					throw new RuntimeException("err@上传附件错误：" + msg);
//				}
//
//				File info = new File(temp);
//				FrmAttachmentDB dbUpload = new FrmAttachmentDB();
//				dbUpload.setMyPK(DBAccess.GenerGUID(0, null, null));
//				dbUpload.Sort = sort;
//				dbUpload.NodeID = getFK_Node();
//				dbUpload.setFK_MapData(athDesc.getFK_MapData());
//				dbUpload.FK_FrmAttachment = athDesc.MyPK;
//				dbUpload.FID = this.getFID(); //流程id.
//				if (fileEncrypt == true)
//				{
//					dbUpload.SetPara("IsEncrypt", 1);
//				}
//
//				dbUpload.RefPKVal = pkVal.toString();
//				dbUpload.setFK_MapData(athDesc.getFK_MapData());
//				dbUpload.FK_FrmAttachment = athDesc.MyPK;
//				dbUpload.FileName = fileName;
//				dbUpload.FileSize = (float)info.length();
//				dbUpload.RDT = DataType.getCurrentDateTimess();
//				dbUpload.Rec = WebUser.getNo();
//				dbUpload.RecName = WebUser.getName();
//				dbUpload.FK_Dept = WebUser.getFK_Dept();
//				dbUpload.FK_DeptName = WebUser.getFK_DeptName();
//				if (athDesc.IsExpCol == true)
//				{
//					if (paras != null && paras.length() > 0)
//					{
//						for (String para : paras.split("[@]", -1))
//						{
//							dbUpload.SetPara(para.split("[=]", -1)[0], para.split("[=]", -1)[1]);
//						}
//					}
//				}
//
//				dbUpload.UploadGUID = guid;
//
//				if (athDesc.getAthSaveWay() == AthSaveWay.DB)
//				{
//					dbUpload.Insert();
//					//把文件保存到指定的字段里.
//					dbUpload.SaveFileToDB("FileDB", temp);
//				}
//
//				if (athDesc.getAthSaveWay() == AthSaveWay.FTPServer)
//				{
//					/*保存到fpt服务器上.*/
//					FtpConnection ftpconn = null;
//					try
//					{
//						ftpconn = new FtpConnection(SystemConfig.FTPServerIP, SystemConfig.FTPServerPort, SystemConfig.FTPUserNo, SystemConfig.FTPUserPassword);
//					}
//					catch (java.lang.Exception e3)
//					{
//						throw new RuntimeException("err@FTP连接失败请检查账号,密码，端口号是否正确");
//					}
//
//
//					String ny = Date.now().toString("yyyy_MM");
//
//					//判断目录年月是否存在.
//					if (ftpconn.DirectoryExist(ny) == false)
//					{
//						ftpconn.CreateDirectory(ny);
//					}
//					ftpconn.SetCurrentDirectory(ny);
//
//					//判断目录是否存在.
//					if (ftpconn.DirectoryExist(athDesc.getFK_MapData()) == false)
//					{
//						ftpconn.CreateDirectory(athDesc.getFK_MapData());
//					}
//
//					//设置当前目录，为操作的目录。
//					ftpconn.SetCurrentDirectory(athDesc.getFK_MapData());
//
//					//把文件放上去.
//					try
//					{
//						ftpconn.PutFile(temp, guid + "." + dbUpload.FileExts);
//					}
//					catch (java.lang.Exception e4)
//					{
//						throw new RuntimeException("err@FTP端口号受限或者防火墙未关闭");
//					}
//					ftpconn.Close();
//
//					//设置路径.
//					dbUpload.FileFullName = ny + "//" + athDesc.getFK_MapData() + "//" + guid + "." + dbUpload.FileExts;
//					dbUpload.Insert();
//					(new File(temp)).delete();
//				}
//
//				uploadFileM += dbUpload.MyPK + ",";
//
//				//执行附件上传后事件，added by liuxc,2017-7-15
//				msg = ExecEvent.DoFrm(mapData, EventListFrm.AthUploadeAfter, en, "@FK_FrmAttachment=" + dbUpload.FK_FrmAttachment + "@FK_FrmAttachmentDB=" + dbUpload.MyPK + "@FileFullName=" + temp);
//				if (DataType.IsNullOrEmpty(msg) == false)
//				{
//					bp.sys.base.Glo.WriteLineError("@AthUploadeAfter事件返回信息，文件：" + dbUpload.FileName + "，" + msg);
//				}
//
//			}
//
//				///#endregion 保存到数据库.
//
//		}
//		//需要判断是否存在AthNum字段
//		if (en.Row.get("AthNum") != null)
//		{
//			int athNum = Integer.parseInt(en.Row.get("AthNum").toString());
//			en.Row.set("AthNum", athNum + 1);
//			en.Update();
//		}
//		//return uploadFileM;
//		if (DataType.IsNullOrEmpty(empNo))
//		{
//			return uploadFileM;
//		}
//		else
//		{
//			return "{\"msg\":\"上传成功\"}";
//		}
//	}
//
//	/**
//	 删除附件
//
//	 @return
//	*/
//	public final String DelWorkCheckAttach() throws Exception {
//		FrmAttachmentDB athDB = new FrmAttachmentDB();
//		athDB.RetrieveByAttr(FrmAttachmentDBAttr.MyPK, this.getMyPK());
//
//		//删除文件
//		if (athDB.FileFullName != null)
//		{
//			if ((new File(athDB.FileFullName)).isFile() == true)
//			{
//				(new File(athDB.FileFullName)).delete();
//			}
//		}
//
//		int i = athDB.Delete(FrmAttachmentDBAttr.MyPK, this.getMyPK());
//		if (i > 0)
//		{
//			return "true";
//		}
//		return "false";
//	}

	public final String FrmVSTO_Init() throws Exception {
		return "";
	}
	/** 
	 表单处理加载
	 
	 @return 
	*/
	public final String FrmSingle_Init() throws Exception {
		if (DataType.IsNullOrEmpty(this.getFK_MapData()))
		{
			throw new RuntimeException("FK_MapData参数不能为空");
		}

		MapData md = new MapData();
		md.setNo(this.getFK_MapData());

		if (md.RetrieveFromDBSources() == 0)
		{
			throw new RuntimeException("未检索到FK_MapData=" + this.getFK_MapData() + "的表单，请核对参数");
		}

		int minOID = 10000000; //最小OID设置为一千万
		int oid = Math.toIntExact(this.getOID());
		Hashtable ht = new Hashtable();
		GEEntity en = md.getHisGEEn();

		if (oid == 0)
		{
			oid = minOID;
		}

		en.setOID(oid);

		if (en.RetrieveFromDBSources() == 0)
		{
			ht.put("IsExist", 0);
		}
		else
		{
			ht.put("IsExist", 1);
		}

		ht.put("OID", oid);
		ht.put("UserNo", WebUser.getNo());
		ht.put("Token", WebUser.getToken());

		return Json.ToJsonEntityModel(ht);
	}


		///#region 从表的选项.
	/** 
	 初始化数据
	 
	 @return 
	*/
//	public final String DtlOpt_Init() throws Exception {
//		MapDtl dtl = new MapDtl(this.getFK_MapDtl());
//
//		if (dtl.ImpModel == 0)
//		{
//			return "err@该从表不允许导入.";
//		}
//
//		if (dtl.ImpModel == 2)
//		{
//			return "url@DtlImpByExcel.htm?FK_MapDtl=" + this.getFK_MapDtl();
//		}
//
//
//		if (DataType.IsNullOrEmpty(dtl.ImpSQLInit))
//		{
//			return "err@从表加载语句为空，请设置从表加载的sql语句。";
//		}
//
//		DataSet ds = new DataSet();
//		DataTable dt = DBAccess.RunSQLReturnTable(dtl.ImpSQLInit);
//
//		return Json.ToJson(dt);
//
//	}
//	/**
//	 增加
//
//	 @return
//	*/
//	public final String DtlOpt_Add() throws Exception {
//		MapDtl dtl = new MapDtl(this.getFK_MapDtl());
//		String pks = this.GetRequestVal("PKs");
//
//		String[] strs = pks.split("[,]", -1);
//		int i = 0;
//		for (String str : strs)
//		{
//			if (DataType.IsNullOrEmpty(str) == true || str.equals("CheckAll") == true)
//			{
//				continue;
//			}
//
//			GEDtl gedtl = new GEDtl(this.getFK_MapDtl());
//			String sql = dtl.ImpSQLFullOneRow;
//			sql = sql.replace("@Key", str);
//
//			DataTable dt = DBAccess.RunSQLReturnTable(sql);
//
//			if (dt.Rows.size() == 0)
//			{
//				return "err@导入数据失败:" + sql;
//			}
//
//			gedtl.Copy(dt.Rows.get(0));
//			gedtl.setRefPK(this.GetRequestVal("RefPKVal"));
//			gedtl.InsertAsNew();
//			i++;
//		}
//
//		return "成功的导入了[" + i + "]行数据...";
//	}
	/** 
	 执行查询.
	 
	 @return 
	*/
	public final String DtlOpt_Search() throws Exception {
		MapDtl dtl = new MapDtl(this.getFK_MapDtl());

		String sql = dtl.getImpSQLSearch();
		sql = sql.replace("@Key", this.GetRequestVal("Key"));
		sql = sql.replace("@WebUser.No", WebUser.getNo());
		sql = sql.replace("@WebUser.Name", WebUser.getName());
		sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());

		DataSet ds = new DataSet();
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		return Json.ToJson(dt);
	}

		///#endregion 从表的选项.


		///#region SQL从表导入.
	public final String DtlImpBySQL_Delete() throws Exception {
		MapDtl dtl = new MapDtl(this.getEnsName());
		DBAccess.RunSQL("DELETE FROM " + dtl.getPTable() + " WHERE RefPK='" + this.getRefPKVal() + "'");
		return "";
	}
	/** 
	 SQL从表导入
	 
	 @return 
	*/
	public final String DtlImpBySQl_Imp() throws Exception {
		//获取参数
		String ensName = this.getEnsName();
		String refpk = this.getRefPKVal();
		long pworkID = this.getPWorkID();
		int fkNode = this.getFK_Node();
		long fid = this.getFID();
		String pk = this.GetRequestVal("PKs");
		GEDtls dtls = new GEDtls(ensName);
		QueryObject qo = new QueryObject(dtls);
		//获取从表权限
		MapDtl dtl = new MapDtl(ensName);

			///#region 处理权限方案。
		if (this.getFK_Node() != 0 && this.getFK_Node() != 999999)
		{
			Node nd = new Node(this.getFK_Node());
			if (nd.getHisFormType() == NodeFormType.SheetTree || nd.getHisFormType() == NodeFormType.RefOneFrmTree)
			{
				FrmNode fn = new FrmNode(nd.getNodeID(), dtl.getFK_MapData());
				if (fn.getFrmSln() == FrmSln.Self)
				{
					String no = this.getFK_MapDtl() + "_" + nd.getNodeID();
					MapDtl mdtlSln = new MapDtl();
					mdtlSln.setNo ( no);
					int result = mdtlSln.RetrieveFromDBSources();
					if (result != 0)
					{
						dtl = mdtlSln;

					}
				}
			}
		}

			///#endregion 处理权限方案。

		//判断是否重复导入
		boolean isInsert = true;
		if (DataType.IsNullOrEmpty(pk) == false)
		{
			String[] pks = pk.split("[@]", -1);
			int idx = 0;
			for (String k : pks)
			{
				if (DataType.IsNullOrEmpty(k))
				{
					continue;
				}
				if (idx == 0)
				{
					qo.AddWhere(k, bp.sys.Glo.getRequest().getParameter(k));
				}
				else
				{
					qo.addAnd();
					qo.AddWhere(k, bp.sys.Glo.getRequest().getParameter(k));
				}
				idx++;
			}
			switch (dtl.getDtlOpenType())
			{
				case ForEmp: // 按人员来控制.
					qo.addAnd();
					qo.AddWhere("RefPk", refpk);
					qo.addAnd();
					qo.AddWhere("Rec", this.GetRequestVal("UserNo"));
					break;
				case ForWorkID: // 按工作ID来控制
					qo.addAnd();
					qo.addLeftBracket();
					qo.AddWhere("RefPk", refpk);
					qo.addOr();
					qo.AddWhere("FID", fid);
					qo.addRightBracket();
					break;
				case ForFID: // 按流程ID来控制.
					qo.addAnd();
					qo.AddWhere("FID", fid);
					break;
			}

			int count = qo.GetCount();
			if (count > 0)
			{
				isInsert = false;
			}
		}
		//导入数据
		if (isInsert == true)
		{

			bp.en.Entity tempVar = dtls.getGetNewEntity();
			GEDtl dtlEn = tempVar instanceof GEDtl ? (GEDtl)tempVar : null;
			//遍历属性，循环赋值.
			for (Attr attr : dtlEn.getEnMap().getAttrs())
			{
				dtlEn.SetValByKey(attr.getKey(), this.GetRequestVal(attr.getKey()));

			}
			switch (dtl.getDtlOpenType()) {
				case ForEmp: // 按人员来控制.
					dtlEn.setRefPKInt(Integer.parseInt(refpk));
					break;
				case ForWorkID: // 按工作ID来控制
					dtlEn.setRefPKInt(Integer.parseInt(refpk));
					dtlEn.SetValByKey("FID", Integer.parseInt(refpk));
					break;
				case ForFID: // 按流程ID来控制.
					dtlEn.setRefPKInt(Integer.parseInt(refpk));
					dtlEn.SetValByKey("FID", fid);
					break;
			}
			dtlEn.SetValByKey("RDT", DataType.getCurrentDate());
			dtlEn.SetValByKey("Rec", this.GetRequestVal("UserNo"));
			//dtlEn.setOID((int)DBAccess.GenerOID(ensName);

			dtlEn.InsertAsOID((int)DBAccess.GenerOID(ensName));
			return String.valueOf(dtlEn.getOID());
		}

		return "";

	}

		///#endregion SQL从表导入


		///#region Excel导入.
	/** 
	 导入excel.
	 
	 @return 
	*/
	public final String DtlImpByExcel_Imp() throws Exception {
		String tempPath = SystemConfig.getPathOfTemp();
		try {
			MapDtl dtl = new MapDtl(this.getFK_MapDtl());
			HttpServletRequest request = getRequest();
			if (CommonFileUtils.getFilesSize(request, "File_Upload") == 0) {
				return "err@请选择要导入的数据信息。";
			}

			String fileName = CommonFileUtils.getOriginalFilename(request, "File_Upload");
			String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
			if (!prefix.equals("xls") && !prefix.equals("xlsx")) {

				return "err@上传的文件必须是Excel文件.";
			}

			String errInfo = "";
			String ext = ".xls";
			if (fileName.contains(".xlsx")) {
				ext = ".xlsx";
			}


			//设置文件名
			String fileNewName = DateUtils.format(new Date(), "yyyyMMddHHmmss") ;
			File file =null;
			try {
				file = File.createTempFile(fileNewName, ext);
			} catch (IOException e1) {
				file = new File(System.getProperty("java.io.tmpdir"), fileNewName + ext);
			}
			file.deleteOnExit();
			try {
				CommonFileUtils.upload(request, "File_Upload", file);
			} catch (Exception e) {
				e.printStackTrace();
				return "err@执行失败";
			}

			GEDtls dtls = new GEDtls(this.getFK_MapDtl());
			DataTable dt = DBLoad.GetTableByExt(file.getAbsolutePath());

			/// #region 检查两个文件是否一致。 生成要导入的属性
			Attrs attrs = dtls.getGetNewEntity().getEnMap().getAttrs();
			Attrs attrsExp = new Attrs();

			boolean isHave = false;
			for (DataColumn dc : dt.Columns) {
				for (Attr attr : attrs) {
					if (dc.ColumnName.equals(attr.getDesc())) {
						isHave = true;
						attrsExp.Add(attr);
						continue;
					}

					if (dc.ColumnName.toLowerCase().equals(attr.getKey().toLowerCase())) {
						isHave = true;
						attrsExp.Add(attr);
						dc.ColumnName = attr.getDesc();
					}
				}
			}
			if (isHave == false)
				throw new Exception("@您导入的excel文件不符合系统要求的格式，请下载模版文件重新填入。");
			/// #endregion

			/// #region 执行导入数据.

			if (this.GetRequestValInt("DDL_ImpWay") == 0)
				DBAccess.RunSQL("DELETE FROM " + dtl.getPTable() + " WHERE RefPK='" + this.getWorkID() + "'");

			int i = 0;
			int oid = (int) DBAccess.GenerOID("Dtl", dt.Rows.size());
			String rdt = DataType.getCurrentDate();

			String errMsg = "";
			for (DataRow dr : dt.Rows) {
				GEDtl dtlEn = (GEDtl) dtls.getGetNewEntity();
				dtlEn.ResetDefaultVal();

				for (Attr attr : attrsExp) {
					if (attr.getUIVisible() == false || dr.getValue(attr.getDesc()) == "")
						continue;
					String val = dr.getValue(attr.getDesc()).toString();
					if (val == null)
						continue;
					val = val.trim();
					switch (attr.getMyFieldType()) {
						case Enum:
						case PKEnum:
							SysEnums ses = new SysEnums(attr.getUIBindKey());
							boolean isHavel = false;
							for (SysEnum se : ses.ToJavaList()) {
								if (val == se.getLab()) {
									val = String.valueOf(se.getIntKey());
									isHavel = true;
									break;
								}
							}
							if (isHavel == false) {
								errMsg += "@数据格式不规范,第(" + i + ")行，列(" + attr.getDesc() + ")，数据(" + val
										+ ")不符合格式,改值没有在枚举列表里.";
								val = attr.getDefaultVal().toString();
							}
							break;
						case FK:
						case PKFK:
							Entities ens = null;
							if (attr.getUIBindKey().contains("."))
								ens = ClassFactory.GetEns(attr.getUIBindKey());
							else
								ens = new GENoNames(attr.getUIBindKey(), "desc");

							ens.RetrieveAll();
							boolean isHavelIt = false;
							for (Entity en : ens) {
								if (val == en.GetValStrByKey("Name")) {
									val = en.GetValStrByKey("No");
									isHavelIt = true;
									break;
								}
							}
							if (isHavelIt == false)
								errMsg += "@数据格式不规范,第(" + i + ")行，列(" + attr.getDesc() + ")，数据(" + val
										+ ")不符合格式,改值没有在外键数据列表里.";
							break;
						default:
							break;
					}

					if (attr.getMyDataType() == DataType.AppBoolean) {
						if (val.trim().equals("是") || val.trim().toLowerCase().equals("yes"))
							val = "1";

						if (val.trim().equals("否") || val.trim().toLowerCase().equals("no"))
							val = "0";
					}

					dtlEn.SetValByKey(attr.getKey(), val);
				}
				// dtlEn.RefPKInt = (int)this.getWorkID();
				// 关联主赋值.
				switch (dtl.getDtlOpenType()) {
					case ForEmp: // 按人员来控制.
						dtlEn.setRefPKInt((int) this.getWorkID());
						break;
					case ForWorkID: // 按工作ID来控制
						dtlEn.setRefPKInt((int) this.getWorkID());
						dtl.SetValByKey("FID", this.getWorkID());
						break;
					case ForFID: // 按流程ID来控制.
						dtlEn.setRefPKInt((int) this.getWorkID());
						dtl.SetValByKey("FID", this.getFID());
						break;
				}

				dtlEn.SetValByKey("RDT", rdt);
				dtlEn.SetValByKey("Rec", WebUser.getNo());
				i++;

				dtlEn.InsertAsOID(oid);
				oid++;
			}
			/// #endregion 执行导入数据.

			if (StringUtils.isEmpty(errMsg) == true)
				return "info@共有(" + i + ")条数据导入成功。";
			else
				return "共有(" + i + ")条数据导入成功，但是出现如下错误:" + errMsg;

		} catch (Exception ex) {
			String msg = ex.getMessage().replace("'", "‘");
			return "err@" + msg;
		}
	}

	/** 
	 BP类从表导入
	 
	 @return 
	*/
	private String BPDtlImpByExcel_Imp(DataTable dt, String fk_mapdtl)throws Exception
	{
		try
		{

				///#region 检查两个文件是否一致。 生成要导入的属性
			Entities dtls = ClassFactory.GetEns(this.getFK_MapDtl());
			bp.en.Entity tempVar = dtls.getGetNewEntity();
			EntityOID dtlEn = tempVar instanceof EntityOID ? (EntityOID)tempVar : null;
			Attrs attrs = dtlEn.getEnMap().getAttrs();
			Attrs attrsExp = new Attrs();

			boolean isHave = false;
			for (DataColumn dc : dt.Columns)
			{
				for (Attr attr : attrs.ToJavaList())
				{
					if (attr.getDesc().equals(dc.ColumnName))
					{
						isHave = true;
						attrsExp.add(attr);
						continue;
					}

					if (dc.ColumnName.toLowerCase().equals(attr.getKey().toLowerCase()))
					{
						isHave = true;
						attrsExp.add(attr);
						dc.ColumnName = attr.getDesc();
					}
				}
			}
			if (isHave == false)
			{
				return "err@您导入的excel文件不符合系统要求的格式，请下载模版文件重新填入。";
			}

				///#endregion


				///#region 执行导入数据.

			if (this.GetRequestValInt("DDL_ImpWay") == 0)
			{
				DBAccess.RunSQL("DELETE FROM " + dtlEn.getEnMap().getPhysicsTable() + " WHERE RefPK='" + this.GetRequestVal("RefPKVal") + "'");
			}

			int i = 0;
			long oid = DBAccess.GenerOID(dtlEn.getEnMap().getPhysicsTable(), dt.Rows.size());
			String rdt = DataType.getCurrentDate();

			String errMsg = "";
			for (DataRow dr : dt.Rows)
			{
				bp.en.Entity tempVar2 = dtls.getGetNewEntity();
				dtlEn = tempVar2 instanceof EntityOID ? (EntityOID)tempVar2 : null;
				dtlEn.ResetDefaultVal(null, null, 0);

				for (Attr attr : attrsExp)
				{
					if (attr.getUIVisible()== false || dr.getValue(attr.getDesc()) == null )
	{continue;
					}
					String val = dr.getValue(attr.getDesc()).toString();
					if (val == null)
					{
						continue;
					}
					val = val.trim();
					switch (attr.getMyFieldType())
					{
						case Enum:
						case PKEnum:
							SysEnums ses = new SysEnums(attr.getUIBindKey());
							boolean isHavel = false;
							for (SysEnum se : ses.ToJavaList())
							{
								if (se.getLab().equals(val))
								{
									val = String.valueOf(se.getIntKey());
									isHavel = true;
									break;
								}
							}
							if (isHavel == false)
							{
								errMsg += "@数据格式不规范,第(" + i + ")行，列(" + attr.getDesc() + ")，数据(" + val + ")不符合格式,改值没有在枚举列表里.";
								val = attr.getDefaultVal().toString();
							}
							break;
						case FK:
						case PKFK:
							Entities ens = null;
							if (attr.getUIBindKey().contains("."))
							{
								ens = ClassFactory.GetEns(attr.getUIBindKey());
							}
							else
							{
								ens = new GENoNames(attr.getUIBindKey(), "desc");
							}

							ens.RetrieveAll();
							boolean isHavelIt = false;
							for (Entity en : ens)
							{
								if (en.GetValStrByKey("Name").equals(val))
								{
									val = en.GetValStrByKey("No");
									isHavelIt = true;
									break;
								}
							}
							if (isHavelIt == false)
							{
								errMsg += "@数据格式不规范,第(" + i + ")行，列(" + attr.getDesc() + ")，数据(" + val + ")不符合格式,改值没有在外键数据列表里.";
							}
							break;
						default:
							break;
					}

					if (attr.getMyDataType() == DataType.AppBoolean)
					{
						if (val.trim().equals("是") || val.trim().toLowerCase().equals("yes"))
						{
							val = "1";
						}

						if (val.trim().equals("否") || val.trim().toLowerCase().equals("no"))
						{
							val = "0";
						}
					}

					dtlEn.SetValByKey(attr.getKey(), val);
				}

				dtlEn.SetValByKey("RefPK", this.GetRequestVal("RefPKVal"));
				i++;

				dtlEn.Insert();
			}

				///#endregion 执行导入数据.

			if (DataType.IsNullOrEmpty(errMsg) == true)
			{
				return "info@共有(" + i + ")条数据导入成功。";
			}
			else
			{
				return "共有(" + i + ")条数据导入成功，但是出现如下错误:" + errMsg;
			}

		}
		catch (RuntimeException ex)
		{
			String msg = ex.getMessage().replace("'", "‘");
			return "err@" + msg;
		}
	}

		///#endregion  Excel导入.


		///#region 打印.
	public final String Print_Init() throws Exception {
		//string ApplicationPath = this.context.Request.PhysicalApplicationPath;
		//String ApplicationPath = HttpContextHelper.RequestApplicationPath;
		String ApplicationPath = SystemConfig.getHostURLOfBS();

		Node nd = new Node(this.getFK_Node());
		String path = ApplicationPath + "DataUser/CyclostyleFile/FlowFrm/" + nd.getFK_Flow() + "/" + nd.getNodeID() + "/";
		String[] fls = null;
		try
		{
			fls = (new File(path)).list();
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}


		DataTable dt = new DataTable();
		dt.Columns.Add("BillNo");
		dt.Columns.Add("BillName");

		int idx = 0;
		int fileIdx = -1;
		for (String f : fls)
		{
			fileIdx++;
			String myfile = f.replace(path, "");
			String[] strs = myfile.split("[.]", -1);
			idx++;

			DataRow dr = dt.NewRow();
			dr.setValue("BillNo", strs[0]);
			dr.setValue("BillName", strs[1]);

			dt.Rows.add(dr);
		}
		//返回json.
		return Json.ToJson(dt);
	}

		///#endregion 打印.


		///#region 附件组件.
	/** 
	 执行删除
	 
	 @return 
	*/
	public final String AttachmentUpload_Del() throws Exception {
		//执行删除.
		String delPK = this.GetRequestVal("DelPKVal");

		FrmAttachmentDB delDB = new FrmAttachmentDB();
		delDB.setMyPK(delPK == null ? this.getMyPK() : delPK);
		delDB.RetrieveFromDBSources();
		delDB.Delete(); //删除上传的文件.
		//OSS服务器
		bp.sys.FrmAttachment myathDesc = new FrmAttachment(delDB.getFK_FrmAttachment());
		if (myathDesc.getAthSaveWay() == AthSaveWay.OSS){
			//删除云端文件
			OSSUploadFileUtils ossUploadFileUtils = new OSSUploadFileUtils();
			ossUploadFileUtils.deleteFile(myathDesc.getName());
		}
		return "删除成功.";
	}
	public final String AttachmentUpload_DownByStream() throws Exception {
		// return AttachmentUpload_Down(true);
		return AttachmentUpload_Down();
	}
	/** 
	 下载
	 
	 @return 
	*/
	public final String AttachmentUpload_Down() throws Exception {
		String zipName = this.getWorkID() + "_" + this.getFK_FrmAttachment();
		/// #region 处理权限控制.
		FrmAttachment athDesc = this.GenerAthDesc();

		// 查询出来数据实体.
		FrmAttachmentDBs dbs = bp.wf.Glo.GenerFrmAttachmentDBs(athDesc, this.getPKVal(), this.getFK_FrmAttachment());
		/// #endregion 处理权限控制.

		if (dbs.size() == 0)
			return "err@文件不存在，不需打包下载。";

		String basePath = SystemConfig.getPathOfDataUser() + "Temp";
		String tempUserPath = basePath + "/" + WebUser.getNo();
		String tempFilePath = basePath + "/" + WebUser.getNo() + "/" + this.getWorkID();
		String zipPath = basePath + "/" + WebUser.getNo();
		String zipFile = zipPath + "/" + zipName + ".zip";
		String info = "";

		File tempFile = new File(tempFilePath);
		try {
			// 删除临时文件，保证一个用户只能存一份，减少磁盘占用空间.
			info = "@创建用户临时目录:" + tempUserPath;
			File file = new File(tempUserPath);
			if (file.exists() == false)
				file.mkdirs();

			// 如果有这个临时的目录就把他删除掉.
			if (tempFile.exists() == true) {
				// tempFile.delete();
				boolean success = FileAccess.deletesFile(tempFile);
				if (!success) {
					Log.DebugWriteInfo("删除临时目录失败");
				}
			}
			if (!tempFile.exists()) {
				tempFile.mkdirs();
			}
		} catch (Exception ex) {
			return "err@组织临时目录出现错误:" + ex.getMessage();
		}

		try {
			for (FrmAttachmentDB db : dbs.ToJavaList()) {
				String copyToPath = tempFilePath;

				// 求出文件路径.
				String fileTempPath = db.GenerTempFile(athDesc.getAthSaveWay());
				if (athDesc.getAthSaveWay() == AthSaveWay.FTPServer) {
					FtpUtil ftpUtil = bp.wf.Glo.getFtpUtil();
					ftpUtil.downloadFile(db.getFileFullName(), fileTempPath);
				}

				if (DataType.IsNullOrEmpty(db.getSort()) == false) {
					copyToPath = tempFilePath + "/" + db.getSort();
					File copyPath = new File(copyToPath);
					if (copyPath.exists() == false)
						copyPath.mkdirs();
				}
				// 新文件目录
				copyToPath = copyToPath + "/" + db.getFileName();
				try {
					InputStream is = new FileInputStream(fileTempPath);
					;
					int buffer = 1024; // 定义缓冲区的大小
					int length = 0;
					byte[] b = new byte[buffer];
					double percent = 0;
					FileOutputStream fos = new FileOutputStream(new File(copyToPath));
					while ((length = is.read(b)) != -1) {
						// percent += length / (double) new
						// File(saveTo).supFileSize * 100D; // 计算上传文件的百分比
						fos.write(b, 0, length); // 向文件输出流写读取的数据

					}
					fos.close();
					new File(fileTempPath).delete();
					if (new File(fileTempPath).exists() == true)
						new File(fileTempPath).delete();
				} catch (RuntimeException ex) {
					ex.getMessage();
				}
				// File.Copy(fileTempPath, copyToPath, true);
			}
		} catch (Exception ex) {
			return "err@组织文件期间出现错误:" + ex.getMessage();
		}

		File zipFileFile = new File(zipFile);
		try {
			while (zipFileFile.exists() == true) {
				zipFileFile.delete();
			}
			// 执行压缩.
			ZipCompress fz = new ZipCompress(zipFile, tempFilePath);
			fz.zip();
			// 删除临时文件夹
			tempFile.delete();
		} catch (Exception ex) {
			return "err@执行压缩出现错误:" + ex.getMessage() + ",路径tempPath:" + tempFilePath + ",zipFile=" + zipFile;
		}

		if (zipFileFile.exists() == false)
			return "err@压缩文件未生成成功,请在点击一次.";

		zipName = DataType.PraseStringToUrlFileName(zipName);

		String url = getRequest().getContextPath() + "DataUser/Temp/" + WebUser.getNo() + "/" + zipName + ".zip";
		return "url@" + url;
	}



//	public final void AttachmentDownFromByte() throws Exception {
//		FrmAttachmentDB downDB = new FrmAttachmentDB();
//		downDB.setMyPK(this.getMyPK());
//		downDB.Retrieve();
//		downDB.FileName = HttpUtility.UrlEncode(downDB.FileName);
//
////ORIGINAL LINE: byte[] byteList = downDB.GetFileFromDB("FileDB", null);
//		byte[] byteList = downDB.GetFileFromDB("FileDB", null);
//		if (byteList != null)
//		{
//			//HttpContext.Current.Response.Charset = "GB2312";
//			//HttpContext.Current.Response.AddHeader("Content-Disposition", "attachment;filename=" + downDB.FileName);
//			//HttpContext.Current.Response.ContentType = "application/octet-stream;charset=gb2312";
//			//HttpContext.Current.Response.BinaryWrite(byteList);
//			//HttpContext.Current.Response.End();
//			//HttpContext.Current.Response.Close();
//			HttpContextHelper.ResponseWriteFile(byteList, downDB.FileName, "application/octet-stream;charset=gb2312");
//		}
//	}
	/** 
	 附件ID.
	*/
	public final String getFKFrmAttachment() throws Exception {
		return this.GetRequestVal("FK_FrmAttachment");
	}
	public final FrmAttachment GenerAthDescOfFoolTruck() throws Exception {
		FoolTruckNodeFrm sln = new FoolTruckNodeFrm();
		sln.setFrmSln(-1);
		String fromFrm = this.GetRequestVal("FromFrm");
		sln.setMyPK(fromFrm + "_" + this.getFK_Node() + "_" + this.getFK_Flow());
		int result = sln.RetrieveFromDBSources();
		FrmAttachment athDesc = new FrmAttachment();
		athDesc.setMyPK(this.getFKFrmAttachment());
		athDesc.RetrieveFromDBSources();

		/*没有查询到解决方案, 就是只读方案 */
		if (result == 0 || sln.getFrmSln() == 1)
		{
			athDesc.setIsUpload(false);
			athDesc.setIsDownload(true);
			athDesc.setHisDeleteWay(AthDeleteWay.None); //删除模式.
			return athDesc;
		}
		//默认方案
		if (sln.getFrmSln() == 0)
		{
			return athDesc;
		}

		//如果是自定义方案,就查询自定义方案信息.
		if (sln.getFrmSln() == 2)
		{
			FrmAttachment athDescNode = new FrmAttachment();
			athDescNode.setMyPK(this.getFKFrmAttachment() + "_" + this.getFK_Node());
			if (athDescNode.RetrieveFromDBSources() == 0)
			{
				//没有设定附件权限，保持原来的附件权限模式
				return athDesc;
			}
			return athDescNode;
		}

		return null;
	}
	/** 
	 生成描述
	 
	 @return 
	*/
	public final FrmAttachment GenerAthDesc() throws Exception {

		/// 为累加表单做的特殊判断.
		if (this.GetRequestValInt("FormType") == 10) {
			if (this.getFK_FrmAttachment().contains(this.getFK_MapData()) == false) {
				return GenerAthDescOfFoolTruck(); // 如果当前表单的ID。
			}
		}

		///

		bp.sys.FrmAttachment athDesc = new bp.sys.FrmAttachment();
		athDesc.setMyPK(this.getFK_FrmAttachment());
		if (this.getFK_Node() == 0 || this.getFK_Flow() == null) {
			athDesc.RetrieveFromDBSources();
			return athDesc;
		}

		athDesc.setMyPK(this.getFK_FrmAttachment());
		int result = athDesc.RetrieveFromDBSources();

		/// 判断是否是明细表的多附件.
		if (result == 0 && DataType.IsNullOrEmpty(this.getFK_Flow()) == false
				&& this.getFK_FrmAttachment().contains("AthMDtl")) {
			athDesc.setFK_MapData(this.getFK_MapData());
			athDesc.setNoOfObj("AthMDtl");
			athDesc.setName("我的从表附件");
			athDesc.setUploadType(AttachmentUploadType.Multi);
			athDesc.Insert();
		}

		/// 判断是否是明细表的多附件。

		/// 判断是否可以查询出来，如果查询不出来，就可能是公文流程。
		if (result == 0 && DataType.IsNullOrEmpty(this.getFK_Flow()) == false
				&& this.getFK_FrmAttachment().contains("DocMultiAth")) {
			/* 如果没有查询到它,就有可能是公文多附件被删除了. */
			athDesc.setMyPK(this.getFK_FrmAttachment());
			athDesc.setNoOfObj("DocMultiAth");
			athDesc.setFK_MapData(this.getFK_MapData());
			athDesc.setExts("*.*");

			// 存储路径.
			// athDesc.SaveTo = "/DataUser/UploadFile/";
			athDesc.setIsNote(false); // 不显示note字段.
			athDesc.setIsVisable(false); // 让其在form 上不可见.

			// 位置.
			athDesc.setX((float) 94.09);
			athDesc.setY((float) 333.18);
			athDesc.setW((float) 626.36);
			athDesc.setH((float) 150);

			// 多附件.
			athDesc.setUploadType(AttachmentUploadType.Multi);
			athDesc.setName("公文多附件(系统自动增加)");
			athDesc.SetValByKey("AtPara",
					"@IsWoEnablePageset=1@IsWoEnablePrint=1@IsWoEnableViewModel=1@IsWoEnableReadonly=0@IsWoEnableSave=1@IsWoEnableWF=1@IsWoEnableProperty=1@IsWoEnableRevise=1@IsWoEnableIntoKeepMarkModel=1@FastKeyIsEnable=0@IsWoEnableViewKeepMark=1@FastKeyGenerRole=@IsWoEnableTemplete=1");
			athDesc.Insert();

			// 有可能在其其它的节点上没有这个附件，所以也要循环增加上它.
			bp.wf.Nodes nds = new bp.wf.Nodes(this.getFK_Flow());
			for (bp.wf.Node nd : nds.ToJavaList()) {
				athDesc.setFK_MapData("ND" + nd.getNodeID());
				athDesc.setMyPK(athDesc.getFK_MapData() + "_" + athDesc.getNoOfObj());
				if (athDesc.getIsExits() == true) {
					continue;
				}

				athDesc.Insert();
			}

			// 重新查询一次，把默认值加上.
			athDesc.RetrieveFromDBSources();
		}

		/// 判断是否可以查询出来，如果查询不出来，就可能是公文流程。

		/// 处理权限方案。
		if (this.getFK_Node() != 0) {
			String fk_mapdata = this.getFK_MapData();
			if (this.getFK_FrmAttachment().contains("AthMDtl") == true) {
				fk_mapdata = this.GetRequestVal("FFK_MapData");
			}

			if (DataType.IsNullOrEmpty(fk_mapdata)) {
				fk_mapdata = this.GetRequestVal("FK_MapData");
			}

			Node nd = new Node(this.getFK_Node());
			if (nd.getHisFormType() == NodeFormType.SheetTree || nd.getHisFormType() == NodeFormType.RefOneFrmTree) {
				// 如果是绑定表单树中的表单，重新赋值绑定表单的名字
				if (nd.getHisFormType() == NodeFormType.RefOneFrmTree) {
					fk_mapdata = nd.getNodeFrmID();
				}
				FrmNode fn = new FrmNode(nd.getNodeID(), fk_mapdata);
				/*
				 * if (fn.FrmSln == FrmSln.Default) { if (fn.WhoIsPK ==
				 * WhoIsPK.getFID()) athDesc.HisCtrlWay = AthCtrlWay.getFID();
				 *
				 * if (fn.WhoIsPK == WhoIsPK.PWorkID) athDesc.HisCtrlWay =
				 * AthCtrlWay.PWorkID;
				 *
				 * if (fn.WhoIsPK == WhoIsPK.P2WorkID) athDesc.HisCtrlWay =
				 * AthCtrlWay.P2WorkID;
				 *
				 *
				 * }
				 *
				 * if (fn.FrmSln == FrmSln.Readonly) { if (fn.WhoIsPK ==
				 * WhoIsPK.getFID()) athDesc.HisCtrlWay = AthCtrlWay.getFID();
				 *
				 * if (fn.WhoIsPK == WhoIsPK.PWorkID) athDesc.HisCtrlWay =
				 * AthCtrlWay.PWorkID;
				 *
				 * athDesc.HisDeleteWay = AthDeleteWay.None; athDesc.IsUpload =
				 * false; athDesc.IsDownload = true;
				 * athDesc.setMyPK(this.FK_FrmAttachment; return athDesc; }
				 */

				if (fn.getFrmSln() == FrmSln.Self) {
					if (this.getFK_FrmAttachment().contains("AthMDtl") == true) {
						athDesc.setMyPK(this.getFK_MapData() + "_" + nd.getNodeID() + "_AthMDtl");
						athDesc.RetrieveFromDBSources();
					} else {
						athDesc.setMyPK(this.getFK_FrmAttachment() + "_" + nd.getNodeID());
						athDesc.RetrieveFromDBSources();
					}
					athDesc.setMyPK(this.getFK_FrmAttachment());
					return athDesc;
				}
			}
		}

		/// 处理权限方案。

		return athDesc;
	}

	public final String getFK_FrmAttachment() {
		return this.GetRequestVal("FK_FrmAttachment");
	}
	/** 
	 打包下载.
	 
	 @return 
	*/
	public final String AttachmentUpload_DownZip() throws Exception {
		String zipName = this.getWorkID() + "_" + this.getFK_FrmAttachment();
		/// #region 处理权限控制.
		FrmAttachment athDesc = this.GenerAthDesc();

		// 查询出来数据实体.
		FrmAttachmentDBs dbs = bp.wf.Glo.GenerFrmAttachmentDBs(athDesc, this.getPKVal(), this.getFK_FrmAttachment());
		/// #endregion 处理权限控制.

		if (dbs.size() == 0)
			return "err@文件不存在，不需打包下载。";

		String basePath = SystemConfig.getPathOfTemp();
		String tempUserPath = basePath + "/" + WebUser.getNo();
		String tempFilePath = basePath + "/" + WebUser.getNo() + "/" + this.getWorkID();
		String zipPath = basePath + "/" + WebUser.getNo();
		String zipFile = zipPath + "/" + zipName + ".zip";
		String info = "";

		File tempFile = new File(tempFilePath);
		try {
			// 删除临时文件，保证一个用户只能存一份，减少磁盘占用空间.
			info = "@创建用户临时目录:" + tempUserPath;
			File file = new File(tempUserPath);
			if (file.exists() == false)
				file.mkdirs();

			// 如果有这个临时的目录就把他删除掉.
			if (tempFile.exists() == true) {
				// tempFile.delete();
				boolean success = FileAccess.deletesFile(tempFile);
				if (!success) {
					Log.DebugWriteInfo("删除临时目录失败");
				}
			}
			if (!tempFile.exists()) {
				tempFile.mkdirs();
			}
		} catch (Exception ex) {
			return "err@组织临时目录出现错误:" + ex.getMessage();
		}

		try {
			for (FrmAttachmentDB db : dbs.ToJavaList()) {
				String copyToPath = tempFilePath;

				// 求出文件路径.
				String fileTempPath = db.GenerTempFile(athDesc.getAthSaveWay());
				if (athDesc.getAthSaveWay() == AthSaveWay.FTPServer) {
					FtpUtil ftpUtil = bp.wf.Glo.getFtpUtil();
					ftpUtil.downloadFile(db.getFileFullName(), fileTempPath);
				}

				if (DataType.IsNullOrEmpty(db.getSort()) == false) {
					copyToPath = tempFilePath + "/" + db.getSort();
					File copyPath = new File(copyToPath);
					if (copyPath.exists() == false)
						copyPath.mkdirs();
				}
				// 新文件目录
				copyToPath = copyToPath + "/" + db.getFileName();
				try {
					InputStream is = new FileInputStream(fileTempPath);
					;
					int buffer = 1024; // 定义缓冲区的大小
					int length = 0;
					byte[] b = new byte[buffer];
					double percent = 0;
					FileOutputStream fos = new FileOutputStream(new File(copyToPath));
					while ((length = is.read(b)) != -1) {
						fos.write(b, 0, length); // 向文件输出流写读取的数据

					}
					fos.close();
					new File(fileTempPath).delete();
					if (new File(fileTempPath).exists() == true)
						new File(fileTempPath).delete();
				} catch (RuntimeException ex) {
					ex.getMessage();
				}
			}
		} catch (Exception ex) {
			return "err@组织文件期间出现错误:" + ex.getMessage();
		}

		File zipFileFile = new File(zipFile);
		try {
			while (zipFileFile.exists() == true) {
				zipFileFile.delete();
			}
			// 执行压缩.
			ZipCompress fz = new ZipCompress(zipFile, tempFilePath);
			fz.zip();
			// 删除临时文件夹
			tempFile.delete();
		} catch (Exception ex) {
			return "err@执行压缩出现错误:" + ex.getMessage() + ",路径tempPath:" + tempFilePath + ",zipFile=" + zipFile;
		}

		if (zipFileFile.exists() == false)
			return "err@压缩文件未生成成功,请在点击一次.";

		zipName = DataType.PraseStringToUrlFileName(zipName);

		String url = "/DataUser/Temp/" + WebUser.getNo() + "/" + zipName + ".zip";
		return "url@" + url;

	}

//	public final String AthSingle_Upload() throws Exception {
//		String mypk = this.GetRequestVal("AthMyPK");
//		FrmAttachmentSingle ath = new FrmAttachmentSingle(mypk);
//		FrmAttachmentDBs dbs = new FrmAttachmentDBs();
//		String pkVal = DataType.IsNullOrEmpty(this.getPKVal()) == true ? null :this.getPKVal();
//		String fileName = mypk + ".docx";
//		String filePath = ath.SaveTo + pkVal + "/" + fileName;
//		if (HttpContextHelper.RequestFilesCount > 0)
//		{
//			//HttpPostedFile file = context.Request.Files[i];
//			HttpPostedFile file = HttpContextHelper.RequestFiles(0);
//			//文件大小，单位字节
//			int fileContentLength = file.ContentLength;
//			//上传路径
//			String savePath = ath.SaveTo + pkVal;
//			//二进制数组
//
////ORIGINAL LINE: byte[] fileBytes = null;
//			byte[] fileBytes = null;
//
////ORIGINAL LINE: fileBytes = new byte[fileContentLength];
//			fileBytes = new byte[fileContentLength];
//			//创建Stream对象，并指向上传文件
//			InputStream fileStream = file.InputStream;
//			//从当前流中读取字节，读入字节数组中
//			fileStream.read(fileBytes, 0, fileContentLength);
//
//			if ((new File(savePath)).isDirectory() == false)
//			{
//				(new File(savePath)).mkdirs();
//			}
//			//创建文件，返回一个 FileStream，它提供对 path 中指定的文件的读/写访问。
//			try (FileStream stream = File.Create(filePath))
//			{
//				//将字节数组写入流
//				stream.Write(fileBytes, 0, fileBytes.length);
//				stream.Close();
//			}
//			//HttpContextHelper.UploadFile(file, filePath);
//
//		}
//		dbs.Retrieve(FrmAttachmentDBAttr.getFK_MapData(), ath.getFK_MapData(), FrmAttachmentDBAttr.FK_FrmAttachment, ath.MyPK, FrmAttachmentDBAttr.RefPKVal, pkVal, null);
//		if (dbs.isEmpty() && ath.AthEditModel != 0)
//		{
//			//增加一条数据
//			FrmAttachmentDB db = new FrmAttachmentDB();
//			db.setMyPK(DBAccess.GenerGUID(0, null, null));
//			db.NodeID = getFK_Node();
//			db.setFK_MapData(ath.getFK_MapData());
//			db.FK_FrmAttachment = ath.MyPK;
//			db.FID = this.getFID(); //流程id.
//			db.RefPKVal = pkVal;
//			db.FileExts = "docx";
//			db.FileName = ath.MyPK + "." + db.FileExts;
//			db.RDT = DataType.getCurrentDateTimess();
//			db.Rec = WebUser.getNo();
//			db.RecName = WebUser.getName();
//			db.FK_Dept = WebUser.getFK_Dept();
//			//设置路径.
//			db.FileFullName = ath.SaveTo + db.RefPKVal + "/" + db.FileName;
//			db.Insert();
//		}
//		return "";
//	}

	public final String AthSingle_TemplateData() throws Exception {
		//获取表单数据
		String pkVal = DataType.IsNullOrEmpty(this.getPKVal()) == true ? null :this.getPKVal();
		if (this.getFK_Node() == 0)
		{
			return "err@没有获取到FK_Node的值";
		}
		if (pkVal == null)
		{
			return "err@没有获取到OID的值";
		}
		Node nd = new Node(this.getFK_Node());
		Work wk = nd.getHisWork();
		wk.setOID(Long.parseLong(pkVal));
		wk.RetrieveFromDBSources();
		Attrs attrs = wk.getEnMap().getAttrs();
		DataTable dt = new DataTable();
		dt.Columns.Add("name");
		dt.Columns.Add("text");
		dt.Columns.Add("type");
		DataRow dr = null;
		for (Attr attr : attrs.ToJavaList())
		{
			dr = dt.NewRow();
			dr.setValue("name", attr.getKey());
			dr.setValue("text", wk.GetValByKey(attr.getKey()));
			dr.setValue("type", "text");
			dt.Rows.add(dr);
		}
		return Json.ToJson(dt);

	}



	public final String DearFileName(String fileName)
	{
		fileName = DearFileNameExt(fileName, "+", "%2B");
		fileName = DearFileNameExt(fileName, " ", "%20");
		fileName = DearFileNameExt(fileName, "/", "%2F");
		fileName = DearFileNameExt(fileName, "?", "%3F");
		fileName = DearFileNameExt(fileName, "%", "%25");
		fileName = DearFileNameExt(fileName, "#", "%23");
		fileName = DearFileNameExt(fileName, "&", "%26");
		fileName = DearFileNameExt(fileName, "=", "%3D");
		return fileName;
	}

	private void CreateDocFile(String filePath)
	{
		//Spire.Doc.Document doc = new Spire.Doc.Document();

		//doc.SaveToFile(filePath, Spire.Doc.FileFormat.Docx2013);
	}
	public final String DearFileNameExt(String fileName, String val, String replVal)
	{
		fileName = fileName.replace(val, replVal);
		fileName = fileName.replace(val, replVal);
		fileName = fileName.replace(val, replVal);
		fileName = fileName.replace(val, replVal);
		fileName = fileName.replace(val, replVal);
		fileName = fileName.replace(val, replVal);
		fileName = fileName.replace(val, replVal);
		fileName = fileName.replace(val, replVal);
		return fileName;
	}

		///#endregion 附件组件



		///#region 必须传递参数
	/** 
	 执行的内容
	*/
	public final String getDoWhat() throws Exception {
		String str = this.GetRequestVal("DoWhat");
		if (DataType.IsNullOrEmpty(str))
		{
			return "Frm";
		}
		return str;
	}
	/** 
	 当前的用户
	*/
	public final String getUserNo()  {
		return this.GetRequestVal("UserNo");
	}
	/** 
	 用户的安全校验码(请参考集成章节)
	*/
	public final String getSID() throws Exception {
		return this.GetRequestVal("Token");
	}
	public final String getAppPath() throws Exception {
		return Glo.getCCFlowAppPath();
	}

	public final String Port_Init() throws Exception {

			///#region 安全性校验.
		if (this.getUserNo() == null || this.getSID() == null || this.getDoWhat() == null || this.getFrmID() == null)
		{
			return "err@必要的参数没有传入，请参考接口规则。";
		}

		if (Dev2Interface.Port_CheckUserLogin(this.getUserNo(), this.getSID()) == false)
		{
			return "err@非法的访问，请与管理员联系。SID=" + this.getSID();
		}

		if (!this.getUserNo().equals(WebUser.getNo()))
		{
			Dev2Interface.Port_SigOut();
			Dev2Interface.Port_Login(this.getUserNo());
		}


			///#endregion 安全性校验.


			///#region 生成参数串.
		String paras = "";
		for (String str : CommonUtils.getRequest().getParameterMap().keySet())
		{
			String val = this.GetRequestVal(str);
			if (val.indexOf('@') != -1)
			{
				throw new RuntimeException("您没有能参数: [ " + str + " ," + val + " ] 给值 ，URL 将不能被执行。");
			}
			switch (str.toLowerCase())
			{
				case "fk_mapdata":
				case "workid":
				case "fk_node":
				case "Token":
					break;
				default:
					paras += "&" + str + "=" + val;
					break;
			}
		}

			///#endregion 生成参数串.

		String url = "";
		switch (this.getDoWhat())
		{
			case "Frm": //如果是调用Frm的查看界面.
				url = "Frm.htm?FK_MapData=" + this.getFrmID() + "&OID=" + this.getOID() + paras;
				break;
			case "Search": //调用查询界面.
				url = "../Comm/Search.htm?EnsName=" + this.getFrmID() + paras;
				break;
			case "Group": //分组分析界面.
				url = "../Comm/Group.htm?EnsName=" + this.getFrmID() + paras;
				break;
			default:
				break;
		}
		return "url@" + url;
	}

		///#endregion



		///#region 前台SQL转移处理
	public final String RunSQL_Init() throws Exception {
		String sql = GetRequestVal("SQL");
		DBAccess.RunSQLReturnTable(sql);
		DataTable dt = new DataTable();
		return Json.ToJson(dt);
	}

		///#endregion



		///#region 表单数据版本比对

	public final String FrmDB_DoCompare() throws Exception {
		FrmDBVer ver = new FrmDBVer(this.getMyPK());
		String mainData = DBAccess.GetBigTextFromDB("Sys_FrmDBVer", "MyPK", this.getMyPK(), "FrmDB");
		DataTable dt = Json.ToDataTable(mainData);
		dt.TableName = "mainData";
		MapDtls dtls = new MapDtls(ver.getFrmID());
		MapData md = new MapData(ver.getFrmID());
		DataSet ds = new DataSet();
		ds.Tables.add(dt);
		ds.Tables.add(ver.ToDataTableField("Sys_FrmDBVer"));
		ds.Tables.add(dtls.ToDataTableField("Sys_MapDtl"));
		ds.Tables.add(md.ToDataTableField("Sys_MapData"));
		return Json.ToJson(ds);
	}

	public final String FrmDBVerAndRemark_Init() throws Exception {
		DataSet ds = new DataSet();
		String field = GetRequestVal("Field");
		int fieldType = GetRequestValInt("FieldType"); // 0 表单字段 1从表
		String frmID = this.getFrmID();
		String rfrmID = this.GetRequestVal("RFrmID");
		//文本字段
		if (fieldType == 0)
		{
			FrmDBRemarks remarks = new FrmDBRemarks();
			remarks.Retrieve(FrmDBRemarkAttr.FrmID, rfrmID, FrmDBRemarkAttr.Field, field, FrmDBRemarkAttr.RefPKVal, this.getRefPKVal(), FrmDBRemarkAttr.RDT);
			ds.Tables.add(remarks.ToDataTableField("Sys_FrmDBRemark"));
			FrmDBVers vers = new FrmDBVers();
			vers.Retrieve(FrmDBVerAttr.FrmID, rfrmID, FrmDBVerAttr.RefPKVal, this.getRefPKVal(), FrmDBVerAttr.RDT);

			for (FrmDBVer ver : vers.ToJavaList())
			{
				String json = DBAccess.GetBigTextFromDB("Sys_FrmDBVer", "MyPK", ver.getMyPK(), "FrmDB");
				if (DataType.IsNullOrEmpty(json) == true)
				{
					ver.setTrackID("");
					continue;
				}

				DataTable dt = Json.ToDataTable(json);
				ver.setTrackID(dt.Rows.get(0).getValue(field).toString());
			}

			ds.Tables.add(vers.ToDataTableField("Sys_FrmDBVer"));
			return Json.ToJson(ds);
		}

		return FrmDB_DtlCompare(field, rfrmID);
		//return BP.Tools.Json.ToJson(ds);
	}

	/** 
	 从表数据的比对
	 
	 @return 
	*/
	public final String FrmDB_DtlCompare(String mapDtl, String rfrmID) throws Exception {
		DataSet myds = new DataSet();

			///#region 加载从表表单模版信息.
		MapDtl dtl = new MapDtl(mapDtl);
		DataTable Sys_MapDtl = dtl.ToDataTableField("Sys_MapDtl");
		myds.Tables.add(Sys_MapDtl);

		//明细表的表单描述
		MapAttrs attrs = dtl.getMapAttrs();
		DataTable Sys_MapAttr = attrs.ToDataTableField("Sys_MapAttr");
		myds.Tables.add(Sys_MapAttr);

		//明细表的配置信息.
		DataTable Sys_MapExt = dtl.getMapExts().ToDataTableField("Sys_MapExt");
		myds.Tables.add(Sys_MapExt);

		//启用附件，增加附件信息
		DataTable Sys_FrmAttachment = dtl.getFrmAttachments().ToDataTableField("Sys_FrmAttachment");
		myds.Tables.add(Sys_FrmAttachment);

			///#endregion 加载从表表单模版信息.


			///#region  把从表的数据放入.
		FrmDBVers vers = new FrmDBVers();
		vers.Retrieve(FrmDBVerAttr.FrmID, rfrmID, FrmDBVerAttr.RefPKVal, this.getRefPKVal(), FrmDBVerAttr.RDT);

		for (FrmDBVer ver : vers.ToJavaList())
		{
			String json = DBAccess.GetBigTextFromDB("Sys_FrmDBVer", "MyPK", ver.getMyPK(), "FrmDtlDB");
			ver.setTrackID(json);
		}
		myds.Tables.add(vers.ToDataTableField("Sys_FrmDBVer"));


			///#endregion  把从表的数据放入.
		return Json.ToJson(myds);
	}

		///#endregion 表单数据版本比对

	/***
	 * 章节表单
	 * @return
	 */
	public String ChapterFrm_Init() throws Exception {

		GEEntity en = new GEEntity(this.getFrmID());
		en.setOID(this.getOID());
		if (en.RetrieveFromDBSources() == 0)
			en.InsertAsOID(this.getOID());

		GroupFields gfs = new GroupFields();
		gfs.Retrieve(GroupFieldAttr.FrmID, this.getFrmID(), "Idx");

		MapAttrs attrs = new MapAttrs();
		attrs.Retrieve(MapAttrAttr.FK_MapData, this.getFrmID(), MapAttrAttr.MyDataType, 1, MapAttrAttr.UIVisible, 1, "Idx");

         //获得数据，是否打勾？
		//获得已经有数据的字段.
		String ptable = en.getEnMap().getPhysicsTable();
		String atparas = DBAccess.RunSQLReturnString("SELECT AtPara FROM " + ptable + " WHERE OID=" + String.valueOf(this.getOID()));
		AtPara ap = new AtPara(atparas);
		String fileds = ap.GetValStrByKey("ChapterFrmSaveFields");
		if (DataType.IsNullOrEmpty(fileds) == false)
		{
			//增加星号标记.
			for(MapAttr attr : attrs.ToJavaList())
			{
				if (fileds.contains("," + attr.getKeyOfEn() + ",") == true)
				{
					attr.SetPara("IsStar", "1");
				}
			}

			//为分组字段设置 IsStar 标记.  标记该分组下，是否所有的字段都已经填写完毕?
			for(GroupField gf : gfs.ToJavaList())
			{

				//判断是否是从表? 取从表的数量.
				if (gf.getCtrlType().equals("Dtl") == true)
				{
					GEEntity geen = new GEEntity(gf.getCtrlID());
					String sql = "SELECT count(*) as num FROM " + geen.getEnMap().getPhysicsTable() + " WHERE refpk='" + this.getWorkID() + "'";
					if (DBAccess.RunSQLReturnValInt(sql) > 0)
					{
						gf.SetPara("IsStar", "1");
						continue;
					}
				}

				//是否是附件？
				if (gf.getCtrlType().equals("Ath") == true)
				{
					String sql = "SELECT COUNT(*) AS NUM FROM Sys_FrmattachmentDB WHERE RefPKVal=" + this.getWorkID() + " ";
					if (DBAccess.RunSQLReturnValInt(sql) > 0)
					{
						gf.SetPara("IsStar", "1");
						continue;
					}
				}

				//表单链接, 检查是否有空白项？
				if (gf.getCtrlType().equals("ChapterFrmLinkFrm") == true)
				{
					GEEntity ge = new GEEntity(gf.getCtrlID());
					ge.setOID(this.getWorkID());
					if (ge.RetrieveFromDBSources() == 1)
					{
						int blankNum = 0;
						MapAttrs attr1s = new MapAttrs(gf.getCtrlID());
						for(MapAttr item : attr1s.ToJavaList())
						{
							if (item.getUIIsInput() == true)
							{
								String val = ge.GetValStrByKey(item.getKeyOfEn());
								if (DataType.IsNullOrEmpty(val) == true)
								{
									blankNum++;
								}
							}
						}
						if (blankNum == 0)
						{
							gf.SetPara("IsStar", "1");
							continue;
						}
					}
				}



				//判断字段.
				if (DataType.IsNullOrEmpty(gf.getCtrlType()) == false)
					continue;

				//是否有未填写的字段？
				boolean isHaveBlank = false;
				for(MapAttr attr : attrs.ToJavaList())
				{
					if (attr.getGroupID() != gf.getOID())
						continue;
					if (attr.getMyDataType() != DataType.AppString || attr.getUIVisible() == false)
						continue;

					if (fileds.contains(attr.getKeyOfEn() + ",") == false)
					{
						isHaveBlank = true;
					}
				}
				if (isHaveBlank == false)
					gf.SetPara("IsStar", "1");
			}
		}
        //endregion 获得数据，是否打勾？

		//组装数据，返回出去.
		DataSet ds = new DataSet();
		ds.Tables.add(gfs.ToDataTableField("GroupFields"));
		ds.Tables.add(attrs.ToDataTableField("MapAttrs"));
		GenerWorkFlow gwf = new GenerWorkFlow(this.getOID());
		ds.Tables.add(gwf.ToDataTableField("WF_GenerWorkFlow"));
		return bp.tools.Json.ToJson(ds);
	}
	/// <summary>
	/// 检查目录是否可以加星.
	/// </summary>
	/// <returns></returns>
	public String ChapterFrm_CheckGroupFieldStar() throws Exception {
		GroupField gf = new GroupField(this.getGroupField());

		//判断是否是从表? 取从表的数量.
		if (gf.getCtrlType().equals("Dtl") == true)
		{
			GEEntity geen = new GEEntity(gf.getCtrlID());
			String sql = "SELECT count(*) as num FROM " + geen.getEnMap().getPhysicsTable() + " WHERE refpk='" + this.getWorkID() + "'";
			if (DBAccess.RunSQLReturnValInt(sql) > 0)
				return "1";
			return "0";
		}

		//是否是附件？
		if (gf.getCtrlType().equals("Ath") == true)
		{
			String sql = "SELECT COUNT(*) AS NUM FROM Sys_FrmattachmentDB WHERE RefPKVal=" + this.getWorkID() + " ";
			if (DBAccess.RunSQLReturnValInt(sql) > 0)
				return "1";
			return "0";
		}

		//表单链接, 检查是否有空白项？
		if (gf.getCtrlType().equals("ChapterFrmLinkFrm") == true)
		{
			GEEntity ge = new GEEntity(gf.getCtrlID());
			ge.setOID(this.getWorkID());
			if (ge.RetrieveFromDBSources() == 1)
			{
				int blankNum = 0;
				MapAttrs attr1s = new MapAttrs(gf.getCtrlID());
				for(MapAttr item : attr1s.ToJavaList())
				{
					if (item.getUIIsInput() == true)
					{
						String val = ge.GetValStrByKey(item.getKeyOfEn());
						if (DataType.IsNullOrEmpty(val) == true)
							blankNum++;
					}
				}
				if (blankNum == 0)
					return "1";
			}
			return "0";
		}
		return "err@没有判断." + gf.ToJson();
	}


	public String ChapterFrm_InitOneField() throws Exception {
		if (bp.da.DataType.IsNullOrEmpty(this.getKeyOfEn()) == true)
			return "err@没有传来字段KeyOfEn的值.";

		String ptable = DBAccess.RunSQLReturnString("SELECT PTable FROM Sys_MapData WHERE No='" + this.getFrmID() + "'", null);
		return bp.da.DBAccess.GetBigTextFromDB(ptable, "OID", String.valueOf(this.getOID()), this.getKeyOfEn());
	}
	public String ChapterFrm_SaveOneField() throws Exception {
		String ptable = DBAccess.RunSQLReturnString("SELECT PTable FROM Sys_MapData WHERE No='" + this.getFrmID() + "'", null);
		String vals = this.getVals();
		if (vals == null)
			vals = "";

		try
		{
			bp.da.DBAccess.SaveBigTextToDB(vals, ptable, "OID", String.valueOf(this.getOID()), this.getKeyOfEn());

			String atparas = DBAccess.RunSQLReturnStringIsNull("SELECT AtPara FROM " + ptable + " WHERE OID=" + String.valueOf(this.getOID()), "");

			//标记该字段已经完成.
			if (atparas.contains("," + this.getKeyOfEn() + ",") == false)
			{
				AtPara ap = new AtPara(atparas);
				String val = ap.GetValStrByKey("ChapterFrmSaveFields");

				if (vals.length() == 0)
					val = val.replace("," + this.getKeyOfEn() + ",", "");
				else
					val += "," + this.getKeyOfEn() + ",";

				ap.SetVal("ChapterFrmSaveFields", val);

				String strs = ap.GenerAtParaStrs();
				int i = DBAccess.RunSQL("UPDATE " + ptable + " SET AtPara='" + strs + "' WHERE OID=" + this.getOID());
				if (i == 0)
					return "err@保存章节字段出现错误，行数据OID=" + this.getOID() + ",不存在.";

				if (this.getVals().length() == 0)
					return "0"; //去掉star.
				return "1";
			}
			else
			{
				//如果存在，并且值为null. qudiao.
				if (vals == "")
				{
					atparas = atparas.replace("," + this.getKeyOfEn() + ",", "");
					int i = DBAccess.RunSQL("UPDATE " + ptable + " SET AtPara='" + atparas + "' WHERE OID=" + this.getOID());
					if (i == 0)
						return "err@保存章节字段出现错误，行数据OID=" + this.getOID() + ",不存在.";
					return "0";
				}
			}
			return "1"; // 增加star.
		}
		catch (Exception ex)
		{
			if (ex.getMessage().contains("ta too long for column") == true || ex.getMessage().contains("太长") == true)
			{
				bp.da.DBAccess.DropTableColumn(this.getFrmID(), this.getKeyOfEn());
				bp.da.DBAccess.SaveBigTextToDB(this.getVals(), this.getFrmID(), "OID", String.valueOf(this.getOID()), this.getKeyOfEn());
				return "保存成功.";
			}
			return ex.getMessage();
		}
	}
	/// <summary>
	/// 根据版本号获取表单的历史数据
	/// </summary>
	/// <returns></returns>
	public String ChartFrm_GetBigTextByVer() throws Exception {
		String dbstr = SystemConfig.getAppCenterDBVarStr();
		String sql = "SELECT MyPK FROM Sys_FrmDBVer WHERE FrmID=" + dbstr + "FrmID AND RefPKVal=" + dbstr + "RefPKVal AND Ver=" + dbstr + "Ver";
		Paras ps = new Paras();
		ps.SQL = sql;
		ps.Add("FrmID", this.getFrmID());
		ps.Add("RefPKVal", this.getOID());
		ps.Add("Ver", GetRequestVal("DBVer"));
		String mypk = DBAccess.RunSQLReturnStringIsNull(ps, "");
		if (DataType.IsNullOrEmpty(mypk) == true)
			return "err@获取表单数据版本失败";
		return DBAccess.GetBigTextFromDB("Sys_FrmDBVer", "MyPK", mypk, "FrmDB");
	}

	public String ChartFrm_GetDtlDataByVer() throws Exception {
		return DBAccess.GetBigTextFromDB("Sys_FrmDBVer", "MyPK", this.getMyPK(), "FrmDtlDB");
	}
    //#endregion 章节表单.

}