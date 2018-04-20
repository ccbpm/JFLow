package BP.WF.Template;

import BP.En.EntityMM;
import BP.En.Map;
import BP.En.UAC;

/** 
 可退回的节点
 节点的退回到有两部分组成.	 
 记录了从一个节点到其他的多个节点.
 也记录了到这个节点的其他的节点.
 
*/
public class NodeReturn extends EntityMM
{

		
	/** 
	 UI界面上的访问控制
	 * @throws Exception 
	 
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	/** 
	退回到
	 
	*/
	public final int getReturnTo()
	{
		return this.GetValIntByKey(NodeReturnAttr.ReturnTo);
	}
	public final void setReturnTo(int value)
	{
		this.SetValByKey(NodeReturnAttr.ReturnTo, value);
	}
	/** 
	 工作流程
	 
	*/
	public final int getFK_Node()
	{
		return this.GetValIntByKey(NodeReturnAttr.FK_Node);
	}
	public final void setFK_Node(int value)
	{
		this.SetValByKey(NodeReturnAttr.FK_Node, value);
	}

		///#endregion


		
	/** 
	 可退回的节点
	 
	*/
	public NodeReturn()
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

		Map map = new Map("WF_NodeReturn", "可退回的节点");

		map.AddTBIntPK(NodeReturnAttr.FK_Node, 0, "节点", true, true);
		map.AddTBIntPK(NodeReturnAttr.ReturnTo, 0, "退回到", true, true);
		map.AddTBString(NodeReturnAttr.Dots, null, "轨迹信息", true, true,0,300,0,false);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}