package BP.WF;

import BP.En.EntityMyPK;

public  class AthUnReadLogAttr {
	
	
	//region 基本属性
	/// <summary>
	/// 工作ID
	/// </summary>
	public static final  String WorkID="WorkID";
    /// <summary>
    /// 流程编号
    /// </summary>
    public static final  String FK_Flow = "FK_Flow";
    /// <summary>
    /// 流程类别
    /// </summary>
    public static final  String FlowName = "FlowName";
	/// <summary>
	/// 删除人员
	/// </summary>
	public static final  String FK_Emp="FK_Emp";
	/// <summary>
	/// 删除原因
	/// </summary>
	public static final  String BeiZhu="BeiZhu";
    /// <summary>
    /// 删除日期
    /// </summary>
    public static final  String SendDT = "SendDT";
    /// <summary>
    /// 删除人员
    /// </summary>
    public static final  String FK_EmpDept = "FK_EmpDept";
    /// <summary>
    /// 删除人员名称
    /// </summary>
    public static final  String FK_EmpDeptName = "FK_EmpDeptName";
    /// <summary>
    /// 第几周？
    /// </summary>
    public static final  String WeekNum = "WeekNum";
    /// <summary>
    /// 隶属年月？
    /// </summary>
    public static final  String FK_Node = "FK_Node";
    /// <summary>
    /// 节点名称
    /// </summary>
    public static final  String NodeName = "NodeName";
	//endregion

    //region 流程属性
    /// <summary>
    /// 标题
    /// </summary>
    public static final  String Title = "Title";
    /// <summary>
    /// 参与人员
    /// </summary>
    public static final  String FlowEmps = "FlowEmps";
    /// <summary>
    /// 流程ID
    /// </summary>
    public static final  String FID = "FID";
    /// <summary>
    /// 发起年月
    /// </summary>
    public static final  String FK_NY = "FK_NY";
    /// <summary>
    /// 发起人ID
    /// </summary>
    public static final  String FlowStarter = "FlowStarter";
    /// <summary>
    /// 发起日期
    /// </summary>
    public static final  String FlowStartSendDT = "FlowStartSendDT";
    /// <summary>
    /// 发起人部门ID
    /// </summary>
    public static final  String FK_Dept = "FK_Dept";
    /// <summary>
    /// 数量
    /// </summary>
    public static final  String MyNum = "MyNum";
    /// <summary>
    /// 结束人
    /// </summary>
    public static final  String FlowEnder = "FlowEnder";
    /// <summary>
    /// 最后活动日期
    /// </summary>
    public static final  String FlowEnderSendDT = "FlowEnderSendDT";
    /// <summary>
    /// 跨度
    /// </summary>
    public static final  String FlowDaySpan = "FlowDaySpan";
    /// <summary>
    /// 结束节点
    /// </summary>
    public static final  String FlowEndNode = "FlowEndNode";
    /// <summary>
    /// 父流程WorkID
    /// </summary>
    public static final  String PWorkID = "PWorkID";
    /// <summary>
    /// 父流程编号
    /// </summary>
    public static final  String PFlowNo = "PFlowNo";

}
