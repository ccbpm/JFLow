package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.WF.Flows;


/** 
 延续子流程.	 
 
*/
public class NodeYGFlow extends EntityOID
{
public static final String getCondExp = null;
	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	/** 
	 UI界面上的访问控制
	 
	*/
	@Override
	public UAC getHisUAC()
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
		return this.GetValStringByKey(NodeYGFlowAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)
	{
		SetValByKey(NodeYGFlowAttr.FK_Flow, value);
	}
	/** 
	 流程名称
	 
	*/
	public final String getFlowName()
	{
		return this.GetValRefTextByKey(NodeYGFlowAttr.FK_Flow);
	}
	//条件表达式
	public final String getCondExp(){
		return this.GetValStringByKey(NodeYGFlowAttr.CondExp);
	}
	public final void setCondExp(String str){
		this.SetValByKey(NodeYGFlowAttr.CondExp, str);
	}
	//表达式类型
	public final ConnDataFrom getExpType(){
		return  ConnDataFrom.forValue(this.GetValIntByKey(NodeYGFlowAttr.ExpType));	
	}
	public final void setExpType(int value){
		SetValByKey(NodeYGFlowAttr.ExpType, (int)value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
	/** 
	 延续子流程
	 
	*/
	public NodeYGFlow()
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



            map.AddTBIntPKOID();                 
            map.AddTBInt(NodeYGFlowAttr.FK_Node, 0, "节点", false, true);

            map.AddDDLEntities(NodeYGFlowAttr.FK_Flow, null, "延续子流程", new Flows(), false);

            //map.AddDDLSysEnum(NodeYGFlowAttr.YGWorkWay, 1, "工作方式", true, true, NodeYGFlowAttr.YGWorkWay,
            //    "@0=停止当前节点等待延续子流程运行完毕后该节点自动向下运行@1=启动延续子流程运行到下一步骤上去");

            map.AddTBInt(NodeYGFlowAttr.Idx, 0, "显示顺序", true, false);
            map.AddDDLSysEnum(NodeYGFlowAttr.ExpType, 3, "表达式类型", true, true, NodeYGFlowAttr.ExpType,
               "@3=按照SQL计算@4=按照参数计算");

            map.AddTBString(NodeYGFlowAttr.CondExp, null, "条件表达式", true, false, 0, 500, 150, true);

            //@du.
            map.AddDDLSysEnum(NodeYGFlowAttr.YBFlowReturnRole, 0, "退回方式", true, true, NodeYGFlowAttr.YBFlowReturnRole,
              "@0=不能退回@1=退回到父流程的开始节点@2=退回到父流程的任何节点@3=退回父流程的启动节点@4=可退回到指定的节点");

           // map.AddTBString(NodeYGFlowAttr.ReturnToNode, null, "要退回的节点", true, false, 0, 200, 150, true);
            map.AddDDLSQL(NodeYGFlowAttr.ReturnToNode, "0", "要退回的节点",
                "SELECT NodeID AS No, Name FROM WF_Node WHERE FK_Flow IN (SELECT FK_Flow FROM WF_Node WHERE NodeID=@FK_Node; )",true);
            
            

            this.set_enMap(map);
            return this.get_enMap();
        }
}
