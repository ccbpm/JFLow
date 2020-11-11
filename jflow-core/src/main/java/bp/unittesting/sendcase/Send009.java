package bp.unittesting.sendcase;

import bp.wf.*;
import bp.wf.data.*;
import bp.wf.template.*;
import bp.en.*;
import bp.da.*;
import bp.web.*;
import bp.unittesting.*;
import bp.unittesting.*;

import java.math.BigDecimal;
import java.util.*;

public class Send009 extends TestBase
{
	/** 
	 异表单分合流的发送
	*/
	public Send009()
	{
		this.Title = "异表单分合流的发送";
		this.DescIt = "流程:009部门年计划流程(异表单分合流),执行发送后的数据是否符合预期要求.";
		this.editState = EditState.UnOK;
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
	 说明:
	 1, zhoupeng 启动.
	 2, 发送给三个人 zhoupeng，张海成 qifenglin，祁凤林、guoxiangbin，郭祥斌、。
	 3，zhoupeng 结束和合流点。
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{
		//初始化变量.
		fk_flow = "009";
		userNo = "zhoupeng";
		fl = new Flow(fk_flow);

		//执行第1步检查，创建工作与发送.
		this.Step1();

		//执行第2_1步检查，zhanghaicheng的发送结果.
		this.Step2_1();

		//执行第2_2步检查，qifenglin 的发送结果.
		this.Step2_2();

		//执行第2_3步检查，guoxiangbin 的发送结果.
		this.Step2_3();

		//最后的检查.
		this.Step3();
	}
	/** 
	 创建流程，发送分流点第1步.
	 * @throws Exception 
	*/
	public final void Step1() throws Exception
	{
		// 让zhoupeng 登录.
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
		sql = "SELECT * FROM ND901 WHERE OID=" + workid;
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

		//组织参数.
		Hashtable ht = new Hashtable();
		ht.put("KeFuBu", 1);
		ht.put("ShiChangBu", 1);
		ht.put("YanFaBu", 1);

		//开始节点:执行发送,并获取发送对象. 主线程向子线程发送.
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workid, ht);


			///第1步: 检查【开始节点】发送对象返回的信息是否完整？
		//从获取的发送对象里获取到下一个工作者: zhanghaicheng,qifenglin,guoxiangbin .
		if (!objs.getVarAcceptersID().equals("zhanghaicheng,qifenglin,guoxiangbin,"))
		{
			throw new RuntimeException("@下一步的接受人不正确,  zhanghaicheng,qifenglin,guoxiangbin, .现在是:" + objs.getVarAcceptersID());
		}

		if (!objs.getVarToNodeIDs().equals("902,903,904,"))
		{
			throw new RuntimeException("@应该是 902,903,904,  现在是:" + objs.getVarToNodeIDs());
		}

		if (objs.getVarWorkID() != workid)
		{
			throw new RuntimeException("@主线程的workid不应该变化:" + objs.getVarWorkID());
		}

		if (objs.getVarCurrNodeID() != 901)
		{
			throw new RuntimeException("@当前节点的编号不能变化:" + objs.getVarCurrNodeID());
		}

		if (objs.getVarTreadWorkIDs() == null)
		{
			throw new RuntimeException("@没有获取到三条子线程ID.");
		}

		if (objs.getVarTreadWorkIDs().contains(",") == false)
		{
			throw new RuntimeException("@没有获取到三条子线程的WorkID:" + objs.getVarTreadWorkIDs());
		}


			///  检查【开始节点】发送对象返回的信息是否完整？


			///第2步: 检查流程引擎控制系统表是否符合预期.
		gwf = new GenerWorkFlow(workid);
		if (gwf.getFK_Node() != 901)
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
		gwl = new GenerWorkerList(workid, 901, WebUser.getNo());
		if (gwl.getIsPass() == true)
		{
			throw new RuntimeException("@干流上的pass状态应该是通过,此人已经没有他的待办工作了.");
		}

		//找出子线程上的工作人员.
		GenerWorkFlows gwfs = new GenerWorkFlows();
		gwfs.Retrieve(GenerWorkerListAttr.FID, workid);
		if (gwfs.size() != 3)
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

			//Node nd = new Node(item.FK_Node);
			//if (nd.iss

			//if (item.FK_Node == 901)
			//    throw new Exception("@当前节点应当是 902 ,现在是:" + item.FK_Node);

			if (item.getWFState() != WFState.Runing)
			{
				throw new RuntimeException("@当前 WFState 应当是 Runing ,现在是:" + item.getWFState().toString());
			}
		}

		//找出子线程工作处理人员的工作列表.
		GenerWorkerLists gwls = new GenerWorkerLists();
		gwls.Retrieve(GenerWorkerListAttr.FID, workid);
		if (gwls.size() != 3)
		{
			throw new RuntimeException("@应该在子线程上查询出来 3 个待办，现在只有(" + gwls.size() + ")个。");
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

			if (!item.getFK_Flow().equals("009"))
			{
				throw new RuntimeException("@应该是 009 现在是:" + item.getFK_Flow());
			}

			//if (item.FK_Node != 902)
			//    throw new Exception("@应该是 902 现在是:" + item.FK_Node);
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
		if (dt.Rows.size() != 3)
		{
			throw new RuntimeException("@应该取出来两个子线程的 WF_EmpWorks 视图中. " + sql);
		}


			/// end 检查流程引擎控制系统表是否符合预期.


			///第3步: 检查【开始节点】发送节点表单-数据信息否完整？
		//检查节点表单表是否有数据？
		sql = "SELECT * FROM ND901 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@应该找到开始节点表单数据，但是没有。");
		}

		if (!dt.Rows.get(0).getValue("Rec").toString().equals(WebUser.getNo()))
		{
			throw new RuntimeException("@没有向主线程开始节点表里写入Rec字段，现在是：" + dt.Rows.get(0).getValue("Rec").toString() + "应当是:" + WebUser.getNo());
		}

		//找出子线程工作处理人员的工作列表.
		gwls = new GenerWorkerLists();
		gwls.Retrieve(GenerWorkerListAttr.FID, workid);

		//检查子线程节点数据表是否正确。
		for (GenerWorkerList item : gwls.ToJavaList())
		{
			sql = "SELECT * FROM ND" + item.getFK_Node() + " WHERE OID=" + item.getWorkID();
			dt = DBAccess.RunSQLReturnTable(sql);
			if (dt.Rows.size() != 1)
			{
				throw new RuntimeException("@不应该发现不到子线程节点数据。");
			}

			for (DataColumn dc : dt.Columns)
			{
				String val = dt.Rows.get(0).getValue(dc.ColumnName).toString();
				switch (dc.ColumnName)
				{
					case WorkAttr.FID:
						if (!String.valueOf(workid).equals(val))
						{
							throw new RuntimeException("@不应当不等于workid.");
						}
						break;
					case WorkAttr.Rec:
						if (!item.getFK_Emp().equals(val))
						{
							throw new RuntimeException("@不应当不等于:" + item.getFK_Emp());
						}
						break;
					//case WorkAttr.Emps:
					//    if (DataType.IsNullOrEmpty(val) || val.Contains(item.FK_Emp) == false)
					//        throw new Exception("@ Emps 不应当为空，或者不包含发起人.");
					//    break;
					default:
						break;
				} //结束列的col判断。
			}
		}

		// 检查报表数据是否完整。
		sql = "SELECT * FROM ND9Rpt WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.Rows.get(0).getValue(dc.ColumnName).toString();
			switch (dc.ColumnName)
			{
				case GERptAttr.FID:
					if (Integer.parseInt(val) != 0)
					{
						throw new RuntimeException("@应该是 FID =0 ");
					}
					break;
				case GERptAttr.FK_Dept:
					if (!val.equals(WebUser.getFK_Dept()))
					{
						throw new RuntimeException("@ FK_Dept 字段填充错误,应当是:" + WebUser.getFK_Dept() + ",现在是:" + val);
					}
					break;
				case GERptAttr.FK_NY:
					if (!val.equals(DataType.getCurrentYearMonth()))
					{
						throw new RuntimeException("@ FK_NY 字段填充错误. ");
					}
					break;
				case GERptAttr.FlowDaySpan:
					if (!val.equals("0"))
					{
						throw new RuntimeException("@ FlowDaySpan 应当是 0 . ");
					}
					break;
				case GERptAttr.FlowEmps:
					if (bp.wf.Glo.getUserInfoShowModel() != UserInfoShowModel.UserNameOnly)
					{
						if (val.contains(WebUser.getNo()) == false)
						{
							throw new RuntimeException("@ 应该包含 zhoupeng , 现在是: " + val);
						}
					}
					break;
				case GERptAttr.FlowEnder:
					if (!val.equals("zhoupeng"))
					{
						throw new RuntimeException("@应该是 zhoupeng 是 FlowEnder ,现在是:" + val);
					}
					break;
				case GERptAttr.FlowEnderRDT:
					break;
				case GERptAttr.FlowEndNode:
					if (!val.equals("901"))
					{
						throw new RuntimeException("@应该是 901 是 FlowEndNode,现在是:" + val);
					}
					break;
				case GERptAttr.FlowStarter:
					if (!val.equals("zhoupeng"))
					{
						throw new RuntimeException("@应该是 zhoupeng 是 FlowStarter, 现在是:" + val);
					}
					break;
				case GERptAttr.PFlowNo:
					if (!val.equals(""))
					{
						throw new RuntimeException("@ PFlowNo 应当是 '' 现在是:" + val);
					}
					break;
				case GERptAttr.PWorkID:
					if (!val.equals("0"))
					{
						throw new RuntimeException("@ PWorkID 应当是 '0' 现在是:" + val);
					}
					break;
				case GERptAttr.Title:
					if (DataType.IsNullOrEmpty(val))
					{
						throw new RuntimeException("@ Title 不应当是空 " + val);
					}
					break;
				case GERptAttr.WFState:
					if (Integer.parseInt(val) != WFState.Runing.getValue())
					{
						throw new RuntimeException("@应该是 WFState.Runing 是当前的状态,现在是：" + val);
					}
					break;
				default:
					break;
			}
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
		//子线程中的接受人员, 分别是 zhanghaicheng,qifenglin,guoxiangbin

		// 让子线程中的一个人 zhanghaicheng 登录, 然后执行向下发起,
		bp.wf.Dev2Interface.Port_Login("zhanghaicheng");

		//获得此人的 009 的待办工作.
		dt = bp.wf.Dev2Interface.DB_GenerEmpWorksOfDataTable(WebUser.getNo(), WFState.Runing, "009");
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
		ht.put("XianYouRenShu", 90);
		ht.put("XinZengRenShu", 20); //把数据放里面去,让它保存到子线程的主表，以检查数据是否汇总到合流节点上。
		objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, threahWorkID, ht);


			///第1步: 检查发送后的变量.
		if (objs.getVarWorkID() != threahWorkID)
		{
			throw new RuntimeException("@应当是 VarWorkID=" + threahWorkID + " ，现在是:" + objs.getVarWorkID());
		}

		if (objs.getVarCurrNodeID() != 902)
		{
			throw new RuntimeException("@应当是 VarCurrNodeID=902 是，现在是:" + objs.getVarCurrNodeID());
		}

		if (objs.getVarToNodeID() != 999)
		{
			throw new RuntimeException("@应当是 VarToNodeID= 999 是，现在是:" + objs.getVarToNodeID());
		}

		if (!objs.getVarAcceptersID().equals("zhoupeng"))
		{
			throw new RuntimeException("@应当是 VarAcceptersID= zhoupeng 是，现在是:" + objs.getVarAcceptersID());
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

		if (gwf.getFK_Node() != 901)
		{
			throw new RuntimeException("@应当是 901, 现在是:" + gwf.getFK_Node());
		}

		if (!gwf.getStarter().equals("zhoupeng"))
		{
			throw new RuntimeException("@应当是 zhoupeng, 现在是:" + gwf.getStarter());
		}

		// 干流的工作人员表是否有变化？
		GenerWorkerLists gwls = new GenerWorkerLists();
		gwls.Retrieve(GenerWorkerListAttr.WorkID, workid);
		for (GenerWorkerList item : gwls.ToJavaList())
		{
			if (!item.getFK_Emp().equals("zhoupeng"))
			{
				throw new RuntimeException("@应当是 zhoupeng, 现在是:" + item.getFK_Emp());
			}

			//如果是开始节点.
			if (item.getFK_Node() == 901)
			{
				if (item.getIsPass() == false)
				{
					throw new RuntimeException("@pass状态错误了，应该是已通过。");
				}
			}

			//如果是结束节点.
			if (item.getFK_Node() == 999)
			{
				//检查子线程完成率. 
				Node nd = new Node(999);
				if (nd.getPassRate().compareTo(new BigDecimal(50)) > 0)
				{
					// 检查主线程数据是否正确.
					sql = "SELECT COUNT(*) FROM WF_EmpWorks WHERE WorkID=" + workid;
					if (DBAccess.RunSQLReturnValInt(sql) != 0)
					{
						throw new RuntimeException("@因为完成率大于 50, 所以一个通过了，主线程的工作人员不能看到.");
					}

					//if (item.IsPassInt != 3)
					//    throw new Exception("@因为完成率大于 50, 所以一个通过了，主线程的工作人员不能看到,现在是:"+item.IsPassInt);
				}
				else
				{
					if (item.getIsPassInt() != 0)
					{
						throw new RuntimeException("@因为小于50，所以只要有一个通过了，主线程的zhoupeng 工作人员应该可以看到待办，但是没有查到。 ");
					}
				}
			}
		}

		//检查子线程的工作人员列表表 。
		gwls = new GenerWorkerLists();
		gwls.Retrieve(GenerWorkerListAttr.FID, workid);
		if (gwls.size() != 3)
		{
			throw new RuntimeException("@不是期望的两条子线程上的工作人员列表数据.");
		}
		for (GenerWorkerList item : gwls.ToJavaList())
		{
			if (item.getFK_Emp().equals("zhanghaicheng"))
			{
				if (item.getIsPass() == false)
				{
					throw new RuntimeException("@此人应该是处理通过了，现在没有通过。");
				}
			}

			if (item.getFK_Emp().equals("qifenglin"))
			{
				if (item.getIsPass() == true)
				{
					throw new RuntimeException("@此人应该有待办，结果不符合预期。");
				}
			}

			if (item.getFK_Emp().equals("guoxiangbin"))
			{
				if (item.getIsPass() == true)
				{
					throw new RuntimeException("@此人应该有待办，结果不符合预期。");
				}
			}
		}

			/// 第2步: 检查引擎控制系统表.


			///第3步: 检查 节点表单表数据.
		sql = "SELECT * FROM ND901 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (!dt.Rows.get(0).getValue("Rec").toString().equals("zhoupeng"))
		{
			throw new RuntimeException("@开始节点的Rec 字段写入错误。");
		}

		////检查节点表单表是否有数据，以及数据是否正确？
		//sql = "SELECT * FROM ND902 WHERE FID=" + workid;
		//dt = DBAccess.RunSQLReturnTable(sql);
		//if (dt.Rows.size() != 2)
		//    throw new Exception("@应该在第一个子线程节点上找到两个数据");

		//foreach (DataRow dr in dt.Rows)
		//{
		//    if (dr["Rec").ToString() == "zhangyifan")
		//        continue;
		//    if (dr["Rec").ToString() == "zhoushengyu")
		//        continue;
		//    throw new Exception("@子线程表单数据没有正确的写入Rec字段.");
		//}

		////检查参数是否存储到子线程的主表上了？
		//sql = "SELECT * FROM ND902 WHERE OID=" + threahWorkID;
		//dt = DBAccess.RunSQLReturnTable(sql);
		//if (dt.Rows.size() != 1)
		//    throw new Exception("@没有找到子线程期望的数据。");

		//if (dt.Rows.get(0).getValue("FuWuQi").ToString() != "90")
		//    throw new Exception("没有存储到指定的位置.");

		//if (dt.Rows.get(0).getValue("ShuMaXiangJi").ToString() != "20")
		//    throw new Exception("没有存储到指定的位置.");

		//// 检查汇总的明细表数据是否copy正确？
		//  sql = "SELECT * FROM ND999Dtl1 WHERE OID=" + threahWorkID;
		//  dt = DBAccess.RunSQLReturnTable(sql);
		//  if (dt.Rows.size() != 1)
		//      throw new Exception("@子线程的数据没有copy到汇总的明细表里.");
		//  dt = DBAccess.RunSQLReturnTable(sql);
		//  if (dt.Rows.size() != 1)
		//      throw new Exception("@没有找到子线程期望的数据。");

		//  if (dt.Rows.get(0).getValue("FuWuQi").ToString() != "90")
		//      throw new Exception("没有存储到指定的位置.");

		//  if (dt.Rows.get(0).getValue("ShuMaXiangJi").ToString() != "20")
		//      throw new Exception("没有存储到指定的位置.");

		//检查报表数据是否正确？
		sql = "SELECT * FROM  ND9Rpt WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		String val = dt.Rows.get(0).getValue(GERptAttr.FlowEnder).toString();
		//if (dt.Rows.get(0).getValue(GERptAttr.FlowEnder).ToString() != "zhoupeng")
		//    throw new Exception("@应该是 zhoupeng 是 FlowEnder, 现在是:"+val);

		if (!dt.Rows.get(0).getValue(GERptAttr.FlowStarter).toString().equals("zhoupeng"))
		{
			throw new RuntimeException("@应该是 zhoupeng 是 FlowStarter .");
		}

		if (!dt.Rows.get(0).getValue(GERptAttr.FlowEndNode).toString().equals("999"))
		{
			throw new RuntimeException("@应该是 999 是 FlowEndNode，而现在是:" + dt.Rows.get(0).getValue(GERptAttr.FlowEndNode).toString());
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
	 每个子线程向下发送
	 * @throws Exception 
	*/
	public final void Step2_2() throws Exception
	{
		// 让子线程中的一个人 zhoushengyu 登录, 然后执行向下发起,
		bp.wf.Dev2Interface.Port_Login("qifenglin");

		//获得此人的 009 的待办工作.
		dt = bp.wf.Dev2Interface.DB_GenerEmpWorksOfDataTable(WebUser.getNo(), WFState.Runing, "009");
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
		ht.put("FuWuQi", 100);
		ht.put("ShuMaXiangJi", 30); //把数据放里面去,让它保存到子线程的主表，以检查数据是否汇总到合流节点上。

		// 执行 子线程向合流点发送.
		objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, threahWorkID, ht);


			///第1步: 检查发送后的变量.
		if (objs.getVarWorkID() != threahWorkID)
		{
			throw new RuntimeException("@应当是 VarWorkID=" + threahWorkID + " ，现在是:" + objs.getVarWorkID());
		}

		if (objs.getVarCurrNodeID() != 903)
		{
			throw new RuntimeException("@应当是 VarCurrNodeID=903 是，现在是:" + objs.getVarCurrNodeID());
		}

		if (objs.getVarToNodeID() != 999)
		{
			throw new RuntimeException("@应当是 VarToNodeID= 999 是，现在是:" + objs.getVarToNodeID());
		}

		//if (objs.VarAcceptersID != "zhoupeng")
		//    throw new Exception("@应当是 VarAcceptersID= zhoupeng 是，现在是:" + objs.VarAcceptersID);

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

		if (gwf.getFK_Node() != 901)
		{
			throw new RuntimeException("@应当是 901, 现在是:" + gwf.getFK_Node());
		}

		if (!gwf.getStarter().equals("zhoupeng"))
		{
			throw new RuntimeException("@应当是 zhoupeng, 现在是:" + gwf.getStarter());
		}

		// 干流的工作人员表是否有变化？
		GenerWorkerLists gwls = new GenerWorkerLists();
		gwls.Retrieve(GenerWorkerListAttr.WorkID, workid);
		for (GenerWorkerList item : gwls.ToJavaList())
		{
			if (!item.getFK_Emp().equals("zhoupeng"))
			{
				throw new RuntimeException("@应当是 zhoupeng, 现在是:" + item.getFK_Emp());
			}

			//如果是开始节点.
			if (item.getFK_Node() == 901)
			{
				if (item.getIsPass() == false)
				{
					throw new RuntimeException("@pass状态错误了，应该是已通过。");
				}
			}

			//如果是结束节点.
			if (item.getFK_Node() == 999)
			{
				//检查子线程完成率.
				Node nd = new Node(999);
				if (nd.getPassRate().compareTo(new BigDecimal(50)) > 0)
				{
					if (item.getIsPassInt() != 0)
					{
						throw new RuntimeException("@因为完成率大于 50, 现在两个都通过了，所以这合流点上也应该是通过的状态。");
					}
				}
				else
				{
					if (item.getIsPassInt() != 0)
					{
						throw new RuntimeException("@因为小于50，所以只要有一个通过了，主线程的zhoupeng 工作人员应该可以看到待办，但是没有查到。 ");
					}
				}
			}
		}

		//检查子线程的工作人员列表表。
		gwls = new GenerWorkerLists();
		gwls.Retrieve(GenerWorkerListAttr.FID, workid);
		if (gwls.size() != 3)
		{
			throw new RuntimeException("@不是期望的两条子线程上的工作人员列表数据, 应该是3,现在是:" + gwls.size());
		}
		for (GenerWorkerList item : gwls.ToJavaList())
		{
			if (item.getFK_Emp().equals("zhanghaicheng"))
			{
				if (item.getIsPass() == false)
				{
					throw new RuntimeException("@此人应该是处理通过了，现在没有通过。");
				}
			}

			if (item.getFK_Emp().equals("qifenglin"))
			{
				if (item.getIsPass() == false)
				{
					throw new RuntimeException("@此人应该是处理通过了，现在没有通过。");
				}
			}

			if (item.getFK_Emp().equals("guoxiangbin"))
			{
				if (item.getIsPass() == true)
				{
					throw new RuntimeException("@此人应该有待办，结果不符合预期。");
				}
			}
		}

			/// 第2步: 检查引擎控制系统表.


			///第3步: 检查 节点表单表数据.
		sql = "SELECT * FROM ND901 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (!dt.Rows.get(0).getValue("Rec").toString().equals("zhoupeng"))
		{
			throw new RuntimeException("@开始节点的Rec 字段写入错误。");
		}

		////检查节点表单表是否有数据，以及数据是否正确？
		//sql = "SELECT * FROM ND902 WHERE FID=" + workid;
		//dt = DBAccess.RunSQLReturnTable(sql);
		//if (dt.Rows.size() != 3)
		//    throw new Exception("@应该在第一个子线程节点上找到 3 个数据,现在是:"+dt.Rows.size());]

		//foreach (DataRow dr in dt.Rows)
		//{
		//    if (dr["Rec").ToString() == "zhangyifan")
		//    {
		//        continue;
		//    }
		//    if (dr["Rec").ToString() == "zhoushengyu")
		//    {
		//        continue;
		//    }
		//    throw new Exception("@子线程表单数据没有正确的写入Rec字段.");
		//}

		////检查参数是否存储到子线程的主表上了？
		//sql = "SELECT * FROM ND902 WHERE OID=" + threahWorkID;
		//dt = DBAccess.RunSQLReturnTable(sql);
		//if (dt.Rows.size() != 1)
		//    throw new Exception("@没有找到子线程期望的数据。");

		//if (dt.Rows.get(0).getValue("FuWuQi").ToString() != "100")
		//    throw new Exception("没有存储到指定的位置.");

		//if (dt.Rows.get(0).getValue("ShuMaXiangJi").ToString() != "30")
		//    throw new Exception("没有存储到指定的位置.");


		//// 检查汇总的明细表数据是否copy正确？
		//sql = "SELECT * FROM ND999Dtl1 WHERE OID=" + threahWorkID;
		//dt = DBAccess.RunSQLReturnTable(sql);
		//if (dt.Rows.size() != 1)
		//    throw new Exception("@子线程的数据没有copy到汇总的明细表里.");
		//dt = DBAccess.RunSQLReturnTable(sql);
		//if (dt.Rows.size() != 1)
		//    throw new Exception("@没有找到子线程期望的数据。");

		//if (dt.Rows.get(0).getValue("FuWuQi").ToString() != "100")
		//    throw new Exception("没有存储到指定的位置.");

		//if (dt.Rows.get(0).getValue("ShuMaXiangJi").ToString() != "30")
		//    throw new Exception("没有存储到指定的位置.");


		//检查报表数据是否正确？
		sql = "SELECT * FROM  ND9Rpt WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		//if (dt.Rows.get(0).getValue(GERptAttr.FlowEnder).ToString() != "zhoupeng")
		//    throw new Exception("@应该是 zhoupeng 是 FlowEnder .");

		if (!dt.Rows.get(0).getValue(GERptAttr.FlowStarter).toString().equals("zhoupeng"))
		{
			throw new RuntimeException("@应该是 zhoupeng 是 FlowStarter .");
		}

		if (!dt.Rows.get(0).getValue(GERptAttr.FlowEndNode).toString().equals("999"))
		{
			throw new RuntimeException("@应该是 999 是 FlowEndNode，现在是:" + dt.Rows.get(0).getValue(GERptAttr.FlowEndNode).toString());
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
	 每个子线程向下发送
	 * @throws Exception 
	*/
	public final void Step2_3() throws Exception
	{
		// 让子线程中的一个人 zhoushengyu 登录, 然后执行向下发起,
		bp.wf.Dev2Interface.Port_Login("guoxiangbin");

		//获得此人的 009 的待办工作.
		dt = bp.wf.Dev2Interface.DB_GenerEmpWorksOfDataTable(WebUser.getNo(), WFState.Runing, "009");
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
		ht.put("FuWuQi", 100);
		ht.put("ShuMaXiangJi", 30); //把数据放里面去,让它保存到子线程的主表，以检查数据是否汇总到合流节点上。

		// 执行 子线程向合流点发送.
		objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, threahWorkID, ht);


			///第1步: 检查发送后的变量.
		if (objs.getVarWorkID() != threahWorkID)
		{
			throw new RuntimeException("@应当是 VarWorkID=" + threahWorkID + " ，现在是:" + objs.getVarWorkID());
		}

		if (objs.getVarCurrNodeID() != 904)
		{
			throw new RuntimeException("@应当是 VarCurrNodeID=904 是，现在是:" + objs.getVarCurrNodeID());
		}

		if (objs.getVarToNodeID() != 999)
		{
			throw new RuntimeException("@应当是 VarToNodeID= 999 是，现在是:" + objs.getVarToNodeID());
		}

		//if (objs.VarAcceptersID != "zhoupeng")
		//    throw new Exception("@应当是 VarAcceptersID= zhoupeng 是，现在是:" + objs.VarAcceptersID);

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

		if (gwf.getFK_Node() != 999)
		{
			throw new RuntimeException("@应当是 999, 现在是:" + gwf.getFK_Node());
		}

		if (!gwf.getStarter().equals("zhoupeng"))
		{
			throw new RuntimeException("@应当是 zhoupeng, 现在是:" + gwf.getStarter());
		}

		// 干流的工作人员表是否有变化？
		GenerWorkerLists gwls = new GenerWorkerLists();
		gwls.Retrieve(GenerWorkerListAttr.WorkID, workid);
		for (GenerWorkerList item : gwls.ToJavaList())
		{
			if (!item.getFK_Emp().equals("zhoupeng"))
			{
				throw new RuntimeException("@应当是 zhoupeng, 现在是:" + item.getFK_Emp());
			}

			//如果是开始节点.
			if (item.getFK_Node() == 901)
			{
				if (item.getIsPass() == false)
				{
					throw new RuntimeException("@pass状态错误了，应该是已通过。");
				}
			}

			//如果是结束节点.
			if (item.getFK_Node() == 999)
			{
				//检查子线程完成率.
				Node nd = new Node(999);
				if (nd.getPassRate().compareTo(new BigDecimal(50)) > 0)
				{
					if (item.getIsPassInt() != 0)
					{
						throw new RuntimeException("@因为完成率大于 50, 现在两个都通过了，所以这合流点上也应该是通过的状态。");
					}
				}
				else
				{
					if (item.getIsPassInt() != 0)
					{
						throw new RuntimeException("@因为小于50，所以只要有一个通过了，主线程的zhoupeng 工作人员应该可以看到待办，但是没有查到。 ");
					}
				}
			}
		}

		//检查子线程的工作人员列表表。
		gwls = new GenerWorkerLists();
		gwls.Retrieve(GenerWorkerListAttr.FID, workid);
		if (gwls.size() != 3)
		{
			throw new RuntimeException("@不是期望的 3 条子线程上的工作人员列表数据.");
		}

		for (GenerWorkerList item : gwls.ToJavaList())
		{
			if (item.getFK_Emp().equals("zhanghaicheng"))
			{
				if (item.getIsPass() == false)
				{
					throw new RuntimeException("@ zhanghaicheng 应该是处理通过了，现在没有通过。IsPass=" + item.getIsPassInt());
				}
			}

			if (item.getFK_Emp().equals("qifenglin"))
			{
				if (item.getIsPass() == false)
				{
					throw new RuntimeException("@ qifenglin 应该是处理通过了，现在没有通过。IsPass=" + item.getIsPassInt());
				}
			}

			if (item.getFK_Emp().equals("guoxiangbin"))
			{
				if (item.getIsPass() == false)
				{
					throw new RuntimeException("@ guoxiangbin 应该是处理通过了，现在没有通过。IsPass=" + item.getIsPassInt());
				}
			}
		}

			/// 第2步: 检查引擎控制系统表.


			///第3步: 检查 节点表单表数据.
		sql = "SELECT * FROM ND901 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (!dt.Rows.get(0).getValue("Rec").toString().equals("zhoupeng"))
		{
			throw new RuntimeException("@开始节点的Rec 字段写入错误。");
		}


		//// 检查汇总的明细表数据是否copy正确？
		//sql = "SELECT * FROM ND999Dtl1 WHERE OID=" + threahWorkID;
		//dt = DBAccess.RunSQLReturnTable(sql);
		//if (dt.Rows.size() != 1)
		//    throw new Exception("@子线程的数据没有copy到汇总的明细表里.");
		//dt = DBAccess.RunSQLReturnTable(sql);
		//if (dt.Rows.size() != 1)
		//    throw new Exception("@没有找到子线程期望的数据。");

		//if (dt.Rows.get(0).getValue("FuWuQi").ToString() != "100")
		//    throw new Exception("没有存储到指定的位置.");

		//if (dt.Rows.get(0).getValue("ShuMaXiangJi").ToString() != "30")
		//    throw new Exception("没有存储到指定的位置.");

		//检查报表数据是否正确？
		sql = "SELECT * FROM  ND9Rpt WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		//if (dt.Rows.get(0).getValue(GERptAttr.FlowEnder).ToString() != "zhoupeng")
		//    throw new Exception("@应该是 zhoupeng 是 FlowEnder ." +);

		if (!dt.Rows.get(0).getValue(GERptAttr.FlowStarter).toString().equals("zhoupeng"))
		{
			throw new RuntimeException("@应该是 zhoupeng 是 FlowStarter .");
		}

		if (!dt.Rows.get(0).getValue(GERptAttr.FlowEndNode).toString().equals("999"))
		{
			throw new RuntimeException("@应该是 999 是 FlowEndNode .");
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
	 执行zhoupeng的 发送。
	 1，检查发送的对象。
	 2，检查流程引擎控制表。
	 3，检查节点表。
	 * @throws Exception 
	*/
	public final void Step3() throws Exception
	{
		// 让主线程上的发起人登录。
		bp.wf.Dev2Interface.Port_Login("zhoupeng");

		// 执行向最后一个节点发送
		objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workid);


			///第1步: 检查发送后的变量.
		if (objs.getVarWorkID() != workid)
		{
			throw new RuntimeException("@应当是 VarWorkID=" + workid + " ，现在是:" + objs.getVarWorkID());
		}

		if (objs.getVarCurrNodeID() != 999)
		{
			throw new RuntimeException("@应当是 VarCurrNodeID=999 是，现在是:" + objs.getVarCurrNodeID());
		}

		if (objs.getVarToNodeID() != 0)
		{
			throw new RuntimeException("@应当是 VarToNodeID= 0 是，现在是:" + objs.getVarToNodeID());
		}

		if (objs.getVarAcceptersID() != null)
		{
			throw new RuntimeException("@应当是 VarAcceptersID= null 是，现在是:" + objs.getVarAcceptersID());
		}

			/// 第1步: 检查发送后的变量.


			///第2步: 检查引擎控制系统表.
		//检查主线程.
		sql = "SELECT * FROM WF_GenerWorkFlow WHERE WorkID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);


		sql = "SELECT * FROM WF_GenerWorkerlist WHERE WorkID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 0)
		{
			throw new RuntimeException("@流程结束了，引擎表的数据没有删除。");
		}

		//检查子线程.
		sql = "SELECT * FROM WF_GenerWorkFlow WHERE FID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);

		sql = "SELECT * FROM WF_GenerWorkerlist WHERE FID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 0)
		{
			throw new RuntimeException("@流程结束了，引擎表的数据没有删除。");
		}

			/// 第2步: 检查引擎控制系统表.


			///第3步: 检查 节点表单表数据.
		//检查明细表合流点上的汇总数据。
		sql = "SELECT * FROM  ND999Dtl1 WHERE RefPK=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 3)
		{
			throw new RuntimeException("@丢失了明细表的汇总数据");
		}

		//int sum = 0;
		//foreach (DataRow dr in dt.Rows)
		//{
		//    sum += int.Parse(dr["FuWuQi").ToString());
		//}
		//if (sum!=190)
		//    throw new Exception("@明细表的汇总数据错误了");

		//检查报表数据是否正确？
		sql = "SELECT * FROM  ND9Rpt WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (!dt.Rows.get(0).getValue(GERptAttr.FlowEnder).toString().equals("zhoupeng"))
		{
			throw new RuntimeException("@应该是 zhoupeng 是 FlowEnder .");
		}

		if (DataType.IsNullOrEmpty(dt.Rows.get(0).getValue(GERptAttr.Title).toString()))
		{
			throw new RuntimeException("@流程走完后标题丢失了");
		}

		if (!dt.Rows.get(0).getValue(GERptAttr.FlowStarter).toString().equals("zhoupeng"))
		{
			throw new RuntimeException("@应该是 zhoupeng 是 FlowStarter .");
		}

		if (!dt.Rows.get(0).getValue(GERptAttr.FlowEndNode).toString().equals("999"))
		{
			throw new RuntimeException("@应该是 999 是 FlowEndNode .");
		}

		if (!dt.Rows.get(0).getValue(GERptAttr.FlowEnder).toString().equals("zhoupeng"))
		{
			throw new RuntimeException("@应该是 zhoupeng 是 FlowEnder .");
		}

		if (Integer.parseInt(dt.Rows.get(0).getValue(GERptAttr.WFState).toString()) != WFState.Complete.getValue())
		{
			throw new RuntimeException("@应该是 WFState.Complete 是当前的状态 .");
		}

		if (Integer.parseInt(dt.Rows.get(0).getValue(GERptAttr.FID).toString()) != 0)
		{
			throw new RuntimeException("@应该是 FID =0 ");
		}

		if (!dt.Rows.get(0).getValue("FK_NY").toString().equals(DataType.getCurrentYearMonth()))
		{
			throw new RuntimeException("@ FK_NY 字段填充错误. ");
		}

		// 检查子线程节点表里的数据是否存在？
		sql = "SELECT * FROM ND902 WHERE FID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@应该在第一个子线程节点上找到1个数据。");
		}
		for (DataRow dr : dt.Rows)
		{
			if (dr.getValue("Rec").toString().equals("zhanghaicheng"))
			{
				continue;
			}
			if (dr.getValue("Rec").toString().equals("qifenglin"))
			{
				continue;
			}
			if (dr.getValue("Rec").toString().equals("guoxiangbin"))
			{
				continue;
			}
			throw new RuntimeException("@子线程表单数据没有正确的写入Rec字段.");
		}

			/// 第3步: 检查 节点表单表数据.

	}
}