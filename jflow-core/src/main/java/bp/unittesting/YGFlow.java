package bp.unittesting;

import bp.wf.*;
import bp.en.*;
import bp.da.*;
import bp.web.*;

/** 
 延续流程
*/
public class YGFlow extends TestBase
{
	/** 
	 延续流程
	*/
	public YGFlow()
	{
		this.Title = "延续流程";
		this.DescIt = "流程: 以demo 流程209,210 为例测试。";
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
	 1， 测试共享任务的取出，放入。
	 2， 延续流程的两种模式
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{
		if (bp.wf.Glo.getIsEnableTaskPool() == false)
		{
			throw new RuntimeException("@此单元测试需要打开web.config中的IsEnableTaskPool配置.");
		}

		 fl = new Flow("209");

		Node nd = new Node(6899);

		String sUser = "zhoupeng";
		bp.wf.Dev2Interface.Port_Login(sUser);

		//创建.
		workID = bp.wf.Dev2Interface.Node_CreateBlankWork(fl.getNo());

		//执行发送，指定发送给两个人。
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(fl.getNo(), workID, null, null, 0, "liping");

		//让liping登陆。
		bp.wf.Dev2Interface.Port_Login("liping");

		//发送到子流程（延续流程上去）上去.
		objs = bp.wf.Dev2Interface.Node_SendWork(fl.getNo(), workID, null, null, 21001, "liping");

			///检查数据是否完整。



			/// 检查数据是否完整。

		//执行撤销工作.
		bp.wf.Dev2Interface.Flow_DoUnSend(fl.getNo(), workID, 0);


	}
	/** 
	 检查数据
	 * @throws Exception 
	*/
	private void CheckData() throws Exception
	{
		// 执行获取任务。
		bp.wf.Dev2Interface.Node_TaskPoolTakebackOne(workID);


			///检查任务
		gwf = new GenerWorkFlow(this.workID);
		if (gwf.getTaskSta() != TaskSta.Takeback)
		{
			throw new RuntimeException("@应该是取走的状态，但是现在是:" + gwf.getTaskSta().toString());
		}

		// 检查 zhanghaicheng， 他不应该有待办任务。
		int v = DBAccess.RunSQLReturnValInt("SELECT COUNT(*) from wf_empWorks where WorkID=" + this.workID + " AND FK_Emp='zhanghaicheng' ", 100);
		if (v != 0)
		{
			throw new RuntimeException("@不应该找到到他的待办。");
		}

		// 从待办里找,来检查zhangyifan 的任务。
		dt = bp.wf.Dev2Interface.DB_GenerEmpWorksOfDataTable();
		boolean isHave = false;
		for (DataRow dr : dt.Rows)
		{
			if (dr.getValue("WorkID").toString().equals(String.valueOf(this.workID)))
			{
				isHave = true;
				break;
			}
		}
		if (isHave == false)
		{
			throw new RuntimeException("@不应该找不到[" + WebUser.getNo() + "]待办，共享任务，申请下来后也要放在待办里。");
		}

		// 从任务池里找。
		dt = bp.wf.Dev2Interface.DB_TaskPool();
		isHave = false;
		for (DataRow dr : dt.Rows)
		{
			if (dr.getValue("WorkID").toString().equals(String.valueOf(this.workID)))
			{
				isHave = true;
				break;
			}
		}
		if (isHave == true)
		{
			throw new RuntimeException("@执行取走这个任务后，不应该再找到她的任务了。");
		}

		// 获得我申请下来的任务
		dt = bp.wf.Dev2Interface.DB_TaskPoolOfMyApply();
		isHave = false;
		for (DataRow dr : dt.Rows)
		{
			if (dr.getValue("WorkID").toString().equals(String.valueOf(this.workID)))
			{
				isHave = true;
				break;
			}
		}
		if (isHave == false)
		{
			throw new RuntimeException("@没有找到" + WebUser.getNo() + "申请的任务");
		}

			/// 检查任务

		// 放入任务池
		bp.wf.Dev2Interface.Node_TaskPoolPutOne(workID);


			///数据检查
		gwf = new GenerWorkFlow(this.workID);
		if (gwf.getTaskSta() != TaskSta.Sharing)
		{
			throw new RuntimeException("@应当是sharing 现在是:" + gwf.getTaskSta().toString());
		}

		// 检查 zhangyifan
		dt = bp.wf.Dev2Interface.DB_GenerEmpWorksOfDataTable();
		isHave = false;
		for (DataRow dr : dt.Rows)
		{
			if (dr.getValue("WorkID").toString().equals(String.valueOf(this.workID)))
			{
				isHave = true;
				break;
			}
		}

		if (isHave == true)
		{
			throw new RuntimeException("@不应该找到她的待办，因为它是共享任务。");
		}

		// 检查 zhanghaicheng， 他应当有待办任务。
		v = DBAccess.RunSQLReturnValInt("SELECT COUNT(*) from wf_empWorks where WorkID=" + this.workID + " AND FK_Emp='zhanghaicheng' ", 100);
		if (v != 1)
		{
			throw new RuntimeException("@不应该找不到到他的待办。");
		}

		// 从任务池里找。
		dt = bp.wf.Dev2Interface.DB_TaskPool();
		isHave = false;
		for (DataRow dr : dt.Rows)
		{
			if (dr.getValue("WorkID").toString().equals(String.valueOf(this.workID)))
			{
				isHave = true;
				break;
			}
		}
		if (isHave == false)
		{
			throw new RuntimeException("@没有在任务池里找到她的待办。");
		}

			/// 检查是否具有她的待办。
	}
}