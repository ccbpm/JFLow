package BP.WF.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import BP.DA.DBType;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.Log;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.SFDBSrc;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
import BP.WF.Flow;
import BP.WF.FlowRunWay;
import BP.WF.ImpFlowTempleteModel;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.Platform;
import BP.WF.HttpHandler.Base.WebContralBase;
import BP.WF.Template.DTSField;
import BP.WF.Template.FlowDTSWay;
import BP.WF.Template.StartGuideWay;
import BP.WF.Template.TruckViewPower;

public class WF_Admin_AttrFlow extends WebContralBase {
	// private static final String FK_Flow = null;

	
	/**
	 * 构造函数
	 */
	public WF_Admin_AttrFlow()
	{
	
	}
	
	
	/**
	 * 代码生成器.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String APICodeFEE_Init() throws Exception {

		if (StringHelper.isNullOrEmpty(getFK_Flow()))
			return "err@FK_Flow参数不能为空！";

		Flow flow = new Flow(this.getFK_Flow());

		String tmpPath = "";

		if (BP.WF.Glo.getPlatform() == Platform.CCFlow)
			tmpPath = SystemConfig.getPathOfWebApp() + "/WF/Admin/AttrFlow/APICodeFEE.txt.CCFlow";
		else
			tmpPath = SystemConfig.getPathOfWebApp() + "/WF/Admin/AttrFlow/APICodeFEE.txt.JFlow";
		File file = new File(tmpPath);
		if (!file.exists())
			return String.format("未找到事件编写模板文件“{0}”，请联系管理员！", tmpPath);

		String Title = flow.getName() + "[" + flow.getNo() + "]";
		String code = BP.DA.DataType.ReadTextFile(tmpPath); 
															
		code = code.replace("F001Templepte", String.format("FEE{0}", flow.getNo())).replace("@FlowName", flow.getName())
				.replace("@FlowNo", flow.getNo());

		// 此处将重要行标示出来，根据下面的数组中的项来检索重要行号
		String[] lineStrings = { "namespace BP.FlowEvent", ": BP.WF.FlowEventBase", "public override string FlowMark",
				"public override string SendWhen()", "public override string SendSuccess()",
				"public override string SendError()", "public override string FlowOnCreateWorkID()",
				"public override string FlowOverBefore()", "public override string FlowOverAfter()",
				"public override string BeforeFlowDel()", "public override string AfterFlowDel()",
				"public override string SaveAfter()", "public override string SaveBefore()",
				"public override string UndoneBefore()", "public override string UndoneAfter()",
				"public override string ReturnBefore()", "public override string ReturnAfter()",
				"public override string AskerAfter()", "public override string AskerReAfter()" };

		// this.Pub1.AddLi(string.Format("<a
		// href=\"APICodeFEE.aspx?FK_Flow={0}&Download=1\" target=\"_blank\"
		// class=\"easyui-linkbutton\"
		// data-options=\"iconCls:'icon-save',plain:true\">下载代码</a><br />",
		// FK_Flow));

		String msg = "<script type=\"text/javascript\">SyntaxHighlighter.highlight();</script>";
		msg += String.format(
				"<pre type=\"syntaxhighlighter\" class=\"brush: csharp; html-script: false; highlight: [{2}]\" title=\"{0}[编号：{1}] 流程自定义事件代码生成\">",
				flow.getName(), flow.getNo(), APICodeFEE_Init_GetImportantLinesNumbers(lineStrings, code));
		msg += code.replace("<", "&lt;"); // SyntaxHighlighter中，使用<Pre>包含的代码要将左尖括号改成其转义形式
		msg += "</pre>";

		return msg;
	}

	/**
	 * 获取重要行的标号连接字符串，如3,6,8
	 * 
	 * @param lineInStrings
	 *            重要行中包含的字符串数组，只要行中包含其中的一项字符串，则这行就是重要行
	 * @param str
	 *            要检索的字符串，使用Environment.NewLine分行
	 * @return
	 */

	private String APICodeFEE_Init_GetImportantLinesNumbers(String[] lineInStrings, String str) {
		String[] lines = str.replace("\r\n", "`").split("`", 0);
		String nums = "";

		for (int i = 0; i < lines.length; i++) {
			for (String instr : lineInStrings) {
				if (lines[i].indexOf(instr) != -1) {
					nums += (i + 1) + ",";
					break;
				}
			}
		}
		return nums.substring(nums.length() - 1);
		// return nums.TrimEnd(',');
	}

	// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	/// #region 发起限制.
	public final String Limit_Init() throws Exception {
		
		BP.WF.Flow fl = new BP.WF.Flow();
		fl.setNo(this.getFK_Flow());
		fl.RetrieveFromDBSources();
		return fl.ToJson();
		
	}

	public final String Limit_Save() throws Exception {
		BP.WF.Flow fl = new BP.WF.Flow(this.getFK_Flow());
		fl.SetValByKey("StartLimitRole", this.GetRequestValInt("StartLimitRole"));
		fl.setStartLimitPara(this.GetRequestVal("StartLimitPara"));
		fl.setStartLimitAlert(this.GetRequestVal("StartLimitAlert"));
		fl.Update();
		return "保存成功.";
	}
	// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	/// #endregion 发起限制.

	/**
	 * 执行流程检查.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String CheckFlow_Init() throws Exception {
		BP.WF.Flow fl = new BP.WF.Flow(this.getFK_Flow());
		String str = fl.DoCheck();
		 str = str.replace("@", "<BR>@");
         str = str.replace("@警告:", "<label style='color:yellow ;'>@警告:</label>");
         str = str.replace("@错误:", "<label style='color:red ;'>@错误:</label>");
		if ("".equals(str))
			str = "检查成功.";
		return str;
	}

	/**
	 * 流程字段列表
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public String FlowFields_Init() throws NumberFormatException, Exception {
		BP.Sys.MapAttrs attrs = new BP.Sys.MapAttrs("ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt");
		return attrs.ToJson();
	}

	/**
	 * 执行初始化
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String AutoStart_Init() throws Exception {
		BP.WF.Flow en = new BP.WF.Flow();
		en.setNo(this.getFK_Flow());
		en.RetrieveFromDBSources();
		return en.ToJson();
	}

	/**
	 * 执行保存
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String AutoStart_Save() throws Exception {
		// 执行保存.
		BP.WF.Flow en = new BP.WF.Flow(this.getFK_Flow());

		en.SetValByKey(BP.WF.Template.FlowAttr.FlowRunWay, this.GetRequestValInt("RB_FlowRunWay"));

		if (en.getHisFlowRunWay() == FlowRunWay.SpecEmp)
			en.setRunObj(this.GetRequestVal("TB_SpecEmp"));

		if (en.getHisFlowRunWay() == FlowRunWay.DataModel)
			en.setRunObj(this.GetRequestVal("TB_DataModel"));

		en.DirectUpdate();
		return "保存成功.";
	}

	/**
	 * 初始化节点属性列表.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String NodeAttrs_Init() throws Exception {
		String strFlowId = GetRequestVal("FK_Flow");
		if (StringHelper.isNullOrEmpty(strFlowId)) {
			return "err@参数错误！";
		}

		Nodes nodes = new Nodes();
		nodes.Retrieve("FK_Flow", strFlowId);
		// 因直接使用nodes.ToJson()无法获取某些字段（e.g.HisFormTypeText,原因：Node没有自己的Attr类）
		// 故此处手动创建前台所需的DataTable
		DataTable dt = new DataTable();
		dt.Columns.Add("NodeID"); // 节点ID
		dt.Columns.Add("Name"); // 节点名称
		dt.Columns.Add("HisFormType"); // 表单方案
		dt.Columns.Add("HisFormTypeText");
		dt.Columns.Add("HisRunModel"); // 节点类型
		dt.Columns.Add("HisRunModelT");

		dt.Columns.Add("HisDeliveryWay"); // 接收方类型
		dt.Columns.Add("HisDeliveryWayText");
		dt.Columns.Add("HisDeliveryWayJsFnPara");
		dt.Columns.Add("HisDeliveryWayCountLabel");
		dt.Columns.Add("HisDeliveryWayCount"); // 接收方Count

		dt.Columns.Add("HisCCRole"); // 抄送人
		dt.Columns.Add("HisCCRoleText");
		dt.Columns.Add("HisFrmEventsCount"); // 消息&事件Count
		dt.Columns.Add("HisFinishCondsCount"); // 流程完成条件Count
		DataRow dr;
		for (Node node : nodes.ToJavaList()) {
			dr = dt.NewRow();
			dr.setValue("NodeID", node.getNodeID());
			dr.setValue("Name", node.getName());
			dr.setValue("HisFormType", node.getHisFormType());
			dr.setValue("HisFormTypeText", node.getHisFormTypeText());
			dr.setValue("HisRunModel", node.getHisRunModel());
			dr.setValue("HisRunModelT", node.getHisRunModelT());
			dr.setValue("HisDeliveryWay", node.getHisDeliveryWay());
			dr.setValue("HisDeliveryWayText", node.getHisDeliveryWayText());

			// 接收方数量
			int intHisDeliveryWayCount = 0;
			if (node.getHisDeliveryWay() == BP.WF.DeliveryWay.ByStation) {
				dr.setValue("HisDeliveryWayJsFnPara", "ByStation");
				dr.setValue("HisDeliveryWayCountLabel", "岗位");
				BP.WF.Template.NodeStations nss = new BP.WF.Template.NodeStations();
				intHisDeliveryWayCount = nss.Retrieve(BP.WF.Template.NodeStationAttr.FK_Node, node.getNodeID());
			} else if (node.getHisDeliveryWay() == BP.WF.DeliveryWay.ByDept) {
				dr.setValue("HisDeliveryWayJsFnPara", "ByDept");
				dr.setValue("HisDeliveryWayCountLabel", "部门");
				BP.WF.Template.NodeDepts nss = new BP.WF.Template.NodeDepts();
				intHisDeliveryWayCount = nss.Retrieve(BP.WF.Template.NodeDeptAttr.FK_Node, node.getNodeID());
			} else if (node.getHisDeliveryWay() == BP.WF.DeliveryWay.ByBindEmp) {
				dr.setValue("HisDeliveryWayJsFnPara", "ByDept");
				dr.setValue("HisDeliveryWayCountLabel", "人员");
				BP.WF.Template.NodeEmps nes = new BP.WF.Template.NodeEmps();
				intHisDeliveryWayCount = nes.Retrieve(BP.WF.Template.NodeStationAttr.FK_Node, node.getNodeID());
			}
			dr.setValue("HisDeliveryWayCount", intHisDeliveryWayCount);

			// 抄送
			dr.setValue("HisCCRole", node.getHisCCRole());
			dr.setValue("HisCCRoleText", node.getHisCCRoleText());

			// 消息&事件Count
			BP.Sys.FrmEvents fes = new BP.Sys.FrmEvents();
			dr.setValue("HisFrmEventsCount", fes.Retrieve(BP.Sys.FrmEventAttr.FK_MapData, "ND" + node.getNodeID()));

			// 流程完成条件Count
			BP.WF.Template.Conds conds = new BP.WF.Template.Conds(BP.WF.Template.CondType.Flow, node.getNodeID());
			dr.setValue("HisFinishCondsCount", conds.size());

			dt.Rows.add(dr);
		}
		return BP.Tools.Json.ToJson(dt);
	}

	/**
	 * 与业务表数据同步
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String DTSBTable_Init() throws Exception {
		DataSet ds = new DataSet();

		// 获得数据源的表.
		BP.Sys.SFDBSrc src = new SFDBSrc("local");
		DataTable dt = src.GetTables();
		dt.TableName = "Tables";
		ds.Tables.add(dt);

		// 把节点信息放入.
		BP.WF.Nodes nds = new Nodes(this.getFK_Flow());
		DataTable dtNode = nds.ToDataTableField("Nodes");
		ds.Tables.add(dtNode);

		// 把流程信息放入.
		BP.WF.Flow fl = new BP.WF.Flow(this.getFK_Flow());
		DataTable dtFlow = fl.ToDataTableField(this.getFK_Flow());
		ds.Tables.add(dtFlow);

		return BP.Tools.Json.ToJson(ds);
	}

	/**
	 * 与业务表数据同步
	 * @throws Exception 
	 */
	public String DTSBTable_Save() throws Exception {
		Flow flow = new Flow(this.getFK_Flow());

		BP.WF.Template.FlowDTSWay dtsWay = (FlowDTSWay.forValue(this.GetRequestValInt("RB_DTSWay")));

		flow.setDTSWay(dtsWay);
		if (flow.getDTSWay() == FlowDTSWay.None) {
			flow.Update();
			return "保存成功.";
		}

		flow.setDTSDBSrc(this.GetRequestVal("DDL_DBSrc"));
		flow.setDTSBTable(this.GetRequestVal("DDL_Table"));

		DTSField field = (DTSField.forValue(this.GetRequestValInt("DTSField")));

		if (field == null)
			field = DTSField.SameNames;
		flow.setDTSField(field);

		SFDBSrc s = new SFDBSrc("local");
		if (field == DTSField.SameNames) {
			DataTable dt = s.GetColumns(flow.getPTable());

			s = new SFDBSrc(flow.getDTSDBSrc());// this.src);
			DataTable ywDt = s.GetColumns(flow.getDTSBTable());// this.ywTableName);

			String str = "";
			String ywStr = "";
			for (DataRow ywDr : ywDt.Rows) {
				for (DataRow dr : dt.Rows) {
					if (ywDr.getValue("No").toString().toUpperCase() == dr.getValue("No").toString().toUpperCase()) {
						if (dr.getValue("No").toString().toUpperCase().equals("OID")) {
							flow.setDTSBTablePK("OID");
						}
						str += dr.getValue("No").toString() + ",";
						ywStr += ywDr.getValue("No").toString() + ",";
					}
				}
			}

			if (!StringHelper.isNullOrEmpty(str))
				flow.setDTSFields(str.substring(str.length() - 1) + "@" + ywStr.substring(ywStr.length() - 1));
			else {
				Log.DebugWriteError("未检测到业务主表【" + flow.getPTable() + "】与表【" + flow.getDTSBTable() + "】有相同的字段名.");
				return "";// 不执行保存
			}
		} else// 按设置的字段匹配 检查在
		{
			try {
				s = new SFDBSrc("local");
				String str = flow.getDTSFields();

				String[] arr = str.split("@", 0);

				String sql = "SELECT " + arr[0] + " FROM " + flow.getPTable();

				s.RunSQL(sql);

				s = new SFDBSrc(flow.getDTSDBSrc());

				sql = "SELECT " + arr[1] + ", " + flow.getDTSBTablePK() + " FROM " + flow.getDTSBTable();

				s.RunSQL(sql);

			} catch (Exception e) {
				// PubClass.Alert(ex.Message);
				Log.DebugWriteError("设置的字段有误.【" + flow.getDTSFields() + "】");
				return "";// 不执行保存
			}
		}
		flow.Update();
		return flow.ToJson();
	}

	/**
	 * 数据调度 - 字段映射.
	 * @throws Exception 
	 */
	public String DTSBTableExt_Init() throws Exception {
		// 定义数据容器.
		DataSet ds = new DataSet();

		// 获得数据表列.
		SFDBSrc src = new SFDBSrc(this.GetRequestVal("FK_DBSrc"));
		DataTable dtColms = src.GetColumns(this.GetRequestVal("TableName"));
		dtColms.TableName = "Cols";

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle) {
			dtColms.Columns.get("NO").ColumnName = "No";
			dtColms.Columns.get("NAME").ColumnName = "Name";
		}

		ds.Tables.add(dtColms);

		// 属性列表.
		MapAttrs attrs = new MapAttrs("ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt");
		DataTable dtAttrs = attrs.ToDataTableStringField("Sys_MapAttr");
		ds.Tables.add(dtAttrs);

		// 转化成json,返回.
		return BP.Tools.Json.ToJson(ds);
	}

	public String DTSBTableExt_Save() throws Exception {
		String rpt = "ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt";
		Flow fl = new Flow(this.getFK_Flow());
		MapAttrs attrs = new MapAttrs(rpt);

		String pk = this.GetRequestVal("DDL_OID");
		if (StringHelper.isNullOrEmpty(pk) == true)
			return "err@必须设置业务表的主键，否则无法同步。";

		String lcStr = "";// 要同步的流程字段
		String ywStr = "";// 第三方字段
		String err = "";
		for (MapAttr attr : attrs.ToJavaList()) {
			int val = this.GetRequestValInt("CB_" + attr.getKeyOfEn());
			if (val == 0)
				continue;

			String refField = this.GetRequestVal("DDL_" + attr.getKeyOfEn());

			// 如果选中的业务字段重复，抛出异常
			if (ywStr.contains("@" + refField + "@")) {
				err += "@配置【" + attr.getKeyOfEn() + " - " + attr.getName() + "】错误, 请确保选中业务字段的唯一性，该业务字段已经被其他字段所使用。";
			}
			lcStr += "@" + attr.getKeyOfEn() + "@,";
			ywStr += "@" + refField + "@,";
		}

		// BP.Web.Controls.RadioBtn rb = this.Pub1.GetRadioBtnByID("rb_workId");

		int pkModel = this.GetRequestValInt("PKModel");

		String ddl_key = this.GetRequestVal("DDL_OID");
		if (pkModel == 0) {
			if (ywStr.contains("@" + ddl_key + "@")) {
				err += "@请确保选中业务字段的唯一性，该业务字段【" + ddl_key + "】已经被其他字段所使用。";
			}
			lcStr = "@OID@," + lcStr;
			ywStr = "@" + ddl_key + "@," + ywStr;
		} else {
			if (ywStr.contains("@" + ddl_key + "@")) {
				err += "@请确保选中业务字段的唯一性，该业务字段【" + ddl_key + "】已经被其他字段所使用。";
			}
			lcStr = "@GUID@," + lcStr;
			ywStr = "@" + ddl_key + "@," + ywStr;
		}

		if (err != "")
			return "err@" + err;

		lcStr = lcStr.replace("@", "");
		ywStr = ywStr.replace("@", "");

		// 去除最后一个字符的操作
		if (StringHelper.isNullOrEmpty(lcStr) || StringHelper.isNullOrEmpty(ywStr)) {
			return "err@要配置的内容为空...";
		}
		lcStr = lcStr.substring(0, lcStr.length() - 1);
		ywStr = ywStr.substring(0, ywStr.length() - 1);

		// 数据存储格式 a,b,c@a_1,b_1,c_1
		fl.setDTSFields(lcStr + "@" + ywStr);
		fl.setDTSBTablePK(pk);
		fl.Update();

		return "设置成功.";
	}

	/**
	 * 前置导航
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String StartGuide_Init() throws Exception {
		BP.WF.Flow en = new BP.WF.Flow();
		en.setNo(this.getFK_Flow());
		en.RetrieveFromDBSources();

		BP.WF.Template.FrmNodes fns = new BP.WF.Template.FrmNodes(Integer.parseInt(this.getFK_Flow() + "01"));

		if (fns.size() > 2) {
			en.getRow().put("nodesCount", "true");
		} else {
			en.getRow().put("nodesCount", "false");
		}

		en.getRow().put("userId", BP.Web.WebUser.getSID());
		// en.Row = ht;
		return en.ToJson();

	}

	/**
	 * 前置导航save
	 * 
	 * @return
	 */
	public String StartGuide_Save() {
		try {
			Flow en = new Flow();
			en.setNo(this.getFK_Flow());
			en.Retrieve();

			int val = this.GetRequestValInt("RB_StartGuideWay");

			en.SetValByKey(BP.WF.Template.FlowAttr.StartGuideWay, val);

			if (en.getStartGuideWay() == StartGuideWay.None) {
				en.setStartGuideWay(BP.WF.Template.StartGuideWay.None);
			}

			if (en.getStartGuideWay() == StartGuideWay.ByHistoryUrl) {
				en.setStartGuidePara1(this.GetRequestVal("TB_ByHistoryUrl"));
				en.setStartGuidePara2("");
				en.setStartGuideWay(BP.WF.Template.StartGuideWay.ByHistoryUrl);
			}

			if (en.getStartGuideWay() == StartGuideWay.BySelfUrl) {
				en.setStartGuidePara1(this.GetRequestVal("TB_SelfURL"));
				en.setStartGuidePara2("");
				en.setStartGuideWay(BP.WF.Template.StartGuideWay.BySelfUrl);
			}

			// 单条模式.
			if (en.getStartGuideWay() == StartGuideWay.BySQLOne) {
				en.setStartGuidePara1(this.GetRequestVal("TB_BySQLOne1")); // 查询语句.
				en.setStartGuidePara2(this.GetRequestVal("TB_BySQLOne2")); // 列表语句.
				en.setStartGuideWay(BP.WF.Template.StartGuideWay.BySQLOne);
			}

			// 多条-子父流程-合卷审批.
			if (en.getStartGuideWay() == StartGuideWay.SubFlowGuide) {
				en.setStartGuidePara1(this.GetRequestVal("TB_SubFlow1")); // 查询语句.
				en.setStartGuidePara2(this.GetRequestVal("TB_SubFlow2")); // 列表语句.
				en.setStartGuideWay(BP.WF.Template.StartGuideWay.SubFlowGuide);
			}

			BP.WF.Template.FrmNodes fns = new BP.WF.Template.FrmNodes(Integer.parseInt(this.getFK_Flow() + "01"));
			if (fns.size() >= 2) {
				if (en.getStartGuideWay() == (StartGuideWay.ByFrms))
					en.setStartGuideWay(BP.WF.Template.StartGuideWay.ByFrms);
			}

			// 右侧的超链接.
			en.setStartGuideLink(this.GetRequestVal("TB_GuideLink"));
			en.setStartGuideLab(this.GetRequestVal("TB_GuideLab"));

			en.Update();
			return "保存成功";
		} catch (Exception ex) {
			return "err@" + ex.getMessage();
		}
	}

	/**
	 * 流程轨迹查看权限
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String TruckViewPower_Init() throws Exception {
		if (StringHelper.isNullOrEmpty(getFK_Flow())) {
			Log.DebugWriteError("流程编号为空");
			return "err@流程编号为空";
		} else {
			BP.WF.Template.TruckViewPower en = new BP.WF.Template.TruckViewPower(getFK_Flow());
			en.Retrieve();
			return en.ToJson();
		}
	}

	/**
	 * 流程轨迹查看权限
	 * 
	 * @return
	 */
	public String TruckViewPower_Save() {
		try {
			BP.WF.Template.TruckViewPower en = new BP.WF.Template.TruckViewPower(getFK_Flow());
			en.Retrieve();

			en = (TruckViewPower) BP.Sys.PubClass.CopyFromRequestByPost(en, getRequest());
			en.Save(); // 执行保存.
			return "保存成功";
		} catch (Exception e) {
			return "err@保存失败";
		}

	}

	/**
	 * 修改节点ICON
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String NodesIcon_Init() throws Exception {
		DataSet ds = new DataSet();
		Nodes nds = new Nodes(this.getFK_Flow());
		DataTable dt = nds.ToDataTableField("Nodes");
		ds.Tables.add(dt);

		// 把文件放入ds.
        String path = SystemConfig.getPathOfWebApp() + "/WF/Admin/ClientBin/NodeIcon/";

		
	//	string path= BP.sys..SystemConfig
		
		File[] files = new File( path).listFiles();		
		

		DataTable dtIcon = new DataTable();
		dtIcon.Columns.Add("No");
		dtIcon.Columns.Add("Name");
		for (File fl : files) {
			
			String fileName =fl.getName(); // = str.substring(str.lastIndexOf("\\") + 1);
			fileName=  fileName.substring(fileName.lastIndexOf("\\") + 1);
			
			fileName = fileName.substring(0, fileName.lastIndexOf("."));

			DataRow dr = dtIcon.NewRow();
			dr.setValue(0,fileName);
			dr.setValue(1,fileName);
			dtIcon.Rows.add(dr);
		}

		dtIcon.TableName = "ICONs";
		ds.Tables.add(dtIcon);

		return BP.Tools.Json.ToJson(ds);
	}

	public final String NodesIconSelect_Save() throws Exception {
		String icon = this.GetRequestVal("ICON");

		Node nd = new Node(this.getFK_Node());
		nd.setICON(icon);
		nd.Update();

		return "保存成功...";
	}

	public void setMultipartRequest(DefaultMultipartHttpServletRequest request) {
		this.request = request;
	}

	private DefaultMultipartHttpServletRequest request;

	// / <summary>
	// / 流程模版导入.
	// / </summary>
	// / <returns></returns>
	public String Imp_Done() throws Exception {
		File xmlFile = null;
		String fileName = UUID.randomUUID().toString();
		try {
			xmlFile = File.createTempFile(fileName, ".xml");
		} catch (IOException e1) {
			xmlFile = new File(System.getProperty("java.io.tmpdir"), fileName + ".xml");
		}
		xmlFile.deleteOnExit();
		String contentType = getRequest().getContentType();
		if (contentType != null && contentType.indexOf("multipart/form-data") != -1) {
			MultipartFile multipartFile = request.getFile("File_Upload");
			try {
				multipartFile.transferTo(xmlFile);
			} catch (Exception e) {
				e.printStackTrace();
				return "err@执行失败";
			}
		}
		
		String flowNo = this.getFK_Flow();
		String FK_FlowSort = this.GetRequestVal("FK_Sort");
		// 检查流程编号
		if (flowNo != null && !"".equals(flowNo)) {
			Flow fl = new Flow(flowNo);
			FK_FlowSort = fl.getFK_FlowSort();
		}
		// 检查流程类别编号
		if (FK_FlowSort == null || "".equals(FK_FlowSort)) {
			return "err@所选流程类别编号不存在。";
		}
		// 导入模式
		ImpFlowTempleteModel model = ImpFlowTempleteModel.forValue(this.GetRequestValInt("ImpWay"));
		if (model == ImpFlowTempleteModel.AsSpecFlowNo)
			flowNo = this.GetRequestVal("SpecFlowNo");

		// 执行导入
		BP.WF.Flow flow;
		try {
			flow = BP.WF.Flow.DoLoadFlowTemplate(FK_FlowSort, xmlFile.getAbsolutePath(), model, flowNo);
			Hashtable<String, String> ht = new Hashtable<String, String>();
			ht.put("FK_Flow", flow.getNo());
			ht.put("FlowName", flow.getName());
			ht.put("FK_FlowSort", flow.getFK_FlowSort());
			ht.put("Msg", "导入成功,流程编号为:" + flow.getNo() + "名称为:" + flow.getName());
			return BP.Tools.Json.ToJson(ht);
		} catch (Exception e) {
			return "err@导入失败: " + e.getMessage();
		}

	}

}
 