package BP.WF.Template;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import BP.Port.*;
import BP.WF.Data.*;
import BP.WF.Template.*;
import BP.WF.Port.*;


/** 
 这里存放每个节点的信息.	 
 
*/
public class NodeSimple extends Entity
{

		///#region 节点属性.
	/** 
	 节点编号
	 
	*/
	public final int getNodeID()
	{
		return this.GetValIntByKey(NodeAttr.NodeID);
	}
	public final void setNodeID(int value)
	{
		this.SetValByKey(NodeAttr.NodeID, value);
	}
	public final String getName()
	{
		return this.GetValStringByKey(NodeAttr.Name);
	}
	public final void setName(String value)
	{
		this.SetValByKey(NodeAttr.Name, value);
	}
	public final float getX()
	{
		return this.GetValFloatByKey(NodeAttr.X);
	}
	public final void setX(float value)
	{
		this.SetValByKey(NodeAttr.X, value);
	}
	/** 
	 y
	 
	*/
	public final float getY()
	{
		return this.GetValFloatByKey(NodeAttr.Y);
	}
	public final void setY(float value)
	{
		this.SetValByKey(NodeAttr.Y, value);
	}
	 
	/** 
	 节点
	 
	*/
	public NodeSimple()
	{
	}
	/** 
	 节点
	 
	 @param _oid 节点ID	
	*/
	public NodeSimple(int _oid)
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

		Map map = new Map("WF_Node", "节点");

		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
 
		map.AddTBIntPK(NodeAttr.NodeID, 0, "节点ID", true, true);
		map.AddTBString(NodeAttr.Name, null, "名称", true, false, 0, 150, 10);
		map.AddTBString(NodeAttr.FK_Flow, null, "流程编号", true, false, 0, 150, 10);
        map.AddTBInt(NodeAttr.RunModel, 0, "运行模式", true, true);
		map.AddTBInt(NodeAttr.Step, 0, "步骤", true, true);
 

		map.AddTBInt(NodeAttr.X, 0, "X坐标", false, false);
		map.AddTBInt(NodeAttr.Y, 0, "Y坐标", false, false);
	 
		this.set_enMap(map);
		return this.get_enMap();
		 
	}
	 
}
 
