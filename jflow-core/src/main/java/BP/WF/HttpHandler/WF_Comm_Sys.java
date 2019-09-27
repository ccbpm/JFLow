package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Difference.Handler.CommonFileUtils;
import BP.Sys.*;
import BP.Tools.DealString;
import BP.Tools.StringHelper;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Template.*;
import BP.NetPlatformImpl.*;
import BP.WF.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import java.io.*;
import java.text.SimpleDateFormat;
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
		EntityMyPK en = (EntityMyPK)ens.getNewEntity();
		//定义属性.
		Attrs attrs = en.getEnMap().getAttrs();

		int impWay = this.GetRequestValInt("ImpWay");


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
				en = (EntityMyPK)ens.getNewEntity();
				//给实体赋值
				errInfo += SetEntityAttrVal("", dr, attrs, en, dt, 0);
				//获取PKVal
				en.setPKVal(en.InitMyPKVals());
				if (en.RetrieveFromDBSources() == 0)
				{
					en.Insert();
					count++;
					successInfo += "&nbsp;&nbsp;<span>MyPK=" + en.getPKVal() + "的导入成功</span><br/>";
				}

			}
		}


			///#endregion 清空方式导入.


			///#region 更新方式导入
		if (impWay == 1 || impWay == 2)
		{
			for (DataRow dr : dt.Rows)
			{
				en = (EntityMyPK)ens.getNewEntity();
				//给实体赋值
				errInfo += SetEntityAttrVal("", dr, attrs, en, dt, 1);

				//获取PKVal
				en.setPKVal(en.InitMyPKVals());
				if (en.RetrieveFromDBSources() == 0)
				{
					en.Insert();
					count++;
					successInfo += "&nbsp;&nbsp;<span>MyPK=" + en.getPKVal() + "的导入成功</span><br/>";
				}
				else
				{
					changeCount++;
					SetEntityAttrVal("", dr, attrs, en, dt, 1);
					successInfo += "&nbsp;&nbsp;<span>MyPK=" + en.getPKVal() + "的更新成功</span><br/>";
				}
			}
		}

			///#endregion

		return "errInfo=" + errInfo + "@Split" + "count=" + count + "@Split" + "successInfo=" + successInfo + "@Split" + "changeCount=" + changeCount;
	}
	/** 
	 执行导入
	 
	 @return 
	*/
	public final String ImpData_Done()
	{
		HttpServletRequest request = getRequest();
//    	MultipartFile multiFile = ((DefaultMultipartHttpServletRequest)request).getFile("File_Upload");  	
//        if (multiFile==null)
//            return "err@请选择要导入的数据信息。";
    	long filesSize = CommonFileUtils.getFilesSize(request, "File_Upload");
    	if(filesSize == 0){
    		return "err@请选择要导入的数据信息。";
    	}

        String errInfo="";

        String ext = ".xls";
//        String fileName = multiFile.getOriginalFilename();
        String fileName = CommonFileUtils.getOriginalFilename(request, "File_Upload");
        if (fileName.contains(".xlsx"))
            ext = ".xlsx";


        //设置文件名
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String currDate = sdf.format(new Date());
		
        String fileNewName = currDate + ext;
        //文件存放路径
        String filePath = BP.Sys.SystemConfig.getPathOfTemp() + "\\" + fileNewName;
        File tempFile = new File(filePath);
        if(tempFile.exists()){
        	tempFile.delete();
		}
		try {
			//multiFile.transferTo(tempFile);
			CommonFileUtils.upload(request, "File_Upload", tempFile);
		} catch (Exception e) {
			e.printStackTrace();
			return "err@执行失败";
		}


        //从excel里面获得数据表.
        DataTable dt = BP.DA.DBLoad.GetTableByExt(filePath);

        //删除临时文件
        tempFile.delete();

        if (dt.Rows.size()==0)
            return "err@无导入的数据";

        //获得entity.
        Entities ens = ClassFactory.GetEns(this.getEnsName());
        Entity en =ens.getNewEntity();
        
        if(en.getPK().equals("MyPK") == true)
            return this.ImpData_DoneMyPK(ens, dt);
        
        if (en.getIsNoEntity()==false)
            return "err@必须是EntityNo是实体";

        String noColName = ""; //实体列的编号名称.
        String nameColName = ""; //实体列的名字名称.
        BP.En.Map map = en.getEnMap();
        String codeStruct = map.getCodeStruct();
        Attr attr=map.GetAttrByKey("No");
        noColName = attr.getDesc(); //

        attr=en.getEnMap().GetAttrByKey("Name");
        nameColName = attr.getDesc(); //

        //定义属性.
        Attrs attrs = en.getEnMap().getAttrs();

        int impWay = this.GetRequestValInt("ImpWay");

        ///#region 清空方式导入.
        //清空方式导入.
        int count =0;//导入的行数
        int changeCount = 0;//更新的行数
        String successInfo="";
        if (impWay==0)
        {
            ens.ClearTable();
            for(DataRow dr : dt.Rows)
            {
                String no = dr.getValue(noColName).toString();
                //判断是否是自增序列，序列的格式
                if(!StringHelper.isNullOrEmpty(codeStruct)){
                	no = DealString.padLeft(no,Integer.parseInt(codeStruct),'0');
                }
                
                String name = dr.getValue(nameColName).toString();

                EntityNoName myen = (EntityNoName) ens.getNewEntity();
                myen.setNo(no);
                if (myen.getIsExits()==true)
                {
                    errInfo += "err@编号["+no+"]["+name+"]重复.";
                    continue;
                }
               
                myen.setName(name);

                 en = ens.getNewEntity();

                //给实体赋值
                errInfo += SetEntityAttrVal(no,dr, attrs, en, dt,0);
                count++;
                successInfo +="&nbsp;&nbsp;<span>"+noColName+"为"+no+","+nameColName+"为"+name+"的导入成功</span><br/>";
                
            }
        }

        ///#endregion 清空方式导入.


        ///#region 更新方式导入
        if (impWay == 1 || impWay == 2)
        {
            for(DataRow dr : dt.Rows)
            {
            	String no = dr.getValue(noColName).toString();
            	//判断是否是自增序列，序列的格式
                if(!StringHelper.isNullOrEmpty(codeStruct)){
                	no = DealString.padLeft(no,Integer.parseInt(codeStruct),'0');
                }
                String name = dr.getValue(nameColName).toString();

                EntityNoName myen = (EntityNoName) ens.getNewEntity();
                myen.setNo(no);
                if (myen.getIsExits() == true)
                {
                    //给实体赋值
                    errInfo += SetEntityAttrVal(no,dr, attrs, myen, dt,1);
                    changeCount++;
                    successInfo +="&nbsp;&nbsp;<span>"+noColName+"为"+no+","+nameColName+"为"+name+"的更新成功</span><br/>";
                    continue;
                }
                myen.setName(name);

                //给实体赋值
                errInfo += SetEntityAttrVal(no,dr, attrs, en, dt,0);
                count++;
                successInfo +="&nbsp;&nbsp;<span>"+noColName+"为"+no+","+nameColName+"为"+name+"的导入成功</span><br/>";
            }
        }
       /// #endregion

           return "errInfo="+errInfo+"@Split"+"count="+count+"@Split"+"successInfo="+successInfo+"@Split"+"changeCount="+changeCount;

}

	private String SetEntityAttrVal(String no, DataRow dr, Attrs attrs, Entity en, DataTable dt, int saveType)
	{
		String errInfo = "";
		//按照属性赋值.
		for (Attr item : attrs)
		{
			if (item.getKey().equals("No"))
			{
				en.SetValByKey(item.getKey(), no);
				continue;
			}
			if (item.getKey().equals("Name"))
			{
				en.SetValByKey(item.getKey(), dr.get(item.getDesc()).toString());
				continue;
			}


			if (dt.Columns.contains(item.getDesc()) == false)
			{
				continue;
			}

			//枚举处理.
			if (item.getMyFieldType() == FieldType.Enum)
			{
				String val = dr.get(item.getDesc()).toString();

				SysEnum se = new SysEnum();
				int i = se.Retrieve(SysEnumAttr.EnumKey, item.getUIBindKey(), SysEnumAttr.Lab, val);

				if (i == 0)
				{
					errInfo += "err@枚举[" + item.getKey() + "][" + item.getDesc() + "]，值[" + val + "]不存在.";
					continue;
				}

				en.SetValByKey(item.getKey(), se.getIntKey());
				continue;
			}

			//外键处理.
			if (item.getMyFieldType() == FieldType.FK)
			{
				String val = dr.get(item.getDesc()).toString();
				Entity attrEn = item.getHisFKEn();
				int i = attrEn.Retrieve("Name", val);
				if (i == 0)
				{
					errInfo += "err@外键[" + item.getKey() + "][" + item.getDesc() + "]，值[" + val + "]不存在.";
					continue;
				}

				if (i != 1)
				{
					errInfo += "err@外键[" + item.getKey() + "][" + item.getDesc() + "]，值[" + val + "]重复..";
					continue;
				}

				//把编号值给他.
				en.SetValByKey(item.getKey(), attrEn.GetValByKey("No"));
				continue;
			}

			//boolen类型的处理..
			if (item.getMyDataType() == DataType.AppBoolean)
			{
				String val = dr.get(item.getDesc()).toString();
				if (val.equals("是") || val.equals("有"))
				{
					en.SetValByKey(item.getKey(), 1);
				}
				else
				{
					en.SetValByKey(item.getKey(), 0);
				}
				continue;
			}

			String myval = dr.get(item.getDesc()).toString();
			en.SetValByKey(item.getKey(), myval);
		}

		try
		{
			if (en.getIsNoEntity() == true)
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
	public final String SystemClass_EnsCheck()
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
				if (attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK)
				{
					refen = ClassFactory.GetEns(attr.getUIBindKey()).getNewEntity();
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
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	public final String SystemClass_Fields()
	{
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		Entity en = ens.getNewEntity();

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
					html += "<td>表/视图:" + myens.getNewEntity().getEnMap().getPhysicsTable() + " 关联字段:" + attr.getUIRefKeyValue() + "," + attr.getUIRefKeyText() + "</td>";
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

			dr.set("No", en.toString());
			try
			{
				dr.set("EnsName", en.getGetNewEntities().toString());
			}
			catch (java.lang.Exception e2)
			{
				dr.set("EnsName", en.toString() + "s");
			}
			dr.set("Name", en.getEnMap().getEnDesc());
			dr.set("PTable", en.getEnMap().getPhysicsTable());
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
		throw new RuntimeException("@标记[" + this.getDoType() + "]，没有找到. @RowURL:" + this.getRequest().getRequestURL());
	}

		///#endregion 执行父类的重写方法.


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
		src.setNo(this.GetRequestVal("TB_No"));
		if (src.RetrieveFromDBSources() > 0 && this.GetRequestVal("NewOrEdit").equals("New"))
		{
			return ("已经存在数据源编号为“" + src.getNo() + "”的数据源，编号不能重复！");
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
				src.setPassword(this.GetRequestVal("TB_PWword"));
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
		File xmlFile = null;
		String fileName="";
		HttpServletRequest request = getRequest();
		String contentType = request.getContentType();
		if (contentType != null && contentType.indexOf("multipart/form-data") != -1) { 
			fileName = CommonFileUtils.getOriginalFilename(request, "File_Upload");
			
			String savePath = BP.Sys.SystemConfig.getPathOfDataUser()+"JSLibData"+"/"+fileName;
			xmlFile = new File(savePath);
			if(xmlFile.exists()){
				xmlFile.delete();
			}
			
			try {
				CommonFileUtils.upload(request, "File_Upload", xmlFile);
			} catch (Exception e) {
				e.printStackTrace();
				return "err@执行失败";
			}
		}
		return "脚本"+fileName+"导入成功";
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
		String savePath = BP.Sys.SystemConfig.getPathOfDataUser() + "RichTextFile" + "\\" + directory;

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
		String savePath = BP.Sys.SystemConfig.getPathOfDataUser()+"JSLibData";
		File dirFile = new File(savePath);
		if(!dirFile.isDirectory()) 
			return "";
	    File[] fileList = dirFile.listFiles();
	    if(fileList==null||fileList.length==0)
	    	return "";
	    DataTable dt = new DataTable();
	    dt.Columns.Add("FileName");
	    dt.Columns.Add("ChangeTime");
	    for(File file:fileList){
	    	DataRow dr = dt.NewRow();
			dr.setValue("FileName", file.getName());
			dr.setValue("ChangeTime",new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(file.lastModified())));
			dt.Rows.add(dr);
	    }
	    return BP.Tools.Json.ToJson(dt);

	}

		///#endregion
}