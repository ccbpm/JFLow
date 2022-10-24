package bp.wf.httphandler;

import bp.difference.handler.CommonFileUtils;
import bp.sys.*;
import bp.da.*;
import bp.en.*;
import bp.ccbill.*;
import bp.difference.*;
import bp.*;
import bp.sys.CCFormAPI;
import bp.wf.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class WF_Admin_FoolFormDesigner_ImpExp extends bp.difference.handler.WebContralBase
{
	/** 
	 构造函数
	*/
	public WF_Admin_FoolFormDesigner_ImpExp()  {
	}


		///#region 导出.
	/** 
	下载
	 
	 @return 
	*/
	public final String Exp_DownFormTemplete() throws Exception {
		bp.wf.httphandler.WF_Admin_CCBPMDesigner en = new WF_Admin_CCBPMDesigner();
		return en.DownFormTemplete();
	}

		///#endregion


		///#region 导入
	/** 
	 初始化 导入的界面 .
	 
	 @return 
	*/
	public final String Imp_Init() throws Exception {
		DataSet ds = new DataSet();

		String sql = "";
		DataTable dt;

		if (this.getFK_Flow() != null)
		{
			//加入节点表单. 如果没有流程参数.

			Paras ps = new Paras();
			ps.SQL = "SELECT NodeID, Name  FROM WF_Node WHERE FK_Flow=" + SystemConfig.getAppCenterDBVarStr() + "FK_Flow ORDER BY NODEID ";
			ps.Add("FK_Flow", this.getFK_Flow(), false);
			dt = DBAccess.RunSQLReturnTable(ps);

			dt.TableName = "WF_Node";

			if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.UpperCase)
			{
				dt.Columns.get("NODEID").ColumnName = "NodeID";
				dt.Columns.get("NAME").ColumnName = "Name";
			}

			if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.Lowercase)
			{
				dt.Columns.get("nodeid").ColumnName = "NodeID";
				dt.Columns.get("name").ColumnName = "Name";
			}


			ds.Tables.add(dt);
		}


			///#region 加入表单库目录.
		if (SystemConfig.getAppCenterDBType( ) == DBType.Oracle)
		{
			sql = "SELECT NO as No ,Name,ParentNo FROM Sys_FormTree ORDER BY  PARENTNO, IDX ";
		}
		else
		{
			sql = "SELECT No,Name,ParentNo FROM Sys_FormTree ORDER BY  PARENTNO, IDX ";
		}

		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_FormTree";
		if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.UpperCase)
		{
			dt.Columns.get("NO").ColumnName = "No";
			dt.Columns.get("NAME").ColumnName = "Name";
			dt.Columns.get("PARENTNO").ColumnName = "ParentNo";
		}
		if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.Lowercase)
		{
			dt.Columns.get("no").ColumnName = "No";
			dt.Columns.get("name").ColumnName = "Name";
			dt.Columns.get("parentno").ColumnName = "ParentNo";
		}
		ds.Tables.add(dt);

		//加入表单
		sql = "SELECT A.No, A.Name, A.FK_FormTree  FROM Sys_MapData A, Sys_FormTree B WHERE A.FK_FormTree=B.No";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapData";
		ds.Tables.add(dt);
		if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.UpperCase)
		{
			dt.Columns.get("NO").ColumnName = "No";
			dt.Columns.get("NAME").ColumnName = "Name";
			dt.Columns.get("FK_FORMTREE").ColumnName = "FK_FormTree";
		}
		if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.Lowercase)
		{
			dt.Columns.get("no").ColumnName = "No";
			dt.Columns.get("name").ColumnName = "Name";
			dt.Columns.get("fk_formtree").ColumnName = "FK_FormTree";
		}

			///#endregion 加入表单库目录.


			///#region 加入流程树目录.
		sql = "SELECT No,Name,ParentNo FROM WF_FlowSort ORDER BY  PARENTNO, IDX ";

		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_FlowSort";
		if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.UpperCase)
		{
			dt.Columns.get("NO").ColumnName = "No";
			dt.Columns.get("NAME").ColumnName = "Name";
			dt.Columns.get("PARENTNO").ColumnName = "ParentNo";
		}

		if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.Lowercase)
		{
			dt.Columns.get("no").ColumnName = "No";
			dt.Columns.get("name").ColumnName = "Name";
			dt.Columns.get("parentno").ColumnName = "ParentNo";
		}

		ds.Tables.add(dt);

		//加入表单
		sql = "SELECT No, Name, FK_FlowSort  FROM WF_Flow ";
		dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_Flow";
		ds.Tables.add(dt);
		if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.UpperCase)
		{
			dt.Columns.get("NO").ColumnName = "No";
			dt.Columns.get("NAME").ColumnName = "Name";
			dt.Columns.get("FK_FLOWSORT").ColumnName = "FK_FlowSort";
		}
		if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.Lowercase)
		{
			dt.Columns.get("no").ColumnName = "No";
			dt.Columns.get("name").ColumnName = "Name";
			dt.Columns.get("fk_flowsort").ColumnName = "FK_FlowSort";
		}

			///#endregion 加入流程树目录.


			///#region 数据源
		SFDBSrcs ens = new SFDBSrcs();
		ens.RetrieveAll();
		ds.Tables.add(ens.ToDataTableField("SFDBSrcs"));

			///#endregion

		//加入系统表.
		return bp.tools.Json.ToJson(ds);
	}

		///#endregion 如果是单据.




	/** 
	 从本机装载表单模版
	 
	 param fileByte 文件流
	 param fk_mapData 表单模版ID
	 param isClear 是否清空？
	 @return 执行结果
	*/
	public final String Imp_LoadFrmTempleteFromLocalFile() throws Exception {
		try
		{
			File xmlFile = null;
			String fileName = UUID.randomUUID().toString();
			try {
				xmlFile = File.createTempFile(fileName, ".xml");
			} catch (IOException e1) {
				xmlFile = new File(System.getProperty("java.io.tmpdir"), fileName + ".xml");
			}
			xmlFile.deleteOnExit();
			HttpServletRequest request = getRequest();
			try {
				CommonFileUtils.upload(request, "file", xmlFile);
			} catch (Exception e) {
				e.printStackTrace();
				return "err@执行失败";
			}

			Object a = getRequest().getParameter("file");
			String fk_mapData = this.getFK_MapData();
			DataSet ds = new DataSet();
			ds.readXml(xmlFile.getAbsolutePath());

			//执行装载.
			MapData.ImpMapData(fk_mapData, ds);
			if (this.getFK_Node() != 0)
			{
				Node nd = new Node(this.getFK_Node());
				nd.RepareMap(nd.getHisFlow());
			}
			//清空缓存
			MapData mymd = new MapData(fk_mapData);
			mymd.RepairMap();
			if (mymd.getHisEntityType() == EntityType.FrmBill.getValue())
			{
				FrmBill bill = new FrmBill(mymd.getNo());
				bill.setEntityType(EntityType.FrmBill);
				bill.setBillNoFormat("ccbpm{yyyy}-{MM}-{dd}-{LSH4}");

				//设置默认的查询条件.
				bill.SetPara("IsSearchKey", 1);
				bill.SetPara("DTSearchWay", 0);

				bill.Update();
				bill.CheckEnityTypeAttrsFor_Bill();
			}


				///#region 如果是实体 EnityNoName .
			if (mymd.getHisEntityType() == EntityType.FrmDict.getValue())
			{
				FrmDict entityDict = new FrmDict(mymd.getNo());
				entityDict.setBillNoFormat("3"); //编码格式.001,002,003.
				entityDict.setBtnNewModel(0);

				//设置默认的查询条件.
				entityDict.SetPara("IsSearchKey", 1);
				entityDict.SetPara("DTSearchWay", 0);

				entityDict.setEntityType(EntityType.FrmDict);

				entityDict.Update();
				entityDict.CheckEnityTypeAttrsFor_EntityNoName();
			}
			SystemConfig.DoClearCash();
			return "执行成功.";
		}
		catch (RuntimeException ex)
		{
			String msg = ex.getMessage();
			if (msg.contains("@此模板文件为流程模板")) {
				return "@以下错误不可导入:此模板文件为流程模板,请用表单模板导入！";
			}
			//第一次导入，可能因为没有字段，导致报错，系统会刷新一次，并修复字段
			//所以再执行一次导入
			try
			{
				String fk_mapData = this.getFK_MapData();
				File xmlFile = null;
				//读取上传的XML 文件.
				DataSet ds = new DataSet();
				String fileName = UUID.randomUUID().toString();
				try {
					xmlFile = File.createTempFile(fileName, ".xml");
				} catch (IOException e1) {
					xmlFile = new File(System.getProperty("java.io.tmpdir"), fileName + ".xml");
				}
				xmlFile.deleteOnExit();
				HttpServletRequest request = getRequest();
				try {
					CommonFileUtils.upload(request, "file", xmlFile);
				} catch (Exception e) {
					e.printStackTrace();
					return "err@执行失败";
				}

				Object a = getRequest().getParameter("file");

				ds.readXml(xmlFile.getAbsolutePath());

				//执行装载.
				MapData.ImpMapData(fk_mapData, ds);

				if (this.getFK_Node() != 0)
				{
					Node nd = new Node(this.getFK_Node());
					nd.RepareMap(nd.getHisFlow());
				}
				//清空缓存
				MapData mymd = new MapData(fk_mapData);
				mymd.RepairMap();
				if (mymd.getHisEntityType() == EntityType.FrmBill.getValue())
				{
					FrmBill bill = new FrmBill(mymd.getNo());
					bill.setEntityType(EntityType.FrmBill);
					bill.setBillNoFormat("ccbpm{yyyy}-{MM}-{dd}-{LSH4}");

					//设置默认的查询条件.
					bill.SetPara("IsSearchKey", 1);
					bill.SetPara("DTSearchWay", 0);

					bill.Update();
					bill.CheckEnityTypeAttrsFor_Bill();
				}

					///#endregion 如果是单据.


					///#region 如果是实体 EnityNoName .
				if (mymd.getHisEntityType() == EntityType.FrmDict.getValue())
				{
					FrmDict entityDict = new FrmDict(mymd.getNo());
					entityDict.setBillNoFormat("3"); //编码格式.001,002,003.
					entityDict.setBtnNewModel(0);

					//设置默认的查询条件.
					entityDict.SetPara("IsSearchKey", 1);
					entityDict.SetPara("DTSearchWay", 0);

					entityDict.setEntityType(EntityType.FrmDict);

					entityDict.Update();
					entityDict.CheckEnityTypeAttrsFor_EntityNoName();
				}
				SystemConfig.DoClearCash();
				return "执行成功.";
			}
			catch (RuntimeException newex)
			{
				return "err@导入失败:" + newex.getMessage();
			}
		}
	}

	/** 
	 从流程上copy表单
	 @徐彪来调用.
	 
	 @return 
	*/
	public final String Imp_CopyFromFlow() throws Exception {
		String ndfrm = "ND" + Integer.parseInt(this.getFK_Flow()) + "01";
		return Imp_CopyFrm(null, ndfrm);
	}
	public final String Imp_FrmEnsName() throws Exception {

		MapData md = new MapData(this.getFrmID());

		Entity en = ClassFactory.GetEns(this.getEnsName()).getGetNewEntity();

		Attrs attrs = en.getEnMap().getAttrs();
		for (Attr item : attrs.ToJavaList())
		{
			if (item.getIsPK() == true)
			{
				continue;
			}

			MapAttr mapAttr = item.getToMapAttr();
			mapAttr.setMyPK(this.getFrmID() + "_" + item.getKey());
			mapAttr.setFK_MapData(this.getFrmID());
			mapAttr.Save();
		}

		return this.getEnsName() + "的属性导入[" + this.getFrmID() + "]成功.";
	}

	/** 
	 从表单库导入
	 从节点导入
	 
	 @return 
	*/
	public final String Imp_FromsCopyFrm() throws Exception {
		return Imp_CopyFrm();
	}
	/** 
	 从节点上Copy
	 
	 param fromMapData 从表单ID
	 param fk_mapdata 到表单ID
	 param isClear 是否清楚现有的元素？
	 param isSetReadonly 是否设置为只读？
	 @return 执行结果
	*/

	public final String Imp_CopyFrm(String toFrmID) throws Exception {
		return Imp_CopyFrm(toFrmID, null);
	}

	public final String Imp_CopyFrm() throws Exception {
		return Imp_CopyFrm(null, null);
	}

//ORIGINAL LINE: public string Imp_CopyFrm(string toFrmID = null, string fromFrmID = null)
	public final String Imp_CopyFrm(String toFrmID, String fromFrmID)throws Exception
	{
		try
		{
			if (toFrmID == null)
			{
				toFrmID = this.getFK_MapData();
			}


			String fromMapData = fromFrmID;
			if (fromMapData == null)
			{
				fromMapData = this.GetRequestVal("FromFrmID");
			}

			boolean isClear = this.GetRequestValBoolen("IsClear");
			boolean isSetReadonly = this.GetRequestValBoolen("IsSetReadonly");

			//首先初始化本部门的.
			MapData mymd = new MapData(toFrmID);
			String frmSort = mymd.getFK_FormTree(); //表单类别,防止表单类别冲掉,导致表单树看不到他.


			MapData mdFrom = new MapData(fromMapData);
			DataSet ds = CCFormAPI.GenerHisDataSet_AllEleInfo(mdFrom.getNo());

			MapData.ImpMapData(toFrmID, ds);

			//设置为只读模式.
			if (isSetReadonly == true)
			{
				MapData.SetFrmIsReadonly(toFrmID);
			}

			//清空缓存
			mymd = new MapData(toFrmID);

			// 如果是节点表单，就要执行一次修复，以免漏掉应该有的系统字段。
			if (toFrmID.contains("ND") == true)
			{
				String fk_node = toFrmID.replace("ND", "");
				Node nd = new Node(Integer.parseInt(fk_node));
				nd.RepareMap(nd.getHisFlow());

				//设置节点ID.
				mymd.setName( nd.getName());
				mymd.setFK_FormTree("");
				mymd.Update();

				//如果包含ND，就保持附件的从表一致.
				if (fromMapData.indexOf("ND") == 0)
				{
					MapDtls dtls = new MapDtls(fromMapData);

				}


			}
			else
			{
				mymd.setFK_FormTree(frmSort);
				mymd.Update();

			}

			mymd.RepairMap();
			if (mymd.getHisEntityType() == EntityType.FrmBill.getValue())
			{
				FrmBill bill = new FrmBill(mymd.getNo());
				bill.setEntityType(EntityType.FrmBill);
				bill.setBillNoFormat("ccbpm{yyyy}-{MM}-{dd}-{LSH4}");

				//设置默认的查询条件.
				bill.SetPara("IsSearchKey", 1);
				bill.SetPara("DTSearchWay", 0);

				bill.Update();
				bill.CheckEnityTypeAttrsFor_Bill();
			}

				///#endregion 如果是单据.


				///#region 如果是实体 EnityNoName .
			if (mymd.getHisEntityType() == EntityType.FrmDict.getValue())
			{
				FrmDict entityDict = new FrmDict(mymd.getNo());
				entityDict.setBillNoFormat("3"); //编码格式.001,002,003.
				entityDict.setBtnNewModel(0);

				//设置默认的查询条件.
				entityDict.SetPara("IsSearchKey", 1);
				entityDict.SetPara("DTSearchWay", 0);

				entityDict.setEntityType(EntityType.FrmDict);

				entityDict.Update();
				entityDict.CheckEnityTypeAttrsFor_EntityNoName();
			}
			SystemConfig.DoClearCash();
			return "执行成功.";

				///#endregion

		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}


		///#region 04.从外部数据源导入
	/** 
	 选择一个数据源，进入步骤2
	 
	 @return 
	*/
	public final String Imp_Src_Step2_Init() throws Exception {
		SFDBSrc src = new SFDBSrc(this.GetRequestVal("FK_SFDBSrc"));

		//获取所有的表/视图
		DataTable dtTables = src.GetTables(false);

		return bp.tools.FormatToJson.ToJson(dtTables);
	}

	/** 
	 获取表字段
	 
	 @return 
	*/
	public final String Imp_Src_Step2_GetColumns() throws Exception {
		DataSet ds = new DataSet();

		//01.当前节点表单已经存在的列
		MapAttrs attrs = new MapAttrs(this.getFK_MapData());
		ds.Tables.add(attrs.ToDataTableField("MapAttrs"));

		//02.数据源表中的列
		SFDBSrc src = new SFDBSrc(this.GetRequestVal("FK_SFDBSrc"));
		DataTable tableColumns = src.GetColumns(this.GetRequestVal("STable"));
		tableColumns.TableName = "TableColumns";
		ds.Tables.add(tableColumns);

		return bp.tools.Json.ToJson(ds);
	}

	public final String Imp_Src_Step3_Init() throws Exception {
		DataSet ds = new DataSet();
		String SColumns = this.GetRequestVal("SColumns");
		SFDBSrc src = new SFDBSrc(this.GetRequestVal("FK_SFDBSrc"));
		DataTable tableColumns = src.GetColumns(this.GetRequestVal("STable"));

		// 01.添加列
		DataTable dt = tableColumns.clone();
		for (DataRow dr : tableColumns.Rows) {
			if (SColumns.contains(dr.getValue("no").toString())) {
				dt.Rows.add(dr);
			}
		}
		dt.TableName = "Columns";
		ds.Tables.add(dt);

		// 02.添加枚举
		SysEnums ens = new SysEnums(MapAttrAttr.MyDataType);
		ds.Tables.add(ens.ToDataTableField("EnumsDataType"));
		ens = new SysEnums(MapAttrAttr.LGType);
		ds.Tables.add(ens.ToDataTableField("EnumsLGType"));

		return bp.tools.Json.ToJson(ds);

	}

	public final String Imp_Src_Step3_Save() throws Exception {

		String hidImpFields = this.GetRequestVal("hidImpFields");
		String[] fields = StringHelper.trimEnd(hidImpFields, ',').split("[,]", -1);

		MapData md = new MapData();
		md.setNo(this.getFK_MapData());
		md.RetrieveFromDBSources();


		String msg = "导入字段信息:";
		boolean isLeft = true;
		for (int i = 0; i < fields.length; i++)
		{
			String colname = fields[i];

			MapAttr ma = new MapAttr();
			ma.setKeyOfEn(colname);
			ma.setName(this.GetRequestVal("TB_Desc_" + colname));
			ma.setFK_MapData(this.getFK_MapData());
			ma.setMyDataType(Integer.parseInt(this.GetRequestVal("DDL_DBType_" + colname)));
			ma.setMaxLen(Integer.parseInt(this.GetRequestVal("TB_Len_" + colname)));
			ma.setUIBindKey(this.GetRequestVal("TB_BindKey_" + colname));
			ma.setMyPK(this.getFK_MapData() + "_" + ma.getKeyOfEn());
			ma.setLGType(FieldTypeS.Normal);

			if (!ma.getUIBindKey().equals(""))
			{
				SysEnums se = new SysEnums();
				se.Retrieve(SysEnumAttr.EnumKey, ma.getUIBindKey(), null);
				if (!se.isEmpty())
				{
					ma.setMyDataType(DataType.AppInt);
					ma.setLGType(FieldTypeS.Enum);
					ma.setUIContralType(UIContralType.DDL);
				}

				SFTable tb = new SFTable();
				tb.setNo(ma.getUIBindKey());
				if (tb.getIsExits() == true)
				{
					ma.setMyDataType(DataType.AppString);
					ma.setLGType(FieldTypeS.FK);
					ma.setUIContralType(UIContralType.DDL);
				}
			}

			if (ma.getMyDataType() == DataType.AppBoolean)
			{
				ma.setUIContralType ( UIContralType.CheckBok);
			}
			if (ma.getIsExits())
			{
				continue;
			}
			ma.Insert();

			msg += "\t\n字段:" + ma.getKeyOfEn() + "" + ma.getName() + "加入成功.";

			isLeft = !isLeft;
		}


		return msg;

	}

		///#endregion


}