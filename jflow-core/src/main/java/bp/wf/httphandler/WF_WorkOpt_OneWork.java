package bp.wf.httphandler;

import bp.difference.handler.WebContralBase;
import bp.en.*;
import bp.da.*;
import bp.sys.*;
import bp.wf.template.*;
import bp.web.*;
import bp.wf.template.sflow.*;
import bp.wf.*;
import java.util.*;

/** 
 页面功能实体
*/
public class WF_WorkOpt_OneWork extends WebContralBase
{
	/** 
	 进度图.
	 
	 @return 
	*/
	public final String JobSchedule_Init() throws Exception {
		DataSet ds = Dev2Interface.DB_JobSchedule(this.getWorkID());
		return bp.tools.Json.ToJson(ds);
	}
	/** 
	 构造函数
	*/
	public WF_WorkOpt_OneWork()  {
	}
	/* 时间轴
	 *
	 * @return
	 * @throws Exception
	 */
	public final DataTable getTimeBase() throws Exception {
		DataSet ds = new DataSet();

		// 获取track.
		DataTable dt = bp.wf.Dev2Interface.DB_GenerTrackTable(this.getFK_Flow(), this.getWorkID(), this.getFID());
		return dt;
	}
	public final GenerWorkerLists getGwf() throws Exception{
		// 获取 WF_GenerWorkFlow
		GenerWorkFlow gwf = new GenerWorkFlow();
		GenerWorkerLists gwls = new GenerWorkerLists();
		gwf.setWorkID(this.getWorkID());
		gwf.RetrieveFromDBSources();

		if (gwf.getWFState() != WFState.Complete) {

			gwls.Retrieve(GenerWorkerListAttr.WorkID, this.getWorkID(), GenerWorkerListAttr.Idx);

			// warning 补偿式的更新. 做特殊的判断，当会签过了以后仍然能够看isPass=90的错误数据.
			for (GenerWorkerList item : gwls.ToJavaList()) {
				if (item.getIsPassInt() == 90 && gwf.getFK_Node() != item.getFK_Node()) {
					item.setIsPassInt(0);
					item.Update();
				}
			}
		}
		return gwls;
	}
	/** 
	 时间轴
	 
	 @return 
	*/
	public final String TimeSubThread_Init() throws Exception {
		DataSet ds = new DataSet();
		String mypks = GetRequestVal("MyPKs");
		mypks = "('" + mypks.replace(",", "','") + "')";
		String sql = "SELECT MyPK,ActionType,ActionTypeText,FID,WorkID,NDFrom,NDFromT,NDTo,NDToT,EmpFrom,EmpFromT,EmpTo,EmpToT,RDT,WorkTimeSpan,Msg,NodeData,Exer,Tag FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track Where MyPK IN" + mypks + " ORDER BY RDT ASC ";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Track";
		//把列名转化成区分大小写.
		if (bp.difference.SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.UpperCase)
		{
			dt.Columns.get("MYPK").ColumnName = "MyPK";
			dt.Columns.get("ACTIONTYPE").ColumnName = "ActionType";
			dt.Columns.get("ACTIONTYPETEXT").ColumnName = "ActionTypeText";
			dt.Columns.get("FID").ColumnName = "FID";
			dt.Columns.get("WORKID").ColumnName = "WorkID";
			dt.Columns.get("NDFROM").ColumnName = "NDFrom";
			dt.Columns.get("NDFROMT").ColumnName = "NDFromT";
			dt.Columns.get("NDTO").ColumnName = "NDTo";
			dt.Columns.get("NDTOT").ColumnName = "NDToT";
			dt.Columns.get("EMPFROM").ColumnName = "EmpFrom";
			dt.Columns.get("EMPFROMT").ColumnName = "EmpFromT";
			dt.Columns.get("EMPTO").ColumnName = "EmpTo";
			dt.Columns.get("EMPTOT").ColumnName = "EmpToT";
			dt.Columns.get("RDT").ColumnName = "RDT";
			dt.Columns.get("WORKTIMESPAN").ColumnName = "WorkTimeSpan";
			dt.Columns.get("MSG").ColumnName = "Msg";
			dt.Columns.get("NODEDATA").ColumnName = "NodeData";
			dt.Columns.get("EXER").ColumnName = "Exer";
			dt.Columns.get("TAG").ColumnName = "Tag";
		}

		if (bp.difference.SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.Lowercase)
		{
			dt.Columns.get("mypk").ColumnName = "MyPK";
			dt.Columns.get("actiontype").ColumnName = "ActionType";
			dt.Columns.get("actiontypetext").ColumnName = "ActionTypeText";
			dt.Columns.get("fid").ColumnName = "FID";
			dt.Columns.get("workid").ColumnName = "WorkID";
			dt.Columns.get("ndfrom").ColumnName = "NDFrom";
			dt.Columns.get("ndfromt").ColumnName = "NDFromT";
			dt.Columns.get("ndto").ColumnName = "NDTo";
			dt.Columns.get("ndtot").ColumnName = "NDToT";
			dt.Columns.get("empfrom").ColumnName = "EmpFrom";
			dt.Columns.get("empfromt").ColumnName = "EmpFromT";
			dt.Columns.get("empto").ColumnName = "EmpTo";
			dt.Columns.get("emptot").ColumnName = "EmpToT";
			dt.Columns.get("rdt").ColumnName = "RDT";
			dt.Columns.get("worktimespan").ColumnName = "WorkTimeSpan";
			dt.Columns.get("msg").ColumnName = "Msg";
			dt.Columns.get("nodedata").ColumnName = "NodeData";
			dt.Columns.get("exer").ColumnName = "Exer";
			dt.Columns.get("tag").ColumnName = "Tag";
		}
		//获取track.
		ds.Tables.add(dt);



			///#region  父子流程数据存储到这里.
		Hashtable ht = new Hashtable();
		for (DataRow dr : dt.Rows)
		{
			ActionType at = ActionType.forValue(Integer.parseInt(dr.getValue(TrackAttr.ActionType).toString()));

			String tag = dr.getValue(TrackAttr.Tag).toString(); //标识.
			String mypk = dr.getValue(TrackAttr.MyPK).toString(); //主键.

			String msg = "";
			if (at == ActionType.CallChildenFlow)
			{
				//被调用父流程吊起。
				if (DataType.IsNullOrEmpty(tag) == false)
				{
					AtPara ap = new AtPara(tag);
					GenerWorkFlow mygwf = new GenerWorkFlow();
					mygwf.setWorkID(ap.GetValInt64ByKey("PWorkID"));
					if (mygwf.RetrieveFromDBSources() == 1)
					{
						msg = "<p>操作员:{" + dr.getValue(TrackAttr.EmpFromT).toString() + "}在当前节点上，被父流程{" + mygwf.getFlowName() + "},<a target=b" + ap.GetValStrByKey("PWorkID") + " href='Track.htm?WorkID=" + ap.GetValStrByKey("PWorkID") + "&FK_Flow=" + ap.GetValStrByKey("PFlowNo") + "' >" + msg + "</a></p>";
					}
					else
					{
						msg = "<p>操作员:{" + dr.getValue(TrackAttr.EmpFromT).toString() + "}在当前节点上，被父流程调用{" + mygwf.getFlowName() + "}，但是该流程被删除了.</p>" + tag;
					}

					msg = "<a target=b" + ap.GetValStrByKey("PWorkID") + " href='Track.htm?WorkID=" + ap.GetValStrByKey("PWorkID") + "&FK_Flow=" + ap.GetValStrByKey("PFlowNo") + "' >" + msg + "</a>";
				}

				//放入到ht里面.
				ht.put(mypk, msg);
			}

			if (at == ActionType.StartChildenFlow)
			{
				if (DataType.IsNullOrEmpty(tag) == false)
				{
					if (tag.contains("Sub"))
					{
						tag = tag.replace("Sub", "C");
					}

					AtPara ap = new AtPara(tag);
					GenerWorkFlow mygwf = new GenerWorkFlow();
					mygwf.setWorkID(ap.GetValInt64ByKey("CWorkID"));
					if (mygwf.RetrieveFromDBSources() == 1)
					{
						msg = "<p>操作员:{" + dr.getValue(TrackAttr.EmpFromT).toString() + "}在当前节点上调用了子流程{" + mygwf.getFlowName() + "}, <a target=b" + ap.GetValStrByKey("CWorkID") + " href='Track.htm?WorkID=" + ap.GetValStrByKey("CWorkID") + "&FK_Flow=" + ap.GetValStrByKey("CFlowNo") + "' >" + msg + "</a></p>";
						msg += "<p>当前子流程状态：{" + mygwf.getWFStateText() + "}，运转到:{" + mygwf.getNodeName() + "}，最后处理人{" + mygwf.getTodoEmps() + "}，最后处理时间{" + mygwf.getRDT() + "}。</p>";
					}
					else
					{
						msg = "<p>操作员:{" + dr.getValue(TrackAttr.EmpFromT).toString() + "}在当前节点上调用了子流程{" + mygwf.getFlowName() + "}，但是该流程被删除了.</p>" + tag;
					}

				}

				//放入到ht里面.
				ht.put(mypk, msg);
			}
		}

			///#endregion


		//把节点审核配置信息.
		NodeWorkCheck fwc = new NodeWorkCheck(this.getFK_Node());
		ds.Tables.add(fwc.ToDataTableField("FrmWorkCheck"));

		//返回结果.
		return bp.tools.Json.ToJson(ds);
	}

	/** 
	 时间轴
	 
	 @return 
	*/
	public final String TimeBase_Init() throws Exception {
		DataSet ds = new DataSet();

		//获取干流程和子线程中的Track信息
		DataTable dt = Dev2Interface.DB_GenerTrackTable(this.getFK_Flow(), this.getWorkID(), this.getFID(), false);
		ds.Tables.add(dt);


		/*#region  父子流程数据存储到这里.
		Hashtable ht = new Hashtable();
		foreach (DataRow dr in dt.Rows)
		{
		    ActionType at = (ActionType)int.Parse(dr[TrackAttr.ActionType].ToString());

		    string tag = dr[TrackAttr.Tag].ToString(); //标识.
		    string mypk = dr[TrackAttr.MyPK].ToString(); //主键.

		    string msg = "";
		    if (at == ActionType.CallChildenFlow)
		    {
		        //被调用父流程吊起。
		        if (DataType.IsNullOrEmpty(tag) == false)
		        {
		            AtPara ap = new AtPara(tag);
		            GenerWorkFlow mygwf = new GenerWorkFlow();
		            mygwf.WorkID = ap.GetValInt64ByKey("PWorkID");
		            if (mygwf.RetrieveFromDBSources() == 1)
		                msg = "<p>操作员:{" + dr[TrackAttr.EmpFromT].ToString() + "}在当前节点上，被父流程{" + mygwf.FlowName + "},<a target=b" + ap.GetValStrByKey("PWorkID") + " href='Track.htm?WorkID=" + ap.GetValStrByKey("PWorkID") + "&FK_Flow=" + ap.GetValStrByKey("PFlowNo") + "' >" + msg + "</a></p>";
		            else
		                msg = "<p>操作员:{" + dr[TrackAttr.EmpFromT].ToString() + "}在当前节点上，被父流程调用{" + mygwf.FlowName + "}，但是该流程被删除了.</p>" + tag;

		            msg = "<a target=b" + ap.GetValStrByKey("PWorkID") + " href='Track.htm?WorkID=" + ap.GetValStrByKey("PWorkID") + "&FK_Flow=" + ap.GetValStrByKey("PFlowNo") + "' >" + msg + "</a>";
		        }

		        //放入到ht里面.
		        ht.Add(mypk, msg);
		    }

		    if (at == ActionType.StartChildenFlow)
		    {
		        if (DataType.IsNullOrEmpty(tag) == false)
		        {
		            if (tag.contains("Sub"))
		                tag = tag.Replace("Sub", "C");

		            AtPara ap = new AtPara(tag);
		            GenerWorkFlow mygwf = new GenerWorkFlow();
		            mygwf.WorkID = ap.GetValInt64ByKey("CWorkID");
		            if (mygwf.RetrieveFromDBSources() == 1)
		            {
		                msg = "<p>操作员:{" + dr[TrackAttr.EmpFromT].ToString() + "}在当前节点上调用了子流程{" + mygwf.FlowName + "}, <a target=b" + ap.GetValStrByKey("CWorkID") + " href='Track.htm?WorkID=" + ap.GetValStrByKey("CWorkID") + "&FK_Flow=" + ap.GetValStrByKey("CFlowNo") + "' >" + msg + "</a></p>";
		                msg += "<p>当前子流程状态：{" + mygwf.WFStateText + "}，运转到:{" + mygwf.NodeName + "}，最后处理人{" + mygwf.TodoEmps + "}，最后处理时间{" + mygwf.RDT + "}。</p>";
		            }
		            else
		                msg = "<p>操作员:{" + dr[TrackAttr.EmpFromT].ToString() + "}在当前节点上调用了子流程{" + mygwf.FlowName + "}，但是该流程被删除了.</p>" + tag;

		        }

		        //放入到ht里面.
		        ht.Add(mypk, msg);
		    }
		}
		#endregion*/

		//获取当前流程的待办信息 WF_GenerWorkFlow
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(this.getWorkID());
		gwf.RetrieveFromDBSources();
		ds.Tables.add(gwf.ToDataTableField("WF_GenerWorkFlow"));

		if (gwf.getWFState() != WFState.Complete)
		{
			GenerWorkerLists gwls = new GenerWorkerLists();
			gwls.Retrieve(GenerWorkerListAttr.WorkID, this.getWorkID(), GenerWorkerListAttr.Idx);

			//warning 补偿式的更新.  做特殊的判断，当会签过了以后仍然能够看isPass=90的错误数据.
			for (GenerWorkerList item : gwls.ToJavaList())
			{
				if (item.isPassInt() == 90 && gwf.getFK_Node() != item.getFK_Node())
				{
					item.setIsPassInt(0);
					item.Update();
				}
			}
			Node nd = new Node(gwf.getFK_Node());
			if (nd.getHisRunModel() == RunModel.FL || nd.getHisRunModel() == RunModel.FHL)
			{
				//获取是否存在退回的分合流点
				GenerWorkerLists tgwls = new GenerWorkerLists();
				tgwls.Retrieve(GenerWorkerListAttr.FID, this.getWorkID(), GenerWorkerListAttr.FK_Node, gwf.getFK_Node(), GenerWorkerListAttr.IsPass, 0, GenerWorkerListAttr.Idx);
				for (GenerWorkerList gwl : tgwls.ToJavaList())
				{
					gwls.AddEntity(gwl);
				}
			}
			ds.Tables.add(gwls.ToDataTableField("WF_GenerWorkerList"));
		}

		//把节点审核配置信息.
		NodeWorkCheck fwc = new NodeWorkCheck(gwf.getFK_Node());
		ds.Tables.add(fwc.ToDataTableField("FrmWorkCheck"));

		//获取启动的子流程信息
		SubFlows subFlows = new SubFlows(this.getFK_Flow());
		ds.Tables.add(subFlows.ToDataTableField("WF_SubFlow"));

		//返回结果.
		return bp.tools.Json.ToJson(ds);
	}


		///#region 执行父类的重写方法.


		///#endregion 执行父类的重写方法.


		///#region 属性.
	public final String getMsg() throws Exception {
		String str = this.GetRequestVal("TB_Msg");
		if (str == null || str.equals("") || str.equals("null"))
		{
			return null;
		}
		return str;
	}
	public final String getUserName() throws Exception {
		String str = this.GetRequestVal("UserName");
		if (str == null || str.equals("") || str.equals("null"))
		{
			return null;
		}
		return str;
	}
	public final String getTitle() throws Exception {
		String str = this.GetRequestVal("Title");
		if (str == null || str.equals("") || str.equals("null"))
		{
			return null;
		}
		return str;
	}
	/** 
	 字典表
	*/
	public final String getFK_SFTable()  {
		String str = this.GetRequestVal("FK_SFTable");
		if (str == null || str.equals("") || str.equals("null"))
		{
			return null;
		}
		return str;

	}
	public final String getEnumKey()  {
		String str = this.GetRequestVal("EnumKey");
		if (str == null || str.equals("") || str.equals("null"))
		{
			return null;
		}
		return str;

	}


	public final String getName() throws Exception {
		String str = WebUser.getName();
		if (str == null || str.equals("") || str.equals("null"))
		{
			return null;
		}
		return str;
	}

		///#endregion 属性.

	public final String FlowBBS_Delete() throws Exception {
		return Dev2Interface.Flow_BBSDelete(this.getFK_Flow(), this.getMyPK(), WebUser.getNo());
	}
	/** 
	 执行撤销
	 
	 @return 
	*/
	public final String OP_UnSend() throws Exception {
		//获取用户当前所在的节点
		String currNode = "";
		switch (DBAccess.getAppCenterDBType())
		{
			case Oracle:
			case KingBaseR3:
			case KingBaseR6:
				currNode = "SELECT FK_Node FROM (SELECT  FK_Node FROM WF_GenerWorkerlist WHERE FK_Emp='" + WebUser.getNo() + "' AND WorkID=" + this.getWorkID() + "  Order by RDT DESC ) WHERE rownum=1";
				break;
			case MySQL:
			case PostgreSQL:
			case UX:
				currNode = "SELECT  FK_Node FROM WF_GenerWorkerlist WHERE FK_Emp='" + WebUser.getNo() + "' AND WorkID=" + this.getWorkID() + "  Order by RDT DESC LIMIT 1";
				break;
			case MSSQL:
				currNode = "SELECT TOP 1 FK_Node FROM WF_GenerWorkerlist WHERE FK_Emp='" + WebUser.getNo() + "' AND WorkID=" + this.getWorkID() + " Order by RDT DESC";
				break;
			default:
				break;
		}
		String unSendToNode = DBAccess.RunSQLReturnString(currNode);
		try
		{
			return Dev2Interface.Flow_DoUnSend(this.getFK_Flow(), this.getWorkID(), Integer.parseInt(unSendToNode), this.getFID());
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	@Override
	protected String DoDefaultMethod() throws Exception {
		return "err@没有判断的执行类型：" + this.getDoType() + " @类 " + this.toString();
	}

	public final String OP_ComeBack() throws Exception {
		WorkFlow wf3 = new WorkFlow(getWorkID());
		wf3.DoComeBackWorkFlow("无");
		return "流程已经被重新启用.";
	}

	public final String OP_UnHungup() throws Exception {
		WorkFlow wf2 = new WorkFlow(getWorkID());
		//  wf2.DoUnHungup();
		return "流程已经被解除挂起.";
	}

	public final String OP_Hungup() throws Exception {
		WorkFlow wf1 = new WorkFlow(getWorkID());
		//wf1.DoHungup()
		return "流程已经被挂起.";
	}

	public final String OP_DelFlow() throws Exception {
		WorkFlow wf = new WorkFlow(getWorkID());
		wf.DoDeleteWorkFlowByReal(true);
		return "流程已经被删除！";
	}

	/** 
	 获取可操作状态信息
	 
	 @return 
	*/
	public final String OP_GetStatus() throws Exception {
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		Hashtable ht = new Hashtable();

		boolean CanPackUp = true; //是否可以打包下载.


			///#region  PowerModel权限的解析
		String psql = "SELECT A.PowerFlag,A.EmpNo,A.EmpName FROM WF_PowerModel A WHERE PowerCtrlType =1" + " UNION " + "SELECT A.PowerFlag,B.No,B.Name FROM WF_PowerModel A, Port_Emp B, Port_DeptEmpStation C WHERE A.PowerCtrlType = 0 AND B.No=C.FK_Emp AND A.StaNo = C.FK_Station";
		psql = "SELECT PowerFlag From(" + psql + ")D WHERE  D.EmpNo='" + WebUser.getNo() + "'";

		String powers = DBAccess.RunSQLReturnStringIsNull(psql, "");


			///#endregion PowerModel权限的解析


			///#region 文件打印的权限判断，这里为天业集团做的特殊判断，现实的应用中，都可以打印.
		if (bp.difference.SystemConfig.getCustomerNo().equals("TianYe") && !WebUser.getNo().equals("admin"))
		{
			CanPackUp = IsCanPrintSpecForTianYe(gwf);
		}

			///#endregion 文件打印的权限判断，这里为天业集团做的特殊判断，现实的应用中，都可以打印.
		if (CanPackUp == true)
		{
			ht.put("CanPackUp", 1);
		}
		else
		{
			ht.put("CanPackUp", 0);
		}

		//获取打印的方式PDF/RDF,节点打印方式
		Node nd = new Node(this.getFK_Node());
		if (nd.getHisPrintDocEnable() == true)
		{
			ht.put("PrintType", 1);
		}
		else
		{
			ht.put("PrintType", 0);
		}


		//是否可以打印.
		switch (gwf.getWFState())
		{
			case Runing: // 运行时
				/*删除流程.*/
				if (Dev2Interface.Flow_IsCanDeleteFlowInstance(this.getFK_Flow(), this.getWorkID(), WebUser.getNo()) == true)
				{
					ht.put("IsCanDelete", 1);
				}
				else
				{
					ht.put("IsCanDelete", 0);
				}

				if (powers.contains("FlowDataDelete") == true)
				{
					//存在移除这个键值
					if (ht.containsKey("IsCanDelete") == true)
					{
						ht.remove("IsCanDelete");
					}
					ht.put("IsCanDelete", 1);
				}


				/*取回审批*/
				String para = "";
				String sql = "SELECT NodeID FROM WF_Node WHERE CheckNodes LIKE '%" + gwf.getFK_Node() + "%'";
				int myNode = DBAccess.RunSQLReturnValInt(sql, 0);
				if (myNode != 0)
				{
					//GetTask gt = new GetTask(myNode);
					//if (gt.Can_I_Do_It())
					//{
					//    ht.Add("TackBackFromNode", gwf.FK_Node);
					//    ht.Add("TackBackToNode", myNode);
					//    ht.Add("CanTackBack", 1);
					//}
				}

				if (bp.difference.SystemConfig.getCustomerNo().equals("TianYe"))
				{
					ht.put("CanUnSend", 1);

				}
				else
				{
					/*撤销发送*/
					GenerWorkerLists workerlists = new GenerWorkerLists();
					QueryObject info = new QueryObject(workerlists);
					info.AddWhere(GenerWorkerListAttr.FK_Emp, WebUser.getNo());
					info.addAnd();
					info.AddWhere(GenerWorkerListAttr.IsPass, "1");
					info.addAnd();
					info.AddWhere(GenerWorkerListAttr.IsEnable, "1");
					info.addAnd();
					info.AddWhere(GenerWorkerListAttr.WorkID, this.getWorkID());

					if (info.DoQuery() > 0)
					{
						ht.put("CanUnSend", 1);
					}
					else
					{
						ht.put("CanUnSend", 0);
					}

					if (powers.contains("FlowDataUnSend") == true)
					{
						//存在移除这个键值
						if (ht.containsKey("CanUnSend") == true)
						{
							ht.remove("CanUnSend");
						}
						ht.put("CanUnSend", 1);
					}

				}


				//流程结束
				if (powers.contains("FlowDataOver") == true)
				{
					ht.put("CanFlowOver", 1);
				}

				//催办
				if (powers.contains("FlowDataPress") == true)
				{
					ht.put("CanFlowPress", 1);
				}



				//是否可以调整工时
				sql = "SELECT CHRole \"CHRole\" From WF_GenerWorkerList G,WF_Node N Where G.FK_Node=N.NodeID AND N.CHRole!=0 AND WorkID=" + this.getWorkID() + " AND FK_Emp='" + WebUser.getNo() + "'";
				DataTable dt = DBAccess.RunSQLReturnTable(sql);
				if (dt.Rows.size() > 0)
				{
					for (DataRow dr : dt.Rows)
					{
						if (Integer.parseInt(dr.getValue("CHRole").toString()) == 1 || Integer.parseInt(dr.getValue("CHRole").toString()) == 3)
						{
							ht.put("CanChangCHRole", 1);
							break;
						}
						else
						{
							ht.put("CanChangCHRole", 2);
						}

					}



				}



				break;
			case Complete: // 完成.
			case Delete: // 逻辑删除..
				/*恢复使用流程*/
				if (WebUser.getNo().equals("admin") == true)
				{
					ht.put("CanRollBack", 1);
				}
				else
				{
					ht.put("CanRollBack", 0);
				}

				if (powers.contains("FlowDataRollback") == true)
				{
					//存在移除这个键值
					if (ht.containsKey("CanRollBack") == true)
					{
						ht.remove("CanRollBack");
					}
					ht.put("CanRollBack", 1);
				}

				if (nd.getCHRole() != 0) //0禁用 1 启用 2 只读 3 启用并可以调整流程应完成时间
				{
					ht.put("CanChangCHRole", 2);

				}


				//判断是否可以打印.
				break;
			case HungUp: // 挂起.
				/*撤销挂起*/
				if (Dev2Interface.Flow_IsCanDoCurrentWork(getWorkID(), WebUser.getNo()) == false)
				{
					ht.put("CanUnHungup", 0);
				}
				else
				{
					ht.put("CanUnHungup", 1);
				}
				break;
			default:
				break;
		}

		return bp.tools.Json.ToJson(ht);

		//return json + "}";
	}
	/** 
	 是否可以打印.
	 
	 param gwf
	 @return 
	*/
	private boolean IsCanPrintSpecForTianYe(GenerWorkFlow gwf) throws Exception {
		//如果已经完成了，并且节点不是最后一个节点就不能打印.
		if (gwf.getWFState() == WFState.Complete)
		{
			Node nd = new Node(gwf.getFK_Node());
			if (nd.isEndNode() == false)
			{
				return false;
			}
		}

		// 判断是否可以打印.
		String sql = "SELECT Distinct NDFrom, EmpFrom FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track WHERE WorkID=" + this.getWorkID();
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		for (DataRow dr : dt.Rows)
		{
			//判断节点是否启用了按钮?
			int nodeid = Integer.parseInt(dr.getValue(0).toString());
			BtnLab btn = new BtnLab(nodeid);
			if (btn.getPrintPDFEnable() == true || btn.getPrintZipEnable() == true)
			{
				String empFrom = dr.getValue(1).toString();
				if (gwf.getWFState() == WFState.Complete && (WebUser.getNo().equals(empFrom) || gwf.getStarter().equals(WebUser.getNo())))
				{
					return true;
				}
			}
		}
		return false;
	}
	/** 
	 获取OneWork页面的tabs集合
	 
	 @return 
	*/
	public final String OneWork_GetTabs() throws Exception {
		DataTable dt = new DataTable();
		dt.Columns.Add("No", String.class);
		dt.Columns.Add("Name", String.class);
		dt.Columns.Add("Url", String.class);
		dt.Columns.Add("IsDefault", Integer.class);
		Flow flow = new Flow(this.getFK_Flow());
		int nodeID = this.getFK_Node();
		if (nodeID == 0)
		{
			GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
			nodeID = gwf.getFK_Node();
		}
		DataRow dr = null;

		String paras = String.format("FK_Node=%1$s&WorkID=%2$s&FK_Flow=%3$s&FID=%4$s&FromWorkOpt=1&CCSta=" + this.GetRequestValInt("CCSta"), String.valueOf(nodeID), this.getWorkID(), this.getFK_Flow(), this.getFID());
		String url = "";
		if (flow.isFrmEnable() == true)
		{

			Node nd = new Node(nodeID);
			url = "../../MyView.htm?" + paras;
			if ((nd.getHisFormType() == NodeFormType.SDKForm || nd.getHisFormType() == NodeFormType.SelfForm))
			{
				if (nd.getFormUrl().contains("?"))
				{
					url = "@url=" + nd.getFormUrl() + "&IsReadonly=1&" + paras;
				}
				else
				{
					url = "@url=" + nd.getFormUrl() + "?IsReadonly=1&" + paras;
				}
			}
			dr = dt.NewRow();
			dr.setValue("No", "Frm");
			dr.setValue("Name", "表单");
			dr.setValue("Url", url);
			dr.setValue("IsDefault", 0);
			dt.Rows.add(dr);
		}

		if (flow.isTruckEnable() == true)
		{
			dr = dt.NewRow();
			dr.setValue("No", "Truck");
			dr.setValue("Name", "轨迹图");
			dr.setValue("Url", "Chart.htm?" + paras);
			dr.setValue("IsDefault", 0);
			dt.Rows.add(dr);
		}

		if (flow.isTimeBaseEnable() == true)
		{
			dr = dt.NewRow();
			dr.setValue("No", "TimeBase");
			dr.setValue("Name", "时间轴");
			dr.setValue("Url", "TimeBase.htm?" + paras);
			dr.setValue("IsDefault", 0);
			dt.Rows.add(dr);
		}

		if (flow.isTableEnable() == true)
		{
			dr = dt.NewRow();
			dr.setValue("No", "Table");
			dr.setValue("Name", "时间表");
			dr.setValue("Url", "Table.htm?" + paras);
			dr.setValue("IsDefault", 0);
			dt.Rows.add(dr);
		}

		if (flow.isOPEnable() == true)
		{
			dr = dt.NewRow();
			dr.setValue("No", "Oper");
			dr.setValue("Name", "操作");
			dr.setValue("Url", "OP.htm?" + paras);
			dr.setValue("IsDefault", 0);
			dt.Rows.add(dr);

		}

		return bp.tools.Json.ToJson(dt);
	}
	/** 
	 获取流程的JSON数据，以供显示工作轨迹/流程设计
	 
	 param fk_flow 流程编号
	 param workid 工作编号
	 param fid 父流程编号
	 @return 
	*/
	public final String Chart_Init2020() throws Exception {
		//参数.
		String fk_flow = this.getFK_Flow();
		long workid = this.getWorkID();
		long fid = this.getFID();

		DataSet ds = new DataSet();
		DataTable dt = null;
		String json = "";
		try
		{
			//流程信息
			String sql = "SELECT No \"No\", Name \"Name\", Paras \"Paras\", ChartType \"ChartType\" FROM WF_Flow WHERE No='" + fk_flow + "'";
			dt = DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "WF_Flow";
			ds.Tables.add(dt);

			//节点信息 ， 
			// NodePosType=0，开始节点， 1中间节点,2=结束节点.
			// RunModel= select * from sys_enum where Enumkey='RunModel' 
			// TodolistModel= select * from sys_enum where Enumkey='TodolistModel' ;
			sql = "SELECT NodeID \"ID\", Name \"Name\", ICON \"Icon\", X \"X\", Y \"Y\", NodePosType \"NodePosType\",RunModel \"RunModel\",HisToNDs \"HisToNDs\",TodolistModel \"TodolistModel\" FROM WF_Node WHERE FK_Flow='" + fk_flow + "' ORDER BY Step";
			dt = DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "WF_Node";
			ds.Tables.add(dt);

			//标签信息
			sql = "SELECT MyPK \"MyPK\", Name \"Name\", X \"X\", Y \"Y\" FROM WF_LabNote WHERE FK_Flow='" + fk_flow + "'";
			dt = DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "WF_LabNote";
			ds.Tables.add(dt);

			//线段方向信息
			sql = "SELECT Node \"Node\", ToNode \"ToNode\", 0 as  \"DirType\", 0 as \"IsCanBack\" FROM WF_Direction WHERE FK_Flow='" + fk_flow + "'";
			dt = DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "WF_Direction";
			ds.Tables.add(dt);

			//如果workid=0就仅仅返回流程图数据.
			if (workid == 0)
			{
				return bp.tools.Json.ToJson(ds);
			}


			//流程信息.
			GenerWorkFlow gwf = new GenerWorkFlow(workid);
			dt = gwf.ToDataTableField("Main"); // DBAccess.RunSQLReturnTable(string.Format(sql, workid));
			dt.TableName = "WF_GenerWorkFlow";
			ds.Tables.add(dt);

			//把节点审核配置信息.
			NodeWorkCheck fwc = new NodeWorkCheck(gwf.getFK_Node());
			ds.Tables.add(fwc.ToDataTableField("FrmWorkCheck"));


			//获取工作轨迹信息
			String trackTable = "ND" + Integer.parseInt(fk_flow) + "Track";
			sql = "SELECT FID \"FID\",NDFrom \"NDFrom\",NDFromT \"NDFromT\",NDTo  \"NDTo\", NDToT \"NDToT\", ActionType \"ActionType\",ActionTypeText \"ActionTypeText\",Msg \"Msg\",RDT \"RDT\",EmpFrom \"EmpFrom\",EmpFromT \"EmpFromT\", EmpToT \"EmpToT\",EmpTo \"EmpTo\" FROM " + trackTable + " WHERE WorkID=" + workid + (fid == 0 ? (" OR FID=" + workid) : (" OR WorkID=" + fid + " OR FID=" + fid)) + " ORDER BY RDT DESC";
			dt = DBAccess.RunSQLReturnTable(sql);

			DataTable newdt = new DataTable();
			newdt = dt;


				///#region 判断轨迹数据中，最后一步是否是撤销或退回状态的，如果是，则删除最后2条数据
			if (dt.Rows.size() > 0)
			{
				if (dt.Rows.get(0).getValue("ActionType").equals(ActionType.Return.getValue())
					|| dt.Rows.get(0).getValue("ActionType").equals(ActionType.UnSend.getValue()))
				{
					if (dt.Rows.size() > 1)
					{
						dt.Rows.remove(1);
						dt.Rows.remove(0);
					}
					else
					{
						dt.Rows.remove(0);
					}
					newdt = dt;
				}
				else if (dt.Rows.size() > 1
						&& (Integer
						.parseInt(dt.Rows.get(1).getValue("ActionType").toString()) == ActionType.Return.getValue()
						|| Integer.parseInt(dt.Rows.get(1).getValue("ActionType").toString()) == ActionType.UnSend
						.getValue()))
				{
					//删除已发送的节点，
					if (dt.Rows.size() > 3)
					{
						dt.Rows.remove(1);
						dt.Rows.remove(1);
					}
					else
					{
						dt.Rows.remove(1);
					}

					String fk_node = "";
					if (dt.Rows.get(0).getValue("NDFrom").equals(dt.Rows.get(0).getValue("NDTo")))
					{
						fk_node = dt.Rows.get(0).getValue("NDFrom").toString();
					}
					if (DataType.IsNullOrEmpty(fk_node) == false)
					{
						//如果是跳转页面，则需要删除中间跳转的节点
						for (DataRow dr : dt.Rows)
						{
							int at = Integer.parseInt(dr.getValue("ACTIONTYPE").toString());
							if (at == ActionType.Skip.getValue() && dr.getValue("NDFrom").toString().equals(fk_node))
								continue;
							DataRow newdr = newdt.NewRow();
							newdr.ItemArray = dr.ItemArray;
							newdt.Rows.add(newdr);
						}
					}
					else
					{
						newdt = dt.copy();
					}
				}
				else
				{
					newdt = dt.copy();
				}
			}
			newdt.TableName = "Track";
			ds.Tables.add(newdt);

				///#endregion


				///#region 如果流程没有完成,就把工作人员列表返回过去.
			if (gwf.getWFState() != WFState.Complete)
			{
				//加入工作人员列表.
				GenerWorkerLists gwls = new GenerWorkerLists();
				long id = this.getFID();
				if (id == 0)
				{
					id = this.getWorkID();
				}

				QueryObject qo = new QueryObject(gwls);
				qo.AddWhere(GenerWorkerListAttr.FID, id);
				qo.addOr();
				qo.AddWhere(GenerWorkerListAttr.WorkID, id);
				qo.DoQuery();

				DataTable dtGwls = gwls.ToDataTableField("WF_GenerWorkerList");
				ds.Tables.add(dtGwls);
			}

				///#endregion 如果流程没有完成,就把工作人员列表返回过去.

			String str = bp.tools.Json.ToJson(ds);
			 //DataType.WriteFile("c:\\GetFlowTrackJsonData_CCflow.txt", str);
			 return str;
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 获得最后一个人的审批意见
	 
	 @return 
	*/
	public final String SubFlowGuid_GenerLastOneCheckNote() throws Exception {
		String table = "ND" + Integer.parseInt(this.getFK_Flow()) + "Track";
		String sql = "SELECT Msg, WriteDB FROM " + table + " WHERE WorkID=" + this.getWorkID() + " AND ActionType=1 ORDER BY RDT DESC ";

		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		String info = dt.Rows.get(0).getValue(0).toString();
		if (info.contains("WorkCheck@") == true)
		{
			info = info.substring(info.indexOf("WorkCheck@") + 10);
		}

		Hashtable ht = new Hashtable();
		ht.put("Msg", info);
		ht.put("WriteDB", dt.Rows.get(0).getValue(1));

		return bp.tools.Json.ToJson(ht);

	}
	public final String Chart_Init() throws Exception {
		String fk_flow = this.getFK_Flow();
		long workid = this.getWorkID();
		long fid = this.getFID();

		DataSet ds = new DataSet();
		DataTable dt = null;
		String json = "";
		try
		{
			//获取流程信息
			String sql = "SELECT No \"No\", Name \"Name\", Paras \"Paras\", ChartType \"ChartType\" FROM WF_Flow WHERE No='" + fk_flow + "'";
			dt = DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "WF_Flow";
			ds.Tables.add(dt);

			//获取流程中的节点信息
			sql = "SELECT NodeID \"ID\", Name \"Name\", ICON \"Icon\", X \"X\", Y \"Y\", NodePosType \"NodePosType\",RunModel \"RunModel\",HisToNDs \"HisToNDs\",TodolistModel \"TodolistModel\" FROM WF_Node WHERE FK_Flow='" + fk_flow + "' ORDER BY Step";
			dt = DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "WF_Node";
			ds.Tables.add(dt);

			//获取流程中的标签信息
			sql = "SELECT MyPK \"MyPK\", Name \"Name\", X \"X\", Y \"Y\" FROM WF_LabNote WHERE FK_Flow='" + fk_flow + "'";
			dt = DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "WF_LabNote";
			ds.Tables.add(dt);

			//获取流程中的线段方向信息
			sql = "SELECT Node \"Node\", ToNode \"ToNode\", 0 as  \"DirType\", 0 as \"IsCanBack\" FROM WF_Direction WHERE FK_Flow='" + fk_flow + "'";
			dt = DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "WF_Direction";
			ds.Tables.add(dt);

			if (workid != 0)
			{
				//获取流程信息，added by liuxc,2016-10-26
				//sql =
				//    "SELECT wgwf.Starter,wgwf.StarterName,wgwf.RDT,wgwf.WFSta,wgwf.WFState FROM WF_GenerWorkFlow wgwf WHERE wgwf.WorkID = " +
				//    workid;                     
				sql = "SELECT wgwf.Starter as \"Starter\"," + "        wgwf.StarterName as \"StarterName\"," + "        wgwf.RDT as \"RDT\"," + "        wgwf.WFSta as \"WFSta\"," + "        se.Lab  as   \"WFStaText\"," + "        wgwf.WFState as \"WFState\"," + "        wgwf.FID as \"FID\"," + "        wgwf.PWorkID as \"PWorkID\"," + "        wgwf.PFlowNo as \"PFlowNo\"," + "        wgwf.PNodeID as \"PNodeID\"," + "        wgwf.FK_Flow as \"FK_Flow\"," + "        wgwf.FK_Node as \"FK_Node\"," + "        wgwf.Title as \"Title\"," + "        wgwf.WorkID as \"WorkID\"," + "        wgwf.NodeName as \"NodeName\"," + "        wf.Name  as   \"FlowName\"" + " FROM   WF_GenerWorkFlow wgwf" + "        INNER JOIN WF_Flow wf" + "             ON  wf.No=wgwf.FK_Flow" + "        INNER JOIN "+bp.sys.base.Glo.SysEnum()+" se" + "             ON  se.IntKey = wgwf.WFSta" + "             AND se.EnumKey = 'WFSta'" + " WHERE  wgwf.WorkID = %1$s" + "        OR  wgwf.FID = %1$s" + "        OR  wgwf.PWorkID = %1$s" + " ORDER BY" + "        wgwf.RDT DESC";

				dt = DBAccess.RunSQLReturnTable(String.format(sql, workid));
				dt.TableName = "FlowInfo";
				ds.Tables.add(dt);

				//获得流程状态.
				WFState wfState = WFState.forValue(Integer.parseInt(dt.Rows.get(0).getValue("WFState").toString())); // (WFState)int.Parse(dt.Rows.get(0).getValue("WFState"].ToString());

				String fk_Node = dt.Rows.get(0).getValue("FK_Node").toString();

				//把节点审核配置信息.
				NodeWorkCheck fwc = new NodeWorkCheck(fk_Node);
				ds.Tables.add(fwc.ToDataTableField("FrmWorkCheck"));


				//获取工作轨迹信息
				String trackTable = "ND" + Integer.parseInt(fk_flow) + "Track";
				sql = "SELECT FID \"FID\",NDFrom \"NDFrom\",NDFromT \"NDFromT\",NDTo  \"NDTo\", NDToT \"NDToT\", ActionType \"ActionType\",ActionTypeText \"ActionTypeText\",Msg \"Msg\",RDT \"RDT\",EmpFrom \"EmpFrom\",EmpFromT \"EmpFromT\", EmpToT \"EmpToT\",EmpTo \"EmpTo\" FROM " + trackTable + " WHERE WorkID=" + workid + (fid == 0 ? (" OR FID=" + workid) : (" OR WorkID=" + fid + " OR FID=" + fid)) + " ORDER BY RDT DESC";

				dt = DBAccess.RunSQLReturnTable(sql);
				DataTable newdt = new DataTable();
				newdt = dt;

				//判断轨迹数据中，最后一步是否是撤销或退回状态的，如果是，则删除最后2条数据
				if (dt.Rows.size() > 0)
				{
					int actionType = Integer.parseInt(dt.Rows.get(0).getValue("ActionType").toString());
					if (actionType== ActionType.Return.getValue() ||actionType==ActionType.UnSend.getValue())
					{
						if (dt.Rows.size() > 1)
						{
							dt.Rows.remove(1);
							dt.Rows.remove(0);
						}
						else
						{
							dt.Rows.remove(0);
						}

						newdt = dt;
					}
					else if (dt.Rows.size() > 1 && (Integer
							.parseInt(dt.Rows.get(1).getValue("ActionType").toString()) == ActionType.Return.getValue()
							|| Integer.parseInt(dt.Rows.get(1).getValue("ActionType").toString()) == ActionType.UnSend
							.getValue()))
					{
						//删除已发送的节点，
						if (dt.Rows.size() > 3)
						{
							dt.Rows.remove(1);
							dt.Rows.remove(1);
						}
						else
						{
							dt.Rows.remove(1);
						}

						String fk_node = "";
						if (dt.Rows.get(0).getValue("NDFrom").equals(dt.Rows.get(0).getValue("NDTo")))
						{
							fk_node = dt.Rows.get(0).getValue("NDFrom").toString();
						}
						if (DataType.IsNullOrEmpty(fk_node) == false)
						{
							//如果是跳转页面，则需要删除中间跳转的节点
							for (DataRow dr : dt.Rows)
							{
								if ((Integer.parseInt(dr.getValue("ACTIONTYPE").toString())==ActionType.Skip.getValue()) && dr.getValue("NDFrom").toString().equals(fk_node))
								{
									continue;
								}
								DataRow newdr = newdt.NewRow();
								newdr.ItemArray = dr.ItemArray;
								newdt.Rows.add(newdr);
							}
						}
						else
						{
							newdt = dt.copy();
						}
					}
					else
					{
						newdt = dt.copy();
					}
				}

				newdt.TableName = "Track";
				ds.Tables.add(newdt);

				//获取预先计算的节点处理人，以及处理时间,added by liuxc,2016-4-15
				sql = "SELECT wsa.FK_Node as \"FK_Node\",wsa.FK_Emp as \"FK_Emp\",wsa.EmpName as \"EmpName\",wsa.TimeLimit as \"TimeLimit\",wsa.TSpanHour as \"TSpanHour\",wsa.ADT as \"ADT\",wsa.SDT as \"SDT\" FROM WF_SelectAccper wsa WHERE wsa.WorkID = " + workid;
				dt = DBAccess.RunSQLReturnTable(sql);
				// dt.TableName = "POSSIBLE";
				dt.TableName = "Possible";
				ds.Tables.add(dt);

				//获取节点处理人数据，及处理/查看信息
				sql = "SELECT wgw.FK_Emp as \"FK_Emp\",wgw.FK_Node as \"FK_Node\",wgw.FK_EmpText as \"FK_EmpText\",wgw.RDT as \"RDT\",wgw.IsRead as \"IsRead\",wgw.IsPass as \"IsPass\" FROM WF_GenerWorkerlist wgw WHERE wgw.WorkID = " + workid + (fid == 0 ? (" OR FID=" + workid) : (" OR WorkID=" + fid + " OR FID=" + fid));
				dt = DBAccess.RunSQLReturnTable(sql);
				dt.TableName = "DISPOSE";
				ds.Tables.add(dt);


				//如果流程没有完成.
				if (wfState != WFState.Complete)
				{
					GenerWorkerLists gwls = new GenerWorkerLists();
					long id = this.getFID();
					if (id == 0)
					{
						id = this.getWorkID();
					}

					QueryObject qo = new QueryObject(gwls);
					qo.AddWhere(GenerWorkerListAttr.FID, id);
					qo.addOr();
					qo.AddWhere(GenerWorkerListAttr.WorkID, id);
					qo.addOrderBy(GenerWorkerListAttr.Idx);
					qo.DoQuery();


					DataTable dtGwls = gwls.ToDataTableField("WF_GenerWorkerList");
					ds.Tables.add(dtGwls);
				}

			}
			else
			{
				String trackTable = "ND" + Integer.parseInt(fk_flow) + "Track";
				sql = "SELECT NDFrom \"NDFrom\", NDTo \"NDTo\",ActionType \"ActionType\",ActionTypeText \"ActionTypeText\",Msg \"Msg\",RDT \"RDT\",EmpFrom \"EmpFrom\",EmpFromT \"EmpFromT\",EmpToT \"EmpToT\",EmpTo \"EmpTo\" FROM " + trackTable + " WHERE WorkID=0 ORDER BY RDT ASC";
				dt = DBAccess.RunSQLReturnTable(sql);
				dt.TableName = "TRACK";
				ds.Tables.add(dt);
			}

			//for (int i = 0; i < ds.Tables.size(); i++)
			//{
			//    dt = ds.Tables[i];
			//    dt.TableName = dt.TableName.ToUpper();
			//    for (int j = 0; j < dt.Columns.size(); j++)
			//    {
			//        dt.Columns.get(j).ColumnName = dt.Columns.get(j).ColumnName.ToUpper();
			//    }
			//}
			//获取子流程
			SubFlows subFlows = new SubFlows(this.getFK_Flow());
			ds.Tables.add(subFlows.ToDataTableField("WF_NodeSubFlow"));

			//获取发起的子流程
			GenerWorkFlows gwfs = new GenerWorkFlows();
			gwfs.Retrieve(GenerWorkFlowAttr.PWorkID, this.getWorkID(), "WorkID");
			ds.Tables.add(gwfs.ToDataTableField("WF_GenerWorkFlow"));
			String str = bp.tools.Json.ToJson(ds);
			//  DataType.WriteFile("c:\\GetFlowTrackJsonData_CCflow.txt", str);
			return str;
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 获取流程的JSON数据，以供显示工作轨迹/流程设计
	 
	 param fk_flow 流程编号
	 param workid 工作编号
	 param fid 父流程编号
	 @return 
	*/
	public final String GetFlowTrackJsonData() throws Exception {
		String fk_flow = this.getFK_Flow();
		long workid = this.getWorkID();
		long fid = this.getFID();


		DataSet ds = new DataSet();
		DataTable dt = null;
		try
		{
			//获取流程信息
			String sql = "SELECT No \"No\", Name \"Name\", Paras \"Paras\", ChartType \"ChartType\" FROM WF_Flow WHERE No='" + fk_flow + "'";
			dt = DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "WF_FLOW";
			ds.Tables.add(dt);

			//获取流程中的节点信息
			sql = "SELECT NodeID \"ID\", Name \"Name\", ICON \"Icon\", X \"X\", Y \"Y\", NodePosType \"NodePosType\", RunModel \"RunModel\",HisToNDs \"HisToNDs\",TodolistModel \"TodolistModel\" FROM WF_Node WHERE FK_Flow='" + fk_flow + "' ORDER BY Step";
			dt = DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "WF_NODE";
			ds.Tables.add(dt);

			//获取流程中的标签信息
			sql = "SELECT MyPK \"MyPK\", Name \"Name\", X \"X\", Y \"Y\" FROM WF_LabNote WHERE FK_Flow='" + fk_flow + "'";
			dt = DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "WF_LABNOTE";
			ds.Tables.add(dt);

			//获取流程中的线段方向信息
			sql = "SELECT Node \"Node\", ToNode \"ToNode\", 0 as  \"DirType\", 0 as \"IsCanBack\" FROM WF_Direction WHERE FK_Flow='" + fk_flow + "'";
			dt = DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "WF_DIRECTION";
			ds.Tables.add(dt);

			if (workid != 0)
			{
				//获取流程信息，added by liuxc,2016-10-26
				sql = "SELECT wgwf.Starter as \"Starter\"," + "        wgwf.StarterName as \"StarterName\"," + "        wgwf.RDT as \"RDT\"," + "        wgwf.WFSta as \"WFSta\"," + "        se.Lab  as   \"WFStaText\"," + "        wgwf.WFState as \"WFState\"," + "        wgwf.FID as \"FID\"," + "        wgwf.PWorkID as \"PWorkID\"," + "        wgwf.PFlowNo as \"PFlowNo\"," + "        wgwf.PNodeID as \"PNodeID\"," + "        wgwf.FK_Flow as \"FK_Flow\"," + "        wgwf.FK_Node as \"FK_Node\"," + "        wgwf.Title as \"Title\"," + "        wgwf.WorkID as \"WorkID\"," + "        wgwf.NodeName as \"NodeName\"," + "        wf.Name  as   \"FlowName\"" + " FROM   WF_GenerWorkFlow wgwf" + "        INNER JOIN WF_Flow wf" + "             ON  wf.No=wgwf.FK_Flow" + "        INNER JOIN "+bp.sys.base.Glo.SysEnum()+" se" + "             ON  se.IntKey = wgwf.WFSta" + "             AND se.EnumKey = 'WFSta'" + " WHERE  wgwf.WorkID = %1$s" + "        OR  wgwf.FID = %1$s" + "        OR  wgwf.PWorkID = %1$s" + " ORDER BY" + "        wgwf.RDT DESC";

				dt = DBAccess.RunSQLReturnTable(String.format(sql, workid));
				dt.TableName = "FLOWINFO";
				ds.Tables.add(dt);

				//获取工作轨迹信息
				String trackTable = "ND" + Integer.parseInt(fk_flow) + "Track";
				sql = "SELECT NDFrom \"NDFrom\",NDFromT \"NDFromT\",NDTo  \"NDTo\", NDToT \"NDToT\", ActionType \"ActionType\",ActionTypeText \"ActionTypeText\",Msg \"Msg\",RDT \"RDT\",EmpFrom \"EmpFrom\",EmpFromT \"EmpFromT\", EmpToT \"EmpToT\",EmpTo \"EmpTo\" FROM " + trackTable + " WHERE WorkID=" + workid + (fid == 0 ? (" OR FID=" + workid) : (" OR WorkID=" + fid + " OR FID=" + fid)) + " ORDER BY RDT ASC";

				dt = DBAccess.RunSQLReturnTable(sql);

				//判断轨迹数据中，最后一步是否是撤销或退回状态的，如果是，则删除最后2条数据
				if (dt.Rows.size() > 0)
				{
					int actionType = Integer.parseInt(dt.Rows.get(0).getValue("ACTIONTYPE").toString());
					if (actionType== ActionType.Return.getValue() || actionType== ActionType.UnSend.getValue())
					{
						if (dt.Rows.size() > 1)
						{
							dt.Rows.remove(0);
							dt.Rows.remove(0);
						}
						else
						{
							dt.Rows.remove(0);
						}
					}
				}

				dt.TableName = "TRACK";
				ds.Tables.add(dt);

				//获取预先计算的节点处理人，以及处理时间,added by liuxc,2016-4-15
				sql = "SELECT wsa.FK_Node as \"FK_Node\",wsa.FK_Emp as \"FK_Emp\",wsa.EmpName as \"EmpName\",wsa.TimeLimit as \"TimeLimit\",wsa.TSpanHour as \"TSpanHour\",wsa.ADT as \"ADT\",wsa.SDT as \"SDT\" FROM WF_SelectAccper wsa WHERE wsa.WorkID = " + workid;
				dt = DBAccess.RunSQLReturnTable(sql);
				dt.TableName = "POSSIBLE";
				ds.Tables.add(dt);

				//获取节点处理人数据，及处理/查看信息
				sql = "SELECT wgw.FK_Emp as \"FK_Emp\",wgw.FK_Node as \"FK_Node\",wgw.FK_EmpText as \"FK_EmpText\",wgw.RDT as \"RDT\",wgw.IsRead as \"IsRead\",wgw.IsPass as \"IsPass\" FROM WF_GenerWorkerlist wgw WHERE wgw.WorkID = " + workid + (fid == 0 ? (" OR FID=" + workid) : (" OR WorkID=" + fid + " OR FID=" + fid));
				dt = DBAccess.RunSQLReturnTable(sql);
				dt.TableName = "DISPOSE";
				ds.Tables.add(dt);
			}
			else
			{
				String trackTable = "ND" + Integer.parseInt(fk_flow) + "Track";
				sql = "SELECT NDFrom \"NDFrom\", NDTo \"NDTo\",ActionType \"ActionType\",ActionTypeText \"ActionTypeText\",Msg \"Msg\",RDT \"RDT\",EmpFrom \"EmpFrom\",EmpFromT \"EmpFromT\",EmpToT \"EmpToT\",EmpTo \"EmpTo\" FROM " + trackTable + " WHERE WorkID=0 ORDER BY RDT ASC";
				dt = DBAccess.RunSQLReturnTable(sql);
				dt.TableName = "TRACK";
				ds.Tables.add(dt);
			}

			//for (int i = 0; i < ds.Tables.size(); i++)
			//{
			//    dt = ds.Tables[i];
			//    dt.TableName = dt.TableName.ToUpper();
			//    for (int j = 0; j < dt.Columns.size(); j++)
			//    {
			//        dt.Columns.get(j).ColumnName = dt.Columns.get(j).ColumnName.ToUpper();
			//    }
			//}

			String str = bp.tools.Json.ToJson(ds);
			//  DataType.WriteFile("c:\\GetFlowTrackJsonData_CCflow.txt", str);
			return str;
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 获得发起的BBS评论.
	 @return 
	*/
	public final String FlowBBSList() throws Exception {
		bp.ccbill.Tracks tracks = new bp.ccbill.Tracks();
		QueryObject qo = new bp.en.QueryObject(tracks);
		qo.AddWhere(TrackAttr.ActionType, bp.ccbill.FrmActionType.BBS);
		qo.addAnd();
		qo.addLeftBracket();

		if (this.getFID() != 0)
		{
			qo.AddWhere(TrackAttr.WorkID, this.getFID());
			qo.addOr();
			qo.AddWhere(TrackAttr.FID, this.getFID());
		}
		else
		{
			qo.AddWhere(TrackAttr.WorkID, this.getWorkID());

			if (this.getWorkID() != 0)
			{
				qo.addOr();
				qo.AddWhere(TrackAttr.FID, this.getWorkID());
			}
		}
		qo.addRightBracket();
		qo.addOrderBy(TrackAttr.RDT);
		qo.DoQuery();
		//转化成json
		return bp.tools.Json.ToJson(tracks.ToDataTableField("dt"));
	}

	/** 查看某一用户的评论.
	*/
	public final String FlowBBS_Check() throws Exception {
		Paras pss = new Paras();
		pss.SQL = "SELECT * FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track WHERE ActionType=" + bp.difference.SystemConfig.getAppCenterDBVarStr() + "ActionType AND WorkID=" + bp.difference.SystemConfig.getAppCenterDBVarStr() + "WorkID AND  EMPFROMT='" + this.getUserName() + "'";
		pss.Add("ActionType", ActionType.FlowBBS.getValue());
		pss.Add("WorkID", this.getWorkID());

		return bp.tools.Json.ToJson(DBAccess.RunSQLReturnTable(pss));
	}
	/** 
	 提交评论.
	 
	 @return 
	*/
	public final String FlowBBS_Save() throws Exception {
		String msg = this.GetValFromFrmByKey("FlowBBS_Doc");
		String fk_mapData = this.GetRequestVal("FK_MapData");
		Node nd = new Node(this.getFK_Node());
		if (DataType.IsNullOrEmpty(fk_mapData) == true)
		{
			fk_mapData = nd.getNodeFrmID();
		}
		MapData mapData = new MapData(fk_mapData);
		Dev2Interface.Track_WriteBBS(fk_mapData, mapData.getName(), this.getWorkID(), msg, this.getFID(), this.getFK_Flow(), null, this.getFK_Node(), nd.getName());
		return "评论信息保存成功";
	}

	/** 
	 回复评论.
	 
	 @return 
	*/
	public final String FlowBBS_Replay() throws Exception {
		SMS sms = new SMS();
		sms.RetrieveByAttr(SMSAttr.MyPK, getMyPK());
		sms.setMyPK(DBAccess.GenerGUID(0, null, null));
		sms.setRDT(DataType.getCurrentDateTime());
		sms.setSendToEmpNo(this.getUserName());
		sms.setSender(WebUser.getNo());
		sms.setTitle(this.getTitle());
		sms.setDocOfEmail(this.getMsg());
		sms.Insert();
		return null;
	}
	/** 
	 统计评论条数.
	 
	 @return 
	*/
	public final String FlowBBS_Count() throws Exception {
		Paras ps = new Paras();
		ps.SQL = "SELECT COUNT(ActionType) FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track WHERE ActionType=" + bp.difference.SystemConfig.getAppCenterDBVarStr() + "ActionType AND WorkID=" + bp.difference.SystemConfig.getAppCenterDBVarStr() + "WorkID";
		ps.Add("ActionType", ActionType.FlowBBS.getValue());
		ps.Add("WorkID", this.getWorkID());
		String count = String.valueOf(DBAccess.RunSQLReturnValInt(ps));
		return count;
	}


}