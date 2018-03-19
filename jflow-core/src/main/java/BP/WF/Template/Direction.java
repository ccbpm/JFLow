package BP.WF.Template;

import BP.En.EntityMyPK;
import BP.En.Map;

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
	public final int getDirType()
	{
		return this.GetValIntByKey(DirectionAttr.DirType);
	}
	public final void setDirType(int value)
	{
		this.SetValByKey(DirectionAttr.DirType, value);
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

		///#endregion


		
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
		if (this.get_enMap()!=null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_Direction", "节点方向信息");

//                
//                 * MyPK 是一个复合主键 是由 Node+'_'+ToNode+'_'+DirType 组合的. 比如: 101_102_1
//                 
		map.AddMyPK();
		map.AddTBString(DirectionAttr.FK_Flow, null, "流程", true, true, 0, 10, 0, false);
		map.AddTBInt(DirectionAttr.Node, 0, "从节点", false, true);
		map.AddTBInt(DirectionAttr.ToNode,0,"到节点",false,true);
		map.AddTBInt(DirectionAttr.DirType, 0, "类型0前进1返回", false, true);
		map.AddTBInt(DirectionAttr.IsCanBack, 0, "是否可以原路返回(对后退线有效)", false, true);
//                
//                 * Dots 存储格式为: @x1,y1@x2,y2
//                 
		map.AddTBString(NodeReturnAttr.Dots, null, "轨迹信息", true, true, 0, 300, 0, false);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	/** 
	 处理pk 
	 
	 @return 
	*/
	@Override
	protected boolean beforeInsert()
	{
		this.setMyPK(this.getFK_Flow()+"_" +this.getNode() + "_" + this.getToNode() + "_" + this.getDirType());
		return super.beforeInsert();
	}
	@Override
	protected boolean beforeDelete()
	{
		this.setMyPK(this.getFK_Flow() + "_" + this.getNode() + "_" + this.getToNode() + "_" + this.getDirType());
		return super.beforeDelete();
	}
}