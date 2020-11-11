package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.wf.port.*;
import bp.port.*;
import bp.wf.*;
import java.util.*;

/** 
 抄送到岗位
 节点的工作岗位有两部分组成.	 
 记录了从一个节点到其他的多个节点.
 也记录了到这个节点的其他的节点.
*/
public class CCStation extends EntityMM
{

		///基本属性
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenAll();
		return uac;
	}
	/** 
	节点
	 * @throws Exception 
	*/
	public final int getFK_Node() throws Exception
	{
		return this.GetValIntByKey(CCStationAttr.FK_Node);
	}
	public final void setFK_Node(int value) throws Exception
	{
		this.SetValByKey(CCStationAttr.FK_Node, value);
	}
	/** 
	 岗位名称
	 * @throws Exception 
	*/
	public final String getFK_StationT() throws Exception
	{
		return this.GetValRefTextByKey(CCStationAttr.FK_Station);
	}
	/** 
	 工作岗位
	 * @throws Exception 
	*/
	public final String getFK_Station() throws Exception
	{
		return this.GetValStringByKey(CCStationAttr.FK_Station);
	}
	public final void setFK_Station(String value) throws Exception
	{
		this.SetValByKey(CCStationAttr.FK_Station, value);
	}

		///


		///构造方法
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
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_CCStation", "抄送岗位");

		map.AddDDLEntitiesPK(CCStationAttr.FK_Node, 0, DataType.AppInt, "节点", new Nodes(), NodeAttr.NodeID, NodeAttr.Name, true);
		map.AddDDLEntitiesPK(CCStationAttr.FK_Station, null, "工作岗位", new Stations(), true);

		this.set_enMap(map);

		return this.get_enMap();
	}

		///
}