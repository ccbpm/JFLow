package BP.Pub;

import BP.En.*;
import BP.DA.*;
import BP.Port.*;
import BP.Sys.*;
import BP.Web.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.math.*;

/** 
 WebRtfReport 的摘要说明。
*/
public class RTFEngine
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 数据实体
	private Entities _HisEns = null;
	public final Entities getHisEns()
	{
		if (_HisEns == null)
		{
			_HisEns = new Emps();
		}

		return _HisEns;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 数据实体

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 数据明细实体
	private System.Text.Encoding _encoder = System.Text.Encoding.GetEncoding("GB2312");

	public final String GetCode(String str)
	{
		if (str == null || str.equals(""))
		{
			return "";
		}

		String rtn = "";
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte[] rr = _encoder.GetBytes(str);
		byte[] rr = _encoder.GetBytes(str);
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: foreach (byte b in rr)
		for (byte b : rr)
		{
			if (b > 122)
			{
				rtn += "\\'" + String.format("%x", b);
			}
			else
			{
				rtn += (char)b;
			}
		}
		return rtn.replace("\n", " \\par ");
	}
	//明细表数据
	private ArrayList _EnsDataDtls = null;
	public final ArrayList getEnsDataDtls()
	{
		if (_EnsDataDtls == null)
		{
			_EnsDataDtls = new ArrayList();
		}
		return _EnsDataDtls;
	}
	//多附件数据
	private Hashtable _EnsDataAths = null;
	public final Hashtable getEnsDataAths()
	{
		if (_EnsDataAths == null)
		{
			_EnsDataAths = new Hashtable();
		}
		return _EnsDataAths;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 数据明细实体

	/** 
	 增加一个数据实体
	 
	 @param en
	*/
	public final void AddEn(Entity en)
	{
		this.getHisEns().AddEntity(en);
	}
	/** 
	 增加一个Ens
	 
	 @param ens
	*/
	public final void AddDtlEns(Entities dtlEns)
	{
		this.getEnsDataDtls().add(dtlEns);
	}
	public String CyclostyleFilePath = "";
	public String TempFilePath = "";

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 获取特殊要处理的流程节点信息.
	public final String GetValueByKeyOfCheckNode(String[] strs)
	{
		for (Entity en : this.getHisEns())
		{
			String val = en.GetValStringByKey(strs[2]);
			switch (strs.length)
			{
				case 1:
				case 2:
					throw new RuntimeException("step1参数设置错误" + strs.toString());
				case 3: // S.9001002.Rec
					return val;
				case 4: // S.9001002.RDT.Year
					switch (strs[3])
					{
						case "Text":
							if (val.equals("0"))
							{
								return "否";
							}
							else
							{
								return "是";
							}
						case "YesNo":
							if (val.equals("1"))
							{
								return "[√]";
							}
							else
							{
								return "[×]";
							}
						case "Year":
							return val.substring(0, 4);
						case "Month":
							return val.substring(5, 7);
						case "Day":
							return val.substring(8, 10);
						case "NYR":
							return DA.DataType.ParseSysDate2DateTime(val).toString("yyyy年MM月dd日");
						case "RMB":
							return Float.parseFloat(val).toString("0.00");
						case "RMBDX":
							return DA.DataType.ParseFloatToCash(Float.parseFloat(val));
						default:
							throw new RuntimeException("step2参数设置错误" + strs);
					}
				default:
					throw new RuntimeException("step3参数设置错误" + strs);
			}
		}
		throw new RuntimeException("step4参数设置错误" + strs);
	}
	public static String GetImgHexString(System.Drawing.Image img, System.Drawing.Imaging.ImageFormat ext)
	{
		StringBuilder imgs = new StringBuilder();
//C# TO JAVA CONVERTER TODO TASK: C# to Java Converter cannot determine whether this System.IO.MemoryStream is input or output:
		MemoryStream stream = new MemoryStream();
		img.Save(stream, ext);
		stream.Close();

//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte[] buffer = stream.ToArray();
		byte[] buffer = stream.ToArray();

		for (int i = 0; i < buffer.length; i++)
		{
			if ((i % 32) == 0)
			{
				imgs.append("\r\n");
			}
			//else if ((i % 8) == 0)
			//{
			//    imgs.Append(" ");
			//}
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte num2 = buffer[i];
			byte num2 = buffer[i];
//C# TO JAVA CONVERTER WARNING: The right shift operator was not replaced by Java's logical right shift operator since the left operand was not confirmed to be of an unsigned type, but you should review whether the logical right shift operator (>>>) is more appropriate:
			int num3 = (num2 & 240) >> 4;
			int num4 = num2 & 15;
			imgs.append("0123456789abcdef".charAt(num3));
			imgs.append("0123456789abcdef".charAt(num4));
		}
		return imgs.toString();
	}
	public Entity HisGEEntity = null;
	/** 
	 获取ICON图片的数据。
	 
	 @param key
	 @return 
	*/
	public final String GetValueImgStrs(String key)
	{
		key = key.replace(" ", "");
		key = key.replace("\r\n", "");

		/*说明是图片文件.*/
		String path = key.replace("OID.Img@AppPath", SystemConfig.getPathOfWebApp());

		//定义rtf中图片字符串
		StringBuilder pict = new StringBuilder();
		//获取要插入的图片
		System.Drawing.Image img = System.Drawing.Image.FromFile(path);

		//将要插入的图片转换为16进制字符串
		String imgHexString;
		key = key.toLowerCase();

		if (key.contains(".png"))
		{
			imgHexString = GetImgHexString(img, System.Drawing.Imaging.ImageFormat.Png);
		}
		else if (key.contains(".jp"))
		{
			imgHexString = GetImgHexString(img, System.Drawing.Imaging.ImageFormat.Jpeg);
		}
		else if (key.contains(".gif"))
		{
			imgHexString = GetImgHexString(img, System.Drawing.Imaging.ImageFormat.Gif);
		}
		else if (key.contains(".ico"))
		{
			imgHexString = GetImgHexString(img, System.Drawing.Imaging.ImageFormat.Icon);
		}
		else
		{
			imgHexString = GetImgHexString(img, System.Drawing.Imaging.ImageFormat.Jpeg);
		}

		//生成rtf中图片字符串
		pict.append("\r\n");
		pict.append("{\\pict");
		pict.append("\\jpegblip");
		pict.append("\\picscalex100");
		pict.append("\\picscaley100");
		pict.append("\\picwgoal" + img.Size.Width * 15);
		pict.append("\\pichgoal" + img.Size.Height * 15);
		pict.append(imgHexString + "}");
		pict.append("\r\n");
		return pict.toString();
	}
	/** 
	 获取ICON图片的数据。
	 
	 @param key
	 @return 
	*/
	public final String GetValueImgStrsOfQR(String billUrl)
	{
		/*说明是图片文件.*/
		String path = SystemConfig.getPathOfTemp() + UUID.NewGuid() + ".png"; // key.Replace("OID.Img@AppPath", SystemConfig.PathOfWebApp);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 生成二维码.
		ThoughtWorks.QRCode.Codec.QRCodeEncoder qrc = new ThoughtWorks.QRCode.Codec.QRCodeEncoder();
		qrc.QRCodeEncodeMode = ThoughtWorks.QRCode.Codec.QRCodeEncoder.ENCODE_MODE.BYTE;
		qrc.QRCodeScale = 4;
		qrc.QRCodeVersion = 7;
		qrc.QRCodeErrorCorrect = ThoughtWorks.QRCode.Codec.QRCodeEncoder.ERROR_CORRECTION.M;
		System.Drawing.Bitmap btm = qrc.Encode(billUrl, System.Text.Encoding.UTF8);
		btm.Save(path);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

		//定义rtf中图片字符串
		StringBuilder pict = new StringBuilder();
		//获取要插入的图片
		System.Drawing.Image img = System.Drawing.Image.FromFile(path);

		//将要插入的图片转换为16进制字符串.
		String imgHexString;
		imgHexString = GetImgHexString(img, System.Drawing.Imaging.ImageFormat.Png);

		//生成rtf中图片字符串
		pict.append("\r\n");
		pict.append("{\\pict");
		pict.append("\\jpegblip");
		pict.append("\\picscalex100");
		pict.append("\\picscaley100");
		pict.append("\\picwgoal" + img.Size.Width * 15);
		pict.append("\\pichgoal" + img.Size.Height * 15);
		pict.append(imgHexString + "}");
		pict.append("\r\n");
		return pict.toString();
	}
	/** 
	 获取M2M数据并输出
	 
	 @param key
	 @return 
	*/
	public final String GetValueM2MStrs(String key)
	{
		return "";

		//string[] strs = key.Split('.');
		//string sql = "SELECT ValsName FROM SYS_M2M WHERE FK_MapData='" + strs[0] + "' AND M2MNo='" + strs[2] + "' AND EnOID='" + this.HisGEEntity.PKVal + "'";
		//string vals = DBAccess.RunSQLReturnStringIsNull(sql, null);
		//if (vals == null)
		//    return "无数据";

		//vals = vals.Replace("@", "  ");
		//vals = vals.Replace("<font color=green>", "");
		//vals = vals.Replace("</font>", "");
		//return vals;

		//string val = "";
		//string[] objs = vals.Split('@');
		//foreach (string obj in objs)
		//{
		//    string[] noName = obj.Split(',');
		//    val += noName[1];
		//}
		//return val;
	}
	/** 
	 获取写字版的数据
	 
	 @param key
	 @return 
	*/
	public final String GetValueBPPaintStrs(String key)
	{
		key = key.replace(" ", "");
		key = key.replace("\r\n", "");

		String[] strs = key.split("[.]", -1);
		String filePath = "";
		try
		{
			filePath = DBAccess.RunSQLReturnString("SELECT Tag2 From Sys_FrmEleDB WHERE RefPKVal=" + this.HisGEEntity.getPKVal() + " AND EleID='" + strs[2].trim() + "'");
			if (filePath == null)
			{
				return "";
			}
		}
		catch (java.lang.Exception e)
		{
			return "";
		}

		//定义rtf中图片字符串
		StringBuilder pict = new StringBuilder();
		//获取要插入的图片
		System.Drawing.Image img = System.Drawing.Image.FromFile(filePath);

		//将要插入的图片转换为16进制字符串
		String imgHexString;
		filePath = filePath.toLowerCase();

		if (filePath.contains(".png"))
		{
			imgHexString = GetImgHexString(img, System.Drawing.Imaging.ImageFormat.Png);
		}
		else if (filePath.contains(".jp"))
		{
			imgHexString = GetImgHexString(img, System.Drawing.Imaging.ImageFormat.Jpeg);
		}
		else if (filePath.contains(".gif"))
		{
			imgHexString = GetImgHexString(img, System.Drawing.Imaging.ImageFormat.Gif);
		}
		else if (filePath.contains(".ico"))
		{
			imgHexString = GetImgHexString(img, System.Drawing.Imaging.ImageFormat.Icon);
		}
		else
		{
			imgHexString = GetImgHexString(img, System.Drawing.Imaging.ImageFormat.Jpeg);
		}

		//生成rtf中图片字符串
		pict.append("\r\n");
		pict.append("{\\pict");
		pict.append("\\jpegblip");
		pict.append("\\picscalex100");
		pict.append("\\picscaley100");
		pict.append("\\picwgoal" + img.Size.Width * 15);
		pict.append("\\pichgoal" + img.Size.Height * 15);
		pict.append(imgHexString + "}");
		pict.append("\r\n");
		return pict.toString();
	}
	/** 
	 获取类名+@+字段格式的数据.
	 比如：
	 Demo_Inc@ABC
	 Emp@Name
	 
	 @param key
	 @return 
	*/
	public final String GetValueByAtKey(String key)
	{
		for (Entity en : this.getHisEns())
		{
			String enKey = en.toString();

			//有可能是 BP.Port.Emp
			if (enKey.contains("."))
			{
				enKey = en.getClass().getSimpleName();
			}

			//如果不包含.
			if (key.contains(enKey + "@") == false)
			{
				continue;
			}

			// 如果不包含 . 就说明，不需要转意。
			if (key.contains(".") == false)
			{
				return en.GetValStringByKey(key.substring(key.indexOf('@') + 1));
			}

			//把实体名去掉
			key = key.replace(enKey + "@", "");
			//把数据破开.
			String[] strs = key.split("[.]", -1);
			if (strs.length == 2)
			{
				if (strs[1].trim().equals("ImgAth"))
				{
					String path1 = BP.Sys.SystemConfig.getPathOfDataUser() + "\\ImgAth\\Data\\" + strs[0].trim() + "_" + en.getPKVal() + ".png";
					//定义rtf中图片字符串.
					StringBuilder mypict = new StringBuilder();
					//获取要插入的图片
					System.Drawing.Image imgAth = System.Drawing.Image.FromFile(path1);

					//将要插入的图片转换为16进制字符串
					String imgHexStringImgAth = GetImgHexString(imgAth, System.Drawing.Imaging.ImageFormat.Jpeg);
					//生成rtf中图片字符串
					mypict.append("\r\n");
					mypict.append("{\\pict");
					mypict.append("\\jpegblip");
					mypict.append("\\picscalex100");
					mypict.append("\\picscaley100");
					mypict.append("\\picwgoal" + imgAth.Size.Width * 15);
					mypict.append("\\pichgoal" + imgAth.Size.Height * 15);
					mypict.append(imgHexStringImgAth + "}");
					mypict.append("\r\n");
					return mypict.toString();
				}

				String val = en.GetValStringByKey(strs[0].trim());
				switch (strs[1].trim())
				{
					case "Text":
						if (val.equals("0"))
						{
							return "否";
						}
						else
						{
							return "是";
						}
					case "Year":
						return val.substring(0, 4);
					case "Month":
						return val.substring(5, 7);
					case "Day":
						return val.substring(8, 10);
					case "NYR":
						return DA.DataType.ParseSysDate2DateTime(val).toString("yyyy年MM月dd日");
					case "RMB":
						return Float.parseFloat(val).toString("0.00");
					case "RMBDX":
						return DA.DataType.ParseFloatToCash(Float.parseFloat(val));
					case "ImgAth":
						String path1 = BP.Sys.SystemConfig.getPathOfDataUser() + "\\ImgAth\\Data\\" + strs[0].trim() + "_" + this.HisGEEntity.getPKVal() + ".png";

						//定义rtf中图片字符串.
						StringBuilder mypict = new StringBuilder();
						//获取要插入的图片
						System.Drawing.Image imgAth = System.Drawing.Image.FromFile(path1);

						//将要插入的图片转换为16进制字符串
						String imgHexStringImgAth = GetImgHexString(imgAth, System.Drawing.Imaging.ImageFormat.Jpeg);
						//生成rtf中图片字符串
						mypict.append("\r\n");
						mypict.append("{\\pict");
						mypict.append("\\jpegblip");
						mypict.append("\\picscalex100");
						mypict.append("\\picscaley100");
						mypict.append("\\picwgoal" + imgAth.Size.Width * 15);
						mypict.append("\\pichgoal" + imgAth.Size.Height * 15);
						mypict.append(imgHexStringImgAth + "}");
						mypict.append("\r\n");
						return mypict.toString();
					case "Siganture":
						String path = BP.Sys.SystemConfig.getPathOfDataUser() + "\\Siganture\\" + val + ".jpg";
						//定义rtf中图片字符串.
						StringBuilder pict = new StringBuilder();
						//获取要插入的图片
						System.Drawing.Image img = System.Drawing.Image.FromFile(path);

						//将要插入的图片转换为16进制字符串
						String imgHexString = GetImgHexString(img, System.Drawing.Imaging.ImageFormat.Jpeg);
						//生成rtf中图片字符串
						pict.append("\r\n");
						pict.append("{\\pict");
						pict.append("\\jpegblip");
						pict.append("\\picscalex100");
						pict.append("\\picscaley100");
						pict.append("\\picwgoal" + img.Size.Width * 15);
						pict.append("\\pichgoal" + img.Size.Height * 15);
						pict.append(imgHexString + "}");
						pict.append("\r\n");
						return pict.toString();
					//替换rtf模板文件中的签名图片标识为图片字符串
					// str = str.Replace(imgMark, pict.ToString());
					default:
						throw new RuntimeException("参数设置错误，特殊方式取值错误：" + key);
				}
			}
		} // 实体循环。

		throw new RuntimeException("参数设置错误 GetValueByKey ：" + key);
	}
	/** 
	 获得所所有的审核人员信息.
	 
	 @return 
	*/
	public final String GetValueCheckWorks()
	{
		String html = "";

		//获得当前待办的人员,把当前审批的人员排除在外,不然就有默认同意的意见可以打印出来.
	   String sql = "SELECT FK_Emp, FK_Node FROM WF_GenerWorkerList WHERE IsPass!=1 AND WorkID=" + this.HisGEEntity.getPKVal();
		DataTable dtOfTodo = DBAccess.RunSQLReturnTable(sql);

		for (DataRow dr : dtTrack.Rows)
		{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 排除正在审批的人员.
			String nodeID = dr.get("NDFrom").toString();
			String empFrom = dr.get("EmpFrom").toString();
			if (dtOfTodo.Rows.Count != 0)
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

		return html;
	}
	/** 
	 获得审核组件的信息.
	 
	 @param key
	 @return 
	*/
	public final String GetValueCheckWorkByKey(String key)
	{
		key = key.replace(" ", "");
		key = key.replace("\r\n", "");

		String[] strs = key.split("[.]", -1);
		if (strs.length == 3)
		{
			/*
			 *  是一个节点一个审核人的模式. <WorkCheck.RDT.101>
			 */
			if (dtTrack == null)
			{
				throw new RuntimeException("@您设置了获取审核组件里的规则，但是你没有给审核组件数据源dtTrack赋值。");
			}

			String nodeID = strs[2];
			for (DataRow dr : dtTrack.Rows)
			{
				if (!dr.get("NDFrom").toString().equals(nodeID))
				{
					continue;
				}

				switch (strs[1])
				{
					case "RDT":
						return dr.get("RDT").toString(); //审核日期.
					case "RDT-NYR":
						String rdt = dr.get("RDT").toString(); //审核日期.
						return BP.DA.DataType.ParseSysDate2DateTimeFriendly(rdt);
					case "Rec":
						return dr.get("EmpFrom").toString(); //记录人.
					case "RecName":
						String recName = dr.get("EmpFromT").toString(); //审核人.
						recName = this.GetCode(recName);
						return recName;
					case "Msg":
					case "Note":
						String text = dr.get("Msg").toString();
						text = text.replace("\\", "\\\\");
						text = this.GetCode(text);
						//return Encoding.GetEncoding("GB2312").GetString(Encoding.UTF8.GetBytes(dr["Msg"].ToString()));
						return text;

					//return System.Text.Encoder  //审核信息.
					default:
						break;
				}
			}
		}

		return "无";
	}

	private String GetValueCheckWorkByKey(DataRow row, String key)
	{
		key = key.replace(" ", "");
		key = key.replace("\r\n", "");

		switch (key)
		{
			case "RDT":
				return row.get("RDT").toString(); //审核日期.
			case "RDT-NYR":
				String rdt = row.get("RDT").toString(); //审核日期.
				return BP.DA.DataType.ParseSysDate2DateTimeFriendly(rdt);
			case "Rec":
				return row.get("EmpFrom").toString(); //记录人.
			case "RecName":
				return row.get("EmpFromT").toString(); //审核人.
			case "Msg":
			case "Note":
				return row.get("Msg").toString();
			default:
				return row.get(key) instanceof String ? (String)row.get(key) : null;
		}
	}
	/** 
	 审核节点的表示方法是 节点ID.Attr.
	 
	 @param key
	 @return 
	*/
	public final String GetValueByKey(String key)
	{
		key = key.replace(" ", "");
		key = key.replace("\r\n", "");

		//获取参数代码.
		if (key.contains("@"))
		{
			return GetValueByAtKey(key);
		}

		String[] strs = key.split("[.]", -1);

		// 如果不包含 . 就说明他是从Rpt中取数据。
		//if (this.HisGEEntity != null && key.Contains("ND") == false)
		if (this.HisGEEntity != null)
		{
			if (strs.length == 1)
			{
				return this.HisGEEntity.GetValStringByKey(key);
			}

			if (strs[1].trim().equals("Editor"))
			{
				//获取富文本的内容
				String content = this.HisGEEntity.GetValStringByKey(strs[0]);
				content = content.replace("img+", "img ");
				String contentHtml = "<html><head></head><body>" + content + "</body></html>";
				String StrNohtml = System.Text.RegularExpressions.Regex.Replace(contentHtml, "<[^>]+>", "");
				StrNohtml = System.Text.RegularExpressions.Regex.Replace(StrNohtml, "&[^;]+;", "");

				return this.GetCode(StrNohtml);


				String htmlpath = BP.Sys.SystemConfig.getPathOfDataUser() + "Bill\\Temp\\EditorHtm.html";
				if ((new File(htmlpath)).isFile() == false)
				{
					File.Create(htmlpath);
				}
				try (OutputStreamWriter sw = new OutputStreamWriter(htmlpath))
				{
					sw.write(contentHtml);
				}

				//如何写入到word
				String html = Files.readString(htmlpath, java.nio.charset.StandardCharsets.UTF_8);

				//byte[] array = Encoding.ASCII.GetBytes(content);
				//StringBuilder editors = new StringBuilder();
				//for (int i = 0; i < array.Length; i++)
				//{

				//    editors.Append(array[i]);

				//}
				//MemoryStream stream = new MemoryStream(array);             //convert stream 2 string      

				//System.IO.StreamReader readStream = new System.IO.StreamReader(contentHtml, Encoding.UTF8);
				return html;

			}

			if (strs[1].trim().equals("ImgAth"))
			{
				String path1 = BP.Sys.SystemConfig.getPathOfDataUser() + "ImgAth\\Data\\" + strs[0].trim() + "_" + this.HisGEEntity.getPKVal() + ".png";
				if (!(new File(path1)).isFile())
				{
					FrmImgAthDB dbImgAth = new FrmImgAthDB();
					dbImgAth.setMyPK(strs[0].trim() + "_" + this.HisGEEntity.getPKVal());
					int count = dbImgAth.RetrieveFromDBSources();
					if (count == 1)
					{
						path1 = BP.Sys.SystemConfig.getPathOfDataUser() + "ImgAth\\Data\\" + dbImgAth.getFileName() + ".png";
						if (!(new File(path1)).isFile())
						{
							return this.GetCode(key);
						}
					}
					return "";
				}
				//定义rtf中图片字符串.
				StringBuilder mypict = new StringBuilder();
				//获取要插入的图片
				System.Drawing.Image imgAth = System.Drawing.Image.FromFile(path1);
				//图片附件描述属性
				FrmImgAth frmImgAth = new FrmImgAth();
				frmImgAth.RetrieveByAttr(FrmImgAthAttr.MyPK, strs[0].trim());
				//图片高宽
				float iWidth = imgAth.Size.Width * 15;
				float iHeight = imgAth.Size.Height * 15;
				if (frmImgAth != null && !DataType.IsNullOrEmpty(frmImgAth.getFK_MapData()))
				{
					iWidth = frmImgAth.getW() * 15;
					iHeight = frmImgAth.getH() * 15;
				}

				//将要插入的图片转换为16进制字符串
				String imgHexStringImgAth = GetImgHexString(imgAth, System.Drawing.Imaging.ImageFormat.Jpeg);
				//生成rtf中图片字符串
				mypict.append("\r\n");
				mypict.append("{\\pict");
				mypict.append("\\jpegblip");
				mypict.append("\\picscalex100");
				mypict.append("\\picscaley100");
				mypict.append("\\picwgoal" + iWidth);
				mypict.append("\\pichgoal" + iHeight);
				mypict.append(imgHexStringImgAth + "}");
				mypict.append("\r\n");
				return mypict.toString();
			}

			if (strs[1].trim().equals("BPPaint"))
			{
				String path1 = DBAccess.RunSQLReturnString("SELECT  Tag2 FROM Sys_FrmEleDB WHERE REFPKVAL=" + this.HisGEEntity.getPKVal() + " AND EleID='" + strs[0].trim() + "'");
				//  string path1 = BP.Sys.SystemConfig.PathOfDataUser + "\\BPPaint\\" + this.HisGEEntity.ToString().Trim() + "\\" + this.HisGEEntity.PKVal + ".png";
				//定义rtf中图片字符串.
				StringBuilder mypict = new StringBuilder();
				//获取要插入的图片
				System.Drawing.Image myBPPaint = System.Drawing.Image.FromFile(path1);

				//将要插入的图片转换为16进制字符串
				String imgHexStringImgAth = GetImgHexString(myBPPaint, System.Drawing.Imaging.ImageFormat.Jpeg);
				//生成rtf中图片字符串
				mypict.append("\r\n");
				mypict.append("{\\pict");
				mypict.append("\\jpegblip");
				mypict.append("\\picscalex100");
				mypict.append("\\picscaley100");
				mypict.append("\\picwgoal" + myBPPaint.Size.Width * 15);
				mypict.append("\\pichgoal" + myBPPaint.Size.Height * 15);
				mypict.append(imgHexStringImgAth + "}");
				mypict.append("\r\n");
				return mypict.toString();
			}

			//根据枚举值返回选中符号
			if (strs[1].contains("-EnumYes") == true)
			{
				String relVal = this.HisGEEntity.GetValStringByKey(strs[0]);
				String[] checkVal = strs[1].split("[-]", -1);
				if (checkVal.length == 1)
				{
					return relVal;
				}
				if (relVal.equals(checkVal[0]))
				{
					return "[√]";
				}
				else
				{
					return "[×]";
				}
			}

			if (strs.length == 2)
			{
				String val = this.HisGEEntity.GetValStringByKey(strs[0].trim());
				switch (strs[1].trim())
				{

					case "Text":
						if (val.equals("0"))
						{
							return "否";
						}
						else
						{
							return "是";
						}
					case "Year":
						return val.substring(0, 4);
					case "Month":
						return val.substring(5, 7);
					case "Day":
						return val.substring(8, 10);
					case "NYR":
						return DA.DataType.ParseSysDate2DateTime(val).toString("yyyy年MM月dd日");
					case "RMB":
						BigDecimal md = Double.isNaN((BigDecimal.Parse(val))) ? Double.NaN : Math.round((BigDecimal.Parse(val)).multiply(Math.pow(10, 2))) / Math.pow(10, 2);
						return md.toString();
					case "RMBDX":
						return this.GetCode(DA.DataType.ParseFloatToCash(Float.parseFloat(val)));
					case "Siganture":
						String path = BP.Sys.SystemConfig.getPathOfDataUser() + "Siganture\\" + val + ".jpg";
						//获取要插入的图片
						if ((new File(path)).isFile() == true)
						{
							//定义rtf中图片字符串
							StringBuilder pict = new StringBuilder();
							System.Drawing.Image img = System.Drawing.Image.FromFile(path);

							//将要插入的图片转换为16进制字符串
							String imgHexString = GetImgHexString(img, System.Drawing.Imaging.ImageFormat.Jpeg);
							//生成rtf中图片字符串
							pict.append("\r\n");
							pict.append("{\\pict");
							pict.append("\\jpegblip");
							pict.append("\\picscalex100");
							pict.append("\\picscaley100");
							pict.append("\\picwgoal" + img.Size.Width * 15);
							pict.append("\\pichgoal" + img.Size.Height * 15);
							pict.append(imgHexString + "}");
							pict.append("\r\n");
							return pict.toString();
						}
						//图片不存在显示中文名，否则显示原值
						String empName = DBAccess.RunSQLReturnStringIsNull("SELECT Name FROM Port_Emp WHERE No='" + val + "'", val);
						return this.GetCode(empName);
					//替换rtf模板文件中的签名图片标识为图片字符串
					// str = str.Replace(imgMark, pict.ToString());
					case "BoolenText":
						if (val.equals("0"))
						{
							return "否";
						}
						else
						{
							return "是";
						}
					case "Boolen":
						if (val.equals("1"))
						{
							return "[√]";
						}
						else
						{
							return "[×]";
						}
						break;
					case "YesNo":
						if (val.equals("1"))
						{
							return "[√]";
						}
						else
						{
							return "[×]";
						}
						break;
					case "Yes":
						if (val.equals("1"))
						{
							return "[√]";
						}
						else
						{
							return "[×]";
						}
					case "No":
						if (val.equals("0"))
						{
							return "[√]";
						}
						else
						{
							return "[×]";
						}
					default:
						throw new RuntimeException("参数设置错误，特殊方式取值错误：" + key);
				}
			}
			else
			{
				throw new RuntimeException("参数设置错误，特殊方式取值错误：" + key);
			}
		}

		for (Entity en : this.getHisEns())
		{
			String enKey = en.toString();
			if (enKey.contains("."))
			{
				enKey = en.getClass().getSimpleName();
			}
			if (key.contains(en.toString() + ".") == false)
			{
				continue;
			}

			/*说明就在这个字段内*/
			if (strs.length == 1)
			{
				throw new RuntimeException("参数设置错误，strs.length=1 。" + key);
			}

			if (strs.length == 2)
			{
				return en.GetValStringByKey(strs[1].trim());
			}

			if (strs.length == 3)
			{
				if (strs[2].trim().equals("ImgAth"))
				{
					String path1 = SystemConfig.getPathOfDataUser() + "\\ImgAth\\Data\\" + strs[1].trim() + "_" + en.getPKVal() + ".png";
					//定义rtf中图片字符串.
					StringBuilder mypict = new StringBuilder();
					//获取要插入的图片
					System.Drawing.Image imgAth = System.Drawing.Image.FromFile(path1);

					//将要插入的图片转换为16进制字符串
					String imgHexStringImgAth = GetImgHexString(imgAth, System.Drawing.Imaging.ImageFormat.Jpeg);
					//生成rtf中图片字符串
					mypict.append("\r\n");
					mypict.append("{\\pict");
					mypict.append("\\jpegblip");
					mypict.append("\\picscalex100");
					mypict.append("\\picscaley100");
					mypict.append("\\picwgoal" + imgAth.Size.Width * 15);
					mypict.append("\\pichgoal" + imgAth.Size.Height * 15);
					mypict.append(imgHexStringImgAth + "}");
					mypict.append("\r\n");
					return mypict.toString();
				}


				String val = en.GetValStringByKey(strs[1].trim());
				switch (strs[2].trim())
				{
					case "Text":
						if (val.equals("0"))
						{
							return "否";
						}
						else
						{
							return "是";
						}
					case "Year":
						return val.substring(0, 4);
					case "Month":
						return val.substring(5, 7);
					case "Day":
						return val.substring(8, 10);
					case "NYR":
						return DA.DataType.ParseSysDate2DateTime(val).toString("yyyy年MM月dd日");
					case "RMB":
						return Float.parseFloat(val).toString("0.00");
					case "RMBDX":
						return DA.DataType.ParseFloatToCash(Float.parseFloat(val));
					case "ImgAth":
						String path1 = SystemConfig.getPathOfDataUser() + "\\ImgAth\\Data\\" + strs[0].trim() + "_" + this.HisGEEntity.getPKVal() + ".png";

						//定义rtf中图片字符串.
						StringBuilder mypict = new StringBuilder();
						//获取要插入的图片
						System.Drawing.Image imgAth = System.Drawing.Image.FromFile(path1);

						//将要插入的图片转换为16进制字符串
						String imgHexStringImgAth = GetImgHexString(imgAth, System.Drawing.Imaging.ImageFormat.Jpeg);
						//生成rtf中图片字符串
						mypict.append("\r\n");
						mypict.append("{\\pict");
						mypict.append("\\jpegblip");
						mypict.append("\\picscalex100");
						mypict.append("\\picscaley100");
						mypict.append("\\picwgoal" + imgAth.Size.Width * 15);
						mypict.append("\\pichgoal" + imgAth.Size.Height * 15);
						mypict.append(imgHexStringImgAth + "}");
						mypict.append("\r\n");
						return mypict.toString();
					case "Siganture":
						String path = SystemConfig.getPathOfDataUser() + "\\Siganture\\" + val + ".jpg";
						//定义rtf中图片字符串.
						StringBuilder pict = new StringBuilder();
						//获取要插入的图片
						System.Drawing.Image img = System.Drawing.Image.FromFile(path);

						//将要插入的图片转换为16进制字符串
						String imgHexString = GetImgHexString(img, System.Drawing.Imaging.ImageFormat.Jpeg);
						//生成rtf中图片字符串
						pict.append("\r\n");
						pict.append("{\\pict");
						pict.append("\\jpegblip");
						pict.append("\\picscalex100");
						pict.append("\\picscaley100");
						pict.append("\\picwgoal" + img.Size.Width * 15);
						pict.append("\\pichgoal" + img.Size.Height * 15);
						pict.append(imgHexString + "}");
						pict.append("\r\n");
						return pict.toString();
					//替换rtf模板文件中的签名图片标识为图片字符串
					// str = str.Replace(imgMark, pict.ToString());
					default:
						throw new RuntimeException("参数设置错误，特殊方式取值错误：" + key);
				}
			}
		}

		throw new RuntimeException("参数设置错误 GetValueByKey ：" + key);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 生成单据
	/** 
	 生成单据
	 
	 @param cfile 模板文件
	*/
	public final void MakeDoc(String cfile)
	{
		String file = PubClass.GenerTempFileName("doc");
		this.MakeDoc(cfile, SystemConfig.getPathOfTemp(), file, true);
	}
	public String ensStrs = "";
	/** 
	 轨迹表（用于输出打印审核轨迹,审核信息.）
	*/
	public DataTable dtTrack = null;
	/** 
	 单据生成 
	 
	 @param cfile 模板文件
	 @param path 生成路径
	 @param file 生成文件
	 @param isOpen 是否用IE打开？
	 @param isOpen 要打开的url用于生成二维码
	*/

	public final void MakeDoc(String templateRtfFile, String path, String file, boolean isOpen)
	{
		MakeDoc(templateRtfFile, path, file, isOpen, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public void MakeDoc(string templateRtfFile, string path, string file,bool isOpen, string billUrl=null)
	public final void MakeDoc(String templateRtfFile, String path, String file, boolean isOpen, String billUrl)
	{
		templateRtfFile = templateRtfFile.replace(".rtf.rtf", ".rtf");

		if ((new File(path)).isDirectory() == false)
		{
			(new File(path)).mkdirs();
		}

		String str = Cash.GetBillStr(templateRtfFile, false).substring(0);
		if (this.getHisEns().Count == 0)
		{
			if (this.HisGEEntity == null)
			{
				throw new RuntimeException("@您没有为报表设置数据源...");
			}
		}

		this.ensStrs = "";
		if (this.getHisEns().Count != 0)
		{
			for (Entity en : this.getHisEns())
			{
				ensStrs += en.toString();
			}
		}
		else
		{
			ensStrs = this.HisGEEntity.toString();
		}

		String error = "";
		String[] paras = null;
		if (this.HisGEEntity != null)
		{
			paras = Cash.GetBillParas(templateRtfFile, ensStrs, this.HisGEEntity);
		}
		else
		{
			paras = Cash.GetBillParas(templateRtfFile, ensStrs, this.getHisEns());
		}

		this.TempFilePath = path + file;
		try
		{
			String key = "";
			String ss = "";

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 替换主表标记
			for (String para : paras)
			{
				if (para == null || para.equals(""))
				{
					continue;
				}

				try
				{
					if (para.contains("Editor"))
					{
						str = str.replace("<" + para + ">", this.GetValueByKey(para));
					}
					else if (para.contains("ImgAth"))
					{
						str = str.replace("<" + para + ">", this.GetValueByKey(para));
					}
					else if (para.contains("Siganture"))
					{
						str = str.replace("<" + para + ">", this.GetValueByKey(para));
					}
					else if (para.contains("Img@AppPath"))
					{
						str = str.replace("<" + para + ">", this.GetValueImgStrs(para));
					}
					else if (para.contains("Img@QR"))
					{
						str = str.replace("<" + para + ">", this.GetValueImgStrsOfQR(billUrl));
					}
					else if (para.contains(".BPPaint"))
					{
						str = str.replace("<" + para + ">", this.GetValueBPPaintStrs(para));
					}
					else if (para.contains(".M2M"))
					{
						str = str.replace("<" + para + ">", this.GetValueM2MStrs(para));
					}
					else if (para.contains(".RMBDX"))
					{
						str = str.replace("<" + para + ">", this.GetValueByKey(para));
					}
					else if (para.contains(".RMB"))
					{
						str = str.replace("<" + para + ">", this.GetValueByKey(para));
					}
					else if (para.contains(".Boolen"))
					{
						str = str.replace("<" + para + ">", this.GetValueByKey(para));
					}
					else if (para.contains(".BoolenText"))
					{
						str = str.replace("<" + para + ">", this.GetValueByKey(para));
					}
					else if (para.contains(".NYR"))
					{
						str = str.replace("<" + para + ">", this.GetCode(this.GetValueByKey(para)));
					}
					else if (para.contains(".Year"))
					{
						str = str.replace("<" + para + ">", this.GetValueByKey(para));
					}
					else if (para.contains(".Month"))
					{
						str = str.replace("<" + para + ">", this.GetValueByKey(para));
					}
					else if (para.contains(".Day"))
					{
						str = str.replace("<" + para + ">", this.GetValueByKey(para));
					}
					else if (para.contains(".Yes") == true)
					{
						str = str.replace("<" + para + ">", this.GetCode(this.GetValueByKey(para)));
					}
					else if (para.contains("-EnumYes") == true)
					{
						str = str.replace("<" + para + ">", this.GetCode(this.GetValueByKey(para)));
					}
					else if (para.contains("WorkCheck.RDT") || para.contains("WorkCheck.Rec") || para.contains("WorkCheck.RecName") || para.contains("WorkCheck.Note")) // 审核组件的审核日期.
					{
						str = str.replace("<" + para + ">", this.GetValueCheckWorkByKey(para));
					}
					else if (para.contains("WorkChecks") == true) //为烟台增加审核人员的信息,把所有的审核人员信息都输入到这里.
					{
						str = str.replace("<" + para + ">", this.GetValueCheckWorks());
					}
					else if (para.contains(".") == true)
					{
						continue; //有可能是明细表数据.
					}
					else
					{
						String val = this.GetValueByKey(para);
						val = val.replace("\\", "\\\\");
						val = this.GetCode(val);
						str = str.replace("<" + para + ">", val);
					}
				}
				catch (RuntimeException ex)
				{
					error += "@替换主表标记取参数[" + para + "]出现错误：有以下情况导致此错误;1你用Text取值时间，此属性不是外键。2,类无此属性。3,该字段是明细表字段但是丢失了明细表标记.<br>更详细的信息：<br>" + ex.getMessage();
					if (SystemConfig.getIsDebug())
					{
						throw new RuntimeException(error);
					}
					Log.DebugWriteError(error);
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 替换主表标记

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 从表
			String shortName = "";
			ArrayList al = this.getEnsDataDtls();
			for (Entities dtls : al)
			{
				Entity dtl = dtls.getGetNewEntity();
				String dtlEnName = dtl.toString();
				shortName = dtlEnName.substring(dtlEnName.lastIndexOf(".") + 1);

				if (str.indexOf(shortName) == -1)
				{
					continue;
				}

				int pos_rowKey = str.indexOf(shortName);
				int row_start = -1, row_end = -1;
				if (pos_rowKey != -1)
				{
					row_start = str.substring(0, pos_rowKey).lastIndexOf("\\row");

					row_end = str.substring(pos_rowKey).indexOf("\\row");
				}

				if (row_start != -1 && row_end != -1)
				{
					String row = str.substring(row_start, (pos_rowKey) + row_end);
					str = str.replace(row, "");

					Map map = dtls.getGetNewEntity().getEnMap();
					int i = dtls.Count;
					while (i > 0)
					{
						i--;
						Object tempVar = row.Clone();
						String rowData = tempVar instanceof String ? (String)tempVar : null;
						dtl = dtls.get(i);
						//替换序号  
						int rowIdx = i + 1;
						rowData = rowData.replace("<IDX>", String.valueOf(rowIdx));

						for (Attr attr : map.getAttrs())
						{
							switch (attr.getMyDataType())
							{
								case DataType.AppDouble:
								case DataType.AppFloat:
									rowData = rowData.replace("<" + shortName + "." + attr.getKey() + ">", dtl.GetValStringByKey(attr.getKey()));
									break;
								case DataType.AppMoney:
									rowData = rowData.replace("<" + shortName + "." + attr.getKey() + ">", dtl.GetValDecimalByKey(attr.getKey()).toString("0.00"));
									break;
								case DataType.AppInt:

									if (attr.getMyDataType() == DataType.AppBoolean)
									{
										rowData = rowData.replace("<" + shortName + "." + attr.getKey() + ">", dtl.GetValStrByKey(attr.getKey()));
										int v = dtl.GetValIntByKey(attr.getKey());
										if (v == 1)
										{
											rowData = rowData.replace("<" + shortName + "." + attr.getKey() + "Text>", "是");
										}
										else
										{
											rowData = rowData.replace("<" + shortName + "." + attr.getKey() + "Text>", "否");
										}
									}
									else
									{
										if (attr.getIsEnum())
										{
											rowData = rowData.replace("<" + shortName + "." + attr.getKey() + "Text>", GetCode(dtl.GetValRefTextByKey(attr.getKey())));
										}
										else
										{
											rowData = rowData.replace("<" + shortName + "." + attr.getKey() + ">", dtl.GetValStrByKey(attr.getKey()));
										}
									}
									break;
								default:
									rowData = rowData.replace("<" + shortName + "." + attr.getKey() + ">", GetCode(dtl.GetValStrByKey(attr.getKey())));
									break;
							}
						}

						str = str.insert(row_start, rowData);
					}
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 从表

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 明细 合计信息。
			al = this.getEnsDataDtls();
			for (Entities dtls : al)
			{
				Entity dtl = dtls.getGetNewEntity();
				String dtlEnName = dtl.toString();
				shortName = dtlEnName.substring(dtlEnName.lastIndexOf(".") + 1);
				//shortName = dtls.ToString().Substring(dtls.ToString().LastIndexOf(".") + 1);
				Map map = dtl.getEnMap();
				for (Attr attr : map.getAttrs())
				{
					switch (attr.getMyDataType())
					{
						case DataType.AppDouble:
						case DataType.AppFloat:
						case DataType.AppMoney:
							key = "<" + shortName + "." + attr.getKey() + ".SUM>";
							if (str.indexOf(key) != -1)
							{
								str = str.replace(key, String.valueOf(dtls.GetSumFloatByKey(attr.getKey())));
							}

							key = "<" + shortName + "." + attr.getKey() + ".SUM.RMB>";
							if (str.indexOf(key) != -1)
							{
								str = str.replace(key, (new Float(dtls.GetSumFloatByKey(attr.getKey()))).toString("0.00"));
							}

							key = "<" + shortName + "." + attr.getKey() + ".SUM.RMBDX>";
							if (str.indexOf(key) != -1)
							{
								str = str.replace(key, GetCode(DA.DataType.ParseFloatToCash(dtls.GetSumFloatByKey(attr.getKey()))));
							}
							break;
						case DataType.AppInt:
							key = "<" + shortName + "." + attr.getKey() + ".SUM>";
							if (str.indexOf(key) != -1)
							{
								str = str.replace(key, String.valueOf(dtls.GetSumIntByKey(attr.getKey())));
							}
							break;
						default:
							break;
					}
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 从表合计

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 审核组件组合信息，added by liuxc,2016-12-16

			//节点单个审核人
		   if (dtTrack != null && str.contains("<WorkCheckBegin>") == false && str.contains("<WorkCheckEnd>") == false)
		   {
			   for (DataRow row : dtTrack.Rows) //此处的22是ActionType.WorkCheck的值，此枚举位于BP.WF项目中，此处暂写死此值
			   {
					int acType = Integer.parseInt(row.get("ACTIONTYPE").toString());
					if (acType != 22)
					{
						continue;
					}
					str = str.replace("<WorkCheck.Msg." + row.get("NDFrom") + ">", this.GetCode(this.GetValueCheckWorkByKey(row, "Msg")));
					str = str.replace("<WorkCheck.Rec." + row.get("NDFrom") + ">", this.GetCode(this.GetValueCheckWorkByKey(row, "EmpFromT")));
					str = str.replace("<WorkCheck.RDT." + row.get("NDFrom") + ">",this.GetCode(this.GetValueCheckWorkByKey(row, "RDT")));


			   }
		   }

			if (dtTrack != null && str.contains("<WorkCheckBegin>") && str.contains("<WorkCheckEnd>"))
			{
				int beginIdx = str.indexOf("<WorkCheckBegin>"); //len:16
				int endIdx = str.indexOf("<WorkCheckEnd>"); //len:14
				String moduleStr = str.substring(beginIdx + 16, beginIdx + 16 + endIdx - beginIdx - 16);
				ArrayList tags = new ArrayList();
				String val = "";
				String field = "";
				String checkStr = "";
				String[] ps = null;


				for (String para : paras)
				{
					if (tangible.StringHelper.isNullOrWhiteSpace(para) || para.contains("WorkCheckList.") == false)
					{
						continue;
					}

					ps = para.split("[.]", -1);
					tags.add(ps[1]);
				}

				for (DataRow row : dtTrack.Rows) //此处的22是ActionType.WorkCheck的值，此枚举位于BP.WF项目中，此处暂写死此值
				{
					int acType = Integer.parseInt(row.get("ACTIONTYPE").toString());
					if (acType != 22)
					{
						continue;
					}
					checkStr = moduleStr;
					for (String tag : tags)
					{
						checkStr = checkStr.replace("<WorkCheckList." + tag + ">", this.GetCode(this.GetValueCheckWorkByKey(row, tag)));
					}

					str = str.insert(beginIdx, checkStr);
					beginIdx += checkStr.length();
					endIdx += checkStr.length();
				}

				str = str.substring(0, beginIdx) + (endIdx < str.length() - 1 ? str.substring(endIdx + 14) : "");

			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 多附件
			for (String athObjEnsName : this.getEnsDataAths().keySet())
			{
				String athName = "Ath." + athObjEnsName;
				String athFilesName = "";
				if (str.indexOf(athName) == -1)
				{
					continue;
				}

				Object tempVar2 = this.getEnsDataAths().get(athObjEnsName);
				FrmAttachmentDBs athDbs = tempVar2 instanceof FrmAttachmentDBs ? (FrmAttachmentDBs)tempVar2 : null;
				if (athDbs == null)
				{
					continue;
				}
				for (FrmAttachmentDB athDb : athDbs)
				{
					if (athFilesName.length() > 0)
					{
						athFilesName += " ， ";
					}

					athFilesName += athDb.getFileName();
				}
				str = str.replace("<" + athName + ">", this.GetCode(athFilesName));
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 要替换的字段
			//if (replaceVals != null && replaceVals.Contains("@"))
			//{
			//    string[] vals = replaceVals.Split('@');
			//    foreach (string val in vals)
			//    {
			//        if (val == null || val == "")
			//            continue;

			//        if (val.Contains("=") == false)
			//            continue;

			//        string myRep = val.Clone() as string;

			//        myRep = myRep.Trim();
			//        myRep = myRep.Replace("null", "");
			//        string[] myvals = myRep.Split('=');
			//        str = str.Replace("<" + myvals[0] + ">", "<" + myvals[1] + ">");
			//    }
			//}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER WARNING: The java.io.OutputStreamWriter constructor does not accept all the arguments passed to the System.IO.StreamWriter constructor:
//ORIGINAL LINE: StreamWriter wr = new StreamWriter(this.TempFilePath, false, Encoding.ASCII);
			OutputStreamWriter wr = new OutputStreamWriter(this.TempFilePath, java.nio.charset.StandardCharsets.US_ASCII);
			str = str.replace("<", "");
			str = str.replace(">", "");
			wr.write(str);
			wr.close();
		}
		catch (RuntimeException ex)
		{
			String msg = "";
			if (SystemConfig.getIsDebug())
			{ // 异常可能与单据的配置有关系。
				try
				{
					this.CyclostyleFilePath = SystemConfig.getPathOfDataUser() + "\\CyclostyleFile\\" + templateRtfFile;
					str = Cash.GetBillStr(templateRtfFile, false);
					msg = "@已经成功的执行修复线  RepairLineV2，您重新发送一次或者，退后重新在发送一次，是否可以解决此问题";
				}
				catch (RuntimeException ex1)
				{
					msg = "执行修复线失败.  RepairLineV2 " + ex1.getMessage();
				}
			}
			throw new RuntimeException("生成文档失败：单据名称[" + this.CyclostyleFilePath + "] 异常信息：" + ex.getMessage() + " @自动修复单据信息：" + msg);
		}
		if (isOpen)
		{
			PubClass.Print(HttpContextHelper.getRequestApplicationPath() + "Temp/" + file);
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 生成单据
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 生成单据
	/** 
	 生成单据根据
	 
	 @param templeteFile 模板文件
	 @param saveToFile
	 @param mainDT
	 @param dtls
	*/
	public final void MakeDocByDataSet(String templeteFile, String saveToPath, String saveToFileName, DataTable mainDT, DataSet dtlsDS)
	{
		String valMain = DBAccess.RunSQLReturnString("SELECT NO FROM SYS_MapData");
		this.HisGEEntity = new GEEntity(valMain);
		this.HisGEEntity.getRow().LoadDataTable(mainDT, mainDT.Rows[0]);
		this.AddEn(this.HisGEEntity); //增加一个主表。
		if (dtlsDS != null)
		{
			for (DataTable dt : dtlsDS.Tables)
			{
				String dtlID = DBAccess.RunSQLReturnString("SELECT NO FROM SYS_MapDtl ");
				BP.Sys.GEDtls dtls = new BP.Sys.GEDtls(dtlID);
				for (DataRow dr : dt.Rows)
				{
					BP.En.Entity tempVar = dtls.getGetNewEntity();
					BP.Sys.GEDtl dtl = tempVar instanceof BP.Sys.GEDtl ? (BP.Sys.GEDtl)tempVar : null;
					dtl.getRow().LoadDataTable(dt, dr);
					dtls.AddEntity(dtl);
				}
				this.AddDtlEns(dtls); //增加一个明晰。
			}
		}

		this.MakeDoc(templeteFile, saveToPath, saveToFileName, false);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法
	/** 
	 RTFEngine
	*/
	public RTFEngine()
	{
		this._EnsDataDtls = null;
		this._HisEns = null;
	}
	/** 
	 传入的是单个实体
	 
	 @param en
	*/
	public RTFEngine(Entity en)
	{
		this._EnsDataDtls = null;
		this._HisEns = null;
		this.HisGEEntity = en;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}