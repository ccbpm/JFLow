package BP.WF;

import BP.En.Map;
import BP.Port.Station;
import BP.Port.Stations;
import BP.WF.Template.NodeAttr;
import BP.WF.Template.NodeEmpAttr;
import BP.WF.Template.NodeEmps;
import BP.WF.Template.NodeStation;
import BP.WF.Template.NodeStations;
import BP.Web.WebUser;

/** 
 取回任务
*/
public class GetTask extends BP.En.Entity
{
	/** 
	 我可以处理当前的工作吗？
	 @return 
	*/
	public final boolean Can_I_Do_It()
	{
		// 判断我是否可以处理当前点数据？ 
		switch (this.getHisDeliveryWay())
		{
			case ByPreviousNodeFormEmpsField:
				NodeEmps ndemps = new NodeEmps(this.getNodeID());
				if (ndemps.Contains(NodeEmpAttr.FK_Emp, WebUser.getNo()) == false)
				{
					return false;
				}
				else
				{
					return true;
				}
			case ByStation:
				Stations sts = WebUser.getHisStations();
				String myStaStrs = "@";
				for (Station st : sts.ToJavaList())
				{
					myStaStrs += "@" + st.getNo();
				}
				myStaStrs = myStaStrs + "@";

				NodeStations ndeStas = new NodeStations(this.getNodeID());
				boolean isHave = false;
				for (NodeStation ndS : ndeStas.ToJavaList())
				{
					if (myStaStrs.contains("@" + ndS.getFK_Station() + "@") == true)
					{
						isHave = true;
						break;
					}
				}
				if (isHave == false)
				{
					return false;
				}
				return true;
			default: // 其它的情况则不与判断。
				// jc.Delete(); // 设置是非法的，直接删除。
				return false;
		}
	}
	/** 
	 投递方式
	*/
	public final DeliveryWay getHisDeliveryWay()
	{
		return DeliveryWay.forValue(this.GetValIntByKey(NodeAttr.DeliveryWay));
	}
	/** 
	 节点ID
	*/
	public final int getNodeID()
	{
		return this.GetValIntByKey(NodeAttr.NodeID);
	}
	public final void setNodeID(int value)
	{
		this.SetValByKey(NodeAttr.NodeID, value);
	}
	/** 
	 名称
	*/
	public final String getName()
	{
		return this.GetValStringByKey(NodeAttr.Name);
	}
	public final void setName(String value)
	{
		this.SetValByKey(NodeAttr.Name, value);
	}
	/** 
	 流程编号
	*/
	public final String getFK_Flow()
	{
		return this.GetValStringByKey(GetTaskAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)
	{
		this.SetValByKey(GetTaskAttr.FK_Flow, value);
	}
	/** 
	 步骤
	*/
	public final int getStep()
	{
		return this.GetValIntByKey(NodeAttr.Step);
	}
	public final String getCheckNodes()
	{
		String s= this.GetValStringByKey(GetTaskAttr.CheckNodes);
		s = s.replace("~", "'");
		return s;
	}
	public final void setCheckNodes(String value)
	{
		this.SetValByKey(GetTaskAttr.CheckNodes, value);
	}
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_Node", "取回任务");

		map.AddTBIntPK(NodeAttr.NodeID, 0,"NodeID", true, true);
		map.AddTBString(NodeAttr.Name, null,"节点名称", true, false, 0, 100, 10);
		map.AddTBInt(NodeAttr.Step,0, "步骤", true, false);
		map.AddTBString(NodeAttr.FK_Flow, null, "流程编号", true, false, 0, 10, 10);
		map.AddTBString(GetTaskAttr.CheckNodes, null, "工作节点s", true, false, 0, 800, 100);

		map.AddTBInt(NodeAttr.DeliveryWay, 0, "访问规则", true, true);
		this.set_enMap(map);
		return this.get_enMap();
	}
	/** 
	 取回任务
	*/
	public GetTask()
	{
	}
	public GetTask(int nodeId)
	{
		this.setNodeID(nodeId);
		this.Retrieve();
	}
}