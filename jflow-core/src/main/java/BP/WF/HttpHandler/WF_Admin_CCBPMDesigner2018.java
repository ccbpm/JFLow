package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Sys.*;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Template.*;
import LitJson.*;
import BP.WF.XML.*;
import BP.WF.*;
import java.util.*;

/** 
 初始化函数
*/
public class WF_Admin_CCBPMDesigner2018 extends DirectoryPageBase
{
	   /** 
	 构造函数
	   */
	public WF_Admin_CCBPMDesigner2018()
	{
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 执行父类的重写方法.
	/** 
	 默认执行的方法
	 
	 @return 
	*/
	@Override
	protected String DoDefaultMethod()
	{

		return "err@没有判断的标记:" + this.getDoType();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 执行父类的重写方法.




//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 节点相关 Nodes
	/** 
	 创建流程节点并返回编号
	 
	 @return 
	*/
	public final String CreateNode()
	{
		try
		{
			String FK_Flow = this.GetRequestVal("FK_Flow");
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

			int nodeId = BP.WF.Template.TemplateGlo.NewNode(FK_Flow, iX, iY, icon);

			BP.WF.Node node = new BP.WF.Node(nodeId);
			node.Update();

			Hashtable ht = new Hashtable();
			ht.put("NodeID", node.getNodeID());
			ht.put("Name", node.getName());

			return BP.Tools.Json.ToJsonEntityModel(ht);
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
	public final String DeleteNode()
	{
		try
		{
			BP.WF.Node node = new BP.WF.Node();
			node.setNodeID(this.getFK_Node());
			if (node.RetrieveFromDBSources() == 0)
			{
				return "err@删除失败,没有删除到数据，估计该节点已经别删除了.";
			}

			if (node.getIsStartNode() == true)
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
	public final String Node_EditNodeName()
	{
		String FK_Node = this.GetValFromFrmByKey("NodeID");
		String NodeName = HttpContextHelper.UrlDecode(this.GetValFromFrmByKey("NodeName"));

		BP.WF.Node node = new BP.WF.Node();
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
	public final String Node_ChangeRunModel()
	{
		String runModel = GetValFromFrmByKey("RunModel");
		BP.WF.Node node = new BP.WF.Node(this.getFK_Node());
		//节点运行模式
		switch (runModel)
		{
			case "NodeOrdinary":
				node.setHisRunModel(BP.WF.RunModel.Ordinary);
				break;
			case "NodeFL":
				node.setHisRunModel(BP.WF.RunModel.FL);
				break;
			case "NodeHL":
				node.setHisRunModel(BP.WF.RunModel.HL);
				break;
			case "NodeFHL":
				node.setHisRunModel(BP.WF.RunModel.FHL);
				break;
			case "NodeSubThread":
				node.setHisRunModel(BP.WF.RunModel.SubThread);
				break;
		}
		node.Update();

		return "设置成功.";
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
			di.Retrieve(DirectionAttr.FK_Flow, this.getFK_Flow(), DirectionAttr.Node, this.getFK_Node(), DirectionAttr.ToNode, this.GetValFromFrmByKey("ToNode"));
			for (Direction direct : di)
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
			int num = labNotes.Retrieve(LabNoteAttr.FK_Flow, this.getFK_Flow());

			String Name = this.GetValFromFrmByKey("LabName");
			int x = Integer.parseInt(this.GetValFromFrmByKey("X"));
			int y = Integer.parseInt(this.GetValFromFrmByKey("Y"));

			lb.setMyPK( this.getFK_Flow() + "_" + x + "_" + y + "_" + (num + 1);
			lb.setName(Name);
			lb.setFK_Flow(this.getFK_Flow());
			lb.setX(x);
			lb.setY(y);

			lb.DirectInsert();

			Hashtable ht = new Hashtable();
			ht.put("MyPK", this.getFK_Flow() + "_" + x + "_" + y + "_" + (num + 1));
			ht.put("FK_Flow", this.getFK_Flow());

			return BP.Tools.Json.ToJsonEntityModel(ht);
		}
		catch (RuntimeException ex)
		{
			return "@err:" + ex.getMessage();
		}
	}

	public final void CheckBillFrm()
	{
		GEEntity en = new GEEntity(this.getEnsName());
		en.CheckPhysicsTable();
	}
}