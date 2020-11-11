package bp.unittesting.returncase;

import bp.wf.*;
import bp.en.*;
import bp.da.*;
import bp.web.*;
import bp.unittesting.*;
import bp.unittesting.*;

public class Test005Return extends TestBase
{
	/** 
	 测试子线程向合流节点退回
	*/
	public Test005Return()
	{
		this.Title = "测试子线程向合流节点退回";
		this.DescIt = "1,退回到开始点，一步步的向下发送。2，退回到分流点，一步步的向下发送.";
		this.DescIt += "重复1,2,测试退回并原路返回。";
		this.editState = EditState.Passed;
	}
	/** 
	 执行.
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{

			///发送
		String fk_flow = "005";
		String startUser = "zhanghaicheng";
		bp.port.Emp starterEmp = new bp.port.Emp(startUser);

		Flow fl = new Flow(fk_flow);

		//让 zhanghaicheng 登录, 在以后，就可以访问WebUser.getNo(), WebUser.getName() 。
		bp.wf.Dev2Interface.Port_Login(startUser);

		//创建空白工作, 发起开始节点.
		long workid = bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow);

		//执行向 分流点 发送, qifenglin接受.
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workid);
		if (!objs.getVarAcceptersID().equals("zhangyifan,zhoushengyu,"))
		{
			throw new RuntimeException("@接受人错误，应当是zhangyifan,zhoushengyu现在是:" + objs.getVarAcceptersID());
		}

			///

		//获得workid.
		String[] workids = objs.getVarTreadWorkIDs().split("[,]", -1);

		long workID1 = Long.parseLong(workids[0]);
		long workID2 = Long.parseLong(workids[1]);

		//让一个子线程登录.
		bp.wf.Dev2Interface.Port_Login("zhangyifan");

		//执行退回.
		String info = bp.wf.Dev2Interface.Node_ReturnWork("005", workID1, workid, 502, 501, startUser, false);


			///检查执行退回是否符合要求。
		GenerWorkFlow gwf = new GenerWorkFlow(workID1);
		if (gwf.getWFState() != WFState.ReturnSta)
		{
			throw new RuntimeException("@子线程的状态应该是退回,现在是:" + gwf.getWFStateText());
		}

		if (!gwf.getStarter().equals("zhanghaicheng"))
		{
			throw new RuntimeException("@子线程的流程发起人应该是，zhanghaicheng,现在是:" + gwf.getStarter());
		}

		if (gwf.getFK_Node() != 501)
		{
			throw new RuntimeException("@子线程的当前节点应该是 501,现在是:" + gwf.getFK_Node());
		}

		//检查是否有待办。
		DataTable dt = bp.wf.Dev2Interface.DB_GenerEmpWorksOfDataTable("zhanghaicheng", "005");
		boolean isHave = false;
		for (DataRow dr : dt.Rows)
		{
			if (!dr.getValue(GenerWorkerListAttr.WorkID).toString().equals(String.valueOf(workID1)))
			{
				continue;
			}
			if (!dr.getValue(GenerWorkerListAttr.FK_Node).toString().equals("501"))
			{
				continue;
			}

			isHave = true;
		}
		if (isHave == false)
		{
			throw new RuntimeException("@没有找到合流节点的待办...");
		}

			/// 检查执行退回是否符合要求。

	}
}