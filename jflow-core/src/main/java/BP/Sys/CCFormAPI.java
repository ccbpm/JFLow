package BP.Sys;

import BP.En.*;
import BP.Tools.StringHelper;
import BP.DA.*;
import BP.Difference.SystemConfig;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.*;
import java.io.*;
import java.nio.file.*;

/**
 * 表单API
 */
public class CCFormAPI {
	/**
	 * 获得附件信息.
	 * 
	 * @param fk_mapdata
	 *            表单ID
	 * @param pk
	 *            主键
	 * @return 附件信息.
	 */
	public static String GetAthInfos(String fk_mapdata, String pk) {
		int num = BP.DA.DBAccess.RunSQL(
				"SELECT COUNT(MYPK) FROM Sys_FrmAttachmentDB WHERE FK_MapData='" + fk_mapdata + "' AND RefPKVal=" + pk);
		return "附件(" + num + ")";
	}

	/// #region 创建修改字段.
	/**
	 * 创建通用组件入口
	 * 
	 * @param fk_mapdata
	 *            表单ID
	 * @param ctrlType
	 *            控件类型
	 * @param no
	 *            编号
	 * @param name
	 *            名称
	 * @param x
	 *            位置x
	 * @param y
	 *            位置y
	 * @throws Exception
	 */
	public static void CreatePublicNoNameCtrl(String fk_mapdata, String ctrlType, String no, String name, float x,
			float y) throws Exception {
		FrmEle fe = null;
		switch (ctrlType) {
		case "Dtl":
			CreateOrSaveDtl(fk_mapdata, no, name, x, y);
			break;
		case "AthMulti":
			CreateOrSaveAthMulti(fk_mapdata, no, name, x, y);
			break;
		case "AthSingle":
			CreateOrSaveAthSingle(fk_mapdata, no, name, x, y);
			break;
		case "AthImg":
			CreateOrSaveAthImg(fk_mapdata, no, name, x, y);
			break;
		case "Fieldset": // 分组.
			fe = new FrmEle();
			fe.setMyPK(fk_mapdata + "_" + no);
			if (fe.RetrieveFromDBSources() != 0) {
				throw new RuntimeException("@创建失败，已经有同名元素[" + no + "]的控件.");
			}
			fe.setFK_MapData(fk_mapdata);
			fe.setEleType("Fieldset");
			fe.setEleName(name);
			fe.setEleID(no);
			fe.setX(x);
			fe.setY(y);
			fe.Insert();
			break;

		case "iFrame": // 框架.
			MapFrame mapFrame = new MapFrame();
			mapFrame.setMyPK(fk_mapdata + "_" + no);
			if (mapFrame.RetrieveFromDBSources() != 0) {
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

		case "HandSiganture": // 签字版
			// 检查是否可以创建字段?
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
			// frmID设置字段所属的分组
			GroupField groupField = new GroupField();
			groupField.Retrieve(GroupFieldAttr.FrmID, fk_mapdata, GroupFieldAttr.CtrlType, "");
			ma.setGroupID(groupField.getOID());
			ma.Insert();
			break;
		default:
			throw new RuntimeException("@没有判断的存储控件:" + ctrlType + ",存储该控件前,需要做判断.");
		}
	}

	/**
	 * 创建/修改-图片附件
	 * 
	 * @param fk_mapdata
	 *            表单ID
	 * @param dtlNo
	 *            明细表编号
	 * @param dtlName
	 *            名称
	 * @param x
	 *            位置x
	 * @param y
	 *            位置y
	 * @throws Exception
	 */
	public static void CreateOrSaveAthImg(String fk_mapdata, String no, String name, float x, float y)
			throws Exception {
		FrmImgAth ath = new FrmImgAth();
		ath.setFK_MapData(fk_mapdata);
		ath.setCtrlID(no);
		ath.setMyPK(fk_mapdata + "_" + no);

		ath.setX(x);
		ath.setY(y);
		ath.Insert();
	}

	/**
	 * 创建/修改-多附件
	 * 
	 * @param fk_mapdata
	 *            表单ID
	 * @param dtlNo
	 *            明细表编号
	 * @param dtlName
	 *            名称
	 * @param x
	 *            位置x
	 * @param y
	 *            位置y
	 * @throws Exception
	 */
	public static void CreateOrSaveAthSingle(String fk_mapdata, String no, String name, float x, float y)
			throws Exception {
		FrmAttachment ath = new FrmAttachment();
		ath.setFK_MapData(fk_mapdata);
		ath.setNoOfObj(no);

		ath.setMyPK(ath.getFK_MapData() + "_" + ath.getNoOfObj());
		ath.RetrieveFromDBSources();
		ath.setUploadType(AttachmentUploadType.Single);
		ath.setName(name);
		ath.setX(x);
		ath.setY(y);
		ath.Save();
	}

	/**
	 * 创建/修改-多附件
	 * 
	 * @param fk_mapdata
	 *            表单ID
	 * @param dtlNo
	 *            明细表编号
	 * @param dtlName
	 *            名称
	 * @param x
	 * @param y
	 * @throws Exception
	 */
	public static void CreateOrSaveAthMulti(String fk_mapdata, String no, String name, float x, float y)
			throws Exception {
		FrmAttachment ath = new FrmAttachment();
		ath.setFK_MapData(fk_mapdata);
		ath.setNoOfObj(no);
		ath.setMyPK(ath.getFK_MapData() + "_" + ath.getNoOfObj());
		int i = ath.RetrieveFromDBSources();

		if (i == 0) {
			if (!SystemConfig.getCustomerNo().equals("Factory5_mobile")) {
				// ath.SaveTo = SystemConfig.PathOfDataUser + "\\UploadFile\\" +
				// fk_mapdata + "\\";
				ath.setSaveTo("/DataUser/UploadFile/" + fk_mapdata + "/");
			}
			if (fk_mapdata.contains("ND") == true) {
				ath.setHisCtrlWay(AthCtrlWay.WorkID);
			}
		}

		ath.setUploadType(AttachmentUploadType.Multi);
		ath.setName(name);
		ath.setX(x);
		ath.setY(y);
		//默认在移动端显示
		ath.SetPara("IsShowMobile", 1);
		ath.Save();
	}

	/**
	 * 创建/修改一个明细表
	 * 
	 * @param fk_mapdata
	 *            表单ID
	 * @param dtlNo
	 *            明细表编号
	 * @param dtlName
	 *            名称
	 * @param x
	 * @param y
	 * @throws Exception
	 */
	public static void CreateOrSaveDtl(String fk_mapdata, String dtlNo, String dtlName, float x, float y)
			throws Exception {
		MapDtl dtl = new MapDtl();
		dtl.setNo(dtlNo);

		if (dtl.RetrieveFromDBSources() == 0) {
			if (dtlName == null) {
				dtlName = dtlNo;
			}

			// 把他的模式复制过来.
			MapData md = new MapData(fk_mapdata);
			dtl.setPTableModel(md.getPTableModel());

			dtl.setW(500);
		}

		dtl.setX(x);
		dtl.setY(y);
		dtl.setName(dtlName);
		dtl.setFK_MapData(fk_mapdata);

		dtl.Save();

		// 初始化他的map.
		dtl.IntMapAttrs();
	}

	/**
	 * 创建一个外部数据字段
	 * 
	 * @param fk_mapdata
	 *            表单ID
	 * @param fieldName
	 *            字段名
	 * @param fieldDesc
	 *            字段中文名
	 * @param fk_SFTable
	 *            外键表
	 * @param x
	 *            位置
	 * @param y
	 *            位置
	 * @param colSpan
	 *            跨的列数
	 * @throws Exception
	 */

	public static void SaveFieldSFTable(String fk_mapdata, String fieldName, String fieldDesc, String fk_SFTable,
			float x, float y) throws Exception {
		SaveFieldSFTable(fk_mapdata, fieldName, fieldDesc, fk_SFTable, x, y, 1);
	}

	// C# TO JAVA CONVERTER NOTE: Java does not support optional parameters.
	// Overloaded method(s) are created above:
	// ORIGINAL LINE: public static void SaveFieldSFTable(string fk_mapdata,
	// string fieldName, string fieldDesc, string fk_SFTable, float x, float y,
	// int colSpan = 1)
	public static void SaveFieldSFTable(String fk_mapdata, String fieldName, String fieldDesc, String fk_SFTable,
			float x, float y, int colSpan) throws Exception {
		// 检查是否可以创建字段?
		MapData md = new MapData();
		md.setNo(fk_mapdata);
		if (md.RetrieveFromDBSources() == 1) {
			md.CheckPTableSaveModel(fieldName);
		}

		// 外键字段表.
		SFTable sf = new SFTable(fk_SFTable);

		if (DataType.IsNullOrEmpty(fieldDesc) == true) {
			fieldDesc = sf.getName();
		}

		MapAttr attr = new MapAttr();
		attr.setMyPK(fk_mapdata + "_" + fieldName);
		attr.RetrieveFromDBSources();

		// 基本属性赋值.
		attr.setFK_MapData(fk_mapdata);
		attr.setKeyOfEn(fieldName);
		attr.setName(fieldDesc);
		attr.setMyDataType(BP.DA.DataType.AppString);

		attr.setUIContralType(BP.En.UIContralType.DDL);
		attr.setUIBindKey(fk_SFTable); // 绑定信息.
		//如果绑定的外键是树形结构的，在AtPara中增加标识
		if (sf.getCodeStruct() == CodeStruct.Tree)
			attr.SetPara("CodeStruct", 1);
		if(DataType.IsNullOrEmpty(sf.getRootVal())==false)
			attr.SetPara("ParentNo", sf.getRootVal());
		attr.setX(x);
		attr.setY(y);

		// 根据外键表的类型不同，设置它的LGType.
		switch (sf.getSrcType()) {
		case CreateTable:
		case TableOrView:
		case BPClass:
			attr.setLGType(FieldTypeS.FK);
			break;
		case SQL: // 是sql模式.
		default:
			attr.setLGType(FieldTypeS.Normal);
			break;
		}

		// frmID设置字段所属的分组
		GroupField groupField = new GroupField();
		groupField.Retrieve(GroupFieldAttr.FrmID, fk_mapdata, GroupFieldAttr.CtrlType, "");
		attr.setGroupID(groupField.getOID());
		attr.Save();

		// 如果是普通的字段, 这个属于外部数据类型,或者webservices类型. sql 语句类型.
		if (attr.getLGType() == FieldTypeS.Normal) {
			MapAttr attrH = new MapAttr();
			attrH.Copy(attr);
			attrH.SetPara("CodeStruct", "");
			attrH.SetPara("ParentNo", "");
			attrH.setKeyOfEn(attr.getKeyOfEn() + "T");
			attrH.setName(attr.getName());
			attrH.setUIContralType(BP.En.UIContralType.TB);
			attrH.setMinLen(0);
			attrH.setMaxLen(500);
			attrH.setMyDataType(BP.DA.DataType.AppString);
			attrH.setUIVisible(false);
			attrH.setUIIsEnable(false);
			attrH.setMyPK(attrH.getFK_MapData() + "_" + attrH.getKeyOfEn());
			attrH.Save();
		}
	}

	/**
	 * 保存枚举字段
	 * 
	 * @param fk_mapdata
	 *            表单ID
	 * @param fieldName
	 *            字段名
	 * @param fieldDesc
	 *            字段描述
	 * @param enumKey
	 *            枚举值
	 * @param ctrlType
	 *            显示的控件类型
	 * @param x
	 *            位置x
	 * @param y
	 *            位置y
	 * @param colSpan
	 *            横跨的行数
	 * @throws Exception
	 */

	public static void SaveFieldEnum(String fk_mapdata, String fieldName, String fieldDesc, String enumKey,
			UIContralType ctrlType, float x, float y) throws Exception {
		SaveFieldEnum(fk_mapdata, fieldName, fieldDesc, enumKey, ctrlType, x, y, 1);
	}

	// C# TO JAVA CONVERTER NOTE: Java does not support optional parameters.
	// Overloaded method(s) are created above:
	// ORIGINAL LINE: public static void SaveFieldEnum(string fk_mapdata, string
	// fieldName, string fieldDesc, string enumKey, UIContralType ctrlType,
	// float x, float y, int colSpan = 1)
	public static void SaveFieldEnum(String fk_mapdata, String fieldName, String fieldDesc, String enumKey,
			UIContralType ctrlType, float x, float y, int colSpan) throws Exception {
		MapAttr ma = new MapAttr();
		ma.setFK_MapData(fk_mapdata);
		ma.setKeyOfEn(fieldName);

		// 赋值主键。
		ma.setMyPK(ma.getFK_MapData() + "_" + ma.getKeyOfEn());

		// 先查询赋值.
		ma.RetrieveFromDBSources();

		ma.setName(fieldDesc);
		ma.setMyDataType(DataType.AppInt);
		ma.setX(x);
		ma.setY(y);
		ma.setUIIsEnable(true);
		ma.setLGType(FieldTypeS.Enum);

		ma.setUIContralType(ctrlType);
		ma.setUIBindKey(enumKey);

		if (ma.getUIContralType() == UIContralType.RadioBtn) {
			SysEnums ses = new SysEnums(ma.getUIBindKey());
			int idx = 0;
			for (SysEnum item : ses.ToJavaList()) {
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

				// 让其变化y值.
				rb.setY(ma.getY() + idx * 30);
				rb.Save();
			}
		}
		// frmID设置字段所属的分组
		GroupField groupField = new GroupField();
		groupField.Retrieve(GroupFieldAttr.FrmID, fk_mapdata, GroupFieldAttr.CtrlType, "");
		ma.setGroupID(groupField.getOID());

		ma.Save();
	}

	public static void NewImage(String frmID, String keyOfEn, String name, float x, float y) throws Exception {
		// BP.Sys.CCFormParse.SaveImage(frmID, control, properties, imgPKs,
		// ctrlID);
		// imgPKs = imgPKs.Replace(ctrlID + "@", "@");

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

	public static void NewField(String frmID, String field, String fieldDesc, int mydataType, float x, float y)
			throws Exception {
		NewField(frmID, field, fieldDesc, mydataType, x, y, 1);
	}

	// C# TO JAVA CONVERTER NOTE: Java does not support optional parameters.
	// Overloaded method(s) are created above:
	// ORIGINAL LINE: public static void NewField(string frmID, string field,
	// string fieldDesc, int mydataType, float x, float y, int colSpan = 1)
	public static void NewField(String frmID, String field, String fieldDesc, int mydataType, float x, float y,
			int colSpan) throws Exception {
		// 检查是否可以创建字段?
		MapData md = new MapData(frmID);
		md.CheckPTableSaveModel(field);

		MapAttr ma = new MapAttr();
		ma.setFK_MapData(frmID);
		ma.setKeyOfEn(field);
		ma.setName(fieldDesc);
		ma.setMyDataType(mydataType);
		if (mydataType==7)
			ma.setIsSupperText(1);
		ma.setX(x);
		ma.setY(y);

		// frmID设置字段所属的分组
		GroupField groupField = new GroupField();
		groupField.Retrieve(GroupFieldAttr.FrmID, frmID, GroupFieldAttr.CtrlType, "");
		ma.setGroupID(groupField.getOID());

		ma.Insert();
	}

	public static void NewEnumField(String fk_mapdata, String field, String fieldDesc, String enumKey,
			UIContralType ctrlType, float x, float y) throws Exception {
		NewEnumField(fk_mapdata, field, fieldDesc, enumKey, ctrlType, x, y, 1);
	}

	// C# TO JAVA CONVERTER NOTE: Java does not support optional parameters.
	// Overloaded method(s) are created above:
	// ORIGINAL LINE: public static void NewEnumField(string fk_mapdata, string
	// field, string fieldDesc, string enumKey, UIContralType ctrlType, float x,
	// float y, int colSpan = 1)
	public static void NewEnumField(String fk_mapdata, String field, String fieldDesc, String enumKey,
			UIContralType ctrlType, float x, float y, int colSpan) throws Exception {
		// 检查是否可以创建字段?
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

		// frmID设置字段所属的分组
		GroupField groupField = new GroupField();
		groupField.Retrieve(GroupFieldAttr.FrmID, fk_mapdata, GroupFieldAttr.CtrlType, "");
		ma.setGroupID(groupField.getOID());

		ma.Insert();

		if (ma.getUIContralType() != UIContralType.RadioBtn) {
			return;
		}

		// 删除可能存在的数据.
		BP.DA.DBAccess.RunSQL("DELETE FROM Sys_FrmRB WHERE KeyOfEn='" + ma.getKeyOfEn() + "' AND FK_MapData='"
				+ ma.getFK_MapData() + "'");

		SysEnums ses = new SysEnums(ma.getUIBindKey());
		int idx = 0;
		for (SysEnum item : ses.ToJavaList()) {
			idx++;
			FrmRB rb = new FrmRB();
			rb.setFK_MapData(ma.getFK_MapData());
			rb.setKeyOfEn(ma.getKeyOfEn());
			rb.setEnumKey(ma.getUIBindKey());

			rb.setLab(item.getLab());
			rb.setIntKey(item.getIntKey());
			rb.setX(ma.getX());

			// 让其变化y值.
			rb.setY(ma.getY() + idx * 30);
			rb.Insert();
		}
	}

	/**
	 * 创建字段分组
	 * 
	 * @param frmID
	 * @param gKey
	 * @param gName
	 * @return
	 * @throws Exception
	 */
	public static String NewCheckGroup(String frmID, String gKey, String gName) throws Exception {
		// string gKey = v1;
		// string gName = v2;
		// string enName1 = v3;

		MapAttr attrN = new MapAttr();
		int i = attrN.Retrieve(MapAttrAttr.FK_MapData, frmID, MapAttrAttr.KeyOfEn, gKey + "_Note");
		i += attrN.Retrieve(MapAttrAttr.FK_MapData, frmID, MapAttrAttr.KeyOfEn, gKey + "_Checker");
		i += attrN.Retrieve(MapAttrAttr.FK_MapData, frmID, MapAttrAttr.KeyOfEn, gKey + "_RDT");
		if (i > 0) {
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
		try {
			nodeid = Integer.parseInt(frmID);
		} catch (java.lang.Exception e) {
			// 转化不成功就是不是节点表单字段.
			return "error:只能节点表单才可以使用审核分组组件。";
		}
		return null;

		/*
		 * Node nd = new Node(); nd.NodeID = nodeid; if
		 * (nd.RetrieveFromDBSources() != 0 &&
		 * DataType.IsNullOrEmpty(nd.FocusField) == true) { nd.FocusField = "@"
		 * + gKey + "_Note"; nd.Update(); }
		 */
	}

	/// #endregion 创建修改字段.

	/// #region 模版操作.
	/**
	 * 创建一个审核分组
	 * 
	 * @param frmID
	 *            表单ID
	 * @param groupName
	 *            分组名称
	 * @param prx
	 *            前缀
	 * @throws Exception
	 */
	public static void CreateCheckGroup(String frmID, String groupName, String prx) throws Exception {
		GroupField gf = new GroupField();
		gf.setLab(groupName);
		gf.setFrmID(frmID);
		int i = gf.Retrieve(GroupFieldAttr.Lab, groupName, GroupFieldAttr.FrmID, frmID);
		if (i == 0) {
			gf.Insert();
		}

		MapAttr attr = new MapAttr();
		attr.setFK_MapData(frmID);
		attr.setKeyOfEn(prx + "_Note");
		attr.setName("审核意见"); // sta; // this.ToE("CheckNote", "审核意见");
		attr.setMyDataType(DataType.AppString);
		attr.setUIContralType(UIContralType.TB);
		attr.setUIIsEnable(true);
		attr.setUIIsLine(true);
		attr.setMaxLen(4000);
		attr.SetValByKey(MapAttrAttr.ColSpan, 4);
		// attr.ColSpan = 4;
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
	 * 创建表单
	 * 
	 * @param frmID
	 *            表单ID
	 * @param frmName
	 *            表单名称
	 * @param frmTreeID
	 *            表单类别编号（表单树ID）
	 * @param frmType
	 *            表单类型
	 * @throws Exception
	 */

	public static void CreateFrm(String frmID, String frmName, String frmTreeID) throws Exception {
		CreateFrm(frmID, frmName, frmTreeID, FrmType.FreeFrm);
	}

	// C# TO JAVA CONVERTER NOTE: Java does not support optional parameters.
	// Overloaded method(s) are created above:
	// ORIGINAL LINE: public static void CreateFrm(string frmID, string frmName,
	// string frmTreeID, FrmType frmType = FrmType.FreeFrm)
	public static void CreateFrm(String frmID, String frmName, String frmTreeID, FrmType frmType) throws Exception {
		MapData md = new MapData();
		md.setNo(frmID);
		if (md.getIsExits() == true) {
			throw new RuntimeException("@表单ID为:" + frmID + " 已经存在.");
		}

		md.setName(frmName);
		md.setHisFrmType(frmType);
		md.Insert();
	}

	/**
	 * 修复表单.
	 * 
	 * @param frmID
	 * @throws Exception
	 */
	public static void RepareCCForm(String frmID) throws Exception {
		MapAttr attr = new MapAttr();
		if (attr.IsExit(MapAttrAttr.KeyOfEn, "OID", MapAttrAttr.FK_MapData, frmID) == false) {
			attr.setFK_MapData(frmID);
			attr.setKeyOfEn("OID");
			attr.setName("主键");
			attr.setMyDataType(BP.DA.DataType.AppInt);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setDefVal("0");
			attr.setHisEditType(BP.En.EditType.Readonly);
			attr.Insert();
		}

	}

	/**
	 * 执行保存
	 * 
	 * @param fk_mapdata
	 * @param jsonStrOfH5Frm
	 * @throws Exception
	 */
	public static void SaveFrm(String fk_mapdata, String jsonStrOfH5Frm) throws Exception {
		JSONObject jd = JSONObject.fromObject(jsonStrOfH5Frm);
		if (jd.isNullObject()) {
			throw new RuntimeException("error:表单格式不正确，保存失败。");
		}

		JSONObject form_MapData = jd.getJSONObject("c");

		// 直接保存表单图信息.
		MapData mapData = new MapData(fk_mapdata);
		mapData.setFrmW(Float.parseFloat(form_MapData.optString("width")));
		mapData.setFrmH(Float.parseFloat(form_MapData.optString("height")));
		mapData.setDesignerTool("Html5");
		mapData.Update();

		// 执行保存.
		SaveFrm(fk_mapdata, jd);

		// 清空缓存.
		BP.DA.CashEntity.getDCash().clear();
	}

	/**
	 * 将表单设计串格式化为Json.
	 * 
	 * @param formData
	 * @return
	 * @throws Exception
	 */
	private static void SaveFrm(String fk_mapdata, JSONObject formData) throws Exception {

		/// #region 求 PKs.
		// 标签.
		String labelPKs = "@";
		FrmLabs labs = new FrmLabs();
		labs.Retrieve(FrmLabAttr.FK_MapData, fk_mapdata);
		for (FrmLab item : labs.ToJavaList()) {
			labelPKs += item.getMyPK() + "@";
		}

		// 超链接.
		String linkPKs = "@";
		FrmLinks links = new FrmLinks();
		links.Retrieve(FrmLabAttr.FK_MapData, fk_mapdata);
		for (FrmLink item : links.ToJavaList()) {
			linkPKs += item.getMyPK() + "@";
		}

		// 按钮.
		String btnsPKs = "@";
		FrmBtns btns = new FrmBtns();
		btns.Retrieve(FrmLabAttr.FK_MapData, fk_mapdata);
		for (FrmBtn item : btns.ToJavaList()) {
			btnsPKs += item.getMyPK() + "@";
		}

		// 图片.
		String imgPKs = "@";
		FrmImgs imgs = new FrmImgs();
		imgs.Retrieve(FrmLabAttr.FK_MapData, fk_mapdata);
		for (FrmImg item : imgs.ToJavaList()) {
			imgPKs += item.getMyPK() + "@";
		}

		// 求已经存在的字段.
		String attrPKs = "@";
		MapAttrs attrs = new MapAttrs();
		attrs.Retrieve(MapDtlAttr.FK_MapData, fk_mapdata);
		for (MapAttr item : attrs.ToJavaList()) {
			if (item.getKeyOfEn().equals("OID")) {
				continue;
			}
			if (item.getUIVisible() == false) {
				continue;
			}

			attrPKs += item.getKeyOfEn() + "@";
		}
		attrPKs += "@";

		// 求明细表.
		String dtlPKs = "@";
		MapDtls dtls = new MapDtls();
		dtls.Retrieve(MapDtlAttr.FK_MapData, fk_mapdata);
		for (MapDtl item : dtls.ToJavaList()) {
			dtlPKs += item.getNo() + "@";
		}
		dtlPKs += "@";

		// 求附件.
		String athMultis = "@";
		FrmAttachments aths = new FrmAttachments();
		aths.Retrieve(MapDtlAttr.FK_MapData, fk_mapdata);
		for (FrmAttachment item : aths.ToJavaList()) {
			athMultis += item.getNoOfObj() + "@";
		}
		athMultis += "@";

		// 图片附件.
		String athImgs = "@";
		FrmImgAths fias = new FrmImgAths();
		;
		fias.Retrieve(MapDtlAttr.FK_MapData, fk_mapdata);
		for (FrmImgAth item : fias.ToJavaList()) {
			athImgs += item.getCtrlID() + "@";
		}
		athImgs += "@";

		// 附加元素..
		String eleIDs = "@";
		FrmEles feles = new FrmEles();
		;
		feles.Retrieve(MapDtlAttr.FK_MapData, fk_mapdata);
		for (FrmEle item : feles.ToJavaList()) {
			eleIDs += item.getEleID() + "@";
		}
		eleIDs += "@";

		// 框架
		String frameIDs = "@";
		MapFrames frames = new MapFrames();
		frames.Retrieve(MapDtlAttr.FK_MapData, fk_mapdata);
		for (MapFrame item : frames.ToJavaList()) {
			frameIDs += item.getMyPK() + "@";
		}
		frameIDs += "@";

		/// #endregion 求PKs.

		// 保存线.
		JSONArray form_Lines = formData.getJSONObject("m").getJSONArray("connectors");
		BP.Sys.CCFormParse.SaveLine(fk_mapdata, form_Lines);

		// 其他控件，Label,Img,TextBox
		JSONArray form_Controls = formData.getJSONObject("s").getJSONArray("figures");

		if (form_Controls.isArray() == false || form_Controls.size() == 0) {
			/* 画布里没有任何东西, 清楚所有的元素. */
			String delSqls = "";
			delSqls += "@DELETE FROM Sys_MapAttr WHERE FK_MapData='" + fk_mapdata + "' AND KeyOfEn NOT IN ('OID')";
			delSqls += "@DELETE FROM Sys_FrmRB WHERE FK_MapData='" + fk_mapdata + "'"; // 枚举值的相关rb.
			delSqls += "@DELETE FROM Sys_MapDtl WHERE FK_MapData='" + fk_mapdata + "'";
			delSqls += "@DELETE FROM Sys_FrmBtn WHERE FK_MapData='" + fk_mapdata + "'";
			delSqls += "@DELETE FROM Sys_FrmLine WHERE FK_MapData='" + fk_mapdata + "'";
			delSqls += "@DELETE FROM Sys_FrmLab WHERE FK_MapData='" + fk_mapdata + "'";
			delSqls += "@DELETE FROM Sys_FrmLink WHERE FK_MapData='" + fk_mapdata + "'";
			delSqls += "@DELETE FROM Sys_FrmImg WHERE FK_MapData='" + fk_mapdata + "'";
			delSqls += "@DELETE FROM Sys_FrmAttachment WHERE FK_MapData='" + fk_mapdata + "'";
			delSqls += "@DELETE FROM Sys_FrmEle WHERE FK_MapData='" + fk_mapdata + "'";
			delSqls += "@DELETE FROM Sys_FrmImgAth WHERE FK_MapData='" + fk_mapdata + "'";
			delSqls += "@DELETE FROM Sys_MapFrame WHERE FK_MapData='" + fk_mapdata + "'";

			BP.DA.DBAccess.RunSQLs(delSqls);
			return;
		}

		String flowEle = "";
		String sqls = "";

		String nodeIDStr = fk_mapdata.replace("ND", "");
		int nodeID = 0;
		if (BP.DA.DataType.IsNumStr(nodeIDStr) == true) {
			nodeID = Integer.parseInt(nodeIDStr);
		}

		// 流程控件.
		String flowCtrls = "";

		// 循环元素.
		for (int idx = 0, jControl = form_Controls.size(); idx < jControl; idx++) {
			JSONObject control = (JSONObject) form_Controls.getJSONObject(idx); // 不存在控件类型不进行处理，继续循环.
			if (control == null || control.get("CCForm_Shape") == null) {
				continue;
			}

			String shape = control.get("CCForm_Shape").toString();

			if (control.optString("CCForm_MyPK") == null) {
				continue;
			}

			if (control.get("CCForm_MyPK") == null) {
				continue;
			}

			String ctrlID = control.get("CCForm_MyPK").toString();
			JSONArray properties = control.getJSONArray("properties"); // 属性集合.

			/// #region 装饰类控件.
			switch (shape) {
			case "Label": // 保存标签.
				if (ctrlID.indexOf("RB_") == 0) {
					/* 让其向下运行. */
					shape = "RadioButtonItem";
				} else {
					BP.Sys.CCFormParse.SaveLabel(fk_mapdata, control, properties, labelPKs, ctrlID);
					labelPKs = labelPKs.replace(ctrlID + "@", "@");
				}
				continue;
			case "Button": // 保存Button.
				BP.Sys.CCFormParse.SaveButton(fk_mapdata, control, properties, btnsPKs, ctrlID);
				btnsPKs = btnsPKs.replace(ctrlID + "@", "@");
				continue;
			case "HyperLink": // 保存link.
				BP.Sys.CCFormParse.SaveHyperLink(fk_mapdata, control, properties, linkPKs, ctrlID);
				linkPKs = linkPKs.replace(ctrlID + "@", "@");
				continue;
			case "Image": // 保存Img.
				BP.Sys.CCFormParse.SaveImage(fk_mapdata, control, properties, imgPKs, ctrlID);
				imgPKs = imgPKs.replace(ctrlID + "@", "@");
				continue;
			default:
				break;
			}

			/// #endregion 装饰类控件.

			/// #region 数据类控件.
			if (shape.contains("TextBox") == true || shape.contains("DropDownList") == true
					|| FrmEle.HandSiganture.equals(shape)) {
				BP.Sys.CCFormParse.SaveMapAttr(fk_mapdata, ctrlID, shape, control, properties, attrPKs);
				attrPKs = attrPKs.replace("@" + ctrlID + "@", "@");
				continue;
			}

			// 求出公共的属性-坐标.
			JSONObject style = control.getJSONObject("style");
			JSONArray vector = style.getJSONArray("gradientBounds");
			float x = Float.parseFloat(String.valueOf(vector.get(0)));
			float y = Float.parseFloat(String.valueOf(vector.get(1)));
			float maxX = Float.parseFloat(String.valueOf(vector.get(2)));
			float maxY = Float.parseFloat(String.valueOf(vector.get(3)));
			float width = maxX - x;
			float height = maxY - y;

			if (shape.equals("Dtl")) {
				// 记录已经存在的ID， 需要当时保存.
				BP.Sys.CCFormParse.SaveDtl(fk_mapdata, ctrlID, x, y, height, width);
				dtlPKs = dtlPKs.replace("@" + ctrlID + "@", "@");
				continue;
			}

			/// #endregion 数据类控件.

			/// #region 附件.
			if (shape.equals("AthMulti") || shape.equals("AthSingle")) {
				// 记录已经存在的ID， 需要当时保存.
				BP.Sys.CCFormParse.SaveAthMulti(fk_mapdata, ctrlID, x, y, height, width);
				athMultis = athMultis.replace("@" + ctrlID + "@", "@");
				continue;
			}
			if (shape.equals("AthImg")) {
				// 记录已经存在的ID， 需要当时保存.
				BP.Sys.CCFormParse.SaveAthImg(fk_mapdata, ctrlID, x, y, height, width);
				athImgs = athImgs.replace("@" + ctrlID + "@", "@");
				continue;
			}

			// 存储到FrmEle 类的控件，都可以使用该方法保存.
			if (shape.equals("Fieldset") || FrmEle.Fieldset.equals(shape)) {
				// 记录已经存在的ID， 需要当时保存.
				BP.Sys.CCFormParse.SaveFrmEle(fk_mapdata, shape, ctrlID, x, y, height, width);
				eleIDs = eleIDs.replace("@" + ctrlID + "@", "@");
				continue;
			}

			if (shape.equals("iFrame")) {
				// 记录已经存在的ID， 需要当时保存.
				BP.Sys.CCFormParse.SaveMapFrame(fk_mapdata, shape, ctrlID, x, y, height, width);
				frameIDs = frameIDs.replace("@" + ctrlID + "@", "@");
				continue;
			}

			if (shape.equals("RadioButton")) {
				if (ctrlID.contains("=") == true) {
					continue;
				}

				// 记录已经存在的ID， 需要当时保存.
				if (ctrlID.contains("RB_") == true) {
					ctrlID = ctrlID.substring(3);
				}

				String str = BP.Sys.CCFormParse.SaveFrmRadioButton(fk_mapdata, ctrlID, x, y);
				if (str == null) {
					continue;
				}

				attrPKs = attrPKs.replace("@" + str + "@", "@");
				continue;
			}

			if (shape.equals("RadioButton")) {
				continue;
			}

			/// #endregion 附件.

			/// #region 处理流程组件, 如果已经传来节点ID,说明是节点表单.
			// 流程类的组件,都记录下来放入到Sys_MapData.FlowCtrls 字段里. 记录控件的位置，原来记录到节点里的都要取消掉.
			// @zhoupeng
			if (shape.equals("FlowChart") || shape.equals("FrmCheck") || shape.equals("SubFlowDtl")
					|| shape.equals("ThreadDtl")) {
				if (flowCtrls.contains(shape) == false) {
					flowCtrls += "@Ctrl=" + shape + ",X=" + x + ",Y=" + y + ",H=" + height + ",W=" + width;
				}
			}

			if (nodeID != 0) {
				sqls = "";
				switch (shape) {
				case "FlowChart":
					if (DBAccess.RunSQLReturnString("SELECT FrmTrackSta FROM WF_Node WHERE NodeID=" + nodeID)
							.equals("0")) {
						/* 状态是 0 就把他启用起来. */
						sqls += "@UPDATE WF_Node SET FrmTrackSta=1,FrmTrack_X=" + x + ",FrmTrack_Y=" + y
								+ ",FrmTrack_H=" + height + ", FrmTrack_W=" + width + " WHERE NodeID=" + nodeIDStr;
					} else {
						/* 仅仅更新位置与高度。 */
						sqls += "@UPDATE WF_Node SET FrmTrack_X=" + x + ",FrmTrack_Y=" + y + ",FrmTrack_H=" + height
								+ ", FrmTrack_W=" + width + " WHERE NodeID=" + nodeIDStr;
					}
					flowEle += shape + ",";
					continue;
				case "FrmCheck":
					if (DBAccess.RunSQLReturnString("SELECT FWCSta FROM WF_Node WHERE NodeID=" + nodeID).equals("0")) {
						/* 状态是 0 就把他启用起来. */
						sqls += "@UPDATE WF_Node SET FWCSta=1,FWC_X=" + x + ",FWC_Y=" + y + ",FWC_H=" + height
								+ ", FWC_W=" + width + " WHERE NodeID=" + nodeIDStr;
					} else {
						/* 仅仅更新位置与高度。 */
						sqls += "@UPDATE WF_Node SET FWC_X=" + x + ",FWC_Y=" + y + ",FWC_H=" + height + ", FWC_W="
								+ width + " WHERE NodeID=" + nodeIDStr;
					}
					flowEle += shape + ",";
					continue;
				case "SubFlowDtl": // 子流程
					if (DBAccess.RunSQLReturnString("SELECT SFSta FROM WF_Node WHERE NodeID=" + nodeID).equals("0")) {
						/* 状态是 0 就把他启用起来. */
						sqls += "@UPDATE WF_Node SET SFSta=1,SF_X=" + x + ",SF_Y=" + y + ",SF_H=" + height + ", SF_W="
								+ width + " WHERE NodeID=" + nodeIDStr;
					} else {
						/* 仅仅更新位置与高度。 */
						sqls += "@UPDATE WF_Node SET SF_X=" + x + ",SF_Y=" + y + ",SF_H=" + height + ", SF_W=" + width
								+ " WHERE NodeID=" + nodeIDStr;
					}
					flowEle += shape + ",";
					continue;
				case "ThreadDtl": // 子线程
					if (DBAccess.RunSQLReturnString("SELECT FrmThreadSta FROM WF_Node WHERE NodeID=" + nodeID)
							.equals("0")) {
						/* 状态是 0 就把他启用起来. */
						sqls += "@UPDATE WF_Node SET FrmThreadSta=1,FrmThread_X=" + x + ",FrmThread_Y=" + y
								+ ",FrmThread_H=" + height + ",FrmThread_W=" + width + " WHERE NodeID=" + nodeIDStr;
					} else {
						/* 仅仅更新位置与高度。 */
						sqls += "@UPDATE WF_Node SET FrmThread_X=" + x + ",FrmThread_Y=" + y + ",FrmThread_H=" + height
								+ ", FrmThread_W=" + width + " WHERE NodeID=" + nodeIDStr;
					}
					flowEle += shape + ",";
					continue;
				case "FrmTransferCustom": // 流转自定义
					if (DBAccess.RunSQLReturnString("SELECT FTCSta FROM WF_Node WHERE NodeID=" + nodeID).equals("0")) {
						/* 状态是 0 就把他启用起来. */
						sqls += "@UPDATE WF_Node SET FTCSta=1,FTC_X=" + x + ",FTC_Y=" + y + ",FTC_H=" + height
								+ ",FTC_W=" + width + " WHERE NodeID=" + nodeIDStr;
					} else {
						/* 仅仅更新位置与高度。 */
						sqls += "@UPDATE WF_Node SET FTC_X=" + x + ",FTC_Y=" + y + ",FrmThread_H=" + height + ",FTC_W="
								+ width + " WHERE NodeID=" + nodeIDStr;
					}
					flowEle += shape + ",";
					continue;
				default:
					break;
				}
			}

			/// #endregion 处理流程组件.

			if (shape.equals("FlowChart") || shape.equals("FrmCheck") || shape.equals("SubFlowDtl")
					|| shape.equals("ThreadDtl")) {
				continue;
			}

			throw new RuntimeException("@没有判断的ccform保存控件的类型:shape = " + shape);
		}

		/// #region 处理节点表单。
		if (nodeID != 0) {
			// 轨迹组件.
			if (flowEle.contains("FlowChart") == false) {
				sqls += "@UPDATE WF_Node SET FrmTrackSta=0 WHERE NodeID=" + nodeID;
			}

			// 审核组件.
			if (flowEle.contains("FrmCheck") == false) {
				sqls += "@UPDATE WF_Node SET FWCSta=0 WHERE NodeID=" + nodeID;
			}

			// 子流程组件.
			if (flowEle.contains("SubFlowDtl") == false) {
				sqls += "@UPDATE WF_Node SET SFSta=0 WHERE NodeID=" + nodeID;
			}

			// 子线城组件.
			if (flowEle.contains("ThreadDtl") == false) {
				sqls += "@UPDATE WF_Node SET FrmThreadSta=0 WHERE NodeID=" + nodeID;
			}

			// 自定义流程组件.
			if (flowEle.contains("FrmTransferCustom") == false) {
				sqls += "@UPDATE WF_Node SET FTCSta=0 WHERE NodeID=" + nodeID;
			}
		}

		// 执行要更新的sql.
		if (!sqls.equals("")) {
			BP.DA.DBAccess.RunSQLs(sqls);
			sqls = "";
		}

		// 更新组件. @zhoupeng.
		DBAccess.RunSQL("UPDATE Sys_MapData SET FlowCtrls='" + flowCtrls + "' WHERE No='" + fk_mapdata + "'");

		/// #endregion 处理节点表单。

		/// #region 删除没有替换下来的 PKs, 说明这些都已经被删除了.
		String[] pks = labelPKs.split("[@]", -1);
		sqls = "";
		for (String pk : pks) {
			if (DataType.IsNullOrEmpty(pk)) {
				continue;
			}
			sqls += "@DELETE FROM Sys_FrmLab WHERE MyPK='" + pk + "'";
		}

		pks = btnsPKs.split("[@]", -1);
		for (String pk : pks) {
			if (DataType.IsNullOrEmpty(pk)) {
				continue;
			}
			sqls += "@DELETE FROM Sys_FrmBtn WHERE MyPK='" + pk + "'";
		}

		pks = linkPKs.split("[@]", -1);
		for (String pk : pks) {
			if (DataType.IsNullOrEmpty(pk)) {
				continue;
			}

			sqls += "@DELETE FROM Sys_FrmLink WHERE MyPK='" + pk + "'";
		}

		pks = imgPKs.split("[@]", -1);
		for (String pk : pks) {
			if (DataType.IsNullOrEmpty(pk)) {
				continue;
			}

			sqls += "@DELETE FROM Sys_FrmImg WHERE MyPK='" + pk + "'";
		}

		pks = attrPKs.split("[@]", -1);
		for (String pk : pks) {
			if (DataType.IsNullOrEmpty(pk)) {
				continue;
			}

			if (pk.equals("OID")) {
				continue;
			}

			sqls += "@DELETE FROM Sys_MapAttr WHERE KeyOfEn='" + pk + "' AND FK_MapData='" + fk_mapdata + "'";
			sqls += "@DELETE FROM Sys_FrmRB WHERE KeyOfEn='" + pk + "' AND FK_MapData='" + fk_mapdata + "'";
		}

		pks = dtlPKs.split("[@]", -1);
		for (String pk : pks) {
			if (DataType.IsNullOrEmpty(pk)) {
				continue;
			}

			// 调用删除逻辑.
			MapDtl dtl = new MapDtl();
			dtl.setNo(pk);
			dtl.RetrieveFromDBSources();
			dtl.Delete();

			// sqls += "@DELETE FROM Sys_MapDtl WHERE No='" + pk + "'";
		}

		pks = athMultis.split("[@]", -1);
		for (String pk : pks) {
			if (DataType.IsNullOrEmpty(pk)) {
				continue;
			}
			sqls += "@DELETE FROM Sys_FrmAttachment WHERE NoOfObj='" + pk + "' AND FK_MapData='" + fk_mapdata + "'";
		}

		// 删除图片附件.
		pks = athImgs.split("[@]", -1);
		for (String pk : pks) {
			if (DataType.IsNullOrEmpty(pk)) {
				continue;
			}

			sqls += "@DELETE FROM Sys_FrmImgAth WHERE CtrlID='" + pk + "' AND FK_MapData='" + fk_mapdata + "'";
		}

		// 删除这些，没有替换下来的数据.
		BP.DA.DBAccess.RunSQLs(sqls);

		/// #endregion 删除没有替换下来的 PKs, 说明这些都已经被删除了.

		// 清空缓存
		MapData mymd = new MapData(fk_mapdata);
		mymd.RepairMap();
		SystemConfig.DoClearCash();
	}

	/**
	 * 复制表单
	 * 
	 * @param srcFrmID
	 *            源表单ID
	 * @param copyFrmID
	 *            copy到表单ID
	 * @param copyFrmName
	 *            表单名称
	 * @throws Exception
	 */
	public static String CopyFrm(String srcFrmID, String copyFrmID, String copyFrmName, String fk_frmTree)
			throws Exception {
		MapData mymd = new MapData();
		mymd.setNo(copyFrmID);
		if (mymd.RetrieveFromDBSources() == 1) {
			throw new RuntimeException("@目标表单ID:" + copyFrmID + "已经存在，位于:" + mymd.getFK_FormTreeText() + "目录下.");
		}

		// 获得源文件信息.
		DataSet ds = GenerHisDataSet(srcFrmID);

		// 导入表单文件.
		ImpFrmTemplate(copyFrmID, ds, false);

		// 复制模版文件.
		MapData md = new MapData(copyFrmID);

		if (md.getHisFrmType() == FrmType.ExcelFrm) {
			/* 如果是excel表单，那就需要复制excel文件. */
			String srcFile = SystemConfig.getPathOfDataUser() + "FrmOfficeTemplate/" + srcFrmID + ".xls";
			String toFile = SystemConfig.getPathOfDataUser() + "FrmOfficeTemplate/" + copyFrmID + ".xls";
			if ((new File(srcFile)).isFile() == true) {
				if ((new File(toFile)).isFile() == false) {
					Files.copy(Paths.get(srcFile), Paths.get(toFile), StandardCopyOption.COPY_ATTRIBUTES);
				}
			}

			srcFile = SystemConfig.getPathOfDataUser() + "FrmOfficeTemplate/" + srcFrmID + ".xlsx";
			toFile = SystemConfig.getPathOfDataUser() + "FrmOfficeTemplate/" + copyFrmID + ".xlsx";
			if ((new File(srcFile)).isFile() == true) {
				if ((new File(toFile)).isFile() == false) {
					Files.copy(Paths.get(srcFile), Paths.get(toFile), StandardCopyOption.COPY_ATTRIBUTES);
				}
			}
		}

		md.Retrieve();

		md.setFK_FormTree(fk_frmTree);
		md.setFK_FrmSort(fk_frmTree);
		md.setName(copyFrmName);
		md.Update();

		return "表单复制成功,您需要重新登录，或者刷新才能看到。";
	}

	/**
	 * 导入表单API
	 * 
	 * @param toFrmID
	 *            要导入的表单ID
	 * @param fromds
	 *            数据源
	 * @param isSetReadonly
	 *            是否把空间设置只读？
	 * @throws Exception 
	 */
	public static void ImpFrmTemplate(String toFrmID, DataSet fromds, boolean isSetReadonly) throws Exception {
		MapData.ImpMapData(toFrmID, fromds);
	}

	/**
	 * 修改frm的事件
	 * 
	 * @param frmID
	 * @throws Exception
	 */
	public static void AfterFrmEditAction(String frmID) throws Exception {
		 
		//删除缓存.
		BP.DA.CashFrmTemplate.Remove(frmID);
		BP.DA.Cash.SetMap(frmID, null);

		MapData mapdata = new MapData();
		mapdata.setNo(frmID);
		mapdata.RetrieveFromDBSources();
		Cash2019.UpdateRow(mapdata.toString(), frmID, mapdata.getRow());
		mapdata.CleanObject();
		return;
	}

	/**
	 * 获得表单信息.
	 * 
	 * @param frmID
	 *            表单
	 * @return
	 * @throws Exception
	 */

	public static DataSet GenerHisDataSet(String frmID) throws Exception {
		return GenerHisDataSet(frmID, null);
	}

	public static DataSet GenerHisDataSet(String frmID, String frmName) throws Exception {
		DataSet dsFrm = BP.DA.CashFrmTemplate.GetFrmDataSetModel(frmID);
		if (dsFrm != null) {
			return dsFrm;
		}

		DataSet ds = new DataSet();

		// 创建实体对象.
		MapData md = new MapData(frmID);

		if (DataType.IsNullOrEmpty(md.getName()) == true && frmName != null) {
			md.setName(frmName);
		}

		// 加入主表信息.
		DataTable Sys_MapData = md.ToDataTableField("Sys_MapData");
		ds.Tables.add(Sys_MapData);

		// 加入分组表.
		DataTable Sys_GroupField = md.getGroupFields().ToDataTableField("Sys_GroupField");
		ds.Tables.add(Sys_GroupField);

		// 加入明细表.
		DataTable Sys_MapDtl = md.getOrigMapDtls().ToDataTableField("Sys_MapDtl");
		ds.Tables.add(Sys_MapDtl);

		// 加入枚举表.
		DataTable Sys_Menu = md.getSysEnums().ToDataTableField("Sys_Enum");
		ds.Tables.add(Sys_Menu);

		// 加入外键属性.
		DataTable Sys_MapAttr = md.getMapAttrs().ToDataTableField("Sys_MapAttr");
		ds.Tables.add(Sys_MapAttr);

		// 加入扩展属性.
		DataTable Sys_MapExt = md.getMapExts().ToDataTableField("Sys_MapExt");
		ds.Tables.add(Sys_MapExt);

		// 线.
		DataTable Sys_FrmLine = md.getFrmLines().ToDataTableField("Sys_FrmLine");
		ds.Tables.add(Sys_FrmLine);

		// link.
		DataTable Sys_FrmLink = md.getFrmLinks().ToDataTableField("Sys_FrmLink");
		ds.Tables.add(Sys_FrmLink);

		// btn.
		DataTable Sys_FrmBtn = md.getFrmBtns().ToDataTableField("Sys_FrmBtn");
		ds.Tables.add(Sys_FrmBtn);

		// Sys_FrmLab.
		DataTable Sys_FrmLab = md.getFrmLabs().ToDataTableField("Sys_FrmLab");
		ds.Tables.add(Sys_FrmLab);

		// img.
		DataTable Sys_FrmImg = md.getFrmImgs().ToDataTableField("Sys_FrmImg");
		ds.Tables.add(Sys_FrmImg);

		// Sys_FrmRB.
		DataTable Sys_FrmRB = md.getFrmRBs().ToDataTableField("Sys_FrmRB");
		ds.Tables.add(Sys_FrmRB);

		// Sys_FrmEle.
		DataTable Sys_FrmEle = md.getFrmEles().ToDataTableField("Sys_FrmEle");
		ds.Tables.add(Sys_FrmEle);

		// Sys_MapFrame.
		DataTable Sys_MapFrame = md.getMapFrames().ToDataTableField("Sys_MapFrame");
		ds.Tables.add(Sys_MapFrame);

		// Sys_FrmAttachment.
		DataTable Sys_FrmAttachment = md.getFrmAttachments().ToDataTableField("Sys_FrmAttachment");
		ds.Tables.add(Sys_FrmAttachment);

		// FrmImgAths. 上传图片附件.
		DataTable Sys_FrmImgAth = md.getFrmImgAths().ToDataTableField("Sys_FrmImgAth");
		ds.Tables.add(Sys_FrmImgAth);

		// FrmImgAthDBs 上传图片信息
		DataTable Sys_FrmImgAthDB = md.getFrmImgAthDB().ToDataTableField("Sys_FrmImgAthDB");
		ds.Tables.add(Sys_FrmImgAthDB);

		// 放入缓存.
		BP.DA.CashFrmTemplate.Put(frmID, ds);

		return ds;
	}

	public static DataSet GenerHisDataSet_AllEleInfo(String fk_mapdata) throws Exception {

		MapData md = new MapData(fk_mapdata);

		// 求出 frmIDs
		String frmIDs = "'" + fk_mapdata + "'";
		MapDtls mdtls = new MapDtls(md.getNo());
		for (MapDtl item : mdtls.ToJavaList()) {
			frmIDs += ",'" + item.getNo() + "'";
		}

		DataSet ds = new DataSet();

		// 加入主表信息.
		DataTable Sys_MapData = md.ToDataTableField("Sys_MapData");
		ds.Tables.add(Sys_MapData);

		// 加入分组表.
		GroupFields gfs = new GroupFields();
		gfs.RetrieveIn(GroupFieldAttr.FrmID, frmIDs);
		DataTable Sys_GroupField = gfs.ToDataTableField("Sys_GroupField");
		ds.Tables.add(Sys_GroupField);

		// 加入明细表.
		DataTable Sys_MapDtl = md.getOrigMapDtls().ToDataTableField("Sys_MapDtl");
		ds.Tables.add(Sys_MapDtl);

		// 加入枚举表.
		SysEnums ses = new SysEnums();
		ses.RetrieveInSQL(SysEnumAttr.EnumKey,
				"SELECT UIBindKey FROM Sys_MapAttr WHERE FK_MapData IN (" + frmIDs + ") ");
		DataTable Sys_Menu = ses.ToDataTableField("Sys_Enum");
		ds.Tables.add(Sys_Menu);

		// 加入字段属性.
		MapAttrs attrs = new MapAttrs();
		attrs.RetrieveIn(MapAttrAttr.FK_MapData, frmIDs);
		DataTable Sys_MapAttr = attrs.ToDataTableField("Sys_MapAttr");
		ds.Tables.add(Sys_MapAttr);

		// 加入扩展属性.
		MapExts exts = new MapExts();
		exts.RetrieveIn(MapAttrAttr.FK_MapData, frmIDs);
		DataTable Sys_MapExt = exts.ToDataTableField("Sys_MapExt");
		ds.Tables.add(Sys_MapExt);

		// 线.
		DataTable Sys_FrmLine = md.getFrmLines().ToDataTableField("Sys_FrmLine");
		ds.Tables.add(Sys_FrmLine);

		// link.
		DataTable Sys_FrmLink = md.getFrmLinks().ToDataTableField("Sys_FrmLink");
		ds.Tables.add(Sys_FrmLink);

		// btn.
		DataTable Sys_FrmBtn = md.getFrmBtns().ToDataTableField("Sys_FrmBtn");
		ds.Tables.add(Sys_FrmBtn);

		// Sys_FrmLab.
		FrmLabs frmlabs = new FrmLabs();
		frmlabs.RetrieveIn(MapAttrAttr.FK_MapData, frmIDs);
		DataTable Sys_FrmLab = frmlabs.ToDataTableField("Sys_FrmLab");
		ds.Tables.add(Sys_FrmLab);

		// img.
		// Sys_FrmLab.
		FrmImgs frmImgs = new FrmImgs();
		frmImgs.RetrieveIn(MapAttrAttr.FK_MapData, frmIDs);
		// DataTable Sys_FrmImg = md.FrmImgs.ToDataTableField("Sys_FrmImg");
		ds.Tables.add(frmImgs.ToDataTableField("Sys_FrmImg"));

		// Sys_FrmRB.
		DataTable Sys_FrmRB = md.getFrmRBs().ToDataTableField("Sys_FrmRB");
		ds.Tables.add(Sys_FrmRB);

		// Sys_FrmEle.
		DataTable Sys_FrmEle = md.getFrmEles().ToDataTableField("Sys_FrmEle");
		ds.Tables.add(Sys_FrmEle);

		// Sys_MapFrame.
		DataTable Sys_MapFrame = md.getMapFrames().ToDataTableField("Sys_MapFrame");
		ds.Tables.add(Sys_MapFrame);

		// Sys_FrmAttachment.
		DataTable Sys_FrmAttachment = md.getFrmAttachments().ToDataTableField("Sys_FrmAttachment");
		ds.Tables.add(Sys_FrmAttachment);

		// FrmImgAths. 上传图片附件.
		DataTable Sys_FrmImgAth = md.getFrmImgAths().ToDataTableField("Sys_FrmImgAth");
		ds.Tables.add(Sys_FrmImgAth);

		return ds;
	}

	/**
	 * 获得表单模版dataSet格式.
	 * 
	 * @param fk_mapdata
	 *            表单ID
	 * @param isCheckFrmType
	 *            是否检查表单类型
	 * @return DataSet
	 * @throws Exception
	 */

	public static DataSet GenerHisDataSet_AllEleInfo2017(String fk_mapdata) throws Exception {
		return GenerHisDataSet_AllEleInfo2017(fk_mapdata, false);
	}

	public static DataSet GenerHisDataSet_AllEleInfo2017(String fk_mapdata, boolean isCheckFrmType) throws Exception {
		MapData md = new MapData(fk_mapdata);

		// 从表.
		String sql = "SELECT * FROM Sys_MapDtl WHERE FK_MapData ='{0}'";
		sql = String.format(sql, fk_mapdata);
		DataTable dtMapDtl = DBAccess.RunSQLReturnTable(sql);
		dtMapDtl.TableName = "Sys_MapDtl";

		String ids = String.format("'%1$s'", fk_mapdata);
		for (DataRow dr : dtMapDtl.Rows) {
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
		sql = "@SELECT * FROM Sys_Enum WHERE EnumKey IN ( SELECT UIBindKey FROM Sys_MapAttr WHERE FK_MapData IN (" + ids
				+ ") ) order By EnumKey,IntKey";
		sqls += sql;

		// 审核组件
		String nodeIDstr = fk_mapdata.replace("ND", "");
		if (DataType.IsNumStr(nodeIDstr)) {
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

		// if (isCheckFrmType == true && md.HisFrmType == FrmType.FreeFrm)
		// {
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
		// }

		// Sys_FrmRB.
		listNames.add("Sys_FrmRB");
		sql = "@SELECT * FROM Sys_FrmRB WHERE " + where;
		sqls += sql;

		// ele.
		listNames.add("Sys_FrmEle");
		sql = "@SELECT * FROM Sys_FrmEle WHERE " + where;
		sqls += sql;

		// Sys_MapFrame.
		listNames.add("Sys_MapFrame");
		sql = "@SELECT * FROM Sys_MapFrame WHERE " + where;
		sqls += sql;

		// Sys_FrmAttachment.
		listNames.add("Sys_FrmAttachment");
		/* 20150730 小周鹏修改 添加AtPara 参数 START */
		// sql = "@SELECT
		// MyPK,FK_MapData,UploadType,X,Y,W,H,NoOfObj,Name,Exts,SaveTo,IsUpload,IsDelete,IsDownload
		// "
		// + " FROM Sys_FrmAttachment WHERE " + where + " AND FK_Node=0";
		sql = "@SELECT * " + " FROM Sys_FrmAttachment WHERE " + where + "";

		/* 20150730 小周鹏修改 添加AtPara 参数 END */
		sqls += sql;

		// Sys_FrmImgAth.
		listNames.add("Sys_FrmImgAth");

		sql = "@SELECT * FROM Sys_FrmImgAth WHERE " + where;
		sqls += sql;

		//// sqls.Replace(";", ";" + Environment.NewLine);
		// DataSet ds = DA.DBAccess.RunSQLReturnDataSet(sqls);
		// if (ds != null && ds.Tables.size() == listNames.size())
		// for (int i = 0; i < listNames.size(); i++)
		// {
		// ds.Tables[i].TableName = listNames[i];
		// }

		String[] strs = sqls.split("[@]", -1);
		DataSet ds = new DataSet();

		if (strs != null && strs.length == listNames.size()) {
			for (int i = 0; i < listNames.size(); i++) {
				String s = strs[i];
				if (DataType.IsNullOrEmpty(s)) {
					continue;
				}
				DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(s);
				dt.TableName = listNames.get(i);
				ds.Tables.add(dt);
			}
		}

		for (DataTable item : ds.Tables) {
			if (item.TableName.equals("Sys_MapAttr") && item.Rows.size() == 0) {
				md.RepairMap();
			}
		}

		ds.Tables.add(dtMapDtl);
		return ds;
	}

	/// #endregion 模版操作.

	/// #region 其他功能.
	/**
	 * 保存枚举
	 * 
	 * @param enumKey
	 *            键值对
	 * @param enumLab
	 *            标签
	 * @param cfg
	 *            配置 @0=xxx@1=yyyy@n=xxxxxc
	 * @return
	 * @throws Exception
	 */

	public static String SaveEnum(String enumKey, String enumLab, String cfg, boolean isNew) throws Exception {
		return SaveEnum(enumKey, enumLab, cfg, isNew, "CH");
	}

	// C# TO JAVA CONVERTER NOTE: Java does not support optional parameters.
	// Overloaded method(s) are created above:
	// ORIGINAL LINE: public static string SaveEnum(string enumKey, string
	// enumLab, string cfg, bool isNew, string lang = "CH")
	public static String SaveEnum(String enumKey, String enumLab, String cfg, boolean isNew, String lang)
			throws Exception {
		SysEnumMain sem = new SysEnumMain();
		sem.setNo(enumKey);
		int dataCount = sem.RetrieveFromDBSources();
		if (dataCount > 0 && isNew) {
			return "err@已存在枚举" + enumKey + ",请修改枚举名字";
		}

		if (dataCount == 0) {
			sem.setName(enumLab);
			sem.setCfgVal(cfg);
			sem.setLang(lang);
			sem.DirectInsert();
		} else {
			sem.setName(enumLab);
			sem.setCfgVal(cfg);
			sem.setLang(lang);
			sem.Update();
		}

		String[] strs = cfg.split("[@]", -1);
		for (String str : strs) {
			if (DataType.IsNullOrEmpty(str)) {
				continue;
			}
			String[] kvs = str.split("[=]", -1);
			SysEnum se = new SysEnum();
			se.setEnumKey(enumKey);
			se.setLang(lang);
			se.setIntKey(Integer.parseInt(kvs[0]));
			// 杨玉慧
			// 解决当 枚举值含有 ‘=’号时，保存不进去的方法
			String[] kvsValues = new String[kvs.length - 1];
			for (int i = 0; i < kvsValues.length; i++) {
				kvsValues[i] = kvs[i + 1];
			}
			se.setLab(StringHelper.join("=", kvsValues));
			se.setMyPK(se.getEnumKey() + "_" + se.getLang() + "_" + se.getIntKey());
			se.Save();
		}
		return "保存成功.";
	}

	/**
	 * 转拼音方法
	 * 
	 * @param name
	 *            字段中文名称
	 * @param isQuanPin
	 *            是否转换全拼
	 * @return 转化后的拼音，不成功则抛出异常.
	 */
	public static String ParseStringToPinyinField(String name, boolean isQuanPin) {
		if (BP.DA.DataType.IsNullOrEmpty(name) == true) {
			return "";
		}

		String s = "";
		try {
			if (isQuanPin == true) {
				s = BP.DA.DataType.ParseStringToPinyin(name);
				if (s.length() > 15) {
					s = BP.DA.DataType.ParseStringToPinyinJianXie(name);
				}
			} else {
				s = BP.DA.DataType.ParseStringToPinyinJianXie(name);
			}

			s = s.trim().replace(" ", "");
			s = s.trim().replace(" ", "");
			// 常见符号
			s = s.replace(",", "").replace(".", "").replace("，", "").replace("。", "").replace("!", "");
			s = s.replace("*", "").replace("@", "").replace("#", "").replace("~", "").replace("|", "");
			s = s.replace("$", "").replace("%", "").replace("&", "").replace("（", "").replace("）", "").replace("【", "")
					.replace("】", "");
			s = s.replace("(", "").replace(")", "").replace("[", "").replace("]", "").replace("{", "").replace("}", "")
					.replace("/", "");
			if (s.length() > 0) {
				// 去除开头数字
				String headStr = s.substring(0, 1);
				if (DataType.IsNumStr(headStr) == true) {
					s = "F" + s;
				}
			}
			// 去掉空格，去掉点.
			s = s.replace(" ", "");
			s = s.replace(".", "");
			return s;
		} catch (RuntimeException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}

	/**
	 * 转拼音全拼/简写方法(若转换后以数字开头，则前面加F)
	 * <p>
	 * added by liuxc,2017-9-25
	 * </p>
	 * 
	 * @param name
	 *            中文字符串
	 * @param isQuanPin
	 *            是否转换全拼
	 * @param removeSpecialSymbols
	 *            是否去除特殊符号，仅保留汉字、数字、字母、下划线
	 * @param maxLen
	 *            转化后字符串最大长度，0为不限制
	 * @return 转化后的拼音，不成功则抛出异常.
	 */
	public static String ParseStringToPinyinField(String name, boolean isQuanPin, boolean removeSpecialSymbols,
			int maxLen) {
		if (BP.DA.DataType.IsNullOrEmpty(name) == true) {
			return "";
		}

		String s = "";

		if (removeSpecialSymbols) {
			name = DataType.ParseStringForName(name, maxLen);
		}

		try {
			if (isQuanPin == true) {
				s = BP.DA.DataType.ParseStringToPinyin(name);
			} else {
				s = BP.DA.DataType.ParseStringToPinyinJianXie(name);
			}

			// 如果全拼长度超过maxLen，则取前maxLen长度的字符
			if (maxLen > 0 && s.length() > maxLen) {
				s = s.substring(0, maxLen);
			}

			if (s.length() > 0) {
				// 去除开头数字
				String headStr = s.substring(0, 1);
				if (DataType.IsNumStr(headStr) == true) {
					s = "F" + (s.length() > maxLen - 1 ? s.substring(0, maxLen - 1) : s);
				}
			}

			return s;
		} catch (RuntimeException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}

	/**
	 * 多音字转拼音
	 * 
	 * @param charT
	 *            单个汉字
	 * @return 包含返回拼音，否则返回null
	 */
	public static String ChinaMulTonesToPinYin(String charT) {
		try {
			ChMulToneXmls mulChs = new ChMulToneXmls();
			mulChs.RetrieveAll();
			for (ChMulToneXml en : mulChs.ToJavaList()) {
				if (en.getNo().equals(charT)) {
					return en.getName();
				}
			}
		} catch (RuntimeException ex) {
			throw new RuntimeException(ex.getMessage());
		}
		return null;
	}

	/**
	 * 获得外键表
	 * 
	 * @param pageNumber
	 *            第几页
	 * @param pageSize
	 *            每页大小
	 * @return json
	 * @throws Exception
	 */
	public static String DB_SFTableList(int pageNumber, int pageSize) throws Exception {
		// 获得查询.
		SFTables sftables = new SFTables();
		QueryObject obj = new QueryObject(sftables);
		int RowCount = obj.GetCount();

		// 查询
		obj.DoQuery(SysEnumMainAttr.No, pageSize, pageNumber);

		// 转化成json.
		return BP.Tools.Json.ToJson(sftables.ToDataTableField());
	}

	/**
	 * 获得隐藏字段集合.
	 * 
	 * @param fk_mapdata
	 * @return
	 * @throws Exception
	 */
	public static String DB_Hiddenfielddata(String fk_mapdata) throws Exception {
		MapAttrs mapAttrs = new MapAttrs();
		QueryObject obj = new QueryObject(mapAttrs);
		obj.AddWhere(MapAttrAttr.FK_MapData, fk_mapdata);
		obj.addAnd();
		obj.AddWhere(MapAttrAttr.UIVisible, "0");
		obj.addAnd();
		obj.AddWhere(MapAttrAttr.EditType, "0");
		// 查询
		obj.DoQuery();

		return mapAttrs.ToJson();
	}

	/// #endregion 其他功能.

}