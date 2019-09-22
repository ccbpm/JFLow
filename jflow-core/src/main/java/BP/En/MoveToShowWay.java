package BP.En;

import BP.DA.*;
import BP.Sys.*;
import BP.Web.Controls.*;
import java.time.*;
import java.math.*;

/** 
 移动到显示方式
*/
public enum MoveToShowWay
{
	/** 
	 不显示
	*/
	None,
	/** 
	 下拉列表
	*/
	DDL,
	/** 
	 平铺
	*/
	Panel;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static MoveToShowWay forValue(int value)
	{
		return values()[value];
	}
}