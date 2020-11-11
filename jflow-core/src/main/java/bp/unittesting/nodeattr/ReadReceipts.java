package bp.unittesting.nodeattr;

import bp.wf.*;
import bp.en.*;
import bp.da.*;
import bp.web.*;
import bp.unittesting.*;
import bp.unittesting.*;
import java.util.*;

/** 
 已读回执
*/
public class ReadReceipts extends TestCaseTemplete
{
	/** 
	 已读回执
	*/
	public ReadReceipts()
	{
		this.Title = "已读回执";
		this.DescIt = "036 已读回执-请假流程 ";
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
	 测试案例 
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{
		//初始化变量.
		fk_flow = "036";
		userNo = "zhangyifan";
		fl = new Flow(fk_flow);

		//执行登陆.
		bp.wf.Dev2Interface.Port_Login(userNo);

		// 创建工作。
		this.workID=bp.wf.Dev2Interface.Node_CreateBlankWork(this.fk_flow, null, null, null, null);
		// 发送到下一步骤(部门经理审批).
		SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(this.fk_flow, this.workID, null, null, 0, null);

		// 获取下一步骤的接收人员，让下一步人员登陆.
		String nextWorker2 = objs.getVarAcceptersID();
		bp.wf.Dev2Interface.Port_Login(nextWorker2);

		// 执行部门经理读取工作的api.
		bp.wf.Dev2Interface.Node_SetWorkRead(objs.getVarToNodeID(), this.workID);


			///检查发起人是否接受到了回执消息.
		if (bp.wf.Glo.getIsEnableSysMessage() == true)
		{
			String sql = "SELECT * FROM Sys_SMS WHERE MsgFlag='RP" + this.workID + "_3602' AND " + SMSAttr.SendTo + "='" + userNo + "'";
			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			if (dt.Rows.size() == 0)
			{
				throw new RuntimeException("@人员(" + userNo + ")没有接受到回执消息。");
			}
		}

			/// 检查发起人是否接受到了回执消息.

		// 让第2步的人员发送， 并获取发送对象.
		Hashtable ht = new Hashtable();
		ht.put(bp.wf.WorkSysFieldAttr.SysIsReadReceipts, "1"); //传入表单参数.
		objs = bp.wf.Dev2Interface.Node_SendWork(this.fk_flow, this.workID, ht, null, 0, null);

		String nextWorker3 = objs.getVarAcceptersID(); //获得第三步骤地接受者。
		bp.wf.Dev2Interface.Port_Login(nextWorker3); // 让第三步骤地工作人员登陆。
		bp.wf.Dev2Interface.Node_SetWorkRead(objs.getVarToNodeID(), this.workID); //执行读取回执。


			///检查部门经理是否接受了已读回执.
		if (bp.wf.Glo.getIsEnableSysMessage() == true)
		{
			String sql = "SELECT * FROM Sys_SMS WHERE MsgFlag='RP" + this.workID + "_3699' AND " + SMSAttr.SendTo + "='" + nextWorker2 + "'";
			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			if (dt.Rows.size() == 0)
			{
				throw new RuntimeException("@人员(" + nextWorker2 + ")没有接受到回执消息。");
			}
		}

			/// 检查部门经理是否接受了已读回执.
	}
}