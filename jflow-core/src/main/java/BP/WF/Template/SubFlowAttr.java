package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 子流程属性
*/
public class SubFlowAttr extends BP.En.EntityOIDNameAttr
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	/** 
	 标题
	*/
	public static final String SubFlowNo = "SubFlowNo";
	/** 
	 流程名称
	*/
	public static final String SubFlowName = "SubFlowName";
	/** 
	 顺序号
	*/
	public static final String Idx = "Idx";
	/** 
	 显示在那里？
	*/
	public static final String YGWorkWay = "YGWorkWay";
	/** 
	 主流程编号
	*/
	public static final String FK_Flow = "FK_Flow";
	/** 
	 节点ID
	*/
	public static final String FK_Node = "FK_Node";
	/** 
	 表达式类型
	*/
	public static final String ExpType = "ExpType";
	/** 
	 条件表达式
	*/
	public static final String CondExp = "CondExp";
	/** 
	 调用时间
	*/
	public static final String InvokeTime = "InvokeTime";
	/** 
	 越轨子流程退回类型
	*/
	public static final String YBFlowReturnRole = "YBFlowReturnRole";
	/** 
	 要退回的节点
	*/
	public static final String ReturnToNode = "ReturnToNode";
	/** 
	 子流程类型
	*/
	public static final String SubFlowType = "SubFlowType";
	/** 
	 子流程模式
	*/
	public static final String SubFlowModel = "SubFlowModel";
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 子流程的发起.
	/** 
	 如果当前为子流程，仅仅只能被调用1次，不能被重复调用。
	*/
	public static final String StartOnceOnly = "StartOnceOnly";
	/** 
	 如果当前为子流程，只有该流程结束后才可以重新启用
	*/
	public static final String CompleteReStart = "CompleteReStart";
	/** 
	 是否启动
	*/
	public static final String IsEnableSpecFlowStart = "IsEnableSpecFlowStart";
	/** 
	 指定的流程启动后，才能启动该子流程.
	*/
	public static final String SpecFlowStart = "SpecFlowStart";
	/** 
	 备注
	*/
	public static final String SpecFlowStartNote = "SpecFlowStartNote";
	/** 
	 是否启用
	*/
	public static final String IsEnableSpecFlowOver = "IsEnableSpecFlowOver";
	/** 
	 指定的子流程结束后，才能启动该子流程.
	*/
	public static final String SpecFlowOver = "SpecFlowOver";
	/** 
	 备注
	*/
	public static final String SpecFlowOverNote = "SpecFlowOverNote";
	/** 
	 是否启用按指定的SQL启动
	*/
	public static final String IsEnableSQL = "IsEnableSQL";
	/** 
	 SQL语句
	*/
	public static final String SpecSQL = "SpecSQL";
	/** 
	 是否启动按指定平级子流程节点
	*/
	public static final String IsEnableSameLevelNode = "IsEnableSameLevelNode";
	/** 
	 平级子流程节点
	*/
	public static final String SameLevelNode = "SameLevelNode";

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 自动启动子流程：发送规则.
	*/
	public static final String SendModel = "SendModel";

}