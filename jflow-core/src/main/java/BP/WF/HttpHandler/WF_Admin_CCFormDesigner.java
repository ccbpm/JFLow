package BP.WF.HttpHandler;

import BP.WF.*;
import BP.Web.*;
import BP.Sys.*;
import BP.DA.*;
import BP.En.*;
import BP.WF.Template.*;
import BP.Frm.*;
import BP.WF.*;

public class WF_Admin_CCFormDesigner extends BP.WF.HttpHandler.DirectoryPageBase
{

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 执行父类的重写方法.
	/** 
	 构造函数
	*/
	public WF_Admin_CCFormDesigner()
	{
	}

	/** 
	 创建枚举类型字段
	 
	 @return 
	*/
	public final String FrmEnumeration_NewEnumField()
	{
		UIContralType ctrl = UIContralType.RadioBtn;
		String ctrlDoType = GetRequestVal("ctrlDoType");
		if (ctrlDoType.equals("DDL"))
		{
			ctrl = UIContralType.DDL;
		}
		else
		{
			ctrl = UIContralType.RadioBtn;
		}

		String fk_mapdata = this.GetRequestVal("FK_MapData");
		String keyOfEn = this.GetRequestVal("KeyOfEn");
		String fieldDesc = this.GetRequestVal("Name");
		String enumKeyOfBind = this.GetRequestVal("UIBindKey"); //要绑定的enumKey.
		float x = this.GetRequestValFloat("x");
		float y = this.GetRequestValFloat("y");

		BP.Sys.CCFormAPI.NewEnumField(fk_mapdata, keyOfEn, fieldDesc, enumKeyOfBind, ctrl, x, y);
		return "绑定成功.";
	}
	/** 
	 创建外键字段.
	 
	 @return 
	*/
	public final String NewSFTableField()
	{
		try
		{
			String fk_mapdata = this.GetRequestVal("FK_MapData");
			String keyOfEn = this.GetRequestVal("KeyOfEn");
			String fieldDesc = this.GetRequestVal("Name");
			String sftable = this.GetRequestVal("UIBindKey");
			float x = Float.parseFloat(this.GetRequestVal("x"));
			float y = Float.parseFloat(this.GetRequestVal("y"));

			//调用接口,执行保存.
			BP.Sys.CCFormAPI.SaveFieldSFTable(fk_mapdata, keyOfEn, fieldDesc, sftable, x, y);
			return "设置成功";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}

	/** 
	 转换拼音
	 
	 @return 
	*/
	public final String ParseStringToPinyin()
	{
		String name = GetRequestVal("name");
		String flag = GetRequestVal("flag");
		//暂时没发现此方法在哪里有调用，edited by liuxc,2017-9-25
		return BP.Sys.CCFormAPI.ParseStringToPinyinField(name, Equals(flag, "true"), true, 20);
	}

	/** 
	 创建隐藏字段.
	 
	 @return 
	*/
	public final String NewHidF()
	{
		MapAttr mdHid = new MapAttr();
		mdHid.setMyPK( this.getFK_MapData() + "_" + this.getKeyOfEn();
		mdHid.FK_MapData = this.getFK_MapData();
		mdHid.KeyOfEn = this.getKeyOfEn();
		mdHid.Name = this.getName();
		mdHid.MyDataType = Integer.parseInt(this.GetRequestVal("FieldType"));
		mdHid.HisEditType = EditType.Edit;
		mdHid.MaxLen = 100;
		mdHid.MinLen = 0;
		mdHid.LGType = FieldTypeS.Normal;
		mdHid.UIVisible = false;
		mdHid.UIIsEnable = false;
		mdHid.Insert();

		return "创建成功..";
	}
	/** 
	 默认执行的方法
	 
	 @return 
	*/
	@Override
	protected String DoDefaultMethod()
	{
		String sql = "";
		//返回值
		try
		{
			switch (this.getDoType())
			{
				default:
					throw new RuntimeException("没有判断的执行标记:" + this.getDoType());
			}
		}
		catch (RuntimeException ex)
		{
			return "err@" + this.toString() + " msg:" + ex.getMessage();
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 执行父类的重写方法.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 创建表单.
	public final String NewFrmGuide_GenerPinYin()
	{
		String isQuanPin = this.GetRequestVal("IsQuanPin");
		String name = this.GetRequestVal("TB_Name");

		//表单No长度最大100，因有前缀CCFrm_，因此此处设置最大94，added by liuxc,2017-9-25
		String str = BP.Sys.CCFormAPI.ParseStringToPinyinField(name, Equals(isQuanPin, "1"), true, 94);

		MapData md = new MapData();
		md.No = str;
		if (md.RetrieveFromDBSources() == 0)
		{
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
	public final String NewFrmGuide_Create()
	{
		MapData md = new MapData();
		md.Name = this.GetRequestVal("TB_Name");
		md.No = DataType.ParseStringForNo(this.GetRequestVal("TB_No"), 100);

		md.HisFrmTypeInt = this.GetRequestValInt("DDL_FrmType");

		//表单的物理表.
		if (md.HisFrmType == BP.Sys.FrmType.Url || md.HisFrmType == BP.Sys.FrmType.Entity)
		{
			md.PTable = this.GetRequestVal("TB_PTable");
		}
		else
		{
			md.PTable = DataType.ParseStringForNo(this.GetRequestVal("TB_PTable"), 100);
		}

		//数据表模式。 @周朋 需要翻译.
		md.PTableModel = this.GetRequestValInt("DDL_PTableModel");

		//@李国文 需要对比翻译.
		String sort = this.GetRequestVal("FK_FrmSort");
		if (DataType.IsNullOrEmpty(sort) == true)
		{
			sort = this.GetRequestVal("DDL_FrmTree");
		}

		md.FK_FrmSort = sort;
		md.FK_FormTree = sort;

		md.AppType = "0"; //独立表单
		md.DBSrc = this.GetRequestVal("DDL_DBSrc");
		if (md.IsExits == true)
		{
			return "err@表单ID:" + md.No + "已经存在.";
		}

		switch (md.HisFrmType)
		{
			//自由，傻瓜，SL表单不做判断
			case BP.Sys.FrmType.FreeFrm:
			case BP.Sys.FrmType.FoolForm:
				break;
			case BP.Sys.FrmType.Url:
			case BP.Sys.FrmType.Entity:
				md.Url = md.PTable;
				break;
			//如果是以下情况，导入模式
			case BP.Sys.FrmType.WordFrm:
			case BP.Sys.FrmType.ExcelFrm:
				break;
			default:
				throw new RuntimeException("未知表单类型.");
		}
		md.Insert();

		//增加上OID字段.
		BP.Sys.CCFormAPI.RepareCCForm(md.No);

		BP.Frm.EntityType entityType = EntityType.forValue(this.GetRequestValInt("EntityType"));

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 如果是单据.
		if (entityType == EntityType.FrmBill)
		{
			BP.Frm.FrmBill bill = new FrmBill(md.No);
			bill.setEntityType(EntityType.FrmBill);
			bill.setBillNoFormat("ccbpm{yyyy}-{MM}-{dd}-{LSH4}");

			//设置默认的查询条件.
			bill.SetPara("IsSearchKey", 1);
			bill.SetPara("DTSearchWay", 0);

			bill.Update();
			bill.CheckEnityTypeAttrsFor_Bill();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 如果是单据.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 如果是实体 EnityNoName .
		if (entityType == EntityType.FrmDict)
		{
			BP.Frm.FrmDict entityDict = new FrmDict(md.No);
			entityDict.setBillNoFormat("3"); //编码格式.001,002,003.

			entityDict.setBtnNewModel(0);

			//设置默认的查询条件.
			entityDict.SetPara("IsSearchKey", 1);
			entityDict.SetPara("DTSearchWay", 0);

			entityDict.setEntityType(EntityType.FrmDict);

			entityDict.Update();
			entityDict.CheckEnityTypeAttrsFor_EntityNoName();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 如果是实体 EnityNoName .

		//创建表与字段.
		GEEntity en = new GEEntity(md.No);
		en.CheckPhysicsTable();

		if (md.HisFrmType == BP.Sys.FrmType.WordFrm || md.HisFrmType == BP.Sys.FrmType.ExcelFrm)
		{
			/*把表单模版存储到数据库里 */
			return "url@../../Comm/RefFunc/En.htm?EnName=BP.WF.Template.MapFrmExcel&PKVal=" + md.No;
		}

		if (md.HisFrmType == BP.Sys.FrmType.Entity)
		{
			return "url@../../Comm/Ens.htm?EnsName=" + md.PTable;
		}

		if (md.HisFrmType == BP.Sys.FrmType.FreeFrm)
		{
			return "url@FormDesigner.htm?FK_MapData=" + md.No;
		}


		return "url@../FoolFormDesigner/Designer.htm?IsFirst=1&FK_MapData=" + md.No;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 创建表单.

	public final String LetLogin()
	{
		BP.Port.Emp emp = new BP.Port.Emp("admin");
		WebUser.SignInOfGener(emp);
		return "登录成功.";
	}

	public final String GoToFrmDesigner_Init()
	{
		//根据不同的表单类型转入不同的表单设计器上去.
		BP.Sys.MapData md = new BP.Sys.MapData(this.getFK_MapData());
		if (md.HisFrmType == BP.Sys.FrmType.FoolForm)
		{
			/* 傻瓜表单 需要翻译. */
			return "url@../FoolFormDesigner/Designer.htm?IsFirst=1&FK_MapData=" + this.getFK_MapData();
		}

		if (md.HisFrmType == BP.Sys.FrmType.FreeFrm)
		{
			/* 自由表单 */
			return "url@FormDesigner.htm?FK_MapData=" + this.getFK_MapData() + "&IsFirst=1";
		}

		if (md.HisFrmType == BP.Sys.FrmType.VSTOForExcel)
		{
			/* 自由表单 */
			return "url@FormDesigner.htm?FK_MapData=" + this.getFK_MapData();
		}

		if (md.HisFrmType == BP.Sys.FrmType.Url)
		{
			/* 自由表单 */
			return "url@../../Comm/RefFunc/EnOnly.htm?EnName=BP.WF.Template.MapDataURL&No=" + this.getFK_MapData();
		}

		if (md.HisFrmType == BP.Sys.FrmType.Entity)
		{
			return "url@../../Comm/Ens.htm?EnsName=" + md.PTable;
		}

		return "err@没有判断的表单转入类型" + md.HisFrmType.toString();
	}

	public final String PublicNoNameCtrlCreate()
	{
		try
		{
			float x = Float.parseFloat(this.GetRequestVal("x"));
			float y = Float.parseFloat(this.GetRequestVal("y"));
			BP.Sys.CCFormAPI.CreatePublicNoNameCtrl(this.getFrmID(), this.GetRequestVal("CtrlType"), this.GetRequestVal("No"), this.GetRequestVal("Name"), x, y);
			return "true";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	public final String NewImage()
	{

		try
		{
			BP.Sys.CCFormAPI.NewImage(this.GetRequestVal("FrmID"), this.GetRequestVal("KeyOfEn"), this.GetRequestVal("Name"), Float.parseFloat(this.GetRequestVal("x")), Float.parseFloat(this.GetRequestVal("y")));
			return "true";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}


	}
	public final String NewField()
	{
		try
		{
			BP.Sys.CCFormAPI.NewField(this.GetRequestVal("FrmID"), this.GetRequestVal("KeyOfEn"), this.GetRequestVal("Name"), Integer.parseInt(this.GetRequestVal("FieldType")), Float.parseFloat(this.GetRequestVal("x")), Float.parseFloat(this.GetRequestVal("y")));
			return "true";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 生成所有表单元素.
	 
	 @return 
	*/
	public final String CCForm_AllElements_ResponseJson()
	{
		try
		{
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

			MapDtls dtls = new MapDtls();
			dtls.Retrieve(MapDtlAttr.FK_MapData, this.getFK_MapData(), MapDtlAttr.FK_Node, 0);
			ds.Tables.add(dtls.ToDataTableField("Sys_MapDtl"));

			FrmLines lines = new FrmLines(this.getFK_MapData());
			ds.Tables.add(lines.ToDataTableField("Sys_FrmLine"));

			BP.Sys.FrmUI.MapFrameExts mapFrameExts = new BP.Sys.FrmUI.MapFrameExts(this.getFK_MapData());
			ds.Tables.add(mapFrameExts.ToDataTableField("Sys_MapFrame"));

			//组织节点组件信息.
			String sql = "";
			if (this.getFK_Node() > 100)
			{
				sql += "select '轨迹图' AS Name,'FlowChart' AS No,FrmTrackSta Sta,FrmTrack_X X,FrmTrack_Y Y,FrmTrack_H H,FrmTrack_W  W from WF_Node WHERE nodeid=" + SystemConfig.getAppCenterDBVarStr() + "nodeid";
				sql += " union select '审核组件'AS Name, 'FrmCheck'AS No,FWCSta Sta,FWC_X X,FWC_Y Y,FWC_H H, FWC_W W from WF_Node WHERE nodeid=" + SystemConfig.getAppCenterDBVarStr() + "nodeid";
				sql += " union select '子流程' AS Name,'SubFlowDtl'AS  No,SFSta Sta,SF_X X,SF_Y Y,SF_H H, SF_W W from WF_Node  WHERE nodeid=" + SystemConfig.getAppCenterDBVarStr() + "nodeid";
				sql += " union select '子线程' AS Name, 'ThreadDtl'AS  No,FrmThreadSta Sta,FrmThread_X X,FrmThread_Y Y,FrmThread_H H,FrmThread_W W from WF_Node WHERE nodeid=" + SystemConfig.getAppCenterDBVarStr() + "nodeid";
				sql += " union select '流转自定义' AS Name,'FrmTransferCustom' AS  No,FTCSta Sta,FTC_X X,FTC_Y Y,FTC_H H,FTC_W  W FROM WF_Node WHERE nodeid=" + SystemConfig.getAppCenterDBVarStr() + "nodeid";
				Paras ps = new Paras();
				ps.SQL = sql;
				ps.Add("nodeid", this.getFK_Node());
				DataTable dt = null;

				try
				{
					dt = DBAccess.RunSQLReturnTable(ps);
				}
				catch (RuntimeException ex)
				{
					FrmSubFlow sb = new FrmSubFlow();
					sb.CheckPhysicsTable();

					TransferCustom tc = new TransferCustom();
					tc.CheckPhysicsTable();

					FrmThread ft = new FrmThread();
					ft.CheckPhysicsTable();

					FrmTrack ftd = new FrmTrack();
					ftd.CheckPhysicsTable();

					FrmTransferCustom ftd1 = new FrmTransferCustom();
					ftd1.CheckPhysicsTable();

					throw ex;
				}

				dt.TableName = "FigureCom";

				if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
				{
					//  figureComCols = "Name,No,Sta,X,Y,H,W";
					dt.Columns[0].ColumnName = "Name";
					dt.Columns[1].ColumnName = "No";
					dt.Columns[2].ColumnName = "Sta";
					dt.Columns[3].ColumnName = "X";
					dt.Columns[4].ColumnName = "Y";
					dt.Columns[5].ColumnName = "H";
					dt.Columns[6].ColumnName = "W";
				}
				ds.Tables.add(dt);
			}

			return BP.Tools.Json.ToJson(ds);
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}

	/** 
	 保存表单
	 
	 @return 
	*/
	public final String SaveForm()
	{
		//清缓存
		BP.Sys.CCFormAPI.AfterFrmEditAction(this.getFK_MapData());

		String docs = this.GetRequestVal("diagram");
		BP.Sys.CCFormAPI.SaveFrm(this.getFK_MapData(), docs);

		return "保存成功.";
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 表格处理.
	public final String Tables_Init()
	{
		BP.Sys.SFTables tabs = new BP.Sys.SFTables();
		tabs.RetrieveAll();
		DataTable dt = tabs.ToDataTableField();
		dt.Columns.Add("RefNum", Integer.class);

		for (DataRow dr : dt.Rows)
		{
			//求引用数量.
			int refNum = BP.DA.DBAccess.RunSQLReturnValInt("SELECT COUNT(KeyOfEn) FROM Sys_MapAttr WHERE UIBindKey='" + dr.get("No") + "'", 0);
			dr.set("RefNum", refNum);
		}
		return BP.Tools.Json.ToJson(dt);
	}
	public final String Tables_Delete()
	{
		try
		{
			BP.Sys.SFTable tab = new BP.Sys.SFTable();
			tab.No = this.getNo();
			tab.Delete();
			return "删除成功.";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}

	public final String TableRef_Init()
	{
		BP.Sys.MapAttrs mapAttrs = new BP.Sys.MapAttrs();
		mapAttrs.RetrieveByAttr(BP.Sys.MapAttrAttr.UIBindKey, this.getFK_SFTable());

		DataTable dt = mapAttrs.ToDataTableField();
		return BP.Tools.Json.ToJson(dt);
	}


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 表单重置
	 
	 @return 
	*/
	public final String ResetFrm_Init()
	{
		MapData md = new MapData(this.getFK_MapData());
		md.ResetMaxMinXY();
		md.Update();
		return "重置成功.";
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 复制表单
	/** 
	 复制表单属性和表单内容
	 
	 @param frmId 新表单ID
	 @param frmName 新表单内容
	*/
	public final void DoCopyFrm()
	{
		String fromFrmID = GetRequestVal("FromFrmID");
		String toFrmID = GetRequestVal("ToFrmID");
		String toFrmName = GetRequestVal("ToFrmName");
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 原表单信息
		//表单信息
		MapData fromMap = new MapData(fromFrmID);
		//单据信息
		FrmBill fromBill = new FrmBill();
		fromBill.No = fromFrmID;
		int billCount = fromBill.RetrieveFromDBSources();
		//实体单据
		FrmDict fromDict = new FrmDict();
		fromDict.No = fromFrmID;
		int DictCount = fromDict.RetrieveFromDBSources();
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 原表单信息

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 复制表单
		MapData toMapData = new MapData();
		toMapData = fromMap;
		toMapData.No = toFrmID;
		toMapData.Name = toFrmName;
		toMapData.Insert();
		if (billCount != 0)
		{
			FrmBill toBill = new FrmBill();
			toBill = fromBill;
			toBill.No = toFrmID;
			toBill.Name = toFrmName;
			toBill.setEntityType(EntityType.FrmBill);
			toBill.Update();
		}
		if (DictCount != 0)
		{
			FrmDict toDict = new FrmDict();
			toDict = fromDict;
			toDict.No = toFrmID;
			toDict.Name = toFrmName;
			toDict.setEntityType(EntityType.FrmDict);
			toDict.Update();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 复制表单

		MapData.ImpMapData(toFrmID, BP.Sys.CCFormAPI.GenerHisDataSet_AllEleInfo(fromFrmID));

		//清空缓存
		toMapData.RepairMap();
		BP.Sys.SystemConfig.DoClearCash();


	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 复制表单

}