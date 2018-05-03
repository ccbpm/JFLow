package BP.WF.HttpHandler;

import java.io.File;

import org.apache.http.protocol.HttpContext;
import org.springframework.util.StringUtils;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.En.Attr;
import BP.En.ClassFactory;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.FieldType;
import BP.Sys.DBSrcType;
import BP.Sys.SFDBSrc;
import BP.Sys.SFDBSrcAttr;
import BP.Sys.SFDBSrcs;
import BP.Sys.SFTable;
import BP.Sys.SFTableAttr;
import BP.Sys.SysEnum;
import BP.Sys.SysEnumAttr;
import BP.Sys.SysEnums;
import BP.Sys.FrmUI.SFTables;
import BP.WF.HttpHandler.Base.WebContralBase;
/** 
 页面功能实体
 
*/
public class WF_Comm_Sys extends WebContralBase
{
	/** 
	 函数库
	 
	 @return 
	*/
	public final String SystemClass_FuncLib()
	{
		String expFileName = "all-wcprops,dir-prop-base,entries";
		String expDirName = ".svn";

		String pathDir = BP.Sys.SystemConfig.getPathOfData() + "/JSLib/";

		String html = "";
		html += "<fieldset>";
		html += "<legend>" + "系统自定义函数. 位置:" + pathDir + "</legend>";


		//.AddFieldSet();
		File dir = new File(pathDir);
		File[] dirs = new File(pathDir).listFiles();
		for (File mydir : dirs)
		{
			if (expDirName.contains(mydir.getName()))
			{
				continue;
			}

			html += "事件名称" + mydir.getName();
			html += "<ul>";
			if(mydir.isDirectory()){
				File[] fls = mydir.listFiles();
				for (File fl : fls)
				{
					if (expFileName.contains(fl.getName()))
					{
						continue;
					}

					html += "<li>" + fl.getName() + "</li>";
				}

			}
			html += "</ul>";
		}
		html += "</fieldset>";

		pathDir = BP.Sys.SystemConfig.getPathOfDataUser() + "/JSLib/";
		html += "<fieldset>";
		html += "<legend>" + "用户自定义函数. 位置:" + pathDir + "</legend>";

		dir = new File(pathDir);
		dirs = dir.listFiles();
		for (File mydir : dirs)
		{
			if (expDirName.contains(mydir.getName()))
			{
				continue;
			}

			html += "事件名称" + mydir.getName();
			html += "<ul>";
			if(mydir.isDirectory()){
				File[] fls = mydir.listFiles();
				for (File fl : fls)
				{
					if (expFileName.contains(fl.getName()))
					{
						continue;
					}
					html += "<li>" + fl.getName() + "</li>";
				}
			}
			html += "</ul>";
		}
		html += "</fieldset>";
		return html;
	}


		///#region 系统实体属性.
	public final String SystemClass_EnsCheck() throws Exception
	{
		try
		{
			BP.En.Entity en = BP.En.ClassFactory.GetEn(this.getEnName());
			BP.En.Map map = en.getEnMap();
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
			for (Attr attr : map.getAttrs())
			{
				//
				if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK)
				{
					refen = ClassFactory.GetEns(attr.getUIBindKey()).getGetNewEntity();
					table = refen.getEnMap().getPhysicsTable();
					sql1 = "SELECT COUNT(*) FROM " + table;

					Attr pkAttr = refen.getEnMap().GetAttrByKey(refen.getPK());
					sql2 = "SELECT COUNT( distinct " + pkAttr.getField() + ") FROM " + table;

					COUNT1 = DBAccess.RunSQLReturnValInt(sql1);
					COUNT2 = DBAccess.RunSQLReturnValInt(sql2);

					if (COUNT1 != COUNT2)
					{
						msg += "<BR>@关联表(" + refen.getEnMap().getEnDesc() + ")主键不唯一，它会造成数据查询不准确或者意向不到的错误：<BR>sql1=" + sql1 + " <BR>sql2=" + sql2;
						msg += "@SQL= SELECT * FROM (  select " + refen.getPK() + ",  COUNT(*) AS NUM  from " + table + " GROUP BY " + refen.getPK() + " ) WHERE NUM!=1";
					}

					sql = "SELECT " + attr.getField() + " FROM " + map.getPhysicsTable() + " WHERE " + attr.getField() + " NOT IN (SELECT " + pkAttr.getField() + " FROM " + table + " )";
					dt = DBAccess.RunSQLReturnTable(sql);
					if (dt.Rows.size() == 0)
					{
						continue;
					}
					else
					{
						msg += "<BR>:有" + dt.Rows.size() + "个错误。" + attr.getDesc() + " sql= " + sql;
					}
				}
				if (attr.getMyFieldType() == FieldType.PKEnum || attr.getMyFieldType() == FieldType.Enum)
				{
					sql = "SELECT " + attr.getField() + " FROM " + map.getPhysicsTable() + " WHERE " + attr.getField() + " NOT IN ( select Intkey from sys_enum WHERE ENUMKEY='" + attr.getUIBindKey() + "' )";
					dt = DBAccess.RunSQLReturnTable(sql);
					if (dt.Rows.size() == 0)
					{
						continue;
					}
					else
					{
						msg += "<BR>:有" + dt.Rows.size() + "个错误。" + attr.getDesc() + " sql= " + sql;
					}
				}
			}

			// 检查pk是否一致。
			if (en.getPKs().length == 1)
			{
				sql1 = "SELECT COUNT(*) FROM " + map.getPhysicsTable();
				COUNT1 = DBAccess.RunSQLReturnValInt(sql1);

				Attr attrMyPK = en.getEnMap().GetAttrByKey(en.getPK());
				sql2 = "SELECT COUNT(DISTINCT " + attrMyPK.getField() + ") FROM " + map.getPhysicsTable();
				COUNT2 = DBAccess.RunSQLReturnValInt(sql2);
				if (COUNT1 != COUNT2)
				{
					msg += "@物理表(" + map.getEnDesc() + ")中主键不唯一;它会造成数据查询不准确或者意向不到的错误：<BR>sql1=" + sql1 + " <BR>sql2=" + sql2;
					msg += "@SQL= SELECT * FROM (  select " + en.getPK() + ",  COUNT(*) AS NUM  from " + map.getPhysicsTable() + " GROUP BY " + en.getPK() + " ) WHERE NUM!=1";
				}
			}

			if (msg.equals(""))
			{
				return map.getEnDesc() + ":数据体检成功,完全正确.";
			}

			String info = map.getEnDesc() + ":数据体检信息：体检失败" + msg;
			return info;

		}
		catch(RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	public final String SystemClass_Fields() throws Exception
	{
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		Entity en = ens.getGetNewEntity();

		BP.En.Map map = en.getEnMap();
		en.CheckPhysicsTable();

		String html = "<table>";

		html += "<caption>数据结构" + map.getEnDesc() + "," + map.getPhysicsTable() + "</caption>";

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
		for (Attr attr : map.getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}
			i++;
			html += "<tr>";
			html += "<td>" + i + "</td>";
			html += "<td>" + attr.getDesc() + "</td>";
			html += "<td>" + attr.getKey() + "</td>";
			html += "<td>" + attr.getField() + "</td>";
			html += "<td>" + attr.getMyDataTypeStr() + "</td>";
			html += "<td>" + attr.getMyFieldType().toString() + "</td>";

			if (attr.getMyDataType() == DataType.AppBoolean || attr.getMyDataType() == DataType.AppDouble || attr.getMyDataType() == DataType.AppFloat || attr.getMyDataType() == DataType.AppInt || attr.getMyDataType() == DataType.AppMoney)
			{
				html += "<td>无</td>";
			}
			else
			{
				html += "<td>" + attr.getMaxLength() + "</td>";
			}


			switch (attr.getMyFieldType())
			{
				case Enum:
				case PKEnum:
					try
					{
						SysEnums ses = new SysEnums(attr.getUIBindKey());
						String str = "";
						for (SysEnum se : ses.ToJavaList())
						{
							str += se.getIntKey() + "&nbsp;" + se.getLab() + ",";
						}
						html += "<td>" + str + "</td>";
					}
					catch (java.lang.Exception e)
					{
						html += "<td>未使用</td>";

					}
					break;
				case FK:
				case PKFK:
					Entities myens = ClassFactory.GetEns(attr.getUIBindKey());
					html += "<td>表/视图:" + myens.getGetNewEntity().getEnMap().getPhysicsTable() + " 关联字段:" + attr.getUIRefKeyValue() + "," + attr.getUIRefKeyText()+"</td>";
					break;
				default:
					html += "<td>无</td>";
					break;
			}

			html += "<td>" + attr.getDefaultVal().toString() + "</td>";
			html += "</tr>";
		}
		html += "</table>";
		return html;
	}

	public final String SystemClass_Init()
	{
		DataTable dt = new DataTable();
		dt.Columns.Add("No",String.class);
		dt.Columns.Add("EnsName",String.class);
		dt.Columns.Add("Name",String.class);
		dt.Columns.Add("PTable",String.class);

		java.util.ArrayList al = null;
		al = BP.En.ClassFactory.GetObjects("BP.En.Entity");
		for (Object obj : al)
		{
			Entity en = null;
			try
			{
				en = (Entity)((obj instanceof Entity) ? obj : null);
				String s = en.getEnDesc();
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

			dr.setValue("No", en.toString() + "");
			try
			{
				dr.setValue("EnsName", en.getGetNewEntities().toString() + "");
			}
			catch (java.lang.Exception e2)
			{
				dr.setValue("EnsName", en.toString()+"s");
			}
			dr.setValue("Name",en.getEnMap().getEnDesc() + "");
			dr.setValue("PTable",en.getEnMap().getPhysicsTable() + "");
			dt.Rows.add(dr);
		}

		return BP.Tools.Json.ToJson(dt);
	}

		///#endregion



		///#region 执行父类的重写方法.
	/** 
	 默认执行的方法
	 
	 @return 
	*/
	@Override
	protected String DoDefaultMethod()
	{
		String sfno = this.getRequest().getParameter("sfno");
		SFTable sftable = null;
		DataTable dt = null;
		StringBuilder s = null;

//C# TO JAVA CONVERTER NOTE: The following 'switch' operated on a string member and was converted to Java 'if-else' logic:
//		switch (this.DoType)
//ORIGINAL LINE: case "DtlFieldUp":
		if (this.getDoType().equals("DtlFieldUp")) //字段上移
		{
				return "执行成功.";


		}
		else
		{
		}

		//找不不到标记就抛出异常.
		throw new RuntimeException("@标记[" + this.getDoType() + "]，没有找到. @RowURL:" + this.getRequest().getRequestURL());
	}

		///#endregion 执行父类的重写方法.


		///#region 数据源管理
	public final String SFDBSrcNewGuide_GetList() throws Exception
	{
		//SysEnums enums = new SysEnums(SFDBSrcAttr.DBSrcType);
		SFDBSrcs srcs = new SFDBSrcs();
		srcs.RetrieveAll();

		return srcs.ToJson();
	}

	public final String SFDBSrcNewGuide_LoadSrc() throws Exception
	{
		DataSet ds = new DataSet();

		SFDBSrc src = new SFDBSrc();
		if (!StringUtils.isEmpty(this.GetRequestVal("No")))
		{
			src = new SFDBSrc();
		}
		ds.Tables.add(src.ToDataTableField("SFDBSrc"));

		SysEnums enums = new SysEnums();
		enums.Retrieve(SysEnumAttr.EnumKey, SFDBSrcAttr.DBSrcType, SysEnumAttr.IntKey);
		ds.Tables.add(enums.ToDataTableField("DBSrcType"));

		return BP.Tools.Json.ToJson(ds);
	}

	public final String SFDBSrcNewGuide_SaveSrc() throws Exception
	{
		SFDBSrc src = new SFDBSrc();
		src.setNo(this.GetRequestVal("TB_No"));
		if (src.RetrieveFromDBSources() > 0 && this.GetRequestVal("NewOrEdit").equals("New"))
		{
			return ("已经存在数据源编号为“" + src.getNo()+ "”的数据源，编号不能重复！");
		}
		src.setName(this.GetRequestVal("TB_Name"));
		src.setDBSrcType(DBSrcType.forValue(this.GetRequestValInt("DDL_DBSrcType")));
		switch (src.getDBSrcType())
		{
			case SQLServer:
			case Oracle:
			case MySQL:
			case Informix:
				if (src.getDBSrcType() != DBSrcType.Oracle)
				{
					src.setDBName(this.GetRequestVal("TB_DBName"));
				}
				else
				{
					src.setDBName("");
				}
				src.setIP(this.GetRequestVal("TB_IP"));
				src.setUserID(this.GetRequestVal("TB_UserID"));
				src.setPassword(this.GetRequestVal("TB_Password"));
				break;
			case WebServices:
				src.setDBName(""); 
				src.setIP(this.GetRequestVal("TB_IP"));
				src.setUserID(""); 
				src.setPassword(""); 
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

	public final String SFDBSrcNewGuide_DelSrc() throws Exception
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

		///#endregion
}