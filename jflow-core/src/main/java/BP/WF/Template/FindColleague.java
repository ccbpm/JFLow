package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.Web.*;
import BP.GPM.*;
import BP.Sys.*;
import BP.WF.*;
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

	public static FindColleague forValue(int value)
	{
		return values()[value];
	}
}