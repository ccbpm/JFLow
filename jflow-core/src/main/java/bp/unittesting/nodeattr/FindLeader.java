package bp.unittesting.nodeattr;

import bp.wf.*;
import bp.en.*;
import bp.da.*;
import bp.web.*;
import bp.unittesting.*;
import bp.unittesting.*;
import java.util.*;

/** 
 测试找领导
*/
public class FindLeader extends TestBase
{
	/** 
	 测试找领导
	*/
	public FindLeader()
	{
		this.Title = "测试找领导";
		this.DescIt = "流程: 以demo 流程 054:找人规则(找领导) 为例测试。";
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
	 1， 分别列举4种。
	 2， 测试找领导的两种模式
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{
		this.fk_flow = "054";
		fl = new Flow(this.fk_flow);
		String sUser = "zhoushengyu";
		bp.wf.Dev2Interface.Port_Login(sUser);

		//创建.
		workID = bp.wf.Dev2Interface.Node_CreateBlankWork(fl.getNo());

		//执行发送, 直接领导.
		Hashtable ht = new Hashtable();
		ht.put("FindLeader", 0); // 找直接领导.
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(fl.getNo(), workID, ht);


			///分析预期
		if (!objs.getVarAcceptersID().equals("zhanghaicheng"))
		{
			throw new RuntimeException("@按照找直接领导的方式0, （直接领导模式）找领导错误，应当是zhanghaicheng现在是" + objs.getVarAcceptersID());
		}

			///

		//执行撤销发送. 指定级别的主管.
		bp.wf.Dev2Interface.Flow_DoUnSend(fl.getNo(), workID);
		ht.clear(); //清除参数.
		ht.put("FindLeader", 1); // 找指定职务级别的领导.
		objs = bp.wf.Dev2Interface.Node_SendWork(fl.getNo(), workID, ht);


			///分析预期
		if (!objs.getVarAcceptersID().equals("zhoupeng"))
		{
			throw new RuntimeException("@按照找直接领导的方式1, （指定级别的领导）找领导错误，应当是zhoupeng现在是" + objs.getVarAcceptersID());
		}

			///



		//执行撤销发送. 特定职务的领导.
		bp.wf.Dev2Interface.Flow_DoUnSend(fl.getNo(), workID);
		ht.clear(); //清除参数.
		ht.put("FindLeader", 2); // 找指定职务级别的领导.
		objs = bp.wf.Dev2Interface.Node_SendWork(fl.getNo(), workID, ht);


			///分析预期
		if (!objs.getVarAcceptersID().equals("zhoupeng"))
		{
			throw new RuntimeException("@按照找直接领导的方式1, （指定级别的领导）找领导错误，应当是zhoupeng现在是" + objs.getVarAcceptersID());
		}

			///


		//执行撤销发送. 特定职务的领导.
		bp.wf.Dev2Interface.Flow_DoUnSend(fl.getNo(), workID);
		ht.clear(); //清除参数.
		ht.put("FindLeader", 2); // 找指定职务级别的领导.
		objs = bp.wf.Dev2Interface.Node_SendWork(fl.getNo(), workID, ht);


			///分析预期
		if (!objs.getVarAcceptersID().equals("zhoupeng"))
		{
			throw new RuntimeException("@按照找直接领导的方式1, （指定级别的领导）找领导错误，应当是zhoupeng现在是" + objs.getVarAcceptersID());
		}

			///


		//执行撤销发送. 特定岗位的领导.
		bp.wf.Dev2Interface.Flow_DoUnSend(fl.getNo(), workID);
		ht.clear(); //清除参数.
		ht.put("FindLeader", 3); // 找指定职务级别的领导.
		objs = bp.wf.Dev2Interface.Node_SendWork(fl.getNo(), workID, ht);


			///分析预期
		if (!objs.getVarAcceptersID().equals("zhoupeng"))
		{
			throw new RuntimeException("@按照找直接领导的方式1, （指定级别的领导）找领导错误，应当是zhoupeng现在是" + objs.getVarAcceptersID());
		}

			///


		// 现在已经把第一排的节点，都已经找全了， 测试左边的两个case .找谁?

		// 测试找谁, 指定节点工作人员的直接领导。
		this.DoFineWho_1();


		// 测试找谁, 指定表单字段的工作人员的直接领导。
		this.DoFineWho_2();

	}
	/** 
	 测试找谁? 指定节点工作人员的直接领导。
	 * @throws Exception 
	*/
	public final void DoFineWho_1() throws Exception
	{
		String sUser = "zhoushengyu";
		bp.wf.Dev2Interface.Port_Login(sUser);

		//创建.
		workID = bp.wf.Dev2Interface.Node_CreateBlankWork(fl.getNo());

		//执行发送, 直接领导.
		Hashtable ht = new Hashtable();
		ht.put("FindLeader", 0); // 找直接领导.
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(fl.getNo(), workID, ht);

		// 让zhanghaicheng登录, 执行第二步骤地发送。
		bp.wf.Dev2Interface.Port_Login(objs.getVarAcceptersID());

		// 让zhanghaicheng 执行发送，应该还发送给zhanghaicheng.
		objs = bp.wf.Dev2Interface.Node_SendWork(fl.getNo(), workID);


			///分析预期
		if (!objs.getVarAcceptersID().equals("zhanghaicheng"))
		{
			throw new RuntimeException("@按照找直接领导的方式1, （指定级别的领导）找领导错误，应当是zhanghaicheng现在是" + objs.getVarAcceptersID());
		}

			///

	}
	/** 
	 指定表单字段的人员的直接领导。
	 * @throws Exception 
	*/
	public final void DoFineWho_2() throws Exception
	{
		String sUser = "zhoushengyu";
		bp.wf.Dev2Interface.Port_Login(sUser);

		//创建.
		workID = bp.wf.Dev2Interface.Node_CreateBlankWork(fl.getNo());

		//执行发送, 直接领导.
		Hashtable ht = new Hashtable();
		ht.put("FindLeader", 1); // 找直接领导.
		ht.put("RenYuanBianHao", "zhoutianjiao");
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(fl.getNo(), workID, ht);

		// 让zhanghaicheng登录, 执行第二步骤地发送。
		bp.wf.Dev2Interface.Port_Login(objs.getVarAcceptersID());

		// 让zhanghaicheng 执行发送，应该还发送给zhanghaicheng.
		objs = bp.wf.Dev2Interface.Node_SendWork(fl.getNo(), workID);


			///分析预期
		if (!objs.getVarAcceptersID().equals("qifenglin"))
		{
			throw new RuntimeException("@按照找直接领导的方式1, （指定级别的领导）找领导错误，应当是qifenglin现在是" + objs.getVarAcceptersID());
		}

			///
	}
}