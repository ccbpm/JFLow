package bp.wf.httphandler;

import bp.da.*;
import bp.en.QueryObject;
import bp.sys.*;
import bp.web.*;
import bp.wf.template.*;
import bp.*;
import bp.wf.*;

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

		if (nd.getHisRunModel() == RunModel.SubThread)
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

	public final String BatchList_Init() throws Exception {
		DataSet ds = new DataSet();

		String FK_Node = GetRequestVal("FK_Node");

		//获取节点信息
		Node nd = new Node(this.getFK_Node());
		Flow fl = nd.getHisFlow();
		ds.Tables.add(nd.ToDataTableField("WF_Node"));

		String sql = "";

		if (nd.getHisRunModel() == RunModel.SubThread)
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

		/*  MapAttrs attrs = new MapAttrs("ND" + this.FK_Node);

		  //获取数据
		  string sql = string.Format("SELECT Title,RDT,ADT,SDT,FID,WorkID,Starter FROM WF_EmpWorks WHERE FK_Emp='{0}' and FK_Node='{1}'", WebUser.getNo(), this.FK_Node);

		  DataTable dt = DBAccess.RunSQLReturnTable(sql);
		  int idx = -1;
		  string msg = "";
		  foreach (DataRow dr in dt.Rows)
		  {
		      idx++;
		      if (idx == nd.BatchListCount)
		      {
		          break;
		      }

		      Int64 workid = Int64.Parse(dr["WorkID"].ToString());
		      string cb = this.GetValFromFrmByKey("CB_" + workid, "0");
		      if (cb == "0") //没有选中
		      {
		          continue;
		      }

		      msg += "@对工作(" + dr["Title"] + ")处理情况如下。<br>";
		      string mes = BP.WF.Dev2Interface.Flow_DoDeleteFlowByFlag(nd.FK_Flow, workid, "批量删除", true);
		      msg += mes;
		      msg += "<hr>";

		  }
		  if (msg == "")
		  {
		      msg = "没有选择需要处理的工作";
		  }*/


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