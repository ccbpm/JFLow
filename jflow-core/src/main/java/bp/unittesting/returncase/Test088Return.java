package bp.unittesting.returncase;

import bp.wf.*;
import bp.en.*;
import bp.da.*;
import bp.web.*;
import bp.unittesting.*;
import bp.unittesting.*;

public class Test088Return extends TestBase
{
	/** 
	 测试退回
	*/
	public Test088Return()
	{
		this.Title = "流程中间步骤具有分合流的退回";
		this.DescIt = "1,退回到开始点，一步步的向下发送。2，退回到分流点，一步步的向下发送.";
		this.DescIt += "重复1,2,测试退回并原路返回。";
		this.editState = EditState.Passed;
	}
	@Override
	public void Do() throws Exception
	{


			///测试退回到开始节点的三种模式.
		//测试从最后一个节点退回到开始节点，然后让其一步步的发送.
		this.TestReturnToStartNode();

		//测试从最后一个节点退回到开始节点，然后让其原路返回.
		this.TestReturnToStartNodeWithTrackback();

		//测试从最后一个节点退回到开始节点，然后让其原路返回， 然后在让其退回并不原路返回。
		this.TestReturnToStartNodeWithTrackback_1();

			/// 测试退回到开始节点的三种模式.



			///测试退回到分流节点的三种模式.
		this.ReturnToFenLiuNode();
		this.ReturnToFenLiuNodeWithTrackback();
		this.ReturnToFenLiuNodeWithTrackback_1();

			/// 测试退回到分流节点的三种模式.

		return;
	}


		///测试退回到分流点.
	/** 
	 测试退回到分流点，然后让其一步步的发送.
	 * @throws Exception 
	*/
	public final void ReturnToFenLiuNode() throws Exception
	{
		//创建测试案例.
		long workid = this.CreateTastCase();

		//让他登录.
		bp.wf.Dev2Interface.Port_Login("zhoupeng");

		//向开始节点退回，并不原路返回。
		bp.wf.Dev2Interface.Node_ReturnWork("088", workid, 0, 8899, 8802, "test", false);

		//让 qifenglin , 登录让其发送,这是合流点发送.
		bp.wf.Dev2Interface.Port_Login("qifenglin");
		bp.wf.Dev2Interface.Node_SendWork("088", workid);

		// 让分流点发送.
		String sql = "SELECT WorkID,FK_Emp FROM WF_GenerWorkerlist WHERE FID=" + workid;
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		for (DataRow dr : dt.Rows)
		{
			long id = Long.parseLong(dr.getValue("WorkID").toString());
			String emp = dr.getValue(1).toString();
			bp.wf.Dev2Interface.Port_Login(emp);
			bp.wf.Dev2Interface.Node_SendWork("088", id);
		}

		//让 zhanghaicheng , 登录让其发送.
		bp.wf.Dev2Interface.Port_Login("zhanghaicheng");
		bp.wf.Dev2Interface.Node_SendWork("088", workid);

		//让 zhoupeng , 登录让其发送。
		bp.wf.Dev2Interface.Port_Login("zhoupeng");
		bp.wf.Dev2Interface.Node_SendWork("088", workid);

		/*
		 * 运行到这里执行成功。
		 */
	}

	/** 
	 测试从最后一个节点退回到分流点，然后让其原路返回..
	 * @throws Exception 
	*/
	public final void ReturnToFenLiuNodeWithTrackback() throws Exception
	{
		//创建测试案例.
		long workid = this.CreateTastCase();

		//让他登录.
		bp.wf.Dev2Interface.Port_Login("zhoupeng");

		//向开始节点退回，并不原路返回。
		bp.wf.Dev2Interface.Node_ReturnWork("088", workid, 0, 8899, 8802, "test", true);

		//让 zhoutianjiao , 登录让其发送。
		bp.wf.Dev2Interface.Port_Login("qifenglin");
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork("088", workid);
		if (!objs.getVarAcceptersID().equals("zhoupeng"))
		{
			throw new RuntimeException("@退回到开始节点并原路返回错误,应该返回给zhoupeng,现在是:" + objs.getVarAcceptersID());
		}

		//让zhoupeng 登录，发送下去。
		bp.wf.Dev2Interface.Port_Login(objs.getVarAcceptersID());
		objs = bp.wf.Dev2Interface.Node_SendWork("088", workid);
	}

	/** 
	 测试从最后一个节点退回到分流点，然后让其原路返回.
	 返回到最后一个节点后，让其在执行一次退回但是并不原路返回，让发起人执行发送
	 看看是否是按照步骤一步步的向下运动。
	 * @throws Exception 
	*/
	public final void ReturnToFenLiuNodeWithTrackback_1() throws Exception
	{
		//创建测试案例.
		long workid = this.CreateTastCase();

		//让他登录.
		bp.wf.Dev2Interface.Port_Login("zhoupeng");

		//向开始节点退回，并不原路返回。
		bp.wf.Dev2Interface.Node_ReturnWork("088", workid, 0, 8899, 8802, "test", true);

		//让 zhoutianjiao , 登录让其发送。
		bp.wf.Dev2Interface.Port_Login("qifenglin");
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork("088", workid);
		if (!objs.getVarAcceptersID().equals("zhoupeng"))
		{
			throw new RuntimeException("@退回到开始节点并原路返回错误,应该返回给zhoupeng,现在是:" + objs.getVarAcceptersID());
		}

		//在执行一次退回,但不原路返回.
		bp.wf.Dev2Interface.Node_ReturnWork("088", workid, 0, 8899, 8802, "test", false);


		//让qifenglin登录, 测试下一步骤，是否按照一步步的走向结束节点。
		bp.wf.Dev2Interface.Port_Login("qifenglin");
		objs = bp.wf.Dev2Interface.Node_SendWork("088", workid);

		// 让分流点发送.
		String sql = "SELECT WorkID,FK_Emp FROM WF_GenerWorkerlist WHERE FID=" + workid;
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			throw new RuntimeException("没有查询到子线程.");
		}

		for (DataRow dr : dt.Rows)
		{
			long id = Long.parseLong(dr.getValue("WorkID").toString());
			String emp = dr.getValue(1).toString();
			bp.wf.Dev2Interface.Port_Login(emp);
			bp.wf.Dev2Interface.Node_SendWork("088", id);
		}

		//让 zhanghaicheng , 登录让其发送.
		bp.wf.Dev2Interface.Port_Login("zhanghaicheng");
		bp.wf.Dev2Interface.Node_SendWork("088", workid);

		//让 zhoupeng , 登录让其发送。
		bp.wf.Dev2Interface.Port_Login("zhoupeng");
		bp.wf.Dev2Interface.Node_SendWork("088", workid);

		/*
		 * 运行到这里执行成功。
		 */
	}


		/// 测试退回到分流点.



		///测试退回到开始节点的三种模式
	/** 
	 测试从最后一个节点退回到开始节点，然后让其原路返回.
	 返回到最后一个节点后，让其在执行一次退回但是并不原路返回，让发起人执行发送
	 看看是否是按照步骤一步步的向下运动。
	 * @throws Exception 
	*/
	public final void TestReturnToStartNodeWithTrackback_1() throws Exception
	{
		//创建测试案例.
		long workid = this.CreateTastCase();

		//让他登录.
		bp.wf.Dev2Interface.Port_Login("zhoupeng");

		//向开始节点退回，并不原路返回。
		bp.wf.Dev2Interface.Node_ReturnWork("088", workid, 0, 8899, 8801, "test", true);

		//让 zhoutianjiao , 登录让其发送。
		bp.wf.Dev2Interface.Port_Login("zhoutianjiao");
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork("088", workid);
		if (!objs.getVarAcceptersID().equals("zhoupeng"))
		{
			throw new RuntimeException("@退回到开始节点并原路返回错误,应该返回给zhoupeng,现在是:" + objs.getVarAcceptersID());
		}

		//在执行一次退回,但不原路返回.
		bp.wf.Dev2Interface.Node_ReturnWork("088", workid, 0, 8899, 8801, "test", false);


		//让zhoutianjiao登录, 测试下一步骤，是否按照一步步的走向结束节点。
		bp.wf.Dev2Interface.Port_Login("zhoutianjiao");
		objs = bp.wf.Dev2Interface.Node_SendWork("088", workid);

		//让 qifenglin , 登录让其发送,这是合流点发送.
		bp.wf.Dev2Interface.Port_Login("qifenglin");
		bp.wf.Dev2Interface.Node_SendWork("088", workid);

		// 让分流点发送.
		String sql = "SELECT WorkID,FK_Emp FROM WF_GenerWorkerlist WHERE FID=" + workid;
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		for (DataRow dr : dt.Rows)
		{
			long id = Long.parseLong(dr.getValue("WorkID").toString());
			String emp = dr.getValue(1).toString();
			bp.wf.Dev2Interface.Port_Login(emp);
			bp.wf.Dev2Interface.Node_SendWork("088", id);
		}

		//让 zhanghaicheng , 登录让其发送.
		bp.wf.Dev2Interface.Port_Login("zhanghaicheng");
		bp.wf.Dev2Interface.Node_SendWork("088", workid);

		//让 zhoupeng , 登录让其发送。
		bp.wf.Dev2Interface.Port_Login("zhoupeng");
		bp.wf.Dev2Interface.Node_SendWork("088", workid);

		/*
		 * 运行到这里执行成功。
		 */
	}

	/** 
	 测试从最后一个节点退回到开始节点，然后让其原路返回..
	 * @throws Exception 
	*/
	public final void TestReturnToStartNodeWithTrackback() throws Exception
	{
		//创建测试案例.
		long workid = this.CreateTastCase();

		//让他登录.
		bp.wf.Dev2Interface.Port_Login("zhoupeng");

		//向开始节点退回，并不原路返回。
		bp.wf.Dev2Interface.Node_ReturnWork("088", workid, 0, 8899, 8801, "test", true);

		//让 zhoutianjiao , 登录让其发送。
		bp.wf.Dev2Interface.Port_Login("zhoutianjiao");
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork("088", workid);
		if (!objs.getVarAcceptersID().equals("zhoupeng"))
		{
			throw new RuntimeException("@退回到开始节点并原路返回错误,应该返回给zhoupeng,现在是:" + objs.getVarAcceptersID());
		}

		//让zhoupeng 登录，发送下去。
		bp.wf.Dev2Interface.Port_Login(objs.getVarAcceptersID());
		objs = bp.wf.Dev2Interface.Node_SendWork("088", workid);

	}
	/** 
	 测试从最后一个节点退回到开始节点，然后让其一步步的发送.
	 * @throws Exception 
	*/
	public final void TestReturnToStartNode() throws Exception
	{
		//创建测试案例.
		long workid = this.CreateTastCase();

		//让他登录.
		bp.wf.Dev2Interface.Port_Login("zhoupeng");

		//向开始节点退回，并不原路返回。
		bp.wf.Dev2Interface.Node_ReturnWork("088", workid, 0, 8899, 8801, "test", false);

		//让 zhoutianjiao , 登录让其发送。
		bp.wf.Dev2Interface.Port_Login("zhoutianjiao");
		bp.wf.Dev2Interface.Node_SendWork("088", workid);

		//让 qifenglin , 登录让其发送,这是合流点发送.
		bp.wf.Dev2Interface.Port_Login("qifenglin");
		bp.wf.Dev2Interface.Node_SendWork("088", workid);

		// 让分流点发送.
		String sql = "SELECT WorkID,FK_Emp FROM WF_GenerWorkerlist WHERE FID=" + workid;
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		for (DataRow dr : dt.Rows)
		{
			long id = Long.parseLong(dr.getValue("WorkID").toString());
			String emp = dr.getValue(1).toString();
			bp.wf.Dev2Interface.Port_Login(emp);
			bp.wf.Dev2Interface.Node_SendWork("088", id);
		}

		//让 zhanghaicheng , 登录让其发送.
		bp.wf.Dev2Interface.Port_Login("zhanghaicheng");
		bp.wf.Dev2Interface.Node_SendWork("088", workid);

		//让 zhoupeng , 登录让其发送。
		bp.wf.Dev2Interface.Port_Login("zhoupeng");
		bp.wf.Dev2Interface.Node_SendWork("088", workid);

		/*
		 * 运行到这里执行成功。
		 */
	}

		/// 测试退回到开始节点的三种模式

	/** 
	 创建一个测试场景，让他跑到结束节点.
	 * @throws Exception 
	*/
	public final long CreateTastCase() throws Exception
	{
		String fk_flow = "088";
		String startUser = "zhoutianjiao";
		bp.port.Emp starterEmp = new bp.port.Emp(startUser);

		Flow fl = new Flow(fk_flow);

		//让周天娇登录, 在以后，就可以访问WebUser.getNo(), WebUser.getName() 。
		bp.wf.Dev2Interface.Port_Login(startUser);

		//创建空白工作, 发起开始节点.
		long workid = bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow);

		//执行向 分流点 发送, qifenglin接受.
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workid);
		if (!objs.getVarAcceptersID().equals("qifenglin"))
		{
			throw new RuntimeException("@接受人错误，应当是qifenglin,现在是:" + objs.getVarAcceptersID());
		}

		//让分流点的发起人登录.
		bp.wf.Dev2Interface.Port_Login(objs.getVarAcceptersID());
		//执行向下发送.
		objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workid);

		if (!objs.getVarAcceptersID().equals("zhangyifan,zhoushengyu,"))
		{
			throw new RuntimeException("@接受人错误，应当是zhangyifan,zhoushengyu,现在是:" + objs.getVarAcceptersID());
		}

		String sql = "SELECT WorkID,FK_Emp FROM WF_GenerWorkerlist WHERE FID=" + workid;
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		for (DataRow dr : dt.Rows)
		{
			long id = Long.parseLong(dr.getValue("WorkID").toString());
			String emp = dr.getValue(1).toString();
			bp.wf.Dev2Interface.Port_Login(emp);
			objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, id);
		}


		//让qifenglin登录,发到最后一个节点,完成tastcase.
		bp.wf.Dev2Interface.Port_Login("zhanghaicheng");
		objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workid);
		if (!objs.getVarAcceptersID().equals("zhoupeng"))
		{
			throw new RuntimeException("@接受人错误，应当是zhoupeng,现在是:" + objs.getVarAcceptersID());
		}

		return workid;
	}

}