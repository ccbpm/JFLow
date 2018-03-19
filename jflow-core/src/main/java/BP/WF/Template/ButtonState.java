package BP.WF.Template;

import BP.DA.DBAccess;
import BP.DA.DataTable;
import BP.WF.GenerWorkerList;
import BP.WF.GenerWorkerListAttr;
import BP.WF.Node;
import BP.WF.WorkNode;

/** 
 按钮状态
*/
public class ButtonState
{
	public long WorkID = 0;
	public int CurrNodeIDOfUI = 0;
	public int CurrNodeIDOfFlow = 0;
	public String FK_Flow = null;
	public final void InitNodeIsCurr()
	{
		// 获取.
		Node nd = new Node(this.CurrNodeIDOfFlow);
		if (nd.getIsStartNode())
		{
			// 开始节点允许删除流程 
			this.Btn_DelFlow = true;
			this.Btn_Send = true;
			this.Btn_Save = true;
			return;
		}
		WorkNode wn = new WorkNode(this.WorkID, this.CurrNodeIDOfFlow);
		WorkNode wnPri = wn.GetPreviousWorkNode();

		// 判断它是否可以处理上一步工作.
		GenerWorkerList wl = new GenerWorkerList();
		int num = wl.Retrieve(GenerWorkerListAttr.FK_Emp, BP.Web.WebUser.getNo(), GenerWorkerListAttr.FK_Node, wnPri.getHisNode().getNodeID(), GenerWorkerListAttr.WorkID, this.WorkID);
		if (num >= 1)
		{
			//如果能够处理上一步工作
		}
		else
		{
			//不能处理上一步工作, 就可以让其退回
			this.Btn_Return = nd.getIsCanReturn();
			this.Btn_Send = true;
			this.Btn_Save = true;
		}
	}
	public final void InitNodeIsNotCurr()
	{
		String sql = "SELECT count(*) FROM WF_GenerWorkerlist WHERE FK_Node=" + this.CurrNodeIDOfUI + " AND WorkID=" + this.WorkID;
		if (DBAccess.RunSQLReturnValInt(sql, 0) >= 1)
		{
			this.Btn_UnSend = true;
		}
	}
	public ButtonState(String fk_flow, int currNodeID, long workid)
	{
		this.FK_Flow = fk_flow;
		this.CurrNodeIDOfUI = currNodeID;
		this.WorkID = workid;
		if (workid != 0)
		{
			this.Btn_Track = true;
		}

		String sql = "SELECT FK_Node FROM WF_GenerWorkFlow WHERE WorkID=" + workid;
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			// 说明没有 workid 初始化工作的情况, 只有保存与发送两个按钮是可用的 
			this.Btn_Send = true;
			this.Btn_Save = true;
			return;
		}

		// 设置当前流程节点。
		this.CurrNodeIDOfFlow = Integer.parseInt(dt.Rows.get(0).getValue(0).toString());

		if (this.CurrNodeIDOfUI == this.CurrNodeIDOfFlow)
		{
			//如果流程运行的节点与当前的节点是相等的
			InitNodeIsCurr();
		}
		else
		{
			InitNodeIsNotCurr();
		}
	}
	/** 
	 保存按钮
	*/
	public boolean Btn_Send = false;
	/** 
	 保存按钮
	*/
	public boolean Btn_Save = false;
	/** 
	 转发
	*/
	public boolean Btn_Forward = false;
	/** 
	 退回
	*/
	public boolean Btn_Return = false;
	/** 
	 撤销发送
	*/
	public boolean Btn_UnSend = false;
	/** 
	 删除流程
	*/
	public boolean Btn_DelFlow = false;
	/** 
	 新建流程
	*/
	public boolean Btn_NewFlow = false;
	/** 
	 工作轨迹
	*/
	public boolean Btn_Track = false;
}