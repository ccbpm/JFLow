package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.wf.port.*;
import bp.wf.*;
import java.util.*;

/** 
 节点人员
 节点的到人员有两部分组成.	 
 记录了从一个节点到其他的多个节点.
 也记录了到这个节点的其他的节点.
*/
public class NodeEmp extends EntityMM
{

		///基本属性
	/** 
	节点
	 * @throws Exception 
	*/
	public final int getFK_Node() throws Exception
	{
		return this.GetValIntByKey(NodeEmpAttr.FK_Node);
	}
	public final void setFK_Node(int value) throws Exception
	{
		this.SetValByKey(NodeEmpAttr.FK_Node,value);
	}
	/** 
	 到人员
	*/
	public final String getFK_Emp()throws Exception
	{
		return this.GetValStringByKey(NodeEmpAttr.FK_Emp);
	}
	public final void setFK_Emp(String value) throws Exception
	{
		this.SetValByKey(NodeEmpAttr.FK_Emp,value);
	}
	public final String getFK_EmpT()throws Exception
	{
		return this.GetValRefTextByKey(NodeEmpAttr.FK_Emp);
	}

		///


		///构造方法
	/** 
	 节点人员
	*/
	public NodeEmp()
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

		Map map = new Map("WF_NodeEmp", "节点人员");
		map.IndexField = NodeEmpAttr.FK_Node;

		map.AddTBIntPK(NodeEmpAttr.FK_Node,0,"Node",true,true);
		map.AddDDLEntitiesPK(NodeEmpAttr.FK_Emp, null, "到人员", new Emps(), true);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///
}