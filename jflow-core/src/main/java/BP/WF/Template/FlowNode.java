package BP.WF.Template;

import BP.En.EntityMM;
import BP.En.Map;
import BP.En.UAC;

/** 
 流程节点
 
*/
public class FlowNode extends EntityMM
{

		
	/** 
	 UI界面上的访问控制
	 
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	/** 
	节点
	 
	*/
	public final int getFK_Node()
	{
		return this.GetValIntByKey(FlowNodeAttr.FK_Node);
	}
	public final void setFK_Node(int value)
	{
		this.SetValByKey(FlowNodeAttr.FK_Node, value);
	}
	/** 
	 工作流程
	 
	*/
	public final String getFK_Flow()
	{
		return this.GetValStringByKey(FlowNodeAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)
	{
		this.SetValByKey(FlowNodeAttr.FK_Flow, value);
	}

		///#endregion


		
	/** 
	 流程岗位属性
	 
	*/
	public FlowNode()
	{
	}
	/** 
	 重写基类方法
	 
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_FlowNode", "流程抄送节点");


		map.AddTBStringPK(FlowNodeAttr.FK_Flow, null, "流程编号", true, true, 1, 20, 20);
		map.AddTBStringPK(FlowNodeAttr.FK_Node, null, "节点", true, true, 1, 20, 20);

			//      map.AddDDLEntitiesPK(FlowNodeAttr.FK_Flow, null, "FK_Flow", new Flows(), true);
			//     map.AddDDLEntitiesPK(FlowNodeAttr.FK_Node, null, "工作节点", new Nodes(), true);
		this.set_enMap(map);


		return this.get_enMap();
	}

		///#endregion
}