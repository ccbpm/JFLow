package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Sys.*;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Template.*;
import ICSharpCode.SharpZipLib.Zip.*;
import BP.WF.*;
import java.io.*;
import java.nio.file.*;

/** 
 页面功能实体
*/
public class WF_CommEntity extends DirectoryPageBase
{

	/** 
	 构造函数
	*/
	public WF_CommEntity()
	{
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 从表.
	/** 
	 初始化
	 
	 @return 
	*/
	public final String Dtl_Save()
	{
		try
		{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region  查询出来从表数据.
			Entities dtls = ClassFactory.GetEns(this.getEnsName());
			Entity dtl = dtls.GetNewEntity;
			dtls.Retrieve(this.GetRequestVal("RefKey"), this.GetRequestVal("RefVal"));
			Map map = dtl.EnMap;
			for (Entity item : dtls)
			{
				String pkval = item.GetValStringByKey(dtl.PK);
				for (Attr attr : map.Attrs)
				{
					if (attr.IsRefAttr == true)
					{
						continue;
					}

					if (attr.MyDataType == DataType.AppDateTime || attr.MyDataType == DataType.AppDate)
					{
						if (attr.UIIsReadonly == true)
						{
							continue;
						}

						String val = this.GetValFromFrmByKey("TB_" + pkval + "_" + attr.Key, null);
						item.SetValByKey(attr.Key, val);
						continue;
					}


					if (attr.UIContralType == UIContralType.TB && attr.UIIsReadonly == false)
					{
						String val = this.GetValFromFrmByKey("TB_" + pkval + "_" + attr.Key, null);
						item.SetValByKey(attr.Key, val);
						continue;
					}

					if (attr.UIContralType == UIContralType.DDL && attr.UIIsReadonly == true)
					{
						String val = this.GetValFromFrmByKey("DDL_" + pkval + "_" + attr.Key);
						item.SetValByKey(attr.Key, val);
						continue;
					}

					if (attr.UIContralType == UIContralType.CheckBok && attr.UIIsReadonly == true)
					{
						String val = this.GetValFromFrmByKey("CB_" + pkval + "_" + attr.Key, "-1");
						if (val.equals("-1"))
						{
							item.SetValByKey(attr.Key, 0);
						}
						else
						{
							item.SetValByKey(attr.Key, 1);
						}
						continue;
					}
				}

				item.Update(); //执行更新.
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion  查询出来从表数据.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 保存新加行.
			int newRowCount = this.GetRequestValInt("NewRowCount");
			for (int i = 0; i < newRowCount; i++)
			{
				String val = "";
				for (Attr attr : map.Attrs)
				{

					if (attr.MyDataType == DataType.AppDateTime || attr.MyDataType == DataType.AppDate)
					{
						if (attr.UIIsReadonly == true)
						{
							continue;
						}

						val = this.GetValFromFrmByKey("TB_" + i + "_" + attr.Key, null);
						dtl.SetValByKey(attr.Key, val);
						continue;
					}


					if (attr.UIContralType == UIContralType.TB && attr.UIIsReadonly == false)
					{
						val = this.GetValFromFrmByKey("TB_" + i + "_" + attr.Key);
						if (attr.IsNum && val.equals(""))
						{
							val = "0";
						}
						 dtl.SetValByKey(attr.Key, val);
						 continue;
					}

					if (attr.UIContralType == UIContralType.DDL && attr.UIIsReadonly == true)
					{
						val = this.GetValFromFrmByKey("DDL_" + i + "_" + attr.Key);
						dtl.SetValByKey(attr.Key, val);
						continue;
					}

					if (attr.UIContralType == UIContralType.CheckBok && attr.UIIsReadonly == true)
					{
						val = this.GetValFromFrmByKey("CB_" + i + "_" + attr.Key, "-1");
						if (val.equals("-1"))
						{
							dtl.SetValByKey(attr.Key, 0);
						}
						else
						{
							dtl.SetValByKey(attr.Key, 1);
						}
						continue;
					}
				}
				//dtl.SetValByKey(pkval, 0);
				dtl.SetValByKey(this.GetRequestVal("RefKey"), this.GetRequestVal("RefVal"));
				dtl.PKVal = "0";
				dtl.Insert();
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 保存新加行.

			return "保存成功.";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 保存
	 
	 @return 
	*/
	public final String Dtl_Init()
	{
		//定义容器.
		DataSet ds = new DataSet();

		//查询出来从表数据.
		Entities dtls = ClassFactory.GetEns(this.getEnsName());
		dtls.Retrieve(this.GetRequestVal("RefKey"), this.GetRequestVal("RefVal"));
		ds.Tables.Add(dtls.ToDataTableField("Dtls"));

		//实体.
		Entity dtl = dtls.GetNewEntity;
		//定义Sys_MapData.
		MapData md = new MapData();
		md.No = this.getEnName();
		md.Name = dtl.EnDesc;

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 加入权限信息.
		//把权限加入参数里面.
		if (dtl.HisUAC.IsInsert)
		{
			md.SetPara("IsInsert", "1");
		}
		if (dtl.HisUAC.IsUpdate)
		{
			md.SetPara("IsUpdate", "1");
		}
		if (dtl.HisUAC.IsDelete)
		{
			md.SetPara("IsDelete", "1");
		}
		if (dtl.HisUAC.IsImp)
		{
			md.SetPara("IsImp", "1");
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 加入权限信息.

		ds.Tables.Add(md.ToDataTableField("Sys_MapData"));

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 字段属性.
		MapAttrs attrs = dtl.EnMap.Attrs.ToMapAttrs;
		DataTable sys_MapAttrs = attrs.ToDataTableField("Sys_MapAttr");
		ds.Tables.Add(sys_MapAttrs);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 字段属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 把外键与枚举放入里面去.
		for (DataRow dr : sys_MapAttrs.Rows)
		{
			String uiBindKey = dr.get("UIBindKey").toString();
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

			if (DataType.IsNullOrEmpty(uiBindKey) == true)
			{
				String myPK = dr.get("MyPK").toString();
				/*如果是空的*/
				//   throw new Exception("@属性字段数据不完整，流程:" + fl.No + fl.Name + ",节点:" + nd.NodeID + nd.Name + ",属性:" + myPK + ",的UIBindKey IsNull ");
			}

			// 检查是否有下拉框自动填充。
			String keyOfEn = dr.get("KeyOfEn").toString();
			String fk_mapData = dr.get("FK_MapData").toString();


			// 判断是否存在.
			if (ds.Tables.Contains(uiBindKey) == true)
			{
				continue;
			}

			ds.Tables.Add(BP.Sys.PubClass.GetDataTableByUIBineKey(uiBindKey));
		}

		for (Attr attr : dtl.EnMap.Attrs)
		{
			if (attr.IsRefAttr == true)
			{
				continue;
			}

			if (DataType.IsNullOrEmpty(attr.UIBindKey) || attr.UIBindKey.Length <= 10)
			{
				continue;
			}

			if (attr.UIIsReadonly == true)
			{
				continue;
			}

			if (attr.UIBindKey.Contains("SELECT") == true || attr.UIBindKey.Contains("select") == true)
			{
				/*是一个sql*/
				Object tempVar = attr.UIBindKey.Clone();
				String sqlBindKey = tempVar instanceof String ? (String)tempVar : null;

				// 判断是否存在.
				if (ds.Tables.Contains(sqlBindKey) == true)
				{
					continue;
				}

				sqlBindKey = BP.WF.Glo.DealExp(sqlBindKey, null, null);

				DataTable dt = DBAccess.RunSQLReturnTable(sqlBindKey);
				dt.TableName = attr.Key;

				//@杜. 翻译当前部分.
				if (SystemConfig.AppCenterDBType == DBType.Oracle || SystemConfig.AppCenterDBType == DBType.PostgreSQL)
				{
					dt.Columns["NO"].ColumnName = "No";
					dt.Columns["NAME"].ColumnName = "Name";
				}

				ds.Tables.Add(dt);
			}
		}

		String enumKeys = "";
		for (Attr attr : dtl.EnMap.Attrs)
		{
			if (attr.MyFieldType == FieldType.Enum)
			{
				enumKeys += "'" + attr.UIBindKey + "',";
			}
		}

		if (enumKeys.length() > 2)
		{
			enumKeys = enumKeys.substring(0, enumKeys.length() - 1);
			// Sys_Enum
			String sqlEnum = "SELECT * FROM Sys_Enum WHERE EnumKey IN (" + enumKeys + ")";
			DataTable dtEnum = DBAccess.RunSQLReturnTable(sqlEnum);
			dtEnum.TableName = "Sys_Enum";

			if (SystemConfig.AppCenterDBType == DBType.Oracle || SystemConfig.AppCenterDBType == DBType.PostgreSQL)
			{
				dtEnum.Columns["MYPK"].ColumnName = "MyPK";
				dtEnum.Columns["LAB"].ColumnName = "Lab";
				dtEnum.Columns["ENUMKEY"].ColumnName = "EnumKey";
				dtEnum.Columns["INTKEY"].ColumnName = "IntKey";
				dtEnum.Columns["LANG"].ColumnName = "Lang";
			}
			ds.Tables.Add(dtEnum);
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 把外键与枚举放入里面去.

		return BP.Tools.Json.ToJson(ds);
	}

	public final String Dtl_Exp()
	{
		String refPKVal = this.GetRequestVal("RefVal");
		Entities dtls = ClassFactory.GetEns(this.getEnsName());
		dtls.Retrieve(this.GetRequestVal("RefKey"), this.GetRequestVal("RefVal"));
		Entity en = dtls.GetNewEntity;
		String name = "数据导出";
		if (refPKVal.contains("/") == true)
		{
			refPKVal = refPKVal.replace("/", "_");
		}
		String filename = refPKVal + "_" + en.toString() + "_" + DataType.CurrentData + "_" + name + ".xls";
		String filePath = ExportDGToExcel(dtls.ToDataTableField(), en, name, null, filename);

		filePath = BP.Sys.SystemConfig.PathOfTemp + filename;

		String tempPath = BP.Sys.SystemConfig.PathOfTemp + refPKVal + "\\";
		if ((new File(tempPath)).isDirectory() == false)
		{
			(new File(tempPath)).mkdirs();
		}

		String myFilePath = BP.Sys.SystemConfig.PathOfDataUser + this.getEnsName().substring(0, this.getEnsName().length() - 1);

		for (Entity dt : dtls)
		{
			String pkval = dt.PKVal.toString();
			Object tempVar = dt.GetValByKey("MyFileExt");
			String ext = tangible.StringHelper.isNullOrWhiteSpace(tempVar instanceof String ? (String)tempVar : null) ? "" : dt.GetValByKey("MyFileExt").toString();
			if (DataType.IsNullOrEmpty(ext) == true)
			{
				continue;
			}
			myFilePath = myFilePath + "\\" + pkval + "." + ext;
			if ((new File(myFilePath)).isFile() == true)
			{
				Files.copy(Paths.get(myFilePath), Paths.get(tempPath + pkval + "." + ext), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
			}
		}
		Files.copy(Paths.get(filePath), Paths.get(tempPath + filename), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);

		//生成压缩文件
		String zipFile = BP.Sys.SystemConfig.PathOfTemp + refPKVal + "_" + en.toString() + "_" + DataType.CurrentData + "_" + name + ".zip";

		File finfo = new File(zipFile);

		(new FastZip()).CreateZip(finfo.getPath(), tempPath, true, "");

		return "/DataUser/Temp/" + refPKVal + "_" + en.toString() + "_" + DataType.CurrentData + "_" + name + ".zip";
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 从表.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 实体的操作.
	/** 
	 实体初始化
	 
	 @return 
	*/
	public final String EntityOnly_Init()
	{
		try
		{
			//是否是空白记录.
			boolean isBlank = DataType.IsNullOrEmpty(this.getPKVal());

			//初始化entity.
			String enName = this.getEnName();
			Entity en = null;
			if (isBlank == true)
			{
				if (DataType.IsNullOrEmpty(this.getEnsName()) == true)
				{
					return "err@类名没有传递过来";
				}
				Entities ens = ClassFactory.GetEns(this.getEnsName());

				if (ens == null)
				{
					return "err@类名错误" + this.getEnsName(); //@李国文.
				}

				en = ens.GetNewEntity;
			}
			else
			{
				en = ClassFactory.GetEn(this.getEnName());
			}

			if (en == null)
			{
				return "err@参数类名不正确.";
			}

			//获得描述.
			Map map = en.EnMap;
			String pkVal = this.getPKVal();
			if (isBlank == false)
			{
				en.PKVal = pkVal;
				int i = en.RetrieveFromDBSources();
				if (i == 0)
				{
					return "err@数据[" + map.EnDesc + "]主键为[" + pkVal + "]不存在，或者没有保存。";
				}
			}
			else
			{
				for (Attr attr : en.EnMap.Attrs)
				{
					en.SetValByKey(attr.Key, attr.DefaultVal);
				}

				//设置默认的数据.
				en.ResetDefaultVal();

				en.SetValByKey("RefPKVal", this.getRefPKVal());

				//自动生成一个编号.
				if (en.IsNoEntity == true && en.EnMap.IsAutoGenerNo == true)
				{
					en.SetValByKey("No", en.GenerNewNoByKey("No"));
				}
			}


			//定义容器.
			DataSet ds = new DataSet();

			//定义Sys_MapData.
			MapData md = new MapData();
			md.No = this.getEnName();
			md.Name = map.EnDesc;

			//附件类型.
			md.SetPara("BPEntityAthType", (int)map.HisBPEntityAthType);


			//多附件上传
			if ((int)map.HisBPEntityAthType == 2)
			{
				//增加附件分类
				DataTable attrFiles = new DataTable("AttrFiles");
				attrFiles.Columns.Add("FileNo");
				attrFiles.Columns.Add("FileName");
				for (AttrFile attrFile : map.HisAttrFiles)
				{
					DataRow dr = attrFiles.NewRow();
					dr.set("FileNo", attrFile.FileNo);
					dr.set("FileName", attrFile.FileName);
					attrFiles.Rows.Add(dr);
				}
				ds.Tables.Add(attrFiles);

				//增加附件列表
				SysFileManagers sfs = new SysFileManagers(en.toString(), en.PKVal.toString());
				ds.Tables.Add(sfs.ToDataTableField("Sys_FileManager"));
			}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 加入权限信息.
			//把权限加入参数里面.
			if (en.HisUAC.IsInsert)
			{
				md.SetPara("IsInsert", "1");
			}
			if (en.HisUAC.IsUpdate)
			{
				md.SetPara("IsUpdate", "1");
			}
			if (isBlank == true)
			{
				if (en.HisUAC.IsDelete)
				{
					md.SetPara("IsDelete", "0");
				}
			}
			else
			{
				if (en.HisUAC.IsDelete)
				{
					md.SetPara("IsDelete", "1");
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 加入权限信息.


			ds.Tables.Add(md.ToDataTableField("Sys_MapData"));

			//把主数据放入里面去.
			DataTable dtMain = en.ToDataTableField("MainTable");
			ds.Tables.Add(dtMain);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 增加上分组信息.
			EnCfg ec = new EnCfg(this.getEnName());
			String groupTitle = ec.GroupTitle;
			if (DataType.IsNullOrEmpty(groupTitle) == true)
			{
				groupTitle = "@" + en.PK + ",基本信息," + map.EnDesc + "";
			}

			//增加上.
			DataTable dtGroups = new DataTable("Sys_GroupField");
			dtGroups.Columns.Add("OID");
			dtGroups.Columns.Add("Lab");
			dtGroups.Columns.Add("Tip");
			dtGroups.Columns.Add("CtrlType");
			dtGroups.Columns.Add("CtrlID");

			String[] strs = groupTitle.split("[@]", -1);
			for (String str : strs)
			{
				if (DataType.IsNullOrEmpty(str))
				{
					continue;
				}

				String[] vals = str.split("[=]", -1);
				if (vals.length == 1)
				{
					vals = str.split("[,]", -1);
				}

				if (vals.length == 0)
				{
					continue;
				}

				DataRow dr = dtGroups.NewRow();
				dr.set("OID", vals[0]);
				dr.set("Lab", vals[1]);
				if (vals.length == 3)
				{
					dr.set("Tip", vals[2]);
				}
				dtGroups.Rows.Add(dr);
			}
			ds.Tables.Add(dtGroups);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 增加上分组信息.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 字段属性.
			MapAttrs attrs = en.EnMap.Attrs.ToMapAttrs;
			DataTable sys_MapAttrs = attrs.ToDataTableField("Sys_MapAttr");
			sys_MapAttrs.Columns.Remove(MapAttrAttr.GroupID);
			sys_MapAttrs.Columns.Add("GroupID");


			//sys_MapAttrs.Columns[MapAttrAttr.GroupID].DataType = typeof(string); //改变列类型.

			//给字段增加分组.
			String currGroupID = "";
			for (DataRow drAttr : sys_MapAttrs.Rows)
			{
				if (currGroupID.equals("") == true)
				{
					currGroupID = dtGroups.Rows[0]["OID"].toString();
				}

				String keyOfEn = drAttr.get(MapAttrAttr.KeyOfEn).toString();
				for (DataRow drGroup : dtGroups.Rows)
				{
					String field = drGroup.get("OID").toString();
					if (keyOfEn.equals(field))
					{
						currGroupID = field;
					}
				}
				drAttr.set(MapAttrAttr.GroupID, currGroupID);
			}
			ds.Tables.Add(sys_MapAttrs);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 字段属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 加入扩展属性.
			MapExts mapExts = new MapExts(this.getEnName() + "s");
			DataTable Sys_MapExt = mapExts.ToDataTableField("Sys_MapExt");
			ds.Tables.Add(Sys_MapExt);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 加入扩展属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 把外键与枚举放入里面去.

			//加入外键.
			for (DataRow dr : sys_MapAttrs.Rows)
			{
				String uiBindKey = dr.get("UIBindKey").toString();
				String lgType = dr.get("LGType").toString();
				if (lgType.equals("2") == false)
				{
					continue;
				}

				String UIIsEnable = dr.get("UIVisible").toString();

				if (UIIsEnable.equals("0") == true)
				{
					continue;
				}

				if (DataType.IsNullOrEmpty(uiBindKey) == true)
				{
					String myPK = dr.get("MyPK").toString();
					/*如果是空的*/
					//   throw new Exception("@属性字段数据不完整，流程:" + fl.No + fl.Name + ",节点:" + nd.NodeID + nd.Name + ",属性:" + myPK + ",的UIBindKey IsNull ");
				}

				// 检查是否有下拉框自动填充。
				String keyOfEn = dr.get("KeyOfEn").toString();
				String fk_mapData = dr.get("FK_MapData").toString();

				// 判断是否存在.
				if (ds.Tables.Contains(uiBindKey) == true)
				{
					continue;
				}

				DataTable dt = BP.Sys.PubClass.GetDataTableByUIBineKey(uiBindKey);
				dt.TableName = keyOfEn;

				ds.Tables.Add(dt);
			}

			//加入sql模式的外键.
			for (Attr attr : en.EnMap.Attrs)
			{
				if (attr.IsRefAttr == true)
				{
					continue;
				}

				if (DataType.IsNullOrEmpty(attr.UIBindKey) || attr.UIBindKey.Length <= 10)
				{
					continue;
				}

				if (attr.UIIsReadonly == true)
				{
					continue;
				}

				if (attr.UIBindKey.Contains("SELECT") == true || attr.UIBindKey.Contains("select") == true)
				{
					/*是一个sql*/
					Object tempVar = attr.UIBindKey.Clone();
					String sqlBindKey = tempVar instanceof String ? (String)tempVar : null;
					sqlBindKey = BP.WF.Glo.DealExp(sqlBindKey, en, null);

					DataTable dt = DBAccess.RunSQLReturnTable(sqlBindKey);
					dt.TableName = attr.Key;

					//@杜. 翻译当前部分.
					if (SystemConfig.AppCenterDBType == DBType.Oracle || SystemConfig.AppCenterDBType == DBType.PostgreSQL)
					{
						dt.Columns["NO"].ColumnName = "No";
						dt.Columns["NAME"].ColumnName = "Name";
					}

					ds.Tables.Add(dt);
				}
			}

			//加入枚举的外键.
			String enumKeys = "";
			for (Attr attr : map.Attrs)
			{
				if (attr.MyFieldType == FieldType.Enum)
				{
					enumKeys += "'" + attr.UIBindKey + "',";
				}
			}

			if (enumKeys.length() > 2)
			{
				enumKeys = enumKeys.substring(0, enumKeys.length() - 1);
				// Sys_Enum
				String sqlEnum = "SELECT * FROM Sys_Enum WHERE EnumKey IN (" + enumKeys + ")";
				DataTable dtEnum = DBAccess.RunSQLReturnTable(sqlEnum);
				dtEnum.TableName = "Sys_Enum";

				if (SystemConfig.AppCenterDBType == DBType.Oracle || SystemConfig.AppCenterDBType == DBType.PostgreSQL)
				{
					dtEnum.Columns["MYPK"].ColumnName = "MyPK";
					dtEnum.Columns["LAB"].ColumnName = "Lab";
					dtEnum.Columns["ENUMKEY"].ColumnName = "EnumKey";
					dtEnum.Columns["INTKEY"].ColumnName = "IntKey";
					dtEnum.Columns["LANG"].ColumnName = "Lang";
				}

				ds.Tables.Add(dtEnum);
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 把外键与枚举放入里面去.


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 增加 上方法.
			DataTable dtM = new DataTable("dtM");
			dtM.Columns.Add("No");
			dtM.Columns.Add("Title");
			dtM.Columns.Add("Tip");
			dtM.Columns.Add("Visable");

			dtM.Columns.Add("Url");
			dtM.Columns.Add("Target");
			dtM.Columns.Add("Warning");
			dtM.Columns.Add("RefMethodType");
			dtM.Columns.Add("GroupName");
			dtM.Columns.Add("W");
			dtM.Columns.Add("H");
			dtM.Columns.Add("Icon");
			dtM.Columns.Add("IsCanBatch");
			dtM.Columns.Add("RefAttrKey");

			RefMethods rms = map.HisRefMethods;
			for (RefMethod item : rms)
			{
				item.HisEn = en;
				//item.HisAttrs = en.EnMap.Attrs;B
				String myurl = "";
				if (item.RefMethodType != RefMethodType.Func)
				{
					Object tempVar2 = item.Do(null);
					myurl = tempVar2 instanceof String ? (String)tempVar2 : null;
					if (myurl == null)
					{
						continue;
					}
				}
				else
				{
					myurl = "../RefMethod.htm?Index=" + item.Index + "&EnName=" + en.toString() + "&EnsName=" + en.GetNewEntities.toString() + "&PKVal=" + this.getPKVal();
				}

				DataRow dr = dtM.NewRow();

				dr.set("No", item.Index);
				dr.set("Title", item.Title);
				dr.set("Tip", item.ToolTip);
				dr.set("Visable", item.Visable);
				dr.set("Warning", item.Warning);

				dr.set("RefMethodType", (int)item.RefMethodType);
				dr.set("RefAttrKey", item.RefAttrKey);
				dr.set("Url", myurl);
				dr.set("W", item.Width);
				dr.set("H", item.Height);
				dr.set("Icon", item.Icon);
				dr.set("IsCanBatch", item.IsCanBatch);
				dr.set("GroupName", item.GroupName);

				dtM.Rows.Add(dr); //增加到rows.
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 增加 上方法.

			//增加方法。
			ds.Tables.Add(dtM);

			return BP.Tools.Json.ToJson(ds);
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 删除实体多附件上传的信息
	 
	 @return 
	*/
	public final String EntityMultiFile_Delete()
	{
		int oid = this.getOID();
		SysFileManager fileManager = new SysFileManager(getOID());
		//获取上传的附件路径，删除附件
		String filepath = fileManager.MyFilePath;
		if (SystemConfig.IsUploadFileToFTP == false)
		{
			if ((new File(filepath)).isFile() == true)
			{
				(new File(filepath)).delete();
			}
		}
		else
		{
			/*保存到fpt服务器上.*/
			FtpSupport.FtpConnection ftpconn = new FtpSupport.FtpConnection(SystemConfig.FTPServerIP, SystemConfig.FTPUserNo, SystemConfig.FTPUserPassword);

			if (ftpconn == null)
			{
				return "err@FTP服务器连接失败";
			}

			 if (ftpconn.FileExist(filepath) == true)
			 {
				 ftpconn.DeleteFile(filepath);
			 }
		}
		fileManager.Delete();
		return fileManager.MyFileName + "删除成功";
	}
	/** 
	 实体初始化
	 
	 @return 
	*/
	public final String Entity_Init()
	{
		try
		{
			//是否是空白记录.
			boolean isBlank = DataType.IsNullOrEmpty(this.getPKVal());
			//if (DataType.IsNullOrEmpty(this.PKVal) == true)
			//    return "err@主键数据丢失，不能初始化En.htm";

			//初始化entity.
			String enName = this.getEnName();
			Entity en = null;
			if (DataType.IsNullOrEmpty(enName) == true)
			{
				if (DataType.IsNullOrEmpty(this.getEnsName()) == true)
				{
					return "err@类名没有传递过来";
				}
				Entities ens = ClassFactory.GetEns(this.getEnsName());
				en = ens.GetNewEntity;
			}
			else
			{
				en = ClassFactory.GetEn(this.getEnName());
			}

			if (en == null)
			{
				return "err@参数类名不正确.";
			}

			//获得描述.
			Map map = en.EnMap;

			String pkVal = this.getPKVal();

			if (isBlank == false)
			{
				en.PKVal = pkVal;
				en.RetrieveFromDBSources();
			}

			//定义容器.
			DataSet ds = new DataSet();

			//把主数据放入里面去.
			DataTable dtMain = en.ToDataTableField("MainTable");
			ds.Tables.Add(dtMain);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 增加 上方法.
			DataTable dtM = new DataTable("dtM");
			dtM.Columns.Add("No");
			dtM.Columns.Add("Title");
			dtM.Columns.Add("Tip");
			dtM.Columns.Add("Visable", java.lang.Class.forName("System.Boolean"));

			dtM.Columns.Add("Url");
			dtM.Columns.Add("Target");
			dtM.Columns.Add("Warning");
			dtM.Columns.Add("RefMethodType");
			dtM.Columns.Add("GroupName");
			dtM.Columns.Add("W");
			dtM.Columns.Add("H");
			dtM.Columns.Add("Icon");
			dtM.Columns.Add("IsCanBatch");
			dtM.Columns.Add("RefAttrKey");
			//判断Func是否有参数
			dtM.Columns.Add("FunPara");


			RefMethods rms = map.HisRefMethods;
			for (RefMethod item : rms)
			{
				item.HisEn = en;

				String myurl = "";
				if (item.RefMethodType == RefMethodType.LinkeWinOpen || item.RefMethodType == RefMethodType.RightFrameOpen || item.RefMethodType == RefMethodType.LinkModel)
				{
					try
					{
						Object tempVar = item.Do(null);
						myurl = tempVar instanceof String ? (String)tempVar : null;
						if (myurl == null)
						{
							continue;
						}
					}
					catch (RuntimeException ex)
					{
						throw new RuntimeException("err@系统错误:根据方法名生成url出现错误:@" + ex.getMessage() + "@" + ex.getCause() + " @方法名:" + item.ClassMethodName);
					}
				}
				else
				{
					myurl = "../RefMethod.htm?Index=" + item.Index + "&EnName=" + en.toString() + "&EnsName=" + en.GetNewEntities.toString() + "&PKVal=" + this.getPKVal();
				}

				DataRow dr = dtM.NewRow();

				dr.set("No", item.Index);
				dr.set("Title", item.Title);
				dr.set("Tip", item.ToolTip);
				dr.set("Visable", item.Visable);
				dr.set("Warning", item.Warning);


				dr.set("RefMethodType", (int)item.RefMethodType);
				dr.set("RefAttrKey", item.RefAttrKey);
				dr.set("Url", myurl);
				dr.set("W", item.Width);
				dr.set("H", item.Height);
				dr.set("Icon", item.Icon);
				dr.set("IsCanBatch", item.IsCanBatch);
				dr.set("GroupName", item.GroupName);
				Attrs attrs = item.HisAttrs;
				if (attrs.size() == 0)
				{
					dr.set("FunPara", "false");
				}
				else
				{
					dr.set("FunPara", "true");
				}

				dtM.Rows.Add(dr); //增加到rows.
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 增加 上方法.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 加入一对多的实体编辑
			AttrsOfOneVSM oneVsM = en.EnMap.AttrsOfOneVSM;
			String sql = "";
			int i = 0;
			if (oneVsM.size() > 0)
			{
				for (AttrOfOneVSM vsM : oneVsM)
				{
					//判断该dot2dot是否显示？
					Entity enMM = vsM.EnsOfMM.GetNewEntity;
					enMM.SetValByKey(vsM.AttrOfOneInMM, this.getPKVal());
					if (enMM.HisUAC.IsView == false)
					{
						continue;
					}
					DataRow dr = dtM.NewRow();
					dr.set("No", enMM.toString());
					// dr["GroupName"] = vsM.GroupName;
					if (en.PKVal != null)
					{
						//判断模式.
						String url = "";
						if (vsM.Dot2DotModel == Dot2DotModel.TreeDept)
						{
							//url = "Dot2DotTreeDeptModel.htm?EnsName=" + en.GetNewEntities.ToString() + "&EnName=" + this.EnName + "&AttrKey=" + vsM.EnsOfMM.ToString();
							//  url = "Branches.htm?EnName=" + en.ToString() + "&AttrKey=" + vsM.EnsOfMM.ToString();

							url = "Branches.htm?EnName=" + this.getEnName() + "&Dot2DotEnsName=" + vsM.EnsOfMM.toString();
							// url += "&PKVal=" + en.PKVal;
							url += "&Dot2DotEnName=" + vsM.EnsOfMM.GetNewEntity.toString(); //存储实体类.
							url += "&AttrOfOneInMM=" + vsM.AttrOfOneInMM; //存储表那个与主表关联. 比如: FK_Node
							url += "&AttrOfMInMM=" + vsM.AttrOfMInMM; //dot2dot存储表那个与实体表.  比如:FK_Station.
							url += "&EnsOfM=" + vsM.EnsOfM.toString(); //默认的B实体分组依据.  比如:FK_Station.
							url += "&DefaultGroupAttrKey=" + vsM.DefaultGroupAttrKey; //默认的B实体分组依据.

						}
						else if (vsM.Dot2DotModel == Dot2DotModel.TreeDeptEmp)
						{
							//   url = "Dot2DotTreeDeptEmpModel.htm?EnsName=" + en.GetNewEntities.ToString() + "&EnName=" + this.EnName + "&AttrKey=" + vsM.EnsOfMM.ToString();
							// url = "Dot2Dot.aspx?EnsName=" + en.GetNewEntities.ToString() + "&EnName=" + this.EnName + "&AttrKey=" + vsM.EnsOfMM.ToString();
							url = "BranchesAndLeaf.htm?EnName=" + this.getEnName() + "&Dot2DotEnsName=" + vsM.EnsOfMM.toString();
							//   url += "&PKVal=" + en.PKVal;
							url += "&Dot2DotEnName=" + vsM.EnsOfMM.GetNewEntity.toString(); //存储实体类.
							url += "&AttrOfOneInMM=" + vsM.AttrOfOneInMM; //存储表那个与主表关联. 比如: FK_Node
							url += "&AttrOfMInMM=" + vsM.AttrOfMInMM; //dot2dot存储表那个与实体表.  比如:FK_Station.
							url += "&EnsOfM=" + vsM.EnsOfM.toString(); //默认的B实体分组依据.  比如:FK_Station.
							url += "&DefaultGroupAttrKey=" + vsM.DefaultGroupAttrKey; //默认的B实体分组依据.  比如:FK_Station.
							//url += "&RootNo=" + vsM.RootNo; //默认的B实体分组依据.  比如:FK_Station.
						}
						else
						{
							// url = "Dot2Dot.aspx?EnsName=" + en.GetNewEntities.ToString() + "&EnName=" + this.EnName + "&AttrKey=" + vsM.EnsOfMM.ToString();
							url = "Dot2Dot.htm?EnName=" + this.getEnName() + "&Dot2DotEnsName=" + vsM.EnsOfMM.toString(); //比如:BP.WF.Template.NodeStations
							url += "&AttrOfOneInMM=" + vsM.AttrOfOneInMM; //存储表那个与主表关联. 比如: FK_Node
							url += "&AttrOfMInMM=" + vsM.AttrOfMInMM; //dot2dot存储表那个与实体表.  比如:FK_Station.
							url += "&EnsOfM=" + vsM.EnsOfM.toString(); //默认的B实体.   //比如:BP.Port.Stations
							url += "&DefaultGroupAttrKey=" + vsM.DefaultGroupAttrKey; //默认的B实体分组依据.  比如:FK_Station.

							//+"&RefAttrEnsName=" + vsM.EnsOfM.ToString();
							//url += "&RefAttrKey=" + vsM.AttrOfOneInMM + "&RefAttrEnsName=" + vsM.EnsOfM.ToString();
						}

						dr.set("Url", url + "&" + en.PK + "=" + en.PKVal + "&PKVal=" + en.PKVal);
						dr.set("Icon", "../Img/M2M.png");

					}

					dr.set("W", "900");
					dr.set("H", "500");
					dr.set("RefMethodType", (int)RefMethodType.RightFrameOpen);


					// 获得选择的数量.
					try
					{
						sql = "SELECT COUNT(*) as NUM FROM " + vsM.EnsOfMM.GetNewEntity.EnMap.PhysicsTable + " WHERE " + vsM.AttrOfOneInMM + "='" + en.PKVal + "'";
						i = DBAccess.RunSQLReturnValInt(sql);
					}
					catch (java.lang.Exception e)
					{
						sql = "SELECT COUNT(*) as NUM FROM " + vsM.EnsOfMM.GetNewEntity.EnMap.PhysicsTable + " WHERE " + vsM.AttrOfOneInMM + "=" + en.PKVal;
						try
						{
							i = DBAccess.RunSQLReturnValInt(sql);
						}
						catch (java.lang.Exception e2)
						{
							vsM.EnsOfMM.GetNewEntity.CheckPhysicsTable();
						}
					}
					dr.set("Title", vsM.Desc + "(" + i + ")");
					dtM.Rows.Add(dr);
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 增加 一对多.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 从表
			EnDtls enDtls = en.EnMap.Dtls;
			for (EnDtl enDtl : enDtls)
			{
				//判断该dtl是否要显示?
				Entity myEnDtl = enDtl.Ens.GetNewEntity; //获取他的en
				myEnDtl.SetValByKey(enDtl.RefKey, this.getPKVal()); //给refpk赋值.
				if (myEnDtl.HisUAC.IsView == false)
				{
					continue;
				}

				DataRow dr = dtM.NewRow();
				//string url = "Dtl.aspx?EnName=" + this.EnName + "&PK=" + this.PKVal + "&EnsName=" + enDtl.EnsName + "&RefKey=" + enDtl.RefKey + "&RefVal=" + en.PKVal.ToString() + "&MainEnsName=" + en.ToString() ;
				String url = "Dtl.htm?EnName=" + this.getEnName() + "&PK=" + this.getPKVal() + "&EnsName=" + enDtl.EnsName + "&RefKey=" + enDtl.RefKey + "&RefVal=" + en.PKVal.toString() + "&MainEnsName=" + en.toString();
				try
				{
					i = DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM " + enDtl.Ens.GetNewEntity.EnMap.PhysicsTable + " WHERE " + enDtl.RefKey + "='" + en.PKVal + "'");
				}
				catch (java.lang.Exception e3)
				{
					try
					{
						i = DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM " + enDtl.Ens.GetNewEntity.EnMap.PhysicsTable + " WHERE " + enDtl.RefKey + "=" + en.PKVal);
					}
					catch (java.lang.Exception e4)
					{
						enDtl.Ens.GetNewEntity.CheckPhysicsTable();
					}
				}

				dr.set("No", enDtl.EnsName);
				dr.set("Title", enDtl.Desc + "(" + i + ")");
				dr.set("Url", url);
				dr.set("GroupName", enDtl.GroupName);

				dr.set("RefMethodType", (int)RefMethodType.RightFrameOpen);

				dtM.Rows.Add(dr);
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 增加 从表.

			ds.Tables.Add(dtM);



			return BP.Tools.Json.ToJson(ds);
		}
		catch (RuntimeException ex)
		{
			return "err@Entity_Init错误:" + ex.getMessage();
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 实体的操作.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 部门人员模式.
	public final String BranchesAndLeaf_SearchByNodeID()
	{
		String dot2DotEnsName = this.GetRequestVal("Dot2DotEnsName");
		String defaultGroupAttrKey = this.GetRequestVal("DefaultGroupAttrKey");
		String key = this.GetRequestVal("Key"); //查询关键字.

		String ensOfM = this.GetRequestVal("EnsOfM"); //多的实体.
		Entities ensMen = ClassFactory.GetEns(ensOfM);
		QueryObject qo = new QueryObject(ensMen); //集合.
		qo.AddWhere(defaultGroupAttrKey, key);
		qo.DoQuery();


		return ensMen.ToJson();
	}
	public final String BranchesAndLeaf_SearchByKey()
	{
		String dot2DotEnsName = this.GetRequestVal("Dot2DotEnsName");
		String defaultGroupAttrKey = this.GetRequestVal("DefaultGroupAttrKey");

		String key = this.GetRequestVal("Key"); //查询关键字.

		String ensOfM = this.GetRequestVal("EnsOfM"); //多的实体.
		Entities ensMen = ClassFactory.GetEns(ensOfM);
		QueryObject qo = new QueryObject(ensMen); //集合.
		qo.AddWhere("No", " LIKE ", "%" + key + "%");
		qo.addOr();
		qo.AddWhere("Name", " LIKE ", "%" + key + "%");
		qo.DoQuery();

		return ensMen.ToJson();
	}
	public final String BranchesAndLeaf_Delete()
	{
		try
		{
			String dot2DotEnName = this.GetRequestVal("Dot2DotEnName");
			String AttrOfOneInMM = this.GetRequestVal("AttrOfOneInMM");
			String AttrOfMInMM = this.GetRequestVal("AttrOfMInMM");
			Entity mm = ClassFactory.GetEn(dot2DotEnName);
			mm.Delete(AttrOfOneInMM, this.getPKVal(), AttrOfMInMM, this.GetRequestVal("Key"));
			return "删除成功.";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 初始化
	 
	 @return 
	*/
	public final String BranchesAndLeaf_Init()
	{
		String dot2DotEnsName = this.GetRequestVal("Dot2DotEnsName");
		String defaultGroupAttrKey = this.GetRequestVal("DefaultGroupAttrKey");

		//string enName = this.GetRequestVal("EnName");
		Entity en = ClassFactory.GetEn(this.getEnName());
		en.PKVal = this.getPKVal();
		en.Retrieve();

		//找到映射.
		AttrsOfOneVSM oneVsM = en.EnMap.AttrsOfOneVSM;
		AttrOfOneVSM vsM = null;
		for (AttrOfOneVSM item : oneVsM)
		{
			if (item.Dot2DotModel == Dot2DotModel.TreeDeptEmp && item.EnsOfMM.toString().equals(dot2DotEnsName) && item.DefaultGroupAttrKey.equals(defaultGroupAttrKey))
			{
				vsM = item;
				break;
			}
		}
		if (vsM == null)
		{
			return "err@参数错误,没有找到VSM";
		}

		//组织数据.
		DataSet ds = new DataSet();
		String rootNo = vsM.RootNo;
		if (rootNo.equals("@WebUser.FK_Dept") || rootNo.equals("WebUser.FK_Dept"))
		{
			rootNo = WebUser.FK_Dept;
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 生成树目录.
		String ensOfM = this.GetRequestVal("EnsOfM"); //多的实体.
		Entities ensMen = ClassFactory.GetEns(ensOfM);
		Entity enMen = ensMen.GetNewEntity;

		Attr attr = enMen.EnMap.GetAttrByKey(defaultGroupAttrKey);
		if (attr == null)
		{
			return "err@在实体[" + ensOfM + "]指定的分树的属性[" + defaultGroupAttrKey + "]不存在，请确认是否删除了该属性?";
		}

		if (attr.MyFieldType == FieldType.Normal)
		{
			return "err@在实体[" + ensOfM + "]指定的分树的属性[" + defaultGroupAttrKey + "]不能是普通字段，必须是外键或者枚举.";
		}

		Entities trees = attr.HisFKEns;
		trees.RetrieveAll();

		DataTable dt = trees.ToDataTableField("DBTrees");
		//如果没有parnetNo 列，就增加上, 有可能是分组显示使用这个模式.
		if (dt.Columns.Contains("ParentNo") == false)
		{
			dt.Columns.Add("ParentNo");
			for (DataRow dr : dt.Rows)
			{
				dr.set("ParentNo", rootNo);
			}
		}
		ds.Tables.Add(dt);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 生成树目录.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 生成选择的数据.
		Entities dot2Dots = ClassFactory.GetEns(dot2DotEnsName);
		dot2Dots.Retrieve(vsM.AttrOfOneInMM, this.getPKVal());

		DataTable dtSelected = dot2Dots.ToDataTableField("DBMMs");

		String attrOfMInMM = this.GetRequestVal("AttrOfMInMM");
		String AttrOfOneInMM = this.GetRequestVal("AttrOfOneInMM");

		dtSelected.Columns[attrOfMInMM].ColumnName = "No";

		if (dtSelected.Columns.Contains(attrOfMInMM + "Text") == false)
		{
			return "err@MM实体类字段属性需要按照外键属性编写:" + dot2DotEnsName + " - " + attrOfMInMM;
		}

		dtSelected.Columns[attrOfMInMM + "Text"].ColumnName = "Name";

		dtSelected.Columns.Remove(AttrOfOneInMM);
		ds.Tables.Add(dtSelected); //已经选择的数据.
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 生成选择的数据.

		return BP.Tools.Json.ToJson(ds);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 部门人员模式.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 分组数据.
	/** 
	 执行保存
	 
	 @return 
	*/
	public final String Dot2Dot_Save()
	{

		try
		{
			String eles = this.GetRequestVal("ElesAAA");

			//实体集合.
			String dot2DotEnsName = this.GetRequestVal("Dot2DotEnsName");
			String attrOfOneInMM = this.GetRequestVal("AttrOfOneInMM");
			String attrOfMInMM = this.GetRequestVal("AttrOfMInMM");

			//获得点对点的实体.
			Entity en = ClassFactory.GetEns(dot2DotEnsName).GetNewEntity;
			en.Delete(attrOfOneInMM, this.getPKVal()); //首先删除.

			String[] strs = eles.split("[,]", -1);
			for (String str : strs)
			{
				if (DataType.IsNullOrEmpty(str) == true)
				{
					continue;
				}

				en.SetValByKey(attrOfOneInMM, this.getPKVal());
				en.SetValByKey(attrOfMInMM, str);
				en.Insert();
			}
			return "数据保存成功.";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 获得分组的数据源
	 
	 @return 
	*/
	public final String Dot2Dot_GenerGroupEntitis()
	{
		String key = this.GetRequestVal("DefaultGroupAttrKey");

		//实体集合.
		String ensName = this.GetRequestVal("EnsOfM");
		Entities ens = ClassFactory.GetEns(ensName);
		Entity en = ens.GetNewEntity;

		Attrs attrs = en.EnMap.Attrs;
		Attr attr = attrs.GetAttrByKey(key);

		if (attr == null)
		{
			return "err@设置的分组外键错误[" + key + "],不存在[" + ensName + "]或者已经被删除.";
		}

		if (attr.MyFieldType == FieldType.Normal)
		{
			return "err@设置的默认分组[" + key + "]不能是普通字段.";
		}

		if (attr.MyFieldType == FieldType.FK)
		{
			Entities ensFK = attr.HisFKEns;
			ensFK.Clear();
			ensFK.RetrieveAll();
			return ensFK.ToJson();
		}

		if (attr.MyFieldType == FieldType.Enum)
		{
			/* 如果是枚举 */
			SysEnums ses = new SysEnums();
			ses.Retrieve(SysEnumAttr.IntKey, attr.UIBindKey);
		}

		return "err@设置的默认分组[" + key + "]不能是普通字段.";
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 分组数据.


}