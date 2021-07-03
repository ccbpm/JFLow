package bp.wf.template;
import bp.da.*;
import bp.difference.SystemConfig;
import bp.sys.*;
import bp.tools.StringHelper;
import bp.web.*;
import bp.wf.*;
import java.io.*;

/** 
 流程模版的操作
*/
public class TemplateGlo
{

	public static Node NewNode(String flowNo, int x, int y) throws Exception
	{
		return NewNode(flowNo, x, y, null);
	}


	public static Node NewNode(String flowNo, int x, int y, String icon) throws Exception
	{
		Flow flow = new Flow(flowNo);

		Node nd = new Node();
		int idx = DBAccess.RunSQLReturnValInt("SELECT COUNT(NodeID) FROM WF_Node WHERE FK_Flow='" + flowNo + "'",0);
		if (idx == 0)
		{
			idx++;
		}

		int nodeID = 0;
		//设置节点ID.
		while (true)
		{
			String strID = flowNo + StringHelper.padLeft(String.valueOf(idx), 2, '0');
			nd.setNodeID(Integer.parseInt(strID));
			if (nd.getIsExits() == false)
			{
				break;
			}
			idx++;
		}
		nodeID = nd.getNodeID();

		//增加了两个默认值值 . 2016.11.15. 目的是让创建的节点，就可以使用.
		nd.setCondModel(DirCondModel.SendButtonSileSelect); //默认的发送方向.
		nd.setHisDeliveryWay(DeliveryWay.BySelected); //上一步发送人来选择.
		nd.setFormType(NodeFormType.FoolForm); //设置为傻瓜表单.
		nd.setFK_Flow(flowNo);

		nd.Insert();

		//为创建节点设置默认值  @sly 部分方法
		String file = SystemConfig.getPathOfDataUser() + "/XML/DefaultNewNodeAttr.xml";
		DataSet ds = new DataSet();
		if ((new File(file)).isFile() == true)
		{
			ds.readXml(file);

			NodeExt ndExt = new NodeExt(nd.getNodeID());
			DataTable dt = ds.Tables.get(0);
			for (DataColumn dc : dt.Columns)
			{
				nd.SetValByKey(dc.ColumnName, dt.Rows.get(0).getValue(dc.ColumnName));
				ndExt.SetValByKey(dc.ColumnName, dt.Rows.get(0).getValue(dc.ColumnName));
			}

			ndExt.setFK_Flow(flowNo);
			ndExt.setNodeID(nodeID);
			ndExt.DirectUpdate();
		}
		nd.setFWCVer(1); //设置为2019版本. 2018版是1个节点1个人,仅仅显示1个意见.
		nd.setNodeID(nodeID);

		nd.setX(x);
		nd.setY(y);
		nd.setICON(icon);
		nd.setStep(idx);

		//节点类型.
		nd.setHisNodeWorkType(NodeWorkType.Work);
		nd.setName("New Node " + idx);
		nd.setHisNodePosType(NodePosType.Mid);
		nd.setFK_Flow(flow.getNo());
		nd.setFlowName(flow.getName());

		//设置审核意见的默认值.
		nd.SetValByKey(NodeWorkCheckAttr.FWCDefInfo, bp.wf.Glo.getDefValWFNodeFWCDefInfo());

		nd.Update(); //执行更新. @sly
		nd.CreateMap();

		//通用的人员选择器.
		bp.wf.template.Selector select = new bp.wf.template.Selector(nd.getNodeID());
		select.setSelectorModel(SelectorModel.GenerUserSelecter);
		select.Update();

		//设置默认值。

		//设置审核组件的高度
		DBAccess.RunSQL("UPDATE WF_Node SET FWC_H=300,FTC_H=300 WHERE NodeID='" + nd.getNodeID() + "'");

		//创建默认的推送消息.
		CreatePushMsg(nd);

		return nd;
	}
	private static void CreatePushMsg(Node nd) throws Exception
	{
		/*创建发送短消息,为默认的消息.*/
		bp.wf.template.PushMsg pm = new bp.wf.template.PushMsg();
		int i = pm.Retrieve(PushMsgAttr.FK_Event, EventListNode.SendSuccess, PushMsgAttr.FK_Node, nd.getNodeID(), PushMsgAttr.FK_Flow, nd.getFK_Flow());
		if (i == 0)
		{
			pm.setFK_Event(EventListNode.SendSuccess);
			pm.setFK_Node(nd.getNodeID());
			pm.setFK_Flow(nd.getFK_Flow());

			pm.setSMSPushWay(1); // 发送短消息.
			pm.setSMSPushModel("Email");
			pm.setMyPK(DBAccess.GenerGUID());
			pm.Insert();
		}

		//设置退回消息提醒.
		i = pm.Retrieve(PushMsgAttr.FK_Event, EventListNode.ReturnAfter, PushMsgAttr.FK_Node, nd.getNodeID(), PushMsgAttr.FK_Flow, nd.getFK_Flow());
		if (i == 0)
		{
			pm.setFK_Event(EventListNode.ReturnAfter);
			pm.setFK_Node(nd.getNodeID());
			pm.setFK_Flow(nd.getFK_Flow());

			pm.setSMSPushWay(1); // 发送短消息.
			pm.setMailPushWay(0); //不发送邮件消息.
			pm.setMyPK(DBAccess.GenerGUID());
			pm.Insert();
		}
	}

	/** 
	 创建一个流程模版
	 
	 @param flowSort 流程类别
	 @param flowName 名称
	 @param dsm 存储方式
	 @param ptable 物理量
	 @param flowMark 标记
	 @return 创建的流程编号
	 * @throws Exception 
	*/
	public static String NewFlow(String flowSort, String flowName, bp.wf.template.DataStoreModel dsm, String ptable, String flowMark) throws Exception
	{
		//定义一个变量.
		Flow flow = new Flow();
		try
		{
			//检查参数的完整性.
			if (DataType.IsNullOrEmpty(ptable) == false && ptable.length() >= 1)
			{
				String c = ptable.substring(0, 1);
				if (DataType.IsNumStr(c) == true)
				{
					throw new RuntimeException("@非法的流程数据表(" + ptable + "),它会导致ccflow不能创建该表.");
				}
			}

			flow.setHisDataStoreModel(dsm);
			flow.setPTable(ptable);
			flow.setFK_FlowSort(flowSort);
			flow.setFlowMark(flowMark);

			if (DataType.IsNullOrEmpty(flowMark) == false)
			{
				if (flow.IsExit(FlowAttr.FlowMark, flowMark))
				{
					throw new RuntimeException("@该流程标示:" + flowMark + "已经存在于系统中.");
				}
			}

			/*给初始值*/
			//this.Paras = "@StartNodeX=10@StartNodeY=15@EndNodeX=40@EndNodeY=10";
			flow.setParas("@StartNodeX=200@StartNodeY=50@EndNodeX=200@EndNodeY=350");

			flow.setNo(flow.GenerNewNoByKey(FlowAttr.No));
			flow.setName( flowName);
			if (DataType.IsNullOrEmpty(flow.getName()))
			{
				flow.setName("新建流程" + flow.getNo()); //新建流程.
			}

			if (flow.getIsExits() == true)
			{
				throw new RuntimeException("err@系统出现自动生成的流程编号重复.");
			}

			if (bp.wf.Glo.getCCBPMRunModel() != CCBPMRunModel.Single)
			{
				flow.setOrgNo(WebUser.getOrgNo()); //隶属组织
			}
			flow.Insert();

			bp.wf.Node nd = new bp.wf.Node();
			nd.setNodeID(Integer.parseInt(flow.getNo() + "01"));
			nd.setName("Start Node"); //  "开始节点";
			nd.setStep(1);
			nd.setFK_Flow(flow.getNo());
			nd.setFlowName(flow.getName());
			nd.setHisNodePosType(NodePosType.Start);
			nd.setHisNodeWorkType(NodeWorkType.StartWork);
			nd.setX(200);
			nd.setY(150);
			nd.setNodePosType(NodePosType.Start);
			nd.setICON("前台");

			//增加了两个默认值值 . 2016.11.15. 目的是让创建的节点，就可以使用.
			nd.setCondModel(DirCondModel.SendButtonSileSelect); //默认的发送方向.
			nd.setHisDeliveryWay(DeliveryWay.BySelected); //上一步发送人来选择.
			nd.setFormType(NodeFormType.FoolForm); //设置为傻瓜表单.

			//如果是集团模式.   
			if (bp.wf.Glo.getCCBPMRunModel() == CCBPMRunModel.GroupInc)
			{
				if (DataType.IsNullOrEmpty(WebUser.getOrgNo()) == true)
				{
					throw new RuntimeException("err@登录信息丢失了组织信息,请重新登录.");
				}

				nd.setHisDeliveryWay(DeliveryWay.BySelectedOrgs);

				//把本组织加入进去.
				FlowOrg fo = new FlowOrg();
				fo.Delete(FlowOrgAttr.FlowNo, nd.getFK_Flow());
				fo.setFlowNo(nd.getFK_Flow());
				fo.setOrgNo(WebUser.getOrgNo());
				fo.Insert();
			}

			nd.Insert();
			nd.CreateMap();

			//为开始节点增加一个删除按钮. @李国文.
			String sql = "UPDATE WF_Node SET DelEnable=1 WHERE NodeID=" + nd.getNodeID();
			DBAccess.RunSQL(sql);

			//nd.HisWork.CheckPhysicsTable();  去掉，检查的时候会执行.
			CreatePushMsg(nd);

			//通用的人员选择器.
			bp.wf.template.Selector select = new bp.wf.template.Selector(nd.getNodeID());
			select.setSelectorModel(SelectorModel.GenerUserSelecter);
			select.Update();

			nd = new bp.wf.Node();

			//为创建节点设置默认值 
			String fileNewNode = SystemConfig.getPathOfDataUser() + "/XML/DefaultNewNodeAttr.xml";
			if ((new File(fileNewNode)).isFile() == true)
			{
				DataSet myds = new DataSet();
				myds.readXml(fileNewNode);
				DataTable dt = myds.Tables.get(0);
				for (DataColumn dc : dt.Columns)
				{
					nd.SetValByKey(dc.ColumnName, dt.Rows.get(0).getValue(dc.ColumnName));
				}
			}
			else
			{
				nd.setHisNodePosType(NodePosType.Mid);
				nd.setHisNodeWorkType(NodeWorkType.Work);
				nd.setX(200);
				nd.setY(250);
				nd.setICON("审核");
				nd.setNodePosType(NodePosType.End);

				//增加了两个默认值值 . 2016.11.15. 目的是让创建的节点，就可以使用.
				nd.setCondModel(DirCondModel.SendButtonSileSelect); //默认的发送方向.
				nd.setHisDeliveryWay(DeliveryWay.BySelected); //上一步发送人来选择.
				nd.setFormType(NodeFormType.FoolForm); //设置为傻瓜表单.
			}

			nd.setNodeID(Integer.parseInt(flow.getNo() + "02"));
			nd.setName("Node 2"); // "结束节点";
			nd.setStep(2);
			nd.setFK_Flow(flow.getNo());
			nd.setFlowName(flow.getName());
			nd.setHisDeliveryWay(DeliveryWay.BySelected); //上一步发送人来选择.
			nd.setFormType(NodeFormType.FoolForm); //设置为傻瓜表单.

			nd.setX(200);
			nd.setY(250);

			//设置审核意见的默认值.
			nd.SetValByKey(NodeWorkCheckAttr.FWCDefInfo,bp.wf.Glo.getDefValWFNodeFWCDefInfo());

			nd.Insert();
			nd.CreateMap();
			//nd.HisWork.CheckPhysicsTable(); //去掉，检查的时候会执行.
			CreatePushMsg(nd);

			//通用的人员选择器.
			select = new bp.wf.template.Selector(nd.getNodeID());
			select.setSelectorModel(SelectorModel.GenerUserSelecter);
			select.Update();

			MapData md = new MapData();
			md.setNo("ND" + Integer.parseInt(flow.getNo()) + "Rpt");
			md.setName( flow.getName());
			md.Save();

			// 装载模版.
			String file = SystemConfig.getPathOfDataUser() + "XML/TempleteSheetOfStartNode.xml";
			if ((new File(file)).isFile() == false && SystemConfig.getIsJarRun()==false)
			{
				throw new RuntimeException("@开始节点表单模版丢失" + file);
			}

			/*如果存在开始节点表单模版*/
			DataSet ds = new DataSet();
			ds.readXml(file);

			String nodeID = "ND" + Integer.parseInt(flow.getNo() + "01");
			MapData.ImpMapData(nodeID, ds);

			//创建track.
			Track.CreateOrRepairTrackTable(flow.getNo());


		}
		catch (RuntimeException ex)
		{
			/**删除垃圾数据.
			*/
			flow.DoDelete();
			//提示错误.
			throw new RuntimeException("err@创建流程错误:" + ex.getMessage());
		}


		FlowExt flowExt = new FlowExt(flow.getNo());
		flowExt.setDesignerNo(WebUser.getNo());
		flowExt.setDesignerName(WebUser.getName());
		flowExt.setDesignTime(DataType.getCurrentDateByFormart("yyyy-MM-dd HH:mm:ss"));
		flowExt.DirectSave();

		//创建连线
		Direction drToNode = new Direction();
		drToNode.setFK_Flow(flow.getNo());
		drToNode.setNode(Integer.parseInt(Integer.parseInt(flow.getNo()) + "01"));
		drToNode.setToNode(Integer.parseInt(Integer.parseInt(flow.getNo()) + "02"));
		drToNode.Insert();

		//增加方向.
		Node mynd = new Node(drToNode.getNode());
		mynd.setHisToNDs(String.valueOf(drToNode.getToNode()));
		mynd.Update();


		//设置流程的默认值.
		for (String key : SystemConfig.getAppSettings().keySet())
		{
			if (key.contains("NewFlowDefVal") == false)
			{
				continue;
			}

			String val = SystemConfig.GetValByKey(key, "");

			//设置值.
			flow.SetValByKey(key.replace("NewFlowDefVal_",""), val);
		}

		//执行一次流程检查, 为了节省效率，把检查去掉了.
		flow.DoCheck();
		return flow.getNo();
	}
	/** 
	 删除节点.
	 
	 @param nodeid
	 * @throws Exception 
	*/
	public static void DeleteNode(int nodeid) throws Exception
	{
		Node nd = new Node(nodeid);
		nd.Delete();
	}
}