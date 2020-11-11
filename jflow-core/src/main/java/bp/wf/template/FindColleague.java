package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.web.*;
import bp.sys.*;
import bp.wf.*;
import java.util.*;

/** 
 查找类型
*/
public enum FindColleague
{
	/** 
	 所有 
	*/
	All,
	/** 
	 指定职务
	*/
	SpecDuty,
	/** 
	 指定岗位
	*/
	SpecStation;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static FindColleague forValue(int value) throws Exception
	{
		return values()[value];
	}
}