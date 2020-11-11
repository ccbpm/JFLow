package bp.unittesting.nodeattr;

import bp.wf.*;
import bp.en.*;
import bp.da.*;
import bp.web.*;
import bp.unittesting.*;
import bp.unittesting.*;

/** 
 测试加签
*/
public class TestAskFor extends TestBase
{
	/** 
	 测试加签
	*/
	public TestAskFor()
	{
		this.Title = "测试加签";
		this.DescIt = "流程: 以demo 流程023 为例测试，节点的加签，加签的发送。";
		this.editState = EditState.Passed;
	}


		///全局变量
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

		/// 变量

	/** 
	 测试案例说明:
	 1， 以最简单的三节点流程 023 说明。
	 2， 测试加签的两种模式
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{
		//模式0，加签后，由加签人发送到下一步.
		this.Mode0();

		//模式1，加签后，由被加签人发送到加签人.
		this.Mode1();
	}

	public final void Mode0() throws Exception
	{
		this.fk_flow = "023";
		fl = new Flow("023");
		String sUser = "zhoupeng";
		bp.wf.Dev2Interface.Port_Login(sUser);

		//创建.
		workID = bp.wf.Dev2Interface.Node_CreateBlankWork(fl.getNo());

		//执行发送.
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(fl.getNo(), workID);

		//让他登陆。
		bp.wf.Dev2Interface.Port_Login(objs.getVarAcceptersID());

		//执行加签，并且直接向下发送。
		bp.wf.Dev2Interface.Node_Askfor(workID, AskforHelpSta.AfterDealSend, "liping", "askforhelp test");


			///检查执行挂起的预期结果.
		GenerWorkFlow gwf = new GenerWorkFlow(this.workID);
		if (gwf.getWFState() != WFState.Askfor)
		{
			throw new RuntimeException("@应当是加签的状态，现在是：" + gwf.getWFStateText());
		}

		if (gwf.getFK_Node() != objs.getVarToNodeID())
		{
			throw new RuntimeException("@流程的待办节点应当是(" + objs.getVarToNodeID() + ")，现在是：" + gwf.getFK_Node());
		}

		// 获取当前工作列表.
		GenerWorkerLists gwls = new GenerWorkerLists(objs.getVarWorkID(), objs.getVarToNodeID());
		if (gwls.size() != 2)
		{
			throw new RuntimeException("@应当有两个人员的列表，加签人与被加签人，现在是:" + gwls.size() + "个");
		}

		String sql = "SELECT * FROM WF_GenerWorkerList WHERE FK_Emp='liping' AND WorkID=" + workID + " AND FK_Node=" + gwf.getFK_Node();
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@没有找到被加签人的工作.");
		}

		// 检查被加签人的状态.
		String var = dt.Rows.get(0).getValue(GenerWorkerListAttr.IsPass).toString();
		if (!var.equals("0"))
		{
			throw new RuntimeException("@被加签人的isPass状态应该是0,显示到待办。，现在是:" + var);
		}

		// 检查被加签人的待办工作.
		sql = "SELECT * FROM WF_EmpWorks WHERE FK_Emp='liping' AND WorkID=" + workID + " AND FK_Node=" + gwf.getFK_Node();
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@没有找到被加签人的待办工作.");
		}

			/// 检查执行挂起的预期结果



			///检查加签人
		sql = "SELECT * FROM WF_GenerWorkerList WHERE FK_Emp='" + objs.getVarAcceptersID() + "' AND WorkID=" + workID + " AND FK_Node=" + gwf.getFK_Node();
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@没有找到加签人的工作.");
		}
		var = dt.Rows.get(0).getValue(GenerWorkerListAttr.IsPass).toString();
		if (!var.equals("5"))
		{
			throw new RuntimeException("@被加签人的isPass状态应该是0,显示到待办。，现在是:" + var);
		}

			/// 检查加签人

		//让加签人登录.
		bp.wf.Dev2Interface.Port_Login("liping");

		// 让加签人执行登录.
		SendReturnObjs objsAskFor = bp.wf.Dev2Interface.Node_SendWork(fl.getNo(), workID);


			///检查加签人执行的结果.
		gwf = new GenerWorkFlow(this.workID);
		if (gwf.getWFState() != WFState.Runing)
		{
			throw new RuntimeException("@应当是的运行状态，现在是：" + gwf.getWFStateText());
		}

		if (gwf.getFK_Node() == objs.getVarCurrNodeID())
		{
			throw new RuntimeException("@应当运行到下一个节点，但是现在是仍然停留在当前节点上：" + gwf.getFK_Node());
		}

		// 检查加签订人的工作.
		sql = "SELECT * FROM WF_EmpWorks WHERE FK_Emp='" + objs.getVarAcceptersID() + "' AND WorkID=" + workID + " AND FK_Node=" + gwf.getFK_Node();
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 0)
		{
			throw new RuntimeException("@不应该找到，加签人的待办工作." + objs.getVarAcceptersID());
		}

			/// 检查加签人执行的结果.
	}

	/** 
	 执行加签后，让被加前人，发送给加签人.
	 * @throws Exception 
	*/
	public final void Mode1() throws Exception
	{
		this.fk_flow = "023";
		fl = new Flow("023");
		String sUser = "zhoupeng";
		bp.wf.Dev2Interface.Port_Login(sUser);

		//创建.
		workID = bp.wf.Dev2Interface.Node_CreateBlankWork(fl.getNo());

		//执行发送.
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(fl.getNo(), workID);

		//让他登陆。
		bp.wf.Dev2Interface.Port_Login(objs.getVarAcceptersID());

		//执行加签，并且直接向下发送。
		bp.wf.Dev2Interface.Node_Askfor(workID, AskforHelpSta.AfterDealSendByWorker, "liping", "askforhelp test");


			///检查执行挂起的预期结果.
		GenerWorkFlow gwf = new GenerWorkFlow(this.workID);
		if (gwf.getWFState() != WFState.Askfor)
		{
			throw new RuntimeException("@应当是加签的状态，现在是：" + gwf.getWFStateText());
		}

		if (gwf.getFK_Node() != objs.getVarToNodeID())
		{
			throw new RuntimeException("@流程的待办节点应当是(" + objs.getVarToNodeID() + ")，现在是：" + gwf.getFK_Node());
		}

		// 获取当前工作列表.
		GenerWorkerLists gwls = new GenerWorkerLists(objs.getVarWorkID(), objs.getVarToNodeID());
		if (gwls.size() != 2)
		{
			throw new RuntimeException("@应当有两个人员的列表，加签人与被加签人，现在是:" + gwls.size() + "个");
		}

		String sql = "SELECT * FROM WF_GenerWorkerList WHERE FK_Emp='liping' AND WorkID=" + workID + " AND FK_Node=" + gwf.getFK_Node();
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@没有找到被加签人的工作.");
		}

		// 检查被加签人的状态.
		String var = dt.Rows.get(0).getValue(GenerWorkerListAttr.IsPass).toString();
		if (!var.equals("0"))
		{
			throw new RuntimeException("@被加签人的isPass状态应该是0,显示到待办。，现在是:" + var);
		}

		// 检查被加签人的待办工作.
		sql = "SELECT * FROM WF_EmpWorks WHERE FK_Emp='liping' AND WorkID=" + workID + " AND FK_Node=" + gwf.getFK_Node();
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@没有找到被加签人的待办工作.");
		}

			/// 检查执行挂起的预期结果



			///检查加签人
		sql = "SELECT * FROM WF_GenerWorkerList WHERE FK_Emp='" + objs.getVarAcceptersID() + "' AND WorkID=" + workID + " AND FK_Node=" + gwf.getFK_Node();
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@没有找到加签人的工作.");
		}
		var = dt.Rows.get(0).getValue(GenerWorkerListAttr.IsPass).toString();
		if (!var.equals("6"))
		{
			throw new RuntimeException("@加签人的isPass状态应该是6，现在是:" + var);
		}

			/// 检查加签人

		//让加签人登录.
		bp.wf.Dev2Interface.Port_Login("liping");

		// 让加签人执行登录.
		SendReturnObjs objsAskFor = bp.wf.Dev2Interface.Node_SendWork(fl.getNo(), workID);


			///检查加签人执行的结果.
		gwf = new GenerWorkFlow(this.workID);
		if (gwf.getWFState() != WFState.Askfor)
		{
			throw new RuntimeException("@应当是的加签状态，现在是:" + gwf.getWFStateText());
		}

		if (gwf.getFK_Node() != objsAskFor.getVarToNodeID())
		{
			throw new RuntimeException("@不应当运行到下一个节点，现在运行到了:" + gwf.getFK_Node());
		}

		// 检查加签订人的工作.
		sql = "SELECT * FROM WF_EmpWorks WHERE FK_Emp='" + objs.getVarAcceptersID() + "' AND WorkID=" + workID + " AND FK_Node=" + gwf.getFK_Node();
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			throw new RuntimeException("@不应该找不到加签人的待办工作:" + objs.getVarAcceptersID());
		}

			/// 检查加签人执行的结果.
	}
}