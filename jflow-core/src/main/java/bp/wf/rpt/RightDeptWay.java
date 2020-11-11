package bp.wf.rpt;

import bp.da.*;
import bp.port.*;
import bp.en.*;
import bp.wf.*;
import bp.sys.*;
import bp.wf.*;
import java.util.*;

/** 
 部门数据权限控制方式
*/
public enum RightDeptWay
{
	/** 
	 所有部门的数据.
	*/
	All,
	/** 
	 本部门的数据.
	*/
	SelfDept,
	/** 
	 本部门与子部门的数据.
	*/
	SelfDeptAndSubDepts,
	/** 
	 指定部门的数据.
	*/
	SpecDepts;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static RightDeptWay forValue(int value) throws Exception
	{
		return values()[value];
	}
}