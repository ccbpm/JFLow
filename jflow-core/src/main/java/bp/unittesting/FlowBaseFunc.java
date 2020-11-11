package bp.unittesting;

import bp.wf.*;
import bp.wf.data.*;
import bp.wf.template.*;
import bp.en.*;
import bp.da.*;
import bp.web.*;

/** 
 流程基础功能
*/
public class FlowBaseFunc extends TestBase
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

		/// 变量

	/** 
	 流程基础功能
	*/
	public FlowBaseFunc()
	{
		this.Title = "流程基础 API 功能测试";
		this.DescIt = "创建、删除、移交、转发、抄送。";
		this.editState = EditState.Passed;
	}
	/** 
	 执行
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{

			///定义全局变量.
		fk_flow = "023";
		userNo = "zhanghaicheng";
		fl = new Flow(fk_flow);

			/// 定义全局变量.

		// 测试删除.
		this.TestDelete();

		// 测试移交.
		this.TestShift();

		// 测试抄送.
		this.TestCC();
	}

	/** 
	 测试抄送
	 * @throws Exception 
	*/
	public final void TestCC() throws Exception
	{
		String sUser = "zhoupeng";
		bp.wf.Dev2Interface.Port_Login(sUser);

		//创建.
		workID = bp.wf.Dev2Interface.Node_CreateBlankWork(fl.getNo());

		//执行发送.
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(fl.getNo(), workID);

		//执行抄送.
		bp.wf.Dev2Interface.Node_CC(fl.getNo(), objs.getVarToNodeID(), workID, "zhoushengyu", "zhoupeng", "移交测试", "", null, 0);

		//让 zhoushengyu 登陆.
		bp.wf.Dev2Interface.Port_Login("zhoushengyu");


			///检查预期结果.
		sql = "SELECT FK_Emp FROM WF_EmpWorks WHERE WorkID=" + workID;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			throw new RuntimeException("@移交后待办丢失。");
		}

		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@应该只有一个人处于待办状态。");
		}

		if (!dt.Rows.get(0).getValue(0).toString().equals("zhoupeng"))
		{
			throw new RuntimeException("@应该是:zhoupeng 现在是:" + dt.Rows.get(0).getValue(0).toString());
		}

		CCList list = new CCList();
		int num = list.Retrieve(CCListAttr.Rec, sUser, CCListAttr.WorkID, workID);
		if (num <= 0)
		{
			throw new RuntimeException("@没有写入抄送数据在 WF_CCList 表中,查询的数量是:" + num);
		}

			/// 检查预期结果
	}
	/** 
	 测试移交
	 * @throws Exception 
	*/
	public final void TestShift() throws Exception
	{
		//创建.
		workID = bp.wf.Dev2Interface.Node_CreateBlankWork(fl.getNo());

		//执行发送.
		bp.wf.Dev2Interface.Node_SendWork(fl.getNo(), workID);

		//执行移交.
		//BP.WF.Dev2Interface.Node_Shift(fl.getNo(), 0, workID, "zhoushengyu", "移交测试");


			///检查预期结果
		sql = "SELECT * FROM WF_EmpWorks WHERE WorkID=" + workID;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			throw new RuntimeException("@移交后待办丢失。");
		}

		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@应该只有一个人处于待办状态。");
		}

		if (!dt.Rows.get(0).getValue("FK_Emp").toString().equals("zhoushengyu"))
		{
			throw new RuntimeException("@没有移交给 zhoushengyu");
		}


			/// 检查预期结果
	}
	/** 
	 测试删除
	 * @throws Exception 
	*/
	public final void TestDelete() throws Exception
	{
		//创建.
		workID = bp.wf.Dev2Interface.Node_CreateBlankWork(fl.getNo());

		//执行发送.
		bp.wf.Dev2Interface.Node_SendWork(fl.getNo(), workID);

		//执行删除.
		bp.wf.Dev2Interface.Flow_DoDeleteFlowByReal(workID, false);


			///检查删除功能是否符合预期.
		gwf = new GenerWorkFlow();
		gwf.setWorkID(workID);
		if (gwf.RetrieveFromDBSources() != 0)
		{
			throw new RuntimeException("@GenerWorkFlow未删除的数据.");
		}

		gwl = new GenerWorkerList();
		gwl.setWorkID(workID);
		if (gwl.RetrieveFromDBSources() != 0)
		{
			throw new RuntimeException("@GenerWorkerList未删除的数据.");
		}

		sql = "SELECT * FROM ND2301 WHERE OID=" + workID;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 0)
		{
			throw new RuntimeException("@ ND2301 节点数据未删除. ");
		}

		sql = "SELECT * FROM ND2302 WHERE OID=" + workID;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 0)
		{
			throw new RuntimeException("@ ND2302 节点数据未删除. ");
		}

		sql = "SELECT * FROM ND23Rpt WHERE OID=" + workID;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 0)
		{
			throw new RuntimeException("@ ND23Rpt 数据未删除. ");
		}

			/// 检查删除功能是否符合预期.
	}
}