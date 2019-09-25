package BP.WF.Template;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import BP.En.Map;
import BP.Port.*;
import BP.WF.Data.*;
import BP.WF.Template.*;
import BP.WF.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 这里存放每个节点的信息.	 
*/
public class NodeSimple extends Entity
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 节点属性.
	/** 
	 节点编号
	 * @throws Exception 
	*/
	public final int getNodeID() throws Exception
	{
		return this.GetValIntByKey(NodeAttr.NodeID);
	}
	public final void setNodeID(int value) throws Exception
	{
		this.SetValByKey(NodeAttr.NodeID, value);
	}
	public final String getName() throws Exception
	{
		return this.GetValStringByKey(NodeAttr.Name);
	}
	public final void setName(String value) throws Exception
	{
		this.SetValByKey(NodeAttr.Name, value);
	}
	public final float getX() throws Exception
	{
		return this.GetValFloatByKey(NodeAttr.X);
	}
	public final void setX(float value) throws Exception
	{
		this.SetValByKey(NodeAttr.X, value);
	}
	/** 
	 y
	 * @throws Exception 
	*/
	public final float getY() throws Exception
	{
		return this.GetValFloatByKey(NodeAttr.Y);
	}
	public final void setY(float value) throws Exception
	{
		this.SetValByKey(NodeAttr.Y, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 节点属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
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

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 基本属性.
		map.AddTBIntPK(NodeAttr.NodeID, 0, "节点ID", true, true);
		map.AddTBString(NodeAttr.Name, null, "名称", true, false, 0, 150, 10);
		map.AddTBString(NodeAttr.FK_Flow, null, "流程编号", true, false, 0, 150, 10);
		map.AddTBInt(NodeAttr.RunModel, 0, "运行模式", true, true);
		map.AddTBInt(NodeAttr.Step, 0, "步骤", true, true);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 基本属性.

		map.AddTBInt(NodeAttr.X, 0, "X坐标", false, false);
		map.AddTBInt(NodeAttr.Y, 0, "Y坐标", false, false);

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}