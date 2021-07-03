package bp.wf;

import java.util.Enumeration;
import bp.da.*;
import bp.difference.ContextHolderUtils;
import bp.difference.SystemConfig;
import bp.sys.*;
import bp.web.*;
import bp.en.*;
import bp.wf.data.*;
import bp.wf.template.*;

/**
 * 流程的API.
 */
public class CCFlowAPI {
	/**
	 * 产生一个WorkNode
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param nd
	 *            节点
	 * @param workID
	 *            工作ID
	 * @param fid
	 *            FID
	 * @param userNo
	 *            用户编号
	 * @return 返回dataset
	 * @throws Exception
	 */

	public static DataSet GenerWorkNode(String fk_flow, Node nd, long workID, long fid, String userNo,
			String fromWorkOpt,long realWorkID) throws Exception {
		return GenerWorkNode(fk_flow, nd, workID, fid, userNo, fromWorkOpt, false,realWorkID);
	}

	public static DataSet GenerWorkNode(String fk_flow, Node nd, long workID, long fid, String userNo,long realWorkID)
			throws Exception {
		return GenerWorkNode(fk_flow, nd, workID, fid, userNo, "0", false,realWorkID);
	}

	public static DataSet GenerWorkNode(String fk_flow, Node nd, long workID, long fid, String userNo,
			String fromWorkOpt, boolean isView,long realWorkID) throws Exception {

		try {
			nd.WorkID = workID; // 为获取表单ID提供参数.

			Work wk = nd.getHisWork();
			wk.setOID(workID);
			wk.RetrieveFromDBSources();

			MapData md = new MapData(nd.getNodeFrmID());

			// 第1.2: 调用,处理用户定义的业务逻辑.
			String sendWhen = ExecEvent.DoNode(EventListNode.FrmLoadBefore, nd, wk, null);

			// 获得表单模版.
			DataSet myds = bp.sys.CCFormAPI.GenerHisDataSet(nd.getNodeFrmID(), nd.getName());

			// 更换表单的名字.
			if (DataType.IsNullOrEmpty(nd.getNodeFrmID()) == false
					&& (nd.getHisFormType() == NodeFormType.FoolForm || nd.getHisFormType() == NodeFormType.FreeForm)) {
				String realName = myds.GetTableByName("Sys_MapData").Rows.get(0).getValue("Name") instanceof String
						? (String) myds.GetTableByName("Sys_MapData").Rows.get(0).getValue("Name") : null;
				if (DataType.IsNullOrEmpty(realName) == true) {
					myds.GetTableByName("Sys_MapData").Rows.get(0).setValue("Name", nd.getName());
				}
			}

			/// 处理表单权限控制方案: 如果是绑定单个表单的时候.
			/* 处理表单权限控制方案: 如果是绑定单个表单的时候. */

			// 这两个变量在累加表单用到.
			FrmNode frmNode = new FrmNode();
			Flow flow = new Flow(fk_flow);
			myds.Tables.add(flow.ToDataTableField("WF_Flow"));

			if (nd.getHisFormType() == NodeFormType.RefOneFrmTree
					|| nd.getHisFormType() == NodeFormType.FoolTruck
					|| flow.getFlowDevModel() == FlowDevModel.JiJian) {
				frmNode.Retrieve(FrmNodeAttr.FK_Frm, nd.getNodeFrmID(), FrmNodeAttr.FK_Node, nd.getNodeID());
				if (DataType.IsNullOrEmpty(frmNode.getMyPK()) == false && frmNode.getFrmSln() != FrmSln.Default) {
					FrmFields fls = new FrmFields(nd.getNodeFrmID(), frmNode.getFK_Node());
					for (FrmField item : fls.ToJavaList()) {
						for (DataRow dr : myds.GetTableByName("Sys_MapAttr").Rows) {
							String keyOfEn = dr.getValue("KeyOfEn").toString();
							if (keyOfEn.equals(item.getKeyOfEn()) == false) {
								continue;
							}

							if (item.getIsSigan() == true) {
								item.setUIIsEnable(false);
							}

							if (item.getUIIsEnable() == true) {
								dr.setValue("UIIsEnable", 1);
							} else {
								dr.setValue("UIIsEnable", 0);
							}

							if (item.getUIVisible() == true) {
								dr.setValue("UIVisible", 1);
							} else {
								dr.setValue("UIVisible", 0);
							}

							if (item.getIsSigan() == true) {
								dr.setValue("IsSigan", 1);
							} else {
								dr.setValue("IsSigan", 0);
							}

							dr.setValue("DefVal", item.getDefVal());
						}
					}

					//从表权限的设置
					MapDtls mapdtls = new MapDtls();
					mapdtls.Retrieve(MapDtlAttr.FK_MapData, nd.getNodeFrmID() + "_" + frmNode.getFK_Node());
					for (DataRow dr : myds.GetTableByName("Sys_MapDtl").Rows) {
						for (MapDtl mapDtl : mapdtls.ToJavaList()) {
							String no = dr.getValue("No").toString() + "_" + frmNode.getFK_Node();
							if (no.equals(mapDtl.getNo()) == true) {
								dr.setValue("IsView", mapDtl.getIsView() == true ? 1 : 0);
								break;
							}

						}
					}
				}
			}

			/// 处理表单权限控制方案: 如果是绑定单个表单的时候.

			// 把流程信息表发送过去.
			GenerWorkFlow gwf = new GenerWorkFlow();
			gwf.setWorkID(realWorkID);
			gwf.RetrieveFromDBSources();
			myds.Tables.add(gwf.ToDataTableField("WF_GenerWorkFlow"));

			// 加入WF_Node.
			DataTable WF_Node = nd.ToDataTableField("WF_Node");
			myds.Tables.add(WF_Node);

			/// 加入组件的状态信息, 在解析表单的时候使用.
			FrmNodeComponent fnc = new FrmNodeComponent(nd.getNodeID());
			nd.WorkID = workID; // 为获取表单ID提供参数.

			// 处理自由表单.
			if (nd.getNodeFrmID().equals("ND" + nd.getNodeID()) == false
					&& nd.getHisFormType() == NodeFormType.FreeForm) {
				/* 说明这是引用到了其他节点的表单，就需要把一些位置元素修改掉. */
				int refNodeID = Integer.parseInt(nd.getNodeFrmID().replace("ND", ""));

				bp.wf.template.FrmNodeComponent refFnc = new FrmNodeComponent(refNodeID);

				fnc.SetValByKey(NodeWorkCheckAttr.FWC_H, refFnc.GetValFloatByKey(NodeWorkCheckAttr.FWC_H));
				fnc.SetValByKey(NodeWorkCheckAttr.FWC_W, refFnc.GetValFloatByKey(NodeWorkCheckAttr.FWC_W));
				fnc.SetValByKey(NodeWorkCheckAttr.FWC_X, refFnc.GetValFloatByKey(NodeWorkCheckAttr.FWC_X));
				fnc.SetValByKey(NodeWorkCheckAttr.FWC_Y, refFnc.GetValFloatByKey(NodeWorkCheckAttr.FWC_Y));

				if (fnc.GetValFloatByKey(NodeWorkCheckAttr.FWC_H) <= 10) {
					fnc.SetValByKey(NodeWorkCheckAttr.FWC_H, 500);
				}

				if (fnc.GetValFloatByKey(NodeWorkCheckAttr.FWC_W) <= 10) {
					fnc.SetValByKey(NodeWorkCheckAttr.FWC_W, 600);
				}

				if (fnc.GetValFloatByKey(NodeWorkCheckAttr.FWC_X) <= 10) {
					fnc.SetValByKey(NodeWorkCheckAttr.FWC_X, 200);
				}

				if (fnc.GetValFloatByKey(NodeWorkCheckAttr.FWC_Y) <= 10) {
					fnc.SetValByKey(NodeWorkCheckAttr.FWC_Y, 200);
				}

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

			/// 没有审核组件分组就增加上审核组件分组.
			if (nd.getNodeFrmID().equals("ND" + nd.getNodeID()) == true
					|| (nd.getHisFormType() == NodeFormType.RefOneFrmTree
							&& DataType.IsNullOrEmpty(frmNode.getMyPK()) == false)) {
				boolean isHaveFWC = false;
				// 绑定表单库中的表单
				if ((DataType.IsNullOrEmpty(frmNode.getMyPK()) == false
						&& frmNode.getIsEnableFWC() != FrmWorkCheckSta.Disable)
						|| (nd.getNodeFrmID().equals("ND" + nd.getNodeID())
								&& nd.getFrmWorkCheckSta() != FrmWorkCheckSta.Disable)) {
					isHaveFWC = true;
				}

				if ((nd.getFormType() == NodeFormType.FoolForm || frmNode.getHisFrmType() == FrmType.FoolForm)
						&& isHaveFWC == true) {
					// 判断是否是傻瓜表单，如果是，就要判断该傻瓜表单是否有审核组件groupfield ,没有的话就增加上.
					DataTable gf = myds.GetTableByName("Sys_GroupField");
					boolean isHave = false;
					for (DataRow dr : gf.Rows) {
						String cType = dr.getValue("CtrlType") instanceof String ? (String) dr.getValue("CtrlType")
								: null;
						if (cType == null) {
							continue;
						}

						if (cType.equals("FWC") == true) {
							isHave = true;
						}
					}

					if (isHave == false) {
						DataRow dr = gf.NewRow();

						nd.WorkID = workID; // 为获取表单ID提供参数.
						dr.setValue(GroupFieldAttr.OID, 100);
						dr.setValue(GroupFieldAttr.FrmID, nd.getNodeFrmID());
						dr.setValue(GroupFieldAttr.CtrlType, "FWC");
						dr.setValue(GroupFieldAttr.CtrlID, "FWCND" + nd.getNodeID());
						dr.setValue(GroupFieldAttr.Idx, 100);
						dr.setValue(GroupFieldAttr.Lab, "审核信息");
						gf.Rows.add(dr);

						myds.Tables.remove("Sys_GroupField");
						myds.Tables.add(gf);

						// 更新,为了让其自动增加审核分组.
						bp.wf.template.FrmNodeComponent refFnc = new FrmNodeComponent(nd.getNodeID());
						refFnc.Update();
					}
				}
			}

			/// 没有审核组件分组就增加上审核组件分组.

			// 把审核组件信息，放入ds.
			myds.Tables.add(fnc.ToDataTableField("WF_FrmNodeComponent"));

			/// 加入组件的状态信息, 在解析表单的时候使用.

			/// 处理累加表单增加 groupfields
			if (nd.getFormType() == NodeFormType.FoolTruck && nd.getIsStartNode() == false
					&& DataType.IsNullOrEmpty(wk.HisPassedFrmIDs) == false) {

				/// 处理字段分组排序.
				// 查询所有的分组, 如果是查看表单的方式，就不应该把当前的表单显示出来.
				String myFrmIDs = "";
				if (fromWorkOpt.equals("1") == true) {
					if (gwf.getWFState() == WFState.Complete) {
						myFrmIDs = wk.HisPassedFrmIDs + ",'ND" + nd.getNodeID() + "'";
					} else {
						myFrmIDs = wk.HisPassedFrmIDs; // 流程未完成并且是查看表单的情况.
					}
				} else {
					myFrmIDs = wk.HisPassedFrmIDs + ",'ND" + nd.getNodeID() + "'";
				}

				GroupFields gfs = new GroupFields();
				gfs.RetrieveIn(GroupFieldAttr.FrmID, "(" + myFrmIDs + ")");

				// 按照时间的顺序查找出来 ids .
				String sqlOrder = "SELECT OID FROM  Sys_GroupField WHERE   FrmID IN (" + myFrmIDs + ")";
				myFrmIDs = myFrmIDs.replace("'", "");
				if (SystemConfig.getAppCenterDBType() == DBType.Oracle
						|| SystemConfig.getAppCenterDBType() == DBType.KingBaseR3
						|| SystemConfig.getAppCenterDBType() == DBType.KingBaseR6) {
					sqlOrder += " ORDER BY INSTR('" + myFrmIDs + "',FrmID) , Idx";
				}

				if (SystemConfig.getAppCenterDBType() == DBType.MSSQL) {
					sqlOrder += " ORDER BY CHARINDEX(FrmID, '" + myFrmIDs + "'), Idx";
				}

				if (SystemConfig.getAppCenterDBType() == DBType.MySQL) {
					sqlOrder += " ORDER BY INSTR('" + myFrmIDs + "', FrmID ), Idx";
				}
				if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL) {
					sqlOrder += " ORDER BY POSITION(FrmID  IN '" + myFrmIDs + "'), Idx";
				}

				if (SystemConfig.getAppCenterDBType() == DBType.DM) {
					sqlOrder += " ORDER BY POSITION(FrmID  IN '" + myFrmIDs + "'), Idx";
				}

				DataTable dtOrder = DBAccess.RunSQLReturnTable(sqlOrder);

				// 创建容器,把排序的分组放入这个容器.
				GroupFields gfsNew = new GroupFields();

				// 遍历查询出来的分组.
				// 只能增加一个审核分组
				GroupField FWCG = null;
				for (DataRow dr : dtOrder.Rows) {
					String pkOID = dr.getValue(0).toString();
					Object tempVar = gfs.GetEntityByKey(pkOID);
					GroupField mygf = tempVar instanceof GroupField ? (GroupField) tempVar : null;
					if (mygf.getCtrlType().equals("FWC")) {
						FWCG = mygf;
						continue;
					}

					gfsNew.AddEntity(mygf); // 把分组字段加入里面去.
				}
				if (FWCG != null) {
					gfsNew.AddEntity(FWCG);
				}

				DataTable dtGF = gfsNew.ToDataTableField("Sys_GroupField");
				myds.Tables.remove("Sys_GroupField");
				myds.Tables.add(dtGF);

				/// 处理字段分组排序.

				/// 处理 mapattrs
				// 求当前表单的字段集合.
				MapAttrs attrs = new MapAttrs();
				QueryObject qo = new QueryObject(attrs);
				qo.AddWhere(MapAttrAttr.FK_MapData, "ND" + nd.getNodeID());
				qo.addOrderBy(MapAttrAttr.Idx);
				qo.DoQuery();

				// 获取走过节点的表单方案
				MapAttrs attrsLeiJia = new MapAttrs();

				// 存在表单方案只读
				String sql1 = "Select FK_Frm From WF_FrmNode Where FK_Frm In(" + wk.HisPassedFrmIDs + ") And FrmSln="
						+ FrmSln.Readonly.getValue() + " And FK_Node=" + nd.getNodeID();
				DataTable dt1 = DBAccess.RunSQLReturnTable(sql1);
				if (dt1.Rows.size() > 0) {
					// 获取节点
					String nodes = "";
					for (DataRow dr : dt1.Rows) {
						nodes += "'" + dr.getValue(0).toString() + "',";
					}

					nodes = nodes.substring(0, nodes.length() - 1);
					qo = new QueryObject(attrsLeiJia);
					qo.AddWhere(MapAttrAttr.FK_MapData, " IN ", "(" + nodes + ")");
					qo.addOrderBy(MapAttrAttr.Idx);
					qo.DoQuery();

					for (MapAttr item : attrsLeiJia.ToJavaList()) {
						if (item.getKeyOfEn().equals("RDT") || item.getKeyOfEn().equals("Rec")) {
							continue;
						}
						item.setUIIsEnable(false); // 设置为只读的.
						attrs.AddEntity(item);
					}

				}

				// 存在表单方案默认
				sql1 = "Select FK_Frm From WF_FrmNode Where FK_Frm In(" + wk.HisPassedFrmIDs + ") And FrmSln="
						+ FrmSln.Default.getValue() + " And FK_Node=" + nd.getNodeID();
				dt1 = DBAccess.RunSQLReturnTable(sql1);
				if (dt1.Rows.size() > 0) {
					// 获取节点
					String nodes = "";
					for (DataRow dr : dt1.Rows) {
						nodes += "'" + dr.getValue(0).toString() + "',";
					}

					nodes = nodes.substring(0, nodes.length() - 1);
					qo = new QueryObject(attrsLeiJia);
					qo.AddWhere(MapAttrAttr.FK_MapData, " IN ", "(" + nodes + ")");
					qo.addOrderBy(MapAttrAttr.Idx);
					qo.DoQuery();

					for (MapAttr item : attrsLeiJia.ToJavaList()) {
						if (item.getKeyOfEn().equals("RDT") || item.getKeyOfEn().equals("Rec")) {
							continue;
						}
						attrs.AddEntity(item);
					}

				}

				// 存在表单方案自定义
				sql1 = "Select FK_Frm From WF_FrmNode Where FK_Frm In(" + wk.HisPassedFrmIDs + ") And FrmSln="
						+ FrmSln.Self.getValue() + " And FK_Node=" + nd.getNodeID();
				dt1 = DBAccess.RunSQLReturnTable(sql1);

				if (dt1.Rows.size() > 0) {
					// 获取节点
					String nodes = "";
					for (DataRow dr : dt1.Rows) {
						nodes += "'" + dr.getValue(0).toString() + "',";
					}

					nodes = nodes.substring(0, nodes.length() - 1);
					qo = new QueryObject(attrsLeiJia);
					qo.AddWhere(MapAttrAttr.FK_MapData, " IN ", "(" + nodes + ")");
					qo.addOrderBy(MapAttrAttr.Idx);
					qo.DoQuery();

					// 获取累加表单的权限
					FrmFields fls = new FrmFields();
					qo = new QueryObject(fls);
					qo.AddWhere(FrmFieldAttr.FK_MapData, " IN ", "(" + nodes + ")");
					qo.addAnd();
					qo.AddWhere(FrmFieldAttr.EleType, FrmEleType.Field);
					qo.addAnd();
					qo.AddWhere(FrmFieldAttr.FK_Node, nd.getNodeID());
					qo.DoQuery();

					for (MapAttr attr : attrsLeiJia.ToJavaList()) {
						if (attr.getKeyOfEn().equals("RDT") || attr.getKeyOfEn().equals("Rec")) {
							continue;
						}

						FrmField frmField = null;
						for (FrmField item : fls.ToJavaList()) {
							if (item.getKeyOfEn().equals(attr.getKeyOfEn())) {
								frmField = item;
								break;
							}
						}
						if (frmField != null) {
							if (frmField.getIsSigan()) {
								attr.setUIIsEnable(false);
							}

							attr.setUIIsEnable(frmField.getUIIsEnable());
							attr.setUIVisible(frmField.getUIVisible());
							attr.setIsSigan(frmField.getIsSigan());
							attr.setDefValReal(frmField.getDefVal());
						}
						attrs.AddEntity(attr);
					}

				}

				// 替换掉现有的.
				myds.Tables.remove("Sys_MapAttr"); // 移除.
				myds.Tables.add(attrs.ToDataTableField("Sys_MapAttr")); // 增加.

				/// 处理mapattrs

				/// 把枚举放入里面去.
				myds.Tables.remove("Sys_Enum");

				myFrmIDs = wk.HisPassedFrmIDs + ",'ND" + nd.getNodeID() + "'";
				SysEnums enums = new SysEnums();
				enums.RetrieveInSQL(SysEnumAttr.EnumKey,
						"SELECT UIBindKey FROM Sys_MapAttr WHERE FK_MapData in(" + myFrmIDs + ")", SysEnumAttr.IntKey);

				// 加入最新的枚举.
				myds.Tables.add(enums.ToDataTableField("Sys_Enum"));

				/// 把枚举放入里面去.

				/// MapExt .
				myds.Tables.remove("Sys_MapExt");

				// 把扩展放入里面去.
				myFrmIDs = wk.HisPassedFrmIDs + ",'ND" + nd.getNodeID() + "'";
				MapExts exts = new MapExts();
				qo = new QueryObject(exts);
				qo.AddWhere(MapExtAttr.FK_MapData, " IN ", "(" + myFrmIDs + ")");
				qo.DoQuery();

				// 加入最新的MapExt.
				myds.Tables.add(exts.ToDataTableField("Sys_MapExt"));

				/// MapExt .

				/// MapDtl .
				myds.Tables.remove("Sys_MapDtl");

				// 把从表放里面
				myFrmIDs = wk.HisPassedFrmIDs + ",'ND" + nd.getNodeID() + "'";
				MapDtls dtls = new MapDtls();
				qo = new QueryObject(dtls);
				qo.AddWhere(MapDtlAttr.FK_MapData, " IN ", "(" + myFrmIDs + ")");
				qo.addAnd();
				qo.AddWhere(MapDtlAttr.FK_Node, 0);

				qo.DoQuery();

				// 加入最新的MapDtl.
				myds.Tables.add(dtls.ToDataTableField("Sys_MapDtl"));

				/// MapDtl .

				/// FrmAttachment .
				myds.Tables.remove("Sys_FrmAttachment");

				// 把附件放里面
				myFrmIDs = wk.HisPassedFrmIDs + ",'ND" + nd.getNodeID() + "'";
				FrmAttachments frmAtchs = new FrmAttachments();
				qo = new QueryObject(frmAtchs);
				qo.AddWhere(FrmAttachmentAttr.FK_MapData, " IN ", "(" + myFrmIDs + ")");
				qo.addAnd();
				qo.AddWhere(FrmAttachmentAttr.FK_Node, 0);
				qo.DoQuery();

				// 加入最新的Sys_FrmAttachment.
				myds.Tables.add(frmAtchs.ToDataTableField("Sys_FrmAttachment"));

				/// FrmAttachment .

			}

			/// 增加 groupfields

			if (isView == false) {
				/// 流程设置信息.
				bp.wf.Dev2Interface.Node_SetWorkRead(nd.getNodeID(), workID);
				if (nd.getIsStartNode() == false) {
					if (gwf.getTodoEmps().contains(WebUser.getNo() + ",") == false) {
						gwf.setTodoEmps(gwf.getTodoEmps() + WebUser.getNo() + "," + WebUser.getName() + ";");
						gwf.Update();
					}
				}

				/// 流程设置信息.
				/// 把主从表数据放入里面.
				/// .工作数据放里面去, 放进去前执行一次装载前填充事件.

				// 重设默认值.
				wk.ResetDefaultVal(nd.getNodeFrmID(), fk_flow, nd.getNodeID());
			}

			// URL参数替换
			if (SystemConfig.getIsBSsystem() == true && isView == false) {
				// 处理传递过来的参数。
				Enumeration enu = ContextHolderUtils.getRequest().getParameterNames();
				while (enu.hasMoreElements()) {
					String k = (String) enu.nextElement();
					if (DataType.IsNullOrEmpty(k) == true) {
						continue;
					}
					wk.SetValByKey(k, bp.sys.Glo.getRequest().getParameter(k));

				}

				// 更新到数据库里.
				wk.DirectUpdate();
			}
			String msg = "";
			MapExts mes = md.getMapExts();
			MapExt me = null;

			if (isView == false) {
				// 执行表单事件
				msg = ExecEvent.DoFrm(md, EventListFrm.FrmLoadBefore, wk);
				if (DataType.IsNullOrEmpty(msg) == false)
					throw new RuntimeException("err@错误:" + msg);

				// 执行装载填充.
				String mypk = MapExtXmlList.PageLoadFull + "_" + md.getNo();

				if (frmNode.getIsEnableLoadData() == true && md.getIsPageLoadFull()==true) {
					Object tempVar2 = mes.GetEntityByKey("MyPK", mypk);
					me = tempVar2 instanceof MapExt ? (MapExt) tempVar2 : null;
					// 执行通用的装载方法.
					MapAttrs attrs = md.getMapAttrs();
					MapDtls dtls = md.getMapDtls();
					Entity tempVar3 = bp.wf.Glo.DealPageLoadFull(wk, me, attrs, dtls);
					wk = tempVar3 instanceof Work ? (Work) tempVar3 : null;
				}
			}

			// 如果是累加表单，就把整个rpt数据都放入里面去.
			if (nd.getFormType() == NodeFormType.FoolTruck && nd.getIsStartNode() == false
					&& DataType.IsNullOrEmpty(wk.HisPassedFrmIDs) == false) {
				GERpt rpt = new GERpt("ND" + Integer.parseInt(nd.getFK_Flow()) + "Rpt", workID);
				rpt.Copy(wk);

				DataTable rptdt = rpt.ToDataTableField("MainTable");
				myds.Tables.add(rptdt);
			} else {
				DataTable mainTable = wk.ToDataTableField("MainTable");
				myds.Tables.add(mainTable);
			}
			String sql = "";
			DataTable dt = null;

			///

			/// 把外键表加入 DataSet
			DataTable dtMapAttr = myds.GetTableByName("Sys_MapAttr");

			DataTable ddlTable = new DataTable();
			ddlTable.Columns.Add("No");

			for (DataRow dr : dtMapAttr.Rows) {
				String lgType = dr.getValue("LGType").toString();
				String uiBindKey = dr.getValue("UIBindKey").toString();

				if (DataType.IsNullOrEmpty(uiBindKey) == true) {
					continue; // 为空就continue.
				}

				if (lgType.equals("1") == true) {
					continue; // 枚举值就continue;
				}

				String uiIsEnable = dr.getValue("UIIsEnable").toString();
				if (uiIsEnable.equals("0") == true && lgType.equals("1") == true) {
					continue; // 如果是外键，并且是不可以编辑的状态.
				}

				if (uiIsEnable.equals("0") == true && lgType.equals("0") == true) {
					continue; // 如果是外部数据源，并且是不可以编辑的状态.
				}

				// 检查是否有下拉框自动填充。
				String keyOfEn = dr.getValue("KeyOfEn").toString();
				String fk_mapData = dr.getValue("FK_MapData").toString();

				/// 处理下拉框数据范围. for 小杨.
				Object tempVar4 = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL,
						MapExtAttr.AttrOfOper, keyOfEn);
				me = tempVar4 instanceof MapExt ? (MapExt) tempVar4 : null;
				if (me != null && myds.GetTableByName(keyOfEn) == null) {
					Object tempVar5 = me.getDoc();
					String fullSQL = tempVar5 instanceof String ? (String) tempVar5 : null;
					if (fullSQL == null) {
						throw new RuntimeException("err@字段[" + keyOfEn + "]下拉框AutoFullDLL，没有配置SQL");
					}

					fullSQL = fullSQL.replace("~", "'");
					fullSQL = bp.wf.Glo.DealExp(fullSQL, wk, null);
					dt = DBAccess.RunSQLReturnTable(fullSQL);
					// 重构新表
					DataTable dt_FK_Dll = new DataTable();
					dt_FK_Dll.TableName = keyOfEn; // 可能存在隐患，如果多个字段，绑定同一个表，就存在这样的问题.
					dt_FK_Dll.Columns.Add("No", String.class);
					dt_FK_Dll.Columns.Add("Name", String.class);
					for (DataRow dllRow : dt.Rows) {
						DataRow drDll = dt_FK_Dll.NewRow();
						drDll.setValue("No", dllRow.getValue("No"));
						drDll.setValue("Name", dllRow.getValue("Name"));
						dt_FK_Dll.Rows.add(drDll);
					}
					myds.Tables.add(dt_FK_Dll);
					continue;
				}

				/// 处理下拉框数据范围.

				// 判断是否存在.
				if (myds.GetTableByName(uiBindKey)!=null) {
					continue;
				}

				DataTable mydt = PubClass.GetDataTableByUIBineKey(uiBindKey);
				if (mydt == null) {
					DataRow ddldr = ddlTable.NewRow();
					ddldr.setValue("No", uiBindKey);
					ddlTable.Rows.add(ddldr);
				} else {
					myds.Tables.add(mydt);
				}
			}
			ddlTable.TableName = "UIBindKey";
			myds.Tables.add(ddlTable);

			/// End把外键表加入DataSet

			/// 处理流程-消息提示.
			if (isView == true || fromWorkOpt.equals("1")) {
				// 如果是查看模式，或者是从WorkOpt打开模式,就不让其显示消息.
			} else {
				
				DataTable dtAlert = new DataTable();
				dtAlert.TableName = "AlertMsg";
				dtAlert.Columns.Add("Title", String.class);
				dtAlert.Columns.Add("Msg", String.class);
				dtAlert.Columns.Add("URL", String.class);

				// string msg = "";
				switch (gwf.getWFState()) {
				case AskForReplay: // 返回加签的信息.
					String mysql = "SELECT * FROM ND" + Integer.parseInt(fk_flow) + "Track WHERE WorkID=" + realWorkID
							+ " AND " + TrackAttr.ActionType + "=" + ActionType.ForwardAskfor.getValue();

					DataTable mydt = DBAccess.RunSQLReturnTable(mysql);
					for (DataRow dr : mydt.Rows) {
						String msgAskFor = dr.getValue(TrackAttr.Msg).toString();
						String worker = dr.getValue(TrackAttr.EmpFrom).toString();
						String workerName = dr.getValue(TrackAttr.EmpFromT).toString();
						String rdt = dr.getValue(TrackAttr.RDT).toString();

						DataRow drMsg = dtAlert.NewRow();
						drMsg.setValue("Title", worker + "," + workerName + "回复信息:");
						drMsg.setValue("Msg", DataType.ParseText2Html(msgAskFor) + "<br>" + rdt);
						dtAlert.Rows.add(drMsg);
					}
					break;
				case Askfor: // 加签.

					sql = "SELECT * FROM ND" + Integer.parseInt(fk_flow) + "Track WHERE WorkID=" + realWorkID + " AND "
							+ TrackAttr.ActionType + "=" + ActionType.AskforHelp.getValue();
					dt = DBAccess.RunSQLReturnTable(sql);
					for (DataRow dr : dt.Rows) {
						String msgAskFor = dr.getValue(TrackAttr.Msg).toString();
						String worker = dr.getValue(TrackAttr.EmpFrom).toString();
						String workerName = dr.getValue(TrackAttr.EmpFromT).toString();
						String rdt = dr.getValue(TrackAttr.RDT).toString();

						DataRow drMsg = dtAlert.NewRow();
						drMsg.setValue("Title", worker + "," + workerName + "请求加签:");
						drMsg.setValue("Msg",
								DataType.ParseText2Html(msgAskFor) + "<br>" + rdt
										+ "<a href='./WorkOpt/AskForRe.htm?FK_Flow=" + fk_flow + "&FK_Node="
										+ nd.getNodeID() + "&WorkID=" + realWorkID + "&FID=" + fid + "' >回复加签意见</a> --");
						dtAlert.Rows.add(drMsg);

					}
					// isAskFor = true;
					break;
				case ReturnSta:
					/* 如果工作节点退回了 */
					ReturnWorks rws = new ReturnWorks();
					rws.Retrieve(ReturnWorkAttr.ReturnToNode, nd.getNodeID(), ReturnWorkAttr.WorkID, realWorkID,
							ReturnWorkAttr.RDT);

					if (rws.size() != 0) {
						String msgInfo = "";
						for (ReturnWork rw : rws.ToJavaList()) {
							// drMsg["Title"] = "来自节点:" + rw.ReturnNodeName + "
							// 退回人:" + rw.ReturnerName + " " + rw.RDT +
							// "&nbsp;<a href='/DataUser/ReturnLog/" + fk_flow +
							// "/" + rw.MyPK + ".htm' target=_blank>工作日志</a>";
							msgInfo += "\t\n来自节点:" + rw.getReturnNodeName() + " 退回人:" + rw.getReturnerName() + "  "
									+ rw.getRDT();
							msgInfo += rw.getBeiZhuHtml();
						}

						String str = nd.getReturnAlert();
						if (!str.equals("")) {
							str = str.replace("~", "'");
							str = str.replace("@PWorkID", String.valueOf(workID));
							str = str.replace("@PNodeID", String.valueOf(nd.getNodeID()));
							str = str.replace("@FK_Node", String.valueOf(nd.getNodeID()));

							str = str.replace("@PFlowNo", fk_flow);
							str = str.replace("@FK_Flow", fk_flow);
							str = str.replace("@PWorkID", String.valueOf(workID));

							str = str.replace("@WorkID", String.valueOf(realWorkID));
							str = str.replace("@OID", String.valueOf(workID));

							DataRow drMsg = dtAlert.NewRow();
							drMsg.setValue("Title", "退回信息");
							drMsg.setValue("Msg", msgInfo + "\t\n" + str);
							dtAlert.Rows.add(drMsg);
						} else {
							DataRow drMsg = dtAlert.NewRow();
							drMsg.setValue("Title", "退回信息");
							drMsg.setValue("Msg", msgInfo + "\t\n" + str);
							dtAlert.Rows.add(drMsg);
						}
					}
					break;
				case Shift:
					/* 判断移交过来的。 */

					String sqlshift = "SELECT * FROM ND" + Integer.parseInt(fk_flow)
							+ "Track WHERE ACTIONTYPE=3 AND WorkID=" + workID + " AND NDFrom='" + gwf.getFK_Node()
							+ "' ORDER BY RDT DESC ";
					DataTable dtshift = DBAccess.RunSQLReturnTable(sqlshift);

					if (dtshift.Rows.size() >= 1) {
						DataRow drMsg = dtAlert.NewRow();
						drMsg.setValue("Title", "移交历史信息");
						msg = "";
						for (DataRow dr : dtshift.Rows) {
							String empFromT = dr.getValue(TrackAttr.EmpFromT).toString();
							String empToT = dr.getValue(TrackAttr.EmpToT).toString();
							String msgShift = dr.getValue(TrackAttr.Msg).toString();

							String temp = "@移交人[" + empFromT + "]。@接受人：" + empToT + "。<br>移交原因：-------------"
									+ msgShift;
							temp = temp.replace("@", "<br>@");
							msg += temp + "<hr/>";
						}

						drMsg.setValue("Msg", msg);
						dtAlert.Rows.add(drMsg);
					}
					break;
				default:
					break;
				}
				myds.Tables.add(dtAlert);
			}
 
			/// 增加流程节点表单绑定信息.
			myds.Tables.add(frmNode.ToDataTableField("WF_FrmNode"));
			if (nd.getHisFormType() == NodeFormType.RefOneFrmTree) {
				/* 独立流程节点表单. */
				nd.WorkID = workID; // 为获取表单ID ( NodeFrmID )提供参数.

				//设置单据编号,对于绑定的表单. @yln.
				if (nd.getIsStartNode() == true && DataType.IsNullOrEmpty(frmNode.getBillNoField()) == false)
				{
					DataTable dtMain = myds.GetTableByName("MainTable");
					if (dtMain.Columns.contains(frmNode.getBillNoField()) == true)
					{
						dtMain.Rows.get(0).setValue(frmNode.getBillNoField(), wk.GetValStringByKey("BillNo"));
					}
				}
				FrmNode fn = new FrmNode();
				fn.setMyPK(nd.getNodeFrmID() + "_" + nd.getNodeID() + "_" + nd.getFK_Flow());
				fn.Retrieve();
				myds.Tables.add(fn.ToDataTableField("FrmNode"));
			}

			/// 增加流程节点表单绑定信息.

			// myds.WriteXml("c:\\11.xml");

			return myds;
		} catch (RuntimeException ex) {
			Log.DebugWriteError(ex.getStackTrace().toString());
			throw new RuntimeException("generoWorkNode@" + ex.getMessage());
		}
	}
}