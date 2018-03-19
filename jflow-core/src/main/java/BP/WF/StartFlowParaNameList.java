package BP.WF;

/** 
 发起流程参数列表,为了防止参数错误.
*/
public class StartFlowParaNameList
{

		///#region 功能的参数标记.
	/** 
	 是否删除草稿
	*/
	public static final String IsDeleteDraft = "IsDeleteDraft";
	/** 
	 指定的表名称.
	*/
	public static final String FromTableName = "FromTableName";
	/** 
	 主键
	*/
	public static final String FromTablePK = "FromTablePK";
	/** 
	 主键值
	*/
	public static final String FromTablePKVal = "FromTablePKVal";
	public static final String CopyFormWorkID = "CopyFormWorkID";
	public static final String CopyFormNode = "CopyFormNode";
	public static final String PFlowNo = "PFlowNo";
	public static final String PNodeID = "PNodeID";
	public static final String PWorkID = "PWorkID";
	public static final String PFID = "PFID";
	/** 
	 发起人
	*/
	public static final String PEmp = "PEmp";
	public static final String JumpToNode = "JumpToNode";
	public static final String JumpToEmp = "JumpToEmp";
}