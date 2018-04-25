package BP.WF.HttpHandler;

import java.util.Enumeration;

import net.sf.json.JSONArray;

import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;

import BP.DA.AtPara;
import BP.DA.DBAccess;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.DA.Log;
import BP.DA.Paras;
import BP.En.Attr;
import BP.En.Attrs;
import BP.En.QueryObject;
import BP.Port.Emp;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapData;
import BP.Sys.SysEnum;
import BP.Sys.SysEnumAttr;
import BP.Sys.SysEnums;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
import BP.WF.ActionType;
import BP.WF.BatchRole;
import BP.WF.CCRole;
import BP.WF.CancelRole;
import BP.WF.Dev2Interface;
import BP.WF.DotNetToJavaStringHelper;
import BP.WF.Flow;
import BP.WF.FlowAppType;
import BP.WF.GenerWorkFlow;
import BP.WF.GenerWorkerListAttr;
import BP.WF.GenerWorkerLists;
import BP.WF.GetTask;
import BP.WF.Glo;
import BP.WF.Node;
import BP.WF.NodeFormType;
import BP.WF.Nodes;
import BP.WF.PrintDocEnable;
import BP.WF.SMS;
import BP.WF.SMSAttr;
import BP.WF.SMSMsgType;
import BP.WF.SendReturnObjs;
import BP.WF.TrackAttr;
import BP.WF.WFSta;
import BP.WF.WFState;
import BP.WF.Work;
import BP.WF.WorkFlow;
import BP.WF.Data.BillAttr;
import BP.WF.Data.Bills;
import BP.WF.HttpHandler.Base.WebContralBase;
import BP.WF.Template.BtnLab;
import BP.WF.Template.CondModel;
import BP.WF.Template.FlowSort;
import BP.WF.Template.FlowSorts;
import BP.WF.Template.FrmWorkCheck;
import BP.WF.Template.FrmWorkCheckSta;
import BP.WF.Template.NodeToolbar;
import BP.WF.Template.NodeToolbarAttr;
import BP.WF.Template.NodeToolbars;
import BP.WF.Template.Selector;
import BP.WF.Template.ShowWhere;
import BP.WF.Template.TurnTo;
import BP.WF.Template.TurnTos;
import BP.WF.Template.WebOfficeWorkModel;
import BP.WF.XML.OneWorkXml;
import BP.WF.XML.OneWorkXmls;
import BP.Web.WebUser;
import BP.XML.XmlEn;
import cn.jflow.common.model.AjaxJson;

public class WF_WorkOpt_OneWork extends WebContralBase {
	/**
	 * 初始化函数
	 * 
	 * @param mycontext
	 */
	public WF_WorkOpt_OneWork(HttpContext mycontext) {
		this.context = mycontext;
	}

	public WF_WorkOpt_OneWork() {
	}
	
	

	
	public final String getMsg()
	{
		String str = this.GetRequestVal("TB_Msg");
		if (str == null || str.equals("") || str.equals("null"))
		{
			return null;
		}
		return str;
	}
	public final String getUserName()
	{
		String str = this.GetRequestVal("UserName");
		if (str == null || str.equals("") || str.equals("null"))
		{
			return null;
		}
		return str;
	}
	public final String getTitle()
	{
		String str = this.GetRequestVal("Title");
		if (str == null || str.equals("") || str.equals("null"))
		{
			return null;
		}
		return str;
	}
		///#endregion 属性.

	//删除评论.
	public String FlowBBS_Delete() throws Exception{
		return BP.WF.Dev2Interface.Flow_BBSDelete(this.getFK_Flow(), this.getMyPK(), WebUser.getNo());
	}
	
	public String OP_Takeback(){
		return "";
	}
	public String OP_UnSend() throws Exception{
		// 转化成编号.
		return BP.WF.Dev2Interface.Flow_DoUnSend(this.getFK_Flow(), this.getWorkID());
	}
	@Override
	protected String DoDefaultMethod()
	{
		return "err@没有判断的执行类型：" + this.getDoType();
	}

	public String OP_ComeBack() throws Exception
	{
		WorkFlow wf3 = new WorkFlow(this.getFK_Flow(), this.getWorkID());
		wf3.DoComeBackWorkFlow("无");
		return "流程已经被重新启用.";
	}

	public String OP_UnHungUp() throws Exception
	{
		WorkFlow wf2 = new WorkFlow(this.getFK_Flow(), this.getWorkID());
		//  wf2.DoUnHungUp();
		return "流程已经被解除挂起.";
	}

	public String OP_HungUp() throws Exception
	{
		WorkFlow wf1 = new WorkFlow(this.getFK_Flow(), this.getWorkID());
		//wf1.DoHungUp()
		return "流程已经被挂起.";
	}

	public String OP_DelFlow() throws Exception
	{
		WorkFlow wf = new WorkFlow(this.getFK_Flow(), this.getWorkID());
		wf.DoDeleteWorkFlowByReal(true);
		return "流程已经被删除！";
	}

	/** 
	 获取可操作状态信息
	 
	 @return 
	 * @throws Exception 
	*/
	public String OP_GetStatus() throws Exception
	{
		int wfState = BP.DA.DBAccess.RunSQLReturnValInt("SELECT WFState FROM WF_GenerWorkFlow WHERE WorkID=" + this.getWorkID(), 1);
		WFState wfstateEnum = WFState.forValue(wfState);
		String json = "{";
		boolean isCan;

		switch (wfstateEnum)
		{
			case Runing: // 运行时
				//删除流程.
				isCan = BP.WF.Dev2Interface.Flow_IsCanDeleteFlowInstance(this.getFK_Flow(), this.getWorkID(), WebUser.getNo());
				json += "\"CanFlowOverByCoercion\":" + (new Boolean(isCan)).toString().toLowerCase() + ",";

				//取回审批
				isCan = false;
				String para = "";
				GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
				String sql = "SELECT NodeID FROM WF_Node WHERE CheckNodes LIKE '%" + gwf.getFK_Node() + "%'";
				int myNode = DBAccess.RunSQLReturnValInt(sql, 0);

				if (myNode != 0)
				{
					GetTask gt = new GetTask(myNode);
					if (gt.Can_I_Do_It())
					{
						isCan = true;
						para = "\"TackBackFromNode\": " + gwf.getFK_Node() + ",\"TackBackToNode\":" + myNode + ",";
					}
				}

				json += "\"CanTackBack\":" + (new Boolean(isCan)).toString().toLowerCase() + "," + para;

				//催办
				json += "\"CanHurry\":false,"; //原逻辑，不能催办

				//撤销发送
				GenerWorkerLists workerlists = new GenerWorkerLists();
				QueryObject info = new QueryObject(workerlists);
				info.AddWhere(GenerWorkerListAttr.FK_Emp, WebUser.getNo());
				info.addAnd();
				info.AddWhere(GenerWorkerListAttr.IsPass, "1");
				info.addAnd();
				info.AddWhere(GenerWorkerListAttr.IsEnable, "1");
				info.addAnd();
				info.AddWhere(GenerWorkerListAttr.WorkID, this.getWorkID());
				isCan = info.DoQuery() > 0;
				json += "\"CanUnSend\":" + (new Boolean(isCan)).toString().toLowerCase();
				break;
			case Complete: // 完成.
			case Delete: // 逻辑删除..
				//恢复使用流程
				isCan = WebUser.getNo().equals("admin");
				json += "\"CanRollBack\":" + (new Boolean(isCan)).toString().toLowerCase();
				break;
			case HungUp: // 挂起.
				//撤销挂起
				isCan = BP.WF.Dev2Interface.Flow_IsCanDoCurrentWork(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), WebUser.getNo());
				json += "\"CanUnHungUp\":" + (new Boolean(isCan)).toString().toLowerCase();
				break;
			default:
				break;
		}

		return json + "}";
	}

	/** 
	 获取附件列表及单据列表
	 
	 @return 
	 * @throws Exception 
	*/
	public String GetAthsAndBills() throws Exception
	{
		String sql = "";
		String json = "{\"Aths\":";

		if (this.getFK_Node() == 0)
		{
			sql = "SELECT fadb.*,wn.Name NodeName FROM Sys_FrmAttachmentDB fadb INNER JOIN WF_Node wn ON wn.NodeID = fadb.NodeID WHERE fadb.FK_FrmAttachment IN (SELECT MyPK FROM Sys_FrmAttachment WHERE  " + BP.WF.Glo.MapDataLikeKey(this.getFK_Flow(), "FK_MapData") + "  AND IsUpload=1) AND fadb.RefPKVal='" + this.getWorkID() + "' ORDER BY fadb.RDT";
		}
		else
		{
			sql = "SELECT fadb.*,wn.Name NodeName FROM Sys_FrmAttachmentDB fadb INNER JOIN WF_Node wn ON wn.NodeID = fadb.NodeID WHERE fadb.FK_FrmAttachment IN (SELECT MyPK FROM Sys_FrmAttachment WHERE  FK_MapData='ND" + this.getFK_Node() + "' ) AND fadb.RefPKVal='" + this.getWorkID() + "' ORDER BY fadb.RDT";
		}

		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		for (DataColumn col : dt.Columns)
		{
			col.ColumnName = col.ColumnName.toUpperCase();
		}

		json += BP.Tools.Json.ToJson(dt) + ",\"Bills\":";

		Bills bills = new Bills();
		bills.Retrieve(BillAttr.WorkID, this.getWorkID());

		json += bills.ToJson() + ",\"AppPath\":\"" + BP.WF.Glo.getCCFlowAppPath()+ "\"}";

		return json;
	}
	/** 
	 获取OneWork页面的tabs集合
	 
	 @return 
	*/
	public String OneWork_GetTabs2018()
	{
		String re = "[";
		OneWorkXmls xmls = new OneWorkXmls();
		xmls.RetrieveAll();		
		

		for (OneWorkXml item : xmls.ToJavaListXmlEnss())
		{
			String url = "";
			url = String.format("%1$s?FK_Node=%2$s&WorkID=%3$s&FK_Flow=%4$s&FID=%5$s", item.getURL(), this.getFK_Node(), this.getWorkID(), this.getFK_Flow(), this.getFID());

			String strx = String.format("\"No\":\"%1$s\",\"Name\":\"%2$s\", \"Url\":\"%3$s\"", item.getNo(), item.getName(), url);
			re += "{" + strx + "},";
		}

		return DotNetToJavaStringHelper.trimEnd(re, ',') + "]";
	}
	public String OneWork_GetTabs()
	{
		String re = "[";
		OneWorkXmls xmls = new OneWorkXmls();
		xmls.RetrieveAll();		
		
		
		//OneWorkXml xml=new OneWorkXml();
		//xml.setNo("xxx");
		
		

		for (OneWorkXml item : xmls.ToJavaListXmlEnss())
		{
			String url = "";
			url = String.format("%1$s?FK_Node=%2$s&WorkID=%3$s&FK_Flow=%4$s&FID=%5$s", item.getURL(), this.getFK_Node(), this.getWorkID(), this.getFK_Flow(), this.getFID());

			String strx = String.format("\"No\":\"%1$s\",\"Name\":\"%2$s\", \"Url\":\"%3$s\"", item.getNo(), item.getName(), url);
			re += "{" + strx + "},";
		}

		return DotNetToJavaStringHelper.trimEnd(re, ',') + "]";
	}
	/** 
	 获取流程的JSON数据，以供显示工作轨迹/流程设计
	 
	 @param fk_flow 流程编号
	 @param workid 工作编号
	 @param fid 父流程编号
	 @return 
	*/
	public String GetFlowTrackJsonData()
	{
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
			dt.TableName = "WF_FLOW";
			ds.Tables.add(dt);

			//获取流程中的节点信息
		    sql = "SELECT NodeID \"ID\", Name \"Name\", ICON \"Icon\", X \"X\", Y \"Y\", NodePosType \"NodePosType\",RunModel \"RunModel\",HisToNDs \"HisToNDs\",TodolistModel \"TodolistModel\" FROM WF_Node WHERE FK_Flow='" +
	                    fk_flow + "' ORDER BY Step";			
			dt = DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "WF_NODE";
			ds.Tables.add(dt);

			//获取流程中的标签信息
            sql = "SELECT MyPK \"MyPK\", Name \"Name\", X \"X\", Y \"Y\" FROM WF_LabNote WHERE FK_Flow='" + fk_flow + "'";
			dt = DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "WF_LABNOTE";
			ds.Tables.add(dt);

			//获取流程中的线段方向信息
            sql = "SELECT Node \"Node\", ToNode \"ToNode\",DirType \"DirType\", IsCanBack \"IsCanBack\",Dots \"Dots\" FROM WF_Direction WHERE FK_Flow='" + fk_flow + "'";
 			dt = DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "WF_DIRECTION";
			ds.Tables.add(dt);


			if (workid != 0)
			{
				//获取流程信息，added by liuxc,2016-10-26
				//sql =
				//    "SELECT wgwf.Starter,wgwf.StarterName,wgwf.RDT,wgwf.WFSta,wgwf.WFState FROM WF_GenerWorkFlow wgwf WHERE wgwf.WorkID = " +
				//    workid;

				  sql = "SELECT wgwf.Starter as \"Starter\","
                          + "        wgwf.StarterName as \"StarterName\","
                          + "        wgwf.RDT as \"RDT\","
                          + "        wgwf.WFSta as \"WFSta\","
                          + "        se.Lab  as   \"WFStaText\","
                          + "        wgwf.WFState as \"WFState\","
                          + "        wgwf.FID as \"FID\","
                          + "        wgwf.PWorkID as \"PWorkID\","
                          + "        wgwf.PFlowNo as \"PFlowNo\","
                          + "        wgwf.PNodeID as \"PNodeID\","
                          + "        wgwf.FK_Flow as \"FK_Flow\","
                          + "        wgwf.FK_Node as \"FK_Node\","
                          + "        wgwf.Title as \"Title\","
                          + "        wgwf.WorkID as \"WorkID\","
                          + "        wgwf.NodeName as \"NodeName\","
                          + "        wf.Name  as   \"FlowName\""
                          + " FROM   WF_GenerWorkFlow wgwf"
                          + "        INNER JOIN WF_Flow wf"
                          + "             ON  wf.No = wgwf.FK_Flow"
                          + "        INNER JOIN Sys_Enum se"
                          + "             ON  se.IntKey = wgwf.WFSta"
                          + "             AND se.EnumKey = 'WFSta'"
                          + " WHERE  wgwf.WorkID = "+workid
                          + "        OR  wgwf.FID = "+workid
                          + "        OR  wgwf.PWorkID = "+workid
                          + " ORDER BY"
                          + "        wgwf.RDT DESC";
				  
				dt = DBAccess.RunSQLReturnTable(sql);
				dt.TableName = "FLOWINFO";
				ds.Tables.add(dt);

				//获取工作轨迹信息
				Object trackTable = "ND" + Integer.parseInt(fk_flow) + "Track";
				
				sql = "SELECT NDFrom \"NDFrom\",NDFromT \"NDFromT\",NDTo  \"NDTo\", NDToT \"NDToT\", ActionType \"ActionType\",ActionTypeText \"ActionTypeText\",Msg \"Msg\",RDT \"RDT\",EmpFrom \"EmpFrom\",EmpFromT \"EmpFromT\", EmpToT \"EmpToT\",EmpTo \"EmpTo\" FROM " + trackTable +
                        " WHERE WorkID=" +
                        workid + (fid == 0 ? (" OR FID=" + workid) : (" OR WorkID=" + fid + " OR FID=" + fid)) + " ORDER BY RDT ASC";

				 
				dt = DBAccess.RunSQLReturnTable(sql);

				//判断轨迹数据中，最后一步是否是撤销或退回状态的，如果是，则删除最后2条数据
				if (dt.Rows.size() > 0)
				{
					Object a = dt.Rows.get(0).getValue("ACTIONTYPE");
					if(!"".equals(a) && a!=null){
						if (Integer.parseInt(dt.Rows.get(0).getValue("ACTIONTYPE").toString())==ActionType.Return.getValue() || Integer.parseInt(dt.Rows.get(0).getValue("ACTIONTYPE").toString()) == ActionType.UnSend.getValue())
						{
							if (dt.Rows.size() > 1)
							{
								dt.Rows.remove(0);//RemoveAt(0);
								dt.Rows.remove(0);//RemoveAt(0);
							}
							else
							{
								dt.Rows.remove(0);//RemoveAt(0);
							}
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
				sql = "SELECT wgw.FK_Emp as \"FK_Emp\",wgw.FK_Node as \"FK_Node\",wgw.FK_EmpText as \"FK_EmpText\",wgw.RDT as \"RDT\",wgw.IsRead as\"IsRead\",wgw.IsPass as \"IsPass\" FROM WF_GenerWorkerlist wgw WHERE wgw.WorkID = " + workid + (fid == 0 ? (" OR FID=" + workid) : (" OR WorkID=" + fid + " OR FID=" + fid));
				dt = DBAccess.RunSQLReturnTable(sql);
				dt.TableName = "DISPOSE";
				ds.Tables.add(dt);
			}
			else
			{
				String trackTable = "ND" + Integer.parseInt(fk_flow) + "Track";
				
				sql = "SELECT NDFrom \"NDFrom\", NDTo \"NDTo\",ActionType \"ActionType\",ActionTypeText \"ActionTypeText\",Msg \"Msg\",RDT \"RDT\",EmpFrom \"EmpFrom\",EmpFromT \"EmpFromT\",EmpToT \"EmpToT\",EmpTo \"EmpTo\" FROM " + trackTable +
                          " WHERE WorkID=0 ORDER BY RDT ASC";
				  
				dt = DBAccess.RunSQLReturnTable(sql);
				dt.TableName = "TRACK";
				ds.Tables.add(dt);
			}

			json = BP.Tools.Json.ToJson(ds);			
		}
		catch (RuntimeException ex)
		{
			json = "err@" + ex.getMessage();
		}

		  
		//json=DataType.ReadTextFile("c:\\GetFlowTrackJsonData_CCflow.txt");		
		//DataType.WriteFile("c:\\GetFlowTrackJsonData_jflow.txt", json);
		 
		return json;
	}
	/** 
	 获得发起的BBS评论.
	 
	 @return 
	*/
	public final String FlowBBSList()
	{
		Paras ps = new Paras();
		ps.SQL = "SELECT * FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track WHERE ActionType=" + BP.Sys.SystemConfig.getAppCenterDBVarStr() + "ActionType AND WorkID=" + BP.Sys.SystemConfig.getAppCenterDBVarStr() + "WorkID";
		ps.Add("ActionType", BP.WF.ActionType.FlowBBS.getValue());
		ps.Add("WorkID", this.getWorkID());

		//转化成json
		return BP.Tools.Json.ToJson(BP.DA.DBAccess.RunSQLReturnTable(ps));
	}

	public final String FlowBBSUser() throws Exception
	{
		String name = "";
		name = BP.Web.WebUser.getNo();
		return name;

	}
	public final String FlowBBSUserName()
	{
		String name = "";
		name = BP.Web.WebUser.getName();
		return name;
	}

	public final String FlowBBSDept()
	{
		Paras ps = new Paras();
		ps.SQL = "select a.name from port_dept a INNER join port_emp b on b.FK_Dept=a.no and b.No='" + this.getUserName() + "'";
		return BP.Tools.Json.ToJson(BP.DA.DBAccess.RunSQLReturnString(ps));
	}

	/** 查看某一用户的评论.
	*/
	public final String FlowBBS_Check()
	{
		Paras pss = new Paras();
		pss.SQL = "SELECT * FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track WHERE ActionType=" + BP.Sys.SystemConfig.getAppCenterDBVarStr() + "ActionType AND WorkID=" + BP.Sys.SystemConfig.getAppCenterDBVarStr() + "WorkID AND  EMPFROMT='" + this.getUserName() + "'";
		pss.Add("ActionType", BP.WF.ActionType.FlowBBS.getValue());
		pss.Add("WorkID", this.getWorkID());
		return BP.Tools.Json.ToJson(BP.DA.DBAccess.RunSQLReturnTable(pss));

	}
	/** 
	 提交评论.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String FlowBBS_Save() throws Exception
	{
		String msg = this.GetValFromFrmByKey("TB_Msg");
		String mypk = BP.WF.Dev2Interface.Flow_BBSAdd(this.getFK_Flow(), this.getWorkID(), this.getFID(), msg, WebUser.getNo(), WebUser.getName());
		Paras ps = new Paras();
		ps.SQL = "SELECT * FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track WHERE MyPK=" + BP.Sys.SystemConfig.getAppCenterDBVarStr() + "MyPK";
		ps.Add("MyPK", mypk);
		return BP.Tools.Json.ToJson(BP.DA.DBAccess.RunSQLReturnTable(ps));
	}

	/** 
	 回复评论.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String FlowBBS_Replay() throws Exception
	{
		SMS sms = new SMS();
		sms.RetrieveByAttr(SMSAttr.MyPK, this.getMyPK());
		sms.setMyPK(DBAccess.GenerGUID());
		sms.setRDT(DataType.getCurrentDataTime());
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
	public final String FlowBBS_Count()
	{

		Paras ps = new Paras();
		ps.SQL = "SELECT COUNT(ActionType) FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track WHERE ActionType=" + BP.Sys.SystemConfig.getAppCenterDBVarStr() + "ActionType AND WorkID=" + BP.Sys.SystemConfig.getAppCenterDBVarStr() + "WorkID";
		ps.Add("ActionType", BP.WF.ActionType.FlowBBS.getValue());
		ps.Add("WorkID", this.getWorkID());
		String count = BP.DA.DBAccess.RunSQLReturnValInt(ps) + "";
		return count;
	}
 
	
	 /** 
	 时间轴
	 
	 @return 
	 * @throws Exception 
*/
	public final String TimeBase_Init() throws Exception
	{
		DataSet ds = new DataSet();

		//获取track.
		DataTable dt = BP.WF.Dev2Interface.DB_GenerTrackTable(this.getFK_Flow(), this.getWorkID(), this.getFID());
		ds.Tables.add(dt);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region  父子流程数据存储到这里.

		java.util.Hashtable ht = new java.util.Hashtable();
		for (DataRow dr : dt.Rows)
		{
			ActionType at =  ActionType.forValue( Integer.parseInt( dr.getValue(TrackAttr.ActionType ).toString() ));

			String tag = dr.getValue(TrackAttr.Tag).toString(); //标识.
			String mypk = dr.getValue(TrackAttr.MyPK).toString(); //主键.

			String msg = "";
			if (at == ActionType.CallChildenFlow)
			{
				//被调用父流程吊起。
				if (DotNetToJavaStringHelper.isNullOrEmpty(tag) == false)
				{
					AtPara ap = new AtPara(tag);
					GenerWorkFlow mygwf = new GenerWorkFlow();
					mygwf.setWorkID( ap.GetValInt64ByKey("PWorkID"));
					if (mygwf.RetrieveFromDBSources() == 1)
					{
						msg = "<p>操作员:{" + dr.getValue(TrackAttr.EmpFromT).toString() + "}在当前节点上，被父流程{" + mygwf.getFlowName() + "},<a target=b" + ap.GetValStrByKey("PWorkID") + " href='Track.aspx?WorkID=" + ap.GetValStrByKey("PWorkID") + "&FK_Flow=" + ap.GetValStrByKey("PFlowNo") + "' >" + msg + "</a></p>";
					}
					else
					{
						msg = "<p>操作员:{" + dr.getValue(TrackAttr.EmpFromT).toString() + "}在当前节点上，被父流程调用{" + mygwf.getFlowName() + "}，但是该流程被删除了.</p>" + tag;
					}

					msg = "<a target=b" + ap.GetValStrByKey("PWorkID") + " href='Track.aspx?WorkID=" + ap.GetValStrByKey("PWorkID") + "&FK_Flow=" + ap.GetValStrByKey("PFlowNo") + "' >" + msg + "</a>";
				}

				//放入到ht里面.
				ht.put(mypk, msg);
			}

			if (at == ActionType.StartChildenFlow)
			{
				if (DotNetToJavaStringHelper.isNullOrEmpty(tag) == false)
				{
					if (tag.contains("Sub"))
					{
						tag = tag.replace("Sub", "C");
					}

					AtPara ap = new AtPara(tag);
					GenerWorkFlow mygwf = new GenerWorkFlow();
					mygwf.setWorkID( ap.GetValInt64ByKey("CWorkID"));
					if (mygwf.RetrieveFromDBSources() == 1)
					{
						msg = "<p>操作员:{" + dr.getValue(TrackAttr.EmpFromT).toString() + "}在当前节点上调用了子流程{" + mygwf.getFlowName() + "}, <a target=b" + ap.GetValStrByKey("CWorkID") + " href='Track.aspx?WorkID=" + ap.GetValStrByKey("CWorkID") + "&FK_Flow=" + ap.GetValStrByKey("CFlowNo") + "' >" + msg + "</a></p>";
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

		//获取 WF_GenerWorkFlow
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID( this.getWorkID()) ;
		gwf.RetrieveFromDBSources();
		ds.Tables.add(gwf.ToDataTableField("WF_GenerWorkFlow"));

		if (gwf.getWFState() != WFState.Complete)
		{
			GenerWorkerLists gwls = new GenerWorkerLists();
			gwls.Retrieve(GenerWorkerListAttr.WorkID, this.getWorkID());

			ds.Tables.add(gwls.ToDataTableField("WF_GenerWorkerList"));
		}

		//返回结果.
		return BP.Tools.Json.ToJson(ds);
	}
 
	public final String TimeBase_OpenFrm() throws Exception
	{
		WF en = new WF();
		return en.Runing_OpenFrm();
	}
	
	public final String Runing_OpenFrm() throws Exception
    {
        BP.WF.HttpHandler.WF wf = new WF();
        return wf.Runing_OpenFrm();
    }
}
