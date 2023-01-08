package bp.sys;

import bp.difference.StringHelper;
import bp.difference.SystemConfig;
import bp.en.*;
import bp.da.*;
import bp.pub.PubClass;
import bp.web.WebUser;

import java.util.*;
import java.io.*;
import java.nio.file.*;

/** 
 表单API
*/
public class CCFormAPI
{
	/** 
	 获得附件信息.
	 
	 param fk_mapdata 表单ID
	 param pk 主键
	 @return 附件信息.
	*/
	public static String GetAthInfos(String fk_mapdata, String pk)
	{
		int num = DBAccess.RunSQL("SELECT COUNT(MYPK) FROM Sys_FrmAttachmentDB WHERE FK_MapData='" + fk_mapdata + "' AND RefPKVal=" + pk);
		return "附件(" + num + ")";
	}
	public static DataSet GenerDBForCCFormDtl(String frmID, MapDtl dtl, int pkval, String atParas) throws Exception
	{
		return GenerDBForCCFormDtl(frmID, dtl, pkval, atParas, "0");
	}

	public static DataSet GenerDBForCCFormDtl(String frmID, MapDtl dtl, int pkval, String atParas, String dtlRefPKVal) throws Exception
	{
		//数据容器,就是要返回的对象.
		DataSet myds = new DataSet();

		//实体.
		GEEntity en = new GEEntity(frmID);
		en.setOID(pkval);
		if (en.RetrieveFromDBSources() == 0)
		{
			en.Insert();
		}

		//把参数放入到 En 的 Row 里面。
		if (DataType.IsNullOrEmpty(atParas) == false)
		{
			AtPara ap = new AtPara(atParas);
			for (String key : ap.getHisHT().keySet())
			{
				try
				{
					if (en.getRow().containsKey(key) == true) //有就该变.
						en.getRow().SetValByKey(key,ap.GetValStrByKey(key));
					else
						en.getRow().put(key, ap.GetValStrByKey(key)); //增加他.
				}
				catch (RuntimeException ex)
				{
					throw new RuntimeException(key);
				}
			}
		}
		if (SystemConfig.getIsBSsystem() == true)
		{
			// 处理传递过来的参数。
			Enumeration enu = bp.sys.Glo.getRequest().getParameterNames();
			while (enu.hasMoreElements()) {
				String k = (String) enu.nextElement();
				en.SetValByKey(k, bp.sys.Glo.getRequest().getParameter(k));
			}

		}



		///加载从表表单模版信息.

		DataTable Sys_MapDtl = dtl.ToDataTableField("Sys_MapDtl");
		myds.Tables.add(Sys_MapDtl);

		//明细表的表单描述
		DataTable Sys_MapAttr = dtl.getMapAttrs().ToDataTableField("Sys_MapAttr");
		myds.Tables.add(Sys_MapAttr);

		//明细表的配置信息.
		DataTable Sys_MapExt = dtl.getMapExts().ToDataTableField("Sys_MapExt");
		myds.Tables.add(Sys_MapExt);

		//启用附件，增加附件信息
		DataTable Sys_FrmAttachment = dtl.getFrmAttachments().ToDataTableField("Sys_FrmAttachment");
		myds.Tables.add(Sys_FrmAttachment);
		/// 加载从表表单模版信息.


		///把从表的- 外键表/枚举 加入 DataSet.
		MapExts mes = dtl.getMapExts();
		MapExt me = null;

		DataTable ddlTable = new DataTable();
		ddlTable.Columns.Add("No");
		for (DataRow dr : Sys_MapAttr.Rows)
		{
			String lgType = dr.getValue("LGType").toString();
			String ctrlType = dr.getValue(MapAttrAttr.UIContralType).toString();

			//没有绑定外键
			String uiBindKey = dr.getValue("UIBindKey").toString();
			if (DataType.IsNullOrEmpty(uiBindKey) == true)
			{
				continue;
			}

			String mypk = dr.getValue("MyPK").toString();


			///枚举字段
			if (lgType.equals("1") == true)
			{
				// 如果是枚举值, 判断是否存在.
				if (myds.GetTableByName(uiBindKey) !=null)
				{
					continue;
				}

				String mysql = "SELECT IntKey AS No, Lab as Name FROM "+bp.sys.base.Glo.SysEnum()+" WHERE EnumKey='" + uiBindKey + "' ORDER BY IntKey ";
				DataTable dtEnum = DBAccess.RunSQLReturnTable(mysql);
				dtEnum.TableName = uiBindKey;

				dtEnum.Columns.get(0).ColumnName = "No";
				dtEnum.Columns.get(1).ColumnName = "Name";

				myds.Tables.add(dtEnum);
				continue;
			}

			///

			String UIIsEnable = dr.getValue("UIIsEnable").toString();
			// 检查是否有下拉框自动填充。
			String keyOfEn = dr.getValue("KeyOfEn").toString();


			///处理下拉框数据范围. for 小杨.
			Object tempVar = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL, MapExtAttr.AttrOfOper, keyOfEn);
			me = tempVar instanceof MapExt ? (MapExt)tempVar : null;
			if (me != null && myds.GetTableByName(uiBindKey) ==null) //是否存在.
			{
				Object tempVar2 = me.getDoc();
				String fullSQL = tempVar2 instanceof String ? (String)tempVar2 : null;
				fullSQL = fullSQL.replace("~", "'");
				fullSQL = bp.wf.Glo.DealExp(fullSQL, en, null);

				if (DataType.IsNullOrEmpty(fullSQL) == true)
				{
					throw new RuntimeException("err@没有给AutoFullDLL配置SQL：MapExt：=" + me.getMyPK() + ",原始的配置SQL为:" + me.getDoc());
				}

				DataTable dt = DBAccess.RunSQLReturnTable(fullSQL);

				dt.TableName = uiBindKey;

				if (SystemConfig.getAppCenterDBType() == DBType.Oracle
						|| SystemConfig.getAppCenterDBType().equals(DBType.KingBaseR3)
						|| SystemConfig.getAppCenterDBType() == DBType.KingBaseR6)
				{
					if (dt.Columns.contains("NO") == true)
					{
						dt.Columns.get("NO").ColumnName = "No";
					}
					if (dt.Columns.contains("NAME") == true)
					{
						dt.Columns.get("NAME").ColumnName = "Name";
					}
					if (dt.Columns.contains("PARENTNO") == true)
					{
						dt.Columns.get("PARENTNO").ColumnName = "ParentNo";
					}
				}

				if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
				{
					if (dt.Columns.contains("no") == true)
					{
						dt.Columns.get("no").ColumnName = "No";
					}
					if (dt.Columns.contains("name") == true)
					{
						dt.Columns.get("name").ColumnName = "Name";
					}
					if (dt.Columns.contains("parentno") == true)
					{
						dt.Columns.get("parentno").ColumnName = "ParentNo";
					}
				}

				myds.Tables.add(dt);
				continue;
			}

			/// 处理下拉框数据范围.


			///外键字段

			// 判断是否存在.
			if (myds.GetTableByName(uiBindKey) !=null)
			{
				continue;
			}

			// 获得数据.
			DataTable mydt = PubClass.GetDataTableByUIBineKey(uiBindKey,en.getRow());

			if (mydt == null)
			{
				DataRow ddldr = ddlTable.NewRow();
				ddldr.setValue("No", uiBindKey);
				ddlTable.Rows.add(ddldr);
			}
			else
			{
				myds.Tables.add(mydt);
			}

			/// 外键字段
		}
		ddlTable.TableName = "UIBindKey";
		myds.Tables.add(ddlTable);

		/// 把从表的- 外键表/枚举 加入 DataSet.


		///把主表数据放入.

		//重设默认值.
		en.ResetDefaultVal();


		//增加主表数据.
		DataTable mainTable = en.ToDataTableField(frmID);
		mainTable.TableName = "MainTable";
		myds.Tables.add(mainTable);

		/// 把主表数据放入.


		/// 把从表的数据放入.
		GEDtls dtls = new GEDtls(dtl.getNo());
		DataTable dtDtl = GetDtlInfo(dtl, dtls, en,dtlRefPKVal);


		// 为明细表设置默认值.
		MapAttrs dtlAttrs = new MapAttrs(dtl.getNo());
		for (MapAttr attr : dtlAttrs.ToJavaList())
		{
			if (attr.getUIContralType()== UIContralType.TB)
				continue;


			//处理它的默认值.
			if (attr.getDefValReal().contains("@") == false)
				continue;

			for (DataRow dr : dtDtl.Rows)
			{
				if (dr.getValue(attr.getKeyOfEn()) == null || DataType.IsNullOrEmpty(dr.getValue(attr.getKeyOfEn()).toString()) == true)
				{
					dr.setValue(attr.getKeyOfEn(), attr.getDefVal());
				}
			}

		}

		dtDtl.TableName = "DBDtl"; //修改明细表的名称.
		myds.Tables.add(dtDtl); //加入这个明细表, 如果没有数据，xml体现为空.

		/// 把从表的数据放入.


		//放入一个空白的实体，用与获取默认值.
		GEDtl dtlBlank = dtls.getGetNewEntity() instanceof GEDtl ? (GEDtl)dtls.getGetNewEntity() : null;
		dtlBlank.ResetDefaultVal();

		myds.Tables.add(dtlBlank.ToDataTableField("Blank"));

		// myds.WriteXml("c:\\xx.xml");

		return myds;
	}
		///#region 创建修改字段.
	/** 
	 创建通用组件入口
	 
	 param fk_mapdata 表单ID
	 param ctrlType 控件类型
	 param no 编号
	 param name 名称
	 param x 位置x
	 param y 位置y
	*/
	public static void CreatePublicNoNameCtrl(String fk_mapdata, String ctrlType, String no, String name, float x, float y) throws Exception {
		switch (ctrlType)
		{
			case "Dtl":
				CreateOrSaveDtl(fk_mapdata, no, name);
				break;
			case "AthMulti":
				CreateOrSaveAthMulti(fk_mapdata, no, name);
				break;
			case "AthSingle":
				CreateOrSaveAthSingle(fk_mapdata, no, name);
				break;
			case "AthImg":
				CreateOrSaveAthImg(fk_mapdata, no, name, x, y);
				break;
			case "iFrame": //框架.
				MapFrame mapFrame = new MapFrame();
				mapFrame.setMyPK(fk_mapdata + "_" + no);
				if (mapFrame.RetrieveFromDBSources() != 0)
				{
					throw new RuntimeException("@创建失败，已经有同名元素[" + no + "]的控件.");
				}
				mapFrame.setFK_MapData(fk_mapdata);
				mapFrame.setEleType("iFrame");
				mapFrame.setName(name);
				mapFrame.setFrmID(no);
				mapFrame.setURL("http://ccflow.org");
				mapFrame.setX(x);
				mapFrame.setY(y);
				mapFrame.setW(400);
				mapFrame.setH(600);
				mapFrame.Insert();
				break;

			case "HandSiganture": //签字版
				//检查是否可以创建字段? 
				MapData md = new MapData(fk_mapdata);
				md.CheckPTableSaveModel(no);

				MapAttr ma = new MapAttr();
				ma.setFK_MapData(fk_mapdata);
				ma.setKeyOfEn(no);
				ma.setName(name);
				ma.setMyDataType(DataType.AppString);
				ma.setUIContralType(UIContralType.HandWriting);
				ma.setX(x);
				ma.setY(y);
				//frmID设置字段所属的分组
				GroupField groupField = new GroupField();
				groupField.Retrieve(GroupFieldAttr.FrmID, fk_mapdata, GroupFieldAttr.CtrlType, "");
				ma.setGroupID((int)groupField.getOID());
				ma.Insert();
				break;
			default:
				throw new RuntimeException("@没有判断的存储控件:" + ctrlType + ",存储该控件前,需要做判断.");
		}
	}
	/** 
	 创建/修改-图片附件
	 
	 param fk_mapdata 表单ID
	 param no 明细表编号
	 param name 名称
	 param x 位置x
	 param y 位置y
	*/
	public static void CreateOrSaveAthImg(String fk_mapdata, String no, String name, float x, float y) throws Exception {
		no = no.trim();
		name = name.trim();

		FrmImgAth ath = new FrmImgAth();
		ath.setFK_MapData(fk_mapdata);
		ath.setCtrlID(no);
		ath.setMyPK(fk_mapdata + "_" + no);

		ath.setX(x);
		ath.setY(y);
		ath.Insert();
	}
	/** 
	 创建/修改-多附件
	 
	 param fk_mapdata 表单ID
	 param no 明细表编号
	 param name 名称
	 param x 位置x
	 param y 位置y
	*/
	public static void CreateOrSaveAthSingle(String fk_mapdata, String no, String name) throws Exception {
		FrmAttachment ath = new FrmAttachment();
		ath.setFK_MapData(fk_mapdata);
		ath.setNoOfObj(no);

		ath.setMyPK(ath.getFK_MapData() + "_" + ath.getNoOfObj());
		ath.RetrieveFromDBSources();
		ath.setUploadType(AttachmentUploadType.Single);
		ath.setName(name);
		ath.Save();
	}
	/** 
	 创建/修改-多附件
	 
	 param fk_mapdata 表单ID
	 param no 明细表编号
	 param name 名称
	 param x
	 param y
	*/
	public static void CreateOrSaveAthMulti(String fk_mapdata, String no, String name) throws Exception {
		FrmAttachment ath = new FrmAttachment();
		ath.setFK_MapData(fk_mapdata);
		ath.setNoOfObj(no);
		ath.setMyPK(ath.getFK_MapData() + "_" + ath.getNoOfObj());
		int i = ath.RetrieveFromDBSources();

		if (i == 0&& fk_mapdata.contains("ND") == true)
			ath.setHisCtrlWay(AthCtrlWay.WorkID);



		ath.setUploadType(AttachmentUploadType.Multi);
		ath.setName(name);

		//默认在移动端显示
		ath.SetPara("IsShowMobile", 1);
		ath.Save();
	}
	/** 
	 创建/修改一个明细表
	 
	 param fk_mapdata 表单ID
	 param dtlNo 明细表编号
	 param dtlName 名称
	*/
	public static void CreateOrSaveDtl(String fk_mapdata, String dtlNo, String dtlName) throws Exception {
		MapDtl dtl = new MapDtl();
		dtl.setNo(dtlNo);

		if (dtl.RetrieveFromDBSources() == 0)
		{
			if (dtlName == null)
			{
				dtlName = dtlNo;
			}

			//把他的模式复制过来.
			MapData md = new MapData(fk_mapdata);
			dtl.setPTableModel(md.getPTableModel());

			dtl.setW(500);
		}

		dtl.setName(dtlName);
		dtl.setFK_MapData(fk_mapdata);

		dtl.Save();

		//初始化他的map.
		dtl.IntMapAttrs();
	}
	/** 
	 创建一个外部数据字段
	 
	 param fk_mapdata 表单ID
	 param fieldName 字段名
	 param fieldDesc 字段中文名
	 param fk_SFTable 外键表
	 param x 位置
	 param y 位置
	*/

	public static void SaveFieldSFTable(String fk_mapdata, String fieldName, String fieldDesc, String fk_SFTable, float x, float y) throws Exception {
		SaveFieldSFTable(fk_mapdata, fieldName, fieldDesc, fk_SFTable, x, y, 1);
	}


	public static void SaveFieldSFTable(String fk_mapdata, String fieldName, String fieldDesc, String fk_SFTable, float x, float y, int colSpan) throws Exception {
		//检查是否可以创建字段? 
		MapData md = new MapData();
		md.setNo(fk_mapdata);
		if (md.RetrieveFromDBSources() == 1)
		{
			md.CheckPTableSaveModel(fieldName);
		}

		//外键字段表.
		SFTable sf = new SFTable(fk_SFTable);

		if (DataType.IsNullOrEmpty(fieldDesc) == true)
		{
			fieldDesc = sf.getName();
		}

		MapAttr attr = new MapAttr();
		attr.setMyPK(fk_mapdata + "_" + fieldName);
		attr.RetrieveFromDBSources();

		//基本属性赋值.
		attr.setFK_MapData(fk_mapdata);
		attr.setKeyOfEn(fieldName);
		attr.setName(fieldDesc);
		attr.setMyDataType(DataType.AppString);

		attr.setUIContralType(UIContralType.DDL);
		attr.setUIBindKey(fk_SFTable); //绑定信息.
		//如果绑定的外键是树形结构的，在AtPara中增加标识
		if (sf.getCodeStruct() == CodeStruct.Tree)
		{
			attr.SetPara("CodeStruct", 1);
		}
		if (DataType.IsNullOrEmpty(sf.getRootVal()) == false)
		{
			attr.SetPara("ParentNo", sf.getRootVal());
		}
		attr.setX(x);
		attr.setY(y);

		//根据外键表的类型不同，设置它的LGType.
		switch (sf.getSrcType())
		{
			case CreateTable:
			case TableOrView:
			case BPClass:
				attr.setLGType(FieldTypeS.FK);
				break;
			case SQL: //是sql模式.
			default:
				attr.setLGType(FieldTypeS.Normal);
				break;
		}

		//frmID设置字段所属的分组
		GroupField groupField = new GroupField();
		groupField.Retrieve(GroupFieldAttr.FrmID, fk_mapdata, GroupFieldAttr.CtrlType, "");
		attr.setGroupID((int)groupField.getOID());
		if (attr.RetrieveFromDBSources() == 0)
		{
			attr.Insert();
		}
		else
		{
			attr.Update();
		}

		//如果是普通的字段, 这个属于外部数据类型,或者webservices类型. sql 语句类型.
		if (attr.getLGType() == FieldTypeS.Normal)
		{
			MapAttr attrH = new MapAttr();
			attrH.Copy(attr);
			attrH.setUIBindKey("");
			attrH.SetPara("CodeStruct", "");
			attrH.SetPara("ParentNo", "");
			attrH.setKeyOfEn(attr.getKeyOfEn() + "T");
			attrH.setName(attr.getName());
			attrH.setUIContralType(UIContralType.TB);
			attrH.setMinLen(0);
			attrH.setMaxLen(500);
			attrH.setMyDataType(DataType.AppString);
			attrH.setUIVisible(false);
			attrH.setUIIsEnable(false);
			attrH.setMyPK(attrH.getFK_MapData() + "_" + attrH.getKeyOfEn());
			if (attrH.RetrieveFromDBSources() == 0)
			{
				attrH.Insert();
			}
			else
			{
				attrH.Update();
			}
		}
	}
	/** 
	 保存枚举字段
	 
	 param fk_mapdata 表单ID
	 param fieldName 字段名
	 param fieldDesc 字段描述
	 param enumKey 枚举值
	 param ctrlType 显示的控件类型
	 param x 位置x
	 param y 位置y
	*/

	public static void SaveFieldEnum(String fk_mapdata, String fieldName, String fieldDesc, String enumKey, UIContralType ctrlType, float x, float y) throws Exception {
		SaveFieldEnum(fk_mapdata, fieldName, fieldDesc, enumKey, ctrlType, x, y, 1);
	}


	public static void SaveFieldEnum(String fk_mapdata, String fieldName, String fieldDesc, String enumKey, UIContralType ctrlType, float x, float y, int colSpan) throws Exception {
		MapAttr ma = new MapAttr();
		ma.setFK_MapData(fk_mapdata);
		ma.setKeyOfEn(fieldName);

		//赋值主键。
		ma.setMyPK(ma.getFK_MapData() + "_" + ma.getKeyOfEn());

		//先查询赋值.
		ma.RetrieveFromDBSources();

		ma.setName(fieldDesc);
		ma.setMyDataType(DataType.AppInt);
		ma.setX(x);
		ma.setY(y);
		ma.setUIIsEnable(true);
		ma.setLGType(FieldTypeS.Enum);

		ma.setUIContralType(ctrlType);
		ma.setUIBindKey(enumKey);

		if (ma.getUIContralType() == UIContralType.RadioBtn)
		{
			SysEnums ses = new SysEnums(ma.getUIBindKey());
			int idx = 0;
			for (SysEnum item : ses.ToJavaList())
			{
				idx++;
				FrmRB rb = new FrmRB();
				rb.setFK_MapData(ma.getFK_MapData());
				rb.setKeyOfEn(ma.getKeyOfEn());
				rb.setIntKey(item.getIntKey());
				rb.setMyPK(rb.getFK_MapData() + "_" + rb.getKeyOfEn() + "_" + rb.getIntKey());
				rb.RetrieveFromDBSources();

				rb.setEnumKey(ma.getUIBindKey());
				rb.setLab(item.getLab());
				rb.setX(ma.getX());

				//让其变化y值.
				rb.setY(ma.getY() + idx * 30);
				rb.Save();
			}
		}
		//frmID设置字段所属的分组
		GroupField groupField = new GroupField();
		groupField.Retrieve(GroupFieldAttr.FrmID, fk_mapdata, GroupFieldAttr.CtrlType, "");
		ma.setGroupID(groupField.getOID());

		ma.Save();
	}

	public static void NewImage(String frmID, String keyOfEn, String name, float x, float y) throws Exception {

		FrmImg img = new FrmImg();
		img.setMyPK(keyOfEn);
		img.setFK_MapData(frmID);
		img.setName(name);
		img.setIsEdit(1);
		img.setHisImgAppType(ImgAppType.Img);
		img.setX(x);
		img.setY(y);

		img.Insert();

	}


	public static void NewField(String frmID, String field, String fieldDesc, int mydataType, float x, float y) throws Exception {
		NewField(frmID, field, fieldDesc, mydataType, x, y, 1);
	}


	public static void NewField(String frmID, String field, String fieldDesc, int mydataType, float x, float y, int colSpan) throws Exception {
		//检查是否可以创建字段? 
		MapData md = new MapData(frmID);
		md.CheckPTableSaveModel(field);

		MapAttr ma = new MapAttr();
		ma.setFK_MapData(frmID);
		ma.setKeyOfEn(field);
		ma.setName(fieldDesc);
		ma.setMyDataType(mydataType);
		if (ma.getMyDataType() == 7)
		{
			ma.setIsSupperText(1);
		}
		ma.setX(x);
		ma.setY(y);

		//frmID设置字段所属的分组
		GroupField groupField = new GroupField();
		groupField.Retrieve(GroupFieldAttr.FrmID, frmID, GroupFieldAttr.CtrlType, "");
		ma.setGroupID(groupField.getOID());

		ma.Insert();
	}

	public static void NewEnumField(String fk_mapdata, String field, String fieldDesc, String enumKey, UIContralType ctrlType, float x, float y) throws Exception {
		NewEnumField(fk_mapdata, field, fieldDesc, enumKey, ctrlType, x, y, 1);
	}


	public static void NewEnumField(String fk_mapdata, String field, String fieldDesc, String enumKey, UIContralType ctrlType, float x, float y, int colSpan) throws Exception {
		//检查是否可以创建字段? 
		MapData md = new MapData(fk_mapdata);
		md.CheckPTableSaveModel(field);

		MapAttr ma = new MapAttr();
		ma.setFK_MapData(fk_mapdata);
		ma.setKeyOfEn(field);
		ma.setName(fieldDesc);
		ma.setMyDataType(DataType.AppInt);
		ma.setX(x);
		ma.setY(y);
		ma.setUIIsEnable(true);
		ma.setLGType(FieldTypeS.Enum);
		ma.setUIContralType(ctrlType);
		ma.setUIBindKey(enumKey);

		//frmID设置字段所属的分组
		GroupField groupField = new GroupField();
		groupField.Retrieve(GroupFieldAttr.FrmID, fk_mapdata, GroupFieldAttr.CtrlType, "");
		ma.setGroupID(groupField.getOID());
		ma.Insert();

		if (ma.getUIContralType() != UIContralType.RadioBtn)
		{
			return;
		}

		//删除可能存在的数据.
		DBAccess.RunSQL("DELETE FROM Sys_FrmRB WHERE KeyOfEn='" + ma.getKeyOfEn() + "' AND FK_MapData='" + ma.getFK_MapData() + "'");

		SysEnums ses = new SysEnums(ma.getUIBindKey());
		int idx = 0;
		for (SysEnum item : ses.ToJavaList())
		{
			idx++;
			FrmRB rb = new FrmRB();
			rb.setFK_MapData(ma.getFK_MapData());
			rb.setKeyOfEn(ma.getKeyOfEn());
			rb.setEnumKey(ma.getUIBindKey());

			rb.setLab(item.getLab());
			rb.setIntKey(item.getIntKey());
			rb.setX(ma.getX());

			//让其变化y值.
			rb.setY(ma.getY() + idx * 30);
			rb.Insert();
		}
	}
	/** 
	 创建字段分组
	 
	 param frmID
	 param gKey
	 param gName
	 @return 
	*/
	public static String NewCheckGroup(String frmID, String gKey, String gName) throws Exception {
		//string gKey = v1;
		//string gName = v2;
		//string enName1 = v3;

		MapAttr attrN = new MapAttr();
		int i = attrN.Retrieve(MapAttrAttr.FK_MapData, frmID, MapAttrAttr.KeyOfEn, gKey + "_Note");
		i += attrN.Retrieve(MapAttrAttr.FK_MapData, frmID, MapAttrAttr.KeyOfEn, gKey + "_Checker");
		i += attrN.Retrieve(MapAttrAttr.FK_MapData, frmID, MapAttrAttr.KeyOfEn, gKey + "_RDT");
		if (i > 0)
		{
			return "err@前缀已经使用：" + gKey + " ， 请确认您是否增加了这个审核分组或者，请您更换其他的前缀。";
		}

		GroupField gf = new GroupField();
		gf.setLab(gName);
		gf.setFrmID(frmID);
		gf.Insert();

		attrN = new MapAttr();
		attrN.setFK_MapData(frmID);
		attrN.setKeyOfEn(gKey + "_Note");
		attrN.setName("审核意见");
		attrN.setMyDataType(DataType.AppString);
		attrN.setUIContralType(UIContralType.TB);
		attrN.setUIIsEnable(true);
		attrN.setUIIsLine(true);
		attrN.setMaxLen(4000);
		attrN.setGroupID(gf.getOID());
		attrN.setUIHeight(23 * 3);
		attrN.setIdx(1);
		attrN.Insert();

		attrN = new MapAttr();
		attrN.setFK_MapData(frmID);
		attrN.setKeyOfEn(gKey + "_Checker");
		attrN.setName("审核人"); // "审核人";
		attrN.setMyDataType(DataType.AppString);
		attrN.setUIContralType(UIContralType.TB);
		attrN.setMaxLen(50);
		attrN.setMinLen(0);
		attrN.setUIIsEnable(true);
		attrN.setUIIsLine(false);
		attrN.setDefVal("@WebUser.Name");
		attrN.setUIIsEnable(false);
		attrN.setGroupID(gf.getOID());
		attrN.setIsSigan(true);
		attrN.setIdx(2);
		attrN.Insert();

		attrN = new MapAttr();
		attrN.setFK_MapData(frmID);
		attrN.setKeyOfEn(gKey + "_RDT");
		attrN.setName("审核日期"); // "审核日期";
		attrN.setMyDataType(DataType.AppDateTime);
		attrN.setUIContralType(UIContralType.TB);
		attrN.setUIIsEnable(true);
		attrN.setUIIsLine(false);
		attrN.setDefVal("@RDT");
		attrN.setUIIsEnable(false);
		attrN.setGroupID(gf.getOID());
		attrN.setIdx(3);
		attrN.Insert();

		/*
		 * 判断是否是节点设置的审核分组，如果是就为节点设置焦点字段。
		 */
		frmID = frmID.replace("ND", "");
		int nodeid = 0;
		try
		{
			nodeid = Integer.parseInt(frmID);
		}
		catch (java.lang.Exception e)
		{
			//转化不成功就是不是节点表单字段.
			return "error:只能节点表单才可以使用审核分组组件。";
		}
		return null;

		/*
		Node nd = new Node();
		nd.NodeID = nodeid;
		if (nd.RetrieveFromDBSources() != 0 && DataType.IsNullOrEmpty(nd.FocusField) == true)
		{
		    nd.FocusField = "@" + gKey + "_Note";
		    nd.Update();
		}
		 * */
	}

		///#endregion 创建修改字段.


		///#region 模版操作.
	/** 
	 创建一个审核分组
	 
	 param frmID 表单ID
	 param groupName 分组名称
	 param prx 前缀
	*/
	public static void CreateCheckGroup(String frmID, String groupName, String prx) throws Exception {
		GroupField gf = new GroupField();
		gf.setLab(groupName);
		gf.setFrmID(frmID);
		int i = gf.Retrieve(GroupFieldAttr.Lab, groupName, GroupFieldAttr.FrmID, frmID);
		if (i == 0)
		{
			gf.Insert();
		}

		MapAttr attr = new MapAttr();
		attr.setFK_MapData(frmID);
		attr.setKeyOfEn(prx + "_Note");
		attr.setName("审核意见"); // sta;  // this.ToE("CheckNote", "审核意见");
		attr.setMyDataType(DataType.AppString);
		attr.setUIContralType(UIContralType.TB);
		attr.setUIIsEnable(true);
		attr.setUIIsLine(true);
		attr.setMaxLen(4000);
		attr.SetValByKey(MapAttrAttr.ColSpan, 4);
		// attr.getColSpan() = 4;
		attr.setGroupID(gf.getOID());
		attr.setUIHeight(23 * 3);
		attr.setIdx(1);
		attr.Insert();
		attr.Update("Idx", 1);


		attr = new MapAttr();
		attr.setFK_MapData(frmID);
		attr.setKeyOfEn(prx + "_Checker");
		attr.setName("审核人"); // "审核人";
		attr.setMyDataType(DataType.AppString);
		attr.setUIContralType(UIContralType.TB);
		attr.setMaxLen(50);
		attr.setMinLen(0);
		attr.setUIIsEnable(true);
		attr.setUIIsLine(false);
		attr.setDefVal("@WebUser.No");
		attr.setUIIsEnable(false);
		attr.setGroupID(gf.getOID());
		attr.setIsSigan(true);
		attr.setIdx(2);
		attr.Insert();
		attr.Update("Idx", 2);

		attr = new MapAttr();
		attr.setFK_MapData(frmID);
		attr.setKeyOfEn(prx + "_RDT");
		attr.setName("审核日期"); // "审核日期";
		attr.setMyDataType(DataType.AppDateTime);
		attr.setUIContralType(UIContralType.TB);
		attr.setUIIsEnable(true);
		attr.setUIIsLine(false);
		attr.setDefVal("@RDT");
		attr.setUIIsEnable(false);
		attr.setGroupID(gf.getOID());
		attr.setIdx(3);
		attr.Insert();
		attr.Update("Idx", 3);
	}
	/** 
	 创建表单
	 
	 param frmID 表单ID
	 param frmName 表单名称
	 param frmTreeID 表单类别编号（表单树ID）
	*/

	public static void CreateFrm(String frmID, String frmName, String frmTreeID) throws Exception {
		CreateFrm(frmID, frmName, frmTreeID, FrmType.FoolForm);
	}


	public static void CreateFrm(String frmID, String frmName, String frmTreeID, FrmType frmType) throws Exception {
		MapData md = new MapData();
		md.setNo(frmID);
		if (md.getIsExits() == true)
		{
			throw new RuntimeException("@表单ID为:" + frmID + " 已经存在.");
		}

		md.setName(frmName);
		md.setHisFrmType(frmType);
		md.Insert();
	}
	/** 
	 一键设置元素只读
	 
	 param frmID 要设置的表单.
	*/
	public static void OneKeySetFrmEleReadonly(String frmID) throws Exception {
		String sql = "UPDATE Sys_MapAttr SET UIIsEnable=0 WHERE FK_MapData='" + frmID + "'";
		DBAccess.RunSQL(sql);

		MapDtls dtls = new MapDtls(frmID);
		for (MapDtl dtl : dtls.ToJavaList())
		{
			dtl.setIsInsert(false);
			dtl.setIsUpdate(false);
			dtl.setIsDelete(false);
			dtl.Update();


		}

		FrmAttachments ens = new FrmAttachments(frmID);
		for (FrmAttachment en : ens.ToJavaList())
		{
			en.setIsUpload(false);
			en.setDeleteWay(0);
			en.Update();


		}

	}
	/** 
	 修复表单.
	 
	 param frmID
	*/
	public static void RepareCCForm(String frmID) throws Exception {
		MapAttr attr = new MapAttr();
		if (attr.IsExit(MapAttrAttr.KeyOfEn, "OID", MapAttrAttr.FK_MapData, frmID) == false)
		{
			attr.setFK_MapData(frmID);
			attr.setKeyOfEn("OID");
			attr.setName("主键");
			attr.setMyDataType(DataType.AppInt);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setDefVal("0");
			attr.setHisEditType(EditType.Readonly);
			attr.Insert();
		}

	}


	/** 
	 复制表单
	 
	 param copyToFrmID 源表单ID

	 param copyFrmName 新实体表单名称
	*/
	public static String CopyFrm(String srcFrmID, String copyToFrmID, String copyFrmName, String fk_frmTree) throws Exception {
		MapData mymd = new MapData();
		mymd.setNo(copyToFrmID);
		if (mymd.RetrieveFromDBSources() == 1)
		{
			throw new RuntimeException("@目标表单ID:" + copyToFrmID + "已经存在，位于:" + mymd.getFK_FormTreeText() + "目录下.");
		}

		//获得源文件信息.
		DataSet ds = GenerHisDataSet(srcFrmID);

		//导入表单文件.
		ImpFrmTemplate(copyToFrmID, ds, false);

		//复制模版文件.
		MapData mdCopyTo = new MapData(copyToFrmID);

		if (mdCopyTo.getHisFrmType() == FrmType.ExcelFrm)
		{
			/*如果是excel表单，那就需要复制excel文件.*/
			String srcFile = SystemConfig.getPathOfDataUser() + "FrmOfficeTemplate/" + srcFrmID + ".xls";
			String toFile = SystemConfig.getPathOfDataUser() + "FrmOfficeTemplate/" + copyToFrmID + ".xls";
			if ((new File(srcFile)).isFile() == true)
			{
				if ((new File(toFile)).isFile() == false)
				{
					Files.copy(Paths.get(srcFile), Paths.get(toFile), StandardCopyOption.COPY_ATTRIBUTES);
				}
			}

			srcFile = SystemConfig.getPathOfDataUser() + "FrmOfficeTemplate/" + srcFrmID + ".xlsx";
			toFile = SystemConfig.getPathOfDataUser() + "FrmOfficeTemplate/" + copyToFrmID + ".xlsx";
			if ((new File(srcFile)).isFile() == true)
			{
				if ((new File(toFile)).isFile() == false)
				{
					Files.copy(Paths.get(srcFile), Paths.get(toFile), StandardCopyOption.COPY_ATTRIBUTES);
				}
			}
		}

		mdCopyTo.Retrieve();

		mdCopyTo.setFK_FormTree(fk_frmTree);
		//  md.FK_FrmSort = fk_frmTree;
		mdCopyTo.setName(copyFrmName);
		mdCopyTo.Update();

		return "表单复制成功,您需要重新登录，或者刷新才能看到。";
	}
	/** 
	 导入表单API
	 
	 param toFrmID 要导入的表单ID
	 param fromds 数据源
	 param isSetReadonly 是否把空间设置只读？
	 * @throws Exception 
	*/
	public static void ImpFrmTemplate(String toFrmID, DataSet fromds, boolean isSetReadonly) throws Exception
	{
		MapData.ImpMapData(toFrmID, fromds);
	}
	/** 
	 修改frm的事件
	 
	 param frmID
	*/
	public static void AfterFrmEditAction(String frmID) throws Exception {
		//清除缓存.
		CashFrmTemplate.Remove(frmID);
		Cash.SetMap(frmID, null);

		MapData mapdata = new MapData();
		mapdata.setNo(frmID);
		mapdata.RetrieveFromDBSources();
		Cash2019.UpdateRow(mapdata.toString(), frmID, mapdata.getRow());
		mapdata.CleanObject();
		return;
	}
	/** 
	 获得表单信息.
	 
	 param frmID 表单
	 @return 
	*/

	public static bp.da.DataSet GenerHisDataSet(String frmID, String frmName) throws Exception {
		return GenerHisDataSet(frmID, frmName, null);
	}

	public static bp.da.DataSet GenerHisDataSet(String frmID) throws Exception {
		return GenerHisDataSet(frmID, null, null);
	}


	public static bp.da.DataSet GenerHisDataSet(String frmID, String frmName, MapData md) throws Exception {
		//首先从缓存获取数据.
		DataSet dsFrm = CashFrmTemplate.GetFrmDataSetModel(frmID);
		if (dsFrm != null)
		{
			return dsFrm;
		}

		DataSet ds = new DataSet();

		//创建实体对象.
		if (md == null)
		{
			md = new MapData(frmID);
		}

		if (DataType.IsNullOrEmpty(md.getName()) == true && frmName != null)
		{
			md.setName(frmName);
		}

		//加入主表信息.
		DataTable Sys_MapData = md.ToDataTableField("Sys_MapData");
		ds.Tables.add(Sys_MapData);

		//加入分组表.
		DataTable Sys_GroupField = md.getGroupFields().ToDataTableField("Sys_GroupField");
		ds.Tables.add(Sys_GroupField);

		//加入明细表.
		DataTable Sys_MapDtl = md.getOrigMapDtls().ToDataTableField("Sys_MapDtl");
		ds.Tables.add(Sys_MapDtl);

		//加入枚举表.
		DataTable Sys_Menu = md.getSysEnums().ToDataTableField("Sys_Enum");
		ds.Tables.add(Sys_Menu);

		//加入外键属性.
		DataTable Sys_MapAttr = md.getMapAttrs().ToDataTableField("Sys_MapAttr");
		ds.Tables.add(Sys_MapAttr);

		//加入扩展属性.
		DataTable Sys_MapExt = md.getMapExts().ToDataTableField("Sys_MapExt");
		ds.Tables.add(Sys_MapExt);

		//Sys_FrmRB.
		DataTable Sys_FrmRB = md.getFrmRBs().ToDataTableField("Sys_FrmRB");
		ds.Tables.add(Sys_FrmRB);


		//img.
		DataTable Sys_FrmImg = md.getFrmImgs().ToDataTableField("Sys_FrmImg");
		ds.Tables.add(Sys_FrmImg);

		//Sys_MapFrame.
		DataTable Sys_MapFrame = md.getMapFrames().ToDataTableField("Sys_MapFrame");
		ds.Tables.add(Sys_MapFrame);

		//Sys_FrmAttachment.
		DataTable Sys_FrmAttachment = md.getFrmAttachments().ToDataTableField("Sys_FrmAttachment");
		ds.Tables.add(Sys_FrmAttachment);

		//FrmImgAths. 上传图片附件.
		DataTable Sys_FrmImgAth = md.getFrmImgAths().ToDataTableField("Sys_FrmImgAth");
		ds.Tables.add(Sys_FrmImgAth);

		//放入缓存.
		CashFrmTemplate.Put(frmID, ds);

		return ds;
	}
	/** 
	 获得表单字段信息字段.
	 
	 param fk_mapdata
	 @return 
	*/
	public static bp.da.DataSet GenerHisDataSet_AllEleInfo(String fk_mapdata) throws Exception {
		MapData md = new MapData(fk_mapdata);

		//求出 frmIDs 
		String frmIDs = "'" + fk_mapdata + "'";
		MapDtls mdtls = new MapDtls(md.getNo());
		for (MapDtl item : mdtls.ToJavaList())
		{
			frmIDs += ",'" + item.getNo() + "'";
		}

		DataSet ds = new DataSet();

		//加入主表信息.
		DataTable Sys_MapData = md.ToDataTableField("Sys_MapData");

		//如果是开发者表单, 就把html信息放入到字段.
		if (md.getHisFrmType() == FrmType.Develop)
		{
			Sys_MapData.Columns.Add("HtmlTemplateFile", String.class);
			String text = DBAccess.GetBigTextFromDB("Sys_MapData", "No", md.getNo(), "HtmlTemplateFile");
			Sys_MapData.Rows.get(0).setValue("HtmlTemplateFile",text) ;
		}


		ds.Tables.add(Sys_MapData);

		//加入分组表.
		GroupFields gfs = new GroupFields();
		gfs.RetrieveIn(GroupFieldAttr.FrmID, frmIDs);
		DataTable Sys_GroupField = gfs.ToDataTableField("Sys_GroupField");
		ds.Tables.add(Sys_GroupField);

		//加入明细表.
		DataTable Sys_MapDtl = md.getOrigMapDtls().ToDataTableField("Sys_MapDtl");
		ds.Tables.add(Sys_MapDtl);

		//加入枚举表.
		SysEnums ses = new SysEnums();
		ses.RetrieveInSQL(SysEnumAttr.EnumKey, "SELECT UIBindKey FROM Sys_MapAttr WHERE FK_MapData IN (" + frmIDs + ") ");
		DataTable Sys_Menu = ses.ToDataTableField("Sys_Enum");
		ds.Tables.add(Sys_Menu);

		//加入字段属性.
		MapAttrs attrs = new MapAttrs();
		attrs.RetrieveIn(MapAttrAttr.FK_MapData, frmIDs);
		DataTable Sys_MapAttr = attrs.ToDataTableField("Sys_MapAttr");
		ds.Tables.add(Sys_MapAttr);

		//加入扩展属性.
		MapExts exts = new MapExts();
		exts.RetrieveIn(MapAttrAttr.FK_MapData, frmIDs);
		DataTable Sys_MapExt = exts.ToDataTableField("Sys_MapExt");
		ds.Tables.add(Sys_MapExt);

		//img.
		//Sys_FrmLab.
		FrmImgs frmImgs = new FrmImgs();
		frmImgs.RetrieveIn(MapAttrAttr.FK_MapData, frmIDs);
		ds.Tables.add(frmImgs.ToDataTableField("Sys_FrmImg"));

		//Sys_FrmRB.
		DataTable Sys_FrmRB = md.getFrmRBs().ToDataTableField("Sys_FrmRB");
		ds.Tables.add(Sys_FrmRB);


		//Sys_MapFrame.
		DataTable Sys_MapFrame = md.getMapFrames().ToDataTableField("Sys_MapFrame");
		ds.Tables.add(Sys_MapFrame);

		//Sys_FrmAttachment.
		DataTable Sys_FrmAttachment = md.getFrmAttachments().ToDataTableField("Sys_FrmAttachment");
		ds.Tables.add(Sys_FrmAttachment);

		//FrmImgAths. 上传图片附件.
		DataTable Sys_FrmImgAth = md.getFrmImgAths().ToDataTableField("Sys_FrmImgAth");
		ds.Tables.add(Sys_FrmImgAth);

		return ds;
	}
	/** 
	 获得表单模版dataSet格式.
	 
	 param fk_mapdata 表单ID
	 @return DataSet
	*/

	public static bp.da.DataSet GenerHisDataSet_AllEleInfo2017(String fk_mapdata) throws Exception {
		return GenerHisDataSet_AllEleInfo2017(fk_mapdata, false);
	}


	public static bp.da.DataSet GenerHisDataSet_AllEleInfo2017(String fk_mapdata, boolean isCheckFrmType) throws Exception {
		MapData md = new MapData(fk_mapdata);

		//从表.
		String sql = "SELECT * FROM Sys_MapDtl WHERE FK_MapData ='%1$s'";
		sql = String.format(sql, fk_mapdata);
		DataTable dtMapDtl = DBAccess.RunSQLReturnTable(sql);
		dtMapDtl.setTableName("Sys_MapDtl");

		String ids = String.format("'%1$s'", fk_mapdata);
		for (DataRow dr : dtMapDtl.Rows)
		{
			ids += ",'" + dr.getValue("No") + "'";
		}
		String sqls = "";
		ArrayList<String> listNames = new ArrayList<String>();

		// Sys_GroupField.
		listNames.add("Sys_GroupField");
		sql = "SELECT * FROM Sys_GroupField WHERE  FrmID IN (" + ids + ")";
		sqls += sql;

		// Sys_Enum
		listNames.add("Sys_Enum");
		sql = "@SELECT * FROM "+bp.sys.base.Glo.SysEnum()+" WHERE EnumKey IN ( SELECT UIBindKey FROM Sys_MapAttr WHERE FK_MapData IN (" + ids + ") ) order By EnumKey,IntKey";
		sqls += sql;

		// 审核组件
		String nodeIDstr = fk_mapdata.replace("ND", "");
		if (DataType.IsNumStr(nodeIDstr))
		{
			// 审核组件状态:0 禁用;1 启用;2 只读;
			listNames.add("WF_Node");
			sql = "@SELECT * FROM WF_Node WHERE NodeID=" + nodeIDstr + " AND  ( FWCSta >0  OR SFSta >0 )";
			sqls += sql;
		}

		String where = " FK_MapData IN (" + ids + ")";

		// Sys_MapData.
		listNames.add("Sys_MapData");
		sql = "@SELECT * FROM Sys_MapData WHERE No='" + fk_mapdata + "'";
		sqls += sql;

		// Sys_MapAttr.
		listNames.add("Sys_MapAttr");

		sql = "@SELECT * FROM Sys_MapAttr WHERE " + where + " AND KeyOfEn NOT IN('WFState') ORDER BY FK_MapData, IDX  ";
		sqls += sql;


		// Sys_MapExt.
		listNames.add("Sys_MapExt");
		sql = "@SELECT * FROM Sys_MapExt WHERE " + where;
		sqls += sql;

		//if (isCheckFrmType == true && md.getHisFrmType() == FrmType.FreeFrm)
		//{
		// line.
		listNames.add("Sys_FrmLine");
		sql = "@SELECT * FROM Sys_FrmLine WHERE " + where;
		sqls += sql;

		// link.
		listNames.add("Sys_FrmLink");
		sql = "@SELECT * FROM Sys_FrmLink WHERE " + where;
		sqls += sql;

		// btn.
		listNames.add("Sys_FrmBtn");
		sql = "@SELECT * FROM Sys_FrmBtn WHERE " + where;
		sqls += sql;

		// Sys_FrmImg.
		listNames.add("Sys_FrmImg");
		sql = "@SELECT * FROM Sys_FrmImg WHERE " + where;
		sqls += sql;

		// Sys_FrmLab.
		listNames.add("Sys_FrmLab");
		sql = "@SELECT * FROM Sys_FrmLab WHERE " + where;
		sqls += sql;
		//}

		// Sys_FrmRB.
		listNames.add("Sys_FrmRB");
		sql = "@SELECT * FROM Sys_FrmRB WHERE " + where;
		sqls += sql;

		// ele.
		listNames.add("Sys_FrmEle");
		sql = "@SELECT * FROM Sys_FrmEle WHERE " + where;
		sqls += sql;

		//Sys_MapFrame.
		listNames.add("Sys_MapFrame");
		sql = "@SELECT * FROM Sys_MapFrame WHERE " + where;
		sqls += sql;

		// Sys_FrmAttachment. 
		listNames.add("Sys_FrmAttachment");
		sql = "@SELECT * " + " FROM Sys_FrmAttachment WHERE " + where + "";

		sqls += sql;

		// Sys_FrmImgAth.
		listNames.add("Sys_FrmImgAth");

		sql = "@SELECT * FROM Sys_FrmImgAth WHERE " + where;
		sqls += sql;



		String[] strs = sqls.split("[@]", -1);
		DataSet ds = new DataSet();

		if (strs != null && strs.length == listNames.size())
		{
			for (int i = 0; i < listNames.size(); i++)
			{
				String s = strs[i];
				if (DataType.IsNullOrEmpty(s))
				{
					continue;
				}
				DataTable dt = DBAccess.RunSQLReturnTable(s);
				dt.setTableName(listNames.get(i));
				ds.Tables.add(dt);
			}
		}

		for (DataTable item : ds.Tables)
		{
			if (item.TableName.equals("Sys_MapAttr") && item.Rows.size() == 0)
			{
				md.RepairMap();
			}
		}

		ds.Tables.add(dtMapDtl);
		return ds;
	}

		///#endregion 模版操作.


		///#region 模版操作 2020.
	/** 
	 A:从一个表单导入另外一个表单模版:
	 1.向已经存在的表单上导入模版.
	 2.用于节点表单的导入,设计表单的时候，新建一个表单后在导入的情况.
	 
	 param frmID
	 param specImpFrmID
	 @return 
	*/
	public static MapData Template_ImpFromSpecFrmID(String frmID, String specImpFrmID)
	{
		return null;
	}

	/** 
	 B:复制表单模版到指定的表单ID.
	 用于复制一个表单，到另外一个表单ID上去.用于表单树的上的表单Copy.
	 
	 param fromFrmID 要copy的表单ID
	 param copyToFrmID copy到的表单ID
	 param copyToFrmName 表单名称
	 @return 
	*/
	public static MapData Template_CopyFrmToFrmIDAsNewFrm(String fromFrmID, String copyToFrmID, String copyToFrmName)
	{

		return null;
	}
	/** 
	 C:导入模版xml文件..
	 导入一个已经存在的表单,如果这个表单ID已经存在就提示错误..
	 
	 param  ds
	 param  frmSort
	 @return 
	*/
	public static MapData Template_LoadXmlTemplateAsNewFrm(DataSet ds, String frmSort) throws Exception {
		MapData md = MapData.ImpMapData(ds);
		md.setOrgNo(DBAccess.RunSQLReturnString("SELECT OrgNo FROM sys_formtree WHERE NO='" + frmSort + "'"));
		md.setFK_FormTree(frmSort);
		md.Update();
		return md;
	}
	public static MapData Template_LoadXmlTemplateAsSpecFrmID(String newFrmID, DataSet ds, String frmSort) throws Exception {
		MapData md = MapData.ImpMapData(newFrmID, ds);
		md.setOrgNo(DBAccess.RunSQLReturnString("SELECT OrgNo FROM sys_formtree WHERE NO='" + frmSort + "'"));
		md.setFK_FormTree(frmSort);
		md.Update();
		return md;
	}

		///#endregion 模版操作.


		///#region 其他功能.
	/** 
	 保存枚举
	 
	 param enumKey 键值对
	 param enumLab 标签
	 param cfg 配置 @0=xxx@1=yyyy@n=xxxxxc
	 param isNew 语言
	 @return 
	*/

	public static String SaveEnum(String enumKey, String enumLab, String cfg, boolean isNew) throws Exception {
		return SaveEnum(enumKey, enumLab, cfg, isNew, "CH");
	}


	public static String SaveEnum(String enumKey, String enumLab, String cfg, boolean isNew, String lang) throws Exception {
		SysEnumMain sem = new SysEnumMain();
		sem.setNo(enumKey);
		int dataCount = sem.RetrieveFromDBSources();
		if (dataCount > 0 && isNew)
		{
			return "err@已存在枚举" + enumKey + ",请修改枚举名字";
		}

		if (dataCount == 0)
		{
			sem.setName(enumLab);
			sem.setCfgVal(cfg);
			sem.setLang(lang);
			sem.DirectInsert();
		}
		else
		{
			sem.setName(enumLab);
			sem.setCfgVal(cfg);
			sem.setLang(lang);
			sem.Update();
		}

		String[] strs = cfg.split("[@]", -1);
		for (String str : strs)
		{
			if (DataType.IsNullOrEmpty(str))
			{
				continue;
			}
			String[] kvs = str.split("[=]", -1);
			SysEnum se = new SysEnum();
			se.setEnumKey(enumKey);
			se.setLang(lang);
			se.setIntKey(Integer.parseInt(kvs[0]));
			//杨玉慧
			//解决当  枚举值含有 '='号时，保存不进去的方法
			String[] kvsValues = new String[kvs.length - 1];
			for (int i = 0; i < kvsValues.length; i++)
			{
				kvsValues[i] = kvs[i + 1];
			}
			se.setLab(StringHelper.join("=", kvsValues));
			se.setMyPK(se.getEnumKey() + "_" + se.getLang() + "_" + se.getIntKey());
			se.Save();
		}
		return "保存成功.";
	}
	/** 
	 转拼音方法
	 
	 param name 字段中文名称
	 param isQuanPin 是否转换全拼
	 @return 转化后的拼音，不成功则抛出异常.
	*/
	public static String ParseStringToPinyinField(String name, boolean isQuanPin)
	{
		if (DataType.IsNullOrEmpty(name) == true)
		{
			return "";
		}

		String s = "";
		try
		{
			if (isQuanPin == true)
			{
				s = DataType.ParseStringToPinyin(name);
				if (s.length() > 15)
				{
					s = DataType.ParseStringToPinyinJianXie(name);
				}
			}
			else
			{
				s = DataType.ParseStringToPinyinJianXie(name);
			}

			s = s.trim().replace(" ", "");
			s = s.trim().replace(" ", "");
			//常见符号
			s = s.replace(",", "").replace(".", "").replace("，", "").replace("。", "").replace("!", "");
			s = s.replace("*", "").replace("@", "").replace("#", "").replace("~", "").replace("|", "");
			s = s.replace("$", "").replace("%", "").replace("&", "").replace("（", "").replace("）", "").replace("【", "").replace("】", "");
			s = s.replace("(", "").replace(")", "").replace("[", "").replace("]", "").replace("{", "").replace("}", "").replace("/", "");
			if (s.length() > 0)
			{
				//去除开头数字
				String headStr = s.substring(0, 1);
				if (DataType.IsNumStr(headStr) == true)
				{
					s = "F" + s;
				}
			}
			//去掉空格，去掉点.
			s = s.replace(" ", "");
			s = s.replace(".", "");
			return s;
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException(ex.getMessage());
		}
	}
	/** 
	 转拼音全拼/简写方法(若转换后以数字开头，则前面加F)
	 <p>added by liuxc,2017-9-25</p>
	 
	 param name 中文字符串
	 param isQuanPin 是否转换全拼
	 param removeSpecialSymbols 是否去除特殊符号，仅保留汉字、数字、字母、下划线
	 param maxLen 转化后字符串最大长度，0为不限制
	 @return 转化后的拼音，不成功则抛出异常.
	*/
	public static String ParseStringToPinyinField(String name, boolean isQuanPin, boolean removeSpecialSymbols, int maxLen)
	{
		if (DataType.IsNullOrEmpty(name) == true)
		{
			return "";
		}

		String s = "";

		if (removeSpecialSymbols)
		{
			name = DataType.ParseStringForName(name, maxLen);
		}

		//单.
		name = name.replace("单", "Dan");

		try
		{
			if (isQuanPin == true)
			{
				s = DataType.ParseStringToPinyin(name);
			}
			else
			{
				s = DataType.ParseStringToPinyinJianXie(name);
			}

			//如果全拼长度超过maxLen，则取前maxLen长度的字符
			if (maxLen > 0 && s.length() > maxLen)
			{
				s = s.substring(0, maxLen);
			}

			if (s.length() > 0)
			{
				//去除开头数字
				String headStr = s.substring(0, 1);
				if (DataType.IsNumStr(headStr) == true)
				{
					s = "F" + (s.length() > maxLen - 1 ? s.substring(0, maxLen - 1) : s);
				}
			}

			return s;
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException(ex.getMessage());
		}
	}
	/** 
	 多音字转拼音
	 
	 param charT 单个汉字
	 @return 包含返回拼音，否则返回null
	*/
	public static String ChinaMulTonesToPinYin(String charT) throws Exception {
		try
		{
			ChMulToneXmls mulChs = new ChMulToneXmls();
			mulChs.RetrieveAll();
			for (ChMulToneXml en : mulChs.ToJavaList())
			{
				if (en.getNo().equals(charT))
				{
					return en.getName();
				}
			}
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException(ex.getMessage());
		}
		return null;
	}

	/** 
	 获得外键表
	 
	 param pageNumber 第几页
	 param pageSize 每页大小
	 @return json
	*/
	public static String DB_SFTableList(int pageNumber, int pageSize) throws Exception {
		//获得查询.
		SFTables sftables = new SFTables();
		QueryObject obj = new QueryObject(sftables);
		int RowCount = obj.GetCount();

		//查询
		obj.DoQuery(SysEnumMainAttr.No, pageSize, pageNumber);

		//转化成json.
		return bp.tools.Json.ToJson(sftables.ToDataTableField());
		// return BP.tools.Entitis2Json.ConvertEntitis2GridJsonOnlyData(sftables, RowCount);
	}
	/** 
	 获得隐藏字段集合.
	 
	 param fk_mapdata
	 @return 
	*/
	public static String DB_Hiddenfielddata(String fk_mapdata) throws Exception {
		MapAttrs mapAttrs = new MapAttrs();
		QueryObject obj = new QueryObject(mapAttrs);
		obj.AddWhere(MapAttrAttr.FK_MapData, fk_mapdata);
		obj.addAnd();
		obj.AddWhere(MapAttrAttr.UIVisible, "0");
		obj.addAnd();
		obj.AddWhere(MapAttrAttr.EditType, "0");
		//查询
		obj.DoQuery();

		return mapAttrs.ToJson();
	}

		///#endregion 其他功能.
		private static DataTable GetDtlInfo(MapDtl dtl, GEDtls dtls, GEEntity en,String dtlRefPKVal) throws Exception
		{
			QueryObject qo = null;
			try
			{
				qo = new QueryObject(dtls);
				switch (dtl.getDtlOpenType())
				{
					case ForEmp: // 按人员来控制.
						qo.AddWhere(GEDtlAttr.RefPK, dtlRefPKVal);
						qo.addAnd();
						qo.AddWhere(GEDtlAttr.Rec, WebUser.getNo());
						break;
					case ForWorkID: // 按工作ID来控制
						//qo.addLeftBracket();
						qo.AddWhere(GEDtlAttr.RefPK, dtlRefPKVal);
					/*qo.addOr();
					qo.AddWhere(GEDtlAttr.FID, pkval);
					qo.addRightBracket();*/

						break;
					case ForFID: // 按工作ID来控制
						qo.AddWhere(GEDtlAttr.FID, dtlRefPKVal);
						break;
					default:
						qo.AddWhere(GEDtlAttr.RefPK, dtlRefPKVal);
						break;
				}
				//条件过滤.
				if ( DataType.IsNullOrEmpty( dtl.getFilterSQLExp()) ==false )
				{
					String exp=dtl.getFilterSQLExp();
					exp=bp.wf.Glo.DealExp(exp, en);
					//exp=exp.replace(oldChar, newChar)

					exp = exp.replace("''", "'");

					if (exp.substring(0, 5).toLowerCase().contains("and") == false)
						exp = " AND " + exp;
					qo.setSQL(exp);
				/*if (exp.contains("!="))
				{
					exp=exp.replace("!=", "=");

					String[] strs = exp.split("[=]", -1);

					if (strs.length >= 2)
					{
						qo.addAnd();
						qo.AddWhere(strs[0].trim(), "!=" , strs[1].trim());
					}
				}else
				{

					String[] strs = exp.split("[=]", -1);
					if (strs.length == 2)
					{
						qo.addAnd();
						qo.AddWhere(strs[0].trim(), strs[1].trim());
					}
				}*/
				}

				//增加排序.
				//排序.
				if (DataType.IsNullOrEmpty(dtl.getOrderBySQLExp()) == false)
				{
					qo.addOrderBy(dtl.getOrderBySQLExp());
				}
				else
				{
					//增加排序.
					qo.addOrderBy(GEDtlAttr.OID);
				}
				qo.DoQuery();
				return dtls.ToDataTableField();
			}
			catch (RuntimeException ex)
			{
				dtls.getGetNewEntity().CheckPhysicsTable();
				return GetDtlInfo(dtl, dtls, en,dtlRefPKVal);
			}

		}

}