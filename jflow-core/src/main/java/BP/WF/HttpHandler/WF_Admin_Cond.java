package BP.WF.HttpHandler;

import java.util.ArrayList;

import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.En.Entities;
import BP.En.EntityMultiTree;
import BP.En.FieldTypeS;
import BP.En.QueryObject;
import BP.Port.Depts;
import BP.Port.Station;
import BP.Port.Stations;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.MapDataAttr;
import BP.Sys.MapDatas;
import BP.Sys.SysEnums;
import BP.Sys.SystemConfig;
import BP.WF.DotNetToJavaStringHelper;
import BP.WF.Flow;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.HttpHandler.Base.WebContralBase;
import BP.WF.Template.Cond;
import BP.WF.Template.CondAttr;
import BP.WF.Template.CondOrAnd;
import BP.WF.Template.CondType;
import BP.WF.Template.Conds;
import BP.WF.Template.ConnDataFrom;
import BP.WF.Template.FlowFormTrees;
import BP.WF.Template.FrmEnableRole;
import BP.WF.Template.FrmNode;
import BP.WF.Template.FrmNodeAttr;
import BP.WF.Template.FrmNodeExt;
import BP.WF.Template.FrmNodeExts;
import BP.WF.Template.FrmNodes;
import BP.WF.Template.SpecOperWay;
import BP.WF.Template.SysFormTree;
import BP.WF.Template.SysFormTreeAttr;
import BP.WF.Template.SysFormTrees;

/**
 * 页面功能实体
 */
public class WF_Admin_Cond extends WebContralBase {
	
	
	/**
	 * 构造函数
	 */
	public WF_Admin_Cond()
	{
	
	}
	
	
	public final String getRefNo() {
		return this.GetRequestVal("RefNo");
	}
  
	/**
	 * 将实体转为树形
	 * 
	 * @param ens
	 * @param rootNo
	 * @param checkIds
	 */
	private StringBuilder appendMenus = new StringBuilder();
	private StringBuilder appendMenuSb = new StringBuilder();

	public final void TansEntitiesToGenerTree(Entities ens, String rootNo,
			String checkIds) {
		Object tempVar = ens.GetEntityByKey(rootNo);
		EntityMultiTree root = (EntityMultiTree) ((tempVar instanceof EntityMultiTree) ? tempVar
				: null);
		if (root == null) {
			throw new RuntimeException("@没有找到rootNo=" + rootNo + "的entity.");
		}
		appendMenus.append("[{");
		appendMenus.append("\"id\":\"" + rootNo + "\"");
		appendMenus.append(",\"text\":\"" + root.getName() + "\"");
		appendMenus.append(",\"state\":\"open\"");

		// attributes
		BP.WF.Template.FlowFormTree formTree = (BP.WF.Template.FlowFormTree) ((root instanceof BP.WF.Template.FlowFormTree) ? root
				: null);
		if (formTree != null) {
			String url = formTree.getUrl() == null ? "" : formTree.getUrl();
			url = url.replace("/", "|");
			appendMenus.append(",\"attributes\":{\"NodeType\":\""
					+ formTree.getNodeType() + "\",\"IsEdit\":\""
					+ formTree.getIsEdit() + "\",\"Url\":\"" + url + "\"}");
		}
		// 增加它的子级.
		appendMenus.append(",\"children\":");
		AddChildren(root, ens, checkIds);
		appendMenus.append(appendMenuSb);
		appendMenus.append("}]");
	}

	public final void AddChildren(EntityMultiTree parentEn, Entities ens,
			String checkIds) {
		appendMenus.append(appendMenuSb);
		appendMenuSb.delete(0, appendMenuSb.length());

		appendMenuSb.append("[");
		for (EntityMultiTree item : convertEntityMultiTree(ens)) {
			if (item.getParentNo() != null
					&& !item.getParentNo().equals(parentEn.getNo())) {
				continue;
			}

			if (checkIds.contains("," + item.getNo() + ",")) {
				appendMenuSb.append("{\"id\":\"" + item.getNo()
						+ "\",\"text\":\"" + item.getName()
						+ "\",\"checked\":true");
			} else {
				appendMenuSb.append("{\"id\":\"" + item.getNo()
						+ "\",\"text\":\"" + item.getName()
						+ "\",\"checked\":false");
			}

			// attributes
			BP.WF.Template.FlowFormTree formTree = (BP.WF.Template.FlowFormTree) ((item instanceof BP.WF.Template.FlowFormTree) ? item
					: null);
			if (formTree != null) {
				String url = formTree.getUrl() == null ? "" : formTree.getUrl();
				String ico = "icon-tree_folder";
				String treeState = "closed";
				url = url.replace("/", "|");
				appendMenuSb.append(",\"attributes\":{\"NodeType\":\""
						+ formTree.getNodeType() + "\",\"IsEdit\":\""
						+ formTree.getIsEdit() + "\",\"Url\":\"" + url + "\"}");
				// 图标
				if (formTree.getNodeType().equals("form")) {
					ico = "icon-sheet";
				}
				appendMenuSb.append(",\"state\":\"" + treeState + "\"");
				appendMenuSb.append(",iconCls:\"");
				appendMenuSb.append(ico);
				appendMenuSb.append("\"");
			}
			// 增加它的子级.
			appendMenuSb.append(",\"children\":");
			AddChildren(item, ens, checkIds);
			appendMenuSb.append("},");
		}
		if (appendMenuSb.length() > 1) {
			appendMenuSb = appendMenuSb.deleteCharAt(appendMenuSb.length() - 1);
		}
		appendMenuSb.append("]");
		appendMenus.append(appendMenuSb);
		appendMenuSb.delete(0, appendMenuSb.length());
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<EntityMultiTree> convertEntityMultiTree(Object obj) {
		return (ArrayList<EntityMultiTree>) obj;
	}

	/**
	 * 打开方向条件的初始化. 到达的节点.
	 * 
	 * @return
	 */
	public final String ConditionLine_Init() {
		String sql = "SELECT A.NodeID, A.Name FROM WF_Node A,  WF_Direction B WHERE A.NodeID=B.ToNode AND B.Node="
				+ this.getFK_Node();

		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		dt.Columns.get(0).ColumnName = "NodeID";
		dt.Columns.get(1).ColumnName = "Name";

		return BP.Tools.Json.DataTableToJson(dt, false);
	}

	/**
	 * 初始化Init.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String Condition_Init() throws Exception {
		String toNodeID = this.GetRequestVal("ToNodeID");
		Cond cond = new Cond();
		cond.Retrieve(CondAttr.NodeID, this.getFK_Node(), CondAttr.ToNodeID,
				toNodeID);
		cond.getRow().put("HisDataFrom", cond.getHisDataFrom().toString());

		// cond.HisDataFrom
		// CurrentCond = DataFrom[cond.HisDataFrom];
		return cond.ToJson();
	}

	// /#region 方向条件 Frm 模版
	/**
	 * 初始化
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String CondByFrm_Init() throws Exception {
		DataSet ds = new DataSet();

		String fk_mainNode = this.GetRequestVal("FK_MainNode");
		String toNodeID = this.GetRequestVal("ToNodeID");

		Node nd = new Node(Integer.parseInt(fk_mainNode));

		CondType condTypeEnum = CondType.forValue(this
				.GetRequestValInt("CondType"));

		// string mypk = fk_mainNode + "_" + toNodeID + "_" + condTypeEnum + "_"
		// + ConnDataFrom.SQLTemplate.ToString();

		// 增加条件集合.
		Conds conds = new Conds();
		conds.Retrieve(CondAttr.FK_Node, fk_mainNode, CondAttr.ToNodeID,
				toNodeID);
		ds.Tables.add(conds.ToDataTableField("WF_Conds"));
		
		//字段集合
		String noteIn = "'FID','PRI','PNodeID','PrjNo', 'PrjName', 'FK_NY','FlowDaySpan', 'MyNum','Rec','CDT','RDT','AtPara','WFSta','FlowNote','FlowStartRDT','FlowEnderRDT','FlowEnder','FlowSpanDays','WFState','OID','PWorkID','PFlowNo','PEmp','FlowEndNode','GUID'";

		// 增加字段集合.
		String sql = "";
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
			sql = "SELECT KeyOfEn as No, KeyOfEn||' - '||Name as Name FROM Sys_MapAttr WHERE FK_MapData='ND"
					+ Integer.parseInt(nd.getFK_Flow()) + "Rpt'";
			sql += " AND KeyOfEn Not IN(" + noteIn + ")";
			sql += " AND MyDataType NOT IN (6,7) ";
		} else if (SystemConfig.getAppCenterDBType() == DBType.MySQL) {
			sql = "SELECT KeyOfEn as No, concat(KeyOfEn , '-' , Name) as Name FROM Sys_MapAttr WHERE FK_MapData='ND"
					+ Integer.parseInt(nd.getFK_Flow()) + "Rpt'";
			sql += " AND KeyOfEn Not IN(" + noteIn + ")";
			sql += " AND MyDataType NOT IN (6,7) ";
		} else {
			sql = "SELECT KeyOfEn as No, KeyOfEn+' - '+Name as Name FROM Sys_MapAttr WHERE FK_MapData='ND"
					+ Integer.parseInt(nd.getFK_Flow()) + "Rpt'";
			sql += " AND KeyOfEn Not IN(" + noteIn + ")";
			sql += " AND MyDataType NOT IN (6,7) ";
		}

		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapAttr";
		dt.Columns.get(0).ColumnName = "No";
		dt.Columns.get(1).ColumnName = "Name";

		DataRow dr = dt.NewRow();
		dr.setValue(0, "all");
		dr.setValue(1, "请选择表单字段");
		dt.Rows.add(dr);
		ds.Tables.add(dt);

		return BP.Tools.Json.ToJson(ds); // cond.ToJson();
	}

	// /#region 方向条件岗位
	/**
	 * 初始化
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String CondByStation_Init() throws Exception {
		DataSet ds = new DataSet();

		// 岗位类型.
		BP.GPM.StationTypes tps = new BP.GPM.StationTypes();
		tps.RetrieveAll();
		ds.Tables.add(tps.ToDataTableField("StationTypes"));

		// 岗位.
		BP.GPM.Stations sts = new BP.GPM.Stations();
		sts.RetrieveAll();
		ds.Tables.add(sts.ToDataTableField("Stations"));

		// 取有可能存盘的数据.
		int FK_MainNode = this.GetRequestValInt("FK_MainNode");
		int ToNodeID = this.GetRequestValInt("ToNodeID");
		Cond cond = new Cond();
		String mypk = FK_MainNode + "_" + ToNodeID + "_Dir_"
				+ ConnDataFrom.Stas.toString();
		cond.setMyPK(mypk);
		cond.RetrieveFromDBSources();
		ds.Tables.add(cond.ToDataTableField("Cond"));

		return BP.Tools.Json.ToJson(ds);
	}

	public final String CondByFrm_InitField() throws Exception {
		// 定义数据容器.
		DataSet ds = new DataSet();

		// 字段属性.
		MapAttr attr = new MapAttr();
		attr.setMyPK("ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt_"
				+ this.getKeyOfEn());
		attr.Retrieve();

		ds.Tables.add(attr.ToDataTableField("Sys_MapAttr"));

		if (attr.getLGType() == FieldTypeS.Enum) {
			SysEnums ses = new SysEnums(attr.getUIBindKey());
			ds.Tables.add(ses.ToDataTableField("Enums"));
		}

		// /#region 增加操作符 number.
		if (attr.getIsNum()) {
			DataTable dtOperNumber = new DataTable();
			dtOperNumber.TableName = "Opers";
			dtOperNumber.Columns.Add("No", String.class);
			dtOperNumber.Columns.Add("Name", String.class);

			DataRow dr = dtOperNumber.NewRow();
			dr.setValue("No", "dengyu");
			dr.setValue("Name", "= 等于");
			dtOperNumber.Rows.add(dr);

			dr = dtOperNumber.NewRow();
			dr.setValue("No", "dayu");
			dr.setValue("Name", " > 大于");
			dtOperNumber.Rows.add(dr);

			dr = dtOperNumber.NewRow();
			dr.setValue("No", "dayudengyu");
			dr.setValue("Name", " >= 大于等于");
			dtOperNumber.Rows.add(dr);

			dr = dtOperNumber.NewRow();
			dr.setValue("No", "xiaoyu");
			dr.setValue("Name", " < 小于");
			dtOperNumber.Rows.add(dr);

			dr = dtOperNumber.NewRow();
			dr.setValue("No", "xiaoyudengyu");
			dr.setValue("Name", " <= 小于等于");
			dtOperNumber.Rows.add(dr);

			dr = dtOperNumber.NewRow();
			dr.setValue("No", "budengyu");
			dr.setValue("Name", " != 不等于");
			dtOperNumber.Rows.add(dr);

			ds.Tables.add(dtOperNumber);
		} else {
			// /#region 增加操作符 string.
			DataTable dtOper = new DataTable();
			dtOper.TableName = "Opers";
			dtOper.Columns.Add("No", String.class);
			dtOper.Columns.Add("Name", String.class);

			DataRow dr = dtOper.NewRow();
			dr.setValue("No", "dengyu");
			dr.setValue("Name", "= 等于");
			dtOper.Rows.add(dr);

			dr = dtOper.NewRow();
			dr.setValue("No", "like");
			dr.setValue("Name", " LIKE 包含");
			dtOper.Rows.add(dr);

			dr = dtOper.NewRow();
			dr.setValue("No", "budengyu");
			dr.setValue("Name", " != 不等于");
			dtOper.Rows.add(dr);
			ds.Tables.add(dtOper);
			// /#endregion 增加操作符 string.
		}
		// /#endregion 增加操作符 number.

		return BP.Tools.Json.ToJson(ds); // cond.ToJson();
	}

	/**
	 * 按照部门条件计算.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String CondByDept_Init() throws Exception {
		DataSet ds = new DataSet();

		// 部门
		Depts depts = new Depts();
		depts.RetrieveAllFromDBSource();
		ds.Tables.add(depts.ToDataTableField("Depts"));

		// 取有可能存盘的数据.
		// int FK_MainNode = this.GetRequestValInt("FK_MainNode");
		// int ToNodeID = this.GetRequestValInt("ToNodeID");
		Cond cond = new Cond();
		// CondType condType =
		// CondType.forValue(this.GetRequestValInt("CondType"));
		// string mypk = this.GetRequestValInt("FK_MainNode") + "_" +
		// this.GetRequestValInt("ToNodeID") + "_" + condType.ToString() + "_" +
		// ConnDataFrom.Depts.ToString();
		cond.setMyPK(this.GetRequestVal("MyPK"));
		;
		cond.RetrieveFromDBSources();
		ds.Tables.add(cond.ToDataTableField("Cond"));

		return BP.Tools.Json.ToJson(ds);
	}

	/**
	 * 初始化
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String CondBySQL_Init() throws Exception {
		String fk_mainNode = this.GetRequestVal("FK_MainNode");
		String toNodeID = this.GetRequestVal("ToNodeID");

		CondType condTypeEnum = CondType.forValue(this
				.GetRequestValInt("CondType"));

		String mypk = fk_mainNode + "_" + toNodeID + "_" + condTypeEnum + "_"
				+ ConnDataFrom.SQL.toString();

		Cond cond = new Cond();
		cond.setMyPK(mypk);
		cond.RetrieveFromDBSources();

		return cond.ToJson();
	}

	/**
	 * 初始化
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String CondBySQLTemplate_Init() throws Exception {
		String fk_mainNode = this.GetRequestVal("FK_MainNode");
		String toNodeID = this.GetRequestVal("ToNodeID");

		CondType condTypeEnum = CondType.forValue(this
				.GetRequestValInt("CondType"));

		String mypk = fk_mainNode + "_" + toNodeID + "_" + condTypeEnum + "_"
				+ ConnDataFrom.SQLTemplate.toString();

		Cond cond = new Cond();
		cond.setMyPK(mypk);
		cond.RetrieveFromDBSources();

		return cond.ToJson();
	}

	// /#region 方向条件Para
	/**
	 * 初始化
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String CondByPara_Init() throws Exception {
		String fk_mainNode = this.GetRequestVal("FK_MainNode");
		String toNodeID = this.GetRequestVal("ToNodeID");

		CondType condTypeEnum = CondType.forValue(this
				.GetRequestValInt("CondType"));

		String mypk = fk_mainNode + "_" + toNodeID + "_" + condTypeEnum + "_"
				+ ConnDataFrom.Paras.toString();

		Cond cond = new Cond();
		cond.setMyPK(mypk);
		cond.RetrieveFromDBSources();

		return cond.ToJson();
	}

	public final String CondStation_Save() throws Exception {
		int FK_MainNode = this.GetRequestValInt("FK_MainNode");
		int ToNodeID = this.GetRequestValInt("ToNodeID");
		CondType HisCondType = CondType.Dir;

		Cond cond = new Cond();
		cond.Delete(CondAttr.NodeID, FK_MainNode, CondAttr.ToNodeID, ToNodeID,
				CondAttr.CondType, HisCondType.getValue());

		String mypk = FK_MainNode + "_" + ToNodeID + "_Dir_"
				+ ConnDataFrom.Stas.toString();

		// 删除岗位条件.
		cond.setMyPK(mypk);
		if (cond.RetrieveFromDBSources() == 0) {
			cond.setHisDataFrom(ConnDataFrom.Stas);
			cond.setNodeID(FK_MainNode);
			cond.setFK_Flow(this.getFK_Flow());
			cond.setToNodeID(ToNodeID);
			cond.Insert();
		}

		String val = "";
		Stations sts = new Stations();
		sts.RetrieveAllFromDBSource();
		for (Station st : sts.ToJavaList()) {
			if (!this.GetRequestVal("CB_" + st.getNo()).equals("1")) {
				continue;
			}
			val += "@" + st.getNo();
		}

		val += "@";
		cond.setOperatorValue(val);
		cond.setHisDataFrom(ConnDataFrom.Stas);
		cond.setFK_Flow(this.getFK_Flow());
		cond.setHisCondType(CondType.Dir);
		cond.setFK_Node(FK_MainNode);

		// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		// /#region //获取"指定的操作员"设置，added by liuxc,2015-10-7
		cond.setSpecOperWay(SpecOperWay.forValue(this.GetRequestValInt("DDL_"
				+ CondAttr.SpecOperWay)));

		if (cond.getSpecOperWay() != SpecOperWay.CurrOper) {
			cond.setSpecOperPara(this.GetRequestVal("TB_"
					+ CondAttr.SpecOperPara));
		} else {
			cond.setSpecOperPara("");
		}
		// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		// /#endregion

		cond.setToNodeID(ToNodeID);
		cond.Update();

		return "保存成功..";
	}

	/**
	 * 保存
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String CondByUrl_Save() throws Exception {
		String fk_mainNode = this.GetRequestVal("FK_MainNode");
		String toNodeID = this.GetRequestVal("ToNodeID");
		CondType condTypeEnum = CondType.forValue(this
				.GetRequestValInt("CondType"));
		String mypk = fk_mainNode + "_" + toNodeID + "_" + condTypeEnum + "_"
				+ ConnDataFrom.Url.toString();

		String sql = this.GetRequestVal("TB_Docs");

		Cond cond = new Cond();
		cond.Delete(CondAttr.NodeID, fk_mainNode, CondAttr.ToNodeID, toNodeID,
				CondAttr.CondType, condTypeEnum.getValue());

		cond.setMyPK(mypk);
		cond.setHisDataFrom(ConnDataFrom.Url);

		cond.setNodeID(this.GetRequestValInt("FK_MainNode"));
		cond.setFK_Node(this.GetRequestValInt("FK_MainNode"));
		cond.setToNodeID(this.GetRequestValInt("ToNodeID"));

		cond.setFK_Flow(this.getFK_Flow());
		cond.setOperatorValue(sql);
		cond.setNote(this.GetRequestVal("TB_Note")); // 备注.

		cond.setFK_Flow(this.getFK_Flow());
		cond.setHisCondType(condTypeEnum);
		cond.Insert();

		return "保存成功..";
	}

	/**
	 * 保存
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String CondByFrm_Save() throws Exception {
		// 定义变量.
		String field = this.GetRequestVal("DDL_Fields");
		field = "ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt_" + field;

		int toNodeID = this.GetRequestValInt("ToNodeID");
		int fk_Node = this.GetRequestValInt("FK_Node");
		String oper = this.GetRequestVal("DDL_Operator");

		String operVal = this.GetRequestVal("OperVal");

		String saveType = this.GetRequestVal("SaveType"); // 保存类型.
		CondType condTypeEnum = CondType.forValue(this
				.GetRequestValInt("CondType"));

		// 把其他的条件都删除掉.
		DBAccess.RunSQL("DELETE FROM WF_Cond WHERE ( NodeID="
				+ this.getFK_Node() + " AND ToNodeID=" + toNodeID
				+ ") AND DataFrom!=" + ConnDataFrom.NodeForm.getValue());

		Cond cond = new Cond();
		cond.setHisDataFrom(ConnDataFrom.NodeForm);
		cond.setNodeID(fk_Node);
		cond.setToNodeID(toNodeID);

		cond.setFK_Node(this.getFK_Node());
		cond.setFK_Operator(oper);
		cond.setOperatorValue(operVal); // 操作值.

		cond.setFK_Attr(field); // 字段属性.

		// cond.OperatorValueT = ""; // this.GetOperValText;
		cond.setFK_Flow(this.getFK_Flow());
		cond.setHisCondType(condTypeEnum);

		if (saveType.equals("AND")) {
			cond.setCondOrAnd(CondOrAnd.ByAnd);
		} else {
			cond.setCondOrAnd(CondOrAnd.ByOr);
		}

		// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		// /#region 方向条件，全部更新.
		Conds conds = new Conds();
		QueryObject qo = new QueryObject(conds);
		qo.AddWhere(CondAttr.NodeID, this.getFK_Node());
		qo.addAnd();
		qo.AddWhere(CondAttr.DataFrom, ConnDataFrom.NodeForm.getValue());
		qo.addAnd();
		qo.AddWhere(CondAttr.CondType, condTypeEnum.getValue());
		if (toNodeID != 0) {
			qo.addAnd();
			qo.AddWhere(CondAttr.ToNodeID, toNodeID);
		}
		int num = qo.DoQuery();
		for (Cond item : conds.ToJavaList()) {
			item.setCondOrAnd(cond.getCondOrAnd());
			item.Update();
		}
		// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		// /#endregion

		// 执行同步
		String sqls = "UPDATE WF_Node SET IsCCFlow=0";
		sqls += "@UPDATE WF_Node  SET IsCCFlow=1 WHERE NodeID IN (SELECT NODEID FROM WF_Cond a WHERE a.NodeID= NodeID AND CondType=1 )";
		BP.DA.DBAccess.RunSQLs(sqls);

		String sql = "UPDATE WF_Cond SET DataFrom="
				+ ConnDataFrom.NodeForm.getValue() + " WHERE NodeID="
				+ cond.getNodeID() + "  AND FK_Node=" + cond.getFK_Node()
				+ " AND ToNodeID=" + toNodeID;
		switch (condTypeEnum) {
		case Flow:
		case Node:
			cond.setMyPK(BP.DA.DBAccess.GenerOID() + ""); // cond.getNodeID() +
															// "_" +
															// cond.FK_Node +
															// "_" +
															// cond.FK_Attr +
															// "_" +
															// cond.OperatorValue;
			cond.Insert();
			BP.DA.DBAccess.RunSQL(sql);
			break;
		case Dir:
			// cond.setMyPK( cond.NodeID +"_"+
			// this.Request.QueryString["ToNodeID"]+"_" + cond.FK_Node + "_" +
			// cond.FK_Attr + "_" + cond.OperatorValue;
			cond.setMyPK(BP.DA.DBAccess.GenerOID() + ""); // cond.NodeID + "_" +
															// cond.FK_Node +
															// "_" +
															// cond.FK_Attr +
															// "_" +
															// cond.OperatorValue;
			cond.setToNodeID(toNodeID);
			cond.Insert();
			BP.DA.DBAccess.RunSQL(sql);
			break;
		case SubFlow: // 启动子流程.
			cond.setMyPK(BP.DA.DBAccess.GenerOID() + ""); // cond.NodeID + "_" +
															// cond.FK_Node +
															// "_" +
															// cond.FK_Attr +
															// "_" +
															// cond.OperatorValue;
			cond.setToNodeID(toNodeID);
			cond.Insert();
			BP.DA.DBAccess.RunSQL(sql);
			break;
		default:
			throw new RuntimeException("未设计的情况。" + condTypeEnum.toString());
		}

		return "保存成功!!";
	}

	public final String CondBySQLTemplate_Save() throws Exception {

		String fk_mainNode = this.GetRequestVal("FK_MainNode");
		String toNodeID = this.GetRequestVal("ToNodeID");
		CondType condTypeEnum = CondType.forValue(this
				.GetRequestValInt("CondType"));
		String mypk = fk_mainNode + "_" + toNodeID + "_" + condTypeEnum + "_"
				+ ConnDataFrom.SQLTemplate.toString();

		String sql = this.GetRequestVal("TB_Docs");

		Cond cond = new Cond();
		cond.Delete(CondAttr.NodeID, fk_mainNode, CondAttr.ToNodeID, toNodeID,
				CondAttr.CondType, condTypeEnum.getValue());

		cond.setMyPK(mypk);
		cond.setHisDataFrom(ConnDataFrom.SQLTemplate);

		cond.setNodeID(this.GetRequestValInt("FK_MainNode"));
		cond.setFK_Node(this.GetRequestValInt("FK_MainNode"));
		cond.setToNodeID(this.GetRequestValInt("ToNodeID"));

		cond.setFK_Flow(this.getFK_Flow());
		cond.setOperatorValue(sql);
		cond.setNote(this.GetRequestVal("TB_Note")); // 备注.

		cond.setFK_Flow(this.getFK_Flow());
		cond.setHisCondType(condTypeEnum);
		cond.Insert();

		return "保存成功..";
	}

	public final String CondBySQL_Save() throws Exception {

		String fk_mainNode = this.GetRequestVal("FK_MainNode");
		String toNodeID = this.GetRequestVal("ToNodeID");
		CondType condTypeEnum = CondType.forValue(this
				.GetRequestValInt("CondType"));
		String mypk = fk_mainNode + "_" + toNodeID + "_" + condTypeEnum + "_"
				+ ConnDataFrom.SQL.toString();

		String sql = this.GetRequestVal("TB_Docs");

		Cond cond = new Cond();
		cond.Delete(CondAttr.NodeID, fk_mainNode, CondAttr.ToNodeID, toNodeID,
				CondAttr.CondType, condTypeEnum.getValue());

		cond.setMyPK(mypk);
		cond.setHisDataFrom(ConnDataFrom.SQL);

		cond.setNodeID(this.GetRequestValInt("FK_MainNode"));
		cond.setFK_Node(this.GetRequestValInt("FK_MainNode"));
		cond.setToNodeID(this.GetRequestValInt("ToNodeID"));

		cond.setFK_Flow(this.getFK_Flow());
		cond.setOperatorValue(sql);
		cond.setNote(this.GetRequestVal("TB_Note")); // 备注.

		cond.setFK_Flow(this.getFK_Flow());
		cond.setHisCondType(condTypeEnum);
		cond.Insert();

		return "保存成功..";
	}

	public final String CondByStation_Save() throws Exception {

		int FK_MainNode = this.GetRequestValInt("FK_MainNode");
		int ToNodeID = this.GetRequestValInt("ToNodeID");
		CondType HisCondType = CondType.Dir;

		Cond cond = new Cond();
		cond.Delete(CondAttr.NodeID, FK_MainNode, CondAttr.ToNodeID, ToNodeID,
				CondAttr.CondType, HisCondType.getValue());

		String mypk = FK_MainNode + "_" + ToNodeID + "_Dir_"
				+ ConnDataFrom.Stas.toString();

		// 删除岗位条件.
		cond.setMyPK(mypk);
		if (cond.RetrieveFromDBSources() == 0) {
			cond.setHisDataFrom(ConnDataFrom.Stas);
			cond.setNodeID(FK_MainNode);
			cond.setFK_Flow(this.getFK_Flow());
			cond.setToNodeID(ToNodeID);
			cond.Insert();
		}

		String val = this.GetRequestVal("emps").replace(",", "@");
		cond.setOperatorValue(val);
		cond.setSpecOperWay(SpecOperWay.forValue(this
				.GetRequestValInt("DDL_SpecOperWay")));
		if (cond.getSpecOperWay() != SpecOperWay.CurrOper) {
			cond.setSpecOperPara(this.GetRequestVal("TB_SpecOperPara"));
		} else {
			cond.setSpecOperPara("");
		}
		cond.setHisDataFrom(ConnDataFrom.Stas);
		cond.setFK_Flow(this.getFK_Flow());
		cond.setHisCondType(CondType.Dir);
		cond.setFK_Node(FK_MainNode);

		cond.setToNodeID(ToNodeID);
		cond.Update();

		return "保存成功..";
	}

	public final String CondByDept_Save() throws Exception {
		int FK_MainNode = this.GetRequestValInt("FK_MainNode");
		int ToNodeID = this.GetRequestValInt("ToNodeID");
		CondType condType = CondType
				.forValue(this.GetRequestValInt("CondType"));

		Cond cond = new Cond();
		cond.Delete(CondAttr.NodeID, this.GetRequestValInt("FK_MainNode"),
				CondAttr.ToNodeID, this.GetRequestValInt("ToNodeID"),
				CondAttr.CondType, condType.getValue());

		String mypk = this.GetRequestValInt("FK_MainNode") + "_"
				+ this.GetRequestValInt("ToNodeID") + "_" + condType.toString()
				+ "_" + ConnDataFrom.Depts.toString();
		cond.setMyPK(mypk);

		if (cond.RetrieveFromDBSources() == 0) {
			cond.setHisDataFrom(ConnDataFrom.Depts);
			cond.setNodeID(this.GetRequestValInt("FK_MainNode"));
			cond.setFK_Flow(this.getFK_Flow());
			cond.setToNodeID(this.GetRequestValInt("ToNodeID"));
			cond.Insert();
		}

		String val = this.GetRequestVal("depts").replace(",", "@");
		cond.setOperatorValue(val);
		cond.setSpecOperWay(SpecOperWay.forValue(this
				.GetRequestValInt("DDL_SpecOperWay")));
		if (cond.getSpecOperWay() != SpecOperWay.CurrOper) {
			cond.setSpecOperPara(this.GetRequestVal("TB_SpecOperPara"));
		} else {
			cond.setSpecOperPara("");
		}
		cond.setHisDataFrom(ConnDataFrom.Depts);
		cond.setFK_Flow(this.getFK_Flow());
		cond.setHisCondType(CondType.Dir);
		cond.setFK_Node(FK_MainNode);

		cond.setToNodeID(ToNodeID);
		cond.Update();

		// switch (condType)
		// {
		// case CondType.Flow:
		// case CondType.Node:
		// cond.Update();
		// break;
		// case CondType.Dir:
		// cond.setToNodeID(this.GetRequestValInt("ToNodeID");
		// cond.Update();
		// break;
		// case CondType.SubFlow:
		// cond.setToNodeID(this.GetRequestValInt("ToNodeID");
		// cond.Update();
		// break;
		// default:
		// throw new Exception("未设计的情况。");
		// }

		return "保存成功!!";
	}

	public final String CondByPara_Save() throws Exception {
		String fk_mainNode = this.GetRequestVal("FK_MainNode");
		String toNodeID = this.GetRequestVal("ToNodeID");
		CondType condTypeEnum = CondType.forValue(this
				.GetRequestValInt("CondType"));
		String mypk = fk_mainNode + "_" + toNodeID + "_" + condTypeEnum + "_"
				+ ConnDataFrom.Paras.toString();

		String sql = this.GetRequestVal("TB_Docs");

		Cond cond = new Cond();
		cond.Delete(CondAttr.NodeID, fk_mainNode, CondAttr.ToNodeID, toNodeID,
				CondAttr.CondType, condTypeEnum.getValue());

		cond.setMyPK(mypk);
		cond.setHisDataFrom(ConnDataFrom.Paras);

		cond.setNodeID(this.GetRequestValInt("FK_MainNode"));
		cond.setFK_Node(this.GetRequestValInt("FK_MainNode"));
		cond.setToNodeID(this.GetRequestValInt("ToNodeID"));

		cond.setFK_Flow(this.getFK_Flow());
		cond.setOperatorValue(sql);
		cond.setNote(this.GetRequestVal("TB_Note")); // 备注.

		cond.setFK_Flow(this.getFK_Flow());
		cond.setHisCondType(condTypeEnum);
		cond.Insert();

		return "保存成功..";
	}

	public final String CondPRI_Init() throws Exception {
		Conds cds = new Conds();
		cds.Retrieve(CondAttr.FK_Node, this.getFK_Node(), CondAttr.CondType, 2,
				CondAttr.PRI);

		for (Cond item : cds.ToJavaList()) {
			Node nd = new Node(item.getToNodeID());
			item.setNote(nd.getName());
		}

		return cds.ToJson();
	}

	public final String CondStation_Init() throws Exception {
		DataSet ds = new DataSet();

		// 岗位类型.
		BP.GPM.StationTypes tps = new BP.GPM.StationTypes();
		tps.RetrieveAll();
		ds.Tables.add(tps.ToDataTableField("StationTypes"));

		// 岗位.
		BP.GPM.Stations sts = new BP.GPM.Stations();
		sts.RetrieveAll();
		ds.Tables.add(sts.ToDataTableField("Stations"));

		// 取有可能存盘的数据.
		int FK_MainNode = this.GetRequestValInt("FK_MainNode");
		int ToNodeID = this.GetRequestValInt("ToNodeID");
		Cond cond = new Cond();
		String mypk = FK_MainNode + "_" + ToNodeID + "_Dir_"
				+ ConnDataFrom.Stas.toString();
		cond.setMyPK(mypk);
		cond.RetrieveFromDBSources();
		ds.Tables.add(cond.ToDataTableField("Cond"));

		return BP.Tools.Json.ToJson(ds);

	}

	public final String CondByUrl_Init() throws Exception {
		String fk_mainNode = this.GetRequestVal("FK_MainNode");
		String toNodeID = this.GetRequestVal("ToNodeID");

		CondType condTypeEnum = CondType.forValue(this
				.GetRequestValInt("CondType"));

		String mypk = fk_mainNode + "_" + toNodeID + "_" + condTypeEnum + "_"
				+ ConnDataFrom.Url.toString();

		Cond cond = new Cond();
		cond.setMyPK(mypk);
		cond.RetrieveFromDBSources();

		return cond.ToJson();
	}

	public final String CondByUrl_Delete() {
		String fk_mainNode = this.GetRequestVal("FK_MainNode");
		String toNodeID = this.GetRequestVal("ToNodeID");
		CondType condTypeEnum = CondType.forValue(this
				.GetRequestValInt("CondType"));

		String mypk = fk_mainNode + "_" + toNodeID + "_" + condTypeEnum + "_"
				+ ConnDataFrom.Url.toString();

		Cond deleteCond = new Cond();
		int i = deleteCond.Delete(CondAttr.NodeID, fk_mainNode,
				CondAttr.ToNodeID, toNodeID, CondAttr.CondType,
				condTypeEnum.getValue());

		if (i == 1) {
			return "删除成功..";
		}

		return "无可删除的数据.";
	}

	public final String CondByFrm_Delete() throws Exception {
		Cond deleteCond = new Cond();
		deleteCond.setMyPK(this.getMyPK());
		int i = deleteCond.Delete();
		if (i == 1) {
			return "删除成功..";
		}

		return "无可删除的数据.";
	}

	// ** 删除
	public final String CondBySQLTemplate_Delete() {
		String fk_mainNode = this.GetRequestVal("FK_MainNode");
		String toNodeID = this.GetRequestVal("ToNodeID");
		CondType condTypeEnum = CondType.forValue(this
				.GetRequestValInt("CondType"));

		String mypk = fk_mainNode + "_" + toNodeID + "_" + condTypeEnum + "_"
				+ ConnDataFrom.SQLTemplate.toString();

		Cond deleteCond = new Cond();
		int i = deleteCond.Delete(CondAttr.NodeID, fk_mainNode,
				CondAttr.ToNodeID, toNodeID, CondAttr.CondType,
				condTypeEnum.getValue());

		if (i == 1) {
			return "删除成功..";
		}

		return "无可删除的数据.";
	}

	// ** 删除
	public final String CondBySQL_Delete() {
		String fk_mainNode = this.GetRequestVal("FK_MainNode");
		String toNodeID = this.GetRequestVal("ToNodeID");
		CondType condTypeEnum = CondType.forValue(this
				.GetRequestValInt("CondType"));

		String mypk = fk_mainNode + "_" + toNodeID + "_" + condTypeEnum + "_"
				+ ConnDataFrom.SQL.toString();

		Cond deleteCond = new Cond();
		int i = deleteCond.Delete(CondAttr.NodeID, fk_mainNode,
				CondAttr.ToNodeID, toNodeID, CondAttr.CondType,
				condTypeEnum.getValue());

		if (i == 1) {
			return "删除成功..";
		}

		return "无可删除的数据.";
	}

	// ** 删除
	public final String CondByStation_Delete() {
		String fk_mainNode = this.GetRequestVal("FK_MainNode");
		String toNodeID = this.GetRequestVal("ToNodeID");
		CondType condTypeEnum = CondType.forValue(this
				.GetRequestValInt("CondType"));

		String mypk = fk_mainNode + "_" + toNodeID + "_" + condTypeEnum + "_"
				+ ConnDataFrom.SQL.toString();

		Cond deleteCond = new Cond();
		int i = deleteCond.Delete(CondAttr.NodeID, fk_mainNode,
				CondAttr.ToNodeID, toNodeID, CondAttr.CondType,
				condTypeEnum.getValue());

		if (i == 1) {
			return "删除成功..";
		}

		return "无可删除的数据.";
	}

	public final String CondByDept_Delete() {
		String fk_mainNode = this.GetRequestVal("FK_MainNode");
		String toNodeID = this.GetRequestVal("ToNodeID");
		CondType condTypeEnum = CondType.forValue(this
				.GetRequestValInt("CondType"));

		String mypk = fk_mainNode + "_" + toNodeID + "_" + condTypeEnum + "_"
				+ ConnDataFrom.SQL.toString();

		Cond deleteCond = new Cond();
		int i = deleteCond.Delete(CondAttr.NodeID, fk_mainNode,
				CondAttr.ToNodeID, toNodeID, CondAttr.CondType,
				condTypeEnum.getValue());

		if (i == 1) {
			return "删除成功..";
		}

		return "无可删除的数据.";
	}

	// ** 删除
	public final String CondByPara_Delete() {
		String fk_mainNode = this.GetRequestVal("FK_MainNode");
		String toNodeID = this.GetRequestVal("ToNodeID");
		CondType condTypeEnum = CondType.forValue(this
				.GetRequestValInt("CondType"));

		String mypk = fk_mainNode + "_" + toNodeID + "_" + condTypeEnum + "_"
				+ ConnDataFrom.Paras.toString();

		Cond deleteCond = new Cond();
		int i = deleteCond.Delete(CondAttr.NodeID, fk_mainNode,
				CondAttr.ToNodeID, toNodeID, CondAttr.CondType,
				condTypeEnum.getValue());

		if (i == 1) {
			return "删除成功..";
		}

		return "无可删除的数据.";
	}

	public final String CondPRI_Move() throws Exception {
		// C# TO JAVA CONVERTER NOTE: The following 'switch' operated on a
		// string member and was converted to Java 'if-else' logic:
		// switch (this.GetRequestVal("MoveType"))
		// ORIGINAL LINE: case "Up":
		if (this.GetRequestVal("MoveType").equals("Up")) {
			Cond up = new Cond(this.getMyPK());
			up.DoUp(this.getFK_Node());
			up.RetrieveFromDBSources();
			DBAccess.RunSQL("UPDATE WF_Cond SET PRI=" + up.getPRI()
					+ " WHERE ToNodeID=" + up.getToNodeID());
		}
		// ORIGINAL LINE: case "Down":
		else if (this.GetRequestVal("MoveType").equals("Down")) {
			Cond down = new Cond(this.getMyPK());
			down.DoDown(this.getFK_Node());
			down.RetrieveFromDBSources();
			DBAccess.RunSQL("UPDATE WF_Cond SET PRI=" + down.getPRI()
					+ " WHERE ToNodeID=" + down.getToNodeID());
		} else {
		}

		Conds cds = new Conds();
		cds.Retrieve(CondAttr.FK_Node, this.getFK_Node(), CondAttr.CondType, 2,
				CondAttr.PRI);
		return cds.ToJson();
	}

	// #region 独立表单的方向条件.
	// / <summary>
	// / 初始化
	// / </summary>
	// / <returns></returns>
	public String StandAloneFrm_Init() throws Exception {
		String sql = "SELECT m.No, m.Name, n.FK_Node, n.FK_Flow FROM WF_FrmNode n INNER JOIN Sys_MapData m ON n.FK_Frm=m.No WHERE n.FrmEnableRole!=5 AND n.FK_Node="
				+ this.getFK_Node();
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Frms";
		dt.Columns.get(0).ColumnName = "No";
		dt.Columns.get(1).ColumnName = "Name";

		DataRow dr = dt.NewRow();
		dr.setValue(0, "all");
		dr.setValue(1, "请选择表单");
		dt.Rows.add(dr);

		DataSet ds = new DataSet();
		ds.Tables.add(dt);

		// 增加条件集合.
		String fk_mainNode = this.GetRequestVal("FK_MainNode");
		String toNodeID = this.GetRequestVal("ToNodeID");
		Conds conds = new Conds();
		conds.Retrieve(CondAttr.FK_Node, fk_mainNode, CondAttr.ToNodeID,
				toNodeID);
		ds.Tables.add(conds.ToDataTableField("WF_Conds"));

		return BP.Tools.Json.ToJson(ds); // cond.ToJson();
	}

	// / <summary>
	// / 获得一个表单的字段.
	// / </summary>
	// / <returns></returns>
	public String StandAloneFrm_InitFrmAttr() throws Exception {
		String frmID = this.GetRequestVal("FrmID");
		MapAttrs attrs = new MapAttrs(frmID);
		return attrs.ToJson();
	}

	public String StandAloneFrm_Save() throws Exception {
		String frmID = this.GetRequestVal("FrmID");

		// 定义变量.
		String field = this.GetRequestVal("DDL_Fields");
		field = frmID + "_" + field;

		int toNodeID = this.GetRequestValInt("ToNodeID");
		int fk_Node = this.GetRequestValInt("FK_Node");
		String oper = this.GetRequestVal("DDL_Operator");

		String operVal = this.GetRequestVal("OperVal");

		// 节点,子线城,还是其他
		CondType condTypeEnum = CondType.forValue(this
				.GetRequestValInt("CondType"));

		// 把其他的条件都删除掉.
		DBAccess.RunSQL("DELETE FROM WF_Cond WHERE ( NodeID="
				+ this.getFK_Node() + " AND ToNodeID=" + toNodeID
				+ ") AND DataFrom!=" + ConnDataFrom.StandAloneFrm.getValue());

		Cond cond = new Cond();
		cond.setHisDataFrom(ConnDataFrom.StandAloneFrm);
		cond.setNodeID(fk_Node);
		cond.setToNodeID(toNodeID);

		cond.setFK_Node(this.getFK_Node());
		cond.setFK_Operator(oper);
		cond.setOperatorValue(operVal); // 操作值.

		cond.setFK_Attr(field); // 字段属性.

		// cond.OperatorValueT = ""; // this.GetOperValText;
		cond.setFK_Flow(this.getFK_Flow());
		cond.setHisCondType(condTypeEnum);

		; // 保存类型.
		if (this.GetRequestVal("SaveType").equals("AND") == true)
			cond.setCondOrAnd(CondOrAnd.ByAnd);
		else
			cond.setCondOrAnd(CondOrAnd.ByOr);

		// #region 方向条件，全部更新.
		Conds conds = new Conds();
		QueryObject qo = new QueryObject(conds);
		qo.AddWhere(CondAttr.NodeID, this.getFK_Node());
		qo.addAnd();
		qo.AddWhere(CondAttr.DataFrom, ConnDataFrom.StandAloneFrm.getValue());
		qo.addAnd();
		qo.AddWhere(CondAttr.CondType, condTypeEnum.getValue());
		if (toNodeID != 0) {
			qo.addAnd();
			qo.AddWhere(CondAttr.ToNodeID, toNodeID);
		}
		int num = qo.DoQuery();
		for (Cond item : conds.Tolist()) {
			item.setCondOrAnd(cond.getCondOrAnd());
			item.Update();
		}
		// #endregion

		/* 执行同步 */
		String sqls = "UPDATE WF_Node SET IsCCFlow=0";
		sqls += "@UPDATE WF_Node  SET IsCCFlow=1 WHERE NodeID IN (SELECT NODEID FROM WF_Cond a WHERE a.NodeID= NodeID AND CondType=1 )";
		BP.DA.DBAccess.RunSQLs(sqls);

		String sql = "UPDATE WF_Cond SET DataFrom="
				+ ConnDataFrom.StandAloneFrm.getValue() + " WHERE NodeID="
				+ cond.getNodeID() + "  AND FK_Node=" + cond.getFK_Node()
				+ " AND ToNodeID=" + toNodeID;

		switch (condTypeEnum) {
		case Flow:
		case Node:
			cond.setMyPK(BP.DA.DBAccess.GenerOID() + ""); // cond.getNodeID() +
															// "_" +
															// cond.FK_Node +
															// "_" +
															// cond.FK_Attr +
															// "_" +
															// cond.OperatorValue;
			cond.Insert();
			BP.DA.DBAccess.RunSQL(sql);
			break;
		case Dir:
			// cond.setMyPK( cond.NodeID +"_"+
			// this.Request.QueryString["ToNodeID"]+"_" + cond.FK_Node + "_" +
			// cond.FK_Attr + "_" + cond.OperatorValue;
			cond.setMyPK(BP.DA.DBAccess.GenerOID() + ""); // cond.NodeID + "_" +
															// cond.FK_Node +
															// "_" +
															// cond.FK_Attr +
															// "_" +
															// cond.OperatorValue;
			cond.setToNodeID(toNodeID);
			cond.Insert();
			BP.DA.DBAccess.RunSQL(sql);
			break;
		case SubFlow: // 启动子流程.
			cond.setMyPK(BP.DA.DBAccess.GenerOID() + ""); // cond.NodeID + "_" +
															// cond.FK_Node +
															// "_" +
															// cond.FK_Attr +
															// "_" +
															// cond.OperatorValue;
			cond.setToNodeID(toNodeID);
			cond.Insert();
			BP.DA.DBAccess.RunSQL(sql);
			break;
		default:
			throw new RuntimeException("未设计的情况。" + condTypeEnum.toString());
		}

		return "保存成功!!";
	}

	// / <summary>
	// / 删除
	// / </summary>
	// / <returns></returns>
	public String StandAloneFrm_Delete() throws Exception {
		Cond deleteCond = new Cond();
		deleteCond.setMyPK(this.getMyPK());
		int i = deleteCond.Delete();
		if (i == 1)
			return "删除成功..";

		return "err@无可删除的数据.";
	}

	public String StandAloneFrm_InitField() throws Exception {
		// 字段属性.
		MapAttr attr = new MapAttr();
		attr.setMyPK(this.getFrmID() + "_" + this.getKeyOfEn());
		attr.Retrieve();
		return AttrCond(attr);
	}

	private String AttrCond(MapAttr attr) throws Exception {

		// 定义数据容器.
		DataSet ds = new DataSet();

		ds.Tables.add(attr.ToDataTableField("Sys_MapAttr"));

		if (attr.getLGType() == FieldTypeS.Enum) {
			SysEnums ses = new SysEnums(attr.getUIBindKey());
			ds.Tables.add(ses.ToDataTableField("Enums"));
		}

		// /#region 增加操作符 number.
		if (attr.getIsNum()) {
			DataTable dtOperNumber = new DataTable();
			dtOperNumber.TableName = "Opers";
			dtOperNumber.Columns.Add("No", String.class);
			dtOperNumber.Columns.Add("Name", String.class);

			DataRow dr = dtOperNumber.NewRow();
			dr.setValue("No", "dengyu");
			dr.setValue("Name", "= 等于");
			dtOperNumber.Rows.add(dr);

			dr = dtOperNumber.NewRow();
			dr.setValue("No", "dayu");
			dr.setValue("Name", " > 大于");
			dtOperNumber.Rows.add(dr);

			dr = dtOperNumber.NewRow();
			dr.setValue("No", "dayudengyu");
			dr.setValue("Name", " >= 大于等于");
			dtOperNumber.Rows.add(dr);

			dr = dtOperNumber.NewRow();
			dr.setValue("No", "xiaoyu");
			dr.setValue("Name", " < 小于");
			dtOperNumber.Rows.add(dr);

			dr = dtOperNumber.NewRow();
			dr.setValue("No", "xiaoyudengyu");
			dr.setValue("Name", " <= 小于等于");
			dtOperNumber.Rows.add(dr);

			dr = dtOperNumber.NewRow();
			dr.setValue("No", "budengyu");
			dr.setValue("Name", " != 不等于");
			dtOperNumber.Rows.add(dr);

			ds.Tables.add(dtOperNumber);
		} else {
			// /#region 增加操作符 string.
			DataTable dtOper = new DataTable();
			dtOper.TableName = "Opers";
			dtOper.Columns.Add("No", String.class);
			dtOper.Columns.Add("Name", String.class);

			DataRow dr = dtOper.NewRow();
			dr.setValue("No", "dengyu");
			dr.setValue("Name", "= 等于");
			dtOper.Rows.add(dr);

			dr = dtOper.NewRow();
			dr.setValue("No", "like");
			dr.setValue("Name", " LIKE 包含");
			dtOper.Rows.add(dr);

			dr = dtOper.NewRow();
			dr.setValue("No", "budengyu");
			dr.setValue("Name", " != 不等于");
			dtOper.Rows.add(dr);
			ds.Tables.add(dtOper);
			// /#endregion 增加操作符 string.
		}
		// /#endregion 增加操作符 number.

		return BP.Tools.Json.ToJson(ds); // cond.ToJson();
	}
	// #endregion

}