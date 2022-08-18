package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.port.*;
import bp.*;
import bp.wf.*;
import java.util.*;

/** 
 可退回的节点
 节点的退回到有两部分组成.	 
 记录了从一个节点到其他的多个节点.
 也记录了到这个节点的其他的节点.
*/
public class NodeReturn extends EntityMM
{

		///#region 基本属性
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()  {
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	/** 
	退回到
	*/
	public final int getReturnTo() throws Exception
	{
		return this.GetValIntByKey(NodeReturnAttr.ReturnTo);
	}
	public final void setReturnTo(int value)  throws Exception
	 {
		this.SetValByKey(NodeReturnAttr.ReturnTo, value);
	}
	/** 
	 工作流程
	*/
	public final int getFK_Node() throws Exception
	{
		return this.GetValIntByKey(NodeReturnAttr.FK_Node);
	}
	public final void setFK_Node(int value)  throws Exception
	 {
		this.SetValByKey(NodeReturnAttr.FK_Node, value);
	}

		///#endregion


		///#region 构造方法
	/** 
	 可退回的节点
	*/
	public NodeReturn()  {
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

		Map map = new Map("WF_NodeReturn", "可退回的节点");

		map.AddTBIntPK(NodeReturnAttr.FK_Node, 0, "节点", true, true);
		map.AddTBIntPK(NodeReturnAttr.ReturnTo, 0, "退回到", true, true);
		//    map.AddTBString(NodeReturnAttr.Dots, null, "轨迹信息", true, true,0,300,0,false);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}