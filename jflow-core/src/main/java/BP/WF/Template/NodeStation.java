package BP.WF.Template;

import BP.En.EntityMM;
import BP.En.Map;
import BP.En.UAC;
import BP.WF.RememberMe;
import BP.WF.RememberMeAttr;

/** 
 节点工作岗位
 节点的工作岗位有两部分组成.	 
 记录了从一个节点到其他的多个节点.
 也记录了到这个节点的其他的节点.
 
*/
public class NodeStation extends EntityMM
{

		
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
		return this.GetValIntByKey(NodeStationAttr.FK_Node);
	}
	public final void setFK_Node(int value)
	{
		this.SetValByKey(NodeStationAttr.FK_Node, value);
	}
	public final String getFK_StationT()
	{
		return this.GetValRefTextByKey(NodeStationAttr.FK_Station);
	}
	/** 
	 工作岗位
	 
	*/
	public final String getFK_Station()
	{
		return this.GetValStringByKey(NodeStationAttr.FK_Station);
	}
	public final void setFK_Station(String value)
	{
		this.SetValByKey(NodeStationAttr.FK_Station, value);
	}

		///#endregion


		
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
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_NodeStation", "节点岗位");

		map.AddTBIntPK(NodeStationAttr.FK_Node, 0,"节点", false,false);

		if (BP.WF.Glo.getOSModel() == BP.Sys.OSModel.OneOne)
		{
			map.AddDDLEntitiesPK(NodeStationAttr.FK_Station, null, "工作岗位", new BP.Port.Stations(), true);
		}
		else
		{
 // #warning ,这里为了方便用户选择，让分组都统一采用了枚举类型. edit zhoupeng. 2015.04.28. 注意jflow也要修改.
			map.AddDDLEntitiesPK(NodeStationAttr.FK_Station, null, "工作岗位", new BP.GPM.Stations(), true);
		}
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

		///#endregion

}