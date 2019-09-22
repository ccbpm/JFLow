package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.WF.Template.*;
import BP.WF.*;
import java.util.*;

/** 
 节点方向
 节点的方向有两部分组成.
 1, Node.
 2, toNode.
 记录了从一个节点到其他的多个节点.
 也记录了到这个节点的其他的节点.
*/
public class Direction extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	/** 
	节点
	*/
	public final int getNode()
	{
		return this.GetValIntByKey(DirectionAttr.Node);
	}
	public final void setNode(int value)
	{
		this.SetValByKey(DirectionAttr.Node, value);
	}
	public final String getFK_Flow()
	{
		return this.GetValStringByKey(DirectionAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)
	{
		this.SetValByKey(DirectionAttr.FK_Flow, value);
	}
	/** 
	 转向的节点
	*/
	public final int getToNode()
	{
		return this.GetValIntByKey(DirectionAttr.ToNode);
	}
	public final void setToNode(int value)
	{
		this.SetValByKey(DirectionAttr.ToNode,value);
	}
	public final boolean getIsCanBack()
	{
		return this.GetValBooleanByKey(DirectionAttr.IsCanBack);
	}
	public final void setIsCanBack(boolean value)
	{
		this.SetValByKey(DirectionAttr.IsCanBack, value);
	}
	public final String getDots()
	{
		return this.GetValStringByKey(DirectionAttr.Dots);
	}
	public final void setDots(String value)
	{
		this.SetValByKey(DirectionAttr.Dots, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 节点方向
	*/
	public Direction()
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

		Map map = new Map("WF_Direction", "节点方向信息");

		map.IndexField = DirectionAttr.FK_Flow;

			/*
			 * MyPK 是一个复合主键 是由 Node+'_'+ToNode+'_'+DirType 组合的. 比如: 101_102_1
			 */
		map.AddMyPK();
		map.AddTBString(DirectionAttr.FK_Flow, null, "流程", true, true, 0, 10, 0, false);
		map.AddTBInt(DirectionAttr.Node, 0, "从节点", false, true);
		map.AddTBInt(DirectionAttr.ToNode,0,"到节点",false,true);
		map.AddTBInt(DirectionAttr.IsCanBack, 0, "是否可以原路返回(对后退线有效)", false, true);
			/*
			 * Dots 存储格式为: @x1,y1@x2,y2
			 */
		map.AddTBString(NodeReturnAttr.Dots, null, "轨迹信息", true, true, 0, 300, 0, false);

			//相关功能。
		map.AttrsOfOneVSM.Add(new BP.WF.Template.DirectionStations(), new BP.WF.Port.Stations(), NodeStationAttr.FK_Node, NodeStationAttr.FK_Station, StationAttr.Name, StationAttr.No, "方向条件与岗位");

			//map.AttrsOfOneVSM.Add(new BP.WF.Template.NodeDepts(), new BP.WF.Port.Depts(), NodeDeptAttr.FK_Node, NodeDeptAttr.FK_Dept, DeptAttr.Name,
			//DeptAttr.No, "节点部门", Dot2DotModel.TreeDept);


		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 处理pk 
	 
	 @return 
	*/
	@Override
	protected boolean beforeInsert()
	{
		this.setMyPK( this.getFK_Flow() + "_" + this.getNode() + "_" + this.getToNode();
		return super.beforeInsert();
	}
	@Override
	protected boolean beforeDelete()
	{
		this.setMyPK( this.getFK_Flow() + "_" + this.getNode() + "_" + this.getToNode();
		return super.beforeDelete();
	}
}