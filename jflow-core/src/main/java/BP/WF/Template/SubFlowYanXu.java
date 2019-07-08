package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.WF.Flows;


/** 
 延续子流程.	 
 
*/
public class SubFlowYanXu extends EntityMyPK
{
public static final String getCondExp = null;
	
	/** 
	 UI界面上的访问控制
	 * @throws Exception 
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		uac.IsInsert=false;
		return uac;
	}
	/** 
	 流程编号
	 
	*/
	public final String getFK_Flow()
	{
		return this.GetValStringByKey(SubFlowYanXuAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)
	{
		SetValByKey(SubFlowYanXuAttr.FK_Flow, value);
	}
	/** 
	 流程名称
	 
	*/
	public final String getFlowName()
	{
		return this.GetValStringByKey(SubFlowYanXuAttr.FK_Flow);
	}
	/** 
	 条件表达式.
	 
	*/
	public final String getCondExp()
	{
		return this.GetValStringByKey(SubFlowYanXuAttr.CondExp);
	}
	public final void setCondExp(String value)
	{
		SetValByKey(SubFlowYanXuAttr.CondExp, value);
	}
	/** 
	 表达式类型
	 
	*/
	public final ConnDataFrom getExpType()
	{
		return ConnDataFrom.forValue(this.GetValIntByKey(SubFlowYanXuAttr.ExpType));
	}
	public final void setExpType(ConnDataFrom value)
	{
		SetValByKey(SubFlowYanXuAttr.ExpType, value.getValue());
	}
	public final String getFK_Node()
	{
		return this.GetValStringByKey(SubFlowYanXuAttr.FK_Node);
	}
	public final void setFK_Node(String value)
	{
		SetValByKey(SubFlowYanXuAttr.FK_Node, value);
	}

	/** 
	 延续子流程
	 
	*/
	public SubFlowYanXu()
	{
	}
	/** 
	 重写基类方法
	 
	*/
	@Override
    public  Map getEnMap()
    {
        
            if (this.get_enMap() != null)
                return this.get_enMap();

            Map map = new Map("WF_NodeSubFlow", "延续子流程");

    		map.AddMyPK();

    		map.AddTBInt(SubFlowYanXuAttr.FK_Node, 0, "节点", false, true);

    		map.AddDDLSysEnum(SubFlowYanXuAttr.SubFlowType, 2, "子流程类型", true, false, SubFlowYanXuAttr.SubFlowType, "@0=手动启动子流程@1=触发启动子流程@2=延续子流程");

    		map.AddTBString(SubFlowYanXuAttr.FK_Flow, null, "子流程编号", true, true, 0, 10, 150, false);
    		map.AddTBString(SubFlowYanXuAttr.FlowName, null, "子流程名称", true, true, 0, 200, 150, false);

    		map.AddDDLSysEnum(SubFlowYanXuAttr.ExpType, 3, "表达式类型", true, true, SubFlowYanXuAttr.ExpType, "@3=按照SQL计算@4=按照参数计算");

    		map.AddTBString(SubFlowYanXuAttr.CondExp, null, "条件表达式", true, false, 0, 500, 150, true);

    		map.AddDDLSysEnum(SubFlowYanXuAttr.YBFlowReturnRole, 0, "退回方式", true, true, SubFlowYanXuAttr.YBFlowReturnRole, "@0=不能退回@1=退回到父流程的开始节点@2=退回到父流程的任何节点@3=退回父流程的启动节点@4=可退回到指定的节点");

    		map.AddDDLSQL(SubFlowYanXuAttr.ReturnToNode, "0", "要退回的节点", "SELECT NodeID AS No, Name FROM WF_Node WHERE FK_Flow IN (SELECT FK_Flow FROM WF_Node WHERE NodeID=@FK_Node; )", true);

    		map.AddTBInt(SubFlowYanXuAttr.Idx, 0, "显示顺序", true, false);
    		
            this.set_enMap(map);
            return this.get_enMap();
    }
	
	/** 
	 设置主键
	 
	 @return 
	 * @throws Exception 
	*/
	@Override
	protected boolean beforeInsert() throws Exception
	{
		this.setMyPK(this.getFK_Node() + "_" + this.getFK_Flow() + "_2");
		return super.beforeInsert();
	}

	/** 
	* 上移
	*@return 
	*/
	public final String DoUp()
	{
		this.DoOrderUp(SubFlowYanXuAttr.FK_Node, this.getFK_Node(), SubFlowYanXuAttr.SubFlowType, "2", SubFlowYanXuAttr.Idx);
		return "执行成功";
	}
	/** 
	 *下移
	 *@return 
	*/
	public final String DoDown()
	{
		this.DoOrderDown(SubFlowYanXuAttr.FK_Node, this.getFK_Node(), SubFlowYanXuAttr.SubFlowType, "2", SubFlowYanXuAttr.Idx);
		return "执行成功";
	}

}
