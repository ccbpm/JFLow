package bp.wf;

import bp.difference.SystemConfig;
import bp.sys.*;
import bp.da.*;
import bp.en.*;
import bp.tools.*;
import bp.web.*;
import bp.port.*;
import bp.wf.rpt.*;
import bp.wf.template.*;
//import bp.tools.ZipCompress;
//import iTextSharp.text.*;
//import iTextSharp.text.pdf.*;
import bp.*;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.time.*;

public class MakeForm2Html
{

	private static StringBuilder GenerHtmlOfFool(MapData mapData, String frmID, long workid, Entity en, String path, String flowNo, String FK_Node) throws Exception {
		return GenerHtmlOfFool(mapData, frmID, workid, en, path, flowNo, FK_Node, NodeFormType.FoolForm);
	}

	private static StringBuilder GenerHtmlOfFool(MapData mapData, String frmID, long workid, Entity en, String path, String flowNo) throws Exception {
		return GenerHtmlOfFool(mapData, frmID, workid, en, path, flowNo, null, NodeFormType.FoolForm);
	}

	private static StringBuilder GenerHtmlOfFool(MapData mapData, String frmID, long workid, Entity en, String path) throws Exception {
		return GenerHtmlOfFool(mapData, frmID, workid, en, path, null, null, NodeFormType.FoolForm);
	}

//ORIGINAL LINE: private static StringBuilder GenerHtmlOfFool(MapData mapData, string frmID, Int64 workid, Entity en, string path, string flowNo = null, string FK_Node = null, NodeFormType formType = NodeFormType.FoolForm)
	private static StringBuilder GenerHtmlOfFool(MapData mapData, String frmID, long workid, Entity en, String path, String flowNo, String FK_Node, NodeFormType formType) throws Exception {
		StringBuilder sb = new StringBuilder();
		//字段集合.
		MapAttrs mapAttrs = new MapAttrs(frmID);
		Attrs attrs = null;
		GroupFields gfs = null;
		if (formType == NodeFormType.FoolTruck && DataType.IsNullOrEmpty(FK_Node) == false)
		{
			Node nd = new Node(FK_Node);
			Work wk = nd.getHisWork();
			wk.setOID(workid);
			wk.RetrieveFromDBSources();

			/* 求出来走过的表单集合 */
			String sql = "SELECT NDFrom FROM ND" + Integer.parseInt(flowNo) + "Track A, WF_Node B ";
			sql += " WHERE A.NDFrom=B.NodeID  ";
			sql += "  AND (ActionType=" + ActionType.Forward.getValue() + " OR ActionType=" + ActionType.Start.getValue() + "  OR ActionType=" + ActionType.Skip.getValue() + ")  ";
			sql += "  AND B.FormType=" + NodeFormType.FoolTruck.getValue() + " "; // 仅仅找累加表单.
			sql += "  AND NDFrom!=" + Integer.parseInt(FK_Node.replace("ND", "")) + " "; //排除当前的表单.


			sql += "  AND (A.WorkID=" + workid + ") ";
			sql += " ORDER BY A.RDT ";

			// 获得已经走过的节点IDs.
			DataTable dtNodeIDs = DBAccess.RunSQLReturnTable(sql);
			String frmIDs = "";
			if (dtNodeIDs.Rows.size() > 0)
			{
				//把所有的节点字段.
				for (DataRow dr : dtNodeIDs.Rows)
				{
					if (frmIDs.contains("ND" + dr.getValue(0).toString()) == true)
					{
						continue;
					}
					frmIDs += "'ND" + dr.getValue(0).toString() + "',";
				}
			}

			if (frmIDs.equals(""))
			{
				frmIDs = "'" + mapData.getNo() + "'";
			}
			else
			{
				frmIDs = frmIDs.substring(0, frmIDs.length() - 1);
			}

			GenerWorkFlow gwf = new GenerWorkFlow(workid);
			if (gwf.getWFState() == WFState.Complete)
			{
				frmIDs = frmIDs + ",'" + FK_Node + "'";
			}
			gfs = new GroupFields();
			gfs.RetrieveIn(GroupFieldAttr.FrmID, "(" + frmIDs + ")");

			mapAttrs = new MapAttrs();
			mapAttrs.RetrieveIn(MapAttrAttr.FK_MapData, "(" + frmIDs + ")");
		}
		else
		{
			gfs = new GroupFields(frmID);
			attrs = en.getEnMap().getAttrs();
		}

		//生成表头.
		String frmName = mapData.getName();
		if (bp.difference.SystemConfig.getAppSettings().get("CustomerNo").equals("TianYe"))
		{
			frmName = "";
		}

		sb.append(" <table style='width:950px;height:auto;' >");

		///#region 生成头部信息.
		sb.append("<tr>");

		sb.append("<td colspan=4 >");

		sb.append("<table border=0 style='width:950px;'>");

		sb.append("<tr  style='border:0px;' >");

		//二维码显示
		boolean IsHaveQrcode = true;
		if (bp.difference.SystemConfig.GetValByKeyBoolen("IsShowQrCode", false) == false)
		{
			IsHaveQrcode = false;
		}

		//判断当前文件是否存在图片
		boolean IsHaveImg = false;
		String IconPath = path + "/icon.png";
		if ((new File(IconPath)).isFile() == true)
		{
			IsHaveImg = true;
		}
		if (IsHaveImg == true)
		{
			sb.append("<td>");
			sb.append("<img src='icon.png' style='height:100px;border:0px;' />");
			sb.append("</td>");
		}
		if (IsHaveImg == false && IsHaveQrcode == false)
		{
			sb.append("<td  colspan=6>");
		}
		else if ((IsHaveImg == true && IsHaveQrcode == false) || (IsHaveImg == false && IsHaveQrcode == true))
		{
			sb.append("<td  colspan=5>");
		}
		else
		{
			sb.append("<td  colspan=4>");
		}

		sb.append("<br><h2><b>" + frmName + "</b></h2>");
		sb.append("</td>");

		if (IsHaveQrcode == true)
		{
			sb.append("<td>");
			sb.append(" <img src='QR.png' style='height:100px;'  />");
			sb.append("</td>");
		}

		sb.append("</tr>");
		sb.append("</table>");

		sb.append("</td>");
		///#endregion 生成头部信息.

		if (DataType.IsNullOrEmpty(FK_Node) == false && DataType.IsNullOrEmpty(flowNo) == false)
		{
			Node nd = new Node(Integer.parseInt(FK_Node.replace("ND","")));
			if (frmID.startsWith("ND") == true && nd.getFrmWorkCheckSta() != FrmWorkCheckSta.Disable)
			{
				Object tempVar = gfs.GetEntityByKey(GroupFieldAttr.CtrlType, "FWC");
				GroupField gf = tempVar instanceof GroupField ? (GroupField)tempVar : null;
				if (gf == null)
				{
					gf = new GroupField();
					gf.setOID(100);
					gf.setFrmID(nd.getNodeFrmID());
					gf.setCtrlType("FWC");
					gf.setCtrlID("FWCND" + nd.getNodeID());
					gf.setIdx(100);
					gf.setLab("审核信息");
					gfs.AddEntity(gf);
				}
			}
		}


		for (GroupField gf : gfs.ToJavaList())
		{
			//输出标题.
			if (!gf.getCtrlType().equals("Ath"))
			{
				sb.append(" <tr>");
				sb.append("  <th colspan=4><b>" + gf.getLab() + "</b></th>");
				sb.append(" </tr>");
			}

			///#region 输出字段.
			if (gf.getCtrlID().equals("") && gf.getCtrlType().equals(""))
			{
				boolean isDropTR = true;
				String html = "";
				for (MapAttr attr : mapAttrs.ToJavaList())
				{
					//处理隐藏字段，如果是不可见并且是启用的就隐藏.
					if (attr.getUIVisible()== false)
					{
						continue;
					}
					if (attr.getGroupID() != attr.getGroupID())
					{
						continue;
					}
					//处理分组数据，非当前分组的数据不输出
					if (attr.getGroupID() != gf.getOID())
					{
						continue;
					}

					String text = "";

					switch (attr.getLGType())
					{
						case Normal: // 输出普通类型字段.
							if (attr.getMyDataType() == 1 && attr.getUIContralType().getValue() == DataType.AppString)
							{

								if (attrs.contains(attr.getKeyOfEn() + "Text") == true)
								{
									text = en.GetValRefTextByKey(attr.getKeyOfEn());
								}
								if (DataType.IsNullOrEmpty(text))
								{
									if (attrs.contains(attr.getKeyOfEn() + "T") == true)
									{
										text = en.GetValStrByKey(attr.getKeyOfEn() + "T");
									}
								}
							}
							else
							{
								//判断是不是图片签名
								if (attr.getIsSigan() == true)
								{
									String SigantureNO = en.GetValStrByKey(attr.getKeyOfEn());
									String src = bp.difference.SystemConfig.getHostURL() + "/DataUser/Siganture/";
									text = "<img src='" + src + SigantureNO + ".JPG' title='" + SigantureNO + "' onerror='this.src=\"" + src + "Siganture.JPG\"' style='height:50px;'  alt='图片丢失' /> ";
								}
								else
								{
									text = en.GetValStrByKey(attr.getKeyOfEn());
								}
								if (attr.getTextModel() == 3)
								{
									text = text.replace("white-space: nowrap;", "");
								}
							}

							break;
						case Enum:
							if (attr.getUIContralType() == UIContralType.CheckBok)
							{
								String s = en.GetValStrByKey(attr.getKeyOfEn()) + ",";
								SysEnums enums = new SysEnums(attr.getUIBindKey());
								for (SysEnum se : enums.ToJavaList())
								{
									if (s.indexOf(se.getIntKey() + ",") != -1)
									{
										text += se.getLab() + " ";
									}
								}

							}
							else
							{
								text = en.GetValRefTextByKey(attr.getKeyOfEn());
							}
							break;
						case FK:
							text = en.GetValRefTextByKey(attr.getKeyOfEn());
							break;
						default:
							break;
					}

					if (attr.getIsBigDoc())
					{
						//这几种字体生成 pdf都乱码
						text = text.replace("仿宋,", "宋体,");
						text = text.replace("仿宋;", "宋体;");
						text = text.replace("仿宋\"", "宋体\"");
						text = text.replace("黑体,", "宋体,");
						text = text.replace("黑体;", "宋体;");
						text = text.replace("黑体\"", "宋体\"");
						text = text.replace("楷体,", "宋体,");
						text = text.replace("楷体;", "宋体;");
						text = text.replace("楷体\"", "宋体\"");
						text = text.replace("隶书,", "宋体,");
						text = text.replace("隶书;", "宋体;");
						text = text.replace("隶书\"", "宋体\"");
					}

					if (attr.getMyDataType() == DataType.AppBoolean)
					{
						if (DataType.IsNullOrEmpty(text) || text.equals("0"))
						{
							text = "[&#10005]" + attr.getName();
						}
						else
						{
							text = "[&#10004]" + attr.getName();
						}
					}

					//线性展示并且colspan=3
					if (attr.getColSpan() == 3 || (attr.getColSpan() == 4 && attr.getUIHeightInt() < 30))
					{
						isDropTR = true;
						html += " <tr>";
						html += " <td  class='FoolFrmFieldCtrl' style='width:143px' >" + attr.getName() + "</td>";
						html += " <td  ColSpan=3 style='width:712.5px' class='FContext'>";
						html += text;
						html += " </td>";
						html += " </tr>";
						continue;
					}

					//线性展示并且colspan=4
					if (attr.getColSpan() == 4)
					{
						isDropTR = true;
						html += " <tr>";
						html += " <td ColSpan=4 class='FoolFrmFieldCtrl' >" + attr.getName() + "</td>";
						html += " </tr>";
						html += " <tr>";
						html += " <td ColSpan=4 class='FContext'>";
						html += text;
						html += " </td>";
						html += " </tr>";
						continue;
					}

					if (isDropTR == true)
					{
						html += " <tr>";
						html += " <td class='FoolFrmFieldCtrl' style='width:143px'>" + attr.getName() + "</td>";
						html += " <td class='FContext' style='width:332px'>";
						html += text;
						html += " </td>";
						isDropTR = !isDropTR;
						continue;
					}

					if (isDropTR == false)
					{
						html += " <td  class='FoolFrmFieldCtrl'style='width:143px'>" + attr.getName() + "</td>";
						html += " <td class='FContext' style='width:332px'>";
						html += text;
						html += " </td>";
						html += " </tr>";
						isDropTR = !isDropTR;
						continue;
					}
				}
				sb.append(html); //增加到里面.
				continue;
			}
			///#endregion 输出字段.

			///#region 如果是从表.
			if (gf.getCtrlType().equals("Dtl"))
			{
				if (DataType.IsNullOrEmpty(gf.getCtrlID()) == true)
				{
					continue;
				}
				/* 如果是从表 */
				MapAttrs attrsOfDtls = null;
				try
				{
					attrsOfDtls = new MapAttrs(gf.getCtrlID());
				}
				catch (RuntimeException ex)
				{
				}

				///#region 输出标题.
				sb.append("<tr><td valign=top colspan=4 >");

				sb.append("<table style='wdith:100%' >");
				sb.append("<tr>");
				for (MapAttr item : attrsOfDtls.ToJavaList())
				{
					if (item.getKeyOfEn().equals("OID"))
					{
						continue;
					}
					if (item.getUIVisible() == false)
					{
						continue;
					}

					sb.append("<th stylle='width:" + item.getUIWidthInt() + "px;'>" + item.getName() + "</th>");
				}
				sb.append("</tr>");
				///#endregion 输出标题.


				///#region 输出数据.
				GEDtls dtls = new GEDtls(gf.getCtrlID());
				dtls.Retrieve(GEDtlAttr.RefPK, workid,"OID");
				for (GEDtl dtl : dtls.ToJavaList())
				{
					sb.append("<tr>");

					for (MapAttr item : attrsOfDtls.ToJavaList())
					{

						if (item.getKeyOfEn().equals("OID") || item.getUIVisible() == false)
						{
							continue;
						}
						String text = "";
						switch (item.getLGType())
						{
							case Normal: // 输出普通类型字段.
								if (item.getMyDataType() == 1 && item.getUIContralType().getValue() == DataType.AppString)
								{

									if (attrs.contains(item.getKeyOfEn() + "Text") == true)
									{
										text = dtl.GetValRefTextByKey(item.getKeyOfEn());
									}
									if (DataType.IsNullOrEmpty(text))
									{
										if (attrs.contains(item.getKeyOfEn() + "T") == true)
										{
											text = dtl.GetValStrByKey(item.getKeyOfEn() + "T");
										}
									}
								}
								else
								{

									text = dtl.GetValStrByKey(item.getKeyOfEn());

									if (item.getIsRichText() == true)
									{
										text = text.replace("white-space: nowrap;", "");
									}
								}

								break;
							case Enum:
								if (item.getUIContralType() == UIContralType.CheckBok)
								{
									String s = en.GetValStrByKey(item.getKeyOfEn()) + ",";
									SysEnums enums = new SysEnums(item.getUIBindKey());
									for (SysEnum se : enums.ToJavaList())
									{
										if (s.indexOf(se.getIntKey() + ",") != -1)
										{
											text += se.getLab() + " ";
										}
									}

								}
								else
								{
									text = dtl.GetValRefTextByKey(item.getKeyOfEn());
								}
								break;
							case FK:
								text = dtl.GetValRefTextByKey(item.getKeyOfEn());
								break;
							default:
								break;
						}

						if (item.getUIContralType() == UIContralType.DDL)
						{
							sb.append("<td>" + text + "</td>");
							continue;
						}

						if (item.getIsNum())
						{
							sb.append("<td style='text-align:right' >" + text + "</td>");
							continue;
						}

						sb.append("<td>" + text + "</td>");
					}
					sb.append("</tr>");
				}
				///#endregion 输出数据.


				sb.append("</table>");

				sb.append(" </td>");
				sb.append(" </tr>");
			}
			///#endregion 如果是从表.

			///#region 如果是附件.
			if (gf.getCtrlType().equals("Ath"))
			{
				if (DataType.IsNullOrEmpty(gf.getCtrlID()) == true)
				{
					continue;
				}
				FrmAttachment ath = new FrmAttachment();
				ath.setMyPK(gf.getCtrlID());
				if (ath.RetrieveFromDBSources() == 0)
				{
					continue;
				}
				if (ath.getIsVisable() == false)
				{
					continue;
				}

				sb.append(" <tr>");
				sb.append("  <th colspan=4><b>" + gf.getLab() + "</b></th>");
				sb.append(" </tr>");

				FrmAttachmentDBs athDBs = bp.wf.Glo.GenerFrmAttachmentDBs(ath, String.valueOf(workid), ath.getMyPK(), workid);


				if (ath.getUploadType() == AttachmentUploadType.Single)
				{
					/* 单个文件 */
					sb.append("<tr><td colspan=4>单附件没有转化:" + ath.getMyPK() + "</td></td>");
					continue;
				}

				if (ath.getUploadType() == AttachmentUploadType.Multi)
				{
					sb.append("<tr><td valign=top colspan=4 >");
					sb.append("<ul>");

					//判断是否有这个目录.
					if ((new File(path + "/pdf/")).isDirectory() == false)
					{
						(new File(path + "/pdf/")).mkdirs();
					}

					for (FrmAttachmentDB item : athDBs.ToJavaList())
					{
						String fileTo = path + "/pdf/" + item.getFileName();
						//加密信息
						boolean fileEncrypt = bp.difference.SystemConfig.getIsEnableAthEncrypt();
						boolean isEncrypt = item.GetParaBoolen("IsEncrypt");
						///#region 从ftp服务器上下载.
						if (ath.getAthSaveWay() == AthSaveWay.FTPServer)
						{
							try
							{
								if ((new File(fileTo)).isFile() == true)
								{
									(new File(fileTo)).delete(); //rn "err@删除已经存在的文件错误,请检查iis的权限:" + ex.getMessage();
								}

								//把文件copy到,
								String file = item.GenerTempFile(ath.getAthSaveWay());

								String fileTempDecryPath = file;
								if (fileEncrypt == true && isEncrypt == true)
								{
									fileTempDecryPath = file + ".tmp";
									AesEncodeUtil.decryptFile(file, fileTempDecryPath);

								}
								Files.copy(Paths.get(fileTempDecryPath), Paths.get(fileTo), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);

								sb.append("<li><a href='" + bp.difference.SystemConfig.GetValByKey("HostURL", "") + "/DataUser/InstancePacketOfData/" + FK_Node + "/" + workid + "/" + "pdf/" + item.getFileName() + "'>" + item.getFileName() + "</a></li>");
							}
							catch (RuntimeException | IOException ex)
							{
								sb.append("<li>" + item.getFileName() + "(<font color=red>文件未从ftp下载成功{" + ex.getMessage() + "}</font>)</li>");
							}
						}
						///#endregion 从ftp服务器上下载.


						///#region 从iis服务器上下载.
						if (ath.getAthSaveWay() == AthSaveWay.IISServer)
						{
							try
							{

								String fileTempDecryPath = item.getFileFullName();
								if (fileEncrypt == true && isEncrypt == true)
								{
									fileTempDecryPath = item.getFileFullName() + ".tmp";
									AesEncodeUtil.decryptFile(item.getFileFullName(), fileTempDecryPath);

								}

								//把文件copy到,
								Files.copy(Paths.get(fileTempDecryPath), Paths.get(fileTo), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);

								sb.append("<li><a href='" + bp.difference.SystemConfig.GetValByKey("HostURL", "") + "/DataUser/InstancePacketOfData/" + frmID + "/" + workid + "/" + "pdf/" + item.getFileName() + "'>" + item.getFileName() + "</a></li>");
							}
							catch (RuntimeException ex)
							{
								sb.append("<li>" + item.getFileName() + "(<font color=red>文件未从web下载成功{" + ex.getMessage() + "}</font>)</li>");
							}
						}

					}
					sb.append("</ul>");
					sb.append("</td></tr>");
				}

			}
			///#endregion 如果是附件.

			//如果是IFrame页面
			if (gf.getCtrlType().equals("Frame") && flowNo != null)
			{
				if (DataType.IsNullOrEmpty(gf.getCtrlID()) == true)
				{
					continue;
				}
				sb.append("<tr>");
				sb.append("  <td colspan='4' >");

				//根据GroupID获取对应的
				MapFrame frame = new MapFrame(gf.getCtrlID());
				//获取URL
				String url = frame.getURL();

				//替换URL的
				url = url.replace("@basePath", bp.difference.SystemConfig.getHostURLOfBS());
				//替换系统参数
				url = url.replace("@WebUser.No", WebUser.getNo());
				url = url.replace("@WebUser.Name;", WebUser.getName());
				url = url.replace("@WebUser.FK_DeptName;", WebUser.getFK_DeptName());
				url = url.replace("@WebUser.FK_Dept;", WebUser.getFK_Dept());

				//替换参数
				if (url.indexOf("?") > 0)
				{
					//获取url中的参数
					url = url.substring(url.indexOf('?'));
					String[] paramss = url.split("[&]", -1);
					for (String param : paramss)
					{
						if (DataType.IsNullOrEmpty(param) || param.indexOf("@") == -1)
						{
							continue;
						}
						String[] paramArr = param.split("[=]", -1);
						if (paramArr.length == 2 && paramArr[1].indexOf('@') == 0)
						{
							if (paramArr[1].indexOf("@WebUser.") == 0)
							{
								continue;
							}
							url = url.replace(paramArr[1], en.GetValStrByKey(paramArr[1].substring(1)));
						}
					}

				}
				sb.append("<iframe style='width:100%;height:auto;' ID='" + frame.getMyPK() + "'    src='" + url + "' frameborder=0  leftMargin='0'  topMargin='0' scrolling=auto></iframe></div>");
				sb.append("</td>");
				sb.append("</tr>");
			}


			///#region 审核组件
			if (gf.getCtrlType().equals("FWC") && flowNo != null)
			{
				NodeWorkCheck fwc = new NodeWorkCheck(frmID);

				String sql = "";
				DataTable dtTrack = null;
				boolean bl = false;
				try
				{
					bl = DBAccess.IsExitsTableCol("Port_Emp", "SignType");
				}
				catch (RuntimeException ex)
				{

				}
				if (bl)
				{
					String tTable = "ND" + Integer.parseInt(flowNo) + "Track";
					sql = "SELECT a." + bp.sys.base.Glo.getUserNo() + ", a.SignType FROM Port_Emp a, " + tTable + " b WHERE a." + Glo.UserNo + "=b.EmpFrom AND B.WorkID=" + workid;

					dtTrack = DBAccess.RunSQLReturnTable(sql);
					dtTrack.TableName = "SignType";
					if (dtTrack.Columns.contains("No") == false)
					{
						dtTrack.Columns.Add("No");
					}
					if (dtTrack.Columns.contains("SignType") == false)
					{
						dtTrack.Columns.Add("SignType");
					}
				}

				String html = ""; // "<table style='width:100%;valign:middle;height:auto;' >";

				///#region 生成审核信息.
				sql = "SELECT NDFromT,Msg,RDT,EmpFromT,EmpFrom,NDFrom FROM ND" + Integer.parseInt(flowNo) + "Track WHERE WorkID=" + workid + " AND ActionType=" + ActionType.WorkCheck.getValue() + " ORDER BY RDT ";
				DataTable dt = DBAccess.RunSQLReturnTable(sql);

				//获得当前待办的人员,把当前审批的人员排除在外,不然就有默认同意的意见可以打印出来.
				sql = "SELECT FK_Emp, FK_Node FROM WF_GenerWorkerList WHERE IsPass!=1 AND WorkID=" + workid;
				DataTable dtOfTodo = DBAccess.RunSQLReturnTable(sql);

				for (DataRow dr : dt.Rows)
				{
					///#region 排除正在审批的人员.
					String nodeID = dr.getValue("NDFrom").toString();
					String empFrom = dr.getValue("EmpFrom").toString();
					if (dtOfTodo.Rows.size() != 0)
					{
						boolean isHave = false;
						for (DataRow mydr : dtOfTodo.Rows)
						{
							if (!mydr.getValue("FK_Node").toString().equals(nodeID))
							{
								continue;
							}

							if (!mydr.getValue("FK_Emp").toString().equals(empFrom))
							{
								continue;
							}
							isHave = true;
						}

						if (isHave == true)
						{
							continue;
						}
					}
					///#endregion 排除正在审批的人员.


					html += "<tr>";
					html += " <td valign=middle class='FContext'>" + dr.getValue("NDFromT") + "</td>";

					String msg = dr.getValue("Msg").toString();

					msg += "<br>";
					msg += "<br>";

					String empStrs = "";
					if (dtTrack == null)
					{
						empStrs = dr.getValue("EmpFromT").toString();
					}
					else
					{
						String singType = "0";
						for (DataRow drTrack : dtTrack.Rows)
						{
							if (drTrack.get("No").toString().equals(dr.getValue("EmpFrom").toString()))
							{
								singType = drTrack.get("SignType").toString();
								break;
							}
						}

						if (singType.equals("0") || singType.equals("2"))
						{
							empStrs = dr.getValue("EmpFromT").toString();
						}


						if (singType.equals("1"))
						{
							String src = bp.difference.SystemConfig.getHostURL() + "/DataUser/Siganture/";
							empStrs = "<img src='" + src + dr.getValue("EmpFrom") + ".JPG' title='" + dr.getValue("EmpFromT") + "' style='height:60px;'  alt='图片丢失' /> ";
						}

					}
					msg += "审核人:" + empStrs + " &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日期:" + dr.getValue("RDT").toString();

					html += " <td colspan=3 valign=middle style='font-size:18px'>" + msg + "</td>";
					html += " </tr>";
				}
				///#endregion 生成审核信息.

				sb.append(" " + html);
			}
		}

		sb.append("</table>");
		return sb;
	}


//	private static void GenerHtmlOfDevelop(MapData mapData, String frmID, long workid, Entity en, String path, String indexFile, String flowNo)
//	{
//		GenerHtmlOfDevelop(mapData, frmID, workid, en, path, indexFile, flowNo, null);
//	}
//
//	private static void GenerHtmlOfDevelop(MapData mapData, String frmID, long workid, Entity en, String path, String indexFile)
//	{
//		GenerHtmlOfDevelop(mapData, frmID, workid, en, path, indexFile, null, null);
//	}

//ORIGINAL LINE: private static void GenerHtmlOfDevelop(MapData mapData, string frmID, Int64 workid, Entity en, string path,string indexFile, string flowNo = null, string FK_Node = null)
//	private static void GenerHtmlOfDevelop(MapData mapData, String frmID, long workid, Entity en, String path, String indexFile, String flowNo, String FK_Node) throws Exception {
//		String htmlString = DataType.ReadTextFile(indexFile);
//		HtmlAgilityPack.HtmlDocument doc = new HtmlAgilityPack.HtmlDocument();
//
//		//用于创建新节点
//		HtmlAgilityPack.HtmlNode createnode = doc.DocumentNode.SelectSingleNode("/p");
//
//		//将字符串转换成 HtmlDocument
//		doc.LoadHtml(htmlString);
//		//字段集合.
//		MapAttrs mapAttrs = new MapAttrs(frmID);
//
//		//获取审核组件的信息
//		String sql = "SELECT NDFromT,Msg,RDT,EmpFromT,EmpFrom,NDFrom FROM ND" + Integer.parseInt(flowNo) + "Track WHERE WorkID=" + workid + " AND ActionType=" + ActionType.WorkCheck.getValue() + " ORDER BY RDT ";
//		DataTable dt = DBAccess.RunSQLReturnTable(sql);
//
//		HtmlAgilityPack.HtmlNode node = null;
//		for (MapAttr attr : mapAttrs.ToJavaList())
//		{
//			//处理隐藏字段，如果是不可见并且是启用的就隐藏.
//			if (attr.getUIVisible()== false)
//			{
//				continue;
//			}
//			String text = en.GetValStrByKey(attr.getKeyOfEn());
//			//外键或者外部数据源
//			if ((attr.getLGType() == FieldTypeS.Normal && attr.getMyDataType() == DataType.AppString && attr.getUIContralType() == UIContralType.DDL) || (attr.getLGType() == FieldTypeS.FK && attr.getMyDataType() == DataType.AppString))
//			{
//				if (mapAttrs.contains(attr.getKeyOfEn() + "Text") == true)
//				{
//					text = en.GetValRefTextByKey(attr.getKeyOfEn());
//				}
//				if (DataType.IsNullOrEmpty(text))
//				{
//					if (mapAttrs.contains(attr.getKeyOfEn() + "T") == true)
//					{
//						text = en.GetValStrByKey(attr.getKeyOfEn() + "T");
//					}
//				}
//				node = doc.GetElementbyId("DDL_" + attr.getKeyOfEn());
//				HtmlAgilityPack.HtmlNode parentNode = node.ParentNode;
//				HtmlAgilityPack.HtmlNode newNode = HtmlAgilityPack.HtmlNode.CreateNode("<span>" + text + "</span>");
//				parentNode.AppendChild(newNode);
//				node.Remove();
//				continue;
//			}
//			//枚举、枚举下拉框
//			if (attr.getMyDataType() == DataType.AppInt && attr.getLGType() == FieldTypeS.Enum)
//			{
//				text = en.GetValStrByKey(attr.getKeyOfEn());
//				//如果是下拉框
//				if (attr.getUIContralType() == UIContralType.DDL)
//				{
//					text = en.GetValRefTextByKey(attr.getKeyOfEn() + "Text");
//					node = doc.GetElementbyId("DDL_" + attr.getKeyOfEn());
//					HtmlAgilityPack.HtmlNode parentNode = node.ParentNode;
//					HtmlAgilityPack.HtmlNode newNode = HtmlAgilityPack.HtmlNode.CreateNode("<span>" + text + "</span>");
//					parentNode.AppendChild(newNode);
//					node.Remove();
//					continue;
//				}
//				doc.GetElementbyId("RB_" + attr.getKeyOfEn() + "_" + text).SetAttributeValue("checked", "checked");
//				continue;
//			}
//			//枚举复选框
//			if (attr.getMyDataType() == DataType.AppString && attr.getLGType() == FieldTypeS.Enum)
//			{
//				text = en.GetValStrByKey(attr.getKeyOfEn());
//				String s = en.GetValStrByKey(attr.getKeyOfEn()) + ",";
//				SysEnums enums = new SysEnums(attr.getUIBindKey());
//				for (SysEnum se : enums.ToJavaList())
//				{
//					if (s.indexOf(se.getIntKey() + ",") != -1)
//					{
//						doc.GetElementbyId("CB_" + attr.getKeyOfEn() + "_" + se.getIntKey()).SetAttributeValue("checked", "checked");
//					}
//					doc.GetElementbyId("CB_" + attr.getKeyOfEn() + "_" + se.getIntKey()).SetAttributeValue("disabled", "disabled");
//				}
//
//				continue;
//			}
//
//			if (attr.getMyDataType() == DataType.AppBoolean)
//			{
//				if (DataType.IsNullOrEmpty(text) || text.equals("0"))
//				{
//					doc.GetElementbyId("CB_" + attr.getKeyOfEn()).SetAttributeValue("checked", "");
//				}
//				else
//				{
//					doc.GetElementbyId("CB_" + attr.getKeyOfEn()).SetAttributeValue("checked", "checked");
//				}
//
//				doc.GetElementbyId("CB_" + attr.getKeyOfEn()).SetAttributeValue("disabled", "disabled");
//				continue;
//
//			}
//			if (attr.getMyDataType() == DataType.AppString)
//			{
//				//签批字段
//				if (attr.getUIContralType() == UIContralType.SignCheck)
//				{
//					node = doc.GetElementbyId("TB_" + attr.getKeyOfEn());
//					DataTable mydt = GetWorkcheckInfoByNodeIDs(dt, text);
//					if (mydt.Rows.size() == 0)
//					{
//						node.Remove();
//						continue;
//					}
//					String _html = "<div style='min-height:17px;'>";
//					_html += "<table style='width:100%'><tbody>";
//					for (DataRow dr : mydt.Rows)
//					{
//						_html += "<tr><td style='border: 1px solid #D6DDE6;'>";
//						_html += "<div style='word-wrap: break-word;line-height:20px;padding:5px;padding-left:50px;'><font color='#999'>" + dr.getValue(1).toString() + "</font></div>";
//						_html += "<div style='text-align:right;padding-right:5px'>" + dr.getValue(3).toString() + "(" + dr.getValue(2).toString() + ")</div>";
//						_html += "</td></tr>";
//					}
//					_html += "</tbody></table></div>";
//					HtmlAgilityPack.HtmlNode parentNode = node.ParentNode;
//					HtmlAgilityPack.HtmlNode newNode = HtmlAgilityPack.HtmlNode.CreateNode(_html);
//					parentNode.AppendChild(newNode);
//					node.Remove();
//					continue;
//				}
//				//字段附件
//				if (attr.getUIContralType() == UIContralType.AthShow)
//				{
//					continue;
//				}
//				//签名
//				if (attr.IsSigan == true)
//				{
//					continue;
//				}
//
//			}
//
//			if (attr.IsBigDoc)
//			{
//				//这几种字体生成 pdf都乱码
//				text = text.replace("仿宋,", "宋体,");
//				text = text.replace("仿宋;", "宋体;");
//				text = text.replace("仿宋\"", "宋体\"");
//				text = text.replace("黑体,", "宋体,");
//				text = text.replace("黑体;", "宋体;");
//				text = text.replace("黑体\"", "宋体\"");
//				text = text.replace("楷体,", "宋体,");
//				text = text.replace("楷体;", "宋体;");
//				text = text.replace("楷体\"", "宋体\"");
//				text = text.replace("隶书,", "宋体,");
//				text = text.replace("隶书;", "宋体;");
//				text = text.replace("隶书\"", "宋体\"");
//				doc.GetElementbyId("TB_" + attr.getKeyOfEn()).InnerHtml = text;
//				doc.GetElementbyId("TB_" + attr.getKeyOfEn()).SetAttributeValue("disabled", "disabled");
//				continue;
//			}
//
//
//			//如果是字符串
//			doc.GetElementbyId("TB_" + attr.getKeyOfEn()).SetAttributeValue("value", text);
//			doc.GetElementbyId("TB_" + attr.getKeyOfEn()).SetAttributeValue("disabled", "disabled");
//		}
//		//获取从表
//		MapDtls dtls = new MapDtls(frmID);
//		for (MapDtl dtl : dtls)
//		{
//			if (dtl.IsView == false)
//			{
//				continue;
//			}
//			String html = GetDtlHtmlByID(dtl, workid, mapData.FrmW);
//			node = doc.DocumentNode.SelectSingleNode("//img[@data-key='" + dtl.getNo() + "']");
//			HtmlAgilityPack.HtmlNode parentNode = node.ParentNode;
//			HtmlAgilityPack.HtmlNode newNode = HtmlAgilityPack.HtmlNode.CreateNode(html);
//			parentNode.AppendChild(newNode);
//			node.Remove();
//		}
//		//获取附件
//		FrmAttachments aths = new FrmAttachments(frmID);
//		for (FrmAttachment ath : aths)
//		{
//			if (ath.IsVisable == false)
//			{
//				continue;
//			}
//			node = doc.DocumentNode.SelectSingleNode("//img[@data-key='" + ath.MyPK + "']");
//			String html = GetAthHtmlByID(ath, workid, path);
//			HtmlAgilityPack.HtmlNode parentNode = node.ParentNode;
//			HtmlAgilityPack.HtmlNode newNode = HtmlAgilityPack.HtmlNode.CreateNode(html);
//			parentNode.AppendChild(newNode);
//			node.Remove();
//		}
//		doc.Save(indexFile, Encoding.UTF8);
//		return;
//	}

	private static DataTable GetWorkcheckInfoByNodeIDs(DataTable dt, String nodeId)
	{
		DataTable mydt = dt;
		if (DataType.IsNullOrEmpty(nodeId) == true)
		{
			return mydt;
		}
		String[] nodeIds = nodeId.split("[,]", -1);
		for (int i = 0;i < nodeIds.length; i++)
		{
			if (DataType.IsNullOrEmpty(nodeIds[i]) == true)
			{
				continue;
			}
			//获取到值
			DataRow[] rows = dt.Select("NDFrom=" + nodeIds[i]);
			for (DataRow dr : rows)
			{
				DataRow myrow = mydt.NewRow();
				myrow.ItemArray = dr.ItemArray;
				mydt.Rows.add(myrow);

			}
		}
		return mydt;
	}

	private static String GetDtlHtmlByID(MapDtl dtl, long workid, float width) throws Exception {
		StringBuilder sb = new StringBuilder();
		MapAttrs attrsOfDtls = new MapAttrs(dtl.getNo());
		int columNum = 0;
		for (MapAttr item : attrsOfDtls.ToJavaList())
		{
			if (item.getKeyOfEn().equals("OID"))
			{
				continue;
			}
			if (item.getUIVisible() == false)
			{
				continue;
			}
			columNum++;
		}
		if (columNum == 0)
		{
			return "";
		}
		int columWidth = (int)100 / columNum;

		sb.append("<table style='width:100%' >");
		sb.append("<tr>");

		for (MapAttr item : attrsOfDtls.ToJavaList())
		{
			if (item.getKeyOfEn().equals("OID"))
			{
				continue;
			}
			if (item.getUIVisible() == false)
			{
				continue;
			}
			sb.append("<th class='DtlTh' style='width:" + columWidth + "%'>" + item.getName() + "</th>");
		}
		sb.append("</tr>");
		///#endregion 输出标题.


		///#region 输出数据.
		GEDtls gedtls = new GEDtls(dtl.getNo());
		gedtls.Retrieve(GEDtlAttr.RefPK, workid, "OID");
		for (GEDtl gedtl : gedtls.ToJavaList())
		{
			sb.append("<tr>");

			for (MapAttr attr : attrsOfDtls.ToJavaList())
			{
				//处理隐藏字段，如果是不可见并且是启用的就隐藏.
				if (attr.getKeyOfEn().equals("OID") || attr.getUIVisible()== false)
				{
					continue;
				}

				String text = "";

				switch (attr.getLGType())
				{
					case Normal: // 输出普通类型字段.
						if (attr.getMyDataType() == 1 && attr.getUIContralType().getValue() == DataType.AppString)
						{

							if (attrsOfDtls.contains(attr.getKeyOfEn() + "Text") == true)
							{
								text = gedtl.GetValRefTextByKey(attr.getKeyOfEn());
							}
							if (DataType.IsNullOrEmpty(text))
							{
								if (attrsOfDtls.contains(attr.getKeyOfEn() + "T") == true)
								{
									text = gedtl.GetValStrByKey(attr.getKeyOfEn() + "T");
								}
							}
						}
						else
						{
							//判断是不是图片签名
							if (attr.getIsSigan() == true)
							{
								String SigantureNO = gedtl.GetValStrByKey(attr.getKeyOfEn());
								String src = bp.difference.SystemConfig.getHostURL() + "/DataUser/Siganture/";
								text = "<img src='" + src + SigantureNO + ".JPG' title='" + SigantureNO + "' onerror='this.src=\"" + src + "Siganture.JPG\"' style='height:50px;'  alt='图片丢失' /> ";
							}
							else
							{
								text = gedtl.GetValStrByKey(attr.getKeyOfEn());
							}
							if (attr.getTextModel() == 3)
							{
								text = text.replace("white-space: nowrap;", "");
							}
						}

						break;
					case Enum:
						if (attr.getUIContralType() == UIContralType.CheckBok)
						{
							String s = gedtl.GetValStrByKey(attr.getKeyOfEn()) + ",";
							SysEnums enums = new SysEnums(attr.getUIBindKey());
							for (SysEnum se : enums.ToJavaList())
							{
								if (s.indexOf(se.getIntKey() + ",") != -1)
								{
									text += se.getLab() + " ";
								}
							}

						}
						else
						{
							text = gedtl.GetValRefTextByKey(attr.getKeyOfEn());
						}
						break;
					case FK:
						text = gedtl.GetValRefTextByKey(attr.getKeyOfEn());
						break;
					default:
						break;
				}

				if (attr.getIsBigDoc())
				{
					//这几种字体生成 pdf都乱码
					text = text.replace("仿宋,", "宋体,");
					text = text.replace("仿宋;", "宋体;");
					text = text.replace("仿宋\"", "宋体\"");
					text = text.replace("黑体,", "宋体,");
					text = text.replace("黑体;", "宋体;");
					text = text.replace("黑体\"", "宋体\"");
					text = text.replace("楷体,", "宋体,");
					text = text.replace("楷体;", "宋体;");
					text = text.replace("楷体\"", "宋体\"");
					text = text.replace("隶书,", "宋体,");
					text = text.replace("隶书;", "宋体;");
					text = text.replace("隶书\"", "宋体\"");
				}

				if (attr.getMyDataType() == DataType.AppBoolean)
				{
					if (DataType.IsNullOrEmpty(text) || text.equals("0"))
					{
						text = "否";
					}
					else
					{
						text = "是";
					}
				}
				if (attr.getIsNum())
				{
					sb.append("<td class='DtlTd' style='text-align:right;' >" + text + "</td>");
				}
				else
				{
					sb.append("<td class='DtlTd' >" + text + "</td>");
				}
			}

			sb.append("</tr>");
		}
		///#endregion 输出数据.


		sb.append("</table>");


		sb.append("</span>");
		return sb.toString();
	}

	private static String GetAthHtmlByID(FrmAttachment ath, long workid, String path) throws Exception {
		StringBuilder sb = new StringBuilder();

		if (ath.getUploadType() == AttachmentUploadType.Multi)
		{


			//判断是否有这个目录.
			if ((new File(path + "/pdf/")).isDirectory() == false)
			{
				(new File(path + "/pdf/")).mkdirs();
			}

			//文件加密
			boolean fileEncrypt = bp.difference.SystemConfig.getIsEnableAthEncrypt();
			FrmAttachmentDBs athDBs = bp.wf.Glo.GenerFrmAttachmentDBs(ath, String.valueOf(workid), ath.getMyPK(), workid);
			sb.append("<table id = 'ShowTable' class='table' style='width:100%'>");
			sb.append("<thead><tr style = 'border:0px;'>");
			sb.append("<th style='width:50px; border: 1px solid #ddd;padding:8px;background-color:white' nowrap='true'>序</th>");
			sb.append("<th style = 'min -width:200px; border: 1px solid #ddd;padding:8px;background-color:white' nowrap='true'>文件名</th>");
			sb.append("<th style = 'width:50px; border: 1px solid #ddd;padding:8px;background-color:white' nowrap='true'>大小KB</th>");
			sb.append("<th style = 'width:120px; border: 1px solid #ddd;padding:8px;background-color:white' nowrap='true'>上传时间</th>");
			sb.append("<th style = 'width:80px; border: 1px solid #ddd;padding:8px;background-color:white' nowrap='true'>上传人</th>");
			sb.append("</thead>");
			sb.append("<tbody>");
			int idx = 0;
			for (FrmAttachmentDB item : athDBs.ToJavaList())
			{
				idx++;
				sb.append("<tr>");
				sb.append("<td class='Idx'>" + idx + "</td>");
				//获取文件是否加密
				boolean isEncrypt = item.GetParaBoolen("IsEncrypt");
				if (ath.getAthSaveWay() == AthSaveWay.FTPServer)
				{
					try
					{
						String toFile = path + "/pdf/" + item.getFileName();
						if ((new File(toFile)).isFile() == false)
						{
							//获取文件是否加密
							String file = item.GenerTempFile(ath.getAthSaveWay());
							String fileTempDecryPath = file;
							if (fileEncrypt == true && isEncrypt == true)
							{
								fileTempDecryPath = file + ".tmp";
								AesEncodeUtil.decryptFile(file, fileTempDecryPath);

							}

							Files.copy(Paths.get(fileTempDecryPath), Paths.get(toFile), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
						}

						sb.append("<td  title='" + item.getFileName() + "'>" + item.getFileName() + "</td>");
					}
					catch (RuntimeException ex)
					{
						sb.append("<td>" + item.getFileName() + "(<font color=red>文件未从ftp下载成功{" + ex.getMessage() + "}</font>)</td>");
					}
				}

				if (ath.getAthSaveWay() == AthSaveWay.IISServer)
				{
					try
					{
						String toFile = path + "/pdf/" + item.getFileName();
						if ((new File(toFile)).isFile() == false)
						{
							//把文件copy到,
							String fileTempDecryPath = item.getFileFullName();
							if (fileEncrypt == true && isEncrypt == true)
							{
								fileTempDecryPath = item.getFileFullName() + ".tmp";
								AesEncodeUtil.decryptFile(item.getFileFullName(), fileTempDecryPath);

							}

							//把文件copy到,
							Files.copy(Paths.get(fileTempDecryPath), Paths.get(path + "/pdf/" + item.getFileName()), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
						}
						sb.append("<td>" + item.getFileName() + "</td>");
					}
					catch (RuntimeException ex)
					{
						sb.append("<td>" + item.getFileName() + "(<font color=red>文件未从ftp下载成功{" + ex.getMessage() + "}</font>)</td>");
					}
				}
				sb.append("<td>" + item.getFileSize() + "KB</td>");
				sb.append("<td>" + item.getRDT() + "</td>");
				sb.append("<td>" + item.getRecName() + "</td>");
				sb.append("</tr>");


			}
			sb.append("</tbody>");

			sb.append("</table>");
		}
		return sb.toString();
	}
	/**
	 单表单，多表单打印PDF

	 param node 节点属性
	 param workid 流程实例WorkID
	 param flowNo 流程编号
	 param pdfName 生成PDF的名称
	 param filePath 生成PDF的路径
	 @return
	 @exception Exception
	*/
	public static String MakeCCFormToPDF(Node node, long workid, String flowNo, String pdfName, String filePath) throws Exception {
		//根据节点信息获取表单方案
		MapData md = new MapData("ND" + node.getNodeID());
		String resultMsg = "";
		GenerWorkFlow gwf = null;

		//获取主干流程信息
		if (flowNo != null)
		{
			gwf = new GenerWorkFlow(workid);
		}

		//存放信息地址
		String hostURL = bp.difference.SystemConfig.GetValByKey("HostURL", "");
		String path = bp.difference.SystemConfig.getPathOfDataUser() + "InstancePacketOfData/" + "ND" + node.getNodeID() + "/" + workid;
		String frmID = node.getNodeFrmID();

		//处理正确的文件名.
		if (DataType.IsNullOrEmpty(pdfName) == true)
		{
			if (DataType.IsNullOrEmpty(flowNo) == false)
			{
				pdfName = DBAccess.RunSQLReturnStringIsNull("SELECT Title FROM WF_GenerWorkFlow WHERE WorkID=" + workid, String.valueOf(workid));
			}
			else
			{
				pdfName = String.valueOf(workid);
			}
		}

		pdfName = DataType.PraseStringToFileName(pdfName);

		Hashtable ht = new Hashtable();

			///#region 单表单打印
		if (node.getHisFormType().getValue() == NodeFormType.FoolForm.getValue() || node.getHisFormType().getValue() == NodeFormType.RefOneFrmTree.getValue() || node.getHisFormType().getValue() == NodeFormType.FoolTruck.getValue() || node.getHisFormType() == NodeFormType.Develop)
		{
			resultMsg = setPDFPath("ND" + node.getNodeID(), workid, flowNo, gwf);
			if (resultMsg.indexOf("err@") != -1)
			{
				return resultMsg;
			}

			String billUrl = bp.difference.SystemConfig.getPathOfDataUser() + "InstancePacketOfData/" + "ND" + node.getNodeID() + "/" + workid + "/index.htm";

			//resultMsg = MakeHtmlDocument(frmID, workid, flowNo, path, billUrl, "ND" + node.getNodeID());

			if (resultMsg.indexOf("err@") != -1)
			{
				return resultMsg;
			}

			ht.put("htm", bp.difference.SystemConfig.GetValByKey("HostURLOfBS", "../../DataUser") + "/InstancePacketOfData/" + "ND" + node.getNodeID() + "/" + workid + "/index.htm");
			//生成pdf文件
			String pdfPath = filePath;
			if (DataType.IsNullOrEmpty(pdfPath) == true)
			{
				pdfPath = path + "/pdf";
			}

			if ((new File(pdfPath)).isDirectory() == false)
			{
				(new File(pdfPath)).mkdirs();
			}

			String pdfFile = pdfPath + "/" + pdfName + ".pdf";
			String pdfFileExe = bp.difference.SystemConfig.getPathOfDataUser() + "ThirdpartySoftware/wkhtmltox/wkhtmltopdf.exe";
			try
			{
				Html2Pdf(pdfFileExe, billUrl, pdfFile);
				if (DataType.IsNullOrEmpty(filePath) == true)
				{
					ht.put("pdf", bp.difference.SystemConfig.GetValByKey("HostURLOfBS", "../../DataUser/") + "InstancePacketOfData/" + "ND" + node.getNodeID() + "/" + workid + "/pdf/" + pdfName + ".pdf");
				}
				else
				{
					ht.put("pdf", pdfPath + "/" + DataType.PraseStringToUrlFileName(pdfName) + ".pdf");
				}
			}
			catch (RuntimeException ex)
			{
				throw new RuntimeException("err@html转PDF错误:PDF的路径" + pdfPath + "可能抛的异常" + ex.getMessage());
			}

			//生成压缩文件
			String zipFile = path + "/../" + pdfName + ".zip";

			File finfo = new File(zipFile);
			ZipFilePath =finfo.getName();

			File zipFileFile = new File(zipFile);
			try {
				while (zipFileFile.exists() == true) {
					zipFileFile.delete();
				}
				// 执行压缩.
				ZipCompress fz = new ZipCompress(zipFile, pdfPath);
				fz.zip();
				ht.put("zip", SystemConfig.GetValByKey("HostURL","") + "/DataUser/InstancePacketOfData/" + frmID + "/" + workid +"/"+ DataType.PraseStringToUrlFileName(pdfName) + ".zip");
			} catch (Exception ex) {
				ht.put("zip","err@执行压缩出现错误:" + ex.getMessage() + ",路径tempPath:" + pdfPath + ",zipFile=" + finfo.getName());
			}
			if (zipFileFile.exists() == false)
				ht.put("zip","err@压缩文件未生成成功,请在点击一次.");



			return bp.tools.Json.ToJsonEntitiesNoNameMode(ht);
		}

			///#endregion 单表单打印

			///#region 多表单合并PDF打印
		if (node.getHisFormType().getValue() == NodeFormType.SheetTree.getValue())
		{
			String pdfPath = filePath;
			//生成pdf文件
			//生成pdf文件
			if (DataType.IsNullOrEmpty(pdfPath) == true)
			{
				pdfPath = path + "/pdf";
			}
			String pdfTempPath = path + "/pdfTemp";

			DataRow dr = null;
			resultMsg = setPDFPath("ND" + node.getNodeID(), workid, flowNo, gwf);
			if (resultMsg.indexOf("err@") != -1)
			{
				return resultMsg;
			}

			//获取绑定的表单
			FrmNodes nds = new FrmNodes(node.getFK_Flow(), node.getNodeID());
			for (FrmNode item : nds.ToJavaList())
			{
				//判断当前绑定的表单是否启用
				if (item.getFrmEnableRoleInt() == FrmEnableRole.Disable.getValue())
				{
					continue;
				}

				//判断 who is pk
				if (flowNo != null && item.getWhoIsPK() == WhoIsPK.PWorkID) //如果是父子流程
				{
					workid = gwf.getPWorkID();
				}
				//获取表单的信息执行打印
				String billUrl = bp.difference.SystemConfig.getPathOfDataUser() + "InstancePacketOfData/" + "ND" + node.getNodeID() + "/" + workid + "/" + item.getFKFrm() + "index.htm";
				//resultMsg = MakeHtmlDocument(item.getFKFrm(), workid, flowNo, path, billUrl, "ND" + node.getNodeID());

				if (resultMsg.indexOf("err@") != -1)
				{
					return resultMsg;
				}

				ht.put("htm_" + item.getFKFrm(), bp.difference.SystemConfig.GetValByKey("HostURLOfBS", "../../DataUser/") + "/InstancePacketOfData/" + "ND" + node.getNodeID() + "/" + workid + "/" + item.getFKFrm() + "index.htm");

				///#region 把所有的文件做成一个zip文件.
				if ((new File(pdfTempPath)).isDirectory() == false)
				{
					(new File(pdfTempPath)).mkdirs();
				}

				String pdfFormFile = pdfTempPath + "/" + item.getFKFrm() + ".pdf";
				String pdfFileExe = bp.difference.SystemConfig.getPathOfDataUser() + "ThirdpartySoftware/wkhtmltox/wkhtmltopdf.exe";
				try
				{
					Html2Pdf(pdfFileExe, resultMsg, pdfFormFile);

				}
				catch (RuntimeException ex)
				{
					/*有可能是因为文件路径的错误， 用补偿的方法在执行一次, 如果仍然失败，按照异常处理. */
					Html2Pdf(pdfFileExe, resultMsg, pdfFormFile);
				}

			}

			//pdf合并
			String pdfFile = pdfPath + "/" + pdfName + ".pdf";
			//开始合并处理
			if (new File(pdfPath).exists() == false)
				new File(pdfPath).mkdirs();

			PDFMergerUtility merger=new PDFMergerUtility();
			String[] fileInFolder= BaseFileUtils.getFiles(pdfTempPath);
			for(int i=0;i<fileInFolder.length;i++){
				merger.addSource(fileInFolder[i]);
			}
			merger.setDestinationFileName(pdfFile);
			merger.mergeDocuments();



			//生成压缩文件
			String zipFile = path + "/../" + pdfName + ".zip";

			File finfo = new File(zipFile);
			ZipFilePath = finfo.getPath(); //文件路径.

			// 执行压缩.
			File zipFileFile = new File(zipFile);
			try {
				while (zipFileFile.exists() == true) {
					zipFileFile.delete();
				}
			ZipCompress fz = new ZipCompress(zipFile, pdfPath);
			fz.zip();
			ht.put("zip", SystemConfig.GetValByKey("HostURL","") + "/DataUser/InstancePacketOfData/" + frmID + "/" + workid +"/"+ DataType.PraseStringToUrlFileName(pdfName) + ".zip");
		} catch (Exception ex) {
			ht.put("zip","err@执行压缩出现错误:" + ex.getMessage() + ",路径tempPath:" + pdfPath + ",zipFile=" + finfo.getName());
		}

		if (zipFileFile.exists() == false)
			ht.put("zip","err@压缩文件未生成成功,请在点击一次.");



		return bp.tools.Json.ToJsonEntitiesNoNameMode(ht);
		}

			///#endregion 多表单合并PDF打印

		return "warning@不存在需要打印的表单";

	}
	/**
	 单据打印

	 param frmId 表单ID
	 param workid 数据ID
	 param filePath PDF路径
	 param pdfName PDF名称
	 @return
	*/
	public static String MakeBillToPDF(String frmId, long workid, String filePath, String pdfName) throws Exception {
		String resultMsg = "";

		//  获取单据的属性信息
		bp.ccbill.FrmBill bill = new bp.ccbill.FrmBill(frmId);

		//存放信息地址
		String path = bp.difference.SystemConfig.getPathOfDataUser() + "InstancePacketOfData/" + bill.getNo() + "/" + workid;

		if (DataType.IsNullOrEmpty(pdfName) == true)
		{
			pdfName = String.valueOf(workid);
		}

		pdfName = DataType.PraseStringToFileName(pdfName);

		Hashtable ht = new Hashtable();
		String pdfPath = filePath;
		//生成pdf文件
		if (DataType.IsNullOrEmpty(pdfPath) == true)
		{
			pdfPath = path + "/pdf";
		}
		DataRow dr = null;
		resultMsg = setPDFPath(frmId, workid, null, null);
		if (resultMsg.indexOf("err@") != -1)
		{
			return resultMsg;
		}

		//获取表单的信息执行打印
		String billUrl = bp.difference.SystemConfig.getPathOfDataUser() + "InstancePacketOfData/" + bill.getNo() + "/" + workid + "/" + "index.htm";
		//resultMsg = MakeHtmlDocument(bill.getNo(), workid, null, path, billUrl, frmId);

		if (resultMsg.indexOf("err@") != -1)
		{
			return resultMsg;
		}

		ht.put("htm", bp.difference.SystemConfig.GetValByKey("HostURLOfBS", "../../DataUser/") + "InstancePacketOfData/" + frmId + "/" + workid + "/" + "index.htm");

		///#region 把所有的文件做成一个zip文件.
		if ((new File(pdfPath)).isDirectory() == false)
		{
			(new File(pdfPath)).mkdirs();
		}

		String pdfFormFile = pdfPath + "/" + pdfName + ".pdf"; //生成的路径.
		String pdfFileExe = bp.difference.SystemConfig.getPathOfDataUser() + "ThirdpartySoftware/wkhtmltox/wkhtmltopdf.exe";
		try
		{
			Html2Pdf(pdfFileExe, resultMsg, pdfFormFile);
			if (DataType.IsNullOrEmpty(filePath) == true)
			{
				ht.put("pdf", bp.difference.SystemConfig.GetValByKey("HostURLOfBS", "../../DataUser/") + "InstancePacketOfData/" + frmId + "/" + workid + "/pdf/" + pdfName + ".pdf");
			}
			else
			{
				ht.put("pdf", pdfPath + "/" + pdfName + ".pdf");
			}
		}
		catch (RuntimeException ex)
		{

			pdfFormFile = pdfPath + "/" + pdfName + ".pdf";

			Html2Pdf(pdfFileExe, resultMsg, pdfFormFile);
			ht.put("pdf", bp.difference.SystemConfig.GetValByKey("HostURLOfBS", "") + "/InstancePacketOfData/" + frmId + "/" + workid + "/pdf/" + bill.getName() + ".pdf");
		}

		//生成压缩文件
		String zipFile = path + "/../" + pdfName + ".zip";

		File finfo = new File(zipFile);
		ZipFilePath = finfo.getPath(); //文件路径.

		File zipFileFile = new File(zipFile);

		try {
			while (zipFileFile.exists() == true) {
				zipFileFile.delete();
			}
			// 执行压缩.
			ZipCompress fz = new ZipCompress(zipFile, pdfPath);
			fz.zip();


			ht.put("zip", SystemConfig.getHostURLOfBS() + "/DataUser/InstancePacketOfData/" + frmId + "/"
					+ DataType.PraseStringToUrlFileName(pdfName) + ".zip");
		} catch (RuntimeException ex) {
			ht.put("zip", "err@生成zip文件遇到权限问题:" + ex.getMessage() + " @Path:" + pdfPath);
		}
		return bp.tools.Json.ToJsonEntitiesNoNameMode(ht);
	}

	public static String MakeFormToPDF(String frmId, String frmName, Node node, long workid, String flowNo, String fileNameFormat, boolean urlIsHostUrl, String basePath) throws Exception {

		String resultMsg = "";
		GenerWorkFlow gwf = null;

		//获取主干流程信息
		if (flowNo != null)
		{
			gwf = new GenerWorkFlow(workid);
		}

		//存放信息地址
		String hostURL = bp.difference.SystemConfig.GetValByKey("HostURL", "");
		String path = bp.difference.SystemConfig.getPathOfDataUser() + "InstancePacketOfData/" + "ND" + node.getNodeID() + "/" + workid;

		//处理正确的文件名.
		if (fileNameFormat == null)
		{
			if (flowNo != null)
			{
				fileNameFormat = DBAccess.RunSQLReturnStringIsNull("SELECT Title FROM WF_GenerWorkFlow WHERE WorkID=" + workid, "" + String.valueOf(workid));
			}
			else
			{
				fileNameFormat = String.valueOf(workid);
			}
		}

		if (DataType.IsNullOrEmpty(fileNameFormat) == true)
		{
			fileNameFormat = String.valueOf(workid);
		}

		fileNameFormat = DataType.PraseStringToFileName(fileNameFormat);

		Hashtable ht = new Hashtable();

		//生成pdf文件
		String pdfPath = path + "/pdf";


		DataRow dr = null;
		resultMsg = setPDFPath("ND" + node.getNodeID(), workid, flowNo, gwf);
		if (resultMsg.indexOf("err@") != -1)
		{
			return resultMsg;
		}

		//获取绑定的表单
		FrmNode frmNode = new FrmNode();
		frmNode.Retrieve(FrmNodeAttr.FK_Frm, frmId);

		//判断当前绑定的表单是否启用
		if (frmNode.getFrmEnableRoleInt() == FrmEnableRole.Disable.getValue())
		{
			return "warning@" + frmName + "没有被启用";
		}

		//判断 who is pk
		if (flowNo != null && frmNode.getWhoIsPK() == WhoIsPK.PWorkID) //如果是父子流程
		{
			workid = gwf.getPWorkID();
		}

		//获取表单的信息执行打印
		String billUrl = bp.difference.SystemConfig.getPathOfDataUser() + "InstancePacketOfData/" + "ND" + node.getNodeID() + "/" + workid + "/" + frmNode.getFKFrm() + "index.htm";
		//resultMsg = MakeHtmlDocument(frmNode.getFKFrm(), workid, flowNo, path, billUrl, "ND" + node.getNodeID());

		if (resultMsg.indexOf("err@") != -1)
		{
			return resultMsg;
		}

		// ht.Add("htm", bp.difference.SystemConfig.GetValByKey("HostURLOfBS", "../../DataUser/") + "/InstancePacketOfData/" + "ND" + node.NodeID + "/" + workid + "/" + frmNode.FK_Frm + "index.htm");

		///#region 把所有的文件做成一个zip文件.
		if ((new File(pdfPath)).isDirectory() == false)
		{
			(new File(pdfPath)).mkdirs();
		}

		fileNameFormat = fileNameFormat.substring(0, fileNameFormat.length() - 1);
		String pdfFormFile = pdfPath + "/" + frmNode.getFKFrm() + ".pdf";
		String pdfFileExe = bp.difference.SystemConfig.getPathOfDataUser() + "ThirdpartySoftware/wkhtmltox/wkhtmltopdf.exe";
		try
		{
			Html2Pdf(pdfFileExe, resultMsg, pdfFormFile);
			if (urlIsHostUrl == false)
			{
				ht.put("pdf", bp.difference.SystemConfig.GetValByKey("HostURLOfBS", "../../DataUser/") + "InstancePacketOfData/" + "ND" + node.getNodeID() + "/" + workid + "/pdf/" + frmNode.getFKFrm() + ".pdf");
			}
			else
			{
				ht.put("pdf", bp.difference.SystemConfig.GetValByKey("HostURL", "") + "/DataUser/InstancePacketOfData/" + "ND" + node.getNodeID() + "/" + workid + "/pdf/" + frmNode.getFKFrm() + ".pdf");
			}


		}
		catch (RuntimeException ex)
		{
			/*有可能是因为文件路径的错误， 用补偿的方法在执行一次, 如果仍然失败，按照异常处理. */
			fileNameFormat = DBAccess.GenerGUID(0, null, null);
			pdfFormFile = pdfPath + "/" + fileNameFormat + ".pdf";

			Html2Pdf(pdfFileExe, resultMsg, pdfFormFile);
			ht.put("pdf", bp.difference.SystemConfig.GetValByKey("HostURLOfBS", "") + "/InstancePacketOfData/" + "ND" + node.getNodeID() + "/" + workid + "/pdf/" + frmNode.getFKFrm() + ".pdf");
		}

		return bp.tools.Json.ToJsonEntitiesNoNameMode(ht);


	}

	/**
	 读取合并的pdf文件名称

	 param Directorypath 目录
	 param outpath 导出的路径
	*/
//	public static void MergePDF(String Directorypath, String outpath)
//	{
//		ArrayList<String> filelist2 = new ArrayList<String>();
//		File di2 = new File(Directorypath);
//		File[] ff2 = di2.GetFiles("*.pdf");
//		BubbleSort(ff2);
//		for (File temp : ff2)
//		{
//			filelist2.add(Directorypath + "/" + temp.getName());
//		}
//
//		PdfReader reader;
//		//iTextSharp.text.Rectangle rec = new iTextSharp.text.Rectangle(1403, 991);
//		Document document = new Document();
////C# TO JAVA CONVERTER TODO TASK: C# to Java Converter cannot determine whether this System.IO.FileStream is input or output:
//		PdfWriter writer = PdfWriter.GetInstance(document, new FileStream(outpath, FileMode.Create));
//		document.Open();
//		PdfContentByte cb = writer.DirectContent;
//		PdfImportedPage newPage;
//		for (int i = 0; i < filelist2.size(); i++)
//		{
//			reader = new PdfReader(filelist2.get(i));
//			int iPageNum = reader.NumberOfPages;
//			for (int j = 1; j <= iPageNum; j++)
//			{
//				document.NewPage();
//				newPage = writer.GetImportedPage(reader, j);
//				cb.AddTemplate(newPage, 0, 0);
//			}
//		}
//		document.Close();
//	}
	/**
	 冒泡排序

	 param arr 文件名数组
	*/
//	public static void BubbleSort(File[] arr)
//	{
//		for (int i = 0; i < arr.length; i++)
//		{
//			for (int j = i; j < arr.length; j++)
//			{
//				if (arr[i].LastWriteTime.compareTo(arr[j].LastWriteTime) > 0) //按创建时间（升序）
//				{
//					File temp = arr[i];
//					arr[i] = arr[j];
//					arr[j] = temp;
//				}
//			}
//		}
//	}


	//前期文件的准备
	private static String setPDFPath(String frmID, long workid, String flowNo, GenerWorkFlow gwf) throws Exception {
		//准备目录文件.
		String path = bp.difference.SystemConfig.getPathOfDataUser() + "InstancePacketOfData/" + frmID + "/";
		try
		{

			path = bp.difference.SystemConfig.getPathOfDataUser() + "InstancePacketOfData/" + frmID + "/";
			if ((new File(path)).isDirectory() == false)
			{
				(new File(path)).mkdirs();
			}

			path = bp.difference.SystemConfig.getPathOfDataUser() + "InstancePacketOfData/" + frmID + "/" + workid;
			if ((new File(path)).isDirectory() == false)
			{
				(new File(path)).mkdirs();
			}

			//把模版文件copy过去.
			String templateFilePath = bp.difference.SystemConfig.getPathOfDataUser() + "InstancePacketOfData/Template/";
			File dir = new File(templateFilePath);
			File[] finfos = dir.listFiles();
			if (finfos.length == 0)
			{
				return "err@不存在模板文件";
			}
			for (File fl : finfos)
			{
				if (fl.getName().contains("ShuiYin"))
				{
					continue;
				}

				if (fl.getName().contains("htm"))
				{
					continue;
				}
				if ((new File(path + "/" + fl.getPath())).isFile() == true)
				{
					(new File(path + "/" + fl.getPath())).delete();
				}
				Files.copy(Paths.get(fl.getPath()), Paths.get(path + "/" + fl.getName()), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
			}

		}
		catch (RuntimeException ex)
		{
			return "err@读写文件出现权限问题，请联系管理员解决。" + ex.getMessage();
		}

		String hostURL = bp.difference.SystemConfig.GetValByKey("HostURL", "");
		String billUrl = hostURL + "/DataUser/InstancePacketOfData/" + frmID + "/" + workid + "/index.htm";

		// begin生成二维码.
		String pathQR = path + "/QR.png"; // key.Replace("OID.Img@AppPath", bp.difference.SystemConfig.getPathOfWebApp());
		if (bp.difference.SystemConfig.GetValByKeyBoolen("IsShowQrCode", false) == true)
		{
			/*说明是图片文件.*/
			String qrUrl = hostURL + "/WF/WorkOpt/PrintDocQRGuide.htm?FrmID=" + frmID + "&WorkID=" + workid + "&FlowNo=" + flowNo;
			if (flowNo != null)
			{
				gwf = new GenerWorkFlow(workid);
				qrUrl = hostURL + "/WF/WorkOpt/PrintDocQRGuide.htm?AP=" + frmID + "$" + workid + "_" + flowNo + "_" + gwf.getFK_Node() + "_" + gwf.getStarter() + "_" + gwf.getFK_Dept();
			}

			//二维码的生成
			QrCodeUtil.createQrCode(qrUrl,path,"QR.png","");

		}
		//end生成二维码.
		return "";
	}

//	private static String DownLoadFielToMemoryStream(String url)
//	{
//		Object tempVar = System.Net.HttpWebRequest.Create(url);
//		var wreq = tempVar instanceof System.Net.HttpWebRequest ? (System.Net.HttpWebRequest)tempVar : null;
//		Object tempVar2 = wreq.GetResponse();
//		System.Net.HttpWebResponse response = tempVar2 instanceof System.Net.HttpWebResponse ? (System.Net.HttpWebResponse)tempVar2 : null;
////C# TO JAVA CONVERTER TODO TASK: C# to Java Converter cannot determine whether this System.IO.MemoryStream is input or output:
//		MemoryStream ms = null;
//		try (var stream = response.GetResponseStream())
//		{
//
////ORIGINAL LINE: Byte[] buffer = new Byte[response.ContentLength];
//			byte[] buffer = new byte[response.ContentLength];
//			int offset = 0, actuallyRead = 0;
//			do
//			{
//				actuallyRead = stream.Read(buffer, offset, buffer.length - offset);
//				offset += actuallyRead;
//			} while (actuallyRead > 0);
////C# TO JAVA CONVERTER TODO TASK: C# to Java Converter cannot determine whether this System.IO.MemoryStream is input or output:
//			ms = new MemoryStream(buffer);
//		}
//		response.Close();
//		return Convert.ToBase64String(ms.ToArray());
//
//	}

	/**
	 zip文件路径.
	*/
	public static String ZipFilePath = "";

	public static String CCFlowAppPath = "/";

//	public static String GetHtml(String url) throws IOException {
//		String html = "";
//		HttpWebRequest rt = null;
//		HttpWebResponse rs = null;
//		InputStream stream = null;
//		InputStreamReader sr = null;
//
//		try
//		{
//			rt = (HttpWebRequest)WebRequest.Create(url);
//			rs = (HttpWebResponse)rt.GetResponse();
//			stream = rs.GetResponseStream();
//			sr = new InputStreamReader(stream, System.Text.Encoding.Default);
//			html = sr.ReadToEnd();
//
//		}
//		catch (RuntimeException ee)
//		{
//			new RuntimeException("发生异常:" + ee.getMessage());
//		}
//		finally
//		{
//			sr.close();
//			stream.close();
//			rs.Close();
//		}
//		return html;
//	}



//	public static String MakeHtmlDocument(String frmID, long workid, String flowNo, String path, String indexFile, String nodeID)throws Exception
//	{
//		try
//		{
//			GenerWorkFlow gwf = null;
//			if (flowNo != null)
//			{
//				gwf = new GenerWorkFlow(workid);
//			}
//
//			///#region 定义变量做准备.
//			//生成表单信息.
//			MapData mapData = new MapData(frmID);
//
//			if (mapData.getHisFrmType() == FrmType.Url)
//			{
//				String url = mapData.getUrlExt();
//
//				//替换系统参数
//				url = url.replace("@WebUser.No", WebUser.getNo());
//				url = url.replace("@WebUser.Name;", WebUser.getName());
//				url = url.replace("@WebUser.FK_DeptName;", WebUser.getFK_DeptName());
//				url = url.replace("@WebUser.FK_Dept;", WebUser.getFK_Dept());
//
//				//替换参数
//				if (url.indexOf("?") > 0)
//				{
//					//获取url中的参数
//					String urlParam = url.substring(url.indexOf('?'));
//					String[] paramss = url.split("[&]", -1);
//					for (String param : paramss)
//					{
//						if (DataType.IsNullOrEmpty(param) || param.indexOf("@") == -1)
//						{
//							continue;
//						}
//						String[] paramArr = param.split("[=]", -1);
//						if (paramArr.length == 2 && paramArr[1].indexOf('@') == 0)
//						{
//							if (paramArr[1].indexOf("@WebUser.") == 0)
//							{
//								continue;
//							}
//							url = url.replace(paramArr[1], gwf.GetValStrByKey(paramArr[1].substring(1)));
//						}
//					}
//
//				}
//				url = url.replace("@basePath", bp.difference.SystemConfig.getHostURLOfBS());
//				if (url.contains("http") == false)
//				{
//					url = bp.difference.SystemConfig.getHostURLOfBS() + url;
//				}
//
//				String str = "<iframe style='width:100%;height:auto;' ID='" + mapData.getNo() + "'    src='" + url + "' frameborder=0  leftMargin='0'  topMargin='0' scrolling=auto></iframe></div>";
//				String docs1 = DataType.ReadTextFile(bp.difference.SystemConfig.getPathOfDataUser() + "InstancePacketOfData/Template/indexUrl.htm");
//				StringBuilder sb1 = new StringBuilder();
//				WebClient MyWebClient = new WebClient();
//				MyWebClient.Credentials = CredentialCache.DefaultCredentials; //获取或设置用于向Internet资源的请求进行身份验证的网络凭据
//
//
//				byte[] pageData = MyWebClient.DownloadData(url); //从指定网站下载数据
//				String pageHtml = Encoding.UTF8.GetString(pageData); //如果获取网站页面采用的是UTF-8，则使用这句
//
//				docs1 = docs1.replace("@Width", mapData.getFrmW() + "px");
//				docs1 = docs1.replace("@Height", mapData.getFrmH() + "px");
//				if (gwf != null)
//				{
//					docs1 = docs1.replace("@Title", gwf.getTitle());
//				}
//				DataType.WriteFile(indexFile, pageHtml);
//				return indexFile;
//			}
//			else if (mapData.getHisFrmType() == FrmType.Develop)
//			{
//				GEEntity enn = new GEEntity(frmID, workid);
//				String ddocs = DataType.ReadTextFile(bp.difference.SystemConfig.getPathOfDataUser() + "InstancePacketOfData/Template/indexDevelop.htm");
//				String htmlString = DBAccess.GetBigTextFromDB("Sys_MapData", "No", mapData.getNo(), "HtmlTemplateFile");
//
//
//				htmlString = htmlString.replace("../../DataUser", bp.difference.SystemConfig.HostURLOfBS + "/DataUser");
//				htmlString = htmlString.replace("../DataUser", bp.difference.SystemConfig.HostURLOfBS + "/DataUser");
//				ddocs = ddocs.replace("@Docs", htmlString);
//
//				ddocs = ddocs.replace("@Height", mapData.FrmH.toString() + "px");
//				ddocs = ddocs.replace("@Title", mapData.getName());
//
//				DataType.WriteFile(indexFile, ddocs);
//			   GenerHtmlOfDevelop(mapData, mapData.getNo(), workid, enn, path, indexFile, flowNo, nodeID);
//
//				return indexFile;
//			}
//			 GEEntity en = new GEEntity(frmID, workid);
//
//
//
//				///#region 生成水文.
//
//			String rdt = "";
//			if (en.getEnMap().getAttrs().contains("RDT"))
//			{
//				rdt = en.GetValStringByKey("RDT");
//				if (rdt.length() > 10)
//				{
//					rdt = rdt.substring(0, 10);
//				}
//			}
//			//先判断节点中水印的设置
//			//判断是否打印水印
//			boolean isPrintShuiYin = bp.difference.SystemConfig.GetValByKeyBoolen("IsPrintBackgroundWord", false);
//			Node nd = null;
//			if (gwf != null)
//			{
//				nd = new Node(gwf.getFK_Node());
//			}
//			if (isPrintShuiYin == true)
//			{
//				String words = "";
//				if (nd.getNodeID() != 0)
//				{
//					words = nd.getShuiYinModle();
//				}
//
//				if (DataType.IsNullOrEmpty(words) == true)
//				{
//					words = Glo.getPrintBackgroundWord();
//				}
//				words = words.replace("@RDT", rdt);
//
//				if (words.contains("@") == true)
//				{
//					words = Glo.DealExp(words, en);
//				}
//
//				String templateFilePathMy = bp.difference.SystemConfig.getPathOfDataUser() + "InstancePacketOfData/Template/";
//				WaterImageManage wim = new WaterImageManage();
//				wim.DrawWords(templateFilePathMy + "ShuiYin.png", words, Float.parseFloat("0.15"), ImagePosition.Center, path + "/ShuiYin.png");
//			}
//
//
//				///#endregion
//
//			//生成 表单的 html.
//			StringBuilder sb = new StringBuilder();
//
//
//				///#region 替换模版文件..
//			//首先判断是否有约定的文件.
//			String docs = "";
//			String tempFile = bp.difference.SystemConfig.getPathOfDataUser() + "InstancePacketOfData/Template/" + mapData.getNo() + ".htm";
//			if ((new File(tempFile)).isFile() == false)
//			{
//				if (gwf != null)
//				{
//
//					if (nd.getHisFormType() == NodeFormType.Develop)
//					{
//						mapData.HisFrmType = FrmType.Develop;
//					}
//					else if (nd.getHisFormType() == NodeFormType.FoolForm || nd.getHisFormType() == NodeFormType.FoolTruck)
//					{
//						mapData.HisFrmType = FrmType.FoolForm;
//					}
//					else if (nd.getHisFormType() == NodeFormType.SelfForm)
//					{
//						mapData.HisFrmType = FrmType.Url;
//					}
//				}
//
//				if (mapData.HisFrmType == FrmType.FoolForm)
//				{
//					docs = DataType.ReadTextFile(bp.difference.SystemConfig.getPathOfDataUser() + "InstancePacketOfData/Template/indexFool.htm");
//					sb = bp.wf.MakeForm2Html.GenerHtmlOfFool(mapData, frmID, workid, en, path, flowNo, nodeID, nd.getHisFormType());
//					docs = docs.replace("@Width", mapData.FrmW.toString() + "px");
//				}
//
//			}
//
//
//
//
//			docs = docs.replace("@Docs", sb.toString());
//
//			docs = docs.replace("@Height", mapData.FrmH.toString() + "px");
//
//			String dateFormat = Date.now().toString("yyyy年MM月dd日 HH时mm分ss秒");
//			docs = docs.replace("@PrintDT", dateFormat);
//
//			if (flowNo != null)
//			{
//				gwf = new GenerWorkFlow(workid);
//				gwf.setWorkID(workid);
//				gwf.RetrieveFromDBSources();
//
//				docs = docs.replace("@Title", gwf.getTitle());
//
//				if (gwf.getWFState() == WFState.Runing)
//				{
//					if (bp.difference.SystemConfig.getCustomerNo().equals("TianYe") && gwf.getNodeName().contains("反馈") == true)
//					{
//						nd = new Node(gwf.getFK_Node());
//						if (nd.isEndNode() == true)
//						{
//							//让流程自动结束.
//							bp.wf.Dev2Interface.Flow_DoFlowOver(workid, "打印并自动结束", 0);
//						}
//					}
//				}
//
//				//替换模版尾部的打印说明信息.
//				String pathInfo = bp.difference.SystemConfig.getPathOfDataUser() + "InstancePacketOfData/Template/EndInfo/" + flowNo + ".txt";
//				if ((new File(pathInfo)).isFile() == false)
//				{
//					pathInfo = bp.difference.SystemConfig.getPathOfDataUser() + "InstancePacketOfData/Template/EndInfo/Default.txt";
//				}
//
//				docs = docs.replace("@EndInfo", DataType.ReadTextFile(pathInfo));
//			}
//
//			//indexFile =  bp.difference.SystemConfig.getPathOfDataUser() + "/InstancePacketOfData/" + frmID + "/" + workid + "/index.htm";
//			DataType.WriteFile(indexFile, docs);
//
//			return indexFile;
//		}
//		catch (RuntimeException ex)
//		{
//			return "err@报表生成错误:" + ex.getMessage();
//		}
//	}
public static String MakeHtmlDocument(String frmID, long workid, String flowNo, String fileNameFormat,
									  boolean urlIsHostUrl, String path, String indexFile, String nodeID, String basePath) throws Exception {
	return MakeHtmlDocument(frmID, workid, flowNo, fileNameFormat, urlIsHostUrl, path, indexFile, nodeID, basePath,
			null);
}


	public static String MakeHtmlDocument(String frmID, long workid, String flowNo, String fileNameFormat,
										  boolean urlIsHostUrl, String path, String indexFile, String nodeID, String basePath, String htmlString) throws Exception {
		try {
			GenerWorkFlow gwf = null;
			if (flowNo != null) {
				gwf = new GenerWorkFlow(workid);
			}

			/// 定义变量做准备.
			// 生成表单信息.
			MapData mapData = new MapData(frmID);

			if (mapData.getHisFrmType() == FrmType.Url) {
				String url = mapData.getUrlExt();
				// 替换系统参数
				url = url.replace("@WebUser.No", WebUser.getNo());
				url = url.replace("@WebUser.Name;", WebUser.getName());
				url = url.replace("@WebUser.FK_DeptName;", WebUser.getFK_DeptName());
				url = url.replace("@WebUser.FK_Dept;", WebUser.getFK_Dept());

				// 替换参数
				if (url.indexOf("?") > 0) {
					// 获取url中的参数
					String urlParam = url.substring(url.indexOf('?'));
					String[] paramss = url.split("[&]", -1);
					for (String param : paramss) {
						if (DataType.IsNullOrEmpty(param) || param.indexOf("@") == -1) {
							continue;
						}
						String[] paramArr = param.split("[=]", -1);
						if (paramArr.length == 2 && paramArr[1].indexOf('@') == 0) {
							if (paramArr[1].indexOf("@WebUser.") == 0) {
								continue;
							}
							url = url.replace(paramArr[1], gwf.GetValStrByKey(paramArr[1].substring(1)));
						}
					}

				}
				url = url.replace("@basePath", basePath);
				if (url.contains("http") == false) {
					url = basePath + url;
				}

				String sb="<iframe style='width:100%;height:auto;' ID='" + mapData.getNo() + "'    src='" + url + "' frameborder=0  leftMargin='0'  topMargin='0' scrolling=auto></iframe></div>";
				String  docs = DataType.ReadTextFile(SystemConfig.getPathOfDataUser() + "InstancePacketOfData/Template/indexUrl.htm");
				docs = docs.replace("@Docs", sb.toString());
				docs = docs.replace("@Width", String.valueOf(mapData.getFrmW())+"px");
				docs = docs.replace("@Height", String.valueOf(mapData.getFrmH())+"px");
				if(gwf!=null)
					docs = docs.replace("@Title", gwf.getTitle());
				DataType.WriteFile(indexFile, docs);
				return indexFile;
			} else if (mapData.getHisFrmType() == FrmType.Develop) {
				String ddocs = bp.da.DataType.ReadTextFile(
						SystemConfig.getPathOfDataUser() + "InstancePacketOfData/Template/indexDevelop.htm");

				// 获取附件

				// 获取从表
				MapDtls dtls = new MapDtls(frmID);
				for (MapDtl dtl : dtls.ToJavaList()) {
					if (dtl.isView() == false) {
						continue;
					}
					String html = GetDtlHtmlByID(dtl, workid, mapData.getFrmW());
					htmlString = htmlString.replace("@Dtl_Fd" + dtl.getNo(), html);
				}
				FrmAttachments aths = new FrmAttachments(frmID);
				for (FrmAttachment ath : aths.ToJavaList()) {
					if (ath.getIsVisable() == false) {
						continue;
					}
					String html = GetAthHtmlByID(ath, workid, path);
					htmlString = htmlString.replace("@Ath_" + ath.getMyPK(), html);
				}

				htmlString = htmlString.replace("../../DataUser", SystemConfig.getHostURLOfBS() + "/DataUser");
				htmlString = htmlString.replace("../DataUser", SystemConfig.getHostURLOfBS() + "/DataUser");
				ddocs = ddocs.replace("@Docs", htmlString);

				ddocs = ddocs.replace("@Height", mapData.getFrmH()+ "px");
				ddocs = ddocs.replace("@Title", mapData.getName());

				bp.da.DataType.WriteFile(indexFile, ddocs);
				return indexFile;
			}
			GEEntity en = new GEEntity(frmID, workid);

			/// 生成水文.

			String rdt = "";
			if (en.getEnMap().getAttrs().contains("RDT")) {
				rdt = en.GetValStringByKey("RDT");
				if (rdt.length() > 10) {
					rdt = rdt.substring(0, 10);
				}
			}
			// 先判断节点中水印的设置
			String words = "";
			Node nd = null;
			if (gwf != null) {
				nd = new Node(gwf.getFK_Node());
				if (nd.getNodeID() != 0) {
					words = nd.getShuiYinModle();
				}
			}
			if (DataType.IsNullOrEmpty(words) == true) {
				words = Glo.getPrintBackgroundWord();
			}
			words = words.replace("@RDT", rdt);

			if (words.contains("@") == true) {
				words = Glo.DealExp(words, en);
			}

			String templateFilePathMy = SystemConfig.getPathOfDataUser() + "InstancePacketOfData/Template/";
			//paintWaterMarkPhoto(templateFilePathMy + "ShuiYin.png",words,path + "/ShuiYin.png");


			///

			// 生成 表单的 html.
			StringBuilder sb = new StringBuilder();

			/// 替换模版文件..
			// 首先判断是否有约定的文件.
			String docs = "";
			String tempFile = SystemConfig.getPathOfDataUser() + "InstancePacketOfData/Template/" + mapData.getNo()
					+ ".htm";
			if ((new File(tempFile)).isFile() == false) {
				if (gwf != null) {

					if (nd.getHisFormType() == NodeFormType.FreeForm) {
						mapData.setHisFrmType(FrmType.FreeFrm);
					} else if (nd.getHisFormType() == NodeFormType.FoolForm
							|| nd.getHisFormType() == NodeFormType.FoolTruck) {
						mapData.setHisFrmType(FrmType.FoolForm);
					} else if (nd.getHisFormType() == NodeFormType.SelfForm) {
						mapData.setHisFrmType(FrmType.Url);
					}
				}

				if (mapData.getHisFrmType() == FrmType.FoolForm) {
					docs = bp.da.DataType.ReadTextFile(
							SystemConfig.getPathOfDataUser() + "InstancePacketOfData/Template/indexFool.htm");
//					sb = bp.wf.MakeForm2Html.GenerHtmlOfFool(mapData, frmID, workid, en, path, flowNo, nodeID, basePath,
//							nd.getHisFormType());
					docs = docs.replace("@Width", mapData.getFrmW() + "px");
				} else if (mapData.getHisFrmType() == FrmType.FreeFrm) {
					docs = bp.da.DataType.ReadTextFile(
							SystemConfig.getPathOfDataUser() + "InstancePacketOfData/Template/indexFree.htm");
//					sb = bp.wf.MakeForm2Html.GenerHtmlOfFree(mapData, frmID, workid, en, path, flowNo, nodeID,
//							basePath);
					docs = docs.replace("@Width", (mapData.getFrmW() * 1.5) + "px");
				}
			}

			docs = docs.replace("@Docs", sb.toString());

			docs = docs.replace("@Height", mapData.getFrmH() + "px");

			String dateFormat = DataType.getCurrentDateByFormart("yyyy年MM月dd日 HH时mm分ss秒");
			docs = docs.replace("@PrintDT", dateFormat);

			if (flowNo != null) {
				gwf = new GenerWorkFlow(workid);
				gwf.setWorkID(workid);
				gwf.RetrieveFromDBSources();

				docs = docs.replace("@Title", gwf.getTitle());

				if (gwf.getWFState() == WFState.Runing) {
					if (SystemConfig.getCustomerNo().equals("TianYe") && gwf.getNodeName().contains("反馈") == true) {
						nd = new Node(gwf.getFK_Node());
						if (nd.isEndNode() == true) {
							// 让流程自动结束.
							bp.wf.Dev2Interface.Flow_DoFlowOver(workid, "打印并自动结束", 0);
						}
					}
				}

				// 替换模版尾部的打印说明信息.
				String pathInfo = SystemConfig.getPathOfDataUser() + "InstancePacketOfData/Template/EndInfo/"
						+ flowNo + ".txt";
				if ((new File(pathInfo)).isFile() == false) {
					pathInfo = SystemConfig.getPathOfDataUser()
							+ "InstancePacketOfData/Template/EndInfo/Default.txt";
				}

				docs = docs.replace("@EndInfo", DataType.ReadTextFile(pathInfo));
			}

			// indexFile = SystemConfig.getPathOfDataUser() +
			// "/InstancePacketOfData/" + frmID + "/" + workid +
			// "/index.htm";
			bp.da.DataType.WriteFile(indexFile, docs);

			return indexFile;
		} catch (RuntimeException ex) {
			return "err@报表生成错误:" + ex.getMessage();
		}
	}
	public static void Html2Pdf(String pdfFileExe, String htmFile, String pdf)
	{
		bp.da.Log.DebugWriteInfo("@开始生成PDF" + pdfFileExe + "@pdf=" + pdf + "@htmFile=" + htmFile);
		StringBuilder cmd = new StringBuilder();
		if (System.getProperty("os.name").indexOf("Windows") == -1) {
			// 非windows 系统
			pdfFileExe = "/home/ubuntu/wkhtmltox/bin/wkhtmltopdf";
		}
		cmd.append(pdfFileExe);
		cmd.append(" ");
		cmd.append(" --header-line");// 页眉下面的线
		// cmd.append(" --header-center 这里是页眉这里是页眉这里是页眉这里是页眉 ");//页眉中间内容
		cmd.append(" --margin-top 3cm ");// 设置页面上边距 (default 10mm)
		// cmd.append(" --header-html
		// file:///"+WebUtil.getServletContext().getRealPath("")+FileUtil.convertSystemFilePath("/style/pdf/head.html"));//
		// (添加一个HTML页眉,后面是网址)
		cmd.append(" --header-spacing 5 ");// (设置页眉和内容的距离,默认0)
		// cmd.append(" --footer-center (设置在中心位置的页脚内容)");//设置在中心位置的页脚内容
		// cmd.append(" --footer-html
		// file:///"+WebUtil.getServletContext().getRealPath("")+FileUtil.convertSystemFilePath("/style/pdf/foter.html"));//
		// (添加一个HTML页脚,后面是网址)
		cmd.append(" --footer-line");// * 显示一条线在页脚内容上)
		cmd.append(" --footer-spacing 5 ");// (设置页脚和内容的距离)

		cmd.append(htmFile);
		cmd.append(" ");
		cmd.append(pdf);
		boolean result = true;
		try {
			Process proc = Runtime.getRuntime().exec(cmd.toString());
			HtmlToPdfInterceptor error = new HtmlToPdfInterceptor(proc.getErrorStream());
			HtmlToPdfInterceptor output = new HtmlToPdfInterceptor(proc.getInputStream());
			error.start();
			output.start();
			proc.waitFor();
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
		//return result;
	}
	/**
	 签名

	 param userNo
	 @return
	*/
	private static String SignPic(String userNo) throws Exception {

		if (DataType.IsNullOrEmpty(userNo))
		{
			return "";
		}
		//如果文件存在
		String path = bp.difference.SystemConfig.getPathOfDataUser() + "Siganture/" + userNo + ".jpg";

		if ((new File(path)).isFile() == false)
		{
			path = bp.difference.SystemConfig.getPathOfDataUser() + "Siganture/" + userNo + ".JPG";
			if ((new File(path)).isFile() == true)
			{
				return "<img src='" + path + "' style='border:0px;width:100px;height:30px;'/>";
			}
			else
			{
				Emp emp = new Emp(userNo);
				return emp.getName();
			}
		}
		else
		{
			return "<img src='" + path + "' style='border:0px;width:100px;height:30px;'/>";
		}

	}


}

///#endregion