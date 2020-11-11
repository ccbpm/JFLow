package bp.wf.port;

import bp.da.*;
import bp.en.*;
import bp.wf.*;
import bp.port.*;
import bp.web.*;
import bp.wf.*;
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

	public static AuthorWay forValue(int value) throws Exception
	{
		return values()[value];
	}
}