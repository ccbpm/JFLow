package bp.unittesting.nodeattr;

import bp.wf.*;
import bp.en.*;
import bp.da.*;
import bp.web.*;
import bp.unittesting.*;
import bp.wf.template.*;
import bp.unittesting.*;

/** 
 测试挂起
*/
public class TestHungUp extends TestBase
{
	/** 
	 测试挂起
	*/
	public TestHungUp()
	{
		this.Title = "测试挂起";
		this.DescIt = "流程: 以demo 流程023 为例测试，流程的挂起，解除挂起。";
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
	 1, 此流程针对于最简单的分合流程进行， zhanghaicheng发起，zhoushengyu,zhangyifan,两个人处理子线程，
		zhanghaicheng 接受子线程汇总数据.
	 2, 测试方法体分成三大部分. 发起，子线程处理，合流点执行，分别对应: Step1(), Step2_1(), Step2_2()，Step3() 方法。
	 3，针对发送测试，不涉及到其它的功能.
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{
		HungUp huEn = new HungUp();
		huEn.CheckPhysicsTable();

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

		//执行挂起。
		bp.wf.Dev2Interface.Node_HungUpWork(workID, 0, null, "hungup test");


			///检查执行挂起的预期结果.
		GenerWorkFlow gwf = new GenerWorkFlow(this.workID);
		if (gwf.getWFState() != WFState.HungUp)
		{
			throw new RuntimeException("@应当是挂起的状态，现在是：" + gwf.getWFStateText());
		}

		GenerWorkerLists gwls = new GenerWorkerLists(workID, this.fk_flow);
		for (GenerWorkerList gwl : gwls.ToJavaList())
		{
			if (gwl.getFK_Node() == fl.getStartNodeID())
			{
				continue;
			}

			if (gwl.getDTOfHungUp().length() < 10)
			{
				throw new RuntimeException("@挂起日期没有写入");
			}

			if (DataType.IsNullOrEmpty(gwl.getDTOfUnHungUp()) == false)
			{
				throw new RuntimeException("@解除挂起日期应当为空,现在是：" + gwl.getDTOfUnHungUp());
			}

			if (gwl.getHungUpTimes() != 1)
			{
				throw new RuntimeException("@挂起次数应当为１");
			}
		}

		HungUp hu = new HungUp();
		hu.setMyPK("2302_" + this.workID);
		if (hu.RetrieveFromDBSources() == 0)
		{
			throw new RuntimeException("@没有找到　HungUp　数据。");
		}


			/// 检查执行挂起的预期结果

		//解除挂起。
		bp.wf.Dev2Interface.Node_UnHungUpWork(workID, "un hungup test");


			///检查接触执行挂起的预期结果.
		gwf = new GenerWorkFlow(this.workID);
		if (gwf.getWFState() != WFState.Runing)
		{
			throw new RuntimeException("@应当是挂起的状态，现在是：" + gwf.getWFStateText());
		}

			/// 检查接触执行挂起的预期结果

		//执行多次挂起于解除挂起.
		bp.wf.Dev2Interface.Node_HungUpWork(workID, 0, null, "hungup test");
		bp.wf.Dev2Interface.Node_UnHungUpWork(workID, "un hungup test");
	}
}