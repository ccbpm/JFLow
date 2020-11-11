package bp.ccbill.template;

import bp.da.*;
import bp.en.*;
import bp.sys.*;
import bp.wf.template.*;
import bp.wf.*;
import bp.port.*;
import bp.ccbill.*;
import java.util.*;

/** 
  控制模型-属性
*/
public class CtrlModelAttr extends EntityMyPKAttr
{
	/** 
	 表单ID
	*/
	public static final String FrmID = "FrmID";
	/** 
	 控制对象
	*/
	public static final String CtrlObj = "CtrlObj";
	/** 
	 所有的人
	*/
	public static final String IsEnableAll = "IsEnableAll";
	public static final String IsEnableStation = "IsEnableStation";
	public static final String IsEnableDept = "IsEnableDept";
	public static final String IsEnableMyDept = "IsEnableMyDept";
	public static final String IsEnableUser = "IsEnableUser";
	public static final String IDOfUsers = "IDOfUsers";
	public static final String IDOfStations = "IDOfStations";
	public static final String IDOfDepts = "IDOfDepts";
}