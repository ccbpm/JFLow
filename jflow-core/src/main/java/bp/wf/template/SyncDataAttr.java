package bp.wf.template;

import bp.en.*;

/** 
 流程数据同步 属性
*/
public class SyncDataAttr extends EntityMyPKAttr
{
	/**流程编号
	*/
	public static final String FlowNo = "FlowNo";
	//同步类型.
	public static final String SyncType = "SyncType";
	//数据库链接URL
	public static final String DBSrc = "DBSrc";
	//API链接URL
	public static final String APIUrl = "APIUrl";
	//备注.
	public static final String Note = "Note";
	//表
	public static final String PTable = "PTable";
	//表的主键.
	public static final String TablePK = "TablePK";
	//查询表
	public static final String SQLTables = "SQLTables";
	//查询字段.
	public static final String SQLFields = "SQLFields";
}
