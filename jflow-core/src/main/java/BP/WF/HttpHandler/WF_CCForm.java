package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Difference.ContextHolderUtils;
import BP.Difference.Handler.CommonFileUtils;
import BP.Difference.Handler.WebContralBase;
import BP.Sys.*;
import BP.Tools.*;
import BP.Web.*;
import BP.WF.Template.*;
import BP.WF.XML.*;
import BP.En.*;
import BP.WF.*;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import org.apache.axis.encoding.Base64;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLDecoder;
import java.nio.channels.FileChannel;

/**
 * 表单功能界面
 */
public class WF_CCForm extends WebContralBase {

	/// #region 多附件.
	public final String Ath_Init() throws Exception {
		try {
			DataSet ds = new DataSet();

			FrmAttachment athDesc = this.GenerAthDesc();

			// 查询出来数据实体.
			String pkVal = this.getPKVal();
			if (athDesc.getHisCtrlWay() == AthCtrlWay.FID) {
				pkVal = String.valueOf(this.getFID());
			}

			BP.Sys.FrmAttachmentDBs dbs = BP.WF.Glo.GenerFrmAttachmentDBs(athDesc, pkVal, this.getFK_FrmAttachment(),
					this.getWorkID(), this.getFID(), this.getPWorkID());

			/// #region 如果图片显示.(先不考虑.)
			if (athDesc.getFileShowWay() == FileShowWay.Pict) {
				/* 如果是图片轮播，就在这里根据数据输出轮播的html代码. */
				if (dbs.size() == 0 && athDesc.getIsUpload() == true) {
					/* 没有数据并且，可以上传,就转到上传的界面上去. */
					return "url@AthImg.htm?1=1" + this.getRequestParas();
				}
			}

			/// #endregion 如果图片显示.

			/// #region 执行装载模版.
			if (dbs.size() == 0 && athDesc.getIsWoEnableTemplete() == true) {
				/* 如果数量为0,就检查一下是否有模版如果有就加载模版文件. */
				String templetePath = BP.Sys.SystemConfig.getPathOfDataUser() + "AthTemplete\\"
						+ athDesc.getNoOfObj().trim();
				if ((new File(templetePath)).isDirectory() == false) {
					(new File(templetePath)).mkdirs();
				}

				/* 有模版文件夹 */
				File mydir = new File(templetePath);
				File[] fls = mydir.listFiles();
				if (fls.length == 0) {
					throw new RuntimeException("@流程设计错误，该多附件启用了模版组件，模版目录:" + templetePath + "里没有模版文件.");
				}

				for (File fl : fls) {
					if ((new File(athDesc.getSaveTo())).isDirectory() == false) {
						(new File(athDesc.getSaveTo())).mkdirs();
					}

					int oid = BP.DA.DBAccess.GenerOID();
					String saveTo = athDesc.getSaveTo() + "\\" + oid + "."
							+ fl.getName().substring(fl.getName().lastIndexOf('.') + 1);
					if (saveTo.contains("@") == true || saveTo.contains("*") == true) {
						/* 如果有变量 */
						saveTo = saveTo.replace("*", "@");
						if (saveTo.contains("@") && this.getFK_Node() != 0) {
							/* 如果包含 @ */
							BP.WF.Flow flow = new BP.WF.Flow(this.getFK_Flow());
							BP.WF.Data.GERpt myen = flow.getHisGERpt();
							myen.setOID(this.getWorkID());
							myen.RetrieveFromDBSources();
							saveTo = BP.WF.Glo.DealExp(saveTo, myen, null);
						}
						if (saveTo.contains("@") == true) {
							throw new RuntimeException("@路径配置错误,变量没有被正确的替换下来." + saveTo);
						}
					}

					try {
						InputStream is = new FileInputStream(saveTo);
						int buffer = 1024; // 定义缓冲区的大小
						int length = 0;
						byte[] b = new byte[buffer];
						FileOutputStream fos = new FileOutputStream(fl);
						while ((length = is.read(b)) != -1) {
							fos.write(b, 0, length); // 向文件输出流写读取的数据

						}
						fos.close();
					} catch (RuntimeException ex) {
						ex.getMessage();
					}

					File info = new File(saveTo);
					FrmAttachmentDB dbUpload = new FrmAttachmentDB();

					dbUpload.CheckPhysicsTable();
					dbUpload.setMyPK(athDesc.getFK_MapData() + String.valueOf(oid));
					dbUpload.setNodeID(String.valueOf(getFK_Node()));
					dbUpload.setFK_FrmAttachment(this.getFK_FrmAttachment());

					if (athDesc.getAthUploadWay() == AthUploadWay.Inherit) {
						/* 如果是继承，就让他保持本地的PK. */
						dbUpload.setRefPKVal(this.getPKVal().toString());
					}

					if (athDesc.getAthUploadWay() == AthUploadWay.Interwork) {
						/* 如果是协同，就让他是PWorkID. */
						Paras ps = new Paras();
						ps.SQL = "SELECT PWorkID FROM WF_GenerWorkFlow WHERE WorkID="
								+ SystemConfig.getAppCenterDBVarStr() + "WorkID";
						ps.Add("WorkID", this.getPKVal());
						String pWorkID = String.valueOf(DBAccess.RunSQLReturnValInt(ps, 0));
						if (pWorkID == null || pWorkID.equals("0")) {

							pWorkID = this.getPKVal();
						}
						dbUpload.setRefPKVal(pWorkID);
					}

					dbUpload.setFK_MapData(athDesc.getFK_MapData());
					dbUpload.setFK_FrmAttachment(this.getFK_FrmAttachment());

					dbUpload.setFileExts(info.getName().substring(info.getName().lastIndexOf(".") + 1));
					dbUpload.setFileFullName(saveTo);
					dbUpload.setFileName(fl.getName());
					dbUpload.setFileSize((float) info.length());

					dbUpload.setRDT(DataType.getCurrentDataTime());
					dbUpload.setRec(WebUser.getNo());
					dbUpload.setRecName(WebUser.getName());

					dbUpload.Insert();

					dbs.AddEntity(dbUpload);
				}
			}

			// 处理权限问题, 有可能当前节点是可以上传或者删除，但是当前节点上不能让此人执行工作。
			boolean isDel = athDesc.getHisDeleteWay() == AthDeleteWay.None ? false : true;
			boolean isUpdate = athDesc.getIsUpload();
			athDesc.setIsUpload(isUpdate);

			String sort = athDesc.getSort().trim();
			if (sort.contains("SELECT") == true || sort.contains("select") == true) {
				String sql = BP.WF.Glo.DealExp(sort, null, null);
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
			return BP.Tools.Json.ToJson(ds);
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}

	/**
	 * 扩展处理.
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String HandlerMapExt() throws Exception {
		String fk_mapExt = this.GetRequestVal("FK_MapExt").toString();
		if (DataType.IsNullOrEmpty(this.GetRequestVal("Key"))) {
			return "";
		}

		String oid = this.GetRequestVal("OID");

		String kvs = this.GetRequestVal("KVs");

		BP.Sys.MapExt me = new BP.Sys.MapExt(fk_mapExt);
		DataTable dt = null;
		String sql = "";
		String key = this.GetRequestVal("Key");
		key = URLDecoder.decode(key, "GB2312");
		key = key.trim();
		key = key.replace("'", ""); // 去掉单引号.

		// key = "周";
		switch (me.getExtType()) {
		case BP.Sys.MapExtXmlList.ActiveDDL: // 动态填充ddl.
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
		case BP.Sys.MapExtXmlList.AutoFullDLL: // 填充下拉框
		case BP.Sys.MapExtXmlList.TBFullCtrl: // 自动完成。
		case BP.Sys.MapExtXmlList.DDLFullCtrl: // 级连ddl.
		case BP.Sys.MapExtXmlList.FullData: // 填充其他控件.
			switch (this.GetRequestVal("DoTypeExt")) {
			case "ReqCtrl":
				// 获取填充 ctrl 值的信息.
				sql = this.DealSQL(me.getDocOfSQLDeal(), key);
				WebUser.SetSessionByKey("DtlKey", key);
				dt = BP.DA.DBAccess.RunSQLReturnTable(sql);

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

					DataTable dtDtlFull = DBAccess.RunSQLReturnTable(mysql);
					BP.DA.DBAccess.RunSQL("DELETE FROM " + dtl.getPTable() + " WHERE RefPK=" + oid);
					for (DataRow dr : dtDtlFull.Rows) {
						BP.Sys.GEDtl mydtl = new GEDtl(fk_dtl);
						dtls.AddEntity(mydtl);
						for (DataColumn dc : dtDtlFull.Columns) {
							mydtl.SetValByKey(dc.ColumnName, dr.get(dc.ColumnName).toString());
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
					if (str == null || str.equals("")){
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
			default:
				key = key.replace("'", "");

				sql = this.DealSQL(me.getDocOfSQLDeal(), key);

				dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
				return JSONTODT(dt);
			}
		default:
			return "err@没有解析的标记" + me.getExtType();
		}

	}

	/**
	 * 处理sql.
	 * 
	 * @param sql
	 *            要执行的sql
	 * @param key
	 *            关键字值
	 * @return 执行sql返回的json
	 * @throws Exception
	 */
	private String DealSQL(String sql, String key) throws Exception {
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

		sql = sql.replace("@WebUser.getNo()", WebUser.getNo());
		sql = sql.replace("@WebUser.getName()", WebUser.getName());
		sql = sql.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());

		String oid = this.GetRequestVal("OID");
		if (oid != null) {
			sql = sql.replace("@OID", oid);
		}

		String kvs = this.GetRequestVal("KVs");

		if (DataType.IsNullOrEmpty(kvs) == false && sql.contains("@") == true) {
			String[] strs = kvs.split("[~]", -1);
			for (String s : strs) {
				if (DataType.IsNullOrEmpty(s) || s.contains("=") == false) {
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

	/**
	 * 构造函数
	 */
	public WF_CCForm() {
	}

	/**
	 * 默认执行的方法
	 * 
	 * @return
	 */
	@Override
	protected String DoDefaultMethod() {
		// 找不不到标记就抛出异常.
		return "err@没有此方法 " + this.toString() + " - " + this.getDoType();
		// throw new RuntimeException("@标记[" + this.getDoType() +
		// "]，没有找到.@原始URL:" + CommonUtils.getRequest().getRequestURI());
	}

	/// #endregion 执行父类的重写方法.

	/// #region frm.htm 主表.
	/**
	 * 执行数据初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String Frm_Init() throws Exception {
		boolean IsMobile = GetRequestValBoolen("IsMobile");
		if (this.GetRequestVal("IsTest") != null) {
			MapData mymd = new MapData(this.getEnsName());
			mymd.RepairMap();
			BP.Sys.SystemConfig.DoClearCash();
		}

		MapData md = new MapData(this.getEnsName());

		/// #region 判断是否是返回的URL.
		if (md.getHisFrmType() == FrmType.Url) {
			String no = this.GetRequestVal("NO");
			String urlParas = "OID=" + this.getRefOID() + "&NO=" + no + "&WorkID=" + this.getWorkID() + "&FK_Node="
					+ this.getFK_Node() + "&UserNo=" + WebUser.getNo() + "&SID=" + this.getSID();

			String url = "";
			/* 如果是URL. */
			if (md.getUrl().contains("?") == true) {
				url = md.getUrl() + "&" + urlParas;
			} else {
				url = md.getUrl() + "?" + urlParas;
			}

			return "url@" + url;
		}

		if (md.getHisFrmType() == FrmType.Entity) {
			String no = this.GetRequestVal("NO");
			String urlParas = "OID=" + this.getRefOID() + "&NO=" + no + "&WorkID=" + this.getWorkID() + "&FK_Node="
					+ this.getFK_Node() + "&UserNo=" + WebUser.getNo() + "&SID=" + this.getSID();

			BP.En.Entities ens = BP.En.ClassFactory.GetEns(md.getPTable());

			BP.En.Entity en = ens.getNewEntity();

			if (en.getIsOIDEntity() == true) {
				BP.En.EntityOID enOID = null;

				enOID = en instanceof BP.En.EntityOID ? (BP.En.EntityOID) en : null;
				if (enOID == null) {
					return "err@系统错误，无法将" + md.getPTable() + "转化成BP.En.EntityOID.";
				}

				enOID.SetValByKey("OID", this.getWorkID());

				if (en.RetrieveFromDBSources() == 0) {
					while (getRequest().getParameterNames().hasMoreElements()) {
						String key = (String) BP.Sys.Glo.getRequest().getParameterNames().nextElement();
						String val = BP.Sys.Glo.getRequest().getParameter(key);
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

		if (md.getHisFrmType() == FrmType.WordFrm) {
			String no = this.GetRequestVal("NO");
			String urlParas = "OID=" + this.getRefOID() + "&NO=" + no + "&WorkID=" + this.getWorkID() + "&FK_Node="
					+ this.getFK_Node() + "&UserNo=" + WebUser.getNo() + "&SID=" + this.getSID() + "&FK_MapData="
					+ this.getFK_MapData() + "&OIDPKVal=" + this.getOID() + "&FID=" + this.getFID() + "&FK_Flow="
					+ this.getFK_Flow();
			/* 如果是URL. */
			String requestParas = this.getRequestParasOfAll();
			String[] parasArrary = this.getRequestParasOfAll().split("[&]", -1);
			for (String str : parasArrary) {
				if (DataType.IsNullOrEmpty(str) || str.contains("=") == false) {
					continue;
				}
				String[] kvs = str.split("[=]", -1);
				if (urlParas.contains(kvs[0])) {
					continue;
				}
				urlParas += "&" + kvs[0] + "=" + kvs[1];
			}
			if (md.getUrl().contains("?") == true) {
				return "url@FrmWord.aspx?1=2" + "&" + urlParas;
			} else {
				return "url@FrmWord.aspx" + "?" + urlParas;
			}
		}

		if (md.getHisFrmType() == FrmType.ExcelFrm) {
			return "url@FrmExcel.aspx?1=2" + this.getRequestParasOfAll();
		}

		/// #endregion 判断是否是返回的URL.

		// 处理参数.
		String paras = this.getRequestParasOfAll();
		paras = paras.replace("&DoType=Frm_Init", "");

		/// #region 流程的独立运行的表单.
		if (this.getFK_Node() != 0 && this.getFK_Node() != 999999) {
			BP.WF.Template.FrmNode fn = new FrmNode();
			fn = new FrmNode(this.getFK_Flow(), this.getFK_Node(), this.getFK_MapData());

			if (fn != null && fn.getWhoIsPK() != WhoIsPK.OID) {
				// 太爷孙关系
				if (fn.getWhoIsPK() == WhoIsPK.PPPWorkID) {
					// 根据PWorkID 获取PPPWorkID
					String sql = "Select PWorkID From WF_GenerWorkFlow Where WorkID=(Select PWorkID From WF_GenerWorkFlow Where WorkID="
							+ this.getPWorkID() + ")";
					String pppworkID = DBAccess.RunSQLReturnString(sql);
					if (DataType.IsNullOrEmpty(pppworkID) == true || pppworkID.equals("0")) {
						throw new RuntimeException("err@不存在太爷孙流程关系，请联系管理员检查流程设计是否正确");
					}

					long PPPWorkID = Long.parseLong(pppworkID);
					paras = paras.replace("&OID=" + this.getWorkID(), "&OID=" + PPPWorkID);
					paras = paras.replace("&WorkID=" + this.getWorkID(), "&WorkID=" + PPPWorkID);
					paras = paras.replace("&PKVal=" + this.getWorkID(), "&PKVal=" + PPPWorkID);
				}

				if (fn.getWhoIsPK() == WhoIsPK.PPWorkID) {
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

				if (md.getHisFrmType() == FrmType.FreeFrm || md.getHisFrmType() == FrmType.FoolForm) {
					if (IsMobile == true) {
						return "url@../FrmView.htm?1=2" + paras;
					}

					if (this.GetRequestVal("Readonly").equals("1") || this.GetRequestVal("IsEdit").equals("0")) {
						return "url@FrmGener.htm?1=2" + paras;
					} else {
						return "url@FrmGener.htm?1=2" + paras;
					}
				}

				if (md.getHisFrmType() == FrmType.VSTOForExcel || md.getHisFrmType() == FrmType.ExcelFrm) {
					if (this.GetRequestVal("Readonly").equals("1") || this.GetRequestVal("IsEdit").equals("0")) {
						return "url@FrmVSTO.htm?1=2" + paras;
					} else {
						return "url@FrmVSTO.htm?1=2" + paras;
					}
				}

				if (IsMobile == true) {
					return "url@../FrmView.htm?1=2" + paras;
				}

				if (this.GetRequestVal("Readonly").equals("1") || this.GetRequestVal("IsEdit").equals("0")) {
					return "url@FrmGener.htm?1=2" + paras;
				} else {
					return "url@FrmGener.htm?1=2" + paras;
				}
			}
		}

		/// #endregion 非流程的独立运行的表单.

		/// #region 非流程的独立运行的表单.

		if (md.getHisFrmType() == FrmType.FreeFrm) {
			if (IsMobile == true) {
				return "url@../FrmView.htm?1=2" + paras;
			}
			if (this.GetRequestVal("Readonly").equals("1") || this.GetRequestVal("IsEdit").equals("0")) {
				return "url@FrmGener.htm?1=2" + paras;
			} else {
				return "url@FrmGener.htm?1=2" + paras;
			}
		}

		if (md.getHisFrmType() == FrmType.VSTOForExcel || md.getHisFrmType() == FrmType.ExcelFrm) {
			if (this.GetRequestVal("Readonly").equals("1") || this.GetRequestVal("IsEdit").equals("0")) {
				return "url@FrmVSTO.htm?1=2" + paras;
			} else {
				return "url@FrmVSTO.htm?1=2" + paras;
			}
		}

		if (IsMobile == true) {
			return "url@../FrmView.htm?1=2" + paras;
		}

		if (this.GetRequestVal("Readonly").equals("1") || this.GetRequestVal("IsEdit").equals("0")) {
			return "url@FrmGener.htm?1=2" + paras;
		} else {
			return "url@FrmGener.htm?1=2" + paras;
		}

		/// #endregion 非流程的独立运行的表单.

	}

	/**
	 * 附件图片
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String FrmImgAthDB_Init() throws Exception {
		String ImgAthPK = this.GetRequestVal("ImgAth");

		FrmImgAthDBs imgAthDBs = new FrmImgAthDBs();
		QueryObject obj = new QueryObject(imgAthDBs);
		obj.AddWhere(FrmImgAthDBAttr.FK_MapData, this.getFK_MapData());
		obj.addAnd();
		obj.AddWhere(FrmImgAthDBAttr.FK_FrmImgAth, ImgAthPK);
		obj.addAnd();
		obj.AddWhere(FrmImgAthDBAttr.RefPKVal, this.getMyPK());
		obj.DoQuery();
		return BP.Tools.Entitis2Json.ConvertEntities2ListJson(imgAthDBs);
	}

	/**
	 * 上传编辑图片
	 * 
	 * @return
	 */
	public final String FrmImgAthDB_Upload() {
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
			return "{SourceImage:\"" + webPath + "\"}";
		}
		return "@err没有选择文件";
	}

	// 复制文件
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

	/**
	 * 剪切图片
	 * 
	 * @return
	 */
	public final String FrmImgAthDB_Cut() {
		String CtrlID = this.GetRequestVal("CtrlID");
		int zoomW = this.GetRequestValInt("ImgAth");
		int zoomH = this.GetRequestValInt("zoomH");
		int x = this.GetRequestValInt("cX");
		int y = this.GetRequestValInt("cY");
		int w = this.GetRequestValInt("cW");
		int h = this.GetRequestValInt("cH");

		String newName = "";
		String fk_mapData = this.getFK_MapData();
		if (fk_mapData.contains("ND") == true)
			newName = CtrlID + "_" + this.getRefPKVal();
		else
			newName = fk_mapData + "_" + CtrlID + "_" + this.getRefPKVal();
		// string newName = ImgAthPK + "_" + this.MyPK + "_" +
		// DateTime.Now.ToString("yyyyMMddHHmmss");
		String webPath = BP.WF.Glo.getCCFlowAppPath() + "DataUser/ImgAth/Data/" + newName + ".png";
		String savePath = SystemConfig.getPathOfDataUser() + "ImgAth/Data/" + newName + ".png";
		savePath = savePath.replace("\\", "/");
		// 获取上传的大图片
		String strImgPath = SystemConfig.getPathOfDataUser() + "ImgAth/Upload/" + newName + ".png";
		strImgPath = strImgPath.replace("\\", "/");

		File file = new File(strImgPath);
		if (file.exists()) {
			cutImage(strImgPath, savePath, x, y, w, h);
			return webPath;
		}
		return webPath;
	}

	// 剪裁图片元方法
	public static void cutImage(String filePath, String newFilePath, int x, int y, int w, int h) {
		// 加载图片到内存
		try {
			ImageInputStream iis = ImageIO.createImageInputStream(new FileInputStream(filePath));
			// 构造一个reader
			Iterator it = ImageIO.getImageReadersByFormatName("png");
			ImageReader reader = (ImageReader) it.next();
			// 绑定inputStream
			reader.setInput(iis);
			// 取得剪裁区域
			ImageReadParam param = reader.getDefaultReadParam();
			Rectangle rect = new Rectangle(x, y, w, h);
			param.setSourceRegion(rect);
			// 从reader得到bufferImage
			BufferedImage bi = reader.read(0, param);
			// 将bufferedImage写成,通过ImageIO
			ImageIO.write(bi, "png", new File(newFilePath));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/// #endregion frm.htm 主表.

	/// #region DtlFrm
	public final String DtlFrm_Init() throws Exception {
		long pk = this.getRefOID();
		if (pk == 0) {
			pk = this.getOID();
		}
		if (pk == 0) {
			pk = this.getWorkID();
		}

		if (pk != 0) {
			return FrmGener_Init();
		}

		MapDtl dtl = new MapDtl(this.getEnsName());

		GEEntity en = new GEEntity(this.getEnsName());
		if (BP.Sys.SystemConfig.getIsBSsystem() == true) {
			// 处理传递过来的参数。
			for (String k : BP.Sys.Glo.getQueryStringKeys()) {
				en.SetValByKey(k, this.GetRequestVal(k));
			}
		}

		// 设置主键.
		en.setOID(DBAccess.GenerOID(this.getEnsName()));

		/// #region 处理权限方案。
		if (this.getFK_Node() != 0 && this.getFK_Node() != 999999) {
			Node nd = new Node(this.getFK_Node());
			if (nd.getHisFormType() == NodeFormType.SheetTree || nd.getHisFormType() == NodeFormType.RefOneFrmTree) {
				FrmNode fn = new FrmNode(nd.getFK_Flow(), nd.getNodeID(), this.getFK_MapData());
				if (fn.getFrmSln() == FrmSln.Self) {
					String no = this.getEnsName() + "_" + nd.getNodeID();
					MapDtl mdtlSln = new MapDtl();
					mdtlSln.setNo(no);
					int result = mdtlSln.RetrieveFromDBSources();
					if (result != 0) {
						dtl = mdtlSln;
					}
				}
			}
		}

		/// #endregion 处理权限方案。
		// 给从表赋值.
		switch (dtl.getDtlOpenType()) {
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

		return "url@DtlFrm.htm?EnsName=" + this.getEnsName() + "&RefPKVal=" + this.getRefPKVal() + "&FrmType="
				+ dtl.getHisEditModel().getValue() + "&OID=" + en.getOID();
	}

	public final String DtlFrm_Delete() throws Exception {
		try {
			GEEntity en = new GEEntity(this.getEnsName());
			en.setOID(this.getOID());
			en.Delete();

			// 如果可以上传附件这删除相应的附件信息
			FrmAttachmentDBs dbs = new FrmAttachmentDBs();
			dbs.Delete(FrmAttachmentDBAttr.FK_MapData, this.getEnsName(), FrmAttachmentDBAttr.RefPKVal,
					this.getRefOID(), FrmAttachmentDBAttr.NodeID, this.getFK_Node());

			return "删除成功.";
		} catch (RuntimeException ex) {
			return "err@删除错误:" + ex.getMessage();
		}
	}

	/// #endregion DtlFrm

	/// #region frmFree
	/**
	 * 实体类的初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String FrmGener_Init_ForBPClass() throws Exception {
		try {

			MapData md = new MapData(this.getEnsName());
			DataSet ds = BP.Sys.CCFormAPI.GenerHisDataSet(md.getNo());

			/// #region 把主表数据放入.
			String atParas = "";
			Entities ens = ClassFactory.GetEns(this.getEnsName());
			Entity en = ens.getNewEntity();
			en.setPKVal(this.getPKVal());

			if (en.RetrieveFromDBSources() == 0) {
				en.Insert();
			}

			// 把参数放入到 En 的 Row 里面。
			if (DataType.IsNullOrEmpty(atParas) == false) {
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
				// 处理传递过来的参数。
				for (String k : BP.Sys.Glo.getQueryStringKeys()) {
					en.SetValByKey(k, this.GetRequestVal(k));
				}
			}

			// 执行表单事件. FrmLoadBefore .
			String msg = md.DoEvent(FrmEventList.FrmLoadBefore, en);
			if (DataType.IsNullOrEmpty(msg) == false) {
				return "err@错误:" + msg;
			}

			// 重设默认值.
			en.ResetDefaultVal();

			// 执行装载填充.
			MapExt me = new MapExt();
			if (me.Retrieve(MapExtAttr.ExtType, MapExtXmlList.PageLoadFull, MapExtAttr.FK_MapData,
					this.getEnsName()) == 1) {
				// 执行通用的装载方法.
				MapAttrs attrs = new MapAttrs(this.getEnsName());
				MapDtls dtls = new MapDtls(this.getEnsName());
				Entity tempVar = BP.WF.Glo.DealPageLoadFull(en, me, attrs, dtls);
				en = tempVar instanceof GEEntity ? (GEEntity) tempVar : null;
			}

			// 执行事件
			md.DoEvent(FrmEventList.SaveBefore, en, null);

			// 增加主表数据.
			DataTable mainTable = en.ToDataTableField(md.getNo());
			mainTable.TableName = "MainTable";

			ds.Tables.add(mainTable);

			/// #endregion 把主表数据放入.

			/// #region 把外键表加入DataSet
			DataTable dtMapAttr = ds.GetTableByName("Sys_MapAttr");

			MapExts mes = md.getMapExts();
			DataTable ddlTable = new DataTable();
			ddlTable.Columns.Add("No");
			for (DataRow dr : dtMapAttr.Rows) {
				String lgType = dr.get("LGType").toString();
				if (lgType.equals("2") == false) {
					continue;
				}

				String UIIsEnable = dr.get("UIVisible").toString();
				if (UIIsEnable.equals("0")) {
					continue;
				}

				String uiBindKey = dr.get("UIBindKey").toString();
				if (DataType.IsNullOrEmpty(uiBindKey) == true) {
					String myPK = dr.get("MyPK").toString();
			
				}

				// 检查是否有下拉框自动填充。
				String keyOfEn = dr.get("KeyOfEn").toString();
				String fk_mapData = dr.get("FK_MapData").toString();

				/// #region 处理下拉框数据范围. for 小杨.
				Object tempVar2 = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL,
						MapExtAttr.AttrOfOper, keyOfEn);
				me = tempVar2 instanceof MapExt ? (MapExt) tempVar2 : null;
				if (me != null) {
					Object tempVar3 = me.getDoc();
					String fullSQL = tempVar3 instanceof String ? (String) tempVar3 : null;
					fullSQL = fullSQL.replace("~", ",");
					fullSQL = BP.WF.Glo.DealExp(fullSQL, en, null);
					DataTable dt = DBAccess.RunSQLReturnTable(fullSQL);
					dt.TableName = keyOfEn; // 可能存在隐患，如果多个字段，绑定同一个表，就存在这样的问题.
					ds.Tables.add(dt);
					continue;
				}

				/// #endregion 处理下拉框数据范围.

				// 判断是否存在.
				if (ds.Tables.contains(uiBindKey) == true) {
					continue;
				}

				DataTable dataTable = BP.Sys.PubClass.GetDataTableByUIBineKey(uiBindKey);
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

			/// #endregion End把外键表加入DataSet

			return BP.Tools.Json.ToJson(ds);
		} catch (RuntimeException ex) {
			GEEntity myen = new GEEntity(this.getEnsName());
			myen.CheckPhysicsTable();

			BP.Sys.CCFormAPI.RepareCCForm(this.getEnsName());
			return "err@装载表单期间出现如下错误,ccform有自动诊断修复功能请在刷新一次，如果仍然存在请联系管理员. @" + ex.getMessage();
		}
	}

	/**
	 * 执行数据初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String FrmGener_Init() throws Exception {
		if (this.getFK_MapData() != null && this.getFK_MapData().contains("BP.") == true) {
			return FrmGener_Init_ForBPClass();
		}

		/// #region 定义流程信息的所用的 配置entity.
		// 节点与表单的权限控制.
		FrmNode fn = null;

		// 是否启用装载填充？ @袁丽娜,
		boolean isLoadData = true;
		// 定义节点变量. @袁丽娜
		Node nd = null;
		if (this.getFK_Node() != 0 && this.getFK_Node() != 999999) {
			nd = new Node(this.getFK_Node());
			nd.WorkID = this.getWorkID(); // 为获取表单ID ( NodeFrmID )提供参数.
			fn = new FrmNode(this.getFK_Flow(), this.getFK_Node(), this.getFK_MapData());
			isLoadData = fn.getIsEnableLoadData();
		}

		/// #endregion 定义流程信息的所用的 配置entity.

		try {

			/// #region 特殊判断 适应累加表单.
			String fromWhere = this.GetRequestVal("FromWorkOpt");
			if (fromWhere != null && fromWhere.equals("1") && this.getFK_Node() != 0 && this.getFK_Node() != 999999) {
				nd = new Node(this.getFK_Node());
				nd.WorkID = this.getWorkID(); // 为获取表单ID ( NodeFrmID )提供参数.

				// 如果是累加表单.
				if (nd.getHisFormType() == NodeFormType.FoolTruck) {
					DataSet myds = BP.WF.CCFlowAPI.GenerWorkNode(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(),
							this.getFID(), WebUser.getNo(), this.GetRequestVal("FromWorkOpt"));

					return BP.Tools.Json.ToJson(myds);
				}
			}

			/// #endregion 特殊判断.适应累加表单.

			MapData md = new MapData(this.getEnsName());
			DataSet ds = BP.Sys.CCFormAPI.GenerHisDataSet(md.getNo());
			String atParas = "";
			// 主表实体.
			GEEntity en = new GEEntity(this.getEnsName());

			long pk = this.getRefOID();
			if (pk == 0) {
				pk = this.getOID();
			}
			if (pk == 0) {
				pk = this.getWorkID();
			}

			/// #region 根据who is pk 获取数据.
			en.setOID(pk);
			if (en.getOID() == 0) {
				en.ResetDefaultVal();
			} else {
				if (en.RetrieveFromDBSources() == 0) {
					en.Insert();
				}
			}

			// 把流程信息表发送过去.
			GenerWorkFlow gwf = new GenerWorkFlow();
			gwf.setWorkID(pk);
			gwf.RetrieveFromDBSources();
			ds.Tables.add(gwf.ToDataTableField("WF_GenerWorkFlow"));

			/// #endregion 根据who is pk 获取数据.

			/// #region 附加参数数据.
			// 把参数放入到 En 的 Row 里面。
			if (DataType.IsNullOrEmpty(atParas) == false) {
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
				// 处理传递过来的参数。
				for (String k : BP.Sys.Glo.getQueryStringKeys()) {
					en.SetValByKey(k, this.GetRequestVal(k));
				}
			}

			// 执行表单事件. FrmLoadBefore .
			String msg = md.DoEvent(FrmEventList.FrmLoadBefore, en);
			if (DataType.IsNullOrEmpty(msg) == false) {
				return "err@错误:" + msg;
			}

			// 重设默认值.
			if (this.GetRequestValBoolen("IsReadonly") == false) {
				en.ResetDefaultVal();
			}

			/// #endregion 附加参数数据.

			/// #region 执行装载填充.与相关的事件.
			MapExt me = null;
			if (isLoadData == true) {
				me = new MapExt();
				if (me.Retrieve(MapExtAttr.ExtType, MapExtXmlList.PageLoadFull, MapExtAttr.FK_MapData,
						this.getEnsName()) == 1) {
					// 执行通用的装载方法.
					MapAttrs attrs = new MapAttrs(this.getEnsName());
					MapDtls dtls = new MapDtls(this.getEnsName());

					if (GetRequestValInt("IsTest") != 1) {
						// 判断是否自定义权限.
						boolean IsSelf = false;
						// 单据或者是单据实体表单
						if (nd == null) {
							Entity tempVar = BP.WF.Glo.DealPageLoadFull(en, me, attrs, dtls, IsSelf, 0,
									this.getWorkID());
							en = tempVar instanceof GEEntity ? (GEEntity) tempVar : null;
						} else {
							if ((nd.getHisFormType() == NodeFormType.SheetTree
									|| nd.getHisFormType() == NodeFormType.RefOneFrmTree)
									&& (fn.getFrmSln() == FrmSln.Self)) {
								IsSelf = true;
							}
							Entity tempVar2 = BP.WF.Glo.DealPageLoadFull(en, me, attrs, dtls, IsSelf, nd.getNodeID(),
									this.getWorkID());
							en = tempVar2 instanceof GEEntity ? (GEEntity) tempVar2 : null;
						}

					}

				}
			}

			// 执行事件
			md.DoEvent(FrmEventList.SaveBefore, en, null);

			/// #endregion 执行装载填充.与相关的事件.

			/// #region 把外键表加入DataSet.
			DataTable dtMapAttr = ds.GetTableByName("Sys_MapAttr");
			MapExts mes = md.getMapExts();
			DataTable ddlTable = new DataTable();
			ddlTable.Columns.Add("No");

			for (DataRow dr : dtMapAttr.Rows) {

				String lgType = dr.get("LGType").toString();
				String uiBindKey = dr.get("UIBindKey").toString();

				String uiVisible = dr.get("UIVisible").toString();
				if (uiVisible.equals("0") == true) {
					continue;
				}

				if (DataType.IsNullOrEmpty(uiBindKey) == true) {
					continue; // 为空就continue.
				}

				if (lgType.equals("1") == true) {
					continue; // 枚举值就continue;
				}

				String uiIsEnable = dr.get("UIIsEnable").toString();
				if (uiIsEnable.equals("0") == true && lgType.equals("1") == true) {
					continue; // 如果是外键，并且是不可以编辑的状态.
				}

				if (uiIsEnable.equals("0") == true && lgType.equals("0") == true) {
					continue; // 如果是外部数据源，并且是不可以编辑的状态.
				}

				// 检查是否有下拉框自动填充。
				String keyOfEn = dr.get("KeyOfEn").toString();
				String fk_mapData = dr.get("FK_MapData").toString();

				/// #region 处理下拉框数据范围. for 小杨.
				Object tempVar3 = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL,
						MapExtAttr.AttrOfOper, keyOfEn);
				me = tempVar3 instanceof MapExt ? (MapExt) tempVar3 : null;
				if (me != null) {
					Object tempVar4 = me.getDoc();
					String fullSQL = tempVar4 instanceof String ? (String) tempVar4 : null;
					fullSQL = fullSQL.replace("~", ",");
					fullSQL = BP.WF.Glo.DealExp(fullSQL, en, null);
					DataTable dt = DBAccess.RunSQLReturnTable(fullSQL);
					dt.TableName = keyOfEn; // 可能存在隐患，如果多个字段，绑定同一个表，就存在这样的问题.
					ds.Tables.add(dt);
					continue;
				}

				/// #endregion 处理下拉框数据范围.

				// 判断是否存在.
				if (ds.Tables.contains(uiBindKey) == true) {
					continue;
				}

				DataTable dataTable = BP.Sys.PubClass.GetDataTableByUIBineKey(uiBindKey);

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

			/// #endregion End把外键表加入DataSet

			/// #region 加入组件的状态信息, 在解析表单的时候使用.

			if (this.getFK_Node() != 0 && this.getFK_Node() != 999999
					&& (fn.getIsEnableFWC() == true || nd.getFrmWorkCheckSta() != FrmWorkCheckSta.Disable)) {
				BP.WF.Template.FrmNodeComponent fnc = new FrmNodeComponent(nd.getNodeID());
				if (!nd.getNodeFrmID().equals("ND" + nd.getNodeID())) {
					/* 说明这是引用到了其他节点的表单，就需要把一些位置元素修改掉. */
					int refNodeID = 0;
					if (nd.getNodeFrmID().indexOf("ND") == -1) {
						refNodeID = nd.getNodeID();
					} else {
						refNodeID = Integer.parseInt(nd.getNodeFrmID().replace("ND", ""));
					}

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
					if (md.getHisFrmType() == FrmType.FoolForm) {
						// 判断是否是傻瓜表单，如果是，就要判断该傻瓜表单是否有审核组件groupfield ,没有的话就增加上.
						DataTable gf = ds.GetTableByName("Sys_GroupField");
						boolean isHave = false;
						for (DataRow dr : gf.Rows) {
							String cType = dr.get("CtrlType") instanceof String ? (String) dr.get("CtrlType") : null;
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
							refFnc.Update();

						}
					}
				}

				/// #region 没有审核组件分组就增加上审核组件分组. @杜需要翻译&测试.
				if (nd.getNodeFrmID().equals("ND" + nd.getNodeID())
						&& nd.getHisFormType() != NodeFormType.RefOneFrmTree) {

					if (md.getHisFrmType() == FrmType.FoolForm) {
						// 判断是否是傻瓜表单，如果是，就要判断该傻瓜表单是否有审核组件groupfield ,没有的话就增加上.
						DataTable gf = ds.GetTableByName("Sys_GroupField");
						boolean isHave = false;
						for (DataRow dr : gf.Rows) {
							String cType = dr.get("CtrlType") instanceof String ? (String) dr.get("CtrlType") : null;
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
							BP.WF.Template.FrmNodeComponent refFnc = new FrmNodeComponent(nd.getNodeID());
							FrmWorkCheck fwc = new FrmWorkCheck(nd.getNodeID());
							if (fn.getFrmSln() == FrmSln.Self || fn.getFrmSln() == FrmSln.Default) {
								fwc.setHisFrmWorkCheckSta(FrmWorkCheckSta.Enable);
							} else {
								fwc.setHisFrmWorkCheckSta(FrmWorkCheckSta.Readonly);
							}

							refFnc.Update();

						}
					}
				}

				/// #endregion 没有审核组件分组就增加上审核组件分组.
				ds.Tables.add(fnc.ToDataTableField("WF_FrmNodeComponent"));
			}
			if (this.getFK_Node() != 0 && this.getFK_Node() != 999999) {
				ds.Tables.add(nd.ToDataTableField("WF_Node"));
			}

			if (this.getFK_Node() != 0 && this.getFK_Node() != 999999) {
				ds.Tables.add(fn.ToDataTableField("WF_FrmNode"));
			}

			/// #endregion 加入组件的状态信息, 在解析表单的时候使用.

			/// #region 处理权限方案
			if (nd != null && nd.getFormType() == NodeFormType.SheetTree) {

				/// #region 只读方案.
				if (fn.getFrmSln() == FrmSln.Readonly) {
					for (DataRow dr : dtMapAttr.Rows) {
						dr.setValue(MapAttrAttr.UIIsEnable, 0);
					}

					// 改变他的属性. 不知道是否应该这样写？
					ds.Tables.remove("Sys_MapAttr");
					ds.Tables.add(dtMapAttr);
				}

				/// #endregion 只读方案.

				/// #region 自定义方案.
				if (fn.getFrmSln() == FrmSln.Self) {
					// 查询出来自定义的数据.
					FrmFields ffs = new FrmFields();
					ffs.Retrieve(FrmFieldAttr.FK_Node, nd.getNodeID(), FrmFieldAttr.FK_MapData, md.getNo());

					// 遍历属性集合.
					for (DataRow dr : dtMapAttr.Rows) {
						String keyOfEn = dr.get(MapAttrAttr.KeyOfEn).toString();
						for (FrmField ff : ffs.ToJavaList()) {
							if (ff.getKeyOfEn().equals(keyOfEn) == false) {
								continue;
							}

							dr.setValue(MapAttrAttr.UIIsEnable, ff.getUIIsEnable()); // 是否只读?
							dr.setValue(MapAttrAttr.UIVisible, ff.getUIVisible()); // 是否可见?
							dr.setValue(MapAttrAttr.UIIsInput, ff.getIsNotNull()); // 是否必填?

							dr.setValue(MapAttrAttr.DefVal, ff.getDefVal()); // 默认值.

							Attr attr = new Attr();
							attr.setMyDataType(DataType.AppString);
							attr.setDefaultValOfReal(ff.getDefVal());
							attr.setKey(ff.getKeyOfEn());

							if (dr.get(MapAttrAttr.UIIsEnable).toString().equals("0")) {
								attr.setUIIsReadonly(true);
							} else {
								attr.setUIIsReadonly(false);
							}

							// 处理默认值.
							if (DataType.IsNullOrEmpty(ff.getDefVal()) == true) {
								continue;
							}

							// 数据类型.
							attr.setMyDataType(Integer.parseInt(dr.get(MapAttrAttr.MyDataType).toString()));
							String v = ff.getDefVal();

							// 设置默认值.
							String myval = en.GetValStrByKey(ff.getKeyOfEn());

							// 设置默认值.
							switch (ff.getDefVal()) {
							case "@WebUser.getNo()":
								if (attr.getUIIsReadonly() == true) {
									en.SetValByKey(attr.getKey(), WebUser.getNo());
								} else {
									if (DataType.IsNullOrEmpty(myval) || myval.equals(v)) {
										en.SetValByKey(attr.getKey(), WebUser.getNo());
									}
								}
								continue;
							case "@WebUser.getName()":
								if (attr.getUIIsReadonly() == true) {
									en.SetValByKey(attr.getKey(), WebUser.getName());
								} else {
									if (DataType.IsNullOrEmpty(myval) || myval.equals(v)) {
										en.SetValByKey(attr.getKey(), WebUser.getName());
									}
								}
								continue;
							case "@WebUser.getFK_Dept()":
								if (attr.getUIIsReadonly() == true) {
									en.SetValByKey(attr.getKey(), WebUser.getFK_Dept());
								} else {
									if (DataType.IsNullOrEmpty(myval) || myval.equals(v)) {
										en.SetValByKey(attr.getKey(), WebUser.getFK_Dept());
									}
								}
								continue;
							case "@WebUser.getFK_DeptName":
								if (attr.getUIIsReadonly() == true) {
									en.SetValByKey(attr.getKey(), WebUser.getFK_DeptName());
								} else {
									if (DataType.IsNullOrEmpty(myval) || myval.equals(v)) {
										en.SetValByKey(attr.getKey(), WebUser.getFK_DeptName());
									}
								}
								continue;
							case "@WebUser.getFK_DeptNameOfFull":
							case "@WebUser.getFK_DeptFullName":
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
									en.SetValByKey(attr.getKey(), DateUtils.format(new Date(), v.replace("@", "")));
								} else {
									if (DataType.IsNullOrEmpty(myval) || myval.equals(v)) {
										en.SetValByKey(attr.getKey(), DateUtils.format(new Date(), v.replace("@", "")));
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

				/// #endregion 自定义方案.

			}

			/// #endregion 处理权限方案s

			/// #region 加入主表的数据.
			// 增加主表数据.
			DataTable mainTable = en.ToDataTableField(md.getNo());
			mainTable.TableName = "MainTable";
			ds.Tables.add(mainTable);

			/// #endregion 加入主表的数据.

			String json = BP.Tools.Json.ToJson(ds);

			// BP.DA.DataType.WriteFile("c:\\aaa.txt", json);
			return json;
		} catch (RuntimeException ex) {
			GEEntity myen = new GEEntity(this.getEnsName());
			myen.CheckPhysicsTable();

			BP.Sys.CCFormAPI.RepareCCForm(this.getEnsName());
			return "err@装载表单期间出现如下错误,ccform有自动诊断修复功能请在刷新一次，如果仍然存在请联系管理员. @" + ex.getMessage();
		}
	}

	public final String FrmFreeReadonly_Init() throws Exception {
		try {
			MapData md = new MapData(this.getEnsName());
			DataSet ds = BP.Sys.CCFormAPI.GenerHisDataSet(md.getNo());

			/// #region 把主表数据放入.
			String atParas = "";
			// 主表实体.
			GEEntity en = new GEEntity(this.getEnsName());

			/// #region 求出 who is pk 值.
			long pk = this.getRefOID();
			if (pk == 0) {
				pk = this.getOID();
			}
			if (pk == 0) {
				pk = this.getWorkID();
			}

			if (this.getFK_Node() != 0 && DataType.IsNullOrEmpty(this.getFK_Flow()) == false) {
				/* 说明是流程调用它， 就要判断谁是表单的PK. */
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

			return Json.ToJson(ds);
		} catch (RuntimeException ex) {
			GEEntity myen = new GEEntity(this.getEnsName());
			myen.CheckPhysicsTable();

			BP.Sys.CCFormAPI.RepareCCForm(this.getEnsName());
			return "err@装载表单期间出现如下错误,ccform有自动诊断修复功能请在刷新一次，如果仍然存在请联系管理员. @" + ex.getMessage();
		}
	}

	/**
	 * 执行保存
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String FrmGener_Save() throws Exception {
		try {
			// 保存主表数据.
			GEEntity en = new GEEntity(this.getEnsName());

			/// #region 求出 who is pk 值.
			long pk = this.getRefOID();
			if (pk == 0) {
				pk = this.getOID();
			}
			if (pk == 0) {
				pk = this.getWorkID();
			}

			if (this.getFK_Node() != 0 && DataType.IsNullOrEmpty(this.getFK_Flow()) == false) {
				/* 说明是流程调用它， 就要判断谁是表单的PK. */
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

				if (fn.getFrmSln() == FrmSln.Readonly) {
					/* 如果是不可以编辑 */
					return "";
				}
			}

			/// #endregion 求who is PK.

			en.setOID(pk);
			int i = en.RetrieveFromDBSources();
			en.ResetDefaultVal();

			Object tempVar = BP.Sys.PubClass.CopyFromRequest(en);
			en = tempVar instanceof GEEntity ? (GEEntity) tempVar : null;

			en.setOID(pk);

			// 处理表单保存前事件.
			MapData md = new MapData(this.getEnsName());

			/// #region 调用事件. @李国文.
			// 是不是从表的保存.
			if (this.GetRequestValInt("IsForDtl") == 1) {

				/// #region 从表保存前处理事件.
				// 获得主表事件.
				FrmEvents fes = new FrmEvents(this.getEnsName()); // 获得事件.
				GEEntity mainEn = null;
				if (fes.size() > 0) {
					String msg = fes.DoEventNode(EventListDtlList.DtlSaveBefore, en);
					if (DataType.IsNullOrEmpty(msg) == false) {
						return "err@" + msg;
					}
				}

				MapDtl mdtl = new MapDtl(this.getEnsName());
				if (mdtl.getFEBD().length() != 0) {
					String str = mdtl.getFEBD();
					BP.Sys.FormEventBaseDtl febd = BP.Sys.Glo.GetFormDtlEventBaseByEnName(mdtl.getNo());

					if (febd != null) {
						febd.HisEn = mdtl.GenerGEMainEntity(this.getRefPKVal());
						febd.HisEnDtl = en;

						febd.DoIt(FrmEventListDtl.RowSaveBefore, febd.HisEn, en, null);
					}
				}

				/// #endregion 从表保存前处理事件.
			} else {
				md.DoEvent(FrmEventList.SaveBefore, en);
			}

			/// #endregion 调用事件. @李国文.

			if (i == 0) {
				en.Insert();
			} else {
				en.Update();
			}

			/// #region 调用事件.
			if (this.GetRequestValInt("IsForDtl") == 1) {

				/// #region 从表保存前处理事件.
				// 获得主表事件.
				FrmEvents fes = new FrmEvents(this.getEnsName()); // 获得事件.
				GEEntity mainEn = null;
				if (fes.size() > 0) {
					String msg = fes.DoEventNode(EventListDtlList.DtlItemSaveAfter, en);
					if (DataType.IsNullOrEmpty(msg) == false) {
						return "err@" + msg;
					}
				}

				MapDtl mdtl = new MapDtl(this.getEnsName());

				if (mdtl.getFEBD().length() != 0) {
					String str = mdtl.getFEBD();
					BP.Sys.FormEventBaseDtl febd = BP.Sys.Glo.GetFormDtlEventBaseByEnName(mdtl.getNo());

					if (febd != null) {
						febd.HisEn = mdtl.GenerGEMainEntity(this.getRefPKVal());
						febd.HisEnDtl = en;

						febd.DoIt(FrmEventListDtl.RowSaveAfter, febd.HisEn, en, null);
					}
				}

				/// #endregion 从表保存前处理事件.
			} else {
				md.DoEvent(FrmEventList.SaveAfter, en);
			}

			/// #endregion 调用事件. @李国文.

			return "保存成功.";
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}

	/// #endregion

	/// #region dtl.htm 从表.
	/**
	 * 初始化从表数据
	 * 
	 * @return 返回结果数据
	 * @throws Exception
	 */
	public final String Dtl_Init() throws Exception {
		DataSet ds = Dtl_Init_Dataset();
		return Json.ToJson(ds);
	}

	private DataSet Dtl_Init_Dataset() throws Exception {

		/// #region 组织参数.
		MapDtl mdtl = new MapDtl(this.getEnsName());
		mdtl.setNo(this.getEnsName());

		/// #region 如果是测试，就创建表.
		if (this.getFK_Node() == 999999 || this.GetRequestVal("IsTest") != null) {
			GEDtl dtl = new GEDtl(mdtl.getNo());
			dtl.CheckPhysicsTable();
		}

		/// #endregion 如果是测试，就创建表.

		String frmID = mdtl.getFK_MapData();
		if (this.getFK_Node() != 0 && this.getFK_Node() != 999999) {
			frmID = frmID.replace("_" + this.getFK_Node(), "");
		}

		if (this.getFK_Node() != 0 && !mdtl.getFK_MapData().equals("Temp")
				&& this.getEnsName().contains("ND" + this.getFK_Node()) == false && this.getFK_Node() != 999999) {
			Node nd = new BP.WF.Node(this.getFK_Node());

			if (nd.getHisFormType() == NodeFormType.SheetTree || nd.getHisFormType() == NodeFormType.RefOneFrmTree
					|| nd.getHisFormType() == NodeFormType.FoolTruck) {
				/*
				 * 如果 1,传来节点ID, 不等于0. 2,不是节点表单. 就要判断是否是独立表单，如果是就要处理权限方案。
				 */
				BP.WF.Template.FrmNode fn = new BP.WF.Template.FrmNode(nd.getFK_Flow(), nd.getNodeID(), frmID);
				if (fn.getFrmSln() == FrmSln.Readonly) {
					mdtl.setIsInsert(false);
					mdtl.setIsDelete(false);
					mdtl.setIsUpdate(false);
					mdtl.setIsReadonly(true);
				}

				/**
				 * 自定义权限.
				 */
				if (fn.getFrmSln() == FrmSln.Self) {
					mdtl.setNo(this.getEnsName() + "_" + this.getFK_Node());
					if (mdtl.RetrieveFromDBSources() == 0) {
						/* 如果设置了自定义方案，但是没有定义，从表属性，就需要去默认值. */
					}
				}
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
		DataSet ds = BP.WF.CCFormAPI.GenerDBForCCFormDtl(frmID, mdtl, Integer.parseInt(this.getRefPKVal()), strs,
				this.getFID());
		return ds;
	}

	/**
	 * 执行从表的保存.
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String Dtl_Save() throws Exception {
		MapDtl mdtl = new MapDtl(this.getEnsName());
		GEDtls dtls = new GEDtls(this.getEnsName());
		FrmEvents fes = new FrmEvents(this.getEnsName()); // 获得事件.
		GEEntity mainEn = null;

		/// #region 从表保存前处理事件.
		if (fes.size() > 0) {
			mainEn = mdtl.GenerGEMainEntity(this.getRefPKVal());
			String msg = fes.DoEventNode(EventListDtlList.DtlSaveBefore, mainEn);
			if (msg != null) {
				throw new RuntimeException(msg);
			}
		}

		/// #endregion 从表保存前处理事件.

		/// #region 保存的业务逻辑.

		/// #endregion 保存的业务逻辑.

		return "保存成功";
	}

	/**
	 * 导出excel与附件信息,并且压缩一个压缩包.
	 * 
	 * @return 返回下载路径
	 * @throws Exception
	 */
	public final String Dtl_ExpZipFiles() throws Exception {
		DataSet ds = Dtl_Init_Dataset();

		return "err@尚未wancheng.";
	}

	/**
	 * 保存单行数据
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String Dtl_SaveRow() throws Exception {
		// 从表.
		String fk_mapDtl = this.getFK_MapDtl();
		MapDtl mdtl = new MapDtl(fk_mapDtl);

		/// #region 处理权限方案。
		if (this.getFK_Node() != 0 && this.getFK_Node() != 999999) {
			Node nd = new Node(this.getFK_Node());
			if (nd.getHisFormType() == NodeFormType.SheetTree || nd.getHisFormType() == NodeFormType.RefOneFrmTree) {
				FrmNode fn = new FrmNode(nd.getFK_Flow(), nd.getNodeID(), this.getFK_MapData());
				if (fn.getFrmSln() == FrmSln.Self) {
					String no = fk_mapDtl + "_" + nd.getNodeID();
					MapDtl mdtlSln = new MapDtl();
					mdtlSln.setNo(no);
					int result = mdtlSln.RetrieveFromDBSources();
					if (result != 0) {
						mdtl = mdtlSln;
						fk_mapDtl = no;
					}
				}
			}
		}

		/// #endregion 处理权限方案。

		// 从表实体.
		GEDtl dtl = new GEDtl(fk_mapDtl);
		int oid = this.getRefOID();
		if (oid != 0) {
			dtl.setOID(oid);
			dtl.RetrieveFromDBSources();
		}

		/// #region 给实体循环赋值/并保存.
		BP.En.Attrs attrs = dtl.getEnMap().getAttrs();
		for (BP.En.Attr attr : attrs) {
			dtl.SetValByKey(attr.getKey(), this.GetRequestVal(attr.getKey()));
		}

		// 关联主赋值.
		dtl.setRefPK(this.getRefPKVal());
		switch (mdtl.getDtlOpenType()) {
		case ForEmp: // 按人员来控制.
			dtl.setRefPK(this.getRefPKVal());
			break;
		case ForWorkID: // 按工作ID来控制
			dtl.setRefPK(this.getRefPKVal());
			dtl.setFID(Long.parseLong(this.getRefPKVal()));
			break;
		case ForFID: // 按流程ID来控制.
			dtl.setRefPK(this.getRefPKVal());
			dtl.setFID(this.getFID());
			break;
		}

		/// #region 从表保存前处理事件.
		// 获得主表事件.
		FrmEvents fes = new FrmEvents(fk_mapDtl); // 获得事件.
		GEEntity mainEn = null;
		if (fes.size() > 0) {
			mainEn = mdtl.GenerGEMainEntity(this.getRefPKVal());
			String msg = fes.DoEventNode(EventListDtlList.DtlSaveBefore, mainEn);
			if (DataType.IsNullOrEmpty(msg) == false) {
				return "err@" + msg;
			}
		}

		if (mdtl.getFEBD().length() != 0) {
			String str = mdtl.getFEBD();
			BP.Sys.FormEventBaseDtl febd = BP.Sys.Glo.GetFormDtlEventBaseByEnName(mdtl.getNo());

			febd.HisEn = mdtl.GenerGEMainEntity(this.getRefPKVal());
			febd.HisEnDtl = dtl;

			febd.DoIt(FrmEventListDtl.RowSaveBefore, febd.HisEn, dtl, null);
		}

		/// #endregion 从表保存前处理事件.

		// 一直找不到refpk 值为null .
		dtl.setRefPK(this.getRefPKVal());
		if (dtl.getOID() == 0) {
			// dtl.setOID(DBAccess.GenerOID();
			dtl.Insert();
		} else {
			dtl.Update();
		}

		/// #endregion 给实体循环赋值/并保存.

		/// #region 从表保存后处理事件。
		if (fes.size() > 0) {
			String msg = fes.DoEventNode(EventListDtlList.DtlSaveEnd, mainEn);
			if (DataType.IsNullOrEmpty(msg) == false) {
				return "err@" + msg;
			}
		}

		if (mdtl.getFEBD().length() != 0) {
			String str = mdtl.getFEBD();
			BP.Sys.FormEventBaseDtl febd = BP.Sys.Glo.GetFormDtlEventBaseByEnName(mdtl.getNo());

			febd.HisEn = mdtl.GenerGEMainEntity(this.getRefPKVal());
			febd.HisEnDtl = dtl;

			febd.DoIt(FrmEventListDtl.RowSaveAfter, febd.HisEn, dtl, null);
		}

		/// #endregion 处理事件.

		// 返回当前数据存储信息.
		return dtl.ToJson();
	}

	/**
	 * 删除
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String Dtl_DeleteRow() throws Exception {
		GEDtl dtl = new GEDtl(this.getFK_MapDtl());
		dtl.setOID(this.getRefOID());

		/// #region 从表 删除 前处理事件.
		// 获得主表事件.
		FrmEvents fes = new FrmEvents(this.getFK_MapDtl()); // 获得事件.
		GEEntity mainEn = null;
		if (fes.size() > 0) {
			String msg = fes.DoEventNode(FrmEventListDtl.DtlRowDelBefore, dtl);
			if (DataType.IsNullOrEmpty(msg) == false) {
				return "err@" + msg;
			}
		}

		MapDtl mdtl = new MapDtl(this.getFK_MapDtl());
		if (mdtl.getFEBD().length() != 0) {
			String str = mdtl.getFEBD();
			BP.Sys.FormEventBaseDtl febd = BP.Sys.Glo.GetFormDtlEventBaseByEnName(mdtl.getNo());
			if (febd != null) {
				febd.HisEn = mdtl.GenerGEMainEntity(this.getRefPKVal());
				febd.HisEnDtl = dtl;
				febd.DoIt(FrmEventListDtl.DtlRowDelBefore, febd.HisEn, dtl, null);
			}
		}

		/// #endregion 从表 删除 前处理事件.

		// 执行删除.
		dtl.Delete();

		/// #region 从表 删除 后处理事件.
		// 获得主表事件.
		fes = new FrmEvents(this.getFK_MapDtl()); // 获得事件.
		if (fes.size() > 0) {
			String msg = fes.DoEventNode(FrmEventListDtl.DtlRowDelAfter, dtl);
			if (DataType.IsNullOrEmpty(msg) == false) {
				return "err@" + msg;
			}
		}

		if (mdtl.getFEBD().length() != 0) {
			String str = mdtl.getFEBD();
			BP.Sys.FormEventBaseDtl febd = BP.Sys.Glo.GetFormDtlEventBaseByEnName(mdtl.getNo());
			if (febd != null) {
				febd.HisEn = mdtl.GenerGEMainEntity(this.getRefPKVal());
				febd.HisEnDtl = dtl;

				febd.DoIt(FrmEventListDtl.DtlRowDelAfter, febd.HisEn, dtl, null);
			}
		}

		/// #endregion 从表 删除 后处理事件.

		// 如果可以上传附件这删除相应的附件信息
		FrmAttachmentDBs dbs = new FrmAttachmentDBs();
		dbs.Delete(FrmAttachmentDBAttr.FK_MapData, this.getFK_MapDtl(), FrmAttachmentDBAttr.RefPKVal, this.getRefOID(),
				FrmAttachmentDBAttr.NodeID, this.getFK_Node());

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

	/// #endregion dtl.htm 从表.

	/// #region dtl.Card
	/**
	 * 初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String DtlCard_Init() throws Exception {
		DataSet ds = new DataSet();

		MapDtl md = new MapDtl(this.getEnsName());
		if (this.getFK_Node() != 0 && !md.getFK_MapData().equals("Temp")
				&& this.getEnsName().contains("ND" + this.getFK_Node()) == false && this.getFK_Node() != 999999) {
			Node nd = new BP.WF.Node(this.getFK_Node());

			if (nd.getHisFormType() == NodeFormType.SheetTree) {
				/*
				 * 如果 1,传来节点ID, 不等于0. 2,不是节点表单. 就要判断是否是独立表单，如果是就要处理权限方案。
				 */
				BP.WF.Template.FrmNode fn = new BP.WF.Template.FrmNode(nd.getFK_Flow(), nd.getNodeID(),
						this.getFK_MapData());
				/**
				 * 自定义权限.
				 */
				if (fn.getFrmSln() == FrmSln.Self) {
					md.setNo(this.getEnsName() + "_" + this.getFK_Node());
					if (md.RetrieveFromDBSources() == 0) {
						md = new MapDtl(this.getEnsName());
					}
				}
			}
		}

		// 主表数据.
		DataTable dt = md.ToDataTableField("Main");
		ds.Tables.add(dt);

		// 主表字段.
		MapAttrs attrs = md.getMapAttrs();
		ds.Tables.add(attrs.ToDataTableField("MapAttrs"));

		// 从表.
		MapDtls dtls = md.getMapDtls();
		ds.Tables.add(dtls.ToDataTableField("MapDtls"));

		// 从表的从表.
		for (MapDtl dtl : dtls.ToJavaList()) {
			MapAttrs subAttrs = new MapAttrs(dtl.getNo());
			ds.Tables.add(subAttrs.ToDataTableField(dtl.getNo()));
		}

		// 从表的数据.
		// GEDtls enDtls = new GEDtls(this.EnsName);

		/// #region 把从表的数据放入.
		GEDtls enDtls = new GEDtls(md.getNo());
		QueryObject qo = null;
		try {
			qo = new QueryObject(enDtls);
			switch (md.getDtlOpenType()) {
			case ForEmp: // 按人员来控制.
				qo.AddWhere(GEDtlAttr.RefPK, this.getRefPKVal());
				qo.addAnd();
				qo.AddWhere(GEDtlAttr.Rec, WebUser.getNo());
				break;
			case ForWorkID: // 按工作ID来控制
				qo.AddWhere(GEDtlAttr.RefPK, this.getRefPKVal());
				break;
			case ForFID: // 按流程ID来控制.
				if (this.getFID() == 0) {
					qo.AddWhere(GEDtlAttr.FID, this.getRefPKVal());
				} else {
					qo.AddWhere(GEDtlAttr.FID, this.getFID());
				}
				break;
			}
		} catch (RuntimeException ex) {
			dtls.getNewEntity().CheckPhysicsTable();
			throw ex;
		}

		// 条件过滤.
		if (!md.getFilterSQLExp().equals("")) {
			String[] strs = md.getFilterSQLExp().split("[=]", -1);
			qo.addAnd();
			qo.AddWhere(strs[0], strs[1]);
		}

		// 增加排序.
		qo.addOrderBy(enDtls.getNewEntity().getPKField());

		// 从表
		DataTable dtDtl = qo.DoQueryToTable();
		dtDtl.TableName = "DTDtls";
		ds.Tables.add(dtDtl);

		/// #endregion
		// enDtls.Retrieve(GEDtlAttr.RefPK, this.RefPKVal);
		// ds.Tables.add(enDtls.ToDataTableField("DTDtls"));

		return BP.Tools.Json.ToJson(ds);

	}

	/**
	 * 获得从表的从表数据
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String DtlCard_Init_Dtl() throws Exception {
		DataSet ds = new DataSet();

		MapDtl md = new MapDtl(this.getEnsName());

		// 主表数据.
		DataTable dt = md.ToDataTableField("Main");
		ds.Tables.add(dt);

		// 主表字段.
		MapAttrs attrs = md.getMapAttrs();
		ds.Tables.add(attrs.ToDataTableField("MapAttrs"));

		GEDtls enDtls = new GEDtls(this.getEnsName());
		enDtls.Retrieve(GEDtlAttr.RefPK, this.getRefPKVal());
		ds.Tables.add(enDtls.ToDataTableField("DTDtls"));

		return BP.Tools.Json.ToJson(ds);
	}

	/**
	 * 保存手写签名图片
	 * 
	 * @return 返回保存结果
	 */
	public final String HandWriting_Save() {
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
			Base64 decoder = new Base64();
			byte[] imgByte = Base64.decode(imgData);
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

	/// #endregion

	/**
	 * 处理SQL的表达式.
	 * 
	 * @param exp
	 *            表达式
	 * @return 从from里面替换的表达式.
	 */
	public final String DealExpByFromVals(String exp) {
		Enumeration enu = ContextHolderUtils.getRequest().getParameterNames();
		while (enu.hasMoreElements()) {
			String strKey = (String) enu.nextElement();
			if (exp.contains("@") == false) {
				return exp;
			}
			String str = strKey.replace("TB_", "").replace("CB_", "").replace("DDL_", "").replace("RB_", "");

			exp = exp.replace("@" + str, ContextHolderUtils.getRequest().getParameter(strKey));

		}

		return exp;
	}

	/**
	 * 初始化树的接口
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public final String PopVal_InitTree() throws Exception {
		String mypk = this.GetRequestVal("FK_MapExt");

		MapExt me = new MapExt();
		me.setMyPK(mypk);
		me.Retrieve();

		// 获得配置信息.
		Hashtable ht = me.PopValToHashtable();
		DataTable dtcfg = BP.Sys.PubClass.HashtableToDataTable(ht);

		String parentNo = this.GetRequestVal("ParentNo");
		if (parentNo == null) {
			parentNo = me.getPopValTreeParentNo();
		}

		DataSet resultDs = new DataSet();
		String sqlObjs = me.getPopValTreeSQL();
		sqlObjs = sqlObjs.replace("@WebUser.getNo()", WebUser.getNo());
		sqlObjs = sqlObjs.replace("@WebUser.getName()", WebUser.getName());
		sqlObjs = sqlObjs.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());
		sqlObjs = sqlObjs.replace("@ParentNo", parentNo);
		sqlObjs = this.DealExpByFromVals(sqlObjs);

		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sqlObjs);
		dt.TableName = "DTObjs";

		// 判断是否是oracle.
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle
				|| SystemConfig.getAppCenterDBType() == DBType.PostgreSQL) {
			dt.Columns.get("NO").ColumnName = "No";
			dt.Columns.get("NAME").ColumnName = "Name";
			dt.Columns.get("PARENTNO").ColumnName = "ParentNo";
		}
		resultDs.Tables.add(dt);

		// doubleTree
		if (me.getPopValWorkModel() == PopValWorkModel.TreeDouble && !parentNo.equals(me.getPopValTreeParentNo())) {
			sqlObjs = me.getPopValDoubleTreeEntitySQL();
			sqlObjs = sqlObjs.replace("@WebUser.getNo()", WebUser.getNo());
			sqlObjs = sqlObjs.replace("@WebUser.getName()", WebUser.getName());
			sqlObjs = sqlObjs.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());
			sqlObjs = sqlObjs.replace("@ParentNo", parentNo);
			sqlObjs = this.DealExpByFromVals(sqlObjs);

			DataTable entityDt = BP.DA.DBAccess.RunSQLReturnTable(sqlObjs);
			entityDt.TableName = "DTEntitys";
			resultDs.Tables.add(entityDt);

			// 判断是否是oracle.
			if (SystemConfig.getAppCenterDBType() == DBType.Oracle
					|| SystemConfig.getAppCenterDBType() == DBType.PostgreSQL) {
				entityDt.Columns.get("NO").ColumnName = "No";
				entityDt.Columns.get("NAME").ColumnName = "Name";

			}

		}

		return BP.Tools.Json.ToJson(resultDs);
	}

	/**
	 * 处理DataTable中的列名，将不规范的No,Name,ParentNo列纠正
	 * 
	 * @param dt
	 */
	public final void DoCheckTableColumnNameCase(DataTable dt) {
		for (DataColumn col : dt.Columns) {
			switch (col.ColumnName.toLowerCase()) {
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
	 * 初始化PopVal的值 除了分页表格模式之外的其他数据值
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String PopVal_Init() throws Exception {
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

		if (me.getPopValWorkModel() == PopValWorkModel.SelfUrl) {
			return "@SelfUrl" + me.getPopValUrl();
		}

		if (me.getPopValWorkModel() == PopValWorkModel.TableOnly) {
			String sqlObjs = me.getPopValEntitySQL();
			sqlObjs = sqlObjs.replace("@WebUser.getNo()", WebUser.getNo());
			sqlObjs = sqlObjs.replace("@WebUser.getName()", WebUser.getName());
			sqlObjs = sqlObjs.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());

			sqlObjs = this.DealExpByFromVals(sqlObjs);

			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sqlObjs);
			dt.TableName = "DTObjs";
			DoCheckTableColumnNameCase(dt);
			ds.Tables.add(dt);
			return BP.Tools.Json.ToJson(ds);
		}

		if (me.getPopValWorkModel() == PopValWorkModel.Group) {
			/*
			 * 分组的.
			 */

			String sqlObjs = me.getPopValGroupSQL();
			if (sqlObjs.length() > 10) {
				sqlObjs = sqlObjs.replace("@WebUser.getNo()", WebUser.getNo());
				sqlObjs = sqlObjs.replace("@WebUser.getName()", WebUser.getName());
				sqlObjs = sqlObjs.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());
				sqlObjs = this.DealExpByFromVals(sqlObjs);

				DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sqlObjs);
				dt.TableName = "DTGroup";
				DoCheckTableColumnNameCase(dt);
				ds.Tables.add(dt);
			}

			sqlObjs = me.getPopValEntitySQL();
			if (sqlObjs.length() > 10) {
				sqlObjs = sqlObjs.replace("@WebUser.getNo()", WebUser.getNo());
				sqlObjs = sqlObjs.replace("@WebUser.getName()", WebUser.getName());
				sqlObjs = sqlObjs.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());
				sqlObjs = this.DealExpByFromVals(sqlObjs);

				DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sqlObjs);
				dt.TableName = "DTEntity";
				DoCheckTableColumnNameCase(dt);
				ds.Tables.add(dt);
			}
			return BP.Tools.Json.ToJson(ds);

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

			// pageSize
			String pageSize = this.GetRequestVal("pageSize");
			if (DataType.IsNullOrEmpty(pageSize)) {
				pageSize = "10";
			}

			// pageIndex
			String pageIndex = this.GetRequestVal("pageIndex");
			if (DataType.IsNullOrEmpty(pageIndex)) {
				pageIndex = "1";
			}

			String sqlObjs = me.getPopValTablePageSQL();
			sqlObjs = sqlObjs.replace("@WebUser.getNo()", WebUser.getNo());
			sqlObjs = sqlObjs.replace("@WebUser.getName()", WebUser.getName());
			sqlObjs = sqlObjs.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());
			sqlObjs = sqlObjs.replace("@Key", key);

			// 三个固定参数.
			sqlObjs = sqlObjs.replace("@PageCount",
					(String.valueOf((Integer.parseInt(pageIndex) - 1) * Integer.parseInt(pageSize))));
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
				String val = this.GetRequestVal(para);
				if (DataType.IsNullOrEmpty(val)) {
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

			// 处理查询条件.
			// $Para=Dept#Label=所在班级#ListSQL=Select No,Name FROM Port_Dept WHERE
			// No='@WebUser.getNo()'
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
					sql = sql.replace("@WebUser.getNo()", WebUser.getNo());
					sql = sql.replace("@WebUser.getName()", WebUser.getName());
					sql = sql.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());
					sql = this.DealExpByFromVals(sql);
				}

				if (cond.contains("#EnumKey=") == true) {
					String enumKey = cond.substring(cond.indexOf("EnumKey") + 8);
					sql = "SELECT IntKey AS No, Lab as Name FROM Sys_Enum WHERE EnumKey='" + enumKey + "'";
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
				if (ds.Tables.contains(para) == true) {
					throw new RuntimeException("@配置的查询,参数名有冲突不能命名为:" + para);
				}

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

	/**
	 * 初始化PopVal 分页表格模式的Count 杨玉慧
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String PopVal_InitTablePageCount() throws Exception {
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
			countSQL = countSQL.replace("@WebUser.getNo()", WebUser.getNo());
			countSQL = countSQL.replace("@WebUser.getName()", WebUser.getName());
			countSQL = countSQL.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());
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
			// No='@WebUser.getNo()'
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
					sql = sql.replace("@WebUser.getNo()", WebUser.getNo());
					sql = sql.replace("@WebUser.getName()", WebUser.getName());
					sql = sql.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());
					sql = this.DealExpByFromVals(sql);
				}

				if (cond.contains("#EnumKey=") == true) {
					String enumKey = cond.substring(cond.indexOf("EnumKey") + 8);
					sql = "SELECT IntKey AS No, Lab as Name FROM Sys_Enum WHERE EnumKey='" + enumKey + "'";
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
		// 返回数据.
		return BP.Tools.Json.ToJson(ds);
	}

	/**
	 * /// 初始化PopVal分页表格的List 杨玉慧
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String PopVal_InitTablePageList() throws Exception {
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

			// pageSize
			String pageSize = this.GetRequestVal("pageSize");
			if (DataType.IsNullOrEmpty(pageSize)) {
				pageSize = "10";
			}

			// pageIndex
			String pageIndex = this.GetRequestVal("pageIndex");
			if (DataType.IsNullOrEmpty(pageIndex)) {
				pageIndex = "1";
			}

			String sqlObjs = me.getPopValTablePageSQL();
			sqlObjs = sqlObjs.replace("@WebUser.getNo()", WebUser.getNo());
			sqlObjs = sqlObjs.replace("@WebUser.getName()", WebUser.getName());
			sqlObjs = sqlObjs.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());
			sqlObjs = sqlObjs.replace("@Key", key);

			// 三个固定参数.
			sqlObjs = sqlObjs.replace("@PageCount",
					(String.valueOf((Integer.parseInt(pageIndex) - 1) * Integer.parseInt(pageSize))));
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
				String val = this.GetRequestVal(para);
				if (DataType.IsNullOrEmpty(val)) {
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
			ds.Tables.add(dt);

			// 处理查询条件.
			// $Para=Dept#Label=所在班级#ListSQL=Select No,Name FROM Port_Dept WHERE
			// No='@WebUser.getNo()'
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
					sql = sql.replace("@WebUser.getNo()", WebUser.getNo());
					sql = sql.replace("@WebUser.getName()", WebUser.getName());
					sql = sql.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());
					sql = this.DealExpByFromVals(sql);
				}

				if (cond.contains("#EnumKey=") == true) {
					String enumKey = cond.substring(cond.indexOf("EnumKey") + 8);
					sql = "SELECT IntKey AS No, Lab as Name FROM Sys_Enum WHERE EnumKey='" + enumKey + "'";
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
		// 返回数据.
		return BP.Tools.Json.ToJson(ds);
	}

	/**
	 * 删除附件
	 * 
	 * @param MyPK
	 * @return
	 * @throws Exception
	 */
	public final String DelWorkCheckAttach() throws Exception {
		FrmAttachmentDB athDB = new FrmAttachmentDB();
		athDB.RetrieveByAttr(FrmAttachmentDBAttr.MyPK, this.getMyPK());

		// 删除文件
		if (athDB.getFileFullName() != null) {
			if ((new File(athDB.getFileFullName())).isFile() == true) {
				(new File(athDB.getFileFullName())).delete();
			}
		}

		int i = athDB.Delete(FrmAttachmentDBAttr.MyPK, this.getMyPK());
		if (i > 0) {
			return "true";
		}
		return "false";
	}

	public final String FrmVSTO_Init() {
		return "";
	}

	/**
	 * 表单处理加载
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String FrmSingle_Init() throws Exception {
		if (DataType.IsNullOrEmpty(this.getFK_MapData())) {
			throw new RuntimeException("FK_MapData参数不能为空");
		}

		MapData md = new MapData();
		md.setNo(this.getFK_MapData());

		if (md.RetrieveFromDBSources() == 0) {
			throw new RuntimeException("未检索到FK_MapData=" + this.getFK_MapData() + "的表单，请核对参数");
		}

		int minOID = 10000000; // 最小OID设置为一千万
		long oid = this.getOID();
		Hashtable ht = new Hashtable();
		GEEntity en = md.getHisGEEn();

		if (oid == 0) {
			oid = minOID;
		}

		en.setOID(oid);

		if (en.RetrieveFromDBSources() == 0) {
			ht.put("IsExist", 0);
		} else {
			ht.put("IsExist", 1);
		}

		ht.put("OID", oid);
		ht.put("UserNo", WebUser.getNo());
		ht.put("SID", WebUser.getSID());

		return BP.Tools.Json.ToJsonEntityModel(ht);
	}

	/// #region 从表的选项.
	/**
	 * 初始化数据
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String DtlOpt_Init() throws Exception {
		MapDtl dtl = new MapDtl(this.getFK_MapDtl());

		if (dtl.getImpModel() == 0) {
			return "err@该从表不允许导入.";
		}

		if (dtl.getImpModel() == 2) {
			return "url@DtlImpByExcel.htm?FK_MapDtl=" + this.getFK_MapDtl();
		}

		if (DataType.IsNullOrEmpty(dtl.getImpSQLInit())) {
			return "err@从表加载语句为空，请设置从表加载的sql语句。";
		}

		DataSet ds = new DataSet();
		DataTable dt = DBAccess.RunSQLReturnTable(dtl.getImpSQLInit());

		return BP.Tools.Json.ToJson(dt);

	}

	/**
	 * 增加
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String DtlOpt_Add() throws Exception {
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
	 * @throws Exception
	 */
	public final String DtlOpt_Search() throws Exception {
		MapDtl dtl = new MapDtl(this.getFK_MapDtl());

		String sql = dtl.getImpSQLSearch();
		sql = sql.replace("@Key", this.GetRequestVal("Key"));
		sql = sql.replace("@WebUser.getNo()", WebUser.getNo());
		sql = sql.replace("@WebUser.getName()", WebUser.getName());
		sql = sql.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());

		DataSet ds = new DataSet();
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		return BP.Tools.Json.ToJson(dt);
	}

	/// #endregion 从表的选项.

	/// #region SQL从表导入.
	public final String DtlImpBySQL_Delete() throws Exception {
		MapDtl dtl = new MapDtl(this.getEnsName());
		BP.DA.DBAccess.RunSQL("DELETE FROM " + dtl.getPTable() + " WHERE RefPK='" + this.getRefPKVal() + "'");
		return "";
	}

	/**
	 * SQL从表导入
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String DtlImpBySQl_Imp() throws Exception {
		// 获取参数
		String ensName = this.getEnsName();
		String refpk = this.getRefPKVal();
		long pworkID = this.getPWorkID();
		int fkNode = this.getFK_Node();
		long fid = this.getFID();
		String pk = this.GetRequestVal("PKs");
		GEDtls dtls = new GEDtls(ensName);
		QueryObject qo = new QueryObject(dtls);
		// 获取从表权限
		MapDtl dtl = new MapDtl(ensName);

		/// #region 处理权限方案。
		if (this.getFK_Node() != 0 && this.getFK_Node() != 999999) {
			Node nd = new Node(this.getFK_Node());
			if (nd.getHisFormType() == NodeFormType.SheetTree || nd.getHisFormType() == NodeFormType.RefOneFrmTree) {
				FrmNode fn = new FrmNode(nd.getFK_Flow(), nd.getNodeID(), dtl.getFK_MapData());
				if (fn.getFrmSln() == FrmSln.Self) {
					String no = this.getFK_MapDtl() + "_" + nd.getNodeID();
					MapDtl mdtlSln = new MapDtl();
					mdtlSln.setNo(no);
					int result = mdtlSln.RetrieveFromDBSources();
					if (result != 0) {
						dtl = mdtlSln;

					}
				}
			}
		}

		/// #endregion 处理权限方案。

		// 判断是否重复导入
		boolean isInsert = true;
		if (DataType.IsNullOrEmpty(pk) == false) {
			String[] pks = pk.split("[@]", -1);
			int idx = 0;
			for (String k : pks) {
				if (DataType.IsNullOrEmpty(k)) {
					continue;
				}
				if (idx == 0) {
					qo.AddWhere(k, this.GetRequestVal(k));
				} else {
					qo.addAnd();
					qo.AddWhere(k, this.GetRequestVal(k));
				}
				idx++;
			}
			switch (dtl.getDtlOpenType()) {
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
			if (count > 0) {
				isInsert = false;
			}
		}
		// 导入数据
		if (isInsert == true) {

			GEDtl dtlEn = dtls.getNewEntity() instanceof GEDtl ? (GEDtl) dtls.getNewEntity() : null;
			// 遍历属性，循环赋值.
			for (Attr attr : dtlEn.getEnMap().getAttrs()) {
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
			dtlEn.SetValByKey("RDT", BP.DA.DataType.getCurrentDate());
			dtlEn.SetValByKey("Rec", this.GetRequestVal("UserNo"));
			// dtlEn.OID = (int)DBAccess.GenerOID(ensName);

			dtlEn.InsertAsOID((int) DBAccess.GenerOID(ensName));
			return String.valueOf(dtlEn.getOID());
		}

		return "";

	}

	/// #endregion SQL从表导入

	/// #region Excel导入.
	/**
	 * 导入excel.
	 * 
	 * @return
	 */
	public final String DtlImpByExcel_Imp() {
		String tempPath = SystemConfig.getPathOfTemp();
		try {
			MapDtl dtl = new MapDtl(this.getFK_MapDtl());
			HttpServletRequest request = getRequest();

			if (CommonFileUtils.getFilesSize(request, "File_Upload") == 0) {
				return "err@请选择要上传的从表模板.";
			}
			// 获取文件名,并取其后缀.
			String fileName = CommonFileUtils.getOriginalFilename(request, "File_Upload");
			String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
			if (!prefix.equals("xls") && !prefix.equals("xlsx")) {

				return "err@上传的文件必须是Excel文件.";
			}
			// 保存临时文件
			String userNo = "";
			try {
				userNo = BP.Web.WebUser.getNo();
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 判断路径是否存在
			tempPath = tempPath.replace("\\", "/");
			File tPath = new File(tempPath);
			if (!tPath.isDirectory()) {
				tPath.mkdirs();
			}
			// 执行保存附件
			File file = new File(tempPath + "/" + userNo + prefix);

			try {
				CommonFileUtils.upload(request, "File_Upload", file);
			} catch (Exception e) {
				e.printStackTrace();
				return "err@文件上传失败.";
			}
			GEDtls dtls = new GEDtls(this.getFK_MapDtl());
			DataTable dt = BP.DA.DBLoad.GetTableByExt(tempPath + "/" + userNo + prefix);

			/// #region 检查两个文件是否一致。 生成要导入的属性
			Attrs attrs = dtls.getNewEntity().getEnMap().getAttrs();
			Attrs attrsExp = new BP.En.Attrs();

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
				BP.DA.DBAccess.RunSQL("DELETE FROM " + dtl.getPTable() + " WHERE RefPK='" + this.getWorkID() + "'");

			int i = 0;
			int oid = (int) BP.DA.DBAccess.GenerOID("Dtl", dt.Rows.size());
			String rdt = BP.DA.DataType.getCurrentDate();

			String errMsg = "";
			for (DataRow dr : dt.Rows) {
				GEDtl dtlEn = (GEDtl) dtls.getNewEntity();
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
							ens = BP.En.ClassFactory.GetEns(attr.getUIBindKey());
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

					if (attr.getMyDataType() == BP.DA.DataType.AppBoolean) {
						if (val.trim() == "是" || val.trim().toLowerCase() == "yes")
							val = "1";

						if (val.trim() == "否" || val.trim().toLowerCase() == "no")
							val = "0";
					}

					dtlEn.SetValByKey(attr.getKey(), val);
				}
				// dtlEn.RefPKInt = (int)this.WorkID;
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
	 * BP类从表导入
	 * 
	 * @return
	 * @throws Exception
	 */
	private String BPDtlImpByExcel_Imp(DataTable dt, String fk_mapdtl) throws Exception {
		try {

			/// #region 检查两个文件是否一致。 生成要导入的属性
			Entities dtls = ClassFactory.GetEns(this.getFK_MapDtl());
			EntityOID dtlEn = dtls.getNewEntity() instanceof EntityOID ? (EntityOID) dtls.getNewEntity() : null;
			BP.En.Attrs attrs = dtlEn.getEnMap().getAttrs();
			BP.En.Attrs attrsExp = new BP.En.Attrs();

			boolean isHave = false;
			for (DataColumn dc : dt.Columns) {
				for (BP.En.Attr attr : attrs) {
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
			if (isHave == false) {
				return "err@您导入的excel文件不符合系统要求的格式，请下载模版文件重新填入。";
			}

			/// #endregion

			/// #region 执行导入数据.

			if (this.GetRequestValInt("DDL_ImpWay") == 0) {
				BP.DA.DBAccess.RunSQL("DELETE FROM " + dtlEn.getEnMap().getPhysicsTable() + " WHERE RefPK='"
						+ this.GetRequestVal("RefPKVal") + "'");
			}

			int i = 0;
			long oid = BP.DA.DBAccess.GenerOID(dtlEn.getEnMap().getPhysicsTable(), dt.Rows.size());
			String rdt = BP.DA.DataType.getCurrentDate();

			String errMsg = "";
			for (DataRow dr : dt.Rows) {
				dtlEn = dtls.getNewEntity() instanceof EntityOID ? (EntityOID) dtls.getNewEntity() : null;
				dtlEn.ResetDefaultVal();

				for (BP.En.Attr attr : attrsExp) {
					if (attr.getUIVisible() == false || dr.get(attr.getDesc()) == null) {
						continue;
					}
					String val = dr.get(attr.getDesc()).toString();
					if (val == null) {
						continue;
					}
					val = val.trim();
					switch (attr.getMyFieldType()) {
					case Enum:
					case PKEnum:
						SysEnums ses = new SysEnums(attr.getUIBindKey());
						boolean isHavel = false;
						for (SysEnum se : ses.ToJavaList()) {
							if (val.equals(se.getLab())) {
								val = String.valueOf(se.getIntKey());
								isHavel = true;
								break;
							}
						}
						if (isHavel == false) {
							errMsg += "@数据格式不规范,第(" + i + ")行，列(" + attr.getDesc() + ")，数据(" + val
									+ ")不符合格式,改值没有在枚举列表里.";
							val = String.valueOf(attr.getDefaultVal());
						}
						break;
					case FK:
					case PKFK:
						Entities ens = null;
						if (attr.getUIBindKey().contains(".")) {
							ens = BP.En.ClassFactory.GetEns(attr.getUIBindKey());
						} else {
							ens = new GENoNames(attr.getUIBindKey(), "desc");
						}

						ens.RetrieveAll();
						boolean isHavelIt = false;
						for (Entity en : ens) {
							if (val.equals(en.GetValStrByKey("Name"))) {
								val = en.GetValStrByKey("No");
								isHavelIt = true;
								break;
							}
						}
						if (isHavelIt == false) {
							errMsg += "@数据格式不规范,第(" + i + ")行，列(" + attr.getDesc() + ")，数据(" + val
									+ ")不符合格式,改值没有在外键数据列表里.";
						}
						break;
					default:
						break;
					}

					if (attr.getMyDataType() == BP.DA.DataType.AppBoolean) {
						if (val.trim().equals("是") || val.trim().toLowerCase().equals("yes")) {
							val = "1";
						}

						if (val.trim().equals("否") || val.trim().toLowerCase().equals("no")) {
							val = "0";
						}
					}

					dtlEn.SetValByKey(attr.getKey(), val);
				}

				dtlEn.SetValByKey("RefPK", this.GetRequestVal("RefPKVal"));
				i++;

				dtlEn.Insert();
			}

			/// #endregion 执行导入数据.

			if (DataType.IsNullOrEmpty(errMsg) == true) {
				return "info@共有(" + i + ")条数据导入成功。";
			} else {
				return "共有(" + i + ")条数据导入成功，但是出现如下错误:" + errMsg;
			}

		} catch (RuntimeException ex) {
			String msg = ex.getMessage().replace("'", "‘");
			return "err@" + msg;
		}
	}

	/// #endregion Excel导入.

	/// #region 打印.
	public final String Print_Init() throws Exception {
		String ApplicationPath = SystemConfig.getHostURLOfBS();

		BP.WF.Node nd = new BP.WF.Node(this.getFK_Node());
		String path = ApplicationPath + "DataUser/CyclostyleFile/FlowFrm/" + nd.getFK_Flow() + "/" + nd.getNodeID()
				+ "/";
		String[] fls = null;
		try {
			fls = (new File(path)).list();
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}

		DataTable dt = new DataTable();
		dt.Columns.Add("BillNo");
		dt.Columns.Add("BillName");

		int idx = 0;
		int fileIdx = -1;
		for (String f : fls) {
			fileIdx++;
			String myfile = f.replace(path, "");
			String[] strs = myfile.split("[.]", -1);
			idx++;

			DataRow dr = dt.NewRow();
			dr.setValue("BillNo", strs[0]);
			dr.setValue("BillName", strs[1]);

			dt.Rows.add(dr);
		}

		// 返回json.
		return BP.Tools.Json.ToJson(dt);
	}

	public final String Print_Done() throws Exception {
		int billIdx = this.GetValIntFromFrmByKey("BillIdx");

		String ApplicationPath = "";
		BP.WF.Node nd = new BP.WF.Node(this.getFK_Node());
		String path = ApplicationPath + "/DataUser/CyclostyleFile/FlowFrm/" + nd.getFK_Flow() + "/" + nd.getNodeID()
				+ "/";
		if ((new File(path)).isDirectory() == false) {
			return "err@模版文件没有找到。" + path;
		}

		String[] fls = (new File(path)).list();
		String file = fls[billIdx];
		file = file.replace(ApplicationPath + "DataUser/CyclostyleFile", "");

		File finfo = new File(file);
		String tempName = finfo.getName().split("[.]", -1)[0];
		String tempNameChinese = finfo.getName().split("[.]", -1)[1];

		String toPath = ApplicationPath + "DataUser/Bill/FlowFrm/" + DateUtils.format(new Date(), "yyyyMMdd") + "/";
		if ((new File(toPath)).isDirectory() == false) {
			(new File(toPath)).mkdirs();
		}

		String billFile = toPath + "/" + tempNameChinese + "." + this.getWorkID() + ".doc";

		BP.Pub.RTFEngine engine = new BP.Pub.RTFEngine();
		if (tempName.toLowerCase().equals("all")) {
			/* 说明要从所有的独立表单上取数据. */
			FrmNodes fns = new FrmNodes(this.getFK_Flow(), this.getFK_Node());
			for (FrmNode fn : fns.ToJavaList()) {
				GEEntity ge = new GEEntity(fn.getFK_Frm(), this.getWorkID());
				engine.AddEn(ge);
				MapDtls mdtls = new MapDtls(fn.getFK_Frm());
				for (MapDtl dtl : mdtls.ToJavaList()) {
					GEDtls enDtls = dtl.getHisGEDtl().getGetNewEntities() instanceof GEDtls
							? (GEDtls) dtl.getHisGEDtl().getGetNewEntities() : null;
					enDtls.Retrieve(GEDtlAttr.RefPK, this.getWorkID());
					engine.getEnsDataDtls().add(enDtls);
				}
			}

			// 增加主表.
			GEEntity myge = new GEEntity("ND" + nd.getNodeID(), this.getWorkID());
			engine.AddEn(myge);

			// 增加从表
			MapDtls mymdtls = new MapDtls(tempName);
			for (MapDtl dtl : mymdtls.ToJavaList()) {
				GEDtls enDtls = dtl.getHisGEDtl().getGetNewEntities() instanceof GEDtls
						? (GEDtls) dtl.getHisGEDtl().getGetNewEntities() : null;
				enDtls.Retrieve(GEDtlAttr.RefPK, this.getWorkID());
				engine.getEnsDataDtls().add(enDtls);
			}

			// 增加多附件数据
			FrmAttachments aths = new FrmAttachments(tempName);
			for (FrmAttachment athDesc : aths.ToJavaList()) {
				FrmAttachmentDBs athDBs = new FrmAttachmentDBs();
				if (athDBs.Retrieve(FrmAttachmentDBAttr.FK_FrmAttachment, athDesc.getMyPK(),
						FrmAttachmentDBAttr.RefPKVal, this.getWorkID(), "RDT") == 0) {
					continue;
				}

				engine.getEnsDataAths().put(athDesc.getNoOfObj(), athDBs);
			}
			engine.MakeDoc(file, toPath, tempNameChinese + "." + this.getWorkID() + ".doc", false);
		} else {
			// 增加主表.
			GEEntity myge = new GEEntity(tempName, this.getWorkID());
			engine.HisGEEntity = myge;
			engine.AddEn(myge);

			// 增加从表.
			MapDtls mymdtls = new MapDtls(tempName);
			for (MapDtl dtl : mymdtls.ToJavaList()) {
				GEDtls enDtls = dtl.getHisGEDtl().getGetNewEntities() instanceof GEDtls
						? (GEDtls) dtl.getHisGEDtl().getGetNewEntities() : null;
				enDtls.Retrieve(GEDtlAttr.RefPK, this.getWorkID());
				engine.getEnsDataDtls().add(enDtls);
			}

			// 增加轨迹表.
			Paras ps = new BP.DA.Paras();
			ps.SQL = "SELECT * FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track WHERE ActionType="
					+ SystemConfig.getAppCenterDBVarStr() + "ActionType AND WorkID="
					+ SystemConfig.getAppCenterDBVarStr() + "WorkID";
			ps.Add(TrackAttr.ActionType, ActionType.WorkCheck.getValue());
			ps.Add(TrackAttr.WorkID, this.getWorkID());
			engine.dtTrack = BP.DA.DBAccess.RunSQLReturnTable(ps);

			engine.MakeDoc(file, toPath, tempNameChinese + "." + this.getWorkID() + ".doc", false);
		}

		/// #region 保存单据，以方便查询.
		Bill bill = new Bill();
		bill.setMyPK(this.getFID() + "_" + this.getWorkID() + "_" + this.getFK_Node() + "_" + billIdx);
		bill.setWorkID(this.getWorkID());
		bill.setFK_Node(this.getFK_Node());
		bill.setFK_Dept(WebUser.getFK_Dept());
		bill.setFK_Emp(WebUser.getNo());

		bill.setUrl("/DataUser/Bill/FlowFrm/" + DateUtils.format(new Date(), "yyyyMMdd") + "/" + tempNameChinese + "."
				+ this.getWorkID() + ".doc");
		bill.setFullPath(toPath + file);

		bill.setRDT(DataType.getCurrentDataTime());
		bill.setFK_NY(DataType.getCurrentYearMonth());
		bill.setFK_Flow(this.getFK_Flow());
		if (this.getWorkID() != 0) {
			GenerWorkFlow gwf = new GenerWorkFlow();
			gwf.setWorkID(this.getWorkID());
			if (gwf.RetrieveFromDBSources() == 1) {
				bill.setEmps(gwf.getEmps());
				bill.setFK_Starter(gwf.getStarter());
				bill.setStartDT(gwf.getRDT());
				bill.setTitle(gwf.getTitle());
				bill.setFK_Dept(gwf.getFK_Dept());
			}
		}

		try {
			bill.Insert();
		} catch (java.lang.Exception e) {
			bill.Update();
		}

		BillTemplates templates = new BillTemplates();
		int iHave = templates.Retrieve(BillTemplateAttr.NodeID, this.getFK_Node(), BillTemplateAttr.BillOpenModel,
				BillOpenModel.WebOffice.getValue());

		return "url@../WebOffice/PrintOffice.htm?MyPK=" + bill.getMyPK();

	}

	/**
	 * 执行删除
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String AttachmentUpload_Del() throws Exception {
		// 执行删除.
		String delPK = this.GetRequestVal("DelPKVal");

		FrmAttachmentDB delDB = new FrmAttachmentDB();
		delDB.setMyPK(delPK == null ? this.getMyPK() : delPK);
		delDB.RetrieveFromDBSources();
		delDB.Delete(); // 删除上传的文件.
		return "删除成功.";
	}

	public final String AttachmentUpload_DownByStream() throws Exception {
		return AttachmentUpload_Down();
	}

	/**
	 * 下载
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String AttachmentUpload_Down() throws Exception {
		FrmAttachmentDB downDB = new FrmAttachmentDB();
		downDB.setMyPK(this.getMyPK());
		downDB.Retrieve();

		FrmAttachment dbAtt = new FrmAttachment();
		dbAtt.setMyPK(downDB.getFK_FrmAttachment());
		dbAtt.Retrieve();

		if (dbAtt.getAthSaveWay() == AthSaveWay.WebServer) {
			return "url@" + DataType.PraseStringToUrlFileName(downDB.getFileFullName());
		}

		if (dbAtt.getAthSaveWay() == AthSaveWay.FTPServer) {
			String fileFullName = downDB.GenerTempFile(dbAtt.getAthSaveWay());
			return "url@" + DataType.PraseStringToUrlFileName(fileFullName);
		}

		if (dbAtt.getAthSaveWay() == AthSaveWay.DB) {
			return "fromdb";
		}
		return "正在下载.";
	}

	/**
	 * 附件ID.
	 */
	public final String getFK_FrmAttachment() {
		return this.GetRequestVal("FK_FrmAttachment");
	}

	public final BP.Sys.FrmAttachment GenerAthDescOfFoolTruck() throws Exception {
		FoolTruckNodeFrm sln = new FoolTruckNodeFrm();
		sln.setFrmSln(-1);
		String fromFrm = this.GetRequestVal("FromFrm");
		sln.setMyPK(fromFrm + "_" + this.getFK_Node() + "_" + this.getFK_Flow());
		int result = sln.RetrieveFromDBSources();
		BP.Sys.FrmAttachment athDesc = new BP.Sys.FrmAttachment();
		athDesc.setMyPK(this.getFK_FrmAttachment());
		athDesc.RetrieveFromDBSources();

		/* 没有查询到解决方案, 就是只读方案 */
		if (result == 0 || sln.getFrmSln() == 1) {
			athDesc.setIsUpload(false);
			athDesc.setIsDownload(true);
			athDesc.setHisDeleteWay(AthDeleteWay.None); // 删除模式.
			return athDesc;
		}
		// 默认方案
		if (sln.getFrmSln() == 0) {
			return athDesc;
		}

		// 如果是自定义方案,就查询自定义方案信息.
		if (sln.getFrmSln() == 2) {
			BP.Sys.FrmAttachment athDescNode = new BP.Sys.FrmAttachment();
			athDescNode.setMyPK(this.getFK_FrmAttachment() + "_" + this.getFK_Node());
			if (athDescNode.RetrieveFromDBSources() == 0) {
				// 没有设定附件权限，保持原来的附件权限模式
				return athDesc;
			}
			return athDescNode;
		}

		return null;
	}

	/**
	 * 生成描述
	 * 
	 * @return
	 * @throws Exception
	 */
	public final BP.Sys.FrmAttachment GenerAthDesc() throws Exception {

		/// #region 为累加表单做的特殊判断.
		if (this.GetRequestValInt("FormType") == 10) {
			if (this.getFK_FrmAttachment().contains(this.getFK_MapData()) == false) {
				return GenerAthDescOfFoolTruck(); // 如果当前表单的ID。
			}
		}

		/// #endregion

		BP.Sys.FrmAttachment athDesc = new BP.Sys.FrmAttachment();
		athDesc.setMyPK(this.getFK_FrmAttachment());
		if (this.getFK_Node() == 0 || this.getFK_Flow() == null) {
			athDesc.RetrieveFromDBSources();
			return athDesc;
		}

		athDesc.setMyPK(this.getFK_FrmAttachment());
		int result = athDesc.RetrieveFromDBSources();

		/// #region 判断是否是明细表的多附件.
		if (result == 0 && DataType.IsNullOrEmpty(this.getFK_Flow()) == false
				&& this.getFK_FrmAttachment().contains("AthMDtl")) {
			athDesc.setFK_MapData(this.getFK_MapData());
			athDesc.setNoOfObj("AthMDtl");
			athDesc.setName("我的从表附件");
			athDesc.setUploadType(AttachmentUploadType.Multi);
			athDesc.Insert();
		}

		/// #endregion 判断是否是明细表的多附件。

		/// #region 判断是否可以查询出来，如果查询不出来，就可能是公文流程。
		if (result == 0 && DataType.IsNullOrEmpty(this.getFK_Flow()) == false
				&& this.getFK_FrmAttachment().contains("DocMultiAth")) {
			/* 如果没有查询到它,就有可能是公文多附件被删除了. */
			athDesc.setMyPK(this.getFK_FrmAttachment());
			athDesc.setNoOfObj("DocMultiAth");
			athDesc.setFK_MapData(this.getFK_MapData());
			athDesc.setExts("*.*");

			// 存储路径.
			athDesc.setSaveTo("/DataUser/UploadFile/");
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
			BP.WF.Nodes nds = new BP.WF.Nodes(this.getFK_Flow());
			for (BP.WF.Node nd : nds.ToJavaList()) {
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

		/// #endregion 判断是否可以查询出来，如果查询不出来，就可能是公文流程。

		/// #region 处理权限方案。
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
				FrmNode fn = new FrmNode(nd.getFK_Flow(), nd.getNodeID(), fk_mapdata);
				if (fn.getFrmSln() == FrmSln.Default) {
					if (fn.getWhoIsPK() == WhoIsPK.FID) {
						athDesc.setHisCtrlWay(AthCtrlWay.FID);
					}

					if (fn.getWhoIsPK() == WhoIsPK.PWorkID) {
						athDesc.setHisCtrlWay(AthCtrlWay.PWorkID);
					}

				}

				if (fn.getFrmSln() == FrmSln.Readonly) {
					if (fn.getWhoIsPK() == WhoIsPK.FID) {
						athDesc.setHisCtrlWay(AthCtrlWay.FID);
					}

					if (fn.getWhoIsPK() == WhoIsPK.PWorkID) {
						athDesc.setHisCtrlWay(AthCtrlWay.PWorkID);
					}

					athDesc.setHisDeleteWay(AthDeleteWay.None);
					athDesc.setIsUpload(false);
					athDesc.setIsDownload(true);
					athDesc.setMyPK(this.getFK_FrmAttachment());
					return athDesc;
				}

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

		/// #endregion 处理权限方案。

		return athDesc;
	}

	/**
	 * 打包下载.
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String AttachmentUpload_DownZip() throws Exception {
		String zipName = this.getWorkID() + "_" + this.getFK_FrmAttachment();
		/// #region 处理权限控制.
		BP.Sys.FrmAttachment athDesc = this.GenerAthDesc();

		// 查询出来数据实体.
		BP.Sys.FrmAttachmentDBs dbs = BP.WF.Glo.GenerFrmAttachmentDBs(athDesc, this.getPKVal(),
				this.getFK_FrmAttachment());
		/// #endregion 处理权限控制.

		if (dbs.size() == 0)
			return "err@文件不存在，不需打包下载。";

		String basePath = SystemConfig.getPathOfDataUser() + "Temp";
		String tempUserPath = basePath + "/" + WebUser.getNo();
		String tempFilePath = basePath + "/" + WebUser.getNo() + "/" + this.getOID();
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

				if (DataType.IsNullOrEmpty(db.getSort()) == false) {
					copyToPath = tempFilePath + "//" + db.getSort();
					File copyPath = new File(copyToPath);
					if (copyPath.exists() == false)
						copyPath.mkdirs();
				}
				// 新文件目录
				copyToPath = copyToPath + "//" + db.getFileName();
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

	public final String DearFileName(String fileName) {
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

	public final String DearFileNameExt(String fileName, String val, String replVal) {
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

	/// #endregion 附件组件

	/// #region 必须传递参数
	/**
	 * 执行的内容
	 */
	public final String getDoWhat() {
		String str = this.GetRequestVal("DoWhat");
		if (DataType.IsNullOrEmpty(str)) {
			return "Frm";
		}
		return str;
	}

	/**
	 * 当前的用户
	 */
	public final String getUserNo() {
		return this.GetRequestVal("UserNo");
	}

	/**
	 * 用户的安全校验码(请参考集成章节)
	 */
	public final String getSID() {
		return this.GetRequestVal("SID");
	}

	public final String getAppPath() {
		return BP.WF.Glo.getCCFlowAppPath();
	}

	public final String Port_Init() throws Exception {

		/// #region 安全性校验.
		if (this.getUserNo() == null || this.getSID() == null || this.getDoWhat() == null || this.getFrmID() == null) {
			return "err@必要的参数没有传入，请参考接口规则。";
		}

		if (BP.WF.Dev2Interface.Port_CheckUserLogin(this.getUserNo(), this.getSID()) == false) {
			return "err@非法的访问，请与管理员联系。SID=" + this.getSID();
		}

		if (!this.getUserNo().equals(WebUser.getNo())) {
			BP.WF.Dev2Interface.Port_SigOut();
			BP.WF.Dev2Interface.Port_Login(this.getUserNo());
		}

		/// #endregion 安全性校验.

		/// #region 生成参数串.
		String paras = "";
		for (String str : BP.Sys.Glo.getQueryStringKeys()) {
			String val = this.GetRequestVal(str);
			if (val.indexOf('@') != -1) {
				throw new RuntimeException("您没有能参数: [ " + str + " ," + val + " ] 给值 ，URL 将不能被执行。");
			}
			switch (str.toLowerCase()) {
			case "fk_mapdata":
			case "workid":
			case "fk_node":
			case "sid":
				break;
			default:
				paras += "&" + str + "=" + val;
				break;
			}
		}

		/// #endregion 生成参数串.

		String url = "";
		switch (this.getDoWhat()) {
		case "Frm": // 如果是调用Frm的查看界面.
			url = "Frm.htm?FK_MapData=" + this.getFrmID() + "&OID=" + this.getOID() + paras;
			break;
		case "Search": // 调用查询界面.
			url = "../Comm/Search.htm?EnsName=" + this.getFrmID() + paras;
			break;
		case "Group": // 分组分析界面.
			url = "../Comm/Group.htm?EnsName=" + this.getFrmID() + paras;
			break;
		default:
			break;
		}
		return "url@" + url;
	}

	/// #endregion

}