package BP ;


@SuppressWarnings("unused")
public class Test001 
{
//	/** 
//	 测试财务报销流程
//	 
//	*/
//	public Test001()
//	{
////		this.Title = "测试财务报销流程";
////		this.DescIt = "流程的正常运转与表单字段的控制的方向条件.";
////		this.EditState = CT.EditState.Passed;
//	}
//	/** 
//	 说明 ：此测试针对于演示环境中的 001 流程编写的单元测试代码。
//	 涉及到了: 创建，发送，撤销，方向条件、退回等功能。
//	 
//	*/
//	//@Override
//	public void Do()
//	{
//		String fk_flow = "001";
//		String userNo = "zhoutianjiao";
//
//		Flow fl = new Flow(fk_flow);
//
//		// zhoutianjiao 登录.
//		BP.WF.Dev2Interface.Port_Login(userNo);
//
//		//创建空白工作, 发起开始节点.
//		long workid = BP.WF.Dev2Interface.Node_CreateBlankWork(fk_flow, null, null, WebUser.getNo(), null, 0,null,0,null);
//
//
//		///#region 检查发起流程后的数据是否完整？
//		//检查创建这个空白是否有数据完整?
//		String sql = "SELECT * FROM " + fl.getPTable() + " WHERE OID=" + workid;
//		DataTable dt = DBAccess.RunSQLReturnTable(sql);
//		if (dt.Rows.size() == 0)
//		{
//			throw new RuntimeException("@发起流程出错误,不应该找不到报表数据.");
//		}
//
//		//检查节点表单表是否有数据？
//		sql = "SELECT * FROM ND101 WHERE OID=" + workid;
//		dt = DBAccess.RunSQLReturnTable(sql);
//		if (dt.Rows.size() == 0)
//		{
//			throw new RuntimeException("@不应该在开始节点表单表中找不到数据，");
//		}
//
//		///#endregion 检查发起流程后的数据是否完整？
//
//		//执行发送，并获取发送对象,.
//		SendReturnObjs objs = BP.WF.Dev2Interface.Node_SendWork(fk_flow, workid);
//
//
//		///#region 检查发送对象返回的信息是否完整？
//		//从获取的发送对象里获取到下一个工作者. 
//		String nextUser = objs.getVarAcceptersID();
//		if (!nextUser.equals("qifenglin"))
//		{
//			throw new RuntimeException(WebUser.getNo() + "启动财务报销流程，下一步的接受人不正确, 应该是qifenglin,他是部门的负责人.现在是:" + nextUser);
//		}
//
//		if (objs.getVarToNodeID() != 102)
//		{
//			throw new RuntimeException(WebUser.getNo() + "启动财务报销流程，下一步的节点, 应该是102.现在是:" + objs.getVarToNodeID());
//		}
//
//
//		//检查节点表单表是否有数据？
//		sql = "SELECT * FROM ND102 WHERE OID=" + workid;
//		dt = DBAccess.RunSQLReturnTable(sql);
//		if (dt.Rows.size() == 0)
//		{
//			throw new RuntimeException("@不应该在部门经理审批表节点表单表中找不到数据，");
//		}
//
//
//		///#endregion 检查发送对象返回的信息是否完整?
//
//		//检查开始节点的执行撤销是否可用？
//		String info = BP.WF.Dev2Interface.Flow_DoUnSend(fk_flow, workid);
//
//
//		///#region 检查撤销发送是否符合预期.
//		//检查节点表单表是否有数据？
//		sql = "SELECT * FROM ND102 WHERE OID=" + workid;
//		dt = DBAccess.RunSQLReturnTable(sql);
//		if (dt.Rows.size() == 1)
//		{
//			throw new RuntimeException("@已经撤销了，当前节点表单数据也应该删除，但是ccflow没有删除它。");
//		}
//
//		//查询流程注册表.
//		GenerWorkFlow gwf = new GenerWorkFlow();
//		gwf.setWorkID(workid);
//		gwf.RetrieveFromDBSources();
//		if (gwf.getFK_Node() != fl.getStartNodeID())
//		{
//			throw new RuntimeException("@撤销发送后的数据不符合预期: 没有停留到开始节点上,而是停留在了:" + gwf.getFK_Node());
//		}
//
//		GenerWorkerList gwl = new GenerWorkerList();
//		gwl.setFK_Emp(WebUser.getNo());
//		gwl.setFK_Node(fl.getStartNodeID());
//		gwl.setWorkID(workid);
//		gwl.Retrieve();
//		if (gwl.getIsPass())
//		{
//			throw new RuntimeException("@撤销发送后的数据不符合预期: 当前的操作人员的停留状态应该是未发送，现在是已发送.");
//		}
//
//		//检查qifenglin的待办工作是否还存在.
//		gwl = new GenerWorkerList();
//		gwl.setFK_Emp(nextUser);
//		gwl.setFK_Node(fl.getStartNodeID());
//		gwl.setWorkID(workid);
//		if (gwl.RetrieveFromDBSources() != 0)
//		{
//			throw new RuntimeException("@撤销发送后的数据不符合预期: 撤销后的接受人的待办工作不应该存在.");
//		}
//
//		//通检查数据符合预期结果。
//		BP.WF.Dev2Interface.Node_SendWork(fk_flow, workid);
//
//		///#endregion 检查撤销发送是否符合预期
//
//		// 让qifenglin 登录.
//		BP.WF.Dev2Interface.Port_Login(nextUser);
//
//		//按小于.HeJiFeiYong < 10000 时向下发送。
//		java.util.Hashtable ht = new java.util.Hashtable();
//		ht.put("HeJiFeiYong", 900);
//		objs = BP.WF.Dev2Interface.Node_SendWork(fk_flow, workid, ht);
//
//
//		///#region 检查 HeJiFeiYong < 1000 向下发送时是否发送到财务部门上去了。
//		//检查节点表单表是否有数据？
//		sql = "SELECT HeJiFeiYong FROM ND102 WHERE OID=" + workid;
//		dt = DBAccess.RunSQLReturnTable(sql);
//		if (dt.Rows.size() != 1)
//		{
//			throw new RuntimeException("@没有找到节点2的数据。");
//		}
//
//		int je = Integer.parseInt(dt.getValue(0, 0).toString());
//		if (je != 900)
//		{
//			throw new RuntimeException("@ht 数据没有传入到节点表里面去,注意如果是自动填充字段将不能写入里面。");
//		}
//
//		if ( ! objs.getVarAcceptersID().equals("yangyilei"))
//		{
//			throw new RuntimeException("@当合计费用小于 10000 时，执行结果不符合预期. 应该提交给财务部经理yangyilei审批.现在是:" + objs.getVarAcceptersID());
//		}
//
//		//检查yangyilei 是否有待办？
//		sql = "SELECT FK_Emp FROM WF_EmpWorks WHERE WorkID=" + workid;
//		dt = DBAccess.RunSQLReturnTable(sql);
//		if (dt.Rows.size() != 1 || !dt.getValue(0, 0).toString().equals(objs.getVarAcceptersID()))
//		{
//			throw new RuntimeException("@执行结果不符合预期: 应该只有一个待办并且是: yangyilei.");
//		}
//
//		//检查完毕执行撤销发送， 目的是检查当数据 大于1万的方向条件.
//		BP.WF.Dev2Interface.Flow_DoUnSend(fk_flow, workid);
//
//		//检查qifenglin 是否有待办？
//		sql = "SELECT FK_Emp FROM WF_EmpWorks WHERE WorkID=" + workid;
//		dt = DBAccess.RunSQLReturnTable(sql);
//		if (dt.Rows.size() != 1 || !dt.getValue(0, 0).toString().equals("qifenglin"))
//		{
//			throw new RuntimeException("@在节点2执行撤销时,数据不符合预期:  qifenglin.");
//		}
//
//		///#endregion
//
//		//按小于.HeJiFeiYong > 10000 时向下发送。
//		ht = new java.util.Hashtable();
//		ht.put("HeJiFeiYong", 990999);
//		objs = BP.WF.Dev2Interface.Node_SendWork(fk_flow, workid, ht);
//
//
//		///#region 检查当合计费用大于10000 时，数据是否符合预期.
//		if ( ! objs.getVarAcceptersID().equals("zhoupeng"))
//		{
//			throw new RuntimeException("@在节点2时,数据大于1万,方向条件不符合预期, 应该执行到zhoupeng 现在转入了:" + objs.getVarAcceptersID());
//		}
//
//		//检查zhoupeng 是否有待办？
//		sql = "SELECT FK_Emp FROM WF_EmpWorks WHERE WorkID=" + workid;
//		dt = DBAccess.RunSQLReturnTable(sql);
//		if (dt.Rows.size() != 1)
//		{
//			throw new RuntimeException("@在节点2时,数据大于1万,方向条件不符合预期, 应该执行到zhoupeng一个人, 现在查询的待办结果不对, 执行的查询sql:" + sql);
//		}
//
//		if (!dt.getValue(0, 0).toString().equals("zhoupeng"))
//		{
//			throw new RuntimeException("@在节点2时,数据大于1万,方向条件不符合预期, 应该是zhoupeng 执行的查询sql:" + sql);
//		}
//
//		// 检查是否转到了 103 节点上去了？
//		gwf = new GenerWorkFlow();
//		gwf.setWorkID(workid);
//		gwf.RetrieveFromDBSources();
//		if (gwf.getFK_Node() != 103)
//		{
//			throw new RuntimeException("@方向条件没有转入到 103上，目前转入到了:" + gwf.getFK_Node());
//		}
//
//		sql = "SELECT HeJiFeiYong FROM ND103 WHERE OID=" + workid;
//		dt = DBAccess.RunSQLReturnTable(sql);
//		if (dt.Rows.size() != 1)
//		{
//			throw new RuntimeException("@没有找到节点(总经理审批)的数据。");
//		}
//
//		///#endregion 检查当合计费用大于10000 时，数据是否符合预期.
//
//		// 让zhoupeng 登录.
//		BP.WF.Dev2Interface.Port_Login("zhoupeng");
//
//		//执行发送.
//		objs = BP.WF.Dev2Interface.Node_SendWork(fk_flow, workid);
//
//
//		///#region 检查总经理审批节点发送后数据是否符合预期？
//		if ( ! objs.getVarAcceptersID().equals("yangyilei"))
//		{
//			throw new RuntimeException("@应该发送到 yangyilei 但是没有发送给他.");
//		}
//
//		//检查qifenglin 是否有待办？
//		sql = "SELECT FK_Emp FROM WF_EmpWorks WHERE WorkID=" + workid;
//		dt = DBAccess.RunSQLReturnTable(sql);
//		if (dt.Rows.size() != 1 || !dt.getValue(0, 0).toString().equals("yangyilei"))
//		{
//			throw new RuntimeException("@应该发送到 yangyilei 但是没有发送给他.");
//		}
//
//		//检查报表数据是否完整？
//		sql = "SELECT FlowEnder,FlowEndNode,FlowStarter,WFState,FID,FK_NY FROM  ND1Rpt WHERE OID=" + workid;
//		dt = DBAccess.RunSQLReturnTable(sql);
//		if (!dt.getValue(0, "FlowEnder").toString().equals("zhoupeng"))
//		{
//			throw new RuntimeException("@应该是 zhoupeng 是 FlowEnder .");
//		}
//
//		if (!dt.getValue(0, "FlowStarter").toString().equals("zhoutianjiao"))
//		{
//			throw new RuntimeException("@应该是 zhoutianjiao 是 FlowStarter .");
//		}
//
//		if (!dt.getValue(0, "FlowEndNode").toString().equals("104"))
//		{
//			throw new RuntimeException("@应该是 104 是 FlowEndNode .");
//		}
//
//		if (Integer.parseInt(dt.getValue(0, "WFState").toString()) != Integer.parseInt(WFState.Runing.toString()))
//		{
//			throw new RuntimeException("@应该是 WFState.Runing 是 WFState .");
//		}
//
//		if (Integer.parseInt(dt.getValue(0, "FID").toString()) != 0)
//		{
//			throw new RuntimeException("@应该是 FID =0");
//		}
//
//		if (!dt.getValue(0, "FK_NY").toString().equals(DataType.getCurrentYearMonth()))
//		{
//			throw new RuntimeException("@ FK_NY 字段填充错误. ");
//		}
//
//		///#endregion 检查总经理审批节点发送后数据是否符合预期
//
//		// 让yangyilei 登录.
//		BP.WF.Dev2Interface.Port_Login("yangyilei");
//
//		//执行发送, 这个是最后节点，它应该自动结束。
//		objs = BP.WF.Dev2Interface.Node_SendWork(fk_flow, workid);
//
//
//		///#region 检查结束节点是否符合预期？
//		gwf = new GenerWorkFlow();
//		gwf.setWorkID(workid);
//		if (gwf.RetrieveFromDBSources() != 0)
//		{
//			throw new RuntimeException("@ 流程结束后 WF_GenerWorkFlow 没有删除. ");
//		}
//
//		sql = "SELECT * FROM wf_GenerWorkerList WHERE WORKID=" + workid;
//		dt = DBAccess.RunSQLReturnTable(sql);
//		if (dt.Rows.size() != 0)
//		{
//			throw new RuntimeException("@ 流程结束后 wf_GenerWorkerList 没有删除. ");
//		}
//
//		//检查报表数据是否完整？
//		sql = "SELECT FlowEnder,FlowEndNode,FlowStarter,WFState,FID,FK_NY FROM  ND1Rpt WHERE OID=" + workid;
//		dt = DBAccess.RunSQLReturnTable(sql);
//		if (!dt.getValue(0, "FlowEnder").toString().equals("yangyilei"))
//		{
//			throw new RuntimeException("@应该是 yangyilei 是 FlowEnder .");
//		}
//
//		if (!dt.getValue(0, "FlowStarter").toString().equals("zhoutianjiao"))
//		{
//			throw new RuntimeException("@应该是 zhoutianjiao 是 FlowStarter .");
//		}
//
//		if (!dt.getValue(0, "FlowEndNode").toString().equals("104"))
//		{
//			throw new RuntimeException("@应该是 104 是 FlowEndNode .");
//		}
//
//		if (Integer.parseInt(dt.getValue(0, "WFState").toString()) != Integer.parseInt(WFState.Complete.toString()))
//		{
//			throw new RuntimeException("@应该是 WFState.Complete 是 WFState .");
//		}
//
//		if (Integer.parseInt(dt.getValue(0, "FID").toString()) != 0)
//		{
//			throw new RuntimeException("@应该是 FID =0");
//		}
//
//		if (!dt.getValue(0, "FK_NY").toString().equals(DataType.getCurrentYearMonth()))
//		{
//			throw new RuntimeException("@ FK_NY 字段填充错误. ");
//		}
//
//		///#endregion 检查结束节点是否符合预期
//
//	}
}