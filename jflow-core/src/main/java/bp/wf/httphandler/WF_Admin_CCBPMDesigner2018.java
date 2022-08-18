package bp.wf.httphandler;

import bp.da.*;
import bp.difference.*;
import bp.difference.handler.WebContralBase;
import bp.sys.*;
import bp.wf.template.*;
import bp.*;
import bp.wf.*;

import java.net.URLDecoder;
import java.util.*;

/** 
 初始化函数
*/
public class WF_Admin_CCBPMDesigner2018 extends WebContralBase
{
	   /** 
	 构造函数
	   */
	public WF_Admin_CCBPMDesigner2018() throws Exception {
	}


		///#region 节点相关 Nodes
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

			Node node = TemplateGlo.NewNode(this.getFK_Flow(), iX, iY, icon);

			Hashtable ht = new Hashtable();
			ht.put("NodeID", node.getNodeID());
			ht.put("Name", node.getName());



				///#region //2019.11.08 增加如果是极简版, 就设置初始化参数.
			Flow fl = new Flow(this.getFK_Flow());
			if (fl.getFlowFrmModel() != FlowFrmModel.Ver2019Earlier)
			{
				FrmNode fm = new FrmNode();
				fm.setFK_Flow(this.getFK_Flow());
				fm.setFKFrm("ND" + Integer.parseInt(this.getFK_Flow() + "01"));
				if (fl.getFlowDevModel() == FlowDevModel.JiJian)
				{
					fm.setEnableFWC(FrmWorkCheckSta.Enable);
				}
				fm.setFK_Node(node.getNodeID());
				fm.setFrmSln(FrmSln.Readonly);
				fm.Insert();
			}

				///#endregion //2019.11.08 增加如果是极简版.


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
			node.setNodeID(this.getFK_Node());
			if (node.RetrieveFromDBSources() == 0)
			{
				return "err@删除失败,没有删除到数据，估计该节点已经别删除了.";
			}

			if (node.isStartNode() == true)
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
	/** 
	 修改节点名称
	 
	 @return 
	*/
	public final String Node_EditNodeName() throws Exception {
		String FK_Node = this.GetValFromFrmByKey("NodeID");
		String NodeName = URLDecoder.decode(this.GetValFromFrmByKey("NodeName"), "UTF-8");

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
	/** 
	 修改节点运行模式
	 
	 @return 
	*/
	public final String Node_ChangeRunModel() throws Exception {
		String runModel = GetValFromFrmByKey("RunModel");
		Node node = new Node(this.getFK_Node());
		//节点运行模式
		switch (runModel)
		{
			case "NodeOrdinary":
				node.setHisRunModel(RunModel.Ordinary);
				break;
			case "NodeFL":
				node.setHisRunModel(RunModel.FL);
				break;
			case "NodeHL":
				node.setHisRunModel(RunModel.HL);
				break;
			case "NodeFHL":
				node.setHisRunModel(RunModel.FHL);
				break;
			case "NodeSubThread":
				node.setHisRunModel(RunModel.SubThread);
				break;
		}
		node.Update();

		return "设置成功.";
	}

		///#endregion end Node

	/** 
	 删除连接线
	 
	 @return 
	*/
	public final String Direction_Delete() throws Exception {
		try
		{
			Directions di = new Directions();
			di.Retrieve(DirectionAttr.FK_Flow, this.getFK_Flow(), DirectionAttr.Node, this.getFK_Node(), DirectionAttr.ToNode, this.GetValFromFrmByKey("ToNode"), null);
			for (Direction direct : di.ToJavaList())
			{
				direct.Delete();
			}
			return "@删除成功！";
		}
		catch (RuntimeException ex)
		{
			return "@err:" + ex.getMessage();
		}
	}
	public final String Direction_Init() throws Exception {
		try
		{
			String pk = this.getFK_Flow() + "_" + this.getFK_Node() + "_" + this.GetValFromFrmByKey("ToNode");

			Direction dir = new Direction();
			dir.setMyPK(pk);

			if (dir.RetrieveFromDBSources() > 0)
			{
				return dir.getDes();
			}

			return "";
		}
		catch (RuntimeException ex)
		{
			return "@err:" + ex.getMessage();
		}
	}
	/** 
	 
	 
	 @return 
	*/
	public final String Direction_Save() throws Exception {
		try
		{
			String pk = this.getFK_Flow() + "_" + this.getFK_Node() + "_" + this.GetValFromFrmByKey("ToNode");

			Direction dir = new Direction();
			dir.setMyPK(pk);

			if (dir.RetrieveFromDBSources() > 0)
			{
				dir.setDes(this.GetValFromFrmByKey("Des"));
				dir.DirectUpdate();
			}

			return "@保存成功！";
		}
		catch (RuntimeException ex)
		{
			return "@err:" + ex.getMessage();
		}
	}
	/** 
	 添加标签
	 
	 @return 
	*/
	public final String CreatLabNote() throws Exception {
		try
		{
			LabNote lb = new LabNote();

			//获取当前流程已经存在的数量
			LabNotes labNotes = new LabNotes();
			int num = labNotes.Retrieve(LabNoteAttr.FK_Flow, this.getFK_Flow(), null);

			String Name = this.GetValFromFrmByKey("LabName");
			int x = Integer.parseInt(this.GetValFromFrmByKey("X"));
			int y = Integer.parseInt(this.GetValFromFrmByKey("Y"));

			lb.setMyPK(this.getFK_Flow() + "_" + x + "_" + y + "_" + (num + 1));
			lb.setName(Name);
			lb.setFK_Flow(this.getFK_Flow());
			lb.setX(x);
			lb.setY(y);

			lb.DirectInsert();

			Hashtable ht = new Hashtable();
			ht.put("MyPK", this.getFK_Flow() + "_" + x + "_" + y + "_" + (num + 1));
			ht.put("FK_Flow", this.getFK_Flow());

			return bp.tools.Json.ToJsonEntityModel(ht);
		}
		catch (RuntimeException ex)
		{
			return "@err:" + ex.getMessage();
		}
	}

	public final void CheckBillFrm() throws Exception {
		GEEntity en = new GEEntity(this.getEnsName());
		en.CheckPhysicsTable();
	}
}