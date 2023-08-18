package bp.ta;

import bp.en.EntityMyPKAttr;

/**
 轨迹属性
*/
public class TrackAttr extends EntityMyPKAttr
{
		///#region 基本属性
	/** 
	 标题
	*/
	public static final String TaskID = "TaskID";
	/** 
	 轨迹内容
	*/
	public static final String PrjNo = "PrjNo";
	/** 
	 按表单字段轨迹
	*/
	public static final String EmpNo = "EmpNo";
	/** 
	 表单字段
	*/
	public static final String EmpName = "EmpName";
	/** 
	 是否启用轨迹到角色
	*/
	public static final String ActionType = "ActionType";
	/** 
	 按照角色计算方式
	*/
	public static final String ActionName = "ActionName";
	/** 
	 是否轨迹到部门
	*/
	public static final String Docs = "Docs";
	/** 
	 是否轨迹到人员
	*/
	public static final String RDT = "RDT";
	/** 
	 是否启用按照SQL轨迹.
	*/
	public static final String RecNo = "RecNo";
	/** 
	 要轨迹的SQL
	*/
	public static final String RecName = "RecName";
		///#endregion

	public static final String PrjSta = "PrjSta";
	public static final String PRI = "PRI";
	public static final String WCL = "WCL";

	public static final String UseHH = "UseHH";
	public static final String UseMM = "UseMM";

}
