package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.wf.*;


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
	public final DeliveryWay getHisDeliveryWay()  {
		return DeliveryWay.forValue(this.GetValIntByKey(NodeAttr.DeliveryWay));
	}
	public final void setHisDeliveryWay(DeliveryWay value)
	 {
		this.SetValByKey(NodeAttr.DeliveryWay, value);
	}
	public final String getDeliveryParas() throws Exception
	{
		return this.GetValStringByKey(NodeAttr.DeliveryParas);
	}
	public final String getName() throws Exception
	{
		return this.GetValStringByKey(NodeAttr.Name);
	}
	public final void setName(String value)  throws Exception
	 {
		this.SetValByKey(NodeAttr.Name, value);
	}
	public final float getX() throws Exception
	{
		return this.GetValFloatByKey(NodeAttr.X);
	}
	public final void setX(float value)  throws Exception
	 {
		this.SetValByKey(NodeAttr.X, value);
	}
	public final String getHisToNDs()
	{
		return this.GetValStrByKey(NodeAttr.HisToNDs);
	}
	public final void setHisToNDs(String value)
	{
		this.SetValByKey(NodeAttr.HisToNDs, value);
	}

	public final boolean getIsResetAccepter()
	{
		return this.GetValBooleanByKey(NodeAttr.IsResetAccepter);
	}

	/** 
	 y
	*/
	public final float getY() throws Exception
	{
		return this.GetValFloatByKey(NodeAttr.Y);
	}
	public final void setY(float value)  throws Exception
	 {
		this.SetValByKey(NodeAttr.Y, value);
	}

		///#endregion 节点属性.


		///#region 构造函数
	/** 
	 节点
	*/
	public NodeSimple()  {
	}
	/** 
	 节点
	 
	 //param _oid 节点ID
	*/
	public NodeSimple(int nodeID) throws Exception {
		this.setNodeID(nodeID);
		this.Retrieve();
	}
	/** 
	 重写基类方法
	*/
	@Override
	public bp.en.Map getEnMap()  {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_Node", "节点");

		map.setDepositaryOfEntity( Depositary.Application);
		map.setDepositaryOfMap( Depositary.Application);


			///#region 基本属性.
		map.AddTBIntPK(NodeAttr.NodeID, 0, "节点ID", true, true);
		map.AddTBString(NodeAttr.Name, null, "名称", true, false, 0, 150, 10);
		map.AddTBString(NodeAttr.FK_Flow, null, "流程编号", true, false, 0, 5, 10);
		map.AddTBInt(NodeAttr.RunModel, 0, "运行模式", true, true);

		map.AddTBInt(NodeAttr.DeliveryWay, 0, "运行模式", true, true);
		map.AddTBString(NodeAttr.DeliveryParas, null, "参数", true, false, 0, 300, 10);

		map.AddTBInt(NodeAttr.Step, 0, "步骤", true, true);
		map.AddBoolean(NodeAttr.IsResetAccepter, false, "可逆节点时重新计算接收人?", true, true, true);

			///#endregion 基本属性.

		map.AddTBInt(NodeAttr.X, 0, "X坐标", false, false);
		map.AddTBInt(NodeAttr.Y, 0, "Y坐标", false, false);

		map.AddTBAtParas(500);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}