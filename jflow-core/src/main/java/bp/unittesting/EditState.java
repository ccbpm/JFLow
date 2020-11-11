package bp.unittesting;

public enum EditState
{
	/** 
	 已经完成
	*/
	Passed,
	/** 
	  编辑中
	*/
	Editing,
	/** 
	 未完成
	*/
	UnOK;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static EditState forValue(int value) throws Exception
	{
		return values()[value];
	}
}