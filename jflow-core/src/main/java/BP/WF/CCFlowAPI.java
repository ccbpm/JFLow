package BP.WF;

import java.util.Enumeration;

import cn.jflow.common.util.ContextHolderUtils;
import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.DA.Log;
import BP.DA.Paras;
import BP.En.Entity;
import BP.En.QueryObject;
import BP.Port.Emp;
import BP.Sys.AthCtrlWay;
import BP.Sys.AthUploadWay;
import BP.Sys.DtlOpenType;
import BP.Sys.EventListOfNode;
import BP.Sys.FrmAttachment;
import BP.Sys.FrmAttachmentAttr;
import BP.Sys.FrmAttachmentDBAttr;
import BP.Sys.FrmAttachments;
import BP.Sys.FrmEventList;
import BP.Sys.FrmImgAthDBs;
import BP.Sys.FrmSubFlowAttr;
import BP.Sys.FrmWorkCheckAttr;
import BP.Sys.GEDtlAttr;
import BP.Sys.GEDtls;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.MapDtl;
import BP.Sys.MapDtls;
import BP.Sys.MapExt;
import BP.Sys.MapExtAttr;
import BP.Sys.MapExtXmlList;
import BP.Sys.MapExts;
import BP.Sys.SystemConfig;
import BP.WF.Template.CondModel;
import BP.WF.Template.FTCAttr;
import BP.WF.Template.FrmNode;
import BP.WF.Template.FrmNodeComponent;
import BP.WF.Template.FrmThreadAttr;
import BP.WF.Template.FrmTrackAttr;
import BP.WF.Template.HungUpAttr;
import BP.WF.Template.HungUps;
import BP.WF.Template.NodeAttr;
import BP.WF.Template.NodeYGFlow;
import BP.WF.Template.NodeYGFlows;
import BP.Web.WebUser;

public class CCFlowAPI
{
	/** 
	 产生一个WorkNode 区分大小写
	 
	 @param fk_flow 流程编号
	 @param fk_node 节点ID
	 @param workID 工作ID
	 @param fid FID
	 @param userNo 用户编号
	 @return 返回dataset
	*/
	public static DataSet GenerWorkNode_2017(String fk_flow, int fk_node, long workID, long fid, String userNo)
	{

		//让其登录. ??? 为什么需要登录？
		if (!userNo.equals(WebUser.getNo()))
		{
			Emp emp = new Emp(userNo);
			BP.Web.WebUser.SignInOfGener(emp);
		}

		//节点.
		if (fk_node == 0)	 
			fk_node = Integer.parseInt(fk_flow + "01");
		 

		if (workID == 0)		 
			workID = BP.WF.Dev2Interface.Node_CreateBlankWork(fk_flow, null, null, userNo, null,0,0,null,0,null,0,null);
		

		Node nd = new Node(fk_node);
		try
		{
  
			
			  MapData md = new MapData();
              md.setNo(  nd.getNodeFrmID());
              
		 
			//md.setNo(nd.getNodeFrmID());
			if (md.RetrieveFromDBSources() == 0)
			{
				throw new RuntimeException("装载错误，该表单ID=" + md.getNo() + "丢失，请修复一次流程重新加载一次.");
			}
			
			Work wk = nd.getHisWork();
            wk.setOID(workID);
            wk.RetrieveFromDBSources();
            
            // 第1.2: 调用,处理用户定义的业务逻辑.
            String sendWhen = nd.getHisFlow().DoFlowEventEntity(EventListOfNode.FrmLoadBefore, nd,
                    wk, null);
            
			//获得表单模版.
			DataSet myds = BP.Sys.CCFormAPI.GenerHisDataSet_2017(md.getNo());
			
			 //把流程信息表发送过去.
            GenerWorkFlow gwf = new GenerWorkFlow();
            gwf.setWorkID(workID);
            gwf.RetrieveFromDBSources();

            //加入WF_Node.
            DataTable WF_Node = nd.ToDataTableField("WF_Node");
            myds.Tables.add(WF_Node);
			
           //  #region 加入组件的状态信息, 在解析表单的时候使用.
			FrmNodeComponent fnc = new FrmNodeComponent(nd.getNodeID());
			
			 if (nd.getNodeFrmID() != "ND" + nd.getNodeID()  && nd.getHisFormType() != NodeFormType.RefOneFrmTree)
             {
                 /*说明这是引用到了其他节点的表单，就需要把一些位置元素修改掉.*/
                 int refNodeID = Integer.parseInt(nd.getNodeFrmID().replace("ND", ""));

                 BP.WF.Template.FrmNodeComponent refFnc = new FrmNodeComponent(refNodeID);

                 fnc.SetValByKey(FrmWorkCheckAttr.FWC_H, refFnc.GetValFloatByKey(FrmWorkCheckAttr.FWC_H));
                 fnc.SetValByKey(FrmWorkCheckAttr.FWC_W, refFnc.GetValFloatByKey(FrmWorkCheckAttr.FWC_W));
                 fnc.SetValByKey(FrmWorkCheckAttr.FWC_X, refFnc.GetValFloatByKey(FrmWorkCheckAttr.FWC_X));
                 fnc.SetValByKey(FrmWorkCheckAttr.FWC_Y, refFnc.GetValFloatByKey(FrmWorkCheckAttr.FWC_Y));
                 
                 if (fnc.GetValFloatByKey(FrmWorkCheckAttr.FWC_H) <= 10)
                     fnc.SetValByKey(FrmWorkCheckAttr.FWC_H, 500);

                 if (fnc.GetValFloatByKey(FrmWorkCheckAttr.FWC_W) <= 10)
                     fnc.SetValByKey(FrmWorkCheckAttr.FWC_W, 600);

                 if (fnc.GetValFloatByKey(FrmWorkCheckAttr.FWC_X) <= 10)
                     fnc.SetValByKey(FrmWorkCheckAttr.FWC_X, 200);

                 if (fnc.GetValFloatByKey(FrmWorkCheckAttr.FWC_Y) <= 10)
                     fnc.SetValByKey(FrmWorkCheckAttr.FWC_Y, 200);
                 

                 fnc.SetValByKey(FrmSubFlowAttr.SF_H, refFnc.GetValFloatByKey(FrmSubFlowAttr.SF_H));
                 fnc.SetValByKey(FrmSubFlowAttr.SF_W, refFnc.GetValFloatByKey(FrmSubFlowAttr.SF_W));
                 fnc.SetValByKey(FrmSubFlowAttr.SF_X, refFnc.GetValFloatByKey(FrmSubFlowAttr.SF_X));
                 fnc.SetValByKey(FrmSubFlowAttr.SF_Y, refFnc.GetValFloatByKey(FrmSubFlowAttr.SF_Y));

                 fnc.SetValByKey(FrmThreadAttr.FrmThread_H, refFnc.GetValFloatByKey(FrmThreadAttr.FrmThread_H));
                 fnc.SetValByKey(FrmThreadAttr.FrmThread_W, refFnc.GetValFloatByKey(FrmThreadAttr.FrmThread_W));
                 fnc.SetValByKey(FrmThreadAttr.FrmThread_X, refFnc.GetValFloatByKey(FrmThreadAttr.FrmThread_X));
                 fnc.SetValByKey(FrmThreadAttr.FrmThread_Y, refFnc.GetValFloatByKey(FrmThreadAttr.FrmThread_Y));

                 fnc.SetValByKey(FrmTrackAttr.FrmTrack_H, refFnc.GetValFloatByKey(FrmTrackAttr.FrmTrack_H));
                 fnc.SetValByKey(FrmTrackAttr.FrmTrack_W, refFnc.GetValFloatByKey(FrmTrackAttr.FrmTrack_W));
                 fnc.SetValByKey(FrmTrackAttr.FrmTrack_X, refFnc.GetValFloatByKey(FrmTrackAttr.FrmTrack_X));
                 fnc.SetValByKey(FrmTrackAttr.FrmTrack_Y, refFnc.GetValFloatByKey(FrmTrackAttr.FrmTrack_Y));

                 fnc.SetValByKey(FTCAttr.FTC_H, refFnc.GetValFloatByKey(FTCAttr.FTC_H));
                 fnc.SetValByKey(FTCAttr.FTC_W, refFnc.GetValFloatByKey(FTCAttr.FTC_W));
                 fnc.SetValByKey(FTCAttr.FTC_X, refFnc.GetValFloatByKey(FTCAttr.FTC_X));
                 fnc.SetValByKey(FTCAttr.FTC_Y, refFnc.GetValFloatByKey(FTCAttr.FTC_Y));
             }
			  
		
	         myds.Tables.add(fnc.ToDataTableField("WF_FrmNodeComponent"));				
	         //#endregion 加入组件的状态信息, 在解析表单的时候使用.

			// #region 流程设置信息.
			if (nd.getIsStartNode() == false)
			{
				BP.WF.Dev2Interface.Node_SetWorkRead(fk_node, workID);
			}

			//增加转向下拉框数据.
			if (nd.getCondModel() == CondModel.SendButtonSileSelect)
			{
				//如果当前节点，是可以显示下拉框的.
				Nodes nds = nd.getHisToNodes();

				DataTable dtToNDs = new DataTable();
				dtToNDs.TableName = "ToNodes";
				dtToNDs.Columns.Add("No",String.class);
				dtToNDs.Columns.Add("Name", String.class);
				dtToNDs.Columns.Add("IsSelectEmps", String.class);

				for (Node item : nds.ToJavaList())
				{
					DataRow dr = dtToNDs.NewRow();
					dr.put("No",item.getNodeID());
					dr.put("Name",item.getName());
					
					if (item.getHisDeliveryWay() == DeliveryWay.BySelected)
					{
						dr.put("IsSelectEmps","1");
					}
					else
					{
						dr.put("IsSelectEmps","0"); //是不是，可以选择接受人.
					}
					dtToNDs.Rows.add(dr);
				}
				
				 //增加到达延续子流程节点. 
                NodeYGFlows ygflows = new NodeYGFlows(String.valueOf(fk_node));
                
                if (ygflows.size() > 1)
                    dtToNDs.Rows.clear();  //为浙商银行做的特殊判断，如果配置了延续流程，就不让其走分支节点.
                
                for (NodeYGFlow item : ygflows.ToJavaList())
                {
                    DataRow dr = dtToNDs.NewRow();
                    
                    dr.put("No",  item.getFK_Flow() + "01");
                    
                    dr.put("Name",  "启动:" +item.getFlowName() );
                   
                    dr.put("IsSelectEmps", "1");
                    //else
                    //  dr["IsSelectEmps"] = "0";  //是不是，可以选择接受人.

                    //设置默认选择的节点.
                    //if (defalutSelectedNodeID == item.NodeID)
                    //    dr["IsSelected"] = "1";
                    //else
                    //    dr["IsSelected"] = "0";

                    dr.put("IsSelected", "0");
                    dtToNDs.Rows.add(dr);
                }
                //#endregion 增加到达延续子流程节点。
                
               // #region 到达其他节点.

                //上一次选择的节点.
                int defalutSelectedNodeID = 0;
                if (nds.size() > 1)
                {
                    String mysql = "";
                    // 找出来上次发送选择的节点.
                    if (SystemConfig.getAppCenterDBType() == DBType.MSSQL)
                        mysql = "SELECT  top 1 NDTo FROM ND" + Integer.parseInt(nd.getFK_Flow()) + "Track A WHERE A.NDFrom=" + fk_node + " AND ActionType=1 ORDER BY WorkID DESC";
                    else if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
                        mysql = "SELECT * FROM ( SELECT  NDTo FROM ND" + Integer.parseInt(nd.getFK_Flow()) + "Track A WHERE A.NDFrom=" + fk_node + " AND ActionType=1 ORDER BY WorkID DESC ) WHERE ROWNUM =1";
                    else if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
                        mysql = "SELECT  NDTo FROM ND" + Integer.parseInt(nd.getFK_Flow()) + "Track A WHERE A.NDFrom=" + fk_node + " AND ActionType=1 ORDER BY WorkID  DESC limit 1,1";

                    //获得上一次发送到的节点.
                    defalutSelectedNodeID = DBAccess.RunSQLReturnValInt(mysql, 0);
                }

                //#region 为天业集团做一个特殊的判断.
                if (SystemConfig.getCustomerNo() == "TianYe" && nd.getName().contains("董事长") == true)
                {
                    /*如果是董事长节点, 如果是下一个节点默认的是备案. */
                	for (Node item : nds.ToJavaList())
                    {
                        if (item.getName().contains("备案") == true && item.getName().contains("待") == false)
                        {
                            defalutSelectedNodeID = item.getNodeID();
                            break;
                        }
                    }
                }
           
 


				//增加一个下拉框, 对方判断是否有这个数据.
				myds.Tables.add(dtToNDs);
			}


			// 节点数据.
			//string sql = "SELECT * FROM WF_Node WHERE NodeID=" + fk_node;
			//DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			//dt.TableName = "WF_NodeBar";
			//myds.Tables.Add(dt);

			//// 流程数据.
			//Flow fl = new Flow(fk_flow);
			//myds.Tables.Add(fl.ToDataTableField("WF_Flow"));

				///#endregion 流程设置信息.


			///#region 把主从表数据放入里面.
			//.工作数据放里面去, 放进去前执行一次装载前填充事件.
			//重设默认值.
            wk.ResetDefaultVal();

			// 处理传递过来的参数。
//			for (String k : System.Web.HttpContext.Current.Request.QueryString.AllKeys)
//			{
//				wk.SetValByKey(k, System.Web.HttpContext.Current.Request.QueryString[k]);
//			}
			Enumeration enu = ContextHolderUtils.getRequest().getParameterNames();
			while(enu.hasMoreElements())
			{
				String k = (String)enu.nextElement();
				wk.SetValByKey(k, ContextHolderUtils.getRequest().getParameter(k));
			}

			// 执行表单事件..
			String msg = md.getFrmEvents().DoEventNode(FrmEventList.FrmLoadBefore, wk);
			if (DotNetToJavaStringHelper.isNullOrEmpty(msg) == false)
			{
				throw new RuntimeException("err@错误:" + msg);
			}

			 // 执行FEE事件.
            String msgOfLoad = nd.getHisFlow().DoFlowEventEntity(EventListOfNode.FrmLoadBefore, nd,
                wk, null);
            if (msgOfLoad != null)
                wk.RetrieveFromDBSources();

			//执行装载填充.
			MapExt me = new MapExt();
			me.setMyPK(wk.NodeFrmID + "_" + MapExtXmlList.PageLoadFull);
			if (me.RetrieveFromDBSources() == 1)
			{
				//执行通用的装载方法.
				MapAttrs attrs = new MapAttrs("ND" + fk_node);
				MapDtls dtls = new MapDtls("ND"+fk_node);
				Entity tempVar = BP.WF.Glo.DealPageLoadFull(wk, me, attrs, dtls);
				wk = (Work)((tempVar instanceof Work) ? tempVar : null);
			}

			DataTable mainTable = wk.ToDataTableField(md.getNo());
			mainTable.TableName = "MainTable";
			myds.Tables.add(mainTable);

			String sql = "";
			DataTable dt = null;

			// #region 图片附件
			FrmImgAthDBs imgAthDBs = new FrmImgAthDBs(nd.getNodeFrmID(), String.valueOf(workID));
            if (imgAthDBs != null && imgAthDBs.size() > 0)
            {
                DataTable dt_ImgAth = imgAthDBs.ToDataTableField("Sys_FrmImgAthDB");
                myds.Tables.add(dt_ImgAth);
            }
            //#endregion
            //#region 增加附件信息.
            FrmAttachments athDescs = new FrmAttachments();
            athDescs.Retrieve(FrmAttachmentAttr.FK_MapData, nd.getNodeFrmID());
            if (athDescs.size() != 0)
            {
                FrmAttachment athDesc = (FrmAttachment) athDescs.get(0);

                //查询出来数据实体.
                BP.Sys.FrmAttachmentDBs dbs = new BP.Sys.FrmAttachmentDBs();
                if (athDesc.getHisCtrlWay() == AthCtrlWay.PWorkID)
                {
                    String pWorkID = String.valueOf(BP.DA.DBAccess.RunSQLReturnValInt("SELECT PWorkID FROM WF_GenerWorkFlow WHERE WorkID=" + workID, 0));
                    if (pWorkID == null || pWorkID.equals("0") ==true)
                        pWorkID = String.valueOf(workID);

                    if (athDesc.getAthUploadWay() == AthUploadWay.Inherit)
                    {
                        /* 继承模式 */
                        BP.En.QueryObject qo = new BP.En.QueryObject(dbs);
                        qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, pWorkID);
                        qo.addOr();
                        qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, workID);
                        qo.addOrderBy("RDT");
                        qo.DoQuery();
                    }

                    if (athDesc.getAthUploadWay() == AthUploadWay.Interwork)
                    {
                        /*共享模式*/
                        dbs.Retrieve(FrmAttachmentDBAttr.RefPKVal, pWorkID);
                    }
                }
                else if (athDesc.getHisCtrlWay() == AthCtrlWay.WorkID)
                {
                    /* 继承模式 */
                    BP.En.QueryObject qo = new BP.En.QueryObject(dbs);
                    qo.AddWhere(FrmAttachmentDBAttr.NoOfObj, athDesc.getNoOfObj());
                    qo.addAnd();
                    qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, workID);
                    qo.addOrderBy("RDT");
                    qo.DoQuery();
                }

                //增加一个数据源.
                myds.Tables.add(dbs.ToDataTableField("Sys_FrmAttachmentDB"));
            }
           
			

			////把从表的数据放入.
			//if (md.MapDtls.Count > 0)
			//{
			//    foreach (MapDtl dtl in md.MapDtls)
			//    {
			//        GEDtls dtls = new GEDtls(dtl.No);
			//        QueryObject qo = null;
			//        try
			//        {
			//            qo = new QueryObject(dtls);
			//            switch (dtl.DtlOpenType)
			//            {
			//                case DtlOpenType.ForEmp:  // 按人员来控制.
			//                    qo.AddWhere(GEDtlAttr.RefPK, workID);
			//                    qo.addAnd();
			//                    qo.AddWhere(GEDtlAttr.Rec, WebUser.getNo());
			//                    break;
			//                case DtlOpenType.ForWorkID: // 按工作ID来控制
			//                    qo.AddWhere(GEDtlAttr.RefPK, workID);
			//                    break;
			//                case DtlOpenType.ForFID: // 按流程ID来控制.
			//                    qo.AddWhere(GEDtlAttr.FID, workID);
			//                    break;
			//            }
			//        }
			//        catch
			//        {
			//            dtls.GetNewEntity.CheckPhysicsTable();
			//        }
			//        DataTable dtDtl = qo.DoQueryToTable();

			//        // 为明细表设置默认值.
			//        MapAttrs dtlAttrs = new MapAttrs(dtl.No);
			//        foreach (MapAttr attr in dtlAttrs)
			//        {
			//            //处理它的默认值.
			//            if (attr.DefValReal.Contains("@") == false)
			//                continue;

			//            foreach (DataRow dr in dtDtl.Rows)
			//                dr[attr.KeyOfEn] = attr.DefVal;
			//        }

			//        dtDtl.TableName = dtl.No; //修改明细表的名称.
			//        myds.Tables.Add(dtDtl); //加入这个明细表, 如果没有数据，xml体现为空.
			//    }
			//}

				///#endregion


            ///#region 把外键表加入DataSet
			//DataTable dtMapAttr = myds.Tables.;
			DataTable dtMapAttr = null;;
			for (DataTable dtb : myds.Tables)
			{
				if("Sys_MapAttr".equals(dtb.getTableName()))
				{
					dtMapAttr = dtb ; 
				}
			}

			MapExts mes = md.getMapExts();

			for (DataRow dr : dtMapAttr.Rows)
			{
				String lgType = dr.getValue("LGType").toString();
				if (lgType.equals("2")==false)
				{
					continue;
				}

				String UIIsEnable = dr.getValue("UIIsEnable").toString();
				if (UIIsEnable.equals("0")==true)
				{
					continue;
				}

				String uiBindKey = dr.getValue("UIBindKey").toString();
				if (DotNetToJavaStringHelper.isNullOrEmpty(uiBindKey) == true)
				{
					String myPK = dr.getValue("MyPK").toString();
					//如果是空的
				 //   throw new Exception("@属性字段数据不完整，流程:" + fl.No + fl.Name + ",节点:" + nd.NodeID + nd.Name + ",属性:" + myPK + ",的UIBindKey IsNull ");
				}

				// 检查是否有下拉框自动填充。
				String keyOfEn = dr.getValue("KeyOfEn").toString();
				String fk_mapData = dr.getValue("FK_MapData").toString();


				///#region 处理下拉框数据范围. for 小杨.
				Object tempVar2 = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL, MapExtAttr.AttrOfOper, keyOfEn);
				me = (MapExt)((tempVar2 instanceof MapExt) ? tempVar2 : null);
				if (me != null)
				{
					Object tempVar3 = me.getDoc();
					String fullSQL = (String)((tempVar3 instanceof String) ? tempVar3 : null);
					fullSQL = fullSQL.replace("~", ",");
					fullSQL = BP.WF.Glo.DealExp(fullSQL, wk, null);
					dt = DBAccess.RunSQLReturnTable(fullSQL);
					dt.TableName = keyOfEn; //可能存在隐患，如果多个字段，绑定同一个表，就存在这样的问题.
					myds.Tables.add(dt);
					continue;
				}

					///#endregion 处理下拉框数据范围.

				// 判断是否存在.
				if (myds.Tables.contains(uiBindKey) == true)
				{
					continue;
				}

				myds.Tables.add(BP.Sys.PubClass.GetDataTableByUIBineKey(uiBindKey));
			}

			///#endregion End把外键表加入DataSet

			///#region 处理流程-消息提示.
			DataTable dtAlert = new DataTable();
			dtAlert.TableName = "AlertMsg";
			dtAlert.Columns.Add("Title", String.class);
			dtAlert.Columns.Add("Msg", String.class);
			dtAlert.Columns.Add("URL", String.class);

			//  string msg = "";
			switch (gwf.getWFState())
			{
				case AskForReplay: // 返回加签的信息.
					String mysql = "SELECT * FROM ND" + Integer.parseInt(fk_flow) + "Track WHERE WorkID=" + workID + " AND " + TrackAttr.ActionType + "=" + ActionType.ForwardAskfor.getValue();
					DataTable mydt = BP.DA.DBAccess.RunSQLReturnTable(mysql);
					for (DataRow dr : mydt.Rows)
					{
						String msgAskFor = dr.getValue(TrackAttr.Msg).toString();
						String worker = dr.getValue(TrackAttr.EmpFrom).toString();
						String workerName = dr.getValue(TrackAttr.EmpFromT).toString();
						String rdt = dr.getValue(TrackAttr.RDT).toString();

						DataRow drMsg = dtAlert.NewRow();
						drMsg.put("Title",worker + "," + workerName + "回复信息:");
						drMsg.put("Msg",DataType.ParseText2Html(msgAskFor) + "<br>" + rdt);
						dtAlert.Rows.add(drMsg);
					}
					break;
				case Askfor: //加签.

					sql = "SELECT * FROM ND" + Integer.parseInt(fk_flow) + "Track WHERE WorkID=" + workID + " AND " + TrackAttr.ActionType + "=" + ActionType.AskforHelp.getValue();
					dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
					for (DataRow dr : dt.Rows)
					{
						String msgAskFor = dr.getValue(TrackAttr.Msg).toString();
						String worker = dr.getValue(TrackAttr.EmpFrom).toString();
						String workerName = dr.getValue(TrackAttr.EmpFromT).toString();
						String rdt = dr.getValue(TrackAttr.RDT).toString();

						DataRow drMsg = dtAlert.NewRow();
						drMsg.put("Title",worker + "," + workerName + "请求加签:");
						drMsg.put("Msg",DataType.ParseText2Html(msgAskFor) + "<br>" + rdt + "<a href='./WorkOpt/AskForRe.htm?FK_Flow=" + fk_flow + "&FK_Node=" + fk_node + "&WorkID=" + workID + "&FID=" + fid + "' >回复加签意见</a> --");
						dtAlert.Rows.add(drMsg);

						//提示信息.
						// this.FlowMsg.AlertMsg_Info(worker + "," + workerName + "请求加签:",
						//   DataType.ParseText2Html(msgAskFor) + "<br>" + rdt + " --<a href='./WorkOpt/AskForRe.jsp?FK_Flow=" + this.FK_Flow + "&FK_Node=" + this.FK_Node + "&WorkID=" + this.WorkID + "&FID=" + this.FID + "' >回复加签意见</a> --");
					}
					// isAskFor = true;
					break;
				case ReturnSta:
					// 如果工作节点退回了
					ReturnWorks rws = new ReturnWorks();
					rws.Retrieve(ReturnWorkAttr.ReturnToNode, fk_node, ReturnWorkAttr.WorkID, workID, ReturnWorkAttr.RDT);
					if (rws.size() != 0)
					{
						String msgInfo = "";
						for (BP.WF.ReturnWork rw : rws.ToJavaList())
						{
							DataRow drMsg = dtAlert.NewRow();
							drMsg.put("Title","来自节点:" + rw.getReturnNodeName() + " 退回人:" + rw.getReturnerName() + "  " + rw.getRDT() + "&nbsp;<a href='../DataUser/ReturnLog/" + fk_flow + "/" + rw.getMyPK() + ".htm' target=_blank>工作日志</a>");
							drMsg.put("Msg",rw.getBeiZhuHtml());
							dtAlert.Rows.add(drMsg);
						}

						String str = nd.getReturnAlert();
						if (!str.equals(""))
						{
							str = str.replace("~", "'");
							str = str.replace("@PWorkID", (new Long(workID)).toString());
							str = str.replace("@PNodeID", (new Integer(nd.getNodeID())).toString());
							str = str.replace("@FK_Node", (new Integer(nd.getNodeID())).toString());

							str = str.replace("@PFlowNo", fk_flow);
							str = str.replace("@FK_Flow", fk_flow);
							str = str.replace("@PWorkID", (new Long(workID)).toString());

							str = str.replace("@WorkID", (new Long(workID)).toString());
							str = str.replace("@OID", (new Long(workID)).toString());



							DataRow drMsg = dtAlert.NewRow();
							drMsg.put("Title","退回信息");
							drMsg.put("Msg",str);
							dtAlert.Rows.add(drMsg);
						}
						/*else
						{
							DataRow drMsg = dtAlert.NewRow();
							drMsg.put("Title","退回信息");
							drMsg.put("Msg",msgInfo);
							dtAlert.Rows.Add(drMsg);
						}*/
						//gwf.WFState = WFState.Runing;
						//gwf.DirectUpdate();
					}
					break;
				case Shift: 
					// 判断移交过来的。 
					ShiftWorks fws = new ShiftWorks();
					BP.En.QueryObject qo = new QueryObject(fws);
					qo.AddWhere(ShiftWorkAttr.WorkID, workID);
					qo.addAnd();
					qo.AddWhere(ShiftWorkAttr.FK_Node, fk_node);
					qo.addOrderBy(ShiftWorkAttr.RDT);
					qo.DoQuery();
					if (fws.size() >= 1)
					{
						DataRow drMsg = dtAlert.NewRow();
						drMsg.put("Title","移交历史信息");
						msg = "";
						for (ShiftWork fw : fws.ToJavaList())
						{
							String temp = "@移交人[" + fw.getFK_Emp() + "," + fw.getFK_EmpName() + "]。@接受人：" + fw.getToEmp() + "," + fw.getToEmpName() + "。<br>移交原因：-------------" + fw.getNoteHtml();
							if (fw.getFK_Emp().equals(WebUser.getNo()))
							{
								temp = "<b>" + temp + "</b>";
							}

							temp = temp.replace("@", "<br>@");
							msg += temp + "<hr/>";
						}
						drMsg.put("Msg",msg);
						dtAlert.Rows.add(drMsg);
					}
					break;
				default:
					break;
			}

				///#endregion
			
			// #region 增加流程节点表单绑定信息.
             if (nd.getHisFormType() == NodeFormType.RefOneFrmTree)
             {
                 /* 独立流程节点表单. */

                 FrmNode fn = new FrmNode();
                 fn.setMyPK(nd.getNodeFrmID() + "_" + nd.getNodeID() + "_" + nd.getFK_Flow());
                 fn.Retrieve();
                 myds.Tables.add(fn.ToDataTableField("FrmNode"));
             }
            // #endregion 增加流程节点表单绑定信息.

			myds.Tables.add(dtAlert);
			return myds;
		}
		catch (RuntimeException ex)
		{
			Log.DebugWriteError(ex.getStackTrace()+ex.getMessage());
			throw new RuntimeException(ex.getMessage());
		}
	}
	/** 
	 产生一个WorkNode
	 
	 @param fk_flow 流程编号
	 @param fk_node 节点ID
	 @param workID 工作ID
	 @param fid FID
	 @param userNo 用户编号
	 @return 返回dataset
	*/
	public static DataSet GenerWorkNodeForAndroid(String fk_flow, int fk_node, long workID, long fid, String userNo)
	{
		if (fk_node == 0)
		{
			fk_node = Integer.parseInt(fk_flow + "01");
		}

		if (workID == 0)
		{
			workID = BP.WF.Dev2Interface.Node_CreateBlankWork(fk_flow, null, null, userNo, null,0,0,null,0,null,0,null);
		}

		try
		{
			Emp emp = new Emp(userNo);
			BP.Web.WebUser.SignInOfGener(emp);

			MapData md = new MapData();
			md.setNo("ND" + fk_node);
			if (md.RetrieveFromDBSources() == 0)
			{
				throw new RuntimeException("装载错误，该表单ID=" + md.getNo() + "丢失，请修复一次流程重新加载一次.");
			}

			//表单模版.
			DataSet myds = BP.Sys.CCFormAPI.GenerHisDataSet(md.getNo());
			return myds;

			/*
				///#region 流程设置信息.
			Node nd = new Node(fk_node);

			if (nd.getIsStartNode() == false)
			{
				BP.WF.Dev2Interface.Node_SetWorkRead(fk_node, workID);
			}

			// 节点数据.
			String sql = "SELECT * FROM WF_Node WHERE NodeID=" + fk_node;
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "WF_NodeBar";
			myds.Tables.add(dt);

			// 流程数据.
			Flow fl = new Flow(fk_flow);
			myds.Tables.add(fl.ToDataTableField("WF_Flow"));

				///#endregion 流程设置信息.


				///#region 把主从表数据放入里面.
			//.工作数据放里面去, 放进去前执行一次装载前填充事件.
			BP.WF.Work wk = nd.getHisWork();
			wk.setOID(workID);
			wk.RetrieveFromDBSources();

			// 处理传递过来的参数。
//			for (String k : System.Web.HttpContext.Current.Request.QueryString.AllKeys)
//			{
//				wk.SetValByKey(k, System.Web.HttpContext.Current.Request.QueryString[k]);
//			}
			Enumeration enu = ContextHolderUtils.getRequest().getParameterNames();
			while (enu.hasMoreElements()){
				String k = (String) enu.nextElement();
				wk.SetValByKey(k,ContextHolderUtils.getRequest().getParameter(k));
			}

			// 执行一次装载前填充.
			String msg = md.getFrmEvents().DoEventNode(FrmEventList.FrmLoadBefore, wk);
			if (DotNetToJavaStringHelper.isNullOrEmpty(msg) == false)
			{
				throw new RuntimeException("错误:" + msg);
			}

			wk.ResetDefaultVal();
			myds.Tables.add(wk.ToDataTableField(md.getNo()));

			//把附件的数据放入.
			if (md.getFrmAttachments().size() > 0)
			{
				sql = "SELECT * FROM Sys_FrmAttachmentDB where RefPKVal=" + workID + " AND FK_MapData='ND" + fk_node + "'";
				dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
				dt.TableName = "Sys_FrmAttachmentDB";
				myds.Tables.add(dt);
			}
			// 图片附件数据放入
			if (md.getFrmImgAths().size() > 0)
			{
				sql = "SELECT * FROM Sys_FrmImgAthDB where RefPKVal=" + workID + " AND FK_MapData='ND" + fk_node + "'";
				dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
				dt.TableName = "Sys_FrmImgAthDB";
				myds.Tables.add(dt);
			}

			//把从表的数据放入.
			if (md.getMapDtls().size() > 0)
			{
				for (MapDtl dtl : md.getMapDtls().ToJavaList())
				{
					GEDtls dtls = new GEDtls(dtl.getNo());
					QueryObject qo = null;
					try
					{
						qo = new QueryObject(dtls);
						switch (dtl.getDtlOpenType())
						{
							case ForEmp: // 按人员来控制.
								qo.AddWhere(GEDtlAttr.RefPK, workID);
								qo.addAnd();
								qo.AddWhere(GEDtlAttr.Rec, WebUser.getNo());
								break;
							case ForWorkID: // 按工作ID来控制
								qo.AddWhere(GEDtlAttr.RefPK, workID);
								break;
							case ForFID: // 按流程ID来控制.
								qo.AddWhere(GEDtlAttr.FID, workID);
								break;
						}
					}
					catch (java.lang.Exception e)
					{
						dtls.getGetNewEntity().CheckPhysicsTable();
					}
					DataTable dtDtl = qo.DoQueryToTable();

					// 为明细表设置默认值.
					MapAttrs dtlAttrs = new MapAttrs(dtl.getNo());
					for (MapAttr attr : dtlAttrs.ToJavaList())
					{
						//处理它的默认值.
						if (attr.getDefValReal().contains("@") == false)
						{
							continue;
						}

						for (DataRow dr : dtDtl.Rows)
						{
							dr.put(attr.getKeyOfEn(),attr.getDefVal());
						}
					}

					dtDtl.TableName = dtl.getNo(); //修改明细表的名称.
					myds.Tables.add(dtDtl); //加入这个明细表, 如果没有数据，xml体现为空.
				}
			}

				///#endregion


				///#region 把外键表加入DataSet
			//DataTable dtMapAttr = myds.Tables["Sys_MapAttr"];
			DataTable dtMapAttr = null;
			for ( DataTable dta : myds.Tables)
			{
				if("Sys_MapAttr".equals(dta.getTableName()))
				{
					dtMapAttr = dta;
				}
			}
			for (DataRow dr : dtMapAttr.Rows)
			{
				String lgType = dr.get("LGType").toString();
				if (!lgType.equals("2"))
				{
					continue;
				}

				String UIIsEnable = dr.get("UIIsEnable").toString();
				if (UIIsEnable.equals("0"))
				{
					continue;
				}

				String uiBindKey = dr.get("UIBindKey").toString();
				if (DotNetToJavaStringHelper.isNullOrEmpty(uiBindKey) == true)
				{
					String myPK = dr.get("MyPK").toString();
					//如果是空的
					throw new RuntimeException("@属性字段数据不完整，流程:" + fl.getNo() + fl.getName() + ",节点:" + nd.getNodeID() + nd.getName() + ",属性:" + myPK + ",的UIBindKey IsNull ");
				}

				// 判断是否存在.
				if (myds.Tables.contains(uiBindKey) == true)
				{
					continue;
				}

				myds.Tables.add(BP.Sys.PubClass.GetDataTableByUIBineKey(uiBindKey));
			}

				///#endregion End把外键表加入DataSet


				///#region 把流程信息放入里面.
			//把流程信息表发送过去.
			GenerWorkFlow gwf = new GenerWorkFlow();
			gwf.setWorkID(workID);
			gwf.RetrieveFromDBSources();

			myds.Tables.add(gwf.ToDataTableField("WF_GenerWorkFlow"));

			if (gwf.getWFState() == WFState.Shift)
			{
				//如果是转发.
				BP.WF.ShiftWorks fws = new ShiftWorks();
				fws.Retrieve(ShiftWorkAttr.WorkID, workID, ShiftWorkAttr.FK_Node, fk_node);
				myds.Tables.add(fws.ToDataTableField("WF_ShiftWork"));
			}

			if (gwf.getWFState() == WFState.ReturnSta)
			{
				//如果是退回.
				ReturnWorks rts = new ReturnWorks();
				rts.Retrieve(ReturnWorkAttr.WorkID, workID, ReturnWorkAttr.ReturnToNode, fk_node, ReturnWorkAttr.RDT);
				myds.Tables.add(rts.ToDataTableField("WF_ReturnWork"));
			}

			if (gwf.getWFState() == WFState.HungUp)
			{
				//如果是挂起.
				HungUps hups = new HungUps();
				hups.Retrieve(HungUpAttr.WorkID, workID, HungUpAttr.FK_Node, fk_node);
				myds.Tables.add(hups.ToDataTableField("WF_HungUp"));
			}

			//if (gwf.WFState == WFState.Askfor)
			//{
			//    //如果是加签.
			//    BP.WF.ShiftWorks fws = new ShiftWorks();
			//    fws.Retrieve(ShiftWorkAttr.WorkID, workID, ShiftWorkAttr.FK_Node, fk_node);
			//    myds.Tables.Add(fws.ToDataTableField("WF_ShiftWork"));
			//}

			long wfid = workID;
			if (fid != 0)
			{
				wfid = fid;
			}


			//放入track信息.
			Paras ps = new Paras();
			ps.SQL = "SELECT * FROM ND" + Integer.parseInt(fk_flow) + "Track WHERE WorkID=" + BP.Sys.SystemConfig.getAppCenterDBVarStr() + "WorkID";
			ps.Add("WorkID", wfid);
			DataTable dtNode = DBAccess.RunSQLReturnTable(ps);
			dtNode.TableName = "Track";
			myds.Tables.add(dtNode);

			//工作人员列表，用于审核组件.
			ps = new Paras();
			ps.SQL = "SELECT * FROM  WF_GenerWorkerlist WHERE WorkID=" + BP.Sys.SystemConfig.getAppCenterDBVarStr() + "WorkID";
			ps.Add("WorkID", wfid);
			DataTable dtGenerWorkerlist = DBAccess.RunSQLReturnTable(ps);
			dtGenerWorkerlist.TableName = "WF_GenerWorkerlist";
			myds.Tables.add(dtGenerWorkerlist);

			//放入CCList信息. 用于审核组件.
			ps = new Paras();
			ps.SQL = "SELECT * FROM WF_CCList WHERE WorkID=" + BP.Sys.SystemConfig.getAppCenterDBVarStr() + "WorkID";
			ps.Add("WorkID", wfid);
			DataTable dtCCList = DBAccess.RunSQLReturnTable(ps);
			dtCCList.TableName = "WF_CCList";
			myds.Tables.add(dtCCList);

			//放入WF_SelectAccper信息. 用于审核组件.
			ps = new Paras();
			ps.SQL = "SELECT * FROM WF_SelectAccper WHERE WorkID=" + BP.Sys.SystemConfig.getAppCenterDBVarStr() + "WorkID";
			ps.Add("WorkID", wfid);
			DataTable dtSelectAccper = DBAccess.RunSQLReturnTable(ps);
			dtSelectAccper.TableName = "WF_SelectAccper";
			myds.Tables.add(dtSelectAccper);

			//放入所有的节点信息. 用于审核组件.
			ps = new Paras();
			ps.SQL = "SELECT * FROM WF_Node WHERE FK_Flow=" + BP.Sys.SystemConfig.getAppCenterDBVarStr() + "FK_Flow ORDER BY " + NodeAttr.Step;
			ps.Add("FK_Flow", fk_flow);
			DataTable dtNodes = DBAccess.RunSQLReturnTable(ps);
			dtNodes.TableName = "Nodes";
			myds.Tables.add(dtNodes);


				///#endregion 把流程信息放入里面.

			return myds;
			*/
		}
		catch (RuntimeException ex)
		{
			Log.DebugWriteError(ex.getStackTrace()+"  Msg:"+ex.getMessage());
			throw new RuntimeException(ex.getMessage());
		}
	}
}