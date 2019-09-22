package BP.WF.Port;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.Port.*;
import BP.Web.*;
import BP.WF.*;
import java.util.*;
import java.time.*;

/** 
 授权方式
*/
public enum AuthorWay
{
	/** 
	 不授权
	*/
	None,
	/** 
	 全部授权
	*/
	All,
	/** 
	 指定流程授权
	*/
	SpecFlows;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static AuthorWay forValue(int value)
	{
		return values()[value];
	}
}