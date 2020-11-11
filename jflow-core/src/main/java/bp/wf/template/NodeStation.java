package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.wf.port.*;
import bp.wf.*;
import java.util.*;

/** 
 节点工作岗位
*/
public class NodeStation extends EntityMM
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
		return this.GetValIntByKey(NodeStationAttr.FK_Node);
	}
	public final void setFK_Node(int value) throws Exception
	{
		this.SetValByKey(NodeStationAttr.FK_Node, value);
	}
	public final String getFK_StationT() throws Exception
	{
		return this.GetValRefTextByKey(NodeStationAttr.FK_Station);
	}
	/** 
	 工作岗位
	 * @throws Exception 
	*/
	public final String getFK_Station() throws Exception
	{
		return this.GetValStringByKey(NodeStationAttr.FK_Station);
	}
	public final void setFK_Station(String value) throws Exception
	{
		this.SetValByKey(NodeStationAttr.FK_Station, value);
	}

		///


		///构造方法
	/** 
	 节点工作岗位
	*/
	public NodeStation()
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

		Map map = new Map("WF_NodeStation", "节点岗位");

		map.AddTBIntPK(NodeStationAttr.FK_Node, 0, "节点", false, false);

			// #warning ,这里为了方便用户选择，让分组都统一采用了枚举类型. edit zhoupeng. 2015.04.28. 注意jflow也要修改.
		map.AddDDLEntitiesPK(NodeStationAttr.FK_Station, null, "工作岗位", new bp.port.Stations(), true);

		this.set_enMap(map);
		return this.get_enMap();
	}

	/** 
	 节点岗位发生变化，删除该节点记忆的接收人员。
	 
	 @return 
	 * @throws Exception 
	*/
	@Override
	protected boolean beforeInsert() throws Exception
	{
		RememberMe remeberMe = new RememberMe();
		remeberMe.Delete(RememberMeAttr.FK_Node, this.getFK_Node());
		return super.beforeInsert();
	}

		///
}