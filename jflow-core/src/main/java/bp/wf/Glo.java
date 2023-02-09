package bp.wf;

import bp.difference.handler.CommonUtils;
import bp.sys.*;
import bp.da.*;
import bp.en.*;
import bp.sys.frmui.ExtImg;
import bp.tools.DateUtils;
import bp.tools.StringHelper;
import bp.web.*;
import bp.port.*;
import bp.wf.data.*;
import bp.wf.data.GERpt;
import bp.wf.port.WFEmp;
import bp.wf.template.*;
import bp.tools.*;
import bp.difference.*;
import bp.wf.template.sflow.*;
import bp.wf.template.ccen.*;
import bp.wf.template.frm.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;
import java.math.*;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.core.io.ClassPathResource;

/** 
 全局(方法处理)
*/
public class Glo {

	///#region 新建节点-流程-默认值.

	//获得外部的参数.
	public static  Hashtable GenerBSParas()
	{

		Hashtable ht=new Hashtable();
		if (SystemConfig.getIsBSsystem() == true)
		{
			String expKeys = ",OID,DoType,HttpHandlerName,DoMethod,t,PWorkID,WorkID,";
			for (String k : CommonUtils.getRequest().getParameterMap().keySet())
			{
				if (k == null )	continue;
				if (expKeys.contains(','+k+',')) continue;

				if (ht.containsKey(k))
				{
					ht.put(k, bp.sys.Glo.getRequest().getParameter(k));
				}
				else
				{
					ht.put(k, bp.sys.Glo.getRequest().getParameter(k));
				}
			}
		}

		return ht;
	}

	/**
	 * 删除垃圾数据
	 */
	public static void ClearDustDB() throws Exception {

		String sqls = "UPDATE WF_Node SET IsCCFlow=0";
		sqls += "@UPDATE WF_Node  SET IsCCFlow=1 WHERE NodeID IN (SELECT NodeID FROM WF_Cond a WHERE a.NodeID= NodeID AND CondType=1 )";
		DBAccess.RunSQLs(sqls);

		// 删除垃圾数据. 
		DBAccess.RunSQL("DELETE FROM WF_NodeEmp WHERE FK_Emp  NOT IN (SELECT " + bp.sys.base.Glo.getUserNoWhitOutAS() + " FROM Port_Emp )");
		DBAccess.RunSQL("DELETE FROM WF_Emp WHERE NO NOT IN (SELECT No FROM Port_Emp )");

		DBAccess.RunSQL("UPDATE WF_Emp SET Name=(SELECT Name From Port_Emp WHERE Port_Emp.No=WF_Emp.No),FK_Dept=(select FK_Dept from Port_Emp where Port_Emp.No=WF_Emp.No)");


		// 更新是否是有完成条件的节点。
		DBAccess.RunSQL("DELETE FROM WF_Direction WHERE Node=0 OR ToNode=0");
		DBAccess.RunSQL("DELETE FROM WF_Direction WHERE Node  NOT IN (SELECT NODEID FROM WF_Node )");
		DBAccess.RunSQL("DELETE FROM WF_Direction WHERE ToNode  NOT IN (SELECT NODEID FROM WF_Node) ");
	}

	/**
	 * 签批组件SQL
	 */
	public static String getSQLOfBillNo() {
		String sql = "";
		switch (SystemConfig.getAppCenterDBType()) {
			case MSSQL:
			case MySQL:
				sql = "SELECT '' AS No, '-请选择-' as Name ";
				break;
			case Oracle:
			case KingBaseR3:
			case KingBaseR6:
				sql = "SELECT '' AS No, '-请选择-' as Name FROM DUAL ";
				break;
			case PostgreSQL:
			case UX:
			default:
				sql = "SELECT '' AS No, '-请选择-' as Name FROM Port_Emp WHERE 1=2 ";
				break;
		}
		sql += " union ";
		sql += " SELECT KeyOfEn AS No,Name From Sys_MapAttr WHERE UIContralType=0 AND UIVisible=1 AND UIIsEnable=1 AND FK_MapData='@FK_Frm'";
		return sql;
	}

	/**
	 * 签批组件SQL
	 */
	public static String getSQLOfCheckField() {
		String sql = "";
		switch (SystemConfig.getAppCenterDBType()) {
			case MSSQL:
			case MySQL:
				sql = "SELECT '' AS No, '-请选择-' as Name ";
				break;
			case Oracle:
			case KingBaseR3:
			case KingBaseR6:
				sql = "SELECT '' AS No, '-请选择-' as Name FROM DUAL ";
				break;
			case PostgreSQL:
			case UX:
			default:
				sql = "SELECT '' AS No, '-请选择-' as Name FROM Port_Emp WHERE 1=2 ";
				break;
		}
		sql += " union ";
		sql += " SELECT KeyOfEn AS No,Name From Sys_MapAttr WHERE UIContralType=14 AND FK_MapData='@FK_Frm'";
		return sql;
	}


	///#region 获取[新建-节点-流程]默认值.

	/**
	 * 新建节点的审核意见默认值.
	 */
	public static String getDefValWFNodeFWCDefInfo() throws Exception {
		return SystemConfig.GetValByKey("DefVal_WF_Node_FWCDefInfo", "同意");
	}

	///#endregion 获取[新建流程]默认值.


	///#region 高级配置.

	/**
	 * CCBPMRunModel
	 */
	public static CCBPMRunModel getCCBPMRunModel() {
		return SystemConfig.getCCBPMRunModel();
	}

	public static String GenerGanttDataOfSubFlows(long workID) throws Exception {
		GenerWorkFlow gwf = new GenerWorkFlow(workID);

		//增加子流程数据.
		GenerWorkFlows gwfs = new GenerWorkFlows();
		gwfs.Retrieve("PWorkID", workID, null);
		String json = "[";


		//主流程的计划完成日期，与实际完成日期的两个时间段.
		json += " { id:'001', name:'总体计划', type:0,";
		json += "series:[{ ";
		json += " name: '项目计划', ";
		json += " start:  " + ToData(gwf.getRDTOfSetting()) + ", ";
		json += " end: " + ToData(gwf.getSDTOfFlow()) + ",";
		json += " TodoSta: " + gwf.getTodoSta() + ", ";
		json += " color: 'blue' ";
		json += "},";

		json += "{ name: '实际执行', ";
		json += " start:  " + ToData(gwf.getRDT()) + ",";
		if (gwf.getWFState() == WFState.Complete) {
			json += " end: " + ToData(gwf.getSendDT()) + ",";
		} else {
			json += " end:' " + DataType.getCurrentDateByFormart("yyyy-MM-dd") + "',";
		}

		json += " TodoSta: " + gwf.getTodoSta() + ",";

		if (gwf.getWFSta() == WFSta.Complete) //流程运行结束
		{
           if (ToData(gwf.getSendDT()).compareTo(ToData(gwf.getSDTOfFlow())) <= 0) //正常
			{
				json += " color: 'green' ";
			} else {
				json += " color: 'red' ";
			}
		} else //未完成
		{
			String sendDt = DateUtils.format(DateUtils.parse(gwf.getSendDT(), "yyyy-MM-dd"));
			String wadingDtOfF = DateUtils.format(DataType.AddDays(sendDt, 3, TWay.Holiday), "yyyy-MM-dd");
        if (sendDt.compareTo(ToData(gwf.getSDTOfFlow())) > 0) //逾期
			{
				json += " color: 'red' ";
			} else {
            if (wadingDtOfF.compareTo(ToData(gwf.getSDTOfFlow())) > 0) {
					json += " color: 'yellow' ";
				} else {
					json += " color: 'green' ";
				}

			}

		}
		json += "}]";
		json += "},";


		//获得节点.
		Nodes nds = new Nodes(gwf.getFK_Flow());
		nds.Retrieve("FK_Flow", gwf.getFK_Flow(), "Step");

		//流转自定义
		String sql = "SELECT FK_Node AS NodeID,NodeName AS Name From WF_TransferCustom WHERE WorkID=" + gwf.getWorkID() + " AND IsEnable=1 Order By Idx";
		DataTable dtYL = DBAccess.RunSQLReturnTable(sql);
		boolean isFirstYLT = true;
		Nodes nodes = new Nodes();
		//含有流转自定义文件
		if (dtYL.Rows.size() != 0) {
			for (Node nd : nds.ToJavaList()) {
				if (nd.GetParaBoolen("IsYouLiTai") == true) {
					if (isFirstYLT == true) {
						for (DataRow dr : dtYL.Rows) {
							nodes.AddEntity(nds.GetEntityByKey(Integer.parseInt(dr.getValue("NodeID").toString())));
						}
					}

					isFirstYLT = false;
					continue;
				}
				nodes.AddEntity(nd);
			}
		} else {
			nodes.AddEntities(nds);
		}


		SubFlows subs = new SubFlows();
		String trackTable = "ND" + Integer.parseInt(gwf.getFK_Flow()) + "Track";
		sql = "SELECT FID \"FID\",NDFrom \"NDFrom\",NDFromT \"NDFromT\",NDTo  \"NDTo\", NDToT \"NDToT\", ActionType \"ActionType\",ActionTypeText \"ActionTypeText\",Msg \"Msg\",RDT \"RDT\",EmpFrom \"EmpFrom\",EmpFromT \"EmpFromT\", EmpToT \"EmpToT\",EmpTo \"EmpTo\" FROM " + trackTable + " WHERE WorkID=" + gwf.getWorkID() + " ORDER BY RDT ASC";

		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		int idxNode = 0;
		for (Node nd : nodes.ToJavaList()) {
			idxNode++;

			String[] dtArray = GetNodeRealTime(dt, nd.getNodeID());
			if (nd.isStartNode() == true) {
				continue;
			}

			//里程碑.
			json += " { id:'" + nd.getNodeID() + "', name:'" + nd.getName() + "', type:0,";
			json += "series:[{ ";
			json += " name: '计划', ";
			json += " start:  " + ToData(gwf.GetParaString("PlantStartDt" + nd.getNodeID())) + ", ";
			json += " end: " + ToData(gwf.GetParaString("CH" + nd.getNodeID())) + ",";
			json += " TodoSta: " + gwf.getTodoSta() + ", ";
			json += " color: 'blue' ";
			json += "},";

			json += "{ name: '实际执行', ";
			if (nd.isStartNode() == true) {
				json += " start:  " + ToData(gwf.getRDT()) + ",";
				json += " end: " + ToData(dtArray[1]) + ",";
				json += " TodoSta: " + gwf.getTodoSta() + ",";
				json += " color: 'green' ";
			} else {
				String start = "";
				String end = "";
				boolean isPass = false;
				json += " start:  " + ToData(dtArray[0]) + ",";


				if (DataType.IsNullOrEmpty(dtArray[1]) == true && DataType.IsNullOrEmpty(dtArray[0]) == true) {
					json += " end:'',";
				} else {
					if (DataType.IsNullOrEmpty(dtArray[1]) == false) {
						isPass = true;
					}
					json += " end: " + ToData(dtArray[1]) + ",";
					end = DateUtils.format(DateUtils.parse(dtArray[1], "yyyy-MM-dd"));
				}

				json += " TodoSta: " + gwf.getTodoSta() + ",";
				String plantCHDt = ToData(gwf.GetParaString("CH" + nd.getNodeID()));
             if (isPass && end.compareTo(plantCHDt) <= 0) {
					json += " color: 'green' "; //正常
				}

             if (isPass && end.compareTo(plantCHDt) > 0) {
					json += " color: 'red' "; //逾期
				}
				if (isPass == false) {
					if (DataType.IsNullOrEmpty(end) == true) {
						end = DataType.getCurrentDateByFormart("yyyy-MM-dd");
					}
					if (end.compareTo(plantCHDt) > 0) {
						json += " color: 'red' ";
					} else {
						// 预警计算
						//String warningDt = DataType.AddDays(end, 3, TWay.Holiday).toString("yyyy-MM-dd");
						String warningDt = DateUtils.format(DataType.AddDays(end, 3, TWay.Holiday), "yyyy-MM-dd");
						if (warningDt.compareTo(plantCHDt) >= 0) {
							json += " color: 'yellow' ";
						} else {
							json += " color: 'green' ";
						}
					}

				}


			}

			json += "}]},";

			//获取子流程
			subs = new SubFlows(nd.getNodeID());
			if (subs.size() == 0) {
				continue;
			}

			String series = "";
			for (SubFlow sub : subs.ToJavaList()) {
				if (sub.getFK_Node() != nd.getNodeID()) {
					continue;
				}
				json += " { id:'" + sub.getFK_Node() + "', name:'" + sub.getSubFlowNo() + " - " + sub.getSubFlowName() + "',type:1,series:[ ";
				//增加子流成.
				int idx = 0;
				String dtlsSubFlow = "";
				//获取流程启动的开始和结束
				long firstStartWorkID = 0;
				long endStartWorkID = 0;
				GenerWorkFlow firstStartGwf = null;
				GenerWorkFlow endStartGwf = null;
				boolean IsFirst = true;
				for (GenerWorkFlow subGWF : gwfs.ToJavaList()) {
					if (!subGWF.getFK_Flow().equals(sub.getSubFlowNo())) {
						continue;
					}
					if (IsFirst == true) {
						firstStartWorkID = subGWF.getWorkID();
						endStartWorkID = subGWF.getWorkID();
						firstStartGwf = subGWF;
						endStartGwf = subGWF;
						continue;

					}
					if (firstStartWorkID > subGWF.getWorkID()) {
						firstStartWorkID = subGWF.getWorkID();
						firstStartGwf = subGWF;
					}

					if (endStartWorkID < subGWF.getWorkID()) {
						endStartWorkID = subGWF.getWorkID();
						endStartGwf = subGWF;
					}

				}

				//没有启动子流程
				if (firstStartWorkID == 0) {
					json += "{ ";
					json += " name: '" + sub.getSubFlowNo() + " - " + sub.getSubFlowName() + "', ";
					json += " start:  " + ToData(DataType.getCurrentDate()) + ", ";
					json += " end:  " + ToData(DataType.getCurrentDate()) + ", ";
					json += " TodoSta: -1, ";
					json += " color: 'brue' ";
					json += "}";
				}
				//子流程被启动了一次
				if (firstStartWorkID != 0 && firstStartWorkID == endStartWorkID) {
					json += "{ ";
					json += " name: '计划',";
					json += " start:  " + ToData(firstStartGwf.getRDT()) + ",";
					json += " end: " + ToData(firstStartGwf.getSDTOfFlow()) + ",";
					json += " TodoSta: -2, ";
					json += " color: '#9999FF' ";
					json += "},";

					json += "{ ";
					json += " name: '实际',";
					json += " start:  " + ToData(firstStartGwf.getRDT()) + ",";
					if (firstStartGwf.getWFState() == WFState.Complete) {
						json += " end: " + ToData(firstStartGwf.getSendDT()) + ",";
					} else {
						json += " end: '" + DataType.getCurrentDateByFormart("yyyy-MM-dd") + "',";
					}

					json += " TodoSta: " + firstStartGwf.getTodoSta() + ", ";

					if (firstStartGwf.getWFSta() == WFSta.Complete) //流程运行结束
					{
						if (ToData(firstStartGwf.getSendDT()).compareTo(ToData(firstStartGwf.getSDTOfFlow())) <= 0) //正常
						{
							json += " color: '#66ff66' "; //绿色
						} else {
							json += " color: '#ff8888' "; //红色
						}
					} else //未完成
					{
						//String sendDt = DataType.ParseSysDate2DateTime(firstStartGwf.getSendDT()).toString("yyyy-MM-dd");
						//String wadingDtOfF = DateUtils.format(DataType.AddDays(sendDt, 3, TWay.Holiday),"yyyy-MM-dd");
						String sendDt = DateUtils.format(DataType.ParseSysDate2DateTime(firstStartGwf.getSendDT()), "yyyy-MM-dd");
						String wadingDtOfF = DateUtils.format(DataType.AddDays(sendDt, 3, TWay.Holiday), "yyyy-MM-dd");
						if (sendDt.compareTo(ToData(firstStartGwf.getSDTOfFlow())) > 0) //逾期
						{
							json += " color: '#ff8888' "; //红色
						} else {
							if (wadingDtOfF.compareTo(ToData(firstStartGwf.getSDTOfFlow())) > 0) {
								json += " color: '#ffff77' "; //黄色
							} else {
								json += " color: '#66ff66' "; //绿色
							}
						}


					}
					json += "}";
				}
				//子流程被启动了多次
				if (firstStartWorkID != 0 && firstStartWorkID != endStartWorkID) {
					json += "{ ";
					json += " name: '计划',";
					json += " start:  " + ToData(firstStartGwf.getRDT()) + ",";
					json += " end: " + ToData(firstStartGwf.getSDTOfFlow()) + ",";
					json += " TodoSta: -2, ";
					json += " color: '#9999FF' ";
					json += "},";

					json += "{ ";
					json += " name: '实际',";
					json += " start:  " + ToData(firstStartGwf.getRDT()) + ",";
					if (endStartGwf.getWFState() == WFState.Complete) {
						json += " end: " + ToData(endStartGwf.getSendDT()) + ",";
					} else {
						json += " end: '" + DataType.getCurrentDateByFormart("yyyy-MM-dd") + "',";
					}

					json += " TodoSta: " + endStartGwf.getTodoSta() + ", ";

					if (endStartGwf.getWFSta() == WFSta.Complete) //流程运行结束
					{
						if (ToData(endStartGwf.getSendDT()).compareTo(ToData(firstStartGwf.getSDTOfFlow())) <= 0) //正常
						{
							json += " color: '#66ff66' "; //绿色
						} else {
							json += " color: '#ff8888' "; //红色
						}
					} else //未完成
					{
						String sendDt = DateUtils.format(DataType.ParseSysDate2DateTime(firstStartGwf.getSendDT()), "yyyy-MM-dd");
						String wadingDtOfF = DateUtils.format(DataType.AddDays(sendDt, 3, TWay.Holiday), "yyyy-MM-dd");


						if (sendDt.compareTo(ToData(firstStartGwf.getSDTOfFlow())) > 0) //逾期
						{
							json += " color: '#ff8888' "; //红色
						} else {
							if (wadingDtOfF.compareTo(ToData(firstStartGwf.getSDTOfFlow())) > 0) {
								json += " color: '#ffff77' "; //黄色
							} else {
								json += " color: '#66ff66' "; //绿色
							}
						}


					}

					json += "}";
				}


				json += "]},";

				json += GetSubFlowJson(sub, workID);

			}

		}
		json = json.substring(0, json.length() - 1);
		json += "]";

		return json;
	}


	private static String GetSubFlowJson(SubFlow sub, long workID) throws Exception {
		//获取子流程的子流程
		SubFlows ssubFlows = new SubFlows();
		ssubFlows.Retrieve(SubFlowYanXuAttr.FK_Flow, sub.getSubFlowNo(), null);

		//获取流程的信息
		GenerWorkFlows sgwfs = new GenerWorkFlows();
		sgwfs.RetrieveInSQL(GenerWorkFlowAttr.PWorkID, "Select WorkID From WF_GenerWorkFlow Where PWorkID=" + workID + " And FK_Flow='" + sub.getSubFlowNo() + "'");

		//获取子流程
		if (ssubFlows.size() == 0) {
			return "";
		}
		StringBuilder json = new StringBuilder();
		String series = "";
		for (SubFlow ssub : ssubFlows.ToJavaList()) {
			if (!ssub.getFK_Flow().equals(sub.getSubFlowNo())) {
				continue;
			}
			json.append(" { id:'" + sub.getFK_Node() + "', name:'" + ssub.getSubFlowNo() + " - " + ssub.getSubFlowName() + "',type:2,series:[ ");
			//增加子流成.
			int idx = 0;
			String dtlsSubFlow = "";
			//获取流程启动的开始和结束
			long firstStartWorkID = 0;
			long endStartWorkID = 0;
			GenerWorkFlow firstStartGwf = null;
			GenerWorkFlow endStartGwf = null;
			boolean IsFirst = true;
			for (GenerWorkFlow subGWF : sgwfs.ToJavaList()) {
				if (!subGWF.getFK_Flow().equals(ssub.getSubFlowNo())) {
					continue;
				}
				if (IsFirst == true) {
					firstStartWorkID = subGWF.getWorkID();
					endStartWorkID = subGWF.getWorkID();
					firstStartGwf = subGWF;
					endStartGwf = subGWF;
					continue;

				}
				if (firstStartWorkID > subGWF.getWorkID()) {
					firstStartWorkID = subGWF.getWorkID();
					firstStartGwf = subGWF;
				}

				if (endStartWorkID < subGWF.getWorkID()) {
					endStartWorkID = subGWF.getWorkID();
					endStartGwf = subGWF;
				}

			}

			//没有启动子流程
			if (firstStartWorkID == 0) {
				json.append("{ ");
				json.append(" name: '" + ssub.getSubFlowNo() + " - " + ssub.getSubFlowName() + "', ");
				json.append(" start:  " + ToData(DataType.getCurrentDate()) + ", ");
				json.append(" end:  " + ToData(DataType.getCurrentDate()) + ", ");
				json.append(" TodoSta: -1, ");
				json.append(" color: 'brue' ");
				json.append("}");
			}
			//子流程被启动了一次
			if (firstStartWorkID != 0 && firstStartWorkID == endStartWorkID) {
				json.append("{ ");
				json.append(" name: '计划',");
				json.append(" start:  " + ToData(firstStartGwf.getRDT()) + ",");
				json.append(" end: " + ToData(firstStartGwf.getSDTOfFlow()) + ",");
				json.append(" TodoSta: -2, ");
				json.append(" color: '#9999FF' ");
				json.append("},");

				json.append("{ ");
				json.append(" name: '实际',");
				json.append(" start:  " + ToData(firstStartGwf.getRDT()) + ",");
				if (firstStartGwf.getWFState() == WFState.Complete) {
					json.append(" end: " + ToData(firstStartGwf.getSendDT()) + ",");
				} else {
					json.append(" end: '" + DataType.getCurrentDateByFormart("yyyy-MM-dd") + "',");
				}

				json.append(" TodoSta: " + firstStartGwf.getTodoSta() + ", ");

				if (firstStartGwf.getWFSta() == WFSta.Complete) //流程运行结束
				{
					if (ToData(firstStartGwf.getSendDT()).compareTo(ToData(firstStartGwf.getSDTOfFlow())) <= 0) //正常
					{
						json.append(" color: '#66ff66' "); //绿色
					} else {
						json.append(" color: '#ff8888' "); //红色
					}
				} else //未完成
				{
					String sendDt = DateUtils.format(DataType.ParseSysDate2DateTime(firstStartGwf.getSendDT()), "yyyy-MM-dd");
					String wadingDtOfF = DateUtils.format(DataType.AddDays(sendDt, 3, TWay.Holiday), "yyyy-MM-dd");
					if (sendDt.compareTo(ToData(firstStartGwf.getSDTOfFlow())) > 0) //逾期
					{
						json.append(" color: '#ff8888' "); //红色
					} else {
						if (wadingDtOfF.compareTo(ToData(firstStartGwf.getSDTOfFlow())) > 0) {
							json.append(" color: '#ffff77' "); //黄色
						} else {
							json.append(" color: '#66ff66' "); //绿色
						}
					}


				}
				json.append("}");
			}
			//子流程被启动了多次
			if (firstStartWorkID != 0 && firstStartWorkID != endStartWorkID) {
				json.append("{ ");
				json.append(" name: '计划',");
				json.append(" start:  " + ToData(firstStartGwf.getRDT()) + ",");
				json.append(" end: " + ToData(firstStartGwf.getSDTOfFlow()) + ",");
				json.append(" TodoSta: -2, ");
				json.append(" color: '#9999FF' ");
				json.append("},");

				json.append("{ ");
				json.append(" name: '实际',");
				json.append(" start:  " + ToData(firstStartGwf.getRDT()) + ",");
				if (endStartGwf.getWFState() == WFState.Complete) {
					json.append(" end: " + ToData(endStartGwf.getSendDT()) + ",");
				} else {
					json.append(" end: '" + DataType.getCurrentDateByFormart("yyyy-MM-dd") + "',");
				}

				json.append(" TodoSta: " + endStartGwf.getTodoSta() + ", ");

				if (endStartGwf.getWFSta() == WFSta.Complete) //流程运行结束
				{
					if (ToData(endStartGwf.getSendDT()).compareTo(ToData(firstStartGwf.getSDTOfFlow())) <= 0) //正常
					{
						json.append(" color: '#66ff66' "); //绿色
					} else {
						json.append(" color: '#ff8888' "); //红色
					}
				} else //未完成
				{
					String sendDt = DateUtils.format(DataType.ParseSysDate2DateTime(firstStartGwf.getSendDT()), "yyyy-MM-dd");
					String wadingDtOfF = DateUtils.format(DataType.AddDays(sendDt, 3, TWay.Holiday), "yyyy-MM-dd");
					if (sendDt.compareTo(ToData(firstStartGwf.getSDTOfFlow())) > 0) //逾期
					{
						json.append(" color: '#ff8888' "); //红色
					} else {
						if (wadingDtOfF.compareTo(ToData(firstStartGwf.getSDTOfFlow())) > 0) {
							json.append(" color: '#ffff77' "); //黄色
						} else {
							json.append(" color: '#66ff66' "); //绿色
						}
					}


				}

				json.append("}");
			}


			json.append("]},");


		}
		return json.toString();
	}

	public static String[] GetNodeRealTime(DataTable dt, int nodeID) {
		String startDt = "";
		String endDt = "";
		for (DataRow dr : dt.Rows) {
			int NDFrom = Integer.parseInt(dr.getValue("NDFrom").toString());
			if (NDFrom == nodeID) {
				endDt = dr.getValue("RDT").toString();
				break;
			}


		}
		for (DataRow dr : dt.Rows) {
			int NDTo = Integer.parseInt(dr.getValue("NDTo").toString());
			if (NDTo == nodeID) {
				if (DataType.IsNullOrEmpty(endDt) == true) {
					startDt = dr.getValue("RDT").toString();
					break;
				}
				if (dr.getValue("RDT").toString().compareTo(endDt) <= 0) {
					startDt = dr.getValue("RDT").toString();
					break;
				}
			}


		}
		String[] dtArray = {startDt, endDt};
		return dtArray;
	}

	/**
	 * 生成甘特图
	 * <p>
	 * param workID
	 *
	 * @return
	 */
	public static String GenerGanttDataOfSubFlowsV20(long workID) throws Exception {
		GenerWorkFlow gwf = new GenerWorkFlow(workID);
		//增加子流程数据.
		GenerWorkFlows gwfs = new GenerWorkFlows();
		gwfs.Retrieve("PWorkID", workID, null);

		String json = "[";

		json += " { id:'" + gwf.getFK_Flow() + "', name:'" + gwf.getFlowName() + "',";

		json += " series:[";
		json += "{ name: \"计划时间\", start:  " + ToData(gwf.getSDTOfFlow()) + ", end: " + ToData(gwf.getSDTOfFlow()) + ", color: \"#f0f0f0\" },";
		json += "{ name: \"实际工作时间\", start: " + ToData(gwf.getRDT()) + ", end: " + ToData(gwf.getSendDT()) + " , color: \"#f0f0f0\" }";
		json += "]";

		if (gwfs.size() == 0) {
			json += "}";
			json += "]";
			return json;
		} else {
			json += "},";
		}

		//增加子流成.
		int idx = 0;
		for (GenerWorkFlow subGWF : gwfs.ToJavaList()) {
			idx++;

			json += " { id:'" + subGWF.getFK_Flow() + "', name:'" + subGWF.getFlowName() + "',";

			json += " series:[";
			json += "{ name: \"实际工作时间\", start:  " + ToData(gwf.getRDT()) + ", end: " + ToData(gwf.getSendDT()) + " }";
			json += "]";

			if (idx == gwfs.size()) {
				json += "}";
				json += "]";
				return json;
			} else {
				json += "},";
			}
		}

		json += "]";

		return json;
	}

	public static String ToData(String dtStr) {

		Date dt = DataType.ParseSysDate2DateTime(dtStr);

		return "'" + DateUtils.format(dt, "yyyy-MM-dd") + "'";

	}

	/**
	 * 生成甘特图
	 *
	 * @return
	 */
	public static String GenerGanttDataOfSubFlowsV1(long workID) throws Exception {
		//定义解构.
		DataTable dtFlows = new DataTable();
		dtFlows.Columns.Add("id");
		dtFlows.Columns.Add("name");

		DataTable dtSeries = new DataTable();
		dtSeries.TableName = "series";
		dtSeries.Columns.Add("name");
		dtSeries.Columns.Add("start");
		dtSeries.Columns.Add("end");
		dtSeries.Columns.Add("color");
		dtSeries.Columns.Add("RefPK");

		//增加主流程数据.
		GenerWorkFlow gwf = new GenerWorkFlow(workID);
		DataRow dr = dtFlows.NewRow();
		dr.setValue("id", gwf.getFK_Flow());
		dr.setValue("name", gwf.getFlowName());
		dtFlows.Rows.add(dr);

		DataRow drItem = dtSeries.NewRow();
		drItem.setValue("name", "项目计划日期");
		drItem.setValue("start", gwf.getRDT());
		drItem.setValue("end", gwf.getSDTOfFlow());
		drItem.setValue("color", "#f0f0f0");
		drItem.setValue("RefPK", gwf.getFK_Flow());
		dtSeries.Rows.add(drItem);

		drItem = dtSeries.NewRow();
		drItem.setValue("name", "项目启动日期");
		drItem.setValue("start", gwf.getRDT());
		drItem.setValue("end", gwf.getSDTOfFlow());
		drItem.setValue("color", "#f0f0f0");
		drItem.setValue("RefPK", gwf.getFK_Flow());
		dtSeries.Rows.add(drItem);


		//增加子流程数据.
		GenerWorkFlows gwfs = new GenerWorkFlows();
		gwfs.Retrieve("PWorkID", workID, null);

		for (GenerWorkFlow subFlow : gwfs.ToJavaList()) {
			dr = dtFlows.NewRow();
			dr.setValue("id", subFlow.getFK_Flow());
			dr.setValue("name", subFlow.getFlowName());
			dtFlows.Rows.add(dr);


			drItem = dtSeries.NewRow();
			drItem.setValue("name", "启动日期");
			drItem.setValue("start", subFlow.getRDT());
			drItem.setValue("end", subFlow.getSDTOfFlow());
			drItem.setValue("color", "#f0f0f0");
			drItem.setValue("RefPK", subFlow.getFK_Flow());
			dtSeries.Rows.add(drItem);
		}

		DataSet ds = new DataSet();
		ds.Tables.add(dtFlows);
		ds.Tables.add(dtSeries);

		return ToJsonOfGantt(ds);
	}

	public static String ToJsonOfGantt(DataSet ds) {
		String json = "[";

		DataTable dtFlows = ds.Tables.get(0);
		DataTable dtSeries = ds.Tables.get(1);

		json += "]";

		return "";

	}

	///#endregion 高级配置.


	///#region 多语言处理.
	private static Hashtable _Multilingual_Cache = null;

	public static DataTable getMultilingual_DT(String className) {
		if (_Multilingual_Cache == null) {
			_Multilingual_Cache = new Hashtable();
		}

		if (_Multilingual_Cache.containsKey(className) == false) {
			DataSet ds = DataType.CXmlFileToDataSet(SystemConfig.getPathOfData() + "lang/xml/" + className + ".xml");
			DataTable dt = ds.Tables.get(0);

			_Multilingual_Cache.put(className, dt);
		}

		return _Multilingual_Cache.get(className) instanceof DataTable ? (DataTable) _Multilingual_Cache.get(className) : null;
	}

	/**
	 * 转换语言.
	 */

	public static String multilingual(String defaultMsg, String className, String key, String p0, String p1, String p2) {
		return multilingual(defaultMsg, className, key, p0, p1, p2, null);
	}

	public static String multilingual(String defaultMsg, String className, String key, String p0, String p1) {
		return multilingual(defaultMsg, className, key, p0, p1, null, null);
	}

	public static String multilingual(String defaultMsg, String className, String key, String p0) {
		return multilingual(defaultMsg, className, key, p0, null, null, null);
	}

	public static String multilingual(String defaultMsg, String className, String key) {
		return multilingual(defaultMsg, className, key, null, null, null, null);
	}

	//ORIGINAL LINE: public static string multilingual(string defaultMsg, string className, string key, string p0 = null, string p1 = null, string p2 = null, string p3 = null)
	public static String multilingual(String defaultMsg, String className, String key, String p0, String p1, String p2, String p3) {
		int num = 4;
		String[] paras = new String[num];
		if (p0 != null) {
			paras[0] = p0;
		}

		if (p1 != null) {
			paras[1] = p1;
		}

		if (p2 != null) {
			paras[2] = p2;
		}

		if (p3 != null) {
			paras[3] = p3;
		}

		return multilingual(defaultMsg, className, key, paras);
	}

	/**
	 * 获取多语言
	 * <p>
	 * param lang
	 * param key
	 * param paramList
	 *
	 * @return
	 */
	public static String multilingual(String defaultMsg, String className, String key, String[] paramList) {
		if (WebUser.getSysLang().equals("zh-cn") || WebUser.getSysLang().equals("CH"))
		{
			defaultMsg = defaultMsg.replace("{0}","%1$s").replace("{1}","%2$s").replace("{2}","%3$s").replace("{3}","%4$s");
			return String.format(defaultMsg, paramList);
		}



		DataTable dt = getMultilingual_DT(className);

		String val = "";
		for (DataRow dr : dt.Rows) {
			if (key.equals((String) dr.getValue(0))) {
				switch (WebUser.getSysLang()) {
					case "zh-cn":
						val = (String) dr.getValue(1);
						break;
					case "zh-tw":
						val = (String) dr.getValue(2);
						break;
					case "zh-hk":
						val = (String) dr.getValue(3);
						break;
					case "en-us":
						val = (String) dr.getValue(4);
						break;
					case "ja-jp":
						val = (String) dr.getValue(5);
						break;
					case "ko-kr":
						val = (String) dr.getValue(6);
						break;
					default:
						val = (String) dr.getValue(1);
						break;
				}
				break;
			}
		}
		return String.format(val, paramList);
	}


	//public static void Multilingual_Demo()
	//{
	//    //普通的多语言处理.
	//    string msg = "您确定要删除吗？";
	//    msg = BP.WF.Glo.Multilingual_Public(msg, "confirm");


	//    //带有参数的语言处理..
	//    msg = "您确定要删除吗？删除{0}后，就不能恢复。";
	//    msg = BP.WF.Glo.Multilingual_Public(msg, "confirmDel", "zhangsan");

	//    //   BP.WF.Glo.Multilingual_Public("confirm",
	//}


	///#endregion 多语言处理.


	///#region 公共属性.

	/**
	 * 打印文件
	 */
	public static String getPrintBackgroundWord() throws Exception {
		Object s = SystemConfig.getAppSettings().get("PrintBackgroundWord");
		if (DataType.IsNullOrEmpty(s)) {
			s = "高凌工作流引擎@开源高凌 - ccflow@openc";
		}
		return s.toString();
	}

	/**
	 * 运行平台.
	 */
	public static Platform getPlatform() throws Exception {
		return Platform.CCFlow;
	}

	/**
	 * 短消息写入类型
	 */
	public static ShortMessageWriteTo getShortMessageWriteTo() throws Exception {
		return ShortMessageWriteTo.forValue(SystemConfig.GetValByKeyInt("ShortMessageWriteTo", 0));
	}

	/**
	 * 当前选择的流程.
	 */
	public static String getCurrFlow() throws Exception {
		Object tempVar = bp.sys.Glo.getRequest().getSession().getAttribute("CurrFlow");
		return tempVar instanceof String ? (String) tempVar : null;
	}

	public static void setCurrFlow(String value) throws Exception {
		bp.sys.Glo.getRequest().getSession().setAttribute("CurrFlow", value);
	}

	/**
	 * 用户编号.
	 */
	public static String UserNo = null;
	/**
	 * 运行平台(用于处理不同的平台，调用不同的URL)
	 */
	public static Plant Plant = bp.wf.Plant.CCFlow;

	///#endregion 公共属性.

	/**
	 * 升级满足云需要.
	 */
	public final void UpdateCloud() throws Exception {
		if (1 == 1) {
			return;
		}


		///#region 为了适应云服务的要求.
		if (DBAccess.IsExitsTableCol(bp.sys.base.Glo.SysEnum(), "OrgNo") == false) {
			//检查数据表.
			SysEnum se = new SysEnum();
			se.CheckPhysicsTable();

			//更新值.
			DBAccess.RunSQL("UPDATE "+bp.sys.base.Glo.SysEnum()+" SET OrgNo='CCS'");

			//更新.
			DBAccess.RunSQL("UPDATE "+bp.sys.base.Glo.SysEnum()+" SET MyPK= EnumKey+'_'+Lang+'_'+IntKey+'_'+OrgNo");
		}
		if (DBAccess.IsExitsTableCol("Sys_EnumMain", "OrgNo") == false) {
			//检查数据表.
			SysEnumMain se = new SysEnumMain();
			se.CheckPhysicsTable();

			//更新值.
			DBAccess.RunSQL("UPDATE Sys_EnumMain SET OrgNo='CCS'");

			//更新.
			DBAccess.RunSQL("UPDATE Sys_EnumMain SET No= No+'_'+OrgNo ");
		}

		if (DBAccess.IsExitsTableCol("Sys_SFTable", "OrgNo") == false) {
			//检查数据表.
			SFTable se = new SFTable();
			se.CheckPhysicsTable();

			//更新值.
			DBAccess.RunSQL("UPDATE Sys_SFTable SET OrgNo='CCS',EnumKey=No");

			//更新.
			DBAccess.RunSQL("UPDATE Sys_SFTable SET No= No+'_'+OrgNo ");
		}


		///#endregion 为了适应云服务的要求.
	}

	/**
	 * 检查GPM菜单.
	 */
	public static void CheckGPM() throws Exception {
		ArrayList al = null;
		String info = "bp.en.Entity";
		al = ClassFactory.GetObjects(info);


		///#region 1, 修复表
		for (Object obj : al) {
			Entity en = null;
			en = obj instanceof Entity ? (Entity) obj : null;
			if (en == null) {
				continue;
			}

			if (en.toString() == null) {
				continue;
			}

			if (en.toString().contains("bp.port.")) {
				continue;
			}

			if (en.toString().contains("BP.GPM.") == false) {
				continue;
			}

			//if (en.ToString().contains("BP.GPM."))
			//    continue;
			//if (en.ToString().contains("BP.Demo."))
			//    continue;

			String table = null;
			try {
				table = en.getEnMap().getPhysicsTable();
				if (table == null) {
					continue;
				}
				if (en.getEnMap().getPhysicsTable().toLowerCase().contains("demo_") == true) {
					continue;
				}
			} catch (java.lang.Exception e) {
				continue;
			}

			switch (table) {
				case "WF_EmpWorks":
				case "WF_GenerEmpWorkDtls":
				case "WF_GenerEmpWorks":
				case "V_GPM_EmpMenu":
					continue;
				case "Sys_Enum":
				case "Sys_Enums":
					en.CheckPhysicsTable();
					break;
				default:
					en.CheckPhysicsTable();
					break;
			}

			en.setPKVal("123");
			try {
				en.RetrieveFromDBSources();
			} catch (RuntimeException ex) {
				Log.DebugWriteError(ex.getMessage());
				en.CheckPhysicsTable();
			}
		}

		///#endregion 修复

		//删除视图.
		if (DBAccess.IsExitsObject("V_GPM_EmpMenu") == true) {
			DBAccess.RunSQL("DROP VIEW V_GPM_EmpMenu");
		}

		if (DBAccess.IsExitsObject("V_GPM_EmpGroupMenu") == true) {
			DBAccess.RunSQL("DROP VIEW V_GPM_EmpGroupMenu");
		}

		if (DBAccess.IsExitsObject("V_GPM_EmpGroup") == true) {
			DBAccess.RunSQL("DROP VIEW V_GPM_EmpGroup");
		}

		if (DBAccess.IsExitsObject("V_GPM_EmpStationMenu") == true) {
			DBAccess.RunSQL("DROP VIEW V_GPM_EmpStationMenu");
		}


		///#region 6, 创建视图。
		String sqlscript = "";
		//MSSQL_GPM_VIEW 语法有所区别
		if (SystemConfig.getAppCenterDBType() == DBType.MSSQL) {
			sqlscript = SystemConfig.getPathOfWebApp() + "GPM//SQLScript/MSSQL_GPM_VIEW.sql";
		}

		//MySQL 语法有所区别
		if (SystemConfig.getAppCenterDBType() == DBType.MySQL) {
			sqlscript = SystemConfig.getPathOfWebApp() + "GPM/SQLScript/MySQL_GPM_VIEW.sql";
		}

		//Oracle 语法有所区别
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle
			|| SystemConfig.getAppCenterDBType() == DBType.KingBaseR3 || SystemConfig.getAppCenterDBType() == DBType.KingBaseR6) {
			sqlscript = SystemConfig.getPathOfWebApp() + "GPM/SQLScript/Oracle_GPM_VIEW.sql";
		}
		if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL || SystemConfig.getAppCenterDBType() == DBType.UX) {
			sqlscript = SystemConfig.getPathOfWebApp() + "GPM/SQLScript/PostgreSQL_GPM_VIEW.sql";
		}

		if (DataType.IsNullOrEmpty(sqlscript) == true) {
			throw new RuntimeException("err@没有判断的数据库类型:" + SystemConfig.getAppCenterDBType().toString());
		}

		DBAccess.RunSQLScriptGo(sqlscript);

		///#endregion 创建视图

	}

	/**
	 * 检查流程
	 *
	 * @return
	 */
	public static String CheckFlows() throws Exception {
		String err = "";
		String sql = "";
		sql += "SELECT a.FK_Attr, a.AttrKey, a.AttrName, a.FK_Flow, B.Name as FlowName, b.OrgNo as OrgNo";
		sql += " FROM wf_cond a,wf_flow b WHERE a.FK_Flow=b.no  ";
		sql += " AND  FK_Attr NOT IN (SELECT MYPK FROM sys_mapattr ) AND LENGTH(FK_Attr) >0";

		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() >= 1) {
			err += "err@在配置如下流程的条件的时候，字段被删除，会导致流程运行错误:" + Json.ToJson(dt);
		}

		return err;
	}

	/**
	 * 升级pop窗体格式.
	 * @return
	 * @throws Exception
	 */
	public static String UpdataTSModel() throws Exception {
            ////#region 节点属性pop窗体.
		if (DBAccess.IsExitsTableCol("WF_Node", "NodeStations") == true)
		{
			//删除可能存在的垃圾数据.
			DBAccess.RunSQL("DELETE  FROM WF_NodeStation WHERE FK_Node NOT IN (SELECT NodeID FROM WF_Node)");
			DBAccess.RunSQL("DELETE  FROM WF_NodeStation WHERE FK_Station NOT IN (SELECT No FROM Port_Station)");

			//求节点集合.
			DataTable dt = DBAccess.RunSQLReturnTable("SELECT DISTINCT FK_Node FROM WF_NodeStation");
			for (DataRow dr : dt.Rows)
			{
				String nodeID = dr.getValue(0).toString();
				String sql = "SELECT No,Name FROM Port_Station WHERE NO IN (SELECT FK_Station FROM WF_NodeStation WHERE FK_Node='" + nodeID + "')";
				DataTable dt1 = DBAccess.RunSQLReturnTable(sql);
				String ids = "";
				String names = "";
				for (DataRow item : dt1.Rows)
				{
					ids += item.getValue(0).toString() + ",";
					names += item.getValue(1).toString() + ",";
				}
				sql = "UPDATE WF_Node SET NodeStations='" + ids + "',NodeStationsT='" + names + "' WHERE NodeID=" + nodeID;
				DBAccess.RunSQL(sql);
			}
		}
		if (DBAccess.IsExitsTableCol("WF_Node", "NodeEmps") == true)
		{
			//删除可能存在的垃圾数据.
			DBAccess.RunSQL("DELETE  FROM WF_NodeEmp WHERE FK_Node NOT IN (SELECT NodeID FROM WF_Node)");
			DBAccess.RunSQL("DELETE  FROM WF_NodeEmp WHERE FK_Emp NOT IN (SELECT No FROM Port_Emp)");

			//求节点集合.
			DataTable dt = DBAccess.RunSQLReturnTable("SELECT DISTINCT FK_Node FROM WF_NodeEmp");
			for (DataRow dr : dt.Rows)
			{
				String nodeID = dr.getValue(0).toString();
				String sql = "SELECT No,Name FROM Port_Emp WHERE NO IN (SELECT FK_Emp FROM WF_NodeEmp WHERE FK_Node='" + nodeID + "')";
				DataTable dt1 = DBAccess.RunSQLReturnTable(sql);
				String ids = "";
				String names = "";
				for (DataRow item : dt1.Rows)
				{
					ids += item.getValue(0).toString() + ",";
					names += item.getValue(1).toString() + ",";
				}
				sql = "UPDATE WF_Node SET NodeEmps='" + ids + "',NodeEmpsT='" + names + "' WHERE NodeID=" + nodeID;
				DBAccess.RunSQL(sql);
			}
		}
		if (DBAccess.IsExitsTableCol("WF_Node", "NodeDepts") == true)
		{
			//删除可能存在的垃圾数据.
			DBAccess.RunSQL("DELETE  FROM WF_NodeDept WHERE FK_Node NOT IN (SELECT NodeID FROM WF_Node)");
			DBAccess.RunSQL("DELETE  FROM WF_NodeDept WHERE FK_Dept NOT IN (SELECT No FROM Port_Dept)");

			//求节点集合.
			DataTable dt = DBAccess.RunSQLReturnTable("SELECT DISTINCT FK_Node FROM WF_NodeDept");
			for (DataRow dr : dt.Rows)
			{
				String nodeID = dr.getValue(0).toString();
				String sql = "SELECT No,Name FROM Port_Dept WHERE NO IN (SELECT FK_Dept FROM WF_NodeDept WHERE FK_Node='" + nodeID + "')";
				DataTable dt1 = DBAccess.RunSQLReturnTable(sql);
				String ids = "";
				String names = "";
				for (DataRow item : dt1.Rows)
				{
					ids += item.getValue(0).toString() + ",";
					names += item.getValue(1).toString() + ",";
				}
				sql = "UPDATE WF_Node SET NodeDepts='" + ids + "',NodeDeptsT='" + names + "' WHERE NodeID=" + nodeID;
				DBAccess.RunSQL(sql);
			}
		}
		////#endregion 节点属性pop窗体.

		////#region 升级枚举编辑格式.. Sys_Enums
		if (DBAccess.IsExitsTableCol("Sys_EnumMain", "Val0") == false)
		{
			//补全SysEnumMain的字段（Idx0-29 , Val0-29）
			SysEnumMain ses = new SysEnumMain();
			ses.CheckPhysicsTable();

			//删除可能存在的垃圾数据.
			DBAccess.RunSQL("DELETE  FROM Sys_EnumMain WHERE No NOT IN (SELECT EnumKey FROM Sys_Enum)");

			//求EnumKey集合.
			SysEnumMains sysEnumMains = new SysEnumMains();
			sysEnumMains.RetrieveAll();

			for (SysEnumMain sem : sysEnumMains.ToJavaList())
			{
				String cfgVal = sem.getCfgVal();
				AtPara atPara = sem.getatPara();
				String enName = atPara.GetValStrByKey("EnName");
				//如果是Int型枚举就批量增加Idx0-29
				if (enName == "TS.Sys.SysEnumMainInt")
				{
					for (int i = 0; i < 30; i++)
					{
						sem.SetValByKey("Idx" + i, i);
					}
					//更新数据
					sem.DirectUpdate();
				}
				//判断格式是否有@
				if (cfgVal.indexOf("@") == -1)
				{
					throw new Exception("err@SysEnumMain的CfgVal格式不正确,请检查.");
				}

				String[] strs = cfgVal.split("@");
				int idx = -1;
				for (String str : strs)
				{
					if (DataType.IsNullOrEmpty(str))
						continue;

					//判断格式是否有=
					if (str.indexOf("=") == -1)
						throw new Exception("err@SysEnumMain的CfgVal格式不正确,请检查.");

					String[] strVal = str.split("=");
					idx++;
					sem.SetValByKey("Idx" + idx, strVal[0]);
					sem.SetValByKey("Val" + idx, strVal[1]);
				}
				//更新数据
				sem.DirectUpdate();
			}
		}
		////#endregion 升级枚举编辑格式.

		////#region 升级外键格式.. Sys_SFTablDtls 不升级也可以用的少.
		if (DBAccess.IsExitsTableCol("Sys_SFTabl", "Name0") == false)
		{
			//补全Sys_SFTabl的字段（BH0-49 , Name0-49）
			SFTable sft = new SFTable();
			sft.CheckPhysicsTable();

			//删除可能存在的垃圾数据.
			DBAccess.RunSQL("DELETE  FROM Sys_SFTable WHERE No NOT IN (SELECT FK_SFTable FROM Sys_SFTableDtl)");

			//求SFTableDtls集合.
			DataTable dt = DBAccess.RunSQLReturnTable("SELECT DISTINCT FK_SFTable FROM Sys_SFTableDtl");

			for (DataRow dr : dt.Rows)
			{
				String fk_SFTable = dr.getValue(0).toString();
				String sql = "SELECT BH,Name,Idx FROM Sys_SFTableDtl WHERE FK_SFTable='" + fk_SFTable + "'";
				DataTable dt1 = DBAccess.RunSQLReturnTable(sql);
				int i = -1;
				for (DataRow item : dt1.Rows)
				{
					i++;
					sql = "UPDATE Sys_SFTable SET BH" + i + "='" + item.getValue(0).toString() + "',Name" + i + "='" + item.getValue(1).toString() + "' WHERE No='" + fk_SFTable + "'";
					DBAccess.RunSQL(sql);
				}
			}
		}
		////#endregion 升级外键格式.

		return "升级成功.";
	}

	///#region 执行安装/升级.
	/**
	 * 当前版本号-为了升级使用.
	 * 20200602:升级方向条件.
	 */
	public static int Ver = 20230208;

	/**
	 * 执行升级
	 *
	 * @return
	 */
	public static String UpdataCCFlowVer() throws Exception {
		if (Glo.getCCBPMRunModel() == CCBPMRunModel.SAAS) {
			return "info@SAAS模式需要手工升级.";
		}

		//string checkErr = CheckFlows();
		//if (DataType.IsNullOrEmpty(checkErr) == false)
		//    return checkErr;


		///#region 检查是否需要升级，并更新升级的业务逻辑.
		String updataNote = "";
		/*
		 * 升级版本记录:
		 * 20150330: 优化发起列表的效率, by:zhoupeng.
		 * 2, 升级表单树,支持动态表单树.
		 * 1, 执行一次Sender发送人的升级，原来由GenerWorkerList 转入WF_GenerWorkFlow.
		 * 0, 静默升级启用日期.2014-12
		 */

		if (DBAccess.IsExitsObject("Sys_Serial") == false) {
			return "";
		}

		if (DBAccess.IsExitsTableCol("Sys_GroupField", "EnName") == true) {
			GroupField groupField = new GroupField();
			groupField.CheckPhysicsTable();
			DBAccess.RunSQL("UPDATE Sys_GroupField SET FrmID=enName WHERE FrmID IS null");
		}


		//升级.
		Auth ath = new Auth();
		ath.CheckPhysicsTable();

		//升级SQL
		UpdataCCFlowVerSQLScript();

		//判断数据库的版本.
		String sql = "SELECT IntVal FROM Sys_Serial WHERE CfgKey='Ver'";
		int currDBVer = DBAccess.RunSQLReturnValInt(sql, 0);
		if (currDBVer != 0 && currDBVer != 0 && currDBVer >= Ver) {
			return null; //不需要升级.
		}

		//#region 升级文本框字段类型.  TextModel=0普通文本，1密码，2=TextArea,3=富文本.
		//说明没有升级. TextModel=0
		if (DBAccess.IsExitsTableCol("Sys_MapAttr", "IsRichText") == true)
		{
			MapAttr ma = new MapAttr();
			ma.CheckPhysicsTable();

			sql = "UPDATE Sys_MapAttr SET TextModel=3 WHERE IsRichText=1 OR AtPara LIKE '%IsRichText=1%'";
			DBAccess.RunSQL(sql);

			sql = "UPDATE Sys_MapAttr SET TextModel=2 WHERE UIHeight >=40 OR IsSupperText=1";
			DBAccess.RunSQL(sql);

			sql = "UPDATE Sys_MapAttr SET TextModel=1 WHERE IsSecret=1";
			DBAccess.RunSQL(sql);

			DBAccess.DropTableColumn("Sys_MapAttr", "IsRichText");
			DBAccess.DropTableColumn("Sys_MapAttr", "IsSecret");
		}
        //#endregion 升级文本框字段类型

		//升级支持ts.
		UpdataTSModel();

		MapData mapData = new MapData();
		mapData.CheckPhysicsTable();


		///#region 升级优化集团版的应用. 2020.04.03

		//--2020.05.28 升级方向条件;
		Cond cond = new Cond();
		cond.CheckPhysicsTable();
		if (DBAccess.IsExitsTableCol("WF_Cond", "PRI") == true) {
			DBAccess.RunSQL("UPDATE WF_Cond SET Idx=PRI ");
			DBAccess.DropTableColumn("WF_Cond", "PRI");
		}


		//--2020.05.25 修改节点自定义按钮功能;
		NodeToolbar bar = new NodeToolbar();
		bar.CheckPhysicsTable();
		if (DBAccess.IsExitsTableCol("WF_NodeToolbar", "ShowWhere") == true) {
			DBAccess.RunSQL("UPDATE WF_NodeToolbar SET IsMyFlow = 1 Where ShowWhere = 1");
			DBAccess.RunSQL("UPDATE WF_NodeToolbar SET IsMyCC = 1 Where ShowWhere = 2");

			DBAccess.DropTableColumn("WF_NodeToolbar", "ShowWhere");
		}

		MapAttr myattr = new MapAttr();
		myattr.CheckPhysicsTable();

		MapDtlExt mde = new MapDtlExt();
		mde.CheckPhysicsTable();

		NodeExt ne = new NodeExt();
		ne.CheckPhysicsTable();

		FlowExt fe = new FlowExt();
		fe.CheckPhysicsTable();

		//检查frmTrack.
		bp.ccbill.Track tk = new bp.ccbill.Track();
		tk.CheckPhysicsTable();

		DeptEmpStation des = new DeptEmpStation();
		des.CheckPhysicsTable();

		DeptEmp de = new DeptEmp();
		de.CheckPhysicsTable();

		Emp emp1 = new Emp();
		emp1.CheckPhysicsTable();

		bp.wf.port.admin2group.Org org = new bp.wf.port.admin2group.Org();
		org.CheckPhysicsTable();

		FlowSort fs = new FlowSort();
		fs.CheckPhysicsTable();

		FlowOrg fo = new FlowOrg();
		fo.CheckPhysicsTable();

		SysEnumMain sem = new SysEnumMain();
		sem.CheckPhysicsTable();

		SysEnum myse = new SysEnum();
		myse.CheckPhysicsTable();

		//检查表.
		bp.wf.ReturnWork rw = new ReturnWork();
		rw.CheckPhysicsTable();

		//检查表.
		GloVar gv = new GloVar();
		gv.CheckPhysicsTable();

		//检查表.
		EnCfg cfg = new EnCfg();
		cfg.CheckPhysicsTable();

		//检查表.
		FrmTree frmTree = new FrmTree();
		frmTree.CheckPhysicsTable();

		SFTable sf = new SFTable();
		sf.CheckPhysicsTable();

		FrmSubFlow sb = new FrmSubFlow();
		sb.CheckPhysicsTable();

		PushMsg pm = new PushMsg();
		pm.CheckPhysicsTable();

		//修复数据表.
		GroupField gf = new GroupField();
		gf.CheckPhysicsTable();


		///#endregion 升级优化集团版的应用


		//检查子流程表.
		if (DBAccess.IsExitsObject("WF_NodeSubFlow") == true) {
			if (DBAccess.IsExitsTableCol("WF_NodeSubFlow", "OID") == true) {
				DBAccess.RunSQL("DROP TABLE WF_NodeSubFlow");
				SubFlowHand sub = new SubFlowHand();
				sub.CheckPhysicsTable();
			}
		}

		// 升级fromjson .//NOTE:此处有何用？而且md变量在下方已经声明，编译都通不过，2017-05-20，liuxc
		//MapData md = new MapData();
		//md.FormJson = "";

		///#endregion 检查是否需要升级，并更新升级的业务逻辑.


		///#region 升级方向条件. 2020.06.02
		if (DBAccess.IsExitsTableCol("WF_Cond", "CondOrAnd") == true) {
			DataTable dt = DBAccess.RunSQLReturnTable("SELECT DISTINCT FK_Node, toNodeID, CondOrAnd, CondType  FROM wf_cond WHERE DataFrom!=100 ");
			for (DataRow dr : dt.Rows) {
				int nodeID = Integer.parseInt(dr.getValue("FK_Node").toString());
				int toNodeID = Integer.parseInt(dr.getValue("toNodeID").toString());

				Conds conds = new Conds();
				conds.Retrieve(CondAttr.FK_Node, nodeID, CondAttr.ToNodeID, toNodeID, CondAttr.Idx);

				//判断是否需要修复？
				if (conds.size() == 1 || conds.size() == 0) {
					continue;
				}

				//判断是否有？
				boolean isHave = false;
				for (Cond myCond : conds.ToJavaList()) {
					if (myCond.getHisDataFrom() == ConnDataFrom.CondOperator) {
						isHave = true;
					}
				}
				if (isHave == true) {
					continue;
				}


				//获得类型.
				int OrAndType = DBAccess.RunSQLReturnValInt("SELECT  CondOrAnd  FROM wf_cond WHERE FK_Node=" + nodeID, -1);
				if (OrAndType == -1) {
					continue;
				}

				int idx = 0;
				int idxSave = 0;
				int count = conds.size();
				for (Cond item : conds.ToJavaList()) {
					idx++;

					idxSave++;
					item.setIdx(idxSave);
					item.Update();

					if (count == idx) {
						continue;
					}

					Cond operCond = new Cond();
					operCond.Copy(item);
					operCond.setMyPK(DBAccess.GenerGUID(0, null, null));
					operCond.setHisDataFrom(ConnDataFrom.CondOperator);

					if (OrAndType == 0) {
						operCond.setOperatorValue(" OR ");
						operCond.setNote(" OR ");
						operCond.setOperatorValue(" OR ");
						operCond.setNote(" OR ");
					} else {
						operCond.setOperatorValue(" AND ");
						operCond.setNote(" AND ");
						operCond.setOperatorValue(" AND ");
						operCond.setNote(" AND ");
					}

					idxSave++;
					operCond.setIdx(idxSave);
					operCond.Insert();
				}
			}

			//升级后删除指定的列.
			DBAccess.DropTableColumn("WF_Cond", "CondOrAnd");
			DBAccess.DropTableColumn("WF_Cond", "NodeID");
		}

		///#endregion 升级方向条件.


		///#region 枚举值
		SysEnumMains enumMains = new SysEnumMains();
		enumMains.RetrieveAll();
		for (SysEnumMain enumMain : enumMains.ToJavaList()) {
			SysEnums ens = new SysEnums();
			ens.Delete(SysEnumAttr.EnumKey, enumMain.getNo());

			//保存数据
			String cfgVal = enumMain.getCfgVal();
			AtPara atPara = enumMain.getatPara();
			String enName = atPara.GetValStrByKey("EnName");
			String[] strs = cfgVal.split("[@]", -1);
			for (String s : strs) {
				if (DataType.IsNullOrEmpty(s) == true) {
					continue;
				}

				String[] vk = s.split("[=]", -1);
				SysEnum se = new SysEnum();
				se.setEnumKey(enumMain.getNo());
				se.setLang(WebUser.getSysLang());
				//根据EnName判断是Int枚举还是String枚举
				if (enName.equals("TS.Sys.SysEnumMainInt"))
				{
					se.setIntKey(Integer.parseInt(vk[0]));
					if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
						se.setMyPK(se.getEnumKey() + "_" + se.getLang() + "_" + se.getIntKey() + "_" + WebUser.getOrgNo()); //关联的主键.

					if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.SAAS)
						se.setMyPK(se.getEnumKey() + "_" + se.getLang() + "_" + se.getIntKey());
				}

				if (enName.equals("TS.Sys.SysEnumMainString"))
				{
					se.setStrKey(vk[0].toString());
					if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
						se.setMyPK(se.getEnumKey() + "_" + se.getLang() + "_" + se.getStrKey() + "_" + WebUser.getOrgNo()); //关联的主键.

					if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.SAAS)
						se.setMyPK(se.getEnumKey() + "_" + se.getLang() + "_" + se.getStrKey());
				}
				String[] kvsValues = new String[vk.length - 1];
				for (int i = 0; i < kvsValues.length; i++) {
					kvsValues[i] = vk[i + 1];
				}
				se.setLab(StringHelper.join("=", kvsValues));
				//如果存在就更新
				if (se.IsExit("MyPK", se.getMyPK()))
					se.DirectUpdate();
				else
					se.DirectInsert();
			}
		}

		///#endregion


		///#region 升级视图. 解决厦门信息港的 - 流程监控与授权.
		if (DBAccess.IsExitsObject("V_MyFlowData") == false) {
			PowerModel pm11 = new PowerModel();
			pm11.CheckPhysicsTable();

			sql = "CREATE VIEW V_MyFlowData ";
			sql += " AS ";
			sql += " SELECT A.*, B.EmpNo as MyEmpNo FROM WF_GenerWorkflow A, WF_PowerModel B ";
			sql += " WHERE  A.FK_Flow=B.FlowNo AND B.PowerCtrlType=1 AND A.WFState>= 2";
			sql += "    UNION  ";
			sql += " SELECT A.*, c.No as MyEmpNo FROM WF_GenerWorkflow A, WF_PowerModel B, Port_Emp C, Port_DeptEmpStation D";
			sql += " WHERE  A.FK_Flow=B.FlowNo  AND B.PowerCtrlType=0 AND C.No=D.FK_Emp AND B.StaNo=D.FK_Station AND A.WFState>=2";
			DBAccess.RunSQL(sql);
		}

		if (DBAccess.IsExitsObject("V_WF_AuthTodolist") == false) {
			bp.wf.Auth en = new Auth();
			en.CheckPhysicsTable();

			sql = "CREATE VIEW V_WF_AuthTodolist ";
			sql += " AS ";
			sql += " SELECT B.FK_Emp Auther,B.FK_EmpText AuthName,A.PWorkID,A.FK_Node,A.FID,A.WorkID,C.AutherToEmpNo,  C.TakeBackDT, A.FK_Flow, A.FlowName,A.Title ";
			sql += " FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B, WF_Auth C";
			sql += " WHERE A.WorkID=B.WorkID AND C.AuthType=1 AND B.FK_Emp=C.Auther AND B.IsPass=0 AND B.IsEnable=1 AND A.FK_Node = B.FK_Node AND A.WFState >=2";
			sql += "    UNION  ";
			sql += " SELECT B.FK_Emp Auther,B.FK_EmpText AuthName,A.PWorkID,A.FK_Node,A.FID,A.WorkID, C.AutherToEmpNo, C.TakeBackDT, A.FK_Flow, A.FlowName,A.Title";
			sql += " FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B, WF_Auth C";
			sql += " WHERE A.WorkID=B.WorkID AND C.AuthType=2 AND B.FK_Emp=C.Auther AND B.IsPass=0 AND B.IsEnable=1 AND A.FK_Node = B.FK_Node AND A.WFState >=2 AND A.FK_Flow=C.FlowNo";
			DBAccess.RunSQL(sql);
		}

		///#endregion 升级视图.

		//升级从表的 fk_node .
		//获取需要修改的从表
		String dtlNos = DBAccess.RunSQLReturnString("SELECT B.NO  FROM SYS_GROUPFIELD A, SYS_MAPDTL B WHERE A.CTRLTYPE='Dtl' AND A.CTRLID=B.NO AND FK_NODE!=0");
		if (DataType.IsNullOrEmpty(dtlNos) == false) {
			dtlNos = dtlNos.replace(",", "','");
			dtlNos = "('" + dtlNos + "')";
			DBAccess.RunSQL("UPDATE SYS_MAPDTL SET FK_NODE=0 WHERE NO IN " + dtlNos);
		}
		FrmNode nff = new FrmNode();
		nff.CheckPhysicsTable();


		///#region 升级审核组件
		if (SystemConfig.getAppCenterDBType() == DBType.MySQL) {
			sql = "UPDATE WF_FrmNode F INNER JOIN(SELECT FWCSta,NodeID FROM WF_Node ) N on F.FK_Node = N.NodeID and  F.IsEnableFWC =1 SET F.IsEnableFWC = N.FWCSta;";
		}
		if (SystemConfig.getAppCenterDBType() == DBType.MSSQL) {
			sql = "UPDATE    F SET IsEnableFWC = N. FWCSta  FROM WF_FrmNode F,WF_Node N    WHERE F.FK_Node = N.NodeID AND F.IsEnableFWC =1";
		}
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle ||SystemConfig.getAppCenterDBType() == DBType.KingBaseR3 ||SystemConfig.getAppCenterDBType() == DBType.KingBaseR6) {
			sql = "UPDATE WF_FrmNode F  SET (IsEnableFWC)=(SELECT FWCSta FROM WF_Node N WHERE F.FK_Node = N.NodeID AND F.IsEnableFWC =1)";
		}
		if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL || SystemConfig.getAppCenterDBType() == DBType.UX) {
			sql = "UPDATE WF_FrmNode SET IsEnableFWC=(SELECT FWCSta FROM WF_Node N Where N.NodeID=WF_FrmNode.FK_Node AND WF_FrmNode.IsEnableFWC=1)";
		}

		DBAccess.RunSQL(sql);

		///#endregion 升级审核组件


		///#region 升级填充数据.
		//pop自动填充
		MapExts exts = new MapExts();
		QueryObject qo = new QueryObject(exts);
		qo.AddWhere(MapExtAttr.ExtType, " LIKE ", "Pop%");
		qo.DoQuery();
		for (MapExt ext : exts.ToJavaList()) {
			String mypk = ext.getFK_MapData() + "_" + ext.getAttrOfOper();
			MapAttr ma = new MapAttr();
			ma.setMyPK(mypk);
			if (ma.RetrieveFromDBSources() == 0) {
				ext.Delete();
				continue;
			}

			if (ext.getExtType().equals(ma.GetParaString("PopModel"))) {
				continue; //已经修复了，或者配置了.
			}

			ma.SetPara("PopModel", ext.getExtType());
			ma.Update();

			if (DataType.IsNullOrEmpty(ext.getTag4()) == true) {
				continue;
			}

			MapExt extP = new MapExt();
			extP.setMyPK(ext.getMyPK() + "_FullData");
			int i = extP.RetrieveFromDBSources();
			if (i == 1) {
				continue;
			}

			extP.setExtType("FullData");
			extP.setFK_MapData(ext.getFK_MapData());
			extP.setAttrOfOper(ext.getAttrOfOper());
			extP.setDBType(ext.getDBType());
			extP.setDoc(ext.getTag4());
			extP.Insert(); // 执行插入.
		}


		//文本自动填充
		exts = new MapExts();
		exts.Retrieve(MapExtAttr.ExtType, MapExtXmlList.TBFullCtrl, null);
		for (MapExt ext : exts.ToJavaList()) {
			String mypk = ext.getFK_MapData() + "_" + ext.getAttrOfOper();
			MapAttr ma = new MapAttr();
			ma.setMyPK(mypk);
			if (ma.RetrieveFromDBSources() == 0) {
				ext.Delete();
				continue;
			}
			String modal = ma.GetParaString("TBFullCtrl");
			if (DataType.IsNullOrEmpty(modal) == false) {
				continue; //已经修复了，或者配置了.
			}

			if (DataType.IsNullOrEmpty(ext.getTag3()) == false) {
				ma.SetPara("TBFullCtrl", "Simple");
			} else {
				ma.SetPara("TBFullCtrl", "Table");
			}

			ma.Update();

			if (DataType.IsNullOrEmpty(ext.getTag4()) == true) {
				continue;
			}

			MapExt extP = new MapExt();
			extP.setMyPK(ext.getMyPK() + "_FullData");
			int i = extP.RetrieveFromDBSources();
			if (i == 1) {
				continue;
			}

			extP.setExtType("FullData");
			extP.setFK_MapData(ext.getFK_MapData());
			extP.setAttrOfOper(ext.getAttrOfOper());
			extP.setDBType(ext.getDBType());
			extP.setDoc(ext.getTag4());

			// 填充从表
			extP.setTag1(ext.getTag1());
			// 填充下拉框
			extP.setTag(ext.getTag());
			extP.Insert(); // 执行插入.
		}

		//下拉框填充其他控件
		//文本自动填充
		exts = new MapExts();
		exts.Retrieve(MapExtAttr.ExtType, MapExtXmlList.DDLFullCtrl, null);
		for (MapExt ext : exts.ToJavaList()) {
			String mypk = ext.getFK_MapData() + "_" + ext.getAttrOfOper();
			MapAttr ma = new MapAttr();
			ma.setMyPK(mypk);
			if (ma.RetrieveFromDBSources() == 0) {
				ext.Delete();
				continue;
			}
			String modal = ma.GetParaString("IsFullData");
			if (DataType.IsNullOrEmpty(modal) == false) {
				continue; //已经修复了，或者配置了.
			}

			//启用填充其他控件
			ma.SetPara("IsFullData", 1);
			ma.Update();


			MapExt extP = new MapExt();
			extP.setMyPK(ext.getMyPK() + "_FullData");
			int i = extP.RetrieveFromDBSources();
			if (i == 1) {
				continue;
			}

			extP.setExtType("FullData");
			extP.setFK_MapData(ext.getFK_MapData());
			extP.setAttrOfOper(ext.getAttrOfOper());
			extP.setDBType(ext.getDBType());
			extP.setDoc(ext.getDoc());

			// 填充从表
			extP.setTag1(ext.getTag1());
			// 填充下拉框
			extP.setTag(ext.getTag());

			extP.Insert(); //执行插入.

		}

		//装载填充
		exts = new MapExts();
		exts.Retrieve(MapExtAttr.ExtType, MapExtXmlList.PageLoadFull, null);
		for (MapExt ext : exts.ToJavaList()) {
			mapData.setNo(ext.getFK_MapData());
			if (mapData.RetrieveFromDBSources() == 0) {
				ext.Delete();
				continue;
			}
			if (DataType.IsNullOrEmpty(mapData.GetParaString("IsPageLoadFull")) == false) {
				continue;
			}
			mapData.setPageLoadFull(true);
			mapData.Update();

			//修改填充数据的值
			ext.setDoc(ext.getTag());

			String tag1 = ext.getTag1();
			if (DataType.IsNullOrEmpty(tag1) == true) {
				ext.Update();
				continue;
			}
			MapDtls dtls = mapData.getMapDtls();
			for (MapDtl dtl : dtls.ToJavaList()) {
				tag1 = tag1.replace("*" + dtl.getNo() + "=", "$" + dtl.getNo() + ":");
			}
			ext.setTag1(tag1);
			ext.Update();
		}

		///#endregion 升级 填充数据.

		String msg = "";
		try {


			///#region 升级事件.
			if (DBAccess.IsExitsTableCol("Sys_FrmEvent", "DoType") == true) {
				FrmEvent frmevent = new FrmEvent();
				frmevent.CheckPhysicsTable();

				DBAccess.RunSQL("UPDATE Sys_FrmEvent SET EventDoType=DoType  ");
				DBAccess.RunSQL("ALTER TABLE Sys_FrmEvent  DROP COLUMN	DoType  ");
			}

			///#endregion


			///#region 修复丢失的发起人.
			Flows fls = new Flows();
			fls.getGetNewEntity().CheckPhysicsTable();

			for (Flow item : fls.ToJavaList()) {
				if (DBAccess.IsExitsObject(item.getPTable()) == false) {
					continue;
				}
				try {
					DBAccess.RunSQL(" UPDATE " + item.getPTable() + " SET FlowStarter =(SELECT Starter FROM WF_GENERWORKFLOW WHERE " + item.getPTable() + ".Oid=WF_GENERWORKFLOW.WORKID)");
				} catch (RuntimeException ex) {
					//   GERpt rpt=new GERpt(
				}
			}

			///#endregion 修复丢失的发起人.


			///#region 给city 设置拼音.
			if (DBAccess.IsExitsObject("CN_City") == true && 1 == 2) {
				if (DBAccess.IsExitsTableCol("CN_City", "PinYin") == true) {
					/*为cn_city 设置拼音.*/
					sql = "SELECT No,Names FROM CN_City ";
					DataTable dtCity = DBAccess.RunSQLReturnTable(sql);

					for (DataRow dr : dtCity.Rows) {
						String no = dr.getValue("No").toString();
						String name = dr.getValue("Names").toString();
						String pinyin1 = DataType.ParseStringToPinyin(name);
						String pinyin2 = DataType.ParseStringToPinyinJianXie(name);
						String pinyin = "," + pinyin1 + "," + pinyin2 + ",";
						DBAccess.RunSQL("UPDATE CN_City SET PinYin='" + pinyin + "' WHERE NO='" + no + "'");
					}
				}
			}

			///#endregion 给city 设置拼音.

			//增加列FlowStars
			bp.wf.port.WFEmp wfemp = new bp.wf.port.WFEmp();
			wfemp.CheckPhysicsTable();


			///#region 更新wf_emp. 的字段类型. 2019.06.19
			DBType dbtype = SystemConfig.getAppCenterDBType();

			//if (DBAccess.IsExitsTableCol("WF_Emp", "StartFlows") == true)
			//    DBAccess.RunSQL("ALTER TABLE WF_Emp DROP Column StartFlows");

			//if (dbtype == DBType.Oracle)
			//    DBAccess.RunSQL("ALTER TABLE WF_Emp Add StartFlows BLOB");

			//if (dbtype == DBType.MySQL)
			//    DBAccess.RunSQL("ALTER TABLE WF_Emp modify StartFlows longtext ");

			//if (dbtype == DBType.MSSQL)
			//    DBAccess.RunSQL(" ALTER TABLE WF_Emp ALTER column  StartFlows nvarchar(4000) null");


			///#endregion 更新wf_emp 的字段类型.

			FrmRB rb = new FrmRB();
			rb.CheckPhysicsTable();

			CC ccEn = new CC();
			ccEn.CheckPhysicsTable();

			WFEmp ccWFEmp = new WFEmp();
			ccWFEmp.CheckPhysicsTable();

			MapDtlExt dtlExt = new MapDtlExt();
			dtlExt.CheckPhysicsTable();

			//删除枚举.
			DBAccess.RunSQL("DELETE FROM "+bp.sys.base.Glo.SysEnum()+" WHERE EnumKey IN ('SelectorModel','CtrlWayAth')");

			//SysEnum se = new SysEnum("FrmType", 1);//NOTE:此处升级时报错，2017-06-13，liuxc

			//2017.5.19 打印模板字段修复
			FrmPrintTemplate bt = new FrmPrintTemplate();
			bt.CheckPhysicsTable();
			if (DBAccess.IsExitsTableCol("Sys_FrmPrintTemplate", "url") == true) {
				DBAccess.RunSQL("UPDATE Sys_FrmPrintTemplate SET TempFilePath = Url WHERE TempFilePath IS null");
			}

			//规范升级根目录.
			DataTable dttree = DBAccess.RunSQLReturnTable("SELECT No FROM Sys_FormTree WHERE ParentNo='-1' ");
			if (dttree.Rows.size() == 1) {
				DBAccess.RunSQL("UPDATE Sys_FormTree SET ParentNo='1' WHERE ParentNo='0' ");
				DBAccess.RunSQL("UPDATE Sys_FormTree SET No='1' WHERE No='0'  ");
				DBAccess.RunSQL("UPDATE Sys_FormTree SET ParentNo='0' WHERE No='1'");
			}


			//删除垃圾数据.
			MapExt.DeleteDB();

			//升级傻瓜表单.
			MapFrmFool mff = new MapFrmFool();
			mff.CheckPhysicsTable();


			///#region 表单方案中的不可编辑, 旧版本如果包含了这个列.
			if (DBAccess.IsExitsTableCol("WF_FrmNode", "IsEdit") == true) {
				/*如果存在这个列,就查询出来=0的设置，就让其设置为不可以编辑的。*/
				sql = "UPDATE WF_FrmNode SET FrmSln=1 WHERE IsEdit=0 ";
				DBAccess.RunSQL(sql);

				sql = "UPDATE WF_FrmNode SET IsEdit=100000";
				DBAccess.RunSQL(sql);
			}

			///#endregion

			//执行升级 2016.04.08 
			Cond cnd = new Cond();
			cnd.CheckPhysicsTable();


			///#region  增加week字段,方便按周统计.
			bp.wf.GenerWorkFlow gwf = new GenerWorkFlow();
			gwf.CheckPhysicsTable();
			sql = "SELECT WorkID,RDT FROM WF_GenerWorkFlow WHERE WeekNum=0 or WeekNum is null ";
			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			for (DataRow dr : dt.Rows) {
				sql = "UPDATE WF_GenerWorkFlow SET WeekNum="
						+ DataType.WeekOfYear(DataType.ParseSysDateTime2DateTime(dr.getValue(1).toString()))
						+ " WHERE WorkID=" + dr.getValue(0).toString();
				DBAccess.RunSQL(sql);
			}

			//查询.
			CH ch = new CH();
			ch.CheckPhysicsTable();

			sql = "SELECT MyPK,DTFrom FROM WF_CH WHERE WeekNum=0 or WeekNum is null ";
			dt = DBAccess.RunSQLReturnTable(sql);
			for (DataRow dr : dt.Rows) {
				sql = "UPDATE WF_CH SET WeekNum="
						+ DataType.WeekOfYear(DataType.ParseSysDateTime2DateTime(dr.getValue(1).toString()))
						+ " WHERE MyPK='" + dr.getValue(0).toString() + "'";
			}

			///#endregion  增加week字段.


			///#region 检查数据源.
			SFDBSrc src = new SFDBSrc();
			src.setNo("local");
			src.setName("本机数据源(默认)");
			if (src.RetrieveFromDBSources() == 0) {
				src.Insert();
			}

			///#endregion 检查数据源.


			///#region 20170613.增加审核组件配置项"是否显示退回的审核信息”对应字段 by:liuxianchen
			try {
				if (DBAccess.IsExitsTableCol("WF_Node", "FWCIsShowReturnMsg") == false) {
					switch (src.getHisDBType()) {
						case MSSQL:
							DBAccess.RunSQL("ALTER TABLE WF_Node ADD FWCIsShowReturnMsg INT NULL");
							break;
						case Oracle:
						case Informix:
						case PostgreSQL:
						case UX:
						case KingBaseR3:
						case KingBaseR6:
							DBAccess.RunSQL("ALTER TABLE WF_Node ADD FWCIsShowReturnMsg INTEGER NULL");
							break;
						case MySQL:
							DBAccess.RunSQL("ALTER TABLE WF_Node ADD FWCIsShowReturnMsg INT NULL");
							break;
						default:
							break;
					}

					DBAccess.RunSQL("UPDATE WF_Node SET FWCIsShowReturnMsg = 0");
				}
			} catch (java.lang.Exception e) {
			}

			///#endregion


			///#region 20170522.增加SL表单设计器中对单选/复选按钮进行字体大小调节的功能 by:liuxianchen

			FrmRB frmRB = new FrmRB();
			frmRB.CheckPhysicsTable();

			try {
				DataTable columns = src.GetColumns("Sys_FrmRB");
				if (columns.Select("No='AtPara'").length == 0) {
					switch (src.getHisDBType()) {
						case MSSQL:
							DBAccess.RunSQL("ALTER TABLE Sys_FrmRB ADD AtPara NVARCHAR(1000) NULL");
							break;
						case Oracle:
						case KingBaseR3:
						case KingBaseR6:
							DBAccess.RunSQL("ALTER TABLE Sys_FrmRB ADD AtPara NVARCHAR2(1000) NULL");
							break;
						case PostgreSQL:
						case UX:
							DBAccess.RunSQL("ALTER TABLE Sys_FrmRB ADD AtPara VARCHAR2(1000) NULL");
							break;
						case MySQL:
						case Informix:
							DBAccess.RunSQL("ALTER TABLE Sys_FrmRB ADD AtPara TEXT NULL");
							break;
						default:
							break;
					}
				}
			} catch (java.lang.Exception e2) {
			}

			///#endregion


			///#region 其他.
			// 更新 PassRate.
			sql = "UPDATE WF_Node SET PassRate=100 WHERE PassRate IS NULL";
			DBAccess.RunSQL(sql);

			///#endregion 其他.


			///#region 升级统一规则.

			///#region 检查必要的升级。
			NodeWorkCheck fwc = new NodeWorkCheck();
			fwc.CheckPhysicsTable();

			Flow myfl = new Flow();
			myfl.CheckPhysicsTable();

			Node nd = new Node();
			nd.CheckPhysicsTable();

			//Sys_SFDBSrc
			SFDBSrc sfDBSrc = new SFDBSrc();
			sfDBSrc.CheckPhysicsTable();

			///#endregion 检查必要的升级。
			MapExt mapExt = new MapExt();
			mapExt.CheckPhysicsTable();

			try {
				String sqls = "";

				if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL || SystemConfig.getAppCenterDBType() == DBType.UX || SystemConfig.getAppCenterDBType() == DBType.KingBaseR3 ||SystemConfig.getAppCenterDBType() == DBType.KingBaseR6) {
					sqls += "UPDATE Sys_MapExt SET MyPK= ExtType||'_'||FK_Mapdata||'_'||AttrOfOper WHERE ExtType='" + MapExtXmlList.TBFullCtrl + "'";
					sqls += "@UPDATE Sys_MapExt SET MyPK= ExtType||'_'||FK_Mapdata||'_'||AttrOfOper WHERE ExtType='" + MapExtXmlList.PopVal + "'";
					sqls += "@UPDATE Sys_MapExt SET MyPK= ExtType||'_'||FK_Mapdata||'_'||AttrOfOper WHERE ExtType='" + MapExtXmlList.DDLFullCtrl + "'";
					sqls += "@UPDATE Sys_MapExt SET MyPK= ExtType||'_'||FK_Mapdata||'_'||AttrsOfActive WHERE ExtType='" + MapExtXmlList.ActiveDDL + "'";
				} else if (SystemConfig.getAppCenterDBType() == DBType.MySQL) {
					sqls += "UPDATE Sys_MapExt SET MyPK=CONCAT(ExtType,'_',FK_Mapdata,'_',AttrOfOper) WHERE ExtType='" + MapExtXmlList.TBFullCtrl + "'";
					sqls += "@UPDATE Sys_MapExt SET MyPK=CONCAT(ExtType,'_',FK_Mapdata,'_',AttrOfOper) WHERE ExtType='" + MapExtXmlList.PopVal + "'";
					sqls += "@UPDATE Sys_MapExt SET MyPK=CONCAT(ExtType,'_',FK_Mapdata,'_',AttrOfOper) WHERE ExtType='" + MapExtXmlList.DDLFullCtrl + "'";
					sqls += "@UPDATE Sys_MapExt SET MyPK=CONCAT(ExtType,'_',FK_Mapdata,'_',AttrOfOper) WHERE ExtType='" + MapExtXmlList.ActiveDDL + "'";
				} else {
					sqls += "UPDATE Sys_MapExt SET MyPK= ExtType+'_'+FK_Mapdata+'_'+AttrOfOper WHERE ExtType='" + MapExtXmlList.TBFullCtrl + "'";
					sqls += "@UPDATE Sys_MapExt SET MyPK= ExtType+'_'+FK_Mapdata+'_'+AttrOfOper WHERE ExtType='" + MapExtXmlList.PopVal + "'";
					sqls += "@UPDATE Sys_MapExt SET MyPK= ExtType+'_'+FK_Mapdata+'_'+AttrOfOper WHERE ExtType='" + MapExtXmlList.DDLFullCtrl + "'";
					sqls += "@UPDATE Sys_MapExt SET MyPK= ExtType+'_'+FK_Mapdata+'_'+AttrsOfActive WHERE ExtType='" + MapExtXmlList.ActiveDDL + "'";
				}

				DBAccess.RunSQLs(sqls);
			} catch (RuntimeException ex) {
				Log.DebugWriteError(ex.getMessage());
			}

			///#endregion

			///#region  更新CA签名(2015-03-03)。
			//BP.Tools.WFSealData sealData = new Tools.WFSealData();
			//sealData.CheckPhysicsTable();
			//sealData.UpdateColumn();
			///#endregion  更新CA签名.

			///#region 其他.
			//升级表单树. 2015.10.05
			SysFormTree sft = new SysFormTree();
			sft.CheckPhysicsTable();


			//表单信息表.
			MapDataExt mapext = new MapDataExt();
			mapext.CheckPhysicsTable();

			TransferCustom tc = new TransferCustom();
			tc.CheckPhysicsTable();

			//增加部门字段。
			CCList cc = new CCList();
			cc.CheckPhysicsTable();

			///#endregion 其他.


			///#region 升级sys_sftable
			//升级
			SFTable tab = new SFTable();
			tab.CheckPhysicsTable();

			///#endregion


			///#region 基础数据更新.
			Node wf_Node = new Node();
			wf_Node.CheckPhysicsTable();
			// 设置节点ICON.
			sql = "UPDATE WF_Node SET ICON='审核.png' WHERE ICON IS NULL";
			DBAccess.RunSQL(sql);

			NodeSheet nodeSheet = new NodeSheet();
			nodeSheet.CheckPhysicsTable();


			///#endregion 基础数据更新.


			///#region 把节点的toolbarExcel, word 信息放入mapdata
			//BP.WF.Template.NodeSheets nss = new NodeSheets();
			//nss.RetrieveAll();
			//foreach (BP.WF.Template.NodeSheet ns in nss)
			//{
			//    ToolbarExcel te = new ToolbarExcel();
			//    te.No = "ND" + ns.NodeID;
			//    te.RetrieveFromDBSources();
			//}

			///#endregion


			///#region 升级SelectAccper
			Direction dir = new Direction();
			dir.CheckPhysicsTable();

			SelectAccper selectAccper = new SelectAccper();
			selectAccper.CheckPhysicsTable();

			///#endregion


			///#region  升级 NodeToolbar
			FrmField ff = new FrmField();
			ff.CheckPhysicsTable();


			SysFormTree ssft = new SysFormTree();
			ssft.CheckPhysicsTable();

			FrmAttachment myath = new FrmAttachment();
			myath.CheckPhysicsTable();

			FrmField ffs = new FrmField();
			ffs.CheckPhysicsTable();

			///#endregion


			///#region 执行sql．
			DBAccess.RunSQL("delete  from "+bp.sys.base.Glo.SysEnum()+" WHERE EnumKey in ('PrintFileType','EventDoType','FormType','BatchRole','StartGuideWay','NodeFormType')");
			DBAccess.RunSQL("UPDATE Sys_FrmSln SET FK_Flow =(SELECT FK_FLOW FROM WF_Node WHERE NODEID=Sys_FrmSln.FK_Node) WHERE FK_Flow IS NULL");

			if (SystemConfig.getAppCenterDBType() == DBType.MSSQL) {
				DBAccess.RunSQL("UPDATE WF_FrmNode SET MyPK=FK_Frm+'_'+convert(varchar,FK_Node )+'_'+FK_Flow");
			}

			if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL || SystemConfig.getAppCenterDBType() == DBType.UX || SystemConfig.getAppCenterDBType() == DBType.KingBaseR3 ||SystemConfig.getAppCenterDBType() == DBType.KingBaseR6) {
				DBAccess.RunSQL("UPDATE WF_FrmNode SET MyPK=FK_Frm||'_'||FK_Node||'_'||FK_Flow");
			}

			if (SystemConfig.getAppCenterDBType() == DBType.MySQL) {
				DBAccess.RunSQL("UPDATE WF_FrmNode SET MyPK=CONCAT(FK_Frm,'_',FK_Node,'_',FK_Flow)");
			}


			///#endregion


			///#region 执行更新.wf_node
			sql = "UPDATE WF_Node SET FWCType=0 WHERE FWCType IS NULL";
			sql += "@UPDATE WF_Node SET FWC_H=0 WHERE FWC_H IS NULL";
			DBAccess.RunSQLs(sql);


			sql = "UPDATE WF_Node SET SFSta=0 WHERE SFSta IS NULL";
			sql += "@UPDATE WF_Node SET SF_H=0 WHERE SF_H IS NULL";
			DBAccess.RunSQLs(sql);

			///#endregion 执行更新.


			///#region 升级站内消息表 2013-10-20
			bp.wf.SMS sms = new SMS();
			sms.CheckPhysicsTable();

			///#endregion


			///#region 重新生成view WF_EmpWorks,  2013-08-06.
			if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single) {
				if (DBAccess.IsExitsObject("WF_EmpWorks") == true) {
					DBAccess.RunSQL("DROP VIEW WF_EmpWorks");
				}

				if (DBAccess.IsExitsObject("V_FlowStarterBPM") == true) {
					DBAccess.RunSQL("DROP VIEW V_FlowStarterBPM");
				}

				if (DBAccess.IsExitsObject("V_TOTALCH") == true) {
					DBAccess.RunSQL("DROP VIEW V_TOTALCH");
				}

				if (DBAccess.IsExitsObject("V_TOTALCHYF") == true) {
					DBAccess.RunSQL("DROP VIEW V_TOTALCHYF");
				}

				if (DBAccess.IsExitsObject("V_TotalCHWeek") == true) {
					DBAccess.RunSQL("DROP VIEW V_TotalCHWeek");
				}

				if (DBAccess.IsExitsObject("V_WF_Delay") == true) {
					DBAccess.RunSQL("DROP VIEW V_WF_Delay");
				}

				String sqlscript = "";
				//执行必须的sql.
				switch (SystemConfig.getAppCenterDBType()) {
					case Oracle:
					case KingBaseR3:
					case KingBaseR6:
						sqlscript = SystemConfig.getPathOfData() + "Install/SQLScript/InitView_Ora.sql";
						break;
					case MSSQL:
					case Informix:
						sqlscript = SystemConfig.getPathOfData() + "Install/SQLScript/InitView_SQL.sql";
						break;
					case MySQL:
						sqlscript = SystemConfig.getPathOfData() + "Install/SQLScript/InitView_MySQL.sql";
						break;
					case PostgreSQL:
					case UX:
						sqlscript = SystemConfig.getPathOfData() + "Install/SQLScript/InitView_PostgreSQL.sql";
						break;
					default:
						break;
				}

				DBAccess.RunSQLScript(sqlscript);
			}


			///#endregion


			///#region 升级Img
			FrmImg img = new FrmImg();
			img.CheckPhysicsTable();

			ExtImg myimg = new ExtImg();
			myimg.CheckPhysicsTable();
			if (DBAccess.IsExitsTableCol("Sys_FrmImg", "SrcType") == true) {
				DBAccess.RunSQL("UPDATE Sys_FrmImg SET ImgSrcType = SrcType WHERE ImgSrcType IS NULL");
				DBAccess.RunSQL("UPDATE Sys_FrmImg SET ImgSrcType = 0 WHERE ImgSrcType IS NULL");
			}

			///#endregion


			///#region 修复 mapattr UIHeight, UIWidth 类型错误.
			switch (SystemConfig.getAppCenterDBType()) {
				case Oracle:
					msg = "@Sys_MapAttr 修改字段";
					break;
				case MSSQL:
					msg = "@修改sql server控件高度和宽度字段。";
					DBAccess.RunSQL("ALTER TABLE Sys_MapAttr ALTER COLUMN UIWidth float");
					DBAccess.RunSQL("ALTER TABLE Sys_MapAttr ALTER COLUMN UIHeight float");
					break;
				default:
					break;
			}

			///#endregion


			///#region 升级常用词汇
			switch (SystemConfig.getAppCenterDBType()) {
				case Oracle:
					int i = DBAccess.RunSQLReturnCOUNT("SELECT * FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'SYS_DEFVAL' AND COLUMN_NAME = 'PARENTNO'");
					if (i == 0) {
						if (DBAccess.IsExitsObject("SYS_DEFVAL") == true) {
							DBAccess.RunSQL("drop table Sys_DefVal");
						}

						DefVal dv = new DefVal();
						dv.CheckPhysicsTable();
					}
					msg = "@Sys_DefVal 修改字段";
					break;
				case MSSQL:
					msg = "@修改 Sys_DefVal。";
					i = DBAccess.RunSQLReturnCOUNT("SELECT * FROM SYSCOLUMNS WHERE ID=OBJECT_ID('Sys_DefVal') AND NAME='ParentNo'");
					if (i == 0) {
						if (DBAccess.IsExitsObject("Sys_DefVal") == true) {
							DBAccess.RunSQL("drop table Sys_DefVal");
						}

						DefVal dv = new DefVal();
						dv.CheckPhysicsTable();
					}
					break;
				default:
					break;
			}

			///#endregion


			///#region 登陆更新错误
			msg = "@登陆时错误。";
			DBAccess.RunSQL("DELETE FROM "+bp.sys.base.Glo.SysEnum()+" WHERE EnumKey IN ('DeliveryWay','RunModel','OutTimeDeal','FlowAppType')");

			///#endregion 登陆更新错误


			///#region 升级表单树
			// 首先检查是否升级过.
			sql = "SELECT * FROM Sys_FormTree WHERE No = '1'";
			DataTable formTree_dt = DBAccess.RunSQLReturnTable(sql);
			if (formTree_dt.Rows.size() == 0) {
				/*没有升级过.增加根节点*/
				SysFormTree formTree = new SysFormTree();
				formTree.setNo("1");
				formTree.setName("表单库");
				formTree.setParentNo("0");
				// formTree.TreeNo = "0";
				formTree.setIdx(0);
				formTree.setDir(true);

				try {
					formTree.DirectInsert();
				} catch (java.lang.Exception e3) {
				}
				//将表单库中的数据转入表单树
				SysFormTrees formSorts = new SysFormTrees();
				formSorts.RetrieveAll();

				for (SysFormTree item : formSorts.ToJavaList()) {
					if (item.getNo().equals("0")) {
						continue;
					}
					SysFormTree subFormTree = new SysFormTree();
					subFormTree.setNo(item.getNo());
					subFormTree.setName(item.getName());
					subFormTree.setParentNo("0");
					subFormTree.Save();
				}
				//表单于表单树进行关联
				sql = "UPDATE Sys_MapData SET FK_FormTree=FK_FrmSort WHERE FK_FrmSort <> '' AND FK_FrmSort IS not null";
				DBAccess.RunSQL(sql);
			}

			///#endregion


			///#region 执行admin登陆. 2012-12-25 新版本.
			Emp emp = new Emp("admin");
			if (emp.RetrieveFromDBSources() == 1) {
				WebUser.SignInOfGener(emp, "CH", false, false, null, null);
			} else {
				emp.SetValByKey("No", "admin");
				emp.setName("admin");
				emp.setFK_Dept("01");
				emp.setIsPass("123");
				emp.Insert();
				WebUser.SignInOfGener(emp, "CH", false, false, null, null);
				//throw new Exception("admin 用户丢失，请注意大小写。");
			}

			///#endregion 执行admin登陆.


			///#region 修复 Sys_FrmImg 表字段 ImgAppType Tag0
			switch (SystemConfig.getAppCenterDBType()) {
				case Oracle:
					int i = DBAccess.RunSQLReturnCOUNT("SELECT * FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'SYS_FRMIMG' AND COLUMN_NAME = 'TAG0'");
					if (i == 0) {
						DBAccess.RunSQL("ALTER TABLE SYS_FRMIMG ADD (ImgAppType number,TAG0 nvarchar(500))");
					}
					msg = "@Sys_FrmImg 修改字段";
					break;
				case MSSQL:
					msg = "@修改 Sys_FrmImg。";
					i = DBAccess.RunSQLReturnCOUNT("SELECT * FROM SYSCOLUMNS WHERE ID=OBJECT_ID('Sys_FrmImg') AND NAME='Tag0'");
					if (i == 0) {
						DBAccess.RunSQL("ALTER TABLE Sys_FrmImg ADD ImgAppType int");
						DBAccess.RunSQL("ALTER TABLE Sys_FrmImg ADD Tag0 nvarchar(500)");
					}
					break;
				default:
					break;
			}

			///#endregion


			///#region 20161101.升级表单，增加图片附件必填验证 by:liuxianchen

			FrmImgAth imgAth = new FrmImgAth();
			imgAth.CheckPhysicsTable();

			sql = "UPDATE Sys_FrmImgAth SET IsRequired = 0 WHERE IsRequired IS NULL";
			DBAccess.RunSQL(sql);

			///#endregion


			///#region 20170520.附件删除规则修复
			try {
				DataTable columns = src.GetColumns("Sys_FrmAttachment");
				if (columns.Select("No='DeleteWay'").length > 0 && columns.Select("No='IsDelete'").length > 0) {
					DBAccess.RunSQL("UPDATE SYS_FRMATTACHMENT SET DeleteWay=IsDelete WHERE DeleteWay IS NULL");
				}
			} catch (java.lang.Exception e4) {
			}

			///#endregion


			///#region 密码加密
			if (SystemConfig.getIsEnablePasswordEncryption() == true && DBAccess.IsView("Port_Emp", SystemConfig.getAppCenterDBType()) == false) {
				Emps emps = new Emps();
				emps.RetrieveAllFromDBSource();
				for (Emp empEn : emps.ToJavaList()) {
					if (DataType.IsNullOrEmpty(empEn.getPass()) || empEn.getPass().length() < 30) {
						empEn.setIsPass(Cryptos.aesEncrypt(empEn.getPass()));
						empEn.DirectUpdate();
					}
				}
			}

			///#endregion

			// 最后更新版本号，然后返回.
			sql = "UPDATE Sys_Serial SET IntVal=" + Ver + " WHERE CfgKey='Ver'";
			if (DBAccess.RunSQL(sql) == 0) {
				sql = "INSERT INTO Sys_Serial (CfgKey,IntVal) VALUES('Ver'," + Ver + ") ";
				DBAccess.RunSQL(sql);
			}
			// 返回版本号.
			return "旧版本:(" + currDBVer + ")新版本:(" + Ver + ")"; // +"\t\n解决问题:" + updataNote;
		} catch (RuntimeException ex) {
			String err = "问题出处:" + ex.getMessage() + "<hr>" + msg + "<br>详细信息:@" + ex.getStackTrace() + "<br>@<a href='../DBInstall.htm' >点这里到系统升级界面。</a>";
			Log.DebugWriteError("系统升级错误:" + err);
			return err;
		}
	}

	/**
	 * 如果发现升级sql文件日期变化了，就自动升级.
	 * 就是说该文件如果被修改了就会自动升级.
	 */
	public static void UpdataCCFlowVerSQLScript()
	{

		String sql = "SELECT IntVal FROM Sys_Serial WHERE CfgKey='UpdataCCFlowVer'";
		String currDBVer = DBAccess.RunSQLReturnStringIsNull(sql, "");

		String sqlScript = SystemConfig.getPathOfData() + "UpdataCCFlowVer.sql";
		if(SystemConfig.getAppCenterDBType() == DBType.KingBaseR3
				|| SystemConfig.getAppCenterDBType() == DBType.KingBaseR6)
			sqlScript = SystemConfig.getPathOfData() + "UpdataCCFlowVerForKingBase.sql";
		SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
		Calendar cal = Calendar.getInstance();
		if(SystemConfig.getIsJarRun()){
			ClassPathResource classPathResource = new ClassPathResource(sqlScript);
			try {
				cal.setTimeInMillis(classPathResource.lastModified());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			File fi = new File(sqlScript);
			if(fi.exists()==false)
				return;
			cal.setTimeInMillis(fi.lastModified());
		}
		String myVer = sdf.format(cal.getTime());

		//判断是否可以执行，当文件发生变化后，才执行。
		if (currDBVer.equals("") || Integer.parseInt(currDBVer) < Integer.parseInt(myVer))
		{
			bp.da.DBAccess.RunSQLScript(sqlScript);
			sql = "UPDATE Sys_Serial SET IntVal=" + myVer + " WHERE CfgKey='UpdataCCFlowVer'";

			if (DBAccess.RunSQL(sql) == 0)
			{
				sql = "INSERT INTO Sys_Serial (CfgKey,IntVal) VALUES('UpdataCCFlowVer'," + myVer + ") ";
				DBAccess.RunSQL(sql);
			}
		}
	}

	/**
	 * CCFlowAppPath
	 */
	public static String getCCFlowAppPath() {
		return SystemConfig.GetValByKey("CCFlowAppPath", "/");
	}

	/**
	 *
	 */
	public static boolean isEnableHuiQianList() throws Exception {
		if (SystemConfig.getCustomerNo().equals("TianYe")) {
			return true;
		}

		return SystemConfig.GetValByKeyBoolen("IsEnableHuiQianList", false);
	}

	/**
	 * 检查是否可以安装高凌BPM系统
	 *
	 * @return
	 */
	public static boolean IsCanInstall() throws Exception {

		if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single) {
			throw new RuntimeException("err@初次安装必须设置 CCBPMRunModel=0 为单机版，安装后在修改集团或者saas版.");
		}

		String sql = "";
		String errInfo = "";
		try {
			errInfo = " 当前用户没有[查询系统表]的权限. ";

			if (DBAccess.IsExitsObject("AA")) {
				errInfo = " 当前用户没有[删除表]的权限. ";
				sql = "DROP TABLE AA";
				DBAccess.RunSQL(sql);
			}

			errInfo = " 当前用户没有[创建表]的权限. ";
			sql = "CREATE TABLE AA (OID int NOT NULL)"; //检查是否可以创建表.
			DBAccess.RunSQL(sql);

			errInfo = " 当前用户没有[插入数据]的权限. ";
			sql = "INSERT INTO AA (OID) VALUES(100)";
			DBAccess.RunSQL(sql);

			try {
				//检查是否可以批量执行sql.
				sql = "UPDATE AA SET OID=0 WHERE OID=1;UPDATE AA SET OID=0 WHERE OID=1;";
				DBAccess.RunSQL(sql);
			} catch (java.lang.Exception e) {
				throw new RuntimeException("err@需要让数据库链接支持批量执行SQL语句，请修改数据库链接配置：&allowMultiQueries=true");
			}


			errInfo = " 当前用户没有[update 表数据]的权限. ";
			sql = "UPDATE AA SET OID=101";
			DBAccess.RunSQL(sql);

			errInfo = " 当前用户没有[delete 表数据]的权限. ";
			sql = "DELETE FROM AA";
			DBAccess.RunSQL(sql);

			errInfo = " 当前用户没有[创建表主键]的权限. ";
			DBAccess.CreatePK("AA", "OID", SystemConfig.getAppCenterDBType());

			errInfo = " 当前用户没有[创建索引]的权限. ";
			DBAccess.CreatIndex("AA", "OID"); //可否创建索引.

			errInfo = " 当前用户没有[查询数据表]的权限. ";
			sql = "select * from AA "; //检查是否有查询的权限.
			DBAccess.RunSQLReturnTable(sql);

			errInfo = " 当前数据库设置区分了大小写，不能对大小写敏感，如果是mysql数据库请参考 https://blog.csdn.net/ccflow/article/details/100079825 ";
			sql = "select * from aa "; //检查是否区分大小写.
			DBAccess.RunSQLReturnTable(sql);

			if (DBAccess.IsExitsObject("AAVIEW")) {
				errInfo = " 当前用户没有[删除视图]的权限. ";
				sql = "DROP VIEW AAVIEW";
				DBAccess.RunSQL(sql);
			}

			errInfo = " 当前用户没有[创建视图]的权限.";
			sql = "CREATE VIEW AAVIEW AS SELECT * FROM AA "; //检查是否可以创建视图.
			DBAccess.RunSQL(sql);

			errInfo = " 当前用户没有[删除视图]的权限.";
			sql = "DROP VIEW AAVIEW"; //检查是否可以删除视图.
			DBAccess.RunSQL(sql);

			errInfo = " 当前用户没有[删除表]的权限.";
			sql = "DROP TABLE AA"; //检查是否可以删除表.
			DBAccess.RunSQL(sql);

			if (SystemConfig.getAppCenterDBType() == DBType.MySQL) {
				sql = " set @@global.sql_mode ='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';";
				DBAccess.RunSQL(sql);
			}

			if (SystemConfig.getAppCenterDBDatabase().contains("-") == true) {
				throw new RuntimeException("err@数据库名称不能包含 '-' 号，您可以使用 '_' .");
			}


			return true;
		} catch (RuntimeException ex) {
			if (DBAccess.IsExitsObject("AA") == true) {
				sql = "DROP TABLE AA";
				DBAccess.RunSQL(sql);
			}

			if (DBAccess.IsExitsObject("AAVIEW") == true) {
				sql = "DROP VIEW AAVIEW";
				DBAccess.RunSQL(sql);
			}

			String info = "高凌工作流引擎 - 检查数据库安装权限出现错误:";
			info += "\t\n1. 当前登录的数据库帐号，必须有创建、删除视图或者table的权限。";
			info += "\t\n2. 必须对表有增、删、改、查的权限。 ";
			info += "\t\n3. 必须有删除创建索引主键的权限。 ";
			info += "\t\n4. 我们建议您设置当前数据库连接用户为管理员权限。 ";
			info += "\t\n ccbpm检查出来的信息如下：" + errInfo;
			info += "\t\n etc: 数据库测试异常信息:" + ex.getMessage();

			throw new RuntimeException("err@" + info);
			//  return false;
		}
	}

	/**
	 * 安装包
	 * <p>
	 * param lang 语言
	 * param demoType 0安装Demo, 1 不安装Demo.
	 */
	public static void DoInstallDataBase(String lang, int demoType) throws Exception {
		boolean isInstallFlowDemo = true;
		if (demoType == 2) {
			isInstallFlowDemo = false;
		}


		///#region 检查是否是空白的数据库。
		//if (DBAccess.IsExitsObject("WF_Emp")
		//     || DBAccess.IsExitsObject("WF_Flow")
		//     || DBAccess.IsExitsObject("Port_Emp")
		//    || DBAccess.IsExitsObject("CN_City"))
		//{
		//    throw new Exception("@当前的数据库好像是一个安装执行失败的数据库，里面包含了一些cc的表，所以您需要删除这个数据库然后执行重新安装。");
		//}

		///#endregion 检查是否是空白的数据库。


		///#region 首先创建Port类型的表, 让admin登录.

		FrmRB rb = new FrmRB();
		rb.CheckPhysicsTable();

		Emp portEmp = new Emp();
		portEmp.CheckPhysicsTable();

		Emp myemp = new Emp();
		myemp.CheckPhysicsTable();

		Dept mydept = new Dept();
		mydept.CheckPhysicsTable();

		Station mySta = new Station();
		mySta.CheckPhysicsTable();

		StationType myStaType = new StationType();
		myStaType.CheckPhysicsTable();

		DeptEmp myde = new DeptEmp();
		myde.CheckPhysicsTable();

		DeptEmpStation mydes = new DeptEmpStation();
		mydes.CheckPhysicsTable();


		FrmRB myrb = new FrmRB();
		myrb.CheckPhysicsTable();

		bp.wf.port.WFEmp wfemp = new bp.wf.port.WFEmp();
		wfemp.CheckPhysicsTable();

		if (DBAccess.IsExitsTableCol("WF_Emp", "StartFlows") == false) {
			String sql = "";
			//增加StartFlows这个字段
			switch (SystemConfig.getAppCenterDBType()) {
				case MSSQL:
					sql = "ALTER TABLE WF_Emp ADD StartFlows Text DEFAULT  NULL";
					break;
				case Oracle:
				case KingBaseR3:
				case KingBaseR6:
					sql = "ALTER TABLE  WF_EMP add StartFlows BLOB";
					break;
				case MySQL:
					sql = "ALTER TABLE WF_Emp ADD StartFlows TEXT COMMENT '可以发起的流程'";
					break;
				case Informix:
					sql = "ALTER TABLE WF_Emp ADD StartFlows VARCHAR(4000) DEFAULT  NULL";
					break;
				case PostgreSQL:
				case UX:
					sql = "ALTER TABLE WF_Emp ADD StartFlows Text DEFAULT  NULL";
					break;
				default:
					throw new RuntimeException("@没有涉及到的数据库类型");
			}
			DBAccess.RunSQL(sql);
		}

		///#endregion 首先创建Port类型的表.


		///#region 3, 执行基本的 sql
		String sqlscript = "";

		Emp empGPM = new Emp();
		empGPM.CheckPhysicsTable();

		//DBAccess.RunSQL("ALTER TABLE Port_Emp ADD Pass NVARCHAR(90) DEFAULT '' NULL ");

		sqlscript = SystemConfig.getCCFlowAppPath() + "WF/Data/Install/SQLScript/Port_Inc_CH_BPM.sql";
		DBAccess.RunSQLScript(sqlscript);

		Emp empAdmin = new Emp("admin");
		//生成Token
		bp.wf.Dev2Interface.Port_GenerToken("admin");
		WebUser.SignInOfGener(empAdmin, "CH", false, false, null, null);

		///#endregion 执行基本的 sql

		ArrayList al = null;
		String info = "bp.en.Entity";
		al = ClassFactory.GetObjects(info);


		///#region 先创建表，否则列的顺序就会变化.
		FlowExt fe = new FlowExt();
		fe.CheckPhysicsTable();

		NodeExt ne = new NodeExt();
		ne.CheckPhysicsTable();

		MapDtl mapdtl = new MapDtl();
		mapdtl.CheckPhysicsTable();

		MapData mapData = new MapData();
		mapData.CheckPhysicsTable();

		SysEnum sysenum = new SysEnum();
		sysenum.CheckPhysicsTable();

		CC cc = new CC();
		cc.CheckPhysicsTable();

		NodeWorkCheck fwc = new NodeWorkCheck();
		fwc.CheckPhysicsTable();

		MapAttr attr = new MapAttr();
		attr.CheckPhysicsTable();

		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.CheckPhysicsTable();

		CH ch = new CH();
		ch.CheckPhysicsTable();


		///#endregion 先创建表，否则列的顺序就会变化.


		///#region 1, 创建or修复表
		for (Object obj : al) {
			Entity en = null;
			en = obj instanceof Entity ? (Entity) obj : null;
			if (en == null|| en.equals("null")) {
				continue;
			}

			//获得类名.
			String clsName = en.toString();
			if (DataType.IsNullOrEmpty(clsName) == true) {
				continue;
			}

			if (clsName.contains("FlowSheet") == true) {
				continue;
			}
			if (clsName.contains("NodeSheet") == true) {
				continue;
			}
			if (clsName.contains("FlowFormTree") == true) {
				continue;
			}
			//if (clsName.contains("GEWork") == true) {
			//	continue;
			//}

			//不安装CCIM的表.
			if (clsName.contains("BP.CCIM")) {
				continue;
			}

			//抽象的类不允许创建表.
			switch (clsName.toUpperCase()) {
			  case "BP.WF.STARTWORK":
                    case "BP.WF.WORK":
                    case "BP.WF.GESTARTWORK":
                    case "BP.EN.GENONAME":
                    case "BP.EN.GETREE":
                    case "BP.WF.GERpt":
                    case "BP.WF.GEENTITY":
                    case "BP.SYS.TSENTITYNONAME":
					continue;
				default:
					break;
			}

			if (isInstallFlowDemo == false) {
				/*如果不安装demo.*/
				if (clsName.contains("BP.CN") || clsName.contains("BP.Demo")) {
					continue;
				}
			}


			String table = null;
			try {
				table = en.getEnMap().getPhysicsTable();
				if (table == null) {
					continue;
				}
			} catch (java.lang.Exception e) {
				continue;
			}

			try {
				switch (table) {
					case "WF_EmpWorks":
					case "WF_GenerEmpWorkDtls":
					case "WF_GenerEmpWorks":
						continue;
					case "Sys_Enum":
					case "Sys_Enums":
						en.CheckPhysicsTable();
						break;
					default:
						en.CheckPhysicsTable();
						break;
				}
				en.CheckPhysicsTable();
			} catch (java.lang.Exception e2) {
			}
		}

		///#endregion 修复


		///#region 2, 注册枚举类型 SQL
		// 2, 注册枚举类型。
		bp.sys.xml.EnumInfoXmls xmls = new bp.sys.xml.EnumInfoXmls();
		xmls.RetrieveAll();
		for (bp.sys.xml.EnumInfoXml xml : xmls.ToJavaList()) {
			SysEnums ses = new SysEnums();
			int count = ses.RetrieveByAttr(SysEnumAttr.EnumKey, xml.getKey());
			if (count > 0) {
				continue;
			}
			ses.RegIt(xml.getKey(), xml.getVals());
		}

		///#endregion 注册枚举类型


		///#region 2.1 重新构建视图. //删除视图.
		if (DBAccess.IsExitsObject("WF_EmpWorks") == true) {
			DBAccess.RunSQL("DROP VIEW WF_EmpWorks");
		}

		if (DBAccess.IsExitsObject("V_WF_Delay") == true) {
			DBAccess.RunSQL("DROP VIEW V_WF_Delay");
		}

		if (DBAccess.IsExitsObject("V_FlowStarter") == true) {
			DBAccess.RunSQL("DROP VIEW V_FlowStarter");
		}

		if (DBAccess.IsExitsObject("V_FlowStarterBPM") == true) {
			DBAccess.RunSQL("DROP VIEW V_FlowStarterBPM");
		}

		if (DBAccess.IsExitsObject("V_TOTALCH") == true) {
			DBAccess.RunSQL("DROP VIEW V_TOTALCH");
		}

		if (DBAccess.IsExitsObject("V_TOTALCHYF") == true) {
			DBAccess.RunSQL("DROP VIEW V_TOTALCHYF");
		}

		if (DBAccess.IsExitsObject("V_TotalCHWeek") == true) {
			DBAccess.RunSQL("DROP VIEW V_TotalCHWeek");
		}

		///#endregion 重新构建视图. //删除视图.


		///#region 4, 创建视图与数据.
		//执行必须的sql.

		sqlscript = "";
		//执行必须的sql.
		switch (SystemConfig.getAppCenterDBType()) {
			case Oracle:
			case KingBaseR3:
			case KingBaseR6:
				sqlscript = SystemConfig.getCCFlowAppPath() + "WF/Data/Install/SQLScript/InitView_Ora.sql";
				break;
			case MSSQL:
			case Informix:
				sqlscript = SystemConfig.getCCFlowAppPath() + "WF/Data/Install/SQLScript/InitView_SQL.sql";
				break;
			case MySQL:
				sqlscript = SystemConfig.getCCFlowAppPath() + "WF/Data/Install/SQLScript/InitView_MySQL.sql";
				break;
			case PostgreSQL:
			case UX:
				sqlscript = SystemConfig.getCCFlowAppPath() + "WF/Data/Install/SQLScript/InitView_PostgreSQL.sql";
				break;
			default:
				break;
		}
		DBAccess.RunSQLScript(sqlscript);

		///#endregion 创建视图与数据


		///#region 5, 初始化数据.
		if (isInstallFlowDemo) {
			// sqlscript =  bp.difference.SystemConfig.getPathOfData() + "Install/SQLScript/InitPublicData.sql";
			// DBAccess.RunSQLScript(sqlscript);
		}

		///#endregion 初始化数据


		///#region 6, 生成临时的 wf_emp 数据。
		if (isInstallFlowDemo == true) {
			Emps emps = new Emps();
			emps.RetrieveAllFromDBSource();
			int i = 0;
			for (Emp emp : emps.ToJavaList()) {
				i++;
				bp.wf.port.WFEmp wfEmp = new bp.wf.port.WFEmp();
				wfEmp.Copy(emp);
				wfEmp.setNo(emp.getUserID());

				if (wfEmp.getEmail().length() == 0) {
					wfEmp.setEmail(emp.getUserID() + "@ccflow.org");
				}

				if (wfEmp.getTel().length() == 0) {
					wfEmp.setTel("82374939-6" + StringHelper.padLeft(String.valueOf(i), 2, '0'));
				}

				if (wfEmp.getIsExits()) {
					wfEmp.Update();
				} else {
					wfEmp.Insert();
				}
			}

			// 生成简历数据.
			/*for (Emp emp : emps.ToJavaList()) {
				for (int myIdx = 0; myIdx < 6; myIdx++) {
					String sql = "";
					sql = "INSERT INTO Demo_Resume (OID,RefPK,NianYue,GongZuoDanWei,ZhengMingRen,BeiZhu,QT) ";
					sql += "VALUES(" + DBAccess.GenerOID("Demo_Resume") + ",'" + emp.getUserID() + "','200" + myIdx + "-01','成都.高凌" + myIdx + "公司','张三','表现良好','其他-" + myIdx + "无')";
					DBAccess.RunSQL(sql);
				}
			}

			DataTable dtStudent = DBAccess.RunSQLReturnTable("SELECT No FROM Demo_Student");
			for (DataRow dr : dtStudent.Rows) {
				String no = dr.getValue(0).toString();
				for (int myIdx = 0; myIdx < 6; myIdx++) {
					String sql = "";
					sql = "INSERT INTO Demo_Resume (OID,RefPK,NianYue,GongZuoDanWei,ZhengMingRen,BeiZhu,QT) ";
					sql += "VALUES(" + DBAccess.GenerOID("Demo_Resume") + ",'" + no + "','200" + myIdx + "-01','成都.高凌" + myIdx + "公司','张三','表现良好','其他-" + myIdx + "无')";
					DBAccess.RunSQL(sql);
				}
			}
			GenerWorkFlowViewNY ny = new GenerWorkFlowViewNY();
			ny.CheckPhysicsTable();*/
			// 生成年度月份数据.
			String sqls = "";
			//Date dtNow = new Date();
			for (int num = 0; num < 12; num++) {
				sqls = "@ INSERT INTO Pub_NY (No,Name) VALUES ('" + DateUtils.format(new Date(), "yyyy-MM") + "','" + DateUtils.format(new Date(), "yyyy-MM") + "')  ";
			}
			DBAccess.RunSQLs(sqls);
		}

		///#endregion 初始化数据


		///#region 7, 装载 demo.flow
		if (isInstallFlowDemo == true) {
			Emp emp = new Emp("admin");
			WebUser.SignInOfGener(emp, "CH", false, false, null, null);
			bp.sys.base.Glo.WriteLineInfo("开始装载模板...");
			String msg = "";

			//装载数据模版.
			bp.wf.dts.LoadTemplete l = new bp.wf.dts.LoadTemplete();
			Object tempVar = l.Do();
			msg = tempVar instanceof String ? (String) tempVar : null;


			bp.sys.base.Glo.WriteLineInfo("装载模板完成。开始修复视图...");


			bp.sys.base.Glo.WriteLineInfo("视图修复完成。");
		}

		if (isInstallFlowDemo == false) {
			//创建一个空白的流程，不然，整个结构就出问题。
			//    FlowSorts fss = new FlowSorts();
			// fss.RetrieveAll();
			//  fss.Delete();
			DBAccess.RunSQL("DELETE FROM WF_FlowSort ");

			FlowSort fs = new FlowSort();
			fs.setName("流程树");
			fs.setNo("1");
			fs.setParentNo("0");
			fs.Insert();

			FlowSort s1 = (FlowSort) fs.DoCreateSubNode(null);
			s1.setName("日常办公类");
			s1.Update();

			//加载一个模版,不然用户不知道如何新建流程.
			TemplateGlo.LoadFlowTemplate(s1.getNo(), SystemConfig.getPathOfData() + "Install/QingJiaFlowDemoInit.xml", ImpFlowTempleteModel.AsTempleteFlowNo);
			Flow fl = new Flow("001");
			fl.DoCheck(); //做一下检查.

			s1 = (FlowSort) fs.DoCreateSubNode(null);
			s1.setName("财务类");
			s1.Update();

			s1 = (FlowSort) fs.DoCreateSubNode(null);
			s1.setName("人力资源类");
			s1.Update();


			//创建一个空白的流程，不然，整个结构就出问题。
			//BP.Sys.FrmTrees frmTrees = new FrmTrees();
			//frmTrees.RetrieveAll();
			//frmTrees.Delete();

			DBAccess.RunSQL("DELETE FROM Sys_FormTree ");
			FrmTree ftree = new FrmTree();
			ftree.setName("表单树");
			ftree.setNo("1");
			ftree.setParentNo("0");
			ftree.Insert();

			FrmTree subFrmTree = (FrmTree) ftree.DoCreateSubNode();
			subFrmTree.setName("流程独立表单");
			subFrmTree.Update();

			subFrmTree = (FrmTree) ftree.DoCreateSubNode();
			subFrmTree.setName("常用信息管理");
			subFrmTree.Update();

			subFrmTree = (FrmTree) ftree.DoCreateSubNode();
			subFrmTree.setName("常用单据");
			subFrmTree.Update();

		}

		///#endregion 装载demo.flow


		///#region 增加图片签名
		if (isInstallFlowDemo == true) {
			try {
				//增加图片签名
				bp.wf.dts.GenerSiganture gs = new bp.wf.dts.GenerSiganture();
				gs.Do();
			} catch (java.lang.Exception e3) {
			}
		}

		///#endregion 增加图片签名.

		//生成拼音，以方便关键字查找.
		bp.wf.dts.GenerPinYinForEmp pinyin = new bp.wf.dts.GenerPinYinForEmp();
		pinyin.Do();


		///#region 执行补充的sql, 让外键的字段长度都设置成100.
		DBAccess.RunSQL("UPDATE Sys_MapAttr SET maxlen=100 WHERE LGType=2 AND MaxLen<100");
		DBAccess.RunSQL("UPDATE Sys_MapAttr SET maxlen=100 WHERE KeyOfEn='FK_Dept'");

		//Nodes nds = new Nodes();
		//nds.RetrieveAll();
		//foreach (Node nd in nds)
		//    nd.HisWork.CheckPhysicsTable();

		///#endregion 执行补充的sql, 让外键的字段长度都设置成100.


		///#region 如果是第一次运行，就执行检查。
		if (isInstallFlowDemo == true) {
			Flows fls = new Flows();
			fls.RetrieveAllFromDBSource();
			for (Flow fl : fls.ToJavaList()) {
				try {
					fl.DoCheck();
				} catch (RuntimeException ex) {
					Log.DebugWriteError(ex.getMessage());
				}
			}
		}

		///#endregion 如果是第一次运行，就执行检查。


		///#region 增加大文本字段列.
		try {
			DBAccess.GetBigTextFromDB("Sys_MapData", "No", "001", "HtmlTemplateFile");
		} catch (java.lang.Exception e4) {
		}

		///#endregion 增加大文本字段列.
	}

	/**
	 * 检查树结构是否符合需求
	 *
	 * @return
	 */
	public static boolean CheckTreeRoot() throws Exception {
		// 流程树根节点校验
		String tmp = "SELECT Name FROM WF_FlowSort WHERE ParentNo='0'";
		tmp = DBAccess.RunSQLReturnString(tmp);
		if (DataType.IsNullOrEmpty(tmp)) {
			tmp = "INSERT INTO WF_FlowSort(No,Name,ParentNo,TreeNo,idx,IsDir) values('01','流程树',0,'',0,0)";
			DBAccess.RunSQLReturnString(tmp);
		}

		// 表单树根节点校验
		tmp = "SELECT Name FROM Sys_FormTree WHERE ParentNo = '0' ";
		tmp = DBAccess.RunSQLReturnString(tmp);
		if (DataType.IsNullOrEmpty(tmp)) {
			tmp = "INSERT INTO Sys_FormTree(No,Name,ParentNo,Idx,IsDir) values('001','表单树',0,0,0)";
			DBAccess.RunSQLReturnString(tmp);
		}

		//检查组织结构是否正确.
		String sql = "SELECT * FROM Port_Dept WHERE ParentNo='0' ";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0) {
			Dept rootDept = new Dept();
			rootDept.setName("总部");
			rootDept.setParentNo("0");
			try {
				rootDept.Insert();
			} catch (RuntimeException ex) {
				Log.DebugWriteError("@尝试向port_dept插入数据失败，应该是视图问题. 技术信息:" + ex.getMessage());
			}
			throw new RuntimeException("@没有找到部门树为0个根节点, 有可能是因为您在集成cc的时候，没有遵守cc的规则，部门树的根节点必须是ParentNo=0。");
		}
		return true;
	}

//	public static void KillProcess(String processName) //杀掉进程的方法
//	{
//		System.Diagnostics.Process[] processes = System.Diagnostics.Process.GetProcesses();
//		for (System.Diagnostics.Process pro : processes)
//		{
//			String name = pro.ProcessName + ".exe";
//			if (name.toLowerCase().equals(processName.toLowerCase()))
//			{
//				pro.Kill();
//			}
//		}
//	}

	/**
	 * 产生新的编号
	 * <p>
	 * param rptKey
	 *
	 * @return
	 */
	public static String GenerFlowNo(String rptKey) {
		rptKey = rptKey.replace("ND", "");
		rptKey = rptKey.replace("Rpt", "");
		switch (rptKey.length()) {
			case 0:
				return "001";
			case 1:
				return "00" + rptKey;
			case 2:
				return "0" + rptKey;
			case 3:
				return rptKey;
			default:
				return "001";
		}
	}

	/**
	 *
	 */
	public static boolean isShowFlowNum() throws Exception {
		switch (SystemConfig.GetValByKey("IsShowFlowNum", "")) {
			case "1":
				return true;
			default:
				return false;
		}
	}

	/**
	 * 产生word文档.
	 * <p>
	 * param wk
	 */
	public static void GenerWord(Object filename, Work wk) {
//		bp.wf.Glo.KillProcess("WINWORD.EXE");
//		String enName = wk.getEnMap().getPhysicsTable();
//		try
//		{
//			RegistryKey delKey = Registry.LocalMachine.OpenSubKey("HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Shared Tools\\Text Converters\\Import\\", true);
//			delKey.DeleteValue("MSWord6.wpc");
//			delKey.Close();
//		}
//		catch (java.lang.Exception e)
//		{
//		}
//
//		GroupField currGF = new GroupField();
//		MapAttrs mattrs = new MapAttrs(enName);
//		GroupFields gfs = new GroupFields(enName);
//		MapDtls dtls = new MapDtls(enName);
//		for (MapDtl dtl : dtls)
//		{
//			dtl.IsUse = false;
//		}
//
//		// 计算出来单元格的行数。
//		int rowNum = 0;
//		for (GroupField gf : gfs)
//		{
//			rowNum++;
//			boolean isLeft = true;
//			for (MapAttr attr : mattrs)
//			{
//				if (attr.getUIVisible()== false)
//				{
//					continue;
//				}
//
//				if (attr.GroupID != gf.OID)
//				{
//					continue;
//				}
//
//				if (attr.UIIsLine)
//				{
//					rowNum++;
//					isLeft = true;
//					continue;
//				}
//
//				if (isLeft == false)
//				{
//					rowNum++;
//				}
//				isLeft = !isLeft;
//			}
//		}
//
//		rowNum = rowNum + 2 + dtls.size();
//
//		// 创建Word文档
//		String CheckedInfo = "";
//		String message = "";
//		Object Nothing = System.Reflection.Missing.Value;


		///#region 没用代码
		//  object filename = fileName;

		//Word.Application WordApp = new Word.ApplicationClass();
		//Word.Document WordDoc = WordApp.Documents.Add(ref  Nothing, ref  Nothing, ref  Nothing, ref  Nothing);
		//try
		//{
		//    WordApp.ActiveWindow.View.Type = Word.WdViewType.wdOutlineView;
		//    WordApp.ActiveWindow.View.SeekView = Word.WdSeekView.wdSeekPrimaryHeader;

		//    #region 增加页眉
		//    // 添加页眉 插入图片
		//    string pict =  bp.difference.SystemConfig.getPathOfDataUser() + "log.jpg"; // 图片所在路径
		//    if (System.IO.File.Exists(pict))
		//    {
		//        System.Drawing.Image img = System.Drawing.Image.FromFile(pict);
		//        object LinkToFile = false;
		//        object SaveWithDocument = true;
		//        object Anchor = WordDoc.Application.Selection.Range;
		//        WordDoc.Application.ActiveDocument.InlineShapes.AddPicture(pict, ref  LinkToFile,
		//            ref  SaveWithDocument, ref  Anchor);
		//        //    WordDoc.Application.ActiveDocument.InlineShapes[1].Width = img.Width; // 图片宽度
		//        //    WordDoc.Application.ActiveDocument.InlineShapes[1].Height = img.Height; // 图片高度
		//    }
		//    WordApp.ActiveWindow.ActivePane.Selection.InsertAfter("[高凌业务流程管理系统 http://ccFlow.org]");
		//    WordApp.Selection.ParagraphFormat.Alignment = Word.WdParagraphAlignment.wdAlignParagraphLeft; // 设置右对齐
		//    WordApp.ActiveWindow.View.SeekView = Word.WdSeekView.wdSeekMainDocument; // 跳出页眉设置
		//    WordApp.Selection.ParagraphFormat.LineSpacing = 15f; // 设置文档的行间距
		//    #endregion

		//    // 移动焦点并换行
		//    object count = 14;
		//    object WdLine = Word.WdUnits.wdLine; // 换一行;
		//    WordApp.Selection.MoveDown(ref  WdLine, ref  count, ref  Nothing); // 移动焦点
		//    WordApp.Selection.TypeParagraph(); // 插入段落

		//    // 文档中创建表格
		//    Word.Table newTable = WordDoc.Tables.add(WordApp.Selection.Range, rowNum, 4, ref  Nothing, ref  Nothing);

		//    // 设置表格样式
		//    newTable.Borders.OutsideLineStyle = Word.WdLineStyle.wdLineStyleThickThinLargeGap;
		//    newTable.Borders.InsideLineStyle = Word.WdLineStyle.wdLineStyleSingle;

		//    newTable.Columns[1].Width = 100f;
		//    newTable.Columns[2].Width = 100f;
		//    newTable.Columns[3].Width = 100f;
		//    newTable.Columns[4].Width = 100f;

		//    // 填充表格内容
		//    newTable.Cell(1, 1).Range.Text = wk.EnDesc;
		//    newTable.Cell(1, 1).Range.Bold = 2; // 设置单元格中字体为粗体

		//    // 合并单元格
		//    newTable.Cell(1, 1).Merge(newTable.Cell(1, 4));
		//    WordApp.Selection.Cells.VerticalAlignment = Word.WdCellVerticalAlignment.wdCellAlignVerticalCenter; // 垂直居中
		//    WordApp.Selection.ParagraphFormat.Alignment = Word.WdParagraphAlignment.wdAlignParagraphCenter; // 水平居中

		//    int groupIdx = 1;
		//    foreach (GroupField gf in gfs)
		//    {
		//        groupIdx++;
		//        // 填充表格内容
		//        newTable.Cell(groupIdx, 1).Range.Text = gf.Lab;
		//        newTable.Cell(groupIdx, 1).Range.Font.Color = Word.WdColor.wdColorDarkBlue; // 设置单元格内字体颜色
		//        newTable.Cell(groupIdx, 1).Shading.BackgroundPatternColor = Word.WdColor.wdColorGray25;
		//        // 合并单元格
		//        newTable.Cell(groupIdx, 1).Merge(newTable.Cell(groupIdx, 4));
		//        WordApp.Selection.Cells.VerticalAlignment = Word.WdCellVerticalAlignment.wdCellAlignVerticalCenter;

		//        groupIdx++;

		//        bool isLeft = true;
		//        bool isColumns2 = false;
		//        int currColumnIndex = 0;
		//        foreach (MapAttr attr in mattrs)
		//        {
		//            if (attr.getUIVisible()== false)
		//                continue;

		//            if (attr.GroupID != gf.OID)
		//                continue;

		//            if (newTable.Rows.size() < groupIdx)
		//                continue;

		//            #region 增加从表
		//            foreach (MapDtl dtl in dtls)
		//            {
		//                if (dtl.IsUse)
		//                    continue;

		//                if (dtl.RowIdx != groupIdx - 3)
		//                    continue;

		//                if (gf.OID != dtl.GroupID)
		//                    continue;

		//                GEDtls dtlsDB = new GEDtls(dtl.getNo());
		//                QueryObject qo = new QueryObject(dtlsDB);
		//                switch (dtl.getDtlOpenType())
		//                {
		//                    case DtlOpenType.ForEmp:
		//                        qo.AddWhere(GEDtlAttr.RefPK, wk.OID);
		//                        break;
		//                    case DtlOpenType.ForWorkID:
		//                        qo.AddWhere(GEDtlAttr.RefPK, wk.OID);
		//                        break;
		//                    case DtlOpenType.ForFID:
		//                        qo.AddWhere(GEDtlAttr.FID, wk.OID);
		//                        break;
		//                }
		//                qo.DoQuery();

		//                newTable.Rows[groupIdx].SetHeight(100f, Word.WdRowHeightRule.wdRowHeightAtLeast);
		//                newTable.Cell(groupIdx, 1).Merge(newTable.Cell(groupIdx, 4));

		//                Attrs dtlAttrs = dtl.GenerMap().Attrs;
		//                int colNum = 0;
		//                foreach (Attr attrDtl in dtlAttrs)
		//                {
		//                    if (attrDtl.UIVisible == false)
		//                        continue;
		//                    colNum++;
		//                }

		//                newTable.Cell(groupIdx, 1).Select();
		//                WordApp.Selection.Delete(ref Nothing, ref Nothing);
		//                Word.Table newTableDtl = WordDoc.Tables.add(WordApp.Selection.Range, dtlsDB.size() + 1, colNum,
		//                    ref Nothing, ref Nothing);

		//                newTableDtl.Borders.OutsideLineStyle = Word.WdLineStyle.wdLineStyleSingle;
		//                newTableDtl.Borders.InsideLineStyle = Word.WdLineStyle.wdLineStyleSingle;

		//                int colIdx = 1;
		//                foreach (Attr attrDtl in dtlAttrs)
		//                {
		//                    if (attrDtl.UIVisible == false)
		//                        continue;
		//                    newTableDtl.Cell(1, colIdx).Range.Text = attrDtl.Desc;
		//                    colIdx++;
		//                }

		//                int idxRow = 1;
		//                foreach (GEDtl item in dtlsDB)
		//                {
		//                    idxRow++;
		//                    int columIdx = 0;
		//                    foreach (Attr attrDtl in dtlAttrs)
		//                    {
		//                        if (attrDtl.UIVisible == false)
		//                            continue;
		//                        columIdx++;

		//                        if (attrDtl.IsFKorEnum)
		//                            newTableDtl.Cell(idxRow, columIdx).Range.Text = item.GetValRefTextByKey(attrDtl.Key);
		//                        else
		//                        {
		//                            if (attrDtl.MyDataType == DataType.AppMoney)
		//                                newTableDtl.Cell(idxRow, columIdx).Range.Text = item.GetValMoneyByKey(attrDtl.Key).ToString("0.00");
		//                            else
		//                                newTableDtl.Cell(idxRow, columIdx).Range.Text = item.GetValStrByKey(attrDtl.Key);

		//                            if (attrDtl.IsNum)
		//                                newTableDtl.Cell(idxRow, columIdx).Range.ParagraphFormat.Alignment = Microsoft.Office.Interop.Word.WdParagraphAlignment.wdAlignParagraphRight;
		//                        }
		//                    }
		//                }

		//                groupIdx++;
		//                isLeft = true;
		//            }
		//            #endregion 增加从表

		//            if (attr.UIIsLine)
		//            {
		//                currColumnIndex = 0;
		//                isLeft = true;
		//                if (attr.IsBigDoc)
		//                {
		//                    newTable.Rows[groupIdx].SetHeight(100f, Word.WdRowHeightRule.wdRowHeightAtLeast);
		//                    newTable.Cell(groupIdx, 1).Merge(newTable.Cell(groupIdx, 4));
		//                    newTable.Cell(groupIdx, 1).Range.Text = attr.Name + ":\r\n" + wk.GetValStrByKey(attr.getKeyOfEn());
		//                }
		//                else
		//                {
		//                    newTable.Cell(groupIdx, 2).Merge(newTable.Cell(groupIdx, 4));
		//                    newTable.Cell(groupIdx, 1).Range.Text = attr.Name;
		//                    newTable.Cell(groupIdx, 2).Range.Text = wk.GetValStrByKey(attr.getKeyOfEn());
		//                }
		//                groupIdx++;
		//                continue;
		//            }
		//            else
		//            {
		//                if (attr.IsBigDoc)
		//                {
		//                    if (currColumnIndex == 2)
		//                    {
		//                        currColumnIndex = 0;
		//                    }

		//                    newTable.Rows[groupIdx].SetHeight(100f, Word.WdRowHeightRule.wdRowHeightAtLeast);
		//                    if (currColumnIndex == 0)
		//                    {
		//                        newTable.Cell(groupIdx, 1).Merge(newTable.Cell(groupIdx, 2));
		//                        newTable.Cell(groupIdx, 1).Range.Text = attr.Name + ":\r\n" + wk.GetValStrByKey(attr.getKeyOfEn());
		//                        currColumnIndex = 3;
		//                        continue;
		//                    }
		//                    else if (currColumnIndex == 3)
		//                    {
		//                        newTable.Cell(groupIdx, 2).Merge(newTable.Cell(groupIdx, 3));
		//                        newTable.Cell(groupIdx, 2).Range.Text = attr.Name + ":\r\n" + wk.GetValStrByKey(attr.getKeyOfEn());
		//                        currColumnIndex = 0;
		//                        groupIdx++;
		//                        continue;
		//                    }
		//                    else
		//                    {
		//                        continue;
		//                    }
		//                }
		//                else
		//                {
		//                    string s = "";
		//                    if (attr.getLGType() == FieldTypeS.Normal)
		//                    {
		//                        if (attr.getMyDataType() == DataType.AppMoney)
		//                            s = wk.GetValDecimalByKey(attr.getKeyOfEn()).ToString("0.00");
		//                        else
		//                            s = wk.GetValStrByKey(attr.getKeyOfEn());
		//                    }
		//                    else
		//                    {
		//                        s = wk.GetValRefTextByKey(attr.getKeyOfEn());
		//                    }

		//                    switch (currColumnIndex)
		//                    {
		//                        case 0:
		//                            newTable.Cell(groupIdx, 1).Range.Text = attr.Name;
		//                            if (attr.IsSigan)
		//                            {
		//                                string path =  bp.difference.SystemConfig.getPathOfDataUser() + "/Siganture/" + s + ".jpg";
		//                                if (System.IO.File.Exists(path))
		//                                {
		//                                    System.Drawing.Image img = System.Drawing.Image.FromFile(path);
		//                                    object LinkToFile = false;
		//                                    object SaveWithDocument = true;
		//                                    //object Anchor = WordDoc.Application.Selection.Range;
		//                                    object Anchor = newTable.Cell(groupIdx, 2).Range;

		//                                    WordDoc.Application.ActiveDocument.InlineShapes.AddPicture(path, ref  LinkToFile,
		//                                        ref  SaveWithDocument, ref  Anchor);
		//                                    //    WordDoc.Application.ActiveDocument.InlineShapes[1].Width = img.Width; // 图片宽度
		//                                    //    WordDoc.Application.ActiveDocument.InlineShapes[1].Height = img.Height; // 图片高度
		//                                }
		//                                else
		//                                {
		//                                    newTable.Cell(groupIdx, 2).Range.Text = s;
		//                                }
		//                            }
		//                            else
		//                            {
		//                                if (attr.IsNum)
		//                                {
		//                                    newTable.Cell(groupIdx, 2).Range.Text = s;
		//                                    newTable.Cell(groupIdx, 2).Range.ParagraphFormat.Alignment = Microsoft.Office.Interop.Word.WdParagraphAlignment.wdAlignParagraphRight;
		//                                }
		//                                else
		//                                {
		//                                    newTable.Cell(groupIdx, 2).Range.Text = s;
		//                                }
		//                            }
		//                            currColumnIndex = 1;
		//                            continue;
		//                            break;
		//                        case 1:
		//                            newTable.Cell(groupIdx, 3).Range.Text = attr.Name;
		//                            if (attr.IsSigan)
		//                            {
		//                                string path =  bp.difference.SystemConfig.getPathOfDataUser() + "/Siganture/" + s + ".jpg";
		//                                if (System.IO.File.Exists(path))
		//                                {
		//                                    System.Drawing.Image img = System.Drawing.Image.FromFile(path);
		//                                    object LinkToFile = false;
		//                                    object SaveWithDocument = true;
		//                                    object Anchor = newTable.Cell(groupIdx, 4).Range;
		//                                    WordDoc.Application.ActiveDocument.InlineShapes.AddPicture(path, ref  LinkToFile,
		//                                        ref  SaveWithDocument, ref  Anchor);
		//                                }
		//                                else
		//                                {
		//                                    newTable.Cell(groupIdx, 4).Range.Text = s;
		//                                }
		//                            }
		//                            else
		//                            {
		//                                if (attr.IsNum)
		//                                {
		//                                    newTable.Cell(groupIdx, 4).Range.Text = s;
		//                                    newTable.Cell(groupIdx, 4).Range.ParagraphFormat.Alignment = Microsoft.Office.Interop.Word.WdParagraphAlignment.wdAlignParagraphRight;
		//                                }
		//                                else
		//                                {
		//                                    newTable.Cell(groupIdx, 4).Range.Text = s;
		//                                }
		//                            }
		//                            currColumnIndex = 0;
		//                            groupIdx++;
		//                            continue;
		//                            break;
		//                        default:
		//                            break;
		//                    }
		//                }
		//            }
		//        }
		//    }  //结束循环

		//    #region 添加页脚
		//    WordApp.ActiveWindow.View.SeekView = Word.WdSeekView.wdSeekPrimaryFooter;
		//    WordApp.ActiveWindow.ActivePane.Selection.InsertAfter("模板由ccflow自动生成，严谨转载。此流程的详细内容请访问 http://doc.ccFlow.org。 建造流程管理系统请致电: 0531-82374939  ");
		//    WordApp.Selection.ParagraphFormat.Alignment = Word.WdParagraphAlignment.wdAlignParagraphRight;
		//    #endregion

		//    // 文件保存
		//    WordDoc.SaveAs(ref  filename, ref  Nothing, ref  Nothing, ref  Nothing,
		//        ref  Nothing, ref  Nothing, ref  Nothing, ref  Nothing,
		//        ref  Nothing, ref  Nothing, ref  Nothing, ref  Nothing, ref  Nothing,
		//        ref  Nothing, ref  Nothing, ref  Nothing);

		//    WordDoc.Close(ref  Nothing, ref  Nothing, ref  Nothing);
		//    WordApp.Quit(ref  Nothing, ref  Nothing, ref  Nothing);
		//    try
		//    {
		//        string docFile = filename.ToString();
		//        string pdfFile = docFile.Replace(".doc", ".pdf");
		//        Glo.Rtf2PDF(docFile, pdfFile);
		//    }
		//    catch (Exception ex)
		//    {
		//        Log.DebugWriteInfo("@生成pdf失败." + ex.Message);
		//    }
		//}
		//catch (Exception ex)
		//{
		//    throw ex;
		//    // WordApp.Quit(ref  Nothing, ref  Nothing, ref  Nothing);
		//    WordDoc.Close(ref  Nothing, ref  Nothing, ref  Nothing);
		//    WordApp.Quit(ref  Nothing, ref  Nothing, ref  Nothing);
		//}

		///#endregion
	}

	///#endregion 执行安装.


	///#region 流程模版的ftp服务器配置.
	public static String getTemplateFTPHost() throws Exception {
		return SystemConfig.GetValByKey("TemplateFTPHost", "116.239.32.14");
	}

	public static int getTemplateFTPPort() throws Exception {
		return SystemConfig.GetValByKeyInt("TemplateFTPPort", 9997);
	}

	public static String getTemplateFTPUser() throws Exception {
		return SystemConfig.GetValByKey("TemplateFTPUser", "oa");
	}

	public static String getTemplateFTPPassword() throws Exception {
		return SystemConfig.GetValByKey("TemplateFTPPassword", "Jszx1234");
	}

	///#endregion 流程模版的ftp服务器配置.


	///#region 全局的方法处理

	/**
	 * 流程数据表系统字段,中间用,分开.
	 */
	public static String getFlowFields() throws Exception {
		String str = "";
		str += GERptAttr.OID + ",";
		str += GERptAttr.AtPara + ",";
		str += GERptAttr.BillNo + ",";
		//  str += GERptAttr.CFlowNo + ",";
		//  str += GERptAttr.CWorkID + ",";
		str += GERptAttr.FID + ",";
		str += GERptAttr.FK_Dept + ",";
		str += GERptAttr.FK_NY + ",";
		str += GERptAttr.FlowDaySpan + ",";
		str += GERptAttr.FlowEmps + ",";
		str += GERptAttr.FlowEnder + ",";
		str += GERptAttr.FlowEnderRDT + ",";
		str += GERptAttr.FlowEndNode + ",";
		str += GERptAttr.FlowNote + ",";
		str += GERptAttr.FlowStarter + ",";
		str += GERptAttr.FlowStartRDT + ",";
		str += GERptAttr.GuestName + ",";
		str += GERptAttr.GuestNo + ",";
		str += GERptAttr.GUID + ",";
		str += GERptAttr.PEmp + ",";
		str += GERptAttr.PFID + ",";
		str += GERptAttr.PFlowNo + ",";
		str += GERptAttr.PNodeID + ",";
		str += GERptAttr.PrjName + ",";
		str += GERptAttr.PrjNo + ",";
		str += GERptAttr.PWorkID + ",";
		str += GERptAttr.Title + ",";
		str += GERptAttr.WFSta + ",";
		str += GERptAttr.WFState + ",";
		str += "Rec,";
		str += "CDT,RDT,WFStateText";
		return str;
		// return typeof(GERptAttr).GetFields().Select(o => o.getName()).ToList();
	}


	///#region 与流程事件实体相关.
	private static Hashtable Htable_FlowFEE = null;

	/**
	 * 获得节点事件实体
	 * <p>
	 * param enName 实例名称
	 *
	 * @return 获得节点事件实体, 如果没有就返回为空.
	 */
	public static FlowEventBase GetFlowEventEntityByEnName(String enName) {
		if (Htable_FlowFEE == null || Htable_FlowFEE.isEmpty()) {
			Htable_FlowFEE = new Hashtable();

			String name = "BP.WF.FlowEventBase";


			ArrayList<FlowEventBase> al = bp.en.ClassFactory.GetObjects("bp.wf.FlowEventBase");
			for (FlowEventBase en : al) {
				if (Htable_FlowFEE.containsKey(en.toString()) == true) {
					continue;
				}
				Htable_FlowFEE.put(en.getClass().getName(), en);
			}
		}
		FlowEventBase myen = Htable_FlowFEE.get(enName) instanceof FlowEventBase ? (FlowEventBase) Htable_FlowFEE.get(enName) : null;
		if (myen == null) {
			//throw new Exception("@根据类名称获取流程事件实体实例出现错误:" + enName + ",没有找到该类的实体.");
			Log.DebugWriteError("@根据类名称获取流程事件实体实例出现错误:" + enName + ",没有找到该类的实体.");
			return null;
		}
		return myen;
	}

	/**
	 * 获得事件实体String，根据编号或者流程标记
	 * <p>
	 * param flowMark 流程标记
	 * param flowNo 流程编号
	 *
	 * @return null, 或者流程实体.
	 */
	public static String GetFlowEventEntityStringByFlowMark(String flowMark) {
		FlowEventBase en = GetFlowEventEntityByFlowMark(flowMark);
		if (en == null) {
			return "";
		}
		return en.getClass().getName();
	}

	/**
	 * 获得事件实体，根据编号或者流程标记.
	 * <p>
	 * param flowMark 流程标记
	 * param flowNo 流程编号
	 *
	 * @return null, 或者流程实体.
	 */
	public static FlowEventBase GetFlowEventEntityByFlowMark(String flowMark) {

		if (Htable_FlowFEE == null || Htable_FlowFEE.isEmpty()) {
			Htable_FlowFEE = new Hashtable();

			String name = "";
			name = "BP.WF.FlowEventBase";

			ArrayList<FlowEventBase> al = bp.en.ClassFactory.GetObjects("bp.wf.FlowEventBase");
			Htable_FlowFEE.clear();
			for (FlowEventBase en : al) {
				if (Htable_FlowFEE.containsKey(en.toString()) == true) {
					continue;
				}
				Htable_FlowFEE.put(en.getClass().getName(), en);
			}
		}

		for (Object key : Htable_FlowFEE.keySet()) {
			FlowEventBase fee = Htable_FlowFEE.get(key) instanceof FlowEventBase ? (FlowEventBase) Htable_FlowFEE.get(key) : null;

			String mark = "," + fee.getFlowMark() + ",";
			if (mark.contains("," + flowMark + ",") == true) {
				return fee;
			}
		}
		return null;
	}

	///#endregion 与流程事件实体相关.

	/**
	 * 执行发送工作后处理的业务逻辑
	 * 用于流程发送后事件调用.
	 * 如果处理失败，就会抛出异常.
	 */
	public static void DealBuinessAfterSendWork(String fk_flow, long workid, String doFunc, String WorkIDs) throws Exception {
		if (doFunc.equals("SetParentFlow")) {
			/* 如果需要设置子父流程信息.
			 * 应用于合并审批,当多个子流程合并审批,审批后发起一个父流程.
			 */

			GenerWorkFlow gwfParent = new GenerWorkFlow(workid);

			String[] workids = WorkIDs.split("[,]", -1);
			String okworkids = ""; //成功发送后的workids.
			GenerWorkFlow gwfSubFlow = new GenerWorkFlow();
			for (String id : workids) {
				if (DataType.IsNullOrEmpty(id)) {
					continue;
				}

				// 把数据copy到里面,让子流程也可以得到父流程的数据。
				long workidC = Long.parseLong(id);
				gwfSubFlow.setWorkID(workidC);
				gwfSubFlow.RetrieveFromDBSources();

				//设置当前流程的ID
				bp.wf.Dev2Interface.SetParentInfo(gwfSubFlow.getFK_Flow(), workidC, gwfParent.getWorkID());

				// 是否可以执行？
				if (bp.wf.Dev2Interface.Flow_IsCanDoCurrentWork(workidC, WebUser.getNo()) == true) {
					//执行向下发送.
					try {
						bp.wf.Dev2Interface.Node_SendWork(gwfSubFlow.getFK_Flow(), workidC);
						okworkids += workidC;
					} catch (RuntimeException ex) {

						///#region 如果有一个发送失败，就撤销子流程与父流程.
						//首先把主流程撤销发送.
						bp.wf.Dev2Interface.Flow_DoUnSend(fk_flow, workid);

						//把已经发送成功的子流程撤销发送.
						String[] myokwokid = okworkids.split("[,]", -1);
						for (String okwokid : myokwokid) {
							if (DataType.IsNullOrEmpty(id)) {
								continue;
							}

							// 把数据copy到里面,让子流程也可以得到父流程的数据。
							workidC = Long.parseLong(id);
							bp.wf.Dev2Interface.Flow_DoUnSend(gwfSubFlow.getFK_Flow(), gwfSubFlow.getWorkID());
						}

						///#endregion 如果有一个发送失败，就撤销子流程与父流程.
						throw new RuntimeException("@在执行子流程(" + gwfSubFlow.getTitle() + ")发送时出现如下错误:" + ex.getMessage());
					}
				}
			}
		}

	}

	///#endregion 全局的方法处理


	///#region web.config 属性.

	/**
	 * 是否admin
	 */
	public static boolean getIsAdmin() throws Exception {
		String s = SystemConfig.GetValByKey("adminers", "");
		if (DataType.IsNullOrEmpty(s)) {
			s = "admin,";
		}
		return s.contains(WebUser.getNo());
	}

	public static boolean isEnableTrackRec() throws Exception {
		String s = SystemConfig.GetValByKey("IsEnableTrackRec", "");
		if (DataType.IsNullOrEmpty(s)) {
			return false;
		}
		if (s.equals("0")) {
			return false;
		}

		return true;
	}

	/**
	 * 获取mapdata字段查询Like。
	 * <p>
	 * param flowNo 流程编号
	 * param colName 列编号
	 */
	public static String MapDataLikeKeyV1(String flowNo, String colName) {
		flowNo = String.valueOf(Integer.parseInt(flowNo));
		String len = SystemConfig.getAppCenterDBLengthStr();
		if (flowNo.length() == 1) {
			return " " + colName + " LIKE 'ND" + flowNo + "%' AND " + len + "(" + colName + ")=5";
		}
		if (flowNo.length() == 2) {
			return " " + colName + " LIKE 'ND" + flowNo + "%' AND " + len + "(" + colName + ")=6";
		}
		if (flowNo.length() == 3) {
			return " " + colName + " LIKE 'ND" + flowNo + "%' AND " + len + "(" + colName + ")=7";
		}

		return " " + colName + " LIKE 'ND" + flowNo + "%' AND " + len + "(" + colName + ")=8";
	}

	public static String MapDataLikeKey(String flowNo, String colName) {
		flowNo = String.valueOf(Integer.parseInt(flowNo));
		String len = SystemConfig.getAppCenterDBLengthStr();

		//edited by liuxc,2016-02-22,合并逻辑，原来分流程编号的位数，现在统一处理
		return " (" + colName + " LIKE 'ND" + flowNo + "%' AND " + len + "(" + colName + ")=" + ("ND".length() + flowNo.length() + 2) + ") OR (" + colName + " = 'ND" + flowNo + "Rpt' ) OR (" + colName + " LIKE 'ND" + flowNo + "__Dtl%' AND " + len + "(" + colName + ")>" + ("ND".length() + flowNo.length() + 2 + "Dtl".length()) + ")";
	}

	/**
	 * 短信时间发送从
	 * 默认从 8 点开始.
	 */
	public static int getSMSSendTimeFromHour() throws Exception {
		try {
			return SystemConfig.GetValByKeyInt("SMSSendTimeFromHour", 0);
		} catch (java.lang.Exception e) {
			return 8;
		}
	}


	/**
	 * 短信时间发送到
	 * 默认到 20 点结束.
	 */
	public static int getSMSSendTimeToHour() throws Exception {
		try {
			return SystemConfig.GetValByKeyInt("SMSSendTimeToHour", 0);
		} catch (java.lang.Exception e) {
			return 8;
		}
	}

	///#endregion webconfig属性.


	///#region 常用方法
	private static String html = "";
	private static ArrayList htmlArr = new ArrayList();
	private static String backHtml = "";
	private static long workid = 0;

	/**
	 * 模拟运行
	 * <p>
	 * param flowNo 流程编号
	 * param empNo 要执行的人员.
	 *
	 * @return 执行信息.
	 */
	public static String Simulation_RunOne(String flowNo, String empNo, String paras) throws Exception {
		backHtml = ""; //需要重新赋空值
		Hashtable ht = null;
		if (DataType.IsNullOrEmpty(paras) == false) {
			AtPara ap = new AtPara(paras);
			ht = ap.getHisHT();
		}

		Emp emp = new Emp(empNo);
		backHtml += " **** 开始使用:" + Glo.GenerUserImgSmallerHtml(emp.getUserID(), emp.getName()) + "登录模拟执行工作流程";
		bp.wf.Dev2Interface.Port_Login(empNo);

		workid = bp.wf.Dev2Interface.Node_CreateBlankWork(flowNo, ht, null, emp.getUserID(), null);
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(flowNo, workid, ht);
		backHtml += objs.ToMsgOfHtml().replace("@", "<br>@"); //记录消息.


		String[] accepters = objs.getVarAcceptersID().split("[,]", -1);


		for (String acce : accepters) {
			if (DataType.IsNullOrEmpty(acce) == true) {
				continue;
			}

			// 执行发送.
			Simulation_Run_S1(flowNo, workid, acce, ht, empNo);
			break;
		}
		//return html;
		//return htmlArr;
		return backHtml;
	}

	private static boolean isAdd = true;

	private static void Simulation_Run_S1(String flowNo, long workid, String empNo, Hashtable ht, String beginEmp) throws Exception {
		//htmlArr.Add(html);
		Emp emp = new Emp(empNo);
		//html = "";
		backHtml += "empNo" + beginEmp;
		backHtml += "<br> **** 让:" + Glo.GenerUserImgSmallerHtml(emp.getUserID(), emp.getName()) + "执行模拟登录. ";
		// 让其登录.
		bp.wf.Dev2Interface.Port_Login(empNo);

		//执行发送.
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(flowNo, workid, ht);
		backHtml += "<br>" + objs.ToMsgOfHtml().replace("@", "<br>@");

		if (objs.getVarAcceptersID() == null) {
			isAdd = false;
			backHtml += " <br> **** 流程结束,查看<a href='/WF/WFRpt.htm?WorkID=" + workid + "&FK_Flow=" + flowNo + "' target=_blank >流程轨迹</a> ====";
			//htmlArr.Add(html);
			//backHtml += "nextEmpNo";
			return;
		}

		if (DataType.IsNullOrEmpty(objs.getVarAcceptersID())) //此处添加为空判断，跳过下面方法的执行，否则出错。
		{
			return;
		}
		String[] accepters = objs.getVarAcceptersID().split("[,]", -1);

		for (String acce : accepters) {
			if (DataType.IsNullOrEmpty(acce) == true) {
				continue;
			}

			//执行发送.
			Simulation_Run_S1(flowNo, workid, acce, ht, beginEmp);
			break; //就不让其执行了.
		}
	}

	/**
	 * 是否手机访问?
	 *
	 * @return
	 */
	public static boolean IsMobile() throws Exception {
		if (SystemConfig.getIsBSsystem() == false) {
			return false;
		}
		String requestHeader = bp.sys.Glo.getRequest().getHeader("User-Agent");
		String[] deviceArray = new String[]{"android", "mac os", "windows phone"};
		if (requestHeader == null)
			return false;
		requestHeader = requestHeader.toLowerCase();
		for (int i = 0; i < deviceArray.length; i++) {
			if (requestHeader.indexOf(deviceArray[i]) > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 加入track
	 * <p>
	 * param at 事件类型
	 * param flowNo 流程编号
	 * param workID 工作ID
	 * param fid 流程ID
	 * param fromNodeID 从节点编号
	 * param fromNodeName 从节点名称
	 * param fromEmpID 从人员ID
	 * param fromEmpName 从人员名称
	 * param toNodeID 到节点编号
	 * param toNodeName 到节点名称
	 * param toEmpID 到人员ID
	 * param toEmpName 到人员名称
	 * param note 消息
	 * param tag 参数用@分开
	 */
	public static String AddToTrack(ActionType at, String flowNo, long workID, long fid, int fromNodeID, String fromNodeName, String fromEmpID, String fromEmpName, int toNodeID, String toNodeName, String toEmpID, String toEmpName, String note, String tag) throws Exception {
		if (toNodeID == 0) {
			toNodeID = fromNodeID;
			toNodeName = fromNodeName;
		}

		Track t = new Track();
		t.setWorkID(workID);
		t.setFID(fid);
		t.setRDT(DataType.getCurrentDateTimess());
		t.setHisActionType(at);

		t.setNDFrom(fromNodeID);
		t.setNDFromT(fromNodeName);

		t.setEmpFrom(fromEmpID);
		t.setEmpFromT(fromEmpName);
		t.FK_Flow = flowNo;

		t.setNDTo(toNodeID);
		t.setNDToT(toNodeName);

		t.setEmpTo(toEmpID);
		t.setEmpToT(toEmpName);
		t.setMsg(note);

		String[] empNos = toEmpID.split("[,]", -1);


		t.setMsg(note);

		//参数.
		if (tag != null) {
			t.setTag(tag);
		}

		try {
			t.Insert();
		} catch (java.lang.Exception e) {
			t.CheckPhysicsTable();
			t.Insert();
		}
		return t.getMyPK();
	}

	/**
	 * 计算表达式是否通过(或者是否正确.)
	 * <p>
	 * param exp 表达式
	 * param en 实体
	 *
	 * @return true/false
	 */
	public static boolean ExeExp(String exp, Entity en) throws Exception {
		exp = exp.replace("@WebUser.No", WebUser.getNo());
		exp = exp.replace("@WebUser.Name", WebUser.getName());
		exp = exp.replace("@WebUser.FK_DeptNameOfFull", WebUser.getFK_DeptNameOfFull());
		exp = exp.replace("@WebUser.FK_DeptName", WebUser.getFK_DeptName());
		exp = exp.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());

		exp = exp.replace("@RDT", DataType.getCurrentDate());
		exp = exp.replace("@DateTime", DataType.getCurrentDateTime());


		String[] strs = exp.split("[ ]", -1);
		boolean isPass = false;

		String key = strs[0].trim();
		String oper = strs[1].trim();
		String val = strs[2].trim();
		val = val.replace("'", "");
		val = val.replace("%", "");
		val = val.replace("~", "");
		Row row = en.getRow();
		for (String item : row.keySet()) {
			if (!item.trim().equals(key)) {
				continue;
			}

			String valPara = row.get(key).toString();
			if (oper.equals("=")) {
				if (val.equals(valPara)) {
					return true;
				}
			}

			if (oper.toUpperCase().equals("LIKE")) {
				if (valPara.contains(val)) {
					return true;
				}
			}

			if (oper.equals(">")) {
				if (Float.parseFloat(valPara) > Float.parseFloat(val)) {
					return true;
				}
			}
			if (oper.equals(">=")) {
				if (Float.parseFloat(valPara) >= Float.parseFloat(val)) {
					return true;
				}
			}
			if (oper.equals("<")) {
				if (Float.parseFloat(valPara) < Float.parseFloat(val)) {
					return true;
				}
			}
			if (oper.equals("<=")) {
				if (Float.parseFloat(valPara) <= Float.parseFloat(val)) {
					return true;
				}
			}

			if (oper.equals("!=")) {
				if (Float.parseFloat(valPara) != Float.parseFloat(val)) {
					return true;
				}
			}

			throw new RuntimeException("@参数格式错误:" + exp + " Key=" + key + " oper=" + oper + " Val=" + val);
		}

		return false;
	}

	/**
	 * 前置导航导入表单数据
	 * <p>
	 * param WorkID
	 * param FK_Flow
	 * param FK_Node
	 * param sKey 选中的No
	 */
	public static DataTable StartGuidEnties(long WorkID, String FK_Flow, int FK_Node, String sKey) throws Exception {
		Flow fl = new Flow(FK_Flow);
		switch (fl.getStartGuideWay()) {
			case SubFlowGuide:
			case BySQLOne:
				String sql = "";
				Object tempVar = fl.getStartGuidePara3();
				sql = tempVar instanceof String ? (String) tempVar : null; //@李国文.
				if (DataType.IsNullOrEmpty(sql) == false) {
					sql = sql.replace("@Key", sKey);
					sql = sql.replace("@key", sKey);
					sql = sql.replace("~", "'");
				} else {
					Object tempVar2 = fl.getStartGuidePara2();
					sql = tempVar2 instanceof String ? (String) tempVar2 : null;
				}

				//sql = " SELECT * FROM (" + sql + ") T WHERE T.NO='" + sKey + "' ";

				//替换变量
				sql = sql.replace("@WebUser.No", WebUser.getNo());
				sql = sql.replace("@WebUser.Name", WebUser.getName());
				sql = sql.replace("@WebUser.FK_DeptName", WebUser.getFK_DeptName());
				sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());


				DataTable dt = DBAccess.RunSQLReturnTable(sql);
				if (dt.Rows.size() == 0) {
					throw new RuntimeException("err@没有找到那一行数据." + sql);
				}

				Hashtable ht = new Hashtable();
				//转换成ht表
				DataRow row = dt.Rows.get(0);
				for (int i = 0; i < row.table.Columns.size(); i++) {
					switch (row.table.Columns.get(i).ColumnName.toLowerCase()) {
						//去除关键字
						case "no":
						case "name":
						case "workid":
						case "fk_flow":
						case "fk_node":
						case "fid":
						case "oid":
						case "mypk":
						case "title":
						case "pworkid":
							break;
						default:
							ht.put(row.table.Columns.get(i).ColumnName, row.getValue(i));
							break;
					}
				}
				//保存
				bp.wf.Dev2Interface.Node_SaveWork(FK_Flow, FK_Node, WorkID, ht);
				return dt;
			case SubFlowGuideEntity:
			case BySystemUrlOneEntity:
				break;
			default:
				break;
		}
		return null;
	}

	/**
	 * 执行PageLoad装载数据
	 * <p>
	 * param item
	 * param en
	 * param mattrs
	 * param dtls
	 *
	 * @return
	 */

	public static Entity DealPageLoadFull(Entity en, MapExt item, MapAttrs mattrs, MapDtls dtls, boolean isSelf, int nodeID) throws Exception {
		return DealPageLoadFull(en, item, mattrs, dtls, isSelf, nodeID, 0);
	}

	public static Entity DealPageLoadFull(Entity en, MapExt item, MapAttrs mattrs, MapDtls dtls, boolean isSelf) throws Exception {
		return DealPageLoadFull(en, item, mattrs, dtls, isSelf, 0, 0);
	}

	public static Entity DealPageLoadFull(Entity en, MapExt item, MapAttrs mattrs, MapDtls dtls) throws Exception {
		return DealPageLoadFull(en, item, mattrs, dtls, false, 0, 0);
	}

	//ORIGINAL LINE: public static Entity DealPageLoadFull(Entity en, MapExt item, MapAttrs mattrs, MapDtls dtls, bool isSelf = false, int nodeID = 0, long workID = 0)
	public static Entity DealPageLoadFull(Entity en, MapExt item, MapAttrs mattrs, MapDtls dtls, boolean isSelf, int nodeID, long workID) throws Exception {
		if (item == null) {
			return en;
		}

		DataTable dt = null;
		String sql = item.getDoc();
		/* 如果有填充主表的sql  */
		sql = Glo.DealExp(sql, en, null);
		String fk_dbSrc = item.getFK_DBSrc();
		//填充方式，0=sql，1=url,2=CCFromRef.js , 3=webapi
		int doWay = item.getDoWay();

		SFDBSrc sfdb = null;
		//如果是sql方式填充
		if (doWay == 0) {
			if (DataType.IsNullOrEmpty(fk_dbSrc) == false && fk_dbSrc.equals("local") == false) {
				sfdb = new SFDBSrc(fk_dbSrc);
			}
			if (DataType.IsNullOrEmpty(sql) == false) {
				int num = sql.toUpperCase().split("WHERE").length - 1;
				if (num == 1) {
					String sqlext = sql.substring(0, sql.toUpperCase().indexOf("WHERE"));
					sqlext = sql.substring(sqlext.length() + 1);
					if (sqlext.contains("@"))
						throw new RuntimeException("设置的sql有错误可能有没有替换的变量:" + sql);
				}
				if (num > 1 && sql.contains("@")) {
					throw new RuntimeException("设置的sql有错误可能有没有替换的变量:" + sql);
				}
				if (sfdb != null) {
					dt = sfdb.RunSQLReturnTable(sql);
				} else {
					dt = DBAccess.RunSQLReturnTable(sql);
				}

				Attrs attrs = en.getEnMap().getAttrs();

				if (dt.Rows.size() == 1) {
					DataRow dr = dt.Rows.get(0);
					for (DataColumn dc : dt.Columns) {
						//去掉一些不需要copy的字段.
						switch (dc.ColumnName) {
							case WorkAttr.OID:
							case WorkAttr.FID:
							case WorkAttr.Rec:
							case WorkAttr.MD5:
							case GERptAttr.FlowEnder:
							case GERptAttr.FlowEnderRDT:
							case GERptAttr.AtPara:
							case GERptAttr.PFlowNo:
							case GERptAttr.PWorkID:
							case GERptAttr.PNodeID:
							case GERptAttr.BillNo:
							case GERptAttr.FlowDaySpan:
							case "RefPK":
							case WorkAttr.RecText:
								continue;
							default:
								break;
						}

						//如果不包含数据库.
						if (attrs.contains(dc.ColumnName) == false) {
							continue;
						}

						//开始赋值.
						if (DataType.IsNullOrEmpty(en.GetValStringByKey(dc.ColumnName)) || en.GetValStringByKey(dc.ColumnName).equals("0") || en.GetValStringByKey(dc.ColumnName).contains("0.0")) {
							en.SetValByKey(dc.ColumnName, dr.getValue(dc.ColumnName).toString());
							continue;
						}

						//获取attr
						Entity entity = mattrs.GetEntityByKey("KeyOfEn", dc.ColumnName);
						if (entity != null) {
							MapAttr attr = (MapAttr) entity;
							if (attr.getLGType() == FieldTypeS.Enum && en.GetValStringByKey(dc.ColumnName).equals("-1")) {
								en.SetValByKey(dc.ColumnName, dr.getValue(dc.ColumnName).toString());
								continue;
							}
							continue;
						}

					}
				}

			}
		}
		//如果是webapi方式填充
		else if (doWay == 3) {
			//请求地址
			String apiUrl = sql;
			//设置请求头
			Hashtable headerMap = new Hashtable();
			//设置token
			String token = "";
			//如果对接系统的token
			if (!DataType.IsNullOrEmpty(WebUser.getToken())) {
				token = WebUser.getToken();
			} else {
				token = WebUser.getSID();
			}

			//设置返回值格式
			headerMap.put("Content-Type", "application/json");
			//设置token，用于接口校验
			headerMap.put("Authorization", token);

			try {
				String postData = HttpClientUtil.doPost(apiUrl, "", headerMap);
				JSONObject j = JSONObject.fromObject(postData);
				if (!j.get("code").toString().equals("200"))
					return en;
				//获取返回的数据
				String jData = j.get("data").toString();
				JSONObject jd = JSONObject.fromObject(jData);
				if (jd.size() == 0)
					return en;
				//获取主表数据
				String mainTable = jd.get("mainTable").toString();
				dt = Json.ToDataTable(mainTable);

				//获取全部附件数据
				JSONArray athsJSON = jd.getJSONArray("aths");
				for (int i = 0; i < athsJSON.size(); i++) {
					//获取附件
					JSONObject athDatas = (JSONObject) athsJSON.get(i);
					//获取附件组件ID
					String FK_FrmAttachment = athDatas.get("attachmentid").toString();
					//获取附件数据
					JSONArray athArryData = athDatas.getJSONArray("attachmentdbs");
					//填充附件数据
					for (int k = 0; k < athArryData.size(); k++) {
						JSONObject athData = (JSONObject) athArryData.get(i);
						String guid = DBAccess.GenerGUID();
						FrmAttachment attachment = new FrmAttachment(FK_FrmAttachment);
						FrmAttachmentDB attachmentDB = new FrmAttachmentDB();
						attachmentDB.setMyPK(guid);
						attachmentDB.setFK_FrmAttachment(FK_FrmAttachment);
						attachmentDB.setRefPKVal(String.valueOf(workID));
						attachmentDB.setFK_MapData(attachment.getFK_MapData());
						attachmentDB.setFID(0);
						attachmentDB.setRec(athData.get("rec").toString());
						attachmentDB.setFileFullName(athData.get("fileFullName").toString());
						attachmentDB.setFileName(athData.get("fileName").toString());
						attachmentDB.setFileExts(athData.get("fileExts").toString());
						attachmentDB.setSort(athData.get("sort").toString());
						attachmentDB.setFK_Dept(athData.get("fk_dept").toString());
						attachmentDB.setFK_DeptName(athData.get("fk_deptName").toString());
						attachmentDB.setRecName(athData.get("recName").toString());
						attachmentDB.setRDT(athData.get("rdt").toString());
						attachment.Insert();
					}
				}

				//获取从表数据
				JSONArray dtlJSON = jd.getJSONArray("dtls");
				for (int i = 0; i < dtlJSON.size(); i++) {
					JSONObject dtlDatas = (JSONObject) dtlJSON.get(i);
					//从表编号
					String dtlNo = dtlDatas.get("dtlNo").toString();
					//定义map
					MapDtl dtl = new MapDtl(dtlNo);
					//插入之前判断
					GEDtls gedtls = null;
					try {
						gedtls = new GEDtls(dtl.getNo());
						if (dtl.getDtlOpenType() == DtlOpenType.ForFID) {
							if (gedtls.RetrieveByAttr(GEDtlAttr.RefPK, workID) > 0) {
								continue;
							}
						} else {
							if (gedtls.RetrieveByAttr(GEDtlAttr.RefPK, en.getPKVal()) > 0) {
								gedtls.Delete(GEDtlAttr.RefPK, en.getPKVal());
							}
						}
					} catch (RuntimeException ex) {
						(gedtls.getGetNewEntity() instanceof GEDtl ? (GEDtl) gedtls.getGetNewEntity() : null).CheckPhysicsTable();
					}

					//从表数据
					JSONArray dtlArryData = dtlDatas.getJSONArray("dtl");
					for (int k = 0; k < dtlArryData.size(); k++) {

						//获取从表数据
						JSONObject dtlData = (JSONObject) dtlArryData.get(i);
						String dtlDataStr = dtlData.get("dtlData").toString();
						//从表附件
						JSONArray dtlAthData = dtlData.getJSONArray("dtlAths");
						//生成table
						DataTable dtlDt = bp.tools.Json.ToDataTable("[" + dtlDataStr + "]");

						//执行数据插入
						for (DataRow dr : dtlDt.Rows) {
							GEDtl gedtl = gedtls.getGetNewEntity() instanceof GEDtl ? (GEDtl) gedtls.getGetNewEntity() : null;
							for (DataColumn dc : dtlDt.Columns) {
								gedtl.SetValByKey(dc.ColumnName, dr.getValue(dc.ColumnName).toString());
							}

							switch (dtl.getDtlOpenType()) {
								case ForEmp: // 按人员来控制.
									gedtl.setRefPK(en.getPKVal().toString());
									gedtl.setFID(Long.parseLong(en.getPKVal().toString()));
									break;
								case ForWorkID: // 按工作ID来控制
									gedtl.setRefPK(en.getPKVal().toString());
									gedtl.setFID(Long.parseLong(en.getPKVal().toString()));
									break;
								case ForFID: // 按流程ID来控制.
									gedtl.setRefPK(String.valueOf(workID));
									gedtl.setFID(Long.parseLong(en.getPKVal().toString()));
									break;
							}
							gedtl.setRDT(DataType.getCurrentDataTime());
							gedtl.setRec(WebUser.getNo());
							gedtl.Insert();
						}
					}

				}
			} catch (RuntimeException ex) {
				throw new RuntimeException("接口请求失败,message:" + ex.getMessage().toString());
			}
		}
		if (DataType.IsNullOrEmpty(item.getTag1()) || item.getTag1().length() < 15) {
			return en;
		}

		// 填充从表.
		for (MapDtl dtl : dtls.ToJavaList()) {
			//如果有数据，就不要填充了.

			String[] sqls = item.getTag1().split("[$]", -1);
			for (String mysql : sqls) {
				if (DataType.IsNullOrEmpty(mysql)) {
					continue;
				}
				if (mysql.contains(dtl.getNo() + ":") == false) {
					continue;
				}
				if (mysql.equals(dtl.getNo() + ":") == true) {
					continue;
				}


				///#region 处理sql.
				sql = Glo.DealSQLExp(mysql.replace(dtl.getNo() + ":", "").toString(), en, null);

				///#endregion 处理sql.

				if (DataType.IsNullOrEmpty(sql)) {
					continue;
				}

				if (sql.contains("@")) {
					throw new RuntimeException("设置的sql有错误可能有没有替换的变量:" + sql);
				}

				if (isSelf == true) {
					MapDtl mdtlSln = new MapDtl();
					mdtlSln.setNo(dtl.getNo() + "_" + nodeID);
					int result = mdtlSln.RetrieveFromDBSources();
					if (result != 0) {
						dtl.setDtlOpenType(mdtlSln.getDtlOpenType());
					}
				}


				GEDtls gedtls = null;

				try {
					gedtls = new GEDtls(dtl.getNo());
					if (dtl.getDtlOpenType() == DtlOpenType.ForFID) {
						if (gedtls.RetrieveByAttr(GEDtlAttr.RefPK, workID) > 0) {
							continue;
						}
					} else {
						if (gedtls.RetrieveByAttr(GEDtlAttr.RefPK, en.getPKVal()) > 0) {
							continue;
						}
					}


					//gedtls.Delete(GEDtlAttr.RefPK, en.PKVal);
				} catch (RuntimeException ex) {
					bp.en.Entity tempVar3 = gedtls.getGetNewEntity();
					(tempVar3 instanceof GEDtl ? (GEDtl) tempVar3 : null).CheckPhysicsTable();
				}

				sql = sql.startsWith(dtl.getNo() + "=") ? sql.substring((dtl.getNo() + "=").length()) : sql;
				if (sfdb != null) {
					dt = sfdb.RunSQLReturnTable(sql);
				} else {
					dt = DBAccess.RunSQLReturnTable(sql);
				}

				for (DataRow dr : dt.Rows) {
					bp.en.Entity tempVar4 = gedtls.getGetNewEntity();
					GEDtl gedtl = tempVar4 instanceof GEDtl ? (GEDtl) tempVar4 : null;
					for (DataColumn dc : dt.Columns) {
						gedtl.SetValByKey(dc.ColumnName, dr.getValue(dc.ColumnName).toString());
					}

					switch (dtl.getDtlOpenType()) {
						case ForEmp: // 按人员来控制.
							gedtl.setRefPK(en.getPKVal().toString());
							gedtl.setFID(Long.parseLong(en.getPKVal().toString()));
							break;
						case ForWorkID: // 按工作ID来控制
							gedtl.setRefPK(en.getPKVal().toString());
							gedtl.setFID(Long.parseLong(en.getPKVal().toString()));
							break;
						case ForFID: // 按流程ID来控制.
							gedtl.setRefPK(String.valueOf(workID));
							gedtl.setFID(Long.parseLong(en.getPKVal().toString()));
							break;
					}
					gedtl.setRDT(DataType.getCurrentDateTime());
					gedtl.setRec(WebUser.getNo());
					gedtl.Insert();
				}
			}
		}
		return en;
	}

	/**
	 * SQL表达式是否正确
	 * <p>
	 * param sqlExp
	 * param ht
	 *
	 * @return
	 */
	public static boolean CondExpSQL(String sqlExp, Hashtable ht, long myWorkID) throws Exception {
		String sql = sqlExp;
		sql = sql.replace("~", "'");
		sql = sql.replace("@WebUser.No", WebUser.getNo());
		sql = sql.replace("@WebUser.Name", WebUser.getName());
		sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());

		for (Object key : ht.keySet()) {
			if (key.equals("OID")) {
				sql = sql.replace("@WorkID", ht.get("OID").toString());
				sql = sql.replace("@OID", ht.get("OID").toString());
				continue;
			}
			sql = sql.replace("@" + key, ht.get(key).toString());
		}

		//从工作流参数里面替换
		if (sql.contains("@") == true && myWorkID != 0) {
			GenerWorkFlow gwf = new GenerWorkFlow(myWorkID);
			AtPara ap = gwf.getatPara();
			for (String str : ap.getHisHT().keySet()) {
				sql = sql.replace("@" + str, ap.GetValStrByKey(str));
			}
		}

		int result = DBAccess.RunSQLReturnValInt(sql, -1);
		if (result <= 0) {
			return false;
		}
		return true;
	}

	/**
	 * 判断表达式是否成立
	 * <p>
	 * param exp 表达式
	 * param en 变量
	 *
	 * @return 是否成立
	 */
	public static boolean CondExpPara(String exp, Hashtable ht, long myWorkID) throws Exception {
		try {
			String[] strs = exp.trim().split("[ ]", -1);

			String key = strs[0].trim();
			String oper = strs[1].trim();
			String val = strs[2].trim();

			val = val.replace("'", "");
			val = val.replace("%", "");
			val = val.replace("~", "");

			String valPara = null;
			if (ht.containsKey(key) == false) {

				boolean isHave = false;
				if (myWorkID != 0) {
					//把外部传来的参数传入到 rptGE 让其做方向条件的判断.
					GenerWorkFlow gwf = new GenerWorkFlow(myWorkID);
					AtPara at = gwf.getatPara();
					for (String str : at.getHisHT().keySet()) {
						if (key.equals(str) == false) {
							continue;
						}

						valPara = at.GetValStrByKey(key);
						isHave = true;
						break;
					}
				}

				if (isHave == false) {
					try {
						if (SystemConfig.getIsBSsystem() == true && CommonUtils.getRequest().getParameterMap().keySet().contains(key) == true) {
							valPara = ContextHolderUtils.getRequest().getParameter(key);
						} else {
							throw new RuntimeException("@判断条件时错误,请确认参数是否拼写错误,没有找到对应的表达式:" + exp + " Key=(" + key + ") oper=(" + oper + ")Val=(" + val + ")");
						}
					} catch (java.lang.Exception e) {
						//有可能是常量. 
						valPara = key;
					}
				}
			} else {
				valPara = ht.get(key).toString().trim();
			}


			///#region 开始执行判断.
			if (oper.equals("=")) {
				if (val.equals(valPara)) {
					return true;
				} else {
					return false;
				}
			}

			if (oper.toUpperCase().equals("LIKE")) {
				if (valPara.contains(val)) {
					return true;
				} else {
					return false;
				}
			}
			if (oper.equals("!=")) {
				if (!val.equals(valPara)) {
					return true;
				} else {
					return false;
				}
			}


			if (DataType.IsNumStr(valPara) == false) {
				throw new RuntimeException("err@表达式错误:[" + exp + "]没有找到参数[" + valPara + "]的值，导致无法计算。");
			}

			if (oper.equals(">")) {
				if (Float.parseFloat(valPara) > Float.parseFloat(val)) {
					return true;
				} else {
					return false;
				}
			}
			if (oper.equals(">=")) {
				if (Float.parseFloat(valPara) >= Float.parseFloat(val)) {
					return true;
				} else {
					return false;
				}
			}
			if (oper.equals("<")) {
				if (Float.parseFloat(valPara) < Float.parseFloat(val)) {
					return true;
				} else {
					return false;
				}
			}
			if (oper.equals("<=")) {
				if (Float.parseFloat(valPara) <= Float.parseFloat(val)) {
					return true;
				} else {
					return false;
				}
			}

			if (oper.equals("!=")) {
				if (Float.parseFloat(valPara) != Float.parseFloat(val)) {
					return true;
				} else {
					return false;
				}
			}
			throw new RuntimeException("@参数格式错误:" + exp + " Key=" + key + " oper=" + oper + " Val=" + val);

			///#endregion 开始执行判断.

		} catch (RuntimeException ex) {
			throw new RuntimeException("计算参数的时候出现错误:" + ex.getMessage());
		}
	}

	/**
	 * 表达式替换
	 * <p>
	 * param exp
	 * param en
	 *
	 * @return
	 */

	public static String DealExp(String exp, Entity en) throws Exception {
		return DealExp(exp, en, "");
	}

	//ORIGINAL LINE: public static string DealExp(string exp, Entity en, string errInfo = "")
	public static String DealExp(String exp, Entity en, String errInfo) throws Exception {
		//替换字符
		exp = exp.replace("~", "'");

		if (exp.contains("@") == false) {
			return exp;
		}

		//首先替换加; 的。
		exp = exp.replace("@WebUser.No;", WebUser.getNo());
		exp = exp.replace("@WebUser.Name;", WebUser.getName());
		exp = exp.replace("@WebUser.FK_DeptName;", WebUser.getFK_DeptName());
		exp = exp.replace("@WebUser.FK_Dept;", WebUser.getFK_Dept());

		// 替换没有 ; 的 .
		exp = exp.replace("@WebUser.No", WebUser.getNo());
		exp = exp.replace("@WebUser.Name", WebUser.getName());
		exp = exp.replace("@WebUser.FK_DeptName", WebUser.getFK_DeptName());
		exp = exp.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
		exp = exp.replace("@WebUser.OrgNo", WebUser.getOrgNo());

		exp = exp.replace("@RDT", DataType.getCurrentDateTime());

		if (exp.contains("@") == false) {
			return exp;
		}

		//增加对新规则的支持. @MyField; 格式.
		if (en != null) {
			Attrs attrs = en.getEnMap().getAttrs();
			Row row = en.getRow();
			//特殊判断.
			if (row.containsKey("OID") == true) {
				exp = exp.replace("@WorkID", row.get("OID").toString());
			}

			if (exp.contains("@") == false) {
				return exp;
			}


			for (Object key : row.keySet()) {
				//值为空或者null不替换
				if (row.get(key) == null) {
					continue;
				}
				if (exp.contains("@" + key + ";")) {
					//先替换有单引号的.
					exp = exp.replace("'@" + key + ";'", "'" + row.get(key).toString() + "'");
					//在更新没有单引号的.
					exp = exp.replace("@" + key + ";", row.get(key).toString());
				}
				if (exp.contains("@" + key)) {
					//先替换有单引号的.
					exp = exp.replace("'@" + key + "'", "'" + row.get(key).toString() + "'");
					//在更新没有单引号的.
					exp = exp.replace("@" + key, row.get(key).toString());
				}
				//不包含@则返回SQL语句
				if (exp.contains("@") == false) {
					return exp;
				}
			}
		}

		if (exp.contains("@") && SystemConfig.getIsBSsystem() == true) {
			/*如果是bs*/
			for (Object key : CommonUtils.getRequest().getParameterMap().keySet()) {
				if (DataType.IsNullOrEmpty(key)) {
					continue;
				}
				exp = exp.replace("@" + key, ContextHolderUtils.getRequest().getParameter(key.toString()));
			}

		}

		exp = exp.replace("~", "'");
		return exp;
	}
	//

	/**
	 * 处理表达式
	 * <p>
	 * param exp 表达式
	 * param en 数据源
	 * param errInfo 错误
	 *
	 * @return
	 */
	public static String DealSQLExp(String exp, Entity en, String errInfo) throws Exception {
		//替换字符
		exp = exp.replace("~", "'");


		String expWhere = "";

		if (exp.contains("@") == false) {
			return exp;
		}

		//首先替换加; 的。
		exp = exp.replace("@WebUser.No;", WebUser.getNo());
		exp = exp.replace("@WebUser.Name;", WebUser.getName());
		exp = exp.replace("@WebUser.FK_DeptName;", WebUser.getFK_DeptName());
		exp = exp.replace("@WebUser.FK_Dept;", WebUser.getFK_Dept());


		// 替换没有 ; 的 .
		exp = exp.replace("@WebUser.No", WebUser.getNo());
		exp = exp.replace("@WebUser.Name", WebUser.getName());
		exp = exp.replace("@WebUser.FK_DeptName", WebUser.getFK_DeptName());
		exp = exp.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());

		if (Glo.getCCBPMRunModel() != CCBPMRunModel.Single) {
			exp = exp.replace("@WebUser.OrgNo", WebUser.getOrgNo());
		}

		if (exp.contains("@") == false) {
			return exp;
		}

		//增加对新规则的支持. @MyField; 格式.
		if (en != null) {
			Row row = en.getRow();
			//特殊判断.
			if (row.containsKey("OID") == true) {
				exp = exp.replace("@WorkID", row.get("OID").toString());
			}

			if (exp.contains("@") == false) {
				return exp;
			}

			for (Object key : row.keySet()) {
				//值为空或者null不替换
				if (row.get(key) == null || row.get(key).equals("") == true) {
					continue;
				}

				if (exp.contains("@" + key + ";")) {
					exp = exp.replace("@" + key + ";", row.get(key).toString());
				}

				//不包含@则返回SQL语句
				if (exp.contains("@") == false) {
					return exp;
				}
			}


			///#region 解决排序问题.
			Attrs attrs = en.getEnMap().getAttrs();
			String mystrs = "";
			for (Attr attr : attrs)
			{
				if (attr.getMyDataType() == DataType.AppString)
				{
					mystrs += "@" + attr.getKey() + ",";
				}
				else
				{
					mystrs += "@" + attr.getKey();
				}
			}
			String[] strs = mystrs.split("[@]", -1);
			DataTable dt = new DataTable();
			dt.Columns.Add(new DataColumn("No", String.class));
			for (String str : strs)
			{
				if (DataType.IsNullOrEmpty(str))
				{
					continue;
				}

				DataRow dr = dt.NewRow();
				dr.setValue(0, str);
				dt.Rows.add(dr);
			}

			///#region 替换变量.
			for (DataRow dr : dt.Rows)
			{
				String key = dr.getValue(0).toString();
				boolean isStr = key.contains(",");
				if (isStr == true)
				{
					key = key.replace(",", "");
				}

				if (DataType.IsNullOrEmpty(en.GetValStrByKey(key)))
				{
					continue;
				}

				exp = exp.replace("@" + key, en.GetValStrByKey(key));
			}

			///#endregion

			if (exp.contains("@") == false)
			{
				return exp;
			}
		}


		// 处理Para的替换.
		if (exp.contains("@") && Glo.getSendHTOfTemp() != null) {
			for (Object key : Glo.getSendHTOfTemp().keySet()) {
				exp = exp.replace("@" + key, Glo.getSendHTOfTemp().get(key).toString());
			}
		}


		if (exp.contains("@") && SystemConfig.getIsBSsystem() == true) {
			/*如果是bs*/
			for (Object key : CommonUtils.getRequest().getParameterMap().keySet()) {
				if (DataType.IsNullOrEmpty(key)) {
					continue;
				}
				exp = exp.replace("@" + key, ContextHolderUtils.getRequest().getParameter(key.toString()));
			}
			/*如果是bs*/
			//foreach (string key in System.Web.HttpContext.Current.Request.Form.AllKeys)
			//{
			//    if (string.IsNullOrEmpty(key))
			//        continue;
			//    exp = exp.Replace("@" + key, System.Web.HttpContext.Current.Request.Form[key]);
			//}

		}

		exp = exp.replace("~", "'");
		exp = exp.replace("\r", "");
		exp = exp.replace("\n", "");
        return exp;
	}
	/** 
	 加密MD5
	 
	 param s
	 @return 
	*/
	public static String GenerMD5(bp.wf.Work wk) throws Exception {
		String s = null;
		for (Attr attr : wk.getEnMap().getAttrs())
		{
			switch (attr.getKey())
			{
				case WorkAttr.MD5:
				case WorkAttr.Rec:
				case GERptAttr.Title:
				// case GERptAttr.Emps:
				case GERptAttr.FK_Dept:
				//case GERptAttr.PRI:
				case GERptAttr.FID:
					continue;
				default:
					break;
			}

			Object tempVar = attr.getDefaultVal();
			String obj = tempVar instanceof String ? (String)tempVar : null;
			//if (obj == null)
			//    continue;
			if (obj != null && obj.contains("@"))
			{
				continue;
			}

			s += wk.GetValStrByKey(attr.getKey());
		}
		s += "ccflow";

		return GetMD5Hash(s);
		//return System.Web.Security.FormsAuthentication.HashPasswordForStoringInConfigFile(s, "MD5").ToLower();
	}

	/** 
	 取得MD5加密串
	 
	 param input 源明文字符串
	 @return 密文字符串
	*/
	public static String GetMD5Hash(String input)
	{
		return input;
	}

	/** 
	 装载流程数据 
	 
	 param xlsFile
	*/
	public static String LoadFlowDataWithToSpecNode(String xlsFile) throws Exception {
		DataTable dt = bp.da.DBLoad.GetTableByExt(xlsFile);
		String err = "";
		String info = "";

		for (DataRow dr : dt.Rows)
		{
			String flowPK = dr.getValue("FlowPK").toString();
			String starter = dr.getValue("Starter").toString();
			String executer = dr.getValue("Executer").toString();
			int toNode = Integer.parseInt(dr.getValue("ToNodeID").toString().replace("ND", ""));
			Node nd = new Node();
			nd.setNodeID(toNode);
			if (nd.RetrieveFromDBSources() == 0)
			{
				err += "节点ID错误:" + toNode;
				continue;
			}
			String sql = "SELECT count(*) as Num FROM ND" + Integer.parseInt(nd.getFK_Flow()) + "01 WHERE FlowPK='" + flowPK + "'";
			int i = DBAccess.RunSQLReturnValInt(sql);
			if (i == 1)
			{
				continue; // 此数据已经调度了。
			}


			///#region 检查数据是否完整。
			bp.port.Emp emp = new bp.port.Emp();
			emp.setNo(executer);
			if (emp.RetrieveFromDBSources() == 0)
			{
				err += "@账号:" + starter + ",不存在。";
				continue;
			}
			if (DataType.IsNullOrEmpty(emp.getFK_Dept()))
			{
				err += "@账号:" + starter + ",没有部门。";
				continue;
			}

			emp.setNo(starter);
			if (emp.RetrieveFromDBSources() == 0)
			{
				err += "@账号:" + executer + ",不存在。";
				continue;
			}
			if (DataType.IsNullOrEmpty(emp.getFK_Dept()))
			{
				err += "@账号:" + executer + ",没有部门。";
				continue;
			}

			///#endregion 检查数据是否完整。

			WebUser.SignInOfGener(emp);
			Flow fl = nd.getHisFlow();
			Work wk = fl.NewWork();

			Attrs attrs = wk.getEnMap().getAttrs();
			//foreach (Attr attr in wk.EnMap.getAttrs())
			//{
			//}

			for (DataColumn dc : dt.Columns)
			{
				Attr attr = attrs.GetAttrByKey(dc.ColumnName.trim());
				if (attr == null)
				{
					continue;
				}

				String val = dr.getValue(dc.ColumnName).toString().trim();
				switch (attr.getMyDataType())
				{
					case DataType.AppString:
					case DataType.AppDate:
					case DataType.AppDateTime:
						wk.SetValByKey(attr.getKey(), val);
						break;
					case DataType.AppInt:
					case DataType.AppBoolean:
						wk.SetValByKey(attr.getKey(), Integer.parseInt(val));
						break;
					case DataType.AppMoney:
					case DataType.AppDouble:
					case DataType.AppFloat:
						wk.SetValByKey(attr.getKey(), new BigDecimal(val));
						break;
					default:
						wk.SetValByKey(attr.getKey(), val);
						break;
				}
			}

			wk.SetValByKey(WorkAttr.Rec, WebUser.getNo());
			//  wk.SetValByKey(GERptAttr.FK_Dept, WebUser.getFK_Dept());
			// wk.SetValByKey("FK_NY", DataType.getCurrentYearMonth());
			wk.Update();

			Node ndStart = nd.getHisFlow().getHisStartNode();
			WorkNode wn = new WorkNode(wk, ndStart);
			try
			{
				info += "<hr>" + wn.NodeSend(nd, executer).ToMsgOfHtml();
			}
			catch (RuntimeException ex)
			{
				err += "<hr>" + ex.getMessage();
				WorkFlow wf = new WorkFlow(fl, wk.getOID());
				wf.DoDeleteWorkFlowByReal(true);
				continue;
			}


			///#region 更新 下一个节点数据。
			Work wkNext = nd.getHisWork();
			wkNext.setOID(wk.getOID());
			wkNext.RetrieveFromDBSources();
			attrs = wkNext.getEnMap().getAttrs();
			for (DataColumn dc : dt.Columns)
			{
				Attr attr = attrs.GetAttrByKey(dc.ColumnName.trim());
				if (attr == null)
				{
					continue;
				}

				String val = dr.getValue(dc.ColumnName).toString().trim();
				switch (attr.getMyDataType())
				{
					case DataType.AppString:
					case DataType.AppDate:
					case DataType.AppDateTime:
						wkNext.SetValByKey(attr.getKey(), val);
						break;
					case DataType.AppInt:
					case DataType.AppBoolean:
						wkNext.SetValByKey(attr.getKey(), Integer.parseInt(val));
						break;
					case DataType.AppMoney:
					case DataType.AppDouble:
					case DataType.AppFloat:
						wkNext.SetValByKey(attr.getKey(), new BigDecimal(val));
						break;
					default:
						wkNext.SetValByKey(attr.getKey(), val);
						break;
				}
			}

			wkNext.DirectUpdate();

			GERpt rtp = fl.getHisGERpt();
			rtp.SetValByKey("OID", wkNext.getOID());
			rtp.RetrieveFromDBSources();
			rtp.Copy(wkNext);
			rtp.DirectUpdate();


			///#endregion 更新 下一个节点数据。
		}
		return info + err;
	}
	public static String LoadFlowDataWithToSpecEndNode(String xlsFile) throws Exception {
		DataTable dt = DBLoad.ReadExcelFileToDataTable(xlsFile);
		DataSet ds = new DataSet();
		ds.Tables.add(dt);
		ds.WriteXml("C:/已完成.xml");

		String err = "";
		String info = "";
		int idx = 0;
		for (DataRow dr : dt.Rows)
		{
			String flowPK = dr.getValue("FlowPK").toString().trim();
			if (DataType.IsNullOrEmpty(flowPK))
			{
				continue;
			}

			String starter = dr.getValue("Starter").toString();
			String executer = dr.getValue("Executer").toString();
			int toNode = Integer.parseInt(dr.getValue("ToNodeID").toString().replace("ND", ""));
			Node ndOfEnd = new Node();
			ndOfEnd.setNodeID(toNode);
			if (ndOfEnd.RetrieveFromDBSources() == 0)
			{
				err += "节点ID错误:" + toNode;
				continue;
			}

			if (ndOfEnd.isEndNode() == false)
			{
				err += "节点ID错误:" + toNode + ", 非结束节点。";
				continue;
			}

			String sql = "SELECT count(*) as Num FROM ND" + Integer.parseInt(ndOfEnd.getFK_Flow()) + "01 WHERE FlowPK='" + flowPK + "'";
			int i = DBAccess.RunSQLReturnValInt(sql);
			if (i == 1)
			{
				continue; // 此数据已经调度了。
			}


				///#region 检查数据是否完整。
			//发起人发起。
			Emp emp = new Emp();
			emp.setUserID (executer);
			if (emp.RetrieveFromDBSources() == 0)
			{
				err += "@账号:" + starter + ",不存在。";
				continue;
			}

			if (DataType.IsNullOrEmpty(emp.getFK_Dept()))
			{
				err += "@账号:" + starter + ",没有设置部门。";
				continue;
			}

			emp = new Emp();
			emp.setUserID (starter);
			if (emp.RetrieveFromDBSources() == 0)
			{
				err += "@账号:" + starter + ",不存在。";
				continue;
			}
			else
			{
				emp.RetrieveFromDBSources();
				if (DataType.IsNullOrEmpty(emp.getFK_Dept()))
				{
					err += "@账号:" + starter + ",没有设置部门。";
					continue;
				}
			}

				///#endregion 检查数据是否完整。


			WebUser.SignInOfGener(emp, "CH", false, false, null, null);
			Flow fl = ndOfEnd.getHisFlow();
			Work wk = fl.NewWork();
			for (DataColumn dc : dt.Columns)
			{
				wk.SetValByKey(dc.ColumnName.trim(), dr.getValue(dc.ColumnName).toString().trim());
			}

			wk.SetValByKey(WorkAttr.Rec, WebUser.getNo());
			//wk.SetValByKey(GERptAttr.FK_Dept, bp.web.WebUser.getFK_Dept());
			//wk.SetValByKey("FK_NY", DataType.getCurrentYearMonth());
			//wk.SetValByKey(WorkAttr.MyNum, 1);
			wk.Update();

			Node ndStart = fl.getHisStartNode();
			WorkNode wn = new WorkNode(wk, ndStart);
			try
			{
				info += "<hr>" + wn.NodeSend(ndOfEnd, executer).ToMsgOfHtml();
			}
			catch (RuntimeException ex)
			{
				err += "<hr>启动错误:" + ex.getMessage();
				DBAccess.RunSQL("DELETE FROM ND" + Integer.parseInt(ndOfEnd.getFK_Flow()) + "01 WHERE FlowPK='" + flowPK + "'");
				WorkFlow wf = new WorkFlow(fl, wk.getOID());
				wf.DoDeleteWorkFlowByReal(true);
				continue;
			}

			//结束点结束。
			emp = new Emp(executer);
			WebUser.SignInOfGener(emp, "CH", false, false, null, null);

			Work wkEnd = ndOfEnd.GetWork(wk.getOID());
			for (DataColumn dc : dt.Columns)
			{
				wkEnd.SetValByKey(dc.ColumnName.trim(), dr.getValue(dc.ColumnName).toString().trim());
			}

			wkEnd.SetValByKey(WorkAttr.Rec, WebUser.getNo());
			//wkEnd.SetValByKey(GERptAttr.FK_Dept, bp.web.WebUser.getFK_Dept());
			//wkEnd.SetValByKey("FK_NY", DataType.getCurrentYearMonth());
			//wkEnd.SetValByKey(WorkAttr.MyNum, 1);
			wkEnd.Update();

			try
			{
				WorkNode wnEnd = new WorkNode(wkEnd, ndOfEnd);
				//  wnEnd.AfterNodeSave();
				info += "<hr>" + wnEnd.NodeSend().ToMsgOfHtml();
			}
			catch (RuntimeException ex)
			{
				err += "<hr>结束错误(系统直接删除它):" + ex.getMessage();
				WorkFlow wf = new WorkFlow(fl, wk.getOID());
				wf.DoDeleteWorkFlowByReal(true);
				continue;
			}
		}
		return info + err;
	}
	/** 
	 判断是否登陆当前UserNo
	 
	 param userNo
	*/
	public static void IsSingleUser(String userNo) throws Exception {
		if (DataType.IsNullOrEmpty(WebUser.getNo()) || !WebUser.getNo().equals(userNo))
		{
			if (!DataType.IsNullOrEmpty(userNo))
			{
				bp.wf.Dev2Interface.Port_Login(userNo);
			}
		}
	}
	//public static void ResetFlowView()
	//{
	//    string sql = "DROP VIEW V_WF_Data ";
	//    try
	//    {
	//        DBAccess.RunSQL(sql);
	//    }
	//    catch
	//    {
	//    }

	//    Flows fls = new Flows();
	//    fls.RetrieveAll();
	//    sql = "CREATE VIEW V_WF_Data AS ";
	//    foreach (Flow fl in fls)
	//    {
	//        fl.CheckRpt();
	//        sql += "\t\n SELECT '" + fl.No + "' as FK_Flow, '" + fl.Name + "' AS FlowName, '" + fl.FK_FlowSort + "' as FK_FlowSort,CDT,Emps,FID,FK_Dept,FK_NY,";
	//        sql += "MyNum,OID,RDT,Rec,Title,WFState,FlowEmps,";
	//        sql += "FlowStarter,FlowStartRDT,FlowEnder,FlowEnderRDT,FlowDaySpan FROM ND" + int.Parse(fl.No) + "Rpt";
	//        sql += "\t\n  UNION";
	//    }
	//    sql = sql.Substring(0, sql.Length - 6);
	//    sql += "\t\n GO";
	//    DBAccess.RunSQL(sql);
	//}
	public static void Rtf2PDF(Object pathOfRtf, Object pathOfPDF)
	{
		//        Object Nothing = System.Reflection.Missing.Value;
		//        //创建一个名为WordApp的组件对象    
		//        Microsoft.Office.Interop.Word.Application wordApp =
		//new Microsoft.Office.Interop.Word.ApplicationClass();
		//        //创建一个名为WordDoc的文档对象并打开    
		//        Microsoft.Office.Interop.Word.Document doc = wordApp.Documents.Open(ref pathOfRtf, ref Nothing, ref Nothing, ref Nothing, ref Nothing,
		// ref Nothing, ref Nothing, ref Nothing, ref Nothing, ref Nothing,
		//ref Nothing, ref Nothing, ref Nothing, ref Nothing, ref Nothing, ref Nothing);

		//        //设置保存的格式    
		//        object filefarmat = Microsoft.Office.Interop.Word.WdSaveFormat.wdFormatPDF;

		//        //保存为PDF    
		//        doc.SaveAs(ref pathOfPDF, ref filefarmat, ref Nothing, ref Nothing, ref Nothing, ref Nothing,
		//ref Nothing, ref Nothing, ref Nothing, ref Nothing, ref Nothing, ref Nothing, ref Nothing,
		// ref Nothing, ref Nothing, ref Nothing);
		//        //关闭文档对象    
		//        doc.Close(ref Nothing, ref Nothing, ref Nothing);
		//        //推出组建    
		//        wordApp.Quit(ref Nothing, ref Nothing, ref Nothing);
		//        GC.Collect();
	}

		///#endregion 常用方法


		///#region 属性
	/** 
	 消息
	*/
	public static String getSessionMsg() throws Exception {
		Paras ps = new Paras();
		ps.SQL = "SELECT Msg FROM WF_Emp where No=" + SystemConfig.getAppCenterDBVarStr() + "FK_Emp";
		ps.Add("FK_Emp", WebUser.getNo(), false);

		return DBAccess.RunSQLReturnString(ps);
	}

	public static Hashtable getSendHTOfTemp() throws Exception
	{
		if (_SendHTOfTemp == null)
		{
			_SendHTOfTemp = new Hashtable();
		}
		return _SendHTOfTemp.get(WebUser.getNo()) instanceof Hashtable ? (Hashtable)_SendHTOfTemp.get(WebUser.getNo()) : null;
	}
	public static void setSessionMsg(String value)throws Exception
	{if (DataType.IsNullOrEmpty(value) == true)
		{
			return;
		}

		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
		{
			String mypk = WebUser.getOrgNo() + "_" + WebUser.getNo();

			Paras myps = new Paras();
			myps.SQL = "UPDATE WF_Emp SET Msg=" + SystemConfig.getAppCenterDBVarStr() + "Msg WHERE No=" + SystemConfig.getAppCenterDBVarStr() + "No";
			myps.Add("Msg", value, false);
			myps.Add("No", mypk, false);
			if (DBAccess.RunSQL(myps) != 0)
			{
				return;
			}

				/*如果没有更新到.*/
			bp.wf.port.WFEmp myemp = new bp.wf.port.WFEmp();
				//myemp.setNo(mypk;

			myemp.SetValByKey("No", mypk);

			myemp.setName(WebUser.getName());
			myemp.setFK_Dept(WebUser.getFK_Dept());

				//SAAS 需要判断.
				//bp.port.Emp emp = new bp.port.Emp();
				//emp.Email = DBAccess.RunSQLReturnString("SELECT Eamil FROM Port_Emp WHERE "); // new bp.port.Emp(WebUser.getNo()).Email;
			myemp.Insert();
			DBAccess.RunSQL(myps);
			return;
		}


		Paras ps = new Paras();
		ps.SQL = "UPDATE WF_Emp SET Msg=" + SystemConfig.getAppCenterDBVarStr() + "v WHERE No=" + SystemConfig.getAppCenterDBVarStr() + "No";
		ps.Add("v", value, false);
		ps.Add("No", WebUser.getNo(), false);
		if (DBAccess.RunSQL(ps) == 1)
		{
			return;
		}

			/*如果没有更新到.*/
		bp.wf.port.WFEmp emp = new bp.wf.port.WFEmp();
		emp.setNo(WebUser.getNo());
		emp.setName(WebUser.getName());
		emp.setFK_Dept(WebUser.getFK_Dept());

			//SAAS 需要判断.
			//bp.port.Emp emp = new bp.port.Emp();
			//emp.Email = DBAccess.RunSQLReturnString("SELECT Eamil FROM Port_Emp WHERE "); // new bp.port.Emp(WebUser.getNo()).Email;
		emp.Insert();
		DBAccess.RunSQL(ps);
	}
	private static Hashtable _SendHTOfTemp = null;
	/** 
	 临时的发送传输变量.
	*/
	//public static Hashtable SendHTOfTemp
	//{
	//    //get
	//    //{
	//    //    if (_SendHTOfTemp == null)
	//    //        _SendHTOfTemp = new Hashtable();
	//    //    return _SendHTOfTemp[bp.web.WebUser.getNo()] as Hashtable;
	//    //}
	//    //set
	//    //{
	//    //    if (value == null)
	//    //        return;

	//    //    if (_SendHTOfTemp == null)
	//    //        _SendHTOfTemp = new Hashtable();
	//    //    _SendHTOfTemp[bp.web.WebUser.getNo()] = value;
	//    //}
	//}
	/** 
	 报表属性集合
	*/
	private static Attrs _AttrsOfRpt = null;
	/** 
	 报表属性集合
	*/
	public static Attrs getAttrsOfRpt() throws Exception {
		if (_AttrsOfRpt == null)
		{
			_AttrsOfRpt = new Attrs();
			_AttrsOfRpt.AddTBInt(GERptAttr.OID, 0, "WorkID", true, true);
			_AttrsOfRpt.AddTBInt(GERptAttr.FID, 0, "FlowID", false, false);

			_AttrsOfRpt.AddTBString(GERptAttr.Title, null, "标题", true, false, 0, 10, 10);
			_AttrsOfRpt.AddTBString(GERptAttr.FlowStarter, null, "发起人", true, false, 0, 10, 10);
			_AttrsOfRpt.AddTBString(GERptAttr.FlowStartRDT, null, "发起时间", true, false, 0, 10, 10);
			_AttrsOfRpt.AddTBString(GERptAttr.WFState, null, "状态", true, false, 0, 10, 10);

				//Attr attr = new Attr();
				//attr.Desc = "流程状态";
				//attr.getKey() = "WFState";
				//attr.setMyFieldType (FieldType.Enum;
				//attr.setUIBindKey("WFState";
				//attr.UITag = "@0=进行中@1=已经完成";

			_AttrsOfRpt.AddDDLSysEnum(GERptAttr.WFState, 0, "流程状态", true, true, GERptAttr.WFState);
			_AttrsOfRpt.AddTBString(GERptAttr.FlowEmps, null, "参与人", true, false, 0, 10, 10);
			_AttrsOfRpt.AddTBString(GERptAttr.FlowEnder, null, "结束人", true, false, 0, 10, 10);
			_AttrsOfRpt.AddTBString(GERptAttr.FlowEnderRDT, null, "最后处理时间", true, false, 0, 10, 10);
			_AttrsOfRpt.AddTBInt(GERptAttr.FlowEndNode, 0, "停留节点", true, false);
			_AttrsOfRpt.AddTBDecimal(GERptAttr.FlowDaySpan, BigDecimal.valueOf(0), "流程时长(天)", true, false);
				//_AttrsOfRpt.AddTBString(GERptAttr.FK_NY, null, "隶属月份", true, false, 0, 10, 10);
		}
		return _AttrsOfRpt;
	}

		///#endregion 属性


		///#region 其他配置.

	/** 
	 帮助
	 
	 param id1
	 param id2
	 @return 
	*/
	public static String GenerHelpCCForm(String text, String id1, String id2)
	{
		if (id1 == null)
		{
			return "<div style='float:right' ><a href='http://ccform.mydoc.io' target=_blank><img src='/WF/Img/Help.gif'>" + text + "</a></div>";
		}
		else
		{
			return "<div style='float:right' ><a href='" + id1 + "' target=_blank><img src='/WF/Img/Help.gif'>" + text + "</a></div>";
		}
	}
	public static String GenerHelpCCFlow(String text, String id1, String id2)
	{
		return "<div style='float:right' ><a href='" + id1 + "' target=_blank><img src='/WF/Img/Help.gif'>" + text + "</a></div>";
	}
	public static String getNodeImagePath() throws Exception {
		return Glo.getIntallPath() + "Data/Node/";
	}


	public static void ClearDBData() throws Exception {
		String sql = "DELETE FROM WF_GenerWorkFlow WHERE fk_flow not in (select no from wf_flow )";
		DBAccess.RunSQL(sql);

		sql = "DELETE FROM WF_GenerWorkerlist WHERE fk_flow not in (select no from wf_flow )";
		DBAccess.RunSQL(sql);
	}
	public static String OEM_Flag = "CCS";
	public static String getFlowFileBill() throws Exception {
		return Glo.getIntallPath() + "DataUser/Bill/";
	}
	private static String _IntallPath = null;
	public static String getIntallPath() throws Exception {
		if (_IntallPath == null)
		{
			if (SystemConfig.getIsBSsystem() == true)
			{
				_IntallPath = SystemConfig.getPathOfWebApp();
			}
		}

		if (_IntallPath == null)
		{
			throw new RuntimeException("@没有实现如何获得 cs 下的根目录.");
		}

		return _IntallPath;
	}
	public static void setIntallPath(String value)throws Exception
	{_IntallPath = value;
	}
	private static String _ServerIP = null;
	public static String getServerIP() throws Exception {
		if (_ServerIP == null) {

			String ip = getHostURL();
		}
		return _ServerIP;
	}
	public static void setServerIP(String value)throws Exception
	{_ServerIP = value;
	}
	/** 
	 全局的安全验证码
	*/
	public static String getGloSID() throws Exception {
		String s = SystemConfig.getAppSettings().get("GloSID") instanceof String ? (String)SystemConfig.getAppSettings().get("GloSID") : null;
		if (DataType.IsNullOrEmpty(s))
		{
			s = "sdfq2erre-2342-234sdf23423-323";
		}
		return s;
	}
	/** 
	 是否启用检查用户的状态?
	 如果启用了:在MyFlow.htm中每次都会检查当前的用户状态是否被禁
	 用，如果禁用了就不能执行任何操作了。启用后，就意味着每次都要
	 访问数据库。
	*/
	public static boolean isEnableCheckUseSta() throws Exception {
		String s = SystemConfig.getAppSettings().get("IsEnableCheckUseSta") instanceof String ? (String)SystemConfig.getAppSettings().get("IsEnableCheckUseSta") : null;
		if (s == null || s.equals("0"))
		{
			return false;
		}
		return true;
	}
	/** 
	 是否启用显示节点名称
	*/
	public static boolean isEnableMyNodeName() throws Exception {
		String s = SystemConfig.getAppSettings().get("IsEnableMyNodeName") instanceof String ? (String)SystemConfig.getAppSettings().get("IsEnableMyNodeName") : null;
		if (s == null || s.equals("0"))
		{
			return false;
		}
		return true;
	}
	/** 
	 检查一下当前的用户是否仍旧有效使用？
	 
	 @return 
	*/
	public static boolean CheckIsEnableWFEmp() throws Exception {
		Paras ps = new Paras();
		ps.SQL = "SELECT UseSta FROM WF_Emp WHERE No=" + SystemConfig.getAppCenterDBVarStr() + "FK_Emp";
		ps.Add("FK_Emp", WebUser.getNo(), false);

		String s = DBAccess.RunSQLReturnStringIsNull(ps, "1");
		if (s == null || s.equals("1") == true)
		{
			return true;
		}
		return false;
	}
	/** 
	 语言
	*/
	public static String Language = "CH";
	public static boolean isQL() throws Exception {
		String s = SystemConfig.getAppSettings().get("IsQL").toString();
		if (s == null || s.equals("0"))
		{
			return false;
		}
		return true;
	}
	/** 
	 是否启用共享任务池？
	*/
	public static boolean isEnableTaskPool() throws Exception {
		return SystemConfig.GetValByKeyBoolen("IsEnableTaskPool", false);
	}
	/** 
	 是否显示标题
	*/
	public static boolean isShowTitle() throws Exception {
		return SystemConfig.GetValByKeyBoolen("IsShowTitle", false);
	}

	/** 
	 用户信息显示格式
	*/
	public static UserInfoShowModel getUserInfoShowModel() throws Exception {
		return UserInfoShowModel.forValue(SystemConfig.GetValByKeyInt("UserInfoShowModel", 0));
	}
	/** 
	 产生用户数字签名
	 
	 @return 
	*/
	public static String GenerUserSigantureHtml(String userNo, String userName)
	{
		return "<img src='" + getCCFlowAppPath() + "DataUser/Siganture/" + userNo + ".jpg' title='" + userName + "' border=0 onerror=\"src='" + getCCFlowAppPath() + "DataUser/UserIcon/DefaultSmaller.png'\" />";
	}
	/** 
	 产生用户小图片
	 
	 @return 
	*/
	public static String GenerUserImgSmallerHtml(String userNo, String userName)
	{
		return "<img src='" + getCCFlowAppPath() + "DataUser/UserIcon/" + userNo + "Smaller.png' border=0  style='height:15px;width:15px;padding-right:5px;vertical-align:middle;'  onerror=\"src='" + getCCFlowAppPath() + "DataUser/UserIcon/DefaultSmaller.png'\" />" + userName;
	}
	/** 
	 产生用户大图片
	 
	 @return 
	*/
	public static String GenerUserImgHtml(String userNo, String userName)
	{
		return "<img src='" + getCCFlowAppPath() + "DataUser/UserIcon/" + userNo + ".png'  style='padding-right:5px;width:60px;height:80px;border:0px;text-align:middle' onerror=\"src='" + getCCFlowAppPath() + "DataUser/UserIcon/Default.png'\" /><br>" + userName;
	}
	/** 
	 更新主表的SQL
	*/
	public static String getUpdataMainDeptSQL() throws Exception {
		return SystemConfig.GetValByKey("UpdataMainDeptSQL", "UPDATE Port_Emp SET FK_Dept=" + SystemConfig.getAppCenterDBVarStr() + "FK_Dept WHERE No=" + SystemConfig.getAppCenterDBVarStr() + "No");
	}
	/** 
	 更新SID的SQL
	*/
	public static String getUpdataSID() {
		return SystemConfig.GetValByKey("UpdataSID", "UPDATE Port_Emp SET SID=" + SystemConfig.getAppCenterDBVarStr() + "SID WHERE No=" + SystemConfig.getAppCenterDBVarStr() + "No");
	}
	/** 
	 处理显示格式
	 
	 param no
	 param name
	 @return 现实格式
	*/
	public static String DealUserInfoShowModel(String no, String name) throws Exception {
		switch (bp.wf.Glo.getUserInfoShowModel())
		{
			case UserIDOnly:
				return no;
			case UserIDUserName:
				// return "(" + no + "," + name + ")";
				return no + "," + name;
			case UserNameOnly:
				//return "(" + name + ")";
				return name;
			default:
				throw new RuntimeException("@没有判断的格式类型.");
		}
	}

	/** 
	 处理人员显示格式
	 <p>added by liuxc,2017-4-27</p>
	 
	 param emps 人员字符串，类似"duqinglian,杜清莲;wangyihan,王一涵;”</param>
	 param idBefore 是否用户id在前面、用户name在后面
	 @return 
	*/

	public static String DealUserInfoShowModel(String emps) throws Exception {
		return DealUserInfoShowModel(emps, true);
	}

//ORIGINAL LINE: public static string DealUserInfoShowModel(string emps, bool idBefore = true)
	public static String DealUserInfoShowModel(String emps, boolean idBefore) throws Exception {
		if (DataType.IsNullOrEmpty(emps))
		{
			return emps;
		}

		boolean haveKH = emps.startsWith("(");

		if (haveKH)
		{
			emps = emps.replace("(", "").replace(")", "");
		}

		String[] es = emps.split(";");
		String newEmps = haveKH ? "(" : "";
		String[] ess = null;

		switch (bp.wf.Glo.getUserInfoShowModel())
		{
			case UserIDOnly:
				for (String e : es)
				{
					ess = e.split("[,]", -1);

					if (ess.length == 1)
					{
						newEmps += ess[0] + ";";
						continue;
					}

					newEmps += (idBefore ? ess[0] : ess[1]) + ";";
				}

				return haveKH ? (newEmps + ")") : newEmps;
			case UserNameOnly:
				for (String e : es)
				{
					ess = e.split("[,]", -1);

					if (ess.length == 1)
					{
						newEmps += ess[0] + ";";
						continue;
					}

					newEmps += (idBefore ? ess[1] : ess[0]) + ";";
				}

				return haveKH ? (newEmps + ")") : newEmps;
			default:
				return emps;
		}
	}
	/** 
	 钉钉是否启用
	*/
	public static boolean isEnableDingDing() throws Exception {
			//如果两个参数都不为空说明启用
		String corpid = SystemConfig.getDing_CorpID();
		String corpsecret = SystemConfig.getDing_CorpID();
		if (DataType.IsNullOrEmpty(corpid) || DataType.IsNullOrEmpty(corpsecret))
		{
			return false;
		}

		return true;
	}
	/** 
	 微信是否启用
	*/
	public static boolean isEnableWeiXin() throws Exception {
			//如果两个参数都不为空说明启用
		String corpid = SystemConfig.getWX_CorpID();
		String corpsecret = SystemConfig.getWX_AppSecret();
		if (DataType.IsNullOrEmpty(corpid) || DataType.IsNullOrEmpty(corpsecret))
		{
			return false;
		}

		return true;
	}
	/** 
	 是否检查表单树字段填写是否为空
	*/
	public static boolean isEnableCheckFrmTreeIsNull() throws Exception {
		return SystemConfig.GetValByKeyBoolen("IsEnableCheckFrmTreeIsNull", true);
	}
	/** 
	 是否启用消息系统消息。
	*/
	public static boolean isEnableSysMessage() throws Exception {
		return SystemConfig.GetValByKeyBoolen("IsEnableSysMessage", true);
	}
	/** 
	 与ccflow流程服务相关的配置: 执行自动任务节点，间隔的时间，以分钟计算，默认为2分钟。
	*/
	public static int getAutoNodeDTSTimeSpanMinutes() throws Exception {
		return SystemConfig.GetValByKeyInt("AutoNodeDTSTimeSpanMinutes", 60);
	}
	/** 
	 ccim集成的数据库.
	 是为了向ccim写入消息.
	*/
	public static String getCCIMDBName() throws Exception {
		String baseUrl = SystemConfig.getAppSettings().get("CCIMDBName").toString();
		if (DataType.IsNullOrEmpty(baseUrl) == true)
		{
			baseUrl = "ccPort.dbo";
		}
		return baseUrl;
	}
	/** 
	 主机
	*/
	public static String getHostURL()  {
		if (SystemConfig.getIsBSsystem())
		{
				/* 如果是BS 就要求 路径.*/
		}

		String baseUrl = SystemConfig.getAppSettings().get("HostURL").toString();
		if (DataType.IsNullOrEmpty(baseUrl) == true)
		{
			baseUrl = "http://127.0.0.1/";
		}

		if (!baseUrl.substring(baseUrl.length() - 1).equals("/"))
		{
			baseUrl = baseUrl + "/";
		}
		return baseUrl;
	}
	/** 
	 移动端主机
	*/
	public static String getMobileURL() throws Exception {
		if (SystemConfig.getIsBSsystem())
		{
				/* 如果是BS 就要求 路径.*/
		}

		String baseUrl = SystemConfig.getAppSettings().get("BpmMobileAddress").toString();
		if (DataType.IsNullOrEmpty(baseUrl) == true)
		{
			baseUrl = "http://127.0.0.1/";
		}

		if (!baseUrl.substring(baseUrl.length() - 1).equals("/"))
		{
			baseUrl = baseUrl + "/";
		}
		return baseUrl;
	}

		///#endregion


		///#region 时间计算.
	/** 
	 设置成工作时间
	 
	 param DateTime
	 @return 
	*/
	public static Date SetToWorkTime(Date dt) throws Exception {
		if (bp.sys.GloVar.getHolidays().contains(DateUtils.format(dt, "MM-dd")))
			{
				dt = DateUtils.addDay(dt, 1);
				/*如果当前是节假日，就要从下一个有效期计算。*/
				while (true) {
					if (bp.sys.GloVar.getHolidays().contains(DateUtils.format(dt, "MM-dd")) == false) {
						{
							break;
						}
					}
					// 从下一个上班时间计算.
					dt = DataType.ParseSysDate2DateTime(DateUtils.format(dt, "yyyy-MM-dd") + " " + Glo.getAMFrom());
					return dt;
				}
			}

			int timeInt = Integer.parseInt(DateUtils.format(dt, "HHmm"));

			//判断是否在A区间, 如果是，就返回A区间的时间点.
			if (Glo.getAMFromInt() >= timeInt) {
				return DataType.ParseSysDate2DateTime(DateUtils.format(dt, "yyyy-MM-dd") + " " + Glo.getPMFrom());
			}

			// 判断是否在E区间, 如果是就返回第2天的上班时间点.
			if (Glo.getPMToInt() <= timeInt) {
				return DataType.ParseSysDate2DateTime(DateUtils.format(dt, "yyyy-MM-dd") + " " + Glo.getPMTo());
			}

			// 如果在午休时间点中间.
			if (Glo.getAMToInt() <= timeInt && Glo.getPMFromInt() > timeInt) {
				return DataType.ParseSysDate2DateTime(DateUtils.format(dt, "yyyy-MM-dd") + " " + Glo.getPMFrom());
			}
			return dt;

	}
		/**
		 在指定的日期上增加小时数。
		 1，扣除午休。
		 2，扣除节假日。

		 param dt
		 param hours
		 @return
		 */
		private static Date AddMinutes (Date dt,int hh, int minutes) throws Exception {
			if (1 == 1) {
				Calendar c = Calendar.getInstance();
				c.setTime(dt);
				c.add(Calendar.HOUR, hh);
				c.add(Calendar.MINUTE, minutes);
				return c.getTime();
			}

			//如果没有设置,就返回.
			if (minutes == 0 && hh == 0) {
				return dt;
			}

			//设置成工作时间.
			dt = SetToWorkTime(dt);

			//首先判断是否是在一天整的时间完成.
			if (minutes == Glo.getAMPMHours() * 60) {
				/*如果需要在一天完成*/
				dt = DataType.AddDays(dt, 1, TWay.Holiday);
				return dt;
			}

			//判断是否是AM.
			boolean isAM = false;
			int timeInt = Integer.parseInt(DateUtils.format(dt, "HHmm"));
			if (Glo.getAMToInt() > timeInt) {
				isAM = true;
			}


			///#region 如果是当天的情况.
			//如果规定的时间在 1天之内.
			if (minutes / 60 / Glo.getAMPMHours() < 1) {
				if (isAM == true) {
					/* 如果是中午, 中午到中午休息之间的时间. */

					long ts = DataType.ParseSysDateTime2DateTime(DateUtils.format(dt, "yyyy-MM-dd") + " " + Glo.getAMTo())
							.getTime() - dt.getTime();
					if (ts / (60 * 1000) >= minutes) {
						/* 如果剩余的分钟大于 要增加的分钟数，就是说+上分钟后，仍然在中午，就直接增加上这个分钟，让其返回。 */
						return DateUtils.addMinutes(dt, minutes);
					} else {
						// 求出到下班时间的分钟数。
						long myts = DataType
								.ParseSysDateTime2DateTime(DateUtils.format(dt, "yyyy-MM-dd") + " " + Glo.getAMTo())
								.getTime() - dt.getTime();

						// 扣除午休的时间.
						int leftMuit = (int) (myts / (60 * 1000) - Glo.getAMPMTimeSpan() * 60);
						if (leftMuit - minutes >= 0) {
							/* 说明还是在当天的时间内. */
							java.util.Date mydt = DataType
									.ParseSysDateTime2DateTime(DateUtils.format(dt, "yyyy-MM-dd") + " " + Glo.getPMTo());
							return DateUtils.addMinutes(mydt, (minutes - leftMuit));
						}

						// 说明要跨到第2天上去了.
						dt = DataType.AddDays(dt, 1, TWay.Holiday);
						// return Glo.AddMinutes(DateUtils.format(dt,"yyyy-MM-dd") +
						// " " + Glo.getAMFrom(), minutes - leftMuit);
					}

					// 把当前的时间加上去.
					dt = DateUtils.addMinutes(dt, minutes);

					// 判断是否是中午.
					boolean isInAM = false;
					timeInt = Integer.parseInt(DateUtils.format(dt, "HHmm"));
					if (Glo.getAMToInt() >= timeInt) {
						isInAM = true;
					}

					if (isInAM == true) {
						// 加上时间后仍然是中午就返回.
						return dt;
					}

					// 延迟一个午休时间.
					dt = DateUtils.addHours(dt, (int) Glo.getAMPMTimeSpan());

					// 判断时间点是否落入了E区间.
					timeInt = Integer.parseInt(DateUtils.format(dt, "HHmm"));
					if (Glo.getPMToInt() <= timeInt) {
						/* 如果落入了E区间. */

						// 求出来时间点到，下班之间的分钟数.
						long tsE = dt.getTime() - DataType
								.ParseSysDate2DateTime(DateUtils.format(dt, "yyyy-MM-dd") + " " + Glo.getPMTo()).getTime();

						// 从次日的上班时间计算+ 这个时间差.
						dt = DataType.ParseSysDate2DateTime(DateUtils.format(dt, "yyyy-MM-dd") + " " + Glo.getPMTo());
						return DateUtils.addMinutes(dt, (int) tsE / (60 * 1000));
					} else {
						/* 过了第2天的情况很少，就不考虑了. */
						return dt;
					}
				} else {
					// 如果是下午, 计算出来到下午下班还需多少分钟，与增加的分钟数据相比较.
					long ts = DataType.ParseSysDateTime2DateTime(DateUtils.format(dt, "yyyy-MM-dd") + " " + Glo.getPMTo())
							.getTime() - dt.getTime();
					if (ts / (60 * 1000) >= minutes) {
						// 如果剩余的分钟大于 要增加的分钟数，就直接增加上这个分钟，让其返回。
						return DateUtils.addMinutes(dt, minutes);
					} else {

						// 剩余的分钟数 = 总分钟数 - 今天下午剩余的分钟数.
						int leftMin = minutes - (int) ts / (60 * 1000);

						// 否则要计算到第2天上去了， 计算时间要从下一个有效的工作日上班时间开始.
						dt = DataType
								.AddDays(
										DataType.ParseSysDateTime2DateTime(
												DateUtils.format(dt, "yyyy-MM-dd") + " " + Glo.getAMFrom()),
										1, TWay.Holiday);
						// 递归调用,让其在次日的上班时间在增加，分钟数。
						return Glo.AddMinutes(dt, 0, leftMin);
					}

				}
			}

			///#endregion 如果是当天的情况.

			return dt;
		}
		/**
		 增加分钟数.

		 param sysdt
		 param minutes
		 @return
		 */
		public static Date AddMinutes (String sysdt,int minutes) throws Exception {
			Date dt = DataType.ParseSysDate2DateTime(sysdt);
			return AddMinutes(dt, 0, minutes);
		}
		/**
		 在指定的日期上增加n天n小时，并考虑节假日

		 param sysdt 指定的日期
		 param day 天数
		 param minutes 分钟数
		 @return 返回计算后的日期
		 */
		public static Date AddDayHoursSpan (String specDT,int day, int hh, int minutes, TWay tway) throws Exception {
			Date mydt = DataType.AddDays(specDT, day, tway);
			return Glo.AddMinutes(mydt, hh, minutes);
		}
		/**
		 在指定的日期上增加n天n小时，并考虑节假日

		 param sysdt 指定的日期
		 param day 天数
		 param minutes 分钟数
		 @return 返回计算后的日期
		 */
		public static Date AddDayHoursSpan (Date specDT,int day, int hh, int minutes, TWay tway) throws Exception {
			Date mydt = bp.da.DataType.AddDays(specDT, day, tway);
			mydt = AddMinutes(mydt, 0, minutes);
			return mydt;
		}


		///#endregion ssxxx.


		///#region 与考核相关.
		/**
		 当流程发送下去以后，就开始执行考核。

		 param fl
		 param nd
		 param workid
		 param fid
		 param title
		 */

	public static void InitCH(Flow fl, Node nd, long workid, long fid, String title) throws Exception {
		InitCH(fl, nd, workid, fid, title, null);
	}


	public static void InitCH(Flow fl, Node nd, long workid, long fid, String title, GenerWorkerList gwl) throws Exception {
		InitCH2017(fl, nd, workid, fid, title, null, null, new Date(), gwl);
	}
	/** 
	 执行考核
	 
	 param fl 流程
	 param nd 节点
	 param workid 工作ID
	 param fid FID
	 param title 标题
	 param prvRDT 上一个时间点
	 param sdt 应完成日期
	 param dtNow 当前日期
	*/
	private static void InitCH2017(Flow fl, Node nd, long workid, long fid, String title, String prvRDT, String sdt, Date dtNow, GenerWorkerList gwl) throws Exception {

		// 开始节点不考核.
		if (nd.isStartNode() || nd.getHisCHWay() == CHWay.None)
		{
			return;
		}

		//如果设置为0,则不考核.
		if (nd.getTimeLimit() == 0 && nd.getTimeLimitHH() == 0 && nd.getTimeLimitMM() == 0)
		{
			return;
		}

		if (dtNow == null)
		{
			dtNow = new Date();
		}


			///#region 求参与人员 todoEmps ，应完成日期 sdt ，与工作派发日期 prvRDT.
		//参与人员.
		String todoEmps = "";
		String dbstr = SystemConfig.getAppCenterDBVarStr();
		if (nd.isEndNode() == true && gwl == null)
		{
			/* 如果是最后一个节点，可以使用这样的方式来求人员信息 , */


				///#region 求应完成日期，与参与的人集合.
			Paras ps = new Paras();
			switch (SystemConfig.getAppCenterDBType( ))
			{
				case MSSQL:
					ps.SQL = "SELECT TOP 1 SDTOfNode, TodoEmps FROM WF_GenerWorkFlow  WHERE WorkID=" + dbstr + "WorkID ";
					break;
				case Oracle:
				case KingBaseR3:
				case KingBaseR6:
					ps.SQL = "SELECT SDTOfNode, TodoEmps FROM WF_GenerWorkFlow  WHERE WorkID=" + dbstr + "WorkID  ";
					break;
				case MySQL:
					ps.SQL = "SELECT SDTOfNode, TodoEmps FROM WF_GenerWorkFlow  WHERE WorkID=" + dbstr + "WorkID  ";
					break;
				case PostgreSQL:
				case UX:
					ps.SQL = "SELECT SDTOfNode, TodoEmps FROM WF_GenerWorkFlow  WHERE WorkID=" + dbstr + "WorkID  ";
					break;
				default:
					throw new RuntimeException("err@没有判断的数据库类型.");
			}

			ps.Add("WorkID", workid);
			DataTable dt = DBAccess.RunSQLReturnTable(ps);
			if (dt.Rows.size() == 0)
			{
				return;
			}
			sdt = dt.Rows.get(0).getValue("SDTOfNode").toString(); //应完成日期.
			todoEmps = dt.Rows.get(0).getValue("TodoEmps").toString(); //参与人员.

				///#endregion 求应完成日期，与参与的人集合.


				///#region 求上一个节点的日期.
			dt = Dev2Interface.Flow_GetPreviousNodeTrack(workid, nd.getNodeID());
			if (dt.Rows.size() == 0)
			{
				return;
			}
			//上一个节点的活动日期.
			prvRDT = dt.Rows.get(0).getValue("RDT").toString();

				///#endregion
		}


		if (nd.isEndNode() == false)
		{
			if (gwl == null)
			{
				gwl = new GenerWorkerList();
				gwl.Retrieve(GenerWorkerListAttr.WorkID, workid, GenerWorkerListAttr.FK_Node, nd.getNodeID(), GenerWorkerListAttr.FK_Emp, WebUser.getNo());
			}

			prvRDT = gwl.getRDT(); // dt.Rows.get(0).getValue("RDT"].ToString(); //上一个时间点的记录日期.
			sdt = gwl.getSDT(); //  dt.Rows.get(0).getValue("SDT"].ToString(); //应完成日期.
			todoEmps = WebUser.getNo() + "," + WebUser.getName() + ";";
		}

			///#endregion 求参与人员，应完成日期，与工作派发日期.


			///#region 求 preSender上一个发送人，preSenderText 发送人姓名
		String preSender = "";
		String preSenderText = "";
		DataTable dt_Sender = Dev2Interface.Flow_GetPreviousNodeTrack(workid, nd.getNodeID());
		if (dt_Sender.Rows.size() > 0)
		{
			preSender = dt_Sender.Rows.get(0).getValue("EmpFrom").toString();
			preSenderText = dt_Sender.Rows.get(0).getValue("EmpFromT").toString();
		}

			///#endregion


			///#region 初始化基础数据.
		CH ch = new CH();
		ch.setWorkID(workid);
		ch.setFID(fid);
		ch.setTitle(title);

		//记录当时设定的值.
		ch.setTimeLimit(nd.getTimeLimit());

		ch.setFK_NY(DateUtils.format(dtNow, "yyyy-MM"));

		ch.setDTFrom(prvRDT); //任务下达时间.
		ch.setDTTo(DateUtils.format(dtNow,"yyyy-MM-dd HH:mm:ss")); //时间到

		ch.setSDT(sdt); //应该完成时间.

		ch.setFK_Flow(nd.getFK_Flow()); //流程信息.
		ch.setFK_FlowT(nd.getFlowName());

		ch.setFK_Node(nd.getNodeID()); //节点.
		ch.setFK_NodeT(nd.getName());

		ch.setFK_Dept(WebUser.getFK_Dept()); //部门.
		ch.setFK_DeptT(WebUser.getFK_DeptName());

		ch.setFK_Emp(WebUser.getNo()); //当事人.
		ch.setFK_EmpT(WebUser.getName());

		// 处理相关联的当事人.
		ch.setGroupEmpsNames(todoEmps);
		//上一步发送人
		ch.setSender(preSender);
		ch.setSenderT(preSenderText);
		//考核状态
		ch.setDTSWay(nd.getHisCHWay().getValue());

		//求参与人员数量.
		String[] strs = todoEmps.split("[;]", -1);
		ch.setGroupEmpsNum(strs.length - 1); //个数.

		//求参与人的ids.
		String empids = ",";
		for (String str : strs)
		{
			if (DataType.IsNullOrEmpty(str))
			{
				continue;
			}

			String[] mystr = str.split("[,]", -1);
			empids += mystr[0] + ",";
		}
		ch.setGroupEmps(empids);

		// mypk.
		ch.setMyPK(nd.getNodeID() + "_" + workid + "_" + fid + "_" + WebUser.getNo());

			///#endregion 初始化基础数据.


			///#region 求计算属性.
		//求出是第几个周.
		ch.setWeekNum(DataType.WeekOfYear(dtNow));

		// UseDays . 求出实际使用天数.
		Date dtFrom = DataType.ParseSysDate2DateTime(ch.getDTFrom());
		Date dtTo = DataType.ParseSysDate2DateTime(ch.getDTTo());

		long ts = dtTo.getTime() - dtFrom.getTime();
		ch.setUseDays(ts / 1000 / 60 / 60 / 24); // 用时，天数
		ch.setUseMinutes(ts / 1000 / 60); // 用时，分钟

		if (DataType.IsNullOrEmpty(ch.getSDT()) == false && ch.getSDT().equals("无") == false)
		{
			// OverDays . 求出 逾期天 数.
			Date sdtOfDT = DataType.ParseSysDate2DateTime(ch.getSDT());

			long myts = dtTo.getTime() - sdtOfDT.getTime();
			ch.setOverDays(myts / 1000 / 60 / 60 / 24); // 逾期的天数.
			ch.setOverMinutes(myts / 1000 / 60); // 逾期的分钟数
			if (sdtOfDT.compareTo(dtTo) >= 0)
			{
				/* 正常完成 */
				ch.setCHSta(CHSta.AnQi); //按期完成.
				ch.setPoints(0);
			}
			else
			{
				/*逾期完成.*/
				ch.setCHSta(CHSta.YuQi); //逾期完成.
				float sum = ch.getOverDays() * nd.getTCent();
				ch.setPoints((float) (Math.round(sum * 100)) / 100);
			}
		}
		else
		{
			/* 正常完成 */
			ch.setCHSta(CHSta.AnQi); //按期完成.
			ch.setPoints(0);
		}


			///#endregion 求计算属性.

		//执行保存.
		try
		{
			ch.DirectInsert();
		}
		catch (java.lang.Exception e)
		{
			if (ch.getIsExits() == true)
			{
				ch.Update();
			}
			else
			{
				//如果遇到退回的情况就可能涉及到主键重复的问题.
				ch.setMyPK(DBAccess.GenerGUID(0, null, null));
				ch.Insert();
			}
		}
	}
	/**
	 中午时间从
	*/
	public static String getAMFrom()  {
		return SystemConfig.GetValByKey("AMFrom", "08:30");
	}
	/**
	 中午时间从
	*/
	public static int getAMFromInt() {
		return Integer.parseInt(Glo.getAMFrom().replace(":", ""));
	}
	/**
	 一天有效的工作小时数
	 是中午工作小时+下午工作小时.
	*/
	public static float getAMPMHours()  {
		return SystemConfig.GetValByKeyFloat("AMPMHours", 8);
	}
	/**
	 中午间隔的小时数
	*/
	public static float getAMPMTimeSpan()  {
		return SystemConfig.GetValByKeyFloat("AMPMTimeSpan", 1);
	}
	/**
	 中午时间到
	*/
	public static String getAMTo() {
		return SystemConfig.GetValByKey("AMTo", "11:30");
	}
	/**
	 中午时间到
	*/
	public static int getAMToInt()  {
		return Integer.parseInt(Glo.getAMTo().replace(":", ""));
	}
	/**
	 下午时间从
	*/
	public static String getPMFrom() {
		return SystemConfig.GetValByKey("PMFrom", "13:30");
	}
	/**
	 到
	*/
	public static int getPMFromInt() throws Exception {
		return Integer.parseInt(Glo.getPMFrom().replace(":", ""));
	}
	/**
	 到
	*/
	public static String getPMTo() throws Exception {
		return SystemConfig.GetValByKey("PMTo", "17:30");
	}
	/**
	 到
	*/
	public static int getPMToInt() throws Exception {
		return Integer.parseInt(Glo.getPMTo().replace(":", ""));
	}

		///#endregion 与考核相关.


		///#region 其他方法。

	/** 
	 删除临时文件
	*/
	public static void DeleteTempFiles() throws Exception {
		try
		{
			//删除目录.
			String temp = SystemConfig.getPathOfTemp();
			File file = new File(temp);
			if(file.exists() == true)
				file.delete();

			//创建目录.
			(new File(temp)).mkdirs();

			//删除pdf 目录.
			temp = SystemConfig.getPathOfDataUser() + "InstancePacketOfData/";
			file = new File(temp);
			File[] dirs = file.listFiles();
			for (File dir : dirs) {
				if (dir.isDirectory() == true && dir.getName().indexOf("ND") == 0) {
					dir.delete();
				}
			}
		}
		catch (RuntimeException ex)
		{

		}
	}

	public static bp.sys.FrmAttachmentDBs GenerFrmAttachmentDBs(FrmAttachment athDesc, String pkval, String FK_FrmAttachment, long workid, long fid, long pworkid, boolean isContantSelf, int fk_node) throws Exception {
		return GenerFrmAttachmentDBs(athDesc, pkval, FK_FrmAttachment, workid, fid, pworkid, isContantSelf, fk_node, null);
	}

	public static bp.sys.FrmAttachmentDBs GenerFrmAttachmentDBs(FrmAttachment athDesc, String pkval, String FK_FrmAttachment, long workid, long fid, long pworkid, boolean isContantSelf) throws Exception {
		return GenerFrmAttachmentDBs(athDesc, pkval, FK_FrmAttachment, workid, fid, pworkid, isContantSelf, 0, null);
	}

	public static bp.sys.FrmAttachmentDBs GenerFrmAttachmentDBs(FrmAttachment athDesc, String pkval, String FK_FrmAttachment, long workid, long fid, long pworkid) throws Exception {
		return GenerFrmAttachmentDBs(athDesc, pkval, FK_FrmAttachment, workid, fid, pworkid, true, 0, null);
	}

	public static bp.sys.FrmAttachmentDBs GenerFrmAttachmentDBs(FrmAttachment athDesc, String pkval, String FK_FrmAttachment, long workid, long fid) throws Exception {
		return GenerFrmAttachmentDBs(athDesc, pkval, FK_FrmAttachment, workid, fid, 0, true, 0, null);
	}

	public static bp.sys.FrmAttachmentDBs GenerFrmAttachmentDBs(FrmAttachment athDesc, String pkval, String FK_FrmAttachment, long workid) throws Exception {
		return GenerFrmAttachmentDBs(athDesc, pkval, FK_FrmAttachment, workid, 0, 0, true, 0, null);
	}

	public static bp.sys.FrmAttachmentDBs GenerFrmAttachmentDBs(FrmAttachment athDesc, String pkval, String FK_FrmAttachment) throws Exception {
		return GenerFrmAttachmentDBs(athDesc, pkval, FK_FrmAttachment, Long.parseLong(pkval), 0, 0, true, 0, null);
	}

	public static FrmAttachmentDBs GenerFrmAttachmentDBs(FrmAttachment athDesc, String pkval, String FK_FrmAttachment, long workid, long fid, long pworkid, boolean isContantSelf, int fk_node, String fk_mapData) throws Exception {
		if (pkval == null)
		{
			pkval = "0"; //解决预览的时候的错误.
		}

		FrmAttachmentDBs dbs = new FrmAttachmentDBs();
		//查询使用的workId
		String ctrlWayId = "";
		if (FK_FrmAttachment.contains("AthMDtl") == true)
		{
			ctrlWayId = pkval;
		}
		else
		{
			MapData mapData = new MapData(athDesc.getFK_MapData());
			if (mapData.getEntityType() == EntityType.FrmDict || mapData.getEntityType() == EntityType.FrmBill)
			{
				ctrlWayId = pkval;
			}
			else
			{
				ctrlWayId = bp.wf.Dev2Interface.GetAthRefPKVal(workid, pworkid, fid, fk_node, fk_mapData, athDesc);
			}
		}


		//如果是空的，就返回空数据结构. @lizhen.
		if (ctrlWayId.equals("0") == true)
		{
			return dbs;
		}

		QueryObject qo = new QueryObject(dbs);
		if (athDesc.getHisCtrlWay() == AthCtrlWay.MySelfOnly || athDesc.getHisCtrlWay() == AthCtrlWay.PK)
		{
			if (FK_FrmAttachment.contains("AthMDtl"))
			{
				/*如果是一个明细表的多附件，就直接按照传递过来的PK来查询.*/
				qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, pkval);
				qo.addAnd();
				qo.AddWhere(FrmAttachmentDBAttr.FK_FrmAttachment, FK_FrmAttachment);

				qo.DoQuery();
			}
			else
			{
				qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, pkval);
				qo.addAnd();
				qo.AddWhere(FrmAttachmentDBAttr.FK_FrmAttachment, FK_FrmAttachment);
				if (isContantSelf == false)
				{
					qo.addAnd();
					qo.AddWhere(FrmAttachmentDBAttr.Rec, "!=", WebUser.getNo());
				}
				qo.addOrderBy("Idx,RDT");
				qo.DoQuery();

			}
			return dbs;
		}

		/* 继承模式 */
		if (athDesc.getAthUploadWay() == AthUploadWay.Interwork)
		{
			qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, ctrlWayId);
		}
		else
		{
			qo.AddWhereIn(FrmAttachmentDBAttr.RefPKVal, "('" + ctrlWayId + "','" + pkval + "')");
		}

		qo.addAnd();
		qo.AddWhere(FrmAttachmentDBAttr.NoOfObj, athDesc.getNoOfObj());

		if (isContantSelf == false)
		{
			qo.addAnd();
			qo.AddWhere(FrmAttachmentDBAttr.Rec, "!=", WebUser.getNo());
		}
		qo.addOrderBy("Idx,RDT");
		qo.DoQuery();
		return dbs;
	}
	/** 
	 获得一个表单的动态附件字段
	 
	 param exts 扩展
	 param nd 节点
	 param en 实体
	 param md map
	 param attrs 属性集合
	 @return 附件的主键
	*/
	public static String GenerActiveAths(MapExts exts, Node nd, Entity en, MapData md, MapAttrs attrs) throws Exception {
		String strs = "";
		for (MapExt me : exts.ToJavaList())
		{
			if (!MapExtXmlList.SepcAthSepcUsers.equals(me.getExtType()))
			{
				continue;
			}

			boolean isCando = false;
			if (!me.getTag1().equals(""))
			{
				String tag1 = me.getTag1() + ",";
				if (tag1.contains(WebUser.getNo() + ","))
				{
					//根据设置的人员计算.
					isCando = true;
				}
			}

			if (!me.getTag2().equals(""))
			{
				//根据sql判断.
				Object tempVar = me.getTag2();
				String sql = tempVar instanceof String ? (String)tempVar : null;
				sql = bp.wf.Glo.DealExp(sql, en);
				if (DBAccess.RunSQLReturnValFloat(sql) > 0)
				{
					isCando = true;
				}
			}

			if (!me.getTag3().equals("") && me.getTag3().equals(WebUser.getFK_Dept()))
			{
				//根据部门编号判断.
				isCando = true;
			}

			if (isCando == false)
			{
				continue;
			}
			strs += me.getDoc();
		}
		return strs;
	}
	/** 
	 获得一个表单的动态权限字段
	 
	 param exts
	 param nd
	 param en
	 param md
	 param attrs
	 @return 
	*/
	public static String GenerActiveFiels(MapExts exts, Node nd, Entity en, MapData md, MapAttrs attrs) throws Exception {
		String strs = "";
		for (MapExt me : exts.ToJavaList())
		{
			if (!MapExtXmlList.SepcFiledsSepcUsers.equals(me.getExtType()))
			{
				continue;
			}

			boolean isCando = false;
			if (!me.getTag1().equals(""))
			{
				String tag1 = me.getTag1() + ",";
				if (tag1.contains(WebUser.getNo() + ","))
				{
					//根据设置的人员计算.
					isCando = true;
				}
			}

			if (!me.getTag2().equals(""))
			{
				//根据sql判断.
				Object tempVar = me.getTag2();
				String sql = tempVar instanceof String ? (String)tempVar : null;
				sql = bp.wf.Glo.DealExp(sql, en);
				if (DBAccess.RunSQLReturnValFloat(sql) > 0)
				{
					isCando = true;
				}
			}

			if (!me.getTag3().equals("") && me.getTag3().equals(WebUser.getFK_Dept()))
			{
				//根据部门编号判断.
				isCando = true;
			}

			if (isCando == false)
			{
				continue;
			}
			strs += me.getDoc();
		}
		return strs;
	}
	/** 
	 检查流程发起限制
	 
	 param flow 流程
	 param wk 开始节点工作
	 @return 
	*/
	public static boolean CheckIsCanStartFlow_InitStartFlow(Flow flow)throws Exception
	{
		StartLimitRole role = flow.getStartLimitRole();
		if (role == StartLimitRole.None)
		{
			return true;
		}

		String sql = "";
		String ptable = flow.getPTable();

		try
		{

				///#region 按照时间的必须是，在表单加载后判断, 不管用户设置是否正确.
			Date dtNow = new Date();
			if (role == StartLimitRole.Day)
			{
				/* 仅允许一天发起一次 */
				sql = "SELECT COUNT(*) as Num FROM " + ptable + " WHERE RDT LIKE '" + DataType.getCurrentDate() + "%' AND WFState NOT IN(0,1) AND FlowStarter='" + WebUser.getNo() + "'";
				if (DBAccess.RunSQLReturnValInt(sql, 0) == 0)
				{
					if (DataType.IsNullOrEmpty(flow.getStartLimitPara()))
					{
						return true;
					}

					//判断时间是否在设置的发起范围内. 配置的格式为 @11:00-12:00@15:00-13:45
					String[] strs = flow.getStartLimitPara().split("[@]", -1);
					for (String str : strs)
					{
						if (DataType.IsNullOrEmpty(str))
						{
							continue;
						}
						String[] timeStrs = str.split("[-]", -1);
						String tFrom = DataType.getCurrentDateByFormart("yyyy-MM-dd") + " " + timeStrs[0].trim();
						String tTo = DataType.getCurrentDateByFormart("yyyy-MM-dd") + " " + timeStrs[1].trim();
						if (DataType.ParseSysDateTime2DateTime(tFrom).compareTo(dtNow) <= 0 && DataType.ParseSysDateTime2DateTime(tTo).compareTo(dtNow) >= 0)
						{
							return true;
						}
					}
					return false;
				}
				else
				{
					return false;
				}
			}

			if (role == StartLimitRole.Week)
			{
				/*
				 * 1, 找出周1 与周日分别是第几日.
				 * 2, 按照这个范围去查询,如果查询到结果，就说明已经启动了。
				 */
				sql = "SELECT COUNT(*) as Num FROM " + ptable + " WHERE RDT >= '" + DataType.WeekOfMonday(dtNow) + "' AND WFState NOT IN(0,1) AND FlowStarter='" + WebUser.getNo() + "'";
				if (DBAccess.RunSQLReturnValInt(sql, 0) == 0)
				{
					if (DataType.IsNullOrEmpty(flow.getStartLimitPara()))
					{
						return true; //如果没有时间的限制.
					}

					//判断时间是否在设置的发起范围内. 
					// 配置的格式为 @Sunday,11:00-12:00@Monday,15:00-13:45, 意思是.周日，周一的指定的时间点范围内可以启动流程.

					String[] strs = flow.getStartLimitPara().split("[@]", -1);
					for (String str : strs)
					{
						if (DataType.IsNullOrEmpty(str))
						{
							continue;
						}

						String weekStr = DateUtils.getDayOfWeekT(Integer.parseInt(DateUtils.dayForWeek(new Date())));
						if (str.toLowerCase().contains(weekStr) == false)
						{
							continue; // 判断是否当前的周.
						}

						String[] timeStrs = str.split("[,]", -1);
						String tFrom = DataType.getCurrentDateByFormart("yyyy-MM-dd") + " " + timeStrs[0].trim();
						String tTo = DataType.getCurrentDateByFormart("yyyy-MM-dd") + " " + timeStrs[1].trim();
						if (DataType.ParseSysDateTime2DateTime(tFrom).compareTo(dtNow) <= 0 && dtNow.compareTo(DataType.ParseSysDateTime2DateTime(tTo)) >= 0)
						{
							return true;
						}
					}
					return false;
				}
				else
				{
					return false;
				}
			}

			// #warning 没有考虑到周的如何处理.

			if (role == StartLimitRole.Month)
			{
				sql = "SELECT COUNT(*) as Num FROM " + ptable + " WHERE FK_NY = '" + DataType.getCurrentYearMonth() + "' AND WFState NOT IN(0,1) AND FlowStarter='" + WebUser.getNo() + "'";
				if (DBAccess.RunSQLReturnValInt(sql, 0) == 0)
				{
					if (DataType.IsNullOrEmpty(flow.getStartLimitPara()))
					{
						return true;
					}

					//判断时间是否在设置的发起范围内. 配置格式: @-01 12:00-13:11@-15 12:00-13:11 , 意思是：在每月的1号,15号 12:00-13:11可以启动流程.
					String[] strs = flow.getStartLimitPara().split("[@]", -1);
					for (String str : strs)
					{
						if (DataType.IsNullOrEmpty(str))
						{
							continue;
						}
						String[] timeStrs = str.split("[-]", -1);
						String tFrom = DataType.getCurrentDateByFormart("yyyy-MM-dd") + " " + timeStrs[0].trim();
						String tTo = DataType.getCurrentDateByFormart("yyyy-MM-dd") + " " + timeStrs[1].trim();
						if (DataType.ParseSysDateTime2DateTime(tFrom).compareTo(dtNow) <= 0 && dtNow.compareTo(DataType.ParseSysDateTime2DateTime(tTo)) >= 0)
						{
							return true;
						}
					}
					return false;
				}
				else
				{
					return false;
				}
			}

			if (role == StartLimitRole.JD)
			{
				sql = "SELECT COUNT(*) as Num FROM " + ptable + " WHERE FK_NY = '" + DataType.getCurrentAPOfJD() + "' AND WFState NOT IN(0,1) AND FlowStarter='" + WebUser.getNo() + "'";
				if (DBAccess.RunSQLReturnValInt(sql, 0) == 0)
				{
					if (DataType.IsNullOrEmpty(flow.getStartLimitPara()))
					{
						return true;
					}

					//判断时间是否在设置的发起范围内.
					String[] strs = flow.getStartLimitPara().split("[@]", -1);
					for (String str : strs)
					{
						if (DataType.IsNullOrEmpty(str))
						{
							continue;
						}
						String[] timeStrs = str.split("[-]", -1);
						String tFrom = DataType.getCurrentDateByFormart("yyyy-MM-dd") + " " + timeStrs[0].trim();
						String tTo = DataType.getCurrentDateByFormart("yyyy-MM-dd") + " " + timeStrs[1].trim();
						if (DataType.ParseSysDateTime2DateTime(tFrom).compareTo(dtNow) <= 0 && dtNow.compareTo(DataType.ParseSysDateTime2DateTime(tTo)) >= 0)
						{
							return true;
						}
					}
					return false;
				}
				else
				{
					return false;
				}
			}

			if (role == StartLimitRole.Year)
			{
				sql = "SELECT COUNT(*) as Num FROM " + ptable + " WHERE FK_NY LIKE '" + DataType.getCurrentYear() + "%' AND WFState NOT IN(0,1) AND FlowStarter='" + WebUser.getNo() + "'";
				if (DBAccess.RunSQLReturnValInt(sql, 0) == 0)
				{
					if (DataType.IsNullOrEmpty(flow.getStartLimitPara()))
					{
						return true;
					}

					//判断时间是否在设置的发起范围内.
					String[] strs = flow.getStartLimitPara().split("[@]", -1);
					for (String str : strs)
					{
						if (DataType.IsNullOrEmpty(str))
						{
							continue;
						}
						String[] timeStrs = str.split("[-]", -1);
						String tFrom = DataType.getCurrentDateByFormart("yyyy-MM-dd") + " " + timeStrs[0].trim();
						String tTo = DataType.getCurrentDateByFormart("yyyy-MM-dd") + " " + timeStrs[1].trim();
						if (DataType.ParseSysDateTime2DateTime(tFrom).compareTo(dtNow) <= 0 && dtNow.compareTo(DataType.ParseSysDateTime2DateTime(tTo)) >= 0)
						{
							return true;
						}
					}
					return false;
				}
				else
				{
					return false;
				}
			}

				///#endregion 按照时间的必须是，在表单加载后判断, 不管用户设置是否正确.


			//为子流程的时候，该子流程只能被调用一次.
			if (role == StartLimitRole.OnlyOneSubFlow)
			{

				if (SystemConfig.getIsBSsystem() == true)
				{

					String pflowNo = CommonUtils.getRequest().getParameter("PFlowNo");
					String pworkid = CommonUtils.getRequest().getParameter("PWorkID");

					if (pworkid == null)
					{
						return true;
					}

					sql = "SELECT Starter, RDT FROM WF_GenerWorkFlow WHERE PWorkID=" + pworkid + " AND FK_Flow='" + flow.getNo() + "'";
					DataTable dt = DBAccess.RunSQLReturnTable(sql);
					if (dt.Rows.size() == 0 || dt.Rows.size() == 1)
					{
						return true;
					}

					//  string title = dt.Rows.get(0).getValue("Title"].ToString();
					String starter = dt.Rows.get(0).getValue("Starter").toString();
					String rdt = dt.Rows.get(0).getValue("RDT").toString();
					return false;
					//throw new Exception(flow.StartLimitAlert + "@该子流程已经被[" + starter + "], 在[" + rdt + "]发起，系统只允许发起一次。");
				}
			}

			// 配置的sql,执行后,返回结果是 0 .
			if (role == StartLimitRole.ResultIsZero)
			{
				sql = bp.wf.Glo.DealExp(flow.getStartLimitPara(), null);
				if (DBAccess.RunSQLReturnValInt(sql, 0) == 0)
				{
					return true;
				}
				else
				{
					return false;
				}
			}

			// 配置的sql,执行后,返回结果是 <> 0 .
			if (role == StartLimitRole.ResultIsNotZero)
			{
				if (DataType.IsNullOrEmpty(flow.getStartLimitPara()) == true)
				{
					return true;
				}

				sql = bp.wf.Glo.DealExp(flow.getStartLimitPara(), null);
				if (DBAccess.RunSQLReturnValInt(sql, 0) != 0)
				{
					return true;
				}
				else
				{
					return false;
				}
			}

			return true;
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("err@发起限制规则[" + role.toString() + "]出现错误:" + ex.getMessage());
		}
	}

	/** 
	 复制表单权限-从一个节点到另一个节点.
	 
	 param fk_flow 流程编号
	 param frmID 表单ID
	 param currNodeID 当前节点
	 param fromNodeID 从节点
	*/
	public static void CopyFrmSlnFromNodeToNode(String fk_flow, String frmID, int currNodeID, int fromNodeID) throws Exception {

			///#region 处理字段.
		//删除现有的.
		FrmFields frms = new FrmFields();
		frms.Delete(FrmFieldAttr.FK_Node, currNodeID, FrmFieldAttr.FK_MapData, frmID);

		//查询出来,指定的权限方案.
		frms.Retrieve(FrmFieldAttr.FK_Node, fromNodeID, FrmFieldAttr.FK_MapData, frmID, null);

		//开始复制.
		for (FrmField item : frms.ToJavaList())
		{
			item.setMyPK(frmID + "_" + fk_flow + "_" + currNodeID + "_" + item.getKeyOfEn());
			item.setFK_Node(currNodeID);
			item.Insert(); // 插入数据库.
		}

			///#endregion 处理字段.

		//没有考虑到附件的权限 20161020 hzm

			///#region 附件权限

		FrmAttachments fas = new FrmAttachments();
		//删除现有节点的附件权限
		fas.Delete(FrmAttachmentAttr.FK_Node, currNodeID, FrmAttachmentAttr.FK_MapData, frmID);
		//查询出 现在表单上是否有附件的情况
		fas.Retrieve(FrmAttachmentAttr.FK_Node, fromNodeID, FrmAttachmentAttr.FK_MapData, frmID, null);

		//复制权限
		for (FrmAttachment fa : fas.ToJavaList())
		{
			fa.setMyPK(fa.getFK_MapData() + "_" + fa.getNoOfObj() + "_" + currNodeID);
			fa.setFK_Node(currNodeID);
			fa.Insert();
		}


			///#endregion

	}
	/** 
	 产生消息,senderEmpNo是为了保证写入消息的唯一性，receiveid才是真正的接收者.
	 如果插入失败.
	 
	 param fromEmpNo 发送人
	 param now 发送时间
	 param msg 消息内容
	 param sendToEmpNo 接受人
	*/
	public static void SendMessageToCCIM(String fromEmpNo, String sendToEmpNo, String msg, String now) throws Exception {
		//周朋.
		return; //暂停支持.

	}

	private static final String StrRegex = "-|;|,|/|(|)|[|]|}|{|%|@|*|!|'|`|~|#|$|^|&|.|?";
	private static final String StrKeyWord = "select|insert|delete|from|count(|drop table|update|truncate|asc(|mid(|char(|xp_cmdshell|exec master|netlocalgroup administrators|:|net user|\"|or|and";
	/** 
	 检查KeyWord是否包涵特殊字符
	 
	 param _sWord 需要检查的字符串
	 @return 
	*/
	public static String CheckKeyWord(String KeyWord)
	{
		//特殊符号
		String[] strRegx = StrRegex.split("|");
		//特殊符号 的注入情况
		for (String key : strRegx)
		{
			if (KeyWord.indexOf(key) >= 0)
			{
				//替换掉特殊字符
				KeyWord = KeyWord.replace(key, "");
			}
		}
		return KeyWord;
	}
	/** 
	 检查_sword是否包涵SQL关键字
	 
	 param _sWord 需要检查的字符串
	 @return 存在SQL注入关键字时返回 true，否则返回 false
	*/
	public static boolean CheckKeyWordInSql(String _sWord)
	{
		boolean result = false;
		//Sql注入de可能关键字
		String[] patten1 = StrKeyWord.split("[|]", -1);
		//Sql注入的可能关键字 的注入情况
		for (String sqlKey : patten1)
		{
			if (_sWord.indexOf(" " + sqlKey) >= 0 || _sWord.indexOf(sqlKey + " ") >= 0)
			{
				//只要存在一个可能出现Sql注入的参数,则直接退出
				result = true;
				break;
			}
		}
		return result;
	}

		///#endregion 其他方法。


		///#region http请求
	/** 
	 Http Get请求
	 
	 param url
	 @return 
	*/
//	public static String HttpGet(String url)
//	{
//		try
//		{
//			HttpWebRequest request;
//			// 创建一个HTTP请求
//			request = (HttpWebRequest)WebRequest.Create(url);
//			// request.Method="get";
//			HttpWebResponse response;
//			response = (HttpWebResponse)request.GetResponse();
//			InputStreamReader myreader = new InputStreamReader(response.GetResponseStream(), java.nio.charset.StandardCharsets.UTF_8);
//			String responseText = myreader.ReadToEnd();
//			myreader.close();
//			response.Close();
//			return responseText;
//		}
//		catch (RuntimeException ex)
//		{
//			//url请求失败
//			return ex.getMessage();
//		}
//	}
	/** 
	 httppost方式发送数据
	 
	 param url 要提交的url
	 param postDataStr
	 param timeOut 超时时间
	 param encode text code.
	 @return 成功：返回读取内容；失败：0
	*/
//	public static String HttpPostConnect(String serverUrl, String postData)
//	{
//		byte[] dataArray = postData.getBytes(StandardCharsets.UTF_8);
//		//创建请求
//		String request = (HttpWebRequest)HttpWebRequest.Create(serverUrl);
//		request.Method = "POST";
//		request.ContentLength = dataArray.Length;
//		//设置上传服务的数据格式  设置之后不好使
//		//request.ContentType = "application/x-www-form-urlencoded";
//		//请求的身份验证信息为默认
//		request.Credentials = CredentialCache.DefaultCredentials;
//		// request.ContentType = "application/x-www-form-urlencoded";
//		request.ContentType = "application/json";
//		//  request.ContentType = "application/x-www-form-urlencoded";
//
//		//请求超时时间
//		request.Timeout = 10000;
//		//创建输入流
////C# TO JAVA CONVERTER TODO TASK: C# to Java Converter cannot determine whether this System.IO.Stream is input or output:
//		Stream dataStream;
//		try
//		{
//			dataStream = request.GetRequestStream();
//		}
//		catch (RuntimeException e)
//		{
//			return "0"; //连接服务器失败
//		}
//		//发送请求
//		dataStream.Write(dataArray, 0, dataArray.Length);
//		dataStream.Close();
//
//		HttpWebResponse res;
//
//		res = (HttpWebResponse)request.GetResponse();
//
//		//}
//		//catch (WebException ex)
//		//{
//		//    throw new Exception(ex.Message);
//		//    //res = (HttpWebResponse)ex.Response;
//		//}
//		InputStreamReader sr = new InputStreamReader(res.GetResponseStream(), java.nio.charset.StandardCharsets.UTF_8);
//		//读取返回消息
//		String data = sr.ReadToEnd();
//		sr.close();
//		return data;
//	}
	/** 
	 httppost方式发送数据
	 
	 param url 要提交的url
	 param headerMap 自定义的请求头
	 param postDataStr
	 param timeOut 超时时间
	 param encode text code.
	 @return 成功：返回读取内容；失败：0
	*/
//	public static String HttpPostConnect(String serverUrl, Hashtable headerMap, String postData)
//	{
//		var dataArray = postData.getBytes(java.nio.charset.StandardCharsets.UTF_8);
//		//创建请求
//		var request = (HttpWebRequest)HttpWebRequest.Create(serverUrl);
//		request.Method = "POST";
//		request.ContentLength = dataArray.Length;
//		//设置上传服务的数据格式  设置之后不好使
//		//request.ContentType = "application/x-www-form-urlencoded";
//		//请求的身份验证信息为默认
//		request.Credentials = CredentialCache.DefaultCredentials;
//		// request.ContentType = "application/x-www-form-urlencoded";
//		request.ContentType = "application/json";
//		//  request.ContentType = "application/x-www-form-urlencoded";
//
//		//设置请求头
//		if (!headerMap.isEmpty())
//		{
//			for (Object key : headerMap.keySet())
//			{
//				request.Headers.Add(key, headerMap.get(key).toString());
//			}
//		}
//
//		//请求超时时间
//		request.Timeout = 10000;
//		//创建输入流
////C# TO JAVA CONVERTER TODO TASK: C# to Java Converter cannot determine whether this System.IO.Stream is input or output:
//		Stream dataStream;
//		try
//		{
//			dataStream = request.GetRequestStream();
//		}
//		catch (RuntimeException e)
//		{
//			return "0"; //连接服务器失败
//		}
//		//发送请求
//		dataStream.Write(dataArray, 0, dataArray.Length);
//		dataStream.Close();
//
//		HttpWebResponse res;
//
//		res = (HttpWebResponse)request.GetResponse();
//
//		//}
//		//catch (WebException ex)
//		//{
//		//    throw new Exception(ex.Message);
//		//    //res = (HttpWebResponse)ex.Response;
//		//}
//		InputStreamReader sr = new InputStreamReader(res.GetResponseStream(), java.nio.charset.StandardCharsets.UTF_8);
//		//读取返回消息
//		String data = sr.ReadToEnd();
//		sr.close();
//		return data;
//	}
	public static String FormatMoney(String money)
	{
		//处理带有负号情况
		int negNumber = money.indexOf("-");
		String prefix = "";
		if (negNumber != -1)
		{
			prefix = "-";
			money = money.substring(1);
		}
		//处理有小数位情况
		int decNumber = money.indexOf(".");
		String postfix = "";
		if (decNumber != -1)
		{
			postfix = money.substring(decNumber);
			money = money.substring(0, decNumber - 1);
		}
		else
		{
			postfix = ".00";
		}
		//开始添加","号
		if (money.length() > 3)
		{
			String str1 = money.substring(0, money.length() - 3);
			String str2 = StringHelper.substring(money, money.length() - 3, 3);
			if (str1.length() > 3)
			{
				return prefix + FormatMoney(str1) + "," + str2 + postfix;
			}
			else
			{
				return prefix + str1 + "," + str2 + postfix;
			}
		}
		else
		{
			return prefix + money + postfix;
		}
	}
	/**
	 * 获得ftp连接对象
	 *
	 * @throws Exception
	 */
		public static FtpUtil getFtpUtil() throws Exception {
			// 获取
			String ip = bp.sys.Glo.String_JieMi_FTP(SystemConfig.getFTPServerIP());

			String userNo = bp.sys.Glo.String_JieMi_FTP(SystemConfig.getFTPUserNo());
			String pass = bp.sys.Glo.String_JieMi_FTP(SystemConfig.getFTPUserPassword());
			String port=bp.sys.Glo.String_JieMi_FTP(String.valueOf(SystemConfig.getFTPServerPort()));

			if(DataType.IsNullOrEmpty(port)){
				port="21";
			}

			FtpUtil ftp = new FtpUtil(ip, Integer.parseInt(port), userNo, pass);
			return ftp;

		// return Platform.JFlow;
	}
	/**
	 * 获得ftp连接对象
	 *
	 * @throws Exception
	 */
	public static SftpUtil getSftpUtil() throws Exception {
		// 获取
		String ip = SystemConfig.getFTPServerIP();

		String userNo = SystemConfig.getFTPUserNo();
		String pass = bp.sys.base.Glo.String_JieMi_FTP(SystemConfig.getFTPUserPassword());

		SftpUtil ftp = new SftpUtil(ip, SystemConfig.getFTPServerPort(), userNo, pass);
		return ftp;

	}
}

	///#endregion http请求
