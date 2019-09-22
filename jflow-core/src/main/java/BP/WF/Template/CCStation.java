package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 抄送到岗位
 节点的工作岗位有两部分组成.	 
 记录了从一个节点到其他的多个节点.
 也记录了到这个节点的其他的节点.
*/
public class CCStation extends EntityMM
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenAll();
		return uac;
	}
	/** 
	节点
	*/
	public final int getFK_Node()
	{
		return this.GetValIntByKey(CCStationAttr.FK_Node);
	}
	public final void setFK_Node(int value)
	{
		this.SetValByKey(CCStationAttr.FK_Node, value);
	}
	/** 
	 岗位名称
	*/
	public final String getFK_StationT()
	{
		return this.GetValRefTextByKey(CCStationAttr.FK_Station);
	}
	/** 
	 工作岗位
	*/
	public final String getFK_Station()
	{
		return this.GetValStringByKey(CCStationAttr.FK_Station);
	}
	public final void setFK_Station(String value)
	{
		this.SetValByKey(CCStationAttr.FK_Station, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 抄送到岗位
	*/
	public CCStation()
	{
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap()
	{
		if (this._enMap != null)
		{
			return this._enMap;
		}

		Map map = new Map("WF_CCStation", "抄送岗位");

		map.AddDDLEntitiesPK(CCStationAttr.FK_Node, 0, DataType.AppInt, "节点", new Nodes(), NodeAttr.NodeID, NodeAttr.Name, true);
		map.AddDDLEntitiesPK(CCStationAttr.FK_Station, null, "工作岗位", new Stations(), true);

		this._enMap = map;

		return this._enMap;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}