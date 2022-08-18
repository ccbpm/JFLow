package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.wf.port.*;
import bp.*;
import bp.wf.*;
import java.util.*;

/** 
 节点部门
 节点的工作部门有两部分组成.	 
 记录了从一个节点到其他的多个节点.
 也记录了到这个节点的其他的节点.
*/
public class NodeDept extends EntityMyPK
{

		///#region 基本属性
	/** 
	节点
	*/
	public final int getFK_Node() throws Exception
	{
		return this.GetValIntByKey(NodeDeptAttr.FK_Node);
	}
	public final void setFK_Node(int value)  throws Exception
	 {
		this.SetValByKey(NodeDeptAttr.FK_Node, value);
	}
	/** 
	 工作部门
	*/
	public final String getFK_Dept() throws Exception
	{
		return this.GetValStringByKey(NodeDeptAttr.FK_Dept);
	}
	public final void setFK_Dept(String value)  throws Exception
	 {
		this.SetValByKey(NodeDeptAttr.FK_Dept, value);
	}
	@Override
	public UAC getHisUAC() {
		UAC uac = new UAC();
		uac.OpenAll();
		return super.getHisUAC();
	}

		///#endregion


		///#region 构造方法
	/** 
	 节点部门
	*/
	public NodeDept()  {
	}
	/** 
	 重写基类方法
	*/
	@Override
	public bp.en.Map getEnMap() {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_NodeDept", "节点部门");

		map.setDepositaryOfEntity( Depositary.None);
		map.setDepositaryOfMap( Depositary.Application);

		map.IndexField = NodeEmpAttr.FK_Node;

		map.AddMyPK();
		map.AddTBInt(NodeStationAttr.FK_Node, 0, "节点", false, false);

		map.AddDDLEntities(NodeDeptAttr.FK_Dept,null,"部门",new bp.port.Depts(),true);

		this.set_enMap(map);

		return this.get_enMap();
	}
	@Override
	protected  boolean beforeUpdateInsertAction() throws Exception
	{
		this.setMyPK(this.getFK_Node() + "_" + this.getFK_Dept());
		return super.beforeUpdateInsertAction();
	}

}