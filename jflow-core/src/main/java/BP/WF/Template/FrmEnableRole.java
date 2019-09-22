package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 表单启用规则
*/
public enum FrmEnableRole
{
	/** 
	 始终启用
	*/
	Allways,
	/** 
	 有数据时启用
	*/
	WhenHaveData,
	/** 
	 有参数时启用
	*/
	WhenHaveFrmPara,
	/** 
	 按表单的字段表达式
	*/
	ByFrmFields,
	/** 
	 按SQL表达式
	*/
	BySQL,
	/** 
	 不启用
	*/
	Disable,
	/** 
	 按岗位
	*/
	ByStation,
	/** 
	 按部门
	*/
	ByDept;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static FrmEnableRole forValue(int value)
	{
		return values()[value];
	}
}