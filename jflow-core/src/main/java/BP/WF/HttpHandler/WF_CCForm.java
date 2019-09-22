package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Sys.*;
import BP.Tools.*;
import BP.Port.*;
import BP.Web.*;
import BP.WF.Template.*;
import BP.WF.XML.*;
import BP.En.*;
import ICSharpCode.SharpZipLib.Zip.*;
import LitJson.*;
import BP.NetPlatformImpl.*;
import BP.WF.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.time.*;

/** 
 表单功能界面
*/
public class WF_CCForm extends DirectoryPageBase
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 多附件.
	public final String Ath_Init()
	{
		try
		{
			DataSet ds = new DataSet();

			FrmAttachment athDesc = this.GenerAthDesc();

			//查询出来数据实体.
			String pkVal = this.getPKVal();
			if (athDesc.HisCtrlWay == AthCtrlWay.FID)
			{
				pkVal = String.valueOf(this.getFID());
			}

			BP.Sys.FrmAttachmentDBs dbs = BP.WF.Glo.GenerFrmAttachmentDBs(athDesc, pkVal, this.getFK_FrmAttachment(), this.getWorkID(), this.getFID(), this.getPWorkID());

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 如果图片显示.(先不考虑.)
			if (athDesc.FileShowWay == FileShowWay.Pict)
			{
				/* 如果是图片轮播，就在这里根据数据输出轮播的html代码.*/
				if (dbs.size() == 0 && athDesc.IsUpload == true)
				{
					/*没有数据并且，可以上传,就转到上传的界面上去.*/
					return "url@AthImg.htm?1=1" + this.getRequestParas();
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 如果图片显示.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 执行装载模版.
			if (dbs.size() == 0 && athDesc.IsWoEnableTemplete == true)
			{
				/*如果数量为0,就检查一下是否有模版如果有就加载模版文件.*/
				String templetePath = BP.Sys.SystemConfig.PathOfDataUser + "AthTemplete\\" + athDesc.NoOfObj.trim();
				if ((new File(templetePath)).isDirectory() == false)
				{
					(new File(templetePath)).mkdirs();
				}

				/*有模版文件夹*/
				File mydir = new File(templetePath);
				File[] fls = mydir.GetFiles();
				if (fls.length == 0)
				{
					throw new RuntimeException("@流程设计错误，该多附件启用了模版组件，模版目录:" + templetePath + "里没有模版文件.");
				}

				for (File fl : fls.ToJavaList())
				{
					if ((new File(athDesc.SaveTo)).isDirectory() == false)
					{
						(new File(athDesc.SaveTo)).mkdirs();
					}

					int oid = BP.DA.DBAccess.GenerOID();
					String saveTo = athDesc.SaveTo + "\\" + oid + "." + fl.getName().substring(fl.getName().lastIndexOf('.') + 1);
					if (saveTo.contains("@") == true || saveTo.contains("*") == true)
					{
						/*如果有变量*/
						saveTo = saveTo.replace("*", "@");
						if (saveTo.contains("@") && this.getFK_Node() != 0)
						{
							/*如果包含 @ */
							BP.WF.Flow flow = new BP.WF.Flow(this.getFK_Flow());
							BP.WF.Data.GERpt myen = flow.getHisGERpt();
							myen.setOID(this.getWorkID());
							myen.RetrieveFromDBSources();
							saveTo = BP.WF.Glo.DealExp(saveTo, myen, null);
						}
						if (saveTo.contains("@") == true)
						{
							throw new RuntimeException("@路径配置错误,变量没有被正确的替换下来." + saveTo);
						}
					}
					fl.CopyTo(saveTo);

					File info = new File(saveTo);
					FrmAttachmentDB dbUpload = new FrmAttachmentDB();

					dbUpload.CheckPhysicsTable();
					dbUpload.setMyPK( athDesc.FK_MapData + String.valueOf(oid);
					dbUpload.NodeID = String.valueOf(getFK_Node());
					dbUpload.FK_FrmAttachment = this.getFK_FrmAttachment();

					if (athDesc.AthUploadWay == AthUploadWay.Inherit)
					{
						/*如果是继承，就让他保持本地的PK. */
						dbUpload.RefPKVal = this.getPKVal().toString();
					}

					if (athDesc.AthUploadWay == AthUploadWay.Interwork)
					{
						/*如果是协同，就让他是PWorkID. */
						Paras ps = new Paras();
						ps.SQL = "SELECT PWorkID FROM WF_GenerWorkFlow WHERE WorkID=" + SystemConfig.getAppCenterDBVarStr() + "WorkID";
						ps.Add("WorkID", this.getPKVal());
						String pWorkID = BP.DA.DBAccess.RunSQLReturnValInt(ps, 0).toString();
						if (pWorkID == null || pWorkID.equals("0"))
						{

							pWorkID = this.getPKVal();
						}
						dbUpload.RefPKVal = pWorkID;
					}

					dbUpload.FK_MapData = athDesc.FK_MapData;
					dbUpload.FK_FrmAttachment = this.getFK_FrmAttachment();

					dbUpload.FileExts = info.Extension;
					dbUpload.FileFullName = saveTo;
					dbUpload.FileName = fl.getName();
					dbUpload.FileSize = (float)info.length();

					dbUpload.RDT = DataType.getCurrentDataTime();
					dbUpload.Rec = WebUser.getNo();
					dbUpload.RecName = WebUser.getName();

					dbUpload.Insert();

					dbs.AddEntity(dbUpload);
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 执行装载模版.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 处理权限问题.
			// 处理权限问题, 有可能当前节点是可以上传或者删除，但是当前节点上不能让此人执行工作。
			// bool isDel = athDesc.IsDeleteInt == 0 ? false : true;
			boolean isDel = athDesc.HisDeleteWay == AthDeleteWay.None ? false : true;
			boolean isUpdate = athDesc.IsUpload;
			//if (isDel == true || isUpdate == true)
			//{
			//    if (this.WorkID != 0
			//        && DataType.IsNullOrEmpty(this.FK_Flow) == false
			//        && this.FK_Node != 0)
			//    {
			//        isDel = BP.WF.Dev2Interface.Flow_IsCanDoCurrentWork(this.FK_Flow, this.FK_Node, this.WorkID, WebUser.getNo());
			//        if (isDel == false)
			//            isUpdate = false;
			//    }
			//}
			athDesc.IsUpload = isUpdate;
			//athDesc.HisDeleteWay = AthDeleteWay.DelAll; 
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 处理权限问题.

			String sort = athDesc.Sort.trim();
			if (sort.contains("SELECT") == true || sort.contains("select") == true)
			{
				String sql = BP.WF.Glo.DealExp(sort, null, null);
				DataTable dt = DBAccess.RunSQLReturnTable(sql);
				String strs = "";
				for (DataRow dr : dt.Rows)
				{
					strs += dr.get(0) + ",";
				}
				athDesc.Sort = strs;
			}


			//增加附件描述.
			ds.Tables.add(athDesc.ToDataTableField("AthDesc"));

			//增加附件.
			ds.Tables.add(dbs.ToDataTableField("DBAths"));

			//返回.
			return BP.Tools.Json.ToJson(ds);
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 多附件.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region HanderMapExt
	/** 
	 扩展处理.
	 
	 @return 
	*/
	public final String HandlerMapExt()
	{
		String fk_mapExt = this.GetRequestVal("FK_MapExt").toString();
		if (DataType.IsNullOrEmpty(this.GetRequestVal("Key")))
		{
			return "";
		}

		String oid = this.GetRequestVal("OID");

		String kvs = this.GetRequestVal("KVs");

		BP.Sys.MapExt me = new BP.Sys.MapExt(fk_mapExt);
		DataTable dt = null;
		String sql = "";
		String key = this.GetRequestVal("Key");
		key = System.Web.HttpUtility.UrlDecode(key, System.Text.Encoding.GetEncoding("GB2312"));
		key = key.trim();
		key = key.replace("'", ""); //去掉单引号.

		// key = "周";
		switch (me.ExtType)
		{
			case BP.Sys.MapExtXmlList.ActiveDDL: // 动态填充ddl.
				sql = this.DealSQL(me.DocOfSQLDeal, key);
				if (sql.contains("@") == true)
				{
					for (String strKey : HttpContextHelper.RequestParamKeys)
					{
						sql = sql.replace("@" + strKey, HttpContextHelper.RequestParams(strKey));
					}
				}
				dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
				return JSONTODT(dt);
			case BP.Sys.MapExtXmlList.AutoFullDLL: //填充下拉框
			case BP.Sys.MapExtXmlList.TBFullCtrl: // 自动完成。
			case BP.Sys.MapExtXmlList.DDLFullCtrl: // 级连ddl.
			case BP.Sys.MapExtXmlList.FullData: // 填充其他控件.
				switch (this.GetRequestVal("DoTypeExt"))
				{
					case "ReqCtrl":
						// 获取填充 ctrl 值的信息.
						sql = this.DealSQL(me.DocOfSQLDeal, key);
						//System.Web.HttpContext.Current.Session["DtlKey"] = key;
						HttpContextHelper.SessionSet("DtlKey", key);
						dt = BP.DA.DBAccess.RunSQLReturnTable(sql);

						return JSONTODT(dt);
						break;
					case "ReqDtlFullList":
						/* 获取填充的从表集合. */
						DataTable dtDtl = new DataTable("Head");
						dtDtl.Columns.Add("Dtl", String.class);
						String[] strsDtl = me.Tag1.split("[$]", -1);
						for (String str : strsDtl)
						{
							if (DataType.IsNullOrEmpty(str))
							{
								continue;
							}

							String[] ss = str.split("[:]", -1);
							String fk_dtl = ss[0];
							if (ss[1].equals("") || ss[1] == null)
							{
								continue;
							}
							//string dtlKey = System.Web.HttpContext.Current.Session["DtlKey"] as string;
							Object tempVar = HttpContextHelper.SessionGet("DtlKey");
							String dtlKey = tempVar instanceof String ? (String)tempVar : null;
							if (dtlKey == null)
							{
								dtlKey = key;
							}
							String mysql = DealSQL(ss[1], dtlKey);

							GEDtls dtls = new GEDtls(fk_dtl);
							MapDtl dtl = new MapDtl(fk_dtl);

							DataTable dtDtlFull = DBAccess.RunSQLReturnTable(mysql);
							BP.DA.DBAccess.RunSQL("DELETE FROM " + dtl.getPTable() + " WHERE RefPK=" + oid);
							for (DataRow dr : dtDtlFull.Rows)
							{
								BP.Sys.GEDtl mydtl = new GEDtl(fk_dtl);
								//  mydtl.OID = dtls.size() + 1;
								dtls.AddEntity(mydtl);
								for (DataColumn dc : dtDtlFull.Columns)
								{
									mydtl.SetValByKey(dc.ColumnName, dr.get(dc.ColumnName).toString());
								}
								mydtl.RefPKInt = Integer.parseInt(oid);
								if (mydtl.OID > 100)
								{
									mydtl.InsertAsOID(mydtl.OID);
								}
								else
								{
									mydtl.OID = 0;
									mydtl.Insert();
								}

							}
							DataRow drRe = dtDtl.NewRow();
							drRe.set(0, fk_dtl);
							dtDtl.Rows.add(drRe);
						}
						return JSONTODT(dtDtl);
						break;
					case "ReqDDLFullList":
						/* 获取要个性化填充的下拉框. */
						DataTable dt1 = new DataTable("Head");
						dt1.Columns.Add("DDL", String.class);
						//    dt1.Columns.Add("SQL", typeof(string));
						if (DataType.IsNullOrEmpty(me.Tag) == false)
						{
							String[] strs = me.Tag.split("[$]", -1);
							for (String str : strs)
							{
								if (str.equals("") || str == null)
								{
									continue;
								}

								String[] ss = str.split("[:]", -1);
								DataRow dr = dt1.NewRow();
								dr.set(0, ss[0]);
								// dr[1] = ss[1];
								dt1.Rows.add(dr);
							}
							return JSONTODT(dt1);
						}
						return "";
						break;
					case "ReqDDLFullListDB":
						/* 获取要个性化填充的下拉框的值. 根据已经传递过来的 ddl id. */
						String myDDL = this.GetRequestVal("MyDDL");
						sql = me.DocOfSQLDeal;
						String[] strs1 = me.Tag.split("[$]", -1);
						for (String str : strs1)
						{
							if (str.equals("") || str == null)
							{
								continue;
							}

							String[] ss = str.split("[:]", -1);
							if (myDDL.equals(ss[0]) && ss.length == 2)
							{
								sql = ss[1];
								sql = this.DealSQL(sql, key);
								break;
							}
						}
						dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
						return JSONTODT(dt);
						break;
					default:
						key = key.replace("'", "");

						sql = this.DealSQL(me.DocOfSQLDeal, key);

						dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
						return JSONTODT(dt);
						break;
				}
				return "";
			default:
				return "err@没有解析的标记" + me.ExtType;
		}

		return "err@没有解析的标记" + me.ExtType;
	}
	/** 
	 处理sql.
	 
	 @param sql 要执行的sql
	 @param key 关键字值
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


		sql = sql.replace("@WebUser.getNo()", WebUser.getNo());
		sql = sql.replace("@WebUser.getName()", WebUser.getName());
		sql = sql.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());

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
			for (String mykey : HttpContextHelper.RequestParamKeys)
			{
				sql = sql.replace("@" + mykey, HttpContextHelper.RequestParams(mykey));
			}
		}

		dealSQL = sql;
		return sql;
	}
	private String dealSQL = "";
	public final String JSONTODT(DataTable dt)
	{
		//  return BP.Tools.Json.ToJson(dt);

		if ((BP.Sys.SystemConfig.getAppCenterDBType() == DBType.Informix || BP.Sys.SystemConfig.getAppCenterDBType() == DBType.Oracle) && dealSQL != null)
		{
			/*如果数据库不区分大小写, 就要按用户输入的sql进行二次处理。*/
			String mysql = dealSQL.trim();
			if (mysql.equals(""))
			{
				return "";
			}

			mysql = mysql.substring(6, mysql.toLowerCase().indexOf("from"));
			mysql = mysql.replace(",", "|");
			String[] strs = mysql.split("[|]", -1);
			String[] pstr = null;
			String ns = null;

			for (String s : strs)
			{
				if (DataType.IsNullOrEmpty(s))
				{
					continue;
				}
				//处理ORACLE中获取字段使用别名的情况，使用别名的字段，取别名
				ns = s.trim();
				pstr = ns.split(" ".toCharArray(), StringSplitOptions.RemoveEmptyEntries);
				if (pstr.length > 1)
				{
					ns = pstr[pstr.length - 1].replace("\"", "");
				}

				for (DataColumn dc : dt.Columns)
				{
					if (dc.ColumnName.toLowerCase().equals(ns.toLowerCase()))
					{
						dc.ColumnName = ns;
						break;
					}
				}
			}
		}
		else
		{
			for (DataColumn dc : dt.Columns)
			{
				if (dc.ColumnName.toLowerCase().equals("no"))
				{
					dc.ColumnName = "No";
					continue;
				}
				if (dc.ColumnName.toLowerCase().equals("name"))
				{
					dc.ColumnName = "Name";
					continue;
				}
			}
		}

		StringBuilder JsonString = new StringBuilder();
		JsonString.append("{ ");
		JsonString.append("\"Head\":[ ");

		if (dt != null && dt.Rows.size() > 0)
		{
			for (int i = 0; i < dt.Rows.size(); i++)
			{
				JsonString.append("{ ");
				for (int j = 0; j < dt.Columns.size(); j++)
				{
					if (j < dt.Columns.size() - 1)
					{
						JsonString.append("\"" + dt.Columns[j].ColumnName.toString() + "\":\"" + dt.Rows[i][j].toString() + "\",");
					}
					else if (j == dt.Columns.size() - 1)
					{
						JsonString.append("\"" + dt.Columns[j].ColumnName.toString() + "\":\"" + dt.Rows[i][j].toString() + "\"");
					}
				}
				/*end Of String*/
				if (i == dt.Rows.size() - 1)
				{
					JsonString.append("} ");
				}
				else
				{
					JsonString.append("}, ");
				}
			}
		}

		JsonString.append("]}");

		return JsonString.toString();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion HanderMapExt

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 执行父类的重写方法.

	/** 
	 构造函数
	*/
	public WF_CCForm()
	{
	}
	/** 
	 默认执行的方法
	 
	 @return 
	*/
	@Override
	protected String DoDefaultMethod()
	{
		//找不不到标记就抛出异常.
		throw new RuntimeException("@标记[" + this.getDoType() + "]，没有找到.@原始URL:" + HttpContextHelper.RequestRawUrl);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 执行父类的重写方法.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region frm.htm 主表.
	/** 
	 执行数据初始化
	 
	 @return 
	*/
	public final String Frm_Init()
	{
		boolean IsMobile = GetRequestValBoolen("IsMobile");
		if (this.GetRequestVal("IsTest") != null)
		{
			MapData mymd = new MapData(this.getEnsName());
			mymd.RepairMap();
			BP.Sys.SystemConfig.DoClearCash();
		}

		MapData md = new MapData(this.getEnsName());

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 判断是否是返回的URL.
		if (md.HisFrmType == FrmType.Url)
		{
			String no = this.GetRequestVal("NO");
			String urlParas = "OID=" + this.getRefOID() + "&NO=" + no + "&WorkID=" + this.getWorkID() + "&FK_Node=" + this.getFK_Node() + "&UserNo=" + WebUser.getNo() + "&SID=" + this.getSID();

			String url = "";
			/*如果是URL.*/
			if (md.Url.Contains("?") == true)
			{
				url = md.Url + "&" + urlParas;
			}
			else
			{
				url = md.Url + "?" + urlParas;
			}

			return "url@" + url;
		}

		if (md.HisFrmType == FrmType.Entity)
		{
			String no = this.GetRequestVal("NO");
			String urlParas = "OID=" + this.getRefOID() + "&NO=" + no + "&WorkID=" + this.getWorkID() + "&FK_Node=" + this.getFK_Node() + "&UserNo=" + WebUser.getNo() + "&SID=" + this.getSID();

			BP.En.Entities ens = BP.En.ClassFactory.GetEns(md.PTable);

			BP.En.Entity en = ens.GetNewEntity;

			if (en.IsOIDEntity == true)
			{
				BP.En.EntityOID enOID = null;

				enOID = en instanceof BP.En.EntityOID ? (BP.En.EntityOID)en : null;
				if (enOID == null)
				{
					return "err@系统错误，无法将" + md.PTable + "转化成BP.En.EntityOID.";
				}

				enOID.SetValByKey("OID", this.getWorkID());

				if (en.RetrieveFromDBSources() == 0)
				{
					for (String key : HttpContextHelper.RequestParamKeys)
					{
						enOID.SetValByKey(key, HttpContextHelper.RequestParams(key));
					}
					enOID.SetValByKey("OID", this.getWorkID());

					enOID.InsertAsOID(this.getWorkID());
				}
			}
			return "url@../Comm/En.htm?EnName=" + md.PTable + "&PKVal=" + this.getWorkID();
		}

		if (md.HisFrmType == FrmType.VSTOForExcel && this.GetRequestVal("IsFreeFrm") == null)
		{
			String url = "FrmVSTO.htm?1=1" + this.getRequestParasOfAll();
			url = url.replace("&&", "&");
			return "url@" + url;
		}

		if (md.HisFrmType == FrmType.WordFrm)
		{
			String no = this.GetRequestVal("NO");
			String urlParas = "OID=" + this.getRefOID() + "&NO=" + no + "&WorkID=" + this.getWorkID() + "&FK_Node=" + this.getFK_Node() + "&UserNo=" + WebUser.getNo() + "&SID=" + this.getSID() + "&FK_MapData=" + this.getFK_MapData() + "&OIDPKVal=" + this.getOID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow();
			/*如果是URL.*/
			String requestParas = this.getRequestParasOfAll();
			String[] parasArrary = this.getRequestParasOfAll().split("[&]", -1);
			for (String str : parasArrary)
			{
				if (DataType.IsNullOrEmpty(str) || str.contains("=") == false)
				{
					continue;
				}
				String[] kvs = str.split("[=]", -1);
				if (urlParas.contains(kvs[0]))
				{
					continue;
				}
				urlParas += "&" + kvs[0] + "=" + kvs[1];
			}
			if (md.Url.Contains("?") == true)
			{
				return "url@FrmWord.aspx?1=2" + "&" + urlParas;
			}
			else
			{
				return "url@FrmWord.aspx" + "?" + urlParas;
			}
		}

		if (md.HisFrmType == FrmType.ExcelFrm)
		{
			return "url@FrmExcel.aspx?1=2" + this.getRequestParasOfAll();
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 判断是否是返回的URL.

		//处理参数.
		String paras = this.getRequestParasOfAll();
		paras = paras.replace("&DoType=Frm_Init", "");

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 流程的独立运行的表单.
		if (this.getFK_Node() != 0 && this.getFK_Node() != 999999)
		{
			BP.WF.Template.FrmNode fn = new FrmNode();
			fn = new FrmNode(this.getFK_Flow(), this.getFK_Node(), this.getFK_MapData());

			if (fn != null && fn.getWhoIsPK() != WhoIsPK.OID)
			{
				//太爷孙关系
				if (fn.getWhoIsPK() == WhoIsPK.PPPWorkID)
				{
					//根据PWorkID 获取PPPWorkID
					String sql = "Select PWorkID From WF_GenerWorkFlow Where WorkID=(Select PWorkID From WF_GenerWorkFlow Where WorkID=" + this.getPWorkID() + ")";
					String pppworkID = DBAccess.RunSQLReturnString(sql);
					if (DataType.IsNullOrEmpty(pppworkID) == true || pppworkID.equals("0"))
					{
						throw new RuntimeException("err@不存在太爷孙流程关系，请联系管理员检查流程设计是否正确");
					}

					long PPPWorkID = Long.parseLong(pppworkID);
					paras = paras.replace("&OID=" + this.getWorkID(), "&OID=" + PPPWorkID);
					paras = paras.replace("&WorkID=" + this.getWorkID(), "&WorkID=" + PPPWorkID);
					paras = paras.replace("&PKVal=" + this.getWorkID(), "&PKVal=" + PPPWorkID);
				}


				if (fn.getWhoIsPK() == WhoIsPK.PPWorkID)
				{
					//根据PWorkID 获取PPWorkID
					GenerWorkFlow gwf = new GenerWorkFlow(this.getPWorkID());
					if (gwf != null && gwf.getPWorkID() != 0)
					{
						paras = paras.replace("&OID=" + this.getWorkID(), "&OID=" + gwf.getPWorkID());
						paras = paras.replace("&WorkID=" + this.getWorkID(), "&WorkID=" + gwf.getPWorkID());
						paras = paras.replace("&PKVal=" + this.getWorkID(), "&PKVal=" + gwf.getPWorkID());
					}
					else
					{
						throw new RuntimeException("err@不存在爷孙流程关系，请联系管理员检查流程设计是否正确");
					}
				}

				if (fn.getWhoIsPK() == WhoIsPK.PWorkID)
				{
					paras = paras.replace("&OID=" + this.getWorkID(), "&OID=" + this.getPWorkID());
					paras = paras.replace("&WorkID=" + this.getWorkID(), "&WorkID=" + this.getPWorkID());
					paras = paras.replace("&PKVal=" + this.getWorkID(), "&PKVal=" + this.getPWorkID());
				}

				if (fn.getWhoIsPK() == WhoIsPK.FID)
				{
					paras = paras.replace("&OID=" + this.getWorkID(), "&OID=" + this.getFID());
					paras = paras.replace("&WorkID=" + this.getWorkID(), "&WorkID=" + this.getFID());
					paras = paras.replace("&PKVal=" + this.getWorkID(), "&PKVal=" + this.getFID());
				}

				if (md.HisFrmType == FrmType.FreeFrm || md.HisFrmType == FrmType.FoolForm)
				{
					if (IsMobile == true)
					{
						return "url@../FrmView.htm?1=2" + paras;
					}

					if (this.GetRequestVal("Readonly").equals("1") || this.GetRequestVal("IsEdit").equals("0"))
					{
						return "url@FrmGener.htm?1=2" + paras;
					}
					else
					{
						return "url@FrmGener.htm?1=2" + paras;
					}
				}

				if (md.HisFrmType == FrmType.VSTOForExcel || md.HisFrmType == FrmType.ExcelFrm)
				{
					if (this.GetRequestVal("Readonly").equals("1") || this.GetRequestVal("IsEdit").equals("0"))
					{
						return "url@FrmVSTO.htm?1=2" + paras;
					}
					else
					{
						return "url@FrmVSTO.htm?1=2" + paras;
					}
				}

				if (IsMobile == true)
				{
					return "url@../FrmView.htm?1=2" + paras;
				}

				if (this.GetRequestVal("Readonly").equals("1") || this.GetRequestVal("IsEdit").equals("0"))
				{
					return "url@FrmGener.htm?1=2" + paras;
				}
				else
				{
					return "url@FrmGener.htm?1=2" + paras;
				}
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 非流程的独立运行的表单.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 非流程的独立运行的表单.

		if (md.HisFrmType == FrmType.FreeFrm)
		{
			if (IsMobile == true)
			{
				return "url@../FrmView.htm?1=2" + paras;
			}
			if (this.GetRequestVal("Readonly").equals("1") || this.GetRequestVal("IsEdit").equals("0"))
			{
				return "url@FrmGener.htm?1=2" + paras;
			}
			else
			{
				return "url@FrmGener.htm?1=2" + paras;
			}
		}

		if (md.HisFrmType == FrmType.VSTOForExcel || md.HisFrmType == FrmType.ExcelFrm)
		{
			if (this.GetRequestVal("Readonly").equals("1") || this.GetRequestVal("IsEdit").equals("0"))
			{
				return "url@FrmVSTO.htm?1=2" + paras;
			}
			else
			{
				return "url@FrmVSTO.htm?1=2" + paras;
			}
		}

		if (IsMobile == true)
		{
			return "url@../FrmView.htm?1=2" + paras;
		}

		if (this.GetRequestVal("Readonly").equals("1") || this.GetRequestVal("IsEdit").equals("0"))
		{
			return "url@FrmGener.htm?1=2" + paras;
		}
		else
		{
			return "url@FrmGener.htm?1=2" + paras;
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 非流程的独立运行的表单.

	}

	/** 
	 附件图片
	 
	 @return 
	*/
	public final String FrmImgAthDB_Init()
	{
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
	 上传编辑图片
	 
	 @return 
	*/
	public final String FrmImgAthDB_Upload()
	{
		String CtrlID = this.GetRequestVal("CtrlID");
		int zoomW = this.GetRequestValInt("zoomW");
		int zoomH = this.GetRequestValInt("zoomH");

		//HttpFileCollection files = this.context.Request.Files;
		if (HttpContextHelper.RequestFilesCount > 0)
		{
			String myName = "";
			String fk_mapData = this.getFK_MapData();
			if (fk_mapData.contains("ND") == true)
			{
				myName = CtrlID + "_" + this.getRefPKVal();
			}
			else
			{
				myName = fk_mapData + "_" + CtrlID + "_" + this.getRefPKVal();
			}

			//生成新路径，解决返回相同src后图片不切换问题
			//string newName = ImgAthPK + "_" + this.getMyPK() + "_" + DateTime.Now.ToString("yyyyMMddHHmmss");
			String webPath = BP.WF.Glo.getCCFlowAppPath() + "DataUser/ImgAth/Data/" + myName + ".png";
			//string saveToPath = this.context.Server.MapPath(BP.WF.Glo.CCFlowAppPath + "DataUser/ImgAth/Data");
			String saveToPath = SystemConfig.PathOfWebApp + (BP.WF.Glo.getCCFlowAppPath() + "DataUser/ImgAth/Data");
			String fileUPloadPath = SystemConfig.PathOfWebApp + (BP.WF.Glo.getCCFlowAppPath() + "DataUser/ImgAth/Upload");
			//创建路径
			if (!(new File(saveToPath)).isDirectory())
			{
				(new File(saveToPath)).mkdirs();
			}
			if (!(new File(fileUPloadPath)).isDirectory())
			{
				(new File(fileUPloadPath)).mkdirs();
			}

			saveToPath = saveToPath + "\\" + myName + ".png";
			fileUPloadPath = fileUPloadPath + "\\" + myName + ".png";
			//files[0].SaveAs(saveToPath);
			HttpContextHelper.UploadFile(HttpContextHelper.RequestFiles(0), saveToPath);

			//源图像  
			System.Drawing.Bitmap oldBmp = new System.Drawing.Bitmap(saveToPath);

			//新图像,并设置新图像的宽高  
			System.Drawing.Bitmap newBmp = new System.Drawing.Bitmap(zoomW, zoomH);
			System.Drawing.Graphics draw = System.Drawing.Graphics.FromImage(newBmp); //从新图像获取对应的Graphics
			System.Drawing.Rectangle rect = new System.Drawing.Rectangle(0, 0, zoomW, zoomH); //指定绘制新图像的位置和大小
			draw.DrawImage(oldBmp, rect); //把源图像的全部完整的内容，绘制到新图像rect这个区域内，

			draw.Dispose();
			oldBmp.Dispose(); //一定要把源图Dispose调，因为保存的是相同路径，需要把之前的图顶替调，如果不释放的话会报错：（GDI+ 中发生一般性错误。）
			newBmp.Save(saveToPath); //保存替换到同一个路径

			//复制一份
			Files.copy(Paths.get(saveToPath), Paths.get(fileUPloadPath), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
			////获取文件大小
			//FileInfo fileInfo = new FileInfo(saveToPath);
			//float fileSize = 0;
			//if (fileInfo.Exists)
			//    fileSize = float.Parse(fileInfo.Length.ToString());

			////更新数据表                
			//FrmImgAthDB imgAthDB = new FrmImgAthDB();
			//imgAthDB.setMyPK( myName;
			//imgAthDB.FK_MapData = this.FK_MapData;
			//imgAthDB.FK_FrmImgAth = ImgAthPK;
			//imgAthDB.RefPKVal = this.MyPK;
			//imgAthDB.FileFullName = webPath;
			//imgAthDB.FileName = newName;
			//imgAthDB.FileExts = "png";
			//imgAthDB.FileSize = fileSize;
			//imgAthDB.RDT = DateTime.Now.ToString("yyyy-MM-dd mm:HH");
			//imgAthDB.Rec = WebUser.getNo();
			//imgAthDB.RecName = WebUser.getName();
			//imgAthDB.Save();
			return "{SourceImage:\"" + webPath + "\"}";
		}
		return "{err:\"没有选择文件\"}";
	}

	/** 
	 剪切图片
	 
	 @return 
	*/
	public final String FrmImgAthDB_Cut()
	{
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
		//string newName = ImgAthPK + "_" + this.getMyPK() + "_" + DateTime.Now.ToString("yyyyMMddHHmmss");
		String webPath = BP.WF.Glo.getCCFlowAppPath() + "DataUser/ImgAth/Data/" + newName + ".png";
		String savePath = SystemConfig.CCFlowAppPath + "DataUser/ImgAth/Data/" + newName + ".png";
		//获取上传的大图片
		//string strImgPath = this.context.Server.MapPath(SystemConfig.CCFlowWebPath + "DataUser/ImgAth/Upload/" + newName + ".png");
		String strImgPath = SystemConfig.PathOfWebApp + SystemConfig.CCFlowWebPath + "DataUser/ImgAth/Upload/" + newName + ".png";
		if ((new File(strImgPath)).isFile() == true)
		{
			//剪切图
			boolean bSuc = Crop(strImgPath, savePath, w, h, x, y);
			//imgAthDB.FileFullName = webPath;
			//imgAthDB.Update();
			return webPath;
		}
		return webPath;
	}

	/** 
	 剪裁图像
	 
	 @param Img
	 @param Width
	 @param Height
	 @param X
	 @param Y
	 @return 
	*/
	private boolean Crop(String Img, String savePath, int Width, int Height, int X, int Y)
	{
		try
		{
			try (System.Drawing.Bitmap OriginalImage = new System.Drawing.Bitmap(Img))
			{
				try (System.Drawing.Bitmap bmp = new System.Drawing.Bitmap(Width, Height, OriginalImage.PixelFormat))
				{
					bmp.SetResolution(OriginalImage.HorizontalResolution, OriginalImage.VerticalResolution);
					try (System.Drawing.Graphics Graphic = System.Drawing.Graphics.FromImage(bmp))
					{
						Graphic.SmoothingMode = System.Drawing.Drawing2D.SmoothingMode.AntiAlias;
						Graphic.InterpolationMode = System.Drawing.Drawing2D.InterpolationMode.HighQualityBicubic;
						Graphic.PixelOffsetMode = System.Drawing.Drawing2D.PixelOffsetMode.HighQuality;
						Graphic.DrawImage(OriginalImage, new System.Drawing.Rectangle(0, 0, Width, Height), X, Y, Width, Height, System.Drawing.GraphicsUnit.Pixel);
						//var ms = new MemoryStream();
						bmp.Save(savePath);
						//return ms.GetBuffer();
						return true;
					}
				}
			}
		}
		catch (RuntimeException Ex)
		{
			throw (Ex);
		}
		return false;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion frm.htm 主表.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region DtlFrm
	public final String DtlFrm_Init()
	{
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
		if (BP.Sys.SystemConfig.IsBSsystem == true)
		{
			// 处理传递过来的参数。
			for (String k : HttpContextHelper.RequestParamKeys)
			{
				en.SetValByKey(k, HttpContextHelper.RequestParams(k));
			}
		}



		//设置主键.
		en.OID = DBAccess.GenerOID(this.getEnsName());
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 处理权限方案。
		if (this.getFK_Node() != 0 && this.getFK_Node() != 999999)
		{
			Node nd = new Node(this.getFK_Node());
			if (nd.getHisFormType() == NodeFormType.SheetTree || nd.getHisFormType() == NodeFormType.RefOneFrmTree)
			{
				FrmNode fn = new FrmNode(nd.getFK_Flow(), nd.getNodeID(), this.getFK_MapData());
				if (fn.getFrmSln() == FrmSln.Self)
				{
					String no = this.getEnsName() + "_" + nd.getNodeID();
					MapDtl mdtlSln = new MapDtl();
					mdtlSln.No = no;
					int result = mdtlSln.RetrieveFromDBSources();
					if (result != 0)
					{
						dtl = mdtlSln;
					}
				}
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 处理权限方案。
		//给从表赋值.
		switch (dtl.DtlOpenType)
		{
			case DtlOpenType.ForEmp: // 按人员来控制.

				en.SetValByKey("RefPK", this.getRefPKVal());
				en.SetValByKey("FID", this.getRefPKVal());
				break;
			case DtlOpenType.ForWorkID: // 按工作ID来控制
				en.SetValByKey("RefPK", this.getRefPKVal());
				en.SetValByKey("FID", this.getRefPKVal());
				break;
			case DtlOpenType.ForFID: // 按流程ID来控制.
				en.SetValByKey("RefPK", this.getRefPKVal());
				en.SetValByKey("FID", this.getFID());
				break;
		}
		en.SetValByKey("RefPK", this.getRefPKVal());

		en.Insert();

		return "url@DtlFrm.htm?EnsName=" + this.getEnsName() + "&RefPKVal=" + this.getRefPKVal() + "&FrmType=" + (int)dtl.HisEditModel + "&OID=" + en.OID;
	}

	public final String DtlFrm_Delete()
	{
		try
		{
			GEEntity en = new GEEntity(this.getEnsName());
			en.OID = this.getOID();
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

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion DtlFrm

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region frmFree
	/** 
	 实体类的初始化
	 
	 @return 
	*/
	public final String FrmGener_Init_ForBPClass()
	{
		try
		{

			MapData md = new MapData(this.getEnsName());
			DataSet ds = BP.Sys.CCFormAPI.GenerHisDataSet(md.No);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 把主表数据放入.
			String atParas = "";
			Entities ens = ClassFactory.GetEns(this.getEnsName());
			Entity en = ens.GetNewEntity;
			en.PKVal = this.getPKVal();

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
					if (en.Row.ContainsKey(key) == true) //有就该变.
					{
						en.Row[key] = ap.GetValStrByKey(key);
					}
					else
					{
						en.Row.Add(key, ap.GetValStrByKey(key)); //增加他.
					}
				}
			}

			if (BP.Sys.SystemConfig.IsBSsystem == true)
			{
				// 处理传递过来的参数。
				for (String k : HttpContextHelper.RequestParamKeys)
				{
					en.SetValByKey(k, HttpContextHelper.RequestParams(k));
				}
			}

			// 执行表单事件. FrmLoadBefore .
			String msg = md.DoEvent(FrmEventList.FrmLoadBefore, en);
			if (DataType.IsNullOrEmpty(msg) == false)
			{
				return "err@错误:" + msg;
			}


			//重设默认值.
			en.ResetDefaultVal();

			//执行装载填充.
			MapExt me = new MapExt();
			if (me.Retrieve(MapExtAttr.ExtType, MapExtXmlList.PageLoadFull, MapExtAttr.FK_MapData, this.getEnsName()) == 1)
			{
				//执行通用的装载方法.
				MapAttrs attrs = new MapAttrs(this.getEnsName());
				MapDtls dtls = new MapDtls(this.getEnsName());
				Entity tempVar = BP.WF.Glo.DealPageLoadFull(en, me, attrs, dtls);
				en = tempVar instanceof GEEntity ? (GEEntity)tempVar : null;
			}

			//执行事件
			md.DoEvent(FrmEventList.SaveBefore, en, null);


			//增加主表数据.
			DataTable mainTable = en.ToDataTableField(md.No);
			mainTable.TableName = "MainTable";

			ds.Tables.add(mainTable);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 把主表数据放入.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 把外键表加入DataSet
			DataTable dtMapAttr = ds.Tables["Sys_MapAttr"];

			MapExts mes = md.MapExts;
			DataTable ddlTable = new DataTable();
			ddlTable.Columns.Add("No");
			for (DataRow dr : dtMapAttr.Rows)
			{
				String lgType = dr.get("LGType").toString();
				if (lgType.equals("2") == false)
				{
					continue;
				}

				String UIIsEnable = dr.get("UIVisible").toString();
				if (UIIsEnable.equals("0"))
				{
					continue;
				}

				//string lgType = dr[MapAttrAttr.LGType].ToString();
				//if (lgType == "0")
				//    continue

				String uiBindKey = dr.get("UIBindKey").toString();
				if (DataType.IsNullOrEmpty(uiBindKey) == true)
				{
					String myPK = dr.get("MyPK").toString();
					/*如果是空的*/
					//   throw new Exception("@属性字段数据不完整，流程:" + fl.No + fl.Name + ",节点:" + nd.NodeID + nd.Name + ",属性:" + myPK + ",的UIBindKey IsNull ");
				}

				// 检查是否有下拉框自动填充。
				String keyOfEn = dr.get("KeyOfEn").toString();
				String fk_mapData = dr.get("FK_MapData").toString();

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#region 处理下拉框数据范围. for 小杨.
				Object tempVar2 = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL, MapExtAttr.AttrOfOper, keyOfEn);
				me = tempVar2 instanceof MapExt ? (MapExt)tempVar2 : null;
				if (me != null)
				{
					Object tempVar3 = me.Doc.Clone();
					String fullSQL = tempVar3 instanceof String ? (String)tempVar3 : null;
					fullSQL = fullSQL.replace("~", ",");
					fullSQL = BP.WF.Glo.DealExp(fullSQL, en, null);
					DataTable dt = DBAccess.RunSQLReturnTable(fullSQL);
					dt.TableName = keyOfEn; //可能存在隐患，如果多个字段，绑定同一个表，就存在这样的问题.
					ds.Tables.add(dt);
					continue;
				}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion 处理下拉框数据范围.

				// 判断是否存在.
				if (ds.Tables.Contains(uiBindKey) == true)
				{
					continue;
				}

				DataTable dataTable = BP.Sys.PubClass.GetDataTableByUIBineKey(uiBindKey);
				if (dataTable != null)
				{
					ds.Tables.add(dataTable);
				}
				else
				{
					DataRow ddldr = ddlTable.NewRow();
					ddldr.set("No", uiBindKey);
					ddlTable.Rows.add(ddldr);
				}
			}
			ddlTable.TableName = "UIBindKey";
			ds.Tables.add(ddlTable);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion End把外键表加入DataSet

			return BP.Tools.Json.DataSetToJson(ds, false);
		}
		catch (RuntimeException ex)
		{
			GEEntity myen = new GEEntity(this.getEnsName());
			myen.CheckPhysicsTable();

			BP.Sys.CCFormAPI.RepareCCForm(this.getEnsName());
			return "err@装载表单期间出现如下错误,ccform有自动诊断修复功能请在刷新一次，如果仍然存在请联系管理员. @" + ex.getMessage();
		}
	}
	/** 
	 执行数据初始化
	 
	 @return 
	*/
	public final String FrmGener_Init()
	{
		if (this.getFK_MapData() != null && this.getFK_MapData().contains("BP.") == true)
		{
			return FrmGener_Init_ForBPClass();
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 定义流程信息的所用的 配置entity.
		//节点与表单的权限控制.
		FrmNode fn = null;

		//是否启用装载填充？ @袁丽娜,
		boolean isLoadData = true;
		//定义节点变量. @袁丽娜 
		Node nd = null;
		if (this.getFK_Node() != 0 && this.getFK_Node() != 999999)
		{
			nd = new Node(this.getFK_Node());
			nd.WorkID = this.getWorkID(); //为获取表单ID ( NodeFrmID )提供参数.
			fn = new FrmNode(this.getFK_Flow(), this.getFK_Node(), this.getFK_MapData());
			isLoadData = fn.getIsEnableLoadData();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 定义流程信息的所用的 配置entity.

		try
		{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 特殊判断 适应累加表单.
			String fromWhere = this.GetRequestVal("FromWorkOpt");
			if (fromWhere != null && fromWhere.equals("1") && this.getFK_Node() != 0 && this.getFK_Node() != 999999)
			{
				nd = new Node(this.getFK_Node());
				nd.WorkID = this.getWorkID(); //为获取表单ID ( NodeFrmID )提供参数.

				//如果是累加表单.
				if (nd.getHisFormType() == NodeFormType.FoolTruck)
				{
					DataSet myds = BP.WF.CCFlowAPI.GenerWorkNode(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), this.getFID(), WebUser.getNo(), this.GetRequestVal("FromWorkOpt"));

					return BP.Tools.Json.ToJson(myds);
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 特殊判断.适应累加表单.

			MapData md = new MapData(this.getEnsName());
			DataSet ds = BP.Sys.CCFormAPI.GenerHisDataSet(md.No);
			String atParas = "";
			//主表实体.
			GEEntity en = new GEEntity(this.getEnsName());

			long pk = this.getRefOID();
			if (pk == 0)
			{
				pk = this.getOID();
			}
			if (pk == 0)
			{
				pk = this.getWorkID();
			}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 根据who is pk 获取数据.
			en.OID = pk;
			if (en.OID == 0)
			{
				en.ResetDefaultVal();
			}
			else
			{
				if (en.RetrieveFromDBSources() == 0)
				{
					en.Insert();
				}
			}

			//把流程信息表发送过去.
			GenerWorkFlow gwf = new GenerWorkFlow();
			gwf.setWorkID(pk);
			gwf.RetrieveFromDBSources();
			ds.Tables.add(gwf.ToDataTableField("WF_GenerWorkFlow"));

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 根据who is pk 获取数据.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 附加参数数据.
			//把参数放入到 En 的 Row 里面。
			if (DataType.IsNullOrEmpty(atParas) == false)
			{
				AtPara ap = new AtPara(atParas);
				for (String key : ap.getHisHT().keySet())
				{
					if (en.Row.ContainsKey(key) == true) //有就该变.
					{
						en.Row[key] = ap.GetValStrByKey(key);
					}
					else
					{
						en.Row.Add(key, ap.GetValStrByKey(key)); //增加他.
					}
				}
			}

			if (BP.Sys.SystemConfig.IsBSsystem == true)
			{
				// 处理传递过来的参数。
				for (String k : HttpContextHelper.RequestParamKeys)
				{
					en.SetValByKey(k, HttpContextHelper.RequestParams(k));
				}
			}

			// 执行表单事件. FrmLoadBefore .
			String msg = md.DoEvent(FrmEventList.FrmLoadBefore, en);
			if (DataType.IsNullOrEmpty(msg) == false)
			{
				return "err@错误:" + msg;
			}

			//重设默认值.
			if (this.GetRequestValBoolen("IsReadonly") == false)
			{
				en.ResetDefaultVal();
			}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 附加参数数据.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 执行装载填充.与相关的事件.
			MapExt me = null;
			if (isLoadData == true)
			{
				me = new MapExt();
				if (me.Retrieve(MapExtAttr.ExtType, MapExtXmlList.PageLoadFull, MapExtAttr.FK_MapData, this.getEnsName()) == 1)
				{
					//执行通用的装载方法.
					MapAttrs attrs = new MapAttrs(this.getEnsName());
					MapDtls dtls = new MapDtls(this.getEnsName());

					if (GetRequestValInt("IsTest") != 1)
					{
						//判断是否自定义权限.
						boolean IsSelf = false;
						//单据或者是单据实体表单
						if (nd == null)
						{
							Entity tempVar = BP.WF.Glo.DealPageLoadFull(en, me, attrs, dtls, IsSelf, 0, this.getWorkID());
							en = tempVar instanceof GEEntity ? (GEEntity)tempVar : null;
						}
						else
						{
							if ((nd.getHisFormType() == NodeFormType.SheetTree || nd.getHisFormType() == NodeFormType.RefOneFrmTree) && (fn.getFrmSln() == FrmSln.Self))
							{
								IsSelf = true;
							}
							Entity tempVar2 = BP.WF.Glo.DealPageLoadFull(en, me, attrs, dtls, IsSelf, nd.getNodeID(), this.getWorkID());
							en = tempVar2 instanceof GEEntity ? (GEEntity)tempVar2 : null;
						}

					}

				}
			}

			//执行事件
			md.DoEvent(FrmEventList.SaveBefore, en, null);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 执行装载填充.与相关的事件.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 把外键表加入DataSet.
			DataTable dtMapAttr = ds.Tables["Sys_MapAttr"];
			MapExts mes = md.MapExts;
			DataTable ddlTable = new DataTable();
			ddlTable.Columns.Add("No");

			for (DataRow dr : dtMapAttr.Rows)
			{

				String lgType = dr.get("LGType").toString();
				String uiBindKey = dr.get("UIBindKey").toString();

				String uiVisible = dr.get("UIVisible").toString();
				if (uiVisible.equals("0") == true)
				{
					continue;
				}

				if (DataType.IsNullOrEmpty(uiBindKey) == true)
				{
					continue; //为空就continue.
				}

				if (lgType.equals("1") == true)
				{
					continue; //枚举值就continue;
				}

				String uiIsEnable = dr.get("UIIsEnable").toString();
				if (uiIsEnable.equals("0") == true && lgType.equals("1") == true)
				{
					continue; //如果是外键，并且是不可以编辑的状态.
				}

				if (uiIsEnable.equals("0") == true && lgType.equals("0") == true)
				{
					continue; //如果是外部数据源，并且是不可以编辑的状态.
				}


				// 检查是否有下拉框自动填充。
				String keyOfEn = dr.get("KeyOfEn").toString();
				String fk_mapData = dr.get("FK_MapData").toString();

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#region 处理下拉框数据范围. for 小杨.
				Object tempVar3 = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL, MapExtAttr.AttrOfOper, keyOfEn);
				me = tempVar3 instanceof MapExt ? (MapExt)tempVar3 : null;
				if (me != null)
				{
					Object tempVar4 = me.Doc.Clone();
					String fullSQL = tempVar4 instanceof String ? (String)tempVar4 : null;
					fullSQL = fullSQL.replace("~", ",");
					fullSQL = BP.WF.Glo.DealExp(fullSQL, en, null);
					DataTable dt = DBAccess.RunSQLReturnTable(fullSQL);
					dt.TableName = keyOfEn; //可能存在隐患，如果多个字段，绑定同一个表，就存在这样的问题.
					ds.Tables.add(dt);
					continue;
				}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion 处理下拉框数据范围.

				// 判断是否存在.
				if (ds.Tables.Contains(uiBindKey) == true)
				{
					continue;
				}

				DataTable dataTable = BP.Sys.PubClass.GetDataTableByUIBineKey(uiBindKey);

				if (dataTable != null)
				{
					ds.Tables.add(dataTable);
				}
				else
				{
					DataRow ddldr = ddlTable.NewRow();
					ddldr.set("No", uiBindKey);
					ddlTable.Rows.add(ddldr);
				}
			}
			ddlTable.TableName = "UIBindKey";
			ds.Tables.add(ddlTable);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion End把外键表加入DataSet

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 加入组件的状态信息, 在解析表单的时候使用.

			if (this.getFK_Node() != 0 && this.getFK_Node() != 999999 && (fn.getIsEnableFWC() == true || nd.getFrmWorkCheckSta() != FrmWorkCheckSta.Disable))
			{
				BP.WF.Template.FrmNodeComponent fnc = new FrmNodeComponent(nd.getNodeID());
				if (!nd.getNodeFrmID().equals("ND" + nd.getNodeID()))
				{
					/*说明这是引用到了其他节点的表单，就需要把一些位置元素修改掉.*/
				   int refNodeID = 0;
					if (nd.getNodeFrmID().indexOf("ND") == -1)
					{
						refNodeID = nd.getNodeID();
					}
					else
					{
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
					if (md.HisFrmType == FrmType.FoolForm)
					{
						//判断是否是傻瓜表单，如果是，就要判断该傻瓜表单是否有审核组件groupfield ,没有的话就增加上.
						DataTable gf = ds.Tables["Sys_GroupField"];
						boolean isHave = false;
						for (DataRow dr : gf.Rows)
						{
							String cType = dr.get("CtrlType") instanceof String ? (String)dr.get("CtrlType") : null;
							if (cType == null)
							{
								continue;
							}

							if (cType.equals("FWC") == true)
							{
								isHave = true;
							}
						}

						if (isHave == false)
						{
							DataRow dr = gf.NewRow();

							nd.WorkID = this.getWorkID(); //为获取表单ID提供参数.
							dr.set(GroupFieldAttr.OID, 100);
							dr.set(GroupFieldAttr.FrmID, nd.getNodeFrmID());
							dr.set(GroupFieldAttr.CtrlType, "FWC");
							dr.set(GroupFieldAttr.CtrlID, "FWCND" + nd.getNodeID());
							dr.set(GroupFieldAttr.Idx, 100);
							dr.set(GroupFieldAttr.Lab, "审核信息");
							gf.Rows.add(dr);

							ds.Tables.Remove("Sys_GroupField");
							ds.Tables.add(gf);

							//执行更新,就自动生成那个丢失的字段分组.
							refFnc.Update();

						}
					}
				}



//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#region 没有审核组件分组就增加上审核组件分组. @杜需要翻译&测试.
				if (nd.getNodeFrmID().equals("ND" + nd.getNodeID()) && nd.getHisFormType() != NodeFormType.RefOneFrmTree)
				{

					if (md.HisFrmType == FrmType.FoolForm)
					{
						//判断是否是傻瓜表单，如果是，就要判断该傻瓜表单是否有审核组件groupfield ,没有的话就增加上.
						DataTable gf = ds.Tables["Sys_GroupField"];
						boolean isHave = false;
						for (DataRow dr : gf.Rows)
						{
							String cType = dr.get("CtrlType") instanceof String ? (String)dr.get("CtrlType") : null;
							if (cType == null)
							{
								continue;
							}

							if (cType.equals("FWC") == true)
							{
								isHave = true;
							}
						}

						if (isHave == false)
						{
							DataRow dr = gf.NewRow();

							nd.WorkID = this.getWorkID(); //为获取表单ID提供参数.
							dr.set(GroupFieldAttr.OID, 100);
							dr.set(GroupFieldAttr.FrmID, nd.getNodeFrmID());
							dr.set(GroupFieldAttr.CtrlType, "FWC");
							dr.set(GroupFieldAttr.CtrlID, "FWCND" + nd.getNodeID());
							dr.set(GroupFieldAttr.Idx, 100);
							dr.set(GroupFieldAttr.Lab, "审核信息");
							gf.Rows.add(dr);

							ds.Tables.Remove("Sys_GroupField");
							ds.Tables.add(gf);

							//更新,为了让其表单上自动增加审核分组.
							BP.WF.Template.FrmNodeComponent refFnc = new FrmNodeComponent(nd.getNodeID());
							FrmWorkCheck fwc = new FrmWorkCheck(nd.getNodeID());
							if (fn.getFrmSln() == FrmSln.Self || fn.getFrmSln() == FrmSln.Default)
							{
								fwc.setHisFrmWorkCheckSta(FrmWorkCheckSta.Enable);
							}
							else
							{
								fwc.setHisFrmWorkCheckSta(FrmWorkCheckSta.Readonly);
							}

							refFnc.Update();

						}
					}
				}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion 没有审核组件分组就增加上审核组件分组.
				ds.Tables.add(fnc.ToDataTableField("WF_FrmNodeComponent"));
			}
			if (this.getFK_Node() != 0 && this.getFK_Node() != 999999)
			{
				ds.Tables.add(nd.ToDataTableField("WF_Node"));
			}

			if (this.getFK_Node() != 0 && this.getFK_Node() != 999999)
			{
				ds.Tables.add(fn.ToDataTableField("WF_FrmNode"));
			}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 加入组件的状态信息, 在解析表单的时候使用.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 处理权限方案
			if (nd != null && nd.getFormType() == NodeFormType.SheetTree)
			{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#region 只读方案.
				if (fn.getFrmSln() == FrmSln.Readonly)
				{
					for (DataRow dr : dtMapAttr.Rows)
					{
						dr.set(MapAttrAttr.UIIsEnable, 0);
					}

					//改变他的属性. 不知道是否应该这样写？
					ds.Tables.Remove("Sys_MapAttr");
					ds.Tables.add(dtMapAttr);
				}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion 只读方案.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#region 自定义方案.
				if (fn.getFrmSln() == FrmSln.Self)
				{
					//查询出来自定义的数据.
					FrmFields ffs = new FrmFields();
					ffs.Retrieve(FrmFieldAttr.FK_Node, nd.getNodeID(), FrmFieldAttr.FK_MapData, md.No);

					//遍历属性集合.
					for (DataRow dr : dtMapAttr.Rows)
					{
						String keyOfEn = dr.get(MapAttrAttr.KeyOfEn).toString();
						for (FrmField ff : ffs)
						{
							if (ff.getKeyOfEn().equals(keyOfEn) == false)
							{
								continue;
							}

							dr.set(MapAttrAttr.UIIsEnable, ff.getUIIsEnable()); //是否只读?
							dr.set(MapAttrAttr.UIVisible, ff.getUIVisible()); //是否可见?
							dr.set(MapAttrAttr.UIIsInput, ff.getIsNotNull()); //是否必填?


							dr.set(MapAttrAttr.DefVal, ff.getDefVal()); //默认值.

							Attr attr = new Attr();
							attr.MyDataType = DataType.AppString;
							attr.DefaultValOfReal = ff.getDefVal();
							attr.Key = ff.getKeyOfEn();

							if (dr.get(MapAttrAttr.UIIsEnable).toString().equals("0"))
							{
								attr.UIIsReadonly = true;
							}
							else
							{
								attr.UIIsReadonly = false;
							}


							//处理默认值.
							if (DataType.IsNullOrEmpty(ff.getDefVal()) == true)
							{
								continue;
							}

							//数据类型.
							attr.MyDataType = Integer.parseInt(dr.get(MapAttrAttr.MyDataType).toString());
							String v = ff.getDefVal();

							//设置默认值.
							String myval = en.GetValStrByKey(ff.getKeyOfEn());

							// 设置默认值.
							switch (ff.getDefVal())
							{
								case "@WebUser.getNo()":
									if (attr.UIIsReadonly == true)
									{
										en.SetValByKey(attr.Key, Web.WebUser.getNo());
									}
									else
									{
										if (DataType.IsNullOrEmpty(myval) || myval.equals(v))
										{
											en.SetValByKey(attr.Key, Web.WebUser.getNo());
										}
									}
									continue;
								case "@WebUser.getName()":
									if (attr.UIIsReadonly == true)
									{
										en.SetValByKey(attr.Key, Web.WebUser.getName());
									}
									else
									{
										if (DataType.IsNullOrEmpty(myval) || myval.equals(v))
										{
											en.SetValByKey(attr.Key, Web.WebUser.getName());
										}
									}
									continue;
								case "@WebUser.getFK_Dept()":
									if (attr.UIIsReadonly == true)
									{
										en.SetValByKey(attr.Key, Web.WebUser.getFK_Dept());
									}
									else
									{
										if (DataType.IsNullOrEmpty(myval) || myval.equals(v))
										{
											en.SetValByKey(attr.Key, Web.WebUser.getFK_Dept());
										}
									}
									continue;
								case "@WebUser.getFK_DeptName":
									if (attr.UIIsReadonly == true)
									{
										en.SetValByKey(attr.Key, Web.WebUser.getFK_DeptName);
									}
									else
									{
										if (DataType.IsNullOrEmpty(myval) || myval.equals(v))
										{
											en.SetValByKey(attr.Key, Web.WebUser.getFK_DeptName);
										}
									}
									continue;
								case "@WebUser.getFK_DeptNameOfFull":
								case "@WebUser.getFK_DeptFullName":
									if (attr.UIIsReadonly == true)
									{
										en.SetValByKey(attr.Key, Web.WebUser.getFK_DeptNameOfFull);
									}
									else
									{
										if (DataType.IsNullOrEmpty(myval) || myval.equals(v))
										{
											en.SetValByKey(attr.Key, Web.WebUser.getFK_DeptNameOfFull);
										}
									}
									continue;
								case "@RDT":
									if (attr.UIIsReadonly == true)
									{
										if (attr.MyDataType == DataType.AppDate || myval.equals(v))
										{
											en.SetValByKey(attr.Key, DataType.CurrentData);
										}

										if (attr.MyDataType == DataType.AppDateTime || myval.equals(v))
										{
											en.SetValByKey(attr.Key, DataType.getCurrentDataTime());
										}
									}
									else
									{
										if (DataType.IsNullOrEmpty(myval) || myval.equals(v))
										{
											if (attr.MyDataType == DataType.AppDate)
											{
												en.SetValByKey(attr.Key, DataType.CurrentData);
											}
											else
											{
												en.SetValByKey(attr.Key, DataType.getCurrentDataTime());
											}
										}
									}
									continue;
								case "@yyyy年MM月dd日":
								case "@yyyy年MM月dd日HH时mm分":
								case "@yy年MM月dd日":
								case "@yy年MM月dd日HH时mm分":
									if (attr.UIIsReadonly == true)
									{
										en.SetValByKey(attr.Key, LocalDateTime.now().toString(v.replace("@", "")));
									}
									else
									{
										if (DataType.IsNullOrEmpty(myval) || myval.equals(v))
										{
											en.SetValByKey(attr.Key, LocalDateTime.now().toString(v.replace("@", "")));
										}
									}
									continue;
								default:
									continue;
							}
						}
					}

					//改变他的属性. 不知道是否应该这样写？
					ds.Tables.Remove("Sys_MapAttr");
					ds.Tables.add(dtMapAttr);

					//处理radiobutton的模式的控件.
				}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion 自定义方案.

			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 处理权限方案s

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 加入主表的数据.
			//增加主表数据.
			DataTable mainTable = en.ToDataTableField(md.No);
			mainTable.TableName = "MainTable";
			ds.Tables.add(mainTable);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 加入主表的数据.

			String json = BP.Tools.Json.DataSetToJson(ds, false);

			//BP.DA.DataType.WriteFile("c:\\aaa.txt", json);
			return json;
		}
		catch (RuntimeException ex)
		{
			GEEntity myen = new GEEntity(this.getEnsName());
			myen.CheckPhysicsTable();

			BP.Sys.CCFormAPI.RepareCCForm(this.getEnsName());
			return "err@装载表单期间出现如下错误,ccform有自动诊断修复功能请在刷新一次，如果仍然存在请联系管理员. @" + ex.getMessage();
		}
	}
	public final String FrmFreeReadonly_Init()
	{
		try
		{
			MapData md = new MapData(this.getEnsName());
			DataSet ds = BP.Sys.CCFormAPI.GenerHisDataSet(md.No);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 把主表数据放入.
			String atParas = "";
			//主表实体.
			GEEntity en = new GEEntity(this.getEnsName());

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
				FrmNode fn = new FrmNode(this.getFK_Flow(), this.getFK_Node(), this.getFK_MapData());
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion  求who is PK.

			en.OID = pk;
			en.RetrieveFromDBSources();

			//增加主表数据.
			DataTable mainTable = en.ToDataTableField(md.No);
			mainTable.TableName = "MainTable";

			ds.Tables.add(mainTable);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 把主表数据放入.

			return BP.Tools.Json.DataSetToJson(ds, false);
		}
		catch (RuntimeException ex)
		{
			GEEntity myen = new GEEntity(this.getEnsName());
			myen.CheckPhysicsTable();

			BP.Sys.CCFormAPI.RepareCCForm(this.getEnsName());
			return "err@装载表单期间出现如下错误,ccform有自动诊断修复功能请在刷新一次，如果仍然存在请联系管理员. @" + ex.getMessage();
		}
	}
	/** 
	 执行保存
	 
	 @return 
	*/
	public final String FrmGener_Save()
	{
		try
		{
			//保存主表数据.
			GEEntity en = new GEEntity(this.getEnsName());

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
				FrmNode fn = new FrmNode(this.getFK_Flow(), this.getFK_Node(), this.getFK_MapData());
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion  求who is PK.

			en.OID = pk;
			int i = en.RetrieveFromDBSources();
			en.ResetDefaultVal();

			Object tempVar = BP.Sys.PubClass.CopyFromRequest(en);
			en = tempVar instanceof GEEntity ? (GEEntity)tempVar : null;

			en.OID = pk;

			// 处理表单保存前事件.
			MapData md = new MapData(this.getEnsName());

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 调用事件.  @李国文.
			//是不是从表的保存.
			if (this.GetRequestValInt("IsForDtl") == 1)
			{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#region 从表保存前处理事件.
				//获得主表事件.
				FrmEvents fes = new FrmEvents(this.getEnsName()); //获得事件.
				GEEntity mainEn = null;
				if (fes.size() > 0)
				{
					String msg = fes.DoEventNode(EventListDtlList.DtlSaveBefore, en);
					if (DataType.IsNullOrEmpty(msg) == false)
					{
						return "err@" + msg;
					}
				}

				MapDtl mdtl = new MapDtl(this.getEnsName());
				if (mdtl.FEBD.Length != 0)
				{
					String str = mdtl.FEBD;
					BP.Sys.FormEventBaseDtl febd = BP.Sys.Glo.GetFormDtlEventBaseByEnName(mdtl.No);

					if (febd != null)
					{
						febd.HisEn = mdtl.GenerGEMainEntity(this.getRefPKVal());
						febd.HisEnDtl = en;

						febd.DoIt(FrmEventListDtl.RowSaveBefore, febd.HisEn, en, null);
					}
				}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion 从表保存前处理事件.
			}
			else
			{
				md.DoEvent(FrmEventList.SaveBefore, en);
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 调用事件.  @李国文.

			if (i == 0)
			{
				en.Insert();
			}
			else
			{
				en.Update();
			}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 调用事件.
			if (this.GetRequestValInt("IsForDtl") == 1)
			{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#region 从表保存前处理事件.
				//获得主表事件.
				FrmEvents fes = new FrmEvents(this.getEnsName()); //获得事件.
				GEEntity mainEn = null;
				if (fes.size() > 0)
				{
					String msg = fes.DoEventNode(EventListDtlList.DtlItemSaveAfter, en);
					if (DataType.IsNullOrEmpty(msg) == false)
					{
						return "err@" + msg;
					}
				}

				MapDtl mdtl = new MapDtl(this.getEnsName());

				if (mdtl.FEBD.Length != 0)
				{
					String str = mdtl.FEBD;
					BP.Sys.FormEventBaseDtl febd = BP.Sys.Glo.GetFormDtlEventBaseByEnName(mdtl.No);

					if (febd != null)
					{
						febd.HisEn = mdtl.GenerGEMainEntity(this.getRefPKVal());
						febd.HisEnDtl = en;

						febd.DoIt(FrmEventListDtl.RowSaveAfter, febd.HisEn, en, null);
					}
				}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion 从表保存前处理事件.
			}
			else
			{
				md.DoEvent(FrmEventList.SaveAfter, en);
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 调用事件.  @李国文.

			return "保存成功.";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region dtl.htm 从表.
	/** 
	 初始化从表数据
	 
	 @return 返回结果数据
	*/
	public final String Dtl_Init()
	{
		DataSet ds = Dtl_Init_Dataset();
		return BP.Tools.Json.DataSetToJson(ds, false);
	}
	private DataSet Dtl_Init_Dataset()
	{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 组织参数.
		MapDtl mdtl = new MapDtl(this.getEnsName());
		mdtl.No = this.getEnsName();

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 如果是测试，就创建表.
		if (this.getFK_Node() == 999999 || this.GetRequestVal("IsTest") != null)
		{
			GEDtl dtl = new GEDtl(mdtl.No);
			dtl.CheckPhysicsTable();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 如果是测试，就创建表.

		String frmID = mdtl.FK_MapData;
		if (this.getFK_Node() != 0 && this.getFK_Node() != 999999)
		{
			frmID = frmID.replace("_" + this.getFK_Node(), "");
		}

		if (this.getFK_Node() != 0 && !mdtl.FK_MapData.equals("Temp") && this.getEnsName().contains("ND" + this.getFK_Node()) == false && this.getFK_Node() != 999999)
		{
			Node nd = new BP.WF.Node(this.getFK_Node());

			if (nd.getHisFormType() == NodeFormType.SheetTree || nd.getHisFormType() == NodeFormType.RefOneFrmTree || nd.getHisFormType() == NodeFormType.FoolTruck)
			{
				/*如果
				 * 1,传来节点ID, 不等于0.
				 * 2,不是节点表单.  就要判断是否是独立表单，如果是就要处理权限方案。*/
				BP.WF.Template.FrmNode fn = new BP.WF.Template.FrmNode(nd.getFK_Flow(), nd.getNodeID(), frmID);
				if (fn.getFrmSln() == FrmSln.Readonly)
				{
					mdtl.IsInsert = false;
					mdtl.IsDelete = false;
					mdtl.IsUpdate = false;
					mdtl.IsReadonly = true;
				}

				/**自定义权限.
				*/
				if (fn.getFrmSln() == FrmSln.Self)
				{
					mdtl.No = this.getEnsName() + "_" + this.getFK_Node();
					if (mdtl.RetrieveFromDBSources() == 0)
					{
						/*如果设置了自定义方案，但是没有定义，从表属性，就需要去默认值. */
					}
				}
			}
		}

		if (this.GetRequestVal("IsReadonly").equals("1"))
		{
			mdtl.IsInsert = false;
			mdtl.IsDelete = false;
			mdtl.IsUpdate = false;
		}


		String strs = this.getRequestParas();
		strs = strs.replace("?", "@");
		strs = strs.replace("&", "@");
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 组织参数.

		//获得他的描述,与数据.
		DataSet ds = BP.WF.CCFormAPI.GenerDBForCCFormDtl(frmID, mdtl, Integer.parseInt(this.getRefPKVal()), strs, this.getFID());
		return ds;
	}
	/** 
	 执行从表的保存.
	 
	 @return 
	*/
	public final String Dtl_Save()
	{
		MapDtl mdtl = new MapDtl(this.getEnsName());
		GEDtls dtls = new GEDtls(this.getEnsName());
		FrmEvents fes = new FrmEvents(this.getEnsName()); //获得事件.
		GEEntity mainEn = null;

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 从表保存前处理事件.
		if (fes.size() > 0)
		{
			mainEn = mdtl.GenerGEMainEntity(this.getRefPKVal());
			String msg = fes.DoEventNode(EventListDtlList.DtlSaveBefore, mainEn);
			if (msg != null)
			{
				throw new RuntimeException(msg);
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 从表保存前处理事件.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 保存的业务逻辑.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 保存的业务逻辑.

		return "保存成功";
	}
	/** 
	 导出excel与附件信息,并且压缩一个压缩包.
	 
	 @return 返回下载路径
	*/
	public final String Dtl_ExpZipFiles()
	{
		DataSet ds = Dtl_Init_Dataset();

		return "err@尚未wancheng.";
	}
	/** 
	 保存单行数据
	 
	 @return 
	*/
	public final String Dtl_SaveRow()
	{
		//从表.
		String fk_mapDtl = this.getFK_MapDtl();
		MapDtl mdtl = new MapDtl(fk_mapDtl);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 处理权限方案。
		if (this.getFK_Node() != 0 && this.getFK_Node() != 999999)
		{
			Node nd = new Node(this.getFK_Node());
			if (nd.getHisFormType() == NodeFormType.SheetTree || nd.getHisFormType() == NodeFormType.RefOneFrmTree)
			{
				FrmNode fn = new FrmNode(nd.getFK_Flow(), nd.getNodeID(), this.getFK_MapData());
				if (fn.getFrmSln() == FrmSln.Self)
				{
					String no = fk_mapDtl + "_" + nd.getNodeID();
					MapDtl mdtlSln = new MapDtl();
					mdtlSln.No = no;
					int result = mdtlSln.RetrieveFromDBSources();
					if (result != 0)
					{
						mdtl = mdtlSln;
						fk_mapDtl = no;
					}
				}
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 处理权限方案。

		//从表实体.
		GEDtl dtl = new GEDtl(fk_mapDtl);
		int oid = this.getRefOID();
		if (oid != 0)
		{
			dtl.OID = oid;
			dtl.RetrieveFromDBSources();
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 给实体循环赋值/并保存.
		BP.En.Attrs attrs = dtl.getEnMap().getAttrs();
		for (BP.En.Attr attr : attrs)
		{
			dtl.SetValByKey(attr.getKey(), this.GetRequestVal(attr.getKey()));
		}

		//关联主赋值.
		dtl.RefPK = this.getRefPKVal();
		switch (mdtl.DtlOpenType)
		{
			case DtlOpenType.ForEmp: // 按人员来控制.
				dtl.RefPK = this.getRefPKVal();
				break;
			case DtlOpenType.ForWorkID: // 按工作ID来控制
				dtl.RefPK = this.getRefPKVal();
				dtl.FID = Long.parseLong(this.getRefPKVal());
				break;
			case DtlOpenType.ForFID: // 按流程ID来控制.
				dtl.RefPK = this.getRefPKVal();
				dtl.FID = this.getFID();
				break;
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 从表保存前处理事件.
		//获得主表事件.
		FrmEvents fes = new FrmEvents(fk_mapDtl); //获得事件.
		GEEntity mainEn = null;
		if (fes.size() > 0)
		{
			mainEn = mdtl.GenerGEMainEntity(this.getRefPKVal());
			String msg = fes.DoEventNode(EventListDtlList.DtlSaveBefore, mainEn);
			if (DataType.IsNullOrEmpty(msg) == false)
			{
				return "err@" + msg;
			}
		}

		if (mdtl.FEBD.Length != 0)
		{
			String str = mdtl.FEBD;
			BP.Sys.FormEventBaseDtl febd = BP.Sys.Glo.GetFormDtlEventBaseByEnName(mdtl.No);

			febd.HisEn = mdtl.GenerGEMainEntity(this.getRefPKVal());
			febd.HisEnDtl = dtl;

			febd.DoIt(FrmEventListDtl.RowSaveBefore, febd.HisEn, dtl, null);
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 从表保存前处理事件.


		//一直找不到refpk  值为null .
		dtl.RefPK = this.getRefPKVal();
		if (dtl.OID == 0)
		{
			//dtl.OID = DBAccess.GenerOID();
			dtl.Insert();
		}
		else
		{
			dtl.Update();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 给实体循环赋值/并保存.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 从表保存后处理事件。
		if (fes.size() > 0)
		{
			String msg = fes.DoEventNode(EventListDtlList.DtlSaveEnd, mainEn);
			if (DataType.IsNullOrEmpty(msg) == false)
			{
				return "err@" + msg;
			}
		}

		if (mdtl.FEBD.Length != 0)
		{
			String str = mdtl.FEBD;
			BP.Sys.FormEventBaseDtl febd = BP.Sys.Glo.GetFormDtlEventBaseByEnName(mdtl.No);

			febd.HisEn = mdtl.GenerGEMainEntity(this.getRefPKVal());
			febd.HisEnDtl = dtl;

			febd.DoIt(FrmEventListDtl.RowSaveAfter, febd.HisEn, dtl, null);
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 处理事件.

		//返回当前数据存储信息.
		return dtl.ToJson();
	}
	/** 
	 删除
	 
	 @return 
	*/
	public final String Dtl_DeleteRow()
	{
		GEDtl dtl = new GEDtl(this.getFK_MapDtl());
		dtl.OID = this.getRefOID();


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 从表 删除 前处理事件.
		//获得主表事件.
		FrmEvents fes = new FrmEvents(this.getFK_MapDtl()); //获得事件.
		GEEntity mainEn = null;
		if (fes.size() > 0)
		{
			String msg = fes.DoEventNode(FrmEventListDtl.DtlRowDelBefore, dtl);
			if (DataType.IsNullOrEmpty(msg) == false)
			{
				return "err@" + msg;
			}
		}

		MapDtl mdtl = new MapDtl(this.getFK_MapDtl());
		if (mdtl.FEBD.Length != 0)
		{
			String str = mdtl.FEBD;
			BP.Sys.FormEventBaseDtl febd = BP.Sys.Glo.GetFormDtlEventBaseByEnName(mdtl.No);
			if (febd != null)
			{
				febd.HisEn = mdtl.GenerGEMainEntity(this.getRefPKVal());
				febd.HisEnDtl = dtl;
				febd.DoIt(FrmEventListDtl.DtlRowDelBefore, febd.HisEn, dtl, null);
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 从表 删除 前处理事件.

		//执行删除.
		dtl.Delete();


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 从表 删除 后处理事件.
		//获得主表事件.
		fes = new FrmEvents(this.getFK_MapDtl()); //获得事件.
		if (fes.size() > 0)
		{
			String msg = fes.DoEventNode(FrmEventListDtl.DtlRowDelAfter, dtl);
			if (DataType.IsNullOrEmpty(msg) == false)
			{
				return "err@" + msg;
			}
		}

		if (mdtl.FEBD.Length != 0)
		{
			String str = mdtl.FEBD;
			BP.Sys.FormEventBaseDtl febd = BP.Sys.Glo.GetFormDtlEventBaseByEnName(mdtl.No);
			if (febd != null)
			{
				febd.HisEn = mdtl.GenerGEMainEntity(this.getRefPKVal());
				febd.HisEnDtl = dtl;

				febd.DoIt(FrmEventListDtl.DtlRowDelAfter, febd.HisEn, dtl, null);
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 从表 删除 后处理事件.

		//如果可以上传附件这删除相应的附件信息
		FrmAttachmentDBs dbs = new FrmAttachmentDBs();
		dbs.Delete(FrmAttachmentDBAttr.FK_MapData, this.getFK_MapDtl(), FrmAttachmentDBAttr.RefPKVal, this.getRefOID(), FrmAttachmentDBAttr.NodeID, this.getFK_Node());

		return "删除成功";
	}
	/** 
	 重新获取单个ddl数据
	 
	 @return 
	*/
	public final String Dtl_ReloadDdl()
	{
		String Doc = this.GetRequestVal("Doc");
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(Doc);
		dt.TableName = "ReloadDdl";
		return BP.Tools.Json.ToJson(dt);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion dtl.htm 从表.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region dtl.Card
	/** 
	 初始化
	 
	 @return 
	*/
	public final String DtlCard_Init()
	{
		DataSet ds = new DataSet();

		MapDtl md = new MapDtl(this.getEnsName());
		if (this.getFK_Node() != 0 && !md.FK_MapData.equals("Temp") && this.getEnsName().contains("ND" + this.getFK_Node()) == false && this.getFK_Node() != 999999)
		{
			Node nd = new BP.WF.Node(this.getFK_Node());

			if (nd.getHisFormType() == NodeFormType.SheetTree)
			{
				/*如果
				 * 1,传来节点ID, 不等于0.
				 * 2,不是节点表单.  就要判断是否是独立表单，如果是就要处理权限方案。*/
				BP.WF.Template.FrmNode fn = new BP.WF.Template.FrmNode(nd.getFK_Flow(), nd.getNodeID(), this.getFK_MapData());
				/**自定义权限.
				*/
				if (fn.getFrmSln() == FrmSln.Self)
				{
					md.No = this.getEnsName() + "_" + this.getFK_Node();
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
		MapAttrs attrs = md.MapAttrs;
		ds.Tables.add(attrs.ToDataTableField("MapAttrs"));

		//从表.
		MapDtls dtls = md.MapDtls;
		ds.Tables.add(dtls.ToDataTableField("MapDtls"));

		//从表的从表.
		for (MapDtl dtl : dtls.ToJavaList())
		{
			MapAttrs subAttrs = new MapAttrs(dtl.No);
			ds.Tables.add(subAttrs.ToDataTableField(dtl.No));
		}

		//从表的数据.
		//GEDtls enDtls = new GEDtls(this.EnsName);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region  把从表的数据放入.
		GEDtls enDtls = new GEDtls(md.No);
		QueryObject qo = null;
		try
		{
			qo = new QueryObject(enDtls);
			switch (md.DtlOpenType)
			{
				case DtlOpenType.ForEmp: // 按人员来控制.
					qo.AddWhere(GEDtlAttr.RefPK, this.getRefPKVal());
					qo.addAnd();
					qo.AddWhere(GEDtlAttr.Rec, WebUser.getNo());
					break;
				case DtlOpenType.ForWorkID: // 按工作ID来控制
					qo.AddWhere(GEDtlAttr.RefPK, this.getRefPKVal());
					break;
				case DtlOpenType.ForFID: // 按流程ID来控制.
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
			dtls.GetNewEntity.CheckPhysicsTable();
			throw ex;
		}

		//条件过滤.
		if (!md.FilterSQLExp.equals(""))
		{
			String[] strs = md.FilterSQLExp.split("[=]", -1);
			qo.addAnd();
			qo.AddWhere(strs[0], strs[1]);
		}

		//增加排序.
		qo.addOrderBy(enDtls.GetNewEntity.PKField);

		//从表
		DataTable dtDtl = qo.DoQueryToTable();
		dtDtl.TableName = "DTDtls";
		ds.Tables.add(dtDtl);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion
		//enDtls.Retrieve(GEDtlAttr.RefPK, this.RefPKVal);
		//ds.Tables.add(enDtls.ToDataTableField("DTDtls"));

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
		MapAttrs attrs = md.MapAttrs;
		ds.Tables.add(attrs.ToDataTableField("MapAttrs"));

		GEDtls enDtls = new GEDtls(this.getEnsName());
		enDtls.Retrieve(GEDtlAttr.RefPK, this.getRefPKVal());
		ds.Tables.add(enDtls.ToDataTableField("DTDtls"));

		return BP.Tools.Json.ToJson(ds);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion dtl.Card

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 保存手写签名图片
	/** 
	 保存手写签名图片
	 
	 @return 返回保存结果
	*/
	public final String HandWriting_Save()
	{
		try
		{
			String basePath = SystemConfig.PathOfDataUser + "HandWritingImg";
			String ny = LocalDateTime.now().toString("yyyy_MM");
			String tempPath = basePath + "\\" + ny + "\\" + this.getFK_Node() + "\\";
			String tempName = this.getWorkID() + "_" + this.getKeyOfEn() + "_" + LocalDateTime.now().toString("HHmmss") + ".png";

			if ((new File(tempPath)).isDirectory() == false)
			{
				(new File(tempPath)).mkdirs();
			}
			//删除改目录下WORKID的文件
			String[] files = (new File(tempPath)).list(File::isFile);
			for (String file : files)
			{
				if (file.contains(this.getWorkID() + "_" + this.getKeyOfEn()) == true)
				{
					(new File(file)).delete();
				}
			}



			String pic_Path = tempPath + tempName;

			String imgData = this.GetValFromFrmByKey("imageData");

			try (System.IO.FileStream fs = new FileStream(pic_Path, FileMode.Create))
			{
				try (BinaryWriter bw = new BinaryWriter(fs))
				{
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte[] data = Convert.FromBase64String(imgData);
					byte[] data = Convert.FromBase64String(imgData);
					bw.Write(data);
					bw.Close();
				}
			}
			return "info@" + pic_Path.replace("\\", "/");
			;
		}
		catch (RuntimeException e)
		{
			return "err@" + e.getMessage();
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 处理SQL的表达式.
	 
	 @param exp 表达式
	 @return 从from里面替换的表达式.
	*/
	public final String DealExpByFromVals(String exp)
	{
		for (String strKey : HttpContextHelper.RequestParamKeys)
		{
			if (exp.contains("@") == false)
			{
				return exp;
			}
			String str = strKey.replace("TB_", "").replace("CB_", "").replace("DDL_", "").replace("RB_", "");

			exp = exp.replace("@" + str, HttpContextHelper.RequestParams(strKey));
		}
		return exp;
	}
	/** 
	 初始化树的接口
	 
	 @param context
	 @return 
	*/
	public final String PopVal_InitTree()
	{
		String mypk = this.GetRequestVal("FK_MapExt");

		MapExt me = new MapExt();
		me.setMyPK( mypk;
		me.Retrieve();

		//获得配置信息.
		Hashtable ht = me.PopValToHashtable();
		DataTable dtcfg = BP.Sys.PubClass.HashtableToDataTable(ht);

		String parentNo = this.GetRequestVal("ParentNo");
		if (parentNo == null)
		{
			parentNo = me.PopValTreeParentNo;
		}

		DataSet resultDs = new DataSet();
		String sqlObjs = me.PopValTreeSQL;
		sqlObjs = sqlObjs.replace("@WebUser.getNo()", WebUser.getNo());
		sqlObjs = sqlObjs.replace("@WebUser.getName()", WebUser.getName());
		sqlObjs = sqlObjs.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());
		sqlObjs = sqlObjs.replace("@ParentNo", parentNo);
		sqlObjs = this.DealExpByFromVals(sqlObjs);

		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sqlObjs);
		dt.TableName = "DTObjs";

		//判断是否是oracle.
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dt.Columns.get("NO").ColumnName = "No";
			dt.Columns.get("NAME").ColumnName = "Name";
			dt.Columns.get("PARENTNO").ColumnName = "ParentNo";
		}
		resultDs.Tables.add(dt);

		//doubleTree
		if (me.PopValWorkModel == PopValWorkModel.TreeDouble && !parentNo.equals(me.PopValTreeParentNo))
		{
			sqlObjs = me.PopValDoubleTreeEntitySQL;
			sqlObjs = sqlObjs.replace("@WebUser.getNo()", WebUser.getNo());
			sqlObjs = sqlObjs.replace("@WebUser.getName()", WebUser.getName());
			sqlObjs = sqlObjs.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());
			sqlObjs = sqlObjs.replace("@ParentNo", parentNo);
			sqlObjs = this.DealExpByFromVals(sqlObjs);

			DataTable entityDt = BP.DA.DBAccess.RunSQLReturnTable(sqlObjs);
			entityDt.TableName = "DTEntitys";
			resultDs.Tables.add(entityDt);


			//判断是否是oracle.
			if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
			{
				entityDt.Columns.get("NO").ColumnName = "No";
				entityDt.Columns.get("NAME").ColumnName = "Name";

			}

		}

		return BP.Tools.Json.ToJson(resultDs);
	}

	/** 
	 处理DataTable中的列名，将不规范的No,Name,ParentNo列纠正
	 
	 @param dt
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
	public final String PopVal_Init()
	{
		MapExt me = new MapExt();
		me.setMyPK( this.getFK_MapExt();
		me.Retrieve();

		//数据对象，将要返回的.
		DataSet ds = new DataSet();

		//获得配置信息.
		Hashtable ht = me.PopValToHashtable();
		DataTable dtcfg = BP.Sys.PubClass.HashtableToDataTable(ht);

		//增加到数据源.
		ds.Tables.add(dtcfg);

		if (me.PopValWorkModel == PopValWorkModel.SelfUrl)
		{
			return "@SelfUrl" + me.PopValUrl;
		}

		if (me.PopValWorkModel == PopValWorkModel.TableOnly)
		{
			String sqlObjs = me.PopValEntitySQL;
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

		if (me.PopValWorkModel == PopValWorkModel.Group)
		{
			/*
			 *  分组的.
			 */

			String sqlObjs = me.PopValGroupSQL;
			if (sqlObjs.length() > 10)
			{
				sqlObjs = sqlObjs.replace("@WebUser.getNo()", WebUser.getNo());
				sqlObjs = sqlObjs.replace("@WebUser.getName()", WebUser.getName());
				sqlObjs = sqlObjs.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());
				sqlObjs = this.DealExpByFromVals(sqlObjs);


				DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sqlObjs);
				dt.TableName = "DTGroup";
				DoCheckTableColumnNameCase(dt);
				ds.Tables.add(dt);
			}

			sqlObjs = me.PopValEntitySQL;
			if (sqlObjs.length() > 10)
			{
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

		if (me.PopValWorkModel == PopValWorkModel.TablePage)
		{
			/* 分页的 */
			//key
			String key = this.GetRequestVal("Key");
			if (DataType.IsNullOrEmpty(key) == true)
			{
				key = "";
			}

			//取出来查询条件.
			String[] conds = me.PopValSearchCond.split("[$]", -1);

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

			String sqlObjs = me.PopValTablePageSQL;
			sqlObjs = sqlObjs.replace("@WebUser.getNo()", WebUser.getNo());
			sqlObjs = sqlObjs.replace("@WebUser.getName()", WebUser.getName());
			sqlObjs = sqlObjs.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());
			sqlObjs = sqlObjs.replace("@Key", key);

			//三个固定参数.
			sqlObjs = sqlObjs.replace("@PageCount", ((Integer.parseInt(pageIndex) - 1) * Integer.parseInt(pageSize)).toString());
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
				String val = HttpContextHelper.RequestParams(para);
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


							sqlObjs = tangible.StringHelper.remove(sqlObjs, lastBlankIndex + startIndex + 1, index - lastBlankIndex - 1);

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


			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sqlObjs);
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
					sql = sql.replace("@WebUser.getNo()", WebUser.getNo());
					sql = sql.replace("@WebUser.getName()", WebUser.getName());
					sql = sql.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());
					sql = this.DealExpByFromVals(sql);
				}

				if (cond.contains("#EnumKey=") == true)
				{
					String enumKey = cond.substring(cond.indexOf("EnumKey") + 8);
					sql = "SELECT IntKey AS No, Lab as Name FROM Sys_Enum WHERE EnumKey='" + enumKey + "'";
				}

				//处理日期的默认值
				//DefVal=@Now-30
				//if (cond.Contains("@Now"))
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
				if (ds.Tables.Contains(para) == true)
				{
					throw new RuntimeException("@配置的查询,参数名有冲突不能命名为:" + para);
				}

				//查询出来数据，就把他放入到dataset里面.
				DataTable dtPara = BP.DA.DBAccess.RunSQLReturnTable(sql);
				dtPara.TableName = para;
				DoCheckTableColumnNameCase(dt);
				ds.Tables.add(dtPara); //加入到参数集合.
			}


			return BP.Tools.Json.ToJson(ds);
		}

		//返回数据.
		return BP.Tools.Json.ToJson(ds);
	}

	/** 
	 初始化PopVal 分页表格模式的Count  杨玉慧
	 
	 @return 
	*/
	public final String PopVal_InitTablePageCount()
	{
		MapExt me = new MapExt();
		me.setMyPK( this.getFK_MapExt();
		me.Retrieve();

		//数据对象，将要返回的.
		DataSet ds = new DataSet();

		//获得配置信息.
		Hashtable ht = me.PopValToHashtable();
		DataTable dtcfg = BP.Sys.PubClass.HashtableToDataTable(ht);

		//增加到数据源.
		ds.Tables.add(dtcfg);

		if (me.PopValWorkModel == PopValWorkModel.SelfUrl)
		{
			return "@SelfUrl" + me.PopValUrl;
		}
		if (me.PopValWorkModel == PopValWorkModel.TablePage)
		{
			/* 分页的 */
			//key
			String key = this.GetRequestVal("Key");
			if (DataType.IsNullOrEmpty(key) == true)
			{
				key = "";
			}

			//取出来查询条件.
			String[] conds = me.PopValSearchCond.split("[$]", -1);

			String countSQL = me.PopValTablePageSQLCount;

			//固定参数.
			countSQL = countSQL.replace("@WebUser.getNo()", WebUser.getNo());
			countSQL = countSQL.replace("@WebUser.getName()", WebUser.getName());
			countSQL = countSQL.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());
			countSQL = countSQL.replace("@Key", key);
			countSQL = this.DealExpByFromVals(countSQL);

			//替换其他参数.
			for (String cond : conds)
			{
				if (cond == null || cond.equals(""))
				{
					continue;
				}

				//参数.
				String para = cond.substring(5, cond.indexOf("#"));
				String val = HttpContextHelper.RequestParams(para);
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
					countSQL = countSQL.replace(para + "=@" + para, "1=1");
					countSQL = countSQL.replace(para + "='@" + para + "'", "1=1");

					//找到para 前面表的别名   如 t.1=1 把t. 去掉
					int startIndex = 0;
					while (startIndex != -1 && startIndex < countSQL.length())
					{
						int index = countSQL.indexOf("1=1", startIndex + 1);
						if (index > 0 && countSQL.substring(startIndex, index).trim().endsWith("."))
						{
							int lastBlankIndex = countSQL.substring(startIndex, index).lastIndexOf(" ");


							countSQL = tangible.StringHelper.remove(countSQL, lastBlankIndex + startIndex + 1, index - lastBlankIndex - 1);

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
					countSQL = countSQL.replace("@" + para, val);
				}
			}

			String count = BP.DA.DBAccess.RunSQLReturnValInt(countSQL, 0).toString();
			DataTable dtCount = new DataTable("DTCout");
			dtCount.TableName = "DTCout";
			dtCount.Columns.Add("Count", Integer.class);
			dtCount.Rows.add(new String[] {count});
			ds.Tables.add(dtCount);


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
					sql = sql.replace("@WebUser.getNo()", WebUser.getNo());
					sql = sql.replace("@WebUser.getName()", WebUser.getName());
					sql = sql.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());
					sql = this.DealExpByFromVals(sql);
				}

				if (cond.contains("#EnumKey=") == true)
				{
					String enumKey = cond.substring(cond.indexOf("EnumKey") + 8);
					sql = "SELECT IntKey AS No, Lab as Name FROM Sys_Enum WHERE EnumKey='" + enumKey + "'";
				}

				//处理日期的默认值
				//DefVal=@Now-30
				//if (cond.Contains("@Now"))
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
				if (ds.Tables.Contains(para) == true)
				{
					throw new RuntimeException("@配置的查询,参数名有冲突不能命名为:" + para);
				}

				//查询出来数据，就把他放入到dataset里面.
				DataTable dtPara = BP.DA.DBAccess.RunSQLReturnTable(sql);
				dtPara.TableName = para;
				ds.Tables.add(dtPara); //加入到参数集合.
			}


			return BP.Tools.Json.ToJson(ds);
		}
		//返回数据.
		return BP.Tools.Json.ToJson(ds);
	}

	/** 
	 /// 
	 初始化PopVal分页表格的List  杨玉慧
	 
	 @return 
	*/
	public final String PopVal_InitTablePageList()
	{
		MapExt me = new MapExt();
		me.setMyPK( this.getFK_MapExt();
		me.Retrieve();

		//数据对象，将要返回的.
		DataSet ds = new DataSet();

		//获得配置信息.
		Hashtable ht = me.PopValToHashtable();
		DataTable dtcfg = BP.Sys.PubClass.HashtableToDataTable(ht);

		//增加到数据源.
		ds.Tables.add(dtcfg);

		if (me.PopValWorkModel == PopValWorkModel.SelfUrl)
		{
			return "@SelfUrl" + me.PopValUrl;
		}
		if (me.PopValWorkModel == PopValWorkModel.TablePage)
		{
			/* 分页的 */
			//key
			String key = this.GetRequestVal("Key");
			if (DataType.IsNullOrEmpty(key) == true)
			{
				key = "";
			}

			//取出来查询条件.
			String[] conds = me.PopValSearchCond.split("[$]", -1);

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

			String sqlObjs = me.PopValTablePageSQL;
			sqlObjs = sqlObjs.replace("@WebUser.getNo()", WebUser.getNo());
			sqlObjs = sqlObjs.replace("@WebUser.getName()", WebUser.getName());
			sqlObjs = sqlObjs.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());
			sqlObjs = sqlObjs.replace("@Key", key);

			//三个固定参数.
			sqlObjs = sqlObjs.replace("@PageCount", ((Integer.parseInt(pageIndex) - 1) * Integer.parseInt(pageSize)).toString());
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
				String val = HttpContextHelper.RequestParams(para);
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


							sqlObjs = tangible.StringHelper.remove(sqlObjs, lastBlankIndex + startIndex + 1, index - lastBlankIndex - 1);

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


			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sqlObjs);
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
					sql = sql.replace("@WebUser.getNo()", WebUser.getNo());
					sql = sql.replace("@WebUser.getName()", WebUser.getName());
					sql = sql.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());
					sql = this.DealExpByFromVals(sql);
				}

				if (cond.contains("#EnumKey=") == true)
				{
					String enumKey = cond.substring(cond.indexOf("EnumKey") + 8);
					sql = "SELECT IntKey AS No, Lab as Name FROM Sys_Enum WHERE EnumKey='" + enumKey + "'";
				}

				//处理日期的默认值
				//DefVal=@Now-30
				//if (cond.Contains("@Now"))
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
				if (ds.Tables.Contains(para) == true)
				{
					throw new RuntimeException("@配置的查询,参数名有冲突不能命名为:" + para);
				}

				//查询出来数据，就把他放入到dataset里面.
				DataTable dtPara = BP.DA.DBAccess.RunSQLReturnTable(sql);
				dtPara.TableName = para;
				ds.Tables.add(dtPara); //加入到参数集合.
			}


			return BP.Tools.Json.ToJson(ds);
		}
		//返回数据.
		return BP.Tools.Json.ToJson(ds);
	}

	//单附件上传方法
	private void SingleAttach(String attachPk, long workid, long fid, int fk_node, String ensName)
	{
		FrmAttachment frmAth = new FrmAttachment();
		frmAth.setMyPK( attachPk;
		frmAth.RetrieveFromDBSources();

		String athDBPK = attachPk + "_" + workid;

		BP.WF.Node currND = new BP.WF.Node(fk_node);
		BP.WF.Work currWK = currND.getHisWork();
		currWK.setOID(workid);
		currWK.Retrieve();
		//处理保存路径.
		String saveTo = frmAth.SaveTo;

		if (saveTo.contains("*") || saveTo.contains("@"))
		{
			/*如果路径里有变量.*/
			saveTo = saveTo.replace("*", "@");
			saveTo = BP.WF.Glo.DealExp(saveTo, currWK, null);
		}

		try
		{
			saveTo = SystemConfig.PathOfWebApp + saveTo; //context.Server.MapPath("~/" + saveTo);
		}
		catch (java.lang.Exception e)
		{
			//saveTo = saveTo;
		}

		if ((new File(saveTo)).isDirectory() == false)
		{
			(new File(saveTo)).mkdirs();
		}


		saveTo = saveTo + "\\" + athDBPK + "." + HttpContextHelper.RequestFiles(0).FileName.substring(HttpContextHelper.RequestFiles(0).FileName.lastIndexOf('.') + 1);
		//context.Request.Files[0].SaveAs(saveTo);
		HttpContextHelper.UploadFile(HttpContextHelper.RequestFiles(0), saveTo);

		File info = new File(saveTo);

		FrmAttachmentDB dbUpload = new FrmAttachmentDB();
		dbUpload.setMyPK( athDBPK;
		dbUpload.FK_FrmAttachment = attachPk;
		dbUpload.RefPKVal = String.valueOf(this.getWorkID());
		dbUpload.FID = fid;
		dbUpload.FK_MapData = ensName;

		dbUpload.FileExts = info.Extension;

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 处理文件路径，如果是保存到数据库，就存储pk.
		if (frmAth.AthSaveWay == AthSaveWay.IISServer)
		{
			//文件方式保存
			dbUpload.FileFullName = saveTo;
		}

		if (frmAth.AthSaveWay == AthSaveWay.DB)
		{
			//保存到数据库
			dbUpload.FileFullName = dbUpload.MyPK;
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 处理文件路径，如果是保存到数据库，就存储pk.


		dbUpload.FileName = HttpContextHelper.RequestFiles(0).FileName;
		dbUpload.FileSize = (float)info.length();
		dbUpload.Rec = WebUser.getNo();
		dbUpload.RecName = WebUser.getName();
		dbUpload.RDT = BP.DA.DataType.getCurrentDataTime();

		dbUpload.NodeID = String.valueOf(fk_node);
		dbUpload.Save();

		if (frmAth.AthSaveWay == AthSaveWay.DB)
		{
			//执行文件保存.
			BP.DA.DBAccess.SaveFileToDB(saveTo, dbUpload.EnMap.PhysicsTable, "MyPK", dbUpload.MyPK, "FDB");
		}
	}

	//多附件上传方法
	public final String MoreAttach()
	{
		String pkVal = this.GetRequestVal("PKVal");
		String attachPk = this.GetRequestVal("AttachPK");
		String paras = this.GetRequestVal("parasData");
		String sort = this.GetRequestVal("Sort");
		//获取sort
		if (DataType.IsNullOrEmpty(sort))
		{
			if (paras != null && paras.length() > 0)
			{
				for (String para : paras.split("[@]", -1))
				{
					if (para.indexOf("Sort") != -1)
					{
						sort = para.split("[=]", -1)[1];
					}
				}
			}
		}
		// 多附件描述.
		BP.Sys.FrmAttachment athDesc = new BP.Sys.FrmAttachment(attachPk);
		MapData mapData = new MapData(athDesc.FK_MapData);
		String msg = null;

		//求出来实体记录，方便执行事件.
		GEEntity en = new GEEntity(athDesc.FK_MapData);
		en.PKVal = pkVal;
		if (en.RetrieveFromDBSources() == 0)
		{
			en.PKVal = this.getFID();
			if (en.RetrieveFromDBSources() == 0)
			{
				en.PKVal = this.getPWorkID();
				en.RetrieveFromDBSources();
			}
		}

		//求主键. 如果该表单挂接到流程上.
		if (this.getFK_Node() != 0)
		{
			//判断表单方案。
			FrmNode fn = new FrmNode(this.getFK_Flow(), this.getFK_Node(), this.getFK_MapData());
			if (fn.getFrmSln() == FrmSln.Readonly)
			{
				return "err@不允许上传附件.";
			}

			//是默认的方案的时候.
			if (fn.getFrmSln() == FrmSln.Default)
			{
				//判断当前方案设置的whoIsPk ，让附件集成 whoIsPK 的设置。
				if (fn.getWhoIsPK() == WhoIsPK.FID)
				{
					pkVal = String.valueOf(this.getFID());
				}

				if (fn.getWhoIsPK() == WhoIsPK.PWorkID)
				{
					pkVal = String.valueOf(this.getPWorkID());
				}
			}

			//自定义方案.
			if (fn.getFrmSln() == FrmSln.Self)
			{
				athDesc = new FrmAttachment(attachPk + "_" + this.getFK_Node());
				if (athDesc.HisCtrlWay == AthCtrlWay.FID)
				{
					pkVal = String.valueOf(this.getFID());
				}

				if (athDesc.HisCtrlWay == AthCtrlWay.PWorkID)
				{
					pkVal = String.valueOf(this.getPWorkID());
				}
			}
		}


		//获取上传文件是否需要加密
		boolean fileEncrypt = SystemConfig.IsEnableAthEncrypt;

		for (int i = 0; i < HttpContextHelper.RequestFilesCount; i++)
		{
			//HttpPostedFile file = context.Request.Files[i];
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
			var file = HttpContextHelper.RequestFiles(i);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 文件上传的iis服务器上 or db数据库里.
			if (athDesc.AthSaveWay == AthSaveWay.IISServer)
			{
				String savePath = athDesc.SaveTo;
				if (savePath.contains("@") == true || savePath.contains("*") == true)
				{
					/*如果有变量*/
					savePath = savePath.replace("*", "@");

					if (savePath.contains("@") && this.getFK_Node() != 0)
					{
						/*如果包含 @ */
						BP.WF.Flow flow = new BP.WF.Flow(this.getFK_Flow());
						BP.WF.Data.GERpt myen = flow.getHisGERpt();
						myen.setOID(this.getWorkID());
						myen.RetrieveFromDBSources();
						savePath = BP.WF.Glo.DealExp(savePath, myen, null);
					}
					if (savePath.contains("@") == true)
					{
						throw new RuntimeException("@路径配置错误,变量没有被正确的替换下来." + savePath);
					}
				}
				else
				{
					savePath = athDesc.SaveTo + "\\" + pkVal;
				}

				//替换关键的字串.
				savePath = savePath.replace("\\\\", "\\");
				try
				{
					if (savePath.contains(SystemConfig.PathOfWebApp) == false)
					{
					savePath = SystemConfig.PathOfWebApp + savePath;
					}
				}
				catch (RuntimeException ex)
				{
					savePath = SystemConfig.PathOfDataUser + "UploadFile\\" + mapData.No + "\\";
					//return "err@获取路径错误" + ex.Message + ",配置的路径是:" + savePath + ",您需要在附件属性上修改该附件的存储路径.";
				}

				try
				{
					if ((new File(savePath)).isDirectory() == false)
					{
						(new File(savePath)).mkdirs();
					}
				}
				catch (RuntimeException ex)
				{
					throw new RuntimeException("err@创建路径出现错误，可能是没有权限或者路径配置有问题:" + savePath + "@异常信息:" + ex.getMessage());
				}

				String exts = System.IO.Path.GetExtension(file.FileName).toLowerCase().replace(".", "");
				if (DataType.IsNullOrEmpty(exts))
				{
					return "err@上传的文件" + file.FileName + "没有扩展名";
				}

				String guid = BP.DA.DBAccess.GenerGUID();
				String fileName = file.FileName.substring(0, file.FileName.lastIndexOf('.'));
				String ext = System.IO.Path.GetExtension(file.FileName);
				String realSaveTo = savePath + "\\" + guid + "." + fileName + ext;

				realSaveTo = realSaveTo.replace("~", "-");
				realSaveTo = realSaveTo.replace("'", "-");
				realSaveTo = realSaveTo.replace("*", "-");
				if (fileEncrypt == true)
				{

					String strtmp = realSaveTo + ".tmp";
					//file.SaveAs(strtmp);//先明文保存到本地(加个后缀名.tmp)
					HttpContextHelper.UploadFile(file, strtmp);
					EncHelper.EncryptDES(strtmp, strtmp.replace(".tmp", "")); //加密
					(new File(strtmp)).delete(); //删除临时文件
				}
				else
				{
					//文件保存的路径
					//file.SaveAs(realSaveTo);
					HttpContextHelper.UploadFile(file, realSaveTo);
				}

				//执行附件上传前事件，added by liuxc,2017-7-15
				msg = mapData.DoEvent(FrmEventList.AthUploadeBefore, en, "@FK_FrmAttachment=" + athDesc.MyPK + "@FileFullName=" + realSaveTo);
				if (!DataType.IsNullOrEmpty(msg))
				{
					BP.Sys.Glo.WriteLineError("@AthUploadeBefore事件返回信息，文件：" + file.FileName + "，" + msg);

					try
					{
						(new File(realSaveTo)).delete();
					}
					catch (java.lang.Exception e)
					{
					}
				}

				File info = new File(realSaveTo);
				FrmAttachmentDB dbUpload = new FrmAttachmentDB();
				dbUpload.setMyPK( guid; // athDesc.FK_MapData + oid.ToString();
				dbUpload.NodeID = String.valueOf(this.getFK_Node());
				dbUpload.Sort = sort;
				dbUpload.FK_FrmAttachment = attachPk;
				dbUpload.FK_MapData = athDesc.FK_MapData;
				dbUpload.FK_FrmAttachment = attachPk;
				dbUpload.FileExts = info.Extension;
				dbUpload.FID = this.getFID();
				if (fileEncrypt == true)
				{
					dbUpload.SetPara("IsEncrypt", 1);
				}

				if (athDesc.IsExpCol == true)
				{
					if (paras != null && paras.length() > 0)
					{
						for (String para : paras.split("[@]", -1))
						{
							dbUpload.SetPara(para.split("[=]", -1)[0], para.split("[=]", -1)[1]);
						}
					}
				}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#region 处理文件路径，如果是保存到数据库，就存储pk.
				if (athDesc.AthSaveWay == AthSaveWay.IISServer)
				{
					//文件方式保存
					dbUpload.FileFullName = realSaveTo;
				}

				if (athDesc.AthSaveWay == AthSaveWay.FTPServer)
				{
					//保存到数据库
					dbUpload.FileFullName = dbUpload.MyPK;
				}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion 处理文件路径，如果是保存到数据库，就存储pk.

				dbUpload.FileName = file.FileName;
				dbUpload.FileSize = (float)info.length();
				dbUpload.RDT = DataType.getCurrentDataTime()ss;
				dbUpload.Rec = WebUser.getNo();
				dbUpload.RecName = WebUser.getName();
				dbUpload.RefPKVal = pkVal;
				dbUpload.FID = this.getFID();

				dbUpload.UploadGUID = guid;
				dbUpload.Insert();

				if (athDesc.AthSaveWay == AthSaveWay.DB)
				{
					//执行文件保存.
					BP.DA.DBAccess.SaveFileToDB(realSaveTo, dbUpload.EnMap.PhysicsTable, "MyPK", dbUpload.MyPK, "FDB");
				}

				//执行附件上传后事件，added by liuxc,2017-7-15
				msg = mapData.DoEvent(FrmEventList.AthUploadeAfter, en, "@FK_FrmAttachment=" + dbUpload.FK_FrmAttachment + "@FK_FrmAttachmentDB=" + dbUpload.MyPK + "@FileFullName=" + dbUpload.FileFullName);
				if (!DataType.IsNullOrEmpty(msg))
				{
					BP.Sys.Glo.WriteLineError("@AthUploadeAfter事件返回信息，文件：" + dbUpload.FileName + "，" + msg);
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 文件上传的iis服务器上 or db数据库里.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 保存到数据库 / FTP服务器上.
			if (athDesc.AthSaveWay == AthSaveWay.DB || athDesc.AthSaveWay == AthSaveWay.FTPServer)
			{
				String guid = DBAccess.GenerGUID();

				//把文件临时保存到一个位置.
				String temp = SystemConfig.PathOfTemp + guid + ".tmp";

				if (fileEncrypt == true)
				{

					String strtmp = SystemConfig.PathOfTemp + guid + "_Desc" + ".tmp";
					//file.SaveAs(strtmp);//先明文保存到本地(加个后缀名.tmp)
					HttpContextHelper.UploadFile(file, strtmp);
					EncHelper.EncryptDES(strtmp, temp); //加密
					(new File(strtmp)).delete(); //删除临时文件
				}
				else
				{
					//文件保存的路径
					//file.SaveAs(temp);
					HttpContextHelper.UploadFile(file, temp);
				}

				//执行附件上传前事件，added by liuxc,2017-7-15
				msg = mapData.DoEvent(FrmEventList.AthUploadeBefore, en, "@FK_FrmAttachment=" + athDesc.MyPK + "@FileFullName=" + temp);
				if (DataType.IsNullOrEmpty(msg) == false)
				{
					BP.Sys.Glo.WriteLineError("@AthUploadeBefore事件返回信息，文件：" + file.FileName + "，" + msg);

					try
					{
						(new File(temp)).delete();
					}
					catch (java.lang.Exception e2)
					{
					}

					throw new RuntimeException("err@上传附件错误：" + msg);
				}

				File info = new File(temp);
				FrmAttachmentDB dbUpload = new FrmAttachmentDB();
				dbUpload.setMyPK( BP.DA.DBAccess.GenerGUID();
				dbUpload.Sort = sort;
				dbUpload.NodeID = String.valueOf(getFK_Node());
				dbUpload.FK_FrmAttachment = athDesc.MyPK;
				dbUpload.FID = this.getFID(); //流程id.
				if (fileEncrypt == true)
				{
					dbUpload.SetPara("IsEncrypt", 1);
				}
				if (athDesc.AthUploadWay == AthUploadWay.Inherit)
				{
					/*如果是继承，就让他保持本地的PK. */
					dbUpload.RefPKVal = pkVal.toString();
				}

				if (athDesc.AthUploadWay == AthUploadWay.Interwork)
				{
					/*如果是协同，就让他是PWorkID. */
					Paras ps = new Paras();
					ps.SQL = "SELECT PWorkID FROM WF_GenerWorkFlow WHERE WorkID=" + SystemConfig.getAppCenterDBVarStr() + "WorkID";
					ps.Add("WorkID", pkVal);
					String pWorkID = BP.DA.DBAccess.RunSQLReturnValInt(ps, 0).toString();
					if (pWorkID == null || pWorkID.equals("0"))
					{
						pWorkID = pkVal;
					}
					dbUpload.RefPKVal = pWorkID;
				}

				dbUpload.FK_MapData = athDesc.FK_MapData;
				dbUpload.FK_FrmAttachment = athDesc.MyPK;
				dbUpload.FileName = file.FileName;
				dbUpload.FileSize = (float)info.length();
				dbUpload.RDT = DataType.getCurrentDataTime()ss;
				dbUpload.Rec = WebUser.getNo();
				dbUpload.RecName = WebUser.getName();
				if (athDesc.IsExpCol == true)
				{
					if (paras != null && paras.length() > 0)
					{
						for (String para : paras.split("[@]", -1))
						{
							dbUpload.SetPara(para.split("[=]", -1)[0], para.split("[=]", -1)[1]);
						}
					}
				}

				dbUpload.UploadGUID = guid;

				if (athDesc.AthSaveWay == AthSaveWay.DB)
				{
					dbUpload.Insert();
					//把文件保存到指定的字段里.
					dbUpload.SaveFileToDB("FileDB", temp);
				}

				if (athDesc.AthSaveWay == AthSaveWay.FTPServer)
				{
					/*保存到fpt服务器上.*/
					FtpSupport.FtpConnection ftpconn = new FtpSupport.FtpConnection(SystemConfig.FTPServerIP, SystemConfig.FTPUserNo, SystemConfig.FTPUserPassword);

					String ny = LocalDateTime.now().toString("yyyy_MM");

					//判断目录年月是否存在.
					if (ftpconn.DirectoryExist(ny) == false)
					{
						ftpconn.CreateDirectory(ny);
					}
					ftpconn.SetCurrentDirectory(ny);

					//判断目录是否存在.
					if (ftpconn.DirectoryExist(athDesc.FK_MapData) == false)
					{
						ftpconn.CreateDirectory(athDesc.FK_MapData);
					}

					//设置当前目录，为操作的目录。
					ftpconn.SetCurrentDirectory(athDesc.FK_MapData);

					//把文件放上去.
					try
					{
						ftpconn.PutFile(temp, guid + "." + dbUpload.FileExts);
					}
					catch (java.lang.Exception e3)
					{
						throw new RuntimeException("err@FTP端口号受限或者防火墙未关闭");
					}
					ftpconn.Close();

					//设置路径.
					dbUpload.FileFullName = ny + "//" + athDesc.FK_MapData + "//" + guid + "." + dbUpload.FileExts;
					dbUpload.Insert();
					(new File(temp)).delete();
				}

				//执行附件上传后事件，added by liuxc,2017-7-15
				msg = mapData.DoEvent(FrmEventList.AthUploadeAfter, en, "@FK_FrmAttachment=" + dbUpload.FK_FrmAttachment + "@FK_FrmAttachmentDB=" + dbUpload.MyPK + "@FileFullName=" + temp);
				if (DataType.IsNullOrEmpty(msg) == false)
				{
					BP.Sys.Glo.WriteLineError("@AthUploadeAfter事件返回信息，文件：" + dbUpload.FileName + "，" + msg);
				}

			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 保存到数据库.

		}
		//需要判断是否存在AthNum字段 
		if (en.Row["AthNum"] != null)
		{
			int athNum = Integer.parseInt(en.Row["AthNum"].toString());
			en.Row["AthNum"] = athNum + 1;
			en.Update();
		}
		return "上传成功.";
	}

	/** 
	 删除附件
	 
	 @param MyPK
	 @return 
	*/
	public final String DelWorkCheckAttach()
	{
		FrmAttachmentDB athDB = new FrmAttachmentDB();
		athDB.RetrieveByAttr(FrmAttachmentDBAttr.MyPK, this.getMyPK());

		//删除文件
		if (athDB.FileFullName != null)
		{
			if ((new File(athDB.FileFullName)).isFile() == true)
			{
				(new File(athDB.FileFullName)).delete();
			}
		}

		int i = athDB.Delete(FrmAttachmentDBAttr.MyPK, this.getMyPK());
		if (i > 0)
		{
			return "true";
		}
		return "false";
	}

	public final String FrmVSTO_Init()
	{
		return "";
	}
	/** 
	 表单处理加载
	 
	 @return 
	*/
	public final String FrmSingle_Init()
	{
		if (DataType.IsNullOrEmpty(this.getFK_MapData()))
		{
			throw new RuntimeException("FK_MapData参数不能为空");
		}

		MapData md = new MapData();
		md.No = this.getFK_MapData();

		if (md.RetrieveFromDBSources() == 0)
		{
			throw new RuntimeException("未检索到FK_MapData=" + this.getFK_MapData() + "的表单，请核对参数");
		}

		int minOID = 10000000; //最小OID设置为一千万
		int oid = this.getOID();
		Hashtable ht = new Hashtable();
		GEEntity en = md.HisGEEn;

		if (oid == 0)
		{
			oid = minOID;
		}

		en.OID = oid;

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
		ht.put("SID", WebUser.SID);

		return BP.Tools.Json.ToJsonEntityModel(ht);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 从表的选项.
	/** 
	 初始化数据
	 
	 @return 
	*/
	public final String DtlOpt_Init()
	{
		MapDtl dtl = new MapDtl(this.getFK_MapDtl());

		if (dtl.ImpModel == 0)
		{
			return "err@该从表不允许导入.";
		}

		if (dtl.ImpModel == 2)
		{
			return "url@DtlImpByExcel.htm?FK_MapDtl=" + this.getFK_MapDtl();
		}


		if (DataType.IsNullOrEmpty(dtl.ImpSQLInit))
		{
			return "err@从表加载语句为空，请设置从表加载的sql语句。";
		}

		DataSet ds = new DataSet();
		DataTable dt = DBAccess.RunSQLReturnTable(dtl.ImpSQLInit);

		return BP.Tools.Json.ToJson(dt);

	}
	/** 
	 增加
	 
	 @return 
	*/
	public final String DtlOpt_Add()
	{
		MapDtl dtl = new MapDtl(this.getFK_MapDtl());
		String pks = this.GetRequestVal("PKs");

		String[] strs = pks.split("[,]", -1);
		int i = 0;
		for (String str : strs)
		{
			if (str.equals("CheckAll") || str == null || str.equals(""))
			{
				continue;
			}

			GEDtl gedtl = new BP.Sys.GEDtl(this.getFK_MapDtl());
			String sql = dtl.ImpSQLFullOneRow;
			sql = sql.replace("@Key", str);

			DataTable dt = DBAccess.RunSQLReturnTable(sql);

			if (dt.Rows.size() == 0)
			{
				return "err@导入数据失败:" + sql;
			}

			gedtl.Copy(dt.Rows[0]);
			gedtl.RefPK = this.GetRequestVal("RefPKVal");
			gedtl.InsertAsNew();
			i++;
		}

		return "成功的导入了[" + i + "]行数据...";
	}
	/** 
	 执行查询.
	 
	 @return 
	*/
	public final String DtlOpt_Search()
	{
		MapDtl dtl = new MapDtl(this.getFK_MapDtl());

		String sql = dtl.ImpSQLSearch;
		sql = sql.replace("@Key", this.GetRequestVal("Key"));
		sql = sql.replace("@WebUser.getNo()", WebUser.getNo());
		sql = sql.replace("@WebUser.getName()", WebUser.getName());
		sql = sql.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());

		DataSet ds = new DataSet();
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		return BP.Tools.Json.ToJson(dt);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 从表的选项.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region SQL从表导入.
	public final String DtlImpBySQL_Delete()
	{
		MapDtl dtl = new MapDtl(this.getEnsName());
		BP.DA.DBAccess.RunSQL("DELETE FROM " + dtl.getPTable() + " WHERE RefPK='" + this.getRefPKVal() + "'");
		return "";
	}
	/** 
	 SQL从表导入
	 
	 @return 
	*/
	public final String DtlImpBySQl_Imp()
	{
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 处理权限方案。
		if (this.getFK_Node() != 0 && this.getFK_Node() != 999999)
		{
			Node nd = new Node(this.getFK_Node());
			if (nd.getHisFormType() == NodeFormType.SheetTree || nd.getHisFormType() == NodeFormType.RefOneFrmTree)
			{
				FrmNode fn = new FrmNode(nd.getFK_Flow(), nd.getNodeID(), dtl.FK_MapData);
				if (fn.getFrmSln() == FrmSln.Self)
				{
					String no = this.getFK_MapDtl() + "_" + nd.getNodeID();
					MapDtl mdtlSln = new MapDtl();
					mdtlSln.No = no;
					int result = mdtlSln.RetrieveFromDBSources();
					if (result != 0)
					{
						dtl = mdtlSln;

					}
				}
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
					qo.AddWhere(k, this.GetRequestVal(k));
				}
				else
				{
					qo.addAnd();
					qo.AddWhere(k, this.GetRequestVal(k));
				}
				idx++;
			}
			switch (dtl.DtlOpenType)
			{
				case DtlOpenType.ForEmp: // 按人员来控制.
					qo.addAnd();
					qo.AddWhere("RefPk", refpk);
					qo.addAnd();
					qo.AddWhere("Rec", this.GetRequestVal("UserNo"));
					break;
				case DtlOpenType.ForWorkID: // 按工作ID来控制
					qo.addAnd();
					qo.addLeftBracket();
					qo.AddWhere("RefPk", refpk);
					qo.addOr();
					qo.AddWhere("FID", fid);
					qo.addRightBracket();
					break;
				case DtlOpenType.ForFID: // 按流程ID来控制.
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

			GEDtl dtlEn = dtls.GetNewEntity instanceof GEDtl ? (GEDtl)dtls.GetNewEntity : null;
			//遍历属性，循环赋值.
			for (Attr attr : dtlEn.getEnMap().getAttrs())
			{
				dtlEn.SetValByKey(attr.getKey(), this.GetRequestVal(attr.getKey()));

			}
			switch (dtl.DtlOpenType)
			{
				case DtlOpenType.ForEmp: // 按人员来控制.
					dtlEn.RefPKInt = Integer.parseInt(refpk);
					break;
				case DtlOpenType.ForWorkID: // 按工作ID来控制
					dtlEn.RefPKInt = Integer.parseInt(refpk);
					dtlEn.SetValByKey("FID", Integer.parseInt(refpk));
					break;
				case DtlOpenType.ForFID: // 按流程ID来控制.
					dtlEn.RefPKInt = Integer.parseInt(refpk);
					dtlEn.SetValByKey("FID", fid);
					break;
			}
			dtlEn.SetValByKey("RDT", BP.DA.DataType.CurrentData);
			dtlEn.SetValByKey("Rec", this.GetRequestVal("UserNo"));
			//dtlEn.OID = (int)DBAccess.GenerOID(ensName);

			dtlEn.InsertAsOID((int)DBAccess.GenerOID(ensName));
			return dtlEn.OID.toString();
		}

		return "";

	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion SQL从表导入

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region Excel导入.
	/** 
	 导入excel.
	 
	 @return 
	*/
	public final String DtlImpByExcel_Imp()
	{
		try
		{
			String tempPath = BP.Sys.SystemConfig.PathOfTemp;

			//HttpFileCollection files = context.Request.Files;
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
			var files = HttpContextHelper.RequestFiles();
			if (files.size() == 0)
			{
				return "err@请选择要上传的从表模版。";
			}
			//求出扩展名.
			String fileName = files[0].FileName;
			if (fileName.contains(".xls") == false)
			{
				return "err@上传的文件必须是excel文件.";
			}

			String ext = ".xls";
			if (fileName.contains(".xlsx"))
			{
				ext = ".xlsx";
			}

			//保存临时文件.
			String file = tempPath + "\\" + WebUser.getNo() + ext;

			if ((new File(tempPath)).isDirectory() == false)
			{
				(new File(tempPath)).mkdirs();
			}

			//执行保存附件
			//files[0].SaveAs(file);
			HttpContextHelper.UploadFile(files[0], file);

			System.Data.DataTable dt = BP.DA.DBLoad.ReadExcelFileToDataTable(file);

			String FK_MapDtl = this.getFK_MapDtl();
			if (FK_MapDtl.contains("BP") == true)
			{
				return BPDtlImpByExcel_Imp(dt, FK_MapDtl);
			}

			MapDtl dtl = new MapDtl(this.getFK_MapDtl());
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 处理权限方案。
			if (this.getFK_Node() != 0 && this.getFK_Node() != 999999)
			{
				Node nd = new Node(this.getFK_Node());
				if (nd.getHisFormType() == NodeFormType.SheetTree || nd.getHisFormType() == NodeFormType.RefOneFrmTree)
				{
					FrmNode fn = new FrmNode(nd.getFK_Flow(), nd.getNodeID(), this.getFK_MapData());
					if (fn.getFrmSln() == FrmSln.Self)
					{
						String no = this.getFK_MapDtl() + "_" + nd.getNodeID();
						MapDtl mdtlSln = new MapDtl();
						mdtlSln.No = no;
						int result = mdtlSln.RetrieveFromDBSources();
						if (result != 0)
						{
							dtl = mdtlSln;
							//fk_mapDtl = no;
						}
					}
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 处理权限方案。
			GEDtls dtls = new GEDtls(this.getFK_MapDtl());
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 检查两个文件是否一致。 生成要导入的属性
			BP.En.Attrs attrs = dtls.GetNewEntity.getEnMap().getAttrs();
			BP.En.Attrs attrsExp = new BP.En.Attrs();

			boolean isHave = false;
			for (DataColumn dc : dt.Columns)
			{
				for (BP.En.Attr attr : attrs)
				{
					if (dc.ColumnName.equals(attr.Desc))
					{
						isHave = true;
						attrsExp.Add(attr);
						continue;
					}

					if (dc.ColumnName.toLowerCase().equals(attr.Key.toLowerCase()))
					{
						isHave = true;
						attrsExp.Add(attr);
						dc.ColumnName = attr.Desc;
					}
				}
			}
			if (isHave == false)
			{
				throw new RuntimeException("@您导入的excel文件不符合系统要求的格式，请下载模版文件重新填入。");
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 执行导入数据.

			if (this.GetRequestValInt("DDL_ImpWay") == 0)
			{
				BP.DA.DBAccess.RunSQL("DELETE FROM " + dtl.getPTable() + " WHERE RefPK='" + this.getWorkID() + "'");
			}

			int i = 0;
			long oid = BP.DA.DBAccess.GenerOID("Dtl", dt.Rows.size());
			String rdt = BP.DA.DataType.CurrentData;

			String errMsg = "";
			for (DataRow dr : dt.Rows)
			{
				GEDtl dtlEn = dtls.GetNewEntity instanceof GEDtl ? (GEDtl)dtls.GetNewEntity : null;
				dtlEn.ResetDefaultVal();

				for (BP.En.Attr attr : attrsExp)
				{
					if (attr.UIVisible == false || dr.get(attr.Desc) == DBNull.Value)
					{
						continue;
					}
					String val = dr.get(attr.Desc).toString();
					if (val == null)
					{
						continue;
					}
					val = val.trim();
					switch (attr.MyFieldType)
					{
						case FieldType.Enum:
						case FieldType.PKEnum:
							SysEnums ses = new SysEnums(attr.UIBindKey);
							boolean isHavel = false;
							for (SysEnum se : ses)
							{
								if (val.equals(se.Lab))
								{
									val = se.IntKey.toString();
									isHavel = true;
									break;
								}
							}
							if (isHavel == false)
							{
								errMsg += "@数据格式不规范,第(" + i + ")行，列(" + attr.getDesc() + ")，数据(" + val + ")不符合格式,改值没有在枚举列表里.";
								val = attr.DefaultVal.toString();
							}
							break;
						case FieldType.FK:
						case FieldType.PKFK:
							Entities ens = null;
							if (attr.UIBindKey.Contains("."))
							{
								ens = BP.En.ClassFactory.GetEns(attr.UIBindKey);
							}
							else
							{
								ens = new GENoNames(attr.UIBindKey, "desc");
							}

							ens.RetrieveAll();
							boolean isHavelIt = false;
							for (Entity en : ens)
							{
								if (val.equals(en.GetValStrByKey("Name")))
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

					if (attr.MyDataType == BP.DA.DataType.AppBoolean)
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

					dtlEn.SetValByKey(attr.Key, val);
				}
				//dtlEn.RefPKInt = (int)this.WorkID;
				//关联主赋值.
				dtl.RefPK = this.getRefPKVal();
				switch (dtl.DtlOpenType)
				{
					case DtlOpenType.ForEmp: // 按人员来控制.
						dtlEn.RefPKInt = (int)this.getWorkID();
						break;
					case DtlOpenType.ForWorkID: // 按工作ID来控制
						dtlEn.RefPKInt = (int)this.getWorkID();
						dtl.SetValByKey("FID", this.getWorkID());
						break;
					case DtlOpenType.ForFID: // 按流程ID来控制.
						dtlEn.RefPKInt = (int)this.getWorkID();
						dtl.SetValByKey("FID", this.getFID());
						break;
				}
				dtlEn.SetValByKey("RDT", rdt);
				dtlEn.SetValByKey("Rec", WebUser.getNo());
				i++;
				//dtlEn.OID = (int)DBAccess.GenerOID(this.EnsName);
				dtlEn.Insert();
				oid++;
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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

	/** 
	 BP类从表导入
	 
	 @return 
	*/
	private String BPDtlImpByExcel_Imp(DataTable dt, String fk_mapdtl)
	{
		try
		{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 检查两个文件是否一致。 生成要导入的属性
			Entities dtls = ClassFactory.GetEns(this.getFK_MapDtl());
			EntityOID dtlEn = dtls.GetNewEntity instanceof EntityOID ? (EntityOID)dtls.GetNewEntity : null;
			BP.En.Attrs attrs = dtlEn.getEnMap().getAttrs();
			BP.En.Attrs attrsExp = new BP.En.Attrs();

			boolean isHave = false;
			for (DataColumn dc : dt.Columns)
			{
				for (BP.En.Attr attr : attrs)
				{
					if (dc.ColumnName.equals(attr.Desc))
					{
						isHave = true;
						attrsExp.Add(attr);
						continue;
					}

					if (dc.ColumnName.toLowerCase().equals(attr.Key.toLowerCase()))
					{
						isHave = true;
						attrsExp.Add(attr);
						dc.ColumnName = attr.Desc;
					}
				}
			}
			if (isHave == false)
			{
				return "err@您导入的excel文件不符合系统要求的格式，请下载模版文件重新填入。";
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 执行导入数据.

			if (this.GetRequestValInt("DDL_ImpWay") == 0)
			{
				BP.DA.DBAccess.RunSQL("DELETE FROM " + dtlEn.EnMap.PhysicsTable + " WHERE RefPK='" + this.GetRequestVal("RefPKVal") + "'");
			}

			int i = 0;
			long oid = BP.DA.DBAccess.GenerOID(dtlEn.EnMap.PhysicsTable, dt.Rows.size());
			String rdt = BP.DA.DataType.CurrentData;

			String errMsg = "";
			for (DataRow dr : dt.Rows)
			{
				dtlEn = dtls.GetNewEntity instanceof EntityOID ? (EntityOID)dtls.GetNewEntity : null;
				dtlEn.ResetDefaultVal();

				for (BP.En.Attr attr : attrsExp)
				{
					if (attr.UIVisible == false || dr.get(attr.Desc) == DBNull.Value)
					{
						continue;
					}
					String val = dr.get(attr.Desc).toString();
					if (val == null)
					{
						continue;
					}
					val = val.trim();
					switch (attr.MyFieldType)
					{
						case FieldType.Enum:
						case FieldType.PKEnum:
							SysEnums ses = new SysEnums(attr.UIBindKey);
							boolean isHavel = false;
							for (SysEnum se : ses)
							{
								if (val.equals(se.Lab))
								{
									val = se.IntKey.toString();
									isHavel = true;
									break;
								}
							}
							if (isHavel == false)
							{
								errMsg += "@数据格式不规范,第(" + i + ")行，列(" + attr.getDesc() + ")，数据(" + val + ")不符合格式,改值没有在枚举列表里.";
								val = attr.DefaultVal.toString();
							}
							break;
						case FieldType.FK:
						case FieldType.PKFK:
							Entities ens = null;
							if (attr.UIBindKey.Contains("."))
							{
								ens = BP.En.ClassFactory.GetEns(attr.UIBindKey);
							}
							else
							{
								ens = new GENoNames(attr.UIBindKey, "desc");
							}

							ens.RetrieveAll();
							boolean isHavelIt = false;
							for (Entity en : ens)
							{
								if (val.equals(en.GetValStrByKey("Name")))
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

					if (attr.MyDataType == BP.DA.DataType.AppBoolean)
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

					dtlEn.SetValByKey(attr.Key, val);
				}

				dtlEn.SetValByKey("RefPK", this.GetRequestVal("RefPKVal"));
				i++;

				dtlEn.Insert();
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion  Excel导入.


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 打印.
	public final String Print_Init()
	{
		//string ApplicationPath = this.context.Request.PhysicalApplicationPath;
		String ApplicationPath = HttpContextHelper.RequestApplicationPath;

		BP.WF.Node nd = new BP.WF.Node(this.getFK_Node());
		String path = ApplicationPath + "DataUser\\CyclostyleFile\\FlowFrm\\" + nd.getFK_Flow() + "\\" + nd.getNodeID() + "\\";
		String[] fls = null;
		try
		{
			fls = (new File(path)).list(File::isFile);
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
			dr.set("BillNo", strs[0]);
			dr.set("BillName", strs[1]);

			dt.Rows.add(dr);
		}

		//返回json.
		return BP.Tools.Json.ToJson(dt);
	}
	public final String Print_Done()
	{
		int billIdx = this.GetValIntFromFrmByKey("BillIdx");

		String ApplicationPath = "";
		BP.WF.Node nd = new BP.WF.Node(this.getFK_Node());
		String path = ApplicationPath + "\\DataUser\\CyclostyleFile\\FlowFrm\\" + nd.getFK_Flow() + "\\" + nd.getNodeID() + "\\";
		if ((new File(path)).isDirectory() == false)
		{
			return "err@模版文件没有找到。" + path;
		}

		String[] fls = (new File(path)).list(File::isFile);
		String file = fls[billIdx];
		file = file.replace(ApplicationPath + "DataUser\\CyclostyleFile", "");

		File finfo = new File(file);
		String tempName = finfo.getName().split("[.]", -1)[0];
		String tempNameChinese = finfo.getName().split("[.]", -1)[1];

		String toPath = ApplicationPath + "DataUser\\Bill\\FlowFrm\\" + LocalDateTime.now().toString("yyyyMMdd") + "\\";
		if ((new File(toPath)).isDirectory() == false)
		{
			(new File(toPath)).mkdirs();
		}

		// string billFile = toPath + "\\" + tempName + "." + this.FID + ".doc";
		String billFile = toPath + "\\" + tempNameChinese + "." + this.getWorkID() + ".doc";

		BP.Pub.RTFEngine engine = new BP.Pub.RTFEngine();
		if (tempName.toLowerCase().equals("all"))
		{
			/* 说明要从所有的独立表单上取数据. */
			FrmNodes fns = new FrmNodes(this.getFK_Flow(), this.getFK_Node());
			for (FrmNode fn : fns)
			{
				GEEntity ge = new GEEntity(fn.getFK_Frm(), this.getWorkID());
				engine.AddEn(ge);
				MapDtls mdtls = new MapDtls(fn.getFK_Frm());
				for (MapDtl dtl : mdtls)
				{
					GEDtls enDtls = dtl.HisGEDtl.GetNewEntities instanceof GEDtls ? (GEDtls)dtl.HisGEDtl.GetNewEntities : null;
					enDtls.Retrieve(GEDtlAttr.RefPK, this.getWorkID());
					engine.EnsDataDtls.Add(enDtls);
				}
			}

			// 增加主表.
			GEEntity myge = new GEEntity("ND" + nd.getNodeID(), this.getWorkID());
			engine.AddEn(myge);

			//增加从表
			MapDtls mymdtls = new MapDtls(tempName);
			for (MapDtl dtl : mymdtls)
			{
				GEDtls enDtls = dtl.HisGEDtl.GetNewEntities instanceof GEDtls ? (GEDtls)dtl.HisGEDtl.GetNewEntities : null;
				enDtls.Retrieve(GEDtlAttr.RefPK, this.getWorkID());
				engine.EnsDataDtls.Add(enDtls);
			}

			//增加多附件数据
			FrmAttachments aths = new FrmAttachments(tempName);
			for (FrmAttachment athDesc : aths)
			{
				FrmAttachmentDBs athDBs = new FrmAttachmentDBs();
				if (athDBs.Retrieve(FrmAttachmentDBAttr.FK_FrmAttachment, athDesc.MyPK, FrmAttachmentDBAttr.RefPKVal, this.getWorkID(), "RDT") == 0)
				{
					continue;
				}

				engine.EnsDataAths.Add(athDesc.NoOfObj, athDBs);
			}
			// engine.MakeDoc(file, toPath, tempName + "." + this.WorkID + ".doc", null, false);
			engine.MakeDoc(file, toPath, tempNameChinese + "." + this.getWorkID() + ".doc", false);
		}
		else
		{
			// 增加主表.
			GEEntity myge = new GEEntity(tempName, this.getWorkID());
			engine.HisGEEntity = myge;
			engine.AddEn(myge);

			//增加从表.
			MapDtls mymdtls = new MapDtls(tempName);
			for (MapDtl dtl : mymdtls)
			{
				GEDtls enDtls = dtl.HisGEDtl.GetNewEntities instanceof GEDtls ? (GEDtls)dtl.HisGEDtl.GetNewEntities : null;
				enDtls.Retrieve(GEDtlAttr.RefPK, this.getWorkID());
				engine.EnsDataDtls.Add(enDtls);
			}

			//增加轨迹表.
			Paras ps = new BP.DA.Paras();
			ps.SQL = "SELECT * FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track WHERE ActionType=" + SystemConfig.getAppCenterDBVarStr() + "ActionType AND WorkID=" + SystemConfig.getAppCenterDBVarStr() + "WorkID";
			ps.Add(TrackAttr.ActionType, ActionType.WorkCheck.getValue());
			ps.Add(TrackAttr.WorkID, this.getWorkID());
			engine.dtTrack = BP.DA.DBAccess.RunSQLReturnTable(ps);

			engine.MakeDoc(file, toPath, tempNameChinese + "." + this.getWorkID() + ".doc", false);
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 保存单据，以方便查询.
		Bill bill = new Bill();
		bill.setMyPK( this.getFID() + "_" + this.getWorkID() + "_" + this.getFK_Node() + "_" + billIdx;
		bill.setWorkID(this.getWorkID());
		bill.setFK_Node(this.getFK_Node());
		bill.setFK_Dept(WebUser.getFK_Dept());
		bill.setFK_Emp(WebUser.getNo());

		bill.setUrl("/DataUser/Bill/FlowFrm/" + LocalDateTime.now().toString("yyyyMMdd") + "/" + tempNameChinese + "." + this.getWorkID() + ".doc");
		bill.setFullPath(toPath + file);

		bill.setRDT(DataType.getCurrentDataTime());
		bill.setFK_NY(DataType.CurrentYearMonth);
		bill.setFK_Flow(this.getFK_Flow());
		if (this.getWorkID() != 0)
		{
			GenerWorkFlow gwf = new GenerWorkFlow();
			gwf.setWorkID(this.getWorkID());
			if (gwf.RetrieveFromDBSources() == 1)
			{
				bill.setEmps(gwf.getEmps());
				bill.setFK_Starter(gwf.getStarter());
				bill.setStartDT(gwf.getRDT());
				bill.setTitle(gwf.getTitle());
				bill.setFK_Dept(gwf.getFK_Dept());
			}
		}

		try
		{
			bill.Insert();
		}
		catch (java.lang.Exception e)
		{
			bill.Update();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

		BillTemplates templates = new BillTemplates();
		int iHave = templates.Retrieve(BillTemplateAttr.NodeID, this.getFK_Node(), BillTemplateAttr.BillOpenModel, BillOpenModel.WebOffice.getValue());

		return "url@../WebOffice/PrintOffice.htm?MyPK=" + bill.MyPK;


	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 打印.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 附件组件.
	/** 
	 执行删除
	 
	 @return 
	*/
	public final String AttachmentUpload_Del()
	{
		//执行删除.
		String delPK = this.GetRequestVal("DelPKVal");

		FrmAttachmentDB delDB = new FrmAttachmentDB();
		delDB.setMyPK( delPK == null ? this.getMyPK() : delPK;
		delDB.RetrieveFromDBSources();
		delDB.Delete(); //删除上传的文件.
		return "删除成功.";
	}
	public final String AttachmentUpload_DownByStream()
	{
		// return AttachmentUpload_Down(true);
		return AttachmentUpload_Down();
	}
	/** 
	 下载
	 
	 @return 
	*/
	public final String AttachmentUpload_Down()
	{
		FrmAttachmentDB downDB = new FrmAttachmentDB();
		downDB.setMyPK( this.getMyPK();
		downDB.Retrieve();

		FrmAttachment dbAtt = new FrmAttachment();
		dbAtt.setMyPK( downDB.FK_FrmAttachment;
		dbAtt.Retrieve();

		if (dbAtt.AthSaveWay == AthSaveWay.IISServer)
		{
			return "url@" + DataType.PraseStringToUrlFileName(downDB.FileFullName);
		}

		if (dbAtt.AthSaveWay == AthSaveWay.FTPServer)
		{
			String fileFullName = downDB.GenerTempFile(dbAtt.AthSaveWay);
			return "url@" + DataType.PraseStringToUrlFileName(fileFullName);
		}

		if (dbAtt.AthSaveWay == AthSaveWay.DB)
		{
			return "fromdb";
		}
		return "正在下载.";
	}

	public final void AttachmentDownFromByte()
	{
		FrmAttachmentDB downDB = new FrmAttachmentDB();
		downDB.setMyPK( this.getMyPK();
		downDB.Retrieve();
		downDB.FileName = HttpUtility.UrlEncode(downDB.FileName);
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte[] byteList = downDB.GetFileFromDB("FileDB", null);
		byte[] byteList = downDB.GetFileFromDB("FileDB", null);
		if (byteList != null)
		{
			//HttpContext.Current.Response.Charset = "GB2312";
			//HttpContext.Current.Response.AddHeader("Content-Disposition", "attachment;filename=" + downDB.FileName);
			//HttpContext.Current.Response.ContentType = "application/octet-stream;charset=gb2312";
			//HttpContext.Current.Response.BinaryWrite(byteList);
			//HttpContext.Current.Response.End();
			//HttpContext.Current.Response.Close();
			HttpContextHelper.ResponseWriteFile(byteList, downDB.FileName, "application/octet-stream;charset=gb2312");
		}
	}
	/** 
	 附件ID.
	*/
	public final String getFK_FrmAttachment()
	{
		return this.GetRequestVal("FK_FrmAttachment");
	}
	public final BP.Sys.FrmAttachment GenerAthDescOfFoolTruck()
	{
		FoolTruckNodeFrm sln = new FoolTruckNodeFrm();
		sln.setFrmSln(-1);
		String fromFrm = this.GetRequestVal("FromFrm");
		sln.setMyPK( fromFrm + "_" + this.getFK_Node() + "_" + this.getFK_Flow();
		int result = sln.RetrieveFromDBSources();
		BP.Sys.FrmAttachment athDesc = new BP.Sys.FrmAttachment();
		athDesc.setMyPK( this.getFK_FrmAttachment();
		athDesc.RetrieveFromDBSources();

		/*没有查询到解决方案, 就是只读方案 */
		if (result == 0 || sln.getFrmSln() == 1)
		{
			athDesc.IsUpload = false;
			athDesc.IsDownload = true;
			athDesc.HisDeleteWay = AthDeleteWay.None; //删除模式.
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
			BP.Sys.FrmAttachment athDescNode = new BP.Sys.FrmAttachment();
			athDescNode.setMyPK( this.getFK_FrmAttachment() + "_" + this.getFK_Node();
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
	public final BP.Sys.FrmAttachment GenerAthDesc()
	{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 为累加表单做的特殊判断.
		if (this.GetRequestValInt("FormType") == 10)
		{
			if (this.getFK_FrmAttachment().contains(this.getFK_MapData()) == false)
			{
				return GenerAthDescOfFoolTruck(); //如果当前表单的ID。
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

		BP.Sys.FrmAttachment athDesc = new BP.Sys.FrmAttachment();
		athDesc.setMyPK( this.getFK_FrmAttachment();
		if (this.getFK_Node() == 0 || this.getFK_Flow() == null)
		{
			athDesc.RetrieveFromDBSources();
			return athDesc;
		}

		athDesc.setMyPK( this.getFK_FrmAttachment();
		int result = athDesc.RetrieveFromDBSources();

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 判断是否是明细表的多附件.
		if (result == 0 && DataType.IsNullOrEmpty(this.getFK_Flow()) == false && this.getFK_FrmAttachment().contains("AthMDtl"))
		{
			athDesc.FK_MapData = this.getFK_MapData();
			athDesc.NoOfObj = "AthMDtl";
			athDesc.Name = "我的从表附件";
			athDesc.UploadType = AttachmentUploadType.Multi;
			athDesc.Insert();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 判断是否是明细表的多附件。

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 判断是否可以查询出来，如果查询不出来，就可能是公文流程。
		if (result == 0 && DataType.IsNullOrEmpty(this.getFK_Flow()) == false && this.getFK_FrmAttachment().contains("DocMultiAth"))
		{
			/*如果没有查询到它,就有可能是公文多附件被删除了.*/
			athDesc.setMyPK( this.getFK_FrmAttachment();
			athDesc.NoOfObj = "DocMultiAth";
			athDesc.FK_MapData = this.getFK_MapData();
			athDesc.Exts = "*.*";

			//存储路径.
			athDesc.SaveTo = "/DataUser/UploadFile/";
			athDesc.IsNote = false; //不显示note字段.
			athDesc.IsVisable = false; // 让其在form 上不可见.

			//位置.
			athDesc.X = (float)94.09;
			athDesc.Y = (float)333.18;
			athDesc.W = (float)626.36;
			athDesc.H = (float)150;

			//多附件.
			athDesc.UploadType = AttachmentUploadType.Multi;
			athDesc.Name = "公文多附件(系统自动增加)";
			athDesc.SetValByKey("AtPara", "@IsWoEnablePageset=1@IsWoEnablePrint=1@IsWoEnableViewModel=1@IsWoEnableReadonly=0@IsWoEnableSave=1@IsWoEnableWF=1@IsWoEnableProperty=1@IsWoEnableRevise=1@IsWoEnableIntoKeepMarkModel=1@FastKeyIsEnable=0@IsWoEnableViewKeepMark=1@FastKeyGenerRole=@IsWoEnableTemplete=1");
			athDesc.Insert();

			//有可能在其其它的节点上没有这个附件，所以也要循环增加上它.
			BP.WF.Nodes nds = new BP.WF.Nodes(this.getFK_Flow());
			for (BP.WF.Node nd : nds.ToJavaList())
			{
				athDesc.FK_MapData = "ND" + nd.getNodeID();
				athDesc.setMyPK( athDesc.FK_MapData + "_" + athDesc.NoOfObj;
				if (athDesc.IsExits == true)
				{
					continue;
				}

				athDesc.Insert();
			}

			//重新查询一次，把默认值加上.
			athDesc.RetrieveFromDBSources();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 判断是否可以查询出来，如果查询不出来，就可能是公文流程。

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 处理权限方案。
		if (this.getFK_Node() != 0)
		{
			String fk_mapdata = this.getFK_MapData();
			if (this.getFK_FrmAttachment().contains("AthMDtl") == true)
			{
				fk_mapdata = this.GetRequestVal("FFK_MapData");
			}

			if (DataType.IsNullOrEmpty(fk_mapdata))
			{
				fk_mapdata = this.GetRequestVal("FK_MapData");
			}



			Node nd = new Node(this.getFK_Node());
			if (nd.getHisFormType() == NodeFormType.SheetTree || nd.getHisFormType() == NodeFormType.RefOneFrmTree)
			{
				FrmNode fn = new FrmNode(nd.getFK_Flow(), nd.getNodeID(), fk_mapdata);
				if (fn.getFrmSln() == FrmSln.Default)
				{
					if (fn.getWhoIsPK() == WhoIsPK.FID)
					{
						athDesc.HisCtrlWay = AthCtrlWay.FID;
					}

					if (fn.getWhoIsPK() == WhoIsPK.PWorkID)
					{
						athDesc.HisCtrlWay = AthCtrlWay.PWorkID;
					}


				}

				if (fn.getFrmSln() == FrmSln.Readonly)
				{
					if (fn.getWhoIsPK() == WhoIsPK.FID)
					{
						athDesc.HisCtrlWay = AthCtrlWay.FID;
					}

					if (fn.getWhoIsPK() == WhoIsPK.PWorkID)
					{
						athDesc.HisCtrlWay = AthCtrlWay.PWorkID;
					}

					athDesc.HisDeleteWay = AthDeleteWay.None;
					athDesc.IsUpload = false;
					athDesc.IsDownload = true;
					athDesc.setMyPK( this.getFK_FrmAttachment();
					return athDesc;
				}

				if (fn.getFrmSln() == FrmSln.Self)
				{
					if (this.getFK_FrmAttachment().contains("AthMDtl") == true)
					{
						athDesc.setMyPK( this.getFK_MapData() + "_" + nd.getNodeID() + "_AthMDtl";
						athDesc.RetrieveFromDBSources();
					}
					else
					{
						athDesc.setMyPK( this.getFK_FrmAttachment() + "_" + nd.getNodeID();
						athDesc.RetrieveFromDBSources();
					}
					athDesc.setMyPK( this.getFK_FrmAttachment();
					return athDesc;
				}
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 处理权限方案。


		return athDesc;
	}
	/** 
	 打包下载.
	 
	 @return 
	*/
	public final String AttachmentUpload_DownZip()
	{
		String zipName = this.getWorkID() + "_" + this.getFK_FrmAttachment();

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 处理权限控制.
		BP.Sys.FrmAttachment athDesc = this.GenerAthDesc();

		//查询出来数据实体.
		BP.Sys.FrmAttachmentDBs dbs = BP.WF.Glo.GenerFrmAttachmentDBs(athDesc, this.getPKVal(), this.getFK_FrmAttachment());
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 处理权限控制.

		if (dbs.size() == 0)
		{
			return "err@文件不存在，不需打包下载。";
		}

		String basePath = SystemConfig.PathOfDataUser + "Temp";
		String tempUserPath = basePath + "\\" + WebUser.getNo();
		String tempFilePath = basePath + "\\" + WebUser.getNo() + "\\" + this.getOID();
		String zipPath = basePath + "\\" + WebUser.getNo();
		String zipFile = zipPath + "\\" + zipName + ".zip";

		String info = "";
		try
		{
			//删除临时文件，保证一个用户只能存一份，减少磁盘占用空间.
			info = "@创建用户临时目录:" + tempUserPath;
			if ((new File(tempUserPath)).isDirectory() == false)
			{
				(new File(tempUserPath)).mkdirs();
			}

			//如果有这个临时的目录就把他删除掉.
			if ((new File(tempFilePath)).isDirectory() == true)
			{
				Directory.Delete(tempFilePath, true);
			}

			(new File(tempFilePath)).mkdirs();
		}
		catch (RuntimeException ex)
		{
			return "err@组织临时目录出现错误:" + ex.getMessage();
		}

		try
		{
			for (FrmAttachmentDB db : dbs)
			{
				String copyToPath = tempFilePath;

				//求出文件路径.
				String fileTempPath = db.GenerTempFile(athDesc.AthSaveWay);
				String fileTempDecryPath = fileTempPath;
				//获取文件是否加密
				boolean fileEncrypt = SystemConfig.IsEnableAthEncrypt;
				boolean isEncrypt = db.GetParaBoolen("IsEncrypt");
				if (fileEncrypt == true && isEncrypt == true)
				{
					fileTempDecryPath = fileTempPath + ".tmp";
					EncHelper.DecryptDES(fileTempPath, fileTempDecryPath);

				}
				if (DataType.IsNullOrEmpty(db.Sort) == false)
				{
					copyToPath = tempFilePath + "//" + db.Sort;
					if ((new File(copyToPath)).isDirectory() == false)
					{
						(new File(copyToPath)).mkdirs();
					}
				}
				//新文件目录
				copyToPath = copyToPath + "//" + db.FileName;

				if ((new File(fileTempDecryPath)).isFile() == true)
				{
					Files.copy(Paths.get(fileTempDecryPath), Paths.get(copyToPath), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
				}
				if (fileEncrypt == true && isEncrypt == true)
				{
					(new File(fileTempDecryPath)).delete();
				}
			}
		}
		catch (RuntimeException ex)
		{
			return "err@组织文件期间出现错误:" + ex.getMessage();
		}

		try
		{
			while ((new File(zipFile)).isFile() == true)
			{
				(new File(zipFile)).delete();
			}
			//执行压缩.
			FastZip fz = new FastZip();
			fz.CreateZip(zipFile, tempFilePath, true, "");
			//删除临时文件夹
			Directory.Delete(tempFilePath, true);
		}
		catch (RuntimeException ex)
		{
			return "err@执行压缩出现错误:" + ex.getMessage() + ",路径tempPath:" + tempFilePath + ",zipFile=" + zipFile;
		}

		if ((new File(zipFile)).isFile() == false)
		{
			return "err@压缩文件未生成成功,请在点击一次.";
		}

		zipName = DataType.PraseStringToUrlFileName(zipName);

		GenerWorkerList gwf = new GenerWorkerList();
		gwf.Retrieve(GenerWorkerListAttr.FK_Emp, WebUser.getNo(), GenerWorkerListAttr.FK_Node, this.getFK_Node(), GenerWorkerListAttr.WorkID, this.getWorkID());

		String str = gwf.GetParaString(athDesc.NoOfObj);
		str += "ALL";
		gwf.SetPara(athDesc.NoOfObj, str);
		gwf.Update();

		String url = HttpContextHelper.RequestApplicationPath + "DataUser/Temp/" + WebUser.getNo() + "/" + zipName + ".zip";
		return "url@" + url;

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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 附件组件


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 必须传递参数
	/** 
	 执行的内容
	*/
	public final String getDoWhat()
	{
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
	public final String getUserNo()
	{
		return this.GetRequestVal("UserNo");
	}
	/** 
	 用户的安全校验码(请参考集成章节)
	*/
	public final String getSID()
	{
		return this.GetRequestVal("SID");
	}
	public final String getAppPath()
	{
		return BP.WF.Glo.getCCFlowAppPath();
	}
	public final String Port_Init()
	{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 安全性校验.
		if (this.getUserNo() == null || this.getSID() == null || this.getDoWhat() == null || this.getFrmID() == null)
		{
			return "err@必要的参数没有传入，请参考接口规则。";
		}

		if (BP.WF.Dev2Interface.Port_CheckUserLogin(this.getUserNo(), this.getSID()) == false)
		{
			return "err@非法的访问，请与管理员联系。SID=" + this.getSID();
		}

		if (!this.getUserNo().equals(WebUser.getNo()))
		{
			BP.WF.Dev2Interface.Port_SigOut();
			BP.WF.Dev2Interface.Port_Login(this.getUserNo());
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 安全性校验.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 生成参数串.
		String paras = "";
		for (String str : HttpContextHelper.RequestParamKeys)
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
				case "sid":
					break;
				default:
					paras += "&" + str + "=" + val;
					break;
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion




}