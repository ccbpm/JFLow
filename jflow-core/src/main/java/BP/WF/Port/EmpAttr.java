package BP.WF.Port;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 工作人员属性
*/
public class EmpAttr extends BP.En.EntityNoNameAttr
{

		///#region 基本属性
	/** 
	 部门
	*/
	public static final String FK_Dept = "FK_Dept";
	///// <summary>
	///// 单位
	///// </summary>
	//public const string FK_Unit = "FK_Unit";
	/** 
	 密码
	*/
	public static final String Pass = "Pass";
	/** 
	 SID
	*/
	public static final String SID = "SID";
	/** 
	 手机号码
	*/
	public static final String Tel = "Tel";

		///#endregion
}