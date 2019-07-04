package BP.WF.HttpHandler;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
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
import BP.DA.DataType;
import BP.DA.Log;
import BP.En.EditType;
import BP.En.FieldTypeS;
import BP.En.UIContralType;
import BP.Sys.FrmAttachments;
import BP.Sys.FrmBtns;
import BP.Sys.FrmImgAths;
import BP.Sys.FrmImgs;
import BP.Sys.FrmLabs;
import BP.Sys.FrmLines;
import BP.Sys.FrmLinks;
import BP.Sys.FrmRBs;
import BP.Sys.GEDtl;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.MapDataAttr;
import BP.Sys.MapDtl;
import BP.Sys.MapDtls;
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
	public String FormDesigner_InitMapData() throws Exception {
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
	 * @throws Exception 
	 */
	public final String FrmEnumeration_NewEnumField() throws Exception {
		
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
	 * @throws Exception 
	 */
	public final String Hiddenfielddata() throws Exception {
		return BP.Sys.CCFormAPI.DB_Hiddenfielddata(this.getFK_MapData());
	}

	public final String HiddenFieldDelete() {
		String records = GetRequestVal("records");
		String FK_MapData = GetRequestVal("FK_MapData");
		MapAttr mapAttrs = new MapAttr();
		int result = mapAttrs.Delete(MapAttrAttr.KeyOfEn, records, MapAttrAttr.FK_MapData, FK_MapData);
		return String.valueOf(result);
	}

	public final String CcformElements() throws Exception {
		return CCForm_AllElements_ResponseJson();
	}

	/**
	 * 创建隐藏字段.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String NewHidF() throws Exception {
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

	public final String NewFrmGuide_GenerPinYin() throws Exception {
		String isQuanPin = this.GetRequestVal("IsQuanPin");
		String name = this.GetRequestVal("TB_Name");
		name = URLDecoder.decode(name, "UTF-8");

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
	 * @throws Exception 
*/
	public final String NewFrmGuide_Init() throws Exception
	{
		DataSet ds = new DataSet();

		SFDBSrc src = new SFDBSrc("local");
		ds.Tables.add(src.ToDataTableField("SFDBSrc"));

		DataTable tables = src.GetTables(true);
		tables.TableName = "Tables";
		ds.Tables.add(tables);

		return BP.Tools.Json.ToJson(ds);

	}
	
	public final String NewFrmGuide_Create() throws Exception {
		MapData md = new MapData();
		md.setName(this.GetRequestVal("TB_Name"));
		md.setNo(this.GetRequestVal("TB_No"));
		
		md.setHisFrmTypeInt(this.GetRequestValInt("DDL_FrmType"));
		
		md.setPTable(this.GetRequestVal("TB_PTable"));
		//表单的物理表.
        if(md.getHisFrmType() == BP.Sys.FrmType.Url ||  md.getHisFrmType() == BP.Sys.FrmType.Entity)
            md.setPTable(this.GetRequestVal("TB_PTable"));
        else
            md.setPTable(DataType.ParseStringForNo(this.GetRequestVal("TB_PTable"), 100));
		
		md.SetValByKey(MapDataAttr.PTableModel, this.GetRequestVal("DDL_PTableModel"));
		
		String sort = this.GetRequestVal("FK_FrmSort");
        if (DataType.IsNullOrEmpty(sort) == true)
            sort = this.GetRequestVal("DDL_FrmTree");

		md.setFK_FrmSort(sort);
		md.setFK_FormTree(sort);
		
		md.setAppType("0"); // 独立表单
		md.setDBSrc(this.GetRequestVal("DDL_DBSrc"));
		if (md.getIsExits() == true) {
			return "err@表单ID:" + md.getNo() + "已经存在.";
		}

		switch (md.getHisFrmType()) {
		// 自由，傻瓜，SL表单不做判断
		case FreeFrm:
		case FoolForm:
			break;
		case Url:
		case Entity:
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
			return "url@../../Comm/En.htm?EnName=BP.WF.Template.MapFrmExcel&PK=" + md.getNo();
		}

		if (md.getHisFrmType() == BP.Sys.FrmType.FreeFrm) {
			return "url@FormDesigner.htm?FK_MapData=" + md.getNo();
		}
		
		if (md.getHisFrmType() == BP.Sys.FrmType.Entity)
            return "url@../../Comm/Ens.htm?EnsName=" + md.getPTable();

		return "url@../FoolFormDesigner/Designer.htm?IsFirst=1&FK_MapData=" + md.getNo();
	}

	public String LetLogin() throws Exception {
		BP.Port.Emp emp = new BP.Port.Emp("admin");
		WebUser.SignInOfGener(emp);
		return "";
	}

	public final String CCFormDesignerSL_Init() {
		return BP.WF.Glo.getSilverlightDownloadUrl();
	}

	public final String GoToFrmDesigner_Init() throws Exception {
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
		
		   if (md.getHisFrmType() == BP.Sys.FrmType.Url)
           {
               /* 自由表单 */
               return "url@../../Comm/EnOnly.htm?EnName=BP.WF.Template.MapDataURL&No=" + this.getFK_MapData();
           }
		   if (md.getHisFrmType() == BP.Sys.FrmType.Entity)
			   return "url@../../Comm/Ens.htm?EnsName=" + md.getPTable();
		   
		return "err@没有判断的表单转入类型" + md.getHisFrmType().toString();
	}

	public final String PublicNoNameCtrlCreate() throws Exception {
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
	
	/**
	 * 创建图片
	 * @return
	 */
	 public String NewImage()
     {

         try
         {
             BP.Sys.CCFormAPI.NewImage(this.GetRequestVal("FrmID"),
                 this.GetRequestVal("KeyOfEn"), this.GetRequestVal("Name"),
                
                 Float.parseFloat(this.GetRequestVal("x")),
                 Float.parseFloat(this.GetRequestVal("y"))
                );
             return "true";
         }
         catch (Exception ex)
         {
             return "err@" + ex.getMessage();
         }


     }
	
	/**
	 * 创建字段
	 * @return
	 * @throws Exception
	 */
	public String NewField() throws Exception {
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
	 * @throws Exception 
	 */
	public final String CCForm_AllElements_ResponseJson() throws Exception {
		
		try {
			
			  DataSet ds = new DataSet();

              MapData mapData = new MapData(this.getFK_MapData());

              //属性.
              MapAttrs attrs = new MapAttrs(this.getFK_MapData());
              attrs.Retrieve(MapAttrAttr.FK_MapData, this.getFK_MapData(), MapAttrAttr.UIVisible, 1);
              ds.Tables.add(attrs.ToDataTableField("Sys_MapAttr"));

              FrmBtns btns = new FrmBtns(this.getFK_MapData());
              ds.Tables.add(btns.ToDataTableField("Sys_FrmBtn"));

              FrmRBs rbs = new FrmRBs(this.getFK_MapData());
              ds.Tables.add(rbs.ToDataTableField("Sys_FrmRB"));

              FrmLabs labs = new FrmLabs(this.getFK_MapData());
              ds.Tables.add(labs.ToDataTableField("Sys_FrmLab"));

              FrmLinks links = new FrmLinks(this.getFK_MapData());
              ds.Tables.add(links.ToDataTableField("Sys_FrmLink"));

              FrmImgs imgs = new FrmImgs(this.getFK_MapData());
              ds.Tables.add(imgs.ToDataTableField("Sys_FrmImg"));

              FrmImgAths imgAths = new FrmImgAths(this.getFK_MapData());
              ds.Tables.add(imgAths.ToDataTableField("Sys_FrmImgAth"));

              FrmAttachments aths = new FrmAttachments(this.getFK_MapData());
              ds.Tables.add(aths.ToDataTableField("Sys_FrmAttachment"));

              MapDtls dtls = new MapDtls(this.getFK_MapData());
              ds.Tables.add(dtls.ToDataTableField("Sys_MapDtl"));

              FrmLines lines = new FrmLines(this.getFK_MapData());
              ds.Tables.add(lines.ToDataTableField("Sys_FrmLine"));
              
              BP.Sys.FrmUI.MapFrameExts mapFrameExts = new BP.Sys.FrmUI.MapFrameExts(this.getFK_MapData());
              ds.Tables.add(mapFrameExts.ToDataTableField("Sys_MapFrame"));

              //组织节点组件信息.
              if (this.getFK_Node() > 100)
              {
              String sql = "";
              sql += "select '轨迹图' Name,'FlowChart' No,FrmTrackSta Sta,FrmTrack_X X,FrmTrack_Y Y,FrmTrack_H H,FrmTrack_W  W from WF_Node WHERE nodeid=" + this.getFK_Node();
              sql += " union select '审核组件' Name, 'FrmCheck' No,FWCSta Sta,FWC_X X,FWC_Y Y,FWC_H H, FWC_W W from WF_Node WHERE nodeid=" + this.getFK_Node();
              sql += " union select '子流程' Name,'SubFlowDtl' No,SFSta Sta,SF_X X,SF_Y Y,SF_H H, SF_W W from WF_Node  WHERE nodeid=" + this.getFK_Node();
              sql += " union select '子线程' Name, 'ThreadDtl' No,FrmThreadSta Sta,FrmThread_X X,FrmThread_Y Y,FrmThread_H H,FrmThread_W W from WF_Node WHERE nodeid=" + this.getFK_Node();
              sql += " union select '流转自定义' Name,'FrmTransferCustom' No,FTCSta Sta,FTC_X X,FTC_Y Y,FTC_H H,FTC_W  W FROM WF_Node WHERE nodeid=" + this.getFK_Node() ;

              DataTable dt = DBAccess.RunSQLReturnTable(sql);
              dt.TableName = "FigureCom";
              if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
              {
                //  figureComCols = "Name,No,Sta,X,Y,H,W";
                  dt.Columns.get(0).setColumnName("Name");
                  dt.Columns.get(1).setColumnName("No");
                  dt.Columns.get(2).setColumnName("Sta");
                  dt.Columns.get(3).setColumnName("X");
                  dt.Columns.get(4).setColumnName("Y");
                  dt.Columns.get(5).setColumnName("H");
                  dt.Columns.get(6).setColumnName("W");                
                   
              }
              
              ds.Tables.add( dt);
              }
              return BP.Tools.Json.ToJson(ds);
			
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}

	/**
	 * 保存表单
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String SaveForm() throws Exception {
		
		  //清缓存
        BP.Sys.CCFormAPI.AfterFrmEditAction(this.getFK_MapData());
        
		BP.Sys.CCFormAPI.SaveFrm(this.getFK_MapData(), this.GetRequestVal("diagram"));

		// 一直没有找到设置3列，自动回到四列的情况.
		DBAccess.RunSQL("UPDATE Sys_MapAttr SET ColSpan=3 WHERE  UIHeight<=23 AND ColSpan=4");
		return "保存成功.";
	}

	/** 
	 表单重置
	 
	 @return 
	 * @throws Exception 
	*/
	public final String ResetFrm_Init() throws Exception
	{
		MapData md = new MapData(this.getFK_MapData());
		md.ResetMaxMinXY();
		md.Update();

		return "重置成功.";
	}
	
	public final String Tables_Init() throws Exception {
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

	public final String Tables_Delete() throws Exception {
		try {
			BP.Sys.SFTable tab = new BP.Sys.SFTable();
			tab.setNo(this.getNo());
			tab.Delete();
			return "删除成功.";
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}

	public final String TableRef_Init() throws Exception {
		BP.Sys.MapAttrs mapAttrs = new BP.Sys.MapAttrs();
		mapAttrs.RetrieveByAttr(BP.Sys.MapAttrAttr.UIBindKey, this.getFK_SFTable());

		DataTable dt = mapAttrs.ToDataTableField();
		return BP.Tools.Json.ToJson(dt);
	}

	public final String Home_Init() throws Exception {
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

		ht.put("MapFrmFrees", "../../Comm/En.htm?EnName=BP.WF.Template.MapFrmFree&PKVal=" + no); // 自由表单属性.
		ht.put("MapFrmFools", "../../Comm/En.htm?EnName=BP.WF.Template.MapFrmFool&PKVal=" + no); // 傻瓜表单属性.
		ht.put("MapFrmExcels", "../../Comm/En.htm?EnName=BP.WF.Template.MapFrmExcel&PKVal=" + no); // Excel表单属性.
		ht.put("MapDataURLs", "../../Comm/En.htm?EnName=BP.WF.Template.MapDataURL&PKVal=" + no); // 嵌入式表单属性.

		return BP.DA.DataType.ToJsonEntityModel(ht);
	}

	/**
	 * 初始化字段列表.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String FiledsList_Init() throws Exception {
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
	 * @throws Exception 
	 */
	public final String FiledsList_Delete() throws Exception {
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
	 * @throws Exception 
	 */
	public final String DtlOpt_Add() throws Exception {
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
	 * @throws Exception 
	 */
	public final String DtlOpt_Init() throws Exception {
		MapDtl dtl = new MapDtl(this.getFK_MapDtl());
		
		DataSet ds = new DataSet();
		DataTable dt = DBAccess.RunSQLReturnTable(dtl.getImpSQLInit());

		return BP.Tools.Json.ToJson(dt);
	}
	
	/** 
	 执行查询.
	 
	 @return
	 * @throws Exception 
	 */

	public final String DtlOpt_Search() throws Exception {
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
