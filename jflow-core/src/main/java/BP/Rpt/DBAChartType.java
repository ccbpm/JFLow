package BP.Rpt;

public enum DBAChartType
{
	Table, Column, Pie, Line;
	
	public int getValue()
	{
		return this.ordinal();
	}
	
	public static DBAChartType forValue(int value)
	{
		return values()[value];
	}
}
