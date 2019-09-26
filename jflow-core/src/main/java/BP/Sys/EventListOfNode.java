package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.Web.*;
import java.util.*;
import java.io.*;
import java.time.*;
import java.math.*;

/** 
 事件标记列表
*/
public class EventListOfNode extends FrmEventList
{

		///#region 节点事件
	/** 
	 创建工作ID
	*/
	public static final String CreateWorkID = "CreateWorkID";
	/** 
	 节点发送前
	*/
	public static final String SendWhen = "SendWhen";
	/** 
	 工作到达
	*/
	public static final String WorkArrive = "WorkArrive";
	/** 
	 节点发送成功后
	*/
	public static final String SendSuccess = "SendSuccess";
	/** 
	 节点发送失败后
	*/
	public static final String SendError = "SendError";
	/** 
	 当节点退回前
	*/
	public static final String ReturnBefore = "ReturnBefore";
	/** 
	 当节点退后
	*/
	public static final String ReturnAfter = "ReturnAfter";
	/** 
	 当节点撤销发送前
	*/
	public static final String UndoneBefore = "UndoneBefore";
	/** 
	 当节点撤销发送后
	*/
	public static final String UndoneAfter = "UndoneAfter";
	/** 
	 当前节点移交后
	*/
	public static final String ShitAfter = "ShitAfter";
	/** 
	 当节点加签后
	*/
	public static final String AskerAfter = "AskerAfter";
	/** 
	 当节点加签答复后
	*/
	public static final String AskerReAfter = "AskerReAfter";
	/** 
	 队列节点发送后
	*/
	public static final String QueueSendAfter = "QueueSendAfter";
	/** 
	 节点打开后.
	*/
	public static final String WhenReadWork = "WhenReadWork";
	/** 
	 节点预警
	*/
	public static final String NodeWarning = "NodeWarning";
	/** 
	 节点逾期
	*/
	public static final String NodeOverDue = "NodeOverDue";
	/** 
	 流程预警
	*/
	public static final String FlowWarning = "FlowWarning";
	/** 
	 流程逾期
	*/
	public static final String FlowOverDue = "FlowOverDue";



		///#endregion 节点事件


		///#region 流程事件
	/** 
	 当创建workid的时候.
	*/
	public static final String FlowOnCreateWorkID = "FlowOnCreateWorkID";
	/** 
	 流程完成时.
	*/
	public static final String FlowOverBefore = "FlowOverBefore";
	/** 
	 结束后.
	*/
	public static final String FlowOverAfter = "FlowOverAfter";
	/** 
	 流程删除前
	*/
	public static final String BeforeFlowDel = "BeforeFlowDel";
	/** 
	 流程删除后
	*/
	public static final String AfterFlowDel = "AfterFlowDel";

		///#endregion 流程事件
}