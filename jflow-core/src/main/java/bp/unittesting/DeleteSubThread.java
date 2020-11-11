package bp.unittesting;

import bp.wf.*;
import bp.wf.data.*;
import bp.wf.template.*;
import bp.en.*;
import bp.da.*;
import bp.web.*;

import java.math.BigDecimal;
import java.util.*;

public class DeleteSubThread extends TestBase
{
	/** 
	 子线程的删除
	*/
	public DeleteSubThread()
	{
		this.Title = "子线程的删除";
		this.DescIt = "流程:005月销售总结(同表单分合流),删除一个子线程是否符合预期的要求.";
		this.editState = EditState.Passed;
	}


		///变量
	/** 
	 流程编号
	*/
	public String fk_flow = "";
	/** 
	 用户编号
	*/
	public String userNo = "";
	/** 
	 所有的流程
	*/
	public Flow fl = null;
	/** 
	 主线程ID
	*/
	public long workid = 0;
	/** 
	 发送后返回对象
	*/
	public SendReturnObjs objs = null;
	/** 
	 工作人员列表
	*/
	public GenerWorkerList gwl = null;
	/** 
	 流程注册表
	*/
	public GenerWorkFlow gwf = null;

		/// 变量

	/** 
	 测试案例说明:
	 1, 此流程针对于最简单的分合流程进行， zhanghaicheng发起，zhoushengyu,zhangyifan,两个人处理子线程，
		zhanghaicheng 接受子线程汇总数据.
	 2, 测试方法体分成三大部分. 发起，子线程处理，合流点执行，分别对应: Step1(), Step2_1(), Step2_2()，Step3() 方法。
	 3，针对发送测试，不涉及到其它的功能.
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{
		//初始化变量.
		fk_flow = "005";
		userNo = "zhanghaicheng";

		fl = new Flow(fk_flow);

		//执行第1步检查，创建工作与发送.
		this.Step1();

		//执行第2_1步检查，zhoushengyu的发送结果.
		this.Step2_1();

		////执行第2_2步检查，zhangyifan的发送结果.
		//this.Step2_2();

		//删除 zhangyifan 的进程.
		this.Step3();
	}
	/** 
	 创建流程，发送分流点第1步.
	 * @throws Exception 
	*/
	public final void Step1() throws Exception
	{
		// 让zhanghaicheng 登录.
		bp.wf.Dev2Interface.Port_Login(userNo);

		//创建空白工作, 发起开始节点.
		workid = bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow);


			///检查 创建流程后的数据是否完整 ？
		// "检查创建这个空白是否有数据完整?;
		sql = "SELECT * FROM " + fl.getPTable() + " WHERE OID=" + workid;
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到报表数据.");
		}

		// 检查节点表单表是否有数据?;
		sql = "SELECT * FROM ND501 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			throw new RuntimeException("@不应该在开始节点表单表中找不到数据，");
		}

		if (!dt.Rows.get(0).getValue("Rec").toString().equals(WebUser.getNo()))
		{
			throw new RuntimeException("@记录人应该是当前人员.");
		}

		// 检查创建这个空白是否有数据完整?;
		sql = "SELECT * FROM WF_EmpWorks WHERE WorkID=" + workid + " AND FK_Emp='" + WebUser.getNo() + "'";
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 0)
		{
			throw new RuntimeException("@找到当前人员的待办就是错误的.");
		}

			/// 检查发起流程后的数据是否完整？

		//开始节点:执行发送,并获取发送对象. 主线程向子线程发送.
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workid);


			///第1步: 检查【开始节点】发送对象返回的信息是否完整？
		//从获取的发送对象里获取到下一个工作者. zhangyifan(张一帆)、zhoushengyu(周升雨).
		if (!objs.getVarAcceptersID().equals("zhangyifan,zhoushengyu,"))
		{
			throw new RuntimeException("@下一步的接受人不正确,  zhangyifan,zhoushengyu, 现在是:" + objs.getVarAcceptersID());
		}

		if (objs.getVarToNodeID() != 502)
		{
			throw new RuntimeException("@应该是 502节点. 现在是:" + objs.getVarToNodeID());
		}

		if (objs.getVarWorkID() != workid)
		{
			throw new RuntimeException("@主线程的workid不应该变化:" + objs.getVarWorkID());
		}

		if (objs.getVarCurrNodeID() != 501)
		{
			throw new RuntimeException("@当前节点的编号不能变化:" + objs.getVarCurrNodeID());
		}

		if (objs.getVarTreadWorkIDs() == null)
		{
			throw new RuntimeException("@没有获取到两条子线程ID.");
		}

		if (objs.getVarTreadWorkIDs().contains(",") == false)
		{
			throw new RuntimeException("@没有获取到两条子线程的WorkID:" + objs.getVarTreadWorkIDs());
		}

			///  检查【开始节点】发送对象返回的信息是否完整？


			///第2步: 检查流程引擎控制系统表是否符合预期.
		gwf = new GenerWorkFlow(workid);
		if (gwf.getFK_Node() != 501)
		{
			throw new RuntimeException("@主线程向子线程发送时，主线程的FK_Node应该不变化，现在：" + gwf.getFK_Node());
		}

		if (gwf.getWFState() != WFState.Runing)
		{
			throw new RuntimeException("@主线程向子线程发送时，主线程的 WFState 应该 WFState.Runing ：" + gwf.getWFState().toString());
		}

		if (!gwf.getStarter().equals(WebUser.getNo()))
		{
			throw new RuntimeException("@应该是发起人员，现在是:" + gwf.getStarter());
		}

		//找出发起人的工作列表.
		gwl = new GenerWorkerList(workid, 501, WebUser.getNo());
		if (gwl.getIsPass() == true)
		{
			throw new RuntimeException("@干流上的pass状态应该是通过,此人已经没有他的待办工作了.");
		}

		//找出子线程上的工作人员.
		GenerWorkFlows gwfs = new GenerWorkFlows();
		gwfs.Retrieve(GenerWorkerListAttr.FID, workid);
		if (gwfs.size() != 2)
		{
			throw new RuntimeException("@应该有两个流程注册，现在是：" + gwfs.size() + "个.");
		}

		//检查它们的注册数据是否完整.
		for (GenerWorkFlow item : gwfs.ToJavaList())
		{
			if (!item.getStarter().equals(WebUser.getNo()))
			{
				throw new RuntimeException("@当前的人员应当是发起人,现在是:" + item.getStarter());
			}

			if (item.getFK_Node() != 502)
			{
				throw new RuntimeException("@当前节点应当是 502 ,现在是:" + item.getFK_Node());
			}

			if (item.getWFState() != WFState.Runing)
			{
				throw new RuntimeException("@当前 WFState 应当是 Runing ,现在是:" + item.getWFState().toString());
			}
		}

		//找出子线程工作处理人员的工作列表.
		GenerWorkerLists gwls = new GenerWorkerLists();
		gwls.Retrieve(GenerWorkerListAttr.FID, workid);
		if (gwls.size() != 2)
		{
			throw new RuntimeException("@应该在子线程上查询出来两个待办，现在只有(" + gwls.size() + ")个。");
		}

		//检查子线程的待办完整性.
		for (GenerWorkerList item : gwls.ToJavaList())
		{
			if (item.getIsPass())
			{
				throw new RuntimeException("@不应该是已经通过，因为他们没有处理。");
			}

			if (item.getIsEnable() == false)
			{
				throw new RuntimeException("@应该是：IsEnable ");
			}

			//if (item.Sender.Contains(WebUser.getNo()) == false)
			//    throw new Exception("@发送人，应该是当前人员。现在是:" + item.Sender);

			if (!item.getFK_Flow().equals("005"))
			{
				throw new RuntimeException("@应该是 005 现在是:" + item.getFK_Flow());
			}

			if (item.getFK_Node() != 502)
			{
				throw new RuntimeException("@应该是 502 现在是:" + item.getFK_Node());
			}
		}

		//取主线程的待办工作.
		sql = "SELECT * FROM WF_EmpWorks WHERE WorkID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 0)
		{
			throw new RuntimeException("@不应当出现主线程的待办在 WF_EmpWorks 视图中. " + sql);
		}

		//取待办子线程的待办工作.
		sql = "SELECT * FROM WF_EmpWorks WHERE FID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 2)
		{
			throw new RuntimeException("@应该取出来两个子线程的 WF_EmpWorks 视图中. " + sql);
		}


			/// end 检查流程引擎控制系统表是否符合预期.


			///第3步: 检查【开始节点】发送节点表单-数据信息否完整？
		//检查节点表单表是否有数据？
		sql = "SELECT * FROM ND501 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@应该找到开始节点表单数据，但是没有。");
		}

		if (!dt.Rows.get(0).getValue("Rec").toString().equals(WebUser.getNo()))
		{
			throw new RuntimeException("@没有向主线程开始节点表里写入Rec字段，现在是：" + dt.Rows.get(0).getValue("Rec").toString() + "应当是:" + WebUser.getNo());
		}

		//检查节点表单表是否有数据，以及数据是否正确？
		sql = "SELECT * FROM ND502 WHERE FID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 2)
		{
			throw new RuntimeException("@应该在第一个子线程节点上找到两个数据。");
		}
		for (DataRow dr : dt.Rows)
		{
			if (dr.getValue("Rec").toString().equals("zhangyifan"))
			{
				continue;
			}
			if (dr.getValue("Rec").toString().equals("zhoushengyu"))
			{
				continue;
			}
			throw new RuntimeException("@子线程表单数据没有正确的写入Rec字段.");
		}


		sql = "SELECT * FROM  ND5Rpt WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (!dt.Rows.get(0).getValue(GERptAttr.FlowEnder).toString().equals("zhanghaicheng"))
		{
			throw new RuntimeException("@应该是 zhanghaicheng 是 FlowEnder .");
		}

		if (!dt.Rows.get(0).getValue(GERptAttr.FlowStarter).toString().equals("zhanghaicheng"))
		{
			throw new RuntimeException("@应该是 zhanghaicheng 是 FlowStarter .");
		}

		if (!dt.Rows.get(0).getValue(GERptAttr.FlowEndNode).toString().equals("502"))
		{
			throw new RuntimeException("@应该是 502 是 FlowEndNode .");
		}

		if (Integer.parseInt(dt.Rows.get(0).getValue(GERptAttr.WFState).toString()) != WFState.Runing.getValue())
		{
			throw new RuntimeException("@应该是 WFState.Runing 是当前的状态。");
		}

		if (Integer.parseInt(dt.Rows.get(0).getValue(GERptAttr.FID).toString()) != 0)
		{
			throw new RuntimeException("@应该是 FID =0 ");
		}

		if (!dt.Rows.get(0).getValue("FK_NY").toString().equals(DataType.getCurrentYearMonth()))
		{
			throw new RuntimeException("@ FK_NY 字段填充错误. ");
		}

			///  检查【开始节点】发送数据信息否完整？
	}
	/** 
	 让子线程中的一个人 zhoushengyu 登录, 然后执行向下发起.
	 检查业务逻辑是否正确？
	 * @throws Exception 
	*/
	public final void Step2_1() throws Exception
	{
		//子线程中的接受人员, 分别是 zhoushengyu,zhangyifan

		// 让子线程中的一个人 zhoushengyu 登录, 然后执行向下发起,
		bp.wf.Dev2Interface.Port_Login("zhoushengyu");

		//获得此人的 005 的待办工作.
		dt = bp.wf.Dev2Interface.DB_GenerEmpWorksOfDataTable(WebUser.getNo(),WFState.Runing, "005");
		if (dt.Rows.size() == 0)
		{
			throw new RuntimeException("@不应该获取不到它的待办数据.");
		}

		//获取子线程的workID.
		int threahWorkID = 0;
		for (DataRow dr : dt.Rows)
		{
			if (Integer.parseInt(dr.getValue("FID").toString()) == workid)
			{
				threahWorkID = Integer.parseInt(dr.getValue("WorkID").toString());
				break;
			}
		}
		if (threahWorkID == 0)
		{
			throw new RuntimeException("@不应当找不到它的待办。");
		}

		// 执行 子线程向合流点发送.
		Hashtable ht = new Hashtable();
		ht.put("FuWuQi",90);
		ht.put("ShuMaXiangJi", 20); //把数据放里面去,让它保存到子线程的主表，以检查数据是否汇总到合流节点上。
		objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, threahWorkID, ht);


			///第1步: 检查发送后的变量.
		if (objs.getVarWorkID() != threahWorkID)
		{
			throw new RuntimeException("@应当是 VarWorkID=" + threahWorkID + " ，现在是:" + objs.getVarWorkID());
		}

		if (objs.getVarCurrNodeID() != 502)
		{
			throw new RuntimeException("@应当是 VarCurrNodeID=502 是，现在是:" + objs.getVarCurrNodeID());
		}

		if (objs.getVarToNodeID() != 599)
		{
			throw new RuntimeException("@应当是 VarToNodeID= 599 是，现在是:" + objs.getVarToNodeID());
		}

		if (!objs.getVarAcceptersID().equals("zhanghaicheng"))
		{
			throw new RuntimeException("@应当是 VarAcceptersID= zhanghaicheng 是，现在是:" + objs.getVarAcceptersID());
		}

			/// 第1步: 检查发送后的变量.


			///第2步: 检查引擎控制系统表.
		//先检查干流数据.
		gwf = new GenerWorkFlow(workid);
		if (gwf.getWFState() != WFState.Runing)
		{
			throw new RuntimeException("@应当是 Runing, 现在是:" + gwf.getWFState());
		}

		if (gwf.getFID() != 0)
		{
			throw new RuntimeException("@应当是 0, 现在是:" + gwf.getFID());
		}

		if (gwf.getFK_Node() != 599)
		{
			throw new RuntimeException("@应当是 599, 现在是:" + gwf.getFK_Node());
		}

		if (!gwf.getStarter().equals("zhanghaicheng"))
		{
			throw new RuntimeException("@应当是 zhanghaicheng, 现在是:" + gwf.getStarter());
		}

		// 干流的工作人员表是否有变化？
		GenerWorkerLists gwls = new GenerWorkerLists();
		gwls.Retrieve(GenerWorkerListAttr.WorkID, workid);
		for (GenerWorkerList item : gwls.ToJavaList())
		{
			if (!item.getFK_Emp().equals("zhanghaicheng"))
			{
				throw new RuntimeException("@应当是 zhanghaicheng, 现在是:" + item.getFK_Emp());
			}

			//如果是开始节点.
			if (item.getFK_Node() == 501)
			{
				if (item.getIsPass() == false)
				{
					throw new RuntimeException("@pass状态错误了，应该是已通过。");
				}
			}

			//如果是结束节点.
			if (item.getFK_Node() == 599)
			{
				//检查子线程完成率. 
				Node nd = new Node(599);
				if (nd.getPassRate().compareTo(new BigDecimal(50)) > 0)
				{
					if (item.getIsPassInt() != 3)
					{
						throw new RuntimeException("@因为完成率大于 50, 所以一个通过了，主线程的工作人员不能看到,现在是:" + item.getIsPassInt());
					}
				}
				else
				{
					if (item.getIsPassInt() != 0)
					{
						throw new RuntimeException("@因为小于50，所以只要有一个通过了，主线程的zhanghaicheng 工作人员应该可以看到待办，但是没有查到。 ");
					}
				}
			}
		}

		//检查子线程的工作人员列表表。
		gwls = new GenerWorkerLists();
		gwls.Retrieve(GenerWorkerListAttr.FID, workid);
		if (gwls.size() != 2)
		{
			throw new RuntimeException("@不是期望的两条子线程上的工作人员列表数据.");
		}
		for (GenerWorkerList item : gwls.ToJavaList())
		{
			if (item.getFK_Emp().equals("zhoushengyu"))
			{
				if (item.getIsPass() == false)
				{
					throw new RuntimeException("@此人应该是处理通过了，现在没有通过。");
				}
			}

			if (item.getFK_Emp().equals("zhangyifan"))
			{
				if (item.getIsPass() == true)
				{
					throw new RuntimeException("@此人应该有待办，结果不符合预期。");
				}
			}
		}

			/// 第2步: 检查引擎控制系统表.


			///第3步: 检查 节点表单表数据.
		sql = "SELECT * FROM ND501 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (!dt.Rows.get(0).getValue("Rec").toString().equals("zhanghaicheng"))
		{
			throw new RuntimeException("@开始节点的Rec 字段写入错误。");
		}

		//检查节点表单表是否有数据，以及数据是否正确？
		sql = "SELECT * FROM ND502 WHERE FID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 2)
		{
			throw new RuntimeException("@应该在第一个子线程节点上找到两个数据。");
		}
		for (DataRow dr : dt.Rows)
		{
			if (dr.getValue("Rec").toString().equals("zhangyifan"))
			{
				continue;
			}
			if (dr.getValue("Rec").toString().equals("zhoushengyu"))
			{
				continue;
			}
			throw new RuntimeException("@子线程表单数据没有正确的写入Rec字段.");
		}

		//检查参数是否存储到子线程的主表上了？
		sql = "SELECT * FROM ND502 WHERE OID=" + threahWorkID;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@没有找到子线程期望的数据。");
		}

		if (!dt.Rows.get(0).getValue("FuWuQi").toString().equals("90"))
		{
			throw new RuntimeException("没有存储到指定的位置.");
		}

		  if (!dt.Rows.get(0).getValue("ShuMaXiangJi").toString().equals("20"))
		  {
			throw new RuntimeException("没有存储到指定的位置.");
		  }



		// 检查汇总的明细表数据是否copy正确？
		  sql = "SELECT * FROM ND599Dtl1 WHERE OID=" + threahWorkID;
		  dt = DBAccess.RunSQLReturnTable(sql);
		  if (dt.Rows.size() != 1)
		  {
			  throw new RuntimeException("@子线程的数据没有copy到汇总的明细表里.");
		  }
		  dt = DBAccess.RunSQLReturnTable(sql);
		  if (dt.Rows.size() != 1)
		  {
			  throw new RuntimeException("@没有找到子线程期望的数据。");
		  }

		  if (!dt.Rows.get(0).getValue("FuWuQi").toString().equals("90"))
		  {
			  throw new RuntimeException("没有存储到指定的位置.");
		  }

		  if (!dt.Rows.get(0).getValue("ShuMaXiangJi").toString().equals("20"))
		  {
			  throw new RuntimeException("没有存储到指定的位置.");
		  }


		//检查报表数据是否正确？
		sql = "SELECT * FROM  ND5Rpt WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (!dt.Rows.get(0).getValue(GERptAttr.FlowEnder).toString().equals("zhanghaicheng"))
		{
			throw new RuntimeException("@应该是 zhanghaicheng 是 FlowEnder .");
		}

		if (!dt.Rows.get(0).getValue(GERptAttr.FlowStarter).toString().equals("zhanghaicheng"))
		{
			throw new RuntimeException("@应该是 zhanghaicheng 是 FlowStarter .");
		}

		if (!dt.Rows.get(0).getValue(GERptAttr.FlowEndNode).toString().equals("502"))
		{
			throw new RuntimeException("@应该是 502 是 FlowEndNode .");
		}

		if (Integer.parseInt(dt.Rows.get(0).getValue(GERptAttr.WFState).toString()) != WFState.Runing.getValue())
		{
			throw new RuntimeException("@应该是 WFState.Runing 是 WFState .");
		}

		if (Integer.parseInt(dt.Rows.get(0).getValue(GERptAttr.FID).toString()) != 0)
		{
			throw new RuntimeException("@应该是 FID =0 ");
		}

		if (!dt.Rows.get(0).getValue("FK_NY").toString().equals(DataType.getCurrentYearMonth()))
		{
			throw new RuntimeException("@ FK_NY 字段填充错误. ");
		}

			/// 第3步: 检查 节点表单表数据.
	}
	/** 
	 执行zhanghaicheng的 发送。
	 1，检查发送的对象。
	 2，检查流程引擎控制表。
	 3，检查节点表。
	 * @throws Exception 
	*/
	public final void Step3() throws Exception
	{
		// 让主线程上的发起人登录.
		bp.wf.Dev2Interface.Port_Login("zhanghaicheng");

		// 查询出来 zhangyifan 的workid.
		String sql = "SELECT WorkID FROM WF_GenerWorkerList WHERE FK_Emp='zhangyifan' AND FID=" + workid;
		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@不应该找不到子线程的.");
		}

		// 取得子线程ID.
		long threakWorkID = Long.parseLong(dt.Rows.get(0).getValue(0).toString());

		// 执行删除子线程. 删除子线程与删除流程是同一个方法。
		bp.wf.Dev2Interface.Flow_DoDeleteFlowByReal(threakWorkID, false);


			///第1步: 检查发送后的变量.
		// 检查 zhangyifan 是否有待办工作？
		sql = "SELECT COUNT(*) FROM WF_EmpWorks WHERE WorkID=" + threakWorkID;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 0)
		{
			throw new RuntimeException("@不应该找到该线程的待办工作.");
		}

		//检查子流程数据是否还在?
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(threakWorkID);
		if (gwf.getIsExits() == true)
		{
			throw new RuntimeException("@子线程数据还存在，这是错误的.");
		}


		//检查子流程数据是否还在?
		GenerWorkerList gwl = new GenerWorkerList();
		gwl.setWorkID(threakWorkID);
		gwl.setFID(workid);
		gwl.setFK_Emp("zhangyifan");
		if (gwl.getIsExits() == true)
		{
			throw new RuntimeException("@子线程数据的待办还存在，这是错误的.");
		}



		//检查主流程数据是否还在?
		  gwf = new GenerWorkFlow();
		gwf.setWorkID(workid);
		if (gwf.getIsExits() == false)
		{
			throw new RuntimeException("@主线程数据已经不存在，这是错误的.");
		}




			/// 第1步: 检查发送后的变量.


		// 删除测试数据.
		bp.wf.Dev2Interface.Flow_DoDeleteFlowByReal(workid, false);

	}
}