package bp.wf.httphandler;

import bp.difference.handler.CommonFileUtils;
import bp.difference.handler.CommonUtils;
import bp.difference.handler.WebContralBase;
import bp.sys.*;
import bp.da.*;
import bp.wf.Glo;
import bp.wf.template.*;
import bp.difference.*;
import bp.*;
import bp.wf.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.io.*;
import java.time.*;

public class WF_Admin_AttrFlow extends WebContralBase
{
	/** 
	 构造函数
	*/
	public WF_Admin_AttrFlow() throws Exception {
	}


		///#region 修改轨迹.
	public final String EditTrackDtl_Init() throws Exception {
		Track tk = new Track(this.getFK_Flow(), this.getMyPK());
		return tk.getMsg();
	}
	public final String EditTrackDtl_Save() throws Exception {
		String msg = this.GetRequestVal("Msg");
		String tackTable = "ND" + Integer.parseInt(this.getFK_Flow()) + "Track";
		String sql = "UPDATE " + tackTable + " SET Msg='" + msg + "' WHERE MyPK='" + this.getMyPK() + "'";
		DBAccess.RunSQL(sql);
		return "修改成功";
	}
	public final String EditTrackDtl_Delete() throws Exception {
		String tackTable = "ND" + Integer.parseInt(this.getFK_Flow()) + "Track";
		String sql = "DELETE FROM  " + tackTable + " WHERE MyPK='" + this.getMyPK() + "'";
		DBAccess.RunSQL(sql);
		return "删除成功.";
	}

		///#endregion



		///#region APICodeFEE_Init.
	/** 
	 代码生成器.
	 
	 @return 
	*/
	public final String APICodeFEE_Init() throws Exception {
		if (DataType.IsNullOrEmpty(getFK_Flow()))
		{
			return "err@FK_Flow参数不能为空！";
		}

		Flow flow = new Flow(this.getFK_Flow());

		String tmpPath = "";

		if (Glo.getPlatform() == Platform.CCFlow)
		{
			tmpPath = SystemConfig.getPathOfWebApp() + "WF/Admin/AttrFlow/APICodeFEE.txt.CCFlow";
		}
		else
		{
			tmpPath = SystemConfig.getPathOfWebApp() + "WF/Admin/AttrFlow/APICodeFEE.txt.JFlow";
		}

		if ((new File(tmpPath)).isFile() == false)
		{
			return String.format("未找到事件编写模板文件“%1$s”，请联系管理员！", tmpPath);
		}

		String Title = flow.getName() + "[" + flow.getNo() + "]";
		String code = DataType.ReadTextFile(tmpPath);
		code = code.replace("F001Templepte", String.format("FEE%1$s", flow.getNo())).replace("@FlowName", flow.getName()).replace("@FlowNo", flow.getNo());


		//此处将重要行标示出来，根据下面的数组中的项来检索重要行号
		String[] lineStrings = new String[] {"namespace BP.FlowEvent", ": BP.WF.FlowEventBase", "public override string FlowMark", "public override string SendWhen()", "public override string SendSuccess()", "public override string SendError()", "public override string FlowOnCreateWorkID()", "public override string FlowOverBefore()", "public override string FlowOverAfter()", "public override string BeforeFlowDel()", "public override string AfterFlowDel()", "public override string SaveAfter()", "public override string SaveBefore()", "public override string UndoneBefore()", "public override string UndoneAfter()", "public override string ReturnBefore()", "public override string ReturnAfter()", "public override string AskerAfter()", "public override string AskerReAfter()"};



		String msg = "<script type=\"text/javascript\">SyntaxHighlighter.highlight();</script>";
		msg += String.format("<pre type=\"syntaxhighlighter\" class=\"brush: csharp; html-script: false; highlight: [%3$s]\" title=\"%1$s[编号：%2$s] 流程自定义事件代码生成\">", flow.getName(), flow.getNo(), APICodeFEE_Init_GetImportantLinesNumbers(lineStrings, code));
		msg += code.replace("<", "&lt;"); //SyntaxHighlighter中，使用<Pre>包含的代码要将左尖括号改成其转义形式
		msg += "</pre>";

		return msg;
	}
	/** 
	 获取重要行的标号连接字符串，如3,6,8
	 
	 param lineInStrings 重要行中包含的字符串数组，只要行中包含其中的一项字符串，则这行就是重要行
	 param str 要检索的字符串，使用Environment.NewLine分行
	 @return 
	*/
	private String APICodeFEE_Init_GetImportantLinesNumbers(String[] lineInStrings, String str)
	{
		String[] lines = str.replace(System.lineSeparator(), "`").split("[`]", -1);
		String nums = "";

		for (int i = 0; i < lines.length; i++)
		{
			for (String instr : lineInStrings)
			{
				if (lines[i].indexOf(instr) != -1)
				{
					nums += (i + 1) + ",";
					break;
				}
			}
		}

		return StringHelper.trimEnd(nums, ',');
	}

		///#endregion APICodeFEE_Init.


		///#region 节点属性（列表）的操作
	/** 
	 初始化节点属性列表.
	 
	 @return 
	*/
	public final String NodeAttrs_Init() throws Exception {
		String strFlowId = GetRequestVal("FK_Flow");
		if (DataType.IsNullOrEmpty(strFlowId))
		{
			return "err@参数错误！";
		}

		Nodes nodes = new Nodes();
		nodes.Retrieve("FK_Flow", strFlowId, null);
		//因直接使用nodes.ToJson()无法获取某些字段（e.g.HisFormTypeText,原因：Node没有自己的Attr类）
		//故此处手动创建前台所需的DataTable
		DataTable dt = new DataTable();
		dt.Columns.Add("NodeID"); //节点ID
		dt.Columns.Add("Name"); //节点名称
		dt.Columns.Add("HisFormType"); //表单方案
		dt.Columns.Add("HisFormTypeText");
		dt.Columns.Add("HisRunModel"); //节点类型
		dt.Columns.Add("HisRunModelT");

		dt.Columns.Add("HisDeliveryWay"); //接收方类型
		dt.Columns.Add("HisDeliveryWayText");
		dt.Columns.Add("HisDeliveryWayJsFnPara");
		dt.Columns.Add("HisDeliveryWayCountLabel");
		dt.Columns.Add("HisDeliveryWayCount"); //接收方Count

		dt.Columns.Add("HisCCRole"); //抄送人
		dt.Columns.Add("HisCCRoleText");
		dt.Columns.Add("HisFrmEventsCount"); //消息&事件Count
		dt.Columns.Add("HisFinishCondsCount"); //流程完成条件Count
		DataRow dr;
		for (Node node : nodes.ToJavaList())
		{
			dr = dt.NewRow();
			dr.setValue("NodeID", node.getNodeID());
			dr.setValue("Name", node.getName());
			dr.setValue("HisFormType", node.getHisFormType());
			dr.setValue("HisFormTypeText", node.getHisFormTypeText());
			dr.setValue("HisRunModel", node.getHisRunModel());
			dr.setValue("HisRunModelT", node.getHisRunModelT());
			dr.setValue("HisDeliveryWay", node.getHisDeliveryWay());
			dr.setValue("HisDeliveryWayText", node.getHisDeliveryWayText());

			//接收方数量
			int intHisDeliveryWayCount = 0;
			if (node.getHisDeliveryWay() == DeliveryWay.ByStation)
			{
				dr.setValue("HisDeliveryWayJsFnPara", "ByStation");
				dr.setValue("HisDeliveryWayCountLabel", "岗位");
				NodeStations nss = new NodeStations();
				intHisDeliveryWayCount = nss.Retrieve(NodeStationAttr.FK_Node, node.getNodeID(), null);
			}
			else if (node.getHisDeliveryWay() == DeliveryWay.ByDept)
			{
				dr.setValue("HisDeliveryWayJsFnPara", "ByDept");
				dr.setValue("HisDeliveryWayCountLabel", "部门");
				NodeDepts nss = new NodeDepts();
				intHisDeliveryWayCount = nss.Retrieve(NodeDeptAttr.FK_Node, node.getNodeID(), null);
			}
			else if (node.getHisDeliveryWay() == DeliveryWay.ByBindEmp)
			{
				dr.setValue("HisDeliveryWayJsFnPara", "ByDept");
				dr.setValue("HisDeliveryWayCountLabel", "人员");
				NodeEmps nes = new NodeEmps();
				intHisDeliveryWayCount = nes.Retrieve(NodeStationAttr.FK_Node, node.getNodeID(), null);
			}
			dr.setValue("HisDeliveryWayCount", intHisDeliveryWayCount);

			//抄送
			dr.setValue("HisCCRole", node.getHisCCRole());
			dr.setValue("HisCCRoleText", node.getHisCCRoleText());

			//消息&事件Count
			FrmEvents fes = new FrmEvents();
			dr.setValue("HisFrmEventsCount", fes.Retrieve(FrmEventAttr.FK_MapData, "ND" + node.getNodeID(), null));

			//流程完成条件Count
			Conds conds = new Conds(CondType.Flow, node.getNodeID());
			dr.setValue("HisFinishCondsCount", conds.size());

			dt.Rows.add(dr);
		}
		return bp.tools.Json.ToJson(dt);
	}

		///#endregion


		///#region 与业务表数据同步
	public final String DTSBTable_Init() throws Exception {
		DataSet ds = new DataSet();

		// 把流程信息放入.
		Flow fl = new Flow(this.getFK_Flow());
		DataTable dtFlow = fl.ToDataTableField("Flow");
		ds.Tables.add(dtFlow);

		//获得数据源的表.
		SFDBSrc src = new SFDBSrc(fl.getDTSDBSrc());
		DataTable dt = src.GetTables(false);

		if (src.getFieldCaseModel() == FieldCaseModel.UpperCase)
		{
			dt.Columns.get("NO").ColumnName = "No";
			dt.Columns.get("NAME").ColumnName = "Name";
		}
		if (src.getFieldCaseModel() == FieldCaseModel.Lowercase)
		{
			dt.Columns.get("no").ColumnName = "No";
			dt.Columns.get("name").ColumnName = "Name";
		}

		dt.TableName = "Tables";
		ds.Tables.add(dt);


		//把节点信息放入.
		Nodes nds = new Nodes(this.getFK_Flow());
		DataTable dtNode = nds.ToDataTableField("Nodes");
		ds.Tables.add(dtNode);




		return bp.tools.Json.ToJson(ds);
	}

	/** 
	 与业务表数据同步
	 
	 @return 
	*/
	public final String DTSBTable_Save() throws Exception {
		//获取流程属性
		Flow flow = new Flow(this.getFK_Flow());
		//获取主键方式
		DataDTSWay dtsWay = DataDTSWay.forValue(this.GetRequestValInt("RB_DTSWay"));

		FlowDTSTime dtsTime = FlowDTSTime.forValue(this.GetRequestValInt("RB_DTSTime"));

		flow.setDTSWay(dtsWay);
		flow.setDTSTime(dtsTime);

		if (flow.getDTSWay() == DataDTSWay.None)
		{
			flow.Update();
			return "保存成功.";
		}

		//保存配置信息
		flow.setDTSDBSrc(this.GetRequestVal("DDL_DBSrc"));
		flow.setDTSBTable(this.GetRequestVal("DDL_Table"));
		flow.setDTSSpecNodes(StringHelper.trimEnd(this.GetRequestVal("CheckBoxIDs"), ','));

		flow.DirectUpdate();
		return "保存成功";
	}


		///#endregion

		///#region 数据同步数据源变化时，关联表的列表发生变化
	public final String DTSBTable_DBSrcChange() throws Exception {
		String dbsrc = this.GetRequestVal("DDL_DBSrc");
		//绑定表. 
		SFDBSrc src = new SFDBSrc(dbsrc);
		DataTable dt = src.GetTables(false);
		if (src.getFieldCaseModel() == FieldCaseModel.UpperCase)
		{
			dt.Columns.get("NO").ColumnName = "No";
			dt.Columns.get("NAME").ColumnName = "Name";
		}
		if (src.getFieldCaseModel() == FieldCaseModel.Lowercase)
		{
			dt.Columns.get("no").ColumnName = "No";
			dt.Columns.get("name").ColumnName = "Name";
		}
		return bp.tools.Json.ToJson(dt);
	}

		///#endregion


		///#region 数据调度 - 字段映射.
	public final String DTSBTableExt_Init() throws Exception {
		//定义数据容器.
		DataSet ds = new DataSet();

		//获得数据表列.
		SFDBSrc src = new SFDBSrc(this.GetRequestVal("FK_DBSrc"));

		DataTable dtColms = src.GetColumns(this.GetRequestVal("TableName"));
		dtColms.TableName = "Cols";
		if (src.getFieldCaseModel() == FieldCaseModel.UpperCase)
		{
			dtColms.Columns.get("NO").ColumnName = "No";
			dtColms.Columns.get("NAME").ColumnName = "Name";
		}
		if (src.getFieldCaseModel() == FieldCaseModel.Lowercase)
		{
			dtColms.Columns.get("no").ColumnName = "No";
			dtColms.Columns.get("name").ColumnName = "Name";
		}

		ds.Tables.add(dtColms); //列名.

		//属性列表.
		MapAttrs attrs = new MapAttrs("ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt");
		DataTable dtAttrs = attrs.ToDataTableStringField("Sys_MapAttr");
		ds.Tables.add(dtAttrs);

		//加入流程配置信息
		Flow flow = new Flow(this.getFK_Flow());
		DataTable dtFlow = flow.ToDataTableField("Flow");
		ds.Tables.add(dtFlow);

		//转化成json,返回.
		return bp.tools.Json.ToJson(ds);
	}
	public final String DTSBTableExt_Save() throws Exception {
		String rpt = "ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt";
		Flow fl = new Flow(this.getFK_Flow());
		MapAttrs mattrs = new MapAttrs(rpt);

		String pk = this.GetRequestVal("DDL_OID");
		if (DataType.IsNullOrEmpty(pk) == true)
		{
			return "err@必须设置业务表的主键，否则无法同步。";
		}


		String lcStr = ""; //要同步的流程字段
		String ywStr = ""; //第三方字段
		String err = "";
		for (MapAttr attr : mattrs.ToJavaList())
		{
			int val = this.GetRequestValInt("CB_" + attr.getKeyOfEn());
			if (val == 0)
			{
				continue;
			}

			String refField = this.GetRequestVal("DDL_" + attr.getKeyOfEn());

			//如果选中的业务字段重复，抛出异常
			if (ywStr.contains("@" + refField + "@"))
			{
				err += "@配置【" + attr.getKeyOfEn() + " - " + attr.getName() + "】错误, 请确保选中业务字段的唯一性，该业务字段已经被其他字段所使用。";
			}
			lcStr += "" + attr.getKeyOfEn() + "=" + refField + "@";
			ywStr += "@" + refField + "@,";
		}

		//    bp.web.Controls.RadioBtn rb = this.Pub1.GetRadioBtnByID("rb_workId");

		int pkModel = this.GetRequestValInt("PKModel");

		String ddl_key = this.GetRequestVal("DDL_OID");
		if (pkModel == 0)
		{
			if (ywStr.contains("@" + ddl_key + "@"))
			{
				err += "@请确保选中业务字段的唯一性，该业务字段【" + ddl_key + "】已经被其他字段所使用。";
			}
			lcStr = "OID=" + ddl_key + "@" + lcStr;
			ywStr = "@" + ddl_key + "@," + ywStr;
		}
		else
		{
			if (ywStr.contains("@" + ddl_key + "@"))
			{
				err += "@请确保选中业务字段的唯一性，该业务字段【" + ddl_key + "】已经被其他字段所使用。";
			}
			lcStr = "GUID=" + ddl_key + "@" + lcStr;
			ywStr = "@" + ddl_key + "@," + ywStr;
		}

		if (!err.equals(""))
		{
			return "err@" + err;
		}

		//lcStr = lcStr.Replace("@", "");
		ywStr = ywStr.replace("@", "");


		//去除最后一个字符的操作
		if (DataType.IsNullOrEmpty(lcStr) || DataType.IsNullOrEmpty(ywStr))
		{
			return "err@要配置的内容为空...";
		}
		lcStr = lcStr.substring(0, lcStr.length() - 1);
		ywStr = ywStr.substring(0, ywStr.length() - 1);


		//数据存储格式   a,b,c@a_1,b_1,c_1
		fl.setDTSFields(lcStr);
		fl.setDTSBTablePK(pk);
		fl.DirectUpdate();

		return "设置成功.";
	}

		///#endregion


		///#region 前置导航save
	/** 
	 前置导航save
	 
	 @return 
	*/
	public final String StartGuide_Save() throws Exception {
		try
		{
			//Flow en = new Flow();
			//en.No = this.FK_Flow;
			//en.Retrieve();

			//int val = this.GetRequestValInt("RB_StartGuideWay");

			//en.SetValByKey(BP.WF.Template.FlowAttr.StartGuideWay, val);

			//if (en.StartGuideWay == StartGuideWay.None)
			//{
			//    en.StartGuideWay = BP.WF.Template.StartGuideWay.None;
			//}

			//if (en.StartGuideWay == StartGuideWay.ByHistoryUrl)
			//{
			//    en.StartGuidePara1 = this.GetRequestVal("TB_ByHistoryUrl");
			//    en.StartGuidePara2 = "";
			//    en.StartGuideWay = BP.WF.Template.StartGuideWay.ByHistoryUrl;
			//}

			//if (en.StartGuideWay == StartGuideWay.BySelfUrl)
			//{
			//    en.StartGuidePara1 = this.GetRequestVal("TB_SelfURL");
			//    en.StartGuidePara2 = "";
			//    en.StartGuideWay = BP.WF.Template.StartGuideWay.BySelfUrl;
			//}

			////单条模式.
			//if (en.StartGuideWay == StartGuideWay.BySQLOne)
			//{
			//    en.StartGuidePara1 = this.GetRequestVal("TB_BySQLOne1");  //查询语句.
			//    en.StartGuidePara2 = this.GetRequestVal("TB_BySQLOne2");  //列表语句.

			//    //@李国文.
			//    en.StartGuidePara3 = this.GetRequestVal("TB_BySQLOne3");  //单行赋值语句.
			//    en.StartGuideWay = BP.WF.Template.StartGuideWay.BySQLOne;
			//}
			////多条模式
			//if (en.StartGuideWay == StartGuideWay.BySQLMulti)
			//{
			//    en.StartGuidePara1 = this.GetRequestVal("TB_BySQLMulti1");  //查询语句.
			//    en.StartGuidePara2 = this.GetRequestVal("TB_BySQLMulti2");  //列表语句.
			//    en.StartGuideWay = BP.WF.Template.StartGuideWay.BySQLMulti;
			//}
			////多条-子父流程-合卷审批.
			//if (en.StartGuideWay == StartGuideWay.SubFlowGuide)
			//{
			//    en.StartGuidePara1 = this.GetRequestVal("TB_SubFlow1");  //查询语句.
			//    en.StartGuidePara2 = this.GetRequestVal("TB_SubFlow2");  //列表语句.
			//    en.StartGuideWay = BP.WF.Template.StartGuideWay.SubFlowGuide;
			//}

			//BP.WF.Template.FrmNodes fns = new BP.WF.Template.FrmNodes(int.Parse(this.FK_Flow + "01"));
			//if (fns.size() >= 2)
			//{
			//    if (en.StartGuideWay == StartGuideWay.ByFrms)
			//        en.StartGuideWay = BP.WF.Template.StartGuideWay.ByFrms;
			//}

			////右侧的超链接.
			//en.StartGuideLink = this.GetRequestVal("TB_GuideLink");
			//en.StartGuideLab = this.GetRequestVal("TB_GuideLab");

		   // en.Update();
			return "保存成功";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}

		///#endregion


		///#region 流程轨迹查看权限
	/** 
	 流程轨迹查看权限
	 
	 @return 
	*/
	public final String TruckViewPower_Save() throws Exception {
		try
		{
			TruckViewPower en = new TruckViewPower(getFK_Flow());
			en.Retrieve();

			Object tempVar = bp.pub.PubClass.CopyFromRequestByPost(en);
			en = tempVar instanceof TruckViewPower ? (TruckViewPower)tempVar : null;
			en.Save(); //执行保存.
			return "保存成功";
		}
		catch (java.lang.Exception e)
		{
			return "err@保存失败";
		}

	}

		///#endregion 流程轨迹查看权限save


		///#region 数据导入.
	/** 
	 流程模版导入.
	 
	 @return 
	*/
	public final String Imp_Done() throws Exception {
		File xmlFile = null;
		String fileName = UUID.randomUUID().toString();
		try {
			xmlFile = File.createTempFile(fileName, ".xml");
		} catch (IOException e1) {
			xmlFile = new File(System.getProperty("java.io.tmpdir"), fileName + ".xml");
		}
		xmlFile.deleteOnExit();
		HttpServletRequest request = ContextHolderUtils.getRequest();
		try{
			CommonFileUtils.upload(request,"File_Upload", xmlFile);
		}catch(Exception e){
			e.printStackTrace();
			return "err@执行失败";
		}

		String flowNo = this.getFK_Flow();
		String FK_FlowSort = this.GetRequestVal("FK_Sort");

		//检查流程编号
		if (DataType.IsNullOrEmpty(flowNo) == false)
		{
			Flow fl = new Flow(flowNo);
			FK_FlowSort = fl.getFK_FlowSort();
		}

		//检查流程类别编号
		if (DataType.IsNullOrEmpty(FK_FlowSort) == true)
		{
			if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
			{
				FK_FlowSort = bp.web.WebUser.getOrgNo();
			}
			else
			{
				return "err@所选流程类别编号不存在。";
			}
		}

		//导入模式
		ImpFlowTempleteModel model = ImpFlowTempleteModel.forValue(this.GetRequestValInt("ImpWay"));
		if (model == ImpFlowTempleteModel.AsSpecFlowNo)
		{
			flowNo = this.GetRequestVal("SpecFlowNo");
		}

		//执行导入
		Flow flow = TemplateGlo.LoadFlowTemplate(FK_FlowSort, xmlFile.getAbsolutePath(), model, flowNo);
		flow.DoCheck(); //要执行一次检查.

		Hashtable ht = new Hashtable();
		ht.put("FK_Flow", flow.getNo());
		ht.put("FlowName", flow.getName());
		ht.put("FK_FlowSort", flow.getFK_FlowSort());
		ht.put("Msg", "导入成功,流程编号为:" + flow.getNo() + "名称为:" + flow.getName());
		return bp.tools.Json.ToJson(ht);
	}

		///#endregion 数据导入.


		///#region 修改node Icon.
	/** 
	 修改节点ICON
	 
	 @return 
	*/
	public final String NodesIcon_Init() throws Exception {
		DataSet ds = new DataSet();
		Nodes nds = new Nodes(this.getFK_Flow());
		DataTable dt = nds.ToDataTableField("Nodes");
		ds.Tables.add(dt);

		//把文件放入ds.
		String path = SystemConfig.getPathOfWebApp() + "WF/Admin/ClientBin/NodeIcon/";
		String[] strs = bp.tools.BaseFileUtils.getFiles(path);
		//String[] strs = (new File(path)).listFiles();
		DataTable dtIcon = new DataTable();
		dtIcon.Columns.Add("No");
		dtIcon.Columns.Add("Name");
		for (String str : strs)
		{
			String fileName = str.substring(str.lastIndexOf("\\") + 1);
			fileName = fileName.substring(0, fileName.lastIndexOf("."));

			DataRow dr = dtIcon.NewRow();
			dr.setValue(0, fileName);
			dr.setValue(1, fileName);
			dtIcon.Rows.add(dr);
		}

		dtIcon.TableName = "ICONs";
		ds.Tables.add(dtIcon);

		return bp.tools.Json.ToJson(ds);
	}

		///#endregion 修改node Icon.

	/** 
	 流程时限消息设置
	 
	 @return 
	*/
	public final String PushMsgEntity_Init() throws Exception {
		DataSet ds = new DataSet();

		//流程上的字段
		MapAttrs attrs = new MapAttrs();
		attrs.Retrieve(MapAttrAttr.FK_MapData, "ND" + Integer.parseInt(this.getFK_Flow()) + "rpt", "LGType", 0, "MyDataType", 1, null);
		ds.Tables.add(attrs.ToDataTableField("FrmFields"));

		//节点 
		Nodes nds = new Nodes(this.getFK_Flow());
		ds.Tables.add(nds.ToDataTableField("Nodes"));

		//mypk
		PushMsg msg = new PushMsg();
		msg.setMyPK(this.getMyPK());
		msg.RetrieveFromDBSources();
		ds.Tables.add(msg.ToDataTableField("PushMsgEntity"));

		return bp.tools.Json.ToJson(ds);
	}

	/** 
	 流程时限消息设置
	 
	 @return 
	*/
	public final String PushMsg_Save() throws Exception {
		PushMsg msg = new PushMsg();
		msg.setMyPK(this.getMyPK());
		msg.RetrieveFromDBSources();

		msg.setFKEvent(this.getFK_Event()); //流程时限规则
		msg.setFK_Flow(this.getFK_Flow());

		Nodes nds = new Nodes(this.getFK_Flow());


			///#region 其他节点的处理人方式（求选择的节点）
		String nodesOfSMS = "";
		for (Node mynd : nds.ToJavaList())
		{
			for (Object key : CommonUtils.getRequest().getParameterMap().keySet())
			{
				if (key.toString().contains("CB_SMS_" + mynd.getNodeID()) && nodesOfSMS.contains(mynd.getNodeID() + "") == false)
				{
					nodesOfSMS += mynd.getNodeID() + ",";
				}


			}
		}
		msg.setSMSNodes(nodesOfSMS);

			///#endregion 其他节点的处理人方式（求选择的节点）

		//发给指定的人员
		msg.setByEmps(this.GetRequestVal("TB_Emps"));

		//短消息发送设备
		msg.setSMSPushModel(this.GetRequestVal("PushModel"));

		//邮件标题
		msg.setMailTitleReal(this.GetRequestVal("TB_title"));

		//短信内容模版.
		msg.setSMSDocReal(this.GetRequestVal("TB_SMS"));

		//保存.
		if (DataType.IsNullOrEmpty(msg.getMyPK()) == true)
		{
			msg.setMyPK(DBAccess.GenerGUID(0, null, null));
			msg.Insert();
		}
		else
		{
			msg.Update();
		}

		return "保存成功..";
	}


		///#region 欢迎页面初始化.
	/** 
	 欢迎页面初始化-获得数量.
	 
	 @return 
	*/
	public final String GraphicalAnalysis_Init() throws Exception {
		Hashtable ht = new Hashtable();
		String fk_flow = GetRequestVal("FK_Flow");
		//所有的实例数量.
		ht.put("FlowInstaceNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(WorkID) FROM WF_GenerWorkFlow WHERE WFState >1 AND Fk_flow = '" + fk_flow + "'")); //实例数.

		//所有的待办数量.
		ht.put("TodolistNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(WorkID) FROM WF_GenerWorkFlow WHERE WFState=2 AND Fk_flow = '" + fk_flow + "'"));

		//所有的运行中的数量.
		ht.put("RunNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(WorkID) FROM WF_GenerWorkFlow WHERE WFSta!=1 AND WFState!=3 AND Fk_flow = '" + fk_flow + "'"));

		//退回数.
		ht.put("ReturnNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(WorkID) FROM WF_GenerWorkFlow WHERE WFState=5 AND Fk_flow = '" + fk_flow + "'"));

		//说有逾期的数量.
		if (SystemConfig.getAppCenterDBType( ) == DBType.MySQL)
		{
			ht.put("OverTimeNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM WF_EMPWORKS where STR_TO_DATE(SDT,'%Y-%m-%d %H:%i') < now() AND Fk_flow = '" + fk_flow + "'"));

		}
		else if (SystemConfig.getAppCenterDBType( ) == DBType.Oracle|| SystemConfig.getAppCenterDBType( ) == DBType.KingBaseR3 ||SystemConfig.getAppCenterDBType( ) == DBType.KingBaseR6)
		{
			String sql = "SELECT COUNT(*) from (SELECT *  FROM WF_EMPWORKS WHERE  REGEXP_LIKE(SDT, '^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}') AND(sysdate - TO_DATE(SDT, 'yyyy-mm-dd hh24:mi:ss')) > 0 AND Fk_flow = '" + fk_flow + "'";

			sql += "UNION SELECT* FROM WF_EMPWORKS WHERE  REGEXP_LIKE(SDT, '^[0-9]{4}-[0-9]{2}-[0-9]{2}$') AND (sysdate - TO_DATE(SDT, 'yyyy-mm-dd')) > 0 AND Fk_flow = '" + fk_flow + "')";

			ht.put("OverTimeNum", DBAccess.RunSQLReturnValInt(sql));
		}
		else
		{
			ht.put("OverTimeNum", DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM WF_EMPWORKS where convert(varchar(100),SDT,120) < CONVERT(varchar(100), GETDATE(), 120) AND Fk_flow = '" + fk_flow + "'"));
		}

		return bp.tools.Json.ToJson(ht);
	}
	/** 
	 获得数量  流程饼图，部门柱状图，月份折线图.
	 
	 @return 
	*/
	public final String GraphicalAnalysis_DataSet() throws Exception {
		DataSet ds = new DataSet();
		String fk_flow = GetRequestVal("FK_Flow");

			///#region  实例分析
		//月份分组.
		String sql = "SELECT FK_NY, count(WorkID) as Num FROM WF_GenerWorkFlow WHERE WFState >1 AND Fk_flow = '" + fk_flow + "' GROUP BY FK_NY";
		DataTable FlowsByNY = DBAccess.RunSQLReturnTable(sql);
		FlowsByNY.TableName = "FlowsByNY";
		ds.Tables.add(FlowsByNY);

		//部门分组.
		sql = "SELECT DeptName, count(WorkID) as Num FROM WF_GenerWorkFlow WHERE WFState >1 AND Fk_flow = '" + fk_flow + "' GROUP BY DeptName ";
		DataTable FlowsByDept = DBAccess.RunSQLReturnTable(sql);
		FlowsByDept.TableName = "FlowsByDept";
		ds.Tables.add(FlowsByDept);

			///#endregion 实例分析。



			///#region 待办 分析
		//待办 - 部门分组.
		sql = "SELECT DeptName, count(WorkID) as Num FROM WF_EmpWorks WHERE WFState >1 AND Fk_flow = '" + fk_flow + "' GROUP BY DeptName";
		DataTable TodolistByDept = DBAccess.RunSQLReturnTable(sql);
		TodolistByDept.TableName = "TodolistByDept";
		ds.Tables.add(TodolistByDept);

		//逾期的 - 人员分组.
		if (SystemConfig.getAppCenterDBType( ) == DBType.MySQL)
		{
			sql = "SELECT  p.name,COUNT (w.WorkID) AS Num from Port_Emp p,WF_EmpWorks w  WHERE p. NO = w.FK_Emp AND WFState >1 and STR_TO_DATE(SDT,'%Y-%m-%d %H:%i') < now() AND Fk_flow = '" + fk_flow + "' GROUP BY p.name,w.FK_Emp";

		}
		else if (SystemConfig.getAppCenterDBType( ) == DBType.Oracle || SystemConfig.getAppCenterDBType( ) == DBType.KingBaseR3 ||SystemConfig.getAppCenterDBType( ) == DBType.KingBaseR6)
		{
			sql = "SELECT  p.name,COUNT (w.WorkID) AS Num from Port_Emp p,WF_EmpWorks w  WHERE p. NO = w.FK_Emp AND WFState >1 and REGEXP_LIKE(SDT, '^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}') AND(sysdate - TO_DATE(SDT, 'yyyy-mm-dd hh24:mi:ss')) > 0 AND Fk_flow = '" + fk_flow + "' GROUP BY p.name,w.FK_Emp ";
			sql += "UNION SELECT  p.name,COUNT (w.WorkID) AS Num from Port_Emp p,WF_EmpWorks w  WHERE p. NO = w.FK_Emp AND WFState >1 and REGEXP_LIKE(SDT, '^[0-9]{4}-[0-9]{2}-[0-9]{2}$') AND (sysdate - TO_DATE(SDT, 'yyyy-mm-dd')) > 0 AND Fk_flow = '" + fk_flow + "' GROUP BY p.name,w.FK_Emp";
		}
		else
		{
			sql = "SELECT  p.name,COUNT (w.WorkID) AS Num from Port_Emp p,WF_EmpWorks w  WHERE p. NO = w.FK_Emp AND WFState >1 and convert(varchar(100),SDT,120) < CONVERT(varchar(100), GETDATE(), 120) AND Fk_flow = '" + fk_flow + "' GROUP BY p.name,w.FK_Emp";
		}
		DataTable OverTimeByEmp = DBAccess.RunSQLReturnTable(sql);
		OverTimeByEmp.TableName = "OverTimeByEmp";
		ds.Tables.add(OverTimeByEmp);
		//逾期的 - 部门分组.
		if (SystemConfig.getAppCenterDBType( ) == DBType.MySQL)
		{
			sql = "SELECT DeptName, count(WorkID) as Num FROM WF_EmpWorks WHERE WFState >1 and STR_TO_DATE(SDT,'%Y-%m-%d %H:%i') < now() AND Fk_flow = '" + fk_flow + "' GROUP BY DeptName";

		}
		else if (SystemConfig.getAppCenterDBType( ) == DBType.Oracle|| SystemConfig.getAppCenterDBType( ) == DBType.KingBaseR3 ||SystemConfig.getAppCenterDBType( ) == DBType.KingBaseR6)
		{
			sql = "SELECT DeptName, count(WorkID) as Num FROM WF_EmpWorks WHERE WFState >1 and REGEXP_LIKE(SDT, '^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}') AND(sysdate - TO_DATE(SDT, 'yyyy-mm-dd hh24:mi:ss')) > 0 AND Fk_flow = '" + fk_flow + "' GROUP BY DeptName ";
			sql += "UNION SELECT DeptName, count(WorkID) as Num FROM WF_EmpWorks WHERE WFState >1 and REGEXP_LIKE(SDT, '^[0-9]{4}-[0-9]{2}-[0-9]{2}$') AND (sysdate - TO_DATE(SDT, 'yyyy-mm-dd')) > 0 AND Fk_flow = '" + fk_flow + "' GROUP BY DeptName";
		}
		else
		{
			sql = "SELECT DeptName, count(WorkID) as Num FROM WF_EmpWorks WHERE WFState >1 and convert(varchar(100),SDT,120) < CONVERT(varchar(100), GETDATE(), 120) AND Fk_flow = '" + fk_flow + "' GROUP BY DeptName";
		}
		DataTable OverTimeByDept = DBAccess.RunSQLReturnTable(sql);
		OverTimeByDept.TableName = "OverTimeByDept";
		ds.Tables.add(OverTimeByDept);
		//逾期的 - 节点分组.
		if (SystemConfig.getAppCenterDBType( ) == DBType.MySQL)
		{
			sql = "Select NodeName,count(*) as Num from WF_EmpWorks WHERE WFState >1 and STR_TO_DATE(SDT,'%Y-%m-%d %H:%i') < now() AND Fk_flow = '" + fk_flow + "' GROUP BY NodeName";

		}
		else if (SystemConfig.getAppCenterDBType( ) == DBType.Oracle || SystemConfig.getAppCenterDBType( ) == DBType.KingBaseR3 ||SystemConfig.getAppCenterDBType( ) == DBType.KingBaseR6)
		{
			sql = "Select NodeName,count(*) as Num from WF_EmpWorks WHERE WFState >1 and REGEXP_LIKE(SDT, '^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}') AND(sysdate - TO_DATE(SDT, 'yyyy-mm-dd hh24:mi:ss')) > 0 AND Fk_flow = '" + fk_flow + "' GROUP BY NodeName ";
			sql += "UNION Select NodeName,count(*) as Num from WF_EmpWorks WHERE WFState >1 and REGEXP_LIKE(SDT, '^[0-9]{4}-[0-9]{2}-[0-9]{2}$') AND (sysdate - TO_DATE(SDT, 'yyyy-mm-dd')) > 0 AND Fk_flow = '" + fk_flow + "' GROUP BY NodeName";
		}
		else
		{
			sql = "Select NodeName,count(*) as Num from WF_EmpWorks WHERE WFState >1 and convert(varchar(100),SDT,120) < CONVERT(varchar(100), GETDATE(), 120) AND Fk_flow = '" + fk_flow + "' GROUP BY NodeName";
		}
		DataTable OverTimeByNode = DBAccess.RunSQLReturnTable(sql);
		OverTimeByNode.TableName = "OverTimeByNode";
		ds.Tables.add(OverTimeByNode);

			///#endregion 逾期。


		return bp.tools.Json.ToJson(ds);
	}

		///#endregion 欢迎页面初始化.


}