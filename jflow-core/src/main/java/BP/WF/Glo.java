package BP.WF;

import BP.Sys.*;
import BP.Tools.StringHelper;
import BP.DA.*;
import BP.En.*;
import BP.Web.*;
import BP.Port.*;
import BP.WF.Data.*;
import BP.WF.Template.*;
import java.util.*;
import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.math.*;

/** 
 全局(方法处理)
*/
public class Glo
{

	public static String GenerGanttDataOfSubFlows(long workID)
	{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");;
		GenerWorkFlow gwf = new GenerWorkFlow(workID);

		//增加子流程数据.
		GenerWorkFlows gwfs = new GenerWorkFlows();
		gwfs.Retrieve("PWorkID", workID);
		String json = "[";


		//主流程的计划完成日期，与实际完成日期的两个时间段.
		json += " { id:'001', name:'总体计划', ";
		json += "series:[{ ";
		json += " name: '项目计划', ";
		json += " start:  " + ToData(gwf.getRDTOfSetting()) + ", ";
		json += " end: " + ToData(gwf.getSDTOfFlow()) + ",";
		json += " TodoSta: " + gwf.getTodoSta() + ", ";
		json += " color: 'blue' ";
		json += "},";

		json += "{ name: '实际执行', ";
		json += " start:  " + ToData(gwf.getRDT()) + ",";
		if (gwf.getWFState() == WFState.Complete)
		{
			json += " end: " + ToData(gwf.getSendDT()) + ",";
		}
		else
		{
			json += " end:' " + LocalDateTime.now().format(formatter) + "',";
		}

		json += " TodoSta: " + gwf.getTodoSta() + ",";

		if (gwf.getWFSta() == WFSta.Complete) //流程运行结束
		{
			if (ToData(gwf.getSendDT()).compareTo(ToData(gwf.getSDTOfFlow())) <= 0) //正常
			{
				json += " color: 'green' ";
			}
			else
			{
				json += " color: 'red' ";
			}
		}
		else //未完成
		{
			String sendDt = BP.DA.DataType.ParseSysDate2DateTime(gwf.getSendDT()).format("yyyy-MM-dd");
			String wadingDtOfF = DataType.AddDays(sendDt, 3, TWay.Holiday).format("yyyy-MM-dd");
			if (sendDt.compareTo(ToData(gwf.getSDTOfFlow())) > 0) //逾期
			{
				json += " color: 'red' ";
			}
			else
			{
				if (wadingDtOfF.compareTo(ToData(gwf.getSDTOfFlow())) > 0)
				{
					json += " color: 'yellow' ";
				}
				else
				{
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
		if (dtYL.Rows.size() != 0)
		{
			for (Node nd : nds.ToJavaList())
			{
				if (nd.GetParaBoolen("IsYouLiTai") == true)
				{
					if (isFirstYLT == true)
					{
						for (DataRow dr : dtYL.Rows)
						{
							nodes.AddEntity(nds.GetEntityByKey(Integer.parseInt(dr.get("NodeID").toString())));
						}
					}

					isFirstYLT = false;
					continue;
				}
				nodes.AddEntity(nd);
			}
		}
		else
		{
			nodes.AddEntities(nds);
		}



		SubFlows subs = new SubFlows();
		String trackTable = "ND" + Integer.parseInt(gwf.getFK_Flow()) + "Track";
		sql = "SELECT FID \"FID\",NDFrom \"NDFrom\",NDFromT \"NDFromT\",NDTo  \"NDTo\", NDToT \"NDToT\", ActionType \"ActionType\",ActionTypeText \"ActionTypeText\",Msg \"Msg\",RDT \"RDT\",EmpFrom \"EmpFrom\",EmpFromT \"EmpFromT\", EmpToT \"EmpToT\",EmpTo \"EmpTo\" FROM " + trackTable +
			  " WHERE WorkID=" + gwf.getWorkID() + " ORDER BY RDT ASC";

		DataTable dt = DBAccess.RunSQLReturnTable(sql);



		int idxNode = 0;
		for (Node nd : nodes.ToJavaList())
		{
			idxNode++;

		   String[] dtArray = GetNodeRealTime(dt, nd.getNodeID());
			if (nd.getIsStartNode() == true)
			{
				continue;
			}

			//里程碑.
			json += " { id:'" + nd.getNodeID() + "', name:'" + nd.getName() + "', ";
			json += "series:[{ ";
			json += " name: '计划', ";
			json += " start:  " + ToData(gwf.GetParaString("PlantStartDt" + nd.getNodeID())) + ", ";
			json += " end: " + ToData(gwf.GetParaString("CH" + nd.getNodeID())) + ",";
			json += " TodoSta: " + gwf.getTodoSta() + ", ";
			json += " color: 'blue' ";
			json += "},";

			json += "{ name: '实际执行', ";
			if (nd.getIsStartNode() == true)
			{
				json += " start:  " + ToData(gwf.getRDT()) + ",";
				json += " end: " + ToData(dtArray[1]) + ",";
				json += " TodoSta: " + gwf.getTodoSta() + ",";
				json += " color: 'green' ";
			}
			else
			{
				String start = "";
				String end = "";
				boolean isPass = false;
				json += " start:  " + ToData(dtArray[0]) + ",";
				

				if (DataType.IsNullOrEmpty(dtArray[1]) == true && DataType.IsNullOrEmpty(dtArray[0]) == true)
				{
					json += " end:'',";
				}
				else
				{
					if (DataType.IsNullOrEmpty(dtArray[1]) == false)
					{
						isPass = true;
					}
					json += " end: " + ToData(dtArray[1]) + ",";
					end = BP.DA.DataType.ParseSysDate2DateTime(dtArray[1]).toString("yyyy-MM-dd");
				}

				json += " TodoSta: " + gwf.getTodoSta() + ",";
				String plantCHDt = ToData(gwf.GetParaString("CH" + nd.getNodeID()));
				if (isPass && end.compareTo(plantCHDt) <= 0)
				{
					json += " color: 'green' "; //正常
				}

				if (isPass && end.compareTo(plantCHDt) > 0)
				{
					json += " color: 'red' "; //逾期
				}
				if (isPass == false)
				{
					if (DataType.IsNullOrEmpty(end) == true)
					{
						end = LocalDateTime.now().format(formatter);
					}
					if (end.compareTo(plantCHDt) > 0)
					{
						json += " color: 'red' ";
					}
					// 预警计算
					String warningDt = DataType.AddDays(end, 3, TWay.Holiday).toString("yyyy-MM-dd");
					if (warningDt.compareTo(plantCHDt) >= 0)
					{
						json += " color: 'yellow' ";
					}
					else
					{
						json += " color: 'green' ";
					}
				}


			}

			json += "}]},";

			//获取子流程
			subs = new SubFlows(nd.getNodeID());
			if (subs.size() == 0)
				continue;

			String series = "";
			for (SubFlow sub : subs.ToJavaList())
			{
				if (sub.getFK_Node() != nd.getNodeID())
				{
					continue;
				}
				json += " { id:'" + sub.getFK_Node() + "', name:'" + sub.getSubFlowNo() + " - " + sub.getSubFlowName() + "',series:[ ";
				//增加子流成.
				int idx = 0;
				String dtlsSubFlow = "";
				//获取流程启动的开始和结束
				long firstStartWorkID = 0;
				long endStartWorkID = 0;
				GenerWorkFlow firstStartGwf = null;
				GenerWorkFlow endStartGwf = null;
				boolean IsFirst = true;
				for (GenerWorkFlow subGWF : gwfs.ToJavaList())
				{
					if (!subGWF.getFK_Flow().equals(sub.getSubFlowNo()))
					{
						continue;
					}
					if (IsFirst == true)
					{
						firstStartWorkID = subGWF.getWorkID();
						endStartWorkID = subGWF.getWorkID();
						firstStartGwf = subGWF;
						endStartGwf = subGWF;
						continue;

					}
					if (firstStartWorkID > subGWF.getWorkID())
					{
						firstStartWorkID = subGWF.getWorkID();
						firstStartGwf = subGWF;
					}

					if (endStartWorkID < subGWF.getWorkID())
					{
						endStartWorkID = subGWF.getWorkID();
						endStartGwf = subGWF;
					}

				}

				//没有启动子流程
				if (firstStartWorkID == 0)
				{
					json += "{ ";
					json += " name: '" + sub.getSubFlowNo() + " - " + sub.getSubFlowName() + "', ";
					json += " start:  " + ToData(DataType.getCurrentDate()) + ", ";
					json += " end:  " + ToData(DataType.getCurrentDate()) + ", ";
					json += " TodoSta: -1, ";
					json += " color: 'brue' ";
					json += "}";
				}
				//子流程被启动了一次
				if (firstStartWorkID != 0 && firstStartWorkID == endStartWorkID)
				{
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
					if (firstStartGwf.getWFState() == WFState.Complete)
					{
						json += " end: " + ToData(firstStartGwf.getSendDT()) + ",";
					}
					else
					{
						json += " end: '" + LocalDateTime.now().format(formatter) + "',";
					}

					json += " TodoSta: " + firstStartGwf.getTodoSta() + ", ";

					if (firstStartGwf.getWFSta() == WFSta.Complete) //流程运行结束
					{
						if (ToData(firstStartGwf.getSendDT()).compareTo(ToData(firstStartGwf.getSDTOfFlow())) <= 0) //正常
						{
							json += " color: '#66ff66' "; //绿色
						}
						else
						{
							json += " color: '#ff8888' "; //红色
						}
					}
					else //未完成
					{
						String sendDt = BP.DA.DataType.ParseSysDate2DateTime(firstStartGwf.getSendDT()).toString("yyyy-MM-dd");
						String wadingDtOfF = DataType.AddDays(sendDt, 3, TWay.Holiday).toString("yyyy-MM-dd");
						if (sendDt.compareTo(ToData(firstStartGwf.getSDTOfFlow())) > 0) //逾期
						{
							json += " color: '#ff8888' "; //红色
						}
						else
						{
							if (wadingDtOfF.compareTo(ToData(firstStartGwf.getSDTOfFlow())) > 0)
							{
								json += " color: '#ffff77' "; //黄色
							}
							else
							{
								json += " color: '#66ff66' "; //绿色
							}
						}


					}
					json += "}";
				}
				//子流程被启动了多次
				if (firstStartWorkID != 0 && firstStartWorkID != endStartWorkID)
				{
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
					if (endStartGwf.getWFState() == WFState.Complete)
					{
						json += " end: " + ToData(endStartGwf.getSendDT()) + ",";
					}
					else
					{
						json += " end: '" + LocalDateTime.now().format(formatter) + "',";
					}

					json += " TodoSta: " + endStartGwf.getTodoSta() + ", ";

					if (endStartGwf.getWFSta() == WFSta.Complete) //流程运行结束
					{
						if (ToData(endStartGwf.getSendDT()).compareTo(ToData(firstStartGwf.getSDTOfFlow())) <= 0) //正常
						{
							json += " color: '#66ff66' "; //绿色
						}
						else
						{
							json += " color: '#ff8888' "; //红色
						}
					}
					else //未完成
					{
						String sendDt = BP.DA.DataType.ParseSysDate2DateTime(endStartGwf.getSendDT()).toString("yyyy-MM-dd");
						String wadingDtOfF = DataType.AddDays(sendDt, 3, TWay.Holiday).toString("yyyy-MM-dd");
						if (sendDt.compareTo(ToData(firstStartGwf.getSDTOfFlow())) > 0) //逾期
						{
							json += " color: '#ff8888' "; //红色
						}
						else
						{
							if (wadingDtOfF.compareTo(ToData(firstStartGwf.getSDTOfFlow())) > 0)
							{
								json += " color: '#ffff77' "; //黄色
							}
							else
							{
								json += " color: '#66ff66' "; //绿色
							}
						}


					}

					json += "}";
				}
				json += "]},";

			}

		}
		json = json.substring(0, json.length() - 1);
		json += "]";

		return json;
	}

	public static String[] GetNodeRealTime(DataTable dt, int nodeID)
	{
		String startDt = "";
		String endDt = "";
		for (DataRow dr : dt.Rows)
		{
			int NDFrom = Integer.parseInt(dr.get("NDFrom").toString());
			if (NDFrom == nodeID)
			{
				endDt = dr.get("RDT").toString();
				break;
			}


		}
		for (DataRow dr : dt.Rows)
		{
			int NDTo = Integer.parseInt(dr.get("NDTo").toString());
			if (NDTo == nodeID)
			{
				if (DataType.IsNullOrEmpty(endDt) == true)
				{
					startDt = dr.get("RDT").toString();
					break;
				}
				if (dr.get("RDT").toString().compareTo(endDt) <= 0)
				{
					startDt = dr.get("RDT").toString();
					break;
				}
			}


		}
		String[] dtArray = {startDt, endDt};
		return dtArray;
	}

	/** 
	 生成甘特图
	 
	 @param workID
	 @return 
	*/
	public static String GenerGanttDataOfSubFlowsV20(long workID)
	{
		GenerWorkFlow gwf = new GenerWorkFlow(workID);
		//增加子流程数据.
		GenerWorkFlows gwfs = new GenerWorkFlows();
		gwfs.Retrieve("PWorkID", workID);

		String json = "[";

		json += " { id:'" + gwf.getFK_Flow() + "', name:'" + gwf.getFlowName() + "',";

		json += " series:[";
		json += "{ name: \"计划时间\", start:  " + ToData(gwf.getSDTOfFlow()) + ", end: " + ToData(gwf.getSDTOfFlow()) + ", color: \"#f0f0f0\" },";
		json += "{ name: \"实际工作时间\", start: " + ToData(gwf.getRDT()) + ", end: " + ToData(gwf.getSendDT()) + " , color: \"#f0f0f0\" }";
		json += "]";

		if (gwfs.size() == 0)
		{
			json += "}";
			json += "]";
			return json;
		}
		else
		{
			json += "},";
		}

		//增加子流成.
		int idx = 0;
		for (GenerWorkFlow subGWF : gwfs.ToJavaList())
		{
			idx++;

			json += " { id:'" + subGWF.getFK_Flow() + "', name:'" + subGWF.getFlowName() + "',";

			json += " series:[";
			json += "{ name: \"实际工作时间\", start:  " + ToData(gwf.getRDT()) + ", end: " + ToData(gwf.getSendDT()) + " }";
			json += "]";

			if (idx == gwfs.size())
			{
				json += "}";
				json += "]";
				return json;
			}
			else
			{
				json += "},";
			}
		}

		json += "]";

		return json;
	}

	public static String ToData(String dtStr)
	{

		LocalDateTime dt = DataType.ParseSysDate2DateTime(dtStr);

		return "'" + dt.toString("yyyy-MM-dd") + "'";

	}
	/** 
	 生成甘特图
	 
	 @return 
	*/
	public static String GenerGanttDataOfSubFlowsV1(long workID)
	{
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
		dr.set("id", gwf.getFK_Flow());
		dr.set("name", gwf.getFlowName());
		dtFlows.Rows.add(dr);

		DataRow drItem = dtSeries.NewRow();
		drItem.set("name", "项目计划日期");
		drItem.set("start", gwf.getRDT());
		drItem.set("end", gwf.getSDTOfFlow());
		drItem.set("color", "#f0f0f0");
		drItem.set("RefPK", gwf.getFK_Flow());
		dtSeries.Rows.add(drItem);

		drItem = dtSeries.NewRow();
		drItem.set("name", "项目启动日期");
		drItem.set("start", gwf.getRDT());
		drItem.set("end", gwf.getSDTOfFlow());
		drItem.set("color", "#f0f0f0");
		drItem.set("RefPK", gwf.getFK_Flow());
		dtSeries.Rows.add(drItem);


		//增加子流程数据.
		GenerWorkFlows gwfs = new GenerWorkFlows();
		gwfs.Retrieve("PWorkID", workID);

		for (GenerWorkFlow subFlow : gwfs.ToJavaList())
		{
			dr = dtFlows.NewRow();
			dr.set("id", subFlow.getFK_Flow());
			dr.set("name", subFlow.getFlowName());
			dtFlows.Rows.add(dr);


			drItem = dtSeries.NewRow();
			drItem.set("name", "启动日期");
			drItem.set("start", subFlow.getRDT());
			drItem.set("end", subFlow.getSDTOfFlow());
			drItem.set("color", "#f0f0f0");
			drItem.set("RefPK", subFlow.getFK_Flow());
			dtSeries.Rows.add(drItem);
		}

		DataSet ds = new DataSet();
		ds.Tables.add(dtFlows);
		ds.Tables.add(dtSeries);

		return ToJsonOfGantt(ds);
	}

		///#region 多语言处理.
	private static Hashtable _Multilingual_Cache = null;
	public static DataTable getMultilingual_DT(String className)
	{
		if (_Multilingual_Cache == null)
		{
			_Multilingual_Cache = new Hashtable();
		}

		if (_Multilingual_Cache.containsKey(className) == false)
		{
			DataSet ds = DataType.CXmlFileToDataSet(BP.Sys.SystemConfig.getPathOfData() + "/lang/xml/" + className + ".xml");
			DataTable dt = ds.Tables.get(0);

			_Multilingual_Cache.put(className, dt);
		}

		return _Multilingual_Cache.get(className) instanceof DataTable ? (DataTable)_Multilingual_Cache.get(className) : null;
	}
	/** 
	 转换语言.
	*/

	public static String multilingual(String defaultMsg, String className, String key, String p0, String p1, String p2)
	{
		return multilingual(defaultMsg, className, key, p0, p1, p2, null);
	}

	public static String multilingual(String defaultMsg, String className, String key, String p0, String p1)
	{
		return multilingual(defaultMsg, className, key, p0, p1, null, null);
	}

	public static String multilingual(String defaultMsg, String className, String key, String p0)
	{
		return multilingual(defaultMsg, className, key, p0, null, null, null);
	}

	public static String multilingual(String defaultMsg, String className, String key)
	{
		return multilingual(defaultMsg, className, key, null, null, null, null);
	}

	public static String multilingual(String defaultMsg, String className, String key, String p0, String p1, String p2, String p3)
	{
		int num = 0;
		if (p0 == null)
		{
			num = 0;
		}
		if (p1 == null)
		{
			num = 1;
		}
		if (p2 == null)
		{
			num = 2;
		}
		if (p3 == null)
		{
			num = 3;
		}

		String[] paras = new String[num];
		if (p0 != null)
		{
			paras[0] = p0;
		}

		if (p1 != null)
		{
			paras[1] = p1;
		}

		if (p2 != null)
		{
			paras[2] = p2;
		}

		if (p3 != null)
		{
			paras[3] = p3;
		}

		return multilingual(defaultMsg, className, key, paras);
	}
	/** 
	 获取多语言
	 
	 @param lang
	 @param key
	 @param paramList
	 @return 
	*/
	public static String multilingual(String defaultMsg, String className, String key, String[] paramList)
	{
		if (WebUser.getSysLang().equals("zh-cn") || WebUser.getSysLang().equals("CH"))
		{
			return String.format(defaultMsg, paramList);
		}


		DataTable dt = getMultilingual_DT(className);

		String val = "";
		for (DataRow dr : dt.Rows)
		{
			if (key.equals((String)dr.ItemArray[0]))
			{
				switch (WebUser.SysLang)
				{
					case "zh-cn":
						val = (String)dr.ItemArray[1];
						break;
					case "zh-tw":
						val = (String)dr.ItemArray[2];
						break;
					case "zh-hk":
						val = (String)dr.ItemArray[3];
						break;
					case "en-us":
						val = (String)dr.ItemArray[4];
						break;
					case "ja-jp":
						val = (String)dr.ItemArray[5];
						break;
					case "ko-kr":
						val = (String)dr.ItemArray[6];
						break;
					default:
						val = (String)dr.ItemArray[1];
						break;
				}
				break;
			}
		}
		return String.format(val, paramList);
	}


	/** 
	 打印文件
	*/
	public static String getPrintBackgroundWord()
	{
		String s = (String) SystemConfig.getAppSettings().get("PrintBackgroundWord");
		if (DataType.IsNullOrEmpty(s))
		{
			s = "驰骋工作流引擎@开源驰骋 - ccflow@openc";
		}
		return s;
	}
	/** 
	 运行平台.
	*/
	public static Platform getPlatform()
	{
		return Platform.CCFlow;
	}

	/** 
	 短消息写入类型
	*/
	public static ShortMessageWriteTo getShortMessageWriteTo()
	{
		return ShortMessageWriteTo.forValue(BP.Sys.SystemConfig.GetValByKeyInt("ShortMessageWriteTo", 0));
	}
	/** 
	 当前选择的流程.
	*/
	public static String getCurrFlow()
	{
		Object tempVar = HttpContextHelper.SessionGet("CurrFlow");
		return tempVar instanceof String ? (String)tempVar : null;
	}
	public static void setCurrFlow(String value)
	{
		HttpContextHelper.SessionSet("CurrFlow", value);
	}
	/** 
	 用户编号.
	*/
	public static String UserNo = null;
	/** 
	 运行平台(用于处理不同的平台，调用不同的URL)
	*/
	public static Plant Plant = BP.WF.Plant.CCFlow;

	
	/** 
	 当前版本号-为了升级使用.
	*/
	public static int Ver = 20190915;
	/** 
	 执行升级
	 
	 @return 
	*/
	public static String UpdataCCFlowVer()
	{
		///#region 检查是否需要升级，并更新升级的业务逻辑.
		String updataNote = "";

		/*
		 * 升级版本记录:
		 * 20150330: 优化发起列表的效率, by:zhoupeng.
		 * 2, 升级表单树,支持动态表单树.
		 * 1, 执行一次Sender发送人的升级，原来由GenerWorkerList 转入WF_GenerWorkFlow.
		 * 0, 静默升级启用日期.2014-12
		 */

		if (BP.DA.DBAccess.IsExitsObject("Sys_Serial") == false)
		{
			return "";
		}

		//检查子流程表.
		if (BP.DA.DBAccess.IsExitsObject("WF_NodeSubFlow") == true)
		{
			if (BP.DA.DBAccess.IsExitsTableCol("WF_NodeSubFlow", "OID") == true)
			{
				DBAccess.RunSQL("DROP TABLE WF_NodeSubFlow");
				SubFlowHand sub = new SubFlowHand();
				sub.CheckPhysicsTable();
			}
		}



		//检查表.
		BP.Sys.GloVar gv = new GloVar();
		gv.CheckPhysicsTable();

		//检查表.
		BP.Sys.EnCfg cfg = new BP.Sys.EnCfg();
		cfg.CheckPhysicsTable();

		//检查表.
		BP.Sys.FrmTree frmTree = new BP.Sys.FrmTree();
		frmTree.CheckPhysicsTable();

		BP.Sys.SFTable sf = new SFTable();
		sf.CheckPhysicsTable();

		BP.WF.Template.FrmSubFlow sb = new FrmSubFlow();
		sb.CheckPhysicsTable();


		BP.WF.Template.PushMsg pm = new PushMsg();
		pm.CheckPhysicsTable();

		//修复数据表.
		BP.Sys.GroupField gf = new GroupField();
		gf.CheckPhysicsTable();

		//先升级脚本,就是说该文件如果被修改了就会自动升级.
		UpdataCCFlowVerSQLScript();

		//判断数据库的版本.
		String sql = "SELECT IntVal FROM Sys_Serial WHERE CfgKey='Ver'";
		int currDBVer = DBAccess.RunSQLReturnValInt(sql, 0);
		if (currDBVer != 0 && currDBVer >= Ver)
		{
			return null; //不需要升级.
		}

		///#endregion 检查是否需要升级，并更新升级的业务逻辑.

			///#region 枚举值
		SysEnumMains enumMains = new SysEnumMains();
		enumMains.RetrieveAll();
		for (SysEnumMain enumMain : enumMains.ToJavaList())
		{
			SysEnums ens = new SysEnums();
			ens.Delete(SysEnumAttr.EnumKey, enumMain.getNo());

			//保存数据
			String cfgVal = enumMain.getCfgVal();
			String[] strs = cfgVal.split("[@]", -1);
			for (String s : strs)
			{
				if (s.equals("") || s == null)
				{
					continue;
				}

				String[] vk = s.split("[=]", -1);
				SysEnum se = new SysEnum();
				se.setIntKey(Integer.parseInt(vk[0]));
				String[] kvsValues = new String[vk.length - 1];
				for (int i = 0; i < kvsValues.length; i++)
				{
					kvsValues[i] = vk[i + 1];
				}
				se.setLab(StringHelper.join("=", kvsValues));
				se.setEnumKey(enumMain.getNo());
				se.setLang(WebUser.getSysLang());
				se.Insert();
			}
		}

		///#region 升级视图. 解决厦门信息港的 - 流程监控与授权.
		if (DBAccess.IsExitsObject("V_MyFlowData") == false)
		{
			BP.WF.Template.PowerModel pm11 = new PowerModel();
			pm11.CheckPhysicsTable();

			sql = "CREATE VIEW V_MyFlowData ";
			sql += " AS ";
			sql += " SELECT A.*, c.No as MyEmpNo FROM WF_GenerWorkflow A, WF_PowerModel B, Port_Emp C ";
			sql += " WHERE  A.FK_Flow=B.FlowNo AND B.EmpNo=C.No AND B.PowerCtrlType=1";
			sql += "    UNION  ";
			sql += " SELECT A.*, c.No as MyEmpNo FROM WF_GenerWorkflow A, WF_PowerModel B, Port_Emp C, Port_DeptEmpStation D";
			sql += " WHERE  A.FK_Flow=B.FlowNo AND B.EmpNo=C.No AND B.PowerCtrlType=0 AND C.No=D.FK_Emp AND B.StaNo=D.FK_Station";
			DBAccess.RunSQL(sql);
		}

		//@sly 安装包弄好了吗？
		if (DBAccess.IsExitsObject("V_WF_AuthTodolist") == false)
		{
			BP.WF.Template.PowerModel pm11 = new PowerModel();
			pm11.CheckPhysicsTable();

			sql = "CREATE VIEW V_WF_AuthTodolist ";
			sql += " AS ";
			sql += " SELECT A.*, c.No as MyEmpNo FROM WF_GenerWorkflow A, WF_PowerModel B, Port_Emp C ";
			sql += " WHERE  A.FK_Flow=B.FlowNo AND B.EmpNo=C.No AND B.PowerCtrlType=1";
			sql += "    UNION  ";
			sql += " SELECT A.*, c.No as MyEmpNo FROM WF_GenerWorkflow A, WF_PowerModel B, Port_Emp C, Port_DeptEmpStation D";
			sql += " WHERE  A.FK_Flow=B.FlowNo AND B.EmpNo=C.No AND B.PowerCtrlType=0 AND C.No=D.FK_Emp AND B.StaNo=D.FK_Station";
			DBAccess.RunSQL(sql);
		}
		///#endregion 升级视图.

		//升级从表的 fk_node .
		//获取需要修改的从表
		String dtlNos = DBAccess.RunSQLReturnString("SELECT B.NO  FROM SYS_GROUPFIELD A, SYS_MAPDTL B WHERE A.CTRLTYPE='Dtl' AND A.CTRLID=B.NO AND FK_NODE!=0");
		if (DataType.IsNullOrEmpty(dtlNos) == false)
		{
			dtlNos = dtlNos.replace(",", "','");
			dtlNos = "('" + dtlNos + "')";
			DBAccess.RunSQL("UPDATE SYS_MAPDTL SET FK_NODE=0 WHERE NO IN " + dtlNos);
		}

		///#region 升级填充数据.
		//pop自动填充
		MapExts exts = new MapExts();
		QueryObject qo = new QueryObject(exts);
		qo.AddWhere(MapExtAttr.ExtType, " LIKE ", "Pop%");
		qo.DoQuery();
		for (MapExt ext : exts.ToJavaList())
		{
			String mypk = ext.getFK_MapData() + "_" + ext.getAttrOfOper();
			MapAttr ma = new MapAttr();
			ma.setMyPK(mypk);
			if (ma.RetrieveFromDBSources() == 0)
			{
				ext.Delete();
				continue;
			}

			if (ma.GetParaString("PopModel").equals(ext.getExtType()))
			{
				continue; //已经修复了，或者配置了.
			}

			ma.SetPara("PopModel", ext.getExtType());
			ma.Update();

			if (DataType.IsNullOrEmpty(ext.getTag4()) == true)
			{
				continue;
			}

			MapExt extP = new MapExt();
			extP.setMyPK(ext.getMyPK() + "_FullData");
			int i = extP.RetrieveFromDBSources();
			if (i == 1)
			{
				continue;
			}

			extP.setExtType("FullData");
			extP.setFK_MapData(ext.getFK_MapData());
			extP.setAttrOfOper(ext.getAttrOfOper());
			extP.setDBType(ext.getDBType());
			extP.setDoc(ext.getTag4());
			extP.Insert(); //执行插入.
		}


		//文本自动填充
		exts = new MapExts();
		exts.Retrieve(MapExtAttr.ExtType, MapExtXmlList.TBFullCtrl);
		for (MapExt ext : exts.ToJavaList())
		{
			String mypk = ext.getFK_MapData() + "_" + ext.getAttrOfOper();
			MapAttr ma = new MapAttr();
			ma.setMyPK(mypk);
			if (ma.RetrieveFromDBSources() == 0)
			{
				ext.Delete();
				continue;
			}
			String modal = ma.GetParaString("TBFullCtrl");
			if (DataType.IsNullOrEmpty(modal) == false)
			{
				continue; //已经修复了，或者配置了.
			}

			if (DataType.IsNullOrEmpty(ext.getTag3()) == false)
			{
				ma.SetPara("TBFullCtrl", "Simple");
			}
			else
			{
				ma.SetPara("TBFullCtrl", "Table");
			}

			ma.Update();

			if (DataType.IsNullOrEmpty(ext.getTag4()) == true)
			{
				continue;
			}

			MapExt extP = new MapExt();
			extP.setMyPK( ext.getMyPK() + "_FullData");
			int i = extP.RetrieveFromDBSources();
			if (i == 1)
			{
				continue;
			}

			extP.setExtType("FullData");
			extP.setFK_MapData(ext.getFK_MapData());
			extP.setAttrOfOper(ext.getAttrOfOper());
			extP.setDBType(ext.getDBType());
			extP.setDoc(ext.getTag4());

			//填充从表
			extP.setTag1(ext.getTag1());
			//填充下拉框
			extP.setTag(ext.getTag());
			extP.Insert(); //执行插入.
		}

		//下拉框填充其他控件
		//文本自动填充
		exts = new MapExts();
		exts.Retrieve(MapExtAttr.ExtType, MapExtXmlList.DDLFullCtrl);
		for (MapExt ext : exts.ToJavaList())
		{
			String mypk = ext.getFK_MapData() + "_" + ext.getAttrOfOper();
			MapAttr ma = new MapAttr();
			ma.setMyPK(mypk);
			if (ma.RetrieveFromDBSources() == 0)
			{
				ext.Delete();
				continue;
			}
			String modal = ma.GetParaString("IsFullData");
			if (DataType.IsNullOrEmpty(modal) == false)
			{
				continue; //已经修复了，或者配置了.
			}

			//启用填充其他控件
			ma.SetPara("IsFullData", 1);
			ma.Update();


			MapExt extP = new MapExt();
			extP.setMyPK( ext.getMyPK() + "_FullData");
			int i = extP.RetrieveFromDBSources();
			if (i == 1)
			{
				continue;
			}

			extP.setExtType("FullData");
			extP.setFK_MapData(ext.getFK_MapData());
			extP.setAttrOfOper(ext.getAttrOfOper());
			extP.setDBType(ext.getDBType());
			extP.setDoc(ext.getDoc());


			//填充从表
			extP.setTag1(ext.getTag1());
			//填充下拉框
			extP.setTag(ext.getTag());

			extP.Insert(); //执行插入.

		}
		///#endregion 升级 填充数据.

		String msg = "";
		try
		{
	
			//#region 创建缺少的视图 Port_Inc.  @fanleiwei 需要翻译.
			if (DBAccess.IsExitsObject("Port_Inc") == false)
			{
				sql = "CREATE VIEW Port_Inc AS SELECT * FROM Port_Dept WHERE (No='100' OR No='1060' OR No='1070') ";
				DBAccess.RunSQL(sql);
			}
				///#region 升级事件.
			if (DBAccess.IsExitsTableCol("Sys_FrmEvent", "DoType") == true)
			{
				BP.Sys.FrmEvent fe = new FrmEvent();
				fe.CheckPhysicsTable();

				DBAccess.RunSQL("UPDATE Sys_FrmEvent SET EventDoType=DoType  ");
				DBAccess.RunSQL("ALTER TABLE Sys_FrmEvent   DROP COLUMN	DoType  ");
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 修复丢失的发起人.
			Flows fls = new Flows();
			fls.RetrieveAll();
			for (Flow item : fls)
			{
				if (DBAccess.IsExitsObject(item.getPTable()) == false)
				{
					continue;
				}
				try
				{
					DBAccess.RunSQL(" UPDATE " + item.getPTable() + " SET FlowStarter =(SELECT Starter FROM WF_GENERWORKFLOW WHERE " + item.getPTable() + ".Oid=WF_GENERWORKFLOW.WORKID)");
				}
				catch (RuntimeException ex)
				{
					//   GERpt rpt=new GERpt(
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 修复丢失的发起人.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 给city 设置拼音.
			if (DBAccess.IsExitsObject("CN_City") == true && 1 == 2)
			{
				if (DBAccess.IsExitsTableCol("CN_City", "PinYin") == true)
				{
					/*为cn_city 设置拼音.*/
					sql = "SELECT No,Names FROM CN_City ";
					DataTable dtCity = DBAccess.RunSQLReturnTable(sql);

					for (DataRow dr : dtCity.Rows)
					{
						String no = dr.get("No").toString();
						String name = dr.get("Names").toString();
						String pinyin1 = DataType.ParseStringToPinyin(name);
						String pinyin2 = DataType.ParseStringToPinyinJianXie(name);
						String pinyin = "," + pinyin1 + "," + pinyin2 + ",";
						DBAccess.RunSQL("UPDATE CN_City SET PinYin='" + pinyin + "' WHERE NO='" + no + "'");
					}
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 给city 设置拼音.

			//增加列FlowStars
			WFEmp wfemp = new WFEmp();
			wfemp.CheckPhysicsTable();

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 更新wf_emp. 的字段类型. 2019.06.19
			DBType dbtype = BP.Sys.SystemConfig.getAppCenterDBType();

			if (dbtype == DBType.Oracle)
			{
				if (DBAccess.IsExitsTableCol("WF_EMP", "startFlows_temp") == false)
				{
					DBAccess.RunSQL("ALTER TABLE  WF_EMP add startFlows_temp CLOB");
				}

				if (DBAccess.IsExitsTableCol("WF_EMP", "STARTFLOWS") == false)
				{
					DBAccess.RunSQL("ALTER TABLE  WF_EMP add STARTFLOWS CLOB");
				}

				//将需要改成大字段的项内容copy到大字段中
				DBAccess.RunSQL("UPDate WF_EMP set startFlows_temp=STARTFLOWS");
				//删除原有字段
				DBAccess.RunSQL("ALTER TABLE  WF_EMP drop column STARTFLOWS");
				//将大字段名改成原字段名
				DBAccess.RunSQL("ALTER TABLE  WF_EMP rename column startFlows_temp to STARTFLOWS");
			}
			if (dbtype == DBType.MySQL)
			{
				DBAccess.RunSQL("ALTER TABLE WF_Emp modify StartFlows longtext ");
			}
			if (dbtype == DBType.MSSQL)
			{
				DataTable dtYueSu = DBAccess.RunSQLReturnTable("SELECT b.name, a.name FName from sysobjects b join syscolumns a on b.id = a.cdefault where a.id = object_id('WF_Emp') and a.Name='StartFlows' ");
				if (dtYueSu.Rows.size() != 0)
				{
					DBAccess.RunSQL(" ALTER TABLE WF_Emp drop  constraint " + dtYueSuget(0).getValue(0));
				}

				DBAccess.RunSQL(" ALTER TABLE WF_Emp ALTER column  StartFlows text");
			}

			if (dbtype == DBType.PostgreSQL)
			{
				//  DBAccess.RunSQL(" ALTER TABLE WF_Emp ALTER column StartFlows type text");
			}


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 更新wf_emp 的字段类型.

			BP.Sys.FrmRB rb = new FrmRB();
			rb.CheckPhysicsTable();

			CC ccEn = new CC();
			ccEn.CheckPhysicsTable();

			BP.WF.Template.MapDtlExt dtlExt = new MapDtlExt();
			dtlExt.CheckPhysicsTable();

			MapData mapData = new MapData();
			mapData.CheckPhysicsTable();

			//删除枚举.
			DBAccess.RunSQL("DELETE FROM Sys_Enum WHERE EnumKey IN ('SelectorModel','CtrlWayAth')");

			//SysEnum se = new SysEnum("FrmType", 1);//NOTE:此处升级时报错，2017-06-13，liuxc

			//2017.5.19 打印模板字段修复
			BP.WF.Template.BillTemplate bt = new BillTemplate();
			bt.CheckPhysicsTable();
			if (DBAccess.IsExitsTableCol("WF_BillTemplate", "url") == true)
			{
				DBAccess.RunSQL("UPDATE WF_BillTemplate SET TempFilePath = Url WHERE TempFilePath IS null");
			}

			//规范升级根目录.
			DataTable dttree = DBAccess.RunSQLReturnTable("SELECT No FROM Sys_FormTree WHERE ParentNo='-1' ");
			if (dttree.Rows.size() == 1)
			{
				DBAccess.RunSQL("UPDATE Sys_FormTree SET ParentNo='1' WHERE ParentNo='0' ");
				DBAccess.RunSQL("UPDATE Sys_FormTree SET No='1' WHERE No='0'  ");
				DBAccess.RunSQL("UPDATE Sys_FormTree SET ParentNo='0' WHERE No='1'");
			}

			MapAttr myattr = new MapAttr();
			myattr.CheckPhysicsTable();

			//删除垃圾数据.
			BP.Sys.MapExt.DeleteDB();

			//升级傻瓜表单.
			MapFrmFool mff = new MapFrmFool();
			mff.CheckPhysicsTable();

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 表单方案中的不可编辑, 旧版本如果包含了这个列.
			if (BP.DA.DBAccess.IsExitsTableCol("WF_FrmNode", "IsEdit") == true)
			{
				/*如果存在这个列,就查询出来=0的设置，就让其设置为不可以编辑的。*/
				sql = "UPDATE WF_FrmNode SET FrmSln=1 WHERE IsEdit=0 ";
				DBAccess.RunSQL(sql);

				sql = "UPDATE WF_FrmNode SET IsEdit=100000";
				DBAccess.RunSQL(sql);
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

			//执行升级 2016.04.08 
			Cond cnd = new Cond();
			cnd.CheckPhysicsTable();

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region  增加week字段,方便按周统计.
			BP.WF.GenerWorkFlow gwf = new GenerWorkFlow();
			gwf.CheckPhysicsTable();
			sql = "SELECT WorkID,RDT FROM WF_GenerWorkFlow WHERE WeekNum=0 or WeekNum is null ";
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			for (DataRow dr : dt.Rows)
			{
				sql = "UPDATE WF_GenerWorkFlow SET WeekNum=" + BP.DA.DataType.CurrentWeekGetWeekByDay(dr.get(1).toString().replace("+", " ")) + " WHERE WorkID=" + dr.get(0).toString();
				BP.DA.DBAccess.RunSQL(sql);
			}

			//查询.
			BP.WF.Data.CH ch = new CH();
			ch.CheckPhysicsTable();

			sql = "SELECT MyPK,DTFrom FROM WF_CH WHERE WeekNum=0 or WeekNum is null ";
			dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			for (DataRow dr : dt.Rows)
			{
				sql = "UPDATE WF_CH SET WeekNum=" + BP.DA.DataType.CurrentWeekGetWeekByDay(dr.get(1).toString()) + " WHERE MyPK='" + dr.get(0).toString() + "'";
				BP.DA.DBAccess.RunSQL(sql);
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion  增加week字段.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 检查数据源.
			SFDBSrc src = new SFDBSrc();
			src.No = "local";
			src.Name = "本机数据源(默认)";
			if (src.RetrieveFromDBSources() == 0)
			{
				src.Insert();
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 检查数据源.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 20170613.增加审核组件配置项"是否显示退回的审核信息"对应字段 by:liuxianchen
			try
			{
				if (BP.DA.DBAccess.IsExitsTableCol("WF_Node", "FWCIsShowReturnMsg") == false)
				{
					switch (src.HisDBType)
					{
						case DBType.MSSQL:
							DBAccess.RunSQL("ALTER TABLE WF_Node ADD FWCIsShowReturnMsg INT NULL");
							break;
						case DBType.Oracle:
						case DBType.Informix:
						case DBType.PostgreSQL:
							DBAccess.RunSQL("ALTER TABLE WF_Node ADD FWCIsShowReturnMsg INTEGER NULL");
							break;
						case DBType.MySQL:
							DBAccess.RunSQL("ALTER TABLE WF_Node ADD FWCIsShowReturnMsg INT NULL");
							break;
						default:
							break;
					}

					DBAccess.RunSQL("UPDATE WF_Node SET FWCIsShowReturnMsg = 0");
				}
			}
			catch (java.lang.Exception e)
			{
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 20170522.增加SL表单设计器中对单选/复选按钮进行字体大小调节的功能 by:liuxianchen
			try
			{
				DataTable columns = src.GetColumns("Sys_FrmRB");
				if (columns.Select("No='AtPara'").length == 0)
				{
					switch (src.HisDBType)
					{
						case DBType.MSSQL:
							DBAccess.RunSQL("ALTER TABLE Sys_FrmRB ADD AtPara NVARCHAR(1000) NULL");
							break;
						case DBType.Oracle:
							DBAccess.RunSQL("ALTER TABLE Sys_FrmRB ADD AtPara NVARCHAR2(1000) NULL");
							break;
						case DBType.PostgreSQL:
							DBAccess.RunSQL("ALTER TABLE Sys_FrmRB ADD AtPara VARCHAR2(1000) NULL");
							break;
						case DBType.MySQL:
						case DBType.Informix:
							DBAccess.RunSQL("ALTER TABLE Sys_FrmRB ADD AtPara TEXT NULL");
							break;
						default:
							break;
					}
				}
			}
			catch (java.lang.Exception e2)
			{
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 其他.
			// 更新 PassRate.
			sql = "UPDATE WF_Node SET PassRate=100 WHERE PassRate IS NULL";
			BP.DA.DBAccess.RunSQL(sql);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 其他.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 升级统一规则.

			try
			{
				String sqls = "";

				if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
				{
					sqls += "UPDATE Sys_MapExt SET MyPK= ExtType||'_'||FK_Mapdata||'_'||AttrOfOper WHERE ExtType='" + MapExtXmlList.TBFullCtrl + "'";
					sqls += "@UPDATE Sys_MapExt SET MyPK= ExtType||'_'||FK_Mapdata||'_'||AttrOfOper WHERE ExtType='" + MapExtXmlList.PopVal + "'";
					sqls += "@UPDATE Sys_MapExt SET MyPK= ExtType||'_'||FK_Mapdata||'_'||AttrOfOper WHERE ExtType='" + MapExtXmlList.DDLFullCtrl + "'";
					sqls += "@UPDATE Sys_MapExt SET MyPK= ExtType||'_'||FK_Mapdata||'_'||AttrsOfActive WHERE ExtType='" + MapExtXmlList.ActiveDDL + "'";
				}


				if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
				{
					sqls += "UPDATE Sys_MapExt SET MyPK=CONCAT(ExtType,'_',FK_Mapdata,'_',AttrOfOper) WHERE ExtType='" + MapExtXmlList.TBFullCtrl + "'";
					sqls += "@UPDATE Sys_MapExt SET MyPK=CONCAT(ExtType,'_',FK_Mapdata,'_',AttrOfOper) WHERE ExtType='" + MapExtXmlList.PopVal + "'";
					sqls += "@UPDATE Sys_MapExt SET MyPK=CONCAT(ExtType,'_',FK_Mapdata,'_',AttrOfOper) WHERE ExtType='" + MapExtXmlList.DDLFullCtrl + "'";
					sqls += "@UPDATE Sys_MapExt SET MyPK=CONCAT(ExtType,'_',FK_Mapdata,'_',AttrOfOper) WHERE ExtType='" + MapExtXmlList.ActiveDDL + "'";
				}
				else
				{
					sqls += "UPDATE Sys_MapExt SET MyPK= ExtType+'_'+FK_Mapdata+'_'+AttrOfOper WHERE ExtType='" + MapExtXmlList.TBFullCtrl + "'";
					sqls += "@UPDATE Sys_MapExt SET MyPK= ExtType+'_'+FK_Mapdata+'_'+AttrOfOper WHERE ExtType='" + MapExtXmlList.PopVal + "'";
					sqls += "@UPDATE Sys_MapExt SET MyPK= ExtType+'_'+FK_Mapdata+'_'+AttrOfOper WHERE ExtType='" + MapExtXmlList.DDLFullCtrl + "'";
					sqls += "@UPDATE Sys_MapExt SET MyPK= ExtType+'_'+FK_Mapdata+'_'+AttrsOfActive WHERE ExtType='" + MapExtXmlList.ActiveDDL + "'";
				}

				BP.DA.DBAccess.RunSQLs(sqls);
			}
			catch (RuntimeException ex)
			{
				BP.DA.Log.DebugWriteError(ex.getMessage());
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

			///#region  更新CA签名(2015-03-03)。
			//BP.Tools.WFSealData sealData = new Tools.WFSealData();
			//sealData.CheckPhysicsTable();
			//sealData.UpdateColumn();
			///#endregion  更新CA签名.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 其他.


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 升级sys_sftable
			//升级
			BP.Sys.SFTable tab = new SFTable();
			tab.CheckPhysicsTable();
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 基础数据更新.
			Node wf_Node = new Node();
			wf_Node.CheckPhysicsTable();
			// 设置节点ICON.
			sql = "UPDATE WF_Node SET ICON='审核.png' WHERE ICON IS NULL";
			BP.DA.DBAccess.RunSQL(sql);

			BP.WF.Template.NodeSheet nodeSheet = new BP.WF.Template.NodeSheet();
			nodeSheet.CheckPhysicsTable();

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 基础数据更新.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 把节点的toolbarExcel, word 信息放入mapdata
			BP.WF.Template.NodeSheets nss = new BP.WF.Template.NodeSheets();
			nss.RetrieveAll();
			for (BP.WF.Template.NodeSheet ns : nss)
			{
				ToolbarExcel te = new ToolbarExcel();
				te.No = "ND" + ns.getNodeID();
				te.RetrieveFromDBSources();
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 升级SelectAccper
			Direction dir = new Direction();
			dir.CheckPhysicsTable();

			SelectAccper selectAccper = new SelectAccper();
			selectAccper.CheckPhysicsTable();
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region  升级 NodeToolbar
			FrmField ff = new FrmField();
			ff.CheckPhysicsTable();

			MapAttr attr = new MapAttr();
			attr.CheckPhysicsTable();

			NodeToolbar bar = new NodeToolbar();
			bar.CheckPhysicsTable();

			FrmNode nff = new FrmNode();
			nff.CheckPhysicsTable();

			SysForm ssf = new SysForm();
			ssf.CheckPhysicsTable();

			SysFormTree ssft = new SysFormTree();
			ssft.CheckPhysicsTable();

			BP.Sys.FrmAttachment myath = new FrmAttachment();
			myath.CheckPhysicsTable();

			FrmField ffs = new FrmField();
			ffs.CheckPhysicsTable();
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 执行sql．
			BP.DA.DBAccess.RunSQL("delete  from Sys_Enum WHERE EnumKey in ('BillFileType','EventDoType','FormType','BatchRole','StartGuideWay','NodeFormType')");
			DBAccess.RunSQL("UPDATE Sys_FrmSln SET FK_Flow =(SELECT FK_FLOW FROM WF_Node WHERE NODEID=Sys_FrmSln.FK_Node) WHERE FK_Flow IS NULL");

			if (SystemConfig.getAppCenterDBType() == DBType.MSSQL)
			{
				DBAccess.RunSQL("UPDATE WF_FrmNode SET MyPK=FK_Frm+'_'+convert(varchar,FK_Node )+'_'+FK_Flow");
			}

			if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
			{
				DBAccess.RunSQL("UPDATE WF_FrmNode SET MyPK=FK_Frm||'_'||FK_Node||'_'||FK_Flow");
			}

			if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
			{
				DBAccess.RunSQL("UPDATE WF_FrmNode SET MyPK=CONCAT(FK_Frm,'_',FK_Node,'_',FK_Flow)");
			}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 检查必要的升级。
			FrmWorkCheck fwc = new FrmWorkCheck();
			fwc.CheckPhysicsTable();

			Flow myfl = new Flow();
			myfl.CheckPhysicsTable();

			Node nd = new Node();
			nd.CheckPhysicsTable();

			//Sys_SFDBSrc
			SFDBSrc sfDBSrc = new SFDBSrc();
			sfDBSrc.CheckPhysicsTable();
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 检查必要的升级。

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 执行更新.wf_node
			sql = "UPDATE WF_Node SET FWCType=0 WHERE FWCType IS NULL";
			sql += "@UPDATE WF_Node SET FWC_X=0 WHERE FWC_X IS NULL";
			sql += "@UPDATE WF_Node SET FWC_Y=0 WHERE FWC_Y IS NULL";
			sql += "@UPDATE WF_Node SET FWC_W=0 WHERE FWC_W IS NULL";
			sql += "@UPDATE WF_Node SET FWC_H=0 WHERE FWC_H IS NULL";
			BP.DA.DBAccess.RunSQLs(sql);


			sql = "UPDATE WF_Node SET SFSta=0 WHERE SFSta IS NULL";
			sql += "@UPDATE WF_Node SET SF_X=0 WHERE SF_X IS NULL";
			sql += "@UPDATE WF_Node SET SF_Y=0 WHERE SF_Y IS NULL";
			sql += "@UPDATE WF_Node SET SF_W=0 WHERE SF_W IS NULL";
			sql += "@UPDATE WF_Node SET SF_H=0 WHERE SF_H IS NULL";
			BP.DA.DBAccess.RunSQLs(sql);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 执行更新.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 升级站内消息表 2013-10-20
			BP.WF.SMS sms = new SMS();
			sms.CheckPhysicsTable();
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 重新生成view WF_EmpWorks,  2013-08-06.

			if (DBAccess.IsExitsObject("WF_EmpWorks") == true)
			{
				BP.DA.DBAccess.RunSQL("DROP VIEW WF_EmpWorks");
			}

			if (DBAccess.IsExitsObject("V_FlowStarter") == true)
			{
				BP.DA.DBAccess.RunSQL("DROP VIEW V_FlowStarter");
			}

			if (DBAccess.IsExitsObject("V_FlowStarterBPM") == true)
			{
				BP.DA.DBAccess.RunSQL("DROP VIEW V_FlowStarterBPM");
			}

			if (DBAccess.IsExitsObject("V_TOTALCH") == true)
			{
				BP.DA.DBAccess.RunSQL("DROP VIEW V_TOTALCH");
			}

			if (DBAccess.IsExitsObject("V_TOTALCHYF") == true)
			{
				BP.DA.DBAccess.RunSQL("DROP VIEW V_TOTALCHYF");
			}

			if (DBAccess.IsExitsObject("V_TotalCHWeek") == true)
			{
				BP.DA.DBAccess.RunSQL("DROP VIEW V_TotalCHWeek");
			}

			if (DBAccess.IsExitsObject("V_WF_Delay") == true)
			{
				BP.DA.DBAccess.RunSQL("DROP VIEW V_WF_Delay");
			}

			String sqlscript = "";
			//执行必须的sql.
			switch (BP.Sys.SystemConfig.getAppCenterDBType())
			{
				case DBType.Oracle:
					sqlscript = BP.Sys.SystemConfig.PathOfData + "\\Install\\SQLScript\\InitView_Ora.sql";
					break;
				case DBType.MSSQL:
				case DBType.Informix:
					sqlscript = BP.Sys.SystemConfig.PathOfData + "\\Install\\SQLScript\\InitView_SQL.sql";
					break;
				case DBType.MySQL:
					sqlscript = BP.Sys.SystemConfig.PathOfData + "\\Install\\SQLScript\\InitView_MySQL.sql";
					break;
				case DBType.PostgreSQL:
					sqlscript = BP.Sys.SystemConfig.PathOfData + "\\Install\\SQLScript\\InitView_PostgreSQL.sql";
					break;
				default:
					break;
			}

			BP.DA.DBAccess.RunSQLScript(sqlscript);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 升级Img
			FrmImg img = new FrmImg();
			img.CheckPhysicsTable();

			BP.Sys.FrmUI.ExtImg myimg = new BP.Sys.FrmUI.ExtImg();
			myimg.CheckPhysicsTable();
			if (DBAccess.IsExitsTableCol("Sys_FrmImg", "SrcType") == true)
			{
				DBAccess.RunSQL("UPDATE Sys_FrmImg SET ImgSrcType = SrcType WHERE ImgSrcType IS NULL");
				DBAccess.RunSQL("UPDATE Sys_FrmImg SET ImgSrcType = 0 WHERE ImgSrcType IS NULL");
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 修复 mapattr UIHeight, UIWidth 类型错误.
			switch (BP.Sys.SystemConfig.getAppCenterDBType())
			{
				case DBType.Oracle:
					msg = "@Sys_MapAttr 修改字段";
					break;
				case DBType.MSSQL:
					msg = "@修改sql server控件高度和宽度字段。";
					DBAccess.RunSQL("ALTER TABLE Sys_MapAttr ALTER COLUMN UIWidth float");
					DBAccess.RunSQL("ALTER TABLE Sys_MapAttr ALTER COLUMN UIHeight float");
					break;
				default:
					break;
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 升级常用词汇
			switch (BP.Sys.SystemConfig.getAppCenterDBType())
			{
				case DBType.Oracle:
					int i = DBAccess.RunSQLReturnCOUNT("SELECT * FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'SYS_DEFVAL' AND COLUMN_NAME = 'PARENTNO'");
					if (i == 0)
					{
						if (DBAccess.IsExitsObject("SYS_DEFVAL") == true)
						{
							DBAccess.RunSQL("drop table Sys_DefVal");
						}

						DefVal dv = new DefVal();
						dv.CheckPhysicsTable();
					}
					msg = "@Sys_DefVal 修改字段";
					break;
				case DBType.MSSQL:
					msg = "@修改 Sys_DefVal。";
					i = DBAccess.RunSQLReturnCOUNT("SELECT * FROM SYSCOLUMNS WHERE ID=OBJECT_ID('Sys_DefVal') AND NAME='ParentNo'");
					if (i == 0)
					{
						if (DBAccess.IsExitsObject("Sys_DefVal") == true)
						{
							DBAccess.RunSQL("drop table Sys_DefVal");
						}

						DefVal dv = new DefVal();
						dv.CheckPhysicsTable();
					}
					break;
				default:
					break;
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 登陆更新错误
			msg = "@登陆时错误。";
			DBAccess.RunSQL("DELETE FROM Sys_Enum WHERE EnumKey IN ('DeliveryWay','RunModel','OutTimeDeal','FlowAppType')");
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 登陆更新错误

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 升级表单树
			// 首先检查是否升级过.
			sql = "SELECT * FROM Sys_FormTree WHERE No = '1'";
			DataTable formTree_dt = DBAccess.RunSQLReturnTable(sql);
			if (formTree_dt.Rows.size() == 0)
			{
				/*没有升级过.增加根节点*/
				SysFormTree formTree = new SysFormTree();
				formTree.No = "1";
				formTree.Name = "表单库";
				formTree.setParentNo("0");
				// formTree.TreeNo = "0";
				formTree.setIdx(0);
				formTree.setIsDir(true);

				try
				{
					formTree.DirectInsert();
				}
				catch (java.lang.Exception e3)
				{
				}
				//将表单库中的数据转入表单树
				SysFormTrees formSorts = new SysFormTrees();
				formSorts.RetrieveAll();

				for (SysFormTree item : formSorts)
				{
					if (item.No.equals("0"))
					{
						continue;
					}
					SysFormTree subFormTree = new SysFormTree();
					subFormTree.No = item.No;
					subFormTree.Name = item.Name;
					subFormTree.setParentNo("0");
					subFormTree.Save();
				}
				//表单于表单树进行关联
				sql = "UPDATE Sys_MapData SET FK_FormTree=FK_FrmSort WHERE FK_FrmSort <> '' AND FK_FrmSort IS not null";
				DBAccess.RunSQL(sql);
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 执行admin登陆. 2012-12-25 新版本.
			Emp emp = new Emp();
			emp.No = "admin";
			if (emp.RetrieveFromDBSources() == 1)
			{
				WebUser.SignInOfGener(emp);
			}
			else
			{
				emp.No = "admin";
				emp.Name = "admin";
				emp.FK_Dept = "01";
				emp.Pass = "123";
				emp.Insert();
				WebUser.SignInOfGener(emp);
				//throw new Exception("admin 用户丢失，请注意大小写。");
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 执行admin登陆.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 修复 Sys_FrmImg 表字段 ImgAppType Tag0
			switch (BP.Sys.SystemConfig.getAppCenterDBType())
			{
				case DBType.Oracle:
					int i = DBAccess.RunSQLReturnCOUNT("SELECT * FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'SYS_FRMIMG' AND COLUMN_NAME = 'TAG0'");
					if (i == 0)
					{
						DBAccess.RunSQL("ALTER TABLE SYS_FRMIMG ADD (ImgAppType number,TAG0 nvarchar(500))");
					}
					msg = "@Sys_FrmImg 修改字段";
					break;
				case DBType.MSSQL:
					msg = "@修改 Sys_FrmImg。";
					i = DBAccess.RunSQLReturnCOUNT("SELECT * FROM SYSCOLUMNS WHERE ID=OBJECT_ID('Sys_FrmImg') AND NAME='Tag0'");
					if (i == 0)
					{
						DBAccess.RunSQL("ALTER TABLE Sys_FrmImg ADD ImgAppType int");
						DBAccess.RunSQL("ALTER TABLE Sys_FrmImg ADD Tag0 nvarchar(500)");
					}
					break;
				default:
					break;
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 20161101.升级表单，增加图片附件必填验证 by:liuxianchen

			FrmImgAth imgAth = new FrmImgAth();
			imgAth.CheckPhysicsTable();

			sql = "UPDATE Sys_FrmImgAth SET IsRequired = 0 WHERE IsRequired IS NULL";
			BP.DA.DBAccess.RunSQL(sql);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 20170520.附件删除规则修复
			try
			{
				DataTable columns = src.GetColumns("Sys_FrmAttachment");
				if (columns.Select("No='DeleteWay'").length > 0 && columns.Select("No='IsDelete'").length > 0)
				{
					DBAccess.RunSQL("UPDATE SYS_FRMATTACHMENT SET DeleteWay=IsDelete WHERE DeleteWay IS NULL");
				}
			}
			catch (java.lang.Exception e4)
			{
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 密码加密
			if (SystemConfig.IsEnablePasswordEncryption == true && BP.DA.DBAccess.IsView("Port_Emp", SystemConfig.getAppCenterDBType()) == false)
			{
				BP.Port.Emps emps = new BP.Port.Emps();
				emps.RetrieveAllFromDBSource();
				for (Emp empEn : emps.ToJavaList())
				{
					if (DataType.IsNullOrEmpty(empEn.Pass) || empEn.Pass.Length < 30)
					{
						empEn.Pass = BP.Tools.Cryptography.EncryptString(empEn.Pass);
						empEn.DirectUpdate();
					}
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

			// 最后更新版本号，然后返回.
			sql = "UPDATE Sys_Serial SET IntVal=" + Ver + " WHERE CfgKey='Ver'";
			if (DBAccess.RunSQL(sql) == 0)
			{
				sql = "INSERT INTO Sys_Serial (CfgKey,IntVal) VALUES('Ver'," + Ver + ") ";
				DBAccess.RunSQL(sql);
			}
			// 返回版本号.
			return "旧版本:(" + currDBVer + ")新版本:(" + Ver + ")"; // +"\t\n解决问题:" + updataNote;
		}
		catch (RuntimeException ex)
		{
			String err = "问题出处:" + ex.getMessage() + "<hr>" + msg + "<br>详细信息:@" + ex.getStackTrace() + "<br>@<a href='../DBInstall.aspx' >点这里到系统升级界面。</a>";
			BP.DA.Log.DebugWriteError("系统升级错误:" + err);
			return err;
		}
	}
	/** 
	 如果发现升级sql文件日期变化了，就自动升级.
	 就是说该文件如果被修改了就会自动升级.
	*/
	public static void UpdataCCFlowVerSQLScript()
	{
		String sql = "SELECT IntVal FROM Sys_Serial WHERE CfgKey='UpdataCCFlowVer'";
		String currDBVer = DBAccess.RunSQLReturnStringIsNull(sql, "");

		String sqlScript = SystemConfig.PathOfData + "\\UpdataCCFlowVer.sql";
		File fi = new File(sqlScript);
		String myVer = fi.LastWriteTime.toString("MMddHHmmss");

		//判断是否可以执行，当文件发生变化后，才执行。
		if (currDBVer.equals("") || Integer.parseInt(currDBVer) < Integer.parseInt(myVer))
		{
			BP.DA.DBAccess.RunSQLScript(sqlScript);
			sql = "UPDATE Sys_Serial SET IntVal=" + myVer + " WHERE CfgKey='UpdataCCFlowVer'";

			if (DBAccess.RunSQL(sql) == 0)
			{
				sql = "INSERT INTO Sys_Serial (CfgKey,IntVal) VALUES('UpdataCCFlowVer'," + myVer + ") ";
				DBAccess.RunSQL(sql);
			}
		}
	}
	/** 
	 CCFlowAppPath
	*/
	public static String getCCFlowAppPath()
	{
		return BP.Sys.SystemConfig.GetValByKey("CCFlowAppPath", "/");
	}
	/** 
	 
	*/
	public static boolean getIsEnableHuiQianList()
	{
		if (SystemConfig.getCustomerNo().equals("TianYe"))
		{
			return true;
		}

		return BP.Sys.SystemConfig.GetValByKeyBoolen("IsEnableHuiQianList", false);
	}
	/** 
	 检查是否可以安装系统
	 
	 @return 
	*/
	public static boolean IsCanInstall()
	{
		try
		{
			String sql = "CREATE TABLE AA (XXX,DDD)";
			BP.DA.DBAccess.RunSQL(sql);

			sql = "CREATE TABLE AAVIEW AS SELECT * FROM AA";
			BP.DA.DBAccess.RunSQL(sql);

			sql = "DROP VIEW AAVIEW";
			BP.DA.DBAccess.RunSQL(sql);

			sql = "DROP TABLE AA";
			BP.DA.DBAccess.RunSQL(sql);
			return true;
		}
		catch (java.lang.Exception e)
		{
			return false;
		}
		return true;
	}

	/** 
	 安装包
	 
	 @param lang 语言
	 @param demoType 0安装Demo, 1 不安装Demo.
	*/
	public static void DoInstallDataBase(String lang, int demoType)
	{
		boolean isInstallFlowDemo = true;
		if (demoType == 2)
		{
			isInstallFlowDemo = false;
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 检查是否是空白的数据库。
		//if (BP.DA.DBAccess.IsExitsObject("WF_Emp")
		//     || BP.DA.DBAccess.IsExitsObject("WF_Flow")
		//     || BP.DA.DBAccess.IsExitsObject("Port_Emp")
		//    || BP.DA.DBAccess.IsExitsObject("CN_City"))
		//{
		//    throw new Exception("@当前的数据库好像是一个安装执行失败的数据库，里面包含了一些cc的表，所以您需要删除这个数据库然后执行重新安装。");
		//}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 检查是否是空白的数据库。


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 首先创建Port类型的表, 让admin登录.

		FrmRB rb = new FrmRB();
		rb.CheckPhysicsTable();


		BP.Port.Emp portEmp = new BP.Port.Emp();
		portEmp.CheckPhysicsTable();

		BP.GPM.Emp myemp = new BP.GPM.Emp();
		myemp.CheckPhysicsTable();

		BP.GPM.Dept mydept = new BP.GPM.Dept();
		mydept.CheckPhysicsTable();

		BP.GPM.Station mySta = new BP.GPM.Station();
		mySta.CheckPhysicsTable();

		BP.GPM.StationType myStaType = new BP.GPM.StationType();
		myStaType.CheckPhysicsTable();

		BP.GPM.DeptEmp myde = new GPM.DeptEmp();
		myde.CheckPhysicsTable();

		BP.GPM.DeptEmpStation mydes = new GPM.DeptEmpStation();
		mydes.CheckPhysicsTable();

		BP.GPM.DeptStation mydeptSta = new GPM.DeptStation();
		mydeptSta.CheckPhysicsTable();

		BP.Sys.FrmRB myrb = new BP.Sys.FrmRB();
		myrb.CheckPhysicsTable();

		WFEmp wfemp = new WFEmp();
		wfemp.CheckPhysicsTable();

		if (BP.DA.DBAccess.IsExitsTableCol("WF_Emp", "StartFlows") == false)
		{
			String sql = "";
			//增加StartFlows这个字段
			switch (SystemConfig.getAppCenterDBType())
			{
				case DBType.MSSQL:
					sql = "ALTER TABLE WF_Emp ADD StartFlows Text DEFAULT  NULL";
					break;
				case DBType.Oracle:
					sql = "ALTER TABLE  WF_EMP add StartFlows BLOB";
					break;
				case DBType.MySQL:
					sql = "ALTER TABLE WF_Emp ADD StartFlows TEXT COMMENT '可以发起的流程'";
					break;
				case DBType.Informix:
					sql = "ALTER TABLE WF_Emp ADD StartFlows VARCHAR(4000) DEFAULT  NULL";
					break;
				case DBType.PostgreSQL:
					sql = "ALTER TABLE WF_Emp ADD StartFlows Text DEFAULT  NULL";
					break;
				default:
					throw new RuntimeException("@没有涉及到的数据库类型");
			}
			DBAccess.RunSQL(sql);
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 首先创建Port类型的表.


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 3, 执行基本的 sql
		String sqlscript = "";

		sqlscript = BP.Sys.SystemConfig.CCFlowAppPath + "\\WF\\Data\\Install\\SQLScript\\Port_Inc_CH_BPM.sql";
		BP.DA.DBAccess.RunSQLScript(sqlscript);

		BP.Port.Emp empAdmin = new Emp("admin");
		WebUser.SignInOfGener(empAdmin);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 执行基本的 sql


		ArrayList al = null;
		String info = "BP.En.Entity";
		al = BP.En.ClassFactory.GetObjects(info);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 先创建表，否则列的顺序就会变化.
		FlowExt fe = new FlowExt();
		fe.CheckPhysicsTable();

		NodeExt ne = new NodeExt();
		ne.CheckPhysicsTable();

		MapDtl mapdtl = new MapDtl();
		mapdtl.CheckPhysicsTable();

		CC cc = new CC();
		cc.CheckPhysicsTable();

		BP.WF.Template.FrmWorkCheck fwc = new FrmWorkCheck();
		fwc.CheckPhysicsTable();

		MapAttr attr = new MapAttr();
		attr.CheckPhysicsTable();

		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.CheckPhysicsTable();

		BP.WF.Data.CH ch = new CH();
		ch.CheckPhysicsTable();
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 先创建表，否则列的顺序就会变化.


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 1, 创建or修复表
		for (Object obj : al)
		{
			Entity en = null;
			en = obj instanceof Entity ? (Entity)obj : null;
			if (en == null)
			{
				continue;
			}

			//获得类名.
			String clsName = en.toString();
			if (clsName == null)
			{
				continue;
			}

			if (clsName.contains("FlowSheet") == true)
			{
				continue;
			}
			if (clsName.contains("NodeSheet") == true)
			{
				continue;
			}
			if (clsName.contains("FlowFormTree") == true)
			{
				continue;
			}


			//不安装CCIM的表.
			if (clsName.contains("BP.CCIM"))
			{
				continue;
			}

			//抽象的类不允许创建表.
			switch (clsName)
			{
				case "BP.WF.StartWork":
				case "BP.WF.Work":
				case "BP.WF.GEStartWork":
				case "BP.En.GENoName":
				case "BP.En.GETree":
				case "BP.WF.Data.GERpt":
					continue;
				default:
					break;
			}

			if (isInstallFlowDemo == false)
			{
				/*如果不安装demo.*/
				if (clsName.contains("BP.CN") || clsName.contains("BP.Demo"))
				{
					continue;
				}
			}


			String table = null;
			try
			{
				table = en.EnMap.PhysicsTable;
				if (table == null)
				{
					continue;
				}
			}
			catch (java.lang.Exception e)
			{
				continue;
			}

			try
			{
				switch (table)
				{
					case "WF_EmpWorks":
					case "WF_GenerEmpWorkDtls":
					case "WF_GenerEmpWorks":
						continue;
					case "Sys_Enum":
						en.CheckPhysicsTable();
						break;
					default:
						en.CheckPhysicsTable();
						break;
				}
				en.CheckPhysicsTable();
			}
			catch (java.lang.Exception e2)
			{
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 修复

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 2, 注册枚举类型 SQL
		// 2, 注册枚举类型。
		BP.Sys.XML.EnumInfoXmls xmls = new BP.Sys.XML.EnumInfoXmls();
		xmls.RetrieveAll();
		for (BP.Sys.XML.EnumInfoXml xml : xmls)
		{
			BP.Sys.SysEnums ses = new BP.Sys.SysEnums();
			int count = ses.RetrieveByAttr(SysEnumAttr.EnumKey, xml.getKey());
			if (count > 0)
			{
				continue;
			}
			ses.RegIt(xml.Key, xml.Vals);
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 注册枚举类型



//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 4, 创建视图与数据.
		//执行必须的sql.

		sqlscript = "";
		//执行必须的sql.
		switch (BP.Sys.SystemConfig.getAppCenterDBType())
		{
			case DBType.Oracle:
				sqlscript = BP.Sys.SystemConfig.CCFlowAppPath + "\\WF\\Data\\Install\\SQLScript\\InitView_Ora.sql";
				break;
			case DBType.MSSQL:
			case DBType.Informix:
				sqlscript = BP.Sys.SystemConfig.CCFlowAppPath + "\\WF\\Data\\Install\\SQLScript\\InitView_SQL.sql";
				break;
			case DBType.MySQL:
				sqlscript = BP.Sys.SystemConfig.CCFlowAppPath + "\\WF\\Data\\Install\\SQLScript\\InitView_MySQL.sql";
				break;
			case DBType.PostgreSQL:
				sqlscript = BP.Sys.SystemConfig.CCFlowAppPath + "\\WF\\Data\\Install\\SQLScript\\InitView_PostgreSQL.sql";
				break;
			default:
				break;
		}

		BP.DA.DBAccess.RunSQLScript(sqlscript);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 创建视图与数据

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 5, 初始化数据.
		if (isInstallFlowDemo)
		{
			sqlscript = SystemConfig.PathOfData + "\\Install\\SQLScript\\InitPublicData.sql";
			BP.DA.DBAccess.RunSQLScript(sqlscript);
		}
		else
		{
			FlowSort fs = new FlowSort();
			fs.No = "1";
			fs.ParentNo = "0";
			fs.Name = "流程树";
			fs.DirectInsert();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 初始化数据

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 6, 生成临时的 wf_emp 数据。
		if (isInstallFlowDemo == true)
		{
			BP.Port.Emps emps = new BP.Port.Emps();
			emps.RetrieveAllFromDBSource();
			int i = 0;
			for (BP.Port.Emp emp : emps.ToJavaList())
			{
				i++;
				WFEmp wfEmp = new WFEmp();
				wfEmp.Copy(emp);
				wfEmp.No = emp.No;

				if (wfEmp.getEmail().length() == 0)
				{
					wfEmp.setEmail(emp.No + "@ccflow.org");
				}

				if (wfEmp.getTel().length() == 0)
				{
					wfEmp.setTel("82374939-6" + tangible.StringHelper.padLeft(String.valueOf(i), 2, '0'));
				}

				if (wfEmp.IsExits)
				{
					wfEmp.Update();
				}
				else
				{
					wfEmp.Insert();
				}
			}

			// 生成简历数据.
			for (BP.Port.Emp emp : emps.ToJavaList())
			{
				for (int myIdx = 0; myIdx < 6; myIdx++)
				{
					String sql = "";
					sql = "INSERT INTO Demo_Resume (OID,RefPK,NianYue,GongZuoDanWei,ZhengMingRen,BeiZhu,QT) ";
					sql += "VALUES(" + DBAccess.GenerOID("Demo_Resume") + ",'" + emp.No + "','200" + myIdx + "-01','济南.驰骋" + myIdx + "公司','张三','表现良好','其他-" + myIdx + "无')";
					DBAccess.RunSQL(sql);
				}
			}

			DataTable dtStudent = BP.DA.DBAccess.RunSQLReturnTable("SELECT No FROM Demo_Student");
			for (DataRow dr : dtStudent.Rows)
			{
				String no = dr.get(0).toString();
				for (int myIdx = 0; myIdx < 6; myIdx++)
				{
					String sql = "";
					sql = "INSERT INTO Demo_Resume (OID,RefPK,NianYue,GongZuoDanWei,ZhengMingRen,BeiZhu,QT) ";
					sql += "VALUES(" + DBAccess.GenerOID("Demo_Resume") + ",'" + no + "','200" + myIdx + "-01','济南.驰骋" + myIdx + "公司','张三','表现良好','其他-" + myIdx + "无')";
					DBAccess.RunSQL(sql);
				}
			}


			// 生成年度月份数据.
			String sqls = "";
			LocalDateTime dtNow = LocalDateTime.now();
			for (int num = 0; num < 12; num++)
			{
				sqls = "@ INSERT INTO Pub_NY (No,Name) VALUES ('" + dtNow.toString("yyyy-MM") + "','" + dtNow.toString("yyyy-MM") + "')  ";
				dtNow = dtNow.plusMonths(1);
			}
			BP.DA.DBAccess.RunSQLs(sqls);
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 初始化数据

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 7, 装载 demo.flow
		if (isInstallFlowDemo == true)
		{
			BP.Port.Emp emp = new BP.Port.Emp("admin");
			WebUser.SignInOfGener(emp);
			BP.Sys.Glo.WriteLineInfo("开始装载模板...");
			String msg = "";

			//装载数据模版.
			BP.WF.DTS.LoadTemplete l = new BP.WF.DTS.LoadTemplete();
			Object tempVar = l.Do();
			msg = tempVar instanceof String ? (String)tempVar : null;


			BP.Sys.Glo.WriteLineInfo("装载模板完成。开始修复视图...");


			BP.Sys.Glo.WriteLineInfo("视图修复完成。");
		}

		if (isInstallFlowDemo == false)
		{
			//创建一个空白的流程，不然，整个结构就出问题。
			FlowSorts fss = new FlowSorts();
			fss.RetrieveAll();
			fss.Delete();

			FlowSort fs = new FlowSort();
			fs.Name = "流程树";
			fs.No = "1";
			fs.ParentNo = "0";
			fs.Insert();

			FlowSort s1 = (FlowSort)fs.DoCreateSubNode();
			s1.Name = "日常办公类";
			s1.Update();

			s1 = (FlowSort)fs.DoCreateSubNode();
			s1.Name = "财务类";
			s1.Update();

			s1 = (FlowSort)fs.DoCreateSubNode();
			s1.Name = "人力资源类";
			s1.Update();


			//创建一个空白的流程，不然，整个结构就出问题。
			BP.Sys.FrmTrees frmTrees = new FrmTrees();
			frmTrees.RetrieveAll();
			frmTrees.Delete();

			FrmTree ftree = new FrmTree();
			ftree.Name = "表单树";
			ftree.No = "1";
			ftree.ParentNo = "0";
			ftree.Insert();

			FrmTree subFrmTree = (FrmTree)ftree.DoCreateSubNode();
			subFrmTree.Name = "流程独立表单";
			subFrmTree.Update();

			subFrmTree = (FrmTree)ftree.DoCreateSubNode();
			subFrmTree.Name = "常用信息管理";
			subFrmTree.Update();

			subFrmTree = (FrmTree)ftree.DoCreateSubNode();
			subFrmTree.Name = "常用单据";
			subFrmTree.Update();

		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 装载demo.flow

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 增加图片签名
		if (isInstallFlowDemo == true)
		{
			try
			{
				//增加图片签名
				BP.WF.DTS.GenerSiganture gs = new BP.WF.DTS.GenerSiganture();
				gs.Do();
			}
			catch (java.lang.Exception e3)
			{
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 增加图片签名.

		//生成拼音，以方便关键字查找.
		BP.WF.DTS.GenerPinYinForEmp pinyin = new BP.WF.DTS.GenerPinYinForEmp();
		pinyin.Do();

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 执行补充的sql, 让外键的字段长度都设置成100.
		DBAccess.RunSQL("UPDATE Sys_MapAttr SET maxlen=100 WHERE LGType=2 AND MaxLen<100");
		DBAccess.RunSQL("UPDATE Sys_MapAttr SET maxlen=100 WHERE KeyOfEn='FK_Dept'");

		//Nodes nds = new Nodes();
		//nds.RetrieveAll();
		//foreach (Node nd in nds)
		//    nd.HisWork.CheckPhysicsTable();
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 执行补充的sql, 让外键的字段长度都设置成100.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 如果是第一次运行，就执行检查。
		if (isInstallFlowDemo == true)
		{
			Flows fls = new Flows();
			fls.RetrieveAllFromDBSource();
			for (Flow fl : fls.ToJavaList())
			{
				try
				{
					fl.DoCheck();
				}
				catch (RuntimeException ex)
				{
					BP.DA.Log.DebugWriteError(ex.getMessage());
				}
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 如果是第一次运行，就执行检查。



	}
	/** 
	 检查树结构是否符合需求
	 
	 @return 
	*/
	public static boolean CheckTreeRoot()
	{
		// 流程树根节点校验
		String tmp = "SELECT Name FROM WF_FlowSort WHERE ParentNo='0'";
		tmp = DBAccess.RunSQLReturnString(tmp);
		if (DataType.IsNullOrEmpty(tmp))
		{
			tmp = "INSERT INTO WF_FlowSort(No,Name,ParentNo,TreeNo,idx,IsDir) values('01','流程树',0,'',0,0)";
			DBAccess.RunSQLReturnString(tmp);
		}

		// 表单树根节点校验
		tmp = "SELECT Name FROM Sys_FormTree WHERE ParentNo = '0' ";
		tmp = DBAccess.RunSQLReturnString(tmp);
		if (DataType.IsNullOrEmpty(tmp))
		{
			tmp = "INSERT INTO Sys_FormTree(No,Name,ParentNo,Idx,IsDir) values('001','表单树',0,0,0)";
			DBAccess.RunSQLReturnString(tmp);
		}

		//检查组织解构是否正确.
		String sql = "SELECT * FROM Port_Dept WHERE ParentNo='0' ";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			BP.Port.Dept rootDept = new BP.Port.Dept();
			rootDept.Name = "总部";
			rootDept.ParentNo = "0";
			try
			{
				rootDept.Insert();
			}
			catch (RuntimeException ex)
			{
				BP.DA.Log.DefaultLogWriteLineWarning("@尝试向port_dept插入数据失败，应该是视图问题. 技术信息:" + ex.getMessage());
			}
			throw new RuntimeException("@没有找到部门树为0个根节点, 有可能是因为您在集成cc的时候，没有遵守cc的规则，部门树的根节点必须是ParentNo=0。");
		}

		if (BP.WF.Glo.getOSModel() == getOSModel().OneOne)
		{
			try
			{
				BP.Port.Dept dept = new BP.Port.Dept();
				dept.Retrieve(BP.Port.DeptAttr.ParentNo, "0");
			}
			catch (RuntimeException ex)
			{
				throw new RuntimeException("@cc的运行模式为OneOne @检查部门的时候错误:有可能是因为您在集成cc的时候，没有遵守cc的规则,Port_Dept列不符合要求，请仔细对比集成手册. 技术信息:" + ex.getMessage());
			}
		}

		if (BP.WF.Glo.getOSModel() == getOSModel().OneMore)
		{
			try
			{
				//  BP.GPM.Depts rootDepts = new BP.GPM.Depts("0");
			}
			catch (RuntimeException ex)
			{
				throw new RuntimeException("@cc的运行模式为OneMore @检查部门的时候错误:有可能是因为您在集成cc的时候，没有遵守cc的规则,Port_Dept列不符合要求，请仔细对比集成手册. 技术信息:" + ex.getMessage());
			}
		}
		return true;
	}

	public static void KillProcess(String processName) //杀掉进程的方法
	{
		System.Diagnostics.Process[] processes = System.Diagnostics.Process.GetProcesses();
		for (System.Diagnostics.Process pro : processes)
		{
			String name = pro.ProcessName + ".exe";
			if (name.toLowerCase().equals(processName.toLowerCase()))
			{
				pro.Kill();
			}
		}
	}
	/** 
	 产生新的编号
	 
	 @param rptKey
	 @return 
	*/
	public static String GenerFlowNo(String rptKey)
	{
		rptKey = rptKey.replace("ND", "");
		rptKey = rptKey.replace("Rpt", "");
		switch (rptKey.length())
		{
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
		return rptKey;
	}
	/** 
	 
	*/
	public static boolean getIsShowFlowNum()
	{
		switch (SystemConfig.AppSettings["IsShowFlowNum"])
		{
			case "1":
				return true;
			default:
				return false;
		}
	}

	/** 
	 产生word文档.
	 
	 @param wk
	*/
	public static void GenerWord(Object filename, Work wk)
	{
		BP.WF.Glo.KillProcess("WINWORD.EXE");
		String enName = wk.EnMap.PhysicsTable;
		try
		{
			RegistryKey delKey = Registry.LocalMachine.OpenSubKey("HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Shared Tools\\Text Converters\\Import\\", true);
			delKey.DeleteValue("MSWord6.wpc");
			delKey.Close();
		}
		catch (java.lang.Exception e)
		{
		}

		GroupField currGF = new GroupField();
		MapAttrs mattrs = new MapAttrs(enName);
		GroupFields gfs = new GroupFields(enName);
		MapDtls dtls = new MapDtls(enName);
		for (MapDtl dtl : dtls.ToJavaList())
		{
			dtl.IsUse = false;
		}

		// 计算出来单元格的行数。
		int rowNum = 0;
		for (GroupField gf : gfs)
		{
			rowNum++;
			boolean isLeft = true;
			for (MapAttr attr : mattrs)
			{
				if (attr.UIVisible == false)
				{
					continue;
				}

				if (attr.GroupID != gf.OID)
				{
					continue;
				}

				if (attr.UIIsLine)
				{
					rowNum++;
					isLeft = true;
					continue;
				}

				if (isLeft == false)
				{
					rowNum++;
				}
				isLeft = !isLeft;
			}
		}

		rowNum = rowNum + 2 + dtls.size();

		// 创建Word文档
		String CheckedInfo = "";
		String message = "";
		Object Nothing = System.Reflection.Missing.Value;

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
		//    string pict = SystemConfig.PathOfDataUser + "log.jpg"; // 图片所在路径
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
		//    WordApp.ActiveWindow.ActivePane.Selection.InsertAfter("[驰骋业务流程管理系统 http://ccFlow.org]");
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
		//            if (attr.UIVisible == false)
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

		//                GEDtls dtlsDB = new GEDtls(dtl.No);
		//                QueryObject qo = new QueryObject(dtlsDB);
		//                switch (dtl.DtlOpenType)
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
		//                            newTableDtl.Cell(idxRow, columIdx).Range.Text = item.GetValRefTextByKey(attrDtl.getKey());
		//                        else
		//                        {
		//                            if (attrDtl.MyDataType == DataType.AppMoney)
		//                                newTableDtl.Cell(idxRow, columIdx).Range.Text = item.GetValMoneyByKey(attrDtl.getKey()).ToString("0.00");
		//                            else
		//                                newTableDtl.Cell(idxRow, columIdx).Range.Text = item.GetValStrByKey(attrDtl.getKey());

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
		//                    newTable.Cell(groupIdx, 1).Range.Text = attr.Name + ":\r\n" + wk.GetValStrByKey(attr.KeyOfEn);
		//                }
		//                else
		//                {
		//                    newTable.Cell(groupIdx, 2).Merge(newTable.Cell(groupIdx, 4));
		//                    newTable.Cell(groupIdx, 1).Range.Text = attr.Name;
		//                    newTable.Cell(groupIdx, 2).Range.Text = wk.GetValStrByKey(attr.KeyOfEn);
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
		//                        newTable.Cell(groupIdx, 1).Range.Text = attr.Name + ":\r\n" + wk.GetValStrByKey(attr.KeyOfEn);
		//                        currColumnIndex = 3;
		//                        continue;
		//                    }
		//                    else if (currColumnIndex == 3)
		//                    {
		//                        newTable.Cell(groupIdx, 2).Merge(newTable.Cell(groupIdx, 3));
		//                        newTable.Cell(groupIdx, 2).Range.Text = attr.Name + ":\r\n" + wk.GetValStrByKey(attr.KeyOfEn);
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
		//                    if (attr.LGType == FieldTypeS.Normal)
		//                    {
		//                        if (attr.MyDataType == DataType.AppMoney)
		//                            s = wk.GetValDecimalByKey(attr.KeyOfEn).ToString("0.00");
		//                        else
		//                            s = wk.GetValStrByKey(attr.KeyOfEn);
		//                    }
		//                    else
		//                    {
		//                        s = wk.GetValRefTextByKey(attr.KeyOfEn);
		//                    }

		//                    switch (currColumnIndex)
		//                    {
		//                        case 0:
		//                            newTable.Cell(groupIdx, 1).Range.Text = attr.Name;
		//                            if (attr.IsSigan)
		//                            {
		//                                string path = BP.Sys.SystemConfig.PathOfDataUser + "\\Siganture\\" + s + ".jpg";
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
		//                                string path = BP.Sys.SystemConfig.PathOfDataUser + "\\Siganture\\" + s + ".jpg";
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
		//        BP.DA.Log.DebugWriteInfo("@生成pdf失败." + ex.Message);
		//    }
		//}
		//catch (Exception ex)
		//{
		//    throw ex;
		//    // WordApp.Quit(ref  Nothing, ref  Nothing, ref  Nothing);
		//    WordDoc.Close(ref  Nothing, ref  Nothing, ref  Nothing);
		//    WordApp.Quit(ref  Nothing, ref  Nothing, ref  Nothing);
		//}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 执行安装.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 全局的方法处理
	/** 
	 流程数据表系统字段,中间用,分开.
	*/
	public static String getFlowFields()
	{
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
		str += GERptAttr.MyNum + ",";
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
		str += "CDT,";
		return str;
			// return typeof(GERptAttr).GetFields().Select(o => o.Name).ToList();
	}



//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 与流程事件实体相关.
	private static Hashtable Htable_FlowFEE = null;
	/** 
	 获得节点事件实体
	 
	 @param enName 实例名称
	 @return 获得节点事件实体,如果没有就返回为空.
	*/
	public static FlowEventBase GetFlowEventEntityByEnName(String enName)
	{
		if (Htable_FlowFEE == null || Htable_FlowFEE.isEmpty())
		{
			Htable_FlowFEE = new Hashtable();
			ArrayList al = BP.En.ClassFactory.GetObjects("BP.WF.FlowEventBase");
			for (FlowEventBase en : al)
			{
				Htable_FlowFEE.put(en.toString(), en);
			}
		}
		FlowEventBase myen = Htable_FlowFEE.get(enName) instanceof FlowEventBase ? (FlowEventBase)Htable_FlowFEE.get(enName) : null;
		if (myen == null)
		{
			//throw new Exception("@根据类名称获取流程事件实体实例出现错误:" + enName + ",没有找到该类的实体.");
			BP.DA.Log.DefaultLogWriteLineError("@根据类名称获取流程事件实体实例出现错误:" + enName + ",没有找到该类的实体.");
			return null;
		}
		return myen;
	}
	/** 
	 获得事件实体String，根据编号或者流程标记
	 
	 @param flowMark 流程标记
	 @param flowNo 流程编号
	 @return null, 或者流程实体.
	*/
	public static String GetFlowEventEntityStringByFlowMark(String flowMark)
	{
		FlowEventBase en = GetFlowEventEntityByFlowMark(flowMark);
		if (en == null)
		{
			return "";
		}
		return en.toString();
	}
	/** 
	 获得事件实体，根据编号或者流程标记.
	 
	 @param flowMark 流程标记
	 @param flowNo 流程编号
	 @return null, 或者流程实体.
	*/
	public static FlowEventBase GetFlowEventEntityByFlowMark(String flowMark)
	{

		if (Htable_FlowFEE == null || Htable_FlowFEE.isEmpty())
		{
			Htable_FlowFEE = new Hashtable();
			ArrayList al = BP.En.ClassFactory.GetObjects("BP.WF.FlowEventBase");
			Htable_FlowFEE.clear();
			for (FlowEventBase en : al)
			{
				Htable_FlowFEE.put(en.toString(), en);
			}
		}

		for (String key : Htable_FlowFEE.keySet())
		{
			FlowEventBase fee = Htable_FlowFEE.get(key) instanceof FlowEventBase ? (FlowEventBase)Htable_FlowFEE.get(key) : null;

			String mark = "," + fee.getFlowMark() + ",";
			if (mark.contains("," + flowMark + ",") == true)
			{
				return fee;
			}
		}
		return null;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 与流程事件实体相关.

	/** 
	 执行发送工作后处理的业务逻辑
	 用于流程发送后事件调用.
	 如果处理失败，就会抛出异常.
	*/
	public static void DealBuinessAfterSendWork(String fk_flow, long workid, String doFunc, String WorkIDs)
	{
		if (doFunc.equals("SetParentFlow"))
		{
			/* 如果需要设置子父流程信息.
			 * 应用于合并审批,当多个子流程合并审批,审批后发起一个父流程.
			 */

			GenerWorkFlow gwfParent = new GenerWorkFlow(workid);

			String[] workids = WorkIDs.split("[,]", -1);
			String okworkids = ""; //成功发送后的workids.
			GenerWorkFlow gwfSubFlow = new GenerWorkFlow();
			for (String id : workids)
			{
				if (DataType.IsNullOrEmpty(id))
				{
					continue;
				}

				// 把数据copy到里面,让子流程也可以得到父流程的数据。
				long workidC = Long.parseLong(id);
				gwfSubFlow.setWorkID(workidC);
				gwfSubFlow.RetrieveFromDBSources();

				//设置当前流程的ID
				BP.WF.Dev2Interface.SetParentInfo(gwfSubFlow.getFK_Flow(), workidC, gwfParent.getWorkID());

				// 是否可以执行？
				if (BP.WF.Dev2Interface.Flow_IsCanDoCurrentWork(workidC, WebUser.getNo()) == true)
				{
					//执行向下发送.
					try
					{
						BP.WF.Dev2Interface.Node_SendWork(gwfSubFlow.getFK_Flow(), workidC);
						okworkids += workidC;
					}
					catch (RuntimeException ex)
					{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
							///#region 如果有一个发送失败，就撤销子流程与父流程.
						//首先把主流程撤销发送.
						BP.WF.Dev2Interface.Flow_DoUnSend(fk_flow, workid);

						//把已经发送成功的子流程撤销发送.
						String[] myokwokid = okworkids.split("[,]", -1);
						for (String okwokid : myokwokid)
						{
							if (DataType.IsNullOrEmpty(id))
							{
								continue;
							}

							// 把数据copy到里面,让子流程也可以得到父流程的数据。
							workidC = Long.parseLong(id);
							BP.WF.Dev2Interface.Flow_DoUnSend(gwfSubFlow.getFK_Flow(), gwfSubFlow.getWorkID());
						}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
							///#endregion 如果有一个发送失败，就撤销子流程与父流程.
						throw new RuntimeException("@在执行子流程(" + gwfSubFlow.getTitle() + ")发送时出现如下错误:" + ex.getMessage());
					}
				}
			}
		}

	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 全局的方法处理

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region web.config 属性.
	/** 
	 根据配置的信息不同，从不同的表里获取人员岗位信息。
	*/
	public static String getEmpStation()
	{

		return "Port_DeptEmpStation";

	}
	/** 
	 是否admin
	*/
	public static boolean getIsAdmin()
	{
		String s = BP.Sys.SystemConfig.AppSettings["adminers"];
		if (DataType.IsNullOrEmpty(s))
		{
			s = "admin,";
		}
		return s.contains(WebUser.getNo());
	}
	public static boolean getIsEnableTrackRec()
	{
		String s = BP.Sys.SystemConfig.AppSettings["IsEnableTrackRec"];
		if (DataType.IsNullOrEmpty(s))
		{
			return false;
		}
		if (s.equals("0"))
		{
			return false;
		}

		return true;
	}
	/** 
	 获取mapdata字段查询Like。
	 
	 @param flowNo 流程编号
	 @param colName 列编号
	*/
	public static String MapDataLikeKeyV1(String flowNo, String colName)
	{
		flowNo = Integer.parseInt(flowNo).toString();
		String len = BP.Sys.SystemConfig.AppCenterDBLengthStr;
		if (flowNo.length() == 1)
		{
			return " " + colName + " LIKE 'ND" + flowNo + "%' AND " + len + "(" + colName + ")=5";
		}
		if (flowNo.length() == 2)
		{
			return " " + colName + " LIKE 'ND" + flowNo + "%' AND " + len + "(" + colName + ")=6";
		}
		if (flowNo.length() == 3)
		{
			return " " + colName + " LIKE 'ND" + flowNo + "%' AND " + len + "(" + colName + ")=7";
		}

		return " " + colName + " LIKE 'ND" + flowNo + "%' AND " + len + "(" + colName + ")=8";
	}
	public static String MapDataLikeKey(String flowNo, String colName)
	{
		flowNo = Integer.parseInt(flowNo).toString();
		String len = BP.Sys.SystemConfig.AppCenterDBLengthStr;

		//edited by liuxc,2016-02-22,合并逻辑，原来分流程编号的位数，现在统一处理
		return " (" + colName + " LIKE 'ND" + flowNo + "%' AND " + len + "(" + colName + ")=" + ("ND".length() + flowNo.length() + 2) + ") OR (" + colName +
			   " = 'ND" + flowNo + "Rpt' ) OR (" + colName + " LIKE 'ND" + flowNo + "__Dtl%' AND " + len + "(" +
			   colName + ")>" + ("ND".length() + flowNo.length() + 2 + "Dtl".length()) + ")";
	}
	/** 
	 短信时间发送从
	 默认从 8 点开始.
	*/
	public static int getSMSSendTimeFromHour()
	{
		try
		{
			return Integer.parseInt(BP.Sys.SystemConfig.AppSettings["SMSSendTimeFromHour"]);
		}
		catch (java.lang.Exception e)
		{
			return 8;
		}
	}
	/** 
	 短信时间发送到
	 默认到 20 点结束.
	*/
	public static int getSMSSendTimeToHour()
	{
		try
		{
			return Integer.parseInt(BP.Sys.SystemConfig.AppSettings["SMSSendTimeToHour"]);
		}
		catch (java.lang.Exception e)
		{
			return 8;
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion webconfig属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 常用方法
	private static String html = "";
	private static ArrayList htmlArr = new ArrayList();
	private static String backHtml = "";
	private static long workid = 0;
	/** 
	 模拟运行
	 
	 @param flowNo 流程编号
	 @param empNo 要执行的人员.
	 @return 执行信息.
	*/
	public static String Simulation_RunOne(String flowNo, String empNo, String paras)
	{
		backHtml = ""; //需要重新赋空值
		Hashtable ht = null;
		if (DataType.IsNullOrEmpty(paras) == false)
		{
			AtPara ap = new AtPara(paras);
			ht = ap.getHisHT();
		}

		Emp emp = new Emp(empNo);
		backHtml += " **** 开始使用:" + Glo.GenerUserImgSmallerHtml(emp.No, emp.Name) + "登录模拟执行工作流程";
		BP.WF.Dev2Interface.Port_Login(empNo);

		workid = BP.WF.Dev2Interface.Node_CreateBlankWork(flowNo, ht, null, emp.No, null);
		SendReturnObjs objs = BP.WF.Dev2Interface.Node_SendWork(flowNo, workid, ht);
		backHtml += objs.ToMsgOfHtml().replace("@", "<br>@"); //记录消息.


		String[] accepters = objs.getVarAcceptersID().split("[,]", -1);


		for (String acce : accepters)
		{
			if (DataType.IsNullOrEmpty(acce) == true)
			{
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
	private static void Simulation_Run_S1(String flowNo, long workid, String empNo, Hashtable ht, String beginEmp)
	{
		//htmlArr.Add(html);
		Emp emp = new Emp(empNo);
		//html = "";
		backHtml += "empNo" + beginEmp;
		backHtml += "<br> **** 让:" + Glo.GenerUserImgSmallerHtml(emp.No, emp.Name) + "执行模拟登录. ";
		// 让其登录.
		BP.WF.Dev2Interface.Port_Login(empNo);

		//执行发送.
		SendReturnObjs objs = BP.WF.Dev2Interface.Node_SendWork(flowNo, workid, ht);
		backHtml += "<br>" + objs.ToMsgOfHtml().replace("@", "<br>@");

		if (objs.getVarAcceptersID() == null)
		{
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

		for (String acce : accepters)
		{
			if (DataType.IsNullOrEmpty(acce) == true)
			{
				continue;
			}

			//执行发送.
			Simulation_Run_S1(flowNo, workid, acce, ht, beginEmp);
			break; //就不让其执行了.
		}
	}
	/** 
	 是否手机访问?
	 
	 @return 
	*/
	public static boolean IsMobile()
	{
		if (SystemConfig.IsBSsystem == false)
		{
			return false;
		}
		String agent = (HttpContextHelper.RequestUserAgent + "").toLowerCase().trim();
		if (agent.equals("") || agent.indexOf("mozilla") != -1 || agent.indexOf("opera") != -1)
		{
			return false;
		}
		return true;
	}
	/** 
	 加入track
	 
	 @param at 事件类型
	 @param flowNo 流程编号
	 @param workID 工作ID
	 @param fid 流程ID
	 @param fromNodeID 从节点编号
	 @param fromNodeName 从节点名称
	 @param fromEmpID 从人员ID
	 @param fromEmpName 从人员名称
	 @param toNodeID 到节点编号
	 @param toNodeName 到节点名称
	 @param toEmpID 到人员ID
	 @param toEmpName 到人员名称
	 @param note 消息
	 @param tag 参数用@分开
	*/
	public static String AddToTrack(ActionType at, String flowNo, long workID, long fid, int fromNodeID, String fromNodeName, String fromEmpID, String fromEmpName, int toNodeID, String toNodeName, String toEmpID, String toEmpName, String note, String tag)
	{
		if (toNodeID == 0)
		{
			toNodeID = fromNodeID;
			toNodeName = fromNodeName;
		}

		Track t = new Track();
		t.setWorkID(workID);
		t.setFID(fid);
		t.setRDT(DataType.getCurrentDataTime());
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

		//参数.
		if (tag != null)
		{
			t.setTag(tag);
		}

		try
		{
			t.Insert();
		}
		catch (java.lang.Exception e)
		{
			t.CheckPhysicsTable();
			t.Insert();
		}
		return t.getMyPK();
	}
	/** 
	 计算表达式是否通过(或者是否正确.)
	 
	 @param exp 表达式
	 @param en 实体
	 @return true/false
	*/
	public static boolean ExeExp(String exp, Entity en)
	{
		exp = exp.replace("@WebUser.getNo()", WebUser.getNo());
		exp = exp.replace("@WebUser.getName()", WebUser.getName());
		exp = exp.replace("@WebUser.getFK_DeptNameOfFull", WebUser.getFK_DeptNameOfFull);
		exp = exp.replace("@WebUser.getFK_DeptName", WebUser.getFK_DeptName);
		exp = exp.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());

		String[] strs = exp.split("[ ]", -1);
		boolean isPass = false;

		String key = strs[0].trim();
		String oper = strs[1].trim();
		String val = strs[2].trim();
		val = val.replace("'", "");
		val = val.replace("%", "");
		val = val.replace("~", "");
		BP.En.Row row = en.Row;
		for (String item : row.keySet())
		{
			if (!item.trim().equals(key))
			{
				continue;
			}

			String valPara = row[key].toString();
			if (oper.equals("="))
			{
				if (val.equals(valPara))
				{
					return true;
				}
			}

			if (oper.toUpperCase().equals("LIKE"))
			{
				if (valPara.contains(val))
				{
					return true;
				}
			}

			if (oper.equals(">"))
			{
				if (Float.parseFloat(valPara) > Float.parseFloat(val))
				{
					return true;
				}
			}
			if (oper.equals(">="))
			{
				if (Float.parseFloat(valPara) >= Float.parseFloat(val))
				{
					return true;
				}
			}
			if (oper.equals("<"))
			{
				if (Float.parseFloat(valPara) < Float.parseFloat(val))
				{
					return true;
				}
			}
			if (oper.equals("<="))
			{
				if (Float.parseFloat(valPara) <= Float.parseFloat(val))
				{
					return true;
				}
			}

			if (oper.equals("!="))
			{
				if (Float.parseFloat(valPara) != Float.parseFloat(val))
				{
					return true;
				}
			}

			throw new RuntimeException("@参数格式错误:" + exp + " Key=" + key + " oper=" + oper + " Val=" + val);
		}

		return false;
	}
	/** 
	 前置导航导入表单数据
	 
	 @param WorkID
	 @param FK_Flow
	 @param FK_Node
	 @param sKey 选中的No
	*/
	public static DataTable StartGuidEnties(long WorkID, String FK_Flow, int FK_Node, String sKey)
	{
		Flow fl = new Flow(FK_Flow);
		switch (fl.getStartGuideWay())
		{
			case SubFlowGuide:
			case BySQLOne:
				String sql = "";
				Object tempVar = fl.getStartGuidePara3().Clone();
				sql = tempVar instanceof String ? (String)tempVar : null; //@李国文.
				if (DataType.IsNullOrEmpty(sql) == false)
				{
					sql = sql.replace("@Key", sKey);
					sql = sql.replace("@key", sKey);
					sql = sql.replace("~", "'");
				}
				else
				{
					Object tempVar2 = fl.getStartGuidePara2().Clone();
					sql = tempVar2 instanceof String ? (String)tempVar2 : null;
				}

				//sql = " SELECT * FROM (" + sql + ") T WHERE T.NO='" + sKey + "' ";

				//替换变量
				sql = sql.replace("@WebUser.getNo()", WebUser.getNo());
				sql = sql.replace("@WebUser.getName()", WebUser.getName());
				sql = sql.replace("@WebUser.getFK_DeptName", WebUser.getFK_DeptName);
				sql = sql.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());


				DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
				if (dt.Rows.size() == 0)
				{
					throw new RuntimeException("err@没有找到那一行数据." + sql);
				}

				Hashtable ht = new Hashtable();
				//转换成ht表
				DataRow row = dt.Rows[0];
				for (int i = 0; i < row.Table.Columns.size(); i++)
				{
					switch (row.Table.Columns[i].ColumnName.toLowerCase())
					{
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
							if (ht.containsKey(row.Table.Columns[i].ColumnName) == true)
							{
								ht.put(row.Table.Columns[i].ColumnName, row.get(i)); //@李国文.
							}
							else
							{
								ht.put(row.Table.Columns[i].ColumnName, row.get(i));
							}
							break;
					}
				}
				//保存
				BP.WF.Dev2Interface.Node_SaveWork(FK_Flow, FK_Node, WorkID, ht);
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
	 执行PageLoad装载数据
	 
	 @param item
	 @param en
	 @param mattrs
	 @param dtls
	 @return 
	*/

	public static Entity DealPageLoadFull(Entity en, MapExt item, MapAttrs mattrs, MapDtls dtls, boolean isSelf, int nodeID)
	{
		return DealPageLoadFull(en, item, mattrs, dtls, isSelf, nodeID, 0);
	}

	public static Entity DealPageLoadFull(Entity en, MapExt item, MapAttrs mattrs, MapDtls dtls, boolean isSelf)
	{
		return DealPageLoadFull(en, item, mattrs, dtls, isSelf, 0, 0);
	}

	public static Entity DealPageLoadFull(Entity en, MapExt item, MapAttrs mattrs, MapDtls dtls)
	{
		return DealPageLoadFull(en, item, mattrs, dtls, false, 0, 0);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static Entity DealPageLoadFull(Entity en, MapExt item, MapAttrs mattrs, MapDtls dtls, bool isSelf = false, int nodeID = 0, long workID = 0)
	public static Entity DealPageLoadFull(Entity en, MapExt item, MapAttrs mattrs, MapDtls dtls, boolean isSelf, int nodeID, long workID)
	{
		if (item == null)
		{
			return en;
		}

		DataTable dt = null;
		String sql = item.Tag;
		if (DataType.IsNullOrEmpty(sql) == false)
		{
			/* 如果有填充主表的sql  */
			sql = Glo.DealExp(sql, en, null);

			if (DataType.IsNullOrEmpty(sql) == false)
			{
				if (sql.contains("@"))
				{
					throw new RuntimeException("设置的sql有错误可能有没有替换的变量:" + sql);
				}
				dt = DBAccess.RunSQLReturnTable(sql);
				if (dt.Rows.size() == 1)
				{
					DataRow dr = dt.Rows[0];
					for (DataColumn dc : dt.Columns)
					{
						//去掉一些不需要copy的字段.
						switch (dc.ColumnName)
						{
							case WorkAttr.OID:
							case WorkAttr.FID:
							case WorkAttr.Rec:
							case WorkAttr.MD5:
							case WorkAttr.MyNum:
							case WorkAttr.RDT:
							case "RefPK":
							case WorkAttr.RecText:
								continue;
							default:
								break;
						}

						if (DataType.IsNullOrEmpty(en.GetValStringByKey(dc.ColumnName)) || en.GetValStringByKey(dc.ColumnName).equals("0"))
						{
							en.SetValByKey(dc.ColumnName, dr.get(dc.ColumnName).toString());
						}
					}
				}
			}
		}

		if (DataType.IsNullOrEmpty(item.Tag1) || item.Tag1.Length < 15)
		{
			return en;
		}

		// 填充从表.
		for (MapDtl dtl : dtls.ToJavaList())
		{
			//如果有数据，就不要填充了.



			String[] sqls = item.Tag1.split("[*]", -1);
			for (String mysql : sqls)
			{
				if (DataType.IsNullOrEmpty(mysql))
				{
					continue;
				}
				if (mysql.contains(dtl.No + "=") == false)
				{
					continue;
				}
				if (mysql.equals(dtl.No + "=") == true)
				{
					continue;
				}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#region 处理sql.
				sql = Glo.DealExp(mysql, en, null);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion 处理sql.

				if (DataType.IsNullOrEmpty(sql))
				{
					continue;
				}

				if (sql.contains("@"))
				{
					throw new RuntimeException("设置的sql有错误可能有没有替换的变量:" + sql);
				}
				if (isSelf == true)
				{
					MapDtl mdtlSln = new MapDtl();
					mdtlSln.No = dtl.No + "_" + nodeID;
					int result = mdtlSln.RetrieveFromDBSources();
					if (result != 0)
					{
						//dtl.No = mdtlSln.No;
						dtl.DtlOpenType = mdtlSln.DtlOpenType;
					}
				}



				GEDtls gedtls = null;

				try
				{
					gedtls = new GEDtls(dtl.No);
					if (dtl.DtlOpenType == DtlOpenType.ForFID)
					{
						if (gedtls.RetrieveByAttr(GEDtlAttr.RefPK, workID) > 0)
						{
							continue;
						}
					}
					else
					{
						if (gedtls.RetrieveByAttr(GEDtlAttr.RefPK, en.PKVal) > 0)
						{
							continue;
						}
					}


					//gedtls.Delete(GEDtlAttr.RefPK, en.PKVal);
				}
				catch (RuntimeException ex)
				{
					(gedtls.GetNewEntity instanceof GEDtl ? (GEDtl)gedtls.GetNewEntity : null).CheckPhysicsTable();
				}

				dt = DBAccess.RunSQLReturnTable(sql.startsWith(dtl.No + "=") ? sql.substring((dtl.No + "=").length()) : sql);
				for (DataRow dr : dt.Rows)
				{
					GEDtl gedtl = gedtls.GetNewEntity instanceof GEDtl ? (GEDtl)gedtls.GetNewEntity : null;
					for (DataColumn dc : dt.Columns)
					{
						gedtl.SetValByKey(dc.ColumnName, dr.get(dc.ColumnName).toString());
					}

					switch (dtl.DtlOpenType)
					{
						case DtlOpenType.ForEmp: // 按人员来控制.
							gedtl.RefPK = en.PKVal.toString();
							gedtl.FID = Long.parseLong(en.PKVal.toString());
							break;
						case DtlOpenType.ForWorkID: // 按工作ID来控制
							gedtl.RefPK = en.PKVal.toString();
							gedtl.FID = Long.parseLong(en.PKVal.toString());
							break;
						case DtlOpenType.ForFID: // 按流程ID来控制.
							gedtl.RefPK = String.valueOf(workID);
							gedtl.FID = Long.parseLong(en.PKVal.toString());
							break;
					}
					gedtl.RDT = DataType.getCurrentDataTime();
					gedtl.Rec = WebUser.getNo();
					gedtl.Insert();
				}
			}
		}
		return en;
	}
	/** 
	 SQL表达式是否正确
	 
	 @param sqlExp
	 @param ht
	 @return 
	*/
	public static boolean CondExpSQL(String sqlExp, Hashtable ht, long myWorkID)
	{
		String sql = sqlExp;
		sql = sql.replace("~", "'");
		sql = sql.replace("@WebUser.getNo()", WebUser.getNo());
		sql = sql.replace("@WebUser.getName()", WebUser.getName());
		sql = sql.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());

		for (String key : ht.keySet())
		{
			if (key.equals("OID"))
			{
				sql = sql.replace("@WorkID", ht.get("OID").toString());
				sql = sql.replace("@OID", ht.get("OID").toString());
				continue;
			}
			sql = sql.replace("@" + key, ht.get(key).toString());
		}

		//从工作流参数里面替换
		if (sql.contains("@") == true && myWorkID != 0)
		{
			GenerWorkFlow gwf = new GenerWorkFlow(myWorkID);
			AtPara ap = gwf.atPara;
			for (String str : ap.getHisHT().keySet())
			{
				sql = sql.replace("@" + str, ap.GetValStrByKey(str));
			}
		}

		int result = DBAccess.RunSQLReturnValInt(sql, -1);
		if (result <= 0)
		{
			return false;
		}
		return true;
	}
	/** 
	 判断表达式是否成立
	 
	 @param exp 表达式
	 @param en 变量
	 @return 是否成立
	*/
	public static boolean CondExpPara(String exp, Hashtable ht, long myWorkID)
	{
		try
		{
			String[] strs = exp.trim().split("[ ]", -1);

			String key = strs[0].trim();
			String oper = strs[1].trim();
			String val = strs[2].trim();

			val = val.replace("'", "");
			val = val.replace("%", "");
			val = val.replace("~", "");

			String valPara = null;
			if (ht.containsKey(key) == false)
			{

				boolean isHave = false;
				if (myWorkID != 0)
				{
					//把外部传来的参数传入到 rptGE 让其做方向条件的判断.
					GenerWorkFlow gwf = new GenerWorkFlow(myWorkID);
					AtPara at = gwf.atPara;
					for (String str : at.HisHT.keySet())
					{
						if (key.equals(str) == false)
						{
							continue;
						}

						valPara = at.GetValStrByKey(key);
						isHave = true;
						break;
					}
				}

				if (isHave == false)
				{
					try
					{
						/*如果不包含指定的关键的key, 就到公共变量里去找. */
						if (BP.WF.Glo.getSendHTOfTemp().containsKey(key) == false)
						{
							throw new RuntimeException("@判断条件时错误,请确认参数是否拼写错误,没有找到对应的表达式:" + exp + " Key=(" + key + ") oper=(" + oper + ")Val=(" + val + ")");
						}
						valPara = BP.WF.Glo.getSendHTOfTemp().get(key).toString().trim();
					}
					catch (java.lang.Exception e)
					{
						//有可能是常量. 
						valPara = key;
					}
				}
			}
			else
			{
				valPara = ht.get(key).toString().trim();
			}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 开始执行判断.
			if (oper.equals("="))
			{
				if (val.equals(valPara))
				{
					return true;
				}
				else
				{
					return false;
				}
			}

			if (oper.toUpperCase().equals("LIKE"))
			{
				if (valPara.contains(val))
				{
					return true;
				}
				else
				{
					return false;
				}
			}


			if (DataType.IsNumStr(valPara) == false)
			{
				throw new RuntimeException("err@表达式错误:[" + exp + "]没有找到参数[" + valPara + "]的值，导致无法计算。");
			}

			if (oper.equals(">"))
			{
				if (Float.parseFloat(valPara) > Float.parseFloat(val))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			if (oper.equals(">="))
			{
				if (Float.parseFloat(valPara) >= Float.parseFloat(val))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			if (oper.equals("<"))
			{
				if (Float.parseFloat(valPara) < Float.parseFloat(val))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			if (oper.equals("<="))
			{
				if (Float.parseFloat(valPara) <= Float.parseFloat(val))
				{
					return true;
				}
				else
				{
					return false;
				}
			}

			if (oper.equals("!="))
			{
				if (Float.parseFloat(valPara) != Float.parseFloat(val))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			throw new RuntimeException("@参数格式错误:" + exp + " Key=" + key + " oper=" + oper + " Val=" + val);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 开始执行判断.

		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("计算参数的时候出现错误:" + ex.getMessage());
		}
	}
	/** 
	 表达式替换
	 
	 @param exp
	 @param en
	 @return 
	*/
	public static String DealExp(String exp, Entity en)
	{
		//替换字符
		exp = exp.replace("~", "'");

		if (exp.contains("@") == false)
		{
			return exp;
		}

		//首先替换加; 的。
		exp = exp.replace("@WebUser.getNo();", WebUser.getNo());
		exp = exp.replace("@WebUser.getName();", WebUser.getName());
		exp = exp.replace("@WebUser.getFK_DeptName;", WebUser.getFK_DeptName);
		exp = exp.replace("@WebUser.getFK_Dept();", WebUser.getFK_Dept());


		// 替换没有 ; 的 .
		exp = exp.replace("@WebUser.getNo()", WebUser.getNo());
		exp = exp.replace("@WebUser.getName()", WebUser.getName());
		exp = exp.replace("@WebUser.getFK_DeptName", WebUser.getFK_DeptName);
		exp = exp.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());

		if (exp.contains("@") == false)
		{
			return exp;
		}

		//增加对新规则的支持. @MyField; 格式.
		if (en != null)
		{
			Attrs attrs = en.getEnMap().getAttrs();
			Row row = en.Row;
			//特殊判断.
			if (row.ContainsKey("OID") == true)
			{
				exp = exp.replace("@WorkID", row["OID"].toString());
			}

			if (exp.contains("@") == false)
			{
				return exp;
			}

			for (String key : row.keySet())
			{
				//值为空或者null不替换
				if (row[key] == null || row[key].equals("") == true)
				{
					continue;
				}

				if (exp.contains("@" + key))
				{
					Attr attr = attrs.GetAttrByKeyOfEn(key);
					//是枚举或者外键替换成文本
					if (attr.MyFieldType == FieldType.Enum || attr.MyFieldType == FieldType.PKEnum || attr.MyFieldType == FieldType.FK || attr.MyFieldType == FieldType.PKFK)
					{
						exp = exp.replace("@" + key, row[key + "Text"].toString());
					}
					else
					{
						if (attr.MyDataType == DataType.AppString && attr.UIContralType == UIContralType.DDL && attr.MyFieldType == FieldType.Normal)
						{
							 exp = exp.replace("@" + key, row[key + "T"].toString());
						}
						else
						{
							exp = exp.replace("@" + key, row[key].toString());
						}
;
					}


				}

				//不包含@则返回SQL语句
				if (exp.contains("@") == false)
				{
					return exp;
				}
			}

		}

		if (exp.contains("@") && SystemConfig.IsBSsystem == true)
		{
			/*如果是bs*/
			for (String key : HttpContextHelper.RequestParamKeys)
			{
				if (DataType.IsNullOrEmpty(key))
				{
					continue;
				}
				exp = exp.replace("@" + key, HttpContextHelper.RequestParams(key));
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
		return exp;
	}
	//
	/** 
	 处理表达式
	 
	 @param exp 表达式
	 @param en 数据源
	 @param errInfo 错误
	 @return 
	*/
	public static String DealExp(String exp, Entity en, String errInfo)
	{
		//替换字符
		exp = exp.replace("~", "'");

		if (exp.contains("@") == false)
		{
			return exp;
		}

		//首先替换加; 的。
		exp = exp.replace("@WebUser.getNo();", WebUser.getNo());
		exp = exp.replace("@WebUser.getName();", WebUser.getName());
		exp = exp.replace("@WebUser.getFK_DeptName;", WebUser.getFK_DeptName);
		exp = exp.replace("@WebUser.getFK_Dept();", WebUser.getFK_Dept());


		// 替换没有 ; 的 .
		exp = exp.replace("@WebUser.getNo()", WebUser.getNo());
		exp = exp.replace("@WebUser.getName()", WebUser.getName());
		exp = exp.replace("@WebUser.getFK_DeptName", WebUser.getFK_DeptName);
		exp = exp.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());

		if (exp.contains("@") == false)
		{
			return exp;
		}

		//增加对新规则的支持. @MyField; 格式.
		if (en != null)
		{
			Row row = en.Row;
			//特殊判断.
			if (row.ContainsKey("OID") == true)
			{
				exp = exp.replace("@WorkID", row["OID"].toString());
			}

			if (exp.contains("@") == false)
			{
				return exp;
			}

			for (String key : row.keySet())
			{
				//值为空或者null不替换
				if (row[key] == null || row[key].equals("") == true)
				{
					continue;
				}

				if (exp.contains("@" + key + ";"))
				{
					exp = exp.replace("@" + key + ";", row[key].toString());
				}

				//不包含@则返回SQL语句
				if (exp.contains("@") == false)
				{
					return exp;
				}
			}


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 解决排序问题.
			Attrs attrs = en.getEnMap().getAttrs();
			String mystrs = "";
			for (Attr attr : attrs)
			{
				if (attr.MyDataType == DataType.AppString)
				{
					mystrs += "@" + attr.Key + ",";
				}
				else
				{
					mystrs += "@" + attr.Key;
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
				dr.set(0, str);
				dt.Rows.add(dr);
			}
			DataView dv = dt.DefaultView;
			dv.Sort = "No DESC";
			DataTable dtNew = dv.Table;
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion  解决排序问题.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 替换变量.
			for (DataRow dr : dtNew.Rows)
			{
				String key = dr.get(0).toString();
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

			if (exp.contains("@") == false)
			{
				return exp;
			}
		}

		// 处理Para的替换.
		if (exp.contains("@") && Glo.getSendHTOfTemp() != null)
		{
			for (String key : Glo.getSendHTOfTemp().keySet())
			{
				exp = exp.replace("@" + key, Glo.getSendHTOfTemp().get(key).toString());
			}
		}

		if (exp.contains("@") && SystemConfig.IsBSsystem == true)
		{
			/*如果是bs*/
			for (String key : HttpContextHelper.RequestParamKeys)
			{
				if (DataType.IsNullOrEmpty(key))
				{
					continue;
				}
				exp = exp.replace("@" + key, HttpContextHelper.RequestParams(key));
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
		return exp;
	}
	/** 
	 加密MD5
	 
	 @param s
	 @return 
	*/
	public static String GenerMD5(BP.WF.Work wk)
	{
		String s = null;
		for (Attr attr : wk.getEnMap().getAttrs())
		{
			switch (attr.getKey())
			{
				case WorkAttr.MD5:
				case WorkAttr.RDT:
				case WorkAttr.CDT:
				case WorkAttr.Rec:
				case StartWorkAttr.Title:
				case StartWorkAttr.Emps:
				case StartWorkAttr.FK_Dept:
				case StartWorkAttr.PRI:
				case StartWorkAttr.FID:
					continue;
				default:
					break;
			}

			String obj = attr.DefaultVal instanceof String ? (String)attr.DefaultVal : null;
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
	 
	 @param input 源明文字符串
	 @return 密文字符串
	*/
	public static String GetMD5Hash(String input)
	{
		System.Security.Cryptography.MD5CryptoServiceProvider md5 = new System.Security.Cryptography.MD5CryptoServiceProvider();
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte[] bs = System.Text.Encoding.UTF8.GetBytes(input);
		byte[] bs = input.getBytes(java.nio.charset.StandardCharsets.UTF_8);
		bs = md5.ComputeHash(bs);
		StringBuilder s = new StringBuilder();
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: foreach (byte b in bs)
		for (byte b : bs)
		{
			s.append(String.format("%02x", b).toLowerCase());
		}
		return s.toString();
	}

	/** 
	 装载流程数据 
	 
	 @param xlsFile
	*/
	public static String LoadFlowDataWithToSpecNode(String xlsFile)
	{
		DataTable dt = BP.DA.DBLoad.ReadExcelFileToDataTable(xlsFile);
		String err = "";
		String info = "";

		for (DataRow dr : dt.Rows)
		{
			String flowPK = dr.get("FlowPK").toString();
			String starter = dr.get("Starter").toString();
			String executer = dr.get("Executer").toString();
			int toNode = Integer.parseInt(dr.get("ToNodeID").toString().replace("ND", ""));
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

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 检查数据是否完整。
			BP.Port.Emp emp = new BP.Port.Emp();
			emp.No = executer;
			if (emp.RetrieveFromDBSources() == 0)
			{
				err += "@账号:" + starter + ",不存在。";
				continue;
			}
			if (DataType.IsNullOrEmpty(emp.FK_Dept))
			{
				err += "@账号:" + starter + ",没有部门。";
				continue;
			}

			emp.No = starter;
			if (emp.RetrieveFromDBSources() == 0)
			{
				err += "@账号:" + executer + ",不存在。";
				continue;
			}
			if (DataType.IsNullOrEmpty(emp.FK_Dept))
			{
				err += "@账号:" + executer + ",没有部门。";
				continue;
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 检查数据是否完整。

			WebUser.SignInOfGener(emp);
			Flow fl = nd.getHisFlow();
			Work wk = fl.NewWork();

			Attrs attrs = wk.getEnMap().getAttrs();
			//foreach (Attr attr in wk.getEnMap().getAttrs())
			//{
			//}

			for (DataColumn dc : dt.Columns)
			{
				Attr attr = attrs.GetAttrByKey(dc.ColumnName.trim());
				if (attr == null)
				{
					continue;
				}

				String val = dr.get(dc.ColumnName).toString().trim();
				switch (attr.MyDataType)
				{
					case DataType.AppString:
					case DataType.AppDate:
					case DataType.AppDateTime:
						wk.SetValByKey(attr.Key, val);
						break;
					case DataType.AppInt:
					case DataType.AppBoolean:
						wk.SetValByKey(attr.Key, Integer.parseInt(val));
						break;
					case DataType.AppMoney:
					case DataType.AppDouble:
					case DataType.AppFloat:
						wk.SetValByKey(attr.Key, BigDecimal.Parse(val));
						break;
					default:
						wk.SetValByKey(attr.Key, val);
						break;
				}
			}

			wk.SetValByKey(WorkAttr.Rec, WebUser.getNo());
			wk.SetValByKey(StartWorkAttr.FK_Dept, WebUser.getFK_Dept());
			wk.SetValByKey("FK_NY", DataType.CurrentYearMonth);
			wk.SetValByKey(WorkAttr.MyNum, 1);
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

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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

				String val = dr.get(dc.ColumnName).toString().trim();
				switch (attr.MyDataType)
				{
					case DataType.AppString:
					case DataType.AppDate:
					case DataType.AppDateTime:
						wkNext.SetValByKey(attr.Key, val);
						break;
					case DataType.AppInt:
					case DataType.AppBoolean:
						wkNext.SetValByKey(attr.Key, Integer.parseInt(val));
						break;
					case DataType.AppMoney:
					case DataType.AppDouble:
					case DataType.AppFloat:
						wkNext.SetValByKey(attr.Key, BigDecimal.Parse(val));
						break;
					default:
						wkNext.SetValByKey(attr.Key, val);
						break;
				}
			}

			wkNext.DirectUpdate();

			GERpt rtp = fl.getHisGERpt();
			rtp.SetValByKey("OID", wkNext.getOID());
			rtp.RetrieveFromDBSources();
			rtp.Copy(wkNext);
			rtp.DirectUpdate();

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 更新 下一个节点数据。
		}
		return info + err;
	}
	public static String LoadFlowDataWithToSpecEndNode(String xlsFile)
	{
		DataTable dt = BP.DA.DBLoad.ReadExcelFileToDataTable(xlsFile);
		DataSet ds = new DataSet();
		ds.Tables.add(dt);
		ds.WriteXml("C:\\已完成.xml");

		String err = "";
		String info = "";
		int idx = 0;
		for (DataRow dr : dt.Rows)
		{
			String flowPK = dr.get("FlowPK").toString().trim();
			if (DataType.IsNullOrEmpty(flowPK))
			{
				continue;
			}

			String starter = dr.get("Starter").toString();
			String executer = dr.get("Executer").toString();
			int toNode = Integer.parseInt(dr.get("ToNodeID").toString().replace("ND", ""));
			Node ndOfEnd = new Node();
			ndOfEnd.setNodeID(toNode);
			if (ndOfEnd.RetrieveFromDBSources() == 0)
			{
				err += "节点ID错误:" + toNode;
				continue;
			}

			if (ndOfEnd.getIsEndNode() == false)
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

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 检查数据是否完整。
			//发起人发起。
			BP.Port.Emp emp = new BP.Port.Emp();
			emp.No = executer;
			if (emp.RetrieveFromDBSources() == 0)
			{
				err += "@账号:" + starter + ",不存在。";
				continue;
			}

			if (DataType.IsNullOrEmpty(emp.FK_Dept))
			{
				err += "@账号:" + starter + ",没有设置部门。";
				continue;
			}

			emp = new BP.Port.Emp();
			emp.No = starter;
			if (emp.RetrieveFromDBSources() == 0)
			{
				err += "@账号:" + starter + ",不存在。";
				continue;
			}
			else
			{
				emp.RetrieveFromDBSources();
				if (DataType.IsNullOrEmpty(emp.FK_Dept))
				{
					err += "@账号:" + starter + ",没有设置部门。";
					continue;
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 检查数据是否完整。


			WebUser.SignInOfGener(emp);
			Flow fl = ndOfEnd.getHisFlow();
			Work wk = fl.NewWork();
			for (DataColumn dc : dt.Columns)
			{
				wk.SetValByKey(dc.ColumnName.trim(), dr.get(dc.ColumnName).toString().trim());
			}

			wk.SetValByKey(WorkAttr.Rec, WebUser.getNo());
			wk.SetValByKey(StartWorkAttr.FK_Dept, WebUser.getFK_Dept());
			wk.SetValByKey("FK_NY", DataType.CurrentYearMonth);
			wk.SetValByKey(WorkAttr.MyNum, 1);
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
			emp = new BP.Port.Emp(executer);
			WebUser.SignInOfGener(emp);

			Work wkEnd = ndOfEnd.GetWork(wk.getOID());
			for (DataColumn dc : dt.Columns)
			{
				wkEnd.SetValByKey(dc.ColumnName.trim(), dr.get(dc.ColumnName).toString().trim());
			}

			wkEnd.SetValByKey(WorkAttr.Rec, WebUser.getNo());
			wkEnd.SetValByKey(StartWorkAttr.FK_Dept, WebUser.getFK_Dept());
			wkEnd.SetValByKey("FK_NY", DataType.CurrentYearMonth);
			wkEnd.SetValByKey(WorkAttr.MyNum, 1);
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
	 
	 @param userNo
	*/
	public static void IsSingleUser(String userNo)
	{
		if (DataType.IsNullOrEmpty(WebUser.getNo()) || !userNo.equals(WebUser.getNo()))
		{
			if (!DataType.IsNullOrEmpty(userNo))
			{
				BP.WF.Dev2Interface.Port_Login(userNo);
			}
		}
	}
	//public static void ResetFlowView()
	//{
	//    string sql = "DROP VIEW V_WF_Data ";
	//    try
	//    {
	//        BP.DA.DBAccess.RunSQL(sql);
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
	//        sql += "\t\n SELECT '" + fl.getNo() + "' as FK_Flow, '" + fl.getName() + "' AS FlowName, '" + fl.FK_FlowSort + "' as FK_FlowSort,CDT,Emps,FID,FK_Dept,FK_NY,";
	//        sql += "MyNum,OID,RDT,Rec,Title,WFState,FlowEmps,";
	//        sql += "FlowStarter,FlowStartRDT,FlowEnder,FlowEnderRDT,FlowDaySpan FROM ND" + int.Parse(fl.No) + "Rpt";
	//        sql += "\t\n  UNION";
	//    }
	//    sql = sql.Substring(0, sql.Length - 6);
	//    sql += "\t\n GO";
	//    BP.DA.DBAccess.RunSQL(sql);
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 常用方法

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	/** 
	 消息
	*/
	public static String getSessionMsg()
	{
		Paras p = new Paras();
		p.SQL = "SELECT Msg FROM WF_Emp where No=" + SystemConfig.getAppCenterDBVarStr() + "FK_Emp";
		p.AddFK_Emp();
		return DBAccess.RunSQLReturnString(p);
	}
	public static void setSessionMsg(String value)
	{
		if (DataType.IsNullOrEmpty(value) == true)
		{
			return;
		}
		Paras p = new Paras();
		p.SQL = "UPDATE WF_Emp SET Msg=" + SystemConfig.getAppCenterDBVarStr() + "v WHERE No=" + SystemConfig.getAppCenterDBVarStr() + "FK_Emp";
		p.Add("v", value);
		p.AddFK_Emp();

		int i = DBAccess.RunSQL(p);
		if (i == 0)
		{
				/*如果没有更新到.*/
			WFEmp emp = new WFEmp();
			emp.No = WebUser.getNo();
			emp.Name = WebUser.getName();
			emp.setFK_Dept(WebUser.getFK_Dept());
			emp.setEmail((new BP.GPM.Emp(WebUser.getNo())).Email);
			emp.Insert();
			DBAccess.RunSQL(p);
		}
	}

	private static String _FromPageType = null;
	public static String getFromPageType()
	{
		_FromPageType = null;
		if (_FromPageType == null)
		{
			try
			{
				String url = HttpContextHelper.RequestRawUrl;
				int i = url.lastIndexOf("/") + 1;
				int i2 = url.indexOf(".aspx") - 6;

				url = url.substring(i);
				url = url.substring(0, url.indexOf(".aspx"));
				_FromPageType = url;
				if (_FromPageType.contains("SmallSingle"))
				{
					_FromPageType = "SmallSingle";
				}
				else if (_FromPageType.contains("Small"))
				{
					_FromPageType = "Small";
				}
				else
				{
					_FromPageType = "";
				}
			}
			catch (RuntimeException ex)
			{
				_FromPageType = "";
					//  throw new Exception(ex.Message + url + " i=" + i + " i2=" + i2);
			}
		}
		return _FromPageType;
	}
	private static Hashtable _SendHTOfTemp = null;
	/** 
	 临时的发送传输变量.
	*/
	public static Hashtable getSendHTOfTemp()
	{
		if (_SendHTOfTemp == null)
		{
			_SendHTOfTemp = new Hashtable();
		}
		return _SendHTOfTemp.get(WebUser.getNo()) instanceof Hashtable ? (Hashtable)_SendHTOfTemp.get(WebUser.getNo()) : null;
	}
	public static void setSendHTOfTemp(Hashtable value)
	{
		if (value == null)
		{
			return;
		}

		if (_SendHTOfTemp == null)
		{
			_SendHTOfTemp = new Hashtable();
		}
		_SendHTOfTemp.put(WebUser.getNo(), value);
	}
	/** 
	 报表属性集合
	*/
	private static Attrs _AttrsOfRpt = null;
	/** 
	 报表属性集合
	*/
	public static Attrs getAttrsOfRpt()
	{
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
				//attr.Key = "WFState";
				//attr.MyFieldType = FieldType.Enum;
				//attr.UIBindKey = "WFState";
				//attr.UITag = "@0=进行中@1=已经完成";

			_AttrsOfRpt.AddDDLSysEnum(GERptAttr.WFState, 0, "流程状态", true, true, GERptAttr.WFState);
			_AttrsOfRpt.AddTBString(GERptAttr.FlowEmps, null, "参与人", true, false, 0, 10, 10);
			_AttrsOfRpt.AddTBString(GERptAttr.FlowEnder, null, "结束人", true, false, 0, 10, 10);
			_AttrsOfRpt.AddTBString(GERptAttr.FlowEnderRDT, null, "最后处理时间", true, false, 0, 10, 10);
			_AttrsOfRpt.AddTBInt(GERptAttr.FlowEndNode, 0, "停留节点", true, false);
			_AttrsOfRpt.AddTBDecimal(GERptAttr.FlowDaySpan, 0, "跨度(天)", true, false);
				//_AttrsOfRpt.AddTBString(GERptAttr.FK_NY, null, "隶属月份", true, false, 0, 10, 10);
		}
		return _AttrsOfRpt;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 属性

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 其他配置.

	/** 
	 帮助
	 
	 @param id1
	 @param id2
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
	public static String getNodeImagePath()
	{
		return Glo.getIntallPath() + "\\Data\\Node\\";
	}
	public static void ClearDBData()
	{
		String sql = "DELETE FROM WF_GenerWorkFlow WHERE fk_flow not in (select no from wf_flow )";
		BP.DA.DBAccess.RunSQL(sql);

		sql = "DELETE FROM WF_GenerWorkerlist WHERE fk_flow not in (select no from wf_flow )";
		BP.DA.DBAccess.RunSQL(sql);
	}
	public static String OEM_Flag = "CCS";
	public static String getFlowFileBill()
	{
		return Glo.getIntallPath() + "\\DataUser\\Bill\\";
	}
	private static String _IntallPath = null;
	public static String getIntallPath()
	{
		if (_IntallPath == null)
		{
			if (SystemConfig.IsBSsystem == true)
			{
				_IntallPath = SystemConfig.PathOfWebApp;
			}
		}

		if (_IntallPath == null)
		{
			throw new RuntimeException("@没有实现如何获得 cs 下的根目录.");
		}

		return _IntallPath;
	}
	public static void setIntallPath(String value)
	{
		_IntallPath = value;
	}
	private static String _ServerIP = null;
	public static String getServerIP()
	{
		if (_ServerIP == null)
		{
			String ip = "127.0.0.1";
			System.Net.IPAddress[] addressList = System.Net.Dns.GetHostByName(System.Net.Dns.GetHostName()).AddressList;
			if (addressList.length > 1)
			{
				_ServerIP = addressList[1].toString();
			}
			else
			{
				_ServerIP = addressList[0].toString();
			}
		}
		return _ServerIP;
	}
	public static void setServerIP(String value)
	{
		_ServerIP = value;
	}
	/** 
	 全局的安全验证码
	*/
	public static String getGloSID()
	{
		String s = BP.Sys.SystemConfig.AppSettings["GloSID"] instanceof String ? (String)BP.Sys.SystemConfig.AppSettings["GloSID"] : null;
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
	public static boolean getIsEnableCheckUseSta()
	{
		String s = BP.Sys.SystemConfig.AppSettings["IsEnableCheckUseSta"] instanceof String ? (String)BP.Sys.SystemConfig.AppSettings["IsEnableCheckUseSta"] : null;
		if (s == null || s.equals("0"))
		{
			return false;
		}
		return true;
	}
	/** 
	 是否启用显示节点名称
	*/
	public static boolean getIsEnableMyNodeName()
	{
		String s = BP.Sys.SystemConfig.AppSettings["IsEnableMyNodeName"] instanceof String ? (String)BP.Sys.SystemConfig.AppSettings["IsEnableMyNodeName"] : null;
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
	public static boolean CheckIsEnableWFEmp()
	{
		Paras ps = new Paras();
		ps.SQL = "SELECT UseSta FROM WF_Emp WHERE No=" + SystemConfig.getAppCenterDBVarStr() + "FK_Emp";
		ps.AddFK_Emp();
		String s = DBAccess.RunSQLReturnStringIsNull(ps, "1");
		if (s.equals("1") || s == null)
		{
			return true;
		}
		return false;
	}
	/** 
	 语言
	*/
	public static String Language = "CH";
	public static boolean getIsQL()
	{
		String s = BP.Sys.SystemConfig.AppSettings["IsQL"];
		if (s == null || s.equals("0"))
		{
			return false;
		}
		return true;
	}
	/** 
	 是否启用共享任务池？
	*/
	public static boolean getIsEnableTaskPool()
	{
		return BP.Sys.SystemConfig.GetValByKeyBoolen("IsEnableTaskPool", false);
	}
	/** 
	 是否显示标题
	*/
	public static boolean getIsShowTitle()
	{
		return BP.Sys.SystemConfig.GetValByKeyBoolen("IsShowTitle", false);
	}

	/** 
	 用户信息显示格式
	*/
	public static UserInfoShowModel getUserInfoShowModel()
	{
		return UserInfoShowModel.forValue(BP.Sys.SystemConfig.GetValByKeyInt("UserInfoShowModel", 0));
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
	public static String getUpdataMainDeptSQL()
	{
		return BP.Sys.SystemConfig.GetValByKey("UpdataMainDeptSQL", "UPDATE Port_Emp SET FK_Dept=" + BP.Sys.SystemConfig.getAppCenterDBVarStr() + "FK_Dept WHERE No=" + BP.Sys.SystemConfig.getAppCenterDBVarStr() + "No");
	}
	/** 
	 更新SID的SQL
	*/
	public static String getUpdataSID()
	{
		return BP.Sys.SystemConfig.GetValByKey("UpdataSID", "UPDATE Port_Emp SET SID=" + BP.Sys.SystemConfig.getAppCenterDBVarStr() + "SID WHERE No=" + BP.Sys.SystemConfig.getAppCenterDBVarStr() + "No");
	}
	/** 
	 处理显示格式
	 
	 @param no
	 @param name
	 @return 现实格式
	*/
	public static String DealUserInfoShowModel(String no, String name)
	{
		switch (BP.WF.Glo.getUserInfoShowModel())
		{
			case UserIDOnly:
				return "(" + no + ")";
			case UserIDUserName:
				return "(" + no + "," + name + ")";
			case UserNameOnly:
				return "(" + name + ")";
			default:
				throw new RuntimeException("@没有判断的格式类型.");
				break;
		}
	}

	/** 
	 处理人员显示格式
	 <p>added by liuxc,2017-4-27</p>
	 
	 @param emps 人员字符串，类似"duqinglian,杜清莲;wangyihan,王一涵;"
	 @param idBefore 是否用户id在前面、用户name在后面
	 @return 
	*/

	public static String DealUserInfoShowModel(String emps)
	{
		return DealUserInfoShowModel(emps, true);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static string DealUserInfoShowModel(string emps, bool idBefore = true)
	public static String DealUserInfoShowModel(String emps, boolean idBefore)
	{
		if (DataType.IsNullOrEmpty(emps))
		{
			return emps;
		}

		boolean haveKH = emps.startsWith("(");

		if (haveKH)
		{
			emps = emps.replace("(", "").replace(")", "");
		}

		String[] es = emps.split(";".toCharArray(), StringSplitOptions.RemoveEmptyEntries);
//C# TO JAVA CONVERTER TODO TASK: C# to Java Converter could not resolve the named parameters in the following line:
//ORIGINAL LINE: string newEmps = haveKH ? "(" : string.Empty;
		String newEmps = haveKH ? "(" : "";
		String[] ess = null;

		switch (BP.WF.Glo.getUserInfoShowModel())
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
	public static boolean getIsEnable_DingDing()
	{
			//如果两个参数都不为空说明启用
		String corpid = BP.Sys.SystemConfig.Ding_CorpID;
		String corpsecret = BP.Sys.SystemConfig.Ding_CorpSecret;
		if (DataType.IsNullOrEmpty(corpid) || DataType.IsNullOrEmpty(corpsecret))
		{
			return false;
		}

		return true;
	}
	/** 
	 微信是否启用
	*/
	public static boolean getIsEnable_WeiXin()
	{
			//如果两个参数都不为空说明启用
		String corpid = BP.Sys.SystemConfig.WX_CorpID;
		String corpsecret = BP.Sys.SystemConfig.WX_AppSecret;
		if (DataType.IsNullOrEmpty(corpid) || DataType.IsNullOrEmpty(corpsecret))
		{
			return false;
		}

		return true;
	}
	/** 
	 运行模式
	*/
	public static OSModel getOSModel()
	{
		return getOSModel().OneMore;

//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
		var model = BP.Sys.SystemConfig.GetValByKeyInt("OSModel", -1);
		return getOSModel().OneMore;

			// OSModel os = (OSModel)BP.Sys.SystemConfig.GetValByKeyInt("OSModel", 0);
			// return os;
	}
	/** 
	 是否检查表单树字段填写是否为空
	*/
	public static boolean getIsEnableCheckFrmTreeIsNull()
	{
		return BP.Sys.SystemConfig.GetValByKeyBoolen("IsEnableCheckFrmTreeIsNull", true);
	}
	/** 
	 是否启用消息系统消息。
	*/
	public static boolean getIsEnableSysMessage()
	{
		return BP.Sys.SystemConfig.GetValByKeyBoolen("IsEnableSysMessage", true);
	}
	/** 
	 与ccflow流程服务相关的配置: 执行自动任务节点，间隔的时间，以分钟计算，默认为2分钟。
	*/
	public static int getAutoNodeDTSTimeSpanMinutes()
	{
		return BP.Sys.SystemConfig.GetValByKeyInt("AutoNodeDTSTimeSpanMinutes", 60);
	}
	/** 
	 ccim集成的数据库.
	 是为了向ccim写入消息.
	*/
	public static String getCCIMDBName()
	{
		String baseUrl = BP.Sys.SystemConfig.AppSettings["CCIMDBName"];
		if (DataType.IsNullOrEmpty(baseUrl) == true)
		{
			baseUrl = "ccPort.dbo";
		}
		return baseUrl;
	}
	/** 
	 主机
	*/
	public static String getHostURL()
	{
		if (BP.Sys.SystemConfig.IsBSsystem)
		{
				/* 如果是BS 就要求 路径.*/
		}

		String baseUrl = BP.Sys.SystemConfig.AppSettings["HostURL"];
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
	public static String getMobileURL()
	{
		if (BP.Sys.SystemConfig.IsBSsystem)
		{
				/* 如果是BS 就要求 路径.*/
		}

		String baseUrl = BP.Sys.SystemConfig.AppSettings["BpmMobileAddress"];
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 时间计算.
	/** 
	 设置成工作时间
	 
	 @param DateTime
	 @return 
	*/
	public static LocalDateTime SetToWorkTime(LocalDateTime dt)
	{
		if (BP.Sys.GloVar.Holidays.Contains(dt.toString("MM-dd")))
		{
			dt = dt.plusDays(1);
			/*如果当前是节假日，就要从下一个有效期计算。*/
			while (true)
			{
				if (BP.Sys.GloVar.Holidays.Contains(dt.toString("MM-dd")) == false)
				{
					break;
				}
				dt = dt.plusDays(1);
			}

			//从下一个上班时间计算.
			dt = DataType.ParseSysDate2DateTime(dt.toString("yyyy-MM-dd") + " " + Glo.getAMFrom());
			return dt;
		}

		int timeInt = Integer.parseInt(dt.toString("HHmm"));

		//判断是否在A区间, 如果是，就返回A区间的时间点.
		if (Glo.getAMFromInt() >= timeInt)
		{
			return DataType.ParseSysDate2DateTime(dt.toString("yyyy-MM-dd") + " " + Glo.getAMFrom());
		}


		//判断是否在E区间, 如果是就返回第2天的上班时间点.
		if (Glo.getPMToInt() <= timeInt)
		{
			return DataType.ParseSysDate2DateTime(dt.toString("yyyy-MM-dd") + " " + Glo.getPMTo());
		}

		//如果在午休时间点中间.
		if (Glo.getAMToInt() <= timeInt && Glo.getPMFromInt() > timeInt)
		{
			return DataType.ParseSysDate2DateTime(dt.toString("yyyy-MM-dd") + " " + Glo.getPMFrom());
		}
		return dt;
	}
	/** 
	 在指定的日期上增加小时数。
	 1，扣除午休。
	 2，扣除节假日。
	 
	 @param dt
	 @param hours
	 @return 
	*/
	private static LocalDateTime AddMinutes(LocalDateTime dt, int hh, int minutes)
	{
		if (1 == 1)
		{
			dt = dt.plusHours(hh);
			dt = dt.plusMinutes(minutes);
			return dt;
		}

		//如果没有设置,就返回.
		if (minutes == 0 && hh == 0)
		{
			return dt;
		}

		//设置成工作时间.
		dt = SetToWorkTime(dt);

		//首先判断是否是在一天整的时间完成.
		if (minutes == Glo.getAMPMHours() * 60)
		{
			/*如果需要在一天完成*/
			dt = DataType.AddDays(dt, 1, TWay.Holiday);
			return dt;
		}

		//判断是否是AM.
		boolean isAM = false;
		int timeInt = Integer.parseInt(dt.toString("HHmm"));
		if (Glo.getAMToInt() > timeInt)
		{
			isAM = true;
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 如果是当天的情况.
		//如果规定的时间在 1天之内.
		if (minutes / 60 / Glo.getAMPMHours() < 1)
		{
			if (isAM == true)
			{
				/*如果是中午, 中午到中午休息之间的时间. */

				TimeSpan ts = DataType.ParseSysDateTime2DateTime(dt.toString("yyyy-MM-dd") + " " + Glo.getAMTo()) - dt;
				if (ts.TotalMinutes >= minutes)
				{
					/*如果剩余的分钟大于 要增加的分钟数，就是说+上分钟后，仍然在中午，就直接增加上这个分钟，让其返回。*/
					return dt.plusMinutes(minutes);
				}
				else
				{
					// 求出到下班时间的分钟数。
					TimeSpan myts = DataType.ParseSysDateTime2DateTime(dt.toString("yyyy-MM-dd") + " " + Glo.getPMTo()) - dt;

					// 扣除午休的时间.
					int leftMuit = (int)(myts.TotalMinutes - Glo.getAMPMTimeSpan() * 60);
					if (leftMuit - minutes >= 0)
					{
						/*说明还是在当天的时间内.*/
						LocalDateTime mydt = DataType.ParseSysDateTime2DateTime(dt.toString("yyyy-MM-dd") + " " + Glo.getPMTo());
						return mydt.plusMinutes(minutes - leftMuit);
					}

					//说明要跨到第2天上去了.
					dt = DataType.AddDays(dt, 1, TWay.Holiday);
					return Glo.AddMinutes(dt.toString("yyyy-MM-dd") + " " + Glo.getAMFrom(), minutes - leftMuit);
				}

				// 把当前的时间加上去.
				dt = dt.plusMinutes(minutes);

				//判断是否是中午.
				boolean isInAM = false;
				timeInt = Integer.parseInt(dt.toString("HHmm"));
				if (Glo.getAMToInt() >= timeInt)
				{
					isInAM = true;
				}

				if (isInAM == true)
				{
					// 加上时间后仍然是中午就返回.
					return dt;
				}

				//延迟一个午休时间.
				dt = dt.plusHours(Glo.getAMPMTimeSpan());

				//判断时间点是否落入了E区间.
				timeInt = Integer.parseInt(dt.toString("HHmm"));
				if (Glo.getPMToInt() <= timeInt)
				{
					/*如果落入了E区间.*/

					// 求出来时间点到，下班之间的分钟数.
					TimeSpan tsE = dt - DataType.ParseSysDate2DateTime(dt.toString("yyyy-MM-dd") + " " + Glo.getPMTo());

					//从次日的上班时间计算+ 这个时间差. 
					dt = DataType.ParseSysDate2DateTime(dt.toString("yyyy-MM-dd") + " " + Glo.getPMTo());
					return dt.plusMinutes(tsE.TotalMinutes);
				}
				else
				{
					/*过了第2天的情况很少，就不考虑了.*/
					return dt;
				}
			}
			else
			{
				/*如果是下午, 计算出来到下午下班还需多少分钟，与增加的分钟数据相比较. */
				TimeSpan ts = DataType.ParseSysDateTime2DateTime(dt.toString("yyyy-MM-dd") + " " + Glo.getPMTo()) - dt;
				if (ts.TotalMinutes >= minutes)
				{
					/*如果剩余的分钟大于 要增加的分钟数，就直接增加上这个分钟，让其返回。*/
					return dt.plusMinutes(minutes);
				}
				else
				{

					//剩余的分钟数 = 总分钟数 - 今天下午剩余的分钟数.
					int leftMin = minutes - (int)ts.TotalMinutes;

					/*否则要计算到第2天上去了， 计算时间要从下一个有效的工作日上班时间开始. */
					dt = DataType.AddDays(DataType.ParseSysDateTime2DateTime(dt.toString("yyyy-MM-dd") + " " + Glo.getAMFrom()), 1, TWay.Holiday);

					//递归调用,让其在次日的上班时间在增加，分钟数。
					return Glo.AddMinutes(dt, 0, leftMin);
				}

			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 如果是当天的情况.

		return dt;
	}
	/** 
	 增加分钟数.
	 
	 @param sysdt
	 @param minutes
	 @return 
	*/
	public static LocalDateTime AddMinutes(String sysdt, int minutes)
	{
		LocalDateTime dt = DataType.ParseSysDate2DateTime(sysdt);
		return AddMinutes(dt, 0, minutes);
	}
	/** 
	 在指定的日期上增加n天n小时，并考虑节假日
	 
	 @param sysdt 指定的日期
	 @param day 天数
	 @param minutes 分钟数
	 @return 返回计算后的日期
	*/
	public static LocalDateTime AddDayHoursSpan(String specDT, int day, int hh, int minutes, TWay tway)
	{
		LocalDateTime mydt = BP.DA.DataType.AddDays(specDT, day, tway);
		return Glo.AddMinutes(mydt, hh, minutes);
	}
	/** 
	 在指定的日期上增加n天n小时，并考虑节假日
	 
	 @param sysdt 指定的日期
	 @param day 天数
	 @param minutes 分钟数
	 @return 返回计算后的日期
	*/
	public static LocalDateTime AddDayHoursSpan(LocalDateTime specDT, int day, int hh, int minutes, TWay tway)
	{
		LocalDateTime mydt = BP.DA.DataType.AddDays(specDT, day, tway);
		mydt = mydt.plusHours(hh); //加小时.
		mydt = mydt.plusMinutes(minutes); //加分钟.
		return mydt;
		//return Glo.AddMinutes(mydt, minutes);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion ssxxx.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 与考核相关.
	/** 
	 当流程发送下去以后，就开始执行考核。
	 
	 @param fl
	 @param nd
	 @param workid
	 @param fid
	 @param title
	*/

	public static void InitCH(Flow fl, Node nd, long workid, long fid, String title)
	{
		InitCH(fl, nd, workid, fid, title, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static void InitCH(Flow fl, Node nd, Int64 workid, Int64 fid, string title, GenerWorkerList gwl = null)
	public static void InitCH(Flow fl, Node nd, long workid, long fid, String title, GenerWorkerList gwl)
	{
		InitCH2017(fl, nd, workid, fid, title, null, null, LocalDateTime.now(), gwl);
	}
	/** 
	 执行考核
	 
	 @param fl 流程
	 @param nd 节点
	 @param workid 工作ID
	 @param fid FID
	 @param title 标题
	 @param prvRDT 上一个时间点
	 @param sdt 应完成日期
	 @param dtNow 当前日期
	*/
	private static void InitCH2017(Flow fl, Node nd, long workid, long fid, String title, String prvRDT, String sdt, LocalDateTime dtNow, GenerWorkerList gwl)
	{
		// 开始节点不考核.
		if (nd.getIsStartNode() || nd.getHisCHWay() == CHWay.None)
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
			dtNow = LocalDateTime.now();
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 求参与人员 todoEmps ，应完成日期 sdt ，与工作派发日期 prvRDT.
		//参与人员.
		String todoEmps = "";
		String dbstr = SystemConfig.getAppCenterDBVarStr();
		if (nd.getIsEndNode() == true && gwl == null)
		{
			/* 如果是最后一个节点，可以使用这样的方式来求人员信息 , */

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 求应完成日期，与参与的人集合.
			Paras ps = new Paras();
			switch (SystemConfig.getAppCenterDBType())
			{
				case DBType.MSSQL:
					ps.SQL = "SELECT TOP 1 SDTOfNode, TodoEmps FROM WF_GenerWorkFlow  WHERE WorkID=" + dbstr + "WorkID ";
					break;
				case DBType.Oracle:
					ps.SQL = "SELECT SDTOfNode, TodoEmps FROM WF_GenerWorkFlow  WHERE WorkID=" + dbstr + "WorkID  ";
					break;
				case DBType.MySQL:
					ps.SQL = "SELECT SDTOfNode, TodoEmps FROM WF_GenerWorkFlow  WHERE WorkID=" + dbstr + "WorkID  ";
					break;
				case DBType.PostgreSQL:
					ps.SQL = "SELECT SDTOfNode, TodoEmps FROM WF_GenerWorkFlow  WHERE WorkID=" + dbstr + "WorkID  ";
					break;
				default:
					throw new RuntimeException("err@没有判断的数据库类型.");
			}

			ps.Add("WorkID", workid);
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(ps);
			if (dt.Rows.size() == 0)
			{
				return;
			}
			sdt = dt.Rows[0]["SDTOfNode"].toString(); //应完成日期.
			todoEmps = dt.Rows[0]["TodoEmps"].toString(); //参与人员.
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 求应完成日期，与参与的人集合.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 求上一个节点的日期.
			dt = Dev2Interface.Flow_GetPreviousNodeTrack(workid, nd.getNodeID());
			if (dt.Rows.size() == 0)
			{
				return;
			}
			//上一个节点的活动日期.
			prvRDT = dt.Rows[0]["RDT"].toString();
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion
		}


		if (nd.getIsEndNode() == false)
		{
			if (gwl == null)
			{
				gwl = new GenerWorkerList();
				gwl.Retrieve(GenerWorkerListAttr.WorkID, workid, GenerWorkerListAttr.FK_Node, nd.getNodeID(), GenerWorkerListAttr.FK_Emp, WebUser.getNo());
			}

			prvRDT = gwl.getRDT(); // dt.Rows[0]["RDT"].ToString(); //上一个时间点的记录日期.
			sdt = gwl.getSDT(); //  dt.Rows[0]["SDT"].ToString(); //应完成日期.
			todoEmps = WebUser.getNo() + "," + WebUser.getName() + ";";
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 求参与人员，应完成日期，与工作派发日期.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 求 preSender上一个发送人，preSenderText 发送人姓名
		String preSender = "";
		String preSenderText = "";
		DataTable dt_Sender = Dev2Interface.Flow_GetPreviousNodeTrack(workid, nd.getNodeID());
		if (dt_Sender.Rows.size() > 0)
		{
			preSender = dt_Sender.Rows[0]["EmpFrom"].toString();
			preSenderText = dt_Sender.Rows[0]["EmpFromT"].toString();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 初始化基础数据.
		BP.WF.Data.CH ch = new CH();
		ch.setWorkID(workid);
		ch.setFID(fid);
		ch.setTitle(title);

		//记录当时设定的值.
		ch.setTimeLimit(nd.getTimeLimit());

		ch.setFK_NY(dtNow.toString("yyyy-MM"));

		ch.setDTFrom(prvRDT); //任务下达时间.
		ch.setDTTo(dtNow.toString("yyyy-MM-dd HH:mm:ss")); //时间到.

		ch.setSDT(sdt); //应该完成时间.

		ch.setFK_Flow(nd.getFK_Flow()); //流程信息.
		ch.setFK_FlowT(nd.getFlowName());

		ch.setFK_Node(nd.getNodeID()); //节点.
		ch.setFK_NodeT(nd.getName());

		ch.setFK_Dept(WebUser.getFK_Dept()); //部门.
		ch.setFK_DeptT(WebUser.getFK_DeptName);

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
		ch.setMyPK( nd.getNodeID() + "_" + workid + "_" + fid + "_" + WebUser.getNo();
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 初始化基础数据.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 求计算属性.
		//求出是第几个周.
		System.Globalization.CultureInfo myCI = new System.Globalization.CultureInfo("zh-CN");
		ch.setWeekNum(myCI.Calendar.GetWeekOfYear(dtNow, System.Globalization.CalendarWeekRule.FirstDay, DayOfWeek.MONDAY));

		// UseDays . 求出实际使用天数.
		LocalDateTime dtFrom = DataType.ParseSysDate2DateTime(ch.getDTFrom());
		LocalDateTime dtTo = DataType.ParseSysDate2DateTime(ch.getDTTo());

		TimeSpan ts = dtTo - dtFrom;
		ch.setUseDays(ts.Days); //用时，天数
		ch.setUseMinutes(ts.Minutes); //用时，分钟
		//int hour = ts.Hours;
		//ch.UseDays += ts.Hours / 8; //使用的天数.

		// OverDays . 求出 逾期天 数.
		LocalDateTime sdtOfDT = DataType.ParseSysDate2DateTime(ch.getSDT());

		TimeSpan myts = dtTo - sdtOfDT;
		ch.setOverDays(myts.Days); //逾期的天数.
		ch.setOverMinutes(myts.Minutes); //逾期的分钟数
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
			ch.setPoints(Float.parseFloat((ch.getOverDays() * nd.getTCent()).toString("0.00")));
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 求计算属性.

		//执行保存.
		try
		{
			ch.DirectInsert();
		}
		catch (java.lang.Exception e)
		{
			if (ch.IsExits == true)
			{
				ch.Update();
			}
			else
			{
				//如果遇到退回的情况就可能涉及到主键重复的问题.
				ch.setMyPK( BP.DA.DBAccess.GenerGUID();
				ch.Insert();
			}
		}
	}
	/** 
	 中午时间从
	*/
	public static String getAMFrom()
	{
		return BP.Sys.SystemConfig.GetValByKey("AMFrom", "08:30");
	}
	/** 
	 中午时间从
	*/
	public static int getAMFromInt()
	{
		return Integer.parseInt(Glo.getAMFrom().replace(":", ""));
	}
	/** 
	 一天有效的工作小时数
	 是中午工作小时+下午工作小时.
	*/
	public static float getAMPMHours()
	{
		return BP.Sys.SystemConfig.GetValByKeyFloat("AMPMHours", 8);
	}
	/** 
	 中午间隔的小时数
	*/
	public static float getAMPMTimeSpan()
	{
		return BP.Sys.SystemConfig.GetValByKeyFloat("AMPMTimeSpan", 1);
	}
	/** 
	 中午时间到
	*/
	public static String getAMTo()
	{
		return BP.Sys.SystemConfig.GetValByKey("AMTo", "11:30");
	}
	/** 
	 中午时间到
	*/
	public static int getAMToInt()
	{
		return Integer.parseInt(Glo.getAMTo().replace(":", ""));
	}
	/** 
	 下午时间从
	*/
	public static String getPMFrom()
	{
		return BP.Sys.SystemConfig.GetValByKey("PMFrom", "13:30");
	}
	/** 
	 到
	*/
	public static int getPMFromInt()
	{
		return Integer.parseInt(Glo.getPMFrom().replace(":", ""));
	}
	/** 
	 到
	*/
	public static String getPMTo()
	{
		return BP.Sys.SystemConfig.GetValByKey("PMTo", "17:30");
	}
	/** 
	 到
	*/
	public static int getPMToInt()
	{
		return Integer.parseInt(Glo.getPMTo().replace(":", ""));
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 与考核相关.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 其他方法。

	/** 
	 删除临时文件
	*/
	public static void DeleteTempFiles()
	{
		try
		{
			//删除目录.
			String temp = BP.Sys.SystemConfig.PathOfTemp;
			Directory.Delete(temp, true);

			//创建目录.
			(new File(temp)).mkdirs();

			//删除pdf 目录.
			temp = BP.Sys.SystemConfig.PathOfDataUser + "InstancePacketOfData\\";
			File info = new File(temp);
			File[] dirs = info.GetDirectories();
			for (File dir : dirs)
			{
				if (dir.getName().indexOf("ND") == 0)
				{
					dir.Delete(true);
				}
			}
		}
		catch (RuntimeException ex)
		{

		}
	}

	public static BP.Sys.FrmAttachmentDBs GenerFrmAttachmentDBs(FrmAttachment athDesc, String pkval, String FK_FrmAttachment, long workid, long fid, long pworkid)
	{
		return GenerFrmAttachmentDBs(athDesc, pkval, FK_FrmAttachment, workid, fid, pworkid, true);
	}

	public static BP.Sys.FrmAttachmentDBs GenerFrmAttachmentDBs(FrmAttachment athDesc, String pkval, String FK_FrmAttachment, long workid, long fid)
	{
		return GenerFrmAttachmentDBs(athDesc, pkval, FK_FrmAttachment, workid, fid, 0, true);
	}

	public static BP.Sys.FrmAttachmentDBs GenerFrmAttachmentDBs(FrmAttachment athDesc, String pkval, String FK_FrmAttachment, long workid)
	{
		return GenerFrmAttachmentDBs(athDesc, pkval, FK_FrmAttachment, workid, 0, 0, true);
	}

	public static BP.Sys.FrmAttachmentDBs GenerFrmAttachmentDBs(FrmAttachment athDesc, String pkval, String FK_FrmAttachment)
	{
		return GenerFrmAttachmentDBs(athDesc, pkval, FK_FrmAttachment, 0, 0, 0, true);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static BP.Sys.FrmAttachmentDBs GenerFrmAttachmentDBs(FrmAttachment athDesc, string pkval, string FK_FrmAttachment, Int64 workid = 0, Int64 fid = 0, Int64 pworkid = 0, bool isContantSelf = true)
	public static BP.Sys.FrmAttachmentDBs GenerFrmAttachmentDBs(FrmAttachment athDesc, String pkval, String FK_FrmAttachment, long workid, long fid, long pworkid, boolean isContantSelf)
	{

		BP.Sys.FrmAttachmentDBs dbs = new BP.Sys.FrmAttachmentDBs();
		if (athDesc.HisCtrlWay == AthCtrlWay.PWorkID)
		{
			String pWorkID = BP.DA.DBAccess.RunSQLReturnValInt("SELECT PWorkID FROM WF_GenerWorkFlow WHERE WorkID=" + pkval, 0).toString();
			if (pWorkID == null || pWorkID.equals("0"))
			{
				pWorkID = pkval;
			}

			if (athDesc.AthUploadWay == AthUploadWay.Inherit)
			{
				/* 继承模式 */
				BP.En.QueryObject qo = new BP.En.QueryObject(dbs);

				if (pWorkID.equals(pkval) == true)
				{
					qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, pkval);
				}
				else
				{
					qo.AddWhereIn(FrmAttachmentDBAttr.RefPKVal, '(' + pWorkID + ',' + pkval + ')');
					//qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, "=", pWorkID, "RefPKVal1");
					//qo.addOr();
					//qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, "=", pkval, "");
				}
				qo.addOrderBy("RDT");
				qo.DoQuery();
			}

			if (athDesc.AthUploadWay == AthUploadWay.Interwork)
			{
				/*共享模式*/
				dbs.Retrieve(FrmAttachmentDBAttr.RefPKVal, pWorkID);
			}
			return dbs;
		}

		if (athDesc.HisCtrlWay == AthCtrlWay.WorkID)
		{
			/* 继承模式 */
			BP.En.QueryObject qo = new BP.En.QueryObject(dbs);
			qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, pkval);
			qo.addAnd();
			qo.AddWhere(FrmAttachmentDBAttr.NoOfObj, athDesc.NoOfObj);
			if (isContantSelf == false)
			{
				qo.addAnd();
				qo.AddWhere(FrmAttachmentDBAttr.Rec, "!=", WebUser.getNo());
			}
			qo.addOrderBy("RDT");
			qo.DoQuery();
			return dbs;
		}

		if (athDesc.HisCtrlWay == AthCtrlWay.FID)
		{
			/* 继承模式 */
			BP.En.QueryObject qo = new BP.En.QueryObject(dbs);
			qo.AddWhere(FrmAttachmentDBAttr.FK_FrmAttachment, athDesc.MyPK);
			qo.addAnd();
			qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, Integer.parseInt(pkval));
			if (isContantSelf == false)
			{
				qo.addAnd();
				qo.AddWhere(FrmAttachmentDBAttr.Rec, "!=", WebUser.getNo());
			}
			qo.addOrderBy("RDT");
			qo.DoQuery();
			return dbs;
		}


		if (athDesc.HisCtrlWay == AthCtrlWay.MySelfOnly || athDesc.HisCtrlWay == AthCtrlWay.PK)
		{
			if (FK_FrmAttachment.contains("AthMDtl"))
			{
				/*如果是一个明细表的多附件，就直接按照传递过来的PK来查询.*/
				BP.En.QueryObject qo = new BP.En.QueryObject(dbs);
				qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, pkval);
				qo.addAnd();
				qo.AddWhere(FrmAttachmentDBAttr.FK_FrmAttachment, FK_FrmAttachment);

				qo.DoQuery();
			}
			else
			{
				BP.En.QueryObject qo = new BP.En.QueryObject(dbs);
				qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, pkval);
				qo.addAnd();
				qo.AddWhere(FrmAttachmentDBAttr.FK_FrmAttachment, FK_FrmAttachment);
				if (isContantSelf == false)
				{
					qo.addAnd();
					qo.AddWhere(FrmAttachmentDBAttr.Rec, "!=", WebUser.getNo());
				}
				qo.addOrderBy("RDT");
				qo.DoQuery();

				//dbs.Retrieve(FrmAttachmentDBAttr.FK_FrmAttachment, FK_FrmAttachment,
				//   FrmAttachmentDBAttr.RefPKVal, pkval, "RDT");
			}
			return dbs;
		}

		throw new RuntimeException("@没有判断的权限控制模式:" + athDesc.HisCtrlWay);

		return dbs;
	}
	/** 
	 获得一个表单的动态附件字段
	 
	 @param exts 扩展
	 @param nd 节点
	 @param en 实体
	 @param md map
	 @param attrs 属性集合
	 @return 附件的主键
	*/
	public static String GenerActiveAths(MapExts exts, Node nd, Entity en, MapData md, MapAttrs attrs)
	{
		String strs = "";
		for (MapExt me : exts)
		{
			if (me.ExtType != MapExtXmlList.SepcAthSepcUsers)
			{
				continue;
			}

			boolean isCando = false;
			if (!me.Tag1.equals(""))
			{
				String tag1 = me.Tag1 + ",";
				if (tag1.contains(WebUser.getNo() + ","))
				{
					//根据设置的人员计算.
					isCando = true;
				}
			}

			if (!me.Tag2.equals(""))
			{
				//根据sql判断.
				Object tempVar = me.Tag2.Clone();
				String sql = tempVar instanceof String ? (String)tempVar : null;
				sql = BP.WF.Glo.DealExp(sql, en, null);
				if (BP.DA.DBAccess.RunSQLReturnValFloat(sql) > 0)
				{
					isCando = true;
				}
			}

			if (!me.Tag3.equals("") && WebUser.getFK_Dept() == me.Tag3)
			{
				//根据部门编号判断.
				isCando = true;
			}

			if (isCando == false)
			{
				continue;
			}
			strs += me.Doc;
		}
		return strs;
	}
	/** 
	 获得一个表单的动态权限字段
	 
	 @param exts
	 @param nd
	 @param en
	 @param md
	 @param attrs
	 @return 
	*/
	public static String GenerActiveFiels(MapExts exts, Node nd, Entity en, MapData md, MapAttrs attrs)
	{
		String strs = "";
		for (MapExt me : exts)
		{
			if (me.ExtType != MapExtXmlList.SepcFiledsSepcUsers)
			{
				continue;
			}

			boolean isCando = false;
			if (!me.Tag1.equals(""))
			{
				String tag1 = me.Tag1 + ",";
				if (tag1.contains(WebUser.getNo() + ","))
				{
					//根据设置的人员计算.
					isCando = true;
				}
			}

			if (!me.Tag2.equals(""))
			{
				//根据sql判断.
				Object tempVar = me.Tag2.Clone();
				String sql = tempVar instanceof String ? (String)tempVar : null;
				sql = BP.WF.Glo.DealExp(sql, en, null);
				if (BP.DA.DBAccess.RunSQLReturnValFloat(sql) > 0)
				{
					isCando = true;
				}
			}

			if (!me.Tag3.equals("") && WebUser.getFK_Dept() == me.Tag3)
			{
				//根据部门编号判断.
				isCando = true;
			}

			if (isCando == false)
			{
				continue;
			}
			strs += me.Doc;
		}
		return strs;
	}
	/** 
	 转到消息显示界面.
	 
	 @param info
	*/
	public static void ToMsg(String info)
	{
		//string rowUrl = BP.Sys.Glo.Request.RawUrl;
		//if (rowUrl.Contains("&IsClient=1"))
		//{
		//    /*说明这是vsto调用的.*/
		//    return;
		//}

		HttpContextHelper.SessionSet("info", info);
		HttpContextHelper.Response.Redirect(Glo.getCCFlowAppPath() + "WF/MyFlowInfo.aspx?Msg=" + DataType.getCurrentDataTime()ss, false);
	}
	public static void ToMsgErr(String info)
	{
		info = "<font color=red>" + info + "</font>";
		HttpContextHelper.SessionSet("info", info);
		HttpContextHelper.Response.Redirect(Glo.getCCFlowAppPath() + "WF/MyFlowInfo.aspx?Msg=" + DataType.getCurrentDataTime()ss, false);
	}
	/** 
	 检查流程发起限制
	 
	 @param flow 流程
	 @param wk 开始节点工作
	 @return 
	*/
	public static boolean CheckIsCanStartFlow_InitStartFlow(Flow flow)
	{
		StartLimitRole role = flow.getStartLimitRole();
		if (role == StartLimitRole.None)
		{
			return true;
		}

		String sql = "";
		String ptable = flow.getPTable();

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 按照时间的必须是，在表单加载后判断, 不管用户设置是否正确.
		LocalDateTime dtNow = LocalDateTime.now();
		if (role == StartLimitRole.Day)
		{
			/* 仅允许一天发起一次 */
			sql = "SELECT COUNT(*) as Num FROM " + ptable + " WHERE RDT LIKE '" + DataType.CurrentData + "%' AND WFState NOT IN(0,1) AND FlowStarter='" + WebUser.getNo() + "'";
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
					String tFrom = LocalDateTime.now().toString("yyyy-MM-dd") + " " + timeStrs[0].trim();
					String tTo = LocalDateTime.now().toString("yyyy-MM-dd") + " " + timeStrs[1].trim();
					if (DataType.ParseSysDateTime2DateTime(tFrom) <= dtNow && dtNow.compareTo(DataType.ParseSysDateTime2DateTime(tTo)) >= 0)
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

					String weekStr = LocalDateTime.now().getDayOfWeek().toString().toLowerCase();
					if (str.toLowerCase().contains(weekStr) == false)
					{
						continue; // 判断是否当前的周.
					}

					String[] timeStrs = str.split("[,]", -1);
					String tFrom = LocalDateTime.now().toString("yyyy-MM-dd") + " " + timeStrs[0].trim();
					String tTo = LocalDateTime.now().toString("yyyy-MM-dd") + " " + timeStrs[1].trim();
					if (DataType.ParseSysDateTime2DateTime(tFrom) <= dtNow && dtNow.compareTo(DataType.ParseSysDateTime2DateTime(tTo)) >= 0)
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
			sql = "SELECT COUNT(*) as Num FROM " + ptable + " WHERE FK_NY = '" + DataType.CurrentYearMonth + "' AND WFState NOT IN(0,1) AND FlowStarter='" + WebUser.getNo() + "'";
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
					String tFrom = LocalDateTime.now().toString("yyyy-MM-") + " " + timeStrs[0].trim();
					String tTo = LocalDateTime.now().toString("yyyy-MM-") + " " + timeStrs[1].trim();
					if (DataType.ParseSysDateTime2DateTime(tFrom) <= dtNow && dtNow.compareTo(DataType.ParseSysDateTime2DateTime(tTo)) >= 0)
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
			sql = "SELECT COUNT(*) as Num FROM " + ptable + " WHERE FK_NY = '" + DataType.CurrentAPOfJD + "' AND WFState NOT IN(0,1) AND FlowStarter='" + WebUser.getNo() + "'";
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
					String tFrom = LocalDateTime.now().toString("yyyy-") + " " + timeStrs[0].trim();
					String tTo = LocalDateTime.now().toString("yyyy-") + " " + timeStrs[1].trim();
					if (DataType.ParseSysDateTime2DateTime(tFrom) <= dtNow && dtNow.compareTo(DataType.ParseSysDateTime2DateTime(tTo)) >= 0)
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
			sql = "SELECT COUNT(*) as Num FROM " + ptable + " WHERE FK_NY LIKE '" + DataType.CurrentYear + "%' AND WFState NOT IN(0,1) AND FlowStarter='" + WebUser.getNo() + "'";
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
					String tFrom = LocalDateTime.now().toString("yyyy-") + " " + timeStrs[0].trim();
					String tTo = LocalDateTime.now().toString("yyyy-") + " " + timeStrs[1].trim();
					if (DataType.ParseSysDateTime2DateTime(tFrom) <= dtNow && dtNow.compareTo(DataType.ParseSysDateTime2DateTime(tTo)) >= 0)
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 按照时间的必须是，在表单加载后判断, 不管用户设置是否正确.


		//为子流程的时候，该子流程只能被调用一次.
		if (role == StartLimitRole.OnlyOneSubFlow)
		{

			if (BP.Sys.SystemConfig.IsBSsystem == true)
			{

				String pflowNo = HttpContextHelper.RequestParams("PFlowNo");
				String pworkid = HttpContextHelper.RequestParams("PWorkID");

				if (pworkid == null)
				{
					return true;
				}

				sql = "SELECT Starter, RDT FROM WF_GenerWorkFlow WHERE PWorkID=" + pworkid + " AND FK_Flow='" + flow.No + "'";
				DataTable dt = DBAccess.RunSQLReturnTable(sql);
				if (dt.Rows.size() == 0 || dt.Rows.size() == 1)
				{
					return true;
				}

				//  string title = dt.Rows[0]["Title"].ToString();
				String starter = dt.Rows[0]["Starter"].toString();
				String rdt = dt.Rows[0]["RDT"].toString();
				return false;
				//throw new Exception(flow.StartLimitAlert + "@该子流程已经被[" + starter + "], 在[" + rdt + "]发起，系统只允许发起一次。");
			}
		}
		return true;
	}

	/** 
	 当要发送是检查流程是否可以允许发起.
	 
	 @param flow 流程
	 @param wk 开始节点工作
	 @return 
	*/
	public static boolean CheckIsCanStartFlow_SendStartFlow(Flow flow, Work wk)
	{
		StartLimitRole role = flow.getStartLimitRole();
		if (role == StartLimitRole.None)
		{
			return true;
		}

		String sql = "";
		String ptable = flow.getPTable();

		if (role == StartLimitRole.ColNotExit)
		{
			/* 指定的列名集合不存在，才可以发起流程。*/

			//求出原来的值.
			String[] strs = flow.getStartLimitPara().split("[,]", -1);
			String val = "";
			for (String str : strs)
			{
				if (DataType.IsNullOrEmpty(str) == true)
				{
					continue;
				}
				try
				{
					val += wk.GetValStringByKey(str);
				}
				catch (RuntimeException ex)
				{
					throw new RuntimeException("@流程设计错误,您配置的检查参数(" + flow.getStartLimitPara() + "),中的列(" + str + ")已经不存在表单里.");
				}
			}

			//找出已经发起的全部流程.
			sql = "SELECT " + flow.getStartLimitPara() + " FROM " + ptable + " WHERE  WFState NOT IN(0,1) AND FlowStarter='" + WebUser.getNo() + "'";
			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			for (DataRow dr : dt.Rows)
			{
				String v = dr.get(0) + dr.get(1) + dr.get(2);
				if (val.equals(v))
				{
					return false;
				}
			}
			return true;
		}

		// 配置的sql,执行后,返回结果是 0 .
		if (role == StartLimitRole.ResultIsZero)
		{
			sql = BP.WF.Glo.DealExp(flow.getStartLimitPara(), wk, null);
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
			sql = BP.WF.Glo.DealExp(flow.getStartLimitPara(), wk, null);
			if (DBAccess.RunSQLReturnValInt(sql, 0) != 0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}

		//为子流程的时候，该子流程只能被调用一次.
		if (role == StartLimitRole.OnlyOneSubFlow)
		{
			sql = "SELECT PWorkID FROM WF_GenerWorkFlow WHERE WorkID=" + wk.getOID();
			String pWorkidStr = DBAccess.RunSQLReturnStringIsNull(sql, "0");
			if (pWorkidStr.equals("0"))
			{
				return true;
			}

			sql = "SELECT Starter, RDT FROM WF_GenerWorkFlow WHERE PWorkID=" + pWorkidStr + " AND FK_Flow='" + flow.No + "'";
			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			if (dt.Rows.size() == 0 || dt.Rows.size() == 1)
			{
				return true;
			}

			//  string title = dt.Rows[0]["Title"].ToString();
			String starter = dt.Rows[0]["Starter"].toString();
			String rdt = dt.Rows[0]["RDT"].toString();

			throw new RuntimeException(flow.getStartLimitAlert() + "@该子流程已经被[" + starter + "], 在[" + rdt + "]发起，系统只允许发起一次。");
		}

		return true;
	}

	/** 
	 复制表单权限-从一个节点到另一个节点.
	 
	 @param fk_flow 流程编号
	 @param frmID 表单ID
	 @param currNodeID 当前节点
	 @param fromNodeID 从节点
	*/
	public static void CopyFrmSlnFromNodeToNode(String fk_flow, String frmID, int currNodeID, int fromNodeID)
	{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 处理字段.
		//删除现有的.
		FrmFields frms = new FrmFields();
		frms.Delete(FrmFieldAttr.FK_Node, currNodeID, FrmFieldAttr.FK_MapData, frmID);

		//查询出来,指定的权限方案.
		frms.Retrieve(FrmFieldAttr.FK_Node, fromNodeID, FrmFieldAttr.FK_MapData, frmID);

		//开始复制.
		for (FrmField item : frms)
		{
			item.setMyPK( frmID + "_" + fk_flow + "_" + currNodeID + "_" + item.getKeyOfEn();
			item.setFK_Node(currNodeID);
			item.Insert(); // 插入数据库.
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 处理字段.

		//没有考虑到附件的权限 20161020 hzm
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 附件权限

		FrmAttachments fas = new FrmAttachments();
		//删除现有节点的附件权限
		fas.Delete(FrmAttachmentAttr.FK_Node, currNodeID, FrmAttachmentAttr.FK_MapData, frmID);
		//查询出 现在表单上是否有附件的情况
		fas.Retrieve(FrmAttachmentAttr.FK_Node, fromNodeID, FrmAttachmentAttr.FK_MapData, frmID);

		//复制权限
		for (FrmAttachment fa : fas)
		{
			fa.setMyPK( fa.FK_MapData + "_" + fa.NoOfObj + "_" + currNodeID;
			fa.FK_Node = currNodeID;
			fa.Insert();
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

	}
	/** 
	 产生消息,senderEmpNo是为了保证写入消息的唯一性，receiveid才是真正的接收者.
	 如果插入失败.
	 
	 @param fromEmpNo 发送人
	 @param now 发送时间
	 @param msg 消息内容
	 @param sendToEmpNo 接受人
	*/
	public static void SendMessageToCCIM(String fromEmpNo, String sendToEmpNo, String msg, String now)
	{
		//周朋.
		return; //暂停支持.

		if (fromEmpNo == null)
		{
			fromEmpNo = "";
		}

		if (DataType.IsNullOrEmpty(sendToEmpNo))
		{
			return;
		}

		// throw new Exception("@接受人不能为空");

		String dbStr = SystemConfig.getAppCenterDBVarStr();
		//保存系统通知消息
		StringBuilder strHql1 = new StringBuilder();
		//加密处理
		msg = BP.Tools.SecurityDES.Encrypt(msg);

		Paras ps = new Paras();
		String sql = "INSERT INTO CCIM_RecordMsg (OID,SendID,MsgDateTime,MsgContent,ImageInfo,FontName,FontSize,FontBold,FontColor,InfoClass,GroupID,SendUserID) VALUES (";
		sql += dbStr + "OID,";
		sql += "'SYSTEM',";
		sql += dbStr + "MsgDateTime,";
		sql += dbStr + "MsgContent,";
		sql += dbStr + "ImageInfo,";
		sql += dbStr + "FontName,";
		sql += dbStr + "FontSize,";
		sql += dbStr + "FontBold,";
		sql += dbStr + "FontColor,";
		sql += dbStr + "InfoClass,";
		sql += dbStr + "GroupID,";
		sql += dbStr + "SendUserID)";
		ps.SQL = sql;

		long messgeID = BP.DA.DBAccess.GenerOID("RecordMsgUser");

		ps.Add("OID", messgeID);
		ps.Add("MsgDateTime", now);
		ps.Add("MsgContent", msg);
		ps.Add("ImageInfo", "");
		ps.Add("FontName", "宋体");
		ps.Add("FontSize", 10);
		ps.Add("FontBold", 0);
		ps.Add("FontColor", -16777216);
		ps.Add("InfoClass", 15);
		ps.Add("GroupID", -1);
		ps.Add("SendUserID", fromEmpNo);
		BP.DA.DBAccess.RunSQL(ps);

		//保存消息发送对象,这个是消息的接收人表.
		ps = new Paras();
		ps.SQL = "INSERT INTO CCIM_RecordMsgUser (OID,MsgId,ReceiveID) VALUES ( ";
		ps.SQL += dbStr + "OID,";
		ps.SQL += dbStr + "MsgId,";
		ps.SQL += dbStr + "ReceiveID)";

		ps.Add("OID", messgeID);
		ps.Add("MsgId", messgeID);
		ps.Add("ReceiveID", sendToEmpNo);
		BP.DA.DBAccess.RunSQL(ps);
	}

	private static final String StrRegex = "-|;|,|/|(|)|[|]|}|{|%|@|*|!|'|`|~|#|$|^|&|.|?";
	private static final String StrKeyWord = "select|insert|delete|from|count(|drop table|update|truncate|asc(|mid(|char(|xp_cmdshell|exec master|netlocalgroup administrators|:|net user|\"|or|and";
	/** 
	 检查KeyWord是否包涵特殊字符
	 
	 @param _sWord 需要检查的字符串
	 @return 
	*/
	public static String CheckKeyWord(String KeyWord)
	{
		//特殊符号
		String[] strRegx = StrRegex.split('|');
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
	 
	 @param _sWord 需要检查的字符串
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 其他方法。
}