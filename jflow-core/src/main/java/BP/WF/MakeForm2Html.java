package BP.WF;

import BP.Sys.*;
import BP.DA.*;
import BP.En.*;
import BP.*;
import BP.Web.*;
import BP.Port.*;
import BP.WF.Rpt.*;
import BP.WF.Data.*;
import BP.WF.Template.*;
import ICSharpCode.SharpZipLib.Zip.*;
import iTextSharp.text.*;
import iTextSharp.text.pdf.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.time.*;

public class MakeForm2Html
{
	/** 
	 生成
	 
	 @param mapData
	 @param frmID
	 @param workid
	 @param en
	 @param path
	 @param flowNo
	 @return 
	*/

	public static StringBuilder GenerHtmlOfFree(MapData mapData, String frmID, long workid, Entity en, String path, String flowNo, String FK_Node)
	{
		return GenerHtmlOfFree(mapData, frmID, workid, en, path, flowNo, FK_Node, null);
	}

	public static StringBuilder GenerHtmlOfFree(MapData mapData, String frmID, long workid, Entity en, String path, String flowNo)
	{
		return GenerHtmlOfFree(mapData, frmID, workid, en, path, flowNo, null, null);
	}

	public static StringBuilder GenerHtmlOfFree(MapData mapData, String frmID, long workid, Entity en, String path)
	{
		return GenerHtmlOfFree(mapData, frmID, workid, en, path, null, null, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static StringBuilder GenerHtmlOfFree(MapData mapData, string frmID, Int64 workid, Entity en, string path, string flowNo = null, string FK_Node = null, string basePath = null)
	public static StringBuilder GenerHtmlOfFree(MapData mapData, String frmID, long workid, Entity en, String path, String flowNo, String FK_Node, String basePath)
	{
		StringBuilder sb = new StringBuilder();

		//字段集合.
		MapAttrs mapAttrs = new MapAttrs(frmID);

		Attrs attrs = en.EnMap.Attrs;

		String appPath = "";
		float wtX = MapData.GenerSpanWeiYi(mapData, 1200);
		//float wtX = 0;
		float x = 0;

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 输出Ele
		FrmEles eles = mapData.FrmEles;
		if (eles.size() >= 1)
		{
			for (FrmEle ele : eles)
			{
				float y = ele.Y;
				x = ele.X + wtX;
				sb.append("<DIV id=" + ele.MyPK + " style='position:absolute;left:" + x + "px;top:" + y + "px;text-align:left;vertical-align:top' >");

				sb.append("\t\n</DIV>");
			}

		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 输出Ele

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 输出竖线与标签 & 超连接 Img.
		FrmLabs labs = mapData.FrmLabs;
		for (FrmLab lab : labs)
		{
			System.Drawing.Color col = System.Drawing.ColorTranslator.FromHtml(lab.FontColor);
			x = lab.X + wtX;
			sb.append("\t\n<DIV id=u2 style='position:absolute;left:" + x + "px;top:" + lab.Y + "px;text-align:left;' >");
			sb.append("\t\n<span style='color:" + lab.FontColorHtml + ";font-family: " + lab.FontName + ";font-size: " + lab.FontSize + "px;' >" + lab.TextHtml + "</span>");
			sb.append("\t\n</DIV>");
		}

		FrmLines lines = mapData.FrmLines;
		for (FrmLine line : lines)
		{
			if (line.X1 == line.X2)
			{
				/* 一道竖线 */
				float h = line.Y1 - line.Y2;
				h = Math.abs(h);
				if (line.Y1 < line.Y2)
				{
					x = line.X1 + wtX;
					sb.append("\t\n<img id='" + line.MyPK + "'  style=\"padding:0px;position:absolute; left:" + x + "px; top:" + line.Y1 + "px; width:" + line.BorderWidth + "px; height:" + h + "px;background-color:" + line.BorderColorHtml + "\" />");
				}
				else
				{
					x = line.X2 + wtX;
					sb.append("\t\n<img id='" + line.MyPK + "'  style=\"padding:0px;position:absolute; left:" + x + "px; top:" + line.Y2 + "px; width:" + line.BorderWidth + "px; height:" + h + "px;background-color:" + line.BorderColorHtml + "\" />");
				}
			}
			else
			{
				/* 一道横线 */
				float w = line.X2 - line.X1;

				if (line.X1 < line.X2)
				{
					x = line.X1 + wtX;
					sb.append("\t\n<img id='" + line.MyPK + "'  style=\"padding:0px;position:absolute; left:" + x + "px; top:" + line.Y1 + "px; width:" + w + "px; height:" + line.BorderWidth + "px;background-color:" + line.BorderColorHtml + "\" />");
				}
				else
				{
					x = line.X2 + wtX;
					sb.append("\t\n<img id='" + line.MyPK + "'  style=\"padding:0px;position:absolute; left:" + x + "px; top:" + line.Y2 + "px; width:" + w + "px; height:" + line.BorderWidth + "px;background-color:" + line.BorderColorHtml + "\" />");
				}
			}
		}

		FrmLinks links = mapData.FrmLinks;
		for (FrmLink link : links)
		{
			String url = link.URL;
			if (url.contains("@"))
			{
				for (MapAttr attr : mapAttrs)
				{
					if (url.contains("@") == false)
					{
						break;
					}
					url = url.replace("@" + attr.KeyOfEn, en.GetValStrByKey(attr.KeyOfEn));
				}
			}
			x = link.X + wtX;
			sb.append("\t\n<DIV id=u2 style='position:absolute;left:" + x + "px;top:" + link.Y + "px;text-align:left;' >");
			sb.append("\t\n<span style='color:" + link.FontColorHtml + ";font-family: " + link.FontName + ";font-size: " + link.FontSize + "px;' > <a href=\"" + url + "\" target='" + link.Target + "'> " + link.Text + "</a></span>");
			sb.append("\t\n</DIV>");
		}

		FrmImgs imgs = mapData.FrmImgs;
		for (FrmImg img : imgs)
		{
			float y = img.Y;
			String imgSrc = "";

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 图片类型
			if (img.HisImgAppType == ImgAppType.Img)
			{
				//数据来源为本地.
				if (img.ImgSrcType == 0)
				{
					if (img.ImgPath.Contains(";") == false)
					{
						imgSrc = img.ImgPath;
					}
				}

				//数据来源为指定路径.
				if (img.ImgSrcType == 1)
				{
					//图片路径不为默认值
					imgSrc = img.ImgURL;
					if (imgSrc.contains("@"))
					{
						/*如果有变量*/
						imgSrc = BP.WF.Glo.DealExp(imgSrc, en, "");
					}
				}

				x = img.X + wtX;
				// 由于火狐 不支持onerror 所以 判断图片是否存在
				imgSrc = "icon.png";

				sb.append("\t\n<div id=" + img.MyPK + " style='position:absolute;left:" + x + "px;top:" + y + "px;text-align:left;vertical-align:top' >");
				if (DataType.IsNullOrEmpty(img.LinkURL) == false)
				{
					sb.append("\t\n<a href='" + img.LinkURL + "' target=" + img.LinkTarget + " ><img src='" + imgSrc + "'  onerror=\"this.src='/DataUser/ICON/CCFlow/LogBig.png'\"  style='padding: 0px;margin: 0px;border-width: 0px;width:" + img.W + "px;height:" + img.H + "px;' /></a>");
				}
				else
				{
					sb.append("\t\n<img src='" + imgSrc + "'  onerror=\"this.src='/DataUser/ICON/CCFlow/LogBig.png'\"  style='padding: 0px;margin: 0px;border-width: 0px;width:" + img.W + "px;height:" + img.H + "px;' />");
				}
				sb.append("\t\n</div>");
				continue;
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 图片类型

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 二维码
			if (img.HisImgAppType == ImgAppType.QRCode)
			{
				x = img.X + wtX;
				String pk = en.PKVal.toString();
				String myPK = frmID + "_" + img.MyPK + "_" + pk;
				FrmEleDB frmEleDB = new FrmEleDB();
				frmEleDB.MyPK = myPK;
				if (frmEleDB.RetrieveFromDBSources() == 0)
				{
					//生成二维码
				}

				sb.append("\t\n<DIV id=" + img.MyPK + " style='position:absolute;left:" + x + "px;top:" + y + "px;text-align:left;vertical-align:top' >");
				sb.append("\t\n<img src='" + frmEleDB.Tag2 + "' style='padding: 0px;margin: 0px;border-width: 0px;width:" + img.W + "px;height:" + img.H + "px;' />");
				sb.append("\t\n</DIV>");

				continue;
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 电子签章
			//图片类型
			if (img.HisImgAppType == ImgAppType.Seal)
			{
				//获取登录人岗位
				String stationNo = "";
				//签章对应部门
				String fk_dept = WebUser.FK_Dept;
				//部门来源类别
				String sealType = "0";
				//签章对应岗位
				String fk_station = img.Tag0;
				//表单字段
				String sealField = "";
				String sql = "";

				//重新加载 可能有缓存
				img.RetrieveFromDBSources();
				//0.不可以修改，从数据表中取，1可以修改，使用组合获取并保存数据
				if ((img.IsEdit == 1))
				{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
						///#region 加载签章
					//如果设置了部门与岗位的集合进行拆分
					if (!DataType.IsNullOrEmpty(img.Tag0) && img.Tag0.Contains("^") && img.Tag0.split("[^]", -1).Length == 4)
					{
						fk_dept = img.Tag0.split("[^]", -1)[0];
						fk_station = img.Tag0.split("[^]", -1)[1];
						sealType = img.Tag0.split("[^]", -1)[2];
						sealField = img.Tag0.split("[^]", -1)[3];
						//如果部门没有设定，就获取部门来源
						if (fk_dept.equals("all"))
						{
							//默认当前登陆人
							fk_dept = WebUser.FK_Dept;
							//发起人
							if (sealType.equals("1"))
							{
								sql = "SELECT FK_Dept FROM WF_GenerWorkFlow WHERE WorkID=" + en.GetValStrByKey("OID");
								fk_dept = BP.DA.DBAccess.RunSQLReturnString(sql);
							}
							//表单字段
							if (sealType.equals("2") && !DataType.IsNullOrEmpty(sealField))
							{
								//判断字段是否存在
								for (MapAttr attr : mapAttrs)
								{
									if (sealField.equals(attr.KeyOfEn))
									{
										fk_dept = en.GetValStrByKey(sealField);
										break;
									}
								}
							}
						}
					}
					//判断本部门下是否有此人
					//sql = "SELECT fk_station from port_deptEmpStation where fk_dept='" + fk_dept + "' and fk_emp='" + WebUser.No + "'";
					sql = String.format(" select FK_Station from Port_DeptStation where FK_Dept ='%1$s' and FK_Station in (select FK_Station from " + BP.WF.Glo.getEmpStation() + " where FK_Emp='%2$s')", fk_dept, WebUser.No);
					DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
					for (DataRow dr : dt.Rows)
					{
						if (fk_station.contains(dr.get(0).toString() + ","))
						{
							stationNo = dr.get(0).toString();
							break;
						}
					}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
						///#endregion 加载签章

					imgSrc = CCFlowAppPath + "DataUser/Seal/" + fk_dept + "_" + stationNo + ".png";
					//设置主键
					String myPK = DataType.IsNullOrEmpty(img.EnPK) ? "seal" : img.EnPK;
					myPK = myPK + "_" + en.GetValStrByKey("OID") + "_" + img.MyPK;

					FrmEleDB imgDb = new FrmEleDB();
					QueryObject queryInfo = new QueryObject(imgDb);
					queryInfo.AddWhere(FrmEleAttr.MyPK, myPK);
					queryInfo.DoQuery();
					//判断是否存在
					if (imgDb == null || DataType.IsNullOrEmpty(imgDb.FK_MapData))
					{
						imgDb.FK_MapData = DataType.IsNullOrEmpty(img.EnPK) ? "seal" : img.EnPK;
						imgDb.EleID = en.GetValStrByKey("OID");
						imgDb.RefPKVal = img.MyPK;
						imgDb.Tag1 = imgSrc;
						imgDb.Insert();
					}

					//添加控件
					x = img.X + wtX;
					sb.append("\t\n<DIV id=" + img.MyPK + " style='position:absolute;left:" + x + "px;top:" + y + "px;text-align:left;vertical-align:top' >");
					sb.append("\t\n<img src='" + imgSrc + "' onerror=\"javascript:this.src='" + appPath + "DataUser/Seal/Def.png'\" style=\"padding: 0px;margin: 0px;border-width: 0px;width:" + img.W + "px;height:" + img.H + "px;\" />");
					sb.append("\t\n</DIV>");
				}
				else
				{
					FrmEleDB realDB = null;
					FrmEleDB imgDb = new FrmEleDB();
					QueryObject objQuery = new QueryObject(imgDb);
					objQuery.AddWhere(FrmEleAttr.FK_MapData, img.EnPK);
					objQuery.addAnd();
					objQuery.AddWhere(FrmEleAttr.EleID, en.GetValStrByKey("OID"));

					if (objQuery.DoQuery() == 0)
					{
						FrmEleDBs imgdbs = new FrmEleDBs();
						QueryObject objQuerys = new QueryObject(imgdbs);
						objQuerys.AddWhere(FrmEleAttr.EleID, en.GetValStrByKey("OID"));
						if (objQuerys.DoQuery() > 0)
						{
							for (FrmEleDB single : imgdbs)
							{
								if (single.FK_MapData.substring(6, single.FK_MapData.Length).equals(img.EnPK.substring(6, img.EnPK.Length)))
								{
									single.FK_MapData = img.EnPK;
									single.MyPK = img.EnPK + "_" + en.GetValStrByKey("OID") + "_" + img.EnPK;
									single.RefPKVal = img.EnPK;
									//  single.DirectInsert();
									//  realDB = single; cut by zhoupeng .没有看明白.
									break;
								}
							}
						}
						else
						{
							realDB = imgDb;
						}
					}
					else
					{
						realDB = imgDb;
					}

					if (realDB != null)
					{
						imgSrc = realDB.Tag1;
						//如果没有查到记录，控件不显示。说明没有走盖章的一步
						x = img.X + wtX;
						sb.append("\t\n<DIV id=" + img.MyPK + " style='position:absolute;left:" + x + "px;top:" + y + "px;text-align:left;vertical-align:top' >");
						sb.append("\t\n<img src='" + imgSrc + "' onerror='javascript:this.src='" + appPath + "DataUser/ICON/" + BP.Sys.SystemConfig.CustomerNo + "/LogBiger.png';' style='padding: 0px;margin: 0px;border-width: 0px;width:" + img.W + "px;height:" + img.H + "px;' />");
						sb.append("\t\n</DIV>");
					}
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion
		}


		FrmBtns btns = mapData.FrmBtns;
		for (FrmBtn btn : btns)
		{
			x = btn.X + wtX;
			sb.append("\t\n<DIV id=u2 style='position:absolute;left:" + x + "px;top:" + btn.Y + "px;text-align:left;' >");
			sb.append("\t\n<span >");

			String doDoc = BP.WF.Glo.DealExp(btn.EventContext, en, null);
			doDoc = doDoc.replace("~", "'");
			switch (btn.HisBtnEventType)
			{
				case BtnEventType.Disable:
					sb.append("<input type=button class=Btn value='" + btn.Text.Replace("&nbsp;", " ") + "' disabled='disabled'/>");
					break;
				case BtnEventType.RunExe:
				case BtnEventType.RunJS:
					sb.append("<input type=button class=Btn value=\"" + btn.Text.Replace("&nbsp;", " ") + "\" enable=true onclick=\"" + doDoc + "\" />");
					break;
				default:
					sb.append("<input type=button value='" + btn.Text + "' />");
					break;
			}
			sb.append("\t\n</span>");
			sb.append("\t\n</DIV>");
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 输出竖线与标签

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 输出数据控件.
		int fSize = 0;
		for (MapAttr attr : mapAttrs)
		{
			//处理隐藏字段，如果是不可见并且是启用的就隐藏.
			if (attr.UIVisible == false && attr.UIIsEnable)
			{
				sb.append("<input type=text value='" + en.GetValStrByKey(attr.KeyOfEn) + "' style='display:none;' />");
				continue;
			}

			if (attr.UIVisible == false)
			{
				continue;
			}

			x = attr.X + wtX;
			if (attr.LGType == FieldTypeS.Enum || attr.LGType == FieldTypeS.FK)
			{
				sb.append("<DIV id='F" + attr.KeyOfEn + "' style='position:absolute; left:" + x + "px; top:" + attr.Y + "px;  height:16px;text-align: left;word-break: keep-all;' >");
			}
			else
			{
				sb.append("<DIV id='F" + attr.KeyOfEn + "' style='position:absolute; left:" + x + "px; top:" + attr.Y + "px; width:" + attr.UIWidth + "px; height:16px;text-align: left;word-break: keep-all;' >");
			}

			sb.append("<span>");

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region add contrals.
			if (attr.MaxLen >= 3999 && attr.TBModel == TBModel.RichText)
			{
				sb.append(en.GetValStrByKey(attr.KeyOfEn));

				sb.append("</span>");
				sb.append("</DIV>");
				continue;
			}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 通过逻辑类型，输出相关的控件.
			String text = "";
			switch (attr.LGType)
			{
				case FieldTypeS.Normal: // 输出普通类型字段.
					if (attr.IsSigan == true)
					{
						text = en.GetValStrByKey(attr.KeyOfEn);
						text = SignPic(text);
						break;
					}
					if (attr.MyDataType == 1 && (int)attr.UIContralType == DataType.AppString)
					{
						if (attrs.Contains(attr.KeyOfEn + "Text") == true)
						{
							text = en.GetValRefTextByKey(attr.KeyOfEn);
						}
						if (DataType.IsNullOrEmpty(text))
						{
							if (attrs.Contains(attr.KeyOfEn + "T") == true)
							{
								text = en.GetValStrByKey(attr.KeyOfEn + "T");
							}
						}
					}
					else
					{
						text = en.GetValStrByKey(attr.KeyOfEn);
					}
					break;
				case FieldTypeS.Enum:
				case FieldTypeS.FK:
					text = en.GetValRefTextByKey(attr.KeyOfEn);
					break;
				default:
					break;
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 通过逻辑类型，输出相关的控件.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion add contrals.


			if (attr.IsBigDoc)
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

			if (attr.MyDataType == DataType.AppBoolean)
			{
				if (DataType.IsNullOrEmpty(text) || text.equals("0"))
				{
					text = "[&#10005]" + attr.Name;
				}
				else
				{
					text = "[&#10004]" + attr.Name;
				}
			}

			sb.append(text);

			sb.append("</span>");
			sb.append("</DIV>");
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region  输出 rb.
		BP.Sys.FrmRBs myrbs = mapData.FrmRBs;
		MapAttr attrRB = new MapAttr();
		for (BP.Sys.FrmRB rb : myrbs)
		{
			x = rb.X + wtX;
			sb.append("<DIV id='F" + rb.MyPK + "' style='position:absolute; left:" + x + "px; top:" + rb.Y + "px; height:16px;text-align: left;word-break: keep-all;' >");
			sb.append("<span style='word-break: keep-all;font-size:12px;'>");

			if (rb.IntKey == en.GetValIntByKey(rb.KeyOfEn))
			{
				sb.append("<b>" + rb.Lab + "</b>");
			}
			else
			{
				sb.append(rb.Lab);
			}

			sb.append("</span>");
			sb.append("</DIV>");
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion  输出 rb.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 输出数据控件.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 输出明细.
		int dtlsCount = 0;
		MapDtls dtls = new MapDtls(frmID);
		for (MapDtl dtl : dtls)
		{
			if (dtl.IsView == false)
			{
				continue;
			}

			dtlsCount++;
			x = dtl.X + wtX;
			float y = dtl.Y;

			sb.append("<DIV id='Fd" + dtl.No + "' style='position:absolute; left:" + x + "px; top:" + y + "px; width:" + dtl.W + "px; height:" + dtl.H + "px;text-align: left;' >");
			sb.append("<span>");

			MapAttrs attrsOfDtls = new MapAttrs(dtl.No);

			sb.append("<table style='wdith:100%' >");
			sb.append("<tr>");
			for (MapAttr item : attrsOfDtls)
			{
				if (item.KeyOfEn.equals("OID"))
				{
					continue;
				}
				if (item.UIVisible == false)
				{
					continue;
				}

				sb.append("<th class='DtlTh'>" + item.Name + "</th>");
			}
			sb.append("</tr>");
			///#endregion 输出标题.


			///#region 输出数据.
			GEDtls gedtls = new GEDtls(dtl.No);
			gedtls.Retrieve(GEDtlAttr.RefPK, workid);
			for (GEDtl gedtl : gedtls)
			{
				sb.append("<tr>");

				for (MapAttr item : attrsOfDtls)
				{
					if (item.KeyOfEn.equals("OID") || item.UIVisible == false)
					{
						continue;
					}

					if (item.UIContralType == UIContralType.DDL)
					{
						sb.append("<td class='DtlTd'>" + gedtl.GetValRefTextByKey(item.KeyOfEn) + "</td>");
						continue;
					}

					if (item.IsNum)
					{
						sb.append("<td class='DtlTd' style='text-align:right' >" + gedtl.GetValStrByKey(item.KeyOfEn) + "</td>");
						continue;
					}

					sb.append("<td class='DtlTd'>" + gedtl.GetValStrByKey(item.KeyOfEn) + "</td>");
				}
				sb.append("</tr>");
			}
			///#endregion 输出数据.


			sb.append("</table>");

			//string src = "";
			//if (dtl.HisEditModel == EditModel.TableModel)
			//{
			//    src = SystemConfig.CCFlowWebPath + "WF/CCForm/Dtl.htm?EnsName=" + dtl.No + "&RefPKVal=" + en.PKVal + "&IsReadonly=1";
			//}
			//else
			//{
			//    src = appPath + "WF/CCForm/DtlCard.htm?EnsName=" + dtl.No + "&RefPKVal=" + en.PKVal + "&IsReadonly=1";
			//}

			//sb.Append("<iframe ID='F" + dtl.No + "' onload= 'F" + dtl.No + "load();'  src='" + src + "' frameborder=0  style='position:absolute;width:" + dtl.W + "px; height:" + dtl.H + "px;text-align: left;'  leftMargin='0'  topMargin='0' scrolling=auto /></iframe>");

			sb.append("</span>");
			sb.append("</DIV>");
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 输出明细.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 审核组件
		if (flowNo != null)
		{
			FrmWorkCheck fwc = new FrmWorkCheck(frmID);
			if (fwc.getHisFrmWorkCheckSta() != FrmWorkCheckSta.Disable)
			{
				x = fwc.getFWC_X() + wtX;
				sb.append("<DIV id='DIVWC" + fwc.getNo() + "' style='position:absolute; left:" + x + "px; top:" + fwc.getFWC_Y() + "px; width:" + fwc.getFWC_W() + "px; height:" + fwc.getFWC_H() + "px;text-align: left;' >");
				sb.append("<span>");

				sb.append("<table   style='border: 1px outset #C0C0C0;padding: inherit; margin: 0;border-collapse:collapse;width:100%;' >");

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#region 生成审核信息.
				if (flowNo != null)
				{
					String sql = "SELECT EmpFrom, EmpFromT,RDT,Msg,NDFrom,NDFromT FROM ND" + Integer.parseInt(flowNo) + "Track WHERE WorkID=" + workid + " AND ActionType=" + ActionType.WorkCheck.getValue() + " ORDER BY RDT ";
					DataTable dt = DBAccess.RunSQLReturnTable(sql);

					//获得当前待办的人员,把当前审批的人员排除在外,不然就有默认同意的意见可以打印出来.
					sql = "SELECT FK_Emp, FK_Node FROM WF_GenerWorkerList WHERE IsPass!=1 AND WorkID=" + workid;
					DataTable dtOfTodo = DBAccess.RunSQLReturnTable(sql);

					for (DataRow dr : dt.Rows)
					{

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
							///#region 排除正在审批的人员.
						String nodeID = dr.get("NDFrom").toString();
						String empFrom = dr.get("EmpFrom").toString();
						if (dtOfTodo.Rows.size() != 0)
						{
							boolean isHave = false;
							for (DataRow mydr : dtOfTodo.Rows)
							{
								if (!mydr.get("FK_Node").toString().equals(nodeID))
								{
									continue;
								}

								if (!mydr.get("FK_Emp").toString().equals(empFrom))
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
							///#endregion 排除正在审批的人员.

						sb.append("<tr>");
						sb.append("<td valign=middle style='border-style: solid;padding: 4px;text-align: left;color: #333333;font-size: 12px;border-width: 1px;border-color: #C2D5E3;' >" + dr.get("NDFromT") + "</td>");

						sb.append("<br><br>");

						String msg = dr.get("Msg").toString();

						msg += "<br>";
						msg += "<br>";
						msg += "审核人:" + dr.get("EmpFromT") + " &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日期:" + dr.get("RDT").toString();

						sb.append("<td colspan=3 valign=middle style='border-style: solid;padding: 4px;text-align: left;color: #333333;font-size: 12px;border-width: 1px;border-color: #C2D5E3;' >" + msg + "</td>");
						sb.append("</tr>");
					}
				}
				sb.append("</table>");
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion 生成审核信息.

				sb.append("</span>");
				sb.append("</DIV>");
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 审核组件

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 父子流程组件
		if (flowNo != null)
		{
			FrmSubFlow subFlow = new FrmSubFlow(frmID);
			if (subFlow.getHisFrmSubFlowSta() != FrmSubFlowSta.Disable)
			{
				x = subFlow.getSF_X() + wtX;
				sb.append("<DIV id='DIVWC" + subFlow.getNo() + "' style='position:absolute; left:" + x + "px; top:" + subFlow.getSF_Y() + "px; width:" + subFlow.getSF_W() + "px; height:" + subFlow.getSF_H() + "px;text-align: left;' >");
				sb.append("<span>");

				String src = appPath + "WF/WorkOpt/SubFlow.aspx?s=2";
				String fwcOnload = "";

				if (subFlow.getHisFrmSubFlowSta() == FrmSubFlowSta.Readonly)
				{
					src += "&DoType=View";
				}

				src += "&r=q";
				sb.append("<iframe ID='FSF" + subFlow.getNo() + "' " + fwcOnload + "  src='" + src + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='" + subFlow.getSF_W() + "' height='" + subFlow.getSF_H() + "'   scrolling=auto/></iframe>");

				sb.append("</span>");
				sb.append("</DIV>");
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 父子流程组件

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 输出附件
		FrmAttachments aths = new FrmAttachments(frmID);
		//FrmAttachmentDBs athDBs = null;
		//if (aths.size() > 0)
		//    athDBs = new FrmAttachmentDBs(frmID, en.PKVal.ToString());

		for (FrmAttachment ath : aths)
		{

			if (ath.UploadType == AttachmentUploadType.Single)
			{
				/* 单个文件 */
				FrmAttachmentDBs athDBs = BP.WF.Glo.GenerFrmAttachmentDBs(ath, String.valueOf(workid), ath.MyPK);
				Object tempVar = athDBs.GetEntityByKey(FrmAttachmentDBAttr.FK_FrmAttachment, ath.MyPK);
				FrmAttachmentDB athDB = tempVar instanceof FrmAttachmentDB ? (FrmAttachmentDB)tempVar : null;
				x = ath.X + wtX;
				float y = ath.Y;
				sb.append("<DIV id='Fa" + ath.MyPK + "' style='position:absolute; left:" + x + "px; top:" + y + "px; text-align: left;float:left' >");
				//  sb.Append("<span>");
				sb.append("<DIV>");

				sb.append("附件没有转化:" + athDB.FileName);


				sb.append("</DIV>");
				sb.append("</DIV>");
			}

			if (ath.UploadType == AttachmentUploadType.Multi)
			{
				x = ath.X + wtX;
				sb.append("<DIV id='Fd" + ath.MyPK + "' style='position:absolute; left:" + x + "px; top:" + ath.Y + "px; width:" + ath.W + "px; height:" + ath.H + "px;text-align: left;' >");
				sb.append("<span>");
				sb.append("<ul>");

				//判断是否有这个目录.
				if ((new File(path + "\\pdf\\")).isDirectory() == false)
				{
					(new File(path + "\\pdf\\")).mkdirs();
				}

				//文件加密
				boolean fileEncrypt = SystemConfig.IsEnableAthEncrypt;
				FrmAttachmentDBs athDBs = BP.WF.Glo.GenerFrmAttachmentDBs(ath, String.valueOf(workid), ath.MyPK);

				for (FrmAttachmentDB item : athDBs)
				{
					//获取文件是否加密
					boolean isEncrypt = item.GetParaBoolen("IsEncrypt");
					if (ath.AthSaveWay == AthSaveWay.FTPServer)
					{
						try
						{
							String toFile = path + "\\pdf\\" + item.FileName;
							if ((new File(toFile)).isFile() == false)
							{
								//把文件copy到,
								////获取文件是否加密
								//bool fileEncrypt = SystemConfig.IsEnableAthEncrypt;
								//bool isEncrypt = item.GetParaBoolen("IsEncrypt");
								String file = item.GenerTempFile(ath.AthSaveWay);
								String fileTempDecryPath = file;
								if (fileEncrypt == true && isEncrypt == true)
								{
									fileTempDecryPath = file + ".tmp";
									BP.Tools.EncHelper.DecryptDES(file, fileTempDecryPath);

								}

								Files.copy(Paths.get(fileTempDecryPath), Paths.get(toFile), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
							}

							sb.append("<li><a href='" + item.FileName + "'>" + item.FileName + "</a></li>");
						}
						catch (RuntimeException ex)
						{
							sb.append("<li>" + item.FileName + "(<font color=red>文件未从ftp下载成功{" + ex.getMessage() + "}</font>)</li>");
						}
					}

					if (ath.AthSaveWay == AthSaveWay.IISServer)
					{
						try
						{
							String toFile = path + "\\pdf\\" + item.FileName;
							if ((new File(toFile)).isFile() == false)
							{
								//把文件copy到,
								String fileTempDecryPath = item.FileFullName;
								if (fileEncrypt == true && isEncrypt == true)
								{
									fileTempDecryPath = item.FileFullName + ".tmp";
									BP.Tools.EncHelper.DecryptDES(item.FileFullName, fileTempDecryPath);

								}

								//把文件copy到,
								Files.copy(Paths.get(fileTempDecryPath), Paths.get(path + "\\pdf\\" + item.FileName), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
							}
							sb.append("<li><a href='" + item.FileName + "'>" + item.FileName + "</a></li>");
						}
						catch (RuntimeException ex)
						{
							sb.append("<li>" + item.FileName + "(<font color=red>文件未从ftp下载成功{" + ex.getMessage() + "}</font>)</li>");
						}
					}

				}
				sb.append("</ul>");

				sb.append("</span>");
				sb.append("</DIV>");
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 输出附件.

		return sb;
	}


	private static StringBuilder GenerHtmlOfFool(MapData mapData, String frmID, long workid, Entity en, String path, String flowNo, String FK_Node, String basePath)
	{
		return GenerHtmlOfFool(mapData, frmID, workid, en, path, flowNo, FK_Node, basePath, NodeFormType.FoolForm);
	}

	private static StringBuilder GenerHtmlOfFool(MapData mapData, String frmID, long workid, Entity en, String path, String flowNo, String FK_Node)
	{
		return GenerHtmlOfFool(mapData, frmID, workid, en, path, flowNo, FK_Node, null, NodeFormType.FoolForm);
	}

	private static StringBuilder GenerHtmlOfFool(MapData mapData, String frmID, long workid, Entity en, String path, String flowNo)
	{
		return GenerHtmlOfFool(mapData, frmID, workid, en, path, flowNo, null, null, NodeFormType.FoolForm);
	}

	private static StringBuilder GenerHtmlOfFool(MapData mapData, String frmID, long workid, Entity en, String path)
	{
		return GenerHtmlOfFool(mapData, frmID, workid, en, path, null, null, null, NodeFormType.FoolForm);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: private static StringBuilder GenerHtmlOfFool(MapData mapData, string frmID, Int64 workid, Entity en, string path, string flowNo = null, string FK_Node = null, string basePath = null, NodeFormType formType = NodeFormType.FoolForm)
	private static StringBuilder GenerHtmlOfFool(MapData mapData, String frmID, long workid, Entity en, String path, String flowNo, String FK_Node, String basePath, NodeFormType formType)
	{
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
					if (frmIDs.contains("ND" + dr.get(0).toString()) == true)
					{
						continue;
					}
					frmIDs += "'ND" + dr.get(0).toString() + "',";
				}
			}
			frmIDs = frmIDs.substring(0, frmIDs.length() - 1);
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
			attrs = en.EnMap.Attrs;
		}

		//生成表头.
		String frmName = mapData.Name;
		if (SystemConfig.AppSettings["CustomerNo"].equals("TianYe"))
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
		if (SystemConfig.GetValByKeyBoolen("IsShowQrCode", false) == false)
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


		for (GroupField gf : gfs)
		{
			//输出标题.
			if (!gf.CtrlType.equals("Ath"))
			{
				sb.append(" <tr>");
				sb.append("  <th colspan=4><b>" + gf.Lab + "</b></th>");
				sb.append(" </tr>");
			}

			///#region 输出字段.
			if (gf.CtrlID.equals("") && gf.CtrlType.equals(""))
			{
				boolean isDropTR = true;
				String html = "";
				for (MapAttr attr : mapAttrs)
				{
					//处理隐藏字段，如果是不可见并且是启用的就隐藏.
					if (attr.UIVisible == false)
					{
						continue;
					}
					if (attr.GroupID != attr.GroupID)
					{
						continue;
					}
					//处理分组数据，非当前分组的数据不输出
					if (attr.GroupID != gf.OID)
					{
						continue;
					}

					String text = "";

					switch (attr.LGType)
					{
						case FieldTypeS.Normal: // 输出普通类型字段.
							if (attr.MyDataType == 1 && (int)attr.UIContralType == DataType.AppString)
							{

								if (attrs.Contains(attr.KeyOfEn + "Text") == true)
								{
									text = en.GetValRefTextByKey(attr.KeyOfEn);
								}
								if (DataType.IsNullOrEmpty(text))
								{
									if (attrs.Contains(attr.KeyOfEn + "T") == true)
									{
										text = en.GetValStrByKey(attr.KeyOfEn + "T");
									}
								}
							}
							else
							{
								text = en.GetValStrByKey(attr.KeyOfEn);
								if (attr.IsRichText == true)
								{
									text = text.replace("white-space: nowrap;", "");
								}
							}

							break;
						case FieldTypeS.Enum:
						case FieldTypeS.FK:
							text = en.GetValRefTextByKey(attr.KeyOfEn);
							break;
						default:
							break;
					}

					if (attr.IsBigDoc)
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

					if (attr.MyDataType == DataType.AppBoolean)
					{
						if (DataType.IsNullOrEmpty(text) || text.equals("0"))
						{
							text = "[&#10005]" + attr.Name;
						}
						else
						{
							text = "[&#10004]" + attr.Name;
						}
					}

					//线性展示并且colspan=3
					if (attr.ColSpan == 3 || (attr.ColSpan == 4 && attr.UIHeightInt < 30))
					{
						isDropTR = true;
						html += " <tr>";
						html += " <td  class='FDesc'  >" + attr.Name + "</td>";
						html += " <td ColSpan=3>";
						html += text;
						html += " </td>";
						html += " </tr>";
						continue;
					}

					//线性展示并且colspan=4
					if (attr.ColSpan == 4)
					{
						isDropTR = true;
						html += " <tr>";
						html += " <td ColSpan=4 class='FDesc' >" + attr.Name + "</td>";
						html += " </tr>";
						html += " <tr>";
						html += " <td ColSpan=4>";
						html += text;
						html += " </td>";
						html += " </tr>";
						continue;
					}

					if (isDropTR == true)
					{
						html += " <tr>";
						html += " <td class='FDesc' >" + attr.Name + "</td>";
						html += " <td class='FContext'  >";
						html += text;
						html += " </td>";
						isDropTR = !isDropTR;
						continue;
					}

					if (isDropTR == false)
					{
						html += " <td  class='FDesc'>" + attr.Name + "</td>";
						html += " <td class='FContext'  >";
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
			if (gf.CtrlType.equals("Dtl"))
			{
				if (DataType.IsNullOrEmpty(gf.CtrlID) == true)
				{
					continue;
				}
				/* 如果是从表 */
				MapAttrs attrsOfDtls = null;
				try
				{
					attrsOfDtls = new MapAttrs(gf.CtrlID);
				}
				catch (RuntimeException ex)
				{
				}

				///#region 输出标题.
				sb.append("<tr><td valign=top colspan=4 >");

				sb.append("<table style='wdith:100%' >");
				sb.append("<tr>");
				for (MapAttr item : attrsOfDtls)
				{
					if (item.KeyOfEn.equals("OID"))
					{
						continue;
					}
					if (item.UIVisible == false)
					{
						continue;
					}

					sb.append("<th stylle='width:" + item.UIWidthInt + "px;'>" + item.Name + "</th>");
				}
				sb.append("</tr>");
				///#endregion 输出标题.


				///#region 输出数据.
				GEDtls dtls = new GEDtls(gf.CtrlID);
				dtls.Retrieve(GEDtlAttr.RefPK, workid);
				for (GEDtl dtl : dtls)
				{
					sb.append("<tr>");

					for (MapAttr item : attrsOfDtls)
					{
						if (item.KeyOfEn.equals("OID") || item.UIVisible == false)
						{
							continue;
						}

						if (item.UIContralType == UIContralType.DDL)
						{
							sb.append("<td>" + dtl.GetValRefTextByKey(item.KeyOfEn) + "</td>");
							continue;
						}

						if (item.IsNum)
						{
							sb.append("<td style='text-align:right' >" + dtl.GetValStrByKey(item.KeyOfEn) + "</td>");
							continue;
						}

						sb.append("<td>" + dtl.GetValStrByKey(item.KeyOfEn) + "</td>");
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
			if (gf.CtrlType.equals("Ath"))
			{
				if (DataType.IsNullOrEmpty(gf.CtrlID) == true)
				{
					continue;
				}
				FrmAttachment ath = new FrmAttachment(gf.CtrlID);
				if (ath.IsVisable == false)
				{
					continue;
				}

				sb.append(" <tr>");
				sb.append("  <th colspan=4><b>" + gf.Lab + "</b></th>");
				sb.append(" </tr>");

				FrmAttachmentDBs athDBs = BP.WF.Glo.GenerFrmAttachmentDBs(ath, String.valueOf(workid), ath.MyPK);


				if (ath.UploadType == AttachmentUploadType.Single)
				{
					/* 单个文件 */
					sb.append("<tr><td colspan=4>单附件没有转化:" + ath.MyPK + "</td></td>");
					continue;
				}

				if (ath.UploadType == AttachmentUploadType.Multi)
				{
					sb.append("<tr><td valign=top colspan=4 >");
					sb.append("<ul>");

					//判断是否有这个目录.
					if ((new File(path + "\\pdf\\")).isDirectory() == false)
					{
						(new File(path + "\\pdf\\")).mkdirs();
					}

					for (FrmAttachmentDB item : athDBs)
					{
						String fileTo = path + "\\pdf\\" + item.FileName;
						//加密信息
						boolean fileEncrypt = SystemConfig.IsEnableAthEncrypt;
						boolean isEncrypt = item.GetParaBoolen("IsEncrypt");
						///#region 从ftp服务器上下载.
						if (ath.AthSaveWay == AthSaveWay.FTPServer)
						{
							try
							{
								if ((new File(fileTo)).isFile() == true)
								{
									(new File(fileTo)).delete(); //rn "err@删除已经存在的文件错误,请检查iis的权限:" + ex.getMessage();
								}

								//把文件copy到,                                  
								String file = item.GenerTempFile(ath.AthSaveWay);

								String fileTempDecryPath = file;
								if (fileEncrypt == true && isEncrypt == true)
								{
									fileTempDecryPath = file + ".tmp";
									BP.Tools.EncHelper.DecryptDES(file, fileTempDecryPath);

								}
								Files.copy(Paths.get(fileTempDecryPath), Paths.get(fileTo), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);

								sb.append("<li><a href='" + SystemConfig.GetValByKey("HostURL", "") + "/DataUser/InstancePacketOfData/" + FK_Node + "/" + workid + "/" + "pdf/" + item.FileName + "'>" + item.FileName + "</a></li>");
							}
							catch (RuntimeException ex)
							{
								sb.append("<li>" + item.FileName + "(<font color=red>文件未从ftp下载成功{" + ex.getMessage() + "}</font>)</li>");
							}
						}
						///#endregion 从ftp服务器上下载.


						///#region 从iis服务器上下载.
						if (ath.AthSaveWay == AthSaveWay.IISServer)
						{
							try
							{

								String fileTempDecryPath = item.FileFullName;
								if (fileEncrypt == true && isEncrypt == true)
								{
									fileTempDecryPath = item.FileFullName + ".tmp";
									BP.Tools.EncHelper.DecryptDES(item.FileFullName, fileTempDecryPath);

								}

								//把文件copy到,
								Files.copy(Paths.get(fileTempDecryPath), Paths.get(fileTo), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);

								sb.append("<li><a href='" + SystemConfig.GetValByKey("HostURL", "") + "/DataUser/InstancePacketOfData/" + frmID + "/" + workid + "/" + "pdf/" + item.FileName + "'>" + item.FileName + "</a></li>");
							}
							catch (RuntimeException ex)
							{
								sb.append("<li>" + item.FileName + "(<font color=red>文件未从web下载成功{" + ex.getMessage() + "}</font>)</li>");
							}
						}

					}
					sb.append("</ul>");
					sb.append("</td></tr>");
				}

			}
			///#endregion 如果是附件.

			//如果是IFrame页面
			if (gf.CtrlType.equals("Frame") && flowNo != null)
			{
				if (DataType.IsNullOrEmpty(gf.CtrlID) == true)
				{
					continue;
				}
				sb.append("<tr>");
				sb.append("  <td colspan='4' >");

				//根据GroupID获取对应的
				MapFrame frame = new MapFrame(gf.CtrlID);
				//获取URL
				String url = frame.URL;

				//替换URL的
				url = url.replace("@basePath", basePath);
				//替换系统参数
				url = url.replace("@WebUser.No", WebUser.No);
				url = url.replace("@WebUser.Name;", WebUser.Name);
				url = url.replace("@WebUser.FK_DeptName;", WebUser.FK_DeptName);
				url = url.replace("@WebUser.FK_Dept;", WebUser.FK_Dept);

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
				sb.append("<iframe style='width:100%;height:auto;' ID='" + frame.MyPK + "'    src='" + url + "' frameborder=0  leftMargin='0'  topMargin='0' scrolling=auto></iframe></div>");
				sb.append("</td>");
				sb.append("</tr>");
			}


			///#region 审核组件
			if (gf.CtrlType.equals("FWC") && flowNo != null)
			{
				FrmWorkCheck fwc = new FrmWorkCheck(frmID);

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
					sql = "SELECT a.No, a.SignType FROM Port_Emp a, " + tTable + " b WHERE a.No=b.EmpFrom AND B.WorkID=" + workid;

					dtTrack = DBAccess.RunSQLReturnTable(sql);
					dtTrack.TableName = "SignType";
					if (dtTrack.Columns.Contains("No") == false)
					{
						dtTrack.Columns.Add("No");
					}
					if (dtTrack.Columns.Contains("SignType") == false)
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
					String nodeID = dr.get("NDFrom").toString();
					String empFrom = dr.get("EmpFrom").toString();
					if (dtOfTodo.Rows.size() != 0)
					{
						boolean isHave = false;
						for (DataRow mydr : dtOfTodo.Rows)
						{
							if (!mydr.get("FK_Node").toString().equals(nodeID))
							{
								continue;
							}

							if (!mydr.get("FK_Emp").toString().equals(empFrom))
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
					html += " <td valign=middle >" + dr.get("NDFromT") + "</td>";

					String msg = dr.get("Msg").toString();

					msg += "<br>";
					msg += "<br>";

					String empStrs = "";
					if (dtTrack == null)
					{
						empStrs = dr.get("EmpFromT").toString();
					}
					else
					{
						String singType = "0";
						for (DataRow drTrack : dtTrack.Rows)
						{
							if (drTrack.get("No").toString().equals(dr.get("EmpFrom").toString()))
							{
								singType = drTrack.get("SignType").toString();
								break;
							}
						}

						if (singType.equals("0") || singType.equals("2"))
						{
							empStrs = dr.get("EmpFromT").toString();
						}


						if (singType.equals("1"))
						{
							empStrs = "<img src='../../../../../DataUser/Siganture/" + dr.get("EmpFrom") + ".jpg' title='" + dr.get("EmpFromT") + "' style='height:60px;' border=0 onerror=\"src='../../../../../DataUser/Siganture/UnName.JPG'\" /> " + dr.get("EmpFromT");
						}

					}
					msg += "审核人:" + empStrs + " &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日期:" + dr.get("RDT").toString();

					html += " <td colspan=3 valign=middle >" + msg + "</td>";
					html += " </tr>";
				}
				///#endregion 生成审核信息.

				sb.append(" " + html);
			}
		}

		sb.append("</table>");
		return sb;
	}
	/** 
	 树形表单转成PDF.
	*/
	public static String MakeCCFormToPDF(Node node, long workid, String flowNo, String fileNameFormat, boolean urlIsHostUrl, String basePath)
	{
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
		String hostURL = SystemConfig.GetValByKey("HostURL", "");
		String path = SystemConfig.PathOfDataUser + "InstancePacketOfData\\" + "ND" + node.getNodeID() + "\\" + workid;
		String frmID = node.getNodeFrmID();

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

		fileNameFormat = BP.DA.DataType.PraseStringToFileName(fileNameFormat);

		Hashtable ht = new Hashtable();

		if (node.getHisFormType().getValue() == NodeFormType.FoolForm.getValue() || node.getHisFormType().getValue() == NodeFormType.FreeForm.getValue() || node.getHisFormType().getValue() == NodeFormType.RefOneFrmTree.getValue() || node.getHisFormType().getValue() == NodeFormType.FoolTruck.getValue())
		{
			resultMsg = setPDFPath("ND" + node.getNodeID(), workid, flowNo, gwf);
			if (resultMsg.indexOf("err@") != -1)
			{
				return resultMsg;
			}

			String billUrl = SystemConfig.PathOfDataUser + "InstancePacketOfData\\" + "ND" + node.getNodeID() + "\\" + workid + "\\index.htm";

			resultMsg = MakeHtmlDocument(frmID, workid, flowNo, fileNameFormat, urlIsHostUrl, path, billUrl, "ND" + node.getNodeID(), basePath);

			if (resultMsg.indexOf("err@") != -1)
			{
				return resultMsg;
			}

			ht.put("htm", SystemConfig.GetValByKey("HostURLOfBS", "../../DataUser") + "/InstancePacketOfData/" + "ND" + node.getNodeID() + "/" + workid + "/index.htm");

			///#region 把所有的文件做成一个zip文件.
			//生成pdf文件
			String pdfPath = path + "\\pdf";

			if ((new File(pdfPath)).isDirectory() == false)
			{
				(new File(pdfPath)).mkdirs();
			}

			fileNameFormat = fileNameFormat.substring(0, fileNameFormat.length() - 1);
			String pdfFile = pdfPath + "\\" + fileNameFormat + ".pdf";
			String pdfFileExe = SystemConfig.PathOfDataUser + "ThirdpartySoftware\\wkhtmltox\\wkhtmltopdf.exe";
			try
			{
				Html2Pdf(pdfFileExe, billUrl, pdfFile);
				if (urlIsHostUrl == false)
				{
					ht.put("pdf", SystemConfig.GetValByKey("HostURLOfBS", "../../DataUser/") + "InstancePacketOfData/" + "ND" + node.getNodeID() + "/" + workid + "/pdf/" + DataType.PraseStringToUrlFileName(fileNameFormat) + ".pdf");
				}
				else
				{
					ht.put("pdf", SystemConfig.GetValByKey("HostURL", "") + "/DataUser/InstancePacketOfData/" + "ND" + node.getNodeID() + "/" + workid + "/pdf/" + DataType.PraseStringToUrlFileName(fileNameFormat) + ".pdf");
				}

			}
			catch (RuntimeException ex)
			{
				/*有可能是因为文件路径的错误， 用补偿的方法在执行一次, 如果仍然失败，按照异常处理. */
				fileNameFormat = DBAccess.GenerGUID();
				pdfFile = pdfPath + "\\" + fileNameFormat + ".pdf";

				Html2Pdf(pdfFileExe, billUrl, pdfFile);
				ht.put("pdf", SystemConfig.GetValByKey("HostURLOfBS", "") + "/InstancePacketOfData/" + "ND" + node.getNodeID() + "/" + workid + "/pdf/" + fileNameFormat + ".pdf");
			}

			//生成压缩文件
			String zipFile = path + "\\..\\" + fileNameFormat + ".zip";

			File finfo = new File(zipFile);
			ZipFilePath = finfo.getPath(); //文件路径.

			try
			{
				(new FastZip()).CreateZip(finfo.getPath(), pdfPath, true, "");

				ht.put("zip", SystemConfig.HostURLOfBS + "/DataUser/InstancePacketOfData/" + "ND" + node.getNodeID() + "/" + DataType.PraseStringToUrlFileName(fileNameFormat) + ".zip");
			}
			catch (RuntimeException ex)
			{
				ht.put("zip", "err@生成zip文件遇到权限问题:" + ex.getMessage() + " @Path:" + pdfFile);
			}

			//把所有的文件做成一个zip文件.

			return BP.Tools.Json.ToJsonEntitiesNoNameMode(ht);
		}

		if (node.getHisFormType().getValue() == NodeFormType.SheetTree.getValue())
		{

			//生成pdf文件
			String pdfPath = path + "\\pdf";
			String pdfTempPath = path + "\\pdfTemp";

			DataRow dr = null;
			resultMsg = setPDFPath("ND" + node.getNodeID(), workid, flowNo, gwf);
			if (resultMsg.indexOf("err@") != -1)
			{
				return resultMsg;
			}

			//获取绑定的表单
			FrmNodes nds = new FrmNodes(node.getFK_Flow(), node.getNodeID());
			for (FrmNode item : nds)
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
				String billUrl = SystemConfig.PathOfDataUser + "InstancePacketOfData\\" + "ND" + node.getNodeID() + "\\" + workid + "\\" + item.getFK_Frm() + "index.htm";
				resultMsg = MakeHtmlDocument(item.getFK_Frm(), workid, flowNo, fileNameFormat, urlIsHostUrl, path, billUrl, "ND" + node.getNodeID(), basePath);

				if (resultMsg.indexOf("err@") != -1)
				{
					return resultMsg;
				}

				ht.put("htm_" + item.getFK_Frm(), SystemConfig.GetValByKey("HostURLOfBS", "../../DataUser/") + "/InstancePacketOfData/" + "ND" + node.getNodeID() + "/" + workid + "/" + item.getFK_Frm() + "index.htm");

				///#region 把所有的文件做成一个zip文件.
				if ((new File(pdfTempPath)).isDirectory() == false)
				{
					(new File(pdfTempPath)).mkdirs();
				}

				fileNameFormat = fileNameFormat.substring(0, fileNameFormat.length() - 1);
				String pdfFormFile = pdfTempPath + "\\" + item.getFK_Frm() + ".pdf";
				String pdfFileExe = SystemConfig.PathOfDataUser + "ThirdpartySoftware\\wkhtmltox\\wkhtmltopdf.exe";
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
			String pdfFile = pdfPath + "\\" + fileNameFormat + ".pdf";
			//开始合并处理
			if ((new File(pdfPath)).isDirectory() == false)
			{
				(new File(pdfPath)).mkdirs();
			}

			MergePDF(pdfTempPath, pdfFile); //合并pdf
										   //合并完删除文件夹

			Directory.Delete(pdfTempPath, true);
			if (urlIsHostUrl == false)
			{
				ht.put("pdf", SystemConfig.GetValByKey("HostURLOfBS", "../../DataUser/") + "InstancePacketOfData/" + frmID + "/" + workid + "/pdf/" + DataType.PraseStringToUrlFileName(fileNameFormat) + ".pdf");
			}
			else
			{
				ht.put("pdf", SystemConfig.GetValByKey("HostURL", "") + "/DataUser/InstancePacketOfData/" + frmID + "/" + workid + "/pdf/" + DataType.PraseStringToUrlFileName(fileNameFormat) + ".pdf");
			}

			//生成压缩文件
			String zipFile = path + "\\..\\" + fileNameFormat + ".zip";

			File finfo = new File(zipFile);
			ZipFilePath = finfo.getPath(); //文件路径.

			try
			{
				(new FastZip()).CreateZip(finfo.getPath(), pdfPath, true, "");

				ht.put("zip", SystemConfig.HostURLOfBS + "/DataUser/InstancePacketOfData/" + frmID + "/" + DataType.PraseStringToUrlFileName(fileNameFormat) + ".zip");
			}
			catch (RuntimeException ex)
			{
				ht.put("zip", "err@生成zip文件遇到权限问题:" + ex.getMessage() + " @Path:" + pdfFile);
			}



			return BP.Tools.Json.ToJsonEntitiesNoNameMode(ht);
		}

		return "warning@不存在需要打印的表单";

	}


	public static String MakeBillToPDF(String frmId, long workid, String basePath)
	{
		return MakeBillToPDF(frmId, workid, basePath, false);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static string MakeBillToPDF(string frmId, Int64 workid, string basePath, bool urlIsHostUrl = false)
	public static String MakeBillToPDF(String frmId, long workid, String basePath, boolean urlIsHostUrl)
	{

		String resultMsg = "";

		//  获取单据的属性信息
		BP.Frm.FrmBill bill = new BP.Frm.FrmBill(frmId);
		String fileNameFormat = null;

		//存放信息地址
		String hostURL = SystemConfig.GetValByKey("HostURL", "");
		String path = SystemConfig.PathOfDataUser + "InstancePacketOfData\\" + bill.No + "\\" + workid;

		//处理正确的文件名.
		if (fileNameFormat == null)
		{
			fileNameFormat = DBAccess.RunSQLReturnStringIsNull("SELECT Title FROM Frm_GenerBill WHERE WorkID=" + workid, "" + String.valueOf(workid));
		}


		if (DataType.IsNullOrEmpty(fileNameFormat) == true)
		{
			fileNameFormat = String.valueOf(workid);
		}

		fileNameFormat = BP.DA.DataType.PraseStringToFileName(fileNameFormat);

		Hashtable ht = new Hashtable();

		//生成pdf文件
		String pdfPath = path + "\\pdf";


		DataRow dr = null;
		resultMsg = setPDFPath(frmId, workid, null, null);
		if (resultMsg.indexOf("err@") != -1)
		{
			return resultMsg;
		}



		//获取表单的信息执行打印
		String billUrl = SystemConfig.PathOfDataUser + "InstancePacketOfData\\" + bill.No + "\\" + workid + "\\" + "index.htm";
		resultMsg = MakeHtmlDocument(bill.No, workid, null, fileNameFormat, urlIsHostUrl, path, billUrl, frmId, basePath);

		if (resultMsg.indexOf("err@") != -1)
		{
			return resultMsg;
		}

		ht.put("htm", SystemConfig.GetValByKey("HostURLOfBS", "../../DataUser/") + "InstancePacketOfData/" + frmId + "/" + workid + "/" + "index.htm");

		///#region 把所有的文件做成一个zip文件.
		if ((new File(pdfPath)).isDirectory() == false)
		{
			(new File(pdfPath)).mkdirs();
		}

		fileNameFormat = fileNameFormat.substring(0, fileNameFormat.length() - 1);
		String pdfFormFile = pdfPath + "\\" + bill.Name + ".pdf";
		String pdfFileExe = SystemConfig.PathOfDataUser + "ThirdpartySoftware\\wkhtmltox\\wkhtmltopdf.exe";
		try
		{
			Html2Pdf(pdfFileExe, resultMsg, pdfFormFile);
			if (urlIsHostUrl == false)
			{
				ht.put("pdf", SystemConfig.GetValByKey("HostURLOfBS", "../../DataUser/") + "InstancePacketOfData/" + frmId + "/" + workid + "/pdf/" + bill.Name + ".pdf");
			}
			else
			{
				ht.put("pdf", SystemConfig.GetValByKey("HostURL", "") + "/DataUser/InstancePacketOfData/" + frmId + "/" + workid + "/pdf/" + bill.Name + ".pdf");
			}


		}
		catch (RuntimeException ex)
		{
			/*有可能是因为文件路径的错误， 用补偿的方法在执行一次, 如果仍然失败，按照异常处理. */
			fileNameFormat = DBAccess.GenerGUID();
			pdfFormFile = pdfPath + "\\" + fileNameFormat + ".pdf";

			Html2Pdf(pdfFileExe, resultMsg, pdfFormFile);
			ht.put("pdf", SystemConfig.GetValByKey("HostURLOfBS", "") + "/InstancePacketOfData/" + frmId + "/" + workid + "/pdf/" + bill.Name + ".pdf");
		}

		//生成压缩文件
		String zipFile = path + "\\..\\" + fileNameFormat + ".zip";

		File finfo = new File(zipFile);
		ZipFilePath = finfo.getPath(); //文件路径.

		try
		{
			(new FastZip()).CreateZip(finfo.getPath(), pdfPath, true, "");

			ht.put("zip", SystemConfig.HostURLOfBS + "/DataUser/InstancePacketOfData/" + frmId + "/" + DataType.PraseStringToUrlFileName(fileNameFormat) + ".zip");
		}
		catch (RuntimeException ex)
		{
			ht.put("zip", "err@生成zip文件遇到权限问题:" + ex.getMessage() + " @Path:" + pdfPath);
		}


		return BP.Tools.Json.ToJsonEntitiesNoNameMode(ht);


	}

	public static String MakeFormToPDF(String frmId, String frmName, Node node, long workid, String flowNo, String fileNameFormat, boolean urlIsHostUrl, String basePath)
	{

		String resultMsg = "";
		GenerWorkFlow gwf = null;

		//获取主干流程信息
		if (flowNo != null)
		{
			gwf = new GenerWorkFlow(workid);
		}

		//存放信息地址
		String hostURL = SystemConfig.GetValByKey("HostURL", "");
		String path = SystemConfig.PathOfDataUser + "InstancePacketOfData\\" + "ND" + node.getNodeID() + "\\" + workid;

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

		fileNameFormat = BP.DA.DataType.PraseStringToFileName(fileNameFormat);

		Hashtable ht = new Hashtable();

		//生成pdf文件
		String pdfPath = path + "\\pdf";


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
		String billUrl = SystemConfig.PathOfDataUser + "InstancePacketOfData\\" + "ND" + node.getNodeID() + "\\" + workid + "\\" + frmNode.getFK_Frm() + "index.htm";
		resultMsg = MakeHtmlDocument(frmNode.getFK_Frm(), workid, flowNo, fileNameFormat, urlIsHostUrl, path, billUrl, "ND" + node.getNodeID(), basePath);

		if (resultMsg.indexOf("err@") != -1)
		{
			return resultMsg;
		}

		// ht.Add("htm", SystemConfig.GetValByKey("HostURLOfBS", "../../DataUser/") + "/InstancePacketOfData/" + "ND" + node.NodeID + "/" + workid + "/" + frmNode.FK_Frm + "index.htm");

		///#region 把所有的文件做成一个zip文件.
		if ((new File(pdfPath)).isDirectory() == false)
		{
			(new File(pdfPath)).mkdirs();
		}

		fileNameFormat = fileNameFormat.substring(0, fileNameFormat.length() - 1);
		String pdfFormFile = pdfPath + "\\" + frmNode.getFK_Frm() + ".pdf";
		String pdfFileExe = SystemConfig.PathOfDataUser + "ThirdpartySoftware\\wkhtmltox\\wkhtmltopdf.exe";
		try
		{
			Html2Pdf(pdfFileExe, resultMsg, pdfFormFile);
			if (urlIsHostUrl == false)
			{
				ht.put("pdf", SystemConfig.GetValByKey("HostURLOfBS", "../../DataUser/") + "InstancePacketOfData/" + "ND" + node.getNodeID() + "/" + workid + "/pdf/" + frmNode.getFK_Frm() + ".pdf");
			}
			else
			{
				ht.put("pdf", SystemConfig.GetValByKey("HostURL", "") + "/DataUser/InstancePacketOfData/" + "ND" + node.getNodeID() + "/" + workid + "/pdf/" + frmNode.getFK_Frm() + ".pdf");
			}


		}
		catch (RuntimeException ex)
		{
			/*有可能是因为文件路径的错误， 用补偿的方法在执行一次, 如果仍然失败，按照异常处理. */
			fileNameFormat = DBAccess.GenerGUID();
			pdfFormFile = pdfPath + "\\" + fileNameFormat + ".pdf";

			Html2Pdf(pdfFileExe, resultMsg, pdfFormFile);
			ht.put("pdf", SystemConfig.GetValByKey("HostURLOfBS", "") + "/InstancePacketOfData/" + "ND" + node.getNodeID() + "/" + workid + "/pdf/" + frmNode.getFK_Frm() + ".pdf");
		}

		return BP.Tools.Json.ToJsonEntitiesNoNameMode(ht);


	}

	/** 
	 读取合并的pdf文件名称
	 
	 @param Directorypath 目录
	 @param outpath 导出的路径
	*/
	public static void MergePDF(String Directorypath, String outpath)
	{
		ArrayList<String> filelist2 = new ArrayList<String>();
		File di2 = new File(Directorypath);
		File[] ff2 = di2.GetFiles("*.pdf");
		BubbleSort(ff2);
		for (File temp : ff2)
		{
			filelist2.add(Directorypath + "\\" + temp.getName());
		}

		PdfReader reader;
		//iTextSharp.text.Rectangle rec = new iTextSharp.text.Rectangle(1403, 991);
		Document document = new Document();
//C# TO JAVA CONVERTER TODO TASK: C# to Java Converter cannot determine whether this System.IO.FileStream is input or output:
		PdfWriter writer = PdfWriter.GetInstance(document, new FileStream(outpath, FileMode.Create));
		document.Open();
		PdfContentByte cb = writer.DirectContent;
		PdfImportedPage newPage;
		for (int i = 0; i < filelist2.size(); i++)
		{
			reader = new PdfReader(filelist2.get(i));
			int iPageNum = reader.NumberOfPages;
			for (int j = 1; j <= iPageNum; j++)
			{
				document.NewPage();
				newPage = writer.GetImportedPage(reader, j);
				cb.AddTemplate(newPage, 0, 0);
			}
		}
		document.Close();
	}
	/** 
	 冒泡排序
	 
	 @param arr 文件名数组
	*/
	public static void BubbleSort(File[] arr)
	{
		for (int i = 0; i < arr.length; i++)
		{
			for (int j = i; j < arr.length; j++)
			{
				if (arr[i].LastWriteTime.compareTo(arr[j].LastWriteTime) > 0) //按创建时间（升序）
				{
					File temp = arr[i];
					arr[i] = arr[j];
					arr[j] = temp;
				}
			}
		}
	}


	//前期文件的准备
	private static String setPDFPath(String frmID, long workid, String flowNo, GenerWorkFlow gwf)
	{
		//准备目录文件.
		String path = SystemConfig.PathOfDataUser + "InstancePacketOfData\\" + frmID + "\\";
		try
		{

			path = SystemConfig.PathOfDataUser + "InstancePacketOfData\\" + frmID + "\\";
			if ((new File(path)).isDirectory() == false)
			{
				(new File(path)).mkdirs();
			}

			path = SystemConfig.PathOfDataUser + "InstancePacketOfData\\" + frmID + "\\" + workid;
			if ((new File(path)).isDirectory() == false)
			{
				(new File(path)).mkdirs();
			}

			//把模版文件copy过去.
			String templateFilePath = SystemConfig.PathOfDataUser + "InstancePacketOfData\\Template\\";
			File dir = new File(templateFilePath);
			File[] finfos = dir.GetFiles();
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
				if ((new File(path + "\\" + fl.getPath())).isFile() == true)
				{
					(new File(path + "\\" + fl.getPath())).delete();
				}
				Files.copy(Paths.get(fl.getPath()), Paths.get(path + "\\" + fl.getName()), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
			}

		}
		catch (RuntimeException ex)
		{
			return "err@读写文件出现权限问题，请联系管理员解决。" + ex.getMessage();
		}

		String hostURL = SystemConfig.GetValByKey("HostURL", "");
		String billUrl = hostURL + "/DataUser/InstancePacketOfData/" + frmID + "/" + workid + "/index.htm";

		// begin生成二维码.
		String pathQR = path + "\\QR.png"; // key.Replace("OID.Img@AppPath", SystemConfig.PathOfWebApp);
		if (SystemConfig.GetValByKeyBoolen("IsShowQrCode", false) == true)
		{
			/*说明是图片文件.*/
			String qrUrl = hostURL + "/WF/WorkOpt/PrintDocQRGuide.htm?FrmID=" + frmID + "&WorkID=" + workid + "&FlowNo=" + flowNo;
			if (flowNo != null)
			{
				gwf = new GenerWorkFlow(workid);
				qrUrl = hostURL + "/WF/WorkOpt/PrintDocQRGuide.htm?AP=" + frmID + "$" + workid + "_" + flowNo + "_" + gwf.getFK_Node() + "_" + gwf.getStarter() + "_" + gwf.getFK_Dept();
			}

			//二维码的生成
			ThoughtWorks.QRCode.Codec.QRCodeEncoder qrc = new ThoughtWorks.QRCode.Codec.QRCodeEncoder();
			qrc.QRCodeEncodeMode = ThoughtWorks.QRCode.Codec.QRCodeEncoder.ENCODE_MODE.BYTE;
			qrc.QRCodeScale = 4;
			qrc.QRCodeVersion = 7;
			qrc.QRCodeErrorCorrect = ThoughtWorks.QRCode.Codec.QRCodeEncoder.ERROR_CORRECTION.M;
			System.Drawing.Bitmap btm = qrc.Encode(qrUrl, System.Text.Encoding.UTF8);
			btm.Save(pathQR);
			//QrCodeUtil.createQrCode(qrUrl,path,"QR.png");
		}
		//end生成二维码.
		return "";
	}

	private static String DownLoadFielToMemoryStream(String url)
	{
		Object tempVar = System.Net.HttpWebRequest.Create(url);
		System.Net.HttpWebRequest wreq = tempVar instanceof System.Net.HttpWebRequest ? (System.Net.HttpWebRequest)tempVar : null;
		Object tempVar2 = wreq.GetResponse();
		System.Net.HttpWebResponse response = tempVar2 instanceof System.Net.HttpWebResponse ? (System.Net.HttpWebResponse)tempVar2 : null;
//C# TO JAVA CONVERTER TODO TASK: C# to Java Converter cannot determine whether this System.IO.MemoryStream is input or output:
		MemoryStream ms = null;
		try (System.IO.Stream stream = response.GetResponseStream())
		{
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: Byte[] buffer = new Byte[response.ContentLength];
			byte[] buffer = new byte[response.ContentLength];
			int offset = 0, actuallyRead = 0;
			do
			{
				actuallyRead = stream.Read(buffer, offset, buffer.length - offset);
				offset += actuallyRead;
			} while (actuallyRead > 0);
//C# TO JAVA CONVERTER TODO TASK: C# to Java Converter cannot determine whether this System.IO.MemoryStream is input or output:
			ms = new MemoryStream(buffer);
		}
		response.Close();
		return Convert.ToBase64String(ms.ToArray());

	}

	/** 
	 zip文件路径.
	*/
	public static String ZipFilePath = "";

	public static String CCFlowAppPath = "/";

	public static String MakeHtmlDocument(String frmID, long workid, String flowNo, String fileNameFormat, boolean urlIsHostUrl, String path, String indexFile, String nodeID, String basePath)
	{
		try
		{
			GenerWorkFlow gwf = null;
			if (flowNo != null)
			{
				gwf = new GenerWorkFlow(workid);
			}

			///#region 定义变量做准备.
			//生成表单信息.
			Node nd = new Node(nodeID);
			MapData mapData = new MapData(frmID);

			if (mapData.HisFrmType == FrmType.Url)
			{
				String url = mapData.Url;
				//替换系统参数
				url = url.replace("@WebUser.No", WebUser.No);
				url = url.replace("@WebUser.Name;", WebUser.Name);
				url = url.replace("@WebUser.FK_DeptName;", WebUser.FK_DeptName);
				url = url.replace("@WebUser.FK_Dept;", WebUser.FK_Dept);

				//替换参数
				if (url.indexOf("?") > 0)
				{
					//获取url中的参数
					String urlParam = url.substring(url.indexOf('?'));
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
							url = url.replace(paramArr[1], gwf.GetValStrByKey(paramArr[1].substring(1)));
						}
					}

				}
				url = url.replace("@basePath", basePath);
				if (url.contains("http") == false)
				{
					url = basePath + url;
				}

				//把URL中的内容转换成流


				String str = "<iframe style='width:100%;height:auto;' ID='" + mapData.No + "'    src='" + url + "' frameborder=0  leftMargin='0'  topMargin='0' scrolling=auto></iframe></div>";
				String docs1 = BP.DA.DataType.ReadTextFile(SystemConfig.PathOfDataUser + "InstancePacketOfData\\Template\\indexUrl.htm");
				//docs1 = docs1.Replace("@Docs", DownLoadFielToMemoryStream(url));

				String url1 = "http://www.baidu.com";
				StringBuilder sb1 = new StringBuilder();
				WebClient MyWebClient = new WebClient();
				MyWebClient.Credentials = CredentialCache.DefaultCredentials; //获取或设置用于向Internet资源的请求进行身份验证的网络凭据
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: Byte[] pageData = MyWebClient.DownloadData(url);
				byte[] pageData = MyWebClient.DownloadData(url); //从指定网站下载数据
				//string pageHtml = Encoding.Default.GetString(pageData);  //如果获取网站页面采用的是GB2312，则使用这句            
				String pageHtml = Encoding.UTF8.GetString(pageData); //如果获取网站页面采用的是UTF-8，则使用这句





				docs1 = docs1.replace("@Width", mapData.FrmW.toString() + "px");
				docs1 = docs1.replace("@Height", mapData.FrmH.toString() + "px");
				if (gwf != null)
				{
					docs1 = docs1.replace("@Title", gwf.getTitle());
				}
				BP.DA.DataType.WriteFile(indexFile, pageHtml);
				return indexFile;
			}
			GEEntity en = new GEEntity(frmID, workid);


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 生成水文.

			String rdt = "";
			if (en.EnMap.Attrs.Contains("RDT"))
			{
				rdt = en.GetValStringByKey("RDT");
				if (rdt.length() > 10)
				{
					rdt = rdt.substring(0, 10);
				}
			}
			//先判断节点中水印的设置
			String words = "";

			if (gwf != null)
			{
				nd = new Node(gwf.getFK_Node());
				if (nd.getNodeID() != 0)
				{
					words = nd.getShuiYinModle();
				}
			}
			if (DataType.IsNullOrEmpty(words) == true)
			{
				words = Glo.getPrintBackgroundWord();
			}
			words = words.replace("@RDT", rdt);

			if (words.contains("@") == true)
			{
				words = Glo.DealExp(words, en);
			}

			String templateFilePathMy = SystemConfig.PathOfDataUser + "InstancePacketOfData\\Template\\";
			WaterImageManage wim = new WaterImageManage();
			wim.DrawWords(templateFilePathMy + "ShuiYin.png", words, Float.parseFloat("0.15"), ImagePosition.Center, path + "\\ShuiYin.png");
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

			//生成 表单的 html.
			StringBuilder sb = new StringBuilder();

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 替换模版文件..
			//首先判断是否有约定的文件.
			String docs = "";
			String tempFile = SystemConfig.PathOfDataUser + "InstancePacketOfData\\Template\\" + mapData.No + ".htm";
			if ((new File(tempFile)).isFile() == false)
			{
				if (gwf != null)
				{

					if (nd.getHisFormType() == NodeFormType.FreeForm)
					{
						mapData.HisFrmType = FrmType.FreeFrm;
					}
					else if (nd.getHisFormType() == NodeFormType.FoolForm || nd.getHisFormType() == NodeFormType.FoolTruck)
					{
						mapData.HisFrmType = FrmType.FoolForm;
					}
					else if (nd.getHisFormType() == NodeFormType.SelfForm)
					{
						mapData.HisFrmType = FrmType.Url;
					}
				}

				if (mapData.HisFrmType == FrmType.FoolForm)
				{
					docs = BP.DA.DataType.ReadTextFile(SystemConfig.PathOfDataUser + "InstancePacketOfData\\Template\\indexFool.htm");
					sb = BP.WF.MakeForm2Html.GenerHtmlOfFool(mapData, frmID, workid, en, path, flowNo, nodeID, basePath, nd.getHisFormType());
					docs = docs.replace("@Width", mapData.FrmW.toString() + "px");
				}
				else if (mapData.HisFrmType == FrmType.FreeFrm)
				{
					docs = BP.DA.DataType.ReadTextFile(SystemConfig.PathOfDataUser + "InstancePacketOfData\\Template\\indexFree.htm");
					sb = BP.WF.MakeForm2Html.GenerHtmlOfFree(mapData, frmID, workid, en, path, flowNo, nodeID, basePath);
					docs = docs.replace("@Width", (mapData.FrmW * 1.5).toString() + "px");
				}
			}




			docs = docs.replace("@Docs", sb.toString());

			docs = docs.replace("@Height", mapData.FrmH.toString() + "px");

			String dateFormat = LocalDateTime.now().toString("yyyy年MM月dd日 HH时mm分ss秒");
			docs = docs.replace("@PrintDT", dateFormat);

			if (flowNo != null)
			{
				gwf = new GenerWorkFlow(workid);
				gwf.setWorkID(workid);
				gwf.RetrieveFromDBSources();

				docs = docs.replace("@Title", gwf.getTitle());

				if (gwf.getWFState() == WFState.Runing)
				{
					if (SystemConfig.CustomerNo.equals("TianYe") && gwf.getNodeName().contains("反馈") == true)
					{
						nd = new Node(gwf.getFK_Node());
						if (nd.getIsEndNode() == true)
						{
							//让流程自动结束.
							BP.WF.Dev2Interface.Flow_DoFlowOver(gwf.getFK_Flow(), workid, "打印并自动结束", 0);
						}
					}
				}

				//替换模版尾部的打印说明信息.
				String pathInfo = SystemConfig.PathOfDataUser + "InstancePacketOfData\\Template\\EndInfo\\" + flowNo + ".txt";
				if ((new File(pathInfo)).isFile() == false)
				{
					pathInfo = SystemConfig.PathOfDataUser + "InstancePacketOfData\\Template\\EndInfo\\Default.txt";
				}

				docs = docs.replace("@EndInfo", DataType.ReadTextFile(pathInfo));
			}

			//indexFile = SystemConfig.getPathOfDataUser() + "\\InstancePacketOfData\\" + frmID + "\\" + workid + "\\index.htm";
			BP.DA.DataType.WriteFile(indexFile, docs);

			return indexFile;
		}
		catch (RuntimeException ex)
		{
			return "err@报表生成错误:" + ex.getMessage();
		}
	}

	public static void Html2Pdf(String pdfFileExe, String htmFile, String pdf)
	{
		BP.DA.Log.DebugWriteInfo("@开始生成PDF" + pdfFileExe + "@pdf=" + pdf + "@htmFile=" + htmFile);
		try
		{
			//横向打印.
			// wkhtmltopdf.exe --orientation Landscape  http://baidu.com afqc.pdf  .

			String fileNameWithOutExtention = UUID.NewGuid().toString();
			//Process p = System.Diagnostics.Process.Start(pdfFileExe, " --disable-external-links " + htmFile + " " + pdf);

			Process process = new Process();
			ProcessStartInfo startInfo = new ProcessStartInfo();
			startInfo.FileName = pdfFileExe; //设定需要执行的命令
			startInfo.Arguments = " --disable-external-links " + htmFile + " " + pdf; //"/C"表示执行完命令后马上退出
			startInfo.UseShellExecute = false; //不使用系统外壳程序启动
			startInfo.RedirectStandardInput = false; //不重定向输入
			startInfo.RedirectStandardOutput = true; //重定向输出
			startInfo.CreateNoWindow = true; //不创建窗口
			startInfo.WindowStyle = ProcessWindowStyle.Hidden;
			Process p = Process.Start(startInfo);
			p.WaitForExit();
			p.Close();
		}
		catch (RuntimeException ex)
		{
			//BP.DA.Log.DebugWriteError("@生成PDF错误" + ex.Message + "@pdf=" + pdf + "@htmFile="+htmFile);
			throw ex;
		}
	}
	/** 
	 签名
	 
	 @param userNo
	 @return 
	*/
	private static String SignPic(String userNo)
	{

		if (tangible.StringHelper.isNullOrWhiteSpace(userNo))
		{
			return "";
		}
		//如果文件存在
		String path = SystemConfig.PathOfDataUser + "Siganture/" + userNo + ".jpg";

		if ((new File(path)).isFile() == false)
		{
			path = SystemConfig.PathOfDataUser + "Siganture/" + userNo + ".JPG";
			if ((new File(path)).isFile() == true)
			{
				return "<img src='" + path + "' style='border:0px;width:100px;height:30px;'/>";
			}
			else
			{
				Emp emp = new Emp(userNo);
				return emp.Name;
			}
		}
		else
		{
			return "<img src='" + path + "' style='border:0px;width:100px;height:30px;'/>";
		}

	}

}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#endregion