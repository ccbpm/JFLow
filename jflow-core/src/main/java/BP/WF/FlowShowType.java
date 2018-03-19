package BP.WF;


public enum FlowShowType
{
	/** 
	 当前工作
	 
	*/
	MyWorks,
	/** 
	 新建
	 
	*/
	WorkNew,
	/** 
	 工作步骤
	 
	*/
	WorkStep,
	/** 
	 工作图片
	 
	*/
	WorkImages;

	public int getValue()
	{
		return this.ordinal();
	}

	public static FlowShowType forValue(int value)
	{
		return values()[value];
	}
}