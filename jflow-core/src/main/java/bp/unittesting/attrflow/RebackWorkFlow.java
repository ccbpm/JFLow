package bp.unittesting.attrflow;

import bp.wf.*;
import bp.da.*;
import bp.unittesting.*;

/** 
 回滚一个流程
*/
public class RebackWorkFlow extends TestBase
{
	/** 
	 回滚一个流程
	*/
	public RebackWorkFlow()
	{
		this.Title = "回滚一个流程";
		this.DescIt = "流程完成后，由于种种原因需要回滚它。";
		this.editState = EditState.Passed;
	}
	/** 
	 执行的方法
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{
		// 执行完成一个流程。
		long workid = this.RunCompeleteOneWork();

		bp.wf.Dev2Interface.Port_Login("admin");

		//恢复到最后一个节点上.
		bp.wf.Dev2Interface.Flow_DoRebackWorkFlow("024", workid, 0, "test");


			///检查数据是否完整.
		String sql = "SELECT COUNT(*) AS N FROM WF_EmpWorks where fk_emp='zhanghaicheng' and workid=" + workid;
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			throw new RuntimeException("@不应该找不到 zhanghaicheng 的待办.");
		}

			/// 检查数据是否完整.

		//让他向下发送.
		// BP.WF.Dev2Interface.Port_Login("zhanghaicheng");
		// BP.WF.Dev2Interface.Node_SendWork("024", workid);

		//恢复到第二个节点上去。
		workid = this.RunCompeleteOneWork();
		bp.wf.Dev2Interface.Flow_DoRebackWorkFlow("024", workid, 2402, "test");


			///检查数据是否完整.
		sql = "SELECT COUNT(*) AS N FROM WF_EmpWorks where fk_emp='zhoupeng' and workid=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			throw new RuntimeException("@不应该找不到 zhoupeng 的待办.");
		}

			/// 检查数据是否完整.
	}
	/** 
	 运行完一个流程，并返回它的workid.
	 
	 @return 
	 * @throws Exception 
	*/
	public final long RunCompeleteOneWork() throws Exception
	{
		String fk_flow = "024";
		String startUser = "zhanghaicheng";
		bp.port.Emp starterEmp = new bp.port.Emp(startUser);

		Flow fl = new Flow(fk_flow);

		//让zhanghaicheng登录, 在以后，就可以访问WebUser.getNo(), WebUser.getName() 。
		bp.wf.Dev2Interface.Port_Login(startUser);

		//创建空白工作, 发起开始节点.
		long workid = bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow);
		//执行发送，并获取发送对象,.
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workid);

		//执行第二步 :  .
		bp.wf.Dev2Interface.Port_Login(objs.getVarAcceptersID());
		objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workid);

		//执行第三步完成:  .
		bp.wf.Dev2Interface.Port_Login(objs.getVarAcceptersID());
		objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workid);
		return workid;
	}
}