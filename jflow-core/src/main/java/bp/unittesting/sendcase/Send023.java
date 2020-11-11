package bp.unittesting.sendcase;

import bp.wf.*;
import bp.wf.data.*;
import bp.wf.template.*;
import bp.en.*;
import bp.port.Emp;
import bp.da.*;
import bp.web.*;
import bp.unittesting.*;
import bp.unittesting.*;
import java.util.*;

/** 
 线性节点的流程发送
*/
public class Send023 extends TestBase
{

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
	public long workID = 0;
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
	/** 
	 发起人
	*/
	public bp.port.Emp starterEmp = null;

		/// 变量

	/** 
	 线性节点的流程发送
	*/
	public Send023()
	{
		this.Title = "线性节点的流程发送";
		this.DescIt = "流程:023最简单的3节点(轨迹模式),执行发送后的数据是否符合预期要求.";
		this.editState = EditState.Passed; //已经能过.
	}

	/** 
	 过程说明：
	 1，以流程 023最简单的3节点(轨迹模式)， 为测试用例。
	 2，仅仅测试发送功能，与检查发送后的数据是否完整.
	 3, 此测试有三个节点发起点、中间点、结束点，对应三个测试方法。
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{

			///定义变量.
		fk_flow = "023";
		userNo = "zhanghaicheng";
		fl = new Flow(fk_flow);

			/// 定义变量.

		//执行第1步骤. 让zhanghaicheng 发起流程.
		this.Step1();

		//执行第2步骤. 让 zhoupeng 处理.
		this.Step2();

		//执行第3步骤. 让zhanghaicheng 结束.
		this.Step3();
	}
	/** 
	 步骤1 让zhanghaicheng 发起流程.
	 * @throws Exception 
	*/
	public final void Step1() throws Exception
	{
		//给发起人赋值.
		starterEmp = new Emp(userNo);

		//让 userNo 登录.
		bp.wf.Dev2Interface.Port_Login(userNo);

		//创建空白工作, 发起开始节点.
		workID = bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow);



			///检查创建工作是否符合预期.
		//检查开始节点写入的数据是否正确？
		sql = "SELECT * FROM ND2301 WHERE OID=" + workID;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到开始节点的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.Rows.get(0).getValue(dc.ColumnName).toString();
			switch (dc.ColumnName)
			{

				case WorkAttr.Emps:
					if (val.contains(WebUser.getNo()) == false)
					{
						throw new RuntimeException("应当包含当前人员,现在是:" + val);
					}
					break;
				case WorkAttr.FID:
					if (!val.equals("0"))
					{
						throw new RuntimeException("应当 = 0,现在是:" + val);
					}
					break;
				case WorkAttr.MD5:
					break;
				case WorkAttr.Rec:
					if (!val.equals(WebUser.getNo()))
					{
						throw new RuntimeException("应当 Rec= " + WebUser.getNo() + ",现在是:" + val);
					}
					break;
				default:
					break;
			}
		}


		//检查流程表的数据.
		sql = "SELECT * FROM " + fl.getPTable() + " WHERE OID=" + workID;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@流程报表数据被删除了.");
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
					if (!val.equals(WebUser.getFK_Dept()))
					{
						throw new RuntimeException("@应当是" + WebUser.getFK_Dept() + ", 现在是:" + val);
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
					if (bp.wf.Glo.getUserInfoShowModel() != UserInfoShowModel.UserNameOnly)
					{
						if (val.contains(WebUser.getNo()) == false)
						{
							throw new RuntimeException("@应当是包含当前人员, 现在是:" + val);
						}
					}
					break;
				case GERptAttr.FlowEnder:
					if (!val.equals(WebUser.getNo()))
					{
						throw new RuntimeException("@应当是 当前人员, 现在是:" + val);
					}
					break;
				case GERptAttr.FlowEnderRDT:
					if (val.contains(DataType.getCurrentDate()) == false)
					{
						throw new RuntimeException("@应当是 当前日期, 现在是:" + val);
					}
					break;
				case GERptAttr.FlowEndNode:
					if (!val.equals("2301"))
					{
						throw new RuntimeException("@应当是 2301, 现在是:" + val);
					}
					break;
				case GERptAttr.FlowStarter:
					if (!val.equals(WebUser.getNo()))
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
					if (sta != WFState.Blank)
					{
						throw new RuntimeException("@应当是  WFState.Blank 现在是" + sta.toString());
					}
					break;
				default:
					break;
			}
		}


			/// 检查创建工作是否符合预期

		//定义一个参数.
		Hashtable ht = new Hashtable();
		ht.put("GoTo", 1);
		ht.put("MyPara", "TestPara");

		//执行发送.
		objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workID, ht);


			///第1步: 检查发送对象.
		//从获取的发送对象里获取到下一个工作者. zhangyifan(张一帆)、zhoushengyu(周升雨).
		if (!objs.getVarAcceptersID().equals("zhoupeng"))
		{
			throw new RuntimeException("@下一步的接受人不正确, 应当是: zhoupeng.现在是:" + objs.getVarAcceptersID());
		}

		if (objs.getVarToNodeID() != 2302)
		{
			throw new RuntimeException("@应该是 2302 节点. 现在是:" + objs.getVarToNodeID());
		}

		if (objs.getVarWorkID() != workID)
		{
			throw new RuntimeException("@主线程的workid不应该变化:" + objs.getVarWorkID());
		}

		if (objs.getVarCurrNodeID() != 2301)
		{
			throw new RuntimeException("@当前节点的编号不能变化:" + objs.getVarCurrNodeID());
		}

		if (objs.getVarTreadWorkIDs() != null)
		{
			throw new RuntimeException("@不应当获得子线程WorkID.");
		}



			/// 第1步: 检查发送对象.


			///第2步: 检查流程引擎表.
		//检查创建这个空白是否有数据完整?
		sql = "SELECT * FROM WF_EmpWorks WHERE WorkID=" + workID + " AND FK_Emp='" + objs.getVarAcceptersID() + "'";
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			throw new RuntimeException("@不应该找不到当前人员的待办.");
		}

		gwf = new GenerWorkFlow(workID);
		if (!gwf.getStarter().equals(WebUser.getNo()) || !gwf.getStarterName().equals(WebUser.getName()))
		{
			throw new RuntimeException("没有写入发起人的信息.");
		}

		if (!gwf.getFK_Dept().equals(starterEmp.getFK_Dept()))
		{
			throw new RuntimeException("@发起人的部门有变化，应当是" + starterEmp.getFK_Dept() + ",现在是:" + gwf.getFK_Dept());
		}

		if (!gwf.getStarter().equals(starterEmp.getNo()))
		{
			throw new RuntimeException("@发起人的 No 有变化，应当是" + starterEmp.getNo() + ",现在是:" + gwf.getStarter());
		}

		//判断当前点.
		if (gwf.getFK_Node() != 2302)
		{
			throw new RuntimeException("@当前点应该是 2302 现在是:" + gwf.getFK_Node());
		}

		//判断TodoEmpsNum.
		if (gwf.getTodoEmpsNum() != 1)
		{
			throw new RuntimeException("@当前点应该是 TodoEmpsNum=0  现在是:" + gwf.getTodoEmpsNum());
		}

		//判断TodoEmps.
		if (!gwf.getTodoEmps().equals("zhoupeng,周朋;"))
		{
			throw new RuntimeException("@当前点应该是 TodoEmps=zhoupeng,周朋;  现在是:" + gwf.getTodoEmps());
		}

		//判断FID.
		if (gwf.getFID() != 0)
		{
			throw new RuntimeException("@当前点应该是 FID=0  现在是:" + gwf.getFID());
		}

		//判断PWorkID，没有谁调用它，应当是 0. 
		if (gwf.getPWorkID() != 0)
		{
			throw new RuntimeException("@没有谁调用它, 当前点应该是 PWorkID=0  现在是:" + gwf.getPWorkID());
		}

		//判断 WFState . 
		if (gwf.getWFState() != WFState.Runing)
		{
			throw new RuntimeException("@应当是 WFState=Runing 现在是:" + gwf.getWFState().toString());
		}

		//检查开始节点 发送人的WF_GenerWorkerList的.
		gwl = new GenerWorkerList();
		gwl.CheckPhysicsTable();

		gwl.setFK_Emp(WebUser.getNo());
		gwl.setFK_Node(2301);
		gwl.setWorkID(workID);
		gwl.Retrieve();

		// 没有分合流应当是 0 .
		if (gwl.getFID() != 0)
		{
			throw new RuntimeException("@没有分合流应当是 0.");
		}

		if (gwl.getIsEnable() == false)
		{
			throw new RuntimeException("@应该是启用的状态 ");
		}

		if (gwl.getIsPass() == false)
		{
			throw new RuntimeException("@应该是通过的状态 ");
		}

		//if (gwl.Sender.Contains(WebUser.getNo())==false)
		//    throw new Exception("@应该是 包含当前状态 . ");

		//if (gwl. != "@MyPara=TestPara@GoTo=1")
		//    throw new Exception("@参数应当是:@MyPara=TestPara@GoTo=1 .现在是:" + gwl.Paras);

		//检查接受人的 WF_GenerWorkerList 的.
		gwl = new GenerWorkerList();
		gwl.setFK_Emp(objs.getVarAcceptersID());
		gwl.setFK_Node(2302);
		gwl.setWorkID(workID);
		gwl.Retrieve();

		// 没有分合流应当是 0 .
		if (gwl.getFID() != 0)
		{
			throw new RuntimeException("@没有分合流应当是 0.");
		}

		if (gwl.getIsEnable() == false)
		{
			throw new RuntimeException("@应该是启用的状态 ");
		}

		if (gwl.getIsPass() == true)
		{
			throw new RuntimeException("@应该是未通过的状态 ");
		}

		//if (gwl.Sender.Contains(WebUser.getNo())==false)
		//    throw new Exception("@应该是 当前人发送的，现在是: " + gwl.Sender);

			/// 第2步: 检查流程引擎表.


			///第3步: 检查节点数据表.
		sql = "SELECT * FROM ND2301 WHERE OID=" + workID;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到开始节点的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.Rows.get(0).getValue(dc.ColumnName).toString();
			switch (dc.ColumnName)
			{
				case WorkAttr.Emps:
					if (val.contains(WebUser.getNo()) == false)
					{
						throw new RuntimeException("应当包含当前人员,现在是:" + val);
					}
					break;
				case WorkAttr.FID:
					if (!val.equals("0"))
					{
						throw new RuntimeException("应当 = 0,现在是:" + val);
					}
					break;
				case WorkAttr.MD5:
					//if (Glo.ism
					//if (val !="0")
					//    throw new Exception("应当 = 0,现在是:"+val);
					break;

				case WorkAttr.Rec:
					if (!val.equals(WebUser.getNo()))
					{
						throw new RuntimeException("应当 Rec= " + WebUser.getNo() + ",现在是:" + val);
					}
					break;
				//case WorkAttr.Sender:
				//    if (val != WebUser.getNo())
				//        throw new Exception("应当 Sender= " + WebUser.getNo() + ",现在是:" + val);
				//    break;
				default:
					break;
			}
		}

		//检查节点2的数据.
		sql = "SELECT * FROM ND2302 WHERE OID=" + workID;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND2302 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.Rows.get(0).getValue(dc.ColumnName).toString();
			switch (dc.ColumnName)
			{
				case WorkAttr.Emps:
					if (val.contains("zhoupeng") == false)
					{
						throw new RuntimeException("第二步骤的处理人员,应当zhoupeng ,现在是:" + val);
					}
					break;
				case WorkAttr.FID:
					if (!val.equals("0"))
					{
						throw new RuntimeException("应当 = 0,现在是:" + val);
					}
					break;
				case WorkAttr.MD5:
					//if (Glo.ism
					//if (val !="0")
					//    throw new Exception("应当 = 0,现在是:"+val);
					break;

				case WorkAttr.Rec:
					if (!val.equals("zhoupeng"))
					{
						throw new RuntimeException("应当 Rec=zhoupeng,现在是:" + val);
					}
					break;
				default:
					break;
			}
		}

		//检查流程表的数据.
		sql = "SELECT * FROM " + fl.getPTable() + " WHERE OID=" + workID;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@流程报表数据被删除了.");
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
					if (!val.equals(WebUser.getFK_Dept()))
					{
						throw new RuntimeException("@应当是" + WebUser.getFK_Dept() + ", 现在是:" + val);
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
					if (bp.wf.Glo.getUserInfoShowModel() != UserInfoShowModel.UserNameOnly)
					{
						if (val.contains(WebUser.getNo()) == false)
						{
							throw new RuntimeException("@应当是包含当前人员, 现在是:" + val);
						}
					}
					break;
				case GERptAttr.FlowEnder:
					if (!val.equals(WebUser.getNo()))
					{
						throw new RuntimeException("@应当是 当前人员, 现在是:" + val);
					}
					break;
				case GERptAttr.FlowEnderRDT:
					if (val.contains(DataType.getCurrentDate()) == false)
					{
						throw new RuntimeException("@应当是 当前日期, 现在是:" + val);
					}
					break;
				case GERptAttr.FlowEndNode:
					if (!val.equals("2302"))
					{
						throw new RuntimeException("@应当是 2302, 现在是:" + val);
					}
					break;
				case GERptAttr.FlowStarter:
					if (!val.equals(WebUser.getNo()))
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

			/// 第3步: 检查节点数据表.
	}
	/** 
	 步骤1 让 zhoupeng 登录去处理.
	 * @throws Exception 
	*/
	public final void Step2() throws Exception
	{
		//让 zhouepng 登录.
		bp.wf.Dev2Interface.Port_Login("zhoupeng");

		//让他向下发送.
		objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workID);


			///第1步: 检查发送对象.
		//从获取的发送对象里获取到下一个工作者. zhangyifan(张一帆)、zhoushengyu(周升雨).
		if (!objs.getVarAcceptersID().equals("zhanghaicheng"))
		{
			throw new RuntimeException("@下一步的接受人不正确, 应当是: zhanghaicheng.现在是:" + objs.getVarAcceptersID());
		}

		if (objs.getVarToNodeID() != 2399)
		{
			throw new RuntimeException("@应该是 2399 节点. 现在是:" + objs.getVarToNodeID());
		}

		if (objs.getVarWorkID() != workID)
		{
			throw new RuntimeException("@主线程的workid不应该变化:" + objs.getVarWorkID());
		}

		if (objs.getVarCurrNodeID() != 2302)
		{
			throw new RuntimeException("@当前节点的编号不能变化,现在是:" + objs.getVarCurrNodeID());
		}

		if (objs.getVarTreadWorkIDs() != null)
		{
			throw new RuntimeException("@不应当获得子线程WorkID.");
		}


			/// 第1步: 检查发送对象.


			///第2步: 检查流程引擎表.
		//检查待办是否存在。
		sql = "SELECT * FROM WF_EmpWorks WHERE WorkID=" + workID + " AND FK_Emp='" + objs.getVarAcceptersID() + "'";
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			throw new RuntimeException("@不应该找不到当前人员的待办.");
		}

		gwf = new GenerWorkFlow(workID);

		if (!gwf.getFK_Dept().equals(starterEmp.getFK_Dept()))
		{
			throw new RuntimeException("@发起人的部门有变化，应当是" + starterEmp.getFK_Dept() + ",现在是:" + gwf.getFK_Dept());
		}

		if (!gwf.getStarter().equals(starterEmp.getNo()))
		{
			throw new RuntimeException("@发起人的 No 有变化，应当是" + starterEmp.getNo() + ",现在是:" + gwf.getStarter());
		}

		//判断当前点.
		if (gwf.getFK_Node() != 2399)
		{
			throw new RuntimeException("@当前点应该是 2399 现在是:" + gwf.getFK_Node());
		}

		//判断当前点.
		if (gwf.getFID() != 0)
		{
			throw new RuntimeException("@当前点应该是 FID=0  现在是:" + gwf.getFID());
		}

		//判断PWorkID，没有谁调用它，应当是 0. 
		if (gwf.getPWorkID() != 0)
		{
			throw new RuntimeException("@没有谁调用它, 当前点应该是 PWorkID=0  现在是:" + gwf.getPWorkID());
		}

		//判断 WFState . 
		if (gwf.getWFState() != WFState.Runing)
		{
			throw new RuntimeException("@应当是 WFState=Runing 现在是:" + gwf.getWFState().toString());
		}

		//检查开始节点 发送人的WF_GenerWorkerList 的.
		gwl = new GenerWorkerList();
		gwl.setFK_Emp(WebUser.getNo());
		gwl.setFK_Node(2302);
		gwl.setWorkID(workID);
		gwl.Retrieve();

		// 没有分合流应当是 0 .
		if (gwl.getFID() != 0)
		{
			throw new RuntimeException("@没有分合流应当是 0.");
		}

		if (gwl.getIsEnable() == false)
		{
			throw new RuntimeException("@应该是启用的状态 ");
		}

		if (gwl.getIsPass() == false)
		{
			throw new RuntimeException("@应该是通过的状态 ");
		}

		//if (gwl.Sender.Contains("zhanghaicheng")==false)
		//    throw new Exception("@应该是 包含当前状态 . ");

		//检查接受人的 WF_GenerWorkerList 的.
		gwl = new GenerWorkerList();
		gwl.setFK_Emp(objs.getVarAcceptersID());
		gwl.setFK_Node(2399);
		gwl.setWorkID(workID);
		gwl.Retrieve();

		// 没有分合流应当是 0 .
		if (gwl.getFID() != 0)
		{
			throw new RuntimeException("@没有分合流应当是 0.");
		}

		if (gwl.getIsEnable() == false)
		{
			throw new RuntimeException("@应该是启用的状态 ");
		}

		if (gwl.getIsPass() == true)
		{
			throw new RuntimeException("@应该是未通过的状态 ");
		}

		//if (gwl.Sender.Contains(WebUser.getNo())==false)
		//    throw new Exception("@应该是 当前人发送的，现在是: " + gwl.Sender);

			/// 第2步: 检查流程引擎表.


			///第3步: 检查节点数据表.
		sql = "SELECT * FROM ND2301 WHERE OID=" + workID;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到开始节点的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.Rows.get(0).getValue(dc.ColumnName).toString();
			switch (dc.ColumnName)
			{

				case WorkAttr.Emps:
					if (val.contains("zhanghaicheng") == false)
					{
						throw new RuntimeException("应当包含当前人员,现在是:" + val);
					}
					break;
				case WorkAttr.FID:
					if (!val.equals("0"))
					{
						throw new RuntimeException("应当 = 0,现在是:" + val);
					}
					break;

				case WorkAttr.Rec:
					if (!objs.getVarAcceptersID().equals(val))
					{
						throw new RuntimeException("应当 Rec=zhanghaicheng,现在是:" + val);
					}
					break;

				default:
					break;
			}
		}

		//检查节点2的数据.
		sql = "SELECT * FROM ND2302 WHERE OID=" + workID;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND2302 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.Rows.get(0).getValue(dc.ColumnName).toString();
			switch (dc.ColumnName)
			{
				case WorkAttr.Emps:
					if (val.contains(WebUser.getNo()) == false)
					{
						throw new RuntimeException("应当包含当前人员,现在是:" + val);
					}
					break;
				case WorkAttr.FID:
					if (!val.equals("0"))
					{
						throw new RuntimeException("应当 = 0,现在是:" + val);
					}
					break;

				case WorkAttr.Rec:
					if (!val.equals("zhoupeng"))
					{
						throw new RuntimeException("应当 Rec= zhoupeng,现在是:" + val);
					}
					break;

				default:
					break;
			}
		}

		//检查节点3的数据.
		sql = "SELECT * FROM ND2399 WHERE OID=" + workID;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND2399 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.Rows.get(0).getValue(dc.ColumnName).toString();
			switch (dc.ColumnName)
			{
				case WorkAttr.Emps:
					if (val.contains("zhanghaicheng") == false)
					{
						throw new RuntimeException("应当包含当前人员,现在是:" + val);
					}
					break;
				case WorkAttr.FID:
					if (!val.equals("0"))
					{
						throw new RuntimeException("应当 = 0,现在是:" + val);
					}
					break;
				case WorkAttr.Rec:
					if (!objs.getVarAcceptersID().equals(val))
					{
						throw new RuntimeException("应当 Rec= " + objs.getVarAcceptersID() + ",现在是:" + val);
					}
					break;

				default:
					break;
			}
		}

		//检查流程表的数据.
		sql = "SELECT * FROM " + fl.getPTable() + " WHERE OID=" + workID;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@流程报表数据被删除了.");
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
					bp.port.Emp emp = new Emp("zhanghaicheng");
					if (!val.equals(emp.getFK_Dept()))
					{
						throw new RuntimeException("@应当是" + emp.getFK_Dept() + ", 现在是:" + val);
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
					if (bp.wf.Glo.getUserInfoShowModel() != UserInfoShowModel.UserNameOnly)
					{
						if (val.contains(WebUser.getNo()) == false)
						{
							throw new RuntimeException("@应当是包含当前人员, 现在是:" + val);
						}
					}
					break;
				case GERptAttr.FlowEnder:
					if (!val.equals(WebUser.getNo()))
					{
						throw new RuntimeException("@应当是 当前人员, 现在是:" + val);
					}
					break;
				case GERptAttr.FlowEnderRDT:
					if (val.contains(DataType.getCurrentDate()) == false)
					{
						throw new RuntimeException("@应当是 当前日期, 现在是:" + val);
					}
					break;
				case GERptAttr.FlowEndNode:
					if (!val.equals("2399"))
					{
						throw new RuntimeException("@应当是 2399, 现在是:" + val);
					}
					break;
				case GERptAttr.FlowStarter:
					if (val.equals(WebUser.getNo()))
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
					if (Integer.parseInt(val) != WFState.Runing.getValue())
					{
						throw new RuntimeException("@应当是  WFState.Runing 现在是" + val);
					}
					break;
				default:
					break;
			}
		}

			/// 第3步: 检查节点数据表.
	}
	/** 
	 步骤3 让zhanghaicheng 结束流程.
	 * @throws Exception 
	*/
	public final void Step3() throws Exception
	{
		//让 zhanghaicheng 登录.
		bp.wf.Dev2Interface.Port_Login("zhanghaicheng");

		//让他向下发送.
		objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workID);


			///第1步: 检查发送对象.
		//从获取的发送对象里获取到下一个工作者. zhangyifan(张一帆)、zhoushengyu(周升雨).
		if (objs.getVarAcceptersID() != null)
		{
			throw new RuntimeException("@接受人员应当为空." + objs.getVarAcceptersID());
		}

		if (objs.getVarToNodeID() != 0)
		{
			throw new RuntimeException("@应当是 0  现在是:" + objs.getVarToNodeID());
		}

		if (objs.getVarWorkID() != workID)
		{
			throw new RuntimeException("@主线程的workid不应该变化:" + objs.getVarWorkID());
		}

		if (objs.getVarCurrNodeID() != 2399)
		{
			throw new RuntimeException("@当前节点的编号不能变化,现在是:" + objs.getVarCurrNodeID());
		}

		if (objs.getVarTreadWorkIDs() != null)
		{
			throw new RuntimeException("@不应当获得子线程WorkID.");
		}


			/// 第1步: 检查发送对象.


			///第2步: 检查流程引擎表.
		//检查待办是否存在。
		sql = "SELECT * FROM WF_EmpWorks WHERE WorkID=" + workID;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 0)
		{
			throw new RuntimeException("@不应该有待办.");
		}


		sql = "SELECT * FROM WF_GenerWorkFlow WHERE WorkID=" + workID;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 0)
		{
			throw new RuntimeException("@流程信息未删除.");
		}

		sql = "SELECT * FROM WF_GenerWorkerList  WHERE WorkID=" + workID;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 0)
		{
			throw new RuntimeException("@工作人员信息未删除..");
		}

			/// 第2步: 检查流程引擎表.


			///第3步: 检查节点数据表.
		sql = "SELECT * FROM ND2301 WHERE OID=" + workID;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到开始节点的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.Rows.get(0).getValue(dc.ColumnName).toString();
			switch (dc.ColumnName)
			{
				case GERptAttr.Title:
					if (DataType.IsNullOrEmpty(val))
					{
						throw new RuntimeException("@流程走完后标题丢失了");
					}
					break;
				case WorkAttr.Emps:
					if (val.contains(WebUser.getNo()) == false)
					{
						throw new RuntimeException("应当包含当前人员,现在是:" + val);
					}
					break;

				case WorkAttr.Rec:
					if (!val.equals("zhanghaicheng"))
					{
						throw new RuntimeException("应当 Rec=zhanghaicheng,现在是:" + val);
					}
					break;
				default:
					break;
			}
		}

		//检查节点2的数据.
		sql = "SELECT * FROM ND2302 WHERE OID=" + workID;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND2302 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.Rows.get(0).getValue(dc.ColumnName).toString();
			switch (dc.ColumnName)
			{
				case WorkAttr.Emps:
					if (val.contains("zhoupeng") == false)
					{
						throw new RuntimeException("应当包含当前人员,现在是:" + val);
					}
					break;
				case WorkAttr.Rec:
					if (!val.equals("zhoupeng"))
					{
						throw new RuntimeException("应当 Rec= zhoupeng,现在是:" + val);
					}
					break;
				case WorkAttr.FID:
					if (!val.equals("0"))
					{
						throw new RuntimeException("应当 = 0,现在是:" + val);
					}
					break;
				default:
					break;
			}
		}

		//检查节点3的数据.
		sql = "SELECT * FROM ND2399 WHERE OID=" + workID;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND2399 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.Rows.get(0).getValue(dc.ColumnName).toString();
			switch (dc.ColumnName)
			{

				case WorkAttr.Rec:
					if (!val.equals("zhanghaicheng"))
					{
						throw new RuntimeException("应当 Rec= zhanghaicheng,现在是:" + val);
					}
					break;
				default:
					break;
			}
		}

		 bp.port.Emp emp = new Emp("zhanghaicheng");

		//检查流程表的数据.
		sql = "SELECT * FROM " + fl.getPTable() + " WHERE OID=" + workID;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@流程报表数据被删除了.");
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
					if (!val.equals(emp.getFK_Dept()))
					{
						throw new RuntimeException("@发起人的部门发生了变化，应当是(" + emp.getFK_Dept() + "),现在是:" + val);
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
					if (bp.wf.Glo.getUserInfoShowModel() != UserInfoShowModel.UserNameOnly)
					{
						if (val.contains("zhanghaicheng") == false || val.contains("zhoupeng") == false)
						{
							throw new RuntimeException("@应当包含的人员，现在不存在, 现在是:" + val);
						}
					}
					break;
				case GERptAttr.FlowEnder:
					if (!val.equals("zhanghaicheng"))
					{
						throw new RuntimeException("@应当是 zhanghaicheng , 现在是:" + val);
					}
					break;
				case GERptAttr.FlowEnderRDT:
					if (val.contains(DataType.getCurrentDate()) == false)
					{
						throw new RuntimeException("@应当是 当前日期, 现在是:" + val);
					}
					break;
				case GERptAttr.FlowEndNode:
					if (!val.equals("2399"))
					{
						throw new RuntimeException("@应当是 2399, 现在是:" + val);
					}
					break;
				case GERptAttr.FlowStarter:
					if (!val.equals("zhanghaicheng"))
					{
						throw new RuntimeException("@应当是 zhanghaicheng, 现在是:" + val);
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
					if (Integer.parseInt(val) != WFState.Complete.getValue())
					{
						throw new RuntimeException("@应当是  WFState.Complete 现在是" + val);
					}
					break;
				default:
					break;
			}
		}

			/// 第3步: 检查节点数据表.
	}
}