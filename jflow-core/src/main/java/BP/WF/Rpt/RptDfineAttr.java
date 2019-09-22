package BP.WF.Rpt;

import BP.DA.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.Sys.*;
import BP.WF.Data.*;
import BP.WF.*;
import java.util.*;

/** 
 报表定义
*/
public class RptDfineAttr extends EntityNoNameAttr
{
	/** 
	 查询的物理表
	*/
	public static final String PTable = "PTable";
	/** 
	 备注
	*/
	public static final String Note = "Note";
	/** 
	 流程编号
	*/
	public static final String FK_Flow = "FK_Flow";
	/** 
	 部门查询权限控制规则
	*/
	public static final String MyDeptRole = "MyDeptRole";
}