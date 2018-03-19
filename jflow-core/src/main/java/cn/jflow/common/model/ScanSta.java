package cn.jflow.common.model;

public enum ScanSta
{
	Working,
	Pause,
	Stop;

	public int getValue()
	{
		return this.ordinal();
	}

	public static ScanSta forValue(int value)
	{
		return values()[value];
	}
}