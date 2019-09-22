package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Sys.*;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Template.*;
import BP.NetPlatformImpl.*;
import BP.WF.*;
import java.util.*;
import java.io.*;
import java.time.*;

/** 
 页面功能实体
*/
public class WF_Comm_Sys extends DirectoryPageBase
{
	/** 
	 单元测试
	 
	 @return 
	*/
	public final String UnitTesting_Init()
	{
		DataTable dt = new DataTable();
		dt.Columns.Add("No");
		dt.Columns.Add("Name");
		dt.Columns.Add("Note");

		ArrayList al = null;
		al = BP.En.ClassFactory.GetObjects("BP.UnitTesting.TestBase");
		for (Object obj : al)
		{
			BP.UnitTesting.TestBase en = null;
			try
			{
				en = obj instanceof BP.UnitTesting.TestBase ? (BP.UnitTesting.TestBase)obj : null;
				if (en == null)
				{
					continue;
				}
				String s = en.Title;
				if (en == null)
				{
					continue;
				}
			}
			catch (java.lang.Exception e)
			{
				continue;
			}

			if (en.toString() == null)
			{
				continue;
			}

			DataRow dr = dt.NewRow();
			dr.set("No", en.toString());
			dr.set("Name", en.Title);
			dr.set("Note", en.Note);
			dt.Rows.add(dr);
		}
		return BP.Tools.Json.ToJson(dt);
	}
	public final String UnitTesting_Done()
	{
		try
		{
			BP.UnitTesting.TestBase tc = BP.UnitTesting.Glo.GetTestEntity(this.getEnName());
			tc.Do();
			return "执行成功.<hr>" + tc.Note.replace("\t\n", "@<br>");
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	public final String ImpData_Init()
	{
		return "";
	}
	private String ImpData_DoneMyPK(Entities ens, DataTable dt)
	{
		//错误信息
		String errInfo = "";
		EntityMyPK en = (EntityMyPK)ens.GetNewEntity;
		//定义属性.
		Attrs attrs = en.EnMap.Attrs;

		int impWay = this.GetRequestValInt("ImpWay");

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 清空方式导入.
		//清空方式导入.
		int count = 0; //导入的行数
		int changeCount = 0; //更新数据的行数
		String successInfo = "";
		if (impWay == 0)
		{
			ens.ClearTable();
			for (DataRow dr : dt.Rows)
			{
				en = (EntityMyPK)ens.GetNewEntity;
				//给实体赋值
				errInfo += SetEntityAttrVal("", dr, attrs, en, dt, 0);
				//获取PKVal
				en.PKVal = en.InitMyPKVals();
				if (en.RetrieveFromDBSources() == 0)
				{
					en.Insert();
					count++;
					successInfo += "&nbsp;&nbsp;<span>MyPK=" + en.PKVal + "的导入成功</span><br/>";
				}

			}
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 清空方式导入.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 更新方式导入
		if (impWay == 1 || impWay == 2)
		{
			for (DataRow dr : dt.Rows)
			{
				en = (EntityMyPK)ens.GetNewEntity;
				//给实体赋值
				errInfo += SetEntityAttrVal("", dr, attrs, en, dt, 1);

				//获取PKVal
				en.PKVal = en.InitMyPKVals();
				if (en.RetrieveFromDBSources() == 0)
				{
					en.Insert();
					count++;
					successInfo += "&nbsp;&nbsp;<span>MyPK=" + en.PKVal + "的导入成功</span><br/>";
				}
				else
				{
					changeCount++;
					SetEntityAttrVal("", dr, attrs, en, dt, 1);
					successInfo += "&nbsp;&nbsp;<span>MyPK=" + en.PKVal + "的更新成功</span><br/>";
				}
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

		return "errInfo=" + errInfo + "@Split" + "count=" + count + "@Split" + "successInfo=" + successInfo + "@Split" + "changeCount=" + changeCount;
	}
	/** 
	 执行导入
	 
	 @return 
	*/
	public final String ImpData_Done()
	{

//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
		var files = HttpContextHelper.RequestFiles(); //context.Request.Files;
		if (files.size() == 0)
		{
			return "err@请选择要导入的数据信息。";
		}

		String errInfo = "";

		String ext = ".xls";
		String fileName = (new File(files[0].FileName)).getName();
		if (fileName.contains(".xlsx"))
		{
			ext = ".xlsx";
		}


		//设置文件名
		String fileNewName = LocalDateTime.now().toString("yyyyMMddHHmmssff") + ext;

		//文件存放路径
		String filePath = BP.Sys.SystemConfig.PathOfTemp + "\\" + fileNewName;
		//files[0].SaveAs(filePath);
		HttpContextHelper.UploadFile(files[0], filePath);
		//从excel里面获得数据表.
		DataTable dt = BP.DA.DBLoad.ReadExcelFileToDataTable(filePath);

		//删除临时文件
		(new File(filePath)).delete();

		if (dt.Rows.size() == 0)
		{
			return "err@无导入的数据";
		}

		//获得entity.
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		Entity en = ens.GetNewEntity;

		if (en.PK.equals("MyPK") == true)
		{
			return this.ImpData_DoneMyPK(ens, dt);
		}

		if (en.IsNoEntity == false)
		{
			return "err@必须是EntityNo或者EntityMyPK实体,才能导入.";
		}

		String noColName = ""; //实体列的编号名称.
		String nameColName = ""; //实体列的名字名称.

		Attr attr = en.EnMap.GetAttrByKey("No");
		noColName = attr.Desc;
		BP.En.Map map = en.EnMap;
		String codeStruct = map.CodeStruct;
		attr = map.GetAttrByKey("Name");
		nameColName = attr.Desc;

		//定义属性.
		Attrs attrs = en.EnMap.Attrs;

		int impWay = this.GetRequestValInt("ImpWay");

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 清空方式导入.
		//清空方式导入.
		int count = 0; //导入的行数
		int changeCount = 0; //更新的行数
		String successInfo = "";
		if (impWay == 0)
		{
			ens.ClearTable();
			for (DataRow dr : dt.Rows)
			{
				String no = dr.get(noColName).toString();
				String name = dr.get(nameColName).toString();

				//判断是否是自增序列，序列的格式
				if (!DataType.IsNullOrEmpty(codeStruct))
				{
					no = tangible.StringHelper.padLeft(no, Integer.parseInt(codeStruct), '0');
				}

				EntityNoName myen = ens.GetNewEntity instanceof EntityNoName ? (EntityNoName)ens.GetNewEntity : null;
				myen.No = no;
				if (myen.IsExits == true)
				{
					errInfo += "err@编号[" + no + "][" + name + "]重复.";
					continue;
				}

				myen.Name = name;

				en = ens.GetNewEntity;

				//给实体赋值
				errInfo += SetEntityAttrVal(no, dr, attrs, en, dt, 0);
				count++;
				successInfo += "&nbsp;&nbsp;<span>" + noColName + "为" + no + "," + nameColName + "为" + name + "的导入成功</span><br/>";
			}
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 清空方式导入.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 更新方式导入
		if (impWay == 1 || impWay == 2)
		{
			for (DataRow dr : dt.Rows)
			{
				String no = dr.get(noColName).toString();
				String name = dr.get(nameColName).toString();
				//判断是否是自增序列，序列的格式
				if (!DataType.IsNullOrEmpty(codeStruct))
				{
					no = tangible.StringHelper.padLeft(no, Integer.parseInt(codeStruct), '0');
				}
				EntityNoName myen = ens.GetNewEntity instanceof EntityNoName ? (EntityNoName)ens.GetNewEntity : null;
				myen.No = no;
				if (myen.IsExits == true)
				{
					//给实体赋值
					errInfo += SetEntityAttrVal(no, dr, attrs, myen, dt, 1);
					changeCount++;
					successInfo += "&nbsp;&nbsp;<span>" + noColName + "为" + no + "," + nameColName + "为" + name + "的更新成功</span><br/>";
					continue;
				}
				myen.Name = name;

				//给实体赋值
				errInfo += SetEntityAttrVal(no, dr, attrs, en, dt, 0);
				count++;
				successInfo += "&nbsp;&nbsp;<span>" + noColName + "为" + no + "," + nameColName + "为" + name + "的导入成功</span><br/>";
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

		return "errInfo=" + errInfo + "@Split" + "count=" + count + "@Split" + "successInfo=" + successInfo + "@Split" + "changeCount=" + changeCount;
	}

	private String SetEntityAttrVal(String no, DataRow dr, Attrs attrs, Entity en, DataTable dt, int saveType)
	{
		String errInfo = "";
		//按照属性赋值.
		for (Attr item : attrs)
		{
			if (item.Key.equals("No"))
			{
				en.SetValByKey(item.Key, no);
				continue;
			}
			if (item.Key.equals("Name"))
			{
				en.SetValByKey(item.Key, dr.get(item.Desc).toString());
				continue;
			}


			if (dt.Columns.Contains(item.Desc) == false)
			{
				continue;
			}

			//枚举处理.
			if (item.MyFieldType == FieldType.Enum)
			{
				String val = dr.get(item.Desc).toString();

				SysEnum se = new SysEnum();
				int i = se.Retrieve(SysEnumAttr.EnumKey, item.UIBindKey, SysEnumAttr.Lab, val);

				if (i == 0)
				{
					errInfo += "err@枚举[" + item.Key + "][" + item.Desc + "]，值[" + val + "]不存在.";
					continue;
				}

				en.SetValByKey(item.Key, se.IntKey);
				continue;
			}

			//外键处理.
			if (item.MyFieldType == FieldType.FK)
			{
				String val = dr.get(item.Desc).toString();
				Entity attrEn = item.HisFKEn;
				int i = attrEn.Retrieve("Name", val);
				if (i == 0)
				{
					errInfo += "err@外键[" + item.Key + "][" + item.Desc + "]，值[" + val + "]不存在.";
					continue;
				}

				if (i != 1)
				{
					errInfo += "err@外键[" + item.Key + "][" + item.Desc + "]，值[" + val + "]重复..";
					continue;
				}

				//把编号值给他.
				en.SetValByKey(item.Key, attrEn.GetValByKey("No"));
				continue;
			}

			//boolen类型的处理..
			if (item.MyDataType == DataType.AppBoolean)
			{
				String val = dr.get(item.Desc).toString();
				if (val.equals("是") || val.equals("有"))
				{
					en.SetValByKey(item.Key, 1);
				}
				else
				{
					en.SetValByKey(item.Key, 0);
				}
				continue;
			}

			String myval = dr.get(item.Desc).toString();
			en.SetValByKey(item.Key, myval);
		}

		try
		{
			if (en.IsNoEntity == true)
			{
				if (saveType == 0)
				{
					en.Insert();
				}
				else
				{
					en.Update();
				}
			}

		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}

		return errInfo;
	}


	/** 
	 构造函数
	*/
	public WF_Comm_Sys()
	{
	}
	/** 
	 函数库
	 
	 @return 
	*/
	public final String SystemClass_FuncLib()
	{
		String expFileName = "all-wcprops,dir-prop-base,entries";
		String expDirName = ".svn";

		String pathDir = BP.Sys.SystemConfig.PathOfData + "\\JSLib\\";

		String html = "";
		html += "<fieldset>";
		html += "<legend>" + "系统自定义函数. 位置:" + pathDir + "</legend>";


		//.AddFieldSet();
		File dir = new File(pathDir);
		File[] dirs = dir.GetDirectories();
		for (File mydir : dirs)
		{
			if (expDirName.contains(mydir.getName()))
			{
				continue;
			}

			html += "事件名称" + mydir.getName();
			html += "<ul>";
			File[] fls = mydir.GetFiles();
			for (File fl : fls.ToJavaList())
			{
				if (expFileName.contains(fl.getName()))
				{
					continue;
				}

				html += "<li>" + fl.getName() + "</li>";
			}
			html += "</ul>";
		}
		html += "</fieldset>";

		pathDir = BP.Sys.SystemConfig.PathOfDataUser + "\\JSLib\\";
		html += "<fieldset>";
		html += "<legend>" + "用户自定义函数. 位置:" + pathDir + "</legend>";

		dir = new File(pathDir);
		dirs = dir.GetDirectories();
		for (File mydir : dirs)
		{
			if (expDirName.contains(mydir.getName()))
			{
				continue;
			}

			html += "事件名称" + mydir.getName();
			html += "<ul>";
			File[] fls = mydir.GetFiles();
			for (File fl : fls.ToJavaList())
			{
				if (expFileName.contains(fl.getName()))
				{
					continue;
				}
				html += "<li>" + fl.getName() + "</li>";
			}
			html += "</ul>";
		}
		html += "</fieldset>";
		return html;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 系统实体属性.
	public final String SystemClass_EnsCheck()
	{
		try
		{
			BP.En.Entity en = BP.En.ClassFactory.GetEn(this.getEnName());
			BP.En.Map map = en.EnMap;
			en.CheckPhysicsTable();
			String msg = "";
			// string msg = "";
			String table = "";
			String sql = "";
			String sql1 = "";
			String sql2 = "";
			int COUNT1 = 0;
			int COUNT2 = 0;

			DataTable dt = new DataTable();
			Entity refen = null;
			for (Attr attr : map.Attrs)
			{
				if (attr.MyFieldType == FieldType.FK || attr.MyFieldType == FieldType.PKFK)
				{
					refen = ClassFactory.GetEns(attr.UIBindKey).GetNewEntity;
					table = refen.EnMap.PhysicsTable;
					sql1 = "SELECT COUNT(*) FROM " + table;

					Attr pkAttr = refen.EnMap.GetAttrByKey(refen.PK);
					sql2 = "SELECT COUNT( distinct " + pkAttr.Field + ") FROM " + table;

					COUNT1 = DBAccess.RunSQLReturnValInt(sql1);
					COUNT2 = DBAccess.RunSQLReturnValInt(sql2);

					if (COUNT1 != COUNT2)
					{
						msg += "<BR>@关联表(" + refen.EnMap.EnDesc + ")主键不唯一，它会造成数据查询不准确或者意向不到的错误：<BR>sql1=" + sql1 + " <BR>sql2=" + sql2;
						msg += "@SQL= SELECT * FROM (  select " + refen.PK + ",  COUNT(*) AS NUM  from " + table + " GROUP BY " + refen.PK + " ) WHERE NUM!=1";
					}

					sql = "SELECT " + attr.Field + " FROM " + map.PhysicsTable + " WHERE " + attr.Field + " NOT IN (SELECT " + pkAttr.Field + " FROM " + table + " )";
					dt = DBAccess.RunSQLReturnTable(sql);
					if (dt.Rows.size() == 0)
					{
						continue;
					}
					else
					{
						msg += "<BR>:有" + dt.Rows.size() + "个错误。" + attr.Desc + " sql= " + sql;
					}
				}
				if (attr.MyFieldType == FieldType.PKEnum || attr.MyFieldType == FieldType.Enum)
				{
					sql = "SELECT " + attr.Field + " FROM " + map.PhysicsTable + " WHERE " + attr.Field + " NOT IN ( select Intkey from sys_enum WHERE ENUMKEY='" + attr.UIBindKey + "' )";
					dt = DBAccess.RunSQLReturnTable(sql);
					if (dt.Rows.size() == 0)
					{
						continue;
					}
					else
					{
						msg += "<BR>:有" + dt.Rows.size() + "个错误。" + attr.Desc + " sql= " + sql;
					}
				}
			}

			// 检查pk是否一致。
			if (en.PKs.Length == 1)
			{
				sql1 = "SELECT COUNT(*) FROM " + map.PhysicsTable;
				COUNT1 = DBAccess.RunSQLReturnValInt(sql1);

				Attr attrMyPK = en.EnMap.GetAttrByKey(en.PK);
				sql2 = "SELECT COUNT(DISTINCT " + attrMyPK.Field + ") FROM " + map.PhysicsTable;
				COUNT2 = DBAccess.RunSQLReturnValInt(sql2);
				if (COUNT1 != COUNT2)
				{
					msg += "@物理表(" + map.EnDesc + ")中主键不唯一;它会造成数据查询不准确或者意向不到的错误：<BR>sql1=" + sql1 + " <BR>sql2=" + sql2;
					msg += "@SQL= SELECT * FROM (  select " + en.PK + ",  COUNT(*) AS NUM  from " + map.PhysicsTable + " GROUP BY " + en.PK + " ) WHERE NUM!=1";
				}
			}

			if (msg.equals(""))
			{
				return map.EnDesc + ":数据体检成功,完全正确.";
			}

			String info = map.EnDesc + ":数据体检信息：体检失败" + msg;
			return info;

		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	public final String SystemClass_Fields()
	{
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		Entity en = ens.GetNewEntity;

		BP.En.Map map = en.EnMap;
		en.CheckPhysicsTable();

		String html = "<table>";

		html += "<caption>数据结构" + map.EnDesc + "," + map.PhysicsTable + "</caption>";

		html += "<tr>";
		html += "<th>序号</th>";
		html += "<th>描述</th>";
		html += "<th>属性</th>";
		html += "<th>物理字段</th>";
		html += "<th>数据类型</th>";
		html += "<th>关系类型</th>";
		html += "<th>长度</th>";
		html += "<th>对应</th>";
		html += "<th>默认值</th>";
		html += "</tr>";

		int i = 0;
		for (Attr attr : map.Attrs)
		{
			if (attr.MyFieldType == FieldType.RefText)
			{
				continue;
			}
			i++;
			html += "<tr>";
			html += "<td>" + i + "</td>";
			html += "<td>" + attr.Desc + "</td>";
			html += "<td>" + attr.Key + "</td>";
			html += "<td>" + attr.Field + "</td>";
			html += "<td>" + attr.MyDataTypeStr + "</td>";
			html += "<td>" + attr.MyFieldType.toString() + "</td>";

			if (attr.MyDataType == DataType.AppBoolean || attr.MyDataType == DataType.AppDouble || attr.MyDataType == DataType.AppFloat || attr.MyDataType == DataType.AppInt || attr.MyDataType == DataType.AppMoney)
			{
				html += "<td>无</td>";
			}
			else
			{
				html += "<td>" + attr.MaxLength + "</td>";
			}


			switch (attr.MyFieldType)
			{
				case FieldType.Enum:
				case FieldType.PKEnum:
					try
					{
						SysEnums ses = new SysEnums(attr.UIBindKey);
						String str = "";
						for (SysEnum se : ses)
						{
							str += se.IntKey + "&nbsp;" + se.Lab + ",";
						}
						html += "<td>" + str + "</td>";
					}
					catch (java.lang.Exception e)
					{
						html += "<td>未使用</td>";

					}
					break;
				case FieldType.FK:
				case FieldType.PKFK:
					Entities myens = ClassFactory.GetEns(attr.UIBindKey);
					html += "<td>表/视图:" + myens.GetNewEntity.EnMap.PhysicsTable + " 关联字段:" + attr.UIRefKeyValue + "," + attr.UIRefKeyText + "</td>";
					break;
				default:
					html += "<td>无</td>";
					break;
			}

			html += "<td>" + attr.DefaultVal.toString() + "</td>";
			html += "</tr>";
		}
		html += "</table>";
		return html;
	}

	public final String SystemClass_Init()
	{
		DataTable dt = new DataTable();
		dt.Columns.Add("No");
		dt.Columns.Add("EnsName");
		dt.Columns.Add("Name");
		dt.Columns.Add("PTable");

		ArrayList al = null;
		al = BP.En.ClassFactory.GetObjects("BP.En.Entity");
		for (Object obj : al)
		{
			Entity en = null;
			try
			{
				en = obj instanceof Entity ? (Entity)obj : null;
				String s = en.EnDesc;
				if (en == null)
				{
					continue;
				}
			}
			catch (java.lang.Exception e)
			{
				continue;
			}

			if (en.toString() == null)
			{
				continue;
			}


			DataRow dr = dt.NewRow();

			dr.set("No", en.toString());
			try
			{
				dr.set("EnsName", en.GetNewEntities.toString());
			}
			catch (java.lang.Exception e2)
			{
				dr.set("EnsName", en.toString() + "s");
			}
			dr.set("Name", en.EnMap.EnDesc);
			dr.set("PTable", en.EnMap.PhysicsTable);
			dt.Rows.add(dr);
		}

		return BP.Tools.Json.ToJson(dt);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 执行父类的重写方法.
	/** 
	 默认执行的方法
	 
	 @return 
	*/
	@Override
	protected String DoDefaultMethod()
	{
		String sfno = this.GetRequestVal("sfno");
		SFTable sftable = null;
		DataTable dt = null;
		StringBuilder s = null;

		switch (this.getDoType())
		{
			case "DtlFieldUp": //字段上移
				return "执行成功.";


			default:
				break;
		}

		//找不不到标记就抛出异常.
		throw new RuntimeException("@标记[" + this.getDoType() + "]，没有找到. @RowURL:" + HttpContextHelper.RequestRawUrl);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 执行父类的重写方法.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 数据源管理
	public final String SFDBSrcNewGuide_GetList()
	{
		//SysEnums enums = new SysEnums(SFDBSrcAttr.DBSrcType);
		SFDBSrcs srcs = new SFDBSrcs();
		srcs.RetrieveAll();

		return srcs.ToJson();
	}

	public final String SFDBSrcNewGuide_LoadSrc()
	{
		DataSet ds = new DataSet();

		SFDBSrc src = new SFDBSrc();
		if (!DataType.IsNullOrEmpty(this.GetRequestVal("No")))
		{
			src = new SFDBSrc(getNo());
		}
		ds.Tables.add(src.ToDataTableField("SFDBSrc"));

		SysEnums enums = new SysEnums();
		enums.Retrieve(SysEnumAttr.EnumKey, SFDBSrcAttr.DBSrcType, SysEnumAttr.IntKey);
		ds.Tables.add(enums.ToDataTableField("DBSrcType"));

		return BP.Tools.Json.ToJson(ds);
	}

	public final String SFDBSrcNewGuide_SaveSrc()
	{
		SFDBSrc src = new SFDBSrc();
		src.No = this.GetRequestVal("TB_No");
		if (src.RetrieveFromDBSources() > 0 && this.GetRequestVal("NewOrEdit").equals("New"))
		{
			return ("已经存在数据源编号为“" + src.No + "”的数据源，编号不能重复！");
		}
		src.Name = this.GetRequestVal("TB_Name");
		src.DBSrcType = (DBSrcType)this.GetRequestValInt("DDL_DBSrcType");
		switch (src.DBSrcType)
		{
			case DBSrcType.SQLServer:
			case DBSrcType.Oracle:
			case DBSrcType.MySQL:
			case DBSrcType.Informix:
				if (src.DBSrcType != DBSrcType.Oracle)
				{
					src.DBName = this.GetRequestVal("TB_DBName");
				}
				else
				{
					src.DBName = "";
				}
				src.IP = this.GetRequestVal("TB_IP");
				src.UserID = this.GetRequestVal("TB_UserID");
				src.Password = this.GetRequestVal("TB_PWword");
				break;
			case DBSrcType.WebServices:
				src.DBName = "";
				src.IP = this.GetRequestVal("TB_IP");
				src.UserID = "";
				src.Password = "";
				break;
			default:
				break;
		}
		//测试是否连接成功，如果连接不成功，则不允许保存。
		String testResult = src.DoConn();

		if (testResult.indexOf("连接配置成功") == -1)
		{
			return (testResult + ".保存失败！");
		}

		src.Save();

		return "保存成功..";
	}

	public final String SFDBSrcNewGuide_DelSrc()
	{
		String no = this.GetRequestVal("No");

		//检验要删除的数据源是否有引用
		SFTables sfs = new SFTables();
		sfs.Retrieve(SFTableAttr.FK_SFDBSrc, no);

		if (sfs.size() > 0)
		{
			//Alert("当前数据源已经使用，不能删除！");
			return "当前数据源已经使用，不能删除！";
			//return;
		}
		SFDBSrc src = new SFDBSrc(no);
		src.Delete();
		return "删除成功..";
	}

	//javaScript 脚本上传
	public final String javaScriptImp_Done()
	{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
		var files = HttpContextHelper.RequestFiles(); //context.Request.Files;
		if (files.size() == 0)
		{
			return "err@请选择要上传的流程模版。";
		}
		String fileName = files[0].FileName;
		String savePath = BP.Sys.SystemConfig.PathOfDataUser + "JSLibData" + "\\" + fileName;

		//存在文件则删除
		if ((new File(savePath)).isDirectory() == true)
		{
			(new File(savePath)).delete();
		}

		//files[0].SaveAs(savePath);
		HttpContextHelper.UploadFile(files[0], savePath);
		return "脚本" + fileName + "导入成功";
	}

	public final String RichUploadFile()
	{
		//HttpFileCollection files = context.Request.Files;
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
		var files = HttpContextHelper.RequestFiles();
		if (files.size() == 0)
		{
			return "err@请选择要上传的图片。";
		}
		//获取文件存放目录
		String directory = this.GetRequestVal("Directory");
		String fileName = files[0].FileName;
		String savePath = BP.Sys.SystemConfig.PathOfDataUser + "RichTextFile" + "\\" + directory;

		if ((new File(savePath)).isDirectory() == false)
		{
			(new File(savePath)).mkdirs();
		}

		savePath = savePath + "\\" + fileName;
		//存在文件则删除
		if ((new File(savePath)).isDirectory() == true)
		{
			(new File(savePath)).delete();
		}

		//files[0].SaveAs(savePath);
		HttpContextHelper.UploadFile(files[0], savePath);
		return savePath;
	}

	/**
	 * 获取已知目录下的文件列表
	 * @return
	 */
	public final String javaScriptFiles()
	{
		String savePath = BP.Sys.SystemConfig.PathOfDataUser + "JSLibData";

		File di = new File(savePath);
		//找到该目录下的文件 
		File[] fileList = di.GetFiles();

		if (fileList == null || fileList.length == 0)
		{
			return "";
		}
		DataTable dt = new DataTable();
		dt.Columns.Add("FileName");
		dt.Columns.Add("ChangeTime");
		for (File file : fileList)
		{
			DataRow dr = dt.NewRow();
			dr.set("FileName", file.getName());
			dr.set("ChangeTime", file.LastAccessTime.toString());

			dt.Rows.add(dr);
		}
		return BP.Tools.Json.ToJson(dt);

	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}