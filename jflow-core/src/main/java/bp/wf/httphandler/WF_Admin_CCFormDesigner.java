package bp.wf.httphandler;

import bp.wf.*;
import bp.web.*;
import bp.sys.*;
import bp.sys.frmui.MapFrameExts;
import bp.da.*;
import bp.difference.SystemConfig;
import bp.difference.handler.WebContralBase;
import bp.en.*;
import bp.wf.template.*;
import bp.ccbill.*;

import java.net.URLDecoder;

public class WF_Admin_CCFormDesigner extends WebContralBase
{


		///执行父类的重写方法.
	/** 
	 构造函数
	*/
	public WF_Admin_CCFormDesigner()
	{
	}

	/** 
	 创建枚举类型字段
	 
	 @return 
	 * @throws Exception 
	*/
	public final String FrmEnumeration_NewEnumField() throws Exception
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

		bp.sys.CCFormAPI.NewEnumField(fk_mapdata, keyOfEn, fieldDesc, enumKeyOfBind, ctrl, x, y);
		return "绑定成功.";
	}
	/** 
	 创建外键字段.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String NewSFTableField() throws Exception
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
			bp.sys.CCFormAPI.SaveFieldSFTable(fk_mapdata, keyOfEn, fieldDesc, sftable, x, y);
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
		return bp.sys.CCFormAPI.ParseStringToPinyinField(name, flag.equals("true")==true?true:false, true, 20);
	}

	/** 
	 创建隐藏字段.
	 
	 @return 
	 * @throws Exception 
	 * @throws NumberFormatException 
	*/
	public final String NewHidF() throws NumberFormatException, Exception
	{
		MapAttr mdHid = new MapAttr();
		mdHid.setMyPK(this.getFK_MapData() + "_" + this.getKeyOfEn());
		mdHid.setFK_MapData(this.getFK_MapData());
		mdHid.setKeyOfEn(this.getKeyOfEn());
		mdHid.setName( this.getName());
		mdHid.setMyDataType(Integer.parseInt(this.GetRequestVal("FieldType")));
		mdHid.setHisEditType(EditType.Edit);
		mdHid.setMaxLen( 100);
		mdHid.setMinLen(0);
		mdHid.setLGType( FieldTypeS.Normal);
		mdHid.setUIVisible( false);
		mdHid.setUIIsEnable(false);
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

		/// 执行父类的重写方法.


		///创建表单.
	public final String NewFrmGuide_GenerPinYin() throws Exception
	{
		String isQuanPin = this.GetRequestVal("IsQuanPin");
		String name = this.GetRequestVal("TB_Name");
		name = URLDecoder.decode(name, "UTF-8");
		//表单No长度最大100，因有前缀CCFrm_，因此此处设置最大94，added by liuxc,2017-9-25
		String str = bp.sys.CCFormAPI.ParseStringToPinyinField(name, isQuanPin.equals("1")== true?true:false, true, 94);

		MapData md = new MapData();
		md.setNo(str);
		if (md.RetrieveFromDBSources() == 0)
		{
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
		return bp.tools.Json.ToJson(ds);

	}
	/** 
	 创建表单
	 
	 @return 
	 * @throws Exception 
	*/
	public final String NewFrmGuide_Create() throws Exception
	{
		MapData md = new MapData();

		try
		{
			md.setName( this.GetRequestVal("TB_Name"));
			md.setNo(DataType.ParseStringForNo(this.GetRequestVal("TB_No"), 100));

			md.setHisFrmTypeInt(this.GetRequestValInt("DDL_FrmType"));

			//表单的物理表.
			if (md.getHisFrmType() == bp.sys.FrmType.Url || md.getHisFrmType() == bp.sys.FrmType.Entity)
			{
				md.setPTable(this.GetRequestVal("TB_PTable"));
			}
			else
			{
				md.setPTable(DataType.ParseStringForNo(this.GetRequestVal("TB_PTable"), 100));
			}

			//数据表模式。 @周朋 需要翻译.
			md.setPTableModel(this.GetRequestValInt("DDL_PTableModel"));

			//@李国文 需要对比翻译.
			String sort = this.GetRequestVal("FK_FrmSort");
			if (DataType.IsNullOrEmpty(sort) == true)
			{
				sort = this.GetRequestVal("DDL_FrmTree");
			}

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
				case FreeFrm:
				case FoolForm:
				case Develop:
					break;
				case Url:
				case Entity:
					md.setUrl(md.getPTable());
					md.setPTable("");
					break;
				//如果是以下情况，导入模式
				case WordFrm:
				case ExcelFrm:
				case VSTOForExcel:
					break;
				default:
					throw new RuntimeException("未知表单类型." + md.getHisFrmType().toString());
			}
			md.Insert();

			//增加上OID字段.
			bp.sys.CCFormAPI.RepareCCForm(md.getNo());

			bp.ccbill.EntityType entityType = EntityType.forValue(this.GetRequestValInt("EntityType"));


				///如果是单据.
			if (entityType == EntityType.FrmBill)
			{
				bp.ccbill.FrmBill bill = new FrmBill(md.getNo());
				bill.setEntityType(EntityType.FrmBill);
				bill.setBillNoFormat("ccbpm{yyyy}-{MM}-{dd}-{LSH4}");

				//设置默认的查询条件.
				bill.SetPara("IsSearchKey", 1);
				bill.SetPara("DTSearchWay", 0);

				bill.Update();
				bill.CheckEnityTypeAttrsFor_Bill();
			}

				/// 如果是单据.


				///如果是实体 EnityNoName .
			if (entityType == EntityType.FrmDict)
			{
				bp.ccbill.FrmDict entityDict = new FrmDict(md.getNo());
				entityDict.setBillNoFormat("3"); //编码格式.001,002,003.
				entityDict.setBtnNewModel(0);

				//设置默认的查询条件.
				entityDict.SetPara("IsSearchKey", 1);
				entityDict.SetPara("DTSearchWay", 0);

				entityDict.setEntityType(EntityType.FrmDict);

				entityDict.Update();
				entityDict.CheckEnityTypeAttrsFor_EntityNoName();
			}

				/// 如果是实体 EnityNoName .

			//创建表与字段.
			GEEntity en = new GEEntity(md.getNo());
			en.CheckPhysicsTable();

			if (md.getHisFrmType() == bp.sys.FrmType.WordFrm || md.getHisFrmType() == bp.sys.FrmType.ExcelFrm)
			{
				/*把表单模版存储到数据库里 */
				return "url@../../Comm/RefFunc/En.htm?EnName=BP.WF.Template.MapFrmExcel&PKVal=" + md.getNo();
			}

			if (md.getHisFrmType() == bp.sys.FrmType.Entity)
			{
				return "url@../../Comm/Ens.htm?EnsName=" + md.getPTable();
			}

			if (md.getHisFrmType() == bp.sys.FrmType.FreeFrm)
			{
				return "url@FormDesigner.htm?FK_MapData=" + md.getNo() + "&EntityType=" + this.GetRequestVal("EntityType");
			}

			if (md.getHisFrmType() == bp.sys.FrmType.Develop)
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

		/// 创建表单.

	public final String LetLogin() throws Exception
	{
		bp.port.Emp emp = new bp.port.Emp("admin");
		WebUser.SignInOfGener(emp);
		return "登录成功.";
	}


	public final String GoToFrmDesigner_Init() throws Exception
	{
		//根据不同的表单类型转入不同的表单设计器上去.
		bp.sys.MapData md = new bp.sys.MapData(this.getFK_MapData());
		if (md.getHisFrmType() == bp.sys.FrmType.FoolForm)
		{
			/* 傻瓜表单 需要翻译. */
			return "url@../FoolFormDesigner/Designer.htm?IsFirst=1&FK_MapData=" + this.getFK_MapData();
		}

		if (md.getHisFrmType() == bp.sys.FrmType.Develop)
		{
			/* 开发者表单 */
			return "url@../DevelopDesigner/Designer.htm?FK_MapData=" + this.getFK_MapData() + "&FrmID=" + this.getFK_MapData() + "&IsFirst=1";
		}

		if (md.getHisFrmType() == bp.sys.FrmType.FreeFrm)
		{
			/* 自由表单 */
			return "url@../CCFormDesigner/FormDesigner.htm?FK_MapData=" + this.getFK_MapData() + "&IsFirst=1";
		}

		if (md.getHisFrmType() == bp.sys.FrmType.VSTOForExcel)
		{
			/* 自由表单 */
			return "url@../CCFormDesigner/FormDesigner.htm?FK_MapData=" + this.getFK_MapData();
		}

		if (md.getHisFrmType() == bp.sys.FrmType.Url)
		{
			/* 自由表单 */
			return "url@../../Comm/RefFunc/EnOnly.htm?EnName=BP.WF.Template.MapDataURL&No=" + this.getFK_MapData();
		}

		if (md.getHisFrmType() == bp.sys.FrmType.Entity)
		{
			return "url@../../Comm/Ens.htm?EnsName=" + md.getPTable();
		}

		return "err@没有判断的表单转入类型" + md.getHisFrmType().toString();
	}

	public final String PublicNoNameCtrlCreate() throws Exception
	{
		try
		{
			float x = Float.parseFloat(this.GetRequestVal("x"));
			float y = Float.parseFloat(this.GetRequestVal("y"));
			bp.sys.CCFormAPI.CreatePublicNoNameCtrl(this.getFrmID(), this.GetRequestVal("CtrlType"), this.GetRequestVal("No"), this.GetRequestVal("Name"), x, y);
			return "true";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	public final String NewImage() throws Exception
	{

		try
		{
			bp.sys.CCFormAPI.NewImage(this.GetRequestVal("FrmID"), this.GetRequestVal("KeyOfEn"), this.GetRequestVal("Name"), Float.parseFloat(this.GetRequestVal("x")), Float.parseFloat(this.GetRequestVal("y")));
			return "true";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}


	}
	public final String NewField() throws Exception
	{
		try
		{
			bp.sys.CCFormAPI.NewField(this.GetRequestVal("FrmID"), this.GetRequestVal("KeyOfEn"), this.GetRequestVal("Name"), Integer.parseInt(this.GetRequestVal("FieldType")), Float.parseFloat(this.GetRequestVal("x")), Float.parseFloat(this.GetRequestVal("y")));
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
	 * @throws Exception 
	*/
	public final String CCForm_AllElements_ResponseJson() throws Exception
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

			MapFrameExts mapFrameExts = new MapFrameExts(this.getFK_MapData());
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
				ps.SQL=sql;
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

				if (SystemConfig.AppCenterDBFieldCaseModel() != FieldCaseModel.None)
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
				ds.Tables.add(dt);
			}

			return bp.tools.Json.ToJson(ds);
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}

	/** 
	 保存表单
	 
	 @return 
	 * @throws Exception 
	*/
	public final String SaveForm() throws Exception
	{
		//清缓存
		bp.sys.CCFormAPI.AfterFrmEditAction(this.getFK_MapData());

		String docs = this.GetRequestVal("diagram");
		bp.sys.CCFormAPI.SaveFrm(this.getFK_MapData(), docs);

		return "保存成功.";
	}


		///表格处理.
	public final String Tables_Init() throws Exception
	{
		bp.sys.SFTables tabs = new bp.sys.SFTables();
		tabs.RetrieveAll();
		DataTable dt = tabs.ToDataTableField();
		dt.Columns.Add("RefNum", Integer.class);

		for (DataRow dr : dt.Rows)
		{
			//求引用数量.
			int refNum = DBAccess.RunSQLReturnValInt("SELECT COUNT(KeyOfEn) FROM Sys_MapAttr WHERE UIBindKey='" + dr.getValue("No") + "'", 0);
			dr.setValue("RefNum", refNum);
		}
		return bp.tools.Json.ToJson(dt);
	}
	public final String Tables_Delete() throws Exception
	{
		try
		{
			bp.sys.SFTable tab = new bp.sys.SFTable();
			tab.setNo(this.getNo());
			tab.Delete();
			return "删除成功.";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}

	public final String TableRef_Init() throws Exception
	{
		MapAttrs mapAttrs = new MapAttrs();
		mapAttrs.RetrieveByAttr(MapAttrAttr.UIBindKey, this.getFK_SFTable());

		DataTable dt = mapAttrs.ToDataTableField();
		return bp.tools.Json.ToJson(dt);
	}



		///

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


		///复制表单
	/** 
	 复制表单属性和表单内容
	 @param frmId 新表单ID
	 @param frmName 新表单内容
	 * @throws Exception 
	*/
	public final void DoCopyFrm() throws Exception
	{
		String fromFrmID = GetRequestVal("FromFrmID");
		String toFrmID = GetRequestVal("ToFrmID");
		String toFrmName = GetRequestVal("ToFrmName");


			///原表单信息
		//表单信息
		MapData toMapData = new MapData(fromFrmID);
		toMapData.setNo(toFrmID);
		toMapData.setName(toFrmName);
		toMapData.Insert();

		MapData.ImpMapData(toFrmID,bp.sys.CCFormAPI.GenerHisDataSet_AllEleInfo(fromFrmID));

		//清空缓存
		toMapData.RepairMap();
		SystemConfig.DoClearCash();


	}

		/// 复制表单

}