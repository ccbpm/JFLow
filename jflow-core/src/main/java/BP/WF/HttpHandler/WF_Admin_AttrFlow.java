package BP.WF.HttpHandler;

import BP.Web.*;
import BP.Sys.*;
import BP.DA.*;
import BP.WF.Template.*;
import BP.WF.*;
import java.util.*;
import java.io.*;
import java.time.*;

public class WF_Admin_AttrFlow extends BP.WF.HttpHandler.DirectoryPageBase
{
	 /** 
	 构造函数
	 */
	public WF_Admin_AttrFlow()
	{
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region APICodeFEE_Init.
	/** 
	 代码生成器.
	 
	 @return 
	*/
	public final String APICodeFEE_Init()
	{
		if (tangible.StringHelper.isNullOrWhiteSpace(getFK_Flow()))
		{
			return "err@FK_Flow参数不能为空！";
		}

		Flow flow = new Flow(this.getFK_Flow());

		String tmpPath = "";

		if (BP.WF.Glo.getPlatform() == Platform.CCFlow)
		{
			tmpPath = SystemConfig.PathOfWebApp + "\\WF\\Admin\\AttrFlow\\APICodeFEE.txt.CCFlow";
		}
		else
		{
			tmpPath = SystemConfig.PathOfWebApp + "\\WF\\Admin\\AttrFlow\\APICodeFEE.txt.JFlow";
		}

		if ((new File(tmpPath)).isFile() == false)
		{
			return String.format("未找到事件编写模板文件“%1$s”，请联系管理员！", tmpPath);
		}

		String Title = flow.Name + "[" + flow.No + "]";
		String code = BP.DA.DataType.ReadTextFile(tmpPath); //, System.Text.Encoding.UTF8).Replace("F001Templepte", string.Format("FEE{0}", flow.No)).Replace("@FlowName", flow.Name).Replace("@FlowNo", flow.No);
		code = code.replace("F001Templepte", String.format("FEE%1$s", flow.No)).replace("@FlowName", flow.Name).replace("@FlowNo", flow.No);


		//此处将重要行标示出来，根据下面的数组中的项来检索重要行号
		String[] lineStrings = new String[] {"namespace BP.FlowEvent", ": BP.WF.FlowEventBase", "public override string FlowMark", "public override string SendWhen()", "public override string SendSuccess()", "public override string SendError()", "public override string FlowOnCreateWorkID()", "public override string FlowOverBefore()", "public override string FlowOverAfter()", "public override string BeforeFlowDel()", "public override string AfterFlowDel()", "public override string SaveAfter()", "public override string SaveBefore()", "public override string UndoneBefore()", "public override string UndoneAfter()", "public override string ReturnBefore()", "public override string ReturnAfter()", "public override string AskerAfter()", "public override string AskerReAfter()"};


		//this.Pub1.AddLi(string.Format("<a href=\"APICodeFEE.aspx?FK_Flow={0}&Download=1\" target=\"_blank\" class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-save',plain:true\">下载代码</a><br />", FK_Flow));

		String msg = "<script type=\"text/javascript\">SyntaxHighlighter.highlight();</script>";
		msg += String.format("<pre type=\"syntaxhighlighter\" class=\"brush: csharp; html-script: false; highlight: [%3$s]\" title=\"%1$s[编号：%2$s] 流程自定义事件代码生成\">", flow.Name, flow.No, APICodeFEE_Init_GetImportantLinesNumbers(lineStrings, code));
		msg += code.replace("<", "&lt;"); //SyntaxHighlighter中，使用<Pre>包含的代码要将左尖括号改成其转义形式
		msg += "</pre>";

		return msg;
	}
	/** 
	 获取重要行的标号连接字符串，如3,6,8
	 
	 @param lineInStrings 重要行中包含的字符串数组，只要行中包含其中的一项字符串，则这行就是重要行
	 @param str 要检索的字符串，使用Environment.NewLine分行
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

		return tangible.StringHelper.trimEnd(nums, ',');
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion APICodeFEE_Init.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 节点属性（列表）的操作
	/** 
	 初始化节点属性列表.
	 
	 @return 
	*/
	public final String NodeAttrs_Init()
	{
		String strFlowId = GetRequestVal("FK_Flow");
		if (DataType.IsNullOrEmpty(strFlowId))
		{
			return "err@参数错误！";
		}

		Nodes nodes = new Nodes();
		nodes.Retrieve("FK_Flow", strFlowId);
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
		for (Node node : nodes)
		{
			dr = dt.NewRow();
			dr.set("NodeID", node.getNodeID());
			dr.set("Name", node.getName());
			dr.set("HisFormType", node.getHisFormType());
			dr.set("HisFormTypeText", node.getHisFormTypeText());
			dr.set("HisRunModel", node.getHisRunModel());
			dr.set("HisRunModelT", node.getHisRunModelT());
			dr.set("HisDeliveryWay", node.getHisDeliveryWay());
			dr.set("HisDeliveryWayText", node.getHisDeliveryWayText());

			//接收方数量
			int intHisDeliveryWayCount = 0;
			if (node.getHisDeliveryWay() == BP.WF.DeliveryWay.ByStation)
			{
				dr.set("HisDeliveryWayJsFnPara", "ByStation");
				dr.set("HisDeliveryWayCountLabel", "岗位");
				BP.WF.Template.NodeStations nss = new BP.WF.Template.NodeStations();
				intHisDeliveryWayCount = nss.Retrieve(BP.WF.Template.NodeStationAttr.FK_Node, node.getNodeID());
			}
			else if (node.getHisDeliveryWay() == BP.WF.DeliveryWay.ByDept)
			{
				dr.set("HisDeliveryWayJsFnPara", "ByDept");
				dr.set("HisDeliveryWayCountLabel", "部门");
				BP.WF.Template.NodeDepts nss = new BP.WF.Template.NodeDepts();
				intHisDeliveryWayCount = nss.Retrieve(BP.WF.Template.NodeDeptAttr.FK_Node, node.getNodeID());
			}
			else if (node.getHisDeliveryWay() == BP.WF.DeliveryWay.ByBindEmp)
			{
				dr.set("HisDeliveryWayJsFnPara", "ByDept");
				dr.set("HisDeliveryWayCountLabel", "人员");
				BP.WF.Template.NodeEmps nes = new BP.WF.Template.NodeEmps();
				intHisDeliveryWayCount = nes.Retrieve(BP.WF.Template.NodeStationAttr.FK_Node, node.getNodeID());
			}
			dr.set("HisDeliveryWayCount", intHisDeliveryWayCount);

			//抄送
			dr.set("HisCCRole", node.getHisCCRole());
			dr.set("HisCCRoleText", node.getHisCCRoleText());

			//消息&事件Count
			BP.Sys.FrmEvents fes = new BP.Sys.FrmEvents();
			dr.set("HisFrmEventsCount", fes.Retrieve(BP.Sys.FrmEventAttr.FK_MapData, "ND" + node.getNodeID()));

			//流程完成条件Count
			BP.WF.Template.Conds conds = new BP.WF.Template.Conds(BP.WF.Template.CondType.Flow, node.getNodeID());
			dr.set("HisFinishCondsCount", conds.size());

			dt.Rows.Add(dr);
		}
		return BP.Tools.Json.ToJson(dt);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 与业务表数据同步
	public final String DTSBTable_Init()
	{
		DataSet ds = new DataSet();

		//获得数据源的表.
		BP.Sys.SFDBSrc src = new SFDBSrc("local");
		DataTable dt = src.GetTables();
		dt.TableName = "Tables";
		ds.Tables.Add(dt);


		//把节点信息放入.
		BP.WF.Nodes nds = new Nodes(this.getFK_Flow());
		DataTable dtNode = nds.ToDataTableField("Nodes");
		ds.Tables.Add(dtNode);


		// 把流程信息放入.
		BP.WF.Flow fl = new BP.WF.Flow(this.getFK_Flow());
		DataTable dtFlow = fl.ToDataTableField("Flow");
		ds.Tables.Add(dtFlow);

		return BP.Tools.Json.DataSetToJson(ds, false);
	}

	/** 
	 与业务表数据同步
	 
	 @return 
	*/
	public final String DTSBTable_Save()
	{
		Flow flow = new Flow(this.getFK_Flow());

		BP.WF.Template.FlowDTSWay dtsWay = BP.WF.Template.FlowDTSWay.forValue(this.GetRequestValInt("RB_DTSWay"));

		flow.setDTSWay(dtsWay);
		if (flow.getDTSWay() == FlowDTSWay.None)
		{
			flow.Update();
			return "保存成功.";
		}

		flow.setDTSDBSrc(this.GetRequestVal("DDL_DBSrc"));
		flow.setDTSBTable(this.GetRequestVal("DDL_Table"));

		DTSField field = DTSField.forValue(this.GetRequestValInt("DTSField"));

		if (field == 0)
		{
			field = DTSField.SameNames;
		}
		flow.setDTSField(field);

		SFDBSrc s = new SFDBSrc("local");
		if (field == DTSField.SameNames)
		{
			DataTable dt = s.GetColumns(flow.getPTable());

			s = new SFDBSrc(flow.getDTSDBSrc()); // this.src);
			DataTable ywDt = s.GetColumns(flow.getDTSBTable()); // this.ywTableName);

			String str = "";
			String ywStr = "";
			for (DataRow ywDr : ywDt.Rows)
			{
				for (DataRow dr : dt.Rows)
				{
					if (ywDr.get("No").toString().toUpperCase().equals(dr.get("No").toString().toUpperCase()))
					{
						if (dr.get("No").toString().toUpperCase().equals("OID"))
						{
							flow.setDTSBTablePK("OID");
						}
						str += dr.get("No").toString() + ",";
						ywStr += ywDr.get("No").toString() + ",";
					}
				}
			}

			if (!DataType.IsNullOrEmpty(str))
			{
				flow.setDTSFields(tangible.StringHelper.trimEnd(str, ',') + "@" + tangible.StringHelper.trimEnd(ywStr, ','));
			}
			else
			{
				PubClass.Alert("未检测到业务主表【" + flow.getPTable() + "】与表【" + flow.getDTSBTable() + "】有相同的字段名.");
				return ""; //不执行保存
			}
		}
		else //按设置的字段匹配   检查在
		{
			try
			{
				s = new SFDBSrc("local");
				String str = flow.getDTSFields();

				String[] arr = str.split("[@]", -1);


				String sql = "SELECT " + arr[0] + " FROM " + flow.getPTable();

				s.RunSQL(sql);

				s = new SFDBSrc(flow.getDTSDBSrc());

				sql = "SELECT " + arr[1] + ", " + flow.getDTSBTablePK() + " FROM " + flow.getDTSBTable();

				s.RunSQL(sql);

			}
			catch (java.lang.Exception e)
			{
				//PubClass.Alert(ex.Message);
				PubClass.Alert("设置的字段有误.【" + flow.getDTSFields() + "】");
				return ""; //不执行保存
			}
		}
		flow.Update();
		return flow.ToJson();
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 数据调度 - 字段映射.
	public final String DTSBTableExt_Init()
	{
		//定义数据容器.
		DataSet ds = new DataSet();

		//获得数据表列.
		SFDBSrc src = new SFDBSrc(this.GetRequestVal("FK_DBSrc"));
		DataTable dtColms = src.GetColumns(this.GetRequestVal("TableName"));
		dtColms.TableName = "Cols";

		if (SystemConfig.AppCenterDBType == DBType.Oracle || SystemConfig.AppCenterDBType == DBType.PostgreSQL)
		{
			dtColms.Columns["NO"].ColumnName = "No";
			dtColms.Columns["NAME"].ColumnName = "Name";
		}

		ds.Tables.Add(dtColms);

		//属性列表.
		MapAttrs attrs = new MapAttrs("ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt");
		DataTable dtAttrs = attrs.ToDataTableStringField("Sys_MapAttr");
		ds.Tables.Add(dtAttrs);

		//转化成json,返回.
		return BP.Tools.Json.DataSetToJson(ds, false);
	}
	public final String DTSBTableExt_Save()
	{
		String rpt = "ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt";
		Flow fl = new Flow(this.getFK_Flow());
		MapAttrs attrs = new MapAttrs(rpt);

		String pk = this.GetRequestVal("DDL_OID");
		if (DataType.IsNullOrEmpty(pk) == true)
		{
			return "err@必须设置业务表的主键，否则无法同步。";
		}


		String lcStr = ""; //要同步的流程字段
		String ywStr = ""; //第三方字段
		String err = "";
		for (MapAttr attr : attrs)
		{
			int val = this.GetRequestValInt("CB_" + attr.KeyOfEn);
			if (val == 0)
			{
				continue;
			}

			String refField = this.GetRequestVal("DDL_" + attr.KeyOfEn);

			//如果选中的业务字段重复，抛出异常
			if (ywStr.contains("@" + refField + "@"))
			{
				err += "@配置【" + attr.KeyOfEn + " - " + attr.Name +
					"】错误, 请确保选中业务字段的唯一性，该业务字段已经被其他字段所使用。";
			}
			lcStr += "@" + attr.KeyOfEn + "@,";
			ywStr += "@" + refField + "@,";
		}

		//    BP.Web.Controls.RadioBtn rb = this.Pub1.GetRadioBtnByID("rb_workId");

		int pkModel = this.GetRequestValInt("PKModel");

		String ddl_key = this.GetRequestVal("DDL_OID");
		if (pkModel == 0)
		{
			if (ywStr.contains("@" + ddl_key + "@"))
			{
				err += "@请确保选中业务字段的唯一性，该业务字段【" + ddl_key +
					"】已经被其他字段所使用。";
			}
			lcStr = "@OID@," + lcStr;
			ywStr = "@" + ddl_key + "@," + ywStr;
		}
		else
		{
			if (ywStr.contains("@" + ddl_key + "@"))
			{
				err += "@请确保选中业务字段的唯一性，该业务字段【" + ddl_key +
					"】已经被其他字段所使用。";
			}
			lcStr = "@GUID@," + lcStr;
			ywStr = "@" + ddl_key + "@," + ywStr;
		}

		if (!err.equals(""))
		{
			return "err@" + err;
		}

		lcStr = lcStr.replace("@", "");
		ywStr = ywStr.replace("@", "");


		//去除最后一个字符的操作
		if (DataType.IsNullOrEmpty(lcStr) || DataType.IsNullOrEmpty(ywStr))
		{
			return "err@要配置的内容为空...";
		}
		lcStr = lcStr.substring(0, lcStr.length() - 1);
		ywStr = ywStr.substring(0, ywStr.length() - 1);


		//数据存储格式   a,b,c@a_1,b_1,c_1
		fl.setDTSFields(lcStr + "@" + ywStr);
		fl.setDTSBTablePK(pk);
		fl.Update();

		return "设置成功.";
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion



//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 前置导航save
	/** 
	 前置导航save
	 
	 @return 
	*/
	public final String StartGuide_Save()
	{
		try
		{
			Flow en = new Flow();
			en.No = this.getFK_Flow();
			en.Retrieve();

			int val = this.GetRequestValInt("RB_StartGuideWay");

			en.SetValByKey(BP.WF.Template.FlowAttr.StartGuideWay, val);

			if (en.getStartGuideWay() == Template.StartGuideWay.None)
			{
				en.setStartGuideWay(BP.WF.Template.StartGuideWay.None);
			}

			if (en.getStartGuideWay() == Template.StartGuideWay.ByHistoryUrl)
			{
				en.setStartGuidePara1(this.GetRequestVal("TB_ByHistoryUrl"));
				en.setStartGuidePara2("");
				en.setStartGuideWay(BP.WF.Template.StartGuideWay.ByHistoryUrl);
			}

			if (en.getStartGuideWay() == Template.StartGuideWay.BySelfUrl)
			{
				en.setStartGuidePara1(this.GetRequestVal("TB_SelfURL"));
				en.setStartGuidePara2("");
				en.setStartGuideWay(BP.WF.Template.StartGuideWay.BySelfUrl);
			}

			//单条模式.
			if (en.getStartGuideWay() == Template.StartGuideWay.BySQLOne)
			{
				en.setStartGuidePara1(this.GetRequestVal("TB_BySQLOne1")); //查询语句.
				en.setStartGuidePara2(this.GetRequestVal("TB_BySQLOne2")); //列表语句.

				//@李国文.
				en.setStartGuidePara3(this.GetRequestVal("TB_BySQLOne3")); //单行赋值语句.
				en.setStartGuideWay(BP.WF.Template.StartGuideWay.BySQLOne);
			}
			//多条模式
			if (en.getStartGuideWay() == Template.StartGuideWay.BySQLMulti)
			{
				en.setStartGuidePara1(this.GetRequestVal("TB_BySQLMulti1")); //查询语句.
				en.setStartGuidePara2(this.GetRequestVal("TB_BySQLMulti2")); //列表语句.
				en.setStartGuideWay(BP.WF.Template.StartGuideWay.BySQLMulti);
			}
			//多条-子父流程-合卷审批.
			if (en.getStartGuideWay() == Template.StartGuideWay.SubFlowGuide)
			{
				en.setStartGuidePara1(this.GetRequestVal("TB_SubFlow1")); //查询语句.
				en.setStartGuidePara2(this.GetRequestVal("TB_SubFlow2")); //列表语句.
				en.setStartGuideWay(BP.WF.Template.StartGuideWay.SubFlowGuide);
			}



			BP.WF.Template.FrmNodes fns = new BP.WF.Template.FrmNodes(Integer.parseInt(this.getFK_Flow() + "01"));
			if (fns.size() >= 2)
			{
				if (en.getStartGuideWay() == Template.StartGuideWay.ByFrms)
				{
					en.setStartGuideWay(BP.WF.Template.StartGuideWay.ByFrms);
				}
			}

			//右侧的超链接.
			en.setStartGuideLink(this.GetRequestVal("TB_GuideLink"));
			en.setStartGuideLab(this.GetRequestVal("TB_GuideLab"));

			en.Update();
			return "保存成功";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion



//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 流程轨迹查看权限
	/** 
	 流程轨迹查看权限
	 
	 @return 
	*/
	public final String TruckViewPower_Save()
	{
		try
		{
			BP.WF.Template.TruckViewPower en = new BP.WF.Template.TruckViewPower(getFK_Flow());
			en.Retrieve();

			Object tempVar = BP.Sys.PubClass.CopyFromRequestByPost(en);
			en = tempVar instanceof BP.WF.Template.TruckViewPower ? (BP.WF.Template.TruckViewPower)tempVar : null;
			en.Save(); //执行保存.
			return "保存成功";
		}
		catch (java.lang.Exception e)
		{
			return "err@保存失败";
		}

	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 流程轨迹查看权限save

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 数据导入.
	/** 
	 流程模版导入.
	 
	 @return 
	*/
	public final String Imp_Done()
	{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
		var files = HttpContextHelper.RequestFiles(); //context.Request.Files;
		if (files.size() == 0)
		{
		return "err@请选择要上传的流程模版。";
		}

		//设置文件名
		String fileNewName = LocalDateTime.now().toString("yyyyMMddHHmmssff") + "_" + (new File(files[0].FileName)).getName();

		//文件存放路径
		String filePath = BP.Sys.SystemConfig.PathOfTemp + "\\" + fileNewName;
		//files[0].SaveAs(filePath);
		HttpContextHelper.UploadFile(files[0], filePath);

		String flowNo = this.getFK_Flow();
		String FK_FlowSort = this.GetRequestVal("FK_Sort");
		//检查流程编号
		if (DataType.IsNullOrEmpty(flowNo) == false)
		{
			Flow fl = new Flow(flowNo);
			FK_FlowSort = fl.getFK_FlowSort();
		}
		//检查流程类别编号
		if (DataType.IsNullOrEmpty(FK_FlowSort))
		{
			return "err@所选流程类别编号不存在。";
		}

		//导入模式
		BP.WF.ImpFlowTempleteModel model = BP.WF.ImpFlowTempleteModel.forValue(this.GetRequestValInt("ImpWay"));
		if (model == ImpFlowTempleteModel.AsSpecFlowNo)
		{
			flowNo = this.GetRequestVal("SpecFlowNo");
		}

		//执行导入
		BP.WF.Flow flow = BP.WF.Flow.DoLoadFlowTemplate(FK_FlowSort, filePath, model, flowNo);

		Hashtable ht = new Hashtable();
		ht.put("FK_Flow", flow.No);
		ht.put("FlowName", flow.Name);
		ht.put("FK_FlowSort", flow.getFK_FlowSort());
		ht.put("Msg", "导入成功,流程编号为:" + flow.No + "名称为:" + flow.Name);
		return BP.Tools.Json.ToJson(ht);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 数据导入.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 修改node Icon.
	/** 
	 修改节点ICON
	 
	 @return 
	*/
	public final String NodesIcon_Init()
	{
		DataSet ds = new System.Data.DataSet();
		Nodes nds = new Nodes(this.getFK_Flow());
		DataTable dt = nds.ToDataTableField("Nodes");
		ds.Tables.Add(dt);

		//把文件放入ds.
		String path = SystemConfig.PathOfWebApp + "\\WF\\Admin\\ClientBin\\NodeIcon\\";
		String[] strs = (new File(path)).list(File::isFile);
		DataTable dtIcon = new System.Data.DataTable();
		dtIcon.Columns.Add("No");
		dtIcon.Columns.Add("Name");
		for (String str : strs)
		{
			String fileName = str.substring(str.lastIndexOf("\\") + 1);
			fileName = fileName.substring(0, fileName.lastIndexOf("."));

			DataRow dr = dtIcon.NewRow();
			dr.set(0, fileName);
			dr.set(1, fileName);
			dtIcon.Rows.Add(dr);
		}

		dtIcon.TableName = "ICONs";
		ds.Tables.Add(dtIcon);

		return BP.Tools.Json.ToJson(ds);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 修改node Icon.

	/** 
	 流程时限消息设置
	 
	 @return 
	*/
	public final String PushMsgEntity_Init()
	{
		DataSet ds = new DataSet();

		//流程上的字段
		BP.Sys.MapAttrs attrs = new BP.Sys.MapAttrs();
		attrs.Retrieve(BP.Sys.MapAttrAttr.FK_MapData, "ND" + Integer.parseInt(this.getFK_Flow()) + "rpt", "LGType", 0, "MyDataType", 1);
		ds.Tables.Add(attrs.ToDataTableField("FrmFields"));

		//节点 
		BP.WF.Nodes nds = new BP.WF.Nodes(this.getFK_Flow());
		ds.Tables.Add(nds.ToDataTableField("Nodes"));

		//mypk
		BP.WF.Template.PushMsg msg = new BP.WF.Template.PushMsg();
		msg.MyPK = this.getMyPK();
		msg.RetrieveFromDBSources();
		ds.Tables.Add(msg.ToDataTableField("PushMsgEntity"));

		return BP.Tools.Json.DataSetToJson(ds, false);
	}

	/** 
	 流程时限消息设置
	 
	 @return 
	*/
	public final String PushMsg_Save()
	{
		BP.WF.Template.PushMsg msg = new BP.WF.Template.PushMsg();
		msg.MyPK = this.getMyPK();
		msg.RetrieveFromDBSources();

		msg.setFK_Event(this.getFK_Event()); //流程时限规则
		msg.setFK_Flow(this.getFK_Flow());

		BP.WF.Nodes nds = new BP.WF.Nodes(this.getFK_Flow());

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 求出来选择的节点.
		String nodesOfSMS = "";
		String nodesOfEmail = "";
		for (BP.WF.Node mynd : nds)
		{
			for (String key : HttpContextHelper.RequestParamKeys)
			{
				if (key.contains("CB_Station_" + mynd.getNodeID()) && nodesOfSMS.contains(mynd.getNodeID() + "") == false)
				{
					nodesOfSMS += mynd.getNodeID() + ",";
				}

				if (key.contains("CB_SMS_" + mynd.getNodeID()) && nodesOfSMS.contains(mynd.getNodeID() + "") == false)
				{
					nodesOfSMS += mynd.getNodeID() + ",";
				}

				if (key.contains("CB_Email_" + mynd.getNodeID()) && nodesOfEmail.contains(mynd.getNodeID() + "") == false)
				{
					nodesOfEmail += mynd.getNodeID() + ",";
				}
			}
		}

		//节点.
		msg.setMailNodes(nodesOfEmail);
		msg.setSMSNodes(nodesOfSMS);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 求出来选择的节点.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 短信保存.
		//短消息发送设备
		msg.setSMSPushModel(this.GetRequestVal("PushModel"));

		//短信推送方式。
		msg.setSMSPushWay((int)(HttpContextHelper.RequestParams("RB_SMS").Replace("RB_SMS_", "")));

		//短信手机字段.
		msg.setSMSField(HttpContextHelper.RequestParams("DDL_SMS_Fields"));
		//替换变量
		String smsstr = HttpContextHelper.RequestParams("TB_SMS");
		//扬玉慧 此处是配置界面  不应该把用户名和用户编号转化掉
		//smsstr = smsstr.Replace("@WebUser.Name", BP.Web.WebUser.Name);
		//smsstr = smsstr.Replace("@WebUser.No", BP.Web.WebUser.No);

		System.Data.DataTable dt = BP.WF.Dev2Interface.DB_GenerEmpWorksOfDataTable();
		// smsstr = smsstr.Replace("@RDT",);
		//短信内容模版.
		msg.setSMSDoc_Real(smsstr);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 短信保存.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 邮件保存.
		//邮件.
		//msg.MailPushWay = Convert.ToInt32(HttpContext.Current.Request["RB_Email"].ToString().Replace("RB_Email_", "")); ;
		//2019-07-25 zyt改造
		msg.setMailPushWay((int)(HttpContextHelper.RequestParams("RB_Email").Replace("RB_Email_", "")));
		//邮件标题与内容.
		msg.setMailTitle_Real(HttpContextHelper.RequestParams("TB_Email_Title"));
		msg.setMailDoc_Real(HttpContextHelper.RequestParams("TB_Email_Doc"));

		//邮件地址.
		msg.setMailAddress(HttpContextHelper.RequestParams("DDL_Email_Fields"));

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 邮件保存.

		//保存.
		if (DataType.IsNullOrEmpty(msg.MyPK) == true)
		{
			msg.MyPK = BP.DA.DBAccess.GenerGUID();
			msg.Insert();
		}
		else
		{
			msg.Update();
		}

		return "保存成功..";
	}
}