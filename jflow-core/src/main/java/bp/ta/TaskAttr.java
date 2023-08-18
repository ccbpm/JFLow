package bp.ta;

import bp.en.EntityOIDAttr;

/** 
 任务属性
*/
public class TaskAttr extends EntityOIDAttr
{
		///#region 基本属性
	/** 
	 标题
	*/
	public static final String OID = "OID";
	/** 
	 任务内容
	*/
	public static final String Title = "Title";
	/** 
	 按表单字段任务
	*/
	public static final String EmpNo = "EmpNo";
	/** 
	 表单字段
	*/
	public static final String EmpName = "EmpName";
	/** 
	 是否启用任务到角色
	*/
	public static final String TaskSta = "TaskSta";
	/** 
	 按照角色计算方式
	*/
	public static final String IsRead = "IsRead";
	/** 
	 即时消息
	*/
	public static final String NowMsg = "NowMsg";
	/** 
	 最近活动日期.
	*/
	public static final String ADT = "ADT";
//C# TO JAVA CONVERTER TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TASK: There is no preprocessor in Java:
		///#region 项目信息.
	/** 
	 是否任务到部门
	*/
	public static final String PrjNo = "PrjNo";
	/** 
	 是否任务到人员
	*/
	public static final String PrjName = "PrjName";
	/** 
	 是否启用按照SQL任务.
	*/
	public static final String StarterNo = "StarterNo";
	/** 
	 要任务的SQL
	*/
	public static final String StarterName = "StarterName";
	public static final String PrjSta = "PrjSta";
	public static final String PRI = "PRI";
	public static final String WCL = "WCL";
	public static final String RDT = "RDT";
//C# TO JAVA CONVERTER TASK: There is no preprocessor in Java:
		///#endregion 项目信息.
	public static final String PTaskID = "PTaskID";

	public static final String SenderNo = "SenderNo";
	public static final String SenderName = "SenderName";

	public static final String NodeNo = "NodeNo";
	public static final String NodeName = "NodeName";


}
