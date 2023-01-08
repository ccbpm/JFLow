package bp.wf;

import bp.difference.SystemConfig;
import bp.en.*;
import bp.da.*;
import bp.port.*;
import bp.web.*;
import bp.sys.*;
import bp.wf.template.*;
import bp.tools.*;
import bp.wf.template.sflow.*;
import net.sf.json.JSONObject;

import java.util.*;

/** 
 WorkNode的附加类: 2020年06月09号
 1， 因为worknode的类方法太多，为了能够更好的减轻代码逻辑.
 2.  一部分方法要移动到这里来. 
*/
public class WorkNodePlus
{
	/** 
	 处理数据源.
	 
	 param fl
	 param nd
	 param track
	*/
	public static void AddNodeFrmTrackDB(Flow fl, Node nd, Track track, Work wk) throws Exception {
		//流程是否启用了版本控制
		if (fl.GetValBooleanByKey("IsEnableDBVer") == false)
			return;
		Paras ps = new Paras();
		String dbstr = SystemConfig.getAppCenterDBVarStr();
		int ver = 1;
		//如果是单个表单.
		if (nd.getHisFormType() == NodeFormType.Develop || nd.getHisFormType() == NodeFormType.FoolTruck || nd.getHisFormType() == NodeFormType.ChapterFrm || nd.getHisFormType() == NodeFormType.FoolForm)
		{
			//接点表单保存NDXXRpt
			String frmID = "ND" + Integer.parseInt(fl.getNo()) + "Rpt";
			String dtlJson = AddNodeFrmDtlDB(nd.getNodeID(), track.getWorkID(), nd.getNodeFrmID());
			String athJson = AddNodeFrmAthDB(nd.getNodeID(), (int) track.getWorkID(), nd.getNodeFrmID());
			//获取版本号
			ps.SQL = "SELECT MAX(Ver) From Sys_FrmDBVer WHERE FrmID=" + dbstr + "FrmID AND RefPKVal=" + dbstr + "RefPKVal";
			ps.Add("FrmID", frmID);
			ps.Add("RefPKVal", track.getWorkID());
			ver = DBAccess.RunSQLReturnValInt(ps,0);
			ver = ver == 0 ? 1:ver + 1;
			FrmDBVer.AddFrmDBTrack(ver,frmID, String.valueOf(track.getWorkID()), track.getMyPK(), wk.ToJson(true), dtlJson,athJson,false);
			return;
		}

		if (nd.getHisFormType() == NodeFormType.RefOneFrmTree)
		{
			FrmNode fn = new FrmNode(nd.getNodeID(),nd.getNodeFrmID());
			if (fn.getFrmSln() == FrmSln.Readonly)
				return;
			MapData md = nd.getMapData();
			//获取版本号
			ps.SQL = "SELECT MAX(Ver) From Sys_FrmDBVer WHERE FrmID=" + dbstr + "FrmID AND RefPKVal=" + dbstr + "RefPKVal";
			ps.Add("FrmID", md.getNo());
			ps.Add("RefPKVal", track.getWorkID());
			ver = DBAccess.RunSQLReturnValInt(ps, 0);
			ver = ver == 0 ? 1 : ver + 1;
			if (md.getHisFrmType() == FrmType.ChapterFrm)
			{
				//获取字段
				MapAttrs attrs = md.getMapAttrs();
				for(MapAttr attr:attrs.ToJavaList())
				{
					if (attr.getUIVisible() == false)
						continue;
					FrmDBVer.AddKeyOfEnDBTrack(ver,nd.getNodeFrmID(), String.valueOf(track.getWorkID()), track.getMyPK(), wk.GetValStringByKey(attr.getKeyOfEn()), attr.getKeyOfEn());
				}
				String json = AddNodeFrmDtlDB(nd.getNodeID(), track.getWorkID(), nd.getNodeFrmID());
				String aths = AddNodeFrmAthDB(nd.getNodeID(), (int) track.getWorkID(), nd.getNodeFrmID());
				FrmDBVer.AddFrmDBTrack(ver,nd.getNodeFrmID(), String.valueOf(track.getWorkID()), track.getMyPK(), null, json, aths,true);

				//获取控件类型是ChapterFrmLinkFrm的分组
				GroupFields groups =new GroupFields();
				groups.Retrieve(GroupFieldAttr.FrmID, md.getNo(), GroupFieldAttr.CtrlType, "ChapterFrmLinkFrm");
				for(GroupField group : groups.ToJavaList())
				{
					//获取表单数据
					GEEntity en = new GEEntity(group.getCtrlID(), track.getWorkID());
					json = AddNodeFrmDtlDB(nd.getNodeID(), track.getWorkID(), group.getCtrlID());
					aths = AddNodeFrmAthDB(nd.getNodeID(), (int) track.getWorkID(), group.getCtrlID());
					if (en.getRow().containsKey("RDT"))
						en.SetValByKey("RDT", "");
					FrmDBVer.AddFrmDBTrack(ver,group.getCtrlID(), String.valueOf(track.getWorkID()), track.getMyPK(), en.ToJson(), json, aths, false);
				}

				return;
			}
			String dtlJson = AddNodeFrmDtlDB(nd.getNodeID(), track.getWorkID(), nd.getNodeFrmID());
			String athJson = AddNodeFrmAthDB(nd.getNodeID(),  (int)track.getWorkID(), nd.getNodeFrmID());
			FrmDBVer.AddFrmDBTrack(ver,nd.getNodeFrmID(), String.valueOf(track.getWorkID()), track.getMyPK(), wk.ToJson(), dtlJson, athJson,false);

			return;
		}

		//如果是多个表单.
		if (nd.getHisFormType() == NodeFormType.SheetTree)
		{
			FrmNodes fns = new FrmNodes(fl.getNo(), nd.getNodeID());

			for (FrmNode fn : fns.ToJavaList())
			{
				if (fn.getFrmSln() == FrmSln.Readonly)
				{
					continue;
				}

				//如果是禁用的.
				if (fn.getFrmEnableRole() == FrmEnableRole.Disable)
				{
					continue;
				}

				//如果是由参数启用的.
				if (fn.getFrmEnableRole() == FrmEnableRole.WhenHaveFrmPara)
				{
					continue;
				}

				GEEntity ge = new GEEntity(fn.getFKFrm());
				ge.setOID(track.getWorkID());
				if (ge.RetrieveFromDBSources() == 0)
				{
					continue;
				}
				//获取版本号
				ps = new Paras();
				ps.SQL = "SELECT MAX(Ver) From Sys_FrmDBVer WHERE FrmID=" + dbstr + "FrmID AND RefPKVal=" + dbstr + "RefPKVal";
				ps.Add("FrmID", fn.getFKFrm());
				ps.Add("RefPKVal", track.getWorkID());
				ver = DBAccess.RunSQLReturnValInt(ps, 0);
				ver = ver == 0 ? 1 : ver + 1;
				String dtlJson = AddNodeFrmDtlDB(nd.getNodeID(), track.getWorkID(), fn.getFKFrm());
				String athJson = AddNodeFrmAthDB(nd.getNodeID(), (int)track.getWorkID(), fn.getFKFrm());
				FrmDBVer.AddFrmDBTrack(ver,fn.getFKFrm(), String.valueOf(track.getWorkID()), track.getMyPK(), ge.ToJson(), dtlJson, athJson,false);

			}
			return;
		}

		//if (fl.HisNodes)
		// FrmDBVer.AddFrmDBTrack()

	}
	/** 
	 获取表单从表的数据集合
	 
	 param nodeId
	 param workID
	 param frmID
	 @return 
	*/
	public static String 	AddNodeFrmDtlDB(int nodeId, long workID, String frmID) throws Exception {
		MapData mapData = new MapData(frmID);
		GenerWorkFlow gwf = new GenerWorkFlow(workID);
		MapDtls dtls = mapData.getMapDtls();
		DataSet ds = new DataSet();
		DataTable dt = null;
		for (MapDtl dtl : dtls.ToJavaList())
		{
			String dtlRefPKVal = bp.wf.Dev2Interface.GetDtlRefPKVal(gwf.getWorkID(), gwf.getPWorkID(), gwf.getFID(), nodeId, dtl.getFK_MapData(), dtl);
			dt = CCFormAPI.GetDtlInfo(dtl,null, dtlRefPKVal);
			dt.TableName = dtl.getPTable();
			ds.Tables.add(dt);
		}
		return Json.ToJson(ds);
	}
	/// <summary>
	/// 获取表单附件的数据集合
	/// </summary>
	/// <param name="nodeId"></param>
	/// <param name="workID"></param>
	/// <param name="frmID"></param>
	/// <returns></returns>
	public static String AddNodeFrmAthDB(int nodeId, int workID, String frmID) throws Exception {
		MapData mapData = new MapData(frmID);
		GenerWorkFlow gwf = new GenerWorkFlow(workID);
		FrmAttachments aths = new FrmAttachments();
		aths.Retrieve("FK_MapData", frmID, "FK_Node", 0);
		DataSet ds = new DataSet();
		FrmAttachmentDBs dbs = null;
		for (FrmAttachment ath : aths.ToJavaList())
		{
			dbs = bp.wf.Glo.GenerFrmAttachmentDBs(ath, String.valueOf(gwf.getWorkID()),
					ath.getMyPK(), gwf.getWorkID(), gwf.getFID(), gwf.getPWorkID(), true, nodeId, frmID);

			ds.Tables.add(dbs.ToDataTableField(ath.getNoOfObj()));
		}
		return bp.tools.Json.ToJson(ds);
	}
	/** 
	 发送草稿实例 2020.10.27 fro 铁路局.
	*/
	public static void SendDraftSubFlow(WorkNode wn) throws Exception {
		//如果不允许发送草稿子流程.
		if (wn.getHisNode().isSendDraftSubFlow() == false)
		{
			return;
		}

		//查询出来该流程实例下的所有草稿子流程.
		GenerWorkFlows gwfs = new GenerWorkFlows();
		gwfs.Retrieve(GenerWorkFlowAttr.PWorkID, wn.getWorkID(), GenerWorkFlowAttr.WFState, 1, null);

		//子流程配置信息.
		SubFlowHandGuide sf = null;

		//开始发送子流程.
		for (GenerWorkFlow gwfSubFlow : gwfs.ToJavaList())
		{
			//获得配置信息.
			if (sf == null || !sf.getFK_Flow().equals(gwfSubFlow.getFK_Flow()))
			{
				String pkval = wn.getHisGenerWorkFlow().getFK_Flow() + "_" + gwfSubFlow.getFK_Flow() + "_0";
				sf = new SubFlowHandGuide();
				sf.setMyPK(pkval);
				sf.RetrieveFromDBSources();
			}

			//把草稿移交给当前人员. - 更新控制表.
			gwfSubFlow.setStarter(WebUser.getNo());
			gwfSubFlow.setStarterName(WebUser.getName());
			gwfSubFlow.Update();
			//把草稿移交给当前人员. - 更新工作人员列表.
			DBAccess.RunSQL("UPDATE WF_GenerWorkerList SET FK_Emp='" + WebUser.getNo() + "',FK_EmpText='" + WebUser.getName() + "' WHERE WorkID=" + gwfSubFlow.getWorkID());
			//更新track表.
			//DBAccess.RunSQL("UPDATE ND"+int.Parse(gwfSubFlow.FK_Flow) +"Track SET FK_Emp='" + WebUser.getNo() + "',FK_EmpText='" + WebUser.getName() + "' WHERE WorkID=" + gwfSubFlow.WorkID);

			//启动子流程. 并把两个字段，写入子流程.
			bp.wf.Dev2Interface.Node_SendWork(gwfSubFlow.getFK_Flow(), gwfSubFlow.getWorkID(), null, null);
		}
	}
	/** 
	 当要发送是检查流程是否可以允许发起.
	 
	 param flow 流程
	 param wk 开始节点工作
	 @return 
	*/
	public static boolean CheckIsCanStartFlow_SendStartFlow(Flow flow, Work wk) throws Exception {
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
			if (DataType.IsNullOrEmpty(flow.getStartLimitPara()) == true)
			{
				throw new RuntimeException("err@流程发起限制规则出现错误,StartLimitRole.ColNotExit , 没有配置参数. ");
			}

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
				String v = dr.getValue(0) + "" + dr.getValue(1) + "" + dr.getValue(2);
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
			sql = bp.wf.Glo.DealExp(flow.getStartLimitPara(), wk, null);
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
			sql = bp.wf.Glo.DealExp(flow.getStartLimitPara(), wk, null);
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

			sql = "SELECT Starter, RDT FROM WF_GenerWorkFlow WHERE PWorkID=" + pWorkidStr + " AND FK_Flow='" + flow.getNo() + "'";
			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			if (dt.Rows.size() == 0 || dt.Rows.size() == 1)
			{
				return true;
			}

			//  string title = dt.Rows.get(0).getValue("Title"].ToString();
			String starter = dt.Rows.get(0).getValue("Starter").toString();
			String rdt = dt.Rows.get(0).getValue("RDT").toString();

			throw new RuntimeException(flow.getStartLimitAlert() + "@该子流程已经被[" + starter + "], 在[" + rdt + "]发起，系统只允许发起一次。");
		}

		return true;
	}
	/** 
	 开始执行数据同步,在流程运动的过程中，
	 数据需要同步到不同的表里去.
	 
	 param fl 流程
	 param gwf 实体
	 param rpt 实体
	*/
	public static void DTSData(Flow fl, GenerWorkFlow gwf, bp.wf.data.GERpt rpt, Node currNode, boolean isStopFlow) throws Exception {
		//判断同步类型.
		if (fl.getDTSWay() == DataDTSWay.None)
		{
			return;
		}

		boolean isActiveSave = false;
		// 判断是否符合流程数据同步条件.
		switch (fl.getDTSTime())
		{
			case AllNodeSend: //所有节点发送后
				isActiveSave = true;
				break;
			case SpecNodeSend: //指定节点发送后
				if (fl.getDTSSpecNodes().contains(String.valueOf(currNode.getNodeID())) == true)
				{
					isActiveSave = true;
				}
				break;
			case WhenFlowOver: //流程结束时
				if (isStopFlow)
				{
					isActiveSave = true;
				}
				break;
			default:
				break;
		}
		if (isActiveSave == false)
		{
			return;
		}


			///#region zqp, 编写同步的业务逻辑,执行错误就抛出异常.
		if (fl.getDTSWay() == DataDTSWay.Syn)
		{
			//获取同步字段
			String[] dtsArray = fl.getDTSFields().split("[@]", -1);
			//本系统字段
			String lcAttrs = "";
			//业务系统字段
			String ywAttrs = "";

			for (int i = 0; i < dtsArray.length; i++)
			{
				//获取本系统字段
				lcAttrs += dtsArray[i].split("[=]", -1)[0] + ",";
				//获取业务系统字段
				ywAttrs += dtsArray[i].split("[=]", -1)[1] + ",";
			}

			String[] lcArr = StringHelper.trimEnd(lcAttrs, ',').split("[,]", -1); //取出对应的主表字段
			String[] ywArr = StringHelper.trimEnd(ywAttrs, ',').split("[,]", -1); //取出对应的业务表字段

			//判断本系统表是否存在
			String sql = "SELECT " + StringHelper.trimEnd(lcAttrs, ',') + " FROM " + fl.getPTable().toUpperCase() + " WHERE OID=" + rpt.getOID();
			DataTable lcDt = DBAccess.RunSQLReturnTable(sql);
			if (lcDt.Rows.size() == 0)
			{
				throw new RuntimeException("没有找到业务表数据.");
			}

			//获取配置的，要同步的业务表
			SFDBSrc src = new SFDBSrc(fl.getDTSDBSrc());
			sql = "SELECT " + StringHelper.trimEnd(ywAttrs, ',') + " FROM " + fl.getDTSBTable().toUpperCase();
			//获取业务表，是否有数据
			DataTable ywDt = src.RunSQLReturnTable(sql);

			//插入字段字符串
			String values = "";
			//更新字段字符串
			String upVal = "";

			//循环本系统表，组织同步语句
			for (int i = 0; i < lcArr.length; i++)
			{
				//系统类别
				switch (src.getDBSrcType())
				{
					case Localhost:
						switch (bp.difference.SystemConfig.getAppCenterDBType( ))
						{
							case MSSQL:
								break;
							case Oracle:
							case KingBaseR3:
							case KingBaseR6:
								//如果是时间类型，要进行转换
								if (ywDt.Columns.get(ywArr[i]).DataType == Date.class)
								{
									if (!DataType.IsNullOrEmpty(lcDt.Rows.get(0).getValue(lcArr[i].toString()).toString()) && !lcDt.Rows.get(0).getValue(lcArr[i].toString()).equals("@RDT"))
									{
										values += "to_date('" + lcDt.Rows.get(0).getValue(lcArr[i].toString()) + "','YYYY-MM-DD'),";
									}
									else
									{
										values += "'',";
									}
									continue;
								}
								values += "'" + lcDt.Rows.get(0).getValue(lcArr[i].toString()) + "',";
								upVal += upVal + ywArr[i] + "='" + lcDt.Rows.get(0).getValue(lcArr[i].toString()) + "',";
								break;
							case MySQL:
								break;
							case Informix:
								break;
							default:
								throw new RuntimeException("没有涉及到的连接测试类型...");
						}
						break;
					case SQLServer:
						break;
					case MySQL:
						break;
					case Oracle:
					case KingBaseR3:
					case KingBaseR6:
						//如果是时间类型，要进行转换
						if (ywDt.Columns.get(ywArr[i]).DataType == Date.class)
						{
							if (!DataType.IsNullOrEmpty(lcDt.Rows.get(0).getValue(lcArr[i].toString()).toString()) && !lcDt.Rows.get(0).getValue(lcArr[i].toString()).equals("@RDT"))
							{
								values += "to_date('" + lcDt.Rows.get(0).getValue(lcArr[i].toString()) + "','YYYY-MM-DD'),";
							}
							else
							{
								values += "'',";
							}
							continue;
						}
						values += "'" + lcDt.Rows.get(0).getValue(lcArr[i].toString()) + "',";
						upVal += upVal + ywArr[i] + "='" + lcDt.Rows.get(0).getValue(lcArr[i].toString()) + "',";
						continue;
					default:
						throw new RuntimeException("暂时不支您所使用的数据库类型!");
				}
				values += "'" + lcDt.Rows.get(0).getValue(lcArr[i].toString()) + "',";

			}

			values = values.substring(0, values.length() - 1);
			upVal = upVal.substring(0, upVal.length() - 1);

			//查询对应的业务表中是否存在这条记录
			sql = "SELECT * FROM " + fl.getDTSBTable().toUpperCase() + " WHERE " + fl.getDTSBTablePK() + "='" + lcDt.Rows.get(0).getValue(fl.getDTSBTablePK()) + "'";
			DataTable dt = src.RunSQLReturnTable(sql);
			//如果存在，执行更新，如果不存在，执行插入
			if (dt.Rows.size() > 0)
			{
				sql = "UPDATE " + fl.getDTSBTable().toUpperCase() + " SET " + upVal + " WHERE " + fl.getDTSBTablePK() + "='" + lcDt.Rows.get(0).getValue(fl.getDTSBTablePK()) + "'";
			}
			else
			{
				sql = "INSERT INTO " + fl.getDTSBTable().toUpperCase() + "(" + StringHelper.trimEnd(ywAttrs, ',') + ") VALUES(" + values + ")";
			}

			try
			{
				src.RunSQL(sql);
			}
			catch (RuntimeException ex)
			{
				throw new RuntimeException(ex.getMessage());
			}
		}
		if (fl.getDTSWay() == DataDTSWay.WebAPI)
		{
			//推送的数据
			String info = "{";
			//推送的主表数据
			String mainTable = "";
			mainTable += "\"mainTable\":";
			mainTable += "{";
			MapAttrs mattrs = new MapAttrs(currNode.getNodeFrmID());
			for (MapAttr attr : mattrs.ToJavaList())
			{
				if (attr.getKeyOfEn().equals("Title") || attr.getKeyOfEn().equals("BillNo"))
				{
					continue;
				}
				if (attr.getKeyOfEn().equals("AtPara") || attr.getKeyOfEn().equals("BillState"))
				{
					continue;
				}
				if (attr.getKeyOfEn().equals("RDT") || attr.getKeyOfEn().equals("OrgNo"))
				{
					continue;
				}
				if (attr.getKeyOfEn().equals("FK_Dept") || attr.getKeyOfEn().equals("FID"))
				{
					continue;
				}
				if (attr.getKeyOfEn().equals("Starter") || attr.getKeyOfEn().equals("StarterName"))
				{
					continue;
				}
				if (attr.getKeyOfEn().equals("OID") || attr.getKeyOfEn().equals("Rec"))
				{
					continue;
				}
				mainTable += "\"" + attr.getKeyOfEn() + "\":\"" + rpt.GetValStrByKey(attr.getKeyOfEn()) + "\",";
			}
			mainTable += "\"oid\":\"" + gwf.getWorkID() + "\"";
			mainTable += "}";

			//推送的从表数据
			String dtls = "[";
			String dtlData = "";

			MapDtls mapDtls = new MapDtls();
			mapDtls.Retrieve(MapDtlAttr.FK_MapData, currNode.getNodeFrmID(), null);
			for (MapDtl dtl : mapDtls.ToJavaList())
			{
				dtlData += "{";
				dtlData += "\"dtlNo\":\"" + dtl.getNo() + "\",";
				//多个从表的数据
				String dtlList = "[";
				//每一行数据
				String dtlOne = "";
				//每一行的字段数据
				String dtlKeys = "";
				//从表附件
				String dtlAths = "[";
				String dtlAth = "";

				MapAttrs dtlAttrs = new MapAttrs(dtl.getNo());
				GEDtls geDtls = new GEDtls(dtl.getNo());
				geDtls.Retrieve(GEDtlAttr.RefPK, gwf.getWorkID(), null);
				for (GEDtl geDtl : geDtls.ToJavaList())
				{
					dtlKeys = "{";
					for (MapAttr attr : dtlAttrs.ToJavaList())
					{
						if (attr.getKeyOfEn().equals("OID") || attr.getKeyOfEn().equals("RefPK"))
						{
							continue;
						}
						if (attr.getKeyOfEn().equals("FID") || attr.getKeyOfEn().equals("RDT"))
						{
							continue;
						}
						if (attr.getKeyOfEn().equals("Rec") || attr.getKeyOfEn().equals("AthNum"))
						{
							continue;
						}
						dtlKeys += "\"" + attr.getKeyOfEn() + "\":\"" + geDtl.GetValByKey(attr.getKeyOfEn()) + "\",";
					}
					if (!DataType.IsNullOrEmpty(dtlKeys))
					{
						dtlKeys = dtlKeys.substring(0, dtlKeys.length() - 1);
					}
					dtlKeys += "}";

					FrmAttachmentDBs attachmentDBs = new FrmAttachmentDBs();
					attachmentDBs.Retrieve(FrmAttachmentDBAttr.FK_MapData, dtl.getNo(), FrmAttachmentDBAttr.RefPKVal, geDtl.getOID(), null);
					for (FrmAttachmentDB frmAttachmentDB : attachmentDBs.ToJavaList())
					{
						dtlAth += "{";
						dtlAth += "\"fileFullName\":\"" + frmAttachmentDB.getFileFullName() + "\",";
						dtlAth += "\"fileName\":\"" + frmAttachmentDB.getFileName() + "\",";
						dtlAth += "\"sort\":\"" + frmAttachmentDB.getSort() + "\",";
						dtlAth += "\"fileExts\":\"" + frmAttachmentDB.getFileExts() + "\",";
						dtlAth += "\"rdt\":\"" + frmAttachmentDB.getRDT() + "\",";
						dtlAth += "\"rec\":\"" + frmAttachmentDB.getRec() + "\",";
						dtlAth += "\"myPK\":\"" + frmAttachmentDB.getMyPK() + "\",";
						dtlAth += "\"recName\":\"" + frmAttachmentDB.getRecName() + "\",";
						dtlAth += "\"fk_dept\":\"" + frmAttachmentDB.getFK_Dept() + "\",";
						dtlAth += "\"fk_deptName\":\"" + frmAttachmentDB.getFK_DeptName() + "\"";
						dtlAth += "},";
					}
					if (!DataType.IsNullOrEmpty(dtlAth))
					{
						dtlAth = dtlAth.substring(0, dtlAth.length() - 1);
					}
					dtlAth += "]";
					dtlOne += "{";
					dtlOne += "\"dtlData\":" + dtlKeys + ",";
					dtlOne += "\"dtlAths\":[" + dtlAth + "";
					dtlOne += "},";
				}
				if (!DataType.IsNullOrEmpty(dtlOne))
				{
					dtlOne = dtlOne.substring(0, dtlOne.length() - 1);
				}
				dtlList += dtlOne;
				dtlList += "]";

				dtlData += "\"dtl\":" + dtlList + "";
				dtlData += "},";
			}
			if (!DataType.IsNullOrEmpty(dtlData))
			{
				dtlData = dtlData.substring(0, dtlData.length() - 1);
			}
			dtls += dtlData;
			dtls += "]";

			//附件数据
			String aths = "[";
			String ath = "";

			FrmAttachments attachments = new FrmAttachments();
			attachments.Retrieve(FrmAttachmentAttr.FK_MapData, currNode.getNodeFrmID(), FrmAttachmentAttr.FK_Node, 0);
			for (FrmAttachment attachment : attachments.ToJavaList()) {
				FrmAttachmentDBs dbs = new FrmAttachmentDBs();
				dbs.Retrieve(FrmAttachmentDBAttr.FK_FrmAttachment, attachment.getMyPK(), FrmAttachmentDBAttr.FK_MapData, currNode.getNodeFrmID(), FrmAttachmentDBAttr.RefPKVal, gwf.getWorkID());
				if (dbs.ToJavaList().size() <= 0)
					continue;
				ath += "{";
				ath += "\"attachmentid\":\"" + attachment.getMyPK() + "\",";

				String athdb = "";
				for (FrmAttachmentDB db : dbs.ToJavaList()) {
					athdb += "{";
					athdb += "\"fileFullName\":\"" + db.getFileFullName() + "\",";
					athdb += "\"fileName\":\"" + db.getFileName() + "\",";
					athdb += "\"sort\":\"" + db.getSort() + "\",";
					athdb += "\"fileExts\":\"" + db.getFileExts() + "\",";
					athdb += "\"rdt\":\"" + db.getRDT() + "\",";
					athdb += "\"myPK\":\"" + db.getMyPK() + "\",";
					athdb += "\"refPKVal\":\"" + db.getRefPKVal() + "\",";
					athdb += "\"rec\":\"" + db.getRec() + "\",";
					athdb += "\"recName\":\"" + db.getRecName() + "\",";
					athdb += "\"fk_dept\":\"" + db.getFK_Dept() + "\",";
					athdb += "\"fk_deptName\":\"" + db.getFK_DeptName() + "\",";
					athdb += "},";
				}
				if (!DataType.IsNullOrEmpty(athdb))
				{
					athdb = athdb.substring(0, athdb.length() - 1);
				}
				ath += "\"athdb\":" + athdb + "";
				ath += "},";
			}
			if (!DataType.IsNullOrEmpty(ath))
			{
				ath = ath.substring(0, ath.length() - 1);
			}
			aths += ath;
			aths += "]";

			info += mainTable;
			info += ",\"dtls\":" + dtls;
			info += ",\"aths\":" + aths;
			info += "}";

			String apiUrl = fl.getDTSWebAPI();
			Hashtable headerMap = new Hashtable();

			//设置返回值格式
			headerMap.put("Content-Type", "application/json");
			//设置token，用于接口校验
			headerMap.put("Authorization", WebUser.getToken());
			//执行POST
			String postData = HttpClientUtil.doPost(apiUrl, info.toString(), headerMap);
			JSONObject j = JSONObject.fromObject(postData);
			if (!j.get("code").toString().equals("200"))
				bp.da.Log.DefaultLogWriteLine(LogType.Info, "同步失败:" + postData.toString());

			return;

		}

			///#endregion zqp, 编写同步的业务逻辑,执行错误就抛出异常.
		return;
	}
	/** 
	 处理协作模式下的删除规则
	 
	 param nd 节点
	 param gwf
	*/
	public static void GenerWorkerListDelRole(Node nd, GenerWorkFlow gwf) throws Exception {
		if (nd.getGenerWorkerListDelRole() == 0)
		{
			return;
		}

		//按照部门删除,同部门下的人员+兼职部门.
		if (nd.getGenerWorkerListDelRole() == 1 || nd.getGenerWorkerListDelRole() == 3 || nd.getGenerWorkerListDelRole() == 4)
		{
			//定义本部门的人员. 主部门+兼职部门.
			String sqlUnion = "";
			if (nd.getGenerWorkerListDelRole() == 1)
			{
				sqlUnion += " SELECT " + bp.sys.base.Glo.getUserNoWhitOutAS() + " as FK_Emp FROM Port_Emp WHERE FK_Dept='" + WebUser.getFK_Dept()+ "' ";
				sqlUnion += " UNION ";
				sqlUnion += " SELECT FK_Emp FROM Port_DeptEmp WHERE FK_Dept='" + WebUser.getFK_Dept()+ "'";
			}

			//主部门的人员.
			if (nd.getGenerWorkerListDelRole() == 3)
			{
				sqlUnion += " SELECT " + bp.sys.base.Glo.getUserNo() + " FROM Port_Emp WHERE FK_Dept='" + WebUser.getFK_Dept()+ "' ";
			}

			//兼职部门的人员.
			if (nd.getGenerWorkerListDelRole() == 4)
			{
				sqlUnion += " SELECT FK_Dept FROM Port_DeptEmp WHERE FK_Dept='" + WebUser.getFK_Dept()+ "'";
			}

			//获得要删除的人员.
			String sql = " SELECT FK_Emp FROM WF_GenerWorkerlist WHERE ";
			sql += " WorkID=" + gwf.getWorkID() + " AND FK_Node=" + gwf.getFK_Node() + " AND IsPass=0 ";
			sql += " AND FK_Emp IN (" + sqlUnion + ")";

			//获得要删除的数据.
			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			for (int i = 0; i < dt.Rows.size(); i++)
			{
				String empNo = dt.Rows.get(i).getValue(0).toString();
				if (empNo.equals(WebUser.getNo()) == true)
				{
					continue;
				}
				sql = "UPDATE WF_GenerWorkerlist SET IsPass=1 WHERE WorkID=" + gwf.getWorkID() + " AND FK_Node=" + gwf.getFK_Node() + " AND FK_Emp='" + empNo + "'";
				DBAccess.RunSQL(sql);
			}
		}

		//按照角色删除,同角色的人员.
		if (nd.getGenerWorkerListDelRole() == 2)
		{
			//1. 求出来: 当前人员的角色集合与节点角色集合的交集， 表示：当前人员用这些角色做了这个节点的事情.
			//获得当前节点使用的岗位, 首先从临时的变量里找（动态的获取的）,没有就到 NodeStation 里找.
			String temp = gwf.GetParaString("NodeStas" + gwf.getFK_Node(), ""); //这个变量是上一个节点通过字段选择出来的.
			String stasSQLIn = "";
			if (DataType.IsNullOrEmpty(temp) == false)
			{
				stasSQLIn = DataType.DealFromatSQLWhereIn(temp);
			}
			else
			{
				DataTable dtStas = DBAccess.RunSQLReturnTable("SELECT FK_Station FROM WF_NodeStation WHERE FK_Node=" + nd.getNodeID());
				if (dtStas.Rows.size() == 0)
					throw new Exception("err@执行按照岗位删除人员待办出现错误，没有找到节点"+nd.getNodeID()+"-"+nd.getName()+",绑定的岗位.");

				String strs = "";
				for (DataRow dr : dtStas.Rows)
				{
					strs += ",'" + dr.getValue(0).toString() + "'";
				}
				stasSQLIn = strs.substring(1);
			}
			if (DataType.IsNullOrEmpty(stasSQLIn) == true)
				throw new Exception("err@没有找到当前节点使用的岗位集合.");

			//求出来我使用的岗位集合.
			String sqlGroupMy = ""; //我使用岗位.
			sqlGroupMy = "SELECT FK_Station FROM Port_DeptEmpStation  WHERE FK_Station IN (" + stasSQLIn + ") AND FK_Emp='" + WebUser.getNo() + "'";
			DataTable dtGroupMy = DBAccess.RunSQLReturnTable(sqlGroupMy);
			String stasGroupMy = "";
			for (int i = 0; i < dtGroupMy.Rows.size(); i++)
				stasGroupMy +=",'" + dtGroupMy.Rows.get(i).getValue(0).toString()+ "'";
			stasGroupMy = stasGroupMy.substring(1);

			//2. 遍历: 当前的操作员，一个个的判断是否可以删除.
			GenerWorkerLists gwls = new GenerWorkerLists();
			gwls.Retrieve(GenerWorkerListAttr.WorkID, gwf.getWorkID(), GenerWorkerListAttr.FK_Node, nd.getNodeID());

			for (GenerWorkerList item : gwls.ToJavaList())
			{
				if (item.getIsEnable() == false)
					continue;
				if (item.getFK_Emp().equals(WebUser.getNo()) == true)
					continue; //要把自己排除在外.

				String sqlGroupUser = "SELECT FK_Station FROM Port_DeptEmpStation WHERE FK_Station IN (" + stasSQLIn + ") AND FK_Emp='" + item.getFK_Emp() + "'";
				DataTable dtGroupUser = DBAccess.RunSQLReturnTable(sqlGroupUser);

				// 判断  sqlGroupMy  >= sqlGroupUser  是否包含,如果包含，就是删除对象.
				boolean isCanDel = true;
				for (DataRow dr : dtGroupUser.Rows){
					String staNo = "'" + dr.getValue(0).toString() + "'";
					if (stasGroupMy.contains(staNo) == false)
						isCanDel = false;
				}
				//符合删除的条件.
				if (isCanDel == true)
				{
					item.setIsEnable(false) ;
					item.setIsPassInt(1);
					item.Update();
				}
			}

			//地瓜土豆问题.
			// 3 检查同岗位的人员是否有交集: 番茄的人,马铃薯的人，都分别审批了，需要删除 番茄+马铃薯岗位的人.
			// 3.1 找出来处理人中，用到人岗位集合， 就是说已经消耗掉的岗位集合.
			String sql = "SELECT B.FK_Station,A.FK_Emp FROM WF_GenerWorkerlist A, Port_DeptEmpStation B ";
			sql += " WHERE A.FK_Emp=B.FK_Emp AND B.FK_Station IN ("+ stasSQLIn + ") AND A.WorkID=" + gwf.getWorkID() + " AND A.FK_Node=" + nd.getNodeID();
			sql += " AND (A.IsPass=1 OR A.FK_Emp='" + WebUser.getNo() + "') ";
			DataTable dtStationsUsed = DBAccess.RunSQLReturnTable(sql);
			String stasUseed = "";
			for (DataRow dr : dtStationsUsed.Rows)
			{
				stasUseed += ",'" + dr.getValue(0).toString() + "'";
			}

			// 3.2 扫描剩余的待办人员，这些待办的人员的使用的本节点的岗位集合 是否 在消耗掉的岗位集合中，如果有，就删除他的待办.
			gwls = new GenerWorkerLists();
			gwls.Retrieve("WorkID", gwf.getWorkID(), "IsPass", 0);
			for(GenerWorkerList item : gwls.ToJavaList())
			{
				if (item.getFK_Emp().equals(WebUser.getNo()) == true)
					continue;

				//未处理的人的岗位集合.
				String sqlGroupUser = "SELECT FK_Station FROM Port_DeptEmpStation A  WHERE  FK_Station IN ("+ stasSQLIn + ") AND FK_Emp='" + item.getFK_Emp() + "'";
				DataTable dtGroupUser = DBAccess.RunSQLReturnTable(sqlGroupUser);

				// 判断  sqlGroupMy  >= sqlGroupUser  是否包含,如果包含，就是删除对象.
				boolean isCanDel = true;
				for (DataRow dr : dtGroupUser.Rows)
				{
					String staNo = "'" + dr.getValue(0).toString() + "'";
					if (stasUseed.contains(staNo) == false)
						isCanDel = false;
				}

				//符合删除的条件.
				if (isCanDel == true)
				{
					item.setIsEnable(false) ;
					item.setIsPassInt(1);
					item.Update();
				}
			}
			//endregion  检查同岗位的人员是否有交集: 番茄的人,马铃薯的人，都分别审批了，需要删除 番茄+马铃薯岗位的人.
		}
	}
	/** 
	 处理发送返回，断头路节点.
	*/
	public static WorkNode IsSendBackNode(WorkNode wn) throws Exception {
		if (wn.getHisNode().isSendBackNode() == false)
		{
			return wn; //如果不是断头路节点，就让其返回.
		}
		if (wn.getHisGenerWorkFlow().getWFState() == WFState.ReturnSta)
		{
			//是退回状态且原路返回的情况
			String sql = "SELECT ReturnNode, Returner, ReturnerName, IsBackTracking ";
			sql += " FROM WF_ReturnWork  ";
			sql += " WHERE WorkID=" + wn.getWorkID() + " ORDER BY RDT DESC";
			DataTable mydt = DBAccess.RunSQLReturnTable(sql);
			if (mydt.Rows.size() != 0 && mydt.Rows.get(0).getValue(3).toString().equals("1") == true)
			{
				wn.JumpToNode = new Node(Integer.parseInt(mydt.Rows.get(0).getValue("ReturnNode").toString()));
				wn.JumpToEmp = mydt.Rows.get(0).getValue("Returner").toString();
				return wn;
			}

		}
		if (wn.getHisNode().getHisToNDNum() != 0)
		{
			throw new RuntimeException("err@流程设计错误:当前节点是发送自动返回节点，但是当前节点不能有到达的节点.");
		}

		if (wn.getHisNode().getHisRunModel() != RunModel.Ordinary)
		{
			throw new RuntimeException("err@流程设计错误:只能是线性节点才能设置[发送并返回]属性,当前节点是[" + wn.getHisNode().getHisRunModel().toString() + "]");
		}

		//判断是否是最后一个人？
		boolean isLastOne = false;
		GenerWorkerLists gwls = new GenerWorkerLists();
		gwls.Retrieve(GenerWorkerListAttr.WorkID, wn.getWorkID(), GenerWorkerListAttr.FK_Node, wn.getHisNode().getNodeID(), GenerWorkerListAttr.IsPass, 0, null);
		if (gwls.size() == 1)
		{
			isLastOne = true; //如果只有一个，本人就是lastOne.
		}

		//WorkNode wn= this.GetPreviousWorkNode();
		//this.JumpToEmp = wn.HisWork.Rec; //对于绑定的表单有问题.
		//this.JumpToNode = wn.HisNode;

		if (isLastOne == true || wn.getHisNode().getTodolistModel() == TodolistModel.QiangBan)
		{
			String ptable = "ND" + Integer.parseInt(wn.getHisFlow().getNo()) + "Track";

			String mysql = "";
			if (wn.getHisNode().getHisRunModel() == RunModel.SubThread)
			{
				mysql = "SELECT NDFrom,EmpFrom FROM " + ptable + " WHERE (WorkID =" + wn.getWorkID() + " AND FID=" + wn.getHisGenerWorkFlow().getFID() + ") AND ActionType!= " + ActionType.UnSend.getValue() + " AND NDTo = " + wn.getHisNode().getNodeID() + " AND(NDTo != NDFrom) AND NDFrom In(Select Node From WF_Direction Where ToNode=" + wn.getHisNode().getNodeID() + " AND FK_Flow='" + wn.getHisFlow().getNo() + "') ORDER BY RDT DESC";
			}
			else
			{
				mysql = "SELECT NDFrom,EmpFrom FROM " + ptable + " WHERE WorkID =" + wn.getWorkID() + " AND ActionType!= " + ActionType.UnSend.getValue() + " AND NDTo = " + wn.getHisNode().getNodeID() + " AND(NDTo != NDFrom) AND NDFrom In(Select Node From WF_Direction Where ToNode=" + wn.getHisNode().getNodeID() + " AND FK_Flow='" + wn.getHisFlow().getNo() + "') ORDER BY RDT DESC";
			}

			//DataTable mydt = DBAccess.RunSQLReturnTable("SELECT FK_Node,FK_Emp FROM WF_GenerWorkerList WHERE WorkID=" + this.WorkID + " AND FK_Node!=" + this.HisNode.NodeID + " ORDER BY RDT DESC ");
			DataTable mydt = DBAccess.RunSQLReturnTable(mysql);
			if (mydt.Rows.size() == 0)
			{
				throw new RuntimeException("系统错误，没有找到上一个节点.");
			}

			wn.JumpToEmp = mydt.Rows.get(0).getValue(1).toString();
			int priNodeID = Integer.parseInt(mydt.Rows.get(0).getValue(0).toString());
			wn.JumpToNode = new Node(priNodeID);

			//清除选择，防止在自动发送到该节点上来.
			wn.getHisGenerWorkFlow().setParasToNodes("");
			wn.getHisGenerWorkFlow().DirectUpdate();

			//清除上次发送的选择,不然下次还会自动发送到当前的节点上来.
			mysql = "DELETE FROM WF_SelectAccper WHERE FK_Node=" + wn.JumpToNode.getNodeID() + " AND WorkID=" + wn.getWorkID();
			DBAccess.RunSQL(mysql);
		}
		return wn;
	}
	/** 
	 处理 askfor 状态
	 
	 param wn
	*/
	public static SendReturnObjs DealAskForState(WorkNode wn) throws Exception {
		/*如果是加签状态, 就判断加签后，是否要返回给执行加签人.*/
		GenerWorkerLists gwls = new GenerWorkerLists();
		gwls.Retrieve(GenerWorkerListAttr.FK_Node, wn.getHisNode().getNodeID(), GenerWorkerListAttr.WorkID, wn.getWorkID(), null);

		boolean isDeal = false;
		AskforHelpSta askForSta = AskforHelpSta.AfterDealSend;
		for (GenerWorkerList item : gwls.ToJavaList())
		{
			if (item.isPassInt() == AskforHelpSta.AfterDealSend.getValue())
			{
				/*如果是加签后，直接发送就不处理了。*/
				isDeal = true;
				askForSta = AskforHelpSta.AfterDealSend;

				// 更新workerlist, 设置所有人员的状态为已经处理的状态,让它走到下一步骤去.
				DBAccess.RunSQL("UPDATE WF_GenerWorkerList SET IsPass=1 WHERE FK_Node=" + wn.getHisNode().getNodeID() + " AND WorkID=" + wn.getWorkID());

				//写入日志.
				wn.AddToTrack(ActionType.ForwardAskfor, item.getFK_Emp(), item.getFK_EmpText(), wn.getHisNode().getNodeID(), wn.getHisNode().getName(), bp.wf.Glo.multilingual("加签后向下发送，直接发送给下一步处理人。", "WorkNode", "send_to_next"));

			}

			if (item.isPassInt() == AskforHelpSta.AfterDealSendByWorker.getValue())
			{
				/*如果是加签后，在由我直接发送。*/
				item.setIsPassInt(0);

				isDeal = true;
				askForSta = AskforHelpSta.AfterDealSendByWorker;

				// 更新workerlist, 设置所有人员的状态为已经处理的状态.
				DBAccess.RunSQL("UPDATE WF_GenerWorkerList SET IsPass=1 WHERE FK_Node=" + wn.getHisNode().getNodeID() + " AND WorkID=" + wn.getWorkID());

				// 把发起加签人员的状态更新过来，让他可见待办工作.
				item.setIsPassInt(0);
				item.Update();

				// 更新流程状态.
				wn.getHisGenerWorkFlow().setWFState(WFState.AskForReplay);
				wn.getHisGenerWorkFlow().Update();

				//让加签人，设置成工作未读。
				bp.wf.Dev2Interface.Node_SetWorkUnRead(wn.getWorkID(), item.getFK_Emp());

				// 从临时变量里获取回复加签意见.
				String replyInfo = wn.getHisGenerWorkFlow().getParasAskForReply();

				////写入日志.
				//this.AddToTrack(ActionType.ForwardAskfor, item.FK_Emp, item.FK_EmpText,
				//    this.HisNode.NodeID, this.HisNode.Name,
				//    "加签后向下发送，并转向加签人发起人（" + item.FK_Emp + "，" + item.FK_EmpText + "）。<br>意见:" + replyInfo);

				//写入日志.
				wn.AddToTrack(ActionType.ForwardAskfor, item.getFK_Emp(), item.getFK_EmpText(), wn.getHisNode().getNodeID(), wn.getHisNode().getName(), bp.wf.Glo.multilingual("回复意见:{0}.", "WorkNode", "reply_comments", replyInfo));

				//加入系统变量。
				wn.addMsg(SendReturnMsgFlag.VarToNodeID, String.valueOf(wn.getHisNode().getNodeID()), SendReturnMsgType.SystemMsg);
				wn.addMsg(SendReturnMsgFlag.VarToNodeName, wn.getHisNode().getName(), SendReturnMsgType.SystemMsg);
				wn.addMsg(SendReturnMsgFlag.VarAcceptersID, item.getFK_Emp(), SendReturnMsgType.SystemMsg);
				wn.addMsg(SendReturnMsgFlag.VarAcceptersName, item.getFK_EmpText(), SendReturnMsgType.SystemMsg);

				//加入提示信息.
				wn.addMsg(SendReturnMsgFlag.SendSuccessMsg, bp.wf.Glo.multilingual("已经转给加签的发起人({0},{1})", "WorkNode", "send_to_the_operator", item.getFK_Emp().toString(), item.getFK_EmpText()), SendReturnMsgType.Info);

				//删除当前操作员临时增加的工作列表记录, 如果不删除就会导致第二次加签失败.
				GenerWorkerList gwl = new GenerWorkerList();
				gwl.Delete(GenerWorkerListAttr.FK_Node, wn.getHisNode().getNodeID(), GenerWorkerListAttr.WorkID, wn.getWorkID(), GenerWorkerListAttr.FK_Emp, wn.getExecer());

				//调用发送成功事件.
				String sendSuccess = ExecEvent.DoNode(EventListNode.SendSuccess, wn);
				wn.HisMsgObjs.AddMsg("info21", sendSuccess, sendSuccess, SendReturnMsgType.Info);

				//执行时效考核.
				Glo.InitCH(wn.getHisFlow(), wn.getHisNode(), wn.getWorkID(), 0, wn.getHisGenerWorkFlow().getTitle());

				//返回发送对象.
				return wn.HisMsgObjs;
			}
		}

		if (isDeal == false)
		{
			throw new RuntimeException(bp.wf.Glo.multilingual("@流程引擎错误，不应该找不到加签的状态.", "WorkNode", "wf_eng_error_1"));
		}

		return null;
	}
	/** 
	 执行分河流状态
	 
	 param wn
	*/
	public static void DealHeLiuState(WorkNode wn) throws Exception {
		/*   如果是合流点 检查当前是否是合流点如果是，则检查分流上的子线程是否完成。*/
		/*检查是否有子线程没有结束*/
		Paras ps = new Paras();
		ps.SQL = "SELECT WorkID,FK_Emp,FK_EmpText,FK_NodeText FROM WF_GenerWorkerList WHERE FID=" + ps.getDBStr() + "FID AND IsPass=0 AND IsEnable=1";
		ps.Add(WorkAttr.FID, wn.getWorkID());

		DataTable dtWL = DBAccess.RunSQLReturnTable(ps);
		String infoErr = "";
		if (dtWL.Rows.size() != 0)
		{
			if (wn.getHisNode().getThreadKillRole() == ThreadKillRole.None || wn.getHisNode().getThreadKillRole() == ThreadKillRole.ByHand)
			{
				infoErr += bp.wf.Glo.multilingual("@您不能向下发送，有如下子线程没有完成。", "WorkNode", "cannot_send_to_next_1");

				for (DataRow dr : dtWL.Rows)
				{
					String op = bp.wf.Glo.multilingual("@操作员编号:{0},{1}", "WorkNode", "current_operator", dr.getValue("FK_Emp").toString(), dr.getValue("FK_EmpText").toString());
					String nd = bp.wf.Glo.multilingual("停留节点:{0}.", "WorkNode", "current_node", dr.getValue("FK_NodeText").toString());
					//infoErr += "@操作员编号:" + dr["FK_Emp"] + "," + dr["FK_EmpText"] + ",停留节点:" + dr["FK_NodeText"];
					infoErr += op + ";" + nd;
				}

				if (wn.getHisNode().getThreadKillRole() == ThreadKillRole.ByHand)
				{
					infoErr += bp.wf.Glo.multilingual("@请通知他们处理完成,或者强制删除子流程您才能向下发送.", "WorkNode", "cannot_send_to_next_2");
				}

				else
				{
					infoErr += bp.wf.Glo.multilingual("@请通知他们处理完成,您才能向下发送.", "WorkNode", "cannot_send_to_next_3");
				}

				//抛出异常阻止它向下运动。
				throw new RuntimeException(infoErr);
			}

			if (wn.getHisNode().getThreadKillRole() == ThreadKillRole.ByAuto)
			{
				//删除每个子线程，然后向下运动。
				for (DataRow dr : dtWL.Rows)
				{
					bp.wf.Dev2Interface.Flow_DeleteSubThread(Long.parseLong(dr.getValue(0).toString()), bp.wf.Glo.multilingual("合流点发送时自动删除", "WorkNode", "auto_delete"));
				}
			}
		}
	}

	/** 
	 子流程运行结束后
	 
	 param wn
	*/
	public static String SubFlowEvent(WorkNode wn) throws Exception {
		GenerWorkFlow gwf = new GenerWorkFlow(wn.getWorkID());
		//判断是否是子流程
		if (gwf.getPWorkID() == 0)
		{
			return "";
		}
		String msg = "";
		//子流程运行到指定节点后父流程自动运行到下一个节点
		if (gwf.getWFState() != WFState.Complete)
		{
			SubFlows subFlows = new SubFlows();
			subFlows.Retrieve(SubFlowAttr.FK_Node, gwf.getPNodeID(), SubFlowAttr.SubFlowNo, wn.getHisFlow().getNo(), null);
			if (subFlows.size() == 0)
			{
				return "";
			}
			SubFlow subFlow = subFlows.get(0) instanceof SubFlow ? (SubFlow)subFlows.get(0) : null;

			if (subFlow.getSubFlowNodeID() == 0 || wn.getHisNode().getNodeID() != subFlow.getSubFlowNodeID())
			{
				return "";
			}

			if (subFlow.getParentFlowSendNextStepRole() == SubFlowRunModel.SpecifiedNodes)
			{
				//获取父流程实例信息
				GenerWorkFlow pgwf = new GenerWorkFlow(gwf.getPWorkID());
				if (pgwf.getFK_Node() == gwf.getPNodeID())
				{
					SendReturnObjs returnObjs = bp.wf.Dev2Interface.Node_SendWork(gwf.getPFlowNo(), gwf.getPWorkID());
					msg = "父流程自动运行到下一个节点，" + returnObjs.ToMsgOfHtml();

				}
				return msg;
			}
			return "";
		}

		//子流程结束后父流程/同级子流程的处理
		if (gwf.getWFState() == WFState.Complete)
		{
			//先判断当前流程是下级子流程还是同级子流程
			long slworkid = gwf.GetParaInt("SLWorkID", 0);
			SubFlows subFlows = new SubFlows();
			SubFlow subFlow = null;
			//下级子流程
			if (slworkid == 0)
			{
				//判断子流程中的设置关系
				subFlows.Retrieve(SubFlowAttr.FK_Node, gwf.getPNodeID(), SubFlowAttr.SubFlowNo, wn.getHisFlow().getNo(), null);
				if (subFlows.size() == 0)
				{
					return "";
				}
				subFlow = subFlows.get(0) instanceof SubFlow ? (SubFlow)subFlows.get(0) : null;

				//把子流程的数据反填到父流程中
				SubFlowOver_CopyDataToParantFlow(subFlow, wn.getHisGenerWorkFlow().getPWorkID(), wn);

				int pnodeId = gwf.getPNodeID();
				FrmSubFlow nd = new FrmSubFlow(pnodeId);
				switch (nd.getAllSubFlowOverRole())
				{
					case None: //父节点不设置所有子流程结束规则
												  //发送成功后显示父流程的待办
						if (subFlow.getSubFlowHidTodolist() == true)
						{
							GenerWorkFlow pgwf = new GenerWorkFlow(gwf.getPWorkID());
							String mysql = "SELECT COUNT(WorkID) as Num FROM WF_GenerWorkFlow WHERE PWorkID=" + gwf.getPWorkID() + " AND FK_Flow='" + wn.getHisFlow().getNo() + "' AND WFState !=3 ";
							if (DBAccess.RunSQLReturnValInt(mysql, 0) == 0)
							{
								DBAccess.RunSQL("UPDATE WF_GenerWorkerlist SET IsPass=0 Where WorkID=" + pgwf.getWorkID() + " AND FK_Node=" + pgwf.getFK_Node() + " AND IsPass=100");

							}
						}

						//单个子流程控制父流程运行到下一个节点
						if (subFlow.getParentFlowSendNextStepRole() == SubFlowRunModel.FlowOver)
						{
							msg = SubFlowOver_ParentFlowAutoSendNextSetp(false, wn.getHisGenerWorkFlow().getPWorkID(), wn.getHisGenerWorkFlow());
						}
						//单个子流程控制父流程结束
						if (subFlow.getParentFlowOverRole() == SubFlowRunModel.FlowOver)
						{
							msg = SubFlowOver_ParentFlowOver(false, wn.getHisGenerWorkFlow().getPWorkID());
						}
						break;
					case SendParentFlowToNextStep: //父流程设置所有子流程结束后，父流程运行到下一个节点
						msg = SubFlowOver_ParentFlowAutoSendNextSetp(true, wn.getHisGenerWorkFlow().getPWorkID(), wn.getHisGenerWorkFlow());
						break;
					case OverParentFlow: //父流程设置所有子流程结束后，父流程结束
						msg = SubFlowOver_ParentFlowOver(true, wn.getHisGenerWorkFlow().getPWorkID());
						break;
					case SendParentFlowToSpecifiedNode://流程运行到指定的节点 ZKR
						msg = SubFlowOver_ParentFlowToSpecifiedNode(true,nd,subFlow, wn.getHisGenerWorkFlow().getPWorkID());
						break;
					default:
						break;

				}

				return msg;
			}

			//同级子流程
			String slFlowNo = gwf.GetParaString("SLFlowNo");
			int slNodeID = gwf.GetParaInt("SLNodeID", 0);
			subFlows.Retrieve(SubFlowAttr.FK_Node, slNodeID, SubFlowAttr.SubFlowNo, wn.getHisFlow().getNo(), null);
			if (subFlows.size() == 0)
			{
				return "";
			}
			subFlow = subFlows.get(0) instanceof SubFlow ? (SubFlow)subFlows.get(0) : null;
			//子流程运行结束后父流程是否自动往下运行一步
			msg = SubFlowOver_DealSLSubFlow(slworkid, subFlow, wn.getHisGenerWorkFlow());

			return msg;
		}
		return "";

	}
	/** 
	 子流程运行结束后把数据反填到父流程中
	 
	 param subFlow
	 param pworkid
	 param wn
	*/
	private static void SubFlowOver_CopyDataToParantFlow(SubFlow subFlow, long pworkid, WorkNode wn) throws Exception {
		//子流程结束后，数据不反填到父流程中
		if (subFlow.getBackCopyRole() == BackCopyRole.None)
		{
			return;
		}
		Node nd = new Node(subFlow.getFK_Node());
		Work pwk = nd.getHisWork();
		pwk.setOID(pworkid);
		pwk.RetrieveFromDBSources();

		GERpt prpt = new bp.wf.GERpt("ND" + Integer.parseInt(subFlow.getFK_Flow()) + "Rpt");
		prpt.setOID(pworkid);
		prpt.RetrieveFromDBSources();


		//判断是否启用了数据字段反填规则
		if (subFlow.getBackCopyRole() == BackCopyRole.AutoFieldMatch || subFlow.getBackCopyRole() == BackCopyRole.MixedMode)
		{
			//子流程数据拷贝到父流程中
			pwk.Copy(wn.getHisWork());
			prpt.Copy(wn.getHisWork());
		}
		// 子流程数据拷贝到父流程中
		if ((subFlow.getBackCopyRole() == BackCopyRole.FollowSetFormat || subFlow.getBackCopyRole() == BackCopyRole.MixedMode) && DataType.IsNullOrEmpty(subFlow.getParentFlowCopyFields()) == false)
		{
			Work wk = wn.getHisWork();
			Attrs attrs = wk.getEnMap().getAttrs();
			//获取子流程的签批字段
			String keyOfEns = "";
			String keyVals = ""; //签批字段存储的值
			for (Attr attr : attrs.ToJavaList())
			{
				if (attr.getUIContralType() == UIContralType.SignCheck)
				{
					keyOfEns += attr.getField() + ",";
					continue;
				}

			}

			//父流程把子流程不同字段进行匹配赋值
			AtPara ap = new AtPara(subFlow.getParentFlowCopyFields());
			for (String str : ap.getHisHT().keySet())
			{
				Object val = ap.GetValStrByKey(str);
				if (DataType.IsNullOrEmpty(val.toString()) == true)
				{
					continue;
				}
				pwk.SetValByKey(val.toString(), wk.GetValByKey(str));
				prpt.SetValByKey(val.toString(), wk.GetValByKey(str));
				if (keyOfEns.contains(str + ",") == true)
				{
					keyVals += wk.GetValByKey(str);
				}
			}
			if (DataType.IsNullOrEmpty(keyVals) == false)
			{
				String trackPTable = "ND" + Integer.parseInt(wn.getHisFlow().getNo()) + "Track";
				//把子流程的签批字段对应的审核信息拷贝到父流程中
				keyVals = keyVals.substring(1);
				String sql = "SELECT * FROM " + trackPTable + " WHERE ActionType=22 AND WorkID=" + wn.getWorkID() + " AND NDFrom IN(" + keyVals + ")";
				DataTable dt = DBAccess.RunSQLReturnTable(sql);
				Tracks tracks = new Tracks();
				QueryObject.InitEntitiesByDataTable(tracks, dt, null);
				for (Track t : tracks.ToJavaList())
				{

					t.setWorkID(pwk.getOID());
					t.setFID(pwk.getFID());
					t.FK_Flow = subFlow.getFK_Flow();
					t.setHisActionType(ActionType.WorkCheck);
					t.setMyPK(String.valueOf(DBAccess.GenerOIDByGUID()));
					t.Insert();
				}
			}

		}
		pwk.Update();
		prpt.Update();

	}
	/** 
	 子流程运行结束后，父流程自动运行到下一个节点
	 
	 param isAllSubFlowOver
	 param pworkid
	 param gwf
	 @return 
	 @exception Exception
	*/
	private static String SubFlowOver_ParentFlowAutoSendNextSetp(boolean isAllSubFlowOver, long pworkid, GenerWorkFlow gwf) throws Exception {
		//所有子流程结束后，父流程自动运行到下一个节点
		if (isAllSubFlowOver == true)
		{
			if (bp.wf.Dev2Interface.Flow_NumOfSubFlowRuning(pworkid) != 0)
			{
				return "";
			}
		}


			///#region 检查父流程是否符合自动运行到下一个节点的条件
		GenerWorkFlow pgwf = new GenerWorkFlow();
		pgwf.setWorkID(pworkid);
		if (pgwf.RetrieveFromDBSources() == 0)
		{
			return ""; // 父流程被删除了也不能执行。
		}

		if (pgwf.getWFState() == WFState.Complete)
		{
			return ""; //父流程已经完成也不能执行.
		}

		//检查父流程的当前停留的节点是否还是发起子流程的节点？
		if (gwf.getPNodeID() != pgwf.getFK_Node())
		{
			return "";
		}

			///#endregion 检查父流程是否符合自动运行到下一个节点的条件

		//获得父流程.
		String[] strs = pgwf.getTodoEmps().split("[;]", -1);
		strs = strs[0].split("[,]", -1);
		String empNo = strs[0];
		if (DataType.IsNullOrEmpty(empNo) == true)
		{
			throw new RuntimeException("err@没有找到父流程的处理人.");
		}

		//当前登录用户.
		String currUserNo = WebUser.getNo();
		try
		{
			Emp emp = new Emp(empNo);
			//让父流程的userNo登录.
			bp.wf.Dev2Interface.Port_Login(emp.getNo());
			SendReturnObjs objs = null;
			try
			{
				objs = bp.wf.Dev2Interface.Node_SendWork(pgwf.getFK_Flow(), pgwf.getWorkID());
				//切换到当前流程节点.
				bp.wf.Dev2Interface.Port_Login(currUserNo);
			}
			catch (RuntimeException ex)
			{
				bp.wf.Dev2Interface.Port_Login(currUserNo);
				throw new RuntimeException(ex.getMessage());
			}

			return "@成功让父流程运行到下一个节点." + objs.ToMsgOfHtml();
		}
		catch (RuntimeException ex)
		{
			//切换到当前流程节点.
			bp.wf.Dev2Interface.Port_Login(currUserNo);
			String info = "这个错误";
			if (ex.getMessage().contains("WorkOpt/") == true)
			{
				info += "@流程设计错误:自动运行到的下一个节点的接收人规则是由上一个人员来选择的,导致到不能自动运行到下一步骤.";
				return info;
			}

			return "@在最后一个子流程完成后，让父流程的节点自动发送时，出现错误:" + ex.getMessage();
		}
	}
	/** 
	 子流程结束后，父流程自动结束
	 
	 param isAllSubFlowOver
	 param pworkid
	 @return 
	*/
	public static String SubFlowOver_ParentFlowOver(boolean isAllSubFlowOver, long pworkid) throws Exception {
		//所有子流程结束后，父流程自动结束
		if (isAllSubFlowOver == true)
		{
			if (bp.wf.Dev2Interface.Flow_NumOfSubFlowRuning(pworkid) != 0)
			{
				return "";
			}
		}
		GenerWorkFlow gwf = new GenerWorkFlow(pworkid);
		if (gwf.getWFState() == WFState.Complete)
		{
			return "";
		}

		return bp.wf.Dev2Interface.Flow_DoFlowOver(gwf.getWorkID(), "父流程[" + gwf.getFlowName() + "],标题为[" + gwf.getTitle() + "]成功结束");
	}

	/**
	 子流程结束后，父流程自动结束

	 param isAllSubFlowOver
	 param pworkid @ZKR
	 @return
	 */
	public static String SubFlowOver_ParentFlowToSpecifiedNode(boolean isAllSubFlowOver,FrmSubFlow nd,SubFlow subFlow, long pworkid) throws Exception {
		//所有子流程结束后，父流程自动结束
		if (isAllSubFlowOver == true)
		{
			if (bp.wf.Dev2Interface.Flow_NumOfSubFlowRuning(pworkid) != 0)
				return "";
		}
		int skipNodeID = nd.GetValIntByKey("SkipNodeID",0);
		GenerWorkFlow gwf = new GenerWorkFlow(pworkid);
		if(skipNodeID==0){
			if (subFlow.getSubFlowHidTodolist() == true)
				DBAccess.RunSQL("UPDATE WF_GenerWorkerlist SET IsPass=0 Where WorkID=" + gwf.getWorkID() + " AND FK_Node=" + gwf.getFK_Node() + " AND IsPass=100");
			throw new Exception("err@所有子流程结束后,父流程跳转到指定节点，指定节点没有设置");
		}
		if (gwf.getWFState() == WFState.Complete)
			return "";
		//当前登录用户.
		String currUserNo = WebUser.getNo();
		try
		{
			//获得父流程.
			String[] strs = gwf.getTodoEmps().split("[;]", -1);
			strs = strs[0].split("[,]", -1);
			String empNo = strs[0];
			if (DataType.IsNullOrEmpty(empNo) == true)
			{
				throw new RuntimeException("err@没有找到父流程的处理人.");
			}
			Emp emp = new Emp(empNo);
			//让父流程的userNo登录.
			bp.wf.Dev2Interface.Port_Login(emp.getNo());
			SendReturnObjs objs = null;
			try
			{
				objs =  bp.wf.Dev2Interface.Node_SendWork(gwf.getFK_Flow(), gwf.getWorkID(),skipNodeID,null);
				//切换到当前流程节点.
				bp.wf.Dev2Interface.Port_Login(currUserNo);
			}
			catch (RuntimeException ex)
			{
				bp.wf.Dev2Interface.Port_Login(currUserNo);
				throw new RuntimeException(ex.getMessage());
			}

			return "@父流程运行到指定节点:<br/>" + objs.ToMsgOfHtml();
		}
		catch (RuntimeException ex)
		{
			//切换到当前流程节点.
			bp.wf.Dev2Interface.Port_Login(currUserNo);
			String info = "这个错误";
			if (ex.getMessage().contains("WorkOpt/") == true)
			{
				info += "@流程设计错误:自动运行到的指定节点["+skipNodeID+"]的接收人规则是由上一个人员来选择的,导致不能自动运行到指定节点.";
				return info;
			}

			return "@在最后一个子流程完成后，让父流程运行到指定节点时，出现错误:" + ex.getMessage();
		}
	}

	/** 
	 子流程结束后，处理同级子流程
	 
	 param slWorkID
	 param subFlow
	 param gwf
	 @return 
	*/
	private static String SubFlowOver_DealSLSubFlow(long slWorkID, SubFlow subFlow, GenerWorkFlow gwf) throws Exception {
		if (subFlow.isAutoSendSLSubFlowOver() == 0)
		{
			return "";
		}
		String slFlowNo = gwf.GetParaString("SLFlowNo");
		int slNodeID = gwf.GetParaInt("SLNodeID", 0);
		Flow fl = new Flow(slFlowNo);
		GenerWorkFlow subGWF = new GenerWorkFlow(slWorkID);
		if (subFlow.isAutoSendSLSubFlowOver() == 1)
		{
			if (subGWF.getFK_Node() != slNodeID)
			{
				return "";
			}

			//主流程自动运行到一下节点
			SendReturnObjs returnObjs = bp.wf.Dev2Interface.Node_SendWork(slFlowNo, slWorkID);
			String sendSuccess = "同级子流程[" + fl.getName() + "]程自动运行到下一个节点，发送过程如下：\n @接收人" + returnObjs.getVarAcceptersName() + "\n @下一步[" + returnObjs.getVarCurrNodeName() + "]启动";
			return sendSuccess;
		}
		//结束父流程
		if (subFlow.isAutoSendSLSubFlowOver() == 2)
		{
			if (subGWF.getWFState() == WFState.Complete)
			{
				return "";
			}
			return bp.wf.Dev2Interface.Flow_DoFlowOver(slWorkID, "同级子流程流程[" + fl.getName() + "],WorkID为[" + slWorkID + "]成功结束");
		}
		return "";
	}
}