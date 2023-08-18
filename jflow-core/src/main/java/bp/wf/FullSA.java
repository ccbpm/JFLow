package bp.wf;

import bp.da.*;
import bp.wf.template.*;
import bp.port.*;
import java.util.Date;

/** 
 计算未来处理人
*/
public class FullSA
{

		///#region 方法.
	/** 
	 计算两个时间点.
	 
	 @param sa
	 @param nd
	*/
	private void InitDT(SelectAccper sa, Node nd) throws Exception {
		//计算上一个时间的发送点.
		if (this.LastTimeDot == null)
		{
			Paras ps = new Paras();
			ps.SQL = "SELECT SDT FROM WF_GenerWorkerlist WHERE WorkID=" + ps.getDBStr() + "WorkID AND FK_Node=" + ps.getDBStr() + "FK_Node";
			ps.Add("WorkID", this.HisCurrWorkNode.getWorkID());
			ps.Add("FK_Node", nd.getNodeID());
			DataTable dt = DBAccess.RunSQLReturnTable(ps);

			for (DataRow dr : dt.Rows)
			{
				this.LastTimeDot = dr.getValue(0).toString();
				break;
			}
		}

		//上一个节点的发送时间点或者 到期的时间点，就是当前节点的接受任务的时间。
		sa.setPlanADT(this.LastTimeDot);

		//计算当前节点的应该完成日期。
		Date dtOfShould = Glo.AddDayHoursSpan( this.LastTimeDot, nd.getTimeLimit(), nd.getTimeLimitHH(), nd.getTimeLimitMM(), nd.getTWay());
		sa.setPlanSDT(DataType.dateToStr(dtOfShould,DataType.getSysDatetimeFormatCN()));

		//给最后的时间点复制.
		this.LastTimeDot = sa.getPlanSDT();
	}
	/** 
	 当前节点应该完成的日期.
	*/
	private String LastTimeDot = null;
	/** 
	 工作Node.
	*/
	public WorkNode HisCurrWorkNode = null;

		///#endregion 方法.

	/** 
	 自动计算未来处理人（该方法在发送成功后执行.）
	*/
	public FullSA()
	{
	}
	public long WorkID = 0;
	public GERpt geRpt = null;
	public GenerWorkFlow gwf = null;
	public Work wk = null;

	/** 
	 自动计算未来处理人（该方法在发送成功后执行.）
	 
	 @param currWorkNode 执行的WorkNode
	*/
	public final void DoIt2023(WorkNode currWorkNode) throws Exception {
		//如果当前不需要计算未来处理人.
		if (currWorkNode.getHisFlow().getItIsFullSA() == false && currWorkNode.ItIsSkip == false)
		{
			return;
		}

		//如果到达最后一个节点,就不处理了.
		if (currWorkNode.getHisNode().getItIsEndNode())
		{
			return;
		}

		//设置变量.
		this.WorkID = currWorkNode.getWorkID();
		this.geRpt = currWorkNode.rptGe;
		this.gwf = currWorkNode.getHisGenerWorkFlow();
		this.wk = currWorkNode.getHisWork();

		//初始化一些变量.
		this.HisCurrWorkNode = currWorkNode;
		Node currND = currWorkNode.getHisNode();
		long workid = currWorkNode.getHisWork().getOID();

		//获得到达的节点.
		Node toNode = currWorkNode.NodeSend_GenerNextStepNode(true);

		//1.调用处理下一个节点的接收人.
		WebUserCopy webUser = new WebUserCopy();
		webUser.LoadWebUser();

		//调用到达node.
		InitToNode(currWorkNode, toNode, webUser);

		//更新人员.
		DBAccess.RunSQL("UPDATE WF_SelectAccper SET EmpName = (SELECT Name FROM Port_Emp WHERE NO=WF_SelectAccper.FK_Emp ) WHERE WF_SelectAccper.WorkID=" + this.WorkID);
	}

	/** 
	 处理到达的节点.
	*/
	public final void InitToNode(WorkNode currWN, Node toNd, WebUserCopy webUser) throws Exception {
		if (toNd.getItIsEndNode() == true)
		{
			return;
		}

		//工作节点.
		WorkNode town = new WorkNode(this.wk, toNd);
		town.setWebUser(webUser); //更改身份.

		town.rptGe = this.geRpt; //设置变量.
		town.setHisGenerWorkFlow(this.gwf);

		//开始找人.
		FindWorker fw = new FindWorker();
		fw.setWebUser(webUser); //设置实体.
		fw.currWn = currWN;
		Node toNode = town.getHisNode();
		//if ((currWN.getHisNode().TodolistModel == TodolistModel.Teamup || currWN.getHisNode().TodolistModel == TodolistModel.TeamupGroupLeader)
		//    && (toNode.getHisDeliveryWay() == DeliveryWay.ByStation || toNode.getHisDeliveryWay() == DeliveryWay.BySenderParentDeptLeader || toNode.getHisDeliveryWay() == DeliveryWay.BySenderParentDeptStations))
		//    return Teamup_InitWorkerLists(fw, town);
	   DataTable dt = fw.DoIt(currWN.getHisFlow(), currWN, town);
		if (dt == null)
		{
			throw new RuntimeException(bp.wf.Glo.multilingual("@没有找到接收人.", "WorkNode", "not_found_receiver"));
		}

		//删除可能有的数据.
		DBAccess.RunSQL("DELETE FROM WF_SelectAccper WHERE WorkID=" + this.WorkID + " AND FK_Node =" + toNode.getNodeID());

		//把人员保存进去.
		SelectAccper sa = new SelectAccper();
		for (DataRow dr : dt.Rows)
		{
			String no = dr.getValue(0).toString();
			String name = "";
			if (dt.Columns.size()== 2)
			{
				name = dr.getValue(0).toString();
			}

			sa = new SelectAccper();
			sa.setEmpNo(no);
			sa.setEmpName(name);
			sa.setNodeID(toNd.getNodeID());
			sa.setNodeIdx(toNd.getStep()); //步骤.

			sa.setWorkID(this.WorkID);
			sa.setInfo("无");
			sa.setAccType(0);
			sa.ResetPK();
			if (sa.getIsExits())
			{
				continue;
			}
			//计算接受任务时间与应该完成任务时间.
			InitDT(sa, toNode);
			sa.Insert();
		}

		//定义变量.
		WebUserCopy myWebUser = new WebUserCopy();
		myWebUser.LoadEmpNo(sa.getEmpNo());

		//执行抄送.
		WorkCC cc = new WorkCC(town, myWebUser);
		cc.DoCC(); //执行抄送动作.

		//计算获得到达的节点.
		Node toNodeTo = town.NodeSend_GenerNextStepNode();

		//递归调用.
		InitToNode(town, toNodeTo, myWebUser);
	}

}
