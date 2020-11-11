package bp.unittesting;

import bp.wf.*;
import bp.en.*;
import bp.da.*;
import bp.web.*;

/** 
 关于标题
*/
public class AboutTitle extends TestBase
{
	/** 
	 关于标题
	*/
	public AboutTitle()
	{
		this.Title = "关于标题";
		this.DescIt = "以测试用例的-001流程做测试用例.";
		this.editState = EditState.Passed;
	}
	/** 
	 执行
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{
		String fk_flow = "001";
		Flow fl = new Flow(fk_flow);

		bp.wf.Dev2Interface.Port_Login("zhoutianjiao");

		//创建空白工作, 发起开始节点.
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_StartWork(fk_flow, null, null, 0, null);

		long workid = objs.getVarWorkID();

		//用第二个人员登录.
		bp.wf.Dev2Interface.Port_Login(objs.getVarAcceptersID());

		//设置标题
		bp.wf.Dev2Interface.Flow_SetFlowTitle(fk_flow, workid, "test");


			///检查标题是否正确.
		String title = DBAccess.RunSQLReturnString("SELECT Title FROM WF_GenerWorkFlow  WHERE WorkID=" + workid);
		if (!title.equals("test"))
		{
			throw new RuntimeException("@Flow_SetFlowTitle 方法失败. WF_GenerWorkFlow没有变化正确。");
		}
		title = DBAccess.RunSQLReturnString("SELECT Title FROM ND" + Integer.parseInt(fl.getNo()) + "Rpt  WHERE OID=" + workid);
		if (!title.equals("test"))
		{
			throw new RuntimeException("@Flow_SetFlowTitle 方法失败.Rpt table 没有变化正确。");
		}

			/// 检查标题是否正确.


		//重新设置标题
		bp.wf.Dev2Interface.Flow_ReSetFlowTitle(fk_flow, objs.getVarToNodeID(), workid);


			///检查标题是否正确.
		  title = DBAccess.RunSQLReturnString("SELECT Title FROM WF_GenerWorkFlow  WHERE WorkID=" + workid);
		if (title.equals("test"))
		{
			throw new RuntimeException("@Flow_SetFlowTitle 方法失败. WF_GenerWorkFlow没有变化正确。");
		}
		title = DBAccess.RunSQLReturnString("SELECT Title FROM ND" + Integer.parseInt(fl.getNo()) + "Rpt  WHERE OID=" + workid);
		if (title.equals("test"))
		{
			throw new RuntimeException("@Flow_SetFlowTitle 方法失败.Rpt table 没有变化正确。");
		}

			/// 检查标题是否正确.

	}
}