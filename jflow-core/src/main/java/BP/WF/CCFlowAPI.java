package BP.WF;

import java.util.Enumeration;

import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.DA.Log;
import BP.En.Entity;
import BP.En.QueryObject;
import BP.Port.Emp;
import BP.Sys.AthCtrlWay;
import BP.Sys.AthUploadWay;
import BP.Sys.EventListOfNode;
import BP.Sys.FrmAttachment;
import BP.Sys.FrmAttachmentAttr;
import BP.Sys.FrmAttachmentDBAttr;
import BP.Sys.FrmAttachments;
import BP.Sys.FrmEventList;
import BP.Sys.FrmImgAthDBs;
import BP.Sys.FrmSubFlowAttr;
import BP.Sys.FrmType;
import BP.Sys.FrmWorkCheckAttr;
import BP.Sys.GroupFieldAttr;
import BP.Sys.GroupFields;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.MapDtlAttr;
import BP.Sys.MapDtls;
import BP.Sys.MapExt;
import BP.Sys.MapExtAttr;
import BP.Sys.MapExtXmlList;
import BP.Sys.MapExts;
import BP.Sys.SysEnumAttr;
import BP.Sys.SysEnums;
import BP.Sys.SystemConfig;
import BP.Tools.ContextHolderUtils;
import BP.WF.Data.GERpt;
import BP.WF.Template.CondModel;
import BP.WF.Template.FTCAttr;
import BP.WF.Template.FrmEleType;
import BP.WF.Template.FrmField;
import BP.WF.Template.FrmFieldAttr;
import BP.WF.Template.FrmFields;
import BP.WF.Template.FrmNode;
import BP.WF.Template.FrmNodeAttr;
import BP.WF.Template.FrmNodeComponent;
import BP.WF.Template.FrmSln;
import BP.WF.Template.FrmThreadAttr;
import BP.WF.Template.FrmTrackAttr;
import BP.WF.Template.NodeYGFlow;
import BP.WF.Template.NodeYGFlows;
import BP.Web.WebUser;

public class CCFlowAPI {
	/**
	 * 产生一个WorkNode 区分大小写
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param fk_node
	 *            节点ID
	 * @param workID
	 *            工作ID
	 * @param fid
	 *            FID
	 * @param userNo
	 *            用户编号
	 * @return 返回dataset
	 * @throws Exception
	 */
	public static DataSet GenerWorkNode(String fk_flow, int fk_node, long workID, long fid, String userNo, String fromWorkOpt)
			throws Exception {

		// 让其登录. 
		if (userNo.equals(WebUser.getNo()) == false) {
			Emp emp = new Emp(userNo);
			BP.Web.WebUser.SignInOfGener(emp);
		}

		// 节点.
		if (fk_node == 0)
			fk_node = Integer.parseInt(fk_flow + "01");

		if (workID == 0)
			workID = BP.WF.Dev2Interface.Node_CreateBlankWork(fk_flow, null, null, userNo, null, 0, 0, null, 0, null, 0,
					null);

		Node nd = new Node(fk_node);
		nd.WorkID = workID; // 为获得表单id，设置的参数.
		try {

			MapData md = new MapData();
			md.setNo(nd.getNodeFrmID());
			md.setName(nd.getName());
			if (md.RetrieveFromDBSources() == 0)
				throw new RuntimeException("装载错误，该表单ID=" + md.getNo() + "丢失，请修复一次流程重新加载一次.");

			Work wk = nd.getHisWork();
			wk.setOID(workID);
			wk.RetrieveFromDBSources();
			wk.ResetDefaultVal();

			// 第1.2: 调用,处理用户定义的业务逻辑.
			String sendWhen = nd.getHisFlow().DoFlowEventEntity(EventListOfNode.FrmLoadBefore, nd, wk, null);

			// 获得表单模版.
			DataSet myds = BP.Sys.CCFormAPI.GenerHisDataSet(md.getNo(), nd.getName());
			
			 //获取表单的mapAttr
            //求出集合.
			myds.Tables.remove("Sys_MapAttr"); //移除
            MapAttrs mattrs = new MapAttrs(md.getNo());
            if (fk_node != 0)
            {
                /*处理表单权限控制方案*/
                FrmNode frmNode = new FrmNode();
                int count = frmNode.Retrieve(FrmNodeAttr.FK_Frm, md.getNo(), FrmNodeAttr.FK_Node, fk_node);
                if (count != 0 && frmNode.getFrmSlnInt() != 0)
                {

                    FrmFields fls = new FrmFields(md.getNo(), frmNode.getFK_Node());

                    for(FrmField item : fls.ToJavaList())
                    {
                        for (MapAttr attr : mattrs.ToJavaList())
                        {
                            if (attr.getKeyOfEn().equals(item.getKeyOfEn()) == false)
                                continue;

                            if (item.getIsSigan())
                                item.setUIIsEnable(false);
                           
                            attr.setUIIsInput(item.getIsNotNull());
                            attr.setUIIsEnable(item.getUIIsEnable());
                            attr.setUIVisible(item.getUIVisible());
                            attr.setIsSigan(item.getIsSigan());
                            attr.setDefValReal(item.getDefVal());
                            String myval = wk.GetValStringByKey(attr.getKeyOfEn());
                            if(DataType.IsNullOrEmpty(item.getDefVal()) == false && DataType.IsNullOrEmpty(myval) == true && item.getDefVal().indexOf("@")!=0)
                            		wk.SetValByKey(attr.getKeyOfEn(),item.getDefVal());
                        }
                    }
                }
            }

            DataTable Sys_MapAttr = mattrs.ToDataTableField("Sys_MapAttr");
            myds.Tables.add(Sys_MapAttr);

			// 把流程信息表发送过去.
			GenerWorkFlow gwf = new GenerWorkFlow();
			gwf.setWorkID(workID);
			gwf.RetrieveFromDBSources();
            myds.Tables.add(gwf.ToDataTableField("WF_GenerWorkFlow"));

			// 加入WF_Node.
			DataTable WF_Node = nd.ToDataTableField("WF_Node");
			myds.Tables.add(WF_Node);

			// #region 加入组件的状态信息, 在解析表单的时候使用.
			FrmNodeComponent fnc = new FrmNodeComponent(nd.getNodeID());

			if (nd.getNodeFrmID() != "ND" + nd.getNodeID() && nd.getHisFormType() != NodeFormType.RefOneFrmTree) {
				/* 说明这是引用到了其他节点的表单，就需要把一些位置元素修改掉. */
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

				// #region 没有审核组件分组就增加上审核组件分组. @杜需要翻译&测试.
				if (md.getHisFrmType() == FrmType.FoolForm) {
					// 判断是否是傻瓜表单，如果是，就要判断该傻瓜表单是否有审核组件groupfield ,没有的话就增加上.
					DataTable gf = null;
					for (DataTable dtb : myds.Tables) {
						if ("Sys_GroupField".equals(dtb.getTableName())) {
							gf = dtb;
						}
					}

					boolean isHave = false;
					for (DataRow dr : gf.Rows) {
						String cType = (String) dr.get("CtrlType");
						if (cType == null)
							continue;

						if (cType.equals("FWC") == true)
							isHave = true;
					}

					if (isHave == false) {

						DataRow dr = gf.NewRow();

						dr.put(GroupFieldAttr.OID, 100);
						dr.put(GroupFieldAttr.EnName, nd.getNodeFrmID());
						dr.put(GroupFieldAttr.CtrlType, "FWC");
						dr.put(GroupFieldAttr.CtrlID, "FWCND" + nd.getNodeID());
						dr.put(GroupFieldAttr.Idx, 100);
						dr.put(GroupFieldAttr.Lab, "审核信息");
						gf.Rows.add(dr);

						myds.Tables.remove("Sys_GroupField");
						myds.Tables.add(gf);
					}
				}
				// #endregion 没有审核组件分组就增加上审核组件分组.
			}

			myds.Tables.add(fnc.ToDataTableField("WF_FrmNodeComponent"));

			// #endregion 加入组件的状态信息, 在解析表单的时候使用.

			if (nd.getFormType() == NodeFormType.FoolTruck && nd.getIsStartNode() == false
					&& DataType.IsNullOrEmpty(wk.HisPassedFrmIDs) == false) {

				// #region 处理字段分组排序.
				// 查询所有的分组.

				 //查询所有的分组, 如果是查看表单的方式，就不应该把当前的表单显示出来.
                String myFrmIDs = ""; 
                if (fromWorkOpt.equals("1") == true)
                {
                    if (gwf.getWFState() == WFState.Complete)
                        myFrmIDs = wk.HisPassedFrmIDs + ",'ND" + fk_node + "'";
                    else
                        myFrmIDs = wk.HisPassedFrmIDs; //流程未完成并且是查看表单的情况.
                }
                else
                {
                    myFrmIDs = wk.HisPassedFrmIDs + ",'ND" + fk_node + "'";
                }
                
				
				
				
				GroupFields gfs = new GroupFields();
				gfs.RetrieveIn(GroupFieldAttr.FrmID, "(" + myFrmIDs + ")");

				// 按照时间的顺序查找出来 ids .
				String sqlOrder = "SELECT OID FROM  Sys_GroupField WHERE   FrmID IN (" + myFrmIDs + ")";
				if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.Oracle) {
					myFrmIDs = myFrmIDs.replace("'", "");
					sqlOrder += " ORDER BY INSTR('" + myFrmIDs + "', FrmID) , Idx";
				}

				if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.MSSQL) {
					myFrmIDs = myFrmIDs.replace("'", "");
					sqlOrder += " ORDER BY CHARINDEX(FrmID, '" + myFrmIDs + "'), Idx";
				}

				if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.MySQL) {
					myFrmIDs = myFrmIDs.replace("'", "");
					sqlOrder += " ORDER BY INSTR('" + myFrmIDs + "',FrmID), Idx";
				}

				DataTable dtOrder = DBAccess.RunSQLReturnTable(sqlOrder);

				// 创建容器,把排序的分组放入这个容器.
				GroupFields gfsNew = new GroupFields();

				// 遍历查询出来的分组.
				for (DataRow dr : dtOrder.Rows) {
					String pkOID = dr.getValue(0).toString();
					Entity mygf = gfs.GetEntityByKey(pkOID);
					gfsNew.AddEntity(mygf); // 把分组字段加入里面去.
				}

				DataTable dtGF = gfsNew.ToDataTableField("Sys_GroupField");
				myds.Tables.remove("Sys_GroupField");
				myds.Tables.add(dtGF);
				// #endregion 处理字段分组排序.

				// #region 处理 mapattrs

				// 求当前表单的字段集合.
				MapAttrs attrs = new MapAttrs();
				QueryObject qo = new QueryObject(attrs);
				qo.AddWhere(MapAttrAttr.FK_MapData, "ND" + nd.getNodeID());
				qo.addOrderBy(MapAttrAttr.Idx);
				qo.DoQuery();

				 //获取走过节点的表单方案
                MapAttrs attrsLeiJia = new MapAttrs();

                //存在表单方案只读
                String sql1 = "Select FK_Frm From WF_FrmNode Where FK_Frm In("+wk.HisPassedFrmIDs+") And FrmSln="+FrmSln.Readonly.getValue() +" And FK_Node="+nd.getNodeID();
                DataTable dt1 = DBAccess.RunSQLReturnTable(sql1);
                if(dt1.Rows.size() > 0){
                    //获取节点
                    String nodes ="";
                    for(DataRow dr : dt1.Rows)
                        nodes+="'"+dr.getValue(0).toString()+"',";

                    nodes = nodes.substring(0,nodes.length()-1);
                    qo = new QueryObject(attrsLeiJia);
                    qo.AddWhere(MapAttrAttr.FK_MapData, " IN ", "(" +nodes + ")");
                    qo.addOrderBy(MapAttrAttr.Idx);
                    qo.DoQuery();

                    for(MapAttr item : attrsLeiJia.ToJavaList()){
                        if (item.getKeyOfEn().equals("RDT") || item.getKeyOfEn().equals("Rec"))
                            continue;
                        item.setUIIsEnable(false); //设置为只读的.
                        attrs.AddEntity(item);
                    }

                }

                //存在表单方案默认
                sql1 = "Select FK_Frm From WF_FrmNode Where FK_Frm In(" + wk.HisPassedFrmIDs + ") And FrmSln=" + FrmSln.Default.getValue() + " And FK_Node=" + nd.getNodeID();
                dt1 = DBAccess.RunSQLReturnTable(sql1);
                 if(dt1.Rows.size() > 0){
                     //获取节点
                    String nodes ="";
                    for(DataRow dr : dt1.Rows)
                        nodes+="'"+dr.get(0).toString()+"',";

                    nodes = nodes.substring(0,nodes.length()-1);
                    qo = new QueryObject(attrsLeiJia);
                    qo.AddWhere(MapAttrAttr.FK_MapData, " IN ", "(" +nodes + ")");
                    qo.addOrderBy(MapAttrAttr.Idx);
                    qo.DoQuery();

                    for(MapAttr item : attrsLeiJia.ToJavaList())
                    {
                        if (item.getKeyOfEn().equals("RDT") || item.getKeyOfEn().equals("Rec"))
                            continue;
                        attrs.AddEntity(item);
                    }

                }

                //存在表单方案自定义
                 sql1 = "Select FK_Frm From WF_FrmNode Where FK_Frm In(" + wk.HisPassedFrmIDs + ") And FrmSln=" + FrmSln.Self.getValue() + " And FK_Node=" + nd.getNodeID();
                dt1 = DBAccess.RunSQLReturnTable(sql1);

                 if(dt1.Rows.size() > 0){
                     //获取节点
                    String nodes ="";
                    for(DataRow dr : dt1.Rows)
                        nodes+="'"+dr.get(0).toString()+"',";

                    nodes = nodes.substring(0,nodes.length()-1);
                    qo = new QueryObject(attrsLeiJia);
                    qo.AddWhere(MapAttrAttr.FK_MapData, " IN ", "(" +nodes + ")");
                    qo.addOrderBy(MapAttrAttr.Idx);
                    qo.DoQuery();

                     //获取累加表单的权限
                    FrmFields fls = new FrmFields();
                    qo = new QueryObject(fls);
                    qo.AddWhere(FrmFieldAttr.FK_MapData, " IN ", "(" + nodes + ")");
                    qo.addAnd();
                    qo.AddWhere(FrmFieldAttr.EleType, FrmEleType.Field);
                    qo.addAnd();
                    qo.AddWhere(FrmFieldAttr.FK_Node, nd.getNodeID());
                    
                    qo.DoQuery();

                    for(MapAttr attr : attrsLeiJia.ToJavaList())
                    {
                        if (attr.getKeyOfEn().equals("RDT") || attr.getKeyOfEn().equals("Rec"))
                            continue;

                        FrmField frmField = null;
                        for(FrmField item : fls.ToJavaList())
                        {
                            if (attr.getKeyOfEn() == item.getKeyOfEn())
                            {
                                frmField = item;
                                break;
                            }
                        }
                        if (frmField != null)
                        {
                            if (frmField.getIsSigan())
                                attr.setUIIsEnable(false);

                            attr.setUIIsEnable(frmField.getUIIsEnable());
                            attr.setUIVisible(frmField.getUIVisible());
                            attr.setIsSigan(frmField.getIsSigan());
                            attr.setDefValReal(frmField.getDefVal());
                        }
                        attrs.AddEntity(attr);
                    }

                }
			
            
				// 替换掉现有的.
				Sys_MapAttr = myds.GetTableByName("Sys_MapAttr");
				myds.Tables.remove("Sys_MapAttr");
				myds.Tables.remove(Sys_MapAttr);

				myds.Tables.add(attrs.ToDataTableField("Sys_MapAttr")); // 增加.
					
				// #endregion 处理mapattrs

				// 计算累加的枚举类型
				DataTable Sys_Menu = myds.GetTableByName("Sys_Enum");
				myds.Tables.remove("Sys_Enum");
				myds.Tables.remove(Sys_Menu);

				// 把枚举放入里面去.
				myFrmIDs = wk.HisPassedFrmIDs + ",'ND" + fk_node + "'";
				SysEnums enums = new SysEnums();
				enums.RetrieveInSQL(SysEnumAttr.EnumKey,
						"SELECT UIBindKey FROM Sys_MapAttr WHERE FK_MapData in(" + myFrmIDs + ")");


				// 加入最新的枚举.
				myds.Tables.add(enums.ToDataTableField("Sys_Enum"));

				// 移除原来的MapExt.
				DataTable dtExts = myds.GetTableByName("Sys_MapExt");
				myds.Tables.remove("Sys_MapExt");
				myds.Tables.remove(dtExts);

				// 把扩展放入里面去.
				myFrmIDs = wk.HisPassedFrmIDs + ",'ND" + fk_node + "'";
				BP.Sys.MapExts exts = new MapExts();
				qo = new QueryObject(exts);
				qo.AddWhere(MapExtAttr.FK_MapData, " IN ", "(" + myFrmIDs + ")");
				qo.DoQuery();


				// 加入最新的MapExt.
				myds.Tables.add(exts.ToDataTableField("Sys_MapExt"));
				
				//#region  MapDtl 
				DataTable dtDtls = myds.GetTableByName("Sys_MapDtl");
				myds.Tables.remove("Sys_MapDtl");
				myds.Tables.remove(dtDtls);
 
                //把从表放里面
                myFrmIDs = wk.HisPassedFrmIDs + ",'ND" + fk_node + "'";
                BP.Sys.MapDtls dtls = new MapDtls();
                qo = new QueryObject(dtls);
                qo.AddWhere(MapDtlAttr.FK_MapData, " IN ", "(" + myFrmIDs + ")");
                qo.addAnd();
                qo.AddWhere(MapDtlAttr.FK_Node, 0);
                qo.DoQuery();

                // 加入最新的MapDtl.
                myds.Tables.add(dtls.ToDataTableField("Sys_MapDtl"));
                //#endregion  MapDtl .


                DataTable dtFrmAths = myds.GetTableByName("Sys_FrmAttachment");
				myds.Tables.remove("Sys_FrmAttachment");
				myds.Tables.remove(dtFrmAths);

                //把附件放里面
                myFrmIDs = wk.HisPassedFrmIDs + ",'ND" + fk_node + "'";
                BP.Sys.FrmAttachment frmAtchs = new FrmAttachment();
                qo = new QueryObject(frmAtchs);
                qo.AddWhere(FrmAttachmentAttr.FK_MapData, " IN ", "(" + myFrmIDs + ")");
                qo.addAnd();
                qo.AddWhere(FrmAttachmentAttr.FK_Node, 0);
                qo.DoQuery();

                // 加入最新的Sys_FrmAttachment.
                myds.Tables.add(frmAtchs.ToDataTableField("Sys_FrmAttachment"));

				

			}

			// #region 流程设置信息.
			BP.WF.Dev2Interface.Node_SetWorkRead(fk_node, workID);
			if(nd.getIsStartNode() == false){
				if (gwf.getTodoEmps().contains(BP.Web.WebUser.getName() + ";") == false)
                {
                    gwf.setTodoEmps(gwf.getTodoEmps() +BP.Web.WebUser.getNo() + "," + BP.Web.WebUser.getName());
                    gwf.Update();
                }
			}

			// 增加转向下拉框数据.
			if (nd.getCondModel() == CondModel.SendButtonSileSelect) {

				if (nd.getIsStartNode() == true || gwf.getTodoEmps().contains(WebUser.getNo() + ",") == true) {

					// 如果当前节点，是可以显示下拉框的.
					Nodes nds = nd.getHisToNodes();

					DataTable dtToNDs = new DataTable();
					dtToNDs.TableName = "ToNodes";
					dtToNDs.Columns.Add("No", String.class);
					dtToNDs.Columns.Add("Name", String.class);
					dtToNDs.Columns.Add("IsSelectEmps", String.class);

					for (Node item : nds.ToJavaList()) {
						DataRow dr = dtToNDs.NewRow();
						dr.setValue("No", item.getNodeID());
						dr.setValue("Name", item.getName());

						if (item.getHisDeliveryWay() == DeliveryWay.BySelected) {
							dr.setValue("IsSelectEmps", "1");
						} else {
							dr.setValue("IsSelectEmps", "0"); // 是不是，可以选择接受人.
						}
						dtToNDs.Rows.add(dr);
					}

					// 增加到达延续子流程节点.
					NodeYGFlows ygflows = new NodeYGFlows(String.valueOf(fk_node));

					if (ygflows.size() > 1)
						dtToNDs.Rows.clear(); // 为浙商银行做的特殊判断，如果配置了延续流程，就不让其走分支节点.

					for (NodeYGFlow item : ygflows.ToJavaList()) {
						DataRow dr = dtToNDs.NewRow();

						dr.setValue("No", item.getFK_Flow() + "01");
						dr.setValue("Name", "启动:" + item.getFlowName());
						dr.setValue("IsSelectEmps", "1");

						dr.put("IsSelected", "0");
						dtToNDs.Rows.add(dr);
					}
					// #endregion 增加到达延续子流程节点。

					// #region 到达其他节点.

					// 上一次选择的节点.
					int defalutSelectedNodeID = 0;
					if (nds.size() > 1) {
						String mysql = "";
						// 找出来上次发送选择的节点.
						if (SystemConfig.getAppCenterDBType() == DBType.MSSQL)
							mysql = "SELECT  top 1 NDTo FROM ND" + Integer.parseInt(nd.getFK_Flow())
									+ "Track A WHERE A.NDFrom=" + fk_node + " AND ActionType=1 ORDER BY WorkID DESC";
						else if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
							mysql = "SELECT * FROM ( SELECT  NDTo FROM ND" + Integer.parseInt(nd.getFK_Flow())
									+ "Track A WHERE A.NDFrom=" + fk_node
									+ " AND ActionType=1 ORDER BY WorkID DESC ) WHERE ROWNUM =1";
						else if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
							mysql = "SELECT  NDTo FROM ND" + Integer.parseInt(nd.getFK_Flow())
									+ "Track A WHERE A.NDFrom=" + fk_node
									+ " AND ActionType=1 ORDER BY WorkID  DESC limit 1,1";

						// 获得上一次发送到的节点.
						defalutSelectedNodeID = DBAccess.RunSQLReturnValInt(mysql, 0);
					}

					// #region 为天业集团做一个特殊的判断.
					if (SystemConfig.getCustomerNo() == "TianYe" && nd.getName().contains("董事长") == true) {
						/* 如果是董事长节点, 如果是下一个节点默认的是备案. */
						for (Node item : nds.ToJavaList()) {
							if (item.getName().contains("备案") == true && item.getName().contains("待") == false) {
								defalutSelectedNodeID = item.getNodeID();
								break;
							}
						}
					}

					// 增加一个下拉框, 对方判断是否有这个数据.
					myds.Tables.add(dtToNDs);
				}
			}
			
			
			// 执行表单事件..
			String msg = md.getFrmEvents().DoEventNode(FrmEventList.FrmLoadBefore, wk);
			if (DotNetToJavaStringHelper.isNullOrEmpty(msg) == false) {
				throw new RuntimeException("err@错误:" + msg);
			}

			// 执行FEE事件.
			String msgOfLoad = nd.getHisFlow().DoFlowEventEntity(EventListOfNode.FrmLoadBefore, nd, wk, null);
			if (msgOfLoad != null)
				wk.RetrieveFromDBSources();
			
			Enumeration enu = ContextHolderUtils.getRequest().getParameterNames();
			while (enu.hasMoreElements()) {
				String k = (String) enu.nextElement();
				wk.SetValByKey(k, ContextHolderUtils.getRequest().getParameter(k));
			}


			// 执行装载填充.
			MapExt me = new MapExt();
			me.setMyPK(MapExtXmlList.PageLoadFull + "_" +wk.NodeFrmID );
			if (me.RetrieveFromDBSources() == 1) {
				// 执行通用的装载方法.
				MapAttrs attrs = new MapAttrs("ND" + fk_node);
				MapDtls dtls = new MapDtls("ND" + fk_node);
				Entity tempVar = BP.WF.Glo.DealPageLoadFull(wk, me, attrs, dtls);
				wk = (Work) ((tempVar instanceof Work) ? tempVar : null);
			}

			// 需要放到这里，不然无法转换出去.
			if( nd.getHisFormType() == NodeFormType.RefOneFrmTree)
				wk.ResetDefaultVal(md.getNo(),nd.getFK_Flow(),nd.getNodeID(),mattrs);
			else
				wk.ResetDefaultVal();

			// 如果是累加表单，就把整个rpt数据都放入里面去.
			if (nd.getFormType() == NodeFormType.FoolTruck && nd.getIsStartNode() == false
					&& DataType.IsNullOrEmpty(wk.HisPassedFrmIDs) == false) {

				  GERpt rpt = new GERpt("ND" + Integer.parseInt(nd.getFK_Flow()) + "Rpt", workID); // nd.HisFlow.HisGERpt;
				  DataTable dt = rpt.ToDataTableField("MainTable");
                  DataTable wkdt = wk.ToDataTableField(md.getNo());
                  //把当前节点的数据 覆盖rpt表中的数据
                  for(DataColumn column : wkdt.Columns)
                  {
                       for(DataColumn column1 : dt.Columns){
                           if(column.ColumnName.equals(column1.ColumnName)){
                               dt.Rows.get(0).setValue(column1.ColumnName, wkdt.Rows.get(0).getValue(column.ColumnName));
                               break;
                           }
                       }
                  }
                  
				myds.Tables.add(dt);
				

			} else {
				
				DataTable mainTable = wk.ToDataTableField(md.getNo());
				mainTable.TableName = "MainTable";
				myds.Tables.add(mainTable);
			}

			String sql = "";
			DataTable dt = null;

			// #region 图片附件
			FrmImgAthDBs imgAthDBs = new FrmImgAthDBs(nd.getNodeFrmID(), String.valueOf(workID));
			if (imgAthDBs != null && imgAthDBs.size() > 0) {
				DataTable dt_ImgAth = imgAthDBs.ToDataTableField("Sys_FrmImgAthDB");
				myds.Tables.add(dt_ImgAth);
			}
			// #endregion
			// #region 增加附件信息.
			FrmAttachments athDescs = new FrmAttachments();
			athDescs.Retrieve(FrmAttachmentAttr.FK_MapData, nd.getNodeFrmID());
			if (athDescs.size() != 0) {
				FrmAttachment athDesc = (FrmAttachment) athDescs.get(0);

				// 查询出来数据实体.
				BP.Sys.FrmAttachmentDBs dbs = new BP.Sys.FrmAttachmentDBs();
				if (athDesc.getHisCtrlWay() == AthCtrlWay.PWorkID) {
					String pWorkID = String.valueOf(BP.DA.DBAccess
							.RunSQLReturnValInt("SELECT PWorkID FROM WF_GenerWorkFlow WHERE WorkID=" + workID, 0));
					if (pWorkID == null || pWorkID.equals("0") == true)
						pWorkID = String.valueOf(workID);

					if (athDesc.getAthUploadWay() == AthUploadWay.Inherit) {
						/* 继承模式 */
						BP.En.QueryObject qo = new BP.En.QueryObject(dbs);
						qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, pWorkID);
						qo.addOr();
						qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, workID);
						qo.addOrderBy("RDT");
						qo.DoQuery();
					}

					if (athDesc.getAthUploadWay() == AthUploadWay.Interwork) {
						/* 共享模式 */
						dbs.Retrieve(FrmAttachmentDBAttr.RefPKVal, pWorkID);
					}
				} else if (athDesc.getHisCtrlWay() == AthCtrlWay.WorkID) {
					/* 继承模式 */
					BP.En.QueryObject qo = new BP.En.QueryObject(dbs);
					qo.AddWhere(FrmAttachmentDBAttr.NoOfObj, athDesc.getNoOfObj());
					qo.addAnd();
					qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, workID);
					qo.addOrderBy("RDT");
					qo.DoQuery();
				}

				// 增加一个数据源.
				myds.Tables.add(dbs.ToDataTableField("Sys_FrmAttachmentDB"));
			}


			DataTable dtMapAttr = null;
			
			for (DataTable dtb : myds.Tables) {
				if ("Sys_MapAttr".equals(dtb.getTableName())) {
					dtMapAttr = dtb;
				}
			}

			MapExts mes = md.getMapExts();

			for (DataRow dr : dtMapAttr.Rows) {

				String lgType = dr.getValue("LGType").toString();
				String uiBindKey = dr.getValue("UIBindKey").toString();

				if (DataType.IsNullOrEmpty(uiBindKey) == true)
					continue; // 为空就continue.

				if (lgType.equals("1") == true)
					continue; // 枚举值就continue;

				String uiIsEnable = dr.getValue("UIIsEnable").toString();
				if (uiIsEnable.equals("0") == true && lgType.equals("1") == true)
					continue; // 如果是外键，并且是不可以编辑的状态.

				if (uiIsEnable.equals("0") == true && lgType.equals("0") == true)
					continue; // 如果是外部数据源，并且是不可以编辑的状态.

				// 检查是否有下拉框自动填充。
				String keyOfEn = dr.getValue("KeyOfEn").toString();
				
				/// #region 处理下拉框数据范围. for 小杨.
				Object tempVar2 = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL,
						MapExtAttr.AttrOfOper, keyOfEn);
				me = (MapExt) ((tempVar2 instanceof MapExt) ? tempVar2 : null);
				if (me != null && myds.Tables.contains(keyOfEn) == false) {
					Object tempVar3 = me.getDoc();
					String fullSQL = (String) ((tempVar3 instanceof String) ? tempVar3 : null);
					fullSQL = fullSQL.replace("~", ",");
					fullSQL = BP.WF.Glo.DealExp(fullSQL, wk, null);
					dt = DBAccess.RunSQLReturnTable(fullSQL);
					dt.TableName = keyOfEn; // 可能存在隐患，如果多个字段，绑定同一个表，就存在这样的问题.
					myds.Tables.add(dt);
					continue;
				}

				/// #endregion 处理下拉框数据范围.

				// 判断是否存在.
				if (myds.Tables.contains(uiBindKey) == true) {
					continue;
				}
				if(BP.Sys.PubClass.GetDataTableByUIBineKey(uiBindKey)!=null)
				myds.Tables.add(BP.Sys.PubClass.GetDataTableByUIBineKey(uiBindKey));
			}

			/// #endregion End把外键表加入DataSet

			/// #region 处理流程-消息提示.
			DataTable dtAlert = new DataTable();
			dtAlert.TableName = "AlertMsg";
			dtAlert.Columns.Add("Title", String.class);
			dtAlert.Columns.Add("Msg", String.class);
			dtAlert.Columns.Add("URL", String.class);

			// string msg = "";
			switch (gwf.getWFState()) {
			case AskForReplay: // 返回加签的信息.
				String mysql = "SELECT * FROM ND" + Integer.parseInt(fk_flow) + "Track WHERE WorkID=" + workID + " AND "
						+ TrackAttr.ActionType + "=" + ActionType.ForwardAskfor.getValue();
				DataTable mydt = BP.DA.DBAccess.RunSQLReturnTable(mysql);
				for (DataRow dr : mydt.Rows) {
					String msgAskFor = dr.getValue(TrackAttr.Msg).toString();
					String worker = dr.getValue(TrackAttr.EmpFrom).toString();
					String workerName = dr.getValue(TrackAttr.EmpFromT).toString();
					String rdt = dr.getValue(TrackAttr.RDT).toString();

					DataRow drMsg = dtAlert.NewRow();
					drMsg.put("Title", worker + "," + workerName + "回复信息:");
					drMsg.put("Msg", DataType.ParseText2Html(msgAskFor) + "<br>" + rdt);
					dtAlert.Rows.add(drMsg);
				}
				break;
			case Askfor: // 加签.

				sql = "SELECT * FROM ND" + Integer.parseInt(fk_flow) + "Track WHERE WorkID=" + workID + " AND "
						+ TrackAttr.ActionType + "=" + ActionType.AskforHelp.getValue();
				dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
				for (DataRow dr : dt.Rows) {
					String msgAskFor = dr.getValue(TrackAttr.Msg).toString();
					String worker = dr.getValue(TrackAttr.EmpFrom).toString();
					String workerName = dr.getValue(TrackAttr.EmpFromT).toString();
					String rdt = dr.getValue(TrackAttr.RDT).toString();

					DataRow drMsg = dtAlert.NewRow();
					drMsg.put("Title", worker + "," + workerName + "请求加签:");
					drMsg.put("Msg", DataType.ParseText2Html(msgAskFor) + "<br>" + rdt + "<a href='#' onclick='AskForRe("+fk_flow+","+fk_node+","+workID+","+fid+")' >回复加签意见</a>");
					
					dtAlert.Rows.add(drMsg);


				}

				break;
			case ReturnSta:
				// 如果工作节点退回了
				ReturnWorks rws = new ReturnWorks();
				rws.Retrieve(ReturnWorkAttr.ReturnToNode, fk_node, ReturnWorkAttr.WorkID, workID, ReturnWorkAttr.RDT);
				if (rws.size() != 0) {
					String msgInfo = "";
					for (BP.WF.ReturnWork rw : rws.ToJavaList()) {
						DataRow drMsg = dtAlert.NewRow();

						drMsg.put("Title", "来自节点:" + rw.getReturnNodeName() + " 退回人:" + rw.getReturnerName() + "  "
								+ rw.getRDT() + "");
						drMsg.put("Msg", rw.getBeiZhuHtml());
						dtAlert.Rows.add(drMsg);
					}

					String str = nd.getReturnAlert();
					if (!str.equals("")) {
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
						drMsg.put("Title", "退回信息");
						drMsg.put("Msg", str);
						dtAlert.Rows.add(drMsg);
					}
	
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
				if (fws.size() >= 1) {
					DataRow drMsg = dtAlert.NewRow();
					drMsg.put("Title", "移交历史信息");
					msg = "";
					for (ShiftWork fw : fws.ToJavaList()) {
						String temp = "@移交人[" + fw.getFK_Emp() + "," + fw.getFK_EmpName() + "]。@接受人：" + fw.getToEmp()
								+ "," + fw.getToEmpName() + "。<br>移交原因：-------------" + fw.getNoteHtml();
						if (fw.getFK_Emp().equals(WebUser.getNo())) {
							temp = "<b>" + temp + "</b>";
						}

						temp = temp.replace("@", "<br>@");
						msg += temp + "<hr/>";
					}
					drMsg.put("Msg", msg);
					dtAlert.Rows.add(drMsg);
				}
				break;
			default:
				break;
			}

			/// #endregion

			// #region 增加流程节点表单绑定信息.
			if (nd.getHisFormType() == NodeFormType.RefOneFrmTree) {
				/* 独立流程节点表单. */

				FrmNode fn = new FrmNode();
				fn.setMyPK(nd.getNodeFrmID() + "_" + nd.getNodeID() + "_" + nd.getFK_Flow());
				fn.Retrieve();
				myds.Tables.add(fn.ToDataTableField("FrmNode"));
			}
			// #endregion 增加流程节点表单绑定信息.

			myds.Tables.add(dtAlert);
			return myds;
		} catch (RuntimeException ex) {
			Log.DebugWriteError(ex.getStackTrace() + ex.getMessage());
			throw new RuntimeException(ex.getMessage());
		}
	}

}