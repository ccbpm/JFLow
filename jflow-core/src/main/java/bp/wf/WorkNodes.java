package bp.wf;

import bp.en.*;
import bp.da.*;
import bp.port.*;
import bp.web.*;
import bp.difference.*;
import bp.sys.*;
import bp.wf.template.*;
import bp.wf.data.*;
import bp.wf.template.sflow.*;
import bp.*;
import java.util.*;
import java.time.*;
import java.math.*;

/** 
 工作节点集合.
*/
public class WorkNodes extends ArrayList<WorkNode>
{

		///#region 构造
	/** 
	 他的工作s
	*/
	public final Works getWorks() throws Exception {
		if (this.size() == 0)
		{
			throw new RuntimeException(bp.wf.Glo.multilingual("@初始化失败，没有找到任何节点。", "WorkNode", "not_found_pre_node_3"));
		}

		Works ens = this.get(0).getHisNode().getHisWorks();
		ens.clear();

		for (WorkNode wn : this)
		{
			ens.AddEntity(wn.getHisWork());
		}
		return ens;
	}
	/** 
	 工作节点集合
	*/
	public WorkNodes() throws Exception {
	}

	public final int GenerByFID(Flow flow, long fid) throws Exception {
		this.clear();

		Nodes nds = flow.getHisNodes();
		for (Node nd : nds.ToJavaList())
		{
			if (nd.getHisRunModel() == RunModel.SubThread)
			{
				continue;
			}

			Work wk = nd.GetWork(fid);
			if (wk == null)
			{
				continue;
			}


			this.Add(new WorkNode(wk, nd));
		}
		return this.size();
	}

	public final int GenerByWorkID(Flow flow, long oid) throws Exception {
		/*退回 ,需要判断跳转的情况，如果是跳转的需要退回到他开始执行的节点
		* 跳转的节点在WF_GenerWorkerlist中不存在该信息
		*/
		String table = "ND" + Integer.parseInt(flow.getNo()) + "Track";

		String actionSQL = "SELECT EmpFrom,EmpFromT,RDT,NDFrom FROM " + table + " WHERE WorkID=" + oid + " AND (ActionType=" + ActionType.Start.getValue() + " OR ActionType=" + ActionType.Forward.getValue() + " OR ActionType=" + ActionType.ForwardFL.getValue() + " OR ActionType=" + ActionType.ForwardHL.getValue() + " OR ActionType=" + ActionType.SubThreadForward.getValue() + " OR ActionType=" + ActionType.Skip.getValue() + " )" + " AND NDFrom IN(SELECT FK_Node FROM WF_Generworkerlist WHERE WorkID=" + oid + ")" + " ORDER BY RDT";
		DataTable dt = DBAccess.RunSQLReturnTable(actionSQL);

		String nds = "";
		for (DataRow dr : dt.Rows)
		{
			Node nd = new Node(Integer.parseInt(dr.getValue("NDFrom").toString()));
			Work wk = nd.GetWork(oid);
			if (wk == null)
			{
				wk = nd.getHisWork();
			}

			// 处理重复的问题.
			if (nds.contains(String.valueOf(nd.getNodeID()) + ",") == true)
			{
				continue;
			}
			nds += String.valueOf(nd.getNodeID()) + ",";


			wk.setRec(dr.getValue("EmpFrom").toString());
			//   wk.RecText = dr["EmpFromT"].ToString();
			wk.SetValByKey("RDT", dr.getValue("RDT").toString());
			this.Add(new WorkNode(wk, nd));
		}
		return this.size();
	}
	/** 
	 删除工作流程
	*/
	public final void DeleteWorks() throws Exception {
		for (WorkNode wn : this)
		{
			if (wn.getHisFlow().getHisDataStoreModel() != DataStoreModel.ByCCFlow)
			{
				return;
			}
			wn.getHisWork().Delete();
		}
	}

		///#endregion


		///#region 方法
	/** 
	 增加一个WorkNode
	 
	 param wn 工作 节点
	*/
	public final void Add(WorkNode wn)
	{
		this.add(wn);
	}
	/** 
	 根据位置取得数据
	*/
	public final WorkNode get(int index)
	{
		//return (WorkNode)this.InnerList[index];
		return this.get(index);
	}

		///#endregion
}