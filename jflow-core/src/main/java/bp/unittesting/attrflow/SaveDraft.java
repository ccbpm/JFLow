package bp.unittesting.attrflow;

import bp.wf.*;
import bp.wf.template.DraftRole;
import bp.da.*;
import bp.unittesting.*;

/** 
 保存草稿
*/
public class SaveDraft extends TestBase
{
	/** 
	 保存草稿
	*/
	public SaveDraft()
	{
		this.Title = "保存草稿";
		this.DescIt = "第一个节点需要保存草稿.";
		this.editState = EditState.Passed;
	}
	/** 
	 执行的方法
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{
		//让liyan 登录.
		bp.wf.Dev2Interface.Port_Login("liyan");

		//设置流程为不保存草稿.
		Flow fl = new Flow("001");
		fl.setDraftRole(DraftRole.None);
		fl.Update();

		//创建workid.
		long workid = bp.wf.Dev2Interface.Node_CreateBlankWork(fl.getNo());

		//删除草稿.
		bp.wf.Dev2Interface.Flow_DoDeleteDraft(fl.getNo(), workid, true);

		//重建workid.
		workid = bp.wf.Dev2Interface.Node_CreateBlankWork(fl.getNo());

		//执行保存.
		bp.wf.Dev2Interface.Node_SaveWork(fl.getNo(), 101, workid);


			///检查保存的结果.
		//从待办理找，如果找到就是错误。
		String sql = "SELECT count(workid) as Num FROM WF_EmpWorks WHERE WorkID=" + workid;
		if (DBAccess.RunSQLReturnValInt(sql, 0) >= 1)
		{
			throw new RuntimeException("@系统错误：不应该查询到他的待办,当前的状态是:" + fl.getDraftRole());
		}

		//获取该流程下的草稿， 如果在草稿箱里能够找到草稿，就是错误。
		DataTable dt = bp.wf.Dev2Interface.DB_GenerDraftDataTable(fl.getNo());
		for (DataRow dr : dt.Rows)
		{
			if (dr.getValue("WorkID").toString().equals(String.valueOf(workid)))
			{
				throw new RuntimeException("@系统错误： 不应该在草稿箱里，找到他的草稿。");
			}
		}

			/// 检查保存的结果.

		//设置规则为，保存到待办列表. 
		fl.setDraftRole(DraftRole.SaveToTodolist);
		fl.Update();
		//执行保存.
		bp.wf.Dev2Interface.Node_SaveWork(fl.getNo(), 101, workid);


			///检查保存的结果.
		//从待办理找，如果找不到就是错误。
		sql = "SELECT count(workid) as Num FROM WF_EmpWorks WHERE WorkID=" + workid;
		if (DBAccess.RunSQLReturnValInt(sql, 0) != 1)
		{
			throw new RuntimeException("@系统错误：没有在待办理列表里，找到他的待办, 当前的状态是:" + fl.getDraftRole() + " sql=" + sql);
		}

		//获取该流程下的草稿， 如果在草稿箱里能够找到草稿，就是错误。
		dt = bp.wf.Dev2Interface.DB_GenerDraftDataTable(fl.getNo());
		for (DataRow dr : dt.Rows)
		{
			if (dr.getValue("WorkID").toString().equals(String.valueOf(workid)))
			{
				throw new RuntimeException("@系统错误： 不应该在草稿箱里，找到他的草稿。");
			}
		}

			/// 检查保存的结果.


		//设置规则为，保存到草稿箱. 
		fl.setDraftRole(DraftRole.SaveToDraftList);
		fl.Update();
		//执行保存.
		bp.wf.Dev2Interface.Node_SaveWork(fl.getNo(), 101, workid);


			///检查保存的结果.
		//从待办理找，如果找不到就是错误。
		sql = "SELECT count(workid) as Num FROM WF_EmpWorks WHERE WorkID=" + workid;
		if (DBAccess.RunSQLReturnValInt(sql, 0) == 0)
		{
			throw new RuntimeException("@系统错误：应该查询到他的待办 .但是没有查询到:" + fl.getDraftRole() + " SQL=" + sql);
		}


		//从待办理找，如果找不到就是错误。
		sql = "SELECT count(workid) as Num FROM WF_EmpWorks WHERE WorkID=" + workid + " AND WFState=1";
		if (DBAccess.RunSQLReturnValInt(sql, 0) == 0)
		{
			throw new RuntimeException("@系统错误：应该查询到他的待办 .但是没有查询到:" + fl.getDraftRole() + " 请检查是否是草稿状态, SQL=" + sql);
		}


		//获取该流程下的草稿， 如果在草稿箱里能够找到草稿，就是错误。
		dt = bp.wf.Dev2Interface.DB_GenerDraftDataTable(fl.getNo());
		boolean isHave = false;
		for (DataRow dr : dt.Rows)
		{
			if (dr.getValue("WorkID").toString().equals(String.valueOf(workid)))
			{
				isHave = true;
				break;
			}
		}
		if (isHave == false)
		{
			throw new RuntimeException("@系统错误： 没有在草稿箱里，找到他的草稿。");
		}


			/// 检查保存的结果.

		//把规则设置回来.
		fl.setDraftRole(DraftRole.None);
		fl.Update();

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