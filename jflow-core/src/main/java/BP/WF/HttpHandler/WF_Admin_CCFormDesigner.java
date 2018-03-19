package BP.WF.HttpHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import org.apache.http.protocol.HttpContext;

import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.Log;
import BP.En.EditType;
import BP.En.FieldTypeS;
import BP.En.UIContralType;
import BP.Sys.GEDtl;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.MapDataAttr;
import BP.Sys.MapDtl;
import BP.Sys.SFDBSrc;
import BP.Sys.SysEnumMain;
import BP.Sys.SystemConfig;
import BP.WF.HttpHandler.Base.WebContralBase;
import BP.Web.WebUser;

public class WF_Admin_CCFormDesigner extends WebContralBase {
	
	// / <summary>
	// / 初始化表单
	// / </summary>
	// / <returns></returns>
	public String FormDesigner_InitMapData() {
		MapData md = new MapData(this.getFK_MapData());
		return md.ToJson();
	}

	/**
	 * 初始化数据
	 * 
	 * @param mycontext
	 */
	public WF_Admin_CCFormDesigner(HttpContext mycontext) {
		this.context = mycontext;
	}

	public WF_Admin_CCFormDesigner() {
	}

	/**
	 * 创建枚举类型字段
	 * 
	 * @return
	 */
	public final String FrmEnumeration_NewEnumField() {
		UIContralType ctrl = UIContralType.RadioBtn;
		String ctrlDoType = GetRequestVal("CtrlDoType");
		if ("DDL".equals(ctrlDoType))
			ctrl = UIContralType.DDL;
		else
			ctrl = UIContralType.RadioBtn;

		String fk_mapdata = this.GetRequestVal("FK_MapData");
		String keyOfEn = this.GetRequestVal("KeyOfEn");
		String fieldDesc = this.GetRequestVal("Name");
		String enumKeyOfBind = this.GetRequestVal("UIBindKey"); // 要绑定的enumKey.
		float x = this.GetRequestValFloat("x");
		float y = this.GetRequestValFloat("y");

		BP.Sys.CCFormAPI.NewEnumField(fk_mapdata, keyOfEn, fieldDesc, enumKeyOfBind, ctrl, x, y, 1);
		return "绑定成功.";
	}

	/// <summary>
	/// 加载表单
	/// </summary>
	/// <returns></returns>
	public final String Loadform() throws IOException {
		MapData mapData = new MapData(this.getFK_MapData());
		return mapData.getFormJson(); // 要返回的值.
	}

	/**
	 * 创建外键字段.
	 * 
	 * @return
	 */
	public final String NewSFTableField() {
		try {
			String fk_mapdata = this.GetRequestVal("FK_MapData");
			String keyOfEn = this.GetRequestVal("KeyOfEn");
			String fieldDesc = this.GetRequestVal("Name");
			String sftable = this.GetRequestVal("UIBindKey");
			float x = Float.parseFloat(this.GetRequestVal("x"));
			float y = Float.parseFloat(this.GetRequestVal("y"));

			// 调用接口,执行保存.
			try {
				BP.Sys.CCFormAPI.SaveFieldSFTable(fk_mapdata, keyOfEn, fieldDesc, sftable, x, y, 0);
			} catch (Exception e) {
				return "设置失败";
			}
			return "设置成功";
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}

	/**
	 * 加载表单
	 * 
	 * @return
	 */
	public final String FormDesigner_Loadform() {
		MapData mapData = new MapData(this.getFK_MapData());
		try {
			return mapData.getFormJson(); // 要返回的值.
		} catch (IOException e) {
			Log.DebugWriteError("WF_Admin_CCFormDesigner Loadform() :" + e.getMessage());
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 转换拼音
	 * 
	 * @return
	 */
	public final String ParseStringToPinyin() {
		String name = GetRequestVal("name");
		String flag = GetRequestVal("flag");
		if (flag.equals("true")) {
			return BP.Sys.CCFormAPI.ParseStringToPinyinField(name, true);
		} else {
			return BP.Sys.CCFormAPI.ParseStringToPinyinField(name, false);
		}
	}

	/**
	 * 获取隐藏字段
	 * 
	 * @return
	 */
	public final String Hiddenfielddata() {
		return BP.Sys.CCFormAPI.DB_Hiddenfielddata(this.getFK_MapData());
	}

	public final String HiddenFieldDelete() {
		String records = GetRequestVal("records");
		String FK_MapData = GetRequestVal("FK_MapData");
		MapAttr mapAttrs = new MapAttr();
		int result = mapAttrs.Delete(MapAttrAttr.KeyOfEn, records, MapAttrAttr.FK_MapData, FK_MapData);
		return String.valueOf(result);
	}

	public final String CcformElements() {
		return CCForm_AllElements_ResponseJson();
	}

	/**
	 * 创建隐藏字段.
	 * 
	 * @return
	 */
	public final String NewHidF() {
//		String fk_mapdataHid = this.GetRequestVal("v1");
//		String key = this.GetRequestVal("v2");
//		String myname = this.GetRequestVal("v3");
//		int dataType = Integer.parseInt(this.GetRequestVal("v4"));
		MapAttr mdHid = new MapAttr();
//		mdHid.setMyPK(fk_mapdataHid + "_" + key);
//		mdHid.setFK_MapData(fk_mapdataHid);
//		mdHid.setKeyOfEn(key);
//		mdHid.setName(myname);
//		mdHid.setMyDataType(dataType);
		mdHid.setMyPK(this.getFK_MapData()+"_"+this.getKeyOfEn());
		mdHid.setFK_MapData(this.getFK_MapData());
		mdHid.setKeyOfEn(this.getKeyOfEn());
		mdHid.setName(this.getName());
		mdHid.setMyDataType(Integer.parseInt(this.GetRequestVal("FieldType")));
		mdHid.setHisEditType(EditType.Edit);
		mdHid.setMaxLen(100);
		mdHid.setMinLen(0);
		mdHid.setLGType(FieldTypeS.Normal);
		mdHid.setUIVisible(false);
		mdHid.setUIIsEnable(false);
		mdHid.Insert();

		return "创建成功..";
	}

	/**
	 * 默认执行的方法
	 * 
	 * @return
	 */
	@Override
	protected String DoDefaultMethod() {
		return "err@没有判断的执行标记:" + this.getDoType();
	}

	public final String NewFrmGuide_GenerPinYin() {
		String isQuanPin = this.GetRequestVal("IsQuanPin");
		String name = this.GetRequestVal("TB_Name");

		String str = "";
		if (isQuanPin.equals("1")) {
			str = BP.Sys.CCFormAPI.ParseStringToPinyinField(name, true);
		} else {
			str = BP.Sys.CCFormAPI.ParseStringToPinyinField(name, false);
		}

		MapData md = new MapData();
		md.setNo(str);
		if (md.RetrieveFromDBSources() == 0) {
			return str;
		}

		return "err@表单ID:" + str + "已经被使用.";
	}

	/** 
	 获得系统的表
	 
	 @return 
*/
	public final String NewFrmGuide_Init()
	{
		DataSet ds = new DataSet();

		SFDBSrc src = new SFDBSrc("local");
		ds.Tables.add(src.ToDataTableField("SFDBSrc"));

		DataTable tables = src.GetTables(true);
		tables.TableName = "Tables";
		ds.Tables.add(tables);

		return BP.Tools.Json.ToJson(ds);

	}
	
	public final String NewFrmGuide_Create() {
		MapData md = new MapData();
		md.setName(this.GetRequestVal("TB_Name"));
		md.setNo(this.GetRequestVal("TB_No"));
		md.setPTable(this.GetRequestVal("TB_PTable"));
		
		
		md.SetValByKey(MapDataAttr.PTableModel, this.GetRequestVal("DDL_PTableModel"));

		md.setFK_FrmSort(this.GetRequestVal("DDL_FrmTree"));
		md.setFK_FormTree(this.GetRequestVal("DDL_FrmTree"));
		md.setAppType("0"); // 独立表单
		md.setDBSrc(this.GetRequestVal("DDL_DBSrc"));
		if (md.getIsExits() == true) {
			return "err@表单ID:" + md.getNo() + "已经存在.";
		}

		md.setHisFrmTypeInt(this.GetRequestValInt("DDL_FrmType"));

		switch (md.getHisFrmType()) {
		// 自由，傻瓜，SL表单不做判断
		case FreeFrm:
		case FoolForm:
			break;
		case Url:
			md.setUrl(md.getPTable());
			break;
		// 如果是以下情况，导入模式
		case WordFrm:
		case ExcelFrm:
			break;
		default:
			throw new RuntimeException("未知表单类型.");
		}
		md.Insert();

		//增加上OID字段.
        BP.Sys.CCFormAPI.RepareCCForm(md.getNo());
		
		if (md.getHisFrmType() == BP.Sys.FrmType.WordFrm || md.getHisFrmType() == BP.Sys.FrmType.ExcelFrm) {
			/* 把表单模版存储到数据库里 */
			return "url@../../Comm/En.htm?EnsName=BP.WF.Template.MapFrmExcels&PK=" + md.getNo();
		}

		if (md.getHisFrmType() == BP.Sys.FrmType.FreeFrm) {
			return "url@FormDesigner.htm?FK_MapData=" + md.getNo();
		}

		return "url@../FoolFormDesigner/Designer.htm?IsFirst=1&FK_MapData=" + md.getNo();
	}

	public String LetLogin() {
		BP.Port.Emp emp = new BP.Port.Emp("admin");
		WebUser.SignInOfGener(emp);
		return "";
	}

	public final String CCFormDesignerSL_Init() {
		return BP.WF.Glo.getSilverlightDownloadUrl();
	}

	public final String GoToFrmDesigner_Init() {
		// 根据不同的表单类型转入不同的表单设计器上去.
		BP.Sys.MapData md = new BP.Sys.MapData(this.getFK_MapData());
		if (md.getHisFrmType() == BP.Sys.FrmType.FoolForm) {
			/* 傻瓜表单 */
			return "url@../FoolFormDesigner/Designer.htm?IsFirst=1&FK_MapData=" + this.getFK_MapData();
		}

		if (md.getHisFrmType() == BP.Sys.FrmType.FreeFrm) {
			/* 自由表单 */
			return "url@FormDesigner.htm?FK_MapData=" + this.getFK_MapData();
		}

		if (md.getHisFrmType() == BP.Sys.FrmType.VSTOForExcel) {
			/* 自由表单 */
			return "url@FormDesigner.htm?FK_MapData=" + this.getFK_MapData();

			// return
			// "url@../../Comm/En.htm?EnsName=BP.WF.Template.MapFrmExcels&PK=" +
			// this.FK_MapData;
			// return
			// "url@./FoolFormDesigner/Designer.htm?IsFirst=1&FK_MapData=" +
			// this.FK_MapData;
		}
		return "err@没有判断的表单转入类型" + md.getHisFrmType().toString();
	}

	public final String PublicNoNameCtrlCreate() {
		try {
			float x = Float.parseFloat(this.GetRequestVal("x"));
			float y = Float.parseFloat(this.GetRequestVal("y"));
			BP.Sys.CCFormAPI.CreatePublicNoNameCtrl(this.getFrmID(), this.GetRequestVal("CtrlType"),
					this.GetRequestVal("No"), this.GetRequestVal("Name"), x, y);
			return "true";
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}

	public String NewField() {
		try {
			BP.Sys.CCFormAPI.NewField(this.GetRequestVal("FrmID"), this.GetRequestVal("KeyOfEn"),
					this.GetRequestVal("Name"), Integer.parseInt(this.GetRequestVal("FieldType")),
					Float.parseFloat(this.GetRequestVal("x")), Float.parseFloat(this.GetRequestVal("y")), 1);
			return "true";
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}

	/**
	 * 获取自由表单所有元素
	 * 
	 * @return json data
	 */
	public final String CCForm_AllElements_ResponseJson() {
		try {
			MapData mapData = new MapData(this.getFK_MapData());

			// 获取表单元素
			String sqls = "SELECT * FROM Sys_MapAttr WHERE UIVisible=1 AND FK_MapData='" + this.getFK_MapData() + "';"
					+ System.getProperty("line.separator") + "SELECT * FROM Sys_FrmBtn WHERE FK_MapData='"
					+ this.getFK_MapData() + "';" + System.getProperty("line.separator")
					+ "SELECT * FROM Sys_FrmRB WHERE FK_MapData='" + this.getFK_MapData() + "';"
					+ System.getProperty("line.separator") + "SELECT * FROM Sys_FrmLab WHERE FK_MapData='"
					+ this.getFK_MapData() + "';" + "SELECT * FROM Sys_FrmLink WHERE FK_MapData='"
					+ this.getFK_MapData() + "';" + "SELECT * FROM Sys_FrmImg WHERE FK_MapData='" + this.getFK_MapData()
					+ "';" + "SELECT * FROM Sys_FrmImgAth WHERE FK_MapData='" + this.getFK_MapData() + "';"
					+ "SELECT * FROM Sys_FrmAttachment WHERE FK_MapData='" + this.getFK_MapData() + "';"
					+ "SELECT * FROM Sys_MapDtl WHERE FK_MapData='" + this.getFK_MapData() + "';"
					+ "SELECT * FROM Sys_FrmLine WHERE FK_MapData='" + this.getFK_MapData() + "';"
					+ "select '轨迹图' Name,'FlowChart' No,FrmTrackSta Sta,FrmTrack_X X,FrmTrack_Y Y,FrmTrack_H H,FrmTrack_W  W from WF_Node where nodeid="
					+ this.getFK_Node()
					+ " union select '审核组件' Name, 'FrmCheck' No,FWCSta Sta,FWC_X X,FWC_Y Y,FWC_H H, FWC_W W from WF_Node where nodeid="
					+ this.getFK_Node()
					+ " union select '子流程' Name,'SubFlowDtl' No,SFSta Sta,SF_X X,SF_Y Y,SF_H H, SF_W W from WF_Node  where nodeid="
					+ this.getFK_Node()
					+ " union select '子线程' Name, 'ThreadDtl' No,FrmThreadSta Sta,FrmThread_X X,FrmThread_Y Y,FrmThread_H H,FrmThread_W W from WF_Node where nodeid="
					+ this.getFK_Node()
					+ " union select '流转自定义' Name,'FrmTransferCustom' No,FTCSta Sta,FTC_X X,FTC_Y Y,FTC_H H,FTC_W  W FROM WF_Node  where nodeid="
					+ this.getFK_Node() + ";";
			;

			// String sqls = "SELECT * FROM Sys_MapAttr WHERE UIVisible=1 AND
			// FK_MapData='" + this.getFK_MapData() + "';" +
			// System.getProperty("line.separator") + "SELECT * FROM Sys_FrmBtn
			// WHERE FK_MapData='" + this.getFK_MapData() + "';" +
			// System.getProperty("line.separator") + "SELECT * FROM Sys_FrmRB
			// WHERE FK_MapData='" + this.getFK_MapData() + "';" +
			// System.getProperty("line.separator") + "SELECT * FROM Sys_FrmLab
			// WHERE FK_MapData='" + this.getFK_MapData() + "';" + "SELECT *
			// FROM Sys_FrmLink WHERE FK_MapData='" + this.getFK_MapData() +
			// "';" + "SELECT * FROM Sys_FrmImg WHERE FK_MapData='" +
			// this.getFK_MapData() + "';" + "SELECT * FROM Sys_FrmImgAth WHERE
			// FK_MapData='" + this.getFK_MapData() + "';" + "SELECT * FROM
			// Sys_FrmAttachment WHERE FK_MapData='" + this.getFK_MapData() +
			// "';" + "SELECT * FROM Sys_MapDtl WHERE FK_MapData='" +
			// this.getFK_MapData() + "';" + "SELECT * FROM Sys_FrmLine WHERE
			// FK_MapData='" + this.getFK_MapData() + "';" + "select '轨迹图'
			// Name,'FlowChart' No,FrmTrackSta Sta,FrmTrack_X X,FrmTrack_Y
			// Y,FrmTrack_H H,FrmTrack_W W from WF_Node where nodeid=" +
			// this.getFK_Node() + " union select '审核组件' Name, 'FrmCheck'
			// No,FWCSta Sta,FWC_X X,FWC_Y Y,FWC_H H, FWC_W W from WF_Node where
			// nodeid=" + this.getFK_Node() + " union select '子流程'
			// Name,'SubFlowDtl' No,SFSta Sta,SF_X X,SF_Y Y,SF_H H, SF_W W from
			// WF_Node where nodeid=" + this.getFK_Node() + " union select '子线程'
			// Name, 'ThreadDtl' No,FrmThreadSta Sta,FrmThread_X X,FrmThread_Y
			// Y,FrmThread_H H,FrmThread_W W from WF_Node where nodeid=" +
			// this.getFK_Node() + " union select '流转自定义'
			// Name,'FrmTransferCustom' No,FTCSta Sta,FTC_X X,FTC_Y Y,FTC_H
			// H,FTC_W W from WF_Node where nodeid=" + this.getFK_Node() + ";";

			DataSet ds = DBAccess.RunSQLReturnDataSet(sqls);

			//// 用列名称进行比对 重新设置
			String mapAttrCols, frmBtnCols, frmRbCols, frmLabCols, sys_FrmLinkCols, sys_FrmImgCols, sys_FrmImgAthCols,
					sys_FrmAttachmentCols, sys_MapDtlCols, sys_FrmLineCols, figureComCols;
			mapAttrCols = "MyPK,FK_MapData,KeyOfEn,Name,DefVal,UIContralType,MyDataType,LGType,UIWidth,UIHeight,UIBindKey,UIRefKey,UIRefKeyText,UIVisible,UIIsEnable,UIIsLine,UIIsInput,Idx,IsSigan,X,Y,GUID,Tag,EditType,AtPara,ExtDefVal,ExtDefValText,MinLen,MaxLen,ExtRows,IsRichText,IsSupperText,Tip,ColSpan,ColSpanText,GroupID,GroupIDText";
			frmBtnCols = "MyPK,FK_MapData,Text,X,Y,IsView,IsEnable,BtnType,UAC,UACContext,EventType,EventContext,MsgOK,MsgErr,GUID,GroupID";
			frmRbCols = "MyPK,FK_MapData,KeyOfEn,EnumKey,Lab,IntKey,X,Y,GUID,Script,FieldsCfg,Tip";
			frmLabCols = "MyPK,FK_MapData,Text,X,Y,FontSize,FontColor,FontName,FontStyle,FontWeight,IsBold,IsItalic,GUID";
			sys_FrmLinkCols = "MyPK,FK_MapData,Text,URL,Target,X,Y,FontSize,FontColor,FontName,FontStyle,IsBold,IsItalic,GUID";
			sys_FrmImgCols = "MyPK,FK_MapData,ImgAppType,X,Y,H,W,ImgURL,ImgPath,LinkURL,LinkTarget,GUID,Tag0,SrcType,IsEdit,Name,EnPK,ImgSrcType";
			sys_FrmImgAthCols = "MyPK,FK_MapData,CtrlID,X,Y,H,W,IsEdit,GUID,Name,IsRequired";
			sys_FrmAttachmentCols = "MyPK,FK_MapData,NoOfObj,FK_Node,Name,Exts,SaveTo,Sort,X,Y,W,H,IsUpload,IsDelete,IsDownload,IsOrder,IsAutoSize,IsNote,IsShowTitle,UploadType,CtrlWay,AthUploadWay,AtPara,RowIdx,GroupID,GUID,DeleteWay,IsWoEnableWF,IsWoEnableSave,IsWoEnableReadonly,IsWoEnableRevise,IsWoEnableViewKeepMark,IsWoEnablePrint,IsWoEnableOver,IsWoEnableSeal,IsWoEnableTemplete,IsWoEnableCheck,IsWoEnableInsertFlow,IsWoEnableInsertFengXian,IsWoEnableMarks,IsWoEnableDown,IsRowLock,IsToHeLiuHZ,IsHeLiuHuiZong,IsTurn2Html,AthRunModel";
			sys_MapDtlCols = "No,Name,FK_MapData,PTable,GroupField,Model,ImpFixTreeSql,ImpFixDataSql,RowIdx,GroupID,RowsOfList,IsEnableGroupField,IsShowSum,IsShowIdx,IsCopyNDData,IsHLDtl,IsReadonly,IsShowTitle,IsView,IsInsert,IsDelete,IsUpdate,IsEnablePass,IsEnableAthM,IsEnableM2M,IsEnableM2MM,WhenOverSize,DtlOpenType,DtlShowModel,X,Y,H,W,FrmW,FrmH,MTR,GUID,FK_Node,AtPara,IsExp,ImpModel,ImpSQLSearch,ImpSQLInit,ImpSQLFull,FilterSQLExp,SubThreadWorker,SubThreadWorkerText";
			sys_FrmLineCols = " MyPK,FK_MapData,X,Y,X1,Y1,X2,Y2,BorderWidth,BorderColor,GUID";
			figureComCols = "Name,No,Sta,X,Y,H,W";

			String[] tableCols = new String[11];
			ds.Tables.get(0).TableName = "Sys_MapAttr";
			tableCols[0] = mapAttrCols;

			ds.Tables.get(1).TableName = "Sys_FrmBtn";
			tableCols[1] = frmBtnCols;
			ds.Tables.get(2).TableName = "Sys_FrmRB";
			tableCols[2] = frmRbCols;
			ds.Tables.get(3).TableName = "Sys_FrmLab";
			tableCols[3] = frmLabCols;
			ds.Tables.get(4).TableName = "Sys_FrmLink";
			tableCols[4] = sys_FrmLineCols;
			ds.Tables.get(5).TableName = "Sys_FrmImg";
			tableCols[5] = sys_FrmImgCols;
			ds.Tables.get(6).TableName = "Sys_FrmImgAth";
			tableCols[6] = sys_FrmImgAthCols;
			ds.Tables.get(7).TableName = "Sys_FrmAttachment";
			tableCols[7] = sys_FrmAttachmentCols;
			ds.Tables.get(8).TableName = "Sys_MapDtl";
			tableCols[8] = sys_MapDtlCols;
			ds.Tables.get(9).TableName = "Sys_FrmLine";
			tableCols[9] = sys_FrmLineCols;
			ds.Tables.get(10).TableName = "FigureCom";
			tableCols[10] = figureComCols;

			/// #region 解决oracle大小写问题.
			if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
				java.util.HashMap<String, String> dicCols = new java.util.HashMap<String, String>();
				// 将所有的列名进行转换（适应ORACLE） ORACLE 不区分大小写，都是大写
				for (int i = 0; i < ds.Tables.size(); i++) {
					dicCols.clear();
					// dicCols = (new
					// List<string>(tableCols[i].Split(','))).ToDictionary(m =>
					// m.ToString().Trim().ToLower(), m => m.Trim());
					for (int m = 0; m < tableCols[i].split(",").length; m++) {
						dicCols.put(tableCols[i].split(",")[m].toLowerCase(), tableCols[i].split(",")[m]);
					}
					DataTable dt = ds.Tables.get(i);
					for (DataColumn dc : dt.Columns) {
						if (dicCols.containsKey(dc.ColumnName.toLowerCase())) {
							dc.ColumnName = dicCols.get(dc.ColumnName.toLowerCase());
						}
					}
				}
			}
			/// #endregion 解决oracle大小写问题.

			return // BP.Tools.Json.ToJson(ds);
			BP.Tools.Json.DataSetToJson(ds, false);
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}

	/**
	 * 保存表单
	 * 
	 * @return
	 */
	public final String SaveForm() {
		BP.Sys.CCFormAPI.SaveFrm(this.getFK_MapData(), this.GetRequestVal("diagram"));

		// 一直没有找到设置3列，自动回到四列的情况.
		DBAccess.RunSQL("UPDATE Sys_MapAttr SET ColSpan=3 WHERE  UIHeight<=23 AND ColSpan=4");
		return "保存成功.";
	}

	/** 
	 表单重置
	 
	 @return 
	*/
	public final String ResetFrm_Init()
	{
		MapData md = new MapData(this.getFK_MapData());
		md.ResetMaxMinXY();
		md.setFormJson("");
		md.Update();

		return "重置成功.";
	}
	
	public final String Tables_Init() {
		BP.Sys.SFTables tabs = new BP.Sys.SFTables();
		tabs.RetrieveAll();
		DataTable dt = tabs.ToDataTableField();
		dt.Columns.Add("RefNum", Integer.class);

		for (DataRow dr : dt.Rows) {
			// 求引用数量.
			int refNum = BP.DA.DBAccess.RunSQLReturnValInt(
					"SELECT COUNT(KeyOfEn) FROM Sys_MapAttr WHERE UIBindKey='" + dr.getValue("No") + "'", 0);
			dr.setValue("RefNum", refNum);
		}
		return BP.Tools.Json.ToJson(dt);
	}

	public final String Tables_Delete() {
		try {
			BP.Sys.SFTable tab = new BP.Sys.SFTable();
			tab.setNo(this.getNo());
			tab.Delete();
			return "删除成功.";
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}

	public final String TableRef_Init() {
		BP.Sys.MapAttrs mapAttrs = new BP.Sys.MapAttrs();
		mapAttrs.RetrieveByAttr(BP.Sys.MapAttrAttr.UIBindKey, this.getFK_SFTable());

		DataTable dt = mapAttrs.ToDataTableField();
		return BP.Tools.Json.ToJson(dt);
	}

	public final String Home_Init() {
		String no = this.GetRequestVal("No");

		MapData md = new MapData(no);

		// 基础信息.
		Hashtable ht = new Hashtable();
		ht.put("No", no);
		ht.put("Name", md.getName());
		ht.put("PTable", md.getPTable());
		ht.put("FrmTypeT", md.getHisFrmTypeText());
		ht.put("FrmTreeName", md.getFK_FormTreeText());

		// 统计信息.
		if (DBAccess.IsExitsObject(md.getPTable()) == true) {
			ht.put("SumDataNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM " + md.getPTable())); // 数据量.
		} else {
			ht.put("SumDataNum", 0); // 数据量.
		}

		ht.put("SumAttrNum",
				DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM Sys_MapAttr WHERE FK_MapData='" + no + "'")); // 字段数量.
		ht.put("SumAttrFK", DBAccess
				.RunSQLReturnValInt("SELECT COUNT(*) FROM Sys_MapAttr WHERE FK_MapData='" + no + "' AND LGType=2 ")); // 外键.
		ht.put("SumAttrEnum", DBAccess
				.RunSQLReturnValInt("SELECT COUNT(*) FROM Sys_MapAttr WHERE FK_MapData='" + no + "' AND LGType=1 ")); // 外键.

		ht.put("MapFrmFrees", "../../Comm/En.htm?EnsName=BP.WF.Template.MapFrmFrees&PK=" + no); // 自由表单属性.
		ht.put("MapFrmFools", "../../Comm/En.htm?EnsName=BP.WF.Template.MapFrmFools&PK=" + no); // 傻瓜表单属性.
		ht.put("MapFrmExcels", "../../Comm/En.htm?EnsName=BP.WF.Template.MapFrmExcels&PK=" + no); // Excel表单属性.
		ht.put("MapDataURLs", "../../Comm/En.htm?EnsName=BP.WF.Template.MapDataURLs&PK=" + no); // 嵌入式表单属性.

		return BP.DA.DataType.ToJsonEntityModel(ht);
	}

	/**
	 * 初始化字段列表.
	 * 
	 * @return
	 */
	public final String FiledsList_Init() {
		MapAttrs attrs = new MapAttrs();
		attrs.Retrieve(MapAttrAttr.FK_MapData, this.getFK_MapData());
		for (MapAttr item : attrs.ToJavaList()) {
			if (item.getLGType() == FieldTypeS.Enum) {
				SysEnumMain se = new SysEnumMain(item.getUIBindKey());
				item.setUIRefKey(se.getCfgVal());
				continue;
			}

			if (item.getLGType() == FieldTypeS.FK) {
				item.setUIRefKey(item.getUIBindKey());
				continue;
			}

			item.setUIRefKey("无");
		}
		return attrs.ToJson();
	}

	/**
	 * 删除字段
	 * 
	 * @return
	 */
	public final String FiledsList_Delete() {
		MapAttr attr = new MapAttr(this.getMyPK());
		if (attr.Delete() == 1) {
			return "删除成功！";
		}

		return "err@删除成功！";
	}

	/**
	 * 增加
	 * 
	 * @return
	 */
	public final String DtlOpt_Add() {
		MapDtl dtl = new MapDtl(this.getFK_MapDtl());
		String pks = this.GetRequestVal("PKs");

		String[] strs = pks.split("[,]", -1);
		int i = 0;
		for (String str : strs) {
			if (str.equals("CheckAll") || str == null || str.equals("")) {
				continue;
			}

			GEDtl gedtl = new BP.Sys.GEDtl(this.getFK_MapDtl());
			String sql = dtl.getImpSQLFull();
			sql = sql.replace("@Key", str);

			DataTable dt = DBAccess.RunSQLReturnTable(sql);

			if (dt.Rows.size() == 0) {
				return "err@导入数据失败:" + sql;
			}

			gedtl.Copy(dt.Rows.get(0));
			gedtl.FK_MapDtl = this.GetRequestVal("RefPKVal");
			gedtl.InsertAsNew();
			i++;
		}

		return "成功的导入了[" + i + "]行数据...";
	}

	/**
	 * 初始化数据
	 * 
	 * @return
	 */
	public final String DtlOpt_Init() {
		MapDtl dtl = new MapDtl(this.getFK_MapDtl());
		
		DataSet ds = new DataSet();
		DataTable dt = DBAccess.RunSQLReturnTable(dtl.getImpSQLInit());

		return BP.Tools.Json.ToJson(dt);
	}
	
	/** 
	 执行查询.
	 
	 @return
	 */

	public final String DtlOpt_Search() {
		MapDtl dtl = new MapDtl(this.getFK_MapDtl());

		String sql = dtl.getImpSQLSearch();
		sql = sql.replace("@Key", this.GetRequestVal("Key"));
		sql = sql.replace("@WebUser.No", WebUser.getNo());
		sql = sql.replace("@WebUser.Name", WebUser.getName());
		sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());

		DataSet ds = new DataSet();
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		return BP.Tools.Json.ToJson(dt);
	}
	/// #endregion 从表的选项.

}
