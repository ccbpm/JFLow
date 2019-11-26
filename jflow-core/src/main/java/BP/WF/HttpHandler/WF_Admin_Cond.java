package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Difference.SystemConfig;
import BP.Difference.Handler.WebContralBase;
import BP.Sys.*;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Template.*;
import BP.WF.*;

/** 
 页面功能实体
*/
public class WF_Admin_Cond extends WebContralBase
{
	 /** 
	 构造函数
	 */
	public WF_Admin_Cond()
	{
	}


		///#region 方向优先级.
	/** 
	 初始化
	 
	 @return 
	 * @throws Exception 
	*/
	public final String CondPRI_Init() throws Exception
	{
		Conds cds = new Conds();
		cds.Retrieve(CondAttr.FK_Node, this.getFK_Node(), CondAttr.CondType, 2, CondAttr.PRI);

		for (Cond item : cds.ToJavaList())
		{
			Node nd = new Node(item.getToNodeID());
			item.setNote(nd.getName());
		}

		return cds.ToJson();
	}
	public final String CondPRI_Move() throws Exception
	{
		switch (this.GetRequestVal("MoveType"))
		{
			case "Up":
				Cond up = new Cond(this.getMyPK());
				up.DoUp(this.getFK_Node());
				up.RetrieveFromDBSources();
				DBAccess.RunSQL("UPDATE WF_Cond SET PRI=" + up.getPRI() + " WHERE ToNodeID=" + up.getToNodeID());
				break;
			case "Down":
				Cond down = new Cond(this.getMyPK());
				down.DoDown(this.getFK_Node());
				down.RetrieveFromDBSources();
				DBAccess.RunSQL("UPDATE WF_Cond SET PRI=" + down.getPRI() + " WHERE ToNodeID=" + down.getToNodeID());
				break;
			default:
				break;
		}


		Conds cds = new Conds();
		cds.Retrieve(CondAttr.FK_Node, this.getFK_Node(), CondAttr.CondType, 2, CondAttr.PRI);
		return cds.ToJson();
	}

		///#endregion 方向优先级.

	private Paras ps = new Paras();
	/** 
	 初始化Init.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Condition_Init() throws Exception
	{
		String toNodeID = this.GetRequestVal("ToNodeID");
		Cond cond = new Cond();
		cond.Retrieve(CondAttr.NodeID, this.getFK_Node(), CondAttr.ToNodeID, toNodeID);
		cond.getRow().put("HisDataFrom", cond.getHisDataFrom().toString());

		//   cond.HisDataFrom
		//CurrentCond = DataFrom[cond.HisDataFrom];
		return cond.ToJson();
	}

	/** 
	 打开方向条件的初始化.
	 到达的节点.
	 
	 @return 
	*/
	public final String ConditionLine_Init()
	{
		ps = new Paras();
		ps.SQL = "SELECT A.NodeID, A.Name FROM WF_Node A,  WF_Direction B WHERE A.NodeID=B.ToNode AND B.Node=" + SystemConfig.getAppCenterDBVarStr() + "Node";
		ps.Add("Node", this.getFK_Node());
		//string sql = "SELECT A.NodeID, A.Name FROM WF_Node A,  WF_Direction B WHERE A.NodeID=B.ToNode AND B.Node=" + this.FK_Node;

		DataTable dt = DBAccess.RunSQLReturnTable(ps);
		dt.Columns.get(0).setColumnName("NodeID");
		dt.Columns.get(1).setColumnName("Name");

		return BP.Tools.Json.ToJson(dt);
	}



		///#region 方向条件URL
	/** 
	 初始化
	 
	 @return 
	 * @throws Exception 
	*/
	public final String CondByUrl_Init() throws Exception
	{
		String fk_mainNode = this.GetRequestVal("FK_MainNode");
		String toNodeID = this.GetRequestVal("ToNodeID");

		CondType condTypeEnum = CondType.forValue(this.GetRequestValInt("CondType"));

		String mypk = fk_mainNode + "_" + toNodeID + "_" + condTypeEnum + "_" + ConnDataFrom.Url.toString();

		Cond cond = new Cond();
		cond.setMyPK(mypk);
		cond.RetrieveFromDBSources();

		return cond.ToJson();
	}
	/** 
	 保存
	 
	 @return 
	 * @throws Exception 
	*/
	public final String CondByUrl_Save() throws Exception
	{
		String fk_mainNode = this.GetRequestVal("FK_MainNode");
		String toNodeID = this.GetRequestVal("ToNodeID");
		CondType condTypeEnum = CondType.forValue(this.GetRequestValInt("CondType"));
		String mypk = fk_mainNode + "_" + toNodeID + "_" + condTypeEnum + "_" + ConnDataFrom.Url.toString();

		String sql = this.GetRequestVal("TB_Docs");

		//把其他的条件都删除掉.
		DBAccess.RunSQL("DELETE FROM WF_Cond WHERE (CondType=" + condTypeEnum.getValue() + " AND  NodeID=" + this.getFK_Node() + " AND ToNodeID=" + toNodeID + ") AND DataFrom!=" + ConnDataFrom.Url.getValue());

		Cond cond = new Cond();
		cond.Delete(CondAttr.NodeID, fk_mainNode, CondAttr.ToNodeID, toNodeID, CondAttr.CondType, condTypeEnum.getValue());

		cond.setMyPK(mypk);
		cond.setHisDataFrom(ConnDataFrom.Url);

		cond.setNodeID(this.GetRequestValInt("FK_MainNode"));
		cond.setFK_Node(this.GetRequestValInt("FK_MainNode"));
		cond.setToNodeID(this.GetRequestValInt("ToNodeID"));

		cond.setFK_Flow(this.getFK_Flow());
		cond.setOperatorValue(sql);
		cond.setNote(this.GetRequestVal("TB_Note")); //备注.

		cond.setFK_Flow(this.getFK_Flow());
		cond.setHisCondType(condTypeEnum);
		cond.Insert();

		return "保存成功..";
	}
	/** 
	 删除
	 
	 @return 
	 * @throws Exception 
	*/
	public final String CondByUrl_Delete() throws Exception
	{
		String fk_mainNode = this.GetRequestVal("FK_MainNode");
		String toNodeID = this.GetRequestVal("ToNodeID");
		CondType condTypeEnum = CondType.forValue(this.GetRequestValInt("CondType"));

		String mypk = fk_mainNode + "_" + toNodeID + "_" + condTypeEnum + "_" + ConnDataFrom.Url.toString();

		Cond deleteCond = new Cond();
		int i = deleteCond.Delete(CondAttr.NodeID, fk_mainNode, CondAttr.ToNodeID, toNodeID, CondAttr.CondType, condTypeEnum.getValue());

		if (i == 1)
		{
			return "删除成功..";
		}

		return "无可删除的数据.";
	}

		///#endregion


		///#region 方向条件 Frm 模版
	/** 
	 初始化
	 
	 @return 
	 * @throws Exception 
	 * @throws NumberFormatException 
	*/
	public final String CondByFrm_Init() throws NumberFormatException, Exception
	{
		DataSet ds = new DataSet();

		String fk_mainNode = this.GetRequestVal("FK_MainNode");
		String toNodeID = this.GetRequestVal("ToNodeID");

		Node nd = new Node(Integer.parseInt(fk_mainNode));

		CondType condTypeEnum = CondType.forValue(this.GetRequestValInt("CondType"));

		//string mypk = fk_mainNode + "_" + toNodeID + "_" + condTypeEnum + "_" + ConnDataFrom.SQLTemplate.ToString();

		//增加条件集合.  @sly
		Conds conds = new Conds();
		conds.Retrieve(CondAttr.FK_Node, Integer.parseInt(fk_mainNode), CondAttr.ToNodeID, Integer.parseInt(toNodeID));

		ds.Tables.add(conds.ToDataTableField("WF_Conds"));

		String noteIn = "'FID','PRI','PNodeID','PrjNo', 'PrjName', 'FK_NY','FlowDaySpan', 'MyNum','Rec','CDT','RDT','AtPara','WFSta','FlowNote','FlowStartRDT','FlowEnderRDT','FlowEnder','FlowSpanDays','WFState','OID','PWorkID','PFlowNo','PEmp','FlowEndNode','GUID'";

		//增加字段集合.
		String sql = "";
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			sql = "SELECT KeyOfEn as No, KeyOfEn||' - '||Name as Name FROM Sys_MapAttr WHERE FK_MapData='ND" + Integer.parseInt(nd.getFK_Flow()) + "Rpt'";
			sql += " AND KeyOfEn Not IN (" + noteIn + ") ";
			sql += " AND MyDataType NOT IN (6,7) ";
		}
		else if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
		{
			sql = "SELECT KeyOfEn as No, CONCAT(KeyOfEn,' - ', Name ) as Name FROM Sys_MapAttr WHERE FK_MapData='ND" + Integer.parseInt(nd.getFK_Flow()) + "Rpt'";
			sql += " AND KeyOfEn Not IN (" + noteIn + ") ";
			sql += " AND MyDataType NOT IN (6,7) ";
		}
		else
		{
			sql = "SELECT KeyOfEn as No, KeyOfEn+' - '+Name as Name FROM Sys_MapAttr WHERE FK_MapData='ND" + Integer.parseInt(nd.getFK_Flow()) + "Rpt'";
			sql += " AND KeyOfEn Not IN (" + noteIn + ") ";
			sql += " AND MyDataType NOT IN (6,7) ";
		}


		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapAttr";
		dt.Columns.get(0).setColumnName("No");
		dt.Columns.get(1).setColumnName("Name");

		DataRow dr = dt.NewRow();
		dr.setValue(0, "all");
		dr.setValue(1, "请选择表单字段");
		dt.Rows.add(dr);
		ds.Tables.add(dt);

		return BP.Tools.Json.ToJson(ds); // cond.ToJson();
	}
	public final String CondByFrm_InitField() throws NumberFormatException, Exception
	{
		//字段属性.
		MapAttr attr = new MapAttr();
		attr.setMyPK("ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt_" + this.getKeyOfEn());
		attr.Retrieve();
		return AttrCond(attr);
	}
	/** 
	 保存
	 
	 @return 
	 * @throws Exception 
	*/
	public final String CondByFrm_Save() throws Exception
	{
		//定义变量.
		String field = this.GetRequestVal("DDL_Fields");
		field = "ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt_" + field;

		int toNodeID = this.GetRequestValInt("ToNodeID");
		int fk_Node = this.GetRequestValInt("FK_Node");
		String oper = this.GetRequestVal("DDL_Operator");

		String operVal = this.GetRequestVal("OperVal");
		String operValT = this.GetRequestVal("OperValText");

		String saveType = this.GetRequestVal("SaveType"); //保存类型.
		CondType condTypeEnum = CondType.forValue(this.GetRequestValInt("CondType"));

		//把其他的条件都删除掉.
		DBAccess.RunSQL("DELETE FROM WF_Cond WHERE (CondType=" + condTypeEnum.getValue() + " AND  NodeID=" + this.getFK_Node() + " AND ToNodeID=" + toNodeID + ") AND DataFrom!=" + ConnDataFrom.NodeForm.getValue());

		Cond cond = new Cond();
		cond.setHisDataFrom(ConnDataFrom.NodeForm);
		cond.setNodeID(fk_Node);
		cond.setToNodeID(toNodeID);

		cond.setFK_Node(this.getFK_Node());
		cond.setFK_Operator(oper);
		cond.setOperatorValue(operVal); //操作值.
		cond.setOperatorValueT(operValT);

		cond.setFK_Attr(field); //字段属性.

		//  cond.OperatorValueT = ""; // this.GetOperValText;
		cond.setFK_Flow(this.getFK_Flow());
		cond.setHisCondType(condTypeEnum);

		if (saveType.equals("AND"))
		{
			cond.setCondOrAnd(CondOrAnd.ByAnd);
		}
		else
		{
			cond.setCondOrAnd(CondOrAnd.ByOr);
		}


			///#region 方向条件，全部更新.
		Conds conds = new Conds();
		QueryObject qo = new QueryObject(conds);
		qo.AddWhere(CondAttr.NodeID, this.getFK_Node());
		qo.addAnd();
		qo.AddWhere(CondAttr.DataFrom, ConnDataFrom.NodeForm.getValue());
		qo.addAnd();
		qo.AddWhere(CondAttr.CondType, condTypeEnum.getValue());
		if (toNodeID != 0)
		{
			qo.addAnd();
			qo.AddWhere(CondAttr.ToNodeID, toNodeID);
		}
		int num = qo.DoQuery();
		for (Cond item : conds.ToJavaList())
		{
			item.setCondOrAnd(cond.getCondOrAnd());
			item.Update();
		}

			///#endregion

		/* 执行同步*/
		String sqls = "UPDATE WF_Node SET IsCCFlow=0";
		sqls += "@UPDATE WF_Node  SET IsCCFlow=1 WHERE NodeID IN (SELECT NODEID FROM WF_Cond a WHERE a.NodeID= NodeID AND CondType=1 )";
		BP.DA.DBAccess.RunSQLs(sqls);

		String sql = "UPDATE WF_Cond SET DataFrom=" + ConnDataFrom.NodeForm.getValue() + " WHERE NodeID=" + cond.getNodeID() + "  AND FK_Node=" + cond.getFK_Node() + " AND ToNodeID=" + toNodeID;
		switch (condTypeEnum)
		{
			case Flow:
			case Node:
				cond.setMyPK(String.valueOf(DBAccess.GenerOID())); //cond.NodeID + "_" + cond.FK_Node + "_" + cond.FK_Attr + "_" + cond.OperatorValue;
				cond.Insert();
				BP.DA.DBAccess.RunSQL(sql);
				break;
			case Dir:
				// cond.setMyPK( cond.NodeID +"_"+ this.Request.QueryString["ToNodeID"]+"_" + cond.FK_Node + "_" + cond.FK_Attr + "_" + cond.OperatorValue;
				cond.setMyPK(String.valueOf(DBAccess.GenerOID())); //cond.NodeID + "_" + cond.FK_Node + "_" + cond.FK_Attr + "_" + cond.OperatorValue;
				cond.setToNodeID(toNodeID);
				cond.Insert();
				BP.DA.DBAccess.RunSQL(sql);
				break;
			case SubFlow: //启动子流程.
				cond.setMyPK(String.valueOf(DBAccess.GenerOID())); //cond.NodeID + "_" + cond.FK_Node + "_" + cond.FK_Attr + "_" + cond.OperatorValue;
				cond.setToNodeID(toNodeID);
				cond.Insert();
				BP.DA.DBAccess.RunSQL(sql);
				break;
			default:
				throw new RuntimeException("未设计的情况。" + condTypeEnum.toString());
		}

		return "保存成功!!";
	}

		///#endregion 方向条件 Frm 模版


		///#region 独立表单的方向条件.
	/** 
	 初始化
	 
	 @return 
	 * @throws Exception 
	*/
	public final String StandAloneFrm_Init() throws Exception
	{
		ps = new Paras();
		ps.SQL = "SELECT m.No, m.Name, n.FK_Node, n.FK_Flow FROM WF_FrmNode n INNER JOIN Sys_MapData m ON n.FK_Frm=m.No WHERE n.FrmEnableRole!=5 AND n.FK_Node=" + SystemConfig.getAppCenterDBVarStr() + "FK_Node";
		ps.Add("FK_Node",this.getFK_Node());
		//string sql = "SELECT m.No, m.Name, n.FK_Node, n.FK_Flow FROM WF_FrmNode n INNER JOIN Sys_MapData m ON n.FK_Frm=m.No WHERE n.FrmEnableRole!=5 AND n.FK_Node=" + this.FK_Node;
		DataTable dt = DBAccess.RunSQLReturnTable(ps);
		dt.TableName = "Frms";
		dt.Columns.get(0).setColumnName("No");
		dt.Columns.get(1).setColumnName("Name");

		DataRow dr = dt.NewRow();
		dr.setValue(0, "all");
		dr.setValue(1, "请选择表单");
		dt.Rows.add(dr);

		DataSet ds = new DataSet();
		ds.Tables.add(dt);

		//增加条件集合.
		String fk_mainNode = this.GetRequestVal("FK_MainNode");
		String toNodeID = this.GetRequestVal("ToNodeID");
		Conds conds = new Conds();
		conds.Retrieve(CondAttr.FK_Node, fk_mainNode, CondAttr.ToNodeID, toNodeID);
		ds.Tables.add(conds.ToDataTableField("WF_Conds"));

		return BP.Tools.Json.ToJson(ds); // cond.ToJson();
	}
	/** 
	 获得一个表单的字段.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String StandAloneFrm_InitFrmAttr() throws Exception
	{
		String frmID = this.GetRequestVal("FrmID");
		MapAttrs attrs = new MapAttrs(frmID);
		return attrs.ToJson();
	}
	public final String StandAloneFrm_Save() throws Exception
	{
		String frmID = this.GetRequestVal("FrmID");

		//定义变量.
		String field = this.GetRequestVal("DDL_Fields");
		field = frmID + "_" + field;

		int toNodeID = this.GetRequestValInt("ToNodeID");
		int fk_Node = this.GetRequestValInt("FK_Node");
		String oper = this.GetRequestVal("DDL_Operator");

		String operVal = this.GetRequestVal("OperVal");

		//节点,子线城,还是其他
		CondType condTypeEnum = CondType.forValue(this.GetRequestValInt("CondType"));

		//把其他的条件都删除掉.
		DBAccess.RunSQL("DELETE FROM WF_Cond WHERE (CondType=" + condTypeEnum.getValue() + " AND  NodeID=" + this.getFK_Node() + " AND ToNodeID=" + toNodeID + ") AND DataFrom!=" + ConnDataFrom.StandAloneFrm.getValue());

		Cond cond = new Cond();
		cond.setHisDataFrom(ConnDataFrom.StandAloneFrm);
		cond.setNodeID(fk_Node);
		cond.setToNodeID(toNodeID);

		cond.setFK_Node(this.getFK_Node());
		cond.setFK_Operator(oper);
		cond.setOperatorValue(operVal); //操作值.

		cond.setFK_Attr(field); //字段属性.

		//  cond.OperatorValueT = ""; // this.GetOperValText;
		cond.setFK_Flow(this.getFK_Flow());
		cond.setHisCondType(condTypeEnum);

		; //保存类型.
		if (this.GetRequestVal("SaveType").equals("AND") == true)
		{
			cond.setCondOrAnd(CondOrAnd.ByAnd);
		}
		else
		{
			cond.setCondOrAnd(CondOrAnd.ByOr);
		}


			///#region 方向条件，全部更新.
		Conds conds = new Conds();
		QueryObject qo = new QueryObject(conds);
		qo.AddWhere(CondAttr.NodeID, this.getFK_Node());
		qo.addAnd();
		qo.AddWhere(CondAttr.DataFrom, ConnDataFrom.StandAloneFrm.getValue());
		qo.addAnd();
		qo.AddWhere(CondAttr.CondType, condTypeEnum.getValue());
		if (toNodeID != 0)
		{
			qo.addAnd();
			qo.AddWhere(CondAttr.ToNodeID, toNodeID);
		}
		int num = qo.DoQuery();
		for (Cond item : conds.ToJavaList())
		{
			item.setCondOrAnd(cond.getCondOrAnd());
			item.Update();
		}

			///#endregion

		/* 执行同步*/
		String sqls = "UPDATE WF_Node SET IsCCFlow=0";
		sqls += "@UPDATE WF_Node  SET IsCCFlow=1 WHERE NodeID IN (SELECT NODEID FROM WF_Cond a WHERE a.NodeID= NodeID AND CondType=1 )";
		BP.DA.DBAccess.RunSQLs(sqls);

		String sql = "UPDATE WF_Cond SET DataFrom=" + ConnDataFrom.StandAloneFrm.getValue() + " WHERE NodeID=" + cond.getNodeID() + "  AND FK_Node=" + cond.getFK_Node() + " AND ToNodeID=" + toNodeID;
		switch (condTypeEnum)
		{
			case Flow:
			case Node:
				cond.setMyPK(String.valueOf(DBAccess.GenerOID())); //cond.NodeID + "_" + cond.FK_Node + "_" + cond.FK_Attr + "_" + cond.OperatorValue;
				cond.Insert();
				BP.DA.DBAccess.RunSQL(sql);
				break;
			case Dir:
				// cond.setMyPK( cond.NodeID +"_"+ this.Request.QueryString["ToNodeID"]+"_" + cond.FK_Node + "_" + cond.FK_Attr + "_" + cond.OperatorValue;
				cond.setMyPK(String.valueOf(DBAccess.GenerOID())); //cond.NodeID + "_" + cond.FK_Node + "_" + cond.FK_Attr + "_" + cond.OperatorValue;
				cond.setToNodeID(toNodeID);
				cond.Insert();
				BP.DA.DBAccess.RunSQL(sql);
				break;
			case SubFlow: //启动子流程.
				cond.setMyPK(String.valueOf(DBAccess.GenerOID())); //cond.NodeID + "_" + cond.FK_Node + "_" + cond.FK_Attr + "_" + cond.OperatorValue;
				cond.setToNodeID(toNodeID);
				cond.Insert();
				BP.DA.DBAccess.RunSQL(sql);
				break;
			default:
				throw new RuntimeException("未设计的情况。" + condTypeEnum.toString());
		}

		return "保存成功!!";
	}

	public final String StandAloneFrm_InitField() throws Exception
	{
		//字段属性.
		MapAttr attr = new MapAttr();
		attr.setMyPK(this.getFrmID() + "_" + this.getKeyOfEn());
		attr.Retrieve();
		return AttrCond(attr);
	}
	private String AttrCond(MapAttr attr) throws Exception
	{
		//定义数据容器.
		DataSet ds = new DataSet();

		ds.Tables.add(attr.ToDataTableField("Sys_MapAttr"));

		if (attr.getLGType() == FieldTypeS.Enum)
		{
			SysEnums ses = new SysEnums(attr.getUIBindKey());
			ds.Tables.add(ses.ToDataTableField("Enums"));
		}


			///#region 增加操作符 number.
		if (attr.getIsNum())
		{
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
		}
		else
		{

				///#region 增加操作符 string.
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

				///#endregion 增加操作符 string.
		}

			///#endregion 增加操作符 number.

		return BP.Tools.Json.ToJson(ds); // cond.ToJson();
	}

		///#endregion


		///#region 方向条件SQL 模版
	/** 
	 初始化
	 
	 @return 
	 * @throws Exception 
	*/
	public final String CondBySQLTemplate_Init() throws Exception
	{
		String fk_mainNode = this.GetRequestVal("FK_MainNode");
		String toNodeID = this.GetRequestVal("ToNodeID");

		CondType condTypeEnum = CondType.forValue(this.GetRequestValInt("CondType"));

		String mypk = fk_mainNode + "_" + toNodeID + "_" + condTypeEnum + "_" + ConnDataFrom.SQLTemplate.toString();

		Cond cond = new Cond();
		cond.setMyPK(mypk);
		cond.RetrieveFromDBSources();

		return cond.ToJson();
	}
	/** 
	 保存
	 
	 @return 
	 * @throws Exception 
	*/
	public final String CondBySQLTemplate_Save() throws Exception
	{

		String fk_mainNode = this.GetRequestVal("FK_MainNode");
		String toNodeID = this.GetRequestVal("ToNodeID");
		CondType condTypeEnum = CondType.forValue(this.GetRequestValInt("CondType"));
		String mypk = fk_mainNode + "_" + toNodeID + "_" + condTypeEnum + "_" + ConnDataFrom.SQLTemplate.toString();

		String sql = this.GetRequestVal("TB_Docs");

		//把其他的条件都删除掉.
		DBAccess.RunSQL("DELETE FROM WF_Cond WHERE (CondType=" + condTypeEnum.getValue() + " AND  NodeID=" + this.getFK_Node() + " AND ToNodeID=" + toNodeID + ") AND DataFrom!=" + ConnDataFrom.SQLTemplate.getValue());

		Cond cond = new Cond();
		cond.Delete(CondAttr.NodeID, fk_mainNode, CondAttr.ToNodeID, toNodeID, CondAttr.CondType, condTypeEnum.getValue());

		cond.setMyPK(mypk);
		cond.setHisDataFrom(ConnDataFrom.SQLTemplate);

		cond.setNodeID(this.GetRequestValInt("FK_MainNode"));
		cond.setFK_Node(this.GetRequestValInt("FK_MainNode"));
		cond.setToNodeID(this.GetRequestValInt("ToNodeID"));

		cond.setFK_Flow(this.getFK_Flow());
		cond.setOperatorValue(sql);
		cond.setNote(this.GetRequestVal("TB_Note")); //备注.

		cond.setFK_Flow(this.getFK_Flow());
		cond.setHisCondType(condTypeEnum);
		cond.Insert();

		return "保存成功..";
	}
	/** 
	 删除
	 
	 @return 
	 * @throws Exception 
	*/
	public final String CondBySQLTemplate_Delete() throws Exception
	{
		String fk_mainNode = this.GetRequestVal("FK_MainNode");
		String toNodeID = this.GetRequestVal("ToNodeID");
		CondType condTypeEnum = CondType.forValue(this.GetRequestValInt("CondType"));

		String mypk = fk_mainNode + "_" + toNodeID + "_" + condTypeEnum + "_" + ConnDataFrom.SQLTemplate.toString();

		Cond deleteCond = new Cond();
		int i = deleteCond.Delete(CondAttr.NodeID, fk_mainNode, CondAttr.ToNodeID, toNodeID, CondAttr.CondType, condTypeEnum.getValue());

		if (i == 1)
		{
			return "删除成功..";
		}

		return "无可删除的数据.";
	}

		///#endregion 方向条件SQL 模版


		///#region 方向条件SQL
	/** 
	 初始化
	 
	 @return 
	 * @throws Exception 
	*/
	public final String CondBySQL_Init() throws Exception
	{
		String fk_mainNode = this.GetRequestVal("FK_MainNode");
		String toNodeID = this.GetRequestVal("ToNodeID");

		CondType condTypeEnum = CondType.forValue(this.GetRequestValInt("CondType"));

		String mypk = fk_mainNode + "_" + toNodeID + "_" + condTypeEnum + "_" + ConnDataFrom.SQL.toString();

		Cond cond = new Cond();
		cond.setMyPK(mypk);
		cond.RetrieveFromDBSources();

		return cond.ToJson();
	}
	/** 
	 保存
	 
	 @return 
	 * @throws Exception 
	*/
	public final String CondBySQL_Save() throws Exception
	{

		String fk_mainNode = this.GetRequestVal("FK_MainNode");
		String toNodeID = this.GetRequestVal("ToNodeID");
		CondType condTypeEnum = CondType.forValue(this.GetRequestValInt("CondType"));
		String mypk = fk_mainNode + "_" + toNodeID + "_" + condTypeEnum + "_" + ConnDataFrom.SQL.toString();

		String sql = this.GetRequestVal("TB_Docs");

		//把其他的条件都删除掉.
		DBAccess.RunSQL("DELETE FROM WF_Cond WHERE (CondType=" + condTypeEnum.getValue() + " AND NodeID=" + this.getFK_Node() + " AND ToNodeID=" + toNodeID + ") AND DataFrom!=" + ConnDataFrom.SQL.getValue());

		Cond cond = new Cond();
		cond.Delete(CondAttr.NodeID, fk_mainNode, CondAttr.ToNodeID, toNodeID, CondAttr.CondType, condTypeEnum.getValue());

		cond.setMyPK(mypk);
		cond.setHisDataFrom(ConnDataFrom.SQL);

		cond.setNodeID(this.GetRequestValInt("FK_MainNode"));
		cond.setFK_Node(this.GetRequestValInt("FK_MainNode"));
		cond.setToNodeID(this.GetRequestValInt("ToNodeID"));

		cond.setFK_Flow(this.getFK_Flow());
		cond.setOperatorValue(sql);
		cond.setNote(this.GetRequestVal("TB_Note")); //备注.

		cond.setFK_Flow(this.getFK_Flow());
		cond.setHisCondType(condTypeEnum);
		cond.Insert();

		return "保存成功..";
	}
	/** 
	 删除
	 
	 @return 
	 * @throws Exception 
	*/
	public final String CondBySQL_Delete() throws Exception
	{
		String fk_mainNode = this.GetRequestVal("FK_MainNode");
		String toNodeID = this.GetRequestVal("ToNodeID");
		CondType condTypeEnum = CondType.forValue(this.GetRequestValInt("CondType"));

		String mypk = fk_mainNode + "_" + toNodeID + "_" + condTypeEnum + "_" + ConnDataFrom.SQL.toString();

		Cond deleteCond = new Cond();
		int i = deleteCond.Delete(CondAttr.NodeID, fk_mainNode, CondAttr.ToNodeID, toNodeID, CondAttr.CondType, condTypeEnum.getValue());

		if (i == 1)
		{
			return "删除成功..";
		}

		return "无可删除的数据.";
	}

		///#endregion


		///#region 方向条件岗位
	/** 
	 初始化
	 
	 @return 
	 * @throws Exception 
	*/
	public final String CondByStation_Init() throws Exception
	{
		DataSet ds = new DataSet();

		//岗位类型.
		BP.GPM.StationTypes tps = new BP.GPM.StationTypes();
		tps.RetrieveAll();
		ds.Tables.add(tps.ToDataTableField("StationTypes"));

		//岗位.
		BP.GPM.Stations sts = new BP.GPM.Stations();
		sts.RetrieveAll();
		ds.Tables.add(sts.ToDataTableField("Stations"));


		//取有可能存盘的数据.
		int FK_MainNode = this.GetRequestValInt("FK_MainNode");
		int ToNodeID = this.GetRequestValInt("ToNodeID");
		Cond cond = new Cond();
		String mypk = FK_MainNode + "_" + ToNodeID + "_Dir_" + ConnDataFrom.Stas.toString();
		cond.setMyPK(mypk);
		cond.RetrieveFromDBSources();
		ds.Tables.add(cond.ToDataTableField("Cond"));

		return BP.Tools.Json.ToJson(ds);
	}
	/** 
	 保存
	 
	 @return 
	 * @throws Exception 
	*/
	public final String CondByStation_Save() throws Exception
	{
		int FK_MainNode = this.GetRequestValInt("FK_MainNode");
		int ToNodeID = this.GetRequestValInt("ToNodeID");
		CondType HisCondType = CondType.Dir;

		Cond cond = new Cond();
		cond.Delete(CondAttr.NodeID, FK_MainNode, CondAttr.ToNodeID, ToNodeID, CondAttr.CondType, HisCondType.getValue());

		String mypk = FK_MainNode + "_" + ToNodeID + "_Dir_" + ConnDataFrom.Stas.toString();

		//把其他的条件都删除掉.
		DBAccess.RunSQL("DELETE FROM WF_Cond WHERE (CondType=" + HisCondType.getValue() + " AND  NodeID=" + this.getFK_Node() + " AND ToNodeID=" + ToNodeID + ") AND DataFrom!=" + ConnDataFrom.Stas.getValue());

		// 删除岗位条件.
		cond.setMyPK(mypk);
		if (cond.RetrieveFromDBSources() == 0)
		{
			cond.setHisDataFrom(ConnDataFrom.Stas);
			cond.setNodeID(FK_MainNode);
			cond.setFK_Flow(this.getFK_Flow());
			cond.setToNodeID(ToNodeID);
			cond.Insert();
		}

		String val = this.GetRequestVal("emps").replace(",", "@");
		String valT = this.GetRequestVal("orgEmps").replace(",", "&nbsp;&nbsp;");
		cond.setOperatorValue(val);
		//cond.OperatorValueT = valT;
		cond.SetPara("OrgEmps", valT);
		cond.setSpecOperWay(SpecOperWay.forValue(this.GetRequestValInt("DDL_SpecOperWay")));
		if (cond.getSpecOperWay() != SpecOperWay.CurrOper)
		{
			cond.setSpecOperPara(this.GetRequestVal("TB_SpecOperPara"));
		}
		else
		{
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
	/** 
	 删除
	 
	 @return 
	 * @throws Exception 
	*/
	public final String CondByStation_Delete() throws Exception
	{
		String fk_mainNode = this.GetRequestVal("FK_MainNode");
		String toNodeID = this.GetRequestVal("ToNodeID");
		CondType condTypeEnum = CondType.forValue(this.GetRequestValInt("CondType"));
		String mypk = fk_mainNode + "_" + toNodeID + "_" + condTypeEnum + "_" + ConnDataFrom.SQL.toString();

		Cond deleteCond = new Cond();
		int i = deleteCond.Delete(CondAttr.NodeID, fk_mainNode, CondAttr.ToNodeID, toNodeID, CondAttr.CondType, condTypeEnum.getValue());

		if (i == 1)
		{
			return "删除成功..";
		}

		return "无可删除的数据.";
	}

		///#endregion


		///#region 按照部门条件计算CondByDept_Delete
	public final String CondByDept_Save() throws Exception
	{
		int FK_MainNode = this.GetRequestValInt("FK_MainNode");
		int ToNodeID = this.GetRequestValInt("ToNodeID");
		CondType condType = CondType.forValue(this.GetRequestValInt("CondType"));

		Cond cond = new Cond();

		//把其他的条件都删除掉.
		DBAccess.RunSQL("DELETE FROM WF_Cond WHERE (CondType=" + condType.getValue() + " AND  NodeID=" + this.getFK_Node() + " AND ToNodeID=" + this.GetRequestValInt("ToNodeID") + ") AND DataFrom!=" + ConnDataFrom.Depts.getValue());

		String mypk = this.GetRequestValInt("FK_MainNode") + "_" + this.GetRequestValInt("ToNodeID") + "_" + condType.toString() + "_" + ConnDataFrom.Depts.toString();
		cond.setMyPK(mypk);

		if (cond.RetrieveFromDBSources() == 0)
		{
			cond.setHisDataFrom(ConnDataFrom.Depts);
			cond.setNodeID(this.GetRequestValInt("FK_MainNode"));
			cond.setFK_Flow(this.getFK_Flow());
			cond.setToNodeID(this.GetRequestValInt("ToNodeID"));
			cond.Insert();
		}

		String val = this.GetRequestVal("depts").replace(",", "@");
		cond.setOperatorValue(val);
		cond.setSpecOperWay(SpecOperWay.forValue(this.GetRequestValInt("DDL_SpecOperWay")));
		if (cond.getSpecOperWay() != SpecOperWay.CurrOper)
		{
			cond.setSpecOperPara(this.GetRequestVal("TB_SpecOperPara"));
		}
		else
		{
			cond.setSpecOperPara("");
		}
		cond.setHisDataFrom(ConnDataFrom.Depts);
		cond.setFK_Flow(this.getFK_Flow());
		cond.setHisCondType(CondType.Dir);
		cond.setFK_Node(FK_MainNode);

		cond.setToNodeID(ToNodeID);
		cond.Update();

		return "保存成功!!";
	}
	public final String CondByDept_Delete() throws Exception
	{
		String fk_mainNode = this.GetRequestVal("FK_MainNode");
		String toNodeID = this.GetRequestVal("ToNodeID");
		CondType condTypeEnum = CondType.forValue(this.GetRequestValInt("CondType"));

		String mypk = fk_mainNode + "_" + toNodeID + "_" + condTypeEnum + "_" + ConnDataFrom.SQL.toString();

		Cond deleteCond = new Cond();
		int i = deleteCond.Delete(CondAttr.NodeID, fk_mainNode, CondAttr.ToNodeID, toNodeID, CondAttr.CondType, condTypeEnum.getValue());

		if (i == 1)
		{
			return "删除成功..";
		}

		return "无可删除的数据.";
	}


		///#endregion


		///#region 方向条件Para
	/** 
	 初始化
	 
	 @return 
	 * @throws Exception 
	*/
	public final String CondByPara_Init() throws Exception
	{
		String fk_mainNode = this.GetRequestVal("FK_MainNode");
		String toNodeID = this.GetRequestVal("ToNodeID");

		CondType condTypeEnum = CondType.forValue(this.GetRequestValInt("CondType"));

		String mypk = fk_mainNode + "_" + toNodeID + "_" + condTypeEnum + "_" + ConnDataFrom.Paras.toString();

		Cond cond = new Cond();
		cond.setMyPK(mypk);
		cond.RetrieveFromDBSources();

		return cond.ToJson();
	}
	/** 
	 保存
	 
	 @return 
	 * @throws Exception 
	*/
	public final String CondByPara_Save() throws Exception
	{
		String fk_mainNode = this.GetRequestVal("FK_MainNode");
		String toNodeID = this.GetRequestVal("ToNodeID");
		CondType condTypeEnum = CondType.forValue(this.GetRequestValInt("CondType"));
		String mypk = fk_mainNode + "_" + toNodeID + "_" + condTypeEnum + "_" + ConnDataFrom.Paras.toString();

		String sql = this.GetRequestVal("TB_Docs");


		//把其他的条件都删除掉.
		DBAccess.RunSQL("DELETE FROM WF_Cond WHERE (CondType=" + condTypeEnum.getValue() + " AND   NodeID=" + this.getFK_Node() + " AND ToNodeID=" + toNodeID + ") AND DataFrom!=" + ConnDataFrom.Paras.getValue());

		Cond cond = new Cond();
		cond.Delete(CondAttr.NodeID, fk_mainNode, CondAttr.ToNodeID, toNodeID, CondAttr.CondType, condTypeEnum.getValue());

		cond.setMyPK(mypk);
		cond.setHisDataFrom(ConnDataFrom.Paras);

		cond.setNodeID(this.GetRequestValInt("FK_MainNode"));
		cond.setFK_Node(this.GetRequestValInt("FK_MainNode"));
		cond.setToNodeID(this.GetRequestValInt("ToNodeID"));

		cond.setFK_Flow(this.getFK_Flow());
		cond.setOperatorValue(sql);
		cond.setNote(this.GetRequestVal("TB_Note")); //备注.

		cond.setFK_Flow(this.getFK_Flow());
		cond.setHisCondType(condTypeEnum);
		cond.Insert();

		return "保存成功..";
	}
	/** 
	 删除
	 
	 @return 
	 * @throws Exception 
	*/
	public final String CondByPara_Delete() throws Exception
	{
		String fk_mainNode = this.GetRequestVal("FK_MainNode");
		String toNodeID = this.GetRequestVal("ToNodeID");
		CondType condTypeEnum = CondType.forValue(this.GetRequestValInt("CondType"));

		String mypk = fk_mainNode + "_" + toNodeID + "_" + condTypeEnum + "_" + ConnDataFrom.Paras.toString();

		Cond deleteCond = new Cond();
		int i = deleteCond.Delete(CondAttr.NodeID, fk_mainNode, CondAttr.ToNodeID, toNodeID, CondAttr.CondType, condTypeEnum.getValue());

		if (i == 1)
		{
			return "删除成功..";
		}

		return "无可删除的数据.";
	}

		///#endregion


		///#region 按照岗位的方向条件.
	public final String CondStation_Init() throws Exception
	{
		DataSet ds = new DataSet();

		//岗位类型.
		BP.GPM.StationTypes tps = new BP.GPM.StationTypes();
		tps.RetrieveAll();
		ds.Tables.add(tps.ToDataTableField("StationTypes"));

		//岗位.
		BP.GPM.Stations sts = new BP.GPM.Stations();
		sts.RetrieveAll();
		ds.Tables.add(sts.ToDataTableField("Stations"));


		//取有可能存盘的数据.
		int FK_MainNode = this.GetRequestValInt("FK_MainNode");
		int ToNodeID = this.GetRequestValInt("ToNodeID");
		Cond cond = new Cond();
		String mypk = FK_MainNode + "_" + ToNodeID + "_Dir_" + ConnDataFrom.Stas.toString();
		cond.setMyPK(mypk);
		cond.RetrieveFromDBSources();
		ds.Tables.add(cond.ToDataTableField("Cond"));

		return BP.Tools.Json.ToJson(ds);


	}


		///#endregion 按照岗位的方向条件.

}