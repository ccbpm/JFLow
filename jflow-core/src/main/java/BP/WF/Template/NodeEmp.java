package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 节点人员
 节点的到人员有两部分组成.	 
 记录了从一个节点到其他的多个节点.
 也记录了到这个节点的其他的节点.
*/
public class NodeEmp extends EntityMM
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	/** 
	节点
	*/
	public final int getFK_Node()
	{
		return this.GetValIntByKey(NodeEmpAttr.FK_Node);
	}
	public final void setFK_Node(int value)
	{
		this.SetValByKey(NodeEmpAttr.FK_Node,value);
	}
	/** 
	 到人员
	*/
	public final String getFK_Emp()
	{
		return this.GetValStringByKey(NodeEmpAttr.FK_Emp);
	}
	public final void setFK_Emp(String value)
	{
		this.SetValByKey(NodeEmpAttr.FK_Emp,value);
	}
	public final String getFK_EmpT()
	{
		return this.GetValRefTextByKey(NodeEmpAttr.FK_Emp);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
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
	public Map getEnMap()
	{
		if (this._enMap != null)
		{
			return this._enMap;
		}

		Map map = new Map("WF_NodeEmp", "节点人员");
		map.IndexField = NodeEmpAttr.FK_Node;

		map.AddTBIntPK(NodeEmpAttr.FK_Node,0,"Node",true,true);
		map.AddDDLEntitiesPK(NodeEmpAttr.FK_Emp, null, "到人员", new Emps(), true);

		this._enMap = map;
		return this._enMap;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}