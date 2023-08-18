package bp.wf.httphandler;

import bp.da.*;
import bp.difference.*;
import bp.sys.*;
import bp.wf.template.*;
import bp.*;
import bp.wf.*;

import java.net.URLDecoder;
import java.util.*;

/** 
 初始化函数
*/
public class WF_Admin_CCBPMDesigner2018 extends bp.difference.handler.DirectoryPageBase
{
	/** 
	 构造函数
	*/
	public WF_Admin_CCBPMDesigner2018()
	{
	}


		///#region 节点相关 Nodes
	public final String CreateCCNode() throws Exception {
		try
		{
			String x = this.GetRequestVal("X");
			String y = this.GetRequestVal("Y");
			String icon = this.GetRequestVal("icon");

			int iX = 20;
			int iY = 20;

			if (DataType.IsNullOrEmpty(x) == false)
			{
				iX = (int)Double.parseDouble(x);
			}

			if (DataType.IsNullOrEmpty(y) == false)
			{
				iY = (int)Double.parseDouble(y);
			}

			Node node = TemplateGlo.NewEtcNode(this.getFlowNo(), iX, iY, NodeType.CCNode);

			Hashtable ht = new Hashtable();
			ht.put("NodeID", node.getNodeID());
			ht.put("Name", node.getName());
			ht.put("NodeType", 2); // 抄送节点.

			return bp.tools.Json.ToJsonEntityModel(ht);
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	public final String CreateSubFlowNode() throws Exception {
		try
		{
			String x = this.GetRequestVal("X");
			String y = this.GetRequestVal("Y");
			String icon = this.GetRequestVal("icon");

			int iX = 20;
			int iY = 20;

			if (DataType.IsNullOrEmpty(x) == false)
			{
				iX = (int)Double.parseDouble(x);
			}

			if (DataType.IsNullOrEmpty(y) == false)
			{
				iY = (int)Double.parseDouble(y);
			}

			Node node = TemplateGlo.NewEtcNode(this.getFlowNo(), iX, iY, NodeType.SubFlowNode);

			Hashtable ht = new Hashtable();
			ht.put("NodeID", node.getNodeID());
			ht.put("Name", node.getName());
			ht.put("NodeType", 3); // 子流程节点.

			return bp.tools.Json.ToJsonEntityModel(ht);
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 创建流程节点并返回编号
	 
	 @return 
	*/
	public final String CreateNode() throws Exception {
		try
		{
			String x = this.GetRequestVal("X");
			String y = this.GetRequestVal("Y");
			String icon = this.GetRequestVal("icon");
			int nodeModel = this.GetRequestValInt("NodeModel");

			int iX = 20;
			int iY = 20;

			if (DataType.IsNullOrEmpty(x) == false)
			{
				iX = (int)Double.parseDouble(x);
			}

			if (DataType.IsNullOrEmpty(y) == false)
			{
				iY = (int)Double.parseDouble(y);
			}

			Node node = TemplateGlo.NewNode(this.getFlowNo(), iX, iY, icon, nodeModel);


			Hashtable ht = new Hashtable();
			ht.put("NodeID", node.getNodeID());
			ht.put("Name", node.getName());
			ht.put("RunModel", node.getHisRunModel().getValue());
			ht.put("NodeType", 0); //用户节点。

			return bp.tools.Json.ToJsonEntityModel(ht);
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 创建条件
	 
	 @return 
	*/
	public final String CreateCond() throws Exception {
		try
		{
			String x = this.GetRequestVal("X");
			String y = this.GetRequestVal("Y");
			String icon = this.GetRequestVal("icon");

			int iX = 20;
			int iY = 20;

			if (DataType.IsNullOrEmpty(x) == false)
			{
				iX = (int)Double.parseDouble(x);
			}

			if (DataType.IsNullOrEmpty(y) == false)
			{
				iY = (int)Double.parseDouble(y);
			}

			Node node = TemplateGlo.NewEtcNode(this.getFlowNo(), iX, iY, NodeType.RouteNode);

			Hashtable ht = new Hashtable();
			ht.put("NodeID", node.getNodeID());
			ht.put("Name", node.getName());
			ht.put("NodeType", 1);

			return bp.tools.Json.ToJsonEntityModel(ht);
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 根据节点编号删除流程节点
	 
	 @return 执行结果
	*/
	public final String DeleteNode() throws Exception {
		try
		{
			Node node = new Node();
			node.setNodeID(this.getNodeID());
			if (node.RetrieveFromDBSources() == 0)
			{
				return "err@删除失败,没有删除到数据，估计该节点已经别删除了.";
			}

			if (node.getItIsStartNode() == true)
			{
				return "err@开始节点不允许被删除。";
			}

			node.Delete();
			return "删除成功.";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}

	public final String EditNodePosition()
	{
		try
		{
			String FK_Node = this.GetValFromFrmByKey("NodeID");
			String x = this.GetValFromFrmByKey("X");
			String y = this.GetValFromFrmByKey("Y");
			Node node = new Node();
			node.setNodeID(Integer.parseInt(FK_Node));
			int left = DataType.IsNullOrEmpty(x) ? 20 : Integer.parseInt(x);
			int top = DataType.IsNullOrEmpty(x) ? 20 : Integer.parseInt(y);
			if (left <= 0)
			{
				left = 20;
			}
			if (top <= 0)
			{
				top = 20;
			}

			int iResult = node.RetrieveFromDBSources();
			if (iResult > 0)
			{
				node.setX(left);
				node.setY(top);
				node.Update();
				return "修改成功.";
			}

			return "err@修改节点失败，请确认该节点是否存在？";
		}
		catch (Exception ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 修改节点名称
	 
	 @return 
	*/
	public final String Node_EditNodeName() throws Exception {
		String FK_Node = this.GetValFromFrmByKey("NodeID");
		String NodeName = URLDecoder.decode(this.GetValFromFrmByKey("NodeName"),"UTF-8");

		Node node = new Node();
		node.setNodeID(Integer.parseInt(FK_Node));
		int iResult = node.RetrieveFromDBSources();
		if (iResult > 0)
		{
			node.setName(NodeName);
			node.Update();
			return "@修改成功.";
		}

		return "err@修改节点失败，请确认该节点是否存在？";
	}

		///#endregion end Node

	/** 
	 删除连接线
	 
	 @return 
	*/
	public final String Direction_Delete()
	{
		try
		{
			Directions di = new Directions();
			di.Retrieve(DirectionAttr.FK_Flow, this.getFlowNo(), DirectionAttr.Node, this.getNodeID(), DirectionAttr.ToNode, this.GetValFromFrmByKey("ToNode"), null);
			for (Direction direct : di.ToJavaList())
			{
				direct.Delete();
			}
			return "@删除成功！";
		}
		catch (Exception ex)
		{
			return "@err:" + ex.getMessage();
		}
	}
	public final String Direction_Init()
	{
		try
		{
			String pk = this.getFlowNo() + "_" + this.getNodeID() + "_" + this.GetValFromFrmByKey("ToNode");

			Direction dir = new Direction();
			dir.setMyPK(pk);

			if (dir.RetrieveFromDBSources() > 0)
			{
				return dir.getDes();
			}

			return "";
		}
		catch (Exception ex)
		{
			return "@err:" + ex.getMessage();
		}
	}
	/** 
	 
	 
	 @return 
	*/
	public final String Direction_Save()
	{
		try
		{
			String pk = this.getFlowNo() + "_" + this.getNodeID() + "_" + this.GetValFromFrmByKey("ToNode");

			Direction dir = new Direction();
			dir.setMyPK(pk);

			if (dir.RetrieveFromDBSources() > 0)
			{
				dir.setDes(this.GetValFromFrmByKey("Des"));
				dir.DirectUpdate();
			}
			return "@保存成功！";
		}
		catch (Exception ex)
		{
			return "@err:" + ex.getMessage();
		}
	}
	/** 
	 添加标签
	 
	 @return 
	*/
	public final String CreatLabNote()
	{
		try
		{
			LabNote lb = new LabNote();

			//获取当前流程已经存在的数量
			LabNotes labNotes = new LabNotes();
			int num = labNotes.Retrieve(LabNoteAttr.FK_Flow, this.getFlowNo(), null);

			String Name = this.GetValFromFrmByKey("LabName");
			int x = Integer.parseInt(this.GetValFromFrmByKey("X"));
			int y = Integer.parseInt(this.GetValFromFrmByKey("Y"));

			lb.setMyPK(this.getFlowNo() + "_" + x + "_" + y + "_" + (num + 1));
			lb.setName(Name);
			lb.setFlowNo(this.getFlowNo());
			lb.setX(x);
			lb.setY(y);

			lb.DirectInsert();

			Hashtable ht = new Hashtable();
			ht.put("MyPK", this.getFlowNo() + "_" + x + "_" + y + "_" + (num + 1));
			ht.put("FK_Flow", this.getFlowNo());

			return bp.tools.Json.ToJsonEntityModel(ht);
		}
		catch (Exception ex)
		{
			return "@err:" + ex.getMessage();
		}
	}

	public final void CheckBillFrm() throws Exception {
		GEEntity en = new GEEntity(this.getEnsName());
		en.CheckPhysicsTable();
	}
}
