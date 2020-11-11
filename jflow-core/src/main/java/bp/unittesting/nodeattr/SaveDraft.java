package bp.unittesting.nodeattr;

import bp.wf.*;
import bp.wf.template.*;
import bp.wf.data.*;
import bp.en.*;
import bp.da.*;
import bp.web.*;
import bp.unittesting.*;
import bp.unittesting.*;

public class SaveDraft extends TestBase
{
	/** 
	 保存草稿-保存草稿
	*/
	public SaveDraft()
	{
		this.Title = "保存草稿";
		this.DescIt = "新建立一个流程实例，保存草稿是否可以？";
		this.editState = EditState.Passed;
	}
	/** 
	 说明 ：此测试针对于演示环境中的 001 流程编写的单元测试代码。
	 涉及到了: 创建，发送，撤销，方向条件、退回等功能。
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{
		String fk_flow = "032";
		String userNo = "zhanghaicheng";

		Flow fl = new Flow(fk_flow);
		if (fl.getDraftRole() == DraftRole.None)
		{
			fl.setDraftRole(DraftRole.SaveToDraftList);
			fl.Update(); //草稿列表.
		}

		// zhanghaicheng 登录.
		bp.wf.Dev2Interface.Port_Login(userNo);

		//创建空白工作.
		long workid = bp.wf.Dev2Interface.Node_CreateBlankWork(fl.getNo());


			///检查创建新工作是否是blank状态.
		GERpt rpt = fl.getHisGERpt();
		rpt.setOID(workid);
		rpt.RetrieveFromDBSources();
		if (rpt.getWFState() != WFState.Blank)
		{
			throw new RuntimeException("@创建新工作应该是Blank状态，现在是:" + rpt.getWFState());
		}

		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(workid);
		if (gwf.RetrieveFromDBSources() == 0)
		{
			throw new RuntimeException("@创建workid没有写入到流程注册表.");
		}

		if (gwf.getWFState() != WFState.Blank)
		{
			throw new RuntimeException("@流程注册表内的wfstate 不是空白状态，现在状态是:" + rpt.getWFState());
		}

			///


		//执行保存.
		bp.wf.Dev2Interface.Node_SaveWork(fl.getNo(), 3201, workid);


			///检查创建新工作是否是blank状态.
		rpt = fl.getHisGERpt();
		rpt.setOID(workid);
		rpt.RetrieveFromDBSources();
		if (rpt.getWFState() != WFState.Draft)
		{
			throw new RuntimeException("@执行保存后应该是Draft状态，现在是:" + rpt.getWFState());
		}

			///



			///检查草稿是否有？
		boolean isHave = false;
		DataTable dt = bp.wf.Dev2Interface.DB_GenerDraftDataTable();
		for (DataRow dr : dt.Rows)
		{
			if (dr.getValue("OID").toString().equals(String.valueOf(workid)))
			{
				isHave = true;
				break;
			}
		}
		if (isHave == true)
		{
			throw new RuntimeException("@不应该找到草稿。");
		}

			///

		//删除草稿.
		bp.wf.Dev2Interface.Node_DeleteDraft(workid);

		//执行创建工作,一个新的workid.
		long workidNew = bp.wf.Dev2Interface.Node_CreateBlankWork(fl.getNo());

		//比较两个workid是否一致. 
		if (workidNew == workid)
		{
			throw new RuntimeException("@执行删除草稿失败.");
		}

		//设置成草稿.
		bp.wf.Dev2Interface.Node_SetDraft(fl.getNo(), workid);


			///检查保存的草稿数据是否完整。
		rpt = fl.getHisGERpt();
		rpt.setOID(workid);
		rpt.RetrieveFromDBSources();
		if (rpt.getWFState() != WFState.Draft)
		{
			throw new RuntimeException("@此 GERpt 应该是 Draft 状态,现在是:" + rpt.getWFState());
		}

		isHave = false;
		dt = bp.wf.Dev2Interface.DB_GenerDraftDataTable();
		for (DataRow dr : dt.Rows)
		{
			if (dr.getValue("OID").toString().equals(String.valueOf(workid)))
			{
				isHave = true;
				break;
			}
		}
		if (isHave == false)
		{
			throw new RuntimeException("@没有从接口里找到草稿。");
		}

			///

	}
}