package BP.WF;

import java.util.ArrayList;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.WF.Template.DataStoreModel;

/** 
 工作节点集合.
 
*/
public class WorkNodes extends ArrayList<WorkNode>
{

		
	/** 
	 他的工作s
	  
	*/
	public final Works getGetWorks()
	{
		if (this.size() == 0)
		{
			throw new RuntimeException("@初始化失败，没有找到任何节点。");
		}

		Works ens = this.getItem(0).getHisNode().getHisWorks();
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
	public WorkNodes()
	{
	}

	public final int GenerByFID(Flow flow, long fid)
	{
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
	/** 
	 这个方法有问题的
	 
	 @param flow
	 @param oid
	 @return 
	*/
	public final int GenerByWorkID2014_01_06(Flow flow, long oid)
	{
		Nodes nds = flow.getHisNodes();
		for (Node nd : nds.ToJavaList())
		{
			Work wk = nd.GetWork(oid);
			if (wk == null)
			{
				continue;
			}
			String table = "ND" + Integer.parseInt(flow.getNo()) + "Track";
			String actionSQL = "SELECT EmpFrom,EmpFromT,RDT FROM " + table + " WHERE WorkID=" + oid + " AND NDFrom=" + nd.getNodeID() + " AND ActionType=" + ActionType.Forward.getValue();
			DataTable dt = DBAccess.RunSQLReturnTable(actionSQL);
			if (dt.Rows.size() == 0)
			{
				continue;
			}

			wk.setRec(dt.Rows.get(0).getValue("EmpFrom").toString());
			wk.setRecText(dt.Rows.get(0).getValue("EmpFromT").toString());
			wk.SetValByKey("RDT", dt.Rows.get(0).getValue("RDT").toString());
			this.Add(new WorkNode(wk, nd));
		}
		return this.size();
	}
	public final int GenerByWorkID(Flow flow, long oid)
	{
		String table = "ND" + Integer.parseInt(flow.getNo()) + "Track";
		String actionSQL = "SELECT EmpFrom,EmpFromT,RDT,NDFrom FROM " + table + " WHERE WorkID=" + oid + " AND (ActionType=" + ActionType.Forward.getValue() + " OR ActionType=" + ActionType.ForwardFL.getValue() + " OR ActionType=" + ActionType.ForwardHL.getValue() + " OR ActionType=" + ActionType.SubFlowForward.getValue() + " ) ORDER BY RDT";
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
			if (nds.contains((new Integer(nd.getNodeID())).toString() + ",") == true)
			{
				continue;
			}
			nds += (new Integer(nd.getNodeID())).toString() + ",";

			wk.setRec(dr.getValue("EmpFrom").toString());
			wk.setRecText(dr.getValue("EmpFromT").toString());
			wk.SetValByKey("RDT", dr.getValue("RDT").toString());
			this.Add(new WorkNode(wk, nd));
		}
		return this.size();
	}
	/** 
	 删除工作流程
	 
	*/
	public final void DeleteWorks()
	{
		for (WorkNode wn : this)
		{
			if (wn.getHisFlow().getHisDataStoreModel() != DataStoreModel.ByCCFlow)
			{
				return;
			}
			wn.getHisWork().Delete();
		}
	}

	/** 
	 增加一个WorkNode
	 
	 @param wn 工作 节点
	*/
	public final void Add(WorkNode wn)
	{
		this.add(wn);
	}
	/** 
	 根据位置取得数据
	 
	*/
	public final WorkNode getItem(int index)
	{
		return (WorkNode)this.get(index);
	}
}