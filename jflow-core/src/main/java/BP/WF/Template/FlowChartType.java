package BP.WF.Template;

import BP.WF.*;

/** 
 图像类型
*/
public enum FlowChartType
{
	/** 
	 几何图形
	*/
	Geometrical,
	/** 
	 头像图形
	*/
	Icon;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static FlowChartType forValue(int value)
	{
		return values()[value];
	}
}