package bp.unittesting;

import bp.wf.*;
import bp.wf.data.*;
import bp.wf.template.*;
import bp.en.*;
import bp.da.*;
import bp.web.*;
import bp.port.*;

/** 
 父子流程
*/
public class CallSubFlow extends TestBase
{
	/** 
	 父子流程
	*/
	public CallSubFlow()
	{
		this.Title = "父子流程";
		this.DescIt = "测试call 子流程,以023 与024流程为实例.";
		this.editState = EditState.Passed;
	}
	/** 
	 说明 ：父子流程
	 涉及到了:  等功能。
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{
		//测试子流程只有一个人.
		Test1();

		//子流程的开始节点有一组人.
		Test2();
	}

	/** 
	 测试子流程只有一个人
	 * @throws Exception 
	*/
	private void Test1() throws Exception
	{
		String fk_flow = "023";
		String userNo = "zhanghaicheng";
		Flow fl = new Flow(fk_flow);

		// zhanghaicheng 登录.
		bp.wf.Dev2Interface.Port_Login(userNo);

		//创建空白工作, 发起开始节点.
		long workid = bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow);
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workid);


		//创建第一个子流程，调用只有一个人的接口.
		Emp empSubFlowStarter = new Emp("zhoupeng");
		long subFlowWorkID = bp.wf.Dev2Interface.Node_CreateStartNodeWork("024", null, null, empSubFlowStarter.getNo(), "子流程发起测试", workid, "023", 0);


			///检查发起的子流程 流程引擎表 是否完整？
		GenerWorkFlow gwf = new GenerWorkFlow(subFlowWorkID);
		if (!gwf.getPFlowNo().equals("023"))
		{
			throw new RuntimeException("@父流程编号错误,应当是023现在是" + gwf.getPFlowNo());
		}

		if (gwf.getPWorkID() != workid)
		{
			throw new RuntimeException("@父流程WorkID错误,应当是" + workid + "现在是" + gwf.getPWorkID());
		}

		if (!gwf.getStarter().equals(empSubFlowStarter.getNo()))
		{
			throw new RuntimeException("@流程发起人编号错误,应当是" + empSubFlowStarter.getNo() + "现在是" + gwf.getStarter());
		}

		if (!gwf.getStarterName().equals(empSubFlowStarter.getName()))
		{
			throw new RuntimeException("@流程发起人 Name 错误,应当是" + empSubFlowStarter.getName() + "现在是" + gwf.getStarterName());
		}

		if (!gwf.getFK_Dept().equals(empSubFlowStarter.getFK_Dept()))
		{
			throw new RuntimeException("@流程隶属部门错误,应当是" + empSubFlowStarter.getFK_Dept() + "现在是" + gwf.getFK_Dept());
		}

		if (!gwf.getTitle().equals("子流程发起测试"))
		{
			throw new RuntimeException("@流程标题 子流程发起测试 错误,应当是 子流程发起测试 现在是" + gwf.getTitle());
		}

		if (gwf.getWFState() != WFState.Runing)
		{
			throw new RuntimeException("@流程 WFState 错误,应当是 Runing 现在是" + gwf.getWFState());
		}

		if (gwf.getFID() != 0)
		{
			throw new RuntimeException("@FID错误,应当是0现在是" + gwf.getFID());
		}

		if (!gwf.getFK_Flow().equals("024"))
		{
			throw new RuntimeException("@FK_Flow错误,应当是024现在是" + gwf.getFK_Flow());
		}

		if (gwf.getFK_Node() != 2401)
		{
			throw new RuntimeException("@停留的当前节点错误,应当是2401现在是" + gwf.getFK_Flow());
		}

		GenerWorkerLists gwls = new GenerWorkerLists(subFlowWorkID, 2401);
		if (gwls.size() != 1)
		{
			throw new RuntimeException("@待办列表个数应当1,现在是" + gwls.size());
		}


		// 检查发起人列表是否完整？
		GenerWorkerList gwl = (GenerWorkerList)gwls.get(0);
		if (!gwl.getFK_Emp().equals(empSubFlowStarter.getNo()))
		{
			throw new RuntimeException("@处理人错误，现在是:" + empSubFlowStarter.getNo());
		}

		if (gwl.getIsPassInt() != 0)
		{
			throw new RuntimeException("@通过状态应当是未通过，现在是:" + gwl.getIsPassInt());
		}

		if (gwl.getFID() != 0)
		{
			throw new RuntimeException("@流程ID 应当是0 ，现在是:" + gwl.getFID());
		}

		if (!gwl.getFK_EmpText().equals(empSubFlowStarter.getName()))
		{
			throw new RuntimeException("@FK_EmpText  错误, 现在是:" + gwl.getFK_EmpText());
		}


			/// 检查发起的子流程 流程引擎表 是否完整？


			///检查发起的子流程数据是否完整？
		//检查报表数据是否完整?
		sql = "SELECT * FROM ND24Rpt WHERE OID=" + subFlowWorkID;
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到子流程的报表数据.");
		}

		for (DataColumn dc : dt.Columns)
		{
			String val = dt.Rows.get(0).getValue(dc.ColumnName).toString();
			switch (dc.ColumnName)
			{
				case GERptAttr.FID:
					if (!val.equals("0"))
					{
						throw new RuntimeException("@应当是0");
					}
					break;
				case GERptAttr.FK_Dept:
					if (!empSubFlowStarter.getFK_Dept().equals(val))
					{
						throw new RuntimeException("@应当是" + empSubFlowStarter.getFK_Dept() + ", 现在是:" + val);
					}
					break;
				case GERptAttr.FK_NY:
					if (!val.equals(DataType.getCurrentYearMonth()))
					{
						throw new RuntimeException("@应当是" + DataType.getCurrentYearMonth() + ", 现在是:" + val);
					}
					break;
				case GERptAttr.FlowDaySpan:
					if (!val.equals("0"))
					{
						throw new RuntimeException("@应当是 0 , 现在是:" + val);
					}
					break;
				case GERptAttr.FlowEmps:
					if (val.contains(empSubFlowStarter.getNo()) == false)
					{
						throw new RuntimeException("@应当是包含当前人员, 现在是:" + val);
					}
					break;
				case GERptAttr.FlowEnder:
					if (!val.equals(empSubFlowStarter.getNo()))
					{
						throw new RuntimeException("@应当是 empSubFlowStarter.getNo(), 现在是:" + val);
					}
					break;
				case GERptAttr.FlowEnderRDT:
					if (val.contains(DataType.getCurrentDate()) == false)
					{
						throw new RuntimeException("@应当是 当前日期, 现在是:" + val);
					}
					break;
				case GERptAttr.FlowEndNode:
					if (!val.equals("2401"))
					{
						throw new RuntimeException("@应当是 2401, 现在是:" + val);
					}
					break;
				case GERptAttr.FlowStarter:
					if (!val.equals(empSubFlowStarter.getNo()))
					{
						throw new RuntimeException("@应当是  WebUser.getNo(), 现在是:" + val);
					}
					break;
				case GERptAttr.FlowStartRDT:
					if (DataType.IsNullOrEmpty(val))
					{
						throw new RuntimeException("@应当不能为空,现在是:" + val);
					}
					break;
				case GERptAttr.Title:
					if (DataType.IsNullOrEmpty(val))
					{
						throw new RuntimeException("@不能为空title" + val);
					}
					break;
				case GERptAttr.WFState:
					WFState sta = WFState.forValue(Integer.parseInt(val));
					if (sta != WFState.Runing)
					{
						throw new RuntimeException("@应当是  WFState.Runing 现在是" + sta.toString());
					}
					break;
				default:
					break;
			}
		}

			/// 检查是否完整？

		// 测试以子流程向下发送，是否成功？
		bp.wf.Dev2Interface.Port_Login(empSubFlowStarter.getNo());
		objs = bp.wf.Dev2Interface.Node_SendWork("024", subFlowWorkID);
		if (objs.getVarToNodeID() != 2402)
		{
			throw new RuntimeException("@子流程向下发送时不成功.");
		}
	}

	/** 
	 测试子流程只有一个人
	 * @throws Exception 
	*/
	private void Test2() throws Exception
	{
		String fk_flow = "023";
		String userNo = "zhanghaicheng";
		Flow fl = new Flow(fk_flow);

		// zhanghaicheng 登录.
		bp.wf.Dev2Interface.Port_Login(userNo);

		//创建空白工作, 发起开始节点.
		long workid = bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow);
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workid); //发送到第二个节点上去

		/*创建子流程. 指定可以处理子流程的处理人员是一个集合。 
		 *
		 * 此api多了两个参数：
		 * 1，该流程隶属于那个部门. 
		 * 2，该流程的参与人集合，用逗号分开.
		 */

		Emp flowStarter = new Emp(WebUser.getNo());

		long subFlowWorkID = 0; // = new Emp(WebUser.getNo());

		//Int64 subFlowWorkID = BP.WF.Dev2Interface.Node_CreateStartNodeWork("024", null, null, "zhanghaicheng",
		//    "1", "zhoupeng,zhoushengyu", "子流程发起测试(为开始节点创建多人的工作处理)", "023", workid);



			///检查发起的子流程 流程引擎表 是否完整？
		GenerWorkFlow gwf = new GenerWorkFlow(subFlowWorkID);
		if (!gwf.getPFlowNo().equals("023"))
		{
			throw new RuntimeException("@父流程编号错误,应当是023现在是" + gwf.getPFlowNo());
		}

		if (gwf.getPWorkID() != workid)
		{
			throw new RuntimeException("@父流程WorkID错误,应当是" + workid + "现在是" + gwf.getPWorkID());
		}

		if (!gwf.getStarter().equals(flowStarter.getNo()))
		{
			throw new RuntimeException("@流程发起人编号错误,应当是" + flowStarter.getNo() + "现在是" + gwf.getStarter());
		}

		if (!gwf.getStarterName().equals(flowStarter.getName()))
		{
			throw new RuntimeException("@流程发起人 Name 错误,应当是" + flowStarter.getName() + "现在是" + gwf.getStarterName());
		}

		if (!gwf.getFK_Dept().equals("1"))
		{
			throw new RuntimeException("@流程隶属部门错误,应当是  1 现在是" + gwf.getFK_Dept());
		}

		if (!gwf.getTitle().equals("子流程发起测试(为开始节点创建多人的工作处理)"))
		{
			throw new RuntimeException("@流程标题 子流程发起测试 错误,应当是 子流程发起测试 现在是" + gwf.getTitle());
		}

		if (gwf.getWFState() != WFState.Runing)
		{
			throw new RuntimeException("@流程 WFState 错误,应当是 Runing 现在是" + gwf.getWFState());
		}

		if (gwf.getFID() != 0)
		{
			throw new RuntimeException("@FID错误,应当是0现在是" + gwf.getFID());
		}

		if (!gwf.getFK_Flow().equals("024"))
		{
			throw new RuntimeException("@FK_Flow错误,应当是024现在是" + gwf.getFK_Flow());
		}

		if (gwf.getFK_Node() != 2401)
		{
			throw new RuntimeException("@停留的当前节点错误,应当是2401现在是" + gwf.getFK_Flow());
		}

		GenerWorkerLists gwls = new GenerWorkerLists(subFlowWorkID, 2401);
		if (gwls.size() != 2)
		{
			throw new RuntimeException("@待办列表个数应当2,现在是" + gwls.size());
		}

		// 检查发起人列表是否完整？
		for (GenerWorkerList gwl : gwls.ToJavaList())
		{
			if (gwl.getIsPassInt() != 0)
			{
				throw new RuntimeException("@通过状态应当是未通过，现在是:" + gwl.getIsPassInt());
			}

			if (gwl.getFID() != 0)
			{
				throw new RuntimeException("@流程ID 应当是0 ，现在是:" + gwl.getFID());
			}

			if (gwl.getFK_Emp().equals("zhoupeng"))
			{
				Emp tempEmp = new Emp(gwl.getFK_Emp());
				if (!gwl.getFK_Dept().equals(tempEmp.getFK_Dept()))
				{
					throw new RuntimeException("@FK_Dept  错误, 现在是:" + gwl.getFK_Dept());
				}
			}
		}

			/// 检查发起的子流程 流程引擎表 是否完整？


			///检查发起的子流程数据是否完整？
		//检查报表数据是否完整?
		sql = "SELECT * FROM ND24Rpt WHERE OID=" + subFlowWorkID;
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到子流程的报表数据.");
		}

		for (DataColumn dc : dt.Columns)
		{
			String val = dt.Rows.get(0).getValue(dc.ColumnName).toString();
			switch (dc.ColumnName)
			{
				case GERptAttr.PWorkID:
					if (!String.valueOf(gwf.getPWorkID()).equals(val))
					{
						throw new RuntimeException("@应当是父流程的workid,现是在:" + val);
					}
					break;
				case GERptAttr.PFlowNo:
					if (!val.equals("023"))
					{
						throw new RuntimeException("@应当是023");
					}
					break;
				case GERptAttr.FID:
					if (!val.equals("0"))
					{
						throw new RuntimeException("@应当是0");
					}
					break;
				case GERptAttr.FK_Dept:
					if (!val.equals("1"))
					{
						throw new RuntimeException("@应当是  1, 现在是:" + val);
					}
					break;
				case GERptAttr.FK_NY:
					if (!val.equals(DataType.getCurrentYearMonth()))
					{
						throw new RuntimeException("@应当是" + DataType.getCurrentYearMonth() + ", 现在是:" + val);
					}
					break;
				case GERptAttr.FlowDaySpan:
					if (!val.equals("0"))
					{
						throw new RuntimeException("@应当是 0 , 现在是:" + val);
					}
					break;
				//case GERptAttr.FlowEmps:
				//    if (val.Contains(empSubFlowStarter.getNo()) == false)
				//        throw new Exception("@应当是包含当前人员, 现在是:" + val);
				//    break;
				//case GERptAttr.FlowEnder:
				//    if (val != empSubFlowStarter.getNo())
				//        throw new Exception("@应当是 empSubFlowStarter.getNo(), 现在是:" + val);
				//    break;
				case GERptAttr.FlowEnderRDT:
					if (val.contains(DataType.getCurrentDate()) == false)
					{
						throw new RuntimeException("@应当是 当前日期, 现在是:" + val);
					}
					break;
				case GERptAttr.FlowEndNode:
					if (!val.equals("2401"))
					{
						throw new RuntimeException("@应当是 2401, 现在是:" + val);
					}
					break;
				//case GERptAttr.FlowStarter:
				//    if (val != empSubFlowStarter.getNo())
				//        throw new Exception("@应当是  WebUser.getNo(), 现在是:" + val);
				//    break;
				case GERptAttr.FlowStartRDT:
					if (DataType.IsNullOrEmpty(val))
					{
						throw new RuntimeException("@应当不能为空,现在是:" + val);
					}
					break;
				case GERptAttr.Title:
					if (DataType.IsNullOrEmpty(val))
					{
						throw new RuntimeException("@不能为空title" + val);
					}
					break;
				case GERptAttr.WFState:
					WFState sta = WFState.forValue(Integer.parseInt(val));
					if (sta != WFState.Runing)
					{
						throw new RuntimeException("@应当是  WFState.Runing 现在是" + sta.toString());
					}
					break;
				default:
					break;
			}
		}

			/// 检查是否完整？

		// 测试以子流程向下发送，是否成功？
		bp.wf.Dev2Interface.Port_Login("zhoupeng");
		objs = bp.wf.Dev2Interface.Node_SendWork("024", subFlowWorkID);


			///检查返回来的变量数据完整性。
		if (objs.getVarToNodeID() != 2402)
		{
			throw new RuntimeException("@子流程向下发送时不成功.");
		}


			/// 检查数据完整性。


			///检查其它的人在开始节点上是否还有待办工作？
		//检查报表数据是否完整?
		sql = "SELECT * FROM WF_EmpWorks WHERE WorkID=" + subFlowWorkID + " AND FK_Emp='zhoushengyu'";
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 0)
		{
			throw new RuntimeException("@在开始节点一个人处理完成后，其它人还有待办.");
		}


			/// 检查其它的人在开始节点上是否还有待办工作。
	}
}