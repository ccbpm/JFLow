package bp.wf.httphandler;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.en.QueryObject;
import bp.sys.*;
import bp.web.*;
import bp.wf.template.*;
import bp.*;
import bp.wf.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/** 
 页面功能实体
*/
public class WF_WorkOpt_Batch extends bp.difference.handler.WebContralBase
{
	/** 
	 构造函数
	*/
	public WF_WorkOpt_Batch() throws Exception {
	}


		///#region  界面 .
	public final String WorkCheckModel_Init() throws Exception {
		DataSet ds = new DataSet();

		String FK_Node = GetRequestVal("FK_Node");

		//获取节点信息
		Node nd = new Node(this.getFK_Node());
		Flow fl = nd.getHisFlow();
		ds.Tables.add(nd.ToDataTableField("WF_Node"));

		String sql = "";

		if (nd.getIsSubThread() == true)
		{
			sql = "SELECT a.*, b.Starter,b.StarterName,b.ADT,b.WorkID FROM " + fl.getPTable() + " a , WF_EmpWorks b WHERE a.OID=B.FID AND b.WFState Not IN (7) AND b.FK_Node=" + nd.getNodeID() + " AND b.FK_Emp='" + WebUser.getNo() + "'";
		}
		else
		{
			sql = "SELECT a.*, b.Starter,b.StarterName,b.ADT,b.WorkID FROM " + fl.getPTable() + " a , WF_EmpWorks b WHERE a.OID=B.WorkID AND b.WFState Not IN (7) AND b.FK_Node=" + nd.getNodeID() + " AND b.FK_Emp='" + WebUser.getNo() + "'";
		}

		//获取待审批的流程信息集合
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		//获取已经有审批信息的集合
		String trackSql="select c.WorkID,c.msg from nd1track c where c.EmpFrom ='" + WebUser.getNo() + "' and c.EmpTo='" + WebUser.getNo() + "' and c.ActionType=22 and c.NDFrom=" + nd.getNodeID() + "  and c.ndto=" + nd.getNodeID() + " ";
		DataTable trackdt = DBAccess.RunSQLReturnTable(trackSql);
		if(trackdt.Rows.size() > 0){
			for (DataRow dr : trackdt.Rows) {
				String workid = dr.getValue("WorkID").toString();
				for (DataRow drn : dt.Rows) {
					String workidn = drn.getValue("WorkID").toString();
					drn.columns.Add("msg",String.class);
					if(workid.equals(workidn)) {
						drn.setValue("msg", dr.getValue("msg").toString());
						break;
					}
					else
						continue;
				}
		    }
		} else {
			for (DataRow drn : dt.Rows) {
				drn.columns.Add("msg",String.class);
				drn.setValue("msg", "");
			}
		}
		dt.TableName = "Works";
		ds.Tables.add(dt);


		//获取按钮权限
		BtnLab btnLab = new BtnLab(this.getFK_Node());
		ds.Tables.add(btnLab.ToDataTableField("Sys_BtnLab"));


		int nodeID = nd.getNodeID();

		//获取字段属性
		MapAttrs attrs = new MapAttrs("ND" + nodeID);

		//获取实际中需要展示的列.
		String batchParas = nd.GetParaString("BatchFields");
		MapAttrs realAttr = new MapAttrs();
		if (DataType.IsNullOrEmpty(batchParas) == false)
		{
			String[] strs = batchParas.split("[,]", -1);
			for (String str : strs)
			{
				if (DataType.IsNullOrEmpty(str) || str.contains("@PFlowNo") == true)
				{
					continue;
				}

				for (MapAttr attr : attrs.ToJavaList())
				{
					if (!attr.getKeyOfEn().equals(str))
					{
						continue;
					}

					realAttr.AddEntity(attr);
				}
			}
		}

		ds.Tables.add(realAttr.ToDataTableField("Sys_MapAttr"));

		return bp.tools.Json.ToJson(ds);
	}
	/** 
	 审核组件模式：批量发送
	 @return 
	*/
	public final String WorkCheckModel_Send() throws Exception {
		//审核批量发送.
		Node nd = new Node(this.getFK_Node());

		//获取要批处理数据
		String sql = String.format("SELECT WorkID, FID,Title FROM WF_EmpWorks WHERE FK_Emp='%1$s' and FK_Node='%2$s'", WebUser.getNo(), this.getFK_Node());
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		int idx = -1;
		String msg = "";
		String msg1 = "";
		//判断是否有传递来的参数.
		int toNode = this.GetRequestValInt("ToNode");
		String toEmps = this.GetRequestVal("ToEmps");
		String editFiles = nd.GetParaString("EditFields");

		//多表单的签批组件的修改
		FrmNode frmNode=null;
		if(nd.getHisFormType() == NodeFormType.SheetTree || nd.getHisFormType() == NodeFormType.RefOneFrmTree){
			FrmNodes frmNodes = new FrmNodes();
			QueryObject qo = new QueryObject(frmNodes);
			qo.AddWhere(FrmNodeAttr.FK_Node,nd.getNodeID());
			qo.addAnd();
			qo.AddWhere(FrmNodeAttr.IsEnableFWC,1);
			qo.addAnd();
			qo.AddWhereIsNotNull(NodeWorkCheckAttr.CheckField);
			qo.DoQuery();
			if(frmNodes.size()!=0)
				frmNode = (FrmNode) frmNodes.get(0);
		}
		int countS=0;
		int countF=0;
		try {
			for (DataRow dr : dt.Rows)
			{
				long workid = Long.parseLong(dr.getValue(0).toString());
				String cb = this.GetValFromFrmByKey("CB_" + workid, "0");
				if (cb.equals("on") == false)
				{
					continue;
				}

				//是否启用了审核组件？
				if (nd.getFrmWorkCheckSta() == FrmWorkCheckSta.Enable)
				{
					//绑定多表单，获取启用审核组件的表单
					if(frmNode!=null){
						GEEntity en = new GEEntity(frmNode.getFKFrm(),workid);
						en.SetValByKey(frmNode.getCheckField(),en.GetValStrByKey(frmNode.getCheckField())+","+nd.getNodeID());
						en.Update();
					}else{
						NodeWorkCheck workCheck = new NodeWorkCheck(nd.getNodeID()) ;
						if(DataType.IsNullOrEmpty(workCheck.getCheckField())==false){
							GEEntity en = new GEEntity(nd.getNodeFrmID(),workid);
							en.SetValByKey(workCheck.getCheckField(),en.GetValStrByKey(workCheck.getCheckField())+","+nd.getNodeID());
							en.Update();
						}
					}

					//获取审核意见的值
					String checkNote = "";

					//选择的多条记录一个意见框.
					int model = nd.GetParaInt("BatchCheckNoteModel", 0);

					//多条记录一个意见.
					if (model==0)
					{
						checkNote = this.GetRequestVal("CheckNote");
					}

					//每条记录都有自己的意见.
					if (model==1)
					{
						checkNote = this.GetValFromFrmByKey("TB_" + workid + "_WorkCheck_Doc", null);
					}

					if (model==2)
					{
						checkNote = " ";
					}

					//写入审核意见.
					if (DataType.IsNullOrEmpty(checkNote) == false)
					{
						Dev2Interface.WriteTrackWorkCheck(nd.getFK_Flow(), nd.getNodeID(), workid, Long.parseLong(dr.getValue("FID").toString()), checkNote, null, null);
					}
				}

				//设置字段的默认值.
				Work wk = nd.getHisWork();
				wk.setOID(workid);
				wk.Retrieve();
				wk.ResetDefaultVal(null, null, 0);
				if (DataType.IsNullOrEmpty(editFiles) == false)
				{
					String[] files = editFiles.split(",");
					String val = "";
					for(String key : files)
					{
						if (DataType.IsNullOrEmpty(key) == true)
							continue;
						val =  this.GetRequestVal("TB_"+ workid+"_"+key);
						wk.SetValByKey(key, val);
					}
				}
				wk.Update();

				//执行工作发送.
				msg1 += "@对工作(" + dr.getValue("Title") + ")处理情况如下";
				SendReturnObjs objs = Dev2Interface.Node_SendWork(nd.getFK_Flow(), workid, toNode, toEmps);
				countS++;
				msg1 += objs.ToMsgOfHtml();
				msg1 += "<br/>";
			}
		}catch(Exception ex){
			countF++;
			//throw new Exception(ex.getMessage());
		}

		if (msg1.equals(""))
		{
			msg1 = "没有选择需要处理的工作";
		}
		msg +="本次批量发送成功"+countS+"件，失败"+countF+"件。<br>"+msg1;
		return msg;
	}

	public final String WorkCheckModelVue_Send() throws Exception {
		//审核批量发送.
		Node nd = new Node(this.getFK_Node());
		String workIds = this.GetRequestVal("WorkIDs");
		String checkMsg = this.GetRequestVal("CheckMsg");
		String selectItems = this.GetRequestVal("SelectItems");
		JSONArray json = null;
		if (DataType.IsNullOrEmpty(selectItems) == false)
		{
			selectItems = URLDecoder.decode(selectItems, "UTF-8");
			json = JSONArray.fromObject(selectItems);
		}
		//获取要批处理数据
		String sql = String.format("SELECT WorkID, FID,Title FROM WF_EmpWorks WHERE FK_Emp='%1$s' and FK_Node='%2$s' and WorkID IN('"+ workIds.replace(",","','")+"')", WebUser.getNo(), this.getFK_Node());
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		int idx = -1;
		String msg = "";

		//判断是否有传递来的参数.
		int toNode = this.GetRequestValInt("ToNode");
		String toEmps = this.GetRequestVal("ToEmps");

		String editFiles = nd.GetParaString("EditFields");

		//多表单的签批组件的修改
		 FrmNode frmNode=null;
		if(nd.getHisFormType() == NodeFormType.SheetTree || nd.getHisFormType() == NodeFormType.RefOneFrmTree){
			FrmNodes frmNodes = new FrmNodes();
			QueryObject qo = new QueryObject(frmNodes);
			qo.AddWhere(FrmNodeAttr.FK_Node,nd.getNodeID());
			qo.addAnd();
			qo.AddWhere(FrmNodeAttr.IsEnableFWC,1);
			qo.addAnd();
			qo.AddWhereIsNotNull(NodeWorkCheckAttr.CheckField);
			qo.DoQuery();
			if(frmNodes.size()!=0)
				frmNode = (FrmNode) frmNodes.get(0);
		}
		final FrmNode frmNodel1 = frmNode;
		int successCout = 0;
		int errorCount = 0;
		String successMsg = "";
		String errorMsg = "";
		//启用线程的时候设置持有上下文的Request容器
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		RequestContextHolder.setRequestAttributes(servletRequestAttributes,true);
		final int POOL_SIZE = dt.Rows.size();
		ThreadPoolExecutor executor = new ThreadPoolExecutor(
				POOL_SIZE,
				POOL_SIZE,
				POOL_SIZE, TimeUnit.SECONDS,
				new ArrayBlockingQueue<>(POOL_SIZE),
				new ThreadPoolExecutor.CallerRunsPolicy());
		List<CompletableFuture<String>> futures = new ArrayList<CompletableFuture<String>>();
		for (DataRow dr : dt.Rows)
		{
			long workid = Long.parseLong(dr.getValue(0).toString());
			JSONObject obj = GetObject(workid,json);
			SystemConfig.setIsBSsystem(false);
			CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
				String threadMsg = "";
				try {
					threadMsg+=SendOneWorkFlow(nd,frmNodel1,workid,Long.parseLong(dr.getValue("FID").toString()),checkMsg,editFiles, dr.getValue("Title"),toNode,toEmps);
				} catch (Exception ex) {
					threadMsg += "err@工作(" +  dr.getValue("Title") + ")发送出现错误:";
					threadMsg += ex.getMessage();
					threadMsg += "<br/>";
				}
				return threadMsg;
			}, executor);
			String result = future.get();
			if(result.startsWith("err@")){
				errorMsg+=result.replace("err@","");
				errorCount++;
			}else{
				successMsg+=result;
				successCout++;
			}
			futures.add(future);
		}

		if (successCout != 0)
			msg += "@发送成功信息如下:<br/>" + successMsg;
		if (errorCount != 0)
			msg += "@发送失败信息如下:<br/>" + errorMsg;

		if (msg.equals(""))
			return "没有选择需要处理的工作";

		return "本次批量发送成功"+successCout+"件，失败"+errorCount+"件。<br>"+msg;
	}

	private String SendOneWorkFlow(Node nd,FrmNode frmNode,long workid,long fid,String checkMsg,String editFiles,Object title,int toNode,String toEmps) throws Exception {
		//是否启用了审核组件？
		if (nd.getFrmWorkCheckSta() == FrmWorkCheckSta.Enable)
		{
			//绑定多表单，获取启用审核组件的表单
			if(frmNode!=null){
				GEEntity en = new GEEntity(frmNode.getFKFrm(),workid);
				en.SetValByKey(frmNode.getCheckField(),en.GetValStrByKey(frmNode.getCheckField())+","+nd.getNodeID());
				en.Update();
			}else{
				NodeWorkCheck workCheck = new NodeWorkCheck(nd.getNodeID()) ;
				if(DataType.IsNullOrEmpty(workCheck.getCheckField())==false){
					GEEntity en = new GEEntity(nd.getNodeFrmID(),workid);
					en.SetValByKey(workCheck.getCheckField(),en.GetValStrByKey(workCheck.getCheckField())+","+nd.getNodeID());
					en.Update();
				}
			}
			//获取审核意见的值
			String checkNote = "";

			//选择的多条记录一个意见框.
			int model = nd.GetParaInt("BatchCheckNoteModel", 0);

			//多条记录一个意见.
			if (model==0  || model ==2)
				checkNote = checkMsg;

			if (model==2)
				checkNote = " ";

			//写入审核意见.
			if (DataType.IsNullOrEmpty(checkNote) == false)
			{
				Dev2Interface.WriteTrackWorkCheck(nd.getFK_Flow(), nd.getNodeID(), workid, fid, checkNote, null, null);
			}
		}

		//设置字段的默认值.
		Work wk = nd.getHisWork();
		wk.setOID(workid);
		wk.Retrieve();
		wk.ResetDefaultVal(null, null, 0);
		if (DataType.IsNullOrEmpty(editFiles) == false)
		{
			String[] files = editFiles.split(",");
			String val = "";
			for(String key : files)
			{
				if (DataType.IsNullOrEmpty(key) == true)
					continue;
				val =  this.GetRequestVal("TB_"+ workid+"_"+key);
				wk.SetValByKey(key, val);
			}
		}
		wk.Update();


		SendReturnObjs objs = Dev2Interface.Node_SendWork(nd.getFK_Flow(), workid, toNode, toEmps);
		//执行工作发送.
		String msg= "@对工作(" +title + ")处理情况如下";
		msg += objs.ToMsgOfHtml();
		msg += "<br/>";
		return msg;
	}

	private JSONObject GetObject(long compareWorkID,JSONArray json){
		if(json == null)
			return null;
		for(int i=0;i<json.size();i++){
			JSONObject obj = (JSONObject) json.get(i);
			//从表编号
			String workid=obj.get("WorkID").toString();
			if(Long.parseLong(workid) == compareWorkID)
				return obj;
		}
		return null;
	}
	public final String BatchList_Init() throws Exception {
		DataSet ds = new DataSet();

		String FK_Node = GetRequestVal("FK_Node");

		//获取节点信息
		Node nd = new Node(this.getFK_Node());
		Flow fl = nd.getHisFlow();
		ds.Tables.add(nd.ToDataTableField("WF_Node"));

		String sql = "";

		if (nd.getIsSubThread() == true)
		{
			sql = "SELECT a.*, b.Starter,b.StarterName,b.ADT,b.WorkID FROM " + fl.getPTable() + " a , WF_EmpWorks b WHERE a.OID=B.FID AND b.WFState Not IN (7) AND b.FK_Node=" + nd.getNodeID() + " AND b.FK_Emp='" + WebUser.getNo() + "'";
		}
		else
		{
			sql = "SELECT a.*, b.Starter,b.StarterName,b.ADT,b.WorkID FROM " + fl.getPTable() + " a , WF_EmpWorks b WHERE a.OID=B.WorkID AND b.WFState Not IN (7) AND b.FK_Node=" + nd.getNodeID() + " AND b.FK_Emp='" + WebUser.getNo() + "'";
		}

		//获取待审批的流程信息集合
		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		dt.TableName = "Batch_List";
		ds.Tables.add(dt);

		//获取按钮权限
		BtnLab btnLab = new BtnLab(this.getFK_Node());

		ds.Tables.add(btnLab.ToDataTableField("Sys_BtnLab"));

		//获取报表数据
		String inSQL = "SELECT WorkID FROM WF_EmpWorks WHERE FK_Emp='" + WebUser.getNo() + "' AND WFState!=7 AND FK_Node=" + this.getFK_Node();
		Works wks = nd.getHisWorks();
		wks.RetrieveInSQL(inSQL);

		ds.Tables.add(wks.ToDataTableField("WF_Work"));

		//获取字段属性
		MapAttrs attrs = new MapAttrs("ND" + this.getFK_Node());

		//获取实际中需要展示的列
		String batchParas = nd.getBatchParas();
		MapAttrs realAttr = new MapAttrs();
		if (DataType.IsNullOrEmpty(batchParas) == false)
		{
			String[] strs = batchParas.split("[,]", -1);
			for (String str : strs)
			{
				if (DataType.IsNullOrEmpty(str) || str.contains("@PFlowNo") == true)
				{
					continue;
				}

				for (MapAttr attr : attrs.ToJavaList())
				{
					if (!attr.getKeyOfEn().equals(str))
					{
						continue;
					}

					realAttr.AddEntity(attr);
				}
			}
		}

		ds.Tables.add(realAttr.ToDataTableField("Sys_MapAttr"));

		return bp.tools.Json.ToJson(ds);
	}


		///#endregion 界面方法.
		public String BatchToolBar_Init() throws Exception {
			DataSet ds = new DataSet();
			Node nd = new Node(this.getFK_Node());
			//获取按钮权限
			BtnLab btnLab = new BtnLab(this.getFK_Node());
			DataTable dt = new DataTable("ToolBar");
			dt.Columns.Add("No");
			dt.Columns.Add("Name");
			dt.Columns.Add("Oper");
			dt.Columns.Add("Role", Integer.class);
			dt.Columns.Add("Icon");
			DataRow dr = dt.NewRow();
			//发送
			dr.setValue("No","Send");
			dr.setValue("Name",btnLab.getSendLab());
			dr.setValue("Oper","");
			dt.Rows.add(dr);
			if (btnLab.getReturnEnable())
			{
				/*退回*/
				dr = dt.NewRow();
				dr.setValue("No","Return");
				dr.setValue("Name",btnLab.getReturnLab());
				dr.setValue("Oper","");
				dt.Rows.add(dr);
			}
			if (btnLab.getDeleteEnable() != 0)
			{
				dr = dt.NewRow();
				dr.setValue("No","DeleteFlow");
				dr.setValue("Name", btnLab.getDeleteLab());
				dr.setValue("Oper","");
				dt.Rows.add(dr);

			}

			if (btnLab.getEndFlowEnable() && nd.isStartNode() == false)
			{
				dr = dt.NewRow();
				dr.setValue("No","EndFlow");
				dr.setValue("Name",btnLab.getEndFlowLab());
				dr.setValue("Oper","");
				dt.Rows.add(dr);

			}
           /* int checkModel = nd.GetParaInt("BatchCheckNoteModel");
            if (nd.FrmWorkCheckSta == FrmWorkCheckSta.Enable && checkModel == 0) {
                *//*增加审核意见*//*
                dr = dt.NewRow();
                dr["No"] = "WorkCheckMsg";
                dr["Name"] = "填写审核意见";
                dr["Oper"] = "";
                dt.Rows.Add(dr);
            }*/
			ds.Tables.add(dt);
			GenerWorkFlow gwf = new GenerWorkFlow();
			gwf.setTodoEmps(WebUser.getNo() + ",");
			DataTable dtNodes = bp.wf.Dev2Interface.Node_GenerDTOfToNodes(gwf, nd);
			if (dtNodes != null)
				ds.Tables.add(dtNodes);

			ds.Tables.add(nd.ToDataTableField("WF_Node"));
			return bp.tools.Json.ToJson(ds);
		}

		///#region 通用方法.
	/** 
	 批量删除
	 
	 @return 
	*/
	public final String Batch_Delete() throws Exception {
		Node nd = new Node(this.getFK_Node());
		String workIDs = this.GetRequestVal("WorkIDs");
		if (DataType.IsNullOrEmpty(workIDs) == true)
		{
			return "err@没有选择需要处理的工作";
		}
		String msg = "";
		GenerWorkFlows gwfs = new GenerWorkFlows();
		gwfs.RetrieveIn("WorkID", workIDs);
		for (GenerWorkFlow gwf : gwfs.ToJavaList())
		{
			msg += "@对工作(" + gwf.getTitle() + ")处理情况如下。<br>";
			String mes = Dev2Interface.Flow_DoDeleteFlowByFlag(gwf.getWorkID(), "批量删除", true);
			msg += mes;
			msg += "<hr>";
		}
		return "批量删除成功" + msg;

	}
	/** 
	 批量退回 待定
	 
	 @return 
	*/
	public final String Batch_Return() throws Exception {
		Node nd = new Node(this.getFK_Node());
		String workIDs = this.GetRequestVal("WorkIDs");
		if (DataType.IsNullOrEmpty(workIDs) == true)
		{
			workIDs = String.valueOf(this.getWorkID());
		}

			//return "err@没有选择需要处理的工作";
		String msg = "";

		String[] vals = this.GetRequestVal("ReturnToNode").split("[@]", -1);
		int toNodeID = Integer.parseInt(vals[0]);

		String toEmp = vals[1];
		String reMesage = this.GetRequestVal("ReturnInfo");

		boolean isBackBoolen = false;
		if (this.GetRequestVal("IsBack").equals("1") == true)
		{
			isBackBoolen = true;
		}

		boolean isKill = false; //是否全部退回.
		String isKillEtcThread = this.GetRequestVal("IsKillEtcThread");
		if (DataType.IsNullOrEmpty(isKillEtcThread) == false && isKillEtcThread.equals("1") == true)
		{
			isKill = true;
		}

		String pageData = this.GetRequestVal("PageData");
		GenerWorkFlows gwfs = new GenerWorkFlows();
		gwfs.RetrieveIn("WorkID", workIDs);
		for (GenerWorkFlow gwf : gwfs.ToJavaList())
		{
			msg += "@对工作(" + gwf.getTitle() + ")处理情况如下。<br>";
			msg += Dev2Interface.Node_ReturnWork(gwf.getWorkID(), toNodeID, toEmp, reMesage, isBackBoolen, pageData, isKill);
			msg += "<hr>";

		}
		return msg;
	}

	/**
	 * 批量结束流程
	 * @return
	 * @throws Exception
	 */
	public String Batch_StopFlow() throws Exception {
		String workIDs = this.GetRequestVal("WorkIDs");
		if (DataType.IsNullOrEmpty(workIDs) == true)
			return "err@没有选择需要处理的工作";
		String msg = "";
		GenerWorkFlows gwfs = new GenerWorkFlows();
		gwfs.RetrieveIn("WorkID", workIDs);
		for(GenerWorkFlow gwf : gwfs.ToJavaList())
		{
			msg += "@对工作(" + gwf.getTitle() + ")处理情况如下。<br>";
			String mes = bp.wf.Dev2Interface.Flow_DoFlowOver(gwf.getWorkID(), "批量结束流程");
			msg += mes;
			msg += "<hr>";
		}
		return "批量结束成功" + msg;

	}
	public final String Batch_WriteMsg() throws Exception {
		Node nd = new Node(this.getFK_Node());
		//获取要批处理数据
		String sql = String.format("SELECT WorkID, FID,Title FROM WF_EmpWorks WHERE FK_Emp='%1$s' and FK_Node='%2$s'", WebUser.getNo(), this.getFK_Node());
		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		String msg = "";

		//判断是否有传递来的参数.
		String batchmsg = GetRequestVal("batchMsg");

		for (DataRow dr : dt.Rows) {
			long workid = Long.parseLong(dr.getValue(0).toString());
			String cb = GetValFromFrmByKey("CB_" + workid, "0");
			if (cb.equals("on") == false) {
				continue;
			}
			Dev2Interface.WriteTrackWorkCheck(nd.getFK_Flow(), nd.getNodeID(), workid, Long.parseLong(dr.getValue("FID").toString()), batchmsg, null, null);
			msg="批量设置审批意见成功";
		}
		return msg;
	}

		///#endregion

}