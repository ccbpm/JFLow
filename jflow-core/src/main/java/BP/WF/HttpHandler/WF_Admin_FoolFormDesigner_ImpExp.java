package BP.WF.HttpHandler;

import BP.WF.*;
import BP.Sys.*;
import BP.Tools.StringHelper;
import BP.DA.*;
import BP.Difference.SystemConfig;
import BP.Difference.Handler.CommonFileUtils;
import BP.Difference.Handler.WebContralBase;

import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

public class WF_Admin_FoolFormDesigner_ImpExp extends WebContralBase {

	/**
	 * 构造函数
	 */
	public WF_Admin_FoolFormDesigner_ImpExp() {
	}

	/// #region 导入
	/**
	 * 初始化 导入的界面 .
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String Imp_Init() throws Exception {
		DataSet ds = new DataSet();

		String sql = "";
		DataTable dt;

		if (this.getFK_Flow() != null) {
			// 加入节点表单. 如果没有流程参数.

			Paras ps = new Paras();
			ps.SQL = "SELECT NodeID, Name  FROM WF_Node WHERE FK_Flow=" + SystemConfig.getAppCenterDBVarStr()
					+ "FK_Flow ORDER BY NODEID ";
			ps.Add("FK_Flow", this.getFK_Flow());
			dt = BP.DA.DBAccess.RunSQLReturnTable(ps);

			dt.TableName = "WF_Node";

			if (SystemConfig.getAppCenterDBType() == DBType.Oracle
					|| SystemConfig.getAppCenterDBType() == DBType.PostgreSQL) {
				dt.Columns.get("NODEID").ColumnName = "NodeID";
				dt.Columns.get("NAME").ColumnName = "Name";
			}

			ds.Tables.add(dt);
		}

		/// #region 加入表单库目录.
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
			sql = "SELECT NO as No ,Name,ParentNo FROM Sys_FormTree ORDER BY  PARENTNO, IDX ";
		} else {
			sql = "SELECT No,Name,ParentNo FROM Sys_FormTree ORDER BY  PARENTNO, IDX ";
		}

		dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_FormTree";
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle
				|| SystemConfig.getAppCenterDBType() == DBType.PostgreSQL) {
			dt.Columns.get("NO").ColumnName = "No";
			dt.Columns.get("NAME").ColumnName = "Name";
			dt.Columns.get("PARENTNO").ColumnName = "ParentNo";
		}
		ds.Tables.add(dt);

		// 加入表单
		sql = "SELECT A.No, A.Name, A.FK_FormTree  FROM Sys_MapData A, Sys_FormTree B WHERE A.FK_FormTree=B.No";
		dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapData";
		ds.Tables.add(dt);
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle
				|| SystemConfig.getAppCenterDBType() == DBType.PostgreSQL) {
			dt.Columns.get("NO").ColumnName = "No";
			dt.Columns.get("NAME").ColumnName = "Name";
			dt.Columns.get("FK_FORMTREE").ColumnName = "FK_FormTree";
		}

		/// #endregion 加入表单库目录.

		/// #region 加入流程树目录.
		sql = "SELECT No,Name,ParentNo FROM WF_FlowSort ORDER BY  PARENTNO, IDX ";

		dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_FlowSort";
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle
				|| SystemConfig.getAppCenterDBType() == DBType.PostgreSQL) {
			dt.Columns.get("NO").ColumnName = "No";
			dt.Columns.get("NAME").ColumnName = "Name";
			dt.Columns.get("PARENTNO").ColumnName = "ParentNo";
		}
		ds.Tables.add(dt);

		// 加入表单
		sql = "SELECT No, Name, FK_FlowSort  FROM WF_Flow ";
		dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_Flow";
		ds.Tables.add(dt);
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle
				|| SystemConfig.getAppCenterDBType() == DBType.PostgreSQL) {
			dt.Columns.get("NO").ColumnName = "No";
			dt.Columns.get("NAME").ColumnName = "Name";
			dt.Columns.get("FK_FLOWSORT").ColumnName = "FK_FlowSort";
		}

		/// #endregion 加入流程树目录.

		/// #region 数据源
		BP.Sys.SFDBSrcs ens = new BP.Sys.SFDBSrcs();
		ens.RetrieveAll();
		ds.Tables.add(ens.ToDataTableField("SFDBSrcs"));

		/// #endregion

		// 加入系统表.
		return BP.Tools.Json.ToJson(ds);
	}

	/**
	 * 从本机装载表单模版
	 * @return 执行结果
	 */
	public String Imp_LoadFrmTempleteFromLocalFile() throws Exception {
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
		// 读取上传的XML 文件.
		DataSet ds = new DataSet();
		ds.readXml(xmlFile.getAbsolutePath());
		// 执行装载.
		try{
			MapData.ImpMapData(fk_mapData, ds);
		}catch(Exception e){
			return e.getMessage();
		}

		if (fk_mapData.contains("ND")) {
			// 判断是否是节点表单
			int nodeID = 0;
			try {
				nodeID = Integer.parseInt(fk_mapData.replace("ND", ""));
			} catch (Exception e) {
				return "执行成功.";
			}

			Node nd = new Node(nodeID);
			Flow flow = new Flow(nd.getFK_Flow());
			nd.RepareMap(flow);
		}
		return "执行成功.";
	}

	/**
	 * 从流程上copy表单 @徐彪来调用.
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String Imp_CopyFromFlow() throws Exception {
		String ndfrm = "ND" + Integer.parseInt(this.getFK_Flow()) + "01";
		return Imp_CopyFrm(ndfrm);
	}

	/**
	 * 从表单库导入 从节点导入
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String Imp_FromsCopyFrm() throws Exception {
		return Imp_CopyFrm();
	}

	/**
	 * 从节点上Copy
	 * @return 执行结果
	 * @throws Exception
	 */

	public final String Imp_CopyFrm() throws Exception {
		return Imp_CopyFrm(null);
	}

	public final String Imp_CopyFrm(String frmID) throws Exception {
		try {
			String fromMapData = frmID;
			if (fromMapData == null) {
				fromMapData = this.GetRequestVal("FromFrmID");
			}

			boolean isClear = this.GetRequestValBoolen("IsClear");
			boolean isSetReadonly = this.GetRequestValBoolen("IsSetReadonly");

			MapData md = new MapData(fromMapData);
			MapData.ImpMapData(this.getFK_MapData(), BP.Sys.CCFormAPI.GenerHisDataSet_AllEleInfo(md.getNo()));

			// 设置为只读模式.
			if (isSetReadonly == true) {
				MapData.SetFrmIsReadonly(this.getFK_MapData());
			}

			// 如果是节点表单，就要执行一次修复，以免漏掉应该有的系统字段。
			if (this.getFK_MapData().contains("ND") == true) {
				String fk_node = this.getFK_MapData().replace("ND", "");
				Node nd = new Node(Integer.parseInt(fk_node));
				nd.RepareMap(nd.getHisFlow());
			}
			// 清空缓存
			MapData mymd = new MapData(fromMapData);
			mymd.RepairMap();
			SystemConfig.DoClearCash();
			return "执行成功.";
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}

	}

	/// #region 04.从外部数据源导入
	/**
	 * 选择一个数据源，进入步骤2
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String Imp_Src_Step2_Init() throws Exception {
		SFDBSrc src = new SFDBSrc(this.GetRequestVal("FK_SFDBSrc"));

		// 获取所有的表/视图
		DataTable dtTables = src.GetTables();

		return BP.Tools.FormatToJson.ToJson(dtTables);
	}

	/**
	 * 获取表字段
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String Imp_Src_Step2_GetColumns() throws Exception {
		DataSet ds = new DataSet();

		// 01.当前节点表单已经存在的列
		MapAttrs attrs = new MapAttrs(this.getFK_MapData());
		ds.Tables.add(attrs.ToDataTableField("MapAttrs"));

		// 02.数据源表中的列
		SFDBSrc src = new SFDBSrc(this.GetRequestVal("FK_SFDBSrc"));
		DataTable tableColumns = src.GetColumns(this.GetRequestVal("STable"));
		tableColumns.TableName = "TableColumns";
		ds.Tables.add(tableColumns);

		return BP.Tools.Json.ToJson(ds);
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
				// dt.ImportRow(dr);
			}
		}
		dt.TableName = "Columns";
		ds.Tables.add(dt);

		// 02.添加枚举
		SysEnums ens = new SysEnums(MapAttrAttr.MyDataType);
		ds.Tables.add(ens.ToDataTableField("EnumsDataType"));
		ens = new SysEnums(MapAttrAttr.LGType);
		ds.Tables.add(ens.ToDataTableField("EnumsLGType"));

		return BP.Tools.Json.ToJson(ds);

	}

	public final String Imp_Src_Step3_Save() throws Exception {

		String hidImpFields = this.GetRequestVal("hidImpFields");
		String[] fields = StringHelper.trimEnd(hidImpFields, ',').split("[,]", -1);

		MapData md = new MapData();
		md.setNo(this.getFK_MapData());
		md.RetrieveFromDBSources();

		String msg = "导入字段信息:";
		boolean isLeft = true;
		float maxEnd = md.getMaxEnd(); // 底部.
		for (int i = 0; i < fields.length; i++) {
			String colname = fields[i];

			MapAttr ma = new MapAttr();
			ma.setKeyOfEn(colname);
			ma.setName(this.GetRequestVal("TB_Desc_" + colname));
			ma.setFK_MapData(this.getFK_MapData());
			ma.setMyDataType(Integer.parseInt(this.GetRequestVal("DDL_DBType_" + colname)));
			ma.setMaxLen(Integer.parseInt(this.GetRequestVal("TB_Len_" + colname)));
			ma.setUIBindKey(this.GetRequestVal("TB_BindKey_" + colname));
			ma.setMyPK(this.getFK_MapData() + "_" + ma.getKeyOfEn());
			ma.setLGType(BP.En.FieldTypeS.Normal);

			if (!ma.getUIBindKey().equals("")) {
				SysEnums se = new SysEnums();
				se.Retrieve(SysEnumAttr.EnumKey, ma.getUIBindKey());
				if (se.size() > 0) {
					ma.setMyDataType(BP.DA.DataType.AppInt);
					ma.setLGType(BP.En.FieldTypeS.Enum);
					ma.setUIContralType(BP.En.UIContralType.DDL);
				}

				SFTable tb = new SFTable();
				tb.setNo(ma.getUIBindKey());
				if (tb.getIsExits() == true) {
					ma.setMyDataType(BP.DA.DataType.AppString);
					ma.setLGType(BP.En.FieldTypeS.FK);
					ma.setUIContralType(BP.En.UIContralType.DDL);
				}
			}

			if (ma.getMyDataType() == BP.DA.DataType.AppBoolean) {
				ma.setUIContralType(BP.En.UIContralType.CheckBok);
			}
			if (ma.getIsExits()) {
				continue;
			}
			ma.Insert();

			msg += "\t\n字段:" + ma.getKeyOfEn() + ma.getName() + "加入成功.";
			FrmLab lab = null;
			if (isLeft == true) {
				maxEnd = maxEnd + 40;
				/* 是否是左边 */
				lab = new FrmLab();
				lab.setMyPK(BP.DA.DBAccess.GenerGUID());
				lab.setFK_MapData(this.getFK_MapData());
				lab.setText(ma.getName());
				lab.setX(40);
				lab.setY(maxEnd);
				lab.Insert();

				ma.setX(lab.getX() + 80);
				ma.setY(maxEnd);
				ma.Update();
			} else {
				lab = new FrmLab();
				lab.setMyPK(BP.DA.DBAccess.GenerGUID());
				lab.setFK_MapData(this.getFK_MapData());
				lab.setText(ma.getName());
				lab.setX(350);
				lab.setY(maxEnd);
				lab.Insert();

				ma.setX(lab.getX() + 80);
				ma.setY(maxEnd);
				ma.Update();
			}
			isLeft = !isLeft;
		}

		// 重新设置.
		md.ResetMaxMinXY();

		return msg;

	}

	/// #endregion

	/// #endregion

}