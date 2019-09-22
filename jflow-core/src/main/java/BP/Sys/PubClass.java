package BP.Sys;

import BP.En.*;
import BP.DA.*;
import BP.Sys.*;
import BP.Web.*;
import BP.Port.*;
import java.util.*;
import java.io.*;
import java.time.*;
import java.math.*;

/** 
 PageBase 的摘要说明。
*/
public class PubClass
{
	/** 
	 发送邮件
	 
	 @param maillAddr 地址
	 @param title 标题
	 @param doc 内容
	*/
	public static void SendMail(String maillAddr, String title, String doc)
	{
		System.Net.Mail.MailMessage myEmail = new System.Net.Mail.MailMessage();
		myEmail.From = new System.Net.Mail.MailAddress("ccflow.cn@gmail.com", "ccflow", System.Text.Encoding.UTF8);

		myEmail.To.Add(maillAddr);
		myEmail.Subject = title;
		myEmail.SubjectEncoding = System.Text.Encoding.UTF8; //邮件标题编码

		myEmail.Body = doc;
		myEmail.BodyEncoding = System.Text.Encoding.UTF8; //邮件内容编码
		myEmail.IsBodyHtml = true; //是否是HTML邮件

		myEmail.Priority = MailPriority.High; //邮件优先级

		SmtpClient client = new SmtpClient();
		client.Credentials = new System.Net.NetworkCredential(SystemConfig.GetValByKey("SendEmailAddress", "ccflow.cn@gmail.com"), SystemConfig.GetValByKey("SendEmailPass", "ccflow123"));

		//上述写你的邮箱和密码
		client.Port = SystemConfig.GetValByKeyInt("SendEmailPort", 587); //使用的端口
		client.Host = SystemConfig.GetValByKey("SendEmailHost", "smtp.gmail.com");
		client.EnableSsl = true; //经过ssl加密.
		Object userState = myEmail;
		try
		{
			client.Send(myEmail);

			//   client.SendAsync(myEmail, userState);
		}
		catch (System.Net.Mail.SmtpException ex)
		{
			throw ex;
		}
	}
	public static String ToHtmlColor(String colorName)
	{
		try
		{
			if (colorName.startsWith("#"))
			{
				colorName = colorName.replace("#", "");
				//update by dgq 六位颜色不需要转换
				if (colorName.length() == 6)
				{
					return "#" + colorName;
				}
			}
			int v = Integer.parseInt(colorName, 16);

//C# TO JAVA CONVERTER WARNING: The right shift operator was not replaced by Java's logical right shift operator since the left operand was not confirmed to be of an unsigned type, but you should review whether the logical right shift operator (>>>) is more appropriate:
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: Color col = Color.FromArgb(Convert.ToByte((v >> 24) & 255), Convert.ToByte((v >> 16) & 255), Convert.ToByte((v >> 8) & 255), Convert.ToByte((v >> 0) & 255));
			Color col = Color.FromArgb((byte)((v >> 24) & 255), (byte)((v >> 16) & 255), (byte)((v >> 8) & 255), (byte)((v >> 0) & 255));

			int alpha = col.A;
			String red = String.valueOf(col.R, 16);
			;
			String green = String.valueOf(col.G, 16);
			String blue = String.valueOf(col.B, 16);
			return String.format("#%1$s%2$s%3$s", red, green, blue);
		}
		catch (java.lang.Exception e)
		{
			return "black";
		}
	}
	public static void InitFrm(String fk_mapdata)
	{
		// 删除数据.
		FrmLabs labs = new FrmLabs();
		labs.Delete(FrmLabAttr.FK_MapData, fk_mapdata);

		FrmLines lines = new FrmLines();
		lines.Delete(FrmLabAttr.FK_MapData, fk_mapdata);

		MapData md = new MapData();
		md.setNo(fk_mapdata);
		if (md.RetrieveFromDBSources() == 0)
		{
			MapDtl mdtl = new MapDtl();
			mdtl.setNo(fk_mapdata);
			if (mdtl.RetrieveFromDBSources() == 0)
			{
				throw new RuntimeException("@对:" + fk_mapdata + "的映射信息不存在.");
			}
			else
			{
				md.Copy(mdtl);
			}
		}

		MapAttrs mattrs = new MapAttrs(fk_mapdata);
		GroupFields gfs = new GroupFields(fk_mapdata);

		int tableW = 700;
		int padingLeft = 3;
		int leftCtrlX = 700 / 100 * 20;
		int rightCtrlX = 700 / 100 * 60;

		String keyID = LocalDateTime.now().toString("yyMMddhhmmss");
		// table 标题。
		int currX = 0;
		int currY = 0;
		FrmLab lab = new FrmLab();
		lab.setText(md.getName());
		lab.setFontSize(20);
		lab.setX(200);
		currY += 30;
		lab.setY(currY);
		lab.setFK_MapData(fk_mapdata);
		lab.setFontWeight("Bold");
		lab.setMyPK("Lab" + keyID + "1");
		lab.Insert();

		// 表格头部的横线.
		currY += 20;
		FrmLine lin = new FrmLine();
		lin.setX1(0);
		lin.setX2(tableW);
		lin.setY1(currY);
		lin.setY2(currY);
		lin.setBorderWidth(2);
		lin.setFK_MapData(fk_mapdata);
		lin.setMyPK("Lin" + keyID + "1");
		lin.Insert();
		currY += 5;

		boolean isLeft = false;
		int i = 2;
		for (GroupField gf : gfs)
		{
			i++;
			lab = new FrmLab();
			lab.setX(0);
			lab.setY(currY);
			lab.setText(gf.getLab());
			lab.setFK_MapData(fk_mapdata);
			lab.setFontWeight("Bold");
			lab.setMyPK("Lab" + keyID + String.valueOf(i));
			lab.Insert();

			currY += 15;
			lin = new FrmLine();
			lin.setX1(padingLeft);
			lin.setX2(tableW);
			lin.setY1(currY);
			lin.setY2(currY);
			lin.setFK_MapData(fk_mapdata);
			lin.setBorderWidth(3);
			lin.setMyPK("Lin" + keyID + String.valueOf(i));
			lin.Insert();

			isLeft = true;
			int idx = 0;
			for (MapAttr attr : mattrs)
			{
				if (gf.getOID() != attr.getGroupID() || attr.getUIVisible() == false)
				{
					continue;
				}

				idx++;
				if (isLeft)
				{
					lin = new FrmLine();
					lin.setX1(0);
					lin.setX2(tableW);
					lin.setY1(currY);
					lin.setY2(currY);
					lin.setFK_MapData(fk_mapdata);
					lin.setMyPK("Lin" + keyID + String.valueOf(i) + idx);
					lin.Insert();
					currY += 14; // 画一横线 .

					lab = new FrmLab();
					lab.setX(lin.getX1() + padingLeft);
					lab.setY(currY);
					lab.setText(attr.getName());
					lab.setFK_MapData(fk_mapdata);
					lab.setMyPK("Lab" + keyID + String.valueOf(i) + idx);
					lab.Insert();

					lin = new FrmLine();
					lin.setX1(leftCtrlX);
					lin.setY1(currY - 14);

					lin.setX2(leftCtrlX);
					lin.setY2(currY);
					lin.setFK_MapData(fk_mapdata);
					lin.setMyPK("Lin" + keyID + String.valueOf(i) + idx + "R");
					lin.Insert(); //画一 竖线

					attr.setX(leftCtrlX + padingLeft);
					attr.setY(currY - 3);
					attr.setUIWidth(150);
					attr.Update();
					currY += 14;
				}
				else
				{
					currY = currY - 14;
					lab = new FrmLab();
					lab.setX(tableW / 2 + padingLeft);
					lab.setY(currY);
					lab.setText(attr.getName());
					lab.setFK_MapData(fk_mapdata);
					lab.setMyPK("Lab" + keyID + String.valueOf(i) + idx);
					lab.Insert();

					lin = new FrmLine();
					lin.setX1(tableW / 2);
					lin.setY1(currY - 14);

					lin.setX2(tableW / 2);
					lin.setY2(currY);
					lin.setFK_MapData(fk_mapdata);
					lin.setMyPK("Lin" + keyID + String.valueOf(i) + idx);
					lin.Insert(); //画一 竖线

					lin = new FrmLine();
					lin.setX1(rightCtrlX);
					lin.setY1(currY - 14);
					lin.setX2(rightCtrlX);
					lin.setY2(currY);
					lin.setFK_MapData(fk_mapdata);
					lin.setMyPK("Lin" + keyID + String.valueOf(i) + idx + "R");
					lin.Insert(); //画一 竖线

					attr.setX(rightCtrlX + padingLeft);
					attr.setY(currY - 3);
					attr.setUIWidth(150);
					attr.Update();
					currY += 14;
				}
				isLeft = !isLeft;
			}
		}
		// table bottom line.
		lin = new FrmLine();
		lin.setX1(0);
		lin.setY1(currY);

		lin.setX2(tableW);
		lin.setY2(currY);
		lin.setFK_MapData(fk_mapdata);
		lin.setBorderWidth(3);
		lin.setMyPK("Lin" + keyID + "eR");
		lin.Insert();

		currY = currY - 28 - 18;
		// 处理结尾. table left line
		lin = new FrmLine();
		lin.setX1(0);
		lin.setY1(50);
		lin.setX2(0);
		lin.setY2(currY);
		lin.setFK_MapData(fk_mapdata);
		lin.setBorderWidth(3);
		lin.setMyPK("Lin" + keyID + "eRr");
		lin.Insert();

		// table right line.
		lin = new FrmLine();
		lin.setX1(tableW);
		lin.setY1(50);
		lin.setX2(tableW);
		lin.setY2(currY);
		lin.setFK_MapData(fk_mapdata);
		lin.setBorderWidth(3);
		lin.setMyPK("Lin" + keyID + "eRr4");
		lin.Insert();
	}
	public static String ColorToStr(System.Drawing.Color color)
	{
		try
		{
			String color_s = System.Drawing.ColorTranslator.ToHtml(color);
			color_s = color_s.substring(1, color_s.length());
			return "#" + String.valueOf(Integer.parseInt(color_s, 16) + 40000, 16);
		}
		catch (java.lang.Exception e)
		{
			return "black";
		}
	}
	/** 
	 处理字段
	 
	 @param fd
	 @return 
	*/
	public static String DealToFieldOrTableNames(String fd)
	{
		String keys = "~!@#$%^&*()+{}|:<>?`=[];,./～！＠＃￥％……＆×（）——＋｛｝｜：“《》？｀－＝［］；＇，．／";
		char[] cc = keys.toCharArray();
		for (char c : cc)
		{
			fd = fd.replace(String.valueOf(c), "");
		}
		String s = fd.substring(0, 1);
		try
		{
			int a = Integer.parseInt(s);
			fd = "F" + fd;
		}
		catch (java.lang.Exception e)
		{
		}
		return fd;
	}
	private static String _KeyFields = null;
	public static String getKeyFields()
	{
		if (_KeyFields == null)
		{
			_KeyFields = BP.DA.DataType.ReadTextFile(SystemConfig.getPathOfWebApp() + SystemConfig.getCCFlowWebPath() + "WF\\Data\\Sys\\FieldKeys.txt");
		}
		return _KeyFields;
	}
	public static boolean IsNum(String str)
	{
		boolean strResult;
		String cn_Regex = "^[\\u4e00-\\u9fa5]+$";
		if (Regex.IsMatch(str, cn_Regex))
		{
			strResult = true;
		}
		else
		{
			strResult = false;
		}
		return strResult;
	}

	public static boolean IsCN(String str)
	{
		boolean strResult;
		String cn_Regex = "^[\\u4e00-\\u9fa5]+$";
		if (Regex.IsMatch(str, cn_Regex))
		{
			strResult = true;
		}
		else
		{
			strResult = false;
		}
		return strResult;
	}

	public static boolean IsImg(String ext)
	{
		ext = ext.replace(".", "").toLowerCase();
		switch (ext)
		{
			case "gif":
				return true;
			case "jpg":
				return true;
			case "bmp":
				return true;
			case "png":
				return true;
			default:
				return false;
		}
	}
	/** 
	 按照比例数小
	 
	 @param ObjH 目标高度
	 @param factH 实际高度
	 @param factW 实际宽度
	 @return 目标宽度
	*/
	public static int GenerImgW_del(int ObjH, int factH, int factW, int isZeroAsWith)
	{
		if (factH == 0 || factW == 0)
		{
			return isZeroAsWith;
		}

		BigDecimal d = BigDecimal.Parse(String.valueOf(ObjH)).divide(BigDecimal.Parse(String.valueOf(factH)).multiply(BigDecimal.Parse(String.valueOf(factW))));

		try
		{
			return Integer.parseInt(d.toString("0"));
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException(d.toString() + ex.getMessage());
		}
	}

	/** 
	 按照比例数小
	 
	 @param ObjH 目标高度
	 @param factH 实际高度
	 @param factW 实际宽度
	 @return 目标宽度
	*/
	public static int GenerImgH(int ObjW, int factH, int factW, int isZeroAsWith)
	{
		if (factH == 0 || factW == 0)
		{
			return isZeroAsWith;
		}

		BigDecimal d = BigDecimal.Parse(String.valueOf(ObjW)).divide(BigDecimal.Parse(String.valueOf(factW)).multiply(BigDecimal.Parse(String.valueOf(factH))));

		try
		{
			return Integer.parseInt(d.toString("0"));
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException(d.toString() + ex.getMessage());
		}
	}
	/** 
	 产生临时文件名称
	 
	 @param hz
	 @return 
	*/
	public static String GenerTempFileName(String hz)
	{
		return Web.WebUser.getNo() + LocalDateTime.now().toString("MMddhhmmss") + "." + hz;
	}
	public static void DeleteTempFiles()
	{
		//string[] strs = System.IO.Directory.GetFiles( MapPath( SystemConfig.TempFilePath )) ;
		String[] strs = (new File(SystemConfig.getPathOfTemp())).list(File::isFile);

		for (String s : strs)
		{
			(new File(s)).delete();
		}
	}
	/** 
	 重新建立索引
	*/
	public static void ReCreateIndex()
	{
		ArrayList als = ClassFactory.GetObjects("BP.En.Entity");
		String sql = "";
		for (Object obj : als)
		{
			Entity en = (Entity)obj;
			if (en.getEnMap().getEnType() == EnType.View)
			{
				continue;
			}
			sql += "IF EXISTS( SELECT name  FROM  sysobjects WHERE  name='" + en.getEnMap().getPhysicsTable() + "') <BR> DROP TABLE " + en.getEnMap().getPhysicsTable() + "<BR>";
			sql += "CREATE TABLE " + en.getEnMap().getPhysicsTable() + " ( <BR>";
			sql += "";
		}


	}

	/** 
	 检查所有的物理表
	*/

	public static void CheckAllPTable()
	{
		CheckAllPTable(null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static void CheckAllPTable(string nameS=null)
	public static void CheckAllPTable(String nameS)
	{
		ArrayList al = BP.En.ClassFactory.GetObjects("BP.En.Entities");
		for (Entities ens : al)
		{
			if (ens == null || ens.toString() == null)
			{
				continue;
			}

			if (nameS != null)
			{
				if (ens.toString().contains(nameS) == false)
				{
					continue;
				}
			}

			try
			{
				Entity en = ens.getGetNewEntity();
				en.CheckPhysicsTable();
			}
			catch (java.lang.Exception e)
			{
			}
		}
	}
	/** 
	 获取datatable.
	 
	 @param uiBindKey
	 @return 
	*/
	public static System.Data.DataTable GetDataTableByUIBineKey(String uiBindKey)
	{
		DataTable dt = new DataTable();
		if (uiBindKey.contains("."))
		{
			Entities ens = BP.En.ClassFactory.GetEns(uiBindKey);
			if (ens == null)
			{
				ens = BP.En.ClassFactory.GetEns(uiBindKey);
			}

			if (ens == null)
			{
				ens = BP.En.ClassFactory.GetEns(uiBindKey);
			}
			if (ens == null)
			{
				throw new RuntimeException("类名错误:" + uiBindKey + ",不能转化成ens.");
			}

			ens.RetrieveAllFromDBSource();
			dt = ens.ToDataTableField(uiBindKey);
			return dt;
		}

		//added by liuxc,2017-09-11,增加动态SQL查询类型的处理，此种类型没有固定的数据表或视图
		SFTable sf = new SFTable();
		sf.setNo(uiBindKey);
		if (sf.RetrieveFromDBSources() != 0)
		{
			if (sf.getSrcType() == SrcType.Handler || sf.getSrcType() == SrcType.JQuery)
			{
				return null;
			}
			dt = sf.GenerHisDataTable();
		}

		if (dt == null)
		{
			dt = new DataTable();
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 把列名做成标准的.
		for (DataColumn col : dt.Columns)
		{
			String colName = col.ColumnName.toLowerCase();
			switch (colName)
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 把列名做成标准的.

		dt.TableName = uiBindKey;
		return dt;
	}


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 系统调度
	public static String GenerDBOfOreacle()
	{
		ArrayList als = ClassFactory.GetObjects("BP.En.Entity");
		String sql = "";
		for (Object obj : als)
		{
			Entity en = (Entity)obj;
			sql += "IF EXISTS( SELECT name  FROM  sysobjects WHERE  name='" + en.getEnMap().getPhysicsTable() + "') <BR> DROP TABLE " + en.getEnMap().getPhysicsTable() + "<BR>";
			sql += "CREATE TABLE " + en.getEnMap().getPhysicsTable() + " ( <BR>";
			sql += "";
		}
		//DA.Log.DefaultLogWriteLine(LogType.Error,msg.Replace("<br>@","\n") ); // 
		return sql;
	}
	public static String DBRpt(DBCheckLevel level)
	{
		// 取出全部的实体
		ArrayList als = ClassFactory.GetObjects("BP.En.Entities");
		String msg = "";
		for (Object obj : als)
		{
			Entities ens = (Entities)obj;
			try
			{
				msg += DBRpt1(level, ens);
			}
			catch (RuntimeException ex)
			{
				msg += "<hr>" + ens.toString() + "体检失败:" + ex.getMessage();
			}
		}

		MapDatas mds = new MapDatas();
		mds.RetrieveAllFromDBSource();
		for (MapData md : mds)
		{
			try
			{
				md.getHisGEEn().CheckPhysicsTable();
				PubClass.AddComment(md.getHisGEEn());
			}
			catch (RuntimeException ex)
			{
				msg += "<hr>" + md.getNo() + "体检失败:" + ex.getMessage();
			}
		}

		MapDtls dtls = new MapDtls();
		dtls.RetrieveAllFromDBSource();
		for (MapDtl dtl : dtls)
		{
			try
			{
				dtl.getHisGEDtl().CheckPhysicsTable();
				PubClass.AddComment(dtl.getHisGEDtl());
			}
			catch (RuntimeException ex)
			{
				msg += "<hr>" + dtl.getNo() + "体检失败:" + ex.getMessage();
			}
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 检查处理必要的基础数据 Pub_Day .
		String sql = "";
		String sqls = "";
		sql = "SELECT count(*) Num FROM Pub_Day";
		try
		{
			if (DBAccess.RunSQLReturnValInt(sql) == 0)
			{
				for (int i = 1; i <= 31; i++)
				{
					String d = tangible.StringHelper.padLeft(String.valueOf(i), 2, '0');
					sqls += "@INSERT INTO Pub_Day(No,Name)VALUES('" + d.toString() + "','" + d.toString() + "')";
				}
			}
		}
		catch (java.lang.Exception e)
		{
		}

		sql = "SELECT count(*) Num FROM Pub_YF";
		try
		{
			if (DBAccess.RunSQLReturnValInt(sql) == 0)
			{
				for (int i = 1; i <= 12; i++)
				{
					String d = tangible.StringHelper.padLeft(String.valueOf(i), 2, '0');
					sqls += "@INSERT INTO Pub_YF(No,Name)VALUES('" + d.toString() + "','" + d.toString() + "')";
				}
			}
		}
		catch (java.lang.Exception e2)
		{
		}

		sql = "SELECT count(*) Num FROM Pub_ND";
		try
		{
			if (DBAccess.RunSQLReturnValInt(sql) == 0)
			{
				for (int i = 2010; i < 2015; i++)
				{
					String d = String.valueOf(i);
					sqls += "@INSERT INTO Pub_ND(No,Name)VALUES('" + d.toString() + "','" + d.toString() + "')";
				}
			}
		}
		catch (java.lang.Exception e3)
		{

		}
		sql = "SELECT count(*) Num FROM Pub_NY";
		try
		{
			if (DBAccess.RunSQLReturnValInt(sql) == 0)
			{
				for (int i = 2010; i < 2015; i++)
				{

					for (int yf = 1; yf <= 12; yf++)
					{
						String d = String.valueOf(i) + "-" + tangible.StringHelper.padLeft(String.valueOf(yf), 2, '0');
						sqls += "@INSERT INTO Pub_NY(No,Name)VALUES('" + d + "','" + d + "')";
					}
				}
			}
		}
		catch (java.lang.Exception e4)
		{
		}

		DBAccess.RunSQLs(sqls);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 检查处理必要的基础数据。
		return msg;
	}
	private static void RepleaceFieldDesc(Entity en)
	{
		String tableId = DBAccess.RunSQLReturnVal("select ID from sysobjects WHERE name='" + en.getEnMap().getPhysicsTable() + "' AND xtype='U'").toString();

		if (tableId == null || tableId.equals(""))
		{
			return;
		}

		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}

		}
	}
	/** 
	 为表增加注释
	 
	 @return 
	*/
	public static String AddComment()
	{
		// 取出全部的实体
		ArrayList als = ClassFactory.GetObjects("BP.En.Entities");
		String msg = "";
		Entity en = null;
		Entities ens = null;
		for (Object obj : als)
		{
			try
			{
				ens = (Entities)obj;
				en = ens.getGetNewEntity();
				if (en.getEnMap().getEnType() == EnType.View || en.getEnMap().getEnType() == EnType.ThirdPartApp)
				{
					continue;
				}
			}
			catch (java.lang.Exception e)
			{
				continue;
			}

			msg += AddComment(en);
		}
		return msg;
	}
	public static String AddComment(Entity en)
	{
		if (en == null)
		{
			return null;
		}

		if (en.getEnMap() == null)
		{
			return null;
		}

		if (en.getEnMap().getPhysicsTable() == null)
		{
			return null;
		}


		if (DBAccess.IsExitsObject(en.getEnMap().getPhysicsTable()) == false)
		{
			return "实体表不存在.";
		}

		try
		{
			switch (en.getEnMap().getEnDBUrl().getDBType())
			{
				case Oracle:
					AddCommentForTable_Ora(en);
					break;
				case MySQL:
					AddCommentForTable_MySql(en);
					break;
				default:
					AddCommentForTable_MS(en);
					break;
			}
			return "";
		}
		catch (RuntimeException ex)
		{
			return "<hr>" + en.toString() + "体检失败:" + ex.getMessage();
		}
	}
	public static void AddCommentForTable_Ora(Entity en)
	{


		en.RunSQL("comment on table " + en.getEnMap().getPhysicsTable() + " IS '" + en.getEnDesc() + "'");
		SysEnums ses = new SysEnums();
		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}

			if (DBAccess.IsExitsTableCol(en.getEnMap().getPhysicsTable(), attr.getField()) == false)
			{
				continue;
			}


			switch (attr.getMyFieldType())
			{
				case PK:
					en.RunSQL("comment on column  " + en.getEnMap().getPhysicsTable() + "." + attr.getField() + " IS '" + attr.getDesc() + "-主键'");
					break;
				case Normal:
					en.RunSQL("comment on column  " + en.getEnMap().getPhysicsTable() + "." + attr.getField() + " IS '" + attr.getDesc() + "'");
					break;
				case Enum:
					ses = new SysEnums(attr.getKey(), attr.UITag);
					en.RunSQL("comment on column  " + en.getEnMap().getPhysicsTable() + "." + attr.getField() + " IS '" + attr.getDesc() + ",枚举类型:" + ses.ToDesc() + "'");
					break;
				case PKEnum:
					ses = new SysEnums(attr.getKey(), attr.UITag);
					en.RunSQL("comment on column  " + en.getEnMap().getPhysicsTable() + "." + attr.getField() + " IS '" + attr.getDesc() + ", 主键:枚举类型:" + ses.ToDesc() + "'");
					break;
				case FK:
					Entity myen = attr.getHisFKEn(); // ClassFactory.GetEns(attr.UIBindKey).GetNewEntity;
					en.RunSQL("comment on column  " + en.getEnMap().getPhysicsTable() + "." + attr.getField() + " IS " + attr.getDesc() + ", 外键:对应物理表:" + myen.getEnMap().getPhysicsTable() + ",表描述:" + myen.getEnDesc());
					break;
				case PKFK:
					Entity myen1 = attr.getHisFKEn(); // ClassFactory.GetEns(attr.UIBindKey).GetNewEntity;
					en.RunSQL("comment on column  " + en.getEnMap().getPhysicsTable() + "." + attr.getField() + " IS '" + attr.getDesc() + ", 主外键:对应物理表:" + myen1.getEnMap().getPhysicsTable() + ",表描述:" + myen1.getEnDesc() + "'");
					break;
				default:
					break;
			}
		}
	}
	public static void AddCommentForTable_MySql(Entity en)
	{
		MySql.Data.MySqlClient.MySqlConnection conn = new MySql.Data.MySqlClient.MySqlConnection(BP.Sys.SystemConfig.getAppCenterDSN());
		en.RunSQL("alter table " + conn.Database + "." + en.getEnMap().getPhysicsTable() + " comment = '" + en.getEnDesc() + "'");


		//获取当前实体对应表的所有字段结构信息
		DataTable cols = DBAccess.RunSQLReturnTable("select column_name,column_default,is_nullable,character_set_name,column_type from information_schema.columns where table_schema = '" + conn.Database + "' and table_name='" + en.getEnMap().getPhysicsTable() + "'");
		SysEnums ses = new SysEnums();
		String sql = "";
		DataRow row = null;

		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}

			row = cols.Select(String.format("column_name='%1$s'", attr.getField()))[0];
			sql = String.format("ALTER TABLE %1$s.%2$s CHANGE COLUMN %3$s %3$s %4$s%5$s%6$s%7$s COMMENT ", conn.Database, en.getEnMap().getPhysicsTable(), attr.getField(), row.get("column_type").toString().toUpperCase(), Equals(row.get("character_set_name"), "utf8") ? " CHARACTER SET 'utf8'" : "", Equals(row.get("is_nullable"), "YES") ? " NULL" : " NOT NULL", Equals(row.get("column_default"), "NULL") ? " DEFAULT NULL" : (Equals(row.get(""), "") ? "" : " DEFAULT " + row.get("")));

			switch (attr.getMyFieldType())
			{
				case PK:
					en.RunSQL(sql + String.format("'%1$s - 主键'", attr.getDesc()));
					break;
				case Normal:
					en.RunSQL(sql + String.format("'%1$s'", attr.getDesc()));
					break;
				case Enum:
					ses = new SysEnums(attr.getKey(), attr.UITag);
					en.RunSQL(sql + String.format("'%1$s,枚举类型:%2$s'", attr.getDesc(), ses.ToDesc()));
					break;
				case PKEnum:
					ses = new SysEnums(attr.getKey(), attr.UITag);
					en.RunSQL(sql + String.format("'%1$s,主键:枚举类型:%2$s'", attr.getDesc(), ses.ToDesc()));
					break;
				case FK:
					Entity myen = attr.getHisFKEn(); // ClassFactory.GetEns(attr.UIBindKey).GetNewEntity;
					en.RunSQL(sql + String.format("'%1$s,外键:对应物理表:%2$s,表描述:%3$s'", attr.getDesc(), myen.getEnMap().getPhysicsTable(), myen.getEnDesc()));
					break;
				case PKFK:
					Entity myen1 = attr.getHisFKEn(); // ClassFactory.GetEns(attr.UIBindKey).GetNewEntity;
					en.RunSQL(sql + String.format("'%1$s,主外键:对应物理表:%2$s,表描述:%3$s'", attr.getDesc(), myen1.getEnMap().getPhysicsTable(), myen1.getEnDesc()));
					break;
				default:
					break;
			}
		}
	}
	private static void AddColNote(Entity en, String table, String col, String note)
	{
		try
		{
			String sql = "execute  sp_dropextendedproperty 'MS_Description','user',dbo,'table','" + table + "','column'," + col;
			en.RunSQL(sql);
		}
		catch (RuntimeException ex)
		{

		}

		try
		{
			String sql = "execute  sp_addextendedproperty 'MS_Description', '" + note + "', 'user', dbo, 'table', '" + table + "', 'column', '" + col + "'";
			en.RunSQL(sql);
		}
		catch (RuntimeException ex)
		{
		}
	}
	/** 
	 为表增加解释
	 
	 @param en
	*/
	public static void AddCommentForTable_MS(Entity en)
	{

		if (en.getEnMap().getEnType() == EnType.View || en.getEnMap().getEnType() == EnType.ThirdPartApp)
		{
			return;
		}

		try
		{
			String sql = "execute  sp_dropextendedproperty 'MS_Description','user',dbo,'table','" + en.getEnMap().getPhysicsTable() + "'";
			en.RunSQL(sql);
		}
		catch (RuntimeException ex)
		{
		}

		try
		{
			String sql = "execute  sp_addextendedproperty 'MS_Description', '" + en.getEnDesc() + "', 'user', dbo, 'table', '" + en.getEnMap().getPhysicsTable() + "'";
			en.RunSQL(sql);
		}
		catch (RuntimeException ex)
		{
		}


		SysEnums ses = new SysEnums();
		for (Attr attr : en.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}
			if (attr.getKey().equals(attr.getDesc()))
			{
				continue;
			}
			if (attr.getField().equals(attr.getDesc()))
			{
				continue;
			}

			switch (attr.getMyFieldType())
			{
				case PK:
					AddColNote(en, en.getEnMap().getPhysicsTable(), attr.getField(), attr.getDesc() + "(主键)");
					//en.RunSQL("comment on table "+ en.EnMap.PhysicsTable+"."+attr.Field +" IS '"+en.EnDesc+"'");
					break;
				case Normal:
					AddColNote(en, en.getEnMap().getPhysicsTable(), attr.getField(), attr.getDesc());
					//en.RunSQL("comment on table "+ en.EnMap.PhysicsTable+"."+attr.Field +" IS '"+en.EnDesc+"'");
					break;
				case Enum:
					ses = new SysEnums(attr.getKey(), attr.UITag);
					//	en.RunSQL("comment on table "+ en.EnMap.PhysicsTable+"."+attr.Field +" IS '"++"'" );
					AddColNote(en, en.getEnMap().getPhysicsTable(), attr.getField(), attr.getDesc() + ",枚举类型:" + ses.ToDesc());
					break;
				case PKEnum:
					ses = new SysEnums(attr.getKey(), attr.UITag);
					AddColNote(en, en.getEnMap().getPhysicsTable(), attr.getField(), attr.getDesc() + ",主键:枚举类型:" + ses.ToDesc());
					//en.RunSQL("comment on table "+ en.EnMap.PhysicsTable+"."+attr.Field +" IS '"+en.EnDesc+", 主键:枚举类型:"+ses.ToDesc()+"'" );
					break;
				case FK:
					Entity myen = attr.getHisFKEn(); // ClassFactory.GetEns(attr.UIBindKey).GetNewEntity;
					AddColNote(en, en.getEnMap().getPhysicsTable(), attr.getField(), attr.getDesc() + ", 外键:对应物理表:" + myen.getEnMap().getPhysicsTable() + ",表描述:" + myen.getEnDesc());
					//en.RunSQL("comment on table "+ en.EnMap.PhysicsTable+"."+attr.Field +" IS "+  );
					break;
				case PKFK:
					Entity myen1 = attr.getHisFKEn(); // ClassFactory.GetEns(attr.UIBindKey).GetNewEntity;
					AddColNote(en, en.getEnMap().getPhysicsTable(), attr.getField(), attr.getDesc() + ", 主外键:对应物理表:" + myen1.getEnMap().getPhysicsTable() + ",表描述:" + myen1.getEnDesc());
					//en.RunSQL("comment on table "+ en.EnMap.PhysicsTable+"."+attr.Field +" IS '"+  );
					break;
				default:
					break;
			}
		}
	}
	/** 
	 产程系统报表，如果出现问题，就写入日志里面。
	 
	 @return 
	*/
	public static String DBRpt1(DBCheckLevel level, Entities ens)
	{
		Entity en = ens.getGetNewEntity();
		if (en.getEnMap().getEnDBUrl().getDBUrlType() != DBUrlType.AppCenterDSN)
		{
			return null;
		}

		if (en.getEnMap().getEnType() == EnType.ThirdPartApp)
		{
			return null;
		}

		if (en.getEnMap().getEnType() == EnType.View)
		{
			return null;
		}

		if (en.getEnMap().getEnType() == EnType.Ext)
		{
			return null;
		}

		// 检测物理表的字段。
		en.CheckPhysicsTable();

		PubClass.AddComment(en);
		return "";

		String msg = "";

		String table = en.getEnMap().getPhysicsTable();
		Attrs fkAttrs = en.getEnMap().getHisFKAttrs();
		if (fkAttrs.Count == 0)
		{
			return msg;
		}
		int num = 0;
		String sql;
		//string msg="";
		for (Attr attr : fkAttrs)
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}

			String enMsg = "";
			try
			{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#region 更新他们，去掉左右空格，因为外键不能包含左右空格。
				if (level == DBCheckLevel.Middle || level == DBCheckLevel.High)
				{
					/*如果是高中级别,就去掉左右空格*/
					if (attr.getMyDataType() == DataType.AppString)
					{
						DBAccess.RunSQL("UPDATE " + en.getEnMap().getPhysicsTable() + " SET " + attr.getField() + " = rtrim( ltrim(" + attr.getField() + ") )");
					}
				}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#region 处理关联表的情况.
				Entities refEns = attr.getHisFKEns(); // ClassFactory.GetEns(attr.UIBindKey);
				Entity refEn = refEns.getGetNewEntity();

				//取出关联的表。
				String reftable = refEn.getEnMap().getPhysicsTable();
				//sql="SELECT COUNT(*) FROM "+en.EnMap.PhysicsTable+" WHERE "+attr.Key+" is null or len("+attr.Key+") < 1 ";
				// 判断外键表是否存在。

				sql = "SELECT COUNT(*) FROM  sysobjects  WHERE  name = '" + reftable + "'";
				//num=DA.DBAccess.RunSQLReturnValInt(sql,0);
				if (DBAccess.IsExitsObject(new DBUrl(DBUrlType.AppCenterDSN), reftable) == false)
				{
					//报告错误信息
					enMsg += "<br>@检查实体：" + en.getEnDesc() + ",字段 " + attr.getKey() + " , 字段描述:" + attr.getDesc() + " , 外键物理表:" + reftable + "不存在:" + sql;
				}
				else
				{
					Attr attrRefKey = refEn.getEnMap().GetAttrByKey(attr.getUIRefKeyValue()); // 去掉主键的左右 空格．
					if (attrRefKey.getMyDataType() == DataType.AppString)
					{
						if (level == DBCheckLevel.Middle || level == DBCheckLevel.High)
						{
							/*如果是高中级别,就去掉左右空格*/
							DBAccess.RunSQL("UPDATE " + reftable + " SET " + attrRefKey.getField() + " = rtrim( ltrim(" + attrRefKey.getField() + ") )");
						}
					}

					Attr attrRefText = refEn.getEnMap().GetAttrByKey(attr.getUIRefKeyText()); // 去掉主键 Text 的左右 空格．

					if (level == DBCheckLevel.Middle || level == DBCheckLevel.High)
					{
						/*如果是高中级别,就去掉左右空格*/
						DBAccess.RunSQL("UPDATE " + reftable + " SET " + attrRefText.getField() + " = rtrim( ltrim(" + attrRefText.getField() + ") )");
					}

				}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#region 外键的实体是否为空
				switch (en.getEnMap().getEnDBUrl().getDBType())
				{
					case Oracle:
						sql = "SELECT COUNT(*) FROM " + en.getEnMap().getPhysicsTable() + " WHERE " + attr.getField() + " is null or length(" + attr.getField() + ") < 1 ";
						break;
					default:
						sql = "SELECT COUNT(*) FROM " + en.getEnMap().getPhysicsTable() + " WHERE " + attr.getField() + " is null or len(" + attr.getField() + ") < 1 ";
						break;
				}

				num = DA.DBAccess.RunSQLReturnValInt(sql, 0);
				if (num == 0)
				{
				}
				else
				{
					enMsg += "<br>@检查实体：" + en.getEnDesc() + ",物理表:" + en.getEnMap().getPhysicsTable() + "出现" + attr.getKey() + "," + attr.getDesc() + "不正确,共有[" + num + "]行记录没有数据。" + sql;
				}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#region 是否能够对应到外键
				//是否能够对应到外键。
				sql = "SELECT COUNT(*) FROM " + en.getEnMap().getPhysicsTable() + " WHERE " + attr.getField() + " NOT IN ( SELECT " + refEn.getEnMap().GetAttrByKey(attr.getUIRefKeyValue()).getField() + " FROM " + reftable + "	 ) ";
				num = DA.DBAccess.RunSQLReturnValInt(sql, 0);
				if (num == 0)
				{
				}
				else
				{
					/*如果是高中级别.*/
					String delsql = "DELETE FROM " + en.getEnMap().getPhysicsTable() + " WHERE " + attr.getField() + " NOT IN ( SELECT " + refEn.getEnMap().GetAttrByKey(attr.getUIRefKeyValue()).getField() + " FROM " + reftable + "	 ) ";
					//int i =DBAccess.RunSQL(delsql);
					enMsg += "<br>@" + en.getEnDesc() + ",物理表:" + en.getEnMap().getPhysicsTable() + "出现" + attr.getKey() + "," + attr.getDesc() + "不正确,共有[" + num + "]行记录没有关联到数据，请检查物理表与外键表。" + sql + "如果您想删除这些对应不上的数据请运行如下SQL: " + delsql + " 请慎重执行.";
				}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#region 判断 主键
				//DBAccess.IsExits("");
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion
			}
			catch (RuntimeException ex)
			{
				enMsg += "<br>@" + ex.getMessage();
			}

			if (!enMsg.equals(""))
			{
				msg += "<BR><b>-- 检查[" + en.getEnDesc() + "," + en.getEnMap().getPhysicsTable() + "]出现如下问题,类名称:" + en.toString() + "</b>";
				msg += enMsg;
			}
		}
		return msg;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion



	/** 
	 转换
	 
	 @param ht
	 @return 
	*/
	public static DataTable HashtableToDataTable(Hashtable ht)
	{
		DataTable dt = new DataTable();
		dt.TableName = "Hashtable";
		for (String key : ht.keySet())
		{
			dt.Columns.Add(key, String.class);
		}

		DataRow dr = dt.NewRow();
		for (String key : ht.keySet())
		{
			dr.set(key, ht.get(key) instanceof String ? (String)ht.get(key) : null);
		}
		dt.Rows.Add(dr);
		return dt;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region
	public static void To(String url)
	{
		HttpContextHelper.getResponse().Redirect(url, true);
	}
	public static void Print(String url)
	{
		HttpContextHelper.ResponseWrite("<script language='JavaScript'> var newWindow =window.open('" + url + "','p','width=0,top=10,left=10,height=1,scrollbars=yes,resizable=yes,toolbar=yes,location=yes,menubar=yes') ; newWindow.focus(); </script> ");
	}
	public static BP.En.Entity CopyFromRequest(BP.En.Entity en)
	{
		//获取传递来的所有的checkbox ids 用于设置该属性为falsse.
		String checkBoxIDs = HttpContextHelper.RequestParams("CheckBoxIDs");
		if (checkBoxIDs != null)
		{
			String[] strs = checkBoxIDs.split("[,]", -1);
			for (String str : strs)
			{
				if (str == null || str.equals(""))
				{
					continue;
				}

				//设置该属性为false.
				en.getRow().put(str.replace("CB_", ""), 0);
			}
		}


		//如果不使用clone 就会导致 "集合已修改;可能无法执行枚举操作。"的错误。
		Object tempVar = en.getRow().Clone();
		Hashtable ht = tempVar instanceof Hashtable ? (Hashtable)tempVar : null;

		/*说明已经找到了这个字段信息。*/
		for (String key : HttpContextHelper.getRequestParamKeys())
		{
			if (key == null || key.equals(""))
			{
				continue;
			}

			//获得实际的值, 具有特殊标记的，系统才赋值.
			Object tempVar2 = key.Clone();
			String attrKey = tempVar2 instanceof String ? (String)tempVar2 : null;
			if (key.startsWith("TB_"))
			{
				attrKey = attrKey.replace("TB_", "");
			}
			else if (key.startsWith("CB_"))
			{
				attrKey = attrKey.replace("CB_", "");
			}
			else if (key.startsWith("DDL_"))
			{
				attrKey = attrKey.replace("DDL_", "");
			}
			else if (key.startsWith("RB_"))
			{
				attrKey = attrKey.replace("RB_", "");
			}
			else
			{
				continue;
			}

			String val = HttpContextHelper.RequestParams(key);

			//其他的属性.
			en.getRow().put(attrKey, val);
		}
		return en;
	}

	public static BP.En.Entity CopyFromRequestByPost(BP.En.Entity en)
	{
		//获取传递来的所有的checkbox ids 用于设置该属性为falsse.
		String checkBoxIDs = HttpContextHelper.RequestParams("CheckBoxIDs");
		if (checkBoxIDs != null)
		{
			String[] strs = checkBoxIDs.split("[,]", -1);
			for (String str : strs)
			{
				if (str == null || str.equals(""))
				{
					continue;
				}

				if (str.contains("CBPara"))
				{
					/*如果是参数字段.*/
					en.getRow().put(str.replace("CBPara_", ""), 0);
				}
				else
				{
					//设置该属性为false.
					en.getRow().put(str.replace("CB_", ""), 0);
				}

			}
		}


		//如果不使用clone 就会导致 "集合已修改;可能无法执行枚举操作。"的错误。
		Object tempVar = en.getRow().Clone();
		Hashtable ht = tempVar instanceof Hashtable ? (Hashtable)tempVar : null;

		/*说明已经找到了这个字段信息。*/
		for (String key : HttpContextHelper.getRequestParamKeys())
		{
			if (key == null || key.equals(""))
			{
				continue;
			}

			//获得实际的值, 具有特殊标记的，系统才赋值.
			Object tempVar2 = key.Clone();
			String attrKey = tempVar2 instanceof String ? (String)tempVar2 : null;
			if (key.startsWith("TB_"))
			{
				attrKey = attrKey.replace("TB_", "");
			}
			else if (key.startsWith("CB_"))
			{
				attrKey = attrKey.replace("CB_", "");
			}
			else if (key.startsWith("DDL_"))
			{
				attrKey = attrKey.replace("DDL_", "");
			}
			else if (key.startsWith("RB_"))
			{
				attrKey = attrKey.replace("RB_", "");
			}
			else
			{
				//给参数赋值.


				if (key.startsWith("TBPara_"))
				{
					attrKey = attrKey.replace("TBPara_", "");
				}
				else if (key.startsWith("DDLPara_"))
				{
					attrKey = attrKey.replace("DDLPara_", "");
				}
				else if (key.startsWith("RBPara_"))
				{
					attrKey = attrKey.replace("RBPara_", "");
				}
				else if (key.startsWith("CBPara_"))
				{
					attrKey = attrKey.replace("CBPara_", "");
				}
				else
				{
					continue;
				}

			}

			String val = HttpContextHelper.RequestParams(key); // Form[key]
			if (key.indexOf("CB_") == 0 || key.indexOf("CBPara_") == 0)
			{
				en.getRow().put(attrKey, 1);
				continue;
			}

			//其他的属性.
			en.getRow().put(attrKey, val);
		}
		return en;
	}

	/** 
	 明细表传参保存
	 
	 @param en
	 @param pk
	 @param map
	 @return 
	*/
	public static Entity CopyDtlFromRequests(Entity en, String pk, Map map)
	{
		String allKeys = ";";
		if (pk == null || pk.equals(""))
		{
			pk = "";
		}
		else
		{
			pk = "_" + pk;
		}

		for (String myK : HttpContextHelper.getRequestParamKeys())
		{
			allKeys += myK + ";";
		}

		Attrs attrs = map.getAttrs();
		for (Attr attr : attrs)
		{
			String relKey = null;
			switch (attr.getUIContralType())
			{
				case TB:
					relKey = "TB_" + attr.getKey() + pk;
					break;
				case CheckBok:
					relKey = "CB_" + attr.getKey() + pk;
					break;
				case DDL:
					relKey = "DDL_" + attr.getKey() + pk;
					break;
				case RadioBtn:
					relKey = "RB_" + attr.getKey() + pk;
					break;
				case MapPin:
					relKey = "TB_" + attr.getKey() + pk;
					break;
				default:
					break;
			}

			if (relKey == null)
			{
				continue;
			}

			if (allKeys.contains(relKey + ";"))
			{
				/*说明已经找到了这个字段信息。*/
				for (String myK : HttpContextHelper.getRequestParamKeys())
				{
					if (myK == null || myK.equals(""))
					{
						continue;
					}

					if (myK.endsWith(relKey))
					{
						if (attr.getUIContralType() == UIContralType.CheckBok)
						{
							String val = HttpContextHelper.RequestParams(myK);
							if (val.equals("on") || val.equals("1") || val.contains(",on"))
							{
								en.SetValByKey(attr.getKey(), 1);
							}
							else
							{
								en.SetValByKey(attr.getKey(), 0);
							}
						}
						else
						{
							en.SetValByKey(attr.getKey(), HttpContextHelper.RequestParams(myK));
						}
					}
				}
				continue;
			}
		}
		if (map.getIsHaveAutoFull() == false)
		{
			return en;
		}
		en.AutoFull();
		return en;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 外部参数.
	*/
	public static String getRequestParas()
	{
		return BP.NetPlatformImpl.Sys_PubClass.getRequestParas();
	}

	/** 
	 不用page 参数，show message
	 
	 @param mess
	*/
	public static void Alert(String mess)
	{
		//string msg1 = "<script language=javascript>alert('" + msg + "');</script>";
		//if (! System.Web.HttpContext.Current.ClientScript.IsClientScriptBlockRegistered("a "))
		//    ClientScript.RegisterClientScriptBlock(this.GetType(), "a ", msg1);


		String script = "<script language=JavaScript>alert('" + mess + "');</script>";
		HttpContextHelper.ResponseWrite(script);



		//	System.Web.HttpContext.Current.Response.aps ( script );
		//  System.Web.HttpContext.Current.Response.Write(script);
	}

	public static void ResponseWriteScript(String script)
	{
		script = "<script language=JavaScript> " + script + "</script>";
		HttpContextHelper.ResponseWrite(script);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

}