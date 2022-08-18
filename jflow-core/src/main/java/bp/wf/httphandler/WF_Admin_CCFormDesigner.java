package bp.wf.httphandler;

import bp.sys.CCFormAPI;
import bp.web.*;
import bp.sys.*;
import bp.da.*;
import bp.en.*;
import bp.ccbill.*;
import bp.*;
import bp.wf.*;

import java.net.URLDecoder;

public class WF_Admin_CCFormDesigner extends bp.difference.handler.WebContralBase
{

		///#region 执行父类的重写方法.
	/** 
	 构造函数
	*/
	public WF_Admin_CCFormDesigner() throws Exception {
	}

	/** 
	 创建枚举类型字段
	 
	 @return 
	*/
	public final String FrmEnumeration_NewEnumField() throws Exception {
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

		bp.sys.CCFormAPI.NewEnumField(fk_mapdata, keyOfEn, fieldDesc, enumKeyOfBind, ctrl, x, y);
		return "绑定成功.";
	}
	/** 
	 创建外键字段.
	 
	 @return 
	*/
	public final String NewSFTableField() throws Exception {
		try
		{
			String fk_mapdata = this.GetRequestVal("FK_MapData");
			String keyOfEn = this.GetRequestVal("KeyOfEn");
			String fieldDesc = this.GetRequestVal("Name");
			String sftable = this.GetRequestVal("UIBindKey");
			float x = Float.parseFloat(this.GetRequestVal("x"));
			float y = Float.parseFloat(this.GetRequestVal("y"));

			//调用接口,执行保存.
			CCFormAPI.SaveFieldSFTable(fk_mapdata, keyOfEn, fieldDesc, sftable, x, y, 1);
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
	public final String ParseStringToPinyin() throws Exception {
		String name = GetRequestVal("name");
		String flag = GetRequestVal("flag");
		//暂时没发现此方法在哪里有调用，edited by liuxc,2017-9-25
		return CCFormAPI.ParseStringToPinyinField(name, flag.equals("true")==true?true:false, true, 20);
	}

	/** 
	 创建隐藏字段.
	 
	 @return 
	*/
	public final String NewHidF() throws Exception {
		MapAttr mdHid = new MapAttr();
		mdHid.setMyPK(this.getFK_MapData() + "_" + this.getKeyOfEn());
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
	 默认执行的方法
	 
	 @return 
	*/
	@Override
	protected String DoDefaultMethod() throws Exception {
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

		///#endregion 执行父类的重写方法.


		///#region 创建表单.
	public final String NewFrmGuide_GenerPinYin() throws Exception {
		String isQuanPin = this.GetRequestVal("IsQuanPin");
		String name = this.GetRequestVal("TB_Name");
		name = URLDecoder.decode(name, "UTF-8");

		//表单No长度最大100，因有前缀CCFrm_，因此此处设置最大94，added by liuxc,2017-9-25
		String str = bp.sys.CCFormAPI.ParseStringToPinyinField(name, isQuanPin.equals("1")== true?true:false, true, 94);

		MapData md = new MapData();
		md.setNo(str);
		if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
		{
			md.setNo(str + "_" + WebUser.getOrgNo());
		}
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
	public final String NewFrmGuide_Init() throws Exception {
		DataSet ds = new DataSet();

		SFDBSrc src = new SFDBSrc("local");
		ds.Tables.add(src.ToDataTableField("SFDBSrc"));

		DataTable tables = src.GetTables(true);
		tables.TableName = "Tables";
		ds.Tables.add(tables);
		return bp.tools.Json.ToJson(ds);

	}
	/** 
	 创建一个DBList.
	 
	 @return 
	*/
	public final String NewFrmGuide_Create_DBList() throws Exception {
		MapData md = new MapData();
		md.setName( this.GetRequestVal("TB_Name"));

		md.setNo(DataType.ParseStringForNo(this.GetRequestVal("TB_No"), 100));
		if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
		{
			md.setNo(md.getNo() + "_" + WebUser.getOrgNo());
		}

		//表单类型.
		md.setHisFrmTypeInt(this.GetRequestValInt("DDL_FrmType"));
		md.setEntityType(EntityType.DBList);
		String sort = this.GetRequestVal("FK_FrmSort");
		if (DataType.IsNullOrEmpty(sort) == true)
		{
			sort = this.GetRequestVal("DDL_FrmTree");
		}

		md.setFK_FormTree(sort);

		//类型.
		md.setAppType ("100"); //独立表单.
		md.setDBSrc (this.GetRequestVal("DDL_DBSrc"));
		if (md.getIsExits() == true)
		{
			return "err@表单ID:" + md.getNo() + "已经存在.";
		}

		//没有设置表，保存不上.
		md.setPTable ( this.getNo());
		md.Insert();

		//增加上OID字段.
		CCFormAPI.RepareCCForm(md.getNo());

		//数据源实体，修改OID的属性为字符型
		MapAttr mapAttr = new MapAttr(md.getNo() + "_OID");
		mapAttr.setMyDataType(DataType.AppString);
		mapAttr.Update();


		FrmDict entityDict = new FrmDict(md.getNo());
		entityDict.CheckEnityTypeAttrsFor_EntityNoName();
		entityDict.InsertToolbarBtns();


			///#region 初始化数据.
		DBList db = new DBList(md.getNo());
		db.setMainTable("A");
		db.setMainTablePK("No");

		if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			db.setExpEn("SELECT A.No as OID, A.No as BillNo, a.Name AS Title, A.Tel,A.Email , A.FK_Dept as DeptNo, B.Name AS DeptT FROM Port_Emp A, Port_Dept B WHERE A.FK_Dept=B.No AND A.No='@Key' ");
			db.setExpList("SELECT A.No as OID, A.No as BillNo, a.Name AS Title, A.Tel, A.PinYin,A.Email, B.No as DeptNo, B.Name AS DeptT FROM Port_Emp A, Port_Dept B WHERE A.FK_Dept=B.No ");
			db.setExpCount("SELECT  count(a.No) as Num FROM Port_Emp A, Port_Dept B WHERE A.FK_Dept=B.No ");
		}
		else
		{
			db.setExpEn("SELECT A.No as OID, A.No as BillNo, a.Name AS Title, A.Tel ,A.Email , A.FK_Dept as DeptNo, B.Name AS DeptT FROM Port_Emp A, Port_Dept B WHERE A.FK_Dept=B.No AND A.No='@Key' AND A.OrgNo='@WebUser.OrgNo' ");
			db.setExpList("SELECT A.No as OID, A.No as BillNo, a.Name AS Title, A.Tel, A.PinYin,A.Email, B.No as DeptNo, B.Name AS DeptT FROM Port_Emp A, Port_Dept B WHERE A.FK_Dept=B.No AND A.OrgNo='@WebUser.OrgNo'  ");
			db.setExpCount("SELECT  count(a.No) as Num FROM Port_Emp A, Port_Dept B WHERE A.FK_Dept=B.No AND A.OrgNo='@WebUser.OrgNo'  ");
		}

		db.Update();

			///#endregion  初始化数据.


		return "创建成功...";
	}
	/** 
	 创建表单
	 
	 @return 
	*/
	public final String NewFrmGuide_Create() throws Exception {
		MapData md = new MapData();

		try
		{
			md.setName( this.GetRequestVal("TB_Name"));
			md.setNo(DataType.ParseStringForNo(this.GetRequestVal("TB_No"), 100));
			md.setHisFrmTypeInt(this.GetRequestValInt("DDL_FrmType"));
			String ptable = this.GetRequestVal("TB_PTable");

			md.setPTable ( ptable);
			//   md.setPTable ( DataType.ParseStringForNo(this.GetRequestVal("TB_PTable"), 100);
			//数据表模式。  需要翻译.
			md.setPTableModel(this.GetRequestValInt("DDL_PTableModel"));

			String sort = this.GetRequestVal("FK_FrmSort");
			if (DataType.IsNullOrEmpty(sort) == true)
			{
				sort = this.GetRequestVal("DDL_FrmTree");
			}

			//    md.FK_FrmSort = sort;
			md.setFK_FrmSort(sort);
			md.setFK_FormTree(sort);

			md.setAppType("0"); //独立表单
			md.setDBSrc(this.GetRequestVal("DDL_DBSrc"));
			if (md.getIsExits() == true)
			{
				return "err@表单ID:" + md.getNo() + "已经存在.";
			}

			switch (md.getHisFrmType())
			{
				//自由，傻瓜，SL表单不做判断
				case FoolForm:
				case Develop:
				case ChapterFrm:
					break;
				case Url:
				case Entity:
					md.setUrlExt(md.getPTable());
					md.setPTable ( "");
					break;
				//如果是以下情况，导入模式
				case WordFrm:
				case WPSFrm:
				case ExcelFrm:
				case VSTOForExcel:
					break;
				default:
					throw new RuntimeException("err@未知表单类型." + md.getHisFrmType().toString());
			}
			md.Insert();

			//增加上OID字段.
			CCFormAPI.RepareCCForm(md.getNo());

			EntityType entityType = EntityType.forValue(this.GetRequestValInt("EntityType"));


				///#region 如果是单据.
			if (entityType == EntityType.FrmBill)
			{
				FrmBill bill = new FrmBill(md.getNo());
				bill.setEntityType(EntityType.FrmBill);
				bill.setBillNoFormat("ccbpm{yyyy}-{MM}-{dd}-{LSH4}");

				//设置默认的查询条件.
				bill.SetPara("IsSearchKey", 1);
				bill.SetPara("DTSearchWay", 0);

				bill.Update();
				boolean isHavePFrmID = false;
				if (DataType.IsNullOrEmpty(this.GetRequestVal("FrmID")) == false)
				{
					isHavePFrmID = true;
				}
				bill.CheckEnityTypeAttrsFor_Bill(isHavePFrmID);
				bill.InsertToolbarBtns();
			}

				///#endregion 如果是单据.


				///#region 如果是实体 EnityNoName .
			if (entityType == EntityType.FrmDict)
			{
				FrmDict entityDict = new FrmDict(md.getNo());
				entityDict.setBillNoFormat("3"); //编码格式.001,002,003.
				entityDict.setBtnNewModel(0);

				//设置默认的查询条件.
				entityDict.SetPara("IsSearchKey", 1);
				entityDict.SetPara("DTSearchWay", 0);

				entityDict.setEntityType(EntityType.FrmDict);

				entityDict.Update();
				entityDict.CheckEnityTypeAttrsFor_EntityNoName();
				entityDict.InsertToolbarBtns();
			}

				///#endregion 如果是实体 EnityNoName .

			//创建表与字段.
			GEEntity en = new GEEntity(md.getNo());
			en.CheckPhysicsTable();

			if (md.getHisFrmType() == FrmType.WPSFrm || md.getHisFrmType() == FrmType.WordFrm || md.getHisFrmType() == FrmType.ExcelFrm)
			{
				/*把表单模版存储到数据库里 */
				return "url@../../Comm/RefFunc/En.htm?EnName=BP.WF.Template.Frm.MapFrmExcel&PKVal=" + md.getNo();
			}

			if (md.getHisFrmType() == FrmType.Entity)
			{
				return "url@../../Comm/Ens.htm?EnsName=" + md.getPTable();
			}

			if (md.getHisFrmType() == FrmType.Develop)
			{
				return "url@../DevelopDesigner/Designer.htm?FK_MapData=" + md.getNo() + "&FrmID=" + md.getNo() + "&EntityType=" + this.GetRequestVal("EntityType");
			}

			return "url@../FoolFormDesigner/Designer.htm?IsFirst=1&FK_MapData=" + md.getNo() + "&EntityType=" + this.GetRequestVal("EntityType");
		}
		catch (RuntimeException ex)
		{
			md.Delete();
			return "err@" + ex.getMessage();
		}
	}

		///#endregion 创建表单.



		///#region 表格处理.
	public final String Tables_Init() throws Exception {
		SFTables tabs = new SFTables();
		tabs.RetrieveAll();
		DataTable dt = tabs.ToDataTableField("dt");
		dt.Columns.Add("RefNum", Integer.class);

		for (DataRow dr : dt.Rows)
		{
			//求引用数量.
			int refNum = DBAccess.RunSQLReturnValInt("SELECT COUNT(KeyOfEn) FROM Sys_MapAttr WHERE UIBindKey='" + dr.getValue("No") + "'", 0);
			dr.setValue("RefNum", refNum);
		}
		return bp.tools.Json.ToJson(dt);
	}
	public final String Tables_Delete() throws Exception {
		try
		{
			SFTable tab = new SFTable();
			tab.setNo(this.getNo());
			tab.Delete();
			return "删除成功.";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}

	public final String TableRef_Init() throws Exception {
		MapAttrs mapAttrs = new MapAttrs();
		mapAttrs.RetrieveByAttr(MapAttrAttr.UIBindKey, this.getFK_SFTable());

		DataTable dt = mapAttrs.ToDataTableField("dt");
		return bp.tools.Json.ToJson(dt);
	}



		///#endregion




		///#region 复制表单

	public final void DoCopyFrm() throws Exception {
		String fromFrmID = GetRequestVal("FromFrmID");
		String toFrmID = GetRequestVal("ToFrmID");
		String toFrmName = GetRequestVal("ToFrmName");
		MapData toMapData = new MapData(fromFrmID);
		toMapData.setNo(toFrmID);
		toMapData.setName(toFrmName);
		toMapData.Insert();
		//导入表单信息
		MapData.ImpMapData(toFrmID, CCFormAPI.GenerHisDataSet_AllEleInfo(fromFrmID));

		if (toMapData.getHisEntityType() == EntityType.FrmBill.getValue())
		{
			FrmBill frmBill = new FrmBill(fromFrmID);
			frmBill.setNo(toFrmID);
			frmBill.setName(toFrmName);
			frmBill.Update();
		}
		if (toMapData.getHisEntityType() == EntityType.FrmDict.getValue())
		{
			FrmDict frmDict = new FrmDict(fromFrmID);
			frmDict.setNo(toFrmID);
			frmDict.setName(toFrmName);
			frmDict.Update();
		}

		//清空缓存

		toMapData.RepairMap();
		bp.difference.SystemConfig.DoClearCash();


	}

		///#endregion 复制表单

}