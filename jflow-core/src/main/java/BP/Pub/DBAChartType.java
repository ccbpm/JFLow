package BP.Pub;

import BP.DA.*;
import BP.En.*;
import BP.Web.Controls.*;
import BP.Web.*;
import BP.Sys.*;

/** 
 显示类型.
*/
public enum DBAChartType
{
	Table,
	Column,
	Pie,
	Line;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static DBAChartType forValue(int value)
	{
		return values()[value];
	}
}