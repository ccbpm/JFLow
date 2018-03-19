package BP.Web;


/** 
 PageBase 的摘要说明。
*/
public class PageBase 
{
//	/** 
//	 关闭窗口
//	*/
//	protected final void WinCloseWithMsg(String mess)
//	{
//		//this.ResponseWriteRedMsg(mess);
//		//return;
//		mess = mess.replace("'", "＇");
//
//		mess = mess.replace("\"", "＂");
//
//		mess = mess.replace(";", "；");
//		mess = mess.replace(")", "）");
//		mess = mess.replace("(", "（");
//
//		mess = mess.replace(",", "，");
//		mess = mess.replace(":", "：");
//
//
//		mess = mess.replace("<", "［");
//		mess = mess.replace(">", "］");
//
//		mess = mess.replace("[", "［");
//		mess = mess.replace("]", "］");
//
//
//		mess = mess.replace("@", "\\n@");
//
//		mess = mess.replace("\r\n", "");
//
//		this.Response.Write("<script language='JavaScript'>alert('" + mess + "'); window.close()</script>");
//	}
//	public final String getRefEnKey()
//	{
//		String str = this.Request.QueryString["No"];
//		if (str == null)
//		{
//			str = this.Request.QueryString["OID"];
//		}
//
//		if (str == null)
//		{
//			str = this.Request.QueryString["MyPK"];
//		}
//
//		if (str == null)
//		{
//			str = this.Request.QueryString["PK"];
//		}
//
//
//		return str;
//	}
//	public final String getMyPK()
//	{
//		return this.Request.QueryString["MyPK"];
//	}
//	public final int getRefOID()
//	{
//		String s = this.Request.QueryString["RefOID"];
//		if (s == null)
//		{
//			s = this.Request.QueryString["OID"];
//		}
//		if (s == null)
//		{
//			return 0;
//		}
//		return Integer.parseInt(s);
//	}
//	public final String GenerTableStr(DataTable dt)
//	{
//		String str = "<Table id='tb' border=1 >";
//		// 标题
//		str += "<TR>";
//		for (DataColumn dc : dt.Columns)
//		{
//			str += "<TD class='DGCellOfHeader" + BP.Web.WebUser.Style + "' >" + dc.ColumnName + "</TD>";
//		}
//		str += "</TR>";
//
//		//内容
//		for (DataRow dr : dt.Rows)
//		{
//			str += "<TR>";
//
//			for (DataColumn dc : dt.Columns)
//			{
//				str += "<TD >" + dr.getItem(dc.ColumnName) + "</TD>";
//			}
//			str += "</TR>";
//		}
//		str += "</Table>";
//		return str;
//	}
//	public final String GenerTablePage(DataTable dt, String title)
//	{
//		return PubClass.GenerTablePage(dt, title);
//	}
//	public final String GenerLabelStr(String title)
//	{
//		return PubClass.GenerLabelStr(title);
//		//return str;
//	}
//
//	public final Control GenerLabel(String title)
//	{
//		String path = this.Request.ApplicationPath;
//		String str = "";
//		str += "<TABLE style='font-size:14px' cellpadding='0' cellspacing='0' background='" + SystemConfig.getSysNo() + "WF/Img/DG_bgright.gif'>";
//		str += "<TR>";
//		str += "<TD>";
//		str += "<IMG src='" + SystemConfig.getSysNo() + "WF/Img/DG_Title_Left.gif' border='0' width='30' height='25'></TD>";
//
//		str += "<TD  valign=bottom noWrap background='" + SystemConfig.getSysNo() + "WF/Img/DG_Title_BG.gif'   height='25' border=0>&nbsp;";
//		str += " &nbsp;<b>" + title + "</b>&nbsp;&nbsp;";
//		str += "</TD>";
//		str += "<TD >";
//		str += "<IMG src='" + SystemConfig.getSysNo() + "WF/Img/DG_Title_Right.gif' border='0' width='25' height='25'></TD>";
//		str += "</TR>";
//		str += "</TABLE>";
//		return this.ParseControl(str);
//	}
//	public final Control GenerLabel_bak(String title)
//	{
//		// return this.ParseControl(title);
//
//		String path = SystemConfig.getSysNo(); //this.Request.ApplicationPath;
//		String str = "";
//
//		str += "<TABLE style='font-size:14px'  cellpadding='0' cellspacing='0' background='" + path + "/Images/DG_bgright.gif'>";
//		str += "<TBODY>";
//		str += "<TR>";
//		str += "<TD>";
//		str += "<IMG src='" + path + "/Images/DG_Title_Left.gif' border='0' width='30' height='20'></TD>";
//		str += "<TD  class=TD  vAlign='center' noWrap background='" + path + "/Images/DG_Title_BG.gif'>&nbsp;";
//		str += " &nbsp;" + title + "&nbsp;&nbsp;";
//		str += "</TD>";
//		str += "<TD>";
//		str += "<IMG src='" + path + "WF/Img/DG_Title_Right.gif' border='0' width='25' height='20'></TD>";
//		str += "</TR>";
//		str += "</TBODY>";
//		str += "</TABLE>";
//		return this.ParseControl(str);
//		//return str;
//	}
//	public final void GenerLabel(Label lab, Entity en, String msg)
//	{
//		lab.Controls.Clear();
//		lab.Controls.Add(this.GenerLabel("<img src='" + en.EnMap.Icon + "' border=0 />" + msg));
//	}
//	public final void GenerLabel(Label lab, String msg)
//	{
//		lab.Controls.Clear();
//		lab.Controls.Add(this.GenerLabel(msg));
//	}
//	//public void GenerLabel(Label lab, Entity en)
//	//{
//	//    this.GenerLabel(lab, en.EnDesc + en.EnMap.TitleExt);
//	//    return;
//
//	//    lab.Controls.Clear();
//	//    if (en.EnMap.Icon == null)
//	//        lab.Controls.Add(this.GenerLabel(en.EnMap.EnDesc));
//	//    else
//	//        lab.Controls.Add(this.GenerLabel("<img src='" + en.EnMap.Icon + "' border=0 />" + en.EnMap.EnDesc + en.EnMap.TitleExt));
//	//}
//	public final String GenerCaption(String title)
//	{
//		if (BP.Web.WebUser.Style.equals("2"))
//		{
//			return "<div class=Table_Title ><span>" + title + "</span></div>";
//		}
//
//		return "<b>" + title + "</b>";
//	}
//	@Override
//	protected void OnLoad(EventArgs e)
//	{
//		//if (Web.WebUser.getNo() == null)
//		//    this.ToSignInPage();
//		super.OnLoad(e);
//	}
//	/** 
//	 导出到一个excel,文件用于，数据导入。
//	 
//	 @param attr
//	 @param sheetName
//	 @return 
//	*/
//	protected final void ExportEnToExcelModel_OpenWin(Attrs attrs, String sheetName)
//	{
//		String filename = sheetName + ".xls";
//		String file = filename;
//		//SystemConfig.getPathOfTemp()
//		String filepath = SystemConfig.getSysNo() + "\\Temp\\";
//
//
//		///#region 参数及变量设置
//		//如果导出目录没有建立，则建立.
//		if ((new java.io.File(filepath)).isDirectory() == false)
//		{
//			(new java.io.File(filepath)).mkdirs();
//		}
//
//		filename = filepath + filename;
//		FileStream objFileStream = new FileStream(filename, FileMode.OpenOrCreate, FileAccess.Write);
//		StreamWriter objStreamWriter = new StreamWriter(objFileStream, System.Text.Encoding.Unicode);
//
//		///#endregion
//
//
//		///#region 生成导出文件
//		String strLine = "";
//		for (Attr attr : attrs)
//		{
//			strLine += attr.Desc + (char)9;
//		}
//
//		objStreamWriter.WriteLine(strLine);
//		objStreamWriter.Close();
//		objFileStream.Close();
//
//		///#endregion
//
//		//this.WinOpen(Request.ApplicationPath+"/Temp/" + file,"sss", 500,800);
//
//		this.Write_Javascript(" window.open('" + SystemConfig.getSysNo() + "/Temp/" + file + "'); ");
//	}
//
//
//		///#region 用户的访问权限
//	/** 
//	 谁能使用这个页面,他是编号组成的字串。
//	 such as ,admin,jww,002, 
//	 if return value is null, It's mean all emps can visit it . 
//	*/
//	protected String WhoCanUseIt()
//	{
//		return null;
//	}
//
//		///#endregion
//
//	private void RP(String msg)
//	{
//		this.Response.Write(msg);
//	}
//	private void RPBR(String msg)
//	{
//		this.Response.Write(msg + "<br>");
//	}
//	public final void TableShow(DataTable dt, String title)
//	{
//
//		this.RPBR(title);
//		this.RPBR("<table border='1' width='100%'>");
//
//	}
//	public final String GenerCreateTableSQL(String className)
//	{
//		ArrayList als = ClassFactory.GetObjects(className);
//		int u = 0;
//		String sql = "";
//		for (Object obj : als)
//		{
//			u++;
//			try
//			{
//				Entity en = (Entity)obj;
//				switch (en.EnMap.EnDBUrl.DBType)
//				{
//					case Oracle:
//						sql += SqlBuilder.GenerCreateTableSQLOfOra_OK(en) + " \n GO \n";
//						break;
//					case Informix:
//						sql += SqlBuilder.GenerCreateTableSQLOfInfoMix(en) + " \n GO \n";
//						break;
//					default:
//						sql += SqlBuilder.GenerCreateTableSQLOfMS(en) + "\n GO \n";
//						break;
//				}
//			}
//			catch (java.lang.Exception e)
//			{
//				continue;
//			}
//			//Map map=en.EnMap;
//			//objStreamWriter.WriteLine(Convert.ToChar(9)+"No:"+u.ToString()+Convert.ToChar(9) +map.EnDesc +Convert.ToChar(9) +map.PhysicsTable+Convert.ToChar(9) +map.EnType);
//		}
//		Log.DefaultLogWriteLineInfo(sql);
//		return sql;
//	}
//
//
//	public final void ExportEntityToExcel(String classbaseName)
//	{
//
//		///#region 文件
//		String filename = "DatabaseDesign.xls";
//		String file = filename;
//		//bool flag = true;
//		String filepath = Request.PhysicalApplicationPath + "\\Temp\\";
//
//		//如果导出目录没有建立，则建立.
//		if ((new java.io.File(filepath)).isDirectory() == false)
//		{
//			(new java.io.File(filepath)).mkdirs();
//		}
//
//		filename = filepath + filename;
//		FileStream objFileStream = new FileStream(filename, FileMode.OpenOrCreate, FileAccess.Write);
//		StreamWriter objStreamWriter = new StreamWriter(objFileStream, System.Text.Encoding.Unicode);
//
//		///#endregion
//
//		//string str="";
//		ArrayList als = ClassFactory.GetObjects(classbaseName);
//		int i = 0;
//		objStreamWriter.WriteLine();
//		objStreamWriter.WriteLine((char)9 + "系统实体[" + classbaseName + "]" + (char)9);
//		objStreamWriter.WriteLine();
//		//objStreamWriter.WriteLine(Convert.ToChar(9)+"感谢使用系统实体结构自动生成器"+Convert.ToChar(9)+"调用日期"+Convert.ToChar(9)+DateTime.Now.ToString("yyyy年MM月dd日"));
//		objStreamWriter.WriteLine((char)9 + "从" + classbaseName + "继承下来的实体有[" + als.size() + "]个");
//
//
//
//		///#region 处理目录
//		objStreamWriter.WriteLine((char)9 + " " + (char)9 + "系统实体目录");
//		objStreamWriter.WriteLine((char)9 + "序号" + (char)9 + "实体名称" + (char)9 + "物理表/视图" + (char)9 + "类型");
//		int u = 0;
//		for (Object obj : als)
//		{
//			try
//			{
//				u++;
//				Entity en = (Entity)obj;
//				Map map = en.EnMap;
//				objStreamWriter.WriteLine((char)9 + "No:" + String.valueOf(u) + (char)9 + map.EnDesc + (char)9 + map.PhysicsTable + (char)9 + map.EnType);
//			}
//			catch (java.lang.Exception e)
//			{
//			}
//		}
//		objStreamWriter.WriteLine();
//
//		///#endregion
//
//		for (Object obj : als)
//		{
//			try
//			{
//
//				i++;
//				Entity en = (Entity)obj;
//				Map map = en.EnMap;
//
//
//				///#region 生成导出文件
//				objStreamWriter.WriteLine("序号" + i);
//				objStreamWriter.WriteLine((char)9 + "实体名称" + (char)9 + map.EnDesc + (char)9 + "物理表/视图" + (char)9 + map.PhysicsTable + (char)9 + "实体类型" + (char)9 + map.EnType);
//				if (map.CodeStruct == null)
//				{
//					objStreamWriter.WriteLine((char)9 + "编码结构信息:无");
//				}
//				else
//				{
//					objStreamWriter.WriteLine((char)9 + "编码结构" + (char)9 + map.CodeStruct + "是否检查编号的长度" + (char)9 + map.IsCheckNoLength);
//				}
//				//objStreamWriter.WriteLine(Convert.ToChar(9)+"物理存放位置"+map.EnDBUrl+Convert.ToChar(9)+"实体内存存放位置"+Convert.ToChar(9)+map.DepositaryOfEntity+Convert.ToChar(9)+"Map 内存存放位置"+Convert.ToChar(9)+map.DepositaryOfMap);
//				objStreamWriter.WriteLine((char)9 + "物理存放位置" + map.EnDBUrl + (char)9 + "Map 内存存放位置" + (char)9 + map.DepositaryOfMap);
//				objStreamWriter.WriteLine((char)9 + "访问权限" + (char)9 + "是否查看" + en.HisUAC.IsView + (char)9 + "是否新建" + en.HisUAC.IsInsert + (char)9 + "是否删除" + en.HisUAC.IsDelete + "是否更新" + en.HisUAC.IsUpdate + (char)9 + "是否附件" + en.HisUAC.IsAdjunct);
//				if (map.Dtls.size() > 0)
//				{
//					/* output dtls */
//					EnDtls dtls = map.Dtls;
//					objStreamWriter.WriteLine((char)9 + "明细/从表信息:个数" + dtls.size());
//					int ii = 0;
//					for (EnDtl dtl : dtls)
//					{
//						ii++;
//						objStreamWriter.WriteLine((char)9 + " " + (char)9 + "编号:" + ii + "描述:" + dtl.Desc + "关系到的实体类" + dtl.EnsName + "外键" + dtl.RefKey);
//						objStreamWriter.WriteLine((char)9 + " " + (char)9 + "物理表:" + dtl.Ens.GetNewEntity.getEnMap().getPhysicsTable());
//						objStreamWriter.WriteLine((char)9 + " " + (char)9 + "备注:关于" + dtl.Desc + "更详细的信息,请参考" + dtl.EnsName);
//					}
//				}
//				else
//				{
//					objStreamWriter.WriteLine((char)9 + "明细/从表信息:无");
//				}
//
//				if (map.AttrsOfOneVSM.size() > 0)
//				{
//					/* output dtls */
//					AttrsOfOneVSM dtls = map.AttrsOfOneVSM;
//					objStreamWriter.WriteLine((char)9 + "多对多关系:个数" + dtls.size());
//					int ii = 0;
//					for (AttrOfOneVSM dtl : dtls)
//					{
//						ii++;
//						objStreamWriter.WriteLine((char)9 + " " + (char)9 + "编号:" + ii + "描述:" + dtl.Desc);
//						objStreamWriter.WriteLine((char)9 + " " + (char)9 + "多对多实体类" + dtl.EnsOfMM.toString() + "外键" + dtl.AttrOfOneInMM);
//						objStreamWriter.WriteLine((char)9 + " " + (char)9 + "此实体关联到的外键" + dtl.AttrOfOneInMM);
//						objStreamWriter.WriteLine((char)9 + " " + (char)9 + "多实体类" + dtl.EnsOfMM.toString() + "外键" + dtl.AttrOfMValue);
//					}
//				}
//				else
//				{
//					objStreamWriter.WriteLine((char)9 + "多对多关系:无");
//				}
//
//				objStreamWriter.WriteLine((char)9 + "表/视图结构");
//				int iii = 0;
//				objStreamWriter.WriteLine((char)9 + " " + (char)9 + "属性序号" + (char)9 + "属性描述" + (char)9 + "属性" + (char)9 + "物理字段" + (char)9 + "数据类型" + (char)9 + "默认值" + (char)9 + "关系类型" + (char)9 + "备注");
//
//				for (Attr attr : map.Attrs)
//				{
//					iii++;
//					if (attr.MyFieldType == FieldType.Enum)
//					{
//						objStreamWriter.WriteLine((char)9 + " " + (char)9 + iii + (char)9 + attr.Desc + (char)9 + attr.Key + (char)9 + attr.Field + (char)9 + attr.MyDataTypeStr + (char)9 + attr.DefaultVal + (char)9 + "枚举" + (char)9 + "枚举Key" + attr.UIBindKey + " 关于枚举的信息请到Sys_Enum表里找到更详细的信息.");
//						continue;
//					}
//					if (attr.MyFieldType == FieldType.PKEnum)
//					{
//						objStreamWriter.WriteLine((char)9 + " " + (char)9 + iii + (char)9 + attr.Desc + (char)9 + attr.Key + (char)9 + attr.Field + (char)9 + attr.MyDataTypeStr + (char)9 + attr.DefaultVal + (char)9 + "主键枚举" + (char)9 + "枚举Key" + attr.UIBindKey + " 关于枚举的信息请到Sys_Enum表里找到更详细的信息.");
//
//						//objStreamWriter.WriteLine(Convert.ToChar(9)+" "+Convert.ToChar(9)+"No:"+iii+Convert.ToChar(9)+"描述"+Convert.ToChar(9)+attr.Desc+Convert.ToChar(9)+"属性"+Convert.ToChar(9)+attr.Key+Convert.ToChar(9)+"属性默认值"+Convert.ToChar(9)+attr.DefaultVal+Convert.ToChar(9)+"物理字段"+Convert.ToChar(9)+attr.Field+Convert.ToChar(9)+"字段关系类型"+Convert.ToChar(9)+"枚举主键"+Convert.ToChar(9)+"字段数据类型 "+Convert.ToChar(9)+attr.MyDataTypeStr+"");
//						continue;
//					}
//					if (attr.MyFieldType == FieldType.FK)
//					{
//						Entity tmp = attr.HisFKEn;
//						objStreamWriter.WriteLine((char)9 + " " + (char)9 + iii + (char)9 + attr.Desc + (char)9 + attr.Key + (char)9 + attr.Field + (char)9 + attr.MyDataTypeStr + (char)9 + attr.DefaultVal + (char)9 + "外键" + (char)9 + "关连的实体:" + tmp.EnDesc + "物理表:" + tmp.getEnMap().getPhysicsTable() + " 关于" + tmp.EnDesc + "信息请到此实体信息里面去找.");
//
//						//objStreamWriter.WriteLine(Convert.ToChar(9)+" "+Convert.ToChar(9)+"No:"+iii+Convert.ToChar(9)+"描述"+Convert.ToChar(9)+attr.Desc+Convert.ToChar(9)+"属性"+Convert.ToChar(9)+attr.Key+Convert.ToChar(9)+"属性默认值"+Convert.ToChar(9)+attr.DefaultVal+Convert.ToChar(9)+"物理字段"+Convert.ToChar(9)+attr.Field+Convert.ToChar(9)+"字段关系类型"+Convert.ToChar(9)+"外键"+Convert.ToChar(9)+"字段数据类型 "+Convert.ToChar(9)+attr.MyDataTypeStr+""+"关系到的实体名称"+Convert.ToChar(9)+tmp.EnDesc+"物理表"+Convert.ToChar(9)+tmp.getEnMap().getPhysicsTable()+Convert.ToChar(9)+"更详细的信息请参考"+Convert.ToChar(9));
//						continue;
//					}
//					if (attr.MyFieldType == FieldType.PKFK)
//					{
//						Entity tmp = attr.HisFKEn;
//						objStreamWriter.WriteLine((char)9 + " " + (char)9 + iii + (char)9 + attr.Desc + (char)9 + attr.Key + (char)9 + attr.Field + (char)9 + attr.MyDataTypeStr + (char)9 + attr.DefaultVal + (char)9 + "外主键" + (char)9 + "关连的实体:" + tmp.EnDesc + "物理表:" + tmp.getEnMap().getPhysicsTable() + " 关于" + tmp.EnDesc + "信息请到此实体信息里面去找.");
//						continue;
//					}
//
//					//其他的情况.
//					if (attr.MyFieldType == FieldType.Normal || attr.MyFieldType == FieldType.PK)
//					{
//						objStreamWriter.WriteLine((char)9 + " " + (char)9 + iii + (char)9 + attr.Desc + (char)9 + attr.Key + (char)9 + attr.Field + (char)9 + attr.MyDataTypeStr + (char)9 + attr.DefaultVal + (char)9 + "普通" + (char)9 + attr.EnterDesc);
//						//objStreamWriter.WriteLine(Convert.ToChar(9)+" "+Convert.ToChar(9)+"No:"+iii+Convert.ToChar(9)+"描述"+Convert.ToChar(9)+attr.Desc+Convert.ToChar(9)+"属性"+Convert.ToChar(9)+attr.Key+Convert.ToChar(9)+"属性默认值"+Convert.ToChar(9)+attr.DefaultVal+Convert.ToChar(9)+"物理字段"+Convert.ToChar(9)+attr.Field+Convert.ToChar(9)+"字段关系类型"+Convert.ToChar(9)+"字符"+Convert.ToChar(9)+"字段数据类型"+Convert.ToChar(9)+attr.MyDataTypeStr+""+Convert.ToChar(9)+"输入要求"+Convert.ToChar(9)+attr.EnterDesc);
//						continue;
//					}
//					//objStreamWriter.WriteLine("属性序号:"+iii+Convert.ToChar(9)+"描述"+Convert.ToChar(9)+attr.Desc+Convert.ToChar(9)+"属性"+Convert.ToChar(9)+attr.Key+Convert.ToChar(9)+"属性默认值"+Convert.ToChar(9)+attr.DefaultVal+Convert.ToChar(9)+"物理字段"+Convert.ToChar(9)+attr.Field+"字段关系类型"+Convert.ToChar(9)+"字符"+Convert.ToChar(9)+"字段数据类型"+Convert.ToChar(9)+attr.MyDataTypeStr+Convert.ToChar(9)+""+"输入要求"+Convert.ToChar(9)+attr.EnterDesc+Convert.ToChar(9));
//				}
//			}
//			catch (java.lang.Exception e2)
//			{
//			}
//		}
//		objStreamWriter.WriteLine();
//		objStreamWriter.WriteLine((char)9 + (char)9 + " " + (char)9 + (char)9 + " 制表人：" + (char)9 + WebUser.Name + (char)9 + "日期：" + (char)9 + java.time.LocalDateTime.now().ToShortDateString());
//
//		objStreamWriter.Close();
//		objFileStream.Close();
//
//		this.Write_Javascript(" window.open('" + SystemConfig.getSysNo() + "/Temp/" + file + "'); ");
//
//
//
//				///#endregion
//
//
//
//	}
//	public final void Helper(String htmlFile)
//	{
//		this.WinOpen(htmlFile);
//	}
//
//	public final void Helper()
//	{
//		this.WinOpen(SystemConfig.getSysNo() + "/" + SystemConfig.AppSettings["PageOfHelper"]);
//	}
//	/** 
//	 取得属性by key.
//	 
//	 @param key
//	 @return 
//	*/
//	public final String GetRequestStrByKey(String key)
//	{
//		return this.Request.QueryString[key];
//	}
//
//
//		///#region 操作方法
//	/** 
//	 showmodaldialog 
//	 
//	 @param url
//	 @param title
//	 @param Height
//	 @param Width
//	*/
//	protected final void ShowModalDialog(String url, String title, int Height, int Width)
//	{
//		String script = "<script language='JavaScript'>window.showModalDialog('" + url + "','','dialogHeight: " + String.valueOf(Height) + "px; dialogWidth: " + String.valueOf(Width) + "px; dialogTop: 100px; dialogLeft: 100px; center: no; help: no'); </script> ";
//
//		//this.RegisterStartupScript("key1s",script); // old .
//		ClientScript.RegisterStartupScript(this.getClass(), "K1", script); // new
//
//		//this.Response.Write( script );
//		//this.RegisterClientScriptBlock("Dia",script);
//	}
//	/** 
//	 关闭窗口
//	*/
//	protected final void WinClose()
//	{
//		this.Response.Write("<script language='JavaScript'> window.close();</script>");
//	}
//	protected final void WinClose(String val)
//	{
//		//经测试谷歌,IE都走window.top.returnValue 方法
//		String clientscript = "<script language='javascript'> if(window.opener != undefined){window.top.returnValue = '" + val + "';} else { window.returnValue = '" + val + "';} window.close(); </script>";
//		//string clientscript = "<script language='javascript'>  window.returnValue = '" + val + "'; window.close(); </script>";
//		this.Page.Response.Write(clientscript);
//	}
//	/** 
//	 打开一个新的窗口
//	 
//	 @param msg
//	*/
//	protected final void WinOpen(String url)
//	{
//		this.WinOpen(url, "", "msg", 900, 500);
//	}
//	protected final String dealUrl(String url)
//	{
//		if (url.indexOf("?") == -1)
//		{
//			//url=url.substing(0,url.IndexOf("",""));
//			return url;
//		}
//		else
//		{
//			return url;
//		}
//	}
//	protected final void WinOpen(String url, String title, String winName, int width, int height)
//	{
//		this.WinOpen(url, title, winName, width, height, 0, 0);
//	}
//	protected final void WinOpen(String url, String title, int width, int height)
//	{
//		this.WinOpen(url, title, "ActivePage", width, height, 0, 0);
//	}
//	protected final void WinOpen(String url, String title, String winName, int width, int height, int top, int left)
//	{
//		WinOpen(url, title, winName, width, height, top, left, false, false);
//	}
//	protected final void WinOpen(String url, String title, String winName, int width, int height, int top, int left, boolean _isShowToolBar, boolean _isShowAddress)
//	{
//		url = url.replace("<", "[");
//		url = url.replace(">", "]");
//		url = url.trim();
//		title = title.replace("<", "[");
//		title = title.replace(">", "]");
//		title = title.replace("\"", "‘");
//		String isShowAddress = "no", isShowToolBar = "no";
//		if (_isShowAddress)
//		{
//			isShowAddress = "yes";
//		}
//		if (_isShowToolBar)
//		{
//			isShowToolBar = "yes";
//		}
//		// this.Response.Write("<script language='JavaScript'> var newWindow =window.showModelessDialog('" + url + "','" + winName + "','width=" + width + "px,top=" + top + "px,left=" + left + "px,height=" + height + "px,scrollbars=yes,resizable=yes,toolbar=" + isShowToolBar + ",location=" + isShowAddress + "'); newWindow.focus(); </script> ");
//		this.Response.Write("<script language='JavaScript'> var newWindow =window.open('" + url + "','" + winName + "','width=" + width + "px,top=" + top + "px,left=" + left + "px,height=" + height + "px,scrollbars=yes,resizable=yes,toolbar=" + isShowToolBar + ",location=" + isShowAddress + "'); newWindow.focus(); </script> ");
//
//	}
//	//private int MsgFontSize=1;
//	/** 
//	 输出到页面上红色的警告。
//	 
//	 @param msg 消息
//	*/
//	protected final void ResponseWriteRedMsg(String msg)
//	{
//		msg = msg.replace("@", "<BR>@");
//		System.Web.HttpContext.Current.Session["info"] = msg;
//		System.Web.HttpContext.Current.Application["info" + WebUser.getNo()] = msg;
//
//		String url = SystemConfig.getSysNo() + "WF/Comm/Port/ErrorPage.aspx?d=" + java.time.LocalDateTime.now().toString();
//		this.WinOpen(url, "警告", "errmsg", 500, 400, 150, 270);
//	}
//	protected final void ResponseWriteShowModalDialogRedMsg(String msg)
//	{
//		msg = msg.replace("@", "<BR>@");
//		System.Web.HttpContext.Current.Session["info"] = msg;
//
//		String url = SystemConfig.getSysNo() + "WF/Comm/Port/ErrorPage.aspx?d=" + java.time.LocalDateTime.now().toString();
//		this.WinOpenShowModalDialog(url, "警告", "msg", 500, 400, 120, 270);
//	}
//	protected final void ResponseWriteShowModalDialogBlueMsg(String msg)
//	{
//		msg = msg.replace("@", "<BR>@");
//		System.Web.HttpContext.Current.Session["info"] = msg;
//
//		String url = SystemConfig.getSysNo() + "WF/Comm/Port/InfoPage.aspx?d=" + java.time.LocalDateTime.now().toString();
//		this.WinOpenShowModalDialog(url, "提示", "msg", 500, 400, 120, 270);
//	}
//
//	protected final void WinOpenShowModalDialog(String url, String title, String key, int width, int height, int top, int left)
//	{
//		//url=this.Request.ApplicationPath+"Comm/ShowModalDialog.htm?"+url;
//		//this.RegisterStartupScript(key,"<script language='JavaScript'>window.showModalDialog('"+url+"','"+key+"' ,'dialogHeight: 500px; dialogWidth:"+width+"px; dialogTop: "+top+"px; dialogLeft: "+left+"px; center: yes; help: no' ) ;  </script> ");
//
//		this.ClientScript.RegisterStartupScript(this.getClass(), key, "<script language='JavaScript'>window.showModalDialog('" + url + "','" + key + "' ,'dialogHeight: 500px; dialogWidth:" + width + "px; dialogTop: " + top + "px; dialogLeft: " + left + "px; center: yes; help: no' ) ;  </script> ");
//
//	}
//	protected final void WinOpenShowModalDialogResponse(String url, String title, String key, int width, int height, int top, int left)
//	{
//		url = this.Request.ApplicationPath + "Comm/ShowModalDialog.htm?" + url;
//		this.Response.Write("<script language='JavaScript'>window.showModalDialog('" + url + "','" + key + "' ,'dialogHeight: 500px; dialogWidth:" + width + "px; dialogTop: " + top + "px; dialogLeft: " + left + "px; center: yes; help: no' ) ;  </script> ");
//	}
//
//	protected final void ResponseWriteRedMsg(RuntimeException ex)
//	{
//		this.ResponseWriteRedMsg(ex.getMessage());
//	}
//	/** 
//	 输出到页面上蓝色的信息。
//	 
//	 @param msg 消息
//	*/
//	protected final void ResponseWriteBlueMsg(String msg)
//	{
//		msg = msg.replace("@", "<br>@");
//		System.Web.HttpContext.Current.Session["info"] = msg;
//		System.Web.HttpContext.Current.Application["info" + WebUser.getNo()] = msg;
//		String url = SystemConfig.getSysNo() + "WF/Comm/Port/InfoPage.aspx?d=" + java.time.LocalDateTime.now().toString();
//		this.WinOpen(url, "信息", "d" + this.Session.SessionID, 500, 300, 150, 270);
//	}
//
//	protected final void AlertHtmlMsg(String msg)
//	{
//		if (tangible.StringHelper.isNullOrEmpty(msg))
//		{
//			return;
//		}
//
//		msg = msg.replace("@", "<br>@");
//		System.Web.HttpContext.Current.Session["info"] = msg;
//		String url = "MsgPage.aspx?d=" + java.time.LocalDateTime.now().toString();
//		this.WinOpen(url, "信息", this.Session.SessionID, 500, 400, 150, 270);
//	}
//	/** 
//	 保存成功
//	*/
//	protected final void ResponseWriteBlueMsg_SaveOK()
//	{
//		Alert("保存成功！", false);
//	}
//	/** 
//	 保存成功
//	 
//	 @param num 记录个数。
//	*/
//	protected final void ResponseWriteBlueMsg_SaveOK(int num)
//	{
//		Alert("共计[" + num + "]条记录保存成功！", false);
//	}
//	/** 
//	 ResponseWriteBlueMsg_DeleteOK
//	*/
//	protected final void ResponseWriteBlueMsg_DeleteOK()
//	{
//
//		this.Alert("删除成功！", false);
//		//
//		//更新成功
//		//			//this.Alert("删除成功!");
//		//			ResponseWriteBlueMsg("删除成功!");
//	}
//	/** 
//	 "共计["+delNum+"]条记录删除成功！"
//	 
//	 @param delNum delNum
//	*/
//	protected final void ResponseWriteBlueMsg_DeleteOK(int delNum)
//	{
//		//this.Alert("删除成功!");
//		this.Alert("共计[" + delNum + "]条记录删除成功！", false);
//
//	}
//	/** 
//	 ResponseWriteBlueMsg_UpdataOK
//	*/
//	protected final void ResponseWriteBlueMsg_UpdataOK()
//	{
//		//this.ResponseWriteBlueMsg("更新成功",false);
//		this.Alert("更新成功!");
//		// ResponseWriteBlueMsg("更新成功!");
//	}
//	protected final void ToSignInPage()
//	{
//		System.Web.HttpContext.Current.Response.Redirect(SystemConfig.PageOfLostSession);
//	}
//	protected final void ToWelPage()
//	{
//		System.Web.HttpContext.Current.Response.Redirect(BP.Sys.Glo.Request.ApplicationPath + "/Wel.aspx");
//	}
//	protected final void ToErrorPage(RuntimeException mess)
//	{
//		this.ToErrorPage(mess.getMessage());
//	}
//	/** 
//	 切换到信息也面。
//	 
//	 @param mess
//	*/
//	protected final void ToErrorPage(String mess)
//	{
//		System.Web.HttpContext.Current.Session["info"] = mess;
//		System.Web.HttpContext.Current.Response.Redirect(SystemConfig.getSysNo() + "WF/Comm/Port/ToErrorPage.aspx?d=" + java.time.LocalDateTime.now().toString(), false);
//	}
//	/** 
//	 切换到信息也面。
//	 
//	 @param mess
//	*/
//	protected final void ToCommMsgPage(String mess)
//	{
//		mess = mess.replace("@", "<BR>@");
//		mess = mess.replace("~", "@");
//
//		System.Web.HttpContext.Current.Session["info"] = mess;
//		if (SystemConfig.AppSettings["PageMsg"] == null)
//		{
//			System.Web.HttpContext.Current.Response.Redirect(SystemConfig.getSysNo() + "WF/Comm/Port/InfoPage.aspx?d=" + java.time.LocalDateTime.now().toString(), false);
//		}
//		else
//		{
//			System.Web.HttpContext.Current.Response.Redirect(SystemConfig.AppSettings["PageMsg"] + "?d=" + java.time.LocalDateTime.now().toString(), false);
//		}
//	}
//	/** 
//	 切换到信息也面。
//	 
//	 @param mess
//	*/
//	protected final void ToWFMsgPage(String mess)
//	{
//		mess = mess.replace("@", "<BR>@");
//		mess = mess.replace("~", "@");
//
//		System.Web.HttpContext.Current.Session["info"] = mess;
//		if (SystemConfig.AppSettings["PageMsg"] == null)
//		{
//			System.Web.HttpContext.Current.Response.Redirect(SystemConfig.getSysNo() + "WF/Comm/Port/InfoPage.aspx?d=" + java.time.LocalDateTime.now().toString(), false);
//		}
//		else
//		{
//			System.Web.HttpContext.Current.Response.Redirect(SystemConfig.AppSettings["PageMsg"] + "?d=" + java.time.LocalDateTime.now().toString(), false);
//		}
//	}
//	protected final void ToMsgPage_Do(String mess)
//	{
//		System.Web.HttpContext.Current.Session["info"] = mess;
//		System.Web.HttpContext.Current.Response.Redirect(SystemConfig.getSysNo() + "WF/Comm/Port/InfoPage.aspx?d=" + java.time.LocalDateTime.now().toString(), false);
//	}
//
//		///#endregion
//
//	/** 
//	转到一个页面上。 '_top'
//	 
//	 @param mess
//	 @param target '_top'
//	*/
//	protected final void ToErrorPage(String mess, String target)
//	{
//		System.Web.HttpContext.Current.Session["info"] = mess;
//		System.Web.HttpContext.Current.Response.Redirect(SystemConfig.getSysNo() + "WF/Comm/Port/InfoPage.aspx target='_top'");
//	}
//
//	/** 
//	 窗口的OnInit事件，自动在页面上加一下记录当前行的Hidden
//	 
//	 @param e
//	*/
//	@Override
//	protected void OnInit(EventArgs e)
//	{
//		//ShowRuning();
//		super.OnInit(e);
//
//		if (this.WhoCanUseIt() != null)
//		{
//			if (this.WhoCanUseIt().equals(WebUser.getNo()))
//			{
//				return;
//			}
//			if (this.WhoCanUseIt().indexOf("," + WebUser.getNo() + ",") == -1)
//			{
//				this.ToErrorPage("您没有权限访问这个页面。");
//			}
//		}
//
//
//
//
//	}
//
//
//		///#region 与控件有关系的操作
//	public final void ShowDataTable(DataTable dt)
//	{
//		this.Response.Write(this.DataTable2Html(dt, true));
//	}
//	/** 
//	 显示DataTable.
//	*/
//	public final String DataTable2Html(DataTable dt, boolean isShowTitle)
//	{
//		String str = "";
//		if (isShowTitle)
//		{
//			str = dt.TableName + " 合计:" + dt.Rows.size() + "记录.";
//		}
//		str += "<Table>";
//		str += "<TR>";
//		for (DataColumn dc : dt.Columns)
//		{
//			str += "  <TD warp=false >";
//			str += dc.ColumnName;
//			str += "  </TD>";
//		}
//		str += "</TR>";
//
//
//		for (DataRow dr : dt.Rows)
//		{
//			str += "<TR>";
//
//			for (DataColumn dc : dt.Columns)
//			{
//				str += "  <TD>";
//				str += dr.getItem(dc.ColumnName);
//				str += "  </TD>";
//			}
//			str += "</TR>";
//		}
//
//		str += "</Table>";
//		return str;
//
//		//this.ResponseWriteBlueMsg(str);
//
//
//	}
//	/** 
//	 显示运行
//	*/
//	public final void ShowRuning()
//	{
//		//if (this.IsPostBack==false)
//		//	return ;		
//
//
//		String str = "<script language=javascript><!-- function showRuning() {	sending.style.visibility='visible' } --> </script>";
//
//
//		// if (!this.IsClientScriptBlockRegistered("ClientProxyScript"))
//		//   this.RegisterClientScriptBlock("ClientProxyScript", str);
//
//		if (!this.ClientScript.IsClientScriptBlockRegistered("ClientProxyScript"))
//		{
//			this.ClientScript.RegisterStartupScript(this.getClass(), "ClientProxyScript", str);
//		}
//
//		if (this.IsPostBack == false)
//		{
//			str = "<div id='sending' style='position: absolute; top: 126; left: -25; z-index: 10; visibility: hidden; width: 903; height: 74'><TABLE WIDTH=100% BORDER=0 CELLSPACING=0 CELLPADDING=0><TR><td width=30%></td><TD bgcolor=#ff9900><TABLE WIDTH=100% height=70 BORDER=0 CELLSPACING=2 CELLPADDING=0><TR><td bgcolor=#eeeeee align=center>系统正在相应您的请求, 请稍候...</td></tr></table></td><td width=30%></td></tr></table></div> ";
//			this.Response.Write(str);
//		}
//	}
//
//
//		///#endregion
//
//
//		///#region 图片属性
//
//	/** 
//	 是否要检查功能
//	*/
//	protected final boolean getIsCheckFunc()
//	{
//			//if (this.SubPageMessage==null || this.SubPageTitle==null) 
//			//return false;
//
//		if (ViewState["IsCheckFunc"] != null)
//		{
//			return (boolean)ViewState["IsCheckFunc"];
//		}
//		else
//		{
//			return true;
//		}
//
//	}
//	protected final void setIsCheckFunc(boolean value)
//	{
//		ViewState["IsCheckFunc"] = value;
//	}
//
//
//
//		///#endregion
//
//
//		///#region 关于session 操作。
//
//	public static Object GetSessionObjByKey(String key)
//	{
//		Object val = System.Web.HttpContext.Current.Session[key];
//		return val;
//	}
//	public static String GetSessionByKey(String key)
//	{
//		return (String)GetSessionObjByKey(key);
//	}
//	/** 
//	 取出来字符串中的 Key1:val1;Key2:val2;  值. 
//	 
//	 @param key1
//	 @param key2
//	 @return 
//	*/
//	public static String GetSessionByKey(String key1, String key2)
//	{
//		String str = GetSessionByKey(key1);
//		if (str == null)
//		{
//			throw new RuntimeException("没有取到" + key1 + "的值.");
//		}
//
//		String[] strs = str.split("[;]", -1);
//		for (String s : strs)
//		{
//			String[] ss = s.split("[:]", -1);
//			if (key2.equals(ss[0]))
//			{
//				return ss[1];
//			}
//		}
//		return null;
//	}
//	public static void SetSessionByKey(String key, Object obj)
//	{
//		System.Web.HttpContext.Current.Session[key] = obj;
//	}
//	public static void SetSessionByKey(String key1, String key2, Object obj)
//	{
//		String str = GetSessionByKey(key1);
//		String KV = key2 + ":" + obj.toString() + ";";
//		if (str == null)
//		{
//			SetSessionByKey(key1, KV);
//			return;
//		}
//
//
//
//		String[] strs = str.split("[;]", -1);
//		for (String s : strs)
//		{
//			String[] ss = s.split("[:]", -1);
//			if (key2.equals(ss[0]))
//			{
//				SetSessionByKey(key1, str.replace(s + ";", KV));
//				return;
//			}
//		}
//
//		SetSessionByKey(key1, str + KV);
//	}
//
//		///#endregion
//
//
//		///#region 对于 ViewState 的操作。
//	/** 
//	 设置 ViewState Value
//	 
//	 @param key
//	 @param val
//	 @param DefaultVal
//	*/
//	public final void SetValueByKey(String key, Object val, Object DefaultVal)
//	{
//		if (val == null)
//		{
//			ViewState[key] = DefaultVal;
//		}
//		else
//		{
//			ViewState[key] = val;
//		}
//	}
//	public final void SetValueByKey(String key, Object val)
//	{
//		ViewState[key] = val;
//	}
//	/** 
//	 取出Val
//	 
//	 @param key
//	 @return 
//	*/
//	public final String GetValueByKey(String key)
//	{
//		try
//		{
//			return ViewState[key].toString();
//		}
//		catch (java.lang.Exception e)
//		{
//			return null;
//		}
//	}
//	public final boolean GetValueByKeyBool(String key)
//	{
//		if (this.GetValueByKey(key).equals("1"))
//		{
//			return true;
//		}
//		return false;
//	}
//	/** 
//	 ss
//	 
//	 @param key ss
//	 @param DefaultVal ss
//	 @return 
//	*/
//	public final String GetValueByKey_del(String key, String DefaultVal)
//	{
//		try
//		{
//			return ViewState[key].toString();
//		}
//		catch (java.lang.Exception e)
//		{
//			return DefaultVal;
//		}
//	}
//	/** 
//	 按照key 取出来,bool 的植. 
//	 
//	 @param key
//	 @param DefaultValue
//	 @return 
//	*/
//	public final boolean GetBoolValusByKey_del(String key, boolean DefaultValue)
//	{
//		try
//		{
//			return Boolean.parseBoolean(this.GetValueByKey(key));
//		}
//		catch (java.lang.Exception e)
//		{
//			return DefaultValue;
//		}
//	}
//	/** 
//	 取出int valus , 如果没有就返回 DefaultValue ;
//	 
//	 @param key
//	 @return 
//	*/
//	public final int GetIntValueByKey_del(String key, int DefaultValue)
//	{
//		try
//		{
//			return Integer.parseInt(ViewState[key].toString());
//		}
//		catch (java.lang.Exception e)
//		{
//			return DefaultValue;
//		}
//	}
//
//		///#endregion
//
//
//
//	/** 
//	 这个table 是用来处理页面上的DataGride. 
//	*/
//	protected final System.Data.DataTable getTable()
//	{
//			//DataTable dt = (System.Data.DataTable)ViewState["Table"];
//		DataTable dt = (System.Data.DataTable)ViewState["Table"];
//		if (dt == null)
//		{
//			dt = new DataTable();
//		}
//		return dt;
//	}
//	protected final void setTable(System.Data.DataTable value)
//	{
//		ViewState["Table"] = value;
//	}
//	protected final System.Data.DataTable getTable_bak()
//	{
//			//DataTable dt = (System.Data.DataTable)ViewState["Table"];
//		Object tempVar = this.Session["Table"];
//		DataTable dt = (DataTable)((tempVar instanceof DataTable) ? tempVar : null);
//		if (dt == null)
//		{
//			dt = new DataTable();
//		}
//		return dt;
//	}
//	protected final void setTable_bak(System.Data.DataTable value)
//	{
//		this.Session["Table"] = value;
//	}
//	protected final System.Data.DataTable getTable1()
//	{
//
//		DataTable dt = (System.Data.DataTable)ViewState["Table1"];
//		if (dt == null)
//		{
//			dt = new DataTable();
//		}
//		return dt;
//	}
//	protected final void setTable1(System.Data.DataTable value)
//	{
//		ViewState["Table1"] = value;
//	}
//	/** 
//	 应用程序主键
//	*/
//	protected final String getPK()
//	{
//		try
//		{
//			return ViewState["PK"].toString();
//		}
//		catch (java.lang.Exception e)
//		{
//			return null;
//		}
//	}
//	protected final void setPK(String value)
//	{
//		ViewState["PK"] = value;
//	}
//	/** 
//	 用来保存状态。
//	*/
//	protected final boolean getIsNew_del()
//	{
//		try
//		{
//			return (boolean)ViewState["IsNew"];
//		}
//		catch (java.lang.Exception e)
//		{
//			return false;
//		}
//	}
//	protected final void setIsNew_del(boolean value)
//	{
//		ViewState["IsNew"] = value;
//	}
//	/** 
//	 PKOID if is null return 0 
//	*/
//	protected final int getPKint()
//	{
//		try
//		{
//			return Integer.parseInt(ViewState["PKint"].toString());
//		}
//		catch (java.lang.Exception e)
//		{
//			return 0;
//		}
//	}
//	protected final void setPKint(int value)
//	{
//		ViewState["PKint"] = value;
//	}
//	//		protected void ShowMessage(string msg)
//	//		{
//	//			PubClass.ShowMessage(msg);
//	//		}		
//	//		protected void ShowMessage_SaveOK()
//	//		{
//	//			PubClass.ShowMessageMSG_SaveOK();
//	//		}
//	protected final void ShowMessage_SaveUnsuccessful()
//	{
//		//PubClass.ShowMessage(msg);
//	}
//
//	//		protected void ShowMessage_UpdateSuccessful()
//	//		{
//	//			PubClass.ShowMessage("更新成功！");
//	//		}
//	protected final void ShowMessage_UpdateUnsuccessful()
//	{
//		//PubClass.ShowMessage(msg);
//	}
//	protected final void Write_Javascript(String script)
//	{
//		script = script.replace("<", "[");
//		script = script.replace(">", "]");
//		Response.Write("<script language=javascript> " + script + " </script>");
//	}
//	protected final void ShowMessageWin(String url)
//	{
//		this.Response.Write("<script language='JavaScript'> window.open('" + url + "')</script>");
//	}
//	protected final void Alert(String mess)
//	{
//		if (tangible.StringHelper.isNullOrEmpty(mess))
//		{
//			return;
//		}
//
//		this.Alert(mess, false);
//	}
//	/** 
//	 不用page 参数，show message
//	 
//	 @param mess
//	*/
//	protected final void Alert(String mess, boolean isClent)
//	{
//		if (tangible.StringHelper.isNullOrEmpty(mess))
//		{
//			return;
//		}
//
//		//this.ResponseWriteRedMsg(mess);
//		//return;
//		mess = mess.replace("'", "＇");
//
//		mess = mess.replace("\"", "＂");
//
//		mess = mess.replace(";", "；");
//		mess = mess.replace(")", "）");
//		mess = mess.replace("(", "（");
//
//		mess = mess.replace(",", "，");
//		mess = mess.replace(":", "：");
//
//
//		mess = mess.replace("<", "［");
//		mess = mess.replace(">", "］");
//
//		mess = mess.replace("[", "［");
//		mess = mess.replace("]", "］");
//
//
//		mess = mess.replace("@", "\\n@");
//
//		mess = mess.replace("\r\n", "");
//
//		String script = "<script language=JavaScript>alert('" + mess + "');</script>";
//		if (isClent)
//		{
//			System.Web.HttpContext.Current.Response.Write(script);
//		}
//		else
//		{
//			this.ClientScript.RegisterStartupScript(this.getClass(), "kesy", script);
//		}
//		//this.RegisterStartupScript("key1", script);
//	}
//
//	protected final void Alert(RuntimeException ex)
//	{
//		this.Alert(ex.getMessage(), false);
//	}
//
//		///#region 公共的方法
//
//
//		///#region 报表导出的问题
//	/** 
//	 根据DataTable内容导出到Excel中  
//	 
//	 @param dt 要导出内容的DataTable
//	 @param filepath 要产生的文件路径
//	 @param filename 要产生的文件
//	 @return 
//	*/
//	protected final boolean ExportDataTableToExcel_OpenWin_del(System.Data.DataTable dt, String title)
//	{
//		String filename = "Ep" + this.Session.SessionID + ".xls";
//		String file = filename;
//		boolean flag = true;
//		String filepath = SystemConfig.getPathOfTemp();
//
//
//		///#region 处理 datatable
//		for (DataColumn dc : dt.Columns)
//		{
//			switch (dc.ColumnName)
//			{
//				case "No":
//					dc.Caption = "编号";
//					break;
//				case "Name":
//					dc.Caption = "名称";
//					break;
//				case "Total":
//					dc.Caption = "合计";
//					break;
//				case "FK_Dept":
//					dc.Caption = "部门编号";
//					break;
//				case "ZSJGName":
//					dc.Caption = "部门名称";
//					break;
//				case "IncNo":
//					dc.Caption = "纳税人编号";
//					break;
//				case "IncName":
//					dc.Caption = "纳税人名称";
//					break;
//				case "TaxpayerNo":
//					dc.Caption = "纳税人编号";
//					break;
//				case "TaxpayerName":
//					dc.Caption = "纳税人名称";
//					break;
//				case "byrk":
//					dc.Caption = "本月入库";
//					break;
//				case "ljrk":
//					dc.Caption = "累计入库";
//					break;
//				case "qntq":
//					dc.Caption = "去年同期";
//					break;
//				case "jtqzje":
//					dc.Caption = "较去年增减额";
//					break;
//				case "jtqzjl":
//					dc.Caption = "较去年增减率";
//					break;
//				case "BenYueYiJiao":
//					dc.Caption = "本月已缴";
//					break;
//				case "BenYueYingJiao":
//					dc.Caption = "本月应缴";
//					break;
//				case "BenYueWeiJiao":
//					dc.Caption = "本月未缴";
//					break;
//				case "LeiJiWeiJiao":
//					dc.Caption = "累计未缴";
//					break;
//				case "QuNianTongQiLeiJiYiJiao":
//					dc.Caption = "去年同期未缴";
//					break;
//
//				case "QianNianTongQiLeiJiYiJiao":
//					dc.Caption = "前年同期累计已缴";
//					break;
//				case "QianNianTongQiLeiJiYingJiao":
//					dc.Caption = "前年同期累计应缴";
//					break;
//
//				case "JiaoQuNianTongQiZhengJian":
//					dc.Caption = "较去年同期增减";
//					break;
//				case "JiaoQuNianTongQiZhengJianLv":
//					dc.Caption = "较去年同期增减率";
//					break;
//
//				case "JiaoQianNianTongQiZhengJian":
//					dc.Caption = "较去年同期增减";
//					break;
//				case "JiaoQianNianTongQiZhengJianLv":
//					dc.Caption = "较前年同期增减率";
//					break;
//				case "LeiJiYiJiao":
//					dc.Caption = "累计已缴";
//					break;
//				case "LeiJiYingJiao":
//					dc.Caption = "累计应缴";
//					break;
//				case "QuNianBenYueYiJiao":
//					dc.Caption = "去年本月已缴";
//					break;
//				case "QuNianBenYueYingJiao":
//					dc.Caption = "去年本月应缴";
//					break;
//				case "QuNianLeiJiYiJiao":
//					dc.Caption = "去年累计已缴";
//					break;
//				case "QuNianLeiJiYingJiao":
//					dc.Caption = "去年累计应缴";
//					break;
//				case "QianNianBenYueYiJiao":
//					dc.Caption = "前年本月已缴";
//					break;
//				case "QianNianBenYueYingJiao":
//					dc.Caption = "前年本月应缴";
//					break;
//				case "QianNianLeiJiYiJiao":
//					dc.Caption = "前年同期累计已缴";
//					break;
//				case "QianNianLeiJiYingJiao":
//					dc.Caption = "前年同期累计应缴";
//					break;
//				case "JiaoQuNianZhengJian":
//					dc.Caption = "较去年同期增减";
//					break;
//				case "JiaoQuNianZhengJianLv":
//					dc.Caption = "较去年同期增减率";
//					break;
//				case "JiaoQianNianZhengJian":
//					dc.Caption = "较前年同期增减";
//					break;
//				case "JiaoQianNianZhengJianLv":
//					dc.Caption = "较前年同期增减率";
//					break;
//				case "level":
//					dc.Caption = "级次";
//					break;
//			}
//		}
//
//		///#endregion
//
//
//		///#region 参数及变量设置
//		//参数校验
//		if (dt == null || dt.Rows.size() <= 0 || filename == null || filename.equals("") || filepath == null || filepath.equals(""))
//		{
//			return false;
//		}
//
//		//如果导出目录没有建立，则建立
//		if ((new java.io.File(filepath)).isDirectory() == false)
//		{
//			(new java.io.File(filepath)).mkdirs();
//		}
//
//		filename = filepath + filename;
//
//		FileStream objFileStream = new FileStream(filename, FileMode.OpenOrCreate, FileAccess.Write);
//		StreamWriter objStreamWriter = new StreamWriter(objFileStream, System.Text.Encoding.Unicode);
//
//		///#endregion
//
//
//		///#region 生成导出文件
//		try
//		{
//			objStreamWriter.WriteLine();
//			objStreamWriter.WriteLine((char)9 + title + (char)9);
//			objStreamWriter.WriteLine();
//			String strLine = "";
//			//生成文件标题
//			for (int i = 0; i < dt.Columns.size(); i++)
//			{
//				strLine = strLine + dt.Columns[i].Caption + (char)9;
//			}
//
//			objStreamWriter.WriteLine(strLine);
//
//			strLine = "";
//
//			//生成文件内容
//			for (int row = 0; row < dt.Rows.size(); row++)
//			{
//				for (int col = 0; col < dt.Columns.size(); col++)
//				{
//					strLine = strLine + dt.Rows[row][col] + (char)9;
//				}
//				objStreamWriter.WriteLine(strLine);
//				strLine = "";
//			}
//			objStreamWriter.WriteLine();
//			objStreamWriter.WriteLine((char)9 + "制表人：" + (char)9 + WebUser.Name + (char)9 + "日期：" + (char)9 + java.time.LocalDateTime.now().ToShortDateString());
//
//		}
//		catch (java.lang.Exception e)
//		{
//			flag = false;
//		}
//		finally
//		{
//			objStreamWriter.Close();
//			objFileStream.Close();
//		}
//
//		///#endregion
//
//
//		///#region 删除掉旧的文件
//		DelExportedTempFile(filepath);
//
//		///#endregion
//
//
//		if (flag)
//		{
//			this.WinOpen("../Temp/" + file);
//			//this.Write_Javascript(" window.open( ); " );
//		}
//
//		return flag;
//	}
//	/** 
//	 删除掉导出时产生的临时文件 2002.11.09 create by bluesky 
//	 
//	 @param filepath 临时文件路径
//	 @return 
//	*/
//	public final boolean DelExportedTempFile(String filepath)
//	{
//		boolean flag = true;
//		try
//		{
//			String[] files = Directory.GetFiles(filepath);
//
//			for (int i = 0; i < files.length; i++)
//			{
//				java.time.LocalDateTime lastTime = File.GetLastWriteTime(files[i]);
//				TimeSpan span = java.time.LocalDateTime.now() - lastTime;
//
//				if (span.Hours >= 1)
//				{
//					(new java.io.File(files[i])).delete();
//				}
//			}
//		}
//		catch (java.lang.Exception e)
//		{
//			flag = false;
//		}
//
//		return flag;
//	}
//
//
//		///#endregion 报表导出
//
//
//
//		///#endregion

}
