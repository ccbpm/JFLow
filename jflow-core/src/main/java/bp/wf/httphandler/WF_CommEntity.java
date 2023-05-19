package bp.wf.httphandler;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.difference.handler.CommonFileUtils;
import bp.difference.handler.WebContralBase;
import bp.pub.PubClass;
import bp.sys.*;
import bp.tools.FtpUtil;
import bp.web.*;
import bp.en.*;
import bp.tools.ZipCompress;
import bp.*;
import bp.wf.*;
import bp.wf.Glo;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLDecoder;
import java.nio.file.*;
import java.util.Date;

/**
 页面功能实体
 */
public class WF_CommEntity extends WebContralBase
{

	/**
	 构造函数
	 */
	public WF_CommEntity() throws Exception {
	}


	///#region 从表.
	/**
	 初始化

	 @return
	 */
	public final String Dtl_Save() throws Exception {
		try {

			/// 查询出来从表数据.
			Entities dtls = ClassFactory.GetEns(this.getEnsName());
			Entity dtl = dtls.getGetNewEntity();
			dtls.Retrieve(this.GetRequestVal("RefKey"), this.GetRequestVal("RefVal"));
			Map map = dtl.getEnMap();
			for (Entity item : dtls) {
				String pkval = item.GetValStringByKey(dtl.getPK());
				for (Attr attr : map.getAttrs()) {
					if (attr.getIsRefAttr() == true) {
						continue;
					}

					if (attr.getMyDataType() == DataType.AppDateTime || attr.getMyDataType() == DataType.AppDate) {
						if (attr.getUIIsReadonly() == true) {
							continue;
						}

						String val = this.GetValFromFrmByKey("TB_" + pkval + "_" + attr.getKey(), null);
						item.SetValByKey(attr.getKey(), val);
						continue;
					}

					if (attr.getUIContralType() == UIContralType.TB && attr.getUIIsReadonly() == false) {
						String val = this.GetValFromFrmByKey("TB_" + pkval + "_" + attr.getKey(), null);
						item.SetValByKey(attr.getKey(), val);
						continue;
					}

					if (attr.getUIContralType() == UIContralType.DDL && attr.getUIIsReadonly() == false) {
						String val = this.GetValFromFrmByKey("DDL_" + pkval + "_" + attr.getKey());
						item.SetValByKey(attr.getKey(), val);
						continue;
					}

					if (attr.getUIContralType() == UIContralType.CheckBok && attr.getUIIsReadonly() == false) {
						String val = this.GetValFromFrmByKey("CB_" + pkval + "_" + attr.getKey(), "-1");
						if (val.equals("-1")) {
							item.SetValByKey(attr.getKey(), 0);
						} else {
							item.SetValByKey(attr.getKey(), 1);
						}
						continue;
					}
				}

				item.Update(); // 执行更新.
			}

			/// 查询出来从表数据.

			/// 保存新加行.
			int newRowCount = this.GetRequestValInt("NewRowCount");
			boolean isEntityOID = dtl.getIsOIDEntity(); // 已同步数据.
			boolean isEntityNo = dtl.getIsNoEntity();
			for (int i = 0; i < newRowCount; i++) {
				String val = "";
				for (Attr attr : map.getAttrs()) {

					if (attr.getMyDataType() == DataType.AppDateTime || attr.getMyDataType() == DataType.AppDate) {
						if (attr.getUIIsReadonly() == true) {
							continue;
						}

						val = this.GetValFromFrmByKey("TB_" + i + "_" + attr.getKey(), null);
						dtl.SetValByKey(attr.getKey(), val);
						continue;
					}

					if (attr.getUIContralType() == UIContralType.TB && attr.getUIIsReadonly() == false) {
						val = this.GetValFromFrmByKey("TB_" + i + "_" + attr.getKey());
						if (attr.getIsNum() && val.equals("")) {
							val = "0";
						}
						dtl.SetValByKey(attr.getKey(), val);
						continue;
					}

					if (attr.getUIContralType() == UIContralType.DDL && attr.getUIIsReadonly() == false) {
						val = this.GetValFromFrmByKey("DDL_" + i + "_" + attr.getKey());
						dtl.SetValByKey(attr.getKey(), val);
						continue;
					}

					if (attr.getUIContralType() == UIContralType.CheckBok && attr.getUIIsReadonly() == false) {
						val = this.GetValFromFrmByKey("CB_" + i + "_" + attr.getKey(), "-1");
						if (val.equals("-1")) {
							dtl.SetValByKey(attr.getKey(), 0);
						} else {
							dtl.SetValByKey(attr.getKey(), 1);
						}
						continue;
					}
				}
				// dtl.SetValByKey(pkval, 0);
				dtl.SetValByKey(this.GetRequestVal("RefKey"), this.GetRequestVal("RefVal"));

				// 已同步数据.
				if (isEntityOID == true) {
					dtl.setPKVal("0");
					dtl.Insert();
					continue;
				}

				if (isEntityNo == true && dtl.getEnMap().getIsAutoGenerNo() == true) {
					dtl.setPKVal(dtl.GenerNewNoByKey("No"));
					dtl.Insert();
					continue;
				}

				// 直接执行保存.
				dtl.Insert();
			}

			/// 保存新加行.

			return "保存成功.";
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}
	/**
	 保存

	 @return
	 */
	public final String Dtl_Init() throws Exception {
		DataSet ds = new DataSet();

		// 查询出来从表数据.
		Entities dtls = ClassFactory.GetEns(this.getEnsName());
		dtls.Retrieve(this.GetRequestVal("RefKey"), this.GetRequestVal("RefVal"));
		ds.Tables.add(dtls.ToDataTableField("Dtls"));

		// 实体.
		Entity dtl = dtls.getGetNewEntity();
		// 定义Sys_MapData.
		MapData md = new MapData();
		md.setNo(this.getEnName());
		md.setName(dtl.getEnDesc());

		/// 加入权限信息.
		// 把权限加入参数里面.
		if (dtl.getHisUAC().IsInsert) {
			md.SetPara("IsInsert", "1");
		}
		if (dtl.getHisUAC().IsUpdate) {
			md.SetPara("IsUpdate", "1");
		}
		if (dtl.getHisUAC().IsDelete) {
			md.SetPara("IsDelete", "1");
		}
		if (dtl.getHisUAC().IsImp) {
			md.SetPara("IsImp", "1");
		}
		md.SetPara("EntityPK", dtl.getPK_Field());
		/// 加入权限信息.

		ds.Tables.add(md.ToDataTableField("Sys_MapData"));

		/// 字段属性.
		MapAttrs attrs = dtl.getEnMap().getAttrs().ToMapAttrs();
		DataTable sys_MapAttrs = attrs.ToDataTableField("Sys_MapAttr");
		ds.Tables.add(sys_MapAttrs);

		/// 字段属性.

		/// 把外键与枚举放入里面去.
		for (DataRow dr : sys_MapAttrs.Rows) {
			String uiBindKey = dr.getValue("UIBindKey").toString();
			String lgType = dr.getValue("LGType").toString();
			if (lgType.equals("2") == false) {
				continue;
			}

			String UIIsEnable = dr.getValue("UIVisible").toString();
			if (UIIsEnable.equals("0")) {
				continue;
			}

			if (DataType.IsNullOrEmpty(uiBindKey) == true) {
				String myPK = dr.getValue("MyPK").toString();
				/* 如果是空的 */
				// throw new Exception("@属性字段数据不完整，流程:" + fl.getNo() + fl.Name + ",节点:" +
				// nd.getNodeID() + nd.Name + ",属性:" + myPK + ",的UIBindKey IsNull ");
			}

			// 检查是否有下拉框自动填充。
			String keyOfEn = dr.getValue("KeyOfEn").toString();
			String fk_mapData = dr.getValue("FK_MapData").toString();

			// 判断是否存在.
			if (ds.GetTableByName(uiBindKey) != null) {
				continue;
			}

			ds.Tables.add(PubClass.GetDataTableByUIBineKey(uiBindKey));
		}

		for (Attr attr : dtl.getEnMap().getAttrs()) {
			if (attr.getIsRefAttr() == true) {
				continue;
			}

			if (DataType.IsNullOrEmpty(attr.getUIBindKey()) || attr.getUIBindKey().length() <= 10) {
				continue;
			}

			if (attr.getUIIsReadonly() == true) {
				continue;
			}

			if (attr.getUIBindKey().contains("SELECT") == true || attr.getUIBindKey().contains("select") == true) {
				/* 是一个sql */
				Object tempVar = attr.getUIBindKey();
				String sqlBindKey = tempVar instanceof String ? (String) tempVar : null;

				// 判断是否存在.
				if (ds.GetTableByName(sqlBindKey) != null) {
					continue;
				}

				sqlBindKey = bp.wf.Glo.DealExp(sqlBindKey, null, null);

				DataTable dt = DBAccess.RunSQLReturnTable(sqlBindKey);
				dt.TableName = attr.getKey();

				// @杜. 翻译当前部分.
				if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.UpperCase)
				{
					dt.Columns.get("NO").setColumnName("No");
					dt.Columns.get("NAME").setColumnName("Name");
				}
				if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.Lowercase)
				{
					dt.Columns.get("no").setColumnName("No");
					dt.Columns.get("name").setColumnName("Name");
				}

				ds.Tables.add(dt);
			}
		}

		String enumKeys = "";
		for (Attr attr : dtl.getEnMap().getAttrs()) {
			if (attr.getMyFieldType() == FieldType.Enum) {
				enumKeys += "'" + attr.getUIBindKey() + "',";
			}
		}

		if (enumKeys.length() > 2) {
			enumKeys = enumKeys.substring(0, enumKeys.length() - 1);
			// Sys_Enum
			String sqlEnum = "SELECT * FROM "+bp.sys.base.Glo.SysEnum()+" WHERE EnumKey IN (" + enumKeys + ")";
			DataTable dtEnum = DBAccess.RunSQLReturnTable(sqlEnum);
			dtEnum.TableName = "Sys_Enum";

			if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.UpperCase)
			{
				dtEnum.Columns.get("MYPK").setColumnName("MyPK");
				dtEnum.Columns.get("LAB").setColumnName("Lab");
				dtEnum.Columns.get("ENUMKEY").setColumnName("EnumKey");
				dtEnum.Columns.get("INTKEY").setColumnName("IntKey");
				dtEnum.Columns.get("LANG").setColumnName("Lang");
			}
			if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.Lowercase)
			{
				dtEnum.Columns.get("mypk").setColumnName("MyPK");
				dtEnum.Columns.get("lab").setColumnName("Lab");
				dtEnum.Columns.get("enumkey").setColumnName("EnumKey");
				dtEnum.Columns.get("intkey").setColumnName("IntKey");
				dtEnum.Columns.get("lang").setColumnName("Lang");
			}
			ds.Tables.add(dtEnum);
		}

		/// 把外键与枚举放入里面去.

		return bp.tools.Json.ToJson(ds);
	}

	public final String Dtl_Exp() throws Exception {
		String refPKVal = this.GetRequestVal("RefVal");
		Entities dtls = ClassFactory.GetEns(this.getEnsName());
		dtls.Retrieve(this.GetRequestVal("RefKey"), this.GetRequestVal("RefVal"));
		Entity en = dtls.getGetNewEntity();
		String name = "数据导出";
		if (refPKVal.contains("/") == true) {
			refPKVal = refPKVal.replace("/", "_");
		}
		String filename = refPKVal + "_" + en.toString() + "_" + DataType.getCurrentDate() + "_" + name + ".xls";
		String filePath = bp.tools.ExportExcelUtil.ExportTempToExcel(dtls.ToDataTableField(), en, name, null);
		filePath = SystemConfig.getPathOfTemp() + filename;

		String tempPath = SystemConfig.getPathOfTemp() + refPKVal + "/";
		if ((new File(tempPath)).isDirectory() == false) {
			(new File(tempPath)).mkdirs();
		}

		String myFilePath = SystemConfig.getPathOfDataUser()
				+ this.getEnsName().substring(0, this.getEnsName().length() - 1);

		for (Entity dt : dtls) {
			String pkval = dt.getPKVal().toString();
			Object tempVar = dt.GetValByKey("MyFileExt");
			String ext = DataType.IsNullOrEmpty(tempVar instanceof String ? (String) tempVar : null) ? ""
					: dt.GetValByKey("MyFileExt").toString();
			if (DataType.IsNullOrEmpty(ext) == true) {
				continue;
			}
			myFilePath = myFilePath + "/" + pkval + "." + ext;
			if ((new File(myFilePath)).isFile() == true) {
				Files.copy(Paths.get(myFilePath), Paths.get(tempPath + pkval + "." + ext),
						StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
			}
		}
		Files.copy(Paths.get(filePath), Paths.get(tempPath + filename), StandardCopyOption.COPY_ATTRIBUTES,
				StandardCopyOption.REPLACE_EXISTING);

		// 生成压缩文件
		String zipFile = SystemConfig.getPathOfTemp() + refPKVal + "_" + en.toString() + "_" + DataType.getCurrentDate()
				+ "_" + name + ".zip";

		// 执行压缩.
		ZipCompress fz = new ZipCompress(zipFile, tempPath);
		fz.zip();

		return "/DataUser/Temp/" + refPKVal + "_" + en.toString() + "_" + DataType.getCurrentDate() + "_" + name
				+ ".zip";
	}

	///#endregion 从表.


	///#region 实体的操作.
	/**
	 实体初始化

	 @return
	 */
	public final String EntityOnly_Init() throws Exception {
		try {
			// 是否是空白记录.
			boolean isBlank = DataType.IsNullOrEmpty(this.getPKVal());

			// 初始化entity.
			String enName = this.getEnName();
			Entity en = null;
			if (isBlank == true) {
				if (DataType.IsNullOrEmpty(this.getEnsName()) == true) {
					return "err@类名没有传递过来";
				}
				Entities ens = ClassFactory.GetEns(this.getEnsName());

				if (ens == null) {
					return "err@类名错误" + this.getEnsName(); // @李国文.
				}

				en = ens.getGetNewEntity();
			} else {
				en = ClassFactory.GetEn(this.getEnName());
			}

			if (en == null) {
				return "err@参数类名不正确.";
			}

			// 获得描述.
			Map map = en.getEnMap();
			String pkVal = this.getPKVal();
			if (isBlank == false) {
				en.setPKVal(pkVal);
				int i = en.RetrieveFromDBSources();
				if (i == 0) {
					return "err@数据[" + map.getEnDesc() + "]主键为[" + pkVal + "]不存在，或者没有保存。";
				}
			} else {
				for (Attr attr : en.getEnMap().getAttrs()) {
					en.SetValByKey(attr.getKey(), attr.getDefaultVal());
				}

				// 设置默认的数据.
				en.ResetDefaultVal();

				en.SetValByKey("RefPKVal", this.getRefPKVal());

				// 自动生成一个编号.
				if (en.getIsNoEntity() == true && en.getEnMap().getIsAutoGenerNo() == true) {
					en.SetValByKey("No", en.GenerNewNoByKey("No"));
				}
			}

			// 定义容器.
			DataSet ds = new DataSet();

			// 定义Sys_MapData.
			MapData md = new MapData();
			md.setNo(this.getEnName());
			md.setName(map.getEnDesc());

			// 附件类型.
			md.SetPara("BPEntityAthType", map.HisBPEntityAthType.getValue());

			// 多附件上传
			if (map.HisBPEntityAthType.getValue() == 2) {
				// 增加附件分类
				DataTable attrFiles = new DataTable("AttrFiles");
				attrFiles.Columns.Add("FileNo");
				attrFiles.Columns.Add("FileName");
				for (AttrFile attrFile : map.getHisAttrFiles()) {
					DataRow dr = attrFiles.NewRow();
					dr.setValue("FileNo", attrFile.FileNo);
					dr.setValue("FileName", attrFile.FileName);
					attrFiles.Rows.add(dr);
				}
				ds.Tables.add(attrFiles);

				// 增加附件列表
				SysFileManagers sfs = new SysFileManagers(en.toString(), en.getPKVal().toString());
				ds.Tables.add(sfs.ToDataTableField("Sys_FileManager"));
			}

			/// 加入权限信息.
			// 把权限加入参数里面.
			if (en.getHisUAC().IsInsert) {
				md.SetPara("IsInsert", "1");
			}
			if (en.getHisUAC().IsUpdate) {
				md.SetPara("IsUpdate", "1");
			}
			if (isBlank == true) {
				if (en.getHisUAC().IsDelete) {
					md.SetPara("IsDelete", "0");
				}
			} else {
				if (en.getHisUAC().IsDelete) {
					md.SetPara("IsDelete", "1");
				}
			}

			/// 加入权限信息.

			ds.Tables.add(md.ToDataTableField("Sys_MapData"));

			// 把主数据放入里面去.
			DataTable dtMain = en.ToDataTableField("MainTable");
			ds.Tables.add(dtMain);

			/// 字段属性.
			Attrs attrs = en.getEnMap().getAttrs();
			MapAttrs mapAttrs = new MapAttrs();
			//获取Map中的分组
			DataTable dtGroups = new DataTable("Sys_GroupField");
			dtGroups.Columns.Add("OID");
			dtGroups.Columns.Add("Lab");
			String groupName = "";
			String groupNames = "";
			for(Attr attr : attrs)
			{
				if (attr.getMyFieldType() == FieldType.RefText)
					continue;
				groupName = attr.GroupName;
				if (groupNames.contains(groupName + ",") == false)
				{
					DataRow dr = dtGroups.NewRow();
					groupNames += groupName + ",";
					dr.setValue("OID",groupName);
					dr.setValue("Lab",groupName);
					dtGroups.Rows.add(dr);
				}

				MapAttr mapAttr = attr.getToMapAttr();
				mapAttr.SetPara("GroupName", attr.GroupName);
				mapAttrs.AddEntity(mapAttr);
			}
			ds.Tables.add(dtGroups);
			DataTable sys_MapAttrs = mapAttrs.ToDataTableField("Sys_MapAttr");
			ds.Tables.add(sys_MapAttrs);

			/// 字段属性.

			/// 加入扩展属性.
			MapExts mapExts = new MapExts(this.getEnName() + "s");
			DataTable Sys_MapExt = mapExts.ToDataTableField("Sys_MapExt");
			ds.Tables.add(Sys_MapExt);

			/// 加入扩展属性.

			/// 把外键与枚举放入里面去.

			// 加入外键.
			for (DataRow dr : sys_MapAttrs.Rows) {
				String uiBindKey = dr.getValue("UIBindKey").toString();
				String lgType = dr.getValue("LGType").toString();
				if (lgType.equals("2") == false) {
					continue;
				}

				String UIIsEnable = dr.getValue("UIVisible").toString();

				String uiIsEnable = dr.getValue("UIIsEnable").toString();
				if (UIIsEnable.equals("0") == true || uiIsEnable.equals("0") == true)
				{
					continue;
				}

				if (DataType.IsNullOrEmpty(uiBindKey) == true) {
					String myPK = dr.getValue("MyPK").toString();
					/* 如果是空的 */
					// throw new Exception("@属性字段数据不完整，流程:" + fl.getNo() + fl.Name + ",节点:" +
					// nd.getNodeID() + nd.Name + ",属性:" + myPK + ",的UIBindKey IsNull ");
				}

				// 检查是否有下拉框自动填充。
				String keyOfEn = dr.getValue("KeyOfEn").toString();
				String fk_mapData = dr.getValue("FK_MapData").toString();

				// 判断是否存在.
				if (ds.GetTableByName(uiBindKey) != null) {
					continue;
				}

				DataTable dt = PubClass.GetDataTableByUIBineKey(uiBindKey);
				dt.TableName = keyOfEn;

				ds.Tables.add(dt);
			}

			// 加入sql模式的外键.
			for (Attr attr : en.getEnMap().getAttrs()) {
				if (attr.getIsRefAttr() == true) {
					continue;
				}

				if (DataType.IsNullOrEmpty(attr.getUIBindKey()) || attr.getUIBindKey().length() <= 10) {
					continue;
				}

				if (attr.getUIIsReadonly() == true) {
					continue;
				}

				if (attr.getUIBindKey().toUpperCase().contains("SELECT") == true) {
					/* 是一个sql */
					Object tempVar = attr.getUIBindKey();
					String sqlBindKey = tempVar instanceof String ? (String) tempVar : null;
					sqlBindKey = bp.wf.Glo.DealExp(sqlBindKey, en, null);

					DataTable dt = DBAccess.RunSQLReturnTable(sqlBindKey);
					dt.TableName = attr.getKey();


					dt.Columns.get(0).setColumnName("No");
					dt.Columns.get(1).setColumnName("Name");


					ds.Tables.add(dt);
				}
			}

			// 加入枚举的外键.
			String enumKeys = "";
			for (Attr attr : map.getAttrs()) {
				if (attr.getMyFieldType() == FieldType.Enum) {
					enumKeys += "'" + attr.getUIBindKey() + "',";
				}
			}

			if (enumKeys.length() > 2) {
				enumKeys = enumKeys.substring(0, enumKeys.length() - 1);
				// Sys_Enum
				String sqlEnum = "SELECT * FROM "+bp.sys.base.Glo.SysEnum()+" WHERE EnumKey IN (" + enumKeys + ")";
				DataTable dtEnum = DBAccess.RunSQLReturnTable(sqlEnum);
				dtEnum.TableName = "Sys_Enum";

				if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.UpperCase){
					dtEnum.Columns.get("MYPK").setColumnName("MyPK");
					dtEnum.Columns.get("LAB").setColumnName("Lab");
					dtEnum.Columns.get("ENUMKEY").setColumnName("EnumKey");
					dtEnum.Columns.get("INTKEY").setColumnName("IntKey");
					dtEnum.Columns.get("LANG").setColumnName("Lang");
				}
				if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.Lowercase){
					dtEnum.Columns.get("mypk").setColumnName("MyPK");
					dtEnum.Columns.get("lab").setColumnName("Lab");
					dtEnum.Columns.get("enumkey").setColumnName("EnumKey");
					dtEnum.Columns.get("intkey").setColumnName("IntKey");
					dtEnum.Columns.get("lang").setColumnName("Lang");
				}

				ds.Tables.add(dtEnum);
			}

			/// 把外键与枚举放入里面去.

			/// 增加 上方法.
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

			RefMethods rms = map.getHisRefMethods();
			for (RefMethod item : rms) {
				item.HisEn = en;
				// item.HisAttrs = en.getEnMap().getAttrs();B
				String myurl = "";
				if (item.refMethodType != RefMethodType.Func) {
					Object tempVar2 = item.Do(null);
					myurl = tempVar2 instanceof String ? (String) tempVar2 : null;
					if (myurl == null) {
						continue;
					}
				} else {
					myurl = "../RefMethod.htm?Index=" + item.Index + "&EnName=" + en.toString() + "&EnsName="
							+ en.getGetNewEntities().toString() + "&PKVal=" + this.getPKVal();
				}

				DataRow dr = dtM.NewRow();

				dr.setValue("No", item.Index);
				dr.setValue("Title", item.Title);
				dr.setValue("Tip", item.ToolTip);
				dr.setValue("Visable", item.Visable);
				dr.setValue("Warning", item.Warning);

				dr.setValue("RefMethodType", item.refMethodType.getValue());
				dr.setValue("RefAttrKey", item.RefAttrKey);
				dr.setValue("Url", myurl);
				dr.setValue("W", item.Width);
				dr.setValue("H", item.Height);
				dr.setValue("Icon", item.Icon);
				dr.setValue("IsCanBatch", item.IsCanBatch);
				dr.setValue("GroupName", item.GroupName);

				dtM.Rows.add(dr); // 增加到rows.
			}

			/// 增加 上方法.

			// 增加方法。
			ds.Tables.add(dtM);

			return bp.tools.Json.ToJson(ds);
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}

	/**
	 实体Entity 单附件上传
	 @return
	 */
	public final String EntityAth_Upload() throws Exception {
		HttpServletRequest request = getRequest();
		String contentType = request.getContentType();
		if (contentType == null || contentType.indexOf("multipart/form-data") == -1)
			return "err@附件上传页面Form表单类型设置错误";
		MultipartHttpServletRequest mrequest = CommonFileUtils.getMultipartHttpServletRequest(request);
		if(mrequest.getFileMap().size()==0)
			return "err@请选择要上传的文件。";
		MultipartFile file = mrequest.getFile("file");
		//获取保存文件信息的实体
		String enName = this.getEnName();
		Entity en = null;
		//是否是空白记录.
		boolean isBlank = DataType.IsNullOrEmpty(this.getPKVal());
		if (isBlank == true)
			return "err@请先保存实体信息然后再上传文件";
		else
			en = ClassFactory.GetEn(this.getEnName());

		if (en == null)
			return "err@参数类名不正确.";
		en.setPKVal(this.getPKVal());
		int i = en.RetrieveFromDBSources();
		if (i == 0)
			return "err@数据[" + this.getEnName() + "]主键为[" + en.getPKVal() + "]不存在，或者没有保存。";

		//获取文件的名称
		String fullfileName =file.getOriginalFilename();
		if (fullfileName.indexOf("/") >= 0)
			fullfileName = fullfileName.substring(fullfileName.lastIndexOf("/") + 1);
		String fileName = fullfileName.substring(0, fullfileName.lastIndexOf('.'));
		//文件后缀
		String ext =fullfileName.replace(fileName+".","");

		//文件大小
		float size = file.getSize() / 1024;

		//保存位置
		String filepath = "";


		//如果是天业集团则保存在ftp服务器上
		if (SystemConfig.getCustomerNo().equals("TianYe") || SystemConfig.getIsUploadFileToFTP() == true)
		{
			String guid = DBAccess.GenerGUID(0, null, null);

			//把文件临时保存到一个位置.
			String temp = SystemConfig.getPathOfTemp() + "" + guid + ".tmp";
			try
			{
				CommonFileUtils.upload(request, "file", new File(temp));
			}
			catch (RuntimeException ex)
			{
				(new File(temp)).delete();
				CommonFileUtils.upload(request, "file", new File(temp));
			}

			String ny = DataType.getCurrentDateByFormart("yyyy_MM");
			String workDir = ny + "/Helper/";
			boolean isOK =false;
			FtpUtil ftpUtil = bp.wf.Glo.getFtpUtil();

			ftpUtil.changeWorkingDirectory(workDir,true);

			// 把文件放在FTP服务器上去.
			isOK=ftpUtil.uploadFile( guid +"."+ ext,temp);

			ftpUtil.releaseConnection();
			//删除临时文件
			(new File(temp)).delete();


			//删除临时文件
			(new File(temp)).delete();

			//设置路径.
			filepath = ny + "/Helper/" + guid+"." + ext;

		}
		else
		{
			String fileSavePath = en.getEnMap().FJSavePath;

			if (DataType.IsNullOrEmpty(fileSavePath) == true)
			{
				fileSavePath = SystemConfig.getPathOfDataUser() + enName;
			}

			if ((new File(fileSavePath)).isDirectory() == false)
			{
				(new File(fileSavePath)).mkdirs();
			}

			filepath = fileSavePath + "/" + this.getPKVal() + "." + ext;

			//存在文件则删除
			if ((new File(filepath)).isFile() == true)
			{
				(new File(filepath)).delete();
			}

			File info = new File(filepath);
			CommonFileUtils.upload(request, "file",info);
		}

		//需要这样写 @李国文.
		en.SetValByKey("MyFileName", fileName);
		en.SetValByKey("MyFilePath", filepath);
		en.SetValByKey("MyFileExt", ext);
		en.SetValByKey("MyFileSize", size);
		en.SetValByKey("WebPath", filepath);

		en.Update();
		return "文件保存成功";
	}
	/**
	 * 单实体多附件上传
	 * @return
	 * @throws Exception
	 */
	public final String EntityMultiAth_Upload() throws Exception {
		HttpServletRequest request = getRequest();
		String contentType = request.getContentType();
		if (contentType == null || contentType.indexOf("multipart/form-data") == -1)
			return "err@附件上传页面Form表单类型设置错误";
		MultipartHttpServletRequest mrequest = CommonFileUtils.getMultipartHttpServletRequest( request);
		if(mrequest.getFileMap().size()==0)
			return "err@请选择要上传的文件。";
		MultipartFile file = mrequest.getFile("file");
		//获取保存文件信息的实体
		String enName = this.getEnName();
		Entity en = null;

		//是否是空白记录.
		boolean isBlank = DataType.IsNullOrEmpty(this.getPKVal());
		if (isBlank == true)
			return "err@请先保存实体信息然后再上传文件";
		else
			en = ClassFactory.GetEn(this.getEnName());

		if (en == null)
			return "err@参数类名不正确.";
		en.setPKVal(this.getPKVal());
		int i = en.RetrieveFromDBSources();
		if (i == 0)
		{
			return "err@数据[" + this.getEnName() + "]主键为[" + en.getPKVal()+ "]不存在，或者没有保存。";
		}

		//获取文件的名称
		String fullfileName =file.getOriginalFilename();
		if (fullfileName.indexOf("/") >= 0)
			fullfileName = fullfileName.substring(fullfileName.lastIndexOf("/") + 1);
		String fileName = fullfileName.substring(0, fullfileName.lastIndexOf('.'));
		//文件后缀
		String ext =fullfileName.replace(fileName,"");

		//文件大小
		float size = file.getSize() / 1024;

		//保存位置
		String filepath = "";

		//如果是天业集团则保存在ftp服务器上
		if (SystemConfig.getCustomerNo().equals("TianYe") || SystemConfig.getIsUploadFileToFTP() == true)
		{
			String guid = DBAccess.GenerGUID(0, null, null);

			//把文件临时保存到一个位置.
			String temp = SystemConfig.getPathOfTemp() + "" + guid + ".tmp";
			try
			{
				CommonFileUtils.upload(request, "file", new File(temp));
			}
			catch (RuntimeException ex)
			{
				(new File(temp)).delete();
				CommonFileUtils.upload(request, "file", new File(temp));
			}


			String ny = DataType.getCurrentDateByFormart("yyyy_MM");
			String workDir = ny + "/Helper/";
			boolean isOK =false;
			FtpUtil ftpUtil = bp.wf.Glo.getFtpUtil();

			ftpUtil.changeWorkingDirectory(workDir,true);

			// 把文件放在FTP服务器上去.
			isOK=ftpUtil.uploadFile( guid + ext,temp);

			ftpUtil.releaseConnection();
			//删除临时文件
			(new File(temp)).delete();

			//设置路径.
			filepath = ny + "/Helper/" + guid + ext;

		}
		else
		{

			String savePath = SystemConfig.getPathOfDataUser() + enName + "/" + this.getPKVal();

			if ((new File(savePath)).isDirectory() == false)
			{
				(new File(savePath)).mkdirs();
			}
			savePath = savePath + "/" + fileName + ext;
			//存在文件则删除
			if ((new File(savePath)).isDirectory() == true)
			{
				(new File(savePath)).delete();
			}

			File info = new File(savePath);
			CommonFileUtils.upload(request, "file",info);
			filepath = "/DataUser/" + enName + "/" + this.getPKVal() + "/" + fileName + ext;
		}
		//保存上传的文件
		SysFileManager fileManager = new SysFileManager();
		fileManager.setAttrFileNo(this.GetRequestVal("FileNo"));
		fileManager.setAttrFileName(URLDecoder.decode(this.GetRequestVal("FileName"), "UTF-8"));
		fileManager.setEnName(this.getEnName());
		fileManager.setRefVal(this.getPKVal());
		fileManager.setMyFileName(fileName);
		fileManager.setMyFilePath(filepath);
		fileManager.setMyFileExt(ext);
		fileManager.setMyFileSize(size);
		fileManager.setWebPath(filepath);
		fileManager.Insert();
		return fileManager.ToJson(true);
	}
	/**
	 删除实体多附件上传的信息
	 @return
	 */
	public final String EntityMultiFile_Delete() throws Exception {
		int oid = (int) this.getOID();
		SysFileManager fileManager = new SysFileManager(oid);
		// 获取上传的附件路径，删除附件
		String filepath = fileManager.getMyFilePath();
		if (SystemConfig.getIsUploadFileToFTP() == false) {
			if ((new File(filepath)).isFile() == true) {
				(new File(filepath)).delete();
			}
		} else {
			/* 保存到fpt服务器上. */
			FtpUtil ftpUtil = bp.wf.Glo.getFtpUtil();
			String msg = ftpUtil.openConnection();
			if (msg.contains("err@") == true) {
				return msg;
			}

			ftpUtil.deleteFile(filepath);

		}
		fileManager.Delete();
		return fileManager.getMyFileName() + "删除成功";
	}
	/**
	 实体初始化

	 @return
	 */
	public final String Entity_Init() throws Exception {

		try {
			// 是否是空白记录.
			boolean isBlank = DataType.IsNullOrEmpty(this.getPKVal());
			// if (DataType.IsNullOrEmpty(this.PKVal) == true)
			// return "err@主键数据丢失，不能初始化En.htm";

			// 初始化entity.
			String enName = this.getEnName();
			Entity en = null;
			if (DataType.IsNullOrEmpty(enName) == true) {
				if (DataType.IsNullOrEmpty(this.getEnsName()) == true) {
					return "err@类名没有传递过来";
				}
				Entities ens = ClassFactory.GetEns(this.getEnsName());
				en = ens.getGetNewEntity();
			} else {
				en = ClassFactory.GetEn(this.getEnName());
			}

			if (en == null) {
				return "err@参数类名不正确.";
			}

			// 获得描述.
			Map map = en.getEnMapInTime();

			String pkVal = this.getPKVal();

			if (isBlank == false) {
				en.setPKVal(pkVal);
				en.RetrieveFromDBSources();
			}

			// 定义容器.
			DataSet ds = new DataSet();

			// 把主数据放入里面去.
			DataTable dtMain = en.ToDataTableField("MainTable");
			ds.Tables.add(dtMain);

			/// 增加 上方法.
			DataTable dtM = new DataTable("dtM");
			dtM.Columns.Add("No");
			dtM.Columns.Add("Title");
			dtM.Columns.Add("Tip");
			dtM.Columns.Add("Visable", Boolean.class);

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
			// 判断Func是否有参数
			dtM.Columns.Add("FunPara");
			dtM.Columns.Add("ClassMethodName");

			RefMethods rms = map.getHisRefMethods();
			for (RefMethod item : rms) {
				item.HisEn = en;

				String myurl = "";
				if (item.refMethodType == RefMethodType.LinkeWinOpen
						|| item.refMethodType == RefMethodType.RightFrameOpen
						|| item.refMethodType == RefMethodType.LinkModel) {
					try {
						Object tempVar = item.Do(null);
						myurl = tempVar instanceof String ? (String) tempVar : null;
						if (myurl == null) {
							continue;
						}
					} catch (RuntimeException ex) {
						throw new RuntimeException("err@系统错误:根据方法名生成url出现错误:@" + ex.getMessage() + "@" + ex.getCause()
								+ " @方法名:" + item.Title + " - 方法:" + item.ClassMethodName);
					}
				} else {
					myurl = "../RefMethod.htm?Index=" + item.Index + "&EnName=" + en.toString() + "&EnsName="
							+ en.getGetNewEntities().toString() + "&PKVal=" + this.getPKVal();
				}

				DataRow dr = dtM.NewRow();

				dr.setValue("No", item.Index);
				dr.setValue("Title", item.Title);
				dr.setValue("Tip", item.ToolTip);
				dr.setValue("Visable", item.Visable);
				dr.setValue("Warning", item.Warning);

				dr.setValue("RefMethodType", item.refMethodType.getValue());
				dr.setValue("RefAttrKey", item.RefAttrKey);
				dr.setValue("Url", myurl);
				dr.setValue("W", item.Width);
				dr.setValue("H", item.Height);
				dr.setValue("Icon", item.Icon);
				dr.setValue("IsCanBatch", item.IsCanBatch);
				dr.setValue("GroupName", item.GroupName);
				Attrs attrs = item.getHisAttrs();
				if (attrs.size() == 0) {
					dr.setValue("FunPara", "false");
				} else {
					dr.setValue("FunPara", "true");
				}
				dr.setValue("ClassMethodName",item.ClassMethodName);
				dtM.Rows.add(dr); // 增加到rows.
			}

			/// 增加 上方法.

			/// 加入一对多的实体编辑
			AttrsOfOneVSM oneVsM = map.getAttrsOfOneVSM();
			String sql = "";
			int i = 0;
			if (oneVsM.size() > 0) {

				for (AttrOfOneVSM vsM : oneVsM) {
					String rootNo = vsM.RootNo;
					if (rootNo != null && rootNo.contains("@") == true) {
						rootNo = rootNo.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
						rootNo = rootNo.replace("@WebUser.OrgNo", WebUser.getOrgNo());
					}

					// 判断该dot2dot是否显示？
					Entity enMM = vsM.getEnsOfMM().getGetNewEntity();
					enMM.SetValByKey(vsM.getAttrOfOneInMM(), this.getPKVal());
					if (enMM.getHisUAC().IsView == false) {
						continue;
					}
					DataRow dr = dtM.NewRow();
					dr.setValue("No", enMM.toString());
					// dr["GroupName"] = vsM.GroupName;
					if (en.getPKVal() != null) {
						// 判断模式.
						String url = "";
						if (vsM.dot2DotModel == Dot2DotModel.TreeDept) {
							url = "Branches.htm?EnName=" + this.getEnName() + "&Dot2DotEnsName="
									+ vsM.getEnsOfMM().toString();
							url += "&Dot2DotEnName=" + vsM.getEnsOfMM().getGetNewEntity().toString(); // 存储实体类.
							url += "&AttrOfOneInMM=" + vsM.getAttrOfOneInMM(); // 存储表那个与主表关联. 比如: FK_Node
							url += "&AttrOfMInMM=" + vsM.getAttrOfMInMM(); // dot2dot存储表那个与实体表. 比如:FK_Station.
							url += "&EnsOfM=" + vsM.getEnsOfM().toString(); // 默认的B实体分组依据. 比如:FK_Station.
							url += "&DefaultGroupAttrKey=" + vsM.DefaultGroupAttrKey; // 默认的B实体分组依据.
							url += "&RootNo=" + rootNo;

						} else if (vsM.dot2DotModel == Dot2DotModel.TreeDeptEmp) {
							url = "BranchesAndLeaf.htm?EnName=" + this.getEnName() + "&Dot2DotEnsName="
									+ vsM.getEnsOfMM().toString();
							url += "&Dot2DotEnName=" + vsM.getEnsOfMM().getGetNewEntity().toString(); // 存储实体类.
							url += "&AttrOfOneInMM=" + vsM.getAttrOfOneInMM(); // 存储表那个与主表关联. 比如: FK_Node
							url += "&AttrOfMInMM=" + vsM.getAttrOfMInMM(); // dot2dot存储表那个与实体表. 比如:FK_Station.
							url += "&EnsOfM=" + vsM.getEnsOfM().toString(); // 默认的B实体分组依据. 比如:FK_Station.
							url += "&DefaultGroupAttrKey=" + vsM.DefaultGroupAttrKey; // 默认的B实体分组依据. 比如:FK_Station.
							url += "&RootNo=" + rootNo;
						} else {
							url = "Dot2Dot.htm?EnName=" + this.getEnName() + "&Dot2DotEnsName="
									+ vsM.getEnsOfMM().toString(); // 比如:BP.WF.Template.NodeStations
							url += "&AttrOfOneInMM=" + vsM.getAttrOfOneInMM(); // 存储表那个与主表关联. 比如: FK_Node
							url += "&AttrOfMInMM=" + vsM.getAttrOfMInMM(); // dot2dot存储表那个与实体表. 比如:FK_Station.
							url += "&EnsOfM=" + vsM.getEnsOfM().toString(); // 默认的B实体. //比如:bp.port.Stations
							url += "&DefaultGroupAttrKey=" + vsM.DefaultGroupAttrKey; // 默认的B实体分组依据. 比如:FK_Station.

						}

						dr.setValue("Url", url + "&" + en.getPK() + "=" + en.getPKVal() + "&PKVal=" + en.getPKVal());
						dr.setValue("Icon", "../Img/M2M.png");

					}

					dr.setValue("W", "900");
					dr.setValue("H", "500");
					dr.setValue("RefMethodType", RefMethodType.RightFrameOpen.getValue());

					// 获得选择的数量.
					try {
						sql = "SELECT COUNT(*) as NUM FROM "
								+ vsM.getEnsOfMM().getGetNewEntity().getEnMap().getPhysicsTable() + " WHERE "
								+ vsM.getAttrOfOneInMM() + "='" + en.getPKVal() + "'";
						i = DBAccess.RunSQLReturnValInt(sql);
					} catch (java.lang.Exception e) {
						sql = "SELECT COUNT(*) as NUM FROM "
								+ vsM.getEnsOfMM().getGetNewEntity().getEnMap().getPhysicsTable() + " WHERE "
								+ vsM.getAttrOfOneInMM() + "=" + en.getPKVal();
						try {
							i = DBAccess.RunSQLReturnValInt(sql);
						} catch (java.lang.Exception e2) {
							vsM.getEnsOfMM().getGetNewEntity().CheckPhysicsTable();
						}
					}
					dr.setValue("Title", vsM.getDesc() + "(" + i + ")");
					dr.setValue("GroupName",vsM.GroupName);
					dtM.Rows.add(dr);
				}
			}

			/// 增加 一对多.

			/// 从表
			EnDtls enDtls = map.getDtls();
			for (EnDtl enDtl : enDtls) {
				// 判断该dtl是否要显示?
				Entity myEnDtl = enDtl.getEns().getGetNewEntity(); // 获取他的en
				myEnDtl.SetValByKey(enDtl.getRefKey(), this.getPKVal()); // 给refpk赋值.
				if (myEnDtl.getHisUAC().IsView == false) {
					continue;
				}

				DataRow dr = dtM.NewRow();

				String url = "";
				if (enDtl.getDtlEditerModel() == DtlEditerModel.DtlBatch)
					url = "DtlBatch.htm?EnName=" + this.getEnName() + "&PK=" + this.getPKVal() + "&EnsName=" + enDtl.getEnsName()  + "&RefKey=" + enDtl.getRefKey() + "&RefVal=" + en.getPKVal().toString() + "&MainEnsName=" + en.toString();
				else
					url = "DtlSearch.htm?EnName=" + this.getEnName() + "&PK=" + this.getPKVal() + "&EnsName=" + enDtl.getEnsName()  + "&RefKey=" + enDtl.getRefKey() + "&RefVal=" + en.getPKVal().toString() + "&MainEnsName=" + en.toString();

				try {
					i = DBAccess.RunSQLReturnValInt(
							"SELECT COUNT(*) FROM " + enDtl.getEns().getGetNewEntity().getEnMap().getPhysicsTable()
									+ " WHERE " + enDtl.getRefKey() + "='" + en.getPKVal() + "'");
				} catch (java.lang.Exception e3) {
					try {
						i = DBAccess.RunSQLReturnValInt(
								"SELECT COUNT(*) FROM " + enDtl.getEns().getGetNewEntity().getEnMap().getPhysicsTable()
										+ " WHERE " + enDtl.getRefKey() + "=" + en.getPKVal());
					} catch (java.lang.Exception e4) {
						enDtl.getEns().getGetNewEntity().CheckPhysicsTable();
					}
				}

				dr.setValue("No", enDtl.getEnsName());
				dr.setValue("Title", enDtl.getDesc() + "(" + i + ")");
				dr.setValue("Url", url);
				dr.setValue("GroupName", enDtl.getGroupName());
				dr.setValue("Icon",enDtl.Icon);
				dr.setValue("RefMethodType", RefMethodType.RightFrameOpen.getValue());

				dtM.Rows.add(dr);
			}

			/// 增加 从表.

			ds.Tables.add(dtM);

			return bp.tools.Json.ToJson(ds);
		} catch (RuntimeException ex) {
			return "err@Entity_Init错误:" + ex.getMessage();
		}
	}

	///#endregion 实体的操作.

	public final String Branches_SearchByKey() throws Exception {

		String key = this.GetRequestVal("Key"); //查询关键字.

		String ensOfM = this.GetRequestVal("EnsOfM"); //多的实体.
		Entities ensMen = ClassFactory.GetEns(ensOfM);
		QueryObject qo = new QueryObject(ensMen); //集合.
		qo.AddWhere("No", " LIKE ", "%" + key + "%");
		qo.addOr();
		qo.AddWhere("Name", " LIKE ", "%" + key + "%");
		qo.DoQuery();

		return ensMen.ToJson("dt");
	}


	///#region 部门人员模式.
	public final String BranchesAndLeaf_SearchByNodeID() throws Exception {
		String dot2DotEnsName = this.GetRequestVal("Dot2DotEnsName");
		String defaultGroupAttrKey = this.GetRequestVal("DefaultGroupAttrKey");
		String key = this.GetRequestVal("Key"); //查询关键字.
		String ensOfM = this.GetRequestVal("EnsOfM"); //多的实体.

		//如果是部门人员信息，关联的有兼职部门.
		String emp1s = bp.sys.base.Glo.DealClassEntityName("bp.port.Emps");
		String emp2s = bp.sys.base.Glo.DealClassEntityName("bp.port.Emps");

		if ((ensOfM.equals(emp1s) == true || ensOfM.equals(emp2s) == true) && defaultGroupAttrKey.equals("FK_Dept") == true)
		{
			String sql = "Select  E." + bp.sys.base.Glo.getUserNo() + " , E.Name ,D.Name AS FK_DeptText,-1 AS TYPE  From Port_DeptEmp DE, Port_Emp E,Port_Dept D Where DE.FK_Emp = E.No And DE.FK_Dept = D.No AND  D.No='" + key + "'";

			sql += " union ";
			sql += "select  E." + bp.sys.base.Glo.getUserNo() + " , E.Name ,D.Name AS FK_DeptText,0 AS TYPE From Port_Emp E,Port_Dept D Where E.Fk_Dept = D.No AND  D.No='" + key + "' ORDER BY TYPE DESC";
			DataTable dtt = DBAccess.RunSQLReturnTable(sql);
			DataTable dt = new DataTable();
			dt.Columns.Add("No", String.class);
			dt.Columns.Add("Name", String.class);
			dt.Columns.Add("FK_DeptText", String.class);
			dt.Columns.Add("type", String.class);
			String emps = "";
			for (DataRow drr : dtt.Rows)
			{
				if (emps.contains(drr.get("No").toString() + ",") == true)
				{
					continue;
				}
				emps += drr.get("No").toString() + ",";

				DataRow dr = dt.NewRow();
				dr.setValue("No", drr.get("No"));
				dr.setValue("Name", drr.get("Name"));
				dr.setValue("FK_DeptText", drr.get("FK_DeptText"));
				dr.setValue("type", drr.get("TYPE"));
				dt.Rows.add(dr);
			}
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
					case "fk_depttext":
						col.ColumnName = "FK_DeptText";
						break;
					case "type":
						col.ColumnName = "TYPE";
						break;
					default:
						break;
				}
			}
			return bp.tools.Json.ToJson(dt);
		}



		Entities ensMen = ClassFactory.GetEns(ensOfM);
		QueryObject qo = new QueryObject(ensMen); //集合.
		qo.AddWhere(defaultGroupAttrKey, key);
		qo.DoQuery();


		return ensMen.ToJson("dt");
	}
	public final String BranchesAndLeaf_SearchByKey() throws Exception {
		String dot2DotEnsName = this.GetRequestVal("Dot2DotEnsName");
		String defaultGroupAttrKey = this.GetRequestVal("DefaultGroupAttrKey");

		String key = this.GetRequestVal("Key"); //查询关键字.
		String rootno = this.GetRequestVal("RootNo"); //查询根节点.

		String ensOfM = this.GetRequestVal("EnsOfM"); //多的实体.
		Entities ensMen = ClassFactory.GetEns(ensOfM);
		QueryObject qo = new QueryObject(ensMen); //集合.
		qo.addLeftBracket();
		qo.AddWhere("No", " LIKE ", "%" + key + "%");
		qo.addOr();
		qo.AddWhere("Name", " LIKE ", "%" + key + "%");
		qo.addRightBracket();
		if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			qo.addAnd();
			qo.AddWhere("OrgNo", WebUser.getOrgNo());
		}
		qo.DoQuery();

		return ensMen.ToJson("dt");
	}
	public final String BranchesAndLeaf_Delete() throws Exception {
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
	 初始化		dt.Rows[3]	error CS0103: 当前上下文中不存在名称"dt”
	 */

	/**
	 @return
	 */
	public final String BranchesAndLeaf_Init() throws Exception {
		String dot2DotEnsName = this.GetRequestVal("Dot2DotEnsName");
		String defaultGroupAttrKey = this.GetRequestVal("DefaultGroupAttrKey");
		dot2DotEnsName = bp.sys.Glo.DealClassEntityName(dot2DotEnsName);
		Entity en = ClassFactory.GetEn(this.getEnName());
		en.setPKVal(this.getPKVal());
		en.Retrieve();

		// 找到映射.
		AttrsOfOneVSM oneVsM = en.getEnMap().getAttrsOfOneVSM();
		AttrOfOneVSM vsM = null;
		for (AttrOfOneVSM item : oneVsM) {
			// if (item.Dot2DotModel != Dot2DotModel.TreeDeptEmp)
			// continue;

			// if (item.EnsOfMM.ToString().Equals(dot2DotEnsName) == false)
			// continue;

			// if (item.DefaultGroupAttrKey == null)
			// continue;

			// if (item.DefaultGroupAttrKey.Equals(dot2DotEnsName) == false)
			// continue;

			// vsM = item;
			// break;

			if (item.dot2DotModel == Dot2DotModel.TreeDeptEmp && item.getEnsOfMM().toString().equals(dot2DotEnsName)
					&& item.DefaultGroupAttrKey.equals(defaultGroupAttrKey)) {
				vsM = item;
				break;
			}
		}
		if (vsM == null) {
			return "err@参数错误,没有找到VSM";
		}

		// 组织数据.
		DataSet ds = new DataSet();
		String rootNo = GetRequestVal("RootNo");
		if (DataType.IsNullOrEmpty(rootNo) == true) {
			rootNo = vsM.RootNo;
		}
		if (rootNo.equals("@WebUser.FK_Dept") || rootNo.equals("WebUser.FK_Dept")) {
			rootNo = WebUser.getFK_Dept();
		}
		if (rootNo.equals("@WebUser.OrgNo") || rootNo.equals("WebUser.OrgNo")) {
			rootNo = WebUser.getOrgNo();
		}

		if (DataType.IsNullOrEmpty(rootNo) == true)
			rootNo = "0";
		/// 生成树目录.
		String ensOfM = this.GetRequestVal("EnsOfM"); // 多的实体.
		Entities ensMen = ClassFactory.GetEns(ensOfM);
		Entity enMen = ensMen.getGetNewEntity();

		Attr attr = enMen.getEnMap().GetAttrByKey(defaultGroupAttrKey);
		if (attr == null) {
			return "err@在实体[" + ensOfM + "]指定的分树的属性[" + defaultGroupAttrKey + "]不存在，请确认是否删除了该属性?";
		}

		if (attr.getMyFieldType() == FieldType.Normal && attr.getUIContralType() != UIContralType.DDL) {
			return "err@在实体[" + ensOfM + "]指定的分树的属性[" + defaultGroupAttrKey + "]不能是普通字段，必须是外键或者枚举.";
		}

		Entities trees = attr.getHisFKEns();
		Entity tree = trees.getGetNewEntity();

		int IsExitParentNo = 0; // 是否存在ParentNo

		int IsExitIdx = 0; // 判断改类是否存在Idx
		if(tree.getClass().getName().endsWith("GENoName")){
			IsExitParentNo =0;
			IsExitIdx =0;
		}else{
			if (DBAccess.IsExitsTableCol(tree.getEnMap().getPhysicsTable(), "Idx") == true
					&& tree.getEnMap().getAttrs().contains("Idx") == true) {
				IsExitIdx = 1;
			}

			if (DBAccess.IsExitsTableCol(tree.getEnMap().getPhysicsTable(), "ParentNo") == true
					&& tree.getEnMap().getAttrs().contains("ParentNo") == true) {
				IsExitParentNo = 1;
			}
		}


		if (IsExitParentNo == 1) {
			if (IsExitIdx == 1) {
				if (rootNo.equals("0")) {
					trees.Retrieve("ParentNo", rootNo, "Idx");
				} else {
					trees.Retrieve("No", rootNo, "Idx");
				}
			} else {
				if (rootNo.equals("0")) {
					trees.Retrieve("ParentNo", rootNo);
				} else {
					trees.Retrieve("No", rootNo);
				}
			}
		} else {
			if (IsExitIdx == 1) {
				trees.RetrieveAll("Idx");
			} else if(tree.getClass().getName().endsWith("GENoName")==false){
				trees.RetrieveAll();
			}
		}

		DataTable dt = trees.ToDataTableField("DBTrees");
		// 如果没有parnetNo 列，就增加上, 有可能是分组显示使用这个模式.
		if (dt.Columns.contains("ParentNo") == false) {
			dt.Columns.Add("ParentNo");
			for (DataRow dr : dt.Rows) {
				dr.setValue("ParentNo", rootNo);
			}
		}
		ds.Tables.add(dt);
		dt = new DataTable();
		dt.TableName = "Base_Info";
		dt.Columns.Add("IsExitParentNo", Integer.class);
		dt.Columns.Add("ExtShowCols", String.class);
		DataRow drr = dt.NewRow();
		drr.setValue("IsExitParentNo", IsExitParentNo);
		drr.setValue("ExtShowCols", vsM.ExtShowCols);
		dt.Rows.add(drr);
		ds.Tables.add(dt);

		/// 生成树目录.

		/// 生成选择的数据.
		boolean saveType = this.GetRequestValBoolen("SaveType");
		Entities dot2Dots = ClassFactory.GetEns(dot2DotEnsName);
		DataTable dtSelected = null;
		if (saveType == true) {
			// 选择的值保存在一个字段中
			String para = this.GetRequestVal("Para");
			String paraVal = this.GetRequestVal("ParaVal");

			String para1 = this.GetRequestVal("Para1");
			String paraVal1 = this.GetRequestVal("ParaVal1");

			String pkval = this.getPKVal();

			if (DataType.IsNullOrEmpty(para) == true) {
				dot2Dots.Retrieve(vsM.getAttrOfOneInMM(), this.getPKVal());
			} else if (DataType.IsNullOrEmpty(para1) == true) {
				pkval = pkval.replace("_" + paraVal, "");
				dot2Dots.Retrieve(vsM.getAttrOfOneInMM(), pkval, para, paraVal);
			}

			else if (DataType.IsNullOrEmpty(para) == false && DataType.IsNullOrEmpty(para1) == false) {
				pkval = pkval.replace("_" + paraVal, "");
				dot2Dots.Retrieve(vsM.getAttrOfOneInMM(), pkval, para, paraVal, para1, paraVal1);
			}

		} else {
			dot2Dots.Retrieve(vsM.getAttrOfOneInMM(), this.getPKVal());
		}
		dtSelected = dot2Dots.ToDataTableField("DBMMs");

		String attrOfMInMM = this.GetRequestVal("AttrOfMInMM");
		String AttrOfOneInMM = this.GetRequestVal("AttrOfOneInMM");

		dtSelected.Columns.get(attrOfMInMM).setColumnName("No");

		if (dtSelected.Columns.contains(attrOfMInMM + "Text") == false && saveType == false) {
			return "err@MM实体类字段属性需要按照外键属性编写:" + dot2DotEnsName + " - " + attrOfMInMM;
		}

		if (saveType == false) {
			dtSelected.Columns.get(attrOfMInMM + "Text").setColumnName("Name");
		}

		if (DataType.IsNullOrEmpty(vsM.ExtShowCols) == false
				&& vsM.ExtShowCols.contains("@" + defaultGroupAttrKey + "=") == true) {
			if (dtSelected.Columns.contains(defaultGroupAttrKey) == false) {
				dtSelected.Columns.Add(defaultGroupAttrKey, String.class);
			}
			for (DataRow dr : dtSelected.Rows) {
				enMen.setPKVal(dr.getValue("No").toString());
				enMen.RetrieveFromDBSources();
				dr.setValue(defaultGroupAttrKey, enMen.getRow().GetValByKey(defaultGroupAttrKey + "Text").toString());
			}
		}

		dtSelected.Columns.remove(AttrOfOneInMM);
		ds.Tables.add(dtSelected); // 已经选择的数据.

		/// 生成选择的数据.

		return bp.tools.Json.ToJson(ds);
	}


	public final String BranchesAndLeaf_GetTreesByParentNo() throws Exception {
		String rootNo = GetRequestVal("RootNo");
		if (DataType.IsNullOrEmpty(rootNo))
		{
			rootNo = "0";
		}

		String defaultGroupAttrKey = this.GetRequestVal("DefaultGroupAttrKey");
		String ensOfM = this.GetRequestVal("EnsOfM"); //多的实体.
		Entities ensMen = ClassFactory.GetEns(ensOfM);
		Entity enMen = ensMen.getGetNewEntity();

		Attr attr = enMen.getEnMap().GetAttrByKey(defaultGroupAttrKey);
		if (attr == null)
		{
			return "err@在实体[" + ensOfM + "]指定的分树的属性[" + defaultGroupAttrKey + "]不存在，请确认是否删除了该属性?";
		}

		if (attr.getMyFieldType() == FieldType.Normal && attr.getUIContralType()!=UIContralType.DDL)
		{
			return "err@在实体[" + ensOfM + "]指定的分树的属性[" + defaultGroupAttrKey + "]不能是普通字段，必须是外键或者枚举.";
		}

		Entities trees = attr.getHisFKEns();
		//判断改类是否存在Idx
		Entity tree = trees.getGetNewEntity();
		if(tree.getClass().getName().endsWith("GENoName")==false){
			if (DBAccess.IsExitsTableCol(tree.getEnMap().getPhysicsTable(), "Idx") == true && tree.getEnMap().getAttrs().contains("Idx") == true)
			{
				trees.Retrieve("ParentNo",rootNo,"Idx");
			}
			else
			{
				trees.Retrieve("ParentNo", rootNo, null);
			}
		}

		DataTable dt = trees.ToDataTableField("DBTrees");
		//如果没有parnetNo 列，就增加上, 有可能是分组显示使用这个模式.
		if (dt.Columns.contains("ParentNo") == false)
		{
			dt.Columns.Add("ParentNo");
			for (DataRow dr : dt.Rows)
			{
				dr.setValue("ParentNo", rootNo);
			}
		}
		return bp.tools.Json.ToJson(dt);

	}

	///#endregion 部门人员模式.


	///#region 分组数据.
	/**
	 执行保存

	 @return
	 */
	public final String Dot2Dot_Save() throws Exception {

		try
		{
			String eles = this.GetRequestVal("ElesAAA");

			//实体集合.
			String dot2DotEnsName = this.GetRequestVal("Dot2DotEnsName");
			String attrOfOneInMM = this.GetRequestVal("AttrOfOneInMM");
			String attrOfMInMM = this.GetRequestVal("AttrOfMInMM");
			boolean saveType = this.GetRequestValBoolen("SaveType");
			//获得点对点的实体.
			Entity en = ClassFactory.GetEns(dot2DotEnsName).getGetNewEntity();
			if (saveType == true)
			{
				//选择的值保存在一个字段中
				String para = this.GetRequestVal("Para");
				String paraVal = this.GetRequestVal("ParaVal");

				String para1 = this.GetRequestVal("Para1");
				String paraVal1 = this.GetRequestVal("ParaVal1");

				//首先删除.
				if (DataType.IsNullOrEmpty(para) == true)
				{
					en.Delete(attrOfOneInMM, this.getPKVal());
				}
				else if (DataType.IsNullOrEmpty(para1) == true)
				{
					en.Delete(attrOfOneInMM, this.getPKVal(), para, paraVal);
				}
				else if (DataType.IsNullOrEmpty(para) == false && DataType.IsNullOrEmpty(para1) == false)
				{
					en.Delete(attrOfOneInMM, this.getPKVal(), para, paraVal, para1, paraVal1);
				}

				if (DataType.IsNullOrEmpty(eles) == true)
				{
					return "没有选择值";
				}
				en.SetValByKey(attrOfOneInMM, this.getPKVal());
				en.SetValByKey(attrOfMInMM, eles);
				if (en.getRow().containsKey(para))
				{
					en.SetValByKey(para, paraVal);
				}
				if (en.getRow().containsKey(para1))
				{
					en.SetValByKey(para1, paraVal1);
				}

				en.Insert();
				return "数据保存成功.";

			}



			en.Delete(attrOfOneInMM, this.getPKVal());

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
	public final String Dot2Dot_GenerGroupEntitis() throws Exception {
		String key = this.GetRequestVal("DefaultGroupAttrKey");

		//实体集合.
		String ensName = this.GetRequestVal("EnsOfM");
		Entities ens = ClassFactory.GetEns(ensName);
		Entity en = ens.getGetNewEntity();

		Attrs attrs = en.getEnMap().getAttrs();
		Attr attr = attrs.GetAttrByKey(key);

		if (attr == null)
		{
			return "err@设置的分组外键错误[" + key + "],不存在[" + ensName + "]或者已经被删除.";
		}

		if (attr.getMyFieldType() == FieldType.Normal && attr.getUIContralType()!=UIContralType.DDL)
		{
			return "err@设置的默认分组[" + key + "]不能是普通字段.";
		}

		if (attr.getMyFieldType() == FieldType.FK)
		{
			Entities ensFK = attr.getHisFKEns();
			ensFK.clear();
			ensFK.RetrieveAll();
			return ensFK.ToJson("dt");
		}
		if (attr.getUIContralType()==UIContralType.DDL && DataType.IsNullOrEmpty(attr.getUIBindKey())==false
		 && attr.getUIBindKey().toUpperCase().startsWith("SELECT"))
		{
			String sqlBindKey = Glo.DealExp(attr.getUIBindKey(), en, null);

			DataTable dt = DBAccess.RunSQLReturnTable(sqlBindKey);
			if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.UpperCase)
			{
				dt.Columns.get("NO").ColumnName = "No";
				dt.Columns.get("NAME").ColumnName = "Name";
			}
			if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.Lowercase)
			{
				dt.Columns.get("no").ColumnName = "No";
				dt.Columns.get("name").ColumnName = "Name";
			}
			return bp.tools.Json.ToJson(dt);
		}

		if (attr.getMyFieldType() == FieldType.Enum)
		{
			/* 如果是枚举 */
			SysEnums ses = new SysEnums();
			ses.Retrieve(SysEnumAttr.IntKey, attr.getUIBindKey(), null);
		}

		return "err@设置的默认分组[" + key + "]不能是普通字段.";
	}

	///#endregion 分组数据.


}