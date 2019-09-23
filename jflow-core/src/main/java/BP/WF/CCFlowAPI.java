package BP.WF;

import BP.DA.*;
import BP.Sys.*;
import BP.Web.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Data.*;
import BP.WF.Template.*;
import BP.Port.*;
import BP.Tools.*;

public class CCFlowAPI
{
	/** 
	 产生一个WorkNode
	 
	 @param fk_flow 流程编号
	 @param fk_node 节点ID
	 @param workID 工作ID
	 @param fid FID
	 @param userNo 用户编号
	 @return 返回dataset
	*/

	public static DataSet GenerWorkNode(String fk_flow, int fk_node, long workID, long fid, String userNo)
	{
		return GenerWorkNode(fk_flow, fk_node, workID, fid, userNo, "0");
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static DataSet GenerWorkNode(string fk_flow, int fk_node, Int64 workID, Int64 fid, string userNo, string fromWorkOpt = "0")
	public static DataSet GenerWorkNode(String fk_flow, int fk_node, long workID, long fid, String userNo, String fromWorkOpt)
	{
		//节点.
		if (fk_node == 0)
		{
			fk_node = Integer.parseInt(fk_flow + "01");
		}

		if (workID == 0)
		{
			workID = BP.WF.Dev2Interface.Node_CreateBlankWork(fk_flow, null, null, userNo, null);
		}

		Node nd = new Node(fk_node);
		try
		{
			nd.WorkID = workID; //为获取表单ID提供参数.
			MapData md = new MapData(nd.getNodeFrmID());

			Work wk = nd.getHisWork();
			wk.setOID(workID);

			wk.RetrieveFromDBSources();
			wk.ResetDefaultVal();

			// 第1.2: 调用,处理用户定义的业务逻辑.
			String sendWhen = nd.getHisFlow().DoFlowEventEntity(EventListOfNode.FrmLoadBefore, nd, wk, null);


			//获得表单模版.
			DataSet myds = BP.Sys.CCFormAPI.GenerHisDataSet(md.getNo(), nd.getName());

			if (DataType.IsNullOrEmpty(nd.getNodeFrmID()) == false && (nd.getHisFormType() == NodeFormType.FoolForm || nd.getHisFormType() == NodeFormType.FreeForm))
			{
				String name = md.getName();
				if (DataType.IsNullOrEmpty(md.getName()) == true)
				{
					name = nd.getName();
				}
				myds.GetTableByName("Sys_MapData").Rows.get(0).setValue("Name",name );
			}

			//移除MapAttr
			myds.Tables.remove("Sys_MapAttr"); //移除.

			//获取表单的mapAttr
			//求出集合.
			MapAttrs mattrs = md.MapAttrs; // new MapAttrs(md.No);

			/*处理表单权限控制方案*/
			FrmNode frmNode = new FrmNode();
			int count = frmNode.Retrieve(FrmNodeAttr.FK_Frm, md.getNo(), FrmNodeAttr.FK_Node, fk_node);
			if (count != 0 && frmNode.getFrmSln() != 0)
			{
				FrmFields fls = new FrmFields(md.getNo(), frmNode.getFK_Node());
				for (FrmField item : fls.ToJavaList())
				{
					for (MapAttr attr : mattrs.ToJavaList())
					{
						if (!item.getKeyOfEn().equals(attr.getKeyOfEn()))
						{
							continue;
						}

						if (item.getIsSigan())
						{
							item.setUIIsEnable(false);
						}

						attr.setUIIsEnable(item.getUIIsEnable());
                        attr.setUIVisible(item.getUIVisible());
                        attr.setIsSigan(item.getIsSigan());
                        attr.setDefValReal(item.getDefVal());
					}
				}
			}


			DataTable Sys_MapAttr = mattrs.ToDataTableField("Sys_MapAttr");
			myds.Tables.add(Sys_MapAttr);

			//把流程信息表发送过去.
			GenerWorkFlow gwf = new GenerWorkFlow();
			gwf.setWorkID(workID);
			gwf.RetrieveFromDBSources();
			myds.Tables.add(gwf.ToDataTableField("WF_GenerWorkFlow"));
			//加入WF_Node.
			DataTable WF_Node = nd.ToDataTableField("WF_Node");
			myds.Tables.add(WF_Node);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 加入组件的状态信息, 在解析表单的时候使用.
			BP.WF.Template.FrmNodeComponent fnc = new FrmNodeComponent(nd.getNodeID());

			nd.WorkID = workID; //为获取表单ID提供参数.
			if (!nd.getNodeFrmID().equals("ND" + nd.getNodeID()) && nd.getHisFormType() != NodeFormType.RefOneFrmTree)
			{
				/*说明这是引用到了其他节点的表单，就需要把一些位置元素修改掉.*/

				int refNodeID = Integer.parseInt(nd.getNodeFrmID().replace("ND", ""));

				BP.WF.Template.FrmNodeComponent refFnc = new FrmNodeComponent(refNodeID);

				fnc.SetValByKey(FrmWorkCheckAttr.FWC_H, refFnc.GetValFloatByKey(FrmWorkCheckAttr.FWC_H));
				fnc.SetValByKey(FrmWorkCheckAttr.FWC_W, refFnc.GetValFloatByKey(FrmWorkCheckAttr.FWC_W));
				fnc.SetValByKey(FrmWorkCheckAttr.FWC_X, refFnc.GetValFloatByKey(FrmWorkCheckAttr.FWC_X));
				fnc.SetValByKey(FrmWorkCheckAttr.FWC_Y, refFnc.GetValFloatByKey(FrmWorkCheckAttr.FWC_Y));

				if (fnc.GetValFloatByKey(FrmWorkCheckAttr.FWC_H) <= 10)
				{
					fnc.SetValByKey(FrmWorkCheckAttr.FWC_H, 500);
				}

				if (fnc.GetValFloatByKey(FrmWorkCheckAttr.FWC_W) <= 10)
				{
					fnc.SetValByKey(FrmWorkCheckAttr.FWC_W, 600);
				}

				if (fnc.GetValFloatByKey(FrmWorkCheckAttr.FWC_X) <= 10)
				{
					fnc.SetValByKey(FrmWorkCheckAttr.FWC_X, 200);
				}

				if (fnc.GetValFloatByKey(FrmWorkCheckAttr.FWC_Y) <= 10)
				{
					fnc.SetValByKey(FrmWorkCheckAttr.FWC_Y, 200);
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

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#region 没有审核组件分组就增加上审核组件分组. @杜需要翻译&测试.
				if (md.HisFrmType == FrmType.FoolForm)
				{
					//判断是否是傻瓜表单，如果是，就要判断该傻瓜表单是否有审核组件groupfield ,没有的话就增加上.
					DataTable gf = myds.Tables["Sys_GroupField"];
					boolean isHave = false;
					for (DataRow dr : gf.Rows)
					{
						String cType = dr.get("CtrlType") instanceof String ? (String)dr.get("CtrlType") : null;
						if (cType == null)
						{
							continue;
						}

						if (cType.equals("FWC") == true)
						{
							isHave = true;
						}
					}

					if (isHave == false)
					{
						DataRow dr = gf.NewRow();

						nd.WorkID = workID; //为获取表单ID提供参数.
						dr.set(GroupFieldAttr.OID, 100);
						dr.set(GroupFieldAttr.FrmID, nd.getNodeFrmID());
						dr.set(GroupFieldAttr.CtrlType, "FWC");
						dr.set(GroupFieldAttr.CtrlID, "FWCND" + nd.getNodeID());
						dr.set(GroupFieldAttr.Idx, 100);
						dr.set(GroupFieldAttr.Lab, "审核信息");
						gf.Rows.add(dr);

						myds.Tables.Remove("Sys_GroupField");
						myds.Tables.add(gf);

						//执行更新,就自动生成那个丢失的字段分组.
						refFnc.Update();
					}

				}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion 没有审核组件分组就增加上审核组件分组.

			}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 没有审核组件分组就增加上审核组件分组. @杜需要翻译&测试.
			if (nd.getNodeFrmID().equals("ND" + nd.getNodeID()) || (nd.getHisFormType() == NodeFormType.RefOneFrmTree && count != 0))
			{
				boolean isHaveFWC = false;
				//绑定表单库中的表单
				if (count != 0 && frmNode.getIsEnableFWC() == true && nd.getFrmWorkCheckSta() != FrmWorkCheckSta.Disable)
				{
					isHaveFWC = true;
				}

				if (nd.getFormType() == NodeFormType.FoolForm && isHaveFWC == true)
				{
					//判断是否是傻瓜表单，如果是，就要判断该傻瓜表单是否有审核组件groupfield ,没有的话就增加上.
					DataTable gf = myds.Tables["Sys_GroupField"];
					boolean isHave = false;
					for (DataRow dr : gf.Rows)
					{
						String cType = dr.get("CtrlType") instanceof String ? (String)dr.get("CtrlType") : null;
						if (cType == null)
						{
							continue;
						}

						if (cType.equals("FWC") == true)
						{
							isHave = true;
						}
					}

					if (isHave == false)
					{
						DataRow dr = gf.NewRow();

						nd.WorkID = workID; //为获取表单ID提供参数.
						dr.set(GroupFieldAttr.OID, 100);
						dr.set(GroupFieldAttr.FrmID, nd.getNodeFrmID());
						dr.set(GroupFieldAttr.CtrlType, "FWC");
						dr.set(GroupFieldAttr.CtrlID, "FWCND" + nd.getNodeID());
						dr.set(GroupFieldAttr.Idx, 100);
						dr.set(GroupFieldAttr.Lab, "审核信息");
						gf.Rows.add(dr);

						myds.Tables.Remove("Sys_GroupField");
						myds.Tables.add(gf);

						//更新,为了让其自动增加审核分组.
						BP.WF.Template.FrmNodeComponent refFnc = new FrmNodeComponent(nd.getNodeID());
						refFnc.Update();

					}
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 没有审核组件分组就增加上审核组件分组.

			myds.Tables.add(fnc.ToDataTableField("WF_FrmNodeComponent"));

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 加入组件的状态信息, 在解析表单的时候使用.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 增加 groupfields
			if (nd.getFormType() == NodeFormType.FoolTruck && nd.getIsStartNode() == false && DataType.IsNullOrEmpty(wk.HisPassedFrmIDs) == false)
			{

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#region 处理字段分组排序.
				//查询所有的分组, 如果是查看表单的方式，就不应该把当前的表单显示出来.
				String myFrmIDs = "";
				if (fromWorkOpt.equals("1") == true)
				{
					if (gwf.getWFState() == WFState.Complete)
					{
						myFrmIDs = wk.HisPassedFrmIDs + ",'ND" + fk_node + "'";
					}
					else
					{
						myFrmIDs = wk.HisPassedFrmIDs; //流程未完成并且是查看表单的情况.
					}
				}
				else
				{
					myFrmIDs = wk.HisPassedFrmIDs + ",'ND" + fk_node + "'";
				}

				GroupFields gfs = new GroupFields();
				gfs.RetrieveIn(GroupFieldAttr.FrmID, "(" + myFrmIDs + ")");

				//按照时间的顺序查找出来 ids .
				String sqlOrder = "SELECT OID FROM  Sys_GroupField WHERE   FrmID IN (" + myFrmIDs + ")";
				if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.Oracle)
				{
					myFrmIDs = myFrmIDs.replace("'", "");
					sqlOrder += " ORDER BY INSTR('" + myFrmIDs + "',FrmID) , Idx";
				}

				if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.MSSQL)
				{
					myFrmIDs = myFrmIDs.replace("'", "");
					sqlOrder += " ORDER BY CHARINDEX(FrmID, '" + myFrmIDs + "'), Idx";
				}

				if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.MySQL)
				{
					myFrmIDs = myFrmIDs.replace("'", "");
					sqlOrder += " ORDER BY INSTR('" + myFrmIDs + "', FrmID ), Idx";
				}
				if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
				{
					myFrmIDs = myFrmIDs.replace("'", "");
					sqlOrder += " ORDER BY INSTR('" + myFrmIDs + "', FrmID ), Idx";
				}

				DataTable dtOrder = DBAccess.RunSQLReturnTable(sqlOrder);

				//创建容器,把排序的分组放入这个容器.
				GroupFields gfsNew = new GroupFields();

				//遍历查询出来的分组.
				for (DataRow dr : dtOrder.Rows)
				{
					String pkOID = dr.get(0).toString();
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
					var mygf = gfs.GetEntityByKey(pkOID);
					gfsNew.AddEntity(mygf); //把分组字段加入里面去.
				}

				DataTable dtGF = gfsNew.ToDataTableField("Sys_GroupField");
				myds.Tables.Remove("Sys_GroupField");
				myds.Tables.add(dtGF);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion 处理字段分组排序.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#region 处理 mapattrs
				//求当前表单的字段集合.
				MapAttrs attrs = new MapAttrs();
				QueryObject qo = new QueryObject(attrs);
				qo.AddWhere(MapAttrAttr.FK_MapData, "ND" + nd.getNodeID());
				qo.addOrderBy(MapAttrAttr.Idx);
				qo.DoQuery();

				//获取走过节点的表单方案
				MapAttrs attrsLeiJia = new MapAttrs();

				//存在表单方案只读
				String sql1 = "Select FK_Frm From WF_FrmNode Where FK_Frm In(" + wk.HisPassedFrmIDs + ") And FrmSln=" + FrmSln.Readonly.getValue() + " And FK_Node=" + nd.getNodeID();
				DataTable dt1 = DBAccess.RunSQLReturnTable(sql1);
				if (dt1.Rows.size() > 0)
				{
					//获取节点
					String nodes = "";
					for (DataRow dr : dt1.Rows)
					{
						nodes += "'" + dr.get(0).toString() + "',";
					}

					nodes = nodes.substring(0,nodes.length() - 1);
					qo = new QueryObject(attrsLeiJia);
					qo.AddWhere(MapAttrAttr.FK_MapData, " IN ", "(" + nodes + ")");
					qo.addOrderBy(MapAttrAttr.Idx);
					qo.DoQuery();

					for (MapAttr item : attrsLeiJia)
					{
						if (item.KeyOfEn.equals("RDT") || item.KeyOfEn.equals("Rec"))
						{
							continue;
						}
						item.UIIsEnable = false; //设置为只读的.
						attrs.AddEntity(item);
					}

				}

				//存在表单方案默认
				sql1 = "Select FK_Frm From WF_FrmNode Where FK_Frm In(" + wk.HisPassedFrmIDs + ") And FrmSln=" + FrmSln.Default.getValue() + " And FK_Node=" + nd.getNodeID();
				dt1 = DBAccess.RunSQLReturnTable(sql1);
				 if (dt1.Rows.size() > 0)
				 {
					 //获取节点
					String nodes = "";
					for (DataRow dr : dt1.Rows)
					{
						nodes += "'" + dr.get(0).toString() + "',";
					}

					nodes = nodes.substring(0,nodes.length() - 1);
					qo = new QueryObject(attrsLeiJia);
					qo.AddWhere(MapAttrAttr.FK_MapData, " IN ", "(" + nodes + ")");
					qo.addOrderBy(MapAttrAttr.Idx);
					qo.DoQuery();

					for (MapAttr item : attrsLeiJia)
					{
						if (item.KeyOfEn.equals("RDT") || item.KeyOfEn.equals("Rec"))
						{
							continue;
						}
						attrs.AddEntity(item);
					}

				 }

				//存在表单方案自定义
				 sql1 = "Select FK_Frm From WF_FrmNode Where FK_Frm In(" + wk.HisPassedFrmIDs + ") And FrmSln=" + FrmSln.Self.getValue() + " And FK_Node=" + nd.getNodeID();
				dt1 = DBAccess.RunSQLReturnTable(sql1);

				 if (dt1.Rows.size() > 0)
				 {
					 //获取节点
					String nodes = "";
					for (DataRow dr : dt1.Rows)
					{
						nodes += "'" + dr.get(0).toString() + "',";
					}

					nodes = nodes.substring(0,nodes.length() - 1);
					qo = new QueryObject(attrsLeiJia);
					qo.AddWhere(MapAttrAttr.FK_MapData, " IN ", "(" + nodes + ")");
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

					for (MapAttr attr : attrsLeiJia)
					{
						if (attr.KeyOfEn.equals("RDT") || attr.KeyOfEn.equals("Rec"))
						{
							continue;
						}

						FrmField frmField = null;
						for (FrmField item : fls)
						{
							if (item.getKeyOfEn().equals(attr.KeyOfEn))
							{
								frmField = item;
								break;
							}
						}
						if (frmField != null)
						{
							if (frmField.getIsSigan())
							{
								attr.setUIIsEnable(false;
							}

							attr.setUIIsEnable(frmField.getUIIsEnable();
							attr.setUIVisible(frmField.getUIVisible();
							attr.IsSigan = frmField.getIsSigan();
							attr.DefValReal = frmField.getDefVal();
						}
						attrs.AddEntity(attr);
					}

				 }

				//替换掉现有的.
				myds.Tables.Remove("Sys_MapAttr"); //移除.
				myds.Tables.add(attrs.ToDataTableField("Sys_MapAttr")); //增加.
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion 处理mapattrs

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#region 把枚举放入里面去.
				myds.Tables.Remove("Sys_Enum");

				myFrmIDs = wk.HisPassedFrmIDs + ",'ND" + fk_node + "'";
				SysEnums enums = new SysEnums();
				enums.RetrieveInSQL(SysEnumAttr.EnumKey, "SELECT UIBindKey FROM Sys_MapAttr WHERE FK_MapData in(" + myFrmIDs + ")", SysEnumAttr.IntKey);

				// 加入最新的枚举.
				myds.Tables.add(enums.ToDataTableField("Sys_Enum"));
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion 把枚举放入里面去.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#region  MapExt .
				myds.Tables.Remove("Sys_MapExt");

				// 把扩展放入里面去.
				myFrmIDs = wk.HisPassedFrmIDs + ",'ND" + fk_node + "'";
				BP.Sys.MapExts exts = new MapExts();
				qo = new QueryObject(exts);
				qo.AddWhere(MapExtAttr.FK_MapData, " IN ", "(" + myFrmIDs + ")");
				qo.DoQuery();

				// 加入最新的MapExt.
				myds.Tables.add(exts.ToDataTableField("Sys_MapExt"));
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion  MapExt .

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#region  MapDtl .
				myds.Tables.Remove("Sys_MapDtl");

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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion  MapDtl .

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#region  FrmAttachment .
				myds.Tables.Remove("Sys_FrmAttachment");

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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion  FrmAttachment .


			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 增加 groupfields

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 流程设置信息.
			BP.WF.Dev2Interface.Node_SetWorkRead(fk_node, workID);

			if (nd.getIsStartNode() == false)
			{
				if (gwf.getTodoEmps().contains(WebUser.getNo() + ",") == false)
				{
					gwf.setTodoEmps(gwf.getTodoEmps() + WebUser.getNo() + "," + WebUser.getName());
					gwf.Update();
				}
			}

			//增加转向下拉框数据.
			if (nd.getCondModel() == CondModel.SendButtonSileSelect)
			{
				if (nd.getIsStartNode() == true || gwf.getTodoEmps().contains(WebUser.getNo() + ",") == true)
				{
					/*如果当前不是主持人,如果不是主持人，就不让他显示下拉框了.*/

					/*如果当前节点，是可以显示下拉框的.*/
					Nodes nds = nd.getHisToNodes();

					DataTable dtToNDs = new DataTable();
					dtToNDs.TableName = "ToNodes";
					dtToNDs.Columns.Add("No", String.class); //节点ID.
					dtToNDs.Columns.Add("Name", String.class); //到达的节点名称.
					dtToNDs.Columns.Add("IsSelectEmps", String.class); //是否弹出选择人的对话框？
					dtToNDs.Columns.Add("IsSelected", String.class); //是否选择？

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
						///#region 增加到达延续子流程节点。
					if (nd.getSubFlowYanXuNum() >= 0)
					{
						SubFlowYanXus ygflows = new SubFlowYanXus(fk_node);
						for (SubFlowYanXu item : ygflows)
						{
							DataRow dr = dtToNDs.NewRow();
							dr.set("No", item.getSubFlowNo() + "01");
							dr.set("Name", "启动:" + item.getSubFlowName());
							dr.set("IsSelectEmps", "1");
							dr.set("IsSelected", "0");
							dtToNDs.Rows.add(dr);
						}
					}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
						///#endregion 增加到达延续子流程节点。

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
						///#region 到达其他节点.
					//上一次选择的节点.
					int defalutSelectedNodeID = 0;
					if (nds.size() > 1)
					{
						String mysql = "";
						// 找出来上次发送选择的节点.
						if (SystemConfig.getAppCenterDBType() == DBType.MSSQL)
						{
							mysql = "SELECT  top 1 NDTo FROM ND" + Integer.parseInt(nd.getFK_Flow()) + "Track A WHERE A.NDFrom=" + fk_node + " AND ActionType=1 ORDER BY WorkID DESC";
						}
						else if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
						{
							mysql = "SELECT * FROM ( SELECT  NDTo FROM ND" + Integer.parseInt(nd.getFK_Flow()) + "Track A WHERE A.NDFrom=" + fk_node + " AND ActionType=1 ORDER BY WorkID DESC ) WHERE ROWNUM =1";
						}
						else if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
						{
							mysql = "SELECT  NDTo FROM ND" + Integer.parseInt(nd.getFK_Flow()) + "Track A WHERE A.NDFrom=" + fk_node + " AND ActionType=1 ORDER BY WorkID  DESC limit 1,1";
						}
						else if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
						{
							mysql = "SELECT  NDTo FROM ND" + Integer.parseInt(nd.getFK_Flow()) + "Track A WHERE A.NDFrom=" + fk_node + " AND ActionType=1 ORDER BY WorkID  DESC limit 1";
						}

						//获得上一次发送到的节点.
						defalutSelectedNodeID = DBAccess.RunSQLReturnValInt(mysql, 0);
					}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
						///#region 为天业集团做一个特殊的判断.
					if (SystemConfig.getCustomerNo().equals("TianYe") && nd.getName().contains("董事长") == true)
					{
						/*如果是董事长节点, 如果是下一个节点默认的是备案. */
						for (Node item : nds)
						{
							if (item.getName().contains("备案") == true && item.getName().contains("待") == false)
							{
								defalutSelectedNodeID = item.getNodeID();
								break;
							}
						}
					}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
						///#endregion 为天业集团做一个特殊的判断.


					for (Node item : nds)
					{
						DataRow dr = dtToNDs.NewRow();
						dr.set("No", item.getNodeID());
						dr.set("Name", item.getName());
						//if (item.hissel

						if (item.getHisDeliveryWay() == DeliveryWay.BySelected)
						{
							dr.set("IsSelectEmps", "1");
						}
						else
						{
							dr.set("IsSelectEmps", "0"); //是不是，可以选择接受人.
						}

						//设置默认选择的节点.
						if (defalutSelectedNodeID == item.getNodeID())
						{
							dr.set("IsSelected", "1");
						}
						else
						{
							dr.set("IsSelected", "0");
						}

						dtToNDs.Rows.add(dr);
					}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
						///#endregion 到达其他节点。


					//增加一个下拉框, 对方判断是否有这个数据.
					myds.Tables.add(dtToNDs);
				}
			}

			// 节点数据.
			//string sql = "SELECT * FROM WF_Node WHERE NodeID=" + fk_node;
			//DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			//dt.TableName = "WF_NodeBar";
			//myds.Tables.add(dt);

			//// 流程数据.
			//Flow fl = new Flow(fk_flow);
			//myds.Tables.add(fl.ToDataTableField("WF_Flow"));
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 流程设置信息.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 把主从表数据放入里面.
			//.工作数据放里面去, 放进去前执行一次装载前填充事件.

			//重设默认值.
			wk.ResetDefaultVal();

			//@樊雷伟 把这部分代码搬到jflow上去. CCFlowAPI. 114行出.
			if (BP.Sys.SystemConfig.IsBSsystem == true)
			{
				// 处理传递过来的参数。
				for (String k : HttpContextHelper.RequestQueryStringKeys)
				{
					if (DataType.IsNullOrEmpty(k) == true)
					{
						continue;
					}

					wk.SetValByKey(k, HttpContextHelper.RequestParams(k));
				}

				// 处理传递过来的frm参数。
				//2019-07-25 zyt改造
				for (String k : HttpContextHelper.RequestParamKeys)
				{
					if (DataType.IsNullOrEmpty(k) == true)
					{
						continue;
					}

					wk.SetValByKey(k, HttpContextHelper.RequestParams(k));
				}

				//更新到数据库里.
				wk.DirectUpdate();
			}

			// 执行表单事件..
			String msg = md.DoEvent(FrmEventList.FrmLoadBefore, wk);
			if (DataType.IsNullOrEmpty(msg) == false)
			{
				throw new RuntimeException("err@错误:" + msg);
			}

			// 执行FEE事件.
			String msgOfLoad = nd.getHisFlow().DoFlowEventEntity(EventListOfNode.FrmLoadBefore, nd, wk, null);
			if (msgOfLoad != null)
			{
				wk.RetrieveFromDBSources();
			}

			//执行装载填充.
			MapExt me = new MapExt();
			if (me.Retrieve(MapExtAttr.ExtType, MapExtXmlList.PageLoadFull, MapExtAttr.FK_MapData, wk.NodeFrmID) == 1)
			{
				//执行通用的装载方法.
				MapAttrs attrs = new MapAttrs(wk.NodeFrmID);
				MapDtls dtls = new MapDtls(wk.NodeFrmID);
				Entity tempVar = BP.WF.Glo.DealPageLoadFull(wk, me, attrs, dtls);
				wk = tempVar instanceof Work ? (Work)tempVar : null;
			}

			//如果是累加表单，就把整个rpt数据都放入里面去.
			if (nd.getFormType() == NodeFormType.FoolTruck && nd.getIsStartNode() == false && DataType.IsNullOrEmpty(wk.HisPassedFrmIDs) == false)
			{

				GERpt rpt = new GERpt("ND" + Integer.parseInt(nd.getFK_Flow()) + "Rpt", workID);
				rpt.Copy(wk);

				DataTable rptdt = rpt.ToDataTableField("MainTable");

				myds.Tables.add(rptdt);
			}
			else
			{
				DataTable mainTable = wk.ToDataTableField(md.No);
				mainTable.TableName = "MainTable";
				myds.Tables.add(mainTable);
			}
			String sql = "";
			DataTable dt = null;
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 把外键表加入DataSet
			DataTable dtMapAttr = myds.Tables["Sys_MapAttr"];
			MapExts mes = md.MapExts;
			DataTable ddlTable = new DataTable();
			ddlTable.Columns.Add("No");

			for (DataRow dr : dtMapAttr.Rows)
			{
				String lgType = dr.get("LGType").toString();
				String uiBindKey = dr.get("UIBindKey").toString();

				if (DataType.IsNullOrEmpty(uiBindKey) == true)
				{
					continue; //为空就continue.
				}

				if (lgType.equals("1") == true)
				{
					continue; //枚举值就continue;
				}

				String uiIsEnable = dr.get("UIIsEnable").toString();
				if (uiIsEnable.equals("0") == true && lgType.equals("1") == true)
				{
					continue; //如果是外键，并且是不可以编辑的状态.
				}

				if (uiIsEnable.equals("0") == true && lgType.equals("0") == true)
				{
					continue; //如果是外部数据源，并且是不可以编辑的状态.
				}

				// 检查是否有下拉框自动填充。
				String keyOfEn = dr.get("KeyOfEn").toString();
				String fk_mapData = dr.get("FK_MapData").toString();


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#region 处理下拉框数据范围. for 小杨.
				Object tempVar2 = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL, MapExtAttr.AttrOfOper, keyOfEn);
				me = tempVar2 instanceof MapExt ? (MapExt)tempVar2 : null;
				if (me != null && myds.Tables.Contains(keyOfEn) == false)
				{
					Object tempVar3 = me.Doc.Clone();
					String fullSQL = tempVar3 instanceof String ? (String)tempVar3 : null;
					fullSQL = fullSQL.replace("~", ",");
					fullSQL = BP.WF.Glo.DealExp(fullSQL, wk, null);
					dt = DBAccess.RunSQLReturnTable(fullSQL);
					//重构新表
					DataTable dt_FK_Dll = new DataTable();
					dt_FK_Dll.TableName = keyOfEn; //可能存在隐患，如果多个字段，绑定同一个表，就存在这样的问题.
					dt_FK_Dll.Columns.Add("No", String.class);
					dt_FK_Dll.Columns.Add("Name", String.class);
					for (DataRow dllRow : dt.Rows)
					{
						DataRow drDll = dt_FK_Dll.NewRow();
						drDll.set("No", dllRow.get("No"));
						drDll.set("Name", dllRow.get("Name"));
						dt_FK_Dll.Rows.add(drDll);
					}
					myds.Tables.add(dt_FK_Dll);
					continue;
				}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion 处理下拉框数据范围.

				// 判断是否存在.
				if (myds.Tables.Contains(uiBindKey) == true)
				{
					continue;
				}

				DataTable mydt = BP.Sys.PubClass.GetDataTableByUIBineKey(uiBindKey);
				if (mydt == null)
				{
					DataRow ddldr = ddlTable.NewRow();
					ddldr.set("No", uiBindKey);
					ddlTable.Rows.add(ddldr);
				}
				else
				{
					myds.Tables.add(mydt);
				}


			}
			ddlTable.TableName = "UIBindKey";
			myds.Tables.add(ddlTable);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion End把外键表加入DataSet

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
						String msgAskFor = dr.get(TrackAttr.Msg).toString();
						String worker = dr.get(TrackAttr.EmpFrom).toString();
						String workerName = dr.get(TrackAttr.EmpFromT).toString();
						String rdt = dr.get(TrackAttr.RDT).toString();

						DataRow drMsg = dtAlert.NewRow();
						drMsg.set("Title", worker + "," + workerName + "回复信息:");
						drMsg.set("Msg", DataType.ParseText2Html(msgAskFor) + "<br>" + rdt);
						dtAlert.Rows.add(drMsg);
					}
					break;
				case Askfor: //加签.

					sql = "SELECT * FROM ND" + Integer.parseInt(fk_flow) + "Track WHERE WorkID=" + workID + " AND " + TrackAttr.ActionType + "=" + ActionType.AskforHelp.getValue();
					dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
					for (DataRow dr : dt.Rows)
					{
						String msgAskFor = dr.get(TrackAttr.Msg).toString();
						String worker = dr.get(TrackAttr.EmpFrom).toString();
						String workerName = dr.get(TrackAttr.EmpFromT).toString();
						String rdt = dr.get(TrackAttr.RDT).toString();

						DataRow drMsg = dtAlert.NewRow();
						drMsg.set("Title", worker + "," + workerName + "请求加签:");
						drMsg.set("Msg", DataType.ParseText2Html(msgAskFor) + "<br>" + rdt + "<a href='./WorkOpt/AskForRe.htm?FK_Flow=" + fk_flow + "&FK_Node=" + fk_node + "&WorkID=" + workID + "&FID=" + fid + "' >回复加签意见</a> --");
						dtAlert.Rows.add(drMsg);

						//提示信息.
						// this.FlowMsg.AlertMsg_Info(worker + "," + workerName + "请求加签:",
						//   DataType.ParseText2Html(msgAskFor) + "<br>" + rdt + " --<a href='./WorkOpt/AskForRe.aspx?FK_Flow=" + this.FK_Flow + "&FK_Node=" + this.FK_Node + "&WorkID=" + this.WorkID + "&FID=" + this.FID + "' >回复加签意见</a> --");
					}
					// isAskFor = true;
					break;
				case ReturnSta:
					/* 如果工作节点退回了*/
					ReturnWorks rws = new ReturnWorks();
					rws.Retrieve(ReturnWorkAttr.ReturnToNode, fk_node, ReturnWorkAttr.WorkID, workID, ReturnWorkAttr.RDT);

					if (rws.size() != 0)
					{
						//string msgInfo = "";
						//foreach (BP.WF.ReturnWork rw in rws)
						//{
						//    DataRow drMsg = dtAlert.NewRow();
						//    //drMsg["Title"] = "来自节点:" + rw.ReturnNodeName + " 退回人:" + rw.ReturnerName + "  " + rw.RDT + "&nbsp;<a href='/DataUser/ReturnLog/" + fk_flow + "/" + rw.MyPK + ".htm' target=_blank>工作日志</a>";
						//    drMsg["Title"] = "来自节点:" + rw.ReturnNodeName + " 退回人:" + rw.ReturnerName + "  " + rw.RDT;
						//    drMsg["Msg"] = rw.BeiZhuHtml;
						//    dtAlert.Rows.add(drMsg);
						//}

						String msgInfo = "";
						for (BP.WF.ReturnWork rw : rws)
						{
							//drMsg["Title"] = "来自节点:" + rw.ReturnNodeName + " 退回人:" + rw.ReturnerName + "  " + rw.RDT + "&nbsp;<a href='/DataUser/ReturnLog/" + fk_flow + "/" + rw.MyPK + ".htm' target=_blank>工作日志</a>";
							msgInfo += "\t\n来自节点:" + rw.getReturnNodeName() + " 退回人:" + rw.getReturnerName() + "  " + rw.getRDT();
							msgInfo += rw.getBeiZhuHtml();
						}

						String str = nd.getReturnAlert();
						if (!str.equals(""))
						{
							str = str.replace("~", "'");
							str = str.replace("@PWorkID", String.valueOf(workID));
							str = str.replace("@PNodeID", String.valueOf(nd.getNodeID()));
							str = str.replace("@FK_Node", String.valueOf(nd.getNodeID()));

							str = str.replace("@PFlowNo", fk_flow);
							str = str.replace("@FK_Flow", fk_flow);
							str = str.replace("@PWorkID", String.valueOf(workID));

							str = str.replace("@WorkID", String.valueOf(workID));
							str = str.replace("@OID", String.valueOf(workID));

							DataRow drMsg = dtAlert.NewRow();
							drMsg.set("Title", "退回信息");
							drMsg.set("Msg", msgInfo + "\t\n" + str);
							dtAlert.Rows.add(drMsg);
						}
						else
						{
							DataRow drMsg = dtAlert.NewRow();
							drMsg.set("Title", "退回信息");
							drMsg.set("Msg", msgInfo + "\t\n" + str);
							dtAlert.Rows.add(drMsg);
						}
					}
					break;
				case Shift:
					/* 判断移交过来的。 */
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
						drMsg.set("Title", "移交历史信息");
						msg = "";
						for (ShiftWork fw : fws)
						{
							String temp = "@移交人[" + fw.getFK_Emp() + "," + fw.getFK_EmpName() + "]。@接受人：" + fw.getToEmp() + "," + fw.getToEmpName() + "。<br>移交原因：-------------" + fw.getNoteHtml();
							if (fw.getFK_Emp().equals(WebUser.getNo()))
							{
								temp = "<b>" + temp + "</b>";
							}

							temp = temp.replace("@", "<br>@");
							msg += temp + "<hr/>";
						}
						drMsg.set("Msg", msg);
						dtAlert.Rows.add(drMsg);
					}
					break;
				default:
					break;
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 增加流程节点表单绑定信息.
			if (nd.getHisFormType() == NodeFormType.RefOneFrmTree)
			{
				/* 独立流程节点表单. */

				nd.WorkID = workID; //为获取表单ID ( NodeFrmID )提供参数.

				FrmNode fn = new FrmNode();
				fn.setMyPK( nd.getNodeFrmID() + "_" + nd.getNodeID() + "_" + nd.getFK_Flow();
				fn.Retrieve();
				myds.Tables.add(fn.ToDataTableField("FrmNode"));
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 增加流程节点表单绑定信息.


			myds.Tables.add(dtAlert);
			return myds;
		}
		catch (RuntimeException ex)
		{
			Log.DebugWriteError(ex.getStackTrace());
			throw new RuntimeException("generoWorkNode@" + ex.getMessage());
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
	public static DataSet GenerWorkNodeForAndroid111_del(String fk_flow, int fk_node, long workID, long fid, String userNo)
	{
		if (fk_node == 0)
		{
			fk_node = Integer.parseInt(fk_flow + "01");
		}

		if (workID == 0)
		{
			workID = BP.WF.Dev2Interface.Node_CreateBlankWork(fk_flow, null, null, userNo, null);
		}

		try
		{
			Emp emp = new Emp(userNo);
			WebUser.SignInOfGener(emp);

			MapData md = new MapData();
			md.No = "ND" + fk_node;
			if (md.RetrieveFromDBSources() == 0)
			{
				throw new RuntimeException("装载错误，该表单ID=" + md.No + "丢失，请修复一次流程重新加载一次.");
			}

			//表单模版.
			DataSet myds = BP.Sys.CCFormAPI.GenerHisDataSet(md.No);
			return myds;

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 流程设置信息.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 把主从表数据放入里面.
			//.工作数据放里面去, 放进去前执行一次装载前填充事件.
			BP.WF.Work wk = nd.getHisWork();
			wk.setOID(workID);
			wk.RetrieveFromDBSources();

			// 处理传递过来的参数。
			for (String k : HttpContextHelper.RequestParamKeys)
			{
				wk.SetValByKey(k, HttpContextHelper.RequestParams(k));
			}

			// 执行一次装载前填充.
			String msg = md.DoEvent(FrmEventList.FrmLoadBefore, wk);
			if (DataType.IsNullOrEmpty(msg) == false)
			{
				throw new RuntimeException("错误:" + msg);
			}

			wk.ResetDefaultVal();
			myds.Tables.add(wk.ToDataTableField(md.No));

			//把附件的数据放入.
			if (md.FrmAttachments.size() > 0)
			{
				sql = "SELECT * FROM Sys_FrmAttachmentDB where RefPKVal=" + workID + " AND FK_MapData='ND" + fk_node + "'";
				dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
				dt.TableName = "Sys_FrmAttachmentDB";
				myds.Tables.add(dt);
			}
			// 图片附件数据放入
			if (md.FrmImgAths.size() > 0)
			{
				sql = "SELECT * FROM Sys_FrmImgAthDB where RefPKVal=" + workID + " AND FK_MapData='ND" + fk_node + "'";
				dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
				dt.TableName = "Sys_FrmImgAthDB";
				myds.Tables.add(dt);
			}

			//把从表的数据放入.
			if (md.MapDtls.size() > 0)
			{
				for (MapDtl dtl : md.MapDtls)
				{
					GEDtls dtls = new GEDtls(dtl.No);
					QueryObject qo = null;
					try
					{
						qo = new QueryObject(dtls);
						switch (dtl.DtlOpenType)
						{
							case DtlOpenType.ForEmp: // 按人员来控制.
								qo.AddWhere(GEDtlAttr.RefPK, workID);
								qo.addAnd();
								qo.AddWhere(GEDtlAttr.Rec, WebUser.getNo());
								break;
							case DtlOpenType.ForWorkID: // 按工作ID来控制
								qo.AddWhere(GEDtlAttr.RefPK, workID);
								break;
							case DtlOpenType.ForFID: // 按流程ID来控制.
								qo.AddWhere(GEDtlAttr.FID, workID);
								break;
						}
					}
					catch (java.lang.Exception e)
					{
						dtls.GetNewEntity.CheckPhysicsTable();
					}
					DataTable dtDtl = qo.DoQueryToTable();

					// 为明细表设置默认值.
					MapAttrs dtlAttrs = new MapAttrs(dtl.No);
					for (MapAttr attr : dtlAttrs)
					{
						//处理它的默认值.
						if (attr.DefValReal.Contains("@") == false)
						{
							continue;
						}

						for (DataRow dr : dtDtl.Rows)
						{
							dr.set(attr.KeyOfEn, attr.DefVal);
						}
					}

					dtDtl.TableName = dtl.No; //修改明细表的名称.
					myds.Tables.add(dtDtl); //加入这个明细表, 如果没有数据，xml体现为空.
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 把外键表加入DataSet
			DataTable dtMapAttr = myds.Tables["Sys_MapAttr"];
			for (DataRow dr : dtMapAttr.Rows)
			{
				String lgType = dr.get("LGType").toString();
				if (lgType.equals("2") == false)
				{
					continue;
				}

				String UIIsEnable = dr.get("UIIsEnable").toString();
				if (UIIsEnable.equals("0") == true)
				{
					continue;
				}

				String uiBindKey = dr.get("UIBindKey").toString();
				if (DataType.IsNullOrEmpty(uiBindKey) == true)
				{
					String myPK = dr.get("MyPK").toString();
					/*如果是空的*/
					throw new RuntimeException("@属性字段数据不完整，流程:" + fl.No + fl.Name + ",节点:" + nd.getNodeID() + nd.getName() + ",属性:" + myPK + ",的UIBindKey IsNull ");
				}

				// 判断是否存在.
				if (myds.Tables.Contains(uiBindKey) == true)
				{
					continue;
				}

				myds.Tables.add(BP.Sys.PubClass.GetDataTableByUIBineKey(uiBindKey));
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion End把外键表加入DataSet

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
			//    myds.Tables.add(fws.ToDataTableField("WF_ShiftWork"));
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

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 把流程信息放入里面.

			return myds;
		}
		catch (RuntimeException ex)
		{
			Log.DebugWriteError(ex.getStackTrace());
			throw new RuntimeException(ex.getMessage());
		}
	}
}